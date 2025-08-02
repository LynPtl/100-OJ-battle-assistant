package com.yueyuan.a100_oj_battle_assistant

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.Filter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.yueyuan.a100_oj_battle_assistant.data.CharacterData
import com.yueyuan.a100_oj_battle_assistant.model.BattleCalculator
import com.yueyuan.a100_oj_battle_assistant.model.Strategy
import com.yueyuan.a100_oj_battle_assistant.utils.TouchOptimizer

class MainActivity : AppCompatActivity() {
    
    // UI Elements
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var characterSpinner: AutoCompleteTextView
    private lateinit var hpInput: EditText
    private lateinit var attackInput: EditText
    private lateinit var defModInput: EditText
    private lateinit var evdModInput: EditText
    private lateinit var strategyGroup: RadioGroup
    private lateinit var strategyExplanation: TextView
    private lateinit var recommendationAction: TextView
    private lateinit var recommendationExplanation: TextView
    private lateinit var toggleDetailsButton: MaterialButton
    private lateinit var detailsContainer: LinearLayout
    private lateinit var processDetails: TextView
    private lateinit var defendProbability: TextView
    private lateinit var evadeProbability: TextView
    private lateinit var languageToggle: MaterialButton
    
    // HP adjustment buttons
    private lateinit var hpMinus2: MaterialButton
    private lateinit var hpMinus1: MaterialButton
    private lateinit var hpPlus1: MaterialButton
    private lateinit var hpPlus2: MaterialButton
    
    // Attack adjustment buttons
    private lateinit var attackMinus1: MaterialButton
    private lateinit var attackPlus1: MaterialButton
    private lateinit var attackPlus3: MaterialButton
    private lateinit var attackClear: MaterialButton
    
    // Core logic
    private val battleCalculator = BattleCalculator()
    private var currentLanguage = "中文"
    private var detailsVisible = false
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        initializeViews()
        setupEventListeners()
        optimizeForMobile()
        updateUILanguage()
        setupCharacterSpinner()
        
        // Set default strategy
        findViewById<RadioButton>(R.id.strategy_resistance).isChecked = true
        updateStrategyExplanation()
        
