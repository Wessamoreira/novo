package negocio.comuns.bancocurriculum;

import java.util.Date;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;
import negocio.comuns.utilitarias.Uteis;

public class LinkVO extends SuperVO{
    
    private static final long serialVersionUID = 2689938174307444905L;
    
    private Integer codigo;
    private Date dataInicio;
    private Date dataFim;
    private UnidadeEnsinoVO unidadeEnsino;    
    private String escopo;
    private String link;
    private String caminhoFotoUsuario;
    private ArquivoVO icone;
    private StatusAtivoInativoEnum situacao;

    public LinkVO() {
        super();

    }

    public Integer getCodigo() {
        if(codigo == null){
            codigo = 0;
        }
        return codigo;
    }
    
    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
    
    public Date getDataInicio() {
        if (dataInicio == null) {
            dataInicio = new Date();
        }
        return dataInicio;
    }

    public String getDataInicio_Apresentar() {
        return (Uteis.getData(getDataInicio()));
    }

    public String getDataFimApresentar() {
        return (Uteis.getData(getDataFim()));
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }
    
    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public UnidadeEnsinoVO getUnidadeEnsino() {
        if(unidadeEnsino == null){
            unidadeEnsino = new UnidadeEnsinoVO();
        }
        return unidadeEnsino;
    }

    
    public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
        this.unidadeEnsino = unidadeEnsino;
    }
    
    public ArquivoVO getIcone() {
        if (icone == null) {
            icone = new ArquivoVO();
        }
        return icone;
    }

    public void setIcone(ArquivoVO icone) {
        this.icone = icone;
    }

    /**
     * @return the escopo
     */
    public String getEscopo() {
        if (escopo == null) {
            escopo = "";
        }
        return escopo;
    }

    public String getEscopo_Apresentar() {
        if (escopo.equals("AM")) {
            return "Ambos";
        }
        if (escopo.equals("AL")) {
            return "Aluno";
        }
        if (escopo.equals("PA")) {
            return "Parceiro";
        }
        return "Ambos";
    }

    /**
     * @param escopo the escopo to set
     */
    public void setEscopo(String escopo) {
        this.escopo = escopo;
    }

    /**
     * @return the link
     */
    public String getLink() {
        if (link == null) {
            link = "";
        }
        return link;
    }

    /**
     * @param link the link to set
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * @return the situacao
     */
    public StatusAtivoInativoEnum getSituacao() {
        if(situacao == null){
            situacao = StatusAtivoInativoEnum.ATIVO;
        }
        return situacao;
    }

    /**
     * @param situacao the situacao to set
     */
    public void setSituacao(StatusAtivoInativoEnum situacao) {
        this.situacao = situacao;
    }

    public Boolean getIsAtivo(){
        return !getNovoObj() && getSituacao().equals(StatusAtivoInativoEnum.ATIVO);
    }

    public Boolean getIsInativo(){
        return !getNovoObj() && getSituacao().equals(StatusAtivoInativoEnum.INATIVO);
    }

    /**
     * @return the caminhoFotoUsuario
     */
    public String getCaminhoFotoUsuario() {
        if (caminhoFotoUsuario == null) {
            caminhoFotoUsuario = "";
        }
        return caminhoFotoUsuario;
    }

    /**
     * @param caminhoFotoUsuario the caminhoFotoUsuario to set
     */
    public void setCaminhoFotoUsuario(String caminhoFotoUsuario) {
        this.caminhoFotoUsuario = caminhoFotoUsuario;
    }


}