package ernir.net;

import io.smallrye.graphql.api.Context;
import io.smallrye.graphql.api.Subscription;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.operators.multi.processors.BroadcastProcessor;
import jakarta.inject.Inject;
import org.eclipse.microprofile.graphql.DefaultValue;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Name;
import org.eclipse.microprofile.graphql.Query;
import org.eclipse.microprofile.graphql.Source;

import java.util.List;

@GraphQLApi
public class FilmResource {

  @Inject GalaxyService service;

  @Query("allFilms")
  @Description("Get all Films from a galaxy far far away")
  public List<Film> getAllFilms() {
    return service.getAllFilms();
  }

  @Query
  @Description("Get a Films from a galaxy far far away")
  public Film getFilm(Context context, @Name("filmId") int id) {
    System.out.println(context);
    return service.getFilm(id);
  }

  public List<Hero> heroes(@Source Film film) {
    return service.getHeroesByFilm(film);
  }

  @Query
  public List<Ally> allies() {
    return service.getAllAllies();
  }

  @Query
  @Description("Get all characters from a galaxy far far away")
  public List<Character> characters() {
    return service.getAllCharacters();
  }

  @Query
  @Description("Search for heroes or films")
  public List<SearchResult> search(String query) {
    return service.search(query);
  }

  @Mutation
  public Hero deleteHero(int id) {
    return service.deleteHero(id);
  }

  @Query
  public List<Hero> getHeroesWithSurname(@DefaultValue("Skywalker") String surname) {
    return service.getHeroesBySurname(surname);
  }
}
