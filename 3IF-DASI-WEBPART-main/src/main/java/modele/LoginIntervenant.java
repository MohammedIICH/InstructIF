/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import metier.modele.Eleve;
import metier.modele.Intervenant;
import metier.service.Service;

/**
 *
 * @author mdeoliveir
 */
public class LoginIntervenant extends Action {

    public LoginIntervenant(Service service) {
        super(service);
    }

    @Override
    public void executer(HttpServletRequest request) {
        Intervenant intervenant = service.authentifierIntervenant(request.getParameter("identifiant"));
        
        System.out.println(intervenant);

        if (intervenant != null) {
            HttpSession session = request.getSession();
            session.setAttribute("mailUser", request.getParameter("identifiant"));
            session.setAttribute("typeUser", "intervenant");
        }
        
        request.setAttribute("intervenant", intervenant);
    }
    
}
