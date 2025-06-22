/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ffos.skroflin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 *
 * @author Korisnik
 */
public record StudentPolicaDTO(
        @Schema(example = "50") int duzina,
        @Schema(example = "100") int sirina,
        @Schema(example = "3") int visina
        ) {
    
}
