<<<<<<< HEAD
package devframework.domain;

import devframework.annotations.JsonReturn;
import devframework.annotations.ServiceClass;
import devframework.annotations.ServiceMethod;

@ServiceClass
public class Agenda {
	
	private int idade = 50;

	@ServiceMethod
	@JsonReturn
	public Agenda getPessoaJson() {
		return this;
	}

	@ServiceMethod
	public int getPessoaPrimitivo() {
		return idade;
	}

	@ServiceMethod
	public String getPessoaComParametro(int a, String b) {
		return "pessoa com parametro int e String";
	}

	@ServiceMethod
	public String getPessoaComParametro(String a, int b) {
		return "pessoa com parametro String e int";
	}
	
	@ServiceMethod(alias="getPessoaComParametroStringLong")
	public String getPessoaComParametro(String a, long b) {
		return "pessoa com parametro String e long anotada com alias";
	}


	@ServiceMethod
	public void getPessoaSemRetorno() {

	}

	public Agenda getPessoaSemAnotacao() {
		return this;
	}

	@ServiceMethod
	public Agenda getPessoa() {
		return this;
	}

=======
package devframework.domain;

import java.util.Date;

import devframework.annotations.JsonReturn;
import devframework.annotations.ServiceClass;
import devframework.annotations.ServiceMethod;

@ServiceClass
public class Agenda {

	private Pessoa pessoa = new Pessoa("Joao", 50, "rua acacias", new Date(System.currentTimeMillis()));

	@ServiceMethod
	@JsonReturn
	public Pessoa getPessoaJson() {
		return pessoa;
	}
	
	@ServiceMethod
	public int getPessoaPrimitivo() {
		return pessoa.getIdade();
	}
	
	@ServiceMethod
	public void getPessoaSemRetorno() {
		
	}
	
	public Pessoa getPessoaSemAnotacao() {
		return pessoa;
	}
	
	@ServiceMethod
	public Pessoa getPessoa() {
		return pessoa;
	}
	
	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}
>>>>>>> 58f2235f0333f63c946c180650e7a7aa794b8e99
}