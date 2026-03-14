/*
 Custom Exception class for handling banking-related errors.
 Uses types to differentiate between specific error scenarios.
*/
public class BankException extends Exception {
    public static final String INSUFFICIENT_FUNDS = "INSUFFICIENT_FUNDS";
    public static final String INVALID_AMOUNT = "INVALID_AMOUNT";
    public static final String ACCOUNT_NOT_FOUND = "ACCOUNT_NOT_FOUND";
    public static final String INVALID_INPUT = "INVALID_INPUT";

    private String type;

    public BankException(String internalMsg, String type) {
        super(internalMsg);
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
