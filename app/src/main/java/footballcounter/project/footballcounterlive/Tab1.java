package footballcounter.project.footballcounterlive;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;

public class Tab1 extends Fragment {

    View v ;

    private int count = 0;

    private Thread secThread;
    private Runnable runnable;

    private String link_match;
    private String id_match;
    private int T;
    private String scorecomment;

    private LinearLayout layout_corner;
    private LinearLayout layout_foul;
    private LinearLayout layout_sot;
    private LinearLayout layout_sat;
    private LinearLayout layout_out;

    private TextView total_corner;
    private TextView total_foul;
    private TextView total_sot;
    private TextView total_sat;
    private TextView total_out;

    private TextView title_corner;
    private TextView title_foul;
    private TextView title_sot;
    private TextView title_sat;
    private TextView title_out;

    private Button corner;
    private Button out;
    private Button sat;
    private Button sot;
    private Button foul;


    private Button corner_count;
    private Button out_count;
    private Button sat_count;
    private Button sot_count;
    private Button foul_count;


    private EditText Ecorner;
    private EditText Eout;
    private EditText Esat;
    private EditText Esot;
    private EditText Efoul;

//    private CheckBox foul_check;
//    private CheckBox sat_check;
//    private CheckBox sot_check;
//    private CheckBox corner_check;
//    private CheckBox out_check;

    private ProgressBar corner_progress;
    private ProgressBar foul_progress;
    private ProgressBar sat_progress;
    private ProgressBar sot_progress;
    private ProgressBar out_progress;

    private ArrayList list_param;
    private ArrayList for_corner;
    private ArrayList for_out;
    private ArrayList for_sot;
    private ArrayList for_sat;
    private ArrayList for_foul;

    private String Timer;
    private List<String> index = Arrays.asList("угловые", "вброс аутов", "фолы", "удары в створ", "удары от ворот");

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.tab1, container, false);

//        обьявление переменных для виджетов из фрагмента счётчика и добавление в арайлисты,
//        чтобы циклом for пройтись по списку и достать нужный элементы и записать расчёты если условия совпадут

        list_param = new ArrayList();
        for_corner = new ArrayList();
        for_out = new ArrayList();
        for_sot = new ArrayList();
        for_sat = new ArrayList();
        for_foul = new ArrayList();

        layout_corner = v.findViewById(R.id.corner_layout);
        corner = v.findViewById(R.id.but_corner);
        Ecorner = v.findViewById(R.id.editText_corner);
        corner_count = v.findViewById(R.id.but_corner_count);
//        corner_check = v.findViewById(R.id.checkBox_corner);
        corner_progress = v.findViewById(R.id.progressBar_corner);
        title_corner = v.findViewById(R.id.title_corner);
        total_corner = v.findViewById(R.id.start_total_corner);


        for_corner.add("угловые");
        for_corner.add(corner);
        for_corner.add(Ecorner);
        for_corner.add(corner_count);
//        for_corner.add(corner_check);
        for_corner.add("check");
        for_corner.add(corner_progress);
        for_corner.add(title_corner);
        for_corner.add(total_corner);
        for_corner.add(layout_corner);

        layout_out = v.findViewById(R.id.out_layout);
        out = v.findViewById(R.id.but_out);
        Eout = v.findViewById(R.id.editText_out);
        out_count = v.findViewById(R.id.but_out_count);
//        out_check = v.findViewById(R.id.checkBox_out);
        out_progress = v.findViewById(R.id.progressBar_out);
        title_out = v.findViewById(R.id.title_out);
        total_out = v.findViewById(R.id.start_total_out);

        for_out.add("вброс аутов");
        for_out.add(out);
        for_out.add(Eout);
        for_out.add(out_count);
//        for_out.add(out_check);
        for_out.add("check");
        for_out.add(out_progress);
        for_out.add(title_out);
        for_out.add(total_out);
        for_out.add(layout_out);

        layout_foul = v.findViewById(R.id.foul_layout);
        foul = v.findViewById(R.id.but_foul);
        Efoul = v.findViewById(R.id.editText_foul);
        foul_count = v.findViewById(R.id.but_foul_count);
//        foul_check = v.findViewById(R.id.checkBox_foul);
        foul_progress = v.findViewById(R.id.progressBar_foul);
        title_foul = v.findViewById(R.id.title_foul);
        total_foul = v.findViewById(R.id.start_total_foul);

        for_foul.add("фолы");
        for_foul.add(foul);
        for_foul.add(Efoul);
        for_foul.add(foul_count);
