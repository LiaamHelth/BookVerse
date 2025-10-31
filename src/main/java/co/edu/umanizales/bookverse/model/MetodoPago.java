package co.edu.umanizales.bookverse.model;

import lombok.Getter;

/**
 * Enumeration representing different payment methods
 */
@Getter
public enum MetodoPago {
    
    EFECTIVO("Efectivo", "Pago en efectivo"),
    TARJETA_CREDITO("Tarjeta de Crédito", "Pago con tarjeta de crédito"),
    TARJETA_DEBITO("Tarjeta de Débito", "Pago con tarjeta de débito"),
    TRANSFERENCIA("Transferencia Bancaria", "Pago mediante transferencia bancaria"),
    PSE("PSE", "Pago seguro en línea"),
    WALLET_DIGITAL("Billetera Digital", "Pago con billetera digital");
    
    private final String nombre;
    private final String descripcion;
    
    MetodoPago(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }
}
