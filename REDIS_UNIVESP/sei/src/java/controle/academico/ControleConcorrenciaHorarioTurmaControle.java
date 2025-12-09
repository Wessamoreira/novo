/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package controle.academico;

import java.io.Serializable;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.ControleConcorrencia;
import controle.arquitetura.SuperControle;
import negocio.comuns.arquitetura.ControleConcorrenciaHorarioTurmaVO;

/**
 *
 * @author Carlos
 */
@Controller("ControleConcorrenciaHorarioTurmaControle")
@Scope("viewScope")
@Lazy
public class ControleConcorrenciaHorarioTurmaControle extends SuperControle implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<ControleConcorrenciaHorarioTurmaVO> listaConcorrencia;

	public ControleConcorrenciaHorarioTurmaControle() {

	}

	public void removerProfessorConcorrencia() {
		try {
			ControleConcorrenciaHorarioTurmaVO obj = (ControleConcorrenciaHorarioTurmaVO) context().getExternalContext().getRequestMap().get("concorrencia");
			ControleConcorrencia.removerProfessorListaProfessorHorario(obj.getCodigoProfessor());
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * @return the listaConcorrencia
	 */
	public List<ControleConcorrenciaHorarioTurmaVO> getListaConcorrencia() {
		return ControleConcorrencia.getControleConcorrenciaHorarioTurmaVOs();
	}

	/**
	 * @param listaConcorrencia
	 *            the listaConcorrencia to set
	 */
	public void setListaConcorrencia(List<ControleConcorrenciaHorarioTurmaVO> listaConcorrencia) {
		this.listaConcorrencia = listaConcorrencia;
	}

}
