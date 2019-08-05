package com.example.app_licorera_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListarActivity extends AppCompatActivity {

    List<Model> modelList = new ArrayList<>();
    RecyclerView mRecyclerView;
    //layout manager for recyclerview
    RecyclerView.LayoutManager layoutManager;

    Button readBtn;

    FloatingActionButton mAddBtn;

    //firestore instance
    FirebaseFirestore db;

    CustomAdapter adapter;

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar);

        //init firestore
        db = FirebaseFirestore.getInstance();

        //initialize views
        mRecyclerView = findViewById(R.id.recycler_view);
        mAddBtn = findViewById(R.id.addBtn);

        //set recycler view properties
        mRecyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        //init progress dialog
        pd = new ProgressDialog(this);

        //show dats in recyclerView
        showData();

        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ListarActivity.this, MainActivity.class));
                finish();
            }
        });

    }

    private void showData() {
        //set title of progress dialog
        pd.setTitle("Cargando datos...");
        pd.show();
        db.collection("licorera")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        //called when data is retrieved
                        modelList.clear();
                        pd.dismiss();
                        for (DocumentSnapshot doc: Objects.requireNonNull(task.getResult())){
                            Model model = new Model(doc.getString("codProduct"),
                                    doc.getString("nombre"),
                                    doc.getString("descripcion"),
                                    doc.getString("precio"));

                            modelList.add(model);
                        }
                        //adapter
                        adapter = new CustomAdapter(ListarActivity.this, modelList);

                        //set adapter to recyclerview
                        mRecyclerView.setAdapter(adapter);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //called when there is any error  while retireving
                Toast.makeText(ListarActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void deleteData(int index){
        //set title of progress dialog
        pd.setTitle("Borrando datos...");
        pd.show();
        db.collection("licorera").document(modelList.get(index).getCodProduct())
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(ListarActivity.this, "Borrado", Toast.LENGTH_SHORT).show();
                        //update data
                        showData();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(ListarActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
