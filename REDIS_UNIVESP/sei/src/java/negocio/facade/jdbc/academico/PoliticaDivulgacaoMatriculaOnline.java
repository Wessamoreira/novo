package negocio.facade.jdbc.academico;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

import negocio.comuns.academico.PoliticaDivulgacaoMatriculaOnlineVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.ead.enumeradores.PeriodoApresentacaoPoliticaDivulgacaoMatriculaOnlineEnum;
import negocio.comuns.ead.enumeradores.SituacaoEnum;
import negocio.comuns.utilitarias.ArquivoHelper;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.ead.AvaliacaoOnline;
import negocio.interfaces.academico.PoliticaDivulgacaoMatriculaOnlineInterfaceFacade;
import webservice.servicos.BannerObject;

@Repository
@Scope("singleton")
@Lazy
public class PoliticaDivulgacaoMatriculaOnline extends ControleAcesso implements PoliticaDivulgacaoMatriculaOnlineInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public PoliticaDivulgacaoMatriculaOnline() throws Exception {
		super();
		setIdEntidade("PoliticaDivulgacaoMatriculaOnline");
	}

	public static void validarDados(PoliticaDivulgacaoMatriculaOnlineVO obj) throws Exception {
		if (obj.getNome().equals("")) {
			throw new ConsistirException("O Campo Nome é necessário !");
		}
		if (obj.getDescricao().equals("")) {
			throw new ConsistirException("O Campo Descrição é necessário !");
		}
		if (obj.getCaminhoBaseLogo().equals("")) {
			throw new ConsistirException("O Campo Banner é necessário !");
		}
		if(obj.getCursoVO().getCodigo().equals(0)) {
			throw new ConsistirException("O Campo Curso é necessário !");
		}
		if (obj.getPeriodoApresentacaoPoliticaDivulgacaoMatriculaOnlineEnum().equals(PeriodoApresentacaoPoliticaDivulgacaoMatriculaOnlineEnum.PERIODO_FIXO)
                && Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(obj.getPeriodoAte()).
                        compareTo(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(obj.getPeriodoDe())) > 1) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_PoliticaDivulgacaoMatriculaOnline_periodoAteMaiorPeriodoDe"));
        }		
		if(obj.getDivulgarParaAluno().equals(false)) {
			obj.getPoliticaDivulgacaoMatriculaOnlineAlunoPublicoAlvoVOs().clear();
		}		
		if(obj.getDivulgarParaProfessor().equals(false)) {
			obj.getPoliticaDivulgacaoMatriculaOnlineProfessorPublicoAlvoVOs().clear();
		}		
		if(obj.getDivulgarParaCoordenador().equals(false)) {
			obj.getPoliticaDivulgacaoMatriculaOnlineCoordenadorPublicoAlvoVOs().clear();
		}		
		if(obj.getDivulgarParaFuncionario().equals(false)) {
			obj.getPoliticaDivulgacaoMatriculaOnlineFuncionarioPublicoAlvoVOs().clear();
		}		
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(final PoliticaDivulgacaoMatriculaOnlineVO obj, UsuarioVO usuarioVO, String publicoAlvo, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		if (obj.getNovoObj()) {
			incluir(obj, usuarioVO, configuracaoGeralSistemaVO);
		} else {
			alterar(obj, usuarioVO, configuracaoGeralSistemaVO);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final PoliticaDivulgacaoMatriculaOnlineVO obj, final UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		try {
			validarDados(obj);
			PoliticaDivulgacaoMatriculaOnline.incluir(getIdEntidade(), true, usuarioVO);
			
			final String sql = " INSERT INTO politicadivulgacaomatriculaonline (nome, descricao, datacadastro, banner, usuario, nomearquivologo, curso, divulgarParaComunidade, situacao, periodoApresentacaoPoliticaDivulgacaoMatriculaOnline, periodoDe, periodoAte, divulgarParaAluno, divulgarParaProfessor, divulgarParaCoordenador, divulgarParaFuncionario) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql);
					try {
						sqlInserir.setString(1, obj.getNome());
						sqlInserir.setString(2, obj.getDescricao());
						sqlInserir.setTimestamp(3, Uteis.getDataJDBCTimestamp(obj.getDataCadastro()));
						sqlInserir.setString(4, obj.getCaminhoBaseLogo());
						sqlInserir.setInt(5, obj.getUsuario().getCodigo());
						sqlInserir.setString(6, obj.getNomeArquivoLogo());
						sqlInserir.setInt(7, obj.getCursoVO().getCodigo());
						sqlInserir.setBoolean(8, obj.getDivulgarParaComunidade());
						sqlInserir.setString(9, obj.getSituacaoEnum().getName());
						sqlInserir.setString(10, obj.getPeriodoApresentacaoPoliticaDivulgacaoMatriculaOnlineEnum().getName());
						if(obj.getPeriodoApresentacaoPoliticaDivulgacaoMatriculaOnlineEnum().equals(PeriodoApresentacaoPoliticaDivulgacaoMatriculaOnlineEnum.PERIODO_FIXO)) {
							sqlInserir.setTimestamp(11, Uteis.getDataJDBCTimestamp(obj.getPeriodoDe()));
							sqlInserir.setTimestamp(12, Uteis.getDataJDBCTimestamp(obj.getPeriodoAte()));
						} else {
							sqlInserir.setNull(11, 0);
							sqlInserir.setNull(12, 0);
						}
						sqlInserir.setBoolean(13, obj.getDivulgarParaAluno());
						sqlInserir.setBoolean(14, obj.getDivulgarParaProfessor());
						sqlInserir.setBoolean(15, obj.getDivulgarParaCoordenador());
						sqlInserir.setBoolean(16, obj.getDivulgarParaFuncionario());
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
			obj.setNovoObj(Boolean.FALSE);
			getFacadeFactory().getPoliticaDivulgacaoMatriculaOnlinePublicoAlvoInterfaceFacade().incluirPoliticaDivulgacaoMatriculaOnlinePublicoAlvo(obj, obj.getPoliticaDivulgacaoMatriculaOnlineAlunoPublicoAlvoVOs(), usuarioVO);
			getFacadeFactory().getPoliticaDivulgacaoMatriculaOnlinePublicoAlvoInterfaceFacade().incluirPoliticaDivulgacaoMatriculaOnlinePublicoAlvo(obj, obj.getPoliticaDivulgacaoMatriculaOnlineCoordenadorPublicoAlvoVOs(), usuarioVO);
			getFacadeFactory().getPoliticaDivulgacaoMatriculaOnlinePublicoAlvoInterfaceFacade().incluirPoliticaDivulgacaoMatriculaOnlinePublicoAlvo(obj, obj.getPoliticaDivulgacaoMatriculaOnlineFuncionarioPublicoAlvoVOs(), usuarioVO);
			getFacadeFactory().getPoliticaDivulgacaoMatriculaOnlinePublicoAlvoInterfaceFacade().incluirPoliticaDivulgacaoMatriculaOnlinePublicoAlvo(obj, obj.getPoliticaDivulgacaoMatriculaOnlineProfessorPublicoAlvoVOs(), usuarioVO);
			salvarLogoPoliticaDivulgacaoMatriculaOnline(obj, configuracaoGeralSistemaVO, usuarioVO);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw e;
		}
	}

	public void salvarLogoPoliticaDivulgacaoMatriculaOnline(PoliticaDivulgacaoMatriculaOnlineVO politica, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception {
		if (politica.getLogoInformada() && !politica.getNomeArquivoLogo().trim().isEmpty()) {			
			String caminhoFinal = PastaBaseArquivoEnum.POLITICA_DIVULGACAO_MATRICULA_ONLINE.getValue() + File.separator + politica.getCodigo();
			String nomeFinal = "logoPolitica" + politica.getNomeArquivoLogo().substring(politica.getNomeArquivoLogo().lastIndexOf("."), politica.getNomeArquivoLogo().length());
			ArquivoHelper.delete(new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + caminhoFinal + File.separator + nomeFinal));
			ArquivoHelper.copiarArquivoDaPastaTempParaPastaFixaComOutroNome(politica.getCaminhoBaseLogo(), caminhoFinal, politica.getNomeArquivoLogo(), nomeFinal, configuracaoGeralSistemaVO, false);
			politica.setCaminhoBaseLogo(caminhoFinal);
			politica.setNomeArquivoLogo(nomeFinal);
			politica.setLogoInformada(false);
			alterarCaminhoArquivoLogo(politica, usuario);
		}

	}
	
	public void alterarCaminhoArquivoLogo(final PoliticaDivulgacaoMatriculaOnlineVO obj, UsuarioVO usuarioVO) throws Exception{
		try {
			final String sql = " UPDATE politicadivulgacaomatriculaonline SET banner=?, nomearquivologo = ? where ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getCaminhoBaseLogo());
					sqlAlterar.setString(2, obj.getNomeArquivoLogo());
					sqlAlterar.setInt(3, obj.getCodigo());
					return sqlAlterar;
				}
			});			
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final PoliticaDivulgacaoMatriculaOnlineVO obj, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		try {
			validarDados(obj);			
			PoliticaDivulgacaoMatriculaOnline.alterar(getIdEntidade(), true, usuarioVO);
			PoliticaDivulgacaoMatriculaOnlineVO politicaDivulgacaoMatriculaOnlineVO =  null;
			if (obj.getLogoInformada() && !obj.getNomeArquivoLogo().trim().isEmpty()) {
				politicaDivulgacaoMatriculaOnlineVO =  consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO, null);
			}
			final String sql = " UPDATE politicadivulgacaomatriculaonline SET nome=?, descricao=?, datacadastro=?, banner=?, nomearquivologo = ?, curso = ?, divulgarParaComunidade = ?, situacao = ?, periodoApresentacaoPoliticaDivulgacaoMatriculaOnline = ?, periodoDe = ?, periodoAte = ?, divulgarParaAluno = ?, divulgarParaProfessor = ?, divulgarParaCoordenador = ?, divulgarParaFuncionario = ? where ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getNome());
					sqlAlterar.setString(2, obj.getDescricao());
					sqlAlterar.setTimestamp(3, Uteis.getDataJDBCTimestamp(obj.getDataCadastro()));
					sqlAlterar.setString(4, obj.getCaminhoBaseLogo());
					sqlAlterar.setString(5, obj.getNomeArquivoLogo());
					sqlAlterar.setInt(6, obj.getCursoVO().getCodigo());
					sqlAlterar.setBoolean(7, obj.getDivulgarParaComunidade());
					sqlAlterar.setString(8, obj.getSituacaoEnum().getName());
					sqlAlterar.setString(9, obj.getPeriodoApresentacaoPoliticaDivulgacaoMatriculaOnlineEnum().getName());
					if(obj.getPeriodoApresentacaoPoliticaDivulgacaoMatriculaOnlineEnum().equals(PeriodoApresentacaoPoliticaDivulgacaoMatriculaOnlineEnum.PERIODO_FIXO)) {
						sqlAlterar.setTimestamp(10, Uteis.getDataJDBCTimestamp(obj.getPeriodoDe()));
						sqlAlterar.setTimestamp(11, Uteis.getDataJDBCTimestamp(obj.getPeriodoAte()));
					} else {
						sqlAlterar.setNull(10, 0);
						sqlAlterar.setNull(11, 0);
					}
					sqlAlterar.setBoolean(12, obj.getDivulgarParaAluno());
					sqlAlterar.setBoolean(13, obj.getDivulgarParaProfessor());
					sqlAlterar.setBoolean(14, obj.getDivulgarParaCoordenador());
					sqlAlterar.setBoolean(15, obj.getDivulgarParaFuncionario());
					sqlAlterar.setInt(16, obj.getCodigo());
					return sqlAlterar;
				}
			});
			obj.setNovoObj(Boolean.FALSE);
			getFacadeFactory().getPoliticaDivulgacaoMatriculaOnlinePublicoAlvoInterfaceFacade().alterarPoliticaDivulgacaoMatriculaOnlinePublicoAlvo(obj, obj.getPoliticaDivulgacaoMatriculaOnlineAlunoPublicoAlvoVOs(), usuarioVO, "ALUNO");
			getFacadeFactory().getPoliticaDivulgacaoMatriculaOnlinePublicoAlvoInterfaceFacade().alterarPoliticaDivulgacaoMatriculaOnlinePublicoAlvo(obj, obj.getPoliticaDivulgacaoMatriculaOnlineCoordenadorPublicoAlvoVOs(), usuarioVO, "COORDENADOR");
			getFacadeFactory().getPoliticaDivulgacaoMatriculaOnlinePublicoAlvoInterfaceFacade().alterarPoliticaDivulgacaoMatriculaOnlinePublicoAlvo(obj, obj.getPoliticaDivulgacaoMatriculaOnlineFuncionarioPublicoAlvoVOs(), usuarioVO, "FUNCIONARIO");
			getFacadeFactory().getPoliticaDivulgacaoMatriculaOnlinePublicoAlvoInterfaceFacade().alterarPoliticaDivulgacaoMatriculaOnlinePublicoAlvo(obj, obj.getPoliticaDivulgacaoMatriculaOnlineProfessorPublicoAlvoVOs(), usuarioVO, "PROFESSOR");
			if (Uteis.isAtributoPreenchido(politicaDivulgacaoMatriculaOnlineVO) && obj.getLogoInformada() && !obj.getNomeArquivoLogo().trim().isEmpty()) {				
				if(!politicaDivulgacaoMatriculaOnlineVO.getNomeArquivoLogo().trim().isEmpty()){
					ArquivoHelper.delete(new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo()+File.separator+politicaDivulgacaoMatriculaOnlineVO.getCaminhoBaseLogo()+File.separator+politicaDivulgacaoMatriculaOnlineVO.getNomeArquivoLogo()));				
				}
			}
			salvarLogoPoliticaDivulgacaoMatriculaOnline(obj, configuracaoGeralSistemaVO, usuarioVO);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirPoliticaDivulgacaoMatriculaOnline(PoliticaDivulgacaoMatriculaOnlineVO obj, UsuarioVO usuario) throws Exception {
		try {
			getFacadeFactory().getProcessoMatriculaCalendarioFacade().desvincularPoliticaDivulgacaoMatriculaOnline(obj.getCodigo(), usuario);
			PoliticaDivulgacaoMatriculaOnlinePublicoAlvo.excluir(getIdEntidade(), true, usuario);
			String sql = "DELETE FROM PoliticaDivulgacaoMatriculaOnline WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		} finally {
		}
	}

	@Override
	public PoliticaDivulgacaoMatriculaOnlineVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, List<UnidadeEnsinoVO> listaUnidadeEnsino) throws Exception {
		String sql = "SELECT * FROM politicadivulgacaomatriculaonline WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, listaUnidadeEnsino, usuario));
	}

	@Override
	public List<PoliticaDivulgacaoMatriculaOnlineVO> consultarPorNomePolitica(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, List<UnidadeEnsinoVO> listaUnidadeEnsino) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "select * from politicadivulgacaomatriculaonline where upper(sem_acentos(nome) ) like(sem_acentos('%" + valorConsulta.toUpperCase() + "%')) order by nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario, listaUnidadeEnsino);
	}

	public List<PoliticaDivulgacaoMatriculaOnlineVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario, List<UnidadeEnsinoVO> listaUnidadeEnsino) throws Exception {
		List<PoliticaDivulgacaoMatriculaOnlineVO> vetResultado = new ArrayList<PoliticaDivulgacaoMatriculaOnlineVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, listaUnidadeEnsino, usuario));
		}
		return vetResultado;
	}

	public PoliticaDivulgacaoMatriculaOnlineVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, List<UnidadeEnsinoVO> listaUnidadeEnsino, UsuarioVO usuario) throws Exception {
		PoliticaDivulgacaoMatriculaOnlineVO obj = new PoliticaDivulgacaoMatriculaOnlineVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setNome(dadosSQL.getString("nome"));
		obj.setDescricao(dadosSQL.getString("descricao"));
		obj.setDataCadastro(dadosSQL.getDate("datacadastro"));
		obj.setCaminhoBaseLogo(dadosSQL.getString("banner"));
		if(Uteis.isAtributoPreenchido(dadosSQL.getInt("usuario"))) {
			obj.getUsuario().setCodigo(new Integer(dadosSQL.getInt("usuario")));
			obj.setUsuario(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getUsuario().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
		}
		obj.setNomeArquivoLogo(dadosSQL.getString("nomeArquivoLogo"));
		obj.getCursoVO().setCodigo(dadosSQL.getInt("curso"));
		obj.setDivulgarParaComunidade(dadosSQL.getBoolean("divulgarparacomunidade"));
		obj.setSituacaoEnum(SituacaoEnum.valueOf(dadosSQL.getString("situacao")));
		obj.setDivulgarParaAluno(dadosSQL.getBoolean("divulgarParaAluno"));
		obj.setDivulgarParaProfessor(dadosSQL.getBoolean("divulgarParaProfessor"));
		obj.setDivulgarParaCoordenador(dadosSQL.getBoolean("divulgarParaCoordenador"));
		obj.setDivulgarParaFuncionario(dadosSQL.getBoolean("divulgarParaFuncionario"));
		obj.setPeriodoApresentacaoPoliticaDivulgacaoMatriculaOnlineEnum(PeriodoApresentacaoPoliticaDivulgacaoMatriculaOnlineEnum.valueOf(dadosSQL.getString("periodoApresentacaoPoliticaDivulgacaoMatriculaOnline")));
		if(nivelMontarDados == Uteis.NIVELMONTARDADOS_TODOS) {
			if(obj.getPeriodoApresentacaoPoliticaDivulgacaoMatriculaOnlineEnum().equals(PeriodoApresentacaoPoliticaDivulgacaoMatriculaOnlineEnum.PERIODO_FIXO)) {
				obj.setPeriodoDe(dadosSQL.getTimestamp("periodode"));
				obj.setPeriodoAte(dadosSQL.getTimestamp("periodoate"));
			}
			if(Uteis.isAtributoPreenchido(dadosSQL.getDate("dataativacao"))) {
				obj.setDataAtivacao(dadosSQL.getDate("dataativacao"));
			}
			if(Uteis.isAtributoPreenchido(dadosSQL.getInt("responsavelativacao"))) {
				obj.getResponsavelAtivacao().setCodigo(dadosSQL.getInt("responsavelativacao"));		
				obj.setResponsavelAtivacao(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavelAtivacao().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
			}
			if(Uteis.isAtributoPreenchido(dadosSQL.getDate("datainativacao"))) {
				obj.setDataInativacao(dadosSQL.getDate("datainativacao"));
			}
			if(Uteis.isAtributoPreenchido(dadosSQL.getInt("responsavelinativacao"))) {
				obj.getResponsavelInativacao().setCodigo(dadosSQL.getInt("responsavelinativacao"));
				obj.setResponsavelInativacao(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavelInativacao().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
			}
			obj.setCursoVO(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(obj.getCursoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, usuario));
			obj.setPoliticaDivulgacaoMatriculaOnlineProfessorPublicoAlvoVOs(getFacadeFactory().getPoliticaDivulgacaoMatriculaOnlinePublicoAlvoInterfaceFacade().consultarPoliticaDivulgacaoMatriculaOnlinePublicoAlvoVOs(obj.getCodigo(), nivelMontarDados, "PROFESSOR", listaUnidadeEnsino, usuario));
			obj.setPoliticaDivulgacaoMatriculaOnlineFuncionarioPublicoAlvoVOs(getFacadeFactory().getPoliticaDivulgacaoMatriculaOnlinePublicoAlvoInterfaceFacade().consultarPoliticaDivulgacaoMatriculaOnlinePublicoAlvoVOs(obj.getCodigo(), nivelMontarDados, "FUNCIONARIO", listaUnidadeEnsino, usuario));
			obj.setPoliticaDivulgacaoMatriculaOnlineCoordenadorPublicoAlvoVOs(getFacadeFactory().getPoliticaDivulgacaoMatriculaOnlinePublicoAlvoInterfaceFacade().consultarPoliticaDivulgacaoMatriculaOnlinePublicoAlvoVOs(obj.getCodigo(), nivelMontarDados, "COORDENADOR", listaUnidadeEnsino, usuario));
			obj.setPoliticaDivulgacaoMatriculaOnlineAlunoPublicoAlvoVOs(getFacadeFactory().getPoliticaDivulgacaoMatriculaOnlinePublicoAlvoInterfaceFacade().consultarPoliticaDivulgacaoMatriculaOnlinePublicoAlvoVOs(obj.getCodigo(), nivelMontarDados, "ALUNO", listaUnidadeEnsino, usuario));
			
		}
		return obj;
	}

	@Override
	public void upLoadLogoPoliticaDivulgacaoMatriculaOnline(FileUploadEvent upload, PoliticaDivulgacaoMatriculaOnlineVO politica, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {
		String nomeIcone = String.valueOf(new Date().getTime()) + upload.getUploadedFile().getName().substring(upload.getUploadedFile().getName().lastIndexOf("."), upload.getUploadedFile().getName().length());
		File arquivoExistente = null;
		try {
			if (politica.getCaminhoBaseLogo().contains(PastaBaseArquivoEnum.POLITICA_DIVULGACAO_MATRICULA_ONLINE_TMP.getValue())) {
				arquivoExistente = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator + politica.getCaminhoBaseLogo() + File.separator + politica.getNomeArquivoLogo());
				ArquivoHelper.delete(arquivoExistente);
			}
			ArquivoHelper.salvarArquivoNaPastaTemp(upload, nomeIcone, PastaBaseArquivoEnum.POLITICA_DIVULGACAO_MATRICULA_ONLINE_TMP.getValue(), configuracaoGeralSistemaVO, usuarioVO);
			politica.setCaminhoBaseLogo(PastaBaseArquivoEnum.POLITICA_DIVULGACAO_MATRICULA_ONLINE_TMP.getValue());
			politica.setNomeArquivoLogo(nomeIcone);
			politica.setLogoInformada(true);
		} catch (Exception e) {
			throw e;
		} finally {
			arquivoExistente = null;
		}
	}

	public static String getIdEntidade() {
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		PoliticaDivulgacaoMatriculaOnline.idEntidade = idEntidade;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSituacaoPoliticaDivulgacaoMatriculaOnlineAtivo(final PoliticaDivulgacaoMatriculaOnlineVO politicaDivulgacaoMatriculaOnlineVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			AvaliacaoOnline.alterar(getIdEntidade(), verificarAcesso, usuarioVO);
			final String sql = "UPDATE politicaDivulgacaoMatriculaOnline" + " SET " + "situacao=?, dataativacao=?, responsavelativacao=?  WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);
					sqlAlterar.setString(1, politicaDivulgacaoMatriculaOnlineVO.getSituacaoEnum().name());
					sqlAlterar.setTimestamp(2, Uteis.getDataJDBCTimestamp(politicaDivulgacaoMatriculaOnlineVO.getDataAtivacao()));
					if (politicaDivulgacaoMatriculaOnlineVO.getResponsavelAtivacao().getCodigo() != 0) {
						sqlAlterar.setInt(3, politicaDivulgacaoMatriculaOnlineVO.getResponsavelAtivacao().getCodigo());
					} else {
						sqlAlterar.setNull(3, 0);
					}
					sqlAlterar.setInt(4, politicaDivulgacaoMatriculaOnlineVO.getCodigo());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSituacaoPoliticaDivulgacaoMatriculaOnlineInativo(final PoliticaDivulgacaoMatriculaOnlineVO politicaDivulgacaoMatriculaOnlineVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			AvaliacaoOnline.alterar(getIdEntidade(), verificarAcesso, usuarioVO);
			final String sql = "UPDATE politicaDivulgacaoMatriculaOnline" + " SET " + "situacao=?, datainativacao=?, responsavelinativacao=?  WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);

					sqlAlterar.setString(1, politicaDivulgacaoMatriculaOnlineVO.getSituacaoEnum().name());

					sqlAlterar.setTimestamp(2, Uteis.getDataJDBCTimestamp(politicaDivulgacaoMatriculaOnlineVO.getDataInativacao()));

					if (politicaDivulgacaoMatriculaOnlineVO.getResponsavelInativacao().getCodigo() != 0) {
						sqlAlterar.setInt(3, politicaDivulgacaoMatriculaOnlineVO.getResponsavelInativacao().getCodigo());
					} else {
						sqlAlterar.setNull(3, 0);
					}
					sqlAlterar.setInt(4, politicaDivulgacaoMatriculaOnlineVO.getCodigo());

					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	public List<PoliticaDivulgacaoMatriculaOnlineVO> consultarPorCodigoCurso(Integer codigoCurso, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		String sqlStr = "select * from politicadivulgacaomatriculaonline where curso = " + codigoCurso + " and situacao = 'ATIVO' order by nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario, null);
	}
	
	@Override
	public List<PoliticaDivulgacaoMatriculaOnlineVO> consultarBanners(UsuarioVO usuario) throws Exception {
		List<PoliticaDivulgacaoMatriculaOnlineVO> politicaDivulgacaoMatriculaOnlineVOs = new ArrayList<PoliticaDivulgacaoMatriculaOnlineVO>(0);
		if(!Uteis.isAtributoPreenchido(usuario) || !Uteis.isAtributoPreenchido(usuario.getPessoa())){
			return politicaDivulgacaoMatriculaOnlineVOs;
		}
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("	select distinct politicadivulgacaomatriculaonline.codigo as politicadivulgacaomatriculaonline, politicadivulgacaomatriculaonline.banner, politicadivulgacaomatriculaonline.nomearquivologo from politicadivulgacaomatriculaonline ");		
		sqlStr.append("	where politicadivulgacaomatriculaonline.situacao = 'ATIVO'");
		sqlStr.append(" and (not exists (select matricula.curso from matricula where aluno = ").append(usuario.getPessoa().getCodigo()).append(" and matricula.curso = politicadivulgacaomatriculaonline.curso)) ");
		sqlStr.append("	and ((politicadivulgacaomatriculaonline.periodoapresentacaopoliticadivulgacaomatriculaonline = 'PERIODO_FIXO' ");
		sqlStr.append("	and politicadivulgacaomatriculaonline.periodode <= now() and politicadivulgacaomatriculaonline.periodoate >= now() )");
		sqlStr.append("	or (politicadivulgacaomatriculaonline.periodoapresentacaopoliticadivulgacaomatriculaonline = 'INDETERMINADO'))");
		if(usuario.getIsApresentarVisaoAluno()) {
			sqlStr.append(" and politicadivulgacaomatriculaonline.divulgarparaaluno = 't'");
			sqlStr.append(" and ((");
			sqlStr.append(" select codigo from politicadivulgacaomatriculaonlinepublicoalvo");
			sqlStr.append(" where politicadivulgacaomatriculaonlinepublicoalvo.politicadivulgacaomatriculaonline = politicadivulgacaomatriculaonline.codigo");
			sqlStr.append(" and politicadivulgacaomatriculaonlinepublicoalvo.publicoalvo like 'ALUNO'");
			sqlStr.append(" limit 1");
			sqlStr.append("	) is null or (");
			sqlStr.append(getQueryConsultaDivulgarParaAluno(usuario.getPessoa()));
			sqlStr.append(") is not null)");
		} else if(usuario.getIsApresentarVisaoProfessor()) {
			sqlStr.append(" and politicadivulgacaomatriculaonline.divulgarparaprofessor = 't'");
			sqlStr.append(" and ((");
			sqlStr.append(" select codigo from politicadivulgacaomatriculaonlinepublicoalvo");
			sqlStr.append(" where politicadivulgacaomatriculaonlinepublicoalvo.politicadivulgacaomatriculaonline = politicadivulgacaomatriculaonline.codigo");
			sqlStr.append(" and politicadivulgacaomatriculaonlinepublicoalvo.publicoalvo like 'PROFESSOR'");
			sqlStr.append(" limit 1");
			sqlStr.append("	) is null or (");
			sqlStr.append(getQueryConsultaDivulgarParaProfessor(usuario.getPessoa()));
			sqlStr.append(") is not null)");
		} else if(usuario.getIsApresentarVisaoCoordenador()) {
			sqlStr.append(" and politicadivulgacaomatriculaonline.divulgarparacoordenador = 't'");
			sqlStr.append(" and ((");
			sqlStr.append(" select codigo from politicadivulgacaomatriculaonlinepublicoalvo");
			sqlStr.append(" where politicadivulgacaomatriculaonlinepublicoalvo.politicadivulgacaomatriculaonline = politicadivulgacaomatriculaonline.codigo");
			sqlStr.append(" and politicadivulgacaomatriculaonlinepublicoalvo.publicoalvo like 'COORDENADOR'");
			sqlStr.append(" limit 1");
			sqlStr.append("	) is null or (");
			sqlStr.append(getQueryConsultaDivulgarParaCoordenador(usuario.getPessoa()));
			sqlStr.append(") is not null)");
		} else if(usuario.getIsApresentarVisaoAdministrativa()) {
			sqlStr.append(" and politicadivulgacaomatriculaonline.divulgarparafuncionario = 't'");
			sqlStr.append(" and ((");
			sqlStr.append(" select codigo from politicadivulgacaomatriculaonlinepublicoalvo");
			sqlStr.append(" where politicadivulgacaomatriculaonlinepublicoalvo.politicadivulgacaomatriculaonline = politicadivulgacaomatriculaonline.codigo");
			sqlStr.append(" and politicadivulgacaomatriculaonlinepublicoalvo.publicoalvo like 'FUNCIONARIO'");
			sqlStr.append(" limit 1");
			sqlStr.append("	) is null or (");
			sqlStr.append(getQueryConsultaDivulgarParaFuncionario(usuario.getPessoa()));
			sqlStr.append(") is not null)");
		}
		
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		PoliticaDivulgacaoMatriculaOnlineVO politicaDivulgacaoMatriculaOnlineVO = null;		
		while(rs.next()) {
			politicaDivulgacaoMatriculaOnlineVO = new PoliticaDivulgacaoMatriculaOnlineVO();
			politicaDivulgacaoMatriculaOnlineVO.setCodigo(rs.getInt("politicadivulgacaomatriculaonline"));
			politicaDivulgacaoMatriculaOnlineVO.setCaminhoBaseLogo(rs.getString("banner"));
			politicaDivulgacaoMatriculaOnlineVO.setNomeArquivoLogo(rs.getString("nomearquivologo"));
			politicaDivulgacaoMatriculaOnlineVOs.add(politicaDivulgacaoMatriculaOnlineVO);
		}
		return politicaDivulgacaoMatriculaOnlineVOs;
	}
	
	public StringBuilder getQueryConsultaDivulgarParaAluno(PessoaVO aluno) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("	select politicadivulgacaomatriculaonlinepublicoalvo.codigo from politicadivulgacaomatriculaonlinepublicoalvo");
		query.append("	inner join matricula on matricula.unidadeensino = politicadivulgacaomatriculaonlinepublicoalvo.unidadeensino");
		query.append("	inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula and matriculaperiodo.codigo = (");
		query.append("		select codigo from matriculaperiodo mp where mp.matricula = matricula.matricula and mp.situacaomatriculaperiodo != 'PC' order by ano||'/'||semestre desc limit 1");
		query.append("	)");
		query.append("	inner join curso on curso.codigo = matricula.curso");
		query.append("	inner join periodoletivo on periodoletivo.codigo = matriculaperiodo.periodoletivomatricula");
		query.append("	where matricula.aluno = ").append(aluno.getCodigo()).append(" and politicadivulgacaomatriculaonlinepublicoalvo.publicoalvo like 'ALUNO'");
		query.append("	and politicadivulgacaomatriculaonlinepublicoalvo.politicadivulgacaomatriculaonline = politicadivulgacaomatriculaonline.codigo	");
		query.append("	and (politicadivulgacaomatriculaonlinepublicoalvo.niveleducacional is null or politicadivulgacaomatriculaonlinepublicoalvo.niveleducacional = curso.niveleducacional)");
		query.append("	and (politicadivulgacaomatriculaonlinepublicoalvo.turno is null or politicadivulgacaomatriculaonlinepublicoalvo.turno = matricula.turno)");
		query.append("	and (politicadivulgacaomatriculaonlinepublicoalvo.turma is null or politicadivulgacaomatriculaonlinepublicoalvo.turma = matriculaperiodo.turma) ");
		query.append("	and ((politicadivulgacaomatriculaonlinepublicoalvo.alunoativo = false and politicadivulgacaomatriculaonlinepublicoalvo.alunoformado = false) ");
		query.append("	or ( politicadivulgacaomatriculaonlinepublicoalvo.alunoativo and politicadivulgacaomatriculaonlinepublicoalvo.alunoformado  and matricula.situacao in ('AT', 'FO') )");
		query.append("	or ( politicadivulgacaomatriculaonlinepublicoalvo.alunoativo and matricula.situacao in ('AT') )");
		query.append("	or ( politicadivulgacaomatriculaonlinepublicoalvo.alunoformado  and matricula.situacao in ('FO') )) ");
		query.append("	and ( politicadivulgacaomatriculaonlinepublicoalvo.periodo = 'TODOS'  ");
		query.append("	or (politicadivulgacaomatriculaonlinepublicoalvo.periodo = 'FAIXA_PERIODO' and periodoletivo.periodoletivo >= politicadivulgacaomatriculaonlinepublicoalvo.periodoletivode and periodoletivo.periodoletivo <= politicadivulgacaomatriculaonlinepublicoalvo.periodoletivoate)");
		query.append("	)");
		query.append("	limit 1	");
		return query;
	}
	
	public StringBuilder getQueryConsultaDivulgarParaProfessor(PessoaVO professor) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("select distinct politicadivulgacaomatriculaonlinepublicoalvo.codigo from politicadivulgacaomatriculaonlinepublicoalvo ");
		query.append("inner join (");
		query.append("	select distinct turma.codigo as turma, turma.unidadeensino, turma.curso, turma.turno, curso.niveleducacional from horarioturma");
		query.append("	inner join turma on turma.codigo = horarioturma.turma");
		query.append("	inner join curso on turma.curso = curso.codigo");		
		query.append("	where anovigente =  '").append(Uteis.getAnoDataAtual()).append("' and semestrevigente = '").append(Uteis.getSemestreAtual()).append("' ");
		query.append("	and curso.periodicidade =  'SE' and turma.situacao =  'AB'");
		query.append("	and exists (select horarioturmadia.codigo from horarioturmadia inner join horarioturmadiaitem on horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia where horarioturmadia.horarioturma = horarioturma.codigo and horarioturmadiaitem.professor = ").append(professor.getCodigo()).append(" limit 1)");
		query.append("	union");
		query.append("	select distinct turmaprincipal.codigo as turma, turma.unidadeensino, turmaprincipal.curso as curso, turmaprincipal.turno, curso.niveleducacional from horarioturma");
		query.append("	inner join turma on turma.codigo = horarioturma.turma");
		query.append("	inner join turmaagrupada on turma.codigo = turmaagrupada.turmaorigem");
		query.append("	inner join turma turmaprincipal on turmaprincipal.codigo = turmaagrupada.turma");
		query.append("	inner join curso on turmaprincipal.curso = curso.codigo");		
		query.append("	where anovigente =  '").append(Uteis.getAnoDataAtual()).append("' and semestrevigente = '").append(Uteis.getSemestreAtual()).append("' ");
		query.append("	and curso.periodicidade =  'SE' and turma.situacao =  'AB'");
		query.append("	and exists (select horarioturmadia.codigo from horarioturmadia inner join horarioturmadiaitem on horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia where horarioturmadia.horarioturma = horarioturma.codigo and horarioturmadiaitem.professor = ").append(professor.getCodigo()).append(" limit 1)");
		query.append("	union");
		query.append("	select distinct turma.codigo as turma, turma.unidadeensino, turma.curso, turma.turno, curso.niveleducacional from horarioturma");
		query.append("	inner join turma on turma.codigo = horarioturma.turma");
		query.append("	inner join curso on turma.curso = curso.codigo");
		query.append("	where anovigente =  '").append(Uteis.getAnoDataAtual()).append("' ");
		query.append("	and curso.periodicidade =  'AN' and turma.situacao =  'AB'");
		query.append("	and exists (select horarioturmadia.codigo from horarioturmadia inner join horarioturmadiaitem on horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia where horarioturmadia.horarioturma = horarioturma.codigo and horarioturmadiaitem.professor = ").append(professor.getCodigo()).append(" limit 1)");
		query.append("	union");
		query.append("	select distinct turmaprincipal.codigo as turma, turma.unidadeensino, turmaprincipal.curso as curso, turmaprincipal.turno, curso.niveleducacional from horarioturma");
		query.append("	inner join turma on turma.codigo = horarioturma.turma");
		query.append("	inner join turmaagrupada on turma.codigo = turmaagrupada.turmaorigem");
		query.append("	inner join turma turmaprincipal on turmaprincipal.codigo = turmaagrupada.turma");
		query.append("	inner join curso on turmaprincipal.curso = curso.codigo");		
		query.append("	where anovigente =  '").append(Uteis.getAnoDataAtual()).append("' ");
		query.append("	and curso.periodicidade =  'AN' and turma.situacao =  'AB'");
		query.append("	and exists (select horarioturmadia.codigo from horarioturmadia inner join horarioturmadiaitem on horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia where horarioturmadia.horarioturma = horarioturma.codigo and horarioturmadiaitem.professor = ").append(professor.getCodigo()).append(" limit 1)");
		query.append("	union");
		query.append("	select distinct turma.codigo as turma, turma.unidadeensino, turma.curso, turma.turno, curso.niveleducacional from horarioturma");
		query.append("	inner join turma on turma.codigo = horarioturma.turma");
		query.append("	inner join curso on turma.curso = curso.codigo");		
		query.append("	where anovigente =  '").append(Uteis.getAnoDataAtual()).append("' ");
		query.append("	and curso.periodicidade =  'IN' and turma.situacao =  'AB'	");
		query.append("	and exists (select horarioturmadia.codigo from horarioturmadia inner join horarioturmadiaitem on horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia where horarioturmadia.horarioturma = horarioturma.codigo and horarioturmadiaitem.professor = ").append(professor.getCodigo()).append(" limit 1)");
		query.append("	union");
		query.append("	select distinct turmaprincipal.codigo as turma, turma.unidadeensino, turmaprincipal.curso as curso, turmaprincipal.turno, curso.niveleducacional from horarioturma");
		query.append("	inner join turma on turma.codigo = horarioturma.turma");
		query.append("	inner join turmaagrupada on turma.codigo = turmaagrupada.turmaorigem");
		query.append("	inner join turma turmaprincipal on turmaprincipal.codigo = turmaagrupada.turma");
		query.append("	inner join curso on turmaprincipal.curso = curso.codigo");		
		query.append("	where curso.periodicidade =  'IN' and turma.situacao =  'AB'");
		query.append("	and exists (select horarioturmadia.codigo from horarioturmadia inner join horarioturmadiaitem on horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia where horarioturmadia.horarioturma = horarioturma.codigo and horarioturmadiaitem.professor = ").append(professor.getCodigo()).append(" limit 1)");
		query.append("	union");
		query.append("	select distinct programacaotutoriaonline.turma, programacaotutoriaonline.unidadeensino, programacaotutoriaonline.curso, turma.turno, programacaotutoriaonline.niveleducacional ");
		query.append("	from programacaotutoriaonline");
		query.append("	inner join programacaotutoriaonlineprofessor on programacaotutoriaonlineprofessor.programacaotutoriaonline = programacaotutoriaonline.codigo	");
		query.append("	inner join turma on turma.codigo = programacaotutoriaonline.turma");
		query.append("	inner join curso on turma.curso = curso.codigo");
		query.append("	where programacaotutoriaonlineprofessor.professor = ").append(professor.getCodigo());
		query.append("	and curso.periodicidade =  'SE' and turma.situacao =  'AB'");
		query.append("	union");
		query.append("	select distinct programacaotutoriaonline.turma, programacaotutoriaonline.unidadeensino, programacaotutoriaonline.curso, turma.turno, programacaotutoriaonline.niveleducacional ");
		query.append("	from programacaotutoriaonline");
		query.append("	inner join programacaotutoriaonlineprofessor on programacaotutoriaonlineprofessor.programacaotutoriaonline = programacaotutoriaonline.codigo");
		query.append("	inner join turma on turma.codigo = programacaotutoriaonline.turma");
		query.append("	inner join curso on turma.curso = curso.codigo");
		query.append("	where programacaotutoriaonlineprofessor.professor = ").append(professor.getCodigo());
		query.append("	and curso.periodicidade =  'AN' and turma.situacao =  'AB'");
		query.append("	union");
		query.append("	select distinct programacaotutoriaonline.turma, programacaotutoriaonline.unidadeensino, programacaotutoriaonline.curso, turma.turno, programacaotutoriaonline.niveleducacional ");
		query.append("	from programacaotutoriaonline");
		query.append("	inner join programacaotutoriaonlineprofessor on programacaotutoriaonlineprofessor.programacaotutoriaonline = programacaotutoriaonline.codigo");
		query.append("	inner join turma on turma.codigo = programacaotutoriaonline.turma");
		query.append("	inner join curso on turma.curso = curso.codigo");
		query.append("	where programacaotutoriaonlineprofessor.professor = ").append(professor.getCodigo());
		query.append("	and curso.periodicidade =  'IN' and turma.situacao =  'AB'");
		query.append(") as t");
		query.append("	on t.unidadeensino = politicadivulgacaomatriculaonlinepublicoalvo.unidadeensino");
		query.append("	and (politicadivulgacaomatriculaonlinepublicoalvo.curso is null  or t.curso = politicadivulgacaomatriculaonlinepublicoalvo.curso)");
		query.append("	and (politicadivulgacaomatriculaonlinepublicoalvo.turno is null  or t.turno = politicadivulgacaomatriculaonlinepublicoalvo.turno)");
		query.append("	and (politicadivulgacaomatriculaonlinepublicoalvo.niveleducacional is null or politicadivulgacaomatriculaonlinepublicoalvo.niveleducacional =  '' ");
		query.append("	or t.niveleducacional = politicadivulgacaomatriculaonlinepublicoalvo.niveleducacional)");
		query.append("	where politicadivulgacaomatriculaonlinepublicoalvo.publicoalvo like 'PROFESSOR'	");
		query.append("	and politicadivulgacaomatriculaonlinepublicoalvo.politicadivulgacaomatriculaonline = politicadivulgacaomatriculaonline.codigo limit 1");
		return query;
	}
	
	public StringBuilder getQueryConsultaDivulgarParaCoordenador(PessoaVO coordenador) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("select distinct politicadivulgacaomatriculaonlinepublicoalvo.codigo from politicadivulgacaomatriculaonlinepublicoalvo ");
		query.append("inner join (");
		query.append("	select distinct cursocoordenador.turma as turma, cursocoordenador.unidadeensino, cursocoordenador.curso, turma.turno, curso.niveleducacional from cursocoordenador");
		query.append("	inner join funcionario on funcionario.codigo = cursocoordenador.funcionario");
		query.append("	inner join curso on curso.codigo = cursocoordenador.curso");
		query.append("	left join turma on turma.codigo = cursocoordenador.turma");
		query.append("	where funcionario.pessoa = ").append(coordenador.getCodigo());
		query.append("	) as t");
		query.append("	on t.unidadeensino = politicadivulgacaomatriculaonlinepublicoalvo.unidadeensino");
		query.append("	and (politicadivulgacaomatriculaonlinepublicoalvo.curso is null  or t.curso = politicadivulgacaomatriculaonlinepublicoalvo.curso)");
		query.append("	and (politicadivulgacaomatriculaonlinepublicoalvo.turno is null  or t.turno = politicadivulgacaomatriculaonlinepublicoalvo.turno)");
		query.append("	and (politicadivulgacaomatriculaonlinepublicoalvo.niveleducacional is null or politicadivulgacaomatriculaonlinepublicoalvo.niveleducacional =  '' ");
		query.append("	or t.niveleducacional = politicadivulgacaomatriculaonlinepublicoalvo.niveleducacional)");
		query.append("	and (politicadivulgacaomatriculaonlinepublicoalvo.turma is null or politicadivulgacaomatriculaonlinepublicoalvo.turma = t.turma)");
		query.append("	where politicadivulgacaomatriculaonlinepublicoalvo.publicoalvo like 'COORDENADOR'	");
		query.append("	and politicadivulgacaomatriculaonlinepublicoalvo.politicadivulgacaomatriculaonline = politicadivulgacaomatriculaonline.codigo limit 1");
		return query;
	}
	
	public StringBuilder getQueryConsultaDivulgarParaFuncionario(PessoaVO funcionario) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("select distinct politicadivulgacaomatriculaonlinepublicoalvo.codigo from politicadivulgacaomatriculaonlinepublicoalvo ");
		query.append("inner join (");
		query.append("	select distinct funcionariocargo.unidadeensino as unidadeensino, departamento.codigo as departamento, cargo.codigo as cargo, funcionario.escolaridade from funcionariocargo");
		query.append("	inner join funcionario on funcionario.codigo = funcionariocargo.funcionario");
		query.append("	inner join cargo on cargo.codigo = funcionariocargo.cargo");
		query.append("	inner join departamento on departamento.codigo = cargo.departamento");
		query.append("	where funcionario.pessoa = ").append(funcionario.getCodigo());
		query.append("	) as t");
		query.append("	on t.unidadeensino = politicadivulgacaomatriculaonlinepublicoalvo.unidadeensino");
		query.append("	and (politicadivulgacaomatriculaonlinepublicoalvo.departamento is null  or t.departamento = politicadivulgacaomatriculaonlinepublicoalvo.departamento)");
		query.append("	and (politicadivulgacaomatriculaonlinepublicoalvo.cargo is null  or t.cargo = politicadivulgacaomatriculaonlinepublicoalvo.cargo)");
		query.append("	and (politicadivulgacaomatriculaonlinepublicoalvo.escolaridade is null or politicadivulgacaomatriculaonlinepublicoalvo.escolaridade =  '' ");
		query.append("	or t.escolaridade = politicadivulgacaomatriculaonlinepublicoalvo.escolaridade)");
		query.append("	where politicadivulgacaomatriculaonlinepublicoalvo.publicoalvo like 'FUNCIONARIO'");
		query.append("	and politicadivulgacaomatriculaonlinepublicoalvo.politicadivulgacaomatriculaonline = politicadivulgacaomatriculaonline.codigo limit 1");
		return query;
	}
	
	@Override
	public Integer consultarCodigoCurso(Integer codigoBanner, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		String sqlStr = "select curso from politicadivulgacaomatriculaonline where codigo = " + codigoBanner + " and situacao = 'ATIVO'";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		Integer codigoCurso = 0;
		if(tabelaResultado.next()) {
			codigoCurso = tabelaResultado.getInt("curso");
		}
		return codigoCurso;
	}
	
	@Override
	public List<BannerObject> consultarBannersAtivosDivulgacaoParaComunidadeMatriculaOnlineExterna(ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select codigo, curso, banner, nomearquivologo, situacao, descricao from politicadivulgacaomatriculaonline ");
		sqlStr.append("where politicadivulgacaomatriculaonline.situacao = 'ATIVO' ");
		sqlStr.append("and  politicadivulgacaomatriculaonline.divulgarparacomunidade = 't' ");
		sqlStr.append("	and ((politicadivulgacaomatriculaonline.periodoapresentacaopoliticadivulgacaomatriculaonline = 'PERIODO_FIXO' ");
		sqlStr.append("	and politicadivulgacaomatriculaonline.periodode <= now() and politicadivulgacaomatriculaonline.periodoate >= now() )");
		sqlStr.append("	or (politicadivulgacaomatriculaonline.periodoapresentacaopoliticadivulgacaomatriculaonline = 'INDETERMINADO'))");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		BannerObject bannerObject = null;
		List<BannerObject> bannerObjects = new ArrayList<BannerObject>(0);
		while(tabelaResultado.next()) {
			bannerObject = new BannerObject();
			bannerObject.setCodigoBanner(tabelaResultado.getInt("codigo"));
			bannerObject.setCaminhoWebImagem(configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo() + "/" + tabelaResultado.getString("banner").replaceAll("\\\\", "/") + "/" + tabelaResultado.getString("nomearquivologo"));
			bannerObject.getCursoObject().setCodigo(tabelaResultado.getInt("curso"));
			bannerObject.setDescricao(tabelaResultado.getString("descricao"));
			bannerObjects.add(bannerObject);
		}
		return bannerObjects;
	}
}
