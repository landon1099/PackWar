package swing.page;
 
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.apache.commons.lang.StringUtils;
import org.tmatesoft.sqljet.core.internal.lang.SqlParser.add_subexpr_return;

import swing.jtree.CheckBoxTreeCellRenderer;
import swing.jtree.CheckBoxTreeNode;
import swing.jtree.CheckBoxTreeNodeSelectionListener;
import swing.pack.PackWar;

public class TreePage {
	
	public void createTree(final String path, final String gPath, List<String> list) {
		JFrame frame = new JFrame("勾选确认");
		frame.setBounds(100, 50, 600, 610);
		frame.setLocationRelativeTo(null);
        JPanel panel = new JPanel(null);
		final JTree tree = new JTree();
		//遍历本地目录
		final CheckBoxTreeNode rootNode = new CheckBoxTreeNode(path);
		rootNode.setUrl(gPath);
		List<CheckBoxTreeNode> setNode = setNode(path, list);
		for (CheckBoxTreeNode sNode : setNode) {
			rootNode.add(sNode);
		}
		DefaultTreeModel model = new DefaultTreeModel(rootNode);
		tree.setFont(new Font("Microsoft YaHei", Font.PLAIN, 15));
		tree.addMouseListener(new CheckBoxTreeNodeSelectionListener());
		tree.setModel(model);
		tree.setCellRenderer(new CheckBoxTreeCellRenderer());
		JScrollPane scroll = new JScrollPane(tree);
		scroll.setBounds(0, 40, 900, 530);
		panel.add(scroll);
        //项目目录 标签
        JLabel gLabel = new JLabel("包名称:");
        gLabel.setFont(new Font("Microsoft YaHei", Font.BOLD, 15));
        gLabel.setForeground(Color.red);
        gLabel.setFont(new Font("Microsoft YaHei", Font.BOLD, 15));
        gLabel.setLocation(20, 10);
        gLabel.setSize(60, 25);
        panel.add(gLabel);
        //生成文件名称  升级包名称
        final JTextField webrootNm = new JTextField(20);
        webrootNm.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));
        webrootNm.setLocation(100, 10);
        webrootNm.setSize(160, 25);
        webrootNm.setEditable(false);
        final PackWar packWar = new PackWar();
        if (packWar.getWebroot(path) == null) {
        	msg("无法识别【WEB-INF】目录！");
		} else {
			webrootNm.setText(packWar.getWebroot(path));
		}
        panel.add(webrootNm);
        // 创建一个下拉列表框
        String[] listData = new String[]{"SIT", "LIV"};
        final JComboBox<String> comboBox = new JComboBox<String>(listData);
        comboBox.setLocation(260, 10);
        comboBox.setSize(60, 25);
        panel.add(comboBox);
        // 设置日期
        final Date date = new Date();
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        JTextField warDate = new JTextField(sdf.format(date));
        warDate.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));
        warDate.setLocation(320, 10);
        warDate.setSize(100, 25);
        panel.add(warDate);
        
        String[] countData = new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15"};
        final JComboBox<String> countCB = new JComboBox<String>(countData);
        countCB.setLocation(420, 10);
        countCB.setSize(60, 25);
        panel.add(countCB);
        
		JButton packBtn = new JButton("打包");
		packBtn.setFont(new Font("Microsoft YaHei", Font.BOLD, 15));
		packBtn.setLocation(500, 10);
		packBtn.setSize(60, 25);
		panel.add(packBtn);
		packBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				List<String> cList = new ArrayList<>();
				getCheckedNodes(rootNode, cList);
				List<String> strList = new ArrayList<>();
				if (StringUtils.isNotBlank(webrootNm.getText())) {
					strList.add(webrootNm.getText());
					strList.add("_"+comboBox.getSelectedItem() +"_"+ sdf.format(date) +"_"+ countCB.getSelectedItem());
					String gFilePath = gPath + "/" + strList.get(0) + strList.get(1);
					if (new File(gFilePath).exists()) {
						String msg = "文件名已存在【更改名称序号】or【删除重名文件】：" + gFilePath;
						msgTwo("提示:", msg, "打开目录", "取消", gFilePath);
					} else {
						String msg = packWar.packWar(path, gPath, cList, strList);
						if ("".equals(msg)) {
							createGenTree(gFilePath);
						} else {
							msg(msg);
							createGenTree(gFilePath);
						}
					}
				} else {
					msg("无法识别【WEB-INF】目录！");
				}
			}
		});
		panel.add(packBtn);
		frame.setContentPane(panel);
		frame.setVisible(true);
		toggleNodes(tree, new TreePath(rootNode), true);//默认展开树
	}
	
	//升级包树
	public  void createGenTree(final String path) {
		JFrame frame = new JFrame("升级包");
		frame.setBounds(900, 50, 400, 610);
		frame.setLocationRelativeTo(null);
        JPanel panel = new JPanel(null);
		final JTree tree = new JTree();
		/** swing ui version **/
		final CheckBoxTreeNode rootNode = new CheckBoxTreeNode(path);
//		rootNode.setUrl(pPath);
		List<CheckBoxTreeNode> setNode = setNode(path, new ArrayList<String>());
		for (CheckBoxTreeNode sNode : setNode) {
			rootNode.add(sNode);
		}
		DefaultTreeModel model = new DefaultTreeModel(rootNode);
		tree.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));
		tree.addMouseListener(new CheckBoxTreeNodeSelectionListener());
		tree.setModel(model);
		tree.setCellRenderer(new CheckBoxTreeCellRenderer());
		
