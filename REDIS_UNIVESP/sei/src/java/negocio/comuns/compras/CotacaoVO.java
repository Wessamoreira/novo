package negocio.comuns.compras;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.financeiro.CentroResultadoOrigemVO;
import negocio.comuns.financeiro.CentroResultadoVO;
import negocio.comuns.financeiro.enumerador.TipoNivelCentroResultadoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

public class CotacaoVO extends SuperVO {

	private Integer codigo;
	private Date dataCotacao;
	private Date dataAutorizacao;
	private List<CotacaoFornecedorVO> cotacaoFornecedorVOs;
	private UsuarioVO responsavelCotacao;
	private UsuarioVO responsavelAutorizacao;
	private String situacao;
	private String motivoRevisao;
	private TramiteCotacaoCompraVO tramiteCotacaoCompra;
	private UnidadeEnsinoVO unidadeEnsinoResponsavelTramitacao;
	private CategoriaProdutoVO categoriaProduto;
	private CategoriaDespesaVO categoriaDespesa;
	private CursoVO cursoCategoriaDespesa;
	private TurnoVO turnoCategoriaDespesa;
	private TurmaVO turmaCategoriaDespesa;
	private UnidadeEnsinoVO unidadeEnsinoCategoriaDespesa;
	private DepartamentoVO departamentoCategoriaDespesa;
	private FuncionarioVO funcionarioCategoriaDespesa;
	private CentroResultadoVO centroResultadoAdministrativo;
	private TipoNivelCentroResultadoEnum tipoNivelCentroResultadoEnum;
	private List<CentroResultadoOrigemVO> listaCentroResultadoOrigemVOs;

	/**
	 * transient
	 */
	private List<UnidadeEnsinoVO> listaUnidadeEnsinoVOs;
	private List<CotacaoHistoricoVO> listaCotacaoHistoricoVOs;
	private CotacaoHistoricoVO atualCotacaoHistoricoVO;
	public static final long serialVersionUID = 1L;

	public CotacaoVO() {
		super();
		inicializarDados();
	}

