package cn.lj.operate;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 这个类用来处理配置文件的sqlBean,将从配置
 *        文件中获取得到信息封装到一个SqlBean的实例中
 * @author as1
 *
 */

public class XmlLoadSql {
	private InputStream is = null;
	private Map<String,SqlBean> map = null;
	public XmlLoadSql(){}
	public XmlLoadSql(InputStream is){
		this.is = is;
		load();
	}

	public XmlLoadSql(String filename){
		this(ClassLoader.getSystemResourceAsStream(filename));
	}

	public Map<String, SqlBean> getMap() {
		return map;
	}

	/*
	 * 设计用来读取一个节点的
	private void loadone(){
		SAXReader reader = new SAXReader();
		map = new HashMap<String,SqlBean>();
		try {
			Document doc = reader.read(is);
			Element ele = (Element) doc.selectSingleNode("//sqlinfo/sql");
			//ele.elementText("sqlsentence")
			//显示为SELECT * FROM user WHERE id=? AND username=?
			String sqlName = ele.attributeValue("name");
			SqlBean sqlBean = new SqlBean();
			fullMap(ele,sqlBean);
			map.put(sqlName, sqlBean);
		} catch (DocumentException e) {
			System.out.println("文档加载失败");
			e.printStackTrace();
		}
	}
	*/
	private void load(){ 
		SAXReader reader = new SAXReader();
		map = new HashMap<String,SqlBean>();
		try {
			Document doc = reader.read(is);
			@SuppressWarnings("rawtypes")
			List nodeList = doc.selectNodes("//sqlinfo/sql");
			//ele.elementText("sqlsentence")
			//显示为SELECT * FROM user WHERE id=? AND username=?
		 for(int i = 0;i < nodeList.size();i++){
			 Element ele = (Element)nodeList.get(i);
			String sqlName = ele.attributeValue("name");
			SqlBean sqlBean = new SqlBean();
			fullMap(ele,sqlBean);
			map.put(sqlName, sqlBean);
		 }
		} catch (DocumentException e) {
			System.out.println("文档加载失败");
			e.printStackTrace();
		}
	}
	
	//将这个sqlbean进行装填,将这个sql语句对应的属性全部装填
	//@SuppressWarnings("rawtypes")
	private void fullMap(Element ele,SqlBean sqlBean){
		sqlBean.setSql(ele.elementText("sqlsentence"));
		if(ele.attributeValue("ResultType") != null){
			sqlBean.setClassName(ele.attributeValue("ResultType"));
		}
		/*Map<String,String> map = sqlBean.getMap();
		Iterator it = ele.elementIterator();
		while(it.hasNext()){
			Element elem = (Element)it.next();
			String name = elem.attributeValue("name");
			String value = elem.attributeValue("value");
			if(name != null ){
				map.put(name, value);
			}
		}*/
	}
}
