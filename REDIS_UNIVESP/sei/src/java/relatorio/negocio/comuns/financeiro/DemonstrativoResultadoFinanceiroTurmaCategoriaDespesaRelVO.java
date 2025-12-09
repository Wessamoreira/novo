package relatorio.negocio.comuns.financeiro;

public class DemonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVO {

	private Integer categoriaDespesa;
	private String identificadorCategoriaDespesa;
	private Integer categoriaDespesaPrincipal;
	private String nivelCategoriaDespesa;
	private Double valor;
	// Campos para controle de linhas da grade
	private Boolean matricula;
	private Boolean mensalidade;
	private Boolean deducoes;
	private Boolean tributos;
	private Boolean descontos;
	private Boolean cancelamentos;
	private Boolean receitaLiquida;
	private Boolean custosDespesaVariavel;
	private Boolean margemContribuicao;
	private Boolean despesasFixas;
	private Boolean resultado;
	private Boolean categoriasDespesas;

	public Integer getCategoriaDespesa() {
		return categoriaDespesa;
	}

	public void setCategoriaDespesa(Integer categoriaDespesa) {
		this.categoriaDespesa = categoriaDespesa;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public String getNivelCategoriaDespesa() {
		if (nivelCategoriaDespesa == null) {
			nivelCategoriaDespesa = "TU";
		}
		return nivelCategoriaDespesa;
	}

	public void setNivelCategoriaDespesa(String nivelCategoriaDespesa) {
		this.nivelCategoriaDespesa = nivelCategoriaDespesa;
	}

	public Integer getCategoriaDespesaPrincipal() {
		if (categoriaDespesaPrincipal == null) {
			categoriaDespesaPrincipal = 0;
		}
		return categoriaDespesaPrincipal;
	}

	public void setCategoriaDespesaPrincipal(Integer categoriaDespesaPrincipal) {
		this.categoriaDespesaPrincipal = categoriaDespesaPrincipal;
	}

	public String getIdentificadorCategoriaDespesa() {
		return identificadorCategoriaDespesa;
	}

	public void setIdentificadorCategoriaDespesa(String identificadorCategoriaDespesa) {
		this.identificadorCategoriaDespesa = identificadorCategoriaDespesa;
	}

	public Boolean getMatricula() {
		if (matricula == null) {
			matricula = Boolean.FALSE;
		}
		return matricula;
	}

	public void setMatricula(Boolean matricula) {
		this.matricula = matricula;
	}

	public Boolean getMensalidade() {
		if (mensalidade == null) {
			mensalidade = Boolean.FALSE;
		}
		return mensalidade;
	}

	public void setMensalidade(Boolean mensalidade) {
		this.mensalidade = mensalidade;
	}

	public Boolean getDeducoes() {
		if (deducoes == null) {
			deducoes = Boolean.FALSE;
		}
		return deducoes;
	}

	public void setDeducoes(Boolean deducoes) {
		this.deducoes = deducoes;
	}

	public Boolean getTributos() {
		if (tributos == null) {
			tributos = Boolean.FALSE;
		}
		return tributos;
	}

	public void setTributos(Boolean tributos) {
		this.tributos = tributos;
	}

	public Boolean getDescontos() {
		if (descontos == null) {
			descontos = Boolean.FALSE;
		}
		return descontos;
	}

	public void setDescontos(Boolean descontos) {
		this.descontos = descontos;
	}

	public Boolean getCancelamentos() {
		if (cancelamentos == null) {
			cancelamentos = Boolean.FALSE;
		}
		return cancelamentos;
	}

	public void setCancelamentos(Boolean cancelamentos) {
		this.cancelamentos = cancelamentos;
	}

	public Boolean getReceitaLiquida() {
		if (receitaLiquida == null) {
			receitaLiquida = Boolean.FALSE;
		}
		return receitaLiquida;
	}

	public void setReceitaLiquida(Boolean receitaLiquida) {
		this.receitaLiquida = receitaLiquida;
	}

	public Boolean getCustosDespesaVariavel() {
		if (custosDespesaVariavel == null) {
			custosDespesaVariavel = Boolean.FALSE;
		}
		return custosDespesaVariavel;
	}

	public void setCustosDespesaVariavel(Boolean custosDespesaVariavel) {
		this.custosDespesaVariavel = custosDespesaVariavel;
	}

	public Boolean getMargemContribuicao() {
		if (margemContribuicao == null) {
			margemContribuicao = Boolean.FALSE;
		}
		return margemContribuicao;
	}

	public void setMargemContribuicao(Boolean margemContribuicao) {
		this.margemContribuicao = margemContribuicao;
	}

	public Boolean getDespesasFixas() {
		if (despesasFixas == null) {
			despesasFixas = Boolean.FALSE;
		}
		return despesasFixas;
	}

	public void setDespesasFixas(Boolean despesasFixas) {
		this.despesasFixas = despesasFixas;
	}

	public Boolean getResultado() {
		if (resultado == null) {
			resultado = Boolean.FALSE;
		}
		return resultado;
	}

	public void setResultado(Boolean resultado) {
		this.resultado = resultado;
	}

	public Boolean getCategoriasDespesas() {
		if (categoriasDespesas == null) {
			categoriasDespesas = Boolean.FALSE;
		}
		return categoriasDespesas;
	}

	public void setCategoriasDespesas(Boolean categoriasDespesas) {
		this.categoriasDespesas = categoriasDespesas;
	}

}
