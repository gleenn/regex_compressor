package com.gleenn;

import com.gleenn.regex_compressor.Options;
import static com.gleenn.regex_compressor.RegexCompressor.*;
import com.gleenn.regex_compressor.TrieImpl;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class RegexCompressorTest {
    private final static List<String> PIRATE_WORDS = Arrays.asList("abandon", "adventure", "ahoy", "anchor", "armada",
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

    @Test
    public void pattern_basic() {
        assertThat(pattern(asList("a")).toString(), is("a"));
        assertThat(pattern(asList("a", "b")).toString(), is("[ab]"));
        assertThat(pattern(asList("a", "b", "ac")).toString(), is("ac?|b"));
        assertThat(pattern(asList("a", "b", "c")).toString(), is("[abc]"));
        assertThat(pattern(asList("a", "bc")).toString(), is("a|bc"));
        assertThat(pattern(asList("abcd", "a")).toString(), is("a(?:bcd)?"));
        assertThat(pattern(asList("a", "b", "ab")).toString(), is("ab?|b"));
        assertThat(pattern(asList("a", "b", "ab", "abc")).toString(), is("a(?:bc?)?|b"));
        assertThat(pattern(asList("foo", "bar", "baz")).toString(), is("foo|ba[rz]"));
        assertThat(pattern(asList("foo", "bar", "baz", "quux")).toString(), is("foo|ba[rz]|quux"));

        assertThat(pattern(asList("aaaab", "aaaac", "aaaad", "aaaae")).toString(), is("aaaa[bcde]"));

    }

    @Test
    public void pattern_matchesStringsCorrectly() {
        assertThat(pattern(asList("a")).matcher("a").find(), is(true));

        assertThat(pattern(asList("*Opens and")).matcher("*Opens and").find(), is(true));
        assertThat(pattern(asList("\\*Opens and")).matcher("\\*Opens and").find(), is(true));
    }

    @Test
    public void pattern_whenGivenEmptyList_returnsRegexThatMatchesNothing() {
        Pattern pattern = pattern(asList(), Options.defaultOptions().withWordBoundaries());
        assertThat(pattern.toString(), is("(?!.)"));

        assertThat(pattern.matcher("").matches(), is(true));
        assertThat(pattern.matcher("a").matches(), is(false));
        assertThat(pattern.matcher("aa").matches(), is(false));
        assertThat(pattern.matcher("aaa").matches(), is(false));

        assertThat(pattern(asList(), Options.defaultOptions().withWordBoundaries()).toString(), is("(?!.)"));
    }

    @Test
    public void pattern_withUnicodeChars() {
        assertThat(pattern(asList("☺")).matcher("☺").find(), is(true));
    }

    @Test
    public void patternRE2_basic() {
        assertThat(patternRE2(asList("a")).matcher("a").find(), is(true));
        assertThat(patternRE2(asList("foo", "bar", "baz", "quux")).matcher("quux").find(), is(true));

        Options options = Options.defaultOptions().withWordBoundaries();
        assertThat(pattern(asList("a"), options).matcher("ab").find(), is(false));
        assertThat(pattern(asList("a"), options).matcher("ba").find(), is(false));
        assertThat(pattern(asList("a"), options).matcher("bab").find(), is(false));

        com.google.re2j.Pattern pattern = patternRE2(PIRATE_WORDS);
        for(String word : PIRATE_WORDS) {
            assertThat("Regex should match " + word, pattern.matcher(word).find(), is(true));
        }
    }

    @Test
    public void pattern_withWordBoundariesOptions() {
        final Options options = Options.defaultOptions().withWordBoundaries();

        assertThat(pattern(asList("a"), options).matcher("a").find(), is(true));
        assertThat(pattern(asList("a"), options).matcher("ab").find(), is(false));
        assertThat(pattern(asList("a"), options).matcher("ba").find(), is(false));
        assertThat(pattern(asList("a"), options).matcher("bab").find(), is(false));
    }

    @Test
    public void pattern_withPrefixSet() {
        final Options options = Options.defaultOptions().prefix("X");
        assertThat(pattern(asList("a"), options).matcher("Xa").find(), is(true));
        assertThat(pattern(asList("a"), options).matcher("Za").find(), is(false));
    }

    @Test
    public void pattern_withSuffixSet() {
        final Options options = Options.defaultOptions().suffix("X");
        assertThat(pattern(asList("a"), options).matcher("aX").find(), is(true));
        assertThat(pattern(asList("a"), options).matcher("aZ").find(), is(false));
    }

    @Test
    public void pattern_withPrefixSet_addsParensToEnsurePrefixIsMatched() {
        final Options options = Options.defaultOptions().prefix("X");
        Pattern pattern = pattern(asList("az", "b"), options);

        assertThat(pattern.toString(), containsString("|"));
        assertThat(pattern.matcher("Xaz").find(), is(true));
        assertThat(pattern.matcher("Xb").find(), is(true));
        assertThat(pattern.matcher("az").find(), is(false));
        assertThat(pattern.matcher("b").find(), is(false));
    }

    @Test
    public void pattern_withSuffixSet_addsParensToEnsureSuffixIsMatched() {
        final Options options = Options.defaultOptions().suffix("X");
        Pattern pattern = pattern(asList("a", "zb"), options);

        assertThat(pattern.toString(), containsString("|"));
        assertThat(pattern.matcher("aX").find(), is(true));
        assertThat(pattern.matcher("zbX").find(), is(true));
        assertThat(pattern.matcher("zb").find(), is(false));
        assertThat(pattern.matcher("a").find(), is(false));
    }

    @Test
    public void pattern_withPrefixAndSuffixSet() {
        final Options options = Options.defaultOptions().suffix("X").prefix("X");
        assertThat(pattern(asList("a"), options).matcher("XaX").find(), is(true));
        assertThat(pattern(asList("a"), options).matcher("ZaX").find(), is(false));
        assertThat(pattern(asList("a"), options).matcher("XaZ").find(), is(false));
        assertThat(pattern(asList("a"), options).matcher("ZaZ").find(), is(false));
    }

    @Test
    public void pattern_withCaseInsensitive() {
        final Options options = Options.defaultOptions().caseSensitive(false);
        assertThat(pattern(asList("a"), options).matcher("a").find(), is(true));
        assertThat(pattern(asList("a"), options).matcher("A").find(), is(true));
        assertThat(pattern(asList("a", "A"), options).toString(), is("(?i)a"));
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
    public void pattern_escaping() {
        assertThat(pattern(asList("{")).toString(), is("\\{"));
        assertThat(pattern(asList(":)")).toString(), is(":\\)"));
        assertThat(pattern(asList(":)-|--<")).toString(), is(":\\)\\-\\|\\-\\-\\<"));
        assertThat(pattern(asList("¯\\_(ツ)_/¯")).toString(), is("¯\\\\_\\(ツ\\)_/¯"));
        assertThat(pattern(asList("*Opens and")).toString(), is("\\*Opens and"));
    }

    @Test
    public void pattern_withRandomStrings() {
        String regexString = pattern(PIRATE_WORDS).toString();

        assertThat(regexString, is("a(?:bandon|dventure|hoy|nchor|rm(?:ada|s)|s(?:ea|hore|sault)|ttack|ye\\-aye)|b(?:" +
                "a(?:d|nd(?:anna|it|olier)|r(?:baric|rel)|ttle)|e(?:ach|head)|o(?:atswain|s'n|unty)|r(?:awl|utal)|ucc" +
                "aneer)|c(?:a(?:nnon|p(?:size|t(?:ain|ure))|rgo|ve)|h(?:allenge|est)|o(?:ast(?:line)?|ins|mpass|n(?:f" +
                "iscate|quest|traband)|r(?:pse|sair)|urse)|r(?:ew|iminal|o(?:ok|w's nest)|uel)|u(?:rse|t(?:lass|throa" +
                "t)))|d(?:a(?:gger|nger|ring)|e(?:ad reckoning|ck(?: hands)?|sert island)|ishonest|oubloon)|e(?:arrin" +
                "g|scape|vil|xplore|ye patch)|f(?:e(?:ar(?:some)?|rocious)|i(?:ght|rst mate)|l(?:ag|eet|otsam and jet" +
                "sam)|ortune)|g(?:a(?:lleon|ngplank)|ear|ibbet|old|reed|un(?:ner|powder)?)|h(?:aul|eist|i(?:gh seas|j" +
                "ack)|o(?:ok|ld|rizon|stile)|u(?:ll|rricane))|i(?:ll(?:egal|\\-gotten)|nfamous|sland)|je(?:tsam|wels)" +
                "|Jolly Roger|k(?:eel(?:haul)?|i(?:dnap|ll)|nife)|l(?:a(?:nd(?:\\-ho|lubber)?|sh|wless)|egend|imey|o(" +
                "?:o(?:kout|t)|re)|ucre)|Long John Silver|m(?:a(?:ggot|laria|p|r(?:auder|oon)|t(?:iner|es)|st|yhem)|e" +
                "(?:nace|rchant)|u(?:sket|tiny))|n(?:a(?:utical|vigate)|otorious)|New World|o(?:cean|ld salt|utcasts|" +
                "verboard)|Old World|p(?:ar(?:ley|rot)|egleg|i(?:eces of eight|llage|rate|stol)|l(?:ank|under)|r(?:ed" +
                "atory|ivateer|owl))|qu(?:arter(?:master|s)|est)|r(?:a(?:id|nsack|t(?:ions)?)|e(?:alm|ckoning|v(?:eng" +
                "e|olt))|i(?:ches|gging)|o(?:am|b(?:ber)?|pe)|u(?:dder|ffian|m|thless))|s(?:a(?:botage|il(?:ing|or)?)" +
                "|c(?:a(?:lawag|r)|urvy)|e(?:a(?:s|weed)|xtant)|h(?:i(?:p(?:mate)?|ver\\-me\\-timbers)|ore)|ilver|k(?" +
                ":iff|ull and bones)|poils|teal|w(?:a(?:b the deck|gger|shbuckling)|ord))|t(?:h(?:ie(?:f|very)|ug)|id" +
                "es|orture|r(?:ade|ea(?:chery|sure(?: island)?)|uce))|un(?:lawful|scrupulous)|v(?:an(?:dalize|quish)|" +
                "essel|i(?:cious|l(?:e|lain)|olen(?:ce|t)))|w(?:alk the plank|eapons)|X marks the spot|y(?:ellow feve" +
                "r|o\\-ho\\-ho)"));

        Pattern pattern = Pattern.compile(regexString);
        for(String word : PIRATE_WORDS) {
            assertThat("Regex should match " + word, pattern.matcher(word).find(), is(true));
        }
    }

    @Test
    void buildRegexTest() {
        StringBuilder result;

        result = new StringBuilder();
        buildRegex(new TrieImpl(null, true), result);
        assertThat(result.toString(), is(""));

        result = new StringBuilder();
        buildRegex(new TrieImpl('a', true), result);
        assertThat(result.toString(), is("a"));

        result = new StringBuilder();
        buildRegex(new TrieImpl('a', false, singletonList('b')), result);
        assertThat(result.toString(), is("ab"));

        result = new StringBuilder();
        buildRegex(new TrieImpl('a', true, singletonList('b')), result);
        assertThat(result.toString(), is("ab?"));

        result = new StringBuilder();
        buildRegex(new TrieImpl('a', false, asList('b', 'c')), result);
        assertThat(result.toString(), is("a[bc]"));

        result = new StringBuilder();
        buildRegex(new TrieImpl('a', true, asList('b', 'c')), result);
        assertThat(result.toString(), is("a[bc]?"));
    }
}
