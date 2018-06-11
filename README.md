# Regex Compressor

[![Download](https://circleci.com/gh/gleenn/regex_compressor.svg?&style=shield&circle-token=40470b2c2097f41db4296428a85971029b275f93)](https://circleci.com/gh/gleenn/regex_compressor)

Regex Compressor is a Java library intended to take a list of strings and generate a compact, Java-compatible regex that matches those exact strings.


## Usage

`import static com.gleenn.regex_compressor.RegexCompressor.*;`

This example shortens the regex by two characters, and also improves the regex performance because it won't have to check the "aaaa" prefix 4 times when matching "aaaad", only once. This can have a large impact on performance.
`compress(asList("aaaab", "aaaac", "aaaad", "aaaae"))` => `"aaaa[bcde]"`

`compress(asList("foo", "bar", "baz"))` => `"foo|ba[rz]"`

Use the `pattern` method instead of `compress` to generate a Java Pattern directly.

## Notes

This should have improved regex performance on large numbers of strings. It uses a trie data structure to compress common prefixes.

Works with Unicode!

Insertion order matters, it can affect the result of matches if you care about more than just does the string match true/false.

## Future work

- Use radix trees to improve performance when compressing long prefixes
- Compress suffixes (although this is pretty non-trivial)

## Related work

This project is based on a Clojure implementation called [Frak](https://github.com/noprompt/frak).
