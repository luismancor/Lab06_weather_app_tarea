package tecsup.example.lab06_widget_tarea;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextView ciudad, fecha, temperatura, tiempo;
    // DELCARACIONES DEL LAB
    private int widgetId =0;
    private Button btnAceptar,btnCancelar;
    private EditText txtMensaje;
    String hume, temp;
    //FIN


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ciudad = findViewById(R.id.city);
        fecha = findViewById(R.id.fecha);
        temperatura = findViewById(R.id.temperature);
        tiempo = findViewById(R.id.weather);

        findWeather();



        //FUNCIONES DEL BOTON Y LAB
        //FUNCIONES DE LAB BOTON Y ENVIO

        txtMensaje = findViewById(R.id.txtEnviar);
        btnAceptar = findViewById(R.id.btnAceptar);
        btnCancelar = findViewById(R.id.btnCancelar);

        Intent recibidowidget = getIntent();
        Bundle parametros = recibidowidget.getExtras();
        if(parametros!=null){
            widgetId=parametros.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        setResult(RESULT_CANCELED);

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences datos = getSharedPreferences("DatosWidget", Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = datos.edit();
                editor.putString("mensaje",txtMensaje.getText().toString());
                editor.putString("fecha",fecha.getText().toString());
                editor.putString("ciudad",ciudad.getText().toString());
                editor.putString("temperatura",temperatura.getText().toString());
                editor.putString("tiempo",tiempo.getText().toString());


                editor.commit();

                AppWidgetManager notificarwidget = AppWidgetManager.getInstance(MainActivity.this);
                weatherWidget.actualizarWidget(MainActivity.this,notificarwidget,widgetId);

                Intent resultado = new Intent();
                resultado.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,widgetId);
                setResult(RESULT_OK,resultado);
                finish();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void findWeather() {

        //String url = "http://api.openweathermap.org/data/2.5/weather?id=3947319&appid=0aea89c5beb871c7e8dd572468aeb774&units=metric&lang=es";
        String url = "http://192.168.0.31:8000/datos";

        JsonArrayRequest jor = new JsonArrayRequest(Request.Method.GET,url,null,new Response.Listener<JSONArray>(){
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(JSONArray response) {
                try {
                    //JSONArray mJsonArray = new JSONArray(response);
                    JSONObject mJsonObject = response.getJSONObject(0);


                    /*JSONObject objeto_principal = response.getJSONObject("main");
                    JSONArray tiempo2 = response.getJSONArray("weather");
                    JSONObject objeto = tiempo2.getJSONObject(0);

                    String temperatura3 = String.valueOf(objeto_principal.getDouble("temp"));
                    String descripcion = objeto.getString("description");
                    String city = response.getString("name");
*/

                    ciudad.setText("Datos capturados");
                    temp = mJsonObject.getString("temperatura");
                    hume = mJsonObject.getString("humedad");

                    temperatura.setText(temp);
                    tiempo.setText(hume);
                    System.out.println(hume+"ESEASDASDASSSSSSSSSSSSSSSSSSSSSSSSS");
                    Log.i(temp,hume);

                    Calendar calendar = Calendar.getInstance();

                    SimpleDateFormat asd = new SimpleDateFormat("yyyy-MMMM-dd HH:mm:ss aaa", new Locale("es", "ES"));



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
        /*
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject objeto_principal = response.getJSONObject("main");
                    JSONArray tiempo2 = response.getJSONArray("weather");
                    JSONObject objeto = tiempo2.getJSONObject(0);

                    String temperatura3 = String.valueOf(objeto_principal.getDouble("temp"));
                    String descripcion = objeto.getString("description");
                    String city = response.getString("name");

                    ciudad.setText(city);
                    temperatura.setText(temperatura3);
                    tiempo.setText(descripcion);



                    Calendar calendar = Calendar.getInstance();

                    SimpleDateFormat asd = new SimpleDateFormat("yyyy-MMMM-dd HH:mm:ss aaa", new Locale("es", "ES"));


                    String fechaFormateada = asd.format(calendar.getTime());
                    fecha.setText(fechaFormateada);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }*/
        , new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        );
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jor);
    }







}
