/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ffos.skroflin.controller;

import ffos.skroflin.model.StudentKutija;
import ffos.skroflin.model.StudentPolica;
import ffos.skroflin.service.StudentKutijaService;
import ffos.skroflin.service.StudentPolicaService;
import ffos.skroflin.service.StudentProstorijaService;
import java.math.BigDecimal;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author svenk
 */
public class StudentKutijaController {

    private final StudentKutijaService kutijaService;
    private final StudentPolicaService policaService;
    private final StudentProstorijaService prostorijaService;

    public StudentKutijaController(StudentKutijaService kutijaService, StudentPolicaService policaService, StudentProstorijaService prostorijaService) {
        this.kutijaService = kutijaService;
        this.policaService = policaService;
        this.prostorijaService = prostorijaService;
    }

    @GetMapping("/get")
    public ResponseEntity get() {
        try {
            return new ResponseEntity<>(kutijaService.getAll(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getBySifra")
    public ResponseEntity getBySifra(
            @RequestParam int sifra
    ) {
        try {
            if (sifra <= 0) {
                return new ResponseEntity<>("Šifra mora biti veća od 0" + " " + sifra, HttpStatus.BAD_REQUEST);
            }

            StudentKutija kutija = kutijaService.getBySifra(sifra);
            if (kutija == null) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(kutija, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/setPolica")
    public ResponseEntity<String> setPolica(
            @RequestParam int policaSifra,
            @RequestParam int kutijaSifra
    ) {
        try {
            if (policaSifra <= 0 || kutijaSifra <= 0) {
                return new ResponseEntity<>("Šifre moraju biti veće od 0", HttpStatus.BAD_REQUEST);
            }

            StudentKutija kutija = kutijaService.getBySifra(kutijaSifra);
            if (kutija == null) {
                return new ResponseEntity<>("Ne postoji Kutija s navedenom šifrom" + " " + kutijaSifra, HttpStatus.BAD_REQUEST);
            }

            StudentPolica polica = policaService.getBySifra(policaSifra);
            if (polica == null) {
                return new ResponseEntity<>("Ne postoji Polica s navedenom šifrom" + " " + policaSifra, HttpStatus.BAD_REQUEST);
            }

            kutijaService.setPolica(kutija, polica);
            return new ResponseEntity<>("Dodana kutija na policu", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getPolicaByOznakaKutije")
    public ResponseEntity getPolicaByOznakaKutije(
            @RequestParam String oznaka
    ) {
        try {
            StudentPolica polica = kutijaService.getPolicaByOznakaKutije(oznaka);
            if (polica == null) {
                return new ResponseEntity<>("Kutija s navedenom oznakom nije pronađena", HttpStatus.BAD_REQUEST);
            }
            
            return new ResponseEntity<>(polica, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping("/dodajKutije")
    public ResponseEntity dodajKutije(
            @RequestParam BigDecimal obujam,
            @RequestParam int brojKutija
    ){
        try {
            if (brojKutija <= 0) {
                return new ResponseEntity<>("Broj kutija mora biti veći od 0" + " " + brojKutija, HttpStatus.BAD_REQUEST);
            }
            kutijaService.dodajKutije(obujam, brojKutija);
            return new ResponseEntity<>("Uspješno dodane kutije", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PatchMapping("/removeKutijaSaPolice")
    public ResponseEntity<String> removeKutijaSaPolice(
            @RequestParam int sifraKutije
    ){
        try {
            StudentKutija kutija = kutijaService.getBySifra(sifraKutije);
            if (kutija == null) {
                return new ResponseEntity<>("Ne postoji kutija s navedenom šifrom" + " " + sifraKutije, HttpStatus.BAD_REQUEST);
            }
            
            kutijaService.removeKutijasaPolice(kutija);
            return new ResponseEntity<>("Kutija uspješno skinuta s police", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
