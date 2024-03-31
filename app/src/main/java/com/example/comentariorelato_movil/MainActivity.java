package com.example.comentariorelato_movil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.comentariorelato_movil.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {


    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    Button buttonsesion, buttonregistro;
    TextView textuser,textnombrerelato,textcomentario,textfechaactual,homefecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
       setSupportActionBar(binding.appBarMain.toolbar);

       ///////////////////////////se enpiesa///////////////////////
       buttonregistro = (Button) findViewById(R.id.bntregis);
        buttonsesion = (Button)findViewById(R.id.btnsesion) ;
        textuser = findViewById(R.id.textuser);
        textnombrerelato = findViewById(R.id.textnombrerelato);
        textcomentario = findViewById(R.id.textcomentario);
        textfechaactual = findViewById(R.id.textfechaactual);
        homefecha = findViewById(R.id.homefecha);


        Intent intent = getIntent();
        String user = intent.getStringExtra("valorExtra5");
        String nombrerelato = intent.getStringExtra("valorExtra1");
        String comentario = intent.getStringExtra("valorExtra2");
        String fechaactual = intent.getStringExtra("valorExtra3");
        String Hora = intent.getStringExtra("valorExtra4");

        textuser.setText(String.valueOf(user));
        textnombrerelato.setText(String.valueOf(nombrerelato));
        textcomentario.setText(String.valueOf(comentario));
        textfechaactual.setText(String.valueOf(fechaactual));
        homefecha.setText(String.valueOf(Hora));

      this.buttonregistro.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              startActivity(new Intent(MainActivity.this, registro.class));
          }
      });

        this.buttonsesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, iniciosession.class));
            }
        });

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_Buscargps, R.id.nav_camera)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}