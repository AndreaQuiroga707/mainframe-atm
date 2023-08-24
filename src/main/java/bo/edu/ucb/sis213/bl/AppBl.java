package bo.edu.ucb.sis213.bl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import bo.edu.ucb.sis213.bl.util.ATMException;
import bo.edu.ucb.sis213.dao.BaseDeDatos;
import bo.edu.ucb.sis213.dao.HistoricoDao;
import bo.edu.ucb.sis213.view.CajeroGUI;
import bo.edu.ucb.sis213.dao.UsuarioDao;  

public class AppBl {
    private static Connection connection;
    static UsuarioDao usuarioDao = new UsuarioDao(connection);

    public static void main(String[] args) {
        try {
            connection = BaseDeDatos.getConnection(); // Obtener la conexión de la clase BaseDeDatos

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    CajeroGUI loginGUI = new CajeroGUI(connection);//conectoin
                    loginGUI.setVisible(true);
                }
            });
         
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al conectar con la base de datos.");
        }
    }
    
    public static void consultarSaldo(Usuario usuario) {
        
        try {
            usuarioDao.consultarSaldo(usuario);
            JOptionPane.showMessageDialog(null, "Su saldo actual es: $" + usuario.getSaldo());
        } catch (ATMException e) {
            JOptionPane.showMessageDialog(null, "Error al consultar el saldo: " + e.getMessage());
        }
    }
    
    public static void realizarDeposito(Usuario usuario, double cantidad) throws ATMException, SQLException {
    System.out.println(cantidad+"cantidaaaad");
        if (cantidad <= 0) {
            //JOptionPane.showMessageDialog(null, "Cantidad no válida.");
            throw new ATMException("Cantidad no válida.");
        } else {
            usuario.actualizarSaldo(cantidad);
            HistoricoDao operacion = new HistoricoDao(usuario.getId(), "deposito", cantidad);
            operacion.guardarEnHistorico(BaseDeDatos.getConnection());
            
            UsuarioDao depositDao = new UsuarioDao(BaseDeDatos.getConnection());
            depositDao.realizarDeposito(usuario, cantidad);

            JOptionPane.showMessageDialog(null, "Depósito realizado con éxito. Su nuevo saldo es: $" + usuario.getSaldo());
         }   
    }  
    
    public static void realizarRetiro(Usuario usuario, double cantidad) throws ATMException, SQLException {
    //System.out.println(cantidad+"cantidaaaad");
        if (cantidad <= 0) {
            //JOptionPane.showMessageDialog(null, "Cantidad no válida.");
            throw new ATMException("Cantidad no válida.");
        } else {
            usuario.actualizarSaldo(-cantidad);
            HistoricoDao operacion = new HistoricoDao(usuario.getId(), "retiro", cantidad);
            operacion.guardarEnHistorico(BaseDeDatos.getConnection());
            
            UsuarioDao retUsuarioDao = new UsuarioDao(BaseDeDatos.getConnection());
            retUsuarioDao.realizarRetiro(usuario, cantidad);

            JOptionPane.showMessageDialog(null, "Retiro realizado con éxito. Su nuevo saldo es: $" + usuario.getSaldo());
         }   
    }  
    
    
    public static void cambiarPINLogic(Usuario usuario, int nuevoPin, Connection connection) throws SQLException {
        UsuarioDao usuarioDao = new UsuarioDao(connection);
        usuarioDao.cambiarPIN(usuario, nuevoPin);
    }

    public Usuario ingresar(String aliasIngresado, int pinIngresado, int intentos) {
        Usuario usuarioActual = null;
        while (intentos > 0) {
            usuarioActual = usuarioDao.validarPIN(aliasIngresado, pinIngresado);
            if (usuarioActual != null) {
                break;
            } else {
                intentos--;
            }
        }
        return usuarioActual;
    }
    
    
}
    
    