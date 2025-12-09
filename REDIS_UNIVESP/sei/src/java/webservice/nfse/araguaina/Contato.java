package webservice.nfse.araguaina;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Contato")
public class Contato {

	@XStreamAlias("Telefone")
	private String telefone;

	@XStreamAlias("Email")
	private String email;

	public Contato(String telefone, String email) {
		this.telefone = telefone;
		this.email = email;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public static class ContatoBuilder {

		private String telefone;

		private String email;

		public ContatoBuilder withTelefone(String telefone) {
			this.telefone = telefone;
			return this;
		}

		public ContatoBuilder withEmail(String email) {
			this.email = email;
			return this;
		}

		public Contato build() {
			return new Contato(this.telefone, this.email);
		}

	}

}
