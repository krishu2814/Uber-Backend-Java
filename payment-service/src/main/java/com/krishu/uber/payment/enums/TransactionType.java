package com.krishu.uber.payment.enums;

// Financial transaction direction: CREDIT increases balance, DEBIT decreases balance
public enum TransactionType {
    CREDIT, // Money added (welcome bonus, top-up, driver earnings)
    DEBIT   // Money deducted (fare payment, fee deduction)
}
