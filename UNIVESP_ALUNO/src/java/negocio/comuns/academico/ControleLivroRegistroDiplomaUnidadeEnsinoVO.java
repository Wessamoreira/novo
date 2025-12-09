package negocio.comuns.academico;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;

/**
 * Reponsável por manter os dados da entidade ControleLivroRegistroDiploma.
 * Classe do tipo VO - Value Object composta pelos atributos da entidade com
 * visibilidade protegida e os métodos de acesso a estes atributos. Classe
 * utilizada para apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class ControleLivroRegistroDiplomaUnidadeEnsinoVO extends SuperVO {

	public static final long serialVersionUID = 1L;

	private Integer codigo;
	
	private ControleLivroRegistroDiplomaVO controleLivroRegistroDiploma;
	
	private UnidadeEnsinoVO unidadeEnsino;
	private  Boolean selecionado;

	
	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}
	
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	
	public ControleLivroRegistroDiplomaVO getControleLivroRegistroDiploma() {
		if (controleLivroRegistroDiploma == null) {
			controleLivroRegistroDiploma = new ControleLivroRegistroDiplomaVO();
		}
		return controleLivroRegistroDiploma;
	}
	
	public void setControleLivroRegistroDiploma(ControleLivroRegistroDiplomaVO controleLivroRegistroDiploma) {
		this.controleLivroRegistroDiploma = controleLivroRegistroDiploma;
	}
	
	public UnidadeEnsinoVO getUnidadeEnsino() {
		if (unidadeEnsino == null) {
			unidadeEnsino = new UnidadeEnsinoVO();
		}
		return unidadeEnsino;
	}
	
	public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}
	
	public Boolean getSelecionado() {
		if(selecionado == null) {
			selecionado =  false;
		}
		return selecionado;
		}
		public void setSelecionado(Boolean selecionado) {
			this.selecionado = selecionado;
		}
		
}
