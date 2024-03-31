package org.example.ch01.item08.codes;

public class Adult {
   public static void main(String[] args)   {
       try (Room myRoom = new Room(7)) {
           System.out.println("안녕~");
       }
   }
}