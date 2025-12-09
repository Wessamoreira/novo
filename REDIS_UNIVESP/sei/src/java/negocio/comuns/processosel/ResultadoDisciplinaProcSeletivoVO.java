package negocio.comuns.processosel;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.processosel.ResultadoProcessoSeletivo;

/**
 * Reponsável por manter os dados da entidade ResultadoDisciplinaProcSeletivo. Classe do tipo VO - Value Object composta pelos atributos da entidade
 * com visibilidade protegida e os métodos de acesso a estes atributos. Classe utilizada para apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 * @see ResultadoProcessoSeletivo
 */
@XmlRootElement(name = "resultadoDisciplinaProcSeletivoVO")
public class ResultadoDisciplinaProcSeletivoVO extends SuperVO {

	private Integer resultadoProcessoSeletivo;
	private Double nota;
	private Integer quantidadeAcertos;
	private String observacoes;
	/**
	 * Atributo responsável por manter o objeto relacionado da classe <code>DisciplinasProcSeletivo </code>.
	 */
	private DisciplinasProcSeletivoVO disciplinaProcSeletivo;
	private String variavelNota;
	private String ordemCriterioDesempate;
	private Double notaMinimaReprovadoImediato;
	public static final long serialVersionUID = 1L;
	private boolean editavel = false;

	/**
	 * Construtor padrão da classe <code>ResultadoDisciplinaProcSeletivo</code>. Cria uma nova instância desta entidade, inicializando automaticamente
	 * seus atributos (Classe VO).
	 */
	public ResultadoDisciplinaProcSeletivoVO() {
		super();
		inicializarDados();
	}

	/**
	 * Operação responsável por validar os dados de um objeto da classe <code>ResultadoDisciplinaProcSeletivoVO</code>. Todos os tipos de consistência
	 * de dados são e devem ser implementadas neste método. São validações típicas: verificação de campos obrigatórios, verificação de valores válidos
	 * para os atributos.
	 *
	 * @exception ConsistirExecption
	 *                Se uma inconsistência for encontrada aumaticamente é gerada uma exceção descrevendo o atributo e o erro ocorrido.
	 */
	public static void validarDados(ResultadoDisciplinaProcSeletivoVO obj) throws ConsistirException {
		if ((obj.getDisciplinaProcSeletivo() == null) || (obj.getDisciplinaProcSeletivo().getCodigo().intValue() == 0)) {
			throw new ConsistirException("O campo DISCIPLINA (Notas por Disciplina) deve ser informado.");
		}
		if (obj.getNota() == null) {
			throw new ConsistirException("O campo NOTA deve ser informado.");
		}
	}

	/**
	 * Operação reponsável por inicializar os atributos da classe.
	 */
	public void inicializarDados() {
		setNota(0.0);
		setObservacoes("");
	}

	/**
	 * Retorna o objeto da classe <code>DisciplinasProcSeletivo</code> relacionado com (<code>ResultadoDisciplinaProcSeletivo</code>).
	 */
	@XmlElement(name = "disciplinaProcSeletivo")
	public DisciplinasProcSeletivoVO getDisciplinaProcSeletivo() {
		if (disciplinaProcSeletivo == null) {
			disciplinaProcSeletivo = new DisciplinasProcSeletivoVO();
		}
		return (disciplinaProcSeletivo);
	}

	/**
	 * Define o objeto da classe <code>DisciplinasProcSeletivo</code> relacionado com (<code>ResultadoDisciplinaProcSeletivo</code>).
	 */
	public void setDisciplinaProcSeletivo(DisciplinasProcSeletivoVO obj) {
		this.disciplinaProcSeletivo = obj;
	}

	@XmlElement(name = "observacoes")
	public String getObservacoes() {
		return (observacoes);
	}

	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}

	@XmlElement(name = "nota")
	public Double getNota() {
		return (nota);
	}

	public void setNota(Double nota) {
		this.nota = nota;
	}

	public Integer getResultadoProcessoSeletivo() {
		return (resultadoProcessoSeletivo);
	}

	public void setResultadoProcessoSeletivo(Integer resultadoProcessoSeletivo) {
		this.resultadoProcessoSeletivo = resultadoProcessoSeletivo;
	}

	@XmlElement(name = "quantidadeAcertos")
	public Integer getQuantidadeAcertos() {
		if (quantidadeAcertos == null) {
			quantidadeAcertos = 0;
		}
		return quantidadeAcertos;
	}

	public void setQuantidadeAcertos(Integer quantidadeAcertos) {
		this.quantidadeAcertos = quantidadeAcertos;
	}

	@XmlElement(name = "variavelNota")
	public String getVariavelNota() {
		if (variavelNota == null) {
			variavelNota = "";
		}
		return variavelNota;
	}

	public void setVariavelNota(String variavelNota) {
		this.variavelNota = variavelNota;
	}

	public String getOrdemCriterioDesempate() {
		if (ordemCriterioDesempate == null) {
			ordemCriterioDesempate = "";
		}
		return ordemCriterioDesempate;
	}

	public void setOrdemCriterioDesempate(String ordemCriterioDesempate) {
		this.ordemCriterioDesempate = ordemCriterioDesempate;
	}

	@XmlElement(name = "notaMinimaReprovadoImediato")
	public Double getNotaMinimaReprovadoImediato() {
		return notaMinimaReprovadoImediato;
	}

	public void setNotaMinimaReprovadoImediato(Double notaMinimaReprovadoImediato) {
		this.notaMinimaReprovadoImediato = notaMinimaReprovadoImediato;
	}

	public boolean isEditavel() {
		return editavel;
	}

	public void setEditavel(boolean editavel) {
		this.editavel = editavel;
	}
}
