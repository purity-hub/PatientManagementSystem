import util.JDBCCon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class Login {

    public Login(){
        //加载图片
        ImageIcon icon=new ImageIcon("D:\\PatientManagementSystem\\src\\images\\background.jpg");
        //将图片放入label中
        JLabel label=new JLabel(icon);
        //设置label的大小
        label.setBounds(0,0,icon.getIconWidth(),icon.getIconHeight());
        JFrame frame=new JFrame();
        //获取窗口的第二层，将label放入
        //noinspection removal
        frame.getLayeredPane().add(label,new Integer(Integer.MIN_VALUE));
        //获取frame的顶层容器,并设置为透明
        JPanel j=(JPanel)frame.getContentPane();
        j.setOpaque(false);
        JPanel panel=new JPanel();
        JLabel usernameLabel = new JLabel("用户名");
        JTextField usernametext = new JTextField(10);
        JLabel passwordLabel = new JLabel("密码");
        JTextField passwordtext = new JPasswordField(10);
        panel.setLayout(new GridLayout(3,2,10,10));
        JButton regist = new JButton("注册");
        JButton login = new JButton("登录");
        panel.add(usernameLabel);
        panel.add(usernametext);
        panel.add(passwordLabel);
        panel.add(passwordtext);
        panel.add(regist);
        panel.add(login);
        //必须设置为透明的。否则看不到图片
        panel.setOpaque(false);
        frame.setLayout(new FlowLayout());
        frame.add(panel);
        frame.setSize(600,500);
        frame.setLocation(500,250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        //注册
        regist.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame1 = new JFrame("注册");
                frame1.setSize(190,120);
                JPanel jPanel = new JPanel();
                JLabel usernamel = new JLabel("用户名");
                JTextField usernamet = new JTextField(10);
                JLabel pwdl = new JLabel("密码");
                JTextField pwdt = new JTextField(10);
                JButton sure = new JButton("注册");
                jPanel.add(usernamel);
                jPanel.add(usernamet);
                jPanel.add(pwdl);
                jPanel.add(pwdt);
                jPanel.add(sure);
                frame1.add(jPanel);
                frame1.setLocation(550,280);
                frame1.setVisible(true);
                sure.addActionListener(new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String username = usernamet.getText();
                        String password = pwdt.getText();
                        JDBCCon con = new JDBCCon();
                        ResultSet query = con.query("select username from nurse");
                        ArrayList<String> usernameList = new ArrayList<>();
                        while(true){
                            try {
                                if (!query.next()) break;
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                            try {
                                usernameList.add(query.getString("username"));
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        }
                        if(usernameList.contains(username)){
                            JOptionPane.showMessageDialog(null,"用户名重复","错误",JOptionPane.ERROR_MESSAGE);
                        }else{
                            con.add("insert into nurse(username,password) values(?,?)",username,password);
                            JOptionPane.showMessageDialog(null,"注册成功","成功",JOptionPane.INFORMATION_MESSAGE);
                            frame1.setVisible(false);
                        }
                    }
                });
            }
        });
        //登录
        login.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernametext.getText();
                String password = passwordtext.getText();
                JDBCCon con = new JDBCCon();
                ResultSet query = con.query("select username from nurse");
                ArrayList<String> usernameList = new ArrayList<>();//用户名列表
                while (true){
                    try {
                        if (!query.next()) break;
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    try {
                        usernameList.add(query.getString("username"));
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
                ResultSet query1 = con.query("select password from nurse where username=?", username);
                String Tpassword="";
                while(true){
                    try {
                        if (!query1.next()) break;
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    try {
                        Tpassword=query1.getString("password");
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
                if(usernameList.contains(username) && password.equals(Tpassword)){
                    //正确
                    frame.setVisible(false);
                    Main main = new Main();
                    main.setUsername(username);//传递用户名
                    System.out.println(main.getUsername());
                }
                if(usernameList.contains(username) && !password.equals(Tpassword)){
                    //密码错误
                    JOptionPane.showMessageDialog(null,"密码错误","错误",JOptionPane.ERROR_MESSAGE);
                }
                if(!usernameList.contains(username)){
                    //没有这个用户名
                    JOptionPane.showMessageDialog(null,"不存在此用户","错误",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
    public static void main(String[] args)
    {
        new Login();
    }
}
