package com.example.app_licorera_firebase;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<ViewHolder> {
    ListarActivity listarActivity;
    List<Model> modelList;

    //Custom Adapter

    public CustomAdapter(ListarActivity listarActivity, List<Model> modelList) {
        this.listarActivity = listarActivity;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        View itemView = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.model_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(itemView);

        //handle item clicks here
        viewHolder.setOnClickListener(new ViewHolder.ClickLlistener() {
            @Override
            public void onItemClick(View view, int position) {
                //this will be called when user click item

                //show data is toast on clicking
                String nombre = modelList.get(position).getNombre();
                String descripcion = modelList.get(position).getDescripcion();
                String precio = modelList.get(position).getPrecio();
                Toast.makeText(listarActivity, "Nombre" + nombre, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, final int position) {
                //this will be called when user long click item

                //creating alertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(listarActivity);

                //options to display in dialog
                String[] options = {"Actualizar", "Delete"};

                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0){
                            //update is clicked
                            //get data
                            String codProduct = modelList.get(position).getCodProduct();
                            String nombre = modelList.get(position).getNombre();
                            String descripcion = modelList.get(position).getDescripcion();
                            String precio = modelList.get(position).getPrecio();

                            //intent to start activity
                            Intent intent = new Intent(listarActivity, MainActivity.class);

                            //put data in intent
                            intent.putExtra("pId", codProduct);
                            intent.putExtra("pNombre", nombre);
                            intent.putExtra("pDescripcion", descripcion);
                            intent.putExtra("pPrecio", precio);

                            //start activity
                            listarActivity.startActivity(intent);
                        }

                        if(which == 1){
                            //delete is clicked
                            listarActivity.deleteData(position);
                        }

                    }
                }).create().show();
            }
        });

       return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //bind views / set data
        holder.mNombre.setText(modelList.get(position).getNombre());
        holder.mDescripcion.setText(modelList.get(position).getDescripcion());
        holder.mPrecio.setText(modelList.get(position).getPrecio());
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }
}

