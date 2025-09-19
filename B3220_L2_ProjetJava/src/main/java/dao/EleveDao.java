/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import javax.persistence.TypedQuery;
import metier.modele.Eleve;

/**
 *
 * @author ehequet
 */
public class EleveDao {
    
    public void create(Eleve eleve) {
        JpaUtil.obtenirContextePersistance().persist(eleve);
    }
    
    public Eleve update(Eleve eleve) {
        return JpaUtil.obtenirContextePersistance().merge(eleve);
    }
    
    public Eleve findEleveByMail(String mail) {
        String requeteJPQL = "SELECT e FROM Eleve e WHERE e.mail = :mail";
        
        TypedQuery<Eleve> query = JpaUtil.obtenirContextePersistance().createQuery(requeteJPQL, Eleve.class);
        query.setParameter("mail", mail);
        
        return (Eleve) query.getSingleResult();
    }
    
}
