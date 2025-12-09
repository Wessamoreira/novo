package negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.financeiro.enumerador.ContratoReceitaSituacaoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoContratosDespesas;

/**
 * Reponsável por manter os dados da entidade ContratosDespesas. Classe do tipo
 * VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class ContratosReceitasVO extends SuperVO {

	private Integer codigo;
	private String tipoContrato;
	private String descricao;
	private Date dataInicio;
	private Date dataTermino;
	private Date dataInicioVigencia;
	private Date dataTerminoVigencia;
	private Date dataPrimeiraParcela;
	private Boolean contratoIndeterminado;
	private ContratoReceitaSituacaoEnum situacao;
	private String mesVencimento;
	private String diaVencimento;
	private Double valorParcela;
	private Integer quantidadeParcelas;
	private Integer sacado;
	private UnidadeEnsinoVO unidadeEnsino;
	private PessoaVO pessoa;
	private String tipoSacado;
	private ParceiroVO parceiro;
	private FornecedorVO fornecedor;
	private CentroReceitaVO centroReceitaVO;
	private MatriculaVO matriculaVO;
	private DepartamentoVO departamentoVO;
	/**
	 * Atributo responsável por manter os objetos da classe
	 * <code>ContratoDespesaEspecifico</code>.
	 */
	private List<ContratoReceitaEspecificoVO> contratoReceitaEspecificoVOs;
	private ContaCorrenteVO contaCorrenteVO;
	private List<ContratoReceitaAlteracaoValorVO> contratoReceitaAlteracaoValorVOs;
	public static final long serialVersionUID = 1L;
	private MatriculaPeriodoVO matriculaPeriodoVO;

	/**
	 * Construtor padrão da classe <code>ContratosDespesas</code>. Cria uma nova
	 * instância desta entidade, inicializando automaticamente seus atributos
	 * (Classe VO).
	 */
	public ContratosReceitasVO() {
		super();
	}

	public String getNomeSacado_Apresentar() {
		if (this.getTipoSacado().equals("PA")) {
			return this.getParceiro().getNome();
		} else if (this.getTipoSacado().equals("FO")) {
			return this.getFornecedor().getNome();
		} else {
			return this.getPessoa().getNome();
		}
	}

	/**
	 * Operação responsável por validar os dados de um objeto da classe
	 * <code>ContratosDespesasVO</code>. Todos os tipos de consistência de dados
	 * são e devem ser implementadas neste método. São validações típicas:
	 * verificação de campos obrigatórios, verificação de valores válidos para
	 * os atributos.
	 * 
	 * @exception ConsistirExecption
	 *                Se uma inconsistência for encontrada aumaticamente é
	 *                gerada uma exceção descrevendo o atributo e o erro
	 *                ocorrido.
	 */
	public static void validarDados(ContratosReceitasVO obj) throws ConsistirException {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		if ((obj.getUnidadeEnsino() == null) || (obj.getUnidadeEnsino().getCodigo().intValue() == 0)) {
			throw new ConsistirException("O campo UNIDADE DE ENSINO (Contratos de Receitas) deve ser informado.");
		}

		if (obj.getTipoContrato().equals("")) {
			throw new ConsistirException("O campo TIPO CONTRATO (Contrato de Receitas) deve ser informado.");
		}
		if (obj.getTipoSacado().equals("FU") && obj.getPessoa().getCodigo() == 0) {
			throw new ConsistirException("O campo FUNCIONÁRIO (Contrato de Receitas) deve ser informado.");
		}
		Uteis.checkState(obj.isTipoSacadoFuncionario() && !Uteis.isAtributoPreenchido(obj.getDepartamentoVO()), "O campo DEPARTAMENTO (Contrato de Receitas) deve ser informado.");
		if (obj.getTipoSacado().equals("AL") && obj.getPessoa().getCodigo() == 0) {
			throw new ConsistirException("O campo ALUNO (Contrato de Receitas) deve ser informado.");
		}
		if (obj.getTipoSacado().equals("AL") && obj.getMatriculaVO().getMatricula().equals("")) {
			throw new ConsistirException("O campo MATRÍCULA (Contrato de Receitas) deve ser informado.");
		}
		if (obj.getTipoSacado().equals("PA") && obj.getParceiro().getCodigo() == 0) {
			throw new ConsistirException("O campo PARCEIRO (Contrato de Receitas) deve ser informado.");
		}
		if (obj.getTipoSacado().equals("FO") && obj.getFornecedor().getCodigo() == 0) {
			throw new ConsistirException("O campo FORNECEDOR (Contrato de Receitas) deve ser informado.");
		}

		if (obj.getDescricao().equals("")) {
			throw new ConsistirException("O campo DESCRIÇÃO (Contrato de Receitas) deve ser informado.");
		}
		if (!obj.getTipoContrato().equals("ES")) {
			if (obj.getDataInicio() == null) {
				throw new ConsistirException("O campo DATA INÍCIO (Contrato de Receitas) deve ser informado.");
			}
		}
		if (!obj.getTipoContrato().equals("ES") && obj.getValorParcela() == 0) {
			throw new ConsistirException("O campo VALOR PARCELA (Contrato de Receitas) deve ser informado.");
		}
		if (!obj.getContratoIndeterminado() && !obj.getTipoContrato().equals("ES")) {
			if (obj.getDataTermino() == null) {
				throw new ConsistirException("O campo DATA TÉRMINO (Contrato de Receitas) deve ser informado.");
			}
			if (obj.getDataInicio().after(obj.getDataTermino()) || obj.getDataInicio().compareTo(obj.getDataTermino()) > 0) {
				throw new ConsistirException("O campo DATA TERMINO deve ser maior que a DATA INÍCIO (Contrato de Receitas).");
			}
		}

		if (!obj.getTipoContrato().equals("ES")) {
			if (obj.getDiaVencimento().equals("")) {
				throw new ConsistirException("O campo DIA VENCIMENTO (Contrato de Receitas) deve ser informado.");
			}
			if (Integer.parseInt(obj.getDiaVencimento()) > 31 || Integer.parseInt(obj.getDiaVencimento()) < 1) {
				throw new ConsistirException("O campo DIA VENCIMENTO (Contrato de Receitas) é inválido.");
			}
		}

		if (obj.getTipoContrato().equals("AN")) {
			if (obj.getMesVencimento().equals("")) {
				throw new ConsistirException("O campo MÊS VENCIMENTO (Contrato de Despesa) deve ser informado.");
			}
			if (Integer.parseInt(obj.getMesVencimento()) > 12 || Integer.parseInt(obj.getMesVencimento()) < 1) {
				throw new ConsistirException("O campo MÊS VENCIMENTO (Contrato de Despesa) é inválido.");
			}
		}
		if (obj.getTipoContrato().equals("ES")) {
			if (obj.getContratoReceitaEspecificoVOs().isEmpty()) {
				throw new ConsistirException("Deve ser informado ao menos um contrato de despesa específico (Contrato de Despesa).");
			}
		}

	}

	public void adicionarObjContratoReceitaEspecificoVOs(ContratoReceitaEspecificoVO obj) throws Exception {

		ContratoReceitaEspecificoVO.validarDados(obj);
		String dataVenc = String.valueOf(obj.getDataVencimento().getDate()) + "/" + String.valueOf(obj.getDataVencimento().getMonth()) + "/" + String.valueOf(obj.getDataVencimento().getYear());
		String dataIni = String.valueOf(getDataInicio().getDate()) + "/" + String.valueOf(getDataInicio().getMonth()) + "/" + String.valueOf(getDataInicio().getYear());
		if (!dataVenc.equals(dataIni)) {
			if (!getTipoContrato().equals(TipoContratosDespesas.ESPECIFICO.getValor()) && obj.getDataVencimento().compareTo(getDataInicio()) == -1) {
				throw new ConsistirException("O campo DATA VENCIMENTO (Vencimento Específico) não pode ser menor que a data de inicio de contrato.");
			}
		}
		Iterator i = getContratoReceitaEspecificoVOs().iterator();
		while (i.hasNext()) {
			ContratoReceitaEspecificoVO objExistente = (ContratoReceitaEspecificoVO) i.next();
			if (objExistente.getDataVencimento().compareTo(obj.getDataVencimento()) == 0) {
				// getContratoDespesaEspecificoVOs().set(index, obj);
				throw new Exception("Já existe uma parcela para esta data de vencimento.");
			}
		}
		getContratoReceitaEspecificoVOs().add(obj);
		Ordenacao.ordenarLista(getContratoReceitaEspecificoVOs(), "ordenacao");
	}

	/**
	 * Operação responsável por excluir um objeto da classe
	 * <code>ContratoDespesaEspecificoVO</code> no List
	 * <code>contratoDespesaEspecificoVOs</code>. Utiliza o atributo padrão de
	 * consulta da classe <code>ContratoDespesaEspecifico</code> -
	 * getNrParcela() - como identificador (key) do objeto no List.
	 * 
	 * @param nrParcela
	 *            Parâmetro para localizar e remover o objeto do List.
	 */
	public void excluirObjContratoReceitaEspecificoVOs(Integer nrParcela) throws Exception {
		int index = 0;
		Iterator i = getContratoReceitaEspecificoVOs().iterator();
		while (i.hasNext()) {
			ContratoReceitaEspecificoVO objExistente = (ContratoReceitaEspecificoVO) i.next();
			if (objExistente.getNrParcela().equals(nrParcela)) {
				getContratoReceitaEspecificoVOs().remove(index);
				return;
			}
			index++;
		}
	}

	/**
	 * Operação responsável por consultar um objeto da classe
	 * <code>ContratoDespesaEspecificoVO</code> no List
	 * <code>contratoDespesaEspecificoVOs</code>. Utiliza o atributo padrão de
	 * consulta da classe <code>ContratoDespesaEspecifico</code> -
	 * getNrParcela() - como identificador (key) do objeto no List.
	 * 
	 * @param nrParcela
	 *            Parâmetro para localizar o objeto do List.
	 */
	public ContratoReceitaEspecificoVO consultarObjContratoReceitaEspecificoVO(Integer nrParcela) throws Exception {
		Iterator i = getContratoReceitaEspecificoVOs().iterator();
		while (i.hasNext()) {
			ContratoReceitaEspecificoVO objExistente = (ContratoReceitaEspecificoVO) i.next();
			if (objExistente.getNrParcela().equals(nrParcela)) {
				return objExistente;
			}
		}
		return null;
	}

	public boolean getApresentarContratoIndeterminado() {

		if (getTipoContrato().equals(TipoContratosDespesas.ESPECIFICO.getValor())) {
			return false;
		}

		return true;
	}

	public void inicializarUnidadeEnsinoLogado(UnidadeEnsinoVO unidadeEnsinoLogada) {
		if (unidadeEnsinoLogada.getCodigo().intValue() != 0) {
			getUnidadeEnsino().setCodigo(unidadeEnsinoLogada.getCodigo());
			getUnidadeEnsino().setNome(unidadeEnsinoLogada.getNome());
		}
	}

	public Boolean getAnual() {
		if (getTipoContrato().equals(TipoContratosDespesas.ANUAL.getValor())) {
			return true;
		}
		return false;
	}

	public Boolean getMensal() {
		if (getTipoContrato().equals(TipoContratosDespesas.MENSAL.getValor())) {
			return true;
		}
		return false;
	}

	public Boolean getEspecifico() {
		if (getTipoContrato().equals(TipoContratosDespesas.ESPECIFICO.getValor())) {
			return true;
		}
		return false;
	}

	public Boolean getAprovado() {
		return getSituacao().equals(ContratoReceitaSituacaoEnum.ATIVO);
	}

	public String getTipoSacado_Apresentar() {
		if (getTipoSacado().equals("AL")) {
			return "Aluno";
		} else if (getTipoSacado().equals("FU")) {
			return "Funcionário";
		} else {
			return "Parceiro";
		}
	}

	public Double getValorParcela() {
		if (valorParcela == null) {
			valorParcela = 0.0;
		}
		return (valorParcela);
	}

	public void setValorParcela(Double valorParcela) {
		this.valorParcela = valorParcela;
	}

	public String getDiaVencimento() {
		if (diaVencimento == null) {
			diaVencimento = "";
		}
		return (diaVencimento);
	}

	public void setDiaVencimento(String diaVencimento) {
		this.diaVencimento = diaVencimento;
	}

	public String getMesVencimento() {
		if (mesVencimento == null) {
			mesVencimento = "";
		}
		return (mesVencimento);
	}

	public void setMesVencimento(String mesVencimento) {
		this.mesVencimento = mesVencimento;
	}

	public Boolean getContratoIndeterminado() {
		if (contratoIndeterminado == null) {
			contratoIndeterminado = Boolean.FALSE;
		}
		return (contratoIndeterminado);
	}

	public Boolean isContratoIndeterminado() {
		if (contratoIndeterminado == null) {
			contratoIndeterminado = Boolean.FALSE;
		}
		return (contratoIndeterminado);
	}

	public void setContratoIndeterminado(Boolean contratoIndeterminado) {
		this.contratoIndeterminado = contratoIndeterminado;
	}

	public Date getDataTermino() {
		if (dataTermino == null) {
			dataTermino = new Date();
		}
		return (dataTermino);
	}

	/**
	 * Operação responsável por retornar um atributo do tipo data no formato
	 * padrão dd/mm/aaaa.
	 */
	public String getDataTermino_Apresentar() {
		return (Uteis.getData(dataTermino));
	}
	
	public String getDataVigenciaTermino_Apresentar() {
		return (Uteis.getData(dataTerminoVigencia));
	}

	public void setDataTermino(Date dataTermino) {
		this.dataTermino = dataTermino;
	}

	public Date getDataInicio() {
		if (dataInicio == null) {
			dataInicio = new Date();
		}
		return (dataInicio);
	}

	public Integer getPorcentagemInicioTermino() {
		Long totalDiasLong = Uteis.nrDiasEntreDatas(getDataTermino(), getDataInicio());
		Long diasAteHojeLong = Uteis.nrDiasEntreDatas(getDataInicio(), new Date());
		Integer totalDias = Integer.valueOf(totalDiasLong.intValue());
		Integer diasAteHoje = Integer.valueOf(diasAteHojeLong.intValue());
		if (totalDias == 0 || diasAteHoje == 0) {
			return 0;
		} else {
			return (diasAteHoje * 100) / totalDias;
		}
	}

	public Date getDataPrimeiraParcela() {
		if (dataPrimeiraParcela == null) {
			dataPrimeiraParcela = new Date();
		}
		return dataPrimeiraParcela;
	}

	public void setDataPrimeiraParcela(Date dataPrimeiraParcela) {
		this.dataPrimeiraParcela = dataPrimeiraParcela;
	}

	/**
	 * Operação responsável por retornar um atributo do tipo data no formato
	 * padrão dd/mm/aaaa.
	 */
	public String getDataInicio_Apresentar() {
		return (Uteis.getData(dataInicio));
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public String getDescricao() {
		if (descricao == null) {
			descricao = "";
		}
		return (descricao);
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getTipoContrato() {
		if (tipoContrato == null) {
			tipoContrato = "";
		}
		return (tipoContrato);
	}

	/**
	 * Operação responsável por retornar o valor de apresentação de um atributo
	 * com um domínio específico. Com base no valor de armazenamento do atributo
	 * esta função é capaz de retornar o de apresentação correspondente. Útil
	 * para campos como sexo, escolaridade, etc.
	 */
	public String getTipoContrato_Apresentar() {
		return TipoContratosDespesas.getDescricao(tipoContrato);
	}

	public void setTipoContrato(String tipoContrato) {
		this.tipoContrato = tipoContrato;
	}

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return (codigo);
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public UnidadeEnsinoVO getUnidadeEnsino() {
		if (unidadeEnsino == null) {
			unidadeEnsino = new UnidadeEnsinoVO();
		}
		return unidadeEnsino;
	}

	public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}

	public Boolean getHabilitarComboBoxUnidadeEnsino(Integer unidadeEnsino) {
		if (unidadeEnsino == 0) {
			return false;
		}
		return true;
	}

	/**
	 * @param turma
	 *            the turma to set
	 */
	public PessoaVO getPessoa() {
		if (pessoa == null) {
			pessoa = new PessoaVO();
		}
		return pessoa;
	}

	public void setPessoa(PessoaVO pessoa) {
		this.pessoa = pessoa;
	}

	public boolean getDadosBasicosDisponiveisSomenteConsulta() {
		if (this.getSituacao().equals(ContratoReceitaSituacaoEnum.ATIVO)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isValorDaParcelaAlteravel() {
		return ContratoReceitaSituacaoEnum.ATIVO.equals(getSituacao()) && !isNovoObj();
	}

	public String getTipoSacado() {
		if (tipoSacado == null) {
			tipoSacado = "FU";
		}
		return tipoSacado;
	}

	public void setTipoSacado(String tipoSacado) {
		this.tipoSacado = tipoSacado;
	}

	public ParceiroVO getParceiro() {
		if (parceiro == null) {
			parceiro = new ParceiroVO();
		}
		return parceiro;
	}

	public void setParceiro(ParceiroVO parceiro) {
		this.parceiro = parceiro;
	}

	public CentroReceitaVO getCentroReceitaVO() {
		if (centroReceitaVO == null) {
			centroReceitaVO = new CentroReceitaVO();
		}
		return centroReceitaVO;
	}

	public void setCentroReceitaVO(CentroReceitaVO centroReceitaVO) {
		this.centroReceitaVO = centroReceitaVO;
	}

	public List<ContratoReceitaEspecificoVO> getContratoReceitaEspecificoVOs() {
		if (contratoReceitaEspecificoVOs == null) {
			contratoReceitaEspecificoVOs = new ArrayList<ContratoReceitaEspecificoVO>(0);
		}
		return contratoReceitaEspecificoVOs;
	}

	public void setContratoReceitaEspecificoVOs(List<ContratoReceitaEspecificoVO> contratoReceitaEspecificoVOs) {
		this.contratoReceitaEspecificoVOs = contratoReceitaEspecificoVOs;
	}

	public Integer getSacado() {
		if (sacado == null) {
			sacado = 0;
		}
		return sacado;
	}

	public void setSacado(Integer sacado) {
		this.sacado = sacado;
	}

	public ContaCorrenteVO getContaCorrenteVO() {
		if (contaCorrenteVO == null) {
			contaCorrenteVO = new ContaCorrenteVO();
		}
		return contaCorrenteVO;
	}

	public void setContaCorrenteVO(ContaCorrenteVO contaCorrenteVO) {
		this.contaCorrenteVO = contaCorrenteVO;
	}

	/**
	 * @return the dataInicioVigencia
	 */
	public Date getDataInicioVigencia() {
		if (dataInicioVigencia == null) {
			dataInicioVigencia = new Date();
		}
		return dataInicioVigencia;
	}

	/**
	 * @param dataInicioVigencia
	 *            the dataInicioVigencia to set
	 */
	public void setDataInicioVigencia(Date dataInicioVigencia) {
		this.dataInicioVigencia = dataInicioVigencia;
	}

	/**
	 * @return the dataTerminoVigencia
	 */
	public Date getDataTerminoVigencia() {
		if (dataTerminoVigencia == null) {
			dataTerminoVigencia = new Date();
		}
		return dataTerminoVigencia;
	}

	/**
	 * @param dataTerminoVigencia
	 *            the dataTerminoVigencia to set
	 */
	public void setDataTerminoVigencia(Date dataTerminoVigencia) {
		this.dataTerminoVigencia = dataTerminoVigencia;
	}

	/**
	 * @return the quantidadeParcelas
	 */
	public Integer getQuantidadeParcelas() {
		if (quantidadeParcelas == null) {
			quantidadeParcelas = 0;
		}
		return quantidadeParcelas;
	}

	/**
	 * @param quantidadeParcelas
	 *            the quantidadeParcelas to set
	 */
	public void setQuantidadeParcelas(Integer quantidadeParcelas) {
		this.quantidadeParcelas = quantidadeParcelas;
	}

	public FornecedorVO getFornecedor() {
		if (fornecedor == null) {
			fornecedor = new FornecedorVO();
		}
		return fornecedor;
	}

	public void setFornecedor(FornecedorVO fornecedor) {
		this.fornecedor = fornecedor;
	}

	public ContratoReceitaSituacaoEnum getSituacao() {
		if (situacao == null) {
			situacao = ContratoReceitaSituacaoEnum.EM_CONSTRUCAO;
		}
		return situacao;
	}

	public void setSituacao(ContratoReceitaSituacaoEnum situacao) {
		this.situacao = situacao;
	}

	public String getSituacao_Apresentar() {
		return getSituacao().getDescricao();
	}
	
	public List<ContratoReceitaAlteracaoValorVO> getContratoReceitaAlteracaoValorVOs() {
		if(contratoReceitaAlteracaoValorVOs == null){
			contratoReceitaAlteracaoValorVOs =new ArrayList<ContratoReceitaAlteracaoValorVO>(0);
		}
		return contratoReceitaAlteracaoValorVOs;
	}

	public void setContratoReceitaAlteracaoValorVOs(
			List<ContratoReceitaAlteracaoValorVO> contratoReceitaAlteracaoValorVOs) {
		this.contratoReceitaAlteracaoValorVOs = contratoReceitaAlteracaoValorVOs;
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

	public DepartamentoVO getDepartamentoVO() {
		departamentoVO = Optional.ofNullable(departamentoVO).orElse(new DepartamentoVO());
		return departamentoVO;
	}

	public void setDepartamentoVO(DepartamentoVO departamentoVO) {
		this.departamentoVO = departamentoVO;
	}
	
	public boolean isTipoSacadoFuncionario(){
		return Uteis.isAtributoPreenchido(getTipoSacado()) && getTipoSacado().equals("FU"); 
	}
	
	@Override
	public String toString() {
		return "Contratos de Receita [" + this.getCodigo() + "]: " + 
                        " Descrição: " + this.getDescricao() + 
                        " Situação: " + this.getSituacao_Apresentar() + 
                        " Tipo Sacado: " + this.getTipoSacado() +
                        " Codigo Sacado: " + this.getSacado() +
                        " Sacado: " + this.getNomeSacado_Apresentar() + 
                        " Valor Parcela: " + Uteis.getDoubleFormatado(this.getValorParcela());
	}

	public MatriculaPeriodoVO getMatriculaPeriodoVO() {
		if (matriculaPeriodoVO == null) {
			matriculaPeriodoVO = new MatriculaPeriodoVO();
		}
		return matriculaPeriodoVO;
	}

	public void setMatriculaPeriodoVO(MatriculaPeriodoVO matriculaPeriodoVO) {
		this.matriculaPeriodoVO = matriculaPeriodoVO;
	}
}
