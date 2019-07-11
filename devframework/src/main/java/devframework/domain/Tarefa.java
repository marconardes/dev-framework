package devframework.domain;

import devframework.annotations.ServiceMethod;

@devframework.annotations.ServiceClass
public class Tarefa {
	
	private String nome = "compromissos";

	@ServiceMethod
	public String getNome() {
		return nome;
	}

	@ServiceMethod
	public void setNome(String nome) {
		this.nome = nome;
	}	
}