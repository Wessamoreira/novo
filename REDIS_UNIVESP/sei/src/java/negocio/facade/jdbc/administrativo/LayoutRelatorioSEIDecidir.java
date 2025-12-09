package negocio.facade.jdbc.administrativo;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import COM.rsa.asn1.el;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FiltroPersonalizadoOpcaoVO;
import negocio.comuns.administrativo.FiltroPersonalizadoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.LayoutRelatorioSEIDecidirArquivoVO;
import negocio.comuns.administrativo.LayoutRelatorioSEIDecidirFuncionarioVO;
import negocio.comuns.administrativo.LayoutRelatorioSEIDecidirPerfilAcessoVO;
import negocio.comuns.administrativo.LayoutRelatorioSEIDecidirVO;
import negocio.comuns.administrativo.LayoutRelatorioSeiDecidirCampoVO;
import negocio.comuns.administrativo.enumeradores.OrigemFiltroPersonalizadoEnum;
import negocio.comuns.administrativo.enumeradores.RelatorioSEIDecidirModuloEnum;
import negocio.comuns.administrativo.enumeradores.RelatorioSEIDecidirNivelDetalhamentoEnum;
import negocio.comuns.arquitetura.PerfilAcessoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.administrativo.LayoutRelatorioSEIDecidirInterface;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;

/**
 * 
 * @author Leonardo Riciolle Data 17/11/2014
 */
@Repository
@Scope("singleton")
@Lazy
public class LayoutRelatorioSEIDecidir extends ControleAcesso implements LayoutRelatorioSEIDecidirInterface {

	private static final long serialVersionUID = 1L;

	protected static String idEntidade;

	public LayoutRelatorioSEIDecidir() throws Exception {
		super();
		setIdEntidade("LayoutRelatorioSEIDecidir");
	}

	public LayoutRelatorioSEIDecidir novo() throws Exception {
		LayoutRelatorioSEIDecidir.incluir(getIdEntidade());
		LayoutRelatorioSEIDecidir obj = new LayoutRelatorioSEIDecidir();
		return obj;
	}

	public static void validarDados(LayoutRelatorioSEIDecidirVO obj) throws Exception {
		if (obj.getDescricao().equals("")) {
			throw new Exception(UteisJSF.internacionalizar("msg_LayoutRelatorioSEIDecidir_descricao"));
		}
	}

