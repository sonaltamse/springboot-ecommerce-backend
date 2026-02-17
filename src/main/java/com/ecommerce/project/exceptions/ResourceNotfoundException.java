package com.ecommerce.project.exceptions;

public class ResourceNotfoundException extends RuntimeException{
    String resourceName, field, fieldName;
    Long fieldId;

    public ResourceNotfoundException() {
    }

    public ResourceNotfoundException(String resourceName, String field, String fieldName) {
        super(String.format("%s not found with %s: %s", resourceName, field, fieldName));
        this.resourceName = resourceName;
        this.field = field;
        this.fieldName = fieldName;
    }

    public ResourceNotfoundException(String resourceName, String field, Long fieldId) {
        super(String.format("%s not found with %s: %d", resourceName, field, fieldId));
        this.resourceName = resourceName;
        this.field = field;
        this.fieldId = fieldId;
    }
}
