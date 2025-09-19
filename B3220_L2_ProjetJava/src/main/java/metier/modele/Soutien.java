/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier.modele;

import java.util.Date;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

/**
 *
 * @author ehequet
 */
@Entity
public class Soutien {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateDemande;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateDebut;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateFin;
    private String description;
    private String bilan;
    private int niveauComprehension;
    
    @ManyToOne
    private Matiere matiere;
    
    @ManyToOne
    private Eleve eleve;
    
    @ManyToOne
    private Intervenant intervenant;

    public Soutien() {
    }

    public Soutien(Date dateDemande, Date dateDebut, String description, Matiere matiere, Eleve eleve) {
        this.dateDemande = dateDemande;
        this.dateDebut = dateDebut;
        this.description = description;
        this.matiere = matiere;
        this.eleve = eleve;
        
        this.dateFin = null;
        this.niveauComprehension = -1;
        this.intervenant = null;
    }

    public Date getDateDemande() {
        return dateDemande;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public String getDescription() {
        return description;
    }

    public String getBilan() {
        return bilan;
    }

    public int getNiveauComprehension() {
        return niveauComprehension;
    }

    public Matiere getMatiere() {
        return matiere;
    }

    public Eleve getEleve() {
        return eleve;
    }

    public Intervenant getIntervenant() {
        return intervenant;
    }

    public Long getId() {
        return id;
    }

    public void setDateDemande(Date dateDemande) {
        this.dateDemande = dateDemande;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setBilan(String bilan) {
        this.bilan = bilan;
    }

    public void setNiveauComprehension(int niveauComprehension) {
        this.niveauComprehension = niveauComprehension;
    }

    public void setMatiere(Matiere matiere) {
        this.matiere = matiere;
    }

    public void setEleve(Eleve eleve) {
        this.eleve = eleve;
    }

    public void setIntervenant(Intervenant intervenant) {
        this.intervenant = intervenant;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.id);
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
        final Soutien other = (Soutien) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Soutien{" + "dateDemande=" + dateDemande + ", dateDebut=" + dateDebut + ", dateFin=" + dateFin + ", description=" + description + ", bilan=" + bilan + ", niveauComprehension=" + niveauComprehension + ", matiere=" + matiere + ", eleve=" + eleve + ", intervenant=" + intervenant + '}';
    }
    
}
