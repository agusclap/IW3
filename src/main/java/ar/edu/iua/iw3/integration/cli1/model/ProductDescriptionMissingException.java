package ar.edu.iua.iw3.integration.cli1.model;

public class ProductDescriptionMissingException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ProductDescriptionMissingException(String message) {
        super(message);
    }

    public ProductDescriptionMissingException(String message, Throwable cause) {
        super(message, cause);
    }
}
