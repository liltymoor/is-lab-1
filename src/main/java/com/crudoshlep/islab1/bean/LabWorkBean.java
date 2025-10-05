package com.crudoshlep.islab1.bean;

import com.crudoshlep.islab1.model.*;
import com.crudoshlep.islab1.service.LabWorkService;
import com.crudoshlep.islab1.service.PersonService;
import jakarta.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import jakarta.validation.Valid;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Managed Bean для управления лабораторными работами
 */
@ManagedBean(name = "labWorkBean")
@ViewScoped
public class LabWorkBean implements Serializable {
    
    @EJB
    private LabWorkService labWorkService;
    
    @EJB
    private PersonService personService;
    
    private List<LabWork> labWorks;
    private LabWork selectedLabWork;
    private LabWork newLabWork;
    
    // Фильтры
    private String nameFilter = "";
    private String difficultyFilter = "";
    private Integer minimalPointFilter;
    
    // Пагинация
    private int currentPage = 0;
    private int pageSize = 10;
    private long totalCount = 0;
    
    // Списки для выбора
    private List<Person> availablePersons;
    private List<Discipline> availableDisciplines;
    
    // Сообщения
    private String message = "";
    private String messageType = "";
    
    public LabWorkBean() {
        initNewLabWork();
    }
    
    /**
     * Инициализация новой лабораторной работы
     */
    private void initNewLabWork() {
        newLabWork = new LabWork();
        newLabWork.setCoordinates(new Coordinates());
        newLabWork.setDiscipline(new Discipline());
        newLabWork.setAuthor(new Person());
        if (newLabWork.getAuthor() != null) {
            newLabWork.getAuthor().setLocation(new Location());
        }
    }
    
    /**
     * Загрузка данных при инициализации
     */
    public void loadData() {
        loadLabWorks();
        loadAvailablePersons();
        loadAvailableDisciplines();
    }
    
    /**
     * Загрузка лабораторных работ
     */
    public void loadLabWorks() {
        try {
            labWorks = labWorkService.getLabWorksWithFilters(
                nameFilter, difficultyFilter, minimalPointFilter, 
                currentPage * pageSize, pageSize
            );
            totalCount = labWorkService.getCountWithFilters(
                nameFilter, difficultyFilter, minimalPointFilter
            );
        } catch (Exception e) {
            showMessage("Ошибка при загрузке данных: " + e.getMessage(), "error");
        }
    }
    
    /**
     * Загрузка доступных авторов
     */
    public void loadAvailablePersons() {
        try {
            availablePersons = personService.getAllPersons();
        } catch (Exception e) {
            availablePersons = new ArrayList<>();
        }
    }
    
    /**
     * Загрузка доступных дисциплин
     */
    public void loadAvailableDisciplines() {
        // Здесь можно добавить сервис для дисциплин
        availableDisciplines = new ArrayList<>();
        // Пока создаем тестовые данные
        Discipline discipline1 = new Discipline();
        discipline1.setId(1L);
        discipline1.setName("Программирование");
        discipline1.setLectureHours(40);
        discipline1.setPracticeHours(20);
        discipline1.setSelfStudyHours(30);
        availableDisciplines.add(discipline1);
    }
    
    /**
     * Применение фильтров
     */
    public void applyFilters() {
        currentPage = 0;
        loadLabWorks();
    }
    
    /**
     * Очистка фильтров
     */
    public void clearFilters() {
        nameFilter = "";
        difficultyFilter = "";
        minimalPointFilter = null;
        currentPage = 0;
        loadLabWorks();
    }
    
    /**
     * Создание новой лабораторной работы
     */
    public String createLabWork() {
        try {
            labWorkService.createLabWork(newLabWork);
            showMessage("Лабораторная работа успешно создана", "success");
            initNewLabWork();
            loadLabWorks();
            return "labworks?faces-redirect=true";
        } catch (Exception e) {
            showMessage("Ошибка при создании лабораторной работы: " + e.getMessage(), "error");
            return null;
        }
    }
    
