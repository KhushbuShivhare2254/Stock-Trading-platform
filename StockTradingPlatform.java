import java.util.*;

// Stock class
class Stock {
    private String symbol;
    private String name;
    private double price;

    public Stock(String symbol, String name, double price) {
        this.symbol = symbol;
        this.name = name;
        this.price = price;
    }

    public String getSymbol() { return symbol; }
    public String getName() { return name; }
    public double getPrice() { return price; }

    public void updatePrice(double newPrice) {
        this.price = Math.max(newPrice, 1.0); // Ensure price doesn't go below 1
    }
}

// Portfolio class
class Portfolio {
    private Map<String, Integer> holdings = new HashMap<>();

    public void buyStock(String symbol, int quantity) {
        holdings.put(symbol, holdings.getOrDefault(symbol, 0) + quantity);
    }

    public boolean sellStock(String symbol, int quantity) {
        if (holdings.containsKey(symbol) && holdings.get(symbol) >= quantity) {
            holdings.put(symbol, holdings.get(symbol) - quantity);
            return true;
        }
        return false;
    }

    public Map<String, Integer> getHoldings() {
        return holdings;
    }
}

// User class
class User {
    private String username;
    private double balance;
    private Portfolio portfolio;

    public User(String username, double balance) {
        this.username = username;
        this.balance = balance;
        this.portfolio = new Portfolio();
    }

    public String getUsername() { return username; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
    public Portfolio getPortfolio() { return portfolio; }
}

// Market Simulator class
class MarketSimulator {
    private Map<String, Stock> stocks = new HashMap<>();

    public MarketSimulator() {
        stocks.put("AAPL", new Stock("AAPL", "Apple", 150.0));
        stocks.put("GOOGL", new Stock("GOOGL", "Google", 2800.0));
        stocks.put("TSLA", new Stock("TSLA", "Tesla", 700.0));
        stocks.put("AMZN", new Stock("AMZN", "Amazon", 3300.0));
    }

    public void simulatePriceChange() {
        Random rand = new Random();
        for (Stock stock : stocks.values()) {
            double change = (rand.nextDouble() - 0.5) * 20; // Random change between -10 and +10
            stock.updatePrice(stock.getPrice() + change);
        }
    }

    public Collection<Stock> getAllStocks() {
        return stocks.values();
    }

    public Stock getStock(String symbol) {
        return stocks.get(symbol);
    }
}

// Main App
public class StockTradingPlatform {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MarketSimulator market = new MarketSimulator();
        User user = new User("khushbu", 10000.0);

        while (true) {
            System.out.println("\n=== Stock Trading Platform ===");
            System.out.println("1. View Market");
            System.out.println("2. Buy Stock");
            System.out.println("3. Sell Stock");
            System.out.println("4. View Portfolio");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    market.simulatePriceChange();
                    System.out.println("\n--- Market Data ---");
                    for (Stock stock : market.getAllStocks()) {
                        System.out.printf("%s (%s): $%.2f\n", stock.getName(), stock.getSymbol(), stock.getPrice());
                    }
                    break;

                case 2:
                    System.out.print("Enter stock symbol: ");
                    String buySymbol = scanner.next().toUpperCase();
                    System.out.print("Enter quantity to buy: ");
                    int buyQty = scanner.nextInt();
                    Stock buyStock = market.getStock(buySymbol);
                    if (buyStock != null) {
                        double totalCost = buyStock.getPrice() * buyQty;
                        if (user.getBalance() >= totalCost) {
                            user.getPortfolio().buyStock(buySymbol, buyQty);
                            user.setBalance(user.getBalance() - totalCost);
                            System.out.println("Stock bought successfully!");
                        } else {
                            System.out.println("Insufficient balance!");
                        }
                    } else {
                        System.out.println("Stock not found.");
                    }
                    break;

                case 3:
                    System.out.print("Enter stock symbol: ");
                    String sellSymbol = scanner.next().toUpperCase();
                    System.out.print("Enter quantity to sell: ");
                    int sellQty = scanner.nextInt();
                    Stock sellStock = market.getStock(sellSymbol);
                    if (sellStock != null) {
                        boolean sold = user.getPortfolio().sellStock(sellSymbol, sellQty);
                        if (sold) {
                            double earnings = sellStock.getPrice() * sellQty;
                            user.setBalance(user.getBalance() + earnings);
                            System.out.println("Stock sold successfully!");
                        } else {
                            System.out.println("You don't own enough of that stock.");
                        }
                    } else {
                        System.out.println("Stock not found.");
                    }
                    break;

                case 4:
                    System.out.println("\n--- Your Portfolio ---");
                    System.out.printf("Balance: $%.2f\n", user.getBalance());
                    for (Map.Entry<String, Integer> entry : user.getPortfolio().getHoldings().entrySet()) {
                        Stock s = market.getStock(entry.getKey());
                        System.out.printf("%s - %d shares ($%.2f each)\n", s.getSymbol(), entry.getValue(), s.getPrice());
                    }
                    break;

                case 5:
                    System.out.println("Exiting... Goodbye!");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid option!");
            }
        }
    }
}

