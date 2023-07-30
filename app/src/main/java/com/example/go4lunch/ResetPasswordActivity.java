package com.example.go4lunch;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.databinding.ActivityResetPasswordBinding;

public class ResetPasswordActivity extends AppCompatActivity {
    private ActivityResetPasswordBinding binding;
    private AuthViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResetPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        binding.btnResetPassword.setOnClickListener(v -> {
            String email = binding.etEmail.getText().toString().trim();
            viewModel.resetPassword(email);
        });
    }
}
