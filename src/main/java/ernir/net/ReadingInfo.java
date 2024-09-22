package ernir.net;

import java.time.LocalDate;
import java.util.Optional;

public record ReadingInfo(Optional<Integer> rating, LocalDate dateRead, LocalDate dateAdded) {}
