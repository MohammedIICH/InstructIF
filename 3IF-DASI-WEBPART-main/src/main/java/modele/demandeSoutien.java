/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import metier.modele.Eleve;
import metier.modele.Matiere;
import metier.modele.Soutien;
import metier.service.Service;

/**
 *
 * @author mdeoliveir
 */
public class demandeSoutien extends Action {

    public demandeSoutien(Service service) {
        super(service);
    }

    @Override
    public void executer(HttpServletRequest request) {
        HttpSession session = request.getSession();

        List<Matiere> lMat = service.listerMatiere();
        String matRecherche = request.getParameter("matiere");
        Matiere matiere = new Matiere();
        for (Matiere mat : lMat) {
            if (mat.getNom().equals(matRecherche)) {
                matiere = mat;
            }
        }
        System.out.println(matiere);
        
        System.out.println(request.getParameter("heureDebut"));
        
        Eleve eleve = service.obtenirDetailsEleve((String) session.getAttribute("mailUser"));
        String description = request.getParameter("description");
        int heure = Integer.parseInt(request.getParameter("heureDebut").split(":")[0]);
        int minute = Integer.parseInt(request.getParameter("heureDebut").split(":")[1]);
        
        Date today = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), heure, minute);
        
        Soutien soutien = service.creerSoutien(eleve, today, matiere.getId(), description);
        
        System.out.println(soutien);
        
        request.setAttribute("soutien", soutien);
    }
    
}
