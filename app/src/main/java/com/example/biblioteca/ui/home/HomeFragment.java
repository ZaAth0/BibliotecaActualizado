package com.example.biblioteca.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.biblioteca.R;
import com.example.biblioteca.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Vincular el layout con ViewBinding
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Configurar el TextView de bienvenida
        TextView textView = binding.textView2;
        textView.setText("\"Cada libro es un puente hacia una vida más grande, más rica y más profunda.\"");


        // Configurar la imagen de la biblioteca
        ImageView libraryImage = binding.imageView10;
        libraryImage.setImageResource(R.drawable.biblio);  // Asegúrate de tener una imagen llamada 'biblio'


        Button welcomeButton = binding.button;

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
