package io.github.bagdad.dakarhelperservice.exception;

public class ExcelStorageNotFoundException extends RuntimeException {

    private final String message;

    private final int statusCode;

    public ExcelStorageNotFoundException(long id) {
        super("ExcelStorage with id " + id + " not found");
        this.message = "ExcelStorage with id " + id + " not found";
        this.statusCode = 404;
    }

}


