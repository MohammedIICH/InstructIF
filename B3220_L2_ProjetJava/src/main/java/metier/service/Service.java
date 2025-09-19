/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier.service;

import dao.EleveDao;
import dao.EtablissementDao;
import dao.IntervenantDao;
import dao.JpaUtil;
import dao.MatiereDao;
import dao.SoutienDao;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import metier.modele.Autre;
import metier.modele.Eleve;
import metier.modele.Enseignant;
import metier.modele.Etablissement;
import metier.modele.Etudiant;
import metier.modele.Intervenant;
import metier.modele.Matiere;
import metier.modele.Soutien;
import util.Message;

/**
 *
 * @author ehequet
 */
public class Service {
       
    /**
     * Renvoie les infos relatives à un établissement grâce à son UAI
     * @param uaiEtablissement
     * @return
     */
    public Etablissement getInfosEtablissementUai(String uaiEtablissement) {
        Etablissement etablissement = null;
        EtablissementDao etablissementDao = new EtablissementDao();
        try {
            JpaUtil.creerContextePersistance();
            etablissement = etablissementDao.findByUai(uaiEtablissement);
            
            if (etablissement == null) {
                etablissement = Outil.trouverInfosEtablissement(uaiEtablissement);
            }
        } catch (Exception ex) {
            etablissement = null;
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        
        return etablissement;
    }
    
    /**
     * Permet d'inscrire un nouvel eleve
     * Vérifie que les attributs de l'eleve sont corrrects et récupère les infos 
     * de l'établissement associé à l'uai s'il existe.
     * @param eleve
     * @param uaiEtablissement
     * @return Boolean : true si l'inscription s'est bien passée, false sinon
     */
    public Boolean inscrireEleve(Eleve eleve, String uaiEtablissement) {
        EleveDao eleveDao = new EleveDao();
        String sujet = "Bienvenue sur le réseau ISTRUCT'IF";
        String corps = "Bonjour " + eleve.getPrenom() + ", nous te confirmons ton inscription sur le réseau INSTRUCT'IF." + 
            "Si tu as besoin d'un soutien pour tes leçons ou tes devoirs, rends-toi sur notre site pour une mise en relation avec un intervenant.";
        Boolean succes = true;
        
        try{
            JpaUtil.creerContextePersistance();
            JpaUtil.ouvrirTransaction();
            
            // Avant tout nous allons vérifier que l'eleve est correctement formé.
            // variables suivantes pourr éviter les magic numbers
            final int terminale = 0;
            final int sixieme = 6;
            
            if (eleve.getMail() == null || eleve.getMail().isEmpty() || eleve.getPrenom() == null || eleve.getPrenom().isEmpty() || 
                eleve.getNom() == null || eleve.getNom().isEmpty() || eleve.getDateNaissance() == null || eleve.getClasse() > sixieme || 
                eleve.getClasse() < terminale || eleve.getMotDePasse() == null || eleve.getMotDePasse().isEmpty()) 
            {
                throw new Exception("Paramètres invalides");
            }
            
            EtablissementDao etablissementDao = new EtablissementDao();

            Etablissement etablissement = etablissementDao.findByUai(uaiEtablissement);
            
            if (etablissement == null) {
                // Si etablissement est null alors il n'est pas dans la bdd : 
                // on tente de le récupérer par l'api mais s'il n'existe pas non plus une exception sera lancée
                etablissement = Outil.trouverInfosEtablissement(uaiEtablissement);
                etablissementDao.create(etablissement);
            }
            
            eleveDao.create(eleve);
            eleve.setEtablissement(etablissement);
            JpaUtil.validerTransaction();
        }
        catch (Exception  ex){
            ex.printStackTrace();
            JpaUtil.annulerTransaction();
            corps = "Bonjour " + eleve.getPrenom() + ", ton inscription sur le réseau INSTRUCT'IF a malencontreusement a échouée... Merci de recommencer ultérieurement.";
            sujet = "Echec de l'inscription sur le réseau ISTRUCT'IF";
            succes = false;
        }
        finally{
            JpaUtil.fermerContextePersistance();
            Message.envoyerMail("contact@instruct.if", eleve.getMail(), sujet, corps);
        }
        return succes;
    }
    
     /**
     * Authentifie la connexion d'un eleve grâce à son mail et son mdp donnés en paramètre
     * Récupération de l'eleve associé au mail, s'il existe on test la correspondance des mdps
     * @param mail
     * @param motDePasse
     * @return Eleve|null : l'instance de l'eleve si les infos sont correctes, nul sinon
     */
    public Eleve authentifierEleve(String mail, String motDePasse) 
    {
        EleveDao eleveDao = new EleveDao();
        Eleve res = null;
        
        try{
            JpaUtil.creerContextePersistance();
            res = eleveDao.findEleveByMail(mail);
            
            if (res == null || res.getMotDePasse() == null || ! res.getMotDePasse().equals(motDePasse)) {
                res = null;
            }
        }
        catch (Exception  ex){
            ex.printStackTrace();
            res = null;
        }
        finally{
            JpaUtil.fermerContextePersistance();
        }
        
        return res;
    }
    
    /**
     * Méthode permettant de modifier les infos relatives à un eleve
     * Le parametre modifications contientt les nouvelles infos à appliquer à l'instance eleve
     * Pour gérerr l'uai, il y a un troisieme paramètre que l'on va considérer uniquement s'il est différent
     * de l'uai de l'établissement actuel de l'eleve. Autrement inutile de mettre à jour.
     * @param eleve
     * @param modifications
     * @param nouvUaiEtablissement
     * @return
     */
    public Eleve modifierEleve(Eleve eleve, Eleve modifications, String nouvUaiEtablissement) {
        final int terminale = 0;
        final int sixieme = 6;
        Etablissement etablissement = null;
        EleveDao eleveDao = new EleveDao();
        Eleve res = eleve;
        
        try {
            JpaUtil.creerContextePersistance();
            JpaUtil.ouvrirTransaction();
            
            if (eleve.getMail() == null || eleve.getMail().isEmpty() || eleve.getPrenom() == null || eleve.getPrenom().isEmpty() || 
                eleve.getNom() == null || eleve.getNom().isEmpty() || eleve.getDateNaissance() == null || eleve.getClasse() > sixieme || 
                eleve.getClasse() < terminale || eleve.getMotDePasse() == null || eleve.getMotDePasse().isEmpty()) 
            {
                throw new Exception();
            }
            
            // On récupère le nouvel établissement si celui ci a changé
            if (nouvUaiEtablissement != null && !eleve.getEtablissement().getUai().equals(nouvUaiEtablissement)) {
                EtablissementDao etablissementDao = new EtablissementDao();
                etablissement = etablissementDao.findByUai(nouvUaiEtablissement);
            
                if (etablissement == null) {
                    etablissement = Outil.trouverInfosEtablissement(nouvUaiEtablissement);
                    etablissementDao.create(etablissement);
                }
            }
            
            // On applique les modifs
            eleve.setPrenom(modifications.getPrenom());
            eleve.setNom(modifications.getNom());
            eleve.setClasse(modifications.getClasse());
            eleve.setDateNaissance(modifications.getDateNaissance());
            eleve.setMotDePasse(modifications.getMotDePasse());
            eleve.setMail(modifications.getMail());
            eleve.setEtablissement(etablissement);
            
            res = eleveDao.update(eleve);
            JpaUtil.validerTransaction();
        } catch (Exception ex) {
            ex.printStackTrace();
            JpaUtil.annulerTransaction();
            res = null;
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        
        return res;
    }
    
    /**
     * Retourne les détails d'un eleve donné par son mail
     * 
     * @param mailEleve
     * @return
     */
    public Eleve obtenirDetailsEleve(String mailEleve) {
        EleveDao eleveDao = new EleveDao();
        Eleve res = null;
        try {
            JpaUtil.creerContextePersistance();
            res = eleveDao.findEleveByMail(mailEleve);
        } catch (Exception ex) {
            ex.printStackTrace();
            res = null;
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        
        return res;
    }
  
    /**
     * Génère des intervenants, choisi aléatoirement entre Autre, Enseignant et Etudiant
     * Les paramètres spécifiques des classes sont choisis aléatoirement parmi une liste fixée
     * @return Boolean : true s'il n'y a pas eu de problème, false sinon
     */
    public Boolean creerIntervenants() {
        IntervenantDao intervenantDao = new IntervenantDao();
        Boolean succes = true;
        Random rand = new Random();
        final String[][] intervenants = {
            {"0642049305", "FAVRO", "Samuel", "sfavro"},
            {"0671150503", "DONODIO GALVIS", "Florine", "florine.donodio-galvis"},
            {"0713200950", "DEKEW", "Simon", "sdekew4845"},
            {"0437340532", "LOU", "Flavien", "flavien.lou"},
            {"0719843316", "GUOGUEN", "Gabriela", "gguoguen2418"},
            {"0441564193", "HERNENDEZ", "Vincent", "vhernendez"},
            {"0647380386", "OBADII", "Yaelle", "yaelle.obadii"},
            {"0557745829", "CHONI-MENG-HIME", "Emmanuel", "echoni-meng-hime4744"},
            {"0298347465", "HUONG", "Adrian Alejandro", "adrian-alejandro.huong"},
            {"0755527086", "LITT", "Raphaël", "rlitt6371"}
        };
        
        // Tableau et variables en durs pour la classe Etudiant
        final String[] specialites = {"Mathématiques", "Français", "Histoire", "Géographie", "Anglais", "Physique", "Chimie", "Espagnol", "Allemand"};
        final int nbSpecialites = 9;
        int indiceSpecialite;
        final String[] universites = {"Univ. Paris", "Univ. Lyon", "Univ. Marseille", "Univ. Bordeaux", "Univ. Dijon", "Univ. Brest"};
        final int nbUniversites = 6;
        int indiceUniversite;
        
        // Tableau et variables en durs pour la classe Enseignant
        final String[] typesEtablissement = {"Collège", "Lycée", "Supérieur", "Primaire"};
        final int nbTypesEtablissement = 4;
        int indiceTypeEtablissement;
        
        // Tableau et variables en durs pour la classe Autre
        final String[] activites = {"Retraite", "Ingénieur", "Docteur", "Historien", "Chercheur"};
        final int nbActivites = 5;
        int indiceActivite;
        
        Intervenant intervenant;
        int nivMin, nivMax;
        try{
            JpaUtil.creerContextePersistance();
            JpaUtil.ouvrirTransaction();
            for (String[] infoIntervenant : intervenants) {
                // Tirer au hasard nivMin, nivMax
                // rand.nextInt prend un paramètre > 0 donc dans le cas où nivMin est nul, on fixe nivMax nul aussi
                nivMin = rand.nextInt(7);
                nivMax = 0;
                if (nivMin > 0) {
                    nivMax = rand.nextInt(nivMin);
                }
                
                // Pour le bon déroulement des tests : 
                nivMin = 6; nivMax = 0;
                switch (rand.nextInt(3)) {
                    case 0 : 
                        indiceActivite = rand.nextInt(nbActivites);
                        intervenant = new Autre(
                            activites[indiceActivite], infoIntervenant[0], infoIntervenant[1], infoIntervenant[2], infoIntervenant[3], nivMin, nivMax
                        );
                        break;
                    case 1 :
                        indiceSpecialite = rand.nextInt(nbSpecialites);
                        indiceUniversite = rand.nextInt(nbUniversites);
                        intervenant = new Etudiant(
                            universites[indiceUniversite], specialites[indiceSpecialite], infoIntervenant[0], infoIntervenant[1], infoIntervenant[2], infoIntervenant[3], nivMin, nivMax
                        );
                        break;
                    default :
                        indiceTypeEtablissement = rand.nextInt(nbTypesEtablissement);
                        intervenant = new Enseignant(
                            typesEtablissement[indiceTypeEtablissement], infoIntervenant[0], infoIntervenant[1], infoIntervenant[2], infoIntervenant[3], nivMin, nivMax
                        );
                }
                
                intervenantDao.create(intervenant);
            }
            JpaUtil.validerTransaction();
        }
        catch (Exception  ex){
            ex.printStackTrace();
            succes = false;
            JpaUtil.annulerTransaction();
        }
        finally{
            JpaUtil.fermerContextePersistance();
        }
        
        return succes;
    }
    
    /**
     * Connecte un intervenant grâce à son login
     * @param login
     * @return
     */
    public Intervenant authentifierIntervenant(String login) {
        IntervenantDao intervenantDao = new IntervenantDao();
        Intervenant res = null;
        
        try {
            JpaUtil.creerContextePersistance();
            res = intervenantDao.findIntervenantByLogin(login);
        } catch (Exception  ex) {
            ex.printStackTrace();
            res = null;
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        
        return res;
    }
    
    /**
     * Créé toutes les matières en dur dans la BDD
     * @return
     */
    public Boolean creerMatieres() {
        MatiereDao matiereDao = new MatiereDao();
        Boolean succes = true;
        final String[] matieres = {"Mathématiques", "Français", "Histoire", "Géographie", "Anglais", "Physique", "Chimie", "Espagnol", "Allemand"};
        Matiere m;
        try {
            JpaUtil.creerContextePersistance();
            JpaUtil.ouvrirTransaction();
            for (String s : matieres) {
                m = new Matiere(s);
                matiereDao.create(m);
            }
            JpaUtil.validerTransaction();
        } catch (Exception  ex){
            ex.printStackTrace();
            succes = false;
            JpaUtil.annulerTransaction();
        } finally{
            JpaUtil.fermerContextePersistance();
        }
        
        return succes;
    }
    
    /**
     * Donne toutes les matières proposées en soutien
     * @return List de toutes les matières
     */
    public List<Matiere> listerMatiere()
    {
        MatiereDao matiereDao = new MatiereDao();
        List<Matiere> res = null;
        try {
            JpaUtil.creerContextePersistance();
            res = matiereDao.findAll();
            
        } catch (Exception  ex){
            ex.printStackTrace();
            res = null;
        } finally{
            JpaUtil.fermerContextePersistance();
        }
        
        return res;
    }
    
    /**
     * Renvoie le libellé de la classe de l'eleve passé en paramètre
     * @param eleve
     * @return
     */
    public String getLibelleClasse(Eleve eleve) {
        final String[] NIVEAUX = {"Terminale", "Première", "Seconde", "Troisième", "Quatrième", "Cinquième", "Sixième"};
        final int terminale = 0;
        final int sixieme = 6;
        int nivClasse = eleve.getClasse();
        if (nivClasse < terminale || nivClasse > sixieme) {
            return null;
        }
        
        return NIVEAUX[nivClasse];
    }
     
    /**
     * Créé un soutien pour l'eleve passé en paramètre pour la matière donnée.Le point délicat est la recherche d'un intervenant disponible et qui a le niveau requis
     * S'il y en a plusieurs : on prend celui qui a effectué le moins de soutien pour l'instant
     * @param eleve
     * @param heureDebut
     * @param idMatiere
     * @param description
     * @return
     */
    public Soutien creerSoutien(Eleve eleve, Date heureDebut, Long idMatiere, String description) 
    {
        IntervenantDao intervenantDao = new IntervenantDao();
        SoutienDao soutienDao = new SoutienDao();
        MatiereDao matiereDao = new MatiereDao();
        EleveDao eleveDao = new EleveDao();
        String message = null;
        
        Soutien soutien = null;
        try {
            JpaUtil.creerContextePersistance();
            JpaUtil.ouvrirTransaction();
            // On vérifie les paramètres 
            // Eleve est supposé correct. Si idMatiere n'existe pas l'exception sera levée plus tard.
            if (heureDebut == null || description == null || description.isEmpty()) {
                throw new Exception();
            }
            Date aujourdhui = new Date();
            
            Matiere matiere = matiereDao.findById(idMatiere);
            
            soutien = new Soutien(aujourdhui, heureDebut, description, matiere, eleve);
            
            soutienDao.create(soutien);
            // On ajoute le soutien à l'élève pour conserver l'intégrité des données et relations
            eleve.addSoutien(soutien);
            eleveDao.update(eleve);
            
            Intervenant intervenant = intervenantDao.findIntervenantDispo(eleve.getClasse());
            if (intervenant != null) {
                intervenant.addSoutien(soutien);
                intervenant.setEstDisponible(false);
                message = "Bonjour " + intervenant.getPrenom() + ". Merci de prendre en charge la demande de Soutien en \"" + matiere.getNom() + 
                                 "\" demandée à " + Outil.formatDateHeure(soutien.getDateDebut()) + " par " + eleve.getPrenom() + " en classe de " + getLibelleClasse(eleve) + ".";
                intervenantDao.update(intervenant);
            }
                        
            JpaUtil.validerTransaction();
            
            if (intervenant != null) {
                Message.envoyerNotification(intervenant.getTelephone(), message);
            }
        }
        catch(Exception  ex){
            ex.printStackTrace();
            JpaUtil.annulerTransaction();
            soutien = null;
        }
        finally{
            JpaUtil.fermerContextePersistance();
        }
        return soutien;
    }
    
    /**
     * Formate le lien de la visio pour le soutien passé en paramètre.
     * @param soutien
     * @return String|null
     */
    public String getLienVisio(Soutien soutien) {
        // Si le soutien n'a pas d'intervenant ou bien qu'il est terminé (ie qu'il a une date de fin)
        // Alors le lien n'est pas valable
        if (soutien.getIntervenant() == null || soutien.getDateFin() != null) {
            return null;
        }
        
        return "https://servif.insa-lyon.fr/InteractIF/visio.html?eleve=" + soutien.getEleve().getMail() + "&intervenant=" + soutien.getIntervenant().getLogin();
    }
    
    /**
     * Retourne un tableau contenant les soutiens suivis par l'eleve passé en paramètre
     * ordonné selon le paramètre ordreTri
     * @param eleve
     * @param ordreTri String parmi : "intervenant", "dateDebut", "matiere"
     * @return
     */
    public List<Soutien> getHistoriqueSoutiensEleve(Eleve eleve, String ordreTri) {
        SoutienDao soutienDao = new SoutienDao();
        List<Soutien> res = null;
        if (ordreTri == null) {
            ordreTri = "dateDebut";
        }
                
        try {
            JpaUtil.creerContextePersistance();
            // Demander s'il vaut mieux faire plusieurs méthodes ou un paramètre ?
            switch (ordreTri) {
                case "matiere" :
                    res = soutienDao.findSoutiensByEleveOrderByMatiere(eleve);
                    break;
                case "intervenant" :
                    res = soutienDao.findSoutiensByEleveOrderByIntervenant(eleve);
                    break;
                case "dateDebut" :
                default :
                    res = soutienDao.findSoutiensByEleveOrderByDateDebut(eleve);       
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            res = null;
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        
        return res;
    }
    
    /**
     * Retourne un tableau contenant les soutiens donnés par l'intervenant passé 
     * en paramètre ordonné selon le paramètre ordreTri
     * @param intervenant
     * @param ordreTri
     * @return
     */
    public List<Soutien> getHistoriqueSoutiensIntervenant(Intervenant intervenant, String ordreTri) {
        if (intervenant == null) {
            return null;
        }
        SoutienDao soutienDao = new SoutienDao();
        
        List<Soutien> res = null;
        
        try {
            JpaUtil.creerContextePersistance();
            // Demander s'il vaut mieux faire plusieurs méthodes ou un paramètre ?
            switch (ordreTri) {
                case "matiere" : 
                    res = soutienDao.findSoutiensByIntervenantOrderByMatiere(intervenant);
                    break;
                case "eleve" :
                    res = soutienDao.findSoutiensByIntervenantOrderByEleve(intervenant);
                    break;
                case "dateDebut" :
                default :
                    res = soutienDao.findSoutiensByIntervenantOrderByDateDebut(intervenant);       
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            res = null;
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        
        return res;
    }
    
    /**
     * Permet à un interrvenant de saisir le bilan relatif à un soutien
     * Envoie un mail contenant le bilan à l'eleve
     * Définis le soutien comme terminé et fixe son heure de fin
     * @param soutien
     * @param intervenant
     * @param bilan String non null et non vide
     * @return
     */
    public Soutien saisirBilanSoutien(Soutien soutien, Intervenant intervenant, String bilan) {
        SoutienDao soutienDao = new SoutienDao();
        IntervenantDao intervenantDao = new IntervenantDao();
        Soutien res = null;
        try {
            JpaUtil.creerContextePersistance();
            JpaUtil.ouvrirTransaction();
            // Programmation défensive : seul l'intervenant du soutien peut saisir le bilan
            if (soutien.getIntervenant() == null || !soutien.getIntervenant().equals(intervenant) ||
                bilan == null || bilan.isEmpty() ) {
                throw new Exception();
            }
            
            soutien.setBilan(bilan);
                                    
            //Le soutien est donc considéré comme terminé :
            Date dateFin = new Date();
            soutien.setDateFin(dateFin);
            intervenant.setEstDisponible(true);
                    
            res = soutienDao.update(soutien);
            intervenantDao.update(intervenant);
            
            JpaUtil.validerTransaction();
            
            // Puis on envoie le bilan à l'eleve
            Message.envoyerMail("contact@instruct.if", soutien.getEleve().getMail(), 
                                "Bilan soutien avec " + intervenant.getPrenom() + " " + intervenant.getNom(), bilan);
        } catch (Exception ex) {
            ex.printStackTrace();
            JpaUtil.annulerTransaction();
            res = null;
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        
        return res;
    }
    
    /**
     * Permet à un eleve de donner son niveau de compréhension quant au soutien
     * @param soutien
     * @param eleve
     * @param niveauComprehension
     * @return
     */
    public Soutien saisirNiveauComprehensionSoutien(Soutien soutien, Eleve eleve, int niveauComprehension) {
        SoutienDao soutienDao = new SoutienDao();
        Soutien res = null;
        try {
            JpaUtil.creerContextePersistance();
            JpaUtil.ouvrirTransaction();
            // Programmation défensive : seul l'eleve du soutien peut saisir le niveau
            // Et celui-ci doit être compris dans [0;5]
            if (soutien.getEleve() == null || !soutien.getEleve().equals(eleve) ||
                niveauComprehension < 0 || niveauComprehension > 5) {
                throw new Exception();
            }
            
            soutien.setNiveauComprehension(niveauComprehension);
            res = soutienDao.update(soutien);
            
            JpaUtil.validerTransaction();
        } catch (Exception ex) {
            ex.printStackTrace();
            JpaUtil.annulerTransaction();
            res = null;
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        
        return res;
    }
    
    /**
     * Retourne les détails d'un soutien selon son id
     * 
     * @param idSoutien
     * @return
     */
    public Soutien obtenirDetailsSoutien(Long idSoutien) {
        SoutienDao soutienDao = new SoutienDao();
        Soutien res = null;
        try {
            JpaUtil.creerContextePersistance();
            res = soutienDao.findById(idSoutien);
        } catch (Exception ex) {
            ex.printStackTrace();
            res = null;
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        
        return res;
    }
    
    /**
     * Donne la durée moyenne de tous les soutiens effectués par l'intervenant en paramètre
     * @param intervenant
     * @return
     */
    public int obtenirDureeMoyenne(Intervenant intervenant) {
        SoutienDao soutienDao = new SoutienDao();
        int res = 0;
        try {
            JpaUtil.creerContextePersistance();
            List<Soutien> soutiens = soutienDao.findSoutiensByIntervenant(intervenant);
            
            // Format de date personnalisé
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH'h'mm");

            // Variables pour calculer la moyenne
            long totalDuration = 0;
            int nbSoutien = soutiens.size();

            // Parcours des soutiens et calcul des durées
            for (Soutien soutien : soutiens) {
                Date dateDebut = soutien.getDateDebut();
                Date dateFin = soutien.getDateFin();
                
                // Calcul de la durée en millisecondes
                long duration = dateFin.getTime() - dateDebut.getTime();
                totalDuration += duration;
            }
            // Calcul de la durée moyenne en minutes
            if (nbSoutien > 0) {
                long averageDuration = totalDuration / nbSoutien;
                res = (int) (averageDuration / (60 * 1000));  // Conversion des millisecondes en minutes
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            res = -1;
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        
        return res;
    }
    
    /**
     * Donne la note de compréhension moyenne de tous les soutiens effectués par l'intervenant en paramètre
     * @param intervenant
     * @return
     */
    public float obtenirComprehensionMoyenne(Intervenant intervenant) {
        SoutienDao soutienDao = new SoutienDao();
        float res =-1;
        try {
            JpaUtil.creerContextePersistance();
            res = soutienDao.findMoyenneSoutien(intervenant);
        } catch (Exception ex) {
            ex.printStackTrace();
            res = -1;
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        
        return res;
    }
                
    /**
     * Donne le nombre de soutien effectués par l'intervenant passé en paramètre
     * @param intervenant
     * @return
     */
    public int obtenirNbSoutiens(Intervenant intervenant) {
        SoutienDao soutienDao = new SoutienDao();
        int res =-1;
        try {
            JpaUtil.creerContextePersistance();
            res = soutienDao.findNbSoutiensByIntervenant(intervenant);
        } catch (Exception ex) {
            ex.printStackTrace();
            res = -1;
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        
        return res;
    }
    
    /**
     * Donne la répartition des classes des élèves de l'intervenant sous forme de tableau, avec pour chaque indice le nombre d'eleve de la classe donnée
     * Même correspondance que pour le reste : 0 pour terminale, 1 pour première ... 6 pour sixième
     * @param intervenant
     * @return
     */
    public int[] obtenirRepartitionClasses(Intervenant intervenant) {
        SoutienDao soutienDao = new SoutienDao();
        int[] res = null;
        List<Soutien> soutiens = null;
        try {
            JpaUtil.creerContextePersistance();
            soutiens = soutienDao.findSoutiensByIntervenant(intervenant);
            
            res = new int[7];  // Tous les éléments sont nulls
            Arrays.fill(res, 0); 

            for(Soutien soutien : soutiens) {
                res[(int)soutien.getEleve().getClasse()] += 1;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            res = null;
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        
        return res;
    }
    
    /**
     * Renvoie un tableau de 24 éléments représentant le nombre de soutien de l'intervenant en paramètre pour une heure donnée
     * Par exemple : entre 00h et 01h, le nombre de soutien est présent à l'indice 0 et ainsi de suite
     * @param intervenant
     * @return Tableau d'entiers représentant le nombre de soutien à chaque heure de la journée
     */
    public int[] obtenirRepartitionHoraire(Intervenant intervenant) {
        SoutienDao soutienDao = new SoutienDao();
        List<Soutien> soutiens = null;
        int[] tab = new int[24];  // Tous les éléments sont nulls
        Arrays.fill(tab, 0); 
        try {
            JpaUtil.creerContextePersistance();
            soutiens = soutienDao.findSoutiensByIntervenant(intervenant);
            for (Soutien soutien : soutiens) {
                Date dateDebut = soutien.getDateDebut();
                Date dateFin = soutien.getDateFin();
                
                Calendar cal = Calendar.getInstance();
                cal.setTime(dateDebut);
                int heureDeb = cal.get(Calendar.HOUR_OF_DAY);
                cal.setTime(dateFin);
                int heureFin = cal.get(Calendar.HOUR_OF_DAY);
                if(heureDeb < heureFin ) {
                    // Sur le même jour
                    for (int i = heureDeb; i <= heureFin; i++) {
                       tab[i]+=1;
                    }
                }
                else {
                    // Sur deux jours différents : par exe 23h - 01h
                    for (int i = 0; i <= heureFin; i++) {
                       tab[i]+=1;
                    }
                    for (int i = heureDeb ; i < 24; ++i) {
                       tab[i]+=1;
                    }
                }
         }
        } catch (Exception ex) {
            ex.printStackTrace();
            soutiens = null;
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        
        return tab;
    }

    /**
     * Donne le nombre d'élève par point géographique qui ont suivi un soutien donné par l'intervenant passé en paramètre
     * @param intervenant
     * @return
     */
    public Map<String, Integer> obtenirRepartitionGeo(Intervenant intervenant) {
        SoutienDao soutienDao = new SoutienDao();
        Map<String, Integer> res = null;
        List<Soutien> soutiens = null;
        try {
            JpaUtil.creerContextePersistance();
            
            res =  new HashMap<>();
            soutiens = soutienDao.findSoutiensByIntervenant(intervenant);
            for (Soutien soutien : soutiens) {
                Double lat = soutien.getEleve().getEtablissement().getLat();
                Double lng = soutien.getEleve().getEtablissement().getLng();

                // Créer une clé unique basée sur lat et lng
                String coordKey = lat + "," + lng;

                // Incrémenter le compteur pour cette clé
                res.put(coordKey, res.getOrDefault(coordKey, 0) + 1);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            res = null;
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        
        return res;
    }
}
