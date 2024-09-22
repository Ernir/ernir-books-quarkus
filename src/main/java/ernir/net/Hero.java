package ernir.net;

import java.util.ArrayList;
import java.util.List;

public class Hero implements Character {

  public String name;
  public String surname;
  public Double height;
  public Integer mass;
  public Boolean darkSide;
  public LightSaber lightSaber;
  public List<Integer> episodeIds = new ArrayList<>();

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getSurname() {
    return surname;
  }
}
