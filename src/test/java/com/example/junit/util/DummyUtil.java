package com.example.junit.util;

import com.example.junit.entity.Book;
import org.springframework.stereotype.Component;

@Component
public class DummyUtil {
    public static Book getBookData(Long id, String name, String summary, Integer rating){
        return Book.builder().id(id).name(name).summary(summary).rating(rating).build();
    }
}
