/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import metier.modele.Eleve;
import metier.service.Service;

/**
 *
 * @author mdeoliveir
 */
public class AuthentifierEleveAction extends Action {

    public AuthentifierEleveAction(Service service) {
        super(service);
    }

    @Override
    public void executer(HttpServletRequest request) {
        Eleve eleve = service.authentifierEleve(request.getParameter("mail"), request.getParameter("password"));
        
        System.out.println(eleve);

        if (eleve != null) {
            HttpSession session = request.getSession();
            session.setAttribute("mailUser", request.getParameter("mail"));
            session.setAttribute("typeUser", "eleve");
        }
        
        request.setAttribute("eleve", eleve);
    }
    
}