	public static void validarDados(CotacaoVO obj) throws ConsistirException {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		if ((obj.getResponsavelCotacao() == null) || (obj.getResponsavelCotacao().getCodigo().intValue() == 0)) {
			throw new ConsistirException("O campo RESPONSÁVEL COTAÇÃO (Cotação) deve ser informado.");
		}
		if (obj.getDataCotacao() == null) {
			throw new ConsistirException("O campo DATA COTAÇÃO (Cotação) deve ser informado.");
		}
		if (obj.getCategoriaProduto() == null || obj.getCategoriaProduto().getCodigo().intValue() == 0) {
			throw new ConsistirException("O campo CATEGORIA PRODUTO (Cotação) deve ser informado.");
		}
		if (Uteis.isAtributoPreenchido(obj.getTramiteCotacaoCompra()) && !Uteis.isAtributoPreenchido(obj.getUnidadeEnsinoResponsavelTramitacao())) {
			throw new ConsistirException("O campo Unidade de Ensino Responsável pela Tramitação (Cotação) deve ser informado.");
		}
		if (!Uteis.isAtributoPreenchido(obj.getCategoriaDespesa())) {
			throw new ConsistirException("O campo Categoria Despesa (Cotação) deve ser informado.");
		}
		
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getUnidadeEnsinoCategoriaDespesa()), "O campo Unidade Ensino Categoria Despesa (Cotação) deve ser informado.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getTipoNivelCentroResultadoEnum()), "O campo Cotação Para (Cotação) deve ser informado.");
		Uteis.checkState(obj.getTipoNivelCentroResultadoEnum().isDepartamento() && !Uteis.isAtributoPreenchido(obj.getDepartamentoCategoriaDespesa()), "O campo Departamento Categoria Despesa (Cotação) deve ser informado.");
		Uteis.checkState(obj.getTipoNivelCentroResultadoEnum().isCurso() && !Uteis.isAtributoPreenchido(obj.getCursoCategoriaDespesa()), "O campo Curso Categoria Despesa (Cotação) deve ser informado.");
		Uteis.checkState(obj.getTipoNivelCentroResultadoEnum().isCursoTurno() && !Uteis.isAtributoPreenchido(obj.getTurnoCategoriaDespesa()), "O campo Turno Categoria Despesa (Cotação) deve ser informado.");
		Uteis.checkState(obj.getTipoNivelCentroResultadoEnum().isTurma() && !Uteis.isAtributoPreenchido(obj.getTurmaCategoriaDespesa()), "O campo Turma Categoria Despesa (Cotação) deve ser informado.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getCentroResultadoAdministrativo()), "O campo Centro de Resultado (Cotação) deve ser informado.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getListaUnidadeEnsinoVOs()), "Deve ser informado pelo menos uma UNIDADE ENSINO (Cotação).");
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getCotacaoFornecedorVOs()), "Deve ser adicionado ao menos um FORNECEDOR (Cotação).");
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getItemCotacaoVOs()), "Deve ser adicionado ao menos um Produto Para O Fornecedor (Cotação).");
		

	}
	
	public void limparCamposPorTipoNivelCentroResultadoEnum() {
		switch (getTipoNivelCentroResultadoEnum()) {
		case CURSO:
			setTurmaCategoriaDespesa(new TurmaVO());
			setTurnoCategoriaDespesa(new TurnoVO());
			if(!getCategoriaDespesa().getExigeCentroCustoRequisitante()){
				setDepartamentoCategoriaDespesa(new DepartamentoVO());	
			}
			break;
		case CURSO_TURNO:
			setCursoCategoriaDespesa(new CursoVO());
			setTurmaCategoriaDespesa(new TurmaVO());			
			if(!getCategoriaDespesa().getExigeCentroCustoRequisitante()){
				setDepartamentoCategoriaDespesa(new DepartamentoVO());	
			}
			break;
		case TURMA:
			setCursoCategoriaDespesa(new CursoVO());
			setTurnoCategoriaDespesa(new TurnoVO());			
			if(!getCategoriaDespesa().getExigeCentroCustoRequisitante()){
				setDepartamentoCategoriaDespesa(new DepartamentoVO());	
			}
			break;
		case DEPARTAMENTO:
			setCursoCategoriaDespesa(new CursoVO());
			setTurnoCategoriaDespesa(new TurnoVO());
			setTurmaCategoriaDespesa(new TurmaVO());			
			break;
		case UNIDADE_ENSINO:
			setCursoCategoriaDespesa(new CursoVO());
			setTurnoCategoriaDespesa(new TurnoVO());
			setTurmaCategoriaDespesa(new TurmaVO());
			if(!getCategoriaDespesa().getExigeCentroCustoRequisitante()){
				setDepartamentoCategoriaDespesa(new DepartamentoVO());	
			}
			break;
		}
	}

	
	public void validarDadosParaLiberacaoMapa() throws ConsistirException {
		boolean isExisteCompraAutorizada = false;
		for (CotacaoFornecedorVO cotacaoFornecedorVO : getCotacaoFornecedorVOs()) {
			for (ItemCotacaoVO item : cotacaoFornecedorVO.getItemCotacaoVOs()) {
				if(item.getCompraAutorizadaFornecedor()){
					isExisteCompraAutorizada = true;
					Uteis.checkState(!Uteis.isAtributoPreenchido(item.getQuantidade()), "O produto "+ item.getProduto().getNome() + " Para o Fornecedor "+cotacaoFornecedorVO.getFornecedor().getNome()+" está selecionado para ser cotado. Portanto o campo Quantidade deve ser informado.");
					Uteis.checkState(!Uteis.isAtributoPreenchido(item.getPrecoUnitario()), "O produto "+ item.getProduto().getNome() + " Para o Fornecedor "+cotacaoFornecedorVO.getFornecedor().getNome()+" está selecionado para ser cotado. Portanto o campo Preço deve ser informado.");
					CotacaoFornecedorVO.validarDadosAutorizacao(cotacaoFornecedorVO);	
				}
			}
		}
		Uteis.checkState(!isExisteCompraAutorizada, "Não foi encontrado nenhum produto marcado para que seja realizado a liberação para o mapa de cotação. ");
	}

	public void inicializarDados() {
		setCodigo(0);
		setDataCotacao(new Date());
		setDataAutorizacao(new Date());
		setSituacao("EC");
		setMotivoRevisao("");
	}	

	public Integer getQtdeFornecedor() {
		return getCotacaoFornecedorVOs().size();
	}

	public Integer getQtdeProduto() {
		return getItemCotacaoVOs().size();
	}

	public List<ItemCotacaoVO> getItemCotacaoVOs() {
		if (Uteis.isAtributoPreenchido(getCotacaoFornecedorVOs())) {
			return getCotacaoFornecedorVOs().get(0).getItemCotacaoVOs();
		}
		return new ArrayList<>();
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

	public UsuarioVO getResponsavelCotacao() {
		if (responsavelCotacao == null) {
			responsavelCotacao = new UsuarioVO();
		}
		return (responsavelCotacao);
	}

	public void setResponsavelCotacao(UsuarioVO obj) {
		this.responsavelCotacao = obj;
	}

	public List<CotacaoFornecedorVO> getCotacaoFornecedorVOs() {
		if (cotacaoFornecedorVOs == null) {
			cotacaoFornecedorVOs = new ArrayList<>();
		}
		return (cotacaoFornecedorVOs);
	}

	public void setCotacaoFornecedorVOs(List<CotacaoFornecedorVO> cotacaoFornecedorVOs) {
		this.cotacaoFornecedorVOs = cotacaoFornecedorVOs;
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

	public Date getDataCotacao() {
		return (dataCotacao);
	}

	public String getDataCotacao_Apresentar() {
		if (dataCotacao == null) {
			return "";
		}
		return (Uteis.getDataComHora(dataCotacao));
	}

	public void setDataCotacao(Date dataCotacao) {
		this.dataCotacao = dataCotacao;
	}

	public Integer getCodigo() {
		return (codigo);
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public CategoriaProdutoVO getCategoriaProduto() {
		if (categoriaProduto == null) {
			categoriaProduto = new CategoriaProdutoVO();
		}
		return categoriaProduto;
	}

	public void setCategoriaProduto(CategoriaProdutoVO categoriaProduto) {
		this.categoriaProduto = categoriaProduto;
	}

	public List<UnidadeEnsinoVO> getListaUnidadeEnsinoVOs() {
		if (listaUnidadeEnsinoVOs == null) {
			listaUnidadeEnsinoVOs = new ArrayList<UnidadeEnsinoVO>(0);
		}
		return listaUnidadeEnsinoVOs;
	}

	public void setListaUnidadeEnsinoVOs(List<UnidadeEnsinoVO> listaUnidadeEnsinoVOs) {
		this.listaUnidadeEnsinoVOs = listaUnidadeEnsinoVOs;
	}

	public boolean isPermiteAlterarCotacao() {
		return isEmCotacao() || isRevisarCotacao();
	}

	public String getSituacao_Apresentar() {
		if (situacao == null) {
			situacao = "";
		}
		if (getSituacao().equals("EC")) {
			return "Em Cotação";
		}
		if (getSituacao().equals("AA")) {
			return "Aguardando Autorização";
		}
		if (getSituacao().equals("AU")) {
			return "Autorizado";
		}
		if (getSituacao().equals("RC")) {
			return "Revisar Cotação";
		}
		if (getSituacao().equals("IN")) {
			return "Indeferido";
		}
		return situacao;
	}

	public boolean isEmCotacao() {
		return getSituacao().equals("EC");
	}

	public boolean isRevisarCotacao() {
		return getSituacao().equals("RC");
	}

	public boolean isIndeferido() {
		return getSituacao().equals("IN");
	}

	public boolean isAguardandoAutorizacao() {
		return getSituacao().equals("AA");
	}

	public boolean isAutorizada() {
		return getSituacao().equals("AU");
	}

	public String getSituacao() {
		if (situacao == null) {
			situacao = "";
		}
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public String getMotivoRevisao() {
		if (motivoRevisao == null) {
			motivoRevisao = "";
		}
		return motivoRevisao;
	}

	public void setMotivoRevisao(String motivoRevisao) {
		this.motivoRevisao = motivoRevisao;
	}

	public CategoriaDespesaVO getCategoriaDespesa() {
		if (categoriaDespesa == null) {
			categoriaDespesa = new CategoriaDespesaVO();
		}
		return categoriaDespesa;
	}

	public void setCategoriaDespesa(CategoriaDespesaVO categoriaDespesa) {
		this.categoriaDespesa = categoriaDespesa;
	}

	public BigDecimal getValorTotalCompraCotacao() {
		return BigDecimal.valueOf(getCotacaoFornecedorVOs().stream().mapToDouble(p -> p.getItemCotacaoVOs().stream().filter(ItemCotacaoVO::getCompraAutorizadaFornecedor).mapToDouble(ItemCotacaoVO::getPrecoTotal).reduce(0D, (a,b) -> Uteis.arrendondarForcando2CadasDecimais(a + b))).reduce(0D, (a,b) -> Uteis.arrendondarForcando2CadasDecimais(a + b)));
	}
	
	public Double getQuantidadeTotalCompraCotacao() {
		return getCotacaoFornecedorVOs().stream().mapToDouble(p -> p.getItemCotacaoVOs().stream().filter(ItemCotacaoVO::getCompraAutorizadaFornecedor).mapToDouble(ItemCotacaoVO::getQuantidade).reduce(0D, (a,b) -> Uteis.arrendondarForcando2CadasDecimais(a + b))).reduce(0D, (a,b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));
	}

	public TramiteCotacaoCompraVO getTramiteCotacaoCompra() {
		tramiteCotacaoCompra = Optional.ofNullable(this.tramiteCotacaoCompra).orElse(new TramiteCotacaoCompraVO());

		return tramiteCotacaoCompra;
	}

	public void setTramiteCotacaoCompra(TramiteCotacaoCompraVO tramiteCotacaoCompra) {
		this.tramiteCotacaoCompra = tramiteCotacaoCompra;
	}

	public UnidadeEnsinoVO getUnidadeEnsinoResponsavelTramitacao() {
		unidadeEnsinoResponsavelTramitacao = Optional.ofNullable(this.unidadeEnsinoResponsavelTramitacao).orElse(new UnidadeEnsinoVO());
		return unidadeEnsinoResponsavelTramitacao;
	}

	public void setUnidadeEnsinoResponsavelTramitacao(UnidadeEnsinoVO unidadeEnsinoResponsavelTramitacao) {
		this.unidadeEnsinoResponsavelTramitacao = unidadeEnsinoResponsavelTramitacao;
	}

	public CursoVO getCursoCategoriaDespesa() {
		cursoCategoriaDespesa = Optional.ofNullable(this.cursoCategoriaDespesa).orElse(new CursoVO());
		return cursoCategoriaDespesa;
	}

	public void setCursoCategoriaDespesa(CursoVO cursoCategoriaDespesa) {
		this.cursoCategoriaDespesa = cursoCategoriaDespesa;
	}

	public UnidadeEnsinoVO getUnidadeEnsinoCategoriaDespesa() {
		unidadeEnsinoCategoriaDespesa = Optional.ofNullable(this.unidadeEnsinoCategoriaDespesa).orElse(new UnidadeEnsinoVO());
		return unidadeEnsinoCategoriaDespesa;
	}

	public void setUnidadeEnsinoCategoriaDespesa(UnidadeEnsinoVO unidadeEnsinoCategoriaDespesa) {
		this.unidadeEnsinoCategoriaDespesa = unidadeEnsinoCategoriaDespesa;
	}

	public DepartamentoVO getDepartamentoCategoriaDespesa() {
		departamentoCategoriaDespesa = Optional.ofNullable(this.departamentoCategoriaDespesa).orElse(new DepartamentoVO());
		return departamentoCategoriaDespesa;
	}

	public void setDepartamentoCategoriaDespesa(DepartamentoVO departamentoCategoriaDespesa) {
		this.departamentoCategoriaDespesa = departamentoCategoriaDespesa;
	}

	public FuncionarioVO getFuncionarioCategoriaDespesa() {
		funcionarioCategoriaDespesa = Optional.ofNullable(this.funcionarioCategoriaDespesa).orElse(new FuncionarioVO());
		return funcionarioCategoriaDespesa;
	}

	public void setFuncionarioCategoriaDespesa(FuncionarioVO funcionarioCategoriaDespesa) {
		this.funcionarioCategoriaDespesa = funcionarioCategoriaDespesa;
	}

	public TurnoVO getTurnoCategoriaDespesa() {
		turnoCategoriaDespesa = Optional.ofNullable(this.turnoCategoriaDespesa).orElse(new TurnoVO());
		return turnoCategoriaDespesa;
	}

	public void setTurnoCategoriaDespesa(TurnoVO turnoCategoriaDespesa) {
		this.turnoCategoriaDespesa = turnoCategoriaDespesa;
	}

	public TurmaVO getTurmaCategoriaDespesa() {
		turmaCategoriaDespesa = Optional.ofNullable(this.turmaCategoriaDespesa).orElse(new TurmaVO());
		return turmaCategoriaDespesa;
	}

	public void setTurmaCategoriaDespesa(TurmaVO turmaCategoriaDespesa) {
		this.turmaCategoriaDespesa = turmaCategoriaDespesa;
	}

	public CentroResultadoVO getCentroResultadoAdministrativo() {
		centroResultadoAdministrativo = Optional.ofNullable(centroResultadoAdministrativo).orElse(new CentroResultadoVO());
		return centroResultadoAdministrativo;
	}

	public void setCentroResultadoAdministrativo(CentroResultadoVO centroResultadoAdministrativo) {
		this.centroResultadoAdministrativo = centroResultadoAdministrativo;
	}	

	public List<CentroResultadoOrigemVO> getListaCentroResultadoOrigemVOs() {
		listaCentroResultadoOrigemVOs = Optional.ofNullable(listaCentroResultadoOrigemVOs).orElse(new ArrayList<>());
		return listaCentroResultadoOrigemVOs;
	}

	public void setListaCentroResultadoOrigemVOs(List<CentroResultadoOrigemVO> listaCentroResultadoOrigemVOs) {
		this.listaCentroResultadoOrigemVOs = listaCentroResultadoOrigemVOs;
	}

	public List<CotacaoHistoricoVO> getListaCotacaoHistoricoVOs() {
		listaCotacaoHistoricoVOs = Optional.ofNullable(listaCotacaoHistoricoVOs).orElse(new ArrayList<>());
		return listaCotacaoHistoricoVOs;
	}

	public void setListaCotacaoHistoricoVOs(List<CotacaoHistoricoVO> listaCotacaoHistoricoVOs) {
		this.listaCotacaoHistoricoVOs = listaCotacaoHistoricoVOs;
	}

	public CotacaoHistoricoVO getAtualCotacaoHistoricoVO() {
		atualCotacaoHistoricoVO = Optional.ofNullable(atualCotacaoHistoricoVO).orElse(new CotacaoHistoricoVO());
		return atualCotacaoHistoricoVO;
	}

	public void setAtualCotacaoHistoricoVO(CotacaoHistoricoVO atualCotacaoHistoricoVO) {
		this.atualCotacaoHistoricoVO = atualCotacaoHistoricoVO;
	}

	public Double getQuantidadeCentroResultadoTotal() {
		return getListaCentroResultadoOrigemVOs().stream().mapToDouble(CentroResultadoOrigemVO::getQuantidade).sum();
	}

	public Double getPorcentagemCentroResultadoTotal() {
		return getListaCentroResultadoOrigemVOs().stream().mapToDouble(CentroResultadoOrigemVO::getPorcentagem).sum();
	}
	
	public Double getPrecoCentroResultadoTotal() {
		return getListaCentroResultadoOrigemVOs().stream().mapToDouble(CentroResultadoOrigemVO::getValor).sum();
	}

	public boolean isExisteFornecedor() {
		return Uteis.isAtributoPreenchido(getCotacaoFornecedorVOs());
	}

	public boolean isCategoriaProdutoInformada() {
		return Uteis.isAtributoPreenchido(getCategoriaProduto());
	}

	public boolean isTramiteCotacaoCompraInformada() {
		return Uteis.isAtributoPreenchido(getTramiteCotacaoCompra());
	}

	public boolean isCategoriaDespesaInformada() {
		return Uteis.isAtributoPreenchido(getCategoriaDespesa());
	}
	
	public TipoNivelCentroResultadoEnum getTipoNivelCentroResultadoEnum() {		
		return tipoNivelCentroResultadoEnum;
	}

	public void setTipoNivelCentroResultadoEnum(TipoNivelCentroResultadoEnum tipoNivelCentroResultadoEnum) {
		this.tipoNivelCentroResultadoEnum = tipoNivelCentroResultadoEnum;
	}

}
