package com.philong.karaoke;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;

import com.philong.adapter.BHAdapter;
import com.philong.model.BaiHat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText edtTimKiem;
    private TabHost tabHost;
    private ListView lsvBH, lsvBHYT, lsvTimKiem;
    private ArrayList<BaiHat> arrBH, arrBHYT, arrTimKiem;
    private BHAdapter adapterBH, adapterBHYT, adapterTimKiem;


    public  static final String DATABASE_NAME = "Arirang.sqlite";
    private static final String FOLDER_DATABASE = "/databases/";
    public  static SQLiteDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addTabHost();
        addControls();
        xuLyDatabase();
        hienThiBaiHat();
        hienThiBaiHatYeuThich();
        addEvents();
    }

    private void addEvents() {
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (tabId.equals("TabBH")) {
                    hienThiBaiHat();
                } else if (tabId.equals("TabBHYT")) {
                    hienThiBaiHatYeuThich();
                }
            }
        });

        edtTimKiem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = edtTimKiem.getText().toString();
                arrTimKiem.clear();
                for (int i = 0; i < arrBH.size(); i++) {
                    String tenBH = arrBH.get(i).getTenBH();
                    if (text.length() <= tenBH.length()) {
                        if (text.equalsIgnoreCase(tenBH.substring(0, text.length()))) {
                            arrTimKiem.add(arrBH.get(i));
                        }
                    }
                }
                adapterTimKiem.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void hienThiBaiHat(){
        database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor = database.query("ArirangSongList", null, null, null, null, null, null);
        arrBH.clear();
        cursor.moveToFirst();
        while(cursor.moveToNext()){
            int maBH = Integer.parseInt(cursor.getString(cursor.getColumnIndex("MABH")));
            String tenBH = cursor.getString(cursor.getColumnIndex("TENBH"));
            String tacGia = cursor.getString(cursor.getColumnIndex("TACGIA"));
            int yeuThich = cursor.getInt(cursor.getColumnIndex("YEUTHICH"));
            arrBH.add(new BaiHat(maBH, tenBH, tacGia, yeuThich));
        }
        adapterBH.notifyDataSetChanged();
    }

    private void hienThiBaiHatYeuThich(){
        arrBHYT.clear();
        for(BaiHat bh : arrBH){
            if(bh.getYeuThich() == 1){
                arrBHYT.add(bh);
            }
        }
        adapterBHYT.notifyDataSetChanged();
    }
    private void xuLyDatabase() {
        File dbFile = getDatabasePath(DATABASE_NAME);
        if(!dbFile.exists()){
            try{
                copyDatabase();
            }catch(Exception ex){
                Log.e("Error: ", ex.toString());
            }
        }
    }

    private void copyDatabase() {
        try {
            InputStream inputStream = getAssets().open(DATABASE_NAME);
            String outFileName = layDuongDanDatabase();
            File f = new File(getApplicationInfo().dataDir + FOLDER_DATABASE);
            if(!f.exists()){
                f.mkdir();
            }
            OutputStream outputStream = new FileOutputStream(outFileName);
            byte buffer[] = new byte[1024];
            int length;
            while((length = inputStream.read(buffer)) > 0){
                outputStream.write(buffer, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
        }catch(Exception ex){
            Log.e("Error: ", ex.toString());
        }
    }

    private String layDuongDanDatabase() {
        return getApplicationInfo().dataDir + FOLDER_DATABASE + DATABASE_NAME;
    }

    private void addControls() {
        edtTimKiem = (EditText)findViewById(R.id.edtTimKiem);
        lsvBH = (ListView)findViewById(R.id.lsvBH);
        lsvBHYT = (ListView)findViewById(R.id.lsvBHYT);
        lsvTimKiem = (ListView)findViewById(R.id.lsvTimKiem);
        arrBH = new ArrayList<>();
        arrBHYT = new ArrayList<>();
        arrTimKiem = new ArrayList<>();
        adapterBH = new BHAdapter(MainActivity.this, R.layout.layout_item_bai_hat, arrBH);
        adapterBHYT = new BHAdapter(MainActivity.this, R.layout.layout_item_bai_hat, arrBHYT);
        adapterTimKiem = new BHAdapter(MainActivity.this, R.layout.layout_item_bai_hat, arrTimKiem);
        lsvBH.setAdapter(adapterBH);
        lsvBHYT.setAdapter(adapterBHYT);
        lsvTimKiem.setAdapter(adapterTimKiem);
    }

    private void addTabHost() {
        tabHost = (TabHost)findViewById(R.id.tabHost);
        tabHost.setup();
        TabHost.TabSpec tabBH = tabHost.newTabSpec("TabBH");
        tabBH.setContent(R.id.tabBH);
        tabBH.setIndicator("Bài Hát");
        tabHost.addTab(tabBH);
        TabHost.TabSpec tabBHYT = tabHost.newTabSpec("TabBHYT");
        tabBHYT.setContent(R.id.tabBHYT);
        tabBHYT.setIndicator("Yêu thích");
        tabHost.addTab(tabBHYT);
        TabHost.TabSpec tabTimKiem = tabHost.newTabSpec("TabTimKiem");
        tabTimKiem.setContent(R.id.tabTimKiem);
        tabTimKiem.setIndicator("", getDrawable(R.drawable.ic_search_24dp));
        tabHost.addTab(tabTimKiem);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        database.close();
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_item, menu);
        MenuItem menuSearch = menu.findItem(R.id.menuSearch);
        SearchView search = (SearchView) menuSearch.getActionView();
        search.setQueryHint("Tìm kiếm...");
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
*/
}
