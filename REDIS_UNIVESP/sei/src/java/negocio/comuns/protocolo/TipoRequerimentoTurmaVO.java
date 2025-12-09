package negocio.comuns.protocolo;

import com.google.gson.annotations.Expose;

import negocio.comuns.academico.TurmaVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.annotation.ExcluirJsonAnnotation;

/**
 * 
 * @author Leonardo Riciolle 28/04/2015
 */
public class TipoRequerimentoTurmaVO extends SuperVO {

	private static final long serialVersionUID = 1L;

	private Integer codigo;
	@ExcluirJsonAnnotation
    @Expose(deserialize = false, serialize = false)
	private TipoRequerimentoCursoVO tipoRequerimentoCursoVO;
	private TurmaVO turmaVO;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public TipoRequerimentoCursoVO getTipoRequerimentoCursoVO() {
		if (tipoRequerimentoCursoVO == null) {
			tipoRequerimentoCursoVO = new TipoRequerimentoCursoVO();
		}
		return tipoRequerimentoCursoVO;
	}

	public void setTipoRequerimentoCursoVO(TipoRequerimentoCursoVO tipoRequerimentoCursoVO) {
		this.tipoRequerimentoCursoVO = tipoRequerimentoCursoVO;
	}

	public TurmaVO getTurmaVO() {
		if (turmaVO == null) {
			turmaVO = new TurmaVO();
		}
		return turmaVO;
	}

	public void setTurmaVO(TurmaVO turmaVO) {
		this.turmaVO = turmaVO;
	}

	
}
