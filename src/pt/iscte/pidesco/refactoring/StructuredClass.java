package pt.iscte.pidesco.refactoring;

import java.lang.reflect.AnnotatedType;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;

/**
 * @author marcorodrigues
 *
 */
/**
 * @author marcorodrigues
 *
 */
public class StructuredClass {

	private String name;
	private Boolean hasSuperClass = false;
	private Class<?> superClassType;
	private Boolean isAbstract = false;
	private List<AnnotatedType> annotatedTypes = new ArrayList<AnnotatedType>();
	private List<FieldDeclaration> fields = new ArrayList<FieldDeclaration>();
	private List<MethodDeclaration> methods = new ArrayList<MethodDeclaration>();
	private List<Modifier> modifiers = new ArrayList<Modifier>();
	

	public StructuredClass() {
		
	}
	
	public void setName(String s) {
		this.name = s;
	}
	
	public void setHasSuperClass(Boolean hasSuperClass) {
		this.hasSuperClass = hasSuperClass;
	} 

	public void setAnnotatedTypes(List<AnnotatedType> annotatedTypes) {
		this.annotatedTypes = annotatedTypes;
	}

	public void setFields(List<FieldDeclaration> fields) {
		this.fields = fields;
	}

	public void setMethods(List<MethodDeclaration> methods) {
		this.methods = methods;
	}

	public void setHasSuperClass() {
		this.hasSuperClass = true;
	}
	
	public void setSuperClassType(Class<?> clazz) {
		this.superClassType = clazz;
	}
	
	public void addAnnotatedTypes(AnnotatedType e){
		this.annotatedTypes.add(e);
	}
		
	public void addField(FieldDeclaration f) {
		this.fields.add(f);
	}
	
	public void addMethod(MethodDeclaration m) {
		this.methods.add(m);
	}

	public void addModifier(Modifier m) {
		this.modifiers.add(m);
	}
	
	public Boolean getHasSuperClass() {
		return hasSuperClass;
	}

	public List<AnnotatedType> getAnnotatedTypes() {
		return annotatedTypes;
	}

	public List<FieldDeclaration> getFields() {
		return fields;
	}

	public List<MethodDeclaration> getMethods() {
		return methods;
	}
	
	public List<Modifier> getModifiers() {
		return modifiers;
	}

	public Class<?> getSuperClassType() {
		return superClassType;
	}
	
	public void setModifiers(List<Modifier> modifiers) {
		this.modifiers = modifiers; 
	}

	public String toString() {
		String s = name + "\n";
		s = s + "Interfaces" + "\n";
		for (AnnotatedType annotatedType : annotatedTypes) {
			s = s + "	" + annotatedType.toString() + "\n";
		}
		s = s + "Has Super Class: " + hasSuperClass.toString() + "\n";
		s = s + "Is Abstract: " + isAbstract.toString() + "\n";
		s = s + "Fields:" + "\n";
		for (FieldDeclaration field : fields) {
			s = s + "	" + field.toString() + "\n";
		}
		for (MethodDeclaration method : methods) {
			s = s + "	" + method.toString() + "\n";
		}
		return s;
	}

	public void setAbstract(boolean a) {
		this.isAbstract = a;
	}
	
}
