package com.gleenn;

import static com.gleenn.regex_compressor.RegexCompressor.buildRegex;
import com.gleenn.regex_compressor.Trie;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class RegexCompressorTest {
    @Test
    public void compressTest() {
//        assertEquals("a(?:b)?|b", RegexCompressor.compress(Arrays.asList("a", "b", "ab")));
    }

    @Test
    void buildRegexTest() {
        StringBuilder result;

        result = new StringBuilder();
        buildRegex(null, true, result);
        assertThat(result.toString(), is(""));

        result = new StringBuilder();
        buildRegex(new Trie(null, true), false, result);
        assertThat(result.toString(), is(""));

        result = new StringBuilder();
        buildRegex(new Trie('a', true), false, result);
        assertThat(result.toString(), is("a"));

        result = new StringBuilder();
        buildRegex(new Trie('a', false, asList('b')), false, result);
        assertThat(result.toString(), is("ab"));

        result = new StringBuilder();
        buildRegex(new Trie('a', true, asList('b')), true, result);
        assertThat(result.toString(), is("ab?"));
    }
}
