package co.edu.umanizales.bookverse.model;

import lombok.Getter;

/**
 * Enumeration representing different payment methods
 */
@Getter
public enum PaymentMethod {
    
    CASH("Cash", "Cash payment"),
    CREDIT_CARD("Credit Card", "Credit card payment"),
    DEBIT_CARD("Debit Card", "Debit card payment"),
    BANK_TRANSFER("Bank Transfer", "Bank transfer payment"),
    PSE("PSE", "Secure online payment"),
    DIGITAL_WALLET("Digital Wallet", "Digital wallet payment");
    
    private final String name;
    private final String description;
    
    PaymentMethod(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
