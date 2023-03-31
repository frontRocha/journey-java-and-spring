package main.java.collections;

import main.Person.Person;

import java.util.ArrayList;
import java.util.List;

public class ExampleList {
    public static void main(String[] args) {
        List<Person> people = new ArrayList<>();

        people.add(new Person(1L, "Joao"));
        var a = people.get(0);

        people.add(a);

        for(Person person: people) {
            System.out.println(person.getName());
        }

        System.out.println("------------");

        for(Person person: people) {
            System.out.println(person.getName());
        }
    }
}
