package com.crudoshlep.islab1.rest;

import com.crudoshlep.islab1.config.JacksonConfig;
import com.crudoshlep.islab1.model.LabWork;
import com.crudoshlep.islab1.service.LabWorkService;
import com.crudoshlep.islab1.websocket.DisciplinesWebSocket;
import com.crudoshlep.islab1.websocket.LabWorkWebSocket;
import com.crudoshlep.islab1.websocket.LocationWebSocket;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

/**
 * REST контроллер для работы с лабораторными работами
 */
@Path("/labworks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LabWorkController {
    @Inject
    private LabWorkService labWorkService;

    public LabWorkController() {
    }

    public LabWorkController(LabWorkService labWorkService) {
        this.labWorkService = labWorkService;
    }
    
    /**
     * Создать новую лабораторную работу
     */
    @POST
    public Response createLabWork(@Valid LabWork labWork) {
        try {
            LabWork created = labWorkService.createLabWork(labWork);

            ObjectMapper mapper = new JacksonConfig().getContext(ObjectMapper.class);
            String json = mapper.writeValueAsString(labWorkService.getAllLabWorks());
            DisciplinesWebSocket.broadcast(json);

            return Response.status(Response.Status.CREATED).entity(created).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Ошибка при создании лабораторной работы: " + e.getMessage()).build();
        }
    }
    
    /**
     * Получить лабораторную работу по ID
     */
    @GET
    @Path("/{id}")
    public Response getLabWorkById(@PathParam("id") Integer id) {
        Optional<LabWork> labWork = labWorkService.getLabWorkById(id);
        if (labWork.isPresent()) {
            return Response.ok(labWork.get()).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Лабораторная работа с ID " + id + " не найдена").build();
        }
    }
    
    /**
     * Обновить лабораторную работу
     */
    @PUT
    @Path("/{id}")
    public Response updateLabWork(@PathParam("id") Integer id, @Valid LabWork labWork) {
        try {
            labWork.setId(id);
            LabWork updated = labWorkService.updateLabWork(labWork);

            ObjectMapper mapper = new JacksonConfig().getContext(ObjectMapper.class);
            String json = mapper.writeValueAsString(labWorkService.getAllLabWorks());
            DisciplinesWebSocket.broadcast(json);

            return Response.ok(updated).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Ошибка при обновлении лабораторной работы: " + e.getMessage()).build();
        }
    }
    
    /**
     * Удалить лабораторную работу по ID
     */
    @DELETE
    @Path("/{id}")
    public Response deleteLabWorkById(@PathParam("id") Integer id) {
        try {
            boolean deleted = labWorkService.deleteLabWorkById(id);
            if (deleted) {
                ObjectMapper mapper = new JacksonConfig().getContext(ObjectMapper.class);
                String json = mapper.writeValueAsString(labWorkService.getAllLabWorks());
                DisciplinesWebSocket.broadcast(json);

                return Response.ok("Лабораторная работа успешно удалена").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Лабораторная работа с ID " + id + " не найдена").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Ошибка при обновлении лабораторной работы: " + e.getMessage()).build();
        }

    }
    
    /**
     * Получить все лабораторные работы с пагинацией
     */
    @GET
    public Response getAllLabWorks(@QueryParam("offset") @DefaultValue("0") int offset,
                                  @QueryParam("limit") @DefaultValue("10") int limit,
                                  @QueryParam("name") String nameFilter,
                                  @QueryParam("difficulty") String difficultyFilter,
                                  @QueryParam("minimalPoint") Integer minimalPointFilter) {
        try {
            List<LabWork> labWorks;
            long totalCount;

            
            if (nameFilter != null || difficultyFilter != null || minimalPointFilter != null) {
                labWorks = labWorkService.getLabWorksWithFilters(nameFilter, difficultyFilter, 
                                                                minimalPointFilter, offset, limit);
                totalCount = labWorkService.getCountWithFilters(nameFilter, difficultyFilter, minimalPointFilter);
            } else {
                labWorks = labWorkService.getLabWorksWithPagination(offset, limit);
                totalCount = labWorkService.getTotalCount();
            }
            
            return Response.ok()
                    .header("X-Total-Count", totalCount)
                    .header("X-Offset", offset)
                    .header("X-Limit", limit)
                    .entity(labWorks)
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Ошибка при получении списка лабораторных работ: " + e.getMessage()).build();
        }
    }
    
    /**
     * Удалить все лабораторные работы с определенным минимальным количеством баллов
     */
    @DELETE
    @Path("/by-minimal-point/{minimalPoint}")
    public Response deleteByMinimalPoint(@PathParam("minimalPoint") int minimalPoint) {
        try {
            int deletedCount = labWorkService.deleteByMinimalPoint(minimalPoint);
            return Response.ok("Удалено " + deletedCount + " лабораторных работ").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Ошибка при удалении лабораторных работ: " + e.getMessage()).build();
        }
    }
    
    /**
     * Подсчитать количество лабораторных работ с определенным максимальным количеством баллов за личные качества
     */
    @GET
    @Path("/count/by-personal-qualities/{personalQualitiesMaximum}")
    public Response countByPersonalQualitiesMaximum(@PathParam("personalQualitiesMaximum") Float personalQualitiesMaximum) {
        try {
            long count = labWorkService.countByPersonalQualitiesMaximum(personalQualitiesMaximum);
            return Response.ok(count).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Ошибка при подсчете лабораторных работ: " + e.getMessage()).build();
        }
    }
    
    /**
     * Подсчитать количество лабораторных работ с автором меньше заданного
     */
    @GET
    @Path("/count/by-author-less-than/{authorId}")
    public Response countByAuthorLessThan(@PathParam("authorId") Long authorId) {
        try {
            long count = labWorkService.countByAuthorLessThan(authorId);
            return Response.ok(count).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Ошибка при подсчете лабораторных работ: " + e.getMessage()).build();
        }
    }
    
    /**
     * Повысить сложность лабораторной работы на указанное число шагов
     */
    @PUT
    @Path("/{id}/increase-difficulty")
    public Response increaseDifficulty(@PathParam("id") Integer id, 
                                     @QueryParam("steps") @DefaultValue("1") int steps) {
        try {
            boolean success = labWorkService.increaseDifficulty(id, steps);
            if (success) {
                return Response.ok("Сложность лабораторной работы успешно повышена").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Лабораторная работа с ID " + id + " не найдена или не имеет сложности").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Ошибка при повышении сложности: " + e.getMessage()).build();
        }
    }
    
    /**
     * Понизить сложность лабораторной работы на указанное число шагов
     */
    @PUT
    @Path("/{id}/decrease-difficulty")
    public Response decreaseDifficulty(@PathParam("id") Integer id, 
                                     @QueryParam("steps") @DefaultValue("1") int steps) {
        try {
            boolean success = labWorkService.decreaseDifficulty(id, steps);
            if (success) {
                return Response.ok("Сложность лабораторной работы успешно понижена").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Лабораторная работа с ID " + id + " не найдена или не имеет сложности").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Ошибка при понижении сложности: " + e.getMessage()).build();
        }
    }
}
