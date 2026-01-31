package io.github.bagdad.dakarhelperservice.exception;

public class VendorFileNotFoundException extends RuntimeException {

    private final String message;

    private final int statusCode;

    public VendorFileNotFoundException(long id) {
        super("VendorFile with id " + id + " not found");
        this.message = "VendorFile with id " + id + " not found";
        this.statusCode = 404;
    }

}
