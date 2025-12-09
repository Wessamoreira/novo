package negocio.facade.jdbc.biblioteca;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.faces. context.ExternalContext;
import jakarta.faces. context.FacesContext;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.net.ftp.FTPClient;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import controle.academico.VisaoAlunoControle;
import controle.arquitetura.SuperControle;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.biblioteca.ArquivoMarc21CatalogoVO;
import negocio.comuns.biblioteca.ArquivoMarc21VO;
import negocio.comuns.biblioteca.CatalogoVO;
import negocio.comuns.biblioteca.ConfiguracaoBibliotecaVO;
import negocio.comuns.biblioteca.enumeradores.FrequenciaNotificacaoGestoresEnum;
import negocio.comuns.biblioteca.enumeradores.RegraAplicacaoBloqueioBibliotecaEnum;
import negocio.comuns.biblioteca.enumeradores.TipoClassificacaoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ProcessarParalelismo;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisFTP;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.biblioteca.ConfiguracaoBibliotecaInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>ConfiguracaoBibliotecaVO</code>. Responsável por
 * implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>ConfiguracaoBibliotecaVO</code>. Encapsula toda a
 * interação com o banco de dados.
 * 
 * @see ConfiguracaoBibliotecaVO
 * @see ControleAcesso
 */
@Repository
public class ConfiguracaoBiblioteca extends ControleAcesso implements ConfiguracaoBibliotecaInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public ConfiguracaoBiblioteca() throws Exception {
		super();
		setIdEntidade("ConfiguracaoBiblioteca");
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe <code>ConfiguracaoBibliotecaVO</code>.
	 */
	public ConfiguracaoBibliotecaVO novo() throws Exception {
		ConfiguracaoBiblioteca.incluir(getIdEntidade());
		ConfiguracaoBibliotecaVO obj = new ConfiguracaoBibliotecaVO();
		return obj;
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe <code>ConfiguracaoBibliotecaVO</code>. Primeiramente valida os dados (
	 * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 *
	 * @param obj
	 *            Objeto da classe <code>ConfiguracaoBibliotecaVO</code> que será gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ConfiguracaoBibliotecaVO obj, UsuarioVO usuario) throws Exception {
		try {
			incluir(getIdEntidade(), true, usuario);
			verificarUnicidadeDePadrao(obj);
			obj.realizarUpperCaseDados();
			final String sql = "INSERT INTO ConfiguracaoBiblioteca( prazoEmpresProfessor, valorMultaDiaProfessor, prazoEmpresAluno, valorMultaDiaAluno, prazoEmpresFuncionario, valorMultaDiaFuncionario, " + " numeromaximoexemplaresaluno, numeromaximoexemplaresprofessor, numeromaximoexemplaresfuncionario, numeromaximolivrosreservados, permitereserva, numerorenovacoesaluno, numerorenovacoesprofessor, " + " numerorenovacoesfuncionario, valorcobrarressarcimento, prazoValidadeReservaCatalogosDisponiveis, emprestimorenovacaocomdebitos, padrao, nome, percentualExemplaresParaConsulta, " + " prazoValidadeReservaCatalogosIndisponiveis, textoPadraoEmprestimo, textoPadraoDevolucao, notificarAlunoUmDiaAntesVencimentoPrazo, notificarAlunoUmDiaAntesAgendaReservaLivro, " + " numeroDiasAtrazoEmprestimoEnviarPrimeiraNotificacao, numeroDiasAtrazoEmprestimoEnviarSegundaNotificacao, numeroDiasAtrazoEmprestimoEnviarTerceiraNotificacao, numeroMaximoDiasEmprestimoAtrazadoEnviarNotificacaoGestores, "
					+ " frequenciaNotificacaoGestoresEnum, grupoDestinatariosNotificacao, tipoClassificacao, qtdeDiaVencimentoMultaAluno, qtdeDiaVencimentoMultaProfessor, qtdeDiaVencimentoMultaFuncionario, funcionarioPadraoEnvioMensagem, " + " solicitarSenhaRealizarEmprestimo, naoRenovarExemplarIndisponivel, utilizarApenasDiasUteisEmprestimo, utilizarDiasUteisCalcularMulta, " + " quantidadeRenovacaoPermitidaVisaoAluno, quantidadeRenovacaoPermitidaVisaoProfessor, quantidadeRenovacaoPermitidaVisaoCoordenador, permiteRenovarExemplarEmAtrasoVisaoProfessor, " + " permiteRenovarExemplarEmAtrasoVisaoAluno, permiteRenovarExemplarEmAtrasoVisaoCoordenador, " + " prazoEmprestimoAlunoFinalDeSemana, valorMultaEmprestimoAlunoFinalDeSemana, prazoEmprestimoProfessorFinalDeSemana, valorMultaEmprestimoProfessorFinalDeSemana, prazoEmprestimoFuncionarioFinalDeSemana, valorMultaEmprestimoFuncionarioFinalDeSemana, "
					+ " textoPadraoUltimaRenovacao, liberaEmprestimoMaisDeUmExemplarMesmoCatalogo, liberaDevolucaoExemplarOutraBiblioteca, permiteRealizarReservaCatalogoDisponivel, " + " qtdeDiasAntesDevolucaoPermitirRenovarExemplarVisaoAluno, qtdeDiasAntesDevolucaoPermitirRenovarExemplarVisaoProfessor, qtdeDiasAntesDevolucaoPermitirRenovarExemplarVisaoCoordenador, quantidadeDiasAntesNotificarPrazoDevolucao, validarMultaOutraBiblioteca, validarExemplarAtrasadoOutraBiblioteca, " + "considerarEmprestimoOutraBibliotecaNrMaximoExemplaresEmprestado, "
					+ " permiteRealizarEmprestimoporHoraAluno, limiteMaximoHorasEmprestimoAluno, valorMultaEmprestimoPorHoraAluno, permiteRealizarEmprestimoporHoraProfessor, limiteMaximoHorasEmprestimoProfessor, valorMultaEmprestimoPorHoraProfessor, permiteRealizarEmprestimoporHoraFuncionario, limiteMaximoHorasEmprestimoFuncionario, valorMultaEmprestimoPorHoraFuncionario, textoPadraoReservaCatalogo, "
					+ " gerarBloqueioPorAtrasoAluno, gerarBloqueioPorAtrasoProfessor, gerarBloqueioPorAtrasoCoordenador, gerarBloqueioPorAtrasoFuncionario, "
					+ " quantidadeDiasGerarBloqueioPorAtrasoAluno, quantidadeDiasGerarBloqueioPorAtrasoProfessor, quantidadeDiasGerarBloqueioPorAtrasoCoordenador, quantidadeDiasGerarBloqueioPorAtrasoFuncionario, "
					+ " regraAplicacaoBloqueioAluno, regraAplicacaoBloqueioProfessor, regraAplicacaoBloqueioCoordenador, regraAplicacaoBloqueioFuncionario, considerarSabadoDiaUtil, considerarDomingoDiaUtil, "
					+ " prazoEmpresVisitante, qtdeDiaVencimentoMultaVisitante, valorMultaDiaVisitante, numeroMaximoExemplaresVisitante, "
					+ " numeroRenovacoesVisitante, consEmpresOutraBibliNrMaximoExemplaresEmprestadoVisitante, prazoEmprestimoVisitanteFinalDeSemana, "
				    + " valorMultaEmprestimoVisitanteFinalDeSemana, permiteRealizarEmprestimoporHoraVisitante, limiteMaximoHorasEmprestimoVisitante, "
				    + " valorMultaEmprestimoPorHoraVisitante, gerarBloqueioPorAtrasoVisitante, regraAplicacaoBloqueioVisitante, possuiIntegracaoMinhaBiblioteca, chaveAutenticacaoMinhaBiblioteca , "
				    + " tamanhoCodigoBarra, permitirRenovarMatriculaDoAlunoQuandoPossuirBloqueioBiblioteca ,possuiIntegracaoEbsco , hostEbsco , usuarioEbsco , senhaEbsco, possuiIntegracaoLexMagister, chaveAutenticacaoLexMagister, informacaoHead  , habilitarIntegracaoBvPearson , linkacessobvperson , chavetokenbvperson "
					+ ")" + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
					+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
					+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,? , ? ,? , ?, ?, ?, ? ,? ,?"
					+ ") returning codigo"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario); // 40
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setInt(1, obj.getPrazoEmpresProfessor().intValue());
					sqlInserir.setDouble(2, obj.getValorMultaDiaProfessor().doubleValue());
					sqlInserir.setInt(3, obj.getPrazoEmpresAluno().intValue());
					sqlInserir.setDouble(4, obj.getValorMultaDiaAluno().doubleValue());
					sqlInserir.setInt(5, obj.getPrazoEmpresFuncionario().intValue());
					sqlInserir.setDouble(6, obj.getValorMultaDiaFuncionario().doubleValue());
					sqlInserir.setInt(7, obj.getNumeroMaximoExemplaresAluno());
					sqlInserir.setInt(8, obj.getNumeroMaximoExemplaresProfessor());
					sqlInserir.setInt(9, obj.getNumeroMaximoExemplaresFuncionario());
					sqlInserir.setInt(10, obj.getNumeroMaximoLivrosReservados());
					sqlInserir.setBoolean(11, obj.getPermiteReserva());
					sqlInserir.setInt(12, obj.getNumeroRenovacoesAluno());
					sqlInserir.setInt(13, obj.getNumeroRenovacoesProfessor());
					sqlInserir.setInt(14, obj.getNumeroRenovacoesFuncionario());
					sqlInserir.setDouble(15, obj.getValorCobrarRessarcimento().doubleValue());
					sqlInserir.setInt(16, obj.getPrazoValidadeReservaCatalogosDisponiveis());
					sqlInserir.setBoolean(17, obj.getEmprestimoRenovacaoComDebitos());
					sqlInserir.setBoolean(18, obj.getPadrao());
					sqlInserir.setString(19, obj.getNome());
					sqlInserir.setInt(20, obj.getPercentualExemplaresParaConsulta());
					sqlInserir.setInt(21, obj.getPrazoValidadeReservaCatalogosIndisponiveis());
					sqlInserir.setString(22, obj.getTextoPadraoEmprestimo());
					sqlInserir.setString(23, obj.getTextoPadraoDevolucao());
					sqlInserir.setBoolean(24, obj.getNotificarAlunoUmDiaAntesVencimentoPrazo());
					sqlInserir.setBoolean(25, obj.getNotificarAlunoUmDiaAntesAgendaReservaLivro());
					if (obj.getNumeroDiasAtrazoEmprestimoEnviarPrimeiraNotificacao() == null) {
						sqlInserir.setNull(26, 0);
					} else {
						sqlInserir.setInt(26, obj.getNumeroDiasAtrazoEmprestimoEnviarPrimeiraNotificacao());
					}
					if (obj.getNumeroDiasAtrazoEmprestimoEnviarSegundaNotificacao() == null) {
						sqlInserir.setNull(27, 0);
					} else {
						sqlInserir.setInt(27, obj.getNumeroDiasAtrazoEmprestimoEnviarSegundaNotificacao());
					}
					if (obj.getNumeroDiasAtrazoEmprestimoEnviarTerceiraNotificacao() == null) {
						sqlInserir.setNull(28, 0);
					} else {
						sqlInserir.setInt(28, obj.getNumeroDiasAtrazoEmprestimoEnviarTerceiraNotificacao());
					}
					if (obj.getNumeroMaximoDiasEmprestimoAtrazadoEnviarNotificacaoGestores() == null) {
						sqlInserir.setNull(29, 0);
					} else {
						sqlInserir.setInt(29, obj.getNumeroMaximoDiasEmprestimoAtrazadoEnviarNotificacaoGestores());
					}
					sqlInserir.setString(30, obj.getFrequenciaNotificacaoGestoresEnum().toString());
					if (obj.getGrupoDestinatariosNotificacao().getCodigo() != 0) {
						sqlInserir.setInt(31, obj.getGrupoDestinatariosNotificacao().getCodigo().intValue());
					} else {
						sqlInserir.setNull(31, 0);
					}
					sqlInserir.setString(32, obj.getTipoClassificacao().toString());
					sqlInserir.setInt(33, obj.getQtdeDiaVencimentoMultaAluno());
					sqlInserir.setInt(34, obj.getQtdeDiaVencimentoMultaProfessor());
					sqlInserir.setInt(35, obj.getQtdeDiaVencimentoMultaFuncionario());
					if (obj.getFuncionarioPadraoEnvioMensagem() == null) {
						sqlInserir.setNull(36, 0);
					} else {
						sqlInserir.setInt(36, obj.getFuncionarioPadraoEnvioMensagem().getCodigo().intValue());
					}

					sqlInserir.setBoolean(37, obj.getSolicitarSenhaRealizarEmprestimo());
					sqlInserir.setBoolean(38, obj.getNaoRenovarExemplarIndisponivel());
					sqlInserir.setBoolean(39, obj.getUtilizarApenasDiasUteisEmprestimo());
					sqlInserir.setBoolean(40, obj.getUtilizarDiasUteisCalcularMulta());

					sqlInserir.setInt(41, obj.getQuantidadeRenovacaoPermitidaVisaoAluno());
					sqlInserir.setInt(42, obj.getQuantidadeRenovacaoPermitidaVisaoProfessor());
					sqlInserir.setInt(43, obj.getQuantidadeRenovacaoPermitidaVisaoCoordenador());

					sqlInserir.setBoolean(44, obj.getPermiteRenovarExemplarEmAtrasoVisaoProfessor());
					sqlInserir.setBoolean(45, obj.getPermiteRenovarExemplarEmAtrasoVisaoAluno());
					sqlInserir.setBoolean(46, obj.getPermiteRenovarExemplarEmAtrasoVisaoCoordenador());

					sqlInserir.setInt(47, obj.getPrazoEmprestimoAlunoFinalDeSemana());
					sqlInserir.setDouble(48, obj.getValorMultaEmprestimoAlunoFinalDeSemana());

					sqlInserir.setInt(49, obj.getPrazoEmprestimoProfessorFinalDeSemana());
					sqlInserir.setDouble(50, obj.getValorMultaEmprestimoProfessorFinalDeSemana());

					sqlInserir.setInt(51, obj.getPrazoEmprestimoFuncionarioFinalDeSemana());
					sqlInserir.setDouble(52, obj.getValorMultaEmprestimoFuncionarioFinalDeSemana());

					sqlInserir.setString(53, obj.getTextoPadraoUltimaRenovacao());
					sqlInserir.setBoolean(54, obj.getLiberaEmprestimoMaisDeUmExemplarMesmoCatalogo());
					sqlInserir.setBoolean(55, obj.getLiberaDevolucaoExemplarOutraBiblioteca());
					sqlInserir.setBoolean(56, obj.getPermiteRealizarReservaCatalogoDisponivel());

					sqlInserir.setInt(57, obj.getQuantidadeDiasAntesDevolucaoPermitirRenovarExemplarVisaoAluno());
					sqlInserir.setInt(58, obj.getQuantidadeDiasAntesDevolucaoPermitirRenovarExemplarVisaoProfessor());
					sqlInserir.setInt(59, obj.getQuantidadeDiasAntesDevolucaoPermitirRenovarExemplarVisaoCoordenador());
					sqlInserir.setInt(60, obj.getQuantidadeDiasAntesNotificarPrazoDevolucao());
					sqlInserir.setBoolean(61, obj.getValidarMultaOutraBiblioteca());
					sqlInserir.setBoolean(62, obj.getValidarExemplarAtrasadoOutraBiblioteca());
					sqlInserir.setBoolean(63, obj.getConsiderarEmprestimoOutraBibliotecaNrMaximoExemplaresEmprestado());

					sqlInserir.setBoolean(64, obj.getPermiteRealizarEmprestimoporHoraAluno());
					sqlInserir.setInt(65, obj.getLimiteMaximoHorasEmprestimoAluno());
					sqlInserir.setDouble(66, obj.getValorMultaEmprestimoPorHoraAluno());
					sqlInserir.setBoolean(67, obj.getPermiteRealizarEmprestimoporHoraProfessor());
					sqlInserir.setInt(68, obj.getLimiteMaximoHorasEmprestimoProfessor());
					sqlInserir.setDouble(69, obj.getValorMultaEmprestimoPorHoraProfessor());
					sqlInserir.setBoolean(70, obj.getPermiteRealizarEmprestimoporHoraFuncionario());
					sqlInserir.setInt(71, obj.getLimiteMaximoHorasEmprestimoFuncionario());
					sqlInserir.setDouble(72, obj.getValorMultaEmprestimoPorHoraFuncionario());
					sqlInserir.setString(73, obj.getTextoPadraoReservaCatalogo());
					sqlInserir.setBoolean(74, obj.getGerarBloqueioPorAtrasoAluno());
					sqlInserir.setBoolean(75, obj.getGerarBloqueioPorAtrasoProfessor());
					sqlInserir.setBoolean(76, obj.getGerarBloqueioPorAtrasoCoordenador());
					sqlInserir.setBoolean(77, obj.getGerarBloqueioPorAtrasoFuncionario());
					sqlInserir.setInt(78, obj.getQuantidadeDiasGerarBloqueioPorAtrasoAluno());
					sqlInserir.setInt(79, obj.getQuantidadeDiasGerarBloqueioPorAtrasoProfessor());
					sqlInserir.setInt(80, obj.getQuantidadeDiasGerarBloqueioPorAtrasoCoordenador());
					sqlInserir.setInt(81, obj.getQuantidadeDiasGerarBloqueioPorAtrasoFuncionario());
					sqlInserir.setString(82, obj.getRegraAplicacaoBloqueioAluno().name());
					sqlInserir.setString(83, obj.getRegraAplicacaoBloqueioProfessor().name());
					sqlInserir.setString(84, obj.getRegraAplicacaoBloqueioCoordenador().name());
					sqlInserir.setString(85, obj.getRegraAplicacaoBloqueioFuncionario().name());
					sqlInserir.setBoolean(86, obj.getConsiderarSabadoDiaUtil());
					sqlInserir.setBoolean(87, obj.getConsiderarDomingoDiaUtil());

					sqlInserir.setInt(88, obj.getPrazoEmpresVisitante());
					sqlInserir.setInt(89, obj.getQtdeDiaVencimentoMultaVisitante());
					sqlInserir.setDouble(90, obj.getValorMultaDiaVisitante());
					sqlInserir.setInt(91, obj.getNumeroMaximoExemplaresVisitante());
					sqlInserir.setInt(92, obj.getNumeroRenovacoesVisitante());
					sqlInserir.setBoolean(93, obj.getConsiderarEmprestimoOutraBibliotecaNrMaximoExemplaresEmprestadoVisitante());
					sqlInserir.setInt(94, obj.getPrazoEmprestimoVisitanteFinalDeSemana());
					sqlInserir.setDouble(95, obj.getValorMultaEmprestimoVisitanteFinalDeSemana());
					sqlInserir.setBoolean(96, obj.getPermiteRealizarEmprestimoporHoraVisitante());
					sqlInserir.setInt(97, obj.getLimiteMaximoHorasEmprestimoVisitante());
					sqlInserir.setDouble(98, obj.getValorMultaEmprestimoPorHoraVisitante());
					sqlInserir.setBoolean(99, obj.getGerarBloqueioPorAtrasoVisitante());
					sqlInserir.setString(100, obj.getRegraAplicacaoBloqueioVisitante().name());
					sqlInserir.setBoolean(101, obj.getPossuiIntegracaoMinhaBiblioteca());
					sqlInserir.setString(102, obj.getChaveAutenticacaoMinhaBiblioteca());
					sqlInserir.setInt(103, obj.getTamanhoCodigoBarra());
					sqlInserir.setBoolean(104, obj.getPermiteRenovarMatriculaQuandoPossuiBloqueioBiblioteca());
					sqlInserir.setBoolean(105, obj.getPossuiIntegracaoEbsco());
					sqlInserir.setString(106, obj.getHostEbsco());
			        sqlInserir.setString(107, obj.getUsuarioEbsco());
			        sqlInserir.setString(108, obj.getSenhaEbsco());		
			        sqlInserir.setBoolean(109, obj.getPossuiIntegracaoLexMagister());
			        sqlInserir.setString(110, obj.getChaveAutenticacaoLexMagister());
			        sqlInserir.setString(111, obj.getInformacaoHead());		
			        sqlInserir.setBoolean(112, obj.getHabilitarIntegracaoBvPearson());
			        sqlInserir.setString(113, obj.getLinkAcessoBVPerson());	
			        sqlInserir.setString(114, obj.getChaveTokenBVPerson());	
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
			getFacadeFactory().getBibliotecaExternaFacade().alterarBibliotecasExternas(obj.getCodigo(), obj.getListaBibliotecaExternaVO(), usuario);
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(true);
			throw e;
		}
	}

