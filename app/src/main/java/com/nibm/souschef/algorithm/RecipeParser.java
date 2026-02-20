package com.nibm.souschef.algorithm;

import com.nibm.souschef.model.RecipeDLL;
import com.nibm.souschef.model.StepNode;

public class RecipeParser {
    public static RecipeDLL parseRecipe(String rawText) {

        RecipeDLL dll = new RecipeDLL();

        if (rawText == null || rawText.isEmpty())
            return dll;

        // split by line
        String[] steps = rawText.split("\\r?\\n");

        for (String step : steps) {

            step = step.trim();

            if (!step.isEmpty()) {
                dll.insertNode(step);
            }
        }
        autoMergeSteps(dll);

        return dll;
    }

    private static void autoMergeSteps(RecipeDLL dll) {

        StepNode node = dll.getHead();

        while (node != null && node.next != null) {

            String current = node.instruction.trim().toLowerCase();
            String next = node.next.instruction.trim().toLowerCase();

            // 🔥 Heuristic: merge if next starts with conjunction
            if (next.startsWith("and ")
                    || next.startsWith("then ")
                    || next.startsWith("until ")
                    || next.startsWith("for ")) {

                dll.setCurrentNode(node);      // ⚠️ we’ll add this
                dll.concatenateWithNext();

                // do NOT advance — list changed
            } else {
                node = node.next;
            }
        }
    }
}
