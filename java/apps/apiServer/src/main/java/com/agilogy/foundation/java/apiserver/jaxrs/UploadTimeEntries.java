package com.agilogy.foundation.java.apiserver.jaxrs;

import com.agilogy.foundation.java.apiserver.Templates;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class UploadTimeEntries {
  private final Templates templates;

  public UploadTimeEntries(Templates templates) {
    this.templates = templates;
  }


  @GET
  public Response uploadTimeEntriesForm() {
    return Response.ok(templates.uploadTimeEntriesForm()).build();
  }

  @POST
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public Response uploadTimeEntries(MultipartFormDataInput formData) throws IOException {
    var is = formData.getParts().get(0).getBody();
    System.out.println(new BufferedReader(new InputStreamReader(is))
                             .lines().collect(Collectors.joining("\n")));
    return Response.ok(templates.uploadTimeEntriesForm()).build();
  }

}