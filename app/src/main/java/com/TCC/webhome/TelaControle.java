package com.TCC.webhome;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.nio.channels.Channel;

public class TelaControle extends AppCompatActivity {
    boolean clicou = false;
    private FirebaseAuth mAuth;
    GridLayout mainGrid;
    Context context;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference(".info/connected");
    DatabaseReference myRef = database.getReference("lamp");
    DatabaseReference tempRef = database.getReference("temperature");
    DatabaseReference espConected = database.getReference("espConnected");
    DatabaseReference presence = database.getReference("presence");
    boolean statusLamp = false;
    boolean espCone = false;
    boolean passed = false;
    String titulo = "Sensor acionado";
    String textAlert = "Sensor barreira 1 acionado";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_controle);

        context = this.getApplicationContext();
        mainGrid = findViewById(R.id.mainGrid);

        readStatus();
        setSingleEvent(mainGrid);
        createNotificationChannel();
    }

    private void firebaseConnected(String connected) {
        final ImageView imageConec = findViewById(R.id.imageConec);
        if(connected.equals("connected")){
            if (espCone) {
                imageConec.setImageResource(R.drawable.ic_conec_mqtt);
                Toast.makeText(TelaControle.this, "Firebase/ESP conectados", Toast.LENGTH_SHORT).show();
            } else {
                imageConec.setImageResource(R.drawable.ic_desconec_mqtt);
                Toast.makeText(TelaControle.this, "Falha na conexão ESP", Toast.LENGTH_LONG).show();
            }
        }else{
            imageConec.setImageResource(R.drawable.ic_desconec_mqtt);
            Toast.makeText(TelaControle.this, "Falha na conexão Firebase", Toast.LENGTH_LONG).show();
        }
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "ALERTA BARREIRA";
            String description = "Sensor de presença";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("sensor", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void creatNotification() {
        Intent intent = new Intent(this, TelaControle.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "sensor")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Sensor ativado")
                .setContentText("Sensor barreira Cozinha ativado")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(6565, builder.build());
    }

    public void readStatus() {
        presence.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                passed = Boolean.parseBoolean(dataSnapshot.getValue().toString());
                if (passed) {
                    creatNotification();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(TelaControle.this, "Desligar :" + error, Toast.LENGTH_SHORT).show();
            }
        });

        espConected.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                espCone = Boolean.parseBoolean(dataSnapshot.getValue().toString());
                firebaseConnected(reference.getKey());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(TelaControle.this, "Desligar :" + error, Toast.LENGTH_SHORT).show();
            }
        });

        tempRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TextView temp = findViewById(R.id.temp);
                temp.setText(dataSnapshot.getValue().toString().concat(" °C"));
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(TelaControle.this, "Desligar :" + error, Toast.LENGTH_SHORT).show();
            }
        });

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ImageView imageView = findViewById(R.id.viCozi);
                statusLamp = Boolean.parseBoolean(dataSnapshot.getValue().toString());
                if (statusLamp) {
                    imageView.setImageResource(R.drawable.ic_lamp_acs);
                } else {
                    imageView.setImageResource(R.drawable.ic_lamp_ico);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(TelaControle.this, "Desligar :" + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setSingleEvent(GridLayout mainGrid) {
        for (int i=0;i < mainGrid.getChildCount();i++){
            final CardView cardView = (CardView)mainGrid.getChildAt(i);
            final int finali = i;

            cardView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int action = event.getAction();
                    ImageView imageView = findViewById(R.id.viCozi);
                    ImageView imageView1 = findViewById(R.id.viQuar);
                    ImageView imageViewS = findViewById(R.id.viSair);

                    if (action == MotionEvent.ACTION_DOWN) {
                        v.setScaleX(0.95f);
                        v.setScaleY(0.95f);
                        cardView.setCardBackgroundColor(cardView.getResources().getColor(R.color.btnClicked));
                        if(finali == 0){
                            if(statusLamp){
                                imageView.setImageResource(R.drawable.ic_lamp_ico);
                                clicou = false;
                                myRef.setValue(false);
//                                Toast.makeText(TelaControle.this, "Desligar :" + finali, Toast.LENGTH_SHORT).show();
                            }else{
                                imageView.setImageResource(R.drawable.ic_lamp_acs);
                                clicou = true;
                                myRef.setValue(true);
//                                Toast.makeText(TelaControle.this, "Ligar :" + finali, Toast.LENGTH_SHORT).show();
                            }
                        }

                        if(finali == 1){
                            if(clicou){
                                imageView1.setImageResource(R.drawable.ic_lamp_ico);
                                clicou = false;
                                Toast.makeText(TelaControle.this, "Desligar :" + finali, Toast.LENGTH_SHORT).show();
                                creatNotification();
                            }else{
                                imageView1.setImageResource(R.drawable.ic_lamp_acs);
                                clicou = true;
                                Toast.makeText(TelaControle.this, "Ligar :" + finali, Toast.LENGTH_SHORT).show();
                                creatNotification();
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
                    } else if (action == MotionEvent.ACTION_UP) {
                        v.animate().cancel();
                        v.animate().scaleX(1f).setDuration(10).start();
                        v.animate().scaleY(1f).setDuration(10).start();
                        cardView.setCardBackgroundColor(Color.BLACK);
                    }
                    return true;
                }
            });
/*            cardView.setOnClickListener(new View.OnClickListener() {
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
            });*/
        }
    }

    private void logoutApp() {
        mAuth.getInstance().signOut();
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }

    public void pubSub(String status) {
        String message = status;
    }
}
