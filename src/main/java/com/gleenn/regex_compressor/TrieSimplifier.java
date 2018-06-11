package com.gleenn.regex_compressor;

import static com.gleenn.regex_compressor.SimpleTrie.buildPrefixTrie;
import static com.gleenn.regex_compressor.SimpleTrie.buildSuffixTrie;

import java.util.List;

public final class TrieSimplifier {
    public static Trie buildSimplifiedTrie(final List<String> strings) {
        Trie prefixTrie = buildPrefixTrie(strings);
        Trie suffixTrie = buildSuffixTrie(strings);

        return simplify(prefixTrie, suffixTrie);
    }

    static Trie simplify(final Trie prefixNode, final Trie suffixNode) {
        return prefixNode;
    }

    static void combine(final Trie prefixNode, final Trie suffixNode) {

    }
}
