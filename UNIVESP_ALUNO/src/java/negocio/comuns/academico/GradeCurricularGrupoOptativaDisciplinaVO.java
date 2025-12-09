package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import negocio.comuns.academico.enumeradores.FormulaCalculoNotaEnum;
import negocio.comuns.academico.enumeradores.ModalidadeDisciplinaEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.Ordenacao;


@XmlRootElement(name = "gradeCurricularGrupoOptativaDisciplinaVO")
public class GradeCurricularGrupoOptativaDisciplinaVO extends SuperVO {

    private static final long serialVersionUID = -8817407078589950563L;
    private Integer codigo;
    private DisciplinaVO disciplina;
    private Integer cargaHoraria;
    private Integer bimestre;
    private Integer cargaHorariaPratica;
    private ModalidadeDisciplinaEnum modalidadeDisciplina;
    private Integer nrCreditos;
    private Double nrCreditoFinanceiro;
    private ConfiguracaoAcademicoVO configuracaoAcademico;
    private Boolean diversificada;
    private Boolean disciplinaComposta;
    private GradeCurricularGrupoOptativaVO gradeCurricularGrupoOptativa;
    private List<GradeDisciplinaCompostaVO> gradeDisciplinaCompostaVOs;
    private List<DisciplinaPreRequisitoVO> disciplinaRequisitoVOs;
    private Integer horaAula;
    /**
     * Atributos criados para controle no aproveitamento de disciplina
     */
    private Boolean selecionadoAproveitamento;
    private DisciplinasAproveitadasVO disciplinasAproveitadasVO;
    private Boolean aproveitamentoIncluidoPorMapaEquivalencia;
    private HistoricoVO historicoAtualAluno;
    /**
     * Transiente Usados na Inclusão do Historico Aluno    
     */
    private List<InclusaoHistoricoAlunoDisciplinaVO> inclusaoHistoricoAlunoDisciplinaVOs;
    private InclusaoHistoricoAlunoDisciplinaVO inclusaoHistoricoAlunoDisciplinaVO;
    private List<HistoricoVO> historicoVOs;
    /**
     * TRANSIENT - utilizado na transferencia de matriz curricular
     */
    private PeriodoLetivoVO periodoLetivoDisciplinaReferenciada;
    /**
	 * Este campo define qual regra será aplicada no calculo da nota da disciplina composta(MAE)
	 */
	private FormulaCalculoNotaEnum formulaCalculoNota;
	/**
	 * Este campo é utilizado quando a formulaCalculoNota for do tipo Formula de Calculo 
	 */
	private String formulaCalculo;
	
	private Boolean disciplinaEstagio;
	

    /**
     * Habilita o recurso de controle de recuperação das disciplinas filhas com base nas regras da disciplina mae
     */
	private Boolean controlarRecuperacaoPelaDisciplinaPrincipal;
	/**
	 * A informação deste campo deve existir nas configurações academicas vinculadas as disciplinas filhas da composição e na disciplina mae
	 */
	private String variavelNotaRecuperacao;
	/**
	 * Deve ser utilizado como variavel nesta formula a variavel informada na gradedisciplinacomposta, o sistema
	 * irá substituir pela nota informada no variavel definida no campo variavelNotaCondicaoUsoRecuperacao, 
	 * Esta formula verifica se o aluno deverá ficar de recuperação ou não nas disciplinas filhas.
	 * 
	 */
	private String condicaoUsoRecuperacao;
	/**
	 * Corresponde a variável da nota que será obtida o valor a ser substituida na condicaoUsoRecuperacao
	 */
	private String variavelNotaCondicaoUsoRecuperacao;
	/**
	 * Deve ser utilizado como variavel nesta formula a variavel informada na gradedisciplinacomposta, o sistema
	 * irá substituir pela nota informada no variavel definida no campo variavelNotaFormulaCalculoNotaRecuperada, 
	 * esta formula tem a finalidade para definir se o aluno recuperou a nota ou não
	 */
	private String formulaCalculoNotaRecuperada;
	/**
	 * Corresponde a variável da nota que será obtida o valor a ser substituida na formulaCalculoNotaRecuperada
	 */
	private String variavelNotaFormulaCalculoNotaRecuperada;
	/**
	 * Deve ser utilizado como nesta formula a variavel informada na gradedisciplinacomposta, o sistema
	 * irá substituir pela nota informada no variavel definida no campo variavelNotaRecuperacao, 
	 * esta formula tem a finalidade para definir a nota que será lançada na nota REC da disciplina mãe
	 */
	private String formulaCalculoNotaRecuperacao;
	
