package com.gleenn;

import com.gleenn.regex_compressor.Trie;
import static com.gleenn.regex_compressor.Trie.hasNoChildren;
import static com.gleenn.regex_compressor.Trie.hasOnlyChild;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import org.testng.annotations.Test;

public class TrieTest {
    @Test
    public void addWordTest_setsTerminalPropertyCorrectly() {
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
    public void addWordTest_setsTerminalPropertyCorrectly_evenWithShorterOverlapper() {
        Trie root = new Trie();
        root.addWord("abcd");
        root.addWord("a");
        assertThat(root.getChildren().get('a').isTerminal(), is(true));

        Trie rootBackwards = new Trie();
        rootBackwards.addWord("a");
        rootBackwards.addWord("abcd");
        assertThat(rootBackwards.getChildren().get('a').isTerminal(), is(true));
    }

    @Test
    public void addReverseWord_reversesStringBeforeAddingToTrie() {
        Trie root = new Trie();
        root.addReverseWord("cat");
        assertThat(root.contains("tac"), is(true));
    }

    @Test
    public void contains_returnsTrueWhenTrieContainsWord() {
        Trie root = new Trie();
        assertThat(root.contains("dog"), is(false));
        root.addWord("dog");
        assertThat(root.contains("dog"), is(true));
        assertThat(root.contains("cat"), is(false));
    }

    @Test
    public void constructorTest_withList() {
        Trie trie = new Trie('a', false, asList('b'));
        assertThat(trie.isTerminal(), is(false));
        assertThat(trie.getChildren().get('b').isTerminal(), is(true));
    }

    @Test
    public void getTest_returnsSubTrieMatchingCharacter() {
        Trie trie = new Trie();
        trie.addWord("a");
        trie.addWord("bc");
        assertThat(trie.get('b').get('c').isTerminal(), is(true));
        assertThat(trie.get('b').get('c').getChildren().size(), is(0));
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

        Trie root1 = new Trie();
        root1.addWord("a").addWord("b").addWord("c");
        Trie root2 = new Trie();
        root2.addWord("a").addWord("b").addWord("c");
        assertThat(root1, equalTo(root2));
        assertThat(root2, equalTo(root1));

        root1.getChildren().get('a').getChildren().get('b').addWord("d");
        assertThat(root1, not(equalTo(root2)));
        assertThat(root2, not(equalTo(root1)));
    }

    @Test
    public void hasOnlyChildTest() {
        Trie trie = new Trie();
        assertThat(hasOnlyChild(trie), is(false));
        trie.addWord("a");
        assertThat(hasOnlyChild(trie), is(true));
        trie.addWord("b");
        assertThat(hasOnlyChild(trie), is(false));
    }

    @Test
    public void hasNoChildrenTest() {
        Trie trie = new Trie();
        assertThat(hasNoChildren(trie), is(true));
        trie.addWord("a");
        assertThat(hasNoChildren(trie), is(false));
    }
}
