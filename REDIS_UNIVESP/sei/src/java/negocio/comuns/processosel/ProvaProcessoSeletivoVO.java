package negocio.comuns.processosel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.enumeradores.SituacaoProvaProcessoSeletivoEnum;
import negocio.comuns.utilitarias.Uteis;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;


@XmlRootElement(name = "provaProcessoSeletivoVO")
public class ProvaProcessoSeletivoVO extends SuperVO {

    private Integer codigo;
    private SituacaoProvaProcessoSeletivoEnum situacaoProvaProcessoSeletivo;
    private String descricao;
    private Date dataCriacao;
    private Date dataAlteracao;
    private UsuarioVO responsavelCriacao;
    private UsuarioVO responsavelAlteracao;
    private List<QuestaoProvaProcessoSeletivoVO> questaoProvaProcessoSeletivoVOs;
    private Boolean possuiRedacao;
    private String temaRedacao;
    private Integer quantidadeMaximaCaracteresRedacao;    
    /**
     * Variaveis Transientes 
     */
    private Integer numeroAcertos;
    private Integer numeroErros;
    
    private Boolean conteudoAlterado;
    private GrupoDisciplinaProcSeletivoVO grupoDisciplinaProcSeletivoVO;

    
    
    public ProvaProcessoSeletivoVO clone(ProvaProcessoSeletivoVO processoSeletivoVO) throws CloneNotSupportedException{
        ProvaProcessoSeletivoVO clone = (ProvaProcessoSeletivoVO) super.clone();
        clone.setCodigo(0);
        clone.setNovoObj(false);
        clone.setResponsavelCriacao(new UsuarioVO());
        clone.setDataCriacao(new Date());
        clone.setSituacaoProvaProcessoSeletivo(SituacaoProvaProcessoSeletivoEnum.EM_ELABORACAO);
        clone.setQuestaoProvaProcessoSeletivoVOs(new ArrayList<QuestaoProvaProcessoSeletivoVO>(0));
        clone.setDescricao(processoSeletivoVO.getDescricao() + " - Clonado");
        for(QuestaoProvaProcessoSeletivoVO questaoProvaProcessoSeletivoVO:this.getQuestaoProvaProcessoSeletivoVOs()){
            QuestaoProvaProcessoSeletivoVO cloneQuestaoProvaProcessoSeletivo = questaoProvaProcessoSeletivoVO.clone();
            clone.getQuestaoProvaProcessoSeletivoVOs().add(cloneQuestaoProvaProcessoSeletivo);
        }
        return clone;            
    }
    
