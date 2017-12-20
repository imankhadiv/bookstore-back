package com.elrast.bookstore.repository;


import com.elrast.bookstore.model.Book;
import com.elrast.bookstore.model.Language;
import com.elrast.bookstore.util.IsbnGenerator;
import com.elrast.bookstore.util.NumberGenerator;
import com.elrast.bookstore.util.TextUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.Date;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class BookRepositoryTest {

    @Inject
    private BookRepository bookRepository;

    @Before
    public void setUp() throws Exception {
    }


    @Test
    public void shouldCreateBook() throws Exception {

        Book book = new Book("a title", "", 12F, "123", new Date(), 100, null, Language.DETACH);
        Book createdBook = bookRepository.create(book);
        assertNotNull(createdBook.getId());
    }

    @Test
    public void shouldBeEmptyBeforeCreatingBook() throws Exception {
         assertEquals(Long.valueOf(2), bookRepository.countAll());
        assertEquals(2, bookRepository.findAll().size());
    }

    @Test(expected = Exception.class)
    public void shouldNotBeAbleToCreateBookWithoutTitle() throws Exception {

        Book book = new Book(null, "", 12F, "123", new Date(), 100, null, Language.DETACH);
        Book createdBook = bookRepository.create(book);
    }

    @Test(expected = Exception.class)
    public void shouldNotFindABookWithoutValidId() throws Exception {

        Book book = bookRepository.find(null);
        assertTrue(book == null);
    }

    @Test
    public void shouldRemoveExtraSpaceFromTitle() {

        Book book = new Book("a  title", "", 12F, "123", new Date(), 100, null, Language.DETACH);
        book = bookRepository.create(book);
        assertEquals("a title", book.getTitle());
    }

    @Test
    public void shouldHaveAValidIsbnNumber() {

        Book book = new Book("a  title", "", 12F, "123", new Date(), 100, null, Language.DETACH);
        book = bookRepository.create(book);
        assertTrue(book.getIsbn().startsWith("123-456"));
    }

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClass(BookRepository.class)
                .addClass(Book.class)
                .addClass(Language.class)
                .addClass(TextUtil.class)
                .addClass(NumberGenerator.class)
                .addClass(IsbnGenerator.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsManifestResource("META-INF/test-persistence.xml", "persistence.xml");
    }
}