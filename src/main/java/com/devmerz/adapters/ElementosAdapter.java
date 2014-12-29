package com.devmerz.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.devmerz.minescrapp.Elements;
import com.devmerz.minescrapp.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by @devmerz on 23/12/14.
 */
public class ElementosAdapter extends BaseAdapter {

    private List<Elements> listado;
    private Context context;
    private ArrayList<Elements> arraylist;

    public ElementosAdapter(Context context , List<Elements> listado){
        this.context = context;
        this.listado = listado;

        this.arraylist = new ArrayList<Elements>();
        this.arraylist.addAll(listado);
    }

    @Override
    public int getCount() { return this.listado.size(); }

    @Override
    public Object getItem(int arg0) { return listado.get(arg0); }

    @Override
    public long getItemId(int arg0) { return arg0; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        if (convertView == null) {
            // Create a new view into the list.
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.list_custom, parent, false);
        }


        TextView ts1 = (TextView) rowView.findViewById(R.id.texto_superior);
        TextView ti1 = (TextView) rowView.findViewById(R.id.texto_inferior);
        ImageView img = (ImageView) rowView.findViewById(R.id.imagencita);

        Elements item = this.listado.get(position);

        // SET IMAGE FROM SD
        File filepath = Environment.getExternalStorageDirectory();
        String path = filepath.getAbsolutePath() + "/Minecrapp/"+item.getNombre().trim()+".png";
        Bitmap b = BitmapFactory.decodeFile(path);


        /*
            Validacion por mala generacion y
            filtro del Scrapping XD
        */
        if(b != null){
            img.setImageBitmap(b);
            ts1.setText(item.getNombre());
            ti1.setText(item.getDescripcion());
        }
        else{
            img.setImageResource(R.drawable.ic_launcher);
            ts1.setText("");
            ti1.setText("");
        }


        return rowView;
    }


}
