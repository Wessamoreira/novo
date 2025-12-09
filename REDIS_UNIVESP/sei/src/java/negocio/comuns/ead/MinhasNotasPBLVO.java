package negocio.comuns.ead;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.academico.ConteudoUnidadePaginaRecursoEducacionalVO;
import negocio.comuns.academico.TemaAssuntoVO;
import negocio.comuns.arquitetura.SuperVO;

/**
 * Classe nao persistida no banco de dados somente para uso em tela.
 * 
 * @author Pedro
 *
 */
public class MinhasNotasPBLVO extends SuperVO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 688091588629742386L;
	
	private TemaAssuntoVO temaAssuntoVO;
	private List<ConteudoUnidadePaginaRecursoEducacionalVO> listaConteudoUnidadePaginaRecursoEducacional;
	private GestaoEventoConteudoTurmaAvaliacaoPBLVO gestaoEventoConteudoTurmaAvaliacaoPBLResultadoFinalGeralVO;

	public TemaAssuntoVO getTemaAssuntoVO() {
		if (temaAssuntoVO == null) {
			temaAssuntoVO = new TemaAssuntoVO();
		}

		return temaAssuntoVO;
	}

	public void setTemaAssuntoVO(TemaAssuntoVO temaAssuntoVO) {
		this.temaAssuntoVO = temaAssuntoVO;
	}

	public List<ConteudoUnidadePaginaRecursoEducacionalVO> getListaConteudoUnidadePaginaRecursoEducacional() {
		if (listaConteudoUnidadePaginaRecursoEducacional == null) {
			listaConteudoUnidadePaginaRecursoEducacional = new ArrayList<ConteudoUnidadePaginaRecursoEducacionalVO>();
		}
		return listaConteudoUnidadePaginaRecursoEducacional;
	}

	public void setListaConteudoUnidadePaginaRecursoEducacional(List<ConteudoUnidadePaginaRecursoEducacionalVO> listaConteudoUnidadePaginaRecursoEducacional) {
		this.listaConteudoUnidadePaginaRecursoEducacional = listaConteudoUnidadePaginaRecursoEducacional;
	}
	
	public Integer getSizeListaConteudoUnidadePaginaRecursoEducacional(){
		return getListaConteudoUnidadePaginaRecursoEducacional().size() + 1;
	}

	public GestaoEventoConteudoTurmaAvaliacaoPBLVO getGestaoEventoConteudoTurmaAvaliacaoPBLResultadoFinalGeralVO() {
		if(gestaoEventoConteudoTurmaAvaliacaoPBLResultadoFinalGeralVO == null){
			gestaoEventoConteudoTurmaAvaliacaoPBLResultadoFinalGeralVO = new GestaoEventoConteudoTurmaAvaliacaoPBLVO();
		}
		return gestaoEventoConteudoTurmaAvaliacaoPBLResultadoFinalGeralVO;
	}

	public void setGestaoEventoConteudoTurmaAvaliacaoPBLResultadoFinalGeralVO(GestaoEventoConteudoTurmaAvaliacaoPBLVO gestaoEventoConteudoTurmaAvaliacaoPBLResultadoFinalGeralVO) {
		this.gestaoEventoConteudoTurmaAvaliacaoPBLResultadoFinalGeralVO = gestaoEventoConteudoTurmaAvaliacaoPBLResultadoFinalGeralVO;
	}
	
	

}
