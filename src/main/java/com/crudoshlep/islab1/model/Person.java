package com.crudoshlep.islab1.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * Класс для представления автора лабораторной работы
 */
@Entity
@Table(name = "persons")
public class Person {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Имя не может быть пустым")
    @Column(name = "name", nullable = false)
    private String name;
    
    @NotNull(message = "Цвет глаз не может быть null")
    @Enumerated(EnumType.STRING)
    @Column(name = "eye_color", nullable = false)
    private Color eyeColor;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "hair_color")
    private Color hairColor;
    
    @NotNull(message = "Местоположение не может быть null")
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;
    
    @NotNull(message = "Дата рождения не может быть null")
    @Column(name = "birthday", nullable = false)
    private LocalDateTime birthday;
    
    @NotNull(message = "Национальность не может быть null")
    @Enumerated(EnumType.STRING)
    @Column(name = "nationality", nullable = false)
    private Country nationality;
    
    // Конструкторы
    public Person() {}
    
    public Person(String name, Color eyeColor, Color hairColor, Location location, 
                  LocalDateTime birthday, Country nationality) {
        this.name = name;
        this.eyeColor = eyeColor;
        this.hairColor = hairColor;
        this.location = location;
        this.birthday = birthday;
        this.nationality = nationality;
    }
    
    // Геттеры и сеттеры
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
    
    public Color getEyeColor() {
        return eyeColor;
    }
    
    public void setEyeColor(Color eyeColor) {
        this.eyeColor = eyeColor;
    }
    
    public Color getHairColor() {
        return hairColor;
    }
    
    public void setHairColor(Color hairColor) {
        this.hairColor = hairColor;
    }
    
    public Location getLocation() {
        return location;
    }
    
    public void setLocation(Location location) {
        this.location = location;
    }
    
    public LocalDateTime getBirthday() {
        return birthday;
    }
    
    public void setBirthday(LocalDateTime birthday) {
        this.birthday = birthday;
    }
    
    public Country getNationality() {
        return nationality;
    }
    
    public void setNationality(Country nationality) {
        this.nationality = nationality;
    }
    
    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", eyeColor=" + eyeColor +
                ", hairColor=" + hairColor +
                ", location=" + location +
                ", birthday=" + birthday +
                ", nationality=" + nationality +
                '}';
    }
}
