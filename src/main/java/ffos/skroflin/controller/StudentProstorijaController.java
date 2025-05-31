/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ffos.skroflin.controller;

import ffos.skroflin.model.StudentPolica;
import ffos.skroflin.model.StudentProstorija;
import ffos.skroflin.model.dto.StudentProstorijaDTO;
import ffos.skroflin.service.StudentPolicaService;
import ffos.skroflin.service.StudentProstorijaService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author svenk
 */
@RestController
@RequestMapping("/api/skroflin/studentProstorija")
public class StudentProstorijaController {
    private final StudentProstorijaService prostorijaService;
    private final StudentPolicaService policaService;

    public StudentProstorijaController(StudentProstorijaService prostorijaService, StudentPolicaService policaService) {
        this.prostorijaService = prostorijaService;
        this.policaService = policaService;
    }
    
    @GetMapping("/get")
    public ResponseEntity get(){
        try {
            return new ResponseEntity<>(prostorijaService.getAll(), HttpStatus.FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/getBySifra")
    public ResponseEntity getBySifra(
            @RequestParam int sifra
    ){
        try {
            if (sifra <= 0) {
                return new ResponseEntity<>("Šifra mora biti veća od 0!" +  " " + sifra, HttpStatus.BAD_REQUEST);
            }
            
            StudentProstorija prostorija = prostorijaService.getBySifra(sifra);
            if (prostorija == null) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/getPoliceUProstoriji")
    public ResponseEntity getPoliceUProstoriji(
            @RequestParam int sifra
    ){
        try {
            if (sifra <= 0) {
                return new ResponseEntity("Šifra prostorije ne smije biti manja o 0!" + " " + sifra, HttpStatus.BAD_REQUEST);
            }
            
            StudentProstorija prostorija = prostorijaService.getBySifra(sifra);
            if (prostorija == null) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            
            List<StudentPolica> police = policaService.getPoliceUProstoriji(prostorija);
            if (police.isEmpty()) {
                return new ResponseEntity<>("Nema polica u ovoj prostoriji!", HttpStatus.NO_CONTENT);
            }
            
            return new ResponseEntity<>(police, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/getProstorPoKabinetu")
    public ResponseEntity getProstorPoKabinetu(
            @RequestParam boolean kabinet
    ){
        try {
            List<StudentProstorija> prostorije = prostorijaService.getProstorPoKabinetu(kabinet);
            
            if (prostorije.isEmpty()) {
                return new ResponseEntity<>("Nema prostorije s navedenim kriterijima!", HttpStatus.NO_CONTENT);
            }
            
            return new ResponseEntity<>(prostorije, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping("/post")
    public ResponseEntity post(
            @RequestBody(required = true) StudentProstorijaDTO dto
    ){
        try {
            if (dto == null) {
                return new ResponseEntity<>("podaci nisu primljeni", HttpStatus.BAD_REQUEST);
            }
            System.out.println("---->" + dto.naziv() + " " + dto.kabinet());
            
            if (dto.naziv() == null || dto.naziv().isEmpty()) {
                return new ResponseEntity<>("Naziv je obavezan!", HttpStatus.BAD_REQUEST);
            }
            
            return new ResponseEntity<>(prostorijaService.post(dto), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
