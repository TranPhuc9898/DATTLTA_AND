package com.thang.quiz.solution;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.thang.quiz.Item.ItemHighScore;
import com.thang.quiz.R;

import java.util.ArrayList;

public class AdatapterItemHighScore extends BaseAdapter {

    ArrayList<ItemHighScore> itemScoreArrayList;

    public AdatapterItemHighScore(ArrayList<ItemHighScore> itemScoreArrayList) {
        this.itemScoreArrayList = itemScoreArrayList;
    }

    @Override
    public int getCount() {
        return itemScoreArrayList.size();
    }

    @Override
    public ItemHighScore getItem(int i) {
        return itemScoreArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = new ViewHolder();
        if(view == null)
        {
            view = View.inflate(viewGroup.getContext(), R.layout.item_highscore,null);
        }

        viewHolder.txt_name = view.findViewById(R.id.txt_nameItemHighScore);
        viewHolder.txt_score = view.findViewById(R.id.txt_scoreHighScore);
        viewHolder.txt_time = view.findViewById(R.id.txt_timeHighScore);

        viewHolder.txt_time.setText(getItem(i).time);
        viewHolder.txt_score.setText(getItem(i).score);
        viewHolder.txt_name.setText(getItem(i).name);

        return view;
    }

    private class ViewHolder{
        TextView txt_score,txt_name,txt_time;
    }
}
