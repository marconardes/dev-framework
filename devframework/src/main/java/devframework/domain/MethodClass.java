package devframework.domain;

import java.util.ArrayList;
import java.util.List;

public class MethodClass {
	private String methodName;
	private List<String> parameters;
	
	public MethodClass() {
        this.parameters = new ArrayList<String>();
    }
	
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String name) {
		this.methodName = name;
	}
	
	public List<String> getParameters() {
		return parameters;
	}
	public void addParameter(String name) {
		this.parameters.add(name);
	}
	
}
