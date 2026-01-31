package io.github.bagdad.dakarhelperservice.exception;

public class ExcelHeaderCategoryNotFoundException extends RuntimeException {

    private final String message;

    private final int statusCode;

    public ExcelHeaderCategoryNotFoundException(long id) {
        super("ExcelHeaderCategory with id " + id + " not found");
        this.message = "ExcelHeaderCategory with id " + id + " not found";
        this.statusCode = 404;
    }

}
