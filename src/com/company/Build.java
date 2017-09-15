package com.company;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class Build extends JFrame implements ActionListener{

    int row , col;

    JButton b1,b2,b3,b4,reset,exit, bok;

    Container main;

    JPanel bp = new JPanel();

    JPanel cp = new JPanel();

    ArrayList sid, bid, pid;
    String rid ;
    ArrayList bs;

    public Build(){
        main = this.getContentPane();
        this.setTitle("地图生成器");

        b1 = new JButton("目的");
        b2 = new JButton("箱子");
        b3 = new JButton("人位");
        b4 = new JButton("空间");

        reset = new JButton("重置");
        bok = new JButton("保存");
        exit = new JButton("退出");

        b1.addActionListener(this);
        b2.addActionListener(this);
        b3.addActionListener(this);
        b4.addActionListener(this);
        reset.addActionListener(this);
        exit.addActionListener(this);
        bok.addActionListener(this);

        cp = new JPanel();


        bp.add(b1); bp.add(b2); bp.add(b3); bp.add(b4); bp.add(bok);
        bp.add(reset); bp.add(exit);

        main.add(bp, BorderLayout.SOUTH);
        main.add(cp,BorderLayout.CENTER);
        this.setDefaultCloseOperation(3);
        setBounds(200,150,500,450);
        //this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
        reset();
        this.validate();
    }

    Color color = null;

    public void reset(){

        String rrr = JOptionPane.showInputDialog(this,"请输入行数");
        String ccc = JOptionPane.showInputDialog(this,"请输入列数");
        if ( rrr == null || ccc == null ){
            return;
        }
        try{
            row = Integer.parseInt(rrr);
            col = Integer.parseInt(ccc);
        }catch(Exception e){
            JOptionPane.showMessageDialog(this,"行列只能指定为数字！");
            return;
        }
        cp.setLayout(new GridLayout(row,col));
        cp.removeAll();
        color = null;
        b1.setEnabled(true);b2.setEnabled(false);b3.setEnabled(false);b4.setEnabled(false);
        bok.setEnabled(false);

        bs = new ArrayList();
        for ( int i = 0; i < row*col; i ++ ){
            Button tb = new Button("" + (i+1));
            tb.addActionListener(this);
            cp.add(tb);
            bs.add(tb);
        }
        pid = new ArrayList();
        bid = new ArrayList();

        sid = new ArrayList();
        cp.validate();

        for ( int i = 0; i <= col; i ++ ){
            Button bb = (Button) bs.get(i);
            bb.setEnabled(false);
            bb.setBackground(Color.black);
        }
        for ( int i = col*row-1; i > col*row - col; i --){
            Button bb = (Button) bs.get(i);
            bb.setEnabled(false);
            bb.setBackground(Color.black);
        }
        for ( int i = col*2-1; i < col * row ; i ++ ){
            if ( i % col == 0 || (i+col+1) % col == 0 ){
                Button bb = (Button) bs.get(i);
                bb.setEnabled(false);
                bb.setBackground(Color.black);
            }
        }
    }

    private void setStat(ArrayList a){
        for ( int i = 0; i < a.size(); i ++ ){
            int index = (Integer.parseInt(a.get(i).toString())) -1;
            Button b = (Button) bs.get(index);
            b.setEnabled(false);
        }
    }
    private void setStat(String id){
        int index = (Integer.parseInt(id)) -1;
        Button b = (Button) bs.get(index);
        b.setEnabled(false);
    }
    public void actionPerformed(ActionEvent e) {
        if ( e.getSource().equals(exit)){
            System.exit(0);
        }else if ( e.getSource().equals(reset)){
            reset();

        }

        else if ( e.getSource().equals(b1)){
            color = Color.blue;
            b1.setEnabled(false);
            b2.setEnabled(true);
        }
        else if ( e.getSource().equals(b2)){
            color = Color.green;
            b2.setEnabled(false);
            b3.setEnabled(true);
            setStat(pid);
        }
        else if ( e.getSource().equals(b3)){
            color = Color.yellow;
            b3.setEnabled(false);
            b4.setEnabled(true);
            setStat(bid);
        }
        else if ( e.getSource().equals(b4)){
            color = Color.cyan;
            bok.setEnabled(true);
            b4.setEnabled(false);
            setStat(rid);
        }
        else if ( e.getSource().equals(bok)){
            if ( JOptionPane.showConfirmDialog(this,"真的要保存？") == 0 ){
                bok.setEnabled(false);
                try {
                    FileWriter fos = new FileWriter("box.dat",true);
                    BufferedWriter bw = new BufferedWriter(fos);
                    bw.write(row + "," + col);
                    bw.newLine();
                    for ( int i = 0; i < pid.size(); i ++ ){
                        if ( i != pid.size()-1 ){
                            bw.write(pid.get(i).toString() + ",");
                        }
                        else {
                            bw.write(pid.get(i).toString());
                        }
                    }
                    bw.newLine();
                    for ( int i = 0; i < bid.size(); i ++ ){
                        if ( i != bid.size()-1 ){
                            bw.write(bid.get(i).toString() + ",");
                        }
                        else {
                            bw.write(bid.get(i).toString());
                        }
                    }
                    bw.newLine();
                    bw.write(rid);
                    bw.newLine();
                    for ( int i = 0; i < sid.size(); i ++ ){
                        if ( i != sid.size()-1 ){
                            bw.write(sid.get(i).toString() + ",");
                        }
                        else {
                            bw.write(sid.get(i).toString());
                        }
                    }
                    bw.newLine();
                    bw.close();
                    fos.close();
                    JOptionPane.showMessageDialog(this,"保存成功");
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }else {
            Button b = (Button)e.getSource();
            if ( color == null ){
                return;
            }
            if ( color.equals(Color.blue)){
                if ( pid.contains(b.getActionCommand())){
                    pid.remove(b.getActionCommand());
                    b.setBackground(new Color(212,208,200));
                    return;
                }else
                    pid.add(b.getActionCommand());
            }else if ( color.equals(Color.green)){
                if ( bid.contains(b.getActionCommand())){
                    bid.remove(b.getActionCommand());
                    b.setBackground(new Color(212,208,200));
                    return;
                }else
                    bid.add(b.getActionCommand());

            } else if ( color.equals(Color.yellow)){
                if ( rid != null && rid.equals(b.getActionCommand())){
                    rid = "";
                    b.setBackground(new Color(212,208,200));
                    return;
                }else{
                    rid = b.getActionCommand();
                    if ( rid != null && !rid.equals("")){
                        Button temp = (Button) bs.get(Integer.parseInt(rid)-1);
                        temp.setBackground(new Color(212,208,200) );
                    }
                }
            } else if ( color.equals(Color.cyan)){
                if ( sid.contains(b.getActionCommand())){
                    sid.remove(b.getActionCommand());
                    b.setBackground(new Color(212,208,200));
                    return;
                }else
                    sid.add(b.getActionCommand());
            }
            b.setBackground(color);
        }

    }

}

