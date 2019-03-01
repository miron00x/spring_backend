package com.example.demo.exception;

public class DocNotFondException extends RuntimeException {
    public DocNotFondException(){
        super("The document was not found!");
    }
}