        runAnalysis() // Initialize calculation
    }
    
    private fun initializeViews() {
        toolbar = findViewById(R.id.toolbar)
        characterSpinner = findViewById(R.id.character_spinner)
        hpInput = findViewById(R.id.hp_input)
        attackInput = findViewById(R.id.attack_input)
        defModInput = findViewById(R.id.def_mod_input)
        evdModInput = findViewById(R.id.evd_mod_input)
        strategyGroup = findViewById(R.id.strategy_group)
        strategyExplanation = findViewById(R.id.strategy_explanation)
        recommendationAction = findViewById(R.id.recommendation_action)
        recommendationExplanation = findViewById(R.id.recommendation_explanation)
        toggleDetailsButton = findViewById(R.id.toggle_details_button)
        detailsContainer = findViewById(R.id.details_container)
        processDetails = findViewById(R.id.process_details)
        defendProbability = findViewById(R.id.defend_probability)
        evadeProbability = findViewById(R.id.evade_probability)
        languageToggle = findViewById(R.id.language_toggle)
        
        // Set up dropdown arrow click
        findViewById<TextView>(R.id.dropdown_arrow).setOnClickListener {
            characterSpinner.requestFocus()
            characterSpinner.showDropDown()
        }
        
        // HP buttons
        hpMinus2 = findViewById(R.id.hp_minus_2)
        hpMinus1 = findViewById(R.id.hp_minus_1)
        hpPlus1 = findViewById(R.id.hp_plus_1)
        hpPlus2 = findViewById(R.id.hp_plus_2)
        
        // Attack buttons
        attackMinus1 = findViewById(R.id.attack_minus_1)
        attackPlus1 = findViewById(R.id.attack_plus_1)
        attackPlus3 = findViewById(R.id.attack_plus_3)
        attackClear = findViewById(R.id.attack_clear)
        
        setSupportActionBar(toolbar)
    }
    
    private fun setupEventListeners() {
        // HP adjustment buttons
        hpMinus2.setOnClickListener { modifyHP(-2) }
        hpMinus1.setOnClickListener { modifyHP(-1) }
        hpPlus1.setOnClickListener { modifyHP(1) }
        hpPlus2.setOnClickListener { modifyHP(2) }
        
        // Attack adjustment buttons
        attackMinus1.setOnClickListener { modifyAttack(-1) }
        attackPlus1.setOnClickListener { modifyAttack(1) }
        attackPlus3.setOnClickListener { modifyAttack(3) }
        attackClear.setOnClickListener { 
            attackInput.setText("0")
            runAnalysis()
        }
        
        // Text change listeners for real-time calculation
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) { runAnalysis() }
        }
        
        hpInput.addTextChangedListener(textWatcher)
        attackInput.addTextChangedListener(textWatcher)
        
        // Character selection - handle both click and text input
        characterSpinner.setOnItemClickListener { _, _, _, _ ->
            autofillHP()
            runAnalysis()
        }
        
        // Handle manual text input for character search
        characterSpinner.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                try {
                    val text = s?.toString()?.trim() ?: ""
                    // Check if the entered text matches a valid character
                    if (text.isNotEmpty() && CharacterData.getCharacterData(currentLanguage).containsKey(text)) {
                        autofillHP()
                    }
                    runAnalysis()
                } catch (e: Exception) {
                    Log.e("BattleHelper", "Error in character text change", e)
                }
            }
        })
        
        // Strategy selection - update explanation and recalculate
        strategyGroup.setOnCheckedChangeListener { _, _ ->
            updateStrategyExplanation()
            runAnalysis()
        }
        
        // Details toggle
        toggleDetailsButton.setOnClickListener { toggleDetails() }
        
        // Language toggle
        languageToggle.setOnClickListener { toggleLanguage() }
    }
    
    private fun setupCharacterSpinner() {
        val characterNames = CharacterData.getCharacterNames(currentLanguage)
        Log.d("BattleHelper", "Setting up character spinner with ${characterNames.size} characters")
        val dropdownArrow = findViewById<TextView>(R.id.dropdown_arrow)
        
        // Create a stable adapter with robust filtering
        val adapter = object : ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, ArrayList(characterNames)) {
            
            private val originalList = ArrayList(characterNames)
            private var currentConstraint = ""
            
            override fun getFilter(): Filter {
                return object : Filter() {
                    override fun performFiltering(constraint: CharSequence?): FilterResults {
                        val results = FilterResults()
                        return try {
                            val query = constraint?.toString()?.lowercase()?.trim() ?: ""
                            currentConstraint = query
                            
                            val filteredList = if (query.isEmpty()) {
                                ArrayList(originalList)
                            } else {
                                originalList.filter { name ->
                                    name.lowercase().contains(query)
                                }
                            }
                            
                            results.values = filteredList
                            results.count = filteredList.size
                            results
                        } catch (e: Exception) {
                            Log.e("BattleHelper", "Error in performFiltering: ${e.message}")
                            results.values = ArrayList(originalList)
                            results.count = originalList.size
                            results
                        }
                    }
                    
                    @Suppress("UNCHECKED_CAST")
                    override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                        try {
                            // Prevent concurrent modification
                            runOnUiThread {
                                try {
                                    clear()
                                    val filteredList = results?.values as? List<String>
                                    if (filteredList != null && filteredList.isNotEmpty()) {
                                        addAll(filteredList)
                                    } else if (currentConstraint.isEmpty()) {
                                        addAll(originalList)
                                    }
                                    notifyDataSetChanged()
                                } catch (e: Exception) {
                                    Log.e("BattleHelper", "Error in UI update: ${e.message}")
                                }
                            }
                        } catch (e: Exception) {
                            Log.e("BattleHelper", "Error in publishResults: ${e.message}")
                        }
                    }
                }
            }
            
            // Update the original list when language changes
            fun updateCharacterList(newList: List<String>) {
                originalList.clear()
                originalList.addAll(newList)
                clear()
                addAll(newList)
                notifyDataSetChanged()
            }
        }
        
        characterSpinner.setAdapter(adapter)
        
        // Clear text when language changes
        characterSpinner.setText("", false)
        
        // Set threshold for showing suggestions
        characterSpinner.threshold = 0
        
        // Store adapter reference for language changes
        characterSpinner.tag = adapter
        
        // Show dropdown when focused and rotate arrow
        characterSpinner.setOnFocusChangeListener { _, hasFocus ->
            try {
                if (hasFocus) {
                    characterSpinner.showDropDown()
                    rotateArrow(dropdownArrow, true)
                } else {
                    rotateArrow(dropdownArrow, false)
                }
            } catch (e: Exception) {
                Log.e("BattleHelper", "Error in focus change: ${e.message}")
            }
        }
        
        // Also show dropdown when clicked
        characterSpinner.setOnClickListener {
            try {
                characterSpinner.showDropDown()
                rotateArrow(dropdownArrow, true)
            } catch (e: Exception) {
                Log.e("BattleHelper", "Error in click: ${e.message}")
            }
        }
        
        // Handle dropdown dismiss
        characterSpinner.setOnDismissListener {
            try {
                rotateArrow(dropdownArrow, false)
            } catch (e: Exception) {
                Log.e("BattleHelper", "Error in dismiss: ${e.message}")
            }
        }
    }
    
    private fun rotateArrow(arrow: TextView, isExpanded: Boolean) {
        val rotation = if (isExpanded) 180f else 0f
        arrow.animate()
            .rotation(rotation)
            .setDuration(200)
            .start()
    }
    
    private fun modifyHP(amount: Int) {
        try {
            val currentValue = hpInput.text.toString().toIntOrNull() ?: 0
            val newValue = maxOf(1, currentValue + amount)
            hpInput.setText(newValue.toString())
        } catch (e: Exception) {
            hpInput.setText("1")
        }
        runAnalysis()
    }
    
    private fun modifyAttack(amount: Int) {
        try {
            val currentValue = attackInput.text.toString().toIntOrNull() ?: 0
            val newValue = maxOf(0, currentValue + amount)
            attackInput.setText(newValue.toString())
        } catch (e: Exception) {
            attackInput.setText("0")
        }
        runAnalysis()
    }
    
    private fun autofillHP() {
        try {
            val characterName = characterSpinner.text.toString().trim()
            if (characterName.isNotEmpty()) {
                val profile = CharacterData.getCharacterData(currentLanguage)[characterName]
                profile?.let {
                    hpInput.setText(it.hp.toString())
                }
            }
        } catch (e: Exception) {
            Log.e("BattleHelper", "Error in autofillHP", e)
        }
        // Don't call runAnalysis here as it will be called by the text watcher
    }
    
    private fun toggleDetails() {
        detailsVisible = !detailsVisible
        if (detailsVisible) {
            detailsContainer.visibility = View.VISIBLE
            toggleDetailsButton.text = if (currentLanguage == "English") "Hide Details ▲" else "隐藏详情 ▲"
        } else {
            detailsContainer.visibility = View.GONE
            toggleDetailsButton.text = if (currentLanguage == "English") "Show Details ▼" else "显示详情 ▼"
        }
    }
    
    private fun toggleLanguage() {
        currentLanguage = if (currentLanguage == "中文") "English" else "中文"
        updateUILanguage()
        
        // Safely recreate the character spinner for language change
        try {
            setupCharacterSpinner()
        } catch (e: Exception) {
            Log.e("BattleHelper", "Error updating language: ${e.message}")
        }
        
        runAnalysis()
    }
    
    private fun updateUILanguage() {
        if (currentLanguage == "English") {
            toolbar.title = "100% OrangeJuice! Battle Helper"
            findViewById<TextView>(R.id.character_label)?.text = "Your Character:"
            characterSpinner.hint = "Select Character..."
            findViewById<TextView>(R.id.hp_label)?.text = "Current HP:"
            findViewById<TextView>(R.id.attack_label)?.text = "Opponent's Final Attack:"
            findViewById<TextView>(R.id.temp_modifiers_title)?.text = "Temporary Modifiers"
            findViewById<TextView>(R.id.def_label)?.text = "DEF:"
            findViewById<TextView>(R.id.evd_label)?.text = "EVD:"
            findViewById<TextView>(R.id.strategy_title)?.text = "Strategy Selection"
            findViewById<RadioButton>(R.id.strategy_survival)?.text = "Survival"
            findViewById<RadioButton>(R.id.strategy_damage)?.text = "Exp. Damage"
            findViewById<RadioButton>(R.id.strategy_resistance)?.text = "Resistance"
            findViewById<TextView>(R.id.recommendation_title)?.text = "Recommended Action:"
            findViewById<TextView>(R.id.process_details_title)?.text = "Calculation Details"
            findViewById<TextView>(R.id.probability_title)?.text = "Probability Analysis"
        } else {
            toolbar.title = "100%鲜橙汁 战斗助手"
            findViewById<TextView>(R.id.character_label)?.text = "你的角色:"
            characterSpinner.hint = "选择角色..."
            findViewById<TextView>(R.id.hp_label)?.text = "当前HP:"
            findViewById<TextView>(R.id.attack_label)?.text = "对手最终攻击力:"
            findViewById<TextView>(R.id.temp_modifiers_title)?.text = "临时修正"
            findViewById<TextView>(R.id.def_label)?.text = "防御:"
            findViewById<TextView>(R.id.evd_label)?.text = "闪避:"
            findViewById<TextView>(R.id.strategy_title)?.text = "策略选择"
            findViewById<RadioButton>(R.id.strategy_survival)?.text = "生存优先"
            findViewById<RadioButton>(R.id.strategy_damage)?.text = "期望伤害"
            findViewById<RadioButton>(R.id.strategy_resistance)?.text = "抗性保留"
            findViewById<TextView>(R.id.recommendation_title)?.text = "推荐行动:"
            findViewById<TextView>(R.id.process_details_title)?.text = "计算过程参考"
            findViewById<TextView>(R.id.probability_title)?.text = "概率深度分析"
        }
        
        updateStrategyExplanation()
        
        if (!detailsVisible) {
            toggleDetailsButton.text = if (currentLanguage == "English") "Show Details ▼" else "显示详情 ▼"
        } else {
            toggleDetailsButton.text = if (currentLanguage == "English") "Hide Details ▲" else "隐藏详情 ▲"
        }
    }
    
    private fun updateStrategyExplanation() {
        val checkedId = strategyGroup.checkedRadioButtonId
        val explanation = when (checkedId) {
            R.id.strategy_survival -> {
                if (currentLanguage == "English") {
                    "INFO: Prioritizes the action with the highest Survival Chance."
                } else {
                    "说明：优先选择【存活概率】更高的一方。"
                }
            }
            R.id.strategy_damage -> {
                if (currentLanguage == "English") {
                    "INFO: Prioritizes the action with the lowest Expected Damage."
                } else {
                    "说明：优先选择【期望伤害】更低的一方。"
                }
            }
            R.id.strategy_resistance -> {
                if (currentLanguage == "English") {
                    "INFO: Prioritizes Exp. Resistance, then Survival Chance/HP/Dmg."
                } else {
                    "说明：以【期望抗性】为最优先，\n依次比较存活率/存活HP/期望伤害。\n抗性 = max(HP + 基础防御, 基础闪避)"
                }
            }
            else -> ""
        }
        strategyExplanation.text = explanation
    }
    
    private fun runAnalysis() {
        try {
            val characterName = characterSpinner.text.toString().trim()
            val profile = CharacterData.getCharacterData(currentLanguage)[characterName]
            
            if (profile == null) {
                recommendationAction.text = "..."
                recommendationExplanation.text = if (currentLanguage == "English") "Please select a character" else "请选择角色"
                // Clear details
                processDetails.text = ""
                defendProbability.text = ""
                evadeProbability.text = ""
                return
            }
            
            val hp = hpInput.text.toString().toIntOrNull()
            val attack = attackInput.text.toString().toIntOrNull()
            val defMod = defModInput.text.toString().toIntOrNull() ?: 0
            val evdMod = evdModInput.text.toString().toIntOrNull() ?: 0
            
            if (hp == null || hp <= 0) {
                recommendationAction.text = "..."
                recommendationExplanation.text = if (currentLanguage == "English") "Please enter valid HP (>0)" else "请输入有效HP (>0)"
                // Clear details
                processDetails.text = ""
                defendProbability.text = ""
                evadeProbability.text = ""
                return
            }
            
            if (attack == null || attack < 0) {
                recommendationAction.text = "..."
                recommendationExplanation.text = if (currentLanguage == "English") "Please enter valid attack (≥0)" else "请输入有效攻击力 (≥0)"
                // Clear details
                processDetails.text = ""
                defendProbability.text = ""
                evadeProbability.text = ""
                return
            }
            
            val strategy = when (strategyGroup.checkedRadioButtonId) {
                R.id.strategy_survival -> Strategy.SURVIVAL
                R.id.strategy_damage -> Strategy.DAMAGE
                R.id.strategy_resistance -> Strategy.RESISTANCE
                else -> Strategy.RESISTANCE
            }
            
            val result = battleCalculator.calculateRecommendation(
                profile, hp, attack, strategy, defMod, evdMod, currentLanguage
            )
            
            // Update UI with results
            recommendationAction.text = result.recommendation
            recommendationExplanation.text = result.explanation
            processDetails.text = result.processDetails
            defendProbability.text = result.defendProbability
            evadeProbability.text = result.evadeProbability
            
            // Set recommendation color based on action
            val color = when {
                result.recommendation.contains("必死") || result.recommendation.contains("Doomed") -> 
                    ContextCompat.getColor(this, R.color.color_doomed)
                result.explanation.contains("必须") || result.explanation.contains("Must") -> 
                    ContextCompat.getColor(this, R.color.color_must_action)
                result.explanation.contains("风险") || result.explanation.contains("RISK") -> 
                    ContextCompat.getColor(this, R.color.color_risk)
                result.recommendation.contains("防御") || result.recommendation.contains("Defend") -> 
                    ContextCompat.getColor(this, R.color.color_defend)
                result.recommendation.contains("闪避") || result.recommendation.contains("Evade") -> 
                    ContextCompat.getColor(this, R.color.color_evade)
                else -> ContextCompat.getColor(this, R.color.black)
            }
            recommendationAction.setTextColor(color)
            
        } catch (e: Exception) {
            // Handle any calculation errors gracefully
            Log.e("BattleHelper", "Error in runAnalysis", e)
            recommendationAction.text = "..."
            recommendationExplanation.text = "错误: ${e.message}"
        }
    }
    
    private fun optimizeForMobile() {
        val mainView = findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.main)
        val scrollView = findViewById<ScrollView>(R.id.scroll_view)
        
        // Optimize touch targets
        TouchOptimizer.optimizeTouchTargets(mainView)
        
        // Handle keyboard insets
        TouchOptimizer.handleKeyboardInsets(mainView, scrollView)
        
        // Optimize EditText inputs
        TouchOptimizer.optimizeEditText(hpInput)
        TouchOptimizer.optimizeEditText(attackInput)
        TouchOptimizer.optimizeEditText(defModInput)
        TouchOptimizer.optimizeEditText(evdModInput)
        
        // Add haptic feedback to important buttons
        listOf(hpMinus2, hpMinus1, hpPlus1, hpPlus2,
               attackMinus1, attackPlus1, attackPlus3, attackClear,
               toggleDetailsButton, languageToggle).forEach { button ->
            button.setOnLongClickListener { 
                it.performHapticFeedback(android.view.HapticFeedbackConstants.LONG_PRESS)
                false
            }
        }
        
        // Character spinner is already configured in setupCharacterSpinner()
    }
    
}