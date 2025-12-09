package negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.Uteis;

public class CategoriaDespesaVO extends SuperVO {

	public static final long serialVersionUID = 1L;
	private Integer codigo;
	private String nivelCategoriaDespesa;
	private String informarTurma;
	private String descricao;
	private Integer categoriaDespesaPrincipal;
	private String identificadorCategoriaDespesa;
	private Boolean isUmaSubCategoria;
	private Boolean exigeNivelAdministrativoPlanoOrcamentario;
	private Boolean exigeNivelAcademicoPlanoOrcamentario;
	private Boolean exigeCentroCustoRequisitante;
	private Boolean apresentarPlanoOrcamentario;
	private Boolean tributo;
	private Boolean cancelamento;
	private List<CategoriaDespesaRateioVO> listaCategoriaDespesaRateioAcademico;
	private List<CategoriaDespesaRateioVO> listaCategoriaDespesaRateioAdministrativo;
	private Boolean informarManualmenteIdentificadorCategoriaDespesa;

	/*
	 * Transiente
	 */
	private Boolean selecionar;


	public enum enumCampoConsultaCategoriaDespesa {
		DESCRICAO, IDENTIFICADOR_CENTRO_DESPESA;
	}

	public CategoriaDespesaVO() {
		super();
		inicializarDados();
	}

	public CategoriaDespesaVO(boolean inicializarRecursivamente) {
		super();
		if (inicializarRecursivamente) {
			inicializarDadosComRecursividade();
		} else {
			inicializarDados();
		}
	}

	public void inicializarDadosComRecursividade() {
		setCodigo(0);
		setCategoriaDespesaPrincipal(null);
		setIsUmaSubCategoria(false);
		setDescricao("");
		setIdentificadorCategoriaDespesa("");
		setInformarTurma("");
	}

	/**
	 * Operação reponsável por inicializar os atributos da classe.
	 */
	public void inicializarDados() {
		setCodigo(0);
		setCategoriaDespesaPrincipal(0);
		setIsUmaSubCategoria(false);
		setDescricao("");
		setIdentificadorCategoriaDespesa("");
		setInformarTurma("");

	}

	public boolean getApresentarTurma() {
		if (!Uteis.isAtributoPreenchido(getInformarTurma()) || isInformaNivelAcademicoNaoControlar()) {
			return false;
		}
		return true;
	}

	public boolean getApresentarDepartamento() {
		return isNivelCategoriaDespesaDepartamento();
	}

	public boolean getApresentarFuncionario() {
		return isNivelCategoriaDespesaFuncionario();

	}

	public String getIdentificadorCategoriaDespesa() {
		return (identificadorCategoriaDespesa);
	}

	public void setIdentificadorCategoriaDespesa(String identificadorCategoriaDespesa) {
		this.identificadorCategoriaDespesa = identificadorCategoriaDespesa;
	}

	public Integer getCategoriaDespesaPrincipal() {
		return (categoriaDespesaPrincipal);
	}

	public void setCategoriaDespesaPrincipal(Integer categoriaDespesaPrincipal) {
		this.categoriaDespesaPrincipal = categoriaDespesaPrincipal;
	}

