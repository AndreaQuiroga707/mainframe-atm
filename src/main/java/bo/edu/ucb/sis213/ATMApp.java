package bo.edu.ucb.sis213;

import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.SwingUtilities;

import bo.edu.ucb.sis213.dao.BaseDeDatos;
import bo.edu.ucb.sis213.view.CajeroGUI;

public class ATMApp {
        public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Connection connection = null;
                try{
                    connection = BaseDeDatos.getConnection();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                new CajeroGUI(connection).setVisible(true);
            }
        });
    }
}

