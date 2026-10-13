import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {

    public Main() {
        setTitle("SecureBank | Digital Banking");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        showWelcome();
    }

    private void showWelcome() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(232, 95, 150));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel logo = new JLabel("♡ SECUREBANK ♡");
        logo.setFont(new Font("Serif", Font.BOLD, 38));
        logo.setForeground(new Color(10, 80, 125));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("SMART BANKING. SECURE FUTURE");
        subtitle.setFont(new Font("Serif", Font.ITALIC, 17));
        subtitle.setForeground(new Color(150, 90, 120));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel welcome = new JLabel("Your money, your future , your dreams.");
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 25));
        welcome.setForeground(Color.WHITE);
        welcome.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton login = createRoundedButton("SIGN IN",
                new Color(245, 183, 61), Color.BLACK);
        JButton register = createRoundedButton("CREATE ACCOUNT",
                Color.WHITE, Color.BLACK);

        login.setAlignmentX(Component.CENTER_ALIGNMENT);
        register.setAlignmentX(Component.CENTER_ALIGNMENT);

        Dimension bigButton = new Dimension(240, 55);
        login.setPreferredSize(bigButton);
        login.setMaximumSize(bigButton);
        register.setPreferredSize(bigButton);
        register.setMaximumSize(bigButton);

        Font bigButtonFont = new Font("Segoe UI", Font.BOLD, 16);
        login.setFont(bigButtonFont);
        register.setFont(bigButtonFont);

        login.addActionListener(e -> showLogin());
        register.addActionListener(e -> showRegister());

        panel.add(Box.createVerticalStrut(100));
        panel.add(logo);
        panel.add(Box.createVerticalStrut(20));
        panel.add(subtitle);
        panel.add(Box.createVerticalStrut(50));
        panel.add(welcome);
        panel.add(Box.createVerticalStrut(40));
        panel.add(login);
        panel.add(Box.createVerticalStrut(20));
        panel.add(register);

        add(panel);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(
                UIManager.getCrossPlatformLookAndFeelClassName()
            );
        } catch (Exception ignored) { }

        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }

    // ============================================================
    //  REGISTER
    // ============================================================
    private void showRegister() {

        JFrame frame = new JFrame("SecureBank - Create Account");
        frame.setSize(450, 560);
        frame.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(
            javax.swing.BorderFactory.createEmptyBorder(40, 50, 40, 50));
        panel.setBackground(new Color(255, 240, 245));

        JLabel title = new JLabel("CREATE ACCOUNT");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(190, 105, 145));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        Color labelColor = new Color(120, 55, 90);
        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);

        JLabel nameLabel = new JLabel("Full Name");
        JLabel userLabel = new JLabel("Username");
        JLabel passLabel = new JLabel("Password");

        nameLabel.setForeground(labelColor);
        userLabel.setForeground(labelColor);
        passLabel.setForeground(labelColor);

        nameLabel.setFont(labelFont);
        userLabel.setFont(labelFont);
        passLabel.setFont(labelFont);

        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        passLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField name = styledField();
        JTextField username = styledField();
        JPasswordField password = styledPassword();

        JButton create = createRoundedButton("REGISTER NOW",
                new Color(225, 126, 169), Color.WHITE);
        create.setAlignmentX(Component.CENTER_ALIGNMENT);
        create.setFont(new Font("Segoe UI", Font.BOLD, 15));

        Dimension createSize = new Dimension(220, 45);
        create.setPreferredSize(createSize);
        create.setMaximumSize(createSize);

        panel.add(title);
        panel.add(Box.createVerticalStrut(35));
        panel.add(nameLabel);
        panel.add(Box.createVerticalStrut(8));
        panel.add(name);
        panel.add(Box.createVerticalStrut(20));
        panel.add(userLabel);
        panel.add(Box.createVerticalStrut(8));
        panel.add(username);
        panel.add(Box.createVerticalStrut(20));
        panel.add(passLabel);
        panel.add(Box.createVerticalStrut(8));
        panel.add(password);
        panel.add(Box.createVerticalStrut(35));
        panel.add(create);

        create.addActionListener(e -> {

            if (name.getText().trim().isEmpty()
                    || username.getText().trim().isEmpty()
                    || password.getPassword().length == 0) {
                JOptionPane.showMessageDialog(
                    frame, "Please fill in all fields!");
                return;
            }

            if (password.getPassword().length < 8) {
                JOptionPane.showMessageDialog(
                    frame,
                    "Password must have at least 8 characters!");
                return;
            }

            try {
                java.io.File folder = new java.io.File("data");
                folder.mkdirs();

                java.io.File file =
                    new java.io.File(folder, "customers.txt");

                String fullName = name.getText().trim();
                String userName = username.getText().trim();
                String plainPassword =
                    new String(password.getPassword());

                // ---- SECURITY: hash the password ----
                String hashed = PasswordUtil.hash(plainPassword);

                String accountNumber = generateAccountNumber(file);

                Customer c = new Customer(
                    fullName, userName, hashed, accountNumber
                );

                java.io.FileWriter writer =
                    new java.io.FileWriter(file, true);
                writer.write(c.toFileLine() + System.lineSeparator());
                writer.close();

                // Create per-user files
                java.io.File txFile = new java.io.File(
                    "data/transactions_" + accountNumber + ".txt");
                if (!txFile.exists()) txFile.createNewFile();

                java.io.File balFile = new java.io.File(
                    "data/balance_" + accountNumber + ".txt");
                if (!balFile.exists()) {
                    try (java.io.FileWriter bw =
                             new java.io.FileWriter(balFile)) {
                        bw.write("0.00");
                    }
                }

                JOptionPane.showMessageDialog(
                    frame,
                    "Registration successful!\n" +
                    "Welcome to SecureBank, " + fullName + "!\n" +
                    "Your account number is: " + accountNumber
                );

                frame.dispose();

            } catch (java.io.IOException ex) {
                JOptionPane.showMessageDialog(
                    frame, "Error saving customer information!");
            }
        });

        frame.add(panel);
        frame.setVisible(true);
    }

    // ============================================================
    //  LOGIN
    // ============================================================
    private void showLogin() {

        JFrame frame = new JFrame("SecureBank - Sign In");
        frame.setSize(450, 520);
        frame.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(
            javax.swing.BorderFactory.createEmptyBorder(45, 50, 45, 50));
        panel.setBackground(new Color(255, 240, 245));

        JLabel title = new JLabel("WELCOME BACK");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(19));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setForeground(new Color(180, 147, 226));
        usernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField usernameField = new JTextField();
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameField.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 42));
        usernameField.setPreferredSize(new Dimension(300, 42));
        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        usernameField.setBorder(
            javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(
                    new Color(180, 147, 226), 1),
                javax.swing.BorderFactory.createEmptyBorder(8, 12, 8, 12)));

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setForeground(new Color(180, 147, 226));
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 42));
        passwordField.setPreferredSize(new Dimension(300, 42));
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordField.setBorder(
            javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(
                    new Color(180, 147, 226), 1),
                javax.swing.BorderFactory.createEmptyBorder(8, 12, 8, 12)));

        JButton signIn = createRoundedButton("SIGN IN",
                new Color(245, 183, 61), Color.BLACK);
        signIn.setAlignmentX(Component.CENTER_ALIGNMENT);
        signIn.setFont(new Font("Segoe UI", Font.BOLD, 15));

        Dimension buttonSize = new Dimension(200, 45);
        signIn.setPreferredSize(buttonSize);
        signIn.setMaximumSize(buttonSize);

        panel.add(title);
        panel.add(Box.createVerticalStrut(45));
        panel.add(usernameLabel);
        panel.add(Box.createVerticalStrut(8));
        panel.add(usernameField);
        panel.add(Box.createVerticalStrut(25));
        panel.add(passwordLabel);
        panel.add(Box.createVerticalStrut(8));
        panel.add(passwordField);
        panel.add(Box.createVerticalStrut(40));
        panel.add(signIn);

        signIn.addActionListener(e -> {

            String enteredUsername =
                usernameField.getText().trim();
            String enteredPassword =
                new String(passwordField.getPassword());

            boolean authenticated = false;

            try (
                java.io.BufferedReader reader =
                    new java.io.BufferedReader(
                        new java.io.FileReader(
                            "data/customers.txt"))
            ) {
                String line;
                while ((line = reader.readLine()) != null) {
                    Customer c = Customer.fromFileLine(line);
                    if (c == null) continue;

                    if (c.getUsername().equals(enteredUsername)) {
                        // ---- SECURITY: verify hash ----
                        if (PasswordUtil.verify(
                                enteredPassword,
                                c.getPasswordHash())) {
                            authenticated = true;
                        }
                        break;
                    }
                }
            } catch (java.io.IOException ex) {
                JOptionPane.showMessageDialog(
                    frame, "Unable to read customer records.");
                return;
            }

            if (authenticated) {
                JOptionPane.showMessageDialog(
                    frame, "Login successful! Welcome back!");
                frame.dispose();
                showDashboard(enteredUsername);
            } else {
                JOptionPane.showMessageDialog(
                    frame,
                    "Incorrect username or password!",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.add(panel);
        frame.setVisible(true);
    }

    // ============================================================
    //  HELPERS
    // ============================================================
    private JTextField styledField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 42));
        field.setPreferredSize(new Dimension(300, 42));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setBorder(
            javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(
                    new Color(245, 194, 215), 1),
                javax.swing.BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        return field;
    }

    private JPasswordField styledPassword() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 42));
        field.setPreferredSize(new Dimension(300, 42));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setBorder(
            javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(
                    new Color(245, 194, 215), 1),
                javax.swing.BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        return field;
    }

    private String[] findCustomer(String username) {
        try (
            java.io.BufferedReader reader =
                new java.io.BufferedReader(
                    new java.io.FileReader("data/customers.txt"))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split("\\|", -1);
                if (details.length >= 3
                    && details[1].equalsIgnoreCase(username)) {
                    return details;
                }
            }
        } catch (java.io.IOException e) {
            JOptionPane.showMessageDialog(
                this, "Could not load customer information.");
        }
        return null;
    }

    // ============================================================
    //  DASHBOARD
    // ============================================================
    private void showDashboard(String findcustomerName) {

        JFrame dashboard = new JFrame("SecureBank Dashboard");
        dashboard.setSize(1000, 650);
        dashboard.setLocationRelativeTo(this);
        dashboard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 247, 251));

        String[] customer = findCustomer(findcustomerName);
        String fullName = (customer != null) ? customer[0] : findcustomerName;
        String accountNumber = (customer != null && customer.length >= 4)
            ? customer[3] : "SB20260000";

        // SIDEBAR
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(230, 650));
        sidebar.setBackground(new Color(15, 23, 42));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(
            javax.swing.BorderFactory.createEmptyBorder(30, 20, 30, 20));

        JLabel logo = new JLabel("♡ SECUREBANK ♡");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 23));
        logo.setForeground(new Color(245, 183, 61));
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(logo);
        sidebar.add(Box.createVerticalStrut(50));

        JLabel menu = new JLabel("MAIN MENU");
        menu.setForeground(Color.LIGHT_GRAY);
        menu.setFont(new Font("Segoe UI", Font.BOLD, 12));
        menu.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(menu);
        sidebar.add(Box.createVerticalStrut(25));

        JLabel dashboardLabel = new JLabel("▦  Dashboard");
        dashboardLabel.setForeground(Color.WHITE);
        dashboardLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        dashboardLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(dashboardLabel);
        sidebar.add(Box.createVerticalStrut(25));

        JButton logout = new JButton("LOG OUT");
        logout.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(logout);

        // CONTENT
        JPanel content = new JPanel();
        content.setBackground(new Color(255, 243, 248));
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(
            javax.swing.BorderFactory.createEmptyBorder(35, 35, 35, 35));

        JLabel welcome = new JLabel("Welcome back, " + fullName);
        welcome.setFont(new Font("Serif", Font.BOLD, 32));
        welcome.setForeground(new Color(160, 65, 110));
        welcome.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(welcome);

        JLabel accountNumberLabel =
            new JLabel("ACCOUNT NUMBER: " + accountNumber);
        accountNumberLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        accountNumberLabel.setForeground(new Color(16, 160, 105));
        accountNumberLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(Box.createVerticalStrut(15));
        content.add(accountNumberLabel);

        JPanel luxuryCard = createLuxuryBankCard(
            fullName, accountNumber, "Savings Account");
        luxuryCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(Box.createVerticalStrut(20));
        content.add(luxuryCard);
        content.add(Box.createVerticalStrut(20));

        // PROFILE
        JPanel profileCard = new JPanel();
        profileCard.setLayout(new BoxLayout(profileCard, BoxLayout.Y_AXIS));
        profileCard.setBackground(new Color(255, 250, 252));
        profileCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        profileCard.setBorder(
            javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(
                    new Color(245, 194, 215)),
                javax.swing.BorderFactory.createEmptyBorder(25, 25, 25, 25)));

        JLabel profileTitle = new JLabel("CUSTOMER PROFILE");
        profileTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        profileTitle.setForeground(new Color(15, 23, 42));
        profileTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel accountType = new JLabel("Account Type: Savings Account");
        accountType.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        accountType.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel accountStatus = new JLabel("Account Status: ACTIVE");
        accountStatus.setFont(new Font("Segoe UI", Font.BOLD, 15));
        accountStatus.setForeground(new Color(16, 160, 105));
        accountStatus.setAlignmentX(Component.LEFT_ALIGNMENT);

        profileCard.add(profileTitle);
        profileCard.add(Box.createVerticalStrut(15));
        profileCard.add(accountType);
        profileCard.add(Box.createVerticalStrut(10));
        profileCard.add(accountStatus);
        profileCard.setMaximumSize(new Dimension(
            Integer.MAX_VALUE, profileCard.getPreferredSize().height));

        content.add(profileCard);
        content.add(Box.createVerticalStrut(15));

        JLabel subtitle = new JLabel("Here is your financial overview.");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        subtitle.setForeground(Color.GRAY);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(subtitle);
        content.add(Box.createVerticalStrut(35));

        // BALANCE
        JPanel balanceCard = new JPanel();
        balanceCard.setLayout(new BoxLayout(balanceCard, BoxLayout.Y_AXIS));
        balanceCard.setBackground(new Color(190, 105, 145));
        balanceCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        balanceCard.setBorder(
            javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(
                    new Color(238, 174, 202), 2),
                javax.swing.BorderFactory.createEmptyBorder(35, 35, 35, 35)));

        JLabel balanceTitle = new JLabel("TOTAL ACCOUNT BALANCE");
        balanceTitle.setForeground(Color.PINK);
        balanceTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        balanceTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel balance = new JLabel("US$ 0.00");
        java.io.File balanceFile = new java.io.File(
            "data/balance_" + accountNumber + ".txt");

        try {
            if (balanceFile.exists() && balanceFile.length() > 0) {
                java.util.Scanner scanner = new java.util.Scanner(balanceFile);
                String savedBalance = scanner.nextLine();
                balance.setText(String.format(
                    "US$ %.2f", Double.parseDouble(savedBalance)));
                scanner.close();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                dashboard, "Could not load saved balance.");
        }

        balance.setForeground(Color.WHITE);
        balance.setFont(new Font("Segoe UI", Font.BOLD, 38));
        balance.setAlignmentX(Component.LEFT_ALIGNMENT);

        balanceCard.add(balanceTitle);
        balanceCard.add(Box.createVerticalStrut(15));
        balanceCard.add(balance);
        balanceCard.setMaximumSize(new Dimension(
            Integer.MAX_VALUE,
            balanceCard.getPreferredSize().height + 10));

        content.add(balanceCard);
        content.add(Box.createVerticalStrut(30));

        // BUTTONS
        JButton deposit = createRoundedButton("♡  DEPOSIT MONEY",
            new Color(16, 160, 105), Color.WHITE);
        JButton withdraw = createRoundedButton("♡  WITHDRAW MONEY",
            new Color(220, 65, 75), Color.WHITE);
        JButton history = createRoundedButton("♡  TRANSACTION HISTORY",
            new Color(245, 183, 61), Color.BLACK);

        Dimension bSize = new Dimension(260, 45);
        deposit.setPreferredSize(bSize);
        deposit.setMaximumSize(bSize);
        withdraw.setPreferredSize(bSize);
        withdraw.setMaximumSize(bSize);
        history.setPreferredSize(bSize);
        history.setMaximumSize(bSize);

        deposit.setAlignmentX(Component.LEFT_ALIGNMENT);
        withdraw.setAlignmentX(Component.LEFT_ALIGNMENT);
        history.setAlignmentX(Component.LEFT_ALIGNMENT);

        content.add(deposit);
        content.add(Box.createVerticalStrut(15));
        content.add(withdraw);
        content.add(Box.createVerticalStrut(15));
        content.add(history);

        mainPanel.add(sidebar, BorderLayout.WEST);

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        dashboard.add(mainPanel);

        // DEPOSIT
        deposit.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(
                dashboard, "Enter deposit amount:",
                "Deposit Money", JOptionPane.QUESTION_MESSAGE);
            if (input != null) {
                try {
                    double amount = Double.parseDouble(input);
                    if (amount <= 0 || !Double.isFinite(amount)) {
                        JOptionPane.showMessageDialog(dashboard,
                            "Enter a valid positive amount!");
                    } else {
                        double current = Double.parseDouble(
                            balance.getText().replace("US$ ", ""));
                        current += amount;
                        balance.setText(String.format("US$ %.2f", current));

                        try (java.io.FileWriter writer =
                             new java.io.FileWriter(
                                 "data/balance_" + accountNumber + ".txt")) {
                            writer.write(String.format("%.2f", current));
                        } catch (java.io.IOException ex) {
                            JOptionPane.showMessageDialog(dashboard,
                                "Could not save balance!");
                        }

                        try (java.io.FileWriter writer =
                             new java.io.FileWriter(
                                 "data/transactions_" + accountNumber + ".txt",
                                 true)) {
                            writer.write(
                                "DEPOSIT | Amount: US$ "
                                + String.format("%.2f", amount)
                                + " | Date: "
                                + java.time.LocalDateTime.now()
                                + System.lineSeparator());
                        } catch (java.io.IOException ex) {
                            JOptionPane.showMessageDialog(dashboard,
                                "Could not save transaction!");
                        }

                        JOptionPane.showMessageDialog(dashboard,
                            "Deposit successful!");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dashboard,
                        "Please enter a valid number!");
                }
            }
        });

        // WITHDRAW
        withdraw.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(
                dashboard, "Enter withdrawal amount:",
                "Withdraw Money", JOptionPane.QUESTION_MESSAGE);
            if (input != null) {
                try {
                    double amount = Double.parseDouble(input);
                    double current = Double.parseDouble(
                        balance.getText().replace("US$ ", ""));

                    if (amount <= 0 || !Double.isFinite(amount)) {
                        JOptionPane.showMessageDialog(dashboard,
                            "Enter a valid positive amount!");
                    } else if (amount > current) {
                        JOptionPane.showMessageDialog(dashboard,
                            "Insufficient funds!");
                    } else {
                        current -= amount;
                        balance.setText(String.format("US$ %.2f", current));

                        try (java.io.FileWriter writer =
                             new java.io.FileWriter(
                                 "data/balance_" + accountNumber + ".txt")) {
                            writer.write(String.format("%.2f", current));
                        } catch (java.io.IOException ex) {
                            JOptionPane.showMessageDialog(dashboard,
                                "Could not save balance!");
                        }

                        try (java.io.FileWriter writer =
                             new java.io.FileWriter(
                                 "data/transactions_" + accountNumber + ".txt",
                                 true)) {
                            writer.write(
                                "WITHDRAWAL | Amount: US$ "
                                + String.format("%.2f", amount)
                                + " | Date: "
                                + java.time.LocalDateTime.now()
                                + System.lineSeparator());
                        } catch (java.io.IOException ex) {
                            JOptionPane.showMessageDialog(dashboard,
                                "Could not save withdrawal transaction!");
                        }

                        JOptionPane.showMessageDialog(dashboard,
                            "Withdrawal successful!");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dashboard,
                        "Please enter a valid number!");
                }
            }
        });

        // HISTORY
        history.addActionListener(e -> {
            javax.swing.table.DefaultTableModel model =
                new javax.swing.table.DefaultTableModel(
                    new String[]{"Transaction", "Amount", "Date"}, 0);

            java.io.File txFile = new java.io.File(
                "data/transactions_" + accountNumber + ".txt");

            if (txFile.exists()) {
                try (java.io.BufferedReader reader =
                     new java.io.BufferedReader(
                         new java.io.FileReader(txFile))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split("\\|");
                        if (parts.length >= 3) {
                            model.addRow(new Object[]{
                                parts[0].trim(),
                                parts[1].replace("Amount:", "").trim(),
                                parts[2].replace("Date:", "").trim()
                            });
                        }
                    }
                } catch (java.io.IOException ex) {
                    JOptionPane.showMessageDialog(dashboard,
                        "Could not load transaction history!");
                    return;
                }
            }

            javax.swing.JTable table = new javax.swing.JTable(model);
            table.setRowHeight(30);
            table.setFont(new java.awt.Font(
                "Segoe UI", java.awt.Font.PLAIN, 13));
            table.getTableHeader().setFont(new java.awt.Font(
                "Segoe UI", java.awt.Font.BOLD, 13));

            javax.swing.JScrollPane scroll =
                new javax.swing.JScrollPane(table);
            scroll.setPreferredSize(new java.awt.Dimension(750, 350));

            JOptionPane.showMessageDialog(dashboard, scroll,
                "SECUREBANK | TRANSACTION HISTORY",
                JOptionPane.PLAIN_MESSAGE);
        });

        // LOGOUT
        logout.addActionListener(e -> {
            dashboard.dispose();
            showWelcome();
        });

        dashboard.setVisible(true);
    }

    // ============================================================
    //  ROUNDED BUTTON
    // ============================================================
    private JButton createRoundedButton(String text,
                                        Color background,
                                        Color foreground) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
                int radius = getHeight();
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(),
                    radius, radius);
                g2.dispose();
                super.paintComponent(g);
            }
            @Override
            protected void paintBorder(Graphics g) { }
        };

        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(javax.swing.BorderFactory.createEmptyBorder(
            12, 24, 12, 24));
        return button;
    }

    // ============================================================
    //  LUXURY CARD
    // ============================================================
    private JPanel createLuxuryBankCard(String customerName,
                                        String accountNumber,
                                        String accountType) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(238, 155, 190),
                    getWidth(), getHeight(),
                    new Color(183, 110, 145));
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 35, 35);

                g2.setColor(new Color(255, 255, 255, 35));
                g2.fillOval(getWidth() - 110, -35, 150, 150);
                g2.fillOval(getWidth() - 70, 25, 100, 100);
                g2.dispose();
            }
        };

                card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(false);
        card.setBorder(javax.swing.BorderFactory.createEmptyBorder(
            22, 25, 22, 25));
        card.setPreferredSize(new Dimension(420, 230));
        card.setMaximumSize(new Dimension(460, 240));

        JLabel bankName = new JLabel("♡ SECUREBANK ♡");
        bankName.setFont(new Font("Serif", Font.BOLD, 23));
        bankName.setForeground(Color.WHITE);
        bankName.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel cardType = new JLabel(
            "PREMIUM " + accountType.toUpperCase()
        );
        cardType.setFont(new Font("SansSerif", Font.BOLD, 12));
        cardType.setForeground(new Color(255, 240, 245));
        cardType.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel chip = new JLabel("▦");
        chip.setFont(new Font("SansSerif", Font.PLAIN, 35));
        chip.setForeground(new Color(255, 235, 205));
        chip.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel account = new JLabel("ACCOUNT  " + accountNumber);
        account.setFont(new Font("Monospaced", Font.BOLD, 15));
        account.setForeground(Color.WHITE);
        account.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel name = new JLabel(customerName.toUpperCase());
        name.setFont(new Font("SansSerif", Font.BOLD, 17));
        name.setForeground(Color.WHITE);
        name.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(bankName);
        card.add(Box.createVerticalStrut(5));
        card.add(cardType);
        card.add(Box.createVerticalStrut(12));
        card.add(chip);
        card.add(Box.createVerticalStrut(8));
        card.add(account);
        card.add(Box.createVerticalStrut(8));
        card.add(name);

        return card;
    }

    // ============================================================
    //  ACCOUNT NUMBER GENERATOR
    // ============================================================
    private String generateAccountNumber(java.io.File customerFile) {

        int maxNumber = 0;

        if (customerFile.exists()) {
            try (
                java.io.BufferedReader reader =
                    new java.io.BufferedReader(
                        new java.io.FileReader(customerFile)
                    )
            ) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split("\\|", -1);
                    if (parts.length >= 4) {
                        String numPart = parts[3]
                            .replace("SB2026", "")
                            .trim();
                        try {
                            int n = Integer.parseInt(numPart);
                            if (n > maxNumber) maxNumber = n;
                        } catch (NumberFormatException ignored) { }
                    }
                }
            } catch (java.io.IOException ignored) { }
        }

        return String.format("SB2026%04d", maxNumber + 1);
    }
}