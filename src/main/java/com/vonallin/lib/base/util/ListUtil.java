package com.vonallin.lib.base.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ListUtil {
    public static List<Object> distinctList(List list) {
        Set<Object> set = new HashSet<>(list);
        return new ArrayList<>(set);
    }
}
