package com.example.biblioteca.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;
import androidx.lifecycle.LiveData;

import java.util.List;
@Dao
public interface BookDao {
    // Método para insertar un libro
    @Insert
    void insert(Book book);

    // Método para obtener todos los libros
    @Query("SELECT * FROM books")
    LiveData<List<Book>> getAllBooks(); // Este es el método correcto

    // Método para obtener un libro por su nombre
    @Query("SELECT * FROM books WHERE title = :bookTitle LIMIT 1")
    Book getBookByTitle(String bookTitle);

    // Método para actualizar un libro
    @Update
    void update(Book book);

    // Método para eliminar un libro
    @Delete
    void delete(Book book);

    @Query("SELECT * FROM books WHERE id = :id LIMIT 1")
    Book getBookById(int id);
}

