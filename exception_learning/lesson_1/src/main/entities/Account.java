package main.entities;

import businessException.BusinessException;

public class Account {
    private Integer number;
    private String holder;
    private Double balance;
    private Double withDrawLimit;
    private Double amount;

    public Account(Integer number, String holder, Double balance, Double withDrawLimit) {
        this.setNumber(number);
        this.setHolder(holder);
        this.setBalance(balance);
        this.setWithDrawLimit(withDrawLimit);
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getHolder() {
        return holder;
    }

    public void setHolder(String holder) {
        this.holder = holder;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Double getWithDrawLimit() {
        return withDrawLimit;
    }

    public void setWithDrawLimit(Double withDrawLimit) {
        this.withDrawLimit = withDrawLimit;
    }

    public void deposit(Double amount) {
        this.setBalance(this.getBalance() + amount);
    }

    public void withdraw(Double amount) throws BusinessException {
        withdrawValidation(amount);
        this.setBalance(this.getBalance() - amount);
    }

    public void withdrawValidation(Double amount) throws BusinessException {
        if(amount > this.getWithDrawLimit()) {
            throw new Error("Limite de saque atingido");
        }

        if(amount > this.getBalance()) {
            throw new Error("Impossivel sacar um valor maior que o saldo atual");
        }
    }
}
