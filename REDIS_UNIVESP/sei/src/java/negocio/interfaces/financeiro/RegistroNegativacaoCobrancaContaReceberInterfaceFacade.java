package negocio.interfaces.financeiro;

import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.AgenteNegativacaoCobrancaContaReceberVO;
import negocio.comuns.financeiro.CentroReceitaVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.RegistroNegativacaoCobrancaContaReceberItemVO;
import negocio.comuns.financeiro.RegistroNegativacaoCobrancaContaReceberVO;
import negocio.comuns.financeiro.enumerador.IntegracaoNegativacaoCobrancaContaReceberEnum;
import negocio.comuns.financeiro.enumerador.TipoAgenteNegativacaoCobrancaContaReceberEnum;
import negocio.comuns.job.RegistroExecucaoJobVO;
import negocio.comuns.utilitarias.ConsistirException;
import relatorio.negocio.comuns.financeiro.FiltroRelatorioFinanceiroVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

public interface RegistroNegativacaoCobrancaContaReceberInterfaceFacade {

	void persistir(RegistroNegativacaoCobrancaContaReceberVO obj, boolean verificarAcesso, ConfiguracaoGeralSistemaVO config, UsuarioVO usuarioVO) throws Exception;
	void validarDados(UnidadeEnsinoVO unidadeEnsino, AgenteNegativacaoCobrancaContaReceberVO agente, Date dataFim) throws ConsistirException;
	void validarDadosExclusao(RegistroNegativacaoCobrancaContaReceberItemVO registroExclusao) throws ConsistirException;
	List<RegistroNegativacaoCobrancaContaReceberVO> consultarPorDataGeracao(Date dataGeracaoInicio, Date dataGeracaoFim, boolean controlarAcesso, UsuarioVO usuarioLogado) throws Exception;
	List<RegistroNegativacaoCobrancaContaReceberVO> consultarPorUnidadeEnsino(String valorConsulta, boolean controlarAcesso, UsuarioVO usuarioLogado) throws Exception;
	List<RegistroNegativacaoCobrancaContaReceberVO> consultarPorCurso(Date dataGeracaoInicio, Date dataGeracaoFim, Integer valorConsultaUnidadeEnsino, String valorConsulta, boolean controlarAcesso, UsuarioVO usuarioLogado) throws Exception;
	List<RegistroNegativacaoCobrancaContaReceberVO> consultarPorTurma(Date dataGeracaoInicio, Date dataGeracaoFim, Integer valorConsultaUnidadeEnsino, String valorConsulta, boolean controlarAcesso, UsuarioVO usuarioLogado) throws Exception;
	List<RegistroNegativacaoCobrancaContaReceberVO> consultarPorAluno(Date dataGeracaoInicio, Date dataGeracaoFim, Integer valorConsultaUnidadeEnsino, String valorConsulta, boolean controlarAcesso, UsuarioVO usuarioLogado) throws Exception;
	List<RegistroNegativacaoCobrancaContaReceberVO> consultarPorMatricula(Date dataGeracaoInicio, Date dataGeracaoFim, Integer valorConsultaUnidadeEnsino, String valorConsulta,boolean validarDataExclusao,  boolean controlarAcesso, UsuarioVO usuarioLogado) throws Exception;
	List<RegistroNegativacaoCobrancaContaReceberVO> consultarPorUsuario(Date dataGeracaoInicio, Date dataGeracaoFim, Integer valorConsultaUnidadeEnsino, String valorConsulta, boolean controlarAcesso, UsuarioVO usuarioLogado) throws Exception;
	List<RegistroNegativacaoCobrancaContaReceberItemVO> consultarContasReceberPorRegistroNegativacaoCobranca(Integer codigoRegistro, UsuarioVO usuarioLogado) throws Exception;
	List<SelectItem> montarListaSelectItemUnidadeEnsino(UnidadeEnsinoVO unidadeEnsinoLogado, UsuarioVO usuarioLogado) throws Exception;
	RegistroNegativacaoCobrancaContaReceberVO criarRegistro(AgenteNegativacaoCobrancaContaReceberVO agente, TipoAgenteNegativacaoCobrancaContaReceberEnum tipoAgente, UnidadeEnsinoVO unidadeEnsino, CursoVO curso, TurmaVO turma, MatriculaVO matricula, UsuarioVO usuarioVO, Date periodoInicial, Date periodoFinal, String aluno, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, List<CentroReceitaVO> centroReceitaVOs, String centroReceita) throws Exception;
	void executarCriacaoRegistroPorAgenteNegativacaoCobrancaContaReceberPorAPI(RegistroExecucaoJobVO registroExecucaoJobVO, ConfiguracaoGeralSistemaVO config) throws Exception;
	void executarRemocaoRegistroPorAgenteNegativacaoCobrancaContaReceberPorAPI(RegistroExecucaoJobVO registroExecucaoJobVO, ConfiguracaoGeralSistemaVO config) throws Exception;	
	
//	RegistroNegativacaoCobrancaContaReceberVO gravarRegistro(RegistroNegativacaoCobrancaContaReceberVO registroNegativacaoCobrancaContaReceberVO, UsuarioVO usuarioLogado) throws Exception;
	void incluir(final RegistroNegativacaoCobrancaContaReceberVO obj, UsuarioVO usuarioLogado) throws Exception;
	void executarExclusaoRegistroNegativacaoCobranca(RegistroNegativacaoCobrancaContaReceberItemVO item, UsuarioVO usuario) throws Exception;
	void executarExclusaoTotalRegistroNegativacaoCobranca(RegistroNegativacaoCobrancaContaReceberVO registro, String motivo, UsuarioVO usuario) throws Exception;
	String consultarSituacaoContaReceber(IntegracaoNegativacaoCobrancaContaReceberEnum integracao, String matricula, Integer curso) throws Exception;
	RegistroNegativacaoCobrancaContaReceberVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<RegistroNegativacaoCobrancaContaReceberVO> consultarPorNossoNumero(Integer valorConsultaUnidadeEnsino, String valorConsulta, boolean controlarAcesso, UsuarioVO usuarioLogado) throws Exception;
	public List<RegistroNegativacaoCobrancaContaReceberVO> consultarContasReceberPorRegistroNegativacaoCobrancaPorPessoa(Integer codigoPessoa, UsuarioVO usuarioLogado) throws Exception;
	public List<RegistroNegativacaoCobrancaContaReceberItemVO> consultarContasReceberPorRegistroNegativacaoCobrancaItemPorPessoa(Integer codigoAgente, Integer codigoPessoa, UsuarioVO usuarioLogado) throws Exception;
	public List<ContaReceberVO> consultarNegociacao(Integer codigoNegociacaoContaReceber);
	public Boolean consultarContaReceberNegativadaCobranca(Integer matriculaPeriodo);
	public List<RegistroNegativacaoCobrancaContaReceberItemVO> execultarConsultaContaReceberPendente(TipoAgenteNegativacaoCobrancaContaReceberEnum tipoAgente, AgenteNegativacaoCobrancaContaReceberVO agente, UnidadeEnsinoVO unidadeEnsino, CursoVO curso, TurmaVO turma, MatriculaVO matricula, UsuarioVO usuarioVO, Date periodoInicial, Date periodoFinal, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, List<CentroReceitaVO> centroReceitaVOs , Boolean jobSerasaApiGeo) throws Exception;
	public Boolean consultarContaReceberNegativadaCobrancaContaReceber(Integer contaReceber);
	void consultar(DataModelo dataModelo, String consultarPor, Date dataGeracaoInicio, Date dataGeracaoFim,
			Integer valorConsultaUnidadeEnsino, String valorConsulta, boolean controlarAcesso, UsuarioVO usuarioLogado)
			throws Exception;
}