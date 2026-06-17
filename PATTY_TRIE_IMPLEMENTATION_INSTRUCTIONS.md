This project uses a trie data structure to compress lists of strings into a compact regular expression. It uses a naive trie implementation which stores every c
haracter as a node. I want to leverage patty tries though, so that long consectutive nodes all with single children are stored as a string instead. Run tests as
necessary and update them to correctly implement a pattty trie
