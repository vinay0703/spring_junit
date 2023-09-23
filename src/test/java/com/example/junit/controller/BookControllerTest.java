package com.example.junit.controller;

import com.example.junit.entity.Book;
import com.example.junit.repository.BookRepository;
import com.example.junit.util.DummyUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.print.attribute.standard.Media;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class BookControllerTest {

    private MockMvc mockMvc;
    private ObjectWriter objectWriter;
    @Mock
    private BookRepository bookRepository;
    @InjectMocks
    private BookController bookController;

    @BeforeEach
    void setup() {
        objectWriter = new ObjectMapper().writer();
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
    }

    @ParameterizedTest
    @MethodSource("provideListBookData")
    void getAllBooks_success(List<Book> books) throws Exception {
        // given
        when(bookRepository.findAll()).thenReturn(books);

        // when
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/book")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(books.size())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", is(books.get(0).getName())));

        // verify
        verify(bookRepository).findAll();
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, -1L, 3L})
    void getBookById_success(Long id) throws Exception {
        // given
        Book book = DummyUtil.getBookData(id, "A", "A", 1);
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));

        // when
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/book/" + id)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", is(book.getName())));

        // verify
        verify(bookRepository).findById(id);
    }

    @Test
    void createRecord_success() throws Exception {
        // given
        Book book = DummyUtil.getBookData(1L, "A", "a", 1);
        String content = objectWriter.writeValueAsString(book);
        when(bookRepository.save(book)).thenReturn(book);

        // when
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/book")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(content)
                )
                .andExpect(MockMvcResultMatchers.status().isOk());

        // verify
        verify(bookRepository).save(book);
    }

    @Test
    void deleteBookById_success() throws Exception{
        // given
        Book book = DummyUtil.getBookData(1L, "A", "a", 1);
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));

        // when
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/book/" + book.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private static Stream<List<Book>> provideListBookData() {
        List<Book> books1 = List.of(DummyUtil.getBookData(1L, "A", "A", 1),
                DummyUtil.getBookData(2L, "B", "B", 2));
        List<Book> books2 = List.of(DummyUtil.getBookData(3L, "C", "C", 3));
        return Stream.of(
                books1, books2
        );
    }
}
