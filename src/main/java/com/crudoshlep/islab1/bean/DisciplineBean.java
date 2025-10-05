package com.crudoshlep.islab1.bean;

import com.crudoshlep.islab1.model.Discipline;
import com.crudoshlep.islab1.service.DisciplineService;
import jakarta.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import jakarta.validation.Valid;

import java.io.Serializable;
import java.util.List;

/**
 * Managed Bean для управления дисциплинами
 */
@ManagedBean(name = "disciplineBean")
@ViewScoped
public class DisciplineBean implements Serializable {
    
    @EJB
    private DisciplineService disciplineService;
    
    private List<Discipline> disciplines;
    private Discipline selectedDiscipline;
    private Discipline newDiscipline;
    
    // Фильтры
    private String nameFilter = "";
    
    // Пагинация
    private int currentPage = 0;
    private int pageSize = 10;
    private long totalCount = 0;
    
    // Сообщения
    private String message = "";
    private String messageType = "";
    
    public DisciplineBean() {
        initNewDiscipline();
    }
    
    /**
     * Инициализация новой дисциплины
     */
    private void initNewDiscipline() {
        newDiscipline = new Discipline();
    }
    
    /**
     * Загрузка данных при инициализации
     */
    public void loadData() {
        loadDisciplines();
    }
    
    /**
     * Загрузка дисциплин
     */
    public void loadDisciplines() {
        try {
            disciplines = disciplineService.getDisciplinesWithFilters(
                nameFilter, currentPage * pageSize, pageSize
            );
            totalCount = disciplineService.getTotalCount();
        } catch (Exception e) {
            showMessage("Ошибка при загрузке данных: " + e.getMessage(), "error");
        }
    }
    
    /**
     * Применение фильтров
     */
    public void applyFilters() {
        currentPage = 0;
        loadDisciplines();
    }
    
    /**
     * Очистка фильтров
     */
    public void clearFilters() {
        nameFilter = "";
        currentPage = 0;
        loadDisciplines();
    }
    
    /**
     * Создание новой дисциплины
     */
    public String createDiscipline() {
        try {
            disciplineService.createDiscipline(newDiscipline);
            showMessage("Дисциплина успешно создана", "success");
            initNewDiscipline();
            loadDisciplines();
            return "disciplines?faces-redirect=true";
        } catch (Exception e) {
            showMessage("Ошибка при создании дисциплины: " + e.getMessage(), "error");
            return null;
        }
    }
    
    /**
     * Обновление дисциплины
     */
    public String updateDiscipline() {
        try {
            disciplineService.updateDiscipline(selectedDiscipline);
            showMessage("Дисциплина успешно обновлена", "success");
            loadDisciplines();
            return "disciplines?faces-redirect=true";
        } catch (Exception e) {
            showMessage("Ошибка при обновлении дисциплины: " + e.getMessage(), "error");
            return null;
        }
    }
    
    /**
     * Удаление дисциплины
     */
    public String deleteDiscipline(Discipline discipline) {
        try {
            disciplineService.deleteDisciplineById(discipline.getId());
            showMessage("Дисциплина успешно удалена", "success");
            loadDisciplines();
            return "disciplines?faces-redirect=true";
        } catch (Exception e) {
            showMessage("Ошибка при удалении дисциплины: " + e.getMessage(), "error");
            return null;
        }
    }
    
    /**
     * Переход к редактированию
     */
    public String editDiscipline(Discipline discipline) {
        selectedDiscipline = discipline;
        return "edit-discipline?faces-redirect=true";
    }
    
    /**
     * Переход к просмотру
     */
    public String viewDiscipline(Discipline discipline) {
        selectedDiscipline = discipline;
        return "view-discipline?faces-redirect=true";
    }
    
    /**
     * Навигация по страницам
     */
    public void firstPage() {
        currentPage = 0;
        loadDisciplines();
    }
    
    public void previousPage() {
        if (currentPage > 0) {
            currentPage--;
            loadDisciplines();
        }
    }
    
    public void nextPage() {
        if (hasNextPage()) {
            currentPage++;
            loadDisciplines();
        }
    }
    
    public void lastPage() {
        currentPage = getTotalPages() - 1;
        loadDisciplines();
    }
    
    /**
     * Проверка наличия следующей страницы
     */
    public boolean hasNextPage() {
        return (currentPage + 1) * pageSize < totalCount;
    }
    
    /**
     * Проверка наличия предыдущей страницы
     */
    public boolean hasPreviousPage() {
        return currentPage > 0;
    }
    
    /**
     * Получение общего количества страниц
     */
    public int getTotalPages() {
        return (int) Math.ceil((double) totalCount / pageSize);
    }
    
    /**
     * Получение номера текущей страницы (для отображения)
     */
    public int getCurrentPageNumber() {
        return currentPage + 1;
    }
    
    /**
     * Показать сообщение
     */
    private void showMessage(String text, String type) {
        message = text;
        messageType = type;
    }
    
    // Геттеры и сеттеры
    public List<Discipline> getDisciplines() {
        if (disciplines == null) {
            loadData();
        }
        return disciplines;
    }
    
    public void setDisciplines(List<Discipline> disciplines) {
        this.disciplines = disciplines;
    }
    
    public Discipline getSelectedDiscipline() {
        return selectedDiscipline;
    }
    
    public void setSelectedDiscipline(Discipline selectedDiscipline) {
        this.selectedDiscipline = selectedDiscipline;
    }
    
    public Discipline getNewDiscipline() {
        return newDiscipline;
    }
    
    public void setNewDiscipline(Discipline newDiscipline) {
        this.newDiscipline = newDiscipline;
    }
    
    public String getNameFilter() {
        return nameFilter;
    }
    
    public void setNameFilter(String nameFilter) {
        this.nameFilter = nameFilter;
    }
    
    public int getCurrentPage() {
        return currentPage;
    }
    
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
    
    public int getPageSize() {
        return pageSize;
    }
    
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    
    public long getTotalCount() {
        return totalCount;
    }
    
    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
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
