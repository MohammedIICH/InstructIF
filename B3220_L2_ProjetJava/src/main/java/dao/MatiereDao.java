/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import javax.persistence.TypedQuery;
import metier.modele.Matiere;


/**
 *
 * @author ehequet
 */
public class MatiereDao {
    
    public void create(Matiere matiere) {
        JpaUtil.obtenirContextePersistance().persist(matiere);
    }
    
    public List<Matiere> findAll() {
        String requeteJPQL = "SELECT m FROM Matiere m ORDER BY m.nom ASC";
        TypedQuery query = JpaUtil.obtenirContextePersistance().createQuery(requeteJPQL, Matiere.class);
        return query.getResultList();
    }
    
    public Matiere findById(Long idMatiere) {
        return JpaUtil.obtenirContextePersistance().find(Matiere.class, idMatiere);
    }
}
