package main.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Basvuru implements Serializable, Comparable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer kod;

    @ManyToOne
    @JoinColumn(name = "aday")
    private Aday aday;

    @ManyToOne
    @JoinColumn(name = "ilan")
    private Ilan ilan;

    private int durum;

    @Override
    public int compareTo(Object o) {
        return this.kod.compareTo(((Basvuru) o).getKod());
    }

    public Integer getKod() {
        return kod;
    }

    public void setKod(Integer kod) {
        this.kod = kod;
    }

    public Aday getAday() {
        return aday;
    }

    public void setAday(Aday aday) {
        this.aday = aday;
    }

    public Ilan getIlan() {
        return ilan;
    }

    public void setIlan(Ilan ilan) {
        this.ilan = ilan;
    }

    public int getDurum() {
        return durum;
    }

    public void setDurum(int durum) {
        this.durum = durum;
    }

}
