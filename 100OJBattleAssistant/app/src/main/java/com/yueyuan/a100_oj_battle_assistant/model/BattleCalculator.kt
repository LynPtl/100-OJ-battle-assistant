package com.yueyuan.a100_oj_battle_assistant.model

import com.yueyuan.a100_oj_battle_assistant.data.CharacterProfile
import kotlin.math.max

data class BattleResult(
    val recommendation: String,
    val explanation: String,
    val processDetails: String,
    val defendProbability: String,
    val evadeProbability: String
)

data class ProbabilityAnalysis(
    val survivalProb: Double,
    val expectedDamage: Double,
    val expectedResistance: Double,
    val expectedSurvivalHP: Double,
    val damageDistribution: Map<Int, Int>
)

enum class Strategy {
    SURVIVAL, DAMAGE, RESISTANCE
}

class BattleCalculator {

    fun calculateRecommendation(
        profile: CharacterProfile,
        currentHP: Int,
        opponentAttack: Int,
        strategy: Strategy,
        customDefMod: Int,
        customEvdMod: Int,
        language: String
    ): BattleResult {
        
        val finalDef = profile.def + customDefMod
        val finalEvd = profile.evd + customEvdMod
        
        // Calculate defense damage for all dice rolls (1-6)
        val defenseDamages = (1..6).map { roll ->
            max(1, opponentAttack - (finalDef + roll))
        }
        
        val minDefenseDamage = defenseDamages.minOrNull() ?: 0
        val maxDefenseDamage = defenseDamages.maxOrNull() ?: 0
        val expectedDefenseDamage = defenseDamages.average()
        
        // Calculate evasion success probability
        val evadeSuccessRolls = (1..6).count { roll ->
            (finalEvd + roll) > opponentAttack
        }
        val evadeSuccessProb = evadeSuccessRolls / 6.0
        val evadeFailDamage = opponentAttack
        val expectedEvadeDamage = (1 - evadeSuccessProb) * evadeFailDamage
        
        // Calculate survival probabilities
        val defenseAnalysis = calculateDefenseAnalysis(profile, currentHP, defenseDamages)
        val evadeAnalysis = calculateEvadeAnalysis(profile, currentHP, evadeSuccessProb, evadeFailDamage)
        
        // Generate process details
        val processDetails = generateProcessDetails(
            finalDef, finalEvd, minDefenseDamage, maxDefenseDamage,
            expectedDefenseDamage, expectedEvadeDamage,
            defenseAnalysis.expectedResistance, evadeAnalysis.expectedResistance,
            language
        )
        
        // Generate probability strings
        val defendProbStr = generateDefendProbabilityString(defenseAnalysis, language)
        val evadeProbStr = generateEvadeProbabilityString(evadeAnalysis, evadeSuccessRolls, evadeFailDamage, language)
        
        // Determine recommendation based on strategy
        val recommendation = when {
            isDoomed(minDefenseDamage, currentHP, evadeSuccessProb, evadeFailDamage) -> 
                getActionText("doomed", language)
            currentHP == 1 && evadeSuccessProb > 0 -> 
                getActionText("evade", language)
            else -> determineStrategyRecommendation(
                strategy, defenseAnalysis, evadeAnalysis,
                minDefenseDamage, currentHP, evadeFailDamage, language
            )
        }
        
        val explanation = generateExplanation(
            recommendation, strategy, defenseAnalysis, evadeAnalysis,
            minDefenseDamage, currentHP, evadeFailDamage, expectedDefenseDamage,
            expectedEvadeDamage, language
        )
        
        return BattleResult(
            recommendation = recommendation,
            explanation = explanation,
            processDetails = processDetails,
            defendProbability = defendProbStr,
            evadeProbability = evadeProbStr
        )
    }
    
