정적팩토리와 생성자에는 매개변수가 많을 경우 대응이 힘들다는 동일한 제약이 있다. 특히 매개변수가 굉장히 많은데 대부분이 고정값인 경우 일반적인 방법으로는 깔끔한 대응이 힘들다. 유연한 대응을 위해 보통 점층적 생성자패턴, 자바 빈즈 패턴, 빌더 패턴을 사용하는데, 이번 챕터에서는 빌더 패턴의 장점을 강조하고 있다. 각 패턴의 장단점을 자세히 살펴보고 빌더 패턴의 장점을 알아보자

## 1\. 점층적 생성자 패턴

> 필수 매개변수만 받는 생성자  
> 필수 매개변수와 선택 매개변수 1개를 받는 생성자,  
> 필수 매개변수와 선택 매개변수 2개를 받는 생성자,  
> 필수 매개변수와 선택 매개변수 3개를 받는 생성자,  
> ...

형태로 매개변수를 전부 받는 생성자까지 늘려가는 방식이다. 인스턴스 생성 시에는 매개변수를 모두 포함한 생성자 중 가장 짧은 것을 골라 호출하면 된다. 점층적 생성자 패턴도 충분히 유용하게 사용이 가능하지만, 매개변수가 많아지면 클라이언트 코드를 읽거나 작성하기 힘들고, 설정하길 원하지 않는 값도 인자로 넣어줘야 하는 경우가 있다.

또한 매개변수의 수가 늘어날수록 의미 헷갈리고, 갯수, 위치를 매번 세어야 하기에 버그로 이어지기 쉽다. 인자의 순서를 바꿔도 컴파일 단계에서는 알 수 없고 런타임단계에서 문제를 일으킬 것이다. 다음은 책의 예제를 그대로 구현해 본 점층적 생성자 패턴이다. 

```
/*
    2-1 점층적 생성자 패턴 - 확장하기 어렵다.
 */
public class NutritionFactsTcp {
    private final int servingSize; // (mL) required
    private final int servings; // (per container) required
    private final int calories; // (per serving) optional
    private final int fat; // (g/serving) optional
    private final int sodium; // (mg/serving) optional
    private final int carbohydrate; // (g/serving) optional
    public NutritionFactsTcp(int servingSize, int servings) {
        this(servingSize, servings, 0);
    }
    public NutritionFactsTcp(int servingSize, int servings,int calories)  {
        this(servingSize, servings, calories, 0);
    }
    public NutritionFactsTcp(int servingSize, int servings,int calories, int fat)  {
        this(servingSize, servings, calories, fat, 0);
    }
    public NutritionFactsTcp(int servingSize, int servings,int calories, int fat, int sodium)  {
        this(servingSize, servings, calories, fat, sodium, 0);
    }
    public NutritionFactsTcp(int servingSize, int servings,int calories, int fat, int sodium, int carbohydrate)  {
        this.servingSize = servingSize;
        this.servings = servings;
        this.calories = calories;
        this.fat = fat;
        this.sodium = sodium;
        this.carbohydrate = carbohydrate;
    }
}
```

매개변수가 추가될 수록 유연하게 대응하는 것이 힘들고, fat / sodium 등의 매개변수 위치를 실수로 잘못 넣더라도 컴파일 단계에서 확인이 불가능하다.

## 2\. 자바빈즈 패턴

매개변수가 없는 생성자로 객체 생성 후 setter 메서드로 값을 부여하는 방식이다. 다음은 책의 예제를 그대로 구현해 본 자바빈즈 패턴이다.

```
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
```

점층적 생성자 패턴의 단점들이 보이지 않게 되었다. 코드가 길지만 인스턴스를 만들기 쉽고, 읽고 쓰기 간결한 코드가 되었다

하지만 해당 인스턴스를 호출하는 부분을 생각해 보자 

```
        NutritionFactsJb pepsiCola = new NutritionFactsJb();
        pepsiCola.setServingSize(240);
        pepsiCola.setServings(8);
        pepsiCola.setCalories(100);
        pepsiCola.setSodium(35);
        pepsiCola.setCarbohydrate(27);
```

