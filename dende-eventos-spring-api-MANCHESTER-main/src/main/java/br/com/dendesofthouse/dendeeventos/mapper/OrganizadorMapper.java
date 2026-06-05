package br.com.dendesofthouse.dendeeventos.mapper;

import br.com.dendesofthouse.dendeeventos.dto.request.OrganizadorRequestDTO;
import br.com.dendesofthouse.dendeeventos.dto.response.OrganizadorResponseDTO;
import br.com.dendesofthouse.dendeeventos.entity.Usuario;
import org.springframework.stereotype.Component;

@Component
public class OrganizadorMapper {

    public static Usuario toModel(OrganizadorRequestDTO dto) {
        if (dto == null) return null;

        Usuario organizador = new Usuario();
        organizador.setNome(dto.getNome());
        organizador.setDataNascimento(dto.getDataNascimento());
        organizador.setSexo(dto.getSexo());
        organizador.setEmail(dto.getEmail());
        organizador.setSenha(dto.getSenha());
        return organizador;
    }

    public static void updateModel(OrganizadorRequestDTO dto, Usuario organizadorExistente) {
        if (dto == null || organizadorExistente == null) return;

        organizadorExistente.setNome(dto.getNome());
        organizadorExistente.setDataNascimento(dto.getDataNascimento());
        organizadorExistente.setSexo(dto.getSexo());

        if (dto.getSenha() != null && !dto.getSenha().isEmpty()) {
            organizadorExistente.setSenha(dto.getSenha());
        }
    }

    public static OrganizadorResponseDTO toResponse(Usuario organizador) {
        if (organizador == null) return null;

        OrganizadorResponseDTO dto = new OrganizadorResponseDTO();
        dto.setId(organizador.getId());
        dto.setNome(organizador.getNome());
        dto.setDataNascimento(organizador.getDataNascimento() != null
                ? organizador.getDataNascimento().toString() : null);
        dto.setIdadeCompleta(organizador.getIdadeCompleta());
        dto.setSexo(organizador.getSexo());
        dto.setEmail(organizador.getEmail());
        dto.setAtivo(organizador.getAtivo());

        if (organizador.getEmpresa() != null) {
            OrganizadorResponseDTO.EmpresaResponseDTO empresaDTO =
                    new OrganizadorResponseDTO.EmpresaResponseDTO();
            empresaDTO.setCnpj(organizador.getEmpresa().getCnpj());
            empresaDTO.setRazaoSocial(organizador.getEmpresa().getRazaoSocial());
            empresaDTO.setNomeFantasia(organizador.getEmpresa().getNomeFantasia());
            dto.setEmpresa(empresaDTO);
        }

        return dto;
    }
}