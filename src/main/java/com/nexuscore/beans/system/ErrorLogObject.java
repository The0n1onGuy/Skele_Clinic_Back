package com.nexuscore.beans.system;

public record ErrorLogObject(
    int httpCode,
    String statusName,
    String operation,
    String detail,
    long timestamp,
    String serverName // Útil para saber de qué servidor viene el log
) {}
