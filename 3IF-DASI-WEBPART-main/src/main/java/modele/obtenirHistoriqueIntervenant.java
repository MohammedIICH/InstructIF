/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import metier.modele.Eleve;
import metier.modele.Intervenant;
import metier.modele.Matiere;
import metier.modele.Soutien;
import metier.service.Service;

/**
 *
 * @author mdeoliveir
 */
public class obtenirHistoriqueIntervenant extends Action {
    public obtenirHistoriqueIntervenant(Service service) {
        super(service);
    }
    
    @Override
    public void executer(HttpServletRequest request) {
        HttpSession session = request.getSession();
        // Assuming the session contains the email of the student
        Intervenant intervenant = service.authentifierIntervenant((String) session.getAttribute("mailUser"));
        String sortingType = request.getParameter("sortingType");
        List<Soutien> listeSoutiens = service.getHistoriqueSoutiensIntervenant(intervenant, sortingType);
        
        request.setAttribute("listeSoutien", listeSoutiens);
    }
}
