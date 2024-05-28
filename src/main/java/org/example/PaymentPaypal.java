package org.example;

import java.util.Scanner;

public class PaymentPaypal implements IPayment {

    public void Buy(int amount) {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter your email: ");

        input.nextLine();

        System.out.println("Enter your password");

        input.nextLine();

        System.out.println("Paypal payment complete! Paid $" + amount);
    }
}
