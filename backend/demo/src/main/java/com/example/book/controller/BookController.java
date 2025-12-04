package com.example.book.controller;

import com.example.book.domain.Book;
import com.example.book.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    //책등록
    @PostMapping
    public Book createBook(@RequestBody Book book){
        return bookService.insertBook(book);
    }
    //책 업데이터

    //책 업데이터(PATCH)

    //책삭제

    //책 조회 (단건)
    @GetMapping("/{bookId}")
    public Book getBook(@PathVariable("bookId") Long bookId) {
        return bookService.findBook(bookId);
    }

    //책 조회 (다건)
}
