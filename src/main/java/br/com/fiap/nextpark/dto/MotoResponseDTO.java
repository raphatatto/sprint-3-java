package br.com.fiap.nextpark.dto;

import br.com.fiap.nextpark.model.Moto;
import br.com.fiap.nextpark.model.StatusMoto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MotoResponseDTO {
    private String placa;
    private String modelo;
    private String cor;
    private StatusMoto status;
    private String codigoVaga;

    public static MotoResponseDTO from(Moto m, String codigoVaga) {
        return new MotoResponseDTO(m.getPlaca(), m.getModelo(), m.getCor(), m.getStatus(), codigoVaga);
    }
}
