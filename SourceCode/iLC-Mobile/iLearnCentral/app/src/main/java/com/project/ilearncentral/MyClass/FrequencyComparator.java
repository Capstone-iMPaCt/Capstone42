package com.project.ilearncentral.MyClass;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class FrequencyComparator implements Comparator<Integer> {

    private final Map<String, Integer> map;

    public FrequencyComparator(Map<String, Integer> map) {
        this.map = map;
    }

    @Override
    public int compare(Integer o1, Integer o2) {
        // Compare value by frequency
        int frequencyCompare = map.get(o2).compareTo(map.get(o1));

        // Compare value if frequency is equal
        int valueCompare = o1.compareTo(o2);

        // If frequency is equal, then just compare by value, otherwise -
        // compare by the frequency.
        if (frequencyCompare == 0)
            return valueCompare;
        else
            return frequencyCompare;
    }

    public static <K, V extends Comparable<? super V>> List<Map.Entry<K, V>>
    findGreatest(Map<K, V> map, int n) {
        Comparator<? super Map.Entry<K, V>> comparator =
                new Comparator<Map.Entry<K, V>>() {
                    @Override
                    public int compare(Map.Entry<K, V> e0, Map.Entry<K, V> e1) {
                        V v0 = e0.getValue();
                        V v1 = e1.getValue();
                        return v0.compareTo(v1);
                    }
                };
        PriorityQueue<Map.Entry<K, V>> highest = new PriorityQueue<Map.Entry<K, V>>(n, comparator);
        for (Map.Entry<K, V> entry : map.entrySet()) {
            highest.offer(entry);
            while (highest.size() > n) {
                highest.poll();
            }
        }
        List<Map.Entry<K, V>> result = new ArrayList<Map.Entry<K, V>>();
        while (highest.size() > 0) {
            result.add(highest.poll());
        }
        return result;
    }
}
