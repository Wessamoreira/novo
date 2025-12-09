package relatorio.negocio.interfaces.academico;

import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TextoPadraoDeclaracaoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import relatorio.negocio.comuns.academico.DeclaracaoPasseEstudantilVO;

public interface DeclaracaoPasseEstudantilRelInterfaceFacade {

	public DeclaracaoPasseEstudantilVO consultarPorCodigoAluno(MatriculaVO matricula, Integer nivelMontarDados,String observacao, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception;

	public DeclaracaoPasseEstudantilVO montarDados(PessoaVO pessoa, MatriculaPeriodoVO matPeriodo, MatriculaVO matricula, String observacao) throws Exception;

	public String imprimirDeclaracaoPasseEstudantil(DeclaracaoPasseEstudantilVO declaracaoPasseEstudantilVO, TextoPadraoDeclaracaoVO textoPadraoDeclaracao, MatriculaVO matriculaVO, ConfiguracaoGeralSistemaVO config, UsuarioVO usuario) throws Exception;

}