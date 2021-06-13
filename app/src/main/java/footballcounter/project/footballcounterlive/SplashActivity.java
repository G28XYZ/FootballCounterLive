package footballcounter.project.footballcounterlive;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.race604.drawable.wave.WaveDrawable;

public class SplashActivity extends AppCompatActivity {
    private int SPLASH_TIME = 3500;
    private Thread secThread;
    private Runnable runnable;
    private View v;
    private NumberProgressBar Load_horizontal;
    private int p0 = (int) System.currentTimeMillis();
    private TextView disklaimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        disklaimer = findViewById(R.id.disclaimer);
        disklaimer.setText(Html.fromHtml("<b>Дисклеймер</b><br><br>" +
                "                               <i>Вся информация в приложении имеет ознакомительный характер.<br>" +
                "                               Информация предоставляется в развлекательных целях.</i>"));

        Load_horizontal = findViewById(R.id.loading_horizontal_splash);
        Load_horizontal.setProgress(0);

        runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Loading();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        secThread = new Thread(runnable);
        secThread.start();

        ImageView imageView = (ImageView) findViewById(R.id.gifImageView);
        WaveDrawable mWaveDrawable = new WaveDrawable(this, R.drawable.fcl_splash);
        imageView.setImageDrawable(mWaveDrawable);

        mWaveDrawable.setIndeterminate(true);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent splashIntent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(splashIntent);
                finish();
            }
        }, SPLASH_TIME);
    }

    public void Loading() throws InterruptedException {

        while ( ((int) System.currentTimeMillis() - p0) < SPLASH_TIME ){
            Thread.sleep(1);
            final int finalPer = (int) System.currentTimeMillis() - p0;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Load_horizontal.setProgress(finalPer);
                }
            });
        }
    }

}