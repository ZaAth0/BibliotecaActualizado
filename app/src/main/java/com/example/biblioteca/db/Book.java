package com.example.biblioteca.db;

import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import android.os.Parcel;



@Entity(tableName = "books")
public class Book implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int id; // Identificador único del libro
    private String title; // Nombre del libro
    private String author; // Autor del libro
    private String synopsis; // Sinopsis del libro
    private int pageCount; // Cantidad de páginas
    private String genre; // Género del libro
    private String publicationDate; // Fecha de publicación
    private String imageUrl; // URL de la imagen de portada

    // Constructor vacío necesario para Serializable
    public Book() {}

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    protected Book(Parcel in) {
        id = in.readInt(); // Recuperamos los valores del parcel
        title = in.readString();
        author = in.readString();
        synopsis = in.readString();
        pageCount = in.readInt();
        genre = in.readString();
        publicationDate = in.readString();
        imageUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id); // Escribimos los valores en el parcel
        dest.writeString(title);
        dest.writeString(author);
        dest.writeString(synopsis);
        dest.writeInt(pageCount);
        dest.writeString(genre);
        dest.writeString(publicationDate);
        dest.writeString(imageUrl);

    }

    @Override
    public int describeContents() {
        return 0; // Retornamos 0 para indicar que no tiene contenido especial
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in); // Creamos un objeto Book a partir del Parcel
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size]; // Creamos un arreglo de Book
        }
    };
}


