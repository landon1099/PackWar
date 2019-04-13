package utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesUtil {
	
	private final String PATH = "/config.properties";

	/**
	 * 获取配置文件全部内容
	 * @param path
	 * @return
	 */
	public Map<String, String> getPropertiesMap(String path) {
		Map<String, String> map = null;
		InputStream in = null;
		Properties pros = new Properties();
		try {
			if (path != null) {
				map = new HashMap<String, String>();
				in = PropertiesUtil.class.getResourceAsStream(PATH);
				pros.load(in);
				Enumeration<?> en = pros.propertyNames();
				while (en.hasMoreElements()) {
					String key = (String) en.nextElement();
					map.put(key, pros.getProperty(key));
//					System.out.println("key=" + key + " value=" + pros.getProperty(key));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("--------读取资源文件出错--------");
		} finally {
			try {
				if (null != in) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("---------关闭流失败---------");
			}
		}
		return map;
	}
	
	public String getValue(String key){
		String value = "";
		InputStream in = null;
		Properties pros = new Properties();
		try {
			if (null != key) {
				in = PropertiesUtil.class.getResourceAsStream(PATH);
				pros.load(in);
				value = pros.getProperty(key);
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("---------读取资源文件出错-----------");
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("----------关闭流失败-----------");
			}
		}
		return value;
	}
	
	public String setValue(String key, String value){
		InputStream in = null;
		FileOutputStream out = null;
		Properties pros = new Properties();
		try {
			if (null != key) {
				in = PropertiesUtil.class.getResourceAsStream(PATH);
				pros.load(in);
				out = new FileOutputStream(this.getClass().getResource(PATH).getPath());
				pros.setProperty(key, value);
				pros.store(out, "Update '" + key + "' value");
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("---------读取资源文件出错-----------");
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("----------关闭流失败-----------");
			}
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("----------关闭流失败-----------");
			}
		}
		return value;
	}
}
