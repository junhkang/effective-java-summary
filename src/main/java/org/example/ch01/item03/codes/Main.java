package org.example.ch01.item03.codes;

import org.example.ch01.item02.codes.*;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.example.ch01.item02.codes.NyPizza.Size.SMALL;
import static org.example.ch01.item02.codes.Pizza.Topping.ONION;
import static org.example.ch01.item02.codes.Pizza.Topping.SAUSAGE;

public class Main {

    public static void main(String[] args) {
        ElvisField ElvisField1 = ElvisField.INSTANCE;
        ElvisField ElvisField2 = ElvisField.INSTANCE;
        System.out.println(ElvisField1.hashCode());
        System.out.println(ElvisField2.hashCode());
        try {
            Constructor<ElvisField> constructor = ElvisField.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            ElvisField ElvisField3 = constructor.newInstance();
            System.out.println(ElvisField3.hashCode());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }



}