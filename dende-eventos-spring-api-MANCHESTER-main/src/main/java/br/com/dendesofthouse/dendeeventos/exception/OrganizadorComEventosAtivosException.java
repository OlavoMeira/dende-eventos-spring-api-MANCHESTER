package br.com.dendesofthouse.dendeeventos.exception;

public class OrganizadorComEventosAtivosException extends RuntimeException {
    public OrganizadorComEventosAtivosException(Long organizadorId) {
        super("Não é possível desativar o organizador " + organizadorId
                + " pois ele possui eventos ativos ou em andamento.");
    }
}
