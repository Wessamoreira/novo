package negocio.comuns.compras;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.SerializationUtils;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.PerguntaRespostaOrigemVO;
import negocio.comuns.academico.QuestionarioRespostaOrigemVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.enumeradores.TipoAutorizacaoRequisicaoEnum;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.financeiro.CentroCustoVO;
import negocio.comuns.financeiro.CentroResultadoVO;
import negocio.comuns.financeiro.enumerador.TipoNivelCentroResultadoEnum;
import negocio.comuns.planoorcamentario.SolicitacaoOrcamentoPlanoOrcamentarioVO;
import negocio.comuns.processosel.QuestionarioVO;
import negocio.comuns.sad.DespesaDWVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.interfaces.arquitetura.SuperVOSelecionadoInterface;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;


public class RequisicaoVO extends SuperVO implements SuperVOSelecionadoInterface{

	/**
	 * 
	 */
	private static final long serialVersionUID = 369553757319223772L;
	private Integer codigo;
	private Date dataRequisicao;
	private String situacaoEntrega;
	private String situacaoAutorizacao;
	private Date dataAutorizacao;
	private String motivoSituacaoAutorizacao;
	private Date dataNecessidadeRequisicao;
	private UsuarioVO responsavelRequisicao;
	private UsuarioVO solicitanteRequisicao;
	private UsuarioVO responsavelAutorizacao;
	private String tipoSacado;
	private FornecedorVO sacadoFornecedor;
	private FuncionarioVO sacadoFuncionario;
	private TurmaVO turma;
	private CategoriaProdutoVO categoriaProduto;
	private CategoriaDespesaVO categoriaDespesa;
	private CursoVO curso;
	private TurnoVO turno;
	private List<RequisicaoItemVO> requisicaoItemVOs;
	private UnidadeEnsinoVO unidadeEnsino;
	private DepartamentoVO departamento;
	private CentroCustoVO centroCusto;
	private FuncionarioCargoVO funcionarioCargoVO;
	private CentroResultadoVO centroResultadoAdministrativo;
	private TipoAutorizacaoRequisicaoEnum tipoAutorizacaoRequisicaoEnum;
	private TipoNivelCentroResultadoEnum tipoNivelCentroResultadoEnum;
	private QuestionarioRespostaOrigemVO questionarioRespostaOrigemAberturaVO;
	private QuestionarioRespostaOrigemVO questionarioRespostaOrigemFechamentoVO;
	
	private List<PerguntaRespostaOrigemVO> perguntaRespostaOrigemVOs;

	// Atributo criado para controle do plano orçamentário
	private Boolean saldoPlanoOrcamentarioInsuficiente;
	private Boolean valorAcimaPrevistoAutorizado;
	
	/**
	 * Campos transient
	 */
	private boolean selecionado = true;
	private String produtoCategoriaPesquisa;
	private boolean liberadoValorMaximoCompraDireta = false;
	private String situacaoTipoAutorizacaoRequisicaoEnum;
	private boolean habilitarBotaoLiberacaoValorMaximoCompraDiretaRequisicao = false;
	
	
	private ArquivoVO arquivoVO;
	private SolicitacaoOrcamentoPlanoOrcamentarioVO solicitacaoOrcamentoPlanoOrcamentarioVO;
	
	private QuestionarioVO questionarioVO;
	private FuncionarioVO funcionarioVO;
	
	private String filtroTipoAutorizacaoRequisicaoEnum;

	public RequisicaoVO() {
		super();
		inicializarDados();
	}

	public static void validarDadosParaNaEntregaRequisicao(RequisicaoVO obj) throws ConsistirException {
		if (!obj.getSituacaoAutorizacao().equals("AU")) {
			throw new ConsistirException("Requisição ainda não foi autorizada.");
		}
		if (obj.getSituacaoEntrega().equals("FI")) {
			throw new ConsistirException("Todos os Itens da Requisição já foram entregue.");
		}
	}

