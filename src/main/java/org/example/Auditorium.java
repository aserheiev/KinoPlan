package org.example;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

/** Eine Klasse, die es uns ermöglicht, den Saal zu besichtigen und Tickets zu reservieren und zu kaufen.
 */

public class Auditorium {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[41m";
    public static final String ANSI_GREEN = "\u001B[42m";
    public static final String ANSI_YELLOW = "\u001B[43m";
    public static final String ANSI_BLUE = "\u001B[34m";


    private final char[][] layout;
    private String saalName;
    private int basketPrice;


    /** Der Constructor. Er erhält die Anzahl der Reihen und Sitze und lässt die Sitze zufällig (mit einer Wahrscheinlichkeit von 30%) belegen
     *
     * @param rowCount Anzahl von Reihen
     * @param seatsPerRow Sitze pro Reihe
     * @param saalName Name vom Saal
     */
    public Auditorium(int rowCount, int seatsPerRow, String saalName) {
        layout = buildLayout(rowCount, seatsPerRow);
        this.saalName = saalName;
        basketPrice = 0;
        randomFill();
    }

    /** füllt die Array mit leeren Sitzen
     *
     * @param rowCount Anzahl von Reihen
     * @param seatsPerRow Sitze pro Reihe
     * @return eine Array von chars, die den Saal representiert
     */
    private char[][] buildLayout(int rowCount, int seatsPerRow) {
        char[][] layout = new char[rowCount][seatsPerRow];

        for (int i = 0; i < layout.length; i++) {
            Arrays.fill(layout[i], 'E');
        }

        return layout;
    }

    /** Eine Funktion, die den Saalplan in der Konsole ausgibt
     */
    private void printGrid() {

        int seatCounter = 1;
        System.out.println("\t\t");
        for (int i = 0; i < layout[0].length; i++) {

            if (i == 0) {
                System.out.print("\t\t" + seatCounter);
            } else {
                System.out.print("\t\t" + seatCounter);
            }
            seatCounter++;

        }

        System.out.println();

        // leer = gruen, reserviert = grün
        for (int i = 0; i < layout.length; i++) {
            System.out.print((i + 1) + "\t|");
            for (int j = 0; j < layout[i].length; j++) {
                if (layout[i][j] == 'E') {
                    System.out.print(ANSI_GREEN + "\t" + layout[i][j] + "\t" + ANSI_RESET + "|");
                } else if (layout[i][j] == 'R') {
                    System.out.print(ANSI_YELLOW + "\t" + layout[i][j] + "\t" + ANSI_RESET + "|");
                } else {
                    System.out.print(ANSI_RED + "\t" + layout[i][j] + "\t" + ANSI_RESET + "|");
                }
            }
            System.out.println();
        }
        System.out.println("There are still " + countEmptySeats(layout) + " empty seats in the theater!");
    }

    /** Die Funktion, die das Hauptmenu ausgibt und die input vom User nimmt.
     *
     */
    public void mainMenu() {
        Scanner input = new Scanner(System.in);
        System.out.println(ANSI_BLUE + "Willkommen im " + saalName + "!" + ANSI_RESET);

        basketPrice = calculatePrice();
        System.out.println("Price of tickets currently in the basket: " + basketPrice);

        System.out.println("1. Check available seats\n2. Select tickets\n3. Unselect tickets\n4. Checkout and pay\n5. Exit");

        int forAnswer = input.nextInt();

        switch (forAnswer) {
            case 1:
                printGrid();
                break;
            case 2:
                buyTicket();
                break;
            case 3:
                unbuyTicket();
                break;
            case 4:
                checkout();
                break;
            case 5:
                System.exit(0);
            default:
                break;
        }
    }

    private int countEmptySeats(char[][] layout) {
        int counter = 0;
        for (int i = 0; i < layout.length; i++) {
            for (int j = 0; j < layout[i].length; j++) {
                if (layout[i][j] == 'E') {
                    counter++;
                }
            }
        }
        return counter;
    }

    /** Die Funktion, in der der User die Zahlungmethode wählen kann, und die
     * dann den dementsprechenden Objekt von Typ IPayment erstellt.
     */
    private void checkout() {
        PaymentMethods paymentMethods;
        IPayment paymentMethod;

        Scanner input = new Scanner(System.in);
        System.out.println("Select your preferred payment method: ");
        System.out.println("1. Paypal\n2. Uemitcoin\n3. Back");

        int auswahl = input.nextInt();

        switch (auswahl) {
            case 1:
                paymentMethod = CreatePaymentMethod.createPaymentMethod(PaymentMethods.Paypal);
                paymentMethod.Buy(basketPrice);
                takeSeats();
                break;
            case 2:
                paymentMethod = CreatePaymentMethod.createPaymentMethod(PaymentMethods.UemitCoin);
                paymentMethod.Buy(basketPrice);
                takeSeats();
                break;
            default:
                break;
        }
    }

    /** Macht die Sitze besetzt nachdem sie bezahlt sind
     *
     */
    private void takeSeats() {
        for (int i = 0; i < layout.length; i++) {
            for (int j = 0; j < layout[i].length; j++) {
                if (layout[i][j] == 'R') {
                    layout[i][j] = 'B';
                }
            }
        }
    }

    /** Eine einfache Funktion, die entweder eine Einzel- oder
     * eine Mehrfachticket-Reservierungsfunktion aufruft
     */
    private void buyTicket() {
        Scanner input = new Scanner(System.in);

        System.out.println("Current ticket price is 15 Euro for each ticket.");
        System.out.println("Do you wish to reserve singular tickets or several in the same row?");

        System.out.println("1. Singular\n2. Several\n3. Back");

        int auswahl = input.nextInt();

        switch (auswahl) {
            case 1:
                buySingleTicket();
                break;
            case 2:
                buyMultipleTickets();
                break;
            case 3:
                break;
        }
    }

    /** Kalkuliert die Preis für alle Tickets im Wärenkorb
     *
     * @return die gesamte Preis für alle Tickets
     */
    private int calculatePrice() {
        int reservedCount = 0;

        for (int i = 0; i < layout.length; i++) {
            for (int j = 0; j < layout[i].length; j++) {
                if (layout[i][j] == 'R') {
                    reservedCount++;
                }
            }
        }

        return reservedCount * 15;
    }

    /** Ruft Funktionen auf, die eine oder mehrere Ticketreservierungen stornieren
     *
     */
    private void unbuyTicket() {
        Scanner input = new Scanner(System.in);

        System.out.println("Do you wish to remove reservations for singular tickets or several in the same row?");

        System.out.println("1. Singular\n2. Several\n3. Back");

        int auswahl = input.nextInt();

        switch (auswahl) {
            case 1:
                unBuySingleTicket();
                break;
            case 2:
                unBuyMultipleTickets();
                break;
            case 3:
                break;
        }
    }

    /** Storniert die Reservation von einem Ticket
     *
     */
    private void unBuySingleTicket() {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter row: ");
        int row = input.nextInt();
        System.out.println("Enter seat: ");
        int seat = input.nextInt();

        if (layout[row - 1][seat - 1] == 'R') {
            System.out.println(ANSI_GREEN + "Understood, dropping reservation." + ANSI_RESET);
            layout[row - 1][seat - 1] = 'E';
        } else {
            // wenn der Sitz sowieso nicht reserviert war
            System.out.println(ANSI_RED + "It wasn't reserved anyway." + ANSI_RESET);
        }
    }

    /** Einen einzelnen Ticket kaufen
     *
     */
    private void buySingleTicket() {
        Scanner input = new Scanner(System.in);
        int row = 0;
        int seat = 0;

        try {
            System.out.println("Reihe eingeben: ");
            row = input.nextInt();
            System.out.println("Sitzplatz eingeben: ");
            seat = input.nextInt();
            if (layout[row - 1][seat - 1] == 'E') {
                System.out.println(ANSI_GREEN + "Cool, der Sitzplatz ist frei! Jetzt reserviert." + ANSI_RESET);
                layout[row - 1][seat - 1] = 'R';
            } else {
                System.out.println(ANSI_RED + "Schon besetzt" + ANSI_RESET);
            }
        } catch (Exception e) {
            System.out.println("Bitte wählen Sie einen vorhandenen Sitzplatz.");
        }

    }

    /** Mehrere Tickets kaufen
     *
     */
    private void buyMultipleTickets() {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter row: ");
        int row = input.nextInt() - 1;
        int startSeat = 0;
        int endSeat = 0;
        boolean notTaken = true;

        // holy JANK. This has to be done because the nextInt() above leaves an empty line in the buffer
        input.nextLine();

        System.out.println("Enter range in a format like \"1 - 10)\": ");
        String seatRange = input.nextLine();

        try {
            startSeat = Integer.parseInt(seatRange.split(" - ")[0]);
            endSeat = Integer.parseInt(seatRange.split(" - ")[1]);
        } catch (Exception e) {
            System.out.println("Something went wrong LOL");
        }

        // überprüfen, ob einer der Sitzplätzen besetzt ist
        try {
            for (int i = startSeat - 1; i < endSeat; i++) {
                if (layout[row][i] == 'B') {
                    notTaken = false;
                    break;
                }
            }

            if (notTaken) {
                for (int i = startSeat - 1; i < endSeat; i++) {
                    layout[row][i] = 'R';
                }
                System.out.println("Reservations made!");
            } else {
                System.out.println(ANSI_RED + "Reservation of these seats wasn't possible. There is an occupied seat in the way" + ANSI_RESET);
            }
        } catch (Exception e) {
            // LMAO
            System.out.println("Something went wrong LOL");
        }
    }

    /**
     * Stornierung von Mehrfachreservierungen
     */
    private void unBuyMultipleTickets() {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter row: ");
        int row = input.nextInt() - 1;
        int startSeat = 0;
        int endSeat = 0;

        // holy JANK #2
        input.nextLine();

        System.out.println("Enter range in a format like \"1 - 10)\": ");
        String seatRange = input.nextLine();


        try {
            startSeat = Integer.parseInt(seatRange.split(" - ")[0]);
            endSeat = Integer.parseInt(seatRange.split(" - ")[1]);
        } catch (Exception e) {
            System.out.println("Illegal range");
        }

        System.out.println(startSeat);
        System.out.println(endSeat);

        for (int i = startSeat - 1; i < endSeat; i++) {
            if (layout[row][i] == 'R') {
                layout[row][i] = 'E';
            }
        }

        System.out.println("Reservations cleared!");
    }

    /** Zufällige (30% Wahrscheinlichkeit) Besetzung der Plätze.
     *
     */
    private void randomFill() {
        Random generator = new Random();

        for (int i = 0; i < layout.length; i++) {
            for (int j = 0; j < layout[i].length; j++) {
                int randy = generator.nextInt(1, 101);
                if (randy <= 30) {
                    layout[i][j] = 'B';
                }
            }
        }
    }
}