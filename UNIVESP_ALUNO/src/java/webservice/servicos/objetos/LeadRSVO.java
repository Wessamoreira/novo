package webservice.servicos.objetos;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import com.google.gson.annotations.SerializedName;

@XmlRootElement(name = "leads")
public class LeadRSVO {
	
	private Integer id;
	private String email;
	private String nome;
	private String nomeBatismo;
	private String companhia;
	private String tituloEmprego;
	private String biografia;
	private String urlPublica;
	private String dataCriacao;
	private Boolean oportunidade;
	private Integer numeroDeConversoes;
	private String usuario;
	private PrimeiraConversaoLeadRSVO primeiraConversao;
	private UltimaConversaoLeadRSVO ultimaConversao;
	private CampoPersonalizadoLeadRSVO camposPersonalizados;
	private String site;
	private String telefone;
	private String celular;
	private String cidade;
	private String estado;
	private String tags;
	private String estagioDaLead;
	private String DataMarcadaUltimaOportunidade;
	private String uuid;
	private String pontuacao;
	private String interesse;
	
	@SerializedName("token_rdstation")
	private String tokenRdStation;
	
	private String identificador;
	
	
	@XmlElement(name = "id")
	public Integer getId() {
		if(id == null)
			id = 0;
		
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@XmlElement(name = "email")
	public String getEmail() {
		if(email == null)
			email = "";
		
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	@XmlElement(name = "name")
	public String getNome() {
		if(nome == null)
			nome = "";
		
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	@XmlElement(name = "name")
	public String getNomeBatismo() {
		if(nomeBatismo == null)
			nomeBatismo = "";
		
		return nomeBatismo;
	}
	public void setNomeBatismo(String nomeBatismo) {
		this.nomeBatismo = nomeBatismo;
	}
	
	@XmlElement(name = "company")
	public String getCompanhia() {
		if(companhia == null)
			companhia = "";
		
		return companhia;
	}
	public void setCompanhia(String companhia) {
		this.companhia = companhia;
	}
	
	@XmlElement(name = "job_title")
	public String getTituloEmprego() {
		if(tituloEmprego == null)
			tituloEmprego = "";
		
		return tituloEmprego;
	}
	public void setTituloEmprego(String tituloEmprego) {
		this.tituloEmprego = tituloEmprego;
	}
	
	@XmlElement(name = "bio")
	public String getBiografia() {
		if(biografia == null)
			biografia = "";
		
		return biografia;
	}
	public void setBiografia(String biografia) {
		this.biografia = biografia;
	}
	
	@XmlElement(name = "public_url")
	public String getUrlPublica() {
		if(urlPublica == null)
			urlPublica = "";
		
		return urlPublica;
	}
	public void setUrlPublica(String urlPublica) {
		this.urlPublica = urlPublica;
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
	
	@XmlElement(name = "opportunity")
	public Boolean getOportunidade() {
		if(oportunidade == null)
			oportunidade = false;
		
		return oportunidade;
	}
	public void setOportunidade(Boolean oportunidade) {
		this.oportunidade = oportunidade;
	}
	
	@XmlElement(name = "number_conversions")
	public Integer getNumeroDeConversoes() {
		if(numeroDeConversoes == null)
			numeroDeConversoes = 0;
		
		return numeroDeConversoes;
	}
	public void setNumeroDeConversoes(Integer numeroDeConversoes) {
		this.numeroDeConversoes = numeroDeConversoes;
	}
	
	@XmlElement(name = "user")
	public String getUsuario() {
		if(usuario == null)
			usuario = "";
		
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	@XmlElement(name = "first_conversion")
	public PrimeiraConversaoLeadRSVO getPrimeiraConversao() {
		if(primeiraConversao == null)
			primeiraConversao = new PrimeiraConversaoLeadRSVO();
		
		return primeiraConversao;
	}
	public void setPrimeiraConversao(PrimeiraConversaoLeadRSVO primeiraConversao) {
		this.primeiraConversao = primeiraConversao;
	}
	
	@XmlElement(name = "last_conversion")
	public UltimaConversaoLeadRSVO getUltimaConversao() {
		if(ultimaConversao == null)
			ultimaConversao = new UltimaConversaoLeadRSVO();
		
		return ultimaConversao;
	}
	public void setUltimaConversao(UltimaConversaoLeadRSVO ultimaConversao) {
		this.ultimaConversao = ultimaConversao;
	}
	
	@XmlElement(name = "custom_fields")
	public CampoPersonalizadoLeadRSVO getCamposPersonalizados() {
		if(camposPersonalizados == null)
			camposPersonalizados = new CampoPersonalizadoLeadRSVO();
		
		return camposPersonalizados;
	}
	public void setCamposPersonalizados(CampoPersonalizadoLeadRSVO camposPersonalizados) {
		this.camposPersonalizados = camposPersonalizados;
	}
	
	@XmlElement(name = "website")
	public String getSite() {
		if(site == null)
			site = "";
		
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	
	@XmlElement(name = "personal_phone")
	public String getTelefone() {
		return telefone != null ? telefone : "";
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	
	@XmlElement(name = "mobile_phone")
	public String getCelular() {
		return celular != null ? celular : "";
	}
	public void setCelular(String celular) {
		this.celular = celular;
	}
	
	@XmlElement(name = "city")
	public String getCidade() {
		if(cidade == null)
			cidade = "";
		
		return cidade;
	}
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}
	
	@XmlElement(name = "state")
	public String getEstado() {
		if(estado == null)
			estado = "";
		
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	@XmlElement(name = "tags")
	public String getTags() {
		if(tags == null)
			tags = "";
		
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	
	@XmlElement(name = "lead_stage")
	public String getEstagioDaLead() {
		if(estagioDaLead == null)
			estagioDaLead = "";
		
		return estagioDaLead;
	}
	public void setEstagioDaLead(String estagioDaLead) {
		this.estagioDaLead = estagioDaLead;
	}
	
	@XmlElement(name = "last_marked_opportunity_date")
	public String getDataMarcadaUltimaOportunidade() {
		if(DataMarcadaUltimaOportunidade == null)
			DataMarcadaUltimaOportunidade = "";
		
		return DataMarcadaUltimaOportunidade;
	}
	public void setDataMarcadaUltimaOportunidade(String dataMarcadaUltimaOportunidade) {
		DataMarcadaUltimaOportunidade = dataMarcadaUltimaOportunidade;
	}
	
	@XmlElement(name = "uuid")
	public String getUuid() {
		if(uuid == null)
			uuid = "";
		
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	@XmlElement(name = "fit_score")
	public String getPontuacao() {
		if(pontuacao == null)
			pontuacao = "";
		
		return pontuacao;
	}
	public void setPontuacao(String pontuacao) {
		this.pontuacao = pontuacao;
	}
	
	@XmlElement(name = "interest")
	public String getInteresse() {
		if(interesse == null)
			interesse = "";
		
		return interesse;
	}
	public void setInteresse(String interesse) {
		this.interesse = interesse;
	}
	public String getTokenRdStation() {
		return tokenRdStation;
	}
	public void setTokenRdStation(String tokenRdStation) {
		this.tokenRdStation = tokenRdStation;
	}
	public String getIdentificador() {
		return identificador;
	}
	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}
	
}