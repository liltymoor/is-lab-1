package com.crudoshlep.islab1.rest;

import com.crudoshlep.islab1.config.JacksonConfig;
import com.crudoshlep.islab1.model.Coordinates;
import com.crudoshlep.islab1.model.Location;
import com.crudoshlep.islab1.service.CoordinatesService;
import com.crudoshlep.islab1.websocket.CoordinatesWebSocket;
import com.crudoshlep.islab1.websocket.LocationWebSocket;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Optional;

@Path("/coordinates")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CoordinatesController {
    @Inject
    CoordinatesService coordinatesService;

    public CoordinatesController() {
    }

    public CoordinatesController(CoordinatesService coordinatesService) {
        this.coordinatesService = coordinatesService;
    }

    @POST
    public Response createCoordinates(Coordinates coordinates) {
        try {
            Coordinates created = coordinatesService.createCoordinates(coordinates);

            ObjectMapper mapper = new JacksonConfig().getContext(ObjectMapper.class);
            String json = mapper.writeValueAsString(coordinatesService.getAllCoordinates());
            CoordinatesWebSocket.broadcast(json);

            return Response.status(Response.Status.CREATED).entity(created).build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Ошибка при создании координат: " + e.getMessage()).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getCoordinatesById(@PathParam("id") Long id) {
        Optional<Coordinates> coordinates = coordinatesService.getCoordinatesById(id);
        if (coordinates.isPresent()) {
            return Response.ok(coordinates.get()).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Координаты с ID " + id + " не найдены").build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateCoordinates(@PathParam("id") Long id, @Valid Coordinates coordinates) {
        try {
            coordinates.setId(id);
            Coordinates updated = coordinatesService.updateCoordinates(coordinates);

            ObjectMapper mapper = new JacksonConfig().getContext(ObjectMapper.class);
            String json = mapper.writeValueAsString(coordinatesService.getAllCoordinates());
            CoordinatesWebSocket.broadcast(json);

            return Response.ok(updated).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Ошибка при обновлении координат: " + e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCoordinatesById(@PathParam("id") Long id) {
        try {
            boolean deleted = coordinatesService.deleteCoordinatesById(id);
            if (deleted) {
                ObjectMapper mapper = new JacksonConfig().getContext(ObjectMapper.class);
                String json = mapper.writeValueAsString(coordinatesService.getAllCoordinates());
                CoordinatesWebSocket.broadcast(json);

                return Response.ok("Координаты успешно удалена").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Локация с ID " + id + " не найден").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Ошибка при обновлении координат: " + e.getMessage()).build();
        }
    }

    @GET
    public Response getAllCoordinates(@QueryParam("offset") @DefaultValue("0") int offset,
                                    @QueryParam("limit") @DefaultValue("10") int limit,
                                    @QueryParam("x") Integer xFilter,
                                    @QueryParam("y") Float yFilter) {
        try {
            List<Coordinates> coordinates = coordinatesService.getCoordinatesWithFilters(xFilter, yFilter, offset, limit);
            long totalCount = coordinatesService.getTotalCount();

            return Response.ok()
                    .header("X-Total-Count", totalCount)
                    .header("X-Offset", offset)
                    .header("X-Limit", limit)
                    .entity(coordinates)
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Ошибка при получении списка авторов: " + e.getMessage()).build();
        }
    }
}
