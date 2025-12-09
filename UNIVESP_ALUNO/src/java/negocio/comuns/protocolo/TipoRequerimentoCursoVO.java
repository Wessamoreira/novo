package negocio.comuns.protocolo;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.arquitetura.SuperVO;

/**
 * 
 * @author Leonardo Riciolle 28/04/2015
 */
public class TipoRequerimentoCursoVO extends SuperVO {

	private static final long serialVersionUID = 1L;

	private Integer codigo;
	private Integer tipoRequerimento;
	private CursoVO cursoVO;
	private List<TipoRequerimentoTurmaVO> listaTipoRequerimentoTurmaVOs;
	private List<TipoRequerimentoCursoTransferenciaInternaCursoVO>  listaTipoRequerimentoTransferenciaCursoVOs ;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public Integer getTipoRequerimento() {
		if (tipoRequerimento == null) {
			tipoRequerimento = 0;
		}
		return tipoRequerimento;
	}

	public void setTipoRequerimento(Integer tipoRequerimento) {
		this.tipoRequerimento = tipoRequerimento;
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
	
	public List<TipoRequerimentoTurmaVO> getListaTipoRequerimentoTurmaVOs() {
		if (listaTipoRequerimentoTurmaVOs == null) {
			listaTipoRequerimentoTurmaVOs = new ArrayList<TipoRequerimentoTurmaVO>(0);
		}
		return listaTipoRequerimentoTurmaVOs;
	}

	public void setListaTipoRequerimentoTurmaVOs(List<TipoRequerimentoTurmaVO> listaTipoRequerimentoTurmaVOs) {
		this.listaTipoRequerimentoTurmaVOs = listaTipoRequerimentoTurmaVOs;
	}

	public List<TipoRequerimentoCursoTransferenciaInternaCursoVO> getListaTipoRequerimentoTransferenciaCursoVOs() {
		if(listaTipoRequerimentoTransferenciaCursoVOs == null ) {
			listaTipoRequerimentoTransferenciaCursoVOs = new ArrayList<TipoRequerimentoCursoTransferenciaInternaCursoVO>(0);
		}
		return listaTipoRequerimentoTransferenciaCursoVOs;
	}

	public void setListaTipoRequerimentoTransferenciaCursoVOs(List<TipoRequerimentoCursoTransferenciaInternaCursoVO> listaTipoRequerimentoTransferenciaCursoVOs) {
		this.listaTipoRequerimentoTransferenciaCursoVOs = listaTipoRequerimentoTransferenciaCursoVOs;
	}
}
