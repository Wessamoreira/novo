package negocio.facade.jdbc.faturamento.nfe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.faturamento.nfe.ConfiguracaoNotaFiscalVO;
import negocio.comuns.faturamento.nfe.enumeradores.CodigoRegimeTributarioEnum;
import negocio.comuns.faturamento.nfe.enumeradores.TipoIntegracaoNfeEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.faturamento.nfe.AmbienteNfeEnum;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.faturamento.nfe.ConfiguracaoNotaFiscalInterfaceFacade;
import webservice.nfse.generic.NaturezaOperacaoEnum;
import webservice.nfse.generic.RegimeEspecialTributacaoEnum;

@Repository
@Lazy
@Scope("singleton")
public class ConfiguracaoNotaFiscal extends ControleAcesso implements ConfiguracaoNotaFiscalInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public ConfiguracaoNotaFiscal() throws Exception {
		super();
		setIdEntidade("ConfiguracaoNotaFiscal");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ConfiguracaoNotaFiscalVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception {
		try {
			validarDados(obj);
			ConfiguracaoNotaFiscal.incluir(getIdEntidade(), true, usuario);
			final StringBuilder sqlStr = new StringBuilder();
			sqlStr.append("INSERT INTO configuracaonotafiscal (nome, senhaUnidadeCertificadora, arquivoCertificado, senhaCertificado, tipoIntegracaoNfe, ambienteNfe, codigoRegimeTributario, ");
			sqlStr.append("issqn, iss, numeroNota, lote, codigoNaturezaOperacao, nomeNaturezaOperacao, codigoNaturezaOperacaoInterestadual, nomeNaturezaOperacaoInterestadual, ");
			sqlStr.append("endereco, setor, numero, complemento, cidade, CEP, telComercial1, utilizarEnderecoDiferenteUnidade, codigoncm, pis, cofins, inss, csll, codigoTributacaoMunicipio, codigoMunicipio, notificarAlunoNotaFiscalGerada, codigoItemListaServico, cstPis, cstCofins, ");
			sqlStr.append("usuarioIntegracaoNotaFiscalServico, senhaUsuarioIntegracaoNotaFiscalServico, textoPadraoDescriminacaoServicoNotaFiscal, aliquotaIR, descricaoAtividadeTributacaoMunicipio, agruparNotaFicalPorResponsavelFinanceiro, tokenWebserviceNFe, naturezaOperacaoEnum, isIncentivadorCultural, codigoCNAE, serie, numeroNotaHomologacao, loteHomologacao, serieHomologacao, fusoHorario, regimeEspecialTributacaoEnum, percentualCargaTributaria, fonteCargaTributaria ,utilizarServicoWebserviceAuxiliar )");
			sqlStr.append("VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,? ) returning codigo ");
			sqlStr.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));

