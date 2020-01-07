package pt.iscte.pidesco.refactoring.ext;


import java.util.List;

import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;

public interface CodeGeneratorInt {
	
	/**
	 * This method is used to get the required information for the code generation
	 * @param name is the string that is used for the superClass
	 * @param fields is the List that should be placed in the code
	 * @param methods is the List that should be placed in the code
	 * */
	public abstract void inputValues(String name, List<FieldDeclaration> fields, List<MethodDeclaration> methods);
	
	/**This method is intended to process the information and compute the code for the SuperClass
	 * @return returns the String witch composes the entire SuperClass code
	 * */
	public abstract String generateContent();
	
}
