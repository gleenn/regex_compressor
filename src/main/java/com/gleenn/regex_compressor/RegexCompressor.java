package com.gleenn.regex_compressor;

import java.util.LinkedHashMap;
import java.util.List;

public class RegexCompressor {
    public static String compress(List<String> strings) {
        Trie trie = new Trie();
        for(String string : strings) {
            trie.addWord(string);
        }
        StringBuilder result = new StringBuilder();
        buildRegex(trie, true, result);
        return result.toString();
    }

    public static void buildRegex(Trie trie, StringBuilder result) {
        buildRegex(trie, false, result);
    }

    public static void buildRegex(Trie trie, boolean skipParens, StringBuilder result) {
        if(trie == null) throw new RuntimeException("Trie cannot be null");
        Character character = trie.getCharacter();
        if(character != null) {
            result.append(character);
        }

        LinkedHashMap<Character, Trie> childrenTries = trie.getChildren();

        if(childrenTries.isEmpty()) {
            return;
        }

        if(childrenTries.size() == 1) {
            for(Trie child : childrenTries.values()) {
                buildRegex(child, result);
            }
        } else {
            if(!skipParens) {
                result.append("(?:");
            }
            for(Trie child : childrenTries.values()) {
                buildRegex(child, result);
                result.append("|");
            }
            result.deleteCharAt(result.length()-1); // remove extra "|"
            if(!skipParens) {
                result.append(")");
            }
        }

        if(!trie.isTerminal()) {
            result.append("?");
        }
    }

    private static String escape(char c) {
        return "\\ ^ $ * + ? . | ( ) { } [ ]";
    }
}
