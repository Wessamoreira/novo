package negocio.comuns.administrativo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.administrativo.enumeradores.AvaliacaoAtendimentoEnum;
import negocio.comuns.administrativo.enumeradores.SituacaoAtendimentoEnum;
import negocio.comuns.administrativo.enumeradores.TipoAtendimentoEnum;
import negocio.comuns.administrativo.enumeradores.TipoOrigemOuvidoriaEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;


/**
 * 
 * @author Pedro
 */
public class AtendimentoVO extends SuperVO {

	private Integer codigo;
	private String nome;
	private String CPF;
	private String telefone;
	private String email;
	private String matriculaAluno;
	private PessoaVO pessoaVO;
	private TipagemOuvidoriaVO tipagemOuvidoriaVO;	
	private AvaliacaoAtendimentoEnum avaliacaoAtendimentoEnum;
	private Date dataAvaliacao;
	private String motivoAvaliacaoRuim;
	private TipoAtendimentoEnum tipoAtendimentoEnum;
	private TipoOrigemOuvidoriaEnum tipoOrigemOuvidoriaEnum;
	private SituacaoAtendimentoEnum situacaoAtendimentoEnum;
	private String assunto;
	private String descricao;
	private Date dataRegistro;
	private FuncionarioVO responsavelAtendimento;
	private Date dataFechamento;
	private UsuarioVO responsavelCadastro;
	private UsuarioVO responsavelFechamento;
	private List<ArquivoVO> listaAnexos;
	private List<AtendimentoInteracaoDepartamentoVO> listaAtendimentoInteracaoDepartamentoVOs;
	private List<AtendimentoInteracaoSolicitanteVO> listaAtendimentoInteracaoSolicitanteVO;
	private Integer unidadeEnsino;
	private Boolean atendimentoJaVisualizado;
	private Boolean atendimentoAtrasado;

	// Campos nao persistido na base de dados somente para controle de tela.
	private String username;
	private String senha;
	private String urlAplicacao;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		if (nome == null) {
			nome = "";
		}
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCPF() {
		if (CPF == null) {
			CPF = "";
		}
		return CPF;
	}

	public void setCPF(String cPF) {
		CPF = cPF;
	}

