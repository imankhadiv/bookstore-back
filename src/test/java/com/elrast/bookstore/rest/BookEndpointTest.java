package com.elrast.bookstore.rest;

import com.elrast.bookstore.model.Book;
import com.elrast.bookstore.model.Language;
import com.elrast.bookstore.repository.BookRepository;
import com.elrast.bookstore.util.IsbnGenerator;
import com.elrast.bookstore.util.NumberGenerator;
import com.elrast.bookstore.util.TextUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.extension.rest.client.ArquillianResteasyResource;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.resteasy.spi.LoggableFailure;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;

import static org.junit.Assert.assertEquals;


@RunWith(Arquillian.class)
@RunAsClient
public class BookEndpointTest {

    @Test
    public void createBook(@ArquillianResteasyResource("api/books") WebTarget webTarget) throws Exception {

        // test counting books
        Response response = webTarget.path("count").request().get();
        assertEquals(Response.Status.NO_CONTENT, response.getStatus());

        if(true)return;

        // Test find all
        response = webTarget.request(MediaType.APPLICATION_JSON).get();
        assertEquals(Response.Status.NO_CONTENT, response.getStatus());

        // Test creating a book
        Book book = new Book("a  title", "", 12F, "123", new Date(), 100, null, Language.DETACH);
        response = webTarget.request(MediaType.APPLICATION_JSON).post(Entity.entity(book, MediaType.APPLICATION_JSON));
        assertEquals(Response.Status.OK, response.getStatus());


    }

    @Deployment(testable = false) // no to package the test class inside archive
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClass(BookRepository.class)
                .addClass(Book.class)
                .addClass(Language.class)
                .addClass(TextUtil.class)
                .addClass(NumberGenerator.class)
                .addClass(IsbnGenerator.class)
                .addClass(BookEndpoint.class)
                .addClass(JAXRSConfiguration.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsManifestResource("META-INF/test-persistence.xml", "persistence.xml");
    }
}