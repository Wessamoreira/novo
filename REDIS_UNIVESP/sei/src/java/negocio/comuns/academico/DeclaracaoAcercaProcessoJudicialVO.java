package negocio.comuns.academico;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.Constantes;

/**
 * Reponsável por manter os dados da entidade DeclaracaoAcercaProcessoJudicial. Classe
 * do tipo VO - Value Object composta pelos atributos da entidade com
 * visibilidade protegida e os métodos de acesso a estes atributos. Classe
 * utilizada para apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 * @author Felipi Alves
 */
public class DeclaracaoAcercaProcessoJudicialVO extends SuperVO {

	private static final long serialVersionUID = 1L;

	private Integer codigo;
	private String declaracao;
	private Boolean selecionado;
	private ExpedicaoDiplomaVO expedicaoDiploma;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getDeclaracao() {
		if (declaracao == null) {
			declaracao = Constantes.EMPTY;
		}
		return declaracao;
	}

	public void setDeclaracao(String declaracao) {
		this.declaracao = declaracao;
	}

	public ExpedicaoDiplomaVO getExpedicaoDiploma() {
		if (expedicaoDiploma == null) {
			expedicaoDiploma = new ExpedicaoDiplomaVO();
		}
		return expedicaoDiploma;
	}

	public void setExpedicaoDiploma(ExpedicaoDiplomaVO expedicaoDiploma) {
		this.expedicaoDiploma = expedicaoDiploma;
	}
	
	public Boolean getSelecionado() {
		if (selecionado == null) {
			selecionado = Boolean.FALSE;
		}
		return selecionado;
	}
	
	public void setSelecionado(Boolean selecionado) {
		this.selecionado = selecionado;
	}

}
