package ernir.net;

import static ernir.net.BookDataColumns.*;

import jakarta.enterprise.context.ApplicationScoped;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class BookCsvParser {

  private final List<BookRecord> books;

  BookCsvParser(@ConfigProperty(name = "net.ernir.book.csv-location") String csvLocation) {
    List<CSVRecord> records;
    try (BufferedReader bufferedReader =
            new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(csvLocation)));
        CSVParser csvParser =
            new CSVParser(
                bufferedReader,
                CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {
      records = csvParser.getRecords();
    } catch (IOException ioException) {
      records = List.of();
    }
    books = records.stream().map(BookCsvParser::toBookRecord).toList();
    System.out.println("Loaded " + books.size() + " books from CSV file");
  }

  public List<BookRecord> getBooks() {
    return books;
  }

  private static BookRecord toBookRecord(CSVRecord record) {
    return new BookRecord(
        record.get(ID),
        record.get(TITLE),
        record.get(AUTHOR),
        record.get(AUTHOR_LAST_FIRST),
        Arrays.stream(record.get(ADDITIONAL_AUTHORS).split(",")).toList(),
        Optional.of(record.get(ISBN)).filter(s -> !s.isBlank()),
        Optional.of(record.get(ISBN_13)).filter(s -> !s.isBlank()),
        Integer.parseInt(record.get(MY_RATING)),
        Double.parseDouble(record.get(AVERAGE_RATING)),
        record.get(PUBLISHER),
        record.get(BINDING),
        Optional.of(record.get(NUMBER_OF_PAGES)).filter(s -> !s.isBlank()).map(Integer::parseInt),
        Optional.of(record.get(YEAR_PUBLISHED)).filter(s -> !s.isBlank()).map(Integer::parseInt),
        Optional.of(record.get(ORIGINAL_PUBLICATION))
            .filter(s -> !s.isBlank())
            .map(Integer::parseInt),
        Optional.of(record.get(DATE_READ))
            .filter(s -> !s.isBlank())
            .map(s -> s.replace("/", "-"))
            .map(LocalDate::parse),
        Optional.of(record.get(DATE_ADDED))
            .filter(s -> !s.isBlank())
            .map(s -> s.replace("/", "-"))
            .map(LocalDate::parse),
        record.get(BOOKSHELVES),
        record.get(BOOKSHELVES_WITH_POSITIONS),
        record.get(EXCLUSIVE_SHELF),
        record.get(MY_REVIEW),
        record.get(SPOILER),
        record.get(PRIVATE_NOTES),
        Integer.parseInt(record.get(READ_COUNT)),
        record.get(OWNED));
  }
}
