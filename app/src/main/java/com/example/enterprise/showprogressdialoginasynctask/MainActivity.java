package com.example.enterprise.showprogressdialoginasynctask;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.os.Handler;
import android.app.ProgressDialog;


public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the widgets reference from XML layout
        final RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl);
        final Button btn = (Button) findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Execute the async task
                new ProgressTask().execute();
            }
        });
    }

    private class ProgressTask extends AsyncTask<Void,Void,Void>{
        private int progressStatus=0;
        private Handler handler = new Handler();

        // Initialize a new instance of progress dialog
        private ProgressDialog pd = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pd.setIndeterminate(false);

            // Set progress style horizontal
            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

            // Set the progress dialog background color
            pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.YELLOW));

            // Make the progress dialog cancellable
            pd.setCancelable(true);
            // Set the maximum value of progress
            pd.setMax(100);
            // Finally, show the progress dialog
            pd.show();
        }

        @Override
        protected Void doInBackground(Void...args){
            // Set the progress status zero on each button click
            progressStatus = 0;

            // Start the lengthy operation in a background thread
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while(progressStatus < 100){
                        // Update the progress status
                        progressStatus +=1;

                        // Try to sleep the thread for 20 milliseconds
                        try{
                            Thread.sleep(20);
                        }catch(InterruptedException e){
                            e.printStackTrace();
                        }

                        // Update the progress bar
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                // Update the progress status
                                pd.setProgress(progressStatus);
                                // If task execution completed
                                if(progressStatus == 100){
                                    // Dismiss/hide the progress dialog
                                    pd.dismiss();
                                }
                            }
                        });
                    }
                }
            }).start(); // Start the operation

            return null;
        }

        protected void onPostExecute(){
            // do something after async task completed.
        }
    }
}
