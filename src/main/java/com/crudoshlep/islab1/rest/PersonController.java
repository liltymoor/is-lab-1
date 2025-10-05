package com.crudoshlep.islab1.rest;

import com.crudoshlep.islab1.model.Person;
import com.crudoshlep.islab1.service.PersonService;
import jakarta.ejb.EJB;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

/**
 * REST контроллер для работы с авторами
 */
@Path("/persons")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PersonController {
    @Inject
    private PersonService personService;
    
    /**
     * Создать нового автора
     */
    @POST
    public Response createPerson(@Valid Person person) {
        try {
            Person created = personService.createPerson(person);
            return Response.status(Response.Status.CREATED).entity(created).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Ошибка при создании автора: " + e.getMessage()).build();
        }
    }
    
    /**
     * Получить автора по ID
     */
    @GET
    @Path("/{id}")
    public Response getPersonById(@PathParam("id") Long id) {
        Optional<Person> person = personService.getPersonById(id);
        if (person.isPresent()) {
            return Response.ok(person.get()).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Автор с ID " + id + " не найден").build();
        }
    }
    
    /**
     * Обновить автора
     */
    @PUT
    @Path("/{id}")
    public Response updatePerson(@PathParam("id") Long id, @Valid Person person) {
        try {
            person.setId(id);
            Person updated = personService.updatePerson(person);
            return Response.ok(updated).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Ошибка при обновлении автора: " + e.getMessage()).build();
        }
    }
    
    /**
     * Удалить автора по ID
     */
    @DELETE
    @Path("/{id}")
    public Response deletePersonById(@PathParam("id") Long id) {
        boolean deleted = personService.deletePersonById(id);
        if (deleted) {
            return Response.ok("Автор успешно удален").build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Автор с ID " + id + " не найден").build();
        }
    }
    
    /**
     * Получить всех авторов с фильтрацией и пагинацией
     */
    @GET
    public Response getAllPersons(@QueryParam("offset") @DefaultValue("0") int offset,
                                 @QueryParam("limit") @DefaultValue("10") int limit,
                                 @QueryParam("name") String nameFilter,
                                 @QueryParam("nationality") String nationalityFilter,
                                 @QueryParam("eyeColor") String eyeColorFilter) {
        try {
            List<Person> persons = personService.getPersonsWithFilters(nameFilter, nationalityFilter, 
                                                                      eyeColorFilter, offset, limit);
            long totalCount = personService.getTotalCount();
            
            return Response.ok()
                    .header("X-Total-Count", totalCount)
                    .header("X-Offset", offset)
                    .header("X-Limit", limit)
                    .entity(persons)
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Ошибка при получении списка авторов: " + e.getMessage()).build();
        }
    }
}
