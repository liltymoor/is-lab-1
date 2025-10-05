package com.crudoshlep.islab1.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

/**
 * Основной класс лабораторной работы
 */
@Entity
@Table(name = "lab_works")
public class LabWork {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Positive(message = "ID должен быть больше 0")
    private Integer id;
    
    @NotBlank(message = "Название не может быть пустым")
    @Column(name = "name", nullable = false)
    private String name;
    
    @NotNull(message = "Координаты не могут быть null")
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "coordinates_id", nullable = false)
    private Coordinates coordinates;
    
    @NotNull(message = "Дата создания не может быть null")
    @Column(name = "creation_date", nullable = false)
    private LocalDate creationDate;
    
    @Column(name = "description")
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty")
    private Difficulty difficulty;
    
    @NotNull(message = "Дисциплина не может быть null")
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "discipline_id", nullable = false)
    private Discipline discipline;
    
    @Positive(message = "Минимальное количество баллов должно быть больше 0")
    @Column(name = "minimal_point", nullable = false)
    private int minimalPoint;
    
    @NotNull(message = "Максимальное количество баллов за личные качества не может быть null")
    @Positive(message = "Максимальное количество баллов за личные качества должно быть больше 0")
    @Column(name = "personal_qualities_maximum", nullable = false)
    private Float personalQualitiesMaximum;
    
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Person author;
    
    // Конструкторы
    public LabWork() {
        this.creationDate = LocalDate.now();
    }
    
    public LabWork(String name, Coordinates coordinates, String description, 
                   Difficulty difficulty, Discipline discipline, int minimalPoint, 
                   Float personalQualitiesMaximum, Person author) {
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = LocalDate.now();
        this.description = description;
        this.difficulty = difficulty;
        this.discipline = discipline;
        this.minimalPoint = minimalPoint;
        this.personalQualitiesMaximum = personalQualitiesMaximum;
        this.author = author;
    }
    
    // Геттеры и сеттеры
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Coordinates getCoordinates() {
        return coordinates;
    }
    
    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }
    
    public LocalDate getCreationDate() {
        return creationDate;
    }
    
    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Difficulty getDifficulty() {
        return difficulty;
    }
    
    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }
    
    public Discipline getDiscipline() {
        return discipline;
    }
    
    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
    }
    
    public int getMinimalPoint() {
        return minimalPoint;
    }
    
    public void setMinimalPoint(int minimalPoint) {
        this.minimalPoint = minimalPoint;
    }
    
    public Float getPersonalQualitiesMaximum() {
        return personalQualitiesMaximum;
    }
    
    public void setPersonalQualitiesMaximum(Float personalQualitiesMaximum) {
        this.personalQualitiesMaximum = personalQualitiesMaximum;
    }
    
    public Person getAuthor() {
        return author;
    }
    
    public void setAuthor(Person author) {
        this.author = author;
    }
    
    @Override
    public String toString() {
        return "LabWork{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", description='" + description + '\'' +
                ", difficulty=" + difficulty +
                ", discipline=" + discipline +
                ", minimalPoint=" + minimalPoint +
                ", personalQualitiesMaximum=" + personalQualitiesMaximum +
                ", author=" + author +
                '}';
    }
}
