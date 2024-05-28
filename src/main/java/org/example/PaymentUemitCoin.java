package org.example;

import java.util.Scanner;

public class PaymentUemitCoin implements IPayment {
    public void Buy(int amount) {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter your wallet number: ");

        input.nextLine();

        System.out.println("Enter your password");

        input.nextLine();

        System.out.println("UemitCoin payment complete! Paid $" + amount);
    }
}
