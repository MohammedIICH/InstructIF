/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import javax.persistence.TypedQuery;
import metier.modele.Etablissement;

/**
 *
 * @author ehequet
 */
public class EtablissementDao {
    
    public void create(Etablissement etablissement) {
        JpaUtil.obtenirContextePersistance().persist(etablissement);
    }
    
    /**
     * Trouve l'établissement identifié par l'uai passé en paramètre, null sinon
     * 
     * On utilise un try/catch ici et le renvoie d'un null en cas d'échec pour éviter de surcharger le code du service d'inscription d'un eleve.
     * @param uai
     * @return Etablissement|null
     */
    public Etablissement findByUai(String uai) {
        String requeteJPQL = "SELECT e FROM Etablissement e WHERE e.uai = :uai";
        
        TypedQuery<Etablissement> query = JpaUtil.obtenirContextePersistance().createQuery(requeteJPQL, Etablissement.class);
        query.setParameter("uai", uai);
        
        try {
            return (Etablissement) query.getSingleResult();
        } catch( Exception ex) {
            return null;
        }
    }
}
