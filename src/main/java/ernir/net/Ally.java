package ernir.net;

public class Ally implements Character {

  public String name;
  public String surname;
  public Hero partner;

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getSurname() {
    return surname;
  }
}
