/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier.service;

import com.google.maps.model.LatLng;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import metier.modele.Etablissement;
import util.EducNetApi;
import util.GeoNetApi;

/**
 *
 * @author ehequet
 */
public class Outil {
    
    /**
     * Récupère les infos relatives à l'uai de l'établissement passé en paramètre et créé un objet Etablissement.
     * Lance une erreur si un tel établissement n'existe pas.
     * @param uaiEtablissement
     * @return
     * @throws Exception 
     */
    public static Etablissement trouverInfosEtablissement(String uaiEtablissement) throws Exception {
        EducNetApi api = new EducNetApi();
        List<String> result = api.getInformationEtablissement(uaiEtablissement);
        if (result == null) {
            // etablissement pas trouvé dans l'api
            throw new Exception();
        }
        String uai = result.get(0);
        String nom = result.get(1);
        String secteur = result.get(2);
        String codeCommune = result.get(3);
        String nomCommune = result.get(4);
        String codeDepartement = result.get(5);
        String nomDepartement = result.get(6);
        String academie = result.get(7);
        String ips = result.get(8);
                
        // On récupère les coordonnées grâce à l'api
        String adresseEtablissement = nom + ", " + nomCommune;
        LatLng coords = GeoNetApi.getLatLng(adresseEtablissement);
        
        return new Etablissement(coords.lat, coords.lng, uai, nom,secteur, codeCommune, nomCommune, codeDepartement, nomDepartement, academie, ips);                    
    }
    
    /**
     * Formate une date en HH'h'MM
     * @param dateFormat
     * @return String formaté
     */
    public static String formatDateHeure(Date dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH'h'mm");
        return sdf.format(dateFormat);
    }
}
