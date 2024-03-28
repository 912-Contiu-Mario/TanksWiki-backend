package dev.tankswikibackend.Entity;

import org.springframework.stereotype.Repository;

public class RepositoryException extends Exception {
    public RepositoryException(String message ){
        super(message);
    }
}
