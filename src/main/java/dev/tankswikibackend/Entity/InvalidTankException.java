package dev.tankswikibackend.Entity;

public class InvalidTankException extends Exception{
    public InvalidTankException(String message){
        super(message);
    }
    public InvalidTankException(){}
}
