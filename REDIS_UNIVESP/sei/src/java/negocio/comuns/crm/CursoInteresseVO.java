package negocio.comuns.crm;
import java.util.Date;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.crm.Prospects;

/**
 * Reponsável por manter os dados da entidade CursoInteresse. Classe do tipo VO - Value Object 
 * composta pelos atributos da entidade com visibilidade protegida e os métodos de acesso a estes atributos.
 * Classe utilizada para apresentar e manter em memória os dados desta entidade.
 * @see SuperVO
 * @see Prospects
*/

public class CursoInteresseVO extends SuperVO {
	
    protected Integer codigo;
    protected Date dataCadastro;
    protected ProspectsVO prospects;
    /** Atributo responsável por manter o objeto relacionado da classe <code>Curso </code>.*/
    protected CursoVO curso;
    protected TurnoVO turno;
	
    /**
     * Construtor padrão da classe <code>CursoInteresse</code>.
     * Cria uma nova instância desta entidade, inicializando automaticamente seus atributos (Classe VO).
    */
    public CursoInteresseVO() {
        super();
    }
     
	
	

    /**
     * Retorna o objeto da classe <code>Curso</code> relacionado com (<code>CursoInteresse</code>).
    */
    public CursoVO getCurso() {
        if (curso == null) {
            curso = new CursoVO();
        }
        return (curso);
    }
     
    /**
     * Define o objeto da classe <code>Curso</code> relacionado com (<code>CursoInteresse</code>).
    */
    public void setCurso( CursoVO obj) {
        this.curso = obj;
    }

    public TurnoVO getTurno() {
    	if (turno == null) {
    		turno = new TurnoVO();
    	}
    	return (turno);
    }
    
    public void setTurno( TurnoVO obj) {
    	this.turno = obj;
    }
    
    public ProspectsVO getProspects() {
        if (prospects == null) {
            prospects = new ProspectsVO();
        }
        return (prospects);
    }
     
    public void setProspects( ProspectsVO prospects ) {
        this.prospects = prospects;
    }

    public Date getDataCadastro() {
        if (dataCadastro == null) {
            dataCadastro = new Date();
        }
        return (dataCadastro);
    }
     
    /**
     * Operação responsável por retornar um atributo do tipo data no formato padrão dd/mm/aaaa. 
    */
    public String getDataCadastro_Apresentar() {
        return (Uteis.getData(dataCadastro));
    }
     
    public void setDataCadastro( Date dataCadastro ) {
        this.dataCadastro = dataCadastro;
    }

    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return (codigo);
    }
     
    public void setCodigo( Integer codigo ) {
        this.codigo = codigo;
    }
}