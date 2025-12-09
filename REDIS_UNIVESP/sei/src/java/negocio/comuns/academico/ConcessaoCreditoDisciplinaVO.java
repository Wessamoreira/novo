package negocio.comuns.academico;

import negocio.comuns.arquitetura.SuperVO;

/**
 *
 * @author Carlos
 */
public class ConcessaoCreditoDisciplinaVO extends SuperVO {
    private Integer codigo;
    private DisciplinaVO disciplinaVO;
    private GradeDisciplinaVO gradeDisciplinaVO;
    private AproveitamentoDisciplinaVO aproveitamentoDisciplinaVO;
    private Integer qtdeCreditoConcedido;
    private String descricaoComplementacaoCH;
    private String ano;
    private String semestre;

    public DisciplinaVO getDisciplinaVO() {
        if (disciplinaVO == null) {
            disciplinaVO = new DisciplinaVO();
        }
        return disciplinaVO;
    }

    public void setDisciplinaVO(DisciplinaVO disciplinaVO) {
        this.disciplinaVO = disciplinaVO;
    }

    public Integer getQtdeCreditoConcedido() {
        if (qtdeCreditoConcedido == null) {
            qtdeCreditoConcedido = 0;
        }
        return qtdeCreditoConcedido;
    }

    public void setQtdeCreditoConcedido(Integer qtdeCreditoConcedido) {
        this.qtdeCreditoConcedido = qtdeCreditoConcedido;
    }

    public String getDescricaoComplementacaoCH() {
        if (descricaoComplementacaoCH == null) {
            descricaoComplementacaoCH = "";
        }
        return descricaoComplementacaoCH;
    }

    public void setDescricaoComplementacaoCH(String descricaoComplementacaoCH) {
        this.descricaoComplementacaoCH = descricaoComplementacaoCH;
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

    public AproveitamentoDisciplinaVO getAproveitamentoDisciplinaVO() {
        if (aproveitamentoDisciplinaVO == null) {
            aproveitamentoDisciplinaVO = new AproveitamentoDisciplinaVO();
        }
        return aproveitamentoDisciplinaVO;
    }

    public void setAproveitamentoDisciplinaVO(AproveitamentoDisciplinaVO aproveitamentoDisciplinaVO) {
        this.aproveitamentoDisciplinaVO = aproveitamentoDisciplinaVO;
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

	public GradeDisciplinaVO getGradeDisciplinaVO() {
		if (gradeDisciplinaVO == null) {
			gradeDisciplinaVO = new GradeDisciplinaVO();
		}
		return gradeDisciplinaVO;
	}

	public void setGradeDisciplinaVO(GradeDisciplinaVO gradeDisciplinaVO) {
		this.gradeDisciplinaVO = gradeDisciplinaVO;
	}
}
