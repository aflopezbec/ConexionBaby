package aix.conexionbaby;

import android.graphics.Color;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class tmpActivity extends AppCompatActivity {

    private GLSurfaceView mySurfaceView;
    private Button temperatura;
    private Button posicion;
    private Button respiraacion;
    private ImageButton infotemperatura;
    private ImageButton infoposicion;
    private ImageButton inforespiracion;
    private MyGLRenderer mMyGL;
    private AppCompatActivity active;

    private String sensor0,txtTemperatura;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tmp);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);


        active = this;
        mMyGL = new MyGLRenderer();

        mySurfaceView = (GLSurfaceView)findViewById(R.id.my_surface_view);
        temperatura = (Button) findViewById(R.id.btn_temperatura);
        posicion = (Button) findViewById(R.id.btn_posicion);
        respiraacion = (Button) findViewById(R.id.btn_respiracion);
        infotemperatura = (ImageButton) findViewById(R.id.infoTemperatura);
        inforespiracion = (ImageButton) findViewById(R.id.infoRespiracion);
        infoposicion = (ImageButton) findViewById(R.id.infoPosicion);


        mySurfaceView.setEGLContextClientVersion(2);
        mySurfaceView.setEGLConfigChooser(8 , 8, 8, 8, 16, 0);
        mySurfaceView.setRenderer(mMyGL);
        mySurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

        sensor0 = "34.5";
        txtTemperatura = "La temperatura corporal del bebé es de ##.# grados. \nSeria bueno revisar al bebé";
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
                builder.setMessage(txtTemperatura);//"La temperatura corporal del bebé es de ##.# grados. \nSeria bueno revisar al bebé");
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

                Double valueTemperatura = Double.valueOf(sensor0);
                if (valueTemperatura<35){
                    txtTemperatura="El bebé se encuentra en una temperatura menor de la normal de "+sensor0+"°";
                    temperatura.setText("Temperatura: MEDIO");
                    infotemperatura.setImageResource(R.drawable.medio_32);
                    //temperatura.setBackgroundColor(Color.parseColor("#f0ad4e"));
                }else if (valueTemperatura>35.4 && valueTemperatura<38.5){
                    txtTemperatura="El bebé se encuentra en una temperatura normal de "+sensor0+"°";
                    temperatura.setText("Temperatura: BAJO");
                    //temperatura.setBackgroundColor(Color.parseColor("#5cb85c"));
                }
                sensor0 = "36.2";


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }
}
