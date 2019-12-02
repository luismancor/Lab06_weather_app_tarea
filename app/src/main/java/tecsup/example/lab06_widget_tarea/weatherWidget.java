package tecsup.example.lab06_widget_tarea;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Implementation of App Widget functionality.
 */
public class weatherWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.weather_widget);


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    //FUNCION AGREGADA DEL WIDGET
    public static void actualizarWidget(Context context, AppWidgetManager appWidgetManager, int widgetId){
        SharedPreferences datos = context.getSharedPreferences("DatosWidget", Context.MODE_PRIVATE);

        String mensaje = datos.getString("mensaje", "Mensaje Recibido");
        String hora = datos.getString("fecha", "Mensaje Recibido");
        String ciudad1 = datos.getString("ciudad", "Mensaje Recibido");
        String temperatura1 = datos.getString("temperatura", "Mensaje Recibido");
        String tiempo1 = datos.getString("tiempo", "Mensaje Recibido");

        RemoteViews controles = new RemoteViews(context.getPackageName(),R.layout.weather_widget);
        controles.setTextViewText(R.id.lblMensaje, mensaje);

        controles.setTextViewText(R.id.lblHora, hora);
        controles.setTextViewText(R.id.tvciudad, ciudad1);
        controles.setTextViewText(R.id.tvtemperatura, temperatura1);
        controles.setTextViewText(R.id.tvtiempo, tiempo1);

//REVISAR 2 veces
        Intent clicenwidget = new Intent(context,MainActivity.class);
        PendingIntent widgetesperando = PendingIntent.getActivity(context,widgetId,clicenwidget,PendingIntent.FLAG_UPDATE_CURRENT);
        controles.setOnClickPendingIntent(R.id.frmWidget,widgetesperando);

        //NO ESTOY SEGURO DEL tecsup.example
        Intent botonwidget = new Intent("tecsup.example.Lab06_Widget_Tarea.ACTUALIZAR_WIDGET");
        botonwidget.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,widgetId);
        PendingIntent botonespera = PendingIntent.getBroadcast(context,widgetId,botonwidget,PendingIntent.FLAG_UPDATE_CURRENT);
        controles.setOnClickPendingIntent(R.id.btnActualizar,botonespera);
        //aqui terminar el boton es el punto 7.8

        appWidgetManager.updateAppWidget(widgetId,controles);
    }
    //METODO ONRECEIVE CREATDO
    public void onReceive(Context context, Intent intent){
        super.onReceive(context, intent);
        if(intent.getAction().equals("tecsup.example.Lab06_Widget_Tarea.ACTUALIZAR_WIDGET")){
            int widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,AppWidgetManager.INVALID_APPWIDGET_ID);

            AppWidgetManager actualizadorwidget = AppWidgetManager.getInstance(context);

            if (widgetId != AppWidgetManager.INVALID_APPWIDGET_ID){
                actualizarWidget(context,actualizadorwidget,widgetId);
            }
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
            actualizarWidget(context,appWidgetManager,appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

