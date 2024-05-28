package org.example;

public class Main {
    public static void main(String[] args) {
        Auditorium saalEins = new Auditorium(13, 16, "Saal Eins");

        while (true) {
            saalEins.mainMenu();
        }
    }
}