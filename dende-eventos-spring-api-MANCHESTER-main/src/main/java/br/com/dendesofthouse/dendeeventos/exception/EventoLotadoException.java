package br.com.dendesofthouse.dendeeventos.exception;

public class EventoLotadoException extends RuntimeException {
    public EventoLotadoException(String nomeEvento) {
        super("O evento '" + nomeEvento + "' está lotado.");
    }
}
