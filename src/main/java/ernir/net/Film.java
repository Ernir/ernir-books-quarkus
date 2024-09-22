package ernir.net;

import java.time.LocalDate;

public record Film(String title, LocalDate releaseDate, Integer episodeID, String director)
    implements SearchResult {}
