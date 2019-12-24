package swing.page;

import org.apache.commons.lang.StringUtils;
import org.tmatesoft.svn.core.SVNException;
import swing.pack.PackWar;
import utils.PropertiesUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainPage {
	
	/**
	 * 创建主页面
	 */
    public  void CreateJFrame() {
        JFrame frame = new JFrame("PackWar");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //关闭所有界面
        final JPanel panel = new JPanel(null);
        //项目目录 标签
        JLabel proUrl = new JLabel("项目目录：");
        proUrl.setForeground(Color.red);
        proUrl.setFont(new Font("Microsoft YaHei", Font.BOLD, 15));
        proUrl.setLocation(10, 40);
        proUrl.setSize(100, 25);
        panel.add(proUrl);
        //项目目录  输入框
        final JTextField proText = new JTextField(60);
        final PropertiesUtil pros = new PropertiesUtil();
        String pPath = pros.getValue("swing_pro_tmp");
        if (StringUtils.isNotBlank(pPath)  && new File(pPath).exists()) {
        	proText.setText(pPath);
		}
        proText.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));
        proText.setLocation(100, 40);
        proText.setSize(660, 25);
        panel.add(proText);
        final JButton pBtn = new JButton("选择");
        pBtn.setFont(new Font("Microsoft YaHei", Font.BOLD, 15));
        pBtn.setLocation(780, 40);
        pBtn.setSize(80, 25);
        panel.add(pBtn);
        // 文件选择器
        final JFileChooser jfcPro = new JFileChooser();
        // 文件选择器的初始目录
        final String pAddr = pros.getValue("swing_pro_addr");
        if (StringUtils.isNotBlank(pAddr) && new File(pAddr).exists()) {
        	jfcPro.setCurrentDirectory(new File(pAddr));//设置默认打开路径
		} else {
			jfcPro.setCurrentDirectory(new File("c:/"));
		}
        pBtn.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		if (e.getSource().equals(pBtn)) {
        			jfcPro.setFileSelectionMode(1);
        			int state = jfcPro.showOpenDialog(null);// 此句是打开文件选择器界面的触发语句
        			if (state == 1) {
        				return;
        			} else {
        				File file = jfcPro.getSelectedFile();
        				proText.setText(file.getAbsolutePath());// 选择到的目录
        			}
        		}
        	}
        });
        //生成目录 标签
        JLabel genUrl = new JLabel("生成目录：");
        genUrl.setForeground(Color.red);
        genUrl.setFont(new Font("Microsoft YaHei", Font.BOLD, 15));
        genUrl.setLocation(10, 140);
        genUrl.setSize(100, 25);
        panel.add(genUrl);
        //生成目录  输入框
        final JTextField genText = new JTextField(60);
        genText.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));
        genText.setLocation(100, 140);
        genText.setSize(660, 25);
        String gPath = pros.getValue("swing_gen_tmp");
        if (StringUtils.isNotBlank(gPath) && new File(gPath).exists()) {
        	genText.setText(gPath);
        }
        panel.add(genText);
        final JButton gBtn = new JButton("选择");
        gBtn.setFont(new Font("Microsoft YaHei", Font.BOLD, 15));
        gBtn.setLocation(780, 140);
        gBtn.setSize(80, 25);
        panel.add(gBtn);
        // 文件选择器
        final JFileChooser jfcGen = new JFileChooser();
        // 文件选择器的初始目录
        String gAddr = pros.getValue("swing_gen_addr");
        if (StringUtils.isNotBlank(gAddr) && new File(gAddr).exists()) {
        	jfcGen.setCurrentDirectory(new File(gAddr));
		} else {
			jfcGen.setCurrentDirectory(new File("c:/"));
		}
        gBtn.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		if (e.getSource().equals(gBtn)) {
        			jfcGen.setFileSelectionMode(1);
        			int state = jfcGen.showOpenDialog(null);// 此句是打开文件选择器界面的触发语句
        			if (state == 1) {
        				return;
        			} else {
        				File file = jfcGen.getSelectedFile();
        				genText.setText(file.getAbsolutePath());// 选择的目录
        			}
        		}
        	}
        });
        //单选按钮
        JRadioButton radio1 = new JRadioButton("不校验");
        radio1.setFont(new Font("Microsoft YaHei", Font.BOLD, 14));
        radio1.setLocation(350, 220);
        radio1.setSize(100, 25);
        JRadioButton radio2 = new JRadioButton("本地SVN");
        radio2.setFont(new Font("Microsoft YaHei", Font.BOLD, 14));
        radio2.setLocation(450, 220);
        radio2.setSize(100, 25);
        // 创建按钮组，把两个单选按钮添加到该组
        final ButtonGroup btnGroup = new ButtonGroup();
        btnGroup.add(radio1);
        btnGroup.add(radio2);
        radio2.setSelected(true);
        panel.add(radio1);
        panel.add(radio2);
        JButton confirmBtn = new JButton("确认");
        confirmBtn.setFont(new Font("Microsoft YaHei", Font.BOLD, 15));
        confirmBtn.setLocation(385, 300);
        confirmBtn.setSize(100, 25);
        confirmBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String pPath = proText.getText().replaceAll("\\\\", "/");
				String gPath = genText.getText().replaceAll("\\\\", "/");
				if (pPath.endsWith("/")) {
					pPath = pPath.substring(0, pPath.split("/").length - 1);
				}
				if (gPath.endsWith("/")) {
					gPath = gPath.substring(0, gPath.split("/").length - 1);
				}
				if (!isPathExist(pPath)) {
					msg("项目目录不存在：" + pPath);
					return;
				}
				if (!isPathExist(gPath)) {
					msg("生成目录不存在：" + gPath);
					return;
				}
				Component[] jcbs = panel.getComponents();
	            for (Component component : jcbs) {
	            	JRadioButton jrb = null;
	            	try {
	            		jrb = (JRadioButton) component;
					} catch (Exception e2) {
						
					}
	            	if (jrb != null && jrb.isSelected()) {
	            		String _proText = "";
	            		String _genText = "";
	            		for (int i = 0; i < pPath.split("/").length - 1; i++) {
	            			_proText += pPath.split("/")[i] + "/";
						}
	            		for (int i = 0; i < gPath.split("/").length - 1; i++) {
	            			_genText += gPath.split("/")[i] + "/";
	            		}
	            		_proText = _proText.substring(0, _proText.length() - 1);
	            		_genText = _genText.substring(0, _genText.length() - 1);
	            		pros.setValue("swing_pro_addr", _proText);//保存项目目录
	            		pros.setValue("swing_gen_addr", _genText);//保存生成目录
	            		String text = jrb.getText();
	            		List<String> cList = null;
	            		TreePage treePage = new TreePage();
	            		switch (text) {
	            		case "不校验":
	            			treePage.createTree(pPath, gPath, new ArrayList<String>());
	            			break;
	            		case "本地SVN":
	            			PackWar packWar = null;
	            			try {
	            				packWar = new PackWar();
								cList = packWar.checkLocalSvn(pPath);
								treePage.createTree(pPath, gPath, cList);
							} catch (SVNException e1) {
								e1.printStackTrace();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
	            			break;
//	            		case "远程SVN":
//	            			break;
	            		default:
	            			break;
	            		}
					}
	            }
			}
        });
        panel.add(confirmBtn);
        frame.setContentPane(panel);
        frame.setVisible(true);
        frame.setBounds(200, 50, 900, 600);
        frame.setLocationRelativeTo(null);
    }
    
    public  Boolean isPathExist(String path) {
		return new File(path).exists();
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
    public  void msgTwo(String title, String msg, String confrim, String cancel) {
    	Object[] options ={ confrim, cancel };  //自定义按钮上的文字
    	int m = JOptionPane.showOptionDialog(null, msg, title,JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]); 
    }
}
