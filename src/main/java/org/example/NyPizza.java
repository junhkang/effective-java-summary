package org.example;


import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

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
