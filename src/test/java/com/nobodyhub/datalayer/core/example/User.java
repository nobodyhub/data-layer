package com.nobodyhub.datalayer.core.example;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

/**
 * @author Ryan
 */
@Entity
@Table
@Data
@ToString
public class User {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String firstName;
    @Column
    private String lastName;
    @Column
    private String email;
    @Column
    private String password;
}