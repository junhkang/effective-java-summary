package org.example.ch01.item07.codes;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.Supplier;

public class Sample {
    public void scope() {
        {
            // SCOPE A 시작
            int a = 10; // 'a' 변수는 이 블록 안에서만 유효
            System.out.println(a); // 블록 A 끝 - 여기를 벗어나는 순간 'a'는 더 이상 접근할 수 없음
        }
        //System.out.println(a); // 여기서 'a'를 사용하려고 하면 scope에서 벗어나기에 컴파일 에러 발생
        {
            // 블록 B 시작 - 이 블록에서는 'a'를 새로 선언할 수 있지만, 위의 'a'와는 전혀 다른 변수임
            String a = "Hello";
            System.out.println(a); // 블록 B 끝 - 여기를 벗어나는 순간 새로 선언된 'a'도 접근할 수 없게 됨
        }
    }
    public void weakHashMap() {
        WeakHashMap<Object, String> cache = new WeakHashMap<>();
        Object key = new Object(); // 이 객체는 키로 사용됨
        cache.put(key, "Value"); // 키와 값 쌍을 캐시에 저장

        key = null; // 이제 'key' 객체에 대한 강한 참조가 없음
    }
}