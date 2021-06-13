package footballcounter.project.footballcounterlive;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.google.android.material.snackbar.Snackbar;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.AEADBadTagException;

import at.markushi.ui.CircleButton;

public class Tab0 extends Fragment {

    private String URL_COUNTRY;
    private String URL_LEAGUE;
    private String URL_CLUB;
    private String CLUB = "";


    private Document RESP_0;
    private Document RESP_1;
    private Document RESP_2;
    private Document[] RESP_ARRAY_0;
    private Document[] RESP_ARRAY_1;
    private Document[] RESP_ARRAY_2;

    private ListView listViewScore;
    private ScoreArrayAdapter ScoreAdapter;
    private ArrayList<ScoreList> ScoreList;

    private ArrayList<String[]> allScore;
    private ArrayList<String[]> awayScore;
    private ArrayList<String[]> homeScore;
    private ArrayList<String[]> RESP_SCORE;

    private RadioButton check0_fullMatch;
    private RadioButton check1_firstTime;
    private RadioButton check2_secondTime;
    private RadioButton check3_allScore;
    private RadioButton check4_homeScore;
    private RadioButton check5_awayScore;
    private LinearLayout radiobtn;
    private LinearLayout headerlistView;

    private String TIME = "0";
    private String TYPE_SCORE = "allScore";

    private Document doc;
    private Thread secThread;
    private Runnable runnable;
    private View v;
    private ViewGroup headerView;

    private ArrayList<CustomScoreItem> mCountryList;
    private ArrayList<CustomScoreItem> mLeagueList;
    private ArrayList<CustomScoreItem> mClubList;

    private CustomScoreAdapter mAdapterCountry;
    private NumberProgressBar loadingScore;
    private int for_load;

    private Spinner spinnerCountry;
    private Spinner spinnerLeague;
    private Spinner spinnerClub;
    private CircleButton info_btn;

    LinearLayout leagueLinear;
    LinearLayout clubLinear;

    private String url_flashscore = "https://www.flashscore.ru";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.tab0, container, false);

        listViewScore = v.findViewById(R.id.listViewScore);
        headerView = (ViewGroup)getLayoutInflater().inflate(R.layout.headerlist_layout, listViewScore, false);
        listViewScore.addHeaderView( headerView );

        ScoreList = new ArrayList<>();
        ScoreAdapter = new ScoreArrayAdapter(getActivity(), R.layout.list_item, ScoreList, getLayoutInflater());
        listViewScore.setAdapter(ScoreAdapter);


        spinnerCountry = (Spinner) headerView.findViewById( R.id.spinner_country );
        spinnerLeague = (Spinner) headerView.findViewById( R.id.spinner_league );
        spinnerClub = (Spinner) headerView.findViewById( R.id.spinner_club );

        loadingScore = (NumberProgressBar) v.findViewById(R.id.loading_score_horizontal);

        leagueLinear = (LinearLayout) headerView.findViewById(R.id.league_linear);
        clubLinear = (LinearLayout) headerView.findViewById(R.id.club_linear);

        spinnerCountry.setPrompt("Выберите страну");
        spinnerClub.setPrompt("Выберите клуб");

        runnable = new Runnable() {
            @Override
            public void run() {
                getCountry();
            }
        };
        secThread = new Thread(runnable);
        secThread.start();

        mCountryList = new ArrayList<>();
        mClubList = new ArrayList<>();

        mCountryList.add( new CustomScoreItem("", "") );
        mClubList.add( new CustomScoreItem("", "") );

        mAdapterCountry = new CustomScoreAdapter(getActivity(), mCountryList);
        spinnerCountry.setAdapter(mAdapterCountry);

        spinnerClub.setAdapter( new CustomScoreAdapter(getActivity(), mClubList) );

        spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CustomScoreItem clickedItem = (CustomScoreItem) parent.getItemAtPosition(position);
                String clickedCountryName = clickedItem.getCustomName();
                final String clickedUrl = clickedItem.getCustomUrl();
                clubLinear.setVisibility( View.INVISIBLE );

                if (!clickedCountryName.equals("")){
                    leagueLinear.setVisibility( View.INVISIBLE );
                    spinnerLeague.setPrompt("Выберите лигу страны: " + clickedCountryName);

                    URL_COUNTRY = clickedUrl;
                    CLUB = "";

                    mLeagueList = new ArrayList<>();
                    mLeagueList.add( new CustomScoreItem("", "") );
                    spinnerLeague.setAdapter( new CustomScoreAdapter(getActivity(), mLeagueList) );

                    mClubList = new ArrayList<>();
                    mClubList.add( new CustomScoreItem("", "") );
                    spinnerClub.setAdapter( new CustomScoreAdapter(getActivity(), mClubList) );

                    Toast.makeText(getActivity(), clickedCountryName + " выбрана.", Toast.LENGTH_SHORT).show();

                    runnable = new Runnable() {
                        @Override
                        public void run() {
                            getLeague(clickedUrl);
                        }
                    };
                    secThread = new Thread(runnable);
                    secThread.start();

                } else {
                    leagueLinear.setVisibility( View.INVISIBLE );
                }
