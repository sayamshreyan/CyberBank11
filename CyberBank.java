import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CyberBank {

    static Scanner sc = new Scanner(System.in);
    static Random random = new Random();

    static String accountName = "SKY";
    static String accountNumber = "CB" + (100000 + random.nextInt(900000));
    static int pin = 2468;

    static double balance = 25000.00;

    static ArrayList<String> transactions = new ArrayList<>();

    static DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public static void main(String[] args) {

        showWelcome();

        if (!login()) {
            System.out.println("\n🔒 SYSTEM LOCKED.");
            System.out.println("Too many failed attempts.");
            return;
        }

        addTransaction("ACCOUNT OPENED", 0);

        boolean running = true;

        while (running) {

            showMenu();

            int choice = readInt("Enter choice: ");

            switch (choice) {

                case 1:
                    showAccount();
                    break;

                case 2:
                    deposit();
                    break;

                case 3:
                    withdraw();
                    break;

                case 4:
                    transfer();
                    break;

                case 5:
                    showTransactions();
                    break;

                case 6:
                    showSystemStats();
                    break;

                case 7:
                    System.out.println("\n👋 LOGGING OUT...");
                    System.out.println("Thank you for using CYBERBANK.");
                    running = false;
                    break;

                default:
                    System.out.println("\n❌ Invalid option.");
            }
        }

        sc.close();
    }

    // ==============================
    // WELCOME SCREEN
    // ==============================

    static void showWelcome() {

        System.out.println();
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║          ⚡ CYBERBANK ⚡              ║");
        System.out.println("║      NEXT GENERATION BANKING         ║");
        System.out.println("╚══════════════════════════════════════╝");

        System.out.println("\nInitializing secure system...");

        for (int i = 0; i < 3; i++) {
            System.out.print(".");
            sleep(500);
        }

        System.out.println("\n\nSYSTEM ONLINE ✓");
    }

    // ==============================
    // LOGIN
    // ==============================

    static boolean login() {

        final int MAX_ATTEMPTS = 3;

        for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {

            System.out.print("\n🔐 Enter 4-digit PIN: ");

            int enteredPin = readInt("");

            if (enteredPin == pin) {

                System.out.println("\n✅ ACCESS GRANTED");
                System.out.println("Welcome back, " + accountName + "!");

                return true;

            } else {

                int remaining = MAX_ATTEMPTS - attempt;

                System.out.println("❌ INCORRECT PIN.");

                if (remaining > 0) {
                    System.out.println("Attempts remaining: " + remaining);
                }
            }
        }

        return false;
    }

    // ==============================
    // MENU
    // ==============================

    static void showMenu() {

        System.out.println("\n");
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║             MAIN TERMINAL           ║");
        System.out.println("╠══════════════════════════════════════╣");
        System.out.println("║  1. 👤 Account Information           ║");
        System.out.println("║  2. 💰 Deposit Money                 ║");
        System.out.println("║  3. 💸 Withdraw Money                ║");
        System.out.println("║  4. 🔄 Transfer Money                ║");
        System.out.println("║  5. 📜 Transaction History            ║");
        System.out.println("║  6. 📊 System Statistics              ║");
        System.out.println("║  7. 🚪 Logout                         ║");
        System.out.println("╚══════════════════════════════════════╝");
    }

    // ==============================
    // ACCOUNT
    // ==============================

    static void showAccount() {

        System.out.println("\n========== ACCOUNT ==========");

        System.out.println("Name           : " + accountName);
        System.out.println("Account Number : " + accountNumber);
        System.out.printf("Balance        : ₹%.2f%n", balance);

        System.out.println("==============================");
    }

    // ==============================
    // DEPOSIT
    // ==============================

    static void deposit() {

        System.out.println("\n========== DEPOSIT ==========");

        double amount = readDouble("Enter amount: ₹");

        if (amount <= 0) {

            System.out.println("❌ Amount must be greater than zero.");
            return;
        }

        balance += amount;

        addTransaction("DEPOSIT +" + formatMoney(amount), amount);

        System.out.printf("✅ ₹%.2f deposited successfully.%n", amount);
        System.out.printf("New Balance: ₹%.2f%n", balance);
    }

    // ==============================
    // WITHDRAW
    // ==============================

    static void withdraw() {

        System.out.println("\n========== WITHDRAW ==========");

        double amount = readDouble("Enter amount: ₹");

        if (amount <= 0) {

            System.out.println("❌ Amount must be greater than zero.");
            return;
        }

        if (amount > balance) {

            System.out.println("❌ INSUFFICIENT BALANCE.");
            return;
        }

        balance -= amount;

        addTransaction("WITHDRAW -" + formatMoney(amount), -amount);

        System.out.printf("✅ ₹%.2f withdrawn successfully.%n", amount);
        System.out.printf("Remaining Balance: ₹%.2f%n", balance);
    }

    // ==============================
    // TRANSFER
    // ==============================

    static void transfer() {

        System.out.println("\n========== MONEY TRANSFER ==========");

        System.out.print("Enter receiver name: ");
        String receiver = sc.nextLine();

        if (receiver.isEmpty()) {

            System.out.println("❌ Receiver name cannot be empty.");
            return;
        }

        System.out.print("Enter receiver account number: ");
        String receiverAccount = sc.nextLine();

        if (receiverAccount.isEmpty()) {

            System.out.println("❌ Account number cannot be empty.");
            return;
        }

        double amount = readDouble("Enter amount: ₹");

        if (amount <= 0) {

            System.out.println("❌ Invalid amount.");
            return;
        }

        if (amount > balance) {

            System.out.println("❌ INSUFFICIENT BALANCE.");
            return;
        }

        System.out.println("\nTransfer Summary");
        System.out.println("-----------------------------");
        System.out.println("Receiver : " + receiver);
        System.out.println("Account  : " + receiverAccount);
        System.out.printf("Amount   : ₹%.2f%n", amount);

        System.out.print("\nConfirm transfer? (Y/N): ");

        String confirmation = sc.nextLine();

        if (confirmation.equalsIgnoreCase("Y")) {

            balance -= amount;

            String transactionId =
                    "TX" + (10000000 + random.nextInt(90000000));

            addTransaction(
                    "TRANSFER -" + formatMoney(amount)
                            + " → " + receiver,
                    -amount
            );

            System.out.println("\n⚡ PROCESSING TRANSACTION...");

            sleep(1000);

            System.out.println("✅ TRANSFER SUCCESSFUL");
            System.out.println("Transaction ID: " + transactionId);
            System.out.printf("Remaining Balance: ₹%.2f%n", balance);

        } else {

            System.out.println("❌ Transfer cancelled.");
        }
    }

    // ==============================
    // TRANSACTION HISTORY
    // ==============================

    static void showTransactions() {

        System.out.println("\n========== TRANSACTION HISTORY ==========");

        if (transactions.isEmpty()) {

            System.out.println("No transactions found.");

            return;
        }

        for (int i = 0; i < transactions.size(); i++) {

            System.out.println((i + 1) + ". " + transactions.get(i));
        }

        System.out.println("==========================================");
    }

    // ==============================
    // SYSTEM STATISTICS
    // ==============================

    static void showSystemStats() {

        System.out.println("\n========== SYSTEM STATISTICS ==========");

        System.out.println("Account Status : ACTIVE");
        System.out.println("Security       : PIN PROTECTED");
        System.out.println("Transactions   : " + transactions.size());
        System.out.printf("Current Funds  : ₹%.2f%n", balance);

        if (balance >= 50000) {

            System.out.println("Financial Rank : 💎 PLATINUM");

        } else if (balance >= 25000) {

            System.out.println("Financial Rank : 🥇 GOLD");

        } else if (balance >= 10000) {

            System.out.println("Financial Rank : 🥈 SILVER");

        } else {

            System.out.println("Financial Rank : 🥉 BRONZE");
        }

        System.out.println("=======================================");
    }

    // ==============================
    // TRANSACTION LOGGER
    // ==============================

    static void addTransaction(String description, double amount) {

        String time = LocalDateTime.now().format(formatter);

        String transaction =
                "[" + time + "] "
                        + description
                        + " | ID: "
                        + generateTransactionId();

        transactions.add(transaction);
    }

    // ==============================
    // TRANSACTION ID
    // ==============================

    static String generateTransactionId() {

        return "TX-" + (100000 + random.nextInt(900000));
    }

    // ==============================
    // MONEY FORMAT
    // ==============================

    static String formatMoney(double amount) {

        return String.format("₹%.2f", amount);
    }

    // ==============================
    // INTEGER INPUT
    // ==============================

    static int readInt(String message) {

        while (true) {

            try {

                System.out.print(message);

                return Integer.parseInt(sc.nextLine());

            } catch (NumberFormatException e) {

                System.out.println("❌ Please enter a valid number.");
            }
        }
    }

    // ==============================
    // DOUBLE INPUT
    // ==============================

    static double readDouble(String message) {

        while (true) {

            try {

                System.out.print(message);

                return Double.parseDouble(sc.nextLine());

            } catch (NumberFormatException e) {

                System.out.println("❌ Please enter a valid amount.");
            }
        }
    }

    // ==============================
    // DELAY
    // ==============================

    static void sleep(long milliseconds) {

        try {

            Thread.sleep(milliseconds);

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();
        }
    }
}