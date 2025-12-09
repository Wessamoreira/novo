package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.basico.ConfiguracaoGEDVO;
import negocio.comuns.basico.enumeradores.ProvedorDeAssinaturaEnum;
import negocio.comuns.basico.enumeradores.TipoProvedorAssinaturaEnum;
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
import negocio.comuns.academico.DocumentoAssinadoPessoaVO;
import negocio.comuns.academico.DocumentoAssinadoVO;
import negocio.comuns.academico.ExpedicaoDiplomaVO;
//import negocio.comuns.academico.GestaoXmlGradeCurricularVO;
import negocio.comuns.academico.enumeradores.DocumentoAssinadoOrigemEnum;
import negocio.comuns.academico.enumeradores.SituacaoDocumentoAssinadoPessoaEnum;
import negocio.comuns.academico.enumeradores.TipoAssinaturaDocumentoEnum;
import negocio.comuns.academico.enumeradores.TipoOrigemDocumentoAssinadoEnum;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.DocumentoAssinadoPessoaInterfaceFacade;
import webservice.certisign.comuns.CertiSignRSVO;
import webservice.certisign.comuns.DocumentPessoaRSVO;
import webservice.techcert.comuns.*;

@Repository
@Scope("singleton")
public class DocumentoAssinadoPessoa extends ControleAcesso implements DocumentoAssinadoPessoaInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7035722896015099005L;

	protected static String idEntidade;

	private static String PESSOA_RESPONSAVEL_ASSINATURA = "pessoaResponsavelAssinatura";

	private static String CPF_PESSOA_DIFERENTE_ASSINATURA = "cpfPessoaDiferenteAssinatura";

	public DocumentoAssinadoPessoa() throws Exception {
		super();
		setIdEntidade("DocumentoAssinado");
	}

	private void validarDados(DocumentoAssinadoPessoaVO obj) {
		/*
		 * if (!Uteis.isAtributoPreenchido(obj.getRecebimentoCompraVO())) { throw new StreamSeiException("O campo Recebimento de Compra (NotaFiscalEntradaRecebimentoCompra) não foi informado."); }
		 */

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(List<DocumentoAssinadoPessoaVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			for (DocumentoAssinadoPessoaVO obj : lista) {
				validarDados(obj);
				if (obj.getCodigo() == 0) {
					incluir(obj, verificarAcesso, usuarioVO);
				} else {
					alterar(obj, verificarAcesso, usuarioVO);
				}
			}
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void incluir(final DocumentoAssinadoPessoaVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		incluir(getIdEntidade(), verificarAcesso, usuario);
		incluir(obj, "DocumentoAssinadoPessoa", new AtributoPersistencia()					
				.add("dataSolicitacao", obj.getDataSolicitacao())
				.add("dataAssinatura", obj.getDataAssinatura())
				.add("pessoa", obj.getPessoaVO())					
				.add("tipoPessoa", obj.getTipoPessoa())
				.add("situacaoDocumentoAssinadoPessoa", obj.getSituacaoDocumentoAssinadoPessoaEnum())
				.add("documentoAssinadoOrigemEnum", obj.getDocumentoAssinadoOrigemEnum())
				.add("tipoAssinaturaDocumentoEnum", obj.getTipoAssinaturaDocumentoEnum())
				.add("motivoRejeicao", obj.getMotivoRejeicao())
				.add("dataRejeicao", obj.getDataRejeicao())
				.add("documentoAssinado", obj.getDocumentoAssinadoVO())
				.add("ordemAssinatura", obj.getOrdemAssinatura())
				.add("codigoAssinatura", obj.getCodigoAssinatura())
				.add("acaoAssinatura", obj.getAcaoAssinatura())
				.add("urlAssinatura", obj.getUrlAssinatura())
				.add("jsonAssinatura", obj.getJsonAssinatura())
				.add("nomePessoa", obj.getNomePessoa())
				.add("emailPessoa", obj.getEmailPessoa())
				.add("cpfPessoa", obj.getCpfPessoa())
				.add("cargo", obj.getCargo())
				.add("assinarPorCNPJ", obj.getAssinarPorCNPJ())
				.add("nome", obj.getNome())
				.add("urlprovedordeassinatura", obj.getUrlProvedorDeAssinatura())
				.add("titulo", obj.getTitulo()), usuario);
				
		
		obj.setNovoObj(Boolean.FALSE);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void alterar(final DocumentoAssinadoPessoaVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		alterar(getIdEntidade(), verificarAcesso, usuario);
		alterar(obj, "DocumentoAssinadoPessoa", new AtributoPersistencia()					
				.add("dataSolicitacao", obj.getDataSolicitacao())
				.add("dataAssinatura", obj.getDataAssinatura())
				.add("pessoa", obj.getPessoaVO())					
				.add("tipoPessoa", obj.getTipoPessoa())
				.add("situacaoDocumentoAssinadoPessoa", obj.getSituacaoDocumentoAssinadoPessoaEnum())
				.add("documentoAssinadoOrigemEnum", obj.getDocumentoAssinadoOrigemEnum())
				.add("tipoAssinaturaDocumentoEnum", obj.getTipoAssinaturaDocumentoEnum())
				.add("motivoRejeicao", obj.getMotivoRejeicao())
				.add("dataRejeicao", obj.getDataRejeicao())
				.add("documentoAssinado", obj.getDocumentoAssinadoVO())
				.add("ordemAssinatura", obj.getOrdemAssinatura())
				.add("codigoAssinatura", obj.getCodigoAssinatura())
				.add("acaoAssinatura", obj.getAcaoAssinatura())
				.add("urlAssinatura", obj.getUrlAssinatura())
				.add("jsonAssinatura", obj.getJsonAssinatura())
				.add("nomePessoa", obj.getNomePessoa())
				.add("emailPessoa", obj.getEmailPessoa())
				.add("cpfPessoa", obj.getCpfPessoa())
				.add("cargo", obj.getCargo())
				.add("assinarPorCNPJ", obj.getAssinarPorCNPJ())
				.add("nome", obj.getNome())
				.add("urlprovedordeassinatura", obj.getUrlProvedorDeAssinatura())
				.add("titulo", obj.getTitulo()), new AtributoPersistencia().add("codigo", obj.getCodigo()), usuario);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void atualizarDadosAssinatura(final DocumentoAssinadoPessoaVO obj, Boolean assinarPorCNPJ, Integer ordemAssinatura, String provedorAssinatura) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE DocumentoAssinadoPessoa set dataAssinatura=?, situacaoDocumentoAssinadoPessoa=?, provedorAssinatura=? WHERE ((codigo = ?))");
		if (Uteis.isAtributoPreenchido(assinarPorCNPJ) && assinarPorCNPJ) {
			sql.append(" and assinarPorCNPJ");
		} else {
			sql.append(" and assinarPorCNPJ = false");
		}
		if (Uteis.isAtributoPreenchido(ordemAssinatura) && ordemAssinatura > 0) {
			sql.append(" and ordemAssinatura = ?");
		}
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
				sqlAlterar.setTimestamp(1, Uteis.getDataJDBCTimestamp(obj.getDataAssinatura()));
				sqlAlterar.setString(2, obj.getSituacaoDocumentoAssinadoPessoaEnum().name());
				if (Uteis.isAtributoPreenchido(provedorAssinatura)) {
					sqlAlterar.setString(3, provedorAssinatura);
				} else {
					sqlAlterar.setNull(3, 0);
				}
				sqlAlterar.setInt(4, obj.getCodigo());
				if (Uteis.isAtributoPreenchido(ordemAssinatura) && ordemAssinatura > 0) {
					sqlAlterar.setInt(5, ordemAssinatura);
				}
				return sqlAlterar;
			}
		});
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public Integer atualizarDadosAssinaturaPorProvedorCertisign(String codigoProvedorAssinatura, String nome, String cpf, SituacaoDocumentoAssinadoPessoaEnum situacao,  String motivoRejeicao, Date dataAssinaturaOrRejeicao, DocumentoAssinadoOrigemEnum documentoAssinadoOrigemEnum, String jsonAssinatura) throws Exception {
		final StringBuilder sb = new StringBuilder("update documentoassinadopessoa set situacaodocumentoassinadopessoa = ?,  documentoassinadoorigemenum=?, jsonassinatura=? ");
		if(situacao.isAssinado()) {
			sb.append(", dataAssinatura=? ");			
		}else {
			sb.append(", datarejeicao=?, motivorejeicao=? ");			
		}		
		sb.append(" where codigo in ( select  documentoassinadopessoa.codigo ");
		sb.append(" from documentoassinadopessoa  ");
		sb.append(" inner join documentoassinado on documentoassinadopessoa.documentoassinado = documentoassinado.codigo  ");
		sb.append(" left join pessoa on pessoa.codigo = documentoassinadopessoa.pessoa  ");
		sb.append(" where  documentoassinado.codigoprovedordeassinatura = ? ");
		sb.append("and ( ");
		if (Uteis.isAtributoPreenchido(nome) && Uteis.isAtributoPreenchido(cpf)) {
			sb.append(" ((documentoassinadopessoa.pessoa is not null and upper(sem_acentos(trim(pessoa.nome))) = upper(sem_acentos(?))) or (documentoassinadopessoa.pessoa is null and upper(sem_acentos(trim(documentoassinadopessoa.nomepessoa))) = upper(sem_acentos(?)))) ");
			sb.append(" or ((documentoassinadopessoa.pessoa is not null and replace(replace((pessoa.cpf),'.',''),'-','') = replace(replace((?),'.',''),'-','')) or (documentoassinadopessoa.pessoa is null and replace(replace((documentoassinadopessoa.cpfpessoa),'.',''),'-','') = replace(replace((?),'.',''),'-',''))) ");
		}else if(Uteis.isAtributoPreenchido(nome)) {
			sb.append(" ((documentoassinadopessoa.pessoa is not null and upper(sem_acentos(trim(pessoa.nome))) = upper(sem_acentos(?))) or (documentoassinadopessoa.pessoa is null and upper(sem_acentos(trim(documentoassinadopessoa.nomepessoa))) = upper(sem_acentos(?)))) ");
		}
		sb.append(" )) returning pessoa ");
		return getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {			
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement update = arg0.prepareStatement(sb.toString());
				int x = 1;
				update.setString(x++, situacao.name());
				update.setString(x++, documentoAssinadoOrigemEnum.name());
				update.setString(x++, jsonAssinatura);
				update.setTimestamp(x++, Uteis.getDataJDBCTimestamp(dataAssinaturaOrRejeicao));
				if(!situacao.isAssinado()) {
					update.setString(x++, motivoRejeicao);
				}
				update.setString(x++, codigoProvedorAssinatura);
				if (Uteis.isAtributoPreenchido(nome) && Uteis.isAtributoPreenchido(cpf)) {
					update.setString(x++, nome.toUpperCase());
					update.setString(x++, nome.toUpperCase());
					update.setString(x++, cpf.trim());
					update.setString(x++, cpf.trim());
				}else if(Uteis.isAtributoPreenchido(nome)) {
					update.setString(x++, nome.toUpperCase());
					update.setString(x++, nome.toUpperCase());
				}
				return update;
			}
		}, new ResultSetExtractor<Integer>() {

			@Override
			public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
				if(arg0.next()) {
					return arg0.getInt("pessoa");
				}
				return null;
			}
		});

	
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public Integer atualizarDadosAssinaturaPorProvedorTechCert(String chaveProvedordeAssinatura, String nome, String cpf, SituacaoDocumentoAssinadoPessoaEnum situacao, String motivoRejeicao, Date dataAssinaturaOrRejeicao, DocumentoAssinadoOrigemEnum documentoAssinadoOrigemEnum, String jsonAssinatura, String urlAssinatura) throws Exception {
		final StringBuilder sb = new StringBuilder("update documentoassinadopessoa set situacaodocumentoassinadopessoa = ?,  documentoassinadoorigemenum=?, jsonassinatura=? ");
		if(Uteis.isAtributoPreenchido(urlAssinatura)) {
			sb.append(", urlassinatura=? ");
		}
		if(situacao.isAssinado()) {
			sb.append(", dataAssinatura=? ");
		}else {
			sb.append(", datarejeicao=?, motivorejeicao=? ");
		}
		sb.append(" where codigo in ( select  documentoassinadopessoa.codigo ");
		sb.append(" from documentoassinadopessoa  ");
		sb.append(" inner join documentoassinado on documentoassinadopessoa.documentoassinado = documentoassinado.codigo  ");
		sb.append(" left join pessoa on pessoa.codigo = documentoassinadopessoa.pessoa  ");
		sb.append(" where  documentoassinado.chaveprovedordeassinatura = ? ");
		sb.append("and ( ");
		if (Uteis.isAtributoPreenchido(nome) && Uteis.isAtributoPreenchido(cpf)) {
			sb.append(" ((documentoassinadopessoa.pessoa is not null and upper(sem_acentos(trim(pessoa.nome))) = upper(sem_acentos(?))) or (documentoassinadopessoa.pessoa is null and upper(sem_acentos(trim(documentoassinadopessoa.nomepessoa))) = upper(sem_acentos(?)))) ");
			sb.append(" or ((documentoassinadopessoa.pessoa is not null and replace(replace((pessoa.cpf),'.',''),'-','') = replace(replace((?),'.',''),'-','')) or (documentoassinadopessoa.pessoa is null and replace(replace((documentoassinadopessoa.cpfpessoa),'.',''),'-','') = replace(replace((?),'.',''),'-',''))) ");
		}else if(Uteis.isAtributoPreenchido(nome)) {
			sb.append(" ((documentoassinadopessoa.pessoa is not null and upper(sem_acentos(trim(pessoa.nome))) = upper(sem_acentos(?))) or (documentoassinadopessoa.pessoa is null and upper(sem_acentos(trim(documentoassinadopessoa.nomepessoa))) = upper(sem_acentos(?)))) ");
		}
		sb.append(" )) returning pessoa ");
		return getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement update = arg0.prepareStatement(sb.toString());
				int x = 1;
				update.setString(x++, situacao.name());
				update.setString(x++, documentoAssinadoOrigemEnum.name());
				update.setString(x++, jsonAssinatura);
				if (Uteis.isAtributoPreenchido(urlAssinatura)) {
					update.setString(x++, urlAssinatura);
				}
				update.setTimestamp(x++, Uteis.getDataJDBCTimestamp(dataAssinaturaOrRejeicao));
				if(!situacao.isAssinado()) {
					update.setString(x++, motivoRejeicao);
				}
				update.setString(x++, chaveProvedordeAssinatura);
				if (Uteis.isAtributoPreenchido(nome) && Uteis.isAtributoPreenchido(cpf)) {
					update.setString(x++, nome.toUpperCase());
					update.setString(x++, nome.toUpperCase());
					update.setString(x++, cpf.trim());
					update.setString(x++, cpf.trim());
				}else if(Uteis.isAtributoPreenchido(nome)) {
					update.setString(x++, nome.toUpperCase());
					update.setString(x++, nome.toUpperCase());
				}
				return update;
			}
		}, new ResultSetExtractor<Integer>() {

			@Override
			public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
				if(arg0.next()) {
					return arg0.getInt("pessoa");
				}
				return null;
			}
		});
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public Integer atualizarDadosAssinaturaPorProvedorTechCertUrl(String chaveProvedordeAssinatura, String nome, String cpf, SituacaoDocumentoAssinadoPessoaEnum situacao, DocumentoAssinadoOrigemEnum documentoAssinadoOrigemEnum, String jsonAssinatura, String urlAssinatura, String urlProvedorDeAssinatura) throws Exception {
		final StringBuilder sb = new StringBuilder("update documentoassinadopessoa set situacaodocumentoassinadopessoa = ?,  documentoassinadoorigemenum=?, jsonassinatura=? ");
		if (Uteis.isAtributoPreenchido(urlAssinatura)) {
			sb.append(", urlassinatura=? ");
		}
		if (Uteis.isAtributoPreenchido(urlProvedorDeAssinatura)) {
			sb.append(", urlprovedordeassinatura=? ");
		}
		sb.append(" where codigo in ( select  documentoassinadopessoa.codigo ");
		sb.append(" from documentoassinadopessoa  ");
		sb.append(" inner join documentoassinado on documentoassinadopessoa.documentoassinado = documentoassinado.codigo  ");
		sb.append(" left join pessoa on pessoa.codigo = documentoassinadopessoa.pessoa  ");
		sb.append(" where  documentoassinado.chaveprovedordeassinatura = ? ");
		sb.append("and ( ");
		if (Uteis.isAtributoPreenchido(nome) && Uteis.isAtributoPreenchido(cpf)) {
			sb.append(" ((documentoassinadopessoa.pessoa is not null and upper(sem_acentos(trim(pessoa.nome))) = upper(sem_acentos(?))) or (documentoassinadopessoa.pessoa is null and upper(sem_acentos(trim(documentoassinadopessoa.nomepessoa))) = upper(sem_acentos(?)))) ");
			sb.append(" or ((documentoassinadopessoa.pessoa is not null and replace(replace((pessoa.cpf),'.',''),'-','') = replace(replace((?),'.',''),'-','')) or (documentoassinadopessoa.pessoa is null and replace(replace((documentoassinadopessoa.cpfpessoa),'.',''),'-','') = replace(replace((?),'.',''),'-',''))) ");
		} else if (Uteis.isAtributoPreenchido(nome)) {
			sb.append(" ((documentoassinadopessoa.pessoa is not null and upper(sem_acentos(trim(pessoa.nome))) = upper(sem_acentos(?))) or (documentoassinadopessoa.pessoa is null and upper(sem_acentos(trim(documentoassinadopessoa.nomepessoa))) = upper(sem_acentos(?)))) ");
		}
		sb.append(" )) returning pessoa ");
		return getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement update = arg0.prepareStatement(sb.toString());
				int x = 1;
				update.setString(x++, situacao.name());
				update.setString(x++, documentoAssinadoOrigemEnum.name());
				update.setString(x++, jsonAssinatura);
				if (Uteis.isAtributoPreenchido(urlAssinatura)) {
					update.setString(x++, urlAssinatura);
				}
				if (Uteis.isAtributoPreenchido(urlProvedorDeAssinatura)) {
					update.setString(x++, urlProvedorDeAssinatura);
				}
				update.setString(x++, chaveProvedordeAssinatura);
				if (Uteis.isAtributoPreenchido(nome) && Uteis.isAtributoPreenchido(cpf)) {
					update.setString(x++, nome.toUpperCase());
					update.setString(x++, nome.toUpperCase());
					update.setString(x++, cpf.trim());
					update.setString(x++, cpf.trim());
				} else if (Uteis.isAtributoPreenchido(nome)) {
					update.setString(x++, nome.toUpperCase());
					update.setString(x++, nome.toUpperCase());
				}
				return update;
			}
		}, new ResultSetExtractor<Integer>() {

			@Override
			public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
				if (arg0.next()) {
					return arg0.getInt("pessoa");
				}
				return null;
			}
		});
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void atualizarSituacaoPendenteDocumentoAssinadoPessoaParaRejeitado(final DocumentoAssinadoVO obj, final String motivo, UsuarioVO usuario) throws Exception {
		final String sql = "UPDATE DocumentoAssinadoPessoa set dataRejeicao=?, motivoRejeicao=?, situacaoDocumentoAssinadoPessoa=? WHERE documentoAssinado = ? and situacaoDocumentoAssinadoPessoa= ? " +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				sqlAlterar.setTimestamp(1, Uteis.getDataJDBCTimestamp(new Date()));
				sqlAlterar.setString(2, motivo);
				sqlAlterar.setString(3, SituacaoDocumentoAssinadoPessoaEnum.REJEITADO.name());
				sqlAlterar.setInt(4, obj.getCodigo());
				sqlAlterar.setString(5, SituacaoDocumentoAssinadoPessoaEnum.PENDENTE.name());
				return sqlAlterar;
			}
		});
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void atualizarSituacaoRejeitadoDocumentoAssinadoPessoaParaPendente(final DocumentoAssinadoVO obj,  UsuarioVO usuario) throws Exception {
		final String sql = "UPDATE DocumentoAssinadoPessoa set dataRejeicao=null, motivoRejeicao=null, situacaoDocumentoAssinadoPessoa=? WHERE documentoAssinado = ? and situacaoDocumentoAssinadoPessoa=? " +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				sqlAlterar.setString(1, SituacaoDocumentoAssinadoPessoaEnum.PENDENTE.name());
				sqlAlterar.setInt(2, obj.getCodigo());
				sqlAlterar.setString(3, SituacaoDocumentoAssinadoPessoaEnum.REJEITADO.name());
				return sqlAlterar;
			}
		});
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void atualizarDadosRejeicao(final DocumentoAssinadoPessoaVO obj) throws Exception {
		final String sql = "UPDATE DocumentoAssinadoPessoa set dataRejeicao=?, motivoRejeicao=?,  situacaoDocumentoAssinadoPessoa=? WHERE ((codigo = ?))";
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				sqlAlterar.setTimestamp(1, Uteis.getDataJDBCTimestamp(obj.getDataRejeicao()));
				sqlAlterar.setString(2, obj.getMotivoRejeicao());
				sqlAlterar.setString(3, obj.getSituacaoDocumentoAssinadoPessoaEnum().name());
				sqlAlterar.setInt(4, obj.getCodigo());
				return sqlAlterar;
			}
		});
	}
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void atualizarEmailSignatarioConcedente(final DocumentoAssinadoPessoaVO obj) throws Exception {
		final String sql = "UPDATE DocumentoAssinadoPessoa set emailPessoa=?   WHERE ((codigo = ?))";
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);				
				sqlAlterar.setString(1, obj.getEmailPessoa());			
				sqlAlterar.setInt(2, obj.getCodigo());
				return sqlAlterar;
			}
		});
	}

	public StringBuilder getSqlConsultaDadosCompletos() {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append(" select  documentoassinadoPessoa.codigo as \"documentoassinadoPessoa.codigo\", documentoassinadoPessoa.dataSolicitacao as \"documentoassinadoPessoa.dataSolicitacao\", ");
		sqlStr.append(" documentoassinadoPessoa.dataAssinatura as \"documentoassinadoPessoa.dataAssinatura\", documentoassinadoPessoa.dataRejeicao as \"documentoassinadoPessoa.dataRejeicao\", ");
		sqlStr.append(" documentoassinadoPessoa.tipoPessoa as \"documentoassinadoPessoa.tipoPessoa\", documentoassinadoPessoa.situacaoDocumentoAssinadoPessoa as \"documentoassinadoPessoa.situacaoDocumentoAssinadoPessoa\", ");
		sqlStr.append(" documentoassinadoPessoa.motivoRejeicao as \"documentoassinadoPessoa.motivoRejeicao\",  documentoassinadoPessoa.ordemAssinatura as \"documentoassinadoPessoa.ordemAssinatura\",  ");
		sqlStr.append(" documentoassinadoPessoa.cargo as \"documentoassinadoPessoa.cargo\",  documentoassinadoPessoa.titulo as \"documentoassinadoPessoa.titulo\",  ");
		sqlStr.append(" documentoassinadoPessoa.codigoAssinatura as \"documentoassinadoPessoa.codigoAssinatura\",  documentoassinadoPessoa.acaoAssinatura as \"documentoassinadoPessoa.acaoAssinatura\",  ");
		sqlStr.append(" documentoassinadoPessoa.urlAssinatura as \"documentoassinadoPessoa.urlAssinatura\",    ");
		sqlStr.append(" documentoassinadoPessoa.nomePessoa as \"documentoassinadoPessoa.nomePessoa\",    ");
		sqlStr.append(" documentoassinadoPessoa.emailPessoa as \"documentoassinadoPessoa.emailPessoa\",    ");
		sqlStr.append(" documentoassinadoPessoa.cpfPessoa as \"documentoassinadoPessoa.cpfPessoa\",    ");
		sqlStr.append(" documentoassinadoPessoa.tipoAssinaturaDocumentoEnum as \"documentoassinadoPessoa.tipoAssinaturaDocumentoEnum\",    ");
		sqlStr.append(" documentoassinadoPessoa.documentoAssinadoOrigemEnum as \"documentoassinadoPessoa.documentoAssinadoOrigemEnum\",    ");
		sqlStr.append(" documentoassinadoPessoa.jsonAssinatura as \"documentoassinadoPessoa.jsonAssinatura\", documentoassinadoPessoa.assinarPorCNPJ as \"documentoassinadoPessoa.assinarPorCNPJ\", documentoassinadoPessoa.nome as \"documentoassinadoPessoa.nome\", ");
		sqlStr.append(" documentoassinadopessoa.provedorAssinatura as \"documentoassinadopessoa.provedorAssinatura\", ");
		sqlStr.append(" pessoa.codigo as \"pessoa.codigo\", ");
		sqlStr.append(" pessoa.nome AS \"pessoa.nome\", pessoa.cpf AS \"pessoa.cpf\", ");
		sqlStr.append(" pessoa.email AS \"pessoa.email\", pessoa.email2 AS \"pessoa.email2\", ");
		sqlStr.append(" pessoa.endereco AS \"pessoa.endereco\", pessoa.numero AS \"pessoa.numero\", ");
		sqlStr.append(" pessoa.setor AS \"pessoa.setor\",  pessoa.senhaCertificadoParaDocumento AS \"pessoa.senhaCertificadoParaDocumento\", ");
		sqlStr.append(" cidade.codigo AS \"cidade.codigo\", cidade.nome AS \"cidade.nome\", ");
		sqlStr.append(" estado.codigo AS \"estado.codigo\", estado.nome AS \"estado.nome\", estado.sigla AS \"estado.sigla\" ");
		sqlStr.append(" from documentoassinadoPessoa ");
		sqlStr.append(" LEFT join pessoa  on documentoassinadoPessoa.pessoa = pessoa.codigo ");
		sqlStr.append(" LEFT JOIN cidade ON cidade.codigo = pessoa.cidade ");
		sqlStr.append(" LEFT JOIN estado ON estado.codigo = cidade.estado ");
		return sqlStr;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<DocumentoAssinadoPessoaVO> consultarDocumentosAssinadoPessoaPorDocumentoAssinado(DocumentoAssinadoVO obj, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sqlStr = getSqlConsultaDadosCompletos();
		sqlStr.append(" where documentoassinadoPessoa.documentoassinado = ").append(obj.getCodigo()).append(" ");
		sqlStr.append(" order by documentoassinadoPessoa,ordemAssinatura ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<DocumentoAssinadoPessoaVO> vetResultado = new ArrayList<DocumentoAssinadoPessoaVO>(0);
		while (tabelaResultado.next()) {
			DocumentoAssinadoPessoaVO dap = montarDadosCompleto(tabelaResultado, nivelMontarDados, usuarioLogado);
			dap.setDocumentoAssinadoVO(obj);
			vetResultado.add(dap);
		}
		return vetResultado;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public Boolean consultaSeExisteDocumentoAssinadoPessoaPorCodigoPorSituacao(Integer codigo, SituacaoDocumentoAssinadoPessoaEnum situacaoDocumentoAssinadoPessoaEnum) throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append(" SELECT count(dap.codigo) QTDE ");
		sql.append(" from documentoassinadopessoa as dap ");
		sql.append(" where dap.codigo = ? ");
		sql.append(" and dap.situacaodocumentoassinadopessoa = ? ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigo, situacaoDocumentoAssinadoPessoaEnum.name());
		return Uteis.isAtributoPreenchido(rs, Uteis.QTDE, TipoCampoEnum.INTEIRO);
	}

	@Override
	public DocumentoAssinadoPessoaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		StringBuilder sqlStr = getSqlConsultaDadosCompletos();
		sqlStr.append(" WHERE documentoassinadopessoa.codigo = ?");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( Documento Assinado ).");
		}
		return (montarDadosCompleto(tabelaResultado, nivelMontarDados, usuario));
	}

	
	public List<DocumentoAssinadoPessoaVO> montarDadosConsultaCompleto(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<DocumentoAssinadoPessoaVO> vetResultado = new ArrayList<DocumentoAssinadoPessoaVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDadosCompleto(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}
	
	@Override
	public void excluir(DocumentoAssinadoPessoaVO obj, UsuarioVO usuario) throws Exception {
		try {
			getFacadeFactory().getControleAcessoFacade().excluir("MapaDocumentoAssinadoPessoa", true, usuario);
			String sql = "DELETE FROM documentoassinadopessoa WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void excluirPendente(DocumentoAssinadoPessoaVO obj, UsuarioVO usuario) throws Exception {
		try {
			getFacadeFactory().getControleAcessoFacade().excluir("MapaDocumentoAssinadoPessoa", true, usuario);
			String sql = "DELETE FROM documentoassinadopessoa WHERE ((codigo = ?) AND situacaodocumentoassinadopessoa = 'PENDENTE')" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	public DocumentoAssinadoPessoaVO consultarDocumentosAssinadoPessoaPorArquivoEPessoaDocumentoAssinado(PessoaVO pessoa, ArquivoVO arquivoVO , boolean controlarAcesso, UsuarioVO usuarioLogado) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuarioLogado);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select documentoassinadopessoa.codigo as codDocumentoAssinadoPessoa, documentoassinado.codigo as codDocumentoAssinado from documentoassinadopessoa ");
		sqlStr.append(" inner join  documentoassinado on documentoassinadopessoa.documentoassinado = documentoassinado.codigo ");
		sqlStr.append(" 	where documentoassinadopessoa.pessoa = ? and documentoassinado.arquivo = ? ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] {pessoa.getCodigo(), arquivoVO.getCodigo()});
		if(tabelaResultado.next()) {
			DocumentoAssinadoPessoaVO obj = new DocumentoAssinadoPessoaVO();
			obj.setCodigo(tabelaResultado.getInt("codDocumentoAssinadoPessoa"));
			obj.getDocumentoAssinadoVO().setCodigo(tabelaResultado.getInt("codDocumentoAssinado"));
			return obj;
		}
		return new DocumentoAssinadoPessoaVO();
	}

	public DocumentoAssinadoPessoaVO montarDadosCompleto(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		DocumentoAssinadoPessoaVO obj = new DocumentoAssinadoPessoaVO();
		obj.setNovoObj(false);
		obj.setCodigo(new Integer(dadosSQL.getInt("documentoassinadopessoa.codigo")));
		obj.setOrdemAssinatura(new Integer(dadosSQL.getInt("documentoassinadopessoa.ordemAssinatura")));
		obj.setDataSolicitacao(dadosSQL.getTimestamp("documentoassinadopessoa.dataSolicitacao"));
		obj.setDataRejeicao(dadosSQL.getTimestamp("documentoassinadopessoa.dataRejeicao"));
		obj.setDataAssinatura(dadosSQL.getTimestamp("documentoassinadopessoa.dataAssinatura"));
		obj.setMotivoRejeicao(dadosSQL.getString("documentoassinadopessoa.motivoRejeicao"));
		obj.setCargo(dadosSQL.getString("documentoassinadopessoa.cargo"));
		obj.setTitulo(dadosSQL.getString("documentoassinadopessoa.titulo"));
		obj.setTipoPessoa(TipoPessoa.valueOf(dadosSQL.getString("documentoassinadopessoa.tipoPessoa")));
		obj.setSituacaoDocumentoAssinadoPessoaEnum(SituacaoDocumentoAssinadoPessoaEnum.valueOf(dadosSQL.getString("documentoassinadopessoa.situacaoDocumentoAssinadoPessoa")));
		obj.setTipoAssinaturaDocumentoEnum(TipoAssinaturaDocumentoEnum.valueOf(dadosSQL.getString("documentoassinadopessoa.tipoAssinaturaDocumentoEnum")));
		obj.setDocumentoAssinadoOrigemEnum(DocumentoAssinadoOrigemEnum.valueOf(dadosSQL.getString("documentoassinadopessoa.documentoAssinadoOrigemEnum")));
		obj.setJsonAssinatura(dadosSQL.getString("documentoassinadopessoa.jsonAssinatura"));
		obj.setCodigoAssinatura(dadosSQL.getString("documentoassinadopessoa.codigoAssinatura"));
		obj.setAcaoAssinatura(dadosSQL.getString("documentoassinadopessoa.acaoAssinatura"));
		obj.setUrlAssinatura(dadosSQL.getString("documentoassinadopessoa.urlAssinatura"));
		obj.setNomePessoa(dadosSQL.getString("documentoassinadopessoa.nomePessoa"));
		obj.setEmailPessoa(dadosSQL.getString("documentoassinadopessoa.emailPessoa"));
		obj.setCpfPessoa(dadosSQL.getString("documentoassinadopessoa.cpfPessoa"));
		obj.setAssinarPorCNPJ(dadosSQL.getBoolean("documentoassinadoPessoa.assinarPorCNPJ"));
		obj.setNome(dadosSQL.getString("documentoassinadoPessoa.nome"));
		obj.setProvedorAssinatura(dadosSQL.getString("documentoassinadopessoa.provedorAssinatura"));
		obj.getDataApresentar();
		obj.getPessoaVO().setCodigo(dadosSQL.getInt("pessoa.codigo"));
		obj.getPessoaVO().setNome(dadosSQL.getString("pessoa.nome"));
		obj.getPessoaVO().setCPF(dadosSQL.getString("pessoa.cpf"));
		obj.getPessoaVO().setSenhaCertificadoParaDocumento(dadosSQL.getString("pessoa.senhaCertificadoParaDocumento"));
		obj.getPessoaVO().setEmail(dadosSQL.getString("pessoa.email"));
		obj.getPessoaVO().setEmail2(dadosSQL.getString("pessoa.email2"));
		obj.getPessoaVO().setEndereco(dadosSQL.getString("pessoa.endereco"));
		obj.getPessoaVO().setNumero(dadosSQL.getString("pessoa.numero"));
		obj.getPessoaVO().setSetor(dadosSQL.getString("pessoa.setor"));
		obj.getPessoaVO().getCidade().setCodigo(dadosSQL.getInt("cidade.codigo"));
		obj.getPessoaVO().getCidade().setNome(dadosSQL.getString("cidade.nome"));
		obj.getPessoaVO().getCidade().getEstado().setCodigo(dadosSQL.getInt("estado.codigo"));
		obj.getPessoaVO().getCidade().getEstado().setNome(dadosSQL.getString("estado.nome"));
		obj.getPessoaVO().getCidade().getEstado().setSigla(dadosSQL.getString("estado.sigla"));

		return obj;
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
	 */
	public static String getIdEntidade() {
		return DocumentoAssinadoPessoa.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		DocumentoAssinadoPessoa.idEntidade = idEntidade;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirPorCodigoDocumentoAssinado(Integer codigo, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		excluir(getIdEntidade(), verificarAcesso, usuario);
		String sql = "DELETE FROM documentoassinadopessoa WHERE ((documentoassinado = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql,new Object[] { codigo});
	}
	
	@Override
	public DocumentoAssinadoPessoaVO consultarDocumentosAssinadoPessoaPorMatricula(String matriculaAluno) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select documentoassinadopessoa.* from documentoassinadopessoa ");
		sqlStr.append(" inner join  documentoassinado on documentoassinadopessoa.documentoassinado = documentoassinado.codigo ");
		sqlStr.append(" inner join matricula on matricula.matricula = documentoassinado.matricula ");
		sqlStr.append(" where documentoassinadopessoa.pessoa =  matricula.aluno and documentoassinado.matricula = ? and documentoassinadopessoa.tipoPessoa = 'ALUNO' ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] {matriculaAluno});
		if(tabelaResultado.next()) {
			DocumentoAssinadoPessoaVO obj = new DocumentoAssinadoPessoaVO();
			obj.setNovoObj(false);
			obj.setCodigo(new Integer(tabelaResultado.getInt("codigo")));
			obj.setOrdemAssinatura(new Integer(tabelaResultado.getInt("ordemAssinatura")));
			obj.setDataSolicitacao(tabelaResultado.getTimestamp("dataSolicitacao"));
			obj.setDataRejeicao(tabelaResultado.getTimestamp("dataRejeicao"));
			obj.setDataAssinatura(tabelaResultado.getTimestamp("dataAssinatura"));
			obj.setMotivoRejeicao(tabelaResultado.getString("motivoRejeicao"));
			obj.setCargo(tabelaResultado.getString("cargo"));
			obj.setTitulo(tabelaResultado.getString("titulo"));
			obj.setTipoPessoa(TipoPessoa.valueOf(tabelaResultado.getString("tipoPessoa")));
			obj.setSituacaoDocumentoAssinadoPessoaEnum(SituacaoDocumentoAssinadoPessoaEnum.valueOf(tabelaResultado.getString("situacaoDocumentoAssinadoPessoa")));
			obj.getDataApresentar();
			obj.getPessoaVO().setCodigo(tabelaResultado.getInt("pessoa"));
			
			return obj;
		}
		return new DocumentoAssinadoPessoaVO();
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void rejeitarContratoAssinadoPendenteAutomaticamente(final DocumentoAssinadoPessoaVO obj , UsuarioVO usuarioLogado) {
		try {
			obj.setDataRejeicao(new Date());
			obj.setSituacaoDocumentoAssinadoPessoaEnum(SituacaoDocumentoAssinadoPessoaEnum.REJEITADO);
			obj.setMotivoRejeicao("Rejeição através de Impressão de contrato pelo Usuario " + usuarioLogado.getNome());			
			atualizarDadosRejeicao(obj);
		} catch (Exception e) {
			obj.setDataRejeicao(null);
			obj.setSituacaoDocumentoAssinadoPessoaEnum(SituacaoDocumentoAssinadoPessoaEnum.PENDENTE);
			obj.setMotivoRejeicao(null);
			throw new StreamSeiException(e);
		}
		
	}
	
	@Override
	public DocumentoAssinadoPessoaVO consultarDocumentosAssinadoPessoaPorMatriculaMatriculaPeriodo(String matriculaAluno , Integer matriculaPeriodo) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select documentoassinadopessoa.* from documentoassinadopessoa ");
		sqlStr.append(" inner join  documentoassinado on documentoassinadopessoa.documentoassinado = documentoassinado.codigo ");
		sqlStr.append(" inner join matricula on matricula.matricula = documentoassinado.matricula ");
		sqlStr.append(" where documentoassinadopessoa.pessoa =  matricula.aluno and documentoassinado.matricula = ? and documentoassinado.matriculaperiodo = ? and documentoassinadopessoa.tipoPessoa = 'ALUNO'  and documentoassinado.tipoorigemdocumentoassinado ='CONTRATO'   ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] {matriculaAluno,matriculaPeriodo});
		if(tabelaResultado.next()) {
			DocumentoAssinadoPessoaVO obj = new DocumentoAssinadoPessoaVO();
			obj.setNovoObj(false);
			obj.setCodigo(new Integer(tabelaResultado.getInt("codigo")));
			obj.setOrdemAssinatura(new Integer(tabelaResultado.getInt("ordemAssinatura")));
			obj.setDataSolicitacao(tabelaResultado.getTimestamp("dataSolicitacao"));
			obj.setDataRejeicao(tabelaResultado.getTimestamp("dataRejeicao"));
			obj.setDataAssinatura(tabelaResultado.getTimestamp("dataAssinatura"));
			obj.setMotivoRejeicao(tabelaResultado.getString("motivoRejeicao"));
			obj.setCargo(tabelaResultado.getString("cargo"));
			obj.setTitulo(tabelaResultado.getString("titulo"));
			obj.setTipoPessoa(TipoPessoa.valueOf(tabelaResultado.getString("tipoPessoa")));
			obj.setSituacaoDocumentoAssinadoPessoaEnum(SituacaoDocumentoAssinadoPessoaEnum.valueOf(tabelaResultado.getString("situacaoDocumentoAssinadoPessoa")));
			obj.getDataApresentar();
			obj.setUrlAssinatura(tabelaResultado.getString("urlassinatura"));
			obj.getPessoaVO().setCodigo(tabelaResultado.getInt("pessoa"));
			
			return obj;
		}
		return new DocumentoAssinadoPessoaVO();
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void atualizarSituacaoPendenteDocumentoAssinadoAlunoParaRejeitado(final DocumentoAssinadoVO obj, final String motivo, UsuarioVO usuario) throws Exception {
		final String sql = "UPDATE DocumentoAssinadoPessoa set dataRejeicao=?, motivoRejeicao=?, situacaoDocumentoAssinadoPessoa=? WHERE documentoAssinado = ? and situacaoDocumentoAssinadoPessoa= ? and pessoa = ? " +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				sqlAlterar.setTimestamp(1, Uteis.getDataJDBCTimestamp(new Date()));
				sqlAlterar.setString(2, motivo);
				sqlAlterar.setString(3, SituacaoDocumentoAssinadoPessoaEnum.REJEITADO.name());
				sqlAlterar.setInt(4, obj.getCodigo());
				sqlAlterar.setString(5, SituacaoDocumentoAssinadoPessoaEnum.PENDENTE.name());
				sqlAlterar.setInt(6, usuario.getPessoa().getCodigo());
				return sqlAlterar;
			}
		});
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void atualizarDadosRejeicaoDocumentosAssinados(final DocumentoAssinadoPessoaVO obj, List<DocumentoAssinadoVO> documentoAssinadoVOs) throws Exception {
		StringBuilder sb = new StringBuilder("UPDATE DocumentoAssinadoPessoa set dataRejeicao=?, motivoRejeicao=?, situacaoDocumentoAssinadoPessoa=? ");
		if (Uteis.isAtributoPreenchido(documentoAssinadoVOs)) {
			sb.append(documentoAssinadoVOs.stream().map(d -> d.getCodigo().toString()).collect(Collectors.joining(", ", " WHERE DocumentoAssinadoPessoa.documentoassinado IN (", ") ")));
		}
		sb.append("AND DocumentoAssinadoPessoa.situacaoDocumentoAssinadoPessoa = 'PENDENTE' ");	
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sb.toString());
				sqlAlterar.setTimestamp(1, Uteis.getDataJDBCTimestamp(obj.getDataRejeicao()));
				sqlAlterar.setString(2, obj.getMotivoRejeicao());
				sqlAlterar.setString(3, obj.getSituacaoDocumentoAssinadoPessoaEnum().name());
				return sqlAlterar;
			}
		});
	}	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void realizarRejeicaoDocumentosAssinados(ExpedicaoDiplomaVO expedicaoDiplomaVO, Boolean apresentarMensagemAssinaturaDigital, UsuarioVO usuarioVO) throws Exception {
		if (expedicaoDiplomaVO.getNovaGeracaoRepresentacaoVisualDiplomaDigital()) {
			return;
		}
		DocumentoAssinadoPessoaVO documentoAssinadoPessoaVO = new DocumentoAssinadoPessoaVO();
		documentoAssinadoPessoaVO.setDataRejeicao(new Date());
		Uteis.checkState(apresentarMensagemAssinaturaDigital && !Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getMotivoRejeicaoDiplomaDigital()), "O Campo Motivo Rejeição Documento Assinado Deve Ser Informado.");
		documentoAssinadoPessoaVO.setMotivoRejeicao(expedicaoDiplomaVO.getMotivoRejeicaoDiplomaDigital());
		documentoAssinadoPessoaVO.setSituacaoDocumentoAssinadoPessoaEnum(SituacaoDocumentoAssinadoPessoaEnum.REJEITADO);
		List<DocumentoAssinadoVO> documentoAssinadoVOs = new ArrayList<DocumentoAssinadoVO>();
		DocumentoAssinadoVO obj = new DocumentoAssinadoVO();
		obj.setExpedicaoDiplomaVO(expedicaoDiplomaVO);
		obj.setMatricula(expedicaoDiplomaVO.getMatricula());
		List<TipoOrigemDocumentoAssinadoEnum> tipoOrigemDocumentoAssinadoEnums = new ArrayList<TipoOrigemDocumentoAssinadoEnum>();
		tipoOrigemDocumentoAssinadoEnums.add(TipoOrigemDocumentoAssinadoEnum.DIPLOMA_DIGITAL);
		documentoAssinadoVOs.addAll(getFacadeFactory().getDocumentoAssinadoFacade().consultarDocumentosAssinadoPorRelatorio(obj, false, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO, tipoOrigemDocumentoAssinadoEnums, null));
		if (Uteis.isAtributoPreenchido(documentoAssinadoVOs)) {
			atualizarDadosRejeicaoDocumentosAssinados(documentoAssinadoPessoaVO, documentoAssinadoVOs);
			for (DocumentoAssinadoVO documentoAssinadoVO : documentoAssinadoVOs) {
				getFacadeFactory().getDocumentoAssinadoFacade().atualizarDocumentoAssinadoInvalido(documentoAssinadoVO.getCodigo(), Boolean.TRUE, expedicaoDiplomaVO.getMotivoRejeicaoDiplomaDigital(), usuarioVO);
			}
		}
	}
	
	@Override
	public void realizarRejeicaoDocumentosAssinados( UsuarioVO usuarioVO) throws Exception {
		List<DocumentoAssinadoVO> documentoAssinadoVOs = new ArrayList<DocumentoAssinadoVO>();
		DocumentoAssinadoVO obj = new DocumentoAssinadoVO();
//		obj.setCursoVO(gestaoXmlGradeCurricularVO.getCursoVO());
//		obj.setUnidadeEnsinoVO(gestaoXmlGradeCurricularVO.getUnidadeEnsinoVO());
//		obj.setGradecurricular(gestaoXmlGradeCurricularVO.getGradeCurricularVO());
		List<TipoOrigemDocumentoAssinadoEnum> tipoOrigemDocumentoAssinadoEnums = new ArrayList<TipoOrigemDocumentoAssinadoEnum>();
		tipoOrigemDocumentoAssinadoEnums.add(TipoOrigemDocumentoAssinadoEnum.CURRICULO_ESCOLAR_DIGITAL);
		documentoAssinadoVOs.addAll(getFacadeFactory().getDocumentoAssinadoFacade().consultarDocumentosAssinadoPorRelatorio(obj, false, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO, tipoOrigemDocumentoAssinadoEnums, null));
		if (Uteis.isAtributoPreenchido(documentoAssinadoVOs)) {
//			Uteis.checkState(!Uteis.isAtributoPreenchido(gestaoXmlGradeCurricularVO.getMotivoRejeicao()), "O Campo Motivo Rejeição Documento Assinado Deve Ser Informado.");
			DocumentoAssinadoPessoaVO documentoAssinadoPessoaVO = new DocumentoAssinadoPessoaVO();
			documentoAssinadoPessoaVO.setDataRejeicao(new Date());
//			documentoAssinadoPessoaVO.setMotivoRejeicao(gestaoXmlGradeCurricularVO.getMotivoRejeicao());
			documentoAssinadoPessoaVO.setSituacaoDocumentoAssinadoPessoaEnum(SituacaoDocumentoAssinadoPessoaEnum.REJEITADO);
			if (Uteis.isAtributoPreenchido(documentoAssinadoVOs)) {
				getFacadeFactory().getDocumentoAssinadoPessoaFacade().atualizarDadosRejeicaoDocumentosAssinados(documentoAssinadoPessoaVO, documentoAssinadoVOs);
//				for (DocumentoAssinadoVO documentoAssinadoVO : documentoAssinadoVOs) {
//					getFacadeFactory().getDocumentoAssinadoFacade().atualizarDocumentoAssinadoInvalido(documentoAssinadoVO.getCodigo(), Boolean.TRUE,  usuarioVO);
//				}
			}
		}
	}

	@Override
	public void processarTrocaResponsavelAssinaturaDocumentoAssinado(DocumentoAssinadoPessoaVO obj, ProvedorDeAssinaturaEnum provedorAssintura, PessoaVO novaPessoa, Integer codigoUnidadeEnsino, DocumentoAssinadoVO documentoAssinadoVO, UsuarioVO usuarioVO) throws Exception {
		validarDadosTrocaResponsavelAssinaturaDocumento(obj, novaPessoa, documentoAssinadoVO);
		if (provedorAssintura.isProvedorSei()) {
			persistirTrocaResponsavelAssinatura(obj, novaPessoa, obj.getPessoaVO(), usuarioVO);
		} else if (provedorAssintura.isProvedorCertisign()) {
			realizarTrocaResponsavelAssinaturaProvedorCertisign(obj, codigoUnidadeEnsino, documentoAssinadoVO.getCodigoProvedorDeAssinatura(), novaPessoa, usuarioVO);
		} else if (provedorAssintura.isProvedorTechCert()) {
			realizarTrocaResponsavelAssinaturaProvedorTechCert(obj, codigoUnidadeEnsino, documentoAssinadoVO, novaPessoa, usuarioVO);
		}
	}

	private void validarDadosTrocaResponsavelAssinaturaDocumento(DocumentoAssinadoPessoaVO dap, PessoaVO novaPessoa, DocumentoAssinadoVO documentoAssinadoVO) throws Exception {
		if (dap.getSituacaoDocumentoAssinadoPessoaEnum().isAssinado()) {
			throw new Exception("Não é possível realizar a troca de responsável de assinatura para um documento que já tenha assinaturas realizadas.");
		}
		if (dap.getTipoPessoa().isMembroComunidade()) {
			throw new Exception("Não é possível realizar a troca de responsável de assinatura para um documento que tenha membros da comunidade como responsáveis.");
		}
		if (dap.getPessoaVO().getCodigo().equals(novaPessoa)) {
			throw new Exception("Não é possível realizar a troca de responsável de assinatura para um documento que já tenha o novo responsável como responsável atual.");
		}
		if (Uteis.removerMascara(dap.getPessoaVO().getCPF()).equals(Uteis.removerMascara(novaPessoa.getCPF()))
				|| Uteis.removerMascara(dap.getCpfPessoa()).equals(Uteis.removerMascara(novaPessoa.getCPF()))) {
			throw new Exception("Não é possivel realizar a troca de assinantes com mesmos CPF.");
		}
		for (DocumentoAssinadoPessoaVO pessoa : documentoAssinadoVO.getListaDocumentoAssinadoPessoa()) {
			if (Uteis.removerMascara(pessoa.getPessoaVO().getCPF()).equals(Uteis.removerMascara(novaPessoa.getCPF()))
					|| Uteis.removerMascara(pessoa.getCpfPessoa()).equals(Uteis.removerMascara(novaPessoa.getCPF()))) {
				throw new Exception("Não é possivel realizar a troca de assinantes com mesmos CPF na fila de ordem de assinaturas.");
			}
			if (Uteis.removerMascara(pessoa.getPessoaVO().getEmail()).equals(Uteis.removerMascara(novaPessoa.getEmail()))
					|| Uteis.removerMascara(pessoa.getEmailPessoa()).equals(Uteis.removerMascara(novaPessoa.getEmail()))) {
				throw new Exception("Não é possivel realizar a troca de assinantes com mesmos CPF na fila de ordem de assinaturas.");
			}
		}
	}

	private void persistirTrocaResponsavelAssinatura(DocumentoAssinadoPessoaVO obj, PessoaVO novaPessoa, PessoaVO pessoaAntiga, UsuarioVO usuarioVO) {
		alterar(obj, "documentoassinadopessoa", new AtributoPersistencia()
						.add("pessoa", novaPessoa.getCodigo())
						.add("datasolicitacao", Uteis.getDataJDBCTimestamp(new Date()))
						.add("codigoassinatura", obj.getCodigoAssinatura())
				, new AtributoPersistencia().add("codigo", obj.getCodigo()).add("pessoa", pessoaAntiga.getCodigo()), usuarioVO);
	}

	private void persistirTrocaResponsavelAssinaturaTechCert(DocumentoAssinadoPessoaVO obj, PessoaVO novaPessoa, PessoaVO pessoaAntiga, UsuarioVO usuarioVO) {
		alterar(obj, "documentoassinadopessoa", new AtributoPersistencia()
						.add("pessoa", novaPessoa.getCodigo())
						.add("nomepessoa", novaPessoa.getNome())
						.add("emailpessoa", novaPessoa.getEmail())
						.add("cpfpessoa", Uteis.removerMascara(novaPessoa.getCPF()))
						.add("urlassinatura", "")
						.add("urlprovedordeassinatura", "")
						.add("datasolicitacao", Uteis.getDataJDBCTimestamp(new Date()))
						.add("codigoassinatura", obj.getCodigoAssinatura())
				, new AtributoPersistencia().add("codigo", obj.getCodigo()).add("pessoa", pessoaAntiga.getCodigo()), usuarioVO);
	}

	private void persistirTrocaResponsavelConcedenteAssinaturaTechCert(DocumentoAssinadoPessoaVO obj, PessoaVO novaPessoa, PessoaVO pessoaAntiga, UsuarioVO usuarioVO) {
		alterar(obj, "documentoassinadopessoa", new AtributoPersistencia()
						.add("nomepessoa", novaPessoa.getNome())
						.add("emailpessoa", novaPessoa.getEmail())
						.add("cpfpessoa", Uteis.removerMascara(novaPessoa.getCPF()))
						.add("urlassinatura", "")
						.add("urlprovedordeassinatura", "")
						.add("datasolicitacao", Uteis.getDataJDBCTimestamp(new Date()))
						.add("codigoassinatura", obj.getCodigoAssinatura())
				, new AtributoPersistencia().add("codigo", obj.getCodigo()), usuarioVO);
	}

	@Override
	public void reordenarAssinatesDocumentoAssinado(DocumentoAssinadoVO documentoAssinadoVO, UsuarioVO usuarioVO, ConfiguracaoGEDVO configuracaoGEDVO) {
		DocumentsTechCertVO documentsTechCertVO = getFacadeFactory().getDocumentoAssinadoFacade().consultarDocumet(configuracaoGEDVO, documentoAssinadoVO.getChaveProvedorDeAssinatura());
		for (DocumentoAssinadoPessoaVO dcp : documentoAssinadoVO.getListaDocumentoAssinadoPessoa()) {
			String nome = Uteis.isAtributoPreenchido(dcp.getPessoaVO().getNome()) ? Uteis.removerAcentos(Uteis.removeCaractersEspeciais(dcp.getPessoaVO().getNome())).replace(" ", "") : Uteis.removerAcentos(Uteis.removeCaractersEspeciais(dcp.getNomePessoa())).replace(" ", "");
			String cpf = Uteis.isAtributoPreenchido(dcp.getPessoaVO().getCPF()) ? Uteis.removeCaractersEspeciais(Uteis.removeCaractersEspeciais(dcp.getPessoaVO().getCPF())).replace(" ", "") : Uteis.removerAcentos(Uteis.removeCaractersEspeciais(dcp.getCpfPessoa())).replace(" ", "");
			for (FlowActionsTechCertVO flow : documentsTechCertVO.getFlowActions()) {
				//===============SIGNER===============
				if (flow.getUser() != null && Uteis.isAtributoPreenchido(flow.getUser().getId())) {
					String userAtual = Uteis.removerAcentos(Uteis.removeCaractersEspeciais(flow.getUser().getName())).replace(" ", "");
					String identifier = Uteis.removerAcentos(Uteis.removeCaractersEspeciais(flow.getUser().getIdentifier())).replace(" ", "");
					Map<String, Boolean> map = validarDocumentoAssinadoPessoaResponsavelPelaAssinatura(nome, cpf, userAtual, identifier);
					if (StatusFlowActionsTechCertEnum.PENDING.getNome().equalsIgnoreCase(flow.getStatus())
							|| StatusFlowActionsTechCertEnum.CREATED.getNome().equalsIgnoreCase(flow.getStatus())) {
						if (map.get(PESSOA_RESPONSAVEL_ASSINATURA)) {
							posRemocaoAssinanteAtualizarOrdem(dcp, flow.getStep(), usuarioVO);
						}
					}
				}
			}
		}
	}

	private void posRemocaoAssinanteAtualizarOrdem(DocumentoAssinadoPessoaVO documentoAssinadoPessoaVO, Integer step, UsuarioVO usuarioVO) {
		alterar(documentoAssinadoPessoaVO, "documentoassinadopessoa", new AtributoPersistencia()
						.add("ordemassinatura", step),
				new AtributoPersistencia().add("codigo", documentoAssinadoPessoaVO.getCodigo()), usuarioVO);
	}

	private void realizarTrocaResponsavelAssinaturaProvedorCertisign(DocumentoAssinadoPessoaVO doc, Integer unidadeEnsino, String codigoProvedorAssinatura, PessoaVO novaPessoa, UsuarioVO usuarioVO) {
		try {
			ConfiguracaoGEDVO configGEDVO = getAplicacaoControle().getConfiguracaoGEDPorUnidadeEnsino(unidadeEnsino, usuarioVO);
			getFacadeFactory().getDocumentoAssinadoFacade().realizarExclusaoParticipantDiscard(doc, configGEDVO);
			realizarInclusaoNovoAssinanteCertiSign(doc, codigoProvedorAssinatura, novaPessoa, configGEDVO, usuarioVO);
		} catch (Exception e) {
			throw new StreamSeiException(e.getMessage());
		}
	}

	private void realizarInclusaoNovoAssinanteCertiSign(DocumentoAssinadoPessoaVO obj, String codigoProvedorAssinatura, PessoaVO novaPessoa, ConfiguracaoGEDVO configGEDVO, UsuarioVO usuarioVO) throws Exception {
		CertiSignRSVO csRSVO = new CertiSignRSVO();
		csRSVO.setDocumentId(new Integer(codigoProvedorAssinatura));
		List<DocumentPessoaRSVO> listaSigners = new ArrayList<>();
		DocumentPessoaRSVO dpRSVO = new DocumentPessoaRSVO();
		dpRSVO.setName(novaPessoa.getNome());
		dpRSVO.setEmail(novaPessoa.getEmail());
		dpRSVO.setIndividualIdentificationCode(novaPessoa.getCPF());
		dpRSVO.setStep(0);
		dpRSVO.setTitle("Assinatura");
		listaSigners.add(dpRSVO);
		csRSVO.setSigners(listaSigners);
		CertiSignRSVO csRetorno = getFacadeFactory().getDocumentoAssinadoFacade().realizarEnvioParticipantAdd(csRSVO, obj, configGEDVO);

		if(csRetorno.getSigners() != null && !csRetorno.getSigners().isEmpty()) {
			for (DocumentPessoaRSVO docPessoaRsVO : csRetorno.getSigners()) {
				obj.setCodigoAssinatura(docPessoaRsVO.getSignerId().toString());
			}
		}
		if(csRetorno.getElectronicSigners() != null && !csRetorno.getElectronicSigners().isEmpty()) {
			for (DocumentPessoaRSVO docPessoaRsVO : csRetorno.getElectronicSigners()) {
				obj.setCodigoAssinatura(docPessoaRsVO.getSignerId().toString());
			}
		}

		persistirTrocaResponsavelAssinatura(obj, novaPessoa, obj.getPessoaVO(), usuarioVO);
	}

	/**
	 * Apos testes a techCert só realiza troca
	 * de assinantes que não concluiram a assinatura
	 * @param docPessoa
	 * @param unidadeEnsino
	 * @param documentoAssinadoVO
	 * @param novaPessoa
	 * @param usuarioVO
	 */
	private void realizarTrocaResponsavelAssinaturaProvedorTechCert(DocumentoAssinadoPessoaVO docPessoa, Integer unidadeEnsino, DocumentoAssinadoVO documentoAssinadoVO, PessoaVO novaPessoa, UsuarioVO usuarioVO) {
		try {
			ConfiguracaoGEDVO configGEDVO = getAplicacaoControle().getConfiguracaoGEDPorUnidadeEnsino(unidadeEnsino, usuarioVO);
			realizarTrocaDeAssinanteTechCert(docPessoa, documentoAssinadoVO, novaPessoa, configGEDVO, usuarioVO);
		} catch (Exception e) {
			throw new StreamSeiException(e.getMessage());
		}
	}

	@Override
	public void realizarTrocaDeAssinanteTechCert(DocumentoAssinadoPessoaVO documentoAssinadoPessoa, DocumentoAssinadoVO documentoAssinadoVO, PessoaVO novaPessoa, ConfiguracaoGEDVO configGEDVO, UsuarioVO usuarioVO) throws Exception {
		getFacadeFactory().getDocumentoAssinadoFacade().validarCpfEmailParaAssinatura(novaPessoa.getEmail(), novaPessoa.getCPF(), novaPessoa.getNome());
		int stepFinalDaFila = documentoAssinadoVO.getListaDocumentoAssinadoPessoa()
				.stream()
				.map(DocumentoAssinadoPessoaVO::getOrdemAssinatura)
				.max(Integer::compareTo)
				.orElse(0);

		Boolean allowElectronicSignature =
				TipoProvedorAssinaturaEnum.ASSINATURA_ELETRONICA == configGEDVO.getTipoProvedorAssinaturaTechCertEnum();

		AddedFlowActionTechCertVO addFlow = new AddedFlowActionTechCertVO();
		addFlow.setType("Signer");
		addFlow.setAllowElectronicSignature(allowElectronicSignature);
		addFlow.setStep(stepFinalDaFila);

		UserTechCertVO user = new UserTechCertVO();
		user.setName(novaPessoa.getNome());
		user.setIdentifier(Uteis.removerMascara(novaPessoa.getCPF()));
		user.setEmail(novaPessoa.getEmail());
		addFlow.setUser(user);
		getFacadeFactory().getDocumentoAssinadoFacade().permissoesDocumentsFlowsTechCert(addFlow);
		addFlow.setNumberRequiredSignatures(null);
		// Realiza a atualização dos assinantes
		getFacadeFactory().getDocumentoAssinadoFacade()
				.realizarAlteracaoParticipanteTechCert(documentoAssinadoPessoa, documentoAssinadoVO.getChaveProvedorDeAssinatura(), addFlow, configGEDVO);

		//A consulta é realizada pois o flowaction id não foi retornado apos a troca do novo assinante
		DocumentsTechCertVO documentsTechCertVO = getFacadeFactory().getDocumentoAssinadoFacade().consultarDocumet(configGEDVO, documentoAssinadoVO.getChaveProvedorDeAssinatura());
//		String nome = Uteis.removerAcentos(Uteis.removeCaractersEspeciais(novaPessoa.getNome())).replace(" ", "");
//		String cpf = Uteis.removerAcentos(Uteis.removeCaractersEspeciais(novaPessoa.getCPF())).replace(" ", "");
		for (FlowActionsTechCertVO flow : documentsTechCertVO.getFlowActions()) {
			//===============SIGNER===============
			if (flow.getUser() != null && Uteis.isAtributoPreenchido(flow.getUser().getId())) {
//				String userAtual = Uteis.removerAcentos(Uteis.removeCaractersEspeciais(flow.getUser().getName()).replace(" ", ""));
//				String identifier = Uteis.removerAcentos(Uteis.removeCaractersEspeciais(flow.getUser().getIdentifier()).replace(" ", ""));
//				Map<String, Boolean> map = validarDocumentoAssinadoPessoaResponsavelPelaAssinatura(nome, cpf, userAtual, identifier);
//				if (map.get(PESSOA_RESPONSAVEL_ASSINATURA)) {
//
//				}
				if (novaPessoa.getEmail().equalsIgnoreCase(flow.getUser().getEmail())){
					documentoAssinadoPessoa.setCodigoAssinatura(flow.getId());
				}
			}
		}

		Set<String> idsConcluidos = documentsTechCertVO.getFlowActions().stream()
				.filter(flow -> StatusFlowActionsTechCertEnum.COMPLETED.getNome().equalsIgnoreCase(flow.getStatus()))
				.map(flow -> flow.getUser() != null ? flow.getUser().getId() : null)
				.filter(Objects::nonNull)
				.collect(Collectors.toSet());
		Map<Integer, String> ordemFlows = new TreeMap<>();

		// filtra os assinantes pendentes com o assinante trocado
		List<DocumentoAssinadoPessoaVO> novaListaAssinantesPendentes = documentoAssinadoVO.getListaDocumentoAssinadoPessoa()
				.stream()
				.map(docPessoaAtual ->
						docPessoaAtual.getCodigo().equals(documentoAssinadoPessoa.getCodigo())
								? documentoAssinadoPessoa
								: docPessoaAtual)
				.filter(docPessoa ->
						SituacaoDocumentoAssinadoPessoaEnum.PENDENTE == docPessoa.getSituacaoDocumentoAssinadoPessoaEnum()).collect(Collectors.toList());

		for (DocumentoAssinadoPessoaVO docPessoaAtual : novaListaAssinantesPendentes) {
			ordemFlows.put(docPessoaAtual.getOrdemAssinatura(), docPessoaAtual.getCodigoAssinatura());
		}

		ordemFlows.values().removeIf(idsConcluidos::contains);

		getFacadeFactory().getDocumentoAssinadoFacade().realizarAlteracaoStepFlowActionTechCert(configGEDVO, documentoAssinadoVO.getChaveProvedorDeAssinatura(), ordemFlows);
		if (TipoPessoa.MEMBRO_COMUNIDADE.getValor().equalsIgnoreCase(documentoAssinadoPessoa.getTipoPessoa().getValor())) {
			persistirTrocaResponsavelConcedenteAssinaturaTechCert(documentoAssinadoPessoa, novaPessoa, documentoAssinadoPessoa.getPessoaVO(), usuarioVO);
		} else {
			persistirTrocaResponsavelAssinaturaTechCert(documentoAssinadoPessoa, novaPessoa, documentoAssinadoPessoa.getPessoaVO(), usuarioVO);
		}
		getFacadeFactory().getDocumentoAssinadoFacade().executarProcessamentoTechCertAntigoPosTrocaAssinante(documentoAssinadoVO.getChaveProvedorDeAssinatura(), usuarioVO, configGEDVO, DocumentoAssinadoOrigemEnum.TECHCERT_SEI);
	}

	@Override
	public void realizarTrocaResponsavelAssinaturaDocumento(DocumentoAssinadoVO documentoAssinadoVO, PessoaVO novaPessoa, Integer codigoDocumentoAssinadoPessoaAlteracao, ConfiguracaoGeralSistemaVO configSistemaVO, UsuarioVO usuarioVO) {
		try {
			for (DocumentoAssinadoPessoaVO dap : documentoAssinadoVO.getListaDocumentoAssinadoPessoa()) {
				if (dap.getCodigo().equals(codigoDocumentoAssinadoPessoaAlteracao) && dap.getSituacaoDocumentoAssinadoPessoaEnum().isPendente()) {
					processarTrocaResponsavelAssinaturaDocumentoAssinado(dap, documentoAssinadoVO.getProvedorDeAssinaturaEnum(), novaPessoa, documentoAssinadoVO.getUnidadeEnsinoVO().getCodigo(), documentoAssinadoVO, usuarioVO);
				}
			}
		} catch (Exception e) {
			throw new StreamSeiException(e.getMessage());
		}
	}

	private Map<String, Boolean> validarDocumentoAssinadoPessoaResponsavelPelaAssinatura(String nomePessoaValidar, String cpfPessoaValidar, String nomePessoaAssinatura, String cpfPessoaAssinatura) {
		Map<String, Boolean> map = new HashMap<>(0);
		map.put(PESSOA_RESPONSAVEL_ASSINATURA, Boolean.FALSE);
		map.put(CPF_PESSOA_DIFERENTE_ASSINATURA, Boolean.FALSE);
		if (Uteis.isAtributoPreenchido(cpfPessoaValidar) && Uteis.isAtributoPreenchido(cpfPessoaAssinatura) && Objects.equals(cpfPessoaValidar, cpfPessoaAssinatura)
				&& Uteis.isAtributoPreenchido(nomePessoaValidar) && Uteis.isAtributoPreenchido(nomePessoaAssinatura) && Objects.equals(nomePessoaValidar, nomePessoaAssinatura)) {
			map.replace(PESSOA_RESPONSAVEL_ASSINATURA, Boolean.TRUE);
		} else if (Uteis.isAtributoPreenchido(cpfPessoaValidar) && Uteis.isAtributoPreenchido(cpfPessoaAssinatura) && Objects.equals(cpfPessoaValidar, cpfPessoaAssinatura)) {
			map.replace(PESSOA_RESPONSAVEL_ASSINATURA, Boolean.TRUE);
		} else if (Uteis.isAtributoPreenchido(nomePessoaValidar) && Uteis.isAtributoPreenchido(nomePessoaAssinatura) && Objects.equals(nomePessoaValidar, nomePessoaAssinatura)) {
			map.replace(PESSOA_RESPONSAVEL_ASSINATURA, Boolean.TRUE);
			if (Uteis.isAtributoPreenchido(cpfPessoaValidar) && Uteis.isAtributoPreenchido(cpfPessoaAssinatura) && !Objects.equals(cpfPessoaValidar, cpfPessoaAssinatura)) {
				map.replace(CPF_PESSOA_DIFERENTE_ASSINATURA, Boolean.TRUE);
			}
		}
		return map;
	}
}
