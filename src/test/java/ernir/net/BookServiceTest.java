package ernir.net;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ernir.net.books.data.BookCsvLine;
import ernir.net.books.data.BookCsvParser;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class BookServiceTest {

  @Test
  void findAllBooks() {
    BookCsvParser parser = mock();
    List<BookCsvLine> mockRecords =
        List.of(
            new BookCsvLine(
                "214240402",
                "Deathseed (The Weirkey Chronicles Book 8)",
                "Sarah Lin",
                "Lin, Sarah",
                List.of(),
                Optional.empty(),
                Optional.empty(),
                0,
                4.52,
                "",
                "Kindle edition",
                Optional.of(465),
                Optional.of(2024),
                Optional.empty(),
                Optional.of(LocalDate.parse("2024-07-28")),
                Optional.of(LocalDate.parse("2024-07-21"))),
            new BookCsvLine(
                "201127814",
                "The Engineer (The Last Horizon, #2)",
                "Will Wight",
                "Wight, Will",
                List.of(),
                Optional.empty(),
                Optional.empty(),
                4,
                4.52,
                "Hidden Gnome Publishing",
                "Kindle edition",
                Optional.of(465),
                Optional.of(2023),
                Optional.of(2023),
                Optional.of(LocalDate.parse("2024-04-23")),
                Optional.of(LocalDate.parse("2024-04-18"))));
    when(parser.getBooks()).thenReturn(mockRecords);

    BookService bookService = new BookService(parser);

    var books = bookService.findAllBooks();
    // TODO make real assertions
    assertThat(books).hasSize(2);
  }

  @Test
  @Disabled
  void findBookByTitle() {}

  @Test
  @Disabled
  void search() {}

  @Test
  @Disabled
  void getBooksByAuthor() {}
}
