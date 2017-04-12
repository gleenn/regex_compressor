package com.gleenn.regex_compressor;

public final class Options {
    private boolean caseSensitive = true;
    private String prefix = null;
    private String suffix = null;

    public Options() { }

    private Options(Options options) {
        caseSensitive = options.caseSensitive;
        prefix = options.prefix;
        suffix = options.suffix;
    }

    public static Options defaultOptions() {
        return new Options();
    }

    public Options caseSensitive(final boolean caseSensitive) {
        Options options = new Options(this);
        options.caseSensitive = caseSensitive;
        return options;
    }

    public Options withWordBoundaries() {
        Options options = new Options(this);
        options.prefix = options.suffix = "\\b";
        return options;
    }

    public Options withWordBoundaries(final String ix) {
        Options options = new Options(this);
        options.prefix = options.suffix = ix;
        return options;
    }

    public Options prefix(final String prefix) {
        Options options = new Options(this);
        options.prefix = prefix;
        return options;
    }

    public Options suffix(final String suffix) {
        Options options = new Options(this);
        options.suffix = suffix;
        return options;
    }

    public boolean isCaseSensitive() {
        return caseSensitive;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }
}
