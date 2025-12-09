package negocio.comuns.processosel;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.ConfiguracaoCandidatoProcessoSeletivoVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;

import negocio.comuns.processosel.enumeradores.SituacaoInscricaoEnum;
import negocio.comuns.utilitarias.Uteis;
import webservice.DateAdapterMobile;
/**
 * Reponsável por manter os dados da entidade Inscricao. Classe do tipo VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */@XmlRootElement(name = "inscricaoVO")
public class InscricaoVO extends SuperVO {

	private Integer codigo;
	private String nrdocumento;
	private Date data;
	private InscricaoVO inscricao;
	// private Date dataProva;
	protected String hora;
	private String situacao;
	private SituacaoInscricaoEnum situacaoInscricao;
	private Date dataLiberacaoPgtoInsc;
//	private DisciplinasProcSeletivoVO opcaoLinguaEstrangeira;
	private String formaAcessoProcSeletivo;
	private Boolean portadorNecessidadeEspecial;
	private String descricaoNecessidadeEspecial;
	private Boolean liberadaForaPrazo;
	private Date dataLiberacaoForaPrazo;
	private Integer chamada;
	// Campos utilizados para integracao com outros modulos de proc. seletivo
	// Sao dados que precisam ser mantidos na inscricao, pois existem no SEI
	// na tabela matricula, mas muitas vezes se tem a inscricao do aluno, mas
	// o mesmo nao está efetivamente matriculado. Assim os dados ficam gravados
	// na inscricao para serem levados para a matricula no ato da mesma.
	// Utilizado na UniRV - 15/11/15. Edigar A. Diniz Jr.
	private Integer classificacao;
	private String codigoFinanceiroMatricula;
	private String descDisciplinasProcSeletivo;
	 private List<ArquivoVO> listaArquivosAnexo;

//	private SalaLocalAulaVO sala;
	/**
	 * Atributo responsável por manter o objeto relacionado da classe <code>UnidadeEnsino </code>.
	 */
	private UnidadeEnsinoVO unidadeEnsino;
	/**
	 * Atributo responsável por manter o objeto relacionado da classe <code>Pessoa </code>.
	 */
	private PessoaVO candidato;
	/**
	 * Atributo responsável por manter o objeto relacionado da classe <code>ProcSeletivo </code>.
	 */
	private ProcSeletivoVO procSeletivo;
	/**
	 * Atributo responsável por manter o objeto relacionado da classe <code>Curso </code>.
	 */
	private UnidadeEnsinoCursoVO cursoOpcao1;
	/**
	 * Atributo responsável por manter o objeto relacionado da classe <code>Curso </code>.
	 */
	private UnidadeEnsinoCursoVO cursoOpcao2;
	/**
	 * Atributo responsável por manter o objeto relacionado da classe <code>Curso </code>.
	 */
	private UnidadeEnsinoCursoVO cursoOpcao3;
	/**
	 * Atributo responsável por manter o objeto relacionado da classe <code>Curso </code>.
	 */
	private UsuarioVO responsavel;
	/**
	 * Atributo responsável por manter o objeto relacionado da classe <code>Pessoa </code>.
	 */
	private UsuarioVO respLiberacaoPgtoInsc;
	/**
	 * Atributo responsável por manter o objeto relacionado da classe <code>Pessoa </code>.
	 */
	private UsuarioVO responsavelLiberacaoForaPrazo;
	private Integer contaReceber;
	private Boolean liberarPagamento;
	private QuestionarioVO questionarioVO;
	private Boolean inscricaoPresencial;
	private Boolean candidatoConvocadoMatricula;
	public static final long serialVersionUID = 1L;
//	private ItemProcSeletivoDataProvaVO itemProcessoSeletivoDataProva;
	private Integer codigoAutenticacao;
	private Date dataAutenticacao;
	private Boolean permitirEmissaoBoleto;
	private Boolean permitirRecebimentoCartaoCreditoOnline;
	private Boolean matriculaRealizada;
	private Timestamp dataHoraInicio;
	private Timestamp dataHoraTermino;
	private Boolean termoFoiAceito;
	private Long tempoRestanteProva;
	private Boolean sobreBolsasEAuxilios;
	private Boolean autodeclaracaoPretoPardoOuIndigena;
	private Boolean escolaPublica;

	/*
	 * Transient
	 */
	private Integer salaAlterar;
	private Integer resultadoProcessoSeletivo;
//    private ContaReceberVO contaReceberVO;
//	private ResultadoProcessoSeletivoVO resultadoProcessoSeletivoVO;
	private Boolean selecionar;
	private MatriculaVO matriculaVO;
	private String formaIngresso;
	private SituacaoInscricaoEnum situacaoInscricaoOrigem;
	private Boolean apresentarResultadoProcessoSeletivo;
	private ConfiguracaoCandidatoProcessoSeletivoVO configuracaoCandidatoProcessoSeletivoVO;
    private Boolean ocultarClassificacao;
	private Boolean  ocultarMedia;
    private Boolean  ocultarChamadaCandidato;
    private Boolean possuiRedacao;
	private String mensagemErro;
	/*
	 * Fim Transient
	 */
//	private GabaritoVO gabaritoVO;
//	private ProvaProcessoSeletivoVO provaProcessoSeletivoVO;
	private ArquivoVO arquivoVO;
	private String formaIngresso_Apresentar;
	private String codigoAutenticacaoNavegador;
	private Boolean permiteAcessarNavegador;
	private String navegadorAcesso;
	private Boolean inscricaoProvenienteImportacao;
	private Timestamp dataHoraVencimentoCodigoAutenticacao;
	private Boolean necessarioInformarCodigoAutenticacao;
	private Boolean possivelAlterarInscricao;
	private Integer cursoAprovado;
	private Boolean alterarNumeroChamada;

