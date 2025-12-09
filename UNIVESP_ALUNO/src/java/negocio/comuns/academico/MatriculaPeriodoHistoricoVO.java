package negocio.comuns.academico;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;

/**
 * Reponsável por manter os dados da entidade MatriculaPeriodoTurmaDisciplina.
 * Classe do tipo VO - Value Object composta pelos atributos da entidade com
 * visibilidade protegida e os métodos de acesso a estes atributos. Classe
 * utilizada para apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class MatriculaPeriodoHistoricoVO extends SuperVO {

    protected Integer codigo;
    protected MatriculaPeriodoVO matriculaPeriodo;
    protected String descricaoOperacao;
    protected Date dataOperacao;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>MatriculaPeriodoTurmaDisciplina</code>.
     * Cria uma nova instância desta entidade, inicializando automaticamente
     * seus atributos (Classe VO).
     */
    public MatriculaPeriodoHistoricoVO() {
        super();
    }

    /**
     * @return the codigo
     */
    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return codigo;
    }

    /**
     * @param codigo
     *            the codigo to set
     */
    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    /**
     * @return the matriculaPeriodoVO
     */
    public MatriculaPeriodoVO getMatriculaPeriodo() {
        if (matriculaPeriodo == null) {
            matriculaPeriodo = new MatriculaPeriodoVO();
        }
        return matriculaPeriodo;
    }

    /**
     * @param matriculaPeriodoVO
     *            the matriculaPeriodoVO to set
     */
    public void setMatriculaPeriodo(MatriculaPeriodoVO matriculaPeriodoVO) {
        this.matriculaPeriodo = matriculaPeriodoVO;
    }

    /**
     * @return the descricaoOperacao
     */
    public String getDescricaoOperacao() {
        if (descricaoOperacao == null) {
            descricaoOperacao = "";
        }
        return descricaoOperacao;
    }

    /**
     * @param descricaoOperacao
     *            the descricaoOperacao to set
     */
    public void setDescricaoOperacao(String descricaoOperacao) {
        this.descricaoOperacao = descricaoOperacao;
    }

    /**
     * @return the dataOperacao
     */
    public Date getDataOperacao() {
        if (dataOperacao == null) {
            dataOperacao = new Date();
        }
        return dataOperacao;
    }

    /**
     * @param dataOperacao
     *            the dataOperacao to set
     */
    public void setDataOperacao(Date dataOperacao) {
        this.dataOperacao = dataOperacao;
    }
}
