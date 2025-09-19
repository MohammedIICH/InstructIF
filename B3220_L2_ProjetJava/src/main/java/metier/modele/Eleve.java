/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier.modele;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;

/**
 *
 * @author ehequet
 */
@Entity
public class Eleve {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, unique = true)
    private String mail;
    private int classe;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateNaissance;
    private String motDePasse;
    private String nom;
    private String prenom;
    @ManyToOne
    private Etablissement etablissement;
    @OneToMany (mappedBy = "eleve")
    private List<Soutien> soutiens;

    public Eleve() {
    }

    public Eleve(String mail, int classe, Date dateNaissance, String motDePasse, String nom, String prenom) {
        this.mail = mail;
        this.classe = classe;
        this.dateNaissance = dateNaissance;
        this.motDePasse = motDePasse;
        this.nom = nom;
        this.prenom = prenom;
        
        this.soutiens = new ArrayList<Soutien>();
    }

    public Long getId() {
        return id;
    }
    
    public String getMail() {
        return mail;
    }

    public int getClasse() {
        return classe;
    }

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setClasse(int classe) {
        this.classe = classe;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Etablissement getEtablissement() {
        return etablissement;
    }

    public void setEtablissement(Etablissement etablissement) {
        this.etablissement = etablissement;
    }

    @Override
    public String toString() {
        return "Eleve{" + "id=" + id + ", mail=" + mail + ", classe=" + classe + ", dateNaissance=" + dateNaissance + ", motDePasse=" + motDePasse + ", nom=" + nom + ", prenom=" + prenom + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
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
        final Eleve other = (Eleve) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
    public void addSoutien(Soutien s) {
        this.soutiens.add(s);
        if (s.getEleve() != this) {
            s.setEleve(this);
        }
    }

    public List<Soutien> getSoutiens() {
        return soutiens;
    }
    
}
