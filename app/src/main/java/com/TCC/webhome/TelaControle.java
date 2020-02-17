package com.TCC.webhome;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;


public class TelaControle extends AppCompatActivity {
    boolean clicou = false;
    private FirebaseAuth mAuth;
    GridLayout mainGrid;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_controle);

        context = this.getApplicationContext();
        conectaMqtt();

        mainGrid = findViewById(R.id.mainGrid);

        setSingleEvent(mainGrid);

    }

    public void conectaMqtt(){
        final ImageView imageConec = findViewById(R.id.imageConec);
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
                            if(clicou){
                                imageView.setImageResource(R.drawable.ic_lamp_ico);
                                clicou = false;
                                pubSub("desliga");
//                                Toast.makeText(TelaControle.this, "Desligar :" + finali, Toast.LENGTH_SHORT).show();
                            }else{
                                imageView.setImageResource(R.drawable.ic_lamp_acs);
                                clicou = true;
                                pubSub("liga");
//                                Toast.makeText(TelaControle.this, "Ligar :" + finali, Toast.LENGTH_SHORT).show();
                            }
                        }

                        if(finali == 1){
                            if(clicou){
                                imageView1.setImageResource(R.drawable.ic_lamp_ico);
                                clicou = false;
                                Toast.makeText(TelaControle.this, "Desligar :" + finali, Toast.LENGTH_SHORT).show();
                            }else{
                                imageView1.setImageResource(R.drawable.ic_lamp_acs);
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
        String topic = "esp8266/pincmd";
        String message = status;
    }
}
