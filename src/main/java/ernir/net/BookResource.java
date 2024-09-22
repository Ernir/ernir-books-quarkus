package ernir.net;

import java.util.List;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Query;

@GraphQLApi
public class BookResource {
  private final BookService service;

  public BookResource(BookService service) {
    this.service = service;
  }

  @Query("allBooks")
  @Description("Get all Books")
  public List<Book> getAllFilms() {
    return service.findAllBooks();
  }
}
