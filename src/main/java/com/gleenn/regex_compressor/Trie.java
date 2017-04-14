package com.gleenn.regex_compressor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

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

    public boolean isTerminal() {
        return terminal;
    }

    public Trie() {
        this(null, false);
    }

    public Trie(Character character, boolean terminal) {
        this(character, terminal, new LinkedHashMap<>());
    }

    public Trie(Character character, boolean terminal, List<Character> childrenCharacters) {
        this.character = character;
        this.terminal = terminal;
        this.children = new LinkedHashMap<>();

        if(childrenCharacters == null) return;
        for(Character child : childrenCharacters) {
            this.children.put(child, new Trie(child, true));
        }
    }

    public Trie(Character character, boolean terminal, LinkedHashMap<Character, Trie> children) {
        if(children == null) {
            throw new RuntimeException("Children cannot be null");
        }
        this.character = character;
        this.terminal = terminal;
        this.children = children;
    }

    public Trie addWord(String word) {
        return addWord(this, word);
    }

    public static Trie addWord(Trie parent, String word) {
        int wordLength = word.length();
        if(wordLength == 0) return parent;

        char c = word.charAt(0);
        Trie insertionNode = parent.children.get(c);
        if(insertionNode == null) {
            insertionNode = new Trie(c, wordLength == 1);
            parent.children.put(c, insertionNode);
        } else if(wordLength == 1) {
            insertionNode.terminal = true;
            return insertionNode;
        }
        return addWord(insertionNode, word.substring(1));
    }

    public Trie get(Character c) {
        return children.get(c);
    }

    public static Trie getOnlyChild(final Trie trie) {
        return new ArrayList<>(trie.getChildren().values()).get(0);
    }

    public static boolean hasOnlyChild(final Trie trie) {
        return trie.getChildren().size() == 1;
    }

    public static boolean hasAtMostOneChild(final Trie trie) {
        return trie.getChildren().size() <= 1;
    }

    public static boolean hasChildren(final Trie trie) {
        return trie.getChildren().size() > 0;
    }

    public static boolean hasNoChildren(final Trie trie) {
        return trie.getChildren().size() == 0;
    }

    public static boolean isTerminal(Trie trie) {
        return trie.isTerminal();
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        Trie node = (Trie) o;

        if(terminal != node.terminal) return false;
        if(character != null ? !character.equals(node.character) : node.character != null) return false;
        return children.equals(node.children);
    }

    @Override
    public int hashCode() {
        int result = character != null ? character.hashCode() : 0;
        result = 31 * result + (terminal ? 1 : 0);
        result = 31 * result + children.hashCode();
        return result;
    }

    public boolean contains(final String word) {
        return contains(this, word);
    }

    private boolean contains(final Trie trie, final String word) {
        final Trie child;
        switch(word.length()) {
            case 0:
                return false;
            case 1:
                child = trie.children.get(word.charAt(0));
                return child != null && child.isTerminal();
            default:
                child = trie.children.get(word.charAt(0));
                return child != null && contains(child, word.substring(1));
        }
    }
}