    @XmlElement(name = "codigo")
    public Integer getCodigo() {
        if(codigo == null){
            codigo = 0;
        }
        return codigo;
    }
    
    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
    
    
    @XmlElement(name = "descricao")
    public String getDescricao() {
        if(descricao == null){
            descricao = "";
        }
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public Date getDataCriacao() {
        if(dataCriacao == null){
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
        if(responsavelCriacao == null){
            responsavelCriacao = new UsuarioVO();
        }
        return responsavelCriacao;
    }
    
    public void setResponsavelCriacao(UsuarioVO responsavelCriacao) {
        this.responsavelCriacao = responsavelCriacao;
    }
    
    public UsuarioVO getResponsavelAlteracao() {
        if(responsavelAlteracao == null){
            responsavelAlteracao = new UsuarioVO();
        }
        return responsavelAlteracao;
    }
    
    public void setResponsavelAlteracao(UsuarioVO responsavelAlteracao) {
        this.responsavelAlteracao = responsavelAlteracao;
    }
    
   

    
   
    
    public Integer getNumeroQuestoes(){
        return getQuestaoProvaProcessoSeletivoVOs().size();
    }
    
    
    public Integer getNumeroAcertos() {
        if(numeroAcertos == null){
            numeroAcertos = 0;
        }
        return numeroAcertos;
    }

    
    public void setNumeroAcertos(Integer numeroAcertos) {
        this.numeroAcertos = numeroAcertos;
    }

    
    public Integer getNumeroErros() {
        if(numeroErros == null){
            numeroErros = 0;
        }
        return numeroErros;
    }

    
    public void setNumeroErros(Integer numeroErros) {
        this.numeroErros = numeroErros;
    }
    
    public String getPorcentagemAcertos(){
        return Uteis.getDoubleFormatado(Uteis.arrendondarForcando2CadasDecimais((getNumeroAcertos().doubleValue()*100)/getQuestaoProvaProcessoSeletivoVOs().size()))+"%";
    }
    
    public String getPorcentagemErros(){
        return Uteis.getDoubleFormatado(Uteis.arrendondarForcando2CadasDecimais((getNumeroErros().doubleValue()*100)/getQuestaoProvaProcessoSeletivoVOs().size()))+"%";
    }

    
    public Boolean getConteudoAlterado() {
        if(conteudoAlterado == null){
            conteudoAlterado = false;
        }
        return conteudoAlterado;
    }

    
    public void setConteudoAlterado(Boolean conteudoAlterado) {
        this.conteudoAlterado = conteudoAlterado;
    }

    
    public SituacaoProvaProcessoSeletivoEnum getSituacaoProvaProcessoSeletivo() {
        if(situacaoProvaProcessoSeletivo == null) {
        	situacaoProvaProcessoSeletivo = SituacaoProvaProcessoSeletivoEnum.EM_ELABORACAO;
        }
        return situacaoProvaProcessoSeletivo;
    }

    
    public void setSituacaoProvaProcessoSeletivo(SituacaoProvaProcessoSeletivoEnum situacaoProvaProcessoSeletivo) {
        this.situacaoProvaProcessoSeletivo = situacaoProvaProcessoSeletivo;
    }

    @XmlElement(name = "questaoProvaProcessoSeletivoVOs")
    public List<QuestaoProvaProcessoSeletivoVO> getQuestaoProvaProcessoSeletivoVOs() {
        if(questaoProvaProcessoSeletivoVOs == null){
            questaoProvaProcessoSeletivoVOs = new ArrayList<QuestaoProvaProcessoSeletivoVO>(0);
        }
        return questaoProvaProcessoSeletivoVOs;
    }

    
    public void setQuestaoProvaProcessoSeletivoVOs(List<QuestaoProvaProcessoSeletivoVO> questaoProvaProcessoSeletivoVOs) {
        this.questaoProvaProcessoSeletivoVOs = questaoProvaProcessoSeletivoVOs;
    }
    
    
    public JRDataSource getQuestaoProvaProcessoSeletivoVOsJrDataSource() {
        return new JRBeanArrayDataSource(getQuestaoProvaProcessoSeletivoVOs().toArray());
    }

    @XmlElement(name = "grupoDisciplinaProcSeletivoVO")
	public GrupoDisciplinaProcSeletivoVO getGrupoDisciplinaProcSeletivoVO() {
		if (grupoDisciplinaProcSeletivoVO == null) {
			grupoDisciplinaProcSeletivoVO = new GrupoDisciplinaProcSeletivoVO();
		}
		return grupoDisciplinaProcSeletivoVO;
	}

	public void setGrupoDisciplinaProcSeletivoVO(GrupoDisciplinaProcSeletivoVO grupoDisciplinaProcSeletivoVO) {
		this.grupoDisciplinaProcSeletivoVO = grupoDisciplinaProcSeletivoVO;
	}

	@XmlElement(name = "possuiRedacao")
	public Boolean getPossuiRedacao() {
		if (possuiRedacao == null) {
			possuiRedacao = false;
		}
		return possuiRedacao;
	}

	public void setPossuiRedacao(Boolean possuiRedacao) {
		this.possuiRedacao = possuiRedacao;
	}

	@XmlElement(name = "temaRedacao")
	public String getTemaRedacao() {
		if (temaRedacao == null) {
			temaRedacao = "";
		}
		return temaRedacao;
	}

	public void setTemaRedacao(String temaRedacao) {
		this.temaRedacao = temaRedacao;
	}

	@XmlElement(name = "quantidadeMaximaCaracteresRedacao")
	public Integer getQuantidadeMaximaCaracteresRedacao() {
		if (quantidadeMaximaCaracteresRedacao == null) {
			quantidadeMaximaCaracteresRedacao = 0;
		}
		return quantidadeMaximaCaracteresRedacao;
	}

	public void setQuantidadeMaximaCaracteresRedacao(Integer quantidadeMaximaCaracteresRedacao) {
		this.quantidadeMaximaCaracteresRedacao = quantidadeMaximaCaracteresRedacao;
	}
	
}
