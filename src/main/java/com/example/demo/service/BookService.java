package com.example.demo.service;

import com.example.demo.dto.BookDto;
import com.example.demo.model.Book;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;


@Service
public class BookService {
    private final ConcurrentHashMap<Long, Book> bookRepository = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    private BookDto mapToDto(Book book){
        return new BookDto(book.id(), book.title(), book.author(), book.isAvailable());
    }

    public List<BookDto> getAllBooks(){
        return bookRepository.values().stream()
                .map(this::mapToDto)
                .collect(Collectors.toUnmodifiableList());
    }
    public Optional<BookDto> getBookById(Long id){
        Book book = bookRepository.get(id);
        if (book == null){
            return Optional.empty();
        }
        return Optional.of(mapToDto(book));
    }
    public BookDto createBook (BookDto bookDto){
        Long newId = idGenerator.getAndIncrement();
        Book newBook = new Book(newId, bookDto.title(), bookDto.author(), bookDto.isAvailable());

        bookRepository.put(newId, newBook);
        return mapToDto(newBook);
    }
    public boolean deleteBook(Long id){
        return bookRepository.remove(id) != null;
    }


}

