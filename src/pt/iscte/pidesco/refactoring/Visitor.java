package pt.iscte.pidesco.refactoring;


import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class Visitor extends ASTVisitor{

	private StructuredClass sc = new StructuredClass(); 
	private Compare c;
	
	public Visitor(Compare c) {
		this.c = c;
	}
	
	//visita as classes pretendidos
	public boolean visit(TypeDeclaration node){
		if(node.isInterface()) {
			return false;
		}
		if(node.getSuperclassType() != null) {
			sc.setHasSuperClass(true);
			sc.setSuperClassType(node.getSuperclassType().getClass());
		}
		sc.setName(node.getName().toString());
		
		sc.setAbstract(Modifier.isAbstract(node.getFlags())); 
		
//		for (Modifier modifier : node.getModifiers()) {
			
//		}
		for (FieldDeclaration fieldDec : node.getFields()) {
			sc.addField(fieldDec);
		}
		for (MethodDeclaration methodDec : node.getMethods()) {
			sc.addMethod(methodDec);
		}
		c.addClasses(sc);
		return true;
	}
}
