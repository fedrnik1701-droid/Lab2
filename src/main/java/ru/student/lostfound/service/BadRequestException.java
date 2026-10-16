package ru.student.lostfound.service;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) { super(message); }
}
