package com.example.biblioteca.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.biblioteca.R;
import com.example.biblioteca.db.Book;

public class BookDetailsDialogFragment extends DialogFragment {
    private static final String ARG_BOOK = "book"; // Clave para el libro

    private Book book; // Objeto Book

    // Método estático para crear el diálogo con el objeto Book
    public static BookDetailsDialogFragment newInstance(Book book) {
        BookDetailsDialogFragment fragment = new BookDetailsDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_BOOK, book); // Usamos putParcelable para pasar el objeto Book
        fragment.setArguments(args); // Añadimos los argumentos al fragmento
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Usamos getParcelable para recuperar el objeto Book desde los argumentos
            book = getArguments().getParcelable(ARG_BOOK);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflamos el layout del fragmento
        View view = inflater.inflate(R.layout.dialog_book_details, container, false);

        TextView titleTextView = view.findViewById(R.id.text_title);
        TextView genreTextView = view.findViewById(R.id.text_genre);
        TextView pageCountTextView = view.findViewById(R.id.text_page_count);
        TextView authorTextView = view.findViewById(R.id.text_author);
        TextView publishDateTextView = view.findViewById(R.id.text_publish_date);

        if (book != null) {
            // Si el libro no es nulo, establecemos los valores en las vistas
            titleTextView.setText(book.getTitle());
            genreTextView.setText(book.getGenre());
            pageCountTextView.setText(String.valueOf(book.getPageCount()));
            authorTextView.setText(book.getAuthor()); // Establecemos el autor
            publishDateTextView.setText(book.getPublicationDate()); // Establecemos la fecha de publicación
        }

        return view;
    }
}
