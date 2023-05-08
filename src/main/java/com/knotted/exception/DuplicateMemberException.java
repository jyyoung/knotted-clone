package com.knotted.exception;

public class DuplicateMemberException extends IllegalStateException{
    public DuplicateMemberException(String message){
        super(message);
    }
}
