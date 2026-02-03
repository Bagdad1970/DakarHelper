package io.github.bagdad.dakarhelperservice.exception;

public class ExcelProductNotFoundException extends RuntimeException {

    private final String message;

    private final int statusCode;

    public ExcelProductNotFoundException(long id) {
        super("ExcelProduct with id " + id + " not found");
        this.message = "ExcelProduct with id " + id + " not found";
        this.statusCode = 404;
    }

}


