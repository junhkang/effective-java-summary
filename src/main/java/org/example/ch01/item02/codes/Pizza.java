package org.example.ch01.item02.codes;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

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
