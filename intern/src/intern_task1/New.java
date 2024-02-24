package intern_task1;

import java.util.*;

class ATM {
    private Map<String, String> userCredentials;
    private Map<String, Double> accountBalanceMap;
    private TransactionsHistory transactionsHistory;

    public ATM() {
        userCredentials = new HashMap<>();
        userCredentials.put("123456", "1234");
        userCredentials.put("789012", "5678");

        accountBalanceMap = new HashMap<>();
        accountBalanceMap.put("123456", 1000.0);
        accountBalanceMap.put("789012", 1000.0);

        transactionsHistory = new TransactionsHistory();
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter User ID: ");
        String userID = scanner.nextLine();
        System.out.print("Enter PIN: ");
        String pin = scanner.nextLine();

        if (authenticateUser(userID, pin)) {
            Withdraw withdraw = new Withdraw(accountBalanceMap, transactionsHistory);
            Deposit deposit = new Deposit(accountBalanceMap, transactionsHistory);
            Transfer transfer = new Transfer(accountBalanceMap, transactionsHistory);

            displayMenu(scanner, userID, withdraw, deposit, transfer);
        } else {
            System.out.println("Authentication failed. Exiting...");
        }
    }

    private boolean authenticateUser(String userID, String pin) {
        return userCredentials.containsKey(userID) && userCredentials.get(userID).equals(pin);
    }

    private void displayMenu(Scanner scanner, String userID, Withdraw withdraw, Deposit deposit, Transfer transfer) {
        int choice;

        do {
            System.out.println("\nATM Menu:");
            System.out.println("1. Transactions History");
            System.out.println("2. Withdraw");
            System.out.println("3. Deposit");
            System.out.println("4. Transfer");
            System.out.println("5. Quit");
            System.out.println("Current Balance: $" + accountBalanceMap.get(userID));
            System.out.print("Enter your choice: ");

            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    transactionsHistory.displayTransactions();
                    break;
                case 2:
                    withdraw.execute(scanner, userID);
                    break;
                case 3:
                    deposit.execute(scanner, userID);
                    break;
                case 4:
                    transfer.execute(scanner, userID);
                    break;
                case 5:
                    System.out.println("Exiting ATM. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }

        } while (choice != 5);
    }

    public static void main(String[] args) {
        ATM atm = new ATM();
        atm.start();
    }
}

class TransactionsHistory {
    private List<String> transactionHistory;

    public TransactionsHistory() {
        transactionHistory = new ArrayList<>();
    }

    public void addToHistory(String transaction) {
        transactionHistory.add(transaction);
    }

    public void displayTransactions() {
        if (transactionHistory.isEmpty()) {
            System.out.println("Transaction history is empty.");
        } else {
            System.out.println("Transaction History:");
            for (String transaction : transactionHistory) {
                System.out.println(transaction);
            }
        }
    }

    public void displayTransactionDetails(String transaction) {
        System.out.println("Transaction Details:");
        System.out.println(transaction);
    }
}

class Withdraw {
    private Map<String, Double> accountBalanceMap;
    private TransactionsHistory transactionsHistory;

    public Withdraw(Map<String, Double> accountBalanceMap, TransactionsHistory transactionsHistory) {
        this.accountBalanceMap = accountBalanceMap;
        this.transactionsHistory = transactionsHistory;
    }

    public void execute(Scanner scanner, String userID) {
        System.out.println("Enter withdrawal amount: ");
        double withdrawalAmount = scanner.nextDouble();

        if (withdrawalAmount > 0 && withdrawalAmount <= accountBalanceMap.get(userID)) {
            double updatedBalance = accountBalanceMap.get(userID) - withdrawalAmount;
            accountBalanceMap.put(userID, updatedBalance);

            String lastTransaction = "Withdrawal: -$" + withdrawalAmount + ", Remaining balance: $" + updatedBalance;
            transactionsHistory.addToHistory(lastTransaction);
            System.out.println("Withdrawal successful. Remaining balance: $" + updatedBalance);
        } else {
            System.out.println("Invalid withdrawal amount or insufficient funds.");
        }
    }
}

class Deposit {
    private Map<String, Double> accountBalanceMap;
    private TransactionsHistory transactionsHistory;

    public Deposit(Map<String, Double> accountBalanceMap, TransactionsHistory transactionsHistory) {
        this.accountBalanceMap = accountBalanceMap;
        this.transactionsHistory = transactionsHistory;
    }

    public void execute(Scanner scanner, String userID) {
        System.out.println("Enter deposit amount: ");
        double depositAmount = scanner.nextDouble();

        if (depositAmount > 0) {
            double updatedBalance = accountBalanceMap.get(userID) + depositAmount;
            accountBalanceMap.put(userID, updatedBalance);

            String lastTransaction = "Deposit: +$" + depositAmount + ", Updated balance: $" + updatedBalance;
            transactionsHistory.addToHistory(lastTransaction);
            System.out.println("Deposit successful. Updated balance: $" + updatedBalance);
        } else {
            System.out.println("Invalid deposit amount.");
        }
    }
}

class Transfer {
    private Map<String, Double> accountBalanceMap;
    private TransactionsHistory transactionsHistory;

    public Transfer(Map<String, Double> accountBalanceMap, TransactionsHistory transactionsHistory) {
        this.accountBalanceMap = accountBalanceMap;
        this.transactionsHistory = transactionsHistory;
    }

    public void execute(Scanner scanner, String senderID) {
        System.out.println("Enter recipient's User ID: ");
        String recipientID = scanner.next();
        System.out.println("Enter transfer amount: ");
        double transferAmount = scanner.nextDouble();

        if (accountBalanceMap.containsKey(recipientID) && transferAmount > 0 && transferAmount <= accountBalanceMap.get(senderID)) {
            // Update sender's balance
            double senderBalance = accountBalanceMap.get(senderID);
            senderBalance -= transferAmount;
            accountBalanceMap.put(senderID, senderBalance);

            // Update recipient's balance
            double recipientBalance = accountBalanceMap.get(recipientID);
            recipientBalance += transferAmount;
            accountBalanceMap.put(recipientID, recipientBalance);

            double updatedBalance = senderBalance;

            String lastTransaction = "Transfer: -$" + transferAmount + " to " + recipientID + ", Remaining balance: $" + updatedBalance;
            transactionsHistory.addToHistory(lastTransaction);
            System.out.println("Transfer successful. Remaining balance: $" + updatedBalance);
        } else {
            System.out.println("Invalid recipient ID, transfer amount, or insufficient funds.");
        }
    }
}
