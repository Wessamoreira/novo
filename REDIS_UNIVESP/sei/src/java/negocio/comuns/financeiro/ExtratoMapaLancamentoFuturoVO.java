package negocio.comuns.financeiro;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.dominios.TipoMapaLancamentoFuturo;

public class ExtratoMapaLancamentoFuturoVO extends SuperVO {

	private static final long serialVersionUID = 1L;

	private Integer codigo;
	private ChequeVO chequeVO;
	private ContaCorrenteVO contaCorrenteCheque;
	private Date dataInicioApresentacao;
	private Date dataFimApresentacao;
	private TipoMapaLancamentoFuturo tipoMapaLancamentoFuturo;
	private UsuarioVO responsavel;

	public ExtratoMapaLancamentoFuturoVO() {

	}

	public ChequeVO getChequeVO() {
		if (chequeVO == null) {
			chequeVO = new ChequeVO();
		}
		return chequeVO;
	}

	public void setChequeVO(ChequeVO chequeVO) {
		this.chequeVO = chequeVO;
	}

	public Date getDataInicioApresentacao() {
		if (dataInicioApresentacao == null) {
			dataInicioApresentacao = new Date();
		}
		return dataInicioApresentacao;
	}

	public void setDataInicioApresentacao(Date dataInicioApresentacao) {
		this.dataInicioApresentacao = dataInicioApresentacao;
	}

	public Date getDataFimApresentacao() {
		return dataFimApresentacao;
	}

	public void setDataFimApresentacao(Date dataFimApresentacao) {
		this.dataFimApresentacao = dataFimApresentacao;
	}


	public UsuarioVO getResponsavel() {
		if (responsavel == null) {
			responsavel = new UsuarioVO();
		}
		return responsavel;
	}

	public void setResponsavel(UsuarioVO responsavel) {
		this.responsavel = responsavel;
	}

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public TipoMapaLancamentoFuturo getTipoMapaLancamentoFuturo() {
		if (tipoMapaLancamentoFuturo == null) {
			tipoMapaLancamentoFuturo = TipoMapaLancamentoFuturo.CHEQUE_A_RECEBER;
		}
		return tipoMapaLancamentoFuturo;
	}

	public void setTipoMapaLancamentoFuturo(TipoMapaLancamentoFuturo tipoMapaLancamentoFuturo) {
		this.tipoMapaLancamentoFuturo = tipoMapaLancamentoFuturo;
	}

	public ContaCorrenteVO getContaCorrenteCheque() {
		if (contaCorrenteCheque == null) {
			contaCorrenteCheque = new ContaCorrenteVO();
		}
		return contaCorrenteCheque;
	}

	public void setContaCorrenteCheque(ContaCorrenteVO contaCorrenteCheque) {
		this.contaCorrenteCheque = contaCorrenteCheque;
	}
}
