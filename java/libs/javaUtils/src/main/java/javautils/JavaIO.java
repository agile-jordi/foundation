package javautils;

import static javautils.ThrowingSupplier.getUnchecked;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public final class JavaIO {
  private JavaIO() {}

  public static byte[] readInputStream(final @NotNull InputStream inputStream) {
    return getUnchecked(() -> {
      var os = new ByteArrayOutputStream();
      var writer = new BufferedWriter(new OutputStreamWriter(os));
      new BufferedReader(new InputStreamReader(inputStream)).transferTo(writer);
      writer.flush();
      return os.toByteArray();
    });
  }
}
