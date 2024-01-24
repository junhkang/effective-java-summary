package org.example;

import java.util.Deque;
import java.util.Queue;

import static org.example.NyPizza.Size.SMALL;
import static org.example.Pizza.Topping.ONION;
import static org.example.Pizza.Topping.SAUSAGE;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello world!");
        NutritionFactsTcp cocaCola = new NutritionFactsTcp(240, 8, 100, 0, 35, 27);

        NutritionFactsJb pepsiCola = new NutritionFactsJb();
        pepsiCola.setServingSize(240);
        pepsiCola.setServings(8);
        pepsiCola.setCalories(100);
        pepsiCola.setSodium(35);
        pepsiCola.setCarbohydrate(27);

        NutritionFactsBp dietCola = new NutritionFactsBp.Builder(240, 8).calories(100).sodium(35).carbohydrate(27).build();

        NyPizza nyPizza = new NyPizza.Builder(SMALL).addTopping(SAUSAGE).addTopping(ONION).build();
        Calzone calzone = new Calzone.Builder().addTopping(ONION).sauceInside().build();

    }

    public static boolean valueOf(boolean b)    {
        return b ? Boolean.TRUE : Boolean.FALSE;
    }
}