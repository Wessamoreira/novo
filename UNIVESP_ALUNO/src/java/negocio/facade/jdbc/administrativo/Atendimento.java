package negocio.facade.jdbc.administrativo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.administrativo.AtendimentoInteracaoDepartamentoVO;
import negocio.comuns.administrativo.AtendimentoInteracaoSolicitanteVO;
import negocio.comuns.administrativo.AtendimentoVO;
import negocio.comuns.administrativo.ConfiguracaoAtendimentoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.TipagemOuvidoriaVO;
import negocio.comuns.administrativo.enumeradores.AvaliacaoAtendimentoEnum;
import negocio.comuns.administrativo.enumeradores.SituacaoAtendimentoEnum;
import negocio.comuns.administrativo.enumeradores.TipoAtendimentoEnum;
import negocio.comuns.administrativo.enumeradores.TipoOrigemOuvidoriaEnum;
import negocio.comuns.arquitetura.PerfilAcessoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.TipoVisaoEnum;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.OrigemArquivo;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.TipoUsuario;
import negocio.facade.jdbc.arquitetura.ControleAcesso;

/**
 * 
 * @author Pedro
 */

@Repository
@Scope("singleton")
@Lazy
public class Atendimento extends ControleAcesso  {

	protected static String idEntidade;

	public Atendimento() throws Exception {
		super();
//		setIdEntidade("");

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarAtualizacaoAtendimentoJaVisualizado(final AtendimentoVO obj, String idEntidade,
			UsuarioVO usuario) throws Exception {
		Atendimento.alterar(idEntidade);
		String sql = "UPDATE Atendimento set atendimentoJaVisualizado=? WHERE ((codigo = ?))";
		getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getAtendimentoJaVisualizado(), obj.getCodigo() });
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarPersistenciaAvaliacaoAtendimento(final AtendimentoVO obj, String idEntidade, UsuarioVO usuario)
			throws Exception {
		Atendimento.alterar(idEntidade);
		if ((obj.getIsAvaliacaoAtendimento_RUIM() || obj.getIsAvaliacaoAtendimento_REGULAR())
				&& (obj.getMotivoAvaliacaoRuim().isEmpty())) {
			throw new Exception(UteisJSF.internacionalizar("msg_Ouvidoria_motivoAvaliacaoRuim"));
		}
		realizarAvaliacaoOuvidoria(obj.getAvaliacaoAtendimentoEnum().name(), obj.getCodigo(),
				obj.getMotivoAvaliacaoRuim());
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarAvaliacaoOuvidoria(String avaliacao, Integer codAtendimento, String motivoAvaliacaoRuim)
			throws Exception {
		String sql = "SELECT dataAvaliacao from atendimento  where codigo = ? and avaliacaoAtendimentoEnum <> ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, codAtendimento,
				AvaliacaoAtendimentoEnum.NENHUM.name());
		if (tabelaResultado.next()) {
			throw new ConsistirException("Esse atendimento já foi avaliado no dia "
					+ Uteis.getDataComHora(tabelaResultado.getDate("dataAvaliacao")));
		}
		sql = "UPDATE Atendimento set avaliacaoAtendimentoEnum = ?, dataAvaliacao=?, motivoAvaliacaoRuim=?  WHERE ((codigo = ?))";
		getConexao().getJdbcTemplate().update(sql, new Object[] { avaliacao, Uteis.getDataJDBCTimestamp(new Date()),
				motivoAvaliacaoRuim, codAtendimento });
		if (avaliacao.equals(AvaliacaoAtendimentoEnum.RUIM.name())
				|| avaliacao.equals(AvaliacaoAtendimentoEnum.REGULAR.name())) {
//			getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade()
//					.executarEnvioMensagemOuvidoriaAtendimentoSituacaoAvaliada(avaliacao, codAtendimento,
//							motivoAvaliacaoRuim);
		}

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarFinalizacaoOuvidoria(final AtendimentoVO obj,
			AtendimentoInteracaoSolicitanteVO atendimentoSolicitante, String idEntidade, UsuarioVO usuario,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		Atendimento.incluir(idEntidade);
		String sql = "UPDATE Atendimento set situacaoAtendimentoEnum = ?, dataFechamento=?, responsavelFechamento=?, atendimentoJaVisualizado=? WHERE ((codigo = ?))";
		getConexao().getJdbcTemplate().update(sql,
				new Object[] { obj.getSituacaoAtendimentoEnum().name(),
						Uteis.getDataJDBCTimestamp(obj.getDataFechamento()), obj.getResponsavelFechamento().getCodigo(),
						obj.getAtendimentoJaVisualizado(), obj.getCodigo() });
//		getFacadeFactory().getAtendimentoInteracaoSolicitanteFacade().alterarAtendimentoInteracaoSolicitanteVO(
//				obj.getCodigo(), obj.getListaAtendimentoInteracaoSolicitanteVO(), usuario);
//		getFacadeFactory().getArquivoFacade().alterarArquivos(obj.getCodigo(), obj.getListaAnexos(),
//				OrigemArquivo.ATENDIMENTO, usuario, configuracaoGeralSistemaVO);
//		getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade()
//				.executarEnvioMensagemOuvidoriaInteracaoSolicitante(obj, atendimentoSolicitante, null, usuario);
//		getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemFinalizacaoOuvidoria(obj,
//				atendimentoSolicitante, usuario);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarRegistroInteracaoSolicitante(final AtendimentoVO obj,
			AtendimentoInteracaoSolicitanteVO atendimentoSolicitante, Boolean questionamentoOuvidor, String idEntidade,
			UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		Atendimento.incluir(idEntidade);
		String sql = "UPDATE Atendimento set situacaoAtendimentoEnum = ?, atendimentoJaVisualizado=? WHERE ((codigo = ?))";
		getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getSituacaoAtendimentoEnum().name(),
				obj.getAtendimentoJaVisualizado(), obj.getCodigo() });
//		getFacadeFactory().getAtendimentoInteracaoSolicitanteFacade().alterarAtendimentoInteracaoSolicitanteVO(
//				obj.getCodigo(), obj.getListaAtendimentoInteracaoSolicitanteVO(), usuario);
		getFacadeFactory().getArquivoFacade().alterarArquivos(obj.getCodigo(), obj.getListaAnexos(),
				OrigemArquivo.ATENDIMENTO, usuario, configuracaoGeralSistemaVO);
//		getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemOuvidoriaInteracaoSolicitante(
//				obj, atendimentoSolicitante, questionamentoOuvidor, usuario);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarRegistroInteracaoDepartamento(final AtendimentoVO obj,
			AtendimentoInteracaoDepartamentoVO atendimentoDepartamento, Boolean questionamentoOuvidor,
			String idEntidade, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO,
			Integer codigoRespostaAtendimentoInteracaoSolicitanteVO) throws Exception {
		Atendimento.incluir(idEntidade);
		String sql = "UPDATE Atendimento set situacaoAtendimentoEnum = ? WHERE ((codigo = ?))";
		getConexao().getJdbcTemplate().update(sql,
				new Object[] { obj.getSituacaoAtendimentoEnum().name(), obj.getCodigo() });
//		getFacadeFactory().getAtendimentoInteracaoDepartamentoFacade().alterarAtendimentoInteracaoDepartamentoVO(obj,
//				obj.getListaAtendimentoInteracaoDepartamentoVOs(), usuario,
//				codigoRespostaAtendimentoInteracaoSolicitanteVO);
		getFacadeFactory().getArquivoFacade().alterarArquivos(obj.getCodigo(), obj.getListaAnexos(),
				OrigemArquivo.ATENDIMENTO, usuario, configuracaoGeralSistemaVO);
//		getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemOuvidoriaInteracaoDepartamento(
//				obj, atendimentoDepartamento, questionamentoOuvidor, usuario);
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>OuvidoriaVO</code>. Primeiramente valida os dados (
	 * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados
	 * e a permissão do usuário para realizar esta operacão na entidade. Isto,
	 * através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj Objeto da classe <code>OuvidoriaVO</code> que será gravado no
	 *            banco de dados.
	 * @exception Exception Caso haja problemas de conexão, restrição de acesso ou
	 *                      validação de dados.
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final AtendimentoVO obj, String idEntidade, UsuarioVO usuario,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		try {
			if (usuario.getCodigo() != 0) {
				incluir(idEntidade, true, usuario);
			}
			validarDados(obj, usuario);
			validarDadosConfiguracaoAtendimento(obj, usuario);
			if (obj.getResponsavelAtendimento().getCodigo() == null
					|| obj.getResponsavelAtendimento().getCodigo() == 0) {
				realizarValidacoesParaBuscarResponsavelAtendimento(obj, obj.getUnidadeEnsino(), true,
						obj.getTipoAtendimentoEnum(), obj.getIdEntidade(), usuario);
			}
			realizarValidacaoParaAtualizacaoDadosPessoa(obj, usuario, configuracaoGeralSistemaVO);
			realizarUpperCaseDados(obj);
			final StringBuilder sql = new StringBuilder("INSERT INTO Atendimento (  ");
			sql.append(" nome, CPF, telefone, email, matriculaAluno, pessoa, tipagemOuvidoria, tipoAtendimentoEnum, ");
			sql.append(
					" tipoOrigemOuvidoriaEnum, situacaoAtendimentoEnum, assunto, descricao, dataRegistro, responsavelAtendimento, ");
			sql.append(
					" dataFechamento, responsavelFechamento, responsavelCadastro, unidadeEnsino, atendimentoJaVisualizado, ");
			sql.append(" avaliacaoAtendimentoEnum, motivoAvaliacaoRuim, dataAvaliacao, atendimentoAtrasado ");
			sql.append(
					" ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?  ) returning codigo");
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
					sqlInserir.setString(1, obj.getNome());
					sqlInserir.setString(2, obj.getCPF());
					sqlInserir.setString(3, obj.getTelefone());
					sqlInserir.setString(4, obj.getEmail());
					sqlInserir.setString(5, obj.getMatriculaAluno());
					if (obj.getPessoaVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(6, obj.getPessoaVO().getCodigo());
					} else {
						sqlInserir.setNull(6, 0);
					}
					if (obj.getTipagemOuvidoriaVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(7, obj.getTipagemOuvidoriaVO().getCodigo());
					} else {
						sqlInserir.setNull(7, 0);
					}
					sqlInserir.setString(8, obj.getTipoAtendimentoEnum().name());
					sqlInserir.setString(9, obj.getTipoOrigemOuvidoriaEnum().name());
					sqlInserir.setString(10, obj.getSituacaoAtendimentoEnum().name());
					sqlInserir.setString(11, obj.getAssunto());
					sqlInserir.setString(12, obj.getDescricao());
					sqlInserir.setTimestamp(13, Uteis.getDataJDBCTimestamp(obj.getDataRegistro()));
					if (obj.getResponsavelAtendimento().getCodigo().intValue() != 0) {
						sqlInserir.setInt(14, obj.getResponsavelAtendimento().getCodigo());
					} else {
						sqlInserir.setNull(14, 0);
					}
					if (obj.getDataFechamento() != null) {
						sqlInserir.setTimestamp(15, Uteis.getDataJDBCTimestamp(obj.getDataFechamento()));
					} else {
						sqlInserir.setNull(15, 0);
					}
					if (obj.getResponsavelFechamento().getCodigo().intValue() != 0) {
						sqlInserir.setInt(16, obj.getResponsavelFechamento().getCodigo());
					} else {
						sqlInserir.setNull(16, 0);
					}
					if (obj.getResponsavelCadastro().getCodigo().intValue() != 0) {
						sqlInserir.setInt(17, obj.getResponsavelCadastro().getCodigo());
					} else {
						sqlInserir.setNull(17, 0);
					}
					sqlInserir.setInt(18, obj.getUnidadeEnsino());
					sqlInserir.setBoolean(19, obj.getAtendimentoJaVisualizado());
					sqlInserir.setString(20, obj.getAvaliacaoAtendimentoEnum().name());
					sqlInserir.setString(21, obj.getMotivoAvaliacaoRuim());
					sqlInserir.setTimestamp(22, Uteis.getDataJDBCTimestamp(obj.getDataAvaliacao()));
					sqlInserir.setBoolean(23, obj.getAtendimentoAtrasado());
					return sqlInserir;

				}
			}, new ResultSetExtractor() {
				public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
			obj.setNovoObj(Boolean.FALSE);
//			getFacadeFactory().getAtendimentoInteracaoDepartamentoFacade().incluirAtendimentoInteracaoDepartamentoVO(
//					obj, obj.getListaAtendimentoInteracaoDepartamentoVOs(), usuario);
//			getFacadeFactory().getAtendimentoInteracaoSolicitanteFacade().incluirAtendimentoInteracaoSolicitanteVO(
//					obj.getCodigo(), obj.getListaAtendimentoInteracaoSolicitanteVO(), usuario);
			if (!obj.getListaAnexos().isEmpty()) {
				getFacadeFactory().getArquivoFacade().incluirArquivos(obj.getListaAnexos(), obj.getCodigo(), OrigemArquivo.ATENDIMENTO, usuario,
						configuracaoGeralSistemaVO);
			}
//			getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemOuvidoriaAbertura(obj,
//					usuario);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe
	 * <code>OuvidoriaVO</code>. Sempre utiliza a chave primária da classe como
	 * atributo para localização do registro a ser alterado. Primeiramente valida os
	 * dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco
	 * de dados e a permissão do usuário para realizar esta operacão na entidade.
	 * Isto, através da operação <code>alterar</code> da superclasse.
	 * 
	 * @param obj Objeto da classe <code>OuvidoriaVO</code> que será alterada no
	 *            banco de dados.
	 * @exception Execption Caso haja problemas de conexão, restrição de acesso ou
	 *                      validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final AtendimentoVO obj, String idEntidade, UsuarioVO usuario,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO,
			Integer codigoRespostaAtendimentoInteracaoSolicitanteVO) throws Exception {
		try {
			if (Uteis.isAtributoPreenchido(usuario)) {
				alterar(idEntidade, true, usuario);
			}
			validarDados(obj, usuario);
			realizarValidacaoParaAtualizacaoDadosPessoa(obj, usuario, configuracaoGeralSistemaVO);
			realizarUpperCaseDados(obj);
			final StringBuilder sql = new StringBuilder(" UPDATE Atendimento set  ");
			sql.append(
					" nome=?, CPF=?, telefone=?, email=?, matriculaAluno=?, pessoa=?, tipagemOuvidoria=?, tipoAtendimentoEnum=?, ");
			sql.append(
					" tipoOrigemOuvidoriaEnum=?, situacaoAtendimentoEnum=?, assunto=?, descricao=?, dataRegistro=?, responsavelAtendimento=?, ");
			sql.append(
					" dataFechamento=?, responsavelFechamento=?, responsavelCadastro=?, unidadeEnsino=?, atendimentoJaVisualizado=?, ");
			sql.append(" avaliacaoAtendimentoEnum=?, motivoAvaliacaoRuim=?, dataAvaliacao=?, atendimentoAtrasado=?  ");
			sql.append(" WHERE ((codigo = ?)) ");
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
					sqlAlterar.setString(1, obj.getNome());
					sqlAlterar.setString(2, obj.getCPF());
					sqlAlterar.setString(3, obj.getTelefone());
					sqlAlterar.setString(4, obj.getEmail());
					sqlAlterar.setString(5, obj.getMatriculaAluno());
					if (obj.getPessoaVO().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(6, obj.getPessoaVO().getCodigo());
					} else {
						sqlAlterar.setNull(6, 0);
					}
					if (obj.getTipagemOuvidoriaVO().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(7, obj.getTipagemOuvidoriaVO().getCodigo());
					} else {
						sqlAlterar.setNull(7, 0);
					}
					sqlAlterar.setString(8, obj.getTipoAtendimentoEnum().name());
					sqlAlterar.setString(9, obj.getTipoOrigemOuvidoriaEnum().name());
					sqlAlterar.setString(10, obj.getSituacaoAtendimentoEnum().name());
					sqlAlterar.setString(11, obj.getAssunto());
					sqlAlterar.setString(12, obj.getDescricao());
					sqlAlterar.setTimestamp(13, Uteis.getDataJDBCTimestamp(obj.getDataRegistro()));
					if (obj.getResponsavelAtendimento().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(14, obj.getResponsavelAtendimento().getCodigo());
					} else {
						sqlAlterar.setNull(14, 0);
					}
					if (obj.getDataFechamento() != null) {
						sqlAlterar.setTimestamp(15, Uteis.getDataJDBCTimestamp(obj.getDataFechamento()));
					} else {
						sqlAlterar.setNull(15, 0);
					}
					if (obj.getResponsavelFechamento().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(16, obj.getResponsavelFechamento().getCodigo());
					} else {
						sqlAlterar.setNull(16, 0);
					}
					if (obj.getResponsavelCadastro().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(17, obj.getResponsavelCadastro().getCodigo());
					} else {
						sqlAlterar.setNull(17, 0);
					}
					sqlAlterar.setInt(18, obj.getUnidadeEnsino());
					sqlAlterar.setBoolean(19, obj.getAtendimentoJaVisualizado());
					sqlAlterar.setString(20, obj.getAvaliacaoAtendimentoEnum().name());
					sqlAlterar.setString(21, obj.getMotivoAvaliacaoRuim());
					sqlAlterar.setTimestamp(22, Uteis.getDataJDBCTimestamp(obj.getDataAvaliacao()));
					sqlAlterar.setBoolean(23, obj.getAtendimentoAtrasado());
					sqlAlterar.setInt(24, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
//			getFacadeFactory().getAtendimentoInteracaoDepartamentoFacade().alterarAtendimentoInteracaoDepartamentoVO(
//					obj, obj.getListaAtendimentoInteracaoDepartamentoVOs(), usuario,
//					codigoRespostaAtendimentoInteracaoSolicitanteVO);
//			getFacadeFactory().getAtendimentoInteracaoSolicitanteFacade().alterarAtendimentoInteracaoSolicitanteVO(
//					obj.getCodigo(), obj.getListaAtendimentoInteracaoSolicitanteVO(), usuario);

			if (!obj.getListaAnexos().isEmpty()) {
				getFacadeFactory().getArquivoFacade().alterarArquivos(obj.getCodigo(), obj.getListaAnexos(),
						OrigemArquivo.ATENDIMENTO, usuario, configuracaoGeralSistemaVO);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.administrativo.OuvidoriaInterfaceFacade#excluir(negocio.
	 * comuns.administrativo.OuvidoriaVO, negocio.comuns.arquitetura.UsuarioVO)
	 */
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(AtendimentoVO obj, String idEntidade, UsuarioVO usuario) throws Exception {
		try {
			excluir(idEntidade, true, usuario);
			String sql = "DELETE FROM Atendimento WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.administrativo.OuvidoriaInterfaceFacade#persistir(negocio
	 * .comuns.administrativo.OuvidoriaVO, negocio.comuns.arquitetura.UsuarioVO)
	 */
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(AtendimentoVO obj, String idEntidade, UsuarioVO usuario,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO,
			Integer codigoRespostaAtendimentoInteracaoSolicitanteVO) throws Exception {
		if (obj.isNovoObj().booleanValue()) {
			incluir(obj, idEntidade, usuario, configuracaoGeralSistemaVO);
		} else {
			alterar(obj, idEntidade, usuario, configuracaoGeralSistemaVO,
					codigoRespostaAtendimentoInteracaoSolicitanteVO);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.administrativo.OuvidoriaInterfaceFacade#validarDados(
	 * negocio.comuns.administrativo.OuvidoriaVO)
	 */
	
	public void validarDados(AtendimentoVO obj, UsuarioVO usuarioVO) throws Exception {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		if (usuarioVO.getIsApresentarVisaoAdministrativa() || usuarioVO.getIsApresentarVisaoProfessor()) {
			if (obj.getCPF().isEmpty() || obj.getPessoaVO().getCPF() == null) {
				throw new Exception(UteisJSF.internacionalizar("msg_Ouvidoria_cpf"));
			}
		}
		if (usuarioVO.getIsApresentarVisaoAdministrativa()) {
			if ((obj.getNome() == null) || (obj.getNome().isEmpty())) {
				throw new Exception(UteisJSF.internacionalizar("msg_Ouvidoria_nome"));
			}
		}
		if ((obj.getTelefone() == null) || (obj.getTelefone().isEmpty())) {
			throw new Exception(UteisJSF.internacionalizar("msg_Ouvidoria_telefone"));
		}
		if ((obj.getEmail() == null) || (obj.getEmail().isEmpty())) {
			throw new Exception(UteisJSF.internacionalizar("msg_Ouvidoria_email"));
		}
		if (usuarioVO.getIsApresentarVisaoAdministrativa()) {
			if ((!obj.getExistePessoa()) && (obj.getSenha() == null || obj.getSenha().isEmpty())) {
				throw new Exception(UteisJSF.internacionalizar("msg_Ouvidoria_senha"));
			}
		}
		if ((getFacadeFactory().getPessoaFacade().consultaSePessoaAluno(obj.getPessoaVO().getCodigo(), false, null))
				&& (obj.getMatriculaAluno() == null || obj.getMatriculaAluno().isEmpty())) {
			throw new Exception(UteisJSF.internacionalizar("msg_Ouvidoria_matriculaAluno"));
		}
		if ((obj.getTipagemOuvidoriaVO() == null) || (obj.getTipagemOuvidoriaVO().getCodigo().intValue() == 0)) {
			throw new Exception(UteisJSF.internacionalizar("msg_Ouvidoria_tipagem"));
		}
		if ((obj.getTipoOrigemOuvidoriaEnum() == null) || (obj.getTipoOrigemOuvidoriaEnum().name().isEmpty())) {
			throw new Exception(UteisJSF.internacionalizar("msg_Ouvidoria_origem"));
		}
		if ((obj.getAssunto() == null) || (obj.getAssunto().isEmpty())) {
			throw new Exception(UteisJSF.internacionalizar("msg_Ouvidoria_assunto"));
		}
		if ((obj.getDescricao() == null)
				|| (Uteis.retiraTags(obj.getDescricao()).replaceAll("Untitled document", "").trim().isEmpty())) {
			throw new Exception(UteisJSF.internacionalizar("msg_Ouvidoria_descricao"));
		}
		if (usuarioVO.getIsApresentarVisaoAdministrativa() || usuarioVO.getIsApresentarVisaoProfessor()) {
			if (obj.getCPF().length() < 14) {
				throw new Exception(UteisJSF.internacionalizar("msg_Ouvidoria_quantidadeCpf"));
			}
		}
		String telefoneSemMascara = Uteis.retirarMascaraTelefone(obj.getTelefone());
		if (telefoneSemMascara.isEmpty() || telefoneSemMascara.length() < 10) {
			throw new Exception(UteisJSF.internacionalizar("msg_Ouvidoria_quantidadeTelefone"));
		}
		if (!Uteis.getValidaEmail(obj.getEmail())) {
			throw new Exception(UteisJSF.internacionalizar("msg_Ouvidoria_emailInvalido"));
		}
		if (usuarioVO.getIsApresentarVisaoAdministrativa() || usuarioVO.getIsApresentarVisaoProfessor()) {
			if (!Uteis.validaCPF(Uteis.removeCaractersEspeciais(obj.getCPF()).replace(" ", ""))) {
				throw new Exception(UteisJSF.internacionalizar("msg_Ouvidoria_cpfInvalido"));
			}
		}
	}

	public void validarCpf(AtendimentoVO obj) throws Exception {
		if (obj.getCPF() == null || obj.getCPF().length() < 14) {
			throw new Exception(UteisJSF.internacionalizar("msg_Ouvidoria_quantidadeCpf"));
		}
		if (!Uteis.validaCPF(Uteis.removeCaractersEspeciais(obj.getCPF()).replace(" ", ""))) {
			throw new Exception(UteisJSF.internacionalizar("msg_Ouvidoria_cpfInvalido"));
		}
	}

	public void validarDadosConfiguracaoAtendimento(AtendimentoVO obj, UsuarioVO usuarioLogado) throws Exception {
//		ConfiguracaoAtendimentoVO configAtendimento = getFacadeFactory().getConfiguracaoAtendimentoFacade()
//				.consultarPorCodigoUnidadeEnsino(obj.getUnidadeEnsino(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
//						usuarioLogado);
//		if (configAtendimento != null
//				&& configAtendimento.getNumeroOuvidoriaQuePodemSerAbertasAoMesmoTempoPorPessoa().intValue() != 0) {
//			Integer qtd = consultarQuantidadeAtendimentoPorCPFPessoaPorTipoAtendimentoPorSituacao(obj.getCPF(),
//					obj.getTipoAtendimentoEnum(), SituacaoAtendimentoEnum.FINALIZADA, false);
//			if (qtd >= configAtendimento.getNumeroOuvidoriaQuePodemSerAbertasAoMesmoTempoPorPessoa()) {
//				throw new Exception(
//						UteisJSF.internacionalizar("msg_Ouvidoria_numeroAtendimentoMaiorQueNaConfiguracao"));
//			}
//		}
	}

	/**
	 * Operação responsável por validar a unicidade dos dados de um objeto da classe
	 * <code>OuvidoriaVO</code>.
	 */
	public void validarUnicidade(List<AtendimentoVO> lista, AtendimentoVO obj) throws ConsistirException {
		for (AtendimentoVO repetido : lista) {
		}
	}

	/**
	 * Operação reponsável por realizar o UpperCase dos atributos do tipo String.
	 */
	public void realizarUpperCaseDados(AtendimentoVO obj) {
		if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
			return;
		}
		obj.setNome(obj.getNome().toUpperCase());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.administrativo.OuvidoriaInterfaceFacade#consultar(java.
	 * lang.String, java.lang.String, boolean, int,
	 * negocio.comuns.arquitetura.UsuarioVO)
	 */
	public List<AtendimentoVO> consultar(String valorConsulta, String campoConsulta,
			TipoAtendimentoEnum tipagemAtedimento, Integer limite, Integer pagina, boolean controlarAcesso,
			int nivelMontarDados, String idEntidade, UsuarioVO usuario) throws Exception {
		if (campoConsulta.equals("CODIGO")) {
			if (valorConsulta.trim().equals("")) {
				throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsultaCodigo"));
			}
			if (valorConsulta.equals("")) {
				valorConsulta = "0";
			}
			int valorInt = Integer.parseInt(valorConsulta);
			return consultarPorCodigo(valorInt, tipagemAtedimento, limite, pagina, controlarAcesso, nivelMontarDados,
					idEntidade, usuario);
		}
		if (campoConsulta.equals("NOME")) {
			if (valorConsulta.trim().length() < qtdCaracteresValidacao) {
				throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsulta"));
			}
			return consultarPorNome(valorConsulta, tipagemAtedimento, limite, pagina, controlarAcesso, nivelMontarDados,
					idEntidade, usuario);
		}
		if (campoConsulta.equals("CPF")) {
			if (valorConsulta.trim().length() < qtdCaracteresValidacao) {
				throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsulta"));
			}
			return consultarPorCPF(valorConsulta, tipagemAtedimento, limite, pagina, controlarAcesso, nivelMontarDados,
					idEntidade, usuario);
		}
		if (campoConsulta.equals("TIPAGEM")) {
			if (valorConsulta.trim().length() < qtdCaracteresValidacao) {
				throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsulta"));
			}
			return consultarPorTipagem(valorConsulta, tipagemAtedimento, limite, pagina, controlarAcesso,
					nivelMontarDados, idEntidade, usuario);
		}
		if (campoConsulta.equals("SITUACAO")) {
			if (valorConsulta.trim().length() < qtdCaracteresValidacao) {
				throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsulta"));
			}
			return consultarPorsituacaoAtendimentoEnum(valorConsulta, tipagemAtedimento, limite, pagina,
					controlarAcesso, nivelMontarDados, idEntidade, usuario);
		}
		return new ArrayList(0);
	}

	
	public Integer consultarTotalRegistro(String valorConsulta, String campoConsulta,
			TipoAtendimentoEnum tipagemAtedimento) throws Exception {
		if (campoConsulta.equals("CODIGO")) {
			if (valorConsulta.trim().equals("")) {
				throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsultaCodigo"));
			}
			if (valorConsulta.equals("")) {
				valorConsulta = "0";
			}
			int valorInt = Integer.parseInt(valorConsulta);
			return consultarTotalRegistroPorCodigo(valorInt, tipagemAtedimento);
		}
		if (campoConsulta.equals("NOME")) {
			if (valorConsulta.trim().length() < qtdCaracteresValidacao) {
				throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsulta"));
			}
			return consultarTotalRegistroPorNome(valorConsulta, tipagemAtedimento);
		}
		if (campoConsulta.equals("CPF")) {
			if (valorConsulta.trim().length() < qtdCaracteresValidacao) {
				throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsulta"));
			}
			return consultarTotalRegistroPorCPF(valorConsulta, tipagemAtedimento);
		}
		if (campoConsulta.equals("TIPAGEM")) {
			if (valorConsulta.trim().length() < qtdCaracteresValidacao) {
				throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsulta"));
			}
			return consultarTotalRegistroPorTipagem(valorConsulta, tipagemAtedimento);
		}
		if (campoConsulta.equals("SITUACAO")) {
			if (valorConsulta.trim().length() < qtdCaracteresValidacao) {
				throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsulta"));
			}
			return consultarTotalRegistroPorsituacaoAtendimentoEnum(valorConsulta, tipagemAtedimento);
		}
		return 0;
	}

	/**
	 * Responsável por realizar uma consulta de <code>TitulacaoCurso</code> através
	 * do valor do atributo <code>nome</code> da classe <code>Curso</code> Faz uso
	 * da operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe <code>TitulacaoCursoVO</code>
	 *         resultantes da consulta.
	 * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
	 */
	
	public List consultarPorCodigoPessoa(Integer valorConsulta, TipoAtendimentoEnum tipagemAtedimento,
			boolean controlarAcesso, int nivelMontarDados, String idEntidade, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(idEntidade, controlarAcesso, usuario);
		StringBuilder sqlSb = new StringBuilder();
		sqlSb.append("SELECT * FROM Atendimento ");
		sqlSb.append(" WHERE pessoa = ").append(valorConsulta).append("");
		sqlSb.append(" and tipoAtendimentoEnum=? ");
		sqlSb.append(" ORDER BY dataRegistro desc ");
		return montarDadosConsulta(
				getConexao().getJdbcTemplate().queryForRowSet(sqlSb.toString(), tipagemAtedimento.name()),
				nivelMontarDados, usuario);
	}

	/**
	 * Responsável por realizar uma consulta de <code>TitulacaoCurso</code> através
	 * do valor do atributo <code>nome</code> da classe <code>Curso</code> Faz uso
	 * da operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe <code>TitulacaoCursoVO</code>
	 *         resultantes da consulta.
	 * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorNome(String valorConsulta, TipoAtendimentoEnum tipagemAtedimento, Integer limite,
			Integer pagina, boolean controlarAcesso, int nivelMontarDados, String idEntidade, UsuarioVO usuario)
			throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(idEntidade, controlarAcesso, usuario);
		valorConsulta += "%";
		StringBuilder sqlSb = new StringBuilder();
		sqlSb.append("SELECT * FROM Atendimento ");
		sqlSb.append(" WHERE nome ilike'").append(valorConsulta).append("'");
		sqlSb.append(" and tipoAtendimentoEnum=? ");
		sqlSb.append(" ORDER BY nome");
		if (limite != null && limite > 0) {
			sqlSb.append(" limit ").append(limite).append(" offset ").append(pagina);
		}
		return montarDadosConsulta(
				getConexao().getJdbcTemplate().queryForRowSet(sqlSb.toString(), tipagemAtedimento.name()),
				nivelMontarDados, usuario);
	}

	public Integer consultarTotalRegistroPorNome(String valorConsulta, TipoAtendimentoEnum tipagemAtedimento)
			throws Exception {
		valorConsulta += "%";
		StringBuilder sqlSb = new StringBuilder();
		sqlSb.append("SELECT count(codigo) FROM Atendimento ");
		sqlSb.append(" WHERE nome ilike'").append(valorConsulta).append("'");
		sqlSb.append(" and tipoAtendimentoEnum=? ");
		sqlSb.append(" ");
		return getTotalRegistro(
				getConexao().getJdbcTemplate().queryForRowSet(sqlSb.toString(), tipagemAtedimento.name()));
	}

	/**
	 * Responsável por realizar uma consulta de <code>TitulacaoCurso</code> através
	 * do valor do atributo <code>nome</code> da classe <code>Curso</code> Faz uso
	 * da operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe <code>TitulacaoCursoVO</code>
	 *         resultantes da consulta.
	 * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCPF(String valorConsulta, TipoAtendimentoEnum tipagemAtedimento, Integer limite,
			Integer pagina, boolean controlarAcesso, int nivelMontarDados, String idEntidade, UsuarioVO usuario)
			throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(idEntidade, controlarAcesso, usuario);
		valorConsulta += "%";
		StringBuilder sqlSb = new StringBuilder();
		sqlSb.append("SELECT * FROM Atendimento ");
		sqlSb.append(" WHERE CPF ilike'").append(valorConsulta).append("'");
		sqlSb.append(" and tipoAtendimentoEnum=? ");
		sqlSb.append(" ORDER BY CPF");
		if (limite != null && limite > 0) {
			sqlSb.append(" limit ").append(limite).append(" offset ").append(pagina);
		}
		return montarDadosConsulta(
				getConexao().getJdbcTemplate().queryForRowSet(sqlSb.toString(), tipagemAtedimento.name()),
				nivelMontarDados, usuario);
	}

	public Integer consultarTotalRegistroPorCPF(String valorConsulta, TipoAtendimentoEnum tipagemAtedimento)
			throws Exception {
		valorConsulta += "%";
		StringBuilder sqlSb = new StringBuilder();
		sqlSb.append("SELECT count(Atendimento.codigo) FROM Atendimento ");
		sqlSb.append(" WHERE CPF ilike'").append(valorConsulta).append("'");
		sqlSb.append(" and tipoAtendimentoEnum=? ");
		sqlSb.append(" ");
		return getTotalRegistro(
				getConexao().getJdbcTemplate().queryForRowSet(sqlSb.toString(), tipagemAtedimento.name()));
	}

	/**
	 * Responsável por realizar uma consulta de <code>TitulacaoCurso</code> através
	 * do valor do atributo <code>nome</code> da classe <code>Curso</code> Faz uso
	 * da operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe <code>TitulacaoCursoVO</code>
	 *         resultantes da consulta.
	 * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorTipagem(String valorConsulta, TipoAtendimentoEnum tipagemAtedimento, Integer limite,
			Integer pagina, boolean controlarAcesso, int nivelMontarDados, String idEntidade, UsuarioVO usuario)
			throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(idEntidade, controlarAcesso, usuario);
		valorConsulta += "%";
		StringBuilder sqlSb = new StringBuilder();
		sqlSb.append("SELECT * FROM Atendimento ");
		sqlSb.append("inner join tipagemouvidoria on tipagemouvidoria.codigo = atendimento.tipagemouvidoria ");
		sqlSb.append(" WHERE tipagemouvidoria.descricao ilike'").append(valorConsulta).append("'");
		sqlSb.append(" and tipoAtendimentoEnum=? ");
		sqlSb.append(" ORDER BY atedimento.nome ");
		if (limite != null && limite > 0) {
			sqlSb.append(" limit ").append(limite).append(" offset ").append(pagina);
		}
		return montarDadosConsulta(
				getConexao().getJdbcTemplate().queryForRowSet(sqlSb.toString(), tipagemAtedimento.name()),
				nivelMontarDados, usuario);
	}

	public Integer consultarTotalRegistroPorTipagem(String valorConsulta, TipoAtendimentoEnum tipagemAtedimento)
			throws Exception {
		valorConsulta += "%";
		StringBuilder sqlSb = new StringBuilder();
		sqlSb.append("SELECT count(Atendimento.codigo) FROM Atendimento ");
		sqlSb.append("inner join tipagemouvidoria on tipagemouvidoria.codigo = atendimento.tipagemouvidoria ");
		sqlSb.append(" WHERE tipagemouvidoria.descricao ilike'").append(valorConsulta).append("'");
		sqlSb.append(" and tipoAtendimentoEnum=? ");
		sqlSb.append("  ");
		return getTotalRegistro(
				getConexao().getJdbcTemplate().queryForRowSet(sqlSb.toString(), tipagemAtedimento.name()));
	}

	/**
	 * Responsável por realizar uma consulta de <code>TitulacaoCurso</code> através
	 * do valor do atributo <code>nome</code> da classe <code>Curso</code> Faz uso
	 * da operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe <code>TitulacaoCursoVO</code>
	 *         resultantes da consulta.
	 * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorsituacaoAtendimentoEnum(String valorConsulta, TipoAtendimentoEnum tipagemAtedimento,
			Integer limite, Integer pagina, boolean controlarAcesso, int nivelMontarDados, String idEntidade,
			UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(idEntidade, controlarAcesso, usuario);
		valorConsulta += "%";
		StringBuilder sqlSb = new StringBuilder();
		sqlSb.append("SELECT * FROM Atendimento ");
		sqlSb.append(" WHERE situacaoAtendimentoEnum ilike'").append(valorConsulta).append("'");
		sqlSb.append(" and tipoAtendimentoEnum=? ");
		sqlSb.append(" ORDER BY nome ");
		if (limite != null && limite > 0) {
			sqlSb.append(" limit ").append(limite).append(" offset ").append(pagina);
		}
		return montarDadosConsulta(
				getConexao().getJdbcTemplate().queryForRowSet(sqlSb.toString(), tipagemAtedimento.name()),
				nivelMontarDados, usuario);
	}

	public Integer consultarTotalRegistroPorsituacaoAtendimentoEnum(String valorConsulta,
			TipoAtendimentoEnum tipagemAtedimento) throws Exception {
		valorConsulta += "%";
		StringBuilder sqlSb = new StringBuilder();
		sqlSb.append("SELECT count(atendimento.codigo) FROM Atendimento ");
		sqlSb.append(" WHERE situacaoAtendimentoEnum ilike'").append(valorConsulta).append("'");
		sqlSb.append(" and tipoAtendimentoEnum=? ");
		return getTotalRegistro(
				getConexao().getJdbcTemplate().queryForRowSet(sqlSb.toString(), tipagemAtedimento.name()));
	}

	/**
	 * Responsável por realizar uma consulta de <code>TitulacaoCurso</code> através
	 * do valor do atributo <code>Integer codigo</code>. Retorna os objetos com
	 * valores iguais ou superiores ao parâmetro fornecido. Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
	 * resultante.
	 * 
	 * @param controlarAcesso Indica se a aplicação deverá verificar se o usuário
	 *                        possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>TitulacaoCursoVO</code>
	 *         resultantes da consulta.
	 * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigo(Integer valorConsulta, TipoAtendimentoEnum tipagemAtedimento, Integer limite,
			Integer pagina, boolean controlarAcesso, int nivelMontarDados, String idEntidade, UsuarioVO usuario)
			throws Exception {

		getFacadeFactory().getControleAcessoFacade().consultar(idEntidade, controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Atendimento WHERE codigo >= ? and tipoAtendimentoEnum=? ORDER BY codigo";
		if (limite != null && limite > 0) {
			sqlStr += " limit " + limite + " offset " + pagina;
		}
		return (montarDadosConsulta(
				getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta, tipagemAtedimento.name()),
				nivelMontarDados, usuario));
	}

	public Integer consultarTotalRegistroPorCodigo(Integer valorConsulta, TipoAtendimentoEnum tipagemAtedimento)
			throws Exception {
		String sqlStr = "SELECT count(codigo) FROM Atendimento WHERE codigo >= ? and tipoAtendimentoEnum=? ";
		return getTotalRegistro(getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta,
				tipagemAtedimento.name()));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma
	 * consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe <code>TitulacaoCursoVO</code>
	 *         resultantes da consulta.
	 */
	public  List<AtendimentoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados,
			UsuarioVO usuario) throws Exception {
		List<AtendimentoVO> vetResultado = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de dados
	 * (<code>ResultSet</code>) em um objeto da classe
	 * <code>TitulacaoCursoVO</code>.
	 * 
	 * @return O objeto da classe <code>TitulacaoCursoVO</code> com os dados
	 *         devidamente montados.
	 */
	public  AtendimentoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario)
			throws Exception {
		AtendimentoVO obj = new AtendimentoVO();
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSCONSULTA) {
			obj.setCodigo(dadosSQL.getInt("codigo"));
			obj.setNome(dadosSQL.getString("nome"));
			obj.setCPF(dadosSQL.getString("cpf"));
			obj.getTipagemOuvidoriaVO().setCodigo(dadosSQL.getInt("tipagemOuvidoria"));
			obj.setDataRegistro(dadosSQL.getTimestamp("dataRegistro"));
			obj.setAssunto(dadosSQL.getString("assunto"));
			obj.setAtendimentoJaVisualizado(dadosSQL.getBoolean("atendimentoJaVisualizado"));
			obj.setAtendimentoAtrasado(dadosSQL.getBoolean("atendimentoAtrasado"));
			obj.setSituacaoAtendimentoEnum(
					SituacaoAtendimentoEnum.valueOf(dadosSQL.getString("situacaoAtendimentoEnum")));
//			obj.setSituacaoAtendimentoEnum(SituacaoAtendimentoEnum.getEnumPorValor(Integer.parseInt(dadosSQL.getString("situacaoAtendimentoEnum"))));			
			montarDadosTipagemOuvidoria(obj, nivelMontarDados, usuario);
			return obj;
		}

		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setNome(dadosSQL.getString("nome"));
		obj.setAssunto(dadosSQL.getString("assunto"));
		obj.setAtendimentoAtrasado(dadosSQL.getBoolean("atendimentoAtrasado"));
		obj.setMotivoAvaliacaoRuim(dadosSQL.getString("motivoAvaliacaoRuim"));
		obj.setCPF(dadosSQL.getString("cpf"));
		obj.setDataFechamento(dadosSQL.getTimestamp("dataFechamento"));
		obj.setDataRegistro(dadosSQL.getTimestamp("dataRegistro"));
		obj.setDataAvaliacao(dadosSQL.getTimestamp("dataAvaliacao"));
		obj.setDescricao(dadosSQL.getString("descricao"));
		obj.setEmail(dadosSQL.getString("email"));
		obj.getResponsavelAtendimento().setCodigo(dadosSQL.getInt("responsavelAtendimento"));
		obj.setMatriculaAluno(dadosSQL.getString("matriculaAluno"));
		obj.getPessoaVO().setCodigo(dadosSQL.getInt("pessoa"));
		obj.getResponsavelFechamento().setCodigo(dadosSQL.getInt("responsavelFechamento"));
		obj.getResponsavelCadastro().setCodigo(dadosSQL.getInt("responsavelCadastro"));
		obj.setAvaliacaoAtendimentoEnum(
				AvaliacaoAtendimentoEnum.valueOf(dadosSQL.getString("avaliacaoAtendimentoEnum")));
		obj.setSituacaoAtendimentoEnum(SituacaoAtendimentoEnum.valueOf(dadosSQL.getString("situacaoAtendimentoEnum")));
		obj.setTelefone(dadosSQL.getString("telefone"));
		obj.getTipagemOuvidoriaVO().setCodigo(dadosSQL.getInt("tipagemOuvidoria"));
		obj.setTipoAtendimentoEnum(TipoAtendimentoEnum.valueOf(dadosSQL.getString("tipoAtendimentoEnum")));
		obj.setTipoOrigemOuvidoriaEnum(TipoOrigemOuvidoriaEnum.valueOf(dadosSQL.getString("tipoOrigemOuvidoriaEnum")));
		obj.setUnidadeEnsino(dadosSQL.getInt("unidadeEnsino"));
		obj.setAtendimentoJaVisualizado(dadosSQL.getBoolean("atendimentoJaVisualizado"));
		obj.setNovoObj(false);

		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_PROCESSAMENTO) {
//			obj.setListaAtendimentoInteracaoSolicitanteVO(getFacadeFactory().getAtendimentoInteracaoSolicitanteFacade()
//					.consultarPorCodigoOuvidoria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
			obj.setListaAnexos(getFacadeFactory().getArquivoFacade().consultarPorCodOrigemTipoOrigem(obj.getCodigo(),
					OrigemArquivo.ATENDIMENTO.getValor(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
			montarDadosListaAnexo(obj);
			montarDadosTipagemOuvidoria(obj, nivelMontarDados, usuario);
			montarDadosPessoaVO(obj, Uteis.NIVELMONTARDADOS_DADOSLOGIN, usuario);
			montarDadosOuvidor(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			montarDadosResponsavelFechamento(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			montarDadosResponsavelCadastro(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			return obj;
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {

			return obj;
		}
//		obj.setListaAtendimentoInteracaoSolicitanteVO(getFacadeFactory().getAtendimentoInteracaoSolicitanteFacade()
//				.consultarPorCodigoOuvidoria(obj.getCodigo(), false, nivelMontarDados, usuario));
//		obj.setListaAtendimentoInteracaoDepartamentoVOs(getFacadeFactory().getAtendimentoInteracaoDepartamentoFacade()
//				.consultarPorCodigoOuvidoria(obj.getCodigo(), false, nivelMontarDados, usuario));
		obj.setListaAnexos(getFacadeFactory().getArquivoFacade().consultarPorCodOrigemTipoOrigem(obj.getCodigo(),
				OrigemArquivo.ATENDIMENTO.getValor(), nivelMontarDados, usuario));
		montarDadosListaAnexo(obj);
		montarDadosTipagemOuvidoria(obj, nivelMontarDados, usuario);
		montarDadosPessoaVO(obj, Uteis.NIVELMONTARDADOS_DADOSLOGIN, usuario);
		montarDadosOuvidor(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosResponsavelFechamento(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosResponsavelCadastro(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		return obj;
	}

	public  void montarDadosPessoaVO(AtendimentoVO obj, int nivelMontarDados, UsuarioVO usuario)
			throws Exception {
		if (obj.getPessoaVO().getCodigo().intValue() == 0) {
			obj.setPessoaVO(new PessoaVO());
			return;
		}
		obj.setPessoaVO(getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(obj.getPessoaVO().getCodigo(),
				false, nivelMontarDados, usuario));
	}

	public  void montarDadosResponsavelFechamento(AtendimentoVO obj, int nivelMontarDados, UsuarioVO usuario)
			throws Exception {
		if (obj.getResponsavelFechamento().getCodigo().intValue() == 0) {
			obj.setResponsavelFechamento(new UsuarioVO());
			return;
		}
		obj.setResponsavelFechamento(getFacadeFactory().getUsuarioFacade()
				.consultarPorChavePrimaria(obj.getResponsavelFechamento().getCodigo(), nivelMontarDados, usuario));
	}

	public  void montarDadosOuvidor(AtendimentoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getResponsavelAtendimento().getCodigo().intValue() == 0) {
			obj.setResponsavelAtendimento(new FuncionarioVO());
			return;
		}
//		obj.setResponsavelAtendimento(getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimariaUnica(
//				obj.getResponsavelAtendimento().getCodigo(), false, nivelMontarDados, usuario));
	}

	public  void montarDadosResponsavelCadastro(AtendimentoVO obj, int nivelMontarDados, UsuarioVO usuario)
			throws Exception {
		if (obj.getResponsavelCadastro().getCodigo().intValue() == 0) {
			obj.setResponsavelCadastro(new UsuarioVO());
			return;
		}
		obj.setResponsavelCadastro(getFacadeFactory().getUsuarioFacade()
				.consultarPorChavePrimaria(obj.getResponsavelCadastro().getCodigo(), nivelMontarDados, usuario));
	}

	public static void montarDadosListaAnexo(AtendimentoVO obj) throws Exception {
		for (ArquivoVO anexo : obj.getListaAnexos()) {
			if (anexo.getPastaBaseArquivo().contains("ouvidoria")) {
				anexo.setPastaBaseArquivoEnum(PastaBaseArquivoEnum.OUVIDORIA);
			}
		}
	}

	public  void montarDadosTipagemOuvidoria(AtendimentoVO obj, int nivelMontarDados, UsuarioVO usuario)
			throws Exception {
		if (obj.getTipagemOuvidoriaVO().getCodigo().intValue() == 0) {
			obj.setTipagemOuvidoriaVO(new TipagemOuvidoriaVO());
			return;
		}
//		obj.setTipagemOuvidoriaVO(getFacadeFactory().getTipagemOuvidoriaFacade()
//				.consultarPorChavePrimaria(obj.getTipagemOuvidoriaVO().getCodigo(), nivelMontarDados, usuario));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.administrativo.OuvidoriaInterfaceFacade#
	 * consultarPorChavePrimaria(java.lang.Integer, int,
	 * negocio.comuns.arquitetura.UsuarioVO)
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.administrativo.OuvidoriaInterfaceFacade#
	 * consultarPorChavePrimaria(java.lang.Integer, int,
	 * negocio.comuns.arquitetura.UsuarioVO)
	 */
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public AtendimentoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, String idEntidade,
			UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(idEntidade, false, usuario);
		String sql = "SELECT * FROM Atendimento WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, codigoPrm);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( Atendimento ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	
	public void adicionarAtendimentoInteracaoDepartamentoVO(AtendimentoVO atendimentoVO,
			AtendimentoInteracaoDepartamentoVO obj) throws Exception {
//		getFacadeFactory().getAtendimentoInteracaoDepartamentoFacade().validarDados(obj);
//		obj.setAtendimentoVO(atendimentoVO);
//		int index = 0;
//		for (AtendimentoInteracaoDepartamentoVO objExistente : atendimentoVO
//				.getListaAtendimentoInteracaoDepartamentoVOs()) {
//			if (objExistente.getDepartamento().getCodigo().equals(obj.getDepartamento().getCodigo())
//					&& objExistente.getFuncionarioVO().getCodigo().equals(obj.getFuncionarioVO().getCodigo())
//					&& objExistente.getQuestionamento().equals(obj.getQuestionamento())) {
//				atendimentoVO.getListaAtendimentoInteracaoDepartamentoVOs().set(index, obj);
//				return;
//			}
//			index++;
//		}
//		atendimentoVO.getListaAtendimentoInteracaoDepartamentoVOs().add(obj);
	}

	
	public void removerAtendimentoInteracaoDepartamentoVO(AtendimentoVO atendimentoVO,
			AtendimentoInteracaoDepartamentoVO obj) throws Exception {
		int index = 0;
		for (AtendimentoInteracaoDepartamentoVO objExistente : atendimentoVO
				.getListaAtendimentoInteracaoDepartamentoVOs()) {
			if (objExistente.getDepartamento().getCodigo().equals(obj.getDepartamento().getCodigo())
					&& objExistente.getFuncionarioVO().getCodigo().equals(obj.getFuncionarioVO().getCodigo())
					&& objExistente.getQuestionamento().equals(obj.getQuestionamento())) {
				atendimentoVO.getListaAtendimentoInteracaoDepartamentoVOs().remove(index);
				return;
			}
			index++;
		}

	}

	
	public void adicionarAtendimentoInteracaoSolicitanteVO(AtendimentoVO atendimentoVO,
			AtendimentoInteracaoSolicitanteVO obj, Boolean questionamentoOuvidor) throws Exception {
//		getFacadeFactory().getAtendimentoInteracaoSolicitanteFacade().validarDados(obj);
//		obj.setAtendimentoVO(atendimentoVO);
//		int index = 0;
//		for (AtendimentoInteracaoSolicitanteVO objExistente : atendimentoVO
//				.getListaAtendimentoInteracaoSolicitanteVO()) {
//			if (objExistente.getQuestionamentoOuvidor().equals(obj.getQuestionamentoOuvidor())) {
//				atendimentoVO.getListaAtendimentoInteracaoSolicitanteVO().set(index, obj);
//				return;
//			}
//			index++;
//		}
//		atendimentoVO.getListaAtendimentoInteracaoSolicitanteVO().add(obj);
	}

	
	public void removerAtendimentoInteracaoSolicitanteVO(AtendimentoVO atendimentoVO,
			AtendimentoInteracaoSolicitanteVO obj) throws Exception {
		int index = 0;
		for (AtendimentoInteracaoSolicitanteVO objExistente : atendimentoVO
				.getListaAtendimentoInteracaoSolicitanteVO()) {
			if (objExistente.getQuestionamentoOuvidor().equals(obj.getQuestionamentoOuvidor())) {
				atendimentoVO.getListaAtendimentoInteracaoSolicitanteVO().remove(index);
				return;
			}
			index++;
		}

	}

	
	public void adicionarArquivoVO(AtendimentoVO atendimentoVO, ArquivoVO obj) throws Exception {
		int index = 0;
		for (ArquivoVO objExistente : atendimentoVO.getListaAnexos()) {
			if (objExistente.getNome().equals(obj.getNome())) {
				atendimentoVO.getListaAnexos().set(index, obj);
				return;
			}
			index++;
		}
		atendimentoVO.getListaAnexos().add(obj);
	}

	
	public void removerArquivoVO(AtendimentoVO atendimentoVO, ArquivoVO obj) throws Exception {
		int index = 0;
		for (ArquivoVO objExistente : atendimentoVO.getListaAnexos()) {
			if (objExistente.getNome().equals(obj.getNome())) {
				atendimentoVO.getListaAnexos().remove(index);
				return;
			}
			index++;
		}

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarValidacaoParaAtualizacaoDadosPessoa(final AtendimentoVO obj, UsuarioVO usuarioLogado,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		if (obj.getExistePessoa() && (!obj.getNome().equals(obj.getPessoaVO().getNome())
				|| !obj.getTelefone().equals(obj.getPessoaVO().getTelefoneRes())
				|| !obj.getEmail().equals(obj.getPessoaVO().getEmail()))) {

			obj.getPessoaVO().setNome(obj.getNome());
			obj.getPessoaVO().setTelefoneRes(obj.getTelefone());
			obj.getPessoaVO().setEmail(obj.getEmail());
			final StringBuilder sql = new StringBuilder(
					" UPDATE pessoa set nome=?, telefoneres=?, email=? WHERE ((codigo = ?)) ");
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
					sqlAlterar.setString(1, obj.getNome());
					sqlAlterar.setString(2, obj.getTelefone());
					sqlAlterar.setString(3, obj.getEmail());
					sqlAlterar.setInt(4, obj.getPessoaVO().getCodigo().intValue());
					return sqlAlterar;
				}
			});
		}

		if (!obj.getUsername().isEmpty() && !obj.getSenha().isEmpty() && !getFacadeFactory().getUsuarioFacade()
				.consultarPorCodigoPessoaSeUsuarioExiste(obj.getPessoaVO().getCodigo(), false, usuarioLogado)) {
			criarNovoUsuario(obj, configuracaoGeralSistemaVO);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void criarNovoUsuario(AtendimentoVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistema)
			throws Exception {
		PerfilAcessoVO perfilAcessoBusca = getFacadeFactory().getPerfilAcessoFacade()
				.consultarPerfilAcessoPadraoOuvidoria(obj.getUnidadeEnsino(), null, configuracaoGeralSistema);
		UsuarioVO usuarioIncluir = criarUsuario(obj, perfilAcessoBusca, configuracaoGeralSistema);
		if (!obj.getExistePessoa()) {
			getFacadeFactory().getPessoaFacade().incluir(usuarioIncluir.getPessoa(), false, null,
					configuracaoGeralSistema, true);
		}
		getFacadeFactory().getUsuarioFacade().incluir(usuarioIncluir, false, null);
		obj.setPessoaVO(usuarioIncluir.getPessoa());
		obj.setResponsavelCadastro(usuarioIncluir);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public UsuarioVO criarUsuario(AtendimentoVO atendimento, PerfilAcessoVO perfilAcesso,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
		UsuarioVO obj = new UsuarioVO();
		obj.setNome(atendimento.getNome());
		if (atendimento.getExistePessoa()) {
			obj.getPessoa().setCodigo(atendimento.getPessoaVO().getCodigo());
		}
		obj.getPessoa().setNome(atendimento.getNome());
		obj.getPessoa().setCPF(atendimento.getCPF());
		obj.getPessoa().setTelefoneRes(atendimento.getTelefone());
		obj.getPessoa().setEmail(atendimento.getEmail());
		obj.setSenha(atendimento.getSenha());
		obj.setUsername(atendimento.getUsername());
		obj.setTipoUsuario("OU");
		obj.setPerfilAdministrador(false);
//        UsuarioPerfilAcessoVO usuarioPerfilAcesso = new UsuarioPerfilAcessoVO();
//        usuarioPerfilAcesso.getUnidadeEnsinoVO().setCodigo(atendimento.getUnidadeEnsino());
//        usuarioPerfilAcesso.setPerfilAcesso(perfilAcesso);
//        obj.adicionarObjUsuarioPerfilAcessoAPartirMatriculaVOs(usuarioPerfilAcesso);
		return obj;
	}

	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarConsultarPessoaExistePorCPF(AtendimentoVO atendimentoVO, TipoAtendimentoEnum tipoAtendimento,
			String idEntidade, UsuarioVO usuarioLogado) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(idEntidade, false, usuarioLogado);
		validarCpf(atendimentoVO);
		String sql = "SELECT * FROM Atendimento WHERE cpf = ? and tipoAtendimentoEnum = ?";
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sql, atendimentoVO.getCPF(),
				tipoAtendimento.name());
		if (dadosSQL.next()) {
			atendimentoVO.setNome(dadosSQL.getString("nome"));
			atendimentoVO.setCPF(dadosSQL.getString("cpf"));
			atendimentoVO.setEmail(dadosSQL.getString("email"));
			atendimentoVO.setTelefone(dadosSQL.getString("telefone"));
//			atendimentoVO.getPessoaVO().setCodigo(dadosSQL.getInt("pessoa"));
//			atendimentoVO.getPessoaVO().setNome(dadosSQL.getString("nome"));
//			atendimentoVO.getPessoaVO().setCPF(dadosSQL.getString("cpf"));
//			atendimentoVO.getPessoaVO().setEmail(dadosSQL.getString("email"));
//			atendimentoVO.getPessoaVO().setTelefoneRes(dadosSQL.getString("telefone"));
			atendimentoVO.setPessoaVO(getFacadeFactory().getPessoaFacade().consultarPorCPFUnico(atendimentoVO.getCPF(),
					0, "", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioLogado));
		} else {
			atendimentoVO.setPessoaVO(getFacadeFactory().getPessoaFacade().consultarPorCPFUnico(atendimentoVO.getCPF(),
					0, "", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioLogado));
			if (atendimentoVO.getExistePessoa()) {
				atendimentoVO.setNome(atendimentoVO.getPessoaVO().getNome());
				atendimentoVO.setCPF(atendimentoVO.getPessoaVO().getCPF());
				atendimentoVO.setEmail(atendimentoVO.getPessoaVO().getEmail());
				atendimentoVO.setTelefone(atendimentoVO.getPessoaVO().getTelefoneRes());
			} else {
				atendimentoVO.setPessoaVO(new PessoaVO());
				atendimentoVO.setNome("");
				atendimentoVO.setEmail("");
				atendimentoVO.setTelefone("");

			}
		}
		atendimentoVO.setSenha("");
		atendimentoVO.setUsername("");
	}

	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarValidacoesParaBuscarResponsavelAtendimento(AtendimentoVO atendimento,
			Integer codigoUnidadeEnisno, Boolean controlarAcesso, TipoAtendimentoEnum tipoAtendimento,
			String idEntidade, UsuarioVO usuario) throws Exception {
		if (Uteis.isAtributoPreenchido(usuario)) {

			getFacadeFactory().getControleAcessoFacade().consultar(idEntidade, controlarAcesso, usuario);
			if (usuario.getTipoUsuario().equals(TipoUsuario.DIRETOR_MULTI_CAMPUS.getValor())
					&& codigoUnidadeEnisno == 0) {
				return;
			}
			if (Uteis.isAtributoPreenchido(usuario.getTipoVisaoAcesso())
					&& usuario.getTipoVisaoAcesso().equals(TipoVisaoEnum.PROFESSOR) && codigoUnidadeEnisno == 0) {
				return;
			}
		}
//		getFacadeFactory().getConfiguracaoAtendimentoFacade().realizarValidacaoSeExisteConfiguracaoAtendimentoValidaPorUnidadeEnsino(codigoUnidadeEnisno, usuario);
		atendimento.setResponsavelAtendimento(realizarBuscaReponsavelAtendimentoPorUltimoAtendimentoSocilitante(
				codigoUnidadeEnisno, atendimento.getCPF(), tipoAtendimento));
		if (atendimento.getResponsavelAtendimento().getCodigo().intValue() == 0) {
			atendimento.setResponsavelAtendimento(realizarBuscaReponsavelAtendimentoPorMenorQuantidadeDeAtendimento(codigoUnidadeEnisno, tipoAtendimento));
			if (atendimento.getResponsavelAtendimento().getCodigo().intValue() == 0) {
				if (usuario.getCodigo() == 0) {
					return ;
				} else {
					throw new ConsistirException("Nenhum Responsável atendimento disponivél no momento. Por favor entre em contato com o administrador do sistema.");
				}
			}
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public FuncionarioVO realizarBuscaReponsavelAtendimentoPorUltimoAtendimentoSocilitante(Integer codigoUnidadeEnisno,
			String cpfSolicitante, TipoAtendimentoEnum tipoAtendimento) throws Exception {
		StringBuilder sql = new StringBuilder(" ");
		sql.append(
				" select funcionario.codigo, pessoa.codigo as codigo_Pessoa, pessoa.nome, pessoa.email, pessoa.email2 from configuracaoatendimento ");
		sql.append(
				" inner join configuracaoatendimentounidadeensino on configuracaoatendimentounidadeensino.configuracaoatendimento = configuracaoatendimento.codigo ");
		sql.append(
				" inner join configuracaoatendimentofuncionario on configuracaoatendimentofuncionario.configuracaoatendimento = configuracaoatendimento.codigo ");
		sql.append(" inner join funcionario on funcionario.codigo = configuracaoatendimentofuncionario.funcionario ");
		sql.append(" inner join pessoa on pessoa.codigo = funcionario.pessoa ");
		sql.append(" where configuracaoatendimentounidadeensino.unidadeEnsino = ?  ");
		sql.append(" and configuracaoatendimentofuncionario.inativotemporario = false ");
		sql.append(
				" and configuracaoatendimentofuncionario.funcionario = (select responsavelAtendimento from atendimento   where cpf = ? and tipoAtendimentoEnum=? order by dataregistro desc  limit 1) ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigoUnidadeEnisno,
				cpfSolicitante, tipoAtendimento.name());
		if (tabelaResultado.next()) {
			FuncionarioVO obj = new FuncionarioVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.getPessoa().setCodigo(tabelaResultado.getInt("codigo_pessoa"));
			obj.getPessoa().setNome(tabelaResultado.getString("nome"));
			obj.getPessoa().setEmail(tabelaResultado.getString("email"));
			obj.getPessoa().setEmail2(tabelaResultado.getString("email2"));
			return obj;
		}
		return null;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public FuncionarioVO realizarBuscaReponsavelAtendimentoPorMenorQuantidadeDeAtendimento(Integer codigoUnidadeEnisno,
			TipoAtendimentoEnum tipoAtendimento) throws Exception {
		StringBuilder sql = new StringBuilder(" ");
		sql.append(
				" select funcionario.codigo, pessoa.codigo  as codigo_pessoa ,  pessoa.nome, pessoa.email, pessoa.email2 from funcionario ");
		sql.append(" inner join pessoa on pessoa.codigo = funcionario.pessoa ");
		sql.append(" where funcionario.codigo =  ");
		sql.append(" ( select funcionario from ( ");

		sql.append(" select configuracaoatendimentofuncionario.funcionario , count (atendimento.codigo) as qtd  ");
		sql.append(" from configuracaoatendimentounidadeensino ");
		sql.append(
				" inner join configuracaoatendimento on configuracaoatendimentounidadeensino.configuracaoatendimento = configuracaoatendimento.codigo ");
		sql.append(
				" inner join configuracaoatendimentofuncionario on configuracaoatendimentofuncionario.configuracaoatendimento = configuracaoatendimento.codigo ");
		sql.append(
				" left join atendimento on atendimento.responsavelAtendimento = configuracaoatendimentofuncionario.funcionario ");
		sql.append(" and atendimento.situacaoAtendimentoEnum <> 'FINALIZADO' and  atendimento.tipoAtendimentoEnum = ?");
		sql.append(" where configuracaoatendimentounidadeensino.unidadeEnsino = ?  ");
		sql.append("  and configuracaoatendimentofuncionario.inativotemporario = false ");
		sql.append(" group by  configuracaoatendimentofuncionario.funcionario order by qtd limit 1 ");
		sql.append(" ) as t )");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(),
				tipoAtendimento.name(), codigoUnidadeEnisno);
		if (tabelaResultado.next()) {
			FuncionarioVO obj = new FuncionarioVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.getPessoa().setCodigo(tabelaResultado.getInt("codigo_pessoa"));
			obj.getPessoa().setNome(tabelaResultado.getString("nome"));
			obj.getPessoa().setEmail(tabelaResultado.getString("email"));
			obj.getPessoa().setEmail2(tabelaResultado.getString("email2"));
			return obj;
		}
		return null;
	}

	public Integer consultarQuantidadeAtendimentoPorCPFPessoaPorTipoAtendimentoPorSituacao(String cpf,
			TipoAtendimentoEnum tipoAtendimentoEnum, SituacaoAtendimentoEnum situacaoAtendimentoEnum,
			boolean igualSituacao) throws Exception {
		StringBuilder sql = new StringBuilder(
				"SELECT count (codigo) as qtd FROM Atendimento WHERE tipoatendimentoenum  = ? and   cpf = ? ");
		if (igualSituacao) {
			sql.append(" and situacaoAtendimentoEnum = ? ");
		} else {
			sql.append(" and situacaoAtendimentoEnum <> ? ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(),
				tipoAtendimentoEnum.name(), cpf, situacaoAtendimentoEnum.name());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( Atendimento ).");
		}
		return tabelaResultado.getInt("qtd");
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Integer consultarQuantidadeAtendimentoJaVisualizadosPorCodigoPessoaPorTipoAtendimento(Integer codigoPessoa,
			TipoAtendimentoEnum tipoAtendimentoEnum) throws Exception {
		StringBuilder sql = new StringBuilder(
				"SELECT count (codigo) as qtd FROM Atendimento WHERE tipoatendimentoenum  = ? and   pessoa = ? and atendimentoJaVisualizado=?");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(),
				tipoAtendimentoEnum.name(), codigoPessoa, false);
		if (!tabelaResultado.next()) {
			return 0;
		}
		return tabelaResultado.getInt("qtd");
	}

	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public List<FuncionarioVO> realizarBuscaPorTodosReponsavelAtendimento(TipoAtendimentoEnum tipoAtendimento)
			throws Exception {
		StringBuilder sql = new StringBuilder(" ");
		sql.append(
				" select distinct funcionario.codigo, pessoa.codigo as codigo_Pessoa, pessoa.nome, pessoa.email, pessoa.email2 from atendimento ");
		sql.append(" inner join funcionario on funcionario.codigo = atendimento.responsavelAtendimento ");
		sql.append(" inner join pessoa on pessoa.codigo = funcionario.pessoa ");
		sql.append(" where tipoAtendimentoEnum= ? order by pessoa.nome  ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(),
				tipoAtendimento.name());
		List<FuncionarioVO> lista = new ArrayList<FuncionarioVO>();
		while (tabelaResultado.next()) {
			FuncionarioVO obj = new FuncionarioVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.getPessoa().setCodigo(tabelaResultado.getInt("codigo_pessoa"));
			obj.getPessoa().setNome(tabelaResultado.getString("nome"));
			obj.getPessoa().setEmail(tabelaResultado.getString("email"));
			obj.getPessoa().setEmail2(tabelaResultado.getString("email2"));
			lista.add(obj);
		}
		return lista;
	}

	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public HashMap<String, Object> consultar(Date dataInicial, Date dataTermino, Integer codigoResponsavelAtendimento,
			String nomeSolicitante, String CPFSolicitante, String emailSolicitante, Integer codigoTipoAtendimento,
			String quadroSituacao, TipoAtendimentoEnum tipagemAtedimento, Integer limite, Integer pagina,
			boolean controlarAcesso, int nivelMontarDados, String idEntidade, UsuarioVO usuario) throws Exception {
		HashMap<String, Object> mapa = new HashMap<String, Object>();
		List<Object> parametros = new ArrayList<>();

		StringBuilder sql = new StringBuilder(" SELECT * from atendimento   ");
		sql.append(" where tipoAtendimentoEnum = '").append(tipagemAtedimento.name()).append("' ");

		realizarValidacaoQuaisFiltrosUtilizadoParaConsulta(sql, dataInicial, dataTermino, codigoResponsavelAtendimento,
				nomeSolicitante, CPFSolicitante, emailSolicitante, codigoTipoAtendimento, quadroSituacao,
				tipagemAtedimento, parametros);

		sql.append(" ORDER BY atendimento.dataRegistro, atendimento.nome ");
		if (limite != null && limite > 0) {
			sql.append(" limit ").append(limite).append(" offset ").append(pagina);
		}
		List<AtendimentoVO> lista = montarDadosConsulta(
				getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), parametros.toArray()), nivelMontarDados,
				usuario);
		mapa.put("LISTA", lista);
		realizarBuscaQuadroSituacaoAtendimento(mapa, dataInicial, dataTermino, codigoResponsavelAtendimento,
				nomeSolicitante, CPFSolicitante, emailSolicitante, codigoTipoAtendimento, tipagemAtedimento);

		return mapa;
	}

	public void realizarValidacaoQuaisFiltrosUtilizadoParaConsulta(StringBuilder sql, Date dataInicial,
			Date dataTermino, Integer codigoResponsavelAtendimento, String nomeSolicitante, String CPFSolicitante,
			String emailSolicitante, Integer codigoTipoAtendimento, String quadroSituacao,
			TipoAtendimentoEnum tipagemAtedimento, List<Object> parametros) throws Exception {
		sql.append(" and ").append(realizarGeracaoWherePeriodo(dataInicial, dataTermino, "dataRegistro", false));
		if (codigoResponsavelAtendimento != 0) {
			sql.append(" and atendimento.responsavelAtendimento = ").append(codigoResponsavelAtendimento).append(" ");
		}
		if (codigoTipoAtendimento != 0) {
			sql.append(" and atendimento.tipagemouvidoria = ").append(codigoTipoAtendimento).append(" ");
		}
		if (!nomeSolicitante.isEmpty()) {
			parametros.add(PERCENT + nomeSolicitante + PERCENT);
			sql.append(" and  sem_acentos(atendimento.nome) ilike(sem_acentos(?))");
		}
		if (!CPFSolicitante.isEmpty()) {
			parametros.add(PERCENT + CPFSolicitante + PERCENT);
			sql.append(" and  atendimento.cpf ilike(?)");
		}
		if (!emailSolicitante.isEmpty()) {
			parametros.add(PERCENT + emailSolicitante + PERCENT);
			sql.append(" and  atendimento.email ilike(?)");
		}
		if (!quadroSituacao.isEmpty()) {
			if (quadroSituacao.equals("ATRASADA")) {
				sql.append(" and  atendimento.atendimentoAtrasado = true");
				sql.append(" and  atendimento.situacaoAtendimentoEnum <> '").append(SituacaoAtendimentoEnum.FINALIZADA)
						.append("'");//
			} else if (quadroSituacao.equals("NOVA")) {
				sql.append(" and  atendimento.situacaoAtendimentoEnum = '")
						.append(SituacaoAtendimentoEnum.EM_ANALISE_OUVIDOR).append("'");
				sql.append(" and  atendimento.codigo not in (")
						.append(realizarMontagemAtendimentoComRelacionamentoTabelaFilhas(tipagemAtedimento))
						.append(")");
			} else if (quadroSituacao.equals("ANALISE_OUVIDOR")) {
				sql.append(" and  atendimento.situacaoAtendimentoEnum = '")
						.append(SituacaoAtendimentoEnum.EM_ANALISE_OUVIDOR).append("'");
				sql.append(" and  atendimento.codigo in (")
						.append(realizarMontagemAtendimentoComRelacionamentoTabelaFilhas(tipagemAtedimento))
						.append(")");
			} else if (quadroSituacao.equals("FINALIZADA")) {
				sql.append(" and  atendimento.situacaoAtendimentoEnum = '").append(SituacaoAtendimentoEnum.FINALIZADA)
						.append("'");
			} else if (quadroSituacao.equals("AGUARDANDO_DEPARTAMENTO")) {
				sql.append(" and  atendimento.situacaoAtendimentoEnum = '")
						.append(SituacaoAtendimentoEnum.EM_PROCESSAMENTO).append("'");
			} else {
				sql.append(" and  atendimento.situacaoAtendimentoEnum = '")
						.append(SituacaoAtendimentoEnum.AGURADANDO_INFORMACAO_SOLICITANTE).append("'");
			}
		}

	}

	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Integer consultarTotalRegistro(Date dataInicial, Date dataTermino, Integer codigoResponsavelAtendimento,
			String nomeSolicitante, String CPFSolicitante, String emailSolicitante, Integer codigoTipoAtendimento,
			String quadroSituacao, TipoAtendimentoEnum tipagemAtedimento) throws Exception {
		List<Object> parametros = new ArrayList<>();
		StringBuilder sql = new StringBuilder(" SELECT  count(Atendimento.codigo) from atendimento   ");
		sql.append(" where tipoAtendimentoEnum = '").append(tipagemAtedimento.name()).append("' ");
		realizarValidacaoQuaisFiltrosUtilizadoParaConsulta(sql, dataInicial, dataTermino, codigoResponsavelAtendimento,
				nomeSolicitante, CPFSolicitante, emailSolicitante, codigoTipoAtendimento, quadroSituacao,
				tipagemAtedimento, parametros);
		return getTotalRegistro(getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), parametros.toArray()));
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarBuscaQuadroSituacaoAtendimento(HashMap<String, Object> mapa, Date dataInicial, Date dataTermino,
			Integer codigoResponsavelAtendimento, String nomeSolicitante, String CPFSolicitante,
			String emailSolicitante, Integer codigoTipoAtendimento, TipoAtendimentoEnum tipoAtendimento)
			throws Exception {
		StringBuilder sql = new StringBuilder(" ");
		List<Object> parametros = new ArrayList<>();
		/* Quadro situação nova */
		sql.append(" SELECT count(1) as qtd, 'NOVA' as tipo from atendimento   ");
		sql.append(" where tipoAtendimentoEnum = '").append(tipoAtendimento)
				.append("' and situacaoAtendimentoEnum='EM_ANALISE_OUVIDOR'  ");
		sql.append(" and codigo not in ( ");
		sql.append(realizarMontagemAtendimentoComRelacionamentoTabelaFilhas(tipoAtendimento));
		sql.append("  )  ");
		realizarValidacaoQuaisFiltrosUtilizadoParaConsulta(sql, dataInicial, dataTermino, codigoResponsavelAtendimento,
				nomeSolicitante, CPFSolicitante, emailSolicitante, codigoTipoAtendimento, "", tipoAtendimento,
				parametros);
		sql.append("   group by tipo ");
		/* termino situação nova */
		sql.append("   union all ");
		/* Quadro situação ANALISE_OUVIDOR */
		sql.append(" SELECT count(1) as qtd, 'ANALISE_OUVIDOR' as tipo from atendimento  ");
		sql.append(" where tipoAtendimentoEnum = '").append(tipoAtendimento)
				.append("' and situacaoAtendimentoEnum='EM_ANALISE_OUVIDOR'  ");
		sql.append(" and codigo in ( ");
		sql.append(realizarMontagemAtendimentoComRelacionamentoTabelaFilhas(tipoAtendimento));
		sql.append("  )  ");
		realizarValidacaoQuaisFiltrosUtilizadoParaConsulta(sql, dataInicial, dataTermino, codigoResponsavelAtendimento,
				nomeSolicitante, CPFSolicitante, emailSolicitante, codigoTipoAtendimento, "", tipoAtendimento,
				parametros);
		sql.append("   group by tipo ");
		/* termino situação ANALISE_OUVIDOR */
		sql.append("   union all ");
		/* Quadro situação ATRASADA */
		sql.append(" SELECT count(1) as qtd, 'ATRASADA' as tipo from atendimento where tipoAtendimentoEnum = '")
				.append(tipoAtendimento)
				.append("'  and  situacaoAtendimentoEnum <> 'FINALIZADA' and  atendimentoAtrasado = true");
		realizarValidacaoQuaisFiltrosUtilizadoParaConsulta(sql, dataInicial, dataTermino, codigoResponsavelAtendimento,
				nomeSolicitante, CPFSolicitante, emailSolicitante, codigoTipoAtendimento, "", tipoAtendimento,
				parametros);
		sql.append(" group by tipo ");
		/* termino situação ATRASADA */
		sql.append("   union all ");
		/* Quadro situação FINALIZADA */
		sql.append(" SELECT count(1) as qtd, 'FINALIZADA' as tipo from atendimento where tipoAtendimentoEnum = '")
				.append(tipoAtendimento).append("'  and  situacaoAtendimentoEnum='FINALIZADA' ");
		realizarValidacaoQuaisFiltrosUtilizadoParaConsulta(sql, dataInicial, dataTermino, codigoResponsavelAtendimento,
				nomeSolicitante, CPFSolicitante, emailSolicitante, codigoTipoAtendimento, "", tipoAtendimento,
				parametros);
		sql.append(" group by tipo ");
		/* termino situação FINALIZADA */
		sql.append("   union all ");
		/* Quadro situação AGUARDANDO_DEPARTAMENTO */
		sql.append(
				"   SELECT count(1) as qtd, 'AGUARDANDO_DEPARTAMENTO' as tipo from atendimento where tipoAtendimentoEnum = '")
				.append(tipoAtendimento).append("' and  situacaoAtendimentoEnum='EM_PROCESSAMENTO'  ");
		realizarValidacaoQuaisFiltrosUtilizadoParaConsulta(sql, dataInicial, dataTermino, codigoResponsavelAtendimento,
				nomeSolicitante, CPFSolicitante, emailSolicitante, codigoTipoAtendimento, "", tipoAtendimento,
				parametros);
		sql.append(" group by tipo ");
		/* termino situação AGUARDANDO_DEPARTAMENTO */
		sql.append("   union all ");
		/* Quadro situação AGUARDANDO_SOLICITANTE */
		sql.append(
				"   SELECT count(1) as qtd, 'AGUARDANDO_SOLICITANTE' as tipo from atendimento where tipoAtendimentoEnum = '")
				.append(tipoAtendimento).append("' and  situacaoAtendimentoEnum='AGURADANDO_INFORMACAO_SOLICITANTE'  ");
		realizarValidacaoQuaisFiltrosUtilizadoParaConsulta(sql, dataInicial, dataTermino, codigoResponsavelAtendimento,
				nomeSolicitante, CPFSolicitante, emailSolicitante, codigoTipoAtendimento, "", tipoAtendimento,
				parametros);
		sql.append(" group by tipo ");
		/* termino situação AGUARDANDO_SOLICITANTE */
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), parametros.toArray());
		while (tabelaResultado.next()) {
			mapa.put(tabelaResultado.getString(2), tabelaResultado.getInt(1));
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public String realizarMontagemAtendimentoComRelacionamentoTabelaFilhas(TipoAtendimentoEnum tipoAtendimento)
			throws Exception {
		StringBuilder sql = new StringBuilder(" ");
		sql.append(" (select distinct atendimento  from atendimentointeracaodepartamento ");
		sql.append(" inner join atendimento on atendimento.codigo = atendimentointeracaodepartamento.atendimento ");
		sql.append(" where tipoAtendimentoEnum = '").append(tipoAtendimento).append("') ");
		sql.append(" union  ");
		sql.append(" (select distinct atendimento  from atendimentointeracaosolicitante ");
		sql.append(" inner join atendimento on atendimento.codigo = atendimentointeracaosolicitante.atendimento ");
		sql.append(" where tipoAtendimentoEnum = '").append(tipoAtendimento).append("') ");
		return sql.toString();
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public String realizarBuscaAtendimentoAtrazados(TipoAtendimentoEnum tipoAtendimento) throws Exception {
		StringBuilder sql = new StringBuilder(" ");
		sql.append(" select array_to_string(array( ");
		sql.append(" SELECT tabela4.codigo from ( ");
		sql.append(" SELECT tabela3.codigo , ");
		sql.append(" tabela3.tempoMaximoParaRespostaOuvidoriaPeloOuvidor, ");
		sql.append(" tabela3.cidade,  ");
		sql.append(" count (tabela3.dia) from ( ");
		sql.append(" SELECT tabela2.codigo, tabela2.dia ,  ");
		sql.append(" tabela2.tempoMaximoParaRespostaOuvidoriaPeloOuvidor, ");
		sql.append(" tabela2.cidade, ");
		sql.append(
				" (EXTRACT(DAY FROM tabela2.dia),EXTRACT(MONTH FROM tabela2.dia),EXTRACT(YEAR FROM tabela2.dia)) as dia_feriado  ");
		sql.append(" FROM ( ");
		sql.append(" SELECT tabela1.codigo,  ");
		sql.append(" tabela1.tempoMaximoParaRespostaOuvidoriaPeloOuvidor, ");
		sql.append(" tabela1.cidade, ");
		sql.append(" (tabela1.dataregistro::date + tabela1.datas_geradas * '1 day' ::interval) AS dia from ( ");
		sql.append(" SELECT atendimento.codigo,  ");
		sql.append(" atendimento.dataregistro,  ");
		sql.append(" configuracaoatendimento.tempoMaximoParaRespostaOuvidoriaPeloOuvidor, ");
		sql.append(" unidadeensino.cidade, ");
		sql.append(" (case when datafechamento  is null ");
		sql.append(" then (generate_series(0, current_date::date - dataregistro::date, 1)) ");
		sql.append(" else (generate_series(0, datafechamento::date - dataregistro::date, 1)) end)  AS datas_geradas ");
		sql.append(" FROM atendimento	 ");
		sql.append(" inner join unidadeensino on unidadeensino.codigo = atendimento.unidadeEnsino ");
		sql.append(
				" inner join configuracaoatendimentoUnidadeEnsino on configuracaoatendimentoUnidadeEnsino.unidadeensino = unidadeensino.codigo ");
		sql.append(
				" inner join configuracaoatendimento on configuracaoatendimento.codigo = configuracaoatendimentoUnidadeEnsino.configuracaoatendimento ");
		sql.append(
				" where situacaoAtendimentoEnum <> 'FINALIZADA'  and configuracaoatendimento.tempoMaximoParaRespostaOuvidoriaPeloOuvidor > 0 ");
		sql.append(" and  tipoAtendimentoEnum = '").append(tipoAtendimento).append("' ");
		sql.append(" ) as tabela1 ");
		sql.append(" )as tabela2  WHERE EXTRACT(DOW FROM tabela2.dia) BETWEEN 1 AND 5  ");
		sql.append(" )as tabela3 ");
		sql.append(" where tabela3.dia_feriado not in ( ");
		sql.append(" select  ");
		sql.append(" case when recorrente = true  ");
		sql.append(" then (EXTRACT(DAY FROM data) , EXTRACT(MONTH FROM data), EXTRACT(YEAR FROM tabela3.dia))  ");
		sql.append(" else (EXTRACT(DAY FROM data) , EXTRACT(MONTH FROM data) , EXTRACT(YEAR FROM data)) ");
		sql.append(" end as dia_feriado ");
		sql.append(" from feriado ");
		sql.append(" where (cidade = tabela3.cidade  or cidade is null)) ");
		sql.append(" group by tabela3.codigo, ");
		sql.append(" tabela3.tempoMaximoParaRespostaOuvidoriaPeloOuvidor, ");
		sql.append(" tabela3.cidade  ");
		sql.append(" having count (tabela3.dia) > tabela3.tempoMaximoParaRespostaOuvidoriaPeloOuvidor ");
		sql.append(" 	) as tabela4 ");
		sql.append(" ),',') as codigoAtendimentoAtrasado  ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getString("codigoAtendimentoAtrasado");
		}
		return "";
	}

//	/**
//	 * Operação reponsável por retornar o identificador desta classe. Este
//	 * identificar é utilizado para verificar as permissões de acesso as
//	 * operações desta classe.
//	 */
//	public static String getIdEntidade() {
//		return Atendimento.idEntidade;
//	}
//
//	/**
//	 * Operação reponsável por definir um novo valor para o identificador desta
//	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
//	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
//	 * que Como o controle de acesso é realizado com base neste identificador,
//	 */
//	public void setIdEntidade(String idEntidade) {
//		Atendimento.idEntidade = idEntidade;
//	}

}
