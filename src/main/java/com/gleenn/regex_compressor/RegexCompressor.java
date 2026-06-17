package com.gleenn.regex_compressor;

import static com.gleenn.regex_compressor.SimpleTrie.*;
import static java.util.regex.Pattern.compile;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class RegexCompressor {
    private static final String REGEX_THAT_MATCHES_NOTHING = "(?!.)";

    public static Pattern pattern(List<String> strings) {
        return compile(compress(strings, Options.defaultOptions()));
    }

    public static Pattern pattern(final List<String> strings, final Options options) {
        return compile(compress(strings, options));
    }

    public static com.google.re2j.Pattern patternRE2(List<String> strings) {
        return com.google.re2j.Pattern.compile(compress(strings, Options.defaultOptions()));
    }

    public static com.google.re2j.Pattern patternRE2(final List<String> strings, final Options options) {
        return com.google.re2j.Pattern.compile(compress(strings, options));
    }

    private static String compress(List<String> strings, final Options options) {
        if(strings.isEmpty()) return REGEX_THAT_MATCHES_NOTHING;
        Trie trie = buildPrefixTrie(options.isCaseSensitive() ?
                strings :
                strings.stream().map(String::toLowerCase).collect(Collectors.toList()));
        
        // Optimize the trie to use patty trie structure
        SimpleTrie.optimize(trie);
        
        return buildRegex(trie, options);
    }

    private static String buildRegex(final Trie trie, final Options options) {
        StringBuilder result = new StringBuilder();

        if(!options.isCaseSensitive()) result.append("(?i)");

        boolean prefixOrSuffixPresent = options.getPrefix() != null || options.getSuffix() != null;

        if(options.getPrefix() != null) result.append(options.getPrefix());
        if(prefixOrSuffixPresent) result.append("(?:");

        buildRegex(trie, result);

        if(prefixOrSuffixPresent) result.append(")");
        if(options.getSuffix() != null) result.append(options.getSuffix());
        return result.toString();
    }

    public static void buildRegex(final Trie trie, final StringBuilder result) {
        if(trie == null) throw new RuntimeException("Trie cannot be null");
        
        // Handle the case where this node has a string value (patty trie optimization)
        if (trie.getString() != null) {
            result.append(escapeString(trie.getString()));
            // If this is a terminal node, add the optional marker
            if (trie.isTerminal()) {
                result.append("?");
            }
            return;
        }
        
        Character character = trie.getCharacter();
        
        if(character != null) {
            result.append(escape(character));
        }
        
        LinkedHashMap<Character, Trie> childrenTries = trie.getChildren();

        if(childrenTries.isEmpty()) {
            // If this is a terminal node, add the optional marker
            if (trie.isTerminal()) {
                result.append("?");
            }
            return;
        }

        // Check if we can use the patty trie optimization for children
        if(hasOnlyChild(trie) && (hasNoChildren(getOnlyChild(trie)) || !trie.isTerminal())) {
            // This is a chain of single children - continue recursively
            for(Trie child : childrenTries.values()) buildRegex(child, result);
        } else {
            boolean allOnlyChildren = true;
            for(Trie child : childrenTries.values()) {
                allOnlyChildren &= hasNoChildren(child) && child.isTerminal();
            }

            if(allOnlyChildren) {
                result.append("[");
                for(Trie child : childrenTries.values()) {
                    buildRegex(child, result);
                }
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
    }

    public static String escape(char c) {
        switch(c) {
            case '(': return "\\(";
            case ')': return "\\)";
            case '{': return "\\{";
            case '}': return "\\}";
            case '[': return "\\[";
            case ']': return "\\]";
            case '.': return "\\.";
            case '+': return "\\+";
            case '*': return "\\*";
            case '?': return "\\?";
            case '^': return "\\^";
            case '<': return "\\<";
            case '>': return "\\>";
            case '-': return "\\-";
            case '=': return "\\=";
            case '!': return "\\!";
            case '$': return "\\$";
            case '\\': return "\\\\";
            case '|': return "\\|";
            default: return c + "";
        }
    }
    
    public static String escapeString(String s) {
        StringBuilder result = new StringBuilder();
        for (char c : s.toCharArray()) {
            result.append(escape(c));
        }
        return result.toString();
    }
}
