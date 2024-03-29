package aix.conexionbaby;

import android.graphics.Color;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Handler bluetoothIn;

    final int handlerState = 0;        				 //used to identify handler message
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder recDataString = new StringBuilder();

    private ConnectedThread mConnectedThread;

    // SPP UUID service - this should work for most devices
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // String for MAC address
    private static String address;

    //Values of interfaz
    private GLSurfaceView mySurfaceView;
    private Button temperatura;
    private Button posicion;
    private Button respiraacion;
    private ImageButton imageTemperatura;
    private ImageButton imagePosicion;
    private ImageButton imageRespiracion;
    private MyGLRenderer mMyGL;
    private AppCompatActivity active;

    private TextView info;

    String txtTemperatura, txtPosicion, txtRespiracion;

    private static final int BAJO = -1;
    private static final int MEDIO = 0;
    private static final int ALTO = 1;

    private int levelTemperatura;
    private int levelRespiracion;
    private int levelPosicion;

    private Double valueRespiracion;
    private Double firstTime = 0.0;
    private Double secondTime = 0.0;
    private Double threeTime = 0.0;
    private byte count = -1;
    private Double sumValue = 0.0;
    private int total = 0;
    private int counter = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        //Final code of tmp
        active = this;
        mMyGL = new MyGLRenderer();

        mySurfaceView = (GLSurfaceView)findViewById(R.id.my_surface_view);
        temperatura = (Button) findViewById(R.id.btn_temperatura);
        posicion = (Button) findViewById(R.id.btn_posicion);
        respiraacion = (Button) findViewById(R.id.btn_respiracion);
        imageTemperatura = (ImageButton) findViewById(R.id.infoTemperatura);
        imageRespiracion = (ImageButton) findViewById(R.id.infoRespiracion);
        imagePosicion = (ImageButton) findViewById(R.id.infoPosicion);

        info = (TextView) findViewById(R.id.textView6);

        mySurfaceView.setEGLContextClientVersion(2);
        mySurfaceView.setEGLConfigChooser(8 , 8, 8, 8, 16, 0);
        mySurfaceView.setRenderer(mMyGL);
        mySurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

        txtTemperatura = txtPosicion = txtRespiracion = "Todo se encuentra en normalidad";



        bluetoothIn = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == handlerState) {										//if message is what we want
                    String readMessage = (String) msg.obj;                                                                // msg.arg1 = bytes from connect thread
                    recDataString.append(readMessage);      								//keep appending to string until ~
                    int endOfLineIndex = recDataString.indexOf("~");                    // determine the end-of-line
                    if (endOfLineIndex > 0) {                                           // make sure there data before ~
                        String dataInPrint = recDataString.substring(0, endOfLineIndex);    // extract string
                        //txtString.setText("Data Received = " + dataInPrint);
                        int dataLength = dataInPrint.length();							//get length of data received
                        //txtStringLength.setText("String Length = " + String.valueOf(dataLength));

                        if (recDataString.charAt(0) == '#')								//if it starts with # we know it is what we are looking for
                        {
                            String [] sensorArray = dataInPrint.split("\\|");
                            String [] positionArray = sensorArray[1].split("&");

                            String sensorTemperatura = sensorArray[0].substring(1);
                            String sensorPitch = positionArray[0];
                            String sensorRoll = positionArray[1];
                            String sensorRespiracion = sensorArray[2];

                            try {
                                Double valueTemperatura = Double.valueOf(sensorTemperatura);
                                if (valueTemperatura<35.5){
                                    txtTemperatura="El bebé se encuentra en una temperatura menor de la normal de "+sensorTemperatura+"°";
                                    temperatura.setText("Temperatura: MEDIO");
                                    levelTemperatura = MEDIO;
                                    //temperatura.setBackgroundColor(Color.parseColor("#f0ad4e"));
                                    imageTemperatura.setImageResource(R.drawable.medio_32);
                                }else if (valueTemperatura>35.4 && valueTemperatura<37.9){
                                    txtTemperatura="El bebé se encuentra en una temperatura normal de "+sensorTemperatura+"°";
                                    temperatura.setText("Temperatura: BAJO");
                                    levelTemperatura = BAJO;
                                    //temperatura.setBackgroundColor(Color.parseColor("#5cb85c"));
                                    imageTemperatura.setImageResource(R.drawable.bajo_32);
                                }else if (valueTemperatura>37.8){
                                    txtTemperatura="El bebé se encuentra en una temperatura mas alta de lo normal.\n tiene "+sensorTemperatura+"° de temperatura";
                                    temperatura.setText("Temperatura: ALTO");
                                    //temperatura.setBackgroundColor(Color.parseColor("#d9534f"));
                                    imageTemperatura.setImageResource(R.drawable.alto_32);
                                }else{
                                    txtTemperatura="ERROR";
                                    temperatura.setText("Temperatura: ALTO >");
                                    levelTemperatura = ALTO;
                                    imageTemperatura.setImageResource(R.drawable.alto_32);
                                    //temperatura.setBackgroundColor(Color.parseColor("#d9534f"));
                                }

                                Double valuePitch = Double.valueOf(sensorPitch);
                                if (valuePitch>0){
                                    txtPosicion = "El bebe se encuentra boca arriba ";
                                    posicion.setText("Posición: BAJO");
                                    levelPosicion = BAJO;
                                    imagePosicion.setImageResource(R.drawable.bajo_32);
                                }else if (valuePitch<=0){
                                    txtPosicion = "El bebe se encuentra boca abajo ";
                                    posicion.setText("Posición: MEDIO");
                                    levelPosicion = MEDIO;
                                    imagePosicion.setImageResource(R.drawable.medio_32);
                                }else{
                                    txtPosicion = "ERROR ";
                                    posicion.setText("Posición: ALTO");
                                    levelPosicion = ALTO;
                                    imagePosicion.setImageResource(R.drawable.alto_32);
                                }

                                Double valueRoll = Double.valueOf(sensorRoll);
                                if (valueRoll>0){
                                    txtPosicion = txtPosicion+" hacia el lado derecho ";
                                }else if (valueRoll<=0){
                                    txtPosicion = txtPosicion+" hacia el lado izquierdo ";
                                }else{
                                    txtPosicion = txtPosicion+" ERROR ";
                                    posicion.setText("Posición: ALTO");
                                    levelPosicion = ALTO;
                                    imagePosicion.setImageResource(R.drawable.alto_32);
                                }

                                //REPIRACIÓN
                                counter ++;
                                if(counter%256==0) {
                                    count++;
                                    if (count == 0) {
                                        firstTime = sumValue/total;
                                        sumValue = 0.0;
                                        total = 0;
                                    } else if (count == 1) {
                                        secondTime = sumValue/total;
                                        sumValue = 0.0;
                                        total = 0;
                                    } else if (count == 2) {
                                        threeTime = sumValue/total;
                                        sumValue = 0.0;
                                        total = 0;
                                        count = -1;
                                    } else {
                                        Log.d("Erro sum MainActivity", "");
                                    }
                                }else{
                                    if(!sensorRespiracion.equals("nan")) {
                                        total++;
                                        valueRespiracion = Double.valueOf(sensorRespiracion);
                                        sumValue = sumValue + valueRespiracion;
                                    }
                                }

                                if(firstTime!=0.0 && secondTime != 0.0 && threeTime!=0.0){
                                    Double prom = (firstTime+secondTime+threeTime)/3;
                                    if (prom>50){
                                        txtRespiracion = "El bebe presenta una correcta respiración en el ultimo minuto y medio";
                                        respiraacion.setText("Respiración: BAJO");
                                        levelRespiracion = BAJO;
                                        imageRespiracion.setImageResource(R.drawable.bajo_32);
                                    }else if (prom<=50){
                                        txtRespiracion = "El bebe spresenta una respiración muy lenta en el ultimo minuto y medio";
                                        respiraacion.setText("Respiración: MEDIO");
                                        levelRespiracion = MEDIO;
                                        imageRespiracion.setImageResource(R.drawable.medio_32);
                                    }else{
                                        txtRespiracion = "ERROR ";
                                        respiraacion.setText("Respiración: ALTO");
                                        levelRespiracion = ALTO;
                                        imageRespiracion.setImageResource(R.drawable.alto_32);
                                    }

                                }else{
                                    txtRespiracion = "No se ha generado reporte de respiración durante el ultimo minuto y medio";
                                    //+sensorRespiracion+"-"+firstTime+"|"+secondTime+"|"+threeTime;
                                    respiraacion.setText("Respiración: ALTO");
                                    levelRespiracion = ALTO;
                                    imageRespiracion.setImageResource(R.drawable.alto_32);
                                }
                                //info.setText( dataInPrint+"-->"+ sumValue+"-"+firstTime+"|"+secondTime+"|"+threeTime+"--"+counter+"|"+count);

                                /*
                                if (valueRespiracion>50){
                                    txtRespiracion = "El bebe presenta una correcta respiración ";
                                    respiraacion.setText("Respiración: BAJO");
                                    levelRespiracion = BAJO;
                                    imageRespiracion.setImageResource(R.drawable.bajo_32);
                                }else if (valueRespiracion<=50){
                                    txtRespiracion = "El bebe spresenta una respiración muy lenta";
                                    respiraacion.setText("Posición: ALTO");
                                    levelRespiracion = MEDIO;
                                    imageRespiracion.setImageResource(R.drawable.medio_32);
                                }else{
                                    txtRespiracion = "ERROR ";
                                    respiraacion.setText("Posición: ALTO");
                                    levelRespiracion = ALTO;
                                    imageRespiracion.setImageResource(R.drawable.alto_32);
                                }*/



                            }catch (Exception e){
                                Log.d("Error leyendo dato", e.toString());
                            }

                            //sensorView0.setText(" Temperatura = " + sensor0 + " C");	//update the textviews with sensor values
                            //sensorView1.setText(" Posición 1 = " + sensor1 + " °");
                            //sensorView2.setText(" Posición 2 = " + sensor2 + " °");
                            //sensorView3.setText(" Respiración = " + sensor3 + " G");
                        }
                        recDataString.delete(0, recDataString.length()); 					//clear all string data
                        // strIncom =" ";
                        dataInPrint = " ";
                    }
                }
            }
        };

        btAdapter = BluetoothAdapter.getDefaultAdapter();       // get Bluetooth adapter
        checkBTState();


        temperatura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float[] color = { 0f, 1.0f, 0f, 1.0f }; //VERDE
                mMyGL.changeColor(color);
                AlertDialog.Builder builder = new AlertDialog.Builder(active);
                builder.setTitle("Información Temperatura");
                builder.setMessage(txtTemperatura);
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
                builder.setMessage(txtPosicion);
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
                builder.setMessage(txtRespiracion);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_phrase){
            startActivity(new Intent(getApplicationContext(), AboutUs.class));
        }
        return super.onOptionsItemSelected(item);
    }


    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {

        return  device.createRfcommSocketToServiceRecord(BTMODULEUUID);
        //creates secure outgoing connecetion with BT device using UUID
    }

    @Override
    public void onResume() {
        super.onResume();

        //Get MAC address from DeviceListActivity via intent
        Intent intent = getIntent();

        //Get the MAC address from the DeviceListActivty via EXTRA
        address = intent.getStringExtra(DeviceListActivity.EXTRA_DEVICE_ADDRESS);

        //create device and set the MAC address
        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        try {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e) {
            Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_LONG).show();
        }
        // Establish the Bluetooth socket connection.
        try
        {
            btSocket.connect();
        } catch (IOException e) {
            try
            {
                btSocket.close();
            } catch (IOException e2)
            {
                //insert code to deal with this
            }
        }
        mConnectedThread = new ConnectedThread(btSocket);
        mConnectedThread.start();

        //I send a character when resuming.beginning transmission to check device is connected
        //If it is not an exception will be thrown in the write method and finish() will be called
        mConnectedThread.write("x");
    }

    @Override
    public void onPause()
    {
        super.onPause();
        try
        {
            //Don't leave Bluetooth sockets open when leaving activity
            btSocket.close();
        } catch (IOException e2) {
            //insert code to deal with this
        }
    }

    //Checks that the Android device Bluetooth is available and prompts to be turned on if off
    private void checkBTState() {

        if(btAdapter==null) {
            Toast.makeText(getBaseContext(), "Device does not support bluetooth", Toast.LENGTH_LONG).show();
        } else {
            if (btAdapter.isEnabled()) {
            } else {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }

    //create new class for connect thread
    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        //creation of the connect thread
        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                //Create I/O streams for connection
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }


        public void run() {
            byte[] buffer = new byte[256];
            int bytes;

            // Keep looping to listen for received messages
            while (true) {
                try {
                    bytes = mmInStream.read(buffer);        	//read bytes from input buffer
                    String readMessage = new String(buffer, 0, bytes);
                    // Send the obtained bytes to the UI Activity via handler
                    bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }
        //write method
        public void write(String input) {
            byte[] msgBuffer = input.getBytes();           //converts entered String into bytes
            try {
                mmOutStream.write(msgBuffer);                //write bytes over BT connection via outstream
            } catch (IOException e) {
                //if you cannot write, close the application
                Toast.makeText(getBaseContext(), "Connection Failure", Toast.LENGTH_LONG).show();
                finish();

            }
        }
    }
}
