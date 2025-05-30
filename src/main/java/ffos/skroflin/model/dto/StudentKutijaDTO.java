/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ffos.skroflin.model.dto;

import ffos.skroflin.model.StudentPolica;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author Korisnik
 */
public record StudentKutijaDTO(
        @Schema(example = "28.05.2025.") Date datumPohrane,
        @Schema(example = "3,54") BigDecimal obujam,
        @Schema(example = "B54") String oznakaKutije,
        @Schema(example = "1") StudentPolica studentPolica
        ) {
    
}
