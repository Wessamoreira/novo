package negocio.comuns.ead;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.enumeradores.NivelComplexidadeQuestaoEnum;
import negocio.comuns.ead.enumeradores.SituacaoQuestaoEnum;
import negocio.comuns.ead.enumeradores.TipoQuestaoEnum;

public class QuestaoVO extends SuperVO {

    /**
     * 
     */
    private static final long serialVersionUID = 6792326477039487154L;

    private Integer codigo;
    private Boolean usoOnline;
    private Boolean usoPresencial;
    private Boolean usoAtividadeDiscursiva;
    private Boolean usoExercicio;
    private String enunciado;
    private String justificativa;
    private String ajuda;
    private String motivoCancelamento;
    private SituacaoQuestaoEnum situacaoQuestaoEnum;
    private TipoQuestaoEnum tipoQuestaoEnum;
    private Date dataCriacao;
    private Date dataAlteracao;
    private UsuarioVO responsavelCriacao;
    private UsuarioVO responsavelAlteracao;
    private NivelComplexidadeQuestaoEnum nivelComplexidadeQuestao;
    private List<OpcaoRespostaQuestaoVO> opcaoRespostaQuestaoVOs;
    private DisciplinaVO disciplina;
    private TurmaVO turmaVO;
    private List<QuestaoConteudoVO> questaoConteudoVOs;
    private List<QuestaoAssuntoVO> questaoAssuntoVOs;
    private String motivoAnulacaoQuestao;
    
    /**
     * Variavel Transient
     */
    private Boolean selecionado = false;
    private Boolean acertouQuestao = false;
    private Boolean errouQuestao = false;
    private Boolean questaoNaoRespondida = false;
    
    public QuestaoVO clone() throws CloneNotSupportedException{
        QuestaoVO clone =  (QuestaoVO) super.clone();
        clone.setDataAlteracao(null);
        clone.setNovoObj(true);
        clone.setCodigo(0);
        clone.setResponsavelAlteracao(null);        
        clone.setSituacaoQuestaoEnum(SituacaoQuestaoEnum.EM_ELABORACAO);
        clone.setOpcaoRespostaQuestaoVOs(new ArrayList<OpcaoRespostaQuestaoVO>(0));        
        for(OpcaoRespostaQuestaoVO opcaoRespostaQuestaoVO:this.getOpcaoRespostaQuestaoVOs()){
            clone.getOpcaoRespostaQuestaoVOs().add(opcaoRespostaQuestaoVO.clone());
        }
        clone.setQuestaoConteudoVOs(new ArrayList<QuestaoConteudoVO>());
        for(QuestaoConteudoVO questaoConteudoVO: this.getQuestaoConteudoVOs()){
        	QuestaoConteudoVO cloneQuestaoConteudoVO = questaoConteudoVO.clone();
            clone.getQuestaoConteudoVOs().add(cloneQuestaoConteudoVO);
        }
        clone.setQuestaoAssuntoVOs(new ArrayList<QuestaoAssuntoVO>());
        for(QuestaoAssuntoVO questaoAssuntoVO: this.getQuestaoAssuntoVOs()){
        	QuestaoAssuntoVO cloneQuestaoAssuntoVO = questaoAssuntoVO.clone();
            clone.getQuestaoAssuntoVOs().add(cloneQuestaoAssuntoVO);
        }
        return clone;
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

    public Boolean getUsoOnline() {
        if (usoOnline == null) {
            usoOnline = false;
        }
        return usoOnline;
    }

    public void setUsoOnline(Boolean usoOnline) {
        this.usoOnline = usoOnline;
    }

    public Boolean getUsoPresencial() {
        if (usoPresencial == null) {
            usoPresencial = false;
        }
        return usoPresencial;
    }

    public void setUsoPresencial(Boolean usoPresencial) {
        this.usoPresencial = usoPresencial;
    }

    public Boolean getUsoAtividadeDiscursiva() {
        if (usoAtividadeDiscursiva == null) {
            usoAtividadeDiscursiva = false;
        }
        return usoAtividadeDiscursiva;
    }

    public void setUsoAtividadeDiscursiva(Boolean usoAtividadeDiscursiva) {
        this.usoAtividadeDiscursiva = usoAtividadeDiscursiva;
    }

    public Boolean getUsoExercicio() {
        if (usoExercicio == null) {
            usoExercicio = false;
        }
        return usoExercicio;
    }

    public void setUsoExercicio(Boolean usoExercicio) {
        this.usoExercicio = usoExercicio;
    }

    public String getEnunciado() {
        if (enunciado == null) {
            enunciado = "";
        }
        return enunciado;
    }

    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }

