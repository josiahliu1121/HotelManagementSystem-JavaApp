package com;

import com.utils.JdbcExecutor;
import com.views.LoginFrame;
import com.views.MainFrame;

import javax.swing.*;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}