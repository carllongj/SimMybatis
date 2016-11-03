package cn.lj.operate;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 处理跟配置文件相关的类的信息
 * @author as1
 */
public class ReflectClass {
	@SuppressWarnings("rawtypes")
	private Class clazz;
	private List<String> fieldNameList;
	private Map<String,Method> methodMap;
	private Map<String,Method> map;

	public ReflectClass(String className){
		LoadInfo(className);
	}
	@SuppressWarnings("rawtypes")
	public Class getClazz() {
		return clazz;
	}
	public List<String> getList() {
		return fieldNameList;
	}

	/**
	 * 获取得到的SqlBean中的class类名称的信息
	 * 获取和封装类的set方法 类型名称 属性名称
	 * @param className
	 */
	private void LoadInfo(String className){
		try {
			clazz = Class.forName(className);
			Field[] fields = clazz.getDeclaredFields();
			fieldNameList = new ArrayList<String>();
			for(Field f : fields){
				f.setAccessible(true);
				fieldNameList.add(f.getName());
			}
			Method[] methods = clazz.getDeclaredMethods();
			methodMap = new HashMap<String,Method>();
			for(Method m : methods){
				if(m.getName().startsWith("set")){
					methodMap.put(m.getName().substring(3), m);
				}
			}
		} catch (ClassNotFoundException e) {
			System.out.println("没有找到对应的类");
			e.printStackTrace();
		}
	}

	/**
	 * 创建这个类的实例,并且将从数据库中获取的数据映射到这个实例中
	 * @param rs
	 * @return
	 */
	public Object newInstance(ResultSet rs){
		Object t = null;
		try {
			/*
			 * 反射获取的方法是随机的保存到list中，不确定!
			while(rs.next()){
			t = clazz.newInstance();
		for(int i = 1;i <= rs.getMetaData().getColumnCount();i++){
			for(int j = 0;j < methodList.size();j++){
			if(methodList.get(j).getParameterTypes()[0].getName().contains("String")){
					methodList.get(j).invoke(t, rs.getString(i));
			}else if(methodList.get(j).getParameterTypes()[0].getName().contains("int")||
					methodList.get(j).getParameterTypes()[0].getName().contains("Integer")){
				methodList.get(j).invoke(t, rs.getInt(i));
			}else if(methodList.get(j).getParameterTypes()[0].getName().contains("Date")){
				methodList.get(j).invoke(t, rs.getDate(i));
			}
			}
		}
		}
			 */
			while(rs.next()){
				t = clazz.newInstance();
				List<String> list = new ArrayList<String>();
				for(int i = 1;i <= rs.getMetaData().getColumnCount();i++){
					list.add(exchange(rs.getMetaData().getColumnName(i)));
				}
//				for(int i = 0;i < list.size();i++){
//					for(int j = 0;j < methodList.size();j++){
//						if(methodList.get(j).getName().contains(exchange(list.get(i)))){
//														if(methodList.get(j).getParameterTypes()[0].getName().contains("String")){
//															methodList.get(j).invoke(t,  rs.getString(list.get(i)));
//														}else if(methodList.get(j).getParameterTypes()[0].getName().contains("int")||
//																methodList.get(j).getParameterTypes()[0].getName().contains("Integer")){
//															methodList.get(j).invoke(t, rs.getInt(list.get(i)));
//														}else if(methodList.get(j).getParameterTypes()[0].getName().contains("Date")){
//															methodList.get(j).invoke(t, rs.getDate(list.get(i)));
//														}else if(methodList.get(j).getParameterTypes()[0].getName().contains("double")||
//																methodList.get(j).getParameterTypes()[0].getName().contains("Double")){
//															methodList.get(j).invoke(t, rs.getDouble(list.get(i)));
//														}else if(methodList.get(j).getParameterTypes()[0].getName().contains("boolean")||
//																methodList.get(j).getParameterTypes()[0].getName().contains("Boolean")){
//							                                methodList.get(j).invoke(t, rs.getBoolean(list.get(i)));
//														}
//
//                       
//						}
//					}
//				}
				
				/**
				 * methodMap是列名对应的方法名称
				 * map是参数类型名称对应方法名称,ResultSet的方法
				 * list是列名
				 */
				HandleRs(rs);
				for(int i = 0;i < list.size();i++){
					String Type = (methodMap.get(list.get(i)).getParameterTypes())[0].getSimpleName();
					Type = exchange(Type);
					methodMap.get(list.get(i)).invoke(t,map.get(Type).invoke(rs, list.get(i)));
				}
				return t;
			}
		} catch (IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	private String exchange(String str){
		return (str.charAt(0)+"").toUpperCase() + str.substring(1);
	}

	private void HandleRs(ResultSet rs){
		@SuppressWarnings("rawtypes")
		Class rsClazz = rs.getClass();
		Method[] Rsmethods = rsClazz.getMethods();
		map = new HashMap<String,Method>();
		for(Method m : Rsmethods){
			if(m.getName().startsWith("get") && m.getParameterTypes().length == 1){
				if((m.getParameterTypes()[0]) == String.class){
					map.put(m.getName().substring(3), m);
				}
			}
		}
	}
}
