package negocio.facade.jdbc.basico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.BiometriaVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.basico.BiometriaInterfaceFacade;
import webservice.servicos.PessoaObject;

@Repository
@Scope("singleton")
@Lazy
public class Biometria extends ControleAcesso implements BiometriaInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public Biometria() throws Exception {
		super();
		setIdEntidade("Biometria");
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void persistir(BiometriaVO obj, Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj);
		realizarPreenchimentoIdDigital(obj, obj.getIdDigital(), obj.getIdDedo());
		if (obj.isNovoObj()) {
			incluir(obj, controlarAcesso, usuarioVO);
		} else {
			alterar(obj, controlarAcesso, usuarioVO);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void incluir(final BiometriaVO obj, Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		incluir(idEntidade, controlarAcesso, usuarioVO);
		obj.setData(new Date());
		obj.setResponsavel(usuarioVO);
		try {
			final StringBuilder sqlStr = new StringBuilder("");
			sqlStr.append(" INSERT INTO biometria (pessoa, responsavel, data, idDigitalPolegarEsquerdo, idDigitalIndicadorEsquerdo, idDigitalMedioEsquerdo, idDigitalAnularEsquerdo, ");
			sqlStr.append(" idDigitalMinimoEsquerdo, idDigitalPolegarDireito, idDigitalIndicadorDireito, idDigitalMedioDireito, idDigitalAnularDireito, idDigitalMinimoDireito, matricula, ativo, sincronizar) ");
			sqlStr.append(" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			obj.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sqlStr.toString());
					if (Uteis.isAtributoPreenchido(obj.getPessoaVO())) {
						sqlInserir.setInt(1, obj.getPessoaVO().getCodigo());
					} else {
						sqlInserir.setNull(1, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getResponsavel())) {
						sqlInserir.setInt(2, obj.getResponsavel().getCodigo());
					} else {
						sqlInserir.setNull(2, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getData())) {
						sqlInserir.setDate(3, Uteis.getDataJDBC(obj.getData()));
					} else {
						sqlInserir.setNull(3, 0);
					}
					sqlInserir.setInt(4, obj.getIdDigitalPolegarEsquerdo());
					sqlInserir.setInt(5, obj.getIdDigitalIndicadorEsquerdo());
					sqlInserir.setInt(6, obj.getIdDigitalMedioEsquerdo());
					sqlInserir.setInt(7, obj.getIdDigitalAnularEsquerdo());
					sqlInserir.setInt(8, obj.getIdDigitalMinimoEsquerdo());
					sqlInserir.setInt(9, obj.getIdDigitalPolegarDireito());
					sqlInserir.setInt(10, obj.getIdDigitalIndicadorDireito());
					sqlInserir.setInt(11, obj.getIdDigitalMedioDireito());
					sqlInserir.setInt(12, obj.getIdDigitalAnularDireito());
					sqlInserir.setInt(13, obj.getIdDigitalMinimoDireito());
					sqlInserir.setString(14, obj.getMatricula());
					sqlInserir.setBoolean(15, obj.getAtivo());
					sqlInserir.setBoolean(16, obj.getSincronizar());
					return sqlInserir;
				}
			}, new ResultSetExtractor<Integer>() {
				@Override
				public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
			obj.setNovoObj(false);
		} catch (Exception e) {
			obj.setNovoObj(true);
			if(e.getMessage().contains("ck_biometria_matricula")){
				throw new Exception(UteisJSF.internacionalizar("msg_Biometria_codigoAcessoUnico"));
			}
			if(e.getMessage().contains("unique_biometria_pessoa")){
				throw new Exception(UteisJSF.internacionalizar("msg_Biometria_pessoaUnico"));
			}
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void alterar(final BiometriaVO obj, Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		alterar(idEntidade, controlarAcesso, usuarioVO);
		obj.setData(new Date());
		obj.setResponsavel(usuarioVO);
		obj.setSincronizar(true);		
		try {
			final StringBuilder sqlStr = new StringBuilder("");
			sqlStr.append(" UPDATE biometria SET pessoa=?, responsavel=?, data=?, idDigitalPolegarEsquerdo=?, idDigitalIndicadorEsquerdo=?, idDigitalMedioEsquerdo=?, idDigitalAnularEsquerdo=?, ");
			sqlStr.append(" idDigitalMinimoEsquerdo=?, idDigitalPolegarDireito=?, idDigitalIndicadorDireito=?, idDigitalMedioDireito=?, idDigitalAnularDireito=?, idDigitalMinimoDireito=?, matricula = ?, ativo = ?, sincronizar = ? ");
			sqlStr.append(" WHERE (codigo = ?) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sqlStr.toString());
					if (Uteis.isAtributoPreenchido(obj.getPessoaVO())) {
						sqlAlterar.setInt(1, obj.getPessoaVO().getCodigo());
					} else {
						sqlAlterar.setNull(1, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getResponsavel())) {
						sqlAlterar.setInt(2, obj.getResponsavel().getCodigo());
					} else {
						sqlAlterar.setNull(2, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getData())) {
						sqlAlterar.setDate(3, Uteis.getDataJDBC(obj.getData()));
					} else {
						sqlAlterar.setNull(3, 0);
					}
					sqlAlterar.setInt(4, obj.getIdDigitalPolegarEsquerdo());
					sqlAlterar.setInt(5, obj.getIdDigitalIndicadorEsquerdo());
					sqlAlterar.setInt(6, obj.getIdDigitalMedioEsquerdo());
					sqlAlterar.setInt(7, obj.getIdDigitalAnularEsquerdo());
					sqlAlterar.setInt(8, obj.getIdDigitalMinimoEsquerdo());
					sqlAlterar.setInt(9, obj.getIdDigitalPolegarDireito());
					sqlAlterar.setInt(10, obj.getIdDigitalIndicadorDireito());
					sqlAlterar.setInt(11, obj.getIdDigitalMedioDireito());
					sqlAlterar.setInt(12, obj.getIdDigitalAnularDireito());
					sqlAlterar.setInt(13, obj.getIdDigitalMinimoDireito());
					sqlAlterar.setString(14, obj.getMatricula());
					sqlAlterar.setBoolean(15, obj.getAtivo());
					sqlAlterar.setBoolean(16, obj.getSincronizar());
					sqlAlterar.setInt(17, obj.getCodigo());
					return sqlAlterar;
				}
			});
			obj.setNovoObj(false);
		} catch (Exception e) {
			obj.setNovoObj(false);
			if(e.getMessage().contains("ck_biometria_matricula")){
				throw new Exception(UteisJSF.internacionalizar("msg_Biometria_codigoAcessoUnico"));
			}
			if(e.getMessage().contains("unique_biometria_pessoa")){
				throw new Exception(UteisJSF.internacionalizar("msg_Biometria_pessoaUnico"));
			}
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(final BiometriaVO obj, Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		realizarPreenchimentoIdDigital(obj, 0, obj.getIdDedo());
		alterar(obj, controlarAcesso, usuarioVO);
	}

	public List<BiometriaVO> consultarPorNomePessoa(String nomePessoa, int nivelMontarDados, Boolean controlarAcesso, UsuarioVO usuarioVO, Integer limit, Integer offset) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioVO);
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append(" SELECT biometria.* FROM biometria ");
		sqlStr.append(" INNER JOIN pessoa ON pessoa.codigo = biometria.pessoa ");
		sqlStr.append(" WHERE upper(sem_acentos(pessoa.nome)) like (upper(sem_acentos('").append(nomePessoa).append("%'))) ");
		sqlStr.append(" order by pessoa.nome ");
		if(limit != null && limit > 0){
			sqlStr.append(" limit ").append(limit).append(" offset ").append(offset);
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(rs, nivelMontarDados, usuarioVO);
	}
	
	public Integer consultarTotalPorNomePessoa(String nomePessoa) throws Exception {		
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append(" SELECT count(biometria.codigo) as qtde FROM biometria ");
		sqlStr.append(" INNER JOIN pessoa ON pessoa.codigo = biometria.pessoa ");
		sqlStr.append(" WHERE upper(sem_acentos(pessoa.nome)) like (upper(sem_acentos('").append(nomePessoa).append("%'))) ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if(rs.next()){
			return rs.getInt("qtde");
		}
		return 0;
	}

	public BiometriaVO consultarPorCodigoPessoa(Integer pessoa, int nivelMontarDados, Boolean controlarAcesso, UsuarioVO usuarioVO, Integer limit, Integer offset) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioVO);
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append(" SELECT * FROM biometria WHERE pessoa = ").append(pessoa);
		if(limit != null && limit > 0){
			sqlStr.append(" limit ").append(limit).append(" offset ").append(offset);
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (rs.next()) {
			return montarDados(rs, nivelMontarDados, usuarioVO);
		}
		return new BiometriaVO();
	}
	
	public Integer consultarTotalPorCodigoPessoa(Integer pessoa) throws Exception {		
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append(" SELECT count(codigo) as qtde FROM biometria WHERE pessoa = ").append(pessoa);		
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if(rs.next()){
			return rs.getInt("qtde");
		}
		return 0;
	}

	public BiometriaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuarioVO);
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append(" SELECT * FROM biometria WHERE biometria.codigo = ").append(codigoPrm);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!rs.next()) {
			throw new ConsistirException("Dados Não Encontrados ( Biometria ).");
		}
		return montarDados(rs, nivelMontarDados, usuarioVO);
	}

	public List<BiometriaVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioVO);
		String sqlStr = "SELECT * FROM biometria WHERE codigo = " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(rs, nivelMontarDados, usuarioVO);
	}

	public BiometriaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		BiometriaVO obj = new BiometriaVO();
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.getPessoaVO().setCodigo(dadosSQL.getInt("pessoa"));
		obj.getResponsavel().setCodigo(dadosSQL.getInt("responsavel"));
		obj.setData(dadosSQL.getDate("data"));
		obj.setIdDigitalPolegarEsquerdo(dadosSQL.getInt("idDigitalPolegarEsquerdo"));
		obj.setIdDigitalIndicadorEsquerdo(dadosSQL.getInt("idDigitalIndicadorEsquerdo"));
		obj.setIdDigitalMedioEsquerdo(dadosSQL.getInt("idDigitalMedioEsquerdo"));
		obj.setIdDigitalAnularEsquerdo(dadosSQL.getInt("idDigitalAnularEsquerdo"));
		obj.setIdDigitalMinimoEsquerdo(dadosSQL.getInt("idDigitalMinimoEsquerdo"));
		obj.setIdDigitalPolegarDireito(dadosSQL.getInt("idDigitalPolegarDireito"));
		obj.setIdDigitalIndicadorDireito(dadosSQL.getInt("idDigitalIndicadorDireito"));
		obj.setIdDigitalMedioDireito(dadosSQL.getInt("idDigitalMedioDireito"));
		obj.setIdDigitalAnularDireito(dadosSQL.getInt("idDigitalAnularDireito"));
		obj.setIdDigitalMinimoDireito(dadosSQL.getInt("idDigitalMinimoDireito"));
		obj.setMatricula(dadosSQL.getString("matricula"));
		obj.setAtivo(dadosSQL.getBoolean("ativo"));
		montarDadosPessoa(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO);
		montarDadosResponsavel(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO);
		obj.setNovoObj(false);
		return obj;
	}

	public void montarDadosPessoa(BiometriaVO obj, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		if (obj.getPessoaVO().getCodigo().intValue() == 0) {
			obj.setPessoaVO(new PessoaVO());
			return;
		}
		obj.setPessoaVO(getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(obj.getPessoaVO().getCodigo(), false, nivelMontarDados, usuarioVO));
	}

	public void montarDadosResponsavel(BiometriaVO obj, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		if (obj.getResponsavel().getCodigo().intValue() == 0) {
			obj.setResponsavel(new UsuarioVO());
			return;
		}
		obj.setResponsavel(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavel().getCodigo(), nivelMontarDados, usuarioVO));
	}

	public List<BiometriaVO> montarDadosConsulta(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		List<BiometriaVO> biometriaVOs = new ArrayList<BiometriaVO>(0);
		while (dadosSQL.next()) {
			biometriaVOs.add(montarDados(dadosSQL, nivelMontarDados, usuarioVO));
		}
		return biometriaVOs;
	}

	public void validarDados(BiometriaVO obj) throws ConsistirException {
		if (!Uteis.isAtributoPreenchido(obj.getPessoaVO())) {
			throw new ConsistirException("Informe a Pessoa para coletar as suas digitais.");
		}
//		if (!obj.getEstadoBio().equals(EstadoBiometriaEnum.ProntoCadastro.toString())) {
//			throw new ConsistirException("É necessário coletar corretamente as amostras de sua digital.");
//		}
	}

	public BiometriaVO realizarPreenchimentoBiometria(PessoaVO pessoaVO, UsuarioVO usuarioVO) throws Exception {
		BiometriaVO biometriaVO = consultarPorCodigoPessoa(pessoaVO.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, usuarioVO, 0, 0);
		if (!Uteis.isAtributoPreenchido(biometriaVO)) {
			biometriaVO.setPessoaVO(pessoaVO);
		}
		if(biometriaVO.getMatricula().trim().isEmpty()){
			biometriaVO.setMatricula(consultarNumeroMatriculaSugerirAcesso(pessoaVO.getCodigo()));
			if(Uteis.isAtributoPreenchido(biometriaVO)){
				alterar(biometriaVO, false, usuarioVO);
			}
		}
		return biometriaVO;
	}
	
	private String consultarNumeroMatriculaSugerirAcesso(Integer pessoa) throws Exception{
		StringBuilder sql  = new StringBuilder("");
		sql.append(" ( select 1 as ordem, matricula.matricula from matricula ");
		sql.append(" inner join curso on curso.codigo = matricula.curso");
		sql.append(" where matricula.aluno =  ").append(pessoa);
		sql.append(" order by case when matricula.situacao = 'AT' then 0 else 1 end, case niveleducacional when 'IN' then 5 when 'BA' then 4 when 'ME' then 3 when 'SU' then 2 when 'PO' then 1 else 6 end, matricula.data desc ");
		sql.append(" limit 1) ");
		sql.append(" union all	");
		sql.append(" (select 2 as ordem, matricula from funcionario 	");
		sql.append(" 	where funcionario.pessoa = ").append(pessoa);
		sql.append(" 	limit 1)");
		sql.append(" order by ordem limit 1");
		SqlRowSet rs  = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if(rs.next()){
			return Uteis.removeCaractersEspeciais2(rs.getString("matricula"));
		}
		return "";
	}

	public void realizarPreenchimentoIdDigital(BiometriaVO biometriaVO, Integer idDigital, Integer idDedo) throws Exception {
		try {
			switch (idDedo) {
			case 1:
				biometriaVO.setIdDigitalPolegarDireito(idDigital);
				break;
			case 2:
				biometriaVO.setIdDigitalIndicadorDireito(idDigital);
				break;
			case 3:
				biometriaVO.setIdDigitalMedioDireito(idDigital);
				break;
			case 4:
				biometriaVO.setIdDigitalAnularDireito(idDigital);
				break;
			case 5:
				biometriaVO.setIdDigitalMinimoDireito(idDigital);
				break;
			case 6:
				biometriaVO.setIdDigitalPolegarEsquerdo(idDigital);
				break;
			case 7:
				biometriaVO.setIdDigitalIndicadorEsquerdo(idDigital);
				break;
			case 8:
				biometriaVO.setIdDigitalMedioEsquerdo(idDigital);
				break;
			case 9:
				biometriaVO.setIdDigitalAnularEsquerdo(idDigital);
				break;
			case 10:
				biometriaVO.setIdDigitalMinimoEsquerdo(idDigital);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
	 */
	public static String getIdEntidade() {
		return Biometria.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		Biometria.idEntidade = idEntidade;
	}
	
	@Override
	public PessoaObject consultarPessoaAcessarCatracaPeloCodigo(Integer codigo){
		PessoaObject pessoa = new PessoaObject();
		if(codigo == null || codigo.equals(0)){
			return pessoa;
		}
		StringBuilder sql  = new StringBuilder("");
		sql.append(" select * from (");
		sql.append(" (select pessoa.codigo, pessoa.nome, matricula.matricula, case when matricula.situacao = 'AT' then 1 else 0 end as ativo from biometria");
		sql.append(" inner join pessoa on pessoa.codigo = biometria.pessoa");
		sql.append(" inner join matricula on matricula.aluno = pessoa.codigo");
		sql.append(" inner join curso on matricula.curso = curso.codigo");
		sql.append(" where pessoa.codigo = ?");
		sql.append(" order by case when matricula.situacao = 'AT' then 1 else 0 end desc, case niveleducacional when 'IN' then 5 when 'BA' then 4 when 'ME' then 3 when 'SU' then 2 when 'PO' then 1 else 6 end, matricula.data desc limit 1)");
		sql.append(" union all");
		sql.append(" select pessoa.codigo, pessoa.nome, funcionario.matricula, case when pessoa.ativo then 1 else 0 end as ativo  from biometria");
		sql.append(" inner join pessoa on pessoa.codigo = biometria.pessoa");
		sql.append(" inner join funcionario on funcionario.pessoa = pessoa.codigo");
		sql.append(" where pessoa.codigo = ? limit 1");
		sql.append(" ) as t order by ativo desc limit 1");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigo, codigo);
		if(rs.next()){
			pessoa.setNome(rs.getString("nome"));
			pessoa.setMatricula(rs.getString("matricula"));
			pessoa.setLiberar(rs.getString("ativo").equals("1"));
		}
		return pessoa;
	}
	
	
	
	@Override	
	public PessoaObject consultarPessoaAcessarCatracaPelaMatricula(String matricula){
		PessoaObject pessoa = new PessoaObject();
		if(matricula == null || matricula.trim().isEmpty()){
			return pessoa;
		}
		StringBuilder sql  = new StringBuilder("");
		sql.append(" select * from (");
		sql.append(" (select pessoa.codigo, pessoa.nome, matricula.matricula, case when matricula.situacao = 'AT' then 1 else 0 end as ativo from biometria");
		sql.append(" inner join pessoa on pessoa.codigo = biometria.pessoa");
		sql.append(" inner join matricula on matricula.aluno = pessoa.codigo");
		sql.append(" inner join curso on matricula.curso = curso.codigo");
		sql.append(" where biometria.matricula = ? and length(biometria.matricula) > 0 ");
		sql.append(" order by case when matricula.situacao = 'AT' then 1 else 0 end desc, case niveleducacional when 'IN' then 5 when 'BA' then 4 when 'ME' then 3 when 'SU' then 2 when 'PO' then 1 else 6 end, matricula.data desc limit 1)");
		sql.append(" union all");
		sql.append(" select pessoa.codigo, pessoa.nome, funcionario.matricula, case when pessoa.ativo then 1 else 0 end as ativo  from biometria");
		sql.append(" inner join pessoa on pessoa.codigo = biometria.pessoa");
		sql.append(" inner join funcionario on funcionario.pessoa = pessoa.codigo");
		sql.append(" where biometria.matricula= ? and length(biometria.matricula) > 0 limit 1");
		sql.append(" ) as t order by ativo desc limit 1");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), matricula, matricula);
		if(rs.next()){
			pessoa.setNome(rs.getString("nome"));
			pessoa.setMatricula(rs.getString("matricula"));
			pessoa.setLiberar(rs.getString("ativo").equals("1"));
		}
		return pessoa;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void gravarStatusBiometria(){		
		StringBuilder sql  = new StringBuilder("");
		sql.append(" update biometria bio set ativo = case when bio.ativo then false else true end, sincronizar = true");
		sql.append(" where bio.ativo != (select case when ativo = 1 then true else false end from (");
		sql.append(" (select case when matricula.situacao = 'AT' then 1 else 0 end as ativo from biometria");
		sql.append(" 		inner join pessoa on pessoa.codigo = biometria.pessoa");
		sql.append(" 		inner join matricula on matricula.aluno = pessoa.codigo");		
		sql.append(" 		where pessoa.codigo = bio.pessoa");
		sql.append(" 		 order by case when matricula.situacao = 'AT' then 1 else 0 end desc limit 1)");
		sql.append(" 		 union all");
		sql.append(" 		 select case when pessoa.ativo then 1 else 0 end as ativo  from biometria");
		sql.append(" 		 inner join pessoa on pessoa.codigo = biometria.pessoa");
		sql.append(" 		 inner join funcionario on funcionario.pessoa = pessoa.codigo");
		sql.append(" 		 where pessoa.codigo = bio.pessoa limit 1");
		sql.append(" 		 ) as t order by ativo desc limit 1) ");		
		getConexao().getJdbcTemplate().update(sql.toString());		
	}
	
	@Override
	public Integer consultarTotalPessoaSincronizarCatraca(){				
		StringBuilder sql  = new StringBuilder("");
		sql.append(" select count(codigo) as qtde  from biometria where sincronizar ");				
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());		
		if(rs.next()){
			return rs.getInt("qtde");
		}
		return 0;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void gravarSincronismoRegistradoPorPessoa(String pessoas){			
		StringBuilder sql  = new StringBuilder(" update biometria set sincronizar = false, datasincronismo = now() where pessoa in (");		
		sql.append(pessoas).append(") ");
		getConexao().getJdbcTemplate().update(sql.toString());	
	}
	
	@Override
	public List<PessoaObject> consultarPessoaSincronizarCatraca(Integer quantidade){
		List<PessoaObject> pessoaVOs = new ArrayList<PessoaObject>(0);		
		StringBuilder sql  = new StringBuilder("");
		sql.append(" select pessoa.codigo, pessoa.nome, biometria.matricula, biometria.ativo from biometria ");
		sql.append(" inner join pessoa on pessoa.codigo = biometria.pessoa ");
		sql.append(" where biometria.sincronizar ");
		sql.append(" order by pessoa.codigo ");
		if(Uteis.isAtributoPreenchido(quantidade)){
			sql.append(" limit  ").append(quantidade);
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		PessoaObject pessoa = null;
		while(rs.next()){
			pessoa = new PessoaObject();
			pessoa.setCodigo(rs.getInt("codigo"));
			pessoa.setNome(rs.getString("nome"));
			pessoa.setMatricula(rs.getString("matricula"));
			pessoa.setLiberar(rs.getBoolean("ativo"));
			pessoaVOs.add(pessoa);
		}
		return pessoaVOs;
	}

}