	/**
	 * Construtor padrão da classe <code>Inscricao</code>. Cria uma nova instância desta entidade, inicializando automaticamente seus atributos
	 * (Classe VO).
	 */
	public InscricaoVO() {
		super();
		inicializarDados();
	}

	/**
	 * Operação reponsável por inicializar os atributos da classe.
	 */
	public void inicializarDados() {
		setCodigo(0);
		setNrdocumento("");
		setData(new Date());
		// setData(new Date());
		setSituacao("PF");
		setDataLiberacaoPgtoInsc(new Date());
		setFormaAcessoProcSeletivo("VE");
		setPortadorNecessidadeEspecial(Boolean.FALSE);
		setDescricaoNecessidadeEspecial("");
		setLiberadaForaPrazo(Boolean.FALSE);
		setDataLiberacaoForaPrazo(new Date());
		setContaReceber(0);
		setLiberarPagamento(Boolean.FALSE);
//		setOpcaoLinguaEstrangeira(new DisciplinasProcSeletivoVO());
	}

//	public ContaReceberVO criarContaReceber(ConfiguracaoFinanceiroVO obj) {
//		ContaReceberVO contaReceberVO = new ContaReceberVO();
//		contaReceberVO.setCentroReceita(obj.getCentroReceitaInscricaoProcessoSeletivoPadrao());		
//		contaReceberVO.setContaCorrente(obj.getContaCorrentePadraoProcessoSeletivo());
//		contaReceberVO.setData(new Date());
//		contaReceberVO.setValor(getProcSeletivo().getValorInscricao());
//		contaReceberVO.getUnidadeEnsino().setCodigo(getUnidadeEnsino().getCodigo());
//		contaReceberVO.setDataVencimento(getProcSeletivo().getDataFim());
//		contaReceberVO.setCandidato(getCandidato());
//		contaReceberVO.setPessoa(getCandidato());
//		contaReceberVO.setSituacao("AR");
//		contaReceberVO.setTipoPessoa("CA");
//		contaReceberVO.setParcela("1/1");
//		contaReceberVO.setCodOrigem(String.valueOf(getCodigo()));
//		contaReceberVO.setTipoOrigem("IPS");
//		contaReceberVO.setDescricaoPagamento("Taxa de Inscrição do Processo Seletivo (" + getProcSeletivo().getDescricao() + ")");
//		contaReceberVO.setNrDocumento((String.valueOf(getUnidadeEnsino().getCodigo()) + "." + String.valueOf(getProcSeletivo().getCodigo()) + "." + String.valueOf(getCodigo())));
//		return contaReceberVO;
//	}

	public Boolean getIsPossuiQuestionario() {
		if (getQuestionarioVO().getCodigo().intValue() > 0) {
			return true;
		}
		return false;
	}
	
	@XmlElement(name = "questionarioVO")
	public QuestionarioVO getQuestionarioVO() {
		if (questionarioVO == null) {
			questionarioVO = new QuestionarioVO();
		}
		return questionarioVO;
	}

	public void setQuestionarioVO(QuestionarioVO questionarioVO) {
		this.questionarioVO = questionarioVO;
	}

	public Boolean getLiberarPagamento() {
		return liberarPagamento;
	}

	public void setLiberarPagamento(Boolean liberarPagamento) {
		this.liberarPagamento = liberarPagamento;
	}
    
	@XmlElement(name = "codigoContaReceber")
	public Integer getContaReceber() {
		if (contaReceber == null) {
			contaReceber = 0;
		}
		return contaReceber;
	}

	public void setContaReceber(Integer contaReceber) {
		this.contaReceber = contaReceber;
	}

	/**
	 * Retorna o objeto da classe <code>Pessoa</code> relacionado com ( <code>Inscricao</code>).
	 */
	public UsuarioVO getRespLiberacaoPgtoInsc() {
		if (respLiberacaoPgtoInsc == null) {
			respLiberacaoPgtoInsc = new UsuarioVO();
		}
		return (respLiberacaoPgtoInsc);
	}

	/**
	 * Define o objeto da classe <code>Pessoa</code> relacionado com ( <code>Inscricao</code>).
	 */
	public void setRespLiberacaoPgtoInsc(UsuarioVO obj) {
		this.respLiberacaoPgtoInsc = obj;
	}

	/**
	 * Retorna o objeto da classe <code>Pessoa</code> relacionado com ( <code>Inscricao</code>).
	 */
	public UsuarioVO getResponsavel() {
		if (responsavel == null) {
			responsavel = new UsuarioVO();
		}
		return (responsavel);
	}

	/**
	 * Define o objeto da classe <code>Pessoa</code> relacionado com ( <code>Inscricao</code>).
	 */
	public void setResponsavel(UsuarioVO obj) {
		this.responsavel = obj;
	}

	/**
	 * Retorna o objeto da classe <code>Curso</code> relacionado com ( <code>Inscricao</code>).
	 */
	@XmlElement(name = "cursoOpcao3")
	public UnidadeEnsinoCursoVO getCursoOpcao3() {
		if (cursoOpcao3 == null) {
			cursoOpcao3 = new UnidadeEnsinoCursoVO();
		}
		return (cursoOpcao3);
	}

	/**
	 * Define o objeto da classe <code>Curso</code> relacionado com ( <code>Inscricao</code>).
	 */
	public void setCursoOpcao3(UnidadeEnsinoCursoVO obj) {
		this.cursoOpcao3 = obj;
	}

