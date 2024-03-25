package org.example.ch01.item05.codes;

import java.util.List;

public class SpellCheckerStatic {
    private static final Lexicon dictionary = new Lexicon();

    private SpellCheckerStatic() {
    } // 객체 생성 방지

    public static boolean isValid(String word) {
        return dictionary.isValid(word);
    }

    public static List<String> suggestions(String typo) {
        return dictionary.suggestions(typo);
    }
}