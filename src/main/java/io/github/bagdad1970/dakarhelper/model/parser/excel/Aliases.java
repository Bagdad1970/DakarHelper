package io.github.bagdad1970.dakarhelper.model.parser.excel;

import java.util.*;

public class Aliases {
    private final Map<String, List<String>> aliasesMap = new HashMap<>();

    public void addAliases(String key, List<String> aliases) {
        aliasesMap.computeIfAbsent(key, k -> new ArrayList<>()).addAll(aliases);
    }

    public Set<String> keySet() {
        return aliasesMap.keySet();
    }

    public boolean hasAliasByKey(String key, String value) {
        String lowerValue = value.toLowerCase().trim();

        return aliasesMap.getOrDefault(key, Collections.emptyList())
                .stream()
                .anyMatch(lowerValue::contains);
    }

    public String getKeyByAlias(String alias) {
        String lowerAlias = alias.toLowerCase().trim();

        for (String key : aliasesMap.keySet()) {
            for (String aliasValue : aliasesMap.get(key)) {
                if (aliasValue.contains(lowerAlias))
                    return key;
            }
        }
        return "";
    }
}