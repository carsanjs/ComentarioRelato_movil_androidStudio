package com.example.comentariorelato_movil;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class registro extends AppCompatActivity {


    Button button_register,button_login,button_Cancel;
    TextInputEditText  txt_documento,txt_name , txt_user ,txt_correoelectronico,txt_password, repet_txtPassword;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_registro);


//        #buttones
        button_Cancel = (Button) findViewById(R.id.btn_cancel);
        button_register = (Button) findViewById(R.id.btn_register);
        button_login = (Button) findViewById(R.id.btn_login);

        // Edittext
        txt_documento = (TextInputEditText) findViewById(R.id.txt_document);
        txt_name = (TextInputEditText) findViewById(R.id.txtname);
        txt_user = (TextInputEditText) findViewById(R.id.txtuser);
        txt_correoelectronico = (TextInputEditText) findViewById(R.id.txtcorreo);
        txt_password = (TextInputEditText) findViewById(R.id.txtpass);
        repet_txtPassword = (TextInputEditText) findViewById(R.id.txtrepetpass);

        TextInputLayout textInputLayoutConfirmPassword = findViewById(R.id.textInputLayoutConfirmPassword);

        this.button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(registro.this, iniciosession.class));
            }
        });

        this.button_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        this.button_register.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Usuarios");


                if (txt_documento.getText().toString().isEmpty() || txt_name.getText().toString().isEmpty() || txt_user.getText().toString().isEmpty() || txt_correoelectronico.getText().toString().isEmpty()  ||    txt_password.getText().toString().isEmpty() || repet_txtPassword.getText().toString().isEmpty()) {

                    String text = "Algunos campos están vacíos o son incorrectos.";
                    Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                }
                else{
                    if(ConfirmarPass(txt_password.getText().toString(), repet_txtPassword.getText().toString())) {
                        Map<String, Object> map = new HashMap<>();

                        map.put("id", txt_documento.getText().toString());
                        map.put("name", txt_name.getText().toString());
                        map.put("usuario", txt_user.getText().toString());
                        map.put("email", txt_correoelectronico.getText().toString());
                        map.put("password", txt_password.getText().toString());
                        map.put("repet_password",repet_txtPassword.getText().toString());
                        myRef.child(txt_documento.getText().toString()).updateChildren(map);

                               AlertDialog.Builder confirm = new AlertDialog.Builder(registro.this);
                               confirm.setTitle("Comprobación");
                               confirm.setMessage("¿Desea Agregar a un nuevo usuario?");
                               confirm.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                              @Override
                              public void onClick(DialogInterface dialogInterface, int i) {
                                  CharSequence text = "Usuario a sido registrado con mucho éxito";
                                  Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                                  try { TimeUnit.SECONDS.sleep(1);}
                                  catch (InterruptedException e) {throw new RuntimeException(e);}
//                            #me lleva a inicio index
                                  Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                  startActivity(intent);
                              }
                          });
                          confirm.setNegativeButton("Denegar", null);
                          AlertDialog ventana = confirm.create();
                          ventana.show();
                          BorrarInput();
                    }
                      else {
                          // La contraseña y la confirmación de contraseña no coinciden
                          // Muestra un mensaje de error al usuario y permite que vuelva a ingresar las contraseñas
                          textInputLayoutConfirmPassword.setError(getString(R.string.error_password_mismatch));
                      }
                }

            }



        });

    }

    private boolean ConfirmarPass(String pass, String confirmPass) {
        return pass.equals(confirmPass);
    }
    private void BorrarInput(){
        txt_documento.setText("");
        txt_name.setText("");
        txt_user.setText("");
        txt_correoelectronico.setText("");
        txt_password.setText("");
        repet_txtPassword.setText("");
    }
}
