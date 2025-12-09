package negocio.facade.jdbc.gsuite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;

import controle.arquitetura.DataModelo;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import negocio.comuns.administrativo.ConfiguracaoSeiGsuiteVO;
import negocio.comuns.administrativo.enumeradores.TipoAdminSdkIntegracaoEnum;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.gsuite.AdminSdkIntegracaoVO;
import negocio.comuns.gsuite.PessoaGsuiteVO;
import negocio.comuns.gsuite.enumeradores.TipoServicoAdminSdkGoogleEnum;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisWebServiceUrl;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.gsuite.AdminSdkIntegracaoInterfaceFacade;
import webservice.arquitetura.InfoWSVO;


@Repository
@Scope("singleton")
@Lazy

public class AdminSdkIntegracao extends ControleAcesso implements AdminSdkIntegracaoInterfaceFacade{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -239795258126201904L;	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void realizarVerificacaoPessoaGsuiteVOValida(PessoaGsuiteVO obj, UsuarioVO usuarioVO) {
		try {
			ConfiguracaoSeiGsuiteVO configuracaoSeiGsuiteVO = getFacadeFactory().getConfiguracaoSeiGsuiteFacade().consultarConfiguracaoSeiGsuitePadrao(Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
			if(Uteis.isAtributoPreenchido(configuracaoSeiGsuiteVO)) {
				Map<String, String> headers = new HashMap<String, String>();
				headers.put(UteisWebServiceUrl.emailpessoagsuite, obj.getEmail());
				HttpResponse<JsonNode> jsonResponse  = unirestHeaders(configuracaoSeiGsuiteVO, UteisWebServiceUrl.URL_GSUITE_ADMIN_SDK + UteisWebServiceUrl.URL_GSUITE_ADMIN_SDK_CONTA_VALIDA, RequestMethod.GET, headers, usuarioVO);
				InfoWSVO info = new Gson().fromJson(jsonResponse.getBody().toString(), InfoWSVO.class);
				Uteis.checkState(info.getCampos().stream().anyMatch(p-> p.getCampo().equals("contaValida") && p.getValor().equals("false")), "O e-mail informado não foi localizado na Conta Gsuite.");	
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	} 
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void executarAlteracaoSenhaContaGsuite(PessoaVO pessoa, String senha, String confirmacaoSenha, UsuarioVO usuario) throws Exception {
		Uteis.checkState(!Uteis.isAtributoPreenchido(senha), "O campo Senha Nova Conta Gsuite deve ser informado.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(confirmacaoSenha), "O campo Confirmação Senha Nova Conta Gsuite deve ser informado.");
		Uteis.checkState(senha.length() < 8, "A senha da Conta do Gsuite não pode ser menor que 8 caracteres.");
		Uteis.checkState(!senha.equals(confirmacaoSenha), "O campo Senha Nova é diferente da Confirmação Senha Nova para Conta Gsuite.");
		ConfiguracaoSeiGsuiteVO configuracaoSeiGsuiteVO = getFacadeFactory().getConfiguracaoSeiGsuiteFacade().consultarConfiguracaoSeiGsuitePadrao(Uteis.NIVELMONTARDADOS_TODOS, usuario);
		Map<String, String> headers = new HashMap<String, String>();
		headers.put(UteisWebServiceUrl.pessoa, pessoa.getCodigo().toString());
		headers.put(UteisWebServiceUrl.senhapessoagsuite, Uteis.encriptarSenhaSHA1(senha));
		unirestHeaders(configuracaoSeiGsuiteVO, UteisWebServiceUrl.URL_GSUITE_ADMIN_SDK + UteisWebServiceUrl.URL_GSUITE_ADMIN_SDK_CONTA_SENHA, RequestMethod.PUT, headers, usuario);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void consultar(DataModelo dataModelo, AdminSdkIntegracaoVO obj ) {
		List<AdminSdkIntegracaoVO> objs = consultaRapidaPorFiltros(dataModelo, obj);
		dataModelo.getListaFiltros().clear();
		dataModelo.setTotalRegistrosEncontrados(consultarTotalPorFiltros(dataModelo, obj));
		dataModelo.setListaConsulta(objs);
	}

	private List<AdminSdkIntegracaoVO> consultaRapidaPorFiltros(DataModelo dataModelo, AdminSdkIntegracaoVO obj) {
		try {
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" WHERE 1 = 1 ");
			montarFiltrosParaConsulta(obj, dataModelo, sqlStr);
			sqlStr.append(" ORDER BY adminSdkIntegracao.dataRegistro desc ");
			UteisTexto.addLimitAndOffset(sqlStr, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), dataModelo.getListaFiltros().toArray());
			return montarDadosConsulta(tabelaResultado, dataModelo.getNivelMontarDados());
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	private Integer consultarTotalPorFiltros(DataModelo dataModelo, AdminSdkIntegracaoVO obj) {
		try {
			StringBuilder sqlStr = getSQLPadraoConsultaTotalBasica();
			sqlStr.append(" WHERE 1= 1 ");
			montarFiltrosParaConsulta(obj, dataModelo, sqlStr);
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), dataModelo.getListaFiltros().toArray());
			return (Integer) Uteis.getSqlRowSetTotalizador(rs, Uteis.QTDE, TipoCampoEnum.INTEIRO);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	private void montarFiltrosParaConsulta(AdminSdkIntegracaoVO obj,  DataModelo dataModelo, StringBuilder sqlStr) {
		if(Uteis.isAtributoPreenchido(obj.getUsuarioVO().getCodigo())) {			
			sqlStr.append(" and usuario.codigo = ? ");
			dataModelo.getListaFiltros().add(obj.getUsuarioVO().getCodigo());
		}else if(Uteis.isAtributoPreenchido(obj.getUsuarioVO().getNome())) {			
			sqlStr.append(" and sem_acentos(usuario.nome) ilike(sem_acentos(?)) ");
			dataModelo.getListaFiltros().add(PERCENT+obj.getUsuarioVO().getNome()+PERCENT);
		}	
		if(Uteis.isAtributoPreenchido(obj.getTipoAdminSdkIntegracaoEnum())) {
			sqlStr.append(" and adminSdkIntegracao.tipoAdminSdkIntegracaoEnum = ? ");
			dataModelo.getListaFiltros().add(obj.getTipoAdminSdkIntegracaoEnum().name());
		}
	}
		
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public AdminSdkIntegracaoVO consultarAdminSdkGoogleEmProcessamentoPorUsuario(Integer usuario, int nivelMontarDados) {
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE adminSdkIntegracao.usuario = ? ");
		sqlStr.append(" and  adminSdkIntegracao.totalRegistro != adminSdkIntegracao.totalProcessado ");
		sqlStr.append(" order by  adminSdkIntegracao.dataRegistro desc limit 1 ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), usuario);
		if (!tabelaResultado.next()) {
			return new AdminSdkIntegracaoVO();
		}
		return montarDados(tabelaResultado, nivelMontarDados);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void consultarAtualizacaoAdminSdkIntegracao(AdminSdkIntegracaoVO obj) {
		StringBuilder sqlStr = new StringBuilder(" select totalProcessado, totalErro from adminSdkIntegracao ");
		sqlStr.append(" WHERE codigo = ? ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), obj.getCodigo());
		if (tabelaResultado.next()) {
			obj.setTotalProcessado(tabelaResultado.getLong("totalProcessado"));
			obj.setTotalErro(tabelaResultado.getLong("totalErro"));
		}
		return;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public AdminSdkIntegracaoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados) {
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE adminSdkIntegracao.codigo = ? ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), codigo);
		if (!tabelaResultado.next()) {
			return new AdminSdkIntegracaoVO();
		}
		return montarDados(tabelaResultado, nivelMontarDados);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void consultarTotalUsuarioAdminSdkGoogle(AdminSdkIntegracaoVO obj) {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select count (distinct codigo) as qtd from pessoagsuite ");
		sqlStr.append(" union all ");
		sqlStr.append(" select count (t.codigo) from ( ");
		sqlStr.append(" select pessoa.codigo ");
		sqlStr.append(" from pessoa ");
		sqlStr.append(" left join matricula on matricula.aluno = pessoa.codigo ");
		sqlStr.append(" left join funcionario on funcionario.pessoa  = pessoa.codigo ");
		sqlStr.append(" where ativo");
		sqlStr.append(" and ((pessoa.aluno and matricula.matricula is not null and matricula.situacao in('AT', 'PR') and true =").append(obj.isCriarContaAluno()).append(")");
		sqlStr.append("       or (pessoa.funcionario  and funcionario.codigo is not null and true =").append(obj.isCriarContaFuncionario()).append(")");
		sqlStr.append("     )");
		sqlStr.append(" and not exists (select codigo from pessoagsuite p2  where p2.pessoa  = pessoa.codigo) ");
		sqlStr.append(" group by pessoa.codigo  ");
		sqlStr.append("  ) as t ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			obj.setTotalRegistro(tabelaResultado.getLong("qtd"));
			tabelaResultado.next();
			obj.setTotalProcessado(tabelaResultado.getLong("qtd"));
		}
	}
	
	private StringBuilder getSQLPadraoConsultaBasica() {
		StringBuilder sql = new StringBuilder();
		sql.append(" select  ");
		sql.append(" adminSdkIntegracao.codigo as \"adminSdkIntegracao.codigo\", ");
		sql.append(" adminSdkIntegracao.totalRegistro as \"adminSdkIntegracao.totalRegistro\", ");
		sql.append(" adminSdkIntegracao.totalProcessado as \"adminSdkIntegracao.totalProcessado\", ");
		sql.append(" adminSdkIntegracao.totalErro as \"adminSdkIntegracao.totalErro\", ");
		sql.append(" adminSdkIntegracao.dataRegistro as \"adminSdkIntegracao.dataRegistro\", ");
		sql.append(" adminSdkIntegracao.criarContaFuncionario as \"adminSdkIntegracao.criarContaFuncionario\", ");
		sql.append(" adminSdkIntegracao.criarContaAluno as \"adminSdkIntegracao.criarContaAluno\", ");
		sql.append(" adminSdkIntegracao.tipoServicoAdminSdkGoogleEnum as \"adminSdkIntegracao.tipoServicoAdminSdkGoogleEnum\", ");
		sql.append(" adminSdkIntegracao.tipoAdminSdkIntegracaoEnum as \"adminSdkIntegracao.tipoAdminSdkIntegracaoEnum\", ");
		sql.append(" usuario.codigo as \"usuario.codigo\", ");
		sql.append(" usuario.nome as \"usuario.nome\" ");
		sql.append(" from adminSdkIntegracao ");
		sql.append(" inner join usuario on usuario.codigo = adminSdkIntegracao.usuario ");
		return sql;
	}
	
	private StringBuilder getSQLPadraoConsultaTotalBasica() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT count(adminSdkIntegracao.codigo) as QTDE FROM adminSdkIntegracao  ");
		sql.append(" inner join usuario on usuario.codigo = adminSdkIntegracao.usuario ");
		return sql;
	}
	
	private List<AdminSdkIntegracaoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados) {
		List<AdminSdkIntegracaoVO> vetResultado = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados));
		}
		return vetResultado;
	}
	
	private AdminSdkIntegracaoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados) {
		AdminSdkIntegracaoVO obj = new AdminSdkIntegracaoVO();
		obj.setCodigo((dadosSQL.getInt("adminSdkIntegracao.codigo")));
		obj.setTotalRegistro(dadosSQL.getLong("adminSdkIntegracao.totalRegistro"));
		obj.setTotalProcessado(dadosSQL.getLong("adminSdkIntegracao.totalProcessado"));
		obj.setTotalErro(dadosSQL.getLong("adminSdkIntegracao.totalErro"));
		obj.setDataRegistro(dadosSQL.getTimestamp("adminSdkIntegracao.dataRegistro"));
		obj.setCriarContaAluno(dadosSQL.getBoolean("adminSdkIntegracao.criarContaAluno"));
		obj.setCriarContaFuncionario(dadosSQL.getBoolean("adminSdkIntegracao.criarContaFuncionario"));		
		obj.setTipoServicoAdminSdkGoogleEnum(TipoServicoAdminSdkGoogleEnum.valueOf(dadosSQL.getString("adminSdkIntegracao.tipoServicoAdminSdkGoogleEnum")));		
		obj.setTipoAdminSdkIntegracaoEnum(TipoAdminSdkIntegracaoEnum.valueOf(dadosSQL.getString("adminSdkIntegracao.tipoAdminSdkIntegracaoEnum")));		
		obj.getUsuarioVO().setCodigo((dadosSQL.getInt("usuario.codigo")));
		obj.getUsuarioVO().setNome((dadosSQL.getString("usuario.nome")));
		return obj;
	}

}
