/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controleur;

import dao.JpaUtil;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import metier.service.Service;
import modele.AjouterBilanSeance;
import modele.AuthentifierEleveAction;
import modele.ConsulterListeMatiereAction;
import modele.Deconnexion;
import modele.DetailsSoutien;
import modele.EvaluerCours;
import modele.LoginIntervenant;
import modele.ObtenirTableau;
import modele.ObtenirTypeUser;
import modele.SignUpEleve;
import modele.demandeSoutien;
import modele.obtenirHistoriqueEleve;
import modele.obtenirHistoriqueIntervenant;
import modele.profilEleve;
import modele.profilIntervenant;
import vue.AjouterBilanSeanceSerialisation;
import vue.ListeMatiereSerialisation;
import vue.AuthentifierEleveSerialisation;
import vue.DeconnexionSerialisation;
import vue.DetailsSoutienSerialisation;
import vue.EvaluerCoursSerialisation;
import vue.LoginIntervenantSerialisation;
import vue.ObtenirTableauSerialisation;
import vue.ObtenirTypeUserSerialisation;
import vue.SignUpEleveSerialisation;
import vue.demandeSoutienSerialisation;
import vue.obtenirHistoriqueEleveSerialisation;
import vue.obtenirHistoriqueIntervenantSerialisation;
import vue.profilEleveSerialisation;
import vue.profilIntervenantSerialisation;

/**
 *
 * @author mdeoliveir
 */
@WebServlet(name = "ActionServlet", urlPatterns = {"/ActionServlet"})
public class ActionServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("todo");
        
        System.out.println("Trace : todo " + action);
        switch(action) {
            case "consulter-liste-matieres" : {
                Service service = new Service();
                new ConsulterListeMatiereAction(service).executer(request);
                new ListeMatiereSerialisation().appliquer(request, response);
                break;
            }
            
            case "authentifier-eleve" : {
                Service service = new Service();
                new AuthentifierEleveAction(service).executer(request);
                new AuthentifierEleveSerialisation().appliquer(request, response);
                break;
            }
            
            case "sign-up-eleve" : {
                Service service = new Service();
                new SignUpEleve(service).executer(request);
                new SignUpEleveSerialisation().appliquer(request, response);
                break;
            }
            
            case "evaluer-cours" : {
                Service service = new Service();
                new EvaluerCours(service).executer(request);
                new EvaluerCoursSerialisation().appliquer(request, response);
                break;
            }
            
            case "obtenir-historique-eleve" : {
                Service service = new Service();
                new obtenirHistoriqueEleve(service).executer(request);
                new obtenirHistoriqueEleveSerialisation().appliquer(request, response);
                break;
            }
            
            case "demande-soutien" : {
                Service service = new Service();
                new demandeSoutien(service).executer(request);
                new demandeSoutienSerialisation().appliquer(request, response);
                break;
            }
            
            case "demande-profil-eleve" : {
                Service service = new Service();
                new profilEleve(service).executer(request);
                new profilEleveSerialisation().appliquer(request, response);
                break;
            }
            
            case "login-intervenant" : {
                Service service = new Service();
                new LoginIntervenant(service).executer(request);
                new LoginIntervenantSerialisation().appliquer(request, response);
                break;
            }

            case "obtenir-statistiques" : {
                Service service = new Service();
                new ObtenirTableau(service).executer(request);
                new ObtenirTableauSerialisation().appliquer(request, response);
                break;
            }

            case "ajouter-bilan-seance" : {
                Service service = new Service();
                new AjouterBilanSeance(service).executer(request);
                new AjouterBilanSeanceSerialisation().appliquer(request, response);
                break;
            }

            case "obtenir-profil-intervenant" : {
                Service service = new Service();
                new profilIntervenant(service).executer(request);
                new profilIntervenantSerialisation().appliquer(request, response);
                break;
            }

            case "obtenir-historique-intervenant" : {
                Service service = new Service();
                new obtenirHistoriqueIntervenant(service).executer(request);
                new obtenirHistoriqueIntervenantSerialisation().appliquer(request, response);
                break;
            }

            case "obtenir-details-soutien" : {
                Service service = new Service();
                new DetailsSoutien(service).executer(request);
                new DetailsSoutienSerialisation().appliquer(request, response);
                break;
            }

            case "obtenir-type-user" : {
                Service service = new Service();
                new ObtenirTypeUser(service).executer(request);
                new ObtenirTypeUserSerialisation().appliquer(request, response);
                break;
            }

            case "deconnexion" : {
                Service service = new Service();
                new Deconnexion(service).executer(request);
                new DeconnexionSerialisation().appliquer(request, response);
                break;
            }

        }
    }
    
    @Override
    public void init() throws ServletException {
        super.init();
        JpaUtil.creerFabriquePersistance();
        // Service service = new Service();
        // service.creerIntervenants();
        // service.creerMatieres();
    }
    
    @Override
    public void destroy() {
        JpaUtil.fermerFabriquePersistance();
        super.destroy();
    }
}
