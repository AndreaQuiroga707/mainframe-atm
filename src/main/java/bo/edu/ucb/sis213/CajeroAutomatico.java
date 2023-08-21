package bo.edu.ucb.sis213;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class CajeroAutomatico {
    public static Usuario usuario;
    private static Connection connection;
    public static void main(String[] args) {
        try {
            connection = BaseDeDatos.getConnection(); // Obtener la conexión de la clase BaseDeDatos

            //CajeroAutomatico cajero = new CajeroAutomatico();

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    CajeroGUI loginGUI = new CajeroGUI(usuario, connection);
                    loginGUI.setVisible(true);
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al conectar con la base de datos.");
        }
        
        
        //CajeroGUI cajeroGUI = new CajeroGUI(usuario,connection);
        
    }

    public static void consultarSaldo(Usuario usuario, Connection connection) {
        if (usuario != null) {
            JOptionPane.showMessageDialog(null, "Su saldo actual es: $" + usuario.getSaldo());
        } else {
            JOptionPane.showMessageDialog(null, "Debe autenticarse para consultar el saldo.");
        }
    }
    
    public static void realizarDeposito(Usuario usuario, Connection connection) {
        if (connection == null) {
            try {
                connection = BaseDeDatos.getConnection(); // Volver a obtener la conexión si es nula
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error al conectar con la base de datos.");
                return; // Salir del método si no se puede obtener la conexión
            }
        }
        
        if (connection != null) {
            String cantidadStr = JOptionPane.showInputDialog("Ingrese la cantidad a depositar: $");
            double cantidad = Double.parseDouble(cantidadStr);
    
            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(null, "Cantidad no válida.");
            } else {
                usuario.actualizarSaldo(cantidad);
                try {
                Operacion operacion = new Operacion(usuario.getId(), "deposito", cantidad);
                operacion.guardarEnHistorico(connection);

                // Actualizar saldo en la base de datos
                String actualizarSaldoQuery = "UPDATE usuarios SET saldo = ? WHERE alias = ?";
                try (PreparedStatement actualizarSaldoStatement = connection.prepareStatement(actualizarSaldoQuery)) {
                    actualizarSaldoStatement.setDouble(1, usuario.getSaldo());
                    actualizarSaldoStatement.setString(2, usuario.getAlias());
                    actualizarSaldoStatement.executeUpdate();
                }

                JOptionPane.showMessageDialog(null, "Depósito realizado con éxito. Su nuevo saldo es: $" + usuario.getSaldo());
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error al realizar el depósito.");
            }
        }
    } else {
        System.out.println("La conexión es nula. No se puede realizar la operación.");
    }
}


    
    public static void realizarRetiro(Usuario usuario, Connection connection) {
        if (connection == null) {
            try {
                connection = BaseDeDatos.getConnection(); // Volver a obtener la conexión si es nula
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error al conectar con la base de datos.");
                return; // Salir del método si no se puede obtener la conexión
            }
        }
        String cantidadStr = JOptionPane.showInputDialog("Ingrese la cantidad a retirar: $");
        double cantidad = Double.parseDouble(cantidadStr);
       
        if (cantidad <= 0) {
            JOptionPane.showMessageDialog(null, "Cantidad no válida.");

        } else if (cantidad > usuario.getSaldo()) {
            JOptionPane.showMessageDialog(null, "Saldo insuficiente.");
        } else {
            usuario.actualizarSaldo(-cantidad); // Restar la cantidad al saldo
            try {
                Operacion operacion = new Operacion(usuario.getId(), "retiro", cantidad);
                operacion.guardarEnHistorico(connection);
    
                // Actualizar saldo en la base de datos
                String actualizarSaldoQuery = "UPDATE usuarios SET saldo = ? WHERE id = ?";
                try (PreparedStatement actualizarSaldoStatement = connection.prepareStatement(actualizarSaldoQuery)) {
                    actualizarSaldoStatement.setDouble(1, usuario.getSaldo());
                    actualizarSaldoStatement.setInt(2, usuario.getId());
                    actualizarSaldoStatement.executeUpdate();
                }
    
                JOptionPane.showMessageDialog(null, "Retiro realizado con éxito. Su nuevo saldo es: $" + usuario.getSaldo());
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al realizar el retiro.");
            }
        }
    }
    
    public static void cambiarPIN(Usuario usuario, Connection connection) {
        if (connection == null) {
            try {
                connection = BaseDeDatos.getConnection(); // Volver a obtener la conexión si es nula
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos.");
                return; // Salir del método si no se puede obtener la conexión
            }
        }
        String pinIngresadoStr = JOptionPane.showInputDialog("Ingrese su PIN actual: ");
        int pinIngresado = Integer.parseInt(pinIngresadoStr);
      
        if (pinIngresado == usuario.getPin()) {
            try {
                String nuevoPinStr = JOptionPane.showInputDialog("Ingrese su nuevo PIN: ");
                int nuevoPin = Integer.parseInt(nuevoPinStr);
                String confirmacionPinStr = JOptionPane.showInputDialog("Confirme su nuevo PIN: ");
                int confirmacionPin = Integer.parseInt(confirmacionPinStr);
         
                if (nuevoPin == confirmacionPin) {
                    usuario.cambiarPIN(nuevoPin);
    
                    // Actualizar PIN en la base de datos
                    String actualizarPinQuery = "UPDATE usuarios SET pin = ? WHERE id = ?";
                    try (PreparedStatement actualizarPinStatement = connection.prepareStatement(actualizarPinQuery)) {
                        actualizarPinStatement.setInt(1, nuevoPin);
                        actualizarPinStatement.setInt(2, usuario.getId());
                        actualizarPinStatement.executeUpdate();
                    }
    
                    JOptionPane.showMessageDialog(null, "PIN actualizado con éxito.");
            } else {
                JOptionPane.showMessageDialog(null, "Los PINs no coinciden.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Por favor, ingrese un PIN válido.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al cambiar el PIN.");
        }
    } else {
        JOptionPane.showMessageDialog(null, "PIN incorrecto.");
        }
    }
    
}