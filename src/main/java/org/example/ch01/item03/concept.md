싱글턴이란 인스턴스를 하나만 생성할 수 있는 클래스로, 함수와 같은 무상태 (stateless) 객체나 설계상 유일해야 하는 시스템 컴포넌트, 혹은 익숙한 스프링 빈이 대표적인 싱글턴의 예로 들 수 있다. 클래스를 싱글턴으로 만들 경우, (타입을 인터페이스로 정의한 후 인터페이스를 구현해서 만든 싱글턴이 아니라면) 싱글턴 인스턴스를 Mock으로 대체할 수 없기에 테스트가 어려울 수 있다.

싱글턴을 만드는 보편적인 두가지 방식이 있는데, 두 방식 모두생성자는 private으로 감춰두고 유일한 인스턴스에 접근할 수 있는 수단으로 public static 멤버를 하나 마련해 둔다.

## 1\. public static final 필드 방식의 싱글턴

```
public class ElvisField {
    public static final ElvisField INSTANCE = new ElvisField();
    private ElvisField() {}
    public void leaveTheBuilding() {
        System.out.println("Whoa baby, I'm outta here!");
    }
}
```

Private 생성자는 public static final 필드인 elvis.instance를 초기화할 때 딱 한 번만 호출된다. public, protected 생성자가 없기에 초기화 시 생성된 인스턴스가 전체 시스템에서 유일함이 보장된다. 단, 권한이 있는 클라이언트의 경우 리플렉션 API 인 AccessibleObject.setAccessible을 사용해 private 생성자를 호출가능하다, 이 경우를 방지하기 위해서는 생성자를 수정하여 두 번째 객체가 되려 할 때 Exception을 던지면 된다. 리플렉션 API를 통해 새로운 객체가 생성되는 것을 확인해 보면

```
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
```

```
821270929
821270929
1160460865
```

기존 ElvisField1, ElvisField2에서는 동일 인스턴스를 반환하지만, 리플렉션 API를 통해 생성하면 새로운 객체가 생성이 된다. 이 경우, 다음과 같이 인스턴스가 이미 존재하는지를 체크하여 예외처리를 하면 싱글턴에 위배되는 상황을 방지할 수 있다.

```
public class ElvisField {
    public static final ElvisField INSTANCE = new ElvisField();
    private ElvisField() {
        // private 생성자는 ElvisField.INSTANCE를 초기화할 때 딱 한번만 호출된다.
        if (INSTANCE != null) {
            throw new IllegalStateException("Already initialized");
        }
        
    }
    public void leaveTheBuilding() {
        System.out.println("Whoa baby, I'm outta here!");
    }
}
```

#### 1-1. 장점

> \- 해당 클래스가 싱글턴임이 API에 명백히 드러남  
> \- public static 필드가 final이니 절대 다른 객체를 참조 불가  
> \- 코드가 훨씬 간결

## 2\. 정적 팩터리 방식의 싱글턴

두 번째 방법은 정적 팩터리 메서드를 public static 멤버로 제공하는 방식이다. 책의 예제를 먼저 살펴보면

```
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
```

Elvis.getInstance는 항상 같은 객체를 참조하기에 애플리케이션 내에서 유일함을 보장한다. (1번 방식의 리플렉션 API의 예외는 동일하게 적용된다.)

#### 2-1. 장점

> \- API를 바꾸지 않고도 싱글턴이 아니게 변경이 가능하다는 유연성  
> \- 유일한 인스턴스를 반환하는 팩터리 메서드가 호출하는 스레드별로 다른 인스턴스를 반환하게 수정도 가능  
> \- 정적 팩터리를 제네릭 싱글턴 팩터리로 생성 가능  
> \- 정적 팩터리메서드 참조를 공급자로 사용 가능 ex) Elvis::getInstance를 Supplier <Elvis>로 사용 가능  
> \* 이러한 장점이 필요 없다면 public 필드 방식이 더 좋다.

위의 두 방식 중 하나로 만든 싱글턴 클래스를 직렬화하려면 단순히 Serializable을 구현하는 및 선언하는 것으로는 부족하다.

직렬화된 인스턴스를 역직렬화할 때마다 새로운 인스턴스가 생성되는 것을 방지하기 위해 readResolve() 메서드를 제공해야 한다.

```
private Object readResolve() {
        return INSTANCE;
    }
```

## 3\. 열거 타입 방식의 싱글턴 - 바람직한 방법

이펙티브 자바에서 말하는 싱글턴을 만드는 가장 바람직한 세 번째 방법은 원소가 하나인 열거 타입을 선언하는 것이다.

```
public enum ElvisEnum {
    INSTANCE;
    public void leaveTheBuilding() {
        System.out.println("Whoa baby, I'm outta here!");
    }
}
```

public 방식과 비슷하지만 훨씬 간결하다. 바로 직렬화가 가능하며 복잡한 직렬화 상황이나 리플렉션 공격에도 인스턴스가 중복되어 생성되는 경우를 완벽하게 막아준다. 대부분의 상황에서는 원소가 하나뿐인 열거 타입이 싱글턴을 만드는 가장 좋은 방법이다.  (만들려는 싱글턴이 Enum 외의 클래스를 상속해야 한다면 이 방법은 사용불가)
