package bo.edu.ucb.sis213.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import bo.edu.ucb.sis213.bl.UsuarioBl;
import bo.edu.ucb.sis213.bl.util.ATMException;

public class UsuarioDao {

    public static UsuarioBl usuario;
    public Connection connection;

    public UsuarioDao(Connection connection) {
        this.connection = connection;
    }

    public double consultarSaldo(UsuarioBl usuario) throws ATMException {
        Connection connectionAux = null;
        try {
            connectionAux = BaseDeDatos.getConnection();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (connectionAux == null) {
            throw new ATMException("No se puede conectar a la base de datos.");
        }

        try {
            // Realizar la operación de consulta en la base de datos
            String consultarSaldoQuery = "SELECT saldo FROM usuarios WHERE id = ?";
            try (PreparedStatement consultarSaldoStatement = connectionAux.prepareStatement(consultarSaldoQuery)) {
                consultarSaldoStatement.setInt(1, usuario.getId());
                try (ResultSet resultSet = consultarSaldoStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getDouble("saldo");
                    } else {
                        throw new ATMException("No se encontró el saldo para el usuario.");
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new ATMException("Error al consultar el saldo.");
        }
}


    public void realizarDeposito(UsuarioBl usuario, BigDecimal cantidad) throws ATMException {
        if (connection == null) {
            throw new ATMException("No se puede conectar a la base de datos.");
        }

        try {
            // Realizar la operación de depósito en la base de datos
            String actualizarSaldoQuery = "UPDATE usuarios SET saldo = saldo + ? WHERE id = ?";
            try (PreparedStatement actualizarSaldoStatement = connection.prepareStatement(actualizarSaldoQuery)) {
                actualizarSaldoStatement.setBigDecimal(1, cantidad);
                actualizarSaldoStatement.setInt(2, usuario.getId());
                actualizarSaldoStatement.executeUpdate();
            }

          
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ATMException("Error al realizar el depósito.");
        }
    }

    public void realizarRetiro(UsuarioBl usuario, BigDecimal cantidad) throws ATMException {
        if (connection == null) {
            throw new ATMException("No se puede conectar a la base de datos.");
        }
        try {
            // Realizar la operación de retiro en la base de datos
            String actualizarSaldoQuery = "UPDATE usuarios SET saldo = saldo - ? WHERE id = ?";
            try (PreparedStatement actualizarSaldoStatement = connection.prepareStatement(actualizarSaldoQuery)) {
                actualizarSaldoStatement.setBigDecimal(1, cantidad);
                actualizarSaldoStatement.setInt(2, usuario.getId());
                actualizarSaldoStatement.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new ATMException("Error al realizar el retiro.");
        }
    }
 
    public void cambiarPIN(UsuarioBl usuario, int nuevoPin) throws SQLException {
        Connection connection2 = BaseDeDatos.getConnection();

        String actualizarPinQuery = "UPDATE usuarios SET pin = ? WHERE id = ?";
        try (PreparedStatement actualizarPinStatement = connection2.prepareStatement(actualizarPinQuery)) {
            actualizarPinStatement.setInt(1, nuevoPin);
            actualizarPinStatement.setInt(2, usuario.getId());
            actualizarPinStatement.executeUpdate();

        }
    }

    public UsuarioBl validarPIN(String alias, int pin) throws ATMException {
        String query = "SELECT id, nombre, saldo, alias FROM usuarios WHERE alias = ? AND pin = ?";
        try {
            Connection connection2 = BaseDeDatos.getConnection();
            PreparedStatement preparedStatement = connection2.prepareStatement(query);
            preparedStatement.setString(1, alias);
            preparedStatement.setInt(2, pin);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nombre = resultSet.getString("nombre");
                BigDecimal saldo = resultSet.getBigDecimal("saldo");
                return new UsuarioBl(id, nombre, pin, saldo, alias);
            }else{
                throw new ATMException("Usuario o PIN incorrecto.");
            }
            }catch (Exception e) {
            e.printStackTrace();
            throw new ATMException("Error al validar el PIN.");
        }
        //return null; // Si el usuario o el PIN son inválidos
    }
    
}
