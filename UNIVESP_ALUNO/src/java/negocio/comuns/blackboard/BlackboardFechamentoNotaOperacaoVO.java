package negocio.comuns.blackboard;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.Uteis;

@SuppressWarnings("serial")
public class BlackboardFechamentoNotaOperacaoVO extends SuperVO {

	private Integer codigo;
	private Boolean executado;
	private String erro;
	private ConfiguracaoAcademicoVO configuracaoAcademica;
	private List<String> listaHistorico;
	private Boolean calcularMedia;
	private DisciplinaVO disciplina;
	private Boolean reprovado;
	private Boolean aprovado;
	private Boolean cursando;
	private String notas;

	public void validarDados() throws Exception {
		Uteis.checkState(!Uteis.isAtributoPreenchido(getConfiguracaoAcademica()), "A configuração acadêmica deve ser informada para criar uma operação de fechamento de nota");
		Uteis.checkState(!Uteis.isAtributoPreenchido(getDisciplina()), "A disciplina deve ser informada para criar uma operação de fechamento de nota");
		Uteis.checkState(!Uteis.isAtributoPreenchido(getListaHistorico()), "A lista de históricos deve ser informada para criar uma operação de fechamento de notas");
		Uteis.checkState(!Uteis.isAtributoPreenchido(getNotas()), "As notas da configuração devem ser informadas para criar uma operação de fechamento de notas");
	}
	
	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}
	
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	
	public Boolean getExecutado() {
		if (executado == null) {
			executado = Boolean.FALSE;
		}
		return executado;
	}
	
	public void setExecutado(Boolean executado) {
		this.executado = executado;
	}
	
	public String getErro() {
		if (erro == null) {
			erro = Constantes.EMPTY;
		}
		return erro;
	}
	
	public void setErro(String erro) {
		this.erro = erro;
	}

	public ConfiguracaoAcademicoVO getConfiguracaoAcademica() {
		if (configuracaoAcademica == null) {
			configuracaoAcademica = new ConfiguracaoAcademicoVO();
		}
		return configuracaoAcademica;
	}

	public void setConfiguracaoAcademica(ConfiguracaoAcademicoVO configuracaoAcademica) {
		this.configuracaoAcademica = configuracaoAcademica;
	}

	public List<String> getListaHistorico() {
		if (listaHistorico == null) {
			listaHistorico = new ArrayList<>(0);
		}
		return listaHistorico;
	}

	public void setListaHistorico(List<String> listaHistorico) {
		this.listaHistorico = listaHistorico;
	}
	
	public Boolean getCalcularMedia() {
		if (calcularMedia == null) {
			calcularMedia = Boolean.FALSE;
		}
		return calcularMedia;
	}

	public void setCalcularMedia(Boolean calcularMedia) {
		this.calcularMedia = calcularMedia;
	}

	public DisciplinaVO getDisciplina() {
		if (disciplina == null) {
			disciplina = new DisciplinaVO();
		}
		return disciplina;
	}

	public void setDisciplina(DisciplinaVO disciplina) {
		this.disciplina = disciplina;
	}

	public Boolean getReprovado() {
		if (reprovado == null) {
			reprovado = Boolean.FALSE;
		}
		return reprovado;
	}

	public void setReprovado(Boolean reprovado) {
		this.reprovado = reprovado;
	}

	public Boolean getAprovado() {
		if (aprovado == null) {
			aprovado = Boolean.FALSE;
		}
		return aprovado;
	}

	public void setAprovado(Boolean aprovado) {
		this.aprovado = aprovado;
	}

	public Boolean getCursando() {
		if (cursando == null) {
			cursando = Boolean.FALSE;
		}
		return cursando;
	}

	public void setCursando(Boolean cursando) {
		this.cursando = cursando;
	}

	public String getNotas() {
		if (notas == null) {
			notas = Constantes.EMPTY;
		}
		return notas;
	}

	public void setNotas(String notas) {
		this.notas = notas;
	}
}