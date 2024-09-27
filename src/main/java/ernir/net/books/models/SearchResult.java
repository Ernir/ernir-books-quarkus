package ernir.net.books.models;

import io.smallrye.graphql.api.Union;

@Union
public sealed interface SearchResult permits Book, Author, Publisher {}
