package main.Person;

import java.util.Objects;

public class Person {
    private Long id;
    private String name;

    public Person(long id, String name) {
        this.setId(id);
        this.setName(name);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(!(o instanceof Person)) return false;

        var person = (Person) o;
        return id.equals(person.getId()) && name.equals(person.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
