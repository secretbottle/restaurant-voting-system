package ru.voting.restaurant_voting_system.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "restaurants")
public class Restaurant extends AbstractNamedEntity {

    public Restaurant(){

    }

    public Restaurant(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

}
