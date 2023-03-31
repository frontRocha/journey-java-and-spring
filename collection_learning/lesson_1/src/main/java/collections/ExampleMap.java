package main.java.collections;

import main.Person.Person;

import java.util.HashMap;
import java.util.Map;

public class ExampleMap {
    public static void main(String[] args) {
        Map<Long, Person> map = new HashMap<>();

        var joao = new Person(1L, "Joao");

        map.put(1L, joao);

        Person joao2 = map.get(1L);
    }
}
