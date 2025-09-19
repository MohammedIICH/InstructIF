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
public class ObtenirTypeUser extends Action {

    public ObtenirTypeUser(Service service) {
        super(service);
    }

    @Override
    public void executer(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String typeUser = (String) session.getAttribute("typeUser");
        
        System.out.println(typeUser);
        
        request.setAttribute("typeUser", typeUser);
    }
    
}
