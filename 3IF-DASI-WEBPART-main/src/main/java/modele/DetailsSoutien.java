/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import metier.modele.Matiere;
import metier.modele.Soutien;
import metier.service.Service;

/**
 *
 * @author mdeoliveir
 */
public class DetailsSoutien extends Action {
    public DetailsSoutien(Service service) {
        super(service);
    }
    
    @Override
    public void executer(HttpServletRequest request) {
        Soutien soutien = service.obtenirDetailsSoutien(Long.parseLong(request.getParameter("idSoutien")));
        
        request.setAttribute("soutien", soutien);
        request.setAttribute("lien", service.getLienVisio(soutien));
    }
}
