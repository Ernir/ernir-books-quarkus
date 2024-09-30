package ernir.net.books.models;

import com.github.slugify.Slugify;

public record Author(String fullName, String firstName, String lastName) implements SearchResult {
  public String slug() {
    return Slugify.builder().build().slugify(fullName());
  }
}
