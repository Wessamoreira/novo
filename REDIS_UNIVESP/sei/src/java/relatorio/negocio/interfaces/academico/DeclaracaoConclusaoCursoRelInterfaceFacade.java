package relatorio.negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TextoPadraoDeclaracaoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import relatorio.negocio.comuns.academico.DeclaracaoConclusaoCursoVO;

public interface DeclaracaoConclusaoCursoRelInterfaceFacade {

    public List<DeclaracaoConclusaoCursoVO> criarObjeto(DeclaracaoConclusaoCursoVO declaracaoConclusaoCursoVO, MatriculaVO matriculaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception;
        
    public void executarVerificacaoDeConclusaoCurso(MatriculaVO matricula) throws Exception;

    public String getDescricaoFiltros();

    public void setDescricaoFiltros(String string);

    public String imprimirDeclaracaoConclusaoCurso(MatriculaVO matriculaVO, DeclaracaoConclusaoCursoVO declaracaoConclusaoCursoVO, TextoPadraoDeclaracaoVO textoPadraoDeclaracao, ConfiguracaoGeralSistemaVO config, UsuarioVO usuario) throws Exception;
}