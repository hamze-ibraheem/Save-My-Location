package com.linagas.android.gastest;

import android.app.Activity;
import android.database.Cursor;

import org.json.JSONArray;

import static com.linagas.android.gastest.MapsActivity.db;

/**
 * Created by hamze sarsour local on 12/2/2016.
 */

public class SqlDB extends Activity {



    static String sqlLat,sqlLng;
    static String sqlAllid,sqlAllName,sqlAllLat,sqlAllLng,sqlAllDescription;










    public void insert_myLocation(String id,String name,String lat,String lng,String description)
    {

        try{
        db.beginTransaction();
            db.execSQL("INSERT INTO myLocation VALUES('"+ id +"','"+ name +"','"+ lat +"','"+ lng +"'" +",'"+ description +"')");
            db.setTransactionSuccessful();
            db.endTransaction();
        }catch(Exception ex){ex.printStackTrace();}
    }

    public void get_myLocation(String id)
    {
        try{
        Cursor c=db.rawQuery("SELECT * FROM myLocation WHERE id = '"+id+"' ", null);
        c.moveToFirst();
        while (!c.isAfterLast()){


            sqlLat = c.getString(2);
            sqlLng = c.getString(3);



            c.moveToNext();

        }
            //
    }
    catch(Exception ex){
        ex.printStackTrace();
    }
    finally{}
    }


    public JSONArray get_allData(JSONArray data)
    {
        data = new JSONArray();



        int i = 0 ;
        try{
            Cursor c=db.rawQuery("SELECT * FROM myLocation ", null);
            c.moveToFirst();
            while (!c.isAfterLast()){

                sqlAllid = c.getString(0);
                sqlAllName = c.getString(1);
                sqlAllLat = c.getString(2);
                sqlAllLng = c.getString(3);
                sqlAllDescription = c.getString(4);


                data.put(sqlAllid);
                data.put(sqlAllName);
                data.put(sqlAllLat);
                data.put(sqlAllLng);
                data.put(sqlAllDescription);

               // jsonString = data.toString();


                i++;

                c.moveToNext();

            }



        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        finally{
            return data;
        }


    }


    public int setMaxID(int maxid)
    {

        try{
            Cursor c=db.rawQuery("SELECT max(id) FROM myLocation ", null);
            c.moveToFirst();
            while (!c.isAfterLast()){


                maxid = Integer.parseInt(c.getString(0));




                c.moveToNext();

            }
            //
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        finally{


                return maxid;

        }
    }

}
