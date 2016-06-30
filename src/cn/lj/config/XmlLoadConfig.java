package cn.lj.config;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
/**
 * 这个类处理xml加载配置文件
 * @author as1
 *
 */
public class XmlLoadConfig implements LoadConfig {
	private InputStream is = null;
	private Connection conn = null;
	
	public XmlLoadConfig(){}
    
	public XmlLoadConfig(InputStream is){
    	   this.is = is;
       }
	public XmlLoadConfig(String filename){
		is = ClassLoader.getSystemResourceAsStream(filename);
	}
	
	public Connection readConfig(){
		if(is == null){
			throw new RuntimeException("当前没有获取到配置文件的信息,需要给出配置文件");
		}
		SAXReader reader = new SAXReader();
		try {
			Document doc = reader.read(is);
			Node node = doc.selectSingleNode("//config/driver");
			String value = node.getStringValue();
			String sqlName = doc.selectSingleNode("//config/sqlname").getStringValue();
			String username = doc.selectSingleNode("//config/username").getStringValue();
			String password = doc.selectSingleNode("//config/password").getStringValue();
		   conn = loadSqlInfo(value ,sqlName,username,password);
		   return conn;
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 连接数据库,并且处理相关信息
	 * @param driver
	 * @param sqlName
	 * @param username
	 * @param password
	 * @return
	 */
	private Connection loadSqlInfo(String driver , String sqlName,String username,String password){
		try {
			Class.forName(driver);
		  conn = DriverManager.getConnection(sqlName, username, password);
		  return conn;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("驱动加载失败");
			 e.printStackTrace();
		}
		return null;
	}
	
}