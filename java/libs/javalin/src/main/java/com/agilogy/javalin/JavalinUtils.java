package com.agilogy.javalin;

import com.agilogy.handlebars.HandlebarsTemplates;
import org.jetbrains.annotations.NotNull;
import io.javalin.http.Context;

import java.util.Map;

public final class JavalinUtils {

  private static final @NotNull HandlebarsTemplates handlebarsTemplates = new HandlebarsTemplates();

  public static void html(
        final @NotNull Context ctx,
        final int status,
        final @NotNull String template,
        final @NotNull Object params
  ) {
    ctx.html(handlebarsTemplates.render(template, params)).status(status);
  }

  public static void html(final @NotNull Context ctx, final int status, final @NotNull String template) {
    html(ctx, status, template, Map.of());
  }

  public static void html(final @NotNull Context ctx, final @NotNull String template) {
    html(ctx, 200, template);
  }

}