//        for_foul.add(foul_check);
        for_foul.add("check");
        for_foul.add(foul_progress);
        for_foul.add(title_foul);
        for_foul.add(total_foul);
        for_foul.add(layout_foul);

        layout_sot = v.findViewById(R.id.sot_layout);
        sot = v.findViewById(R.id.but_sot);
        Esot = v.findViewById(R.id.editText_sot);
        sot_count = v.findViewById(R.id.but_sot_count);
//        sot_check = v.findViewById(R.id.checkBox_sot);
        sot_progress = v.findViewById(R.id.progressBar_sot);
        title_sot = v.findViewById(R.id.title_sot);
        total_sot = v.findViewById(R.id.start_total_sot);

        for_sot.add("удары в створ");
        for_sot.add(sot);
        for_sot.add(Esot);
        for_sot.add(sot_count);
//        for_sot.add(sot_check);
        for_sot.add("check");
        for_sot.add(sot_progress);
        for_sot.add(title_sot);
        for_sot.add(total_sot);
        for_sot.add(layout_sot);

        layout_sat = v.findViewById(R.id.sat_layout);
        sat = v.findViewById(R.id.but_sat);
        Esat = v.findViewById(R.id.editText_sat);
        sat_count = v.findViewById(R.id.but_sat_count);
//        sat_check = v.findViewById(R.id.checkBox_sat);
        sat_progress = v.findViewById(R.id.progressBar_sat);
        title_sat = v.findViewById(R.id.title_sat);
        total_sat = v.findViewById(R.id.start_total_sat);

        for_sat.add("удары от ворот");
        for_sat.add(sat);
        for_sat.add(Esat);
        for_sat.add(sat_count);
//        for_sat.add(sat_check);
        for_sat.add("check");
        for_sat.add(sat_progress);
        for_sat.add(title_sat);
        for_sat.add(total_sat);
        for_sat.add(layout_sat);

//        общий для всех список с вложеными списками с виджетами фрагмента-счётчика, по нему будет идти цикл for
        list_param.add(for_sat);
        list_param.add(for_sot);
        list_param.add(for_foul);
        list_param.add(for_out);
        list_param.add(for_corner);

//      запуск кругового цикла в потоке, так как при переходе в табах по фрагментам,
//      фрагмент пересоздается вызывая снова onCreateView,
//      таким образом создавая новые виджеты и снова добавляя их список выше, и если не создать списки заново то они будут соседствовать со старыми виджетами и всё сломается
//      поэтому вначале функции создаются пустые списки добавляются вновь созданные элементы,
//      а поток просто парсит один список и берёт то что нужно, count стоит чтобы не запускать поток вновь, если убрать - потоки будут размножаться.
        if (count == 0) {
            Executor executor = (runnable) -> {
                new Thread(runnable).start();
            };

            Runnable task = this::Loop;

            Runnable task_loop_total = this::LoopTotal;

            executor.execute(task);
            executor.execute(task_loop_total);
//            runnable = new Runnable() {
//                @Override
//                public void run() {
//                    Loop();
//                }
//            };
//            secThread = new Thread(runnable);
//            secThread.start();

//            runnable = new Runnable() {
//                @Override
//                public void run() {
//                    LoopTotal();
//                }
//            };
//            secThread = new Thread(runnable);
//            secThread.start();

        }
        return v;
    }

    public void init(int c)
    {
//        count += c;
//        Log.d("MyLog", String.valueOf(count));
    }

    public void setLink(String url, String id)
    {
//        ссылка на выбранный матч, устанавливется за счёт функции (getlink) в маинАктивити которая назначены слушателем на тот или иной спарсеный матч
        id_match = id;
        link_match = url;
//      если выбрать новый матч, то значения с предыдущего матча не нужны,
//      поэтому через цикл итерируемся по главному списку с параметрами и очищаем элеметы от значений
        for (int j=0; j < list_param.size(); j++) {
            final ArrayList statlist = (ArrayList) list_param.get(j);
            final Button btn = (Button) statlist.get(1);
            final Button btn_count = (Button) statlist.get(3);
//            final CheckBox check = (CheckBox) statlist.get(4);
            final ProgressBar prog_bar = (ProgressBar) statlist.get(5);
            final TextView title = (TextView) statlist.get(6);
            final TextView total = (TextView) statlist.get(7);
            final LinearLayout layout_check = (LinearLayout) statlist.get(8);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        btn.setText("");
                        btn_count.setText("");
                        title.setText(statlist.get(0).toString().toUpperCase());
                        prog_bar.setProgress(0);
//                        check.setChecked(false);
                        Drawable draw_layout = VectorDrawableCompat.create(getResources(), R.drawable.style_for_info__standart, null);
                        layout_check.setBackground(draw_layout);
                        total.setText("~~>");
                    } catch (Exception e) {
                        // e.printStackTrace();
                    }
                }
            });
        }
    }

