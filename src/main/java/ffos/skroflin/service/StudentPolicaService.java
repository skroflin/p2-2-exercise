/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ffos.skroflin.service;

import ffos.skroflin.model.StudentPolica;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author Korisnik
 */

@Service
public class StudentPolicaService extends StudentService{
    public List<StudentPolica> getAll(){
        return session.createQuery("from student_polica", StudentPolica.class).list();
    }
    
    public StudentPolica getBySifra(int sifra){
        return session.get(StudentPolica.class, sifra);
    }
}
