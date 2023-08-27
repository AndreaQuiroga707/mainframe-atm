package bo.edu.ucb.sis213.bl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import bo.edu.ucb.sis213.bl.util.ATMException;
import bo.edu.ucb.sis213.bl.util.SinIntentosException;
import bo.edu.ucb.sis213.dao.BaseDeDatos;
import bo.edu.ucb.sis213.dao.HistoricoDao;
import bo.edu.ucb.sis213.dao.UsuarioDao;  

public class AppBl {
    private static Connection connection;
    static UsuarioDao usuarioDao = new UsuarioDao(connection);

    public static void consultarSaldo(UsuarioBl usuario) throws ATMException, SQLException {
        try {
            usuarioDao.consultarSaldo(usuario);
            //JOptionPane.showMessageDialog(null, "Su saldo es: $" + usuario.getSaldo());
        } catch (ATMException e) {
            throw new ATMException("Error al consultar el saldo: " + e.getMessage());
        }
    }
    
    public static void realizarDeposito(UsuarioBl usuario, BigDecimal cantidad) throws ATMException, SQLException {
    System.out.println(cantidad+"cantidad");
        //if (cantidad <= 0) {
        if (cantidad.compareTo(BigDecimal.ZERO) <= 0) {
            //JOptionPane.showMessageDialog(null, "Cantidad no válida.");
            throw new ATMException("Cantidad no válida.");
        } else {
            usuario.actualizarSaldo(cantidad);
            HistoricoDao operacion = new HistoricoDao(usuario.getId(), "deposito", cantidad);
            operacion.guardarEnHistorico(BaseDeDatos.getConnection());
            
            UsuarioDao depositDao = new UsuarioDao(BaseDeDatos.getConnection());
            depositDao.realizarDeposito(usuario, cantidad);

            //JOptionPane.showMessageDialog(null, "Depósito realizado con éxito. Su nuevo saldo es: $" + usuario.getSaldo());
         }   
    }  
    
    public static void realizarRetiro(UsuarioBl usuario, BigDecimal cantidad) throws ATMException, SQLException {
   
        //if (cantidad <= 0) {
        if (cantidad.compareTo(BigDecimal.ZERO) <= 0) {
            //JOptionPane.showMessageDialog(null, "Cantidad no válida.");
            throw new ATMException("Cantidad no válida.");
        } else {
            usuario.actualizarSaldo(cantidad.negate());//resta
            HistoricoDao operacion = new HistoricoDao(usuario.getId(), "retiro", cantidad);
            operacion.guardarEnHistorico(BaseDeDatos.getConnection());
            
            UsuarioDao retUsuarioDao = new UsuarioDao(BaseDeDatos.getConnection());
            retUsuarioDao.realizarRetiro(usuario, cantidad);

            //JOptionPane.showMessageDialog(null, "Retiro realizado con éxito. Su nuevo saldo es: $" + usuario.getSaldo());
        }   
    }  
    
    
    public static void cambiarPINLogic(UsuarioBl usuario, int pinActual, int nuevoPin1, int nuevoPin2, Connection connection) throws SQLException, ATMException {
        if (nuevoPin1 != nuevoPin2) {
            throw new ATMException("Los PINs no coinciden");
        }
        if (nuevoPin1 == pinActual) {
            throw new ATMException("El nuevo PIN no puede ser igual al actual");
        }
        if (usuario.getPin() != pinActual) {
            throw new ATMException("El PIN actual no es correcto");
        }
        if (nuevoPin1 < 1000 || nuevoPin1 > 9999) {
            throw new ATMException("El PIN debe ser de 4 dígitos");
        }
        
        UsuarioDao usuarioDao = new UsuarioDao(connection);
        usuarioDao.cambiarPIN(usuario, nuevoPin2);
        usuario.cambiarPIN(nuevoPin2);
    }

    public UsuarioBl ingresar(String aliasIngresado, int pinIngresado, int intentos) throws Exception{
        if(intentos == 0){
            throw new SinIntentosException("No tiene más intentos");
        }
        UsuarioBl usuarioActual = null;
        usuarioActual = usuarioDao.validarPIN(aliasIngresado, pinIngresado);

        return usuarioActual;
    }    
}
    
    