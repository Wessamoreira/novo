package negocio.comuns.compras;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.financeiro.CentroResultadoVO;
import negocio.comuns.financeiro.enumerador.TipoNivelCentroResultadoEnum;
import negocio.comuns.utilitarias.Uteis;

public class CompraItemVO extends SuperVO {

	private Integer codigo;
	private CompraVO compra;
	private Double quantidadeRequisicao;
	private Double quantidadeAdicional;
	private Double precoUnitario;
	private Double quantidadeRecebida;
	private ProdutoServicoVO produto;
	private CategoriaDespesaVO categoriaDespesa;
	private TipoNivelCentroResultadoEnum tipoNivelCentroResultadoEnum;
	private DepartamentoVO departamentoVO;
	private CursoVO cursoVO;
	private TurnoVO turnoVO;
	private TurmaVO turma;
	private CentroResultadoVO centroResultadoAdministrativo;
	private List<RequisicaoItemVO> listaRequisicaoItem;
	public static final long serialVersionUID = 1L;

	public CompraItemVO() {

	}

	public CompraItemVO(CategoriaDespesaVO categoriaDespesa, CentroResultadoVO centroResultadoAdministrativo, TipoNivelCentroResultadoEnum tipoNivelCentroResultadoEnum, DepartamentoVO departamento, CursoVO cursoVO, TurnoVO turnoVO, TurmaVO turma) {
		this.categoriaDespesa = categoriaDespesa;
		this.departamentoVO = departamento;
		this.centroResultadoAdministrativo= centroResultadoAdministrativo;
		this.tipoNivelCentroResultadoEnum= tipoNivelCentroResultadoEnum;
		this.cursoVO = cursoVO;
		this.turnoVO = turnoVO;
		this.turma = turma;
	}

