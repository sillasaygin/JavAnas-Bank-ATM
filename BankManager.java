
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/*
 MANAGER CLASS
 Handles the collection of accounts and File I/O operations.
 */
public class BankManager {
    // List to store accounts
    private List<BankAccount> accounts;

    public BankManager() {
        accounts = new ArrayList<>();
    }

    public void addAccount(BankAccount account) {
        accounts.add(account);
    }

    public BankAccount findAccount(String accountNumber) {
        for (BankAccount acc : accounts) {
            if (acc.getAccountNumber().equals(accountNumber)) {
                return acc;
            }
        }
        return null; // Not found
    }

    /*
     * Reads account data from the specified file.
     * Expected Format: AccountNumber,Name,Surname,Password,Balance
     */
    /*
     * FILE I/O: Reading from a file (accounts.txt)
     * Format: AccountNumber,Name,Surname,Password,Balance
     */
    public void loadAccounts(String filename) throws IOException {
        accounts.clear();
        File file = new File(filename);
        if (!file.exists())
            return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Delegate parsing to BankAccount
                BankAccount account = BankAccount.restoreFromData(line);
                if (account != null) {
                    accounts.add(account);
                }
            }
        }
    }

    // AUTHENTICATION
    public BankAccount authenticate(String accountNumber, String password) {
        BankAccount account = findAccount(accountNumber);
        if (account != null && account.validatePassword(password)) {
            return account;
        }
        return null;
    }

    // CREATE ACCOUNT
    // Returns true if successful, false if account number exists
    public boolean createAccount(String accountNumber, String name, String surname, String password) {
        if (findAccount(accountNumber) != null) {
            return false;
        }

        // Create new account instance (Checking by default)
        CheckingAccount newAccount = new CheckingAccount(accountNumber, name, surname, password, 0.0);
        accounts.add(newAccount);
        return true;
    }

    public boolean createSavingAccount(CheckingAccount parentAccount, double initialDeposit) throws BankException {
        String savingId = parentAccount.getAccountNumber() + "-S";
        if (findAccount(savingId) != null) {
            return false; // Already exists
        }

        // Validate initial deposit from parent
        parentAccount.withdraw(initialDeposit);

        SavingAccount saving = new SavingAccount(savingId, parentAccount.getName(), parentAccount.getSurname(),
                parentAccount.getPassword(), initialDeposit);
        accounts.add(saving);
        return true;
    }

    // TRANSFER MONIES
    public void transfer(BankAccount fromAccount, String toAccountNumber, double amount)
            throws BankException {
        BankAccount toAccount = findAccount(toAccountNumber);
        if (toAccount == null) {
            throw new BankException("Destination account not found.", BankException.ACCOUNT_NOT_FOUND);
        }

        // Withdraw first (validates funds)
        fromAccount.withdraw(amount);
        // Then deposit
        toAccount.deposit(amount);
    }

    // FILE I/O: Writing to a file
    public void saveAccounts(String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (BankAccount account : accounts) {
                writer.write(account.toFileString());
                writer.newLine();
            }
        }
    }

    public List<BankAccount> getAllAccounts() {
        return accounts;
    }
}
