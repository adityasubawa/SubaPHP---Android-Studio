package com.suba.php;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PostDetail extends AppCompatActivity implements View.OnClickListener{
    private TextView judulTv, kategoriTv, tanggalTv, contentTv;
    private TextView PostId;
    private ImageView imageIv;
    private String id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();

        id = intent.getStringExtra(ApiConfig.POST_ID);

        imageIv = (ImageView) findViewById(R.id.imageIv);
        PostId = (TextView) findViewById(R.id.id);
        judulTv = (TextView) findViewById(R.id.judulTv);
        tanggalTv = (TextView) findViewById(R.id.tanggalTv);
        kategoriTv = (TextView) findViewById(R.id.kategoriTv);
        contentTv = (TextView) findViewById(R.id.contentTv);
        PostId.setText(id);

        getPostDetail();

    }

    private void getPostDetail(){
        class GetPost extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(PostDetail.this,"Mengambil data...","Mohon tunggu...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                showPost(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(ApiConfig.URL_GET_POST,id);
                return s;
            }
        }
        GetPost ge = new GetPost();
        ge.execute();
    }

    private void showPost(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(ApiConfig.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);
            String imgurl = ""+ApiConfig.BASE_URL+"admin/";
            String judul = c.getString(ApiConfig.KEY_POST_JUDUL);
            String publish_date = c.getString(ApiConfig.KEY_POST_PUBLISH);
            String gambar= imgurl + c.getString(ApiConfig.KEY_POST_GAMBAR);
            String desg = c.getString(ApiConfig.KEY_POST_ARTIKEL);
            String kategori = c.getString(ApiConfig.KEY_POST_KATEGORI);

            judulTv.setText(judul);
            String htmltext = Html.fromHtml(desg).toString();
            contentTv.setText(Html.fromHtml(htmltext));
            tanggalTv.setText(publish_date);
            kategoriTv.setText(kategori);
            imageIv.setVisibility(View.GONE);
            Picasso.get().load(gambar).into(imageIv, new Callback() {
                @Override
                public void onSuccess() {
                    imageIv.setVisibility(View.VISIBLE);
                }

                @Override
                public void onError(Exception e) {
                    imageIv.setVisibility(View.VISIBLE);
                    imageIv.setImageResource(R.drawable.ic_baseline_image_black);
                }


            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    public void onClick(View v) {
        
    }
}
