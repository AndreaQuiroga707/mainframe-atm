package bo.edu.ucb.sis213;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Operacion {
    private int usuarioId;
    private String tipoOperacion;
    private double cantidad;

    public Operacion(int usuarioId, String tipoOperacion, double cantidad) {
        this.usuarioId = usuarioId;
        this.tipoOperacion = tipoOperacion;
        this.cantidad = cantidad;
    }

    public void guardarEnHistorico(Connection connection) throws SQLException {
        String historicoQuery = "INSERT INTO historico (usuario_id, tipo_operacion, cantidad) VALUES (?, ?, ?)";
        try (PreparedStatement historicoStatement = connection.prepareStatement(historicoQuery)) {
            historicoStatement.setInt(1, usuarioId);
            historicoStatement.setString(2, tipoOperacion);
            historicoStatement.setDouble(3, cantidad);
            historicoStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al guardar la operación en el historial.", e);
        }
    }
}
