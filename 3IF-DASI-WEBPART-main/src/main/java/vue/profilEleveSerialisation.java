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
import metier.modele.Soutien;

/**
 *
 * @author mdeoliveir
 */
public class profilEleveSerialisation extends Serialisation {

    @Override
    public void appliquer(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json;charset=UTF-8");
        
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException ex) {
            Logger.getLogger(profilEleveSerialisation.class.getName()).log(Level.SEVERE, null, ex);
        }
        Eleve eleve = (Eleve) request.getAttribute("eleve");
        
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject jsonContainer = new JsonObject();
        
        JsonArray jsonEleve = new JsonArray();
        JsonObject jsonInfos = new JsonObject();
        jsonInfos.addProperty("id", eleve.getId());
        jsonInfos.addProperty("datenaissance", String.valueOf(eleve.getDateNaissance()));
        jsonInfos.addProperty("codeEtablissement", eleve.getEtablissement().getUai());
        jsonInfos.addProperty("classe", eleve.getClasse());
        jsonInfos.addProperty("mail", eleve.getMail());
        jsonInfos.addProperty("nomEtablissement", eleve.getEtablissement().getNom());
        jsonInfos.addProperty("nom", eleve.getNom());
        jsonInfos.addProperty("prenom", eleve.getPrenom());
        jsonEleve.add(jsonInfos);
        
        jsonContainer.add("infosEleve", jsonEleve);
        
        JsonObject jsonResultat = new JsonObject();
        
        jsonResultat.add("container", jsonContainer);
        
        System.out.println(gson.toJson(jsonContainer));
        
        out.println(jsonContainer);
    }
}
