package com.crudoshlep.islab1.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

/**
 * Класс для представления координат
 */
@Entity
@Table(name = "coordinates")
public class Coordinates {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Min(value = -578, message = "Значение поля x должно быть больше -579")
    @Column(name = "x_coord", nullable = false)
    private int x;
    
    @Max(value = 101, message = "Максимальное значение поля y: 101")
    @Column(name = "y_coord", nullable = false)
    private float y;
    
    // Конструкторы
    public Coordinates() {}
    
    public Coordinates(int x, float y) {
        this.x = x;
        this.y = y;
    }
    
    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public int getX() {
        return x;
    }
    
    public void setX(int x) {
        this.x = x;
    }
    
    public float getY() {
        return y;
    }
    
    public void setY(float y) {
        this.y = y;
    }
    
    @Override
    public String toString() {
        return "Coordinates{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