	private Boolean ofertaJaValida;
	private Boolean disciplinaOfertada;
	private Boolean disciplinaJaAdicionada;
	private Boolean utilizarEmissaoXmlDiploma;

	@XmlElement(name = "codigo")
    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    @XmlElement(name = "disciplina")
    public DisciplinaVO getDisciplina() {
        if (disciplina == null) {
            disciplina = new DisciplinaVO();
        }
        return disciplina;
    }

    public void setDisciplina(DisciplinaVO disciplina) {
        this.disciplina = disciplina;
    }

    public Integer getCargaHoraria() {
        if (cargaHoraria == null) {
            cargaHoraria = 0;
        }
        return cargaHoraria;
    }

    public void setCargaHoraria(Integer cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    public Integer getCargaHorariaPratica() {
        if (cargaHorariaPratica == null) {
            cargaHorariaPratica = 0;
        }
        return cargaHorariaPratica;
    }

    public void setCargaHorariaPratica(Integer cargaHorariaPratica) {
        this.cargaHorariaPratica = cargaHorariaPratica;
    }

    public Integer getCargaHorariaTeorica() {
        return getCargaHoraria() - getCargaHorariaPratica();
    }

    public ModalidadeDisciplinaEnum getModalidadeDisciplina() {
        if (modalidadeDisciplina == null) {
            modalidadeDisciplina = ModalidadeDisciplinaEnum.PRESENCIAL;
        }
        return modalidadeDisciplina;
    }

    public void setModalidadeDisciplina(ModalidadeDisciplinaEnum modalidadeDisciplina) {
        this.modalidadeDisciplina = modalidadeDisciplina;
    }

    public Integer getNrCreditos() {
        if (nrCreditos == null) {
            nrCreditos = 0;
        }
        return nrCreditos;
    }

    public void setNrCreditos(Integer nrCreditos) {
        this.nrCreditos = nrCreditos;
    }

    public ConfiguracaoAcademicoVO getConfiguracaoAcademico() {
        if (configuracaoAcademico == null) {
            configuracaoAcademico = new ConfiguracaoAcademicoVO();
        }
        return configuracaoAcademico;
    }

    public void setConfiguracaoAcademico(ConfiguracaoAcademicoVO configuracaoAcademico) {
        this.configuracaoAcademico = configuracaoAcademico;
    }

    public Boolean getDiversificada() {
        if (diversificada == null) {
            diversificada = false;
        }
        return diversificada;
    }

    public void setDiversificada(Boolean diversificada) {
        this.diversificada = diversificada;
    }

    public GradeCurricularGrupoOptativaVO getGradeCurricularGrupoOptativa() {
        if (gradeCurricularGrupoOptativa == null) {
            gradeCurricularGrupoOptativa = new GradeCurricularGrupoOptativaVO();
        }
        return gradeCurricularGrupoOptativa;
    }

    public void setGradeCurricularGrupoOptativa(GradeCurricularGrupoOptativaVO gradeCurricularGrupoOptativa) {
        this.gradeCurricularGrupoOptativa = gradeCurricularGrupoOptativa;
    }

    public Double getNrCreditoFinanceiro() {
        if (nrCreditoFinanceiro == null) {
            nrCreditoFinanceiro = 0.0;
        }
        return nrCreditoFinanceiro;
    }

    public void setNrCreditoFinanceiro(Double nrCreditoFinanceiro) {
        this.nrCreditoFinanceiro = nrCreditoFinanceiro;
    }

    public List<GradeDisciplinaCompostaVO> getGradeDisciplinaCompostaVOs() {
        if (gradeDisciplinaCompostaVOs == null) {
            gradeDisciplinaCompostaVOs = new ArrayList<GradeDisciplinaCompostaVO>(0);
        }
        return gradeDisciplinaCompostaVOs;
    }

    public void setGradeDisciplinaCompostaVOs(List<GradeDisciplinaCompostaVO> gradeDisciplinaCompostaVOs) {
        this.gradeDisciplinaCompostaVOs = gradeDisciplinaCompostaVOs;
    }

    public Boolean getDisciplinaComposta() {
        if (disciplinaComposta == null) {
            disciplinaComposta = false;
        }
        return disciplinaComposta;
    }

    public void setDisciplinaComposta(Boolean disciplinaComposta) {
        this.disciplinaComposta = disciplinaComposta;
    }

    public Boolean getSelecionadoAproveitamento() {
        if (selecionadoAproveitamento == null) {
            selecionadoAproveitamento = Boolean.FALSE;
        }
        return selecionadoAproveitamento;
    }

    public void setSelecionadoAproveitamento(Boolean selecionadoAproveitamento) {
        this.selecionadoAproveitamento = selecionadoAproveitamento;
    }

    public DisciplinasAproveitadasVO getDisciplinasAproveitadasVO() {
        if (disciplinasAproveitadasVO == null) {
            disciplinasAproveitadasVO = new DisciplinasAproveitadasVO();
        }
        return disciplinasAproveitadasVO;
    }

    public void setDisciplinasAproveitadasVO(DisciplinasAproveitadasVO disciplinasAproveitadasVO) {
        this.disciplinasAproveitadasVO = disciplinasAproveitadasVO;
    }

    /**
     * @return the historicoAtualAluno
     */
    public HistoricoVO getHistoricoAtualAluno() {
        if (historicoAtualAluno == null) {
            historicoAtualAluno = new HistoricoVO();
        }
        return historicoAtualAluno;
    }

    /**
     * @param historicoAtualAluno the historicoAtualAluno to set
     */
    public void setHistoricoAtualAluno(HistoricoVO historicoAtualAluno) {
        this.historicoAtualAluno = historicoAtualAluno;
    }

    /**
     * @return the periodoLetivoDisciplinaReferenciada
     */
    public PeriodoLetivoVO getPeriodoLetivoDisciplinaReferenciada() {
        if (periodoLetivoDisciplinaReferenciada == null) {
            periodoLetivoDisciplinaReferenciada = new PeriodoLetivoVO();
        }
        return periodoLetivoDisciplinaReferenciada;
    }

    /**
     * @param periodoLetivoDisciplinaReferenciada the periodoLetivoDisciplinaReferenciada to set
     */
    public void setPeriodoLetivoDisciplinaReferenciada(PeriodoLetivoVO periodoLetivoDisciplinaReferenciada) {
        this.periodoLetivoDisciplinaReferenciada = periodoLetivoDisciplinaReferenciada;
    }

	/**
	 * @return the formulaCalculoNota
	 */
	public FormulaCalculoNotaEnum getFormulaCalculoNota() {
		if (formulaCalculoNota == null) {
			formulaCalculoNota = FormulaCalculoNotaEnum.MEDIA;
		}
		return formulaCalculoNota;
	}

	/**
	 * @param formulaCalculoNota the formulaCalculoNota to set
	 */
	public void setFormulaCalculoNota(FormulaCalculoNotaEnum formulaCalculoNota) {
		this.formulaCalculoNota = formulaCalculoNota;
	}
	
	public String getFormulaCalculo() {
		if (formulaCalculo == null) {
			formulaCalculo = "";
		}
		return formulaCalculo;
	}

	public void setFormulaCalculo(String formulaCalculo) {
		this.formulaCalculo = formulaCalculo;
	}
	
	public Boolean getIsUtilizaFormulaCalculo(){
		return getDisciplinaComposta() && getFormulaCalculoNota().equals(FormulaCalculoNotaEnum.FORMULA_CALCULO);
	}
	
	//TRANSIENT
	/**
	 * @author Victor Hugo de Paula Costa 19/08/2015 09:40
	 */
	private MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO;
	private Boolean selecionado;

	public MatriculaPeriodoTurmaDisciplinaVO getMatriculaPeriodoTurmaDisciplinaVO() {
		if(matriculaPeriodoTurmaDisciplinaVO == null) {
			matriculaPeriodoTurmaDisciplinaVO = new MatriculaPeriodoTurmaDisciplinaVO();
		}
		return matriculaPeriodoTurmaDisciplinaVO;
	}

	public void setMatriculaPeriodoTurmaDisciplinaVO(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO) {
		this.matriculaPeriodoTurmaDisciplinaVO = matriculaPeriodoTurmaDisciplinaVO;
	}

	public Boolean getSelecionado() {
		if(selecionado == null) {
			selecionado = false;
		}
		return selecionado;
	}

	public void setSelecionado(Boolean selecionado) {
		this.selecionado = selecionado;
	}
	
	public List<DisciplinaPreRequisitoVO> getDisciplinaRequisitoVOs() {
		if (disciplinaRequisitoVOs == null) {
			disciplinaRequisitoVOs = new ArrayList<DisciplinaPreRequisitoVO>();
		}
		return disciplinaRequisitoVOs;
	}

	public void setDisciplinaRequisitoVOs(List<DisciplinaPreRequisitoVO> disciplinaRequisitoVOs) {
		this.disciplinaRequisitoVOs = disciplinaRequisitoVOs;
	}
	
	@SuppressWarnings("rawtypes")
	public void adicionarObjDisciplinaPreRequisitoVOs(DisciplinaPreRequisitoVO obj) throws Exception {
		DisciplinaPreRequisitoVO.validarDados(obj);
		int index = 0;
		Iterator i = getDisciplinaRequisitoVOs().iterator();
		while (i.hasNext()) {
			DisciplinaPreRequisitoVO objExistente = (DisciplinaPreRequisitoVO) i.next();
			if (objExistente.getDisciplina().getCodigo().equals(obj.getDisciplina().getCodigo())) {
				getDisciplinaRequisitoVOs().set(index, obj);
				return;
			}
			index++;
		}
		getDisciplinaRequisitoVOs().add(obj);
	}
	
	@SuppressWarnings("rawtypes")
	public DisciplinaPreRequisitoVO consultarObjDisciplinaPreRequisitoVO(Integer preRequisito) throws Exception {
		Iterator i = getDisciplinaRequisitoVOs().iterator();
		while (i.hasNext()) {
			DisciplinaPreRequisitoVO objExistente = (DisciplinaPreRequisitoVO) i.next();
			if (objExistente.getDisciplina().getCodigo().equals(preRequisito)) {
				return objExistente;
			}
		}
		return null;
	}
	
	public String getPreRequisitoApresentar() {
		String requisitos = "";
		for (DisciplinaPreRequisitoVO disciplinaPreRequisitoVO : getDisciplinaRequisitoVOs()) {
			if (requisitos.trim().isEmpty()) {
				requisitos = disciplinaPreRequisitoVO.getDisciplina().getCodigo() + " - " + disciplinaPreRequisitoVO.getDisciplina().getNome();
			} else {
				requisitos += ", " + disciplinaPreRequisitoVO.getDisciplina().getCodigo() + " - " + disciplinaPreRequisitoVO.getDisciplina().getNome();
			}
		}
		return requisitos;
	}
	
	public String getPreRequisitoAbreviaturaApresentar() {
		String requisitos = "";
		String abrev = "";
		for (DisciplinaPreRequisitoVO disciplinaPreRequisitoVO : getDisciplinaRequisitoVOs()) {
			abrev = disciplinaPreRequisitoVO.getDisciplina().getAbreviatura();
			if (abrev.trim().isEmpty()) {
				abrev = disciplinaPreRequisitoVO.getDisciplina().getCodigo().toString();
			}
			if (requisitos.trim().isEmpty()) {
				requisitos = abrev;
			} else {
				requisitos += ", " + abrev;
			}
		}
		return requisitos;
	}
	
	/**
	 * Método elaborado para atender layout 3, que exibe apenas a abreviatura da disciplina
	 * @return
	 */
	public String getAbreviaturaPreRequisitoApresentar() {
		String valor = "";
		Ordenacao.ordenarLista(getDisciplinaRequisitoVOs(), "codigo");
		for (DisciplinaPreRequisitoVO disciplinaPreRequisitoVO : getDisciplinaRequisitoVOs()) {
			if (valor.trim().isEmpty()) {
				valor = disciplinaPreRequisitoVO.getDisciplina().getAbreviatura();
			} else {
				valor += ", " + disciplinaPreRequisitoVO.getDisciplina().getAbreviatura();
			}
		}
		return valor;
	}
	
    public String getOrdenacao() {
        return getDisciplina().getNome();
    }
    
	public Integer getHoraAula() {
		if (horaAula == null) {
			horaAula = 0;
		}
		return horaAula;
	}

	public void setHoraAula(Integer horaAula) {
		this.horaAula = horaAula;
	}

	public Boolean getDisciplinaEstagio() {
		if (disciplinaEstagio == null) {
			disciplinaEstagio = false;
		}
		return disciplinaEstagio;
	}

	public void setDisciplinaEstagio(Boolean disciplinaEstagio) {
		this.disciplinaEstagio = disciplinaEstagio;
	}

	public Boolean getAproveitamentoIncluidoPorMapaEquivalencia() {
		if (aproveitamentoIncluidoPorMapaEquivalencia == null) {
			aproveitamentoIncluidoPorMapaEquivalencia = Boolean.FALSE;
		}
		return aproveitamentoIncluidoPorMapaEquivalencia;
	}

	public void setAproveitamentoIncluidoPorMapaEquivalencia(Boolean aproveitamentoIncluidoPorMapaEquivalencia) {
		this.aproveitamentoIncluidoPorMapaEquivalencia = aproveitamentoIncluidoPorMapaEquivalencia;
	}

	public List<InclusaoHistoricoAlunoDisciplinaVO> getInclusaoHistoricoAlunoDisciplinaVOs() {
		if(inclusaoHistoricoAlunoDisciplinaVOs == null){
			inclusaoHistoricoAlunoDisciplinaVOs = new ArrayList<InclusaoHistoricoAlunoDisciplinaVO>(0);
		}
		return inclusaoHistoricoAlunoDisciplinaVOs;
	}

	public void setInclusaoHistoricoAlunoDisciplinaVOs(
			List<InclusaoHistoricoAlunoDisciplinaVO> inclusaoHistoricoAlunoDisciplinaVOs) {
		this.inclusaoHistoricoAlunoDisciplinaVOs = inclusaoHistoricoAlunoDisciplinaVOs;
	}

	public InclusaoHistoricoAlunoDisciplinaVO getInclusaoHistoricoAlunoDisciplinaVO() {
		if(inclusaoHistoricoAlunoDisciplinaVO == null){
			inclusaoHistoricoAlunoDisciplinaVO = new InclusaoHistoricoAlunoDisciplinaVO();
		}
		return inclusaoHistoricoAlunoDisciplinaVO;
	}

	public void setInclusaoHistoricoAlunoDisciplinaVO(
			InclusaoHistoricoAlunoDisciplinaVO inclusaoHistoricoAlunoDisciplinaVO) {
		this.inclusaoHistoricoAlunoDisciplinaVO = inclusaoHistoricoAlunoDisciplinaVO;
	}

	public List<HistoricoVO> getHistoricoVOs() {
		if(historicoVOs == null){
			historicoVOs = new ArrayList<HistoricoVO>(0);
		}
		return historicoVOs;
	}

	public void setHistoricoVOs(List<HistoricoVO> historicoVOs) {
		this.historicoVOs = historicoVOs;
	}


	public Boolean getControlarRecuperacaoPelaDisciplinaPrincipal() {
		if(controlarRecuperacaoPelaDisciplinaPrincipal == null){
			controlarRecuperacaoPelaDisciplinaPrincipal = false;
		}
		return controlarRecuperacaoPelaDisciplinaPrincipal;
	}

	public void setControlarRecuperacaoPelaDisciplinaPrincipal(Boolean controlarRecuperacaoPelaDisciplinaPrincipal) {
		this.controlarRecuperacaoPelaDisciplinaPrincipal = controlarRecuperacaoPelaDisciplinaPrincipal;
	}

	public String getCondicaoUsoRecuperacao() {
		if(condicaoUsoRecuperacao == null){
			condicaoUsoRecuperacao = "";
		}
		return condicaoUsoRecuperacao;
	}

	public void setCondicaoUsoRecuperacao(String condicaoUsoRecuperacao) {
		this.condicaoUsoRecuperacao = condicaoUsoRecuperacao;
	}

	public String getVariavelNotaCondicaoUsoRecuperacao() {
		if(variavelNotaCondicaoUsoRecuperacao == null){
			variavelNotaCondicaoUsoRecuperacao = "";
		}
		return variavelNotaCondicaoUsoRecuperacao;
	}

	public void setVariavelNotaCondicaoUsoRecuperacao(String variavelNotaCondicaoUsoRecuperacao) {
		this.variavelNotaCondicaoUsoRecuperacao = variavelNotaCondicaoUsoRecuperacao;
	}

	public String getFormulaCalculoNotaRecuperada() {
		if(formulaCalculoNotaRecuperada == null){
			formulaCalculoNotaRecuperada = "";
		}
		return formulaCalculoNotaRecuperada;
	}

	public void setFormulaCalculoNotaRecuperada(String formulaCalculoNotaRecuperada) {
		this.formulaCalculoNotaRecuperada = formulaCalculoNotaRecuperada;
	}

	public String getVariavelNotaFormulaCalculoNotaRecuperada() {
		if(variavelNotaFormulaCalculoNotaRecuperada == null){
			variavelNotaFormulaCalculoNotaRecuperada = "";
		}
		return variavelNotaFormulaCalculoNotaRecuperada;
	}

	public void setVariavelNotaFormulaCalculoNotaRecuperada(String variavelNotaFormulaCalculoNotaRecuperada) {
		this.variavelNotaFormulaCalculoNotaRecuperada = variavelNotaFormulaCalculoNotaRecuperada;
	}

	public String getVariavelNotaRecuperacao() {
		if(variavelNotaRecuperacao == null){
			variavelNotaRecuperacao = "";
		}
		return variavelNotaRecuperacao;
	}

	public void setVariavelNotaRecuperacao(String variavelNotaRecuperacao) {
		this.variavelNotaRecuperacao = variavelNotaRecuperacao;
	}
		
	public Boolean getApresentarOpcaoRecuperacao(){
		return getDisciplinaComposta();	
	}
	
	
	public String getFormulaCalculoNotaRecuperacao() {
		if(formulaCalculoNotaRecuperacao == null){
			formulaCalculoNotaRecuperacao = "";
		}
		return formulaCalculoNotaRecuperacao;
	}

	public void setFormulaCalculoNotaRecuperacao(String formulaCalculoNotaRecuperacao) {
		this.formulaCalculoNotaRecuperacao = formulaCalculoNotaRecuperacao;
	}
	
	@Override
	public String toString() {
		return "Grade Curricular Grupo Optativa Disciplina: " + this.getCodigo() + " Grade Grupo Optativa: " + this.getGradeCurricularGrupoOptativa().getDescricao();
	}

	public Integer getBimestre() {
		if(bimestre == null) {
			bimestre = 0;
		}
		return bimestre;
	}

	public void setBimestre(Integer bimestre) {
		this.bimestre = bimestre;
	}

	public Boolean getDisciplinaOfertada() {
		if(disciplinaOfertada == null) {
			disciplinaOfertada =  false;
		}
		return disciplinaOfertada;
	}

	public void setDisciplinaOfertada(Boolean disciplinaOfertada) {
		this.disciplinaOfertada = disciplinaOfertada;
	}

	public Boolean getOfertaJaValida() {
		if(ofertaJaValida == null) {
			ofertaJaValida =  false;
		}		
		return ofertaJaValida;
	}

	public void setOfertaJaValida(Boolean ofertaJaValida) {
		this.ofertaJaValida = ofertaJaValida;
	}

	public Boolean getDisciplinaJaAdicionada() {
		if(disciplinaJaAdicionada == null) {
			disciplinaJaAdicionada = false;
		}
		return disciplinaJaAdicionada;
	}

	public void setDisciplinaJaAdicionada(Boolean disciplinaJaAdicionada) {
		this.disciplinaJaAdicionada = disciplinaJaAdicionada;
	}
	
	public Boolean getUtilizarEmissaoXmlDiploma() {
		if (utilizarEmissaoXmlDiploma == null) {
			utilizarEmissaoXmlDiploma = Boolean.TRUE;
		}
		return utilizarEmissaoXmlDiploma;
	}
	
	public void setUtilizarEmissaoXmlDiploma(Boolean utilizarEmissaoXmlDiploma) {
		this.utilizarEmissaoXmlDiploma = utilizarEmissaoXmlDiploma;
	}
}
