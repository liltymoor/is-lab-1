package com.crudoshlep.islab1.bean;

import com.crudoshlep.islab1.model.*;
import com.crudoshlep.islab1.service.PersonService;
import jakarta.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import jakarta.validation.Valid;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Managed Bean для управления авторами
 */
@ManagedBean(name = "personBean")
@ViewScoped
public class PersonBean implements Serializable {
    
    @EJB
    private PersonService personService;
    
    private List<Person> persons;
    private Person selectedPerson;
    private Person newPerson;
    
    // Фильтры
    private String nameFilter = "";
    private String nationalityFilter = "";
    private String eyeColorFilter = "";
    
    // Пагинация
    private int currentPage = 0;
    private int pageSize = 10;
    private long totalCount = 0;
    
    // Сообщения
    private String message = "";
    private String messageType = "";
    
    public PersonBean() {
        initNewPerson();
    }
    
    /**
     * Инициализация нового автора
     */
    private void initNewPerson() {
        newPerson = new Person();
        newPerson.setLocation(new Location());
    }
    
    /**
     * Загрузка данных при инициализации
     */
    public void loadData() {
        loadPersons();
    }
    
    /**
     * Загрузка авторов
     */
    public void loadPersons() {
        try {
            persons = personService.getPersonsWithFilters(
                nameFilter, nationalityFilter, eyeColorFilter, 
                currentPage * pageSize, pageSize
            );
            totalCount = personService.getTotalCount();
        } catch (Exception e) {
            showMessage("Ошибка при загрузке данных: " + e.getMessage(), "error");
        }
    }
    
    /**
     * Применение фильтров
     */
    public void applyFilters() {
        currentPage = 0;
        loadPersons();
    }
    
    /**
     * Очистка фильтров
     */
    public void clearFilters() {
        nameFilter = "";
        nationalityFilter = "";
        eyeColorFilter = "";
        currentPage = 0;
        loadPersons();
    }
    
    /**
     * Создание нового автора
     */
    public String createPerson() {
        try {
            personService.createPerson(newPerson);
            showMessage("Автор успешно создан", "success");
            initNewPerson();
            loadPersons();
            return "persons?faces-redirect=true";
        } catch (Exception e) {
            showMessage("Ошибка при создании автора: " + e.getMessage(), "error");
            return null;
        }
    }
    
    /**
     * Обновление автора
     */
    public String updatePerson() {
        try {
            personService.updatePerson(selectedPerson);
            showMessage("Автор успешно обновлен", "success");
            loadPersons();
            return "persons?faces-redirect=true";
        } catch (Exception e) {
            showMessage("Ошибка при обновлении автора: " + e.getMessage(), "error");
            return null;
        }
    }
    
    /**
     * Удаление автора
     */
    public String deletePerson(Person person) {
        try {
            personService.deletePersonById(person.getId());
            showMessage("Автор успешно удален", "success");
            loadPersons();
            return "persons?faces-redirect=true";
        } catch (Exception e) {
            showMessage("Ошибка при удалении автора: " + e.getMessage(), "error");
            return null;
        }
    }
    
    /**
     * Переход к редактированию
     */
    public String editPerson(Person person) {
        selectedPerson = person;
        return "edit-person?faces-redirect=true";
    }
    
    /**
     * Переход к просмотру
     */
    public String viewPerson(Person person) {
        selectedPerson = person;
        return "view-person?faces-redirect=true";
    }
    
    /**
     * Навигация по страницам
     */
    public void firstPage() {
        currentPage = 0;
        loadPersons();
    }
    
    public void previousPage() {
        if (currentPage > 0) {
            currentPage--;
            loadPersons();
        }
    }
    
    public void nextPage() {
        if (hasNextPage()) {
            currentPage++;
            loadPersons();
        }
    }
    
    public void lastPage() {
        currentPage = getTotalPages() - 1;
        loadPersons();
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
    public List<Person> getPersons() {
        if (persons == null) {
            loadData();
        }
        return persons;
    }
    
    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }
    
    public Person getSelectedPerson() {
        return selectedPerson;
    }
    
    public void setSelectedPerson(Person selectedPerson) {
        this.selectedPerson = selectedPerson;
    }
    
    public Person getNewPerson() {
        return newPerson;
    }
    
    public void setNewPerson(Person newPerson) {
        this.newPerson = newPerson;
    }
    
    public String getNameFilter() {
        return nameFilter;
    }
    
    public void setNameFilter(String nameFilter) {
        this.nameFilter = nameFilter;
    }
    
    public String getNationalityFilter() {
        return nationalityFilter;
    }
    
    public void setNationalityFilter(String nationalityFilter) {
        this.nationalityFilter = nationalityFilter;
    }
    
    public String getEyeColorFilter() {
        return eyeColorFilter;
    }
    
    public void setEyeColorFilter(String eyeColorFilter) {
        this.eyeColorFilter = eyeColorFilter;
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
