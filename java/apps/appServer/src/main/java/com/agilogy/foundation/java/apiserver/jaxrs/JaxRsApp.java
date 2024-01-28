package com.agilogy.foundation.java.apiserver.jaxrs;

import jakarta.ws.rs.SeBootstrap;

import java.io.IOException;

public class JaxRsApp {
  public static void main(String[] args) throws IOException {
    SeBootstrap.start(new TimeTrackingAdapter(), SeBootstrap.Configuration.builder().port(8080).build())
               .toCompletableFuture().join();
    System.in.read();

  }
}
