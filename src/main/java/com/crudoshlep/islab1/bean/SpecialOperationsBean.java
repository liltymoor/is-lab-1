package com.crudoshlep.islab1.bean;

import com.crudoshlep.islab1.service.LabWorkService;
import jakarta.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import java.io.Serializable;

/**
 * Managed Bean для специальных операций
 */
@ManagedBean(name = "specialOperationsBean")
@ViewScoped
public class SpecialOperationsBean implements Serializable {
    
    @EJB
    private LabWorkService labWorkService;
    
    // Параметры для операций
    private Integer deleteMinimalPoint;
    private Float countPersonalQualities;
    private Long countAuthorId;
    private Integer difficultyLabWorkId;
    private Integer difficultySteps = 1;
    
    // Результаты операций
    private String countResult;
    
    // Сообщения
    private String message = "";
    private String messageType = "";
    
    /**
     * Удалить все лабораторные работы с определенным минимальным количеством баллов
     */
    public String deleteByMinimalPoint() {
        try {
            if (deleteMinimalPoint == null) {
                showMessage("Введите количество минимальных баллов", "error");
                return null;
            }
            
            int deletedCount = labWorkService.deleteByMinimalPoint(deleteMinimalPoint);
            showMessage("Удалено " + deletedCount + " лабораторных работ с минимальными баллами " + deleteMinimalPoint, "success");
            deleteMinimalPoint = null;
            return "special-operations?faces-redirect=true";
        } catch (Exception e) {
            showMessage("Ошибка при удалении лабораторных работ: " + e.getMessage(), "error");
            return null;
        }
    }
    
    /**
     * Подсчитать количество лабораторных работ с определенным максимальным количеством баллов за личные качества
     */
    public String countByPersonalQualitiesMaximum() {
        try {
            if (countPersonalQualities == null) {
                showMessage("Введите максимальное количество баллов за личные качества", "error");
                return null;
            }
            
            long count = labWorkService.countByPersonalQualitiesMaximum(countPersonalQualities);
            countResult = "Найдено " + count + " лабораторных работ с максимальными баллами за личные качества: " + countPersonalQualities;
            showMessage("Операция выполнена успешно", "success");
            return "special-operations?faces-redirect=true";
        } catch (Exception e) {
            showMessage("Ошибка при подсчете лабораторных работ: " + e.getMessage(), "error");
            return null;
        }
    }
    
    /**
     * Подсчитать количество лабораторных работ с автором меньше заданного
     */
    public String countByAuthorLessThan() {
        try {
            if (countAuthorId == null) {
                showMessage("Введите ID автора", "error");
                return null;
            }
            
            long count = labWorkService.countByAuthorLessThan(countAuthorId);
            countResult = "Найдено " + count + " лабораторных работ с автором, ID которого меньше " + countAuthorId;
            showMessage("Операция выполнена успешно", "success");
            return "special-operations?faces-redirect=true";
        } catch (Exception e) {
            showMessage("Ошибка при подсчете лабораторных работ: " + e.getMessage(), "error");
            return null;
        }
    }
    
    /**
     * Повысить сложность лабораторной работы
     */
    public String increaseDifficulty() {
        try {
            if (difficultyLabWorkId == null || difficultySteps == null) {
                showMessage("Введите ID лабораторной работы и количество шагов", "error");
                return null;
            }
            
            boolean success = labWorkService.increaseDifficulty(difficultyLabWorkId, difficultySteps);
            if (success) {
                showMessage("Сложность лабораторной работы #" + difficultyLabWorkId + " успешно повышена на " + difficultySteps + " шагов", "success");
            } else {
                showMessage("Лабораторная работа с ID " + difficultyLabWorkId + " не найдена или не имеет сложности", "error");
            }
            return "special-operations?faces-redirect=true";
        } catch (Exception e) {
            showMessage("Ошибка при повышении сложности: " + e.getMessage(), "error");
            return null;
        }
    }
    
    /**
     * Понизить сложность лабораторной работы
     */
    public String decreaseDifficulty() {
        try {
            if (difficultyLabWorkId == null || difficultySteps == null) {
                showMessage("Введите ID лабораторной работы и количество шагов", "error");
                return null;
            }
            
            boolean success = labWorkService.decreaseDifficulty(difficultyLabWorkId, difficultySteps);
            if (success) {
                showMessage("Сложность лабораторной работы #" + difficultyLabWorkId + " успешно понижена на " + difficultySteps + " шагов", "success");
            } else {
                showMessage("Лабораторная работа с ID " + difficultyLabWorkId + " не найдена или не имеет сложности", "error");
            }
            return "special-operations?faces-redirect=true";
        } catch (Exception e) {
            showMessage("Ошибка при понижении сложности: " + e.getMessage(), "error");
            return null;
        }
    }
    
    /**
     * Показать сообщение
     */
    private void showMessage(String text, String type) {
        message = text;
        messageType = type;
    }
    
    // Геттеры и сеттеры
    public Integer getDeleteMinimalPoint() {
        return deleteMinimalPoint;
    }
    
    public void setDeleteMinimalPoint(Integer deleteMinimalPoint) {
        this.deleteMinimalPoint = deleteMinimalPoint;
    }
    
    public Float getCountPersonalQualities() {
        return countPersonalQualities;
    }
    
    public void setCountPersonalQualities(Float countPersonalQualities) {
        this.countPersonalQualities = countPersonalQualities;
    }
    
    public Long getCountAuthorId() {
        return countAuthorId;
    }
    
    public void setCountAuthorId(Long countAuthorId) {
        this.countAuthorId = countAuthorId;
    }
    
    public Integer getDifficultyLabWorkId() {
        return difficultyLabWorkId;
    }
    
    public void setDifficultyLabWorkId(Integer difficultyLabWorkId) {
        this.difficultyLabWorkId = difficultyLabWorkId;
    }
    
    public Integer getDifficultySteps() {
        return difficultySteps;
    }
    
    public void setDifficultySteps(Integer difficultySteps) {
        this.difficultySteps = difficultySteps;
    }
    
    public String getCountResult() {
        return countResult;
    }
    
    public void setCountResult(String countResult) {
        this.countResult = countResult;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getMessageType() {
        return messageType;
    }
    
    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }
}
