package bo.edu.ucb.sis213;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ATMApp {

    // Frame para las pantallas
    private JFrame frame;

    // Componentes para la pantalla de login
    private JPanel loginPanel;
    private JTextField userField;
    private JPasswordField pinField;
    private JButton loginButton;

    // Componentes para el menú principal
    private JPanel mainMenuPanel;
    private JButton checkBalanceButton;
    private JButton depositButton;
    private JButton withdrawButton;
    private JButton changePinButton;
    private JButton exitButton;

    public ATMApp() {
        frame = new JFrame("ATM App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);

        // Inicializar la pantalla de login
        initLoginScreen();

        frame.setVisible(true);
    }

    private void initLoginScreen() {
        loginPanel = new JPanel(new GridLayout(3, 2));

        userField = new JTextField();
        pinField = new JPasswordField();

        loginButton = new JButton("Ingresar");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Aquí puedes agregar una validación de las credenciales
                // Por ahora solo mostraremos el menú principal al presionar el botón
                showMainMenu();
            }
        });

        loginPanel.add(new JLabel("Usuario:"));
        loginPanel.add(userField);
        loginPanel.add(new JLabel("PIN:"));
        loginPanel.add(pinField);
        loginPanel.add(new JLabel());
        loginPanel.add(loginButton);

        frame.add(loginPanel);
    }

    private void showMainMenu() {
        frame.remove(loginPanel);

        mainMenuPanel = new JPanel(new GridLayout(5, 1));

        checkBalanceButton = new JButton("Consultar saldo");
        depositButton = new JButton("Realizar un depósito");
        withdrawButton = new JButton("Realizar un retiro");
        changePinButton = new JButton("Cambiar PIN");
        exitButton = new JButton("Salir");

        // Para simplificar, solo agregaremos una acción al botón de salir
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        mainMenuPanel.add(checkBalanceButton);
        mainMenuPanel.add(depositButton);
        mainMenuPanel.add(withdrawButton);
        mainMenuPanel.add(changePinButton);
        mainMenuPanel.add(exitButton);

        frame.add(mainMenuPanel);
        frame.revalidate();
        frame.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ATMApp();
            }
        });
    }
}
