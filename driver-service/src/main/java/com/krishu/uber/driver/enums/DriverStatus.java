package com.krishu.uber.driver.enums;

// Represents the operational availability state of a driver
public enum DriverStatus {
    AVAILABLE,  // Online and ready to accept ride requests
    BUSY,       // Currently assigned to an active trip
    OFFLINE     // Offline and not accepting rides
}
