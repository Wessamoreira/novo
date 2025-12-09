package negocio.comuns.processosel;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.enumeradores.SituacaoGrupoDisciplinaProcSeletivoEnum;

/**
 * Reponsável por manter os dados da entidade ProcSeletivo. Classe do tipo VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */

@XmlRootElement(name = "grupoDisciplinaProcSeletivoVO")
public class GrupoDisciplinaProcSeletivoVO extends SuperVO {

	private Integer codigo;
	private String descricao;
	private SituacaoGrupoDisciplinaProcSeletivoEnum situacao;
	private String formulaCalculoAprovacao;

	/**
	 * Atributo responsável por manter os objetos da classe <code>ProcSeletivoDisciplinasProcSeletivo</code>.
	 */
	private List<DisciplinasGrupoDisciplinaProcSeletivoVO> disciplinasGrupoDisciplinaProcSeletivoVOs;
	private UsuarioVO responsavel;
	public static final long serialVersionUID = 1L;

	/**
	 * Construtor padrão da classe <code>ProcSeletivo</code>. Cria uma nova instância desta entidade, inicializando automaticamente seus atributos
	 * (Classe VO).
	 */
	public GrupoDisciplinaProcSeletivoVO() {
		super();
	}

	/**
	 * Retorna o objeto da classe <code>Pessoa</code> relacionado com ( <code>ProcSeletivo</code>).
	 */
	@XmlElement(name = "responsavel")
	public UsuarioVO getResponsavel() {
		if (responsavel == null) {
			responsavel = new UsuarioVO();
		}
		return (responsavel);
	}

	/**
	 * Define o objeto da classe <code>Pessoa</code> relacionado com ( <code>ProcSeletivo</code>).
	 */
	public void setResponsavel(UsuarioVO obj) {
		this.responsavel = obj;
	}

	/**
	 * Retorna Atributo responsável por manter os objetos da classe <code>ProcSeletivoDisciplinasProcSeletivo</code>.
	 */
	@XmlElement(name = "disciplinasGrupoDisciplinaProcSeletivoVOs")
	public List<DisciplinasGrupoDisciplinaProcSeletivoVO> getDisciplinasGrupoDisciplinaProcSeletivoVOs() {
		if (disciplinasGrupoDisciplinaProcSeletivoVOs == null) {
			disciplinasGrupoDisciplinaProcSeletivoVOs = new ArrayList<DisciplinasGrupoDisciplinaProcSeletivoVO>(0);
		}
		return (disciplinasGrupoDisciplinaProcSeletivoVOs);
	}

	/**
	 * Define Atributo responsável por manter os objetos da classe <code>ProcSeletivoDisciplinasProcSeletivo</code>.
	 */
	public void setDisciplinasGrupoDisciplinaProcSeletivoVOs(List<DisciplinasGrupoDisciplinaProcSeletivoVO> disciplinasGrupoDisciplinaProcSeletivoVOs) {
		this.disciplinasGrupoDisciplinaProcSeletivoVOs = disciplinasGrupoDisciplinaProcSeletivoVOs;
	}

	@XmlElement(name = "descricao")
	public String getDescricao() {
		if (descricao == null) {
			descricao = "";
		}
		return (descricao);
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@XmlElement(name = "codigo")
	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return (codigo);
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public void ativar() {
		setSituacao(SituacaoGrupoDisciplinaProcSeletivoEnum.ATIVA);
	}

	public void inativar() {
		setSituacao(SituacaoGrupoDisciplinaProcSeletivoEnum.INATIVA);
	}

	@XmlElement(name = "situacao")
	public SituacaoGrupoDisciplinaProcSeletivoEnum getSituacao() {
		if (situacao == null) {
			situacao = SituacaoGrupoDisciplinaProcSeletivoEnum.EM_ELABORACAO;
		}
		return situacao;
	}

	public String getSituacao_Apresentar() {
		if (getSituacao().equals(SituacaoGrupoDisciplinaProcSeletivoEnum.EM_ELABORACAO)) {
			return SituacaoGrupoDisciplinaProcSeletivoEnum.EM_ELABORACAO.getValorApresentar();
		}
		if (getSituacao().equals(SituacaoGrupoDisciplinaProcSeletivoEnum.ATIVA)) {
			return SituacaoGrupoDisciplinaProcSeletivoEnum.ATIVA.getValorApresentar();
		}
		if (getSituacao().equals(SituacaoGrupoDisciplinaProcSeletivoEnum.INATIVA)) {
			return SituacaoGrupoDisciplinaProcSeletivoEnum.INATIVA.getValorApresentar();
		}
		return SituacaoGrupoDisciplinaProcSeletivoEnum.EM_ELABORACAO.getValorApresentar();
	}

	public void setSituacao(SituacaoGrupoDisciplinaProcSeletivoEnum situacao) {
		this.situacao = situacao;
	}

	@XmlElement(name = "formulaCalculoAprovacao")
	public String getFormulaCalculoAprovacao() {
		if (formulaCalculoAprovacao == null) {
			formulaCalculoAprovacao = "";
		}
		return formulaCalculoAprovacao;
	}

	public void setFormulaCalculoAprovacao(String formulaCalculoAprovacao) {
		this.formulaCalculoAprovacao = formulaCalculoAprovacao;
	}

}
