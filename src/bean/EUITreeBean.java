package bean;

import java.util.ArrayList;
import java.util.List;

public class EUITreeBean {
	private int id;				//编号
	private String text;		//节点名(文件/目录名)
	private boolean checked;		//是否选中
	private Attributes attributes;	//可选设置参数
	private String state = "open";
	private List<EUITreeBean> children = new ArrayList<EUITreeBean>(); //子节点
	
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public Attributes getAttributes() {
		return attributes;
	}
	public void setAttributes(Attributes attributes) {
		this.attributes = attributes;
	}
	public List<EUITreeBean> getChildren() {
		return children;
	}
	public void setChildren(List<EUITreeBean> children) {
		this.children = children;
	}
	@Override
	public String toString() {
		return "EUITreeBean [id=" + id + ", text=" + text + ", checked="
				+ checked + ", attributes=" + attributes + ", children="
				+ children + "]";
	}
	
	
}