	public String getDescricao() {
		return (descricao);
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Integer getCodigo() {
		if (this.codigo == null) {
			return 0;
		}
		return (codigo);
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public boolean verificarCategoriaDespesaPrimeiroNivel() {
		return !Uteis.isAtributoPreenchido(getCategoriaDespesaPrincipal());
	}

	public String getApresentarCategoriaDespesa() {
		if (getDescricao().trim().equals("")) {
			return "";
		}
		return (getDescricao() + " - " + getIdentificadorCategoriaDespesa());
	}

	public String getNivelCategoriaDespesa_Apresentar() {
		if (isNivelCategoriaDespesaUnidadeEnsino()) {
			return "Unidade Ensino";
		}
		if (isNivelCategoriaDespesaDepartamento()) {
			return "Departamento";
		}
		if (isNivelCategoriaDespesaFuncionario()) {
			return "Funcionário";
		}
		if (isNivelCategoriaDespesaNaoControlar()) {
			return "Não Controlar";
		}
		return "";
	}

	public String getNivelCategoriaDespesa() {
		if (nivelCategoriaDespesa == null) {
			nivelCategoriaDespesa = "";
		}
		return nivelCategoriaDespesa;
	}

	public void setNivelCategoriaDespesa(String nivelCategoriaDespesa) {
		this.nivelCategoriaDespesa = nivelCategoriaDespesa;
	}

	public boolean isNivelCategoriaDespesaUnidadeEnsino() {
		return Uteis.isAtributoPreenchido(getNivelCategoriaDespesa()) && getNivelCategoriaDespesa().equals("UE");
	}

	public boolean isNivelCategoriaDespesaDepartamento() {
		return Uteis.isAtributoPreenchido(getNivelCategoriaDespesa()) && getNivelCategoriaDespesa().equals("DE");
	}

	public boolean isNivelCategoriaDespesaFuncionario() {
		return Uteis.isAtributoPreenchido(getNivelCategoriaDespesa()) && getNivelCategoriaDespesa().equals("FU");
	}
	
	public boolean isNivelCategoriaDespesaNaoControlar() {
		return Uteis.isAtributoPreenchido(getNivelCategoriaDespesa()) && getNivelCategoriaDespesa().equals("NC");
	}

	public boolean isInformaNivelAcademicoTurma() {
		return Uteis.isAtributoPreenchido(getInformarTurma()) && getInformarTurma().equals("TU");
	}

	public boolean isInformaNivelAcademicoCurso() {
		return Uteis.isAtributoPreenchido(getInformarTurma()) && getInformarTurma().equals("CU");
	}

	public boolean isInformaNivelAcademicoCursoTurno() {
		return Uteis.isAtributoPreenchido(getInformarTurma()) && getInformarTurma().equals("CT");
	}

	public boolean isInformaNivelAcademicoNaoControlar() {
		return Uteis.isAtributoPreenchido(getInformarTurma()) && getInformarTurma().equals("NC");
	}

	public String getInformarTurma() {
		if (informarTurma == null) {
			informarTurma = "";
		}
		return informarTurma;
	}

	public void setInformarTurma(String informarTurma) {
		this.informarTurma = informarTurma;
	}

	/**
	 * @return the isUmaSubCategoria
	 */
	public Boolean getIsUmaSubCategoria() {
		if (isUmaSubCategoria == null) {
			isUmaSubCategoria = false;
		}
		return isUmaSubCategoria;
	}

	public void setIsUmaSubCategoria(Boolean isUmaSubCategoria) {
		this.isUmaSubCategoria = isUmaSubCategoria;
	}

	public Boolean getExigeNivelAdministrativoPlanoOrcamentario() {
		if (exigeNivelAdministrativoPlanoOrcamentario == null) {
			exigeNivelAdministrativoPlanoOrcamentario = Boolean.FALSE;
		}
		return exigeNivelAdministrativoPlanoOrcamentario;
	}

	public void setExigeNivelAdministrativoPlanoOrcamentario(Boolean exigeNivelAdministrativoPlanoOrcamentario) {
		this.exigeNivelAdministrativoPlanoOrcamentario = exigeNivelAdministrativoPlanoOrcamentario;
	}

	public Boolean getExigeNivelAcademicoPlanoOrcamentario() {
		if (exigeNivelAcademicoPlanoOrcamentario == null) {
			exigeNivelAcademicoPlanoOrcamentario = Boolean.FALSE;
		}
		return exigeNivelAcademicoPlanoOrcamentario;
	}

	public void setExigeNivelAcademicoPlanoOrcamentario(Boolean exigeNivelAcademicoPlanoOrcamentario) {
		this.exigeNivelAcademicoPlanoOrcamentario = exigeNivelAcademicoPlanoOrcamentario;
	}

	public Boolean getExigeCentroCustoRequisitante() {
		if (exigeCentroCustoRequisitante == null) {
			exigeCentroCustoRequisitante = Boolean.FALSE;
		}
		return exigeCentroCustoRequisitante;
	}

	public void setExigeCentroCustoRequisitante(Boolean exigeCentroCustoRequisitante) {
		this.exigeCentroCustoRequisitante = exigeCentroCustoRequisitante;
	}

	public Boolean getApresentarPlanoOrcamentario() {
		if (apresentarPlanoOrcamentario == null) {
			apresentarPlanoOrcamentario = Boolean.FALSE;
		}
		return apresentarPlanoOrcamentario;
	}

	public void setApresentarPlanoOrcamentario(Boolean apresentarPlanoOrcamentario) {
		this.apresentarPlanoOrcamentario = apresentarPlanoOrcamentario;
	}

	public Boolean getSelecionar() {
		if (selecionar == null) {
			selecionar = false;
		}
		return selecionar;
	}

	public void setSelecionar(Boolean selecionar) {
		this.selecionar = selecionar;
	}

	public List<CategoriaDespesaRateioVO> getListaCategoriaDespesaRateioAcademico() {
		if (listaCategoriaDespesaRateioAcademico == null) {
			listaCategoriaDespesaRateioAcademico = new ArrayList<>();
		}
		return listaCategoriaDespesaRateioAcademico;
	}

	public void setListaCategoriaDespesaRateioAcademico(List<CategoriaDespesaRateioVO> listaCategoriaDespesaRateioAcademica) {
		this.listaCategoriaDespesaRateioAcademico = listaCategoriaDespesaRateioAcademica;
	}

	public List<CategoriaDespesaRateioVO> getListaCategoriaDespesaRateioAdministrativo() {
		if (listaCategoriaDespesaRateioAdministrativo == null) {
			listaCategoriaDespesaRateioAdministrativo = new ArrayList<>();
		}
		return listaCategoriaDespesaRateioAdministrativo;
	}

	public void setListaCategoriaDespesaRateioAdministrativo(List<CategoriaDespesaRateioVO> listaCategoriaDespesaRateioAdministrativa) {
		this.listaCategoriaDespesaRateioAdministrativo = listaCategoriaDespesaRateioAdministrativa;
	}

	public Double getTotalPorcentagemRateioContabilAcademico() {
		return getListaCategoriaDespesaRateioAcademico().stream().mapToDouble(CategoriaDespesaRateioVO::getPorcentagem).sum();
	}

	public Double getTotalPorcentagemRateioContabilAdministrativo() {
		return getListaCategoriaDespesaRateioAdministrativo().stream().mapToDouble(CategoriaDespesaRateioVO::getPorcentagem).sum();
	}

	public Boolean getTributo() {
		if (tributo == null) {
			tributo = Boolean.FALSE;
		}
		return tributo;
	}

	public void setTributo(Boolean tributo) {
		this.tributo = tributo;
	}

	public Boolean getCancelamento() {
		if (cancelamento == null) {
			cancelamento = Boolean.FALSE;
		}
		return cancelamento;
	}

	public void setCancelamento(Boolean cancelamento) {
		this.cancelamento = cancelamento;
	}

	public Boolean getInformarManualmenteIdentificadorCategoriaDespesa() {
		if (informarManualmenteIdentificadorCategoriaDespesa == null) {
			informarManualmenteIdentificadorCategoriaDespesa = false;
		}
		return informarManualmenteIdentificadorCategoriaDespesa;
	}

	public void setInformarManualmenteIdentificadorCategoriaDespesa(Boolean informarManualmenteIdentificadorCategoriaDespesa) {
		this.informarManualmenteIdentificadorCategoriaDespesa = informarManualmenteIdentificadorCategoriaDespesa;
	}
	
	public boolean getDesabilitarInformarManualmenteIdentificadorCategoriaDespesa() {
		return Uteis.isAtributoPreenchido(getCodigo()) && getInformarManualmenteIdentificadorCategoriaDespesa();
	}
}
