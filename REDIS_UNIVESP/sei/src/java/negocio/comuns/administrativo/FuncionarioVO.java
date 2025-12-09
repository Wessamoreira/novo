package negocio.comuns.administrativo;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import controle.financeiro.RelatorioSEIDecidirControle;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.FuncionarioDependenteVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.enumeradores.SituacaoFuncionarioEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.financeiro.enumerador.TipoIdentificacaoChavePixEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.NivelFormacaoAcademica;
import negocio.comuns.utilitarias.dominios.RegimeTrabalhoDocente;
import negocio.comuns.utilitarias.dominios.SituacaoDocente;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

/**
 * Reponsável por manter os dados da entidade Funcionario. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
@XmlRootElement(name = "funcionarioVO")
public class FuncionarioVO extends SuperVO implements Comparable {

	public static final long serialVersionUID = 1L;

	private Integer codigo;
	private Date dataAdmissao;
	private String matricula;
	private List<FuncionarioCargoVO> funcionarioCargoVOs;
	private PessoaVO pessoa;
	private DepartamentoVO departamento;
	private UnidadeEnsinoVO unidadeEnsino;
	private Boolean exerceCargoAdministrativo;
	private String empresaRecebimento;
	private String cnpjEmpresaRecebimento;
	private String nomeBanco;
	private String numeroBancoRecebimento;
	private String numeroAgenciaRecebimento;
	private String contaCorrenteRecebimento;
	private String digitoAgenciaRecebimento;
	private String digitoCorrenteRecebimento;
	private String situacaoDocente;
	private String regimeTrabalhoDocente;
	private boolean docenteSubstituto;
	private String atuacaoDocente;
	private Boolean informarMatricula;
	private boolean naoNotificarInclusaoUsuario=false;

	private Integer qtdeAlunoVinculadosConsultor;

	private String observacao;
	private String escolaridade;

	private Double valorComissao;
	private ArquivoVO arquivoAssinaturaVO;
	private String caminhoBaseAssinatura;
	private String nomeArquivoAssinatura;
	private String operacaoBancaria;

	/**
	 * Atributo transiente somente para controle
	 * 
	 * @author Carlos Eugênio - 08/11/2016
	 * @return
	 */
	private Integer totalProspectPorConsultor;
	private CampanhaPublicoAlvoVO campanhaPublicoAlvoVO;
	private Integer quantidadeCompromissoUltrapassouDataLimiteCampanha;
	private Date dataPrimeiroCompromisso;
	private Date dataUltimoCompromisso;
	// Transient
	private List<MatriculaVO> matriculaConsultorVO;

	private List<FuncionarioDependenteVO> dependenteVOs;
	private Boolean selecionado;
	
	 /**
     * Usado na classe {@link RelatorioSEIDecidirControle}
     */
	private Boolean filtrarFuncionarioVO;
	private String chaveEnderecamentoPix;
	private TipoIdentificacaoChavePixEnum tipoIdentificacaoChavePixEnum;
	private boolean criarUsuario = false;
	

	/**
	 * Construtor padrão da classe <code>Funcionario</code>. Cria uma nova instância
	 * desta entidade, inicializando automaticamente seus atributos (Classe VO).
	 */
	public FuncionarioVO() {
		super();
		inicializarDados();
	}

	/**
	 * Operação responsável por validar os dados de um objeto da classe
	 * <code>FuncionarioVO</code>. Todos os tipos de consistência de dados são e
	 * devem ser implementadas neste método. São validações típicas: verificação de
	 * campos obrigatórios, verificação de valores válidos para os atributos.
	 * 
	 * @exception ConsistirExecption
	 *                Se uma inconsistência for encontrada aumaticamente é gerada
	 *                uma exceção descrevendo o atributo e o erro ocorrido.
	 */
	public static void validarDados(FuncionarioVO obj) throws ConsistirException {
		if (obj.getMatricula().equals("")) {
			throw new ConsistirException("O campo MATRÍCULA (Funcionário) deve ser informado.");
		}
		if (obj.pessoa.getNome().equals("")) {
			throw new ConsistirException("O campo NOME (Funcionário) deve ser informado.");
		}

		if (obj.pessoa.getDataNasc() == null) {
			throw new ConsistirException("O campo DATA NASCIMENTO (Dados Pessoais) deve ser informado.");
		}
		if (obj.pessoa.getCPF().equals("")) {
			throw new ConsistirException("O campo CPF (Dados Funcionais) deve ser informado.");
		}
	}

	/**
	 * Operação reponsável por inicializar os atributos da classe.
	 */
	public void inicializarDados() {
		setCodigo(0);
		setDataAdmissao(new Date());
		setExerceCargoAdministrativo(Boolean.FALSE);
		setMatricula("");
		setNomeBanco("");
		setNumeroBancoRecebimento("");
		setNumeroAgenciaRecebimento("");
		setContaCorrenteRecebimento("");
	}

	public void adicionarObjFuncionarioCargoVOs(FuncionarioCargoVO obj) throws Exception {
		FuncionarioCargoVO.validarDados(obj);
		Iterator<FuncionarioCargoVO> i = getFuncionarioCargoVOs().iterator();
		int index = 0;
		int aux = -1;
		FuncionarioCargoVO objAux = new FuncionarioCargoVO();
		while (i.hasNext()) {

			FuncionarioCargoVO objExistente = i.next();

			if (objExistente.getUnidade().getCodigo().equals(obj.getUnidade().getCodigo())
					&& objExistente.getCargo().getCodigo().equals(obj.getCargo().getCodigo())
					&& objExistente.getSituacaoFuncionario().equals(SituacaoFuncionarioEnum.ATIVO.name())) {
				if (!objExistente.getItemEmEdicao()) {
					throw new ConsistirException(UteisJSF
							.internacionalizar("prt_FuncionarioCargo_existeCadastroAtivoMesmoCargoEUnidadeEnsino"));
				}
			}

			if (objExistente.getCodigo().equals(obj.getCodigo()) && objExistente.getItemEmEdicao()) {
				obj.setItemEmEdicao(false);
				aux = index;
				objAux = obj;
			}
			index++;
		}
		if (aux >= 0) {
			getFuncionarioCargoVOs().set(aux, objAux);
		} else {
			getFuncionarioCargoVOs().add(obj);
		}
	}

	public void excluirObjFuncionarioCargoVOs(Integer cargo) {
		int index = 0;
		Iterator i = getFuncionarioCargoVOs().iterator();
		while (i.hasNext()) {
			FuncionarioCargoVO objExistente = (FuncionarioCargoVO) i.next();
			if (objExistente.getCargo().getCodigo().intValue() == cargo.intValue()) {
				getFuncionarioCargoVOs().remove(index);
				return;
			}
			index++;
		}
	}

	public FuncionarioCargoVO consultarObjFuncionarioCargoVO(Integer cargo) throws Exception {
		Iterator i = getFuncionarioCargoVOs().iterator();
		while (i.hasNext()) {
			FuncionarioCargoVO objExistente = (FuncionarioCargoVO) i.next();
			if (objExistente.getCargo().getCodigo().intValue() == cargo.intValue()) {
				return objExistente;
			}
		}
		return null;
	}

	public int getSituacaoDocenteCenso() {
		SituacaoDocente situacao = SituacaoDocente.getEnum(situacaoDocente);
		if (situacao == null) {
			return 1;
		} else {
			return situacao.getCodigoCenso();
		}
	}

	public String getRegimeTrabalhoDoceneteCenso() {
		RegimeTrabalhoDocente regime = RegimeTrabalhoDocente.getEnum(regimeTrabalhoDocente);
		if (regime == null) {
			return "4";
		} else {
			return String.valueOf(regime.getCodigoCenso());
		}
	}

	public int getDocenteSubstituto() {
		if (isDocenteSubstituto() && getSituacaoDocenteCenso() == 1) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * Retorna o objeto da classe <code>Pessoa</code> relacionado com
	 * (<code>Funcionario</code>).
	 */
	@XmlElement(name = "pessoa")
	public PessoaVO getPessoa() {
		if (pessoa == null) {
			pessoa = new PessoaVO();
		}
		return (pessoa);
	}

	/**
	 * Define o objeto da classe <code>Pessoa</code> relacionado com
	 * (<code>Funcionario</code>).
	 */
	public void setPessoa(PessoaVO obj) {
		this.pessoa = obj;
	}

	//
	// /**
	// * Retorna o objeto da classe <code>Departamento</code> relacionado com
	// (<code>Funcionario</code>).
	// */
	public DepartamentoVO getDepartamento() {
		if (departamento == null) {
			departamento = new DepartamentoVO();
		}
		return (departamento);
	}
	//
	// /**
	// * Define o objeto da classe <code>Departamento</code> relacionado com
	// (<code>Funcionario</code>).
	// */

	public void setDepartamento(DepartamentoVO obj) {
		this.departamento = obj;
	}

	/**
	 * Retorna o objeto da classe <code>UnidadeEnsino</code> relacionado com
	 * (<code>Funcionario</code>).
	 */
	public UnidadeEnsinoVO getUnidadeEnsino() {
		if (unidadeEnsino == null) {
			unidadeEnsino = new UnidadeEnsinoVO();
		}
		return (unidadeEnsino);
	}

	/**
	 * Define o objeto da classe <code>UnidadeEnsino</code> relacionado com
	 * (<code>Funcionario</code>).
	 */
	public void setUnidadeEnsino(UnidadeEnsinoVO obj) {
		this.unidadeEnsino = obj;
	}

	public Date getDataAdmissao() {
		return (dataAdmissao);
	}

	/**
	 * Operação responsável por retornar um atributo do tipo data no formato padrão
	 * dd/mm/aaaa.
	 */
	public String getDataAdmissao_Apresentar() {
		return (Uteis.getData(dataAdmissao));
	}

	public void setDataAdmissao(Date dataAdmissao) {
		this.dataAdmissao = dataAdmissao;
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

	public Boolean getExerceCargoAdministrativo() {
		if (exerceCargoAdministrativo == null) {
			exerceCargoAdministrativo = Boolean.FALSE;
		}
		return exerceCargoAdministrativo;
	}

	public void setExerceCargoAdministrativo(Boolean exerceCargoAdministrativo) {
		this.exerceCargoAdministrativo = exerceCargoAdministrativo;
	}

	/**
	 * @return the funcionarioCargoVOs
	 */
	public List<FuncionarioCargoVO> getFuncionarioCargoVOs() {
		if (funcionarioCargoVOs == null) {
			funcionarioCargoVOs = new ArrayList<FuncionarioCargoVO>(0);
		}
		return funcionarioCargoVOs;
	}

	/**
	 * @param funcionarioCargoVOs
	 *            the funcionarioCargoVOs to set
	 */
	public void setFuncionarioCargoVOs(List funcionarioCargoVOs) {
		this.funcionarioCargoVOs = funcionarioCargoVOs;
	}

	public void inicializarCentroCustoFuncionario() {
		getFuncionarioCargoVOs().forEach(fc -> fc.montarCentroCusto(this));
	}

	/** Gera uma lista dos centros de custo de um funcionario */
	public List getListaCentroCusto() {
		inicializarCentroCustoFuncionario();
		List listaCusto = new ArrayList();

		for (FuncionarioCargoVO funcionarioCargo : (List<FuncionarioCargoVO>) getFuncionarioCargoVOs()) {
			listaCusto.add(funcionarioCargo.getCentroCusto());
		}
		return listaCusto;
	}

	/**
	 * @return the nomeBanco
	 */
	public String getNomeBanco() {
		if (nomeBanco == null) {
			nomeBanco = "";
		}
		return nomeBanco;
	}

	/**
	 * @param nomeBanco
	 *            the nomeBanco to set
	 */
	public void setNomeBanco(String nomeBanco) {
		this.nomeBanco = nomeBanco;
	}

	/**
	 * @return the numeroBancoRecebimento
	 */
	public String getNumeroBancoRecebimento() {
		if (numeroBancoRecebimento == null) {
			numeroBancoRecebimento = "";
		}
		return numeroBancoRecebimento;
	}

	/**
	 * @param numeroBancoRecebimento
	 *            the numeroBancoRecebimento to set
	 */
	public void setNumeroBancoRecebimento(String numeroBancoRecebimento) {
		this.numeroBancoRecebimento = numeroBancoRecebimento;
	}

	/**
	 * @return the numeroAgenciaRecebimento
	 */
	public String getNumeroAgenciaRecebimento() {
		if (numeroAgenciaRecebimento == null) {
			numeroAgenciaRecebimento = "";
		}
		return numeroAgenciaRecebimento;
	}

	/**
	 * @param numeroAgenciaRecebimento
	 *            the numeroAgenciaRecebimento to set
	 */
	public void setNumeroAgenciaRecebimento(String numeroAgenciaRecebimento) {
		this.numeroAgenciaRecebimento = numeroAgenciaRecebimento;
	}

	/**
	 * @return the contaCorrenteRecebimento
	 */
	public String getContaCorrenteRecebimento() {
		if (contaCorrenteRecebimento == null) {
			contaCorrenteRecebimento = "";
		}
		return contaCorrenteRecebimento;
	}

	/**
	 * @param contaCorrenteRecebimento
	 *            the contaCorrenteRecebimento to set
	 */
	public void setContaCorrenteRecebimento(String contaCorrenteRecebimento) {
		this.contaCorrenteRecebimento = contaCorrenteRecebimento;
	}

	public String getSituacaoDocente() {
		if (situacaoDocente == null) {
			situacaoDocente = "";
		}
		return situacaoDocente;
	}

	public void setSituacaoDocente(String situacaoDocente) {
		this.situacaoDocente = situacaoDocente;
	}

	public String getRegimeTrabalhoDocente() {
		if (regimeTrabalhoDocente == null) {
			regimeTrabalhoDocente = "";
		}
		return regimeTrabalhoDocente;
	}

	public void setRegimeTrabalhoDocente(String regimeTrabalhoDocente) {
		this.regimeTrabalhoDocente = regimeTrabalhoDocente;
	}

	public boolean isDocenteSubstituto() {
		return docenteSubstituto;
	}

	public void setDocenteSubstituto(boolean docenteSubstituto) {
		this.docenteSubstituto = docenteSubstituto;
	}

	public String getAtuacaoDocente() {
		if (atuacaoDocente == null) {
			atuacaoDocente = "";
		}
		return atuacaoDocente;
	}

	public void setAtuacaoDocente(String atuacaoDocente) {
		this.atuacaoDocente = atuacaoDocente;
	}

	public Boolean getInformarMatricula() {
		if (informarMatricula == null) {
			informarMatricula = false;
		}
		return informarMatricula;
	}

	public void setInformarMatricula(Boolean informarMatricula) {
		this.informarMatricula = informarMatricula;
	}

	public boolean isNaoNotificarInclusaoUsuario() {
		return naoNotificarInclusaoUsuario;
	}

	public void setNaoNotificarInclusaoUsuario(boolean naoNotificarInclusaoUsuario) {
		this.naoNotificarInclusaoUsuario = naoNotificarInclusaoUsuario;
	}

	/**
	 * @return the qtdeAlunoVinculadosConsultor
	 */
	public Integer getQtdeAlunoVinculadosConsultor() {
		if (qtdeAlunoVinculadosConsultor == null) {
			qtdeAlunoVinculadosConsultor = 0;
		}
		return qtdeAlunoVinculadosConsultor;
	}

	/**
	 * @param qtdeAlunoVinculadosConsultor
	 *            the qtdeAlunoVinculadosConsultor to set
	 */
	public void setQtdeAlunoVinculadosConsultor(Integer qtdeAlunoVinculadosConsultor) {
		this.qtdeAlunoVinculadosConsultor = qtdeAlunoVinculadosConsultor;
	}

	/**
	 * @return the observacao
	 */
	public String getObservacao() {
		if (observacao == null) {
			observacao = "";
		}
		return observacao;
	}

	/**
	 * @param observacao
	 *            the observacao to set
	 */
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public String getEscolaridade() {
		if (escolaridade == null) {
			escolaridade = "";
		}
		return (escolaridade);
	}

	public String getEscolaridade_Apresentar() {
		return NivelFormacaoAcademica.getDescricao(getEscolaridade());
	}

	public void setEscolaridade(String escolaridade) {
		this.escolaridade = escolaridade;
	}

	public Double getValorComissao() {
		if (valorComissao == null) {
			valorComissao = 0.0;
		}
		return valorComissao;
	}

	public void setValorComissao(Double valorComissao) {
		this.valorComissao = valorComissao;
	}

	/**
	 * @return the arquivoAssinaturaVO
	 */
	public ArquivoVO getArquivoAssinaturaVO() {
		if (arquivoAssinaturaVO == null) {
			arquivoAssinaturaVO = new ArquivoVO();
		}
		return arquivoAssinaturaVO;
	}

	/**
	 * @param arquivoAssinaturaVO
	 *            the arquivoAssinaturaVO to set
	 */
	public void setArquivoAssinaturaVO(ArquivoVO arquivoAssinaturaVO) {
		this.arquivoAssinaturaVO = arquivoAssinaturaVO;
	}

	/**
	 * @return the caminhoBaseAssinatura
	 */
	public String getCaminhoBaseAssinatura() {
		if (caminhoBaseAssinatura == null) {
			caminhoBaseAssinatura = "";
		}
		return caminhoBaseAssinatura;
	}

	/**
	 * @param caminhoBaseAssinatura
	 *            the caminhoBaseAssinatura to set
	 */
	public void setCaminhoBaseAssinatura(String caminhoBaseAssinatura) {
		this.caminhoBaseAssinatura = caminhoBaseAssinatura;
	}

	/**
	 * @return the nomeArquivoAssinatura
	 */
	public String getNomeArquivoAssinatura() {
		if (nomeArquivoAssinatura == null) {
			nomeArquivoAssinatura = "";
		}
		return nomeArquivoAssinatura;
	}

	/**
	 * @param nomeArquivoAssinatura
	 *            the nomeArquivoAssinatura to set
	 */
	public void setNomeArquivoAssinatura(String nomeArquivoAssinatura) {
		this.nomeArquivoAssinatura = nomeArquivoAssinatura;
	}

	public String getEmpresaRecebimento() {
		return empresaRecebimento;
	}

	public void setEmpresaRecebimento(String empresaRecebimento) {
		this.empresaRecebimento = empresaRecebimento;
	}

	public String getCnpjEmpresaRecebimento() {
		return cnpjEmpresaRecebimento;
	}

	public void setCnpjEmpresaRecebimento(String cnpjEmpresaRecebimento) {
		this.cnpjEmpresaRecebimento = cnpjEmpresaRecebimento;
	}

	public Integer getTotalProspectPorConsultor() {
		if (totalProspectPorConsultor == null) {
			totalProspectPorConsultor = 0;
		}
		return totalProspectPorConsultor;
	}

	public void setTotalProspectPorConsultor(Integer totalProspectPorConsultor) {
		this.totalProspectPorConsultor = totalProspectPorConsultor;
	}

	public CampanhaPublicoAlvoVO getCampanhaPublicoAlvoVO() {
		if (campanhaPublicoAlvoVO == null) {
			campanhaPublicoAlvoVO = new CampanhaPublicoAlvoVO();
		}
		return campanhaPublicoAlvoVO;
	}

	public void setCampanhaPublicoAlvoVO(CampanhaPublicoAlvoVO campanhaPublicoAlvoVO) {
		this.campanhaPublicoAlvoVO = campanhaPublicoAlvoVO;
	}

	public Integer getQuantidadeCompromissoUltrapassouDataLimiteCampanha() {
		if (quantidadeCompromissoUltrapassouDataLimiteCampanha == null) {
			quantidadeCompromissoUltrapassouDataLimiteCampanha = 0;
		}
		return quantidadeCompromissoUltrapassouDataLimiteCampanha;
	}

	public void setQuantidadeCompromissoUltrapassouDataLimiteCampanha(
			Integer quantidadeCompromissoUltrapassouDataLimiteCampanha) {
		this.quantidadeCompromissoUltrapassouDataLimiteCampanha = quantidadeCompromissoUltrapassouDataLimiteCampanha;
	}

	public Date getDataPrimeiroCompromisso() {
		if (dataPrimeiroCompromisso == null) {
			dataPrimeiroCompromisso = new Date();
		}
		return dataPrimeiroCompromisso;
	}

	public String getDataPrimeiroCompromisso_Apresentar() {
		return Uteis.getDataAno4Digitos(getDataPrimeiroCompromisso());
	}

	public void setDataPrimeiroCompromisso(Date dataPrimeiroCompromisso) {
		this.dataPrimeiroCompromisso = dataPrimeiroCompromisso;
	}

	public Date getDataUltimoCompromisso() {
		if (dataUltimoCompromisso == null) {
			dataUltimoCompromisso = new Date();
		}
		return dataUltimoCompromisso;
	}

	public String getDataUltimoCompromisso_Apresentar() {
		return Uteis.getDataAno4Digitos(getDataUltimoCompromisso());
	}

	public void setDataUltimoCompromisso(Date dataUltimoCompromisso) {
		this.dataUltimoCompromisso = dataUltimoCompromisso;
	}

	public String getNomeConsultor() {
		return getPessoa().getNome();
	}

	public List<MatriculaVO> getMatriculaConsultorVO() {
		if (matriculaConsultorVO == null) {
			matriculaConsultorVO = new ArrayList<MatriculaVO>();
		}
		return matriculaConsultorVO;
	}

	public void setMatriculaConsultorVO(List<MatriculaVO> matriculaConsultorVO) {
		this.matriculaConsultorVO = matriculaConsultorVO;
	}

	public JRDataSource getListaMatricula() {
		return new JRBeanArrayDataSource(getMatriculaConsultorVO().toArray());
	}

	public String getDigitoAgenciaRecebimento() {
		return digitoAgenciaRecebimento;
	}

	public void setDigitoAgenciaRecebimento(String digitoAgenciaRecebimento) {
		this.digitoAgenciaRecebimento = digitoAgenciaRecebimento;
	}

	public String getDigitoCorrenteRecebimento() {
		return digitoCorrenteRecebimento;
	}

	public void setDigitoCorrenteRecebimento(String digitoCorrenteRecebimento) {
		this.digitoCorrenteRecebimento = digitoCorrenteRecebimento;
	}

	public List<FuncionarioDependenteVO> getDependenteVOs() {
		if (dependenteVOs == null)
			dependenteVOs = new ArrayList<>();
		return dependenteVOs;
	}

	public void setDependenteVOs(List<FuncionarioDependenteVO> dependenteVOs) {
		this.dependenteVOs = dependenteVOs;
	}

	public String getOperacaoBancaria() {
		if (operacaoBancaria == null) {
			operacaoBancaria = "";
		}
		return operacaoBancaria;
	}

	public void setOperacaoBancaria(String operacaoBancaria) {
		this.operacaoBancaria = operacaoBancaria;
	}

	public Boolean getFiltrarFuncionarioVO() {
		if (filtrarFuncionarioVO == null) {
			filtrarFuncionarioVO = false;
		}
		return filtrarFuncionarioVO;
	}

	public void setFiltrarFuncionarioVO(Boolean filtrarFuncionarioVO) {
		this.filtrarFuncionarioVO = filtrarFuncionarioVO;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FuncionarioVO other = (FuncionarioVO) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

	@Override
	public int compareTo(Object o) {
		FuncionarioVO funcionarioVO = (FuncionarioVO) o;
		return Integer.compare(this.codigo, funcionarioVO.codigo);
	}

	public Boolean getSelecionado() {
		if (selecionado == null) {
			selecionado = false;
		}
		return selecionado;
	}

	public void setSelecionado(Boolean selecionado) {
		this.selecionado = selecionado;
	}
	
	public String getChaveEnderecamentoPix() {
		if(chaveEnderecamentoPix == null ) {
			chaveEnderecamentoPix =""; 
		}
		return chaveEnderecamentoPix;
	}

	public void setChaveEnderecamentoPix(String chaveEnderecamentoPix) {
		this.chaveEnderecamentoPix = chaveEnderecamentoPix;
	}

	public TipoIdentificacaoChavePixEnum getTipoIdentificacaoChavePixEnum() {
		if(tipoIdentificacaoChavePixEnum == null ) {
			tipoIdentificacaoChavePixEnum = TipoIdentificacaoChavePixEnum.CPF_CNPJ;
		}
		return tipoIdentificacaoChavePixEnum;
	}

	public void setTipoIdentificacaoChavePixEnum(TipoIdentificacaoChavePixEnum tipoIdentificacaoChavePixEnum) {
		this.tipoIdentificacaoChavePixEnum = tipoIdentificacaoChavePixEnum;
	}
	
	/**
	 * @return the criarUsuario
	 */
	public boolean isCriarUsuario() {
		return criarUsuario;
	}

	/**
	 * @param criarUsuario the criarUsuario to set
	 */
	public void setCriarUsuario(boolean criarUsuario) {
		this.criarUsuario = criarUsuario;
	}
	
	/**
	 * @author FELIPE
	 * 
	 * metodo de vinculo de Funcionario a Usuario
	 */
	
}
