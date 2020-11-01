package com.thang.quiz.solution;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.thang.quiz.R;

import java.util.ArrayList;

public class AnswerListAdapter extends BaseAdapter {
    ArrayList<Integer> answer, answercorrect;
    ArrayList<String> ques;
    ArrayList<Integer> times;
    ArrayList<String>[] option;

    public AnswerListAdapter(ArrayList<Integer> answer, ArrayList<Integer> answercorrect, ArrayList<String> ques, ArrayList<String> opA, ArrayList<String> opB, ArrayList<String> opC, ArrayList<String> opD, ArrayList<Integer> times) {
        this.answer = answer;
        this.answercorrect = answercorrect;
        this.ques = ques;
        this.times = times;
        option = new ArrayList[4];
        option[0] = opA;
        option[1] = opB;
        option[2] = opC;
        option[3] = opD;
    }

    @Override
    public int getCount() {
        return ques.size();
    }

    @Override
    public Object getItem(int i) {
        return ques.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolderItem viewHolderItem = new ViewHolderItem();
        if(view==null)
        {
            view = View.inflate(viewGroup.getContext(), R.layout.item_answer,null);
        }
        viewHolderItem.txt_Ques = view.findViewById(R.id.txt_question);
        viewHolderItem.txt_Ans = view.findViewById(R.id.txt_answer);
        viewHolderItem.txt_Ans_Correct = view.findViewById(R.id.txt_correct);
        viewHolderItem.txt_Times = view.findViewById(R.id.txt_timeanswerit);

        viewHolderItem.txt_Ques.setText(i + 1 + ". "+ques.get(i));
        if(answer.get(i) == 0)
        {
            viewHolderItem.txt_Ans.setText("You don't answer");
        }
        else {
            viewHolderItem.txt_Ans.setText("You answer: " + option[answer.get(i)-1].get(i).toString());
        }

        viewHolderItem.txt_Ans_Correct.setText("Answer correct: " + option[answercorrect.get(i)-1].get(i).toString());
        viewHolderItem.txt_Times.setText("Time: "+times.get(i));

        return view;
    }

    class ViewHolderItem{
        TextView txt_Ques,txt_Ans,txt_Ans_Correct,txt_Times;
    }
}
