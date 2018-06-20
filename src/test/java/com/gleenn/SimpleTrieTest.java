package com.gleenn;

import com.gleenn.regex_compressor.Trie;
import com.gleenn.regex_compressor.SimpleTrie;

import static com.gleenn.regex_compressor.SimpleTrie.*;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import org.testng.annotations.Test;

import java.util.Collections;

public class SimpleTrieTest {
    @Test
    public void addWordTest_setsTerminalPropertyCorrectly() {
        Trie root = new SimpleTrie();
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
        Trie root = new SimpleTrie();
        root.addWord("abcd");
        root.addWord("a");
        assertThat(root.get('a').isTerminal(), is(true));

        SimpleTrie rootBackwards = new SimpleTrie();
        rootBackwards.addWord("a");
        rootBackwards.addWord("abcd");
        assertThat(rootBackwards.get('a').isTerminal(), is(true));
    }

    @Test
    public void addReverseWord_reversesStringBeforeAddingToTrie() {
        Trie root = new SimpleTrie();
        root.addReverseWord("cat");
        assertThat(root.contains("tac"), is(true));
    }

    @Test
    public void contains_returnsTrueWhenTrieContainsWord() {
        Trie root = new SimpleTrie();
        assertThat(root.contains("dog"), is(false));
        root.addWord("dog");
        assertThat(root.contains("dog"), is(true));
        assertThat(root.contains("cat"), is(false));
    }

    @Test
    public void constructorTest_withList() {
        Trie trie = new SimpleTrie('a', false, asList('b'));
        assertThat(trie.isTerminal(), is(false));
        assertThat(trie.get('b').isTerminal(), is(true));
    }

    @Test
    public void get_returnsSubTrieMatchingCharacter() {
        Trie trie = new SimpleTrie();
        assertThat(trie.addWord("a").isTerminal(), is(true));
        trie.addWord("bc");
        assertThat(trie.get('a').isTerminal(), is(true));
        assertThat(trie.get('b').get('c').isTerminal(), is(true));
    }

    @Test
    public void equalsTest() {
        assertThat(new SimpleTrie().equals(new SimpleTrie()), is(true));
        assertThat(new SimpleTrie().addWord("a").equals(new SimpleTrie().addWord("a")), is(true));

        SimpleTrie aFirst = new SimpleTrie();
        aFirst.addWord("a").addWord("b");
        SimpleTrie bFirst = new SimpleTrie();
        bFirst.addWord("b").addWord("a");
        assertThat(aFirst.equals(bFirst), is(false));

        SimpleTrie root1 = new SimpleTrie();
        root1.addWord("a").addWord("b").addWord("c");
        Trie root2 = new SimpleTrie();
        root2.addWord("a").addWord("b").addWord("c");
        assertThat(root1, equalTo(root2));
        assertThat(root2, equalTo(root1));

        root1.get('a').get('b').addWord("d");
        assertThat(root1, not(equalTo(root2)));
        assertThat(root2, not(equalTo(root1)));
    }

    @Test
    public void hasOnlyChildTest() {
        Trie trie = new SimpleTrie();
        assertThat(hasOnlyChild(trie), is(false));
        trie.addWord("a");
        assertThat(hasOnlyChild(trie), is(true));
        trie.addWord("b");
        assertThat(hasOnlyChild(trie), is(false));
    }

    @Test
    public void hasNoChildrenTest() {
        Trie trie = new SimpleTrie();
        assertThat(hasNoChildren(trie), is(true));
        trie.addWord("a");
        assertThat(hasNoChildren(trie), is(false));
    }

    @Test
    public void matchingPrefixesTest_withoutEnforceWordBoundary() {
        Trie trie = new SimpleTrie();

        assertThat(matchingPrefixes(trie, ""), is(emptyList()));
        assertThat(matchingPrefixes(trie, "asdf"), is(emptyList()));

        trie.addWord("a");
        assertThat(matchingPrefixes(trie, "a"), is(singletonList("a")));
        assertThat(matchingPrefixes(trie, "b"), is(emptyList()));

        trie.addWord("ab");
        assertThat(matchingPrefixes(trie, "ab"), is(asList("a", "ab")));
        assertThat(matchingPrefixes(trie, " ab"), is(emptyList()));

        trie.addWord("c");
        assertThat(matchingPrefixes(trie, "cab"), is(singletonList("c")));
    }

    @Test
    public void matchingPrefixesTest_withEnforceWordBoundary() {
        assertThat(SimpleTrie.wordBoundary.matcher(" ").find(), is(true));

        Trie trie = new SimpleTrie();

        assertThat(matchingPrefixes(trie, "", true), is(emptyList()));
        assertThat(matchingPrefixes(trie, "asdf", true), is(emptyList()));

        trie.addWord("a");
        assertThat(matchingPrefixes(trie, "a", true), is(singletonList("a")));
        assertThat(matchingPrefixes(trie, "b", true), is(emptyList()));

        trie.addWord("ab");
        assertThat(matchingPrefixes(trie, "ab", true), is(singletonList("ab")));
        assertThat(matchingPrefixes(trie, " ab", true), is(emptyList()));

        trie.addWord("c");
        assertThat(matchingPrefixes(trie, "c ab", true), is(singletonList("c")));
        assertThat(matchingPrefixes(trie, "c ", true), is(singletonList("c")));

        assertThat(matchingPrefixes(trie, "ca b", true), is(emptyList()));

        assertThat(matchingPrefixes(trie, "ab\n", true), is(singletonList("ab")));

        trie = new SimpleTrie();
        trie.addWord("a");
        trie.addWord("a\nb");
        trie.addWord("c");
        assertThat(matchingPrefixes(trie, "a", true), is(singletonList("a")));
        assertThat(matchingPrefixes(trie, "a\n", true), is(singletonList("a")));
        assertThat(matchingPrefixes(trie, "\n", true), is(emptyList()));
    }
}
