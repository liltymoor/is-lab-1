package com.crudoshlep.islab1.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

/**
 * Класс для представления дисциплины
 */
@Entity
@Table(name = "disciplines")
public class Discipline {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Название дисциплины не может быть пустым")
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "lecture_hours")
    private long lectureHours;
    
    @NotNull(message = "Количество часов практики не может быть null")
    @Column(name = "practice_hours", nullable = false)
    private Integer practiceHours;
    
    @NotNull(message = "Количество часов самостоятельной работы не может быть null")
    @Column(name = "self_study_hours", nullable = false)
    private Integer selfStudyHours;
    
    // Конструкторы
    public Discipline() {}
    
    public Discipline(String name, long lectureHours, Integer practiceHours, Integer selfStudyHours) {
        this.name = name;
        this.lectureHours = lectureHours;
        this.practiceHours = practiceHours;
        this.selfStudyHours = selfStudyHours;
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
    
    public long getLectureHours() {
        return lectureHours;
    }
    
    public void setLectureHours(long lectureHours) {
        this.lectureHours = lectureHours;
    }
    
    public Integer getPracticeHours() {
        return practiceHours;
    }
    
    public void setPracticeHours(Integer practiceHours) {
        this.practiceHours = practiceHours;
    }
    
    public Integer getSelfStudyHours() {
        return selfStudyHours;
    }
    
    public void setSelfStudyHours(Integer selfStudyHours) {
        this.selfStudyHours = selfStudyHours;
    }
    
    @Override
    public String toString() {
        return "Discipline{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lectureHours=" + lectureHours +
                ", practiceHours=" + practiceHours +
                ", selfStudyHours=" + selfStudyHours +
                '}';
    }
}
