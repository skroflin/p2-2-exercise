/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ffos.skroflin.service;

import ffos.skroflin.model.StudentKutija;
import ffos.skroflin.model.StudentPolica;
import ffos.skroflin.model.dto.StudentKutijaDTO;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author Korisnik
 */
@Service
public class StudentKutijaService extends StudentService{
    public List<StudentKutija> getAll(){
        return session.createQuery("from student_kutija", StudentKutija.class).list();
    }
    
    public StudentKutija getBySifra(int sifra){
        return session.get(StudentKutija.class, sifra);
    }
    
    public StudentKutija post(StudentKutijaDTO o){
        StudentKutija studentKutija = new StudentKutija(o.datumPohrane(), o.obujam(), o.oznakaKutije());
        session.beginTransaction();
        session.persist(o);
        session.getTransaction().commit();
        return studentKutija;
    }
    
    public void setPolica(StudentKutija kutija, StudentPolica polica){
        kutija.setStudentPolica(polica);
        session.beginTransaction();
        session.merge(kutija);
        session.getTransaction().commit();
    }
    
    public void removeKutijasaPolice(StudentKutija kutija){
        kutija.setStudentPolica(null);
        session.beginTransaction();
        session.merge(kutija);
        session.beginTransaction().commit();
    }
}
