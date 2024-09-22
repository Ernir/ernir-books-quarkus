package ernir.net;

import jakarta.enterprise.context.ApplicationScoped;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@ApplicationScoped
public class BookData {

  BookData() {
    try (InputStream inputStream = getClass().getResourceAsStream("/goodreads_library_export.csv");
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
      List<String> lines = reader.lines().toList();
      System.out.println(lines);
    } catch (IOException ioException) {
      ioException.printStackTrace();
    }
  }
}
