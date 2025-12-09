package negocio.comuns.academico;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.dominios.SituacaoVinculoMatricula;

public class MapaAlunoAptoFormarVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4319569355808818040L;

	private MatriculaPeriodoVO matriculaPeriodo;	
	private Integer qtdeDisc;
	private Integer qtdeDiscCursada;
	private Date dataAulaIni;
	private Date dataAulaFim;
	private String situacao;
	
	public MatriculaPeriodoVO getMatriculaPeriodo() {
		if (matriculaPeriodo == null) {
			matriculaPeriodo = new MatriculaPeriodoVO();
		}
		return matriculaPeriodo;
	}
	
	public void setMatriculaPeriodo(MatriculaPeriodoVO matriculaPeriodo) {
		this.matriculaPeriodo = matriculaPeriodo;
	}
	
	public Integer getQtdeDisc() {
		if (qtdeDisc == null) {
			qtdeDisc = 0;
		}
		return qtdeDisc;
	}
	
	public void setQtdeDisc(Integer qtdeDisc) {
		this.qtdeDisc = qtdeDisc;
	}
	
	public Integer getQtdeDiscCursada() {
		if (qtdeDiscCursada == null) {
			qtdeDiscCursada = 0;
		}
		return qtdeDiscCursada;
	}
	
	public void setQtdeDiscCursada(Integer qtdeDiscCursada) {
		this.qtdeDiscCursada = qtdeDiscCursada;
	}
	
	public Date getDataAulaIni() {
		if (dataAulaIni == null) {
			dataAulaIni = new Date();
		}
		return dataAulaIni;
	}
	
	public void setDataAulaIni(Date dataAulaIni) {
		this.dataAulaIni = dataAulaIni;
	}
	
	public Date getDataAulaFim() {
		if (dataAulaFim == null) {
			dataAulaFim = new Date();
		}
		return dataAulaFim;
	}
	
	public void setDataAulaFim(Date dataAulaFim) {
		this.dataAulaFim = dataAulaFim;
	}
	
	public String getSituacao() {
		if (situacao == null) {
			situacao = "";
		}
		return situacao;
	}
	
	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}
	
	 public String getSituacao_Apresentar() {
	        try {
	            return SituacaoVinculoMatricula.getDescricao(situacao);
	        } catch (Exception e) {
	            return SituacaoVinculoMatricula.getDescricao("ER");
	        }
	    }

}
