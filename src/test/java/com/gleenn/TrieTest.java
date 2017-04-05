package com.gleenn;

import com.gleenn.regex_compressor.Trie;
import static com.gleenn.regex_compressor.Trie.hasNoChildren;
import static com.gleenn.regex_compressor.Trie.hasOnlyChild;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
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
