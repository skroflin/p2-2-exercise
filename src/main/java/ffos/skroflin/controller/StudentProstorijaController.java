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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Student -> Prostorija", description = "Dostupne rute za entitet StudentProstorija")
@RestController
@RequestMapping("/api/skroflin/studentProstorija")
public class StudentProstorijaController {

    private final StudentProstorijaService prostorijaService;
    private final StudentPolicaService policaService;

    public StudentProstorijaController(StudentProstorijaService prostorijaService, StudentPolicaService policaService) {
        this.prostorijaService = prostorijaService;
        this.policaService = policaService;
    }

    @Operation(
            summary = "Dohvaća sve prostorije", tags = {"get", "prostorija"},
            description = "Dohvaća sve prostorije sa svim podacima"
    )
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = StudentProstorija.class)))),
                @ApiResponse(responseCode = "500", description = "Interna pogreška servera", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
            })

    @GetMapping("/get")
    public ResponseEntity get() {
        try {
            return new ResponseEntity<>(prostorijaService.getAll(), HttpStatus.FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Dohvaća prostoriju po šifri",
            description = "Dohvaća prostoriju po danoj šifri sa svim podacima. "
            + "Ukoliko ne postoji prostorija za danu šifru vraća prazan odgovor",
            tags = {"prostorija", "getBy"},
            parameters = {
                @Parameter(
                        name = "sifra",
                        allowEmptyValue = false,
                        required = true,
                        description = "Primarni ključ prostorije u bazi podataka, mora biti veći od nula",
                        example = "2"
                )})
    @ApiResponses({
        @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = StudentProstorija.class), mediaType = "application/json")),
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
                return new ResponseEntity<>("Šifra mora biti veća od 0!" + " " + sifra, HttpStatus.BAD_REQUEST);
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

    @Operation(
            summary = "Dohvaća police u prostoriji",
            description = "Dohvaća police u prostoriji po danoj šifri sa svim podacima o policama u navedenoj prostoriji. "
            + "Ukoliko ne postoji polica za danu šifru vraća prazan odgovor",
            tags = {"polica", "prostorija", "getBy"},
            parameters = {
                @Parameter(
                        name = "sifra",
                        allowEmptyValue = false,
                        required = true,
                        description = "Primarni ključ police u bazi podataka, mora biti veći od nula",
                        example = "2"
                )})
    @ApiResponses({
        @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = StudentProstorija.class), mediaType = "application/json")),
        @ApiResponse(responseCode = "204", description = "Ne postoji kolegij za danu šifru"),
        @ApiResponse(responseCode = "400", description = "Šifra mora biti veća od nula", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "500", description = "Interna pogreška servera", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
    })

    @GetMapping("/getPoliceUProstoriji")
    public ResponseEntity getPoliceUProstoriji(
            @RequestParam int sifra
    ) {
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

    @Operation(
            summary = "Dohvaća prostor po kabinetu",
            description = "Dohvaća prostor po danom kabinetu sa svim podacima o prostoriji. "
            + "Ukoliko ne postoji prostor za dan kabinet, vraća prazan odgovor",
            tags = {"prostor", "getBy"},
            parameters = {
                @Parameter(
                        name = "sifra",
                        allowEmptyValue = false,
                        required = true,
                        description = "Primarni ključ police u bazi podataka, mora biti veći od nula",
                        example = "2"
                )})
    @ApiResponses({
        @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = StudentProstorija.class), mediaType = "application/json")),
        @ApiResponse(responseCode = "204", description = "Ne postoji kolegij za danu šifru"),
        @ApiResponse(responseCode = "400", description = "Šifra mora biti veća od nula", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "500", description = "Interna pogreška servera", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
    })

    @GetMapping("/getProstorPoKabinetu")
    public ResponseEntity getProstorPoKabinetu(
            @RequestParam boolean kabinet
    ) {
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

    @Operation(
            summary = "Kreira novu prostoriju",
            tags = {"post", "prostorija"},
            description = "Kreira novu prostoriju. Kabinet obavezan")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Kreirano", content = @Content(schema = @Schema(implementation = StudentProstorija.class), mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Loš zahtjev (nije primljen dto objekt ili ne postoji ime ili prezime ili jmbag)", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "500", description = "Interna pogreška servera", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
    })

    @PostMapping("/post")
    public ResponseEntity post(
            @RequestBody(required = true) StudentProstorijaDTO dto
    ) {
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
