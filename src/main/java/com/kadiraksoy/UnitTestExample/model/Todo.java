package com.kadiraksoy.UnitTestExample.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "todos", schema = "workshop")
@NoArgsConstructor
@AllArgsConstructor
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @ManyToMany
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private boolean isComplete;

    @CreationTimestamp
    private Date createdDate;
}
