package com.agilogy.foundation.java.apiserver;

import com.agilogy.routing.Routing;
import com.agilogy.routing.request.HttpRequest;
import io.undertow.Undertow;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.util.ImmediateInstanceFactory;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javautils.NotNull;
import static javautils.ThrowingRunnable.runUnchecked;

import java.util.Enumeration;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class UndertowServer extends HttpServlet {

  private final @NotNull Routing<BiConsumer<HttpServletRequest, HttpServletResponse>> routes;

  public UndertowServer(@NotNull final Routing<BiConsumer<HttpServletRequest, HttpServletResponse>> routes) {
    this.routes = routes;
  }

  private <A> Stream<A> stream(Supplier<Enumeration<A>> enumeration) {
    final Iterable<A> iterable = () -> enumeration.get().asIterator();
    return StreamSupport.stream(iterable.spliterator(), false);
  }

  @Override protected void service(HttpServletRequest req, HttpServletResponse resp) {
    final var headers = stream(req::getHeaderNames).collect(Collectors.toMap(name -> name, req::getHeader));
    var httpRequest = new HttpRequest(req.getMethod(), req.getRequestURI(), headers);
    routes.handle(httpRequest).accept(req, resp);
  }

  public static void start(
        @NotNull final Routing<BiConsumer<HttpServletRequest, HttpServletResponse>> routes,
        final int port
  ) {
    runUnchecked(() -> {
      var servlet = Servlets.servlet("MyServlet", UndertowServer.class).addMapping("/");
      servlet.setInstanceFactory(new ImmediateInstanceFactory<>(new UndertowServer(routes)));
      DeploymentInfo deployment =
            Servlets.deployment()
                    .setClassLoader(UndertowServer.class.getClassLoader())
                    .setContextPath("/")
                    .setDeploymentName("undertow.war")
                    .addServlets(servlet);

      DeploymentManager manager = Servlets.defaultContainer().addDeployment(deployment);
      manager.deploy();

      Undertow server = Undertow.builder().addHttpListener(port, "localhost").build();
      server.start();
    });
  }
}
