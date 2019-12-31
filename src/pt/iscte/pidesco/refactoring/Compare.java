package pt.iscte.pidesco.refactoring;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;

public class Compare {

	private List<StructuredClass> classes = new ArrayList<StructuredClass>();
	private List<StructuredClass> chosenClasses = new ArrayList<StructuredClass>();
	private List<FieldDeclaration> fields = new ArrayList<>();
	private List<MethodDeclaration> methods = new ArrayList<>();
	private List<FieldDeclaration> final_fields = new ArrayList<>();
	private List<MethodDeclaration> final_methods = new ArrayList<>();
	private Matrix<StructuredClass,FieldDeclaration> mFieldsMatrix;
	private Matrix<StructuredClass,MethodDeclaration> mMethodsMatrix;

	public Compare() {

	}

	public Compare(List<StructuredClass> classes) {
		this.classes = classes;
	}

	public void addClasses(StructuredClass clazz) {
		this.classes.add(clazz);
	}

	public String toString() {
		String s = "";
		for (StructuredClass clazz : classes) {
			s=s+clazz.toString();
		}
		return s;
	}

	public static boolean sameField(FieldDeclaration f1, FieldDeclaration f2) {
		if(f1.toString().equals(f2.toString())) {
			return true;
		}
		return false;
	}

	public static boolean sameMethod(MethodDeclaration m1, MethodDeclaration m2) {
		if(m1.toString().equals(m2.toString())) {
			return true;
		}
		return false;
	}

	public void compileFiedlMatrix() {
		Matrix<StructuredClass, FieldDeclaration> m = new Matrix<StructuredClass, FieldDeclaration>( fields.size(), classes.size());
		for (StructuredClass structuredClass : classes) {
			m.addC(structuredClass);
		}
		for (FieldDeclaration field: fields) {
			m.addM(field);
		}
		for (StructuredClass c : classes) {
			for (FieldDeclaration f : fields) {
				for (int i = 0; i < c.getFields().size(); i++) {
					if(sameField(c.getFields().get(i),f)){
						m.setValue(c, f, 1);
					}
				}
			}
		} 
		this.mFieldsMatrix = m;
	}

	public void compileMethodlMatrix() {
		Matrix<StructuredClass, MethodDeclaration> m = new Matrix<StructuredClass, MethodDeclaration>(methods.size(), chosenClasses.size());
		for (StructuredClass structuredClass : chosenClasses) {
			m.addC(structuredClass);
		}
		for (MethodDeclaration method: methods) {
			m.addM(method);
		}
		for (StructuredClass c : chosenClasses) {
			for (MethodDeclaration method : methods) {
				for (int i = 0; i < c.getMethods().size(); i++) {
					if(sameMethod(c.getMethods().get(i),method)){
						m.setValue(c, method, 1);
					}
				}
			}
		} 
		this.mMethodsMatrix = m;
	}

	public void getAllFields() {
		List<FieldDeclaration> fieldList = new ArrayList<FieldDeclaration>();
		int i;
		for (StructuredClass clazz : classes) {
			for (FieldDeclaration f1: clazz.getFields()) {
				i = 0;
				boolean contains = false;
				if(fieldList.isEmpty()) {
					fieldList.add(f1);
					break;
				}
				while(i < fieldList.size()) {
					if(sameField(f1, fieldList.get(i))) {
						i = 0;
						contains = true;
						break;
					}
					i++;
				}
				if(!contains) {
					fieldList.add(f1);
					contains = false;
				}
			}
		}
		this.fields = fieldList;
	}

	public void getAllMethods() {
		List<MethodDeclaration> methodList = new ArrayList<MethodDeclaration>();
		int i;
		for (StructuredClass clazz : chosenClasses) {
			for (MethodDeclaration m1: clazz.getMethods()) {
				i = 0;
				boolean contains = false;
				if(methodList.isEmpty()) {
					methodList.add(m1);
					break;
				}
				while(i < methodList.size()) {
					if(sameMethod(m1, methodList.get(i))) {
						i = 0;
						contains = true;
						break;
					}
					i++;
				}
				if(!contains) {
					methodList.add(m1);
					contains = false;
				}
			}
		}
		this.methods = methodList;
	}

	public void compare() {

		getAllFields();
		compileFiedlMatrix();
		int[] l = mFieldsMatrix.getSugestion();
		mFieldsMatrix.choose(l);
		this.final_fields = mFieldsMatrix.getFinalM();
		this.chosenClasses = mFieldsMatrix.getFinalC();
		if(this.chosenClasses.size()>1) {
			getAllMethods();
			compileMethodlMatrix();
			int[] l2 = mMethodsMatrix.getSugestion();
			mMethodsMatrix.choose(l2);
			this.final_methods = mMethodsMatrix.getFinalM();

		}
	}

	public List<StructuredClass> getClasses() {
		return classes;
	}

	public List<FieldDeclaration> getFinalFields() {
		return final_fields;
	}

	public List<MethodDeclaration> getFinalMethods() {
		return final_methods;
	}
}