//                Log.d("MyLog", clickedItem.getCustomUrl());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerLeague.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CustomScoreItem clickedItem = (CustomScoreItem) parent.getItemAtPosition(position);
                String clickedLeagueName = clickedItem.getCustomName();
                final String clickedUrl = clickedItem.getCustomUrl();

                if (!clickedLeagueName.equals("")){
                    clubLinear.setVisibility( View.INVISIBLE );
                    spinnerClub.setPrompt("Выберите клуб лиги: " + clickedLeagueName);

                    URL_LEAGUE = clickedUrl;
                    CLUB = "";

                    mClubList = new ArrayList<>();
                    mClubList.add( new CustomScoreItem("", "") );
                    spinnerClub.setAdapter( new CustomScoreAdapter(getActivity(), mClubList) );

                    Toast.makeText(getActivity(), clickedLeagueName, Toast.LENGTH_SHORT).show();
                    runnable = new Runnable() {
                        @Override
                        public void run() {
                            getClub(clickedUrl);
                        }
                    };
                    secThread = new Thread(runnable);
                    secThread.start();

                } else {
                    clubLinear.setVisibility( View.INVISIBLE );
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerClub.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CustomScoreItem clickedItem = (CustomScoreItem) parent.getItemAtPosition(position);
                final String clickedClubName = clickedItem.getCustomName();
                final String clickedUrl = clickedItem.getCustomUrl();

                if (!clickedClubName.equals("")){
                    URL_CLUB = clickedUrl;
                    CLUB = clickedClubName;
                    Toast.makeText(getActivity(), clickedClubName, Toast.LENGTH_SHORT).show();
                    runnable = new Runnable() {
                        @Override
                        public void run() {
                            getClubResults(clickedClubName, clickedUrl);
                        }
                    };
                    secThread = new Thread(runnable);
                    secThread.start();

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        check0_fullMatch = (RadioButton) headerView.findViewById(R.id.full_time);
        check1_firstTime = (RadioButton) headerView.findViewById(R.id.first_time);
        check2_secondTime = (RadioButton) headerView.findViewById(R.id.second_time);
        radiobtn = (LinearLayout) headerView.findViewById(R.id.radiobtn);
        headerlistView = (LinearLayout) headerView.findViewById(R.id.headerlistView);

        check0_fullMatch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    TIME = "0";
                    check1_firstTime.setChecked(false);
                    check2_secondTime.setChecked(false);

                    runnable = new Runnable() {
                        @Override
                        public void run() {
                            try{
                                getClubResults(CLUB, URL_CLUB);
                            } catch (Exception e){
//
                            }
                        }
                    };
                    secThread = new Thread(runnable);
                    secThread.start();
                }
            }
        });

        check1_firstTime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    TIME = "1";
                    check0_fullMatch.setChecked(false);
                    check2_secondTime.setChecked(false);
                    runnable = new Runnable() {
                        @Override
                        public void run() {

                            try{
                                getClubResults(CLUB, URL_CLUB);
                            } catch (Exception e){
//
                            }
                        }
                    };
                    secThread = new Thread(runnable);
                    secThread.start();
                }
            }
        });

        check2_secondTime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    TIME = "2";
                    check0_fullMatch.setChecked(false);
                    check1_firstTime.setChecked(false);
                    runnable = new Runnable() {
                        @Override
                        public void run() {
                            try{
                                getClubResults(CLUB, URL_CLUB);
                            } catch (Exception e){
//
                            }

                        }
                    };
                    secThread = new Thread(runnable);
                    secThread.start();
                }
            }
        });

        check3_allScore = (RadioButton) headerView.findViewById(R.id.allScore);
        check4_homeScore = (RadioButton) headerView.findViewById(R.id.homeScore);
        check5_awayScore = (RadioButton) headerView.findViewById(R.id.awayScore);

        check3_allScore.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    TYPE_SCORE = "allScore";
                    check4_homeScore.setChecked(false);
                    check5_awayScore.setChecked(false);
                    runnable = new Runnable() {
                        @Override
                        public void run() {
                            try{
                                getClubResults(CLUB, URL_CLUB);
                            } catch (Exception e){
//
                            }
                        }
                    };
                    secThread = new Thread(runnable);
                    secThread.start();
                }
            }
        });

        check4_homeScore.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                   TYPE_SCORE = "homeScore";
                    check3_allScore.setChecked(false);
                    check5_awayScore.setChecked(false);
                    runnable = new Runnable() {
                        @Override
                        public void run() {
                            try{
                                getClubResults(CLUB, URL_CLUB);
                            } catch (Exception e){
//
                            }
                        }
                    };
                    secThread = new Thread(runnable);
                    secThread.start();
                }
            }
        });

        check5_awayScore.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    TYPE_SCORE = "awayScore";
                    check3_allScore.setChecked(false);
                    check4_homeScore.setChecked(false);
                    runnable = new Runnable() {
                        @Override
                        public void run() {
                            try{
                                getClubResults(CLUB, URL_CLUB);
                            } catch (Exception e){
//
                            }
                        }
                    };
                    secThread = new Thread(runnable);
                    secThread.start();
                }
            }
        });

        info_btn = (CircleButton) headerView.findViewById(R.id.info_btn);
        info_btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Snackbar.make(v, "Выберите страну, затем лигу и клуб. Загрузится статистика крайних сыграных матчей клуба.", 5000)
                            .setAction("Action", null)
                            .show();
                }
                return false;
            }
        });


        return v;
    }



    public void getCountry() {

        try {
            doc = Jsoup.connect(url_flashscore).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Elements liTag = doc.getElementsByTag("li");

        for (int i=0; i < liTag.size(); i++){
            try {
                String liTag_str = String.valueOf(liTag.get(i));
                Pattern pattern = Pattern.compile("lmenu_[0-9]+");
                Matcher matcher = pattern.matcher( liTag_str );
                if (matcher.find()) {
                    Elements lmenu = liTag.get(i).getElementById( matcher.group() ).select("a");
                    final String href = url_flashscore + lmenu.attr("href");
                    final String lmenu_text = lmenu.text().split(" ")[0];

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mCountryList.add(new CustomScoreItem( lmenu_text, href ));
                            }
                        });
                }
            } catch (Exception e) {
//                Log.d("MyLog", String.valueOf(e));
            }
        }

    }

    public void getLeague(String url) {

        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Elements selected_country_list = doc.getElementsByClass("menu selected-country-list");
        Elements ulTag = selected_country_list.get(0).children();

        for (int i=0; i<ulTag.size(); i++){
            try {
                Elements aTag = ulTag.get(i).select("a");
                final String href = aTag.attr("href");
                final String href_text = aTag.text().split("  ")[0];

                if (!href_text.equals("")){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mLeagueList.add(new CustomScoreItem( href_text, url_flashscore + href ));
                        }
                    });
                }

            } catch (Exception e) {
//
            }
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                leagueLinear.setVisibility( View.VISIBLE );
            }
        });
    }

    public void getClub(String url) {
        Document resp = null;
        try {
            resp = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Pattern pattern = Pattern.compile("\"tournament\":\"[\\w]+\",\"tournamentStage\":\"[\\w]+\"");
        Matcher matcher = pattern.matcher( String.valueOf(resp) );

        String matcherTag1 = "";
        String matcherTag2 = "";

        if (matcher.find()){
            matcherTag1 = matcher.group()
                    .split(",")[0]
                    .split(":")[1]
                    .replace("\"","");

            matcherTag2 = matcher.group()
                    .split(",")[1]
                    .split(":")[1]
                    .replace("\"","");

            if( String.valueOf(resp).contains("Главный турнир") ){
                matcherTag2 = String.valueOf(resp)
                        .split("\">Главный турнир")[0]
                        .split("&ts=")[1];
            }
        }

        String URL = String.format("https://d.flashscore.ru/x/feed/ss_1_%s_%s_table_overall", matcherTag1, matcherTag2);

        Document RESP = null;

        String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.92 YaBrowser/19.10.0.1522 Yowser/2.5 Safari/537.36";

        Map<String, String> headerMap = new HashMap<String, String>();

        headerMap.put("Accept", "*/*");
        headerMap.put("Accept-Language", "*");
        headerMap.put("Referer", "https://d.flashscore.ru/x/feed/proxy-local");
        headerMap.put("Sec-Fetch-Mode", "cors");
        headerMap.put("X-Fsign", "SW9D1eZo");
        headerMap.put("X-GeoIP", "1");
        headerMap.put("X-Requested-With", "XMLHttpRequest");

        try {
            RESP = Jsoup.connect(URL)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.132 Safari/537.36")
                    .headers(headerMap)
                    .get();

        } catch (IOException e) {
            e.printStackTrace();
        }
        Pattern pattern_2 = Pattern.compile("\\/team\\/[\\D\\w]+\\/[\\w]+\\/");
        Elements team_name_span = RESP.getElementsByClass("team_name_span");

        for (int i=0; i<team_name_span.size(); i++){
            Elements a_onclick = team_name_span.get(i).select("a");

            String onclick = a_onclick.attr("onclick");
            String onclick_text = a_onclick.text();

            Matcher matcher_2 = pattern_2.matcher( onclick );

            if (matcher_2.find()) {
                final String link_for_team = url_flashscore + matcher_2.group()
                        .split(":")[0]
                        .replace("')", "") + "results/";

                final String name_team = onclick_text;

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mClubList.add(new CustomScoreItem( name_team, link_for_team ));
                    }
                });
            }
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                clubLinear.setVisibility( View.VISIBLE );
            }
        });
    }

    public void getClubResults(String CLUB, String url) {
//        Log.d("MyLog", url);
        for_load = 0;
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Element participant_page_data_results = doc.getElementById("participant-page-data-results");

        String[] game = participant_page_data_results.text()
                .replace("~AA÷", "\n\n")
                .replace("¬AD÷","\n\n")
                .split("\n\n");

//        Log.d("MyLog", String.valueOf(doc));
        allScore = new ArrayList<String[]>();
        awayScore = new ArrayList<String[]>();
        homeScore = new ArrayList<String[]>();

        String access_string = "";

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadingScore.setVisibility(View.VISIBLE);
                loadingScore.setProgress(0);
            }
        });

        for(int i=0; i < game.length; i++){

            if (game[i].length()==8) {

                String[] subAll = new String[5];

                subAll[0] = String.format( "https://flashscore.ru/match/%s/#match-summary", game[i] );
                subAll[1] = String.format( "https://d.flashscore.ru/x/feed/d_su_%s_ru_1", game[i] );
                subAll[2] = String.format( "https://d.flashscore.ru/x/feed/d_st_%s_ru_1", game[i] );
                subAll[3] = String.format( "https://d.flashscore.ru/x/feed/d_od_%s_ru_1_eu", game[i] );
                subAll[4] = String.format( "https://d.flashscore.ru/x/feed/d_li_%s_ru_1", game[i] );

                allScore.add( String.format("%s,%s,%s,%s,%s", subAll[0],subAll[1],subAll[2],subAll[3],subAll[4]).split(",") ) ;
            }
            
            if ( game[i].contains( String.format( "AF÷%s¬JB", CLUB ) )){

                String[] subAway = new String[5];
                access_string += String.format( "%s\n", game[i-1] );

                subAway[0] = String.format( "https://flashscore.ru/match/%s/#match-summary", game[i-1] );
                subAway[1] = String.format( "https://d.flashscore.ru/x/feed/d_su_%s_ru_1", game[i-1] );
                subAway[2] = String.format( "https://d.flashscore.ru/x/feed/d_st_%s_ru_1", game[i-1] );
                subAway[3] = String.format( "https://d.flashscore.ru/x/feed/d_od_%s_ru_1_eu", game[i-1] );
                subAway[4] = String.format( "https://d.flashscore.ru/x/feed/d_li_%s_ru_1", game[i-1] );

                awayScore.add( String.format("%s,%s,%s,%s,%s", subAway[0],subAway[1],subAway[2],subAway[3],subAway[4]).split(",") ) ;
            }

        }

        for(int i=0; i < game.length; i++){
            if ( game[i].length()==8 && !access_string.contains(game[i].split(" ")[0]) )
            {
                    String[] subHome = new String[5];
                    subHome[0] = String.format("https://flashscore.ru/match/%s/#match-summary", game[i]);
                    subHome[1] = String.format("https://d.flashscore.ru/x/feed/d_su_%s_ru_1", game[i]);
                    subHome[2] = String.format("https://d.flashscore.ru/x/feed/d_st_%s_ru_1", game[i]);
                    subHome[3] = String.format("https://d.flashscore.ru/x/feed/d_od_%s_ru_1_eu", game[i]);
                    subHome[4] = String.format("https://d.flashscore.ru/x/feed/d_li_%s_ru_1", game[i]);

                    homeScore.add(String.format("%s,%s,%s,%s,%s", subHome[0], subHome[1], subHome[2], subHome[3], subHome[4]).split(","));

            }
        }

        getParseStats();
    }

    public void getParseStats(){

        if(TYPE_SCORE.equals("allScore")){
            RESP_SCORE = allScore;
        }
        if(TYPE_SCORE.equals("homeScore")){
            RESP_SCORE = homeScore;
        }
        if(TYPE_SCORE.equals("awayScore")){
            RESP_SCORE = awayScore;
        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ScoreList = new ArrayList<>();
                RESP_ARRAY_2 = new Document[RESP_SCORE.size()];
                RESP_ARRAY_1 = new Document[RESP_SCORE.size()];
                RESP_ARRAY_0 = new Document[RESP_SCORE.size()];
                ScoreAdapter = new ScoreArrayAdapter(getActivity(), R.layout.list_item, ScoreList, getLayoutInflater());
                headerlistView.setVisibility(View.INVISIBLE);
//                radiobtn.setVisibility(View.INVISIBLE);
                listViewScore.setAdapter(ScoreAdapter);

            }
        });

        String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.92 YaBrowser/19.10.0.1522 Yowser/2.5 Safari/537.36";

        final Map<String, String> headerMap = new HashMap<String, String>();

        headerMap.put("Accept", "*/*");
        headerMap.put("Accept-Language", "*");
        headerMap.put("Referer", "https://d.flashscore.ru/x/feed/proxy-local");
        headerMap.put("Sec-Fetch-Mode", "cors");
        headerMap.put("X-Fsign", "SW9D1eZo");
        headerMap.put("X-GeoIP", "1");
        headerMap.put("X-Requested-With", "XMLHttpRequest");



        for (int i=0; i < RESP_SCORE.size(); i++) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            final int index = i;

            Executor executor = (runnable) -> {
                new Thread(runnable).start();
            };

            Runnable task_0 = () -> {
                try {
                    RESP_0 = Jsoup.connect(RESP_SCORE.get(index)[0])
                            .headers(headerMap)
                            .get();
                    RESP_ARRAY_0[index] = RESP_0;

                    RESP_1 = Jsoup.connect(RESP_SCORE.get(index)[1])
                            .headers(headerMap)
                            .get();
                    RESP_ARRAY_1[index] = RESP_1;

                    RESP_2 = Jsoup.connect(RESP_SCORE.get(index)[2])
                            .headers(headerMap)
                            .get();
                    RESP_ARRAY_2[index] = RESP_2;
                } catch (Exception e) {
//                    Log.d("MyLog", String.valueOf(e));
                }
            };
