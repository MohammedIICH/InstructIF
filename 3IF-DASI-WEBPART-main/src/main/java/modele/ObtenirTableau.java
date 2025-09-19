/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import metier.modele.Intervenant;
import metier.modele.Matiere;
import metier.modele.Soutien;
import metier.service.Service;

/**
 *
 * @author mdeoliveir
 */
public class ObtenirTableau extends Action {
    public ObtenirTableau(Service service) {
        super(service);
    }
    
    @Override
    public void executer(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Intervenant intervenant = service.authentifierIntervenant((String) session.getAttribute("mailUser"));
        
        int[] repartClasse = service.obtenirRepartitionClasses(intervenant);
        int dureeMoy = service.obtenirDureeMoyenne(intervenant);
        int nbCours = intervenant.getNbSoutiens();
        float progMoy = service.obtenirComprehensionMoyenne(intervenant);
        Map<String, Integer> repartGeo = service.obtenirRepartitionGeo(intervenant);
        int[] repartHor = service.obtenirRepartitionHoraire(intervenant);
        
        request.setAttribute("repartClasse", repartClasse);
        request.setAttribute("dureeMoy", dureeMoy);
        request.setAttribute("nbCours", nbCours);
        request.setAttribute("progMoy", progMoy);
        request.setAttribute("repartGeo", repartGeo);
        request.setAttribute("dureeMoy", repartHor);
    }
}
