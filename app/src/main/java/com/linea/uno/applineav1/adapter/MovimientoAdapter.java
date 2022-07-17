package com.linea.uno.applineav1.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.linea.uno.applineav1.R;
import com.linea.uno.applineav1.entities.Movimiento;
import java.util.List;

//clase anidada
public class MovimientoAdapter extends RecyclerView.Adapter<MovimientoAdapter.ViewHolder>{

    private List<Movimiento> listaMovimientos;
    private Context context;

    public MovimientoAdapter(List<Movimiento> listaMovimientos) {
        this.listaMovimientos = listaMovimientos;
    }

    @NonNull
    @Override
    public MovimientoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto,parent,false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovimientoAdapter.ViewHolder holder, int position) {
        holder.setItem(this.listaMovimientos.get(position));
    }

    @Override
    public int getItemCount() {
        return this.listaMovimientos.size();
    }

    public void updateItemsMovimientos(List<Movimiento> movimiento) {
        this.listaMovimientos.clear();
        this.listaMovimientos.addAll(movimiento);
        this.notifyDataSetChanged();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder{


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void setItem(final Movimiento movimiento){
            TextView tvmontoRecarga = itemView.findViewById(R.id.tvmontoRecarga);
            TextView tvFechaMovimiento = itemView.findViewById(R.id.tvFechaMovimiento);

            tvmontoRecarga.setText(movimiento.getMonto_total().toString());
            tvFechaMovimiento.setText(movimiento.getFecha().toString());
            Log.e("montoTotal: ",tvmontoRecarga.getText().toString()+"/"+movimiento.getMonto_total().toString());
            Log.e("fechaMovimiento: ",tvFechaMovimiento.getText().toString()+"/"+movimiento.getFecha().toString());
        }
    }
}
