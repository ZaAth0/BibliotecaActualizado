package com.example.biblioteca.ui.dashboard;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.biblioteca.databinding.FragmentSearchBinding;
import com.example.biblioteca.db.AppDatabase;
import com.example.biblioteca.db.Book;
import com.example.biblioteca.db.BookDao;
import com.example.biblioteca.ui.BookDetailsDialogFragment;
import com.example.biblioteca.ui.EditBookActivity;
import com.example.biblioteca.ui.adapters.BookAdapter;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    private BookAdapter bookAdapter;
    private List<Book> bookList;




    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Inicializar la lista de libros y el adaptador
        bookList = new ArrayList<>();
        bookAdapter = new BookAdapter(bookList, new BookAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(Book book) {
                // Manejar clic en editar

                Intent intent = new Intent(getContext(), EditBookActivity.class);
                intent.putExtra("book_id", book.getId()); // Envía el ID del libro para cargar sus datos
                startActivity(intent);

            }

            @Override
            public void onDeleteClick(Book book) {
                // Manejar clic en eliminar
                new Thread(() -> {
                    AppDatabase db = AppDatabase.getDatabase(getContext());
                    db.bookDao().delete(book);

                    getActivity().runOnUiThread(() -> {
                        bookList.remove(book);
                        bookAdapter.notifyDataSetChanged();
                    });
                }).start();
            }

            @Override
            public void onDetailsClick(Book book) {
                // Manejar clic en detalles

                BookDetailsDialogFragment dialogFragment = BookDetailsDialogFragment.newInstance(book);
                dialogFragment.show(getChildFragmentManager(), "BookDetailsDialog");
            }
        });

        // Configurar el RecyclerView
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(bookAdapter);

        // Cargar libros desde la base de datos
        loadBooks();

        // Configurar el SearchView
        SearchView searchView = binding.searchView;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Manejar la búsqueda cuando se envía el texto
                searchBooks(query);
                searchView.clearFocus(); // Limpiar el foco después de la búsqueda
                return true; // Indica que el evento ha sido manejado
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filtrar la lista en tiempo real
                if (newText.isEmpty()) {
                    loadBooks(); // Cargar todos los libros si no hay texto
                } else {
                    searchBooks(newText); // Filtrar la búsqueda
                }
                return true;
            }
        });

        return root;
    }

    private void loadBooks() {
        // Obtener la base de datos
        AppDatabase db = AppDatabase.getDatabase(getContext());
        BookDao bookDao = db.bookDao();

        // Observar los cambios en los libros
        bookDao.getAllBooks().observe(getViewLifecycleOwner(), new Observer<List<Book>>() {
            @Override
            public void onChanged(List<Book> books) {
                bookList.clear();
                bookList.addAll(books);
                bookAdapter.notifyDataSetChanged();
            }
        });
    }

    private void searchBooks(String query) {
        // Obtener la base de datos
        AppDatabase db = AppDatabase.getDatabase(getContext());
        BookDao bookDao = db.bookDao();

        // Cargar los libros que coinciden con la búsqueda
        bookDao.getAllBooks().observe(getViewLifecycleOwner(), books -> {
            List<Book> results = new ArrayList<>();

            if (books != null) {
                for (Book book : books) {
                    if (book.getTitle().toLowerCase().contains(query.toLowerCase())) {
                        results.add(book);
                    }
                }
            }

            // Actualizar el adaptador con los resultados filtrados

                bookList.clear();
                bookList.addAll(results);
                bookAdapter.notifyDataSetChanged();

        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