    private fun calculateDefenseAnalysis(
        profile: CharacterProfile,
        currentHP: Int,
        defenseDamages: List<Int>
    ): ProbabilityAnalysis {
        val survivingOutcomes = defenseDamages.filter { currentHP - it > 0 }
        val survivalProb = survivingOutcomes.size / 6.0
        val expectedSurvivalHP = if (survivingOutcomes.isNotEmpty()) {
            survivingOutcomes.map { currentHP - it }.average()
        } else 0.0
        
        val resistanceOutcomes = defenseDamages.map { damage ->
            getResistance(currentHP - damage, profile.def, profile.evd)
        }
        val expectedResistance = resistanceOutcomes.average()
        
        val damageDistribution = defenseDamages.groupingBy { it }.eachCount()
        
        return ProbabilityAnalysis(
            survivalProb = survivalProb,
            expectedDamage = defenseDamages.average(),
            expectedResistance = expectedResistance,
            expectedSurvivalHP = expectedSurvivalHP,
            damageDistribution = damageDistribution
        )
    }
    
    private fun calculateEvadeAnalysis(
        profile: CharacterProfile,
        currentHP: Int,
        evadeSuccessProb: Double,
        evadeFailDamage: Int
    ): ProbabilityAnalysis {
        val survivalProb = if (evadeFailDamage >= currentHP) evadeSuccessProb else 1.0
        val expectedSurvivalHP = if (survivalProb > 0) currentHP.toDouble() else 0.0
        val expectedDamage = (1 - evadeSuccessProb) * evadeFailDamage
        
        val resOnSuccess = getResistance(currentHP, profile.def, profile.evd)
        val resOnFailure = getResistance(currentHP - evadeFailDamage, profile.def, profile.evd)
        val expectedResistance = evadeSuccessProb * resOnSuccess + (1 - evadeSuccessProb) * resOnFailure
        
        val damageDistribution = if (evadeSuccessProb > 0 && evadeSuccessProb < 1.0) {
            mapOf(0 to (evadeSuccessProb * 6).toInt(), evadeFailDamage to ((1 - evadeSuccessProb) * 6).toInt())
        } else if (evadeSuccessProb == 1.0) {
            mapOf(0 to 6)
        } else {
            mapOf(evadeFailDamage to 6)
        }
        
        return ProbabilityAnalysis(
            survivalProb = survivalProb,
            expectedDamage = expectedDamage,
            expectedResistance = expectedResistance,
            expectedSurvivalHP = expectedSurvivalHP,
            damageDistribution = damageDistribution
        )
    }
    
    private fun getResistance(hp: Int, baseDef: Int, baseEvd: Int): Double {
        return if (hp <= 0) {
            max(1 + baseDef, baseEvd) - 1.0
        } else {
            max(hp + baseDef, baseEvd).toDouble()
        }
    }
    
    private fun isDoomed(minDefenseDamage: Int, currentHP: Int, evadeSuccessProb: Double, evadeFailDamage: Int): Boolean {
        return minDefenseDamage >= currentHP && evadeSuccessProb == 0.0 && evadeFailDamage >= currentHP
    }
    
    private fun determineStrategyRecommendation(
        strategy: Strategy,
        defenseAnalysis: ProbabilityAnalysis,
        evadeAnalysis: ProbabilityAnalysis,
        minDefenseDamage: Int,
        currentHP: Int,
        evadeFailDamage: Int,
        language: String
    ): String {
        when (strategy) {
            Strategy.SURVIVAL -> {
                if (minDefenseDamage >= currentHP && evadeAnalysis.survivalProb > 0) {
                    return getActionText("evade", language)
                }
                if (evadeFailDamage >= currentHP && defenseAnalysis.survivalProb > 0) {
                    return getActionText("defend", language)
                }
                return if (defenseAnalysis.survivalProb > evadeAnalysis.survivalProb) {
                    getActionText("defend", language)
                } else {
                    getActionText("evade", language)
                }
            }
            
            Strategy.RESISTANCE -> {
                if (defenseAnalysis.expectedResistance > evadeAnalysis.expectedResistance) {
                    return getActionText("defend", language)
                }
                if (evadeAnalysis.expectedResistance > defenseAnalysis.expectedResistance) {
                    return getActionText("evade", language)
                }
                // Tie-breaker: survival probability
                if (defenseAnalysis.survivalProb > evadeAnalysis.survivalProb) {
                    return getActionText("defend", language)
                }
                if (evadeAnalysis.survivalProb > defenseAnalysis.survivalProb) {
                    return getActionText("evade", language)
                }
                // Second tie-breaker: expected survival HP
                return if (defenseAnalysis.expectedSurvivalHP > evadeAnalysis.expectedSurvivalHP) {
                    getActionText("defend", language)
                } else {
                    getActionText("evade", language)
                }
            }
            
            Strategy.DAMAGE -> {
                return if (defenseAnalysis.expectedDamage <= evadeAnalysis.expectedDamage) {
                    getActionText("defend", language)
                } else {
                    getActionText("evade", language)
                }
            }
        }
    }
    
