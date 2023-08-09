package com.ap;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.BDD.BD;
import com.BDD.Students;
import com.BDD.StudentsAdapter;
import com.ap.databinding.FragmentFirstBinding;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private ActivityResultLauncher<Intent> selectImageLauncher;
    private Uri selectedImageUri;
    private RecyclerView Datos;
    ImageButton btni;
    private Button btnImportar,btnAceptar,btnCancelar;
    private EditText id, name;
    private List<String[]> dataList;
    private int currentStudentIndex = 0;
    private List<Students> studentsList = new ArrayList<>();
    private int x=0;

    private ActivityResultLauncher<String> getContent = registerForActivityResult(
            new ActivityResultContracts.GetContent(),uri->{
                if(uri!=null){
                    x=1;
                    SetVisibility();
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
        Build(view);
        btnImportar.setOnClickListener(v -> {
            if(btnImportar.getText()=="Importar"){
                getContent.launch("text/csv");
            }else{
                x=0;
                SetVisibility();
                dataList=new ArrayList<>();
                Confirm();
            }
        });
        btnAceptar.setOnClickListener(v -> {
            if(btnAceptar.getText().equals("Aceptar")){
                currentStudentIndex = 0; // Restablece el índice al primer estudiante
                x = 4;
                SetVisibility();
                showStudentData();
            }else{
                if(btnAceptar.getText().equals("Siguiente")){
                    currentStudentIndex++;
                    insertDataIntoDatabase();
                    if (currentStudentIndex < studentsList.size()-1) {
                        if(currentStudentIndex==1){
                            x=2;
                            SetVisibility();
                        }
                        showStudentData();
                    } else {
                        showStudentData();
                        x = 3;
                        SetVisibility();
                    }
                }else{
                    NavHostFragment.findNavController(FirstFragment.this)
                            .navigate(R.id.action_FirstFragment_to_SecondFragment);
                }
            }
        });
        btnCancelar.setOnClickListener(v->{
            if(btnCancelar.getText().equals("Cancelar")){
                x=0;
                SetVisibility();
                dataList=new ArrayList<>();
            }else if(btnCancelar.getText().equals("Anterior")){
                currentStudentIndex--;
                if (currentStudentIndex >= 0) {
                    showStudentData();
                }else{
                    x=4;
                    SetVisibility();
                }
            }
        });
        btni.setOnClickListener(v-> selectImage());
        selectImageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == RESULT_OK){
                        Intent data = result.getData();
                        if (data != null){
                            Uri imageUri = data.getData();
                            showSelectedImage(imageUri);
                        }
                    }
                });
    }

    private void Build(View view){
        Datos=view.findViewById(R.id.Datos);
        btnImportar=view.findViewById(R.id.btnImportar);
        btnAceptar=view.findViewById(R.id.btnAceptar);
        btnCancelar=view.findViewById(R.id.btnCancelar);
        btni=view.findViewById(R.id.btnImage);
        id=view.findViewById(R.id.edtxt1);
        name=view.findViewById(R.id.edtxt2);
        SetVisibility();
    }

    private void SetVisibility(){
        ConstraintLayout.LayoutParams layoutParamsI = (ConstraintLayout.LayoutParams) btnImportar.getLayoutParams();
        ConstraintLayout.LayoutParams layoutParamsA = (ConstraintLayout.LayoutParams) btnAceptar.getLayoutParams();
        ConstraintLayout.LayoutParams layoutParamsC = (ConstraintLayout.LayoutParams) btnCancelar.getLayoutParams();
        switch (x){
            case 0:
                btnAceptar.setVisibility(View.GONE);
                btnCancelar.setVisibility(View.GONE);
                btni.setVisibility(View.GONE);
                id.setVisibility(View.GONE);
                name.setVisibility(View.GONE);
                btnImportar.setVisibility(View.VISIBLE);
                Datos.setVisibility(View.VISIBLE);
                id.setText("");
                name.setText("");
                btnImportar.setText("Importar");
                layoutParamsA.topToBottom = R.id.Datos;
                btnImportar.setLayoutParams(layoutParamsI);
                layoutParamsC.topToBottom = R.id.Datos;
                btnImportar.setLayoutParams(layoutParamsI);
                layoutParamsI.topToBottom = R.id.Datos;
                btnImportar.setLayoutParams(layoutParamsI);
                break;
            case 1:
                btnAceptar.setVisibility(View.VISIBLE);
                btnCancelar.setVisibility(View.VISIBLE);
                btni.setVisibility(View.GONE);
                Datos.setVisibility(View.VISIBLE);
                id.setVisibility(View.GONE);
                name.setVisibility(View.GONE);
                btnImportar.setVisibility(View.GONE);
                id.setText("");
                name.setText("");
                btnAceptar.setText("Aceptar");
                btnCancelar.setText("Cancelar");
                break;
            case 2:
                btnAceptar.setVisibility(View.VISIBLE);
                btnCancelar.setVisibility(View.VISIBLE);
                btni.setVisibility(View.VISIBLE);
                id.setVisibility(View.VISIBLE);
                name.setVisibility(View.VISIBLE);
                btnImportar.setVisibility(View.VISIBLE);
                Datos.setVisibility(View.GONE);
                id.setText("");
                name.setText("");
                btnAceptar.setText("Siguiente");
                btnCancelar.setText("Anterior");
                btnImportar.setText("Cancelar");
                layoutParamsA.topToBottom = R.id.edtxt2;
                btnImportar.setLayoutParams(layoutParamsI);
                layoutParamsC.topToBottom = R.id.edtxt2;
                btnImportar.setLayoutParams(layoutParamsI);
                layoutParamsI.topToBottom = R.id.edtxt2;
                btnImportar.setLayoutParams(layoutParamsI);
                break;
            case 3:
                btnAceptar.setVisibility(View.VISIBLE);
                btnCancelar.setVisibility(View.VISIBLE);
                id.setVisibility(View.VISIBLE);
                name.setVisibility(View.VISIBLE);
                btni.setVisibility(View.VISIBLE);
                btnImportar.setVisibility(View.VISIBLE);
                Datos.setVisibility(View.GONE);
                id.setText("");
                name.setText("");
                btnAceptar.setText("Terminar");
                btnCancelar.setText("Anterior");
                btnImportar.setText("Cancelar");
                layoutParamsA.topToBottom = R.id.edtxt2;
                btnImportar.setLayoutParams(layoutParamsI);
                layoutParamsC.topToBottom = R.id.edtxt2;
                btnImportar.setLayoutParams(layoutParamsI);
                layoutParamsI.topToBottom = R.id.edtxt2;
                btnImportar.setLayoutParams(layoutParamsI);
                break;
            case 4:
                btnAceptar.setVisibility(View.VISIBLE);
                btnCancelar.setVisibility(View.GONE);
                btni.setVisibility(View.VISIBLE);
                id.setVisibility(View.VISIBLE);
                name.setVisibility(View.VISIBLE);
                btnImportar.setVisibility(View.VISIBLE);
                Datos.setVisibility(View.GONE);
                id.setText("");
                name.setText("");
                btnAceptar.setText("Siguiente");
                btnCancelar.setText("Anterior");
                btnImportar.setText("Cancelar");
                layoutParamsA.topToBottom = R.id.edtxt2;
                btnImportar.setLayoutParams(layoutParamsI);
                layoutParamsC.topToBottom = R.id.edtxt2;
                btnImportar.setLayoutParams(layoutParamsI);
                layoutParamsI.topToBottom = R.id.edtxt2;
                btnImportar.setLayoutParams(layoutParamsI);
        }
    }

    private void readAndInsert(Uri fileUri){
        try{
            DocumentFile documentFile = DocumentFile.fromSingleUri(requireActivity(),fileUri);
            if(documentFile != null && documentFile.exists()){
                InputStream inputStream = requireActivity().getContentResolver().openInputStream(fileUri);
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                dataList = new ArrayList<>();
                while ((line = reader.readLine()) != null) {
                    String[] row = line.split(",");
                    dataList.add(row);
                }
                reader.close();
                Confirm();
                //Toast.makeText(getActivity(), "Datos importados exitosamente", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "El archivo no existe", Toast.LENGTH_SHORT).show();
                x=0;
                SetVisibility();
            }
        } catch (IOException e) {
            e.printStackTrace();
            x=0;
            SetVisibility();
            Toast.makeText(getActivity(), "Ocurrio un error, intentalo nuevamente", Toast.LENGTH_SHORT).show();
        }
    }

    private void Confirm(){
        studentsList.clear();
        for (String[] row : dataList) {
            String id = row[0];
            String name = row[1];
            String image = "0000";
            String teamId = "00";
            studentsList.add(new Students(id, name, image, teamId));
        }
        StudentsAdapter studentsAdapter = new StudentsAdapter(studentsList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        Datos.setLayoutManager(layoutManager);
        Datos.setAdapter(studentsAdapter);
    }

    private void showStudentData() {
        if (currentStudentIndex >= 0 && currentStudentIndex < studentsList.size()) {
            Students student = studentsList.get(currentStudentIndex);
            id.setText(student.getID());
            name.setText(student.getName());
        }
    }

    private void showSelectedImage(Uri imageUri){
        selectedImageUri = imageUri;
        btni.setImageURI(imageUri);
    }

    private void selectImage(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        selectImageLauncher.launch(intent);
    }

    private void insertDataIntoDatabase() {
        String updateId = id.getText().toString();
        String updatedName = name.getText().toString();
        String image = String.valueOf(selectedImageUri);
        String teamId = "00";
        BD databaseHelper = new BD(requireContext());
        databaseHelper.Add(updateId, updatedName,image,teamId);
        databaseHelper.close();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}