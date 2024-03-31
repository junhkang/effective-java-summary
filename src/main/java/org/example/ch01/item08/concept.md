## 1\. finalizer와 cleaner란?

finalizer와 cleaner는 자바의 2가지 객체 소멸자이다. finalizer는 **Object.finalize()** 메서드를 오버라이딩 함으로써 사용된다. 작동 여부 및 시점을 예측할 수 없고 상황에 따라 위험할 수 있어 일반적으로 불필요하며, 기능의 잘못된 동작, 낮은 성능, 이식성 문제의 원인이 되기도 한다. 기본적으로는 사용하면 안 되고, 자바 9에서는 finalizer를 deprecated API로 지정하고, **java.lang.ref.Cleaner** 클래스를 사용하여 구현된 cleaner를 대안으로 제시하였으나, cleaner 또한 finalizer보다는 덜 위험 하지만 여전히 예측불가하고 성능이 좋지 않아 일반적으로 불필요하다. 

## 2\. 사용 시 문제점

### 2-1. 예측불가능한 실행시점

finalizer, cleaner는 즉시 실행된다는 보장이 없다. 객체에 접근할 수 없게 된 후 언제 실행될지 알 수 없다는 뜻이며, 특정시점에 실행을 요하는 작업에는 사용이 불가능하다. 예를 들어 파일 닫기를 기능을 맡기면, 제때 안 닫히기 때문에 시스템이 열 수 있는 파일 개수에는 한계가 있고 큰 문제로 이어질 수 있다. finazlier, cleaner가 언제 수행될지는 전적으로 가비치 콜렉터 알고리즘에 달려있고, 가비지 콜렉터 구현마다 다르기에 finalizer, cleaner 수행시점에 의존되는 모든 프로그램의 동작 또한 예측할 수 없다. 이는 두 번째 문제인 "이식성 문제"로도 이어진다.

### 2-2. 이식성 문제

가비지 컬렉션의 작동 방식은 JVM의 구현에 따라 다르기 때문에, 서로 다른 JVM에서는 Finalizer의 동작이 달라질 수 있다. 그러므로 테스트한 JVM에서는 잘 작동해도 운영시점의 시스템에서는 잘 작동 안 할 수가 있다.

### 2-3. 성능 저하

클래스에 finalizer를 쓰면, 그 인스턴스의 자원회수가 멋대로 지연될 수 있다. finalizer는 객체가 가비지 컬렉션에 의해 처리될 때 실행되지만, 가비지 컬렉터의 실행시기는 JVM에 따라 달라지며, 프로그램의 실행흐름을 예측하기 힘들다. 필요한 자원이 즉시 실행되지 않고 예상 못한 시점까지 남아있을 수 있기에 메모리 누수 같은 문제로 이어질 수 있다. 또한 finalizer가 실행되는 과정에서는 추가적인 처리가 필요하기에 가비지 컬렉터는 finalizable 객체를 별도로 관리해야 하며, 이 객체들은 일반적인 가비지 컬렉션 프로세스보다 더 복잡한 과정을 거친다. 따라서 전반적인 가비지 컬렉션 성능에 영향을 미치며, 애플리케이션 응답시간에도 영향을 미치게 된다. 

### 2-4. finalize 스레드의 우선순위

finalizer 스레드는 다른 애플리케이션 스레드보다 우선순위가 낮아서 실행이 안된 채 대기만 하는 상황이 발생할 수도 있으며, 자바 언어 명세는 어떤 스레드 어떤 순서대로가 finalizer를 수행할지 명시하지 않고 있어 명확한 해결방법이 없다. cleaner는 자신이 수행할 스레드를 제어할 수 있다는 면에서 조금 낫지만 여전히 백그라운드에서 수행되며 가비지 컬렉터의 통제하에 있으니 즉각 수행을 보장할 수 없다는 점은 동일하다.

### 2-5. 수행 여부의 불확실성

finalizer와 cleaner는 실행 시점뿐 아니라 수행 여부도 보장하지 않는다. 접근할 수 없는 일부 객체에 포함된 종료 작업을 수행하지 못한 채로 프로그램이 중단될 수 있다는 뜻이다. JVM이 종료 과정에서 남아있는 모든 finalizer 스레드를 실행하려고 시도하지만, System.exit(), 운영체제의 강제 종료 등 시스템이 갑자기 종료되면 실행을 보장할 수 없다.  그래서 프로그램의 생애주기와 상관없는 중요한 자원이나, 상태를 영구적으로 수정하는 작업에서는 절대 Finalizer / Cleaner에 의존하면 안 된다. 데이터베이스 같은 공유자원의 영구락을 해제하는 작업을 Finalizer와 Cleaner에 맡겨놓으면 분산시스템 전체가 서서히 멈추게 될 것이다.

