package ernir.net;

import jakarta.inject.Inject;
import java.util.List;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Query;

@GraphQLApi
public class BookResource {

  @Inject BookService service;

  @Query("allBooks")
  @Description("Get all Books")
  public List<Book> getAllFilms() {
    return service.findAllBooks();
  }
}
