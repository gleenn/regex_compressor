package com.gleenn.regex_compressor;

import static com.gleenn.regex_compressor.Trie.*;

import java.util.LinkedHashMap;
import java.util.List;

public final class RegexCompressor {
    public static String compress(List<String> strings) {
        Trie trie = new Trie();
        for(String string : strings) {
            trie.addWord(string);
        }
        StringBuilder result = new StringBuilder();
        buildRegex(trie, result);
        return result.toString();
    }

    public static void buildRegex(final Trie trie, final StringBuilder result) {
        if(trie == null) throw new RuntimeException("Trie cannot be null");
        Character character = trie.getCharacter();
        if(character != null) {
            result.append(character);
        }
        LinkedHashMap<Character, Trie> childrenTries = trie.getChildren();

        if(childrenTries.isEmpty()) return;

        if(hasOnlyChild(trie) && (hasNoChildren(getOnlyChild(trie)) || !trie.isTerminal())) {
            for(Trie child : childrenTries.values()) buildRegex(child, result);
        } else {
            boolean allOnlyChildren = true;
            for(Trie child : childrenTries.values()) {
                allOnlyChildren &= hasNoChildren(child) && child.isTerminal();
            }

            if(allOnlyChildren) {
                result.append("[");
                for(Trie child : childrenTries.values()) buildRegex(child, result);
                result.append("]");
            } else {
                if(character != null) result.append("(?:");
                for(Trie child : childrenTries.values()) {
                    buildRegex(child, result);
                    result.append("|");
                }
                result.deleteCharAt(result.length() - 1);
                if(character != null) result.append(")");
            }
        }

        if(trie.isTerminal()) result.append("?");
    }

//    private static String escape(char c) {
//        return "\\ ^ $ * + ? . | ( ) { } [ ]";
//    }
}
