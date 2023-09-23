package com.example.junit.controller;

import com.example.junit.entity.Book;
import com.example.junit.repository.BookRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/book")
@AllArgsConstructor
public class BookController {
    private BookRepository bookRepository;

    @GetMapping
    public List<Book> getAllBooks(){
        return bookRepository.findAll();
    }

    @GetMapping("/{id}")
    public Book getBookById(@PathVariable("id") Long id){
        return bookRepository.findById(id).get();
    }

    @PostMapping
    public Book createBook(@RequestBody @Valid Book book){
        return bookRepository.save(book);
    }

    @PutMapping
    public Book updateBook(@RequestBody @Valid Book book) throws Exception {
        if(book == null || book.getId() == null){
            throw new Exception("Book id must not be null!");
        }
        Optional<Book> optionalBook = bookRepository.findById(book.getId());
        if(!optionalBook.isPresent()){
            throw new Exception("Book with id = " + book.getId() + " not found.");
        }

        Book existingBook = optionalBook.get();
        existingBook.setId(book.getId());
        existingBook.setName(book.getName());
        existingBook.setSummary(book.getSummary());
        existingBook.setRating(book.getRating());

        return bookRepository.save(existingBook);
    }

}
