package ru.student.lostfound.model;

public record LossReport(Long id, Long userId, String description, String category, String status) {}
