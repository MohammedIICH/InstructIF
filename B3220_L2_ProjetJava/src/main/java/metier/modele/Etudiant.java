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
public class Etudiant extends Intervenant{
    
    private String universite;
    private String specialite;

    public Etudiant() {
    }

    public Etudiant(String universite, String specialite, String telephone, String nom, String prenom, String login, int nivMin, int nivMax) {
        super(telephone, nom, prenom, login, nivMin, nivMax);
        this.universite = universite;
        this.specialite = specialite;
    }

    public String getUniversite() {
        return universite;
    }

    public String getSpecialite() {
        return specialite;
    }

    public void setUniversite(String universite) {
        this.universite = universite;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }
    
    
}
