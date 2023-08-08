package com.ap;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.BDD.BD;
import com.BDD.Students;
import com.ap.databinding.FragmentFirstBinding;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private RecyclerView Datos;
    private Button btnImportar;
    List<Students> listaI = new ArrayList<>();
    Adaptador adaptador;

    private ActivityResultLauncher<String> getContent = registerForActivityResult(
            new ActivityResultContracts.GetContent(),uri->{
                if(uri!=null){
                    //Leer y almacenar datos
                    readAndInsert(uri);
                }
            }
    );

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Build();
        btnImportar.setOnClickListener(v -> {
            getContent.launch("text/csv");
        });


        /*binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });*/
    }

    private void Build(){
        Datos.findViewById(R.id.Datos);
        btnImportar.findViewById(R.id.btnImportar);
        Datos.setLayoutManager(new GridLayoutManager(getActivity(),1));
    }

    private void readAndInsert(Uri fileUri){
        try{
            DocumentFile documentFile = DocumentFile.fromSingleUri(requireActivity(),fileUri);
            if(documentFile != null && documentFile.exists()){
                InputStream inputStream = requireActivity().getContentResolver().openInputStream(fileUri);
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                List<String[]> dataList = new ArrayList<>();
                while ((line = reader.readLine()) != null) {
                    String[] row = line.split(",");
                    dataList.add(row);
                }
                reader.close();
                insertDataIntoDatabase(dataList);
                Toast.makeText(getActivity(), "Datos importados exitosamente", Toast.LENGTH_SHORT).show();
            } else {
                // Manejar caso si el archivo no existe
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Manejar errores
        }
    }

    private void insertDataIntoDatabase(List<String[]> dataList) {
        BD databaseHelper = new BD(requireContext());
        for (String[] row : dataList) {
            // Supongamos que row[0] es el ID, row[1] es el nombre
            String id = row[0];
            String name = row[1];
            String image = "0000";
            String teamId = "00";
            // Insertar datos en la tabla 'Students'
            databaseHelper.Add(id, name, image, teamId);
            databaseHelper.close();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}