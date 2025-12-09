package negocio.facade.jdbc.faturamento.nfe;

import java.io.File;
import java.net.URI;
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
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.faturamento.nfe.LoteInutilizacaoVO;
import negocio.comuns.faturamento.nfe.NfeVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.faturamento.nfe.CodigoRetorno;
import negocio.comuns.utilitarias.faturamento.nfe.Inutilizar;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.comuns.utilitarias.faturamento.nfe.UteisNfe;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.faturamento.nfe.InutilizacaoNotaInterfaceFacade;

@Repository
@Scope(value = "singleton")
@Lazy
@Transactional(readOnly = true)
public class InutilizacaoNotaFiscal extends ControleAcesso implements InutilizacaoNotaInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected static String idEntidade;

	public InutilizacaoNotaFiscal() throws Exception {
		super();
		setIdEntidade("InutilizacaoNotaFiscal");
	}

	@Override
	public List<LoteInutilizacaoVO> buscarPorPerido(Date dataInicial, Date dataFinal, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		 sqlStr.append(" SELECT codigo, unidadeensino, usuario, datainutilizacao, nrinicial , nrfinal, motivo ");
		 sqlStr.append(" FROM loteInutilizacao ");
		 sqlStr.append(" WHERE datainutilizacao BETWEEN '").append(UteisData.getDataJDBC(dataInicial)).append("' ");
		 sqlStr.append(" AND '").append(UteisData.getDataJDBC(dataFinal)).append("' ");
		 SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuarioLogado);
	}
	
	@Transactional(readOnly = false, rollbackFor = { Throwable.class })
	public List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		List vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuarioLogado));
		}
		tabelaResultado = null;
		return vetResultado;
	}
	
	@Transactional(readOnly = false, rollbackFor = { Throwable.class })
	public LoteInutilizacaoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		LoteInutilizacaoVO obj = new LoteInutilizacaoVO();
		
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria((dadosSQL.getInt("unidadeensino")), false, nivelMontarDados, usuarioLogado));
		obj.setUsuarioVO(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria((dadosSQL.getInt("usuario")), nivelMontarDados, usuarioLogado));
		obj.setDataInutilizacao(dadosSQL.getDate("datainutilizacao"));
		obj.setNrInicial(dadosSQL.getInt("nrinicial"));
		obj.setNrfinal(dadosSQL.getInt("nrfinal"));
		obj.setMotivo(dadosSQL.getString("motivo"));
		return obj;
	}

	@Override
	public  List<LoteInutilizacaoVO> buscarNumeroNota(Integer numeroNota, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception  {
		StringBuilder sqlStr = new StringBuilder(); 
		 sqlStr.append(" SELECT codigo, unidadeensino, usuario, datainutilizacao, nrinicial , nrfinal, motivo ");
		 sqlStr.append(" FROM loteInutilizacao ");
		 sqlStr.append(" WHERE nrinicial >= ").append(numeroNota);
		 sqlStr.append(" OR nrfinal <= ").append(numeroNota);
		 SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuarioLogado);
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void inutilizarNota(LoteInutilizacaoVO loteInutilizacao, ConfiguracaoGeralSistemaVO configuracaoRespositoriArquivo, UsuarioVO usuarioLogado) throws Exception {
		try {
			String diretorioNotasEnviadasStr = configuracaoRespositoriArquivo.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.NFE.getValue() + File.separator + PastaBaseArquivoEnum.NOTAS_INUTILIZADAS.getValue() + File.separator + loteInutilizacao.getUnidadeEnsinoVO().getCodigo() + File.separator;
			File diretorioNotasEnviadas = new File(diretorioNotasEnviadasStr);
				if (!diretorioNotasEnviadas.exists()) {
					diretorioNotasEnviadas.mkdirs();
				}
				String nomeArquivoXML = diretorioNotasEnviadasStr + Uteis.getMontarCodigoBarra(loteInutilizacao.getNrInicial().toString() + loteInutilizacao.getNrfinal(), 15) + ".xml";
				UteisNfe.validarCaminhoCertificado(loteInutilizacao.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO(), getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesLocalUploadArquivoFixo());
				if (loteInutilizacao.getNrInicial() != 0 && loteInutilizacao.getNrfinal() != 0) { 
					loteInutilizacao.setXmlRetorno(Inutilizar.A1(
						loteInutilizacao.getUnidadeEnsinoVO().getCidade().getEstado().getCodigoIBGE(), 
						nomeArquivoXML, 
						Uteis.removeCaractersEspeciais(loteInutilizacao.getMotivo().trim()), 
						Uteis.removerMascara(loteInutilizacao.getUnidadeEnsinoVO().getCNPJ()),
						"55", 
						"1",
						loteInutilizacao.getNrInicial().toString(),
						loteInutilizacao.getNrfinal().toString(),
						getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesLocalUploadArquivoFixo(),
						loteInutilizacao.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO(),
						usuarioLogado));
				
				
					loteInutilizacao.setXmlEnvio(UteisNfe.obterXMLNFeOriginalPorCaminho(nomeArquivoXML));

				}
				String codigoRetorno = UteisNfe.obterCodigoRetorno(loteInutilizacao.getXmlRetorno());
				if (codigoRetorno.equals("102")) {
					if (!loteInutilizacao.getXmlRetorno().equals("")) {
						loteInutilizacao.setProtocoloInutilizacao(UteisNfe.obterProtocoloInutilizacao(loteInutilizacao.getXmlRetorno()));
						gravarInutilizacao(loteInutilizacao, usuarioLogado);
					}
				}
				loteInutilizacao.setMensagemRetorno(CodigoRetorno.MensagemRetorno(codigoRetorno));
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void inutilizarNotaWebservice(LoteInutilizacaoVO loteInutilizacao, ConfiguracaoGeralSistemaVO confGeralSistemaWebserviceVO, ConfiguracaoGeralSistemaVO configuracaoRespositoriArquivo, UsuarioVO usuarioLogado) throws Exception {
		try {
			String diretorioNotasEnviadasStr = configuracaoRespositoriArquivo.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.NFE.getValue() + File.separator + PastaBaseArquivoEnum.NOTAS_INUTILIZADAS.getValue() + File.separator + loteInutilizacao.getUnidadeEnsinoVO().getCodigo();
			String nomeArquivoXML = Uteis.getMontarCodigoBarra(loteInutilizacao.getNrInicial().toString() + loteInutilizacao.getNrfinal(), 15) + ".xml";
			UteisNfe.validarCaminhoCertificado(loteInutilizacao.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO(), getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesLocalUploadArquivoFixo());
			if (loteInutilizacao.getNrInicial() != 0 && loteInutilizacao.getNrfinal() != 0) {
				String url = confGeralSistemaWebserviceVO.getUrlWebserviceNFe() + "/servico/inutilizacaoNFe/" + loteInutilizacao.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getTokenWebserviceNFe();
				RestTemplate restTemplate = new RestTemplate();
				NfeVO nfeVO = new NfeVO();
				nfeVO.setCnpjEmit(loteInutilizacao.getUnidadeEnsinoVO().getCNPJ());
				nfeVO.setNNF(loteInutilizacao.getNrInicial().toString());
				nfeVO.setIndSinc(loteInutilizacao.getNrfinal().toString());
				nfeVO.setSerie("1");
				nfeVO.setMotivo(loteInutilizacao.getMotivo().trim());
				nfeVO.setPastaArquivoXML(diretorioNotasEnviadasStr);
				nfeVO.setNomeArquivoXML(nomeArquivoXML);
		    	RequestEntity<NfeVO> request = RequestEntity.post(new URI(url)).contentType(MediaType.APPLICATION_JSON).body(nfeVO);
		    	ResponseEntity<NfeVO> response = restTemplate.exchange(request, NfeVO.class);
		    	NfeVO nfeAux = response.getBody();
		    	loteInutilizacao.setMensagemRetorno(nfeAux.getMotivo());
				loteInutilizacao.setXmlEnvio(nfeAux.getStatus() + " - " + nfeAux.getMotivo() + " - " + nfeAux.getDataRecebimento());
		    	if (nfeAux.getStatus().equals("102")) {
		    		if (!nfeAux.getProtocolo().trim().isEmpty()) {
						loteInutilizacao.setProtocoloInutilizacao(nfeAux.getProtocolo());
						gravarInutilizacao(loteInutilizacao, usuarioLogado);
					}
		    	}
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	public void gravarInutilizacao(final LoteInutilizacaoVO obj, UsuarioVO usuarioLogado) throws Exception {
		try {
			incluir(getIdEntidade(), usuarioLogado);
			final StringBuilder sqlStr = new StringBuilder();
			sqlStr.append(" INSERT INTO loteInutilizacao ( nrInicial , nrfinal, dataInutilizacao, motivo, xmlEnvio, xmlRetorno, usuario, unidadeensino, mensagemRetorno, protocoloInutilizacao ) ");
			sqlStr.append(" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo ");
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sqlStr.toString());

					sqlInserir.setInt(1, obj.getNrInicial());
					sqlInserir.setInt(2, obj.getNrfinal());
					sqlInserir.setTimestamp(3, Uteis.getDataJDBCTimestamp(new Date()));
					sqlInserir.setString(4, obj.getMotivo());
					sqlInserir.setString(5, obj.getXmlEnvio());
					sqlInserir.setString(6, obj.getXmlRetorno());
					sqlInserir.setInt(7, obj.getUsuarioVO().getCodigo());
					sqlInserir.setInt(8, obj.getUnidadeEnsinoVO().getCodigo());
					sqlInserir.setString(9, obj.getMensagemRetorno());
					sqlInserir.setString(10, obj.getProtocoloInutilizacao());
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						return arg0.getInt("codigo");
					}
					return 0;
				}
			}));

		} catch (Exception e) {
			throw e;
		}

	}

	public static String getIdEntidade() {
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		InutilizacaoNotaFiscal.idEntidade = idEntidade;
	}

	

	
}