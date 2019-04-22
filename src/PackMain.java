
import java.awt.EventQueue;

import javax.swing.UIManager;

import swing.page.MainPage;
import freeseawind.lf.LittleLuckLookAndFeel;


public class PackMain {
	
	/** Swing 版本 main function **/
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable(){
            public void run(){
                try{
                	UIManager.setLookAndFeel(LittleLuckLookAndFeel.class.getName());
                	MainPage.CreateJFrame();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
	}
	
}
