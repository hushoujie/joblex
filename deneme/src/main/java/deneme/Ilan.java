package deneme;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.validation.constraints.Future;
import javax.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Ilan implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int kod;

    private String uzman;

    private boolean durum;

    @Size(min=5, max=200, message="uzunluk 5 ile 200 arasında olmalı")
    private String baslik;

    @Size(min=5, max=200, message="uzunluk 5 ile 200 arasında olmalı")
    private String tanim;

    @Size(min=5, max=200, message="uzunluk 5 ile 200 arasında olmalı")
    private String ozellikler;

    @Future(message="gelecekte olmalı")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date aktivasyon;

    @Future(message="gelecekte olmalı")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date kapanma;

    public int getKod() {
        return kod;
    }

    public void setKod(int kod) {
        this.kod = kod;
    }

    public String getUzman() {
        return uzman;
    }

    public void setUzman(String uzman) {
        this.uzman = uzman;
    }

    public boolean isDurum() {
        return durum;
    }

    public void setDurum(boolean durum) {
        this.durum = durum;
    }

    public String getBaslik() {
        return baslik;
    }

    public void setBaslik(String baslik) {
        this.baslik = baslik;
    }

    public String getTanim() {
        return tanim;
    }

    public void setTanim(String tanim) {
        this.tanim = tanim;
    }

    public String getOzellikler() {
        return ozellikler;
    }

    public void setOzellikler(String ozellikler) {
        this.ozellikler = ozellikler;
    }

    public Date getAktivasyon() {
        return aktivasyon;
    }

    public void setAktivasyon(Date aktivasyon) {
        this.aktivasyon = aktivasyon;
    }

    public Date getKapanma() {
        return kapanma;
    }

    public void setKapanma(Date kapanma) {
        this.kapanma = kapanma;
    }

}
