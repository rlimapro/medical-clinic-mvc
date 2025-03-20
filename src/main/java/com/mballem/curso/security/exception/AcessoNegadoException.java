package com.mballem.curso.security.exception;

public class AcessoNegadoException extends RuntimeException {
    public AcessoNegadoException(String message) {
        super(message);
    }
}
