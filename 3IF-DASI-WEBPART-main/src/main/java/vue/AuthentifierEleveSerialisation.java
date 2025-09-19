/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import metier.modele.Eleve;
import metier.modele.Matiere;

/**
 *
 * @author mdeoliveir
 */
public class AuthentifierEleveSerialisation extends Serialisation {

    @Override
    public void appliquer(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json;charset=UTF-8");
        
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException ex) {
            Logger.getLogger(AuthentifierEleveSerialisation.class.getName()).log(Level.SEVERE, null, ex);
        }
        Eleve eleve = (Eleve)request.getAttribute("eleve");
        
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject jsonContainer = new JsonObject();
        
        JsonArray jsonAuth = new JsonArray();
        
        JsonObject jsonState = new JsonObject();
        if (eleve == null) {
            jsonState.addProperty("isAuth", false);
        } else {
            jsonState.addProperty("isAuth", true);
        }
        jsonAuth.add(jsonState);
        
        jsonContainer.add("auth", jsonAuth);
        
        JsonObject jsonResultat = new JsonObject();
        
        jsonResultat.add("container", jsonContainer);
        
        System.out.println(gson.toJson(jsonContainer));
        
        out.println(jsonContainer);
    }
}
