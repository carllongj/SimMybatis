package cn.lj.operate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cn.lj.config.LoadConfig;
import cn.lj.config.XmlLoadConfig;

public class OperateSqlConn implements OperateSql{
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	private XmlLoadSql ls = null;

	/**
	 * 传入为一个名称,那么将这个名称解析为一个sqlconfig配置文件的名称进行加载
	 * @param configName
	 * @param sqlName
	 */
	public OperateSqlConn(String configName,String sqlName){
		this(new XmlLoadConfig(configName),sqlName);
	}

	//创建一个对象并且获取连接
	public OperateSqlConn(LoadConfig lc,String sqlName){
		this(lc.readConfig(),sqlName);
	}

	public OperateSqlConn(Connection conn,String sqlStenceName){
		this.conn = conn;
		ls = new XmlLoadSql(sqlStenceName); //加载sql语句并进行初始化
	}
	
	/**
	 * 查询一个对象的实例,并且实例只能是一个
	 */
	public Object selectOne(String sqlName,Object obj){
		SqlBean sqlBean = ls.getMap().get(sqlName);
		   handleResult(sqlName,obj);
		   try {
			rs = pstmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
			Object instance = null;
			if(rs != null){
				ReflectClass rc = new ReflectClass(sqlBean.getClassName());
				instance = rc.newInstance(rs);
			}
			return instance;
	}
	

	@Override
	public void delete(String sqlName,Object obj) {
		handleResult(sqlName,obj);
		try {
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	//	public List<Object> selectList(String sqlName){
//		
//	}
 
	/**
	 * 处理查询语句,设置参数,只支持一个基本的数据类型和String
	 * @param sqlName
	 * @return
	 */
	private void handleResult(String sqlName,Object obj){
		try {
			SqlBean sqlBean = ls.getMap().get(sqlName);
			pstmt = conn.prepareStatement(sqlBean.getSql());
			/*Map<String,String> map = sqlBean.getMap();
			List<String> list = StringSplit(sqlBean.getSql());
			for(int i = 0;i < list.size();i++){
				pstmt.setString(i + 1 , map.get(list.get(i)));
			}*/
			pstmt.setObject(1, obj);
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 将sql进行切割,将获取到的值放到list中
	 * @author as1
	 * @param sql
	 * @return
	 */
	private List<String> StringSplit(String sql){
		String[] str = sql.split("[wW][hH][eE][rR][eE]\\s");
		String[] str1 = str[1].split("\\s[aA][nN][dD]\\s");
		List<String> list = new ArrayList<String>();
		for(String s : str1){
			String[] str2 = s.split("=");
			list.add(str2[0]);
		}
		return list;
	}

	@Override
	public void add(String sqlName, Object obj) {
		
	}

}
