package com.example.comentariorelato_movil;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Edit_DeletComentarioRelato extends AppCompatActivity {

    Button btncancel, btnupdate, btndelet, btnbuscar;
    EditText id,nombre,comentario,fecha;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_gallery);

       btncancel = (Button) findViewById(R.id.btncancelE);
        btnupdate = (Button) findViewById(R.id.bntactualizarE);
        btndelet = (Button) findViewById(R.id.btndeletE);
        btnbuscar = (Button) findViewById(R.id.btnbuscar);

        id = (EditText) findViewById(R.id.txtcodigoE);
        nombre = (EditText) findViewById(R.id.txtnombrepublicacionE);
        comentario = (EditText) findViewById(R.id.txtcomentarioedit);
        fecha = (EditText) findViewById(R.id.txtfechapublicacione);


        this.btndelet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = id.getText().toString();

                if (code.isEmpty()){
                    CharSequence text = "El campo codigo no puede estar vacio";
                    Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                }else{
                    DatabaseReference db = FirebaseDatabase.getInstance().getReference("Publicaciones");
                    db.child(code).removeValue();
                    CharSequence text = "Se ha eliminado la Publicación correctamente";
                    Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                    BorrarInput();
                }
            }
        });

        this.btncancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnbuscar.setOnClickListener(View ->{
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("Publicaciones");
            myRef.child(id.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (!id.getText().toString().isEmpty() && snapshot.exists()) {
                        String Nombre = snapshot.child("Nombre_Publicacion").getValue().toString();
                        String Comentario = snapshot.child("Comentario").getValue().toString();
                        String Fecha = snapshot.child("Fecha_Creación").getValue().toString();
                        nombre.setText(Nombre);
                        comentario.setText(Comentario);
                        fecha.setText(Fecha);

                    }

                    if(id.getText().toString().isEmpty()){
                        Context context = getApplicationContext();
                        String mensaje = "INGRESE LOS DATOS";
                        Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
                    }

                    if(!snapshot.exists()) {
                        BorrarInput();
                        Context context = getApplicationContext();
                        String mensaje = "PUBLICACIÓN NO ENCONTRADA";
                        Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    String mensaje = "La búsqueda de datos fue cancelada. Error: " + error.getMessage();
                    Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
                }
            });
        });

        this.btnupdate.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                String nombrerelato,coment,codigo,date;
                nombrerelato = nombre.getText().toString();
                coment = comentario.getText().toString();
                codigo = id.getText().toString();
                date = fecha.getText().toString();

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Publicaciones");


                if (id.getText().toString().isEmpty() || nombre.getText().toString().isEmpty() || comentario.getText().toString().isEmpty() || fecha.getText().toString().isEmpty()) {

                    String text = "Algunos campos están vacíos o son incorrectos.";
                    Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                }
                else{

                        Map<String, Object> map = new HashMap<>();
                        map.put("id", id.getText().toString());
                        map.put("Nombre_Publicacion", nombre.getText().toString());
                        map.put("Comentario", comentario.getText().toString());
                        map.put("Fecha_Creación", fecha.getText().toString());
                        Calendar calendar = Calendar.getInstance();

                        int hour = calendar.get(Calendar.HOUR_OF_DAY);
                        int minute = calendar.get(Calendar.MINUTE);
                        int second = calendar.get(Calendar.SECOND);



                    String periodo;
                    if (calendar.get(Calendar.AM_PM) == Calendar.AM) {
                        periodo = "AM";
                    } else {
                        periodo = "PM";
                    }


                    // Formatear la fecha
                    String horaActual = String.format("%02d:%02d:%02d", hour, minute, second);
                    map.put("Hora de Publicación",horaActual + " " + periodo);
                    myRef.child(id.getText().toString()).updateChildren(map);

                    map.put("Hora de Publicación",horaActual + " " + periodo);
                        AlertDialog.Builder confirm = new AlertDialog.Builder(Edit_DeletComentarioRelato.this);
                        confirm.setTitle("Comprobación");
                        confirm.setMessage("¿Desea editar un usuario?");
                        confirm.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                CharSequence text = "Usuario a sido actualizado con mucho éxito";
                                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                                try { TimeUnit.SECONDS.sleep(1);}
                                catch (InterruptedException e) {throw new RuntimeException(e);}
//                            #me lleva a inicio index
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                                intent.putExtra("valorExtra1", nombrerelato);
                                intent.putExtra("valorExtra2",coment);
                                intent.putExtra("valorExtra3", date);
                                intent.putExtra("valorExtra4",horaActual);
                                intent.putExtra("valorExtra5",codigo);
                                startActivity(intent);
                            }
                        });
                        confirm.setNegativeButton("Denegar", null);
                        AlertDialog ventana = confirm.create();
                        ventana.show();
                        BorrarInput();
                }

            }



        });


    }

    private void BorrarInput(){
        id.setText("");
        nombre.setText("");
        comentario.setText("");
        fecha.setText("");
    }

}
