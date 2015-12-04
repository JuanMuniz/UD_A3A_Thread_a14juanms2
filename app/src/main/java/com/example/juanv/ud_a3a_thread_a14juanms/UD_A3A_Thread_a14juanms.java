package com.example.juanv.ud_a3a_thread_a14juanms;

import java.lang.ref.WeakReference;
import java.util.Random;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class UD_A3A_Thread_a14juanms extends Activity {
    Random r=new Random();
    TextView mTextField;
    TextView txtNumero;
    int numero;
    int numerito;
    boolean fin= false;
    private final int TEMPO_CRONO = 20;
    private task t;
    private Thread meuFio;
    private ClassPonte ponte = new ClassPonte(this);


    // INICO DA CLASE HANDLER
    private static class ClassPonte extends Handler {

        private WeakReference<UD_A3A_Thread_a14juanms> mTarget = null;

        ClassPonte(UD_A3A_Thread_a14juanms target) {
            mTarget = new WeakReference<UD_A3A_Thread_a14juanms>(target);
        }

        @Override
        public void handleMessage(Message msg) {

            UD_A3A_Thread_a14juanms target = mTarget.get();
            TextView texto1 = (TextView) target
                    .findViewById(R.id.mTextField);

            if (msg.arg2==1){
                Toast.makeText(target.getApplicationContext(), "ACABOUSE O CRONO", Toast.LENGTH_LONG).show();
                texto1.setText(String.valueOf(0));
            }
            else {
                texto1.setText(String.valueOf(msg.arg1));
            }
        }
    };
    // Fin do Handler




    private class MeuFio extends Thread {

        public void run() {
            for (int a = TEMPO_CRONO; a >=0; a--) {
                if (fin){
                    break;
                }
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.arg1 = a;
                    numero=a;
                    ponte.sendMessage(msg);
                } catch (InterruptedException e) {

                    e.printStackTrace();
                }
            }
            Message msgFin = new Message();
            msgFin.arg2 = 1;
            ponte.sendMessage(msgFin);
        }       // FIN DO RUN
    }

    ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ud__a3_a__thread_a14juanms);
        txtNumero=(TextView)findViewById(R.id.txtNumero);

        //INICIAR CRONO THREAD
        Button btnCrono = (Button) findViewById(R.id.btnCrono);
        btnCrono.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (((meuFio == null) || (!meuFio.isAlive())) && ((t==null) || (t.getStatus()==AsyncTask.Status.FINISHED))) {
                    fin = false;
                    txtNumero.setText("");
                    numerito = r.nextInt(6) + 5;
                    txtNumero.setText("" + numerito);
                    Toast.makeText(getApplicationContext(), "INICIANDO FIO", Toast.LENGTH_LONG).show();
                    meuFio = new MeuFio();
                    meuFio.start();
                } else {
                    Toast.makeText(getApplicationContext(), "NON TE DEIXO INICIAR O FIO ATA QUE REMATE :)", Toast.LENGTH_LONG).show();
                }
            }
        });
        //PARAR CRONO THREAD
        Button btnParar=(Button) findViewById(R.id.btnParar);
        btnParar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                fin = true;
                if (numero == numerito) {
                    Toast.makeText(UD_A3A_Thread_a14juanms.this, "ACERTACHES!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //INICIAR CRONO ASYNC
        Button btnCronoAsync = (Button)findViewById(R.id.btnCronoAsync);
        btnCronoAsync.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (((t==null) || (t.getStatus()==AsyncTask.Status.FINISHED)) && ((meuFio==null) ||  meuFio.isAlive()==false)){
                    txtNumero.setText("");
                    numerito=r.nextInt(6)+5;
                    txtNumero.setText(""+numerito);
                    t= new task();
                    t.execute();

                }
                else {
                    Toast.makeText(getApplicationContext(), "A tarefa non acabou!!!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
       //CANCELAR CRONO ASYNC
        Button btnCancelarAsync = (Button)findViewById(R.id.btnPararAsync);
        btnCancelarAsync.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (t.getStatus() == AsyncTask.Status.RUNNING) {
                    t.cancel(true);
                }
            }

        });
    }

    //CLASE ASYNCTASK
    private class task extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            for (int i = TEMPO_CRONO; i >=0; i--) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                Message msg = new Message();
                msg.arg1 = i;
                numero=i;
                ponte.sendMessage(msg);

                if (isCancelled())
                    break;
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                Toast.makeText(getApplicationContext(), "Crono finalizado!",
                        Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            if (numero == numerito) {
                Toast.makeText(UD_A3A_Thread_a14juanms.this, "ACERTACHES!!!", Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(getApplicationContext(), "Crono cancelado!",
                    Toast.LENGTH_SHORT).show();
        }
    };






}