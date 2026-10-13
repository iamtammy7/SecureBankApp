public class Customer {

    private final String fullName;
    private final String username;
    private final String passwordHash;   // "salt:hash"
    private final String accountNumber;

    public Customer(String fullName, String username,
                    String passwordHash, String accountNumber) {
        this.fullName = fullName;
        this.username = username;
        this.passwordHash = passwordHash;
        this.accountNumber = accountNumber;
    }

    public String getFullName() { return fullName; }
    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public String getAccountNumber() { return accountNumber; }

    public String toFileLine() {
        return fullName + "|" + username + "|" +
               passwordHash + "|" + accountNumber;
    }

    public static Customer fromFileLine(String line) {
        String[] parts = line.split("\\|", -1);
        if (parts.length < 4) {
            if (parts.length == 3) {
                return new Customer(parts[0], parts[1], parts[2], null);
            }
            return null;
        }
        return new Customer(parts[0], parts[1], parts[2], parts[3]);
    }
}