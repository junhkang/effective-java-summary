package org.example.ch01.item03.codes;

public class ElvisField {
    public static final ElvisField INSTANCE = new ElvisField();
    private ElvisField() {}
    public void leaveTheBuilding() {
        System.out.println("Whoa baby, I'm outta here!");
    }
    private Object readResolve() {
        return INSTANCE;
    }
}
