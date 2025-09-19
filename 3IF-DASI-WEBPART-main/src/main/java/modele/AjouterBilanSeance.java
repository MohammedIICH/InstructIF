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
public class AjouterBilanSeance extends Action {
    public AjouterBilanSeance(Service service) {
        super(service);
    }
    
    @Override
    public void executer(HttpServletRequest request) {
        HttpSession session = request.getSession();

        String bilanSeance = request.getParameter("bilan");
        Soutien soutien = service.obtenirDetailsSoutien(Long.parseLong(request.getParameter("idSoutien")));
        Intervenant intervenant = service.authentifierIntervenant((String) session.getAttribute("mailUser"));
        
        Soutien res = service.saisirBilanSoutien(soutien, intervenant, bilanSeance);
        
        if (res != null) {
            request.setAttribute("success", true);
        }
        else {
            request.setAttribute("success", false);
        }
    }
}
