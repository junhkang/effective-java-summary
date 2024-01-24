package org.example;

import java.math.BigInteger;
import java.util.Collections;
import java.util.EnumSet;
import java.lang.Boolean;

public class Sample {
    public Sample(boolean b)   {
        //b ? Boolean.TRUE : Boolean.FALSE;
        //BigInteger.probablePrime()
        //Collections

    }
    public static Boolean valueOf(boolean b)    {
        return b ? Boolean.TRUE : Boolean.FALSE;
    }
    public static boolean isOdd(int i)    {
        return i%2 == 0 ? Boolean.TRUE : Boolean.FALSE;
    }
    public static boolean isEven(int i)    {
        return i%2 == 1 ? Boolean.TRUE : Boolean.FALSE;
    }
}
