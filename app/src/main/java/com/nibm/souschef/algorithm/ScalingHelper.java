package com.nibm.souschef.algorithm;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScalingHelper {
    // Only ingredient units
    private static final String UNIT_PATTERN =
            "(grams|gram|g|kg|ml|l|liters|cups|cup|tbsp|tsp)";

    public static String scaleStep(String text, double factor) {

        Pattern pattern = Pattern.compile(
                "(\\d+(\\.\\d+)?)\\s*" + UNIT_PATTERN,
                Pattern.CASE_INSENSITIVE
        );

        Matcher matcher = pattern.matcher(text);

        StringBuffer result = new StringBuffer();

        while (matcher.find()) {

            double value = Double.parseDouble(matcher.group(1));

            double scaled = value * factor;

            String unit = matcher.group(3);

            String replacement = scaled + " " + unit;

            matcher.appendReplacement(result, replacement);
        }

        matcher.appendTail(result);

        return result.toString();
    }
}
