package negocio.interfaces.academico;

import java.io.File;
import java.util.Date;
import java.util.List;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.CalendarioRelatorioFinalFacilitadorVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.RelatorioFinalFacilitadorVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;

public interface RelatorioFinalFacilitadorInterfaceFacade {
    
    
    void persistir(RelatorioFinalFacilitadorVO relatorioFinalFacilitadorVO, UsuarioVO usuarioVO) throws Exception;
    void excluir(RelatorioFinalFacilitadorVO relatorioFinalFacilitadorVO, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;
	List<RelatorioFinalFacilitadorVO> consultar(Integer disciplina, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, Boolean validarAcesso, int nivelMontarDados, UsuarioVO usuarioVO, Integer limit, Integer offset) throws Exception;
	Integer consultarTotalRegistro(Integer disciplina, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO) throws Exception;
	Boolean validarRelatorioFinalFacilitador(MatriculaPeriodoTurmaDisciplinaVO matriculaperiodoturmadisciplina);
	void preencherDadosRelatorioFacilitadorQuestionarioRespostaOrigem(RelatorioFinalFacilitadorVO relatorioFinalFacilitadorVO, Integer codigo, UsuarioVO usuarioLogado) throws Exception;
	RelatorioFinalFacilitadorVO consultarPorChamvePrimaria(Integer relatorioFinalFacilitador, Boolean validarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;
	PessoaVO consultarSupervisorPorFacilitador(String matriculaFacilitador, String ano, String semestre, UsuarioVO usuarioVO) throws Exception;
	boolean realizarVerificacaoFacilitador(Integer codigoPessoa, String ano, String semestre) throws Exception;
	List<RelatorioFinalFacilitadorVO> consultarRelatorioFacilitador(RelatorioFinalFacilitadorVO relatorioFinalFacilitador, List<UnidadeEnsinoVO> listaUnidadeEnsino, Date dataFinal, Date dataInicial, CalendarioRelatorioFinalFacilitadorVO  calendarioRelatorioFinalFacilitadorVO, String multiplasSituacoes, int nivelMontarDados, DataModelo dataModelo, UsuarioVO usuario) throws Exception;
	void alterarSituacaoRelatorioFacilitador(RelatorioFinalFacilitadorVO relatorioFinalFacilitador, Double valorHistoricoNota, UsuarioVO usuarioVO);
	List<RelatorioFinalFacilitadorVO> consultarDashboradRelatorioFacilitador(RelatorioFinalFacilitadorVO relatorioFinalFacilitador, List<UnidadeEnsinoVO> listaUnidadeEnsino, Date dataFinal, Date dataInicial, CalendarioRelatorioFinalFacilitadorVO calendarioRelatorioFinalFacilitadorVO, DataModelo dataModelo, UsuarioVO usuario) throws Exception;
	File realizarGeracaoExcel(List<RelatorioFinalFacilitadorVO> listaRelatorioFinalFacilitadorVO, String urlLogoPadraoRelatorio, UsuarioVO usuario) throws Exception;
	void preencherTodosRelatorioFacilitador(List<RelatorioFinalFacilitadorVO> listaRelatorioFinalFacilitadorVOs);
	void desmarcarTodosRelatorioFacilitador(List<RelatorioFinalFacilitadorVO> listaRelatorioFinalFacilitadorVOs);
	List<PessoaVO> consultarSupervisorPorNome(String valorConsulta, String ano, String semestre, UsuarioVO usuarioVO) throws Exception;
	List<PessoaVO> consultarSupervisorPorCPF(String valorConsulta, String ano, String semestre, UsuarioVO usuarioVO) throws Exception;
	StringBuffer montarFiltroTipoSupervisorFacilitador(String ano, String semestre, String tiposalaaulablackboardpessoa);
	void validarDados(RelatorioFinalFacilitadorVO obj) throws Exception;
	RelatorioFinalFacilitadorVO consultarDadosEnvioEmail(RelatorioFinalFacilitadorVO relatorioFinalFacilitadorVO, UsuarioVO usuarioVO) throws Exception;
	Integer consultarTotalizadorNaoEnviouRelatorio(RelatorioFinalFacilitadorVO relatorioFinalFacilitadorFiltroVO, List<UnidadeEnsinoVO> unidadeEnsinoVOMarcadasParaSeremUtilizadas, Date dataEnvioAnaliseInicio, Date dataEnvioAnaliseFim, CalendarioRelatorioFinalFacilitadorVO calendarioRelatorioFinalFacilitadorVO, String multiplasSituacoes) throws Exception;
}
