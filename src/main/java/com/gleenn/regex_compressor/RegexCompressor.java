package com.gleenn.regex_compressor;

import java.util.LinkedHashMap;
import java.util.List;

public class RegexCompressor {
    public static String compress(List<String> strings) {
        return compress(strings, false);
    }

    public static String compress(List<String> strings, boolean allowEmpty) {
        Trie trie = new Trie();
        for(String string : strings) {
            trie.addWord(string);
        }
        StringBuilder result = new StringBuilder();
        buildRegex(trie, allowEmpty, result);
        return result.toString();
    }

    public static void buildRegex(Trie trie, boolean optional, StringBuilder result) {
        if(trie == null) return;
        Character character = trie.getCharacter();
        if(character == null) return;

        result.append(character);
        LinkedHashMap<Character, Trie> childrenTries = trie.getChildren();

        if(childrenTries.isEmpty()) return;

        if(childrenTries.size() == 1) {
            for(Trie child : childrenTries.values()) {
                buildRegex(child, optional, result);
            }
        } else {
            result.append("(?:");
            for(Trie child : childrenTries.values()) {
                buildRegex(child, optional, result);
            }
            result.append(")");
        }

        if(optional) {
            result.append("?");
        }
    }

    private static String escape(char c) {
        return "\\ ^ $ * + ? . | ( ) { } [ ]";
    }
}
