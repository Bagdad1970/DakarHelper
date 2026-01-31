package io.github.bagdad.dakarhelperservice.exception;

public class VendorNotFoundException extends RuntimeException {

    private final String message;

    private final int statusCode;

    public VendorNotFoundException(long id) {
        super("Vendor with id " + id + " not found");
        this.message = "Vendor with id " + id + " not found";
        this.statusCode = 404;
    }

}
