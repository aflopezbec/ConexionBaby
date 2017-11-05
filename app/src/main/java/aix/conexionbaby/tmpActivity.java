package aix.conexionbaby;

import android.opengl.GLSurfaceView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class tmpActivity extends AppCompatActivity {

    private GLSurfaceView mySurfaceView;
    private Button temperatura;
    private Button posicion;
    private Button respiraacion;
    private MyGLRenderer mMyGL;
    private AppCompatActivity active;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tmp);
        setTitle("GUDBI - Monitor");

        active = this;
        mMyGL = new MyGLRenderer();

        mySurfaceView = (GLSurfaceView)findViewById(R.id.my_surface_view);
        temperatura = (Button) findViewById(R.id.btn_temperatura);
        posicion = (Button) findViewById(R.id.btn_posicion);
        respiraacion = (Button) findViewById(R.id.btn_respiracion);


        mySurfaceView.setEGLContextClientVersion(2);
        mySurfaceView.setEGLConfigChooser(8 , 8, 8, 8, 16, 0);
        mySurfaceView.setRenderer(mMyGL);
        mySurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

        //float[] color = { 0f, 1.0f, 0f, 1.0f }; //VERDE
        //float[] color = { 1.0f, 0f, 0f, 1.0f } ; //ROJO
        //mMyGL.changeColor(color);

        temperatura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float[] color = { 0f, 1.0f, 0f, 1.0f }; //VERDE
                mMyGL.changeColor(color);
                AlertDialog.Builder builder = new AlertDialog.Builder(active);
                builder.setTitle("Información Temperatura");
                builder.setMessage("La temperatura corporal del bebé es de ##.# grados. \nSeria bueno revisar al bebé");
                builder.setPositiveButton("OK",null);
                builder.create();
                builder.show();
            }
        });

        posicion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float[] color = { 1.0f, 1.0f, 0f, 1.0f }; //AMARILLO
                mMyGL.changeColor(color);
                AlertDialog.Builder builder = new AlertDialog.Builder(active);
                builder.setTitle("Información Posición");
                builder.setMessage("El bebe se encuentra en un poco de lado. \nSeria bueno revisar al bebé");
                builder.setPositiveButton("OK",null);
                builder.create();
                builder.show();

            }
        });

        respiraacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float[] color = { 1.0f, 0f, 0f, 1.0f } ; //ROJO
                mMyGL.changeColor(color);
                AlertDialog.Builder builder = new AlertDialog.Builder(active);
                builder.setTitle("Información Respiración");
                builder.setMessage("La  reespiración del bebé en los ultimos 5 minutos ha sido muy acelerada.");
                builder.setPositiveButton("OK",null);
                builder.create();
                builder.show();

            }
        });

    }
}
