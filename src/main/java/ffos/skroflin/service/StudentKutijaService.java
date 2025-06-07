/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ffos.skroflin.service;

import ffos.skroflin.model.StudentKutija;
import ffos.skroflin.model.StudentPolica;
import ffos.skroflin.model.dto.StudentKutijaDTO;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;
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
        session.persist(studentKutija);
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
        session.getTransaction().commit();
    }
    
    public void dodajKutije(BigDecimal obujam, int brojKutija){
        session.beginTransaction();
        for (int i = 0; i < brojKutija; i++) {
            StudentKutija kutija = new StudentKutija(new Date(), obujam, "Oznaka" + " " + UUID.randomUUID());
            session.persist(kutija);
        }
        session.getTransaction().commit();
    }
    
    public StudentPolica getPolicaByOznakaKutije(String oznaka){
        StudentKutija kutija = session.createQuery("select * from student_kutija where oznakaKutij like :oznaka", StudentKutija.class)
                .setParameter("oznaka", "%" + oznaka + "%")
                .getSingleResult();
        return kutija.getStudentPolica();
    }
}
