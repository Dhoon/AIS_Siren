package com.example.ais_siren;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import static org.eclipse.paho.client.mqttv3.MqttClient.*;

public class Code_enter extends AppCompatActivity {

// MqQTT subscribe and notification
    // let server direct to this activity directly and invoke the notification.

    NotificationCompat.Builder notification;
    private static final int notiID = 12345;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_enter_code);

        String clientId = generateClientId();
        final MqttAndroidClient client =
                new MqttAndroidClient(this.getApplicationContext(), "tcp://broker.hivemq.com:1883",
                        clientId);

        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener () {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }


            String topic = "foo/bar";
            int qos = 1;
            try {
                IMqttToken subToken = client.subscribe(topic, qos);
                subToken.setActionCallback(new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        // The message was published
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken,
                                          Throwable exception) {
                        // The subscription could not be performed, maybe the user was not
                        // authorized to subscribe on the specified topic e.g. using wildcards

                    }
                });
            } catch (MqttException e) {
                e.printStackTrace();
            }

            client.setCallback (new MqttCallback () {
                @Override
                public void connectionLost(Throwable cause) {

                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    Toast.makeText (Code_enter.this, new String (message.getPayload ()),Toast.LENGTH_SHORT).show ();
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {

                }
            });



//        notification = new NotificationCompat.Builder (this);
//        notification.setAutoCancel (true);
//
//        notification.setSmallIcon (R.drawable.ic_launcher_foreground)
//                .setWhen (System.currentTimeMillis ())
//                .setContentTitle ("Verification")
//                .setContentText ("code");
//
//        Intent intent = new Intent (Code_enter.this, MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity (this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        notification.setContentIntent (pendingIntent);
//
//        NotificationManager nm = (NotificationManager) getSystemService (NOTIFICATION_SERVICE);
//        nm.notify (12345, notification.build ());
    }


}
