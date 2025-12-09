package negocio.comuns.processosel;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import negocio.comuns.arquitetura.SuperVO;
import negocio.facade.jdbc.processosel.ProcSeletivo;

/**
 * Reponsável por manter os dados da entidade ProcSeletivoDisciplinasProcSeletivo. Classe do tipo VO - Value Object composta pelos atributos da
 * entidade com visibilidade protegida e os métodos de acesso a estes atributos. Classe utilizada para apresentar e manter em memória os dados desta
 * entidade.
 * 
 * @see SuperVO
 * @see ProcSeletivo
 */
@XmlRootElement(name = "disciplinasGrupoDisciplinaProcSeletivoVO")
public class DisciplinasGrupoDisciplinaProcSeletivoVO extends SuperVO {

	private Integer grupoDisciplinaProcSeletivo;
	/**
	 * Atributo responsável por manter o objeto relacionado da classe <code>DisciplinasProcSeletivo </code>.
	 */
	private String variavelNota;
	private String ordemCriterioDesempate;
	private Double notaMinimaReprovadoImediato;
	private String formaCalculoAprovacao;
	public static final long serialVersionUID = 1L;

	/**
	 * Construtor padrão da classe <code>ProcSeletivoDisciplinasProcSeletivo</code>. Cria uma nova instância desta entidade, inicializando
	 * automaticamente seus atributos (Classe VO).
	 */
	public DisciplinasGrupoDisciplinaProcSeletivoVO() {
		super();
		inicializarDados();
	}

	/**
	 * Operação reponsável por inicializar os atributos da classe.
	 */
	public void inicializarDados() {
	}

	/**
	 * Retorna o objeto da classe <code>DisciplinasProcSeletivo</code> relacionado com (<code>ProcSeletivoDisciplinasProcSeletivo</code>).
	 */
//	@XmlElement(name = "disciplinasProcSeletivo")
//	public DisciplinasProcSeletivoVO getDisciplinasProcSeletivo() {
//		if (disciplinasProcSeletivo == null) {
//			disciplinasProcSeletivo = new DisciplinasProcSeletivoVO();
//		}
//		return (disciplinasProcSeletivo);
//	}
//
//	/**
//	 * Define o objeto da classe <code>DisciplinasProcSeletivo</code> relacionado com (<code>ProcSeletivoDisciplinasProcSeletivo</code>).
//	 */
//	public void setDisciplinasProcSeletivo(DisciplinasProcSeletivoVO obj) {
//		this.disciplinasProcSeletivo = obj;
//	}

	public Integer getGrupoDisciplinaProcSeletivo() {
		if (grupoDisciplinaProcSeletivo == null) {
			grupoDisciplinaProcSeletivo = 0;
		}
		return (grupoDisciplinaProcSeletivo);
	}

	public void setGrupoDisciplinaProcSeletivo(Integer grupoDisciplinaProcSeletivo) {
		this.grupoDisciplinaProcSeletivo = grupoDisciplinaProcSeletivo;
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

	@XmlElement(name = "ordemCriterioDesempate")
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

	@XmlElement(name = "formaCalculoAprovacao")
	public String getFormaCalculoAprovacao() {
		if (formaCalculoAprovacao == null) {
			formaCalculoAprovacao = "";
		}
		return formaCalculoAprovacao;
	}

	public void setFormaCalculoAprovacao(String formaCalculoAprovacao) {
		this.formaCalculoAprovacao = formaCalculoAprovacao;
	}
}
