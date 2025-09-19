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
import metier.modele.Matiere;
import metier.modele.Soutien;
import metier.service.Service;

/**
 *
 * @author mdeoliveir
 */
public class profilEleve extends Action {
    public profilEleve(Service service) {
        super(service);
    }
    
    @Override
    public void executer(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String studentMail = null;
        Eleve eleve = null;
        // Assuming the session contains the email of the student
        if (session.getAttribute("typeUser") == "intervenant") {
            studentMail = request.getParameter("mailEleve");
            eleve = service.obtenirDetailsEleve(studentMail);
        }
        else {
            eleve = service.obtenirDetailsEleve((String) session.getAttribute("mailUser"));
        }
        request.setAttribute("eleve", eleve);
    }
}
