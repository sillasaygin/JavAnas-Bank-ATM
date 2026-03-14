/*
 Represents a Saving Account.
 Features: Daily Interest, separate balance, transfers to/from Checking.
 ID Suffix: "-S"
 */
public class SavingAccount extends BankAccount {

    public SavingAccount(String accountNumber, String name, String surname, String password, double balance) {
        super(accountNumber, name, surname, password, balance);
    }

    @Override
    public String getAccountType() {
        return "Saving Account";
    }

    // Calculates the projected balance after one day with 1% interest.
    // Formula: Balance * 1.01
    public double calculateDailyInterestProjection() {
        return getBalance() * 1.01;
    }
}
