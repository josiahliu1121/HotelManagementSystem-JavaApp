package com.utils;

import javax.swing.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

// Formatter for date picker
public class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public Object stringToValue(String text) throws ParseException {
        return dateFormat.parse(text);
    }

    @Override
    public String valueToString(Object value) throws ParseException {
        if (value instanceof Date) {
            return dateFormat.format((Date) value);
        } else if (value instanceof Calendar) {
            return dateFormat.format(((Calendar) value).getTime()); // Convert Calendar to Date
        }
        return "";
    }
}
