package negocio.facade.jdbc.compras;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Preconditions;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.CotacaoHistoricoVO;
import negocio.comuns.compras.CotacaoVO;
import negocio.comuns.compras.DepartamentoTramiteCotacaoCompraVO;
import negocio.comuns.compras.MaterialTramiteCotacaoCompraVO;
import negocio.comuns.compras.TipoDistribuicaoCotacaoEnum;
import negocio.comuns.compras.TramiteCotacaoCompraVO;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.arquitetura.SuperFacadeJDBC;
import negocio.interfaces.compras.CotacaoHistoricoInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class CotacaoHistorico extends ControleAcesso implements CotacaoHistoricoInterfaceFacade {

	private static final long serialVersionUID = 2497665135553702389L;

	

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(CotacaoHistoricoVO obj, Boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		try {

			obj.validarDados();
			TramiteCotacaoCompra.alterar(getIdEntidade(), controlarAcesso, usuario);

			this.validarRestricoes(obj);

			String sqlInsertTramite = "INSERT INTO cotacaohistorico(datainicio, datafim, retorno, cotacao, usuario, departamentotramitecotacaocompra, anteriorhistorico, proximohistorico, motivoretorno, observacao) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

			GeneratedKeyHolder holder = new GeneratedKeyHolder();
			SuperFacadeJDBC.getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement statement = con.prepareStatement(sqlInsertTramite, new String[] { "codigo" });
					Uteis.setValuePreparedStatement(obj.getDataInicio(), 1, statement);
					Uteis.setValuePreparedStatement(obj.getDataFim(), 2, statement);
					Uteis.setValuePreparedStatement(obj.isRetorno(), 3, statement);
					Uteis.setValuePreparedStatement(obj.getCotacao(), 4, statement);
					Uteis.setValuePreparedStatement(obj.getResponsavel(), 5, statement);
					Uteis.setValuePreparedStatement(obj.getDepartamentoTramiteCotacaoCompra(), 6, statement);
					Uteis.setValuePreparedStatement(obj.getAnteriorHistorico(), 7, statement);
					Uteis.setValuePreparedStatement(obj.getProximoHistorico(), 8, statement);
					Uteis.setValuePreparedStatement(obj.getMotivoRetorno(), 9, statement);
					Uteis.setValuePreparedStatement(obj.getObservacao(), 10, statement);

					return statement;
				}
			}, holder);

			obj.setCodigo(holder.getKey().intValue());

			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}

	}

	private void validarRestricoes(CotacaoHistoricoVO obj) {
		// TODO Auto-generated method stub

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(CotacaoHistoricoVO obj, Boolean controlarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralPadraoSistema) throws Exception {

		try {

			obj.validarDados();
			TramiteCotacaoCompra.alterar(getIdEntidade(), controlarAcesso, usuario);

			this.validarRestricoes(obj);

			String sqlInsertTramite = "UPDATE cotacaohistorico SET datainicio=?, datafim=?, retorno=?, cotacao=?, usuario=?, departamentotramitecotacaocompra=?, anteriorhistorico=?, proximohistorico=?, motivoretorno=?, observacao=? WHERE codigo=?;";

			GeneratedKeyHolder holder = new GeneratedKeyHolder();
			SuperFacadeJDBC.getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement statement = con.prepareStatement(sqlInsertTramite, new String[] { "codigo" });
					Uteis.setValuePreparedStatement(obj.getDataInicio(), 1, statement);
					Uteis.setValuePreparedStatement(obj.getDataFim(), 2, statement);
					Uteis.setValuePreparedStatement(obj.isRetorno(), 3, statement);
					Uteis.setValuePreparedStatement(obj.getCotacao(), 4, statement);
					Uteis.setValuePreparedStatement(obj.getResponsavel(), 5, statement);
					Uteis.setValuePreparedStatement(obj.getDepartamentoTramiteCotacaoCompra(), 6, statement);
					Uteis.setValuePreparedStatement(obj.getAnteriorHistorico(), 7, statement);
					Uteis.setValuePreparedStatement(obj.getProximoHistorico(), 8, statement);
					Uteis.setValuePreparedStatement(obj.getMotivoRetorno(), 9, statement);
					Uteis.setValuePreparedStatement(obj.getObservacao(), 10, statement);
					Uteis.setValuePreparedStatement(obj.getCodigo(), 11, statement);

					return statement;
				}
			}, holder);

			this.incluirMateriaisTramiteCotacaoCompra(obj.getListaMaterialTramiteCotacaoCompra(), controlarAcesso, usuario, configuracaoGeralPadraoSistema);

			obj.setCodigo(holder.getKey().intValue());

			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}

	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void atualizarResponsavelCotacaoHistorio(CotacaoHistoricoVO obj, UsuarioVO usuario)  {
		try {
			StringBuilder sqlStr = new StringBuilder();
			sqlStr.append(" UPDATE cotacaohistorico SET usuario = ").append(obj.getResponsavel().getCodigo());
			sqlStr.append(" WHERE codigo = ").append(obj.getCodigo()).append(" ");
			sqlStr.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			getConexao().getJdbcTemplate().update(sqlStr.toString());
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@SuppressWarnings("static-access")
	private void incluirMaterialTramiteCotacaoCompra(MaterialTramiteCotacaoCompraVO obj, Boolean controlarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralPadraoSistema) throws Exception {
		try {

			getFacadeFactory().getArquivoHelper().copiarArquivoDaPastaTempParaPastaFixa(PastaBaseArquivoEnum.TRAMITE_COTACAO_COMPRA_TMP.getValue(), obj.getArquivoVO().getNome(), configuracaoGeralPadraoSistema);
			obj.getArquivoVO().setPastaBaseArquivo(getFacadeFactory().getArquivoHelper().getPastaBase(PastaBaseArquivoEnum.TRAMITE_COTACAO_COMPRA.getValue(), configuracaoGeralPadraoSistema));
			getFacadeFactory().getArquivoFacade().incluir(obj.getArquivoVO(), usuario, configuracaoGeralPadraoSistema);

			String sqlInsertTramite = "INSERT INTO materialtramitecotacaocompra(descricao, cotacaohistorico, arquivo) VALUES (?, ?, ?);";

			GeneratedKeyHolder holder = new GeneratedKeyHolder();
			SuperFacadeJDBC.getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement statement = con.prepareStatement(sqlInsertTramite, new String[] { "codigo" });
					Uteis.setValuePreparedStatement(obj.getDescricao(), 1, statement);
					Uteis.setValuePreparedStatement(obj.getCotacaoHistorico(), 2, statement);
					Uteis.setValuePreparedStatement(obj.getArquivoVO(), 3, statement);

					return statement;
				}
			}, holder);

			obj.setCodigo(holder.getKey().intValue());

			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirMateriaisTramiteCotacaoCompra(List<MaterialTramiteCotacaoCompraVO> obj, Boolean controlarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralPadraoSistema) throws Exception {
		for (MaterialTramiteCotacaoCompraVO p : obj) {
			this.incluirMaterialTramiteCotacaoCompra(p, controlarAcesso, usuario, configuracaoGeralPadraoSistema);
		}
	}

	public List<MaterialTramiteCotacaoCompraVO> consultarMateriaisPorHistorico(CotacaoHistoricoVO cotacaoHistorico, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);

		String sql = "select * from materialtramitecotacaocompra where cotacaohistorico = " + cotacaoHistorico.getCodigo() + ";";
		SqlRowSet rowSet = getConexao().getJdbcTemplate().queryForRowSet(sql);

		List<MaterialTramiteCotacaoCompraVO> listCotacaoCompra = this.montarDadosMaterialTramiteCotacaoCompra(rowSet, cotacaoHistorico, Uteis.NIVELMONTARDADOS_TODOS, usuario);

		return listCotacaoCompra;
	}

	@Override
	public CotacaoHistoricoVO consultarPorCotacao(CotacaoVO cotacao, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);

		String sql = "select * from CotacaoHistorico where cotacao = " + cotacao.getCodigo() + " and anteriorHistorico is null ORDER BY dataInicio limit 1";
		SqlRowSet rowSet = getConexao().getJdbcTemplate().queryForRowSet(sql);

		if (!rowSet.next()) {
			return null;
		}

		CotacaoHistoricoVO cotacaoHistorico = this.montarDados(rowSet, Uteis.NIVELMONTARDADOS_TODOS, usuario);

		cotacaoHistorico.setCotacao(cotacao);

		cotacaoHistorico.getListaMaterialTramiteCotacaoCompra().addAll(this.consultarMateriaisPorHistorico(cotacaoHistorico, controlarAcesso, usuario));

		cotacaoHistorico.setNovoObj(false);
		return this.consultarProximaCotacaoHistorico(cotacaoHistorico, controlarAcesso, usuario);
	}

	private CotacaoHistoricoVO consultarProximaCotacaoHistorico(CotacaoHistoricoVO cotacaoHistoricoAtual, boolean controlarAcesso, UsuarioVO usuario) throws Exception {

		String sql = "select * from CotacaoHistorico where anteriorHistorico = " + cotacaoHistoricoAtual.getCodigo() + ";";
		SqlRowSet rowSet = getConexao().getJdbcTemplate().queryForRowSet(sql);

		if (!rowSet.next()) {
			return cotacaoHistoricoAtual;
		}

		CotacaoHistoricoVO proximaCotacaoHistorico = this.montarDados(rowSet, Uteis.NIVELMONTARDADOS_TODOS, usuario);

		proximaCotacaoHistorico.setAnteriorHistorico(cotacaoHistoricoAtual);
		cotacaoHistoricoAtual.setProximoHistorico(proximaCotacaoHistorico);
		proximaCotacaoHistorico.setCotacao(cotacaoHistoricoAtual.getCotacao());

		proximaCotacaoHistorico.getListaMaterialTramiteCotacaoCompra().addAll(this.consultarMateriaisPorHistorico(proximaCotacaoHistorico, controlarAcesso, usuario));

		proximaCotacaoHistorico.setNovoObj(false);

		return this.consultarProximaCotacaoHistorico(proximaCotacaoHistorico, controlarAcesso, usuario);
	}

	private CotacaoHistoricoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {

		CotacaoHistoricoVO cotacaoHistorico = new CotacaoHistoricoVO();

		cotacaoHistorico.setCodigo(dadosSQL.getInt("codigo"));

		cotacaoHistorico.setDataInicio(dadosSQL.getDate("dataInicio"));
		cotacaoHistorico.setDataFim(dadosSQL.getDate("dataFim"));
		cotacaoHistorico.setRetorno(dadosSQL.getBoolean("retorno"));
		cotacaoHistorico.setObservacao(dadosSQL.getString("observacao"));
		cotacaoHistorico.setMotivoRetorno(dadosSQL.getString("motivoRetorno"));

		int responsavelCodigo = dadosSQL.getInt("usuario");
		Optional<UsuarioVO> usuarioOptional = Optional.empty();
		if (responsavelCodigo > 0) {
			usuarioOptional = Optional.ofNullable(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(responsavelCodigo, nivelMontarDados, usuario));
		}

		cotacaoHistorico.setResponsavel(usuarioOptional.orElse(new UsuarioVO()));

		int departamentoTramiteCotacaoCompraCodigo = dadosSQL.getInt("departamentoTramiteCotacaoCompra");
		Optional<DepartamentoTramiteCotacaoCompraVO> optionalRepartamentoTramiteCotacaoCompra = Optional.empty();
		if (departamentoTramiteCotacaoCompraCodigo > 0) {
			DepartamentoTramiteCotacaoCompraVO departamentoTramiteCotacaoCompra = getFacadeFactory().getTramiteFacade().consultarDepartamentoTramitePorCodigo(departamentoTramiteCotacaoCompraCodigo, usuario);
			optionalRepartamentoTramiteCotacaoCompra = Optional.ofNullable(departamentoTramiteCotacaoCompra);
		}

		cotacaoHistorico.setDepartamentoTramiteCotacaoCompra(optionalRepartamentoTramiteCotacaoCompra.orElse(new DepartamentoTramiteCotacaoCompraVO()));
		return cotacaoHistorico;

	}

	private List<MaterialTramiteCotacaoCompraVO> montarDadosMaterialTramiteCotacaoCompra(SqlRowSet dadosSQL, CotacaoHistoricoVO cotacaoHistorico, int nivelMontarDados, UsuarioVO usuario) throws Exception {

		ArrayList<MaterialTramiteCotacaoCompraVO> list = new ArrayList<>();
		while (dadosSQL.next()) {
			MaterialTramiteCotacaoCompraVO p = new MaterialTramiteCotacaoCompraVO();
			p.setCodigo(dadosSQL.getInt("codigo"));
			p.setDescricao(dadosSQL.getString("descricao"));
			p.setCotacaoHistorico(cotacaoHistorico);
			ArquivoVO arquivo = getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(dadosSQL.getInt("arquivo"), nivelMontarDados, usuario);
			p.setArquivoVO(arquivo);
			p.setNovoObj(false);
			list.add(p);
		}
		return list;

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public boolean avancarTramiteCotacao(CotacaoHistoricoVO cotacaoHistorico, UsuarioVO selecionado, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralPadraoSistema) throws Exception {

		if(cotacaoHistorico.getDepartamentoTramiteCotacaoCompra().isObservacaoObrigatoria() && !Uteis.isAtributoPreenchido(cotacaoHistorico.getObservacao())){
			throw new StreamSeiException("O campo Observação deve ser informado.");
		}
		if(!cotacaoHistorico.getResponsavel().getCodigo().equals(usuario.getCodigo())){
			cotacaoHistorico.setResponsavel(usuario);
		}
		
		CotacaoHistoricoVO novoCotacaoHistorico = new CotacaoHistoricoVO();
		novoCotacaoHistorico.setAnteriorHistorico(cotacaoHistorico);
		novoCotacaoHistorico.setCotacao(cotacaoHistorico.getCotacao());
		novoCotacaoHistorico.setDataInicio(new Date());
		if (historicoAnteriorPossuiOrdemMaiorQueAtual(cotacaoHistorico)) {
			novoCotacaoHistorico.setDepartamentoTramiteCotacaoCompra(cotacaoHistorico.getAnteriorHistorico().getDepartamentoTramiteCotacaoCompra());
			novoCotacaoHistorico.setResponsavel(cotacaoHistorico.getAnteriorHistorico().getResponsavel());
		} else {
			novoCotacaoHistorico.setDepartamentoTramiteCotacaoCompra(this.buscarProximoDepartamentoTramiteCotacaoCompra(cotacaoHistorico));
			novoCotacaoHistorico.setResponsavel(this.buscarResponsavel(novoCotacaoHistorico, selecionado, controlarAcesso, nivelMontarDados, usuario));
		}
		this.incluir(novoCotacaoHistorico, controlarAcesso, usuario);
		
		cotacaoHistorico.setDataFim(new Date());
		cotacaoHistorico.setProximoHistorico(novoCotacaoHistorico);
		this.alterar(cotacaoHistorico, controlarAcesso, usuario, configuracaoGeralPadraoSistema);

		return false;
	}

	@Override
	public boolean historicoAnteriorPossuiOrdemMaiorQueAtual(CotacaoHistoricoVO cotacaoHistorico) {
		return Objects.nonNull(cotacaoHistorico.getAnteriorHistorico()) && cotacaoHistorico.getDepartamentoTramiteCotacaoCompra().getOrdem() < cotacaoHistorico.getAnteriorHistorico().getDepartamentoTramiteCotacaoCompra().getOrdem();
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public CotacaoHistoricoVO iniciarTramiteCotacao(CotacaoVO cotacao, UsuarioVO selecionado, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		CotacaoHistoricoVO cotacaoHistoricoVO = new CotacaoHistoricoVO();

		cotacaoHistoricoVO.setCotacao(cotacao);
		cotacaoHistoricoVO.setDataInicio(new Date());

		cotacaoHistoricoVO.setDepartamentoTramiteCotacaoCompra(this.buscarProximoDepartamentoTramiteCotacaoCompra(cotacaoHistoricoVO));
		cotacaoHistoricoVO.setResponsavel(this.buscarResponsavel(cotacaoHistoricoVO, selecionado, controlarAcesso, nivelMontarDados, usuario));
		this.incluir(cotacaoHistoricoVO, controlarAcesso, usuario);
		return cotacaoHistoricoVO;

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public boolean finalizarTramiteCotacao(CotacaoHistoricoVO cotacaoHistorico, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralPadraoSistema) throws Exception {
		if(!cotacaoHistorico.getResponsavel().getCodigo().equals(usuario.getCodigo())){
			cotacaoHistorico.setResponsavel(usuario);
		}
		cotacaoHistorico.setDataFim(new Date());
		cotacaoHistorico.setObservacao("Finalização do trâmite");
		if (cotacaoHistorico.getCotacao().getSituacao().equals("IN")) {
			cotacaoHistorico.setObservacao("Trâmite indeferido");
		}

		this.alterar(cotacaoHistorico, controlarAcesso, usuario, configuracaoGeralPadraoSistema);
		return true;

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public boolean retornarTramiteCotacao(CotacaoHistoricoVO cotacaoHistorico, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralPadraoSistema) throws Exception {

		CotacaoHistoricoVO novoCotacaoHistorico = new CotacaoHistoricoVO();

		novoCotacaoHistorico.setAnteriorHistorico(cotacaoHistorico);
		cotacaoHistorico.setRetorno(true);
		novoCotacaoHistorico.setRetorno(true);
		novoCotacaoHistorico.setCotacao(cotacaoHistorico.getCotacao());
		novoCotacaoHistorico.setDataInicio(new Date());

		CotacaoHistoricoVO departamentoTRamiteCotacaoCompra = this.getDepartamentoTRamiteCotacaoCompra(cotacaoHistorico);
		novoCotacaoHistorico.setDepartamentoTramiteCotacaoCompra(departamentoTRamiteCotacaoCompra.getDepartamentoTramiteCotacaoCompra());
		novoCotacaoHistorico.setResponsavel(departamentoTRamiteCotacaoCompra.getResponsavel());

		this.incluir(novoCotacaoHistorico, controlarAcesso, usuario);
		if(!cotacaoHistorico.getResponsavel().getCodigo().equals(usuario.getCodigo())){
			cotacaoHistorico.setResponsavel(usuario);
		}
		cotacaoHistorico.setDataFim(new Date());
		cotacaoHistorico.setProximoHistorico(novoCotacaoHistorico);

		this.alterar(cotacaoHistorico, controlarAcesso, usuario, configuracaoGeralPadraoSistema);

		return false;

	}

	public CotacaoHistoricoVO getDepartamentoTRamiteCotacaoCompra(CotacaoHistoricoVO cotacaoHistorico) {

		CotacaoHistoricoVO parametroCotacaoHistorico = cotacaoHistorico;

		while (Objects.nonNull(parametroCotacaoHistorico.getAnteriorHistorico())) {
			parametroCotacaoHistorico = parametroCotacaoHistorico.getAnteriorHistorico();
			if (cotacaoHistorico.getDepartamentoTramiteCotacaoCompra().getOrdem() > parametroCotacaoHistorico.getDepartamentoTramiteCotacaoCompra().getOrdem()) {
				return parametroCotacaoHistorico;
			}
		}
		throw new IllegalStateException("Não existe histórico anterior!!!");
	}	

	private UsuarioVO buscarResponsavel(CotacaoHistoricoVO cotacaoHistoricoVO, UsuarioVO selecionado, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		Preconditions.checkNotNull(cotacaoHistoricoVO.getDepartamentoTramiteCotacaoCompra(), "Não Foi Encontrado um Departamento para Tramitar esta Cotação com Base nas Regras do Cadastro Trâmite Cotação Compra!");
		Preconditions.checkNotNull(cotacaoHistoricoVO.getDepartamentoTramiteCotacaoCompra().getTipoDistribuicaoCotacao(), "Tipo de Distribuição não pode ser nulo!");
		switch (cotacaoHistoricoVO.getDepartamentoTramiteCotacaoCompra().getTipoDistribuicaoCotacao()) {
		case COORDENADOR_CURSO_ESPECIFICO_TRAMITE:
			Uteis.checkState(!Uteis.isAtributoPreenchido(selecionado), "Não foi encontrado um Responsável para o Trâmite!");
			return selecionado;
		case FUNCIONARIO_TRAMITE:
			Uteis.checkState(!Uteis.isAtributoPreenchido(selecionado), "Não foi encontrado um Responsável para o Trâmite!");
			return selecionado;
		case FUNCIONARIO_CARGO_DEPARTAMENTO:
			return this.consultarFuncionarioDepartamentoCargo(cotacaoHistoricoVO, controlarAcesso, nivelMontarDados, usuario);
		case FUNCIONARIO_DEPARTAMENTO:
			return this.consultarFuncionarioDepartamento(cotacaoHistoricoVO, controlarAcesso, nivelMontarDados, usuario);
		case FUNCIONARIO_ESPECIFICO:
			return this.consultarFuncionarioEspecifico(cotacaoHistoricoVO, controlarAcesso, nivelMontarDados, usuario);
		case GERENTE_DEPARTAMENTO:
			return this.consultarGerenteDepartamento(cotacaoHistoricoVO, controlarAcesso, nivelMontarDados, usuario);
		default:
			throw new IllegalStateException("Tipo de Distribuição não condiz com nenhuma distribuição cadastrada no sistema");
		}
	}

	private UsuarioVO consultarFuncionarioDepartamentoCargo(CotacaoHistoricoVO cotacaoHistoricoVO, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		UsuarioVO usuarioResponsavel = getFacadeFactory().getUsuarioFacade().consultarPorDepartamentoCargoEPoliticaDistribuicao(cotacaoHistoricoVO.getDepartamentoTramiteCotacaoCompra().getDepartamentoVO(), cotacaoHistoricoVO.getDepartamentoTramiteCotacaoCompra().getCargoVO(), cotacaoHistoricoVO.getDepartamentoTramiteCotacaoCompra().getTipoPoliticaDistribuicao(), controlarAcesso, nivelMontarDados, usuario);
		Uteis.checkState(!Uteis.isAtributoPreenchido(usuarioResponsavel), String.format("Não foi encontrado um Funcionário para o departamento %s do Cargo %s na unidade de ensino <NOME UNIDADE DE ENSINO> da tramitação da cotação", cotacaoHistoricoVO.getDepartamentoTramiteCotacaoCompra().getDepartamentoVO().getNome(), cotacaoHistoricoVO.getDepartamentoTramiteCotacaoCompra().getCargoVO().getNome()));
		return usuarioResponsavel;
	}

	private UsuarioVO consultarFuncionarioDepartamento(CotacaoHistoricoVO cotacaoHistoricoVO, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		UsuarioVO usuarioResponsavel = getFacadeFactory().getUsuarioFacade().consultarPorDepartamentoCargoEPoliticaDistribuicao(cotacaoHistoricoVO.getDepartamentoTramiteCotacaoCompra().getDepartamentoVO(), null, cotacaoHistoricoVO.getDepartamentoTramiteCotacaoCompra().getTipoPoliticaDistribuicao(), controlarAcesso, nivelMontarDados, usuario);
		Preconditions.checkNotNull(usuarioResponsavel, String.format("Não foi encontrado um responsável para o departamento %s na unidade de ensino <NOME UNIDADE DE ENSINO> da tramitação da cotação", cotacaoHistoricoVO.getDepartamentoTramiteCotacaoCompra().getDepartamentoVO().getNome()));
		return usuarioResponsavel;
	}

	private UsuarioVO consultarGerenteDepartamento(CotacaoHistoricoVO cotacaoHistoricoVO, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<UsuarioVO> usuarioResponsavel = getFacadeFactory().getUsuarioFacade().consultaUsuarioGerentePorDepartamento(cotacaoHistoricoVO.getDepartamentoTramiteCotacaoCompra().getDepartamentoVO(),null, nivelMontarDados, usuario);
		Uteis.checkState(!Uteis.isAtributoPreenchido(usuarioResponsavel), String.format("Não foi encontrado um Gerente para o departamento %s na unidade de ensino <NOME UNIDADE DE ENSINO> da tramitação da cotação", cotacaoHistoricoVO.getDepartamentoTramiteCotacaoCompra().getDepartamentoVO().getNome()));
		return usuarioResponsavel.get(0);
	}

	private UsuarioVO consultarFuncionarioEspecifico(CotacaoHistoricoVO cotacaoHistoricoVO, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		FuncionarioVO responsavel = cotacaoHistoricoVO.getDepartamentoTramiteCotacaoCompra().getFuncionario();
		Preconditions.checkNotNull(responsavel, String.format("Não foi encontrado um responsável para o departamento %s na unidade de ensino <NOME UNIDADE DE ENSINO> da tramitação da cotação", cotacaoHistoricoVO.getDepartamentoTramiteCotacaoCompra().getDepartamentoVO().getNome()));
		UsuarioVO usuarioResponsavel = getFacadeFactory().getUsuarioFacade().consultarPorPessoa(responsavel.getPessoa().getCodigo(), controlarAcesso, nivelMontarDados, usuario);
		return usuarioResponsavel;
	}

	@Override
	public DepartamentoTramiteCotacaoCompraVO buscarProximoDepartamentoTramiteCotacaoCompra(CotacaoHistoricoVO cotacaoHistoricoVO) {
		List<DepartamentoTramiteCotacaoCompraVO> listaDepartamentoTramite = cotacaoHistoricoVO.getCotacao().getTramiteCotacaoCompra().getListaDepartamentoTramite();
		Preconditions.checkState(listaDepartamentoTramite.size() > 0, "Lista de Departamento Tramite não pode ser vazia!");

		DepartamentoTramiteCotacaoCompraVO vo = null;
		Optional<DepartamentoTramiteCotacaoCompraVO> optVO = Optional.empty();
		if (Objects.isNull(cotacaoHistoricoVO.getAnteriorHistorico())) {

			if (Objects.isNull(cotacaoHistoricoVO.getDepartamentoTramiteCotacaoCompra())) {
				optVO = listaDepartamentoTramite.stream()
				        .filter(p -> cotacaoHistoricoVO.validaCotacaoPassaDepartamento(cotacaoHistoricoVO.getCotacao(), p))
				        .findFirst();
				if (optVO.isPresent()) {
					vo = optVO.get();
				}
			} else {
				optVO = listaDepartamentoTramite.stream()
				        .filter(p -> cotacaoHistoricoVO.validaCotacaoPassaDepartamento(cotacaoHistoricoVO.getCotacao(), p))
				        .filter(p -> p.getOrdem().compareTo(cotacaoHistoricoVO.getDepartamentoTramiteCotacaoCompra().getOrdem()) > 0)
				        .findFirst();
				if (optVO.isPresent()) {
					vo = optVO.get();
				}
			}

		} else {
			DepartamentoTramiteCotacaoCompraVO dtcc = cotacaoHistoricoVO.getDepartamentoTramiteCotacaoCompra();
			for (DepartamentoTramiteCotacaoCompraVO p : listaDepartamentoTramite) {
				if (cotacaoHistoricoVO.validaCotacaoPassaDepartamento(cotacaoHistoricoVO.getCotacao(), p) && p.getOrdem() > dtcc.getOrdem()) {
					vo = p;
					break;
				}
			}

		}
		return vo;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public boolean isTramiteIniciado(CotacaoVO cotacao) {
		String sql = "select count(codigo) as qtd from CotacaoHistorico where cotacao = " + cotacao.getCodigo() + " limit 1";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
		return Uteis.isAtributoPreenchido(tabelaResultado, "qtd", TipoCampoEnum.INTEIRO);
	}
	
	
	
	
	
	
	
	
	
	private StringBuilder getSQLPadraoConsultaBasica() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT cotacaohistorico.codigo as \"cotacaohistorico.codigo\",  ");
		sql.append(" cotacaohistorico.datainicio as \"cotacaohistorico.datainicio\", ");
		sql.append(" cotacaohistorico.datafim as \"cotacaohistorico.datafim\", ");
		sql.append(" cotacaohistorico.retorno as \"cotacaohistorico.retorno\", ");
		sql.append(" cotacaohistorico.motivoretorno as \"cotacaohistorico.motivoretorno\", ");
		sql.append(" cotacaohistorico.observacao as \"cotacaohistorico.observacao\", ");
		
		sql.append(" departamentotramite.codigo as \"departamentotramite.codigo\", ");
		sql.append(" departamentotramite.tipodistribuicaocotacao as \"departamentotramite.tipodistribuicaocotacao\", ");
		sql.append(" departamentotramite.prazoexecucao as \"departamentotramite.prazoexecucao\", ");
		sql.append(" departamentotramite.orientacaoresponsavel as \"departamentotramite.orientacaoresponsavel\", ");
		sql.append(" departamentotramite.observacaoobrigatoria as \"departamentotramite.observacaoobrigatoria\", ");
		sql.append(" departamentotramite.ordem as \"departamentotramite.ordem\", ");
		
		sql.append(" tramite.codigo as \"tramite.codigo\", tramite.nome as \"tramite.nome\", ");
				
		sql.append(" usuario.codigo as \"usuario.codigo\", usuario.nome as \"usuario.nome\",  ");
		
		sql.append(" departamento.codigo as \"departamento.codigo\", departamento.nome as \"departamento.nome\"  ");
		
		sql.append(" FROM cotacaohistorico ");
		sql.append(" inner join usuario on usuario.codigo = cotacaohistorico.usuario ");
		sql.append(" inner join departamentotramite on departamentotramite.codigo   = cotacaohistorico.departamentoTramiteCotacaoCompra ");
		sql.append(" inner join tramite on tramite.codigo = departamentotramite.tramite ");
		sql.append(" left join departamento on departamento.codigo = departamentotramite.departamento ");
		

		return sql;
	}
	
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<CotacaoHistoricoVO> consultaRapidaPorCotacao(CotacaoVO cotacao,  UsuarioVO usuario) {
		try {
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" WHERE cotacaohistorico.cotacao = ").append(cotacao.getCodigo()).append(" ");
			sqlStr.append(" ORDER BY cotacaohistorico.datainicio ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			List<CotacaoHistoricoVO> vetResultado = new ArrayList<>();
			while (tabelaResultado.next()) {
				CotacaoHistoricoVO obj = new CotacaoHistoricoVO();
				montarDadosBasico(obj, tabelaResultado);
				obj.setCotacao(cotacao);
				vetResultado.add(obj);
			}
			return vetResultado;
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<CotacaoHistoricoVO> montarDadosConsultaBasica(SqlRowSet tabelaResultado, UsuarioVO usuario) {
		List<CotacaoHistoricoVO> vetResultado = new ArrayList<>();
		while (tabelaResultado.next()) {
			CotacaoHistoricoVO obj = new CotacaoHistoricoVO();
			montarDadosBasico(obj, tabelaResultado);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	private void montarDadosBasico(CotacaoHistoricoVO obj, SqlRowSet dadosSQL) {
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo(dadosSQL.getInt("cotacaohistorico.codigo"));
		obj.setDataInicio(dadosSQL.getDate("cotacaohistorico.dataInicio"));
		obj.setDataFim(dadosSQL.getDate("cotacaohistorico.dataFim"));
		obj.setRetorno(dadosSQL.getBoolean("cotacaohistorico.retorno"));
		obj.setObservacao(dadosSQL.getString("cotacaohistorico.observacao"));
		obj.setMotivoRetorno(dadosSQL.getString("cotacaohistorico.motivoRetorno"));
		
		obj.setDepartamentoTramiteCotacaoCompra(new DepartamentoTramiteCotacaoCompraVO());
		obj.getDepartamentoTramiteCotacaoCompra().setCodigo((dadosSQL.getInt("departamentotramite.codigo")));
		obj.getDepartamentoTramiteCotacaoCompra().setTipoDistribuicaoCotacao(TipoDistribuicaoCotacaoEnum.valueOf((dadosSQL.getString("departamentotramite.tipodistribuicaocotacao"))));		
		obj.getDepartamentoTramiteCotacaoCompra().setPrazoExecucao((dadosSQL.getInt("departamentotramite.prazoexecucao")));
		obj.getDepartamentoTramiteCotacaoCompra().setOrdem((dadosSQL.getInt("departamentotramite.ordem")));
		obj.getDepartamentoTramiteCotacaoCompra().setObservacaoObrigatoria((dadosSQL.getBoolean("departamentotramite.observacaoobrigatoria")));
		obj.getDepartamentoTramiteCotacaoCompra().setOrientacaoResponsavel((dadosSQL.getString("departamentotramite.orientacaoresponsavel")));
		
		obj.getDepartamentoTramiteCotacaoCompra().getDepartamentoVO().setCodigo((dadosSQL.getInt("departamento.codigo")));
		obj.getDepartamentoTramiteCotacaoCompra().getDepartamentoVO().setNome((dadosSQL.getString("departamento.nome")));
		
		obj.getDepartamentoTramiteCotacaoCompra().setTramiteVO(new TramiteCotacaoCompraVO());
		obj.getDepartamentoTramiteCotacaoCompra().getTramiteVO().setCodigo((dadosSQL.getInt("tramite.codigo")));
		obj.getDepartamentoTramiteCotacaoCompra().getTramiteVO().setNome((dadosSQL.getString("tramite.nome")));
		
		obj.setResponsavel(new UsuarioVO());
		obj.getResponsavel().setCodigo((dadosSQL.getInt("usuario.codigo")));
		obj.getResponsavel().setNome((dadosSQL.getString("usuario.nome")));

		
	}
	
	

	

}
