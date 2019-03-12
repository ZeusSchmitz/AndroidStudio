package com.TCC.webhome;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

import static android.support.constraint.Constraints.TAG;

public class TelaControle extends AppCompatActivity {
    boolean clicou = false;
    private FirebaseAuth mAuth;
    GridLayout mainGrid;
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
        setContentView(R.layout.activity_tela_controle);

        context = this.getApplicationContext();
        conectaMqtt();


/*        LinearLayout linearLayout = findViewById(R.id.liCozi);
        final ImageView imageView = findViewById(R.id.viCozi);
        LinearLayout layoutSair = findViewById(R.id.liSair);
        final ImageView imageSair = findViewById(R.id.viSair);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clicou){
                    imageView.setImageResource(R.drawable.ic_lamp_acs);
                    clicou = false;
                }else{
                    imageView.setImageResource(R.drawable.ic_lamp_ico);
                    clicou = true;
                }
            }
        });

        layoutSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clicou){
                    //v.setBackgroundColor(Color.BLACK);
                    imageSair.setImageResource(R.drawable.ic_sair_v);
                    clicou = false;
                    logoutApp();
                }else{
                    imageSair.setImageResource(R.drawable.ic_sair);
                    //v.setBackgroundColor(Color.YELLOW);
                    clicou = true;
                }
            }
        });*/

        mainGrid = findViewById(R.id.mainGrid);

        setSingleEvent(mainGrid);

    }

    public void conectaMqtt(){
        final ImageView imageConec = findViewById(R.id.imageConec);
//        final TextView statusConec = findViewById(R.id.statusConec);
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
                    imageConec.setImageResource(R.drawable.ic_conec_mqtt);
//                    statusConec.setText("Conectado");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Log.d(TAG, "onFailure");
                    imageConec.setImageResource(R.drawable.ic_desconec_mqtt);
//                    statusConec.setText("Desconectando");
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void setSingleEvent(GridLayout mainGrid) {
        for (int i=0;i < mainGrid.getChildCount();i++){
            final CardView cardView = (CardView)mainGrid.getChildAt(i);
            final int finali = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageView imageView = findViewById(R.id.viCozi);
                    ImageView imageViewS = findViewById(R.id.viSair);
                    if(finali == 0){
                        if(clicou){
                            imageView.setImageResource(R.drawable.ic_lamp_ico);
                            clicou = false;
                            Toast.makeText(TelaControle.this, "Desligar :" + finali, Toast.LENGTH_SHORT).show();
                        }else{
                            imageView.setImageResource(R.drawable.ic_lamp_acs);
                            clicou = true;
                            Toast.makeText(TelaControle.this, "Ligar :" + finali, Toast.LENGTH_SHORT).show();
                        }
                    }

                    if(finali == 8){
                        if(clicou){
                            imageViewS.setImageResource(R.drawable.ic_sair);
                            clicou = false;
                        }else{
                            imageViewS.setImageResource(R.drawable.ic_sair_v);
                            clicou = true;
                            logoutApp();
                        }
                    }
                }
            });
        }
    }

    private void logoutApp() {
        mAuth.getInstance().signOut();
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }
}
