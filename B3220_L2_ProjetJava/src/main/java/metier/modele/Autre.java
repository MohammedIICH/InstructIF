/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier.modele;

import javax.persistence.Entity;

/**
 *
 * @author ehequet
 */
@Entity
public class Autre extends Intervenant{
    
    private String activite;

    public Autre() {
    }

    public Autre(String activite,  String telephone, String nom, String prenom, String login, int nivMin, int nivMax) {
        super(telephone, nom, prenom, login, nivMin, nivMax);
        this.activite = activite;
    }

    public String getActivite() {
        return activite;
    }

    public void setActivite(String activite) {
        this.activite = activite;
    }

    @Override
    public String toString() {
        return super.toString() +  " Autre{" + "activite=" + activite + '}';
    }
    
    
    
}
