package br.ufes.inf.nemo.ocl2alloy;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.ocl.Environment;
import org.eclipse.ocl.ParserException;
import org.eclipse.ocl.OCLInput;
import org.eclipse.ocl.uml.UMLEnvironmentFactory;
import org.eclipse.uml2.uml.Constraint;

import br.ufes.inf.nemo.ontouml2uml.OntoUML2UML;

/**
 * @author John Guerson
 */

public class OCLParser {
	
	public List<Constraint> umlconstraintsList;
	
	public org.eclipse.ocl.uml.UMLEnvironment umlenv;
	
    public org.eclipse.ocl.utilities.UMLReflection <
    	org.eclipse.uml2.uml.Package,
    	org.eclipse.uml2.uml.Classifier,
    	org.eclipse.uml2.uml.Operation,
    	org.eclipse.uml2.uml.Property,
    	org.eclipse.uml2.uml.EnumerationLiteral,
    	org.eclipse.uml2.uml.Parameter,
    	org.eclipse.uml2.uml.State,
    	org.eclipse.uml2.uml.CallOperationAction,
    	org.eclipse.uml2.uml.SendSignalAction,
    	org.eclipse.uml2.uml.Constraint
    > umlreflection;
    
    public String logDetails;
    
    /**
     * Constructor.
     */
    public OCLParser () { logDetails = new String(); }
    
    /**
     * Test...
     */
    public static void main (String[] args)
    {
    	OCLParser oclparser = new OCLParser();
    	String refpath = "C:\\Users\\John\\SVNs\\SVN-OLED\\OCL2Alloy\\model\\project.refontouml";
    	String oclPath = "C:\\Users\\John\\SVNs\\SVN-OLED\\OCL2Alloy\\model\\project.ocl";
    	
    	try {
    		
			oclparser.parse(oclPath, refpath);
			System.out.println(oclparser.logDetails);
			
		} catch (IOException | ParserException e) {			
			e.printStackTrace();
		}
    }
    
    /**
     *	Parse OCL from a OntoUML Package Model, String Constraints and UML Path. 
     */
	public Resource parse(RefOntoUML.Package refmodel, String oclConstraints, String umlPath)
	{		
		Resource umlResource = null;
		boolean succeeds = false;
		
		try {
			
			umlResource = OntoUML2UML.Transformation(refmodel,umlPath);
			logDetails += OntoUML2UML.logDetails;
		
			org.eclipse.uml2.uml.Package umlmodel = (org.eclipse.uml2.uml.Package) umlResource.getContents().get(0);
			umlResource.getResourceSet().getPackageRegistry().put(null,umlmodel);	
			
			org.eclipse.ocl.uml.OCL.initialize(umlResource.getResourceSet());
			
			// this line was added due to a bug of Eclipse :
			// https://bugs.eclipse.org/bugs/show_bug.cgi?id=372258
			Environment.Registry.INSTANCE.registerEnvironment(new UMLEnvironmentFactory().createEnvironment());
			
			org.eclipse.ocl.uml.UMLEnvironmentFactory envFactory = new org.eclipse.ocl.uml.UMLEnvironmentFactory(umlResource.getResourceSet());
			umlenv = envFactory.createEnvironment();		
			org.eclipse.ocl.uml.OCL myOCL = org.eclipse.ocl.uml.OCL.newInstance(umlenv);
			myOCL.setParseTracingEnabled(true);		
			
			OCLInput document = new OCLInput(oclConstraints);		
			
			logDetails += "\n"+"Parsing OCL Domain Constraints...";
					
			umlconstraintsList = myOCL.parse(document);
			umlreflection = umlenv.getUMLReflection();		
		
			succeeds = true;
		}catch(ParserException e1)
		{
			logDetails += "\nA Parser Error ocurred: "+e1.getMessage()+"\n";			
		}
		
		if(succeeds) logDetails += "\n"+"Executed Succesfully.\n";
		else logDetails += "\n"+"A Undentified Error ocurred.\n";
		
		return umlResource;
	}
    
	/**
	 * Parse OCL from a OCL Path and OntoUML Path.
	 */
    public Resource parse(String oclAbsolutePath, String refAbsolutePath) throws IOException,ParserException
	{   	
    	
		Resource umlResource = OntoUML2UML.Transformation(refAbsolutePath);							
		this.logDetails += OntoUML2UML.logDetails;
		
		org.eclipse.uml2.uml.Package umlmodel = (org.eclipse.uml2.uml.Package) umlResource.getContents().get(0);		
		umlResource.getResourceSet().getPackageRegistry().put(null,umlmodel);		
		org.eclipse.ocl.uml.OCL.initialize(umlResource.getResourceSet());
		
		// this line was added due to a bug of Eclipse :
		// https://bugs.eclipse.org/bugs/show_bug.cgi?id=372258
		Environment.Registry.INSTANCE.registerEnvironment(new UMLEnvironmentFactory().createEnvironment());
		
		org.eclipse.ocl.uml.UMLEnvironmentFactory envFactory = new org.eclipse.ocl.uml.UMLEnvironmentFactory(umlResource.getResourceSet());
		umlenv = envFactory.createEnvironment();		
		org.eclipse.ocl.uml.OCL myOCL = org.eclipse.ocl.uml.OCL.newInstance(umlenv);
		myOCL.setParseTracingEnabled(true);
		
		InputStream input = new FileInputStream(oclAbsolutePath);
		org.eclipse.ocl.OCLInput document = new org.eclipse.ocl.OCLInput(input);	
		
		umlconstraintsList = myOCL.parse(document);
		umlreflection = umlenv.getUMLReflection();
		
		return umlResource;
	}	
}