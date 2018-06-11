package com.gleenn;

import com.gleenn.regex_compressor.Trie;
import static com.gleenn.regex_compressor.TrieImpl.hasNoChildren;
import static com.gleenn.regex_compressor.TrieImpl.hasOnlyChild;
import com.gleenn.regex_compressor.TrieImpl;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import org.testng.annotations.Test;

public class TrieImplTest {
    @Test
    public void addWordTest_setsTerminalPropertyCorrectly() {
        Trie root = new TrieImpl();
        assertThat(root.isTerminal(), is(false));

        Trie trieA = root.addWord("a");
        assertThat(trieA.isTerminal(), is(true));

        Trie trieC = root.addWord("abc");
        assertThat(trieC.isTerminal(), is(true));

        Trie trieB = trieA.get('b');
        assertThat(trieB.isTerminal(), is(false));
    }

    @Test
    public void addWordTest_setsTerminalPropertyCorrectly_evenWithShorterOverlapper() {
        Trie root = new TrieImpl();
        root.addWord("abcd");
        root.addWord("a");
        assertThat(root.get('a').isTerminal(), is(true));

        TrieImpl rootBackwards = new TrieImpl();
        rootBackwards.addWord("a");
        rootBackwards.addWord("abcd");
        assertThat(rootBackwards.get('a').isTerminal(), is(true));
    }

    @Test
    public void addReverseWord_reversesStringBeforeAddingToTrie() {
        Trie root = new TrieImpl();
        root.addReverseWord("cat");
        assertThat(root.contains("tac"), is(true));
    }

    @Test
    public void contains_returnsTrueWhenTrieContainsWord() {
        Trie root = new TrieImpl();
        assertThat(root.contains("dog"), is(false));
        root.addWord("dog");
        assertThat(root.contains("dog"), is(true));
        assertThat(root.contains("cat"), is(false));
    }

    @Test
    public void constructorTest_withList() {
        Trie trie = new TrieImpl('a', false, asList('b'));
        assertThat(trie.isTerminal(), is(false));
        assertThat(trie.get('b').isTerminal(), is(true));
    }

    @Test
    public void get_returnsSubTrieMatchingCharacter() {
        Trie trie = new TrieImpl();
        trie.addWord("a");
        trie.addWord("bc");
        assertThat(trie.get('b').get('c').isTerminal(), is(true));
    }

    @Test
    public void equalsTest() {
        assertThat(new TrieImpl().equals(new TrieImpl()), is(true));
        assertThat(new TrieImpl().addWord("a").equals(new TrieImpl().addWord("a")), is(true));

        TrieImpl aFirst = new TrieImpl();
        aFirst.addWord("a").addWord("b");
        TrieImpl bFirst = new TrieImpl();
        bFirst.addWord("b").addWord("a");
        assertThat(aFirst.equals(bFirst), is(false));

        TrieImpl root1 = new TrieImpl();
        root1.addWord("a").addWord("b").addWord("c");
        Trie root2 = new TrieImpl();
        root2.addWord("a").addWord("b").addWord("c");
        assertThat(root1, equalTo(root2));
        assertThat(root2, equalTo(root1));

        root1.get('a').get('b').addWord("d");
        assertThat(root1, not(equalTo(root2)));
        assertThat(root2, not(equalTo(root1)));
    }

    @Test
    public void hasOnlyChildTest() {
        Trie trie = new TrieImpl();
        assertThat(hasOnlyChild(trie), is(false));
        trie.addWord("a");
        assertThat(hasOnlyChild(trie), is(true));
        trie.addWord("b");
        assertThat(hasOnlyChild(trie), is(false));
    }

    @Test
    public void hasNoChildrenTest() {
        Trie trie = new TrieImpl();
        assertThat(hasNoChildren(trie), is(true));
        trie.addWord("a");
        assertThat(hasNoChildren(trie), is(false));
    }
}