	public static void validarDados(CompraItemVO obj) {
		if (!obj.isValidarDados()) {
			return;
		}
		if(Uteis.isAtributoPreenchido(obj.getQuantidadeAdicional())){
			Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getCategoriaDespesa()), "O campo Categoria de Despesa (Itens da Compra) deve ser informado.");
			Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getTipoNivelCentroResultadoEnum()), "O campo Centro Resultado Para (Itens da Compra) deve ser informado.");
			Uteis.checkState(obj.getTipoNivelCentroResultadoEnum().isDepartamento() && !Uteis.isAtributoPreenchido(obj.getDepartamentoVO()), "O campo Departamento (Itens da Compra) deve ser informado.");
			Uteis.checkState(obj.getTipoNivelCentroResultadoEnum().isCurso() && !Uteis.isAtributoPreenchido(obj.getCursoVO()), "O campo Curso (Itens da Compra) deve ser informado.");
			Uteis.checkState(obj.getTipoNivelCentroResultadoEnum().isCursoTurno() && !Uteis.isAtributoPreenchido(obj.getTurnoVO()), "O campo Turno (Itens da Compra) deve ser informado.");
			Uteis.checkState(obj.getTipoNivelCentroResultadoEnum().isTurma() && !Uteis.isAtributoPreenchido(obj.getTurma()), "O campo Turma (Itens da Compra) deve ser informado.");
			Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getCentroResultadoAdministrativo()), "O campo Centro Resultado (Itens da Compra) deve ser informado.");
		}
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getProduto()), "O campo Produto (Itens da Compra) deve ser informado.");
		Uteis.checkState(obj.getQuantidade().doubleValue() == 0.0, "O campo QUANTIDADE (Itens da Compra) deve ser informado.");
		Uteis.checkState(obj.getPrecoUnitario().doubleValue() == 0.0, "O campo PREÇO UNITÁRIO (Itens da Compra) deve ser informado.");
	}
	
	public void limparCamposPorTipoNivelCentroResultadoEnum() {
		switch (getTipoNivelCentroResultadoEnum()) {
		case CURSO:
			setTurma(new TurmaVO());
			setTurnoVO(new TurnoVO());
			if(!getCategoriaDespesa().getExigeCentroCustoRequisitante()){
				setDepartamentoVO(new DepartamentoVO());	
			}
			break;
		case CURSO_TURNO:
			setCursoVO(new CursoVO());
			setTurma(new TurmaVO());			
			if(!getCategoriaDespesa().getExigeCentroCustoRequisitante()){
				setDepartamentoVO(new DepartamentoVO());	
			}
			break;
		case TURMA:
			setCursoVO(new CursoVO());
			setTurnoVO(new TurnoVO());			
			if(!getCategoriaDespesa().getExigeCentroCustoRequisitante()){
				setDepartamentoVO(new DepartamentoVO());	
			}
			break;
		case DEPARTAMENTO:
			setCursoVO(new CursoVO());
			setTurnoVO(new TurnoVO());
			setTurma(new TurmaVO());			
			break;
		case UNIDADE_ENSINO:
			setCursoVO(new CursoVO());
			setTurnoVO(new TurnoVO());
			setTurma(new TurmaVO());
			if(!getCategoriaDespesa().getExigeCentroCustoRequisitante()){
				setDepartamentoVO(new DepartamentoVO());	
			}
			break;
		}
	}

	

	public ProdutoServicoVO getProduto() {
		if (produto == null) {
			produto = new ProdutoServicoVO();
		}
		return (produto);
	}

	public void setProduto(ProdutoServicoVO obj) {
		this.produto = obj;
	}

	public Double getQuantidadeRecebida() {
		if (quantidadeRecebida == null) {
			quantidadeRecebida = 0.0;
		}
		return (quantidadeRecebida);
	}

	public void setQuantidadeRecebida(Double quantidadeRecebida) {
		this.quantidadeRecebida = quantidadeRecebida;
	}

	public Double getPrecoUnitario() {
		if (precoUnitario == null) {
			precoUnitario = 0.0;
		}
		return (precoUnitario);
	}

	public void setPrecoUnitario(Double precoUnitario) {
		this.precoUnitario = precoUnitario;
	}

	public Double getQuantidade() {
		return getQuantidadeRequisicao() + getQuantidadeAdicional();
	}	
	
	
	public CompraVO getCompra() {
		compra = Optional.ofNullable(compra).orElse(new CompraVO());
		return compra;
	}

	public void setCompra(CompraVO compra) {
		this.compra = compra;
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

	public Double getPrecoAdicionalTotal() {
		return Uteis.arrendondarForcando2CadasDecimais(getQuantidadeAdicional() * getPrecoUnitario());
	}
	
	public Double getPrecoTotal() {
		return Uteis.arrendondarForcando2CadasDecimais(getQuantidade() * getPrecoUnitario());
	}
	
	

	public Double getQuantidadeRequisicao() {
		quantidadeRequisicao = Optional.ofNullable(quantidadeRequisicao).orElse(0.0);
		return quantidadeRequisicao;
	}

	public void setQuantidadeRequisicao(Double quantidadeRequisicao) {
		this.quantidadeRequisicao = quantidadeRequisicao;
	}

	public Double getQuantidadeAdicional() {
		quantidadeAdicional = Optional.ofNullable(quantidadeAdicional).orElse(0.0);
		return quantidadeAdicional;
	}

	public void setQuantidadeAdicional(Double quantidadeAdicional) {
		this.quantidadeAdicional = quantidadeAdicional;
	}

	public CategoriaDespesaVO getCategoriaDespesa() {
		if (categoriaDespesa == null) {
			categoriaDespesa = new CategoriaDespesaVO();
		}
		return categoriaDespesa;
	}

	public void setCategoriaDespesa(CategoriaDespesaVO centroDespesa) {
		this.categoriaDespesa = centroDespesa;
	}

	public CursoVO getCursoVO() {
		if (cursoVO == null) {
			cursoVO = new CursoVO();
		}
		return cursoVO;
	}

	public void setCursoVO(CursoVO cursoVO) {
		this.cursoVO = cursoVO;
	}

	public TurnoVO getTurnoVO() {
		if (turnoVO == null) {
			turnoVO = new TurnoVO();
		}
		return turnoVO;
	}

	public void setTurnoVO(TurnoVO turnoVO) {
		this.turnoVO = turnoVO;
	}

	public TurmaVO getTurma() {
		if (turma == null) {
			turma = new TurmaVO();
		}
		return turma;
	}

	public void setTurma(TurmaVO turma) {
		this.turma = turma;
	}

	public DepartamentoVO getDepartamentoVO() {
		departamentoVO = Optional.ofNullable(departamentoVO).orElse(new DepartamentoVO());
		return departamentoVO;
	}

	public void setDepartamentoVO(DepartamentoVO departamentoVO) {
		this.departamentoVO = departamentoVO;
	}

	public CentroResultadoVO getCentroResultadoAdministrativo() {
		centroResultadoAdministrativo = Optional.ofNullable(centroResultadoAdministrativo).orElse(new CentroResultadoVO());
		return centroResultadoAdministrativo;
	}

	public void setCentroResultadoAdministrativo(CentroResultadoVO centroResultadoAdministrativo) {
		this.centroResultadoAdministrativo = centroResultadoAdministrativo;
	}	

	public TipoNivelCentroResultadoEnum getTipoNivelCentroResultadoEnum() {
		return tipoNivelCentroResultadoEnum;
	}

	public void setTipoNivelCentroResultadoEnum(TipoNivelCentroResultadoEnum tipoNivelCentroResultadoEnum) {
		this.tipoNivelCentroResultadoEnum = tipoNivelCentroResultadoEnum;
	}

	public List<RequisicaoItemVO> getListaRequisicaoItem() {
		listaRequisicaoItem = Optional.ofNullable(listaRequisicaoItem).orElse(new ArrayList<>());
		return listaRequisicaoItem;
	}

	public void setListaRequisicaoItem(List<RequisicaoItemVO> listaRequisicaoItem) {
		this.listaRequisicaoItem = listaRequisicaoItem;
	}
	
	public boolean isCategoriaDespesaInformada(){
		return Uteis.isAtributoPreenchido(getCategoriaDespesa());
	}
	
	public boolean isExibirCampoCentroResultado(){
		return isCategoriaDespesaInformada() && Uteis.isAtributoPreenchido(getQuantidadeAdicional());
	}

}
