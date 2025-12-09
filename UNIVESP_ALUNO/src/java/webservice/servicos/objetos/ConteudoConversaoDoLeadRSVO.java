package webservice.servicos.objetos;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "content")
public class ConteudoConversaoDoLeadRSVO {
	
	private String nome;
	private String email;
	private String telefone;
	private String dataCriacao;
	private String identificador;
	private Integer idCompanhia;
	private Integer id;
	private String unidadeEnsino;
	private String cursoInteresse;
	private String tokenImportado;
	
	@XmlElement(name = "nome")
	public String getNome() {
		if(nome == null)
			nome = "";
		
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	@XmlElement(name = "email_lead")
	public String getEmail() {
		if(email == null)
			email = "";
		
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	@XmlElement(name = "telefone")
	public String getTelefone() {
		if(telefone == null)
			telefone = "";
		return telefone;
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	
	@XmlElement(name = "created_at")
	public String getDataCriacao() {
		if(dataCriacao == null)
			dataCriacao = "";
		
		return dataCriacao;
	}
	public void setDataCriacao(String dataCriacao) {
		this.dataCriacao = dataCriacao;
	}
	
	@XmlElement(name = "identificador")
	public String getIdentificador() {
		if(identificador == null)
			identificador = "";
		
		return identificador;
	}
	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}
	
	@XmlElement(name = "company_id")
	public Integer getIdCompanhia() {
		if(idCompanhia == null)
			idCompanhia = 0; 
		
		return idCompanhia;
	}
	public void setIdCompanhia(Integer idCompanhia) {
		this.idCompanhia = idCompanhia;
	}
	
	@XmlElement(name = "id")
	public Integer getId() {
		if(id == null)
			id = 0;
		
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@XmlElement(name = "unidade_ensino")
	public String getUnidadeEnsino() {
		if(unidadeEnsino == null)
			unidadeEnsino = "";
		
		return unidadeEnsino;
	}
	public void setUnidadeEnsino(String unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}
	
	@XmlElement(name = "curso_interesse")
	public String getCursoInteresse() {
		if(cursoInteresse == null)
			cursoInteresse = "";
		
		return cursoInteresse;
	}
	public void setCursoInteresse(String cursoInteresse) {
		this.cursoInteresse = cursoInteresse;
	}
	
	@XmlElement(name = "import_token")
	public String getTokenImportado() {
		if(tokenImportado == null)
			tokenImportado = "";
		
		return tokenImportado;
	}
	public void setTokenImportado(String tokenImportado) {
		this.tokenImportado = tokenImportado;
	}
	

}