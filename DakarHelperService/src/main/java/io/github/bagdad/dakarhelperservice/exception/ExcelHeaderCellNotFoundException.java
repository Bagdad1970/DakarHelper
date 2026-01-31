package io.github.bagdad.dakarhelperservice.exception;

public class ExcelHeaderCellNotFoundException extends RuntimeException {

    private final String message;

    private final int statusCode;

    public ExcelHeaderCellNotFoundException(long id) {
        super("ExcelHeaderCell with id " + id + " not found");
        this.message = "ExcelHeaderCell with id " + id + " not found";
        this.statusCode = 404;
    }

}
