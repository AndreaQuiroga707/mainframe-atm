package bo.edu.ucb.sis213;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

public class CajeroGUI extends JFrame {

    private JTextField usuarioField;
    private JPasswordField pinField;
    private JButton ingresarButton;
    private Usuario usuarioAutenticado;
    private Connection connection;

    private int intentosRestantes = 3; // Intentos restantes por sesión

    public CajeroGUI() {
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
                var usuario = usuarioField.getText();
                char[] pinChars = pinField.getPassword();
                int pin = Integer.parseInt(new String(pinChars));

                Login.Ingresar(usuario, pin, intentosRestantes);

                if (Login.getUsuarioActual() != null) {
                    usuarioAutenticado = Login.getUsuarioActual();
                    //copilot how can i show a joptionpane saying welcome name user help me please
                    JOptionPane.showMessageDialog(CajeroGUI.this,
                            "Bienvenid@ " + usuarioAutenticado.getNombre() + ".",
                            "Inicio de sesión exitoso", JOptionPane.INFORMATION_MESSAGE);

                    //System.out.println("Usuario autenticado"+ usuarioAutenticado);
                    dispose(); // Cerrar la ventana de inicio de sesión
                    
                    new CajeroGUI(usuarioAutenticado, connection); 

                } else {
                    intentosRestantes--;

                    if (intentosRestantes > 0) {
                        JOptionPane.showMessageDialog(CajeroGUI.this,
                                "Usuario o PIN incorrectos. Le quedan " + intentosRestantes + " intentos.",
                                "Error de inicio de sesión", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(CajeroGUI.this,
                                "Ha excedido el número de intentos. La aplicación se cerrará.",
                                "Error de inicio de sesión", JOptionPane.ERROR_MESSAGE);
                        System.exit(0); // O realiza alguna otra acción si lo deseas
                    }

                    // Limpiar los campos para permitir nuevos intentos
                    usuarioField.setText("");
                    pinField.setText("");
                    usuarioField.requestFocus();
                }
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
                System.out.println("Consultar Saldo"+ usuarioAutenticado);
                CajeroAutomatico.consultarSaldo(usuarioAutenticado, connection); // Pasar usuarioAutenticado como argumento
            }
        });


        ActionListener realizarDepositoListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Consultar conexion "+ connection);
                CajeroAutomatico.realizarDeposito(usuarioAutenticado, connection);
            }
        };
        
        realizarDepositoButton.addActionListener(realizarDepositoListener);


        realizarRetiroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CajeroAutomatico.realizarRetiro(usuarioAutenticado, connection);
            }
        });

        cambiarPINButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CajeroAutomatico.cambiarPIN(usuarioAutenticado, connection);
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CajeroGUI().setVisible(true);
            }
        });
    }
}