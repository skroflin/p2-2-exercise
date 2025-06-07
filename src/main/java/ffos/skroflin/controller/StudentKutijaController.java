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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.math.BigDecimal;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@Tag(name = "Student -> Kutija", description = "Dostupne rute za entitet StudentKutija")
@RestController
@RequestMapping("/api/skroflin/studentKutija")
public class StudentKutijaController {

    private final StudentKutijaService kutijaService;
    private final StudentPolicaService policaService;
    private final StudentProstorijaService prostorijaService;

    public StudentKutijaController(StudentKutijaService kutijaService, StudentPolicaService policaService, StudentProstorijaService prostorijaService) {
        this.kutijaService = kutijaService;
        this.policaService = policaService;
        this.prostorijaService = prostorijaService;
    }

    @Operation(
            summary = "Dohvaća sve kutija", tags = {"get", "kutija"},
            description = "Dohvaća sve kutije sa svim podacima"
    )
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = StudentKutija.class)))),
                @ApiResponse(responseCode = "500", description = "Interna pogreška servera", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
            })

    @GetMapping("/get")
    public ResponseEntity get() {
        try {
            return new ResponseEntity<>(kutijaService.getAll(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Dohvaća kutiju po šifri",
            description = "Dohvaća kutiju po danoj šifri sa svim podacima. "
            + "Ukoliko ne postoji kutija za danu šifru vraća prazan odgovor",
            tags = {"kutija", "getBy"},
            parameters = {
                @Parameter(
                        name = "sifra",
                        allowEmptyValue = false,
                        required = true,
                        description = "Primarni ključ kutije u bazi podataka, mora biti veći od nula",
                        example = "2"
                )})
    @ApiResponses({
        @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = StudentKutija.class), mediaType = "application/json")),
        @ApiResponse(responseCode = "204", description = "Ne postoji student za danu šifru", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "400", description = "Šifra mora biti veća od nula", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "500", description = "Interna pogreška servera", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
    })

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

    @Operation(
            summary = "Postavlja kutiju na policu",
            tags = {"kutija", "polica", "patch"},
            description = "Postavlja kutiju polici. Šifra kutije i police obavezne",
            parameters = {
                @Parameter(
                        name = "policaSifra",
                        allowEmptyValue = false,
                        required = true,
                        description = "Primarni ključ police u bazi podataka, mora biti veći od nula",
                        example = "2"
                ),
                @Parameter(
                        name = "kutijaSifra",
                        allowEmptyValue = false,
                        required = true,
                        description = "Primarni ključ kutije u bazi podataka, mora biti veći od nula",
                        example = "2"
                )
            }
    )
    
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

    @Operation(
            summary = "Dohvaća police po oznaci kutije",
            description = "Dohvaća police po uvjetu vrijednosti oznake s kutije sa svim podacima. ",
            tags = {"student", "getBy",},
            parameters = {
                @Parameter(
                        name = "oznaka",
                        allowEmptyValue = false,
                        required = true,
                        description = "Oznaka kutije",
                        example = "ern"
                )})
    @ApiResponses({
        @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = StudentKutija.class)))),
        @ApiResponse(responseCode = "400", description = "Uvjet, stranica i po stranici moraju biti mora biti postavljen", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "500", description = "Interna pogreška servera", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
    })
    
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

    @Operation(
            summary = "Kreira nove kutije sa svojim obujmom.",
            tags = {"post", "student"},
            description = "Kreira onoliko kutija koliko primi kroz parametar sa obujmom ", parameters = {
                @Parameter(
                        name = "broj",
                        allowEmptyValue = false,
                        required = true,
                        description = "Broj kutija koji će biti kreirani.",
                        example = "10"
                ),
                @Parameter(
                        name = "obujam",
                        allowEmptyValue = false,
                        required = true,
                        description = "Obujam s kojim će kutije biti napravljene.",
                        example = "10"
                )  
            })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Kreirano", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "400", description = "Loš zahtjev (nije primljen broj koliko studenata treba dodati)", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "500", description = "Interna pogreška servera", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
    })
    
    @PostMapping("/dodajKutije")
    public ResponseEntity dodajKutije(
            @RequestParam BigDecimal obujam,
            @RequestParam int brojKutija
    ) {
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
    
    @Operation(
            summary = "Ukloni kutiju s police",
            tags = {"kutija", "polica", "delete"},
            description = "Uklanja kutiju s police. Šifra kutije obavezne. Ukoliko kutija ne postoji na polici, neće biti obrisana",
            parameters = {
                @Parameter(
                        name = "kutijaSifra",
                        allowEmptyValue = false,
                        required = true,
                        description = "Primarni ključ kutije u bazi podataka, mora biti veći od nula",
                        example = "2"
                )
            }
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Profesor postavljen na kolegij", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "400", description = "Loš zahtjev (nisu primljene šifre dobre ili ne postoji student/profesor po tim šiframa)", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "500", description = "Interna pogreška servera", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
    })
    
    @DeleteMapping("/removeKutijaSaPolice")
    public ResponseEntity<String> removeKutijaSaPolice(
            @RequestParam int sifraKutije
    ) {
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
