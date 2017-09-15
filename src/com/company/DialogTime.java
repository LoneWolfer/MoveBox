package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class DialogTime extends JDialog implements ActionListener,WindowListener {

        JComboBox tlvl = new JComboBox();
        JButton bok, bexit;
        JLabel mes = new JLabel("我是: " );

        Container m = null;
        MainFrame tf;

public DialogTime(Frame f){
        super(f,"推箱子", true);
        tf = (MainFrame) f;
        bok = new JButton("确定");
        bexit = new JButton("退出");
        tlvl.addItem("菜鸟级");
        tlvl.addItem("业余级");
        tlvl.addItem("竞赛级");

        m = this.getContentPane();
        m.setLayout(null);

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setResizable(false);


        mes.setBounds(70, 50, 50, 24);
        tlvl.setBounds(120,50, 120, 24);

        bok.setBounds(90, 130, 65, 24);
        bexit.setBounds(160, 130, 65, 24);

        bok.addActionListener(this);
        bexit.addActionListener(this);
        m.add(mes); m.add(tlvl);  m.add(bok); m.add(bexit);


        int w = 300 , h = 220;
        int x = (Toolkit.getDefaultToolkit().getScreenSize().width - w) / 2;
        int y = (Toolkit.getDefaultToolkit().getScreenSize().height - h) / 2;
        this.setBounds(x, y , w, h);
        this.addWindowListener(this);
        this.setVisible(true);
        }

public void actionPerformed(ActionEvent e) {
        if ( e.getSource().equals(bexit)){
        System.exit(0);
        }
        else{
        Object obj = tlvl.getSelectedItem();
        if ( obj.equals("菜鸟级")){
        tf.btime = 50;
        }
        else if ( obj.equals("业余级")){
        tf.btime = 30;
        }
        else if ( obj.equals("竞赛级")){
        tf.btime = 10;
        }
        }
        this.dispose();
        }

public void windowActivated(WindowEvent e) {
        // TODO 自动生成方法存根

        }

public void windowClosed(WindowEvent e) {
        // TODO 自动生成方法存根

        }

public void windowClosing(WindowEvent e) {
        System.exit(0);

        }

public void windowDeactivated(WindowEvent e) {
        // TODO 自动生成方法存根

        }

public void windowDeiconified(WindowEvent e) {
        // TODO 自动生成方法存根

        }

public void windowIconified(WindowEvent e) {
        // TODO 自动生成方法存根

        }

public void windowOpened(WindowEvent e) {
        // TODO 自动生成方法存根

        }
}
