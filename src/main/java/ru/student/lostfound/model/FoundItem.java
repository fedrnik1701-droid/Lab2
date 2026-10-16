package ru.student.lostfound.model;

public record FoundItem(Long id, String description, String category, String foundLocation, String status) {}