    private fun generateProcessDetails(
        finalDef: Int, finalEvd: Int, minDefDmg: Int, maxDefDmg: Int,
        expDefDmg: Double, expEvdDmg: Double, expDefRes: Double, expEvdRes: Double,
        language: String
    ): String {
        return if (language == "English") {
            " Calculation Details \n" +
            "Final DEF: $finalDef | Final EVD: $finalEvd\n" +
            "DEF Dmg Range: $minDefDmg-$maxDefDmg\n" +
            "DEF Expected Dmg: ${"%.2f".format(expDefDmg)} | EVD Expected Dmg: ${"%.2f".format(expEvdDmg)}\n" +
            "DEF Expected Resistance: ${"%.2f".format(expDefRes)} | EVD Expected Resistance: ${"%.2f".format(expEvdRes)}"
        } else {
            " 计算过程参考 \n" +
            "最终防御力: $finalDef | 最终闪避力: $finalEvd\n" +
            "防御伤害范围: $minDefDmg-$maxDefDmg\n" +
            "防御期望伤害: ${"%.2f".format(expDefDmg)} | 闪避期望伤害: ${"%.2f".format(expEvdDmg)}\n" +
            "防御后期望抗性: ${"%.2f".format(expDefRes)} | 闪避后期望抗性: ${"%.2f".format(expEvdRes)}"
        }
    }
    
    private fun generateDefendProbabilityString(analysis: ProbabilityAnalysis, language: String): String {
        val actionText = getActionText("defend", language)
        val survivalText = if (language == "English") "Survival Chance" else "存活概率"
        val damageUnit = if (language == "English") "dmg" else "点"
        
        val sb = StringBuilder()
        sb.append("【$actionText】\n")
        sb.append("$survivalText: ${"%.1f%%".format(analysis.survivalProb * 100)}\n")
        
        analysis.damageDistribution.toSortedMap().forEach { (damage, count) ->
            sb.append(" P($damage $damageUnit) = $count/6\n")
        }
        
        return sb.toString().trim()
    }
    
    private fun generateEvadeProbabilityString(
        analysis: ProbabilityAnalysis, 
        evadeSuccessRolls: Int, 
        evadeFailDamage: Int, 
        language: String
    ): String {
        val actionText = getActionText("evade", language)
        val survivalText = if (language == "English") "Survival Chance" else "存活概率"
        val damageUnit = if (language == "English") "dmg" else "点"
        
        val sb = StringBuilder()
        sb.append("【$actionText】\n")
        sb.append("$survivalText: ${"%.1f%%".format(analysis.survivalProb * 100)}\n")
        
        if (evadeSuccessRolls > 0) {
            sb.append(" P(0 $damageUnit) = $evadeSuccessRolls/6\n")
        }
        if (evadeSuccessRolls < 6) {
            sb.append(" P($evadeFailDamage $damageUnit) = ${6 - evadeSuccessRolls}/6\n")
        }
        
        return sb.toString().trim()
    }
    