//            Runnable task_1 = () -> {
//                try {
//                    RESP_1 = Jsoup.connect(RESP_SCORE.get(index)[1])
//                            .headers(headerMap)
//                            .get();
//                    RESP_ARRAY_1[index] = RESP_1;
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            };
//            Runnable task_2 = () -> {
//                try {
//                    RESP_2 = Jsoup.connect(RESP_SCORE.get(index)[2])
//                            .headers(headerMap)
//                            .get();
//                    RESP_ARRAY_2[index] = RESP_2;
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            };

            executor.execute(task_0);
//            executor.execute(task_1);
//            executor.execute(task_2);

    }


        for (int i=0; i < RESP_SCORE.size(); i++) {

            final int index = i;

            for_load = i + 1;
            for (int per = loadingScore.getProgress() + 1; per < Math.round(for_load * (1000 / RESP_SCORE.size())); per++) {
                final int finalPer = per;
                try {
                    Thread.sleep(0, 10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadingScore.setProgress(finalPer);
                    }
                });
            }


            boolean wait_resp = true;

            while (wait_resp) {
                if (RESP_ARRAY_0[i] != null && RESP_ARRAY_1[i] != null && RESP_ARRAY_2[i] != null) {
                    wait_resp = false;
                }
            }

                String replace_text = "";
                try {
                    Elements info_status_mstat = RESP_ARRAY_0[i].getElementsByClass("info-status mstat");
                    replace_text = info_status_mstat.get(0).text();
                } catch (Exception e) {
                    replace_text = "";
                }


                Elements team_primary_content = RESP_ARRAY_0[i].getElementsByClass("team-primary-content");

                Elements description__country = RESP_ARRAY_0[i].getElementsByClass("description__country");

                Element description__time = RESP_ARRAY_0[i].getElementById("utime");

                String info_bubble_text = "";

                try {
                    Elements info_bubble = RESP_ARRAY_0[i].getElementsByClass("info-bubble");
                    info_bubble_text = "\n" + info_bubble.get(0).text();
                } catch (Exception e) {
                    info_bubble_text = "";
                }

                String titleTextMatch = String.valueOf(Html.fromHtml(String.format("%s<sup>%s</sup><br>%s %s",
                        description__country.get(0).text(),
                        description__time.text(),
                        team_primary_content.get(0).text().replace(replace_text, ""),
                        info_bubble_text)));

                String statText__allValue = "";
                String statText__homeValue = "";
                String statText__titleValue = "";
                String statText__awayValue = "";


                try {
                    Element tab_statistics = RESP_ARRAY_2[i].getElementById(String.format("tab-statistics-%s-statistic", TIME));
                    Elements statRow = tab_statistics.getElementsByClass("statTextGroup");

                    for (int j = 0; j < statRow.size(); j++) {
                        Elements statText = statRow.get(j).select("div");
                        if (j + 1 == statRow.size()) {
                            statText__allValue += statText.get(0).text();
                            statText__homeValue += statText.get(1).text();
                            statText__titleValue += statText.get(2).text();
                            statText__awayValue += statText.get(3).text();
                        } else {
                            statText__allValue += statText.get(0).text() + "\n";
                            statText__homeValue += statText.get(1).text() + "\n";
                            statText__titleValue += statText.get(2).text() + "\n";
                            statText__awayValue += statText.get(3).text() + "\n";
                        }

                    }
                } catch (Exception e) {
                    statText__allValue = "Нет данных по статистике";
                    statText__titleValue = "Нет данных по статистике";
                }

            Elements detailMS = RESP_ARRAY_1[i].getElementsByClass("detailMS");

            String titleTextTime1 = "";
            String titleTextTime2 = "";

            String homeText1 = "";
            String homeText2 = "";

            String awayText1 = "";
            String awayText2 = "";

            String subTime = "1";

            for(int j=0; j < detailMS.get(0).childrenSize(); j++){

                if( detailMS.get(0).child(j).toString().contains("1-й тайм") ){
                    titleTextTime1 = detailMS.get(0).child(j).text();
                    continue;
                }

                if( detailMS.get(0).child(j).toString().contains("2-й тайм") ){
                    titleTextTime2 = detailMS.get(0).child(j).text();
                    subTime = "2";
                }

                if( subTime.equals("1") ){
                    if( detailMS.get(0).child(j).toString().contains("home odd") ) {
                        if (detailMS.get(0).child(j).toString().contains("icon-box soccer-ball")) {
                            homeText1 += "\n" + detailMS.get(0).child(j).text() + "  ⚽";
                            awayText1 += "\n";
                            continue;
                        }
                        if( detailMS.get(0).child(j).toString().contains("substitution") ){
                            homeText1 += "\n" + detailMS.get(0).child(j).text() + "  \uD83D\uDD04";
                            awayText1 += "\n";
                            continue;
                        }
                        if( detailMS.get(0).child(j).toString().contains("icon y-card") ){
                            homeText1 += "\n" + detailMS.get(0).child(j).text() + "  ⚠️";
                            awayText1 += "\n";
                            continue;
                        }
                        if( detailMS.get(0).child(j).toString().contains("icon r-card") ){
                            homeText1 += "\n" + detailMS.get(0).child(j).text() + "  \uD83D\uDD34";
                            awayText1 += "\n";
                            continue;
                        }
                    }

                    if( detailMS.get(0).child(j).toString().contains("away even") ) {
                        if (detailMS.get(0).child(j).toString().contains("icon-box soccer-ball")) {
                            awayText1 += "\n" + detailMS.get(0).child(j).text() + "  ⚽";
                            homeText1 += "\n";
                            continue;
                        }
                        if( detailMS.get(0).child(j).toString().contains("substitution") ){
                            awayText1 += "\n" + detailMS.get(0).child(j).text() + "  \uD83D\uDD04";
                            homeText1 += "\n";
                            continue;
                        }
                        if( detailMS.get(0).child(j).toString().contains("icon y-card") ){
                            awayText1 += "\n" + detailMS.get(0).child(j).text() + "  ⚠️";
                            homeText1 += "\n";
                            continue;
                        }
                        if( detailMS.get(0).child(j).toString().contains("icon r-card") ){
                            awayText1 += "\n" + detailMS.get(0).child(j).text() + "  \uD83D\uDD34";
                            homeText1 += "\n";
                            continue;
                        }
                    }

                } else {

                    if( detailMS.get(0).child(j).toString().contains("home odd") ) {
                        if (detailMS.get(0).child(j).toString().contains("icon-box soccer-ball")) {
                            homeText2 += "\n" + detailMS.get(0).child(j).text() + "  ⚽";
                            awayText2 += "\n";
                            continue;
                        }
                        if( detailMS.get(0).child(j).toString().contains("substitution") ){
                            homeText2 += "\n" + detailMS.get(0).child(j).text() + "  \uD83D\uDD04";
                            awayText2 += "\n";
                            continue;
                        }
                        if( detailMS.get(0).child(j).toString().contains("icon y-card") ){
                            homeText2 += "\n" + detailMS.get(0).child(j).text() + "  ⚠️";
                            awayText2 += "\n";
                            continue;
                        }
                        if( detailMS.get(0).child(j).toString().contains("icon r-card") ){
                            homeText2 += "\n" + detailMS.get(0).child(j).text() + "  \uD83D\uDD34";
                            awayText2 += "\n";
                            continue;
                        }
                    }

                    if( detailMS.get(0).child(j).toString().contains("away even") ) {
                        if (detailMS.get(0).child(j).toString().contains("icon-box soccer-ball")) {
                            awayText2 += "\n" + detailMS.get(0).child(j).text() + "  ⚽";
                            homeText2 += "\n";
                            continue;
                        }
                        if( detailMS.get(0).child(j).toString().contains("substitution") ){
                            awayText2 += "\n" + detailMS.get(0).child(j).text() + "  \uD83D\uDD04";
                            homeText2 += "\n";
                            continue;
                        }
                        if( detailMS.get(0).child(j).toString().contains("icon y-card") ){
                            awayText2 += "\n" + detailMS.get(0).child(j).text() + "  ⚠️";
                            homeText2 += "\n";
                            continue;
                        }
                        if( detailMS.get(0).child(j).toString().contains("icon r-card") ){
                            awayText2 += "\n" + detailMS.get(0).child(j).text() + "  \uD83D\uDD34";
                            homeText2 += "\n";
                            continue;
                        }
                    }
                }

            }

            ScoreList items = new ScoreList();

            items.setAllText(statText__titleValue);
            items.setHomeText(statText__homeValue);
            items.setAwayText(statText__awayValue);

            items.setTitleTextTime1(titleTextTime1);
            items.setTitleTextTime2(titleTextTime2);

            items.setHomeTextTime1(homeText1);
            items.setHomeTextTime2(homeText2);

            items.setAwayTextTime1(awayText1);
            items.setAwayTextTime2(awayText2);

            items.setTitleTextMatch(titleTextMatch);
            ScoreList.add(items);

        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run()
            {
                loadingScore.setVisibility(View.INVISIBLE);
                loadingScore.setProgress(0);
                ScoreAdapter.notifyDataSetChanged();
                headerlistView.setVisibility(View.VISIBLE);
//                radiobtn.setVisibility(View.VISIBLE);
            }
        });
    }

}


//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        });