	public static void validarDadosConsulta(String valorConsulta) throws Exception {
		if (valorConsulta.equals("")) {
			throw new Exception("Deve ser informado um valor para a consulta.");
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistirTodos(LayoutRelatorioSEIDecidirVO obj, boolean verificarAcesso, UsuarioVO usuarioVO, List<PerfilAcessoVO> listaIdsPerfilAcesso, List<FuncionarioVO> listaIdsFuncionario) throws Exception {
		this.persistir(obj, verificarAcesso, usuarioVO);

		persistirLayoutRelatorioSEIDecidirArquivo(obj, usuarioVO);

		persistirLayoutRelatorioSEIDecidirFuncionario(obj, usuarioVO, listaIdsFuncionario);

		persistirLayoutRelatorioSEIDecidirPerfilAcesso(obj, usuarioVO, listaIdsPerfilAcesso);
	}

	private void persistirLayoutRelatorioSEIDecidirArquivo(LayoutRelatorioSEIDecidirVO obj, UsuarioVO usuarioVO) throws Exception {
		getFacadeFactory().getLayoutRelatorioSEIDecidirArquivoInterfaceFacade().excluirPorLayoutRelatorioSeiDecidir(obj, false, usuarioVO);

		for (LayoutRelatorioSEIDecidirArquivoVO layoutRelatorioSEIDecidirArquivoVO : obj.getListaRelatorioSEIDecidirArquivo()) {
			layoutRelatorioSEIDecidirArquivoVO.setLayoutRelatorioSEIDecidirVO(obj);
			getFacadeFactory().getLayoutRelatorioSEIDecidirArquivoInterfaceFacade().persistir(layoutRelatorioSEIDecidirArquivoVO, false, usuarioVO);
		}
	}

	private void persistirLayoutRelatorioSEIDecidirPerfilAcesso(LayoutRelatorioSEIDecidirVO obj, UsuarioVO usuarioVO, List<PerfilAcessoVO> listaIdsPerfilAcesso) throws Exception {
		getFacadeFactory().getLayoutRelatorioSEIDecidirPerfilAcessoInterfaceFacade().excluirPorLayoutRelatorioSeiDecidir(obj, false, usuarioVO);

		for (PerfilAcessoVO perfilAcessoVO : listaIdsPerfilAcesso) {
			LayoutRelatorioSEIDecidirPerfilAcessoVO layoutRelatorioSEIDecidirPerfilAcessoVO = new LayoutRelatorioSEIDecidirPerfilAcessoVO();
			layoutRelatorioSEIDecidirPerfilAcessoVO.setPerfilAcessoVO(perfilAcessoVO);
			layoutRelatorioSEIDecidirPerfilAcessoVO.setLayoutRelatorioSEIDecidirVO(obj);
			getFacadeFactory().getLayoutRelatorioSEIDecidirPerfilAcessoInterfaceFacade().persistir(layoutRelatorioSEIDecidirPerfilAcessoVO, false, usuarioVO);
		}
	}

	private void persistirLayoutRelatorioSEIDecidirFuncionario(LayoutRelatorioSEIDecidirVO obj, UsuarioVO usuarioVO, List<FuncionarioVO> listaIdsFuncionario) throws Exception {
		getFacadeFactory().getLayoutRelatorioSEIDecidirFuncionarioInterfaceFacade().excluirPorLayoutRelatorioSeiDecidir(obj, false, usuarioVO);

		for (FuncionarioVO funcionarioVO : listaIdsFuncionario) {
			LayoutRelatorioSEIDecidirFuncionarioVO layoutRelatorioSEIDecidirFuncionario = new LayoutRelatorioSEIDecidirFuncionarioVO();
			layoutRelatorioSEIDecidirFuncionario.setFuncionarioVO(funcionarioVO);
			layoutRelatorioSEIDecidirFuncionario.setLayoutRelatorioSEIDecidirVO(obj);
			getFacadeFactory().getLayoutRelatorioSEIDecidirFuncionarioInterfaceFacade().persistir(layoutRelatorioSEIDecidirFuncionario, false, usuarioVO);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(LayoutRelatorioSEIDecidirVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			if (obj.getNovoObj()) {
				incluir(obj, verificarAcesso, usuarioVO);
			} else {
				alterar(obj, verificarAcesso, usuarioVO);
			}

		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final LayoutRelatorioSEIDecidirVO obj, boolean verificarAcesso, final UsuarioVO usuarioVO) throws Exception {
		try {
			validarDados(obj);
			LayoutRelatorioSEIDecidir.incluir(getIdEntidade(), verificarAcesso, usuarioVO);
			final StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO layoutrelatorioseidecidir(descricao, modulo, niveldetalhamento, agruparRelatorioPor, condicaoWhereAdicional, ");
			sql.append(" groupByAdicional, nomePadraoArquivo, tipoGeracaoRelatorio, tagAberturaGeralXml, tagFechamentoGeralXml, tagAberturaLinhaXml, tagFechamentoLinhaXml, subRelatorio,");
			sql.append(" subRelatorioCrossTab , textoCabecalho , gerarLinhaEmBranco, condicaoJoinAdicional, orderByAdicional, layoutPersonalizado, queryLayoutPersonalizado, ");
			
			sql.append(" apresentarFiltroFixoUnidadeEnsino, apresentarFiltroFixoNivelEducacional, apresentarFiltroFixoCotaIngresso, apresentarFiltroFixoCurso, apresentarFiltroFixoTurno, apresentarFiltroFixoTurma, ");
			sql.append(" apresentarFiltroFixoMatricula, apresentarFiltroFixoTurmaEstudouDisciplina, apresentarFiltroFixoDisciplina, apresentarFiltroFixoFiltroPeriodo, apresentarFiltroFixoPeriodoLetivo, apresentarFiltroFixoSituacaoAceiteEletronicoContrato, ");
			sql.append(" apresentarFiltroFixoSituacaoFiltroSituacaoAcademica, apresentarFiltroFixoSituacaoFinanceiraMatricula, ");
			
			sql.append(" apresentarFiltroFixoCentroReceita, apresentarFiltroFixoFormaPagamento, apresentarFiltroFixoContaCorrente, apresentarFiltroFixoTipoPessoa, apresentarFiltroFixoConsiderarUnidadeFinanceira, apresentarFiltroFixoContaCorrenteRecebimento, ");
			sql.append(" apresentarFiltroFixoSituacaoContaReceber, apresentarFiltroFixoTipoOrigem, apresentarFiltroFixoPeriodoLetivoCentroResultado, apresentarFiltroFixoCentroResultado, apresentarFiltroFixoNivelCentroResultado, apresentarFiltroFixoCategoriaDespesa, ");
			
			sql.append(" apresentarFiltroFixoFavorecido, apresentarFiltroFixoSituacaoContaPagar, apresentarFiltroFixoTipoRequerimento, apresentarFiltroFixoResponsavel, apresentarFiltroFixoTurmaReposicao, apresentarFiltroFixoRequerente, apresentarFiltroFixoCoordenador, ");
			sql.append(" apresentarFiltroFixoDepartamentoResponsavel, apresentarFiltroFixoSituacaoTramite, apresentarFiltroFixoSituacaoRequerimento, apresentarFiltroFixoSituacaoFinanceiraRequerimento, ");
			
			sql.append(" apresentarFiltroFixoBiblioteca, apresentarFiltroFixoTipoCatalogo, apresentarFiltroFixoClassificacaoBibliografica, apresentarFiltroFixoTitulo, apresentarFiltroFixoSecao, apresentarFiltroFixoAreaConhecimento, apresentarFiltroFixoFormaEntrada, ");
			sql.append(" apresentarFiltroFixoDataInicioAquisicao, apresentarFiltroFixoDataFimAquisicao, apresentarFiltroFixoTipo, apresentarFiltroFixoCatalogoPeriodico, apresentarFiltroFixoSituacaoEmprestimo, apresentarFiltroFixoDataInicioEmprestimo, apresentarFiltroFixoDataFimEmprestimo, ");
			
			sql.append(" apresentarFiltroFixoProcessoSeletivo, apresentarFiltroFixoDataProvaInicio, apresentarFiltroFixoDataProvaFim, apresentarFiltroFixoSituacaoResultadoProcessoSeletivo, apresentarFiltroFixoSituacaoInscricao, apresentarFiltroFixoSituacaoFinanceiraProcessoSeletivo, ");
			
			sql.append(" apresentarFiltroFixoCampanha, apresentarFiltroFixoConsultor, apresentarFiltroFixoComponenteEstagio, apresentarFiltroFixoSituacaoEstagio, apresentarFiltroFixoPeriodoEstagio ");
			sql.append(" )");
			sql.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ? , ?, ?, ?, ?, ?, "
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
					+ "? ,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? "
					+ ") returning codigo " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql.toString());
					try {
						sqlInserir.setString(1, obj.getDescricao());
						sqlInserir.setString(2, obj.getModulo().toString());
						sqlInserir.setString(3, obj.getNivelDetalhamento().toString());
						sqlInserir.setString(4, obj.getAgruparRelatorioPor());
						sqlInserir.setString(5, obj.getCondicaoWhereAdicional());
						sqlInserir.setString(6, obj.getGroupByAdicional());
						sqlInserir.setString(7, obj.getNomePadraoArquivo());
						sqlInserir.setString(8, obj.getTipoGeracaoRelatorio().name());
						sqlInserir.setString(9, obj.getTagAberturaGeralXml());
						sqlInserir.setString(10, obj.getTagFechamentoGeralXml());
						sqlInserir.setString(11, obj.getTagAberturaLinhaXml());
						sqlInserir.setString(12, obj.getTagFechamentoLinhaXml());
						sqlInserir.setBoolean(13, obj.getSubRelatorio());
						sqlInserir.setBoolean(14, obj.getSubRelatorioCrossTab());
						sqlInserir.setString(15, obj.getTextoCabecalho());
						sqlInserir.setBoolean(16, obj.getGerarLinhaEmBranco());
						sqlInserir.setString(17, obj.getCondicaoJoinAdicional());
						sqlInserir.setString(18, obj.getOrderByAdicional());
						sqlInserir.setBoolean(19, obj.getLayoutPersonalizado());
						sqlInserir.setString(20, obj.getQueryLayoutPersonalizado());

						sqlInserir.setBoolean(21, obj.getApresentarFiltroFixoUnidadeEnsino());
						sqlInserir.setBoolean(22, obj.getApresentarFiltroFixoNivelEducacional());
						sqlInserir.setBoolean(23, obj.getApresentarFiltroFixoCotaIngresso());
						sqlInserir.setBoolean(24, obj.getApresentarFiltroFixoCurso());
						sqlInserir.setBoolean(25, obj.getApresentarFiltroFixoTurno());
						sqlInserir.setBoolean(26, obj.getApresentarFiltroFixoTurma());
						sqlInserir.setBoolean(27, obj.getApresentarFiltroFixoMatricula());
						sqlInserir.setBoolean(28, obj.getApresentarFiltroFixoTurmaEstudouDisciplina());
						sqlInserir.setBoolean(29, obj.getApresentarFiltroFixoDisciplina());
						sqlInserir.setBoolean(30, obj.getApresentarFiltroFixoFiltroPeriodo());
						sqlInserir.setBoolean(31, obj.getApresentarFiltroFixoPeriodoLetivo());
						sqlInserir.setBoolean(32, obj.getApresentarFiltroFixoSituacaoAceiteEletronicoContrato());
						sqlInserir.setBoolean(33, obj.getApresentarFiltroFixoSituacaoFiltroSituacaoAcademica());
						sqlInserir.setBoolean(34, obj.getApresentarFiltroFixoSituacaoFinanceiraMatricula());
						
						sqlInserir.setBoolean(35, obj.getApresentarFiltroFixoCentroReceita());
						sqlInserir.setBoolean(36, obj.getApresentarFiltroFixoFormaPagamento());
						sqlInserir.setBoolean(37, obj.getApresentarFiltroFixoContaCorrente());
						sqlInserir.setBoolean(38, obj.getApresentarFiltroFixoTipoPessoa());
						sqlInserir.setBoolean(39, obj.getApresentarFiltroFixoConsiderarUnidadeFinanceira());
						sqlInserir.setBoolean(40, obj.getApresentarFiltroFixoContaCorrenteRecebimento());
						sqlInserir.setBoolean(41, obj.getApresentarFiltroFixoSituacaoContaReceber());
						sqlInserir.setBoolean(42, obj.getApresentarFiltroFixoTipoOrigem());
						sqlInserir.setBoolean(43, obj.getApresentarFiltroFixoPeriodoLetivoCentroResultado());
						sqlInserir.setBoolean(44, obj.getApresentarFiltroFixoCentroResultado());
						sqlInserir.setBoolean(45, obj.getApresentarFiltroFixoNivelCentroResultado());
						sqlInserir.setBoolean(46, obj.getApresentarFiltroFixoCategoriaDespesa());
						
						sqlInserir.setBoolean(47, obj.getApresentarFiltroFixoFavorecido());
						sqlInserir.setBoolean(48, obj.getApresentarFiltroFixoSituacaoContaPagar());
						
						sqlInserir.setBoolean(49, obj.getApresentarFiltroFixoTipoRequerimento());
						sqlInserir.setBoolean(50, obj.getApresentarFiltroFixoResponsavel());
						sqlInserir.setBoolean(51, obj.getApresentarFiltroFixoTurmaReposicao());
						sqlInserir.setBoolean(52, obj.getApresentarFiltroFixoRequerente());
						sqlInserir.setBoolean(53, obj.getApresentarFiltroFixoCoordenador());
						sqlInserir.setBoolean(54, obj.getApresentarFiltroFixoDepartamentoResponsavel());
						sqlInserir.setBoolean(55, obj.getApresentarFiltroFixoSituacaoTramite());
						sqlInserir.setBoolean(56, obj.getApresentarFiltroFixoSituacaoRequerimento());
						sqlInserir.setBoolean(57, obj.getApresentarFiltroFixoSituacaoFinanceiraRequerimento());
						
						sqlInserir.setBoolean(58, obj.getApresentarFiltroFixoBiblioteca());
						sqlInserir.setBoolean(59, obj.getApresentarFiltroFixoTipoCatalogo());
						sqlInserir.setBoolean(60, obj.getApresentarFiltroFixoClassificacaoBibliografica());
						sqlInserir.setBoolean(61, obj.getApresentarFiltroFixoTitulo());
						sqlInserir.setBoolean(62, obj.getApresentarFiltroFixoSecao());
						sqlInserir.setBoolean(63, obj.getApresentarFiltroFixoAreaConhecimento());
						sqlInserir.setBoolean(64, obj.getApresentarFiltroFixoFormaEntrada());
						sqlInserir.setBoolean(65, obj.getApresentarFiltroFixoDataInicioAquisicao());
						sqlInserir.setBoolean(66, obj.getApresentarFiltroFixoDataFimAquisicao());
						sqlInserir.setBoolean(67, obj.getApresentarFiltroFixoTipo());
						sqlInserir.setBoolean(68, obj.getApresentarFiltroFixoCatalogoPeriodico());
						sqlInserir.setBoolean(69, obj.getApresentarFiltroFixoSituacaoEmprestimo());
						sqlInserir.setBoolean(70, obj.getApresentarFiltroFixoDataInicioEmprestimo());
						sqlInserir.setBoolean(71, obj.getApresentarFiltroFixoDataFimEmprestimo());
						
						sqlInserir.setBoolean(72, obj.getApresentarFiltroFixoProcessoSeletivo());
						sqlInserir.setBoolean(73, obj.getApresentarFiltroFixoDataProvaInicio());
						sqlInserir.setBoolean(74, obj.getApresentarFiltroFixoDataProvaFim());
						sqlInserir.setBoolean(75, obj.getApresentarFiltroFixoSituacaoResultadoProcessoSeletivo());
						sqlInserir.setBoolean(76, obj.getApresentarFiltroFixoSituacaoInscricao());
						sqlInserir.setBoolean(77, obj.getApresentarFiltroFixoSituacaoFinanceiraProcessoSeletivo());
						
						sqlInserir.setBoolean(78, obj.getApresentarFiltroFixoCampanha());
						sqlInserir.setBoolean(79, obj.getApresentarFiltroFixoConsultor());
						
						sqlInserir.setBoolean(80, obj.getApresentarFiltroFixoComponenteEstagio());
						sqlInserir.setBoolean(81, obj.getApresentarFiltroFixoSituacaoEstagio());
						sqlInserir.setBoolean(82, obj.getApresentarFiltroFixoPeriodoEstagio());
						
					} catch (final Exception x) {
						return null;
					}
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(final ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
			getFacadeFactory().getLayoutRelatorioSEIDecidirCampoInterfaceFacade().persistirLayoutRelatorioSeiDecidirCampoVOs(obj, verificarAcesso, usuarioVO);
			getFacadeFactory().getFiltroPersonalizadoFacade().incluirFiltroPersonalizado(obj.getCodigo(), OrigemFiltroPersonalizadoEnum.SEI_DECIDIR, obj.getListaFiltroPersonalizadoVOs(), usuarioVO);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final LayoutRelatorioSEIDecidirVO obj, boolean verificarAcesso, final UsuarioVO usuarioVO) throws Exception {
		try {
			validarDados(obj);
			LayoutRelatorioSEIDecidir.alterar(getIdEntidade(), verificarAcesso, usuarioVO);
			final StringBuilder sql = new StringBuilder();
			sql.append("UPDATE layoutrelatorioseidecidir  SET descricao=?, modulo=?, niveldetalhamento=?, agruparRelatorioPor = ?, condicaoWhereAdicional = ?, ");
			sql.append(" groupByAdicional = ?, nomePadraoArquivo = ?, tipoGeracaoRelatorio = ?, tagAberturaGeralXml = ?, tagFechamentoGeralXml = ?, tagAberturaLinhaXml = ?, tagFechamentoLinhaXml = ? , subrelatorio = ?, subRelatorioCrossTab=? , textoCabecalho = ? , gerarLinhaEmBranco = ?, condicaoJoinAdicional = ?, orderByAdicional = ?, "
					+ " layoutPersonalizado = ?, queryLayoutPersonalizado = ?, ");
			
			sql.append(" apresentarFiltroFixoUnidadeEnsino = ?, apresentarFiltroFixoNivelEducacional = ?, apresentarFiltroFixoCotaIngresso = ?, apresentarFiltroFixoCurso = ?, apresentarFiltroFixoTurno = ?, apresentarFiltroFixoTurma = ?, ");
			sql.append(" apresentarFiltroFixoMatricula = ?, apresentarFiltroFixoTurmaEstudouDisciplina = ?, apresentarFiltroFixoDisciplina = ?, apresentarFiltroFixoFiltroPeriodo = ?, apresentarFiltroFixoPeriodoLetivo = ?, apresentarFiltroFixoSituacaoAceiteEletronicoContrato = ?, ");
			sql.append(" apresentarFiltroFixoSituacaoFiltroSituacaoAcademica = ?, apresentarFiltroFixoSituacaoFinanceiraMatricula = ?, ");
			
			sql.append(" apresentarFiltroFixoCentroReceita = ?, apresentarFiltroFixoFormaPagamento = ?, apresentarFiltroFixoContaCorrente = ?, apresentarFiltroFixoTipoPessoa = ?, apresentarFiltroFixoConsiderarUnidadeFinanceira = ?, apresentarFiltroFixoContaCorrenteRecebimento = ?, ");
			sql.append(" apresentarFiltroFixoSituacaoContaReceber = ?, apresentarFiltroFixoTipoOrigem = ?, apresentarFiltroFixoPeriodoLetivoCentroResultado = ?, apresentarFiltroFixoCentroResultado = ?, apresentarFiltroFixoNivelCentroResultado = ?, apresentarFiltroFixoCategoriaDespesa = ?, ");
			
			sql.append(" apresentarFiltroFixoFavorecido = ?, apresentarFiltroFixoSituacaoContaPagar = ?, apresentarFiltroFixoTipoRequerimento = ?, apresentarFiltroFixoResponsavel = ? , apresentarFiltroFixoTurmaReposicao = ?, apresentarFiltroFixoRequerente = ?, apresentarFiltroFixoCoordenador = ?, ");
			sql.append(" apresentarFiltroFixoDepartamentoResponsavel = ?, apresentarFiltroFixoSituacaoTramite = ?, apresentarFiltroFixoSituacaoRequerimento = ?, apresentarFiltroFixoSituacaoFinanceiraRequerimento = ?, ");
			
			sql.append(" apresentarFiltroFixoBiblioteca = ?, apresentarFiltroFixoTipoCatalogo = ?, apresentarFiltroFixoClassificacaoBibliografica = ?, apresentarFiltroFixoTitulo = ?, apresentarFiltroFixoSecao = ?, apresentarFiltroFixoAreaConhecimento = ?, apresentarFiltroFixoFormaEntrada = ?, ");
			sql.append(" apresentarFiltroFixoDataInicioAquisicao = ?, apresentarFiltroFixoDataFimAquisicao = ?, apresentarFiltroFixoTipo = ?, apresentarFiltroFixoCatalogoPeriodico = ?, apresentarFiltroFixoSituacaoEmprestimo = ?, apresentarFiltroFixoDataInicioEmprestimo = ?, apresentarFiltroFixoDataFimEmprestimo = ?, ");
			
			sql.append(" apresentarFiltroFixoProcessoSeletivo = ?, apresentarFiltroFixoDataProvaInicio = ?, apresentarFiltroFixoDataProvaFim = ?, apresentarFiltroFixoSituacaoResultadoProcessoSeletivo = ?, apresentarFiltroFixoSituacaoInscricao = ?, apresentarFiltroFixoSituacaoFinanceiraProcessoSeletivo = ?, ");
			
			sql.append(" apresentarFiltroFixoCampanha = ?, apresentarFiltroFixoConsultor = ?, apresentarFiltroFixoComponenteEstagio = ?, apresentarFiltroFixoSituacaoEstagio = ?, apresentarFiltroFixoPeriodoEstagio = ?");
			
			sql.append(" WHERE codigo = " + obj.getCodigo() + ";" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql.toString());
					sqlAlterar.setString(1, obj.getDescricao());
					sqlAlterar.setString(2, obj.getModulo().toString());
					sqlAlterar.setString(3, obj.getNivelDetalhamento().toString());
					sqlAlterar.setString(4, obj.getAgruparRelatorioPor());
					sqlAlterar.setString(5, obj.getCondicaoWhereAdicional());
					sqlAlterar.setString(6, obj.getGroupByAdicional());
					sqlAlterar.setString(7, obj.getNomePadraoArquivo());
					sqlAlterar.setString(8, obj.getTipoGeracaoRelatorio().name());
					sqlAlterar.setString(9, obj.getTagAberturaGeralXml());
					sqlAlterar.setString(10, obj.getTagFechamentoGeralXml());
					sqlAlterar.setString(11, obj.getTagAberturaLinhaXml());
					sqlAlterar.setString(12, obj.getTagFechamentoLinhaXml());
					sqlAlterar.setBoolean(13, obj.getSubRelatorio());
					sqlAlterar.setBoolean(14, obj.getSubRelatorioCrossTab());
					sqlAlterar.setString(15, obj.getTextoCabecalho());
					sqlAlterar.setBoolean(16, obj.getGerarLinhaEmBranco());
					sqlAlterar.setString(17, obj.getCondicaoJoinAdicional());
					sqlAlterar.setString(18, obj.getOrderByAdicional());
					sqlAlterar.setBoolean(19, obj.getLayoutPersonalizado());
					sqlAlterar.setString(20, obj.getQueryLayoutPersonalizado());
					int x = 21;
					sqlAlterar.setBoolean(x++, obj.getApresentarFiltroFixoUnidadeEnsino());
					sqlAlterar.setBoolean(x++, obj.getApresentarFiltroFixoNivelEducacional());
					sqlAlterar.setBoolean(x++, obj.getApresentarFiltroFixoCotaIngresso());
					sqlAlterar.setBoolean(x++, obj.getApresentarFiltroFixoCurso());
					sqlAlterar.setBoolean(x++, obj.getApresentarFiltroFixoTurno());
					sqlAlterar.setBoolean(x++, obj.getApresentarFiltroFixoTurma());
					sqlAlterar.setBoolean(x++, obj.getApresentarFiltroFixoMatricula());
					sqlAlterar.setBoolean(x++, obj.getApresentarFiltroFixoTurmaEstudouDisciplina());
					sqlAlterar.setBoolean(x++, obj.getApresentarFiltroFixoDisciplina());
					sqlAlterar.setBoolean(x++, obj.getApresentarFiltroFixoFiltroPeriodo());
					sqlAlterar.setBoolean(x++, obj.getApresentarFiltroFixoPeriodoLetivo());
					sqlAlterar.setBoolean(x++, obj.getApresentarFiltroFixoSituacaoAceiteEletronicoContrato());
					sqlAlterar.setBoolean(x++, obj.getApresentarFiltroFixoSituacaoFiltroSituacaoAcademica());
					sqlAlterar.setBoolean(x++, obj.getApresentarFiltroFixoSituacaoFinanceiraMatricula());
					
					sqlAlterar.setBoolean(x++, obj.getApresentarFiltroFixoCentroReceita());
					sqlAlterar.setBoolean(x++, obj.getApresentarFiltroFixoFormaPagamento());
					sqlAlterar.setBoolean(x++, obj.getApresentarFiltroFixoContaCorrente());
					sqlAlterar.setBoolean(x++, obj.getApresentarFiltroFixoTipoPessoa());
					sqlAlterar.setBoolean(x++, obj.getApresentarFiltroFixoConsiderarUnidadeFinanceira());
					sqlAlterar.setBoolean(x++, obj.getApresentarFiltroFixoContaCorrenteRecebimento());
					sqlAlterar.setBoolean(x++, obj.getApresentarFiltroFixoSituacaoContaReceber());
					sqlAlterar.setBoolean(x++, obj.getApresentarFiltroFixoTipoOrigem());
					sqlAlterar.setBoolean(x++, obj.getApresentarFiltroFixoPeriodoLetivoCentroResultado());
					
					sqlAlterar.setBoolean(x++, obj.getApresentarFiltroFixoCentroResultado());
					sqlAlterar.setBoolean(x++, obj.getApresentarFiltroFixoNivelCentroResultado());
					sqlAlterar.setBoolean(x++, obj.getApresentarFiltroFixoCategoriaDespesa());
					
					sqlAlterar.setBoolean(x++, obj.getApresentarFiltroFixoFavorecido());
					sqlAlterar.setBoolean(x++, obj.getApresentarFiltroFixoSituacaoContaPagar());
					
					sqlAlterar.setBoolean(x++, obj.getApresentarFiltroFixoTipoRequerimento());
					sqlAlterar.setBoolean(x++, obj.getApresentarFiltroFixoResponsavel());
					sqlAlterar.setBoolean(x++, obj.getApresentarFiltroFixoTurmaReposicao());
					sqlAlterar.setBoolean(x++, obj.getApresentarFiltroFixoRequerente());
					sqlAlterar.setBoolean(x++, obj.getApresentarFiltroFixoCoordenador());
					sqlAlterar.setBoolean(x++, obj.getApresentarFiltroFixoDepartamentoResponsavel());
					sqlAlterar.setBoolean(x++, obj.getApresentarFiltroFixoSituacaoTramite());
					sqlAlterar.setBoolean(x++, obj.getApresentarFiltroFixoSituacaoRequerimento());
					sqlAlterar.setBoolean(x++, obj.getApresentarFiltroFixoSituacaoFinanceiraRequerimento());
					
					sqlAlterar.setBoolean(x++, obj.getApresentarFiltroFixoBiblioteca());
					sqlAlterar.setBoolean(x++, obj.getApresentarFiltroFixoTipoCatalogo());
					sqlAlterar.setBoolean(x++, obj.getApresentarFiltroFixoClassificacaoBibliografica());
					sqlAlterar.setBoolean(x++, obj.getApresentarFiltroFixoTitulo());
					sqlAlterar.setBoolean(x++, obj.getApresentarFiltroFixoSecao());
					sqlAlterar.setBoolean(x++, obj.getApresentarFiltroFixoAreaConhecimento());
					sqlAlterar.setBoolean(x++, obj.getApresentarFiltroFixoFormaEntrada());
					sqlAlterar.setBoolean(x++, obj.getApresentarFiltroFixoDataInicioAquisicao());
					sqlAlterar.setBoolean(x++, obj.getApresentarFiltroFixoDataFimAquisicao());
					sqlAlterar.setBoolean(x++, obj.getApresentarFiltroFixoTipo());
					sqlAlterar.setBoolean(x++, obj.getApresentarFiltroFixoCatalogoPeriodico());
					sqlAlterar.setBoolean(x++, obj.getApresentarFiltroFixoSituacaoEmprestimo());
					sqlAlterar.setBoolean(x++, obj.getApresentarFiltroFixoDataInicioEmprestimo());
					sqlAlterar.setBoolean(x++, obj.getApresentarFiltroFixoDataFimEmprestimo());
					
					sqlAlterar.setBoolean(x++, obj.getApresentarFiltroFixoProcessoSeletivo());
					sqlAlterar.setBoolean(x++, obj.getApresentarFiltroFixoDataProvaInicio());
					sqlAlterar.setBoolean(x++, obj.getApresentarFiltroFixoDataProvaFim());
					sqlAlterar.setBoolean(x++, obj.getApresentarFiltroFixoSituacaoResultadoProcessoSeletivo());
					sqlAlterar.setBoolean(x++, obj.getApresentarFiltroFixoSituacaoInscricao());
					sqlAlterar.setBoolean(x++, obj.getApresentarFiltroFixoSituacaoFinanceiraProcessoSeletivo());
					
					sqlAlterar.setBoolean(x++, obj.getApresentarFiltroFixoCampanha());
					sqlAlterar.setBoolean(x++, obj.getApresentarFiltroFixoConsultor());
					
					sqlAlterar.setBoolean(x++, obj.getApresentarFiltroFixoComponenteEstagio());
					sqlAlterar.setBoolean(x++, obj.getApresentarFiltroFixoSituacaoEstagio());
					sqlAlterar.setBoolean(x++, obj.getApresentarFiltroFixoPeriodoEstagio());
					
					return sqlAlterar;
				}
			});
			getFacadeFactory().getLayoutRelatorioSEIDecidirCampoInterfaceFacade().persistirLayoutRelatorioSeiDecidirCampoVOs(obj, verificarAcesso, usuarioVO);
			getFacadeFactory().getFiltroPersonalizadoFacade().alterarFiltroPersonalizado(obj.getCodigo(), OrigemFiltroPersonalizadoEnum.SEI_DECIDIR, obj.getListaFiltroPersonalizadoVOs(), usuarioVO);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(LayoutRelatorioSEIDecidirVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj);
		try {
			LayoutRelatorioSEIDecidir.excluir(getIdEntidade(), verificarAcesso, usuarioVO);

			excluirLayoutRelatorioSEIDecidirArquivo(obj, usuarioVO);

			getFacadeFactory().getLayoutRelatorioSEIDecidirPerfilAcessoInterfaceFacade().excluirPorLayoutRelatorioSeiDecidir(obj, false, usuarioVO);
			getFacadeFactory().getLayoutRelatorioSEIDecidirFuncionarioInterfaceFacade().excluirPorLayoutRelatorioSeiDecidir(obj, false, usuarioVO);
			getFacadeFactory().getFiltroPersonalizadoFacade().excluirFiltroPersonalizadoPorCodigoOrigemEOrigem(obj.getCodigo(), OrigemFiltroPersonalizadoEnum.SEI_DECIDIR, usuarioVO);

			getFacadeFactory().getLayoutRelatorioSEIDecidirCampoInterfaceFacade().excluirLayoutRelatorioSEIDecidir(obj.getCodigo(), verificarAcesso, usuarioVO);
			StringBuilder sql = new StringBuilder();
			sql.append("DELETE FROM layoutrelatorioseidecidir WHERE (codigo = ?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			getConexao().getJdbcTemplate().update(sql.toString(), obj.getCodigo());
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Excluir o arquivo do Banco de dados e do diretório.
	 * 
	 * @param obj
	 * @param usuarioVO
	 * @throws Exception
	 */
	public void excluirLayoutRelatorioSEIDecidirArquivo(LayoutRelatorioSEIDecidirVO obj, UsuarioVO usuarioVO) throws Exception {
		if (Uteis.isAtributoPreenchido(obj.getListaRelatorioSEIDecidirArquivo())) {
			for (LayoutRelatorioSEIDecidirArquivoVO layoutRelatorioSEIDecidirArquivoVO : obj.getListaRelatorioSEIDecidirArquivo()) {
				getFacadeFactory().getLayoutRelatorioSEIDecidirArquivoInterfaceFacade().excluir(layoutRelatorioSEIDecidirArquivoVO, false, usuarioVO);
				
				if (Uteis.isAtributoPreenchido(layoutRelatorioSEIDecidirArquivoVO.getArquivoVO())) {
					ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoPadraoSistema();
					getFacadeFactory().getArquivoFacade().excluir(layoutRelatorioSEIDecidirArquivoVO.getArquivoVO(), usuarioVO, configuracaoGeralSistemaVO);
				}
			}
		}
	}

	@Override
	public List<LayoutRelatorioSEIDecidirVO> consultar(String valorConsulta, String campoConsulta, boolean verificarAcesso, UsuarioVO usuarioVO, LayoutRelatorioSEIDecidirVO layoutRelatorioSEIDecidirVO, int nivelMontarDados, String modulo) throws Exception {
		List<LayoutRelatorioSEIDecidirVO> objs = new ArrayList<LayoutRelatorioSEIDecidirVO>();
		//validarDadosConsulta(valorConsulta);
		if (campoConsulta.equals("descricao")) {
			return objs = consultarPorDescricaoModulo(valorConsulta, verificarAcesso, usuarioVO, nivelMontarDados, modulo);
		}
		return objs;
	}

	@Override
	public List<LayoutRelatorioSEIDecidirVO> consultarTodos(boolean verificarAcesso, UsuarioVO usuarioVO, int nivelMontarDados) throws Exception {
		StringBuilder sqlStr = new StringBuilder("SELECT * FROM layoutrelatorioseidecidir");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuarioVO);
	}

	public List<LayoutRelatorioSEIDecidirVO> consultarPorDescricao(String descricao, boolean verificarAcesso, UsuarioVO usuarioVO, int nivelMontarDados) throws Exception {
		StringBuilder sqlStr = new StringBuilder("SELECT * FROM layoutrelatorioseidecidir WHERE descricao ilike(?) order by descricao");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), descricao + PERCENT);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuarioVO);
	}

	public List<LayoutRelatorioSEIDecidirVO> consultarPorDescricaoModulo(String descricao, boolean verificarAcesso, UsuarioVO usuarioVO, int nivelMontarDados, String modulo) throws Exception {
		List<Object> listaFiltro = new ArrayList<>();
		StringBuilder sqlStr = new StringBuilder("SELECT * FROM layoutrelatorioseidecidir WHERE unaccent(descricao) ilike(?) ");
		listaFiltro.add(Uteis.removerAcentos(descricao) + PERCENT);
		if (Uteis.isAtributoPreenchido(modulo) && !modulo.equals("TODOS")) {
			sqlStr.append(" and modulo = ?");
			listaFiltro.add(modulo);
		}
		sqlStr.append(" order by descricao");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), listaFiltro.toArray());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuarioVO);
	}

