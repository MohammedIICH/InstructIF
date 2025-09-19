/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import javax.persistence.TypedQuery;
import metier.modele.Eleve;
import metier.modele.Intervenant;
import metier.modele.Soutien;

/**
 *
 * @author ehequet
 */
public class SoutienDao {
    
    public void create(Soutien soutien) {
        JpaUtil.obtenirContextePersistance().persist(soutien);
    }
    
    public Soutien update(Soutien soutien) {
        return JpaUtil.obtenirContextePersistance().merge(soutien);
    }
    
    public Soutien findById(Long id) {
        return JpaUtil.obtenirContextePersistance().find(Soutien.class, id);
    }
    
    public List<Soutien> findSoutiensByEleveOrderByDateDebut(Eleve eleve) {
        String requeteJPQL = "SELECT s FROM Soutien s WHERE s.eleve.mail = :mail ORDER BY s.dateDebut asc";
        TypedQuery<Soutien> query = JpaUtil.obtenirContextePersistance().createQuery(requeteJPQL, Soutien.class);
        query.setParameter("mail", eleve.getMail());
        return query.getResultList();
    }
    
    public List<Soutien> findSoutiensByEleveOrderByIntervenant(Eleve eleve) {
        // Pour afficher tous les soutiens même ceux qui n'on pas d'intervenant : jointure à gauche
        String requeteJPQL =  "SELECT s FROM Soutien s LEFT JOIN s.intervenant i WHERE s.eleve.mail = :mail ORDER BY i.nom ASC, i.prenom ASC";
        TypedQuery<Soutien> query = JpaUtil.obtenirContextePersistance().createQuery(requeteJPQL, Soutien.class);
        query.setParameter("mail", eleve.getMail());
        return query.getResultList();
    }
    
    public List<Soutien> findSoutiensByEleveOrderByMatiere(Eleve eleve) {
        String requeteJPQL = "SELECT s FROM Soutien s WHERE s.eleve.mail = :mail ORDER BY s.matiere.nom asc";
        TypedQuery<Soutien> query = JpaUtil.obtenirContextePersistance().createQuery(requeteJPQL, Soutien.class);
        query.setParameter("mail", eleve.getMail());
        return query.getResultList();
    }
    
    public List<Soutien> findSoutiensByIntervenantOrderByDateDebut(Intervenant intervenant) {
        String requeteJPQL = "SELECT s FROM Soutien s WHERE s.intervenant.login = :login ORDER BY s.dateDebut asc";
        TypedQuery<Soutien> query = JpaUtil.obtenirContextePersistance().createQuery(requeteJPQL, Soutien.class);
        query.setParameter("login", intervenant.getLogin());
        return query.getResultList();
    }
    
    public List<Soutien> findSoutiensByIntervenantOrderByEleve(Intervenant intervenant) {
        String requeteJPQL = "SELECT s FROM Soutien s WHERE s.intervenant.login = :login ORDER BY s.eleve.nom asc, s.eleve.prenom asc";
        TypedQuery<Soutien> query = JpaUtil.obtenirContextePersistance().createQuery(requeteJPQL, Soutien.class);
        query.setParameter("login", intervenant.getLogin());
        return query.getResultList();
    }
    
    public List<Soutien> findSoutiensByIntervenantOrderByMatiere(Intervenant intervenant) {
        String requeteJPQL = "SELECT s FROM Soutien s WHERE s.intervenant.login = :login ORDER BY s.matiere.nom asc";
        TypedQuery<Soutien> query = JpaUtil.obtenirContextePersistance().createQuery(requeteJPQL, Soutien.class);
        query.setParameter("login", intervenant.getLogin());
        return query.getResultList();
    }
    
    public float findMoyenneSoutien(Intervenant intervenant) {
        String requeteJPQL = "SELECT AVG(s.niveauComprehension * 1.0) FROM Soutien s WHERE s.intervenant.login = :login AND s.niveauComprehension >= 0";
        TypedQuery<Double> query = JpaUtil.obtenirContextePersistance().createQuery(requeteJPQL, Double.class);
        query.setParameter("login", intervenant.getLogin());
        
        return (float) query.getSingleResult().floatValue();
    }
    
    public int findNbSoutiensByIntervenant(Intervenant intervenant) {
        String requeteJPQL = "SELECT COUNT(s) FROM Soutien s WHERE s.intervenant.login = :login ";
        TypedQuery<Long> query = JpaUtil.obtenirContextePersistance().createQuery(requeteJPQL, Long.class);
        query.setParameter("login", intervenant.getLogin());

        Integer res = query.getSingleResult().intValue();

        return res;
    }
    
    public List<Soutien> findSoutiensByIntervenant(Intervenant intervenant) {
        String requeteJPQL = "SELECT s FROM Soutien s WHERE s.intervenant.login = :login AND s.dateFin IS NOT NULL";
        TypedQuery<Soutien> query = JpaUtil.obtenirContextePersistance().createQuery(requeteJPQL, Soutien.class);
        query.setParameter("login", intervenant.getLogin());

        List<Soutien> soutiens = query.getResultList();
        
        return soutiens;
    }
}
