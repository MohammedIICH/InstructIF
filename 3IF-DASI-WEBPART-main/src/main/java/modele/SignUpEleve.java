/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import metier.modele.Eleve;
import metier.modele.Matiere;
import metier.service.Service;

/**
 *
 * @author mdeoliveir
 */
public class SignUpEleve extends Action {
    public SignUpEleve(Service service) {
        super(service);
    }
    
    @Override
    public void executer(HttpServletRequest request) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date dateNaissance = null;
        try {
            dateNaissance = sdf.parse(request.getParameter("dateNaissance"));
        } catch (ParseException ex) {
            Logger.getLogger(SignUpEleve.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Eleve eleve = new Eleve(
                request.getParameter("mail"), 
                Integer.parseInt(request.getParameter("classe")), 
                dateNaissance,
                request.getParameter("password"), 
                request.getParameter("nom"), 
                request.getParameter("prenom")
        );
        String uaiEtablissement = request.getParameter("uaiEtablissement");
        Boolean success = service.inscrireEleve(eleve, uaiEtablissement);
        
        request.setAttribute("success", success);
    }
}
