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
public class Enseignant  extends Intervenant{
    private String typeEtablissement;

    public Enseignant() {
    }

    public Enseignant(String typeEtablissement, String telephone, String nom, String prenom, String login, int nivMin, int nivMax) {
        super(telephone, nom, prenom, login, nivMin, nivMax);
        this.typeEtablissement = typeEtablissement;
    }

    public String getTypeEtablissement() {
        return typeEtablissement;
    }

    public void setTypeEtablissement(String typeEtablissement) {
        this.typeEtablissement = typeEtablissement;
    }
    
}
