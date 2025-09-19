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
public class Deconnexion extends Action {

    public Deconnexion(Service service) {
        super(service);
    }

    @Override
    public void executer(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Boolean success = true;

        try {
            // Invalidate the session to log out the user
            session.invalidate();
        } catch (Exception e) {
            success = false;
            System.out.println("Error during logout: " + e.getMessage());
        }
        
        System.out.println(success ? "Logout successful" : "Logout failed");
        
        request.setAttribute("success", success);
    }
    
}
