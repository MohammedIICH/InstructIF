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
public class profilIntervenant extends Action {
    public profilIntervenant(Service service) {
        super(service);
    }
    
    @Override
    public void executer(HttpServletRequest request) {
        HttpSession session = request.getSession();
        // Assuming the session contains the email of the intervenant
        Intervenant intervenant = service.authentifierIntervenant((String) session.getAttribute("mailUser"));
        request.setAttribute("intervenant", intervenant);
    }
}
