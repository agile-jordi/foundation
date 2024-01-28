package com.agilogy.foundation.java.apiserver.jaxrs;

import com.agilogy.foundation.java.apiserver.Templates;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.Response;

import java.util.Collections;
import java.util.Set;

@Path("/")
public class TimeTrackingAdapter extends Application {

  private final Templates templates = new Templates();

  @Override
  public Set<Class<?>> getClasses() {
    return Collections.singleton(TimeTrackingAdapter.class);
  }

  @GET @Path("/")
  public Response sayHello() {
    return Response.ok(templates.index()).build();
  }


  @Path("/uploadTimeEntries")
  public UploadTimeEntries getItemContentResource() {
    return new UploadTimeEntries(templates);
  }

}

