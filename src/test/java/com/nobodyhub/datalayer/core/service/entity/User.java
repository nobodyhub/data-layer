package com.nobodyhub.datalayer.core.service.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

/**
 * @author Ryan
 */
@Data
@ToString
@Entity
@Table(name = "User")
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