package com.cwc.certificate.utility;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.StringJoiner;
import java.util.stream.IntStream;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/02/14
 */

/**
 * Enhance code and remove if-else conditions to reduce boilerplate code
 * Use Modern java 8 features to improve performance of the application
 */
@Component
@Slf4j
public class NumberToWordsConverter {

    private static final String[] units = {
            "", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten",
            "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen"
    };

    private static final String[] tens = {
            "", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"
    };

    private static final String[] scaleLabels = {
            "Crore", "Lakh", "Thousand", "Hundred", ""
    };

    public String convert(int n) {
        if (n == 0) {
            return "Zero";
        }

        if (n < 0) {
            return "Minus " + convert(-n);
        }

        return convertToWords(n);
    }

    private String convertToWords(int n) {
        StringJoiner result = new StringJoiner(" ");
        int[] numberParts = {n / 10000000, (n / 100000) % 100, (n / 1000) % 100, (n / 100) % 10, n % 100};
        IntStream.range(0, numberParts.length)
                .filter(i -> numberParts[i] != 0)
                .forEach(i -> {
                    if (i == 3) {
                        result.add(units[numberParts[i]] + " " + scaleLabels[i]);
                        if (numberParts[4] != 0) {
                            result.add("and");
                        }
                    } else {
                        result.add(convertBelowThousand(numberParts[i]) + " " + scaleLabels[i]);
                    }
                });

        return result.toString().trim();
    }
    private String convertBelowThousand(int n) {
        if (n < 20) {
            return units[n];
        } else if (n < 100) {
            return tens[n / 10] + ((n % 10 != 0) ? " " + units[n % 10] : "");
        } else {
            return units[n / 100] + " Hundred" + ((n % 100 != 0) ? " " + convertBelowThousand(n % 100) : "");
        }
    }
}