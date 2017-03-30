package com.gleenn.regex_compressor;

import static com.gleenn.regex_compressor.Trie.*;
import static java.lang.System.out;

import java.util.ArrayList;
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
        out.print("Char: ");
        if(character != null) {
            result.append(character);
        }
        out.print(character + " has ");

        LinkedHashMap<Character, Trie> childrenTries = trie.getChildren();

        out.println(childrenTries.size() + " children and is terminal " + (trie.isTerminal() ? "t" : "f"));
        if(childrenTries.isEmpty()) {
            return;
        }

        if(hasOnlyChild(trie) &&
           hasNoChildren(getOnlyChild(trie))
//                &&
//           getOnlyChild(trie).isTerminal()
           ||
           !trie.isTerminal() &&
           hasOnlyChild(trie) &&
           hasAtMostOneChild((getOnlyChild(trie))))
        {
            for(Trie child : childrenTries.values()) {
                buildRegex(child, result);
            }
        } else {
            if(character != null) result.append("(?:");
            for(Trie child : childrenTries.values()) {
                buildRegex(child, result);
                result.append("|");
            }
            result.deleteCharAt(result.length() - 1); // remove extra "|"
            if(character != null) result.append(")");
        }

        if(trie.isTerminal()) {
            result.append("?");
        }
    }

//    private static String escape(char c) {
//        return "\\ ^ $ * + ? . | ( ) { } [ ]";
//    }
}
