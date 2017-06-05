package seniorproject.androidtoarduinobluetooth;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.AsyncTask;
import java.io.IOException;
import java.util.UUID;

public class ledControl extends AppCompatActivity {

    Button btnOn, btnOff, btnDis;
    SeekBar brightness;
    TextView lumn;
    String address = null;
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get address for bluetooth device
        Intent newint = getIntent();
        address = newint.getStringExtra(ArduinoToAndroid.EXTRA_ADDRESS);
    //call the view of led control activity, call widgets
        setContentView(R.layout.activity_led_control);
        btnDis = (Button)findViewById(R.id.button2);
        btnOn = (Button)findViewById(R.id.button3);
        btnOff = (Button)findViewById(R.id.button4);
        brightness = (SeekBar)findViewById(R.id.seekBar);
        lumn = (TextView)findViewById(R.id.textView);


        new ConnectBt().execute();
        btnOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                turnOnLed();
            }
        });

        btnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                turnOffLed();
            }
        });

        btnDis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Disconnect();
            }
        });

        brightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekbar, int progress, boolean fromUser)
            {
                if(fromUser)
                {
                    lumn.setText(String.valueOf(progress));
                    try
                    {
                        btSocket.getOutputStream().write(String.valueOf(progress).getBytes());
                    }
                    catch(IOException e)
                    {

                    }
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }
        });


        setContentView(R.layout.activity_led_control);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if(fab != null)
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void msg(String s)
    {
      Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }

    private void Disconnect()
    {
        if (btSocket != null) {
            try {
                btSocket.close();
            }
            catch(IOException e)
            {
                msg("Error");
            }
            finish();
        }
    }

    private void turnOffLed()
    {
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write("TF".toString().getBytes());
            }
            catch(IOException e)
            {
                msg("Error");
            }
            finish();
        }
    }

    private void turnOnLed()
    {
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write("TO".toString().getBytes());
            }
            catch(IOException e)
            {
                msg("Error");
            }
            finish();
        }
    }

    private class ConnectBt extends AsyncTask<Void,Void,Void>
    {
        private boolean  ConnectSuccess = true;
        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(ledControl.this, "Connecting...", "Please Wait!!");
        }

        @Override
        protected Void doInBackground(Void... devices)
        {
            try
            {
                if(btSocket == null || !isBtConnected)
                {
                    //get mobile bluetooth device
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();
                    //connect to the device address, see if it is available
                    BluetoothDevice deviceAddress = myBluetooth.getRemoteDevice(address);
                    //create RFCOMM Connection
                    btSocket = deviceAddress.createInsecureRfcommSocketToServiceRecord(myUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();
                }
            }
            catch (IOException e)
            {
                //failure can be caught here
                ConnectSuccess = false;
            }
            return null;
        }
        //check if everything is OK
        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);
            if(!ConnectSuccess)
            {
                msg("Connection Failed. Is it a SPP Bluetooth? Try Again. ");
                finish();
            }
        }
    }

}
