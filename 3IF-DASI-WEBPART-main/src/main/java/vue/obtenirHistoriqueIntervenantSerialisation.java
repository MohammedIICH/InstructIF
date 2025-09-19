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
import metier.modele.Matiere;
import metier.modele.Soutien;

/**
 *
 * @author mdeoliveir
 */
public class obtenirHistoriqueIntervenantSerialisation extends Serialisation {

    @Override
    public void appliquer(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json;charset=UTF-8");
        
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException ex) {
            Logger.getLogger(obtenirHistoriqueEleveSerialisation.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<Soutien> listeSoutien = (List<Soutien>) request.getAttribute("listeSoutien");
        
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject jsonContainer = new JsonObject();
        
        JsonObject jsonListeSoutien = new JsonObject();
        for (Soutien s : listeSoutien) {
            System.out.println(s);
            JsonObject jsonSoutien = new JsonObject();
            jsonSoutien.addProperty("eleve", s.getEleve().getPrenom() + " " + s.getEleve().getNom());
            jsonSoutien.addProperty("matiere", s.getMatiere().getNom());
            jsonSoutien.addProperty("dateDebut", String.valueOf(s.getDateDebut()));
            jsonSoutien.addProperty("idSoutien", s.getId());
            jsonListeSoutien.add(String.valueOf(s.getId()), jsonSoutien);
        }
        jsonContainer.add("listeSoutien", jsonListeSoutien);
        
        JsonObject jsonResultat = new JsonObject();
        
        jsonResultat.add("container", jsonContainer);
        
        System.out.println(gson.toJson(jsonContainer));
        
        out.println(jsonContainer);
    }
}
