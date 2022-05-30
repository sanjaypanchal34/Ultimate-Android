package com.sk.ultimateplayerhq.utils;

import android.content.Context;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputLayout;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

public class InputUtils {

    public static TextView getEditText(@NonNull TextInputLayout layout) {
        return layout.getEditText();
    }

    public static String getText(@NonNull TextInputLayout layout) {
        return getEditText(layout).getText().toString();
    }

    public static String getTextWithTrim(@NonNull TextInputLayout layout) {
        return getEditText(layout).getText().toString().trim();
    }

    public static String getTextWithTrim(@NonNull EditText layout) {
        return layout.getText().toString().trim();
    }

    public static void hideKeyBoard(Context context, IBinder binder) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(binder, 0);
        }
    }


    public static boolean isNotValidEmail(@NonNull TextInputLayout layout) {
        boolean b = TextUtils.isEmpty(getTextWithTrim(layout)) || !android.util.Patterns.EMAIL_ADDRESS.matcher(getTextWithTrim(layout)).matches();
        layout.setErrorEnabled(b);
        return b;
    }


    public static boolean isNotValidEmail(@NonNull EditText layout) {
        boolean b = TextUtils.isEmpty(getTextWithTrim(layout)) || !android.util.Patterns.EMAIL_ADDRESS.matcher(getTextWithTrim(layout)).matches();
        return b;
    }

    public static boolean isEmpty(@NonNull TextInputLayout layout) {
        boolean b = getTextWithTrim(layout).isEmpty();
        layout.setErrorEnabled(b);
        return b;
    }

    public static String get2DigitNumber(Object number) {
        NumberFormat f = new DecimalFormat("00");
        return f.format(number);
    }

    public static boolean isMobile(@NonNull TextInputLayout layout) {
        boolean b = getTextWithTrim(layout).trim().length() == 10;
        layout.setErrorEnabled(!b);
        return b;
    }


    public static boolean isPassword(@NonNull TextInputLayout layout) {
        Pattern PASSWORD_PATTERN
                = Pattern.compile(
                "[a-zA-Z0-9!@#$]{8,24}");

        return TextUtils.isEmpty(getTextWithTrim(layout)) || !PASSWORD_PATTERN.matcher(getTextWithTrim(layout)).matches();
    }


    public static boolean isPassword(@NonNull EditText layout) {
        Pattern PASSWORD_PATTERN
                = Pattern.compile(
                "[a-zA-Z0-9!@#$]{8,24}");

        return TextUtils.isEmpty(getTextWithTrim(layout)) || !PASSWORD_PATTERN.matcher(getTextWithTrim(layout)).matches();
    }

    public static void setError(TextInputLayout layout, String error) {
        layout.setError(error);
    }

    public static void setText(String s, TextInputLayout layout) {
        getEditText(layout).setText(s);
    }

    public static String enter(String s) {
        return String.format("Enter %s", s);
    }

    public static String enterValid(String s) {
        return String.format("Enter Valid %s", s);
    }

    public static String twoDigit(int i) {
        return new DecimalFormat("00").format(i);
    }

    public static String get_dd_MM_yyyy(Date date) {
        return new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(date);
    }

    public static String getYYYY_MM_DD(String DD_MM_YYYY) {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(get_date3(DD_MM_YYYY));
    }

    public static Date get_date(String yyyy_MM_dd_hh_mm_ss) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH).parse(yyyy_MM_dd_hh_mm_ss);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    public static Date get_date2(String yyyy_MM_dd) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(yyyy_MM_dd);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    public static Date get_date3(String dd_mm_yyyy) {
        try {
            return new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(dd_mm_yyyy);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    public static int getCountOfDays(String createdDateString, String expireDateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        Date createdConvertedDate = null, expireCovertedDate = null, todayWithZeroTime = null;
        try {
            createdConvertedDate = dateFormat.parse(createdDateString);
            expireCovertedDate = dateFormat.parse(expireDateString);

            Date today = new Date();

            todayWithZeroTime = dateFormat.parse(dateFormat.format(today));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int cYear = 0, cMonth = 0, cDay = 0;

        if (createdConvertedDate.after(todayWithZeroTime)) {
            Calendar cCal = Calendar.getInstance();
            cCal.setTime(createdConvertedDate);
            cYear = cCal.get(Calendar.YEAR);
            cMonth = cCal.get(Calendar.MONTH);
            cDay = cCal.get(Calendar.DAY_OF_MONTH);

        } else {
            Calendar cCal = Calendar.getInstance();
            cCal.setTime(todayWithZeroTime);
            cYear = cCal.get(Calendar.YEAR);
            cMonth = cCal.get(Calendar.MONTH);
            cDay = cCal.get(Calendar.DAY_OF_MONTH);
        }


        Calendar eCal = Calendar.getInstance();
        eCal.setTime(expireCovertedDate);

        int eYear = eCal.get(Calendar.YEAR);
        int eMonth = eCal.get(Calendar.MONTH);
        int eDay = eCal.get(Calendar.DAY_OF_MONTH);

        Calendar date1 = Calendar.getInstance();
        Calendar date2 = Calendar.getInstance();

        date1.clear();
        date1.set(cYear, cMonth, cDay);
        date2.clear();
        date2.set(eYear, eMonth, eDay);

        long diff = date2.getTimeInMillis() - date1.getTimeInMillis();

        float dayCount = (float) diff / (24 * 60 * 60 * 1000);

        return (int) dayCount;
    }

    public static int monthsBetweenDates(Date startDate, Date endDate) {

        Calendar start = Calendar.getInstance();
        start.setTime(startDate);

        Calendar end = Calendar.getInstance();
        end.setTime(endDate);

        int monthsBetween = 0;
        int dateDiff = end.get(Calendar.DAY_OF_MONTH) - start.get(Calendar.DAY_OF_MONTH);

        if (dateDiff < 0) {
            int borrrow = end.getActualMaximum(Calendar.DAY_OF_MONTH);
            dateDiff = (end.get(Calendar.DAY_OF_MONTH) + borrrow) - start.get(Calendar.DAY_OF_MONTH);
            monthsBetween--;

            if (dateDiff > 0) {
                monthsBetween++;
            }
        } else {
            monthsBetween++;
        }
        monthsBetween += end.get(Calendar.MONTH) - start.get(Calendar.MONTH);
        monthsBetween += (end.get(Calendar.YEAR) - start.get(Calendar.YEAR)) * 12;
        return monthsBetween - 1;
    }

    public static Object getTag(TextInputLayout layout) {
        return layout.getTag();
    }

    public static String select(String s) {
        return String.format("Select %s", s);

    }
}