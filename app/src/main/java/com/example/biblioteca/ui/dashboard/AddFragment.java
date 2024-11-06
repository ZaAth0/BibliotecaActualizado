package com.example.biblioteca.ui.dashboard;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.biblioteca.databinding.FragmentAddBinding;
import com.example.biblioteca.db.AppDatabase;
import com.example.biblioteca.db.Book;
import com.example.biblioteca.db.BookDao;

import java.util.List;

public class AddFragment extends Fragment {
    private static final int GALLERY_REQUEST_CODE = 1; // Constante para el código de solicitud de galería

    private FragmentAddBinding binding;
    private AppDatabase db;
    private BookDao bookDao;
    private Uri imageUri; // Para almacenar la URI de la imagen seleccionada

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AddVista addViewModel = new ViewModelProvider(this).get(AddVista.class);

        binding = FragmentAddBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Inicializar la base de datos
        db = AppDatabase.getDatabase(requireContext());
        bookDao = db.bookDao();

        // Agregar el Observer para obtener todos los libros
        bookDao.getAllBooks().observe(getViewLifecycleOwner(), new Observer<List<Book>>() {
            @Override
            public void onChanged(List<Book> books) {
                // Aquí puedes hacer algo con la lista de libros
                // Por ejemplo, puedes imprimir el tamaño de la lista en Logcat
                if (books != null) {
                    Toast.makeText(getActivity(), "Número de libros: " + books.size(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Capturar los campos de entrada
        final EditText tituloLibroInput = binding.TituloLibro; // Nombre del libro
        final EditText autorLibroInput = binding.AutorLibro; // Autor del libro
        final EditText sinopsisLibroInput = binding.SinopsisLibro; // Sinopsis
        final EditText paginasLibroInput = binding.PaginasLibro; // Cantidad de páginas
        final EditText generoLibroInput = binding.GeneroLibro; // Género
        final EditText fechaPublicacionInput = binding.FechaPublicacionLibro; // Fecha de publicación
        final ImageView portadasImageView = binding.PortadaLibro; // ImageView para mostrar la portada
        final Button botonSeleccionarImagen = binding.buttonPortada; // Botón para seleccionar imagen
        final Button botonAgregar = binding.botonAgregar; // Botón para agregar

        // Configurar el clic del botón para seleccionar la imagen
        botonSeleccionarImagen.setOnClickListener(v -> openGallery());

        // Configurar el clic del botón para guardar
        botonAgregar.setOnClickListener(v -> saveBook(
                tituloLibroInput,
                autorLibroInput,
                sinopsisLibroInput,
                paginasLibroInput,
                generoLibroInput,
                fechaPublicacionInput,
                portadasImageView
        ));

        return root;
    }

    // Método para abrir la galería y seleccionar una imagen
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == getActivity().RESULT_OK && data != null) {
            imageUri = data.getData();
            binding.PortadaLibro.setImageURI(imageUri); // Mostrar la imagen seleccionada
        }
    }

    // Método para guardar el libro
    private void saveBook(EditText tituloLibroInput, EditText autorLibroInput, EditText sinopsisLibroInput,
                          EditText paginasLibroInput, EditText generoLibroInput, EditText fechaPublicacionInput,
                          ImageView portadasImageView) {
        // Obtener los datos ingresados
        String titulo = tituloLibroInput.getText().toString();
        String autor = autorLibroInput.getText().toString();
        String sinopsis = sinopsisLibroInput.getText().toString();
        String paginasString = paginasLibroInput.getText().toString();
        String genero = generoLibroInput.getText().toString();
        String fechaPublicacion = fechaPublicacionInput.getText().toString();

        // Validar que los campos no estén vacíos
        if (titulo.isEmpty() || autor.isEmpty() || sinopsis.isEmpty() || paginasString.isEmpty() || genero.isEmpty() || fechaPublicacion.isEmpty()) {
            Toast.makeText(getActivity(), "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convertir la cantidad de páginas a un entero
        int paginas;
        try {
            paginas = Integer.parseInt(paginasString);
        } catch (NumberFormatException e) {
            Toast.makeText(getActivity(), "Por favor, ingrese un número válido para las páginas.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear el nuevo libro
        Book newBook = new Book();
        newBook.setTitle(titulo);
        newBook.setAuthor(autor);
        newBook.setSynopsis(sinopsis);
        newBook.setPageCount(paginas);
        newBook.setGenre(genero);
        newBook.setPublicationDate(fechaPublicacion);
        // Añadir la URL de la imagen si se ha seleccionado
        if (imageUri != null) {
            newBook.setImageUrl(imageUri.toString());
        }

        // Guardar el libro en la base de datos
        new Thread(() -> {
            try {
                bookDao.insert(newBook);
            } catch (Exception e) {
                e.printStackTrace(); // Esto mostrará el error en Logcat
            }
            // Mostrar un mensaje de éxito
            getActivity().runOnUiThread(() -> {
                Toast.makeText(getActivity(), "Libro guardado", Toast.LENGTH_SHORT).show();
                clearFields(tituloLibroInput, autorLibroInput, sinopsisLibroInput, paginasLibroInput, generoLibroInput, fechaPublicacionInput, portadasImageView);
            });
        }).start();
    }

    // Método para limpiar los campos después de guardar
    private void clearFields(EditText titulo, EditText autor, EditText sinopsis, EditText paginas,
                             EditText genero, EditText fechaPublicacion, ImageView portada) {
        titulo.setText("");
        autor.setText("");
        sinopsis.setText("");
        paginas.setText("");
        genero.setText("");
        fechaPublicacion.setText("");
        portada.setImageResource(0); // Limpiar la imagen
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