//
    public void setTimer(String timer){
        Timer = timer;
    }


    public void LoopTotal(){
        while (true)
        {
            Document _doc = null;
            try {
                Thread.sleep(5000);
                _doc = Jsoup.connect("http://football-statistic.online/total_on_time").get();
            } catch (Exception e) {
                try {
                    _doc = Jsoup.connect("https://football-statistic.ru/total_on_time").get();
                } catch (IOException ex) {
//                    ex.printStackTrace();
                    continue;
                }
            }

            if (_doc != null)
            {

                try {
                    JSONObject root = new JSONObject( _doc.body().text() );
                    JSONObject ids = (JSONObject) root.get("ids");
                    JSONObject _id_ = (JSONObject) ids.get(id_match);
                    for(int i=0; i < list_param.size();i++){
                        final ArrayList statlist = (ArrayList) list_param.get(i);
                        String name_param = (String) statlist.get(0);
                        final TextView total = (TextView) statlist.get(7);
                        try {
                            final String json_param = _id_.get(name_param).toString();
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        total.setText(json_param + "-> ");
                                    } catch (Exception e) {
                                        // e.printStackTrace();
                                    }
                                }
                            });
                        } catch (Exception e){
                        continue;
                        }
                    }
                } catch (JSONException e) {
//                    e.printStackTrace();
                }

            }

        }
    }

    public void Loop()
    {
        count += 1;
        while (true)
        {
            String doc = null;
            try {
                Thread.sleep(1000);
                doc = Jsoup.connect(link_match).ignoreContentType(true).execute().body();
            } catch (Exception e) {
//                e.printStackTrace();
            }

            if (doc != null)
            {
//                Log.d("MyLog", "DOC NOT NULL " + count);
                ParamSet(doc);
            }

        }

    }

    public void ParamSet(String doc)
    {
        try
        {
            JSONObject root = new JSONObject(doc);
            JSONArray events = root.getJSONArray("events");
            JSONObject main_event = events.getJSONObject(0);

            if ( doc.contains("1-й тайм") ){
                T = 1;
            } else {
                T = 0;
            }

            for (int i = 0; i < events.length(); i++)
            { final JSONObject event = events.getJSONObject(i);
                if (T == 1)
                {
                    String name_param = event.getString("name");
                    for (String key : index){
                        if (name_param.equals("1-й тайм " + key))
                        {
                            for (int j=0; j < list_param.size(); j++)
                            {
                                ArrayList statlist = (ArrayList) list_param.get(j);
//                                Log.d("MyLog", String.valueOf(statlist.get(0).toString().equals(key)));
                                if ( statlist.get(0).toString().equals(key) )
                                {
                                    MathStat(event, statlist);
                                }
                            }

                        }
                    }
                }

                if (T == 0)
                {
                    String name_param = event.getString("name");
                    for (String key : index){
                        if (name_param.equals(key))
                        {
                            for (int j=0; j < list_param.size(); j++)
                            {
                                ArrayList statlist = (ArrayList) list_param.get(j);
//                                Log.d("MyLog", String.valueOf(statlist.get(0).toString().equals(key)));
                                if ( statlist.get(0).toString().equals(key) )
                                {
                                    MathStat(event, statlist);
                                }
                            }

                        }
                    }
                }

            }
        } catch (Exception e)
        {
//
        }

    }

    public void MathStat(final JSONObject event, final ArrayList statlist)

    {
        try {
            // парсинг json по ключу subcategorie -> quotes чтобы достать значения тоталов и их кефы
            JSONArray sub = event.getJSONArray("subcategories");
            JSONObject subcategories = sub.getJSONObject(0);

//            два списка с тоталом больше и меньше

            JSONArray q = subcategories.getJSONArray("quotes");

            JSONObject quotes_0 = q.getJSONObject(0);

//            список с тоталом больше
            final String TB = quotes_0.getString("p");
            final String TB_val = quotes_0.getString("quote");

            JSONObject quotes_1 = q.getJSONObject(1);

//            список с тоталом меньше
            final String TM = quotes_1.getString("p");
            final String TM_val = quotes_1.getString("quote");

//                    счёт стат события из json
            String[] score = event.getString("score").split("-");
//                    занчения первой и второй команды
            int k1 = Integer.parseInt( score[0] );
            int k2 = Integer.parseInt( score[1] );

//          кнопка для записи спасрсеных тоталов
            final Button btn = (Button) statlist.get(1);
            final String text_for_btn = String.format("Тотал: %s\n(Б-%s | М-%s)", TB, TB_val, TM_val);

            String upper_1;
            String upper_2;
            if (T==0) {
                upper_1 = "<sup><small> 1-тайм</small></sup>";
                upper_2 = "<sup><small> 2-тайм</small></sup>";
                scorecomment = event.getString("scoreComment");
            } else {
                upper_1 = "";
                upper_2 = "<sup><small>1-тайм</small></sup>";
                scorecomment = "";
            }

            final String text_for_title = String.format("%s: (%s)   %s", statlist.get(0).toString().toUpperCase(), event.getString("score") + upper_2, scorecomment.split(" ")[0] + upper_1 );

//          три элемента - инпут для извлечения значения пользовательского ввода тотала,
//          кнопка-счётчик для записи результата расчёта мат ожидания,
//          титульник для выводы текущего счёта стистических событий в матче
            final EditText edit = (EditText) statlist.get(2);
            final Button btn_count = (Button) statlist.get(3);
            final ProgressBar prog_bar = (ProgressBar) statlist.get(5);
            final TextView title = (TextView) statlist.get(6);
            final LinearLayout layout_check = (LinearLayout) statlist.get(8);

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        btn.setText(text_for_btn);
                        title.setText( Html.fromHtml(text_for_title) );
                    } catch (Exception e) {
                        // e.printStackTrace();
                    }
                }
            });

