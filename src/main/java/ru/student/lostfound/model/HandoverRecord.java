package ru.student.lostfound.model;

import java.time.LocalDateTime;

public record HandoverRecord(Long id, Long itemClaimId, LocalDateTime handoverDate, boolean confirmed) {}
