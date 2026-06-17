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
        // Optimize both tries using patty trie technique
        SimpleTrie.optimize(prefixNode);
        SimpleTrie.optimize(suffixNode);
        return prefixNode;
    }

    static void combine(final Trie prefixNode, final Trie suffixNode) {
        // This method would combine prefix and suffix tries if needed
        // For now, we just return the prefix trie as it's the main one
    }
}
