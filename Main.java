package budget;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


public class Main {


    private static double balance;
    static ArrayList<Purchase> purchases = new ArrayList<>();
    public static void main(String[] args) {
        printMenu(purchases);
        System.exit(0);
    }

    public static void printMenu(ArrayList<Purchase> purchases) {
        System.out.println("Choose your action:");
        System.out.println("1) Add income");
        System.out.println("2) Add purchase");
        System.out.println("3) Show list of purchases");
        System.out.println("4) Balance");
        System.out.println("5) Save");
        System.out.println("6) Load");
        System.out.println("7) Analyze (Sort)");
        System.out.println("0) Exit");
        String input = "";
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.print("");
            input = scanner.nextLine();
            System.out.println();
        } catch (InputMismatchException e) {
            System.out.println("Error: " + e.getMessage());
        }
        switch (input) {
            case ("1"):
                addIncome(scanner);
                break;

            case ("2"):
                addPurchase(purchases);
                break;

            case ("3"):
                listPurchases(scanner, purchases);
                break;

            case ("4"):
                showBalance(purchases);
                break;

            case("5"):
                save(purchases);
                printMenu(purchases);
            case("6"):
                purchases = load();
                printMenu(purchases);
            case("7"):
                sortMenu(purchases, scanner);

            case ("0"):
                System.out.println("Bye!");
                System.exit(0);
            default:
                System.out.println("Invalid choice");
                System.out.println();
                printMenu(purchases);
        }
    }

    public static void addIncome(Scanner scanner) {
        System.out.println("Enter income:");
        double incomeToAdd = scanner.nextDouble();
        balance += incomeToAdd;
        System.out.println("Income was added!");
        System.out.println();
        printMenu(purchases);
    }

    public static void addPurchase(ArrayList<Purchase> purchases) {
        try (Scanner scanner = new Scanner(System.in)){
            Purchase purchase = createPurchase(scanner);
            if (purchase != null) {
                System.out.println("Enter purchase name:");
                String thisPurchase = scanner.nextLine();
                System.out.println("Enter its price:");
                String priceString = scanner.nextLine();
                priceString.replace("$", "");
                Float priceFloat = Float.parseFloat(priceString);
                priceString = String.format("%.2f", priceFloat);
                StringBuilder sb = new StringBuilder();
                sb.append(thisPurchase + " " + "$" + priceString);
                purchase.setDescription(sb.toString());
                purchases.add(purchase);
                Float price = Float.parseFloat(priceString);
                purchase.setPrice(price);
                if (balance - price > 0) {
                    balance -= price;
                } else {
                    balance = 0;
                }
            } else {
                 printMenu(purchases);
            }
        } catch (InputMismatchException e) {
            System.out.println("Error: " + e.getMessage());
        }
        System.out.println("Purchase was added!");
        System.out.println();
        addPurchase(purchases);
    }

    public static Purchase createPurchase (Scanner scanner) {
        System.out.println("Choose the type of purchase");
        System.out.println("1) Food");
        System.out.println("2) Clothes");
        System.out.println("3) Entertainment");
        System.out.println("4) Other");
        System.out.println("5) Back");
        try {
            String purchaseType = scanner.nextLine();
            System.out.println();
            switch (purchaseType) {
                case "1":
                    return new Food("", "Food", 0.00f);
                case "2":
                    return new Clothes("", "Clothes", 0.00f);
                case "3":
                    return new Entertainment("", "Entertainment", 0.00f);
                case "4":
                default:
                    return new Other("", "Other", 0.00f);
                case "5":
                    printMenu(purchases);
            }
        } catch (InputMismatchException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    public static void listPurchases(Scanner scanner, ArrayList<Purchase> purchases) {
        if (purchases.size() == 0) {
            System.out.println("Purchase list is empty");
            System.out.println();
            printMenu(purchases);
        }

        double price = 0.00;
        double total = 0.00;
        String type = pickType(scanner);
        ArrayList<Purchase> selectedPurchases = new ArrayList<>();
        String purchaseType = "";
        switch (type) {
            case "Food":
                for (Purchase item : purchases) {
                    System.out.println("TYPE:" + item.getType());
                    if (item.getType().equals("Food")) {
                        purchaseType = item.getType();
                        selectedPurchases.add(item);
                    }
                }
                break;
            case "Clothes":
                for (Purchase item : purchases) {
                    if (item.getType().equals("Clothes")) {
                        purchaseType = item.getType();
                        selectedPurchases.add(item);
                    }
                }
                break;
            case "Entertainment":
                for (Purchase item : purchases) {
                    if (item.getType().equals("Entertainment")) {
                        purchaseType = item.getType();
                        selectedPurchases.add(item);
                    }
                }
                break;
            case "Other":
                for (Purchase item : purchases) {
                    if (item.getType().equals("Other")) {
                        purchaseType = item.getType();
                        selectedPurchases.add(item);
                    }
                }
                break;
            case "All":
            default:
                selectedPurchases = purchases;
                purchaseType = "All";
                break;
        }
        if (purchaseType.length() > 1) {
            System.out.println(purchaseType + ":");
        }
        for (Purchase item : selectedPurchases) {
            System.out.println(item.getDescription());
            String[] splits = item.getDescription().split("\\$");
            price = Double.parseDouble(splits[splits.length - 1]);
            total += price;
        }

        System.out.printf("Total sum: $%.2f", total);
        System.out.println();
        System.out.println();

        listPurchases(scanner, purchases);
    }

    public static void showBalance(ArrayList<Purchase> purchases) {
        System.out.printf("Balance: $%.2f",balance);
        System.out.println();
        System.out.println();
        printMenu(purchases);
    }

    public static String pickType(Scanner scanner) {
        System.out.println("Choose the type of purchases");
        System.out.println("1) Food");
        System.out.println("2) Clothes");
        System.out.println("3) Entertainment");
        System.out.println("4) Other");
        System.out.println("5) All");
        System.out.println("6) Back");

        try {
            String choice = scanner.nextLine();
            System.out.println();
            switch(choice) {
                case "1":
                    return "Food";
                case "2":
                    return "Clothes";
                case "3":
                    return "Entertainment";
                case "4":
                    return "Other";
                case "5":
                    return "All";
                default:
                    printMenu(purchases);
            }
        } catch (InputMismatchException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return "";
    }

    public static boolean save(ArrayList<Purchase> purchases) {
        File file = new File("purchases.txt");
        System.out.println("NUMBER OF PURCHASES: " + purchases.size());
        try (FileWriter writer = new FileWriter(file)) {
            for (Purchase purchase : purchases) {
                writer.write(purchase.getDescription() + ":" + purchase.getType() + "\n");

            }
            writer.write("Balance: " + String.valueOf(balance));
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
        System.out.println("Purchases were saved!");
        System.out.println();
        return true;
    }

    public static ArrayList<Purchase> load () {
        File file = new File("purchases.txt");
        ArrayList<Purchase> purchases= new ArrayList<>();

        try(Scanner scanner = new Scanner(file)) {
            while(scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String splits[] = line.split(":");
                if (splits[0].equals("Balance")) {
                    balance = Double.parseDouble(splits[1]);
                } else {
                    String[] getPriceField = line.split("\\$");
                    String findPrice = getPriceField[getPriceField.length - 1];

                    int stopIndex = findPrice.indexOf(':');
                    String priceString = findPrice.substring(0, stopIndex);
                    Float price = Float.parseFloat(priceString);
                    Purchase purchase = new Purchase(splits[0], splits[1], price);
                    purchases.add(purchase);
                }
            }
        } catch (FileNotFoundException e){
            System.out.println("Error: " + e.getMessage());
        }
        System.out.println("Purchases were loaded!");
        System.out.println();

        return purchases;
    }

    public static void sortMenu(ArrayList<Purchase> purchases, Scanner scanner) {
        System.out.println("How do you want to sort?");
        System.out.println("1) Sort all purchases");
        System.out.println("2) Sort by type");
        System.out.println("3) Sort certain type");
        System.out.println("4) Back");

        try {
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    sort(purchases, "all", scanner);
                    break;
                case "2":
                    sort(purchases, "type", scanner);
                    break;
                case "3":
                    sort(purchases, "certain type", scanner);
                case "4":
                    System.out.println();
                    printMenu(purchases);
            }
        } catch (InputMismatchException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void sort(ArrayList<Purchase> purchases, String howToSort, Scanner scanner) {
        ArrayList<Purchase> sortedPurchases;

        switch (howToSort) {
            case "all":
                checkEmptyList(purchases, scanner);
                System.out.println();
                System.out.println("All:");
                sortedPurchases = bubbleSort(purchases);
                for (Purchase purchase : sortedPurchases) {
                    System.out.println(purchase.getDescription());
                }
                System.out.println("Total sum: $" + getTotal(purchases));
                System.out.println();
                sortMenu(purchases, scanner);
                break;
            case "type":
                System.out.println();
                ArrayList<Purchase> food = new ArrayList<>();
                ArrayList<Purchase> entertainment = new ArrayList<>();
                ArrayList<Purchase> clothes = new ArrayList<>();
                ArrayList<Purchase> other = new ArrayList<>();
                for (Purchase purchase : purchases) {
                    //Sort into Arraylists by type
                    switch(purchase.getType()) {
                        case "Food":
                            food.add(purchase);
                            break;
                        case "Entertainment":
                            entertainment.add(purchase);
                            break;
                        case "Clothes":
                            clothes.add(purchase);
                            break;
                        case "Other":
                            other.add(purchase);
                            break;
                    }
                }

                ArrayList<Purchase> typesList = new ArrayList<>();
                ArrayList<Purchase> sortedTypes;
                typesList.add(new Purchase("", "Food", getTotal(food)));
                typesList.add(new Purchase("","Entertainment", getTotal(entertainment)));
                typesList.add(new Purchase("","Clothes", getTotal(clothes)));
                typesList.add(new Purchase("","Other", getTotal(other)));

                sortedTypes = bubbleSort(typesList);

                Float grandTotal = 0.00f;
                for (Purchase purchase : sortedTypes) {
                    System.out.println(purchase.getType() + " - $" + purchase.getPrice());
                    grandTotal += purchase.getPrice();
                }

                System.out.printf("Total sum: $%.2f\n", grandTotal);
                System.out.println();
                sortMenu(purchases, scanner);
                break;
            case "certain type":
                System.out.println();
                System.out.println("Choose the type of purchase by which to sort");
                System.out.println("1) Food");
                System.out.println("2) Clothes");
                System.out.println("3) Entertainment");
                System.out.println("4) Other");

                ArrayList<Purchase> selectedPurchases = new ArrayList<>();
                String choice = scanner.nextLine();

                if (purchases.size() == 0) {
                    System.out.println();
                    System.out.println("Purchase list is empty!");
                    System.out.println();
                    sortMenu(purchases, scanner);
                }

                String typeString = "";
                final Map<String, String> types = Map.of(
                        "1", "Food",
                        "2", "Clothes",
                        "3", "Entertainment",
                        "4", "Other"
                );
                for (var key : types.entrySet()) {
                    if (key.getKey().equals(choice)) {
                        typeString = key.getValue();
                    }
                }

                if (!typeString.equals(null)) {
                    for (Purchase purchase : purchases) {
                        if (purchase.getType().equals(typeString)) {
                            selectedPurchases.add(purchase);
                        }
                    }
                    System.out.println();
                    System.out.println(typeString + ":");
                    ArrayList<Purchase> sorted = bubbleSort(selectedPurchases);
                    for (Purchase purchase : sorted) {
                        System.out.println(purchase.getDescription());
                    }
                    System.out.printf("Total sum: $%.2f\n", getTotal(sorted));
                    System.out.println();
                } else {
                    sort(purchases, howToSort, scanner);
                }
                sortMenu(purchases, scanner);
                break;
            default:
                sort(purchases, howToSort, scanner);
            }
        }


    public static ArrayList<Purchase> bubbleSort(ArrayList<Purchase> purchases) {
        Purchase[] purchaseArray = new Purchase[purchases.size()];
        //turn the Arraylist into an array
            int index = 0;
            for (Purchase purchase : purchases) {
                purchaseArray[index] = new Purchase(purchase.getDescription(), purchase.getType(), purchase.getPrice());
                index++;
        }

        ArrayList<Purchase> sorted = new ArrayList<>();
        for (int i = 0; i < purchaseArray.length - 1; i++) {
            for (int j = 0; j < purchaseArray.length - i - 1; j++) {
                /* if a pair of adjacent elements has the wrong order it swaps them */
                if (purchaseArray[j].getPrice() < purchaseArray[j + 1].getPrice()) {
                    Purchase temp = purchaseArray[j];
                    purchaseArray[j] = purchaseArray[j + 1];
                    purchaseArray[j + 1] = temp;
                }
            }
        }

        for (Purchase purchase : purchaseArray) {
            sorted.add(purchase);
        }
        return sorted;
    }

    public static Float getTotal(ArrayList<Purchase> purchases) {
        Float total = 0.00f;
        for ( Purchase purchase : purchases) {
            total += purchase.getPrice();
        }
        return total;
    }

    public static void checkEmptyList (ArrayList<Purchase> purchases, Scanner scanner) {
        if (purchases.isEmpty()) {
            System.out.println();
            System.out.println("Purchase list is empty!");
            System.out.println();
            sortMenu(purchases, scanner);
        }
        return;
    }
}