package cn.lj.operate;

import java.util.HashMap;
import java.util.Map;
/**
 * 这个类用来保存从配置文件中获取的sql信息
 * @author as1
 *
 */
public class SqlBean {
	private String sql;
	private Map<String,String> map;
	private String ClassName;
	
	public String getClassName() {
		return ClassName;
	}
	public void setClassName(String className) {
		ClassName = className;
	}
	public SqlBean() {
		super();
		map = new HashMap<String,String>();
	}
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public Map<String, String> getMap() {
		return map;
	}
	public void setMap(Map<String, String> map) {
		this.map = map;
	}
}
