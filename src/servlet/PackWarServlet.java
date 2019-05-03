package servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNDiffClient;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNStatus;
import org.tmatesoft.svn.core.wc.SVNStatusClient;
import org.tmatesoft.svn.core.wc.SVNStatusType;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import service.ApplyService;
import service.ApplyServiceImpl;
import service.SvnService;
import service.SvnServiceImpl;
import utils.PropertiesUtil;
import bean.ApplyBean;
import bean.Attributes;
import bean.EUITreeBean;
import bean.PackBean;
import bean.SvnBean;
import bean.SvnLogBean;
import bean.SvnLoginBean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.xpath.internal.operations.Bool;

public class PackWarServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static int id = 1;
	private static SVNClientManager ourClientManager;

	/**
	 * 打包
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void packWar(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		double startTime = System.currentTimeMillis();
		
		String webRootName = request.getParameter("webRootName");
		String pack_addr = request.getParameter("pack_addr");
		String projectUrl = request.getParameter("projectUrl").replaceAll("\\\\", "/").replaceAll("//", "/");
		String projectName = request.getParameter("projectName");
		String file_name = request.getParameter("file_name");
		String checkedUrl = request.getParameter("checkedUrl").replaceAll("\\\\", "/").replaceAll("//", "/");
		String[] urls = checkedUrl.split(",");
		
		PackBean packBean = new PackBean();
		packBean.setWebRootName(webRootName);
		if (pack_addr.endsWith("/") || pack_addr.endsWith("\\\\")) {
			pack_addr = pack_addr.substring(0, pack_addr.length()-1);
		}
		packBean.setPack_addr(pack_addr);
		packBean.setProjectUrl(projectUrl);
		packBean.setProjectName(projectName);
		
		//设置生成文件名
		String fileStr = "";
		if (StringUtils.isNotBlank(file_name)) {
			fileStr = file_name;
			String tmpPath = pack_addr + "/" + fileStr;
			String flagString = "";
			while (new File(tmpPath).exists()) {
				String num_str = fileStr.substring(fileStr.length() - 1);
				int num = Integer.parseInt(num_str) + 1;
				fileStr = fileStr.substring(0, fileStr.length() - 1) + num;
				tmpPath = pack_addr + "/" + fileStr;
				flagString = "[@]1";
			}
			fileStr += flagString;
			if (flagString != "") {
				tmpPath += "[@]1";
				while (new File(tmpPath).exists()) {
					String num_str = fileStr.substring(fileStr.length() - 1);
					int num = Integer.parseInt(num_str) + 1;
					fileStr = fileStr.substring(0, fileStr.length() - 1) + num;
					tmpPath = pack_addr + "/" + fileStr;
				}
			}
		} else {
			SimpleDateFormat f = new SimpleDateFormat("-yyyyMMdd-HHmmss");
			Date date = new Date();
			fileStr = webRootName + f.format(date);
		}
		packBean.setFileStr(fileStr);
		
		//生成war文件
		String msg = "以下文件在webRoot目录中不存在，请检查webroot目录(或classes目录)：";
		String msgOld = "以下文件在webRoot目录中不存在，请检查webroot目录(或classes目录)：";
		for(int i=0; i<urls.length; i++){
			if (StringUtils.isNotBlank(urls[i])) {
				File file = new File(urls[i]);
				if (file.isFile()) {
					//打包
					String temp = generateWar(urls[i], packBean);
					if (!"".equals(temp)) {
						msg += "<div>"+temp+"</div>";
					}
				} else if (file.isDirectory()) {
					//不处理文件目录
					/*if (!file.exists()) {
						file.mkdir();
					}*/
				}
			}
		}
		JSONObject jsonObject = new JSONObject();
	    jsonObject.put("warPath", pack_addr+"/"+fileStr);
	    jsonObject.put("fileStr", fileStr);
	    if (!msg.equals(msgOld)) {
	    	jsonObject.put("msg", msg);
	    	System.out.println("==errorin packWar---"+msg);
		}
	    response.setContentType("text/html;charset=UTF-8");
	    response.getWriter().write(jsonObject.toString());
	    
	    double endTime = System.currentTimeMillis();
	    double tmpTime = (endTime - startTime)/1000;
	    tmpTime = (double)(Math.round(tmpTime*100))/100;
	    System.out.println("------打包用时：【" + tmpTime + "s】------");
	}
	
	/**
	 * 修改文件名称
	 */
	public void modifyName(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String file_addr = request.getParameter("file_addr");
		String new_file_nm = request.getParameter("new_file_nm");
		file_addr = file_addr.replaceAll("\\\\", "/").replaceAll("//", "/");
		File file = new File(file_addr);
		if(file.exists()){
			String parent = file.getParent().replaceAll("\\\\", "/");
			file_addr = parent + "/" + new_file_nm;
			File tmpFile = new File(file_addr);
			if (tmpFile.exists()) {
				System.out.println("==errorin modifyName---文件存在，无法修改名称！" + file_addr);
				return;
			}
			boolean renameTo = file.renameTo(new File(file_addr));
			if (!renameTo) {
				System.out.println("==errorin modifyName---修改失败！：" + file_addr);
				return;
			}
		}
		
		JSONObject json = new JSONObject();
		json.put("file_addr", file_addr);
		json.put("msg", "");
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().write(json.toJSONString());
	}
	
	/**
	 * 获取项目所有文件列表
	 * @param request
	 * @param response
	 * @throws SVNException 
	 */
	public void getProFiles(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SVNException {
		double startTime = System.currentTimeMillis();
		String projectUrl = request.getParameter("proUrl");
		String isCheckSvn = request.getParameter("isCheckSvn");
		
		if (!new File(projectUrl).exists()) {
			System.out.println("==errorin getProFiles---"+"地址不存在："+projectUrl);
			return;
		}
		//获取路径下项目所有文件列表
		List<String> fileList = new ArrayList<String>();
		fileList = getUrls(projectUrl, 1, fileList);
		//获取webRootName
		String webRootName = null;
		for (String url : fileList) {
			if (url.contains("WEB-INF")) {
				String[] webinfUris = url.split("WEB-INF");
				String[] webinfUri = webinfUris[0].split("/");
				webRootName = webinfUri[webinfUri.length-1];
				break;
			}
		}
		
		//获取编译版本号
		double version = 0.0;
		version = getVersion(fileList);
		
		//校验本地SVN
		List<String> changedList = new ArrayList<>();
		Map<String, String> doDiffMap = new HashMap<>();
		if ("true".equals(isCheckSvn)) {
			
			checkSvn(changedList, projectUrl);
			for (String path : changedList) {
				doDiffMap.put(path, "");
			}
			
			//存储项目目录下所有变化文件的上级目录
			List<String> tempList = new ArrayList<String>();
			for (String path : changedList) {
				int length = path.split("/").length;
				String[] tmpStrs = path.split("/");
				for (int i = tmpStrs.length-1; i>length; i--) {
					String temp = path.split("/")[path.split("/").length-1];
					path = path.replaceAll("/"+temp, "");
					if (!tempList.contains(path)) {
						tempList.add(path);
					}
				}
			}
			changedList.addAll(tempList);
		}
		String jsonData = getJSONString(projectUrl, changedList, version);
		Map<String, String> diffMap = getVsionFiles(doDiffMap);
		
		//计算运行用时
		double endTime = System.currentTimeMillis();
		double tmpTime = (endTime - startTime)/1000;
		tmpTime = (double)(Math.round(tmpTime*100))/100;
		System.out.println("------获取目录用时：【" + tmpTime + "s】------");
		
		JSONObject json = new JSONObject();
	    json.put("jsonData", jsonData);
	    json.put("version", version);
	    json.put("webRootName", webRootName);
	    json.put("diffMap", diffMap);
	    response.setContentType("text/html;charset=UTF-8");
	    response.getWriter().write(json.toString());
	    
	}
	
	/**
	 * 校验本地SVN
	 * @param changedList
	 * @param projectUrl
	 * @return
	 * @throws IOException
	 * @throws SVNException 
	 */
	public static List<String> checkSvn(List<String> changedList, String projectUrl)
			throws SVNException, IOException {
		List<String> tmpList = new ArrayList<>();
		File dirFile = new File(projectUrl);
		String[] list = dirFile.list();
//		for(String fileName : list) {
//			if (".svn".equals(fileName)) {
				tmpList = getUrls(projectUrl, 1, tmpList);
				for(String url : tmpList){
					url = url.replaceAll("\\\\", "/");
					//System.out.println("------对比本地文件：" + url);
					if (!url.contains("classes")) {
						//System.out.println(url);
						//过滤.开头目录
						String tmpName = url.split("/")[url.split("/").length - 1];
						String temp = tmpName.split("\\.")[0].trim();
						if (!"".equals(temp)) {
							if (isFileChanged(url)) {
								changedList.add(url);
							}
						}
					}
				}
//				break ;
//			}
//		}
		return changedList;
	}
	
	public static double getVersion(List<String> fileList) {
		double version = 0.0;
		for (String url : fileList) {
			if (url.endsWith(".class")) {
				try{
					FileInputStream fis = new FileInputStream(url);
					int length = fis.available();
					byte[] data = new byte[length];
					fis.read(data);
					fis.close();
					//解析文件数据
					int major_version = (((int)data[6]) << 8) + data[7];
//					System.out.println("主版本号(version):"+major_version+"----------");
					switch (major_version){
					case 51:
						version = 1.7;
						break;
					case 50:
						version = 1.6;
						break;
					case 49:
						version = 1.5;
						break;
					case 48:
						version = 1.4;
						break;
					case 52:
						version = 1.8;
						break;
					case 53:
						version = 1.9;
						break;
					default :
						version = 0.0;
						break;
					}
					
				}catch(Exception e){
					System.out.println(e);
				}
				return version;
			}
		}
		return version;
	}

	/**
	 * 设置json根目录、version
	 * @param path
	 * @param localList
	 * @param version
	 * @return
	 */
	public static String getJSONString(String path, List<String> localList, double version) {
		
		//设置根目录
		EUITreeBean bean = new EUITreeBean();
		bean.setId(id++);
		bean.setState("open");
		bean.setChecked(false);
		Attributes attributes = new Attributes();
		attributes.setUrl(path);
		bean.setAttributes(attributes);
		String textStr = path.split("/")[path.split("/").length-1];
		String versionStr = "";
		if (version!=0.0) {
			versionStr = "<span class='span'>____JDK: "+version+"</span>";
		}
		if (!"".equals(textStr)) {
			bean.setText(textStr+versionStr);
		}
		//获取所有children json
		ArrayList<EUITreeBean> children = getJSONUrl(path, 1, localList);
		bean.setChildren(children);
		String json = "["+JSONObject.toJSONString(bean)+"]";
		
		return json;
	}

	/**
	 * 获取项目目录
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @return json/attribute
	 */
	public void getProDirs(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String local_addr = request.getParameter("local_addr");
		String pack_addr = request.getParameter("pack_addr");
		String svn_addr = request.getParameter("svn_addr");
		String svnDirs_addr = request.getParameter("svnDirs_addr");
		//svn地址判断
		if (StringUtils.isNotBlank(svnDirs_addr)) {
			String name = request.getParameter("name");
			String password = request.getParameter("password");
			List<String> svnDirs = getSvnDirs(name, password, svnDirs_addr);
			JSONObject jsonObject = new JSONObject();
		    jsonObject.put("svnDirs_addr_list", svnDirs);
		    jsonObject.put("flag", "1");
		    response.setContentType("text/html;charset=UTF-8");
		    response.getWriter().write(jsonObject.toString());
		    return;
		}
		String flag = request.getParameter("flag");
		Map<String, String> addr_map = new HashMap<>();
		Map<String, String> tmp_map = new HashMap<>();
		tmp_map.put("local_addr", local_addr);
		tmp_map.put("pack_addr", pack_addr);
		tmp_map.put("svn_addr", svn_addr);
		//flag为1表示从前端请求：获取本地项目目录、生成目录的路径
		if ("1".equals(flag)) {
			for (Entry<String, String> entry : tmp_map.entrySet()) {
				String key = entry.getKey();
				String path = entry.getValue();
				if (StringUtils.isNotBlank(path)) {
					path = path.replaceAll("\\\\", "/").replaceAll("//", "/");
					if (path.length() == 2) {
						path += "/";
					}
					//路径不存在则返回【前台控制每次只传入一个地址】
					File localFile = new File(path);
					if (!localFile.exists()) {
						flag = "0";
						JSONObject json = new JSONObject();
					    json.put("flag", flag);
					    response.setContentType("text/html;charset=UTF-8");
					    response.getWriter().write(json.toJSONString());
						return;
					}
					addr_map.put(key, path);
					System.out.println("------传入："+key+"--传入地址："+path);
				}
			}
		} else {
			//初始化
			addr_map = tmp_map;
		}
		//读取、存储路径配置文件
		try {
			JSONObject json = new JSONObject();
			PropertiesUtil propertiesUtil = new PropertiesUtil();
			for (Entry<String, String> entry : addr_map.entrySet()) {
				String key = entry.getKey();
				String path = entry.getValue();
				if (StringUtils.isNotBlank(path)) {
					propertiesUtil.setValue(key, path);
				} else {
					path = propertiesUtil.getValue(key);
					if (StringUtils.isBlank(path)) {
						path = "c:/";
					}
				}
				List<String> addrList = new ArrayList<String>();
				if (!"svn_addr".equals(key)) {
					addrList = getUrls(path, 0, addrList);
				}
				if ("1".equals(flag)) {
					json.put(key + "_list", addrList);
				} else {
					request.setAttribute(key + "_list", addrList);
					//获取SVN路径
					List<String> svn_addr_list = new ArrayList<String>();
					for (int i = 1; i <= 60; i++) {
						String value = propertiesUtil.getValue("svn_addr_" + i);
						if (StringUtils.isNotBlank(value)) {
							svn_addr_list.add(value);
						}
					}
					request.setAttribute(key, path);
					request.setAttribute("svn_addr_list", svn_addr_list);
				}
			}
			if ("1".equals(flag)) {
				response.setContentType("text/html;charset=UTF-8");
			    response.getWriter().write(json.toJSONString());
				return;
			} else {
				String name = propertiesUtil.getValue("name");
				String password = propertiesUtil.getValue("password");
				if (StringUtils.isNotBlank(name) && StringUtils.isNotBlank(password)) {
					request.setAttribute("name", name);
					request.setAttribute("password", password);
					request.setAttribute("account_flag", "true");
				}
				this.forward(request, response, "pages/packwar");
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("==errorin getProDirs---:[读取/写入、判断/新建目录地址]---------");
		}
	}

	/**
	 * 生成war包文件
	 * @param path
	 */
	public static String generateWar(String path, PackBean packBean) {
		String backupPath = path;
		String projectName = packBean.getProjectName();
		String webRootName = packBean.getWebRootName();
		// e:/projects/TestProject/
		String projectUrl = packBean.getProjectUrl();
		// e:/projects/B_Pack_To
		String pack_addr = packBean.getPack_addr();
		String fileStr = packBean.getFileStr();
		//提前处理.java后缀
		if (path.endsWith(".java")) {
			path = path.replace(".java", ".class");
		}
		String generate_path = "";
		//处理资源目录（不包含webRootName的目录）
		if (!path.contains(projectName+"/"+webRootName)) {
			String temp_uri = path.replace(projectUrl, ""); //  src/com/itheima/dao/UserDao.java
			String path_head = projectUrl + webRootName + "/WEB-INF/classes";
			String[] uriStrings = temp_uri.split("/");
			for (int count = 1; count< uriStrings.length; count++) {
				String path_foot = "";
				for (int i = count; i < uriStrings.length; i++) {
					path_foot = path_foot + "/" + uriStrings[i];
				}
				path = path_head + path_foot;
				//System.out.println(path);
				if (new File(path).exists()) {
					break;
				}
			}
		}
		String oldStr = projectUrl;
		String newStr = pack_addr + "/" + fileStr + "/";
		generate_path = path.replace(oldStr, newStr);
		if (new File(path).exists()) {
			copyFile(path, generate_path);
		} else {
			return backupPath;
		}
		
		//检查$path
		if (path.endsWith(".class")) {
			String fileName = path.split("/")[path.split("/").length-1].replace(".class", "");
			String upperUrl = (new File(path)).getParent().replaceAll("\\\\", "/");
			List<String> tmpList = new ArrayList<String>();
			tmpList = getUrls(upperUrl, 0, tmpList);
			for (String string : tmpList) {
				if (string.contains(fileName+"$")) {
					path = upperUrl+"/"+string;
					String tmp_generate_path =  generate_path.replace(fileName+".class", string);
					copyFile(path, tmp_generate_path);
				}
			}
		}
		return "";
	}
	
	/**
	 * 复制文件
	 * @param path
	 * @param generate_path
	 */
	public static void copyFile(String path, String generate_path) {
		FileInputStream input = null;
		FileOutputStream output = null;
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			File newWarFile = new File(generate_path);
			if (!newWarFile.getParentFile().exists()) {
				boolean result = newWarFile.getParentFile().mkdirs();
				if (!result) {
					System.out.println("==errorin copyFile---创建文件夹失败：" + generate_path);
				}
			}
			input = new FileInputStream(path);
			output = new FileOutputStream(generate_path);
			bis = new BufferedInputStream(input);
			bos = new BufferedOutputStream(output);
			int ch = 0;
			while ((ch = bis.read()) != -1) {
				bos.write(ch);
			}
			System.out.println("------生成war包文件:"+generate_path);
		} catch (Exception e) {
			System.out.println("==errorin copyFile---生成war包文件出错：" + generate_path);
			e.printStackTrace();
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					System.out.println("关流失败");
					e.printStackTrace();
				}
			}
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					System.out.println("关流失败");
					e.printStackTrace();
				}
			}
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					System.out.println("关流失败");
					e.printStackTrace();
				}
			}
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					System.out.println("关流失败");
					e.printStackTrace();
				}
			}
		}
	}
	
	
	/**
	 * 比对svn文件差异
	 * @param local_path
	 * @param svn_path
	 * @param sameList
	 * @throws IOException 
	 */
	public static void compareFiles(String local_path, String svn_path, List<String> sameList) {
		//文件不存在，直接返回
		if (!(new File(svn_path).exists())) {
			return;
		}
		//文件大小为0时，单独处理
		if (new File(local_path).length() == 0) {
			if (new File(svn_path).length() == 0) {
				sameList.add(local_path);
			}
			return;
		}
		FileInputStream fis_war = null;
		FileInputStream fis_svn = null;
		BufferedInputStream bis_war = null;
		BufferedInputStream bis_svn = null;
		try {
			fis_war = new FileInputStream(local_path);
			fis_svn = new FileInputStream(svn_path);
			bis_war = new BufferedInputStream(fis_war);
			bis_svn = new BufferedInputStream(fis_svn);
			try {
				int c;
				//这里容易漏掉第一行为空的情况：c = -1
				while ((c = bis_war.read()) != -1) {
					if (bis_svn.read() != c)
						return;
				}
				sameList.add(local_path);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (bis_war != null) {
				try {
					bis_war.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (bis_svn != null) {
				try {
					bis_svn.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fis_war != null) {
				try {
					fis_war.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fis_svn != null) {
				try {
					fis_svn.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 同一目录下的路径地址排序
	 * @param files
	 * @return
	 */
	public static ArrayList<String> sortUrl(File[] files){
		for(int i=0;i<files.length;i++){
			for(int j=i+1;j<files.length;j++){
				if((files[i].compareTo(files[j]))>0){
					File temp;
					temp=files[i];
					files[i]=files[j];
					files[j]=temp;
				}
			}
		}
		ArrayList<String> fileList = new ArrayList<String>();
		for(int i=0;i<files.length;i++){
			if (files[i].isDirectory()) {
				fileList.add(files[i].toString().replaceAll("\\\\", "/"));
			}
		}
		for(int i=0;i<files.length;i++){
			if (files[i].isFile()) {
				fileList.add(files[i].toString().replaceAll("\\\\", "/"));
			}
		}
		return fileList;
	}
	
	/**
	 * 获取目录下的文件路径
	 * 
	 * type=0遍历当前目录
	 * type=1遍历所有文件
	 * @param path
	 * @param type
	 */
	public static List<String> getUrls(String path, int type, List<String> list) {
		if (!new File(path).exists()) {
			System.out.println("==errorin getUrls---目录不存在"+path);
			return null;
		}
		File file = new File(path);
		if (file.isFile()) {
			System.out.println("==errorin getUrls---输入的路径为文件，请选择目录！");
			return null;
		}

		File[] fileLists = file.listFiles();
		if (type == 0) {
			for (int i = 0; i < fileLists.length; i++) { 
				String temp = fileLists[i].getAbsolutePath().replaceAll("\\\\", "/");
				temp = temp.split("/")[temp.split("/").length-1];
				list.add(temp);
			}
			return list;
		}
		if (type == 1) {
			for (int i = 0; i < fileLists.length; i++) {
				if (fileLists[i].isDirectory()) {
					// 过滤.开头目录
					String fileName = fileLists[i].getName();
					String temp = fileName.split("\\.")[0].trim();
					// 过滤 idea 项目 out/artifacts 目录
					path = path.replaceAll("\\\\", "/");
					boolean isOutDir = path.contains("out/artifacts");
					if (!"".equals(temp) && !isOutDir) {
						getUrls(fileLists[i].getAbsolutePath(), 1, list);
					}
				}else {
					list.add(fileLists[i].getAbsolutePath().replaceAll("\\\\", "/"));
				}
			}
		}
		return list;
	}
	
	 
	/**
	 * 获取文件/目录json列表
	 * type=1 （暂未使用）
	 * @param path
	 * @param type
	 */
	public static ArrayList<EUITreeBean> getJSONUrl(String path, int type, List<String> list) {
		ArrayList<EUITreeBean> children = new ArrayList<EUITreeBean>();
		File file = new File(path);
		if (file.isFile()) {//是文件
			EUITreeBean bean = new EUITreeBean();
			bean.setId(id++);
			if (list.contains(path)) {
				bean.setChecked(true);
			} else {
				bean.setChecked(false);
			}
			Attributes attributes = new Attributes();
			attributes.setUrl(path);
			bean.setAttributes(attributes);
			String textStr = path.split("/")[path.split("/").length-1];
			if (!"".equals(textStr)) {
				bean.setText(textStr);
				return children;
			} else {
				return null;
			}
		} 
		if (file.isDirectory()) {//是目录
			File[] fileArr = file.listFiles();
			//对目录进行排序
			ArrayList<String> fileList = sortUrl(fileArr);
			for (int i = 0; i < fileList.size(); i++) { // 循环遍历这个集合内容
				String absolutePath = fileList.get(i).replaceAll("\\\\", "/");
				String text = absolutePath.split("/")[absolutePath.split("/").length-1];
				//过滤所有.开头的文件
				String temp = text.split("\\.")[0].trim();
				//这里可能有文件无后缀的问题
				if (!"".equals(temp)) {
					EUITreeBean treeBean = new EUITreeBean();
					treeBean.setId(id++);
					File tmpFile = new File(fileList.get(i));
					if (list.contains(fileList.get(i))) {
						if (tmpFile.isDirectory()) {
							treeBean.setState("open");
							ArrayList<EUITreeBean> tmpList = getJSONUrl(fileList.get(i), 1, list);
							treeBean.setChildren(tmpList);
						} else {
							treeBean.setChecked(true);
						}
					} else {
						if (tmpFile.isDirectory()) {
							treeBean.setState("closed");
						}
						ArrayList<EUITreeBean> tmpList = getJSONUrl(fileList.get(i), 1, list);
						treeBean.setChildren(tmpList);
						treeBean.setChecked(false);
					}
					Attributes attributes = new Attributes();
					attributes.setUrl(absolutePath);
					treeBean.setAttributes(attributes);
					treeBean.setText(text);
					children.add(treeBean);
				}
			}
			return children;
		}
		return children;
	}

	/**
	 * 执行对比文件
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void doCompareFile(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String localProUrl = request.getParameter("localProUrl").replaceAll("\\\\", "/").replaceAll("//", "/");
		String svnProUrl = request.getParameter("svnProUrl").replaceAll("\\\\", "/").replaceAll("//", "/");
		
		List<String> localList = new ArrayList<String>();
		localList = getUrls(localProUrl, 1, localList);
		List<String> sameList = new ArrayList<String>();
		double startTime = System.currentTimeMillis();
		for (String local_path : localList) {
			String svn_path = local_path.replace(localProUrl, svnProUrl);
			if (local_path.contains("WEB-INF/classes")) {
				sameList.add(local_path);
			} else {
				compareFiles(local_path, svn_path, sameList);
				System.out.println("------对比文件："+local_path);
			}
		}
		
		double endTime = System.currentTimeMillis();
		double tmpTime = (endTime - startTime)/1000;
		tmpTime = (double)(Math.round(tmpTime*100))/100;
		System.out.println("------对比用时：【" + tmpTime + "s】------");
		
		localList.removeAll(sameList);
		
		//存储该项目目录下的所有上级目录
		List<String> tempList = new ArrayList<String>();
		for (String path : localList) {
			int length = path.split("/").length;
			String[] tmpStrs = path.split("/");
			for (int i = tmpStrs.length-1; i>length; i--) {
				String temp = path.split("/")[path.split("/").length-1];
				path = path.replaceAll("/"+temp, "");
				if (!tempList.contains(path)) {
					tempList.add(path);
				}
			}
		}
		localList.addAll(tempList);
		
		
		String jsonData = getJSONString(localProUrl, localList, 0);
	    JSONObject json = new JSONObject();
	    json.put("compareData", jsonData);
	    response.setContentType("text/html;charset=UTF-8");
	    response.getWriter().write(json.toString());
	}
	
	/**
	 * 获取War包目录信息
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void getWarInfo(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String warPath = request.getParameter("warPath");
		String jsonData = null;
		if (new File(warPath).isDirectory()) {
			jsonData = getJSONString(warPath, new ArrayList<String>(), 0);
		}
		JSONObject json = new JSONObject();
	    json.put("warData", jsonData);
	    response.setContentType("text/html;charset=UTF-8");
	    response.getWriter().write(json.toString().replaceAll("false", "true"));
	}
	
	/**
	 * 删除指定url文件
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void deleteUrl(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String path = request.getParameter("deleteUrl");
		String warPath = request.getParameter("warPath");
		String f = request.getParameter("f");
		int flag = 0;
		//f = 1 delete war files
		if (StringUtils.isNotBlank(f)) {
			File file = new File(path);
			if (file.exists()) {
				if (file.isFile()){
					file.delete();
				} else {
					delDirs(file);
				}
	        } else {
	        	flag = 1;
	        	System.out.println("==errorin deleteUrl---要删除的文件不存在：" + path);
			}
			file = new File(path);
			if (!file.exists()) {
				flag = 1;
				System.out.println("------删除成功!");
			} else {
				flag = 0;
				System.out.println("==errorin deleteUrl---删除失败：" + path);
			}
			JSONObject json = new JSONObject();
			String warReload = "";
			if (new File(warPath).exists())
				warReload = getJSONString(warPath, new ArrayList<String>(), 0);
			else
				warReload = "[{\"text\":\"文件已删除\",\"checked\":\"true\",\"state\":\"closed\",\"attributes\":{\"url\":\"\"}}]";
			json.put("flag", flag);
		    json.put("warReload", warReload);
		    response.setCharacterEncoding("UTF-8");
		    response.setContentType("application/json");
		    response.getWriter().write(json.toString());
			return;
		}
		//delete file、删除上级空目录
		File file = new File(path);
		String parentPath = null;
		if (path.split("/").length != 3) {
			parentPath = file.getParent();
		}
		if (file.exists()) {
			if (file.isFile()){
				file.delete();
			} else {
				delDirs(file);
			}
			if (parentPath!=null) {
				delUpDirs(parentPath);
			}
        } else {
        	flag = 1;
            System.out.println("------要删除的文件不存在:" + path );
        }
		if (flag != 1) {
			flag = file.exists()==true ? 0:1;
		}
		String warReload = "";
		if (new File(warPath).exists())
			warReload = getJSONString(warPath, new ArrayList<String>(), 0);
		else
			warReload = "[{\"text\":\"文件已删除\",\"checked\":\"true\",\"state\":\"closed\",\"attributes\":{\"url\":\"\"}}]";
		JSONObject json = new JSONObject();
		json.put("flag", flag);
	    json.put("warReload", warReload);
	    response.setCharacterEncoding("UTF-8");
	    response.setContentType("application/json");
	    response.getWriter().write(json.toString().replaceAll("false", "true"));
	}
	
	/**
	 * 复制勾选文件
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void copyCheckedFile(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String pack_addr = request.getParameter("pack_addr");
		String projectUrl = request.getParameter("projectUrl");
		
		String[] uris = projectUrl.split("/");
		String projectName = uris[uris.length-1];
		
		//获取勾选文件的url
		String checkedUrl = request.getParameter("checkedUrl").replaceAll("//", "/");
		String[] urls = checkedUrl.split(",");
		
		//文件名时间戳
		SimpleDateFormat f = new SimpleDateFormat("——HHmmss");
		Date date = new Date();
		String fileStr = "[复制]" + projectName + f.format(date);
		double startTime = System.currentTimeMillis();
		for(int i=0; i<urls.length; i++){
			if (StringUtils.isNotBlank(urls[i])) {
				File file = new File(urls[i]);
				if (file.isFile()) {
					//打包
					String oldStr = projectUrl;
					String newStr = pack_addr + fileStr + "/";
					String generate_path = urls[i].replace(oldStr, newStr);
					copyFile(urls[i], generate_path);
				} else {//目录不存在，需创建
					if (!file.exists()) {
						file.mkdir();
					}
				}
			}
		}
		
		double endTime = System.currentTimeMillis();
		double tmpTime = (endTime - startTime)/1000;
		tmpTime = (double)(Math.round(tmpTime*100))/100;
		System.out.println("------复制用时：【" + tmpTime + "s】------");
		
		JSONObject jsonObject = new JSONObject();
	    jsonObject.put("warPath", pack_addr + fileStr);
	    response.setContentType("text/html;charset=UTF-8");
	    response.getWriter().write(jsonObject.toString());
	}
	
	/**
	 * 删除目录
	 * @param file
	 */
	public static void delDirs(File file){
		File[] files = file.listFiles();
		for (File tmpFile : files) {
			if (tmpFile.isFile()) {
				tmpFile.delete();
			} else {
				delDirs(tmpFile);
			}
		}
		file.delete();
	}
	
	/**
	 * 删除上级空目录
	 * @param path
	 */
	public static void delUpDirs(String path){
		File file = new File(path);
		String parentPath = null;
		if (path.split("\\\\").length != 3) {
			parentPath = file.getParent();
		}
		if (new File(path).listFiles().length == 0) {
			file.delete();
			if (parentPath != null) {
				delUpDirs(parentPath);
			}
		}
	}
	
	/**
	 * 检查本地文件状态是否改变
	 * @return Boolean
	 * @throws IOException
	 * @throws SVNException 
	 */
	public static Boolean isFileChanged(String url) {
		Boolean resultBoolean = false;
		ourClientManager = SVNClientManager.newInstance(null, null, null); 
        File compFile = new File(url);
        SVNStatusClient statusClient = ourClientManager.getStatusClient();
        SVNStatus doStatus = null;
		try {
			doStatus = statusClient.doStatus(compFile, false);
			SVNStatusType nodeStatus = doStatus.getContentsStatus();
			if (!SVNStatusType.STATUS_NORMAL.equals(nodeStatus)) {
				resultBoolean = true;
				System.out.println("---"+ nodeStatus +"---：" + url);
			}
		} catch (SVNException e) {
			if (!url.contains("META-INF")) {
				resultBoolean = true;
				System.out.println("---判断url是否版本文件抛出异常：" + url);
//				e.printStackTrace();
			}
		}
        return resultBoolean;
	}
	
	/**
	 * 清空TXT文本内容
	 * @param file
	 */
	public static void clearInfoForFile(File file) {
		try {
	        if(!file.exists()) {
	            file.createNewFile();
	            return;
	        }
	        FileWriter fileWriter =new FileWriter(file);
	        fileWriter.write("");
	        fileWriter.flush();
	        fileWriter.close();
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
	}
	
	/**
	 * 前端请求：校验本地文件是否存在
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public static void isFileExist(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String pack_addr = request.getParameter("pack_addr");
		String file_name = request.getParameter("file_name");
		File file = new File(pack_addr + file_name);
		JSONObject json = new JSONObject();
	    json.put("flag", file.exists());
	    response.setContentType("text/html;charset=UTF-8");
	    response.getWriter().write(json.toString());
	}
	
	/**
	 * 获取svn日志(查询、确认)
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws SVNException 
	 */
	public static void getSvnInfos(HttpServletRequest request, HttpServletResponse response) throws IOException, SVNException {
		String name = request.getParameter("name");
		String password = request.getParameter("password");
		String flag = request.getParameter("flag");
		String rootUrl = request.getParameter("rootUrl");
		String begin = request.getParameter("begin");
		String end = request.getParameter("end");
		
		//校验svn连接
		SvnLoginBean loginBean = new SvnLoginBean(name, password, rootUrl);
		int result = doValidSvn(loginBean);
		if (result != 1) {
			JSONObject json = new JSONObject();
			json.put("result", result);
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write(json.toString());
			return;
		}
		
		String authors = "";
		String versions = "";
		SvnService svnService = new SvnServiceImpl();
		//确认获取svn信息
		if (StringUtils.isNotBlank(flag)) {
			authors = request.getParameter("authors");
			versions = request.getParameter("versions");
			String[] authors_sp = authors.split(",");
			String[] versions_sp = versions.split(",");
			SvnBean svnBean = null;
			Map<String, String> cMap = new HashMap<>();
			Map<String, String> dMap = new HashMap<>();
			List<Map<String, String>> list = new ArrayList<>();
			list.add(cMap);
			list.add(dMap);
			svnBean = new SvnBean();
			svnBean.setSvnLoginBean(loginBean);
			//svn传入的地址末尾无反斜线
			svnBean.setUrl(rootUrl);
			if (StringUtils.isBlank(versions_sp[0])) {
				//选择多人
				for (int i = 1; i < versions_sp.length; i++) {
					if (StringUtils.isNotBlank(versions_sp[i]) && StringUtils.isNotBlank(authors_sp[i])) {
						svnBean.setAuthor(authors_sp[i]);
						svnBean.setStartVersion(Long.parseLong(versions_sp[i]));
						
						list = svnService.getLogByVersion(svnBean, list);
					}
				}
			} else {
				//选择全部
				svnBean.setAuthor("all");
				svnBean.setStartVersion(Long.parseLong(versions_sp[0]));
				list = svnService.getLogByVersion(svnBean, list);
			}
			//校验删除文件：最新版本有，在dMap设置value：A；最新版本没有，从cMap移出。
			if (list.get(0).size() > 0) {
				list = svnService.checkDelete(svnBean, list);
			}
			cMap = list.get(0);
			dMap = list.get(1);
			String projectUrl = request.getParameter("proUrl");
			if (!new File(projectUrl).exists()) {
				System.out.println("==errorin getProFiles---"+"地址不存在："+projectUrl);
				return;
			}
			
			double startTime = System.currentTimeMillis();
			
			//获取路径下项目所有文件列表
			List<String> fileList = new ArrayList<String>();
			fileList = getUrls(projectUrl, 1, fileList);
			//获取webRootName
			String webRootName = null;
			for (String url : fileList) {
				if (url.contains("WEB-INF")) {
					String[] webinfUris = url.split("WEB-INF");
					String[] webinfUri = webinfUris[0].split("/");
					webRootName = webinfUri[webinfUri.length-1];
					break;
				}
			}
			//获取编译版本号
			double version = 0.0;
			version = getVersion(fileList);
			//判断svn与本地路径连接规则
			int startIndex = 0;
			for (Entry<String, String> entry : cMap.entrySet()) {
				String uri = entry.getKey();
				String[] uriSplit = uri.split("/");
				String tmpStr = "";
				int tmplength = 1;
				Boolean isExsit = false;
				for (int i = 1; i < uriSplit.length - 1; i++) {
					tmplength = 1;
					for (int j = 1; j <= i; j++) {
						tmplength += uriSplit[j].length() + 1;
						tmpStr = uri.substring(tmplength);
					}
					if (new File(projectUrl + tmpStr).exists()) {
						isExsit = true;
						startIndex = tmplength;
						System.out.println("------规则路径：" + projectUrl + tmpStr);
						System.out.println("------路径规则：svn路径去除多余字段[" + uri.substring(0, startIndex) + "]");
						break;
					}
				}
				if (isExsit) {
					break;
				}
			}
			if (startIndex == 0) {
				System.out.println("---errorsin getSvnInfos : 无法判断svn与本地路径连接规则！请检查本地文件是否存在！");
				return ;
			}
			//svn路径转本地路径
			List<String> changedList = new ArrayList<>();
			Map<String, String> tmpMap = new HashMap<>();
			for (Entry<String, String> entry : cMap.entrySet()) {
				String uri = entry.getKey();
				String subUri = uri.substring(startIndex);
				changedList.add(projectUrl + subUri);
				if (!new File(projectUrl + subUri).exists()) {
					tmpMap.put(projectUrl + subUri, "N");
					System.out.println("------本地不存在:"+projectUrl + subUri);
				} else {
					tmpMap.put(projectUrl + subUri, entry.getValue());
				}
			}
			String jsonData = getJSONString(projectUrl, changedList, version);
			
			JSONObject json = new JSONObject();
		    json.put("jsonData", jsonData);
		    json.put("version", version);
		    json.put("webRootName", webRootName);
		    json.put("dMap", dMap);
		    json.put("cMap", tmpMap);
		    response.setContentType("text/html;charset=UTF-8");
		    response.getWriter().write(json.toString());
		    
		    //计算运行用时
		    double endTime = System.currentTimeMillis();
		    double tmpTime = (endTime - startTime)/1000;
		    tmpTime = (double)(Math.round(tmpTime*100))/100;
		    System.out.println("------获取目录用时：【" + tmpTime + "s】------");
		    
		} else {
			//查询svn信息
			SvnBean svnBean = new SvnBean();
			svnBean.setUrl(rootUrl);
			svnBean.setBegin(begin);
			svnBean.setEnd(end);
			svnBean.setSvnLoginBean(loginBean);
			List<SvnLogBean> svnLogBeanList = null;
			svnLogBeanList = svnService.getLogByTime(svnBean);
			Map<String, String> authorMap = new HashMap<>();
			for (SvnLogBean bean : svnLogBeanList) {
				authorMap.put(bean.getAuthor(), bean.getAuthor());
			}
			JSONObject json = new JSONObject();
			json.put("logInfos", svnLogBeanList);
			json.put("authorMap", authorMap);
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write(json.toString());
		}
	}
	
	/**
	 * 打开本地文件
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public static void openDir(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String path = request.getParameter("path");
		File file=new File(path == null ? "" : path);
		if (file.exists()) {
			try {
				Runtime.getRuntime().exec(
						"rundll32 SHELL32.DLL,ShellExec_RunDLL "
								+ "Explorer.exe /select," + file.getAbsolutePath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			JSONObject json = new JSONObject();
			json.put("result", "1");
//			response.setContentType("text/html;charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
		    response.setContentType("application/json");
			response.getWriter().write(json.toString());
			System.out.println("---地址不存在");
		}
	}
	
	/**
	 * SVN连接校验
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public static void validateSvn(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String name = request.getParameter("name");
		String password = request.getParameter("password");
		String svnUrl = request.getParameter("svnUrl");
		SvnLoginBean loginBean = new SvnLoginBean(name, password, svnUrl);
		int result = doValidSvn(loginBean);
		JSONObject json = new JSONObject();
		json.put("result", result);
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().write(json.toString());
	}
	
	/**
	 * 执行svn账号连接校验
	 * @param loginBean
	 * @return
	 */
	private static int doValidSvn(SvnLoginBean loginBean) {
		PropertiesUtil propertiesUtil = new PropertiesUtil();
		propertiesUtil.setValue("name", loginBean.getName());
		propertiesUtil.setValue("password", loginBean.getPassword());
		SvnService service = new SvnServiceImpl();
		//1:连接成功，0:账号密码错误(确认权限)，-1:连接失败(检查网络)
		int result = service.testSvnConnection(loginBean);
		String svnUrl = loginBean.getSvnUrl();
		String model = loginBean.getModel();
		if (result == 1 && StringUtils.isNotBlank(svnUrl) && "store".equals(model)) {
			String _str = "svn_addr_";
			String value = "";
			Boolean isExist = false;
			for (int i = 1; i <= 50; i++) {
				value = propertiesUtil.getValue(_str + i);
				if (svnUrl.equals(value)) {
					isExist = true;
					break;
				}
			}
			if (isExist) {
				for (int i = 1; i <= 50; i++) {
					value = propertiesUtil.getValue(_str + i);
					if (StringUtils.isBlank(value)) {
						propertiesUtil.setValue(_str + i, svnUrl);
						propertiesUtil.setValue("svn_addr", svnUrl);
						break;
					}
				}
			}
		}
		return result;
	}
	
	public static void uploadFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String warPath = request.getParameter("warPath");
		String name = request.getParameter("name");
		String password = request.getParameter("password");
		SvnService service = new SvnServiceImpl();
		File file = new File(warPath);
		SvnLoginBean loginBean = null;
		String result = "";
		//svn连接测试
//		service.testSvnConnection(loginBean);
		if (file.exists()) {
			loginBean = new SvnLoginBean(name, password, warPath);
			result = service.uploadFile(file, loginBean);
		} else {
			result = "0";
		}
		JSONObject json = new JSONObject();
		json.put("result", result);
		response.setCharacterEncoding("UTF-8");
	    response.setContentType("application/json");
		response.getWriter().write(json.toString());
	}
	
	public static List<String> getSvnDirs(String name, String password, String path) {
		SvnService service = new SvnServiceImpl();
		List<String> svnDirs = service.getSvnDirs(name, password, path);
		return svnDirs;
	}
	
	public static void insertApply(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String svnUpperUrl = request.getParameter("svnUpperUrl");
		String svnUrl = request.getParameter("svnUrl");
		String apply_no = request.getParameter("apply_no");
		ApplyService service = new ApplyServiceImpl();
		ApplyBean applyBean = new ApplyBean();
		applyBean.setApply_no(apply_no);
		applyBean.setSvnUpperUrl(svnUpperUrl);
		applyBean.setSvnUrl(svnUrl);
		int result = service.insertApply(applyBean);
		JSONObject json = new JSONObject();
		json.put("result", result);
		response.setCharacterEncoding("UTF-8");
	    response.setContentType("application/json");
		response.getWriter().write(json.toString());
	}
	
	@SuppressWarnings("deprecation")
	public static void getDiff(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int status = 1;
		String path = request.getParameter("path");
		FSRepositoryFactory.setup();
		ISVNAuthenticationManager authManager =  null;
		SVNDiffClient svnDiffClient = new SVNDiffClient(authManager, null);
		File file = new File(path);
//		File diffTxt = new File(PackWarServlet.class.getResource("/diff.txt").getPath());
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			try {
				svnDiffClient.doDiff(file, SVNRevision.COMMITTED, SVNRevision.COMMITTED, SVNRevision.WORKING, true, false, baos);
			} catch (SVNException e) {
				status = 0;
				System.out.println("==errorin PackWarServlet.getDiff().doDiff() :请连接内网，获取文件修改信息！");
			}
		} catch (Exception e) {
			status = 0;
		}
		
		InputStreamReader in = null;
		BufferedReader br = null;
		StringBuffer sb = null;
		try {
			in = new InputStreamReader(new ByteArrayInputStream(baos.toByteArray()));
			br = new BufferedReader(in);
			String line;
			sb = new StringBuffer();
			while ((line = br.readLine()) != null) {
				//char(43) +   char(45) -   添加三个空格
				if (line.length() > 1) {
					if (line.charAt(0) == 43 && line.charAt(1) != 43 && 
							line.charAt(1) != 32 && line.charAt(1) != 9) {
						line = line.substring(0, 1) + "    " + line.substring(1, line.length());
					}
					if (line.charAt(0) == 45 && line.charAt(1) != 45 && 
							line.charAt(1) != 32 && line.charAt(1) != 9) {
						line = line.substring(0, 1) + "    " + line.substring(1, line.length());
					}
				}
				sb.append(line + "\r\n");
			}
		} catch (Exception e) {
			status = 0;
		} finally {
			if (br != null) {
				br.close();
			}
			if (in != null) {
				in.close();
			}
		}
//		clearInfoForFile(diffTxt);
		JSONObject json = new JSONObject();
		json.put("diff", sb.toString() == null ? "" : sb.toString());
		json.put("status", status);
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().write(json.toString());
	}
	
	public static Map<String, String> getVsionFiles(Map<String, String> doDiffMap) {
		Map<String, String> vMap = new HashMap<>();
		for (Entry<String, String> entry : doDiffMap.entrySet()) {
			SVNClientManager ourClientManager = SVNClientManager.newInstance(null, null, null); 
			File compFile = new File(entry.getKey());
			SVNStatusClient statusClient = ourClientManager.getStatusClient();
			SVNStatus doStatus = null;
			try {
				doStatus = statusClient.doStatus(compFile, false);
				SVNStatusType nodeStatus = doStatus.getContentsStatus();
				if (SVNStatusType.STATUS_MODIFIED.equals(nodeStatus)) {
					vMap.put(entry.getKey(), "modified");
//					System.out.println("---"+ nodeStatus +"---："+entry.getKey());
				} else {
					vMap.put(entry.getKey(), "none");
				}
			} catch (SVNException e) {
				e.printStackTrace();
				System.out.println("==errorin PackWarServlet.getVsionFiles() 无法判断是否版本文件："+entry.getKey());
			}
		}
		return vMap;
	}
	
}
