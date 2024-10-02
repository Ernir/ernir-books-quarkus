package ernir.net.books;

import ernir.net.books.BookService.AuthorWithBooks;
import ernir.net.books.BookService.AuthorWithCounts;
import ernir.net.books.models.Book;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("")
public class BookWebResource {
  private final BookService bookService;

  BookWebResource(BookService bookService) {
    this.bookService = bookService;
  }

  @CheckedTemplate
  public static class Templates {
    public static native TemplateInstance books(List<Book> books);

    public static native TemplateInstance book(Book book);

    public static native TemplateInstance authors(List<AuthorWithCounts> authors);

    public static native TemplateInstance author(AuthorWithBooks author);
  }

  @GET
  @Path("")
  @Produces(MediaType.TEXT_HTML)
  public TemplateInstance findBooks() {
    return Templates.books(bookService.findReadBooks());
  }

  @GET
  @Path("{id}")
  @Produces(MediaType.TEXT_HTML)
  public TemplateInstance getBook(@PathParam("id") int id) {
    return Templates.book(bookService.getBookById(id));
  }

  @GET
  @Path("authors/")
  @Produces(MediaType.TEXT_HTML)
  public TemplateInstance findAuthors() {
    return Templates.authors(bookService.findAuthorsByBookCount());
  }

  @GET
  @Path("authors/{slug}")
  @Produces(MediaType.TEXT_HTML)
  public TemplateInstance getAuthor(@PathParam("slug") String slug) {
    return Templates.author(bookService.getAuthorWithBooks(slug));
  }
}
