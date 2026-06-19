package com.projeto.usuario.infrastructure.exception;

public class ResourceNotFondException extends RuntimeException {
    public ResourceNotFondException(String mensagem) {
        super(mensagem);
    }
    public ResourceNotFondException (String mensagem, Throwable throwable){
        super(mensagem, throwable);
    }
}
