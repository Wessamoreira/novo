package negocio.facade.jdbc.estagio;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.enumeradores.TipoEstagioEnum;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;
import negocio.comuns.estagio.GrupoPessoaItemVO;
import negocio.comuns.estagio.GrupoPessoaVO;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.estagio.GrupoPessoaItemInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class GrupoPessoaItem extends ControleAcesso implements GrupoPessoaItemInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6300725396309830573L;
	private static String idEntidade = "GrupoPessoa";

	public static String getIdEntidade() {
		return GrupoPessoaItem.idEntidade;
	}
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(List<GrupoPessoaItemVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO) {
		try {
			for (GrupoPessoaItemVO obj : lista) {
				if (obj.getCodigo() == 0) {
					incluir(obj, verificarAcesso, usuarioVO);
				} else {
					alterar(obj, verificarAcesso, usuarioVO);
				}	
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);			
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final GrupoPessoaItemVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			GrupoPessoaItem.incluir(getIdEntidade(), verificarAcesso, usuario);
			incluir(obj, "grupoPessoaItem", new AtributoPersistencia()
					.add("grupoPessoa", obj.getGrupoPessoaVO())
					.add("pessoa", obj.getPessoaVO())
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
	public void alterar(final GrupoPessoaItemVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			GrupoPessoaItem.alterar(getIdEntidade(), verificarAcesso, usuario);
			alterar(obj, "grupoPessoaItem", new AtributoPersistencia()
					.add("grupoPessoa", obj.getGrupoPessoaVO())
					.add("pessoa", obj.getPessoaVO())
					.add("statusAtivoInativoEnum", obj.getStatusAtivoInativoEnum())
					,new AtributoPersistencia().add("codigo", obj.getCodigo()), usuario);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void atualizarCampoStatus(final GrupoPessoaItemVO obj, StatusAtivoInativoEnum statusAtivoInativo, UsuarioVO usuario) {
		try {
			if(Uteis.isAtributoPreenchido(obj.getCodigo())) {
			alterar(obj, "grupoPessoaItem", new AtributoPersistencia()
					.add("statusAtivoInativoEnum", statusAtivoInativo)
					,new AtributoPersistencia().add("codigo", obj.getCodigo()), usuario);
			}else if(Uteis.isAtributoPreenchido(obj.getPessoaVO().getCodigo())) { 
				alterar(obj, "grupoPessoaItem", new AtributoPersistencia()
						.add("statusAtivoInativoEnum", statusAtivoInativo)
						,new AtributoPersistencia().add("pessoa", obj.getPessoaVO().getCodigo()), usuario);
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void inativarGrupoPessoaItemVO(final GrupoPessoaItemVO obj, UsuarioVO usuario) throws Exception {
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getCodigo()), "Não é possível realizar essa operação pois o mesmo ainda não foi gravado.");
		obj.setStatusAtivoInativoEnum(StatusAtivoInativoEnum.INATIVO);		
		atualizarCampoStatus(obj, obj.getStatusAtivoInativoEnum(), usuario);
		if(Uteis.isAtributoPreenchido(obj.getQtdeEstagioObrigatorio())) {
			getFacadeFactory().getEstagioFacade().realizarDistribuicaoGrupoPessoaItemPorInativacao(obj, usuario);	
		}
	}
	
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private StringBuilder getSQLPadraoConsulta() {
		StringBuilder sql = new StringBuilder("select ");
		sql.append(" grupoPessoaItem.codigo as \"grupoPessoaItem.codigo\",  ");
		sql.append(" grupoPessoaItem.statusAtivoInativoEnum as \"grupoPessoaItem.statusAtivoInativoEnum\",  ");
		sql.append(" grupoPessoaItem.grupoPessoa as \"grupoPessoaItem.grupoPessoa\",  ");
		
		sql.append(" pessoa.codigo as \"pessoa.codigo\", ");
		sql.append(" pessoa.nome as \"pessoa.nome\", ");
		sql.append(" pessoa.cpf as \"pessoa.cpf\", ");
		sql.append(" pessoa.email as \"pessoa.email\", ");
		sql.append(" pessoa.email2 as \"pessoa.email2\", ");
		sql.append(" pessoa.funcionario as \"pessoa.funcionario\", ");
		sql.append(" pessoa.aluno as \"pessoa.aluno\" ");
		
		sql.append(" FROM grupoPessoaItem ");
		sql.append(" left join pessoa on pessoa.codigo = grupoPessoaItem.pessoa ");
		return sql;
	}
	
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private StringBuilder getSQLPadraoConsultaBasica() {
		StringBuilder sql = new StringBuilder("select COUNT(*) OVER() as totalRegistroConsulta, ");
		sql.append(" grupoPessoaItem.codigo as \"grupoPessoaItem.codigo\",  ");
		sql.append(" grupoPessoaItem.statusAtivoInativoEnum as \"grupoPessoaItem.statusAtivoInativoEnum\",  ");
		
		sql.append(" grupoPessoa.codigo as \"grupoPessoa.codigo\",  ");
		sql.append(" grupoPessoa.nome as \"grupoPessoa.nome\",  ");
		
		sql.append(" pessoa.codigo as \"pessoa.codigo\", ");
		sql.append(" pessoa.nome as \"pessoa.nome\", ");
		sql.append(" pessoa.cpf as \"pessoa.cpf\", ");
		sql.append(" pessoa.email as \"pessoa.email\", ");
		sql.append(" pessoa.email2 as \"pessoa.email2\", ");
		sql.append(" pessoa.funcionario as \"pessoa.funcionario\", ");
		sql.append(" pessoa.aluno as \"pessoa.aluno\" ");
		sql.append(" FROM grupoPessoaItem ");
		sql.append(" inner join grupoPessoa on grupoPessoa.codigo = grupoPessoaItem.grupoPessoa ");
		sql.append(" inner join pessoa on pessoa.codigo = grupoPessoaItem.pessoa ");
		sql.append(" left join pessoaemailinstitucional on pessoaemailinstitucional.pessoa = pessoa.codigo ");
		return sql;
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public Integer consultarQuantidadeEstagioObrigatorioPorGrupoPessoaItemVO(GrupoPessoaItemVO obj, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder(" select count(estagio.codigo) as QTDE from estagio ");
		sql.append(" inner join grupopessoaitem on estagio.grupopessoaitem  = grupopessoaitem.codigo");
		sql.append(" where grupopessoaitem.codigo  = ? ");
		sql.append(" and estagio.tipoestagio != ? ");
		sql.append(" and estagio.situacaoestagioenum in ('EM_ANALISE', 'EM_CORRECAO')  ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), obj.getCodigo(), TipoEstagioEnum.NAO_OBRIGATORIO.name());
		return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, Uteis.QTDE, TipoCampoEnum.INTEIRO);
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public Integer consultarQuantidadeEstagioNaoObrigatorioPorGrupoPessoaItemVO(GrupoPessoaItemVO obj, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder(" select count(estagio.codigo) as QTDE from estagio ");
		sql.append(" inner join grupopessoaitem on estagio.grupopessoaitem  = grupopessoaitem.codigo");
		sql.append(" where grupopessoaitem.codigo  = ? ");
		sql.append(" and estagio.tipoestagio = ? ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), obj.getCodigo(), TipoEstagioEnum.NAO_OBRIGATORIO.name());
		return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, Uteis.QTDE, TipoCampoEnum.INTEIRO);
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public GrupoPessoaItemVO buscarGrupoPessoaItemDistribuicaoQuantitativoPorEstagio(TipoEstagioEnum tipoEstagioEnum, String matricula, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringJoiner sql = new StringJoiner(" ");
		sql.add(" select grupopessoaitem.codigo, pessoa.nome, count(estagio.grupopessoaitem) as qtd ");
		sql.add(" from grupopessoaitem ");
		sql.add(" inner join pessoa on pessoa.codigo = grupopessoaitem.pessoa ");		
		if (tipoEstagioEnum.isTipoObrigatorio()){
			sql.add(" inner join curso on curso.grupopessoaanaliserelatoriofinalestagio = grupopessoaitem.grupopessoa ");
		}else if (tipoEstagioEnum.isTipoObrigatorioEquivalencia()){
			sql.add(" inner join curso on curso.grupopessoaanaliseequivalenciaestagioobrigatorio = grupopessoaitem.grupopessoa ");			
		}else if (tipoEstagioEnum.isTipoObrigatorioAproveitamento()){
			sql.add(" inner join curso on curso.grupopessoaanaliseaproveitamentoestagioobrigatorio = grupopessoaitem.grupopessoa ");
		}	
		sql.add(" inner join matricula on matricula.curso = curso.codigo ");
		sql.add(" left join estagio on estagio.grupopessoaitem = grupopessoaitem.codigo and estagio.situacaoestagioenum not in('DEFERIDO', 'INDEFERIDO') ");
		sql.add(" WHERE grupopessoaitem.statusAtivoInativoEnum = ? ");
		sql.add(" and  matricula.matricula =  ? ");
		sql.add(" group by grupopessoaitem.codigo, pessoa.nome  ");
		sql.add(" order by count(estagio.grupopessoaitem), pessoa.nome ");
		sql.add(" limit 1 ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), StatusAtivoInativoEnum.ATIVO.name(), matricula);
		if (tabelaResultado.next()) {
			return consultarPorChavePrimaria(tabelaResultado.getInt("codigo"), nivelMontarDados, usuario);
		}
		return new GrupoPessoaItemVO();
	}
	
	
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public GrupoPessoaItemVO buscarGrupoPessoaItemDistribuicaoQuantitativoPorGrupoPessoaItemInativo(GrupoPessoaItemVO grupoPessoaItem, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringJoiner sql = new StringJoiner(" ");
		sql.add(" select grupopessoaitem.codigo, pessoa.nome, count(estagio.grupopessoaitem) as qtd ");
		sql.add(" from grupopessoaitem ");
		sql.add(" inner join pessoa on pessoa.codigo = grupopessoaitem.pessoa ");
		sql.add(" left join estagio on estagio.grupopessoaitem = grupopessoaitem.codigo and estagio.situacaoestagioenum not in('DEFERIDO', 'INDEFERIDO') ");
		sql.add(" WHERE grupopessoaitem.statusAtivoInativoEnum = ? ");
		sql.add(" and  grupopessoaitem.codigo != ? ");
		sql.add(" and  grupopessoaitem.grupopessoa = ? ");
		sql.add(" group by grupopessoaitem.codigo, pessoa.nome  ");
		sql.add(" order by count(estagio.grupopessoaitem), pessoa.nome ");
		sql.add(" limit 1 ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), StatusAtivoInativoEnum.ATIVO.name(), grupoPessoaItem.getCodigo(), grupoPessoaItem.getGrupoPessoaVO().getCodigo());
		if (tabelaResultado.next()) {
			return consultarPorChavePrimaria(tabelaResultado.getInt("codigo"), nivelMontarDados, usuario);
		}
		return new GrupoPessoaItemVO();
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void consultar(DataModelo dataModelo, GrupoPessoaItemVO obj) throws Exception {
		dataModelo.getListaConsulta().clear();
		dataModelo.getListaFiltros().clear();
		dataModelo.setListaConsulta(consultaRapidaPorFiltros(obj, dataModelo));
	}

	private List<GrupoPessoaItemVO> consultaRapidaPorFiltros(GrupoPessoaItemVO obj, DataModelo dataModelo) {
		try {
			ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" WHERE 1=1 ");
			montarFiltrosParaConsulta(obj, dataModelo, sqlStr);
			sqlStr.append(" ORDER BY grupoPessoaItem.codigo desc ");
			UteisTexto.addLimitAndOffset(sqlStr, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), dataModelo.getListaFiltros().toArray());
			montarTotalizadorConsultaBasica(dataModelo, tabelaResultado);
			return montarDadosConsultaBasica(tabelaResultado, dataModelo.getNivelMontarDados(), dataModelo.getUsuario());
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	private void montarFiltrosParaConsulta(GrupoPessoaItemVO obj, DataModelo dataModelo, StringBuilder sqlStr) {
		if (Uteis.isAtributoPreenchido(obj.getGrupoPessoaVO().getNome())) {
			sqlStr.append(" and lower(sem_acentos(grupoPessoa.nome)) like(lower(sem_acentos(?)))");
			dataModelo.getListaFiltros().add(PERCENT + obj.getGrupoPessoaVO().getNome().toLowerCase() + PERCENT);
		}
		if (Uteis.isAtributoPreenchido(obj.getPessoaVO().getNome())) {
			sqlStr.append(" and ((");
			sqlStr.append(" lower(sem_acentos(pessoa.nome)) like(lower(sem_acentos(?)))");
			dataModelo.getListaFiltros().add(PERCENT + obj.getPessoaVO().getNome().toLowerCase() + PERCENT);
			sqlStr.append(" ) or ( ");
			sqlStr.append(" lower(sem_acentos(pessoaemailinstitucional.nome)) like(lower(sem_acentos(?)))");
			dataModelo.getListaFiltros().add(PERCENT + obj.getPessoaVO().getNome().toLowerCase() + PERCENT);
			sqlStr.append(" )) ");
		}
		if (Uteis.isAtributoPreenchido(obj.getPessoaVO().getCPF())) {
			sqlStr.append(" and (replace(replace((pessoa.cpf),'.',''),'-','')) LIKE(?) ");
			dataModelo.getListaFiltros().add(PERCENT + Uteis.retirarMascaraCPF(obj.getPessoaVO().getCPF()) + PERCENT);
		}
		if (Uteis.isAtributoPreenchido(obj.getPessoaVO().getEmail())) {
			sqlStr.append(" and ((");
			sqlStr.append(" lower(sem_acentos(pessoa.email)) like(lower(sem_acentos(?)))");
			dataModelo.getListaFiltros().add(PERCENT + obj.getPessoaVO().getEmail().toLowerCase() + PERCENT);
			sqlStr.append(" ) or ( ");
			sqlStr.append(" lower(sem_acentos(pessoaemailinstitucional.email)) like(lower(sem_acentos(?)))");
			dataModelo.getListaFiltros().add(PERCENT + obj.getPessoaVO().getEmail().toLowerCase() + PERCENT);
			sqlStr.append(" )) ");
		}
	}
	
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<GrupoPessoaItemVO> consultarPorGrupoPessoaItemVOExistenteEstagio(Integer pessoa, int nivelMontarDados, UsuarioVO usuario) {
		try {
			StringBuilder sqlStr = getSQLPadraoConsulta();
			sqlStr.append(" inner join  estagio on estagio.grupoPessoaItem = grupoPessoaItem.codigo ");
			sqlStr.append(" where 1=1 ");
			if(Uteis.isAtributoPreenchido(pessoa)) {
				sqlStr.append(" and pessoa.codigo = ").append(pessoa);
			}
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			List<GrupoPessoaItemVO> vetResultado = new ArrayList<>();
			while (tabelaResultado.next()) {
				GrupoPessoaItemVO gpi = new GrupoPessoaItemVO();
				montarDados(gpi, tabelaResultado, nivelMontarDados, usuario);
				vetResultado.add(gpi);
			}
			return vetResultado;
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<GrupoPessoaItemVO> consultarPorGrupoPessoaVO(GrupoPessoaVO obj, StatusAtivoInativoEnum statusAtivoInativoEnum, int nivelMontarDados, UsuarioVO usuario) {
		try {
			StringBuilder sqlStr = getSQLPadraoConsulta();
			sqlStr.append(" where grupoPessoaItem.grupoPessoa = ? ");
			if(Uteis.isAtributoPreenchido(statusAtivoInativoEnum)) {
				sqlStr.append(" and grupoPessoaItem.statusAtivoInativoEnum = '").append(statusAtivoInativoEnum.name()).append("' ");	
			}
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), obj.getCodigo());
			List<GrupoPessoaItemVO> vetResultado = new ArrayList<>();
			while (tabelaResultado.next()) {
				GrupoPessoaItemVO gpi = new GrupoPessoaItemVO();
				montarDados(gpi, tabelaResultado, nivelMontarDados, usuario);
				gpi.setGrupoPessoaVO(obj);
				vetResultado.add(gpi);
			}
			return vetResultado;
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public GrupoPessoaItemVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) {
		try {
			StringBuilder sqlStr = getSQLPadraoConsulta();
			sqlStr.append(" where grupoPessoaItem.codigo = ? ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), codigoPrm);
			if (!tabelaResultado.next()) {
				throw new StreamSeiException("Dados Não Encontrados ( GrupoPessoaItemVO ).");
			}
			GrupoPessoaItemVO obj = new GrupoPessoaItemVO();
			montarDados(obj, tabelaResultado, nivelMontarDados, usuario);
			return obj;
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<GrupoPessoaItemVO> montarDados(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<GrupoPessoaItemVO> vetResultado = new ArrayList<>();
		while (tabelaResultado.next()) {
			GrupoPessoaItemVO obj = new GrupoPessoaItemVO();
			montarDados(obj, tabelaResultado, nivelMontarDados, usuario);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	private void montarDados(GrupoPessoaItemVO obj, SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws  Exception {
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo(dadosSQL.getInt("grupoPessoaItem.codigo"));
		obj.setStatusAtivoInativoEnum(StatusAtivoInativoEnum.valueOf(dadosSQL.getString("grupoPessoaItem.statusAtivoInativoEnum")));
		obj.getGrupoPessoaVO().setCodigo(dadosSQL.getInt("grupoPessoaItem.grupoPessoa"));
	
		obj.getPessoaVO().setCodigo((dadosSQL.getInt("pessoa.codigo")));
		obj.getPessoaVO().setNome((dadosSQL.getString("pessoa.nome")));
		obj.getPessoaVO().setCPF((dadosSQL.getString("pessoa.cpf")));
		obj.getPessoaVO().setEmail(dadosSQL.getString("pessoa.email"));
		obj.getPessoaVO().setEmail2(dadosSQL.getString("pessoa.email2"));
		obj.getPessoaVO().setFuncionario(dadosSQL.getBoolean("pessoa.funcionario"));
		obj.getPessoaVO().setAluno(dadosSQL.getBoolean("pessoa.aluno"));
		if(nivelMontarDados != Uteis.NIVELMONTARDADOS_COMBOBOX) {
			obj.getPessoaVO().setListaPessoaEmailInstitucionalVO(getFacadeFactory().getPessoaEmailInstitucionalFacade().consultarListaPessoaEmailInstitucionalPorPessoa(obj.getPessoaVO().getCodigo(), nivelMontarDados, usuario));
		}
		
	}
	
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<GrupoPessoaItemVO> montarDadosConsultaBasica(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) {
		List<GrupoPessoaItemVO> vetResultado = new ArrayList<>();
		while (tabelaResultado.next()) {
			vetResultado.add(montarDadosBasica(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}
	
	private GrupoPessoaItemVO montarDadosBasica(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) {
		GrupoPessoaItemVO obj = new GrupoPessoaItemVO();
		obj.setCodigo((dadosSQL.getInt("grupoPessoaItem.codigo")));
		obj.setStatusAtivoInativoEnum(StatusAtivoInativoEnum.valueOf(dadosSQL.getString("grupoPessoaItem.statusAtivoInativoEnum")));
		
		obj.getGrupoPessoaVO().setCodigo(dadosSQL.getInt("grupoPessoa.codigo"));
		obj.getGrupoPessoaVO().setNome(dadosSQL.getString("grupoPessoa.nome"));
		
		obj.getPessoaVO().setCodigo((dadosSQL.getInt("pessoa.codigo")));
		obj.getPessoaVO().setNome((dadosSQL.getString("pessoa.nome")));
		obj.getPessoaVO().setCPF((dadosSQL.getString("pessoa.cpf")));
		obj.getPessoaVO().setEmail(dadosSQL.getString("pessoa.email"));
		obj.getPessoaVO().setEmail2(dadosSQL.getString("pessoa.email2"));
		obj.getPessoaVO().setFuncionario(dadosSQL.getBoolean("pessoa.funcionario"));
		obj.getPessoaVO().setAluno(dadosSQL.getBoolean("pessoa.aluno"));
		obj.getPessoaVO().setListaPessoaEmailInstitucionalVO(getFacadeFactory().getPessoaEmailInstitucionalFacade().consultarListaPessoaEmailInstitucionalPorPessoa(obj.getPessoaVO().getCodigo(), nivelMontarDados, usuario));
		return obj;
	}
	
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<PessoaVO> consultarPorPessoaGrupoPessoaItemVOExistenteEstagio(Integer pessoa, int nivelMontarDados, UsuarioVO usuario) {
		try {
			StringBuilder sql = new StringBuilder("select ");			
			sql.append(" distinct pessoa.codigo as \"pessoa.codigo\", ");
			sql.append(" pessoa.nome as \"pessoa.nome\", ");
			sql.append(" pessoa.cpf as \"pessoa.cpf\", ");
			sql.append(" pessoa.email as \"pessoa.email\", ");
			sql.append(" pessoa.email2 as \"pessoa.email2\", ");
			sql.append(" pessoa.funcionario as \"pessoa.funcionario\", ");
			sql.append(" pessoa.aluno as \"pessoa.aluno\" ");			
			sql.append(" FROM grupoPessoaItem ");
			sql.append(" inner join pessoa on pessoa.codigo = grupoPessoaItem.pessoa ");			
			sql.append(" where 1=1 ");
			sql.append(" and exists (select estagio.codigo from estagio where estagio.grupoPessoaItem = grupoPessoaItem.codigo limit 1) ");
			if(Uteis.isAtributoPreenchido(pessoa)) {
				sql.append(" and pessoa.codigo = ").append(pessoa);
			}
			sql.append(" order by pessoa.nome ");
			SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
			List<PessoaVO> vetResultado = new ArrayList<PessoaVO>(0);
			while (dadosSQL.next()) {
				PessoaVO pessoaVO = new PessoaVO();
				pessoaVO.setCodigo((dadosSQL.getInt("pessoa.codigo")));
				pessoaVO.setNome((dadosSQL.getString("pessoa.nome")));
				pessoaVO.setCPF((dadosSQL.getString("pessoa.cpf")));
				pessoaVO.setEmail(dadosSQL.getString("pessoa.email"));
				pessoaVO.setEmail2(dadosSQL.getString("pessoa.email2"));
				pessoaVO.setFuncionario(dadosSQL.getBoolean("pessoa.funcionario"));
				pessoaVO.setAluno(dadosSQL.getBoolean("pessoa.aluno"));				
				vetResultado.add(pessoaVO);
			}
			return vetResultado;
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

}
