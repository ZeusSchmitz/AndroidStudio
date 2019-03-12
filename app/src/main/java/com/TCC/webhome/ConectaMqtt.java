package com.TCC.webhome;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

import static android.support.constraint.Constraints.TAG;

public class ConectaMqtt extends AppCompatActivity {
    static String MQTTHOST = "tcp://m11.cloudmqtt.com:10661";
    static String USERNAME = "rcbdsiji";
    static String PASSWORD = "BjNDV0XWvUEv";
    static String topicstr = "esp8266/pincmd";
    MqttAndroidClient client;
    Context context;
    IMqttToken token;
    MqttConnectOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conecta_mqtt);

        context = this.getApplicationContext();
        conectaMqtt();
    }

    public void conectaMqtt(){
        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(context, MQTTHOST, clientId);

        options = new MqttConnectOptions();
        options.setUserName(USERNAME);
        options.setPassword(PASSWORD.toCharArray());

        try {
            token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Log.d(TAG, "onSuccess");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Log.d(TAG, "onFailure");

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
