package footballcounter.project.footballcounterlive;

import android.graphics.drawable.Drawable;

public class ListItemClass {
    String data_1;
    String data_2;
    String data_3;
    String data_4;

    String mTag_1;
    String mTag_2;

    Drawable Backround;

    String Count;

    int Color;

    public String getTag_1() {
        return mTag_1;
    }

    public void setTag_1(String tag_1) {
        this.mTag_1 = tag_1;
    }

    public void setBackgr(Drawable b) { this.Backround = b;

    }
    public Drawable getBackgr() {return Backround;}

    public void setTextColor(int color) { this.Color = color; }

    public void setCountLink(int count){
        this.Count = String.valueOf(count);
    }

    public String getCountLink(){
        return Count;
    }

    public Integer getTextColor()
    {
        return Color;
    }

    public String getData_1() {
        return data_1;
    }

    public void setData_1(String data_1) {
        this.data_1 = data_1;
    }

    public String getData_2() {
        return data_2;
    }

    public void setData_2(String data_2) {
        this.data_2 = data_2;
    }

    public String getTag_2() {
        return mTag_2;
    }

    public void setTag_2(String tag_2) {
        this.mTag_2 = tag_2;
    }

    public String getData_3() {
        return data_3;
    }

    public void setData_3(String data_3) {
        this.data_3 = data_3;
    }

    public String getData_4() {
        return data_4;
    }

    public void setData_4(String data_4) {
        this.data_4 = data_4;
    }
}