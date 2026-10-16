package ru.student.lostfound.model;

public record ItemClaim(Long id, Long lossReportId, Long foundItemId, String evidence, String status) {}