	/**
	 * Retorna o objeto da classe <code>Curso</code> relacionado com ( <code>Inscricao</code>).
	 */
	@XmlElement(name = "cursoOpcao2")
	public UnidadeEnsinoCursoVO getCursoOpcao2() {
		if (cursoOpcao2 == null) {
			cursoOpcao2 = new UnidadeEnsinoCursoVO();
		}
		return (cursoOpcao2);
	}

	/**
	 * Define o objeto da classe <code>Curso</code> relacionado com ( <code>Inscricao</code>).
	 */
	public void setCursoOpcao2(UnidadeEnsinoCursoVO obj) {
		this.cursoOpcao2 = obj;
	}

	/**
	 * Retorna o objeto da classe <code>Curso</code> relacionado com ( <code>Inscricao</code>).
	 */
	@XmlElement(name = "cursoOpcao1")
	public UnidadeEnsinoCursoVO getCursoOpcao1() {
		if (cursoOpcao1 == null) {
			cursoOpcao1 = new UnidadeEnsinoCursoVO();
		}
		return (cursoOpcao1);
	}

	/**
	 * Define o objeto da classe <code>Curso</code> relacionado com ( <code>Inscricao</code>).
	 */
	public void setCursoOpcao1(UnidadeEnsinoCursoVO obj) {
		this.cursoOpcao1 = obj;
	}

	/**
	 * Retorna o objeto da classe <code>ProcSeletivo</code> relacionado com ( <code>Inscricao</code>).
	 */
	@XmlElement(name = "procSeletivo")
	public ProcSeletivoVO getProcSeletivo() {
		if (procSeletivo == null) {
			procSeletivo = new ProcSeletivoVO();
		}
		return (procSeletivo);
	}

	/**
	 * Define o objeto da classe <code>ProcSeletivo</code> relacionado com ( <code>Inscricao</code>).
	 */
	public void setProcSeletivo(ProcSeletivoVO obj) {
		this.procSeletivo = obj;
	}

	/**
	 * Retorna o objeto da classe <code>Pessoa</code> relacionado com ( <code>Inscricao</code>).
	 */
	@XmlElement(name = "candidato")
	public PessoaVO getCandidato() {
		if (candidato == null) {
			candidato = new PessoaVO();
		}
		return (candidato);
	}

	/**
	 * Define o objeto da classe <code>Pessoa</code> relacionado com ( <code>Inscricao</code>).
	 */
	public void setCandidato(PessoaVO obj) {
		this.candidato = obj;
	}

	/**
	 * Retorna o objeto da classe <code>UnidadeEnsino</code> relacionado com ( <code>Inscricao</code>).
	 */
	@XmlElement(name = "unidadeEnsino")
	public UnidadeEnsinoVO getUnidadeEnsino() {
		if (unidadeEnsino == null) {
			unidadeEnsino = new UnidadeEnsinoVO();
		}
		return (unidadeEnsino);
	}

	/**
	 * Define o objeto da classe <code>UnidadeEnsino</code> relacionado com ( <code>Inscricao</code>).
	 */
	public void setUnidadeEnsino(UnidadeEnsinoVO obj) {
		this.unidadeEnsino = obj;
	}

	public Date getDataLiberacaoPgtoInsc() {
		return (dataLiberacaoPgtoInsc);
	}

	/**
	 * Operação responsável por retornar um atributo do tipo data no formato padrão dd/mm/aaaa.
	 */
	public String getDataLiberacaoPgtoInsc_Apresentar() {
		return (Uteis.getData(dataLiberacaoPgtoInsc));
	}

	public void setDataLiberacaoPgtoInsc(Date dataLiberacaoPgtoInsc) {
		this.dataLiberacaoPgtoInsc = dataLiberacaoPgtoInsc;
	}

	@XmlElement(name = "situacao")
	public String getSituacao() {
		return (situacao);
	}

