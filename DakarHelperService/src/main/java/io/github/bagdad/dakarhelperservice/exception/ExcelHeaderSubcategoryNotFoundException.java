package io.github.bagdad.dakarhelperservice.exception;

public class ExcelHeaderSubcategoryNotFoundException extends RuntimeException {

    private final String message;

    private final int statusCode;

    public ExcelHeaderSubcategoryNotFoundException(long id) {
        super("VendorFile with id " + id + " not found");
        this.message = "ExcelHeaderSubcategory with id " + id + " not found";
        this.statusCode = 404;
    }
}
