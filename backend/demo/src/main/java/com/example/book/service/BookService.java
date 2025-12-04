package com.example.book.service;

import com.example.book.domain.Book;
import com.example.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    //책 등록
    public Book insertBook(Book book) {
        return bookRepository.save(book);
    }

    //책업데이트
    public Book updateBook(Long id, Book book) {
        Book b = getBook(id);

        b.setTitle(book.getTitle());
        b.setSubTitle(book.getSubTitle());
        b.setAuthor(book.getAuthor());
        b.setPublisher(book.getPublisher());
        b.setStatus(book.getStatus());

        return bookRepository.save(b);


    }

    private Book getBook(Long id) {
       return bookRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 책 입니다"));

    }
    //책 업데이터(PATCH)
    public Book updateBook(Long id, Book.Status status){
        Book b = getBook(id);
        b.setStatus(status);
        return bookRepository.save(b);
    }
    //책 삭제
    public void deleteBook(long id){
        Book b = getBook(id);
        if (b.getStatus() == Book.Status.BORROWED){
            throw new IllegalArgumentException("대여중인 책은 삭제할수 없습니다");
        }
        bookRepository.delete(b);
    }
    //책 조회()단건
    public Book findBook(Long id){
        return getBook(id);
    }
    //책 조회 다건
    public List<Book> findBooks(){
       return bookRepository.findAll();
    }
}
