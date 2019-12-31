package pt.iscte.pidesco.refactoring;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import pt.iscte.pidesco.extensibility.PidescoView;
import pt.iscte.pidesco.projectbrowser.model.SourceElement;
import pt.iscte.pidesco.projectbrowser.service.ProjectBrowserListener;
import pt.iscte.pidesco.projectbrowser.service.ProjectBrowserServices;
import pt.iscte.pidesco.refactoring.ext.CodeGeneratorInt;

public class Viewer implements PidescoView{

	public Collection<SourceElement> selection = new ArrayList<SourceElement>();
	private Compare compare;
	private List<CodeGeneratorInt> code_gen = new ArrayList<CodeGeneratorInt>();

	public  void setSelection(Collection<SourceElement> c) {
		selection = c;
	}

	@Override
	public void createContents(Composite viewArea, Map<String, Image> imageMap) {
		viewArea.setLayout(new RowLayout(SWT.HORIZONTAL));
		BundleContext context = Activator.getContext();
		ServiceReference<ProjectBrowserServices> serviceReference = context.getServiceReference(ProjectBrowserServices.class);
		ProjectBrowserServices projServ = context.getService(serviceReference);


		projServ.addListener(new ProjectBrowserListener.Adapter() {

			@Override
			public void selectionChanged(Collection<SourceElement> selection) {

				Viewer.this.selection = selection;

			}
		});

		Composite composite = new Composite(viewArea, SWT.EMBEDDED);
		composite.setLayout(new RowLayout(SWT.VERTICAL));

		Button button = new Button(composite, SWT.PUSH);
		button.setText("Identify");

		Label report = new Label(composite, SWT.PUSH);
		report.setText("Report Log: ");

		Text reportLog = new Text(composite, SWT.BORDER | SWT.READ_ONLY);
		reportLog.setLayoutData(new RowData(200,SWT.DEFAULT));

		Label superClass = new Label(composite, SWT.PUSH);
		superClass.setText("Name the Super Class: ");

		Text superClass_name = new Text(composite, SWT.BORDER );
		superClass_name.setLayoutData(new RowData(200,SWT.DEFAULT));

		button.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				if(selection.isEmpty()) {
					reportLog.setText("Please select some classes.");
				}
				else {
					boolean canWork = false;
					compare = new Compare();
					for (SourceElement sourceElement : selection) {
						if(sourceElement.isClass()) {
							Visitor visitor = new Visitor(compare);
							Parser.parse(sourceElement.getFile().getAbsolutePath(), visitor);
							canWork = true;
						}
					}
					if(canWork) {
						compare.toString();
						compare.compare();
						reportLog.setText("");
					}
					else {
						reportLog.setText("Please select some classes.");
					}
				}
			}
		});



		Composite comp = new Composite(viewArea, SWT.EMBEDDED);
		comp.setLayout(new RowLayout(SWT.VERTICAL));

		Label label = new Label(comp, SWT.PUSH);
		label.setText("Default Configuration");

		Button button3 = new Button(comp, SWT.PUSH);
		button3.setText("Preview");

		Text text = new Text(comp, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL | SWT.READ_ONLY);
		text.setLayoutData(new RowData(200,200));

		button3.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				CodeGeneratorInt c = new CodeGen();
				c.inputValues("Superclass", compare.getFinalFields(), compare.getFinalMethods());
				text.setText(c.generateContent());
			}
		});

		Button button1 = new Button(comp, SWT.PUSH);
		button1.setText("Generate");

		button1.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				if(selection.isEmpty()) {
					reportLog.setText("Please select some classes.");
				}
				else {
					List a = (List) selection;
					String name = superClass_name.getText();
					if(name == "") {
						reportLog.setText("Please insert a name for the Super Class.");
					}
					else {

						String path = ((SourceElement) a.get(0)).getParent().getFile().getAbsolutePath()+"/";
						CodeGeneratorInt c = new CodeGen();
						c.inputValues(name, compare.getFinalFields(), compare.getFinalMethods());
						File file = createFile(path, name);
						writeFile(c.generateContent(), file);
					}
				}
			}

			private void writeFile(String s, File file) {
				try {
					FileWriter fw = new FileWriter(file);
					fw.write(s);
					fw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			private File createFile(String path, String name) {
				File file = new File(path + name + ".java");
				return file;
			}
		});



		IExtensionRegistry extRegistry = Platform.getExtensionRegistry();
		IExtensionPoint extensionPoint = extRegistry.getExtensionPoint("pt.iscte.pidesco.refactoring.providers2");
		IExtension[] extensions = extensionPoint.getExtensions();
		for(IExtension e : extensions) {
			IConfigurationElement[] confElements = e.getConfigurationElements();
			for(IConfigurationElement c : confElements) {
				String s = c.getAttribute("name");
				try {
					Object o = c.createExecutableExtension("class");
					CodeGeneratorInt cg = (CodeGeneratorInt) c.createExecutableExtension("class");
					code_gen.add(cg);
				} catch (CoreException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}




		for (CodeGeneratorInt cg: code_gen) {

			Composite comp2 = new Composite(viewArea, SWT.EMBEDDED);
			comp2.setLayout(new RowLayout(SWT.VERTICAL));

			Label label2 = new Label(comp2, SWT.PUSH);
			label2.setText("Nome do Componente");

			Button button4 = new Button(comp2, SWT.PUSH);
			button4.setText("Preview");

			Text text1 = new Text(comp2, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL | SWT.READ_ONLY);
			text1.setLayoutData(new RowData(200,200));

			Button button2 = new Button(comp2, SWT.PUSH);
			button2.setText("Generate1");

			button4.addListener(SWT.Selection, new Listener() {

				@Override
				public void handleEvent(Event event) {
					text1.setText(cg.generateContent());
				}
			});
			button2.addListener(SWT.Selection, new Listener() {

				@Override
				public void handleEvent(Event event) {
					String name = superClass_name.getText();
					if(name == "") {
						reportLog.setText("Please insert a name for the Super Class.");
					}
					else {
						List a = (List) selection;
						String path = ((SourceElement) a.get(0)).getParent().getFile().getAbsolutePath() + "/";
						cg.inputValues(name, compare.getFinalFields(), compare.getFinalMethods());
						File file = createFile(path, name);
						writeFile(cg.generateContent(), file);
					}
				}

				private void writeFile(String s, File file) {
					try {
						FileWriter fw = new FileWriter(file);
						fw.write(s);
						fw.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				private File createFile(String path, String name) {
					File file = new File(path + name + ".java");
					return file;
				}
			});
		}

	}

}
