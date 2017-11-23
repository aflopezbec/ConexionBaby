package aix.conexionbaby;

import android.content.Intent;
import android.graphics.Color;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class tmpActivity extends AppCompatActivity {

    private GLSurfaceView mySurfaceView;
    private Button temperatura;
    private Button posicion;
    private Button respiraacion;
    private Button btnTest;
    private Button btnGeneral;
    private int flag = 0;

    private ImageButton imageTemperatura;
    private ImageButton imagePosicion;
    private ImageButton imageRespiracion;
    private MyGLRenderer mMyGL;
    private AppCompatActivity active;

    private String txtTemperatura,sensorTemperatura,txtPosicion,txtRespiracion;

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
        btnTest = (Button) findViewById(R.id.btnTest);
        btnGeneral = (Button) findViewById(R.id.btn_general);

        respiraacion = (Button) findViewById(R.id.btn_respiracion);
        imageTemperatura = (ImageButton) findViewById(R.id.infoTemperatura);
        imageRespiracion = (ImageButton) findViewById(R.id.infoRespiracion);
        imagePosicion = (ImageButton) findViewById(R.id.infoPosicion);


        mySurfaceView.setEGLContextClientVersion(2);
        mySurfaceView.setEGLConfigChooser(8 , 8, 8, 8, 16, 0);
        mySurfaceView.setRenderer(mMyGL);
        mySurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

       // String dataInPrint = "#34.59|-1.68&-0.04|0.00|~";
        //String [] sensorArray = dataInPrint.split("\\|");
        //String [] positionArray = sensorArray[1].split("&");

        //sensorTemperatura = sensorArray[0].substring(1);             //get sensor value from string between indices 1-5
        //String sensorPitch = positionArray[0];            //same again...
        //String sensorRoll = positionArray[1];
        //String sensorRespiracion = sensorArray[2];

        //sensor0 = "34.5";
        //txtTemperatura = "T: "+sensorTemperatura+" Pi: "+sensorPitch+" Roll: "+sensorRoll+" Res: "+sensorRespiracion;
        //float[] color = { 0f, 1.0f, 0f, 1.0f }; //VERDE
        //float[] color = { 1.0f, 0f, 0f, 1.0f } ; //ROJO
        //mMyGL.changeColor(color);
        txtTemperatura = "La termperatura corporal del bebé es de 36.2 grados. Tiene una temperatura normal.";
        txtPosicion = "El bebé se encuentra boca arriba hacia la derecha";
        txtRespiracion = "En el ultimo reporte el bebé presenta una respiración normal";

        imageRespiracion.setImageResource(R.drawable.bajo_32);
        imagePosicion.setImageResource(R.drawable.bajo_32);
        imageTemperatura.setImageResource(R.drawable.bajo_32);
        btnGeneral.setBackgroundColor(Color.parseColor("#5cb85c"));//BAJO :: VERDE

        //float[] color = { 0f, 1.0f, 0f, 1.0f }; //VERDE
        //mMyGL.changeColor(color);

        temperatura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                //float[] color = { 1.0f, 1.0f, 0f, 1.0f }; //AMARILLO
                //mMyGL.changeColor(color);
                AlertDialog.Builder builder = new AlertDialog.Builder(active);
                builder.setTitle("Información Posición");
                builder.setMessage(txtPosicion);
                builder.setPositiveButton("OK",null);
                builder.create();
                builder.show();
            }
        });

        respiraacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //float[] color = { 1.0f, 0f, 0f, 1.0f } ; //ROJO
                //mMyGL.changeColor(color);
                AlertDialog.Builder builder = new AlertDialog.Builder(active);
                builder.setTitle("Información Respiración");
                builder.setMessage(txtRespiracion);
                builder.setPositiveButton("OK",null);
                builder.create();
                builder.show();

            }
        });

        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag==0){
                    float[] color = { 1.0f, 1.0f, 0f, 1.0f }; //AMARILLO
                    mMyGL.changeColor(color);
                    txtTemperatura = "La termperatura corporal del bebé es de 35.4 grados. Tiene una temperatura mas baja de lo normal.";
                    temperatura.setText("TEMPERATURA: MEDIO");
                    btnGeneral.setText("RIESGO: MEDIO");
                    imageTemperatura.setImageResource(R.drawable.medio_32);
                    btnGeneral.setBackgroundColor(Color.parseColor("#f0ad4e"));
                }else if(flag==1){
                    float[] color = { 1.0f, 1.0f, 0f, 1.0f }; //AMARILLO
                    mMyGL.changeColor(color);
                    txtRespiracion = "En el ultimo minuto y medio el bebé presenta una respiración mas lenta de lo normal";
                    respiraacion.setText("RESPIRACIÓN: MEDIO");
                    imageRespiracion.setImageResource(R.drawable.medio_32);
                    btnGeneral.setBackgroundColor(Color.parseColor("#f0ad4e"));
                }else if(flag==2){
                    float[] color = { 1.0f, 0f, 0f, 1.0f } ; //ROJO
                    mMyGL.changeColor(color);
                    txtPosicion = "El bebé se encuentra boca abajo hacia la derecha ";
                    posicion.setText("POSICIÖN: MEDIO");
                    btnGeneral.setText("RIESGO: ALTO");
                    imagePosicion.setImageResource(R.drawable.medio_32);
                    btnGeneral.setBackgroundColor(Color.parseColor("#d9534f"));
                }else if(flag==3){
                    float[] color = { 1.0f, 1.0f, 0f, 1.0f }; //AMARILLO
                    mMyGL.changeColor(color);
                    txtTemperatura = "La termperatura corporal del bebé es de 36.4 grados. Tiene una temperatura normal.";
                    temperatura.setText("TEMPERATURA: BAJO");
                    btnGeneral.setText("RIESGO: MEDIO");
                    imageTemperatura.setImageResource(R.drawable.bajo_32);
                    btnGeneral.setBackgroundColor(Color.parseColor("#f0ad4e"));
                }else if(flag==4){
                    float[] color = { 1.0f, 0f, 0f, 1.0f } ; //ROJO
                    mMyGL.changeColor(color);
                    txtTemperatura = "La termperatura corporal del bebé es de 38.5 grados. Tiene una temperatura muy alta";
                    temperatura.setText("TEMPERATURA: ALTO");
                    btnGeneral.setText("RIESGO: ALTO");
                    imageTemperatura.setImageResource(R.drawable.alto_32);
                    btnGeneral.setBackgroundColor(Color.parseColor("#d9534f"));
                }else if(flag==5){
                    float[] color = { 0f, 1.0f, 0f, 1.0f }; //VERDE
                    mMyGL.changeColor(color);
                    temperatura.setText("TEMPERATURA: BAJO");
                    posicion.setText("POSICIÖN: BAJO");
                    respiraacion.setText("RESPIRACIÓN: BAJO");

                    btnGeneral.setText("RIESGO: BAJO");
                    txtTemperatura = "La termperatura corporal del bebé es de 36.2 grados. Tiene una temperatura normal.";
                    txtPosicion = "El bebé se encuentra boca arriba hacia la derecha";
                    txtRespiracion = "En el ultimo reporte el bebé presenta una respiración normal";

                    imageRespiracion.setImageResource(R.drawable.bajo_32);
                    imagePosicion.setImageResource(R.drawable.bajo_32);
                    imageTemperatura.setImageResource(R.drawable.bajo_32);
                    btnGeneral.setBackgroundColor(Color.parseColor("#5cb85c"));//BAJO :: VERDE
                    flag = -1;
                }
                flag= flag+1;

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_phrase){
            startActivity(new Intent(getApplicationContext(), AboutUs.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
