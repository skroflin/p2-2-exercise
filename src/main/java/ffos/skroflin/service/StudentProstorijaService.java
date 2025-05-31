/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ffos.skroflin.service;

import ffos.skroflin.model.StudentPolica;
import ffos.skroflin.model.StudentProstorija;
import ffos.skroflin.model.dto.StudentProstorijaDTO;
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
    
    public StudentProstorija post(StudentProstorijaDTO o){
        StudentProstorija studentProstorija = new StudentProstorija(o.kabinet(), o.naziv());
        session.beginTransaction();
        session.persist(o);
        session.getTransaction().commit();
        return studentProstorija;
    }
    
    public List<StudentPolica> getPoliceUProstoriji(StudentProstorija prostorija){
        return session.createQuery("from student_polica p join p.studentProstorija s where s = :prostorija", StudentPolica.class)
                .setParameter("prostorija", prostorija)
                .list();
    }
    
    public List<StudentProstorija> getProstorPoKabinetu(boolean jeKabinet){
        return session.createQuery("from student_prostorija where kabinet = :kabinet", StudentProstorija.class)
                .setParameter("kabinet", jeKabinet)
                .list();
    }
}
