package tikape.minifoorumi;
import tikape.minifoorumi.domain.Viesti;
import tikape.minifoorumi.domain.Viestiketju;
import tikape.minifoorumi.database.Database;
import tikape.minifoorumi.dao.ViestiDao;
import tikape.minifoorumi.dao.ViestiketjuDao;
import spark.Spark;
import java.util.HashMap;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

public class MinifoorumiApplication {

    public static void main(String[] args) throws Exception {
        Database database = new Database("jdbc:sqlite:forum.db");
        ViestiketjuDao viestiketjut = new ViestiketjuDao(database, "Viestiketju");
        ViestiDao viestit = new ViestiDao(database, "Viesti");
        
        Spark.get("/messages", (req, res) -> {
            // hae viestit
            //Integer viestiketjuId = Integer.parseInt(req.params(":id"));
            HashMap map = new HashMap<>();
            map.put("viestit", viestit.findAll());
            map.put("viestiketjut", viestiketjut.findAll());
            return new ModelAndView(map, "messages");
        }, new ThymeleafTemplateEngine());
        
        Spark.post("/messages/:id", (req, res) -> {
                java.util.Date utilAika = new java.util.Date();
                java.sql.Date sqlAika = new java.sql.Date( utilAika.getTime() );
                Integer viestiketjuId = Integer.parseInt(req.queryParams("viestiketju_id"));
                
                Viesti viesti = new Viesti(null, viestiketjuId, req.queryParams("name"), req.queryParams("content"), sqlAika);
                viestit.save(viesti);
                return "";
        });
        
        Spark.post("/messages", (req, res) -> {
                java.util.Date utilAika = new java.util.Date();
                java.sql.Date sqlAika = new java.sql.Date( utilAika.getTime() );
                Viestiketju viestiketju = new Viestiketju(null, req.queryParams("topic"));
                viestiketjut.save(viestiketju);
                // todo: vaihda tähän uuden ketjun id
                Viesti viesti = new Viesti(null, 1, req.queryParams("name"), req.queryParams("content"), sqlAika);
                viestit.save(viesti);
                //TODO: res.redirect("/messages/" + req.params(":id"));
                res.redirect("/messages");
                return "";
        });
    }
}
