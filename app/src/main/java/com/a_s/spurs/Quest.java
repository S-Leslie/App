package com.a_s.spurs;

import java.util.ArrayList;

public class Quest {
    String text;
    String answer = "";
    public Quest(String str)
    {
        // Делим строку на ячейки (должно получиться 2 штуки)
        str = str.replace("</td>", "<td>");
        String[] que = str.split("<td>");
        ArrayList<String> q = new ArrayList<String>();
        for (String s : que)
            if (!s.equals(""))
                q.add(s);
        // Заносим вопрос
        text = q.get(0);
        // Пересохрняем ответы
        String temp = q.get(1);
        // Преобразуем строку
        temp = temp.replace("</p>", "");
        temp = temp.replace("\"></img>", "");
        temp = temp.replace("<img src=\"", "<p>:i:");
        // Пока есть строки
        String[] parts = temp.split("<p>");
        for (String i : parts)
        {
            if (!i.equals(""))
                answer += i + "\n";
        }
    }

    public String GetQuestion() { return text; }
    public String GetAnswer() { return answer; }
}
