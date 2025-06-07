# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Regex Compressor is a Java library that generates compact, performance-optimized regular expressions from lists of strings. The library uses a trie data structure to identify common prefixes and generate character classes where appropriate.

**Main API entry point**: `com.gleenn.regex_compressor.RegexCompressor` with static methods:
- `compress(List<String>)` → compressed regex string
- `pattern(List<String>)` → Java Pattern object  
- `patternRE2(List<String>)` → RE2J Pattern object

## Build and Test Commands

- **Build**: `mvn compile`
- **Run tests**: `mvn test` or `make test`
- **Deploy**: `make deploy` (requires version bump)

## Architecture

The core compression algorithm works in two phases:

1. **Trie Construction** (`SimpleTrie.buildPrefixTrie()`): Build a trie from input strings to identify common prefixes
2. **Regex Generation** (`RegexCompressor.buildRegex()`): Traverse the trie to generate optimized regex patterns using character classes `[abc]` and alternation `a|b|c`

**Key Components**:
- `Trie` interface: Core data structure for string storage and traversal
- `SimpleTrie`: Primary implementation using LinkedHashMap for children, with radix tree infrastructure
- `Options`: Configuration for case sensitivity, word boundaries, prefix/suffix
- `TrieSimplifier`: Experimental suffix compression (incomplete)

**Radix Tree Implementation**:
- Each `SimpleTrie` node can store a `path` (string) instead of just a single character
- Infrastructure includes `optimizeRadixPaths()` for compressing linear chains
- `getPath()` method returns the compressed string path for a node
- Regex generation handles both single characters and string paths via `buildRegex()`
- Currently disabled to maintain backward compatibility, but ready for selective use

The regex generation algorithm optimizes by:
- Using character classes `[abc]` when all children are terminal nodes
- Using alternation `(a|b|c)` for complex branching
- Adding `?` quantifier for optional terminal nodes
- Supporting radix tree paths for potential memory/traversal optimizations

## Development Notes

- Uses Java 8 syntax and APIs
- Supports both standard Java regex and Google RE2J patterns
- Testing uses JUnit and TestNG frameworks
- Character escaping handled in `RegexCompressor.escape()`
- Insertion order in input list affects regex match precedence
- Radix tree optimization available but disabled by default to preserve test compatibility