package com.gleenn;

import static com.gleenn.regex_compressor.RegexCompressor.buildRegex;
import static com.gleenn.regex_compressor.RegexCompressor.compress;
import com.gleenn.regex_compressor.Trie;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.Test;

public class RegexCompressorTest {
    @Test
    public void compressTest() {
        assertThat(compress(asList("a")), is("a"));
        assertThat(compress(asList("a", "b")), is("a|b"));
        assertThat(compress(asList("a", "b", "c")), is("a|b|c"));
        assertThat(compress(asList("a", "bc")), is("a|bc"));
        assertThat(compress(asList("abcd", "a")), is("a(?:bcd)?"));
        assertThat(compress(asList("a", "b", "ab")), is("ab?|b"));
        assertThat(compress(asList("a", "b", "ab", "abc")), is("a(?:bc?)?|b"));
    }

    @Test
    void buildRegexTest() {
        StringBuilder result;

        result = new StringBuilder();
        buildRegex(new Trie(null, true), result);
        assertThat(result.toString(), is(""));

        result = new StringBuilder();
        buildRegex(new Trie('a', true), result);
        assertThat(result.toString(), is("a"));

        result = new StringBuilder();
        buildRegex(new Trie('a', false, asList('b')), result);
        assertThat(result.toString(), is("ab"));

        result = new StringBuilder();
        buildRegex(new Trie('a', true, asList('b')), result);
        assertThat(result.toString(), is("ab?"));

        result = new StringBuilder();
        buildRegex(new Trie('a', false, asList('b', 'c')), result);
        assertThat(result.toString(), is("a(?:b|c)"));

        result = new StringBuilder();
        buildRegex(new Trie('a', true, asList('b', 'c')), result);
        assertThat(result.toString(), is("a(?:b|c)?"));
    }
}
