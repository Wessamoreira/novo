package negocio.facade.jdbc.estagio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.DocumentoAssinadoPessoaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.AtivoInativoEnum;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.estagio.ConcedenteVO;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.administrativo.FormacaoAcademica;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.estagio.ConcedenteInterfaceFacade;


@Repository
@Scope("singleton")
@Lazy
public class Concedente extends ControleAcesso implements ConcedenteInterfaceFacade{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9072443567573312250L;
	private static String idEntidade = "Concedente";

	public static String getIdEntidade() {
		return Concedente.idEntidade;
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void valiarDados(ConcedenteVO obj) {
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getTipoConcedenteVO()), UteisJSF.internacionalizar("msg_Concedente_tipoConcedente"));
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getConcedente()), UteisJSF.internacionalizar("msg_Concedente_concedente"));
		Uteis.checkState(obj.getTipoConcedenteVO().isCnpjObrigatorio() && !Uteis.isAtributoPreenchido(obj.getCnpj()), UteisJSF.internacionalizar("msg_Concedente_cnpj"));
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getTelefone()), UteisJSF.internacionalizar("msg_Concedente_telefone"));
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getCep()), UteisJSF.internacionalizar("msg_Concedente_cep"));
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getEndereco()), UteisJSF.internacionalizar("msg_Concedente_endereco"));
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getBairro()), UteisJSF.internacionalizar("msg_Concedente_bairro"));
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getCidade()), UteisJSF.internacionalizar("msg_Concedente_cidade"));
		Uteis.checkState(Uteis.isAtributoPreenchido(obj.getEmailResponsavelConcedente()) && !Uteis.getValidaEmail(obj.getEmailResponsavelConcedente()), UteisJSF.internacionalizar("msg_Concedente_emailValido"));
		if((Uteis.isAtributoPreenchido(obj.getCnpj()) || obj.getTipoConcedenteVO().isCnpjObrigatorio()) && validarUnicidade(obj)) {
			StringBuilder sb = new StringBuilder("Já existe o Concedente com ");
			if(Uteis.isAtributoPreenchido(obj.getCnpj()) || obj.getTipoConcedenteVO().isCnpjObrigatorio()) {
				sb.append(" o CNPJ ").append(obj.getCnpj()).append(" informado.");
			}
			Uteis.checkState(true, sb.toString());
		}
		if(obj.getTipoConcedenteVO().getCodigoMECObrigatorio()) {
			Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getCodigoEscolaMEC()), UteisJSF.internacionalizar("O campo Código da Escola no MEC deve ser informado."));
		}
		
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(ConcedenteVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) {
		try {
			valiarDados(obj);
			if (obj.getCodigo() == 0) {
				incluir(obj, verificarAcesso, usuarioVO);
			} else {
				alterar(obj, verificarAcesso, usuarioVO);
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ConcedenteVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			incluir(getIdEntidade(), verificarAcesso, usuario);
			incluir(obj, "concedente", new AtributoPersistencia()
					.add("tipoConcedente", obj.getTipoConcedenteVO())
					.add("cnpj", obj.getCnpj()) 
					.add("concedente", obj.getConcedente()) 
					.add("telefone", obj.getTelefone()) 
					.add("cep", obj.getCep()) 
					.add("endereco", obj.getEndereco()) 
					.add("numero", obj.getNumero()) 
					.add("bairro", obj.getBairro()) 
					.add("cidade", obj.getCidade())
					.add("responsavelConcedente", obj.getResponsavelConcedente()) 
					.add("cpfResponsavelConcedente", obj.getCpfResponsavelConcedente()) 
					.add("emailResponsavelConcedente", obj.getEmailResponsavelConcedente()) 
					.add("telefoneResponsavelConcedente", obj.getTelefoneResponsavelConcedente())
					.add("codigoEscolaMEC", obj.getCodigoEscolaMEC())
					.add("situacao", obj.getSituacao())
					,usuario);
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw new StreamSeiException(e);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ConcedenteVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			alterar(getIdEntidade(), verificarAcesso, usuario);
			alterar(obj, "concedente", new AtributoPersistencia()
					.add("tipoConcedente", obj.getTipoConcedenteVO())
					.add("cnpj", obj.getCnpj()) 
					.add("concedente", obj.getConcedente()) 
					.add("telefone", obj.getTelefone()) 
					.add("cep", obj.getCep()) 
					.add("endereco", obj.getEndereco()) 
					.add("numero", obj.getNumero()) 
					.add("bairro", obj.getBairro()) 
					.add("cidade", obj.getCidade())
					.add("responsavelConcedente", obj.getResponsavelConcedente()) 
					.add("cpfResponsavelConcedente", obj.getCpfResponsavelConcedente()) 
					.add("emailResponsavelConcedente", obj.getEmailResponsavelConcedente()) 
					.add("telefoneResponsavelConcedente", obj.getTelefoneResponsavelConcedente())
					.add("codigoEscolaMEC", obj.getCodigoEscolaMEC())
					.add("situacao", obj.getSituacao())
					, new AtributoPersistencia().add("codigo", obj.getCodigo()), usuario);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(ConcedenteVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			excluir(getIdEntidade(), verificarAcesso, usuario);
			getConexao().getJdbcTemplate().update("DELETE FROM concedente WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario), obj.getCodigo());
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void consultar(DataModelo dataModelo, ConcedenteVO obj) throws Exception {
		dataModelo.getListaConsulta().clear();
		dataModelo.getListaFiltros().clear();
		dataModelo.setListaConsulta(consultaRapidaPorFiltros(obj, dataModelo));
	}

	private List<ConcedenteVO> consultaRapidaPorFiltros(ConcedenteVO obj, DataModelo dataModelo) {
		try {
			consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" WHERE 1=1 ");
			montarFiltrosParaConsulta(obj, dataModelo, sqlStr);
			sqlStr.append(" ORDER BY concedente.codigo desc ");
			UteisTexto.addLimitAndOffset(sqlStr, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), dataModelo.getListaFiltros().toArray());
			montarTotalizadorConsultaBasica(dataModelo, tabelaResultado);
			return montarDadosConsulta(tabelaResultado, dataModelo.getNivelMontarDados(), dataModelo.getUsuario());
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	private void montarFiltrosParaConsulta(ConcedenteVO obj, DataModelo dataModelo, StringBuilder sqlStr) {
		if (Uteis.isAtributoPreenchido(obj.getCodigo())) {
			sqlStr.append(" and concedente.codigo = ? ");
			dataModelo.getListaFiltros().add(obj.getCodigo());
		}
		if (Uteis.isAtributoPreenchido(obj.getConcedente())) {
			sqlStr.append(" and lower(sem_acentos(concedente.concedente)) like(lower(sem_acentos(?)))");
			dataModelo.getListaFiltros().add(PERCENT + obj.getConcedente().toLowerCase() + PERCENT);
		}
		if (Uteis.isAtributoPreenchido(obj.getCnpj())) {
			sqlStr.append(" and lower(sem_acentos(concedente.cnpj)) like(lower(sem_acentos(?)))");
			dataModelo.getListaFiltros().add(PERCENT + obj.getCnpj().toLowerCase() + PERCENT);
		}
		
		if (Uteis.isAtributoPreenchido(obj.getTipoConcedenteVO())) {
			sqlStr.append(" and concedente.tipoConcedente = ? ");
			dataModelo.getListaFiltros().add(obj.getTipoConcedenteVO(). getCodigo());
		}
		if (Uteis.isAtributoPreenchido(obj.getSituacaoConsultar())) {
			sqlStr.append(" and situacao ='").append(obj.getSituacaoConsultar()).append("' ");
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public ConcedenteVO consultarPorCnpj(String cnpj, Integer tipoConcedente, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) {
		try {
			consultar(getIdEntidade(), controlarAcesso, usuario);
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" where replace(replace(replace(concedente.cnpj,'.',''),'-',''), '/', '') = ? ");
			if(Uteis.isAtributoPreenchido(tipoConcedente)) {
				sqlStr.append(" and tipoConcedente.codigo = ").append(tipoConcedente);
			}
			sqlStr.append(" and situacao = 'ATIVO' ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), Uteis.retirarMascaraCNPJ(cnpj));
			if (!tabelaResultado.next()) {
				return new ConcedenteVO();
			}
			return (montarDados(tabelaResultado, nivelMontarDados, usuario));
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public ConcedenteVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) {
		try {
			consultar(getIdEntidade(), controlarAcesso, usuario);
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" where concedente.codigo = ? ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), codigoPrm);
			if (!tabelaResultado.next()) {
				throw new StreamSeiException("Dados Não Encontrados ( ConcedenteVO ).");
			}
			return (montarDados(tabelaResultado, nivelMontarDados, usuario));
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	private Boolean validarUnicidade(ConcedenteVO obj) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT codigo FROM concedente ");
		sql.append(" WHERE 1=1 ");
		sql.append(" and cnpj= '").append(obj.getCnpj()).append("'");
		if (Uteis.isAtributoPreenchido(obj.getCodigo())) {
			sql.append(" and codigo != ").append(obj.getCodigo()).append(" ");
		}
		return getConexao().getJdbcTemplate().queryForRowSet(sql.toString()).next();
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private StringBuilder getSQLPadraoConsultaBasica() {
		StringBuilder sql = new StringBuilder(getSelectTotalizadorConsultaBasica());
		sql.append(" concedente.codigo, concedente.cnpj, concedente.concedente,  concedente.telefone,  ");
		sql.append(" concedente.cep, concedente.endereco, concedente.numero,  concedente.bairro,  ");
		sql.append(" concedente.responsavelConcedente, concedente.cpfResponsavelConcedente, concedente.emailResponsavelConcedente,  concedente.telefoneResponsavelConcedente,  ");
		sql.append(" concedente.cidade, concedente.codigoEscolaMEC, concedente.situacao, ");
		sql.append(" tipoConcedente.codigo as \"tipoConcedente.codigo\",  ");
		sql.append(" tipoConcedente.nome as \"tipoConcedente.nome\", ");
		sql.append(" tipoConcedente.codigoMECObrigatorio as \"tipoConcedente.codigoMECObrigatorio\", ");
		sql.append(" tipoConcedente.cnpjObrigatorio as \"tipoConcedente.cnpjObrigatorio\", ");
		sql.append(" tipoConcedente.permitirCadastroConcedente as \"tipoConcedente.permitirCadastroConcedente\" ");
		sql.append(" FROM concedente ");
		sql.append(" inner join tipoconcedente  on tipoconcedente.codigo = concedente.tipoconcedente");

		return sql;
	}

	private List<ConcedenteVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<ConcedenteVO> vetResultado = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	private ConcedenteVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) {
		ConcedenteVO obj = new ConcedenteVO();
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setCnpj(dadosSQL.getString("cnpj"));
		obj.setConcedente(dadosSQL.getString("concedente"));
		obj.setTelefone(dadosSQL.getString("telefone"));
		obj.setCep(dadosSQL.getString("cep"));
		obj.setEndereco(dadosSQL.getString("endereco"));
		obj.setNumero(dadosSQL.getString("numero"));
		obj.setBairro(dadosSQL.getString("bairro"));
		obj.setCidade(dadosSQL.getString("cidade"));
		obj.setResponsavelConcedente(dadosSQL.getString("responsavelConcedente"));
		obj.setCpfResponsavelConcedente(dadosSQL.getString("cpfResponsavelConcedente"));
		obj.setEmailResponsavelConcedente(dadosSQL.getString("emailResponsavelConcedente"));
		obj.setTelefoneResponsavelConcedente(dadosSQL.getString("telefoneResponsavelConcedente"));
		obj.setCodigoEscolaMEC(dadosSQL.getString("codigoEscolaMEC"));
		obj.setSituacao(AtivoInativoEnum.valueOf(dadosSQL.getString("situacao")));
		
		obj.getTipoConcedenteVO().setCodigo(dadosSQL.getInt("tipoconcedente.codigo"));
		obj.getTipoConcedenteVO().setNome(dadosSQL.getString("tipoconcedente.nome"));
		obj.getTipoConcedenteVO().setCnpjObrigatorio(dadosSQL.getBoolean("tipoConcedente.cnpjObrigatorio"));
		obj.getTipoConcedenteVO().setCodigoMECObrigatorio(dadosSQL.getBoolean("tipoConcedente.codigoMECObrigatorio"));
		obj.getTipoConcedenteVO().setPermitirCadastroConcedente(dadosSQL.getBoolean("tipoConcedente.permitirCadastroConcedente"));

		return obj;

	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void atualizarEmailConcedente(final ConcedenteVO  obj) throws Exception {
		final String sql = "UPDATE concedente set emailResponsavelConcedente=?   WHERE ((codigo = ?))";
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);				
				sqlAlterar.setString(1, obj.getEmailResponsavelConcedente());			
				sqlAlterar.setInt(2, obj.getCodigo());
				return sqlAlterar;
			}
		});
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<ConcedenteVO> consultarPorNome(String nomeConcedente, Integer tipoConcedente, Integer limite, Integer pagina, DataModelo dataModelo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) {
		try {
			consultar(getIdEntidade(), controlarAcesso, usuario);
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" where (sem_acentos(concedente.concedente)) ILIKE (sem_acentos(?)) ");
			if(Uteis.isAtributoPreenchido(tipoConcedente)) {
				sqlStr.append(" and tipoConcedente.codigo = ").append(tipoConcedente);
			}
			sqlStr.append(" and situacao = 'ATIVO' ");
			sqlStr.append(" ORDER BY concedente.concedente ");
			if (limite != null && limite > 0) {
				sqlStr.append(" limit ").append(limite).append(" offset ").append(pagina);
			}
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), nomeConcedente);
			dataModelo.setTotalRegistrosEncontrados(0);
			if (tabelaResultado.next()) {
				dataModelo.setTotalRegistrosEncontrados(tabelaResultado.getInt("totalRegistroConsulta"));
			}
			tabelaResultado.beforeFirst();
			return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
}
