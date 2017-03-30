[ ![Download](https://circleci.com/gh/gleenn/regex_compressor.svg?&style=shield&circle-token=40470b2c2097f41db4296428a85971029b275f93) ](https://circleci.com/gh/gleenn/regex_compressor.svg?&style=shield&circle-token=40470b2c2097f41db4296428a85971029b275f93)

# Regex Compressor

Regex Compressor is a Java library intended to take a list of strings and generate a compact, Java-compatible regex that matches those exact strings. For instance

`compress(asList("foo", "bar", "baz"))` => `"foo|ba(?:r|z)"`

This should have improved regex performance on large numbers of strings.

This project is based on a Clojure implementation called [Frak](https://github.com/noprompt/frak).
