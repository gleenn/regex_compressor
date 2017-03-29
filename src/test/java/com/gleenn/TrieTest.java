package com.gleenn;

import com.gleenn.regex_compressor.Trie;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.Test;

public class TrieTest {
    @Test
    public void addWordTest() {
        Trie root = new Trie();
        assertThat(root.isTerminal(), is(false));

        Trie child = root.addWord("a");
        assertThat(child.isTerminal(), is(true));
    }
}
