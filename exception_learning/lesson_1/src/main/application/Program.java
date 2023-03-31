package main.application;

import businessException.BusinessException;
import main.entities.Account;

import java.util.Locale;
import java.util.Scanner;

public class Program {
    public static void main(String[] agr) {
        Locale.setDefault(Locale.US);

        var sc = new Scanner(System.in);

        System.out.println("Informe os dados da conta");
        System.out.println("Numero: ");
        int number = sc.nextInt();
        System.out.println("Titular: ");
        sc.nextLine();
        String holder = sc.nextLine();
        System.out.println("Saldo inicial: ");
        Double balance = sc.nextDouble();
        System.out.println("Limite de saque: ");
        Double withdrawLimit = sc.nextDouble();

        var acc = new Account(number, holder, balance, withdrawLimit);

        System.out.println();
        System.out.println("Informe uma quantida para sacar");

        Double amount = sc.nextDouble();


        try {
            acc.withdraw(amount);
            System.out.printf("Novo saldo: %.2f%n" , acc.getBalance());
        } catch(BusinessException err) {
            System.out.println(err.getMessage());
        }

        sc.close();
    }
}
