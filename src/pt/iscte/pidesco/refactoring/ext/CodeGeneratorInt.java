package pt.iscte.pidesco.refactoring.ext;


import java.util.List;

import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;

public interface CodeGeneratorInt {
	
	//This method is used to get the required information for the code generation
	public abstract void inputValues(String name, List<FieldDeclaration> fields, List<MethodDeclaration> methods);
	//This method returns the String witch composes the entire SuperClass
	public abstract String generateContent();
	
}
