package com.example.app_licorera_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    EditText etDescripcion, etPrecio, etNombre;
    Button mSaveBtn, mReadBtn;
    ProgressDialog pd;
    //ProgressBar
    FirebaseFirestore db ;

    String pId, pNombre, pDescripcion, pPrecio;

    //Actividad principal.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();
        etDescripcion = findViewById(R.id.etDescripcion);
        etPrecio = findViewById(R.id.etPrecio);
        etNombre = findViewById(R.id.etNombre);
        mSaveBtn = findViewById(R.id.btnCrear);
        mReadBtn = findViewById(R.id.btnLeer);
        pd = new ProgressDialog(this);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            //update data
            mSaveBtn.setText("Actualizar");
            //get data
            pId = bundle.getString("pId");
            pNombre = bundle.getString("pNombre");
            pDescripcion = bundle.getString("pDescripcion");
            pPrecio = bundle.getString("pPrecio");
            //set data
            etNombre.setText(pNombre);
            etDescripcion.setText(pDescripcion);
            etPrecio.setText(pPrecio);
        } else {
            //new data
            mSaveBtn.setText("Guardar");
        }

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codProduct = pId;
                //String precio = pPrecio;
                String nombre = etNombre.getText().toString().trim();
                String descripcion = etDescripcion.getText().toString().trim();
                String precio = etPrecio.getText().toString().trim();

                validarIngreso(nombre, descripcion);

                //update
                Bundle bundle1 = getIntent().getExtras();
                if(bundle1 != null){
                    //updating
                    updateData(codProduct, nombre, descripcion, precio);
                } else {
                    //arreglar actualizar precio.
                    //adding new
                    insertarData(nombre, descripcion, precio);
                }
            }
        });

        mReadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ListarActivity.class));
                finish();
            }
        });
    }

    private void updateData(String codProduct, String nombre, String descripcion, String precio) {
        pd.setTitle("Actualizando datos...");
        pd.show();

        db.collection("licorera").document(codProduct)
                .update("nombre", nombre, "descripcion",descripcion, "precio", precio)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //called when updated successfully
                        pd.dismiss();
                        Toast.makeText(MainActivity.this, "Actualizado", Toast.LENGTH_SHORT).show();
                        limpiarCampos();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(MainActivity.this, "Error al actualizar, verifica", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void validarIngreso(String descripcion, String nombre){
        if(descripcion.isEmpty() || nombre.isEmpty() || etPrecio.getText().toString().isEmpty()) {
            Toast.makeText(this, "Campos obligatorios ", Toast.LENGTH_SHORT).show();
        }
    }

    private void insertarData(String nombre, String descripcion, String precio){
        pd.setTitle("Agregando datos");
        pd.show();
        String codProduct = UUID.randomUUID().toString();
        try {
            //int precio = Integer.parseInt(etPrecio.getText().toString().trim());
            Map<String, Object> user = new HashMap<>();
            user.put("codProduct", codProduct);
            user.put("nombre", nombre);
            user.put("descripcion", descripcion);
            user.put("precio", precio);

            db.collection("licorera")
                .document(codProduct).set(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        pd.dismiss();
                        Toast.makeText(MainActivity.this, "Agregado", Toast.LENGTH_SHORT).show();
                        limpiarCampos();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pd.dismiss();
                    Toast.makeText(MainActivity.this, "Error al ingresar, verifica", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "Campo númerico...", Toast.LENGTH_SHORT).show();
        }
    }

    private void limpiarCampos(){
        etNombre.setText("");
        etDescripcion.setText("");
        etPrecio.setText("");
    }

}
