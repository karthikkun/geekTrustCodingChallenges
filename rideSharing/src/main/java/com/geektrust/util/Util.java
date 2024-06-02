package com.geektrust.util;

import java.util.Comparator;
import java.util.Map;

public class Util {
    public static final Comparator<Map.Entry<String, Double>> COMPARATOR =
            (Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) -> {
                int valueComparison = Double.compare(o1.getValue(), o2.getValue());
                if (valueComparison == 0)
                    return o1.getKey().compareTo(o2.getKey());
                return valueComparison;
            };
}
