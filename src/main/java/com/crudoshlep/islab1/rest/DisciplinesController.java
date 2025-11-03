package com.crudoshlep.islab1.rest;

import com.crudoshlep.islab1.config.JacksonConfig;
import com.crudoshlep.islab1.model.Discipline;
import com.crudoshlep.islab1.model.Location;
import com.crudoshlep.islab1.service.DisciplineService;
import com.crudoshlep.islab1.service.LabWorkService;
import com.crudoshlep.islab1.websocket.DisciplinesWebSocket;
import com.crudoshlep.islab1.websocket.LocationWebSocket;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Optional;

@Path("/disciplines")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DisciplinesController {
    @Inject
    private DisciplineService disciplineService;

    public DisciplinesController() {
    }

    public DisciplinesController(DisciplineService disciplineService) {
        this.disciplineService = disciplineService;
    }

    @POST
    public Response createDiscipline(Discipline discipline) {
        try {
            Discipline created = disciplineService.createDiscipline(discipline);

            ObjectMapper mapper = new JacksonConfig().getContext(ObjectMapper.class);
            String json = mapper.writeValueAsString(disciplineService.getAllDisciplines());
            DisciplinesWebSocket.broadcast(json);

            return Response.status(Response.Status.CREATED).entity(created).build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Ошибка при создании дисциплины: " + e.getMessage()).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getDisciplineById(@PathParam("id") Long id) {
        Optional<Discipline> discipline = disciplineService.getDisciplineById(id);
        if (discipline.isPresent()) {
            return Response.ok(discipline.get()).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Дисциплина с ID " + id + " не найдена").build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateDiscipline(@PathParam("id") Long id, @Valid Discipline discipline) {
        try {
            discipline.setId(id);
            Discipline updated = disciplineService.updateDiscipline(discipline);

            ObjectMapper mapper = new JacksonConfig().getContext(ObjectMapper.class);
            String json = mapper.writeValueAsString(disciplineService.getAllDisciplines());
            DisciplinesWebSocket.broadcast(json);

            return Response.ok(updated).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Ошибка при обновлении дисциплины: " + e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteDisciplineById(@PathParam("id") Long id) {
        try {
            boolean deleted = disciplineService.deleteDisciplineById(id);
            if (deleted) {
                ObjectMapper mapper = new JacksonConfig().getContext(ObjectMapper.class);
                String json = mapper.writeValueAsString(disciplineService.getAllDisciplines());
                DisciplinesWebSocket.broadcast(json);

                return Response.ok("Дисциплина успешно удалена").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Дисциплина с ID " + id + " не найден").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Ошибка при обновлении дисциплины: " + e.getMessage()).build();
        }
    }

    @GET
    public Response getAllDisciplines(@QueryParam("offset") @DefaultValue("0") int offset,
                                    @QueryParam("limit") @DefaultValue("10") int limit,
                                    @QueryParam("name") String nameFilter) {
        try {
            List<Discipline> disciplines = disciplineService.getDisciplinesWithFilters(nameFilter, offset, limit);
            long totalCount = disciplineService.getTotalCount();
            return Response.ok()
                    .header("X-Total-Count", totalCount)
                    .header("X-Offset", offset)
                    .header("X-Limit", limit)
                    .entity(disciplines)
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Ошибка при получении списка дисциплин: " + e.getMessage()).build();
        }
    }
}
