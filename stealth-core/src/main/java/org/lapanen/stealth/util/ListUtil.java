package org.lapanen.stealth.util;

import java.util.ArrayList;
import java.util.List;

public class ListUtil {

    public static <T> void section(final List<T> original, final List<T> replacement, final List<T> droppedOut, final List<T> newElements) {
        final List<T> remainder = new ArrayList<T>(replacement);
        for (T originalElement : original) {
            if (!replacement.contains(originalElement)) {
                droppedOut.add(originalElement);
            } else {
                remainder.remove(originalElement);
            }
        }
        newElements.addAll(remainder);
    }

}