	public static void atualizarSituacaoEntregaRequisicao(RequisicaoVO obj) {
		String situacao = "FI";
		for (RequisicaoItemVO item : (List<RequisicaoItemVO>) obj.requisicaoItemVOs) {
			if (item.getQuantidadeEntregue().doubleValue() > 0 && item.getQuantidadeEntregue().doubleValue() <= item.getQuantidadeAutorizada().doubleValue()) {
				obj.setSituacaoEntrega("PA");
				return;
			}
			if (item.getQuantidadeEntregue().doubleValue() == 0 && item.getQuantidadeAutorizada().doubleValue() > 0) {
				situacao = "PE";
			}
		}
		obj.setSituacaoEntrega(situacao);
	}
	
	public void inicializarDados() {
		setCodigo(0);
		setDataRequisicao(new Date());
		setSituacaoEntrega("PE");
		setSituacaoAutorizacao("PE");
		setDataAutorizacao(null);
		setMotivoSituacaoAutorizacao("");
		setCentroCusto(new CentroCustoVO(this.getUnidadeEnsino(), this.getDepartamento(), this.getResponsavelRequisicao().getPessoa()));
	}
	
	public void limparCamposPorTipoNivelCentroResultadoEnum() {
		switch (getTipoNivelCentroResultadoEnum()) {
		case CURSO:
			setTurma(new TurmaVO());
			setTurno(new TurnoVO());
			if(!getCategoriaDespesa().getExigeCentroCustoRequisitante()){
				setDepartamento(new DepartamentoVO());	
			}
			break;
		case CURSO_TURNO:
			setCurso(new CursoVO());
			setTurma(new TurmaVO());
			if(!getCategoriaDespesa().getExigeCentroCustoRequisitante()){
				setDepartamento(new DepartamentoVO());	
			}
			break;
		case TURMA:
			setCurso(new CursoVO());
			setTurno(new TurnoVO());
			if(!getCategoriaDespesa().getExigeCentroCustoRequisitante()){
				setDepartamento(new DepartamentoVO());	
			}
			break;
		case DEPARTAMENTO:
			setCurso(new CursoVO());
			setTurno(new TurnoVO());
			setTurma(new TurmaVO());
			break;
		case UNIDADE_ENSINO:
			setCurso(new CursoVO());
			setTurno(new TurnoVO());
			setTurma(new TurmaVO());
			if(!getCategoriaDespesa().getExigeCentroCustoRequisitante()){
				setDepartamento(new DepartamentoVO());	
			}
			break;
		}
	}

	public void atualizarCentroCustoVO() {
		setCentroCusto(new CentroCustoVO(this.getUnidadeEnsino(), this.getDepartamento(), this.getResponsavelRequisicao().getPessoa()));
	}

	public boolean isSacadoFuncionarioSelecionado() {
		return "FU".equals(getTipoSacado());
	}

	public boolean isSacadoFornecedorSelecionado() {
		return "FO".equals(getTipoSacado());
	}

	public DespesaDWVO getDespesaDW(Double valor, Integer funcionario) {
		DespesaDWVO obj = new DespesaDWVO("RE");
		obj.setAno(Uteis.getAnoData(new Date()));
		obj.setMes(Uteis.getMesDataAtual());
		obj.setData(new Date());
		obj.getCategoriaDespesa().setCodigo(getCategoriaDespesa().getCodigo());
		obj.getFornecedor().setCodigo(getSacadoFornecedor().getCodigo());
		obj.getFuncionario().setCodigo(getSacadoFuncionario().getCodigo());
		obj.getFuncionarioCentroCusto().setCodigo(funcionario);
		obj.getDepartamento().setCodigo(getDepartamento().getCodigo());
		obj.getTurma().setCodigo(getTurma().getCodigo());
		obj.getCurso().setCodigo(getTurma().getCurso().getCodigo());
		obj.getTurno().setCodigo(getTurma().getTurno().getCodigo());
		obj.getAreaConhecimento().setCodigo(getTurma().getCurso().getAreaConhecimento().getCodigo());
		obj.getUnidadeEnsino().setCodigo(getUnidadeEnsino().getCodigo());
		obj.setTipoSacado(getTipoSacado());
		obj.setValor(valor);
		return obj;
	}

