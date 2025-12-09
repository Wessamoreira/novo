package negocio.comuns.academico;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.academico.Matricula;

/**
 * Reponsável por manter os dados da entidade DocumetacaoMatricula. Classe do
 * tipo VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 * @see Matricula
 */
public class LogMatriculaVO extends SuperVO {

    private Integer codigo;
    private Date dataAlteracao;
    private UsuarioVO responsavel;
    private String acao;
    private String matricula;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>DocumetacaoMatricula</code>. Cria uma
     * nova instância desta entidade, inicializando automaticamente seus
     * atributos (Classe VO).
     */
    public LogMatriculaVO() {
        super();
    }


    public String getMatricula() {
        if (matricula == null) {
            matricula = "";
        }
        return (matricula);
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    /**
     * @return the dataAlteracao
     */
    public Date getDataAlteracao() {
        if (dataAlteracao == null) {
            dataAlteracao = new Date();
        }
        return dataAlteracao;
    }

    /**
     * @param dataAlteracao the dataAlteracao to set
     */
    public void setDataAlteracao(Date dataAlteracao) {
        this.dataAlteracao = dataAlteracao;
    }

    /**
     * @return the responsavel
     */
    public UsuarioVO getResponsavel() {
        if (responsavel == null) {
            responsavel = new UsuarioVO();
        }
        return responsavel;
    }

    /**
     * @param responsavel the responsavel to set
     */
    public void setResponsavel(UsuarioVO responsavel) {
        this.responsavel = responsavel;
    }

    /**
     * @return the acao
     */
    public String getAcao() {
        if (acao == null) {
            acao = "";
        }
        return acao;
    }

    /**
     * @param acao the acao to set
     */
    public void setAcao(String acao) {
        this.acao = acao;
    }

    public String getDataAlteracao_Apresentar() {
        if (dataAlteracao == null) {
            return "";
        }
        return (Uteis.getData(dataAlteracao));
    }

}
