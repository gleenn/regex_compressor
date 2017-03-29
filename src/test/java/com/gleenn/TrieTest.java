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

        Trie trieA = root.addWord("a");
        assertThat(trieA.isTerminal(), is(true));

        Trie trieC = root.addWord("abc");
        assertThat(trieC.isTerminal(), is(true));

        Trie trieB = trieA.getChildren().get('b');
        assertThat(trieB.isTerminal(), is(false));
    }

    @Test
    public void equalsTest() {
        assertThat(new Trie().equals(new Trie()), is(true));
        assertThat(new Trie().addWord("a").equals(new Trie().addWord("a")), is(true));

        Trie aFirst = new Trie();
        aFirst.addWord("a").addWord("b");
        Trie bFirst = new Trie();
        bFirst.addWord("b").addWord("a");
        assertThat(aFirst.equals(bFirst), is(false));
    }
}
