package relatorio.negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TextoPadraoDeclaracaoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import relatorio.negocio.comuns.academico.DeclaracaoAbandonoCursoVO;

public interface DeclaracaoAbandonoCursoRelInterfaceFacade {

	public List<DeclaracaoAbandonoCursoVO> criarObjeto(MatriculaVO matriculaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception;

    public void setDescricaoFiltros(String string);

    public String getDescricaoFiltros();

    public String imprimirDeclaracaoAbandonoCurso(MatriculaVO matriculaVO, TextoPadraoDeclaracaoVO textoPadraoDeclaracao, ConfiguracaoGeralSistemaVO config, UsuarioVO usuario) throws Exception;

}