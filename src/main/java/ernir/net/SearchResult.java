package ernir.net;

import io.smallrye.graphql.api.Union;

@Union
public sealed interface SearchResult permits Book, Author, Publisher {}
