package bo.edu.ucb.sis213.view;

import javax.swing.*;

import bo.edu.ucb.sis213.bl.AppBl;
import bo.edu.ucb.sis213.bl.Login;
import bo.edu.ucb.sis213.bl.Usuario;
import bo.edu.ucb.sis213.bl.util.ATMException;
import bo.edu.ucb.sis213.dao.BaseDeDatos;
import bo.edu.ucb.sis213.dao.UsuarioDao;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CajeroGUI extends JFrame {
    private JTextField usuarioField;
    private JPasswordField pinField;
    private JButton ingresarButton;
    private Connection connection;
    private Usuario usuarioAutenticado;
    private int intentosRestantes = 3; // Intentos restantes por sesión


    public CajeroGUI(Connection connection) {
        this.connection = connection; // Almacena la conexión en la instancia
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
                int pin = Integer.parseInt(new String(pinChars));

                AppBl appBl = new AppBl();
                usuarioAutenticado = appBl.ingresar(usuario, pin, intentosRestantes);

                if (usuarioAutenticado != null) {
                    JOptionPane.showMessageDialog(CajeroGUI.this,
                            "Bienvenid@ " + usuarioAutenticado.getNombre() + ".",
                            "Inicio de sesión exitoso", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                    // ...
                    new CajeroGUI(usuarioAutenticado, connection); 

                } else {
                    JOptionPane.showMessageDialog(CajeroGUI.this,
                    "Ha excedido el número de intentos. La aplicación se cerrará.",
                    "Error de inicio de sesión", JOptionPane.ERROR_MESSAGE);
                    System.exit(0); // O realiza alguna otra acción si lo deseas
                }

                usuarioField.setText("");
                pinField.setText("");
                usuarioField.requestFocus();
            }
        });
    }

    
    public CajeroGUI(Usuario usuarioAutenticado, Connection connection) {
        this.usuarioAutenticado = usuarioAutenticado;
        this.connection = connection; // Almacena la conexión en la instancia
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



    consultarSaldoButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

            AppBl.consultarSaldo(usuarioAutenticado);  
        }
    });


        ActionListener realizarDepositoListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                
                String cantidadStr = JOptionPane.showInputDialog("Ingrese la cantidad a depositar: $");
                double cantidad = Double.parseDouble(cantidadStr);  
                
                System.out.println("uwwww"+cantidad);
                try {
                    AppBl.realizarDeposito(usuarioAutenticado, cantidad);
                } catch (SQLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (ATMException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }  
            }
        };
        
        realizarDepositoButton.addActionListener(realizarDepositoListener);


        realizarRetiroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              
                String cantidadStr = JOptionPane.showInputDialog("Ingrese la cantidad a retirar: $");
                double cantidad = Double.parseDouble(cantidadStr);  
                
                System.out.println("uwwww"+cantidad);
                try {
                    AppBl.realizarRetiro(usuarioAutenticado, cantidad);
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
                    int nuevoPin = Integer.parseInt(JOptionPane.showInputDialog("Ingrese su nuevo PIN: "));
                    Connection connection = BaseDeDatos.getConnection();
                    AppBl.cambiarPINLogic(usuarioAutenticado, nuevoPin, connection);
                    connection.close();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Por favor, ingrese un PIN válido.");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error al cambiar el PIN.");
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

        frame.setVisible(true); 
        
    }
}
