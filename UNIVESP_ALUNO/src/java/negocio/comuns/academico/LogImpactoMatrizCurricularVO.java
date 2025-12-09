package negocio.comuns.academico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import negocio.comuns.academico.enumeradores.TituloImpactoMatrizCurricularEnum;
import negocio.comuns.arquitetura.SuperVO;

public class LogImpactoMatrizCurricularVO extends SuperVO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer codigo;
	private LogGradeCurricularVO logGradeCurricularVO;
	private TituloImpactoMatrizCurricularEnum tituloImpactoMatrizCurricularEnum;
	private StringBuilder msgAvisoAlteracaoGrade;
	private String mensagemLiEConcordoComOsTermos;
	private Boolean liEConcordoComOsTermos;
	
	private Boolean logReferenteExclusaoHistorico;
	private String tipoExclusaoHistorico;
	
	private List<HistoricoVO> listaHistoricoImpactoAlteracaoVOs;
	private List<TurmaVO> listaTurmaVOs;
	private List<MatriculaVO> listaMatriculaVOs;
	private Integer qtdeAulaProgramadaDisciplina;
	private Integer qtdeRegistroAulaDisciplina;
	private Integer qtdeVagaTurmaDisciplina;
	
	private JSONArray impactos; 
	
	public StringBuilder getMsgAvisoAlteracaoGrade() {
		if (msgAvisoAlteracaoGrade == null) {
			msgAvisoAlteracaoGrade = new StringBuilder();
		}
		return msgAvisoAlteracaoGrade;
	}
	public void setMsgAvisoAlteracaoGrade(StringBuilder msgAvisoAlteracaoGrade) {
		this.msgAvisoAlteracaoGrade = msgAvisoAlteracaoGrade;
	}
	public String getMensagemLiEConcordoComOsTermos() {
		if (mensagemLiEConcordoComOsTermos == null) {
			mensagemLiEConcordoComOsTermos = "";
		}
		return mensagemLiEConcordoComOsTermos;
	}
	public void setMensagemLiEConcordoComOsTermos(String mensagemLiEConcordoComOsTermos) {
		this.mensagemLiEConcordoComOsTermos = mensagemLiEConcordoComOsTermos;
	}
	public Boolean getLiEConcordoComOsTermos() {
		if (liEConcordoComOsTermos == null) {
			liEConcordoComOsTermos = false;
		}
		return liEConcordoComOsTermos;
	}
	public void setLiEConcordoComOsTermos(Boolean liEConcordoComOsTermos) {
		this.liEConcordoComOsTermos = liEConcordoComOsTermos;
	}

	public List<HistoricoVO> getListaHistoricoImpactoAlteracaoVOs() {
		if (listaHistoricoImpactoAlteracaoVOs == null) {
			listaHistoricoImpactoAlteracaoVOs = new ArrayList<HistoricoVO>(0);
		}
		return listaHistoricoImpactoAlteracaoVOs;
	}

	public void setListaHistoricoImpactoAlteracaoVOs(List<HistoricoVO> listaHistoricoImpactoAlteracaoVOs) {
		this.listaHistoricoImpactoAlteracaoVOs = listaHistoricoImpactoAlteracaoVOs;
	}
	public Boolean getLogReferenteExclusaoHistorico() {
		if (logReferenteExclusaoHistorico == null) {
			logReferenteExclusaoHistorico = false;
		}
		return logReferenteExclusaoHistorico;
	}
	public void setLogReferenteExclusaoHistorico(Boolean logReferenteExclusaoHistorico) {
		this.logReferenteExclusaoHistorico = logReferenteExclusaoHistorico;
	}
	public String getTipoExclusaoHistorico() {
		if (tipoExclusaoHistorico == null) {
			tipoExclusaoHistorico = "FORA_GRADE";
		}
		return tipoExclusaoHistorico;
	}
	public void setTipoExclusaoHistorico(String tipoExclusaoHistorico) {
		this.tipoExclusaoHistorico = tipoExclusaoHistorico;
	}
	public List<TurmaVO> getListaTurmaVOs() {
		if (listaTurmaVOs == null) {
			listaTurmaVOs = new ArrayList<TurmaVO>(0);
		}
		return listaTurmaVOs;
	}
	public void setListaTurmaVOs(List<TurmaVO> listaTurmaVOs) {
		this.listaTurmaVOs = listaTurmaVOs;
	}
	public Integer getQtdeAulaProgramadaDisciplina() {
		if (qtdeAulaProgramadaDisciplina == null) {
			qtdeAulaProgramadaDisciplina = 0;
		}
		return qtdeAulaProgramadaDisciplina;
	}
	public void setQtdeAulaProgramadaDisciplina(Integer qtdeAulaProgramadaDisciplina) {
		this.qtdeAulaProgramadaDisciplina = qtdeAulaProgramadaDisciplina;
	}
	public Integer getQtdeRegistroAulaDisciplina() {
		if (qtdeRegistroAulaDisciplina == null) {
			qtdeRegistroAulaDisciplina = 0;
		}
		return qtdeRegistroAulaDisciplina;
	}
	public void setQtdeRegistroAulaDisciplina(Integer qtdeRegistroAulaDisciplina) {
		this.qtdeRegistroAulaDisciplina = qtdeRegistroAulaDisciplina;
	}
	public Integer getQtdeVagaTurmaDisciplina() {
		if (qtdeVagaTurmaDisciplina == null) {
			qtdeVagaTurmaDisciplina = 0;
		}
		return qtdeVagaTurmaDisciplina;
	}
	public void setQtdeVagaTurmaDisciplina(Integer qtdeVagaTurmaDisciplina) {
		this.qtdeVagaTurmaDisciplina = qtdeVagaTurmaDisciplina;
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
	public LogGradeCurricularVO getLogGradeCurricularVO() {
		if (logGradeCurricularVO == null) {
			logGradeCurricularVO = new LogGradeCurricularVO();
		}
		return logGradeCurricularVO;
	}
	public void setLogGradeCurricularVO(LogGradeCurricularVO logGradeCurricularVO) {
		this.logGradeCurricularVO = logGradeCurricularVO;
	}
	public JSONArray getImpactos() {
		if (impactos == null) {
			impactos = new JSONArray();
		}
		return impactos;
	}
	public void setImpactos(JSONArray impactos) {
		this.impactos = impactos;
	}
	public TituloImpactoMatrizCurricularEnum getTituloImpactoMatrizCurricularEnum() {
		if (tituloImpactoMatrizCurricularEnum == null) {
			tituloImpactoMatrizCurricularEnum = TituloImpactoMatrizCurricularEnum.INCLUSAO_GRADE_DISCIPLINA;
		}
		return tituloImpactoMatrizCurricularEnum;
	}
	public void setTituloImpactoMatrizCurricularEnum(TituloImpactoMatrizCurricularEnum tituloImpactoMatrizCurricularEnum) {
		this.tituloImpactoMatrizCurricularEnum = tituloImpactoMatrizCurricularEnum;
	}
	public List<MatriculaVO> getListaMatriculaVOs() {
		if (listaMatriculaVOs == null) {
			listaMatriculaVOs = new ArrayList<MatriculaVO>(0);
		}
		return listaMatriculaVOs;
	}
	public void setListaMatriculaVOs(List<MatriculaVO> listaMatriculaVOs) {
		this.listaMatriculaVOs = listaMatriculaVOs;
	}
	
	
}
