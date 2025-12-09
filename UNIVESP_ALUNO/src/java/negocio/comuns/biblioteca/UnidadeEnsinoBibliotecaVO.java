package negocio.comuns.biblioteca;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;

public class UnidadeEnsinoBibliotecaVO extends SuperVO {

    private UnidadeEnsinoVO unidadeEnsino;
    private Integer biblioteca;
    public static final long serialVersionUID = 1L;

    public UnidadeEnsinoBibliotecaVO() {
        super();
    }

    public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
        this.unidadeEnsino = unidadeEnsino;
    }

    public UnidadeEnsinoVO getUnidadeEnsino() {
        if (unidadeEnsino == null) {
            unidadeEnsino = new UnidadeEnsinoVO();
        }
        return unidadeEnsino;
    }

	public Integer getBiblioteca() {
		if (biblioteca == null) {
			biblioteca = 0;
		}
		return biblioteca;
	}

	public void setBiblioteca(Integer biblioteca) {
		this.biblioteca = biblioteca;
	}

    
}
