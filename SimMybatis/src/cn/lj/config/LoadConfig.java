package cn.lj.config;

import java.sql.Connection;
/**
 * 
 * @author carl
 *  定义加载sql配置文件
 */
public interface LoadConfig {
	//定义加载配置文件
          Connection readConfig();
}