	@Override
	public LayoutRelatorioSEIDecidirVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder("SELECT * FROM layoutrelatorioseidecidir WHERE codigo = ?");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), codigo);
		if (tabelaResultado.next()) {
			return montarDados(tabelaResultado, nivelMontarDados, usuarioVO);
		}
		return new LayoutRelatorioSEIDecidirVO();
	}

	public List<LayoutRelatorioSEIDecidirVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		List<LayoutRelatorioSEIDecidirVO> layoutRelatorioSEIDecidirVOs = new ArrayList<LayoutRelatorioSEIDecidirVO>(0);
		while (tabelaResultado.next()) {
			layoutRelatorioSEIDecidirVOs.add(montarDados(tabelaResultado, nivelMontarDados, usuarioVO));
		}
		return layoutRelatorioSEIDecidirVOs;
	}

	public LayoutRelatorioSEIDecidirVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		LayoutRelatorioSEIDecidirVO obj = new LayoutRelatorioSEIDecidirVO();
		obj.setNovoObj(false);
		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.setDescricao(tabelaResultado.getString("descricao"));
		obj.setAgruparRelatorioPor(tabelaResultado.getString("agruparRelatorioPor"));
		obj.setCondicaoWhereAdicional(tabelaResultado.getString("condicaoWhereAdicional"));
		obj.setCondicaoJoinAdicional(tabelaResultado.getString("condicaoJoinAdicional"));
		obj.setGroupByAdicional(tabelaResultado.getString("groupByAdicional"));
		obj.setOrderByAdicional(tabelaResultado.getString("orderByAdicional"));
		obj.setNomePadraoArquivo(tabelaResultado.getString("nomePadraoArquivo"));
		obj.setTipoGeracaoRelatorio(TipoRelatorioEnum.valueOf(tabelaResultado.getString("tipoGeracaoRelatorio")));
		obj.setSubRelatorio(tabelaResultado.getBoolean("subRelatorio"));
		obj.setSubRelatorioCrossTab(tabelaResultado.getBoolean("subRelatorioCrossTab"));
		obj.setTextoCabecalho(tabelaResultado.getString("textoCabecalho"));
		obj.setLayoutPersonalizado(tabelaResultado.getBoolean("layoutPersonalizado"));
		obj.setQueryLayoutPersonalizado(tabelaResultado.getString("queryLayoutPersonalizado"));
		
		obj.setApresentarFiltroFixoUnidadeEnsino(tabelaResultado.getBoolean("apresentarFiltroFixoUnidadeEnsino"));
		obj.setApresentarFiltroFixoNivelEducacional(tabelaResultado.getBoolean("apresentarFiltroFixoNivelEducacional"));
		obj.setApresentarFiltroFixoCotaIngresso(tabelaResultado.getBoolean("ApresentarFiltroFixoCotaIngresso"));
		obj.setApresentarFiltroFixoCurso(tabelaResultado.getBoolean("apresentarFiltroFixoCurso"));
		obj.setApresentarFiltroFixoTurno(tabelaResultado.getBoolean("apresentarFiltroFixoTurno"));
		obj.setApresentarFiltroFixoTurma(tabelaResultado.getBoolean("apresentarFiltroFixoTurma"));
		obj.setApresentarFiltroFixoMatricula(tabelaResultado.getBoolean("apresentarFiltroFixoMatricula"));
		obj.setApresentarFiltroFixoTurmaEstudouDisciplina(tabelaResultado.getBoolean("apresentarFiltroFixoTurmaEstudouDisciplina"));
		obj.setApresentarFiltroFixoDisciplina(tabelaResultado.getBoolean("apresentarFiltroFixoDisciplina"));
		obj.setApresentarFiltroFixoFiltroPeriodo(tabelaResultado.getBoolean("apresentarFiltroFixoFiltroPeriodo")); 
		obj.setApresentarFiltroFixoPeriodoLetivo(tabelaResultado.getBoolean("ApresentarFiltroFixoPeriodoLetivo"));
		obj.setApresentarFiltroFixoSituacaoAceiteEletronicoContrato(tabelaResultado.getBoolean("ApresentarFiltroFixoSituacaoAceiteEletronicoContrato"));
		obj.setApresentarFiltroFixoSituacaoFiltroSituacaoAcademica(tabelaResultado.getBoolean("ApresentarFiltroFixoSituacaoFiltroSituacaoAcademica"));
		obj.setApresentarFiltroFixoSituacaoFinanceiraMatricula(tabelaResultado.getBoolean("ApresentarFiltroFixoSituacaoFinanceiraMatricula"));
		
		obj.setApresentarFiltroFixoCentroReceita(tabelaResultado.getBoolean("apresentarFiltroFixoCentroReceita"));
		obj.setApresentarFiltroFixoFormaPagamento(tabelaResultado.getBoolean("apresentarFiltroFixoFormaPagamento"));
		obj.setApresentarFiltroFixoContaCorrente(tabelaResultado.getBoolean("apresentarFiltroFixoContaCorrente"));
		obj.setApresentarFiltroFixoTipoPessoa(tabelaResultado.getBoolean("apresentarFiltroFixoTipoPessoa"));
		obj.setApresentarFiltroFixoConsiderarUnidadeFinanceira(tabelaResultado.getBoolean("apresentarFiltroFixoConsiderarUnidadeFinanceira"));
		obj.setApresentarFiltroFixoContaCorrenteRecebimento(tabelaResultado.getBoolean("apresentarFiltroFixoContaCorrenteRecebimento"));
		obj.setApresentarFiltroFixoSituacaoContaReceber(tabelaResultado.getBoolean("apresentarFiltroFixoSituacaoContaReceber"));
		obj.setApresentarFiltroFixoTipoOrigem(tabelaResultado.getBoolean("apresentarFiltroFixoTipoOrigem"));
		obj.setApresentarFiltroFixoPeriodoLetivoCentroResultado(tabelaResultado.getBoolean("apresentarFiltroFixoPeriodoLetivoCentroResultado"));
		
		obj.setApresentarFiltroFixoCentroResultado(tabelaResultado.getBoolean("apresentarFiltroFixoCentroResultado"));
		obj.setApresentarFiltroFixoNivelCentroResultado(tabelaResultado.getBoolean("apresentarFiltroFixoNivelCentroResultado"));
		obj.setApresentarFiltroFixoCategoriaDespesa(tabelaResultado.getBoolean("apresentarFiltroFixoCategoriaDespesa"));
		
		obj.setApresentarFiltroFixoFavorecido(tabelaResultado.getBoolean("apresentarFiltroFixoFavorecido"));
		obj.setApresentarFiltroFixoSituacaoContaPagar(tabelaResultado.getBoolean("apresentarFiltroFixoSituacaoContaPagar"));
		
		obj.setApresentarFiltroFixoTipoRequerimento(tabelaResultado.getBoolean("apresentarFiltroFixoTipoRequerimento"));
		obj.setApresentarFiltroFixoResponsavel(tabelaResultado.getBoolean("apresentarFiltroFixoResponsavel"));
		obj.setApresentarFiltroFixoTurmaReposicao(tabelaResultado.getBoolean("apresentarFiltroFixoTurmaReposicao"));
		obj.setApresentarFiltroFixoRequerente(tabelaResultado.getBoolean("apresentarFiltroFixoRequerente"));
		obj.setApresentarFiltroFixoCoordenador(tabelaResultado.getBoolean("apresentarFiltroFixoCoordenador"));
		obj.setApresentarFiltroFixoDepartamentoResponsavel(tabelaResultado.getBoolean("apresentarFiltroFixoDepartamentoResponsavel"));
		obj.setApresentarFiltroFixoSituacaoTramite(tabelaResultado.getBoolean("apresentarFiltroFixoSituacaoTramite"));
		obj.setApresentarFiltroFixoSituacaoRequerimento(tabelaResultado.getBoolean("apresentarFiltroFixoSituacaoRequerimento"));
		obj.setApresentarFiltroFixoSituacaoFinanceiraRequerimento(tabelaResultado.getBoolean("apresentarFiltroFixoSituacaoFinanceiraRequerimento"));
		
		obj.setApresentarFiltroFixoBiblioteca(tabelaResultado.getBoolean("apresentarFiltroFixoBiblioteca"));
		obj.setApresentarFiltroFixoTipoCatalogo(tabelaResultado.getBoolean("apresentarFiltroFixoTipoCatalogo"));
		obj.setApresentarFiltroFixoClassificacaoBibliografica(tabelaResultado.getBoolean("apresentarFiltroFixoClassificacaoBibliografica"));
		obj.setApresentarFiltroFixoTitulo(tabelaResultado.getBoolean("apresentarFiltroFixoTitulo"));
		obj.setApresentarFiltroFixoSecao(tabelaResultado.getBoolean("apresentarFiltroFixoSecao"));
		obj.setApresentarFiltroFixoAreaConhecimento(tabelaResultado.getBoolean("apresentarFiltroFixoAreaConhecimento"));
		obj.setApresentarFiltroFixoFormaEntrada(tabelaResultado.getBoolean("apresentarFiltroFixoFormaEntrada"));
		obj.setApresentarFiltroFixoDataInicioAquisicao(tabelaResultado.getBoolean("apresentarFiltroFixoDataInicioAquisicao"));
		obj.setApresentarFiltroFixoDataFimAquisicao(tabelaResultado.getBoolean("apresentarFiltroFixoDataFimAquisicao"));
		obj.setApresentarFiltroFixoTipo(tabelaResultado.getBoolean("apresentarFiltroFixoTipo"));
		obj.setApresentarFiltroFixoCatalogoPeriodico(tabelaResultado.getBoolean("apresentarFiltroFixoCatalogoPeriodico"));
		obj.setApresentarFiltroFixoSituacaoEmprestimo(tabelaResultado.getBoolean("apresentarFiltroFixoSituacaoEmprestimo"));
		obj.setApresentarFiltroFixoDataInicioEmprestimo(tabelaResultado.getBoolean("apresentarFiltroFixoDataInicioEmprestimo"));
		obj.setApresentarFiltroFixoDataFimEmprestimo(tabelaResultado.getBoolean("apresentarFiltroFixoDataFimEmprestimo"));
		
		obj.setApresentarFiltroFixoProcessoSeletivo(tabelaResultado.getBoolean("apresentarFiltroFixoProcessoSeletivo"));
		obj.setApresentarFiltroFixoDataProvaInicio(tabelaResultado.getBoolean("apresentarFiltroFixoDataProvaInicio"));
		obj.setApresentarFiltroFixoDataProvaFim(tabelaResultado.getBoolean("apresentarFiltroFixoDataProvaFim"));
		obj.setApresentarFiltroFixoSituacaoResultadoProcessoSeletivo(tabelaResultado.getBoolean("apresentarFiltroFixoSituacaoResultadoProcessoSeletivo"));
		obj.setApresentarFiltroFixoSituacaoInscricao(tabelaResultado.getBoolean("apresentarFiltroFixoSituacaoInscricao"));
		obj.setApresentarFiltroFixoSituacaoFinanceiraProcessoSeletivo(tabelaResultado.getBoolean("apresentarFiltroFixoSituacaoFinanceiraProcessoSeletivo"));
		
		obj.setApresentarFiltroFixoCampanha(tabelaResultado.getBoolean("apresentarFiltroFixoCampanha"));
		obj.setApresentarFiltroFixoConsultor(tabelaResultado.getBoolean("apresentarFiltroFixoConsultor"));
		
		obj.setApresentarFiltroFixoComponenteEstagio(tabelaResultado.getBoolean("apresentarFiltroFixoComponenteEstagio"));
		obj.setApresentarFiltroFixoSituacaoEstagio(tabelaResultado.getBoolean("apresentarFiltroFixoSituacaoEstagio"));
		obj.setApresentarFiltroFixoPeriodoEstagio(tabelaResultado.getBoolean("apresentarFiltroFixoPeriodoEstagio"));
		
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
			return obj;
		}
		obj.setTagAberturaGeralXml(tabelaResultado.getString("tagAberturaGeralXml"));
		obj.setTagFechamentoGeralXml(tabelaResultado.getString("tagFechamentoGeralXml"));
		obj.setTagAberturaLinhaXml(tabelaResultado.getString("tagAberturaLinhaXml"));
		obj.setTagFechamentoLinhaXml(tabelaResultado.getString("tagFechamentoLinhaXml"));
		if (Uteis.isAtributoPreenchido(tabelaResultado.getString("modulo"))) {
			obj.setModulo(RelatorioSEIDecidirModuloEnum.valueOf(tabelaResultado.getString("modulo")));
		}
		if (Uteis.isAtributoPreenchido(tabelaResultado.getString("niveldetalhamento"))) {
			obj.setNivelDetalhamento(RelatorioSEIDecidirNivelDetalhamentoEnum.valueOf(tabelaResultado.getString("niveldetalhamento")));
		}
		obj.setGerarLinhaEmBranco(tabelaResultado.getBoolean("gerarLinhaEmBranco"));
		obj.setListaFiltroPersonalizadoVOs(getFacadeFactory().getFiltroPersonalizadoFacade().consultarPorOrigemCodigoOrigem(obj.getCodigo(), OrigemFiltroPersonalizadoEnum.SEI_DECIDIR, usuarioVO));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_TODOS) {
			obj.setLayoutRelatorioSeiDecidirCampoVOs(getFacadeFactory().getLayoutRelatorioSEIDecidirCampoInterfaceFacade().consultarPorLayoutRelatorio(obj, Uteis.NIVELMONTARDADOS_TODOS, false, usuarioVO));
		}
		return obj;
	}

	@Override
	public List<LayoutRelatorioSEIDecidirVO> consultarPorModulo(RelatorioSEIDecidirModuloEnum modulo, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		LayoutRelatorioSEIDecidir.consultar(getIdEntidade(), controlarAcesso, usuarioVO);
		StringBuilder sql = new StringBuilder("select * from layoutrelatorioseidecidir ");
		if (modulo != null) {
			sql.append(" where modulo = '").append(modulo.name()).append("' ");
		}
		sql.append(" and subrelatorio = false ");
		if(Uteis.isAtributoPreenchido(usuarioVO.getPessoa())) {
			sql.append(" and ( ");
			sql.append(" (not exists (select LayoutRelatorioSEIDecidirPerfilAcesso.codigo from LayoutRelatorioSEIDecidirPerfilAcesso where LayoutRelatorioSEIDecidirPerfilAcesso.layoutrelatorioseidecidir = layoutrelatorioseidecidir.codigo) ");
			sql.append(" and not exists (select LayoutRelatorioSEIDecidirFuncionario.codigo from LayoutRelatorioSEIDecidirFuncionario where LayoutRelatorioSEIDecidirFuncionario.layoutrelatorioseidecidir = layoutrelatorioseidecidir.codigo)) ");
			sql.append(" or exists (select LayoutRelatorioSEIDecidirPerfilAcesso.codigo from LayoutRelatorioSEIDecidirPerfilAcesso where LayoutRelatorioSEIDecidirPerfilAcesso.layoutrelatorioseidecidir = layoutrelatorioseidecidir.codigo ");
			sql.append(" and LayoutRelatorioSEIDecidirPerfilAcesso.perfilacesso in (select UsuarioPerfilAcesso.perfilAcesso from UsuarioPerfilAcesso where usuario =  ").append(usuarioVO.getCodigo()).append(")) ");
			sql.append(" or exists (select LayoutRelatorioSEIDecidirFuncionario.codigo from LayoutRelatorioSEIDecidirFuncionario where LayoutRelatorioSEIDecidirFuncionario.layoutrelatorioseidecidir = layoutrelatorioseidecidir.codigo ");		
			sql.append(" and LayoutRelatorioSEIDecidirFuncionario.funcionario in (select f.codigo from funcionario as f where f.pessoa = ").append(usuarioVO.getPessoa().getCodigo()).append(")) ");
			
			sql.append(" ) ");
		}
		sql.append(" order by descricao ");
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()), nivelMontarDados, usuarioVO);
	}
	
	@Override
	public List<LayoutRelatorioSEIDecidirVO> consultarPorModuloENivelDetalhamento(RelatorioSEIDecidirModuloEnum modulo,
			RelatorioSEIDecidirNivelDetalhamentoEnum nivelDetalhamento, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
	
		StringBuilder sql = new StringBuilder("select * from layoutrelatorioseidecidir ");
		sql.append(" where 1 = 1");
		if (modulo != null) {
			sql.append(" and modulo = '").append(modulo.name()).append("' ");
		}
		
		if (Uteis.isAtributoPreenchido(nivelDetalhamento)) {
			sql.append(" and niveldetalhamento = '").append(nivelDetalhamento.name()).append("' ");
		}
		sql.append(" and subrelatorio = false ");
		sql.append(" order by descricao ");
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()), nivelMontarDados, usuarioVO);
	}

	@Override
	public void alterarOrdemApresentacaoCampo(LayoutRelatorioSEIDecidirVO layoutRelatorioSEIDecidirVO, LayoutRelatorioSeiDecidirCampoVO obj, LayoutRelatorioSeiDecidirCampoVO obj2) {
		int ordemInicial = obj.getOrdemApresentacao();
		int ordemFinal = obj2.getOrdemApresentacao();		
		if (ordemInicial != ordemFinal) {
			if(ordemInicial > ordemFinal){
				layoutRelatorioSEIDecidirVO.getLayoutRelatorioSeiDecidirCampoVOs().get(ordemInicial-1).setOrdemApresentacao(ordemFinal);
				ordemInicial--;
				while(ordemInicial >=  ordemFinal){
					layoutRelatorioSEIDecidirVO.getLayoutRelatorioSeiDecidirCampoVOs().get(ordemInicial-1).setOrdemApresentacao(ordemInicial+1);
					ordemInicial--;
				}	
			}else if(ordemInicial < ordemFinal){				
				layoutRelatorioSEIDecidirVO.getLayoutRelatorioSeiDecidirCampoVOs().get(ordemInicial-1).setOrdemApresentacao(ordemFinal);
				ordemInicial++;
				while(ordemInicial <=  ordemFinal){
					layoutRelatorioSEIDecidirVO.getLayoutRelatorioSeiDecidirCampoVOs().get(ordemInicial-1).setOrdemApresentacao(ordemInicial-1);
					ordemInicial++;
				}						
			}
			Ordenacao.ordenarLista(layoutRelatorioSEIDecidirVO.getLayoutRelatorioSeiDecidirCampoVOs(), "ordemApresentacao");
		}		
	}

	@Override
	public void realizarAlteracaoOrdemLayoutRelatorioSeiDecidirCampoVOs(LayoutRelatorioSEIDecidirVO layoutRelatorioSEIDecidirVO, LayoutRelatorioSeiDecidirCampoVO obj, boolean esquerda) throws Exception {
		if (esquerda && obj.getOrdemApresentacao() > 1) {
			layoutRelatorioSEIDecidirVO.getLayoutRelatorioSeiDecidirCampoVOs().get(obj.getOrdemApresentacao() - 2).setOrdemApresentacao(obj.getOrdemApresentacao());
			obj.setOrdemApresentacao(obj.getOrdemApresentacao() - 1);
		} else if (!esquerda && obj.getOrdemApresentacao() < layoutRelatorioSEIDecidirVO.getLayoutRelatorioSeiDecidirCampoVOs().size()) {
			layoutRelatorioSEIDecidirVO.getLayoutRelatorioSeiDecidirCampoVOs().get(obj.getOrdemApresentacao()).setOrdemApresentacao(obj.getOrdemApresentacao());
			obj.setOrdemApresentacao(obj.getOrdemApresentacao() + 1);
		}
		Ordenacao.ordenarLista(layoutRelatorioSEIDecidirVO.getLayoutRelatorioSeiDecidirCampoVOs(), "ordemApresentacao");
	}

	@Override
	public void adicionarLayoutRelatorioSeiDecidirCampoVOs(LayoutRelatorioSEIDecidirVO layoutRelatorioSEIDecidirVO, LayoutRelatorioSeiDecidirCampoVO obj) throws Exception {
		LayoutRelatorioSeiDecidirCampo.validarDados(obj);
		if (obj.getOrdemApresentacao() > 0) {
			layoutRelatorioSEIDecidirVO.getLayoutRelatorioSeiDecidirCampoVOs().set(obj.getOrdemApresentacao() - 1, obj);
		} else {
			obj.setOrdemApresentacao(layoutRelatorioSEIDecidirVO.getLayoutRelatorioSeiDecidirCampoVOs().size() + 1);
			layoutRelatorioSEIDecidirVO.getLayoutRelatorioSeiDecidirCampoVOs().add(obj);
		}
	}

	@Override
	public void removerLayoutRelatorioSeiDecidirCampoVOs(LayoutRelatorioSEIDecidirVO layoutRelatorioSEIDecidirVO, LayoutRelatorioSeiDecidirCampoVO obj) {
		layoutRelatorioSEIDecidirVO.getLayoutRelatorioSeiDecidirCampoVOs().remove(obj.getOrdemApresentacao() - 1);
		realizarReordenacaoLayoutRelatorioSeiDecidirCampoVO(layoutRelatorioSEIDecidirVO);
	}

	private void realizarReordenacaoLayoutRelatorioSeiDecidirCampoVO(LayoutRelatorioSEIDecidirVO layoutRelatorioSEIDecidirVO) {
		Integer ordemApresentacao = 1;
		for (LayoutRelatorioSeiDecidirCampoVO obj2 : layoutRelatorioSEIDecidirVO.getLayoutRelatorioSeiDecidirCampoVOs()) {
			obj2.setOrdemApresentacao(ordemApresentacao++);
		}
		Ordenacao.ordenarLista(layoutRelatorioSEIDecidirVO.getLayoutRelatorioSeiDecidirCampoVOs(), "ordemApresentacao");
	}
	
	@Override
	public String realizarExportacaoLayout(LayoutRelatorioSEIDecidirVO layoutRelatorioSEIDecidirVO) throws Exception{
		JAXBContext context = null;
		String nomeArquivo = "LAYOUT_" + layoutRelatorioSEIDecidirVO.getCodigo() + ".xml";
		File layout = null;
		Marshaller marshaller = null;
		try {
			context = JAXBContext.newInstance(LayoutRelatorioSEIDecidirVO.class);
			layout = new File(UteisJSF.getCaminhoWeb() + File.separator + "relatorio" + File.separator + nomeArquivo);
			marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(layoutRelatorioSEIDecidirVO, layout);
			return nomeArquivo;
		} catch (Exception e) {
			throw e;
		} finally {
			context = null;
			layout = null;
			marshaller = null;
		}
	}

	@Override
	public LayoutRelatorioSEIDecidirVO realizarImportacaoLayout(FileUploadEvent uploadEvent) throws Exception{
		LayoutRelatorioSEIDecidirVO layoutRelatorioSEIDecidirVO = null;
		JAXBContext context = null;
		Unmarshaller unMarshaller = null;
		try{
			context = JAXBContext.newInstance(LayoutRelatorioSEIDecidirVO.class);
			unMarshaller = context.createUnmarshaller();
			layoutRelatorioSEIDecidirVO = (LayoutRelatorioSEIDecidirVO) unMarshaller.unmarshal(uploadEvent.getUploadedFile().getInputStream());			
		}catch(Exception e){
			throw e;
		}finally{
			uploadEvent = null;
			context = null;
			unMarshaller = null;
		}
		return layoutRelatorioSEIDecidirVO;
	}
	
	@Override
    public void alterarOrdemFiltroPersonalizado(LayoutRelatorioSEIDecidirVO layoutRelatorioSEIDecidirVO, FiltroPersonalizadoVO filtroPersonalizadoVO1, FiltroPersonalizadoVO filtroPersonalizadoVO2) throws Exception {
        int ordem1 = filtroPersonalizadoVO1.getOrdem();
        filtroPersonalizadoVO1.setOrdem(filtroPersonalizadoVO2.getOrdem());
        filtroPersonalizadoVO2.setOrdem(ordem1);        
        Ordenacao.ordenarLista(layoutRelatorioSEIDecidirVO.getListaFiltroPersonalizadoVOs(), "ordem");
    }
	

	public static String getIdEntidade() {
		if (idEntidade == null) {
			idEntidade = "";
		}
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		LayoutRelatorioSEIDecidir.idEntidade = idEntidade;
	}
}
