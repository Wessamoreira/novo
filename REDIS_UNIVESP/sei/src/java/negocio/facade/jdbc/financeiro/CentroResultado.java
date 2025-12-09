package negocio.facade.jdbc.financeiro;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Iterators;

import controle.arquitetura.DataModelo;
import controle.arquitetura.TreeNodeCustomizado;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.enumeradores.SituacaoEnum;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.financeiro.CentroResultadoOrigemVO;
import negocio.comuns.financeiro.CentroResultadoRestricaoUsoVO;
import negocio.comuns.financeiro.CentroResultadoVO;
import negocio.comuns.financeiro.enumerador.RestricaoUsoCentroResultadoEnum;
import negocio.comuns.financeiro.enumerador.TipoCentroResultadoOrigemEnum;
import negocio.comuns.financeiro.enumerador.TipoNivelCentroResultadoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ProcessarParalelismo;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.CentroResultadoInterfaceFacade;

/**
 * 
 * @author PedroOtimize
 *
 */
@Repository
@Scope("singleton")
@Lazy
public class CentroResultado extends ControleAcesso implements CentroResultadoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4941199867280068581L;
	private static String idEntidade = "CentroResultado";

	public CentroResultado() {
		super();
	}

	private void validarDados(CentroResultadoVO obj) {
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getIdentificadorCentroResultado()), "O campo Identificador Centro Resultado (Centro Resultado) não foi informado.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getDescricao()), "O campo Descrição (Centro Resultado) não foi informado.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getSituacaoEnum()), "O campo Situação (Centro Resultado) não foi informado.");
		Uteis.checkState(validarUnicidade(obj), "Já existe um Centro de Resultado com esse Identificador: " + obj.getIdentificadorCentroResultado() + ".");
		Uteis.checkState(Uteis.isAtributoPreenchido(obj.getCodigo()) && validarLoopCentroResultadoPrincipal(obj), "Não é possível realizar essa operação, pois o Centro de Resultado Superior ira gerar um loop de Hierarquia.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.faturamento.nfe.CentroResultadoInterfaceFacade#persistir(negocio.comuns.faturamento.nfe.CentroResultadoVO, boolean, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(CentroResultadoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) {
		try {
			validarDados(obj);
			if (obj.getCodigo() == 0) {
				incluir(obj, verificarAcesso, usuarioVO);
			} else {
				alterar(obj, verificarAcesso, usuarioVO);
			}
			validarSeRegistroForamExcluidoDasListaSubordinadas(obj.getListaCentroResultadoRestricaoUsoVOs(), "CentroResultadoRestricaoUso", "CentroResultado", obj.getCodigo(), usuarioVO);
			getFacadeFactory().getCentroResultadoRestricaoUsoFacade().persistir(obj.getListaCentroResultadoRestricaoUsoVOs(), false, usuarioVO);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final CentroResultadoVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			CentroResultado.incluir(getIdEntidade(), verificarAcesso, usuario);
			final StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO CentroResultado (centroResultadoPrincipal, identificadorCentroResultado, descricao, ");
			sql.append("  restricaoUsoCentroResultado, situacao  )");
			sql.append("  VALUES ( ?, ?, ?, ?, ?)");
			sql.append(" returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql.toString());
					int i = 0;
					Uteis.setValuePreparedStatement(obj.getCentroResultadoPrincipal(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getIdentificadorCentroResultado(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getDescricao(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getRestricaoUsoCentroResultadoEnum().name(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getSituacaoEnum(), ++i, sqlInserir);
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(final ResultSet rs) throws SQLException {
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
			throw new StreamSeiException(e);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final CentroResultadoVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			CentroResultado.alterar(getIdEntidade(), verificarAcesso, usuario);
			final StringBuilder sql = new StringBuilder();
			sql.append("UPDATE CentroResultado ");
			sql.append("   SET centroResultadoPrincipal=?, identificadorCentroResultado=?, descricao=?,  ");
			sql.append("   restricaoUsoCentroResultado=?, situacao=? ");
			sql.append("   WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql.toString());
					int i = 0;
					Uteis.setValuePreparedStatement(obj.getCentroResultadoPrincipal(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getIdentificadorCentroResultado(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getDescricao(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getRestricaoUsoCentroResultadoEnum().name(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getSituacaoEnum(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);
					return sqlAlterar;
				}
			}) == 0) {
				incluir(obj, false, usuario);
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.faturamento.nfe.CentroResultadoInterfaceFacade#excluir(negocio.comuns.faturamento.nfe.CentroResultadoVO, boolean, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(CentroResultadoVO obj, boolean retornarRestricao, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			CentroResultado.excluir(getIdEntidade(), verificarAcesso, usuario);
			StringBuilder sql= new StringBuilder("");
			sql.append("select centroresultado.descricao as codigoOrigem, 'Centro Resultado' as origem from centroresultado where centroresultadoprincipal = ? 	");
			sql.append("union all	");
			sql.append("select centroresultadoorigem.codigo::varchar as codigoOrigem, 'Lançamento Centro Resultado' as origem from centroresultadoorigem where centroresultadoadministrativo = ?	");
			sql.append("union all	");
			sql.append("select unidadeensino.nome as codigoOrigem, 'Unidade Ensino'  as origem from unidadeensinocursocentroresultado  inner join unidadeensino on unidadeensino.codigo = unidadeensinocursocentroresultado.unidadeensino where unidadeensinocursocentroresultado.centroresultado = ?	");
			sql.append("union all	");
			sql.append("select unidadeensino.nome as codigoOrigem, 'Unidade Ensino'  as origem from unidadeensinoniveleducacionalcentroresultado  inner join unidadeensino on unidadeensino.codigo = unidadeensinoniveleducacionalcentroresultado.unidadeensino where unidadeensinoniveleducacionalcentroresultado.centroresultado = ?	");
			sql.append("union all	");
			sql.append("select unidadeensino.nome as codigoOrigem, 'Unidade Ensino'  as origem from unidadeensino where unidadeensino.centroresultado = ?	");
			sql.append("union all	");
			sql.append("select unidadeensino.nome as codigoOrigem, 'Unidade Ensino'  as origem from unidadeensino where unidadeensino.centroresultadorequerimento = ?	");
			sql.append("union all	");
			sql.append("select unidadeensino.nome as codigoOrigem, 'Unidade Ensino'  as origem from unidadeensinotiporequerimentocentroresultado inner join unidadeensino on unidadeensino.codigo = unidadeensinotiporequerimentocentroresultado.unidadeensino where unidadeensinotiporequerimentocentroresultado.centroresultado = ?	");
			sql.append("union all	");
			sql.append("select biblioteca.nome as codigoOrigem, 'Biblioteca'  as origem from biblioteca where biblioteca.centroresultado = ?	");
			sql.append("union all	");
			sql.append("select departamento.nome as codigoOrigem, 'Departamento'  as origem from departamento where departamento.centroresultado = ?	");			
			sql.append("union all	");
			sql.append("select requisicao.codigo::varchar as codigoOrigem, 'Requisição'  as origem from requisicao where requisicao.centroresultadoadministrativo = ?	");
			sql.append("union all	");
			sql.append("select turma.identificadorTurma as codigoOrigem, 'Turma'  as origem from turma where turma.centroresultado = ?	");
			sql.append("limit 1	");
			
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), obj.getCodigo(), obj.getCodigo(), obj.getCodigo(), obj.getCodigo(), obj.getCodigo(), obj.getCodigo(), obj.getCodigo(), obj.getCodigo(), obj.getCodigo(), obj.getCodigo(), obj.getCodigo());
			if(!rs.next()) {			
			getConexao().getJdbcTemplate().update("DELETE FROM CentroResultado WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario), obj.getCodigo());
			}else {
				if(retornarRestricao) {
					throw new Exception("Este registro é referenciado por outro cadastro ("+rs.getString("origem")+" - "+rs.getString("codigoOrigem")+").");
				}
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	private void validarDadosCentroResultadoRestricaoUso(CentroResultadoRestricaoUsoVO obj) {
		Uteis.checkState(obj.getCentroResultadoVO().getRestricaoUsoCentroResultadoEnum().isUsuarioEspecificos() && !Uteis.isAtributoPreenchido(obj.getUsuarioVO()), "O campo Usuário Especifico deve ser informado. ");
		Uteis.checkState(obj.getCentroResultadoVO().getRestricaoUsoCentroResultadoEnum().isUsuarioPerfilAcesso() && !Uteis.isAtributoPreenchido(obj.getPerfilAcessoVO()), "O campo Perfil Acesso Especifico deve ser informado. ");

	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void adicionarCentroResultadoRestricaoUso(CentroResultadoVO obj, CentroResultadoRestricaoUsoVO crru, UsuarioVO usuario) {
		try {
			crru.setCentroResultadoVO(obj);
			validarDadosCentroResultadoRestricaoUso(crru);
			if (crru.getCentroResultadoVO().getRestricaoUsoCentroResultadoEnum().isUsuarioPerfilAcesso()) {
				crru.setPerfilAcessoVO(getFacadeFactory().getPerfilAcessoFacade().consultarPorChavePrimaria(crru.getPerfilAcessoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
			}
			int index = 0;
			for (CentroResultadoRestricaoUsoVO objExistente : obj.getListaCentroResultadoRestricaoUsoVOs()) {
				if (objExistente.equalsCampoSelecaoLista(crru)) {
					obj.getListaCentroResultadoRestricaoUsoVOs().set(index, crru);
					return;
				}
				index++;
			}
			obj.getListaCentroResultadoRestricaoUsoVOs().add(crru);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void removerCentroResultadoRestricaoUso(CentroResultadoVO obj, CentroResultadoRestricaoUsoVO crru, UsuarioVO usuario) {
		Iterator<CentroResultadoRestricaoUsoVO> i = obj.getListaCentroResultadoRestricaoUsoVOs().iterator();
		while (i.hasNext()) {
			CentroResultadoRestricaoUsoVO objExistente = i.next();
			if (objExistente.equalsCampoSelecaoLista(crru)) {
				i.remove();
				return;
			}
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public boolean validarRestricaoUsoCentroResultado(CentroResultadoVO obj, DepartamentoVO departamento, CursoVO curso, TurmaVO turma, UsuarioVO usuario) {
		try {
			switch (obj.getRestricaoUsoCentroResultadoEnum()) {
			case APENAS_COORDENADOR_CURSO:
				return getFacadeFactory().getCursoCoordenadorFacade().consultarSeExisteCoordenadorPorUsuario(usuario, curso, turma);
			case APENAS_FUNCIONARIO_DEPARTAMENTO:
				return !Uteis.isAtributoPreenchido(departamento) ? false : getFacadeFactory().getDepartamentoFacade().consultarSePessoaTrabalhaNoDepartamento(usuario.getPessoa().getCodigo(), departamento.getCodigo(), obj.getCodigo());
			case APENAS_GESTOR_DEPARTAMENTO:
				return !Uteis.isAtributoPreenchido(departamento) ? false : getFacadeFactory().getDepartamentoFacade().consultarSeGerenteTrabalhaNoDepartamento(usuario.getPessoa().getCodigo(), departamento.getCodigo(), obj.getCodigo());
			case USUARIO_PERFIL_ACESSO:
				return getFacadeFactory().getCentroResultadoRestricaoUsoFacade().consultarSeExistePerfilAcessoEspecificoPorCentroResultado(obj.getCodigo(), usuario.getPerfilAcesso().getCodigo());
			case USUARIO_ESPECIFICOS:
				return getFacadeFactory().getCentroResultadoRestricaoUsoFacade().consultarSeExisteUsuarioEspecificoPorCentroResultado(obj.getCodigo(), usuario.getCodigo());
			case NENHUM:
				return true;
			case NUNCA_RECEBER_REGISTRO:
				return false;
			default:
				throw new StreamSeiException("Tipo de Restrição de Uso não condiz com nenhuma opção no sistema.");
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void validarNivelCentroResultadoSuperior(CentroResultadoVO obj, CentroResultadoVO centroResultadoSuperior) {
		Uteis.checkState(Uteis.isAtributoPreenchido(obj) && obj.getCodigo().equals(centroResultadoSuperior.getCodigo()), "Não é possível adicionar o centro de resultado superior a ele mesmo.");
		obj.setCentroResultadoPrincipal(centroResultadoSuperior);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void validarGeracaoDoCentroResultadoSuperiorAutomatico(CentroResultadoVO obj, CentroResultadoVO centroResultadoSuperior, UsuarioVO usuarioVO) {
		if(!Uteis.isAtributoPreenchido(obj.getCentroResultadoPrincipal()) && Uteis.isAtributoPreenchido(centroResultadoSuperior)){
			obj.setCentroResultadoPrincipal(centroResultadoSuperior);
			validarLoopCentroResultadoPrincipal(obj);
			final String sql = "UPDATE CentroResultado set centroResultadoPrincipal=? WHERE ((codigo = ?)) " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setInt(1, obj.getCentroResultadoPrincipal().getCodigo());
					sqlAlterar.setInt(2, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public CentroResultadoVO validarGeracaoDoCentroResultadoAutomatico(String descricao,  Integer unidadeEnsino, TipoNivelEducacional tipoNivelEducacional, Integer curso, boolean isValidarCentroResultadoSuperior, UsuarioVO usuarioVO) {
		CentroResultadoVO obj = new CentroResultadoVO();
		if (isValidarCentroResultadoSuperior && Uteis.isAtributoPreenchido(unidadeEnsino) && Uteis.isAtributoPreenchido(tipoNivelEducacional)) {
			obj.setCentroResultadoPrincipal(consultarCentroResultadoPorUnidadeEnsinoNivelEducacional(unidadeEnsino, tipoNivelEducacional, usuarioVO));
		} else if (isValidarCentroResultadoSuperior && Uteis.isAtributoPreenchido(unidadeEnsino) && Uteis.isAtributoPreenchido(curso)) {
			obj.setCentroResultadoPrincipal(consultarCentroResultadoPorUnidadeEnsinoCurso(unidadeEnsino, curso, usuarioVO));
		} else if (isValidarCentroResultadoSuperior && Uteis.isAtributoPreenchido(unidadeEnsino)) {
			obj.setCentroResultadoPrincipal(consultarCentroResultadoPorUnidadeEnsino(unidadeEnsino, usuarioVO));
		}
		obj.setDescricao(descricao);
		obj.setSituacaoEnum(SituacaoEnum.ATIVO);
		obj.setRestricaoUsoCentroResultadoEnum(RestricaoUsoCentroResultadoEnum.NENHUM);
		obj = consultarCentroResultadoPorDescricaodoPorSituacao(obj, usuarioVO);
		if (!Uteis.isAtributoPreenchido(obj)) {
			obj.setIdentificadorCentroResultado(consultarMaiorCodigoCentroResultado(obj.getCentroResultadoPrincipal().getIdentificadorCentroResultado()).toString());
			if (Uteis.isAtributoPreenchido(obj.getCentroResultadoPrincipal().getIdentificadorCentroResultado())) {
				obj.setIdentificadorCentroResultado(obj.getCentroResultadoPrincipal().getIdentificadorCentroResultado() + "." + obj.getIdentificadorCentroResultado());
			}
			getFacadeFactory().getCentroResultadoFacade().persistir(obj, false, usuarioVO);
		}
		return obj;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public StringBuilder getSQLPadraoConsultaArvoreCentroResultadoPorNivel() {
		StringBuilder sql = new StringBuilder();
		sql.append(" WITH RECURSIVE centroresultadosuperior ( ");
		sql.append(" nivel, codigo, identificadorcentroresultado, centroresultadoprincipal, descricao, nivelsuperior, situacao, codigossuperiores  ");
		sql.append("  ) as ( ");
		sql.append(" select ");
		sql.append("	trim(to_char(row_number() over( order by case when isnumeric(replace(centroresultado.identificadorcentroresultado, '.', '')) then to_char(replace(centroresultado.identificadorcentroresultado, '.', '')::numeric, '0000000000') else replace(centroresultado.identificadorcentroresultado, '.', '') end, centroresultado.codigo), '000000000000')) as nivel, ");
		sql.append("	centroresultado.codigo, ");
		sql.append("	centroresultado.identificadorcentroresultado, ");
		sql.append("	centroresultado.centroresultadoprincipal, ");
		sql.append("	centroresultado.descricao, ");
		sql.append("	trim(to_char(row_number() over(order by centroresultado.codigo), '000000000000')) as nivelsuperior, ");
		sql.append("	centroresultado.situacao, ");
		sql.append("	centroresultado.codigo || '' as codigossuperiores ");
		sql.append(" from centroresultado ");
		sql.append(" where centroresultado.centroresultadoprincipal is null ");
		sql.append(" union all ");
		sql.append(" select ");
		sql.append("	centroresultadosuperior.nivel::varchar || '.' || trim(to_char(row_number() over(order by centroresultadosuperior.nivel, centroresultadosuperior.nivelsuperior, case when isnumeric(replace(centroresultado.identificadorcentroresultado, '.', '')) then to_char(replace(centroresultado.identificadorcentroresultado, '.', '')::numeric, '0000000000') else replace(centroresultado.identificadorcentroresultado, '.', '') end, centroresultado.codigo), '000000000000')) as nivel, ");
		sql.append("	centroresultado.codigo, ");
		sql.append("	centroresultado.identificadorcentroresultado, ");
		sql.append("	centroresultado.centroresultadoprincipal, ");
		sql.append("	centroresultado.descricao, ");
		sql.append("	centroresultadosuperior.nivel as nivelsuperior, ");
		sql.append("	centroresultado.situacao, ");
		sql.append("	centroresultadosuperior.codigossuperiores || ',' || centroresultado.codigo as codigossuperiores ");
		sql.append(" from centroresultado ");
		sql.append(" inner join centroresultadosuperior on centroresultadosuperior.codigo = centroresultado.centroresultadoprincipal )");
		return sql;
	}
	
	private StringBuilder getSQLPadraoConsultaArvoreCentroResultado() {
		StringBuilder sql = new StringBuilder();
		sql.append(" WITH RECURSIVE centroresultadosuperior ( ");
		sql.append(" nivel, codigo, identificadorcentroresultado, centroresultadoprincipal, descricao, nivelsuperior  ");
		sql.append("  ) as ( ");
		return sql;
	}

	private StringBuilder getSQLPadraoConsultaBasicaArvore() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT centroResultado.codigo as \"centroResultado.codigo\",  ");
		sql.append(" centroResultado.identificadorCentroResultado as \"centroResultado.identificadorCentroResultado\",  ");
		sql.append(" centroResultado.descricao as \"centroResultado.descricao\",  ");
		sql.append(" centroResultado.centroResultadoPrincipal as \"centroResultadoPrincipal.codigo\" ");
		sql.append(" FROM centroResultado ");
		return sql;
	}
	
	private StringBuilder getSQLPadraoConsultarCentroResultadoArvoreCompleta(CentroResultadoVO obj, SituacaoEnum situacaoEnum) {
		StringBuilder sql = new StringBuilder("");
		sql.append(" WITH RECURSIVE centroresultadosuperior (	");
		sql.append(" 	nivel, codigo, identificadorcentroresultado, centroresultadoprincipal, descricao, nivelsuperior, situacao, restricaoUsoCentroResultado	");
		sql.append(" ) as (	");
		sql.append(" 	select trim(to_char(row_number() over(	");
		sql.append(" 	order by case when isnumeric(replace(centroresultado.identificadorcentroresultado, '.', '')) 	");
		sql.append(" 	then to_char(replace(centroresultado.identificadorcentroresultado, '.', '')::numeric, '0000000000') 	");
		sql.append(" 	else replace(centroresultado.identificadorcentroresultado, '.', '') end, centroresultado.codigo), '000000000000')) as nivel, 	");
		sql.append(" 	centroresultado.codigo, 	");
		sql.append(" 	centroresultado.identificadorcentroresultado, 	");
		sql.append(" 	centroresultado.centroresultadoprincipal, 	");
		sql.append(" 	centroresultado.descricao, trim(to_char(row_number() over(order by centroresultado.codigo), '000000000000'))  as nivelsuperior, centroresultado.situacao, centroResultado.restricaoUsoCentroResultado ");		
		sql.append(" 	from centroresultado ");
		if(Uteis.isAtributoPreenchido(obj)) {
			sql.append(" 	where centroresultado.codigo = ").append(obj.getCodigo());
		}else {
			sql.append(" 	where centroresultado.centroresultadoprincipal is null 		");
		}		
		if(situacaoEnum != null) {
			sql.append(" 	and centroresultado.situacao = '").append(situacaoEnum.name()).append("' ");
		}
		sql.append(" 	union all	");
		sql.append(" 	select centroresultadosuperior.nivel::varchar||'.'||	");
		sql.append(" 	trim(to_char(row_number() over(order by centroresultadosuperior.nivel, 	");
		sql.append(" 	centroresultadosuperior.nivelsuperior, case when isnumeric(replace(centroresultado.identificadorcentroresultado, '.', '')) 	");
		sql.append(" 	then to_char(replace(centroresultado.identificadorcentroresultado, '.', '')::numeric, '0000000000') 	");
		sql.append(" 	else replace(centroresultado.identificadorcentroresultado, '.', '') end, centroresultado.codigo), '000000000000')) as nivel, 	");
		sql.append(" 	centroresultado.codigo, 	");
		sql.append(" 	centroresultado.identificadorcentroresultado, 	");
		sql.append(" 	centroresultado.centroresultadoprincipal, centroresultado.descricao, 	");
		sql.append(" 	centroresultadosuperior.nivel as nivelsuperior, centroresultado.situacao, centroResultado.restricaoUsoCentroResultado	");
		sql.append(" 	from centroresultado 	");
		sql.append(" 	inner join centroresultadosuperior on centroresultadosuperior.codigo = centroresultado.centroresultadoprincipal				");
		if(situacaoEnum != null) {
			sql.append(" 	where centroresultado.situacao = '").append(situacaoEnum.name()).append("' ");
		}
		sql.append(" ) select codigo as \"centroResultado.codigo\",	");
		sql.append("  identificadorCentroResultado as \"centroResultado.identificadorCentroResultado\",	");
		sql.append("  descricao as \"centroResultado.descricao\",	");
		sql.append("  situacao as \"centroResultado.situacao\",	");
		sql.append("  restricaoUsoCentroResultado as \"centroResultado.restricaoUsoCentroResultado\",	");
		sql.append("  centroresultadoprincipal as \"centroresultadoprincipal.codigo\"	");
		sql.append(" from centroresultadosuperior	");		
		sql.append(" order by nivel, nivelsuperior	");	
		return sql;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public TreeNodeCustomizado consultarArvoreCentroResultadoSuperior(CentroResultadoVO obj, Boolean controlarAcesso, UsuarioVO usuarioVO) {
		StringBuilder sql = getSQLPadraoConsultaArvoreCentroResultado();
		sql.append(getSQLPadraoConsultaBasicaArvore());
		sql.append(" where centroResultado.codigo= ").append(obj.getCentroResultadoPrincipal().getCodigo());
		sql.append(" union ");
		sql.append(getSQLPadraoConsultaBasicaArvore());
		sql.append(" inner join crSuperior on centroResultado.codigo = crSuperior.\"centroResultadoPrincipal.codigo\" ");
		sql.append(" ) select * from crSuperior order by  ");
		sql.append(" case when crSuperior.\"centroResultadoPrincipal.codigo\" is null then 0  ");
		sql.append("  when crSuperior.\"centroResultadoPrincipal.codigo\" > crSuperior.\"centroResultado.codigo\" then crSuperior.\"centroResultado.codigo\" ");
		sql.append("  else crSuperior.\"centroResultadoPrincipal.codigo\" end, ");
		sql.append("  crSuperior.\"centroResultado.codigo\" ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosArvoreCentroResultado(rs);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public TreeNodeCustomizado consultarArvoreCentroResultadoInferior(CentroResultadoVO obj, SituacaoEnum situacaoEnum, Boolean controlarAcesso, UsuarioVO usuarioVO) {
		StringBuilder sql = getSQLPadraoConsultarCentroResultadoArvoreCompleta(obj, situacaoEnum);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosArvoreCentroResultado(rs);
	}

	private TreeNodeCustomizado montarDadosArvoreCentroResultado(SqlRowSet rs) {
		TreeNodeCustomizado treeNodeCustomizadoRaiz = new TreeNodeCustomizado();
		Map<Integer, TreeNodeCustomizado> nodes = new HashMap<>(0);
		while (rs.next()) {
			CentroResultadoVO cr = new CentroResultadoVO();
			cr.setNovoObj(Boolean.FALSE);
			cr.setCodigo(rs.getInt("centroResultado.codigo"));
			cr.setIdentificadorCentroResultado(rs.getString("centroResultado.identificadorCentroResultado"));
			cr.setDescricao(rs.getString("centroResultado.descricao"));
			cr.getCentroResultadoPrincipal().setCodigo(rs.getInt("centroResultadoPrincipal.codigo"));
			TreeNodeCustomizado nodeImpl = new TreeNodeCustomizado(cr);
			if (!nodes.containsKey(cr.getCentroResultadoPrincipal().getCodigo())) {
				nodes.put(cr.getCentroResultadoPrincipal().getCodigo(), treeNodeCustomizadoRaiz);
			}
			nodeImpl.setData(cr);
			nodeImpl.setMaximizarTree(false);
			nodes.get(cr.getCentroResultadoPrincipal().getCodigo()).addChild(cr, nodeImpl);
			nodes.put(cr.getCodigo(), nodeImpl);
		}
		return treeNodeCustomizadoRaiz;
	}

	private StringBuilder getSQLPadraoConsultaTotalBasica() {
		StringBuilder sql = new StringBuilder("");
		sql.append(" SELECT count(CentroResultado.codigo) as qtde FROM CentroResultado ");
		sql.append(" LEFT JOIN centroResultado as centroResultadoPrincipal ON centroResultadoPrincipal.codigo = centroResultado.centroResultadoPrincipal ");
		return sql;
	}

	private StringBuilder getSQLPadraoConsultaBasica() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT centroResultado.codigo as \"centroResultado.codigo\",  ");
		sql.append(" centroResultado.identificadorCentroResultado as \"centroResultado.identificadorCentroResultado\", centroResultado.descricao as \"centroResultado.descricao\", ");
		sql.append(" centroResultado.restricaoUsoCentroResultado as \"centroResultado.restricaoUsoCentroResultado\", ");
		sql.append(" centroResultado.situacao as \"centroResultado.situacao\",  ");
		sql.append(" centroResultadoPrincipal.codigo as \"centroResultadoPrincipal.codigo\",  ");
		sql.append(" centroResultadoPrincipal.identificadorCentroResultado as \"centroResultadoPrincipal.identificadorCentroResultado\", centroResultadoPrincipal.descricao as \"centroResultadoPrincipal.descricao\", ");
		sql.append(" centroResultadoPrincipal.restricaoUsoCentroResultado as \"centroResultadoPrincipal.restricaoUsoCentroResultado\", ");
		sql.append(" centroResultadoPrincipal.situacao as \"centroResultadoPrincipal.situacao\" ");
		sql.append(" FROM centroResultado ");
		sql.append(" LEFT JOIN centroResultado as centroResultadoPrincipal ON centroResultadoPrincipal.codigo = centroResultado.centroResultadoPrincipal ");

		return sql;
	}

	private void validarFiltrosParaConsulta(SituacaoEnum situacaoEnum, boolean isValidarRestricaoUsoCentroResultado, DepartamentoVO departamento, CursoVO curso, TurmaVO turma, StringBuilder sqlStr, DataModelo dataModelo) {
		if (Uteis.isAtributoPreenchido(situacaoEnum)) {
			sqlStr.append(" and centroResultado.situacao = ? ");
			dataModelo.getListaFiltros().add(situacaoEnum.name());
		}
		if(isValidarRestricaoUsoCentroResultado){
			sqlStr.append(" and case  ");
			sqlStr.append("     when centroResultado.restricaoUsoCentroResultado = 'NENHUM' then true ");
			sqlStr.append("     when centroResultado.restricaoUsoCentroResultado = 'NUNCA_RECEBER_REGISTRO' then false ");
			sqlStr.append("     when centroResultado.restricaoUsoCentroResultado = 'USUARIO_ESPECIFICOS' and ").append(dataModelo.getUsuario().getCodigo()).append(" in (select usuario from CentroResultadoRestricaoUso where CentroResultadoRestricaoUso.centroResultado = centroresultado.codigo) then true ");
			sqlStr.append("     when centroResultado.restricaoUsoCentroResultado = 'USUARIO_PERFIL_ACESSO' and ").append(dataModelo.getUsuario().getPerfilAcesso().getCodigo()).append(" in (select perfilacesso from CentroResultadoRestricaoUso where CentroResultadoRestricaoUso.centroResultado = centroresultado.codigo) then true  ");
			validarFiltrosParaConsultaPorRestricaoApenasCoordenadorCurso(sqlStr, curso, turma, dataModelo);
			validarFiltrosParaConsultaPorRestricaoDepartamento(sqlStr, departamento,  dataModelo);
			sqlStr.append("     else false end ");
		}		
	}

	private void validarFiltrosParaConsultaPorRestricaoDepartamento(StringBuilder sqlStr, DepartamentoVO departamento,  DataModelo dataModelo) {
		if(Uteis.isAtributoPreenchido(departamento)){
			sqlStr.append(" when centroResultado.restricaoUsoCentroResultado = 'APENAS_FUNCIONARIO_DEPARTAMENTO' then  ");
			sqlStr.append(" (SELECT case when count(1) > 0 then true else false end FROM departamento ");
			sqlStr.append(" inner join cargo on cargo.departamento = departamento.codigo ");
			sqlStr.append(" inner join funcionariocargo on funcionariocargo.cargo = cargo.codigo ");
			sqlStr.append(" inner join funcionario on funcionariocargo.funcionario = funcionario.codigo ");
			sqlStr.append(" inner join pessoa on funcionario.pessoa = pessoa.codigo ");
			sqlStr.append(" where pessoa.codigo = ").append(dataModelo.getUsuario().getPessoa().getCodigo());
			sqlStr.append(" and departamento.codigo = ").append(departamento.getCodigo());
			sqlStr.append(" and departamento.centroResultado = centroResultado.codigo");
			sqlStr.append(" ) ");
			
			sqlStr.append(" when centroResultado.restricaoUsoCentroResultado = 'APENAS_GESTOR_DEPARTAMENTO' then  ");
			sqlStr.append(" (SELECT case when count(1) > 0 then true else false end FROM departamento ");
			sqlStr.append(" inner join cargo on cargo.departamento = departamento.codigo ");
			sqlStr.append(" inner join funcionariocargo on funcionariocargo.cargo = cargo.codigo ");
			sqlStr.append(" inner join funcionario on funcionariocargo.funcionario = funcionario.codigo ");
			sqlStr.append(" inner join pessoa on funcionario.pessoa = pessoa.codigo ");
			sqlStr.append(" where pessoa.codigo = ").append(dataModelo.getUsuario().getPessoa().getCodigo());
			sqlStr.append(" and departamento.codigo = ").append(departamento.getCodigo());
			sqlStr.append(" and departamento.centroResultado = centroResultado.codigo");
			sqlStr.append(" and funcionariocargo.gerente = true ");
			sqlStr.append(" ) ");
		}else{
			sqlStr.append(" when centroResultado.restricaoUsoCentroResultado = 'APENAS_FUNCIONARIO_DEPARTAMENTO' then false ");
			sqlStr.append(" when centroResultado.restricaoUsoCentroResultado = 'APENAS_GESTOR_DEPARTAMENTO' then false ");
		}
	}

	private void validarFiltrosParaConsultaPorRestricaoApenasCoordenadorCurso(StringBuilder sqlStr,  CursoVO curso, TurmaVO turma, DataModelo dataModelo) {
		if(Uteis.isAtributoPreenchido(curso) || Uteis.isAtributoPreenchido(turma)){
			sqlStr.append(" when centroResultado.restricaoUsoCentroResultado = 'APENAS_COORDENADOR_CURSO' then ");
			sqlStr.append(" (select case when count(1) > 0 then true else false end from cursocoordenador ");
			sqlStr.append(" inner join funcionario ON funcionario.codigo = cursocoordenador.funcionario ");
			sqlStr.append(" inner join usuario on usuario.pessoa = funcionario.pessoa ");
			sqlStr.append(" where usuario.codigo = ").append(dataModelo.getUsuario().getCodigo());
			if(Uteis.isAtributoPreenchido(curso)){
				 sqlStr.append(" and cursocoordenador.curso =  ").append(curso.getCodigo());  
			 }
			 if(Uteis.isAtributoPreenchido(turma)){
				 sqlStr.append(" and ( cursocoordenador.curso in ( select curso from turma where codigo = ").append(turma.getCodigo()).append(" ) or cursocoordenador.turma =  ").append(turma.getCodigo()).append(" ) ");  
			 }
			 sqlStr.append(" ) ");
		}else{
			sqlStr.append(" when centroResultado.restricaoUsoCentroResultado = 'APENAS_COORDENADOR_CURSO' then false ");
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void consultar(SituacaoEnum situacaoEnum, boolean isValidarRestricaoUsoCentroResultado, DepartamentoVO departamento, CursoVO curso, TurmaVO turma, DataModelo dataModelo) {
		List<CentroResultadoVO> objs = new ArrayList<>();

		switch (CentroResultadoVO.enumCampoConsultaCentroResultado.valueOf(dataModelo.getCampoConsulta())) {
		case IDENTIFICADOR_CENTRO_RESULTADO:
			objs = getFacadeFactory().getCentroResultadoFacade().consultaRapidaPorIdentificadorCentroResultado(dataModelo.getValorConsulta(), situacaoEnum, isValidarRestricaoUsoCentroResultado, departamento, curso, turma, dataModelo);
			dataModelo.getListaFiltros().clear();
			dataModelo.setTotalRegistrosEncontrados(getFacadeFactory().getCentroResultadoFacade().consultarTotalPorIdentificadorCentroResultado(dataModelo.getValorConsulta(), situacaoEnum, isValidarRestricaoUsoCentroResultado, departamento, curso, turma,dataModelo));
			break;
		case DESCRICAO_CENTRO_RESULTADO:
			objs = getFacadeFactory().getCentroResultadoFacade().consultaRapidaPorDescricaoCentroResultado(dataModelo.getValorConsulta(), situacaoEnum, isValidarRestricaoUsoCentroResultado, departamento, curso, turma,dataModelo);
			dataModelo.getListaFiltros().clear();
			dataModelo.setTotalRegistrosEncontrados(getFacadeFactory().getCentroResultadoFacade().consultarTotalPorDescricaoCentroResultado(dataModelo.getValorConsulta(), situacaoEnum, isValidarRestricaoUsoCentroResultado, departamento, curso, turma,dataModelo));
			break;
		case DESCRICAO_CENTRO_RESULTADO_SUPERIOR:
			objs = getFacadeFactory().getCentroResultadoFacade().consultaRapidaPorDescricaoCentroResultadoSuperior(dataModelo.getValorConsulta(), situacaoEnum, isValidarRestricaoUsoCentroResultado, departamento, curso, turma,dataModelo);
			dataModelo.getListaFiltros().clear();
			dataModelo.setTotalRegistrosEncontrados(getFacadeFactory().getCentroResultadoFacade().consultarTotalPorDescricaoCentroResultadoSuperior(dataModelo.getValorConsulta(), situacaoEnum, isValidarRestricaoUsoCentroResultado, departamento, curso, turma,dataModelo));
			break;
		default:
			break;
		}
		dataModelo.setListaConsulta(objs);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public List<CentroResultadoVO>  consultarCentroResultadoPorArvoreCompleta(CentroResultadoVO obj, SituacaoEnum situacaoEnum, Boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioVO);
		StringBuilder sql = getSQLPadraoConsultarCentroResultadoArvoreCompleta(obj, situacaoEnum);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(rs, nivelMontarDados ,usuarioVO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.faturamento.nfe.CentroResultadoInterfaceFacade#consultaRapidaPorNumero(java.lang.Long, boolean, int, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<CentroResultadoVO> consultaRapidaPorIdentificadorCentroResultado(String valorConsulta, SituacaoEnum situacaoEnum, boolean isValidarRestricaoUsoCentroResultado, DepartamentoVO departamento, CursoVO curso, TurmaVO turma, DataModelo dataModelo) {

		try {
			ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" WHERE lower(centroResultado.identificadorCentroResultado) like (?)");
			dataModelo.getListaFiltros().add(valorConsulta.toLowerCase() + PERCENT);
			validarFiltrosParaConsulta(situacaoEnum, isValidarRestricaoUsoCentroResultado, departamento, curso, turma, sqlStr, dataModelo);
			sqlStr.append(" ORDER BY centroResultado.identificadorCentroResultado  ");
			UteisTexto.addLimitAndOffset(sqlStr, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), dataModelo.getListaFiltros().toArray());
			return montarDadosConsultaBasica(rs, dataModelo.getNivelMontarDados(), dataModelo.getUsuario());
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public Integer consultarTotalPorIdentificadorCentroResultado(String valorConsulta, SituacaoEnum situacaoEnum, boolean isValidarRestricaoUsoCentroResultado, DepartamentoVO departamento, CursoVO curso, TurmaVO turma, DataModelo dataModelo) {

		try {
			StringBuilder sqlStr = getSQLPadraoConsultaTotalBasica();
			sqlStr.append(" WHERE lower(centroResultado.identificadorCentroResultado) like (?)");
			dataModelo.getListaFiltros().add(valorConsulta.toLowerCase() + PERCENT);
			validarFiltrosParaConsulta(situacaoEnum, isValidarRestricaoUsoCentroResultado,  departamento, curso, turma, sqlStr, dataModelo);
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), dataModelo.getListaFiltros().toArray());
			return (Integer) Uteis.getSqlRowSetTotalizador(rs, "qtde", TipoCampoEnum.INTEIRO);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.faturamento.nfe.CentroResultadoInterfaceFacade#consultaRapidaPorNumero(java.lang.Long, boolean, int, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<CentroResultadoVO> consultaRapidaPorDescricaoCentroResultado(String valorConsulta, SituacaoEnum situacaoEnum, boolean isValidarRestricaoUsoCentroResultado, DepartamentoVO departamento, CursoVO curso, TurmaVO turma, DataModelo dataModelo) {

		try {
			ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" WHERE lower(sem_acentos(centroResultado.descricao) ) like (lower(sem_acentos(?)))");
			dataModelo.getListaFiltros().add(valorConsulta.toLowerCase() + PERCENT);
			validarFiltrosParaConsulta(situacaoEnum, isValidarRestricaoUsoCentroResultado,  departamento, curso, turma, sqlStr, dataModelo);
			sqlStr.append(" ORDER BY centroResultado.descricao  ");
			UteisTexto.addLimitAndOffset(sqlStr, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), dataModelo.getListaFiltros().toArray());
			return montarDadosConsultaBasica(rs, dataModelo.getNivelMontarDados(), dataModelo.getUsuario());
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public Integer consultarTotalPorDescricaoCentroResultado(String valorConsulta, SituacaoEnum situacaoEnum, boolean isValidarRestricaoUsoCentroResultado, DepartamentoVO departamento, CursoVO curso, TurmaVO turma, DataModelo dataModelo) {

		try {
			StringBuilder sqlStr = getSQLPadraoConsultaTotalBasica();
			sqlStr.append(" WHERE lower(sem_acentos(centroResultado.descricao) ) like (lower(sem_acentos(?)))");
			dataModelo.getListaFiltros().add(valorConsulta.toLowerCase() + PERCENT);
			validarFiltrosParaConsulta(situacaoEnum, isValidarRestricaoUsoCentroResultado,  departamento, curso, turma, sqlStr, dataModelo);
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), dataModelo.getListaFiltros().toArray());
			return (Integer) Uteis.getSqlRowSetTotalizador(rs, "qtde", TipoCampoEnum.INTEIRO);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.faturamento.nfe.CentroResultadoInterfaceFacade#consultaRapidaPorNumero(java.lang.Long, boolean, int, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<CentroResultadoVO> consultaRapidaPorDescricaoCentroResultadoSuperior(String valorConsulta, SituacaoEnum situacaoEnum, boolean isValidarRestricaoUsoCentroResultado, DepartamentoVO departamento, CursoVO curso, TurmaVO turma, DataModelo dataModelo) {

		try {
			ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" WHERE lower(sem_acentos(centroResultadoPrincipal.descricao) ) like (lower(sem_acentos(?)))");
			dataModelo.getListaFiltros().add(valorConsulta.toLowerCase() + PERCENT);
			validarFiltrosParaConsulta(situacaoEnum, isValidarRestricaoUsoCentroResultado,  departamento, curso, turma, sqlStr, dataModelo);
			sqlStr.append(" ORDER BY centroResultadoPrincipal.descricao ");
			UteisTexto.addLimitAndOffset(sqlStr, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), dataModelo.getListaFiltros().toArray());
			return montarDadosConsultaBasica(rs, dataModelo.getNivelMontarDados(), dataModelo.getUsuario());
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public Integer consultarTotalPorDescricaoCentroResultadoSuperior(String valorConsulta, SituacaoEnum situacaoEnum, boolean isValidarRestricaoUsoCentroResultado, DepartamentoVO departamento, CursoVO curso, TurmaVO turma, DataModelo dataModelo) {
		try {
			StringBuilder sqlStr = getSQLPadraoConsultaTotalBasica();
			sqlStr.append(" WHERE lower(sem_acentos(centroResultadoPrincipal.descricao) ) like (lower(sem_acentos(?)))");
			dataModelo.getListaFiltros().add(valorConsulta.toLowerCase() + PERCENT);
			validarFiltrosParaConsulta(situacaoEnum, isValidarRestricaoUsoCentroResultado,  departamento, curso, turma, sqlStr, dataModelo);
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), dataModelo.getListaFiltros().toArray());
			return (Integer) Uteis.getSqlRowSetTotalizador(rs, "qtde", TipoCampoEnum.INTEIRO);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public boolean validarLoopCentroResultadoPrincipal(CentroResultadoVO obj) {
		if(!Uteis.isAtributoPreenchido(obj.getCentroResultadoPrincipal())){
			return false;
		}
		StringBuilder sql = new StringBuilder();
		sql.append(" select 1 as qtde from ( ");
		sql.append(" WITH RECURSIVE  crSuperior (  \"centroResultado.codigo\",  \"centroResultadoPrincipal.codigo\"    )as ( ");
		sql.append(" SELECT centroResultado.codigo as \"centroResultado.codigo\",  centroResultado.centroResultadoPrincipal as \"centroResultadoPrincipal.codigo\"  ");
		sql.append(" FROM centroResultado  where centroResultado.codigo =  ").append(obj.getCentroResultadoPrincipal().getCodigo());
		sql.append(" union ");
		sql.append(" SELECT centroResultado.codigo as \"centroResultado.codigo\", centroResultado.centroResultadoPrincipal as \"centroResultadoPrincipal.codigo\"  ");
		sql.append(" FROM centroResultado  ");
		sql.append(" inner join crSuperior on centroResultado.codigo = crSuperior.\"centroResultadoPrincipal.codigo\"  ) ");
		sql.append(" select * from crSuperior order by   case when crSuperior.\"centroResultadoPrincipal.codigo\" is null then 0   when crSuperior.\"centroResultadoPrincipal.codigo\" > crSuperior.\"centroResultado.codigo\" then crSuperior.\"centroResultado.codigo\"   else crSuperior.\"centroResultadoPrincipal.codigo\" end,   crSuperior.\"centroResultado.codigo\" ");
		sql.append(" ) as t where   \"centroResultado.codigo\" in ( ").append(obj.getCodigo()).append(") ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		Integer existeCentroResultadoPai = (Integer) Uteis.getSqlRowSetTotalizador(rs, "qtde", TipoCampoEnum.INTEIRO);
		return Uteis.isAtributoPreenchido(existeCentroResultadoPai);
	}

	private Boolean validarUnicidade(CentroResultadoVO obj) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT codigo FROM CentroResultado ");
		sql.append(" WHERE identificadorCentroResultado = ? ");
		if (Uteis.isAtributoPreenchido(obj.getCodigo())) {
			sql.append(" and codigo != ").append(obj.getCodigo()).append(" ");
		}
		return getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), obj.getIdentificadorCentroResultado()).next();
	}
	
	private Integer consultarMaiorCodigoCentroResultado(String identificadorCentroResultadoSuperior) {
		try {
			StringBuilder sql = new StringBuilder("SELECT max(codigo) as maior FROM CentroResultado ");
			
			if(Uteis.isAtributoPreenchido(identificadorCentroResultadoSuperior)) {
				sql = new StringBuilder("");
				sql.append(" select 1 as ordem, max(replace(centroResultado.identificadorcentroresultado, '").append(identificadorCentroResultadoSuperior).append(".', '')::NUMERIC::INT) as maior ");
				sql.append(" FROM CentroResultado ");
				sql.append(" inner join centroresultado crs on crs.codigo = centroresultado.centroresultadoprincipal ");
				sql.append( " where crs.identificadorcentroresultado = '").append(identificadorCentroResultadoSuperior).append("'");
				sql.append( " and (length(CentroResultado.identificadorcentroresultado) - length(replace(CentroResultado.identificadorcentroresultado, '.', ''))) "); 
				sql.append( " =  ((length('").append(identificadorCentroResultadoSuperior).append("') - length(replace('").append(identificadorCentroResultadoSuperior).append("', '.', ''))) + 1) ");
				sql.append(" and isnumeric(replace(centroResultado.identificadorcentroresultado, '").append(identificadorCentroResultadoSuperior).append(".', ''))");
				sql.append(" union all");
				sql.append(" select 2 as ordem, count(centroresultado.codigo) as maior");
				sql.append(" FROM CentroResultado ");
				sql.append(" inner join centroresultado crs on crs.codigo = centroresultado.centroresultadoprincipal ");
				sql.append(" where crs.identificadorcentroresultado = '").append(identificadorCentroResultadoSuperior).append("'");
				sql.append(" having not exists (");
				sql.append("	select cr.codigo from centroresultado as cr where cr.identificadorcentroresultado = ('").append(identificadorCentroResultadoSuperior).append(".'||(count(centroresultado.codigo)+1))");
				sql.append(" )");
				sql.append(" union all");
				sql.append(" select 3 as ordem, max(centroresultado.codigo) as maior");
				sql.append(" FROM CentroResultado ");
				sql.append(" inner join centroresultado crs on crs.codigo = centroresultado.centroresultadoprincipal ");
				sql.append(" where crs.identificadorcentroresultado = '").append(identificadorCentroResultadoSuperior).append("'");
				sql.append(" having not exists (");
				sql.append("	select cr.codigo from centroresultado as cr where cr.identificadorcentroresultado = ('").append(identificadorCentroResultadoSuperior).append(".'||(max(centroresultado.codigo)+1))");
				sql.append(" )");
				sql.append(" order by ordem limit 1");
				
			}			
			
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
			return ((Integer) Uteis.getSqlRowSetTotalizador(rs, "maior", TipoCampoEnum.INTEIRO)) + 1;
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	@Override
	public CentroResultadoVO consultarCentroResultadoEstoquePorUnidadeEnsino(Integer unidadeEnsino, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder(" select * from ( ");
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sqlStr.append(" select centroresultado.*, 1 as ordem from centroresultado  ");
			sqlStr.append(" left join departamento on centroresultado.codigo = departamento.centroresultado");
			sqlStr.append(" where departamento.controlaestoque = true ");
			sqlStr.append(" and departamento.unidadeensino = ").append(unidadeEnsino);
			sqlStr.append(" union all ");
		}
		sqlStr.append(" select centroresultado.* , 2 as ordem from centroresultado ");
		sqlStr.append(" left join departamento on centroresultado.codigo = departamento.centroresultado");
		sqlStr.append(" where  departamento.controlaestoque = true and departamento.unidadeensino is null ");
		sqlStr.append(" ) as t order by ordem limit 1 ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			return new CentroResultadoVO();
		}
		return montarDados(tabelaResultado, nivelMontarDados, usuario);
	}

	@Override
	public CentroResultadoVO consultarCentroResultadoPorUnidadeEnsino(Integer unidadeEnsino, UsuarioVO usuario) {
		StringBuilder sql = new StringBuilder("SELECT CentroResultado.* FROM CentroResultado ");
		sql.append(" inner join unidadeensino on unidadeensino.centroresultado = centroresultado.codigo ");
		sql.append(" and unidadeensino.codigo = ? ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), unidadeEnsino);
		if (!tabelaResultado.next()) {
			return new CentroResultadoVO();
		}
		return (montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
	}

	@Override
	public CentroResultadoVO consultarCentroResultadoPorDepartamento(Integer departamento, UsuarioVO usuario) {
		StringBuilder sql = new StringBuilder("SELECT CentroResultado.* FROM CentroResultado ");
		sql.append(" inner join departamento on departamento.centroresultado = centroresultado.codigo ");
		sql.append(" and departamento.codigo = ? ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), departamento);
		if (!tabelaResultado.next()) {
			return new CentroResultadoVO();
		}
		return (montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
	}
	

	@Override
	public CentroResultadoVO consultarCentroResultadoPorTurma(Integer turma, UsuarioVO usuario) {
		StringBuilder sql = new StringBuilder("SELECT CentroResultado.* FROM CentroResultado ");
		sql.append(" inner join turma on turma.centroresultado = centroresultado.codigo ");
		sql.append(" and turma.codigo = ? ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), turma);
		if (!tabelaResultado.next()) {
			return new CentroResultadoVO();
		}
		return (montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
	}

	@Override
	public CentroResultadoVO consultarCentroResultadoPorUnidadeEnsinoCurso(Integer unidadeEnsino, Integer curso, UsuarioVO usuario) {
		StringBuilder sql = new StringBuilder("SELECT CentroResultado.* FROM CentroResultado ");
		sql.append(" inner join unidadeensinocursocentroresultado on unidadeensinocursocentroresultado.centroresultado = centroresultado.codigo ");
		sql.append(" and unidadeensinocursocentroresultado.unidadeensino = ? and unidadeensinocursocentroresultado.curso = ?");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), unidadeEnsino, curso);
		if (!tabelaResultado.next()) {
			return new CentroResultadoVO();
		}
		return (montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
	}

	@Override
	public CentroResultadoVO consultarCentroResultadoPorUnidadeEnsinoNivelEducacional(Integer unidadeEnsino, TipoNivelEducacional tipoNivelEducacional, UsuarioVO usuario) {
		StringBuilder sql = new StringBuilder("SELECT CentroResultado.* FROM CentroResultado ");
		sql.append(" inner join unidadeensinoniveleducacionalcentroresultado on unidadeensinoniveleducacionalcentroresultado.centroresultado = centroresultado.codigo ");
		sql.append(" and unidadeensinoniveleducacionalcentroresultado.unidadeensino = ? and unidadeensinoniveleducacionalcentroresultado.tipoNivelEducacional = ?");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), unidadeEnsino, tipoNivelEducacional.name());
		if (!tabelaResultado.next()) {
			return new CentroResultadoVO();
		}
		return (montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
	}

	private CentroResultadoVO consultarCentroResultadoPorDescricaodoPorSituacao(CentroResultadoVO obj, UsuarioVO usuario) {
		StringBuilder sql = new StringBuilder("SELECT * FROM CentroResultado ");
		sql.append(" WHERE descricao = ? and  situacao = ? ");
		if(Uteis.isAtributoPreenchido(obj.getCentroResultadoPrincipal())) {
			sql.append(" and centroResultadoPrincipal = ").append(obj.getCentroResultadoPrincipal().getCodigo()); 	
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), obj.getDescricao(), obj.getSituacaoEnum().name());
		if (!tabelaResultado.next()) {
			return obj;
		}
		return (montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
	}

	@Override
	public CentroResultadoVO consultaCentroResultadoPadraoConfiguracaoFinanceiroPorUnidadeEnsino(Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" select distinct CentroResultado.*, 1 as ordem from unidadeensino ");
		sql.append(" inner join configuracoes on configuracoes.codigo = ( ");
		sql.append(" case when unidadeensino.configuracoes is not null then unidadeensino.configuracoes else  ");
		sql.append(" (select c.codigo from configuracoes c where c.padrao = true ) end) ");
		sql.append(" inner join configuracaofinanceiro on configuracaofinanceiro.configuracoes = configuracoes.codigo ");
		sql.append(" inner join configuracaofinanceirocartao on configuracaofinanceiro.codigo = configuracaofinanceirocartao.configuracaofinanceiro ");
		sql.append(" inner join centroResultado on centroResultado.codigo = configuracaofinanceirocartao.centroResultadoAdministrativo");
		sql.append(" where unidadeensino.codigo = ").append(unidadeEnsino);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (tabelaResultado.next()) {
			return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		}
		return null;
	}
	
	
	@Override
	public CentroResultadoVO consultarCentroResultadoPadraoRequerimentoPorUnidadeEnsino(Integer unidadeEnsino, Integer requerimento, UsuarioVO usuario) {
		StringBuilder sqlStr = new StringBuilder(" select * from ( ");
		if(Uteis.isAtributoPreenchido(requerimento)){
			sqlStr.append(" select  centroresultado.*,   1 as ordem from centroresultado  ");
			sqlStr.append(" inner join unidadeensinotiporequerimentocentroresultado on unidadeensinotiporequerimentocentroresultado.centroresultado = centroresultado.codigo ");
			sqlStr.append(" inner join requerimento on requerimento.tiporequerimento = unidadeensinotiporequerimentocentroresultado.tiporequerimento ");
			sqlStr.append(" where unidadeensinotiporequerimentocentroresultado.unidadeensino = ").append(unidadeEnsino);
			sqlStr.append(" and requerimento.codigo = ").append(requerimento);
			sqlStr.append(" union  ");
		}
		sqlStr.append(" select  centroresultado.*,   2 as ordem from centroresultado  ");
		sqlStr.append(" inner join unidadeensino on   unidadeensino.centroresultadorequerimento  = centroresultado.codigo ");
		sqlStr.append(" where unidadeensino.codigo = ").append(unidadeEnsino);
		sqlStr.append(" ) as t order by ordem limit 1 ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			return new CentroResultadoVO();
		}
		return (montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public CentroResultadoVO consultarCentroResultadoPadraoBibliotecaPorEmprestimo(Integer emprestimo, UsuarioVO usuario) {
		StringBuilder sqlStr = new StringBuilder(" ");
		sqlStr.append(" select  centroresultado.* from centroresultado  ");
		sqlStr.append(" inner join biblioteca on biblioteca.centroresultado = centroresultado.codigo ");
		sqlStr.append(" inner join emprestimo on emprestimo.biblioteca = biblioteca.codigo ");
		sqlStr.append(" where emprestimo.codigo = ").append(emprestimo);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			return new CentroResultadoVO();
		}
		return (montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public CentroResultadoVO consultarCentroResultadoPorContratoReceita(Integer contratosreceitas, UsuarioVO usuario) {
		StringBuilder sqlStr = new StringBuilder(" ");
		sqlStr.append(" select  centroresultado.* from centroresultado  ");
		sqlStr.append(" inner join departamento on departamento.centroresultado = centroresultado.codigo ");
		sqlStr.append(" inner join contratosreceitas on contratosreceitas.departamento = departamento.codigo ");
		sqlStr.append(" where contratosreceitas.codigo = ").append(contratosreceitas);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			return new CentroResultadoVO();
		}
		return (montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public Integer consultarMaiorNivelCentroResultado(UsuarioVO usuario) {
		StringBuilder sqlStr = getSQLPadraoConsultaArvoreCentroResultadoPorNivel();
		sqlStr.append(" select ");
		sqlStr.append(" max(distinct fn_countcaracter(centroresultadosuperior2.nivel, '.')) as qtdNivel ");
		sqlStr.append(" from centroresultado ");
		sqlStr.append(" inner join centroresultadosuperior as centroresultadosuperior on centroresultado.codigo = any(string_to_array(centroresultadosuperior.codigossuperiores, ',')::int[]) ");
		sqlStr.append(" inner join centroresultadosuperior as centroresultadosuperior2 on centroresultado.codigo = centroresultadosuperior2.codigo ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return ((Integer) Uteis.getSqlRowSetTotalizador(rs, "qtdNivel", TipoCampoEnum.INTEIRO));
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public CentroResultadoVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		return getAplicacaoControle().getCentroResultadoVO(codigoPrm, usuario);
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public CentroResultadoVO consultarPorChavePrimariaUnica(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) {
		try {
			ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
			String sql = "SELECT * FROM CentroResultado WHERE codigo = ?";
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, codigoPrm);
			if (!tabelaResultado.next()) {
				throw new StreamSeiException("Dados Não Encontrados ( CentroResultadoVO ).");
			}
			return (montarDados(tabelaResultado, nivelMontarDados, usuario));
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<CentroResultadoVO> montarDadosConsultaBasica(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) {
		List<CentroResultadoVO> vetResultado = new ArrayList<>();
		while (tabelaResultado.next()) {
			CentroResultadoVO obj = new CentroResultadoVO();
			montarDadosBasico(obj, tabelaResultado, nivelMontarDados, usuario);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	private void montarDadosBasico(CentroResultadoVO obj, SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) {
		try {
			obj.setNovoObj(Boolean.FALSE);
			obj.setCodigo(dadosSQL.getInt("centroResultado.codigo"));
			obj.setIdentificadorCentroResultado(dadosSQL.getString("centroResultado.identificadorCentroResultado"));
			obj.setDescricao(dadosSQL.getString("centroResultado.descricao"));
			if(Uteis.isAtributoPreenchido(dadosSQL.getString("centroResultado.restricaoUsoCentroResultado"))){
				obj.setRestricaoUsoCentroResultadoEnum(RestricaoUsoCentroResultadoEnum.valueOf(dadosSQL.getString("centroResultado.restricaoUsoCentroResultado")));	
			}
			
			obj.setSituacaoEnum(SituacaoEnum.valueOf(dadosSQL.getString("centroResultado.situacao")));
			if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
				return;
			}

			obj.getCentroResultadoPrincipal().setCodigo(dadosSQL.getInt("centroResultadoPrincipal.codigo"));
			if (Uteis.isAtributoPreenchido(obj.getCentroResultadoPrincipal())) {
				obj.getCentroResultadoPrincipal().setIdentificadorCentroResultado(dadosSQL.getString("centroResultadoPrincipal.identificadorCentroResultado"));
				obj.getCentroResultadoPrincipal().setDescricao(dadosSQL.getString("centroResultadoPrincipal.descricao"));
				if(Uteis.isAtributoPreenchido(dadosSQL.getString("centroResultadoPrincipal.restricaoUsoCentroResultado"))){
					obj.getCentroResultadoPrincipal().setRestricaoUsoCentroResultadoEnum(RestricaoUsoCentroResultadoEnum.valueOf(dadosSQL.getString("centroResultadoPrincipal.restricaoUsoCentroResultado")));
				}
				obj.getCentroResultadoPrincipal().setSituacaoEnum(SituacaoEnum.valueOf(dadosSQL.getString("centroResultadoPrincipal.situacao")));
			}
			if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSCONSULTA) {
				return;
			}
			if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
				return;
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	private CentroResultadoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) {
		try {
			CentroResultadoVO obj = new CentroResultadoVO();
			obj.setNovoObj(Boolean.FALSE);
			obj.setCodigo(dadosSQL.getInt("codigo"));
			obj.setIdentificadorCentroResultado(dadosSQL.getString("identificadorCentroResultado"));
			obj.setDescricao(dadosSQL.getString("descricao"));
			if(Uteis.isAtributoPreenchido(dadosSQL.getString("restricaoUsoCentroResultado"))){
				obj.setRestricaoUsoCentroResultadoEnum(RestricaoUsoCentroResultadoEnum.valueOf(dadosSQL.getString("restricaoUsoCentroResultado")));	
			}
			obj.setSituacaoEnum(SituacaoEnum.valueOf(dadosSQL.getString("situacao")));
			if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
				return obj;
			}
			obj.setCentroResultadoPrincipal(Uteis.montarDadosVO(dadosSQL.getInt("centroResultadoPrincipal"), CentroResultadoVO.class, p -> getFacadeFactory().getCentroResultadoFacade().consultarPorChavePrimaria(p, false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario)));
			obj.setListaCentroResultadoRestricaoUsoVOs(getFacadeFactory().getCentroResultadoRestricaoUsoFacade().consultaRapidaPorCentroResultado(obj, Uteis.NIVELMONTARDADOS_TODOS, usuario));
			return obj;
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<CentroResultadoVO> consultaResultadoEstoque(DataModelo dataModelo) {

		try {
			ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
			StringBuilder sql = new StringBuilder();
			sql.append("select centroResultado.codigo as \"centroResultado.codigo\", centroResultado.identificadorcentroresultado as \"centroResultado.identificadorCentroResultado\", centroResultado.descricao as \"centroResultado.descricao\", centroResultado.situacao as \"centroResultado.situacao\", centroResultado.restricaoUsoCentroResultado as \"centroResultado.restricaoUsoCentroResultado\" ");
			sql.append("from departamento ");
			sql.append("inner join centroresultado on centroresultado.codigo = departamento.centroresultado ");
			sql.append("where departamento.controlaestoque");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
			return montarDadosConsultaBasica(tabelaResultado, dataModelo.getNivelMontarDados(), dataModelo.getUsuario());
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	@Override
	public CentroResultadoVO consultarCentroResultadoArquivoExcel(String descricaoCentroResultado, Integer codigoUnidadeEnsino, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append(" select centroresultado.codigo as codigocentroresultado, centroresultado.descricao as descricaocentroresultado, 'DEPARTAMENTO' as tiponivelcentroresultado,  departamento.codigo as codigoorigem, departamento.nome nomeorigem, departamento.unidadeensino, 0 as curso, 0 as turno from departamento ");
		sqlStr.append("inner join centroresultado on centroresultado.codigo = departamento.centroresultado ");
		sqlStr.append("where centroresultado.descricao = '").append(descricaoCentroResultado).append("'");
		sqlStr.append("union all ");
		sqlStr.append("select centroresultado.codigo as codigocentroresultado, centroresultado.descricao as descricaocentroresultado, 'TURMA' as tiponivelcentroresultado,  turma.codigo as codigoorigem, turma.identificadorturma as nomeorigem, turma.unidadeensino, turma.curso, turma.turno from turma ");
		sqlStr.append("inner join centroresultado on centroresultado.codigo = turma.centroresultado  ");
		sqlStr.append("where centroresultado.descricao = '").append(descricaoCentroResultado).append("'");
		sqlStr.append("union all ");
		sqlStr.append("select centroresultado.codigo as codigocentroresultado, centroresultado.descricao as descricaocentroresultado, 'CURSO' as tiponivelcentroresultado,  unidadeensinocursocentroresultado.curso as codigoorigem, curso.nome as nomeorigem, unidadeensinocursocentroresultado.unidadeensino, unidadeensinocursocentroresultado.curso, 0 as turno from unidadeensinocursocentroresultado ");
		sqlStr.append("inner join centroresultado on centroresultado.codigo = unidadeensinocursocentroresultado.centroresultado "); 
		sqlStr.append("inner join curso on curso.codigo = unidadeensinocursocentroresultado.curso ");
		sqlStr.append("where centroresultado.descricao = '").append(descricaoCentroResultado).append("'");
		sqlStr.append("and unidadeensinocursocentroresultado.unidadeensino = ").append(codigoUnidadeEnsino);
		sqlStr.append("union all ");
		sqlStr.append("select centroresultado.codigo as codigocentroresultado, centroresultado.descricao as descricaocentroresultado, 'NIVEL_EDUCACIONAL' as tiponivelcentroresultado,  unidadeensinoniveleducacionalcentroresultado.codigo as codigoorigem, unidadeensinoniveleducacionalcentroresultado.tiponiveleducacional as nomeorigem, unidadeensinoniveleducacionalcentroresultado.unidadeensino, 0 as curso, 0 as turno from unidadeensinoniveleducacionalcentroresultado ");
		sqlStr.append("inner join centroresultado on centroresultado.codigo = unidadeensinoniveleducacionalcentroresultado.centroresultado ");
		sqlStr.append("where centroresultado.descricao = '").append(descricaoCentroResultado).append("'");
		sqlStr.append("and unidadeensinoniveleducacionalcentroresultado.unidadeensino = ").append(codigoUnidadeEnsino);
		sqlStr.append("union all ");
		sqlStr.append("select centroresultado.codigo as codigocentroresultado, centroresultado.descricao as descricaocentroresultado, 'UNIDADE_ENSINO' as tiponivelcentroresultado,  unidadeensino.codigo as codigoorigem, unidadeensino.NOME as nomeorigem, unidadeensino.CODIGO as unidadeensino, 0 as curso, 0 as turno ");
		sqlStr.append("from unidadeensino ");
		sqlStr.append("inner join centroresultado on centroresultado.codigo = unidadeensino.centroresultado ");
		
		sqlStr.append("where centroresultado.descricao = '").append(descricaoCentroResultado).append("' limit 1");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		
		
		if (tabelaResultado.next()) {
			CentroResultadoVO centroResultadoVO = new CentroResultadoVO();
			centroResultadoVO.setCodigo(tabelaResultado.getInt("codigocentroresultado"));
			centroResultadoVO.setDescricao(tabelaResultado.getString("descricaocentroresultado"));
			centroResultadoVO.setTipoNivelCentroResultado(tabelaResultado.getString("tiponivelcentroresultado"));
			centroResultadoVO.setCodigoOrigem(tabelaResultado.getInt("codigoorigem"));
			centroResultadoVO.setNomeOrigem(tabelaResultado.getString("nomeorigem"));
			if(tabelaResultado.getInt("unidadeensino")  != 0) {
				centroResultadoVO.getUnidadeEnsinoVO().setCodigo(tabelaResultado.getInt("unidadeensino"));
			}else {
				centroResultadoVO.getUnidadeEnsinoVO().setCodigo(codigoUnidadeEnsino);
			}
			centroResultadoVO.getCursoVO().setCodigo(tabelaResultado.getInt("curso"));
			centroResultadoVO.getTurnoVO().setCodigo(tabelaResultado.getInt("turno"));
			return centroResultadoVO;
		} else {
			return new CentroResultadoVO();
		}
		
	}
	
    public List<CentroResultadoOrigemVO> realizarLeituraArquivoExcel(FileUploadEvent upload, UnidadeEnsinoVO unidadeEnsinoVO,  UsuarioVO usuarioLogado ,  ConsistirException consistirException) throws Exception {
    	
        try {
            String extensao = (upload.getUploadedFile().getName().substring((upload.getUploadedFile().getName().lastIndexOf(".") + 1), upload.getUploadedFile().getName().length()));
            if (extensao.equals("xls") || extensao.equals("xlsx")) {
            	
                XSSFWorkbook workbook = new XSSFWorkbook(upload.getUploadedFile().getInputStream());
                if (workbook.getNumberOfSheets() > 1) {
                    throw new Exception("O arquivo informado possui mais de uma planilha. Remova as planilhas não utilizadas.");
                } else {
                   return realizarLeituraPlanilha(workbook.getSheetAt(0), unidadeEnsinoVO, usuarioLogado, consistirException);
                }
            } else {
                throw new Exception("Extensão do arquivo não é valida. Somente arquivos em excel serão processados.");
            }
           // verificarDuplicidadeNomeEmail(registroEntradaVO.getRegistroEntradaProspectsVOs(), listaErros);
                        
        } catch (Exception e) {
            throw e;
        }
    }
    
    public List<CentroResultadoOrigemVO> realizarLeituraPlanilha(final XSSFSheet planilha, final UnidadeEnsinoVO unidadeEnsinoVO,  final UsuarioVO usuarioLogado,  final ConsistirException consistirException) throws Exception {
    	int size = Iterators.size(planilha.iterator());
    	final List<CentroResultadoOrigemVO> centroResultadoOrigemVOs =  new ArrayList<CentroResultadoOrigemVO>(size);   	
    	consistirException.setListaMensagemErro(new ArrayList<String>(size));
        boolean comParalelismo = true;
        if(comParalelismo) {
        ProcessarParalelismo.executar(0, size, consistirException, new ProcessarParalelismo.Processo() {			
			@Override
			public void run(int i) {

				try {
					//System.out.println("linha: "+i);
					XSSFRow linha = planilha.getRow(i);
					if(linha != null) {
						realizarLeituraLinha(linha, unidadeEnsinoVO, centroResultadoOrigemVOs, usuarioLogado, i, consistirException.getListaMensagemErro());
					}
				} catch (Exception e) {
					//System.out.println("erro linha: "+i);
					consistirException.adicionarListaMensagemErro(e.getMessage());
				}
			 
			}
		});
        }else {
        	for(int i = 0; i<= size; i++) {
        		try {
					XSSFRow linha = planilha.getRow(i);
					if(linha != null) {
						if(realizarLeituraLinha(linha, unidadeEnsinoVO, centroResultadoOrigemVOs, usuarioLogado, i, consistirException.getListaMensagemErro())) {
							break;
						}
					}
				} catch (Exception e) {
					consistirException.adicionarListaMensagemErro(e.getMessage());
				}
        	}
        }
        centroResultadoOrigemVOs.removeIf(cro -> cro == null);
        if (Uteis.isAtributoPreenchido(consistirException.getListaMensagemErro())) {
        	consistirException.getListaMensagemErro().removeIf(erro -> erro == null);
        	consistirException.getListaMensagemErro().add(0, UteisJSF.internacionalizar("msg_Centro_Resultados_Erro"));			
					
		}
        return centroResultadoOrigemVOs;
        
    }
    
    public Boolean realizarLeituraLinha(XSSFRow linha, UnidadeEnsinoVO unidadeEnsinoVO, List<CentroResultadoOrigemVO> centroResultadoOrigemVOs, UsuarioVO usuarioLogado, int posicaoLinha, List<String> listaErrosEncontrados) throws Exception {
    	//NotaFiscalEntradaVO registroEntradaProspectsVO = new NotaFiscalEntradaVO();
       // ProspectsVO prospectsVO = new ProspectsVO();
       // prospectsVO.setTipoOrigemCadastro(TipoOrigemCadastroProspectEnum.REGISTROENTRADA);
    	CentroResultadoOrigemVO centroResultadoOrigemVO = new CentroResultadoOrigemVO();
        int posicaoColuna = 0;
        /* A variavel j vai somente ate a 5 pois nao preciso validar as proximas colunas */
        if (linha == null) {
            return true;
        }
//        for (int j = 0; j <= 1; j++) {
            XSSFCell celula = linha.getCell(0);
            if(celula.getCellType() != XSSFCell.CELL_TYPE_BLANK) {
            if (posicaoLinha == 0) {
                realizarValidacaoCabecarioExcel(celula);
            } else {
                posicaoColuna = executarPreencimentoCentroResultadoVO(centroResultadoOrigemVO, celula, usuarioLogado, 0, posicaoLinha, listaErrosEncontrados);
                if (posicaoColuna >= 1) {
                    return true;
                }
                celula = linha.getCell(1);
                posicaoColuna = executarPreencimentoCentroResultadoVO(centroResultadoOrigemVO, celula, usuarioLogado, 1, posicaoLinha, listaErrosEncontrados);
            }
            }else {
                return true;
            }
            
//        }
        if (posicaoLinha != 0) {
        	
        	adicionarCentroResultadoCentroResultadoOrigem(unidadeEnsinoVO, centroResultadoOrigemVOs, centroResultadoOrigemVO, usuarioLogado,listaErrosEncontrados, posicaoLinha);
        }
        return false;
    }
    
    public void realizarValidacaoCabecarioExcel(XSSFCell celula) throws Exception {
        if (celula == null) {
            throw new Exception("A planilha informada provavelmente possui menos colunas do que o padrão exigido pelo Sistema.");
        }
        if (celula.getReference().equals("A1") && !celula.getStringCellValue().trim().equalsIgnoreCase("centro resultado")) {
            throw new Exception("A informação da posição A1 do excel deve estar a palavra \"Centro Resultado\"");
        }
        if (celula.getReference().equals("B1") && !celula.getStringCellValue().trim().equalsIgnoreCase("valor")) {
            throw new Exception("A informação da posição B1 do excel deve estar a palavra \"Valor\"");
        }
    }
    
   public int executarPreencimentoCentroResultadoVO(CentroResultadoOrigemVO centroResultadoOrigemVO, XSSFCell celula, UsuarioVO usuarioLogado, int posicaoColuna, int posicaoLinha, List<String> listaErros) throws Exception {
        try {
        	
            if (posicaoColuna == 0) {
            	centroResultadoOrigemVO.getCentroResultadoAdministrativo().setDescricao(realizarObterValorString(celula, posicaoColuna, posicaoLinha, listaErros));

                if (centroResultadoOrigemVO.getCentroResultadoAdministrativo().getDescricao().equals("") || celula.getCellType() == XSSFCell.CELL_TYPE_BLANK) {
                    return 18;
                }
            }  else if (posicaoColuna == 1 ) { 
            	Double parseDouble = Double.parseDouble(realizarObterValorString(celula, posicaoColuna, posicaoLinha, listaErros));
            	centroResultadoOrigemVO.setValor(parseDouble);
            	if (parseDouble != null && parseDouble < 0.0) {
            		listaErros.add("Centro Resultado: " + centroResultadoOrigemVO.getCentroResultadoAdministrativo().getDescricao() + " possui valor negativo: " + Uteis.getDoubleFormatado(parseDouble));
            	}
            } 
        } catch (Exception e) {
            throw e;
        }
        return posicaoColuna;

    }
   
   public String realizarObterValorString(XSSFCell celula, int coluna, int linha, List<String> listaErros) throws Exception {
       if ((celula == null || XSSFCell.CELL_TYPE_BLANK == celula.getCellType()) && (coluna == 1)) {
           coluna++;
           linha++;
           listaErros.add("Dado inconsistente na linha: " + linha + ", coluna: " + coluna);
           coluna--;
           linha--;
           return null;
       } else if (celula == null || XSSFCell.CELL_TYPE_BLANK == celula.getCellType()) {
           return "";
       } else if (XSSFCell.CELL_TYPE_STRING == celula.getCellType()) {
           return celula.getStringCellValue();
       } else if (XSSFCell.CELL_TYPE_NUMERIC == celula.getCellType()) {
           return new BigDecimal(celula.getNumericCellValue()).toPlainString();
       } else {
           listaErros.add("Valor incorreto para coluna " + celula.getReference());
           return null;
       }
   }
      
   public synchronized void adicionarCentroResultadoCentroResultadoOrigem(UnidadeEnsinoVO unidadeEnsinoVO,  List<CentroResultadoOrigemVO> centroResultadoOrigemVOs, CentroResultadoOrigemVO centroResultadoOrigemVO, UsuarioVO usuarioLogado,List<String>listaErrosEncontrados, int linhaArquivo) throws Exception {
	 	  
		 try {
			 
			String descricaoCentroResultado = centroResultadoOrigemVO.getCentroResultadoAdministrativo().getDescricao();			
			CentroResultadoVO centroResultado = new CentroResultadoVO();
			centroResultado = getFacadeFactory().getCentroResultadoFacade().consultarCentroResultadoArquivoExcel(descricaoCentroResultado, unidadeEnsinoVO.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado);
							
			if (Uteis.isAtributoPreenchido(centroResultado)) {
				centroResultadoOrigemVO.setCentroResultadoAdministrativo(centroResultado);
				for(CentroResultadoOrigemVO centroResultadoOrigemVO2: centroResultadoOrigemVOs) {
					if(centroResultadoOrigemVO2 != null && centroResultadoOrigemVO2.getCentroResultadoAdministrativo().getCodigo().equals(centroResultado.getCodigo())) {
						centroResultadoOrigemVO2.setValor(centroResultadoOrigemVO2.getValor()+centroResultadoOrigemVO.getValor());
						return;
					}
				}
				centroResultadoOrigemVOs.add(centroResultadoOrigemVO);				
			}
			else {
				
				listaErrosEncontrados.add(descricaoCentroResultado);
				//throw new Exception("Não foi possível encontrar o centro de resultado "+descricaoCentroResultado +" no sistema.");
			}
		 
		 } catch (Exception e) {
			 throw e;
		}
	  }
	
   @Override
   public synchronized void realizarCarregamentoDadosCentroResultadoOrigemPadraoCategoriaDespesa(final CentroResultadoOrigemVO centroResultadoOrigemVO,final CategoriaDespesaVO categoriaDespesaVO, final UsuarioVO usuario) throws Exception{
	  
	   centroResultadoOrigemVO.setCategoriaDespesaVO(categoriaDespesaVO);
		centroResultadoOrigemVO.setQuantidade(1.0);
		centroResultadoOrigemVO
				.setTipoCentroResultadoOrigemEnum(TipoCentroResultadoOrigemEnum.NOTA_FISCAL_ENTRADA_ITEM);

		if(!centroResultadoOrigemVO.getCentroResultadoAdministrativo().getTipoNivelCentroResultado()
				.equals("UNIDADE_ENSINO") && !Uteis.isAtributoPreenchido(centroResultadoOrigemVO.getCentroResultadoAdministrativo()
				.getUnidadeEnsinoVO().getCodigo())) {
			throw new Exception("Não foi possível definir a UNIDADE DE ENSINO vinculado ao centro de resultado "+centroResultadoOrigemVO.getCentroResultadoAdministrativo().getDescricao()+".");
		}
		
		if (centroResultadoOrigemVO.getCentroResultadoAdministrativo().getTipoNivelCentroResultado()
				.equals("DEPARTAMENTO")) {
			centroResultadoOrigemVO.setDepartamentoVO(getFacadeFactory().getDepartamentoFacade().consultarPorChavePrimaria(
							centroResultadoOrigemVO.getCentroResultadoAdministrativo().getCodigoOrigem(), false,
							Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
			centroResultadoOrigemVO.setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade()
					.consultarPorChavePrimaria(centroResultadoOrigemVO.getCentroResultadoAdministrativo()
							.getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
							usuario));
			centroResultadoOrigemVO.setTipoNivelCentroResultadoEnum(TipoNivelCentroResultadoEnum.DEPARTAMENTO);
		} else if (centroResultadoOrigemVO.getCentroResultadoAdministrativo().getTipoNivelCentroResultado()
				.equals("TURMA")) {
			centroResultadoOrigemVO.setTurmaVO(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(
					centroResultadoOrigemVO.getCentroResultadoAdministrativo().getCodigoOrigem(),
					Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
			centroResultadoOrigemVO.getCursoVO().setCodigo(
					centroResultadoOrigemVO.getCentroResultadoAdministrativo().getCursoVO().getCodigo());
			centroResultadoOrigemVO.getTurnoVO().setCodigo(
					centroResultadoOrigemVO.getCentroResultadoAdministrativo().getTurnoVO().getCodigo());
			centroResultadoOrigemVO.setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade()
					.consultarPorChavePrimaria(centroResultadoOrigemVO.getCentroResultadoAdministrativo()
							.getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
							usuario));
			centroResultadoOrigemVO.setTipoNivelCentroResultadoEnum(TipoNivelCentroResultadoEnum.TURMA);
		} else if (centroResultadoOrigemVO.getCentroResultadoAdministrativo().getTipoNivelCentroResultado()
				.equals("CURSO")) {
			centroResultadoOrigemVO.setCursoVO(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(
					centroResultadoOrigemVO.getCentroResultadoAdministrativo().getCodigoOrigem(),
					Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, usuario));
			centroResultadoOrigemVO.setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade()
					.consultarPorChavePrimaria(centroResultadoOrigemVO.getCentroResultadoAdministrativo()
							.getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
							usuario));
			centroResultadoOrigemVO.setTipoNivelCentroResultadoEnum(TipoNivelCentroResultadoEnum.CURSO);
		} else if (centroResultadoOrigemVO.getCentroResultadoAdministrativo().getTipoNivelCentroResultado()
				.equals("NIVEL_EDUCACIONAL")) {
			centroResultadoOrigemVO.setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade()
					.consultarPorChavePrimaria(centroResultadoOrigemVO.getCentroResultadoAdministrativo()
							.getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
							usuario));
			centroResultadoOrigemVO
					.setTipoNivelCentroResultadoEnum(TipoNivelCentroResultadoEnum.UNIDADE_ENSINO);
		} else if (centroResultadoOrigemVO.getCentroResultadoAdministrativo().getTipoNivelCentroResultado()
				.equals("UNIDADE_ENSINO")) {
			centroResultadoOrigemVO
					.setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(
							centroResultadoOrigemVO.getCentroResultadoAdministrativo().getCodigoOrigem(), false,
							Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
			centroResultadoOrigemVO
					.setTipoNivelCentroResultadoEnum(TipoNivelCentroResultadoEnum.UNIDADE_ENSINO);
		}
   }
	
	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
	 */
	public static String getIdEntidade() {
		return CentroResultado.idEntidade;
	}

}
