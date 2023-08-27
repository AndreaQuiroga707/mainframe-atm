package bo.edu.ucb.sis213.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class HistoricoDao {
    private int usuarioId;
    private String tipoOperacion;
    private BigDecimal cantidad;

    public HistoricoDao(int usuarioId, String tipoOperacion, BigDecimal cantidad2) {
        this.usuarioId = usuarioId;
        this.tipoOperacion = tipoOperacion;
        this.cantidad = cantidad2;
    }

    public void guardarEnHistorico(Connection connection) throws SQLException {
        String historicoQuery = "INSERT INTO historico (usuario_id, tipo_operacion, cantidad) VALUES (?, ?, ?)";
        try (PreparedStatement historicoStatement = connection.prepareStatement(historicoQuery)) {
            historicoStatement.setInt(1, usuarioId);
            historicoStatement.setString(2, tipoOperacion);
            historicoStatement.setBigDecimal(3, cantidad);
            historicoStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al guardar la operación en el historial.", e);
        }
    }
}
