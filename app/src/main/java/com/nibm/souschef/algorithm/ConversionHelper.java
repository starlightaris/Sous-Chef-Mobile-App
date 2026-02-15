package com.nibm.souschef.algorithm;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConversionHelper {
    public static String convertGramsToOunces(String text) {

        Pattern pattern = Pattern.compile("(\\d+(\\.\\d+)?)\\s*grams");
        Matcher matcher = pattern.matcher(text);

        StringBuffer result = new StringBuffer();

        while (matcher.find()) {

            double grams = Double.parseDouble(matcher.group(1));
            double ounces = grams * 0.035274;

            String replacement = ounces + " ounces";

            matcher.appendReplacement(result, replacement);
        }

        matcher.appendTail(result);

        return result.toString();
    }
}
