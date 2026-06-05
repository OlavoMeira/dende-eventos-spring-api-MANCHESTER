package br.com.dendesofthouse.dendeeventos.mapper;

import br.com.dendesofthouse.dendeeventos.dto.response.IngressoResponseDTO;
import br.com.dendesofthouse.dendeeventos.entity.Ingresso;
import org.springframework.stereotype.Component;

@Component
public class IngressoMapper {

    public static IngressoResponseDTO toResponse(Ingresso ingresso) {
        if (ingresso == null) return null;

        IngressoResponseDTO dto = new IngressoResponseDTO();
        dto.setId(ingresso.getId());
        dto.setValorPago(ingresso.getValorPago());
        dto.setValorReembolsado(ingresso.getValorReembolsado());
        dto.setStatus(ingresso.getStatus());

        dto.setDataCompra(ingresso.getDataCompra() != null ? ingresso.getDataCompra().toString() : null);
        dto.setDataCancelamento(ingresso.getDataCancelamento() != null ? ingresso.getDataCancelamento().toString() : null);

        if (ingresso.getEvento() != null) {
            dto.setEvento(ingresso.getEvento().getNome());
            dto.setDataEvento(ingresso.getEvento().getDataInicio() != null
                    ? ingresso.getEvento().getDataInicio().toString() : null);
            dto.setEventoAtivo(ingresso.getEvento().getAtivo());
            dto.setEventoFinalizado(ingresso.getEvento().isFinalizado());
        }

        return dto;
    }
}
