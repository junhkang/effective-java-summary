package org.example.ch01.item02.codes;
/*
    2-2 자바빈즈 패턴 - 일관성이 깨지고 불변으로 만들수 없음
 */
public class NutritionFactsJb {
    private int servingSize = -1;
    private int servings = -1;
    private int calories = 0;
    private int fat = 0;
    private int sodium = 0;
    private int carbohydrate = 0;
    public NutritionFactsJb() { }
    // Setters
    public void setServingSize(int val) { servingSize = val; }
    public void setServings(int val) { servings = val; }
    public void setCalories(int val) { calories = val; }
    public void setFat(int val) { fat = val; }
    public void setSodium(int val) {sodium = val;}
    public void setCarbohydrate(int val)    {sodium = val;}
}
