/**
 * 
 */
package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Rodrigo Wind
 *
 */
public class ProgramacaoAulaResumoSemanaVO {
	/**
	 * @author Rodrigo Wind - 17/09/2015
	 */
	private Date dataInicio;
	private Date dataTermino;
	private List<ProgramacaoAulaResumoDisciplinaSemanaVO> programacaoAulaResumoDisciplinaSemanaVOs;
	/**
	 * @return the dataInicio
	 */
	public Date getDataInicio() {
		if (dataInicio == null) {
			dataInicio = new Date();
		}
		return dataInicio;
	}
	/**
	 * @param dataInicio the dataInicio to set
	 */
	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}
	/**
	 * @return the dataTermino
	 */
	public Date getDataTermino() {
		if (dataTermino == null) {
			dataTermino = new Date();
		}
		return dataTermino;
	}
	/**
	 * @param dataTermino the dataTermino to set
	 */
	public void setDataTermino(Date dataTermino) {
		this.dataTermino = dataTermino;
	}
	/**
	 * @return the programacaoAulaResumoDisciplinaSemanaVOs
	 */
	public List<ProgramacaoAulaResumoDisciplinaSemanaVO> getProgramacaoAulaResumoDisciplinaSemanaVOs() {
		if (programacaoAulaResumoDisciplinaSemanaVOs == null) {
			programacaoAulaResumoDisciplinaSemanaVOs = new ArrayList<ProgramacaoAulaResumoDisciplinaSemanaVO>(0);
		}
		return programacaoAulaResumoDisciplinaSemanaVOs;
	}
	/**
	 * @param programacaoAulaResumoDisciplinaSemanaVOs the programacaoAulaResumoDisciplinaSemanaVOs to set
	 */
	public void setProgramacaoAulaResumoDisciplinaSemanaVOs(List<ProgramacaoAulaResumoDisciplinaSemanaVO> programacaoAulaResumoDisciplinaSemanaVOs) {
		this.programacaoAulaResumoDisciplinaSemanaVOs = programacaoAulaResumoDisciplinaSemanaVOs;
	}
	
	
	
	
}
