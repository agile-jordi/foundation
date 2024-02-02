package com.agilogy.javalin;

import com.agilogy.handlebars.HandlebarsTemplates;
import io.javalin.http.Context;
import io.javalin.http.UploadedFile;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;

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

  public static UploadedFile getFile(final @NotNull Context ctx, String fileName, String contentType) {
    var file = ctx.uploadedFile(fileName);
    if (file == null) {
      throw new HttpException.ClientError("File " + fileName + " not found");
    }
    if (!Objects.equals(file.contentType(), contentType)) {
      throw new HttpException.ClientError("File " + fileName + " is not a " + contentType);
    }
    return file;
  }

  public static @NotNull String formParamOrEmpty(final @NotNull Context ctx, final @NotNull String name) {
    final var result = ctx.formParam(name);
    return result == null ? "" : result;
  }
}
