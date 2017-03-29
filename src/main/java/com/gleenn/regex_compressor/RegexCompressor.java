package com.gleenn.regex_compressor;

import java.util.List;

public class RegexCompressor {
    public static String compress(List<String> strings) {
        Trie trie = new Trie();
        for (String string : strings) {
            trie.addWord(string);
        }
        StringBuilder result = new StringBuilder();
        buildRegex(trie, result);
        return result.toString();
    }

    public static void buildRegex(Trie trie, StringBuilder result) {
        Character character = trie.getCharacter();
        if(character == null) return;

        result.append(character);
        for (Trie child : trie.getChildren()) {
            
        }
    }

    private static String escape(char c) {
        return "\\ ^ $ * + ? . | ( ) { } [ ]";
    }
}
