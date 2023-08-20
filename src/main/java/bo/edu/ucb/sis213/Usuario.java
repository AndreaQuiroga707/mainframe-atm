package bo.edu.ucb.sis213;

public class Usuario {
    private int id;
    private String nombre;
    private int pin;
    private double saldo;
    private String alias;

    public Usuario(int id, String nombre, int pin, double saldo, String alias) {
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

    public double getSaldo() {
        return saldo;
    }

    public String getAlias() {
        return alias;
    }

    public void actualizarSaldo(double cantidad) {
        saldo += cantidad;
    }

    public void cambiarPIN(int nuevoPin) {
        pin = nuevoPin;
    }
}
