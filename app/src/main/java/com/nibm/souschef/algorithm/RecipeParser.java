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

        while (node != null) {

            // Merge with Next
            if (node.next != null) {

                String next = node.next.instruction.trim().toLowerCase();

                if (next.startsWith("and ")
                        || next.startsWith("then ")
                        || next.startsWith("until ")
                        || next.startsWith("for ")) {

                    dll.setCurrentNode(node);
                    dll.concatenateWithNext();
                    continue;
                }
            }

            // Merge with Previous
            if (node.prev != null) {

                String current = node.instruction.trim().toLowerCase();

                if (current.startsWith("and ")
                        || current.startsWith("then ")
                        || current.startsWith("until ")
                        || current.startsWith("for ")) {

                    dll.setCurrentNode(node);
                    dll.concatenateWithPrev();
                    node = dll.getCurrentNode();
                    continue;
                }
            }

            node = node.next;
        }
    }
}
