package com.krishu.uber.driver.dto;

import com.krishu.uber.driver.enums.DriverStatus;
import jakarta.validation.constraints.NotNull;

// Request body or parameter to change driver availability state
public class StatusUpdateRequest {

    @NotNull(message = "Status cannot be null (AVAILABLE, BUSY, OFFLINE)")
    private DriverStatus status;

    public StatusUpdateRequest() {
    }

    public StatusUpdateRequest(DriverStatus status) {
        this.status = status;
    }

    public DriverStatus getStatus() {
        return status;
    }

    public void setStatus(DriverStatus status) {
        this.status = status;
    }
}
