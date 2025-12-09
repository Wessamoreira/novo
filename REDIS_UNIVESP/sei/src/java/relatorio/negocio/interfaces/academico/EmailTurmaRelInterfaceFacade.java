package relatorio.negocio.interfaces.academico;

import negocio.comuns.academico.TurmaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import relatorio.negocio.comuns.academico.EmailTurmaRelVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

public interface EmailTurmaRelInterfaceFacade {

	public EmailTurmaRelVO criarObjeto(Integer curso, Integer turma, String ano, String semestre, boolean alunoReposicao, Integer disciplina, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO) throws Exception;

	public String getIdEntidadeEmailTurmaRel();

	public void validarDados(Integer unidadeEnsino, TurmaVO turmaVO, String ano, String semestre) throws Exception;
}