package com.example.biblioteca.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.biblioteca.R;
import com.example.biblioteca.db.Book;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private List<Book> bookList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onEditClick(Book book);
        void onDeleteClick(Book book);
        void onDetailsClick(Book book);
    }

    public BookAdapter(List<Book> bookList, OnItemClickListener listener) {
        this.bookList = bookList;
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_libro, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = bookList.get(position);

        holder.textNombreLibro.setText(book.getTitle());
        holder.textGenero.setText(book.getGenre());
        holder.textPaginas.setText(String.valueOf(book.getPageCount()));

        if (book.getImageUrl() != null && !book.getImageUrl().isEmpty()) {
            Picasso.get().load(book.getImageUrl()).into(holder.imageView);
        } else {
            holder.imageView.setImageResource(R.drawable.book); // Imagen por defecto
        }

        holder.buttonEdit.setOnClickListener(v -> onItemClickListener.onEditClick(book));
        holder.buttonDelete.setOnClickListener(v -> onItemClickListener.onDeleteClick(book));
        holder.buttonDetails.setOnClickListener(v -> onItemClickListener.onDetailsClick(book));
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public void updateList(List<Book> newBookList) {
        bookList.clear();
        bookList.addAll(newBookList);
        notifyDataSetChanged();
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textNombreLibro, textGenero, textPaginas;
        Button buttonEdit, buttonDelete, buttonDetails;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imagelibro);
            textNombreLibro = itemView.findViewById(R.id.text_nombre_libro);
            textGenero = itemView.findViewById(R.id.text_genero);
            textPaginas = itemView.findViewById(R.id.text_paginas);
            buttonEdit = itemView.findViewById(R.id.button_edit);
            buttonDelete = itemView.findViewById(R.id.button_eliminar);
            buttonDetails = itemView.findViewById(R.id.button_detalles);
        }
    }
}
