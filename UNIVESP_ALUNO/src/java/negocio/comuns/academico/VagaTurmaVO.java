package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.UteisJSF;

/**
 * Reponsável por manter os dados da entidade Turma. Classe do tipo VO - Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class VagaTurmaVO extends SuperVO implements Cloneable {

	private Integer codigo;
	private TurmaVO turmaVO;
	private String ano;
	private String semestre;
	private Date dataCadastro;
	private UsuarioVO usuarioResponsavel;
	private List<VagaTurmaDisciplinaVO> vagaTurmaDisciplinaVOs;
	public static final long serialVersionUID = 1L;

	/**
	 * Construtor padrão da classe <code>Turma</code>. Cria uma nova instância desta entidade, inicializando automaticamente seus atributos (Classe
	 * VO).
	 */
	@SuppressWarnings("OverridableMethodCallInConstructor")
	public VagaTurmaVO() {
		super();
		inicializarDados();
	}

	/**
	 * Operação responsável por validar os dados de um objeto da classe <code>TurmaVO</code>. Todos os tipos de consistência de dados são e devem ser
	 * implementadas neste método. São validações típicas: verificação de campos obrigatórios, verificação de valores válidos para os atributos.
	 * 
	 * @exception ConsistirExecption
	 *                Se uma inconsistência for encontrada aumaticamente é gerada uma exceção descrevendo o atributo e o erro ocorrido.
	 */
	public static void validarDados(VagaTurmaVO obj) throws ConsistirException {
		if (obj.getTurmaVO().getCodigo().intValue() == 0) {
			throw new ConsistirException("O campo TURMA deve ser informado.");
		}
		if (obj.getTurmaVO().getPeriodicidade().equals("AN") || obj.getTurmaVO().getPeriodicidade().equals("SE")) {
			if (obj.getAno().equals("")) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_AlunosMatriculadosGeralRel_ano"));
			}
			if (obj.getAno().length() < 4) {
				throw new ConsistirException("O campo ANO deve possuir 4 dígitos");
			}
		}
		if (obj.getTurmaVO().getPeriodicidade().equals("SE")) {
			if (obj.getSemestre().equals("")) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_AlunosMatriculadosGeralRel_semestre"));
			}
		}
	}

	/**
	 * Operação reponsável por inicializar os atributos da classe.
	 */
	public void inicializarDados() {
		setCodigo(0);
	}

	/**
	 * Operação responsável por adicionar um novo objeto da classe <code>MatriculaPeriodoVO</code> ao List <code>matriculaPeriodoVOs</code>. Utiliza o
	 * atributo padrão de consulta da classe <code>MatriculaPeriodo</code> - getPeriodoLetivoMatricula().getCodigo() - como identificador (key) do
	 * objeto no List.
	 * 
	 * @param obj
	 *            Objeto da classe <code>MatriculaPeriodoVO</code> que será adiocionado ao Hashtable correspondente.
	 */
	public void adicionarObjTurmaDisciplinaVOs(VagaTurmaDisciplinaVO obj) throws Exception {
		VagaTurmaDisciplinaVO.validarDados(obj);
		int index = 0;
		Iterator i = getVagaTurmaDisciplinaVOs().iterator();
		while (i.hasNext()) {
			VagaTurmaDisciplinaVO objExistente = (VagaTurmaDisciplinaVO) i.next();
			if (objExistente.getDisciplina().getCodigo().equals(obj.getDisciplina().getCodigo())) {
				getVagaTurmaDisciplinaVOs().set(index, obj);
				return;
			}
			index++;
		}
		getVagaTurmaDisciplinaVOs().add(obj);
	}

	/**
	 * Operação responsável por excluir um objeto da classe <code>MatriculaPeriodoVO</code> no List <code>matriculaPeriodoVOs</code>. Utiliza o
	 * atributo padrão de consulta da classe <code>MatriculaPeriodo</code> - getPeriodoLetivoMatricula().getCodigo() - como identificador (key) do
	 * objeto no List.
	 * 
	 * @param periodoLetivoMatricula
	 *            Parâmetro para localizar e remover o objeto do List.
	 */
	@SuppressWarnings("element-type-mismatch")
	public void excluirObjVagaTurmaDisciplinaVOs(Integer disciplina) throws Exception {
		int index = 0;
		Iterator i = getVagaTurmaDisciplinaVOs().iterator();
		while (i.hasNext()) {
			VagaTurmaDisciplinaVO objExistente = (VagaTurmaDisciplinaVO) i.next();
			if (objExistente.getDisciplina().getCodigo().equals(disciplina)) {
				getVagaTurmaDisciplinaVOs().remove(index);
				return;
			}
			index++;
		}
	}

	/**
	 * Operação responsável por consultar um objeto da classe <code>MatriculaPeriodoVO</code> no List <code>matriculaPeriodoVOs</code>. Utiliza o
	 * atributo padrão de consulta da classe <code>MatriculaPeriodo</code> - getPeriodoLetivoMatricula().getCodigo() - como identificador (key) do
	 * objeto no List.
	 * 
	 * @param periodoLetivoMatricula
	 *            Parâmetro para localizar o objeto do List.
	 */
	public VagaTurmaDisciplinaVO consultarObjVagaTurmaDisciplinaVO(Integer disciplina) throws Exception {
		Iterator i = getVagaTurmaDisciplinaVOs().iterator();
		while (i.hasNext()) {
			VagaTurmaDisciplinaVO objExistente = (VagaTurmaDisciplinaVO) i.next();
			if (objExistente.getDisciplina().getCodigo().equals(disciplina)) {
				return objExistente;
			}
		}
		return null;
	}

	public Integer getNrVagasDisponiveis(Integer disciplina) {
		Iterator i = this.getVagaTurmaDisciplinaVOs().iterator();
		while (i.hasNext()) {
			VagaTurmaDisciplinaVO vagaTurmaDisciplinaVO = (VagaTurmaDisciplinaVO) i.next();
			if (vagaTurmaDisciplinaVO.getDisciplina().getCodigo().equals(disciplina)) {
				return vagaTurmaDisciplinaVO.getNrVagasMatricula();
			}
		}
		return 0;
	}

	public List<VagaTurmaDisciplinaVO> getVagaTurmaDisciplinaVOs() {
		if (vagaTurmaDisciplinaVOs == null) {
			vagaTurmaDisciplinaVOs = new ArrayList();
		}
		return vagaTurmaDisciplinaVOs;
	}

	public void setVagaTurmaDisciplinaVOs(List vagaTurmaDisciplinaVOs) {
		this.vagaTurmaDisciplinaVOs = vagaTurmaDisciplinaVOs;
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

	public TurmaVO getTurmaVO() {
		if (turmaVO == null) {
			turmaVO = new TurmaVO();
		}
		return turmaVO;
	}

	public void setTurmaVO(TurmaVO turmaVO) {
		this.turmaVO = turmaVO;
	}

	public String getAno() {
		if (ano == null) {
			ano = "";
		}
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getSemestre() {
		if (semestre == null) {
			semestre = "";
		}
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	public Date getDataCadastro() {
		if (dataCadastro == null) {
			dataCadastro = new Date();
		}
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public UsuarioVO getUsuarioResponsavel() {
		if (usuarioResponsavel == null) {
			usuarioResponsavel = new UsuarioVO();
		}
		return usuarioResponsavel;
	}

	public void setUsuarioResponsavel(UsuarioVO usuarioResponsavel) {
		this.usuarioResponsavel = usuarioResponsavel;
	}

}
