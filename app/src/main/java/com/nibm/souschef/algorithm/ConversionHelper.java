package com.nibm.souschef.algorithm;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConversionHelper {

    public static String convertMetricToUS(String text) {

        text = convertGrams(text);
        text = convertKilograms(text);
        text = convertMilliliters(text);
        text = convertCelsius(text);

        return text;
    }

    // grams → ounces
    private static String convertGrams(String text) {

        Pattern pattern = Pattern.compile("(\\d+(\\.\\d+)?)\\s*(g|grams?)",
                Pattern.CASE_INSENSITIVE);

        Matcher matcher = pattern.matcher(text);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {

            double grams = Double.parseDouble(matcher.group(1));
            double ounces = grams * 0.035274;

            String replacement =
                    String.format("%.2f ounces", ounces);

            matcher.appendReplacement(result, replacement);
        }

        matcher.appendTail(result);
        return result.toString();
    }

    // kg → pounds
    private static String convertKilograms(String text) {

        Pattern pattern = Pattern.compile("(\\d+(\\.\\d+)?)\\s*(kg|kilograms?)",
                Pattern.CASE_INSENSITIVE);

        Matcher matcher = pattern.matcher(text);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {

            double kg = Double.parseDouble(matcher.group(1));
            double pounds = kg * 2.20462;

            String replacement =
                    String.format("%.2f pounds", pounds);

            matcher.appendReplacement(result, replacement);
        }

        matcher.appendTail(result);
        return result.toString();
    }

    // ml → cups
    private static String convertMilliliters(String text) {

        Pattern pattern = Pattern.compile("(\\d+(\\.\\d+)?)\\s*(ml|milliliters?)",
                Pattern.CASE_INSENSITIVE);

        Matcher matcher = pattern.matcher(text);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {

            double ml = Double.parseDouble(matcher.group(1));
            double cups = ml / 240.0;

            String replacement =
                    String.format("%.2f cups", cups);

            matcher.appendReplacement(result, replacement);
        }

        matcher.appendTail(result);
        return result.toString();
    }

    // Celsius → Fahrenheit
    private static String convertCelsius(String text) {

        Pattern pattern = Pattern.compile("(\\d+(\\.\\d+)?)\\s*(C|celsius)",
                Pattern.CASE_INSENSITIVE);

        Matcher matcher = pattern.matcher(text);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {

            double c = Double.parseDouble(matcher.group(1));
            double f = (c * 9 / 5) + 32;

            String replacement =
                    String.format("%.1f F", f);

            matcher.appendReplacement(result, replacement);
        }

        matcher.appendTail(result);
        return result.toString();
    }
}