가비지 컬렉션과 Finalization을 유도할 수 있는 **System.gc, System.runFinalization** 메서드가 있지만 실행 가능성을 높여줄 뿐, 실행을 보장하진 않기에 자원 해제와 관련된 중요한 작업은 명시적으로 관리하고, finalizer, cleaner에 의존하지 않는 방향으로 설계하는 것이 좋다. 

자원의 회수를 완전히 보장해 주는 기능이 존재하기는 하지만 사용이 권장되지는 않는다. **System.runFinalizersOnExit, Runtime.runFinalizersOnExit**를 사용하면 자바 프로그램이 종료될 때 모든 객체에 대해 finalizer를 실행하도록 강제하는 기능을 제공한다. 이론적으로는 프로그램 종료 시 모든 리소스가 정리되도록 보장하는 듯 보이지만, 이 메서드들은 사용이 권장되지 않는다. 이 메서드들은 내부적으로 Thread.stop을 사용할 수 있는데, 이 경우 스레드를 강제로 종료시키지만, 락이 비정상적으로 해제되거나 중요한 데이터가 변경될 위험이 있다.

<p align="center"><img src="./images/img.png"/></p>

### 2-6. 예외처리

finalizer도중 예외가 발생하면 이후 작업이 남아도 그 순간 종료된다. 처리 안된 예외 때문에 해당 객체는 마무리가 덜 된 상태로 남을 수 있다. 다른 스레드가 이처럼 훼손된 객체를 사용하려고 하면 어떻게 동작할지 예측불가하다. 보통 미처리 예외가 스레드를 중단하면, 스택 트레이스 내역을 출력하지만,  finalizer에서 발생하면 경고조차 출력 안 하지 않는다. (Cleaner를 사용하는 라이브러리는 자신의 스레드를 통제하기 때문에 이런 문제는 발생하지 않는다)

### 2-7. 보안문제

finalizer 공격에 노출되어 심각한 보안문제도 일으킬 수 있다. 공격자는 악의적인 목적으로 설계된 하위 클래스를 생성하고 finalizer를 오버라이드 하여 악의적인 코드를 포함할 수 있다. 생성자나 직렬화 과정 (readObject, readResolve)에서 예외가 발생하면 이 생성되다만 객체에서 악의적인 하위클래스의 finalizer가 수행될 수 있게 한다. 원래 생성되다가 만 객체는 가비지 컬렉션 대상이 되지만 finalizer가 아직 실행되지 않았기에, 가비지 컬렉션에 의해 수집되기 전에 finalizer가 실행된다. 이때 하위 클래스의 finalizer 내부에서 실행되는 코드는 정적 필드에 자신의 참조를 할당하여 가비지 컬렉터가 수집하지 못하게 할 수 있다. 이렇게 "살아남은" 객체가 만들어지고 원래 의도하지 않았던 여러 동작을 수행하게 할 수 있다. 예를 들어 보안이 강화된 영역에서의 작업수행, 민감정보 노출, 시스템 리소스 남용 등이 가능하다. 

## 3\. 대안

객체 생성을 막으려면 생성자에서 예외를 던지는 것만으로 충분하지만 finalizer가 있다면 그렇지 않다. finalizer가 존재하는 경우 객체 생성 중 예외가 발생해도, 가비지 컬렉션 대상이 되기 전에 finalizer가 실행될 수 있기 때문이다. final 클래스들은 그 누구도 하위클래스를 만들 수 없으니 (상속할 수 없으니) 이 공격에서 자연스럽게 보호된다. final이 아닌 크래스를 finalizer공격으로부터 방어하려면 아무 일도 하지 않는 finalizer메서드를 만들고 final로 선언하면 된다.

```
protected final void finalize() throws Throwable {
    // 아무 작업도 수행하지 않음
}
```

이렇게 선언하면 클래스가 final로 선언되지 않아 상속이 가능한 경우에도, 하위 클래스에서 finalize() 메서드를 오버라이드 할 수 없게 만들어 공격으로부터 보호될 수 있다,. 

