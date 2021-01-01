package com.suba.php;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements ListView.OnItemClickListener{

    private ListView listView;

    private String JSON_STRING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        getJSON();
    }

    private void getPost(){
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(ApiConfig.TAG_JSON_ARRAY);
            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String imgurl = ""+ApiConfig.BASE_URL+"admin/";
                String id = jo.getString(ApiConfig.TAG_ID);
                String judul = jo.getString(ApiConfig.TAG_JUDUL);
                String gambar= imgurl + jo.getString(ApiConfig.TAG_GAMBAR);
                String content = Html.fromHtml(jo.getString(ApiConfig.TAG_ARTIKEL)).toString().replaceAll("<p>", "").trim();
                String tanggal = jo.getString(ApiConfig.TAG_PUBLISH);
                String kategori = jo.getString(ApiConfig.TAG_KATEGORI);

                HashMap<String,String> posts = new HashMap<>();
                posts.put(ApiConfig.TAG_ID,id);
                posts.put(ApiConfig.TAG_JUDUL,judul);
                posts.put("gambar", gambar);
                posts.put(ApiConfig.TAG_ARTIKEL,content);
                posts.put(ApiConfig.TAG_PUBLISH,tanggal);
                posts.put(ApiConfig.TAG_KATEGORI,kategori);

                list.add(posts);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new CustomImageAdapter(

                MainActivity.this, list, R.layout.post_list,
                new String[]{ApiConfig.TAG_ID, ApiConfig.TAG_JUDUL, ApiConfig.TAG_ARTIKEL,ApiConfig.TAG_KATEGORI,ApiConfig.TAG_PUBLISH},
                new int[]{R.id.id, R.id.judulTv, R.id.contentTv, R.id.kategoriTv, R.id.tanggalTv});
        listView.setAdapter(adapter);
    }

    private void getJSON(){
        class GetJSON extends AsyncTask<Void,Void,String>{

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this,"Mengambil data...","Mohon tunggu...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                getPost();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(ApiConfig.URL_GET_ALL);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, PostDetail.class);
        HashMap<String,String> map =(HashMap)parent.getItemAtPosition(position);
        String empId = map.get(ApiConfig.POST_ID).toString();
        intent.putExtra(ApiConfig.POST_ID,empId);
        startActivity(intent);
    }



}