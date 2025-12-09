package negocio.comuns.protocolo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.TipoTransferenciaTurmaEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;

public class TransferenciaTurmaVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private TurmaVO turmaOrigem;
	private TurmaVO turmaDestino;
	private MatriculaVO matriculaVO;
	private DisciplinaVO disciplinaVO;
	private String observacao;
	private Date data;
	private UsuarioVO usuarioVO;
	private MatriculaPeriodoVO ultimaMatriculaPeriodoAtiva;
	private Integer qtdeHorasRegistrarAbono;
	/**
	 * Transient
	 */
	private MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO;
	private List<SelectItem> listaSelectItemTurma;
	private Boolean realizarAbonoRegistroAula;
	private TipoTransferenciaTurmaEnum tipoTransferenciaTurma;
	/**
	 * Fim Transient
	 */
	private TurmaVO turmaPraticaOrigem;
	private TurmaVO turmaPraticaDestino;
	private TurmaVO turmaTeoricaOrigem;
	private TurmaVO turmaTeoricaDestino;
	private List<SelectItem> listaSelectItemTurmaPratica;
	private List<SelectItem> listaSelectItemTurmaTeorica;
	private Integer qtdeHorasRegistrarAbonoTurmaTeorica;
	private Integer qtdeHorasRegistrarAbonoTurmaPratica;

	/**
	 * Fim Transient
	 */

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public TurmaVO getTurmaOrigem() {
		if (turmaOrigem == null) {
			turmaOrigem = new TurmaVO();
		}
		return turmaOrigem;
	}

	public void setTurmaOrigem(TurmaVO turmaOrigem) {
		this.turmaOrigem = turmaOrigem;
	}

	public TurmaVO getTurmaDestino() {
		if (turmaDestino == null) {
			turmaDestino = new TurmaVO();
		}
		return turmaDestino;
	}

	public void setTurmaDestino(TurmaVO turmaDestino) {
		this.turmaDestino = turmaDestino;
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

	public DisciplinaVO getDisciplinaVO() {
		if (disciplinaVO == null) {
			disciplinaVO = new DisciplinaVO();
		}
		return disciplinaVO;
	}

	public void setDisciplinaVO(DisciplinaVO disciplinaVO) {
		this.disciplinaVO = disciplinaVO;
	}

	public String getObservacao() {
		if (observacao == null) {
			observacao = "";
		}
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public Date getData() {
		if (data == null) {
			data = new Date();
		}
		return data;
	}

	public String getData_Apresentar() {
		return (Uteis.getData(data, "dd/MM/yyyy HH:mm"));
	}

	public void setData(Date data) {
		this.data = data;
	}

	public UsuarioVO getUsuarioVO() {
		if (usuarioVO == null) {
			usuarioVO = new UsuarioVO();
		}
		return usuarioVO;
	}

	public void setUsuarioVO(UsuarioVO usuarioVO) {
		this.usuarioVO = usuarioVO;
	}

	public MatriculaPeriodoVO getUltimaMatriculaPeriodoAtiva() {
		if (ultimaMatriculaPeriodoAtiva == null) {
			ultimaMatriculaPeriodoAtiva = new MatriculaPeriodoVO();
		}
		return ultimaMatriculaPeriodoAtiva;
	}

	public void setUltimaMatriculaPeriodoAtiva(MatriculaPeriodoVO ultimaMatriculaPeriodoAtiva) {
		this.ultimaMatriculaPeriodoAtiva = ultimaMatriculaPeriodoAtiva;
	}

	public Integer getQtdeHorasRegistrarAbono() {
		if (qtdeHorasRegistrarAbono == null) {
			qtdeHorasRegistrarAbono = 0;
		}
		return qtdeHorasRegistrarAbono;
	}

	public void setQtdeHorasRegistrarAbono(Integer qtdeHorasRegistrarAbono) {
		this.qtdeHorasRegistrarAbono = qtdeHorasRegistrarAbono;
	}

	public List<SelectItem> getListaSelectItemTurma() {
		if (listaSelectItemTurma == null) {
			listaSelectItemTurma = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemTurma;
	}

	public void setListaSelectItemTurma(List<SelectItem> listaSelectItemTurma) {
		this.listaSelectItemTurma = listaSelectItemTurma;
	}

	public Boolean getExisteRegistroAula() {
		return getQtdeHorasRegistrarAbono() > 0;
	}

	public Boolean getRealizarAbonoRegistroAula() {
		if (realizarAbonoRegistroAula == null) {
			realizarAbonoRegistroAula = false;
		}
		return realizarAbonoRegistroAula;
	}

	public void setRealizarAbonoRegistroAula(Boolean realizarAbonoRegistroAula) {
		this.realizarAbonoRegistroAula = realizarAbonoRegistroAula;
	}

	public MatriculaPeriodoTurmaDisciplinaVO getMatriculaPeriodoTurmaDisciplinaVO() {
		if (matriculaPeriodoTurmaDisciplinaVO == null) {
			matriculaPeriodoTurmaDisciplinaVO = new MatriculaPeriodoTurmaDisciplinaVO();
		}
		return matriculaPeriodoTurmaDisciplinaVO;
	}

	public void setMatriculaPeriodoTurmaDisciplinaVO(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO) {
		this.matriculaPeriodoTurmaDisciplinaVO = matriculaPeriodoTurmaDisciplinaVO;
	}

	/**
	 * @return the tipoTransferenciaTurma
	 */
	public TipoTransferenciaTurmaEnum getTipoTransferenciaTurma() {
		if (tipoTransferenciaTurma == null) {
			tipoTransferenciaTurma = TipoTransferenciaTurmaEnum.TURMA_BASE;
		}
		return tipoTransferenciaTurma;
	}

	/**
	 * @param tipoTransferenciaTurma
	 *            the tipoTransferenciaTurma to set
	 */
	public void setTipoTransferenciaTurma(TipoTransferenciaTurmaEnum tipoTransferenciaTurma) {
		this.tipoTransferenciaTurma = tipoTransferenciaTurma;
	}

	public TurmaVO getTurmaPraticaOrigem() {
		if (turmaPraticaOrigem == null) {
			turmaPraticaOrigem = new TurmaVO();
		}
		return turmaPraticaOrigem;
	}

	public void setTurmaPraticaOrigem(TurmaVO turmaPraticaOrigem) {
		this.turmaPraticaOrigem = turmaPraticaOrigem;
	}

	public TurmaVO getTurmaPraticaDestino() {
		if (turmaPraticaDestino == null) {
			turmaPraticaDestino = new TurmaVO();
		}
		return turmaPraticaDestino;
	}

	public void setTurmaPraticaDestino(TurmaVO turmaPraticaDestino) {
		this.turmaPraticaDestino = turmaPraticaDestino;
	}

	public TurmaVO getTurmaTeoricaOrigem() {
		if (turmaTeoricaOrigem == null) {
			turmaTeoricaOrigem = new TurmaVO();
		}
		return turmaTeoricaOrigem;
	}

	public void setTurmaTeoricaOrigem(TurmaVO turmaTeoricaOrigem) {
		this.turmaTeoricaOrigem = turmaTeoricaOrigem;
	}

	public TurmaVO getTurmaTeoricaDestino() {
		if (turmaTeoricaDestino == null) {
			turmaTeoricaDestino = new TurmaVO();
		}
		return turmaTeoricaDestino;
	}

	public void setTurmaTeoricaDestino(TurmaVO turmaTeoricaDestino) {
		this.turmaTeoricaDestino = turmaTeoricaDestino;
	}

	public List<SelectItem> getListaSelectItemTurmaTeorica() {
		if (listaSelectItemTurmaTeorica == null) {
			listaSelectItemTurmaTeorica = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemTurmaTeorica;
	}

	public void setListaSelectItemTurmaTeorica(List<SelectItem> listaSelectItemTurmaTeorica) {
		this.listaSelectItemTurmaTeorica = listaSelectItemTurmaTeorica;
	}

	public List<SelectItem> getListaSelectItemTurmaPratica() {
		if (listaSelectItemTurmaPratica == null) {
			listaSelectItemTurmaPratica = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemTurmaPratica;
	}

	public void setListaSelectItemTurmaPratica(List<SelectItem> listaSelectItemTurmaPratica) {
		this.listaSelectItemTurmaPratica = listaSelectItemTurmaPratica;
	}

	public boolean isExisteRegistroAulaTurmaTeorica() {
		return getQtdeHorasRegistrarAbonoTurmaTeorica() > 0;
	}

	public boolean isExisteRegistroAulaTurmaPratica() {
		return getQtdeHorasRegistrarAbonoTurmaPratica() > 0;
	}

	public Integer getQtdeHorasRegistrarAbonoTurmaTeorica() {
		if (qtdeHorasRegistrarAbonoTurmaTeorica == null) {
			qtdeHorasRegistrarAbonoTurmaTeorica = 0;
		}
		return qtdeHorasRegistrarAbonoTurmaTeorica;
	}

	public void setQtdeHorasRegistrarAbonoTurmaTeorica(Integer qtdeHorasRegistrarAbonoTurmaTeorica) {
		this.qtdeHorasRegistrarAbonoTurmaTeorica = qtdeHorasRegistrarAbonoTurmaTeorica;
	}

	public Integer getQtdeHorasRegistrarAbonoTurmaPratica() {
		if (qtdeHorasRegistrarAbonoTurmaPratica == null) {
			qtdeHorasRegistrarAbonoTurmaPratica = 0;
		}
		return qtdeHorasRegistrarAbonoTurmaPratica;
	}

	public void setQtdeHorasRegistrarAbonoTurmaPratica(Integer qtdeHorasRegistrarAbonoTurmaPratica) {
		this.qtdeHorasRegistrarAbonoTurmaPratica = qtdeHorasRegistrarAbonoTurmaPratica;
	}

}