그렇다면 파일이나 스레드 등 종료해야 할 자원을 담고 있는 객체의 클래스에서 finalizer / cleaner의 대안은 무엇일까? AutoCloseable을 구현하여, 클라이언트에서 인스턴스를 다 쓰면 close를 호출하는 방식을 사용하면 된다. AutoCloseable 인터페이스는 단 하나의 메서드 'close()'를 선언하고, 이 인터페이스를 구현하는 클래스는 close() 메서드 내에서 필요한 자원 정리 작업 (파일 닫기, 네트워크 연결해제 등)을 수행한다. 각 인스턴스는 자신이 닫혔는지를 추적하는 것이 좋다. close 메서드에서 이 객체는 더 이상 유효하지 않음을 필드에 기록하고 다른 메서드는 이 필드를 검사해서 객체가 닫힌 후에 호출했다면 IllegalstateException을 던짐으로써 이미 닫힌 자원을 잘못 사용하는 것을 방지할 수 있다. 

```
public class Resource implements AutoCloseable {
    private boolean closed = false;

    public void doSomething() {
        if (closed) {
            throw new IllegalStateException("Resource is closed");
        }
    }

    @Override
    public void close() {
        closed = true;
        // 그 외 자원 정리 작업 수행, 예: 파일 닫기, 네트워크 연결 해제
    }
}
```

## 4\. 적절한 사용

그러면 이렇게 단점이 많은 Finalizer와 Cleaner는 왜 존재하는걸가? 물론 여러 단점과 함께 사용될 때 주의가 필요하지만, 특정상황에서는 유용할 수도 있다.

### 4-1. 안정망

자원소유자가 close메서드를 호출하지 않는 것에 대비한 안정망 역할을 한다. 즉 클라이언트 코드가 자원회수를 제대로 하지 않았을 때, 자원이 늦게라도 해제될 수 있도록 한다. 완벽한 해결책은 아니지만 자원 누수를 일부 방지할 수 있는 마지막 수단이 될 수 있다. 다만 안정망 역할의 finalizer와 cleaner를 구현할 때는 그만한 가치가 있는지 자원의 중요성, 자원의 해제 필요성, 프로그램의 전반적인 안정성 들을 충분히 고려 후 사용해야 한다. 자바 라이브러리의 일부 클래스는 사용자의 실수를 대비하여 안정망 역할의 finalizer를 제공한다. FileInputStream, FileOutputStream, ThreadPoolExecutor 등의 라이브러리는 내부적으로 자원을 회수하는 로직을 포함하고 있어, 이 객체들이 사용되지 않고 가비지 컬렉션 대상이 될 때 자원을 정리할 수 있다. 

### 4-2. 네이티브 피어 자원의 관리

네이티브 페어(native pear)와 연결된 객체에 대한 자원관리를 해준다. 일반 자바 객체에서 네이티브 메서드를 통해 기능을 위임한 네이티브 객체를 말하며 네이티브 피어는 자바 객체가 아니니 GC가 존재를 알 수 없다. 네이티브 피워와 연결된 자원은 자바 플랫폼의 자동 메모리관리 영역 밖에 있으므로 finalizer / cleaner 가 나서서 처리하기에 적당한 작업이다. 다만 성능저하를 감당할 수 있고, 네이티브 피어가 중요한 자원을 가지고 있지 않을 때만 해당된다. 네이티브 피어가 중요한 시스템 자원을 많이 사용하거나 대용량 메모리할당, 파일, 네티워크 연결 등 자원 해제를 신속하게 수행해야 하는 경우에는 앞서 말한 close를 사용해야 한다.

### 4-3. Cleaner의 사용 예

Cleaner는 사용하기 좀 더 까다롭다. Cleaner를 안정망으로 사용하는 AutoCloseable클래스인 Room 클래스를 살펴보자

```
public class Room implements AutoClosable {
    private static final Cleaner cleaner = Cleaner.create();

    private static class State implements Runnable  {
        int numJunkPiles;
        State(int numJunkPiles) {
            this.numJunkPiles = numJunkPiles
        }
        @Override public void run() {
            System.out.println("방 청소");
            numJunkPiles = 0;
        }
    }

    private final State state;

    private final Cleaner.Cleanable cleanable;

    public Room(int numJunkPiles)   {
        state = new State(numJunkPiles);
        cleanable = cleaner.register(this, state);
    }
    @Override public void close()   {
        cleanable.clean();
    }
}
```

Cleaner를 사용하여 객체와 연결된 정리작업을 Cleaner에 등록할 수 있고, 객체가 가비지 컬렉션 되어 메모리에서 제거될 때 해당 작업이 실행된다. 또한 공개 API에 노출되지 않기에 정리작업을 내부적으로만 관리할 수 있고 직접 호출할 수 없다.

