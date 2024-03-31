package com.example.comentariorelato_movil;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;

public class MainCamera extends AppCompatActivity {
    Button btn_Captura;
    ImageView imgForm;
    String rutaImg;
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_camera);

        btn_Captura =(Button) findViewById(R.id.captureButton);
        imgForm = (ImageView) findViewById(R.id.imageView);


        // Solicitar permiso de la cámara
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            setupCameraCapture();
        }

        btn_Captura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                File imgArchivo=null;
                Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                try {
                    imgArchivo=crearImagen();
                    Toast.makeText(MainCamera.this, "Imagen guardada correctamente", Toast.LENGTH_SHORT).show();

                } catch (IOException e) {
                    Toast.makeText(MainCamera.this, "Error al guardar la imagen", Toast.LENGTH_SHORT).show();
                    throw new RuntimeException(e);
                }
                if(imgArchivo!=null){
                    Uri fotoUri = FileProvider.getUriForFile(MainCamera.this,"com.example.comentariorelato_movil.fileprovider",imgArchivo);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,fotoUri);
                }

                else {
                    // Muestra un mensaje de error si el almacenamiento externo no está disponible para escribir
                    Toast.makeText(MainCamera.this, "El almacenamiento externo no está disponible", Toast.LENGTH_SHORT).show();
                }
                cameraLan.launch(intent);


            }
        });
    }
    private void setupCameraCapture() {
        // Realizar configuración de la cámara si es necesario
        btn_Captura.setEnabled(true);
    }

    ActivityResultLauncher<Intent> cameraLan=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode()==RESULT_OK){
                Bitmap image= BitmapFactory.decodeFile(rutaImg);
                imgForm.setImageBitmap(image);
                Toast.makeText(MainCamera.this, "Guardado correctamente", Toast.LENGTH_SHORT).show();

                // Volver a cargar la vista de la cámara
                recreate();
            }
        }
    });

    private File crearImagen() throws IOException {
        String nombreImagen="foto_";
        File carpeta=getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File img=File.createTempFile(nombreImagen,".jpg",carpeta);
        rutaImg=img.getAbsolutePath();
        return img;
    }

    ActivityResultLauncher<Intent> LanzaCamara= registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK){
                Bundle extras = result.getData().getExtras();
                Bitmap imagen= (Bitmap) extras.get("data");
                imgForm.setImageBitmap(imagen);
            }
        }
    });
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupCameraCapture();
            } else {
                Toast.makeText(MainCamera.this, "usuario denegó el permiso de la cámara", Toast.LENGTH_SHORT).show();
            }
            }
        }
}