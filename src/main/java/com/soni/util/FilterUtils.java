package com.soni.util;

import java.util.ArrayList;
import java.util.List;

public class FilterUtils {
    //Use generics to allow type-specific filtering
    public static <T> ArrayList<T> filter(List<T> items, Filter<T> filter) {
        ArrayList<T> filteredItems = new ArrayList<>();
        for (T item : items) {
            if (filter.getFilter(item)) {
                filteredItems.add(item);
            }
        }
        return filteredItems;
    }

    //Define a generic interface for filters
    public interface Filter<T> {
        boolean getFilter(T item);
    }
}