	public String getTelefone() {
		if (telefone == null) {
			telefone = "";
		}
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getEmail() {
		if (email == null) {
			email = "";
		}
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMatriculaAluno() {
		if (matriculaAluno == null) {
			matriculaAluno = "";
		}
		return matriculaAluno;
	}

	public void setMatriculaAluno(String matriculaAluno) {
		this.matriculaAluno = matriculaAluno;
	}

	public PessoaVO getPessoaVO() {
		if (pessoaVO == null) {
			pessoaVO = new PessoaVO();
		}
		return pessoaVO;
	}

	public void setPessoaVO(PessoaVO pessoaVO) {
		this.pessoaVO = pessoaVO;
	}

	public TipagemOuvidoriaVO getTipagemOuvidoriaVO() {
		if (tipagemOuvidoriaVO == null) {
			tipagemOuvidoriaVO = new TipagemOuvidoriaVO();
		}
		return tipagemOuvidoriaVO;
	}

	public void setTipagemOuvidoriaVO(TipagemOuvidoriaVO tipagemOuvidoriaVO) {
		this.tipagemOuvidoriaVO = tipagemOuvidoriaVO;
	}

	public TipoAtendimentoEnum getTipoAtendimentoEnum() {
		if (tipoAtendimentoEnum == null) {
			tipoAtendimentoEnum = TipoAtendimentoEnum.OUVIDORIA;
		}
		return tipoAtendimentoEnum;
	}

	public void setTipoAtendimentoEnum(TipoAtendimentoEnum tipoAtendimentoEnum) {
		this.tipoAtendimentoEnum = tipoAtendimentoEnum;
	}

	public TipoOrigemOuvidoriaEnum getTipoOrigemOuvidoriaEnum() {
		if (tipoOrigemOuvidoriaEnum == null) {
			tipoOrigemOuvidoriaEnum = TipoOrigemOuvidoriaEnum.SISTEMA;
		}
		return tipoOrigemOuvidoriaEnum;
	}

	public void setTipoOrigemOuvidoriaEnum(TipoOrigemOuvidoriaEnum tipoOrigemOuvidoriaEnum) {
		this.tipoOrigemOuvidoriaEnum = tipoOrigemOuvidoriaEnum;
	}

	public SituacaoAtendimentoEnum getSituacaoAtendimentoEnum() {
		if (situacaoAtendimentoEnum == null) {
			situacaoAtendimentoEnum = SituacaoAtendimentoEnum.EM_ANALISE_OUVIDOR;
		}
		return situacaoAtendimentoEnum;
	}

	public void setSituacaoAtendimentoEnum(SituacaoAtendimentoEnum situacaoOuvidoriaEnum) {
		this.situacaoAtendimentoEnum = situacaoOuvidoriaEnum;
	}

	public String getAssunto() {
		if (assunto == null) {
			assunto = "";
		}
		return assunto;
	}

	public void setAssunto(String assunto) {
		this.assunto = assunto;
	}

	public String getDescricao() {
		if (descricao == null) {
			descricao = "";
		}
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getDataRegistroApresentar() {
		return Uteis.getDataComHora(getDataRegistro());
	}

	public Date getDataRegistro() {
		if (dataRegistro == null) {
			dataRegistro = new Date();
		}
		return dataRegistro;
	}

	public void setDataRegistro(Date dataRegistro) {
		this.dataRegistro = dataRegistro;
	}

	public FuncionarioVO getResponsavelAtendimento() {
		if (responsavelAtendimento == null) {
			responsavelAtendimento = new FuncionarioVO();
		}
		return responsavelAtendimento;
	}

	public void setResponsavelAtendimento(FuncionarioVO responsavelAtendimento) {
		this.responsavelAtendimento = responsavelAtendimento;
	}

	public UsuarioVO getResponsavelCadastro() {
		if (responsavelCadastro == null) {
			responsavelCadastro = new UsuarioVO();
		}
		return responsavelCadastro;
	}

	public void setResponsavelCadastro(UsuarioVO responsavelCadastro) {
		this.responsavelCadastro = responsavelCadastro;
	}

	public String getDataFechamentoApresentar() {
		return Uteis.getDataComHora(getDataFechamento());
	}

	public Date getDataFechamento() {
		return dataFechamento;
	}

	public void setDataFechamento(Date dataFechamento) {
		this.dataFechamento = dataFechamento;
	}

	public UsuarioVO getResponsavelFechamento() {
		if (responsavelFechamento == null) {
			responsavelFechamento = new UsuarioVO();
		}
		return responsavelFechamento;
	}

	public void setResponsavelFechamento(UsuarioVO responsavelFechamento) {
		this.responsavelFechamento = responsavelFechamento;
	}

	public AvaliacaoAtendimentoEnum getAvaliacaoAtendimentoEnum() {
		if(avaliacaoAtendimentoEnum == null){
			avaliacaoAtendimentoEnum = AvaliacaoAtendimentoEnum.NENHUM;
		}
		return avaliacaoAtendimentoEnum;
	}

	public void setAvaliacaoAtendimentoEnum(AvaliacaoAtendimentoEnum avaliacaoAtendimentoEnum) {
		this.avaliacaoAtendimentoEnum = avaliacaoAtendimentoEnum;
	}
	
	

	public Date getDataAvaliacao() {
		return dataAvaliacao;
	}

	public void setDataAvaliacao(Date dataAvaliacao) {
		this.dataAvaliacao = dataAvaliacao;
	}
	
	

	public String getMotivoAvaliacaoRuim() {
		if(motivoAvaliacaoRuim == null){
			motivoAvaliacaoRuim = "";
		}
		return motivoAvaliacaoRuim;
	}

	public void setMotivoAvaliacaoRuim(String motivoAvaliacaoRuim) {
		this.motivoAvaliacaoRuim = motivoAvaliacaoRuim;
	}

	public List<ArquivoVO> getListaAnexos() {
		if (listaAnexos == null) {
			listaAnexos = new ArrayList<ArquivoVO>();
		}
		return listaAnexos;
	}

	public void setListaAnexos(List<ArquivoVO> listaAnexos) {
		this.listaAnexos = listaAnexos;
	}

	public List<AtendimentoInteracaoDepartamentoVO> getListaAtendimentoInteracaoDepartamentoVOs() {
		if (listaAtendimentoInteracaoDepartamentoVOs == null) {
			listaAtendimentoInteracaoDepartamentoVOs = new ArrayList<AtendimentoInteracaoDepartamentoVO>();
		}
		return listaAtendimentoInteracaoDepartamentoVOs;
	}

	public void setListaAtendimentoInteracaoDepartamentoVOs(List<AtendimentoInteracaoDepartamentoVO> listaOuvidoriaInteracaoDepartamentoVOs) {
		this.listaAtendimentoInteracaoDepartamentoVOs = listaOuvidoriaInteracaoDepartamentoVOs;
	}

	public List<AtendimentoInteracaoSolicitanteVO> getListaAtendimentoInteracaoSolicitanteVO() {
		if (listaAtendimentoInteracaoSolicitanteVO == null) {
			listaAtendimentoInteracaoSolicitanteVO = new ArrayList<AtendimentoInteracaoSolicitanteVO>();
		}
		return listaAtendimentoInteracaoSolicitanteVO;
	}

	public void setListaAtendimentoInteracaoSolicitanteVO(List<AtendimentoInteracaoSolicitanteVO> listaOuvidoriaInteracaoSolicitanteVO) {
		this.listaAtendimentoInteracaoSolicitanteVO = listaOuvidoriaInteracaoSolicitanteVO;
	}

	public Integer getUnidadeEnsino() {
		if (unidadeEnsino == null) {
			unidadeEnsino = 0;
		}
		return unidadeEnsino;
	}

	public void setUnidadeEnsino(Integer unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}

	public Boolean getExistePessoa() {
		if (getPessoaVO().getCodigo().intValue() != 0) {
			return true;
		}
		return false;
	}

	public String getTitulo_Apresentar_Atendimento() {
		if (getIsTipoAtendimentoOuvidoria()) {
			return "Ouvidoria";
		} else if (getIsTipoAtendimentoFaleConosco()) {
			return "Fale Conosco";
		} else {
			return "Suporte Aluno";
		}
	}

	public String getIdEntidade() {
		if (getIsTipoAtendimentoOuvidoria()) {
			return "Ouvidoria";
		} else if (getIsTipoAtendimentoFaleConosco()) {
			return "FaleConosco";
		} else {
			return "SuporteAluno";
		}
	}
	
	

	public Boolean getIsTipoAtendimentoOuvidoria() {
		if (getTipoAtendimentoEnum().equals(TipoAtendimentoEnum.OUVIDORIA)) {
			return true;
		}
		return false;
	}

	public Boolean getIsTipoAtendimentoSuporteAluno() {
		if (getTipoAtendimentoEnum().equals(TipoAtendimentoEnum.SUPORTE_ALUNO)) {
			return true;
		}
		return false;
	}

	public Boolean getIsTipoAtendimentoFaleConosco() {
		if (getTipoAtendimentoEnum().equals(TipoAtendimentoEnum.FALE_CONOSCO)) {
			return true;
		}
		return false;
	}

	public Boolean getIsFinalizada() {
		if (getSituacaoAtendimentoEnum().equals(SituacaoAtendimentoEnum.FINALIZADA)) {
			return true;
		}
		return false;
	}

	public Boolean getIsAguradandoInformacaoSolicitante() {
		if (getSituacaoAtendimentoEnum().equals(SituacaoAtendimentoEnum.AGURADANDO_INFORMACAO_SOLICITANTE)) {
			return true;
		}
		return false;
	}

	public Boolean getIsEmAnaliseOuvidor() {
		if (getSituacaoAtendimentoEnum().equals(SituacaoAtendimentoEnum.EM_ANALISE_OUVIDOR)) {
			return true;
		}
		return false;
	}

	public Boolean getIsEmProcessamento() {
		if (getSituacaoAtendimentoEnum().equals(SituacaoAtendimentoEnum.EM_PROCESSAMENTO)) {
			return true;
		}
		return false;
	}
	
	public Boolean getIsAvaliacaoAtendimento_NENHUM() {
		if (getAvaliacaoAtendimentoEnum().equals(AvaliacaoAtendimentoEnum.NENHUM)) {
			return true;
		}
		return false;
	}
	
	public Boolean getIsAvaliacaoAtendimento_REGULAR() {
		if (getAvaliacaoAtendimentoEnum().equals(AvaliacaoAtendimentoEnum.REGULAR)) {
			return true;
		}
		return false;
	}
	public Boolean getIsAvaliacaoAtendimento_RUIM() {
		if (getAvaliacaoAtendimentoEnum().equals(AvaliacaoAtendimentoEnum.RUIM)) {
			return true;
		}
		return false;
	}

	public Integer getNumeroLinhaInteracaoSolicitante() {
		if (getListaAtendimentoInteracaoSolicitanteVO().size() > 10) {
			return 10;
		}
		return getListaAtendimentoInteracaoSolicitanteVO().size();
	}

	public Integer getNumeroColunaInteracaoSolicitante() {
		return 1;
	}

	public AtendimentoInteracaoSolicitanteVO getObterUltimoAtendimentoInteracaoSolicitanteVO() {
		if (!getListaAtendimentoInteracaoSolicitanteVO().isEmpty()) {
			return (AtendimentoInteracaoSolicitanteVO) getListaAtendimentoInteracaoSolicitanteVO().get(getListaAtendimentoInteracaoSolicitanteVO().size() - 1);
		}
		return new AtendimentoInteracaoSolicitanteVO();
	}

	public AtendimentoInteracaoDepartamentoVO getObterUltimoAtendimentoInteracaoDepartamentoVO() {
		if (!getListaAtendimentoInteracaoDepartamentoVOs().isEmpty()) {
			return (AtendimentoInteracaoDepartamentoVO) getListaAtendimentoInteracaoDepartamentoVOs().get(getListaAtendimentoInteracaoDepartamentoVOs().size() - 1);
		}
		return new AtendimentoInteracaoDepartamentoVO();
	}

	public String getUsername() {
		if (username == null) {
			username = "";
		}
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSenha() {
		if (senha == null) {
			senha = "";
		}
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public Boolean getAtendimentoJaVisualizado() {
		if (atendimentoJaVisualizado == null) {
			atendimentoJaVisualizado = false;
		}
		return atendimentoJaVisualizado;
	}

	public void setAtendimentoJaVisualizado(Boolean atendimentoJaVisualizado) {
		this.atendimentoJaVisualizado = atendimentoJaVisualizado;
	}
	
	

	public Boolean getAtendimentoAtrasado() {
		if (atendimentoAtrasado == null) {
			atendimentoAtrasado = false;
		}
		return atendimentoAtrasado;
	}

	public void setAtendimentoAtrasado(Boolean atendimentoAtrasado) {
		this.atendimentoAtrasado = atendimentoAtrasado;
	}

	public String getUrlAplicacao() {
		if (urlAplicacao == null) {
			urlAplicacao = "";
		}
		return urlAplicacao;
	}

	public void setUrlAplicacao(String urlAplicacao) {
		this.urlAplicacao = urlAplicacao;
	}

	public String getCss_LeituraOrObrigatorio() {
		if (getNovoObj()) {
			return "form-control camposObrigatorios";
		}
		return "form-control camposSomenteLeitura";
	}

	public String getCss_LeituraOrNormal() {
		if (getNovoObj()) {
			return "form-control campos";
		}
		return "form-control camposSomenteLeitura";

	}
	
	public String getAvaliacao_1_Estrela_Apresentacao() {		
		StringBuilder sb = new StringBuilder();
		sb.append(" <a href=\"").append(UteisJSF.getAcessoAplicadoURL().substring(0, UteisJSF.getAcessoAplicadoURL().lastIndexOf("/")) + "/AvaliarAtendimentoServlet?aval=1&aten=").append(getCodigo()).append("\" target=\"_blank\"> <img src=\"resources/imagens/atendimento/star1.png\" border=0/></a>  ");
		return sb.toString();
	}
	public String getAvaliacao_2_Estrela_Apresentacao() {		
		StringBuilder sb = new StringBuilder();
		sb.append(" <a href=\"").append(UteisJSF.getAcessoAplicadoURL().substring(0, UteisJSF.getAcessoAplicadoURL().lastIndexOf("/")) + "/AvaliarAtendimentoServlet?aval=2&aten=").append(getCodigo()).append("\" target=\"_blank\"> <img src=\"resources/imagens/atendimento/star2.png\" border=0/></a>  ");
		return sb.toString();
	}
	public String getAvaliacao_3_Estrela_Apresentacao() {		
		StringBuilder sb = new StringBuilder();
		sb.append(" <a href=\"").append(UteisJSF.getAcessoAplicadoURL().substring(0, UteisJSF.getAcessoAplicadoURL().lastIndexOf("/")) + "/AvaliarAtendimentoServlet?aval=3&aten=").append(getCodigo()).append("\" target=\"_blank\"> <img src=\"resources/imagens/atendimento/star3.png\" border=0/></a>  ");
		return sb.toString();
	}
	public String getAvaliacao_4_Estrela_Apresentacao() {		
		StringBuilder sb = new StringBuilder();
		sb.append(" <a href=\"").append(UteisJSF.getAcessoAplicadoURL().substring(0, UteisJSF.getAcessoAplicadoURL().lastIndexOf("/")) + "/AvaliarAtendimentoServlet?aval=4&aten=").append(getCodigo()).append("\" target=\"_blank\"> <img src=\"resources/imagens/atendimento/star4.png\" border=0/></a>  ");
		return sb.toString();
	}
	public String getAcesso_Url_Apresentacao() {		
		StringBuilder sb = new StringBuilder();
		sb.append("<a href=\"").append(UteisJSF.getAcessoAplicadoURL().substring(0, UteisJSF.getAcessoAplicadoURL().lastIndexOf("/")) + "/index.xhtml").append("\"> CLIQUE AQUI </a>");
		return sb.toString();
	}

	public String executarGeracaoMensagemAcessoUrlAplicacao() {
		StringBuilder sb = new StringBuilder();
		sb.append("\r <br />");
		sb.append("\r <br />");
		sb.append("---------------------------------------------------------------------------");
		sb.append("\r <br />");
		sb.append("\r <br />");
		sb.append("Prezado usuário(a) ").append(getNome()).append(", \r <br />");
		sb.append("\r <br />");
		sb.append("\r <br />");
		sb.append("SEGUE SEU USERNAME:").append(getUsername()).append(" E SUA SENHA:").append(getSenha()).append(" \r <br />");
		sb.append("PARA ACESSAR O SISTEMA ").append(getAcesso_Url_Apresentacao());
		sb.append("\r <br />");
		sb.append("\r <br />");
		sb.append("---------------------------------------------------------------------------");
		return sb.toString();
	}
	
	public Boolean getApresentar_BotaoAvaliacao(){
		if(getIsFinalizada() &&  getIsAvaliacaoAtendimento_NENHUM() ){
			return true;
		}
		return false;
	}
	
	

//	public String executarGeracaoMensagemFinalizacaoAtendimento() throws Exception {
		// StringBuilder tituloEmail = new
		// StringBuilder("Solicitação para Finalização do Chamado (").append(chamado.getCodigo()).append(")");
//		StringBuilder corpoMensagem = new StringBuilder("");
//		String urlAcesso = UteisJSF.getAcessoAplicadoURL().substring(0, UteisJSF.getAcessoAplicadoURL().lastIndexOf("/"));
//		corpoMensagem.append("<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" width=\"626\"> ");
//		corpoMensagem.append("<tbody><tr><td><span style=\"font-size: 11px;\"><span style=\"font-family: arial,helvetica,sans-serif;\"><img id=\"_x0000_i1025\" src=\"imagens/email/cima_sei.jpg\" border=\"0\" alt=\"\" width=\"626\" height=\"60\" /></span></span></td></tr> ");
//		corpoMensagem.append("<tr><td><div style=\"margin-left: 10px;\"><span style=\"font-size: 11px;\"><span style=\"font-family: arial,helvetica,sans-serif;\"><br /></span></span></div> ");
//		corpoMensagem.append("<div style=\"margin-left: 10px;\"><span style=\"font-size: 11px;\"><span style=\"font-family: arial,helvetica,sans-serif;\">Prezado NOME_PESSOA ,</span></span></div> ");
//		corpoMensagem.append("<div style=\"margin-left: 10px;\"><span style=\"font-size: 11px;\"><span style=\"font-family: arial,helvetica,sans-serif;\"><br /></span></span></div> ");
//		corpoMensagem.append("<div style=\"margin-left: 10px;\"><span style=\"font-size: 11;\"><span style=\"font-family: arial,helvetica,sans-serif;\">&nbsp;&nbsp;&nbsp;&nbsp;O <b>Atendimento</b> foi concluído, solicito por gentileza que avalie o mesmo.</span></span></div> ");
//		corpoMensagem.append("<div style=\"margin-left: 10px;\"><span style=\"font-size: 11px;\"><span style=\"font-family: arial,helvetica,sans-serif;\"><br /></span></span></div> ");
//		corpoMensagem.append("<table style=\"margin-left:20px; border-collapse:collapse; border: 1px solid #C0C0C0; \" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"> ");
//		corpoMensagem.append("<tbody><tr><td style=\"width: 20px; padding: 2.75pt; border: 1px solid #c0c0c0;\" width=\"77\" valign=\"top\"> ");
//		corpoMensagem.append("<div><span style=\"font-size: 11px;\"><span style=\"font-family: arial,helvetica,sans-serif;\">Atendimento:</span></span></div></td> ");
//		corpoMensagem.append("<td style=\"width: 346.2pt; padding: 2.75pt; border: 1px solid #c0c0c0;\" width=\"462\" valign=\"top\"> ");
//		corpoMensagem.append("<div><span style=\"font-size: 11px;\"><span style=\"font-family: arial,helvetica,sans-serif;\"> CODIGO_ATENDIMENTO  </span></span></div></td> ");
//		corpoMensagem.append("<td style=\"width: 100pt; padding: 2.75pt; border: 1px solid #c0c0c0;\" width=\"100\" valign=\"top\"> ");
//		corpoMensagem.append("<div><span style=\"font-size: 11px;\"><span style=\"font-family: arial,helvetica,sans-serif;\">Data Abertura:</span></span></div></td> ");
//		corpoMensagem.append("<td style=\"width: 200.2pt; padding: 2.75pt; border: 1px solid #c0c0c0;\" width=\"200\" valign=\"top\"> ");
//		corpoMensagem.append("<div><span style=\"font-size: 11px;\"><span style=\"font-family: arial,helvetica,sans-serif;\"> DATA_ABERTURA_ATENDIMENTO </span></span></div></td></tr> ");
//		corpoMensagem.append("<tr><td style=\"width: 57.6pt; padding: 2.75pt; border: 1px solid #c0c0c0;\" width=\"77\"  valign=\"top\"> ");
//		corpoMensagem.append("<div><span style=\"font-size: 11px;\"><span style=\"font-family: arial,helvetica,sans-serif;\">Assunto:</span></span></div></td> ");
//		corpoMensagem.append("<td colspan=3 style=\"width: 346.2pt; border: 1px solid #c0c0c0; padding: 2.75pt;\" width=\"462\" valign=\"top\"> ");
//		corpoMensagem.append("<div><span style=\"font-size: 11px;\"><span style=\"font-family: arial,helvetica,sans-serif;\"> ASSUNTO_ATENDIMENTO </span></span></div></td></tr></tbody></table> ");
//		corpoMensagem.append("<div style=\"margin-left: 10px;\"><span style=\"font-size: 11px;\"><span style=\"font-family: arial,helvetica,sans-serif;\">&nbsp;</span></span></div> ");
//		corpoMensagem.append("<div style=\"margin-left: 10px;\"><span style=\"font-size: 11px;\"><span style=\"font-family: arial,helvetica,sans-serif;\"><em>&nbsp;&nbsp;&nbsp;&nbsp;Clique em um dos botões abaixo, atribuindo seu grau de satisfação deste atendimento.</em><span style=\"color: #b3b3b3;\">&nbsp;</span></span></span></div> ");
//		corpoMensagem.append("<div style=\"margin-left: 10px;\"><span style=\"font-size: 11px;\"><span style=\"font-family: arial,helvetica,sans-serif;\">&nbsp;</span></span></div> ");
//
//		corpoMensagem.append("<table style=\"margin-left:5px; border-collapse:collapse; \" border=\"0\" cellspacing=\"2\" cellpadding=\"4\">");
//		
//		corpoMensagem.append("<tr> <td> AVALIACAO_4ESTRELA_ATENDIMENTO </td> ");
//		corpoMensagem.append("<tr> <td> <a href=\"").append(urlAcesso + "/AvaliarChamadoServlet?aval=4&aten=").append(getCodigo()).append("\" target=\"_blank\"> <img src=\"http://www.otimize-ti.com.br/chamado/btnOtimo.png\" border=0/></a></td> ");		
//		corpoMensagem.append("<td><b>ÓTIMO</b> - O atendimento superou minhas expectativas.</em><span style=\"color: #b3b3b3;\">&nbsp;</span></span></span></div></td></tr> ");
//		
//		
//		corpoMensagem.append("<tr><td > AVALIACAO_3ESTRELA_ATENDIMENTO </td> ");
//		corpoMensagem.append("<tr><td ><a href=\"").append(urlAcesso + "/AvaliarChamadoServlet?aval=3&aten=").append(getCodigo()).append("\" target=\"_blank\">  <img src=\"http://www.otimize-ti.com.br/chamado/btnBom.png\" border=0/></a></td> ");
//		corpoMensagem.append("<td><b>BOM</b> - O problema foi resolvido e minhas expectativas foram atendidas.</em><span style=\"color: #b3b3b3;\">&nbsp;</span></span></span></div></td></tr> ");
//		
//		
//		corpoMensagem.append("<tr><td> AVALIACAO_2ESTRELA_ATENDIMENTO </td> ");
		//corpoMensagem.append("<tr><td><a href=\"").append(urlAcesso + "/AvaliarChamadoServlet?aval=2&aten=").append(getCodigo()).append("\" target=\"_blank\"> <img src=\"http://www.otimize-ti.com.br/chamado/btnRegular.png\" border=0/></a></td> ");
//		corpoMensagem.append("<td><b>REGULAR</b> - O problema foi resolvido, mas minhas expectativas não foram atendidas.</em><span style=\"color: #b3b3b3;\">&nbsp;</span></span></span></div></td></tr> ");
//		
//		corpoMensagem.append("<tr><td> AVALIACAO_1ESTRELA_ATENDIMENTO </td> ");
		//corpoMensagem.append("<tr><td><a href=\"").append(urlAcesso + "/AvaliarChamadoServlet?aval=1&aten=").append(getCodigo()).append("\" target=\"_blank\"> <img src=\"http://www.otimize-ti.com.br/chamado/btnRuim.png\" border=0/></a></td> ");
//		corpoMensagem.append("<td><b>RUIM</b> - Meu problema não foi completamente resolvido. Minhas expectativas não foram atendidas em algumas ocasiões.</em><span style=\"color: #b3b3b3;\">&nbsp;</span></span></span></div></td></tr></table> ");

		
		
		//		corpoMensagem.append("<table style=\"margin-left:5px; border-collapse:collapse; \" border=\"0\" cellspacing=\"2\" cellpadding=\"4\">");
//		corpoMensagem.append("<tr><td> <a href=\"").append(urlAcesso + "/AvaliarChamadoServlet?aval=4&aten=").append(getCodigo()).append("\" target=\"_blank\"> ");
//		corpoMensagem.append("<img src=\"http://www.otimize-ti.com.br/chamado/btnOtimo.png\" border=0/></a></td> ");
//		corpoMensagem.append("<td><a style=\"text-decoration:none;color:#000;\" href=\"").append(urlAcesso + "/AvaliarChamadoServlet?aval=4&aten=").append(getCodigo()).append("\" target=\"_blank\"><b>ÓTIMO</b> - O atendimento superou minhas expectativas.</em><span style=\"color: #b3b3b3;\">&nbsp;</span></span></span></div></a></td></tr> ");
//		corpoMensagem.append("<tr><td ><a href=\"").append(urlAcesso + "/AvaliarChamadoServlet?aval=3&aten=").append(getCodigo()).append("\" target=\"_blank\"> ");
//		corpoMensagem.append("<img src=\"http://www.otimize-ti.com.br/chamado/btnBom.png\" border=0/></a></td> ");
//		corpoMensagem.append("<td><a style=\"text-decoration:none;color:#000;\" href=\"").append(urlAcesso + "/AvaliarChamadoServlet?aval=3&aten=").append(getCodigo()).append("\" target=\"_blank\"><b>BOM</b> - O problema foi resolvido e minhas expectativas foram atendidas.</em><span style=\"color: #b3b3b3;\">&nbsp;</span></span></span></div></a></td></tr> ");
//		corpoMensagem.append("<tr><td><a href=\"").append(urlAcesso + "/AvaliarChamadoServlet?aval=2&aten=").append(getCodigo()).append("\" target=\"_blank\"> ");
//		corpoMensagem.append("<img src=\"http://www.otimize-ti.com.br/chamado/btnRegular.png\" border=0/></a></td> ");
//		corpoMensagem.append("<td><a style=\"text-decoration:none;color:#000;\" href=\"").append(urlAcesso + "/AvaliarChamadoServlet?aval=2&aten=").append(getCodigo()).append("\" target=\"_blank\"><b>REGULAR</b> - O problema foi resolvido, mas minhas expectativas não foram atendidas.</em><span style=\"color: #b3b3b3;\">&nbsp;</span></span></span></div></a></td></tr> ");
//		corpoMensagem.append("<tr><td><a href=\"").append(urlAcesso + "/AvaliarChamadoServlet?aval=1&aten=").append(getCodigo()).append("\" target=\"_blank\"> ");
//		corpoMensagem.append("<img src=\"http://www.otimize-ti.com.br/chamado/btnRuim.png\" border=0/></a></td> ");
//		corpoMensagem.append("<td><a style=\"text-decoration:none;color:#000;\" href=\"").append(urlAcesso + "/AvaliarChamadoServlet?aval=1&aten=").append(getCodigo()).append("\" target=\"_blank\"><b>RUIM</b> - Meu problema não foi completamente resolvido. Minhas expectativas não foram atendidas em algumas ocasiões.</em><span style=\"color: #b3b3b3;\">&nbsp;</span></span></span></div></a></td></tr></table> ");

//		corpoMensagem.append("<div style=\"margin-left: 10px;\"><span style=\"font-size: 11px;\"><span style=\"font-family: arial,helvetica,sans-serif;\">&nbsp;</span></span></div> ");
//		corpoMensagem.append("<div style=\"margin-left: 10px;\"><span style=\"font-size: 11px;\"><span style=\"font-family: arial,helvetica,sans-serif;\"><em>&nbsp;&nbsp;&nbsp;&nbsp;Caso o atendimento não esteja completo, você pode interagir por meio do site  <em> ACESSO_APLICACAO </em> </em><span style=\"color: #b3b3b3;\">&nbsp;</span></span></span></div> ");
//		corpoMensagem.append("<div style=\"margin-left: 10px;\"><span style=\"font-size: 11px;\"><span style=\"font-family: arial,helvetica,sans-serif;\">&nbsp;</span></span></div> ");
//		corpoMensagem.append("<div style=\"margin-left: 10px;\"><span style=\"font-size: 11px;\"><span style=\"font-family: arial,helvetica,sans-serif;\"><em>&nbsp;&nbsp;&nbsp;&nbsp;Observa&ccedil;&atilde;o Importante: Este e-mail n&atilde;o pode ser respondido - esta &eacute; uma mensagem autom&aacute;tica.</em><span style=\"color: #b3b3b3;\">&nbsp;</span></span></span></div> ");
//		corpoMensagem.append("<div style=\"margin-left: 10px;\"><span style=\"font-size: 11px;\"><span style=\"font-family: arial,helvetica,sans-serif;\"><br /></span></span></div> ");
//		corpoMensagem.append("<div style=\"margin-left: 10px;\"><span style=\"font-size: 11px;\"><span style=\"font-family: arial,helvetica,sans-serif;\">Atenciosamente,</span></span></div> ");
//		corpoMensagem.append("<div style=\"margin-left: 10px;\"><span style=\"font-size: 11px;\"><span style=\"font-family: arial,helvetica,sans-serif;\">&nbsp;</span></span></div> ");
//		corpoMensagem.append("<div style=\"margin-left: 10px;\"><span style=\"font-size: 11px;\"><span style=\"font-family: arial,helvetica,sans-serif;\">Equipe de Suporte</span></span></div> ");		
//		corpoMensagem.append("<div style=\"margin-left: 10px;\"><span style=\"font-size: 11px;\"><span style=\"font-family: arial,helvetica,sans-serif;\"><br /></span></span></div></td></tr> ");
//		corpoMensagem.append("<tr><td><span style=\"font-size: 11px;\"><span style=\"font-family: arial,helvetica,sans-serif;\"><img id=\"_x0000_i1028\" src=\"imagens/email/baixo_sei.jpg\" alt=\"\" width=\"626\" height=\"18\" /></span></span></td></tr></tbody></table> ");
//		return corpoMensagem.toString();
//	}

}
