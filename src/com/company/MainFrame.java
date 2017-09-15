package com.company;


import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class MainFrame extends JFrame implements KeyListener,ActionListener,Runnable {

    List pid = new ArrayList();  //目标地点id

    List bid = new ArrayList();  //箱子id

    List sid = new ArrayList();  //空间id

    List qid = new ArrayList();  //墙id

    String rid = "";

    int rows = 0;
    int cols = 0;

    int px, py; //人初始位置


    int x ,y ,w = 30 ,h = 30;
    BaseBox ren;
    int rrx, rry, bbx, bby;//撤消位置
    int bindex = -1;
    boolean isOver = false;

    int count;

    HashMap map = new HashMap(); //所有物体的集合
    //集合中key是特体的序号,value是物体BoaseBox对象

    int lvl = 1;  //第1关
    Container main ;

    JButton reset = new JButton("重玩");
    JButton next = new JButton("下一关");
    JButton goback = new JButton("上一关");

    JButton createMap = new JButton("自制地图");

    JButton back = new JButton("后悔");
    JButton exit = new JButton("退出");
    JLabel mesLvl = new JLabel("第 1 关");
    JLabel numlvl = new JLabel("");
    JLabel timelvl = new JLabel("");
    int times = 0;//时间限制
    int btime = 60;
    Thread thread = null;

    public MainFrame (){

        main = this.getContentPane();

        main.setLayout(null);

        this.setTitle("Java版推箱子");
        reset.setBounds(195,10,70,25);
        goback.setBounds(45,10,75,25);
        next.setBounds(120,10, 75,25);


        back.setBounds(265,10,70,25);
        exit.setBounds(340,10,70,25);
        createMap.setBounds(415,10,70,25);
        mesLvl.setBounds(200,50, 120,30);
        timelvl.setBounds(320,50, 120,30);
        numlvl.setBounds(100,60,100,25);

        timelvl.setFont(new Font("Snap ITC",0, 25));
        timelvl.setForeground(Color.red);
        mesLvl.setFont(new Font("方正姚体",0, 26));
        numlvl.setFont(new Font("宋体",0, 18));
        numlvl.setForeground(Color.blue);

        next.addActionListener(this);
        back.addActionListener(this);
        exit.addActionListener(this);
        goback.addActionListener(this);
        createMap.addActionListener(this);
        this.setDefaultCloseOperation(3);

        reset.addActionListener(this);
        reset.addKeyListener(this);
        back.addKeyListener(this);
        goback.addKeyListener(this);
        exit.addKeyListener(this);
        createMap.addKeyListener(this);
        next.addKeyListener(this);
        this.addKeyListener(this);


        int w1 = 700, h1 = 500;
        this.setSize(new Dimension(w1,h1));
        int x1 = (Toolkit.getDefaultToolkit().getScreenSize().width - w1) / 2;
        int y1 = (Toolkit.getDefaultToolkit().getScreenSize().height - h1) / 2;

        setBounds(x1,y1,w1,h1);
        setVisible(true);
        new DialogTime(this);
        init();
    }


    void init(){
        //读取文件，获取地图数据
        if ( !getMap() ){
            mesLvl.setText("第 " + lvl + " 关");
            return;
        }

        times = btime*pid.size();//每个箱子30秒
        count = 0; //一共进行了多少步
        main.removeAll();
        main.add(reset);
        main.add(goback);
        main.add(next);

        main.add(back);
        main.add(exit);
        main.add(createMap);
        main.add(mesLvl);
        numlvl.setText("已进行" + count + "步");
        main.add(numlvl);
        timelvl.setText("" + times);
        main.add(timelvl);

        back.setEnabled(false);
        reset.requestFocus(true);
        mesLvl.setText("第 " + lvl + " 关");


        isOver = false;
        int xx = 80;
        int yy = 100;
        int _type = 0;
        for ( int i = 0; i < rows*cols; i ++ ){
            if ( pid.contains("" + (i+1)) ){
                _type = 0;
            }else if ( bid.contains("" + (i+1))){  //箱子
                _type = 1;
            }else if ( rid.equals("" + (i+1))){  //人位
                _type = 2;
            }else if ( sid.contains("" + (i+1))){ //空间
                _type = 3;
            }else if ( qid.contains("" + (i+1))){
                _type = 4;
            }
            if ( _type != 3 ){
                BaseBox b = new BaseBox(_type,xx,yy);
                map.put("" + (i+1), b);
                if ( rid.equals("" + (i+1))){
                    ren = b;
                    px = xx; py = yy;  x = xx; y = yy;
                    main.add(ren);
                }
            }

            if ( (i+1)% cols == 0 ){
                yy += 30;
                xx = 80;
            }
            else {
                xx += 30;
            }
        }
        Vector vv = new Vector(map.values());
        for ( int i = 0; i < vv.size(); i ++ ){
            BaseBox b  = (BaseBox) vv.get(i);
            if ( b.type == 1 ){
                main.add(b);
            }
        }
        for ( int i = 0; i < vv.size(); i ++ ){
            BaseBox b  = (BaseBox) vv.get(i);
            if ( b.type == 0 ){
                main.add(b);
            }
        }
        for ( int i = 0; i < vv.size(); i ++ ){
            BaseBox b  = (BaseBox) vv.get(i);
            if ( b.type == 3 ){
                main.add(b);
            }
        }
        for ( int i = 0; i < vv.size(); i ++ ){
            BaseBox b  = (BaseBox) vv.get(i);
            if ( b.type == 4 ){
                main.add(b);
            }
        }
        this.repaint();
        if ( thread != null && thread.isAlive() )
            thread.stop();
        thread = new Thread(this);
        thread.start();
        reset.requestFocus(true);
    }


    public boolean getMap(){
        if ( lvl < 1 ){
            JOptionPane.showMessageDialog(this,"这已经是第一关了！");
            lvl = 1;
            return false;
        }
        try {
            InputStream in = this.getClass().getResourceAsStream("data/box.dat");
            InputStreamReader isr = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(isr);
            for ( int i = 0; i < (lvl-1)*5; i ++ ){
                br.readLine();
            }
            String[] line = br.readLine().trim().split(",");
            rows = Integer.parseInt(line[0]);
            cols = Integer.parseInt(line[1]);

            pid.clear(); bid.clear(); sid.clear();  qid.clear();  map.clear();
            line = br.readLine().trim().split(",");
            for ( int i = 0; i < line.length; i ++ ){
                pid.add(line[i]);
            }

            line = br.readLine().trim().split(",");
            for ( int i = 0; i < line.length; i ++ ){
                bid.add(line[i]);
            }

            rid = br.readLine().trim();

            line = br.readLine().trim().split(",");
            for ( int i = 0; i < line.length; i ++ ){
                sid.add(line[i]);
            }

            for ( int i = 1; i <= rows*cols; i ++ ){
                String si = "" + i;
                if ( !rid.equals(si) && !pid.contains(si) && ! bid.contains(si) && ! sid.contains(si)){
                    qid.add(si);
                }
            }

            br.close();
            isr.close();
            in.close();
        } catch (Exception e) {
            System.out.println("数据文件不存在或数据为空！");
            JOptionPane.showMessageDialog(this,"这已经是最后一关了！");
            lvl --; return false;
        }
        return true;
    }



    public void keyPressed(KeyEvent e) {
        if ( isOver ){
            back.setEnabled(false);
            return;
        }
        int c = e.getKeyCode();
        if ( c != 37 && c != 38 && c != 39 && c != 40 )
            return;

        int rx = x;
        int ry = y;

        if ( c == 37 ){
            x -= 30;
        }else if ( c == 38 ){
            y -= 30;
        }else if ( c == 39 ){
            x += 30;
        }else if ( c == 40 ){
            y += 30;
        }
        bindex = -1;
        Vector v = new Vector(map.values());
        for ( int i = 0; i < v.size(); i ++ ){
            BaseBox temp = (BaseBox) v.get(i);
            if ( x == temp.getX() && y == temp.getY()){
                //前面是墙走不动
                if ( temp.type == 4 ){
                    x = rx; y = ry;
                    bindex = -1;
                    return;
                }
                //前面是箱子，判断箱子前面是什么
                else if ( temp.type == 1 ){
                    int nx = 30; int ny = 30;
                    //先要知道用户是向左还是上还是右还是下
                    if ( rx > x ){
                        nx = -30;
                        ny = 0;
                    } else if ( rx < x ){
                        ny = 0;
                    }
                    if ( ry > y ){
                        ny = -30;
                        nx = 0;
                    } else if ( ry < y ){
                        nx = 0;
                    }
                    int tx = x + nx;   int ty = y + ny;
                    boolean fg = false; //是否换图像
                    for ( int j = 0; j < v.size(); j ++ ){
                        BaseBox obj = (BaseBox) v.get(j);
                        if ( tx == obj.getX() && ty == obj.getY() ){
                            //前面是墙或箱子，推不动
                            if ( obj.type == 4 || obj.type == 1 ){
                                x = rx; y = ry;
                                return;
                            }
                            else if ( obj.type == 0 ){
                                //检查这个目的点上有没有箱子
                                boolean ck = false;
                                for ( int k = 0; k < v.size(); k ++){
                                    if ( k == j )  continue;
                                    BaseBox kb = (BaseBox) v.get(k);
                                    if ( kb.getX() == obj.getX() && kb.getY() == obj.getY() && kb.type == 1){
                                        ck = true; break;
                                    }
                                }
                                if ( ck){
                                    x = rx; y = ry; return;
                                }
                                fg = true;
                                break;
                            }else {
                                break;
                            }
                        }
                    }
                    if ( fg ) {
                        temp.setImg("gif/ok.gif");
                        temp.repaint();
                    }
                    else if ( temp.getFileName().equals("gif/ok.gif")){
                        temp.setImg("gif/box.gif");
                        temp.repaint();
                    }
                    //可以移动箱子
                    bbx = temp.getX(); bby = temp.getY();
                    bindex = i;
                    temp.move(tx, ty);
                }
            }
        }
        ren.move(x, y);
        count ++;
        this.numlvl.setText("已进行" + count + "步");
        rrx = rx; rry = ry;
        back.setEnabled(true);
        if ( check()){
            isOver = true;
            JOptionPane.showMessageDialog(this, "恭喜，过关！您一共推了 " + count  + " 步。");
        }
    }


    public boolean check(){
        int cnt = 0;
        Vector v = new Vector(map.values());
        Vector bv = new Vector();
        Vector pv = new Vector();
        for ( int i = 0; i < v.size(); i ++ ){
            BaseBox temp = (BaseBox) v.get(i);
            if ( temp.type == 1){
                bv.add(temp);
            }else  if ( temp.type == 0 ){
                pv.add(temp);
            }
        }

        for ( int i = 0; i < bv.size(); i ++ ){
            BaseBox temp = (BaseBox) bv.get(i);
            for ( int j = 0; j < pv.size(); j ++ ){
                BaseBox tb = (BaseBox) pv.get(j);
                if ( tb.getX() == temp.getX() && tb.getY() == temp.getY()){
                    cnt ++;

                    break;
                }
            }
        }
        if ( cnt == pv.size() )  return true;

        return false;
    }




    public void keyReleased(KeyEvent e) {
        // TODO 自动生成方法存根

    }






    public void keyTyped(KeyEvent e) {
        // TODO 自动生成方法存根

    }


    public void actionPerformed(ActionEvent e) {
        if ( e.getSource().equals(reset)){
            if ( JOptionPane.showConfirmDialog(this,"你真的要重玩本关吗？","退出", JOptionPane.YES_NO_OPTION) == 0){
                times = 0;
                init();
            }
        }
        else if ( e.getSource().equals(back)){
            if ( bindex != -1 ){
                Vector v = new Vector(map.values());
                BaseBox temp = (BaseBox) v.get(bindex);
                temp.move(bbx,bby);
            }
            ren.move(rrx, rry);
            x = rrx; y = rry;
            back.setEnabled(false);
        }
        else if ( e.getSource().equals(exit)){
            if ( JOptionPane.showConfirmDialog(this,"你真的要退出吗？","退出", JOptionPane.YES_NO_OPTION) == 0){
                System.exit(0);
            }
        }
        else if (e.getSource().equals(createMap)){
            Build build = new Build();
        }
        else if ( e.getSource().equals(next)){
            lvl ++;
            init();
        }else if ( e.getSource().equals(goback)){
            lvl --;
            init();
        }
    }


    public void run() {
        while ( times > -1 && !isOver ){
            this.timelvl.setText("" + times);
            try {
                thread.sleep(1000);
                times --;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if ( times == -1 ){
            JOptionPane.showMessageDialog(this,"时间到了！重新开始吧。");
            isOver = true;
        }
    }
}

