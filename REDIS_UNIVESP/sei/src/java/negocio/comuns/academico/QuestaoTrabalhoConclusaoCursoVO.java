package negocio.comuns.academico;

import negocio.comuns.arquitetura.SuperVO;

public class QuestaoTrabalhoConclusaoCursoVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7540333131535827190L;

	private Integer codigo;
	private String tipoPergunta;
	private TrabalhoConclusaoCursoVO trabalhoConclusaoCurso;
	private String enunciado;
	private String origemQuestao;
	private Double valor;
	private Integer ordemApresentacao;
	private Double valorMaximoNotaFormatacao;
	private Double valorMaximoNotaConteudo;

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getEnunciado() {
		if (enunciado == null) {
			enunciado = "";
		}
		return enunciado;
	}

	public void setEnunciado(String enunciado) {
		this.enunciado = enunciado;
	}

	public Double getValor() {
		if (valor == null) {
			valor = 0.0;
		}
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public Integer getOrdemApresentacao() {
		if (ordemApresentacao == null) {
			ordemApresentacao = 0;
		}
		return ordemApresentacao;
	}

	public void setOrdemApresentacao(Integer ordemApresentacao) {
		this.ordemApresentacao = ordemApresentacao;
	}

//	public List<OpcaoRespostaVagaQuestaoVO> getOpcaoRespostaVagaQuestaoVOs() {
//		if (opcaoRespostaVagaQuestaoVOs == null) {
//			opcaoRespostaVagaQuestaoVOs = new ArrayList<OpcaoRespostaVagaQuestaoVO>(0);
//		}
//		return opcaoRespostaVagaQuestaoVOs;
//	}
//
//	public void setOpcaoRespostaVagaQuestaoVOs(List<OpcaoRespostaVagaQuestaoVO> opcaoRespostaVagaQuestaoVOs) {
//		this.opcaoRespostaVagaQuestaoVOs = opcaoRespostaVagaQuestaoVOs;
//	}

	public String getTipoPergunta() {
		if (tipoPergunta == null) {
			tipoPergunta = "conteudo";
		}
		return tipoPergunta;
	}

	public void setTipoPergunta(String tipoPergunta) {
		this.tipoPergunta = tipoPergunta;
	}

	public String getOrigemQuestao() {
		if (origemQuestao == null) {
			origemQuestao = "";
		}
		return origemQuestao;
	}

	public void setOrigemQuestao(String origemQuestao) {
		this.origemQuestao = origemQuestao;
	}

	public TrabalhoConclusaoCursoVO getTrabalhoConclusaoCurso() {
		if (trabalhoConclusaoCurso == null) {
			trabalhoConclusaoCurso = new TrabalhoConclusaoCursoVO();
		}
		return trabalhoConclusaoCurso;
	}

	public void setTrabalhoConclusaoCurso(TrabalhoConclusaoCursoVO trabalhoConclusaoCurso) {
		this.trabalhoConclusaoCurso = trabalhoConclusaoCurso;
	}

	public Double getValorMaximoNotaFormatacao() {
		if (valorMaximoNotaFormatacao == null) {
			valorMaximoNotaFormatacao = 0.0;
		}
		return valorMaximoNotaFormatacao;
	}

	public void setValorMaximoNotaFormatacao(Double valorMaximoNotaFormatacao) {
		this.valorMaximoNotaFormatacao = valorMaximoNotaFormatacao;
	}

	public Double getValorMaximoNotaConteudo() {
		if (valorMaximoNotaConteudo == null) {
			valorMaximoNotaConteudo = 0.0;
		}
		return valorMaximoNotaConteudo;
	}

	public void setValorMaximoNotaConteudo(Double valorMaximoNotaConteudo) {
		this.valorMaximoNotaConteudo = valorMaximoNotaConteudo;
	}

}
