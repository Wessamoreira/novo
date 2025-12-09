package negocio.comuns.crm;
import java.util.Date;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.arquitetura.SuperVO;

/**
 * Reponsável por manter os dados da entidade RegistroEntrada. Classe do tipo VO - Value Object 
 * composta pelos atributos da entidade com visibilidade protegida e os métodos de acesso a estes atributos.
 * Classe utilizada para apresentar e manter em memória os dados desta entidade.
 * @see SuperVO
*/

public class ProspectIndicacaoVO extends SuperVO {
	
    private Integer codigo;
    private Date dataIndicacao;
    private ProspectsVO quemIndicou;
    protected CursoVO cursoIndicado;
	
    /**
     * Construtor padrão da classe <code>ProspectIndicacaoVO</code>.
     * Cria uma nova instância desta entidade, inicializando automaticamente seus atributos (Classe VO).
    */
    public ProspectIndicacaoVO() {
        super();
    }
     
    /**
     * Retorna o objeto da classe <code>Curso</code> relacionado com (<code>RegistroEntrada</code>).
    */
    public CursoVO getCursoIndicado() {
        if (cursoIndicado == null) {
            cursoIndicado = new CursoVO();
        }
        return (cursoIndicado);
    }
     
    /**
     * Define o objeto da classe <code>Curso</code> relacionado com (<code>RegistroEntrada</code>).
    */
    public void setCursoIndicado( CursoVO obj) {
        this.cursoIndicado = obj;
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
     * @param codigo the codigo to set
     */
    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    /**
     * @return the dataIndicacao
     */
    public Date getDataIndicacao() {
        if (dataIndicacao == null) {
            dataIndicacao = new Date();
        }
        return dataIndicacao;
    }

    /**
     * @param dataIndicacao the dataIndicacao to set
     */
    public void setDataIndicacao(Date dataIndicacao) {
        this.dataIndicacao = dataIndicacao;
    }

    /**
     * @return the quemIndicou
     */
    public ProspectsVO getQuemIndicou() {
        if (quemIndicou == null) {
            quemIndicou = new ProspectsVO();
        }
        return quemIndicou;
    }

    /**
     * @param quemIndicou the quemIndicou to set
     */
    public void setQuemIndicou(ProspectsVO quemIndicou) {
        this.quemIndicou = quemIndicou;
    }
   
}