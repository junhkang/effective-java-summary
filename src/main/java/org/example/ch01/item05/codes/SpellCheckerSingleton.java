package org.example.ch01.item05.codes;

import java.util.List;

public class SpellCheckerSingleton {
    private static final Lexicon dictionary = new Lexicon();

    private SpellCheckerSingleton() {
    }
    public static SpellCheckerSingleton INSTANCE = new SpellCheckerSingleton();
    public static boolean isValid(String word) {
        return dictionary.isValid(word);
    }

    public static List<String> suggestions(String typo) {
        return dictionary.suggestions(typo);
    }
}