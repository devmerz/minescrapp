package com.devmerz.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.devmerz.minescrapp.Elements;

import java.util.ArrayList;

/**
 * Created by @devmerz on 23/12/14.
 */
public class ElementsMinecraft {

    public static final String idDB = "id";
    public static final String ingredienteDB = "ingrediente";
    public static final String imagenDB = "imagen";
    public static final String descripcionDB = "descripcion";
    public static final String nombreDB = "nombre";


    private static final String DB = "MinescrappDB";
    private static final String Table = "ElementsMinescrapp";
    private static final int Version = 1;

    private BDHelper miHelper;
    private final Context miContexto;
    private SQLiteDatabase miBD;


    public ElementsMinecraft(Context c){
        miContexto = c;
    }



    private static class BDHelper extends SQLiteOpenHelper{

        public BDHelper(Context context) {
            super(context, DB, null, Version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE "+ Table +"("+
                        idDB + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                        ingredienteDB + " TEXT NOT NULL, " +
                        imagenDB + " TEXT NOT NULL, "+
                        descripcionDB + " TEXT NOT NULL, "+
                        nombreDB + " TEXT NOT NULL);"
                      );
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i2) {
            db.execSQL("DROP TABLE IF EXIST "+ Table);
            onCreate(db);
        }
    }

    public ElementsMinecraft abrir(){
        miHelper = new BDHelper(miContexto);
        miBD = miHelper.getWritableDatabase();
        return this;
    }

    public void cerrar() {
        miHelper.close();
    }
    public long newElement(String ingredientes, String imagen, String descripcion, String nombre) {
        ContentValues cv = new ContentValues();
        cv.put(ingredienteDB ,ingredientes );
        cv.put(imagenDB , imagen);
        cv.put(descripcionDB , descripcion);
        cv.put(nombreDB , nombre);
        return  miBD.insert(Table , null , cv);
    }

    public ArrayList<Elements> ListAll() {
        String[] columns = new String[]{idDB , ingredienteDB , imagenDB , descripcionDB , nombreDB};
        Cursor c = miBD.query(Table , columns ,null , null , null , null , null);
        String res = "";
        int iFila = c.getColumnIndex(idDB);
        int iIngredientes= c.getColumnIndex(ingredienteDB);
        int iImagen = c.getColumnIndex(imagenDB);
        int iDescripcion = c.getColumnIndex(descripcionDB);
        int iNombre = c.getColumnIndex(nombreDB);

        ArrayList<Elements> elem = new ArrayList<Elements>();


        for (c.moveToFirst(); !c.isAfterLast();c.moveToNext()){

            Elements elemento =  new Elements();
            //res = res + c.getString(iFila) + " "+  +" "++" "++"";
            elemento.setIngredientes(c.getString(iIngredientes));
            elemento.setImagen(c.getString(iImagen));
            elemento.setDescripcion(c.getString(iDescripcion));
            elemento.setNombre(c.getString(iNombre));
            elem.add(elemento);

        }
        return elem;
    }

    public ArrayList<Elements> findMyWord(String p){
        String[] res = new String[]{idDB , ingredienteDB , imagenDB , descripcionDB , nombreDB};

        Cursor c = miBD.rawQuery("SELECT * FROM ElementsMinescrapp WHERE nombre LIKE '%"+p+"%' OR descripcion LIKE '%"+p+"%'" , null);

        int iFila = c.getColumnIndex(idDB);
        int iIngredientes= c.getColumnIndex(ingredienteDB);
        int iImagen = c.getColumnIndex(imagenDB);
        int iDescripcion = c.getColumnIndex(descripcionDB);
        int iNombre = c.getColumnIndex(nombreDB);

        ArrayList<Elements> elem = new ArrayList<Elements>();

        for (c.moveToFirst(); !c.isAfterLast();c.moveToNext()){

            Elements elemento =  new Elements();
            //res = res + c.getString(iFila) + " "+  +" "++" "++"";
            elemento.setIngredientes(c.getString(iIngredientes));
            elemento.setImagen(c.getString(iImagen));
            elemento.setDescripcion(c.getString(iDescripcion));
            elemento.setNombre(c.getString(iNombre));
            elem.add(elemento);
            //Log.i("LIKE" ,c.getString(iNombre));

        }
        return elem;
    }


}
