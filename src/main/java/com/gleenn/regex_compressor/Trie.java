package com.gleenn.regex_compressor;

import java.util.LinkedHashMap;

public interface Trie {
    Trie addWord(String word);
    Trie addReverseWord(String word);
    Character getCharacter();
    LinkedHashMap<Character, Trie> getChildren();
    boolean isTerminal();
    void setTerminal(boolean terminal);
    boolean contains(String word);
    Trie get(Character c);
}
