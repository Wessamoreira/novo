package negocio.comuns.academico;

import java.util.Date;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.processosel.QuestionarioVO;
import negocio.comuns.utilitarias.Uteis;

public class CalendarioRelatorioFinalFacilitadorVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3705593141862029952L;
	private Integer codigo;
	private String situacao;
	private DisciplinaVO disciplinaVO;
	private Date dataInicio;
	private Date dataFim;
	private String ano;
	private String semestre;
	private String mes;
	private QuestionarioVO questionarioVO;
	private String variavelTipoNota;

	
	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
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

	public Date getDataInicio() {
		if (dataInicio == null) {
			dataInicio = new Date();
	    }
		return dataInicio;
	}
	
	 public String getDataInicio_Apresentar() {
        if (dataInicio == null) {
            return "";
        }
        return (Uteis.getData(dataInicio));
    }

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		if (dataFim == null) {
			dataFim = new Date();
	    }
		return dataFim;
	}

	 public String getDataFim_Apresentar() {
        if (dataFim == null) {
            return "";
        }
        return (Uteis.getData(dataFim));
    }
	
	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
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

	public String getAno() {
		if (ano == null) {
			ano = "";
		}
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getSemestre() {
		if (semestre == null) {
			semestre = "";
		}
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	public String getMes() {
		if (mes == null) {
			mes = "";
		}
		return mes;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}

	public QuestionarioVO getQuestionarioVO() {
		if (questionarioVO == null) {
			questionarioVO = new QuestionarioVO();
		}
		return questionarioVO;
	}

	public void setQuestionarioVO(QuestionarioVO questionarioVO) {
		this.questionarioVO = questionarioVO;
	}

	public String getVariavelTipoNota() {
		if (variavelTipoNota == null) {
			variavelTipoNota = "";
		}
		return variavelTipoNota;
	}

	public void setVariavelTipoNota(String variavelTipoNota) {
		this.variavelTipoNota = variavelTipoNota;
	}
}
