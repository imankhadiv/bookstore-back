package com.elrast.bookstore.rest;


import com.elrast.bookstore.model.Book;
import com.elrast.bookstore.repository.BookRepository;

import javax.inject.Inject;
import javax.validation.constraints.Min;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

@Path("/books")
public class BookEndpoint {

    @Inject
    private BookRepository bookRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBooks() {
        List<Book> bookList = bookRepository.findAll();
        if (bookList.size() == 0) {
            return Response.noContent().build();
        }
        return Response.ok(bookList).build();
    }

    @GET
    @Path("/count")
    public Response countBooks() {
        Long nOfBooks = bookRepository.countAll();
        if (nOfBooks == 0) return Response.noContent().build();
        return Response.ok(nOfBooks).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createBook(Book book, @Context UriInfo uriInfo) {

        book = bookRepository.create(book);
        URI createURI = uriInfo.getBaseUriBuilder().path(book.getId().toString()).build(book);
        return Response.created(createURI).build();
    }

    @DELETE
    @Path("/{id : \\d+}")
    public Response deleteBook(@PathParam("id") @Min(1) Long id) {
        bookRepository.delete(id);
        return Response.noContent().build();
    }

    @GET
    @Path("/{id : \\d+}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBook(@PathParam("id") @Min(1) Long id) {

        Book book = bookRepository.find(id);
        if (book == null) return Response.noContent().build();
        return Response.ok(book).build();
    }
}
