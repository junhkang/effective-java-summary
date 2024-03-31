package org.example.ch01.item08.codes;

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