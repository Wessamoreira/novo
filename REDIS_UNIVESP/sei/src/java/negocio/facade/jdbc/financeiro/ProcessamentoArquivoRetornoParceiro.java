package negocio.facade.jdbc.financeiro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.CentroResultadoOrigemVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaPagarNegociacaoPagamentoVO;
import negocio.comuns.financeiro.ContaPagarVO;
import negocio.comuns.financeiro.ContaReceberNegociacaoRecebimentoVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.FormaPagamentoNegociacaoPagamentoVO;
import negocio.comuns.financeiro.FormaPagamentoNegociacaoRecebimentoVO;
import negocio.comuns.financeiro.NegociacaoPagamentoVO;
import negocio.comuns.financeiro.NegociacaoRecebimentoVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.financeiro.ProcessamentoArquivoRetornoParceiroAlunoVO;
import negocio.comuns.financeiro.ProcessamentoArquivoRetornoParceiroExcelVO;
import negocio.comuns.financeiro.ProcessamentoArquivoRetornoParceiroVO;
import negocio.comuns.financeiro.enumerador.SituacaoProcessamentoArquivoRetornoEnum;
import negocio.comuns.financeiro.enumerador.TipoNivelCentroResultadoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.OrigemContaPagar;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.ProcessamentoArquivoRetornoParceiroInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class ProcessamentoArquivoRetornoParceiro extends ControleAcesso implements ProcessamentoArquivoRetornoParceiroInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 119024424139918377L;

	protected static String idEntidade;

	public ProcessamentoArquivoRetornoParceiro() throws Exception {
		super();
		setIdEntidade("ProcessamentoArquivoRetornoParceiro");
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe
	 * <code>ProcessamentoArquivoRetornoParceiroVO</code>.
	 */
	public ProcessamentoArquivoRetornoParceiroVO novo() throws Exception {
		ProcessamentoArquivoRetornoParceiro.incluir(getIdEntidade());
		ProcessamentoArquivoRetornoParceiroVO obj = new ProcessamentoArquivoRetornoParceiroVO();
		return obj;
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public static void validarDados(ProcessamentoArquivoRetornoParceiroVO obj) throws ConsistirException {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}

		if (!Uteis.isAtributoPreenchido(obj.getParceiroVO())) {
			throw new ConsistirException("O campo PARCEIRO deve ser informado.");
		}
		if (!Uteis.isAtributoPreenchido(obj.getUnidadeEnsinoVO())) {
			throw new ConsistirException("O campo UNIDADE ENSINO deve ser informado.");
		}

		if (!Uteis.isAtributoPreenchido(obj.getDataRepasse())) {
			throw new ConsistirException("O campo DATA REPASSE deve ser informado.");
		}

	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void realizarLeituraArquivoExcel(FileUploadEvent upload, ProcessamentoArquivoRetornoParceiroVO obj, UsuarioVO usuarioLogado) throws Exception {
		try {
			validarDados(obj);
			String extensao = (upload.getUploadedFile().getName().substring((upload.getUploadedFile().getName().lastIndexOf(".") + 1), upload.getUploadedFile().getName().length()));
			if (extensao.equals("xlsx")) {
				obj.limparCampos();
				obj.setNomeArquivo(Uteis.getNomeArquivo(upload.getUploadedFile().getName()));
				boolean planilhaSeiExiste = false;
				XSSFWorkbook workbook = new XSSFWorkbook(upload.getUploadedFile().getInputStream());
				for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
					if (workbook.getSheetName(i).trim().equals("SEI")) {
						planilhaSeiExiste = true;
						realizarLeituraPlanilha(workbook.getSheetAt(i), obj, usuarioLogado);
					}
				}
				if (!planilhaSeiExiste) {
					throw new Exception("O arquivo informado possui mais de uma planilha. E não encontrou nenhuma com o nome \"SEI\".");
				}
				validarContasReceberBolsaCusteadoConvenioQueNaoEstaoNoArquivo(obj, usuarioLogado);
			} else {
				throw new Exception("Extensão do arquivo não é valida. Somente arquivos em excel(.xlsx) serão processados.");
			}
			extensao = null;
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private void realizarLeituraPlanilha(XSSFSheet planilha, ProcessamentoArquivoRetornoParceiroVO obj, UsuarioVO usuarioLogado) throws Exception {
		for (int i = 0; i <= planilha.getLastRowNum(); i++) {
			if (planilha.getRow(i).getRowNum() == 0) {
				executarValidacaoCabecarioExcel(planilha.getRow(i));
			} else {
				realizarLeituraLinhaAndColuna(planilha.getRow(0), planilha.getRow(i), obj, usuarioLogado);
			}
		}
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private void realizarLeituraLinhaAndColuna(XSSFRow cabecario, XSSFRow linha, ProcessamentoArquivoRetornoParceiroVO obj, UsuarioVO usuarioLogado) throws Exception {
		int ultimaColuna = linha.getLastCellNum() - 1;
		ProcessamentoArquivoRetornoParceiroAlunoVO processamentoAluno = new ProcessamentoArquivoRetornoParceiroAlunoVO();
		processamentoAluno.setCpf(linha.getCell(0).getStringCellValue());
		processamentoAluno.setProcessamentoArquivoRetornoParceiroVO(obj);
		processamentoAluno.setValorRepasse(linha.getCell(ultimaColuna).getNumericCellValue());
		for (int i = 1; i < ultimaColuna; i++) {
			ProcessamentoArquivoRetornoParceiroExcelVO objExcel = new ProcessamentoArquivoRetornoParceiroExcelVO();
			XSSFCell colunaDataCompetencia = cabecario.getCell(i);
			XSSFCell colunaValorConta = linha.getCell(i);
			if (colunaValorConta.getCellType() == XSSFCell.CELL_TYPE_NUMERIC && colunaValorConta.getNumericCellValue() == 0.0) {
				continue;
			}
			if (colunaValorConta.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
				objExcel.setValorConta(colunaValorConta.getNumericCellValue());
			} else {
				throw new Exception("O valor informado para celula " + colunaValorConta.getReference() + " não é um número valido.");
			}
			if (colunaDataCompetencia.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
				objExcel.setDataCompetencia(colunaDataCompetencia.getDateCellValue());
				String dataCompentencia = Uteis.getDataMesAnoConcatenado(objExcel.getDataCompetencia());
				if (!obj.getDatasDeCompetencia().contains(dataCompentencia)) {
					obj.setDatasDeCompetencia(obj.getDatasDeCompetencia() + dataCompentencia + ";");
				}
			} else {
				throw new Exception("O valor informado para celula " + colunaDataCompetencia.getReference() + " não é mês de compentencia valido.");
			}
			objExcel.setProcessamentoArquivoRetornoParceiroAlunoVO(processamentoAluno);
			objExcel.setContaReceberVO(getFacadeFactory().getContaReceberFacade().consultarContaReceberProcessamentoArquivoParceiroExcel(objExcel, usuarioLogado));
			if (Uteis.isAtributoPreenchido(objExcel.getContaReceberVO())) {
				obj.getListaCodigoContaReceberEncontradosNoArquivo().add(objExcel.getContaReceberVO().getCodigo());
			}
			processamentoAluno.getListaContasAReceber().add(objExcel);

		}

		obj.separarProcessamentoArquivoRetornoParceiroExcelVOEmLista(processamentoAluno);
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void executarValidacaoCabecarioExcel(XSSFRow linha) throws Exception {
		if (!linha.getCell(linha.getFirstCellNum()).getStringCellValue().trim().equals("CPF")) {
			throw new Exception("O valor da referencia " + linha.getCell(linha.getFirstCellNum()).getReference() + " está errada. O valor deve ser CPF");
		}
		if (!linha.getCell(linha.getLastCellNum() - 1).getStringCellValue().trim().equals("Valor Repasse")) {
			throw new Exception("O valor da referencia " + linha.getCell(linha.getFirstCellNum()).getReference() + " está errada. O valor deve ser Valor Repasse");
		}
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private void validarContasReceberBolsaCusteadoConvenioQueNaoEstaoNoArquivo(ProcessamentoArquivoRetornoParceiroVO obj, UsuarioVO usuarioLogado) throws Exception {
		List<ProcessamentoArquivoRetornoParceiroAlunoVO> listaAluno = getFacadeFactory().getProcessamentoArquivoRetornoParceiroAlunoFacade().consultarContaReceberBolsaCusteadaConvenioPorParceiroPorMesCompetencia(obj, usuarioLogado);
		for (ProcessamentoArquivoRetornoParceiroAlunoVO aluno : listaAluno) {
			aluno.setProcessamentoArquivoRetornoParceiroVO(obj);
			obj.separarProcessamentoArquivoRetornoParceiroExcelVOEmLista(aluno);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(ProcessamentoArquivoRetornoParceiroVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		if (obj.getCodigo() == 0) {
			incluir(obj, verificarAcesso, usuarioVO);
		} else {
			alterar(obj, verificarAcesso, usuarioVO);
		}
		
		List<ProcessamentoArquivoRetornoParceiroAlunoVO> listaTemp = new ArrayList<ProcessamentoArquivoRetornoParceiroAlunoVO>();
		listaTemp.addAll(obj.getListaContasAReceber());
		listaTemp.addAll(obj.getListaContasRecebidas());
		listaTemp.addAll(obj.getListaContasNaoLocalizada());
		listaTemp.addAll(obj.getListaContaValoresDivergente());
		listaTemp.addAll(obj.getListaContasMesCompetencia());
		validarSeRegistroForamExcluidoDasListaSubordinadas(listaTemp, "ProcessamentoArquivoRetornoParceiroAluno", "processamentoArquivoRetornoParceiro", obj.getCodigo(), usuarioVO);
		getFacadeFactory().getProcessamentoArquivoRetornoParceiroAlunoFacade().persistir(obj.getListaContasAReceber(), false, usuarioVO);
		getFacadeFactory().getProcessamentoArquivoRetornoParceiroAlunoFacade().persistir(obj.getListaContasRecebidas(), false, usuarioVO);
		getFacadeFactory().getProcessamentoArquivoRetornoParceiroAlunoFacade().persistir(obj.getListaContasNaoLocalizada(), false, usuarioVO);
		getFacadeFactory().getProcessamentoArquivoRetornoParceiroAlunoFacade().persistir(obj.getListaContaValoresDivergente(), false, usuarioVO);
		getFacadeFactory().getProcessamentoArquivoRetornoParceiroAlunoFacade().persistir(obj.getListaContasMesCompetencia(), false, usuarioVO);
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>ProcessamentoArquivoRetornoParceiroVO</code>. Primeiramente valida
	 * os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o
	 * banco de dados e a permissão do usuário para realizar esta operacão na
	 * entidade. Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe
	 *            <code>ProcessamentoArquivoRetornoParceiroVO</code> que será
	 *            gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ProcessamentoArquivoRetornoParceiroVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			ProcessamentoArquivoRetornoParceiro.incluir(getIdEntidade(), verificarAcesso, usuario);
			final StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO ProcessamentoArquivoRetornoParceiro (parceiro, unidadeEnsino, dataGeracao, dataRepasse, datasDeCompetencia, responsavel, ");
			sql.append("            contasBaixadas, nomeArquivo, qtdeLote, loteAtual, dataInicioProcessamento, dataTerminoProcessamento, situacaoProcessamento, ");
			sql.append("            motivoErroProcessamento, contapagar, gerarcontapagar ) ");
			sql.append("    VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ?)");
			sql.append(" returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql.toString());
					int i = 0;
					sqlInserir.setInt(++i, obj.getParceiroVO().getCodigo());
					sqlInserir.setInt(++i, obj.getUnidadeEnsinoVO().getCodigo());
					sqlInserir.setDate(++i, Uteis.getDataJDBC(obj.getDataGeracao()));
					sqlInserir.setDate(++i, Uteis.getDataJDBC(obj.getDataRepasse()));
					sqlInserir.setString(++i, obj.getDatasDeCompetencia());
					sqlInserir.setInt(++i, obj.getResponsavel().getCodigo());
					sqlInserir.setBoolean(++i, obj.getContasBaixadas());
					sqlInserir.setString(++i, obj.getNomeArquivo());
					sqlInserir.setInt(++i, obj.getQtdeLote());
					sqlInserir.setInt(++i, obj.getLoteAtual());
					sqlInserir.setDate(++i, Uteis.getDataJDBC(obj.getDataInicioProcessamento()));
					sqlInserir.setDate(++i, Uteis.getDataJDBC(obj.getDataTerminoProcessamento()));
					sqlInserir.setString(++i, obj.getSituacaoProcessamento().name());
					sqlInserir.setString(++i, obj.getMotivoErroProcessamento());
					if (Uteis.isAtributoPreenchido(obj.getContaPagarVO())) {
						sqlInserir.setInt(++i, obj.getContaPagarVO().getCodigo());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					sqlInserir.setBoolean(++i, obj.isGerarContaPagar());
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
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw e;
		}
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe
	 * <code>ProcessamentoArquivoRetornoParceiroVO</code>. Sempre utiliza a
	 * chave primária da classe como atributo para localização do registro a ser
	 * alterado. Primeiramente valida os dados (<code>validarDados</code>) do
	 * objeto. Verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade. Isto, através da operação
	 * <code>alterar</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe
	 *            <code>ProcessamentoArquivoRetornoParceiroVO</code> que será
	 *            alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ProcessamentoArquivoRetornoParceiroVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			ProcessamentoArquivoRetornoParceiro.alterar(getIdEntidade(), verificarAcesso, usuario);
			final StringBuilder sql = new StringBuilder();
			sql.append("UPDATE ProcessamentoArquivoRetornoParceiro ");
			sql.append("   SET parceiro=?, unidadeEnsino=?, dataGeracao=?, dataRepasse=?, datasDeCompetencia=?, responsavel=?, contasBaixadas=?, ");
			sql.append("       nomeArquivo=?, qtdeLote=?, loteAtual=?, dataInicioProcessamento=?, dataTerminoProcessamento=?, situacaoProcessamento=?, motivoErroProcessamento=?, ");
			sql.append("       contapagar=?, gerarcontapagar=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql.toString());
					int i = 0;
					sqlAlterar.setInt(++i, obj.getParceiroVO().getCodigo());
					sqlAlterar.setInt(++i, obj.getUnidadeEnsinoVO().getCodigo());
					sqlAlterar.setDate(++i, Uteis.getDataJDBC(obj.getDataGeracao()));
					sqlAlterar.setDate(++i, Uteis.getDataJDBC(obj.getDataRepasse()));
					sqlAlterar.setString(++i, obj.getDatasDeCompetencia());
					sqlAlterar.setInt(++i, obj.getResponsavel().getCodigo());
					sqlAlterar.setBoolean(++i, obj.getContasBaixadas());
					sqlAlterar.setString(++i, obj.getNomeArquivo());
					sqlAlterar.setInt(++i, obj.getQtdeLote());
					sqlAlterar.setInt(++i, obj.getLoteAtual());
					sqlAlterar.setDate(++i, Uteis.getDataJDBC(obj.getDataInicioProcessamento()));
					sqlAlterar.setDate(++i, Uteis.getDataJDBC(obj.getDataTerminoProcessamento()));
					sqlAlterar.setString(++i, obj.getSituacaoProcessamento().name());
					sqlAlterar.setString(++i, obj.getMotivoErroProcessamento());
					if (Uteis.isAtributoPreenchido(obj.getContaPagarVO())) {
						sqlAlterar.setInt(++i, obj.getContaPagarVO().getCodigo());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					sqlAlterar.setBoolean(++i, obj.isGerarContaPagar());
					sqlAlterar.setInt(++i, obj.getCodigo());
					return sqlAlterar;
				}
			}) == 0) {
				incluir(obj, false, usuario);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarRegistroInicioProcessamento(final Integer processamentoArquivo, final Integer qtdeLote, final Date dataInicioProcessamento, UsuarioVO usuario) throws Exception {
		try {
			final String sql = "UPDATE ProcessamentoArquivoRetornoParceiro set qtdeLote=?, situacaoProcessamento=?, dataInicioProcessamento=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setInt(1, qtdeLote);
					sqlAlterar.setString(2, SituacaoProcessamentoArquivoRetornoEnum.EM_PROCESSAMENTO.name());
					sqlAlterar.setTimestamp(3, Uteis.getDataJDBCTimestamp(dataInicioProcessamento));
					sqlAlterar.setInt(4, processamentoArquivo);
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarRegistroLoteEmProcessamento(final Integer processamentoArquivo, final Integer loteAtual, UsuarioVO usuario) throws Exception {
		final String sql = "UPDATE ProcessamentoArquivoRetornoParceiro set loteAtual = ? WHERE ((codigo = ?)) " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				sqlAlterar.setInt(1, loteAtual);
				sqlAlterar.setInt(2, processamentoArquivo);
				return sqlAlterar;
			}
		});
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void realizarAtualizacaoContaPagar(final ProcessamentoArquivoRetornoParceiroVO processamento, UsuarioVO usuario) throws Exception {
		final String sql = "UPDATE ProcessamentoArquivoRetornoParceiro set contapagar = ? WHERE ((codigo = ?)) " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				sqlAlterar.setInt(1, processamento.getContaPagarVO().getCodigo());
				sqlAlterar.setInt(2, processamento.getCodigo());
				return sqlAlterar;
			}
		});
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarRegistroErroProcessamento(final Integer processamentoArquivo, final String erro, final Date dataTerminoProcessamento, UsuarioVO usuario) throws Exception {
		final String sql = "UPDATE ProcessamentoArquivoRetornoParceiro set situacaoProcessamento=?, dataTerminoProcessamento=?, motivoErroProcessamento = ?, contasBaixadas=?  WHERE ((codigo = ?)) " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				sqlAlterar.setString(1, SituacaoProcessamentoArquivoRetornoEnum.ERRO_PROCESSAMENTO.name());
				sqlAlterar.setTimestamp(2, Uteis.getDataJDBCTimestamp(dataTerminoProcessamento));
				sqlAlterar.setString(3, erro);
				sqlAlterar.setBoolean(4, false);
				sqlAlterar.setInt(5, processamentoArquivo);
				return sqlAlterar;
			}
		});
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarRegistroTerminoProcessamento(final Integer processamentoArquivo, final Date dataTerminoProcessamento, UsuarioVO usuario) throws Exception {
		final String sql = "UPDATE ProcessamentoArquivoRetornoParceiro set situacaoProcessamento=?, dataTerminoProcessamento=?, contasBaixadas=? WHERE ((codigo = ?)) " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				sqlAlterar.setString(1, SituacaoProcessamentoArquivoRetornoEnum.PROCESSAMENTO_CONCLUIDO.name());
				sqlAlterar.setTimestamp(2, Uteis.getDataJDBCTimestamp(dataTerminoProcessamento));
				sqlAlterar.setBoolean(3, true);
				sqlAlterar.setInt(4, processamentoArquivo);
				return sqlAlterar;
			}
		});
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void baixarContasRegistroArquivo(ProcessamentoArquivoRetornoParceiroVO processamento, List<ProcessamentoArquivoRetornoParceiroAlunoVO> contaReceberArquivoVOs, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		for (ProcessamentoArquivoRetornoParceiroAlunoVO processamentoAluno : contaReceberArquivoVOs) {
			NegociacaoRecebimentoVO negociacaoRecebimentoVO = new NegociacaoRecebimentoVO();
			negociacaoRecebimentoVO.setResponsavel(usuario);
			negociacaoRecebimentoVO.setUnidadeEnsino(processamento.getUnidadeEnsinoVO());
			negociacaoRecebimentoVO.setContaCorrenteCaixa(processamento.getParceiroVO().getContaCorrenteDaUnidadeEnsino(processamento.getUnidadeEnsinoVO()));
			negociacaoRecebimentoVO.setData(processamento.getDataRepasse());
			negociacaoRecebimentoVO.setParceiroVO(processamento.getParceiroVO());
			negociacaoRecebimentoVO.setTipoPessoa(TipoPessoa.PARCEIRO.getValor());
			negociacaoRecebimentoVO.setValorTotalRecebimento(0.0);
			atualizarDadosContaReceberParaBaixa(processamento, processamentoAluno, negociacaoRecebimentoVO, configuracaoFinanceiroVO, usuario);
			preencherNegociacaoRecebimentoVO(negociacaoRecebimentoVO, processamento, processamentoAluno, configuracaoFinanceiroVO, usuario);
			preencherTotalContaPagar(processamento, processamentoAluno);
		}
		validaGeracaoContaPagar(processamento, usuario);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private void atualizarDadosContaReceberParaBaixa(ProcessamentoArquivoRetornoParceiroVO processamento, ProcessamentoArquivoRetornoParceiroAlunoVO processamentoAluno, NegociacaoRecebimentoVO negociacaoRecebimentoVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		int index = 0;
		for (ProcessamentoArquivoRetornoParceiroExcelVO processamentoContaReceber : processamentoAluno.getListaContasAReceber()) {
			getFacadeFactory().getContaReceberFacade().carregarDados(processamentoContaReceber.getContaReceberVO(), configuracaoFinanceiroVO, usuario);
			if (validarTentativaBaixaContaReceberDuplicidade(processamentoContaReceber, processamento, usuario) || !processamentoContaReceber.getContaReceberVO().isGerarNegociacaoRecebimento()) {
				index++;
				continue;
			}
			preencherContaReceberParaBaixa(processamento, processamentoAluno, processamentoContaReceber, configuracaoFinanceiroVO, processamento.getResponsavel(), 1);
			processamentoContaReceber.getContaReceberVO().setGerarNegociacaoRecebimento(false);
			processamentoAluno.getListaContasAReceber().set(index, processamentoContaReceber);
			index++;
			ContaReceberNegociacaoRecebimentoVO contaReceberNegociacaoRecebimentoVO = new ContaReceberNegociacaoRecebimentoVO();
			contaReceberNegociacaoRecebimentoVO.setContaReceber(processamentoContaReceber.getContaReceberVO());
			contaReceberNegociacaoRecebimentoVO.setNegociacaoRecebimento(negociacaoRecebimentoVO.getCodigo());
			contaReceberNegociacaoRecebimentoVO.setValorTotal(processamentoContaReceber.getContaReceberVO().getValor());
			negociacaoRecebimentoVO.adicionarObjContaReceberNegociacaoRecebimentoVOs(contaReceberNegociacaoRecebimentoVO);
		}
	}

	/**
	 * insere no historico da contareceber a tenta de baixa do arquivo
	 * novamente, seja pela duplidade do arquivo ou seja pelo fato de ter
	 * renomeado o arquivo de retorno.
	 * 
	 * @param processamentoContaReceberArquivo
	 * @param processamento
	 * @param usuario
	 * @throws Exception
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private Boolean validarTentativaBaixaContaReceberDuplicidade(ProcessamentoArquivoRetornoParceiroExcelVO processamentoContaReceberArquivo, ProcessamentoArquivoRetornoParceiroVO processamento, UsuarioVO usuario) throws Exception {
		if (getFacadeFactory().getContaReceberFacade().verificaSituacaoContaReceberPorCodigoSituacao(processamentoContaReceberArquivo.getContaReceberVO().getCodigo(), "RE", usuario)) {
			getFacadeFactory().getContaReceberHistoricoFacade().criarContaReceberHistoricoPorBaixaAutomaticas(processamentoContaReceberArquivo.getContaReceberVO(), processamento.getResponsavel(), usuario);
			return true;
		}
		return false;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private void preencherTotalContaPagar(ProcessamentoArquivoRetornoParceiroVO processamento, ProcessamentoArquivoRetornoParceiroAlunoVO processamentoAluno) {
		if (processamento.isGerarContaPagar()) {
			if (processamentoAluno.getValorTotalParceiro() > processamentoAluno.getValorRepasse()) {
				processamento.getContaPagarVO().setValor(processamento.getContaPagarVO().getValor() + (processamentoAluno.getValorTotalParceiro() - processamentoAluno.getValorRepasse()));
			} else if (processamentoAluno.getValorTotalParceiro() < processamentoAluno.getValorRepasse()) {
				processamento.getContaPagarVO().setValor(processamento.getContaPagarVO().getValor() + (processamentoAluno.getValorRepasse() - processamentoAluno.getValorTotalParceiro()));
			}
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private void preencherContaReceberParaBaixa(ProcessamentoArquivoRetornoParceiroVO processamento, ProcessamentoArquivoRetornoParceiroAlunoVO processamentoAluno, ProcessamentoArquivoRetornoParceiroExcelVO processamentoContaReceber, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO, Integer diasVariacaoDataVencimento) throws Exception {

		processamentoContaReceber.getContaReceberVO().setRealizandoRecebimento(true);
		Date dataVencimentoOriginal = null;
		if (configuracaoFinanceiroVO.getVencimentoParcelaDiaUtil()) {
			dataVencimentoOriginal = processamentoContaReceber.getContaReceberVO().getDataVencimento();
		}
		verificarDataVencimentoUtilizarDiaUtil(processamentoContaReceber.getContaReceberVO(), configuracaoFinanceiroVO, usuarioVO);
		processamentoContaReceber.getContaReceberVO().getCalcularValorFinal(processamento.getDataRepasse(), configuracaoFinanceiroVO, false, processamento.getDataRepasse(), usuarioVO);
		if (configuracaoFinanceiroVO.getVencimentoParcelaDiaUtil()) {
			processamentoContaReceber.getContaReceberVO().setDataVencimento(dataVencimentoOriginal);
		}

		if (processamentoContaReceber.getValorConta() < processamentoContaReceber.getContaReceberVO().getValor()) {
			processamentoContaReceber.getContaReceberVO().setTipoDescontoLancadoRecebimento("VA");
			processamentoContaReceber.getContaReceberVO().setValorDescontoLancadoRecebimento(processamentoContaReceber.getContaReceberVO().getValor() - processamentoContaReceber.getValorConta());
			processamentoContaReceber.getContaReceberVO().setValorCalculadoDescontoLancadoRecebimento(processamentoContaReceber.getContaReceberVO().getValor() - processamentoContaReceber.getValorConta());
		} else if (processamentoContaReceber.getValorConta() > processamentoContaReceber.getContaReceberVO().getValor()) {
			processamentoContaReceber.getContaReceberVO().setAcrescimo(processamentoContaReceber.getValorConta() - processamentoContaReceber.getContaReceberVO().getValor());
		}

		processamentoContaReceber.getContaReceberVO().setValorRecebido(processamentoContaReceber.getValorConta());
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private void verificarDataVencimentoUtilizarDiaUtil(ContaReceberVO contaReceberVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
		if (configuracaoFinanceiroVO.getVencimentoParcelaDiaUtil()) {
			contaReceberVO.getDataOriginalVencimento();
			contaReceberVO.setDataVencimentoDiaUtil(getFacadeFactory().getContaReceberFacade().obterDataVerificandoDiaUtil(contaReceberVO.getDataVencimento(), contaReceberVO.getUnidadeEnsino().getCidade().getCodigo(), usuarioVO));
			contaReceberVO.setDataVencimento(contaReceberVO.getDataVencimentoDiaUtil());
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private void preencherNegociacaoRecebimentoVO(NegociacaoRecebimentoVO negociacaoRecebimentoVO, ProcessamentoArquivoRetornoParceiroVO processamento, ProcessamentoArquivoRetornoParceiroAlunoVO processamentoAluno, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {

		FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO = new FormaPagamentoNegociacaoRecebimentoVO();
		formaPagamentoNegociacaoRecebimentoVO.setFormaPagamento(processamento.getParceiroVO().getFormaPagamentoRecebimento());
		formaPagamentoNegociacaoRecebimentoVO.setContaCorrente(processamento.getParceiroVO().getContaCorrenteDaUnidadeEnsino(processamento.getUnidadeEnsinoVO()));
		formaPagamentoNegociacaoRecebimentoVO.setValorRecebimento(processamentoAluno.getValorTotalContaReceber());
		formaPagamentoNegociacaoRecebimentoVO.setNegociacaoRecebimento(negociacaoRecebimentoVO.getCodigo());
		negociacaoRecebimentoVO.adicionarObjFormaPagamentoNegociacaoRecebimentoVOs(formaPagamentoNegociacaoRecebimentoVO);
		negociacaoRecebimentoVO.setValorTotal(negociacaoRecebimentoVO.getValorTotalRecebimento());
		negociacaoRecebimentoVO.setRecebimentoBoletoAutomatico(Boolean.TRUE);
		if (!negociacaoRecebimentoVO.getContaReceberNegociacaoRecebimentoVOs().isEmpty()) {
			getFacadeFactory().getNegociacaoRecebimentoFacade().incluir(negociacaoRecebimentoVO, configuracaoFinanceiroVO, false, usuario);
			getFacadeFactory().getCampanhaFacade().finalizarAgendaCompromissoContaReceber(negociacaoRecebimentoVO);
		}

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void validaGeracaoContaPagar(ProcessamentoArquivoRetornoParceiroVO processamento, UsuarioVO usuario) throws Exception {
		if (processamento.isGerarContaPagar() || Uteis.isAtributoPreenchido(processamento.getContaPagarVO())) {
			if (!Uteis.isAtributoPreenchido(processamento.getContaPagarVO())) {
				processamento.getContaPagarVO().setCodOrigem(processamento.getCodigo().toString());
				processamento.getContaPagarVO().setData(processamento.getDataRepasse());
				processamento.getContaPagarVO().setDataFatoGerador(processamento.getDataRepasse());
				processamento.getContaPagarVO().setDataVencimento(processamento.getDataRepasse());
				processamento.getContaPagarVO().setDescricao("Pagamento de taxas para baixa de arquivo de Parceiro.");
				processamento.getContaPagarVO().setParceiro(processamento.getParceiroVO());
				processamento.getContaPagarVO().setResponsavel(usuario);
				processamento.getContaPagarVO().setResponsavelFinanceiro(usuario.getPessoa());
				processamento.getContaPagarVO().setSituacao("AP");
				processamento.getContaPagarVO().setTipoOrigem(OrigemContaPagar.PROCESSAMENTO_ARQUIVO_RETORNO_PARCEIRO.getValor());
				processamento.getContaPagarVO().setTipoSacado("PA");
				processamento.getContaPagarVO().setJuro(0.0);
				processamento.getContaPagarVO().setMulta(0.0);
				processamento.getContaPagarVO().setNrDocumento("");
				processamento.getContaPagarVO().setUnidadeEnsino(processamento.getUnidadeEnsinoVO());
				CentroResultadoOrigemVO cro = new CentroResultadoOrigemVO();
				cro.setCategoriaDespesaVO(processamento.getParceiroVO().getCategoriaDespesa());
				cro.setUnidadeEnsinoVO(processamento.getUnidadeEnsinoVO());
				cro.setQuantidade(1.00);
				cro.setPorcentagem(100.00);
				cro.setValor(processamento.getContaPagarVO().getValor());
				cro.setTipoNivelCentroResultadoEnum(TipoNivelCentroResultadoEnum.UNIDADE_ENSINO);
				getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().preencherDadosPorCategoriaDespesa(cro, usuario);
				processamento.getContaPagarVO().getListaCentroResultadoOrigemVOs().clear();
				processamento.getContaPagarVO().getListaCentroResultadoOrigemVOs().add(cro);
				getFacadeFactory().getContaPagarFacade().incluir(processamento.getContaPagarVO(), false, true, usuario);
				realizarAtualizacaoContaPagar(processamento, usuario);
			} else {
				getFacadeFactory().getContaPagarFacade().alterar(processamento.getContaPagarVO(), false, false, usuario);
			}
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void preencherNegociacaoPagamentoVO(ProcessamentoArquivoRetornoParceiroVO processamento, UsuarioVO usuario) throws Exception {
		if (Uteis.isAtributoPreenchido(processamento.getContaPagarVO())) {
			NegociacaoPagamentoVO negociacaoPagamentoVO = new NegociacaoPagamentoVO();

			ContaPagarNegociacaoPagamentoVO contaPagarNegociacaoPagamento = new ContaPagarNegociacaoPagamentoVO();
			contaPagarNegociacaoPagamento.setContaPagar(processamento.getContaPagarVO());
			contaPagarNegociacaoPagamento.setNegociacaoContaPagar(negociacaoPagamentoVO.getCodigo());
			contaPagarNegociacaoPagamento.setValorContaPagar(Uteis.arrendondarForcando2CasasDecimais(processamento.getContaPagarVO().getValor()));
			negociacaoPagamentoVO.getContaPagarNegociacaoPagamentoVOs().add(contaPagarNegociacaoPagamento);
			negociacaoPagamentoVO.setValorTotal(Uteis.arrendondarForcando2CasasDecimais(processamento.getContaPagarVO().getValor()));
			
			FormaPagamentoNegociacaoPagamentoVO formaPagamentoNegociacaoPagamentoVO = new FormaPagamentoNegociacaoPagamentoVO();
			formaPagamentoNegociacaoPagamentoVO.setFormaPagamento(processamento.getParceiroVO().getFormaPagamento());
			formaPagamentoNegociacaoPagamentoVO.setContaCorrente(processamento.getParceiroVO().getContaCorrenteDaUnidadeEnsino(processamento.getUnidadeEnsinoVO()));			
			formaPagamentoNegociacaoPagamentoVO.setValor(Uteis.arrendondarForcando2CasasDecimais(processamento.getContaPagarVO().getValor()));
			formaPagamentoNegociacaoPagamentoVO.setNegociacaoContaPagar(negociacaoPagamentoVO.getCodigo());
			negociacaoPagamentoVO.getFormaPagamentoNegociacaoPagamentoVOs().add(formaPagamentoNegociacaoPagamentoVO);
			negociacaoPagamentoVO.setValorTotalPagamento(Uteis.arrendondarForcando2CasasDecimais(processamento.getContaPagarVO().getValor()));
			

			negociacaoPagamentoVO.setData(processamento.getDataRepasse());
			negociacaoPagamentoVO.setDataRegistro(processamento.getDataRepasse());
			negociacaoPagamentoVO.setUnidadeEnsino(processamento.getUnidadeEnsinoVO());
			negociacaoPagamentoVO.setTipoSacado(processamento.getContaPagarVO().getTipoSacado());
			negociacaoPagamentoVO.setParceiro(processamento.getParceiroVO());
			negociacaoPagamentoVO.setResponsavel(usuario);
			getFacadeFactory().getNegociacaoPagamentoFacade().incluir(negociacaoPagamentoVO, usuario);
		}
		realizarRegistroTerminoProcessamento(processamento.getCodigo(), new Date(), usuario);
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe
	 * <code>ProcessamentoArquivoRetornoParceiroVO</code>. Sempre localiza o
	 * registro a ser excluído através da chave primária da entidade.
	 * Primeiramente verifica a conexão com o banco de dados e a permissão do
	 * usuário para realizar esta operacão na entidade. Isto, através da
	 * operação <code>excluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe
	 *            <code>ProcessamentoArquivoRetornoParceiroVO</code> que será
	 *            removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(ProcessamentoArquivoRetornoParceiroVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			ProcessamentoArquivoRetornoParceiro.excluir(getIdEntidade(), verificarAcesso, usuario);
			String sql = "DELETE FROM ProcessamentoArquivoRetornoParceiro WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void realizarAtualizacaoDadosProcessamento(ProcessamentoArquivoRetornoParceiroVO obj) throws Exception {
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet("SELECT * FROM ProcessamentoArquivoRetornoParceiro WHERE codigo = ? ", obj.getCodigo());
		if (dadosSQL.next()) {
			obj.setQtdeLote(dadosSQL.getInt("qtdeLote"));
			obj.setLoteAtual(dadosSQL.getInt("loteAtual"));
			obj.setDataInicioProcessamento(dadosSQL.getDate("dataInicioProcessamento"));
			obj.setDataTerminoProcessamento(dadosSQL.getDate("dataTerminoProcessamento"));
			obj.setMotivoErroProcessamento(dadosSQL.getString("motivoErroProcessamento"));
			if (dadosSQL.getString("situacaoProcessamento") != null && !dadosSQL.getString("situacaoProcessamento").trim().isEmpty()) {
				obj.setSituacaoProcessamento(SituacaoProcessamentoArquivoRetornoEnum.valueOf(dadosSQL.getString("situacaoProcessamento")));
			}
		}
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private StringBuffer getSQLPadraoConsultaBasica() {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT distinct parp.codigo as \"parp.codigo\", parp.datageracao as \"parp.datageracao\",  ");
		sql.append(" parp.situacaoProcessamento as \"parp.situacaoProcessamento\", parp.nomearquivo as \"parp.nomearquivo\",  ");
		sql.append(" parp.gerarcontapagar as \"parp.gerarcontapagar\",  ");
		sql.append(" parc.codigo as \"parc.codigo\", parc.nome as \"parc.nome\", parc.cpf as \"parc.cpf\", parc.cnpj as \"parc.cnpj\",  ");
		sql.append(" u.nome as \"u.nome\", ");
		sql.append(" p.codigo as \"p.codigo\", p.nome as \"p.nome\", p.email as \"p.email\", p.email2 as \"p.email2\", ");
		sql.append(" ue.codigo as \"ue.codigo\", ue.nome as \"ue.nome\"   ");
		sql.append(" FROM ProcessamentoArquivoRetornoParceiro parp ");
		sql.append(" LEFT JOIN parceiro parc ON parc.codigo = parp.parceiro ");
		sql.append(" LEFT JOIN usuario u ON u.codigo = parp.responsavel ");
		sql.append(" LEFT JOIN pessoa p ON p.codigo = u.pessoa ");
		sql.append(" LEFT JOIN unidadeensino ue ON ue.codigo = parp.unidadeEnsino ");
		return sql;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<ProcessamentoArquivoRetornoParceiroVO> consultaRapidaPorCodigo(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE parp.codigo = ").append(valorConsulta).append(" ");
		if (unidadeEnsino != 0) {
			sqlStr.append(" AND ue.codigo = ").append(unidadeEnsino);
		}
		sqlStr.append(" ORDER BY parp.datageracao desc ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<ProcessamentoArquivoRetornoParceiroVO> consultaRapidaPorDataGeracao(Date dataInicial, Date dataFinal, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE parp.datageracao >= '").append(Uteis.getDataBD0000(dataInicial)).append("' ");
		sqlStr.append(" AND parp.datageracao <= '").append(Uteis.getDataBD2359(dataFinal)).append("' ");
		if (unidadeEnsino != 0) {
			sqlStr.append(" AND ue.codigo = ").append(unidadeEnsino);
		}
		sqlStr.append(" ORDER BY parp.datageracao desc ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	/**
	 * Responsável por realizar uma consulta de
	 * <code>ProcessamentoArquivoRetornoParceiro</code> através do valor do
	 * atributo <code>Integer codigo</code>. Retorna os objetos com valores
	 * iguais ou superiores ao parâmetro fornecido. Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>ProcessamentoArquivoRetornoParceiroVO</code> resultantes da
	 *         consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Override
	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM ProcessamentoArquivoRetornoParceiro WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<ProcessamentoArquivoRetornoParceiroVO> montarDadosConsultaBasica(SqlRowSet tabelaResultado) throws Exception {
		List<ProcessamentoArquivoRetornoParceiroVO> vetResultado = new ArrayList<ProcessamentoArquivoRetornoParceiroVO>(0);
		while (tabelaResultado.next()) {
			ProcessamentoArquivoRetornoParceiroVO obj = new ProcessamentoArquivoRetornoParceiroVO();
			montarDadosBasico(obj, tabelaResultado);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private void montarDadosBasico(ProcessamentoArquivoRetornoParceiroVO obj, SqlRowSet dadosSQL) {
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo(new Integer(dadosSQL.getInt("parp.codigo")));
		obj.setDataGeracao(dadosSQL.getTimestamp("parp.datageracao"));
		obj.setNomeArquivo(dadosSQL.getString("parp.nomearquivo"));
		obj.setGerarContaPagar(dadosSQL.getBoolean("parp.gerarcontapagar"));
		if (dadosSQL.getString("parp.situacaoProcessamento") != null && !dadosSQL.getString("parp.situacaoProcessamento").trim().isEmpty()) {
			obj.setSituacaoProcessamento(SituacaoProcessamentoArquivoRetornoEnum.valueOf(dadosSQL.getString("parp.situacaoProcessamento")));
		}
		obj.getParceiroVO().setCodigo(dadosSQL.getInt("parc.codigo"));
		obj.getParceiroVO().setNome(dadosSQL.getString("parc.nome"));
		obj.getParceiroVO().setCPF(dadosSQL.getString("parc.cpf"));
		obj.getParceiroVO().setCNPJ(dadosSQL.getString("parc.cnpj"));

		obj.getResponsavel().setNome(dadosSQL.getString("u.nome"));
		obj.getResponsavel().getPessoa().setCodigo(dadosSQL.getInt("p.codigo"));
		obj.getResponsavel().getPessoa().setNome(dadosSQL.getString("p.nome"));
		obj.getResponsavel().getPessoa().setEmail(dadosSQL.getString("p.email"));
		obj.getResponsavel().getPessoa().setEmail2(dadosSQL.getString("p.email2"));

		obj.getUnidadeEnsinoVO().setCodigo(dadosSQL.getInt("ue.codigo"));
		obj.getUnidadeEnsinoVO().setNome(dadosSQL.getString("ue.nome"));

	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma
	 * consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>ProcessamentoArquivoRetornoParceiroVO</code> resultantes da
	 *         consulta.
	 */
	public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de
	 * dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>ProcessamentoArquivoRetornoParceiroVO</code>.
	 * 
	 * @return O objeto da classe
	 *         <code>ProcessamentoArquivoRetornoParceiroVO</code> com os dados
	 *         devidamente montados.
	 */

	public static ProcessamentoArquivoRetornoParceiroVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ProcessamentoArquivoRetornoParceiroVO obj = new ProcessamentoArquivoRetornoParceiroVO();
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.getResponsavel().setCodigo(dadosSQL.getInt("responsavel"));
		obj.getUnidadeEnsinoVO().setCodigo(dadosSQL.getInt("unidadeensino"));
		obj.getParceiroVO().setCodigo(dadosSQL.getInt("parceiro"));
		obj.getContaPagarVO().setCodigo(dadosSQL.getInt("contapagar"));
		obj.setNomeArquivo(dadosSQL.getString("nomearquivo"));
		obj.setDatasDeCompetencia(dadosSQL.getString("datasdecompetencia"));
		obj.setDataGeracao(dadosSQL.getTimestamp("dataGeracao"));
		obj.getUnidadeEnsinoVO().setCodigo(dadosSQL.getInt("unidadeEnsino"));
		obj.setQtdeLote(dadosSQL.getInt("qtdeLote"));
		obj.setLoteAtual(dadosSQL.getInt("loteAtual"));
		obj.setDataInicioProcessamento(dadosSQL.getDate("dataInicioProcessamento"));
		obj.setDataTerminoProcessamento(dadosSQL.getDate("dataTerminoProcessamento"));
		obj.setMotivoErroProcessamento(dadosSQL.getString("motivoErroProcessamento"));
		obj.setContasBaixadas(dadosSQL.getBoolean("contasbaixadas"));
		obj.setGerarContaPagar(dadosSQL.getBoolean("gerarcontapagar"));

		if (dadosSQL.getString("situacaoProcessamento") != null && !dadosSQL.getString("situacaoProcessamento").trim().isEmpty()) {
			obj.setSituacaoProcessamento(SituacaoProcessamentoArquivoRetornoEnum.valueOf(dadosSQL.getString("situacaoProcessamento")));
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
			return obj;
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		List<ProcessamentoArquivoRetornoParceiroAlunoVO> lista = getFacadeFactory().getProcessamentoArquivoRetornoParceiroAlunoFacade().consultaRapidaPorProcessamentoArquivoRetornoParceiro(obj, usuario);
		for (ProcessamentoArquivoRetornoParceiroAlunoVO processamentoArquivoRetornoParceiroAlunoVO : lista) {
			processamentoArquivoRetornoParceiroAlunoVO.setProcessamentoArquivoRetornoParceiroVO(obj);
			obj.separarProcessamentoArquivoRetornoParceiroExcelVOEmLista(processamentoArquivoRetornoParceiroAlunoVO);
		}
		montarDadosParceiro(obj, nivelMontarDados, usuario);
		montarDadosResponsavel(obj, nivelMontarDados, usuario);
		montarDadosUnidadeEnsino(obj, nivelMontarDados, usuario);
		montarDadosContaPagar(obj, nivelMontarDados, usuario);
		return obj;
	}

	/**
	 * Operação responsável por localizar um objeto da classe
	 * <code>ProcessamentoArquivoRetornoParceiroVO</code> através de sua chave
	 * primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto
	 *                procurado.
	 */
	@Override
	public ProcessamentoArquivoRetornoParceiroVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sql = "SELECT * FROM ProcessamentoArquivoRetornoParceiro WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( ProcessamentoArquivoRetornoParceiro ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public static void montarDadosResponsavel(ProcessamentoArquivoRetornoParceiroVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getResponsavel().getCodigo().intValue() == 0) {
			obj.setResponsavel(new UsuarioVO());
			return;
		}
		obj.setResponsavel(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavel().getCodigo(), nivelMontarDados, usuario));
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public static void montarDadosUnidadeEnsino(ProcessamentoArquivoRetornoParceiroVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getUnidadeEnsinoVO().getCodigo().intValue() == 0) {
			obj.setUnidadeEnsinoVO(new UnidadeEnsinoVO());
			return;
		}
		obj.setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsinoVO().getCodigo(), false, nivelMontarDados, usuario));
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public static void montarDadosParceiro(ProcessamentoArquivoRetornoParceiroVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getParceiroVO().getCodigo().intValue() == 0) {
			obj.setParceiroVO(new ParceiroVO());
			return;
		}
		obj.setParceiroVO(getFacadeFactory().getParceiroFacade().consultarPorChavePrimaria(obj.getParceiroVO().getCodigo(), false, nivelMontarDados, usuario));
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public static void montarDadosContaPagar(ProcessamentoArquivoRetornoParceiroVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getContaPagarVO().getCodigo().intValue() == 0) {
			obj.setContaPagarVO(new ContaPagarVO());
			return;
		}
		obj.setContaPagarVO(getFacadeFactory().getContaPagarFacade().consultarPorChavePrimaria(obj.getContaPagarVO().getCodigo(), false, nivelMontarDados, usuario));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return ProcessamentoArquivoRetornoParceiro.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		ProcessamentoArquivoRetornoParceiro.idEntidade = idEntidade;
	}

}
