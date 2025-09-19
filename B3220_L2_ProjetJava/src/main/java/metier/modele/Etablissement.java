/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier.modele;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author ehequet
 */
@Entity
public class Etablissement {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Double lat;
    private Double lng;
    @Column(nullable = false, unique = true)
    private String uai;
    private String nom;
    private String secteur;
    private String codeInseeCommune;
    private String nomCommune;
    private String codeDepartement;
    private String departement;
    private String academie;
    private String ips;

    

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public void setUai(String uai) {
        this.uai = uai;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setSecteur(String secteur) {
        this.secteur = secteur;
    }

    public void setCodeInseeCommune(String codeInseeCommune) {
        this.codeInseeCommune = codeInseeCommune;
    }

    public void setNomCommune(String nomCommune) {
        this.nomCommune = nomCommune;
    }

    public void setCodeDepartement(String codeDepartement) {
        this.codeDepartement = codeDepartement;
    }

    public void setDepartement(String departement) {
        this.departement = departement;
    }

    public void setAcademie(String academie) {
        this.academie = academie;
    }

    public void setIps(String ips) {
        this.ips = ips;
    }

    public Long getId() {
        return id;
    }

    public String getUai() {
        return uai;
    }

    public String getNom() {
        return nom;
    }

    public String getSecteur() {
        return secteur;
    }

    public String getCodeInseeCommune() {
        return codeInseeCommune;
    }

    public String getNomCommune() {
        return nomCommune;
    }

    public String getCodeDepartement() {
        return codeDepartement;
    }

    public String getDepartement() {
        return departement;
    }

    public String getAcademie() {
        return academie;
    }

    public String getIps() {
        return ips;
    }

    public Etablissement() {
    }

    public Etablissement(Double lat, Double lng, String uai, String nom, String secteur, String codeInseeCommune, String nomCommune, String codeDepartement, String departement, String academie, String ips) {
        this.lat = lat;
        this.lng = lng;
        this.uai = uai;
        this.nom = nom;
        this.secteur = secteur;
        this.codeInseeCommune = codeInseeCommune;
        this.nomCommune = nomCommune;
        this.codeDepartement = codeDepartement;
        this.departement = departement;
        this.academie = academie;
        this.ips = ips;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.id);
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
        final Etablissement other = (Etablissement) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Etablissement{" + "id=" + id + ", lat=" + lat + ", lng=" + lng + ", uai=" + uai + ", nom=" + nom + ", secteur=" + secteur + ", codeInseeCommune=" + codeInseeCommune + ", nomCommune=" + nomCommune + ", codeDepartement=" + codeDepartement + ", departement=" + departement + ", academie=" + academie + ", ips=" + ips + '}';
    }

   
}
