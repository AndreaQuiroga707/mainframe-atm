package bo.edu.ucb.sis213.view;

import javax.swing.*;

import bo.edu.ucb.sis213.bl.AppBl;
//import bo.edu.ucb.sis213.bl.Login;
import bo.edu.ucb.sis213.bl.UsuarioBl;
import bo.edu.ucb.sis213.bl.util.ATMException;
import bo.edu.ucb.sis213.bl.util.SinIntentosException;
import bo.edu.ucb.sis213.dao.BaseDeDatos;
import bo.edu.ucb.sis213.dao.UsuarioDao;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

public class CajeroGUI extends JFrame {
    private JTextField usuarioField;
    private JPasswordField pinField;
    private JButton ingresarButton;
    private UsuarioBl usuarioAutenticado;
    private int intentosRestantes = 2; // Intentos restantes por sesión


    public CajeroGUI(Connection connection) {
        //this.connection = connection; // Almacena la conexión en la instancia
        new UsuarioDao(connection);
        // Configuración de la ventana
        setTitle("Login");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Componentes de la interfaz
        JPanel panel = new JPanel(new GridLayout(3, 2));

        JLabel usuarioLabel = new JLabel("Usuario:");
        usuarioField = new JTextField();
        JLabel pinLabel = new JLabel("PIN:");
        pinField = new JPasswordField();
        ingresarButton = new JButton("Ingresar");

        panel.add(usuarioLabel);
        panel.add(usuarioField);
        panel.add(pinLabel);
        panel.add(pinField);
        panel.add(new JLabel()); // Espacio en blanco
        panel.add(ingresarButton);

        add(panel);

        ingresarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usuario = usuarioField.getText();
                char[] pinChars = pinField.getPassword();
                
                int pin;//= Integer.parseInt(new String(pinChars));
                try {
                    pin = Integer.parseInt(new String(pinChars));
                } catch (NumberFormatException e1) {
                    JOptionPane.showMessageDialog(null, "Por favor, ingrese un PIN válido.");
                    usuarioField.setText("");
                    pinField.setText("");
                    usuarioField.requestFocus();
                    return;
                }
                AppBl appBl = new AppBl();
                try {
                    usuarioAutenticado = appBl.ingresar(usuario, pin, intentosRestantes);
                    JOptionPane.showMessageDialog(null, "Bienvenid@ " + usuarioAutenticado.getNombre() + ".");
                    dispose(); // Cierra la ventana de login
                    new CajeroGUI(usuarioAutenticado, connection); 
                } catch(SinIntentosException el){
                    JOptionPane.showMessageDialog(null, "Ha excedido el número de intentos. La aplicación se cerrará.");
                    System.exit(0); // O realiza alguna otra acción si lo deseas
                } 
                catch (NumberFormatException e1){
                    JOptionPane.showMessageDialog(null, "Error al ingresar: " + e1.getMessage());
                }
                catch (Exception e1) {
                    // TODO Auto-generated catch block
                    JOptionPane.showMessageDialog(null, "Error al ingresar: " + e1.getMessage()+". Intentos restantes: "+intentosRestantes  );
                    usuarioField.setText("");
                    pinField.setText("");
                    usuarioField.requestFocus();
                    intentosRestantes--;
                    //e1.printStackTrace();
                }
            }
        });
    }
    
    public CajeroGUI(UsuarioBl usuarioAutenticado, Connection connection) {
        this.usuarioAutenticado = usuarioAutenticado;
        crearGUI();
    }
    private void crearGUI() {
        JFrame frame = new JFrame("ATM - Menú Principal");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(5, 1));

        JButton consultarSaldoButton = new JButton("Consultar Saldo");
        JButton realizarDepositoButton = new JButton("Realizar Depósito");
        JButton realizarRetiroButton = new JButton("Realizar Retiro");
        JButton cambiarPINButton = new JButton("Cambiar PIN");
        JButton salirButton = new JButton("Salir");

        frame.setLocationRelativeTo(null); // Centra la ventana en la pantalla

        consultarSaldoButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                AppBl.consultarSaldo(usuarioAutenticado);
                JOptionPane.showMessageDialog(null, "Su saldo actual es: $" + usuarioAutenticado.getSaldo());
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (ATMException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            
        }
    });

    realizarDepositoButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            
            String cantidadStr = JOptionPane.showInputDialog("Ingrese la cantidad a depositar: $");
            BigDecimal cantidad;
            try {
                //cantidad = Double.parseDouble(cantidadStr);
                cantidad = new BigDecimal(cantidadStr);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Por favor, ingrese una cantidad válida.");
                return;
            }
            
            //System.out.println("owo"+cantidad);
            try {
                AppBl.realizarDeposito(usuarioAutenticado, cantidad);
                JOptionPane.showMessageDialog(null, "Depósito realizado con éxito. Su nuevo saldo es: $" + usuarioAutenticado.getSaldo());
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (ATMException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }  
        }
    });
        
        //realizarDepositoButton.addActionListener(realizarDepositoListener);

    realizarRetiroButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            
            String cantidadStr = JOptionPane.showInputDialog("Ingrese la cantidad a retirar: $");
            //double cantidad = Double.parseDouble(cantidadStr);  
            BigDecimal cantidad;
            cantidad = new BigDecimal(cantidadStr);
            
            System.out.println("owo"+cantidad);
            try {
                AppBl.realizarRetiro(usuarioAutenticado, cantidad);
                JOptionPane.showMessageDialog(null, "Retiro realizado con éxito. Su nuevo saldo es: $" + usuarioAutenticado.getSaldo());
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (ATMException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }  
        }
    });

    cambiarPINButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int pinActual = Integer.parseInt(JOptionPane.showInputDialog("Ingrese su PIN actual: "));

                int nuevoPin1 = Integer.parseInt(JOptionPane.showInputDialog("Ingrese su nuevo PIN: "));

                int nuevoPin2 = Integer.parseInt(JOptionPane.showInputDialog("Confirme su nuevo PIN: "));
                Connection connection = BaseDeDatos.getConnection();
                AppBl.cambiarPINLogic(usuarioAutenticado, pinActual, nuevoPin1, nuevoPin2, connection);
                JOptionPane.showMessageDialog(null, "PIN cambiado con éxito.");
                connection.close();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Por favor, ingrese un PIN válido.");
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al cambiar el PIN.");
            } catch (ATMException ex) {
                JOptionPane.showMessageDialog(null, "Error al cambiar el PIN: " + ex.getMessage());
            }
        }
    });

    salirButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Gracias por usar el cajero. ¡Hasta luego!");
            System.exit(0);
        }
    });
    
    frame.add(consultarSaldoButton);
    frame.add(realizarDepositoButton);
    frame.add(realizarRetiroButton);
    frame.add(cambiarPINButton);
    frame.add(salirButton);

    frame.setVisible(true); // Muestra la ventana       
    }
}
