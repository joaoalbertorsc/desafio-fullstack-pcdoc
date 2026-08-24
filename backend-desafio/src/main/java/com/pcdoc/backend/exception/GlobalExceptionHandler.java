package com.pcdoc.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProdutoNaoEncontradoException.class)
    public ProblemDetail handleProdutoNaoEncontrado(ProdutoNaoEncontradoException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setTitle("Produto não encontrado");
        problemDetail.setType(URI.create("https://api.pcdoc.com/errors/not-found"));
        return problemDetail;
    }

    @ExceptionHandler(ProdutoJaCadastradoException.class)
    public ProblemDetail handleProdutoJaCadastrado(ProdutoJaCadastradoException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problemDetail.setTitle("Produto já cadastrado");
        problemDetail.setType(URI.create("https://api.pcdoc.com/errors/product-already-exists"));
        return problemDetail;
    }

    // Trata os erros de validação do Jakarta Validation (@NotNull, @Min, etc)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidacao(MethodArgumentNotValidException ex) {
        String erros = ex.getBindingResult().getFieldErrors().stream()
                .map(erro -> erro.getField() + ": " + erro.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Erros de validação: " + erros);
        problemDetail.setTitle("Dados de requisição inválidos");
        problemDetail.setType(URI.create("https://api.pcdoc.com/errors/invalid-data"));
        return problemDetail;
    }
}
