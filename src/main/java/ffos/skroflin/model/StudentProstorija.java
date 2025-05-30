/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ffos.skroflin.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

/**
 *
 * @author Korisnik
 */
@Entity(name = "student_prostorija")
public class StudentProstorija extends Entitet{
    private boolean kabinet;
    private String naziv;

    @Column(nullable = false)
    public boolean isKabinet() {
        return kabinet;
    }

    public void setKabinet(boolean kabinet) {
        this.kabinet = kabinet;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }
    
}
