package negocio.comuns.processosel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.facade.jdbc.processosel.ProcSeletivo;

/**
 * Reponsável por manter os dados da entidade ProcSeletivoUnidadeEnsino. Classe
 * do tipo VO - Value Object composta pelos atributos da entidade com
 * visibilidade protegida e os métodos de acesso a estes atributos. Classe
 * utilizada para apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 * @see ProcSeletivo
 */
public class ProcSeletivoUnidadeEnsinoVO extends SuperVO {

    private ProcSeletivoVO procSeletivo;
    private Integer codigo;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>UnidadeEnsino </code>.
     */
    private UnidadeEnsinoVO unidadeEnsino;
    /**
     * Atributo responsável por manter os objetos da classe
     * <code>ProcSeletivoCurso</code>.
     */
    private List<ProcSeletivoCursoVO> procSeletivoCursoVOs;
    private List<ProcSeletivoUnidadeEnsinoEixoCursoVO> procSeletivoUnidadeEnsinoEixoCursoVOs;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>ProcSeletivoUnidadeEnsino</code>. Cria
     * uma nova instância desta entidade, inicializando automaticamente seus
     * atributos (Classe VO).
     */
    public ProcSeletivoUnidadeEnsinoVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>ProcSeletivoUnidadeEnsinoVO</code>. Todos os tipos de consistência
     * de dados são e devem ser implementadas neste método. São validações
     * típicas: verificação de campos obrigatórios, verificação de valores
     * válidos para os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(ProcSeletivoUnidadeEnsinoVO obj) throws ConsistirException {
        if ((obj.getUnidadeEnsino() == null) || (obj.getUnidadeEnsino().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo UNIDADE ENSINO (Unidade de Ensino Processo Seletivo) deve ser informado.");
        }
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
    }

    public void adicionarObjProcSeletivoUnidadeEnsinoCursoVOs(ProcSeletivoCursoVO obj) throws Exception {
        ProcSeletivoCursoVO.validarDados(obj);
        int index = 0;
        Iterator<ProcSeletivoCursoVO> i = getProcSeletivoCursoVOs().iterator();
        while (i.hasNext()) {
            ProcSeletivoCursoVO objExistente = (ProcSeletivoCursoVO) i.next();
            if (objExistente.getUnidadeEnsinoCurso().getCodigo().equals(obj.getUnidadeEnsinoCurso().getCodigo())) {
                getProcSeletivoCursoVOs().set(index, obj);
                return;
            }
            index++;
        }
        getProcSeletivoCursoVOs().add(obj);
        Ordenacao.ordenarLista(getProcSeletivoCursoVOs(), "ordenacao");
        // adicionarObjSubordinadoOC
    }

    /**
     * Operação responsável por excluir um objeto da classe
     * <code>ProcSeletivoCursoVO</code> no List
     * <code>procSeletivoCursoVOs</code>. Utiliza o atributo padrão de consulta
     * da classe <code>ProcSeletivoCurso</code> - getCurso().getCodigo() - como
     * identificador (key) do objeto no List.
     *
     * @param curso
     *            Parâmetro para localizar e remover o objeto do List.
     */
    public void excluirObjProcSeletivoCursoVOs(ProcSeletivoCursoVO obj) throws Exception {
        int index = 0;
        Iterator<ProcSeletivoCursoVO> i = getProcSeletivoCursoVOs().iterator();
        while (i.hasNext()) {
            ProcSeletivoCursoVO objExistente = (ProcSeletivoCursoVO) i.next();
            if (objExistente.getUnidadeEnsinoCurso().getCodigo().equals(obj.getUnidadeEnsinoCurso().getCodigo())) {
                getProcSeletivoCursoVOs().remove(index);
                return;
            }
            index++;
        }
        // excluirObjSubordinadoOC
    }

    public ProcSeletivoCursoVO consultarObjProcSeletivoCursoVO(Integer unidadeEnsino) throws Exception {
        Iterator<ProcSeletivoCursoVO> i = getProcSeletivoCursoVOs().iterator();
        while (i.hasNext()) {
            ProcSeletivoCursoVO objExistente = (ProcSeletivoCursoVO) i.next();
            if (objExistente.getUnidadeEnsinoCurso().getCodigo().equals(unidadeEnsino)) {
                return objExistente;
            }
        }
        return null;
        // consultarObjSubordinadoOC
    }

    /**
     * Retorna o objeto da classe <code>UnidadeEnsino</code> relacionado com (
     * <code>ProcSeletivoUnidadeEnsino</code>).
     */
    public UnidadeEnsinoVO getUnidadeEnsino() {
        if (unidadeEnsino == null) {
            unidadeEnsino = new UnidadeEnsinoVO();
        }
        return (unidadeEnsino);
    }

    /**
     * Define o objeto da classe <code>UnidadeEnsino</code> relacionado com (
     * <code>ProcSeletivoUnidadeEnsino</code>).
     */
    public void setUnidadeEnsino(UnidadeEnsinoVO obj) {
        this.unidadeEnsino = obj;
    }

    public List<ProcSeletivoCursoVO> getProcSeletivoCursoVOs() {
        if (procSeletivoCursoVOs == null) {
            procSeletivoCursoVOs = new ArrayList<ProcSeletivoCursoVO>(0);
        }
        return (procSeletivoCursoVOs);
    }

    /**
     * Define Atributo responsável por manter os objetos da classe
     * <code>ProcSeletivoCurso</code>.
     */
    public void setProcSeletivoCursoVOs(List<ProcSeletivoCursoVO> procSeletivoCursoVOs) {
        this.procSeletivoCursoVOs = procSeletivoCursoVOs;
    }

    public ProcSeletivoVO getProcSeletivo() {
    	if(procSeletivo == null) {
    		procSeletivo = new ProcSeletivoVO();
    	}
        return (procSeletivo);
    }

    public void setProcSeletivo(ProcSeletivoVO procSeletivo) {
        this.procSeletivo = procSeletivo;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
    
    public Boolean getContemProcSeletivoCurso(Integer procSeletivo, Integer curso, Integer unidadeEnsino, Integer turno) {
    	for (ProcSeletivoCursoVO procSeletivoCursoVO : getProcSeletivoCursoVOs()) {
			if (procSeletivoCursoVO.getProcSeletivoUnidadeEnsino().getProcSeletivo().getCodigo().equals(procSeletivo)
					&& procSeletivoCursoVO.getUnidadeEnsinoCurso().getCurso().getCodigo().equals(curso)
					&& procSeletivoCursoVO.getUnidadeEnsinoCurso().getTurno().getCodigo().equals(turno)
					&& procSeletivoCursoVO.getUnidadeEnsinoCurso().getUnidadeEnsino().equals(unidadeEnsino)) {
				return true;
			}
		}
    	return false;
    }
    
    public String getOrdenacao() {
    	return getUnidadeEnsino().getNome();
    }

	public List<ProcSeletivoUnidadeEnsinoEixoCursoVO> getProcSeletivoUnidadeEnsinoEixoCursoVOs() {
		if(procSeletivoUnidadeEnsinoEixoCursoVOs == null) {
			procSeletivoUnidadeEnsinoEixoCursoVOs =  new ArrayList<ProcSeletivoUnidadeEnsinoEixoCursoVO>(0);
		}
		return procSeletivoUnidadeEnsinoEixoCursoVOs;
	}

	public void setProcSeletivoUnidadeEnsinoEixoCursoVOs(
			List<ProcSeletivoUnidadeEnsinoEixoCursoVO> procSeletivoUnidadeEnsinoEixoCursoVOs) {
		this.procSeletivoUnidadeEnsinoEixoCursoVOs = procSeletivoUnidadeEnsinoEixoCursoVOs;
	}
    
    
}
