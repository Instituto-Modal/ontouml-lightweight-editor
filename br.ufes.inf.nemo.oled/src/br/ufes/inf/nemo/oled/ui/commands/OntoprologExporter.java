package br.ufes.inf.nemo.oled.ui.commands;

import java.awt.Component;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

import RefOntoUML.Element;
import RefOntoUML.Namespace;
import br.ufes.inf.nemo.oled.Main;
import br.ufes.inf.nemo.oled.OntoprologExceptionType;
import br.ufes.inf.nemo.oled.draw.Connection;
import br.ufes.inf.nemo.oled.draw.DiagramElement;
import br.ufes.inf.nemo.oled.model.UmlDiagram;
import br.ufes.inf.nemo.oled.model.UmlProject;
import br.ufes.inf.nemo.oled.ui.diagram.DiagramEditor;
import br.ufes.inf.nemo.oled.umldraw.structure.ClassElement;
import br.ufes.inf.nemo.oled.umldraw.structure.StructureDiagram;
import br.ufes.inf.nemo.oled.util.ApplicationResources;
import br.ufes.inf.nemo.oled.util.ModelHelper;

/**
 * This class exports a diagram to a Owl File.
 */
public class OntoprologExporter {
	
	private List<String> filterEntitys = Arrays.asList("category", "collective", "kind", "mixin", "mode", "phase", "quantity", "role", "role mixin", "subkind");

	/**
	 * Export the editor graphics to a file in PNG format.
	 * 
	 * @param editor
	 *            the editor
	 * @param file
	 *            the file to write
	 * @throws IOException
	 *             if error occurred
	 */
	public void writeOntoprolog(Component comp, File file, DiagramEditor diagram) throws IOException {
		if (canWrite(comp, file)) {
			BufferedWriter out = null;
			try {
				FileWriter fstream = new FileWriter(file + this.getSuffix(), true);
				out = new BufferedWriter(fstream);
				out.write(this.getHeaderFile());
						
				try {
					out.write(this.diagramToOntoprolog(diagram.getDiagram()));					
				} catch (OntoprologExceptionType e) {
					Main.printOutLine("Error to ontoprolog code: "+e.getMessage());	
					JOptionPane.showMessageDialog(null, "Error to generate ontoprolog code:\n"+e.getMessage(), ApplicationResources.getInstance().getString("error.readfile.title"), JOptionPane.ERROR_MESSAGE);
				}
					
				out.write(this.getFooterFile());
			} catch (IOException e) {
				    System.err.println("Error: " + e.getMessage());
			} finally {
				if(out != null) {
					out.close();
				}
			}
		}
	}
	
	private String diagramToOntoprolog(StructureDiagram diagram) throws OntoprologExceptionType {
		String result = "";
		String pattern = "^<<(\\w+)>>\\s?(\\w+)$";
		Pattern r = Pattern.compile(pattern);
		for(DiagramElement c: diagram.getChildren()) {
			if (diagram.getConnections().contains(c)) continue;
			Matcher m = r.matcher(c.toString().toLowerCase());
			if (m.find()) {
				String type = m.group(1);
				if (!this.filterEntitys.contains(type))
					throw new OntoprologExceptionType("Tipo de entidade não suportada: " + type);
				
				result += m.group(2) + " :: " + m.group(1) + "\n";
				
				List<Connection> connections = (List<Connection>)((ClassElement)c).getConnections();
				for(Connection con: connections) {
					String aux = con.toString();
				}
			}
		}
		return result;
	}

	protected boolean canWrite(Component component, File file) {
	    if (file.exists()) {
	      ApplicationResources resources = ApplicationResources.getInstance();
	      String message = resources.getString("dialog.replacefile.confirm.msg");
	      message = MessageFormat.format(message, file.getName());
	      String title = resources.getString("dialog.replacefile.confirm.title");
	      return JOptionPane.showConfirmDialog(component, message, title,
	        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
	    }
	    return true;
	  }
	
	protected String getHeaderFile() {
		return ":- include('../../../ontoprolog/src/ontoprolog/ontoprolog').\n"
				+ "%-------\n"
				+ "main program\n"
				+ "%-------\n\n";
	}
	
	protected String getFooterFile() {
		return "\n%-------\n"
				+ "Ontoprolog compilation\n"
				+ "%-------\n"
				+ ":- otp_compile, check_semantics.\n";
	}

	/**
	 * {@inheritDoc}
	 */
	protected String getSuffix() {
		return ".pl";
	}
}
