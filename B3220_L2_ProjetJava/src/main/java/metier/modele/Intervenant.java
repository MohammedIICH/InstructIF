/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier.modele;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;

/**
 *
 * @author ehequet
 */
@Entity
@Inheritance (strategy = InheritanceType.JOINED)
public abstract class Intervenant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Boolean estDisponible;
    private String telephone;
    private String nom;
    private String prenom;
    @Column(nullable = false, unique = true)
    private String login;
    private int nivMin;
    private int nivMax;
    private int nbSoutiens;
    
    @OneToMany (mappedBy = "intervenant")
    private List<Soutien> soutiens;

    public Boolean getEstDisponible() {
        return estDisponible;
    }

    public int getNbSoutiens() {
        return nbSoutiens;
    }

    public void setSoutiens(List<Soutien> soutiens) {
        this.soutiens = soutiens;
    }

    public Long getId() {
        return id;
    }

    public void setNbSoutiens(int nbSoutiens) {
        this.nbSoutiens = nbSoutiens;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getLogin() {
        return login;
    }

    public int getNivMin() {
        return nivMin;
    }

    public int getNivMax() {
        return nivMax;
    }

    public void setEstDisponible(Boolean estDisponible) {
        this.estDisponible = estDisponible;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setNivMin(int nivMin) {
        this.nivMin = nivMin;
    }

    public void setNivMax(int nivMax) {
        this.nivMax = nivMax;
    }

    public Intervenant() {
    }

    public Intervenant(String telephone, String nom, String prenom, String login, int nivMin, int nivMax) {
        this.telephone = telephone;
        this.nom = nom;
        this.prenom = prenom;
        this.login = login;
        this.nivMin = nivMin;
        this.nivMax = nivMax;
        
        this.estDisponible = true;
        this.nbSoutiens = 0;
        this.soutiens = new ArrayList<>();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Intervenant other = (Intervenant) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
    public void addSoutien(Soutien s) {
        s.setIntervenant(this);
        this.soutiens.add(s);
        ++this.nbSoutiens;
    }

    public List<Soutien> getSoutiens() {
        return soutiens;
    }

    @Override
    public String toString() {
        return "Intervenant{" + "id=" + id + ", estDisponible=" + estDisponible + ", telephone=" + telephone + ", nom=" + nom + ", prenom=" + prenom + ", login=" + login + ", nivMin=" + nivMin + ", nivMax=" + nivMax + ", nbSoutiens=" + nbSoutiens + '}';
    }
    
    
    
}
