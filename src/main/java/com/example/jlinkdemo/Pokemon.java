package com.example.jlinkdemo;

import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
public class Pokemon {

    @Id
    @Getter
    @Setter
    private int id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private int weight;

    @Getter
    @Setter
    private int height;
}
