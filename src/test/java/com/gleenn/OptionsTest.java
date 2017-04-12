package com.gleenn;

import com.gleenn.regex_compressor.Options;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.nullValue;
import org.testng.annotations.Test;

public class OptionsTest {
    @Test
    public void caseSensitive() {
        assertThat(Options.defaultOptions().isCaseSensitive(), is(true));

        assertThat(Options.defaultOptions().caseSensitive(false).isCaseSensitive(), is(false));
        assertThat(Options.defaultOptions().caseSensitive(true).isCaseSensitive(), is(true));
    }

    @Test
    public void prefix() {
        assertThat(Options.defaultOptions().getPrefix(), nullValue());
        assertThat(Options.defaultOptions().prefix("foobar").getPrefix(), is("foobar"));
    }

    @Test
    public void suffix() {
        assertThat(Options.defaultOptions().getSuffix(), nullValue());
        assertThat(Options.defaultOptions().suffix("foobar").getSuffix(), is("foobar"));
    }

    @Test
    public void withWordBoundaries() {
        assertThat(Options.defaultOptions().withWordBoundaries().getPrefix(), is("\\b"));
        assertThat(Options.defaultOptions().withWordBoundaries().getSuffix(), is("\\b"));

        assertThat(Options.defaultOptions().withWordBoundaries("foobar").getPrefix(), is("foobar"));
        assertThat(Options.defaultOptions().withWordBoundaries("foobar").getSuffix(), is("foobar"));
    }
}
