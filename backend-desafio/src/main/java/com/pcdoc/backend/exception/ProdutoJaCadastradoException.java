package com.pcdoc.backend.exception;

public class ProdutoJaCadastradoException extends RuntimeException {

    public ProdutoJaCadastradoException(String mensagem) {
        super(mensagem);
    }
}
