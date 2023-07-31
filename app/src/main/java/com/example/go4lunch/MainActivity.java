package com.example.go4lunch;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.go4lunch.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_user, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();

        // Navigation Controller pour la NavigationView
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Get the BottomNavigationView reference
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Navigation Controller pour le BottomNavigationView
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        // Ajouter un écouteur de clic pour le menu de navigation
        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_logout) {
                // Appeler la fonction de déconnexion ici
                logoutUser();
                return true;
            }
            return NavigationUI.onNavDestinationSelected(item, navController);
        });
    }

    private void logoutUser() {
        // Ajoutez ici le code pour déconnecter l'utilisateur de votre application.
        // Cela peut impliquer d'utiliser FirebaseAuth pour déconnecter l'utilisateur actuel.
        // Voici comment vous pouvez le faire :

        // FirebaseAuth.getInstance().signOut();

        // Rediriger l'utilisateur vers l'écran de connexion ou une autre activité de départ si nécessaire.
        // Par exemple, si vous avez une activité de connexion LoginActivity, vous pouvez faire ceci :
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish(); // Fermer MainActivity pour qu'il ne soit pas accessible en appuyant sur le bouton "Retour".

        // Assurez-vous de mettre en place votre activité LoginActivity pour permettre à l'utilisateur de se connecter à nouveau.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.onNavDestinationSelected(item, navController) || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
