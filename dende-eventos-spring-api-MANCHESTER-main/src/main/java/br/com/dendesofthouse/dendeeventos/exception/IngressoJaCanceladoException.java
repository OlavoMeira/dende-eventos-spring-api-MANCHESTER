package br.com.dendesofthouse.dendeeventos.exception;

public class IngressoJaCanceladoException extends RuntimeException {
    public IngressoJaCanceladoException(Long ingressoId) {
        super("O ingresso com id " + ingressoId + " já está cancelado.");
    }
}