완전한 객체 하나를 만들려면 메서드를 여러 개 호출해야 하고, 객체가 완전히 생성되기 전까지는 일관성이 무너진 상태가 된다. 점층적 생성자 패턴에서는 매개변수들이 유효한지 생성자에서만 확인하면 일관성이 유지가 되었지만, 자바빈즈 패턴에서는 불가능하다.  클래스의 속성을 변경하는 메서드를 제공하는 것이기에 불변으로 만들 수 없으며, 일관성이 깨진 객체를 만들면 버그가 생성된 시점과 실제 문제를 일으키는 코드가 물리적으로 떨어져 있을 것이라 디버깅 힘들다. 이렇게 일관성이 무너지는 문제로 자바빈즈 패턴에서는 클래스를 불변으로 만들수 없고, 스레드 안정성을 얻으려면 개발자가 추가작업이 필요하다.

생성이 끝난 객체를 수동으로 얼리고 얼리기 전에는 사용할 수 없도록 하기도 하지만, 다루기 어려워 실전에선 거의 쓰이지 않는다고 한다. 쓰더라도 객체 사용 전에 freeze 메서드를 확실히 호출했는지 컴파일러가 보증할 수 없어 런타임 오류에는 여전히 취약하다.

## 3\. 빌더패턴

클라이언트는 필요객체를 직접 만드는 대신 필수 매개변수만으로 생서자를 호출하여 빌더 객체를 얻을 수 있다. 그다음 빌더객체가 제공하는 setter메서드들로 원하는 매개변수를 설정하고, 마지막으로 매개변수가 없는 빌드 메서드를 호출하여 필요한 객체를 얻는다.

(builder는 생성할 클래스 안에 정적 멤버 클래스로 만들어두는 것이 관례이다.)

다음은 다음은 책의 예제를 그대로 구현해 본 빌더 패턴이다.

```
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
```

NutirionFactsBp 클래스는 불변이며 모든 매개변수의 기본 값을 한 곳에 모아둔다. 빌더의 세터 메서드들은 빌더 자신을 반환하기에 연쇄적으로 호출가능하다. 이런 방식을 흐르듯 연결되듯 메서드 호출이 된다는 뜻에서 플루언트 API (fluent API) 혹은 메서드 연쇄 (method chaining)이라 한다. 실제로 이 클래스를 사용하는 클라이언트 코드를 보면

```
NutritionFactsBp dietCola = new NutritionFactsBp.Builder(240, 8).calories(100).sodium(35).carbohydrate(27).build();
```

한눈에 봐도 읽기 쉽고 쓰기 쉽다.

빌더 패턴은 (파이썬과 스칼라에 있는) 명명된 선택적 매개변수(named optional parameters)를 흉내 낸 것이다. 잘못된 매개변수를 일찍 발견하려면 빌더의 생성자와 메서드에서 입력 매개변수를 검사하고, 빌드 메서드가 호출하는 생성자에서 여러 매개변수에 걸친 불변식을 검사하면 된다. 공격에 대비해 이런 불변식을 보장하려면 빌더로부터 매개변수를 복사한 후 해당 객체 필드들도 검사해야 한다, 검사하다가 유효하지 않은 값을 발견하면 자세한 내용의 메시지를 담아 IllegalArgumentException을 던지도록 하자.

## 4\. 계층적으로 설계된 클래스와 빌더 패턴

빌더패턴은 계층적으로 설계된 클래스와 함께 쓸 때 더욱 유용하다. 각 계층의 클래스에 빌더를 멤버로 정의하여 추상 클래스에는 추상빌더를, 구체클래스는 구체 빌더를 가지게한다. 다음은 다음은 책의 예제를 그대로 구현해 본 계층적 설계 클래스와 빌더 패턴의 조합이다.

```
/*
    2-4 계층적으로 설계된 클래스와 잘 어울리는 빌더 패턴
 */
public abstract class Pizza {
    public enum Topping {HAM, MUSHROOM, ONION, PEPPER, SAUSAGE}
    final Set<Topping> toppings;
    
    abstract static class Builder<T extends Builder<T>> {
        EnumSet<Topping> toppings = EnumSet.noneOf(Topping.class);
        public T addTopping (Topping topping)   {
            toppings.add(Objects.requireNonNull(topping));
            return self();
        }
        abstract Pizza build();
        protected abstract T self();
    }
    Pizza (Builder<?> builder)  {
        toppings = builder.toppings.clone();
    }
}
```

Pizza.Builder 클레스는 재귀적 타입 한정을 이용하는 제네릭 타입이다. 추상 메서드인 self를 더해 하위클래스에서는 형변환 없이 메서드 연쇄를 지원할 수 있게 했으며, 이처럼 Self 타입이 없는 자바를 위한 우회 방법을 "시뮬레이트한 셀프타입"(simulated self-type) 관용구라고 한다. 다음은 Pizza 하위의 서로 다른 매개변수를 필수로 받는 2개의 클래스 예제이다.

