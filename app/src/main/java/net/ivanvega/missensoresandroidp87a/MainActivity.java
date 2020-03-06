package net.ivanvega.missensoresandroidp87a;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    TextView txt;
    TextView txtStepDetector;
    TextView txtProxy;
    TextView txtacelero;
    TextView txtorienta;


    private SensorManager sensorManager;
    private Sensor mLight;
    private Sensor sensorConuter;
    private Sensor sensorProxy;
    private Sensor sensoracele;
    private Sensor sensorRotation;

    Sensor magneticField ;


    private final float[] accelerometerReading = new float[3];
    private final float[] magnetometerReading = new float[3];

    private final float[] rotationMatrix = new float[9];
    private final float[] orientationAngles = new float[3];
    private Sensor accelerometer;
    private boolean mLastMagnetometerSet;
    private boolean mLastAccelerometerSet;
    private float mTargetDirection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt = findViewById(R.id.txtSenLx);
        txtStepDetector = findViewById(R.id.txtStepDetector);
        txtProxy = findViewById(R.id.txtProximidad);
        txtacelero = findViewById(R.id.txtacelero);
        txtorienta = findViewById(R.id.txtorientacion);

        sensorManager =
                (SensorManager) getSystemService(Context.SENSOR_SERVICE);


        List<Sensor> deviceSensors =
                sensorManager.getSensorList(Sensor.TYPE_ALL);


        for(Sensor sensor : deviceSensors){
            String datos =  "Nombre: " + sensor.getName();
            datos += "\nTipo: " + sensor.getStringType();
            datos += "\nVendor: " + sensor.getVendor();
            datos += "\nRangoMaximo: " + sensor.getMaximumRange();
            datos += "\nEnergia: " + sensor.getPower();
            Log.i("MISSENSORES", datos);
        }

        if (sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null){
            // Success! There's a magnetometer.
            Toast.makeText(this,
                    "Tenemos sensores de campo magnetco",
                    Toast.LENGTH_SHORT).show();
            Log.i("MISSERVICIOS", "Si hay de campo magnetico");
            List<Sensor> lst =
                    sensorManager.getSensorList(Sensor.TYPE_MAGNETIC_FIELD);

            for(Sensor sensor : lst){
                String datos =  "Nombre: " + sensor.getName();
                datos += "\nTipo: " + sensor.getStringType();
                datos += "\nVendor: " + sensor.getVendor();
                datos += "\nRangoMaximo: " + sensor.getMaximumRange();
                datos += "\nEnergia: " + sensor.getPower();
                Log.i("MISSENSORES", datos);
            }

        } else {
            // Failure! No magnetometer.
        }
        mLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensorConuter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER );
        sensorProxy = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        sensoracele = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //sensorRotation = sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);
        //magneticField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

