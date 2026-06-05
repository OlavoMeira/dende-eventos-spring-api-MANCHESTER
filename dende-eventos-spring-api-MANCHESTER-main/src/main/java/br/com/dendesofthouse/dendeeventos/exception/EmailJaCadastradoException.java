package br.com.dendesofthouse.dendeeventos.exception;

public class EmailJaCadastradoException extends RuntimeException {
    public EmailJaCadastradoException(String email, String tipo) {
        super(tipo + " com e-mail " + email + " já está cadastrado.");
    }
}
