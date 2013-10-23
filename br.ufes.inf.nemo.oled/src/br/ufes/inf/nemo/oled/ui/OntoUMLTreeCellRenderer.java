package br.ufes.inf.nemo.oled.ui;

import it.cnr.imaa.essi.lablib.gui.checkboxtree.CheckboxTree;
import it.cnr.imaa.essi.lablib.gui.checkboxtree.CheckboxTreeCellRenderer;
import it.cnr.imaa.essi.lablib.gui.checkboxtree.TreeCheckingModel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

import org.eclipse.emf.ecore.EObject;

import br.ufes.inf.nemo.oled.util.ColorPalette;
import br.ufes.inf.nemo.oled.util.ColorPalette.ThemeColor;

import RefOntoUML.Association;
import RefOntoUML.Classifier;
import RefOntoUML.Generalization;
import RefOntoUML.GeneralizationSet;
import RefOntoUML.Package;
import RefOntoUML.Property;

/**
 * OntoUML Cell Renderer for CheckBox Tree
 */
public class OntoUMLTreeCellRenderer extends DefaultTreeCellRenderer implements CheckboxTreeCellRenderer {

	private static final long serialVersionUID = 1L;
	
	public JCheckBox checkbox = new JCheckBox();
	public JPanel panel = new JPanel();
	public JLabel label = new JLabel();    	
	public JLabel uniqueName = new JLabel();
	public Boolean showUniqueName = false;     	
	
	@Override
	public boolean isOnHotspot(int x, int y) 
	{
		return (checkbox.getBounds().contains(x, y));
	}
	
	public void showOrHideUniqueName()
	{
		if(showUniqueName) showUniqueName=false;
		else showUniqueName=true;
	}
	
	public OntoUMLTreeCellRenderer() 
	{
		super();    		
		label.setFocusable(true);
		label.setOpaque(true);
		uniqueName.setFocusable(true);
		uniqueName.setOpaque(true);    		 
		panel.setLayout(new BorderLayout(0, 0));
		panel.add(BorderLayout.WEST,checkbox);
		panel.add(BorderLayout.CENTER,label);
		panel.add(BorderLayout.EAST,uniqueName);    		
		checkbox.setBackground(UIManager.getColor("Tree.textBackground"));    		
		panel.setBackground(UIManager.getColor("Tree.textBackground"));
		uniqueName.setBackground(UIManager.getColor("Tree.textBackground"));    		
	}
   
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) 
	{    		
		String elementType;
		OntoUMLElement treeNodeElem = null;    		
		
		if( ((DefaultMutableTreeNode)value).getUserObject() instanceof OntoUMLElement ){
			
			treeNodeElem = ((OntoUMLElement)((DefaultMutableTreeNode)value).getUserObject());
			
			EObject element = treeNodeElem.getElement();
			
    		if (element != null) {
    			elementType = element.getClass().toString().replace("class " +"RefOntoUML.impl.", "").replace("Impl", "");
    		} else
    			elementType = "ERROR";
    		
    		if (showUniqueName)
    		{
    			if(element instanceof Generalization) uniqueName.setText("");
    			else if(element instanceof GeneralizationSet) uniqueName.setText("");
    			else if(element instanceof Package) uniqueName.setText("");
    			else if(element instanceof Classifier) uniqueName.setText(" ("+treeNodeElem.getUniqueName()+")");
    			else if(element instanceof Association) uniqueName.setText(" ("+treeNodeElem.getUniqueName()+")");
    			else if(element instanceof Property) uniqueName.setText(" ("+treeNodeElem.getUniqueName()+")");      			
    		} else {
    			uniqueName.setText("");        			
    		}
    		
		    if (elementType.toLowerCase().equals("property"))
    			label.setIcon(new ImageIcon(getClass().getClassLoader().getResource("resources/br/ufes/inf/nemo/oled/ui/tree/property.gif")));
    		else
    			label.setIcon(new ImageIcon(getClass().getClassLoader().getResource("resources/br/ufes/inf/nemo/oled/ui/tree/"+elementType.toLowerCase()+".png")));
    		
		}else{

		}    		
	    
		label.setText(value.toString());
		    		
		uniqueName.setForeground(Color.gray);    		 		
		        				    
		if (selected){    			
			
			label.setBackground(ColorPalette.getInstance().getColor(ThemeColor.GREEN_LIGHT));
			label.setForeground(UIManager.getColor(Color.WHITE));
			//label.setBackground(UIManager.getColor("Tree.selectionBackground"));
			
			//label.setBackground(UIManager.getColor("Tree.textBackground"));    			
		}else{
			label.setBackground(UIManager.getColor("Tree.textBackground"));    			
		}
		    		
		TreeCheckingModel checkingModel = ((CheckboxTree)tree).getCheckingModel();
	   	TreePath path = tree.getPathForRow(row);
	   	
	   	boolean enabled = checkingModel.isPathEnabled(path);
	   	boolean checked = checkingModel.isPathChecked(path);
	   	boolean grayed = checkingModel.isPathGreyed(path);
	   	
	   	checkbox.setEnabled(enabled);
	   	    	   	
	   	if (grayed) {
	   		label.setForeground(Color.lightGray);
	   	} else {
	   		label.setForeground(Color.black);
	   	}
	   	    	   	
	   	checkbox.setSelected(checked);
	   	
	   	return panel;
	}
}