package com.gleenn.regex_compressor;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Trie {
    final Character character;
    boolean terminal;
    final LinkedHashMap<Character, Trie> children;

    public Character getCharacter() {
        return character;
    }

    public LinkedHashMap<Character, Trie> getChildren() {
        return children;
    }

    public boolean hasNoChildren() {
        return children.isEmpty();
    }

    public boolean isTerminal() {
        return terminal;
    }

    public Trie() {
        this(null, false);
    }

    public Trie(Character character, boolean terminal) {
        this(character, terminal, null);
    }

    public Trie(Character character, boolean terminal, List<Character> childrenCharacters) {
        this.character = character;
        this.terminal = terminal;
        this.children = new LinkedHashMap<Character, Trie>();

        if(childrenCharacters == null) return;
        for (Character child : childrenCharacters) {
            this.children.put(child, new Trie(child, true));
        }
    }

    public Trie addWord(String word) {
        return addWord(this, word);
    }

    public static Trie addWord(Trie parent, String word) {
        int wordLength = word.length();
        if (wordLength == 0) return parent;

        char c = word.charAt(0);
        Trie node = parent.children.get(c);
        if (node == null) {
            node = new Trie(c, wordLength > 1);
            parent.children.put(c, node);
        } else if (wordLength == 1) {
            node.terminal = true;
        }
        return addWord(node, word.substring(1));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Trie node = (Trie) o;

        if (terminal != node.terminal) return false;
        if (character != null ? !character.equals(node.character) : node.character != null) return false;
        return children.equals(node.children);
    }

    @Override
    public int hashCode() {
        int result = character != null ? character.hashCode() : 0;
        result = 31 * result + (terminal ? 1 : 0);
        result = 31 * result + children.hashCode();
        return result;
    }
}
