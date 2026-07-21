package com.projeto.usuario.infrastructure.exception;

public class IllegalArgumentException extends RuntimeException {

    public IllegalArgumentException(String mensagem) {
        super(mensagem);
    }
    public IllegalArgumentException(String mensagem, Throwable throwable) {
        super(mensagem,throwable);
    }

}
