package com.thang.quiz.Activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.thang.quiz.R;
import com.thang.quiz.solution.AnswerListAdapter;

import java.util.ArrayList;


public class SolutionActivity extends AppCompatActivity {

    static ArrayList<Integer> Answers;
    static ArrayList<String> Question;
    static ArrayList<String> optA;
    static ArrayList<String> optB;
    static ArrayList<String> optC;
    static ArrayList<String> optD;
    static ArrayList<Integer> Answer;
    ArrayList<Integer> times;

//    @BindView(R.id.viewpager)
//    ViewPager viewPager;
//
//    @BindView(R.id.sliding_tabs)
//    TabLayout tabLayout;
    ListView listView;
    TextView txt_times;
    double totalTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution_answer);
//        ButterKnife.bind(this);

        listView = findViewById(R.id.list_view);
        txt_times = findViewById(R.id.txt_timeanswersl);

        Log.d("asdasd","asdasd");
        Answers = getIntent().getIntegerArrayListExtra("Answers");
        Answer = getIntent().getIntegerArrayListExtra("Answer");
        Question = getIntent().getStringArrayListExtra("Question");
        optA = getIntent().getStringArrayListExtra("optA");
        optB = getIntent().getStringArrayListExtra("optB");
        optC = getIntent().getStringArrayListExtra("optC");
        optD = getIntent().getStringArrayListExtra("optD");
        times = getIntent().getIntegerArrayListExtra("Times");

        AnswerListAdapter answerListAdapter = new AnswerListAdapter(Answers,Answer,Question,optA,optB,optC,optD,times);
        listView.setAdapter(answerListAdapter);

        for (int i:times) {
            totalTime += i;
        }

        txt_times.setText("Average time: "+totalTime/10+"\nTotal score: "+getIntent().getIntExtra("Score",0)+"\10");

//        SimpleFragmentPagerAdapter adapter = new SimpleFragmentPagerAdapter(this,getSupportFragmentManager());
//        viewPager.setAdapter(adapter);
//        tabLayout.setupWithViewPager(viewPager);
    }

//    @Override
//    public void onBackPressed() {
////        Intent intent = new Intent(this,HomeActivity.class);
////        startActivity(intent);
//    }

    public static ArrayList<Integer> getAnswer() {
        return Answers;
    }

    public static ArrayList<Integer> getAnswers() {
        return Answer;
    }

    public static ArrayList<String> getQuestion() {
        return Question;
    }

    public static ArrayList<String> getOptA() {
        return optA;
    }

    public static ArrayList<String> getOptB() {
        return optB;
    }

    public static ArrayList<String> getOptC() {
        return optC;
    }

    public static ArrayList<String> getOptD() {
        return optD;
    }
}