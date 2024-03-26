package org.example.ch01.item06.codes;

import org.example.ch01.item03.codes.ElvisField;

import java.lang.reflect.Constructor;

public class Main {

    public static void main(String[] args) {
        Sample sample = new Sample();
        RomanNumerals romanNumerals = new RomanNumerals();
        long beforeTime = System.currentTimeMillis();

        for (int i = 0; i < 100000; i++) {
            sample.isRomanNumeral("MCMLXXVI");
        }

        long afterTime = System.currentTimeMillis();
        long secDiffTime = (afterTime - beforeTime);

        System.out.println("시간차이(ms) : "+secDiffTime);

        long beforeTime2 = System.currentTimeMillis();

        for (int i = 0; i < 100000; i++) {
            romanNumerals.isRomanNumeral("MCMLXXVI");
        }

        long afterTime2 = System.currentTimeMillis();
        long secDiffTime2 = (afterTime2 - beforeTime2);

        System.out.println("시간차이(ms) : "+secDiffTime2);


        long beforeTime3 = System.currentTimeMillis();
        sample.sumLong();
        long afterTime3 = System.currentTimeMillis();
        long secDiffTime3 = (afterTime3 - beforeTime3);

        System.out.println("시간차이(ms) - Long : "+secDiffTime3);

        long beforeTime4 = System.currentTimeMillis();
        sample.sumlong();
        long afterTime4 = System.currentTimeMillis();
        long secDiffTime4 = (afterTime4 - beforeTime4);

        System.out.println("시간차이(ms) - long : "+secDiffTime4);
    }



}