static으로 선언된 중첩클래스인 State는 cleaner가 방을 청소할 때 수거할 자원들(자바 객체에 의해 관리되어야 하는 자원)을 담고 있다. 예제에서는 방안의 쓰레기 수를 뜻하는 numJunkPiles 필드가 수거할 자원에 해당하지만 실제 활용에서는, 이 필드는 네이티브 피어와 같은 더 복잡한 자원을 관리하는 데 사용될 수 있기에, 가리키는 포인터를 담은 final long 변수로 대체될 수도 있다.

State는 Runnable 인터페이스를 구현하고 Cleaner에 의해 자원이 해제될 때 수행될 정리 작업들을 정의한다. 이 안의 run() 메서드는 실제 자원 해제 로직이 구현(예제에서는 쓰레기 수를 0으로 설정, 방청소 문구를 print 하는 작업)되며 cleanable에 의해 한번만 호출된다.

Room 객체는 생성자에서 Cleaner를 사용하여 Room자신과 State 객체를 등록하고, 이 과정에서 Cleaner는 Cleanable 인스턴스를 생성한다.

run 메서드가 호출되는 2가지 상황을 살펴보자

-   **close 메서드의 명시적 호출 -** 보통 Room의 close 메서드를 호출할 때. close 메서드에서 Cleanable의 clean을 호출하면 이 메서드 안에 run을 호출한다.
-   **가비지 컬렉터에 의한 자동 호출 -** 가비지 컬렉터가 Room을 회수할 때까지 클라이언트가 close를 호출 안 하면, cleaner가 State의 run 메서드를 호출해 준다. (즉시 실행을 보장하지 않는다.)

State인스턴스는 절대 Room인스턴스를 참조하면 안 된다. Room 인스턴스를 참조할 경우 순환참조가 생겨 가비지 컬렉터가 Room 인스턴스를 회수할 수 없다. (State가 정적 중첩 클래스인 이유이다.) 정적이 아닌 중첩 클래스는 자동으로 바깥 객체의 참조를 갖게 된다. 이와 비슷하게 람다 역시 바깥 객체의 참조를 갖기 쉬우니 사용하지 않는 것이 좋다.

## 5\. 올바른 사용 예제

앞서 이야기한 대로 Room의 cleaner는 단지 안정망으로만 쓰였다. 클라이언트가 모든 Room생성을 try with resources블록으로 감쌌다면 자동 청소는 전혀 필요하지 않다. 다음 잘 짜인 클라이언트 코드의 예를 확인해 보자

```
public static void main(String[] args)   {
    try (Room myRoom = new Room(7)) {
        System.out.println("안녕~");
    }
}
```

Room 객체는 try-with-resources 문내에서 생성된다. Room 객체가 AutoCloseable 인터페이스를 구현하고 있기 때문에 가능하며, try 블록이 종료된 후 "안녕~"을 출력한 후 Room 객체의 close() 메서드가 자동으로 호출된다. 따라서 "안녕"을 출력한 후 "방 청소"가 확정적으로 출력된다. 반면에 아래 코드를 확인해 보면

```
public static void main(String[] args) {
    new Room(99);
    System.out.println("아무렴");
}
```

이 코드는 Room 객체가 생성된 후 어떠한 변수도 할당되지 않고, close() 메서드가 명시적으로 호출되지 않는다. 이경우 Room 객체는 가비지 컬렉션 대상이 되며 Cleaner에 의해 정리될 수 있다. 하지만 Cleaner가 실제로 언제 작업을 수행할지는 가비지의 실행 타이밍에 의존되어 있기 때문에 "아무렴" 후에 "방청소"의 출력 시간을 확정할 수 없으며, JVM에 따라 작업 자체가 이루어지지 않을 수 있다. (특히 System.exit()'을 통해 종료될 경우)

<p align="center"><img src="./images/img2.png"/></p>

## 6\. 정리

Cleaner는 안정망 역할이나 중요하지 않은 네이티브 자원회수용으로만 사용하고, 이 경우에도 불확실성과 성능저하를 주의해서 사용해야 한다. 자원의 해제가 명확히 필요한 경우 AutoCloseable 인터페이스를 구현하여 사용하고 try-with-resources의 사용을 통해 클라이언트 코드에서 자원을 적극적으로 관리해 주도록 하자.

블로그 : https://junhkang.tistory.com/80