package footballcounter.project.footballcounterlive;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Spinner spinnerCountry;

    private AdView mAdView;
    private Document doc;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Pager adapter;
    private View v;
    private Thread secThread;
    private Runnable runnable;
    private String link_match;
    private String id_match;
    private String info_title;
    private String info_message;

    private TextView title;
    private int T;

    private Tab0 tab0_view;
    private Tab1 tab1_view;
    private DialogFragment InfoFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        YandexMetricaConfig config = YandexMetricaConfig.newConfigBuilder( "8c65e199-a402-4a23-b461-00e735a82b07" ).build();
//        // Initializing the AppMetrica SDK.
//        YandexMetrica.activate(getApplicationContext(), config);

        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, initializationStatus -> {
        });
        mAdView = (AdView) findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        viewPager = (ViewPager) findViewById(R.id.pager);
        title = (TextView) findViewById(R.id.title);

        adapter = new Pager(getSupportFragmentManager() );
        tab0_view = new Tab0();
        tab1_view = new Tab1();

        adapter.AddFragment(tab0_view, "Статистика");
        adapter.AddFragment(tab1_view, "Счётчик");
        adapter.AddFragment(new Tab2(), "Список игр");
        adapter.AddFragment(new Tab3(), "FAQ");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon( R.drawable.ic_statistica );
        tabLayout.getTabAt(1).setIcon( R.drawable.ic_timer );
        tabLayout.getTabAt(2).setIcon( R.drawable.ic_counter );
        tabLayout.getTabAt(3).setIcon( R.drawable.androidpit_6810 );

        runnable = new Runnable() {
            @Override
            public void run() {
                Loop();
            }
        };
        secThread = new Thread(runnable);
        secThread.start();

    }


    public void getLink(View view)
    {
//        получение ссылки матча при нажатии кнопки с названием матча, ссылка берётся из тега кнопки, который устанавливается при парсинге по главной ссылке ресурса
        id_match = view.getTag().toString();
        link_match = String.format( "https://line13.bkfon-resources.com/line/eventView?lang=ru&eventId=%s&scopeMarket=1600", view.getTag().toString() );

//        передача ссылки в первый фрагмент-счётчик, для дальнеёшего парсинга
        tab1_view.setLink(link_match, id_match);

    }

    public void Loop()
    {
        while (true)
        {

            String doc = null;
            try {
                Thread.sleep(1000);
                doc = Jsoup.connect(link_match).ignoreContentType(true).execute().body();
            } catch (Exception e) {
//                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        title.setText("Обновите ссылки матчей и выберите матч из списка.");
                    }
                });

                continue;
            }

            if (doc != null)
            {
                TitleSet(doc);
            }
            if (doc==null || doc.contains("Событие не найдено"))
            {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        title.setText("Матч закончился.\nОбновите ссылки матчей и выберите матч из списка.");
                    }
                });
            }
        }

    }

    public void TitleSet(String doc)
    {
        try {
            JSONObject root = new JSONObject(doc);
            JSONArray events = root.getJSONArray("events");
            final JSONObject main_event = events.getJSONObject(0);


            final String title_text = String.format("<sup><small>%s  %s  %s</sup></small><br>%s",

                    main_event.getString("team1"),
                    main_event.getString("score"),
                    main_event.getString("team2"),
                    main_event.getString("timer")
            );

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // обновление главного титульника с информацией о матче (таймер - команды - счёт) а так же передача таймера в первый фрагмент счётчик
                    title.setText( Html.fromHtml(title_text) );
                    try {
                        tab1_view.setTimer(main_event.getString("timer"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (Exception e)
        {
//
        }
    }

    public void infoMatch(View view)
    {

        final String link_match_info = String.format( "https://line13.bkfon-resources.com/line/eventView?lang=ru&eventId=%s&scopeMarket=1600", view.getTag().toString() );
        info_message = "";
        info_title = "Ошибка...";
        runnable = new Runnable() {
            @Override
            public void run() {
                getInfo(link_match_info);
            }
        };
        secThread = new Thread(runnable);
        secThread.start();
    }

    public void getInfo(String url){
        try {
            String doc_info = Jsoup.connect(url).ignoreContentType(true).execute().body();
            JSONObject root = new JSONObject(doc_info);
            JSONArray events = root.getJSONArray("events");

            final JSONObject main_event = events.getJSONObject(0);

            info_title = String.format("%s  %s  %s  -  %s",
                    main_event.getString("team1"),
                    main_event.getString("score"),
                    main_event.getString("team2"),
                    main_event.getString("timer")
            );

            for (int i = 0; i < events.length(); i++){

                try {
                    JSONObject event = events.getJSONObject(i);
                    if (event.getString("name").equals("угловые") ||
                            event.getString("name").equals("удары в створ") ||
                            event.getString("name").equals("удары от ворот") ||
                            event.getString("name").equals("фолы") ||
                            event.getString("name").equals("вброс аутов") ||
                            event.getString("name").equals("желтые карты") ||
                            event.getString("name").equals("офсайды"))
                    {
                        info_message += String.format("%s: %s\n", event.getString("name"), event.getString("score"));
                    }
                } catch (Exception e){
                    info_message = "Возможно матч закончился, обновите ссылки.";
                }
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle(info_title)
                            .setMessage(info_message)
                            .setIcon(R.drawable.ic_ball)
                            .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // Закрываем окно
                                    dialog.cancel();
                                }
                            }).create();
                    builder.show();
                }
            });

        } catch (Exception e){
//            Log.d("MyLog", String.valueOf(e));
        }
    }

    public void linkToBot(View v) {
        Uri address = Uri.parse("https://t.me/just_parsingBot");
        Intent openlink = new Intent(Intent.ACTION_VIEW, address);
        startActivity(openlink);
    }

}