	public void verificarUnicidadeDePadrao(ConfiguracaoBibliotecaVO configuracaoBibliotecaVO) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select codigo from configuracaobiblioteca where padrao = true");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		tabelaResultado.next();

		if (tabelaResultado.getRow() == 0) {
			if (!configuracaoBibliotecaVO.getPadrao()) {
				throw new Exception("Não há configuração padrão da biblioteca salva no sistema, selecione a opção (PADRÃO)");
			}
		} else {
			Integer codigoConfBiblioteca = tabelaResultado.getInt("codigo");
			if (configuracaoBibliotecaVO.getPadrao()) {
				if (!configuracaoBibliotecaVO.getCodigo().equals(codigoConfBiblioteca)) {
					throw new Exception("O sistema pode ter apenas uma configuração padrão para a biblioteca, desmarque a opção (PADRAO)");
				}
			} else {
				if (configuracaoBibliotecaVO.getCodigo().equals(codigoConfBiblioteca)) {
					throw new Exception("Esta é a configuração padrão para a biblioteca, não pode ser desabilitada a opção (PADRAO)");
				}
			}
		}
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe <code>ConfiguracaoBibliotecaVO</code>. Sempre utiliza a chave primária
	 * da classe como atributo para localização do registro a ser alterado. Primeiramente valida os dados (<code>validarDados</code>) do objeto.
	 * Verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação
	 * <code>alterar</code> da superclasse.
	 *
	 * @param obj
	 *            Objeto da classe <code>ConfiguracaoBibliotecaVO</code> que será alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ConfiguracaoBibliotecaVO obj, UsuarioVO usuario) throws Exception {
		try {
			alterar(getIdEntidade(), true, usuario);
			ConfiguracaoBibliotecaVO.validarDados(obj);
			verificarUnicidadeDePadrao(obj);
			obj.realizarUpperCaseDados();
			final StringBuilder sql = new StringBuilder("UPDATE ConfiguracaoBiblioteca set prazoEmpresProfessor=?, valorMultaDiaProfessor=?, prazoEmpresAluno=?, valorMultaDiaAluno=?, prazoEmpresFuncionario=?, valorMultaDiaFuncionario=?, ");
			sql.append(" numeromaximoexemplaresaluno=?, numeromaximoexemplaresprofessor=?, numeromaximoexemplaresfuncionario=?, numeromaximolivrosreservados=?, permitereserva=?, numerorenovacoesaluno=?, numerorenovacoesprofessor=?, ");
			sql.append(" numerorenovacoesfuncionario=?, valorcobrarressarcimento=?, prazoValidadeReservaCatalogosdisponiveis=?, emprestimorenovacaocomdebitos=?, padrao=?, nome=?, percentualExemplaresParaConsulta=?, ");
			sql.append(" prazoValidadeReservaCatalogosIndisponiveis=?, textoPadraoEmprestimo=?, textoPadraoDevolucao=?, notificarAlunoUmDiaAntesVencimentoPrazo=?, notificarAlunoUmDiaAntesAgendaReservaLivro=?, ");
			sql.append(" numeroDiasAtrazoEmprestimoEnviarPrimeiraNotificacao=?, numeroDiasAtrazoEmprestimoEnviarSegundaNotificacao=?, numeroDiasAtrazoEmprestimoEnviarTerceiraNotificacao=?, ");
			sql.append(" numeroMaximoDiasEmprestimoAtrazadoEnviarNotificacaoGestores=?, frequenciaNotificacaoGestoresEnum=?, grupoDestinatariosNotificacao=?, tipoClassificacao=? , qtdeDiaVencimentoMultaAluno = ?, ");
			sql.append(" qtdeDiaVencimentoMultaProfessor = ?, qtdeDiaVencimentoMultaFuncionario = ?, funcionarioPadraoEnvioMensagem=?, solicitarSenhaRealizarEmprestimo = ?, naoRenovarExemplarIndisponivel= ?, ");
			sql.append(" utilizarApenasDiasUteisEmprestimo=?, utilizarDiasUteisCalcularMulta=?, quantidadeRenovacaoPermitidaVisaoAluno=?, quantidadeRenovacaoPermitidaVisaoProfessor=?, ");
			sql.append(" quantidadeRenovacaoPermitidaVisaoCoordenador=?, permiteRenovarExemplarEmAtrasoVisaoProfessor=?, permiteRenovarExemplarEmAtrasoVisaoAluno=?, permiteRenovarExemplarEmAtrasoVisaoCoordenador=?, ");
			sql.append(" prazoEmprestimoAlunoFinalDeSemana=?, valorMultaEmprestimoAlunoFinalDeSemana=?, prazoEmprestimoProfessorFinalDeSemana=?, valorMultaEmprestimoProfessorFinalDeSemana=?, ");
			sql.append(" prazoEmprestimoFuncionarioFinalDeSemana=?, valorMultaEmprestimoFuncionarioFinalDeSemana=?, textoPadraoUltimaRenovacao=?, liberaEmprestimoMaisDeUmExemplarMesmoCatalogo=?, liberaDevolucaoExemplarOutraBiblioteca=?, permiteRealizarReservaCatalogoDisponivel=?, ");
			sql.append(" qtdeDiasAntesDevolucaoPermitirRenovarExemplarVisaoAluno = ?, qtdeDiasAntesDevolucaoPermitirRenovarExemplarVisaoProfessor = ?, qtdeDiasAntesDevolucaoPermitirRenovarExemplarVisaoCoordenador = ?, quantidadeDiasAntesNotificarPrazoDevolucao=?, validarMultaOutraBiblioteca=?, validarExemplarAtrasadoOutraBiblioteca=?, ");
			sql.append(" considerarEmprestimoOutraBibliotecaNrMaximoExemplaresEmprestado=?, permiteRealizarEmprestimoporHoraAluno=?, limiteMaximoHorasEmprestimoAluno=?, valorMultaEmprestimoPorHoraAluno=?, permiteRealizarEmprestimoporHoraProfessor=?, limiteMaximoHorasEmprestimoProfessor=?, valorMultaEmprestimoPorHoraProfessor=?, permiteRealizarEmprestimoporHoraFuncionario=?, limiteMaximoHorasEmprestimoFuncionario=?, valorMultaEmprestimoPorHoraFuncionario=?, textoPadraoReservaCatalogo=?, ");
			sql.append(" gerarBloqueioPorAtrasoAluno=?, gerarBloqueioPorAtrasoProfessor=?, gerarBloqueioPorAtrasoCoordenador=?, gerarBloqueioPorAtrasoFuncionario=?, ");
			sql.append(" quantidadeDiasGerarBloqueioPorAtrasoAluno=?, quantidadeDiasGerarBloqueioPorAtrasoProfessor=?, quantidadeDiasGerarBloqueioPorAtrasoCoordenador=?, quantidadeDiasGerarBloqueioPorAtrasoFuncionario=?, ");
			sql.append(" regraAplicacaoBloqueioAluno=?, regraAplicacaoBloqueioProfessor=?, regraAplicacaoBloqueioCoordenador=?, regraAplicacaoBloqueioFuncionario=?, considerarSabadoDiaUtil = ?, considerarDomingoDiaUtil = ?, ");			
			sql.append(" prazoEmpresVisitante = ?, qtdeDiaVencimentoMultaVisitante = ?, valorMultaDiaVisitante = ?, numeroMaximoExemplaresVisitante = ?, ");
			sql.append(" numeroRenovacoesVisitante = ?, consEmpresOutraBibliNrMaximoExemplaresEmprestadoVisitante = ?, prazoEmprestimoVisitanteFinalDeSemana = ?, ");
			sql.append(" valorMultaEmprestimoVisitanteFinalDeSemana = ?, permiteRealizarEmprestimoporHoraVisitante = ?, limiteMaximoHorasEmprestimoVisitante = ?, ");
			sql.append(" valorMultaEmprestimoPorHoraVisitante = ?, gerarBloqueioPorAtrasoVisitante = ?, regraAplicacaoBloqueioVisitante = ?, possuiIntegracaoMinhaBiblioteca=?, chaveAutenticacaoMinhaBiblioteca=? , tamanhoCodigoBarra=?, permitirRenovarMatriculaDoAlunoQuandoPossuirBloqueioBiblioteca=? , possuiIntegracaoEbsco=? , hostEbsco=? , usuarioEbsco=? , senhaEbsco=?, possuiIntegracaoLexMagister = ?, chaveAutenticacaoLexMagister = ?, informacaoHead = ? , habilitarIntegracaoBvPearson= ? ,  linkacessobvperson=? , chavetokenbvperson=? ");			
			sql.append(" WHERE (codigo = ?) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));

			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
					sqlAlterar.setInt(1, obj.getPrazoEmpresProfessor().intValue());
					sqlAlterar.setDouble(2, obj.getValorMultaDiaProfessor().doubleValue());
					sqlAlterar.setInt(3, obj.getPrazoEmpresAluno().intValue());
					sqlAlterar.setDouble(4, obj.getValorMultaDiaAluno().doubleValue());
					sqlAlterar.setInt(5, obj.getPrazoEmpresFuncionario().intValue());
					sqlAlterar.setDouble(6, obj.getValorMultaDiaFuncionario().doubleValue());
					sqlAlterar.setInt(7, obj.getNumeroMaximoExemplaresAluno());
					sqlAlterar.setInt(8, obj.getNumeroMaximoExemplaresProfessor());
					sqlAlterar.setInt(9, obj.getNumeroMaximoExemplaresFuncionario());
					sqlAlterar.setInt(10, obj.getNumeroMaximoLivrosReservados());
					sqlAlterar.setBoolean(11, obj.getPermiteReserva());
					sqlAlterar.setInt(12, obj.getNumeroRenovacoesAluno());
					sqlAlterar.setInt(13, obj.getNumeroRenovacoesProfessor());
					sqlAlterar.setInt(14, obj.getNumeroRenovacoesFuncionario());
					sqlAlterar.setDouble(15, obj.getValorCobrarRessarcimento().doubleValue());
					sqlAlterar.setInt(16, obj.getPrazoValidadeReservaCatalogosDisponiveis());
					sqlAlterar.setBoolean(17, obj.getEmprestimoRenovacaoComDebitos());
					sqlAlterar.setBoolean(18, obj.getPadrao());
					sqlAlterar.setString(19, obj.getNome());
					sqlAlterar.setInt(20, obj.getPercentualExemplaresParaConsulta());
					sqlAlterar.setInt(21, obj.getPrazoValidadeReservaCatalogosIndisponiveis());
					sqlAlterar.setString(22, obj.getTextoPadraoEmprestimo());
					sqlAlterar.setString(23, obj.getTextoPadraoDevolucao());
					sqlAlterar.setBoolean(24, obj.getNotificarAlunoUmDiaAntesVencimentoPrazo());
					sqlAlterar.setBoolean(25, obj.getNotificarAlunoUmDiaAntesAgendaReservaLivro());
					if (obj.getNumeroDiasAtrazoEmprestimoEnviarPrimeiraNotificacao() == null) {
						sqlAlterar.setNull(26, 0);
					} else {
						sqlAlterar.setInt(26, obj.getNumeroDiasAtrazoEmprestimoEnviarPrimeiraNotificacao());
					}
					if (obj.getNumeroDiasAtrazoEmprestimoEnviarSegundaNotificacao() == null) {
						sqlAlterar.setNull(27, 0);
					} else {
						sqlAlterar.setInt(27, obj.getNumeroDiasAtrazoEmprestimoEnviarSegundaNotificacao());
					}
					if (obj.getNumeroDiasAtrazoEmprestimoEnviarTerceiraNotificacao() == null) {
						sqlAlterar.setNull(28, 0);
					} else {
						sqlAlterar.setInt(28, obj.getNumeroDiasAtrazoEmprestimoEnviarTerceiraNotificacao());
					}
					if (obj.getNumeroMaximoDiasEmprestimoAtrazadoEnviarNotificacaoGestores() == null) {
						sqlAlterar.setNull(29, 0);
					} else {
						sqlAlterar.setInt(29, obj.getNumeroMaximoDiasEmprestimoAtrazadoEnviarNotificacaoGestores());
					}
					sqlAlterar.setString(30, obj.getFrequenciaNotificacaoGestoresEnum().toString());
					if (obj.getGrupoDestinatariosNotificacao().getCodigo() != 0) {
						sqlAlterar.setInt(31, obj.getGrupoDestinatariosNotificacao().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(31, 0);
					}
					sqlAlterar.setString(32, obj.getTipoClassificacao().toString());
					sqlAlterar.setInt(33, obj.getQtdeDiaVencimentoMultaAluno());
					sqlAlterar.setInt(34, obj.getQtdeDiaVencimentoMultaProfessor());
					sqlAlterar.setInt(35, obj.getQtdeDiaVencimentoMultaFuncionario());
					if (obj.getFuncionarioPadraoEnvioMensagem() == null) {
						sqlAlterar.setNull(36, 0);
					} else {
						sqlAlterar.setInt(36, obj.getFuncionarioPadraoEnvioMensagem().getCodigo().intValue());
					}
					sqlAlterar.setBoolean(37, obj.getSolicitarSenhaRealizarEmprestimo());
					sqlAlterar.setBoolean(38, obj.getNaoRenovarExemplarIndisponivel());
					sqlAlterar.setBoolean(39, obj.getUtilizarApenasDiasUteisEmprestimo());
					sqlAlterar.setBoolean(40, obj.getUtilizarDiasUteisCalcularMulta());

					sqlAlterar.setInt(41, obj.getQuantidadeRenovacaoPermitidaVisaoAluno());
					sqlAlterar.setInt(42, obj.getQuantidadeRenovacaoPermitidaVisaoProfessor());
					sqlAlterar.setInt(43, obj.getQuantidadeRenovacaoPermitidaVisaoCoordenador());

					sqlAlterar.setBoolean(44, obj.getPermiteRenovarExemplarEmAtrasoVisaoProfessor());
					sqlAlterar.setBoolean(45, obj.getPermiteRenovarExemplarEmAtrasoVisaoAluno());
					sqlAlterar.setBoolean(46, obj.getPermiteRenovarExemplarEmAtrasoVisaoCoordenador());

					sqlAlterar.setInt(47, obj.getPrazoEmprestimoAlunoFinalDeSemana());
					sqlAlterar.setDouble(48, obj.getValorMultaEmprestimoAlunoFinalDeSemana());

					sqlAlterar.setInt(49, obj.getPrazoEmprestimoProfessorFinalDeSemana());
					sqlAlterar.setDouble(50, obj.getValorMultaEmprestimoProfessorFinalDeSemana());

					sqlAlterar.setInt(51, obj.getPrazoEmprestimoFuncionarioFinalDeSemana());
					sqlAlterar.setDouble(52, obj.getValorMultaEmprestimoFuncionarioFinalDeSemana());

					sqlAlterar.setString(53, obj.getTextoPadraoUltimaRenovacao());
					sqlAlterar.setBoolean(54, obj.getLiberaEmprestimoMaisDeUmExemplarMesmoCatalogo());

					sqlAlterar.setBoolean(55, obj.getLiberaDevolucaoExemplarOutraBiblioteca());
					sqlAlterar.setBoolean(56, obj.getPermiteRealizarReservaCatalogoDisponivel());

					sqlAlterar.setInt(57, obj.getQuantidadeDiasAntesDevolucaoPermitirRenovarExemplarVisaoAluno());
					sqlAlterar.setInt(58, obj.getQuantidadeDiasAntesDevolucaoPermitirRenovarExemplarVisaoProfessor());
					sqlAlterar.setInt(59, obj.getQuantidadeDiasAntesDevolucaoPermitirRenovarExemplarVisaoCoordenador());
					sqlAlterar.setInt(60, obj.getQuantidadeDiasAntesNotificarPrazoDevolucao());
					sqlAlterar.setBoolean(61, obj.getValidarMultaOutraBiblioteca());
					sqlAlterar.setBoolean(62, obj.getValidarExemplarAtrasadoOutraBiblioteca());
					sqlAlterar.setBoolean(63, obj.getConsiderarEmprestimoOutraBibliotecaNrMaximoExemplaresEmprestado());
					sqlAlterar.setBoolean(64, obj.getPermiteRealizarEmprestimoporHoraAluno());
					sqlAlterar.setInt(65, obj.getLimiteMaximoHorasEmprestimoAluno());
					sqlAlterar.setDouble(66, obj.getValorMultaEmprestimoPorHoraAluno());
					sqlAlterar.setBoolean(67, obj.getPermiteRealizarEmprestimoporHoraProfessor());
					sqlAlterar.setInt(68, obj.getLimiteMaximoHorasEmprestimoProfessor());
					sqlAlterar.setDouble(69, obj.getValorMultaEmprestimoPorHoraProfessor());
					sqlAlterar.setBoolean(70, obj.getPermiteRealizarEmprestimoporHoraFuncionario());
					sqlAlterar.setInt(71, obj.getLimiteMaximoHorasEmprestimoFuncionario());
					sqlAlterar.setDouble(72, obj.getValorMultaEmprestimoPorHoraFuncionario());
					sqlAlterar.setString(73, obj.getTextoPadraoReservaCatalogo());
					sqlAlterar.setBoolean(74, obj.getGerarBloqueioPorAtrasoAluno());
					sqlAlterar.setBoolean(75, obj.getGerarBloqueioPorAtrasoProfessor());
					sqlAlterar.setBoolean(76, obj.getGerarBloqueioPorAtrasoCoordenador());
					sqlAlterar.setBoolean(77, obj.getGerarBloqueioPorAtrasoFuncionario());
					sqlAlterar.setInt(78, obj.getQuantidadeDiasGerarBloqueioPorAtrasoAluno());
					sqlAlterar.setInt(79, obj.getQuantidadeDiasGerarBloqueioPorAtrasoProfessor());
					sqlAlterar.setInt(80, obj.getQuantidadeDiasGerarBloqueioPorAtrasoCoordenador());
					sqlAlterar.setInt(81, obj.getQuantidadeDiasGerarBloqueioPorAtrasoFuncionario());
					sqlAlterar.setString(82, obj.getRegraAplicacaoBloqueioAluno().name());
					sqlAlterar.setString(83, obj.getRegraAplicacaoBloqueioProfessor().name());
					sqlAlterar.setString(84, obj.getRegraAplicacaoBloqueioCoordenador().name());
					sqlAlterar.setString(85, obj.getRegraAplicacaoBloqueioFuncionario().name());
					sqlAlterar.setBoolean(86, obj.getConsiderarSabadoDiaUtil());
					sqlAlterar.setBoolean(87, obj.getConsiderarDomingoDiaUtil());	
					
					sqlAlterar.setInt(88, obj.getPrazoEmpresVisitante());
					sqlAlterar.setInt(89, obj.getQtdeDiaVencimentoMultaVisitante());
					sqlAlterar.setDouble(90, obj.getValorMultaDiaVisitante());
					sqlAlterar.setInt(91, obj.getNumeroMaximoExemplaresVisitante());
					sqlAlterar.setInt(92, obj.getNumeroRenovacoesVisitante());
					sqlAlterar.setBoolean(93, obj.getConsiderarEmprestimoOutraBibliotecaNrMaximoExemplaresEmprestadoVisitante());
					sqlAlterar.setInt(94, obj.getPrazoEmprestimoVisitanteFinalDeSemana());
					sqlAlterar.setDouble(95, obj.getValorMultaEmprestimoVisitanteFinalDeSemana());
					sqlAlterar.setBoolean(96, obj.getPermiteRealizarEmprestimoporHoraVisitante());
					sqlAlterar.setInt(97, obj.getLimiteMaximoHorasEmprestimoVisitante());
					sqlAlterar.setDouble(98, obj.getValorMultaEmprestimoPorHoraVisitante());
					sqlAlterar.setBoolean(99, obj.getGerarBloqueioPorAtrasoVisitante());
					sqlAlterar.setString(100, obj.getRegraAplicacaoBloqueioVisitante().name());
					sqlAlterar.setBoolean(101, obj.getPossuiIntegracaoMinhaBiblioteca());
					sqlAlterar.setString(102, obj.getChaveAutenticacaoMinhaBiblioteca());
					sqlAlterar.setInt(103, obj.getTamanhoCodigoBarra());
					sqlAlterar.setBoolean(104, obj.getPermiteRenovarMatriculaQuandoPossuiBloqueioBiblioteca());
					sqlAlterar.setBoolean(105, obj.getPossuiIntegracaoEbsco());
					sqlAlterar.setString(106, obj.getHostEbsco());
					sqlAlterar.setString(107, obj.getUsuarioEbsco());
					sqlAlterar.setString(108, obj.getSenhaEbsco());
					sqlAlterar.setBoolean(109, obj.getPossuiIntegracaoLexMagister());
					sqlAlterar.setString(110, obj.getChaveAutenticacaoLexMagister());
					sqlAlterar.setString(111, obj.getInformacaoHead());		
					sqlAlterar.setBoolean(112, obj.getHabilitarIntegracaoBvPearson());
					sqlAlterar.setString(113, obj.getLinkAcessoBVPerson());	
					sqlAlterar.setString(114, obj.getChaveTokenBVPerson());	
					sqlAlterar.setInt(115, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
			getFacadeFactory().getBibliotecaExternaFacade().alterarBibliotecasExternas(obj.getCodigo(), obj.getListaBibliotecaExternaVO(), usuario);
			if(obj.getPadrao()) {
				getAplicacaoControle().removerConfiguracaoBibliotecaPadrao();
			}			
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe <code>ConfiguracaoBibliotecaVO</code>. Sempre localiza o registro a ser excluído
	 * através da chave primária da entidade. Primeiramente verifica a conexão com o banco de dados e a permissão do usuário para realizar esta
	 * operacão na entidade. Isto, através da operação <code>excluir</code> da superclasse.
	 *
	 * @param obj
	 *            Objeto da classe <code>ConfiguracaoBibliotecaVO</code> que será removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(ConfiguracaoBibliotecaVO obj, UsuarioVO usuario) throws Exception {
		try {
			excluir(getIdEntidade(), true, usuario);
			String sql = "DELETE FROM ConfiguracaoBiblioteca WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
			getFacadeFactory().getBibliotecaExternaFacade().excluirBibliotecasExternas(obj.getCodigo(), usuario);
		} catch (Exception e) {
			throw e;
		}
	}

	public ConfiguracaoBibliotecaVO consultarConfiguracaoPadrao(int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT * FROM configuracaobiblioteca WHERE padrao = true ");
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!dadosSQL.next()) {
			throw new ConsistirException("Não existe uma Configuração Biblioteca PADRÃO. Por favor, crie uma.");
		}
		return montarDados(dadosSQL, nivelMontarDados);
	}
	
	public ConfiguracaoBibliotecaVO consultarConfiguracaoMinhaBiblioteca() throws Exception {
		String sqlStr = "SELECT possuiIntegracaoMinhaBiblioteca, chaveAutenticacaoMinhaBiblioteca FROM configuracaobiblioteca WHERE padrao = true limit 1";
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (dadosSQL.next()) {
			ConfiguracaoBibliotecaVO obj = new ConfiguracaoBibliotecaVO();
			obj.setPossuiIntegracaoMinhaBiblioteca(dadosSQL.getBoolean("possuiIntegracaoMinhaBiblioteca"));
			obj.setChaveAutenticacaoMinhaBiblioteca(dadosSQL.getString("chaveAutenticacaoMinhaBiblioteca"));
			return obj;
		}
		return new ConfiguracaoBibliotecaVO();
	}
	
	
	
	public ConfiguracaoBibliotecaVO consultarConfiguracaoBibliotecaLexMagister() throws Exception {
		String sqlStr = "SELECT possuiIntegracaoLexMagister, chaveAutenticacaoLexMagister , informacaoHead   FROM configuracaobiblioteca WHERE padrao = true limit 1";
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (dadosSQL.next()) {
			ConfiguracaoBibliotecaVO obj = new ConfiguracaoBibliotecaVO();
			obj.setPossuiIntegracaoLexMagister(dadosSQL.getBoolean("possuiIntegracaoLexMagister"));
			obj.setChaveAutenticacaoLexMagister(dadosSQL.getString("chaveAutenticacaoLexMagister"));
			obj.setInformacaoHead(dadosSQL.getString("informacaoHead"));
			return obj;
		}
		return new ConfiguracaoBibliotecaVO();
	}
	
	@Override
	public ConfiguracaoBibliotecaVO consultarConfiguracaoBibliotecaBVPearson() {
		try {
		String sqlStr = "SELECT habilitarIntegracaoBvPearson , linkacessobvperson , chavetokenbvperson   FROM configuracaobiblioteca WHERE padrao = true limit 1";
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (dadosSQL.next()) {
			ConfiguracaoBibliotecaVO obj = new ConfiguracaoBibliotecaVO();
			obj.setHabilitarIntegracaoBvPearson(dadosSQL.getBoolean("habilitarIntegracaoBvPearson"));
			obj.setLinkAcessoBVPerson(dadosSQL.getString("linkacessobvperson"));
			obj.setChaveTokenBVPerson(dadosSQL.getString("chavetokenbvperson"));
			return obj;
		}
		}catch (Exception e) {
			// TODO: handle exception
		}
		return new ConfiguracaoBibliotecaVO();
	}


	public ConfiguracaoBibliotecaVO consultarConfiguracaoPorBiblioteca(Integer biblioteca, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		ConfiguracaoBibliotecaVO obj = null;
		StringBuilder sb = new StringBuilder();
		sb.append("select configuracaoBiblioteca.* from configuracaoBiblioteca ");
		sb.append(" INNER JOIN biblioteca ON biblioteca.configuracaoBiblioteca = configuracaoBiblioteca.codigo ");
		sb.append(" WHERE biblioteca.codigo = ").append(biblioteca);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (!tabelaResultado.next()) {
			return new ConfiguracaoBibliotecaVO();
		}
		obj = montarDados(tabelaResultado, nivelMontarDados);

		return obj;
	}

	public ConfiguracaoBibliotecaVO consultarConfiguracaoPadraoFuncionario(Boolean padrao, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuarioLogado) throws Exception {
		ConfiguracaoBibliotecaVO obj = null;
		String sqlStr = "SELECT * FROM configuracaoBiblioteca WHERE padrao = '" + padrao + "' ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Não existe uma Configuração Biblioteca PADRÃO. Por favor, cria uma.");
		}
		obj = montarDados(tabelaResultado, nivelMontarDados);
		obj.setFuncionarioPadraoEnvioMensagem(montarDadosFuncionario(obj.getFuncionarioPadraoEnvioMensagem().getCodigo(), controlarAcesso, usuarioLogado));
		return obj;
	}

	/**
	 * Responsável por realizar uma consulta de <code>ConfiguracaoBiblioteca</code> através do valor do atributo <code>Integer codigo</code>. Retorna
	 * os objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho
	 * de prerarar o List resultante.
	 *
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>ConfiguracaoBibliotecaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<ConfiguracaoBibliotecaVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), false, usuario);
		String sqlStr = "SELECT * FROM ConfiguracaoBiblioteca WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
	}

	/**
	 * Responsável por realizar uma consulta de <code>ConfiguracaoBiblioteca</code> através do valor do atributo <code>String valorConsulta</code>.Faz
	 * uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>ConfiguracaoBibliotecaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Override
	public List<ConfiguracaoBibliotecaVO> consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM ConfiguracaoBiblioteca WHERE upper( nome ) like('");
		sql.append(valorConsulta.toUpperCase());
		sql.append("%') order by nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados ( <code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 *
	 * @return List Contendo vários objetos da classe <code>ConfiguracaoBibliotecaVO</code> resultantes da consulta.
	 */
	public  List<ConfiguracaoBibliotecaVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
		List<ConfiguracaoBibliotecaVO> vetResultado = new ArrayList<ConfiguracaoBibliotecaVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>ConfiguracaoBibliotecaVO</code>.
	 *
	 * @return O objeto da classe <code>ConfiguracaoBibliotecaVO</code> com os dados devidamente montados.
	 */
	public  ConfiguracaoBibliotecaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados) throws Exception {
		ConfiguracaoBibliotecaVO obj = new ConfiguracaoBibliotecaVO();
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setNome(dadosSQL.getString("nome"));		
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
			return obj;
		}
		obj.setTamanhoCodigoBarra(dadosSQL.getByte("tamanhoCodigoBarra"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			return obj;
		}
		obj.setPrazoEmpresProfessor(dadosSQL.getInt("prazoEmpresProfessor"));
		obj.setValorMultaDiaProfessor(dadosSQL.getDouble("valorMultaDiaProfessor"));
		obj.setPrazoEmpresAluno(dadosSQL.getInt("prazoEmpresAluno"));
		obj.setValorMultaDiaAluno(dadosSQL.getDouble("valorMultaDiaAluno"));
		obj.setPrazoEmpresFuncionario(dadosSQL.getInt("prazoEmpresFuncionario"));
		obj.setValorMultaDiaFuncionario(dadosSQL.getDouble("valorMultaDiaFuncionario"));
		obj.setNumeroMaximoExemplaresAluno(dadosSQL.getInt("numeromaximoexemplaresaluno"));
		obj.setNumeroMaximoExemplaresProfessor(dadosSQL.getInt("numeromaximoexemplaresprofessor"));
		obj.setNumeroMaximoExemplaresFuncionario(dadosSQL.getInt("numeromaximoexemplaresfuncionario"));
		obj.setNumeroMaximoLivrosReservados(dadosSQL.getInt("numeromaximolivrosreservados"));
		obj.setPermiteReserva(dadosSQL.getBoolean("permitereserva"));
		obj.setNumeroRenovacoesAluno(dadosSQL.getInt("numerorenovacoesaluno"));
		obj.setNumeroRenovacoesProfessor(dadosSQL.getInt("numerorenovacoesprofessor"));
		obj.setNumeroRenovacoesFuncionario(dadosSQL.getInt("numerorenovacoesfuncionario"));
		obj.setValorCobrarRessarcimento(dadosSQL.getDouble("valorcobrarressarcimento"));
		obj.setPrazoValidadeReservaCatalogosDisponiveis(dadosSQL.getInt("prazoValidadeReservaCatalogosDisponiveis"));
		obj.setPrazoValidadeReservaCatalogosIndisponiveis(dadosSQL.getInt("prazoValidadeReservaCatalogosIndisponiveis"));
		obj.setEmprestimoRenovacaoComDebitos(dadosSQL.getBoolean("emprestimorenovacaocomdebitos"));
		obj.setPadrao(dadosSQL.getBoolean("padrao"));
		obj.setPercentualExemplaresParaConsulta(dadosSQL.getInt("percentualExemplaresParaConsulta"));
		obj.setTextoPadraoEmprestimo(dadosSQL.getString("textoPadraoEmprestimo"));
		obj.setTextoPadraoDevolucao(dadosSQL.getString("textoPadraoDevolucao"));
		obj.setTextoPadraoUltimaRenovacao(dadosSQL.getString("textoPadraoUltimaRenovacao"));
		obj.setNotificarAlunoUmDiaAntesVencimentoPrazo(dadosSQL.getBoolean("notificarAlunoUmDiaAntesVencimentoPrazo"));
		obj.setNotificarAlunoUmDiaAntesAgendaReservaLivro(dadosSQL.getBoolean("notificarAlunoUmDiaAntesAgendaReservaLivro"));
		obj.setNumeroDiasAtrazoEmprestimoEnviarPrimeiraNotificacao((Integer) dadosSQL.getObject("numeroDiasAtrazoEmprestimoEnviarPrimeiraNotificacao"));
		obj.setNumeroDiasAtrazoEmprestimoEnviarSegundaNotificacao((Integer) dadosSQL.getObject("numeroDiasAtrazoEmprestimoEnviarSegundaNotificacao"));
		obj.setNumeroDiasAtrazoEmprestimoEnviarTerceiraNotificacao((Integer) dadosSQL.getObject("numeroDiasAtrazoEmprestimoEnviarTerceiraNotificacao"));
		obj.setNumeroMaximoDiasEmprestimoAtrazadoEnviarNotificacaoGestores((Integer) dadosSQL.getObject("numeroMaximoDiasEmprestimoAtrazadoEnviarNotificacaoGestores"));
		obj.setQtdeDiaVencimentoMultaAluno(dadosSQL.getInt("qtdeDiaVencimentoMultaAluno"));
		obj.setQtdeDiaVencimentoMultaFuncionario(dadosSQL.getInt("qtdeDiaVencimentoMultaFuncionario"));
		obj.setQtdeDiaVencimentoMultaProfessor(dadosSQL.getInt("qtdeDiaVencimentoMultaProfessor"));
		obj.setSolicitarSenhaRealizarEmprestimo(dadosSQL.getBoolean("solicitarSenhaRealizarEmprestimo"));
		obj.setNaoRenovarExemplarIndisponivel(dadosSQL.getBoolean("naoRenovarExemplarIndisponivel"));
		obj.setUtilizarApenasDiasUteisEmprestimo(dadosSQL.getBoolean("utilizarApenasDiasUteisEmprestimo"));
		obj.setUtilizarDiasUteisCalcularMulta(dadosSQL.getBoolean("utilizarDiasUteisCalcularMulta"));
		obj.setLiberaEmprestimoMaisDeUmExemplarMesmoCatalogo(dadosSQL.getBoolean("liberaEmprestimoMaisDeUmExemplarMesmoCatalogo"));
		obj.setLiberaDevolucaoExemplarOutraBiblioteca(dadosSQL.getBoolean("liberaDevolucaoExemplarOutraBiblioteca"));
		obj.setPermiteRealizarReservaCatalogoDisponivel(dadosSQL.getBoolean("permiterealizarreservacatalogodisponivel"));

		obj.setQuantidadeRenovacaoPermitidaVisaoAluno(dadosSQL.getInt("quantidadeRenovacaoPermitidaVisaoAluno"));
		obj.setQuantidadeRenovacaoPermitidaVisaoProfessor(dadosSQL.getInt("quantidadeRenovacaoPermitidaVisaoProfessor"));
		obj.setQuantidadeRenovacaoPermitidaVisaoCoordenador(dadosSQL.getInt("quantidadeRenovacaoPermitidaVisaoCoordenador"));

		obj.setPermiteRenovarExemplarEmAtrasoVisaoProfessor(dadosSQL.getBoolean("permiteRenovarExemplarEmAtrasoVisaoProfessor"));
		obj.setPermiteRenovarExemplarEmAtrasoVisaoAluno(dadosSQL.getBoolean("permiteRenovarExemplarEmAtrasoVisaoAluno"));
		obj.setPermiteRenovarExemplarEmAtrasoVisaoCoordenador(dadosSQL.getBoolean("permiteRenovarExemplarEmAtrasoVisaoCoordenador"));

		obj.setQuantidadeDiasAntesDevolucaoPermitirRenovarExemplarVisaoAluno(dadosSQL.getInt("qtdeDiasAntesDevolucaoPermitirRenovarExemplarVisaoAluno"));
		obj.setQuantidadeDiasAntesDevolucaoPermitirRenovarExemplarVisaoProfessor(dadosSQL.getInt("qtdeDiasAntesDevolucaoPermitirRenovarExemplarVisaoProfessor"));
		obj.setQuantidadeDiasAntesDevolucaoPermitirRenovarExemplarVisaoCoordenador(dadosSQL.getInt("qtdeDiasAntesDevolucaoPermitirRenovarExemplarVisaoCoordenador"));

		obj.setQuantidadeDiasAntesNotificarPrazoDevolucao(dadosSQL.getInt("quantidadeDiasAntesNotificarPrazoDevolucao"));

		obj.setPrazoEmprestimoAlunoFinalDeSemana(dadosSQL.getInt("prazoEmprestimoAlunoFinalDeSemana"));
		obj.setValorMultaEmprestimoAlunoFinalDeSemana(dadosSQL.getDouble("valorMultaEmprestimoAlunoFinalDeSemana"));

		obj.setPrazoEmprestimoProfessorFinalDeSemana(dadosSQL.getInt("prazoEmprestimoProfessorFinalDeSemana"));
		obj.setValorMultaEmprestimoProfessorFinalDeSemana(dadosSQL.getDouble("valorMultaEmprestimoProfessorFinalDeSemana"));

		obj.setPrazoEmprestimoFuncionarioFinalDeSemana(dadosSQL.getInt("prazoEmprestimoFuncionarioFinalDeSemana"));
		obj.setValorMultaEmprestimoFuncionarioFinalDeSemana(dadosSQL.getDouble("valorMultaEmprestimoFuncionarioFinalDeSemana"));

		obj.setValidarMultaOutraBiblioteca(dadosSQL.getBoolean("validarMultaOutraBiblioteca"));
		obj.setValidarExemplarAtrasadoOutraBiblioteca(dadosSQL.getBoolean("validarExemplarAtrasadoOutraBiblioteca"));
		obj.setConsiderarEmprestimoOutraBibliotecaNrMaximoExemplaresEmprestado(dadosSQL.getBoolean("considerarEmprestimoOutraBibliotecaNrMaximoExemplaresEmprestado"));
		
		obj.setPermiteRealizarEmprestimoporHoraAluno(dadosSQL.getBoolean("permiteRealizarEmprestimoporHoraAluno"));
		obj.setLimiteMaximoHorasEmprestimoAluno(dadosSQL.getInt("limiteMaximoHorasEmprestimoAluno"));
		obj.setValorMultaEmprestimoPorHoraAluno(dadosSQL.getDouble("valorMultaEmprestimoPorHoraAluno"));
		obj.setPermiteRealizarEmprestimoporHoraProfessor(dadosSQL.getBoolean("permiteRealizarEmprestimoporHoraProfessor"));
		obj.setLimiteMaximoHorasEmprestimoProfessor(dadosSQL.getInt("limiteMaximoHorasEmprestimoProfessor"));
		obj.setValorMultaEmprestimoPorHoraProfessor(dadosSQL.getDouble("valorMultaEmprestimoPorHoraProfessor"));
		obj.setPermiteRealizarEmprestimoporHoraFuncionario(dadosSQL.getBoolean("permiteRealizarEmprestimoporHoraFuncionario"));
		obj.setLimiteMaximoHorasEmprestimoFuncionario(dadosSQL.getInt("limiteMaximoHorasEmprestimoFuncionario"));
		obj.setValorMultaEmprestimoPorHoraFuncionario(dadosSQL.getDouble("valorMultaEmprestimoPorHoraFuncionario"));
		obj.setTextoPadraoReservaCatalogo(dadosSQL.getString("textoPadraoReservaCatalogo"));
		obj.setGerarBloqueioPorAtrasoAluno(dadosSQL.getBoolean("gerarBloqueioPorAtrasoAluno"));
		obj.setGerarBloqueioPorAtrasoCoordenador(dadosSQL.getBoolean("gerarBloqueioPorAtrasoCoordenador"));
		obj.setGerarBloqueioPorAtrasoProfessor(dadosSQL.getBoolean("gerarBloqueioPorAtrasoProfessor"));
		obj.setGerarBloqueioPorAtrasoFuncionario(dadosSQL.getBoolean("gerarBloqueioPorAtrasoFuncionario"));
		obj.setQuantidadeDiasGerarBloqueioPorAtrasoAluno(dadosSQL.getInt("quantidadeDiasGerarBloqueioPorAtrasoAluno"));
		obj.setQuantidadeDiasGerarBloqueioPorAtrasoCoordenador(dadosSQL.getInt("quantidadeDiasGerarBloqueioPorAtrasoCoordenador"));
		obj.setQuantidadeDiasGerarBloqueioPorAtrasoProfessor(dadosSQL.getInt("quantidadeDiasGerarBloqueioPorAtrasoProfessor"));
		obj.setQuantidadeDiasGerarBloqueioPorAtrasoFuncionario(dadosSQL.getInt("quantidadeDiasGerarBloqueioPorAtrasoFuncionario"));
		obj.setRegraAplicacaoBloqueioAluno(RegraAplicacaoBloqueioBibliotecaEnum.valueOf(dadosSQL.getString("regraAplicacaoBloqueioAluno")));
		obj.setRegraAplicacaoBloqueioCoordenador(RegraAplicacaoBloqueioBibliotecaEnum.valueOf(dadosSQL.getString("regraAplicacaoBloqueioCoordenador")));
		obj.setRegraAplicacaoBloqueioProfessor(RegraAplicacaoBloqueioBibliotecaEnum.valueOf(dadosSQL.getString("regraAplicacaoBloqueioProfessor")));
		obj.setRegraAplicacaoBloqueioFuncionario(RegraAplicacaoBloqueioBibliotecaEnum.valueOf(dadosSQL.getString("regraAplicacaoBloqueioFuncionario")));
		obj.setConsiderarSabadoDiaUtil(dadosSQL.getBoolean("considerarSabadoDiaUtil"));
		obj.setConsiderarDomingoDiaUtil(dadosSQL.getBoolean("considerarDomingoDiaUtil"));

		
		obj.setPrazoEmpresVisitante(dadosSQL.getInt("prazoEmpresVisitante"));
		obj.setQtdeDiaVencimentoMultaVisitante(dadosSQL.getInt("qtdeDiaVencimentoMultaVisitante"));
		obj.setValorMultaDiaVisitante(dadosSQL.getDouble("valorMultaDiaVisitante"));
		obj.setNumeroMaximoExemplaresVisitante(dadosSQL.getInt("numeroMaximoExemplaresVisitante"));
		obj.setNumeroRenovacoesVisitante(dadosSQL.getInt("numeroRenovacoesVisitante"));
		obj.setConsiderarEmprestimoOutraBibliotecaNrMaximoExemplaresEmprestadoVisitante(dadosSQL.getBoolean("consEmpresOutraBibliNrMaximoExemplaresEmprestadoVisitante"));
		obj.setPrazoEmprestimoVisitanteFinalDeSemana(dadosSQL.getInt("prazoEmprestimoVisitanteFinalDeSemana"));
		obj.setValorMultaEmprestimoVisitanteFinalDeSemana(dadosSQL.getDouble("valorMultaEmprestimoVisitanteFinalDeSemana"));
		obj.setPermiteRealizarEmprestimoporHoraVisitante(dadosSQL.getBoolean("permiteRealizarEmprestimoporHoraVisitante"));
		obj.setLimiteMaximoHorasEmprestimoVisitante(dadosSQL.getInt("limiteMaximoHorasEmprestimoVisitante"));
		obj.setValorMultaEmprestimoPorHoraVisitante(dadosSQL.getDouble("valorMultaEmprestimoPorHoraVisitante"));
		obj.setGerarBloqueioPorAtrasoVisitante(dadosSQL.getBoolean("gerarBloqueioPorAtrasoVisitante"));
		obj.setRegraAplicacaoBloqueioVisitante(RegraAplicacaoBloqueioBibliotecaEnum.valueOf(dadosSQL.getString("regraAplicacaoBloqueioVisitante")));
		obj.setPossuiIntegracaoMinhaBiblioteca(dadosSQL.getBoolean("possuiIntegracaoMinhaBiblioteca"));
		obj.setChaveAutenticacaoMinhaBiblioteca(dadosSQL.getString("chaveAutenticacaoMinhaBiblioteca"));
		obj.setPossuiIntegracaoLexMagister(dadosSQL.getBoolean("possuiIntegracaoLexMagister"));
		obj.setChaveAutenticacaoLexMagister(dadosSQL.getString("chaveAutenticacaoLexMagister"));
		obj.setInformacaoHead(dadosSQL.getString("informacaoHead"));
		obj.setHabilitarIntegracaoBvPearson(dadosSQL.getBoolean("habilitarIntegracaoBvPearson"));
		obj.setLinkAcessoBVPerson(dadosSQL.getString("linkacessobvperson"));
		obj.setChaveTokenBVPerson(dadosSQL.getString("chaveTokenBVPerson"));
		if ((dadosSQL.getString("frequenciaNotificacaoGestoresEnum")) == null) {
			obj.setFrequenciaNotificacaoGestoresEnum(FrequenciaNotificacaoGestoresEnum.DIARIA);
		} else {
			obj.setFrequenciaNotificacaoGestoresEnum(FrequenciaNotificacaoGestoresEnum.valueOf(dadosSQL.getString("frequenciaNotificacaoGestoresEnum")));
		}
		if (dadosSQL.getString("tipoClassificacao") == null) {
			obj.setTipoClassificacao(TipoClassificacaoEnum.CDU);
		} else {
			obj.setTipoClassificacao(TipoClassificacaoEnum.valueOf(dadosSQL.getString("tipoClassificacao")));
		}
		obj.getGrupoDestinatariosNotificacao().setCodigo(dadosSQL.getInt("grupoDestinatariosNotificacao"));
		obj.setFuncionarioPadraoEnvioMensagem(getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimariaUnica(dadosSQL.getInt("funcionarioPadraoEnvioMensagem"), false, Uteis.NIVELMONTARDADOS_COMBOBOX, new UsuarioVO()));

		obj.setNovoObj(Boolean.FALSE);
		obj.setListaBibliotecaExternaVO(getFacadeFactory().getBibliotecaExternaFacade().consultarPorCodigoConfiguracaoBiblioteca(obj.getCodigo(), false, null));
		
		obj.setPermiteRenovarMatriculaQuandoPossuiBloqueioBiblioteca(dadosSQL.getBoolean("permitirRenovarMatriculaDoAlunoQuandoPossuirBloqueioBiblioteca"));
		obj.setPossuiIntegracaoEbsco(dadosSQL.getBoolean("possuiIntegracaoEbsco"));
		obj.setHostEbsco(dadosSQL.getString("hostEbsco"));
		obj.setUsuarioEbsco(dadosSQL.getString("usuarioEbsco"));
		obj.setSenhaEbsco(dadosSQL.getString("senhaEbsco"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {

			return obj;
		}

		return obj;
	}

	/**
	 * Operação responsável por localizar um objeto da classe <code>ConfiguracaoBibliotecaVO</code> através de sua chave primária.
	 *
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto procurado.
	 */
	public ConfiguracaoBibliotecaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), false, usuario);
		String sqlStr = "SELECT * FROM ConfiguracaoBiblioteca WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( ConfiguracaoBiblioteca ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações
	 * desta classe.
	 */
	public static String getIdEntidade() {
		return ConfiguracaoBiblioteca.idEntidade;
	}

	public FuncionarioVO montarDadosFuncionario(Integer codigoFuncionario, boolean controlarAcesso, UsuarioVO usuarioLogado) throws Exception {
		FuncionarioVO obj = new FuncionarioVO();
		if (codigoFuncionario != null && codigoFuncionario > 0) {
			obj = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorChavePrimaria(codigoFuncionario, controlarAcesso, usuarioLogado);
		}
		return obj;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste
	 * identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		ConfiguracaoBiblioteca.idEntidade = idEntidade;
	}

	/**
	 * Operação responsável por validar os dados de um objeto da classe <code>ConfiguracaoBibliotecaVO</code>. Todos os tipos de consistência de dados
	 * são e devem ser implementadas neste método. São validações típicas: verificação de campos obrigatórios, verificação de valores válidos para os
	 * atributos.
	 * 
	 * @exception ConsistirExecption
	 *                Se uma inconsistência for encontrada aumaticamente é gerada uma exceção descrevendo o atributo e o erro ocorrido.
	 */
	@Override
	public void validarDados(ConfiguracaoBibliotecaVO configuracaoBibliotecaVO) throws ConsistirException {
		if (configuracaoBibliotecaVO.getNome() == null || configuracaoBibliotecaVO.getNome().equals("")) {
			throw new ConsistirException("O Nome da Configuração Biblioteca deve ser informado.");
		}
		if (configuracaoBibliotecaVO.getPrazoValidadeReservaCatalogosIndisponiveis() == null || configuracaoBibliotecaVO.getPrazoValidadeReservaCatalogosIndisponiveis() <= 0) {
			throw new ConsistirException("O Campo Validade da Reserva Catálogos Indisponíveis (Em Dias) (Aba Geral) deve ser informado.");
		}
		if (configuracaoBibliotecaVO.getPrazoValidadeReservaCatalogosDisponiveis() == null || configuracaoBibliotecaVO.getPrazoValidadeReservaCatalogosDisponiveis() <= 0 || configuracaoBibliotecaVO.getPrazoValidadeReservaCatalogosIndisponiveis() == null || configuracaoBibliotecaVO.getPrazoValidadeReservaCatalogosIndisponiveis() <= 0) {
			throw new ConsistirException("O Campo Validade da Reserva Catálogos Disponíveis (Em Horas) (Aba Geral) deve ser informado.");
		}
		if (configuracaoBibliotecaVO.getNumeroMaximoLivrosReservados() == null || configuracaoBibliotecaVO.getNumeroMaximoLivrosReservados() <= 0) {
			throw new ConsistirException("O Campo Nr Máximo de Catálogos que podem ser reservados (Aba Geral) deve ser informado.");
		}
		if (configuracaoBibliotecaVO.getFuncionarioPadraoEnvioMensagem() == null || configuracaoBibliotecaVO.getFuncionarioPadraoEnvioMensagem().getCodigo() == null || configuracaoBibliotecaVO.getFuncionarioPadraoEnvioMensagem().getCodigo() == 0) {
			throw new ConsistirException("O Campo Usuário Padrão Envio Mensagem (Notif.) (Aba Geral) deve ser Informado.");
		}
		if(!configuracaoBibliotecaVO.getUtilizarApenasDiasUteisEmprestimo()) {
			configuracaoBibliotecaVO.setConsiderarDomingoDiaUtil(false);
			configuracaoBibliotecaVO.setConsiderarSabadoDiaUtil(false);
		}
		if (configuracaoBibliotecaVO.getPossuiIntegracaoMinhaBiblioteca() && configuracaoBibliotecaVO.getChaveAutenticacaoMinhaBiblioteca().trim().isEmpty()) {
			throw new ConsistirException("O Campo API Key (Aba Minha Biblioteca) deve ser Informado.");
		}
		if (configuracaoBibliotecaVO.getPossuiIntegracaoEbsco() && configuracaoBibliotecaVO.getHostEbsco().trim().isEmpty()) {
			throw new ConsistirException("O Campo Host EBSCO (Aba Integração EBSCO) deve ser Informado.");
		}
		if (configuracaoBibliotecaVO.getPossuiIntegracaoEbsco() && configuracaoBibliotecaVO.getUsuarioEbsco().trim().isEmpty()) {
			throw new ConsistirException("O Campo Usuário EBSCO (Aba Integração EBSCO) deve ser Informado.");
		}
		if (configuracaoBibliotecaVO.getPossuiIntegracaoEbsco() && configuracaoBibliotecaVO.getSenhaEbsco().trim().isEmpty()) {
			throw new ConsistirException("O Campo Senha EBSCO (Aba Integração EBSCO) deve ser Informado.");
		}
		if (configuracaoBibliotecaVO.getTamanhoCodigoBarra() < 6) {
			throw new ConsistirException("A Quantidade de Números de Codigo de Barra a Ser Gerado (Aba Geral) não pode ser menor que 6.");
		}	
		if (configuracaoBibliotecaVO.getHabilitarIntegracaoBvPearson() && configuracaoBibliotecaVO.getChaveTokenBVPerson().trim().isEmpty()) {
			throw new ConsistirException("O Campo Chave Token BV - Person (Aba Integração Pearson) deve ser Informado.");
		}
		if (configuracaoBibliotecaVO.getHabilitarIntegracaoBvPearson() && configuracaoBibliotecaVO.getLinkAcessoBVPerson().trim().isEmpty()) {
			throw new ConsistirException("O Campo Link Gateway BV - Person (Aba Integração Pearson) deve ser Informado.");
		}
		
		
		// if (configuracaoBibliotecaVO.getPercentualExemplaresParaConsulta() == null ||
		// configuracaoBibliotecaVO.getPercentualExemplaresParaConsulta().equals("")
		// || configuracaoBibliotecaVO.getPercentualExemplaresParaConsulta() <= 0) {
		// throw new ConsistirException("O Percentual de Exemplares para consulta deve ser informado.");
		// }
	}

	@Override
	public FuncionarioVO consultarFuncionarioPadraoEnvioNotificacaoEmail(UsuarioVO usuarioLogado) throws DataAccessException, Exception {
		String sqlStr = "SELECT funcionarioPadraoEnvioMensagem FROM configuracaobiblioteca where configuracaobiblioteca.padrao = true  limit 1";
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (dadosSQL.next()) {
			FuncionarioVO obj = new FuncionarioVO();
			Integer funcionarioPadraoCondigo = dadosSQL.getInt("funcionarioPadraoEnvioMensagem");
			obj = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorChavePrimaria(funcionarioPadraoCondigo, true, usuarioLogado);
			return obj;
		}
		return new FuncionarioVO();
	}

	public ConfiguracaoBibliotecaVO executarObterConfiguracaoBibliotecaComBaseTipoPessoa(Integer biblioteca, String tipoPessoa, String matricula, UsuarioVO usuarioVO) throws Exception {
		if (!biblioteca.equals(0)) {
			if (tipoPessoa.equals("AL")) {
				return consultarConfiguracaoBibliotecaPorBibliotecaUnidadeEnsinoENivelEducacional(biblioteca, matricula, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
			} else {
				return consultarConfiguracaoBibliotecaPorBibliotecaUnidadeEnsinoENivelEducacional(biblioteca, "", Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
			}
		} else {
			return consultarConfiguracaoPadrao(Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
		}
	}

	public ConfiguracaoBibliotecaVO consultarConfiguracaoBibliotecaPorBibliotecaUnidadeEnsinoENivelEducacional(Integer biblioteca, String matricula, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT configuracaobiblioteca.* FROM biblioteca ");
		sqlStr.append(" INNER JOIN ( ");
		sqlStr.append(" 	select 1 as ordem, configuracaobibliotecaniveleducacional.configuracaobiblioteca, configuracaobibliotecaniveleducacional.biblioteca from matricula ");
		sqlStr.append(" 	inner join curso on curso.codigo = matricula.curso ");
		sqlStr.append(" 	inner join configuracaobibliotecaniveleducacional on configuracaobibliotecaniveleducacional.biblioteca = ").append(biblioteca);
		sqlStr.append(" 	and configuracaobibliotecaniveleducacional.unidadeensino = matricula.unidadeensino ");
		sqlStr.append(" 	and configuracaobibliotecaniveleducacional.niveleducacional = curso.niveleducacional ");
		sqlStr.append("		where matricula.matricula = '").append(matricula).append("' ");
		sqlStr.append(" 	union ");
		sqlStr.append(" 	select 2, configuracaobibliotecaniveleducacional.configuracaobiblioteca, configuracaobibliotecaniveleducacional.biblioteca from matricula ");
		sqlStr.append(" 	inner join curso on curso.codigo = matricula.curso ");
		sqlStr.append(" 	inner join configuracaobibliotecaniveleducacional on configuracaobibliotecaniveleducacional.biblioteca = ").append(biblioteca);
		sqlStr.append(" 	and configuracaobibliotecaniveleducacional.unidadeensino = matricula.unidadeensino ");
		sqlStr.append(" 	and (configuracaobibliotecaniveleducacional.niveleducacional is null or configuracaobibliotecaniveleducacional.niveleducacional = '') ");
		sqlStr.append(" 	where matricula.matricula = '").append(matricula).append("' ");
		sqlStr.append(" 	union ");
		sqlStr.append(" 	select 3, biblioteca.configuracaobiblioteca, biblioteca.codigo from biblioteca where codigo = ").append(biblioteca);
		sqlStr.append(" 	order by ordem limit 1 ");
		sqlStr.append(" ) as configuracaobibliotecautilizar on configuracaobibliotecautilizar.biblioteca = biblioteca.codigo ");
		sqlStr.append(" inner join configuracaobiblioteca on configuracaobiblioteca.codigo = configuracaobibliotecautilizar.configuracaobiblioteca ");
		sqlStr.append(" where biblioteca.codigo = ").append(biblioteca);
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (dadosSQL.next()) {
			return montarDados(dadosSQL, nivelMontarDados);
		}
		
		return new ConfiguracaoBibliotecaVO();
	}
	
	
	public ConfiguracaoBibliotecaVO consultarConfiguracaoBibliotecaPorUnidadeEnsinoENivelEducacional( String matricula, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT configuracaobiblioteca.* FROM biblioteca ");
		sqlStr.append(" INNER JOIN ( ");
		sqlStr.append(" 	select 1 as ordem, configuracaobibliotecaniveleducacional.configuracaobiblioteca, configuracaobibliotecaniveleducacional.biblioteca from matricula ");
		sqlStr.append(" 	inner join curso on curso.codigo = matricula.curso ");
		sqlStr.append(" 	inner join configuracaobibliotecaniveleducacional on configuracaobibliotecaniveleducacional.unidadeensino = matricula.unidadeensino ");
		sqlStr.append(" 	and configuracaobibliotecaniveleducacional.niveleducacional = curso.niveleducacional ");
		sqlStr.append("		where matricula.matricula = '").append(matricula).append("' ");
		sqlStr.append(" 	union ");
		sqlStr.append(" 	select 2, configuracaobibliotecaniveleducacional.configuracaobiblioteca, configuracaobibliotecaniveleducacional.biblioteca from matricula ");
		sqlStr.append(" 	inner join curso on curso.codigo = matricula.curso ");
		sqlStr.append(" 	inner join configuracaobibliotecaniveleducacional on configuracaobibliotecaniveleducacional.unidadeensino = matricula.unidadeensino ");
		sqlStr.append(" 	and (configuracaobibliotecaniveleducacional.niveleducacional is null or configuracaobibliotecaniveleducacional.niveleducacional = '') ");
		sqlStr.append(" 	where matricula.matricula = '").append(matricula).append("' ");
		sqlStr.append(" ) as configuracaobibliotecautilizar on configuracaobibliotecautilizar.biblioteca = biblioteca.codigo ");
		sqlStr.append(" inner join configuracaobiblioteca on configuracaobiblioteca.codigo = configuracaobibliotecautilizar.configuracaobiblioteca ");
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (dadosSQL.next()) {
			return montarDados(dadosSQL, nivelMontarDados);
		}
		return new ConfiguracaoBibliotecaVO();
	}

	@Override
	public Boolean realizarValidacaoBibliotecaLexMagisterHabilitado(Boolean bibliotecaLexMagister,
			UsuarioVO usuarioLogado, VisaoAlunoControle visaoAlunoControle) {
		try {
			if (bibliotecaLexMagister && getAplicacaoControle().carregarDadosConfiguracaoBibliotecaPadrao().getPossuiIntegracaoLexMagister()) {
				boolean usuarioAtivoMinhaBiblioteca = false;
				if (usuarioLogado.getIsApresentarVisaoAluno()) {
					return visaoAlunoControle != null && visaoAlunoControle.getMatricula() != null && visaoAlunoControle.getMatricula().getSituacao().equals("AT");
				} else {
					usuarioAtivoMinhaBiblioteca = true;
				}
				return usuarioAtivoMinhaBiblioteca;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	
	}
	
	@Override
	public Boolean realizarValidacaoBibliotecaBVPearsonHabilitado(Boolean bibliotecaBvPearson,
			UsuarioVO usuarioLogado, VisaoAlunoControle visaoAlunoControle) {
		try {
			if (bibliotecaBvPearson && getAplicacaoControle().carregarDadosConfiguracaoBibliotecaPadrao().getHabilitarIntegracaoBvPearson()) {
				boolean usuarioAtivoBibliotecaBvPearson = false;
				if (usuarioLogado.getIsApresentarVisaoAluno()) {
					return visaoAlunoControle != null && visaoAlunoControle.getMatricula() != null && visaoAlunoControle.getMatricula().getSituacao().equals("AT");
				} else {
					usuarioAtivoBibliotecaBvPearson = true;
				}
				return usuarioAtivoBibliotecaBvPearson;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		
	}
	

	@Override
	public void realizarNavegacaoParaBibliotecaLexMagister(UsuarioVO usuarioLogadoVO){
		ConfiguracaoBibliotecaVO configuracaoBiblioteca = new ConfiguracaoBibliotecaVO();
		ConfiguracaoGeralSistemaVO conGeralSistemaVO = new ConfiguracaoGeralSistemaVO();
		try {
			configuracaoBiblioteca = getAplicacaoControle().carregarDadosConfiguracaoBibliotecaPadrao();
			conGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesMinhaBiblioteca();
			
				if (configuracaoBiblioteca.getPossuiIntegracaoLexMagister()) {				
					if(Uteis.isAtributoPreenchido(configuracaoBiblioteca.getInformacaoHead()) && Uteis.isAtributoPreenchido(configuracaoBiblioteca.getChaveAutenticacaoLexMagister())){						
							
								if(configuracaoBiblioteca.getInformacaoHead().contains("(SESSION ID)") && configuracaoBiblioteca.getInformacaoHead().contains("(CHAVE )")){
									String head =  Uteis.substituirPadraoString(configuracaoBiblioteca.getInformacaoHead(), "(SESSION ID)", usuarioLogadoVO == null ? new Date().getTime()+"" : usuarioLogadoVO.getCodigo().toString());				
									String chave=  configuracaoBiblioteca.getChaveAutenticacaoLexMagister() +""+Uteis.getData(new Date(), "dd/MM/yyyy");
									       chave=   Uteis.encriptarMD5(chave);
									       head =   Uteis.substituirPadraoString(head, "(CHAVE )", chave);
									       if(Uteis.isAtributoPreenchido(chave) && Uteis.isAtributoPreenchido(head)) {
									    	   
									    	   HttpServletResponse  http = (HttpServletResponse) context().getExternalContext().getResponse();					
										       context().getExternalContext().getSessionMap().put("CHAVE", chave);
										       context().getExternalContext().getSessionMap().put("HEAD", head);
										       if(usuarioLogadoVO == null) {
//										    	    http.sendRedirect("paginaLexMagister.xhtml");
										       }else if(usuarioLogadoVO.getIsApresentarVisaoAdministrativa()) {
										    	    http.sendRedirect("../../paginaLexMagister.xhtml");
										       }else {
										    	   //entra na visao aluno professor  coordenador 
										    	   http.sendRedirect("../paginaLexMagister.xhtml");
										       }
									       }else {
									    	    throw new Exception("Chave de autencicação invalida  .");
									       }	
								}else {									
									throw new Exception("cabeçalho  head com parametros incorretos .");
								}
									       
							
							
							
							
						}else {
							throw new Exception("Dados Chave autenticação  e cabecalho head Biblioteca Lex Magister invalidos . ");
						}
						
					
					
				} else {
					throw new Exception("Integração com  Biblioteca Lex Magister  está desabilitada na Configuração Biblioteca.");
				}				
			
			
		} catch (Exception e) {
    	    try {
    			String msg = "<p>Falha ao acessar a plataforma  Biblioteca Lex Magister.</p><p>" + e.getMessage() + "</p>";
    			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
    			String urlStr = conGeralSistemaVO.getUrlAcessoExternoAplicacao() + "/paginaErroCustomizada.xhtml?msg=" + msg;
    			URL url= new URL(urlStr);
    		    URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
				externalContext.redirect(urlStr=uri.toASCIIString());
			} catch (Exception e1) {
			}
		}
		
		
	}
	
	
	
	@Override
	public void verificarConexaoBibliotecaEbsco(String host , String usuario , String senha) throws Exception {
		validarDadosEbsco(host,usuario,senha);		
	    FTPClient ftpCliente = new FTPClient();
		UteisFTP uteisftp = new UteisFTP(host ,usuario,senha,ftpCliente );		
		try {
			uteisftp.connect();	
			uteisftp.disconnectFTP();
		}catch (UnknownHostException ex) {			
			throw new ConsistirException("Erro ao conectar Servidor Verifique sua Internet .");
		}catch (Exception e) {
		    throw e;
		}
		
		
	}
	
	@Override
	public void validarDadosEbsco(String host, String usuario, String senha) throws Exception {		
			if (!Uteis.isAtributoPreenchido(host)) {
				throw new Exception("O Campo Host EBSCO deve ser informado. ");
			}
			if (!Uteis.isAtributoPreenchido(usuario)) {
				throw new Exception("O Campo Usuario EBSCO deve ser informado.");
			}
			if (!Uteis.isAtributoPreenchido(senha)) {
				throw new Exception("O Campo Senha EBSCO deve ser informado. ");
			}
		
	}
	
	@Override
	public ConfiguracaoBibliotecaVO consultarConfiguracaoBibliotecaEbsco() throws Exception {
		String sqlStr = "SELECT codigo , possuiIntegracaoEbsco, hostEbsco ,  usuarioEbsco , senhaEbsco FROM configuracaobiblioteca WHERE padrao = true limit 1";
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (dadosSQL.next()) {
			ConfiguracaoBibliotecaVO obj = new ConfiguracaoBibliotecaVO();
			obj.setPossuiIntegracaoEbsco(dadosSQL.getBoolean("possuiIntegracaoEbsco"));
			obj.setHostEbsco(dadosSQL.getString("hostEbsco"));
			obj.setUsuarioEbsco(dadosSQL.getString("usuarioEbsco"));
			obj.setSenhaEbsco(dadosSQL.getString("senhaEbsco"));
			obj.setCodigo(dadosSQL.getInt("codigo"));
			return obj;
		}
		return new ConfiguracaoBibliotecaVO();
	}

	@Override
	public void realizarEnvioCatalogoIntegracaoEbsco(List<ArquivoMarc21VO> arquivoMarc21VOs , ProgressBarVO progressBarVO, ConfiguracaoGeralSistemaVO config , boolean validarIntegracaoEbsco,String comandoIncluirAlterarExcluirCatalogoEbsco) throws Exception {	
		try {			
		//  final ConfiguracaoBibliotecaVO configuracaoBiblioteca = consultarConfiguracaoBibliotecaEbsco() ;		 
			 final ConfiguracaoBibliotecaVO configuracaoBiblioteca = getAplicacaoControle().carregarDadosConfiguracaoBibliotecaPadrao();
		 if(Uteis.isAtributoPreenchido(configuracaoBiblioteca) && configuracaoBiblioteca.getPossuiIntegracaoEbsco()) {	
			
			 UteisFTP ftpEbscoParalel = new UteisFTP(configuracaoBiblioteca.getHostEbsco(),configuracaoBiblioteca.getUsuarioEbsco(),
	                 configuracaoBiblioteca.getSenhaEbsco(),new FTPClient()); 	
			 
			
			    ConsistirException consistirException = new ConsistirException();
							
				UteisFTP ftpEbsco = new UteisFTP(configuracaoBiblioteca.getHostEbsco(),configuracaoBiblioteca.getUsuarioEbsco(),
		                 configuracaoBiblioteca.getSenhaEbsco(),new FTPClient()); 
				String pastaFTP  = "update";	
				if(arquivoMarc21VOs.get(0).getArquivoMarc21CatalogoVOs().size() > 1) {
					  pastaFTP = "full";		
				}		    
			    
			     ftpEbsco.connect();
				 ftpEbsco.getFtpClient().changeWorkingDirectory(pastaFTP);
				 Integer ponteiro =0 ;
				 Integer nArquivoTimeout=100;
			     for(ArquivoMarc21VO  arquivoMarc21VO : arquivoMarc21VOs) {
			    	     if(ponteiro.equals(nArquivoTimeout)) {			    	    	
			    	    	  ftpEbsco.disconnectFTP();	
			    	    	 Thread.sleep(3000);
			    	    	 ftpEbsco = new UteisFTP(configuracaoBiblioteca.getHostEbsco(),configuracaoBiblioteca.getUsuarioEbsco(),
					               configuracaoBiblioteca.getSenhaEbsco(),new FTPClient()); 
			    	    	 Thread.sleep(60000);
			    	    	 ftpEbsco.connect();
			    	    	 ftpEbsco.getFtpClient().changeWorkingDirectory(pastaFTP);
			    	    	 ponteiro = 0 ;
			    	     }
						 progressBarVO.incrementar();
//				         getFacadeFactory().getArquivoMarc21Facade().executarExportarArquivoMarc21XML(arquivoMarc21VO, progressBarVO.getUsuarioVO(),config,comandoIncluirAlterarExcluirCatalogoEbsco);		
				         realizarEnvioCatalogoIntegracaoEbsco(arquivoMarc21VO, ftpEbsco, config, Boolean.TRUE ,progressBarVO);
				         ponteiro ++ ;
				        
			   }
			     ftpEbsco.disconnectFTP();
			
			 progressBarVO.setForcarEncerramento(true);
			
			
		 }else {
		    	if(validarIntegracaoEbsco) {		    		
		    		throw new Exception("Integração com  Biblioteca Ebsco está desabilitada na Configuração Biblioteca.");
		    	}
		    }
		 } catch (Exception e) {
				throw e;
			}
		
	}

	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarEnvioCatalogoIntegracaoEbsco(ArquivoMarc21VO  arquivoMarc21VO , UteisFTP ftpClient, 
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaPrivilegiandoACfgDaUnidade ,boolean validarIntegracaoEbsco
			,ProgressBarVO progressBarVO ) throws Exception  {
		
	 		try {
	 			for(ArquivoMarc21CatalogoVO arquivoMarcCatalogo : arquivoMarc21VO.getArquivoMarc21CatalogoVOs()) {
	 				  CatalogoVO catalogo = arquivoMarcCatalogo.getCatalogoVO(); 
	 				 if(!catalogo.getEnviadoEbsco()){					
							catalogo.setEnviadoEbsco(Boolean.TRUE);
//							getFacadeFactory().getCatalogoFacade().alterarEnvioEbsco(catalogo, progressBarVO.getUsuarioVO());
						}
	 			}
	 		 			
					
	    	    boolean  enviou = false;
	    	    int quantidadeTentativas = 3 ;
		        File fileTmp = new File(
						configuracaoGeralSistemaPrivilegiandoACfgDaUnidade.getLocalUploadArquivoFixo()
								+ File.separator + arquivoMarc21VO.getArquivoVO().getPastaBaseArquivo()
								+ File.separator + arquivoMarc21VO.getArquivoVO().getNome());
			       
	    	    while(!enviou) {		   
	    		   try {	 
		    			ftpClient.realizarEnvioArquivoFtp(configuracaoGeralSistemaPrivilegiandoACfgDaUnidade , arquivoMarc21VO.getArquivoVO() );
		    			enviou = true ;
		    		    fileTmp.delete();			    		   
	    		   }catch (Exception e) {
	    			      ftpClient.disconnectFTP();
	    			      Thread.sleep(3000);	    		  
	    			      ftpClient.connect();
	    			      quantidadeTentativas --;
		    		      if(quantidadeTentativas == 0) {
		    			      ftpClient.disconnectFTP();
		    			      fileTmp.delete();
		    			     throw e;
		    		      } 
				   }	    		  
	    	    }  
		
		      } catch (Exception e) {			
			     throw e ;
		      }
	}
	
	
	

	@Override
	public String realizarCriacaoTokenPearson(String loginPearson) {
		try { 
		//	ConfiguracaoBibliotecaVO conf =  consultarConfiguracaoBibliotecaBVPearson();
			ConfiguracaoBibliotecaVO conf =  getAplicacaoControle().carregarDadosConfiguracaoBibliotecaPadrao();			
			String chave = loginPearson+ conf.getChaveTokenBVPerson();
			return Uteis.encriptarMD5(chave) ; 
		} catch (Exception e) {		
			e.printStackTrace();
		}
		return null;
	}

    
}


