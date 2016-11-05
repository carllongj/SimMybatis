package cn.lj.domain;

import java.util.Date;

import cn.lj.operate.OperateSql;
import cn.lj.operate.OperateSqlConn;

public class Test {

	public static void main(String[] args) throws Exception {
		OperateSql os = new OperateSqlConn("Config.xml", "Sql.xml");
 //测试单个查询
//		User user = (User) os.selectOne("selectUser");
//		System.out.println(user);
//		
		Long time = new Date().getTime();
		User user = (User)os.selectOne("selectUserByName","诸葛亮");
		System.out.println(user);
		Long time1 = new Date().getTime();
		System.out.println(time1-time);
	}
}