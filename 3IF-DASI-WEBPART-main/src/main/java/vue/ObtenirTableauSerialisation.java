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
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import metier.modele.Matiere;

/**
*
* @author mdeoliveir
*/
public class ObtenirTableauSerialisation extends Serialisation {

   @Override
   public void appliquer(HttpServletRequest request, HttpServletResponse response) {
       response.setContentType("application/json;charset=UTF-8");
       
       PrintWriter out = null;
       try {
           out = response.getWriter();
       } catch (IOException ex) {
           Logger.getLogger(ObtenirTableauSerialisation.class.getName()).log(Level.SEVERE, null, ex);
       }
       int[] repartClasse = (int[]) request.getAttribute("repartClasse");
       int dureeMoy = (int) request.getAttribute("dureeMoy");
       int nbCours = (int) request.getAttribute("nbCours");
       float progMoy = (float) request.getAttribute("progMoy");
       Map<String, Integer> repartGeo = (Map<String, Integer>) request.getAttribute("repartGeo");
       int[] repartHor = (int[]) request.getAttribute("repartHor");
       
       Gson gson = new GsonBuilder().setPrettyPrinting().create();
       JsonObject jsonContainer = new JsonObject();
       
       JsonArray jsonRepartClasse = new JsonArray();
       for (int i = 0; i < repartClasse.length; i++) {
           jsonRepartClasse.add(repartClasse[i]);
       }
       jsonContainer.add("repartClasse", jsonRepartClasse);

       jsonContainer.addProperty("dureeMoy", dureeMoy);

       jsonContainer.addProperty("nbCours", nbCours);

        jsonContainer.addProperty("progMoy", progMoy);

        JsonObject jsonRepartGeo = new JsonObject();
         for (Map.Entry<String, Integer> entry : repartGeo.entrySet()) {
              jsonRepartGeo.addProperty(entry.getKey(), entry.getValue());
            }
        jsonContainer.add("repartGeo", jsonRepartGeo);

        JsonArray jsonRepartHor = new JsonArray();
        for (int i = 0; i < repartHor.length; i++) {
            jsonRepartHor.add(repartHor[i]);
        }
        jsonContainer.add("repartHor", jsonRepartHor);
       
       JsonObject jsonResultat = new JsonObject();
       
       jsonResultat.add("container", jsonContainer);
       
       System.out.println(gson.toJson(jsonContainer));
       
       out.println(jsonContainer);
   }
}
