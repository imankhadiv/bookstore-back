package com.elrast.bookstore.repository;


import com.elrast.bookstore.model.Book;
import com.elrast.bookstore.util.NumberGenerator;
import com.elrast.bookstore.util.TextUtil;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.List;

import static javax.transaction.Transactional.TxType.REQUIRED;
import static javax.transaction.Transactional.TxType.SUPPORTS;

@Transactional(SUPPORTS)
public class BookRepository {

    @PersistenceContext(unitName = "bookStorePU")
    private EntityManager entityManager;

    @Inject
    TextUtil textUtil;

    @Inject
    NumberGenerator numberGenerator;

    public Book find(@NotNull Long id) {
        return entityManager.find(Book.class, id);
    }

    @Transactional(REQUIRED)
    public Book create(Book book) {
        book.setTitle(textUtil.sanitize(book.getTitle()));
        book.setIsbn(numberGenerator.generateNumber());
        entityManager.persist(book);
        return book;
    }

    @Transactional(REQUIRED)
    public void delete(Long id) {
        entityManager.remove(entityManager.getReference(Book.class, id));
    }

    public List<Book> findAll() {
        TypedQuery<Book> query = entityManager.createQuery("select b from Book b order by b.title DESC ", Book.class);
        return query.getResultList();
    }

    public Long countAll() {
        TypedQuery<Long> query = entityManager.createQuery("select count(b) from Book b ", Long.class);
        return query.getSingleResult();
    }
}
