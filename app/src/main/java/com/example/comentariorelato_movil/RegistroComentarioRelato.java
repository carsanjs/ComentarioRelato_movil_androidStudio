package com.example.comentariorelato_movil;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RegistroComentarioRelato extends AppCompatActivity {


    TextInputEditText id, nombre,comentario;

    Button btncancel, btnpublic;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_slideshow);

        //Botones
        btncancel = (Button) findViewById(R.id.btndeletE);
        btnpublic = (Button) findViewById(R.id.bntactualizarE);

        //textinputeditext
        id = (TextInputEditText) findViewById(R.id.codigoP);
        nombre = (TextInputEditText) findViewById(R.id.nombreP);
        comentario = (TextInputEditText) findViewById(R.id.comentarioP);


      btnpublic.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                String nombrerelato,coment,codigo;
                nombrerelato = nombre.getText().toString();
                coment = comentario.getText().toString();
                codigo = id.getText().toString();

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Publicaciones");

                if (id.getText().toString().isEmpty() || nombre.getText().toString().isEmpty() || comentario.getText().toString().isEmpty()) {

                    String mensaje = "Los campos no pueden estar vacios";
                    Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
                }
                else{

                    Map<String, Object> map = new HashMap<>();

                    map.put("id",id.getText().toString());
                    map.put("Nombre_Publicacion",nombre.getText().toString());
                    map.put("Comentario",comentario.getText().toString());
                    // Obtener la fecha actual
                    Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH) + 1;
                    int day = calendar.get(Calendar.DAY_OF_MONTH);

                    int hour = calendar.get(Calendar.HOUR_OF_DAY);
                    int minute = calendar.get(Calendar.MINUTE);
                    int second = calendar.get(Calendar.SECOND);

                    // Formatear la fecha y hora

                    String periodo;
                    if (calendar.get(Calendar.AM_PM) == Calendar.AM) {
                        periodo = "AM";
                    } else {
                        periodo = "PM";
                    }


                    // Formatear la fecha
                    String fecha_actual = String.format("%02d/%02d/%04d", day, month, year);
                    String horaActual = String.format("%02d:%02d:%02d", hour, minute, second);

                    map.put("Fecha_Creación", fecha_actual);
                    map.put("Hora de Publicación",horaActual + " " + periodo);
                    myRef.child(id.getText().toString()).updateChildren(map);

                    AlertDialog.Builder confirm = new AlertDialog.Builder(RegistroComentarioRelato.this);
                    confirm.setTitle("Comprobación");
                    confirm.setMessage("¿Desea hacer una Publicacion?");
                    confirm.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            CharSequence text = "se a publicado con exito";
                            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                            try { TimeUnit.SECONDS.sleep(1);}
                            catch (InterruptedException e) {throw new RuntimeException(e);}
//                            #me lleva a inicio index
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("valorExtra1", nombrerelato);
                            intent.putExtra("valorExtra2",coment);
                            intent.putExtra("valorExtra3", fecha_actual);
                            intent.putExtra("valorExtra4",horaActual);
                            intent.putExtra("valorExtra5",codigo);
                            startActivity(intent);

                        }
                    });
                    confirm.setNegativeButton("Denegar", null);
                    AlertDialog ventana = confirm.create();
                    ventana.show();
                    limpiarInput();
                }
            }
        });
        this.btncancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void limpiarInput(){
        id.setText("");
        nombre.setText("");
        comentario.setText("");
    }

}
