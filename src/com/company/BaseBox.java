package com.company;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JPanel;


public class BaseBox extends JPanel {

    int w = 30, h = 30;

    int type = 0; //物体的类型，默认为目的地，1为箱子，2为人位，3为空间，4为墙

    Image img = null;
    String filename = "";

    public BaseBox(int type, int x, int y){
        this.type = type;
        this.setBounds(x,y,w,h);
        if ( type == 0 )
            filename = "gif/pot.gif";
        else if ( type == 1 )
            filename = "gif/box.gif";
        else if ( type == 2 )
            filename = "gif/ren.gif";
        else if ( type == 3 )
            filename = "gif/box.gif";
        else
            filename = "gif/qiang.gif";
        img = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource(filename));
    }


    public void setImg(String s){
        img = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource(s));
        filename = s;
    }

    public String getFileName(){
        return filename;
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(img,0,0,this);
    }
}

