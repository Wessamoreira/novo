package negocio.comuns.secretaria;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;

public class TransferenciaTurnoVO extends SuperVO {

	private Integer codigo;
	public static final long serialVersionUID = 1L;
	private MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO;
	private MatriculaPeriodoVO matriculaPeriodoOrigem;
	private MatriculaPeriodoVO matriculaPeriodoDestino;
	private List<TransferenciaTurnoDisciplinaVO> transferenciaTurnoDisciplinaVOs;
	private UsuarioVO responsavel;
	private Date data;

	public TransferenciaTurnoVO() {
	}

	public void setMatriculaPeriodoTurmaDisciplinaVO(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO) {
		this.matriculaPeriodoTurmaDisciplinaVO = matriculaPeriodoTurmaDisciplinaVO;
	}

	public MatriculaPeriodoTurmaDisciplinaVO getMatriculaPeriodoTurmaDisciplinaVO() {
		if (matriculaPeriodoTurmaDisciplinaVO == null) {
			matriculaPeriodoTurmaDisciplinaVO = new MatriculaPeriodoTurmaDisciplinaVO();
		}
		return matriculaPeriodoTurmaDisciplinaVO;
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

	public MatriculaPeriodoVO getMatriculaPeriodoOrigem() {
		if (matriculaPeriodoOrigem == null) {
			matriculaPeriodoOrigem = new MatriculaPeriodoVO();
		}
		return matriculaPeriodoOrigem;
	}

	public void setMatriculaPeriodoOrigem(MatriculaPeriodoVO matriculaPeriodoOrigem) {
		this.matriculaPeriodoOrigem = matriculaPeriodoOrigem;
	}

	public MatriculaPeriodoVO getMatriculaPeriodoDestino() {
		if (matriculaPeriodoDestino == null) {
			matriculaPeriodoDestino = new MatriculaPeriodoVO();
		}
		return matriculaPeriodoDestino;
	}

	public void setMatriculaPeriodoDestino(MatriculaPeriodoVO matriculaPeriodoDestino) {
		this.matriculaPeriodoDestino = matriculaPeriodoDestino;
	}

	public List<TransferenciaTurnoDisciplinaVO> getTransferenciaTurnoDisciplinaVOs() {
		if (transferenciaTurnoDisciplinaVOs == null) {
			transferenciaTurnoDisciplinaVOs = new ArrayList<TransferenciaTurnoDisciplinaVO>(0);
		}
		return transferenciaTurnoDisciplinaVOs;
	}

	public void setTransferenciaTurnoDisciplinaVOs(List<TransferenciaTurnoDisciplinaVO> transferenciaTurnoDisciplinaVOs) {
		this.transferenciaTurnoDisciplinaVOs = transferenciaTurnoDisciplinaVOs;
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

	public Date getData() {
		if (data == null) {
			data = new Date();
		}
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getData_Apresentar() {
		return UteisData.getData(getData());
	}
}
