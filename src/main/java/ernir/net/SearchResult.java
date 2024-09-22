package ernir.net;

import io.smallrye.graphql.api.Union;

@Union
public sealed interface SearchResult permits Film, Character {}
