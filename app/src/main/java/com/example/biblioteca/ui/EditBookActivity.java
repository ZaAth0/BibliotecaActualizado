package com.example.biblioteca.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.biblioteca.R;
import com.example.biblioteca.db.AppDatabase;
import com.example.biblioteca.db.Book;

public class EditBookActivity extends AppCompatActivity {
    private EditText editTitle, editGenre, editPageCount, editAuthor, editPublishDate;
    private ImageView bookImage;
    private Book book;
    private Uri imageUri;

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editbook_activity);

        editTitle = findViewById(R.id.edit_title);
        editGenre = findViewById(R.id.edit_genre);
        editPageCount = findViewById(R.id.edit_page_count);
        editAuthor = findViewById(R.id.edit_author);
        editPublishDate = findViewById(R.id.edit_publish_date);
        bookImage = findViewById(R.id.edit_book_image);

        int bookId = getIntent().getIntExtra("book_id", -1);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }

        // Cargar libro desde la base de datos si el ID es válido
        if (bookId != -1) {
            new Thread(() -> {
                AppDatabase db = AppDatabase.getDatabase(this);
                book = db.bookDao().getBookById(bookId);

                runOnUiThread(() -> {
                    if (book != null) {
                        editTitle.setText(book.getTitle());
                        editGenre.setText(book.getGenre());
                        editPageCount.setText(String.valueOf(book.getPageCount()));
                        editAuthor.setText(book.getAuthor());
                        editPublishDate.setText(book.getPublicationDate());

                        if (book.getImageUrl() != null) {
                            imageUri = Uri.parse(book.getImageUrl());
                            bookImage.setImageURI(imageUri);
                        }
                    } else {
                        Toast.makeText(this, "Error: libro no encontrado.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }).start();
        } else {
            Toast.makeText(this, "ID de libro inválido.", Toast.LENGTH_SHORT).show();
            finish();
        }

        Button changeImageButton = findViewById(R.id.button_change_image);
        changeImageButton.setOnClickListener(v -> openImagePicker());

        Button saveButton = findViewById(R.id.button_save);
        saveButton.setOnClickListener(v -> saveChanges());
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                imageUri = selectedImageUri;
                bookImage.setImageURI(imageUri);

                // Conceder permisos para que la aplicación acceda a la URI más adelante
                getContentResolver().takePersistableUriPermission(
                        imageUri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                );
            }
        }
    }


    private void saveChanges() {
        String title = editTitle.getText().toString();
        String genre = editGenre.getText().toString();
        String pageCountStr = editPageCount.getText().toString();
        String author = editAuthor.getText().toString();
        String publishDate = editPublishDate.getText().toString();

        if (title.isEmpty() || genre.isEmpty() || pageCountStr.isEmpty() || author.isEmpty() || publishDate.isEmpty()) {
            runOnUiThread(() -> Toast.makeText(this, "Por favor completa todos los campos.", Toast.LENGTH_SHORT).show());
            return;
        }

        try {
            int pageCount = Integer.parseInt(pageCountStr);

            new Thread(() -> {
                if (book != null) {
                    book.setTitle(title);
                    book.setGenre(genre);
                    book.setPageCount(pageCount);
                    book.setAuthor(author);
                    book.setPublicationDate(publishDate);

                    if (imageUri != null) {
                        book.setImageUrl(imageUri.toString());
                    }

                    AppDatabase db = AppDatabase.getDatabase(this);
                    db.bookDao().update(book);

                    runOnUiThread(() -> {
                        Toast.makeText(this, "Cambios guardados exitosamente.", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                }
            }).start();

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Número de páginas inválido.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImagePicker();
            } else {
                Toast.makeText(this, "Permiso denegado para acceder a la galería", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
