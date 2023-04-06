package util;

import java.sql.*;

public class JDBCCon {
    //数据库驱动，这里选择的是mysql的jdbc驱动
    public static final String DRIVER="com.mysql.cj.jdbc.Driver";
    //数据库url
    public static final String URL="jdbc:mysql://localhost:3306/patientsystem";
    //用户名
    public static final String USER="root";
    //密码
    public static final String PASSWORD="123456";
    //本项目采用spring mvc三层架构,因为是使用swing,故先将数据库的增删改查写为一个公共的，就不用每次都连接数据库驱动了
    //连接
    private Connection connection;
    //陈述语句
    private PreparedStatement preparedStatement;
    //返回的结果集
    private ResultSet resultSet;

    //初始化加载驱动
    public JDBCCon(){
        try {
            //加载驱动程序
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            //捕获类找不到的异常
            e.printStackTrace();
        }
    }

    //连接数据库
    public Connection getConnection(){
        try {
            //获取数据库连接
            connection= DriverManager.getConnection(URL,USER,PASSWORD);
        } catch (SQLException e) {
            //捕获数据库异常
            e.printStackTrace();
        }
        return connection;
    }

    //关闭数据库
    public void CloseDb(){
        if(resultSet!=null){
            //第一步,关闭结果集
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(preparedStatement!=null){
            //第二步,关闭语句
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(connection!=null){
            //第三步,关闭连接
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //增加,由于传入的数据和类型可能都不相同,故采用Object
    public boolean add(String sql,Object...pras){
        int result = 0;
        connection=getConnection();
        try {
            preparedStatement=connection.prepareStatement(sql);
            for(int i=0;i<pras.length;i++){
                preparedStatement.setObject(i+1,pras[i]);
            }
            result = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result != 0;
    }
    //删除
    public boolean delete(String sql,String id){
        int result=0;
        connection=getConnection();
        try {
            preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setString(1,id);
            result = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result != 0;
    }
    //修改
    public boolean update(String sql, Object id, Object... pras){
        //修改和增加相似
        int result=0;
        connection=getConnection();
        try {
            preparedStatement=connection.prepareStatement(sql);
            for(int i=0;i<pras.length;i++){
                preparedStatement.setObject(i+1,pras[i]);
            }
            preparedStatement.setString(pras.length+1, String.valueOf(id));
            result = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result != 0;
    }
    //查询,返回的参数数量不定
    public ResultSet query(String sql,Object...pras){
        connection=getConnection();
        try {
            //sql语句连接数据库
            preparedStatement=connection.prepareStatement(sql);
            if(pras!=null){
                for(int i=0;i<pras.length;i++){
                    //将所有需要返回的数据返回
                    preparedStatement.setObject(i+1,pras[i]);
                }
            }
            //不指定是返回全部的结果
            resultSet=preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }
}

