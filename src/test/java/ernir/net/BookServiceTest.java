package ernir.net;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ernir.net.books.data.BookCsvLine;
import ernir.net.books.data.BookCsvParser;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import ernir.net.books.models.Series;
import ernir.net.books.models.SeriesMembership;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class BookServiceTest {

  private static final BookCsvLine DEATHSEED_CSV =
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
          Optional.of(LocalDate.parse("2024-07-21")));
  private static final BookCsvLine WAKESPIRE_CSV =
      new BookCsvLine(
          "195698323",
          "Wakespire (The Weirkey Chronicles #7)",
          "Sarah Lin",
          "Lin, Sarah",
          List.of(),
          Optional.empty(),
          Optional.empty(),
          0,
          4.46,
          "",
          "Kindle edition",
          Optional.of(505),
          Optional.of(2023),
          Optional.of(2023),
          Optional.of(LocalDate.parse("2024-05-02")),
          Optional.of(LocalDate.parse("2024-04-24")));
  private static final BookCsvLine THE_ENGINEER_CSV =
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
          Optional.of(LocalDate.parse("2024-04-18")));
  private static final BookCsvLine UNFETTERED_CSV =
      new BookCsvLine(
          "15832300",
          "Unfettered (Unfettered, #1)",
          "Shawn Speakman",
          "Speakman, Shawn",
          List.of(
              "Robert Jordan",
              "Terry Brooks",
              "Patrick Rothfuss",
              "Brandon Sanderson",
              "Jacqueline Carey",
              "Tad Williams",
              "R.A. Salvatore",
              "Naomi Novik",
              "Geno Salvatore",
              "Peter V. Brett",
              "Daniel Abraham",
              "Lev Grossman",
              "David Anthony Durham",
              "Peter Orullian",
              "Blake Charlton",
              "Michael J. Sullivan",
              "Eldon Thompson",
              "Robert V.S. Redick",
              "Carrie Vaughn",
              "Kevin Hearne",
              "Jennifer Bosworth",
              "Todd Lockwood",
              "Mark  Lawrence"),
          Optional.empty(),
          Optional.empty(),
          4,
          3.81,
          "Grim Oak Press",
          "Hardcover",
          Optional.of(574),
          Optional.of(2013),
          Optional.of(2013),
          Optional.of(LocalDate.parse("2014-02-25")),
          Optional.of(LocalDate.parse("2014-02-25")));
  private static final BookCsvLine SEVEN_OF_INFINITIES_CSV =
      new BookCsvLine(
          "56254221",
          "Seven of Infinities (Xuya Universe)",
          "Aliette de Bodard",
          "Bodard, Aliette de",
          List.of(),
          Optional.of("1625675321"),
          Optional.empty(),
          0,
          3.99,
          "JABberwocky Literary Agency, Inc.",
          "Kindle edition",
          Optional.of(115),
          Optional.of(2020),
          Optional.of(2020),
          Optional.of(LocalDate.parse("2023-11-25")),
          Optional.of(LocalDate.parse("2023-11-09")));
  private static final BookCsvLine BRAVE_NEW_WORLD_CSV =
      new BookCsvLine(
          "5129",
          "Brave New World",
          "Aldous Huxley",
          "Huxley, Aldous",
          List.of(),
          Optional.of("0060929871"),
          Optional.of("9780060929879"),
          4,
          3.99,
          "HarperPerennial / Perennial Classics",
          "Paperback",
          Optional.of(268),
          Optional.of(1998),
          Optional.of(1932),
          Optional.of(LocalDate.parse("2014-04-29")),
          Optional.of(LocalDate.parse("2014-04-29")));

  @Test
  void findAllBooks() {
    BookCsvParser parser = mock();
    List<BookCsvLine> csvs =
        List.of(
            DEATHSEED_CSV,
            WAKESPIRE_CSV,
            THE_ENGINEER_CSV,
            UNFETTERED_CSV,
            SEVEN_OF_INFINITIES_CSV,
            BRAVE_NEW_WORLD_CSV);
    when(parser.getBooks()).thenReturn(csvs);

    BookService bookService = new BookService(parser);

    var books = bookService.findAllBooks();
    // TODO make real assertions
    assertThat(books).hasSize(6);
  }

  @Test
  void findAllBooks_none_available() {
    BookCsvParser parser = mock();

    when(parser.getBooks()).thenReturn(List.of());

    BookService bookService = new BookService(parser);

    var books = bookService.findAllBooks();
    assertThat(books).isEmpty();
  }

  @Test
  void getSeriesByName() {
    BookCsvParser parser = mock();

    when(parser.getBooks()).thenReturn(List.of(DEATHSEED_CSV, WAKESPIRE_CSV));

    BookService bookService = new BookService(parser);

    var series = bookService.getSeriesByName("The Weirkey Chronicles");

    assertThat(series).hasValue(new Series("The Weirkey Chronicles"));
  }

  @Test
  void getSeriesByName_not_found() {
    BookCsvParser parser = mock();

    when(parser.getBooks()).thenReturn(List.of(DEATHSEED_CSV, WAKESPIRE_CSV));

    BookService bookService = new BookService(parser);

    var series = bookService.getSeriesByName("The Weird Chronicles");

    assertThat(series).isEmpty();
  }

  @Test
  void findBooksOfSeries() {
    BookCsvParser parser = mock();

    when(parser.getBooks()).thenReturn(List.of(DEATHSEED_CSV, WAKESPIRE_CSV));

    BookService bookService = new BookService(parser);

    Series theWeirkeyChronicles = new Series("The Weirkey Chronicles");
    List<SeriesMembership> series = bookService.findBooksOfSeries(theWeirkeyChronicles);

    assertThat(series)
        .containsExactly(
            new SeriesMembership(theWeirkeyChronicles, Optional.of("8")),
            new SeriesMembership(theWeirkeyChronicles, Optional.of("7")));
  }

  @Test
  @Disabled
  void findBookOfSeriesMembership() {}

  @Test
  @Disabled
  void testFindBookByTitle() {}

  @Test
  @Disabled
  void testSearch() {}

  @Test
  @Disabled
  void testGetBooksByAuthor() {}
}
