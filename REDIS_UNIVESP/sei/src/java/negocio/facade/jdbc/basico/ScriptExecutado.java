package negocio.facade.jdbc.basico;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
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

import controle.arquitetura.AplicacaoControle;
import controle.arquitetura.AssuntoDebugEnum;
import negocio.comuns.basico.ScriptExecutadoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.formula.interpreters.SQLInterpreter;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.basico.ScriptExecutadoInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
@SuppressWarnings({"unchecked", "serial", "rawtypes" })
public class ScriptExecutado extends ControleAcesso implements ScriptExecutadoInterfaceFacade {

	@Autowired
	private SQLInterpreter sqlInterpreter;
	
	@Override
	public void executarScripts() throws Exception {
		executarRotinaScripts();
	}
	
	private Map<Date, File> consultarArquivosNaoExecutados() throws Exception {
		Map<Date, File> arquivosNaoExecutados = new TreeMap<Date, File>();
		HashSet<String> arquivosExecutadosComSucesso = consultarScriptsExecutadosComSucesso();
		URL url = this.getClass().getResource("/sql");
		if (url != null && Uteis.isAtributoPreenchido(url.getPath())) {
			String path = (Uteis.isSistemaOperacionalWindows() && url.getPath().startsWith("/")) ? url.getPath().substring(1, url.getPath().length()) : url.getPath() ;
			path = URLDecoder.decode(path, "UTF-8");
			Files.walk(Paths.get(path)).forEach(arquivo -> {
				// operacao .contains utiliza HashSet para ter melhor performance
	        	if (arquivo.toFile().isFile() && arquivo.toFile().getName().endsWith(".sql") &&
	        			!arquivosExecutadosComSucesso.contains(arquivo.toFile().getName())) {
	        		Date data = realizarCapturaDataPeloNomeArquivo(arquivo.toFile());
	        		if (data != null) {
	        			while (arquivosNaoExecutados.containsKey(data)) {
	        				Calendar calendar = Calendar.getInstance();
	        				calendar.add(Calendar.SECOND, 1);
	        				data = calendar.getTime();
	        			}
	        			arquivosNaoExecutados.put(data, arquivo.toFile());
	        		} else {
	        			try {
	        				AplicacaoControle.getScriptsInvalidos().add(montarDados(arquivo.toFile(), false, "inválido"));
		        		} catch (Exception e) {
		        			AplicacaoControle.realizarEscritaErroDebug(AssuntoDebugEnum.SCRIPT, e);
		    				e.printStackTrace();
		    			}
	        		}
	        	}
			});
		}
		return arquivosNaoExecutados;
	}
	
	private HashSet<String> consultarScriptsExecutadosComSucesso() {
		HashSet<String> nomes = new HashSet<String>(0);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet("SELECT DISTINCT nome FROM public.scriptexecutado WHERE sucesso");
		while (rs.next()) {
			nomes.add(rs.getString("nome"));
		}
		return nomes;
	}

	private Date realizarCapturaDataPeloNomeArquivo(File arquivo) {
		try {
			if (arquivo != null && Uteis.isAtributoPreenchido(arquivo.getName())) {
				String[] partes = arquivo.getName().replace(".sql", "").split("-");
				if (partes.length < 3) {
					AplicacaoControle.realizarEscritaTextoDebug(AssuntoDebugEnum.SCRIPT, "Data no nome do arquivo não identificada");
					return null;
				} else if (partes.length < 5) {
					return new SimpleDateFormat("dd-MM-yyyy-HH-mm").parse(partes[0] + "-" + partes[1] + "-" + partes[2] + "-23-59");
				} else {
					return new SimpleDateFormat("dd-MM-yyyy-HH-mm").parse(partes[0] + "-" + partes[1] + "-" + partes[2] + "-" + partes[3] + "-" + partes[4]);
				}
			} else {
				AplicacaoControle.realizarEscritaTextoDebug(AssuntoDebugEnum.SCRIPT, "Nome do arquivo vazio");
				return null;
			}
		} catch (ParseException e) {
			AplicacaoControle.realizarEscritaErroDebug(AssuntoDebugEnum.SCRIPT, e);
			return null;
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
	public void executarRotinaScripts() throws Exception {
		AplicacaoControle.limparScriptsComFalha();
		boolean abortado = false;
		Map<Date, File> arquivos = consultarArquivosNaoExecutados();
		if (!AplicacaoControle.getScriptsInvalidos().isEmpty()) {
			abortado = true;
		}
		// Arquivos ja estao ordenados pela data por usar TreeMap<Date, File>
		for (Map.Entry<Date, File> entry : arquivos.entrySet()) {
			File arquivo = entry.getValue();
			try {
				if (abortado) {
					AplicacaoControle.getScriptsAbortados().add(montarDados(arquivo, false, "abortado"));
				} else {
					String sql = getFacadeFactory().getArquivoHelper().lerArquivoTexto(arquivo, "ISO-8859-1");
					sqlInterpreter.executaSql(sql);
					if (!persistirScriptExecutado(montarDados(arquivo, true, ""))) {
						abortado = true;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				AplicacaoControle.realizarEscritaErroDebug(AssuntoDebugEnum.SCRIPT, e);
				abortado = true;
				ScriptExecutadoVO script = montarDados(arquivo, false, e.getMessage());
				persistirScriptExecutado(script);
				AplicacaoControle.getScriptsNaoExecutados().add(script);
			}
		}
	}

	private boolean persistirScriptExecutado(ScriptExecutadoVO scriptExecutadoVO) {
		try {
			persistir(scriptExecutadoVO);
			return true;
		} catch (Exception e) {
			AplicacaoControle.getScriptsNaoPersistidos().add(scriptExecutadoVO);
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void persistir(ScriptExecutadoVO obj) throws Exception {
		try {
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection arg0) throws SQLException {
					StringBuilder sql = new StringBuilder("INSERT INTO scriptexecutado(nome, dataexecucao, sql, versaoSistema, sucesso, mensagemErro)");
					sql.append(" VALUES (?, ?, ?, ?, ?, ?) ");
					sql.append(" returning codigo --ul:0");
					final PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
					int i = 0;
					Uteis.setValuePreparedStatement(obj.getNome(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getDataExecucao(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getSql(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getVersaoSistema(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getSucesso(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getMensagemErro(), ++i, sqlInserir);
					return sqlInserir;
				}
			}, new ResultSetExtractor() {
				public Object extractData(final ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						obj.setNovoObj(false);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
		} catch (Exception e) {
			obj.setNovoObj(true);
			throw e;
		}
	}

	private ScriptExecutadoVO montarDados(File arquivo, Boolean sucesso, String mensagemErro) throws Exception {
		ScriptExecutadoVO obj = new ScriptExecutadoVO();
		obj.setNome(arquivo.getName().trim());
		obj.setSql(getFacadeFactory().getArquivoHelper().lerArquivoTexto(arquivo, "UTF-8"));
		obj.setVersaoSistema(Uteis.VERSAO_SISTEMA);
		obj.setSucesso(sucesso);
		obj.setMensagemErro(mensagemErro);
		return obj;
	}
	
	@Override
	public void alterarScriptNaoExecutadoParaExecutadoComSucesso(Integer codigo) throws Exception {
		try {
			final String sql = "update scriptexecutado set sucesso = true, mensagemErro = 'Marcado como executado' where codigo = ?";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setInt(1, codigo);
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

}