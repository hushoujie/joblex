package main.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Aday implements Serializable, Comparable {

    private String country;

    private String email;

    private String firstname;

    private String headline;

    @Id
    private String id;

    private String industry;

    private boolean karaliste;

    private String karalistesebebi;

    private String lastname;

    private String location;

    private String summary;

    @OneToMany(mappedBy = "aday", cascade = CascadeType.ALL)
    private List<Pozisyon> pozisyonlar;

    @OneToMany(mappedBy = "aday", cascade = CascadeType.ALL)
    private List<Basvuru> basvurular;

    @Override
    public int compareTo(Object o) {
        return this.id.compareTo(((Aday) o).getId());
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public boolean isKaraliste() {
        return karaliste;
    }

    public void setKaraliste(boolean karaliste) {
        this.karaliste = karaliste;
    }

    public String getKaralistesebebi() {
        return karalistesebebi;
    }

    public void setKaralistesebebi(String karalistesebebi) {
        this.karalistesebebi = karalistesebebi;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<Pozisyon> getPozisyonlar() {
        return pozisyonlar;
    }

    public void setPozisyonlar(List<Pozisyon> pozisyonlar) {
        this.pozisyonlar = pozisyonlar;
    }

    public List<Basvuru> getBasvurular() {
        return basvurular;
    }

    public void setBasvurular(List<Basvuru> basvurular) {
        this.basvurular = basvurular;
    }

}
