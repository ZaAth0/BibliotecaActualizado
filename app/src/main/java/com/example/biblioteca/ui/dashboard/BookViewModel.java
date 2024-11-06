package com.example.biblioteca.ui.dashboard;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.biblioteca.db.AppDatabase;
import com.example.biblioteca.db.Book;
import com.example.biblioteca.db.BookDao;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BookViewModel extends AndroidViewModel {
    private final BookDao bookDao;
    private final LiveData<List<Book>> allBooks;
    private final ExecutorService executorService;

    public BookViewModel(Application application) {
        super(application);
        AppDatabase db = AppDatabase.getDatabase(application);
        bookDao = db.bookDao();
        allBooks = bookDao.getAllBooks();
        executorService = Executors.newSingleThreadExecutor(); // Para operaciones en segundo plano
    }

    public LiveData<List<Book>> getAllBooks() {
        return allBooks;
    }

    public void insert(Book book) {
        executorService.execute(() -> bookDao.insert(book));
    }

    public void update(Book book) {
        executorService.execute(() -> bookDao.update(book));
    }

    public void delete(Book book) {
        executorService.execute(() -> bookDao.delete(book));
    }
}
