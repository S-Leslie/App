package com.a_s.spurs;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public String DBName = "";
    ArrayList<Quest> list = new ArrayList<Quest>();
    ArrayList<Quest> find = new ArrayList<Quest>();
    boolean SEARCHINANSWER = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        EditText et = findViewById(R.id.editText);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                FindingText();
                FillList();
            }
        });
        //File filename = Environment.getExternalStorageDirectory();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10001);
        } else {
            ReadFile();
            FindingText();
            FillList();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.action_file){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10001);
            } else {
                Intent intent = new Intent(MainActivity.this, Choose.class);
                startActivityForResult(intent, 2);
            }
        }
        if(id==R.id.action_search){
            SEARCHINANSWER = !SEARCHINANSWER;
            item.setChecked(SEARCHINANSWER);
            FindingText();
            FillList();
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void ReadFile() {
        String filename = "";
        String sysFile = "ChoosedFile";
        try {
            InputStream inputStream = openFileInput(sysFile);
            InputStreamReader isr = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(isr);
            filename += reader.readLine();
            inputStream.close();
        } catch (Throwable t) {

        }
        ////
        if (filename != "" && new File(filename).exists()) {
            try {
                InputStreamReader ISR = new InputStreamReader(new FileInputStream(filename), "UTF16");
                BufferedReader reader = new BufferedReader(ISR);
                String line;
                String text = "";
                while ((line = reader.readLine()) != null) {
                    text += line;
                }
                FillDB(text);
            } catch (Throwable t) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Ошибка")
                        .setMessage("Невозможно прочесть БД")
                        .setCancelable(false)
                        .setNegativeButton("ОК",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        } else {
            try {
                String path = Environment.getExternalStorageDirectory().toString();
                File spursFolder = new File(path+"/spurs");
                if (!spursFolder.exists())
                    spursFolder.mkdir();
            } catch (Exception e) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Ошибка создания папки")
                        .setMessage("Пожалуйста, создайте папку вручную")
                        .setCancelable(true)
                        .setNegativeButton("ОК",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        //super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK)
        {
            ReadFile();
            FindingText();
            FillList();
        }
    }
    // Заполнение списка вопросов
    private void FillDB(String DB)
    {
        // Удаляем крайние строки
        DB = DB.replace("<table border=\"1\"><tbody>", "");
        DB = DB.replace("</tbody></table>", "");
        // Убираем все переносы на новую строку
        DB = DB.replace("\n", "");
        // Разбиваем базу данных по отдельным вопросам
        DB = DB.replace("</tr>","<tr>");
        String[] temp = DB.split("<tr>");
        list.clear();
        try
        {
            for (String i : temp)
            {
                if(!i.equals(""))
                list.add(new Quest(i));
            }
        }
        catch (Exception e)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Ошибка чтения файла!")
                    .setMessage("База данных повреждена")
                    .setCancelable(true)
                    .setNegativeButton("ОК",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    private void FindingText()
    {
        find.clear();
        EditText et = findViewById(R.id.editText);
        String[] parts = et.getText().toString().split(" ");
        for(Quest i : list)
        {
            Boolean skip = false;
            String searchIn = i.GetQuestion();
            if (SEARCHINANSWER)
                searchIn += " " + i.GetAnswer();
            for (String j : parts)
            if (!searchIn.toLowerCase().contains(j.toLowerCase()))
                skip = true;
            if (!skip) find.add(i);
        }
    }

    private void FillList()
    {
        LinearLayout LL = findViewById(R.id.master);
        LL.removeAllViews();
        LinearLayout.LayoutParams LP = new LinearLayout.LayoutParams(-1, -2);
        EditText et = findViewById(R.id.editText);
        ScrollView SV = findViewById(R.id.scroll);

        int count = 0;
        for (Quest i : find)
        {
            Button but = new Button(this);
            but.setText(i.GetQuestion());
            but.setLayoutParams(LP);
            but.setId(count);
            but.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, Answers.class);
                    int c = ((Button)v).getId();
                    intent.putExtra("text", find.get(c).GetQuestion());
                    intent.putExtra("answ", find.get(c).GetAnswer());
                    startActivity(intent);
                }
            });
            count++;
            LL.addView(but, LP);
        }
    }
}
