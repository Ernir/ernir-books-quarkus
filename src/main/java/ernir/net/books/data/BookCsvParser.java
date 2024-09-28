package ernir.net.books.data;

import static ernir.net.books.data.BookDataColumns.*;

import jakarta.enterprise.context.ApplicationScoped;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class BookCsvParser {

  private final List<BookCsvLine> books;

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
      System.err.println("Error parsing CSV file: " + ioException.getMessage());
      records = List.of();
    }
    books = records.stream().map(BookCsvParser::toBookRecord).toList();
    System.out.println("Loaded " + books.size() + " books from CSV file");
  }

  public List<BookCsvLine> getBooks() {
    return books;
  }

  private static BookCsvLine toBookRecord(CSVRecord record) {
    return new BookCsvLine(
        record.get(ID),
        record.get(TITLE),
        record.get(AUTHOR),
        record.get(AUTHOR_LAST_FIRST),
        Arrays.stream(record.get(ADDITIONAL_AUTHORS).split(",")).toList(),
        Optional.of(record.get(ISBN))
            .map(isbn -> isbn.replace("\"", "").replace("=", ""))
            .filter(isNotBlank()),
        Optional.of(record.get(ISBN_13))
            .map(isbn -> isbn.replace("\"", "").replace("=", ""))
            .filter(isNotBlank()),
        Integer.parseInt(record.get(MY_RATING)),
        Double.parseDouble(record.get(AVERAGE_RATING)),
        record.get(PUBLISHER),
        record.get(BINDING),
        Optional.of(record.get(NUMBER_OF_PAGES)).filter(isNotBlank()).map(Integer::parseInt),
        Optional.of(record.get(YEAR_PUBLISHED)).filter(isNotBlank()).map(Integer::parseInt),
        Optional.of(record.get(ORIGINAL_PUBLICATION)).filter(isNotBlank()).map(Integer::parseInt),
        Optional.of(record.get(DATE_READ))
            .filter(isNotBlank())
            .map(s -> s.replace("/", "-"))
            .map(LocalDate::parse),
        Optional.of(record.get(DATE_ADDED))
            .filter(isNotBlank())
            .map(s -> s.replace("/", "-"))
            .map(LocalDate::parse));
  }

  private static Predicate<String> isNotBlank() {
    return s -> !s.isBlank();
  }
}
