package com.octal.fsm.entities;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "role")
public class Role extends AbstractPersistable{

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "description",length = 512)
    private String description;


}