    public String getJustificativa() {
        if (justificativa == null) {
            justificativa = "";
        }
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }

    public String getAjuda() {
        if (ajuda == null) {
            ajuda = "";
        }
        return ajuda;
    }

    public void setAjuda(String ajuda) {
        this.ajuda = ajuda;
    }

    public SituacaoQuestaoEnum getSituacaoQuestaoEnum() {
        if(situacaoQuestaoEnum == null) {
        	situacaoQuestaoEnum = SituacaoQuestaoEnum.INATIVA;
        }
        return situacaoQuestaoEnum;
    }

    public void setSituacaoQuestaoEnum(SituacaoQuestaoEnum situacaoQuestaoEnum) {
        this.situacaoQuestaoEnum = situacaoQuestaoEnum;
    }

    public TipoQuestaoEnum getTipoQuestaoEnum() {
        return tipoQuestaoEnum;
    }

    public void setTipoQuestaoEnum(TipoQuestaoEnum tipoQuestaoEnum) {
        this.tipoQuestaoEnum = tipoQuestaoEnum;
    }

    public Date getDataCriacao() {
        if (dataCriacao == null) {
            dataCriacao = new Date();
        }
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Date getDataAlteracao() {
    	if(dataAlteracao == null) {
    		dataAlteracao = new Date();
    	}
        return dataAlteracao;
    }

    public void setDataAlteracao(Date dataAlteracao) {
        this.dataAlteracao = dataAlteracao;
    }

    public UsuarioVO getResponsavelCriacao() {
    	if(responsavelCriacao == null) {
    		responsavelCriacao = new UsuarioVO();
    	}
        return responsavelCriacao;
    }

    public void setResponsavelCriacao(UsuarioVO responsavelCriacao) {
        this.responsavelCriacao = responsavelCriacao;
    }

    public UsuarioVO getResponsavelAlteracao() {
    	if(responsavelAlteracao == null) {
    		responsavelAlteracao = new UsuarioVO();
    	}
        return responsavelAlteracao;
    }

    public void setResponsavelAlteracao(UsuarioVO responsavelAlteracao) {
        this.responsavelAlteracao = responsavelAlteracao;
    }

    public NivelComplexidadeQuestaoEnum getNivelComplexidadeQuestao() {    
    	if(nivelComplexidadeQuestao == null) {
    		nivelComplexidadeQuestao = NivelComplexidadeQuestaoEnum.FACIL;
    	}
        return nivelComplexidadeQuestao;
    }

    public void setNivelComplexidadeQuestao(NivelComplexidadeQuestaoEnum nivelComplexidadeQuestao) {
        this.nivelComplexidadeQuestao = nivelComplexidadeQuestao;
    }

    public List<OpcaoRespostaQuestaoVO> getOpcaoRespostaQuestaoVOs() {
        if (opcaoRespostaQuestaoVOs == null) {
            opcaoRespostaQuestaoVOs = new ArrayList<OpcaoRespostaQuestaoVO>(0);
        }
        return opcaoRespostaQuestaoVOs;
    }

    public void setOpcaoRespostaQuestaoVOs(List<OpcaoRespostaQuestaoVO> opcaoRespostaQuestaoVOs) {
        this.opcaoRespostaQuestaoVOs = opcaoRespostaQuestaoVOs;
    }

    public DisciplinaVO getDisciplina() {
        if (disciplina == null) {
            disciplina = new DisciplinaVO();
        }
        return disciplina;
    }

    public void setDisciplina(DisciplinaVO disciplinaVO) {
        this.disciplina = disciplinaVO;
    }

    public String getMotivoCancelamento() {
        if (motivoCancelamento == null) {
            motivoCancelamento = "";
        }
        return motivoCancelamento;
    }

    public void setMotivoCancelamento(String motivoCancelamento) {
        this.motivoCancelamento = motivoCancelamento;
    }

    private String usoQuestaoApresentar;

    public String getUsoQuestaoApresentar() {
        if (usoQuestaoApresentar == null) {
            if (getUsoAtividadeDiscursiva()) {
                usoQuestaoApresentar = "Atividade Discursiva";
            } else {
                if (getUsoExercicio()) {
                    usoQuestaoApresentar = "Exercício";
                }
                if (getUsoOnline()) {
                    usoQuestaoApresentar = usoQuestaoApresentar == null? "On-line" : usoQuestaoApresentar+"/On-line";
                }
                if (getUsoOnline()) {
                    usoQuestaoApresentar = usoQuestaoApresentar == null? "Presencial" : usoQuestaoApresentar+"/Presencial";
                }
            }
        }
        return usoQuestaoApresentar;
    }
    
    public Integer getNumeroOpcoes(){
        return getOpcaoRespostaQuestaoVOs().size();
    }

    
    public void setUsoQuestaoApresentar(String usoQuestaoApresentar) {
        this.usoQuestaoApresentar = usoQuestaoApresentar;
    }

    
    public Boolean getSelecionado() {
        if(selecionado == null){
            selecionado = false;
        }
        return selecionado;
    }

    
    public void setSelecionado(Boolean selecionado) {
        this.selecionado = selecionado;
    }
    
    
    public Boolean getIsExisteAjuda(){
        return getAjuda() != null && !getAjuda().trim().isEmpty();
    }
    
    public Boolean getIsExisteJustificativa(){
        return getJustificativa() != null && !getJustificativa().trim().isEmpty();
    }

    
    public Boolean getAcertouQuestao() {
        if(acertouQuestao == null){
            acertouQuestao = false;
        }
        return acertouQuestao;
    }

    
    public void setAcertouQuestao(Boolean acertouQuestao) {
        this.acertouQuestao = acertouQuestao;
    }

    
    public Boolean getErrouQuestao() {
        if(errouQuestao == null){
            errouQuestao = false;
        }
        return errouQuestao;
    }

    
    public void setErrouQuestao(Boolean errouQuestao) {
        this.errouQuestao = errouQuestao;
    }

    
    public Boolean getQuestaoNaoRespondida() {
        if(questaoNaoRespondida == null){
            questaoNaoRespondida = false;
        }
        return questaoNaoRespondida;
    }

    
    public void setQuestaoNaoRespondida(Boolean questaoNaoRespondida) {
        this.questaoNaoRespondida = questaoNaoRespondida;
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

	public List<QuestaoConteudoVO> getQuestaoConteudoVOs() {
		if (questaoConteudoVOs == null) {
			questaoConteudoVOs = new ArrayList<QuestaoConteudoVO>();
		}
		return questaoConteudoVOs;
	}

	public void setQuestaoConteudoVOs(List<QuestaoConteudoVO> questaoConteudoVOs) {
		this.questaoConteudoVOs = questaoConteudoVOs;
	}

	public List<QuestaoAssuntoVO> getQuestaoAssuntoVOs() {
		if(questaoAssuntoVOs == null) {
			questaoAssuntoVOs = new ArrayList<QuestaoAssuntoVO>();
		}
		return questaoAssuntoVOs;
	}

	public void setQuestaoAssuntoVOs(List<QuestaoAssuntoVO> questaoAssuntoVOs) {
		this.questaoAssuntoVOs = questaoAssuntoVOs;
	}
	
	public String getMotivoAnulacaoQuestao() {
		if (motivoAnulacaoQuestao == null) {
			motivoAnulacaoQuestao = "";
		}
		return motivoAnulacaoQuestao;
	}

	public void setMotivoAnulacaoQuestao(String motivoAnulacaoQuestao) {
		this.motivoAnulacaoQuestao = motivoAnulacaoQuestao;
	}
}
