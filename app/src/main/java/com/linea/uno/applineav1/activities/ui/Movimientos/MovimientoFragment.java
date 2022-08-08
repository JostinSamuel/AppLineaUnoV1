package com.linea.uno.applineav1.activities.ui.Movimientos;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.linea.uno.applineav1.R;
import com.linea.uno.applineav1.viewmodel.MovimientoViewModel;

import java.io.File;
import java.io.FileOutputStream;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MovimientoFragment extends Fragment {

    private ActivityResultLauncher<String> perReqLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
        if (result) {
            successMessage("Gracias por concedernos el permiso, oprime el boton nuevamente");
        } else {
            errorMessage("Permiso denegado, no podemos continuar");
        }
    });

    Button btnDescargar;

    private MovimientoViewModel movimientoViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_movimiento,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        initViewModel();
    }

    private void initViewModel() {
        movimientoViewModel = new ViewModelProvider(this).get(MovimientoViewModel.class);
    }

    private void init(View view) {

        btnDescargar = view.findViewById(R.id.btn_descargarPDF);

        btnDescargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exportInvoice();
            }
        });
    }


    public void exportInvoice() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
            movimientoViewModel.exportInvoice().observe(getViewLifecycleOwner(), response -> {
                if (response.getRpta() == 1) {
                    try {
                        boolean folderCreated = true;
                        File path = requireContext().getExternalFilesDir("/recargas");
                        if (!path.exists()) {
                            folderCreated = false;
                            Toast.makeText(requireContext(), "No se pudo crear la carpeta para guardar los archivos.", Toast.LENGTH_LONG);
                        }
                        if (folderCreated) {
                            byte[] bytes = response.getBody().bytes();
                            File file = new File(path, "Historial.pdf");
                            FileOutputStream fileOutputStream = new FileOutputStream(file);
                            fileOutputStream.write(bytes);
                            fileOutputStream.close();
                            successMessage("PDF descargado!");
                            //PREVISUALIZAR EL PDF EN EL APP
                            /*new MaterialAlertDialogBuilder(requireContext()).setTitle("Exportar Detalle")
                                    .setMessage("Detalle guardado correctamente en la siguiente ubicación: "
                                            + file.getAbsolutePath() + " ¿Deseas visualizarlo ahora?")
                                    .setCancelable(false)
                                    .setPositiveButton("Sí", (dialog, i) -> {
                                        dialog.dismiss();
                                        Intent intent = new Intent(requireContext(), VerInvoiceActivity.class);
                                        intent.putExtra("pdf", bytes);
                                        startActivity(intent);
                                    }).setNegativeButton("Quizas más tarde", (dialogInterface, i) -> {
                                        dialogInterface.dismiss();
                                    }).show();*/
                        }
                    } catch (Exception e) {
                        errorMessage("No se pudo guardar el archivo en el dispositivo");
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(requireContext(), response.getMessage(), Toast.LENGTH_LONG);
                }
            });
        } else {
            new SweetAlertDialog(requireContext(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Concedenos el permiso")
                    .setContentText("Debido a que vamos a descargar un archivo, es necesario tener acceso al almacenamiento interno")
                    .setConfirmButton("Vale", sweetAlertDialog -> {
                        sweetAlertDialog.dismissWithAnimation();
                        perReqLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    }).setCancelButton("Quizas mas tarde", SweetAlertDialog::dismissWithAnimation).show();
        }
    }

    public void successMessage(String message) {
        new SweetAlertDialog(requireContext(),
                SweetAlertDialog.SUCCESS_TYPE).setTitleText("Buen Trabajo!")
                .setContentText(message).show();
    }

    public void errorMessage(String message) {
        new SweetAlertDialog(requireContext(),
                SweetAlertDialog.ERROR_TYPE).setTitleText("Oops...!")
                .setContentText(message).show();
    }
    
}