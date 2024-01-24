package org.example.ch01.item03.codes;

public class EvlisStaticFactory {
    private static final EvlisStaticFactory INSTANCE = new EvlisStaticFactory();
    private EvlisStaticFactory() {}
    public static EvlisStaticFactory getInstance() {
        return INSTANCE;
    }
    public void leaveTheBuilding() {
        System.out.println("Whoa baby, I'm outta here!");
    }
}
