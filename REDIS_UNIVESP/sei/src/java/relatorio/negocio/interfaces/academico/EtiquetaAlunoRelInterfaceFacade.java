package relatorio.negocio.interfaces.academico;

import java.util.Date;
import java.util.List;

import relatorio.negocio.comuns.financeiro.FiltroRelatorioFinanceiroVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.LayoutEtiquetaVO;
import negocio.comuns.financeiro.CartaCobrancaRelVO;
import negocio.comuns.financeiro.CentroReceitaVO;

public interface EtiquetaAlunoRelInterfaceFacade {

	public String realizarImpressaoEtiquetaMatricula(LayoutEtiquetaVO layoutEtiqueta, Integer numeroCopias, Integer linha, Integer coluna, Integer curso, Integer turma, String matricula, String ano, String semestre, Integer periodoletivo, String tipoRelatorio, String nivelEducacional, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, UsuarioVO usuarioLogado, String via) throws Exception;
	
	public String realizarImpressaoEtiquetaCartaCobranca(LayoutEtiquetaVO layoutEtiqueta, CartaCobrancaRelVO cartaCobranca, Date periodoInicial, Date periodoFinal, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, Integer numeroCopias, Integer linha, Integer coluna, Integer curso, Integer turma, String matricula, String ano, String semestre, Integer periodoletivo, String tipoRelatorio, String nivelEducacional, List<CentroReceitaVO> centroReceitaVOs, UnidadeEnsinoVO unidadeEnsino, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO,  UsuarioVO usuarioLogado) throws Exception;
}