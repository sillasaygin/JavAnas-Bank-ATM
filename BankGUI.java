import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class BankGUI extends JFrame {
    private BankManager bankManager = new BankManager();
    private final String DATA_FILE = "accounts.txt";
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private BankAccount currentUser;

    // Components Requiring Updates
    private JLabel lblCheckBalance, lblCheckWelcome;
    private JLabel lblSaveBalance, lblSaveWelcome, lblSaveInterest;

    public BankGUI() {
        loadData();
        setTitle("JavAnas Bank");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Add Panels
        mainPanel.add(createWelcomePanel(), "Welcome");
        mainPanel.add(createLoginPanel(), "Login");
        mainPanel.add(createRegisterPanel(), "Register");
        mainPanel.add(createSelectionPanel(), "Selection");

        // Separate Dashboards
        mainPanel.add(createCheckingDashboard(), "CheckingDashboard");
        mainPanel.add(createSavingDashboard(), "SavingDashboard");

        add(mainPanel);
        showPanel("Welcome");
    }

    private void showPanel(String name) {
        cardLayout.show(mainPanel, name);
    }

    private void loadData() {
        try {
            File f = new File(DATA_FILE);
            if (f.exists())
                bankManager.loadAccounts(DATA_FILE);
        } catch (IOException e) {
            showMessage("Error loading data: " + e.getMessage());
        }
    }

    private void saveData() {
        try {
            bankManager.saveAccounts(DATA_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Helper Methods
    private JButton createButton(String text, ActionListener action) {
        JButton btn = new JButton(text);
        btn.addActionListener(action);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        return btn;
    }

    private JPanel createFormPanel(JComponent... components) {
        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        for (JComponent c : components)
            panel.add(c);
        return panel;
    }

    private void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    // Welcome, Login, Register
    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel title = new JLabel("Welcome to JavAnas Bank", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(title);
        panel.add(createButton("Log In", e -> showPanel("Login")));
        panel.add(createButton("Create Account", e -> showPanel("Register")));
        return panel;
    }

    private JPanel createLoginPanel() {
        JTextField accNumField = new JTextField();
        JPasswordField passField = new JPasswordField();
        return createFormPanel(
                new JLabel("Account Number:"), accNumField,
                new JLabel("Password:"), passField,
                createButton("Confirm", e -> {
                    BankAccount acc = bankManager.authenticate(accNumField.getText(),
                            new String(passField.getPassword()));
                    if (acc != null) {
                        currentUser = acc;
                        accNumField.setText("");
                        passField.setText("");

                        if (currentUser instanceof SavingAccount) {
                            updateSavingDashboard();
                            showPanel("SavingDashboard");
                        } else {
                            updateSelectionPanel();
                            showPanel("Selection");
                        }
                    } else {
                        showMessage("Invalid Credentials");
                    }
                }),
                createButton("Back", e -> showPanel("Welcome")));
    }

    private JPanel createRegisterPanel() {
        JTextField nameF = new JTextField(), surnameF = new JTextField();
        JTextField accF = new JTextField();
        JPasswordField passF = new JPasswordField();

        return createFormPanel(
                new JLabel("Name:"), nameF, new JLabel("Surname:"), surnameF,
                new JLabel("Account Number:"), accF, new JLabel("Password:"), passF,
                createButton("Register", e -> {
                    if (bankManager.createAccount(accF.getText(), nameF.getText(), surnameF.getText(),
                            new String(passF.getPassword()))) {
                        saveData();
                        showMessage("Account Created! Please Login.");
                        nameF.setText("");
                        surnameF.setText("");
                        accF.setText("");
                        passF.setText("");
                        showPanel("Login");
                    } else {
                        showMessage("Invalid Input or Account Exists");
                    }
                }),
                createButton("Back", e -> showPanel("Welcome")));
    }

    // Selection Panel
    private JPanel selectionContainer = new JPanel(new GridLayout(0, 1, 10, 10));

    private JPanel createSelectionPanel() {
        selectionContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        return selectionContainer;
    }

    private void updateSelectionPanel() {
        selectionContainer.removeAll();
        selectionContainer.add(new JLabel("Select Account", SwingConstants.CENTER));

        selectionContainer.add(createButton("Checking: " + currentUser.getAccountNumber(), e -> {
            if (currentUser instanceof SavingAccount) {
                String parentId = currentUser.getAccountNumber().replace("-S", "");
                currentUser = bankManager.findAccount(parentId);
            }
            updateCheckingDashboard();
            showPanel("CheckingDashboard");
        }));

        String savingId = currentUser.getAccountNumber() + "-S";
        if (currentUser instanceof SavingAccount)
            savingId = currentUser.getAccountNumber();
        BankAccount saving = bankManager.findAccount(savingId);

        if (saving != null) {
            final BankAccount sAcc = saving;
            selectionContainer.add(createButton("Saving: " + sAcc.getAccountNumber(), e -> {
                currentUser = sAcc;
                updateSavingDashboard();
                showPanel("SavingDashboard");
            }));
        } else {
            selectionContainer.add(createButton("Create Saving Account", e -> {
                String input = JOptionPane.showInputDialog(this, "Initial Deposit:");
                if (input != null) {
                    try {
                        if (bankManager.createSavingAccount((CheckingAccount) currentUser, Double.parseDouble(input))) {
                            saveData();
                            showMessage("Saving Account Created!");
                            updateSelectionPanel();
                        }
                    } catch (Exception ex) {
                        showMessage("Error: " + ex.getMessage());
                    }
                }
            }));
        }
        selectionContainer.add(createButton("Logout", e -> {
            currentUser = null;
            showPanel("Welcome");
        }));
        selectionContainer.revalidate();
        selectionContainer.repaint();
    }

    // Checking Dashboard
    private JPanel createCheckingDashboard() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel header = new JPanel(new GridLayout(3, 1));
        lblCheckWelcome = new JLabel("Welcome", SwingConstants.CENTER);
        lblCheckWelcome.setFont(new Font("Arial", Font.BOLD, 18));
        lblCheckBalance = new JLabel("", SwingConstants.CENTER);
        header.add(lblCheckWelcome);
        header.add(new JLabel("CHECKING ACCOUNT", SwingConstants.CENTER));
        header.add(lblCheckBalance);
        panel.add(header, BorderLayout.NORTH);

        JPanel actions = new JPanel(new GridLayout(3, 1, 10, 10));
        actions.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        actions.add(createButton("Deposit", e -> doTransaction("DEPOSIT")));
        actions.add(createButton("Withdraw", e -> doTransaction("WITHDRAW")));
        actions.add(createButton("Transfer", e -> doTransaction("TRANSFER")));
        panel.add(actions, BorderLayout.CENTER);

        panel.add(createButton("Back to Selection", e -> {
            updateSelectionPanel();
            showPanel("Selection");
        }), BorderLayout.SOUTH);
        return panel;
    }

    private void updateCheckingDashboard() {
        if (currentUser == null)
            return;
        lblCheckWelcome.setText("Hello, " + currentUser.getName());
        lblCheckBalance.setText("Balance: $" + currentUser.getBalance());
    }

    // Saving Dashboard
    private JPanel createSavingDashboard() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel header = new JPanel(new GridLayout(4, 1));
        lblSaveWelcome = new JLabel("Welcome", SwingConstants.CENTER);
        lblSaveWelcome.setFont(new Font("Arial", Font.BOLD, 18));
        lblSaveBalance = new JLabel("", SwingConstants.CENTER);
        lblSaveInterest = new JLabel("", SwingConstants.CENTER);

        header.add(lblSaveWelcome);
        header.add(new JLabel("SAVING ACCOUNT", SwingConstants.CENTER));
        header.add(lblSaveBalance);
        header.add(lblSaveInterest);
        panel.add(header, BorderLayout.NORTH);

        JPanel actions = new JPanel(new GridLayout(2, 1, 10, 10));
        actions.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        actions.add(createButton("Add Money (from Checking)", e -> {
            String parentId = currentUser.getAccountNumber().replace("-S", "");
            doTransferBetweenAccounts(parentId, currentUser.getAccountNumber(), "Deposit");
        }));

        actions.add(createButton("Withdraw (to Checking)", e -> {
            String parentId = currentUser.getAccountNumber().replace("-S", "");
            doTransferBetweenAccounts(currentUser.getAccountNumber(), parentId, "Withdrawal");
        }));

        panel.add(actions, BorderLayout.CENTER);
        panel.add(createButton("Back to Selection", e -> {
            // Restore Parent Context Logic
            String parentId = currentUser.getAccountNumber().replace("-S", "");
            currentUser = bankManager.findAccount(parentId);
            updateSelectionPanel();
            showPanel("Selection");
        }), BorderLayout.SOUTH);
        return panel;
    }

    private void updateSavingDashboard() {
        if (currentUser == null)
            return;
        lblSaveWelcome.setText("Hello, " + currentUser.getName());
        lblSaveBalance.setText("Balance: $" + currentUser.getBalance());
        if (currentUser instanceof SavingAccount) {
            double proj = ((SavingAccount) currentUser).calculateDailyInterestProjection();
            lblSaveInterest.setText(String.format("Maturity: $%.2f", proj));
        }
    }

    // Transaction Handling
    private void doTransaction(String type) {
        try {
            String input = JOptionPane.showInputDialog(this, "Enter Amount:");
            if (input == null)
                return;
            double amt = Double.parseDouble(input);

            if (type.equals("DEPOSIT"))
                currentUser.deposit(amt);
            else if (type.equals("WITHDRAW"))
                currentUser.withdraw(amt);
            else if (type.equals("TRANSFER")) {
                String target = JOptionPane.showInputDialog(this, "Target Account:");
                if (target != null)
                    bankManager.transfer(currentUser, target, amt);
            }
            saveData();
            showMessage("Success!");
            updateCheckingDashboard();
        } catch (Exception e) {
            showMessage("Error: " + e.getMessage());
        }
    }

    private void doTransferBetweenAccounts(String fromId, String toId, String opName) {
        try {
            String input = JOptionPane.showInputDialog(this, "Amount to " + opName + ":");
            if (input == null)
                return;
            BankAccount from = bankManager.findAccount(fromId);
            bankManager.transfer(from, toId, Double.parseDouble(input));
            saveData();
            showMessage("Success!");
            updateSavingDashboard();
        } catch (Exception e) {
            showMessage("Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BankGUI().setVisible(true));
    }
}