//            если в инпуте есть что-то кроме 0 или пустой строки, то забирает значение
            Float e_float = null;
            if (!edit.getText().toString().equals("") &&
                !edit.getText().toString().equals("0") &&
                !edit.getText().toString().equals("0.0"))
            {
//                берёт значение из инпута (плавающая точка)
                e_float = Float.parseFloat(edit.getText().toString());
            } else {
//                если в инпуте ничего нет, очищает поля куда мог бы записывать расчёты, и сбрасывает саму переменную
                e_float = null;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            btn_count.setText( "" );
                            prog_bar.setProgress(0);
                            Drawable draw_layout = VectorDrawableCompat.create(getResources(), R.drawable.style_for_info__standart, null);
                            layout_check.setBackground(draw_layout);
//                            title.setText(statlist.get(0).toString().toUpperCase());
                        } catch (Exception e) {
                            // e.printStackTrace();
                        }
                    }
                });
            }

//            если в переменной со значением из инпута что-то есть, начинается расчёт мат ожидания
            if (e_float != null)
            {
                try
                {

//                    тотал на текущее стат событие преобразует из любого тотала из списка спарсенного выше по subcategories -> quotes
                    int TOTAL = (int) Math.floor( Float.parseFloat(TB) );

//                    расчёт минут и секудн времени для совершение одного стат события за один тайм, в расчёт идёт пользоваительский тотал из инпута,
//                    пример: если вначале матча дают линию на то или иной событие с тоталом на один тайм 4.5 ,
//                    то расчёт для времени ожидания "игры" одного такого события будет такой - 45/4.5 = 10 минут - 1 событие. 45 это время тайма
                    final int minut = (int) Math.floor(45/e_float);
                    final int secund = (int) Math.floor((45/e_float)%1*60);

//                    таймер матча, берётся из
                    String[] timer = Timer.split(":");

                    final int timer_min = Integer.parseInt(timer[0]);
                    final int timer_sec = Integer.parseInt(timer[1]);

                    int math_min = (int) (
                            timer_min +
                            Integer.parseInt( String.valueOf(minut*(TOTAL-(k1+k2)))) +
                            Math.floor( (secund*(TOTAL-(k1+k2)) + timer_sec)/60 )
                            );

                    int math_sec = (int) Math.floor(
                            (secund*(TOTAL-(k1+k2)) + timer_sec)%60
                             );

//                    final CheckBox check = (CheckBox) statlist.get(4);

                    if (T==0){

                        if (math_min >= 90)
                        {
                            final int prog_math = ((math_min - 90)*60) + math_sec;

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        prog_bar.setProgress( prog_math );
                                    } catch (Exception e) {
                                        // e.printStackTrace();
                                    }
                                }
                            });


                            if (prog_math >= 180)
                            {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
//                                            check.setChecked(true);
                                            Drawable draw_layout = VectorDrawableCompat.create(getResources(), R.drawable.style_for_info__green_check, null);
                                            layout_check.setBackground(draw_layout);
                                        } catch (Exception e) {
                                            // e.printStackTrace();
                                        }
                                    }
                                });
                            } else {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
//                                            check.setChecked(false);
                                            Drawable draw_layout = VectorDrawableCompat.create(getResources(), R.drawable.style_for_info__standart, null);
                                            layout_check.setBackground(draw_layout);
                                        } catch (Exception e) {
                                            // e.printStackTrace();
                                        }
                                    }
                                });
                            }

                        } else {

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        prog_bar.setProgress( 0 );
//                                        check.setChecked(false);
                                        Drawable draw_layout = VectorDrawableCompat.create(getResources(), R.drawable.style_for_info__standart, null);
                                        layout_check.setBackground(draw_layout);
                                    } catch (Exception e) {
                                        // e.printStackTrace();
                                    }
                                }
                            });
                        }

                    } else {

                        if (math_min >= 45)
                        {
                            final int prog_math = ((math_min - 45)*60) + math_sec;

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        prog_bar.setProgress( prog_math );
                                    } catch (Exception e) {
                                        // e.printStackTrace();
                                    }
                                }
                            });


                            if (prog_math >= 180)
                            {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
//                                            check.setChecked(false);
//                                            check.setChecked(true);
                                            Drawable draw_layout = VectorDrawableCompat.create(getResources(), R.drawable.style_for_info__green_check, null);
                                            layout_check.setBackground(draw_layout);
                                        } catch (Exception e) {
                                            // e.printStackTrace();
                                        }
                                    }
                                });
                            } else {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
//                                            check.setChecked(false);
                                            Drawable draw_layout = VectorDrawableCompat.create(getResources(), R.drawable.style_for_info__standart, null);
                                            layout_check.setBackground(draw_layout);
                                        } catch (Exception e) {
                                            // e.printStackTrace();
                                        }
                                    }
                                });
                            }
                        } else {

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        prog_bar.setProgress( 0 );
//                                        check.setChecked(false);
                                        Drawable draw_layout = VectorDrawableCompat.create(getResources(), R.drawable.style_for_info__standart, null);
                                        layout_check.setBackground(draw_layout);
                                    } catch (Exception e) {
                                        // e.printStackTrace();
                                    }
                                }
                            });
                        }

                    }

                    final String text_for_count = String.format("%s:%s", math_min, math_sec);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {
//                                title.setText(Html.fromHtml(text_for_title));
                                btn_count.setText( text_for_count );
                            } catch (Exception e) {
                                // e.printStackTrace();
                            }
                        }
                    });

                } catch (Exception e) {

//                    Log.d("MyLog", String.valueOf(e));
                    e.printStackTrace();
                }
            }

        } catch (Exception e)
        {
            final Button btn = (Button) statlist.get(1);
            final Button btn_count = (Button) statlist.get(3);
//            final CheckBox check = (CheckBox) statlist.get(4);
            final ProgressBar prog_bar = (ProgressBar) statlist.get(5);
            final TextView title = (TextView) statlist.get(6);
            final LinearLayout layout_check = (LinearLayout) statlist.get(8);

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        btn.setText( "" );
                        btn_count.setText( "" );
                        title.setText(statlist.get(0).toString().toUpperCase());
                        prog_bar.setProgress(0);
//                        check.setChecked(false);
                        Drawable draw_layout = VectorDrawableCompat.create(getResources(), R.drawable.style_for_info__standart, null);
                        layout_check.setBackground(draw_layout);
                    } catch (Exception e) {
                        // e.printStackTrace();
                    }
                }
            });
            Log.d("MyLog", String.valueOf(e));
            e.printStackTrace();
        }
    }

}
