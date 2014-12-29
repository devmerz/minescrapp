package com.devmerz.minescrapp;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.devmerz.adapters.ElementosAdapter;
import com.devmerz.http.HttpRequest;
import com.devmerz.http.HttpRequest.HttpRequestException;

import com.devmerz.sqlite.ElementsMinecraft;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Random;


public class MainActivity extends Activity {

    public static final String TAG = "com.devmerz.minescrapp";
    public static final String urlImage = "http://minecraftwiki.es";

    EditText buscador;
    ListView lista;
    TextView texto;

    private ImageView ivPoster;
    private TextView tvTitle, tvWritters, tvActors, tvPlot;

    ProgressDialog loading;
    ArrayList<String> a = new ArrayList<String>();
    ArrayList<Elements> datos = new ArrayList<Elements>();
    ArrayList<Elements> resFound = new ArrayList<Elements>();

    ElementsMinecraft info;

    ElementosAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        buscador = (EditText) findViewById(R.id.buscadorET);
        lista = (ListView)findViewById(R.id.listaLV);
        texto = (TextView)findViewById(R.id.textoTV);

        // List all element if exists
        info = new ElementsMinecraft(this);
        info.abrir();
        datos = info.ListAll();
        info.cerrar();

        lista.setAdapter(new ElementosAdapter(this, datos));
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView adapterView, View view, int i, long l) {

                Dialog d = new Dialog(MainActivity.this);

                d.setTitle(datos.get(i).getNombre());
                ImageView im = new ImageView(MainActivity.this);


                //Rescue image from sd card
                File filepath = Environment.getExternalStorageDirectory();
                String path = filepath.getAbsolutePath() + "/Minecrapp/"+datos.get(i).getNombre().trim()+".png";
                Bitmap b = BitmapFactory.decodeFile(path);
                im.setImageBitmap(b);

                d.setContentView(im);
                d.getWindow().setLayout(550 , 540);
                d.show();
                Toast.makeText(MainActivity.this , datos.get(i).getIngredientes() , Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void searchTool(View view){


        String foo  = buscador.getText().toString();
        if (foo.equals("consumir")){
            loading  = ProgressDialog.show(MainActivity.this , "Scrapping a Minecraft XD" , " Espere por favor ..." , true);
            String url = String.format("http://minescrapp.16mb.com/" , foo);
            new LoadToolsTask().execute(url);
        }else{
            resFound =  new ArrayList<Elements>();

            ElementsMinecraft search = new ElementsMinecraft(this);
            search.abrir();
            resFound = search.findMyWord(foo);
            lista.setAdapter(new ElementosAdapter(this, resFound));
            search.cerrar();

            lista.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                @Override
                public void onItemClick(AdapterView adapterView, View view, int i, long l) {
                    TextView x = (TextView) view.findViewById(R.id.texto_superior);
                    x.getText();

                    Dialog d = new Dialog(MainActivity.this);
                    d.setTitle(resFound.get(i).getNombre());
                    ImageView im = new ImageView(MainActivity.this);

                    //Rescue image from sd card
                    File filepath = Environment.getExternalStorageDirectory();
                    String path = filepath.getAbsolutePath() + "/Minecrapp/"+resFound.get(i).getNombre().trim()+".png";
                    Bitmap b = BitmapFactory.decodeFile(path);
                    im.setImageBitmap(b);
                    //
                    d.setContentView(im);
                    d.getWindow().setLayout(550 , 540);
                    d.show();

                    Toast.makeText(MainActivity.this , resFound.get(i).getIngredientes() , Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    private class LoadToolsTask extends AsyncTask<String , Long ,String>{

        protected String doInBackground(String... urls){
            try {


                String response = HttpRequest.get(urls[0]).accept("application/json").body();

                //
                try {
                    JSONObject elementosMinecraft = new JSONObject(response);
                    JSONArray dataJsonArr = elementosMinecraft.getJSONArray("elementos");

                    for (int i = 0; i < dataJsonArr.length(); i++) {
                        JSONObject c = dataJsonArr.getJSONObject(i);

                        try {
                            URL url = new URL("http://minecraftwiki.es"+c.getString("imagen"));
                            URLConnection urlCon = url.openConnection();

                            File filepath = Environment.getExternalStorageDirectory();


                            File dir = new File(filepath.getAbsolutePath() + "/Minecrapp/");
                            dir.mkdirs();


                            File file = new File(dir, c.getString("nombre").trim()+".png");

                            InputStream is = urlCon.getInputStream();
                            FileOutputStream fos = new FileOutputStream(file);


                            byte[] array = new byte[1000];
                            int leido = is.read(array);
                            while (leido > 0) {
                                fos.write(array, 0, leido);
                                leido = is.read(array);
                            }

                            is.close();
                            fos.close();
                        }
                        catch (IOException a){

                        }
                    }

                }
                catch(Exception e){

                }
                //
                return HttpRequest.get(urls[0]).accept("application/json").body();
            } catch (HttpRequestException exception) {
                return null;
            }
        }
        protected void onPostExecute(String response) {

            loading.cancel();

            try {

                JSONObject elementosMinecraft = new JSONObject(response);
                JSONArray dataJsonArr = elementosMinecraft.getJSONArray("elementos");

                ElementsMinecraft entrada = new ElementsMinecraft(MainActivity.this);
                entrada.abrir();

                for (int i = 0; i < dataJsonArr.length(); i++) {
                    JSONObject c = dataJsonArr.getJSONObject(i);
                    entrada.newElement(c.getString("ingredientes"), c.getString("imagen"), c.getString("descripcion"), c.getString("nombre"));
                }
                entrada.cerrar();


                info = new ElementsMinecraft(MainActivity.this);
                info.abrir();
                datos = info.ListAll();
                info.cerrar();

                lista.setAdapter(new ElementosAdapter(MainActivity.this, datos));
                lista.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                    @Override
                    public void onItemClick(AdapterView adapterView, View view, int i, long l) {

                        Dialog d = new Dialog(MainActivity.this);
                        d.setTitle(datos.get(i).getNombre());
                        ImageView im = new ImageView(MainActivity.this);

                         //Rescue image from sd card
                        File filepath = Environment.getExternalStorageDirectory();
                        String path = filepath.getAbsolutePath() + "/Minecrapp/"+datos.get(i).getNombre().trim()+".png";
                        Bitmap b = BitmapFactory.decodeFile(path);
                        im.setImageBitmap(b);
                        //
                        d.setContentView(im);
                        d.show();
                    }
                });

            }
            catch (Exception ex){
                ex.printStackTrace();
            }

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.about) {
            Toast.makeText(this , "@devmerz" , Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
