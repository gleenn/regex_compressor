package com.gleenn.regex_compressor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Pattern;

public class SimpleTrie implements Trie {
    final private Character character;
    private boolean terminal;
    final private LinkedHashMap<Character, Trie> children;

    public Character getCharacter() {
        return character;
    }

    public LinkedHashMap<Character, Trie> getChildren() {
        return children;
    }

    public boolean isTerminal() {
        return terminal;
    }

    @Override
    public void setTerminal(boolean terminal) {
        this.terminal = terminal;
    }

    public SimpleTrie() {
        this(null, false);
    }

    public SimpleTrie(Character character, boolean terminal) {
        this(character, terminal, new LinkedHashMap<>());
    }

    public SimpleTrie(Character character, boolean terminal, List<Character> childrenCharacters) {
        this.character = character;
        this.terminal = terminal;
        this.children = new LinkedHashMap<>();

        if (childrenCharacters == null) return;
        for (Character child : childrenCharacters) {
            this.children.put(child, new SimpleTrie(child, true));
        }
    }

    public SimpleTrie(Character character, boolean terminal, LinkedHashMap<Character, Trie> children) {
        if (children == null) {
            throw new RuntimeException("Children cannot be null");
        }
        this.character = character;
        this.terminal = terminal;
        this.children = children;
    }

    public Trie addWord(String word) {
        return addWord(this, word);
    }

    private static Trie addWord(Trie parent, String word) {
        int wordLength = word.length();
        if (wordLength == 0) return parent;

        char c = word.charAt(0);
        Trie insertionNode = parent.getChildren().get(c);
        if (insertionNode == null) {
            insertionNode = new SimpleTrie(c, wordLength == 1);
            parent.getChildren().put(c, insertionNode);
        } else if (wordLength == 1) {
            insertionNode.setTerminal(true);
            return insertionNode;
        }
        return addWord(insertionNode, word.substring(1));
    }

    public Trie addReverseWord(final String word) {
        return addReverseWord(this, word);
    }

    private Trie addReverseWord(final Trie parent, final String word) {
        int wordLength = word.length();
        if (wordLength == 0) return parent;

        char c = word.charAt(wordLength - 1);
        Trie insertionNode = parent.getChildren().get(c);
        if (insertionNode == null) {
            insertionNode = new SimpleTrie(c, wordLength == 1);
            parent.getChildren().put(c, insertionNode);
        } else if (wordLength == 1) {
            insertionNode.setTerminal(true);
            return insertionNode;
        }
        return addReverseWord(insertionNode, word.substring(0, wordLength - 1));
    }

    public static Trie buildPrefixTrie(List<String> strings) {
        Trie trie = new SimpleTrie();
        for (String string : strings) {
            trie.addWord(string);
        }
        return trie;
    }

    public static Trie buildSuffixTrie(List<String> strings) {
        Trie trie = new SimpleTrie();
        for (String string : strings) {
            trie.addReverseWord(string);
        }
        return trie;
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Trie node = (Trie) o;

        if (terminal != node.isTerminal()) return false;
        if (character != null ? !character.equals(node.getCharacter()) : node.getCharacter() != null) return false;
        return children.equals(node.getChildren());
    }

    @Override
    public int hashCode() {
        int result = character != null ? character.hashCode() : 0;
        result = 31 * result + (terminal ? 1 : 0);
        result = 31 * result + children.hashCode();
        return result;
    }

    public static List<String> matchingPrefixes(Trie trie, String input) {
        return matchingPrefixes(trie, input, 0, new ArrayList<String>());
    }

    private static List<String> matchingPrefixes(Trie trie, String input, int offset, List<String> results) {
        if (offset >= input.length()) return results;

        Trie child = trie.get(input.charAt(offset));
        if (child == null) return results;
        if (child.isTerminal()) {
            results.add(input.substring(0, offset + 1));
        }
        return matchingPrefixes(child, input, offset + 1, results);
    }

    public boolean contains(final String word) {
        return contains(this, word);
    }

    private boolean contains(final Trie trie, final String word) {
        final Trie child;
        switch (word.length()) {
            case 0:
                return false;
            case 1:
                child = trie.getChildren().get(word.charAt(0));
                return child != null && child.isTerminal();
            default:
                child = trie.getChildren().get(word.charAt(0));
                return child != null && contains(child, word.substring(1));
        }
    }
}