//		//遍历本地目录
//		final DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(path);
////		final CheckBoxTreeNode rootNode = new CheckBoxTreeNode(path);
//		List<String> list = new ArrayList<String>();
//		List<DefaultMutableTreeNode> setNode = setDefaultNode(path);
//		for (DefaultMutableTreeNode sNode : setNode) {
//			rootNode.add(sNode);
//		}
//		
//		DefaultTreeModel model = new DefaultTreeModel(rootNode);
        // 设置树显示根节点句柄
        tree.setShowsRootHandles(true);
//		tree.setModel(model);
		JScrollPane scroll = new JScrollPane(tree);
		scroll.setBounds(0, 40, 900, 530);
		panel.add(scroll);
		//打开目录
		JButton expandBtn = new JButton("打开目录");
		expandBtn.setFont(new Font("Microsoft YaHei", Font.BOLD, 15));
		expandBtn.setLocation(20, 10);
		expandBtn.setSize(100, 25);
		panel.add(expandBtn);
		expandBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				File file = new File(path); 
				try {
					Runtime.getRuntime().exec( "rundll32 SHELL32.DLL,ShellExec_RunDLL "
							+ "Explorer.exe /select," + file.getAbsolutePath());
				} catch (IOException e1) {
					msg("打开目录失败！");
					e1.printStackTrace();
				}
			}
		});
		frame.setContentPane(panel);
		frame.setVisible(true);
		toggleNodes(tree, new TreePath(rootNode), true);//默认展开树
	}

	//设置【勾选确认树】
	public  List<CheckBoxTreeNode> setNode(String path, List<String> list) {
		List<CheckBoxTreeNode> nodeList = new ArrayList<>();
		File file = new File(path);
		if (file.isDirectory()) {
			File[] fileArr = file.listFiles();
			ArrayList<String> fileList = sortUrl(fileArr);
			CheckBoxTreeNode node = null;
			for (String url : fileList) {
				String absolutePath = url.replaceAll("\\\\", "/");
				String text = absolutePath.split("/")[absolutePath.split("/").length-1];
				//过滤所有.开头的文件
				String temp = text.split("\\.")[0].trim();
				if (!"".equals(temp)) {
					File tmpFile = new File(url);
					boolean isContain = list.contains(url);
					if (tmpFile.isDirectory()) {
						node = new CheckBoxTreeNode(text, true, false, url);
						List<CheckBoxTreeNode> tmpNode = setNode(url, list);
						for (CheckBoxTreeNode tnode : tmpNode) {
							node.add(tnode);
						}
						nodeList.add(node);
					} else {
						if (isContain) {
							node = new CheckBoxTreeNode(text, false, true, url);
						} else {
							node = new CheckBoxTreeNode(text, false, false, url);
						}
						nodeList.add(node);
					}
				}
			}
			return nodeList;
		}
		return null;
	}
	
	//设置【升级包树】
	public  List<DefaultMutableTreeNode> setDefaultNode(String path) {
		List<DefaultMutableTreeNode> nodeList = new ArrayList<>();
		File file = new File(path);
		if (file.isDirectory()) {
			File[] fileArr = file.listFiles();
			ArrayList<String> fileList = sortUrl(fileArr);
			DefaultMutableTreeNode node = null;
			for (String url : fileList) {
				String absolutePath = url.replaceAll("\\\\", "/");
				String text = absolutePath.split("/")[absolutePath.split("/").length-1];
				//过滤所有.开头的文件
				String temp = text.split("\\.")[0].trim();
				if (!"".equals(temp)) {
					File tmpFile = new File(url);
					node = new DefaultMutableTreeNode(text);
					if (tmpFile.isDirectory()) {
						List<DefaultMutableTreeNode> tmpNode = setDefaultNode(url);
						for (DefaultMutableTreeNode tnode : tmpNode) {
							node.add(tnode);
						}
					}
					nodeList.add(node);
				}
			}
			return nodeList;
		}
		return null;
	}
	
	
	public  ArrayList<String> sortUrl(File[] files){
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
	
	//展开节点
	public  void toggleNodes(JTree tree, TreePath parent, boolean expand) {
	    // Traverse children
	    TreeNode node = (TreeNode) parent.getLastPathComponent();
	    if (node.getChildCount() >= 0) {
	        for (Enumeration<?> e = node.children(); e.hasMoreElements();) {
	            TreeNode n = (TreeNode) e.nextElement();
	            TreePath path = parent.pathByAddingChild(n);
	            toggleNodes(tree, path, expand);
	        }
	    }
	 
	    if (expand) {
	        tree.expandPath(parent);
	    } else {
	        tree.collapsePath(parent);
	    }
	}
	
	//获取勾选的路径
	public  void getCheckedNodes(CheckBoxTreeNode node, List<String> list) 
	{
	   if (node.isSelected() && !node.getAllowsChildren()) {
		   String url = node.getUrl();
		   if (StringUtils.isNotBlank(url)) {
			   list.add(url.replaceAll("\\\\", "/"));
		   }
	   }
	   if (node.getChildCount() >= 0) {
		   for (Enumeration<?> e = node.children(); e.hasMoreElements(); ) {
			   CheckBoxTreeNode n = (CheckBoxTreeNode)e.nextElement();
			   getCheckedNodes(n, list);
		   }
	   }
	}
	
	
	
	/**
	 * 警告提示框
	 * @param msg
	 */
    public  void msg(String msg) {
    	if (JOptionPane.OK_OPTION==JOptionPane
    			.showOptionDialog(null, msg, "提示", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, new String[]{"确认"}, null)){
//    		System.out.println("ok");
		} else {
//			System.out.println("sss");
		}
    }
    /**
     * 两个按钮消息提示框
     * @param title 标题
     * @param msg 提示信息
     * @param confrim 确认
     * @param cancel 取消
     */
    public  void msgTwo(String title, String msg, String confrim, String cancel, String path) {
    	Object[] options ={ confrim, cancel };  //自定义按钮上的文字
    	int m = JOptionPane.showOptionDialog(null, msg, title,JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]); 
    	if (!"".equals(confrim) && m == 0) {
    		File file=new File(path); 
    		try {
				Runtime.getRuntime().exec("rundll32 SHELL32.DLL,ShellExec_RunDLL "
						+ "Explorer.exe /select," + file.getAbsolutePath());
			} catch (IOException e) {
				msg("无法打开目录");
				e.printStackTrace();
			}
		}
    }
    
}