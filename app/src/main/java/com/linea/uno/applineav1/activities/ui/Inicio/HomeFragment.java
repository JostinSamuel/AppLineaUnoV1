package com.linea.uno.applineav1.activities.ui.Inicio;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.linea.uno.applineav1.R;
import com.linea.uno.applineav1.adapter.MovimientoAdapter;
import com.linea.uno.applineav1.entities.Movimiento;
import com.linea.uno.applineav1.viewmodel.MovimientoViewModel;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView rvhistorialMovimientos;
    private List<Movimiento> listaMovimientos= new ArrayList<>();
    private MovimientoAdapter movimientoAdapter;
    private MovimientoViewModel movimientoViewModel;
    SharedPreferences pref;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        return inflater.inflate(R.layout.fragment_home,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializar(view);
        inicializarAdapter();
        cargarDatos();
    }

    private void inicializar(View view) {
        ViewModelProvider modelProvider = new ViewModelProvider(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.HORIZONTAL, false);
        rvhistorialMovimientos = view.findViewById(R.id.rvHistorialMovimientos);
        rvhistorialMovimientos.setLayoutManager(gridLayoutManager);
        movimientoViewModel = modelProvider.get(MovimientoViewModel.class);
    }

    private void cargarDatos() {
        //lista de productos
        String email = pref.getString("emailCliente",null);
        Log.e("email home: ",email);
        movimientoViewModel.getLastFiveMovements(email).observe(getViewLifecycleOwner(),response->{
            movimientoAdapter.updateItemsMovimientos(response.getBody());
            Log.e("response.getBody {}",response.getBody().toString());
        });
    }

    private void inicializarAdapter() {
        //adapter productos
        movimientoAdapter = new MovimientoAdapter(listaMovimientos);
        rvhistorialMovimientos.setAdapter(movimientoAdapter);
    }
}