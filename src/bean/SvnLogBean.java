package bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SvnLogBean {
	private Long version;
	private List<String> options = new ArrayList<>();
	private String author;
	private Map<String, String> authorMap = new HashMap<>();
	private String date; 
	private String info; 
	private List<ChangedUrls> changedUrls = new ArrayList<>();
	private Map<String, String> cMap = new HashMap<>();
	private Map<String, String> dMap = new HashMap<>();
	
	public Map<String, String> getcMap() {
		return cMap;
	}
	public void setcMap(Map<String, String> cMap) {
		this.cMap = cMap;
	}
	public Map<String, String> getdMap() {
		return dMap;
	}
	public void setdMap(Map<String, String> dMap) {
		this.dMap = dMap;
	}
	public Map<String, String> getAuthorMap() {
		return authorMap;
	}
	public void setAuthorMap(Map<String, String> authorMap) {
		this.authorMap = authorMap;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Long getVersion() {
		return version;
	}
	public void setVersion(Long version) {
		this.version = version;
	}

	public List<String> getOptions() {
		return options;
	}
	public void setOptions(List<String> options) {
		this.options = options;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public List<ChangedUrls> getChangedUrls() {
		return changedUrls;
	}
	public void setChangedUrls(List<ChangedUrls> changedUrls) {
		this.changedUrls = changedUrls;
	}
	
	
}
