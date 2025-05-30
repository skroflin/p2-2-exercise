/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ffos.skroflin.service;

import ffos.skroflin.model.StudentProstorija;
import java.util.List;

/**
 *
 * @author Korisnik
 */
public class StudentProstorijaService extends StudentService{
    public List<StudentProstorija> getAll(){
        return session.createQuery("from student_prostorija", StudentProstorija.class).list();
    }
    
    public StudentProstorija getBySifra(int sifra){
        return session.get(StudentProstorija.class, sifra);
    }
}
