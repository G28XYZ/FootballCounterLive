package footballcounter.project.footballcounterlive;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class ScoreArrayAdapter extends ArrayAdapter<ScoreList> {
    private LayoutInflater inflater;
    private List<ScoreList> listItem = new ArrayList<>();


    public ScoreArrayAdapter(@NonNull Context context, int resource, List<ScoreList> listItem, LayoutInflater inflater) {
        super(context, resource, listItem);
        this.inflater = inflater;
        this.listItem = listItem;

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ScoreArrayAdapter.ViewHolder viewHolder;
        ScoreList listItemMain = listItem.get(position);


        if(convertView == null){
            convertView = inflater.inflate(R.layout.score_layout, null, false);
            viewHolder = new ViewHolder();

            viewHolder.homeText = convertView.findViewById(R.id.homeText);
            viewHolder.allText = convertView.findViewById(R.id.allText);
            viewHolder.awayText = convertView.findViewById(R.id.awayText);
            viewHolder.titleTextMatch = convertView.findViewById(R.id.titleTextMatch);
            viewHolder.titleTextTime1 = convertView.findViewById(R.id.titleTextTime1);
            viewHolder.titleTextTime2 = convertView.findViewById(R.id.titleTextTime2);
            viewHolder.awayTextTime1 = convertView.findViewById(R.id.awayTextDetails1);
            viewHolder.awayTextTime2 = convertView.findViewById(R.id.awayTextDetails2);
            viewHolder.homeTextTime1 = convertView.findViewById(R.id.homeTextDetails1);
            viewHolder.homeTextTime2 = convertView.findViewById(R.id.homeTextDetails2);

            convertView.setTag(viewHolder);

        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.homeText.setText(listItemMain.getHomeText());

        viewHolder.allText.setText(listItemMain.getAllText());

        viewHolder.awayText.setText(listItemMain.getAwayText());

        viewHolder.titleTextMatch.setText( listItemMain.getTitleTextMatch() );

        viewHolder.titleTextTime1.setText( listItemMain.getTitleTextTime1() );
        viewHolder.titleTextTime2.setText( listItemMain.getTitleTextTime2() );

        viewHolder.homeTextTime1.setText( listItemMain.getHomeTextTime1() );
        viewHolder.homeTextTime2.setText( listItemMain.getHomeTextTime2() );

        viewHolder.awayTextTime1.setText( listItemMain.getAwayTextTime1() );
        viewHolder.awayTextTime2.setText( listItemMain.getAwayTextTime2() );


        return convertView;
    }

    private class ViewHolder {

        Button homeText;
        Button allText;
        Button awayText;
        TextView titleTextMatch;

        TextView titleTextTime1;
        TextView titleTextTime2;

        TextView homeTextTime1;
        TextView homeTextTime2;

        TextView awayTextTime1;
        TextView awayTextTime2;

    }
}
