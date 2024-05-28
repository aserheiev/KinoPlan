package org.example;

public class CreatePaymentMethod {
    public static IPayment createPaymentMethod(PaymentMethods paymentMethod) {
        switch (paymentMethod) {
            case Paypal:
                return new PaymentPaypal();
            case UemitCoin:
                return new PaymentUemitCoin();
            default:
                System.out.println("Payment method not supported!");
                break;
        }
        return null;
    }
}
