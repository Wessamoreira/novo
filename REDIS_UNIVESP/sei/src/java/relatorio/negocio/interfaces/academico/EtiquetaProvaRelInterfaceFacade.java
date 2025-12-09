package relatorio.negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.LayoutEtiquetaVO;
import negocio.comuns.biblioteca.enumeradores.TipoRelatorioEtiquetaAcademicoEnum;
import relatorio.negocio.comuns.academico.EtiquetaProvaRelVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

/**
 *
 * @author Carlos
 */
public interface EtiquetaProvaRelInterfaceFacade {
	public EtiquetaProvaRelVO realizarCriacaoOjbRel(Integer unidadeEnsino, TurmaVO turma, Integer disciplina, String ano, String semestre, String turno, Boolean trazerAlunoPendenteFinanceiramente, TipoRelatorioEtiquetaAcademicoEnum tipoRelatorioAcademico, FiltroRelatorioAcademicoVO filtroAcademicoVO) throws Exception;

	public List montarListaSelectItemUnidadeEnsino(UnidadeEnsinoVO unidadeEnsinoLogado, UsuarioVO usuarioLogado) throws Exception;

	public List consultarTurma(String campoConsultaTurma, UnidadeEnsinoVO unidadeEnsinoVO, String valorConsultaTurma, UsuarioVO usuarioLogado) throws Exception;

	public void validarDados(Integer unidadeEnsino, TurmaVO turma, String ano, String semestre) throws Exception;

	String realizarImpressaoEtiquetaProva(LayoutEtiquetaVO layoutEtiqueta, List<EtiquetaProvaRelVO> etiquetaProvaRelVOs, Integer numeroCopias, Integer linha, Integer coluna, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioLogado) throws Exception;

	List<EtiquetaProvaRelVO> consultarOjbRel(Integer unidadeEnsino, TurmaVO turma, Integer disciplina, String ano, String semestre, String turno, Boolean trazerAlunoPendenteFinanceiramente, TipoRelatorioEtiquetaAcademicoEnum tipoRelatorioAcademico, FiltroRelatorioAcademicoVO filtroAcademicoVO) throws Exception;
}
