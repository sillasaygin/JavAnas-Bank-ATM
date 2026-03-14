
/*
 Represents a generic bank account.
 Manages balance and basic operations.
 */
public abstract class BankAccount {
    // Abstract method to get account type description
    public abstract String getAccountType();

    // Private fields
    private String accountNumber;
    private String name;
    private String surname;
    private String password;
    private double balance;

    public BankAccount(String accountNumber, String name, String surname, String password, double balance) {
        this.accountNumber = accountNumber;
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.balance = balance;
    }

    // Factory method to restore an account from a data string.
    // Encapsulates the file format logic within the class itself.

    public static BankAccount restoreFromData(String dataLine) {
        String[] parts = dataLine.split(",");
        if (parts.length < 5)
            return null;

        String accountNumber = parts[0];
        String name = parts[1];
        String surname = parts[2];
        String password = parts[3];
        double balance = Double.parseDouble(parts[4]);

        if (accountNumber.endsWith("-S")) {
            return new SavingAccount(accountNumber, name, surname, password, balance);
        } else {
            return new CheckingAccount(accountNumber, name, surname, password, balance);
        }
    }

    // Public getters
    public String getAccountNumber() {
        return accountNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws BankException {
        if (name == null || name.trim().isEmpty()) {
            throw new BankException("Name cannot be empty", BankException.INVALID_INPUT);
        }
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) throws BankException {
        if (surname == null || surname.trim().isEmpty()) {
            throw new BankException("Surname cannot be empty", BankException.INVALID_INPUT);
        }
        this.surname = surname;
    }

    public boolean validatePassword(String inputPassword) {
        return this.password.equals(inputPassword);
    }

    public String getPassword() { // For file saving
        return password;
    }

    public double getBalance() {
        return balance;
    }

    // Deposit method is common for all accounts.
    // @param amount Amount to deposit
    public void deposit(double amount) throws BankException {
        if (amount > 0) {
            this.balance += amount;
        } else {
            throw new BankException("Deposit amount must be positive", BankException.INVALID_AMOUNT);
        }
    }

    // Withdraws the specified amount from the account.
    // Ensures the balance does not drop below zero.

    // @param amount Amount to withdraw

    // @throws BankException if funds are not enough
    public void withdraw(double amount) throws BankException {
        if (amount <= 0) {
            throw new BankException("Withdrawal amount must be positive", BankException.INVALID_AMOUNT);
        }

        if (balance >= amount) {
            balance -= amount;
        } else {
            throw new BankException("Insufficient funds.", BankException.INSUFFICIENT_FUNDS);
        }
    }

    // Helper method for file I/O formatting
    public String toFileString() {
        // Format: AccountNumber,Name,Surname,Password,Balance
        return getAccountNumber() + "," + getName() + "," + getSurname() + "," + getPassword() + "," + getBalance();
    }
}
