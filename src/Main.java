import util.JDBCCon;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

public class Main {
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Main() {
        JFrame frame = new JFrame("病人就医管理");//新建容器对象
        frame.setSize(600,500);//设置容器大小
        frame.setLocation(500,250);//设置位置
        JTabbedPane jTabbedPane = new JTabbedPane();
        //挂号
        JPanel registered = new JPanel();
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(10,3,1,5));
        JLabel pid = new JLabel("身份证");
        JTextField pidtext = new JTextField(15);
        pidtext.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(pidtext.getText().length()>=18){
                    pidtext.setText(pidtext.getText().substring(0,18));
                }
            }
        });
        JLabel pname = new JLabel("姓名");
        JTextField pnametext = new JTextField(15);
        JLabel psex = new JLabel("性别");
        JComboBox<String> psexbox = new JComboBox<>();
        psexbox.addItem("男");
        psexbox.addItem("女");
        JLabel page = new JLabel("年龄");
        JTextField pagetext = new JTextField(15);
        JLabel ptel = new JLabel("联系电话");
        JTextField pteltext = new JTextField(15);
        pteltext.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(pteltext.getText().length()>=11){
                    pteltext.setText(pteltext.getText().substring(0,11));
                }
            }
        });
        JLabel pathogen = new JLabel("症状");
        JTextField pathogentext = new JTextField();
        JLabel department = new JLabel("科室");
        JComboBox<String> departmentbox = new JComboBox<>();
        JLabel doctor = new JLabel("医生");//联动效果
        JComboBox<String> doctorbox = new JComboBox<>();
        JDBCCon con = new JDBCCon();
        ResultSet query = con.query("select distinct department from doctor");
        ResultSet query2 = con.query("select dname from doctor where department='内科'");
        ArrayList<String> departments = new ArrayList<>();
        ArrayList<String> dnames = new ArrayList<>();
        while (true){
            try {
                if (!query.next() ||!query2.next()) break;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                departments.add(query.getString("department"));
                dnames.add(query2.getString("dname"));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        for (String s : departments) {
            departmentbox.addItem(s);
        }
        for (String dname : dnames) {
            doctorbox.addItem(dname);
        }
        departmentbox.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nowdepartment = departmentbox.getItemAt(departmentbox.getSelectedIndex());
                doctorbox.removeAllItems();
                ResultSet query1 = con.query("select dname from doctor where department=?", nowdepartment);
                while (true){
                    try {
                        if (!query1.next()) break;
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    try {
                        doctorbox.addItem(query1.getString("dname"));
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        JButton regist = new JButton("挂号");
        JButton clear = new JButton("清空");
        jPanel.add(pid);
        jPanel.add(pidtext);
        jPanel.add(pname);
        jPanel.add(pnametext);
        jPanel.add(psex);
        jPanel.add(psexbox);
        jPanel.add(page);
        jPanel.add(pagetext);
        jPanel.add(ptel);
        jPanel.add(pteltext);
        jPanel.add(pathogen);
        jPanel.add(pathogentext);
        jPanel.add(department);
        jPanel.add(departmentbox);
        jPanel.add(doctor);
        jPanel.add(doctorbox);
        jPanel.add(regist);
        jPanel.add(clear);
        jPanel.add(new JLabel("这个本来是刷卡后获取数据的,由于"));
        jPanel.add(new JLabel("没有相应的设备,故采用人工填写"));
        registered.add(jPanel);
        //排队
        JPanel lineup = new JPanel();
        final String[] lineupmsg={"序号","科室","姓名","就诊医生"};
        ResultSet query1 = con.query("select visitnumber,department,pname,dname from queue,doctor,patient where doctor.did=queue.did and queue.pid=patient.pid and starttime is not null and endtime is null order by department,dname");
        //表格信息
        final Vector<Vector<Comparable>> vect = new Vector();//初始化向量
        //表格模型
        AbstractTableModel tm = new AbstractTableModel() {// 实现AbstractTableModel的抽象方法
            private static final long serialVersionUID = 1L;

            public int getColumnCount() {
                return lineupmsg.length;
            }

            public int getRowCount() {
                // TODO 自动生成的方法存根
                return vect.size();
            }

            public Object getValueAt(int row, int column) {
                // TODO 自动生成的方法存根
                if (!vect.isEmpty())
                    return (((Vector<?>) vect.elementAt(row)).elementAt(column));
                else
                    return null;
            }

            public String getColumnName(int column) {
                return lineupmsg[column];// 设置表格列名
            }

            public void setValueAt(Object value, int row, int column) {
            }

            public Class<? extends Object> getColumnClass(int c) {
                return getValueAt(0, c).getClass();
            }// 取得所属对象类

            public boolean isCellEditable(int row, int column) {

                return false;
            }// 设置单元格不可编辑
        };
        //新建排队信息表格类
        JTable queueTable = new JTable(tm);
        queueTable.setRowHeight(20);
        queueTable.setPreferredScrollableViewportSize(new Dimension(560,200));
        queueTable.setToolTipText("显示所有的排队信息");
        //排队信息表自动调整
        queueTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        //设置单元格分割线
        queueTable.setShowHorizontalLines(true);
        queueTable.setShowVerticalLines(true);
        //滚轮,将滚轮绑定表格
        JScrollPane scrollPane = new JScrollPane(queueTable);
        //将获取到的数据加进表格中
        vect.removeAllElements();// 初始化向量对象
        tm.fireTableStructureChanged();// 更新表格内容
        while (true) {
            try {
                if (!query1.next()) break;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            @SuppressWarnings("rawtypes")
            Vector<Comparable> v = new Vector<Comparable>();
            try {
                v.add(query1.getInt(1));
                v.add(query1.getString(2));
                v.add(query1.getString(3));
                v.add(query1.getString(4));
                vect.add(v);
                tm.fireTableStructureChanged();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        JLabel label = new JLabel("科室");
        JComboBox<String> comboBox = new JComboBox<>();
        JLabel label1 = new JLabel("医生");
        JComboBox<String> comboBox1 = new JComboBox<>();
        ResultSet query3 = con.query("select distinct department from queue,doctor where queue.did=doctor.did and starttime is not null and endtime is null");
        while(true){
            try {
                if (!query3.next()) break;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                comboBox.addItem(query3.getString("department"));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        comboBox.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                comboBox1.removeAllItems();
                ResultSet query4 = con.query("select distinct dname from queue,doctor where queue.did=doctor.did and starttime is not null and endtime is null and department=?",comboBox.getItemAt(comboBox.getSelectedIndex()));
                while(true){
                    try {
                        if (!query4.next()) break;
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    try {
                        comboBox1.addItem(query4.getString("dname"));
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        JButton search = new JButton("查询");
        JLabel ceshi = new JLabel("测试");
        JLabel jLabel = new JLabel("  目前排队人数:");
        lineup.add(jLabel);
        lineup.add(ceshi);
        JLabel jLabel1 = new JLabel("人     ");
        lineup.add(jLabel1);
        jLabel.setVisible(false);
        jLabel1.setVisible(false);
        ceshi.setVisible(false);
        search.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String itemAt = comboBox1.getItemAt(comboBox1.getSelectedIndex());
                ResultSet query4 = con.query("select visitnumber,department,pname,dname from queue,doctor,patient where doctor.did=queue.did and queue.pid=patient.pid and starttime is not null and endtime is null and dname=?", itemAt);
                //刷新页面
                vect.removeAllElements();// 初始化向量对象
                tm.fireTableStructureChanged();// 更新表格内容
                while (true) {
                    try {
                        if (!query4.next()) break;
                    } catch (SQLException ec) {
                        ec.printStackTrace();
                    }
                    @SuppressWarnings("rawtypes")
                    Vector<Comparable> v = new Vector<Comparable>();
                    try {
                        v.add(query4.getInt(1));
                        v.add(query4.getString(2));
                        v.add(query4.getString(3));
                        v.add(query4.getString(4));
                        vect.add(v);
                        tm.fireTableStructureChanged();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
                //新增组件查看排队的情况
                ResultSet query5 = con.query("select count(*) number from queue,doctor,patient where doctor.did=queue.did and queue.pid=patient.pid and starttime is not null and endtime is null and dname=?", itemAt);
                while (true){
                    try {
                        if (!query5.next()) break;
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    try {
                        ceshi.setText(String.valueOf(query5.getInt("number")));
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
                jLabel.setVisible(true);
                jLabel1.setVisible(true);
                ceshi.setVisible(true);
            }
        });
        lineup.add(label);
        lineup.add(comboBox);
        lineup.add(label1);
        lineup.add(comboBox1);
        lineup.add(search);
        lineup.add(scrollPane);
        JLabel visitnumber = new JLabel("序号");
        JTextField visitnumbertext = new JTextField(10);
        JLabel keshi = new JLabel("科室");
        JTextField keshitext = new JTextField(10);
        JLabel xingming = new JLabel("姓名");
        JTextField xingmingtext = new JTextField(10);
        JLabel jiuzheng = new JLabel("就诊医生");
        JTextField jiuzhengtext = new JTextField(10);
        JButton seeAdoctor = new JButton("就诊");
        lineup.add(visitnumber);
        lineup.add(visitnumbertext);
        lineup.add(keshi);
        lineup.add(keshitext);
        lineup.add(xingming);
        lineup.add(xingmingtext);
        lineup.add(jiuzheng);
        lineup.add(jiuzhengtext);
        lineup.add(seeAdoctor);
        queueTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(e.getClickCount()==2){
                    //双击事件
                    int row = queueTable.getSelectedRow();
                    visitnumbertext.setText(String.valueOf(queueTable.getValueAt(row,0)));
                    keshitext.setText((String) queueTable.getValueAt(row,1));
                    xingmingtext.setText((String) queueTable.getValueAt(row,2));
                    jiuzhengtext.setText((String) queueTable.getValueAt(row,3));
                }
            }
        });

        //付款
        JPanel pay = new JPanel();
        final String[] paymsg={"序号","科室","姓名","就诊医生"};
        ResultSet query6 = con.query("select visitnumber,department,pname,dname from queue,doctor,patient where doctor.did=queue.did and queue.pid=patient.pid and starttime is not null and endtime is not null and paymoney is null order by department,dname");
        //表格信息
        final Vector<Vector<Comparable>> vect1 = new Vector();//初始化向量
        //表格模型
        AbstractTableModel tm1 = new AbstractTableModel() {// 实现AbstractTableModel的抽象方法
            private static final long serialVersionUID = 1L;

            public int getColumnCount() {
                return paymsg.length;
            }

            public int getRowCount() {
                // TODO 自动生成的方法存根
                return vect1.size();
            }

            public Object getValueAt(int row, int column) {
                // TODO 自动生成的方法存根
                if (!vect.isEmpty())
                    return (((Vector<?>) vect1.elementAt(row)).elementAt(column));
                else
                    return null;
            }

            public String getColumnName(int column) {
                return paymsg[column];// 设置表格列名
            }

            public void setValueAt(Object value, int row, int column) {
            }

            public Class<? extends Object> getColumnClass(int c) {
                return getValueAt(0, c).getClass();
            }// 取得所属对象类

            public boolean isCellEditable(int row, int column) {

                return false;
            }// 设置单元格不可编辑
        };
        //新建排队信息表格类
        JTable payTable = new JTable(tm1);
        payTable.setRowHeight(20);
        payTable.setPreferredScrollableViewportSize(new Dimension(560,200));
        payTable.setToolTipText("显示所有的排队信息");
        //排队信息表自动调整
        payTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        //设置单元格分割线
        payTable.setShowHorizontalLines(true);
        payTable.setShowVerticalLines(true);
        //滚轮,将滚轮绑定表格
        JScrollPane scrollPane1 = new JScrollPane(payTable);
        //将获取到的数据加进表格中
        vect1.removeAllElements();// 初始化向量对象
        tm1.fireTableStructureChanged();// 更新表格内容
        while (true) {
            try {
                if (!query6.next()) break;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            @SuppressWarnings("rawtypes")
            Vector<Comparable> v = new Vector<Comparable>();
            try {
                v.add(query6.getInt(1));
                v.add(query6.getString(2));
                v.add(query6.getString(3));
                v.add(query6.getString(4));
                vect1.add(v);
                tm1.fireTableStructureChanged();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        pay.add(scrollPane1);
        JLabel visitnumber1 = new JLabel("序号");
        JTextField visitnumbertext1 = new JTextField(10);
        JLabel keshi1 = new JLabel("科室");
        JTextField keshitext1 = new JTextField(10);
        JLabel xingming1 = new JLabel("姓名");
        JTextField xingmingtext1 = new JTextField(10);
        JLabel jiuzheng1 = new JLabel("就诊医生");
        JTextField jiuzhengtext1 = new JTextField(10);
        JLabel payLabel = new JLabel("付款金额:");
        JTextField paytext = new JTextField(10);
        JButton paymoney = new JButton("付款");
        pay.add(visitnumber1);
        pay.add(visitnumbertext1);
        pay.add(keshi1);
        pay.add(keshitext1);
        pay.add(xingming1);
        pay.add(xingmingtext1);
        pay.add(jiuzheng1);
        pay.add(jiuzhengtext1);
        pay.add(payLabel);
        pay.add(paytext);
        pay.add(paymoney);
        payTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(e.getClickCount()==2){
                    int row = payTable.getSelectedRow();
                    visitnumbertext1.setText(String.valueOf(payTable.getValueAt(row,0)));
                    keshitext1.setText(String.valueOf(payTable.getValueAt(row,1)));
                    xingmingtext1.setText(String.valueOf(payTable.getValueAt(row,2)));
                    jiuzhengtext1.setText(String.valueOf(payTable.getValueAt(row,3)));
                }
            }
        });
        JPanel tongji = new JPanel();
        final String[] tongjimsg={"科室","花费","总看病人数"};
        ResultSet query9 = con.query("select department,sum(paymoney),count(*) from queue,doctor where doctor.did=queue.did group by department");
        //表格信息
        final Vector<Vector<Comparable>> vect3 = new Vector();//初始化向量
        //表格模型
        AbstractTableModel tm3 = new AbstractTableModel() {// 实现AbstractTableModel的抽象方法
            private static final long serialVersionUID = 1L;

            public int getColumnCount() {
                return tongjimsg.length;
            }

            public int getRowCount() {
                // TODO 自动生成的方法存根
                return vect3.size();
            }

            public Object getValueAt(int row, int column) {
                // TODO 自动生成的方法存根
                if (!vect3.isEmpty())
                    return (((Vector<?>) vect3.elementAt(row)).elementAt(column));
                else
                    return null;
            }

            public String getColumnName(int column) {
                return tongjimsg[column];// 设置表格列名
            }

            public void setValueAt(Object value, int row, int column) {
            }

            public Class<? extends Object> getColumnClass(int c) {
                return getValueAt(0, c).getClass();
            }// 取得所属对象类

            public boolean isCellEditable(int row, int column) {

                return false;
            }// 设置单元格不可编辑
        };
        //新建排队信息表格类
        JTable tongjiTable = new JTable(tm3);
        tongjiTable.setRowHeight(20);
        tongjiTable.setPreferredScrollableViewportSize(new Dimension(560,200));
        tongjiTable.setToolTipText("显示所有的统计信息");
        //排队信息表自动调整
        tongjiTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        //设置单元格分割线
        tongjiTable.setShowHorizontalLines(true);
        tongjiTable.setShowVerticalLines(true);
        //滚轮,将滚轮绑定表格
        JScrollPane scrollPane3 = new JScrollPane(tongjiTable);
        //将获取到的数据加进表格中
        vect3.removeAllElements();// 初始化向量对象
        tm3.fireTableStructureChanged();// 更新表格内容
        while (true) {
            try {
                if (!query9.next()) break;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            @SuppressWarnings("rawtypes")
            Vector<Comparable> v = new Vector<Comparable>();
            try {
                v.add(query9.getString(1));
                v.add(query9.getDouble(2));
                v.add(query9.getInt(3));
                vect3.add(v);
                tm3.fireTableStructureChanged();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        tongji.add(scrollPane3);
        paymoney.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String visitnumber = visitnumbertext1.getText();
                double money=0.0;
                if(!paytext.getText().equals("")){
                    //输入金额不为空
                    money= Double.parseDouble(paytext.getText());
                    con.update("update queue set paymoney=? where visitnumber=?",visitnumber,money);
                    JOptionPane.showMessageDialog(null,"付款成功","成功",JOptionPane.INFORMATION_MESSAGE);
                    //更新表格3
                    ResultSet query8 = con.query("select visitnumber,department,pname,dname from queue,doctor,patient where doctor.did=queue.did and queue.pid=patient.pid and starttime is not null and endtime is not null and paymoney is null order by department,dname");
                    vect1.removeAllElements();// 初始化向量对象
                    tm1.fireTableStructureChanged();// 更新表格内容
                    while (true) {
                        try {
                            if (!query8.next()) break;
                        } catch (SQLException ec) {
                            ec.printStackTrace();
                        }
                        @SuppressWarnings("rawtypes")
                        Vector<Comparable> v = new Vector<Comparable>();
                        try {
                            v.add(query8.getInt(1));
                            v.add(query8.getString(2));
                            v.add(query8.getString(3));
                            v.add(query8.getString(4));
                            vect1.add(v);
                            tm1.fireTableStructureChanged();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                    //更新表格4
                    ResultSet query10 = con.query("select department,sum(paymoney),count(*) from queue,doctor where doctor.did=queue.did group by department");
                    vect3.removeAllElements();// 初始化向量对象
                    tm3.fireTableStructureChanged();// 更新表格内容
                    while (true) {
                        try {
                            if (!query10.next()) break;
                        } catch (SQLException ec) {
                            ec.printStackTrace();
                        }
                        @SuppressWarnings("rawtypes")
                        Vector<Comparable> v = new Vector<Comparable>();
                        try {
                            v.add(query10.getString(1));
                            v.add(query10.getDouble(2));
                            v.add(query10.getInt(3));
                            vect3.add(v);
                            tm3.fireTableStructureChanged();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                }else{
                    JOptionPane.showMessageDialog(null,"没有输入金额","错误",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        jTabbedPane.addTab("挂号",registered);
        jTabbedPane.addTab("排队",lineup);
        jTabbedPane.addTab("付款",pay);
        jTabbedPane.addTab("统计",tongji);
        frame.add(jTabbedPane);
        frame.setVisible(true);//可见
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //就诊按钮监听事件
        seeAdoctor.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int visitnumber = Integer.parseInt(visitnumbertext.getText());
                //获取当前时间写入endtime中
                Date date = new Date();//获得系统时间
                SimpleDateFormat sdf = new SimpleDateFormat( " yyyy-MM-dd HH:mm:ss " );
                String nowTime = sdf.format(date);
                con.update("update queue set endtime=? where visitnumber=?",visitnumber,nowTime);
                ResultSet query3 = con.query("select visitnumber,department,pname,dname from queue,doctor,patient where doctor.did=queue.did and queue.pid=patient.pid and starttime is not null and endtime is null order by department,dname");
                ResultSet query7 = con.query("select visitnumber,department,pname,dname from queue,doctor,patient where doctor.did=queue.did and queue.pid=patient.pid and starttime is not null and endtime is not null and paymoney is null order by department,dname");
                JOptionPane.showMessageDialog(null,"就诊成功","成功",JOptionPane.INFORMATION_MESSAGE);
                //刷新页面1
                vect.removeAllElements();// 初始化向量对象
                tm.fireTableStructureChanged();// 更新表格内容
                while (true) {
                    try {
                        if (!query3.next()) break;
                    } catch (SQLException ec) {
                        ec.printStackTrace();
                    }
                    @SuppressWarnings("rawtypes")
                    Vector<Comparable> v = new Vector<Comparable>();
                    try {
                        v.add(query3.getInt(1));
                        v.add(query3.getString(2));
                        v.add(query3.getString(3));
                        v.add(query3.getString(4));
                        vect.add(v);
                        tm.fireTableStructureChanged();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
                //刷新页面2
                vect1.removeAllElements();// 初始化向量对象
                tm1.fireTableStructureChanged();// 更新表格内容
                while (true) {
                    try {
                        if (!query7.next()) break;
                    } catch (SQLException ec) {
                        ec.printStackTrace();
                    }
                    @SuppressWarnings("rawtypes")
                    Vector<Comparable> v = new Vector<Comparable>();
                    try {
                        v.add(query7.getInt(1));
                        v.add(query7.getString(2));
                        v.add(query7.getString(3));
                        v.add(query7.getString(4));
                        vect1.add(v);
                        tm1.fireTableStructureChanged();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
                ResultSet query5 = con.query("select distinct department from queue,doctor where queue.did=doctor.did and starttime is not null and endtime is null");
                ResultSet query4 = con.query("select distinct dname from queue,doctor where queue.did=doctor.did and starttime is not null and endtime is null");
                while(true){
                    try {
                        if (!query5.next()||!query4.next()) break;
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                    try {
                        comboBox.removeAllItems();
                        comboBox.addItem(query5.getString("department"));
                        comboBox1.removeAllItems();
                        comboBox1.addItem(query4.getString("dname"));
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
//                //刷新标签人数
                jLabel.setVisible(false);
                jLabel1.setVisible(false);
                ceshi.setVisible(false);
//                String dname = jiuzhengtext.getText();
//                ResultSet query6 = con.query("select count(*) number from queue,doctor,patient where doctor.did=queue.did and queue.pid=patient.pid and starttime is not null and endtime is null and dname=?", dname);
//                while(true){
//                    try {
//                        if (!query6.next()) break;
//                    } catch (SQLException ex) {
//                        ex.printStackTrace();
//                    }
//                    try {
//                        ceshi.setText(String.valueOf(query6.getInt("number")));
//                    } catch (SQLException ex) {
//                        ex.printStackTrace();
//                    }
//                }
            }
        });
        //挂号
        regist.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String pid=pidtext.getText();
                String pname = pnametext.getText();
                String psex = psexbox.getItemAt(psexbox.getSelectedIndex());
                int page = 0;
                if(pagetext.getText().equals("")){
                    page=0;
                }else{
                    page= Integer.parseInt(pagetext.getText());
                }
                String ptel = pteltext.getText();
                String pathogen = pathogentext.getText();
                ResultSet query5 = con.query("select pid from patient");
                ArrayList<String> pids = new ArrayList<>();
                while (true){
                    try {
                        if (!query5.next()) break;
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    try {
                        pids.add(query5.getString("pid"));
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
                if(!pids.contains(pid)){
                    //添加唯一的病人信息
                    con.add("insert into patient(pid,pname,psex,page,ptel) values(?,?,?,?,?)",pid,pname,psex,page,ptel);
                }
                con.add("insert into medical_records(pid,pathogen) values(?,?)",pid,pathogen);
                JOptionPane.showMessageDialog(null,"就诊成功","成功",JOptionPane.INFORMATION_MESSAGE);
                //获取当前时间写入starttime中
                Date date = new Date();//获得系统时间
                SimpleDateFormat sdf = new SimpleDateFormat( " yyyy-MM-dd HH:mm:ss " );
                String nowTime = sdf.format(date);
                String didname = doctorbox.getItemAt(doctorbox.getSelectedIndex());
                ResultSet query3 = con.query("select did from doctor where dname=?", didname);
                int did=0;
                while(true){
                    try {
                        if (!query3.next()) break;
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    try {
                        did=query3.getInt("did");
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
                con.add("insert into queue(did,pid,starttime) values(?,?,?)",did,pid,nowTime);
                ResultSet query4 = con.query("select visitnumber,department,pname,dname from queue,doctor,patient where doctor.did=queue.did and queue.pid=patient.pid and starttime is not null and endtime is null");
                //刷新页面
                vect.removeAllElements();// 初始化向量对象
                tm.fireTableStructureChanged();// 更新表格内容
                while (true) {
                    try {
                        if (!query4.next()) break;
                    } catch (SQLException ec) {
                        ec.printStackTrace();
                    }
                    @SuppressWarnings("rawtypes")
                    Vector<Comparable> v = new Vector<Comparable>();
                    try {
                        v.add(query4.getInt(1));
                        v.add(query4.getString(2));
                        v.add(query4.getString(3));
                        v.add(query4.getString(4));
                        vect.add(v);
                        tm.fireTableStructureChanged();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        //清空填写数据
        clear.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pnametext.setText("");
                psexbox.setSelectedIndex(0);
                pagetext.setText("");
                pteltext.setText("");
                pathogentext.setText("");
                //热部署JLabel测试：System.out.println("测试");
            }
        });
    }

    public static void main(String[] args) {
        new Main();
    }
}
