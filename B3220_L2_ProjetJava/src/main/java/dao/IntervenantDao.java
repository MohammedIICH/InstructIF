/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import javax.persistence.TypedQuery;
import metier.modele.Intervenant;

/**
 *
 * @author ehequet
 */
public class IntervenantDao {
    
    public void create(Intervenant intervenant) {
        JpaUtil.obtenirContextePersistance().persist(intervenant);
    }
    
    public Intervenant update(Intervenant intervenant) {
        return JpaUtil.obtenirContextePersistance().merge(intervenant);
    }
    
    public Intervenant findIntervenantDispo(int niv){
        Intervenant res = null;
        String requeteJPQL = "SELECT i FROM Intervenant i WHERE i.nivMax <= :niv AND i.nivMin >= :niv AND i.estDisponible = true ORDER BY i.nbSoutiens ASC";
        TypedQuery query = JpaUtil.obtenirContextePersistance().createQuery(requeteJPQL, Intervenant.class);
        query.setParameter("niv", niv);
        
        List<Intervenant> listIntervenants = query.getResultList();
                
        if (!listIntervenants.isEmpty()) {
            res = listIntervenants.get(0);
        }
        
        return res;
    }
        
    public Intervenant findIntervenantByLogin(String login) {
        String requeteJPQL = "SELECT i FROM Intervenant i WHERE i.login = :log";
        
        TypedQuery<Intervenant> query = JpaUtil.obtenirContextePersistance().createQuery(requeteJPQL, Intervenant.class);
        query.setParameter("log", login);
        
        return (Intervenant) query.getSingleResult();
    }
}