//         accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//        if (accelerometer != null) {
//            sensorManager.registerListener(sensorEventListener, accelerometer,
//                    SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
//        }
//
//         magneticField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
//        if (magneticField != null) {
//            sensorManager.registerListener(sensorEventListener, magneticField,
//                    SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
//        }

    }

    SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {

            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                System.arraycopy(event.values, 0, accelerometerReading,
                        0, accelerometerReading.length);
                mLastAccelerometerSet = true;

            } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                System.arraycopy(event.values, 0, magnetometerReading,
                        0, magnetometerReading.length);
                mLastMagnetometerSet = true;
            }
            if (mLastAccelerometerSet && mLastMagnetometerSet) {
                updateOrientationAngles();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this,
                mLight,
                SensorManager.SENSOR_DELAY_NORMAL
                );
        sensorManager.registerListener(this,sensorConuter, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this,sensorProxy, SensorManager.SENSOR_DELAY_NORMAL);
        //sensorManager.registerListener(this,sensoracele, SensorManager.SENSOR_DELAY_UI);
        //sensorManager.registerListener(this,sensorRotation, SensorManager.SENSOR_DELAY_NORMAL);
        //sensorManager.registerListener(this, magneticField, SensorManager.SENSOR_DELAY_UI);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometer != null) {
            sensorManager.registerListener(sensorEventListener, accelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
        }

        magneticField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if (magneticField != null) {
            sensorManager.registerListener(sensorEventListener, magneticField,
                    SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
        sensorManager.unregisterListener(sensorEventListener);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        switch( sensorEvent.sensor.getType()){
            case Sensor.TYPE_STEP_DETECTOR:
                txtStepDetector.setText( "Sensor Detector Pasos: " + String.valueOf( sensorEvent.values[0]));
                break;


            case Sensor.TYPE_STEP_COUNTER:
                txtStepDetector.setText( "Sensor Detector Pasos: " + String.valueOf( sensorEvent.values[0]));
                break;

            case Sensor.TYPE_LIGHT:
                // The light sensor returns a single value.
                // Many sensors return 3 values, one for each axis.
                float lux = sensorEvent.values[0];
                // Do something with this sensor value.
                txt.setText( String.valueOf( lux));

                Log.i("LUZ", "Valores: " + String.valueOf( lux) );
                break;

            case Sensor.TYPE_PROXIMITY:
                txtProxy.setText( "Sensor Proximidad: " + String.valueOf( sensorEvent.values[0]));
                break;
            case Sensor.TYPE_GAME_ROTATION_VECTOR:
                float[] rotMatrix = new float[9];
                float[] rotVals = new float[3];

                SensorManager.getRotationMatrixFromVector(rotMatrix, sensorEvent.values);
                SensorManager.remapCoordinateSystem(rotMatrix,
                        SensorManager.AXIS_X, SensorManager.AXIS_Y, rotMatrix);

                SensorManager.getOrientation(rotMatrix, rotVals);
                float azimuth = (float) Math.toDegrees(rotVals[0]);
                float pitch = (float) Math.toDegrees(rotVals[1]);
                float roll = (float) Math.toDegrees(rotVals[2]);


                //txtorienta.setText("Orientacion. Azimuth: " + String.valueOf(azimuth) + "  ;   " + String.valueOf(rotVals[2]));

                if(azimuth > 60 && azimuth < 90){
//                    sensorLayout.setBackgroundResource(R.drawable.sensor_background_three);


                }else if(pitch > 10){
//                    sensorLayout.setBackgroundResource(R.drawable.sensor_background_two);

                }else if(roll > 15){
//                    sensorLayout.setBackgroundResource(R.drawable.sensor_background);

                }else{
//                    sensorLayout.setBackgroundResource(R.drawable.sensor_background_four);

                }
                break;

            case  Sensor.TYPE_MAGNETIC_FIELD:
               // System.arraycopy(sensorEvent.values, 0, magnetometerReading,
                 //       0, magnetometerReading.length);
                //updateOrientationAngles();
                break;
            case Sensor.TYPE_ACCELEROMETER:
                //txtacelero.setText("Valor en X: " + String.valueOf(sensorEvent.values[0]) + "\nValor en Y: " + String.valueOf(sensorEvent.values[1]) + "\nValor en z: " + String.valueOf(sensorEvent.values[2]));
                //System.arraycopy(sensorEvent.values, 0, accelerometerReading,
                 //       0, accelerometerReading.length);
                //updateOrientationAngles();
                break;
        }




    }

    // Compute the three orientation angles based on the most recent readings from
    // the device's accelerometer and magnetometer.
    public void updateOrientationAngles() {
        // Update rotation matrix, which is needed to update orientation angles.
        SensorManager.getRotationMatrix(rotationMatrix, null,
                accelerometerReading, magnetometerReading);

        // "mRotationMatrix" now has up-to-date information.

        SensorManager.getOrientation(rotationMatrix, orientationAngles);

        // "mOrientationAngles" now has up-to-date information.

        float azimuthInRadians = orientationAngles[0];
        float azimuthInDegress = (float) (Math.toDegrees(azimuthInRadians) + 360) % 360;

        mTargetDirection = azimuthInDegress;

        Log.i("ORIEnTA", String.valueOf(mTargetDirection));
        txtorienta.setText("Orientacion. Azimuth: "  + mTargetDirection);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {


    }
}
