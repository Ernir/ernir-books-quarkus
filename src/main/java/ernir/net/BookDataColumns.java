package ernir.net;

public enum BookDataColumns {
  ID("Book Id"),
  TITLE("Title"),
  AUTHOR("Author"),
  AUTHOR_LAST_FIRST("Author l-f"),
  ADDITIONAL_AUTHORS("Additional Authors"),
  ISBN("ISBN"),
  ISBN_13("ISBN13"),
  MY_RATING("My Rating"),
  AVERAGE_RATING("Average Rating"),
  PUBLISHER("Publisher"),
  BINDING("Binding"),
  NUMBER_OF_PAGES("Number of Pages"),
  YEAR_PUBLISHED("Year Published"),
  ORIGINAL_PUBLICATION("Original Publication Year"),
  DATE_READ("Date Read"),
  DATE_ADDED("Date Added"),
  BOOKSHELVES("Bookshelves"),
  BOOKSHELVES_WITH_POSITIONS("Bookshelves with positions"),
  EXCLUSIVE_SHELF("Exclusive Shelf"),
  MY_REVIEW("My Review"),
  SPOILER("Spoiler"),
  PRIVATE_NOTES("Private Notes"),
  READ_COUNT("Read Count"),
  OWNED("Owned Copies");

  private final String columnName;

  @Override
  public String toString() {
    return columnName;
  }

  BookDataColumns(String columnName) {
    this.columnName = columnName;
  }
}
