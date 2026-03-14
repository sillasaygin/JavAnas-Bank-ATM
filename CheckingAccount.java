/*
 Represents a standard Checking Account.
 Contains standard banking operations.
 */
public class CheckingAccount extends BankAccount {

    public CheckingAccount(String accountNumber, String name, String surname, String password, double balance) {
        super(accountNumber, name, surname, password, balance);
    }

    @Override
    public String getAccountType() {
        return "Checking Account";
    }
}
