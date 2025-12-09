package webservice.servicos;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import negocio.comuns.arquitetura.SuperVO;

@XmlRootElement(name = "integracaoMatriculaCRMVO")
public class IntegracaoMatriculaCRMVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2822711427395301539L;
	private Integer codigoUnidade;
	private String nomeUnidade;
	private IntegracaoPessoaVO pessoa;
	private List<IntegracaoDisciplinaMatriculaVO> disciplinaMatriculaVOs;
	private List<IntegracaoPlanoDescontoVO> planoDescontoVOs;
	private Integer codigoTurma;
	private String nomeTurma;
	private Integer codigoConsultor;
	private String nomeConsultor;
	private Integer codigoProcessoMatricula;
	private String descricaoProcessoMatricula;
	private Integer codigoPlanoFinanceiroCurso;
	private String descricaoPlanoFinanceiroCurso;
	private Integer codigoPlanoDesconto;
	private Integer codigoCondicaoPagamentoPlanoFinanceiroCurso;
	private String descricaoCondicaoPagamentoPlanoFinanceiroCurso;
	private Integer codigoContrato;
	private String mensagemErro;
	private String matricula;
	private String tipoMatricula;
	private Boolean permitiMatricula4Modulo;
	private Boolean permitiMatriculaInadipliente;
	private String htmlContratoMatricula;
	private String diretorioBoletoMatricula;
	private String diretorioContratoPdf;
	private String dataMatricula;
	private String dataVencimentoMatricula;
	private String dataBaseGeracaoParcela;
	private Integer usuarioResponsavel;
	private String cpf;
	private Boolean erro;

	/**
	 * Construtor padrão da classe <code>Curso</code>. Cria uma nova instância desta entidade, inicializando automaticamente seus atributos (Classe VO).
	 */
	public IntegracaoMatriculaCRMVO() {
		super();
	}

	@XmlElement(name = "codigoUnidade")
	public Integer getCodigoUnidade() {
		if (codigoUnidade == null) {
			codigoUnidade = 0;
		}
		return codigoUnidade;
	}

	public void setCodigoUnidade(Integer codigoUnidade) {
		this.codigoUnidade = codigoUnidade;
	}

	@XmlElement(name = "nomeUnidade")
	public String getNomeUnidade() {
		if (nomeUnidade == null) {
			nomeUnidade = "";
		}
		return nomeUnidade;
	}

	public void setNomeUnidade(String nomeUnidade) {
		this.nomeUnidade = nomeUnidade;
	}

	@XmlElement(name = "codigoTurma")
	public Integer getCodigoTurma() {
		if (codigoTurma == null) {
			codigoTurma = 0;
		}
		return codigoTurma;
	}

	public void setCodigoTurma(Integer codigoTurma) {
		this.codigoTurma = codigoTurma;
	}

	@XmlElement(name = "nomeTurma")
	public String getNomeTurma() {
		if (nomeTurma == null) {
			nomeTurma = "";
		}
		return nomeTurma;
	}

	public void setNomeTurma(String nomeTurma) {
		this.nomeTurma = nomeTurma;
	}

	@XmlElement(name = "codigoConsultor")
	public Integer getCodigoConsultor() {
		if (codigoConsultor == null) {
			codigoConsultor = 0;
		}
		return codigoConsultor;
	}

	public void setCodigoConsultor(Integer codigoConsultor) {
		this.codigoConsultor = codigoConsultor;
	}

	@XmlElement(name = "nomeConsultor")
	public String getNomeConsultor() {
		if (nomeConsultor == null) {
			nomeConsultor = "";
		}
		return nomeConsultor;
	}

	public void setNomeConsultor(String nomeConsultor) {
		this.nomeConsultor = nomeConsultor;
	}

	@XmlElement(name = "codigoProcessoMatricula")
	public Integer getCodigoProcessoMatricula() {
		if (codigoProcessoMatricula == null) {
			codigoProcessoMatricula = 0;
		}
		return codigoProcessoMatricula;
	}

	public void setCodigoProcessoMatricula(Integer codigoProcessoMatricula) {
		this.codigoProcessoMatricula = codigoProcessoMatricula;
	}

	@XmlElement(name = "descricaoProcessoMatricula")
	public String getDescricaoProcessoMatricula() {
		if (descricaoProcessoMatricula == null) {
			descricaoProcessoMatricula = "";
		}
		return descricaoProcessoMatricula;
	}

	public void setDescricaoProcessoMatricula(String descricaoProcessoMatricula) {
		this.descricaoProcessoMatricula = descricaoProcessoMatricula;
	}

	@XmlElement(name = "codigoPlanoFinanceiroCurso")
	public Integer getCodigoPlanoFinanceiroCurso() {
		if (codigoPlanoFinanceiroCurso == null) {
			codigoPlanoFinanceiroCurso = 0;
		}
		return codigoPlanoFinanceiroCurso;
	}

	public void setCodigoPlanoFinanceiroCurso(Integer codigoPlanoFinanceiroCurso) {
		this.codigoPlanoFinanceiroCurso = codigoPlanoFinanceiroCurso;
	}

	@XmlElement(name = "descricaoPlanoFinanceiroCurso")
	public String getDescricaoPlanoFinanceiroCurso() {
		if (descricaoPlanoFinanceiroCurso == null) {
			descricaoPlanoFinanceiroCurso = "";
		}
		return descricaoPlanoFinanceiroCurso;
	}

	public void setDescricaoPlanoFinanceiroCurso(String descricaoPlanoFinanceiroCurso) {
		this.descricaoPlanoFinanceiroCurso = descricaoPlanoFinanceiroCurso;
	}

	@XmlElement(name = "codigoCondicaoPagamentoPlanoFinanceiroCurso")
	public Integer getCodigoCondicaoPagamentoPlanoFinanceiroCurso() {
		if (codigoCondicaoPagamentoPlanoFinanceiroCurso == null) {
			codigoCondicaoPagamentoPlanoFinanceiroCurso = 0;
		}
		return codigoCondicaoPagamentoPlanoFinanceiroCurso;
	}

	public void setCodigoCondicaoPagamentoPlanoFinanceiroCurso(Integer codigoCondicaoPagamentoPlanoFinanceiroCurso) {
		this.codigoCondicaoPagamentoPlanoFinanceiroCurso = codigoCondicaoPagamentoPlanoFinanceiroCurso;
	}

	@XmlElement(name = "descricaoCondicaoPagamentoPlanoFinanceiroCurso")
	public String getDescricaoCondicaoPagamentoPlanoFinanceiroCurso() {
		if (descricaoCondicaoPagamentoPlanoFinanceiroCurso == null) {
			descricaoCondicaoPagamentoPlanoFinanceiroCurso = "";
		}
		return descricaoCondicaoPagamentoPlanoFinanceiroCurso;
	}

	public void setDescricaoCondicaoPagamentoPlanoFinanceiroCurso(String descricaoCondicaoPagamentoPlanoFinanceiroCurso) {
		this.descricaoCondicaoPagamentoPlanoFinanceiroCurso = descricaoCondicaoPagamentoPlanoFinanceiroCurso;
	}

	@XmlElement(name = "pessoa")
	public IntegracaoPessoaVO getPessoa() {
		if (pessoa == null) {
			pessoa = new IntegracaoPessoaVO();
		}
		return pessoa;
	}

	public void setPessoa(IntegracaoPessoaVO pessoa) {
		this.pessoa = pessoa;
	}

	@XmlElement(name = "mensagemErro")
	public String getMensagemErro() {
		if (mensagemErro == null) {
			mensagemErro = "";
		}
		return mensagemErro;
	}

	public void setMensagemErro(String mensagemErro) {
		this.mensagemErro = mensagemErro;
	}

	@XmlElement(name = "matricula")
	public String getMatricula() {
		if (matricula == null) {
			matricula = "";
		}
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	@XmlElement(name = "erro")
	public Boolean getErro() {
		if (erro == null) {
			erro = Boolean.FALSE;
		}
		return erro;
	}

	public void setErro(Boolean erro) {
		this.erro = erro;
	}

	@XmlElement(name = "disciplinaMatriculaVOs")
	public List<IntegracaoDisciplinaMatriculaVO> getDisciplinaMatriculaVOs() {
		if (disciplinaMatriculaVOs == null) {
			disciplinaMatriculaVOs = new ArrayList<IntegracaoDisciplinaMatriculaVO>();
		}
		return disciplinaMatriculaVOs;
	}

	public void setDisciplinaMatriculaVOs(List<IntegracaoDisciplinaMatriculaVO> disciplinaMatriculaVOs) {
		this.disciplinaMatriculaVOs = disciplinaMatriculaVOs;
	}

	@XmlElement(name = "tipoMatricula")
	public String getTipoMatricula() {
		if (tipoMatricula == null) {
			tipoMatricula = "NO";
		}
		return tipoMatricula;
	}

	public void setTipoMatricula(String tipoMatricula) {
		this.tipoMatricula = tipoMatricula;
	}

	@XmlElement(name = "codigoPlanoDesconto")
	public Integer getCodigoPlanoDesconto() {
		if (codigoPlanoDesconto == null) {
			codigoPlanoDesconto = 0;
		}
		return codigoPlanoDesconto;
	}

	public void setCodigoPlanoDesconto(Integer codigoPlanoDesconto) {
		this.codigoPlanoDesconto = codigoPlanoDesconto;
	}

	@XmlElement(name = "htmlContratoMatricula")
	public String getHtmlContratoMatricula() {
		if (htmlContratoMatricula == null) {
			htmlContratoMatricula = "";
		}
		return htmlContratoMatricula;
	}

	public void setHtmlContratoMatricula(String htmlContratoMatricula) {
		this.htmlContratoMatricula = htmlContratoMatricula;
	}

	@XmlElement(name = "diretorioBoletoMatricula")
	public String getDiretorioBoletoMatricula() {
		if (diretorioBoletoMatricula == null) {
			diretorioBoletoMatricula = "";
		}
		return diretorioBoletoMatricula;
	}

	public void setDiretorioBoletoMatricula(String diretorioBoletoMatricula) {
		this.diretorioBoletoMatricula = diretorioBoletoMatricula;
	}

	@XmlElement(name = "dataMatricula")
	public String getDataMatricula() {
		if (dataMatricula == null) {
			dataMatricula = "";
		}
		return dataMatricula;
	}

	public void setDataMatricula(String dataMatricula) {
		this.dataMatricula = dataMatricula;
	}	
	@XmlElement(name = "permitiMatricula4Modulo")
	public Boolean getPermitiMatricula4Modulo() {
		if (permitiMatricula4Modulo == null) {
			permitiMatricula4Modulo = Boolean.FALSE;
		}
		return permitiMatricula4Modulo;
	}

	public void setPermitiMatricula4Modulo(Boolean permitiMatricula4Modulo) {
		this.permitiMatricula4Modulo = permitiMatricula4Modulo;
	}

	@XmlElement(name = "diretorioContratoPdf")
	public String getDiretorioContratoPdf() {
		if (diretorioContratoPdf == null) {
			diretorioContratoPdf = "";
		}
		return diretorioContratoPdf;
	}

	public void setDiretorioContratoPdf(String diretorioContratoPdf) {
		this.diretorioContratoPdf = diretorioContratoPdf;
	}

	//Nao inicializar Pois valor nulo e utilizado em regras Pedro Andrade.
	@XmlElement(name = "permitiMatriculaInadipliente")
	public Boolean getPermitiMatriculaInadipliente() {
		return permitiMatriculaInadipliente;
	}

	public void setPermitiMatriculaInadipliente(Boolean permitiMatriculaInadipliente) {
		this.permitiMatriculaInadipliente = permitiMatriculaInadipliente;
	}

	@XmlElement(name = "dataVencimentoMatricula")
	public String getDataVencimentoMatricula() {
		if (dataVencimentoMatricula == null) {
			dataVencimentoMatricula = "";
		}
		return dataVencimentoMatricula;
	}

	public void setDataVencimentoMatricula(String dataVencimentoMatricula) {
		this.dataVencimentoMatricula = dataVencimentoMatricula;
	}

	@XmlElement(name = "dataBaseGeracaoParcela")
	public String getDataBaseGeracaoParcela() {
		if (dataBaseGeracaoParcela == null) {
			dataBaseGeracaoParcela = "";
		}
		return dataBaseGeracaoParcela;
	}

	public void setDataBaseGeracaoParcela(String dataBaseGeracaoParcela) {
		this.dataBaseGeracaoParcela = dataBaseGeracaoParcela;
	}

	@XmlElement(name = "usuarioResponsavel")
	public Integer getUsuarioResponsavel() {
		if (usuarioResponsavel == null) {
			usuarioResponsavel = 0;
		}
		return usuarioResponsavel;
	}

	public void setUsuarioResponsavel(Integer usuarioResponsavel) {
		this.usuarioResponsavel = usuarioResponsavel;
	}

	@XmlElement(name = "planoDescontoVOs")
	public List<IntegracaoPlanoDescontoVO> getPlanoDescontoVOs() {
		if (planoDescontoVOs == null) {
			planoDescontoVOs = new ArrayList<>();
		}
		return planoDescontoVOs;
	}

	public void setPlanoDescontoVOs(List<IntegracaoPlanoDescontoVO> planoDescontoVOs) {
		this.planoDescontoVOs = planoDescontoVOs;
	}

	@XmlElement(name = "cpf")
	public String getCpf() {
		if (cpf == null) {
			cpf = "";
		}
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	@XmlElement(name = "codigoContrato")
	public Integer getCodigoContrato() {
		return codigoContrato;
	}

	public void setCodigoContrato(Integer codigoContrato) {
		this.codigoContrato = codigoContrato;
	}
	
	

}
