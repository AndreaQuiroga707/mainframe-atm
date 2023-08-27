package bo.edu.ucb.sis213.bl;

import java.math.BigDecimal;

public class UsuarioBl {
    private int id;
    private String nombre;
    private int pin;
    private BigDecimal saldo;
    private String alias;

    public UsuarioBl(int id, String nombre, int pin, BigDecimal saldo, String alias) {
        this.id = id;
        this.nombre = nombre;
        this.pin = pin;
        this.saldo = saldo;
        this.alias = alias;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getPin() {
        return pin;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public String getAlias() {
        return alias;
    }

    public void actualizarSaldo(BigDecimal cantidad) {
        //saldo += cantidad;
        saldo = saldo.add(cantidad);
    }

    public void cambiarPIN(int nuevoPin) {
        pin = nuevoPin;
    }
}
