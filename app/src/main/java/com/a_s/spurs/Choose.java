package com.a_s.spurs;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

public class Choose extends Activity {
    String[] files = null;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_choose);
        String path = Environment.getExternalStorageDirectory().toString()+"/spurs";
        files = new File(path).list();
        LinearLayout LL = findViewById(R.id.master);
        ViewGroup.LayoutParams LP = new ViewGroup.LayoutParams(-1, -2);
        if(files!=null)
        for(int i=0; i<files.length; i++)
        {
            if (files[i].endsWith(".html"))
            {
                Button but = new Button(this);
                but.setId(i);
                but.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        But_Click(v);
                    }
                });
                but.setText(new File(files[i]).getName());
                LL.addView(but, LP);
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "Другой процесс запрещает доступ к папке spurs. Пожалуйста, завершите его.", Toast.LENGTH_LONG).show();
            this.setResult(RESULT_CANCELED);
            this.finish();
        }
    }

    private void But_Click(View v)
    {
        int id = ((Button)v).getId();
        SetFileName(files[id]);
    }

    private void SetFileName(String file)
    {
        try {
            FileOutputStream fos = openFileOutput("ChoosedFile", Context.MODE_PRIVATE);
            fos.write((Environment.getExternalStorageDirectory().toString()+"/spurs/"+file).getBytes());
            fos.close();

        } catch (Throwable t) {
            Toast toast = Toast.makeText(getApplicationContext(), "Невозможно сохранить выбор!", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }this.setResult(RESULT_OK);
        this.finish();
    }
}
