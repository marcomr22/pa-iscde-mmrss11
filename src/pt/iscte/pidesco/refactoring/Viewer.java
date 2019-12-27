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
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
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
	private Compare compare = new Compare();
	private List<CodeGeneratorInt> code_gen = new ArrayList<CodeGeneratorInt>();
	
	public  void setSelection(Collection<SourceElement> c) {
		selection = c;
	}
	
	@Override
	public void createContents(Composite viewArea, Map<String, Image> imageMap) {
		viewArea.setLayout(new RowLayout(SWT.VERTICAL));
		BundleContext context = Activator.getContext();
		ServiceReference<ProjectBrowserServices> serviceReference = context.getServiceReference(ProjectBrowserServices.class);
		ProjectBrowserServices projServ = context.getService(serviceReference);

		
		projServ.addListener(new ProjectBrowserListener.Adapter() {
			
			@Override
			public void selectionChanged(Collection<SourceElement> selection) {
				
				Viewer.this.selection = selection;
				
			}
		});
		
		Group g1 = new Group(viewArea, SWT.PUSH);
		
		Button button = new Button(g1, SWT.PUSH);
		button.setText("Identify");
		
		button.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
			
				for (SourceElement sourceElement : selection) {
					Visitor visitor = new Visitor(compare);
					Parser.parse(sourceElement.getFile().getAbsolutePath(), visitor);
				}
				compare.toString();
				compare.compare();
			}
		});
		
		
		Text text = new Text(g1, SWT.PUSH);
		
		Button button3 = new Button(g1, SWT.PUSH);
		button3.setText("Preview");
		
		button3.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				CodeGeneratorInt c = new CodeGen();
				c.inputValues("Superclass", compare.getFinalFields(), compare.getFinalMethods());
				text.setText(c.generateContent());
				g1.pack();
			}
		});
		
		Button button1 = new Button(g1, SWT.PUSH);
		button1.setText("Generate");
		
		button1.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				List a = (List) selection;
				String path = ((SourceElement) a.get(0)).getParent().getFile().getAbsolutePath();
				CodeGeneratorInt c = new CodeGen();
				c.inputValues("Superclass", compare.getFinalFields(), compare.getFinalMethods());
				File file = createFile(path, "SuperClass");
				writeFile(c.generateContent(), file);
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
				File file = new File(path + "." + name + ".java");
				return file;
			}
		});
		g1.pack();
		
		
		
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
		
		Group g = new Group(viewArea, SWT.PUSH);
		g.setLayout(new RowLayout(SWT.VERTICAL));
		for (CodeGeneratorInt cg: code_gen) {
			
			Button button2 = new Button(g, SWT.PUSH);
			button2.setText("Generate1");
			
			Text text1 = new Text(g, SWT.PUSH);
			
			Button button4 = new Button(g, SWT.PUSH);
			button4.setText("Preview");
			
			button4.addListener(SWT.Selection, new Listener() {
				
				@Override
				public void handleEvent(Event event) {
					text1.setText(cg.generateContent());
					g.pack();
				}
			});
			button2.addListener(SWT.Selection, new Listener() {
			
				@Override
				public void handleEvent(Event event) {
					List a = (List) selection;
					String path = ((SourceElement) a.get(0)).getParent().getFile().getAbsolutePath();
					
					cg.inputValues("Superclass", compare.getFinalFields(), compare.getFinalMethods());
					File file = createFile(path, "SuperClass");
					writeFile(cg.generateContent(), file);
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
					File file = new File(path + "." + name + ".java");
					return file;
				}
			});
		}
		
	}
	
}
