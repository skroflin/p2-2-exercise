/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ffos.skroflin.service;

import ffos.skroflin.model.StudentKutija;
import ffos.skroflin.model.StudentPolica;
import ffos.skroflin.model.StudentProstorija;
import ffos.skroflin.model.dto.StudentPolicaDTO;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author Korisnik
 */
@Service
public class StudentPolicaService extends StudentService {

    public List<StudentPolica> getAll() {
        return session.createQuery("from student_polica", StudentPolica.class).list();
    }

    public StudentPolica getBySifra(int sifra) {
        return session.get(StudentPolica.class, sifra);
    }

    public StudentPolica post(StudentPolicaDTO o) {
        StudentPolica studentPolica = new StudentPolica(o.duzina(), o.sirina(), o.visina());
        session.beginTransaction();
        session.persist(studentPolica);
        session.getTransaction().commit();
        return studentPolica;
    }

    public void setStudentProstorija(StudentProstorija prostorija, StudentPolica polica) {
        polica.setStudentProstorija(prostorija);
        session.beginTransaction();
        session.merge(prostorija);
        session.getTransaction().commit();
    }

    public void removePolicaIzProstorije(StudentPolica polica) {
        polica.setStudentProstorija(null);
        session.beginTransaction();
        session.merge(polica);
        session.getTransaction().commit();
    }

    public List<StudentKutija> getKutijeNaPolici(StudentPolica polica) {
        return session.createQuery("from student_kutija where studentPolica = :polica", StudentKutija.class)
                .setParameter("polica", polica)
                .list();
    }

    public int getUkupnaSirinaPolice() {
        Long ukupno = session.createQuery("select sum(sirina) from student_polica", Long.class)
                .getSingleResult();
        return ukupno != null ? ukupno.intValue(): 0;
    }

    public List<StudentPolica> getPoliceUProstoriji(StudentProstorija prostorija) {
        return session.createQuery("from student_polica p join p.studentProstorija s where s = :prostorija", StudentPolica.class)
                .setParameter("prostorija", prostorija)
                .list();
    }
}
