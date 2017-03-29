package com.gleenn;

import static com.gleenn.regex_compressor.RegexCompressor.buildRegex;
import com.gleenn.regex_compressor.Trie;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.Test;

public class RegexCompressorTest {
    @Test
    public void compressTest() {
//        assertEquals("a(?:b)?|b", RegexCompressor.compress(Arrays.asList("a", "b", "ab")));
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
        buildRegex(new Trie('a', true, asList('b')), result);
        assertThat(result.toString(), is("ab"));
    }
}
