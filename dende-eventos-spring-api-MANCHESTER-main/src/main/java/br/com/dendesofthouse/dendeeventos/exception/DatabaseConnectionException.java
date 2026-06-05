package br.com.dendesofthouse.dendeeventos.exception;

public class DatabaseConnectionException extends RuntimeException {
  public DatabaseConnectionException(String message, Throwable cause) {
    super(message, cause);
  }
}