	public boolean isExigeCompraCotacao() {
		return Uteis.isAtributoPreenchido(getRequisicaoItemVOs()) && getRequisicaoItemVOs().stream().anyMatch(p -> p.getProdutoServico().getExigeCompraCotacao());
	}

	public void adicionarObjRequisicaoItemVOs(RequisicaoItemVO obj) throws Exception {
		RequisicaoItemVO.validarDados(obj);
		int index = 0;
		if(!obj.getRequisicaoVO().getCodigo().equals(0) && !this.getCodigo().equals(obj.getRequisicaoVO().getCodigo())) {
			throw new Exception("O produto/serviço "+obj.getProdutoServico().getNome()+" é da requisição de código "+obj.getRequisicaoVO().getCodigo()+", provavelmente você está trabalhando com duas abas abertas da mesma tela.");	
		}
		obj.setRequisicaoVO(this);
		Iterator<RequisicaoItemVO> i = getRequisicaoItemVOs().iterator();
		while (i.hasNext()) {
			RequisicaoItemVO objExistente = i.next();
			if (objExistente.getProdutoServico().getCodigo().equals(obj.getProdutoServico().getCodigo())) {
				getRequisicaoItemVOs().set(index, obj);
				return;
			}
			index++;
		}
		getRequisicaoItemVOs().add(obj);
		getRequisicaoItemVOs().sort((r1, r2) -> r1.getProdutoServico().getNome().compareTo(r2.getProdutoServico().getNome()));
	}

	public void excluirObjRequisicaoItemVOs(Integer produto) throws Exception {
		int index = 0;
		Iterator<RequisicaoItemVO> i = getRequisicaoItemVOs().iterator();
		while (i.hasNext()) {			RequisicaoItemVO objExistente =  i.next();

			if (objExistente.getProdutoServico().getCodigo().equals(produto)) {
				getRequisicaoItemVOs().remove(index);
				return;
			}

			index++;
		}
	}
	
	public UsuarioVO getResponsavelAutorizacao() {
		if (responsavelAutorizacao == null) {
			responsavelAutorizacao = new UsuarioVO();
		}
		return (responsavelAutorizacao);
	}

