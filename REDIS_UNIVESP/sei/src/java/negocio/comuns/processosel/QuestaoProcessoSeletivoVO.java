package negocio.comuns.processosel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.enumeradores.NivelComplexidadeQuestaoEnum;
import negocio.comuns.ead.enumeradores.SituacaoQuestaoEnum;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;


@XmlRootElement(name = "questaoProcessoSeletivoVO")
public class QuestaoProcessoSeletivoVO extends SuperVO {
    
    
    private Integer codigo;
    private String enunciado;
    private String justificativa;
    private String ajuda;
    private String motivoCancelamento;
    private SituacaoQuestaoEnum situacaoQuestaoEnum;
    private Date dataCriacao;
    private Date dataAlteracao;
    private UsuarioVO responsavelCriacao;
    private UsuarioVO responsavelAlteracao;
    private NivelComplexidadeQuestaoEnum nivelComplexidadeQuestao;
    private List<OpcaoRespostaQuestaoProcessoSeletivoVO> opcaoRespostaQuestaoProcessoSeletivoVOs;
    private DisciplinasProcSeletivoVO disciplinaProcSeletivo;
    
    /**
     * Variavel Transient
     */
    private Boolean selecionado = false;
    private Boolean acertouQuestao = false;
    private Boolean errouQuestao = false;
    private Boolean questaoNaoRespondida = false;
    
    public QuestaoProcessoSeletivoVO clone() throws CloneNotSupportedException{
        QuestaoProcessoSeletivoVO clone =  (QuestaoProcessoSeletivoVO) super.clone();
        clone.setDataAlteracao(null);
        clone.setNovoObj(true);
        clone.setCodigo(0);
        clone.setResponsavelAlteracao(null);        
        clone.setSituacaoQuestaoEnum(SituacaoQuestaoEnum.EM_ELABORACAO);
        clone.setOpcaoRespostaQuestaoProcessoSeletivoVOs(new ArrayList<OpcaoRespostaQuestaoProcessoSeletivoVO>(0));        
        for(OpcaoRespostaQuestaoProcessoSeletivoVO opcaoRespostaQuestaoProcessoSeletivoVO:this.getOpcaoRespostaQuestaoProcessoSeletivoVOs()){
            clone.getOpcaoRespostaQuestaoProcessoSeletivoVOs().add(opcaoRespostaQuestaoProcessoSeletivoVO.clone());
        }
        return clone;
    }

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
   
    @XmlElement(name = "enunciado")
    public String getEnunciado() {
        if (enunciado == null) {
            enunciado = "";
        }
        return enunciado;
    }

    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }

    @XmlElement(name = "justificativa")
    public String getJustificativa() {
        if (justificativa == null) {
            justificativa = "";
        }
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }

    @XmlElement(name = "ajuda")
    public String getAjuda() {
        if (ajuda == null) {
            ajuda = "";
        }
        return ajuda;
    }

    public void setAjuda(String ajuda) {
        this.ajuda = ajuda;
    }

    @XmlElement(name = "situacaoQuestaoEnum")
    public SituacaoQuestaoEnum getSituacaoQuestaoEnum() {
        
        return situacaoQuestaoEnum;
    }

    public void setSituacaoQuestaoEnum(SituacaoQuestaoEnum situacaoQuestaoEnum) {
        this.situacaoQuestaoEnum = situacaoQuestaoEnum;
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
        return dataAlteracao;
    }

    public void setDataAlteracao(Date dataAlteracao) {
        this.dataAlteracao = dataAlteracao;
    }

    public UsuarioVO getResponsavelCriacao() {
        return responsavelCriacao;
    }

    public void setResponsavelCriacao(UsuarioVO responsavelCriacao) {
        this.responsavelCriacao = responsavelCriacao;
    }

    public UsuarioVO getResponsavelAlteracao() {
        return responsavelAlteracao;
    }

    public void setResponsavelAlteracao(UsuarioVO responsavelAlteracao) {
        this.responsavelAlteracao = responsavelAlteracao;
    }

    @XmlElement(name = "nivelComplexidadeQuestao")
    public NivelComplexidadeQuestaoEnum getNivelComplexidadeQuestao() {        
        return nivelComplexidadeQuestao;
    }

    public void setNivelComplexidadeQuestao(NivelComplexidadeQuestaoEnum nivelComplexidadeQuestao) {
        this.nivelComplexidadeQuestao = nivelComplexidadeQuestao;
    }

    

    @XmlElement(name = "opcaoRespostaQuestaoProcessoSeletivoVOs")
    public List<OpcaoRespostaQuestaoProcessoSeletivoVO> getOpcaoRespostaQuestaoProcessoSeletivoVOs() {
        if(opcaoRespostaQuestaoProcessoSeletivoVOs == null){
            opcaoRespostaQuestaoProcessoSeletivoVOs = new ArrayList<OpcaoRespostaQuestaoProcessoSeletivoVO>(0);
        }
        return opcaoRespostaQuestaoProcessoSeletivoVOs;
    }

    
    public void setOpcaoRespostaQuestaoProcessoSeletivoVOs(List<OpcaoRespostaQuestaoProcessoSeletivoVO> opcaoRespostaQuestaoProcessoSeletivoVOs) {
        this.opcaoRespostaQuestaoProcessoSeletivoVOs = opcaoRespostaQuestaoProcessoSeletivoVOs;
    }

    
    @XmlElement(name = "disciplinaProcSeletivo")
    public DisciplinasProcSeletivoVO getDisciplinaProcSeletivo() {
        if(disciplinaProcSeletivo == null){
            disciplinaProcSeletivo = new DisciplinasProcSeletivoVO();
        }
        return disciplinaProcSeletivo;
    }

    
    public void setDisciplinaProcSeletivo(DisciplinasProcSeletivoVO disciplinaProcSeletivo) {
        this.disciplinaProcSeletivo = disciplinaProcSeletivo;
    }

    @XmlElement(name = "motivoCancelamento")
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

   
    public Integer getNumeroOpcoes(){
        return getOpcaoRespostaQuestaoProcessoSeletivoVOs().size();
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

    
    @XmlElement(name = "acertouQuestao")
    public Boolean getAcertouQuestao() {
        if(acertouQuestao == null){
            acertouQuestao = false;
        }
        return acertouQuestao;
    }

    
    public void setAcertouQuestao(Boolean acertouQuestao) {
        this.acertouQuestao = acertouQuestao;
    }
    
    @XmlElement(name = "errouQuestao")
    public Boolean getErrouQuestao() {
        if(errouQuestao == null){
            errouQuestao = false;
        }
        return errouQuestao;
    }

    
    public void setErrouQuestao(Boolean errouQuestao) {
        this.errouQuestao = errouQuestao;
    }

    
    @XmlElement(name = "questaoNaoRespondida")
    public Boolean getQuestaoNaoRespondida() {
        if(questaoNaoRespondida == null){
            questaoNaoRespondida = false;
        }
        return questaoNaoRespondida;
    }

    
    public void setQuestaoNaoRespondida(Boolean questaoNaoRespondida) {
        this.questaoNaoRespondida = questaoNaoRespondida;
    }
    
    public JRDataSource getOpcaoRespostaQuestaoProcessoSeletivoVOsJrDataSource() {
        return new JRBeanArrayDataSource(getOpcaoRespostaQuestaoProcessoSeletivoVOs().toArray());
    }

}
