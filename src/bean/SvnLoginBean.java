package bean;

public class SvnLoginBean {
	private String name;
	private String password;
	private String svnUrl;
	private String model;//模式
	
	
	
	public SvnLoginBean() {
		super();
	}

	public SvnLoginBean(String name, String password, String svnUrl) {
		this.name = name;
		this.password = password;
		this.svnUrl = svnUrl;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSvnUrl() {
		return svnUrl;
	}
	public void setSvnUrl(String svnUrl) {
		this.svnUrl = svnUrl;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	
	
}
