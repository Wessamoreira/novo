package webservice.boletoonline.caixaEconomicaFederal.classes;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class SibarVO {
	
	@XStreamAlias("VERSAO")
	private String versao;
	
	@XStreamAlias("AUTENTICACAO")
	private String autenticacao;
	
	@XStreamAlias("USUARIO_SERVICO")
	private String usuario_servico;
	
	@XStreamAlias("OPERACAO")
	private String operacao;
	
	@XStreamAlias("SISTEMA_ORIGEM")
	private String sistema_origem;
	
	@XStreamAlias("UNIDADE")
	private String unidade;
	
	@XStreamAlias("IDENTIFICADOR_ORIGEM")
	private String identificadorOrigem;
	
	@XStreamAlias("DATA_HORA")
	private String data_hora;
	
	public SibarVO() {
		
	}

	public String getVersao() {
		return versao;
	}

	public void setVersao(String versao) {
		this.versao = versao;
	}

	public String getAutenticacao() {
		return autenticacao;
	}

	public void setAutenticacao(String autenticacao) {
		this.autenticacao = autenticacao;
	}

	public String getUsuario_servico() {
		return usuario_servico;
	}

	public void setUsuario_servico(String usuario_servico) {
		this.usuario_servico = usuario_servico;
	}

	public String getOperacao() {
		return operacao;
	}

	public void setOperacao(String operacao) {
		this.operacao = operacao;
	}

	public String getSistema_origem() {
		return sistema_origem;
	}

	public void setSistema_origem(String sistema_origem) {
		this.sistema_origem = sistema_origem;
	}

	public String getUnidade() {
		return unidade;
	}

	public void setUnidade(String unidade) {
		this.unidade = unidade;
	}

	public String getData_hora() {
		return data_hora;
	}

	public void setData_hora(String data_hora) {
		this.data_hora = data_hora;
	}

	public String getIdentificadorOrigem() {
		return identificadorOrigem;
	}

	public void setIdentificadorOrigem(String identificadorOrigem) {
		this.identificadorOrigem = identificadorOrigem;
	}

	

	

}
