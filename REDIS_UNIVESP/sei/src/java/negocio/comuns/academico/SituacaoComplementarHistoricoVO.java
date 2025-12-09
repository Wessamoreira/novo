package negocio.comuns.academico;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.dominios.SituacaoHistorico;

public class SituacaoComplementarHistoricoVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 249438205545932917L;
	private Integer codigo;
	private String nome;
	private String sigla;
	private SituacaoHistorico situacaoHistorico;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		if (nome == null) {
			nome = "";
		}
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSigla() {
		if (sigla == null) {
			sigla = "";
		}
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public SituacaoHistorico getSituacaoHistorico() {
		return situacaoHistorico;
	}

	public void setSituacaoHistorico(SituacaoHistorico situacaoHistorico) {
		this.situacaoHistorico = situacaoHistorico;
	}

}
