/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ffos.skroflin.controller;

import ffos.skroflin.model.StudentKutija;
import ffos.skroflin.model.StudentPolica;
import ffos.skroflin.model.StudentProstorija;
import ffos.skroflin.model.dto.StudentPolicaDTO;
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
import java.util.List;
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
@Tag(name = "Student -> Polica", description = "Dostupne rute za entitet StudentPolica")
@RestController
@RequestMapping("/api/skroflin/studentPolica")
public class StudentPolicaController {

    private final StudentPolicaService policaService;
    private final StudentKutijaService kutijaService;
    private final StudentProstorijaService prostorijaService;

    public StudentPolicaController(
            StudentPolicaService policaService,
            StudentKutijaService kutijaService,
            StudentProstorijaService prostorijaService) {
        this.policaService = policaService;
        this.kutijaService = kutijaService;
        this.prostorijaService = prostorijaService;
    }

    @Operation(
            summary = "Dohvaća sve police", tags = {"get", "polica"},
            description = "Dohvaća sve police sa svim podacima."
    )
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = StudentPolica.class)))),
                @ApiResponse(responseCode = "500", description = "Interna pogreška servera", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
            })

    @GetMapping("/get")
    public ResponseEntity get() {
        try {
            return new ResponseEntity<>(policaService.getAll(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Dohvaća policu po šifri",
            description = "Dohvaća policu po danoj šifri sa svim podacima o polici. "
            + "Ukoliko ne postoji polica za danu šifru vraća prazan odgovor",
            tags = {"polica", "getBy"},
            parameters = {
                @Parameter(
                        name = "sifra",
                        allowEmptyValue = false,
                        required = true,
                        description = "Primarni ključ kolegija u bazi podataka, mora biti veći od nula",
                        example = "2"
                )})
    @ApiResponses({
        @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = StudentPolica.class), mediaType = "application/json")),
        @ApiResponse(responseCode = "204", description = "Ne postoji polica za danu šifru"),
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

            StudentPolica polica = policaService.getBySifra(sifra);
            if (polica == null) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(polica, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Kreira novu policu",
            tags = {"post", "polica"},
            description = "Kreira novu policu. Dužina, širina i visina je obavezna!")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Kreirano", content = @Content(schema = @Schema(implementation = StudentPolica.class), mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Loš zahtjev (nije primljen dto objekt ili ne postoji naziv)", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "500", description = "Interna pogreška servera", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
    })

    @PostMapping("/post")
    public ResponseEntity post(
            @RequestParam(required = true) StudentPolicaDTO dto
    ) {
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
    ) {
        try {

            StudentPolica polica = policaService.getBySifra(sifraPolice);
            if (polica == null) {
                return new ResponseEntity<>("Ne postoji polica s navedenom šifrom" + " " + sifraPolice, HttpStatus.BAD_REQUEST);
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
    ) {
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

    @GetMapping("/getKutijeNaPolici")
    public ResponseEntity getKutijeNaPolici(
            @RequestParam int sifra
    ) {
        try {
            if (sifra <= 0) {
                return new ResponseEntity<>("Šifra mora biti veća od 0" + " " + sifra, HttpStatus.BAD_REQUEST);
            }

            StudentPolica polica = policaService.getBySifra(sifra);
            if (polica == null) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            List<StudentKutija> kutije = policaService.getKutijeNaPolici(polica);
            if (kutije.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(kutije, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getUkupnaSirinaPolice")
    public ResponseEntity getUkupnaSirinaPolice() {
        try {
            int ukupnaSirina = policaService.getUkupnaSirinaPolice();

            if (ukupnaSirina == 0) {
                return new ResponseEntity<>("Nema police u bazi sa navedenim širinama!", HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(ukupnaSirina, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
