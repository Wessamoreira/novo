package negocio.comuns.secretaria;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.arquitetura.SuperVO;

public class TransferenciaTurnoDisciplinaVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private Integer transferenciaTurno;
	private MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO;
	private Integer qtdeHorasRegistrarAbono;
	/**
	 * Transient utilizado na Trasferencia de Turno
	 */
	private List<SelectItem> listaSelectItemTurma;
	private Boolean apresentarMensagemQtdeHorasAbono;
	private Boolean existeRegistroAula;
	private Boolean realizarAbonoRegistroAula;
	private TurmaVO turmaAntiga;
	private TurmaVO turmaTransferida;
	private TurmaVO turmaPraticaAntiga;
	private TurmaVO turmaPraticaTransferida;
	private TurmaVO turmaTeoricaAntiga;
	private TurmaVO turmaTeoricaTransferida;
	private List<SelectItem> listaSelectItemTurmaPratica;
	private List<SelectItem> listaSelectItemTurmaTeorica;
	private Boolean existeRegistroAulaPratica;
	private Boolean existeRegistroAulaTeorica;
	private Integer qtdeHorasAbonoTurmaPratica;
	private Integer qtdeHorasAbonoTurmaTeorica;

	/**
	 * Fim Transient
	 * 
	 * @author Wellington Rodrigues - 06/03/2015
	 * @return
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

	public Integer getTransferenciaTurno() {
		if (transferenciaTurno == null) {
			transferenciaTurno = 0;
		}
		return transferenciaTurno;
	}

	public void setTransferenciaTurno(Integer transferenciaTurno) {
		this.transferenciaTurno = transferenciaTurno;
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

	public List<SelectItem> getListaSelectItemTurma() {
		if (listaSelectItemTurma == null) {
			listaSelectItemTurma = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemTurma;
	}

	public void setListaSelectItemTurma(List<SelectItem> listaSelectItemTurma) {
		this.listaSelectItemTurma = listaSelectItemTurma;
	}

	public Boolean getApresentarMensagemQtdeHorasAbono() {
		if (apresentarMensagemQtdeHorasAbono == null) {
			apresentarMensagemQtdeHorasAbono = false;
		}
		return apresentarMensagemQtdeHorasAbono;
	}

	public void setApresentarMensagemQtdeHorasAbono(Boolean apresentarMensagemQtdeHorasAbono) {
		this.apresentarMensagemQtdeHorasAbono = apresentarMensagemQtdeHorasAbono;
	}

	public Boolean getExisteRegistroAula() {
		if (existeRegistroAula == null) {
			existeRegistroAula = false;
		}
		return existeRegistroAula;
	}

	public void setExisteRegistroAula(Boolean existeRegistroAula) {
		this.existeRegistroAula = existeRegistroAula;
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

	public Boolean getRealizarAbonoRegistroAula() {
		if (realizarAbonoRegistroAula == null) {
			realizarAbonoRegistroAula = false;
		}
		return realizarAbonoRegistroAula;
	}

	public void setRealizarAbonoRegistroAula(Boolean realizarAbonoRegistroAula) {
		this.realizarAbonoRegistroAula = realizarAbonoRegistroAula;
	}
	
	public TurmaVO getTurmaAntiga() {
		if(turmaAntiga == null){
			turmaAntiga = new TurmaVO();
		}
		return turmaAntiga;
	}

	public void setTurmaAntiga(TurmaVO turmaAntiga) {
		this.turmaAntiga = turmaAntiga;
	}

	public TurmaVO getTurmaTransferida() {
		if(turmaTransferida == null){
			turmaTransferida = new TurmaVO();
		}
		return turmaTransferida;
	}

	public void setTurmaTransferida(TurmaVO turmaTransferida) {
		this.turmaTransferida = turmaTransferida;
	}

	public TurmaVO getTurmaPraticaAntiga() {
		if (turmaPraticaAntiga == null) {
			turmaPraticaAntiga = new TurmaVO();
		}
		return turmaPraticaAntiga;
	}

	public void setTurmaPraticaAntiga(TurmaVO turmaPraticaAntiga) {
		this.turmaPraticaAntiga = turmaPraticaAntiga;
	}

	public TurmaVO getTurmaPraticaTransferida() {
		if (turmaPraticaTransferida == null) {
			turmaPraticaTransferida = new TurmaVO();
		}
		return turmaPraticaTransferida;
	}

	public void setTurmaPraticaTransferida(TurmaVO turmaPraticaTransferida) {
		this.turmaPraticaTransferida = turmaPraticaTransferida;
	}

	public TurmaVO getTurmaTeoricaAntiga() {
		if (turmaTeoricaAntiga == null) {
			turmaTeoricaAntiga = new TurmaVO();
		}
		return turmaTeoricaAntiga;
	}

	public void setTurmaTeoricaAntiga(TurmaVO turmaTeoricaAntiga) {
		this.turmaTeoricaAntiga = turmaTeoricaAntiga;
	}

	public TurmaVO getTurmaTeoricaTransferida() {
		if (turmaTeoricaTransferida == null) {
			turmaTeoricaTransferida = new TurmaVO();
		}
		return turmaTeoricaTransferida;
	}

	public void setTurmaTeoricaTransferida(TurmaVO turmaTeoricaTransferida) {
		this.turmaTeoricaTransferida = turmaTeoricaTransferida;
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

	public List<SelectItem> getListaSelectItemTurmaTeorica() {
		if (listaSelectItemTurmaTeorica == null) {
			listaSelectItemTurmaTeorica = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemTurmaTeorica;
	}

	public void setListaSelectItemTurmaTeorica(List<SelectItem> listaSelectItemTurmaTeorica) {
		this.listaSelectItemTurmaTeorica = listaSelectItemTurmaTeorica;
	}

	public Boolean getExisteRegistroAulaPratica() {
		if (existeRegistroAulaPratica == null) {
			existeRegistroAulaPratica = Boolean.FALSE;
		}
		return existeRegistroAulaPratica;
	}

	public void setExisteRegistroAulaPratica(Boolean existeRegistroAulaPratica) {
		this.existeRegistroAulaPratica = existeRegistroAulaPratica;
	}

	public Boolean getExisteRegistroAulaTeorica() {
		if (existeRegistroAulaTeorica == null) {
			existeRegistroAulaTeorica = Boolean.FALSE;
		}
		return existeRegistroAulaTeorica;
	}

	public void setExisteRegistroAulaTeorica(Boolean existeRegistroAulaTeorica) {
		this.existeRegistroAulaTeorica = existeRegistroAulaTeorica;
	}

	public Integer getQtdeHorasAbonoTurmaPratica() {
		if (qtdeHorasAbonoTurmaPratica == null) {
			qtdeHorasAbonoTurmaPratica = 0;
		}
		return qtdeHorasAbonoTurmaPratica;
	}

	public void setQtdeHorasAbonoTurmaPratica(Integer qtdeHorasAbonoTurmaPratica) {
		this.qtdeHorasAbonoTurmaPratica = qtdeHorasAbonoTurmaPratica;
	}

	public Integer getQtdeHorasAbonoTurmaTeorica() {
		if (qtdeHorasAbonoTurmaTeorica == null) {
			qtdeHorasAbonoTurmaTeorica = 0;
		}
		return qtdeHorasAbonoTurmaTeorica;
	}

	public void setQtdeHorasAbonoTurmaTeorica(Integer qtdeHorasAbonoTurmaTeorica) {
		this.qtdeHorasAbonoTurmaTeorica = qtdeHorasAbonoTurmaTeorica;
	}
}
