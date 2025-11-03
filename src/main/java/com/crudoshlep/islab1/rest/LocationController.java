package com.crudoshlep.islab1.rest;

import com.crudoshlep.islab1.config.JacksonConfig;
import com.crudoshlep.islab1.model.Location;
import com.crudoshlep.islab1.service.LocationService;
import com.crudoshlep.islab1.websocket.LocationWebSocket;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Optional;

@Path("/locations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LocationController {
    @Inject
    private LocationService locationService;

    public LocationController() {
    }

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @POST
    public Response createLocation(Location location) {
        try {
            Location created = locationService.createLocation(location);

            ObjectMapper mapper = new JacksonConfig().getContext(ObjectMapper.class);
            String json = mapper.writeValueAsString(locationService.getAllLocations());
            LocationWebSocket.broadcast(json);

            return Response.status(Response.Status.CREATED).entity(created).build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Ошибка при создании локации: " + e.getMessage()).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getLocationById(@PathParam("id") Long id) {
        Optional<Location> location = locationService.getLocationById(id);
        if (location.isPresent()) {
            return Response.ok(location.get()).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Локация с ID " + id + " не найдена").build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateLocation(@PathParam("id") Long id, @Valid Location location) {
        try {
            location.setId(id);
            Location updated = locationService.updateLocation(location);
            return Response.ok(updated).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Ошибка при обновлении локации: " + e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteLocationById(@PathParam("id") Long id) {
        try {
            boolean deleted = locationService.deleteLocationById(id);
            if (deleted) {

                ObjectMapper mapper = new JacksonConfig().getContext(ObjectMapper.class);
                String json = mapper.writeValueAsString(locationService.getAllLocations());
                LocationWebSocket.broadcast(json);

                return Response.ok("Локация успешно удалена").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Локация с ID " + id + " не найден").build();
            }
        }
        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Ошибка при создании локации: " + e.getMessage()).build();
        }
    }

    @GET
    public Response getAllLocations(@QueryParam("offset") @DefaultValue("0") int offset,
                                  @QueryParam("limit") @DefaultValue("10") int limit,
                                  @QueryParam("name") String nameFilter,
                                  @QueryParam("x") Double xFilter,
                                  @QueryParam("y") Float yFilter,
                                  @QueryParam("z") Float zFilter) {
        try {
            List<Location> locations = locationService.getLocationsWithFilters(nameFilter, xFilter, yFilter, zFilter, offset, limit);
            long totalCount = locationService.getTotalCount();
            /*
            Ошибка при получении списка авторов: JDBC exception executing SQL [select l1_0.id,l1_0.name,l1_0.x_coord,l1_0.y_coord,l1_0.z_coord from locations l1_0 where lower(l1_0.name) like ? escape '' order by l1_0.id offset ? rows fetch first ? rows only] [Ошибка ввода/вывода при отправке бэкенду] [n/a]
             */
            return Response.ok()
                    .header("X-Total-Count", totalCount)
                    .header("X-Offset", offset)
                    .header("X-Limit", limit)
                    .entity(locations)
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Ошибка при получении списка авторов: " + e.getMessage()).build();
        }
    }
}
