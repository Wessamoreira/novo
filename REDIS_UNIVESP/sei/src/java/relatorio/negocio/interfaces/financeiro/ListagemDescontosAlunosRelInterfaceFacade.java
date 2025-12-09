package relatorio.negocio.interfaces.financeiro;

import java.util.Date;
import java.util.List;

import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import relatorio.negocio.comuns.financeiro.ListagemDescontosAlunoUnidadeEnsinoVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

public interface ListagemDescontosAlunosRelInterfaceFacade {

	void validarDados(TurmaVO turmaVO, UnidadeEnsinoCursoVO unidadeEnsinoCursoVO, String campoFiltrarPor, List<UnidadeEnsinoVO> unidadeEnsinoVOs, PeriodicidadeEnum periodicidadeEnum, String ano, String semestre, Date dataInicio, Date dataTermino, Integer descontoProgressivo, Integer convenio, Integer planoDesconto, Boolean apresentarDescontoRateio, Boolean apresentarDescontoAluno, Boolean apresentarDescontoRecebimento) throws Exception;

	List<ListagemDescontosAlunoUnidadeEnsinoVO> criarObjeto(UnidadeEnsinoCursoVO unidadeEnsinoCursoVO, TurmaVO turmaVO, String campoFiltroPor, String ano, String semestre, Integer descontoProgressivo, Integer planoDesconto, Integer convenio, List<UnidadeEnsinoVO> unidadeEnsinoVOs, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, Integer parceiro, Integer categoriaDesconto, String tipoLayout, PeriodicidadeEnum periodicidadeEnum, Date dataInicio, Date dataTermino, UsuarioVO usuarioVO, Boolean apresentarDescontoRateio, Boolean apresentarDescontoAluno, Boolean apresentarDescontoRecebimento) throws Exception;

}
