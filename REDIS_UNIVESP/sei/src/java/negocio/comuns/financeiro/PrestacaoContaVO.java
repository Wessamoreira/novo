package negocio.comuns.financeiro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.enumerador.TipoPrestacaoContaEnum;
import negocio.comuns.utilitarias.Uteis;

public class PrestacaoContaVO extends SuperVO implements Serializable {

	private static final long serialVersionUID = -7107657532590054578L;

	private Integer codigo;
	private Date dataCadastro;
	private UsuarioVO responsavelCadastro;
	private Date dataAlteracao;
	private UsuarioVO responsavelAlteracao;
	private String descricao;
	private TurmaVO turma;
	private UnidadeEnsinoVO unidadeEnsino;
	private List<ItemPrestacaoContaCategoriaDespesaVO> itemPrestacaoContaCategoriaDespesaVOs;
	private List<ItemPrestacaoContaCategoriaDespesaVO> itemPrestacaoContaCategoriaDespesaContaReceberVOs;
	private List<ItemPrestacaoContaOrigemContaReceberVO> itemPrestacaoContaOrigemContaReceberVOs;
	private List<ItemPrestacaoContaTurmaVO> itemPrestacaoContaTurmaVOs;
	private TipoPrestacaoContaEnum tipoPrestacaoConta;
	private Double valorTotalPagamento;
	private Double valorTotalRecebimento;
	private Double valorTotalPrestacaoContaTurma;
	private Double saldoAntesAlteracao;
	private Date dataCompetencia;
	private Double saldoAnterior;
	private Double saldoFinal;

	public void calcularValorTotalItemPrestacaoContaCategoriaDespesaVO() {
	    	setValorTotalPagamento(getItemPrestacaoContaCategoriaDespesaVOs().stream().map(p -> p.getValorTotalPrestacaoConta()).reduce(0D, (a,b) -> Uteis.arrendondarForcando2CadasDecimais(a + b)));
	    }

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public Date getDataCadastro() {
		if (dataCadastro == null) {
			dataCadastro = new Date();
		}
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
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

	public Date getDataAlteracao() {
		return dataAlteracao;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

	public UsuarioVO getResponsavelAlteracao() {
		if (responsavelAlteracao == null) {
			responsavelAlteracao = new UsuarioVO();
		}
		return responsavelAlteracao;
	}

	public void setResponsavelAlteracao(UsuarioVO responsavelAlteracao) {
		this.responsavelAlteracao = responsavelAlteracao;
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

	public TurmaVO getTurma() {
		if (turma == null) {
			turma = new TurmaVO();
		}
		return turma;
	}

	public void setTurma(TurmaVO turma) {
		this.turma = turma;
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

	public List<ItemPrestacaoContaCategoriaDespesaVO> getItemPrestacaoContaCategoriaDespesaVOs() {
		if (itemPrestacaoContaCategoriaDespesaVOs == null) {
			itemPrestacaoContaCategoriaDespesaVOs = new ArrayList<>(0);
		}
		return itemPrestacaoContaCategoriaDespesaVOs;
	}

	public void setItemPrestacaoContaCategoriaDespesaVOs(
			List<ItemPrestacaoContaCategoriaDespesaVO> itemPrestacaoContaCategoriaDespesaVOs) {
		this.itemPrestacaoContaCategoriaDespesaVOs = itemPrestacaoContaCategoriaDespesaVOs;
	}

	public List<ItemPrestacaoContaOrigemContaReceberVO> getItemPrestacaoContaOrigemContaReceberVOs() {
		if (itemPrestacaoContaOrigemContaReceberVOs == null) {
			itemPrestacaoContaOrigemContaReceberVOs = new ArrayList<>(0);
		}
		return itemPrestacaoContaOrigemContaReceberVOs;
	}

	public void setItemPrestacaoContaOrigemContaReceberVOs(
			List<ItemPrestacaoContaOrigemContaReceberVO> itemPrestacaoOrigemContaReceberVOs) {
		this.itemPrestacaoContaOrigemContaReceberVOs = itemPrestacaoOrigemContaReceberVOs;
	}

	public List<ItemPrestacaoContaTurmaVO> getItemPrestacaoContaTurmaVOs() {
		if (itemPrestacaoContaTurmaVOs == null) {
			itemPrestacaoContaTurmaVOs = new ArrayList<ItemPrestacaoContaTurmaVO>(0);
		}
		return itemPrestacaoContaTurmaVOs;
	}

	public void setItemPrestacaoContaTurmaVOs(List<ItemPrestacaoContaTurmaVO> itemPrestacaoContaTurmaVOs) {
		this.itemPrestacaoContaTurmaVOs = itemPrestacaoContaTurmaVOs;
	}

	public TipoPrestacaoContaEnum getTipoPrestacaoConta() {
		if (tipoPrestacaoConta == null) {
			tipoPrestacaoConta = TipoPrestacaoContaEnum.TURMA;
		}
		return tipoPrestacaoConta;
	}

	public void setTipoPrestacaoConta(TipoPrestacaoContaEnum tipoPrestacaoConta) {
		this.tipoPrestacaoConta = tipoPrestacaoConta;
	}

	public Double getValorTotalRecebimento() {
		if (valorTotalRecebimento == null) {
			valorTotalRecebimento = 0.0;
		}
		return valorTotalRecebimento;
	}

	public void setValorTotalRecebimento(Double valorTotalRecebimento) {
		this.valorTotalRecebimento = valorTotalRecebimento;
	}

	public Double getValorTotalPagamento() {
		
		if (valorTotalPagamento == null) {
			valorTotalPagamento = 0.0;
		}
		
		return valorTotalPagamento ;
	}
	
	public void setValorTotalPagamento(Double valorTotalPagamento) {
		this.valorTotalPagamento = valorTotalPagamento;
	}

	public Double getValorTotalPrestacaoContaTurma() {
		if (valorTotalPrestacaoContaTurma == null) {
			valorTotalPrestacaoContaTurma = 0.0;
		}
		return valorTotalPrestacaoContaTurma;
	}

	public void setValorTotalPrestacaoContaTurma(Double valorTotalPrestacaoContaTurma) {
		this.valorTotalPrestacaoContaTurma = valorTotalPrestacaoContaTurma;
	}

	public Double getSaldo() {
		return getValorTotalRecebimento() + getValorTotalPrestacaoContaTurma() - getValorTotalPagamento();
	}

	public Double getSaldoAntesAlteracao() {
		if (saldoAntesAlteracao == null) {
			saldoAntesAlteracao = 0.0;
		}
		return saldoAntesAlteracao;
	}

	public void setSaldoAntesAlteracao(Double saldoAntesAlteracao) {
		this.saldoAntesAlteracao = saldoAntesAlteracao;
	}

	public Date getDataCompetencia() {
		return dataCompetencia;
	}

	public void setDataCompetencia(Date dataCompetencia) {
		this.dataCompetencia = dataCompetencia;
	}

	public Double getSaldoAnterior() {
		if (saldoAnterior == null) {
			saldoAnterior = 0.0;
		}
		return saldoAnterior;
	}

	public void setSaldoAnterior(Double saldoAnterior) {
		this.saldoAnterior = saldoAnterior;
	}

	public Double getSaldoFinal() {
		if (saldoFinal == null) {
			saldoFinal = 0.0;
		}
		return saldoFinal;
	}

	public void setSaldoFinal(Double saldoFinal) {
		this.saldoFinal = saldoFinal;
	}
	
}
