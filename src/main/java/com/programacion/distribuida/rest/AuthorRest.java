package com.programacion.distribuida.rest;

import com.programacion.distribuida.db.Authors;
import com.programacion.distribuida.servicios.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@ApplicationScoped
@RequiredArgsConstructor
@Path("/authors")
@Consumes(MediaType.APPLICATION_JSON)
public class AuthorRest {

     AuthorRepository repository;

    @GET
    @Path("/{id}")
    @Operation(summary = "Autor por su Id")
    @APIResponses({@APIResponse(responseCode = "404", description = "no se puede encontrar"),
                   @APIResponse(responseCode = "200",description = "encontrado",
                                content = @Content(mediaType = "application/json",
                                                   schema = @Schema(implementation = Authors.class)))}
    )
    public Authors findById(@PathParam("id") Integer id) {
        return repository.findById(id);
    }

    @GET
    @Operation(summary = "Lista completa")
    @APIResponses({@APIResponse(responseCode = "404", description = "Lista no encontrada"),
            @APIResponse(responseCode = "200",description = "Lista encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Authors.class, type = SchemaType.ARRAY)))}
    )
    public List<Authors> findAll() {
        return repository
                .findAll()
                .list();
    }

    @POST
    @Operation(summary = "Crear Autor", description = "Crear Autor")
    public void insert(@RequestBody(description = "Autor a Crear ",
                                    required = true,
                                    content = @Content(schema = @Schema(implementation = Authors.class))) Authors obj) {
        repository.persist(obj);
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Actualizar", description = "actualizar Autor")
    public void update(@RequestBody(description = "Autor a actualizar",
                                    required = true,
                                    content = @Content(schema = @Schema(implementation = Authors.class))) Authors obj,
                       @Parameter(description = "Id del Autora actualizar", required = true)
                       @PathParam("id") Integer id) {

        var author = repository.findById(id);

        author.setFirtName(obj.getFirtName());
        author.setLastName(obj.getLastName());
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Elimina Autor", description = "Eliminar Autor")
    public void delete(@Parameter(description = "Autor a eliminar", required = true)
                           @PathParam("id") Integer id ) {
        repository.deleteById(id);
    }
}
