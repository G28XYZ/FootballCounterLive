package footballcounter.project.footballcounterlive;

import android.os.Bundle;
import footballcounter.project.footballcounterlive.R;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Tab3 extends Fragment {

    View v ;
    private TextView textAbout;
    private TextView textForLink;
    private TextView Link;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.tab3, container, false);

        String telegram_link = "https://t.me/just_parsingBot";
        textAbout = (TextView) v.findViewById(R.id.text_about);
        textForLink = (TextView) v.findViewById(R.id.text_for_link_to_bot);
        Link = (TextView) v.findViewById(R.id.link);

        String text_for_link = "<br><br><b>Вся информация в приложении имеет ознакомительный характер. Информация предоставляется в развлекательных целях.</b><br><br>Телеграмм бот по футбольной статистике<br>";


        String text_about = String.format(
                "<br><br>О приложении.<br><i>Что это такое?</i><br>- Football Counter Live (счётчик на <b>ТОТАЛ МЕНЬШЕ</b>)<br>" +
                "Счётчик статистики по футболу только live матчи (парсит линию Fonbet)<br><br> " +
                "<i>Как он (счётчик) работает?</i><br>- во вкладке 'Список игр' нажмите кнопку снизу - 'Обновить ссылки'<br>" +
                "появится список с названием текущих live-матчей по футболу.<br><br><i>Что дальше?</i><br> " +

                        "<br>Формула расчёта на Тотал Меньше (ТМ) событий в лайве следующая - рассмотрим пример:<br><br>" +
                        "Время на тайм в футболе 45 минут Т = 45 . " +
                        "Тотал на фолы по линии вначале тайма дали 9.5 с кефом 1.85, округлим тотал до 10, потому что кеф достаточно высокий." +
                        "<br><br>Таким образом вычислим сколько нужно времени на один фол в тайме.<br><br>" +
                        "45 / 10 = 4.5<br> " +
                        "Конвертируем в минуты получаем - время одного события 4 минуты 30 секунд.<br><br>" +
                        "Это время и нужно знать перед началом матча, чтобы расчитывать мат.ожидание сыграных стат событий в лайве.<br>" +
                        "Второй этап примера - идёт уже 21-ая минута матча, динамика игры менялась как и события, и команды нафолили например 7 раз, " +
                        "тотал на фолы по ходу игры тоже меняется и к 21-ой минуте составляет 13.5, при общем счёте команд по фолам, напоминаем - 7 (команда 1 = 3 фола | команда 2 = 4 фола).<br><br>" +
                        "Время на одно событие нам уже известно это 4:30, умножим на разницу тотала фолов и общий счёт команд по фолам.<br><br>" +
                        "(13<sup>(округляем в меньшую сторону)</sup> - 7<sup>(количество фолов на 21-ой минуте)</sup>) * 4.5 = 27<br><br>" +
                        "плюсуем полученное время к времени матча (21 + 27) у нас получится 48, таким образом ориентировочно только к 48-ой минуте может сыграть тотал 13.5, " +
                        "тайм длится 45 минут, плюс добавочное время, поэтому такой тотал на данном промежутке времени подходящий для ставки на ТМ 13.5<br>" +
                        "Это приложение производит все эти расчёты. Необходимо только во вкладке 'Счётчик' вбить в поле ввода в таблички " +
                        "слева соответвующий тотал на тайм. Иногда этот тотал появляется левее от поля ввода с указательной стрелочкой -><br><br>" +
                        "Если совсем ничего не понятно, может и не надо...?<br>" +
                        "Кстати аналогия расчёта для Тотал Больше почти такая же, но время расчётное нужно ловить не 48 минут и выше, а как минимум чтобы было меньше 45 минут. " +
                        "Но это уже совсем другая история." +

                "<br><br><i>P.S. От разработчика: - </i><br>" +
                "Мануал написан каряво, но по мере поступления отзывов и " +
                "предложений буду формировать более понятный текст или схемы.<br><br>" +
                "P.S.S. Приложение может вылетать (редко). Задержка по времени в счётчике напрямую зависит от скорости интернета пользователя.");

        textForLink.setText(Html.fromHtml(text_for_link));
//        Link.setText(telegram_link);
        textAbout.setText(Html.fromHtml(text_about));


        return v;
    }
}