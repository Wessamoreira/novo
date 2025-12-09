package relatorio.negocio.jdbc.academico;

public class FiltroRelatorioContaReceberVO {
	
	private Boolean tipoOrigemContaReceberInscricaoProcessoSeletivo;
	private Boolean tipoOrigemContaReceberMatricula;
	private Boolean tipoOrigemContaReceberRequerimento;
	private Boolean tipoOrigemContaReceberBiblioteca;
	private Boolean tipoOrigemContaReceberMensalidade;
	private Boolean tipoOrigemContaReceberMaterialDidatico;
	private Boolean tipoOrigemContaReceberDevolucaoCheque;
	private Boolean tipoOrigemContaReceberNegociacao;
	private Boolean tipoOrigemContaReceberBolsaCusteadaConvenio;
	private Boolean tipoOrigemContaReceberContratoReceita;
	private Boolean tipoOrigemContaReceberInclusaoReposicao;
	private Boolean tipoOrigemContaReceberOutros;
	private Boolean tipoOrigemContaReceberTodos;
	
	public void selecionarTodosTiposOrigemContaReceber() {
		if (getTipoOrigemContaReceberTodos()) {
			setTipoOrigemContaReceberInscricaoProcessoSeletivo(true);
			setTipoOrigemContaReceberMatricula(true);
			setTipoOrigemContaReceberRequerimento(true);
			setTipoOrigemContaReceberBiblioteca(true);
			setTipoOrigemContaReceberMensalidade(true);
			setTipoOrigemContaReceberDevolucaoCheque(true);
			setTipoOrigemContaReceberNegociacao(true);
			setTipoOrigemContaReceberBolsaCusteadaConvenio(true);
			setTipoOrigemContaReceberContratoReceita(true);
			setTipoOrigemContaReceberInclusaoReposicao(true);
			setTipoOrigemContaReceberOutros(true);
			setTipoOrigemContaReceberMaterialDidatico(true);
			
		} else {
			setTipoOrigemContaReceberInscricaoProcessoSeletivo(false);
			setTipoOrigemContaReceberMatricula(false);
			setTipoOrigemContaReceberRequerimento(false);
			setTipoOrigemContaReceberBiblioteca(false);
			setTipoOrigemContaReceberMensalidade(false);
			setTipoOrigemContaReceberDevolucaoCheque(false);
			setTipoOrigemContaReceberNegociacao(false);
			setTipoOrigemContaReceberBolsaCusteadaConvenio(false);
			setTipoOrigemContaReceberContratoReceita(false);
			setTipoOrigemContaReceberInclusaoReposicao(false);
			setTipoOrigemContaReceberOutros(false);
			setTipoOrigemContaReceberMaterialDidatico(false);
		}
	}
	
	public Boolean getTipoOrigemContaReceberInscricaoProcessoSeletivo() {
		if (tipoOrigemContaReceberInscricaoProcessoSeletivo == null) {
			tipoOrigemContaReceberInscricaoProcessoSeletivo = Boolean.FALSE;
		}
		return tipoOrigemContaReceberInscricaoProcessoSeletivo;
	}

	public void setTipoOrigemContaReceberInscricaoProcessoSeletivo(Boolean tipoOrigemContaReceberInscricaoProcessoSeletivo) {
		this.tipoOrigemContaReceberInscricaoProcessoSeletivo = tipoOrigemContaReceberInscricaoProcessoSeletivo;
	}

	public Boolean getTipoOrigemContaReceberMatricula() {
		if (tipoOrigemContaReceberMatricula == null) {
			tipoOrigemContaReceberMatricula = Boolean.TRUE;
		}
		return tipoOrigemContaReceberMatricula;
	}

	public void setTipoOrigemContaReceberMatricula(Boolean tipoOrigemContaReceberMatricula) {
		this.tipoOrigemContaReceberMatricula = tipoOrigemContaReceberMatricula;
	}

	public Boolean getTipoOrigemContaReceberRequerimento() {
		if (tipoOrigemContaReceberRequerimento == null) {
			tipoOrigemContaReceberRequerimento = Boolean.FALSE;
		}
		return tipoOrigemContaReceberRequerimento;
	}

	public void setTipoOrigemContaReceberRequerimento(Boolean tipoOrigemContaReceberRequerimento) {
		this.tipoOrigemContaReceberRequerimento = tipoOrigemContaReceberRequerimento;
	}

	public Boolean getTipoOrigemContaReceberBiblioteca() {
		if (tipoOrigemContaReceberBiblioteca == null) {
			tipoOrigemContaReceberBiblioteca = Boolean.FALSE;
		}
		return tipoOrigemContaReceberBiblioteca;
	}

	public void setTipoOrigemContaReceberBiblioteca(Boolean tipoOrigemContaReceberBiblioteca) {
		this.tipoOrigemContaReceberBiblioteca = tipoOrigemContaReceberBiblioteca;
	}

