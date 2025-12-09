package negocio.facade.jdbc.compras;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Strings;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.QuestionarioRespostaOrigemVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.academico.enumeradores.MesAnoEnum;
import negocio.comuns.academico.enumeradores.SituacaoQuestionarioRespostaOrigemEnum;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.CategoriaProdutoVO;
import negocio.comuns.compras.CotacaoVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.compras.RequisicaoItemVO;
import negocio.comuns.compras.RequisicaoVO;
import negocio.comuns.compras.enumeradores.TipoAutorizacaoRequisicaoEnum;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.financeiro.CentroResultadoVO;
import negocio.comuns.financeiro.enumerador.TipoNivelCentroResultadoEnum;
import negocio.comuns.planoorcamentario.PlanoOrcamentarioVO;
import negocio.comuns.processosel.QuestionarioVO;
import negocio.comuns.processosel.enumeradores.EscopoPerguntaEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.OrigemArquivo;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.compras.RequisicaoInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class Requisicao extends ControleAcesso implements RequisicaoInterfaceFacade {

	private static final long serialVersionUID = 6264217504826610219L;

	protected static String idEntidade = "Requisicao";

	public void verificarPermissaoAutorizarIndeferir(UsuarioVO usuario) throws Exception {
		super.verificarPermissaoUsuarioFuncionalidade("AutorizarRequisicao", usuario);
	}

	@Override
	public void validarDados(RequisicaoVO obj, UsuarioVO usuario) {
		try {
			if (!obj.isValidarDados().booleanValue()) {
				return;
			}			
			Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getDataRequisicao()), "O campo Data (Requisição) deve ser informado.");
			Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getResponsavelRequisicao()), "O campo Responsável (Requisição) deve ser informado.");
			Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getSolicitanteRequisicao().getPessoa()), "O campo Requisitante (Requisição) deve ser informado.");
			Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getCategoriaProduto()), "O campo Categoria de Produto (Requisição) deve ser informado.");
			Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getCategoriaDespesa()), "O campo Categoria de Despesa (Requisição) deve ser informado.");
			Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getUnidadeEnsino()), "O campo Unidade Ensino (Requisição) deve ser informado.");
			Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getTipoNivelCentroResultadoEnum()), "O campo Requisição Para (Requisição) deve ser informado.");
			Uteis.checkState((obj.getCategoriaDespesa().getExigeCentroCustoRequisitante() && !Uteis.isAtributoPreenchido(obj.getFuncionarioCargoVO())), "O campo Centro de Custo (Requisição) deve ser informado.");
			//Uteis.checkState(obj.getTipoNivelCentroResultadoEnum().isCurso() && !Uteis.isAtributoPreenchido(obj.getCurso()), "O campo Curso (Requisição) deve ser informado.");
			//Uteis.checkState(obj.getTipoNivelCentroResultadoEnum().isCursoTurno() && !Uteis.isAtributoPreenchido(obj.getTurno()), "O campo Turno (Requisição) deve ser informado.");
			//Uteis.checkState(obj.getTipoNivelCentroResultadoEnum().isTurma() && !Uteis.isAtributoPreenchido(obj.getTurma()), "O campo Turma (Requisição) deve ser informado.");		
			Uteis.checkState((obj.getTipoNivelCentroResultadoEnum().isDepartamento() && !Uteis.isAtributoPreenchido(obj.getDepartamento())), "O campo Departamento (Requisição) deve ser informado.");
			Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getCentroResultadoAdministrativo()), "O campo Centro de Resultado (Requisição) deve ser informado.");
			Uteis.checkState(obj.getRequisicaoItemVOs().isEmpty(), "O campo Item da Requisição (Itens da Requisição) deve ser informado.");
			if (obj.isExigeCompraCotacao()) {
				obj.setTipoSacado("");
				obj.setSacadoFuncionario(new FuncionarioVO());
				obj.setSacadoFornecedor(new FornecedorVO());
			} else {
				validarSeRequisicaoDeveInformaSacado(obj);
			}
			
			if(obj.getCategoriaProduto().getObrigarDataNecessidadeRequisicao()) {
				Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getDataNecessidadeRequisicao()), "O campo Data Necessidade da  (Requisição) deve ser informado.");
			}			
			
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	private void validarSeRequisicaoDeveInformaSacado(RequisicaoVO obj) {
		Boolean possuiSacado = false;
		if (Uteis.isAtributoPreenchido(obj.getSacadoFornecedor())) {
			obj.getSacadoFuncionario().setCodigo(0);
			possuiSacado = true;
		}
		if (Uteis.isAtributoPreenchido(obj.getSacadoFuncionario())) {
			obj.getSacadoFornecedor().setCodigo(0);
			possuiSacado = true;
		}
		if (!possuiSacado) {
			throw new StreamSeiException("Para este tipo requisição é necessário informar o SACADO (ou destinatário) da mesma.");
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public synchronized void verificarBloqueioRequisicao(Integer tipoBloqueio, final boolean bloqueio, final RequisicaoVO requisicaoVO) {
		if (tipoBloqueio.equals(Uteis.BLOQUEIO_VERIFICAR)) {
			String sql = "SELECT bloqueio FROM Requisicao WHERE (codigo = ?)";
			SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, requisicaoVO.getCodigo());
			boolean bloqueioVerificar = false;
			if (resultado.next()) {
				bloqueioVerificar = resultado.getBoolean("bloqueio");
			}
			if (bloqueioVerificar) {
				throw new StreamSeiException("Requisição já está bloqueada para alterações, pois o responsável por sua autorização já iniciou sua avaliação.");
			}
			return;
		} else if (tipoBloqueio.equals(Uteis.BLOQUEIO_GERAR)) {
			final String sql = "UPDATE Requisicao set bloqueio=? WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setBoolean(1, bloqueio);
					sqlAlterar.setInt(2, requisicaoVO.getCodigo());
					return sqlAlterar;
				}
			});
			return;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(RequisicaoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) {
		this.persistir(obj, verificarAcesso, usuarioVO, false);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistirComQuestionarioAbertura(RequisicaoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO, boolean movimentarPlanoOrcamentario) throws Exception {
		getFacadeFactory().getQuestionarioRespostaOrigemInterfaceFacade().incluir(obj.getQuestionarioRespostaOrigemAberturaVO(), usuarioVO);
		this.persistir(obj, verificarAcesso, usuarioVO, movimentarPlanoOrcamentario);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(RequisicaoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO, boolean movimentarPlanoOrcamentario) {
		boolean novoObj = obj.getNovoObj();
		try {
			
			validarDados(obj, usuarioVO);
			if (movimentarPlanoOrcamentario &&  !obj.getSituacaoAutorizacao().equals("IN")) {
				getFacadeFactory().getRequisicaoFacade().validarDadosBaixaPlanoOrcamentario(obj, usuarioVO, obj.getDepartamento(), obj.getSituacaoAutorizacao().equals("PE"));				
			}
			if (obj.getCodigo() == 0) {	
				incluir(obj, verificarAcesso, usuarioVO);
				if(Uteis.isAtributoPreenchido(obj.getQuestionarioRespostaOrigemAberturaVO().getQuestionarioVO())) {
					getFacadeFactory().getQuestionarioRespostaOrigemInterfaceFacade().incluir(obj.getQuestionarioRespostaOrigemAberturaVO(), usuarioVO);
					alterarQuestionarioAberturaRequisicao(obj, usuarioVO);
				}
			} else {
				alterar(obj, verificarAcesso, usuarioVO);
			}
			validarSeRegistroForamExcluidoDasListaSubordinadas(obj.getRequisicaoItemVOs(), "requisicaoItem", idEntidade, obj.getCodigo(), usuarioVO);			
			getFacadeFactory().getRequisicaoItemFacade().persistir(obj.getRequisicaoItemVOs(), usuarioVO);
		} catch (Exception e) {
			if(novoObj) {
				obj.setCodigo(0);
				obj.setNovoObj(true);
				for(RequisicaoItemVO requisicaoItemVO: obj.getRequisicaoItemVOs()) {
					requisicaoItemVO.setCodigo(0);
					requisicaoItemVO.setNovoObj(true);
				}
			}
			throw new StreamSeiException(e);
		}
	}

	private void incluir(final RequisicaoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			Requisicao.incluir(getIdEntidade(), verificarAcesso, usuarioVO);
			final String sql = "INSERT INTO Requisicao( responsavelRequisicao, dataRequisicao, unidadeEnsino, departamento, " 
			+ "situacaoEntrega, situacaoAutorizacao, dataAutorizacao, responsavelAutorizacao, motivoSituacaoAutorizacao," 
			+ " sacadoFornecedor, sacadoFuncionario, tipoSacado, turma, categoriaProduto, categoriaDespesa , curso , " 
			+ " turno, dataNecessidadeRequisicao, solicitanteRequisicao, funcionariocargo, centroResultadoAdministrativo, " 
			+ " tipoNivelCentroResultadoEnum ) " 
			+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " 
			+ " returning codigo " 
			+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					if (obj.getResponsavelRequisicao().getCodigo().intValue() != 0) {
						sqlInserir.setInt(1, obj.getResponsavelRequisicao().getCodigo().intValue());
					} else {
						sqlInserir.setNull(1, 0);
					}
					sqlInserir.setTimestamp(2, Uteis.getDataJDBCTimestamp(obj.getDataRequisicao()));
					if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
						sqlInserir.setInt(3, obj.getUnidadeEnsino().getCodigo().intValue());
					} else {
						sqlInserir.setNull(3, 0);
					}
					if (obj.getDepartamento().getCodigo().intValue() != 0) {
						sqlInserir.setInt(4, obj.getDepartamento().getCodigo().intValue());
					} else {
						sqlInserir.setNull(4, 0);
					}
					sqlInserir.setString(5, obj.getSituacaoEntrega());
					sqlInserir.setString(6, obj.getSituacaoAutorizacao());
					sqlInserir.setTimestamp(7, Uteis.getDataJDBCTimestamp(obj.getDataAutorizacao()));
					if (obj.getResponsavelAutorizacao().getCodigo().intValue() != 0) {
						sqlInserir.setInt(8, obj.getResponsavelAutorizacao().getCodigo().intValue());
					} else {
						sqlInserir.setNull(8, 0);
					}
					sqlInserir.setString(9, obj.getMotivoSituacaoAutorizacao());
					if (obj.getSacadoFornecedor().getCodigo().intValue() != 0) {
						sqlInserir.setInt(10, obj.getSacadoFornecedor().getCodigo().intValue());
					} else {
						sqlInserir.setNull(10, 0);
					}
					if (obj.getSacadoFuncionario().getCodigo().intValue() != 0) {
						sqlInserir.setInt(11, obj.getSacadoFuncionario().getCodigo().intValue());
					} else {
						sqlInserir.setNull(11, 0);
					}
					sqlInserir.setString(12, obj.getTipoSacado());
					if (obj.getTurma().getCodigo().intValue() != 0) {
						sqlInserir.setInt(13, obj.getTurma().getCodigo().intValue());
					} else {
						sqlInserir.setNull(13, 0);
					}
					if (obj.getCategoriaProduto().getCodigo().intValue() != 0) {
						sqlInserir.setInt(14, obj.getCategoriaProduto().getCodigo().intValue());
					} else {
						sqlInserir.setNull(14, 0);
					}
					if (obj.getCategoriaDespesa().getCodigo().intValue() != 0) {
						sqlInserir.setInt(15, obj.getCategoriaDespesa().getCodigo().intValue());
					} else {
						sqlInserir.setNull(15, 0);
					}
					if (obj.getCurso().getCodigo().intValue() != 0) {
						sqlInserir.setInt(16, obj.getCurso().getCodigo().intValue());
					} else {
						sqlInserir.setNull(16, 0);
					}
					if (obj.getTurno().getCodigo().intValue() != 0) {
						sqlInserir.setInt(17, obj.getTurno().getCodigo().intValue());
					} else {
						sqlInserir.setNull(17, 0);
					}

					Uteis.setValuePreparedStatement(obj.getDataNecessidadeRequisicao(), 18, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getSolicitanteRequisicao(), 19, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getFuncionarioCargoVO(), 20, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getCentroResultadoAdministrativo(), 21, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getTipoNivelCentroResultadoEnum(), 22, sqlInserir);
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {

				public Object extractData(ResultSet arg0) throws SQLException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
			obj.setNovoObj(Boolean.FALSE);
			persistirArquivoRequisicao(obj, usuarioVO);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}

	private void alterar(final RequisicaoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			Requisicao.alterar(getIdEntidade(), verificarAcesso, usuarioVO);
			validarDados(obj, usuarioVO);
			final String sql = " UPDATE Requisicao set responsavelRequisicao=?, dataRequisicao=?, unidadeEnsino=?, " 
			+ " departamento=?, situacaoEntrega=?, situacaoAutorizacao=?, dataAutorizacao=?, responsavelAutorizacao=?, " 
			+ " motivoSituacaoAutorizacao=?, sacadoFornecedor=?, sacadoFuncionario=?, tipoSacado=?, turma=?, " 
			+ " categoriaProduto=?, categoriaDespesa=?, curso=?, turno=?, dataNecessidadeRequisicao=?, solicitanteRequisicao=?, " 
			+ " funcionariocargo=?, centroResultadoAdministrativo=?, " 
			+ " tipoNivelCentroResultadoEnum=? " 
			+ " WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					if (obj.getResponsavelRequisicao().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(1, obj.getResponsavelRequisicao().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(1, 0);
					}
					sqlAlterar.setTimestamp(2, Uteis.getDataJDBCTimestamp(obj.getDataRequisicao()));
					if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(3, obj.getUnidadeEnsino().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(3, 0);
					}
					if (obj.getDepartamento().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(4, obj.getDepartamento().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(4, 0);
					}
					sqlAlterar.setString(5, obj.getSituacaoEntrega());
					sqlAlterar.setString(6, obj.getSituacaoAutorizacao());
					sqlAlterar.setTimestamp(7, Uteis.getDataJDBCTimestamp(obj.getDataAutorizacao()));
					if (obj.getResponsavelAutorizacao().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(8, obj.getResponsavelAutorizacao().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(8, 0);
					}
					sqlAlterar.setString(9, obj.getMotivoSituacaoAutorizacao());

					if (obj.getSacadoFornecedor().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(10, obj.getSacadoFornecedor().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(10, 0);
					}
					if (obj.getSacadoFuncionario().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(11, obj.getSacadoFuncionario().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(11, 0);
					}
					sqlAlterar.setString(12, obj.getTipoSacado());
					if (obj.getTurma().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(13, obj.getTurma().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(13, 0);
					}
					if (obj.getCategoriaProduto().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(14, obj.getCategoriaProduto().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(14, 0);
					}
					if (obj.getCategoriaDespesa().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(15, obj.getCategoriaDespesa().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(15, 0);
					}
					if (obj.getCurso().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(16, obj.getCurso().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(16, 0);
					}

					if (obj.getTurno().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(17, obj.getTurno().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(17, 0);
					}

					Uteis.setValuePreparedStatement(obj.getDataNecessidadeRequisicao(), 18, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getSolicitanteRequisicao(), 19, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getFuncionarioCargoVO(), 20, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getCentroResultadoAdministrativo(), 21, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getTipoNivelCentroResultadoEnum(), 22, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getCodigo(), 23, sqlAlterar);
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}
	
	@Override
	public void alterarQuestionarioAberturaRequisicao(final RequisicaoVO obj, UsuarioVO usuarioVO) throws Exception {
			final String sql = " UPDATE Requisicao set questionarioRespostaOrigemAbertura =? " 
					+ " WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);					
					Uteis.setValuePreparedStatement(obj.getQuestionarioRespostaOrigemAberturaVO(), 1, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getCodigo(), 2, sqlAlterar);
					return sqlAlterar;
				}
			});		
	}

	@Override
	public void alterarQuestionarioFechamentoRequisicao(final RequisicaoVO obj, UsuarioVO usuarioVO) throws Exception {
		final String sql = " UPDATE Requisicao set questionarioRespostaOrigemFechamento =? " 
				+ " WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);					
				Uteis.setValuePreparedStatement(obj.getQuestionarioRespostaOrigemFechamentoVO(), 1, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getCodigo(), 2, sqlAlterar);
				return sqlAlterar;
			}
		});		
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void atualizarSituacaoRequisicaoPorOperacaoEmCotacao(CotacaoVO obj, UsuarioVO usuario) throws Exception {
		try {
			if(Uteis.isAtributoPreenchido(obj)) {
			StringBuilder sql = new StringBuilder("UPDATE Requisicao set situacaoAutorizacao=?, dataAutorizacao=?, responsavelAutorizacao=?, motivoSituacaoAutorizacao=? ");
			sql.append(" WHERE codigo in ( select distinct requisicao from requisicaoitem where cotacao  = ? and cotacao > 0) ");
			sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());					
					sqlAlterar.setString(1, obj.getSituacao());
					sqlAlterar.setDate(2, Uteis.getDataJDBC(new Date()));
					sqlAlterar.setInt(3, usuario.getCodigo());
					if(obj.getSituacao().equals("IN")) {
						sqlAlterar.setString(4, obj.getMotivoRevisao());
					}else {
						sqlAlterar.setString(4, "");
					}
					sqlAlterar.setInt(5, obj.getCodigo());
					return sqlAlterar;
				}
			});
			}
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSituacao(final Integer requisicao, final String situacao) throws Exception {
		final String sql = "UPDATE Requisicao set situacaoEntrega=? WHERE ((codigo = ?))";
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				sqlAlterar.setString(1, situacao);
				sqlAlterar.setInt(2, requisicao.intValue());
				return sqlAlterar;
			}
		});

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSituacaoEntrega(Integer requisicao) throws Exception {
		String sql = "select sum(quantidadeAutorizada) as autorizada, sum(quantidadeEntregue) as entregue from requisicaoItem where requisicao = " + requisicao;
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
		String situacao = "FI";
		if (tabelaResultado.next()) {
			if (tabelaResultado.getDouble("autorizada") > tabelaResultado.getDouble("entregue") && tabelaResultado.getDouble("entregue") > 0) {
				situacao = "PA";
			}
			if (tabelaResultado.getDouble("autorizada") > tabelaResultado.getDouble("entregue") && tabelaResultado.getDouble("entregue") == 0) {
				situacao = "PE";
			}
		}
		alterarSituacao(requisicao, situacao);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(RequisicaoVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			Requisicao.excluir(getIdEntidade(), true, usuarioVO);
			Uteis.checkState(!obj.isSituacaoAutorizacaoPendente(), "Não é possivel excluir uma requisição que não esteja com a situação Aguardando Autorização.");
			String sql = "DELETE FROM Requisicao WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(sql, obj.getCodigo());
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void preencherDadosPorCategoriaDespesa(RequisicaoVO obj, UsuarioVO usuario) throws Exception {
		try {
			CentroResultadoVO crAdministrativo = null;
			CursoVO cursoFiltro = null;
			TurmaVO turmaFiltro = null;
			if (Uteis.isAtributoPreenchido(obj.getFuncionarioCargoVO())) {
				obj.setFuncionarioCargoVO(getFacadeFactory().getFuncionarioCargoFacade().consultarPorChavePrimaria(obj.getFuncionarioCargoVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario));
				obj.setUnidadeEnsino(obj.getFuncionarioCargoVO().getUnidade());
				if (!Uteis.isAtributoPreenchido(obj.getDepartamento()) || obj.getCategoriaDespesa().getExigeCentroCustoRequisitante()) {
					obj.setDepartamento(Uteis.isAtributoPreenchido(obj.getFuncionarioCargoVO().getDepartamento()) ? obj.getFuncionarioCargoVO().getDepartamento() : obj.getFuncionarioCargoVO().getCargo().getDepartamento());
				}
			}

			if (obj.getTipoNivelCentroResultadoEnum().isUnidadeEnsino() && Uteis.isAtributoPreenchido(obj.getUnidadeEnsino())) {
				crAdministrativo = getFacadeFactory().getCentroResultadoFacade().consultarCentroResultadoPorUnidadeEnsino(obj.getUnidadeEnsino().getCodigo(), usuario);
			}
			if (obj.getTipoNivelCentroResultadoEnum().isDepartamento() && Uteis.isAtributoPreenchido(obj.getDepartamento())) {
				crAdministrativo = getFacadeFactory().getCentroResultadoFacade().consultarCentroResultadoPorDepartamento(obj.getDepartamento().getCodigo(), usuario);
			}
			if ((obj.getTipoNivelCentroResultadoEnum().isCurso() || obj.getTipoNivelCentroResultadoEnum().isCursoTurno()) && Uteis.isAtributoPreenchido(obj.getUnidadeEnsino()) && Uteis.isAtributoPreenchido(obj.getCurso()) ) {
				crAdministrativo = getFacadeFactory().getCentroResultadoFacade().consultarCentroResultadoPorUnidadeEnsinoCurso(obj.getUnidadeEnsino().getCodigo(), obj.getCurso().getCodigo(), usuario);
				cursoFiltro = obj.getCurso();
				turmaFiltro = null;
			}
			if (obj.getTipoNivelCentroResultadoEnum().isTurma() && Uteis.isAtributoPreenchido(obj.getTurma())) {
				crAdministrativo = getFacadeFactory().getCentroResultadoFacade().consultarCentroResultadoPorTurma(obj.getTurma().getCodigo(), usuario);
				turmaFiltro = obj.getTurma();
				cursoFiltro = null;
			}
			if(Uteis.isAtributoPreenchido(crAdministrativo) && getFacadeFactory().getCentroResultadoFacade().validarRestricaoUsoCentroResultado(crAdministrativo, obj.getDepartamento(), cursoFiltro, turmaFiltro, usuario)){
				obj.setCentroResultadoAdministrativo(Uteis.isAtributoPreenchido(obj.getCentroResultadoAdministrativo()) ? obj.getCentroResultadoAdministrativo() : crAdministrativo);	
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Override
	public void consultar(String codigo, String responsavel, String solicitante, String departamento, String categoria, String produtoCategoria, String autorizacao, String entrega, Integer unidadeEnsino, DataModelo dataModelo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) {
		try {
			dataModelo.setListaConsulta(consultarPorTodosOsTermos(codigo, responsavel, solicitante, departamento, categoria, produtoCategoria, autorizacao, entrega, unidadeEnsino, dataModelo, controlarAcesso, nivelMontarDados, usuario));
			dataModelo.getListaFiltros().clear();
			dataModelo.setTotalRegistrosEncontrados(consultarTotalTodosOsTermos(codigo, responsavel, solicitante, departamento, categoria, produtoCategoria, autorizacao, entrega, unidadeEnsino, dataModelo));
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	private List<RequisicaoVO> consultarPorTodosOsTermos(String codigo, String responsavel, String solicitante, String departamento, String categoria, String produtoCategoria, String autorizacao, String entrega, Integer unidadeEnsino, DataModelo dataModelo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder stringJoiner = new StringBuilder(" ");
		stringJoiner.append(" SELECT REQUISICAO.* FROM REQUISICAO");
		stringJoiner.append(" INNER JOIN USUARIO AS RESPONSAVEL ON REQUISICAO.RESPONSAVELREQUISICAO = RESPONSAVEL.CODIGO");
		stringJoiner.append(" INNER JOIN USUARIO AS SOLICITANTE ON REQUISICAO.SOLICITANTEREQUISICAO = SOLICITANTE.CODIGO");
		stringJoiner.append(" INNER JOIN CATEGORIAPRODUTO ON REQUISICAO.CATEGORIAPRODUTO = CATEGORIAPRODUTO.CODIGO");
		stringJoiner.append(" INNER JOIN UNIDADEENSINO ON REQUISICAO.UNIDADEENSINO = UNIDADEENSINO.CODIGO");
		stringJoiner.append(" LEFT JOIN DEPARTAMENTO ON REQUISICAO.DEPARTAMENTO = DEPARTAMENTO.CODIGO");
		stringJoiner.append(" WHERE 1=1");
		montarFiltrosConsulta(codigo, responsavel, solicitante, departamento, categoria, produtoCategoria, autorizacao, entrega, unidadeEnsino, dataModelo.getDataIni(), dataModelo.getDataFim(), stringJoiner);
		stringJoiner.append(" ORDER BY requisicao.codigo");
		UteisTexto.addLimitAndOffset(stringJoiner, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(stringJoiner.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);

	}

	private Integer consultarTotalTodosOsTermos(String codigo, String responsavel, String solicitante, String departamento, String categoria, String produtoCategoria, String autorizacao, String entrega, Integer unidadeEnsino, DataModelo dataModelo) throws Exception {
		try {
			StringBuilder stringJoiner = new StringBuilder(" ");
			stringJoiner.append(" SELECT count(REQUISICAO.codigo) as qtde FROM REQUISICAO");
			stringJoiner.append(" INNER JOIN USUARIO AS RESPONSAVEL ON REQUISICAO.RESPONSAVELREQUISICAO = RESPONSAVEL.CODIGO");
			stringJoiner.append(" INNER JOIN USUARIO AS SOLICITANTE ON REQUISICAO.SOLICITANTEREQUISICAO = SOLICITANTE.CODIGO");
			stringJoiner.append(" INNER JOIN CATEGORIAPRODUTO ON REQUISICAO.CATEGORIAPRODUTO = CATEGORIAPRODUTO.CODIGO");
			stringJoiner.append(" INNER JOIN UNIDADEENSINO ON REQUISICAO.UNIDADEENSINO = UNIDADEENSINO.CODIGO");
			stringJoiner.append(" LEFT JOIN DEPARTAMENTO ON REQUISICAO.DEPARTAMENTO = DEPARTAMENTO.CODIGO");
			stringJoiner.append(" WHERE 1=1");
			montarFiltrosConsulta(codigo, responsavel, solicitante, departamento, categoria, produtoCategoria, autorizacao, entrega, unidadeEnsino, dataModelo.getDataIni(), dataModelo.getDataFim(), stringJoiner);
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(stringJoiner.toString());
			return (Integer) Uteis.getSqlRowSetTotalizador(rs, "qtde", TipoCampoEnum.INTEIRO);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	private void montarFiltrosConsulta(String codigo, String responsavel, String solicitante, String departamento, String categoria, String produtoCategoria, String autorizacao, String entrega, Integer unidadeEnsino, Date inicio, Date fim, StringBuilder stringJoiner) {
		if (!Strings.isNullOrEmpty(codigo)) {
			stringJoiner.append(String.format(" and requisicao.codigo = %s", codigo));
			return;
		}
		if (!Strings.isNullOrEmpty(departamento)) {
			stringJoiner.append(String.format(" and lower(sem_acentos(departamento.nome)) ilike sem_acentos('%%%s%%')", departamento.toLowerCase()));
		}
		if (!Strings.isNullOrEmpty(categoria)) {
			stringJoiner.append(String.format(" and lower(sem_acentos(categoriaproduto.nome)) ilike sem_acentos('%%%s%%')", categoria.toLowerCase()));
		}
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			stringJoiner.append(" and unidadeensino.codigo =  ").append(unidadeEnsino);
		}
		if (!Strings.isNullOrEmpty(autorizacao)) {
			stringJoiner.append(String.format(" and requisicao.situacaoAutorizacao = '%s'", autorizacao));
		}
		if (!Strings.isNullOrEmpty(entrega)) {
			stringJoiner.append(String.format(" and requisicao.situacaoEntrega = '%s'", entrega));
		}
		if (Objects.nonNull(inicio) && Objects.nonNull(fim)) {
			stringJoiner.append(String.format(" and requisicao.dataRequisicao BETWEEN '%s'  AND '%s'", Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(inicio), Uteis.getDataComHoraSetadaParaUltimoMinutoDia(fim)));
		}

		if (!Strings.isNullOrEmpty(responsavel) && !Strings.isNullOrEmpty(solicitante)) {
			stringJoiner.append(String.format(" and ( (lower(sem_acentos(responsavel.nome)) ilike sem_acentos('%%%s%%')) ", responsavel.toLowerCase()));
			stringJoiner.append(String.format(" or (lower(sem_acentos(solicitante.nome)) ilike sem_acentos('%%%s%%')) )", responsavel.toLowerCase()));
		}
		if (!Strings.isNullOrEmpty(responsavel) && Strings.isNullOrEmpty(solicitante)) {
			stringJoiner.append(String.format(" and lower(sem_acentos(responsavel.nome)) ilike sem_acentos('%%%s%%')", responsavel.toLowerCase()));
		}
		if (!Strings.isNullOrEmpty(solicitante) && Strings.isNullOrEmpty(responsavel)) {
			stringJoiner.append(String.format(" and lower(sem_acentos(solicitante.nome)) ilike sem_acentos('%%%s%%')", solicitante.toLowerCase()));
		}

		if (!Strings.isNullOrEmpty(produtoCategoria)) {
			stringJoiner.append(" and requisicao.codigo in (");
			stringJoiner.append(" select requisicao.codigo from requisicao");
			stringJoiner.append(" left join requisicaoitem on requisicaoitem.requisicao = requisicao.codigo");
			stringJoiner.append(" left join produtoservico on requisicaoitem.produtoservico = produtoservico.codigo");
			stringJoiner.append(String.format(" where lower(sem_acentos(produtoservico.nome)) ilike sem_acentos('%%%s%%'))", produtoCategoria.toLowerCase()));
		}
	}

	public List<RequisicaoVO> consultarPorSituacaoPendente(boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM requisicao WHERE situacaoEntrega = 'PE'";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}
	
	
	public List<RequisicaoVO> consultarPorNomeDepartamento(String valorConsulta, Date dataIni, Date dataFim, String pendente, String parcial, String entregue, String situacaoAutorizacao, Integer unidadeEnsino, Integer usuarioPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT Requisicao.* FROM Requisicao, Departamento WHERE Requisicao.departamento = Departamento.codigo and upper( Departamento.nome ) like('" + valorConsulta.toUpperCase() + "%') ";
		sqlStr = montaFiltroConsultaModal(dataIni, dataFim, pendente, parcial, entregue, situacaoAutorizacao, unidadeEnsino, usuarioPrm, sqlStr);
		sqlStr += " ORDER BY Departamento.nome";

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	
	public List<RequisicaoVO> consultarPorNomeUsuario(String valorConsulta, Date dataIni, Date dataFim, String pendente, String parcial, String entregue, String situacaoAutorizacao, Integer unidadeEnsino, Integer usuarioPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Requisicao, Usuario WHERE (Requisicao.responsavelRequisicao = Usuario.codigo) and (lower (Usuario.nome) like ('" + valorConsulta.toLowerCase() + "%')) ";
		sqlStr = montaFiltroConsultaModal(dataIni, dataFim, pendente, parcial, entregue, situacaoAutorizacao, unidadeEnsino, usuarioPrm, sqlStr);		
		sqlStr += " ORDER BY Requisicao.codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);

	}

	public List<RequisicaoVO> consultarPorCodigo(Integer valorConsulta, Date dataIni, Date dataFim, String pendente, String parcial, String entregue, String situacaoAutorizacao, Integer unidadeEnsino, Integer usuarioPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Requisicao where 1=1 ";
		if (valorConsulta.intValue() != 0) {
			sqlStr += " and codigo = " + valorConsulta.intValue();
		}
		sqlStr = montaFiltroConsultaModal(dataIni, dataFim, pendente, parcial, entregue, situacaoAutorizacao, unidadeEnsino, usuarioPrm, sqlStr);
		sqlStr += " ORDER BY codigo";

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	private String montaFiltroConsultaModal(Date dataIni, Date dataFim, String pendente, String parcial, String entregue, String situacaoAutorizacao, Integer unidadeEnsino, Integer usuarioPrm, String sqlStr) {
		if(Uteis.isAtributoPreenchido(dataIni) && Uteis.isAtributoPreenchido(dataFim)){
			sqlStr += " and Requisicao.dataRequisicao between '" + Uteis.getDataJDBC(dataIni) + "' and '" + Uteis.getDataJDBC(dataFim) + "' ";	
		}

		if (Uteis.isAtributoPreenchido(situacaoAutorizacao)) {
			sqlStr += " and situacaoAutorizacao = '" + situacaoAutorizacao.toUpperCase() + "' ";
		}
		
		if(Uteis.isAtributoPreenchido(pendente) || Uteis.isAtributoPreenchido(parcial) || Uteis.isAtributoPreenchido(entregue)){
			String sqlSituacaoEntrega = " and situacaoEntrega in(";
			if (Uteis.isAtributoPreenchido(pendente) && "PE".equals(pendente)) {
				sqlSituacaoEntrega += "'" + pendente.toUpperCase() + "',";
				
			}
			if (Uteis.isAtributoPreenchido(parcial) && "PA".equals(parcial)) {
				sqlSituacaoEntrega += "'" + parcial.toUpperCase() + "',";
			}
			if (Uteis.isAtributoPreenchido(entregue)&& "FI".equals(entregue)) {
				sqlSituacaoEntrega += "'" + entregue.toUpperCase() + "',";
			}
			sqlStr = sqlStr + sqlSituacaoEntrega.substring(0, sqlSituacaoEntrega.length()-1) + ")";
		}		
		
		if (unidadeEnsino.intValue() != 0) {
			sqlStr += " and Requisicao.unidadeEnsino = " + unidadeEnsino.intValue();
		}
		
		if (usuarioPrm.intValue() != 0) {
			sqlStr += " and Requisicao.responsavelRequisicao = " + usuarioPrm.intValue();
		}
		return sqlStr;
	}

	@Override
	public List<RequisicaoVO> consultarPorCategoriaProdutoPorUnidadeEnsinoPorTipoAutorizacaoRequisicaoComSituacaoAutorizadaComSituacaoEntreguePendente(Integer categoriaProduto, List<UnidadeEnsinoVO> unidadeEnsinoVOs, TipoAutorizacaoRequisicaoEnum tipoAutorizacaoRequisicaoEnum, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE requisicao.situacaoAutorizacao = 'AU' ");
		sqlStr.append(" and requisicao.situacaoEntrega != 'FI' ");
		if (Uteis.isAtributoPreenchido(unidadeEnsinoVOs)) {
			sqlStr.append(" and requisicao.unidadeEnsino in (");
			for (UnidadeEnsinoVO ue : unidadeEnsinoVOs) {
				sqlStr.append(ue.getCodigo()).append(", ");
			}
			sqlStr.append("0) ");
		}
		if (Uteis.isAtributoPreenchido(categoriaProduto)) {
			sqlStr.append(" and requisicao.categoriaproduto = ").append(categoriaProduto);
		}
		if (Uteis.isAtributoPreenchido(tipoAutorizacaoRequisicaoEnum)) {
			sqlStr.append(" and requisicaoitem.tipoAutorizacaoRequisicao = '").append(tipoAutorizacaoRequisicaoEnum.name()).append("'");
			if (tipoAutorizacaoRequisicaoEnum.isCompraDireta()) {
				sqlStr.append(" and requisicaoitem.compraitem is null ");
			} else if (tipoAutorizacaoRequisicaoEnum.isCotacao()) {
				sqlStr.append(" and (requisicaoitem.cotacao is null or requisicaoitem.cotacao = 0) ");
			}
		}
		sqlStr.append(" ORDER BY requisicao.dataRequisicao ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<RequisicaoVO> vetResultado = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			RequisicaoVO req = montarDadosBasicaSemRequisicaoItem(tabelaResultado, nivelMontarDados, usuario);
			if (nivelMontarDados == Uteis.NIVELMONTARDADOS_TODOS) {
				req.setRequisicaoItemVOs(getFacadeFactory().getRequisicaoItemFacade().consultarRapidaRequisicaoItems(req, tipoAutorizacaoRequisicaoEnum, nivelMontarDados, usuario));
			}
			vetResultado.add(req);
		}
		return vetResultado;
	}

	@Override
	public RequisicaoVO consultarRapidaPorCodigo(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE requisicao.codigo = ? ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), codigoPrm);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( Requisicao ).");
		}
		return (montarDadosBasicaSemRequisicaoItem(tabelaResultado, nivelMontarDados, usuario));
	}

	public String consultarSituacaoAutorizacaoPorCodigo(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT situacaoAutorizacao FROM Requisicao WHERE codigo = " + valorConsulta.intValue();
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (tabelaResultado.next()) {
			return tabelaResultado.getString("situacaoAutorizacao");
		}
		return "";
	}

	private StringBuilder getSQLPadraoConsultaBasica() {
		StringBuilder sql = new StringBuilder(" ");
		sql.append(" SELECT DISTINCT ");
		sql.append(" requisicao.codigo as \"requisicao.codigo\", requisicao.dataRequisicao as \"requisicao.dataRequisicao\",   ");
		sql.append(" requisicao.situacaoEntrega as \"requisicao.situacaoEntrega\", requisicao.situacaoAutorizacao as \"requisicao.situacaoAutorizacao\",   ");
		//sql.append(" requisicao.tipoAutorizacaoRequisicao as \"requisicao.tipoAutorizacaoRequisicao\", ");
		sql.append(" requisicao.tipoNivelCentroResultadoEnum as \"requisicao.tipoNivelCentroResultadoEnum\", ");
		sql.append(" responsavel.codigo as \"responsavel.codigo\", responsavel.nome as \"responsavel.nome\",   ");
		sql.append(" solicitante.codigo as \"solicitante.codigo\", solicitante.nome as \"solicitante.nome\",   ");
		sql.append(" categoriaproduto.codigo as \"categoriaproduto.codigo\", categoriaproduto.nome as \"categoriaproduto.nome\",   ");
		sql.append(" unidadeensino.codigo as \"unidadeensino.codigo\", unidadeensino.nome as \"unidadeensino.nome\",   ");
		sql.append(" categoriaDespesa.codigo as \"categoriaDespesa.codigo\", categoriaDespesa.descricao as \"categoriaDespesa.descricao\",   ");
		sql.append(" categoriaDespesa.informarTurma as \"categoriaDespesa.informarTurma\", categoriaDespesa.nivelCategoriaDespesa as \"categoriaDespesa.nivelCategoriaDespesa\",   ");		
		sql.append(" funcionariocargo.codigo as \"funcionariocargo.codigo\",  ");
		sql.append(" departamento.codigo as \"departamento.codigo\", departamento.nome as \"departamento.nome\",  ");
		sql.append(" curso.codigo as \"curso.codigo\", curso.nome as \"curso.nome\",  ");
		sql.append(" turno.codigo as \"turno.codigo\", turno.nome as \"turno.nome\",  ");
		sql.append(" turma.codigo as \"turma.codigo\", turma.identificadorturma as \"turma.identificadorturma\",  ");
		sql.append(" centroresultadoadministrativo.codigo as \"centroresultadoadministrativo.codigo\", centroresultadoadministrativo.descricao as \"centroresultadoadministrativo.descricao\", centroresultadoadministrativo.identificadorCentroResultado as \"centroresultadoadministrativo.identificadorCentroResultado\", ");
		sql.append(" requisicao.motivosituacaoautorizacao as \"requisicao.motivosituacaoautorizacao\" ");
		
		sql.append(" FROM requisicao ");
		sql.append(" INNER JOIN requisicaoitem ON requisicaoitem.requisicao = requisicao.codigo ");
		sql.append(" INNER JOIN usuario AS responsavel ON requisicao.responsavelrequisicao = responsavel.codigo ");
		sql.append(" INNER JOIN usuario AS solicitante ON requisicao.solicitanterequisicao = solicitante.codigo ");
		sql.append(" INNER JOIN categoriaproduto ON requisicao.categoriaproduto = categoriaproduto.codigo ");
		sql.append(" INNER JOIN unidadeensino ON requisicao.unidadeensino = unidadeensino.codigo ");
		sql.append(" INNER JOIN categoriaDespesa ON requisicao.categoriaDespesa = categoriaDespesa.codigo ");
		sql.append(" LEFT JOIN departamento on departamento.codigo = requisicao.departamento");
		sql.append(" LEFT JOIN funcionariocargo on funcionariocargo.codigo = requisicao.funcionariocargo");
		sql.append(" LEFT JOIN curso on curso.codigo = requisicao.curso");
		sql.append(" LEFT JOIN turno on turno.codigo = requisicao.turno");
		sql.append(" LEFT JOIN turma on turma.codigo = requisicao.turma");
		sql.append(" LEFT JOIN centroresultado AS centroresultadoadministrativo ON requisicao.centroresultadoadministrativo = centroresultadoadministrativo.codigo ");
		return sql;
	}
	

	private RequisicaoVO montarDadosBasicaSemRequisicaoItem(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		RequisicaoVO obj = new RequisicaoVO();
		obj.setCodigo((dadosSQL.getInt("requisicao.codigo")));
		obj.setDataRequisicao(dadosSQL.getTimestamp("requisicao.dataRequisicao"));
		obj.setSituacaoEntrega(dadosSQL.getString("requisicao.situacaoEntrega"));
		obj.setSituacaoAutorizacao(dadosSQL.getString("requisicao.situacaoAutorizacao"));

		obj.getResponsavelRequisicao().setCodigo((dadosSQL.getInt("responsavel.codigo")));
		obj.getResponsavelRequisicao().setNome((dadosSQL.getString("responsavel.nome")));

		obj.getSolicitanteRequisicao().setCodigo((dadosSQL.getInt("responsavel.codigo")));
		obj.getSolicitanteRequisicao().setNome((dadosSQL.getString("responsavel.nome")));

		obj.getCategoriaProduto().setCodigo((dadosSQL.getInt("categoriaProduto.codigo")));
		obj.getCategoriaProduto().setNome((dadosSQL.getString("categoriaProduto.nome")));

		obj.getCategoriaDespesa().setCodigo((dadosSQL.getInt("categoriaDespesa.codigo")));
		obj.getCategoriaDespesa().setDescricao((dadosSQL.getString("categoriaDespesa.descricao")));
		obj.getCategoriaDespesa().setInformarTurma((dadosSQL.getString("categoriaDespesa.informarTurma")));
		obj.getCategoriaDespesa().setNivelCategoriaDespesa((dadosSQL.getString("categoriaDespesa.nivelCategoriaDespesa")));
		

		obj.getDepartamento().setCodigo((dadosSQL.getInt("departamento.codigo")));
		obj.getDepartamento().setNome((dadosSQL.getString("departamento.nome")));

		obj.getFuncionarioCargoVO().setCodigo((dadosSQL.getInt("funcionariocargo.codigo")));

		obj.getCurso().setCodigo((dadosSQL.getInt("curso.codigo")));
		obj.getCurso().setNome((dadosSQL.getString("curso.nome")));

		obj.getTurno().setCodigo((dadosSQL.getInt("turno.codigo")));
		obj.getTurno().setNome((dadosSQL.getString("turno.nome")));

		obj.getTurma().setCodigo((dadosSQL.getInt("turma.codigo")));
		obj.getTurma().setIdentificadorTurma((dadosSQL.getString("turma.identificadorturma")));

		obj.getUnidadeEnsino().setCodigo((dadosSQL.getInt("unidadeEnsino.codigo")));
		obj.getUnidadeEnsino().setNome((dadosSQL.getString("unidadeEnsino.nome")));

		obj.getCentroResultadoAdministrativo().setCodigo((dadosSQL.getInt("centroResultadoAdministrativo.codigo")));
		obj.getCentroResultadoAdministrativo().setDescricao((dadosSQL.getString("centroResultadoAdministrativo.descricao")));
		obj.getCentroResultadoAdministrativo().setIdentificadorCentroResultado((dadosSQL.getString("centroResultadoAdministrativo.identificadorCentroResultado")));		
		/*if (Uteis.isAtributoPreenchido(dadosSQL.getString("requisicao.tipoAutorizacaoRequisicao"))) {
			obj.setTipoAutorizacaoRequisicaoEnum(TipoAutorizacaoRequisicaoEnum.valueOf(dadosSQL.getString("requisicao.tipoAutorizacaoRequisicao")));
		}*/
		if (Uteis.isAtributoPreenchido(dadosSQL.getString("requisicao.tipoNivelCentroResultadoEnum"))) {
			obj.setTipoNivelCentroResultadoEnum(TipoNivelCentroResultadoEnum.valueOf(dadosSQL.getString("requisicao.tipoNivelCentroResultadoEnum")));
		}
		obj.setMotivoSituacaoAutorizacao(dadosSQL.getString("requisicao.motivosituacaoautorizacao"));
		return obj;
	}

	public List<RequisicaoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<RequisicaoVO> vetResultado = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	public static RequisicaoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		RequisicaoVO obj = new RequisicaoVO();
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo((dadosSQL.getInt("codigo")));
		obj.setDataRequisicao(dadosSQL.getTimestamp("dataRequisicao"));
		obj.getResponsavelRequisicao().setCodigo((dadosSQL.getInt("responsavelRequisicao")));
		obj.getSolicitanteRequisicao().setCodigo((dadosSQL.getInt("solicitanteRequisicao")));
		obj.getCategoriaProduto().setCodigo(dadosSQL.getInt("categoriaProduto"));
		obj.getCategoriaDespesa().setCodigo((dadosSQL.getInt("categoriaDespesa")));
		obj.setSituacaoEntrega(dadosSQL.getString("situacaoEntrega"));
		obj.getTurma().setCodigo((dadosSQL.getInt("turma")));
		obj.getCurso().setCodigo((dadosSQL.getInt("curso")));
		obj.getTurno().setCodigo((dadosSQL.getInt("turno")));
		obj.setDataNecessidadeRequisicao(dadosSQL.getDate("dataNecessidadeRequisicao"));
		obj.getFuncionarioCargoVO().setCodigo((dadosSQL.getInt("funcionarioCargo")));
		obj.getDepartamento().setCodigo((dadosSQL.getInt("departamento")));
		obj.getUnidadeEnsino().setCodigo((dadosSQL.getInt("unidadeEnsino")));
		obj.setSituacaoAutorizacao(dadosSQL.getString("situacaoAutorizacao"));
		obj.setDataAutorizacao(dadosSQL.getTimestamp("dataAutorizacao"));
		obj.getResponsavelAutorizacao().setCodigo((dadosSQL.getInt("responsavelAutorizacao")));
		obj.setMotivoSituacaoAutorizacao(dadosSQL.getString("motivoSituacaoAutorizacao"));
		obj.setTipoSacado(dadosSQL.getString("tipoSacado"));
		obj.getSacadoFornecedor().setCodigo((dadosSQL.getInt("sacadoFornecedor")));
		obj.getSacadoFuncionario().setCodigo((dadosSQL.getInt("sacadoFuncionario")));
		obj.getCentroResultadoAdministrativo().setCodigo((dadosSQL.getInt("centroResultadoAdministrativo")));
		/*if (Uteis.isAtributoPreenchido(dadosSQL.getString("tipoAutorizacaoRequisicao"))) {
			obj.setTipoAutorizacaoRequisicaoEnum(TipoAutorizacaoRequisicaoEnum.valueOf(dadosSQL.getString("tipoAutorizacaoRequisicao")));
		}*/
		if (Uteis.isAtributoPreenchido(dadosSQL.getString("tipoNivelCentroResultadoEnum"))) {
			obj.setTipoNivelCentroResultadoEnum(TipoNivelCentroResultadoEnum.valueOf(dadosSQL.getString("tipoNivelCentroResultadoEnum")));
		}

		if (Uteis.isAtributoPreenchido(dadosSQL.getString("questionariorespostaorigemabertura"))) {
			obj.getQuestionarioRespostaOrigemAberturaVO().setCodigo(dadosSQL.getInt("questionariorespostaorigemabertura"));
		}

		if (Uteis.isAtributoPreenchido(dadosSQL.getString("questionariorespostaorigemfechamento"))) {
			obj.getQuestionarioRespostaOrigemFechamentoVO().setCodigo(dadosSQL.getInt("questionariorespostaorigemfechamento"));
		}

		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			return obj;
		}
		obj.setResponsavelRequisicao(Uteis.montarDadosVO(dadosSQL.getInt("responsavelRequisicao"), UsuarioVO.class, p -> getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(p, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario)));
		obj.setSolicitanteRequisicao(Uteis.montarDadosVO(dadosSQL.getInt("solicitanteRequisicao"), UsuarioVO.class, p -> getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(p, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario)));
		obj.setUnidadeEnsino(Uteis.montarDadosVO(dadosSQL.getInt("unidadeEnsino"), UnidadeEnsinoVO.class, p -> getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(p, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario)));
		montarDadosDepartamento(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosArquivo(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSCONSULTA) {
			return obj;
		}

		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		montarDadosCategoriaProduto(obj, usuario);
		montarDadosCategoriaDespesa(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		obj.setCentroResultadoAdministrativo(Uteis.montarDadosVO(dadosSQL.getInt("centroResultadoAdministrativo"), CentroResultadoVO.class, p -> getFacadeFactory().getCentroResultadoFacade().consultarPorChavePrimaria(p, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario)));
		obj.setRequisicaoItemVOs(getFacadeFactory().getRequisicaoItemFacade().consultarRapidaRequisicaoItems(obj, null, nivelMontarDados, usuario));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
			return obj;
		}
		obj.setCurso(Uteis.montarDadosVO(dadosSQL.getInt("curso"), CursoVO.class, p -> getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(p, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, false, usuario)));
		obj.setTurno(Uteis.montarDadosVO(dadosSQL.getInt("turno"), TurnoVO.class, p -> getFacadeFactory().getTurnoFacade().consultarPorChavePrimaria(p, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario)));
		obj.setTurma(Uteis.montarDadosVO(dadosSQL.getInt("turma"), TurmaVO.class, p -> getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(p, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario)));
		obj.setFuncionarioCargoVO(Uteis.montarDadosVO(dadosSQL.getInt("funcionarioCargo"), FuncionarioCargoVO.class, p -> getFacadeFactory().getFuncionarioCargoFacade().consultarPorChavePrimaria(p, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario)));
		montarDadosSacadoFornecedor(obj, nivelMontarDados, usuario);
		montarDadosSacadoFuncionario(obj, nivelMontarDados, usuario);
		montarDadosResponsavelAutorizacao(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		obj.atualizarCentroCustoVO();
		return obj;
	}
	
	public static RequisicaoVO montarDados(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
		RequisicaoVO obj = new RequisicaoVO();
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo((dadosSQL.getInt("codigo")));
		obj.setDataRequisicao(dadosSQL.getTimestamp("dataRequisicao"));
		obj.getResponsavelRequisicao().setCodigo((dadosSQL.getInt("responsavelRequisicao")));
		obj.getSolicitanteRequisicao().setCodigo((dadosSQL.getInt("solicitanteRequisicao")));
		if (Uteis.isAtributoPreenchido(dadosSQL.getInt("categoriaProduto"))) {
			obj.setCategoriaProduto(getFacadeFactory().getCategoriaProdutoFacade().consultarPorCodigo(dadosSQL.getInt("categoriaProduto"),
					Uteis.NIVELMONTARDADOS_TODOS, usuario));
		}
		obj.getCategoriaDespesa().setCodigo((dadosSQL.getInt("categoriaDespesa")));
		obj.setSituacaoEntrega(dadosSQL.getString("situacaoEntrega"));
		obj.getTurma().setCodigo((dadosSQL.getInt("turma")));
		obj.getCurso().setCodigo((dadosSQL.getInt("curso")));
		obj.getTurno().setCodigo((dadosSQL.getInt("turno")));
		obj.setDataNecessidadeRequisicao(dadosSQL.getDate("dataNecessidadeRequisicao"));
		obj.getFuncionarioCargoVO().setCodigo((dadosSQL.getInt("funcionarioCargo")));
		obj.getDepartamento().setCodigo((dadosSQL.getInt("departamento")));
		obj.getUnidadeEnsino().setCodigo((dadosSQL.getInt("unidadeEnsino")));
		obj.setSituacaoAutorizacao(dadosSQL.getString("situacaoAutorizacao"));
		obj.setDataAutorizacao(dadosSQL.getTimestamp("dataAutorizacao"));
		obj.getResponsavelAutorizacao().setCodigo((dadosSQL.getInt("responsavelAutorizacao")));
		obj.setMotivoSituacaoAutorizacao(dadosSQL.getString("motivoSituacaoAutorizacao"));
		obj.setTipoSacado(dadosSQL.getString("tipoSacado"));
		obj.getSacadoFornecedor().setCodigo((dadosSQL.getInt("sacadoFornecedor")));
		obj.getSacadoFuncionario().setCodigo((dadosSQL.getInt("sacadoFuncionario")));
		obj.setResponsavelRequisicao(Uteis.montarDadosVO(dadosSQL.getInt("responsavelRequisicao"), UsuarioVO.class, p -> getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(p, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario)));
		obj.setSolicitanteRequisicao(Uteis.montarDadosVO(dadosSQL.getInt("solicitanteRequisicao"), UsuarioVO.class, p -> getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(p, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario)));
		
		obj.getCentroResultadoAdministrativo().setCodigo((dadosSQL.getInt("centroResultadoAdministrativo")));
		obj.setUnidadeEnsino(Uteis.montarDadosVO(dadosSQL.getInt("unidadeEnsino"), UnidadeEnsinoVO.class, p -> getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(p, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario)));
		montarDadosDepartamento(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		return obj;
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>UsuarioVO</code> relacionado ao objeto <code>RequisicaoVO</code>. Faz uso da chave primária da classe <code>UsuarioVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosResponsavelAutorizacao(RequisicaoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getResponsavelAutorizacao().getCodigo().intValue() == 0) {
			obj.setResponsavelAutorizacao(new UsuarioVO());
			return;
		}
		obj.setResponsavelAutorizacao(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavelAutorizacao().getCodigo(), nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>GrupoTrabalhoVO</code> relacionado ao objeto <code>RequisicaoVO</code>. Faz uso da chave primária da classe <code>GrupoTrabalhoVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosDepartamento(RequisicaoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getDepartamento().getCodigo().intValue() == 0) {
			obj.setDepartamento(new DepartamentoVO());
			return;
		}
		obj.setDepartamento(getFacadeFactory().getDepartamentoFacade().consultarPorChavePrimaria(obj.getDepartamento().getCodigo(), false, nivelMontarDados, usuario));
	}

	public static void montarDadosSacadoFornecedor(RequisicaoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getSacadoFornecedor().getCodigo().intValue() == 0) {
			obj.setSacadoFornecedor(new FornecedorVO());
			return;
		}
		obj.setSacadoFornecedor(getFacadeFactory().getFornecedorFacade().consultarPorChavePrimaria(obj.getSacadoFornecedor().getCodigo(), false, nivelMontarDados, usuario));
	}

	public static void montarDadosSacadoFuncionario(RequisicaoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getSacadoFuncionario().getCodigo().intValue() == 0) {
			obj.setSacadoFuncionario(new FuncionarioVO());
			return;
		}
		obj.setSacadoFuncionario(getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimaria(obj.getSacadoFuncionario().getCodigo(), 0, false, nivelMontarDados, usuario));
	}

	public static void montarDadosCategoriaProduto(RequisicaoVO obj, UsuarioVO usuario) throws Exception {
		if (obj.getCategoriaProduto().getCodigo().intValue() == 0) {
			obj.setCategoriaProduto(new CategoriaProdutoVO());
			return;
		}
		obj.setCategoriaProduto(getFacadeFactory().getCategoriaProdutoFacade().consultarPorChavePrimaria(obj.getCategoriaProduto().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuario));
	}

	public static void montarDadosCategoriaDespesa(RequisicaoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getCategoriaDespesa().getCodigo().intValue() == 0) {
			obj.setCategoriaDespesa(new CategoriaDespesaVO());
			return;
		}
		obj.setCategoriaDespesa(getFacadeFactory().getCategoriaDespesaFacade().consultarPorChavePrimaria(obj.getCategoriaDespesa().getCodigo(), false, nivelMontarDados, usuario));
	}

	public RequisicaoVO consultarPorChavePrimaria(Integer codigoPrm, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = new StringBuilder("SELECT * FROM Requisicao WHERE codigo = ").append(codigoPrm);
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sql.append(" and Requisicao.unidadeEnsino = ").append(unidadeEnsino.intValue());
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( Requisição ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	@Override
	public Boolean getPermiteAlterarCategoriaDespesa(UsuarioVO usuario) {
		try {
			return MapaCotacao.verificarPermissaoFuncionalidadeUsuario("PermiteAlterarCategoriaDespesa", usuario);
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public Boolean getPermiteAlterarRequisitante(UsuarioVO usuario) {
		try {
			return MapaCotacao.verificarPermissaoFuncionalidadeUsuario("PermiteAlterarRequisitante", usuario);
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public Boolean getPermiteCadastrarRequisicaoTodasUnidadesEnsino(UsuarioVO usuario) {
		try {
			return MapaCotacao.verificarPermissaoFuncionalidadeUsuario("PermiteCadastrarRequisicaoTodasUnidadesEnsino", usuario);
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public List<RequisicaoVO> consultarPorPlanoOrcamentario(PlanoOrcamentarioVO planoOrcamentarioVO, int nivelMontarDados,
			UsuarioVO usuarioLogado) throws Exception {
		String sqlStr = "SELECT * FROM Requisicao WHERE codigo = ? ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);

		List<RequisicaoVO> lista = new ArrayList<>();
		while (tabelaResultado.next()) {
			lista.add(montarDados(tabelaResultado, nivelMontarDados, usuarioLogado));
		}
		return lista;
	}

	@Override
	public void validarDadosBaixaPlanoOrcamentario(RequisicaoVO obj, UsuarioVO usuario, DepartamentoVO departamentoVO, boolean validarQuantidadeSolicitada) throws Exception {
		if (!Uteis.isAtributoPreenchido(obj.getDepartamento()) && Uteis.isAtributoPreenchido(obj.getFuncionarioCargoVO())) {
			obj.setFuncionarioCargoVO(getFacadeFactory().getFuncionarioCargoFacade().consultarPorChavePrimaria(obj.getFuncionarioCargoVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario));
			departamentoVO = obj.getFuncionarioCargoVO().getDepartamento();
		} else {
			departamentoVO = obj.getDepartamento();
		}
		Double valorTotal = 0.0;
		if ((Uteis.isAtributoPreenchido(obj.getSituacaoAutorizacao()) && obj.getSituacaoAutorizacao().equals("PE")) && !validarQuantidadeSolicitada) {
			if (validarQuantidadeSolicitada) {				
				valorTotal = Uteis.arrendondarForcando2CasasDecimais(obj.getRequisicaoItemVOs().stream().mapToDouble(rivo -> rivo.getQuantidadeSolicitada() * rivo.getValorUnitario()).reduce(Double::sum).orElse(0.0) * -1);
			} else {
				valorTotal = Uteis.arrendondarForcando2CasasDecimais(obj.getRequisicaoItemVOs().stream().mapToDouble(rivo -> rivo.getQuantidadeAutorizada() * rivo.getValorUnitario()).reduce(Double::sum).orElse(0.0) * -1);
			}
		} else {
			if (validarQuantidadeSolicitada) {	
				valorTotal = Uteis.arrendondarForcando2CasasDecimais(obj.getRequisicaoItemVOs().stream().mapToDouble(rivo -> rivo.getQuantidadeSolicitada() * rivo.getValorUnitario()).reduce(Double::sum).orElse(0.0));
			} else {				
				valorTotal = Uteis.arrendondarForcando2CasasDecimais(obj.getRequisicaoItemVOs().stream().mapToDouble(rivo -> rivo.getQuantidadeAutorizada() * rivo.getValorUnitario()).reduce(Double::sum).orElse(0.0));
			}
			getFacadeFactory().getPlanoOrcamentarioFacade().validarDadosSaldoPlanoOrcamentario(obj, valorTotal, departamentoVO, obj.getUnidadeEnsino(), usuario);
		}
		
	}

	@Override
	public Double validarDadosEstornoBaixaPlanoOrcamentario(RequisicaoVO obj, UsuarioVO usuario, DepartamentoVO departamentoVO, Double valorTotal) throws Exception {
		if (!Uteis.isAtributoPreenchido(obj.getDepartamento()) && Uteis.isAtributoPreenchido(obj.getFuncionarioCargoVO())) {
			obj.setFuncionarioCargoVO(getFacadeFactory().getFuncionarioCargoFacade().consultarPorChavePrimaria(obj.getFuncionarioCargoVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario));
			departamentoVO = obj.getFuncionarioCargoVO().getDepartamento();
		} else {
			departamentoVO = obj.getDepartamento();
		}
		
		return obj.getRequisicaoItemVOs().stream().mapToDouble(rivo -> rivo.getQuantidadeAutorizada() * rivo.getValorUnitario()).reduce(Double::sum).orElse(0.0);
	}

	public static String getIdEntidade() {
		return Requisicao.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		Requisicao.idEntidade = idEntidade;
	}
	
	public String consultarSituacaoEntregaPorCodigo(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT situacaoEntrega FROM Requisicao WHERE codigo = " + valorConsulta.intValue();
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (tabelaResultado.next()) {
			return tabelaResultado.getString("situacaoEntrega");
		}
		return "";
	}
	
	public void persistirArquivoRequisicao(RequisicaoVO requisicaoVO, UsuarioVO usuarioVO) throws Exception {
		if (!requisicaoVO.getArquivoVO().getNome().equals("") && requisicaoVO.getArquivoVO().getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.ARQUIVO_TMP)) {
			requisicaoVO.getArquivoVO().setCodOrigem(requisicaoVO.getCodigo());            			
			requisicaoVO.getArquivoVO().setOrigem(OrigemArquivo.REQUISICAO.getValor());

			getFacadeFactory().getArquivoFacade().incluir(requisicaoVO.getArquivoVO(), usuarioVO, getAplicacaoControle().getConfiguracaoGeralSistemaVO(0, usuarioVO));			   
		}    
	}
	
	public static void montarDadosArquivo(RequisicaoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {		
		obj.setArquivoVO(getFacadeFactory().getArquivoFacade().consultarPorCodOrigemRequisicao(obj.getCodigo() ,nivelMontarDados, usuario));				
	}
	
	@Override
	public StringBuilder getSqlBaseValorConsumidoPlanoOrcamentario(Integer planoOrcamentario,  Integer solicitacaoPlanoOrcamentario, Integer unidadeEnsino, Integer departamento, Integer categoriaDespesa, MesAnoEnum mesAno, String ano) {
		StringBuilder sql  =  new StringBuilder("");
		sql.append("  select sum(quantidadeautorizada * case when compraitem.codigo is not null then compraitem.precoUnitario else requisicaoitem.valorunitario end) as valorconsumido from requisicaoitem");
		sql.append("  left join compraitem on compraitem.codigo = requisicaoitem.compraitem ");
		if(Uteis.isAtributoPreenchido(solicitacaoPlanoOrcamentario) || Uteis.isAtributoPreenchido(planoOrcamentario)  || Uteis.isAtributoPreenchido(departamento)   || Uteis.isAtributoPreenchido(categoriaDespesa)) {
			sql.append("  inner join itemsolicitacaoorcamentoplanoorcamentario on itemsolicitacaoorcamentoplanoorcamentario.codigo = requisicaoitem.itemsolicitacaoorcamentoplanoorcamentario ");
		}
		if(Uteis.isAtributoPreenchido(solicitacaoPlanoOrcamentario) || Uteis.isAtributoPreenchido(planoOrcamentario)  || Uteis.isAtributoPreenchido(departamento)) {
			sql.append("  inner join solicitacaoorcamentoplanoorcamentario on itemsolicitacaoorcamentoplanoorcamentario.solicitacaoorcamentoplanoorcamentario = solicitacaoorcamentoplanoorcamentario.codigo ");
		}
		if(Uteis.isAtributoPreenchido(planoOrcamentario)) {
			sql.append("  inner join planoorcamentario on planoorcamentario.codigo = solicitacaoorcamentoplanoorcamentario.planoorcamentario ");
		}
		sql.append("  inner join requisicao on requisicao.codigo = requisicaoitem.requisicao ");
		sql.append("  where requisicao.situacaoautorizacao = 'AU' and requisicaoitem.quantidadeautorizada > 0  ");	
		if(Uteis.isAtributoPreenchido(planoOrcamentario)) {
			sql.append(" and   planoorcamentario.codigo  = ").append(planoOrcamentario).append("");
		}
		if(Uteis.isAtributoPreenchido(solicitacaoPlanoOrcamentario)) {
			sql.append(" and   solicitacaoorcamentoplanoorcamentario.codigo  = ").append(solicitacaoPlanoOrcamentario).append("");
		}
		if(Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sql.append(" and   solicitacaoorcamentoplanoorcamentario.unidadeEnsino  = ").append(unidadeEnsino).append("");
		}
		if(Uteis.isAtributoPreenchido(departamento)) {
			sql.append(" and   solicitacaoorcamentoplanoorcamentario.departamento  = ").append(departamento).append("");
		}
		if(Uteis.isAtributoPreenchido(categoriaDespesa)) {
			sql.append(" and   itemsolicitacaoorcamentoplanoorcamentario.categoriaDespesa  = ").append(categoriaDespesa).append("");
		}
		if(Uteis.isAtributoPreenchido(mesAno)) {
			sql.append(" and  extract(month from requisicao.datarequisicao) = '").append(mesAno.getKey()).append("'::int");
		}
		if(Uteis.isAtributoPreenchido(mesAno)) {
			sql.append(" and  extract(year from requisicao.datarequisicao) = ").append(ano).append("");
		}
		return sql;
	}
	
	@Override
	public Double consultarValorConsumidoPlanoOrcamentario(Integer planoOrcamentario,  Integer solicitacaoPlanoOrcamentario, Integer unidadeEnsino, Integer departamento, Integer categoriaDespesa, MesAnoEnum mesAno, String ano, UsuarioVO usuarioVO) throws Exception{
		StringBuilder sql  =  new StringBuilder(getSqlBaseValorConsumidoPlanoOrcamentario(planoOrcamentario, solicitacaoPlanoOrcamentario, unidadeEnsino, departamento, categoriaDespesa, mesAno, ano));		
		SqlRowSet rs  =  getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if(rs.next()) {
			return rs.getDouble("valorconsumido");
		}
		return 0.0;
	}

	@Override
	public QuestionarioRespostaOrigemVO realizarCriacaoQuestionarioRespostaOrigem(RequisicaoVO requisicaoVO, QuestionarioVO questionarioVO, UsuarioVO usuarioVO) throws Exception{
		QuestionarioRespostaOrigemVO questionarioRespostaOrigemVO = getFacadeFactory().getQuestionarioRespostaOrigemInterfaceFacade().preencherQuestionarioRespostaOrigem(questionarioVO, usuarioVO);
		questionarioRespostaOrigemVO.setEscopo(EscopoPerguntaEnum.REQUISICAO);
		questionarioRespostaOrigemVO.setSituacaoQuestionarioRespostaOrigemEnum(SituacaoQuestionarioRespostaOrigemEnum.DEFERIDO);
		questionarioRespostaOrigemVO.setRequisicaoVO(requisicaoVO);
		return questionarioRespostaOrigemVO;
	}

	@Override
	public List<RequisicaoVO> consultarRequisicaoRespostaQuestionarioFechamento(UsuarioVO usuarioVO, int nivelMontarDados) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" select requisicao.* from requisicao");
		sql.append(" inner join categoriaproduto on categoriaproduto.codigo = requisicao.categoriaproduto");
		sql.append(" where categoriaproduto.questionarioentregarequisicao is not null");
		sql.append(" and requisicao.situacaoAutorizacao = 'AU'");
		sql.append(" and requisicao.situacaoentrega = 'FI' ");
		sql.append(" and requisicao.questionariorespostaorigemfechamento is null and requisicao.solicitanterequisicao = ?");

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), usuarioVO.getCodigo());
		List<RequisicaoVO> lista = new ArrayList<>(0);

		while(rs.next()) {
			lista.add(montarDados(rs, usuarioVO));
		}
		return lista;
	}

	@Override
	public StringBuilder getSqlBaseValorConsumidoPlanoOrcamentario(Integer planoOrcamentario,  Integer solicitacaoPlanoOrcamentario, Integer unidadeEnsino, Integer departamento, Integer categoriaDespesa, MesAnoEnum mesAno, String ano, Integer itemsolicitacaoorcamentoplanoorcamentario) {
		StringBuilder sql  =  new StringBuilder("");
		sql.append("  select sum(quantidadeautorizada * case when compraitem.codigo is not null then compraitem.precoUnitario else requisicaoitem.valorunitario end) as valorconsumido ");
		sql.append(getSqlFromBasePlanoOrcamentario(planoOrcamentario, solicitacaoPlanoOrcamentario, unidadeEnsino, departamento, categoriaDespesa, mesAno, ano, itemsolicitacaoorcamentoplanoorcamentario));
		return sql;
	}

	@Override
	public Double consultarValorConsumidoPlanoOrcamentario(Integer planoOrcamentario,  Integer solicitacaoPlanoOrcamentario, Integer unidadeEnsino, Integer departamento, Integer categoriaDespesa, MesAnoEnum mesAno, String ano, UsuarioVO usuarioVO, Integer itemsolicitacaoorcamentoplanoorcamentario) throws Exception{
		StringBuilder sql  =  new StringBuilder(getSqlBaseValorConsumidoPlanoOrcamentario(planoOrcamentario, solicitacaoPlanoOrcamentario, unidadeEnsino, departamento, categoriaDespesa, mesAno, ano, itemsolicitacaoorcamentoplanoorcamentario));		
		SqlRowSet rs  =  getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if(rs.next()) {
			return rs.getDouble("valorconsumido");
		}
		return 0.0;
	}
	
	public StringBuilder getSqlFromBasePlanoOrcamentario(Integer planoOrcamentario,  Integer solicitacaoPlanoOrcamentario, Integer unidadeEnsino, Integer departamento, Integer categoriaDespesa, MesAnoEnum mesAno, String ano, Integer itemsolicitacaoorcamentoplanoorcamentario) {
		StringBuilder sql  =  new StringBuilder("");		
		sql.append("  from requisicaoitem");
		sql.append("  left join compraitem on compraitem.codigo = requisicaoitem.compraitem ");
		if(Uteis.isAtributoPreenchido(solicitacaoPlanoOrcamentario) || Uteis.isAtributoPreenchido(planoOrcamentario)  || Uteis.isAtributoPreenchido(departamento)   || Uteis.isAtributoPreenchido(categoriaDespesa) || Uteis.isAtributoPreenchido(itemsolicitacaoorcamentoplanoorcamentario)) {
			sql.append("  inner join itemsolicitacaoorcamentoplanoorcamentario on itemsolicitacaoorcamentoplanoorcamentario.codigo = requisicaoitem.itemsolicitacaoorcamentoplanoorcamentario ");
			sql.append("  inner join solicitacaoorcamentoplanoorcamentario on itemsolicitacaoorcamentoplanoorcamentario.solicitacaoorcamentoplanoorcamentario = solicitacaoorcamentoplanoorcamentario.codigo ");
			sql.append("  inner join planoorcamentario on planoorcamentario.codigo = solicitacaoorcamentoplanoorcamentario.planoorcamentario ");
			if( Uteis.isAtributoPreenchido(departamento)   || Uteis.isAtributoPreenchido(categoriaDespesa) || Uteis.isAtributoPreenchido(mesAno)) {				
				sql.append("  inner join detalhamentoperiodoorcamentario on itemsolicitacaoorcamentoplanoorcamentario.codigo = detalhamentoperiodoorcamentario.itemsolicitacaoorcamentoplanoorcamentario");
			}
		}

		sql.append("  inner join requisicao on requisicao.codigo = requisicaoitem.requisicao ");
		sql.append("  where requisicao.situacaoautorizacao = 'AU' and requisicaoitem.quantidadeautorizada > 0  ");
		if(Uteis.isAtributoPreenchido(planoOrcamentario)) {
			sql.append(" and   planoorcamentario.codigo  = ").append(planoOrcamentario).append("");
		}
		if(Uteis.isAtributoPreenchido(solicitacaoPlanoOrcamentario)) {
			sql.append(" and   solicitacaoorcamentoplanoorcamentario.codigo  = ").append(solicitacaoPlanoOrcamentario).append("");
		}
		if(Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sql.append(" and   solicitacaoorcamentoplanoorcamentario.unidadeEnsino  = ").append(unidadeEnsino).append("");
		}
		if(Uteis.isAtributoPreenchido(departamento)) {
			sql.append(" and   solicitacaoorcamentoplanoorcamentario.departamento  = ").append(departamento).append("");
		}
		if(Uteis.isAtributoPreenchido(categoriaDespesa)) {
			sql.append(" and   itemsolicitacaoorcamentoplanoorcamentario.categoriaDespesa  = ").append(categoriaDespesa).append("");
		}
		if(Uteis.isAtributoPreenchido(itemsolicitacaoorcamentoplanoorcamentario)) {
			sql.append(" and   itemsolicitacaoorcamentoplanoorcamentario.codigo  = ").append(itemsolicitacaoorcamentoplanoorcamentario).append("");
		}
		if(Uteis.isAtributoPreenchido(mesAno)) {
			sql.append(" and  extract(month from requisicao.datarequisicao) = '").append(mesAno.getKey()).append("'::int");
			sql.append(" and detalhamentoperiodoorcamentario.ano = '"+ano+"'");
		}
		if(Uteis.isAtributoPreenchido(mesAno)) {
			sql.append(" and  extract(year from requisicao.datarequisicao) = ").append(ano).append("");
			sql.append(" and detalhamentoperiodoorcamentario.mes = '"+MesAnoEnum.getEnum(mesAno.getKey())+"'");
		}
		
		return sql;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirQuestionarioRespostaAlterandoQuestionarioRespostaOrigemFechamentoRequisicao(final RequisicaoVO requisicaoVO, UsuarioVO usuarioVO) throws Exception {
		getFacadeFactory().getQuestionarioRespostaOrigemInterfaceFacade().incluir(requisicaoVO.getQuestionarioRespostaOrigemFechamentoVO(), usuarioVO);
		getFacadeFactory().getRequisicaoFacade().alterarQuestionarioFechamentoRequisicao(requisicaoVO,usuarioVO);
	}
}