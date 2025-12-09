package negocio.comuns.academico;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade PlanoCurso. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class PlanoCursoVO extends SuperVO {

    private Integer codigo;
    private String objetivosEspecificos;
    private String objetivosGerais;
    private String estrategiasAvaliacao;
    private String metodologia;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>PlanoCurso</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public PlanoCursoVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>PlanoCursoVO</code>. Todos os tipos de consistência de dados são e
     * devem ser implementadas neste método. São validações típicas: verificação
     * de campos obrigatórios, verificação de valores válidos para os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(PlanoCursoVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
//		if (obj.getObjetivosEspecificos().equals("")) {
//			throw new ConsistirException("O campo OBJETIVOS ESPECÍFICOS (PlanoCurso) deve ser informado.");
//		}
//		if (obj.getObjetivosGerais().equals("")) {
//			throw new ConsistirException("O campo OBJETIVOS GERAIS (PlanoCurso) deve ser informado.");
//		}
//		if (obj.getEstrategiasAvaliacao().equals("")) {
//			throw new ConsistirException("O campo ESTRATÉGIAS DE AVALIAÇÃO (PlanoCurso) deve ser informado.");
//		}
//		if (obj.getMetodologia().equals("")) {
//			throw new ConsistirException("O campo METODOLOGIA (PlanoCurso) deve ser informado.");
//		}
    }

    /**
     * Operação reponsável por realizar o UpperCase dos atributos do tipo
     * String.
     */
    public void realizarUpperCaseDados() {
        if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
            return;
        }
        setObjetivosEspecificos(getObjetivosEspecificos().toUpperCase());
        setObjetivosGerais(getObjetivosGerais().toUpperCase());
        setEstrategiasAvaliacao(getEstrategiasAvaliacao().toUpperCase());
        setMetodologia(getMetodologia().toUpperCase());
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(new Integer(0));
        setObjetivosEspecificos("");
        setObjetivosGerais("");
        setEstrategiasAvaliacao("");
        setMetodologia("");
    }

    public String getMetodologia() {
        if (metodologia == null) {
            metodologia = "";
        }
        return (metodologia);
    }

    public void setMetodologia(String metodologia) {
        this.metodologia = metodologia;
    }

    public String getEstrategiasAvaliacao() {
        if (estrategiasAvaliacao == null) {
            estrategiasAvaliacao = "";
        }
        return (estrategiasAvaliacao);
    }

    public void setEstrategiasAvaliacao(String estrategiasAvaliacao) {
        this.estrategiasAvaliacao = estrategiasAvaliacao;
    }

    public String getObjetivosGerais() {
        if (objetivosGerais == null) {
            objetivosGerais = "";
        }
        return (objetivosGerais);
    }

    public void setObjetivosGerais(String objetivosGerais) {
        this.objetivosGerais = objetivosGerais;
    }

    public String getObjetivosEspecificos() {
        if (objetivosEspecificos == null) {
            objetivosEspecificos = "";
        }
        return (objetivosEspecificos);
    }

    public void setObjetivosEspecificos(String objetivosEspecificos) {
        this.objetivosEspecificos = objetivosEspecificos;
    }

    public Integer getCodigo() {
        if (codigo == null) {
            codigo = new Integer(0);
        }
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
}
