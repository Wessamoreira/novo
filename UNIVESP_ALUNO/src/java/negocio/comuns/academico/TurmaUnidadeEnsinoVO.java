package negocio.comuns.academico;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;

public class TurmaUnidadeEnsinoVO extends SuperVO  {
	
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private String identificadorTurma;
	private String categoriaCondicaoPagamento;
//	private PlanoFinanceiroCursoVO planoFinanceiroCurso;
	private String regraDefinicaoVagas;
	private Integer nrVagas;
	private Integer nrVagasMaxima;
	private Integer nrVagasReposicao;
	private Integer ano;
	private Integer semestre;
	private Boolean registrarAberturaConfirmacao;
	private TurmaAberturaVO turmaAberturaVO;
	private Boolean vincularTurmasCloneNaTurmaAgrupada;
	private TurmaAgrupadaVO turmaAgrupadaVO;
	
	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		return unidadeEnsinoVO;
	}
	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}
	public String getIdentificadorTurma() {
		if(identificadorTurma == null) {
			identificadorTurma = "";
		}
		return identificadorTurma;
	}
	public void setIdentificadorTurma(String identificadorTurma) {
		this.identificadorTurma = identificadorTurma;
	}
	public String getCategoriaCondicaoPagamento() {
		if(categoriaCondicaoPagamento == null) {
			categoriaCondicaoPagamento = "";
		}
		return categoriaCondicaoPagamento;
	}
	public void setCategoriaCondicaoPagamento(String categoriaCondicaoPagamento) {
		this.categoriaCondicaoPagamento = categoriaCondicaoPagamento;
	}
//	
	public String getRegraDefinicaoVagas() {
		if(regraDefinicaoVagas == null) {
			regraDefinicaoVagas = "";
		}
		return regraDefinicaoVagas;
	}
	public void setRegraDefinicaoVagas(String regraDefinicaoVagas) {
		this.regraDefinicaoVagas = regraDefinicaoVagas;
	}
	public Integer getNrVagas() {
		if(nrVagas == null) {
			nrVagas = 0;
		}
		return nrVagas;
	}
	public void setNrVagas(Integer nrVagas) {
		this.nrVagas = nrVagas;
	}
	
	
	
	public Integer getNrVagasMaxima() {
		if(nrVagasMaxima == null) {
			nrVagasMaxima = 0;
		}
		return nrVagasMaxima;
	}
	public void setNrVagasMaxima(Integer nrVagasMaxima) {
		this.nrVagasMaxima = nrVagasMaxima;
	}
	public Boolean getExibirDados() {
		return getRegraDefinicaoVagas().equals("VM");
	}
	public Integer getNrVagasReposicao() {
		if(nrVagasReposicao == null) {
			nrVagasReposicao = 0;
		}
		return nrVagasReposicao;
	}
	public void setNrVagasReposicao(Integer nrVagasReposicao) {
		this.nrVagasReposicao = nrVagasReposicao;
	}
	public Integer getAno() {
		return ano;
	}
	public void setAno(Integer ano) {
		this.ano = ano;
	}
	public Integer getSemestre() {
		return semestre;
	}
	public void setSemestre(Integer semestre) {
		this.semestre = semestre;
	}
	public Boolean getRegistrarAberturaConfirmacao() {
		if(registrarAberturaConfirmacao == null) {
			registrarAberturaConfirmacao = false;
		}
		return registrarAberturaConfirmacao;
	}
	public void setRegistrarAberturaConfirmacao(Boolean registrarAberturaConfirmacao) {
		this.registrarAberturaConfirmacao = registrarAberturaConfirmacao;
	}
	public TurmaAberturaVO getTurmaAberturaVO() {
		if(turmaAberturaVO == null) {
			turmaAberturaVO = new TurmaAberturaVO();
		}
		return turmaAberturaVO;
	}
	public void setTurmaAberturaVO(TurmaAberturaVO turmaAberturaVO) {
		this.turmaAberturaVO = turmaAberturaVO;
	}
	public Boolean getVincularTurmasCloneNaTurmaAgrupada() {
		if(vincularTurmasCloneNaTurmaAgrupada == null) {
			vincularTurmasCloneNaTurmaAgrupada = false;
		}
		return vincularTurmasCloneNaTurmaAgrupada;
	}
	public void setVincularTurmasCloneNaTurmaAgrupada(Boolean vincularTurmasCloneNaTurmaAgrupada) {
		this.vincularTurmasCloneNaTurmaAgrupada = vincularTurmasCloneNaTurmaAgrupada;
	}
	public TurmaAgrupadaVO getTurmaAgrupadaVO() {
		if(turmaAgrupadaVO == null) {
			turmaAgrupadaVO = new TurmaAgrupadaVO();
		}
		return turmaAgrupadaVO;
	}
	public void setTurmaAgrupadaVO(TurmaAgrupadaVO turmaAgrupadaVO) {
		this.turmaAgrupadaVO = turmaAgrupadaVO;
	}

}
