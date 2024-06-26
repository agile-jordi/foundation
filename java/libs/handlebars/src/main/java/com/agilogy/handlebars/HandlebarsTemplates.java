package com.agilogy.handlebars;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.helper.ConditionalHelpers;
import org.jetbrains.annotations.NotNull;
import javautils.ThrowingSupplier;

import java.util.HashMap;
import java.util.Map;

public final class HandlebarsTemplates {

  private final @NotNull Handlebars handlebars;
  private final @NotNull Map<String, Template> compiledTemplates = new HashMap<>();

  public HandlebarsTemplates() {
    this.handlebars = new Handlebars();
    handlebars.registerHelper("eq", ConditionalHelpers.eq);
  }

  public @NotNull String render(@NotNull final String viewName) {
    return render(viewName, Map.of());
  }
  public @NotNull String render(@NotNull final String viewName, Object params) {
    return ThrowingSupplier.getUnchecked(() -> {
      var template = compiledTemplates.get(viewName);
      if (template == null) {
        template = handlebars.compile("views/" + viewName);
        compiledTemplates.put(viewName, template);
      }
      return template.apply(params);
    });
  }

}