	public void setResponsavelAutorizacao(UsuarioVO obj) {
		this.responsavelAutorizacao = obj;
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

	public DepartamentoVO getDepartamento() {
		if (departamento == null) {
			departamento = new DepartamentoVO();
		}
		return departamento;
	}

	public void setDepartamento(DepartamentoVO departamento) {
		this.departamento = departamento;
	}

	public UsuarioVO getResponsavelRequisicao() {
		if (responsavelRequisicao == null) {
			responsavelRequisicao = new UsuarioVO();
		}
		return (responsavelRequisicao);
	}

	public void setResponsavelRequisicao(UsuarioVO obj) {
		this.responsavelRequisicao = obj;
	}

	public List<RequisicaoItemVO> getRequisicaoItemVOs() {
		if (requisicaoItemVOs == null) {
			requisicaoItemVOs = new ArrayList<>();
		}
		return (requisicaoItemVOs);
	}

	public void setRequisicaoItemVOs(List<RequisicaoItemVO> requisicaoItemVOs) {
		this.requisicaoItemVOs = requisicaoItemVOs;
	}
	
	public Double getQuantidadeTotalSolicitada() {
		return getRequisicaoItemVOs().stream().mapToDouble(RequisicaoItemVO::getQuantidadeSolicitada).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));
	}
	
	public Double getQuantidadeTotalAutorizada() {
		return getRequisicaoItemVOs().stream().mapToDouble(RequisicaoItemVO::getQuantidadeAutorizada).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));
	}
	
	public Double getValorTotalRequisicao() {
		return getRequisicaoItemVOs().stream().mapToDouble(RequisicaoItemVO::getValorTotal).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));
	}
	
	public Double getValorTotalRequisicaoPorCompraDireta() {
		return getRequisicaoItemVOs().stream().filter(p->p.getTipoAutorizacaoRequisicaoEnum().isCompraDireta()).mapToDouble(RequisicaoItemVO::getValorTotal).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));
	}

	public boolean isExisteRequisicaoItemPorCompraDireta() {
		return Uteis.isAtributoPreenchido(getRequisicaoItemVOs()) && getRequisicaoItemVOs().stream().anyMatch(p-> p.getTipoAutorizacaoRequisicaoEnum().isCompraDireta());   
	}
	
	public boolean isExisteRequisicaoItemPorRetirada() {
		return Uteis.isAtributoPreenchido(getRequisicaoItemVOs()) && getRequisicaoItemVOs().stream().anyMatch(p-> p.getTipoAutorizacaoRequisicaoEnum().isRetirada());   
	}
	
	public String getMotivoSituacaoAutorizacao() {
		return (motivoSituacaoAutorizacao);
	}

	public void setMotivoSituacaoAutorizacao(String motivoSituacaoAutorizacao) {
		this.motivoSituacaoAutorizacao = motivoSituacaoAutorizacao;
	}

	public Date getDataAutorizacao() {
		return (dataAutorizacao);
	}

	public String getDataAutorizacao_Apresentar() {
		if (dataAutorizacao == null) {
			return "";
		}
		return (Uteis.getDataComHora(dataAutorizacao));
	}

	public void setDataAutorizacao(Date dataAutorizacao) {
		this.dataAutorizacao = dataAutorizacao;
	}

	public String getSituacaoAutorizacao() {
		return (situacaoAutorizacao);
	}

	public Boolean getAutorizado() {
		return Uteis.isAtributoPreenchido(getSituacaoAutorizacao()) && getSituacaoAutorizacao().equals("AU");
	}

	public boolean isSituacaoAutorizacaoIndeferido() {
		return Uteis.isAtributoPreenchido(getSituacaoAutorizacao()) && getSituacaoAutorizacao().equals("IN");
	}

	public boolean isSituacaoAutorizacaoPendente() {
		return Uteis.isAtributoPreenchido(getSituacaoAutorizacao()) && getSituacaoAutorizacao().equals("PE");
	}

	public String getSituacaoAutorizacao_Apresentar() {
		if (situacaoAutorizacao.equals("AU")) {
			return "Autorizado";
		}
		if (situacaoAutorizacao.equals("IN")) {
			return "Indeferido";
		}
		if (situacaoAutorizacao.equals("PE")) {
			return "Pendente";
		}
		return (situacaoAutorizacao);
	}

	public void setSituacaoAutorizacao(String situacaoAutorizacao) {
		this.situacaoAutorizacao = situacaoAutorizacao;
	}

	public String getSituacaoEntrega() {
		return (situacaoEntrega);
	}

	public String getSituacaoEntrega_Apresentar() {
		if (situacaoEntrega.equals("FI")) {
			return "Finalizada";
		}
		if (situacaoEntrega.equals("PE")) {
			return "Pendente";
		}
		if (situacaoEntrega.equals("PA")) {
			return "Parcial";
		}
		return (situacaoEntrega);
	}

	public void setSituacaoEntrega(String situacaoEntrega) {
		this.situacaoEntrega = situacaoEntrega;
	}

	public Date getDataRequisicao() {
		return (dataRequisicao);
	}

	public String getDataRequisicao_Apresentar() {
		if (dataRequisicao == null) {
			return "";
		}
		return (Uteis.getDataComHora(dataRequisicao));
	}

	public void setDataRequisicao(Date dataRequisicao) {
		this.dataRequisicao = dataRequisicao;
	}

	public Integer getCodigo() {
		return (codigo);
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the sacadoFornecedor
	 */
	public FornecedorVO getSacadoFornecedor() {
		if (sacadoFornecedor == null) {
			sacadoFornecedor = new FornecedorVO();
		}
		return sacadoFornecedor;
	}

	/**
	 * @param sacadoFornecedor
	 *            the sacadoFornecedor to set
	 */
	public void setSacadoFornecedor(FornecedorVO sacadoFornecedor) {
		this.sacadoFornecedor = sacadoFornecedor;
	}

	/**
	 * @return the sacadoFuncionario
	 */
	public FuncionarioVO getSacadoFuncionario() {
		if (sacadoFuncionario == null) {
			sacadoFuncionario = new FuncionarioVO();
		}
		return sacadoFuncionario;
	}

	/**
	 * @param sacadoFuncionario
	 *            the sacadoFuncionario to set
	 */
	public void setSacadoFuncionario(FuncionarioVO sacadoFuncionario) {
		this.sacadoFuncionario = sacadoFuncionario;
	}

	/**
	 * @return the tipoSacado
	 */
	public String getTipoSacado() {
		return tipoSacado;
	}

	/**
	 * @param tipoSacado
	 *            the tipoSacado to set
	 */
	public void setTipoSacado(String tipoSacado) {
		this.tipoSacado = tipoSacado;
	}

	/**
	 * @return the turma
	 */
	public TurmaVO getTurma() {
		if (turma == null) {
			turma = new TurmaVO();
		}
		return turma;
	}

	/**
	 * @param turma
	 *            the turma to set
	 */
	public void setTurma(TurmaVO turma) {
		this.turma = turma;
	}

	/**
	 * @return the categoriaDespesa
	 */
	public CategoriaDespesaVO getCategoriaDespesa() {
		if (categoriaDespesa == null) {
			categoriaDespesa = new CategoriaDespesaVO();
		}
		return categoriaDespesa;
	}

	/**
	 * @param categoriaDespesa
	 *            the categoriaDespesa to set
	 */
	public void setCategoriaDespesa(CategoriaDespesaVO categoriaDespesa) {
		this.categoriaDespesa = categoriaDespesa;
	}

	/**
	 * @return the categoriaProduto
	 */
	public CategoriaProdutoVO getCategoriaProduto() {
		if (categoriaProduto == null) {
			categoriaProduto = new CategoriaProdutoVO();
		}
		return categoriaProduto;
	}

	/**
	 * @param categoriaProduto
	 *            the categoriaProduto to set
	 */
	public void setCategoriaProduto(CategoriaProdutoVO categoriaProduto) {
		this.categoriaProduto = categoriaProduto;
	}

	/**
	 * @return the centroCusto
	 */
	public CentroCustoVO getCentroCusto() {
		return centroCusto;
	}

	/**
	 * @param centroCusto
	 *            the centroCusto to set
	 */
	public void setCentroCusto(CentroCustoVO centroCusto) {
		this.centroCusto = centroCusto;
	}

	public FuncionarioCargoVO getFuncionarioCargoVO() {
		funcionarioCargoVO = Optional.ofNullable(funcionarioCargoVO).orElse(new FuncionarioCargoVO());
		return funcionarioCargoVO;
	}

	public void setFuncionarioCargoVO(FuncionarioCargoVO funcionarioCargoVO) {
		this.funcionarioCargoVO = funcionarioCargoVO;
	}

	public CursoVO getCurso() {
		if (curso == null) {
			curso = new CursoVO();
		}
		return curso;
	}

	public Date getDataNecessidadeRequisicao() {
		return dataNecessidadeRequisicao;
	}

	public void setDataNecessidadeRequisicao(Date dataNecessidadeRequisicao) {
		this.dataNecessidadeRequisicao = dataNecessidadeRequisicao;
	}

	public void setCurso(CursoVO curso) {
		this.curso = curso;
	}

	public TurnoVO getTurno() {
		if (turno == null) {
			turno = new TurnoVO();
		}
		return turno;
	}

	public void setTurno(TurnoVO turno) {
		this.turno = turno;
	}

	public Boolean getSaldoPlanoOrcamentarioInsuficiente() {
		if (saldoPlanoOrcamentarioInsuficiente == null) {
			saldoPlanoOrcamentarioInsuficiente = Boolean.FALSE;
		}
		return saldoPlanoOrcamentarioInsuficiente;
	}

	public void setSaldoPlanoOrcamentarioInsuficiente(Boolean saldoPlanoOrcamentarioInsuficiente) {
		this.saldoPlanoOrcamentarioInsuficiente = saldoPlanoOrcamentarioInsuficiente;
	}

	public Boolean getValorAcimaPrevistoAutorizado() {
		if (valorAcimaPrevistoAutorizado == null) {
			valorAcimaPrevistoAutorizado = Boolean.FALSE;
		}
		return valorAcimaPrevistoAutorizado;
	}

	public void setValorAcimaPrevistoAutorizado(Boolean valorAcimaPrevistoAutorizado) {
		this.valorAcimaPrevistoAutorizado = valorAcimaPrevistoAutorizado;
	}

	public UsuarioVO getSolicitanteRequisicao() {
		this.solicitanteRequisicao = Optional.ofNullable(this.solicitanteRequisicao).orElse(new UsuarioVO());
		return solicitanteRequisicao;
	}

	public void setSolicitanteRequisicao(UsuarioVO responsavelCadastro) {
		this.solicitanteRequisicao = responsavelCadastro;
	}
	
	public CentroResultadoVO getCentroResultadoAdministrativo() {
		centroResultadoAdministrativo = Optional.ofNullable(centroResultadoAdministrativo).orElse(new CentroResultadoVO());
		return centroResultadoAdministrativo;
	}

	public void setCentroResultadoAdministrativo(CentroResultadoVO centroResultadoAdministrativo) {
		this.centroResultadoAdministrativo = centroResultadoAdministrativo;
	}

	
	public TipoAutorizacaoRequisicaoEnum getTipoAutorizacaoRequisicaoEnum() {
		if(tipoAutorizacaoRequisicaoEnum == null) {
			tipoAutorizacaoRequisicaoEnum = TipoAutorizacaoRequisicaoEnum.RETIRADA;
		}
		return tipoAutorizacaoRequisicaoEnum;
	}

	public void setTipoAutorizacaoRequisicaoEnum(TipoAutorizacaoRequisicaoEnum tipoAutorizacaoRequisicaoEnum) {
		this.tipoAutorizacaoRequisicaoEnum = tipoAutorizacaoRequisicaoEnum;
	}

	public String getProdutoCategoriaPesquisa() {
		produtoCategoriaPesquisa = Optional.ofNullable(produtoCategoriaPesquisa).orElse("");
		return produtoCategoriaPesquisa;
	}

	public void setProdutoCategoriaPesquisa(String produtoCategoriaPesquisa) {
		this.produtoCategoriaPesquisa = produtoCategoriaPesquisa;
	}

	public String getCssFuncionarioCargo() {
		return isCategoriaDespesaInformada() && (getCategoriaDespesa().getExigeCentroCustoRequisitante()) ? "camposObrigatorios" : "campos";
	}

	public boolean isCategoriaProdutoInformada() {
		return Uteis.isAtributoPreenchido(getCategoriaProduto());
	}

	public boolean isCategoriaDespesaInformada() {
		return Uteis.isAtributoPreenchido(getCategoriaDespesa());
	}

	public boolean isLiberadoValorMaximoCompraDireta() {
		return liberadoValorMaximoCompraDireta;
	}

	public void setLiberadoValorMaximoCompraDireta(boolean liberadoValorMaximoCompraDireta) {
		this.liberadoValorMaximoCompraDireta = liberadoValorMaximoCompraDireta;
	}
	
	public boolean getHabilitarBotaoLiberacaoValorMaximoCompraDiretaRequisicao() {
		return habilitarBotaoLiberacaoValorMaximoCompraDiretaRequisicao;
	}

	public void setHabilitarBotaoLiberacaoValorMaximoCompraDiretaRequisicao(boolean habilitarBotaoLiberacaoValorMaximoCompraDiretaRequisicao) {
		this.habilitarBotaoLiberacaoValorMaximoCompraDiretaRequisicao = habilitarBotaoLiberacaoValorMaximoCompraDiretaRequisicao;
	}

	public TipoNivelCentroResultadoEnum getTipoNivelCentroResultadoEnum() {		
		return tipoNivelCentroResultadoEnum;
	}

	public void setTipoNivelCentroResultadoEnum(TipoNivelCentroResultadoEnum tipoNivelCentroResultadoEnum) {
		this.tipoNivelCentroResultadoEnum = tipoNivelCentroResultadoEnum;
	}

	public boolean isExibirDataPrevisaoPagamento() {
		return Uteis.isAtributoPreenchido(getRequisicaoItemVOs()) && getRequisicaoItemVOs().stream().anyMatch(p-> !p.getProdutoServico().getExigeCompraCotacao());   
	}

	@Override
	public boolean isSelecionado() {
		return selecionado;
	}

	@Override
	public void setSelecionado(boolean selecionado) {
		this.selecionado = selecionado;
	}

	public RequisicaoVO duplicar() {

		RequisicaoVO clone = SerializationUtils.clone(this);
		clone.setCodigo(0);
		clone.setNovoObj(true);
		clone.setResponsavelAutorizacao(new UsuarioVO());
		clone.setDataAutorizacao(null);
		clone.setMotivoSituacaoAutorizacao("");
		clone.setSituacaoEntrega("PE");
		clone.setSituacaoAutorizacao("PE");
		clone.setMotivoSituacaoAutorizacao("");
		clone.getRequisicaoItemVOs().forEach(p -> {
			p.setCodigo(0);
			p.setRequisicaoVO(clone);
			p.setQuantidadeAutorizada(0.0);
			p.setQuantidadeEntregue(0.0);	
			p.setCotacao(0);
			p.setCompraItemVO(new CompraItemVO());
			p.setNovoObj(true);
		});
		return clone;
	}

	public ArquivoVO getArquivoVO() {
		if (arquivoVO == null) {
			arquivoVO = new ArquivoVO();
		}
		return arquivoVO;
	}
	public SolicitacaoOrcamentoPlanoOrcamentarioVO getSolicitacaoOrcamentoPlanoOrcamentarioVO() {
		if (solicitacaoOrcamentoPlanoOrcamentarioVO == null) {
			solicitacaoOrcamentoPlanoOrcamentarioVO = new SolicitacaoOrcamentoPlanoOrcamentarioVO();
		}
		return solicitacaoOrcamentoPlanoOrcamentarioVO;
	}

	public void setSolicitacaoOrcamentoPlanoOrcamentarioVO(
			SolicitacaoOrcamentoPlanoOrcamentarioVO solicitacaoOrcamentoPlanoOrcamentarioVO) {
		this.solicitacaoOrcamentoPlanoOrcamentarioVO = solicitacaoOrcamentoPlanoOrcamentarioVO;
	}

	public QuestionarioVO getQuestionarioVO() {
		if (questionarioVO == null) {
			questionarioVO = new QuestionarioVO();
		}
		return questionarioVO;
	}

	public void setQuestionarioVO(QuestionarioVO questionarioVO) {
		this.questionarioVO = questionarioVO;
	}

	public QuestionarioRespostaOrigemVO getQuestionarioRespostaOrigemAberturaVO() {
		if (questionarioRespostaOrigemAberturaVO == null) {
			questionarioRespostaOrigemAberturaVO = new QuestionarioRespostaOrigemVO();
		}
		return questionarioRespostaOrigemAberturaVO;
	}

	public void setQuestionarioRespostaOrigemAberturaVO(QuestionarioRespostaOrigemVO questionarioRespostaOrigemAberturaVO) {
		this.questionarioRespostaOrigemAberturaVO = questionarioRespostaOrigemAberturaVO;
	}

	public QuestionarioRespostaOrigemVO getQuestionarioRespostaOrigemFechamentoVO() {
		if (questionarioRespostaOrigemFechamentoVO == null) {
			questionarioRespostaOrigemFechamentoVO = new QuestionarioRespostaOrigemVO();
		}
		return questionarioRespostaOrigemFechamentoVO;
	}

	public void setQuestionarioRespostaOrigemFechamentoVO(
			QuestionarioRespostaOrigemVO questionarioRespostaOrigemFechamentoVO) {
		this.questionarioRespostaOrigemFechamentoVO = questionarioRespostaOrigemFechamentoVO;
	}

	public List<PerguntaRespostaOrigemVO> getPerguntaRespostaOrigemVOs() {
		if (perguntaRespostaOrigemVOs == null) {
			perguntaRespostaOrigemVOs = new ArrayList<>();
		}
		return perguntaRespostaOrigemVOs;
	}

	public void setPerguntaRespostaOrigemVOs(List<PerguntaRespostaOrigemVO> perguntaRespostaOrigemVOs) {
		this.perguntaRespostaOrigemVOs = perguntaRespostaOrigemVOs;
	}

	public FuncionarioVO getFuncionarioVO() {
		if (funcionarioVO == null) {
			funcionarioVO = new FuncionarioVO();
		}
		return funcionarioVO;
	}

	public void setFuncionarioVO(FuncionarioVO funcionarioVO) {
		this.funcionarioVO = funcionarioVO;
	}

	public JRDataSource getListaRequisicaoItemJR() {
		return new JRBeanArrayDataSource(getRequisicaoItemVOs().toArray());
	}
	
	public String getTipoNivelCentroResultadoEnumApresentar() {
		if (Uteis.isAtributoPreenchido(getTipoNivelCentroResultadoEnum())) {
			return TipoNivelCentroResultadoEnum.valorApresentar(getTipoNivelCentroResultadoEnum());
		}
		return "";
	}

	public String getTipoNivelCentroResultado() {
		if (Uteis.isAtributoPreenchido(getTipoNivelCentroResultadoEnum())) {
			return getTipoNivelCentroResultadoEnum().name();
		}
		return "";
	}

	public JRDataSource getQuestionarioPerguntaRespostaOrigemJRDataSource() {
		if (Uteis.isAtributoPreenchido(getQuestionarioRespostaOrigemAberturaVO().getPerguntaRespostaOrigemVOs())) {
			return new JRBeanArrayDataSource(getQuestionarioRespostaOrigemAberturaVO().getPerguntaRespostaOrigemVOs().toArray());
		} else {			
			return new JRBeanArrayDataSource(getQuestionarioRespostaOrigemFechamentoVO().getPerguntaRespostaOrigemVOs().toArray());
		}
	}

	public JRDataSource getQuestionarioPerguntaRespostaOrigemAberturaJRDataSource() {
		return new JRBeanArrayDataSource(getQuestionarioRespostaOrigemAberturaVO().getPerguntaRespostaOrigemVOs().toArray());
	}

	public JRDataSource getQuestionarioPerguntaRespostaOrigemFechamentoJRDataSource() {
		return new JRBeanArrayDataSource(getQuestionarioRespostaOrigemFechamentoVO().getPerguntaRespostaOrigemVOs().toArray());
	}
	public void setArquivoVO(ArquivoVO arquivoVO) {
		this.arquivoVO = arquivoVO;
	}

	public String getSituacaoTipoAutorizacaoRequisicaoEnum() {
		if (situacaoTipoAutorizacaoRequisicaoEnum == null) {
			situacaoTipoAutorizacaoRequisicaoEnum = "";
		}
		return situacaoTipoAutorizacaoRequisicaoEnum;
	}

	public void setSituacaoTipoAutorizacaoRequisicaoEnum(String situacaoTipoAutorizacaoRequisicaoEnum) {
		this.situacaoTipoAutorizacaoRequisicaoEnum = situacaoTipoAutorizacaoRequisicaoEnum;
	}
	
	public String getFiltroTipoAutorizacaoRequisicaoEnum() {
		if (filtroTipoAutorizacaoRequisicaoEnum == null) {
			filtroTipoAutorizacaoRequisicaoEnum = "";
		}
		return filtroTipoAutorizacaoRequisicaoEnum;
	}

	public void setFiltroTipoAutorizacaoRequisicaoEnum(String filtroTipoAutorizacaoRequisicaoEnum) {
		this.filtroTipoAutorizacaoRequisicaoEnum = filtroTipoAutorizacaoRequisicaoEnum;
	}
}
