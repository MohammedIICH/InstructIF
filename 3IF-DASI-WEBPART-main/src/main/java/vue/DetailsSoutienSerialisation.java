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
public class DetailsSoutienSerialisation extends Serialisation {

    @Override
    public void appliquer(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json;charset=UTF-8");
        
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException ex) {
            Logger.getLogger(DetailsSoutienSerialisation.class.getName()).log(Level.SEVERE, null, ex);
        }
        Soutien soutien = (Soutien)request.getAttribute("soutien");
        
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject jsonContainer = new JsonObject();
        
        JsonArray jsonSoutien = new JsonArray();
        JsonObject jsonInfos = new JsonObject();
        jsonInfos.addProperty("id", soutien.getId());
        jsonInfos.addProperty("dateDemande", String.valueOf(soutien.getDateDemande()));
        jsonInfos.addProperty("dateDebut", String.valueOf(soutien.getDateDebut()));
        jsonInfos.addProperty("dateFin", String.valueOf(soutien.getDateFin()));
        jsonInfos.addProperty("description", soutien.getDescription());
        jsonInfos.addProperty("bilan", soutien.getBilan());
        jsonInfos.addProperty("niveauComprehension", soutien.getNiveauComprehension());
        jsonInfos.addProperty("matiere", String.valueOf(soutien.getMatiere().getNom()));
        jsonInfos.addProperty("eleve", String.valueOf(soutien.getEleve().getPrenom()));
        jsonInfos.addProperty("intervenant", String.valueOf(soutien.getIntervenant().getPrenom()));
        jsonInfos.addProperty("lien", (String) request.getAttribute("lien"));
        jsonSoutien.add(jsonInfos);
        
        jsonContainer.add("infosSoutien", jsonSoutien);
        
        JsonObject jsonResultat = new JsonObject();
        
        jsonResultat.add("container", jsonContainer);
        
        System.out.println(gson.toJson(jsonContainer));
        
        out.println(jsonContainer);
    }
}
