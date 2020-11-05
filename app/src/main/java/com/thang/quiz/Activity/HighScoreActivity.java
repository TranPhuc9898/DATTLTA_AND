package com.thang.quiz.Activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thang.quiz.Interface.InterfaceAPI;
import com.thang.quiz.Item.Categaries;
import com.thang.quiz.Item.ItemHighScore;
import com.thang.quiz.R;
import com.thang.quiz.solution.AdatapterItemHighScore;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HighScoreActivity extends AppCompatActivity implements InterfaceAPI, View.OnClickListener, AdapterView.OnItemSelectedListener {

    Spinner sp_mode1,sp_mode2;
    String urlstr = "https://opentdb.com/api_category.php";
    ListView listView;
    List<String> modeList;
    ArrayList<Categaries> itemcategariesArrayList;
    ArrayList<ItemHighScore> itemScoreArrayList;
    BaseAdapter adapterCategaries,adapterMode;
    AdatapterItemHighScore adatapterItemHighScore;

    FirebaseDatabase database;
    DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        Intallzation();
        DataInstall();
//        database = FirebaseDatabase.getInstance();
//        myRef = database.getReference("highscore");
//        // Read from the database
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                int value = dataSnapshot.getValue(Integer.class);
//                Log.d("TAG", "Value is: " + value);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Log.w("TAG", "Failed to read value.", error.toException());
//            }
//        });
    }

    private void Intallzation()
    {
        sp_mode1 = findViewById(R.id.spinner_mode1);
        sp_mode2 = findViewById(R.id.spinner_mode2);
        listView = findViewById(R.id.listHighscore);

        itemcategariesArrayList = new ArrayList<>();
        itemcategariesArrayList.add(new Categaries(0,"Any Category"));
        adapterCategaries = new BaseAdapter() {
            @Override
            public int getCount() {
                return itemcategariesArrayList.size();
            }

            @Override
            public Categaries getItem(int i) {
                return itemcategariesArrayList.get(i);
            }

            @Override
            public long getItemId(int i) {
                return itemcategariesArrayList.get(i).id;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                if(view == null)
                {
                    view = View.inflate(viewGroup.getContext(),R.layout.item_spinner,null);
                }
                TextView txt_itemspinner = view.findViewById(R.id.txt_itemspinner);
                txt_itemspinner.setText(getItem(i).scategary);
                return view;
            }
        };
        sp_mode1.setAdapter(adapterCategaries);
        sp_mode1.setOnItemSelectedListener(this);

        modeList = new ArrayList<>();
        modeList.add("Easy");
        modeList.add("Normal");
        modeList.add("Hard");

        adapterMode = new BaseAdapter() {
            @Override
            public int getCount() {
                return modeList.size();
            }

            @Override
            public String getItem(int i) {
                return modeList.get(i);
            }

            @Override
            public long getItemId(int i) {
                return 0;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                if(view == null)
                {
                    view = View.inflate(viewGroup.getContext(),R.layout.item_spinner,null);
                }
                TextView txt_itemspinner = view.findViewById(R.id.txt_itemspinner);
                txt_itemspinner.setText(getItem(i));
                return view;
            }
        };
        sp_mode2.setAdapter(adapterMode);
        sp_mode2.setOnItemSelectedListener(this);

        itemScoreArrayList = new ArrayList<>();
        itemScoreArrayList.add(new ItemHighScore("abc","abc","abc"));
        itemScoreArrayList.add(new ItemHighScore("abc","abc","abc"));
        itemScoreArrayList.add(new ItemHighScore("abc","abc","abc"));
        itemScoreArrayList.add(new ItemHighScore("abc","abc","abc"));
        itemScoreArrayList.add(new ItemHighScore("abc","abc","abc"));

        adatapterItemHighScore = new AdatapterItemHighScore(itemScoreArrayList);
        listView.setAdapter(adatapterItemHighScore);

    }

    private void DataInstall() {
        final InterfaceAPI interfaceAPI = (InterfaceAPI) this;
        final StringBuffer response = new StringBuffer();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urlstr);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestProperty("Content-Type", "application/json; utf-8");
                    con.setRequestProperty("Accept", "application/json");
                    con.setRequestMethod("GET");
                    if(con.getResponseCode() == 200)
                    {
                        Log.d("asdasd","OK");
                        BufferedReader inputStreamReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                        String in;
                        while ((in = inputStreamReader.readLine())!= null)
                            response.append(in);
                        interfaceAPI.ResponseURL(true,response);
//                        Log.d("123123",response.toString());
                    }
                    else {
                        Log.d("asdasd","False");
                        interfaceAPI.ResponseURL(false,response);
                    }
                    con.disconnect();
                } catch (Exception e) {
                    Log.d("asdasd", e.toString());
//                    interfaceAPI.ResponseURL(false,response);
                }
            }
        });
    }

    @Override
    public void ResponseURL(Boolean check, StringBuffer response) {
        if(check)
        {
            try {
                JSONObject jsonObject = new JSONObject(response.toString());
                JSONArray jsonArray = jsonObject.getJSONArray("trivia_categories");
                for (int i = 0; i < jsonArray.length(); i++) {
                    itemcategariesArrayList.add(new Categaries(jsonArray.getJSONObject(i).getInt("id"),jsonArray.getJSONObject(i).getString("name")));
                }
                adapterCategaries.notify();
                sp_mode1.setAdapter(adapterCategaries);
                Log.d("123123",itemcategariesArrayList.toString());
            }
            catch (Exception ex) {
                Toast.makeText(this, ex.toString(), Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId())
        {
            case R.id.spinner_mode1:{
                Log.d("axzaxz",""+itemcategariesArrayList.get(i).id);
                SetDataListView("",1);
//                Toast.makeText(HighScoreActivity.this, categoriesArrayList.get(i), Toast.LENGTH_SHORT).show();
            }
            break;
            case R.id.spinner_mode2:{
                Log.d("axzaxz",modeList.get(i));
                SetDataListView("",1);
//                Toast.makeText(HighScoreActivity.this, modeList.get(i), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void SetDataListView(String mode,int categaries)
    {
        itemScoreArrayList = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReferenceFromUrl("https://quiz-e756f.firebaseio.com/highscore/easy/catergaries:0");
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Log.d("TAG", "Value is: " + dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
    }
}