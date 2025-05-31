/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ffos.skroflin.controller;

import ffos.skroflin.model.StudentPolica;
import ffos.skroflin.model.StudentProstorija;
import ffos.skroflin.model.dto.StudentPolicaDTO;
import ffos.skroflin.service.StudentKutijaService;
import ffos.skroflin.service.StudentPolicaService;
import ffos.skroflin.service.StudentProstorijaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author svenk
 */
@RestController
@RequestMapping("/api/skroflin/studentPolica")
public class StudentPolicaController {
    private final StudentPolicaService policaService;
    private final StudentKutijaService kutijaService;
    private final StudentProstorijaService prostorijaService;

    public StudentPolicaController(StudentPolicaService policaService, StudentKutijaService kutijaService, StudentProstorijaService prostorijaService) {
        this.policaService = policaService;
        this.kutijaService = kutijaService;
        this.prostorijaService = prostorijaService;
    }
    
    @GetMapping("/get")
    public ResponseEntity get(){
        try {
            return new ResponseEntity<>(policaService.getAll(), HttpStatus.OK);
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
                return new ResponseEntity<>("Šifra mora biti veća od 0" + " " + sifra, HttpStatus.BAD_REQUEST);
            }
            
            StudentPolica polica = policaService.getBySifra(sifra);
            if (polica == null) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            
            return new ResponseEntity<>(polica, HttpStatus.OK);
            
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping("/post")
    public ResponseEntity post(
            @RequestParam(required = true) StudentPolicaDTO dto
    ){
        try {
            if (dto == null) {
                return new ResponseEntity<>("Podaci nisu primljeni" + " ", HttpStatus.BAD_REQUEST);
            }
            
            System.out.println("---->" + dto.duzina() + ", " + dto.visina() + ", " + dto.visina());
            
            return new ResponseEntity<>(policaService.post(dto), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PatchMapping("/setStudentProstorija")
    public ResponseEntity setStudentProstorija(
            @RequestParam int sifraPolice,
            @RequestParam int sifraProstorije
    ){
        try {
            
            StudentPolica polica = policaService.getBySifra(sifraPolice);
            if(polica == null){
                return new ResponseEntity<>("Ne postoji polica s navedenom šifrom" +  " " + sifraPolice, HttpStatus.BAD_REQUEST);
            }
            
            StudentProstorija prostorija = prostorijaService.getBySifra(sifraProstorije);
            if (prostorija == null) {
                return new ResponseEntity<>("Ne postoji prostorija s navedenom šifrom" + " " + sifraProstorije, HttpStatus.BAD_REQUEST);
            }
            
            policaService.setStudentProstorija(prostorija, polica);
            return new ResponseEntity<>("Dodana polica u prostoriji", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PatchMapping("/removePolicaIzProstorije")
    public ResponseEntity<String> removePolicaIzProstorije(
            @RequestParam int sifraPolice
    ){
        try {
            
            StudentPolica polica = policaService.getBySifra(sifraPolice);
            if (polica == null) {
                return new ResponseEntity<>("Ne postoji polica s navedenom šifrom" + " " + sifraPolice, HttpStatus.BAD_REQUEST);
            }
            
            policaService.removePolicaIzProstorije(polica);
            return new ResponseEntity<>("Polica uklonjena iz prostorije!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