    /**
     * Обновление лабораторной работы
     */
    public String updateLabWork() {
        try {
            labWorkService.updateLabWork(selectedLabWork);
            showMessage("Лабораторная работа успешно обновлена", "success");
            loadLabWorks();
            return "labworks?faces-redirect=true";
        } catch (Exception e) {
            showMessage("Ошибка при обновлении лабораторной работы: " + e.getMessage(), "error");
            return null;
        }
    }
    
    /**
     * Удаление лабораторной работы
     */
    public String deleteLabWork(LabWork labWork) {
        try {
            labWorkService.deleteLabWorkById(labWork.getId());
            showMessage("Лабораторная работа успешно удалена", "success");
            loadLabWorks();
            return "labworks?faces-redirect=true";
        } catch (Exception e) {
            showMessage("Ошибка при удалении лабораторной работы: " + e.getMessage(), "error");
            return null;
        }
    }
    
    /**
     * Переход к редактированию
     */
    public String editLabWork(LabWork labWork) {
        selectedLabWork = labWork;
        return "edit-labwork?faces-redirect=true";
    }
    
    /**
     * Переход к просмотру
     */
    public String viewLabWork(LabWork labWork) {
        selectedLabWork = labWork;
        return "view-labwork?faces-redirect=true";
    }
    
    /**
     * Навигация по страницам
     */
    public void firstPage() {
        currentPage = 0;
        loadLabWorks();
    }
    
    public void previousPage() {
        if (currentPage > 0) {
            currentPage--;
            loadLabWorks();
        }
    }
    
    public void nextPage() {
        if (hasNextPage()) {
            currentPage++;
            loadLabWorks();
        }
    }
    
    public void lastPage() {
        currentPage = getTotalPages() - 1;
        loadLabWorks();
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
    
    /**
     * Получение списка сложностей для выбора
     */
    public Difficulty[] getDifficulties() {
        return Difficulty.values();
    }
    
    /**
     * Получение списка цветов для выбора
     */
    public Color[] getColors() {
        return Color.values();
    }
    
    /**
     * Получение списка стран для выбора
     */
    public Country[] getCountries() {
        return Country.values();
    }
    
    // Геттеры и сеттеры
    public List<LabWork> getLabWorks() {
        if (labWorks == null) {
            loadData();
        }
        return labWorks;
    }
    
    public void setLabWorks(List<LabWork> labWorks) {
        this.labWorks = labWorks;
    }
    
    public LabWork getSelectedLabWork() {
        return selectedLabWork;
    }
    
    public void setSelectedLabWork(LabWork selectedLabWork) {
        this.selectedLabWork = selectedLabWork;
    }
    
    public LabWork getNewLabWork() {
        return newLabWork;
    }
    
    public void setNewLabWork(LabWork newLabWork) {
        this.newLabWork = newLabWork;
    }
    
    public String getNameFilter() {
        return nameFilter;
    }
    
    public void setNameFilter(String nameFilter) {
        this.nameFilter = nameFilter;
    }
    
    public String getDifficultyFilter() {
        return difficultyFilter;
    }
    
    public void setDifficultyFilter(String difficultyFilter) {
        this.difficultyFilter = difficultyFilter;
    }
    
    public Integer getMinimalPointFilter() {
        return minimalPointFilter;
    }
    
    public void setMinimalPointFilter(Integer minimalPointFilter) {
        this.minimalPointFilter = minimalPointFilter;
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
    
    public List<Person> getAvailablePersons() {
        return availablePersons;
    }
    
    public void setAvailablePersons(List<Person> availablePersons) {
        this.availablePersons = availablePersons;
    }
    
    public List<Discipline> getAvailableDisciplines() {
        return availableDisciplines;
    }
    
    public void setAvailableDisciplines(List<Discipline> availableDisciplines) {
        this.availableDisciplines = availableDisciplines;
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
