package com.skeeper.minicode.models;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// todo: set to json
public class KeySymbolBarModel {
    private static Map<String, String> symbols = new HashMap<>() {{
        put("+", "+");
        put("tab", "\t");
        put(";", ";");
        put("()", "()");
        put("[]", "[]");
        put("{}", "{}");
    }};

    public static void addSymbol(String key, String value) {
        symbols.put(key, value);

    }

    public static void removeSymbol(String key) {
        symbols.remove(key);
    }


    public static Map<String, String> getKeySymbolsMap() {
        return symbols;
    }

}
