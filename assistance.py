# -*- coding: utf-8 -*-

import tkinter as tk
from tkinter import ttk, messagebox
from collections import Counter
import math


# ======================================================================
# 1. 核心计算逻辑与数据模块
# ======================================================================

def setup_language_and_logic():
    character_data_chn = """
    QP,5,0,0,0,5
    由希,5,+2,-1,-1,5
    亚琉,5,-1,-1,+2,5
    须玖莉,4,+1,-1,+2,5
    姬梦,5,+1,-1,+1,5
    优空,4,+1,0,+1,5
    玛儿,4,+1,+1,-1,5
    菲尔涅特,6,-1,+2,-2,5
    彼特,3,+1,+1,+1,4
    凯,5,+1,0,0,5
    玛丽啵噗,7,-1,-1,-1,5
    兔萌萌（单人）,6,+2,0,+1,6
    兔萌萌（多人）,4,+2,0,0,6
    鸡,3,-1,-1,+1,3
    机器球,3,-1,+1,-1,4
    海鸥,3,+1,-1,-1,4
    店长,6,+2,0,-1,6
    希夫机器人,5,+1,0,-1,—
    飞天城堡,8,+1,-1,-2,6
    秀拉,4,0,+1,0,4
    奈奈子,3,0,+2,+1,4
    QP（危机）,5,0,0,-1,5
    纱季,4,0,0,+1,4
    京介,5,-1,+2,0,5
    克里拉拉莉丝,6,0,0,-1,5
    佳爱,4,0,-1,+1,4
    阿露特,5,0,-1,+1,5
    京子,5,-1,+3,-2,5
    玛丽啵噗（混成）,7,-1,-1,-1,5
    夏姆,4,0,+1,+1,5
    雪莉,5,+1,+1,+1,5
    优空（军服）,4,+1,0,+1,5
    星球破坏者,5,+2,0,-1,5
    甜点破坏者,6,0,0,0,6
    亚琉（争夺）,5,-1,-1,+2,5
    娜德,5,-1,-1,+1,5
    米缪,3,-1,0,+1,1
    托玛特,3,+1,0,0,3
    斩子,8,0,-1,0,5
    NoName,5,+1,-1,0,6
    NoName（头）,2,-1,-1,-1,6
    柯欧蕾帕露可,4,0,0,+1,5
    美羽咲,4,+1,-2,0,5
    由希（危机）,5,+1,0,-1,5
    兔萌萌（休闲）,4,-1,0,+1,5
    兔萌萌（甜点品尝者）,6,+3,0,0,6
    须玖莉（Ver.2）,4,+1,-1,+2,5
    杏,4,0,-1,+2,5
    铁基拉,5,0,+1,-3,6
    梅依,4,0,0,0,5
    夏美,5,-1,0,0,5
    妮可,4,0,0,+1,5
    亚瑟,7,0,-1,-1,6
    依琉,5,0,0,0,5
    米拉,5,+1,-1,+1,5
    优空＆夏姆（可爱者）,4,0,0,+1,5
    夕希,4,0,0,0,5
    艾拉,5,+1,+1,+1,5
    魅樱,6,0,-1,+1,5
    须玖莉（460亿年）,4,+1,0,+2,5
    栖海花,5,+1,-1,+1,5
    艾莉,5,+1,0,0,5
    露露,5,0,+1,-1,5
    玛儿（飞行员）,4,+1,+1,-1,5
    亚莉希安罗妮,4,+1,-1,+1,5
    缇欧特拉塔,5,0,0,+1,4
    阿妮尔,5,+1,-1,0,5
    梅妮,4,0,+1,0,5
    QP太,5,0,0,0,5
    克丽丝,5,-1,0,0,5
    哈琳娜,5,-1,+1,+1,5
    库克,4,0,-1,+3,5
    孤独车手,5,0,0,-1,5
    商人,5,0,0,0,6
    月夜姬梦,5,+1,-2,+2,5
    社交界的菲尔涅特,6,-1,+1,-1,5
    莫尔特,5,+1,+1,+1,5
    梅丝卡尔,4,0,+1,+1,5
    希夫,8,0,-2,0,5
    星乃丽华,3,+2,0,0,5
    沃缇,5,-3,+1,0,5
    波美拉尼亚斯,5,+1,-1,0,5
    甜点创造者,4,0,+1,+1,5
    纱季（甜点制作者）,5,0,+1,-1,5
    夏美（甜点评论者）,5,-1,+1,+1,5
    节庆魅樱,5,+2,-2,0,5
    睡衣克里拉拉莉丝,4,0,+1,0,6
    囚犯米缪,4,+1,0,+1,4
    母亲啵噗,7,-1,-1,-1,5
    黑暗露露,5,+1,+1,-1,6
    超能艾莉,4,+1,0,0,4
    勇者凯,5,+1,0,0,5
    格雷因,5,+1,+2,-2,6
    波本,7,0,+1,-1,6
    啵悠,5,0,0,-1,5
    䌷,6,+1,0,-2,6
    春留果,5,0,-1,+2,5
    夏奈多,4,0,+2,-1,5
    """
    character_data_eng = """
    QP,5,0,0,0,5
    Yuki,5,+2,-1,-1,5
    Aru,5,-1,-1,+2,5
    Suguri,4,+1,-1,+2,5
    Hime,5,+1,-1,+1,5
    Sora,4,+1,0,+1,5
    Marc,4,+1,+1,-1,5
    Fernet,6,-1,+2,-2,5
    Peat,3,+1,+1,+1,4
    Kai,5,+1,0,0,5
    Marie Poppo,7,-1,-1,-1,5
    Tomomo (Singleplayer),6,+2,0,+1,6
    Tomomo (Softened),4,+2,0,0,6
    Chicken,3,-1,-1,+1,3
    Robo Ball,3,-1,+1,-1,4
    Seagull,3,+1,-1,-1,4
    Store Manager,6,+2,0,-1,6
    Shifu Robot,5,+1,0,-1,—
    Flying Castle,8,+1,-1,-2,6
    Syura,4,0,+1,0,4
    Nanako,3,0,+2,+1,4
    QP (Dangerous),5,0,0,-1,5
    Saki,4,0,0,+1,4
    Kyousuke,5,-1,+2,0,5
    Krilalaris,6,0,0,-1,5
    Kae,4,0,-1,+1,4
    Alte,5,0,-1,+1,5
    Kyoko,5,-1,+3,-2,5
    Marie Poppo (Mixed),7,-1,-1,-1,5
    Sham,4,0,+1,+1,5
    Sherry,5,+1,+1,+1,5
    Sora (Military),4,+1,0,+1,5
    Star Breaker,5,+2,0,-1,5
    Sweet Breaker,6,0,0,0,6
    Aru (Scramble),5,-1,-1,+2,5
    Nath,5,-1,-1,+1,5
    Mimyuu,3,-1,0,+1,1
    Tomato,3,+1,0,0,3
    Kiriko,8,0,-1,0,5
    NoName,5,+1,-1,0,6
    NoName (Head),2,-1,-1,-1,6
    Ceoreparque,4,0,0,+1,5
    Miusaki,4,+1,-2,0,5
    Yuki (Dangerous),5,+1,0,-1,5
    Tomomo (Casual),4,-1,0,+1,5
    Tomomo (Sweet Eater),6,+3,0,0,6
    Suguri (Ver.2),4,+1,-1,+2,5
    Tsih,4,0,-1,+2,5
    Tequila,5,0,+1,-3,6
    Mei,4,0,0,0,5
    Natsumi,5,-1,0,0,5
    Nico,4,0,0,+1,5
    Arthur,7,0,-1,-1,6
    Iru,5,0,0,0,5
    Mira,5,+1,-1,+1,5
    Cuties,4,0,0,+1,5
    Yuuki,4,0,0,0,5
    Islay,5,+1,+1,+1,5
    Mio,6,0,-1,+1,5
    Suguri (46 Billion Years),4,+1,0,+2,5
    Sumika,5,+1,-1,+1,5
    Ellie,5,+1,0,0,5
    Lulu,5,0,+1,-1,5
    Marc (Pilot),4,+1,+1,-1,5
    Alicianrone,4,+1,-1,+1,5
    Teotoratta,5,0,0,+1,4
    Arnelle,5,+1,-1,0,5
    Maynie,4,0,+1,0,5
    Kyupita,5,0,0,0,5
    Chris,5,-1,0,0,5
    Halena,5,-1,+1,+1,5
    Cook,4,0,-1,+3,5
    Lone Rider,5,0,0,-1,5
    Merchant,5,0,0,0,6
    Hime (Moonlight),5,+1,-2,+2,5
    Fernet (Noble),6,-1,+1,-1,5
    Malt,5,+1,+1,+1,5
    Mescal,4,0,+1,+1,5
    Shifu,8,0,-2,0,5
    Hoshino Reika,3,+2,0,0,5
    Watty,5,-3,+1,0,5
    Pomeranius,5,+1,-1,0,5
    Sweet Creator,4,0,+1,+1,5
    Saki (Sweet Maker),5,0,+1,-1,5
    Natsumi (Sweet Blogger),5,-1,+1,+1,5
    Mio (Festive),5,+2,-2,0,5
    Krilalaris (Pajamas),4,0,+1,0,6
    Mimyuu (Jailbird),4,+1,0,+1,4
    Mother Poppo,7,-1,-1,-1,5
    Dark Lulu,5,+1,+1,-1,6
    Hyper Ellie,4,+1,0,0,4
    Kai (Hero),5,+1,0,0,5
    Grain,5,+1,+2,-2,6
    Bourbon,7,0,+1,-1,6
    Poyo,5,0,0,-1,5
    Chuu,6,+1,0,-2,6
    Haruka,5,0,-1,+2,5
    Kanata,4,0,+2,-1,5
    """

    def parse_character_data(data_string):
        profiles = {}
        lines = data_string.strip().split('\n')
        for line in lines:
            line = line.strip()
            if not line: continue
            try:
                clean_line = line.split('] ', 1)[1] if '] ' in line else line
                parts = clean_line.strip().split(',')
                if len(parts) < 6: continue
                profiles[parts[0]] = {"hp": int(parts[1]), "atk": int(parts[2]), "def": int(parts[3]),
                                      "evd": int(parts[4]), "rec": 0 if parts[5] == '—' else int(parts[5])}
            except (IndexError, ValueError) as e:
                print(f"Skipping malformed line: '{line}' -> Error: {e}")
        return profiles

    character_data = {"中文": parse_character_data(character_data_chn),
                      "English": parse_character_data(character_data_eng)}

    ui_texts = {
        "中文": {
            "window_title": "100% OJ 战斗助手 V6.5", "char_label": "你的角色:", "hp_label": "自己当前HP:",
            "attack_label": "对手最终攻击力:",
            "custom_mod_frame": "临时修正", "def_mod_label": "防御:", "evd_mod_label": "闪避:",
            "strategy_frame": "策略选择", "strat_survival": "生存优先", "strat_damage": "期望伤害优先",
            "strat_resistance": "抗性保留优先",
            "strategy_explanations": {"survival": "说明：优先选择【存活概率】更高的一方。",
                                      "damage": "说明：优先选择【期望伤害】更低的一方。",
                                      "resistance": "说明：优先选择战后【期望抗性】更高的一方。\n(抗性 = max(HP + 基础防御, 基础闪避))"},
            "result_title": "推荐行动:", "initial_explanation": "请填入数据", "process_title": "--- 计算过程参考 ---",
            "final_def": "最终防御力:", "final_evd": "最终闪避力:", "def_dmg_range": "防御伤害范围:",
            "evd_success_rate": "闪避成功率:", "evd_fail_dmg": "闪避失败伤害:", "exp_def_dmg": "防御期望伤害",
            "exp_evd_dmg": "闪避期望伤害",
            "exp_def_res": "防御后期望抗性", "exp_evd_res": "闪避后期望抗性", "prob_analysis_title": "概率深度分析",
            "survival_chance": "存活概率",
            "actions": {"defend": "防御", "evade": "闪避", "doomed": "必死"},
            "recommendations": {
                "must_defend": "说明: 必须防御！\n闪避失败将受到 {:.0f} 点伤害，会导致被KO。\n防御是唯一能存活的选择。",
                "must_evade_1hp": "说明: 你只有1点HP，必须闪避！\n防御至少会受到1点伤害导致KO，闪避是唯一存活的希望。",
                "must_evade_def_lethal": "说明: 必须闪避！\n防御至少会受到 {:.0f} 点伤害，必定导致KO。\n闪避是唯一可能存活的选择。",
                "doomed": "说明: 存活无望。\n即使掷出最好的防御骰依然会KO；\n同时闪避也已无成功可能，注定失败并被KO。",
                "def_is_better": "说明: 防御的平均伤害更低，是更稳妥的选择。",
                "evd_is_better": "说明: 闪避的平均伤害更低。",
                "resistance_is_better": "说明: 此选项战后的期望抗性值更高。",
                "risk_warning": "\n\n风险提示: 闪避失败将受到 {:.0f} 伤害并被KO！",
                "survival_is_better": "说明: 此选项拥有更高的存活概率。"}, "unit_damage": "点"
        },
        "English": {
            "window_title": "100% OJ Battle Helper V6.5", "char_label": "Your Character:",
            "hp_label": "Your Current HP:", "attack_label": "Opponent's Final Attack:",
            "custom_mod_frame": "Temporary Modifiers", "def_mod_label": "DEF:", "evd_mod_label": "EVD:",
            "strategy_frame": "Strategy", "strat_survival": "Survival Priority",
            "strat_damage": "Expected Damage Priority", "strat_resistance": "Resistance Priority",
            "strategy_explanations": {"survival": "INFO: Prioritizes the action with the highest Survival Chance.",
                                      "damage": "INFO: Prioritizes the action with the lowest Expected Damage.",
                                      "resistance": "INFO: Prioritizes the action with the highest post-combat\nExpected Resistance. (Resistance = max(HP + Base DEF, Base EVD))"},
            "result_title": "Recommendation:", "initial_explanation": "Please input data",
            "process_title": "--- Calculation Details ---", "final_def": "Final DEF:", "final_evd": "Final EVD:",
            "def_dmg_range": "DEF Dmg Range:", "evd_success_rate": "EVD Success Rate:", "evd_fail_dmg": "EVD Fail Dmg:",
            "exp_def_dmg": "DEF Expected Dmg", "exp_evd_dmg": "EVD Expected Dmg",
            "exp_def_res": "DEF Expected Resistance", "exp_evd_res": "EVD Expected Resistance",
            "prob_analysis_title": "Probability Analysis", "survival_chance": "Survival Chance",
            "actions": {"defend": "Defend", "evade": "Evade", "doomed": "Doomed"},
            "recommendations": {
                "must_defend": "INFO: Must Defend!\nFailing Evasion will deal {:.0f} damage, causing a KO.\nDefending is the only way to survive.",
                "must_evade_1hp": "INFO: You only have 1 HP, you must Evade!\nDefending will deal at least 1 damage, causing a KO. Evasion is your only hope.",
                "must_evade_def_lethal": "INFO: Must Evade!\nDefending will deal at least {:.0f} damage, a guaranteed KO.\nEvasion is the only chance of survival.",
                "doomed": "INFO: Survival is impossible.\nEven the best DEF roll is lethal, and Evasion is guaranteed to fail and be lethal.",
                "def_is_better": "INFO: Defending has lower average damage and is the safer choice.",
                "evd_is_better": "INFO: Evading has lower average damage.",
                "resistance_is_better": "INFO: This option results in higher expected post-combat resistance.",
                "risk_warning": "\n\nRISK: Failing this Evasion will deal {:.0f} damage and cause a KO!",
                "survival_is_better": "INFO: This option offers a higher chance of survival."}, "unit_damage": "dmg"
        }
    }

    # ... (get_strategic_recommendation function remains the same) ...
    def get_strategic_recommendation(profile, lang_texts, current_hp, opponent_attack, strategy, custom_def_mod,
                                     custom_evd_mod):
        final_def, final_evd = profile['def'] + custom_def_mod, profile['evd'] + custom_evd_mod;
        defense_damages = [max(1, opponent_attack - (final_def + roll)) for roll in range(1, 7)];
        min_damage_on_def, max_damage_on_def = min(defense_damages), max(defense_damages);
        expected_damage_def = sum(defense_damages) / 6.0;
        evade_success_rolls_count = sum(1 for roll in range(1, 7) if (final_evd + roll) > opponent_attack);
        evade_success_prob = evade_success_rolls_count / 6.0;
        final_damage_on_evade_fail = opponent_attack;
        expected_damage_evd = (1 - evade_success_prob) * final_damage_on_evade_fail

        def get_resistance(hp, base_def, base_evd):
            if hp <= 0: return max(1 + base_def, base_evd) - 1
            return max(hp + base_def, base_evd)

        def_resistance_outcomes = [get_resistance(current_hp - dmg, profile['def'], profile['evd']) for dmg in
                                   defense_damages];
        expected_resistance_def = sum(def_resistance_outcomes) / 6.0
        res_on_evd_succ, res_on_evd_fail = get_resistance(current_hp, profile['def'], profile['evd']), get_resistance(
            current_hp - final_damage_on_evade_fail, profile['def'], profile['evd'])
        expected_resistance_evd = (evade_success_prob * res_on_evd_succ) + ((1 - evade_success_prob) * res_on_evd_fail)
        def_survival_outcomes_hp = [current_hp - dmg for dmg in defense_damages if current_hp - dmg > 0]
        def_survival_prob = len(def_survival_outcomes_hp) / 6.0;
        exp_hp_on_def_survival = sum(def_survival_outcomes_hp) / len(
            def_survival_outcomes_hp) if def_survival_outcomes_hp else 0
        exp_hp_on_evd_survival = current_hp if evade_success_prob > 0 else 0
        process_details = (
            f"{lang_texts['process_title']}\n{lang_texts['final_def']} {final_def} | {lang_texts['final_evd']} {final_evd}\n{lang_texts['def_dmg_range']} {min_damage_on_def:.0f}-{max_damage_on_def:.0f}\n{lang_texts['exp_def_dmg']}: {expected_damage_def:.2f} | {lang_texts['exp_evd_dmg']}: {expected_damage_evd:.2f}\n{lang_texts['exp_def_res']}: {expected_resistance_def:.2f} | {lang_texts['exp_evd_res']}: {expected_resistance_evd:.2f}")
        def_dmg_counts = Counter(defense_damages)
        def_prob_str = f"【{lang_texts['actions']['defend']}】\n{lang_texts['survival_chance']}: {def_survival_prob:.1%}\n";
        for dmg, count in sorted(
            def_dmg_counts.items()): def_prob_str += f" P({dmg} {lang_texts['unit_damage']}) = {count}/6\n"
        evd_survival_prob = evade_success_prob if final_damage_on_evade_fail >= current_hp else 1.0
        evd_prob_str = f"【{lang_texts['actions']['evade']}】\n{lang_texts['survival_chance']}: {evd_survival_prob:.1%}\n"
        if evade_success_prob > 0: evd_prob_str += f" P(0 {lang_texts['unit_damage']}) = {evade_success_rolls_count}/6\n"
        if evade_success_prob < 1: evd_prob_str += f" P({final_damage_on_evade_fail} {lang_texts['unit_damage']}) = {6 - evade_success_rolls_count}/6\n"
        probabilities = {"defend": def_prob_str.strip(), "evade": evd_prob_str.strip()}
        reco_texts, actions = lang_texts['recommendations'], lang_texts['actions']
        is_doomed = min_damage_on_def >= current_hp and evade_success_prob == 0 and final_damage_on_evade_fail >= current_hp
        if is_doomed: return {"recommendation": actions['doomed'], "explanation": reco_texts['doomed'],
                              "details": process_details, "probabilities": probabilities}
        if current_hp == 1 and evade_success_prob > 0: return {"recommendation": actions['evade'],
                                                               "explanation": reco_texts['must_evade_1hp'],
                                                               "details": process_details,
                                                               "probabilities": probabilities}
        if strategy == 'survival':
            if min_damage_on_def >= current_hp and evade_success_prob > 0: return {"recommendation": actions['evade'],
                                                                                   "explanation": reco_texts[
                                                                                       'must_evade_def_lethal'].format(
                                                                                       min_damage_on_def),
                                                                                   "details": process_details,
                                                                                   "probabilities": probabilities}
            if final_damage_on_evade_fail >= current_hp and max_damage_on_def < current_hp: return {
                "recommendation": actions['defend'],
                "explanation": reco_texts['must_defend'].format(final_damage_on_evade_fail), "details": process_details,
                "probabilities": probabilities}
            if def_survival_prob > evd_survival_prob: return {"recommendation": actions['defend'],
                                                              "explanation": reco_texts['survival_is_better'],
                                                              "details": process_details,
                                                              "probabilities": probabilities}
            if evd_survival_prob > def_survival_prob: return {"recommendation": actions['evade'],
                                                              "explanation": reco_texts['survival_is_better'],
                                                              "details": process_details,
                                                              "probabilities": probabilities}
        if strategy == 'resistance':
            if expected_resistance_def > expected_resistance_evd: return {"recommendation": actions['defend'],
                                                                          "explanation": reco_texts[
                                                                              'resistance_is_better'],
                                                                          "details": process_details,
                                                                          "probabilities": probabilities}
            if expected_resistance_evd > expected_resistance_def: return {"recommendation": actions['evade'],
                                                                          "explanation": reco_texts[
                                                                              'resistance_is_better'],
                                                                          "details": process_details,
                                                                          "probabilities": probabilities}
            if exp_hp_on_evd_survival > exp_hp_on_def_survival: return {"recommendation": actions['evade'],
                                                                        "explanation": reco_texts[
                                                                            'resistance_is_better'],
                                                                        "details": process_details,
                                                                        "probabilities": probabilities}
        explanation_details = f"{lang_texts['exp_def_dmg']}: {expected_damage_def:.2f}\n{lang_texts['exp_evd_dmg']}: {expected_damage_evd:.2f}\n\n"
        if expected_damage_def <= expected_damage_evd:
            recommendation, explanation = actions['defend'], explanation_details + reco_texts['def_is_better']
        else:
            recommendation, explanation = actions['evade'], explanation_details + reco_texts['evd_is_better']
        if strategy == 'damage' and final_damage_on_evade_fail >= current_hp: explanation += reco_texts[
            'risk_warning'].format(final_damage_on_evade_fail)
        return {"recommendation": recommendation, "explanation": explanation, "details": process_details,
                "probabilities": probabilities}

    return character_data, ui_texts, get_strategic_recommendation


