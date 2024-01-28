package com.agilogy.foundation.java.apiserver;

public class App {

  public static void main(final String[] args) {
    final var timeTrackingAdapter = new TimeTrackingAdapter();
    timeTrackingAdapter.start(8080, false);
  }


}