```
/*
    2-5 뉴욕피자
 */
public class NyPizza extends Pizza {
    public enum Size {SMALL, MEDIUM, LARGE}
    private final Size size;
    public static class Builder extends Pizza.Builder<Builder> {
        private final Size size;
        public Builder(Size size)   {
            this.size = Objects.requireNonNull(size);
        }
        @Override
        public NyPizza build()  {
            return new NyPizza(this);
        }
        @Override
        protected Builder self()    {
            return this;
        }
    }
    private NyPizza(Builder builder)   {
        super(builder);
        size = builder.size;
    }
}
```

```
/*
    2-5 칼조네
 */
public class Calzone extends Pizza {
    private final boolean sauceInside;
    
    public static class Builder extends Pizza.Builder<Builder>  {
        private boolean sauceInside = false; // 기본값
        public Builder sauceInside() {
            sauceInside = true;
            return this;
        }
        @Override
        public Calzone build()  {
        	// 구현체 하위 클래스를 반환하도록 선언
            return new Calzone(this);
        }
        @Override
        protected Builder self()    {
            return this;
        }
        
    }
    
    private Calzone(Builder builder)    {
        super(builder);
        sauceInside = builder.sauceInside;
    }
    

}
```

각 하위 클래스의 빌더의 build 메서드는 해당 구현체 하위 클래스를 반환하도록 선언한다.  예제처럼 하위 클래스의 메서드가 상위 클래스의 메서드가 정의한 반환 타입이 아닌, 그 하위 타입을 반환하는 기능을 공변 반환 타이핑(covariant return typing)이라고이를 통해 클라이언트가 형변환에 신경 쓰지 않고 빌더 사용이 가능하다. 실제 클래스가 사용되는 방식을 확인해 보자.

```
NyPizza nyPizza = new NyPizza.Builder(SMALL).addTopping(SAUSAGE).addTopping(ONION).build();
Calzone calzone = new Calzone.Builder().addTopping(ONION).sauceInside().build();
```

빌더를 사용하면 가변인수 매개변수를 여러 개 사용 가능하며 각각 적절한 메서드로 나눠 선언하면 된다. 메서드를 여러 번 호출하도록 하고 각 호출 때 넘겨진 변수들을 하나의 필드로 모을 수도 있다.

빌더타입은 유연하다. 빌더하나로 여러 객체를 순회하면서 만들 수 있고 빌더에 넘기는 매개변수에 따라 다른 객체를 만들수도 있다. 객체마다 부여되는 일련번호 같은 특정 필드는 빌더가 알아서 채울 수도 있다.

## 4\. 빌더 패턴의 단점?

그렇다면 이러한 빌더 패턴엔 어떤 단점이 있을까? 먼저 객체를 만들기 위해 빌더부터 만들어야 한다는 단점이 있다. 빌더 생성하는 비용이 그리 크진 않겠지만 성능이 매우 민감한 경우 문제 될 수 있다.

그리고 매개변수의 개수가 몇 개 안될 때는 점층적 생성자 패턴보다 코드가 장황하다. 예제에서만 보아도 다른 패턴에 비해 좀더 복잡한 구조를 가지고 있다. 위의 예제들은 매개변수가 몇개 안 되는 경우이고, 빌더패턴은 4개 이상의 매개변수에서 진정한 값 어치를 한다.

또한 API는 최초 생성 시의 규격보다 시간이 지날수록 매개변수가 점차 많아지는 경향이 있기에 당장은 매개변수가 확장성과 함께 빌더 패턴을 고려해 볼 필요가 있다. 물론 생성자/정적 팩토리 방식으로 시작했다가 나중에 매개변수가 많아지면 빌더패턴으로 전환도 가능하지만 애초에 빌더로 시작하는 편이 나을 때가 많다.

## 5\. 결론

생성자/정적팩토리가 처리해야 할 매개변수가 많거나, 매개변수 중 다수가 필수값이 아니거나 같은 타입이라면 특히 빌더 패턴을 선택하자.

빌더는 점층적 생성자보다 클라이언트 코드를 읽고 쓰기 간결하게 해 주고,자바빈즈보다 훨씬 안전하다는 장점이 있다.
