
import dao.JpaUtil;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import metier.modele.Eleve;
import metier.modele.Intervenant;
import metier.modele.Matiere;
import metier.modele.Soutien;
import metier.service.Service;
import static util.Message.FG_BLUE;
import static util.Message.FG_GREEN;
import static util.Message.FG_RED;
import static util.Message.RESET;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ehequet
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        JpaUtil.creerFabriquePersistance();
        
        Service service = new Service();
        service.creerMatieres();
        service.creerIntervenants();
//        
//        Boolean resTestGenererMatiere = testGenererMatiere();
//        
//        // Test sans Intervenants : 
//        Boolean resFonctionnelEleve = testFonctionnelEleveInscriptionConnexionModification();
//        
//        // Puis on génère les intervenants :
//        Boolean resTestGenererIntervenants = genererIntervenants();
//        
//        Boolean resFonctionnelMultipleDemande = testFonctionnelMultipleDemande();
//        
//        /* // Tests des cas limites des différentes fonctions : attention, ne fonctionne pas toujours à cause de l'aléatoire et des tests de recette
//        Boolean resTestInscription = testInscription();
//        Boolean resTestAuthentification = testAuthentification();
//        Boolean resTestAuthentIntervenant = testAuthentIntervenant();
//        Boolean resTestCreerSoutien = testCreerSoutien();
//        Boolean resTestFindSoutienEleve = testFindSoutienEleve();
//        Boolean resTestFindSoutienIntervenant = testFindSoutienIntervenant();
//        Boolean resTestSaisirNote = testSaisirNote();
//        Boolean resTestSaisirBilan = testSaisirBilan();
//        Boolean resTestStats = testStatistiques();
//            
//        System.out.println(FG_BLUE + "Test inscription eleves : " + (resTestInscription ? FG_GREEN + "PASSED" : FG_RED +"FAILED") + RESET);
//        System.out.println(FG_BLUE + "Test authent eleves : " + (resTestAuthentification ? FG_GREEN + "PASSED" : FG_RED +"FAILED") + RESET);
//        System.out.println(FG_BLUE + "Test authentification intervenants : " + (resTestAuthentIntervenant ? FG_GREEN + "PASSED" : FG_RED +"FAILED") + RESET);
//        System.out.println(FG_BLUE + "Test créer soutien : " + (resTestCreerSoutien ? FG_GREEN + "PASSED" : FG_RED +"FAILED") + RESET); 
//        System.out.println(FG_BLUE + "Test find soutien Eleve : " + (resTestFindSoutienEleve ? FG_GREEN + "PASSED" : FG_RED +"FAILED") + RESET);
//        System.out.println(FG_BLUE + "Test find soutien Intervenant : " + (resTestFindSoutienIntervenant ? FG_GREEN + "PASSED" : FG_RED +"FAILED") + RESET); 
//        System.out.println(FG_BLUE + "Test saisir note : " + (resTestSaisirNote ? FG_GREEN + "PASSED" : FG_RED +"FAILED") + RESET); 
//        System.out.println(FG_BLUE + "Test saisir bilan : " + (resTestSaisirBilan ? FG_GREEN + "PASSED" : FG_RED +"FAILED") + RESET); 
//        */
//        System.out.println(FG_BLUE + "Test unitaires Statistiques : " + (testStatistiques()? FG_GREEN + "PASSED" : FG_RED +"FAILED") + RESET);      
//        
//        System.out.println(FG_BLUE + "Test Focntionnel Eleve : " + (resFonctionnelEleve? FG_GREEN + "PASSED" : FG_RED +"FAILED") + RESET); 
//        System.out.println(FG_BLUE + "Test Focntionnel Trop de demandes : " + (resFonctionnelMultipleDemande? FG_GREEN + "PASSED" : FG_RED +"FAILED") + RESET); 
//        
               
        JpaUtil.fermerFabriquePersistance();
    }
    
    public static Boolean testFonctionnelMultipleDemande() {
        Service service = new Service();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date aujourdhui, demain, hier;
        try {
            aujourdhui = sdf.parse("12/07/1998");
            demain = sdf.parse("13/08/1999");
            hier = sdf.parse("11/06/1997");
            
        } catch (Exception ex) {
            return false;
        }
        Date[] tabDate = {aujourdhui, hier, demain};
        String uai = "0691664J";
        Eleve eleveInstance = new Eleve("nouvnouv@gmail.com", 5, aujourdhui, "mdp", "NOUVEL", "Demandeur");
        if (!service.inscrireEleve(eleveInstance, uai)) {
            return false;
        }
        
        Eleve connexionEleve = service.authentifierEleve("nouvnouv@gmail.com", "mdp");
        if (connexionEleve == null || !connexionEleve.getMail().equals("nouvnouv@gmail.com")) {
            return false;
        }
        Soutien soutien = null;
        for (int i = 0; i < 10 ; ++i) {
            
            soutien = service.creerSoutien(connexionEleve, tabDate[i % 3], new Long((i % 6) + 1), "Ma demande n°" + i);
            if (soutien == null || soutien.getIntervenant() == null) {
                return false;
            }
        }
        // Le prochain soutien ne devrait pas trouver d'intervenant : 
        Soutien soutienNoIntervenant = service.creerSoutien(connexionEleve, aujourdhui, new Long(1), "Pas d'intervenant !");
        if (soutienNoIntervenant == null || soutienNoIntervenant.getIntervenant() != null) {
            return false;
        }
        
        System.out.println("Testez le lien :");
        System.out.println(service.getLienVisio(soutien));
        
        Intervenant intervenant = soutien.getIntervenant();
        Intervenant intervenantAuthentifie = service.authentifierIntervenant(intervenant.getLogin());
        
        if (!intervenant.equals(intervenantAuthentifie)) {
            return false;
        }
        
        // On saisit un bilan pour un soutien : <=> le bilan est terminé
        Soutien soutienNote = service.saisirNiveauComprehensionSoutien(soutien, connexionEleve, 5);
        Soutien nouvSoutien = service.saisirBilanSoutien(soutien, intervenantAuthentifie, "Super");
        Intervenant intervenantSoutien = nouvSoutien.getIntervenant();
        
        
        if (nouvSoutien.getBilan() == null || nouvSoutien.getDateFin() == null || !intervenantSoutien.getEstDisponible() || nouvSoutien.getNiveauComprehension() != 5) {
            return false;
        }
        
        Soutien soutienIntervenant = service.creerSoutien(connexionEleve, aujourdhui, new Long(1), "intervenant à deux soutiens !");
        if (soutienIntervenant == null || soutienIntervenant.getIntervenant() == null || soutienIntervenant.getIntervenant().getNbSoutiens() != 2) {
            return false;
        }
        
        List<Soutien> soutiens = service.getHistoriqueSoutiensEleve(connexionEleve, "intervenant");
        
        // liste de taille 12 : les 9 en cours de la boucle + la terminé + celle à deux soutiens
        if (soutiens.size() != 12) {
            return false;
        }
        // On va vérifier l'ordre donné
        int cpt = 0;
        String precedent = null;
        System.out.println("Soutiens de " + connexionEleve + " triés par intervenant (nom, prenom)");
        for (Soutien s : soutiens) {
            if (!s.getEleve().equals(connexionEleve) || (precedent != null && s.getIntervenant() != null && precedent.compareTo(s.getIntervenant().getNom() + " " + s.getIntervenant().getPrenom()) > 0)) {
                return false;
            }
            System.out.println("" + ++cpt + " : id = " + s.getId() + " " + (s.getIntervenant() != null ? s.getIntervenant().getNom() + " " + s.getIntervenant().getPrenom() : "null" ));
            precedent = (s.getIntervenant() != null ? s.getIntervenant().getNom() + " " + s.getIntervenant().getPrenom() : null );
        }
        
        soutiens = service.getHistoriqueSoutiensEleve(connexionEleve, "dateDebut");
        
        // liste de taille 12 : les 9 en cours de la boucle + la terminé + celle à deux soutiens
        if (soutiens.size() != 12) {
            return false;
        }
        Date datePrecedente = null;
        cpt = 0;
        System.out.println("Soutiens de " + connexionEleve + " triés par dateDebut ");
        for (Soutien s : soutiens) {
            if (!s.getEleve().equals(connexionEleve) || (datePrecedente != null && datePrecedente.compareTo(s.getDateDebut()) > 0)) {
                return false;
            }
            System.out.println("" + ++cpt + " : id = " + s.getId() + " " + s.getDateDebut());
            datePrecedente = s.getDateDebut();
        }
        
        soutiens = service.getHistoriqueSoutiensEleve(connexionEleve, "matiere"); 
        
        // liste de taille 12 : les 9 en cours de la boucle + la terminé + celle à deux soutiens
        if (soutiens.size() != 12) {
            return false;
        }
        precedent = null;
        cpt = 0;
        System.out.println("Soutiens de " + connexionEleve + " triés par matière ");
        for (Soutien s : soutiens) {
            if (!s.getEleve().equals(connexionEleve) || (precedent != null && precedent.compareTo(s.getMatiere().getNom()) > 0)) {
                return false;
            }
            System.out.println("" + ++cpt + " : id = " + s.getId() + " " + s.getMatiere().getNom());
            precedent = s.getMatiere().getNom();
        }
        
        return true;
    }
    
    public static Boolean testFonctionnelEleveInscriptionConnexionModification() {
        Service service = new Service();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date aujourdhui;
        try {
            aujourdhui = sdf.parse("12/07/1998");
        } catch (Exception ex) {
            return false;
        }
        
        String uai = "0691664J";
        Eleve eleveInstance = new Eleve("NouvelEleve@gmail.com", 5, aujourdhui, "mdp", "NOUVEL", "Eleve");
        if (!service.inscrireEleve(eleveInstance, uai)) {
            return false;
        }
        
        Eleve connexionEleve = service.authentifierEleve("NouvelEleve@gmail.com", "mdp");
        if (connexionEleve == null || !connexionEleve.getMail().equals("NouvelEleve@gmail.com")) {
            return false;
        }
        
        Eleve eleveModification = new Eleve("NouvelEleve@gmail.com", 5, aujourdhui, "mdp", "MAJNOM", "Eleve");
        
        Eleve eleveUpdate = service.modifierEleve(connexionEleve, eleveModification, null);
        
        if (eleveUpdate == null || !eleveUpdate.getNom().equals("MAJNOM")) {
            return false;
        }
        
        System.out.println("Liste des matières : ");
        List<Matiere> matieres = service.listerMatiere();
        for (Matiere m : matieres) {
            System.out.println(m);
        }
                
        Soutien soutien = service.creerSoutien(eleveUpdate, aujourdhui, new Long(1), "J'ai besoin de soutien pour une analyse de texte svp");
        if (soutien == null || soutien.getIntervenant() != null || !soutien.getEleve().equals(eleveUpdate)) {
            return false;
        }
        
        Eleve eleveFind = service.obtenirDetailsEleve("NouvelEleve@gmail.com");
        if (!eleveFind.equals(eleveUpdate)) {
            return false;
        }
        
        Soutien soutienFind = service.obtenirDetailsSoutien(soutien.getId());
        if (!soutienFind.equals(soutien)) {
            return false;
        }
        
        List<Soutien> historique = service.getHistoriqueSoutiensEleve(eleveUpdate, "intervenant");
        
        if (historique == null || historique.size() != 1 || !historique.get(0).equals(soutien)) {
            return false;
        }
        
        return true;
    }
    
    public static Boolean testGenererMatiere() {
        Service service = new Service();
        return service.creerMatieres();
    }
    
    public static Boolean genererIntervenants() {
        Service service = new Service();
        return service.creerIntervenants();
    }
    
    public static Boolean testInscription()
    {
        Service service = new Service();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date aujourdhui;
        try {
            aujourdhui = sdf.parse("12/07/1998");
            
            Eleve e = new Eleve( "mail@gmail.com", 0 , aujourdhui, "mdp", "HUGO", "Victor");
            Boolean res = service.inscrireEleve(e,"0691664J");
            
            Eleve nouvEleve = new Eleve( "eleve2@gmail.com", 0 , aujourdhui, "mdp", "HUGO", "Victor");
            Boolean res2 = service.inscrireEleve(nouvEleve, "0691664J");
            
            Eleve eleveSansEtablissement = new Eleve( "eleveSansEtablissement@gmail.com", 0 , aujourdhui, "mdp", "HUGO", "Victor");
            Boolean res3 = service.inscrireEleve(eleveSansEtablissement,"3930P");
            
            Eleve eleveClasseTropPetite = new Eleve("mauvaiseClasse@gmail.com", -1, aujourdhui, "mdp", "mauvaise", "CLASSE");
            Eleve eleveClasseTropGrande = new Eleve("mauvaiseClasse2@gmail.com", 7, aujourdhui, "mdp", "mauvaise", "CLASSE");
            
            Eleve eleveMailEmpty = new Eleve("", 6, aujourdhui, "mdp", "mauvaise", "CLASSE");
            Eleve eleveMailNull = new Eleve(null, 6, aujourdhui, "mdp", "mauvaise", "CLASSE");
            
            Eleve eleveMdpEmpty = new Eleve("mdpEmpty@gmail.com", 3, aujourdhui, "", "mauvaise", "CLASSE");
            Eleve eleveMdpNull = new Eleve("mdpNull@gmail.com", 3, aujourdhui, null, "mauvaise", "CLASSE");
            
            Eleve eleveNomEmpty = new Eleve("nomEmpty@gmail.com", 3, aujourdhui, "mdp", "", "CLASSE");
            Eleve eleveNomNull = new Eleve("nomNull@gmail.com", 3, aujourdhui, "mdp", null, "CLASSE");
            
            Eleve elevePrenomEmpty = new Eleve("prenomEmpty@gmail.com", 3, aujourdhui, "mdp", "mauvaise", "");
            Eleve elevePrenomNull = new Eleve("prenomNull@gmail.com", 3, aujourdhui, "mdp", "mauvaise", null);
            
            if (
                service.inscrireEleve(eleveClasseTropPetite, "0691664J") || service.inscrireEleve(eleveClasseTropGrande, "0691664J") ||
                service.inscrireEleve(eleveMailEmpty, "0691664J") || service.inscrireEleve(eleveMailNull, "0691664J") ||
                service.inscrireEleve(eleveMdpEmpty, "0691664J") || service.inscrireEleve(eleveMdpNull, "0691664J") ||
                service.inscrireEleve(eleveNomNull, "0691664J") || service.inscrireEleve(eleveNomEmpty, "0691664J") ||
                service.inscrireEleve(elevePrenomEmpty, "0691664J") || service.inscrireEleve(elevePrenomNull, "0691664J")
                ) {
                return false;
            }

            return res && res2 && !res3;
        } catch(Exception ex){
            ex.printStackTrace();
            return false;
        }
    }
    
    public static Boolean testAuthentification()
    {        
        Service service = new Service(); 
        
        Eleve testOK = service.authentifierEleve("mail@gmail.com", "mdp");
        
        Eleve testKOmdpIncorrect = service.authentifierEleve("mail@gmail.com", "NON");
        
        Eleve testKOmailNotExists = service.authentifierEleve("test@ne.com", "mdp");
        
        return testOK != null && testOK.getMail().equals("mail@gmail.com") && testKOmdpIncorrect == null && testKOmailNotExists == null;
    }
    
    public static Boolean testAuthentIntervenant() {
        Service service = new Service();
        
        String login = "sfavro";
        Intervenant intervenantExistant = service.authentifierIntervenant(login);
        
        Intervenant intervenantNonExistant = service.authentifierIntervenant("NOTEXIST");
        
        return intervenantNonExistant == null && intervenantExistant != null && intervenantExistant.getLogin().equals(login);
    }

    
    public static Boolean testCreerSoutien()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date aujourdhui;
        try {
            aujourdhui = sdf.parse("12/07/1998");
        }catch (Exception ex) {
            return false;
        }
            //aujourdhui = new Date();
        Eleve e = new Eleve( "testCreerSoutien@gmail.com", 3, aujourdhui, "mdp", "CREATE", "SOUTIEN");
        
        Service service = new Service(); 
        
        service.inscrireEleve(e,"0691664J");
        String description = "Test Creation Soutien alaide j'ai besoin d'aide";
        
        Date heureDebut = new Date();
        
        Soutien s = service.creerSoutien(e, heureDebut, new Long(1) , description);
        
        // Ici, il ne doit pas y avoir de prof de dispo
        Soutien s2 = service.creerSoutien(e, aujourdhui, new Long(1) , "Pb en français");
                
        try {
            aujourdhui = sdf.parse("12/07/2010");
        }catch (Exception ex) {
            return false;
        }
        Soutien s3 = service.creerSoutien(e, aujourdhui, new Long(1), "Maths");
                
        return s != null && s.getIntervenant() != null && s2 != null && s2.getIntervenant() != null && s3 != null && s3.getIntervenant() != null;
    }
    
    public static Boolean testFindSoutienEleve() {
        Service service = new Service();
        Eleve e = service.authentifierEleve("testCreerSoutien@gmail.com", "mdp");
        List<Soutien> res  = service.getHistoriqueSoutiensEleve(e, "");
        List<Soutien> res2  = service.getHistoriqueSoutiensEleve(e, "intervenant");
        List<Soutien> res3  = service.getHistoriqueSoutiensEleve(e, "matiere");
        List<Soutien> res4  = service.getHistoriqueSoutiensEleve(e, "dateDebut");
        
        System.out.println( "default :     ");
        for (Soutien s : res) {
            System.out.println(s);
        }
        System.out.println( "intervenant : ");
        for (Soutien s : res2) {
            System.out.println(s);
        }
        System.out.println( "matiere :     ");
        for (Soutien s : res3) {
            System.out.println(s);
        }
        System.out.println( "dateDebut :   ");
        for (Soutien s : res4) {
            System.out.println(s);
        }
       
        return res != null && res2 != null && res3 != null && res4 != null;
    }
    
    public static Boolean testFindSoutienIntervenant() {
        Service service = new Service();
        Intervenant intervenant = service.authentifierIntervenant("sfavro");
        List<Soutien> res  = service.getHistoriqueSoutiensIntervenant(intervenant, "");
        List<Soutien> res2  = service.getHistoriqueSoutiensIntervenant(intervenant, "intervenant");
        List<Soutien> res3  = service.getHistoriqueSoutiensIntervenant(intervenant, "matiere");
        List<Soutien> res4  = service.getHistoriqueSoutiensIntervenant(intervenant, "dateDebut");
       
        return res != null && res2 != null && res3 != null && res4 != null;
    }
    
    public static Boolean testSaisirNote() {
        Service service = new Service();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date aujourdhui;
        try {
            aujourdhui = sdf.parse("12/07/1998");
        }catch (Exception ex) {
            return false;
        }
        Eleve e = service.authentifierEleve("testCreerSoutien@gmail.com", "mdp");
        Soutien soutien = service.creerSoutien(e, aujourdhui, new Long(1), "Besoin d'aide en Maths !");
        
        if (soutien == null || soutien.getIntervenant() == null) {
            return false;
        }
        
        Eleve mauvaisEleve = service.authentifierEleve("mail@gmail.com", "mdp");
                
        if (service.saisirNiveauComprehensionSoutien(soutien, e, 6) != null || service.saisirNiveauComprehensionSoutien(soutien, e, -1) != null ||
            service.saisirNiveauComprehensionSoutien(soutien, mauvaisEleve, 5) != null) {
            return false;
        }
        
        Soutien res = service.saisirNiveauComprehensionSoutien(soutien, e, 5);
        
        return res != null && res.getNiveauComprehension() == 5 && res.equals(soutien);
    }
    
    public static Boolean testSaisirBilan() {
        Service service = new Service();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date aujourdhui;
        try {
            aujourdhui = sdf.parse("12/07/1998");
        }catch (Exception ex) {
            return false;
        }
        Eleve e = service.authentifierEleve("mail@gmail.com", "mdp");
        Soutien soutien = service.creerSoutien(e, aujourdhui, new Long(1), "Need to speak well");
        
        if (soutien == null || soutien.getIntervenant() == null) {
            return false;
        }
        
        Intervenant intervenant = soutien.getIntervenant();
        // tester avec un autre intervenant ?
        
        if (service.saisirBilanSoutien(soutien, intervenant, "") != null || service.saisirBilanSoutien(soutien, intervenant, null) != null) {
            return false;
        }
                
        Soutien res = service.saisirBilanSoutien(soutien, intervenant, "Super");
        
        return res != null && res.getBilan() != null && res.getBilan().equals("Super") && res.getDateFin() != null && res.equals(soutien) && intervenant.getEstDisponible();
    }
    
     public static Boolean testStatistiques() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy HH'h'mm");
        Date aujourdhui;
        Date debut1;
        Date debut2;
        Date fin1;
        Date fin2;
        try {
            debut1= sdf2.parse("12/07/1998 16h30");
            debut2= sdf2.parse("12/07/1998 14h50");
            fin1= sdf2.parse("13/07/1998 5h30");
            fin2= sdf2.parse("12/07/1998 16h50");
            aujourdhui = sdf.parse("12/07/1998");
        }catch (Exception ex) {
            return false;
        }
            //aujourdhui = new Date();
        Eleve e = new Eleve( "testStats@gmail.com", 2 , aujourdhui, "mdp", "Verify", "Stats");
        
        Service service = new Service(); 
        
        service.inscrireEleve(e,"0691664J");
        String description = "Test generation stats alaide j'ai besoin d'aide";
        
        Date heureDebut = new Date();
        
    //    Intervenant testEnseignant =  new Enseignant("public", true, "06 81 81 81 55", "Prof", "Histoire", "STHISTOIRE", 6, 0);
        Intervenant testEnseignant = service.authentifierIntervenant("sfavro");
        
        Soutien s = service.creerSoutien(e, debut1, new Long(1), description);
        s.setIntervenant(testEnseignant);
        s.setDateFin(fin1);
        service.saisirNiveauComprehensionSoutien(s,e,4);
        
        
        Soutien s2 = service.creerSoutien(e, debut2, new Long(1), "Pas de prof");
        s2.setIntervenant(testEnseignant);
        s2.setDateFin(fin2);
        service.saisirNiveauComprehensionSoutien(s2,e,5);
        
        
        int res = service.obtenirDureeMoyenne(testEnseignant);        
        float res2 = service.obtenirComprehensionMoyenne(testEnseignant);
        int res3 = service.obtenirNbSoutiens(testEnseignant);
        int[] res4 = service.obtenirRepartitionClasses(testEnseignant);
        int[] res5 = service.obtenirRepartitionHoraire(testEnseignant);
        Map<String, Integer> res6 = service.obtenirRepartitionGeo(testEnseignant);
        
        System.out.println(res);
        System.out.println(res2);
        System.out.println(res3);
        System.out.println(Arrays.toString(res4)); 
        System.out.println(Arrays.toString(res5));  
        System.out.println(res6.toString());
        
        return res != -1 && res2 != -1 && res3 !=-1 && res4 != null && res5 != null && res6 != null;
    }
    
}
