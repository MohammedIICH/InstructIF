/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import metier.modele.Matiere;
import metier.service.Service;

/**
 *
 * @author mdeoliveir
 */
public class ConsulterListeMatiereAction extends Action {
    public ConsulterListeMatiereAction(Service service) {
        super(service);
    }
    
    @Override
    public void executer(HttpServletRequest request) {
        List<Matiere> listeMatieres = service.listerMatiere();
        for (Matiere mat : listeMatieres) {
            System.out.println(mat);
        }
        
        request.setAttribute("listeMatieres", listeMatieres);
    }
}
