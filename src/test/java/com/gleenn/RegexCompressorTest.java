package com.gleenn;

import static com.gleenn.regex_compressor.RegexCompressor.buildRegex;
import static com.gleenn.regex_compressor.RegexCompressor.compress;
import static com.gleenn.regex_compressor.RegexCompressor.escape;
import com.gleenn.regex_compressor.Trie;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class RegexCompressorTest {
    @Test
    public void compressTest() {
        assertThat(compress(asList("a")), is("a"));
        assertThat(compress(asList("a", "b")), is("[ab]"));
        assertThat(compress(asList("a", "b", "c")), is("[abc]"));
        assertThat(compress(asList("a", "bc")), is("a|bc"));
        assertThat(compress(asList("abcd", "a")), is("a(?:bcd)?"));
        assertThat(compress(asList("a", "b", "ab")), is("ab?|b"));
        assertThat(compress(asList("a", "b", "ab", "abc")), is("a(?:bc?)?|b"));
        assertThat(compress(asList("foo", "bar", "baz")), is("foo|ba[rz]"));
        assertThat(compress(asList("foo", "bar","baz","quux")), is("foo|ba[rz]|quux"));
    }

    @Test
    public void escapeTest() {
        assertThat(escape('('), is("\\("));
        assertThat(escape(')'), is("\\)"));
        assertThat(escape('{'), is("\\{"));
        assertThat(escape('}'), is("\\}"));
        assertThat(escape('['), is("\\["));
        assertThat(escape(']'), is("\\]"));
        assertThat(escape('.'), is("\\."));
        assertThat(escape('+'), is("\\+"));
        assertThat(escape('*'), is("\\*"));
        assertThat(escape('?'), is("\\?"));
        assertThat(escape('^'), is("\\^"));
        assertThat(escape('$'), is("\\$"));
        assertThat(escape('|'), is("\\|"));
        assertThat(escape('\\'), is("\\\\"));
        assertThat(escape('<'), is("\\<"));
        assertThat(escape('>'), is("\\>"));
        assertThat(escape('-'), is("\\-"));
        assertThat(escape('='), is("\\="));
        assertThat(escape('!'), is("\\!"));

        assertThat(escape('a'), is("a"));
        assertThat(escape('A'), is("A"));
        assertThat(escape('√'), is("√"));
    }
    @Test
    public void compressTest_escaping() {
        assertThat(compress(asList("{")), is("\\{"));
        assertThat(compress(asList(":)")), is(":\\)"));
        assertThat(compress(asList(":)-|--<")), is(":\\)\\-\\|\\-\\-\\<"));
        assertThat(compress(asList("¯\\_(ツ)_/¯")), is("¯\\\\_\\(ツ\\)_/¯"));
    }

    @Test
    public void compressTest_withRandomStrings() {
        List<String> words = Arrays.asList("abandon", "adventure", "ahoy", "anchor", "armada",
                "arms", "asea", "ashore", "assault", "attack", "aye-aye", "bad", "bandanna", "bandit",
                "bandolier", "barbaric", "barrel", "battle", "beach", "behead", "boatswain", "bos'n",
                "bounty", "brawl", "brutal", "buccaneer", "cannon", "capsize", "captain", "capture",
                "cargo", "cave", "challenge", "chest", "coast", "coastline", "coins", "compass",
                "confiscate", "conquest", "contraband", "corpse", "corsair", "course", "crew", "criminal",
                "crook", "crow's nest", "cruel", "curse", "cutlass", "cutthroat", "dagger", "danger",
                "daring", "dead reckoning", "deck", "deck hands", "desert island", "dishonest",
                "doubloon", "earring", "escape", "evil", "explore", "eye patch", "fear", "fearsome",
                "ferocious", "fight", "first mate", "flag", "fleet", "flotsam and jetsam", "fortune",
                "galleon", "gangplank", "gear", "gibbet", "gold", "greed", "gun", "gunner", "gunpowder",
                "haul", "heist", "high seas", "hijack", "hook", "hold", "horizon", "hostile", "hull",
                "hurricane", "illegal", "ill-gotten", "infamous", "island", "jetsam", "jewels",
                "Jolly Roger", "keel", "keelhaul", "kidnap", "kill", "knife", "land", "land-ho",
                "landlubber", "lash", "lawless", "legend", "limey", "Long John Silver", "lookout",
                "loot", "lore", "lucre", "maggot", "malaria", "map", "marauder", "matiner", "maroon",
                "mast", "mates", "mayhem", "menace", "merchant", "musket", "mutiny", "nautical",
                "navigate", "New World", "notorious", "ocean", "old salt", "Old World", "outcasts",
                "overboard", "parley", "parrot", "pegleg", "pieces of eight", "pillage", "pirate",
                "pistol", "plank", "plunder", "predatory", "privateer", "prowl", "quartermaster",
                "quarters", "quest", "raid", "ransack", "rat", "rations", "realm", "reckoning",
                "revenge", "revolt", "riches", "rigging", "roam", "rob", "robber", "rope", "rudder",
                "ruffian", "rum", "ruthless", "sabotage", "sail", "sailing", "sailor", "scalawag",
                "scar", "scurvy", "seas", "seaweed", "sextant", "ship", "shipmate", "shiver-me-timbers",
                "shore", "silver", "skiff", "skull and bones", "spoils", "steal", "swab the deck",
                "swagger", "swashbuckling", "sword", "thief", "thievery", "thug", "tides", "torture",
                "trade", "treachery", "treasure", "treasure island", "truce", "unlawful", "unscrupulous",
                "vandalize", "vanquish", "vessel", "vicious", "vile", "villain", "violence", "violent",
                "walk the plank", "weapons", "X marks the spot", "yellow fever", "yo-ho-ho");
        String regexString = compress(words);
        assertThat(regexString, is("a(?:bandon|dventure|hoy|nchor|rm(?:ada|s)|s(?:ea|hore|sault)|ttack|ye\\-aye)|b(?:a(?:d|nd(?:anna|it|olier)|r(?:baric|rel)|ttle)|e(?:ach|head)|o(?:atswain|s'n|unty)|r(?:awl|utal)|uccaneer)|c(?:a(?:nnon|p(?:size|t(?:ain|ure))|rgo|ve)|h(?:allenge|est)|o(?:ast(?:line)?|ins|mpass|n(?:fiscate|quest|traband)|r(?:pse|sair)|urse)|r(?:ew|iminal|o(?:ok|w's nest)|uel)|u(?:rse|t(?:lass|throat)))|d(?:a(?:gger|nger|ring)|e(?:ad reckoning|ck(?: hands)?|sert island)|ishonest|oubloon)|e(?:arring|scape|vil|xplore|ye patch)|f(?:e(?:ar(?:some)?|rocious)|i(?:ght|rst mate)|l(?:ag|eet|otsam and jetsam)|ortune)|g(?:a(?:lleon|ngplank)|ear|ibbet|old|reed|un(?:ner|powder)?)|h(?:aul|eist|i(?:gh seas|jack)|o(?:ok|ld|rizon|stile)|u(?:ll|rricane))|i(?:ll(?:egal|\\-gotten)|nfamous|sland)|je(?:tsam|wels)|Jolly Roger|k(?:eel(?:haul)?|i(?:dnap|ll)|nife)|l(?:a(?:nd(?:\\-ho|lubber)?|sh|wless)|egend|imey|o(?:o(?:kout|t)|re)|ucre)|Long John Silver|m(?:a(?:ggot|laria|p|r(?:auder|oon)|t(?:iner|es)|st|yhem)|e(?:nace|rchant)|u(?:sket|tiny))|n(?:a(?:utical|vigate)|otorious)|New World|o(?:cean|ld salt|utcasts|verboard)|Old World|p(?:ar(?:ley|rot)|egleg|i(?:eces of eight|llage|rate|stol)|l(?:ank|under)|r(?:edatory|ivateer|owl))|qu(?:arter(?:master|s)|est)|r(?:a(?:id|nsack|t(?:ions)?)|e(?:alm|ckoning|v(?:enge|olt))|i(?:ches|gging)|o(?:am|b(?:ber)?|pe)|u(?:dder|ffian|m|thless))|s(?:a(?:botage|il(?:ing|or)?)|c(?:a(?:lawag|r)|urvy)|e(?:a(?:s|weed)|xtant)|h(?:i(?:p(?:mate)?|ver\\-me\\-timbers)|ore)|ilver|k(?:iff|ull and bones)|poils|teal|w(?:a(?:b the deck|gger|shbuckling)|ord))|t(?:h(?:ie(?:f|very)|ug)|ides|orture|r(?:ade|ea(?:chery|sure(?: island)?)|uce))|un(?:lawful|scrupulous)|v(?:an(?:dalize|quish)|essel|i(?:cious|l(?:e|lain)|olen(?:ce|t)))|w(?:alk the plank|eapons)|X marks the spot|y(?:ellow fever|o\\-ho\\-ho)"));

        Pattern pattern = Pattern.compile(regexString);
        for(String word : words) {
            assertThat("Regex should match " + word, pattern.matcher(word).find(), is(true));
        }
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
        buildRegex(new Trie('a', false, singletonList('b')), result);
        assertThat(result.toString(), is("ab"));

        result = new StringBuilder();
        buildRegex(new Trie('a', true, singletonList('b')), result);
        assertThat(result.toString(), is("ab?"));

        result = new StringBuilder();
        buildRegex(new Trie('a', false, asList('b', 'c')), result);
        assertThat(result.toString(), is("a[bc]"));

        result = new StringBuilder();
        buildRegex(new Trie('a', true, asList('b', 'c')), result);
        assertThat(result.toString(), is("a[bc]?"));
    }
}
