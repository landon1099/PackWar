package swing.jtree;
 
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
 

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JTree;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.DefaultTreeModel;

import org.apache.commons.lang.StringUtils;
 
public class CheckBoxTreeNodeSelectionListener extends MouseAdapter
{
	@Override
	public void mouseClicked(MouseEvent event) 
	{
		JTree tree = (JTree)event.getSource();
		int x = event.getX();
		int y = event.getY();
		int row = tree.getRowForLocation(x, y);
		TreePath path = tree.getPathForRow(row);
		if (path != null) {
			CheckBoxTreeNode node = (CheckBoxTreeNode)path.getLastPathComponent();
			boolean isSelected = !node.isSelected();
			node.setSelected(isSelected);
			((DefaultTreeModel)tree.getModel()).nodeStructureChanged(node);
		}
	}
	
}