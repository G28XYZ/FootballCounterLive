package footballcounter.project.footballcounterlive;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class CustomArrayAdapter extends ArrayAdapter<ListItemClass> {
    private LayoutInflater inflater;
    private List<ListItemClass> listItem = new ArrayList<>();


    public CustomArrayAdapter(@NonNull Context context, int resource, List<ListItemClass> listItem, LayoutInflater inflater) {
        super(context, resource, listItem);
        this.inflater = inflater;
        this.listItem = listItem;

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        ListItemClass listItemMain = listItem.get(position);
        if(convertView == null){
            convertView = inflater.inflate(R.layout.list_item, null, false);
            viewHolder = new ViewHolder();
            viewHolder.data1 = convertView.findViewById(R.id.game_btn);
            viewHolder.data2 = convertView.findViewById(R.id.info);
            viewHolder.data3 = convertView.findViewById(R.id.count_link);
//            viewHolder.data4 = convertView.findViewById(R.id.tvData4);
            convertView.setTag(viewHolder);

        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.data1.setText(listItemMain.getData_1());
        viewHolder.data1.setTag(listItemMain.getTag_1());
        viewHolder.data1.setTextColor(listItemMain.getTextColor());
//        viewHolder.data1.setBackground(listItemMain.getBackgr());

        viewHolder.data2.setTag(listItemMain.getTag_2());

        viewHolder.data3.setText(listItemMain.getCountLink());

//        viewHolder.data3.setText(listItemMain.getData_3());
//        viewHolder.data4.setText(listItemMain.getData_4());


        return convertView;
    }
    private class ViewHolder{
        TextView data1;
        GifImageView data2;
        TextView data3;
//        TextView data4;


    }
}