# ======================================================================
# 2. GUI界面模块
# ======================================================================

class OjBattleHelperApp:
    def __init__(self, root):
        self.root, self.slider_was_dragged = root, False
        self.character_data, self.ui_texts, self.get_recommendation = setup_language_and_logic()
        self.lang_var = tk.StringVar(value="中文")
        self.full_char_list = []
        self.setup_window()
        self.create_widgets()
        self.setup_traces()
        self.update_ui_language()

    def setup_window(self):
        self.root.attributes('-topmost', True)
        self.root.attributes('-alpha', 1.0)
        self.root.resizable(False, False)
        self.root.protocol("WM_DELETE_WINDOW", self.on_closing)

    def create_widgets(self):
        self.main_frame = ttk.Frame(self.root, padding="10 10 10 10")
        self.main_frame.grid(row=0, column=0, sticky="nsew")
        self.main_frame.columnconfigure(1, weight=1)

        self.char_var, self.hp_var, self.attack_var = tk.StringVar(), tk.StringVar(value="0"), tk.StringVar(value="0")
        self.strategy_var, self.custom_def_mod_var, self.custom_evd_mod_var = tk.StringVar(
            value="survival"), tk.StringVar(value="0"), tk.StringVar(value="0")
        self.alpha_var = tk.DoubleVar(value=1.0)

        self.char_label = ttk.Label(self.main_frame)
        self.char_menu = ttk.Combobox(self.main_frame, textvariable=self.char_var, state='normal')
        self.char_menu.bind('<<ComboboxSelected>>', self.on_character_select)
        self.char_menu.bind('<KeyRelease>', self.on_char_search)
        # 修正: 移除了 <FocusIn> 事件绑定，以解决交互冲突

        self.hp_label = ttk.Label(self.main_frame)
        hp_frame = ttk.Frame(self.main_frame)
        self.hp_entry = tk.Entry(hp_frame, textvariable=self.hp_var, width=6, relief=tk.SUNKEN, borderwidth=2,
                                 justify='center', bg='#f0f0f8')
        ttk.Button(hp_frame, text="-2", width=3, command=lambda: self.modify_hp(-2)).pack(side=tk.LEFT, padx=(0, 2))
        ttk.Button(hp_frame, text="-1", width=3, command=lambda: self.modify_hp(-1)).pack(side=tk.LEFT)
        self.hp_entry.pack(side=tk.LEFT, padx=5, ipady=1)
        ttk.Button(hp_frame, text="+1", width=3, command=lambda: self.modify_hp(1)).pack(side=tk.LEFT, padx=(0, 2))
        ttk.Button(hp_frame, text="+2", width=3, command=lambda: self.modify_hp(2)).pack(side=tk.LEFT)

        self.attack_label = ttk.Label(self.main_frame)
        atk_frame = ttk.Frame(self.main_frame)
        self.attack_entry = tk.Entry(atk_frame, textvariable=self.attack_var, width=6, relief=tk.SUNKEN, borderwidth=2,
                                     justify='center', bg='#f0f0f8')
        self.attack_entry.pack(side=tk.LEFT, padx=(0, 5), ipady=1)
        ttk.Button(atk_frame, text="-1", width=3, command=lambda: self.modify_attack(-1)).pack(side=tk.LEFT,
                                                                                               padx=(0, 2))
        ttk.Button(atk_frame, text="+1", width=3, command=lambda: self.modify_attack(1)).pack(side=tk.LEFT)
        ttk.Button(atk_frame, text="+3", width=3, command=lambda: self.modify_attack(3)).pack(side=tk.LEFT, padx=2)
        ttk.Button(atk_frame, text="C", width=3, command=self.clear_attack).pack(side=tk.LEFT)

        self.custom_mod_frame = ttk.LabelFrame(self.main_frame)
        def_mod_subframe = ttk.Frame(self.custom_mod_frame)
        evd_mod_subframe = ttk.Frame(self.custom_mod_frame)
        def_mod_subframe.pack(side=tk.LEFT, padx=5, pady=2, expand=True, fill=tk.X)
        evd_mod_subframe.pack(side=tk.RIGHT, padx=5, pady=2, expand=True, fill=tk.X)
        self.def_mod_label = ttk.Label(def_mod_subframe)
        self.def_mod_label.pack(side=tk.LEFT)
        self.def_mod_entry = ttk.Entry(def_mod_subframe, textvariable=self.custom_def_mod_var, width=4)
        self.def_mod_entry.pack(side=tk.LEFT, padx=2)
        ttk.Button(def_mod_subframe, text="-1", width=2,
                   command=lambda: self.modify_custom_mod(self.custom_def_mod_var, -1)).pack(side=tk.LEFT)
        ttk.Button(def_mod_subframe, text="+1", width=2,
                   command=lambda: self.modify_custom_mod(self.custom_def_mod_var, 1)).pack(side=tk.LEFT)
        ttk.Button(def_mod_subframe, text="C", width=2,
                   command=lambda: self.clear_custom_mod(self.custom_def_mod_var)).pack(side=tk.LEFT)
        self.evd_mod_label = ttk.Label(evd_mod_subframe)
        self.evd_mod_label.pack(side=tk.LEFT)
        self.evd_mod_entry = ttk.Entry(evd_mod_subframe, textvariable=self.custom_evd_mod_var, width=4)
        self.evd_mod_entry.pack(side=tk.LEFT, padx=2)
        ttk.Button(evd_mod_subframe, text="-1", width=2,
                   command=lambda: self.modify_custom_mod(self.custom_evd_mod_var, -1)).pack(side=tk.LEFT)
        ttk.Button(evd_mod_subframe, text="+1", width=2,
                   command=lambda: self.modify_custom_mod(self.custom_evd_mod_var, 1)).pack(side=tk.LEFT)
        ttk.Button(evd_mod_subframe, text="C", width=2,
                   command=lambda: self.clear_custom_mod(self.custom_evd_mod_var)).pack(side=tk.LEFT)

        self.strategy_frame = ttk.LabelFrame(self.main_frame)
        self.strat_survival_rb = ttk.Radiobutton(self.strategy_frame, variable=self.strategy_var, value="survival",
                                                 command=self.update_strategy_explanation)
        self.strat_damage_rb = ttk.Radiobutton(self.strategy_frame, variable=self.strategy_var, value="damage",
                                               command=self.update_strategy_explanation)
        self.strat_resistance_rb = ttk.Radiobutton(self.strategy_frame, variable=self.strategy_var, value="resistance",
                                                   command=self.update_strategy_explanation)
        self.strategy_explanation_label = ttk.Label(self.main_frame, justify=tk.LEFT, foreground="gray")

        self.result_title_label = ttk.Label(self.main_frame, font=("Arial", 12))
        self.result_action_label = ttk.Label(self.main_frame, font=("Arial", 20, "bold"))
        self.result_explanation_label = ttk.Label(self.main_frame, wraplength=300, justify=tk.LEFT)
        self.process_details_label = ttk.Label(self.main_frame, font=("Courier", 9), foreground="gray", justify=tk.LEFT)
        self.prob_frame = ttk.LabelFrame(self.main_frame)
        self.def_prob_label = ttk.Label(self.prob_frame, font=("Courier", 9), justify=tk.LEFT)
        self.evd_prob_label = ttk.Label(self.prob_frame, font=("Courier", 9), justify=tk.LEFT)

        self.transparency_label = ttk.Label(self.main_frame, text="窗口透明度 / Transparency:")
        self.transparency_scale = ttk.Scale(self.main_frame, from_=0.3, to=1.0, orient=tk.HORIZONTAL,
                                            variable=self.alpha_var)
        self.transparency_scale.bind("<ButtonPress-1>", self.on_slider_press)
        self.transparency_scale.bind("<B1-Motion>", self.on_slider_drag)
        self.transparency_scale.bind("<ButtonRelease-1>", self.on_slider_release)
        self.lang_toggle_button = ttk.Button(self.main_frame, text="中 | EN", command=self.toggle_language, width=8)

        # --- 布局 ---
        self.char_label.grid(row=0, column=0, sticky=tk.W, padx=5, pady=2);
        self.char_menu.grid(row=0, column=1, sticky=tk.EW, padx=5, pady=2)
        self.hp_label.grid(row=1, column=0, sticky=tk.W, padx=5, pady=2);
        hp_frame.grid(row=1, column=1, sticky=tk.EW, padx=5, pady=2)
        self.attack_label.grid(row=2, column=0, sticky=tk.W, padx=5, pady=2);
        atk_frame.grid(row=2, column=1, sticky=tk.EW, padx=5, pady=2)
        self.custom_mod_frame.grid(row=3, column=0, columnspan=2, padx=5, pady=5, sticky=tk.EW)
        self.strategy_frame.grid(row=4, column=0, columnspan=2, padx=5, pady=5, sticky=tk.EW);
        self.strat_survival_rb.pack(side=tk.LEFT, padx=5, expand=True);
        self.strat_damage_rb.pack(side=tk.LEFT, padx=5, expand=True);
        self.strat_resistance_rb.pack(side=tk.LEFT, padx=5, expand=True)
        self.strategy_explanation_label.grid(row=5, column=0, columnspan=2, sticky=tk.W, padx=5, pady=(0, 5))
        ttk.Separator(self.main_frame, orient='horizontal').grid(row=6, column=0, columnspan=2, sticky='ew', pady=5)
        self.result_title_label.grid(row=7, column=0, columnspan=2, sticky=tk.W, padx=5);
        self.result_action_label.grid(row=8, column=0, columnspan=2, pady=(5, 10))
        self.result_explanation_label.grid(row=9, column=0, columnspan=2, sticky=tk.W, padx=5, pady=5);
        self.process_details_label.grid(row=10, column=0, columnspan=2, sticky=tk.W, padx=5, pady=(5, 0))
        self.prob_frame.grid(row=11, column=0, columnspan=2, sticky=tk.EW, padx=5, pady=5);
        self.def_prob_label.pack(side=tk.LEFT, padx=5, pady=2, anchor='nw');
        self.evd_prob_label.pack(side=tk.RIGHT, padx=5, pady=2, anchor='ne')
        ttk.Separator(self.main_frame, orient='horizontal').grid(row=12, column=0, columnspan=2, sticky='ew', pady=5)
        self.transparency_label.grid(row=13, column=0, sticky=tk.W, padx=5);
        self.transparency_scale.grid(row=13, column=1, sticky=tk.EW, padx=5, pady=(0, 5))
        self.lang_toggle_button.grid(row=14, column=0, columnspan=2, pady=5)

    def setup_traces(self):
        vars_to_trace = [self.hp_var, self.attack_var, self.strategy_var, self.custom_def_mod_var,
                         self.custom_evd_mod_var];
        [var.trace_add("write", self.trigger_analysis) for var in vars_to_trace]

    def trigger_analysis(self, *args):
        self.run_analysis()

    def on_slider_press(self, event):
        self.slider_was_dragged = False

    def on_slider_drag(self, event):
        self.slider_was_dragged = True; self.alpha_var.set(self.transparency_scale.get()); self.update_transparency(
            self.alpha_var.get())

    def on_slider_release(self, event):
        if not self.slider_was_dragged:
            if event.widget.winfo_width() > 0:
                value = (event.x / event.widget.winfo_width()) * (1.0 - 0.3) + 0.3;
                value = max(0.3, min(1.0, value))
                self.alpha_var.set(value);
                self.update_transparency(value)

    def on_char_search(self, event):
        search_term = self.char_var.get()
        if not search_term:
            self.char_menu['values'] = self.full_char_list
        else:
            filtered_list = [char for char in self.full_char_list if search_term.lower() in char.lower()]
            self.char_menu['values'] = filtered_list

    def on_character_select(self, event):
        self.autofill_hp()
        self.run_analysis()

    def toggle_language(self):
        self.lang_var.set("English" if self.lang_var.get() == "中文" else "中文"); self.update_ui_language()

    def update_ui_language(self):
        lang, texts = self.lang_var.get(), self.ui_texts[self.lang_var.get()]
        self.root.title(texts['window_title']);
        self.char_label.config(text=texts['char_label']);
        self.hp_label.config(text=texts['hp_label']);
        self.attack_label.config(text=texts['attack_label']);
        self.custom_mod_frame.config(text=texts['custom_mod_frame'])
        self.def_mod_label.config(text=texts['def_mod_label']);
        self.evd_mod_label.config(text=texts['evd_mod_label']);
        self.strategy_frame.config(text=texts['strategy_frame']);
        self.strat_survival_rb.config(text=texts['strat_survival'])
        self.strat_damage_rb.config(text=texts['strat_damage']);
        self.strat_resistance_rb.config(text=texts['strat_resistance']);
        self.result_title_label.config(text=texts['result_title']);
        self.prob_frame.config(text=texts['prob_analysis_title'])
        self.result_explanation_label.config(text=texts['initial_explanation']);
        self.result_action_label.config(text="...");
        self.process_details_label.config(text="");
        self.def_prob_label.config(text="");
        self.evd_prob_label.config(text="")
        self.full_char_list = sorted(list(self.character_data[lang].keys()));
        self.char_menu['values'] = self.full_char_list;
        self.char_var.set(self.full_char_list[0]);
        self.autofill_hp();
        self.update_strategy_explanation()

    def update_strategy_explanation(self):
        lang, texts = self.lang_var.get(), self.ui_texts[self.lang_var.get()];
        selected_strategy = self.strategy_var.get()
        self.strategy_explanation_label.config(text=texts['strategy_explanations'].get(selected_strategy, ""));
        self.run_analysis()

    def run_analysis(self, *args):
        try:
            lang, char_name = self.lang_var.get(), self.char_var.get();
            profile = self.character_data[lang].get(char_name)
            if not profile or not all(v.get() for v in [self.hp_var, self.attack_var]): return
            hp_value, attack_value = int(self.hp_var.get()), int(self.attack_var.get());
            strategy, custom_def, custom_evd = self.strategy_var.get(), int(self.custom_def_mod_var.get() or 0), int(
                self.custom_evd_mod_var.get() or 0)
            results = self.get_recommendation(profile, self.ui_texts[lang], hp_value, attack_value, strategy,
                                              custom_def, custom_evd)
            recommendation, explanation, process_details, probabilities = results["recommendation"], results[
                "explanation"], results["details"], results["probabilities"]
            reco_texts, actions = self.ui_texts[lang]['recommendations'], self.ui_texts[lang]['actions'];
            color = "black"
            if recommendation == actions['doomed']:
                color = "gray"
            elif any(msg in explanation for msg in
                     [reco_texts['must_evade_1hp'], reco_texts['must_evade_def_lethal'].format(0),
                      reco_texts['must_defend'].format(0)]):
                color = "red"
            elif reco_texts['risk_warning'].format(0) in explanation:
                color = "orange"
            elif recommendation == actions['defend']:
                color = "blue"
            elif recommendation == actions['evade']:
                color = "green"
            self.result_action_label.config(text=recommendation, foreground=color);
            self.result_explanation_label.config(text=explanation);
            self.process_details_label.config(text=process_details)
            self.def_prob_label.config(text=probabilities['defend']);
            self.evd_prob_label.config(text=probabilities['evade'])
        except (ValueError, tk.TclError):
            return

    def autofill_hp(self):
        char_name, lang = self.char_var.get(), self.lang_var.get(); self.hp_var.set(
            str(self.character_data[lang].get(char_name, {}).get("hp", 0)))

    def modify_hp(self, amount):
        try: self.hp_var.set(str(max(1, int(self.hp_var.get()) + amount)));
        except ValueError: self.hp_var.set("1")

    def modify_attack(self, amount):
        try: self.attack_var.set(str(max(0, int(self.attack_var.get()) + amount)));
        except ValueError: self.attack_var.set("0")

    def clear_attack(self):
        self.attack_var.set("0")

    def modify_custom_mod(self, var, amount):
        try: var.set(str(int(var.get() or 0) + amount));
        except ValueError: var.set(str(amount))

    def clear_custom_mod(self, var):
        var.set("0")

    def update_transparency(self, value):
        self.root.attributes('-alpha', float(value))

    def on_closing(self):
        if messagebox.askokcancel("Quit", "Are you sure you want to quit?"): self.root.destroy()


# ======================================================================
# 3. 程序入口
# ======================================================================
if __name__ == "__main__":
    from tkinter import TclError;
    root = tk.Tk();
    app = OjBattleHelperApp(root)
    if root.winfo_exists(): root.mainloop()