/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ffos.skroflin.service;

import ffos.skroflin.model.StudentKutija;
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
}
