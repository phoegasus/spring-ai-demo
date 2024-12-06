package org.phoegasus.sample;

import java.util.Arrays;

public class App {
  public static void main(String[] args) {
    Arrays.stream(TypeJouet.values()).forEach(System.out::println);
  }
}
