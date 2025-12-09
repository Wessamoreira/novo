package relatorio.negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.arquitetura.UsuarioVO;
public interface ComunicadoDebitoDocumentosAlunoRelInterfaceFacade {

//	public List<ComunicadoDebitoDocumentosAlunoVO> criarObjeto(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception ;

    public void setDescricaoFiltros(String string);

    public String getDescricaoFiltros();

}