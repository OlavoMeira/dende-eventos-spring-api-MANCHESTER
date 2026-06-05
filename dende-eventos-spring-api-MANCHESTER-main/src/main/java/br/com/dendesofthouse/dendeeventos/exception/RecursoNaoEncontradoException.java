package br.com.dendesofthouse.dendeeventos.exception;

public class RecursoNaoEncontradoException extends RuntimeException {
  public RecursoNaoEncontradoException(String recurso, Long id) {
    super(recurso + " com id " + id + " não encontrado.");
  }

  public RecursoNaoEncontradoException(String message) {
    super(message);
  }
}