    private fun generateExplanation(
        recommendation: String, strategy: Strategy, 
        defenseAnalysis: ProbabilityAnalysis, evadeAnalysis: ProbabilityAnalysis,
        minDefenseDamage: Int, currentHP: Int, evadeFailDamage: Int,
        expectedDefenseDamage: Double, expectedEvadeDamage: Double,
        language: String
    ): String {
        val defendAction = getActionText("defend", language)
        val evadeAction = getActionText("evade", language)
        val doomedAction = getActionText("doomed", language)
        
        when (recommendation) {
            doomedAction -> {
                return if (language == "English") {
                    "INFO: Survival is impossible.\nEven the best DEF roll is lethal, and Evasion is guaranteed to fail and be lethal."
                } else {
                    "说明: 存活无望。\n即使掷出最好的防御骰依然会KO；同时闪避也已无成功可能，注定失败并被KO。"
                }
            }
            
            evadeAction -> {
                if (currentHP == 1) {
                    return if (language == "English") {
                        "INFO: You only have 1 HP, you must Evade!\nDefending will deal at least 1 damage, causing a KO. Evasion is your only hope."
                    } else {
                        "说明: 你只有1点HP，必须闪避！\n防御至少会受到1点伤害导致KO，闪避是唯一存活的希望。"
                    }
                }
                
                if (minDefenseDamage >= currentHP) {
                    return if (language == "English") {
                        "INFO: Must Evade!\nDefending will deal at least ${minDefenseDamage} damage, a guaranteed KO.\nEvasion is the only chance of survival."
                    } else {
                        "说明: 必须闪避！\n防御至少会受到 ${minDefenseDamage} 点伤害，必定导致KO。\n闪避是唯一可能存活的选择。"
                    }
                }
            }
            
            defendAction -> {
                if (evadeFailDamage >= currentHP && defenseAnalysis.survivalProb > 0) {
                    return if (language == "English") {
                        "INFO: Must Defend!\nFailing Evasion will deal ${evadeFailDamage} damage, causing a KO.\nDefending is the only way to survive."
                    } else {
                        "说明: 必须防御！\n闪避失败将受到 ${evadeFailDamage} 点伤害，会导致被KO。\n防御是唯一能存活的选择。"
                    }
                }
            }
        }
        
        // Strategy-based explanations
        when (strategy) {
            Strategy.SURVIVAL -> {
                return if (language == "English") {
                    "INFO: This option offers a higher chance of survival."
                } else {
                    "说明: 此选项拥有更高的存活概率。"
                }
            }
            
            Strategy.RESISTANCE -> {
                return if (language == "English") {
                    "INFO: This option results in higher expected post-combat resistance."
                } else {
                    "说明: 此选项战后的期望抗性值更高。"
                }
            }
            
            Strategy.DAMAGE -> {
                val baseExplanation = if (language == "English") {
                    "DEF Expected Dmg: ${"%.2f".format(expectedDefenseDamage)}\n" +
                    "EVD Expected Dmg: ${"%.2f".format(expectedEvadeDamage)}\n\n" +
                    if (recommendation == defendAction) "INFO: Defending has lower average damage." 
                    else "INFO: Evading has lower average damage."
                } else {
                    "防御期望伤害: ${"%.2f".format(expectedDefenseDamage)}\n" +
                    "闪避期望伤害: ${"%.2f".format(expectedEvadeDamage)}\n\n" +
                    if (recommendation == defendAction) "说明: 防御的平均伤害更低。" 
                    else "说明: 闪避的平均伤害更低。"
                }
                
                if (strategy == Strategy.DAMAGE && evadeFailDamage >= currentHP) {
                    val riskWarning = if (language == "English") {
                        "\n\nRISK: Failing this Evasion will deal ${evadeFailDamage} damage and cause a KO!"
                    } else {
                        "\n\n风险提示: 闪避失败将受到 ${evadeFailDamage} 伤害并被KO！"
                    }
                    return baseExplanation + riskWarning
                }
                
                return baseExplanation
            }
        }
    }
    
    private fun getActionText(action: String, language: String): String {
        return if (language == "English") {
            when (action) {
                "defend" -> "Defend"
                "evade" -> "Evade"
                "doomed" -> "Doomed"
                else -> action
            }
        } else {
            when (action) {
                "defend" -> "防御"
                "evade" -> "闪避"
                "doomed" -> "必死"
                else -> action
            }
        }
    }
}