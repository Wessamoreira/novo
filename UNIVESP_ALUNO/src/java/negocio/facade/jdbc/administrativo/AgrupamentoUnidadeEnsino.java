package negocio.facade.jdbc.administrativo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import controle.arquitetura.DataModelo;
import negocio.comuns.administrativo.AgrupamentoUnidadeEnsinoItemVO;
import negocio.comuns.administrativo.AgrupamentoUnidadeEnsinoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.administrativo.AgrupamentoUnidadeEnsinoInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class AgrupamentoUnidadeEnsino extends ControleAcesso implements AgrupamentoUnidadeEnsinoInterfaceFacade{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1307316821748541558L;
	private static String idEntidade = "AgrupamentoUnidadeEnsino";

	public static String getIdEntidade() {
		return AgrupamentoUnidadeEnsino.idEntidade;
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void valiarDados(AgrupamentoUnidadeEnsinoVO obj) throws Exception {
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getDescricao()), UteisJSF.internacionalizar("msg_AgrupamentoUnidadeEnsino_descricao"));		
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getAbreviatura()), UteisJSF.internacionalizar("msg_AgrupamentoUnidadeEnsino_abreviatura"));		
		Uteis.checkState(obj.getListaAgrupamentoUnidadeEnsinoItemVO().isEmpty(), UteisJSF.internacionalizar("msg_AgrupamentoUnidadeEnsino_existeAgrupamentoUnidadeEnsinoItem"));		
		validarUnicidade(obj, true);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(AgrupamentoUnidadeEnsinoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws ConsistirException, Exception{
		valiarDados(obj);
		if (obj.getCodigo() == 0) {
			incluir(obj, verificarAcesso, usuarioVO);
		} else {
			alterar(obj, verificarAcesso, usuarioVO);
		}
		validarSeRegistroForamExcluidoDasListaSubordinadas(obj.getListaAgrupamentoUnidadeEnsinoItemVO(), "agrupamentoUnidadeEnsinoItem", idEntidade, obj.getCodigo(), usuarioVO);
		getFacadeFactory().getAgrupamentoUnidadeEnsinoItemFacade().persistir(obj.getListaAgrupamentoUnidadeEnsinoItemVO(), verificarAcesso, usuarioVO);
		
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final AgrupamentoUnidadeEnsinoVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			incluir(getIdEntidade(), verificarAcesso, usuario);
			incluir(obj, "agrupamentounidadeensino", new AtributoPersistencia()
					.add("descricao", obj.getDescricao())					
					.add("abreviatura", obj.getAbreviatura())					
					.add("statusAtivoInativoEnum", obj.getStatusAtivoInativoEnum())
					, usuario);
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw new StreamSeiException(e);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final AgrupamentoUnidadeEnsinoVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			alterar(getIdEntidade(), verificarAcesso, usuario);
			alterar(obj, "agrupamentounidadeensino", new AtributoPersistencia()
					.add("descricao", obj.getDescricao())					
					.add("abreviatura", obj.getAbreviatura())					
					.add("statusAtivoInativoEnum", obj.getStatusAtivoInativoEnum())
					, new AtributoPersistencia().add("codigo", obj.getCodigo()), usuario);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void atualizarStatusAgrupamentoUnidadeEnsino(final AgrupamentoUnidadeEnsinoVO obj, StatusAtivoInativoEnum statusAtivoInativoEnum, boolean verificarAcesso, UsuarioVO usuario) throws ConsistirException, Exception {
		try {
			alterar(getIdEntidade(), verificarAcesso, usuario);
			if(statusAtivoInativoEnum.isAtivo()) {
				validarUnicidade(obj, true);	
			}
			atualizar("agrupamentounidadeensino"
					, new AtributoPersistencia().add("statusAtivoInativoEnum", statusAtivoInativoEnum.name())
					, new AtributoPersistencia().add("codigo", obj.getCodigo()), usuario);
		} catch (Exception e) {
			throw e;
		}
	}
	

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(AgrupamentoUnidadeEnsinoVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		excluir(getIdEntidade(), verificarAcesso, usuario);
		getFacadeFactory().getSalaAulaBlackboardFacade().consultarSeExisteAgrupamentoUnidadeEnsino(obj.getCodigo(), true, usuario);
		getConexao().getJdbcTemplate().update("DELETE FROM agrupamentounidadeensino WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario), obj.getCodigo());
	}
	
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void realizarPreenchimentoAgrupamentoUnidadeEnsinoItemVO(AgrupamentoUnidadeEnsinoVO obj,  List<UnidadeEnsinoVO> listaUnidadeEnsino, UsuarioVO usuario) {
		obj.getListaAgrupamentoUnidadeEnsinoItemVO().removeIf(auei-> !listaUnidadeEnsino.stream().filter(p-> p.getCodigo().equals(auei.getUnidadeEnsinoVO().getCodigo())).findFirst().get().getFiltrarUnidadeEnsino());
		
		listaUnidadeEnsino.stream()
		.filter(p-> p.getFiltrarUnidadeEnsino() 
				&& obj.getListaAgrupamentoUnidadeEnsinoItemVO().stream().noneMatch(auei-> auei.getUnidadeEnsinoVO().getCodigo().equals(p.getCodigo())))
		.forEach(p->{
			AgrupamentoUnidadeEnsinoItemVO auei = new AgrupamentoUnidadeEnsinoItemVO();
			auei.setUnidadeEnsinoVO(p);
			auei.setAgrupamentoUnidadeEnsinoVO(obj);
			obj.getListaAgrupamentoUnidadeEnsinoItemVO().add(auei);
		});
		
	}
	

	
	

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void consultar(DataModelo dataModelo, AgrupamentoUnidadeEnsinoVO obj) throws Exception {
		dataModelo.getListaConsulta().clear();
		dataModelo.getListaFiltros().clear();
		dataModelo.setListaConsulta(consultaRapidaPorFiltros(obj, dataModelo));
		dataModelo.getListaFiltros().clear();
		dataModelo.setTotalRegistrosEncontrados(consultarTotalPorFiltros(obj, dataModelo));
	}

	private List<AgrupamentoUnidadeEnsinoVO> consultaRapidaPorFiltros(AgrupamentoUnidadeEnsinoVO obj, DataModelo dataModelo) {
		try {
			getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" WHERE 1=1 ");
			montarFiltrosParaConsulta(obj, dataModelo, sqlStr);
			sqlStr.append(" ORDER BY agrupamentoUnidadeEnsino.codigo desc ");
			UteisTexto.addLimitAndOffset(sqlStr, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), dataModelo.getListaFiltros().toArray());
			return montarDadosConsulta(tabelaResultado, dataModelo.getNivelMontarDados(), dataModelo.getUsuario());
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}


	private void montarFiltrosParaConsulta(AgrupamentoUnidadeEnsinoVO obj, DataModelo dataModelo, StringBuilder sqlStr) {
		if (Uteis.isAtributoPreenchido(obj.getCodigo())) {
			sqlStr.append(" and agrupamentoUnidadeEnsino.codigo = ? ");
			dataModelo.getListaFiltros().add(obj.getCodigo());
		}
		if (Uteis.isAtributoPreenchido(obj.getDescricao())) {
			sqlStr.append(" and lower(sem_acentos(agrupamentoUnidadeEnsino.descricao)) like(lower(sem_acentos(?)))");
			dataModelo.getListaFiltros().add(PERCENT + obj.getDescricao().toLowerCase() + PERCENT);
		}
		if (Uteis.isAtributoPreenchido(obj.getAbreviatura())) {
			sqlStr.append(" and lower(sem_acentos(agrupamentoUnidadeEnsino.abreviatura)) like(lower(sem_acentos(?)))");
			dataModelo.getListaFiltros().add(PERCENT + obj.getAbreviatura().toLowerCase() + PERCENT);
		}
		if (Uteis.isAtributoPreenchido(obj.getStatusAtivoInativoEnum())) {
			sqlStr.append(" and agrupamentoUnidadeEnsino.statusAtivoInativoEnum = ? ");
			dataModelo.getListaFiltros().add(obj.getStatusAtivoInativoEnum().name());
		}
		if (Uteis.isAtributoPreenchido(obj.getFiltroNomeUnidadeEnsino())) {
			sqlStr.append(" and lower(sem_acentos(unidadeensino.nome)) like(lower(sem_acentos(?)))");
			dataModelo.getListaFiltros().add(PERCENT + obj.getFiltroNomeUnidadeEnsino().toLowerCase() + PERCENT);
		}
	}
	

	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public AgrupamentoUnidadeEnsinoVO consultarPorUnidadeEnsino(Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) {
		try {
			getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();		
			sqlStr.append(" where unidadeensino.codigo = ? ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), unidadeEnsino);
			if (!tabelaResultado.next()) {
				return new AgrupamentoUnidadeEnsinoVO();
			}
			return (montarDados(tabelaResultado, nivelMontarDados, usuario));
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public AgrupamentoUnidadeEnsinoVO consultarPorDescricao(String descricao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) {
		try {
			getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" where lower(sem_acentos(agrupamentoUnidadeEnsino.descricao)) = (lower(sem_acentos(?))) ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), descricao);
			if (!tabelaResultado.next()) {
				return new AgrupamentoUnidadeEnsinoVO();
			}
			return (montarDados(tabelaResultado, nivelMontarDados, usuario));
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public AgrupamentoUnidadeEnsinoVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) {
		try {
			getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
			StringBuilder sqlStr = getSQLPadraoConsultaCompleto();
			sqlStr.append(" where agrupamentoUnidadeEnsino.codigo = ? ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), codigoPrm);
			if (!tabelaResultado.next()) {
				throw new StreamSeiException("Dados Não Encontrados ( AgrupamentoUnidadeEnsinoVO ).");
			}
			tabelaResultado.beforeFirst();
			return (montarDadosCompleto(tabelaResultado, nivelMontarDados, usuario));
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	private boolean validarUnicidade(AgrupamentoUnidadeEnsinoVO obj, boolean isLevantarExcecao) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" select agrupamentoUnidadeEnsinoItem.codigo, unidadeensino.nome , agrupamentoUnidadeEnsino.abreviatura ");
		sql.append(" from agrupamentoUnidadeEnsinoItem ");
		sql.append(" inner join unidadeEnsino on  unidadeEnsino.codigo = agrupamentoUnidadeEnsinoItem.unidadeEnsino ");
		sql.append(" inner join agrupamentoUnidadeEnsino on  agrupamentoUnidadeEnsinoItem.agrupamentoUnidadeEnsino = agrupamentoUnidadeEnsino.codigo ");
		sql.append(" where agrupamentoUnidadeEnsinoItem.unidadeensino in (").append(UteisTexto.converteListaEntidadeCampoCodigoParaCondicaoIn(obj.getListaAgrupamentoUnidadeEnsinoItemVO(), "unidadeEnsinoVO.codigo")).append(") ");
		sql.append(" and agrupamentoUnidadeEnsino.statusAtivoInativoEnum = '").append(StatusAtivoInativoEnum.ATIVO).append("'");
		if (Uteis.isAtributoPreenchido(obj.getCodigo())) {
			sql.append(" and agrupamentoUnidadeEnsino.codigo != ").append(obj.getCodigo()).append(" ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		boolean existeUnicidade = tabelaResultado.next();
		if (existeUnicidade && !isLevantarExcecao ) {
			return true;
		}else if(existeUnicidade && isLevantarExcecao ) {
			ConsistirException ce = new ConsistirException();
			tabelaResultado.beforeFirst();
			while (tabelaResultado.next()) {
				AgrupamentoUnidadeEnsinoItemVO auei = new AgrupamentoUnidadeEnsinoItemVO();
				auei.setCodigo(tabelaResultado.getInt("codigo"));
				auei.getAgrupamentoUnidadeEnsinoVO().setAbreviatura(tabelaResultado.getString("abreviatura"));
				auei.getUnidadeEnsinoVO().setNome(tabelaResultado.getString("nome"));
				ce.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_AgrupamentoUnidadeEnsino_validarUnicidade").replace("{0}", auei.getAgrupamentoUnidadeEnsinoVO().getAbreviatura().toUpperCase()).replace("{1}", auei.getUnidadeEnsinoVO().getNome().toUpperCase()));
			}
			throw ce;
		}
		return false ;
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private StringBuilder getSQLPadraoConsultaBasica() {
		StringBuilder sql = new StringBuilder("select distinct ");
		sql.append(" agrupamentoUnidadeEnsino.codigo, ");
		sql.append(" agrupamentoUnidadeEnsino.descricao, ");
		sql.append(" agrupamentoUnidadeEnsino.abreviatura, ");
		sql.append(" agrupamentoUnidadeEnsino.statusAtivoInativoEnum ");
		sql.append(" FROM agrupamentoUnidadeEnsino ");
		sql.append(" left join agrupamentoUnidadeEnsinoItem on  agrupamentoUnidadeEnsinoItem.agrupamentoUnidadeEnsino = agrupamentoUnidadeEnsino.codigo ");
		sql.append(" left join unidadeensino on  unidadeensino.codigo = agrupamentoUnidadeEnsinoItem.unidadeensino ");
		return sql;
	}
	
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private Integer consultarTotalPorFiltros(AgrupamentoUnidadeEnsinoVO obj, DataModelo dataModelo) {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append(" select count (distinct  agrupamentoUnidadeEnsino.codigo) as QTDE ");
			sql.append(" FROM agrupamentoUnidadeEnsino ");
			sql.append(" left join agrupamentoUnidadeEnsinoItem on  agrupamentoUnidadeEnsinoItem.agrupamentoUnidadeEnsino = agrupamentoUnidadeEnsino.codigo ");
			sql.append(" left join unidadeensino on  unidadeensino.codigo = agrupamentoUnidadeEnsinoItem.unidadeensino ");
			sql.append(" WHERE 1= 1 ");
			montarFiltrosParaConsulta(obj, dataModelo, sql);
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), dataModelo.getListaFiltros().toArray());
			return (Integer) Uteis.getSqlRowSetTotalizador(rs, Uteis.QTDE, TipoCampoEnum.INTEIRO);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private StringBuilder getSQLPadraoConsultaCompleto() {
		StringBuilder sql = new StringBuilder("select ");
		sql.append(" agrupamentoUnidadeEnsino.codigo, agrupamentoUnidadeEnsino.descricao, ");
		sql.append(" agrupamentoUnidadeEnsino.abreviatura, agrupamentoUnidadeEnsino.statusAtivoInativoEnum, ");

		sql.append(" agrupamentoUnidadeEnsinoItem.codigo as \"agrupamentoUnidadeEnsinoItem.codigo\",  ");
		
		sql.append(" unidadeensino.codigo as \"unidadeensino.codigo\", unidadeensino.nome as \"unidadeensino.nome\",  ");
		sql.append(" unidadeensino.cnpj as \"unidadeensino.cnpj\", unidadeensino.abreviatura as \"unidadeensino.abreviatura\"  ");
		
		sql.append(" FROM agrupamentoUnidadeEnsino ");
		sql.append(" inner join agrupamentoUnidadeEnsinoItem on  agrupamentoUnidadeEnsinoItem.agrupamentoUnidadeEnsino = agrupamentoUnidadeEnsino.codigo ");
		sql.append(" inner join unidadeensino on  unidadeensino.codigo = agrupamentoUnidadeEnsinoItem.unidadeensino ");
		return sql;
	}
	

	private List<AgrupamentoUnidadeEnsinoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<AgrupamentoUnidadeEnsinoVO> vetResultado = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	private AgrupamentoUnidadeEnsinoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		AgrupamentoUnidadeEnsinoVO obj = new AgrupamentoUnidadeEnsinoVO();
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setDescricao(dadosSQL.getString("descricao"));
		obj.setAbreviatura(dadosSQL.getString("abreviatura"));
		obj.setStatusAtivoInativoEnum(StatusAtivoInativoEnum.valueOf(dadosSQL.getString("statusAtivoInativoEnum")));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSCONSULTA) {
			return obj;
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		return obj;

	}
	
	
	private AgrupamentoUnidadeEnsinoVO montarDadosCompleto(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		AgrupamentoUnidadeEnsinoVO obj = new AgrupamentoUnidadeEnsinoVO();
		while (dadosSQL.next()) {
			if(!Uteis.isAtributoPreenchido(obj)){
				obj.setNovoObj(Boolean.FALSE);
				obj.setCodigo(dadosSQL.getInt("codigo"));
				obj.setDescricao(dadosSQL.getString("descricao"));
				obj.setAbreviatura(dadosSQL.getString("abreviatura"));
				obj.setStatusAtivoInativoEnum(StatusAtivoInativoEnum.valueOf(dadosSQL.getString("statusAtivoInativoEnum")));		
			}
			AgrupamentoUnidadeEnsinoItemVO auei = buscarAgrupamentoUnidadeEnsinoItemVO(dadosSQL.getInt("agrupamentoUnidadeEnsinoItem.codigo"), obj);
			auei.setNovoObj(Boolean.FALSE);
			auei.setCodigo(dadosSQL.getInt("agrupamentoUnidadeEnsinoItem.codigo"));
			auei.getUnidadeEnsinoVO().setCodigo(dadosSQL.getInt("unidadeensino.codigo"));
			auei.getUnidadeEnsinoVO().setNome(dadosSQL.getString("unidadeensino.nome"));
			auei.getUnidadeEnsinoVO().setAbreviatura(dadosSQL.getString("unidadeensino.abreviatura"));
			auei.getUnidadeEnsinoVO().setCNPJ(dadosSQL.getString("unidadeensino.cnpj"));
			auei.setAgrupamentoUnidadeEnsinoVO(obj);
			obj.getListaAgrupamentoUnidadeEnsinoItemVO().add(auei);
		}
		return obj;		
	}
	
	private AgrupamentoUnidadeEnsinoItemVO buscarAgrupamentoUnidadeEnsinoItemVO(Integer codigo, AgrupamentoUnidadeEnsinoVO obj) {
		return obj.getListaAgrupamentoUnidadeEnsinoItemVO()
				.stream()
				.filter(objsExistente -> objsExistente.getCodigo().equals(codigo))
				.findFirst()
				.orElse(new AgrupamentoUnidadeEnsinoItemVO());
		
	}

}
