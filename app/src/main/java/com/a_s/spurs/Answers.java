package com.a_s.spurs;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class Answers extends Activity {
    ArrayList<String> lines = new ArrayList<>();
    String path;
    @Override
    protected void onCreate(Bundle savedInstanceState )
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_answer);
        String text = getIntent().getStringExtra("text");
        String answ = getIntent().getStringExtra("answ");
        ScrollView SV = findViewById(R.id.ans_scroll);
        LinearLayout LL = findViewById(R.id.ans_mast);
        LinearLayout.LayoutParams LP = new LinearLayout.LayoutParams(-1, -2);
        EditText et1 = findViewById(R.id.et1);
        et1.setText(text);
        et1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Et_LongClick(v);
                return false;
            }
        });
        path = Environment.getExternalStorageDirectory().toString() + "/spurs";
        GetLines(answ);
        String tempAns = "";
        for (int i = 0; i < lines.size(); i++)
        {
            if (lines.get(i).startsWith(":i:")) {
                try {
                    if (!tempAns.equals("")) {
                        EnterText(tempAns, LL);
                        tempAns = "";
                    }
                    Uri imageName = Uri.parse(path + "/" + lines.get(i).replaceFirst(":i:", ""));
                    ///
                    Bitmap BM = BitmapFactory.decodeFile(imageName.toString());
                    ///
                    ImageView IV = new ImageView(this);
                    IV.setImageBitmap(BM);
                    IV.setScaleType(ImageView.ScaleType.FIT_START);
                    int width = getWindowManager().getDefaultDisplay().getWidth();
                    int height = width*BM.getHeight()/BM.getWidth();
                    IV.setLayoutParams(new ViewGroup.LayoutParams(width, height));
                    IV.setId(i);
                    IV.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            IV_LongClick(v);
                            return false;
                        }
                    });
                    LL.addView(IV);
                } catch (Exception e)
                {
                    Toast toast = Toast.makeText(getApplicationContext(), "Ошибка открытия изображения!", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }else tempAns += lines.get(i) + "\n";
        }
        if (!tempAns.equals(""))
            EnterText(tempAns, LL);
    }

    private void GetLines(String text)
    {
        String[] parts = text.split("\n");
        for (String i : parts)
        lines.add(i);
    }

    private void EnterText(String text, LinearLayout LL)
    {
        TextView TV = new TextView(this);
        TV.setText(text);
        TV.setTextColor(Color.BLACK);
        TV.setPadding(10, 0, 10, 0);
        TV.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                TV_LongClick(v);
                return false;
            }
        });
        LL.addView(TV);
    }

    private void TV_LongClick(View v)
    {
        String str = ((TextView)v).getText().toString();
        ClipboardManager myClipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData myClip = ClipData.newPlainText("text", str);
        myClipboard.setPrimaryClip(myClip);
        Toast.makeText(getApplicationContext(), "Текст скопирован", Toast.LENGTH_SHORT).show();
    }

    private void Et_LongClick(View v)
    {
        String str = ((EditText)v).getText().toString();
        ClipboardManager myClipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData myClip = ClipData.newPlainText("text", str);
        myClipboard.setPrimaryClip(myClip);
        Toast.makeText(getApplicationContext(), "Текст скопирован", Toast.LENGTH_SHORT).show();
    }

    private void IV_LongClick(View v)
    {

        Intent intent = new Intent(Answers.this, Image.class);
        String uri = lines.get(((ImageView) v).getId()).replace(":i:", "");
        intent.putExtra("img", path + "/" + uri);
        startActivity(intent);
    }
}
