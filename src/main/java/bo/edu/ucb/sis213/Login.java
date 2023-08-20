package bo.edu.ucb.sis213;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login {

    private static Connection connection;
    private static int intentosRestantes = 3;
    private static Usuario usuarioActual = null;

    public static void Ingresar(String aliasIngresado, int pinIngresado, int intentos) {
        connection = null;
        try {
            connection = BaseDeDatos.getConnection();
        } catch (SQLException ex) {
            System.err.println("No se puede conectar a la Base de Datos.");
            ex.printStackTrace();
            System.exit(1);
        }

        while (intentos > 0) {
            usuarioActual = validarPIN(aliasIngresado, pinIngresado);
            if (usuarioActual != null) {
                break;
            } else {
                intentos--;
            }
        }
    }

    public static Usuario validarPIN(String alias, int pin) {
        String query = "SELECT id, nombre, saldo, alias FROM usuarios WHERE alias = ? AND pin = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, alias);
            preparedStatement.setInt(2, pin);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nombre = resultSet.getString("nombre");
                double saldo = resultSet.getDouble("saldo");
                return new Usuario(id, nombre, pin, saldo, alias);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Si el usuario o el PIN son inválidos
    }

    public static int getIntentosRestantes() {
        return intentosRestantes;
    }

    public static Usuario getUsuarioActual() {
        return usuarioActual;
    }
}
