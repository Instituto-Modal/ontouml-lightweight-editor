package br.ufes.inf.nemo.ocl2owl_swrl.factory.uml2.uml.internal.impl;

import java.util.ArrayList;
import java.util.Set;

import org.eclipse.uml2.uml.internal.impl.NamedElementImpl;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.SWRLAtom;
import org.semanticweb.owlapi.model.SWRLDArgument;

import br.ufes.inf.nemo.common.ontoumlparser.OntoUMLParser;
import br.ufes.inf.nemo.ocl2owl_swrl.exceptions.NonImplemented;
import br.ufes.inf.nemo.ocl2owl_swrl.exceptions.Ocl2Owl_SwrlException;
import br.ufes.inf.nemo.ocl2owl_swrl.factory.Factory;



/**
 * @author fredd_000
 * @version 1.0
 * @created 24-set-2013 09:16:13
 */
public class NamedElementImplFactory extends Factory {

	public NamedElementImpl m_NamedElementImpl;
	
	public NamedElementImplFactory(NamedElementImpl m_NamedElementImpl){
		this.m_NamedElementImpl = m_NamedElementImpl;
	}
	
	/**
	 * This method solve the rule fragment recursively and has to be overridden.
	 */
	@Override
	public ArrayList<SWRLDArgument> solve(String ctStereotype, OntoUMLParser refParser, String nameSpace, OWLOntologyManager manager, OWLDataFactory factory, OWLOntology ontology, Set<SWRLAtom> antecedent, Set<SWRLAtom> consequent, SWRLDArgument referredArgument, Boolean operatorNot, int repeatNumber, Boolean leftSideOfImplies) throws Ocl2Owl_SwrlException {
		//get the string of the rule
		String rule = getStrRule(this.m_NamedElementImpl);
		//throw as non implemented method
		throw new NonImplemented("solve()", rule);
	}
	
	
}