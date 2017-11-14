package com.ditmarcastro.reviewmovie;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.ditmarcastro.reviewmovie.Collection.Item;
import com.ditmarcastro.reviewmovie.restApi.OnRestLoadListener;
import com.ditmarcastro.reviewmovie.restApi.RestApi;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnRestLoadListener, android.view.View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        loadComponents();
        //checkRest();
    }

    private void checkRest() {
        RestApi api = new RestApi("POST");
        api.setOnRestLoadListener(this, 10);
        api.addParams("name","Hernan");
        api.addParams("email","Hernan@gmail.com");
        api.addParams("password","123456");
        api.execute("http://192.168.1.109:3000/createuser");
    }

    // cargar los componentes visuales que corresponden a la lista
    private void loadComponents() {
        ListView list = (ListView)this.findViewById(R.id.list_main);
        ArrayList<Item> list_data = new ArrayList<Item>();
        for (int i = 0; i < 100; i++ ) {
            Item p = new Item();
            p.id = i;
            p.title = "Titulo " + i;
            p.description = "Descripcion "+i;
            p.url = "image " + i;
            list_data.add(p);
        }

        ListAdapter adapter = new com.ditmarcastro.reviewmovie.Collection.ListAdapter(this, list_data);
        list.setAdapter(adapter);

        FloatingActionButton btn = (FloatingActionButton)this.findViewById(R.id.fab);
        btn.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent camera = new Intent(this, Camera.class);
            camera.putExtra("hello","HOLA SOY LA ACTIVIDAD PADRE");
            this.startActivity(camera);


        } else if (id == R.id.nav_gallery) {
            Intent gallery = new Intent(this, Gallery.class);
            this.startActivity(gallery);


        } else if (id == R.id.nav_slideshow) {
            Intent slideshow = new Intent(this, Slideshow.class);
            this.startActivity(slideshow);


        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRestLoadComplete(JSONObject obj, int id) {
        // EL POST
        if(id == 10){
            Toast.makeText(this, obj.toString(),Toast.LENGTH_SHORT).show();
        }
        if(id == 11){

        }
        if(id == 100) {
            Toast.makeText(this,obj.toString(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActionResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onClick(View view) {
        Intent photo = new Intent(Intent.ACTION_PICK);
        photo.setType("image/");
        this.startActivityForResult(photo, 1);
        //super.onActivityResult();
    }
    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent data) {
        if(requestCode == 1) {
            if(responseCode == Activity.RESULT_OK){
                Uri imagen = data.getData();
                String url = this.getPath(imagen);
                File myFile = new File(url);
                RequestParams params = new RequestParams();
                try {
                    params.put("image", myFile);
                } catch(FileNotFoundException e) {}

// send request
                AsyncHttpClient client = new AsyncHttpClient();
                client.post("http://192.168.1.107:3000/sendimage", params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {
                        // handle success response
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] bytes, Throwable throwable) {
                        // handle failure response
                    }
                });
                //Bitmap bitmapImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imagen);
                    /*File fileimage = new File(this.getCacheDir(),"tmpImage");
                    fileimage.createNewFile();
                    OutputStream os = new BufferedOutputStream(new FileOutputStream(fileimage));
                    bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, os);
                    os.close();
                    RestApi api = new RestApi("POST");
                    api.setFile(fileimage);
                    api.setOnRestLoadListener(this,100);
                    api.execute("http://192.168.1.107:3000/sendimage");
                    */


                //String url = this.getPath(imagen);
                //File fileimage = new File(url);

            }
        }
    }
    public String getPath(Uri uri) {
        int column_index;
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}
