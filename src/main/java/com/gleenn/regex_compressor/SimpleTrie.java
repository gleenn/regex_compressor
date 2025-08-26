package com.gleenn.regex_compressor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class SimpleTrie implements Trie {
    final private Character character;
    private String path;
    private boolean terminal;
    final private LinkedHashMap<Character, Trie> children;

    public Character getCharacter() {
        return character;
    }

    public String getPath() {
        return path;
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
        this(null, null, false);
    }

    public SimpleTrie(Character character, boolean terminal) {
        this(character, character != null ? character.toString() : null, terminal, new LinkedHashMap<>());
    }

    public SimpleTrie(Character character, String path, boolean terminal) {
        this(character, path, terminal, new LinkedHashMap<>());
    }

    public SimpleTrie(Character character, boolean terminal, List<Character> childrenCharacters) {
        this.character = character;
        this.path = character != null ? character.toString() : null;
        this.terminal = terminal;
        this.children = new LinkedHashMap<>();

        if (childrenCharacters == null) return;
        for (Character child : childrenCharacters) {
            this.children.put(child, new SimpleTrie(child, true));
        }
    }

    public SimpleTrie(Character character, boolean terminal, LinkedHashMap<Character, Trie> children) {
        this(character, character != null ? character.toString() : null, terminal, children);
    }

    public SimpleTrie(Character character, String path, boolean terminal, LinkedHashMap<Character, Trie> children) {
        if (children == null) {
            throw new RuntimeException("Children cannot be null");
        }
        this.character = character;
        this.path = path;
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
        
        if (wordLength > 1) {
            return addWord(insertionNode, word.substring(1));
        }
        return insertionNode;
    }

    public Trie addReverseWord(final String word) {
        return addReverseWord(this, word);
    }

    private Trie addReverseWord(final Trie parent, final String word) {
        int wordLength = word.length();
        if (wordLength == 0) return parent;

        String reversedWord = new StringBuilder(word).reverse().toString();
        return addWord(parent, reversedWord);
    }

    public static Trie buildPrefixTrie(List<String> strings) {
        Trie trie = new SimpleTrie();
        for (String string : strings) {
            trie.addWord(string);
        }
        return trie;
    }
    
    public static Trie optimizeRadixPaths(Trie trie) {
        if (trie.getChildren().isEmpty()) {
            return trie;
        }
        
        LinkedHashMap<Character, Trie> optimizedChildren = new LinkedHashMap<>();
        
        for (Map.Entry<Character, Trie> entry : trie.getChildren().entrySet()) {
            Trie child = optimizeRadixPaths(entry.getValue());
            
            String chainPath = extractLinearChain(child);
            if (chainPath.length() > 4) {
                Trie endNode = findEndOfChain(child);
                SimpleTrie compressedNode = new SimpleTrie(
                    child.getCharacter(),
                    chainPath,
                    endNode.isTerminal(),
                    new LinkedHashMap<>(endNode.getChildren())
                );
                optimizedChildren.put(entry.getKey(), compressedNode);
            } else {
                optimizedChildren.put(entry.getKey(), child);
            }
        }
        
        return new SimpleTrie(
            ((SimpleTrie) trie).getCharacter(),
            ((SimpleTrie) trie).getPath(),
            trie.isTerminal(),
            optimizedChildren
        );
    }
    
    private static String extractLinearChain(Trie node) {
        StringBuilder chain = new StringBuilder();
        Trie current = node;
        
        while (current != null && 
               current.getChildren().size() <= 1 && 
               !current.isTerminal()) {
            chain.append(current.getCharacter());
            if (current.getChildren().size() == 1) {
                current = current.getChildren().values().iterator().next();
            } else {
                break;
            }
        }
        
        if (current != null) {
            chain.append(current.getCharacter());
        }
        
        return chain.toString();
    }
    
    private static Trie findEndOfChain(Trie node) {
        Trie current = node;
        
        while (current != null && 
               current.getChildren().size() == 1 && 
               !current.isTerminal()) {
            current = current.getChildren().values().iterator().next();
        }
        
        return current;
    }
    
    private static Trie compressRadixPaths(Trie trie) {
        if (trie.getChildren().isEmpty()) {
            return trie;
        }
        
        LinkedHashMap<Character, Trie> compressedChildren = new LinkedHashMap<>();
        
        for (Map.Entry<Character, Trie> entry : trie.getChildren().entrySet()) {
            Trie child = compressRadixPaths(entry.getValue());
            
            if (!child.isTerminal() && child.getChildren().size() == 1) {
                Trie grandchild = child.getChildren().values().iterator().next();
                String compressedPath = child.getCharacter() + ((SimpleTrie) grandchild).getPath();
                SimpleTrie compressedNode = new SimpleTrie(
                    child.getCharacter(),
                    compressedPath,
                    grandchild.isTerminal(),
                    new LinkedHashMap<>(grandchild.getChildren())
                );
                compressedChildren.put(entry.getKey(), compressedNode);
            } else {
                compressedChildren.put(entry.getKey(), child);
            }
        }
        
        return new SimpleTrie(
            ((SimpleTrie) trie).getCharacter(),
            ((SimpleTrie) trie).getPath(),
            trie.isTerminal(),
            compressedChildren
        );
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
        if (path != null ? !path.equals(((SimpleTrie) node).getPath()) : ((SimpleTrie) node).getPath() != null) return false;
        return children.equals(node.getChildren());
    }

    @Override
    public int hashCode() {
        int result = character != null ? character.hashCode() : 0;
        result = 31 * result + (path != null ? path.hashCode() : 0);
        result = 31 * result + (terminal ? 1 : 0);
        result = 31 * result + children.hashCode();
        return result;
    }

    public static List<String> matchingPrefixes(Trie trie, String input) {
        return matchingPrefixes(trie, input, 0, false, new ArrayList<String>());
    }

    public static List<String> matchingPrefixes(Trie trie, String input, boolean enforceWordBoundary) {
        return matchingPrefixes(trie, input, 0, enforceWordBoundary, new ArrayList<String>());
    }

    public final static Pattern wordBoundary = Pattern.compile("\\A(?:\\W|\\Z)");

    private static List<String> matchingPrefixes(Trie trie, String input, int offset, boolean enforceWordBoundary, List<String> results) {
        if (offset >= input.length()) return results;

        Trie child = trie.get(input.charAt(offset));
        if (child == null) return results;
        
        String childPath = ((SimpleTrie) child).getPath();
        if (childPath == null) childPath = String.valueOf(child.getCharacter());
        
        if (offset + childPath.length() <= input.length() && 
            input.substring(offset, offset + childPath.length()).equals(childPath)) {
            
            int newOffset = offset + childPath.length();
            if (child.isTerminal()) {
                String prefix = input.substring(0, newOffset);
                if (!enforceWordBoundary || wordBoundary.matcher(input.substring(newOffset)).find())
                    results.add(prefix);
            }
            return matchingPrefixes(child, input, newOffset, enforceWordBoundary, results);
        }
        
        return results;
    }

    public boolean contains(final String word) {
        return contains(this, word);
    }

    private boolean contains(final Trie trie, final String word) {
        if (word.length() == 0) {
            return trie.isTerminal();
        }
        
        final Trie child = trie.getChildren().get(word.charAt(0));
        if (child == null) return false;
        
        String childPath = ((SimpleTrie) child).getPath();
        if (childPath == null) childPath = String.valueOf(child.getCharacter());
        
        if (word.length() < childPath.length()) {
            return false;
        } else if (word.length() == childPath.length()) {
            return word.equals(childPath) && child.isTerminal();
        } else if (word.startsWith(childPath)) {
            return contains(child, word.substring(childPath.length()));
        } else {
            return false;
        }
    }
}