package one.digitalinnovation.gof.dto;

import lombok.Builder;
import one.digitalinnovation.gof.model.Endereco;

@Builder
public record ClienteDTO(Long id, String nome, Endereco endereco) {
        }
