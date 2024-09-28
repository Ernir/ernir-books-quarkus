package ernir.net.books.models;

import java.util.Optional;

public record SeriesMembership(Series series, Optional<String> position) {}
