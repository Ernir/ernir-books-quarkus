package ernir.net.books;

import ernir.net.books.models.Book;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("books")
public class BookWebResource {
  private final BookService bookService;

  BookWebResource(BookService bookService) {
    this.bookService = bookService;
  }

  @CheckedTemplate
  public static class Templates {
    public static native TemplateInstance books(List<Book> books);
  }

  @GET
  @Path("")
  @Produces(MediaType.TEXT_HTML)
  public TemplateInstance get() {
    return Templates.books(bookService.findReadBooks());
  }
}
