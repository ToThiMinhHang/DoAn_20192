package com.hang.doan.readbooks.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Transform {
    public static <T, R> List<R> transform(Iterable<T> list, Function<T, R> function) {
        List<R> results = new ArrayList<>();
        for (T item: list) {
            R result = function.apply(item);
            results.add(result);
        }
        return results;
    }
}
