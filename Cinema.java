package Cinema;

import java.io.*;
import java.util.*;

/**
 * Created by TaioYui on 25.11.2015.
 */
public class Cinema {
    //global variables
    List<Films> filmsList = new ArrayList<>();
    List<Order> ordersList = new ArrayList<>();
    List<Menu> menusList = new ArrayList<>();
    String separator = ";";//separator for csv

    public static void main(String[] args) throws IOException {
        System.out.format("+----------------------------------+%n");
        System.out.format("|---< Welcome to Kawaii Cinema >---|%n");
        System.out.format("+----------------------------------+%n");

        Cinema cinema = new Cinema();//create object cinema for using non static methods
        cinema.readFilmList();
        cinema.readOrderList();
        cinema.createMainMenu();
        cinema.mainMenu();
        cinema.writeOrderList();
    }

    private void createMainMenu() { //create main menu and add each item to menu list
        Menu menu1 = new Menu(1, "Create a new order");
        Menu menu2 = new Menu(2, "List the films");
        Menu menu3 = new Menu(3, "View previous orders");
        Menu menu4 = new Menu(4, "Quit the program");
        menusList.add(menu1);
        menusList.add(menu2);
        menusList.add(menu3);
        menusList.add(menu4);
    }

    private void readFilmList() throws IOException {//read films list to memory
        BufferedReader filmsReader = new BufferedReader(new FileReader("films.txt"));
        //BufferedReader filmsReader = new BufferedReader(new FileReader(workDir + File.separator + "films.txt"));
        String line;
        while ((line = filmsReader.readLine()) != null) {//read until the empty string
            String fields[] = line.split(separator);
            Films readStr = new Films(Integer.valueOf(fields[0]), fields[1], fields[2], Double.valueOf(fields[3]));
            filmsList.add(readStr);//add new string to films list
        }
    }

    private void readOrderList() throws IOException {//read orders list to memory
        BufferedReader ordersReader = new BufferedReader(new FileReader("orders.txt"));
        String line;
        while ((line = ordersReader.readLine()) != null) {
            String fields[] = line.split(separator);
            Order readStr = new Order(filmsList.get(Integer.parseInt(fields[0]) - 1), Integer.valueOf(fields[1]),
                    Double.valueOf(fields[2]));
            ordersList.add(readStr);//add new string to orders list
        }
    }

    private void printMainMenu() {
        System.out.println("Main Menu");
        for (Menu menu : menusList) {
            System.out.println("[" + menu.number + "] " + menu.name);
        }
    }

    private void writeOrderList() throws IOException {//write orders from the list to file
        File file = new File("orders.txt");
        FileWriter orderWriter = new FileWriter(file);
        String s;
        for (Order list : ordersList) {
            s = String.valueOf(list.chosenFilm.number) + separator + list.amount + separator +
                    String.format(Locale.US, "%.2f", list.totalPrice); //decimal format for double with "." separator
            orderWriter.write(s);
            orderWriter.append("\n");//write line by line
        }
        orderWriter.flush();
        orderWriter.close();
    }

    private void mainMenu() throws IOException {
        int pick = 0;
        Scanner sc = new Scanner(System.in);
        while (pick != 4) { //loop works until input 4
            printMainMenu();
            pick = checkInputDigits();
            if (pick == 2) {
                printFilmsList();
                System.out.println("press Enter for back to Menu");
                sc.nextLine();
            } else if (pick == 3) {
                printOrdersList();
            } else if (pick == 1) {
                createNewOrder();
            } else if (pick == 4) {
                System.out.println("Bye Bye");
            } else {
                System.out.println("Input digits 1-4");
            }
        }
    }

    private void createNewOrder() throws IOException {
        Set<String> allowedSymbols = new HashSet<>(Arrays.asList("y", "Y", "n", "N"));//set of allowed symbols for input
        printFilmsList();
        Scanner sc = new Scanner(System.in);//read input from the console
        int filmNumber = -1;
        int amount;
        int age;
        double totalPrice = 0;
        String name;

        System.out.println("Choose film");
        while (!(filmNumber >= 1 && filmNumber <= filmsList.size())) {
            System.out.println("Input number from 1 to " + filmsList.size());
            filmNumber = checkInputDigits();
        }
        Films choosenFilm = filmsList.get(filmNumber - 1);//field index starts with 0
        System.out.println("Input amount");

        amount = checkInputDigits();
        for (int i = 0; i < amount; i++) {//loop from 0 to tickets amount
            System.out.println("Input your age for person #" + (i + 1));
            age = checkInputDigits();
            if ((age > 13) && (age < 65)) {
                totalPrice = totalPrice + choosenFilm.price;
            } else if (age <= 12) {
                totalPrice = totalPrice + choosenFilm.price * 0.8;//20% out of regular price
            } else {
                totalPrice = totalPrice + choosenFilm.price * 0.6;//40% out of regular price
            }

        }
        Order newOrder = new Order(choosenFilm, amount, totalPrice);//use string format for indent
        System.out.println(String.format("%-40s", newOrder.chosenFilm.title) + String.format("%-10s", newOrder.chosenFilm.showtime
                + "pm") + String.format("%-10s", newOrder.amount) + String.format("%.2f", newOrder.totalPrice) + " EUR");
        System.out.println("Confirm y/n");
        while (!allowedSymbols.contains(name = sc.nextLine())) {//loop work until one symbol from set will be read
            System.out.println("Only y or n allowed!");
        }
        if (name.equals("y") || name.equals("Y")) {
            ordersList.add(newOrder);
        }

    }

    private void printOrdersList() throws IOException {//print previous orders list to console
        Scanner scn = new Scanner(System.in);
        if (ordersList.size() == 0) {//if orders lis file empty
            System.out.println("Your don`t have orders");
        } else {
            for (Order list : ordersList) {
                System.out.println(String.format("%-40s", list.chosenFilm.title) + String.format("%-10s", list.chosenFilm.showtime + "pm")
                        + String.format("%-10s", list.amount) + String.format("%.2f", list.totalPrice) + " EUR");
            }
        }

        System.out.println("press Enter for back to Menu");
        scn.nextLine();//line for input enter (pause before read enter)

    }

    private void printFilmsList() {
        for (Films list : filmsList) {
            System.out.println("[" + String.valueOf(list.number) + "] " + String.format("%-40s", list.title) + " "
                    + String.format("%-10s", list.showtime + "pm")
                    + String.format("%.2f", list.price) + " EUR");

        }
    }

    private int checkInputDigits() {//check input symbols are digits
        Scanner sc = new Scanner(System.in);
        Integer pick = null;//create empty object Integer type
        while (pick == null) {//do while object is empty
            try {
                String str = sc.nextLine();//read string from console
                pick = Integer.valueOf(str);//convert string to int
            } catch (Exception e) {
                System.out.println("Only digits allowed!");
            }
        }

        return pick;
    }
}