package ernir.net;

import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static java.util.stream.Collectors.toList;

@ApplicationScoped
public class GalaxyService {

  private final List<Hero> heroes = new ArrayList<>();

  private final List<Film> films = new ArrayList<>();

  private final List<Ally> allies = new ArrayList<>();

  public GalaxyService() {

    Film aNewHope = new Film("A New Hope", LocalDate.of(1977, Month.MAY, 25), 4, "George Lucas");
    Film theEmpireStrikesBack =
        new Film("The Empire Strikes Back", LocalDate.of(1980, Month.MAY, 21), 5, "George Lucas");

    Film returnOfTheJedi =
        new Film("Return Of The Jedi", LocalDate.of(1983, Month.MAY, 25), 6, "George Lucas");

    films.add(aNewHope);
    films.add(theEmpireStrikesBack);
    films.add(returnOfTheJedi);

    Hero luke = new Hero();
    luke.name = "Luke";
    luke.surname = "Skywalker";
    luke.height = 1.7;
    luke.mass = 73;
    luke.lightSaber = LightSaber.GREEN;
    luke.darkSide = false;
    luke.episodeIds.addAll(Arrays.asList(4, 5, 6));

    Hero leia = new Hero();
    leia.name = "Leia";
    leia.surname = "Organa";
    leia.height = 1.5;
    leia.mass = 51;
    leia.darkSide = false;
    leia.episodeIds.addAll(Arrays.asList(4, 5, 6));

    Hero vader = new Hero();
    vader.name = "Darth";
    vader.surname = "Vader";
    vader.height = 1.9;
    vader.mass = 89;
    vader.darkSide = true;
    vader.lightSaber = LightSaber.RED;
    vader.episodeIds.addAll(Arrays.asList(4, 5, 6));

    heroes.add(luke);
    heroes.add(leia);
    heroes.add(vader);

    Ally jarjar = new Ally();
    jarjar.name = "Jar Jar";
    jarjar.surname = "Binks";
    allies.add(jarjar);
  }

  public List<Film> getAllFilms() {
    System.out.printf("Returning %s films%n", films.size());
    return films;
  }

  public Film getFilm(int id) {
    return films.get(id);
  }

  public List<Hero> getHeroesByFilm(Film film) {
    return heroes.stream()
        .filter(hero -> hero.episodeIds.contains(film.episodeID()))
        .collect(toList());
  }

  public void addHero(Hero hero) {
    heroes.add(hero);
  }

  public Hero deleteHero(int id) {
    return heroes.remove(id);
  }

  public List<Hero> getHeroesBySurname(String surname) {
    return heroes.stream().filter(hero -> hero.surname.equals(surname)).collect(toList());
  }

  public List<Ally> getAllAllies() {
    return allies;
  }

  public List<Character> getAllCharacters() {
    List<Character> characters = new ArrayList<>();
    characters.addAll(heroes);
    characters.addAll(allies);
    return characters;
  }

  public List<SearchResult> search(String query) {
    List<Film> matchingFilms =
        films.stream()
            .filter(film -> film.title().contains(query) || film.director().contains(query))
            .toList();
    List<SearchResult> results = new ArrayList<>(matchingFilms);
    List<Character> matchingCharacters =
        getAllCharacters().stream()
            .filter(
                character ->
                    character.getName().contains(query) || character.getSurname().contains(query))
            .toList();
    results.addAll(matchingCharacters);
    return results;
  }
}
