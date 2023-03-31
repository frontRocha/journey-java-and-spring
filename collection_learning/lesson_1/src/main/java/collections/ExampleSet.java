package main.java.collections;

import main.Person.Person;

import java.util.HashSet;
import java.util.Set;

public class ExampleSet {
    public static void main(String[] args) {
        Set<Person> peoples = new HashSet<>();

        var joao = new Person(1l, "joao");
        peoples.add(joao);

        for(Person person: peoples) {
            System.out.println(person.getName());
        }

        System.out.println("----------");
        boolean add = peoples.add(new Person(1L, "joao"));

        if(add) {
            System.out.println("adicionou");
        }

        if(!add) {
            System.out.println("Nao adicionou");
        }

        for(Person person: peoples) {
            System.out.println(person.getName());
        }
    }
}