	public Boolean getTipoOrigemContaReceberMensalidade() {
		if (tipoOrigemContaReceberMensalidade == null) {
			tipoOrigemContaReceberMensalidade = Boolean.TRUE;
		}
		return tipoOrigemContaReceberMensalidade;
	}

	public void setTipoOrigemContaReceberMensalidade(Boolean tipoOrigemContaReceberMensalidade) {
		this.tipoOrigemContaReceberMensalidade = tipoOrigemContaReceberMensalidade;
	}

	public Boolean getTipoOrigemContaReceberDevolucaoCheque() {
		if (tipoOrigemContaReceberDevolucaoCheque == null) {
			tipoOrigemContaReceberDevolucaoCheque = Boolean.FALSE;
		}
		return tipoOrigemContaReceberDevolucaoCheque;
	}

	public void setTipoOrigemContaReceberDevolucaoCheque(Boolean tipoOrigemContaReceberDevolucaoCheque) {
		this.tipoOrigemContaReceberDevolucaoCheque = tipoOrigemContaReceberDevolucaoCheque;
	}

	public Boolean getTipoOrigemContaReceberNegociacao() {
		if (tipoOrigemContaReceberNegociacao == null) {
			tipoOrigemContaReceberNegociacao = Boolean.FALSE;
		}
		return tipoOrigemContaReceberNegociacao;
	}

	public void setTipoOrigemContaReceberNegociacao(Boolean tipoOrigemContaReceberNegociacao) {
		this.tipoOrigemContaReceberNegociacao = tipoOrigemContaReceberNegociacao;
	}

	public Boolean getTipoOrigemContaReceberBolsaCusteadaConvenio() {
		if (tipoOrigemContaReceberBolsaCusteadaConvenio == null) {
			tipoOrigemContaReceberBolsaCusteadaConvenio = Boolean.FALSE;
		}
		return tipoOrigemContaReceberBolsaCusteadaConvenio;
	}

	public void setTipoOrigemContaReceberBolsaCusteadaConvenio(Boolean tipoOrigemContaReceberBolsaCusteadaConvenio) {
		this.tipoOrigemContaReceberBolsaCusteadaConvenio = tipoOrigemContaReceberBolsaCusteadaConvenio;
	}

	public Boolean getTipoOrigemContaReceberContratoReceita() {
		if (tipoOrigemContaReceberContratoReceita == null) {
			tipoOrigemContaReceberContratoReceita = Boolean.FALSE;
		}
		return tipoOrigemContaReceberContratoReceita;
	}

	public void setTipoOrigemContaReceberContratoReceita(Boolean tipoOrigemContaReceberContratoReceita) {
		this.tipoOrigemContaReceberContratoReceita = tipoOrigemContaReceberContratoReceita;
	}

	public Boolean getTipoOrigemContaReceberInclusaoReposicao() {
		if (tipoOrigemContaReceberInclusaoReposicao == null) {
			tipoOrigemContaReceberInclusaoReposicao = Boolean.FALSE;
		}
		return tipoOrigemContaReceberInclusaoReposicao;
	}

	public void setTipoOrigemContaReceberInclusaoReposicao(Boolean tipoOrigemContaReceberInclusaoReposicao) {
		this.tipoOrigemContaReceberInclusaoReposicao = tipoOrigemContaReceberInclusaoReposicao;
	}

	public Boolean getTipoOrigemContaReceberOutros() {
		if (tipoOrigemContaReceberOutros == null) {
			tipoOrigemContaReceberOutros = Boolean.FALSE;
		}
		return tipoOrigemContaReceberOutros;
	}

	public void setTipoOrigemContaReceberOutros(Boolean tipoOrigemContaReceberOutros) {
		this.tipoOrigemContaReceberOutros = tipoOrigemContaReceberOutros;
	}

	public Boolean getTipoOrigemContaReceberTodos() {
		if (tipoOrigemContaReceberTodos == null) {
			tipoOrigemContaReceberTodos = Boolean.FALSE;
		}
		return tipoOrigemContaReceberTodos;
	}

	public void setTipoOrigemContaReceberTodos(Boolean tipoOrigemContaReceberTodos) {
		this.tipoOrigemContaReceberTodos = tipoOrigemContaReceberTodos;
	}

	public Boolean getTipoOrigemContaReceberMaterialDidatico() {
		if (tipoOrigemContaReceberMaterialDidatico == null) {
			tipoOrigemContaReceberMaterialDidatico = Boolean.FALSE;
		}
		return tipoOrigemContaReceberMaterialDidatico;
	}

	public void setTipoOrigemContaReceberMaterialDidatico(Boolean tipoOrigemContaReceberMaterialDidatico) {
		this.tipoOrigemContaReceberMaterialDidatico = tipoOrigemContaReceberMaterialDidatico;
	}
	
	

}
