package com.example.comentariorelato_movil;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class iniciosession extends AppCompatActivity {
    Button btncreatecount , btnsesion ;
    TextInputEditText InputpasS, inputcorreo;

    private DatabaseReference database;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_iniciosesion);

        mAuth = FirebaseAuth.getInstance();

//        #buttones
        btncreatecount = (Button) findViewById(R.id.btn_createcount);
        btnsesion = (Button) findViewById(R.id.btn_sesion);

//      #edittext
        inputcorreo = (TextInputEditText) findViewById(R.id.input_correo);
        InputpasS = (TextInputEditText) findViewById(R.id.input_pass);

        TextInputLayout textConfirmcorreo = findViewById(R.id.textinputcorreo);
        TextInputLayout textConfirmPass = findViewById(R.id.textinpoutpass);
        //        _____________botones de navegacion:_______________________
//        #re-dirrecionar a registrar
        btncreatecount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(iniciosession.this, registro.class));
            }
        });


        btnsesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myusers = database.getReference("Usuarios");

                try {

                     if (!inputcorreo.getText().toString().isEmpty() && !InputpasS.getText().toString().isEmpty()){

                         if(ValidateEmail() && ValidatePassword()) {
                               myusers.addListenerForSingleValueEvent(new ValueEventListener() {
                               @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                        if (inputcorreo.getText().toString().equals(snapshot.child("email").getValue()) && InputpasS.getText().toString().equals(snapshot.child("password").getValue())) {

                                        CharSequence text = "Usuario Registrado, successful";
                                        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                        limpiarInput();
                                         return;
                                   }

                                 }
                                   textConfirmcorreo.setError(getString(R.string.error_correo_login));
                                   textConfirmPass.setError(getString(R.string.error_pass_login));
                                   CharSequence text = "Usuario o contraseña incorrecta.";
                                     Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();

                            }
                                @Override
                                  public void onCancelled(@NonNull DatabaseError error) {
                            }
                         });
                      }
                    }
                }
                catch (Exception e){
                    System.out.print(e.getMessage());
                }
            }
        });


    }

    public Boolean ValidateEmail(){
        String ValEmail=inputcorreo.getText().toString();
        if (ValEmail.isEmpty()){
            inputcorreo.setError("El Correo Electronico no puede estar vacío.");
            return false;
        }else{
            inputcorreo.setError(null);
            return true;
        }
    }
    public Boolean ValidatePassword(){
        String ValPassword=InputpasS.getText().toString();
        if (ValPassword.isEmpty()){
            InputpasS.setError("El Contraseña no puede estar vacía.");
            return false;
        }else{
            InputpasS.setError(null);
            return true;
        }
    }

    private void limpiarInput(){
        inputcorreo.setText("");
        InputpasS.setText("");
    }
}
