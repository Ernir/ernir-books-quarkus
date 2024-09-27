package ernir.net;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class BookServiceTest {

  @Test
  void findAllBooks() {
    BookCsvParser parser = mock(BookCsvParser.class);
    List<BookRecord> mockRecords =
        List.of(
            new BookRecord(
                "214240402",
                "Deathseed (The Weirkey Chronicles Book 8)",
                "Author1",
                "Lin,Sarah",
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
                Optional.of(LocalDate.parse("2024-07-21")),
                "",
                "",
                "",
                "",
                "",
                "",
                1,
                ""));
    when(parser.getBooks()).thenReturn(mockRecords);

    BookService bookService = new BookService(parser);

    var books = bookService.findAllBooks();
    // TODO make real assertions
    assertThat(books).hasSize(1);
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
