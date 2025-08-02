# 100% OJ Battle Assistant Changelog

## New Platform: Native Android Version Release

* **This version was completed and merged by community contributor [TimeZ250](https://github.com/TimeZ250).**
* **New:** The project adds a native Android version developed using Kotlin and XML.
* **Platform Expansion:** The project now officially supports both Desktop (Python/Tkinter) and Mobile (Android) platforms.
* **Feature Alignment:** The Android version aims to implement the same core decision-making algorithms and strategies as the desktop version, providing a native application experience for mobile users.

## V1.x: Core Functionality & Basic Strategy

* **V1.0: Initial Version**
    * Created a graphical user interface (GUI) based on `Tkinter`.
    * Implemented the core logic for calculating the expected damage for "Defend" and "Evade" actions.
    * Added a character dropdown menu, an attack power input field, and status checkboxes.
    * Set the window to an "always on top" mode.

* **V1.1: HP & Survival Logic**
    * Added an input field for "Your Current HP".
    * Added survival risk assessment: if failing to evade would result in a KO while defending would not, "Defend" is recommended.
    * Implemented a feature to automatically fill in the character's maximum HP upon selection.

* **V1.2: Strategy Selection**
    * Added strategy options for "Survival Priority" and "Expected Damage Priority".
    * Refactored the decision-making logic to make recommendations based on the selected strategy.

* **V1.3: Game Rule Correction**
    * Corrected the success condition for "Evade" from `Evasion + Dice Roll >= Attack` to `Evasion + Dice Roll > Attack`.

* **V1.4: Custom Modifiers**
    * Added input fields for "Temporary Modifiers" to allow users to input additional values for defense and evasion.

## V2.x: Data & Interaction

* **V2.0: Full Character Database & UI Overhaul**
    * Integrated the complete official character database, replacing the previous sample data.
    * Added `-2, -1, +1, +2` shortcut buttons for the "Your Current HP" input field.
    * Added `C, +1, +5, -1` shortcut buttons for the "Opponent's Final Attack" input field.
    * Updated the internal data parser to be compatible with the official data format.

* **V2.1: Interaction Optimization**
    * Added a sunken relief and background color to the HP and attack input fields for better visual distinction.
    * Changed the `+5` attack shortcut button to `+3` and reordered the buttons to `-1, +1, +3, C`.

## V3.x: Bilingual Support & Core Algorithm Fixes

* **V3.0 - V3.6**:
    * **Bilingual Support:** Added a "Chinese / English" language switch and established a UI text localization system.
    * **Data Integration:** Integrated the Chinese version of the character database.
    * **User Experience:** Replaced the `OptionMenu` with a `Combobox` for the character selection menu to enable mouse wheel scrolling.
    * **Bug Fix:** Fixed a bug that caused mixed languages to be displayed in the UI.
    * **Core Algorithm Correction (V3.6):** Corrected the "Survival Priority" logic to properly recommend "Evade" in situations where defending is a guaranteed KO.

## V4.x: In-Depth Analysis

* **V4.0 - V4.1**:
    * **New Feature:** Added an "In-Depth Probability Analysis" module to display the survival probability and detailed damage distribution for both "Defend" and "Evade".
    * **Algorithm Improvement:** Rewrote the "Survival Priority" strategy to establish "survival probability" as the highest decision-making priority, with expected damage used only as a tie-breaker.

## V5.x: "Resistance" Strategy & Ultimate Decision-Making

* **V5.0 - V5.5**:
    * **New Dimension:** Introduced the "Resistance" concept and the "Resistance Priority" strategy.
    * **Algorithm Optimization:** The penalty for a KO was changed from negative infinity to a "KO Penalty" based on character stats.
    * **UI Improvement:** Added a permanent strategy explanation area, replacing the previous tooltip.
    * **Ultimate Tie-Breaker Rule (V5.5):** Established a decision-making chain that compares the "Expected HP upon Survival" when primary metrics are tied.

## V6.x: Usability Refinements

* **V6.0 - V6.5**:
    * **New Feature:** Added a case-insensitive, real-time search (autocomplete) feature for the character selection box.
    * **Interaction Fixes:** After multiple iterations, resolved conflicts between the "auto-clear," "continuous input," and "dropdown selection" features of the search box.

## V7.x: Layout & Final Fixes

* **V7.0**:
    * Added a collapse feature to allow users to hide or show the advanced options and analysis area.
    * Added a "Show/Hide Details" button to control the collapsed state.

* **V7.1**:
    * Fixed a startup crash caused by missing character search functions.
    * Optimized the tie-breaking logic for the "Resistance Priority" strategy by adding multiple decision-making levels.
    * Changed the default startup strategy to "Resistance Priority".

* **V7.2**:
    * Adjusted the UI layout to move the "Recommended Action" result out of the collapsible area, making it permanently visible.

* **V7.3 - V7.5**:
    * Fixed a bug where the details area was expanded by default at startup and the button text did not match.
    * Fixed a conflict between the "click-to-position" and "drag" functionalities of the slider.
    * Fixed a bug where manually inputting "Temporary Modifiers" would not trigger an immediate recalculation.
    * Fixed a logic loophole in the final algorithm related to "Survival Priority".