			if (!obj.getArquivoVO().getNome().equals("")) {
				getFacadeFactory().getArquivoFacade().incluir(obj.getArquivoVO(), usuario, configuracaoGeralSistemaVO);
			}

			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sqlStr.toString());
					sqlInserir.setString(1, obj.getNome());
					sqlInserir.setString(2, obj.getSenhaUnidadeCertificadora());
					if (!obj.getArquivoVO().getCodigo().equals(0)) {
						sqlInserir.setInt(3, obj.getArquivoVO().getCodigo());
					} else {
						sqlInserir.setNull(3, 0);
					}
					sqlInserir.setString(4, obj.getSenhaCertificado());
					sqlInserir.setString(5, obj.getTipoIntegracaoNfeEnum().getKey().toString());
					sqlInserir.setString(6, obj.getAmbienteNfeEnum().getKey().toString());
					sqlInserir.setString(7, obj.getCodigoRegimeTributarioEnum().getKey().toString());
					sqlInserir.setDouble(8, obj.getIssqn());
					sqlInserir.setDouble(9, obj.getIss());
					sqlInserir.setInt(10, obj.getNumeroNota().intValue());
					sqlInserir.setInt(11, obj.getLote().intValue());
					sqlInserir.setString(12, obj.getCodigoNaturezaOperacao());
					sqlInserir.setString(13, obj.getNomeNaturezaOperacao());
					sqlInserir.setString(14, obj.getCodigoNaturezaOperacaoInterestadual());
					sqlInserir.setString(15, obj.getNomeNaturezaOperacaoInterestadual());
					sqlInserir.setString(16, obj.getEndereco());
					sqlInserir.setString(17, obj.getSetor());
					sqlInserir.setString(18, obj.getNumero());
					sqlInserir.setString(19, obj.getComplemento());
					sqlInserir.setInt(20, obj.getCidade().getCodigo());
					sqlInserir.setString(21, obj.getCEP());
					sqlInserir.setString(22, obj.getTelComercial1());
					sqlInserir.setBoolean(23, obj.getUtilizarEnderecoDiferenteUnidade());
					sqlInserir.setString(24, obj.getCodigoNCM());
					sqlInserir.setDouble(25, obj.getPis());
					sqlInserir.setDouble(26, obj.getCofins());
					sqlInserir.setDouble(27, obj.getInss());
					sqlInserir.setDouble(28, obj.getCsll());
					sqlInserir.setString(29, obj.getCodigoTributacaoMunicipio());
					sqlInserir.setString(30, obj.getCodigoMunicipio());
					sqlInserir.setBoolean(31, obj.getNotificarAlunoNotaFiscalGerada());
					sqlInserir.setString(32, obj.getCodigoItemListaServico());
					sqlInserir.setString(33, obj.getCstPis());
					sqlInserir.setString(34, obj.getCstCofins());
					sqlInserir.setString(35, obj.getUsuarioIntegracaoNotaFiscalServico());
					sqlInserir.setString(36, obj.getSenhaUsuarioIntegracaoNotaFiscalServico());
					sqlInserir.setString(37, obj.getTextoPadraoDescriminacaoServicoNotaFiscal());
					sqlInserir.setDouble(38, obj.getAliquotaIR());
					sqlInserir.setString(39, obj.getDescricaoAtividadeTributacaoMunicipio());
					sqlInserir.setBoolean(40, obj.isAgruparNotaFicalPorResponsavelFinanceiro());
					sqlInserir.setString(41, obj.getTokenWebserviceNFe());
					if (obj.getNaturezaOperacaoEnum() != null) {
						sqlInserir.setString(42, obj.getNaturezaOperacaoEnum().name());
					} else {
						sqlInserir.setNull(42, 0);
					}
					sqlInserir.setBoolean(43, obj.getIsIncentivadorCultural());
					sqlInserir.setString(44, obj.getCodigoCNAE());
					sqlInserir.setString(45, obj.getSerie());
					sqlInserir.setInt(46, obj.getNumeroNotaHomologacao());
					sqlInserir.setInt(47, obj.getLoteHomologacao());
					sqlInserir.setString(48, obj.getSerieHomologacao());
					sqlInserir.setInt(49, obj.getFusoHorario());
					if (Uteis.isAtributoPreenchido(obj.getRegimeEspecialTributacaoEnum())) {
						sqlInserir.setString(50, obj.getRegimeEspecialTributacaoEnum().toString());
					} else {
						sqlInserir.setNull(50, 0);
					}
					sqlInserir.setDouble(51, obj.getPercentualCargaTributaria());
					sqlInserir.setString(52, obj.getFonteCargaTributaria());
					sqlInserir.setBoolean(53, obj.getUtilizarServicoWebserviceAuxiliar());
					return sqlInserir;
				}

			}, new ResultSetExtractor<Integer>() {
				public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}

			}));

			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ConfiguracaoNotaFiscalVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception {
		try {
			validarDados(obj);
			ConfiguracaoNotaFiscal.alterar(getIdEntidade(), true, usuario);
			final StringBuilder sqlStr = new StringBuilder();
			sqlStr.append("UPDATE configuracaonotafiscal set nome=?, senhaUnidadeCertificadora=?, arquivoCertificado=?, senhaCertificado=?, tipoIntegracaoNfe=?, ambienteNfe=?, codigoRegimeTributario=?, ");
			sqlStr.append("issqn=?, iss=?, numeroNota=?, lote=?, codigoNaturezaOperacao=?, nomeNaturezaOperacao=?, codigoNaturezaOperacaoInterestadual=?, nomeNaturezaOperacaoInterestadual=?, ");
			sqlStr.append("endereco=?, setor=?, numero=?, complemento=?, cidade=?, CEP=?, telComercial1=?, utilizarEnderecoDiferenteUnidade=?, codigoncm=?, pis = ?, cofins=?, inss=?, csll=?, codigoTributacaoMunicipio=?, codigoMunicipio=?, notificarAlunoNotaFiscalGerada=?, codigoItemListaServico=?, cstPis=?, cstCofins=?, ");
			sqlStr.append("usuarioIntegracaoNotaFiscalServico=?, senhaUsuarioIntegracaoNotaFiscalServico=?, textoPadraoDescriminacaoServicoNotaFiscal=?, aliquotaIR=?, descricaoAtividadeTributacaoMunicipio=?, agruparNotaFicalPorResponsavelFinanceiro=?, tokenWebserviceNFe=?, naturezaOperacaoEnum=?, isIncentivadorCultural=?, codigoCNAE=?, serie=?, numeroNotaHomologacao=?, loteHomologacao=?, serieHomologacao=?, fusoHorario=?, regimeEspecialTributacaoEnum=?, percentualCargaTributaria=?, fonteCargaTributaria=? ,utilizarServicoWebserviceAuxiliar=? ");
			sqlStr.append("WHERE ((codigo = ?)) ");
			sqlStr.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));

			if (obj.getArquivoVO().getCodigo().equals(0)) {
				getFacadeFactory().getArquivoFacade().incluir(obj.getArquivoVO(), usuario, configuracaoGeralSistemaVO);
			} else {
				getFacadeFactory().getArquivoFacade().alterar(obj.getArquivoVO(), usuario, configuracaoGeralSistemaVO);
			}

			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sqlStr.toString());
					sqlAlterar.setString(1, obj.getNome());
					sqlAlterar.setString(2, obj.getSenhaUnidadeCertificadora());
					sqlAlterar.setInt(3, obj.getArquivoVO().getCodigo());
					sqlAlterar.setString(4, obj.getSenhaCertificado());
					sqlAlterar.setString(5, obj.getTipoIntegracaoNfeEnum().getKey().toString());
					sqlAlterar.setString(6, obj.getAmbienteNfeEnum().getKey().toString());
					sqlAlterar.setString(7, obj.getCodigoRegimeTributarioEnum().getKey().toString());
					sqlAlterar.setDouble(8, obj.getIssqn());
					sqlAlterar.setDouble(9, obj.getIss());
					sqlAlterar.setInt(10, obj.getNumeroNota().intValue());
					sqlAlterar.setInt(11, obj.getLote().intValue());
					sqlAlterar.setString(12, obj.getCodigoNaturezaOperacao());
					sqlAlterar.setString(13, obj.getNomeNaturezaOperacao());
					sqlAlterar.setString(14, obj.getCodigoNaturezaOperacaoInterestadual());
					sqlAlterar.setString(15, obj.getNomeNaturezaOperacaoInterestadual());
					sqlAlterar.setString(16, obj.getEndereco());
					sqlAlterar.setString(17, obj.getSetor());
					sqlAlterar.setString(18, obj.getNumero());
					sqlAlterar.setString(19, obj.getComplemento());
					sqlAlterar.setInt(20, obj.getCidade().getCodigo());
					sqlAlterar.setString(21, obj.getCEP());
					sqlAlterar.setString(22, obj.getTelComercial1());
					sqlAlterar.setBoolean(23, obj.getUtilizarEnderecoDiferenteUnidade());
					sqlAlterar.setString(24, obj.getCodigoNCM());
					sqlAlterar.setDouble(25, obj.getPis());
					sqlAlterar.setDouble(26, obj.getCofins());
					sqlAlterar.setDouble(27, obj.getInss());
					sqlAlterar.setDouble(28, obj.getCsll());
					sqlAlterar.setString(29, obj.getCodigoTributacaoMunicipio());
					sqlAlterar.setString(30, obj.getCodigoMunicipio());
					sqlAlterar.setBoolean(31, obj.getNotificarAlunoNotaFiscalGerada());
					sqlAlterar.setString(32, obj.getCodigoItemListaServico());
					sqlAlterar.setString(33, obj.getCstPis());
					sqlAlterar.setString(34, obj.getCstCofins());
					sqlAlterar.setString(35, obj.getUsuarioIntegracaoNotaFiscalServico());
					sqlAlterar.setString(36, obj.getSenhaUsuarioIntegracaoNotaFiscalServico());
					sqlAlterar.setString(37, obj.getTextoPadraoDescriminacaoServicoNotaFiscal());
					sqlAlterar.setDouble(38, obj.getAliquotaIR());
					sqlAlterar.setString(39, obj.getDescricaoAtividadeTributacaoMunicipio());
					sqlAlterar.setBoolean(40, obj.isAgruparNotaFicalPorResponsavelFinanceiro());
					sqlAlterar.setString(41, obj.getTokenWebserviceNFe());
					if (obj.getNaturezaOperacaoEnum() != null) {
						sqlAlterar.setString(42, obj.getNaturezaOperacaoEnum().name());
					} else {
						sqlAlterar.setNull(42, 0);
					}
					sqlAlterar.setBoolean(43, obj.getIsIncentivadorCultural());
					sqlAlterar.setString(44, obj.getCodigoCNAE());
					sqlAlterar.setString(45, obj.getSerie());
					sqlAlterar.setInt(46, obj.getNumeroNotaHomologacao());
					sqlAlterar.setInt(47, obj.getLoteHomologacao());
					sqlAlterar.setString(48, obj.getSerieHomologacao());
					sqlAlterar.setInt(49, obj.getFusoHorario());
					if (Uteis.isAtributoPreenchido(obj.getRegimeEspecialTributacaoEnum())) {
						sqlAlterar.setString(50, obj.getRegimeEspecialTributacaoEnum().toString());
					} else {
						sqlAlterar.setNull(50, 0);
					}
					sqlAlterar.setDouble(51, obj.getPercentualCargaTributaria());
					sqlAlterar.setString(52, obj.getFonteCargaTributaria());
					sqlAlterar.setBoolean(53, obj.getUtilizarServicoWebserviceAuxiliar());
					sqlAlterar.setInt(54, obj.getCodigo());
					return sqlAlterar;
				}

			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(final ConfiguracaoNotaFiscalVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception {
		try {
			ConfiguracaoNotaFiscal.excluir(getIdEntidade(), true, usuario);
			getFacadeFactory().getArquivoFacade().excluir(obj.getArquivoVO(), usuario, configuracaoGeralSistemaVO);
			String sql = "DELETE FROM configuracaonotafiscal WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public ConfiguracaoNotaFiscalVO consultarPorChavePrimaria(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ConfiguracaoNotaFiscal.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT * FROM configuracaonotafiscal ");
		sqlStr.append(" WHERE codigo = ").append(valorConsulta.intValue());
		sqlStr.append(" ORDER BY nome ");
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (dadosSQL.next()) {
			return montarDados(dadosSQL, nivelMontarDados, usuario);
		}
		return new ConfiguracaoNotaFiscalVO();
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public List<ConfiguracaoNotaFiscalVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ConfiguracaoNotaFiscal.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT * FROM configuracaonotafiscal ");
		sqlStr.append(" WHERE codigo = ").append(valorConsulta.intValue());
		sqlStr.append(" ORDER BY nome ");
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<ConfiguracaoNotaFiscalVO> objs = new ArrayList<ConfiguracaoNotaFiscalVO>(0);
		while (dadosSQL.next()) {
			objs.add(montarDados(dadosSQL, nivelMontarDados, usuario));
		}
		return objs;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public List<ConfiguracaoNotaFiscalVO> consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ConfiguracaoNotaFiscal.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT * FROM configuracaonotafiscal ");
		sqlStr.append(" WHERE sem_acentos(trim(lower(nome))) like (trim(sem_acentos('%").append(valorConsulta.toLowerCase()).append("%')))");
		sqlStr.append(" ORDER BY nome ");
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<ConfiguracaoNotaFiscalVO> objs = new ArrayList<ConfiguracaoNotaFiscalVO>(0);
		while (dadosSQL.next()) {
			objs.add(montarDados(dadosSQL, nivelMontarDados, usuario));
		}
		return objs;
	}

	public ConfiguracaoNotaFiscalVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ConfiguracaoNotaFiscalVO obj = new ConfiguracaoNotaFiscalVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setNome(dadosSQL.getString("nome"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
			return obj;
		}
		obj.setAmbienteNfeEnum(AmbienteNfeEnum.toString(dadosSQL.getString("ambienteNfe")));
		obj.setCodigoRegimeTributarioEnum(CodigoRegimeTributarioEnum.toString(dadosSQL.getString("codigoregimetributario")));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSCONSULTA) {
			return obj;
		}
		obj.setSenhaUnidadeCertificadora(dadosSQL.getString("senhaUnidadeCertificadora"));
		obj.setArquivoVO(getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(dadosSQL.getInt("arquivoCertificado"), Uteis.NIVELMONTARDADOS_TODOS, usuario));
		obj.setSenhaCertificado(dadosSQL.getString("senhaCertificado"));
		obj.setIssqn(dadosSQL.getDouble("issqn"));
		obj.setIss(dadosSQL.getDouble("iss"));
		obj.setNumeroNota(dadosSQL.getLong("numeroNota"));
		obj.setLote(dadosSQL.getLong("lote"));
		obj.setCodigoNaturezaOperacao(dadosSQL.getString("codigoNaturezaOperacao"));
		obj.setNomeNaturezaOperacao(dadosSQL.getString("nomeNaturezaOperacao"));
		obj.setCodigoNaturezaOperacaoInterestadual(dadosSQL.getString("codigoNaturezaOperacaoInterestadual"));
		obj.setNomeNaturezaOperacaoInterestadual(dadosSQL.getString("nomeNaturezaOperacaoInterestadual"));
		obj.setEndereco(dadosSQL.getString("endereco"));
		obj.setSetor(dadosSQL.getString("setor"));
		obj.setNumero(dadosSQL.getString("numero"));
		obj.setComplemento(dadosSQL.getString("complemento"));
		obj.setCEP(dadosSQL.getString("CEP"));
		obj.setTelComercial1(dadosSQL.getString("telComercial1"));
		obj.getCidade().setCodigo(dadosSQL.getInt("cidade"));
		obj.setUtilizarEnderecoDiferenteUnidade(dadosSQL.getBoolean("utilizarEnderecoDiferenteUnidade"));
		obj.setNotificarAlunoNotaFiscalGerada(dadosSQL.getBoolean("notificarAlunoNotaFiscalGerada"));
		obj.setCodigoNCM(dadosSQL.getString("codigoncm"));
		obj.setTipoIntegracaoNfeEnum(TipoIntegracaoNfeEnum.getEnumPorKey(dadosSQL.getInt("tipointegracaonfe")));
		obj.setPis(dadosSQL.getDouble("pis"));
		obj.setCofins(dadosSQL.getDouble("cofins"));
		obj.setInss(dadosSQL.getDouble("inss"));
		obj.setCsll(dadosSQL.getDouble("csll"));
		obj.setCodigoTributacaoMunicipio(dadosSQL.getString("codigoTributacaoMunicipio"));
		obj.setCodigoMunicipio(dadosSQL.getString("codigoMunicipio"));
		obj.setCodigoItemListaServico(dadosSQL.getString("codigoItemListaServico"));
		obj.setCstPis(dadosSQL.getString("cstPis"));
		obj.setCstCofins(dadosSQL.getString("cstCofins"));
		obj.setUsuarioIntegracaoNotaFiscalServico(dadosSQL.getString("usuarioIntegracaoNotaFiscalServico"));
		obj.setSenhaUsuarioIntegracaoNotaFiscalServico(dadosSQL.getString("senhaUsuarioIntegracaoNotaFiscalServico"));
		obj.setTextoPadraoDescriminacaoServicoNotaFiscal(dadosSQL.getString("textoPadraoDescriminacaoServicoNotaFiscal"));
		obj.setAliquotaIR(dadosSQL.getDouble("aliquotaIR"));
		obj.setDescricaoAtividadeTributacaoMunicipio(dadosSQL.getString("descricaoAtividadeTributacaoMunicipio"));
		obj.setAgruparNotaFicalPorResponsavelFinanceiro(dadosSQL.getBoolean("agruparNotaFicalPorResponsavelFinanceiro"));
		obj.setTokenWebserviceNFe(dadosSQL.getString("tokenWebserviceNFe"));
		obj.setNaturezaOperacaoEnum(dadosSQL.getString("naturezaOperacaoEnum") != null ? NaturezaOperacaoEnum.valueOf(dadosSQL.getString("naturezaOperacaoEnum")) : null);
		obj.setRegimeEspecialTributacaoEnum(dadosSQL.getString("regimeEspecialTributacaoEnum") != null ? RegimeEspecialTributacaoEnum.valueOf(dadosSQL.getString("regimeEspecialTributacaoEnum")) : null);
		obj.setIsIncentivadorCultural(dadosSQL.getBoolean("isIncentivadorCultural"));
		obj.setCodigoCNAE(dadosSQL.getString("codigoCNAE"));
		obj.setSerie(dadosSQL.getString("serie"));
		obj.setNumeroNotaHomologacao(dadosSQL.getInt("numeroNotaHomologacao"));
		obj.setLoteHomologacao(dadosSQL.getInt("loteHomologacao"));
		obj.setSerieHomologacao(dadosSQL.getString("serieHomologacao"));
		obj.setFusoHorario(dadosSQL.getInt("fusoHorario"));
		obj.setPercentualCargaTributaria(dadosSQL.getDouble("percentualCargaTributaria"));
		obj.setFonteCargaTributaria(dadosSQL.getString("fonteCargaTributaria"));
		obj.setUtilizarServicoWebserviceAuxiliar(dadosSQL.getBoolean("utilizarServicoWebserviceAuxiliar"));
		montarDadosCidade(obj, nivelMontarDados, usuario);
		obj.setNovoObj(Boolean.FALSE);
		return obj;
	}

	public void validarDados(ConfiguracaoNotaFiscalVO obj) throws Exception {
		if (obj.getNome() == null || obj.getNome().equals("")) {
			throw new Exception("Informe o Nome da configuração.");
		}
		if (obj.getArquivoVO() == null || obj.getArquivoVO().getNome().equals("")) {
			throw new Exception("Faça o Upload do Certificado.");
		}
		if (obj.getSenhaCertificado() == null || obj.getSenhaCertificado().equals("")) {
			throw new Exception("Informe a Senha do Certificado.");
		}
		if (obj.getUtilizarEnderecoDiferenteUnidade()) {
			if (!Uteis.isAtributoPreenchido(obj.getCEP())) {
				throw new Exception("O campo CEP (DADOS ENDEREÇO) deve ser preenchido.");
			}
			if (!Uteis.isAtributoPreenchido(obj.getEndereco())) {
				throw new Exception("O campo Endereço (DADOS ENDEREÇO) deve ser preenchido.");
			}
			if (!Uteis.isAtributoPreenchido(obj.getSetor())) {
				throw new Exception("O campo Bairro/Setor (DADOS ENDEREÇO) deve ser preenchido.");
			}
			if (!Uteis.isAtributoPreenchido(obj.getTelComercial1())) {
				throw new Exception("O campo Telefone (DADOS ENDEREÇO) deve ser preenchido.");
			}
		}
		if(obj.getCstPis().equals("01") || obj.getCstPis().equals("02")) {
			if(obj.getPis().equals(0.00)) {
				throw new Exception(UteisJSF.internacionalizar("msg_ConfiguracaoNotaFiscal_aliquotaPISObrigatoria"));
			}
		}
		if(obj.getCstCofins().equals("01") || obj.getCstCofins().equals("02")) {
			if(obj.getCofins().equals(0.00)) {
				throw new Exception(UteisJSF.internacionalizar("msg_ConfiguracaoNotaFiscal_aliquotaCOFINSObrigatoria"));
			}
		}	
		
		if(obj.getTipoIntegracaoNfeEnum().equals(TipoIntegracaoNfeEnum.NFE) && !Uteis.isAtributoPreenchido(obj.getCodigoNCM())) {
			throw new Exception(UteisJSF.internacionalizar("msg_ConfiguracaoNotaFiscal_codigoNcmObrigatorio"));
		}
	}

	public static String getIdEntidade() {
		return ConfiguracaoNotaFiscal.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		ConfiguracaoNotaFiscal.idEntidade = idEntidade;
	}

	@Override
	public ConfiguracaoNotaFiscalVO consultarPorUnidadeEnsino(Integer unidadeEnsino, Boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		ConfiguracaoNotaFiscal.consultar(getIdEntidade(), controlarAcesso, usuarioVO);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT configuracaonotafiscal.* FROM configuracaonotafiscal ");
		sqlStr.append("inner join unidadeEnsino on unidadeEnsino.configuracaonotafiscal =  configuracaonotafiscal.codigo ");
		sqlStr.append("WHERE unidadeEnsino.codigo = ").append(unidadeEnsino);
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (dadosSQL.next()) {
			return montarDados(dadosSQL, nivelMontarDados, usuarioVO);
		}
		return new ConfiguracaoNotaFiscalVO();
	}

	public static void montarDadosCidade(ConfiguracaoNotaFiscalVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (!Uteis.isAtributoPreenchido(obj.getCidade())) {
			obj.setCidade(new CidadeVO());
			return;
		}
		obj.setCidade(getFacadeFactory().getCidadeFacade().consultarPorChavePrimaria(obj.getCidade().getCodigo(), false, usuario));
	}
	
	@Override
	public List<ConfiguracaoNotaFiscalVO> consultarConfiguracaoNotaFiscal(Boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT configuracaonotafiscal.* FROM configuracaonotafiscal ");
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(dadosSQL, nivelMontarDados, usuarioVO);
	}
	
	private List<ConfiguracaoNotaFiscalVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		List<ConfiguracaoNotaFiscalVO> vetResultado = new ArrayList<ConfiguracaoNotaFiscalVO>();
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuarioLogado));
		}
		return vetResultado;
	}
}
