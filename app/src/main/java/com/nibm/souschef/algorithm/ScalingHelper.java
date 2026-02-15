package com.nibm.souschef.algorithm;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScalingHelper {
    public static String scaleStep(String text, double factor) {

        Pattern pattern = Pattern.compile("\\d+(\\.\\d+)?");

        Matcher matcher = pattern.matcher(text);

        StringBuffer result = new StringBuffer();

        while (matcher.find()) {

            double value = Double.parseDouble(matcher.group());

            double scaled = value * factor;

            matcher.appendReplacement(result,
                    String.valueOf(scaled));
        }

        matcher.appendTail(result);

        return result.toString();
    }
}
