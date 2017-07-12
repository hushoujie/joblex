package main.domains;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Basvuru implements Serializable, Comparable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer kod;

    private String aday;

    private int ilan;

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

    public String getAday() {
        return aday;
    }

    public void setAday(String aday) {
        this.aday = aday;
    }

    public int getIlan() {
        return ilan;
    }

    public void setIlan(int ilan) {
        this.ilan = ilan;
    }

    public int getDurum() {
        return durum;
    }

    public void setDurum(int durum) {
        this.durum = durum;
    }

}
