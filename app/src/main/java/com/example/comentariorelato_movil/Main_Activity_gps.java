package com.example.comentariorelato_movil;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;
import java.util.Locale;

public class Main_Activity_gps  extends AppCompatActivity {

    private TextView txt_longitud, txt_latitud,textView_location,speed_s,altitude_a,bearing_b,accuracy_a;
    private Button btn_coordenadas;
    private ProgressBar progressBar;

    private Handler handler;
    private static final int TIEMPO_ESPERA = 100000; // 2 segundos
    LocationManager locationManager;

    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_gps);

        txt_latitud = (TextView) findViewById(R.id.txt_latiutd);
        txt_longitud = (TextView) findViewById(R.id.txt_longuitud);
        textView_location=(TextView)findViewById(R.id.textView_location);

        speed_s=(TextView)findViewById(R.id.speed);
        altitude_a=(TextView)findViewById(R.id.altitude);
        bearing_b=(TextView)findViewById(R.id.bearing);
        accuracy_a=(TextView)findViewById(R.id.accuracy);

        btn_coordenadas = (Button) findViewById(R.id.id_btn);
        progressBar =(ProgressBar) findViewById(R.id.progressBar);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        handler = new Handler();

        if(ContextCompat.checkSelfPermission(Main_Activity_gps.this, Manifest.permission.ACCESS_COARSE_LOCATION) != (PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

        }


        btn_coordenadas.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Mostrar ProgressBar
                progressBar.setVisibility(View.VISIBLE);
                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(Main_Activity_gps.this, new OnSuccessListener<Location>(){
                    @Override
                        public void onSuccess (Location location){
                        progressBar.setVisibility(View.GONE);
                        float speed,bearing,accuracy;
                        double altitude;
                        String speedString,altitudeString,bearingString,accuracyString;

                   if(location!=null){
                       txt_latitud.setText(String.valueOf(location.getLatitude()));
                        txt_longitud.setText(String.valueOf(location.getLongitude()));
                       try {
                           Geocoder geocoder = new Geocoder(Main_Activity_gps.this, Locale.getDefault());
                           List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                           String address = addresses.get(0).getAddressLine(0);

                           speed = location.getSpeed();
                           speedString = String.format("%.2f",speed);
                           speed_s.setText(speedString);

                           altitude = location.getAltitude();
                           altitudeString =String.format("%.2f",altitude);
                           altitude_a.setText(altitudeString);

                           bearing = location.getBearing();
                           bearingString =String.format("%.2f",bearing);
                           bearing_b.setText(bearingString);

                           accuracy = location.getAccuracy();
                           accuracyString =String.format("%.2f",accuracy);
                           accuracy_a.setText(accuracyString);

                           textView_location.setText(address);
                           // Abrir Google Maps con las coordenadas
                           double latitude = location.getLatitude();
                           double longitude = location.getLongitude();
                           String uri = "geo:" + latitude + "," + longitude + "?q=" + latitude + "," + longitude;
                           Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                           intent.setPackage("com.google.android.apps.maps");

                           // Verificar si hay una aplicación de mapas instalada
                           if (intent.resolveActivity(getPackageManager()) != null) {
                               CharSequence text = "Iniciando Busqueda...";
                               Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                               startActivity(intent);
                           } else {
                               CharSequence text = "No se encontró una aplicación de mapas";
                               Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                           }

                       }catch (Exception e){
                           e.printStackTrace();
                       }
                   }
                    else{
                        txt_latitud.setText("null");
                        txt_longitud.setText("null");
                    }

                }

            });

            }
        });



        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Ocultar ProgressBar después del tiempo de espera
                progressBar.setVisibility(View.GONE);

                // Ocultar resultados anteriores
                txt_latitud.setText("");
                txt_longitud.setText("");
                textView_location.setText("");
            }
        }, TIEMPO_ESPERA);









    }
}