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
        buildRegex(trie, result);
        return result.toString();
    }

    public static void buildRegex(Trie trie, StringBuilder result) {
        if(trie == null) throw new RuntimeException("Trie cannot be null");
        Character character = trie.getCharacter();
        System.out.println("My name is " + character + " and I am " + (trie.isTerminal() ? "" : "not") + " terminal");
        if(character != null) {
            result.append(character);
        }

        LinkedHashMap<Character, Trie> childrenTries = trie.getChildren();

        if(childrenTries.isEmpty()) {
            return;
        }

        if(!trie.isTerminal() || childrenTries.size() > 1) {
            for(Trie child : childrenTries.values()) {
                buildRegex(child, result);
            }
        } else {
            result.append("(?:");
            for(Trie child : childrenTries.values()) {
                buildRegex(child, result);
                result.append("|");
            }
            result.deleteCharAt(result.length()-1); // remove extra "|"
            result.append(")");
        }
        if(trie.isTerminal()) {
            result.append("?");
        }
    }

    private static String escape(char c) {
        return "\\ ^ $ * + ? . | ( ) { } [ ]";
    }
}
