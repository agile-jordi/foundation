package com.agilogy.foundation.java.apiserver;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import static javautils.ThrowingRunnable.runOrThrow;
import static javautils.ThrowingSupplier.getOrThrow;

import java.util.Map;

public final class Templates {

  private Template index = null;
  private Template uploadTimeEntriesForm = null;

  public Templates() {
    runOrThrow(() -> {
      Handlebars handlebars = new Handlebars();
      handlebars.compile("views/header");
      this.index = handlebars.compile("views/index.html");
      this.uploadTimeEntriesForm = handlebars.compile("views/uploadTimeEntriesForm.html");
    });
  }

  public String index() {
    return getOrThrow(() -> index.apply(Map.of("title", "Agilogy Time Tracking")));
  }

  public String uploadTimeEntriesForm() {
    return getOrThrow(() -> uploadTimeEntriesForm.apply(Map.of("title", "Upload time entries")));
  }
}