	/**
	 * Operação responsável por retornar o valor de apresentação de um atributo com um domínio específico. Com base no valor de armazenamento do
	 * atributo esta função é capaz de retornar o de apresentação correspondente. Útil para campos como sexo, escolaridade, etc.
	 */
	@XmlElement(name = "situacao_Apresentar")
	public String getSituacao_Apresentar() {
		if (situacao.equals("PF")) {
			return "Pendente Financeiramente";
		}
		if (situacao.equals("CO")) {
			return "Confirmada";
		}
		return (situacao);
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	@XmlElement(name = "data")
	@XmlJavaTypeAdapter(DateAdapterMobile.class)
	public Date getData() {
		return data;
	}

	/**
	 * Operação responsável por retornar um atributo do tipo data no formato padrão dd/mm/aaaa.
	 */
	@XmlElement(name = "data_apresentar")
	public String getData_Apresentar() {

		return (Uteis.getData(data));
	}

	public void setData(Date data) {
		this.data = data;
	}

	@XmlElement(name = "codigo")
	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return (codigo);
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getDescricaoNecessidadeEspecial() {
		return (descricaoNecessidadeEspecial);
	}

	public void setDescricaoNecessidadeEspecial(String descricaoNecessidadeEspecial) {
		this.descricaoNecessidadeEspecial = descricaoNecessidadeEspecial;
	}

	public Boolean getPortadorNecessidadeEspecial() {
		return (portadorNecessidadeEspecial);
	}

	public Boolean isPortadorNecessidadeEspecial() {
		return (portadorNecessidadeEspecial);
	}

	public void setPortadorNecessidadeEspecial(Boolean portadorNecessidadeEspecial) {
		this.portadorNecessidadeEspecial = portadorNecessidadeEspecial;
	}

	public String getFormaAcessoProcSeletivo() {
		return (formaAcessoProcSeletivo);
	}

	/**
	 * Operação responsável por retornar o valor de apresentação de um atributo com um domínio específico. Com base no valor de armazenamento do
	 * atributo esta função é capaz de retornar o de apresentação correspondente. Útil para campos como sexo, escolaridade, etc.
	 */
	public String getFormaAcessoProcSeletivo_Apresentar() {
		if (formaAcessoProcSeletivo.equals("VE")) {
			return "Vestibular";
		}
		if (formaAcessoProcSeletivo.equals("TE")) {
			return "Treini";
		}
		if (formaAcessoProcSeletivo.equals("EN")) {
			return "ENEM";
		}
		if (formaAcessoProcSeletivo.equals("RE")) {
			return "Redação";
		}
		return (formaAcessoProcSeletivo);
	}

	public void setFormaAcessoProcSeletivo(String formaAcessoProcSeletivo) {
		this.formaAcessoProcSeletivo = formaAcessoProcSeletivo;
	}

//	@XmlElement(name = "disciplinasProcSeletivoVO")
//	public DisciplinasProcSeletivoVO getOpcaoLinguaEstrangeira() {
//		if (opcaoLinguaEstrangeira == null) {
//			opcaoLinguaEstrangeira = new DisciplinasProcSeletivoVO();
//		}
//		return (opcaoLinguaEstrangeira);
//	}
//
//	public void setOpcaoLinguaEstrangeira(DisciplinasProcSeletivoVO opcaoLinguaEstrangeira) {
//		this.opcaoLinguaEstrangeira = opcaoLinguaEstrangeira;
//	}
//	

	/**
	 * Retorna o objeto da classe <code>Pessoa</code> relacionado com ( <code>Inscricao</code>).
	 */
	public UsuarioVO getResponsavelLiberacaoForaPrazo() {
		if (responsavelLiberacaoForaPrazo == null) {
			responsavelLiberacaoForaPrazo = new UsuarioVO();
		}
		return (responsavelLiberacaoForaPrazo);
	}

	/**
	 * Define o objeto da classe <code>Pessoa</code> relacionado com ( <code>Inscricao</code>).
	 */
	public void setResponsavelLiberacaoForaPrazo(UsuarioVO obj) {
		this.responsavelLiberacaoForaPrazo = obj;
	}

	public Date getDataLiberacaoForaPrazo() {
		return (dataLiberacaoForaPrazo);
	}

	/**
	 * Operação responsável por retornar um atributo do tipo data no formato padrão dd/mm/aaaa.
	 */
	public String getDataLiberacaoForaPrazo_Apresentar() {
		return (Uteis.getData(dataLiberacaoForaPrazo));
	}

	public void setDataLiberacaoForaPrazo(Date dataLiberacaoForaPrazo) {
		this.dataLiberacaoForaPrazo = dataLiberacaoForaPrazo;
	}

	public Boolean getLiberadaForaPrazo() {
		return (liberadaForaPrazo);
	}

	public Boolean isLiberadaForaPrazo() {
		return (liberadaForaPrazo);
	}

	public void setLiberadaForaPrazo(Boolean liberadaForaPrazo) {
		this.liberadaForaPrazo = liberadaForaPrazo;
	}
	@XmlElement(name = "nrdocumento")
	public String getNrdocumento() {
		return nrdocumento;
	}

	public void setNrdocumento(String nrdocumento) {
		this.nrdocumento = nrdocumento;
	}

	// public String getDataProva_Apresentar() {
	// return (Uteis.getData(dataProva, "dd/MM/yyyy")) + " - " + hora;
	// }
	//
	// /**
	// * @return the dataProva
	// */
	// public Date getDataProva() {
	// return dataProva;
	// }
	//
	// /**
	// * @param dataProva the dataProva to set
	// */
	// public void setDataProva(Date dataProva) {
	// this.dataProva = dataProva;
	// }

	// /**
	// * @return the hora
	// */
	// public String getHora() {
	// if (hora == null) {
	// hora = "00:00";
	// }
	// return hora.trim();
	// }
	//
	// /**
	// * @param hora the hora to set
	// */
	// public void setHora(String hora) {
	// this.hora = hora;
	// }

	/**
	 * @return the inscricaoPresencial
	 */
	public Boolean getInscricaoPresencial() {
		if (inscricaoPresencial == null) {
			inscricaoPresencial = Boolean.TRUE;
		}
		return inscricaoPresencial;
	}

	/**
	 * @param inscricaoPresencial
	 *            the inscricaoPresencial to set
	 */
	public void setInscricaoPresencial(Boolean inscricaoPresencial) {
		this.inscricaoPresencial = inscricaoPresencial;
	}

//	public SalaLocalAulaVO getSala() {
//		if (sala == null) {
//			sala = new SalaLocalAulaVO();
//		}
//		return sala;
//	}
//
//	public void setSala(SalaLocalAulaVO sala) {
//		this.sala = sala;
//	}

	@XmlElement(name = "candidatoConvocadoMatricula")
	public Boolean getCandidatoConvocadoMatricula() {
		if (candidatoConvocadoMatricula == null) {
			candidatoConvocadoMatricula = Boolean.FALSE;
		}
		return candidatoConvocadoMatricula;
	}

	public void setCandidatoConvocadoMatricula(Boolean candidatoConvocadoMatricula) {
		this.candidatoConvocadoMatricula = candidatoConvocadoMatricula;
	}

	public Integer getSalaAlterar() {
		if (salaAlterar == null) {
			salaAlterar = 0;
		}
		return salaAlterar;
	}

	public void setSalaAlterar(Integer salaAlterar) {
		this.salaAlterar = salaAlterar;
	}

	public Integer getResultadoProcessoSeletivo() {
		if (resultadoProcessoSeletivo == null) {
			resultadoProcessoSeletivo = 0;
		}
		return resultadoProcessoSeletivo;
	}

	public void setResultadoProcessoSeletivo(Integer resultadoProcessoSeletivo) {
		this.resultadoProcessoSeletivo = resultadoProcessoSeletivo;
	}

//	@XmlElement(name = "itemProcessoSeletivoDataProva")
//	public ItemProcSeletivoDataProvaVO getItemProcessoSeletivoDataProva() {
//		if (itemProcessoSeletivoDataProva == null) {
//			itemProcessoSeletivoDataProva = new ItemProcSeletivoDataProvaVO();
//		}
//		return itemProcessoSeletivoDataProva;
//	}
//
//	public void setItemProcessoSeletivoDataProva(ItemProcSeletivoDataProvaVO itemProcessoSeletivoDataProva) {
//		this.itemProcessoSeletivoDataProva = itemProcessoSeletivoDataProva;
//	}

	@XmlElement(name = "chamada")
	public Integer getChamada() {
		if (chamada == null) {
			chamada = 0;
		}
		return chamada;
	}

	public void setChamada(Integer chamada) {
		this.chamada = chamada;
	}

//	@XmlElement(name = "resultadoProcessoSeletivoVO")
//	public ResultadoProcessoSeletivoVO getResultadoProcessoSeletivoVO() {
//		if (resultadoProcessoSeletivoVO == null) {
//			resultadoProcessoSeletivoVO = new ResultadoProcessoSeletivoVO();
//		}
//		return resultadoProcessoSeletivoVO;
//	}
//
//	public void setResultadoProcessoSeletivoVO(ResultadoProcessoSeletivoVO resultadoProcessoSeletivoVO) {
//		this.resultadoProcessoSeletivoVO = resultadoProcessoSeletivoVO;
//	}

	@XmlElement(name = "situacaoInscricao")
	public SituacaoInscricaoEnum getSituacaoInscricao() {
		if (situacaoInscricao == null) {
			situacaoInscricao = SituacaoInscricaoEnum.ATIVO;
		}
		return situacaoInscricao;
	}

	@XmlElement(name = "situacaoInscricao_Apresentar")
	public String getSituacaoInscricao_Apresentar() {
		if (situacaoInscricao == null) {
			situacaoInscricao = SituacaoInscricaoEnum.ATIVO;
		}
		return situacaoInscricao.getValorApresentar();
	}

	public void setSituacaoInscricao(SituacaoInscricaoEnum situacaoInscricao) {
		this.situacaoInscricao = situacaoInscricao;
	}

	public Boolean getSelecionar() {
		if (selecionar == null) {
			selecionar = Boolean.FALSE;
		}
		return selecionar;
	}

	public void setSelecionar(Boolean selecionar) {
		this.selecionar = selecionar;
	}

	public boolean getNaoCompareceu() {
		return getSituacaoInscricao().equals(SituacaoInscricaoEnum.NAO_COMPARECEU);
	}

	public MatriculaVO getMatriculaVO() {
		if (matriculaVO == null) {
			matriculaVO = new MatriculaVO();
		}
		return matriculaVO;
	}

	public void setMatriculaVO(MatriculaVO matriculaVO) {
		this.matriculaVO = matriculaVO;
	}

	public boolean getApresentarBoletoInscricao() {
		if (this.getCodigo().intValue() > 0 && this.getContaReceber() > 0 && this.getSituacao().equals("PF")) {
			return true;
		}
		return false;
	}

	/**
	 * @return the classificacao
	 */
	@XmlElement(name = "classificacao")
	public Integer getClassificacao() {
		if (classificacao == null) {
			classificacao = 0;
		}
		return classificacao;
	}

	/**
	 * @param classificacao
	 *            the classificacao to set
	 */
	public void setClassificacao(Integer classificacao) {
		this.classificacao = classificacao;
	}

	/**
	 * @return the codigoFinanceiroMatricula
	 */
	public String getCodigoFinanceiroMatricula() {
		if (codigoFinanceiroMatricula == null) {
			codigoFinanceiroMatricula = "";
		}
		return codigoFinanceiroMatricula;
	}

	/**
	 * @param codigoFinanceiroMatricula
	 *            the codigoFinanceiroMatricula to set
	 */
	public void setCodigoFinanceiroMatricula(String codigoFinanceiroMatricula) {
		this.codigoFinanceiroMatricula = codigoFinanceiroMatricula;
	}

	/**
	 * @return the descDisciplinasProcSeletivo
	 */
	public String getDescDisciplinasProcSeletivo() {
		if (descDisciplinasProcSeletivo == null) {
			descDisciplinasProcSeletivo = "";
		}
		return descDisciplinasProcSeletivo;
	}

	/**
	 * @param descDisciplinasProcSeletivo
	 *            the descDisciplinasProcSeletivo to set
	 */
	public void setDescDisciplinasProcSeletivo(String descDisciplinasProcSeletivo) {
		this.descDisciplinasProcSeletivo = descDisciplinasProcSeletivo;
	}

//	public GabaritoVO getGabaritoVO() {
//		if (gabaritoVO == null) {
//			gabaritoVO = new GabaritoVO();
//		}
//		return gabaritoVO;
//	}
//
//	public void setGabaritoVO(GabaritoVO gabaritoVO) {
//		this.gabaritoVO = gabaritoVO;
//	}
//
//	@XmlElement(name = "provaProcessoSeletivoVO")
//	public ProvaProcessoSeletivoVO getProvaProcessoSeletivoVO() {
//		if (provaProcessoSeletivoVO == null) {
//			provaProcessoSeletivoVO = new ProvaProcessoSeletivoVO();
//		}
//		return provaProcessoSeletivoVO;
//	}
//
//	public void setProvaProcessoSeletivoVO(ProvaProcessoSeletivoVO provaProcessoSeletivoVO) {
//		this.provaProcessoSeletivoVO = provaProcessoSeletivoVO;
//	}
//
//	/**
//	 * @return the contaReceberVO
//	 */
//	@XmlElement(name = "contaReceberVO")
//	public ContaReceberVO getContaReceberVO() {
//		if (contaReceberVO == null) {
//			contaReceberVO = new ContaReceberVO();
//		}
//		return contaReceberVO;
//	}
//
//	/**
//	 * @param contaReceberVO the contaReceberVO to set
//	 */
//	public void setContaReceberVO(ContaReceberVO contaReceberVO) {
//		this.contaReceberVO = contaReceberVO;
//	}
//
//	public String getSituacaoApresentarFichaAluno() {
//		if (getSituacaoInscricao().equals(SituacaoInscricaoEnum.ATIVO)) {
//			if (!getResultadoProcessoSeletivoVO().getCodigo().equals(0)) {
//				if (getResultadoProcessoSeletivoVO().getResultadoPrimeiraOpcao().equals("AP") || getResultadoProcessoSeletivoVO().getResultadoSegundaOpcao().equals("AP") || getResultadoProcessoSeletivoVO().getResultadoTerceiraOpcao().equals("AP")) {
//					return "APROVADO";
//				}
//				if (getResultadoProcessoSeletivoVO().getResultadoPrimeiraOpcao().equals("RE") || getResultadoProcessoSeletivoVO().getResultadoSegundaOpcao().equals("RE") || getResultadoProcessoSeletivoVO().getResultadoTerceiraOpcao().equals("RE")) {
//					return "REPROVADO";
//				}
//			}
//			if (getSituacao().equals("PF")) {
//				return "AGUARDANDO_PAGAMENTO";
//			}
//			if (getSituacao().equals("CO")) {
//				return "AGUARDANDO_RESULTADO";
//			}
//		}
//		if (getSituacaoInscricao().equals(SituacaoInscricaoEnum.CANCELADO) || getSituacaoInscricao().equals(SituacaoInscricaoEnum.CANCELADO_OUTRA_INSCRICAO)) {
//			return "CANCELADO";
//		}
//		if (getSituacaoInscricao().equals(SituacaoInscricaoEnum.NAO_COMPARECEU)) {
//			return "NAO_COMPARECEU";
//		}
//		return "";
//		
//	}
	
	public SituacaoInscricaoEnum getSituacaoInscricaoOrigem() {
		if (situacaoInscricaoOrigem == null) {
			situacaoInscricaoOrigem = SituacaoInscricaoEnum.ATIVO;
		}
		return situacaoInscricaoOrigem;
	}

	public void setSituacaoInscricaoOrigem(SituacaoInscricaoEnum situacaoInscricaoOrigem) {
		this.situacaoInscricaoOrigem = situacaoInscricaoOrigem;
	}
	
	@XmlElement(name = "formaIngresso")
	public String getFormaIngresso() {
		if (formaIngresso == null) {
			formaIngresso = "";
		}
		return formaIngresso;
	}

	public void setFormaIngresso(String formaIngresso) {
		this.formaIngresso = formaIngresso;
	}
	
	@XmlElement(name = "arquivoVO")
	public ArquivoVO getArquivoVO() {
		if (arquivoVO == null) {
			arquivoVO = new ArquivoVO();
		}
		return arquivoVO;
	}

	public void setArquivoVO(ArquivoVO arquivoVO) {
		this.arquivoVO = arquivoVO;
	}

	public List<ArquivoVO> getListaArquivosAnexo() {
		if (listaArquivosAnexo == null) {
            listaArquivosAnexo = new ArrayList();
        }
		
		return listaArquivosAnexo;
	}
	

	public void setListaArquivosAnexo(List<ArquivoVO> listaArquivosAnexo) {
		this.listaArquivosAnexo = listaArquivosAnexo;
	}

	public String getFormaIngresso_Apresentar() {
		
		if (getFormaIngresso().equals("EN")) {
			formaIngresso_Apresentar = "Enem/Análise Documento";
		}else if (getFormaIngresso().equals("PD")) {
			formaIngresso_Apresentar = "Portador de Diploma";
		}else if (getFormaIngresso().equals("TR")) {
			formaIngresso_Apresentar = "Transferência";
		}else {
			formaIngresso_Apresentar = "Processo Seletivo";
		}
		
		return formaIngresso_Apresentar;
	}
	
	public Integer getCodigoAutenticacao() {
		return codigoAutenticacao;
	}

	public void setCodigoAutenticacao(Integer codigoAutenticacao) {
		this.codigoAutenticacao = codigoAutenticacao;
	}

	public Date getDataAutenticacao() {
		return dataAutenticacao;
	}

	public void setDataAutenticacao(Date dataAutenticacao) {
		this.dataAutenticacao = dataAutenticacao;
	}

	@XmlElement(name = "permitirEmissaoBoleto")
	public Boolean getPermitirEmissaoBoleto() {
		if (permitirEmissaoBoleto == null) {
			permitirEmissaoBoleto = false;
		}
		return permitirEmissaoBoleto;
	}

	public void setPermitirEmissaoBoleto(Boolean permitirEmissaoBoleto) {
		this.permitirEmissaoBoleto = permitirEmissaoBoleto;
	}

	@XmlElement(name = "permitirRecebimentoCartaoCreditoOnline")
	public Boolean getPermitirRecebimentoCartaoCreditoOnline() {
		if (permitirRecebimentoCartaoCreditoOnline == null) {
			permitirRecebimentoCartaoCreditoOnline = false;
		}
		return permitirRecebimentoCartaoCreditoOnline;
	}

	public void setPermitirRecebimentoCartaoCreditoOnline(Boolean permitirRecebimentoCartaoCreditoOnline) {
		this.permitirRecebimentoCartaoCreditoOnline = permitirRecebimentoCartaoCreditoOnline;
	}

	@XmlElement(name = "matriculaRealizada")
	public Boolean getMatriculaRealizada() {
		if (matriculaRealizada == null) {
			matriculaRealizada = false;
		}
		return matriculaRealizada;
	}

	public void setMatriculaRealizada(Boolean matriculaRealizada) {
		this.matriculaRealizada = matriculaRealizada;
	}
	
	@XmlElement(name = "dataHoraTermino")
	public Timestamp getDataHoraTermino() {
		return dataHoraTermino;
	}

	public void setDataHoraTermino(Timestamp dataHoraTermino) {
		this.dataHoraTermino = dataHoraTermino;
	}

	@XmlElement(name = "dataHoraInicio")
	public Timestamp getDataHoraInicio() {
		return dataHoraInicio;
	}

	public void setDataHoraInicio(Timestamp dataHoraInicio) {
		this.dataHoraInicio = dataHoraInicio;
	}

	@XmlElement(name = "termoFoiAceito")
	public Boolean getTermoFoiAceito() {
		if (termoFoiAceito == null) {
			termoFoiAceito = false;
		}
		return termoFoiAceito;
	}

	public void setTermoFoiAceito(Boolean termoFoiAceito) {
		this.termoFoiAceito = termoFoiAceito;
	}
	
	@XmlElement(name = "tempoRestanteProva")
	public Long getTempoRestanteProva() {
		if (tempoRestanteProva == null) {
			tempoRestanteProva = 0L;
		}
		return tempoRestanteProva;
	}

	public void setTempoRestanteProva(Long tempoRestanteProva) {
		this.tempoRestanteProva = tempoRestanteProva;
	}

	@XmlElement(name = "configuracaoCandidatoProcessoSeletivoVO")
	public ConfiguracaoCandidatoProcessoSeletivoVO getConfiguracaoCandidatoProcessoSeletivoVO() {
		if(configuracaoCandidatoProcessoSeletivoVO == null ) {
			configuracaoCandidatoProcessoSeletivoVO = new ConfiguracaoCandidatoProcessoSeletivoVO();
		}
		return configuracaoCandidatoProcessoSeletivoVO;
	}

	public void setConfiguracaoCandidatoProcessoSeletivoVO(ConfiguracaoCandidatoProcessoSeletivoVO configuracaoCandidatoProcessoSeletivoVO) {
		this.configuracaoCandidatoProcessoSeletivoVO = configuracaoCandidatoProcessoSeletivoVO;
	}
	

	@XmlElement(name = "apresentarResultadoProcessoSeletivo")
	public Boolean getApresentarResultadoProcessoSeletivo() {
		if (apresentarResultadoProcessoSeletivo == null) {
			apresentarResultadoProcessoSeletivo = false;
		}
		return apresentarResultadoProcessoSeletivo;
	}

	public void setApresentarResultadoProcessoSeletivo(Boolean apresentarResultadoProcessoSeletivo) {
		this.apresentarResultadoProcessoSeletivo = apresentarResultadoProcessoSeletivo;
	}
	
	@XmlElement(name = "ocultarClassificacao")
    public Boolean getOcultarClassificacao() {
		if(ocultarClassificacao == null) {
			ocultarClassificacao = Boolean.FALSE;
		}
		return ocultarClassificacao;
	}

	public void setOcultarClassificacao(Boolean ocultarClassificacao) {
		this.ocultarClassificacao = ocultarClassificacao;
	}

	@XmlElement(name = "ocultarMedia")
	public Boolean getOcultarMedia() {
		if(ocultarMedia == null ) {
			ocultarMedia = Boolean.FALSE;
		}
		return ocultarMedia;
	}

	public void setOcultarMedia(Boolean ocultarMedia) {
		this.ocultarMedia = ocultarMedia;
	}

	@XmlElement(name = "ocultarChamadaCandidato")
	public Boolean getOcultarChamadaCandidato() {
		if(ocultarChamadaCandidato == null) {
			ocultarChamadaCandidato = Boolean.FALSE;
		}
		return ocultarChamadaCandidato;
	}

	public void setOcultarChamadaCandidato(Boolean ocultarChamadaCandidato) {
		this.ocultarChamadaCandidato = ocultarChamadaCandidato;
	}
	
	
	public boolean getPossuiRedacao() {
		if(possuiRedacao == null) {
			possuiRedacao = Boolean.FALSE;
		}
		return possuiRedacao;
	}
	
	public void setPossuiRedacao(Boolean possuiRedacao) {
		this.possuiRedacao = possuiRedacao;
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

	@XmlElement(name = "codigoAutenticacaoNavegador")
	public String getCodigoAutenticacaoNavegador() {
		if(codigoAutenticacaoNavegador == null) {
			codigoAutenticacaoNavegador ="";
		}
		return codigoAutenticacaoNavegador;
	}

	public void setCodigoAutenticacaoNavegador(String codigoAutenticacaoNavegador) {
		this.codigoAutenticacaoNavegador = codigoAutenticacaoNavegador;
	}

	@XmlElement(name = "permiteAcessarNavegador")
	public Boolean getPermiteAcessarNavegador() {
		if(permiteAcessarNavegador == null) {
			permiteAcessarNavegador = Boolean.FALSE;
		}
		return permiteAcessarNavegador;
	}

	public void setPermiteAcessarNavegador(Boolean permiteAcessarNavegador) {
		this.permiteAcessarNavegador = permiteAcessarNavegador;
	}

	public String getNavegadorAcesso() {
		if(navegadorAcesso == null) {
			navegadorAcesso = "";
		}
		return navegadorAcesso;
	}

	public void setNavegadorAcesso(String navegadorAcesso) {
		this.navegadorAcesso = navegadorAcesso;
	}

	@XmlElement(name = "sobreBolsasEAuxilios")
	public Boolean getSobreBolsasEAuxilios() {
		if (sobreBolsasEAuxilios == null) {
			sobreBolsasEAuxilios = false;
		}
		return sobreBolsasEAuxilios;
	}

	public void setSobreBolsasEAuxilios(Boolean sobreBolsasEAuxilios) {
		this.sobreBolsasEAuxilios = sobreBolsasEAuxilios;
	}

	@XmlElement(name = "autodeclaracaoPretoPardoOuIndigena")
	public Boolean getAutodeclaracaoPretoPardoOuIndigena() {
		if (autodeclaracaoPretoPardoOuIndigena == null) {
			autodeclaracaoPretoPardoOuIndigena = false;
		}
		return autodeclaracaoPretoPardoOuIndigena;
	}

	public void setAutodeclaracaoPretoPardoOuIndigena(Boolean autodeclaracaoPretoPardoOuIndigena) {
		this.autodeclaracaoPretoPardoOuIndigena = autodeclaracaoPretoPardoOuIndigena;
	}
	
	
	@XmlElement(name = "escolaPublica")
	public Boolean getEscolaPublica() {
		if (escolaPublica == null) {
			escolaPublica = false;
		}
		return escolaPublica;
	}

	public void setEscolaPublica(Boolean escolaPublica) {
		this.escolaPublica = escolaPublica;
	}
	

	public Boolean getInscricaoProvenienteImportacao() {
		if (inscricaoProvenienteImportacao == null) {
			inscricaoProvenienteImportacao = false;
		}
		return inscricaoProvenienteImportacao;
	}

	public void setInscricaoProvenienteImportacao(Boolean inscricaoProvenienteImportacao) {
		this.inscricaoProvenienteImportacao = inscricaoProvenienteImportacao;
	}
	
	@XmlElement(name = "dataHoraVencimentoCodigoAutenticacao")
	public Timestamp getDataHoraVencimentoCodigoAutenticacao() {
		return dataHoraVencimentoCodigoAutenticacao;
	}

	public void setDataHoraVencimentoCodigoAutenticacao(Timestamp dataHoraVencimentoCodigoAutenticacao) {
		this.dataHoraVencimentoCodigoAutenticacao = dataHoraVencimentoCodigoAutenticacao;
	}

	public Boolean getNecessarioInformarCodigoAutenticacao() {
		if(necessarioInformarCodigoAutenticacao == null) {
			necessarioInformarCodigoAutenticacao = Boolean.FALSE;
		}
		return necessarioInformarCodigoAutenticacao;
	}

	public void setNecessarioInformarCodigoAutenticacao(Boolean necessarioInformarCodigoAutenticacao) {
		this.necessarioInformarCodigoAutenticacao = necessarioInformarCodigoAutenticacao;
	}
	
	@XmlElement(name = "possivelAlterarInscricao")
	public Boolean getPossivelAlterarInscricao() {
		if(possivelAlterarInscricao == null) {
		   possivelAlterarInscricao = Boolean.TRUE;
		}
		return possivelAlterarInscricao;
	}
	
	public void setPossivelAlterarInscricao(Boolean possivelAlterarInscricao) {
		this.possivelAlterarInscricao = possivelAlterarInscricao;
	}
	
	
	public void naoEhPossivelAlterarInscricao() {
		setPossivelAlterarInscricao(Boolean.FALSE);
	}
	
	@XmlElement(name = "cursoAprovado")
	public Integer getCursoAprovado() {
		if (cursoAprovado == null) {
			cursoAprovado = 0;
		}
		return cursoAprovado;
	}

	public void setCursoAprovado(Integer cursoAprovado) {
		this.cursoAprovado = cursoAprovado;
	}

	
	public void setAlterarNumeroChamada(Boolean alterarNumeroChamada) {	
		this.alterarNumeroChamada =alterarNumeroChamada;
		
	}
	
	/**
	 * campo transient criado para validar no momento de importar os dados do candidato para a inscrição 
	 * caso o mesmo ja tenha inscrição o sistema altera sua chamada caso ja nao tenha se matriculado 	   
	 * @return
	 */
	public Boolean getAlterarNumeroChamada() {
		if (alterarNumeroChamada == null) {
			alterarNumeroChamada =Boolean.FALSE;
		}
		return alterarNumeroChamada;
	}

	
	
}
