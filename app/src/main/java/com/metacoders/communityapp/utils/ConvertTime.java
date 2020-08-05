package com.metacoders.communityapp.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ConvertTime {

    private static final String LIST_SEPARATOR = ",";

    public static String listKeString (List<String> list){
        StringBuilder sb = new StringBuilder();
        for (String str : list) {
            sb.append(str).append(LIST_SEPARATOR);
        }
        sb.setLength(sb.length() - LIST_SEPARATOR.length());
        return sb.toString();
    }

    public static ArrayList<String> stringKeList(String s){
        return new ArrayList<>(Arrays.asList(s.split(LIST_SEPARATOR)));
    }


    public static String capitalize(String input){
        String[] katas = input.toLowerCase().split(" ");
        StringBuilder builder = new StringBuilder();
        for (int i=0; i<katas.length; i++){
            String kata = katas[i];
            if (i>0 && kata.length()>0){
                builder.append(" ");
            }
            String cap = kata.substring(0,1).toUpperCase() + kata.substring(1);
            builder.append(cap);
        }
        return builder.toString();
    }

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    private static Date currentDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    public static String getTimeAgo(Date date) {
        long time = date.getTime();
        if (time < 1000000000000L) {
            time *= 1000;
        }

        long now = currentDate().getTime();
        if (time > now || time <= 0) {
            return "just now";
        }

        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "moments ago";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 60 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 2 * HOUR_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else if (diff < 3 * DAY_MILLIS){
            return diff / DAY_MILLIS + " days ago";
        } else {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat th = new SimpleDateFormat("yyyy");
            int tahun = Integer.parseInt(th.format(date));
            if (Calendar.getInstance().get(Calendar.YEAR) == tahun){
                @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy");
                return format.format(date);
            } else {
                @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy");
                return df.format(date);
            }
        }
    }
    public static int cekHari(String tgl) {
        SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy",Locale.getDefault());
        SimpleDateFormat nf = new SimpleDateFormat("dd MM yyyy",Locale.getDefault());
        try {
            Date date = df.parse(tgl);
            String ntgl = nf.format(date);
            int getTgl = ntgl.indexOf(" ");
            int getBln = ntgl.indexOf(" ",getTgl+1);
            int getThn = ntgl.indexOf(" ",getBln+1);
            int bln = Integer.parseInt(ntgl.substring(getTgl+1,getBln))+1;
            int hari = Integer.parseInt(ntgl.substring(0,getTgl));
            int tahun = Integer.parseInt(ntgl.substring(getBln+1));
            Calendar calendar = Calendar.getInstance();
            int thn_now = calendar.get(Calendar.YEAR);
            int bln_now = calendar.get(Calendar.MONTH);
            int tgl_now = calendar.get(Calendar.DAY_OF_MONTH);

            if (thn_now == tahun && bln_now == bln){
                return hari - tgl_now;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

}