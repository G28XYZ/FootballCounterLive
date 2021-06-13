package footballcounter.project.footballcounterlive;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class CustomScoreAdapter extends ArrayAdapter<CustomScoreItem> {

    public CustomScoreAdapter(Context context, ArrayList<CustomScoreItem> countryList) {
        super(context, 0, countryList);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }
    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.country_spinner_row, parent, false
            );
        }
//        ImageView imageViewFlag = convertView.findViewById(R.id.image_view_flag);
        TextView textViewName = convertView.findViewById(R.id.flashscore_text);
        CustomScoreItem currentItem = getItem(position);
        if (currentItem != null) {
//            imageViewFlag.setImageResource(currentItem.getFlagImage());
            textViewName.setText(currentItem.getCustomName());
        }
        return convertView;
    }
}