
import freeseawind.lf.LittleLuckLookAndFeel;
import swing.page.MainPage;

import javax.swing.*;
import java.awt.*;


public class PackMain {
	
	/** Swing 版本 main function **/
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable(){
            public void run(){
                try{
                	UIManager.setLookAndFeel(LittleLuckLookAndFeel.class.getName());
                    new MainPage().CreateJFrame();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
	}
	
}
