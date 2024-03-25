package org.example.ch01.item05.codes;

import java.util.List;
import java.util.Objects;

public class SpellCheckerInjection {
    private static final Lexicon dictionary;

    public SpellCheckerInjection(Lexicon disctionary) {
        this.dictionary = Objects.requireNonNull(disctionary);
    }
    public static boolean isValid(String word) {
        return dictionary.isValid(word);
    }
    public static List<String> suggestions(String typo) {
        return dictionary.suggestions(typo);
    }
}