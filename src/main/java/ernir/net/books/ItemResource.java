package ernir.net.books;

import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.math.BigDecimal;

@Path("item")
public class ItemResource {
  public record Item(String name, BigDecimal price) {}

  @CheckedTemplate
  public static class Templates {
    public static native TemplateInstance item(Item item);
  }

  @GET
  @Path("{id}")
  @Produces(MediaType.TEXT_HTML)
  public TemplateInstance get(@PathParam("id") Integer id) {
    return Templates.item(new Item("Fancy item", BigDecimal.ONE));
  }
}
