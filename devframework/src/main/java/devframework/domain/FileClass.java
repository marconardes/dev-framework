package devframework.domain;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import devframework.annotations.ServiceClass;
import devframework.annotations.ServiceMethod;
import devframework.domain.MethodClass;
import devframework.utils.ClassLoaderUtils;

public class FileClass {
	
	private String className;
	private String packageName;
	private int methodSize;
	private List<MethodClass> methods;
	
	public String getClassName() {
		return className;
	}
	public void setClassName(String name) {
		this.className = name;
	}
	
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String name) {
		this.packageName = name;
	}
	
	public int getMethodSize() {
		return methodSize;
	}
	public void setMethodSize(int size) {
		this.methodSize = size;
	}
	
	public List<MethodClass> getMethods() {
		return methods;
	}
	public void setMethods(List<MethodClass> methods) {
		this.methods = methods;
	}
	public void addMethods(MethodClass method) {
		this.methods.add(method);
	}
	
	public List<MethodClass> listMethods(String filePath) throws Exception {
		
		List<MethodClass> methodsClass = new ArrayList<MethodClass>();
		
		Class<?> clazz = ClassLoaderUtils.getInstance().loadClass(filePath);
		if (clazz.isAnnotationPresent(ServiceClass.class)) {
			for (Method m : clazz.getMethods()) {
				if (m.isAnnotationPresent(ServiceMethod.class)) {
					MethodClass method = new MethodClass();
					
					String[] returnMethod = m.getReturnType().getTypeName().replace(".", " ").toString().split(" ");
		        	String returnName = returnMethod[returnMethod.length-1];
					
					method.setMethodName(returnName + " " + m.getName());
					
					for (Parameter p : m.getParameters()) {
						String namePar = p.getType().getSimpleName() + " " + p.getName();
						method.addParameter(namePar);
					}
					
					methodsClass.add(method);
				}
			}
		}
		return methodsClass;
	}
	
}
