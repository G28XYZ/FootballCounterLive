package footballcounter.project.footballcounterlive;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.timqi.sectorprogressview.ColorfulRingProgressView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class Tab2 extends Fragment {

    private Document doc;
    private Thread secThread;
    private Runnable runnable;

    private ListView listView;
    private CustomArrayAdapter adapter;
    private ArrayList<ListItemClass> arrayList;

    private Bundle SAVEINSTANCE;

    public String url_events = "https://line13.bkfon-resources.com/line/mobile/showEvents?lang=ru&lineType=live&skId=1&scopeMarket=1600";
    private List linkGame;
    private Object JSONException;
    public GifImageView updateButton;
    private boolean worker = true;
    private com.daimajia.numberprogressbar.NumberProgressBar Load_horizontal;
    private com.timqi.sectorprogressview.ColorfulRingProgressView Load_round;

    View v ;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        v = inflater.inflate(R.layout.tab2, container, false);
        updateButton = (GifImageView) v.findViewById(R.id.refresh_btn);
        Load_horizontal = (NumberProgressBar) v.findViewById(R.id.loading_horizontal);
        Load_round = (ColorfulRingProgressView) v.findViewById(R.id.loading_round);

//        final TextView updateBox = (TextView) v.findViewById(R.id.textBox);

        updateButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                updateButton.setBackgroundResource( R.drawable.ref_on_press );
                return false;
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateButton.setBackgroundResource( R.drawable.for_ref_btn );
                init();
            }
        });

        return v;
    }

    public void init()
    {

        linkGame = new ArrayList();
        listView = v.findViewById(R.id.list_View);
        arrayList = new ArrayList<>();
        adapter = new CustomArrayAdapter(getActivity(), R.layout.list_item, arrayList, getLayoutInflater());
        listView.setAdapter(adapter);
        runnable = new Runnable() {
            @Override
            public void run() {
                if (worker) {
                    getWeb();
                }
            }
        };
        secThread = new Thread(runnable);
        secThread.start();
    }

    public void getWeb()
    {

        worker = false;

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Load_horizontal.setVisibility(View.VISIBLE);
                updateButton.setVisibility(View.INVISIBLE);
                Load_horizontal.setProgress(0);
                Load_round.setPercent(0);
            }
        });

        int count = 0;
        try {
            String doc = Jsoup.connect(url_events).ignoreContentType(true).execute().body();
            JSONObject root = new JSONObject(doc);
            final JSONArray events = root.getJSONArray("events");

            for (int i = 0; i < events.length(); i++)
            {
                final JSONObject event = events.getJSONObject(i);
                final int for_load = i+1;
                for (int per=Load_horizontal.getProgress()+1; per < Math.round(for_load*(1000/events.length())); per++)
                {
                    final int finalPer = per;
                    Thread.sleep(0,10);
                    getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Load_horizontal.setProgress(finalPer);
                        Load_round.setPercent(finalPer);

                    }
                });
                 }

                try {
                    if (event.getInt("parentId") == 0) {
                        count += 1;
                        String id = event.getString("id");
                        String team1 = event.getString("team1");
                        String team2 = event.getString("team2");
                        String score = event.getString("score");

                        ListItemClass items = new ListItemClass();
                        items.setData_1(String.format("%s  %s  %s", team1, score, team2));
                        items.setData_3(String.format("%s", id));
                        items.setTag_1(String.format("%s", id));
                        items.setTag_2(String.format("%s", id));

                        items.setCountLink(count);

                        if (checkStat(String.format("https://line13.bkfon-resources.com/line/eventView?lang=ru&eventId=%s&scopeMarket=1600", id))) {
                            int color = ContextCompat.getColor(getActivity(), R.color.MyColorGreen);
                            Drawable draw = VectorDrawableCompat.create(getResources(), R.drawable.style_for_linkmatch_green, null);
                            items.setBackgr(draw);
                            items.setTextColor(color);

                        } else {
                            int color = ContextCompat.getColor(getActivity(), R.color.MyColorOrange);
                            Drawable draw = VectorDrawableCompat.create(getResources(), R.drawable.style_for_linkmatch_red, null);
                            items.setBackgr(draw);
                            items.setTextColor(color);
                        }

//                                            Log.d( "MyLog", String.format("%s  %s  %s  %s", team1, score, team2, id) );

                        arrayList.add(items);
                    }
                } catch (Exception e){
                    continue;
                }
            }

//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    adapter.notifyDataSetChanged();
//                }
//            });
        } catch (Exception e) {
//            e.printStackTrace();

        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
                Load_horizontal.setVisibility(View.INVISIBLE);
                updateButton.setVisibility(View.VISIBLE);
                Load_horizontal.setProgress(0);
                Load_round.setPercent(0);
            }
        });
        worker = true;
    }

    private boolean checkStat(String url) {

        String link_match = null;
        try {
            link_match = Jsoup.connect(url).ignoreContentType(true).execute().body();
        } catch (Exception e) {
//            e.printStackTrace();
        }

        if (link_match!=null) {
            if (link_match.contains("угловые") ||
                    link_match.contains("удары в створ") ||
                    link_match.contains("удары от ворот") ||
                    link_match.contains("фолы") ||
                    link_match.contains("вброс аутов"))
            {
                return true;
            }
        }

        return false;
    }


}