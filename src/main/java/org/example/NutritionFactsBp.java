package org.example;
/*
    2-3 빌더패턴 - 점층적 생성자 패턴과 자바빈즈 패턴의 장점만 취함
 */
public class NutritionFactsBp {
    private final int servingSize ;
    private final int servings ;
    private final int calories;
    private final int fat;
    private final int sodium;
    private final int carbohydrate;
    public static class Builder {
        private final int servingSize ;
        private final int servings ;
        private int calories = 0;
        private int fat = 0;
        private int sodium = 0;
        private int carbohydrate = 0;
        public Builder(int servingSize, int servings)   {
            this.servingSize = servingSize;
            this.servings = servings;
        }
        public Builder calories(int val)    {
            calories = val;
            return this;
        }
        public Builder fat(int val) {
            fat = val;
            return this;
        }
        public Builder sodium(int val) {
            sodium = val;
            return this;
        }
        public Builder carbohydrate(int val) {
            carbohydrate = val;
            return this;
        }
        public NutritionFactsBp build()   {
            return new NutritionFactsBp(this);
        }
    }
    private NutritionFactsBp(Builder builder) {
        servingSize = builder.servingSize;
        servings = builder.servings;
        calories = builder.calories;
        fat = builder.fat;
        sodium = builder.sodium;
        carbohydrate = builder.carbohydrate;
    }
}
