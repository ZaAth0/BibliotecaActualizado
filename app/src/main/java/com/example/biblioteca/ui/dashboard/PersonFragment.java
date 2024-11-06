package com.example.biblioteca.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.biblioteca.databinding.FragmentPersonBinding;

public class PersonFragment extends Fragment {

    private FragmentPersonBinding binding;
    private PerfilVista perfilVista;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        perfilVista = new ViewModelProvider(this).get(PerfilVista.class);

        binding = FragmentPersonBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Obtener las vistas
        final TextView userNameText = binding.NombreUsuarioValue;
        final TextView userEmailText = binding.UsuarioCorreoValue;
        final TextView userPhoneText = binding.UsuarioTelefonoValue;

        final EditText editUserName = binding.editUserName;
        final EditText editUserEmail = binding.editUserEmail;
        final EditText editUserPhone = binding.editUserPhone;

        final Button btnEdit = binding.btnEdit;
        final Button btnSave = binding.btnSave;

        // Mostrar los datos iniciales
        perfilVista.getUserName().observe(getViewLifecycleOwner(), userNameText::setText);
        perfilVista.getUserEmail().observe(getViewLifecycleOwner(), userEmailText::setText);
        perfilVista.getUserPhone().observe(getViewLifecycleOwner(), userPhoneText::setText);

        // Configurar el botón de "Editar"
        btnEdit.setOnClickListener(v -> {
            // Mostrar los campos editables y el botón de guardar
            editUserName.setVisibility(View.VISIBLE);
            editUserEmail.setVisibility(View.VISIBLE);
            editUserPhone.setVisibility(View.VISIBLE);
            btnSave.setVisibility(View.VISIBLE);

            // Rellenar los campos con los datos actuales
            editUserName.setText(userNameText.getText());
            editUserEmail.setText(userEmailText.getText());
            editUserPhone.setText(userPhoneText.getText());

            // Ocultar los datos estáticos
            userNameText.setVisibility(View.GONE);
            userEmailText.setVisibility(View.GONE);
            userPhoneText.setVisibility(View.GONE);
            btnEdit.setVisibility(View.GONE);
        });

        // Configurar el botón de "Guardar"
        btnSave.setOnClickListener(v -> {
            // Guardar los cambios
            perfilVista.updateUserName(editUserName.getText().toString());
            perfilVista.updateUserEmail(editUserEmail.getText().toString());
            perfilVista.updateUserPhone(editUserPhone.getText().toString());

            // Actualizar la UI con los nuevos datos
            userNameText.setText(editUserName.getText().toString());
            userEmailText.setText(editUserEmail.getText().toString());
            userPhoneText.setText(editUserPhone.getText().toString());

            // Volver a mostrar los datos estáticos y ocultar los campos editables
            editUserName.setVisibility(View.GONE);
            editUserEmail.setVisibility(View.GONE);
            editUserPhone.setVisibility(View.GONE);
            btnSave.setVisibility(View.GONE);

            // Volver a mostrar el botón de "Editar"
            userNameText.setVisibility(View.VISIBLE);
            userEmailText.setVisibility(View.VISIBLE);
            userPhoneText.setVisibility(View.VISIBLE);
            btnEdit.setVisibility(View.VISIBLE);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
