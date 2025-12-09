package negocio.facade.jdbc.faturamento.nfe;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.gson.Gson;

import controle.arquitetura.AplicacaoControle;
import controle.arquitetura.AssuntoDebugEnum;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ComunicadoInternoDestinatarioVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.PersonalizacaoMensagemAutomaticaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TagsMensagemAutomaticaEnum;
import negocio.comuns.administrativo.enumeradores.TemplateMensagemAutomaticaEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.contabil.enumeradores.TipoOrigemHistoricoBloqueioEnum;
import negocio.comuns.faturamento.nfe.ConfiguracaoNotaFiscalVO;
import negocio.comuns.faturamento.nfe.ItemNfeVO;
import negocio.comuns.faturamento.nfe.NfeVO;
import negocio.comuns.faturamento.nfe.NotaFiscalSaidaServicoVO;
import negocio.comuns.faturamento.nfe.NotaFiscalSaidaVO;
import negocio.comuns.faturamento.nfe.enumeradores.CodigoRegimeTributarioEnum;
import negocio.comuns.faturamento.nfe.enumeradores.SituacaoNotaFiscalSaidaEnum;
import negocio.comuns.faturamento.nfe.enumeradores.TipoIntegracaoNfeEnum;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaReceberRecebimentoVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.utilitarias.AcessoException;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.ProcessarParalelismo;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.PrioridadeComunicadoInterno;
import negocio.comuns.utilitarias.dominios.TipoComunicadoInterno;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.comuns.utilitarias.faturamento.nfe.AmbienteNfeEnum;
import negocio.comuns.utilitarias.faturamento.nfe.CartaCorrecao;
import negocio.comuns.utilitarias.faturamento.nfe.CodigoRetorno;
import negocio.comuns.utilitarias.faturamento.nfe.ConsultarLote;
import negocio.comuns.utilitarias.faturamento.nfe.ConsultarNota;
import negocio.comuns.utilitarias.faturamento.nfe.DadosNfe;
import negocio.comuns.utilitarias.faturamento.nfe.Inutilizar;
import negocio.comuns.utilitarias.faturamento.nfe.StatusServico;
import negocio.comuns.utilitarias.faturamento.nfe.TipoCertificadoEnum;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.comuns.utilitarias.faturamento.nfe.UteisNfe;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.faturamento.nfe.NotaFiscalSaidaInterfaceFacade;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import relatorio.negocio.comuns.arquitetura.SuperParametroRelVO;
import relatorio.negocio.comuns.financeiro.FiltroRelatorioFinanceiroVO;
import webservice.nfse.TipoAcaoServicoNFSEEnum;
import webservice.nfse.WebServicesNFSEEnum;
import webservice.nfse.generic.NaturezaOperacaoEnum;
import webservice.nfse.generic.RegimeEspecialTributacaoEnum;

@Repository
@Scope(value = "singleton")
@Lazy
@Transactional(readOnly = true)
public class NotaFiscalSaida extends ControleAcesso implements NotaFiscalSaidaInterfaceFacade {

	protected static String idEntidade;
	private FileOutputStream os;

	public NotaFiscalSaida() throws Exception {
		super();
		setIdEntidade("NotaFiscalSaida");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.financeiro.teste#novo(negocio.comuns.arquitetura.
	 * UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public NotaFiscalSaidaVO novo(UsuarioVO usuarioLogado) throws Exception {
		incluir(getIdEntidade(), usuarioLogado);
		NotaFiscalSaidaVO obj = new NotaFiscalSaidaVO();
		return obj;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.financeiro.teste#incluir(negocio.comuns.financeiro
	 * .NotaFiscalSaidaVO, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void incluir(final NotaFiscalSaidaVO obj, UsuarioVO usuarioLogado) throws Exception {
		try {
			incluir(getIdEntidade(), usuarioLogado);
			obj.realizarUpperCaseDados();
			final StringBuilder sqlStr = new StringBuilder();
			sqlStr.append(" INSERT INTO NotaFiscalSaida ( dataEmissao, responsavel, aluno, numero, modelo, serie, valorTotal, ");
			sqlStr.append(" identificadorReceita, lote, cancelado, motivoCancelamento, loteCancelamento, recibo, situacao, dataSituacao, observacaoContribuinte, protocolo, ");
			sqlStr.append(" formaPagamento, codigoNaturezaOperacao, nomeNaturezaOperacao, unidadeEnsino, matricula, tipoPessoa, responsavelFinanceiro, funcionario, parceiro, valorTotalPIS, valorTotalCOFINS, ");
			sqlStr.append(" valorTotalINSS, valorTotalIRRF, valorTotalCSLL, valorTotalISSQN, valorLiquido, aliquotaIssqn, aliquotaPis, aliquotaCofins, aliquotaInss, aliquotaCsll, aliquotaIr, ");
			sqlStr.append(" endereco, setor, nomeRazaoSocial, cnpjCpf, cep, inscricaoEstadual, uf, telefone, municipio , informacaoFiliacaoApresentarDanfe, issRetido, naturezaOperacaoEnum, isIncentivadorCultural, codigoCNAE, regimeEspecialTributacaoEnum, percentualCargaTributaria, valorCargaTributaria, fonteCargaTributaria ) ");
			sqlStr.append(" VALUES (?, ?, ?, ?, ?, ? , ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo");
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sqlStr.toString());

					sqlInserir.setDate(1, UteisData.getDataJDBC(obj.getDataEmissao()));
					if (obj.getResponsavel().getCodigo().intValue() != 0) {
						sqlInserir.setInt(2, obj.getResponsavel().getCodigo().intValue());
					} else {
						sqlInserir.setNull(2, 0);
					}
					if (obj.getPessoaVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(3, obj.getPessoaVO().getCodigo().intValue());
					} else {
						sqlInserir.setNull(3, 0);
					}
					sqlInserir.setLong(4, obj.getNumero());
					sqlInserir.setString(5, obj.getModelo());
					sqlInserir.setString(6, obj.getSerie());
					sqlInserir.setDouble(7, obj.getValorTotal());
					sqlInserir.setString(8, obj.getIdentificadorReceita());
					sqlInserir.setString(9, obj.getLote());
					sqlInserir.setBoolean(10, obj.getCancelado());
					sqlInserir.setString(11, obj.getMotivoCancelamento());
					sqlInserir.setString(12, obj.getLoteCancelamento());
					sqlInserir.setString(13, obj.getRecibo());
					sqlInserir.setString(14, obj.getSituacao());
					sqlInserir.setTimestamp(15, UteisData.getDataJDBCTimestamp(obj.getDataStuacao()));
					sqlInserir.setString(16, obj.getObservacaoContribuinte());
					sqlInserir.setString(17, obj.getProtocolo());

					if (obj.getFormaPagamento().getCodigo().intValue() != 0) {
						sqlInserir.setInt(18, obj.getFormaPagamento().getCodigo());
					} else {
						sqlInserir.setNull(18, 0);
					}
					sqlInserir.setString(19, obj.getCodigoNaturezaOperacao());
					sqlInserir.setString(20, obj.getNomeNaturezaOperacao());
					sqlInserir.setInt(21, obj.getUnidadeEnsinoVO().getCodigo());
					sqlInserir.setString(22, obj.getMatricula());
					sqlInserir.setString(23, obj.getTipoPessoa());
					if (obj.getResponsavelFinanceiro().getCodigo().intValue() != 0) {
						sqlInserir.setInt(24, obj.getResponsavelFinanceiro().getCodigo().intValue());
					} else {
						sqlInserir.setNull(24, 0);
					}
					if (obj.getFuncionarioVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(25, obj.getFuncionarioVO().getCodigo());
					} else {
						sqlInserir.setNull(25, 0);
					}
					if (obj.getParceiroVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(26, obj.getParceiroVO().getCodigo().intValue());
					} else {
						sqlInserir.setNull(26, 0);
					}
					sqlInserir.setDouble(27, obj.getValorTotalPIS());
					sqlInserir.setDouble(28, obj.getValorTotalCOFINS());
					sqlInserir.setDouble(29, obj.getValorTotalINSS());
					sqlInserir.setDouble(30, obj.getValorTotalIRRF());
					sqlInserir.setDouble(31, obj.getValorTotalCSLL());
					sqlInserir.setDouble(32, obj.getTotalIssqn());
					sqlInserir.setDouble(33, obj.getValorLiquido());
					sqlInserir.setDouble(34, obj.getAliquotaIssqn());
					sqlInserir.setDouble(35, obj.getAliquotaPis());
					sqlInserir.setDouble(36, obj.getAliquotaCofins());
					sqlInserir.setDouble(37, obj.getAliquotaInss());
					sqlInserir.setDouble(38, obj.getAliquotaCsll());
					sqlInserir.setDouble(39, obj.getAliquotaIr());
					sqlInserir.setString(40, obj.getEndereco());
					sqlInserir.setString(41, obj.getSetor());
					sqlInserir.setString(42, obj.getNomeRazaoSocial());
					sqlInserir.setString(43, obj.getCnpjCpf());
					sqlInserir.setString(44, obj.getCep());
					sqlInserir.setString(45, obj.getInscricaoEstadual());
					sqlInserir.setString(46, obj.getUf());
					sqlInserir.setString(47, obj.getTelefone());
					sqlInserir.setString(48, obj.getMunicipio());
					sqlInserir.setString(49, obj.getInformacaoFiliacaoApresentarDanfe());
					sqlInserir.setBoolean(50, obj.getIssRetido());
					if (obj.getNaturezaOperacaoEnum() != null) {
						sqlInserir.setString(51, obj.getNaturezaOperacaoEnum().name());
					} else {
						sqlInserir.setNull(51, 0);
					}
					sqlInserir.setBoolean(52, obj.getIsIncentivadorCultural());
					sqlInserir.setString(53, obj.getCodigoCNAE());
					if (Uteis.isAtributoPreenchido(obj.getRegimeEspecialTributacaoEnum())) {
						sqlInserir.setString(54, obj.getRegimeEspecialTributacaoEnum().toString());
					} else {
						sqlInserir.setNull(54, 0);
					}
					sqlInserir.setDouble(55, obj.getPercentualCargaTributaria());
					sqlInserir.setDouble(56, obj.getValorCargaTributaria());
					sqlInserir.setString(57, obj.getFonteCargaTributaria());
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
			getFacadeFactory().getNotaFiscalSaidaServicoFacade().incluirNotaFiscalSaidaServicos(obj.getCodigo(), obj.getNotaFiscalSaidaServicoVOs(), usuarioLogado);

			obj.setNovoObj(Boolean.FALSE);

		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);

			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.financeiro.teste#consultar(java.lang.String,
	 * java.lang.String, java.lang.Integer, java.util.Date, java.util.Date,
	 * java.lang.String, java.lang.String, int, int, boolean, int,
	 * negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List consultar(String campoConsulta, String valorConsulta, Date dataInicio, Date dataTermino, String situacao, UnidadeEnsinoVO unidadeEnsinoVO, CursoVO cursoVO, TurmaVO turmaVO, int limite, int pagina, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado, String tipoPessoa) throws Exception {
		int valorInt;
		if (campoConsulta.equals("codigo")) {
			if (valorConsulta != null && !valorConsulta.equals("")) {
				valorInt = Integer.parseInt(valorConsulta);
			} else {
				valorInt = 0;
			}
			return consultarPorCodigo(valorInt, dataInicio, dataTermino, situacao, limite, pagina, controlarAcesso, nivelMontarDados, usuarioLogado);
		}

		if (campoConsulta.equals("sacado")) {
			return consultarPorNomeCliente(valorConsulta, dataInicio, dataTermino, situacao, unidadeEnsinoVO, cursoVO, turmaVO, limite, pagina, controlarAcesso, nivelMontarDados, usuarioLogado);
		}

		if (campoConsulta.equals("matricula")) {
			return consultarPorMatricula(valorConsulta, dataInicio, dataTermino, situacao, unidadeEnsinoVO, cursoVO, turmaVO, limite, pagina, controlarAcesso, nivelMontarDados, usuarioLogado);
		}

		if (campoConsulta.equals("numero")) {
			Long numero;
			if (valorConsulta != null && !valorConsulta.equals("")) {
				numero = Long.valueOf(valorConsulta);
			} else {
				numero = 0L;
			}

			return consultarPorNumero(new Long(numero), dataInicio, dataTermino, situacao, unidadeEnsinoVO, cursoVO, turmaVO, limite, pagina, true, nivelMontarDados, usuarioLogado);
		}
		
		if (campoConsulta.equals("tipoPessoa")) {
			return consultarPorTipoPessoa(valorConsulta, dataInicio, dataTermino, situacao, unidadeEnsinoVO, cursoVO, turmaVO, limite, pagina, controlarAcesso, nivelMontarDados, usuarioLogado, tipoPessoa);
		}

		return new ArrayList(0);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public Integer consultarTotalRegistros(String campoConsulta, String valorConsulta, Integer codigoEmpresa, String transferencia, Date dataInicio, Date dataTermino, String identificador, UsuarioVO usuarioLogado) throws Exception {
		int valorInt;
		if (campoConsulta.equals("codigo")) {
			if (valorConsulta != null && !valorConsulta.equals("")) {
				valorInt = Integer.parseInt(valorConsulta);
			} else {
				valorInt = 0;
			}
			return obterTotalRegistroConsultarPorCodigo((valorInt), codigoEmpresa, dataInicio, dataTermino);
		}

		if (campoConsulta.equals("dataEmissao")) {
			return obterTotalRegistroConsultarPorDataEmissao(codigoEmpresa, transferencia, dataInicio, dataTermino);
		}

		if (campoConsulta.equals("nomeCliente")) {
			return obterTotalRegistroConsultarPorNomeCliente(valorConsulta, codigoEmpresa, transferencia, dataInicio, dataTermino, usuarioLogado);
		}

		if (campoConsulta.equals("situacao")) {
			return obterTotalRegistroConsultarPorSituacao(valorConsulta, codigoEmpresa, transferencia, dataInicio, dataTermino);
		}

		if (campoConsulta.equals("numero")) {
			Long numero;
			if (valorConsulta != null && !valorConsulta.equals("")) {
				numero = Long.valueOf(valorConsulta);
			} else {
				numero = 0L;
			}
			return obterTotalRegistroConsultarPorNumero(numero, transferencia, codigoEmpresa, dataInicio, dataTermino);
		}

		if (campoConsulta.equals("modelo")) {
			return obterTotalRegistroConsultarPorModelo(valorConsulta, dataInicio, dataTermino, codigoEmpresa);
		}

		if (campoConsulta.equals("codigoVisaoCliente")) {

			long valorLong = 0;
			if (valorConsulta != null && !valorConsulta.equals("")) {
				valorLong = Long.parseLong(valorConsulta);
			} else {
				valorLong = 0;
			}

			return obterTotalRegistroConsultarPorNumeroUnicoCliente(valorLong, usuarioLogado.getPessoa().getCodigo());
		}
		if (campoConsulta.equals("dataEmissaoVisaoCliente")) {
			return obterTotalRegistroConsultarDataEmissaoCliente(usuarioLogado.getPessoa().getCodigo(), dataInicio, dataTermino);
		}

		return 0;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public Integer obterTotalRegistroConsultarPorModelo(String valorConsulta, Date prmIni, Date prmFim, Integer codigoEmpresa) throws Exception {
		StringBuilder sqlStr = new StringBuilder("SELECT COUNT(notafiscalsaida.codigo) FROM NotaFiscalSaida WHERE 1=1");
		if (valorConsulta != null) {
			sqlStr.append(" and upper( modelo ) like('").append(valorConsulta.toUpperCase()).append("%') ");
		}

		if (codigoEmpresa.intValue() != 0) {
			sqlStr.append(" and empresa = ").append(codigoEmpresa.intValue());
		}
		if (prmIni != null && prmFim != null) {
			sqlStr.append(" and ((dataemissao>= '");
			sqlStr.append(UteisData.getDataJDBC(prmIni));
			sqlStr.append("') and (dataemissao <= '");
			sqlStr.append(UteisData.getDataJDBC(prmFim));
			sqlStr.append("')) ");
		} else if (prmIni != null) {
			sqlStr.append(" and ((dataemissao >= '");
			sqlStr.append(UteisData.getDataJDBC(prmIni));
			sqlStr.append("')) ");
		} else if (prmFim != null) {
			sqlStr.append(" and ((dataemissao <= '");
			sqlStr.append(UteisData.getDataJDBC(prmFim));
			sqlStr.append("')) ");
		}

		return (getTotalRegistro(getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString())));

	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public Integer obterTotalRegistroConsultarPorNumero(Long valorConsulta, String transferencia, Integer codigoEmpresa, Date prmIni, Date prmFim) throws Exception {
		StringBuilder sqlStr = new StringBuilder("SELECT COUNT(notafiscalsaida.codigo) FROM NotaFiscalSaida ");
		if (valorConsulta != 0) {
			sqlStr.append("WHERE  numero = ").append(valorConsulta).append(" ");
		} else {
			sqlStr.append("WHERE numero >= ").append(valorConsulta).append(" ");
		}
		if (transferencia.equals("S")) {
			sqlStr.append("SELECT * FROM NotaFiscalSaida WHERE numero =").append(valorConsulta).append(" and transferencia = true ");
		}
		if (transferencia.equals("N")) {
			sqlStr.append("SELECT * FROM NotaFiscalSaida WHERE numero =").append(valorConsulta).append(" and transferencia = false ");
		}
		if (codigoEmpresa.intValue() != 0) {
			sqlStr.append(" and empresa = ").append(codigoEmpresa.intValue());
		}

		if (prmIni != null && prmFim != null) {
			sqlStr.append(" and ((dataemissao>= '");
			sqlStr.append(UteisData.getDataJDBC(prmIni));
			sqlStr.append("') and (dataemissao <= '");
			sqlStr.append(UteisData.getDataJDBC(prmFim));
			sqlStr.append("')) ");
		} else if (prmIni != null) {
			sqlStr.append(" and ((dataemissao >= '");
			sqlStr.append(UteisData.getDataJDBC(prmIni));
			sqlStr.append("')) ");
		} else if (prmFim != null) {
			sqlStr.append(" and ((dataemissao <= '");
			sqlStr.append(UteisData.getDataJDBC(prmFim));
			sqlStr.append("')) ");
		}

		return (getTotalRegistro(getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString())));
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public Integer obterTotalRegistroConsultarPorSituacao(String valorConsulta, Integer codigoEmpresa, String transferencia, Date prmIni, Date prmFim) throws Exception {
		StringBuilder sqlStr = new StringBuilder("SELECT COUNT(NotaFiscalSaida.codigo) FROM NotaFiscalSaida WHERE 1=1");
		if (!valorConsulta.isEmpty()) {
			sqlStr.append(" and upper(situacao) = '").append(valorConsulta.toUpperCase()).append("' ");
		}
		if (codigoEmpresa.intValue() != 0) {
			sqlStr.append(" and empresa = ").append(codigoEmpresa.intValue());
		}

		if (prmIni != null && prmFim != null) {
			sqlStr.append(" and ((dataemissao>= '");
			sqlStr.append(UteisData.getDataJDBC(prmIni));
			sqlStr.append("') and (dataemissao <= '");
			sqlStr.append(UteisData.getDataJDBC(prmFim));
			sqlStr.append("')) ");
		} else if (prmIni != null) {
			sqlStr.append(" and ((dataemissao >= '");
			sqlStr.append(UteisData.getDataJDBC(prmIni));
			sqlStr.append("')) ");
		} else if (prmFim != null) {
			sqlStr.append(" and ((dataemissao <= '");
			sqlStr.append(UteisData.getDataJDBC(prmFim));
			sqlStr.append("')) ");
		}

		return (getTotalRegistro(getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString())));
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public Integer obterTotalRegistroConsultarPorCodigo(Integer valorConsulta, Integer codigoEmpresa, Date prmIni, Date prmFim) throws Exception {
		StringBuilder sqlStr = new StringBuilder("SELECT COUNT(notaFiscalSaida.codigo)  FROM NotaFiscalSaida WHERE codigo >= " + valorConsulta.intValue() + " ");
		if (codigoEmpresa.intValue() != 0) {
			sqlStr.append(" and empresa = ").append(codigoEmpresa.intValue());
		}

		if (valorConsulta.intValue() == 0) {
			if (prmIni != null && prmFim != null) {
				sqlStr.append(" and ((dataemissao>= '");
				sqlStr.append(UteisData.getDataJDBC(prmIni));
				sqlStr.append("') and (dataemissao <= '");
				sqlStr.append(UteisData.getDataJDBC(prmFim));
				sqlStr.append("')) ");
			} else if (prmIni != null) {
				sqlStr.append(" and ((dataemissao >= '");
				sqlStr.append(UteisData.getDataJDBC(prmIni));
				sqlStr.append("')) ");
			} else if (prmFim != null) {
				sqlStr.append(" and ((dataemissao <= '");
				sqlStr.append(UteisData.getDataJDBC(prmFim));
				sqlStr.append("')) ");
			}
		}

		return (getTotalRegistro(getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString())));
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public Integer obterTotalRegistroConsultarPorDataEmissao(Integer codigoEmpresa, String transferencia, Date prmIni, Date prmFim) throws Exception {
		StringBuilder sqlStr = new StringBuilder("SELECT COUNT(NotaFiscalSaida.codigo) FROM NotaFiscalSaida WHERE 1=1");

		if (prmIni != null && prmFim != null) {
			sqlStr.append(" and ((dataEmissao>= '");
			sqlStr.append(UteisData.getDataJDBC(prmIni));
			sqlStr.append("') and (dataEmissao <= '");
			sqlStr.append(UteisData.getDataJDBC(prmFim));
			sqlStr.append("')) ");
		} else if (prmIni != null) {
			sqlStr.append(" and ((dataEmissao >= '");
			sqlStr.append(UteisData.getDataJDBC(prmIni));
			sqlStr.append("')) ");
		} else if (prmFim != null) {
			sqlStr.append(" and ((dataEmissao <= '");
			sqlStr.append(UteisData.getDataJDBC(prmFim));
			sqlStr.append("')) ");
		}

		if (transferencia.equals("S")) {
			sqlStr.append(" and transferencia = true ");
		}
		if (transferencia.equals("N")) {
			sqlStr.append(" and transferencia = false ");
		}

		if (codigoEmpresa.intValue() != 0) {
			sqlStr.append(" and empresa = ").append(codigoEmpresa.intValue());
		}

		return (getTotalRegistro(getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString())));
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public Integer obterTotalRegistroConsultarPorNomeCliente(String valorConsulta, Integer codigoEmpresa, String transferencia, Date prmIni, Date prmFim, UsuarioVO usuarioLogado) throws Exception {
		// List<ClienteVO> listaClientes =
		// getCliente().consultarPorNome(valorConsulta, "", false,
		// Uteis.NIVELMONTARDADOS_DADOSBASICOS);
		StringBuilder sqlStr = new StringBuilder("SELECT COUNT(notafiscalsaida.codigo) FROM NotaFiscalSaida WHERE 1=1 ");
		List<PessoaVO> listaClientes = null;
		if (listaClientes != null && !valorConsulta.isEmpty()) {
			listaClientes = getFacadeFactory().getPessoaFacade().consultarPorNome(valorConsulta, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado);
			if (listaClientes.isEmpty()) {
				return 0;
			}
		}
		if (listaClientes != null) {
			int x = 0;
			for (PessoaVO cliente : listaClientes) {
				if (x == 0) {
					sqlStr.append(" and (destinatario = ").append(cliente.getCodigo().intValue());
					x = 1;
				} else {
					sqlStr.append(" or destinatario = ").append(cliente.getCodigo().intValue());
				}

			}
			sqlStr.append(" ) ");
		}

		if (transferencia.equals("S")) {
			sqlStr.append(" and transferencia = true ");
		}
		if (transferencia.equals("N")) {
			sqlStr.append(" and transferencia = false ");
		}
		if (codigoEmpresa.intValue() != 0) {
			sqlStr.append(" and empresa = ").append(codigoEmpresa.intValue());
		}

		if (prmIni != null && prmFim != null) {
			sqlStr.append(" and ((dataemissao>= '");
			sqlStr.append(UteisData.getDataJDBC(prmIni));
			sqlStr.append("') and (dataemissao <= '");
			sqlStr.append(UteisData.getDataJDBC(prmFim));
			sqlStr.append("')) ");
		} else if (prmIni != null) {
			sqlStr.append(" and ((dataemissao >= '");
			sqlStr.append(UteisData.getDataJDBC(prmIni));
			sqlStr.append("')) ");
		} else if (prmFim != null) {
			sqlStr.append(" and ((dataemissao <= '");
			sqlStr.append(UteisData.getDataJDBC(prmFim));
			sqlStr.append("')) ");
		}

		return (getTotalRegistro(getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString())));

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void alterar(final NotaFiscalSaidaVO obj, UsuarioVO usuarioLogado) throws Exception {
		alterar(getIdEntidade(), usuarioLogado);
		final StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" UPDATE notafiscalsaida set tipopessoa = ?, aluno = ?, responsavelfinanceiro = ?, endereco= ?, setor= ?, nomeRazaoSocial= ?, cnpjCpf= ?, cep= ?, inscricaoEstadual= ?, uf= ?, telefone= ?, municipio = ?, issretido = ?, naturezaOperacaoEnum = ?, isIncentivadorCultural = ?, codigoCNAE = ?, regimeEspecialTributacaoEnum = ?, numeroRPS = ? WHERE codigo = ? ");

		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sqlStr.toString());
				sqlAlterar.setString(1, obj.getTipoPessoa());
				if (obj.getPessoaVO().getCodigo() != 0) {
					sqlAlterar.setInt(2, obj.getPessoaVO().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(2, 0);
				}
				if (obj.getResponsavelFinanceiro().getCodigo() != 0) {
					sqlAlterar.setInt(3, obj.getResponsavelFinanceiro().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(3, 0);
				}
				sqlAlterar.setString(4, obj.getEndereco());
				sqlAlterar.setString(5, obj.getSetor());
				sqlAlterar.setString(6, obj.getNomeRazaoSocial());
				sqlAlterar.setString(7, obj.getCnpjCpf());
				sqlAlterar.setString(8, obj.getCep());
				sqlAlterar.setString(9, obj.getInscricaoEstadual());
				sqlAlterar.setString(10, obj.getUf());
				sqlAlterar.setString(11, obj.getTelefone());
				sqlAlterar.setString(12, obj.getMunicipio());
				sqlAlterar.setBoolean(13, obj.getIssRetido());
				if (obj.getNaturezaOperacaoEnum() != null) {
					sqlAlterar.setString(14, obj.getNaturezaOperacaoEnum().name());
				} else {
					sqlAlterar.setNull(14, 0);
				}
				sqlAlterar.setBoolean(15, obj.getIsIncentivadorCultural());
				sqlAlterar.setString(16, obj.getCodigoCNAE());
				if (Uteis.isAtributoPreenchido(obj.getRegimeEspecialTributacaoEnum())) {
					sqlAlterar.setString(17, obj.getRegimeEspecialTributacaoEnum().toString());
				} else {
					sqlAlterar.setNull(17, 0);
				}
				sqlAlterar.setInt(18, obj.getNumeroRPS());
				sqlAlterar.setInt(19, obj.getCodigo().intValue());
				return sqlAlterar;
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.financeiro.teste#gravarRecibo(java.lang.Integer,
	 * java.lang.String)
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void gravarRecibo(Integer codigo, String recibo, UsuarioVO usuarioLogado) throws Exception {
		try {

			String sql = "UPDATE NotaFiscalSaida set recibo=? WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(sql, new Object[] { recibo, codigo });

		} catch (Exception e) {

			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.financeiro.teste#gravarXmlEnvio(java.lang.Integer,
	 * java.lang.String)
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void gravarXmlEnvio(Integer codigo, String xmlEnvio, UsuarioVO usuarioLogado) throws Exception {
		try {
			String sql = "UPDATE NotaFiscalSaida set xmlEnvio=? WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(sql, new Object[] { xmlEnvio, codigo });
		} catch (Exception e) {
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.financeiro.teste#gravarXmlCancelamento(java.lang.
	 * Integer, java.lang.String)
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void gravarXmlCancelamento(Integer codigo, String xmlCancelamento, UsuarioVO usuarioLogado) throws Exception {
		try {
			String sql = "UPDATE NotaFiscalSaida set xmlCancelamento=? WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(sql, new Object[] { xmlCancelamento, codigo });
		} catch (Exception e) {
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.financeiro.teste#gravarSituacaoEnvio(java.lang.Integer ,
	 * java.lang.String, java.lang.String, java.util.Date, java.lang.String)
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void gravarSituacaoEnvio(NotaFiscalSaidaVO notaFiscal, String situacao, String protocolo, Date dataSituacao, String identificadorReceita, ConfiguracaoNotaFiscalVO configuracaoNotaFiscalVO, UsuarioVO usuarioLogado) throws Exception {
		try {
			if (configuracaoNotaFiscalVO.getTipoIntegracaoNfeEnum().equals(TipoIntegracaoNfeEnum.NFSE) && (situacao.equals(SituacaoNotaFiscalSaidaEnum.AUTORIZADA.getValor()) || situacao.equals(SituacaoNotaFiscalSaidaEnum.ENVIADA.getValor()))) {
				gravarNumeroNotaFiscal(notaFiscal, configuracaoNotaFiscalVO, notaFiscal.getNumero(),usuarioLogado);
			}
			String sql = "UPDATE NotaFiscalSaida set situacao=?, dataSituacao=?, dataEmissao=?, protocolo=?, identificadorReceita=? WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(sql, new Object[] { situacao, UteisData.getDataJDBCTimestamp(dataSituacao), UteisData.getDataJDBCTimestamp(notaFiscal.getDataEmissao()), protocolo, identificadorReceita, notaFiscal.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void gravarEnvioNFSeWebservice(NotaFiscalSaidaVO notaFiscal, UsuarioVO usuarioLogado) throws Exception {
		try {
//			if (notaFiscal.getSituacao().equals(SituacaoNotaFiscalSaidaEnum.AUTORIZADA.getValor()) ||
//					notaFiscal.getSituacao().equals(SituacaoNotaFiscalSaidaEnum.ENVIADA.getValor())) {
//				String sqlStr = "UPDATE configuracaonotafiscal set numeronota=?  WHERE codigo = " + notaFiscal.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCodigo() + " and numeronota < ?";
//				getConexao().getJdbcTemplate().update(sqlStr, new Object[] { notaFiscal.getNumeroRPS(), notaFiscal.getNumeroRPS() });
//			}
			String sql = "UPDATE NotaFiscalSaida set situacao=?, dataSituacao=?, xmlEnvio=?, mensagemretorno=?, numeroRPS=?, lote=?, numero=?, protocolo=?, linkacesso=? WHERE codigo=?";
			getConexao().getJdbcTemplate().update(sql, new Object[] { notaFiscal.getSituacao(), UteisData.getDataJDBCTimestamp(notaFiscal.getDataStuacao()),
					notaFiscal.getXmlEnvio(), notaFiscal.getMensagemRetorno(), notaFiscal.getNumeroRPS(), notaFiscal.getLote(),
					notaFiscal.getNumero(), notaFiscal.getProtocolo(), notaFiscal.getLinkAcesso(), notaFiscal.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.financeiro.teste#gravarSituacaoCancelamento(java.
	 * lang.Integer, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void gravarSituacaoCancelamento(Integer codigo, String situacao, String protocolo, String xmlCancelamento, String motivo, UsuarioVO usuarioLogado) throws Exception {
		try {
			String sql = "UPDATE notafiscalsaida set situacao=?, protocoloCancelamento=?, xmlCancelamento=?, motivoCancelamento = ? WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(sql, new Object[] { situacao, protocolo, xmlCancelamento, motivo, codigo });
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void gravarSituacaoCartaCorrecao(NotaFiscalSaidaVO nf, UsuarioVO usuarioLogado) throws Exception {
		try {
			String sql = "UPDATE NotaFiscalSaida set descricaoCartaCorrecao = ?, seqEventoCartaCorrecao = ?, protocoloCartaCorrecao = ?, xmlCartaCorrecao = ? WHERE codigo = ?";
			getConexao().getJdbcTemplate().update(sql, new Object[] { nf.getDescricaoCartaCorrecao(), nf.getSeqEventoCartaCorrecao(), nf.getProtocoloCartaCorrecao(), nf.getXmlCartaCorrecao(), nf.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.financeiro.teste#gravarSituacaoInutilizado(java.lang
	 * .Integer, java.lang.String, java.lang.String, java.lang.String,
	 * negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void gravarSituacaoInutilizado(Integer codigo, String situacao, String protocolo, String motivo, UsuarioVO usuarioLogado) throws Exception {
		try {
			String sql = "UPDATE NotaFiscalSaida set situacao=?, protocoloInutilizacao=?, motivoInutilizacao=? WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(sql, new Object[] { situacao, protocolo, motivo, codigo });
		} catch (Exception e) {
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.financeiro.teste#inicializarDadosObjetoEnvio(OtimizeNFe
	 * .nfeVersao2.NfeVO, negocio.comuns.financeiro.NotaFiscalSaidaVO,
	 * java.lang.String)
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void inicializarDadosObjetoEnvio(NfeVO nfeVO, NotaFiscalSaidaVO nota, ConfiguracaoNotaFiscalVO conNotaFiscalVO,ConfiguracaoGeralSistemaVO configuracaoRespositorioArquivo, UsuarioVO usuarioLogado) throws Exception {
		nfeVO.setNNF(nota.getNumero().toString());

		nfeVO.setCrt(nota.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCodigoRegimeTributarioEnum().getKey().toString());
		nfeVO.setIdNfe(nota.getIdentificadorReceita());

		nfeVO.setDataEmissao(UteisData.getDataAplicandoFormatacao(new Date(), "yyyy-MM-dd'T'HH:mm:ss"));
		nfeVO.setTpNF(DadosNfe.NFE_SAIDA);
		nfeVO.setCodigoIBGEMunicipio(nota.getUnidadeEnsinoVO().getCidade().getCodigoIBGE().toString().trim());
		nfeVO.setTipoImpressao(DadosNfe.PAISAGEM);

		nfeVO.setTipoEmissao("1");

		nfeVO.setDataSaidaEntrada(UteisData.getDataAplicandoFormatacao(new Date(), "yyyy-MM-dd'T'HH:mm:ss"));

		nfeVO.setAmbiente(nota.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getAmbienteNfeEnum().getKey().toString());
		nfeVO.setFinalidadeEmissao(DadosNfe.FINALIDADE_EMISSAO_NFE_NORMAL);
		nfeVO.setProcessoEmissao(DadosNfe.EMISSAO_CONTRIBUITE);

		montarDadosEmitenteNotaFiscal(nfeVO, nota, conNotaFiscalVO);
		montarDadosDestinatarioNotaFiscal(nfeVO, nota);

		nfeVO.setObservacao(nota.getObservacao().trim());
		nfeVO.setObservacaoContribuinte(nota.getObservacaoContribuinte().trim());
		nfeVO.setTotalBaseSubTrib(nota.getTotalBaseSubTrib().toString());
		nfeVO.setTotalSubTrib(nota.getTotalSubTrib().toString());

		nfeVO.setValorTotal(Uteis.getValorTruncadoDeDoubleParaString(nota.getValorTotal(), 2));
		nfeVO.setTotalIssqn(nota.getTotalIssqn());
		nfeVO.setTotalBaseIssqn(nota.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getIssqn());

		nfeVO.setIdOperacaoConsumidorFinal("1");
		nfeVO.setIndPresencaComprador(DadosNfe.OPERACAO_NAO_PRESENCIAL_OUTROS);

		nfeVO.setModelo(nota.getModelo());
		nfeVO.setSerie(nota.getSerie());

		if (AmbienteNfeEnum.PRODUCAO.equals(nota.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getAmbienteNfeEnum())) {
			nfeVO.setIdLote(String.valueOf(nota.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getLote()));
		} else {
			nfeVO.setIdLote(String.valueOf(nota.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getLoteHomologacao()));
		}
		nfeVO.setIndSinc("0");
		nota.setValorTotalPIS(0.00);
		nota.setValorTotalCOFINS(0.00);
		ItemNfeVO item = null;
		for (NotaFiscalSaidaServicoVO nServicoVO : nota.getNotaFiscalSaidaServicoVOs()) {
			item = new ItemNfeVO();
			ContaReceberVO contaRecVO = new ContaReceberVO();
			contaRecVO.setCodigo(nServicoVO.getContaReceberVO().getCodigo());
			if (Uteis.isAtributoPreenchido(contaRecVO.getCodigo())) {
				getFacadeFactory().getContaReceberFacade().carregarDados(contaRecVO, NivelMontarDados.BASICO, new ConfiguracaoFinanceiroVO(), null);
			}
			item.setNomeProduto(nServicoVO.getDescricao());
			
			if (UteisNfe.retirarCaracteresEspeciais(Uteis.removerAcentuacao(item.getNomeProduto().trim())).trim().length() >= 120) {
	        	String stringFormatada = UteisNfe.retirarCaracteresEspeciais(Uteis.removerAcentuacao(item.getNomeProduto().trim())).trim().substring(0, 120);
	        	item.setNomeProduto(stringFormatada);
	        }
			
			item.setCodigoProduto(contaRecVO.getCentroReceita().getCodigo().toString());
			item.setValorProd(nServicoVO.getPrecoUnitario().toString());
			item.setAliquotaIssqn(nServicoVO.getAliquotaIssqn().toString());
			item.setValorImpostoIssqn(nServicoVO.getTotalIssqn().toString());
			item.setValorBaseCalcImpostoIssqn(nServicoVO.getBaseIssqn().toString());
			if (nota.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getPis() != null
			        && !nota.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getPis().equals(0.00)) {
				nServicoVO.setTotalPIS(Uteis.arrendondarForcando2CadasDecimais(nServicoVO.getPrecoUnitario() * (nota.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getPis() / 100)));
			}
			if (nota.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCofins() != null
			        && !nota.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCofins().equals(0.00)) {
				nServicoVO.setTotalCOFINS(Uteis.arrendondarForcando2CadasDecimais(nServicoVO.getPrecoUnitario() * (nota.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCofins() / 100)));
			}
			item.setValorTotalPIS(nServicoVO.getTotalPIS().toString());
			item.setValotTotalCOFINS(nServicoVO.getTotalCOFINS().toString());
			item.setCfop(nfeVO.getCodigoNaturezaOperacao());
			item.setNcm(nServicoVO.getCodigoNCM());
			item.setCstPIS(conNotaFiscalVO.getCstPis());
			item.setAliquotaPIS(conNotaFiscalVO.getPis().toString());
			item.setCstCOFINS(conNotaFiscalVO.getCstCofins());
			item.setAliquotaCOFINS(conNotaFiscalVO.getCofins().toString());
			nfeVO.getItemNfeVOs().add(item);
			nota.setValorTotalPIS(nota.getValorTotalPIS() + nServicoVO.getTotalPIS());
			nota.setValorTotalCOFINS(nota.getValorTotalCOFINS() + nServicoVO.getTotalCOFINS());
			nServicoVO.setAliquotaPIS(nota.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getPis());
			nServicoVO.setAliquotaCOFINS(nota.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCofins());
			if (nota.getIssRetido()) {
				item.setValorTotalRetencaoIss(nServicoVO.getTotalIssqn().toString());
				nfeVO.setIssRetido(true);
				nfeVO.setValorRetencaoIss(nfeVO.getValorRetencaoIss() + nServicoVO.getTotalIssqn());
			}
			nServicoVO.getNotaFiscalSaida().setCodigo(nota.getCodigo());
			getFacadeFactory().getNotaFiscalSaidaServicoFacade().alterar(nServicoVO, usuarioLogado);
			item = null;
		}
		nfeVO.setValorTotalPIS(nota.getValorTotalPIS().toString());
		nfeVO.setValorTotalCOFINS(nota.getValorTotalCOFINS().toString());
		if (nota.getTipoPessoa().equals(TipoPessoa.PARCEIRO.getValor()) && nota.getParceiroVO().getPossuiAliquotaEmissaoNotaEspecifica()) {
			nfeVO.setPossuiAliquotasEspecificasParceiro(nota.getParceiroVO().getPossuiAliquotaEmissaoNotaEspecifica());
			nfeVO.setValorRetidoPIS(Uteis.arrendondarForcando2CadasDecimaisStr(new Double(nfeVO.getValorTotal()) * (nota.getParceiroVO().getPis() / 100)));
			nfeVO.setValorRetidoCOFINS(Uteis.arrendondarForcando2CadasDecimaisStr(new Double(nfeVO.getValorTotal()) * (nota.getParceiroVO().getCofins() / 100)));
			nfeVO.setValorRetidoCSLL(Uteis.arrendondarForcando2CadasDecimaisStr(new Double(nfeVO.getValorTotal()) * (nota.getParceiroVO().getCsll() / 100)));
			nfeVO.setBaseCalculoIRRF(Uteis.arrendondarForcando2CadasDecimaisStr(nota.getParceiroVO().getIRRF()));
			nfeVO.setValorRetidoIRRF(Uteis.arrendondarForcando2CadasDecimaisStr(new Double(nfeVO.getValorTotal()) * (nota.getParceiroVO().getIRRF() / 100)));
			nfeVO.setBaseCalculoRetencaoPrevidenciaSocial(Uteis.arrendondarForcando2CadasDecimaisStr(nota.getParceiroVO().getInss()));
			nfeVO.setValorRetidoPrevidenciaSocial(Uteis.arrendondarForcando2CadasDecimaisStr(new Double(nfeVO.getValorTotal()) * (nota.getParceiroVO().getInss() / 100)));
		}
		gravarPISCOFINS(nota.getCodigo(), nota.getValorTotalPIS(), nota.getValorTotalCOFINS(), usuarioLogado);
		
		nota.setObservacaoContribuinte(montarDescrimicaoNotaFiscalServico(nfeVO.getObservacaoContribuinte(), new Object[] { nfeVO.getNomeDest(), getFacadeFactory().getCursoFacade().consultaRapidaPorMatricula(nota.getMatricula(), false, usuarioLogado).getNome().replace("&", "E"), nota.getTipoPessoa(), Uteis.getMesReferenciaData(nota.getDataEmissao()), nota.getNomesConvenios() }));
		nfeVO.setObservacaoContribuinte(nota.getObservacaoContribuinte());
		nfeVO.setPastaArquivoXML(configuracaoRespositorioArquivo.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.NFE.getValue() + File.separator + PastaBaseArquivoEnum.NOTAS_ENVIADAS.getValue() + File.separator + nota.getUnidadeEnsinoVO().getCodigo());
		nfeVO.setNomeArquivoXML(Uteis.getMontarCodigoBarra(nota.getNumero().toString(), 9) + ".xml");		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.financeiro.teste#getTipoIcms(java.lang.Double,
	 * java.lang.Double, java.lang.Double, java.lang.Double, java.lang.Double,
	 * java.lang.Double, java.lang.Boolean, java.lang.Double)
	 */
	@Override
	public String getTipoIcms(Double aliquotaICMS, Double baseICMS, Double precoItem, Double aliquotaSubTrib, Double baseSubTrib, Double totalSubTrib, Boolean baseReduzida, Double valorIPI) {
		if (Uteis.arrendondarForcando2CasasDecimais(baseICMS.doubleValue()) == Uteis.arrendondarForcando2CasasDecimais(precoItem.doubleValue()) && !baseReduzida && Uteis.arrendondarForcando2CasasDecimais(baseSubTrib.doubleValue()) > 0) {
			return DadosNfe.TRIBUTACAO_ICMS_COM_SUB_TRUB;
		}

		if (Uteis.arrendondarForcando2CasasDecimais(baseICMS.doubleValue()) > 0 && baseReduzida && Uteis.arrendondarForcando2CasasDecimais(baseSubTrib.doubleValue()) > 0) {
			return DadosNfe.TRIBUTACAO_ICMS_COM_REDUCAO_BASE_CALCULO_COM_SUB_TRIB;
		}

		if (Uteis.arrendondarForcando2CasasDecimais(baseICMS.doubleValue()) == 0 && Uteis.arrendondarForcando2CasasDecimais(baseSubTrib.doubleValue()) > 0 && Uteis.arrendondarForcando2CasasDecimais(aliquotaSubTrib.doubleValue()) == 0 && Uteis.arrendondarForcando2CasasDecimais(totalSubTrib.doubleValue()) > 0) {
			return DadosNfe.TRIBUTACAO_ICMS_ISENTA_COM_SUB_TRIB_ANTERIOR;
		}

		if (Uteis.arrendondarForcando2CasasDecimais(baseICMS.doubleValue()) == 0 && Uteis.arrendondarForcando2CasasDecimais(baseSubTrib.doubleValue()) > 0) {
			return DadosNfe.TRIBUTACAO_ICMS_ISENTA_COM_SUB_TRIB;
		}

		if (Uteis.arrendondarForcando2CasasDecimais(baseICMS.doubleValue()) == 0) {
			return DadosNfe.TRIBUTACAO_ICMS_ISENTA;
		}

		if (Uteis.arrendondarForcando2CasasDecimais(baseICMS.doubleValue()) == Uteis.arrendondarForcando2CasasDecimais(precoItem.doubleValue()) && !baseReduzida) {
			return DadosNfe.TRIBUTACAO_ICMS_INTEGRAL;
		} else if (baseReduzida) {
			return DadosNfe.TRIBUTACAO_ICMS_COM_REDUCAO_BASE_CALCULO;
		}

		if (Uteis.arrendondarForcando2CasasDecimais(Uteis.arrendondarForcando2CasasDecimais(baseICMS.doubleValue() - valorIPI)) == Uteis.arrendondarForcando2CasasDecimais(precoItem.doubleValue()) && !baseReduzida) {
			return DadosNfe.TRIBUTACAO_ICMS_INTEGRAL;
		}

		return DadosNfe.TRIBUTACAO_ICMS_OUTROS;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void alterarObservacaoContribuinte(NotaFiscalSaidaVO obj, UsuarioVO usuarioLogado) throws Exception {
		alterar(getIdEntidade(), usuarioLogado);
		String sql = "UPDATE NotaFiscalSaida set observacaoContribuinte = ? WHERE codigo = ?";
		getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getObservacaoContribuinte(), obj.getCodigo() });
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void alterarMensagemRetorno(NotaFiscalSaidaVO obj, UsuarioVO usuarioLogado) throws Exception {
		alterar(getIdEntidade(), usuarioLogado);
		String sql = "UPDATE NotaFiscalSaida set mensagemretorno = ? WHERE codigo = ?";
		getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getMensagemRetorno(), obj.getCodigo() });
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void alterarXmlNfeProc(NotaFiscalSaidaVO obj, UsuarioVO usuarioLogado) throws Exception {
		if(Uteis.isAtributoPreenchido( obj.getXmlNfeProc()) && Uteis.isAtributoPreenchido( obj.getCodigo())) {
			alterar(getIdEntidade(), usuarioLogado);
			String sql = "UPDATE NotaFiscalSaida set xmlnfeproc = ? WHERE codigo = ?";
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getXmlNfeProc(),  });
		}
		
	}

	private void gerarProcNFe(NotaFiscalSaidaVO notaSaidaVO, ConfiguracaoGeralSistemaVO configuracaoRespositoriArquivo) throws TransformerException, ConsistirException {
		try {
			String xmlEnvio = notaSaidaVO.getXmlEnvio();
			String xmlRetorno = notaSaidaVO.getRetornoCompleto();
			Document document = documentFactory(xmlEnvio);
			NodeList nodeListNfe = document.getDocumentElement().getElementsByTagName("NFe");
			NodeList nodeListInfNfe = document.getElementsByTagName("infNFe");

			for (int i = 0; i < nodeListNfe.getLength(); i++) {
				Element el = (Element) nodeListInfNfe.item(i);
				String chaveNFe = el.getAttribute("Id");

				String xmlNFe = outputXML(nodeListNfe.item(i));
				String xmlProtNFe = getProtNFe(xmlRetorno, chaveNFe);

				String diretorioXml = configuracaoRespositoriArquivo.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.NFE.getValue() + File.separator + PastaBaseArquivoEnum.NOTAS_ENVIADAS.getValue() + File.separator + notaSaidaVO.getUnidadeEnsinoVO().getCodigo() + File.separator + Uteis.getMontarCodigoBarra(notaSaidaVO.getNumero().toString(), 9) + ".xml";

				os = new FileOutputStream(diretorioXml);
				os.write(buildNFeProc(xmlNFe, xmlProtNFe).getBytes());
				notaSaidaVO.setXmlNfeProc(buildNFeProc(xmlNFe, xmlProtNFe));
				os.flush();
				os.close();
			}
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static String buildNFeProc(String xmlNFe, String xmlProtNFe) {
		StringBuilder nfeProc = new StringBuilder();
		nfeProc.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
		        .append("<nfeProc versao=\"2.00\" xmlns=\"http://www.portalfiscal.inf.br/nfe\">")
		        .append(xmlNFe)
		        .append(xmlProtNFe)
		        .append("</nfeProc>");
		return nfeProc.toString();
	}

	private static String getProtNFe(String xml, String chaveNFe) throws SAXException,
	        IOException, ParserConfigurationException, TransformerException {
		Document document = documentFactory(xml);
		NodeList nodeListProtNFe = document.getDocumentElement().getElementsByTagName("protNFe");
		NodeList nodeListChNFe = document.getElementsByTagName("chNFe");
		for (int i = 0; i < nodeListProtNFe.getLength(); i++) {
			Element el = (Element) nodeListChNFe.item(i);
			String chaveProtNFe = el.getTextContent();
			if (chaveNFe.contains(chaveProtNFe)) {
				return outputXML(nodeListProtNFe.item(i));
			}
		}
		return "";
	}

	private static String outputXML(Node node) throws TransformerException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer trans = tf.newTransformer();
		trans.transform(new DOMSource(node), new StreamResult(os));
		String xml = os.toString();
		if ((xml != null) && (!"".equals(xml))) {
			xml = xml.replaceAll("<\\?xml version=\"1.0\" encoding=\"UTF-8\"\\?>", "");
		}
		return xml;
	}

	private static Document documentFactory(String xml) throws SAXException, IOException, ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		Document document = factory.newDocumentBuilder().parse(new ByteArrayInputStream(xml.getBytes()));
		return document;
	}

	public static String lerXML(String fileXML) throws IOException {
		String linha = "";
		StringBuilder xml = new StringBuilder();

		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileXML)));
		while ((linha = in.readLine()) != null) {
			xml.append(linha);
		}
		in.close();

		return xml.toString();
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void realizarEnvioEmailNotaFiscal(NotaFiscalSaidaVO notaSaidaVO, ConfiguracaoGeralSistemaVO configuracaoRepositorioArquivo, ConfiguracaoGeralSistemaVO configuracaoEmail,Boolean notificarAlunoNotaFiscalGerada , UsuarioVO usuarioLogado) throws Exception {
		if (notaSaidaVO.getSituacao().equals(SituacaoNotaFiscalSaidaEnum.AUTORIZADA.getValor()) &&  notificarAlunoNotaFiscalGerada ) {
			realizarEscritaTextoDebugNFE_LOG(" Acessando metodo realizarEnvioEmailNotaFiscal para a Nota Fiscal Saida codigo : " +notaSaidaVO.getCodigo()  +"Situacao :" +notaSaidaVO.getSituacao() );
			try {
				PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_EMAIL_ENVIO_NFSE, false, notaSaidaVO.getUnidadeEnsinoVO().getCodigo(), usuarioLogado);
				if (mensagemTemplate != null) {
					if (notaSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getTipoIntegracaoNfeEnum().equals(TipoIntegracaoNfeEnum.NFE)) {
						gerarDanfeParaEnvioEmailCliente(notaSaidaVO, Uteis.getMontarCodigoBarra(notaSaidaVO.getNumeroNota(), 9), usuarioLogado, configuracaoRepositorioArquivo);
						List<File> anexos = new ArrayList<File>(0);
						anexos.add(new File(configuracaoRepositorioArquivo.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.NFE.getValue() + File.separator + PastaBaseArquivoEnum.NOTAS_ENVIADAS.getValue() + File.separator + notaSaidaVO.getUnidadeEnsinoVO().getCodigo() + File.separator + Uteis.getMontarCodigoBarra(notaSaidaVO.getNumeroNota(), 9) + ".xml"));
						anexos.add(new File(configuracaoRepositorioArquivo.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.NFE.getValue() + File.separator + PastaBaseArquivoEnum.NOTAS_ENVIADAS.getValue() + File.separator + notaSaidaVO.getUnidadeEnsinoVO().getCodigo() + File.separator + Uteis.getMontarCodigoBarra(notaSaidaVO.getNumeroNota(), 9) + ".pdf"));
						gerarArquivosEnvio(anexos, notaSaidaVO.getSituacao(), configuracaoRepositorioArquivo);
						if (!notaSaidaVO.getPessoaVO().getEmail().trim().isEmpty()) {
							ComunicacaoInternaVO comunicacaoInternaVO = new ComunicacaoInternaVO();
	
							comunicacaoInternaVO.setTipoComunicadoInterno(TipoComunicadoInterno.SOMENTE_LEITURA.getValor());
							comunicacaoInternaVO.setPrioridade(PrioridadeComunicadoInterno.NORMAL.getValor());
							comunicacaoInternaVO.setTipoMarketing(Boolean.FALSE);
							comunicacaoInternaVO.setTipoLeituraObrigatoria(Boolean.FALSE);
							comunicacaoInternaVO.setDigitarMensagem(Boolean.TRUE);
							comunicacaoInternaVO.setResponsavel(configuracaoEmail.getResponsavelPadraoComunicadoInterno());
							comunicacaoInternaVO.setTipoDestinatario(notaSaidaVO.getTipoPessoa());
							comunicacaoInternaVO.setTipoRemetente("FU");
							comunicacaoInternaVO.setAluno(notaSaidaVO.getPessoaVO());
							comunicacaoInternaVO.setUnidadeEnsino(notaSaidaVO.getUnidadeEnsinoVO());
	
							ComunicadoInternoDestinatarioVO destinatario = new ComunicadoInternoDestinatarioVO();
							destinatario.setCiJaLida(Boolean.FALSE);
							destinatario.setDestinatario(notaSaidaVO.getPessoaVO());
							destinatario.setEmail(notaSaidaVO.getPessoaVO().getEmail());
							destinatario.setNome(notaSaidaVO.getPessoaVO().getNome());
							destinatario.setTipoComunicadoInterno(TipoComunicadoInterno.SOMENTE_LEITURA.getValor());
							comunicacaoInternaVO.getComunicadoInternoDestinatarioVOs().add(destinatario);
							
							comunicacaoInternaVO.setEnviarEmail(Boolean.TRUE);
							comunicacaoInternaVO.setAssunto(mensagemTemplate.getAssunto());
							comunicacaoInternaVO.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
							comunicacaoInternaVO.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
							comunicacaoInternaVO.setMensagem(realizarSubstituicaoTagsMensagemNotaFiscal(notaSaidaVO, destinatario, mensagemTemplate.getMensagem()));
	
							ArquivoVO arquivoVO = new ArquivoVO();
							arquivoVO.setCodOrigem(notaSaidaVO.getCodigo());
							arquivoVO.setOrigem("NFE");
							arquivoVO.setApresentarPortalAluno(true);
							arquivoVO.setArquivoEstaNoDiretorioFixo(true);
							arquivoVO.setArquivoExisteHD(true);
							arquivoVO.setDescricao("(XML) Nota Fiscal Nº " + notaSaidaVO.getNumero());
							arquivoVO.setExtensao("xml");
							arquivoVO.setSituacao("AT");
							arquivoVO.setPastaBaseArquivo(configuracaoRepositorioArquivo.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.NFE.getValue() + File.separator + PastaBaseArquivoEnum.NOTAS_ENVIADAS.getValue() + File.separator + notaSaidaVO.getUnidadeEnsinoVO().getCodigo());
							arquivoVO.setNome(Uteis.getMontarCodigoBarra(notaSaidaVO.getNumeroNota(), 9) + ".xml");
							arquivoVO.setPastaBaseArquivoEnum(PastaBaseArquivoEnum.NFE);
							comunicacaoInternaVO.getListaArquivosAnexo().add(arquivoVO);
	
							arquivoVO = new ArquivoVO();
							arquivoVO.setCodOrigem(notaSaidaVO.getCodigo());
							arquivoVO.setOrigem("NFE");
							arquivoVO.setApresentarPortalAluno(true);
							arquivoVO.setArquivoEstaNoDiretorioFixo(true);
							arquivoVO.setArquivoExisteHD(true);
							arquivoVO.setDescricao("(DANFE) Nota Fiscal Nº " + notaSaidaVO.getNumero());
							arquivoVO.setExtensao("pdf");
							arquivoVO.setSituacao("AT");
							arquivoVO.setPastaBaseArquivo(configuracaoRepositorioArquivo.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.NFE.getValue() + File.separator + PastaBaseArquivoEnum.NOTAS_ENVIADAS.getValue() + File.separator + notaSaidaVO.getUnidadeEnsinoVO().getCodigo());
							arquivoVO.setNome(Uteis.getMontarCodigoBarra(notaSaidaVO.getNumeroNota(), 9) + ".pdf");
							arquivoVO.setPastaBaseArquivoEnum(PastaBaseArquivoEnum.NFE);
							comunicacaoInternaVO.getListaArquivosAnexo().add(arquivoVO);
							getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoInternaVO, false, usuarioLogado, configuracaoEmail,null);
						}
					} else {
						if (!mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
							ComunicacaoInternaVO comunicacaoInternaVO = montarDadosEnvioEmailNFSE(notaSaidaVO, configuracaoEmail, mensagemTemplate, usuarioLogado);
							getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoInternaVO, false, usuarioLogado, configuracaoEmail,null);
						}
					}
				}
			} catch (Exception e) {
				notaSaidaVO.setMensagemRetorno(e.getMessage());
				alterarMensagemRetorno(notaSaidaVO, usuarioLogado);
				throw e;
			}
		}
	}

	/**
	 * Metodo para gerar os arquivos das notas fiscais de saida para envio,
	 * organizando os arquivos em diretorios por data de criação da Nt Fiscal de
	 * saida
	 * 
	 * @author Vinicius Nunes
	 * @param files
	 *            lista de arquivos a serem organizados no diretorio
	 * @param situacao
	 *            situação da nota fiscal representada pelos arquivos
	 * 
	 */
	private void gerarArquivosEnvio(List<File> files, String situacao, ConfiguracaoGeralSistemaVO conSistemaVO) throws Exception {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		String path = conSistemaVO.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.NFE.getValue() + File.separator + PastaBaseArquivoEnum.BACKUP_NFE.getValue() + File.separator + dateFormat.format(new Date());

		// CA - nata cancelamento
		// CC - carta correcao
		for (File file : files) {
			String nomeArquivo = file.getName();
			if (nomeArquivo.substring(nomeArquivo.lastIndexOf(".") + 1, nomeArquivo.length()).equals("xml")) {
				if (situacao.equals("CA")) {
					nomeArquivo = nomeArquivo.substring(0, nomeArquivo.lastIndexOf(".")) + "_nfe_Cancelamento.xml";
				} else if (situacao.equals("CC")) {
					nomeArquivo = nomeArquivo.substring(0, nomeArquivo.lastIndexOf(".")) + "_nfe_CartaCorrecao.xml";
				} else {
					nomeArquivo = nomeArquivo.substring(0, nomeArquivo.lastIndexOf(".")) + "_nfe_Envio.xml";
				}
			}
			Uteis.realizarCopiaArquivo(file.getAbsolutePath(), path + File.separator + nomeArquivo, true);
		}
	}

	/**
	 * Metedo para a obtenção dos arquivos de notas fiscais de um determinado dia.
	 * 
	 * @author Vinicius Nunes
	 * @param date
	 *            data base para consultar os arquivos de um determinado dia.
	 * @return lista de arquivos (PDF,xml) correspodente as notas fiscais de saida
	 *         do dia
	 */
	@Override
	public List<File> getArquivosEnvioPorData(Date date) throws Exception {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		// String path = "D:\\backupNfe" + File.separator +
		// dateFormat.format(new Date());
		String path = File.separator + "backupNfe" + File.separator + dateFormat.format(date);
		File dir = new File(path);
		if (!dir.exists()) {
			return new ArrayList<File>(0);
		}
		return new ArrayList<File>(Arrays.asList(dir.listFiles()));
	}

	/**
	 * Metodo para a criação de um arquivo.
	 * 
	 * @param xml
	 *            string contendo o XML de referencia.
	 * @return Arquivo criado com o conteudo passado.
	 * @throws IOException
	 */
	private File gerarArquivoXml(String xml, String name) throws IOException {
		String tempDir = System.getProperty("java.io.tmpdir");
		File fl = new File(tempDir + File.separator + name + ".xml");
		OutputStream os = new FileOutputStream(fl);
		os.write(xml.getBytes());
		os.flush();
		os.close();
		return fl;
	}

	public void gerarPdfParaEnvioEmailCliente(NotaFiscalSaidaVO notaFiscalSaida, String carta, UsuarioVO usuarioLogado) throws Exception {
		try {
			HashMap parameters = new HashMap();
			List lista = new ArrayList(0);
			lista.add(notaFiscalSaida);
			JRDataSource jr = new JRBeanArrayDataSource(lista.toArray());
			String nomeJasperReportDesignIReport = "relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + "CartaCorrecaoNFRel.jasper";
			File arquivoIReport = new File(UteisJSF.getCaminhoBase() + File.separator + nomeJasperReportDesignIReport);
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(arquivoIReport);
			JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, jr);
			String nomeRelPDF = "relatorio" + File.separator + carta + "-cartaCorrecao.pdf";
			File pdfFile = new File(UteisJSF.getCaminhoWeb() + File.separator + nomeRelPDF);
			if (pdfFile.exists()) {
				try {
					pdfFile.delete();
				} catch (Exception e) {
					nomeRelPDF = "relatorios" + File.separator + carta + "-cartaCorrecao.pdf";
					pdfFile = new File(UteisJSF.getCaminhoWeb() + File.separator + nomeRelPDF);
				}
			}
			JRPdfExporter jrpdfexporter = new JRPdfExporter();
			jrpdfexporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
			jrpdfexporter.setParameter(JRExporterParameter.OUTPUT_FILE, pdfFile);
			jrpdfexporter.exportReport();
		} catch (Exception e) {
			// //System.out.println("CartaCorrecao" + e.getMessage());
			throw e;
		}
	}

	public void gerarDanfeParaEnvioEmailCliente(NotaFiscalSaidaVO notaFiscalSaida, String danfe, UsuarioVO usuarioLogado, ConfiguracaoGeralSistemaVO configuracao) throws Exception {
		try {
			SuperParametroRelVO superParametroRelVO = new SuperParametroRelVO();
			String designIReportRelatorio = "";
			/*
			 * HashMap parameters = new HashMap(); parameters.put("usuario",
			 * usuarioLogado.getNome()); parameters.put("codigoUsuario",
			 * Integer.parseInt(usuarioLogado.getCodigo().toString()));
			 * parameters.put("nomeEmpresa",
			 * notaFiscalSaida.getUnidadeEnsinoVO().getNome());
			 * parameters.put("SUBREPORT_DIR", UteisJSF.getCaminhoClassesAplicacao() +
			 * File.separator + "relatorio" + File.separator + "designRelatorio" +
			 * File.separator + "financeiro" + File.separator);
			 */
			superParametroRelVO.adicionarParametro("usuario", usuarioLogado.getNome());
			superParametroRelVO.adicionarParametro("codigoUsuario", Integer.parseInt(usuarioLogado.getCodigo().toString()));
			superParametroRelVO.adicionarParametro("nomeEmpresa", notaFiscalSaida.getUnidadeEnsinoVO().getNome());
			superParametroRelVO.adicionarParametro("SUBREPORT_DIR", UteisJSF.getCaminhoClassesAplicacao() + File.separator + "relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator);

			if (notaFiscalSaida.getUnidadeEnsinoVO().getRazaoSocial().equals("") && notaFiscalSaida.getUnidadeEnsinoVO().getCodigo().intValue() != 0) {
				notaFiscalSaida.setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(notaFiscalSaida.getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioLogado));
			}
			List lista = new ArrayList(0);
			lista.add(notaFiscalSaida);
			JRDataSource jr = new JRBeanArrayDataSource(lista.toArray());

			if (notaFiscalSaida.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getTipoIntegracaoNfeEnum().equals(TipoIntegracaoNfeEnum.NFE)) {
				designIReportRelatorio = "relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + "NFRel.jrxml";
			} else if (notaFiscalSaida.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getTipoIntegracaoNfeEnum().equals(TipoIntegracaoNfeEnum.NFSE)) {

				designIReportRelatorio = "relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + notaFiscalSaida.getWebServicesNFSEEnum().getLayoutRelatorioString() + ".jrxml";

			}

			String nomeJasperReportDesignIReport = designIReportRelatorio.substring(0, designIReportRelatorio.lastIndexOf(".")) + ".jasper";

			File arquivoIReport = new File(UteisJSF.getCaminhoBase() + File.separator + nomeJasperReportDesignIReport);

			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(arquivoIReport);
			JasperPrint print = JasperFillManager.fillReport(jasperReport, superParametroRelVO.getParametros(), jr);

			File pdfFile = new File(configuracao.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.NFE.getValue() + File.separator + PastaBaseArquivoEnum.NOTAS_ENVIADAS.getValue() + File.separator + notaFiscalSaida.getUnidadeEnsinoVO().getCodigo());
			if (!pdfFile.exists()) {
				pdfFile.mkdirs();
			}
			pdfFile = new File(configuracao.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.NFE.getValue() + File.separator + PastaBaseArquivoEnum.NOTAS_ENVIADAS.getValue() + File.separator + notaFiscalSaida.getUnidadeEnsinoVO().getCodigo() + File.separator + danfe + ".pdf");
			if (pdfFile.exists()) {
				return;
			}
			JRPdfExporter jrpdfexporter = new JRPdfExporter();
			jrpdfexporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
			jrpdfexporter.setParameter(JRExporterParameter.OUTPUT_FILE, pdfFile);
			jrpdfexporter.exportReport();
		} catch (Exception e) {
			// //System.out.println("DANFE" + e.getMessage());
			throw e;
		}
	}

	@Override
	public String consultarXmlEnvioNotaFiscal(Integer numero) throws Exception {
		try {
			StringBuilder sqlStr = new StringBuilder();
			sqlStr.append("select xmlenvio from notafiscalsaida where codigo = ").append(numero);

			SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			if (dadosSQL.next()) {
				return dadosSQL.getString("xmlenvio");
			}
			return "";
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public String consultarXmlProcNFENotaFiscal(Integer codigo) throws Exception {
		try {
			StringBuilder sqlStr = new StringBuilder();
			sqlStr.append("select xmlnfeproc from notafiscalsaida where codigo = ").append(codigo);

			SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			if (dadosSQL.next()) {
				return dadosSQL.getString("xmlnfeproc");
			}
			return "";
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public String consultarXmlCancelamentoNotaFiscal(Integer numero) throws Exception {
		try {
			StringBuilder sqlStr = new StringBuilder();
			sqlStr.append("select xmlcancelamento from notafiscalsaida where codigo = ").append(numero);

			SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			if (dadosSQL.next()) {
				return dadosSQL.getString("xmlcancelamento");
			}
			return "";
		} catch (Exception e) {
			throw e;
		}
	}

	public File[] executarCompactarNotasEnviadas(final List<NotaFiscalSaidaVO> notaFiscalSaidaVOs, final ConfiguracaoGeralSistemaVO conSistemaVO, final UsuarioVO usuarioVO, ProgressBarVO ProgressBarNotasCompactadas) throws Exception {
		try {

			final List<File> listaArquivos = new ArrayList<File>(0);
			final List<File> listaArquivosXml = new ArrayList<File>(0);
			// As listas são inicializadas com o valor nulo por conta da concorrência do
			// paralelismo. Depois cada índice é substituído pelo arquivo usando o método
			// set.
			for (int i = 0; i < notaFiscalSaidaVOs.size(); i++) {
				listaArquivos.add(null);
			}
			for (int i = 0; i < notaFiscalSaidaVOs.size(); i++) {
				listaArquivosXml.add(null);
			}

			final ConsistirException ex = new ConsistirException();
			ProcessarParalelismo.Processo processo = new ProcessarParalelismo.Processo() {
				@Override
				public void run(int i) {
					NotaFiscalSaidaVO nota = notaFiscalSaidaVOs.get(i);
					try {
						if (nota.getSituacao().equals("AU") || (nota.getSituacao().equals(SituacaoNotaFiscalSaidaEnum.CANCELADA.getValor()) && nota.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getTipoIntegracaoNfeEnum().equals(TipoIntegracaoNfeEnum.NFE))) {
							nota = getFacadeFactory().getNotaFiscalSaidaFacade().consultarPorChavePrimaria(nota.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
							PastaBaseArquivoEnum pastaBase = PastaBaseArquivoEnum.NOTAS_ENVIADAS;
							nota.setWebServicesNFSEEnum(WebServicesNFSEEnum.getEnumPorIdCidadeTipoAcaoServico(nota.getUnidadeEnsinoVO().getCidade().getCodigo(), TipoAcaoServicoNFSEEnum.GerarNfse));
							CidadeVO cidade = nota.getUnidadeEnsinoVO().getCidadeNFSe();
							if (cidade.isWebserviceNFSeImplementado()) {
								nota.setWebServicesNFSEEnum(WebServicesNFSEEnum.NFSE_WEBSERVICE);
							}
							String nomeArquivo = Uteis.getMontarCodigoBarra(nota.getNumeroNota(), 9) + ".xml";
							if (nota.getSituacao().equals(SituacaoNotaFiscalSaidaEnum.CANCELADA.getValor())) {
								pastaBase = PastaBaseArquivoEnum.NOTAS_CANCELADAS;
								nomeArquivo = Uteis.getMontarCodigoBarra(nota.getNumeroNota(), 9) + "Cancelamento.xml";
							}
							File arquivoXml = new File(conSistemaVO.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.NFE.getValue() + File.separator + pastaBase.getValue() + File.separator + nota.getUnidadeEnsinoVO().getCodigo());
							if (!arquivoXml.exists()) {
								arquivoXml.mkdirs();
							}
							arquivoXml = new File(conSistemaVO.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.NFE.getValue() + File.separator + pastaBase.getValue() + File.separator + nota.getUnidadeEnsinoVO().getCodigo() + File.separator + nomeArquivo);
							if (!arquivoXml.exists()) {
								if (nota.getSituacao().equals(SituacaoNotaFiscalSaidaEnum.AUTORIZADA.getValor())) {
									PrintWriter xml = getFacadeFactory().getArquivoHelper().criarArquivoTexto(arquivoXml.getParent(), arquivoXml.getName(), false);
									nota.setXmlNfeProc(consultarXmlProcNFENotaFiscal(nota.getCodigo()));
									if (nota.getXmlNfeProc() != null && !nota.getXmlNfeProc().trim().equals("")) {
										xml.append(nota.getXmlNfeProc());
									} else {
										nota.setXmlEnvio(consultarXmlEnvioNotaFiscal(nota.getCodigo()));
										xml.append(nota.getXmlEnvio());
									}
									xml.flush();
									xml.close();
									listaArquivosXml.set(i, arquivoXml);
								} else if (nota.getSituacao().equals(SituacaoNotaFiscalSaidaEnum.CANCELADA.getValor())) {
									nota.setXmlCancelamento(getFacadeFactory().getNotaFiscalSaidaFacade().consultarXmlCancelamentoNotaFiscal(nota.getCodigo()));
									PrintWriter xml = getFacadeFactory().getArquivoHelper().criarArquivoTexto(arquivoXml.getParent(), arquivoXml.getName(), false);
									xml.append(nota.getXmlCancelamento());
									xml.flush();
									xml.close();
									listaArquivosXml.set(i, arquivoXml);
								}
							} else {
								listaArquivosXml.set(i, arquivoXml);
							}

							if (nota.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getTipoIntegracaoNfeEnum().equals(TipoIntegracaoNfeEnum.NFE) && nota.getSituacao().equals(SituacaoNotaFiscalSaidaEnum.AUTORIZADA.getValor())) {
								File arquivoPdf = new File(conSistemaVO.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.NFE.getValue() + File.separator + PastaBaseArquivoEnum.NOTAS_ENVIADAS.getValue() + File.separator + nota.getUnidadeEnsinoVO().getCodigo());
								if (!arquivoPdf.exists()) {
									arquivoPdf.mkdirs();
								}
								arquivoPdf = new File(conSistemaVO.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.NFE.getValue() + File.separator + PastaBaseArquivoEnum.NOTAS_ENVIADAS.getValue() + File.separator + nota.getUnidadeEnsinoVO().getCodigo() + File.separator + Uteis.getMontarCodigoBarra(nota.getNumeroNota(), 9) + ".pdf");
								if (!arquivoPdf.exists()) {
									gerarDanfeParaEnvioEmailCliente(nota, Uteis.getMontarCodigoBarra(nota.getNumeroNota(), 9), usuarioVO, conSistemaVO);
									listaArquivos.set(i, arquivoPdf);
								} else {
									listaArquivos.set(i, arquivoPdf);
								}
							} else if (nota.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getTipoIntegracaoNfeEnum().equals(TipoIntegracaoNfeEnum.NFSE) && nota.getSituacao().equals(SituacaoNotaFiscalSaidaEnum.AUTORIZADA.getValor())) {
								if (!nota.getWebServicesNFSEEnum().getLayoutRelatorioString().equals("")) {
									ConfiguracaoNotaFiscalVO configuracaoNotaFiscalVO = getFacadeFactory().getConfiguracaoNotaFiscalFacade().consultarPorUnidadeEnsino(nota.getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
									calcularImpostos(nota, configuracaoNotaFiscalVO);
									File arquivoPdf = new File(conSistemaVO.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.NFE.getValue() + File.separator + PastaBaseArquivoEnum.NOTAS_ENVIADAS.getValue() + File.separator + nota.getUnidadeEnsinoVO().getCodigo());
									if (!arquivoPdf.exists()) {
										arquivoPdf.mkdirs();
									}
									arquivoPdf = new File(conSistemaVO.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.NFE.getValue() + File.separator + PastaBaseArquivoEnum.NOTAS_ENVIADAS.getValue() + File.separator + nota.getUnidadeEnsinoVO().getCodigo() + File.separator + Uteis.getMontarCodigoBarra(nota.getNumeroNota(), 9) + ".pdf");
									if (!arquivoPdf.exists()) {
										gerarDanfeParaEnvioEmailCliente(nota, Uteis.getMontarCodigoBarra(nota.getNumeroNota(), 9), usuarioVO, conSistemaVO);
										listaArquivos.set(i, arquivoPdf);
									} else {
										listaArquivos.set(i, arquivoPdf);
									}
								}
							}
						}
					} catch (ConsistirException e) {
						ex.adicionarListaMensagemErro("Erro ao processar NFE: " + nota.getNumeroNota() + " - " + e.getMessage() + ".");
					} catch (Exception e) {
						ex.adicionarListaMensagemErro("Erro ao processar NFE: " + nota.getNumeroNota() + " - " + e.getMessage() + ".");
					} finally {
//						ProgressBarNotasCompactadas.incrementar();
						ProgressBarNotasCompactadas.setStatus("Compactando nota de número: " + nota.getNumeroNota());
						nota = null;
					}
				}
			};
			ProcessarParalelismo.executar(0, notaFiscalSaidaVOs.size(), ex, processo);
			processo = null;
			if (!ex.getListaMensagemErro().isEmpty()) {
				throw ex;
			}
			listaArquivos.addAll(listaArquivosXml);
			return listaArquivos.toArray(new File[listaArquivos.size()]);
		} catch (ConsistirException ex) {
			throw ex;
		} catch (Exception e) {
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.financeiro.teste#consultarNota(negocio.comuns.financeiro
	 * .NotaFiscalSaidaVO, java.lang.String, negocio.nfe.utilitarias.AmbienteNfe,
	 * negocio.nfe.utilitarias.TipoCertificado,
	 * negocio.comuns.basico.ConfiguracaoGeralSistemaVO, java.lang.String,
	 * negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public String consultarNota(NotaFiscalSaidaVO notaFiscalSaidaVO, ConfiguracaoGeralSistemaVO conGeralSistemaVO, UsuarioVO usuarioLogado) throws Exception {
		try {
			if (!notaFiscalSaidaVO.getIdentificadorReceita().isEmpty()) {
				UteisNfe.validarCaminhoCertificado(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO(), conGeralSistemaVO);
				String xmlRetorno = ConsultarNota.ConsularNota(notaFiscalSaidaVO.getUnidadeEnsinoVO().getCidade().getEstado().getCodigoIBGE(), notaFiscalSaidaVO.getIdentificadorReceita().substring(3), conGeralSistemaVO, notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO(), usuarioLogado);
				String status = UteisNfe.lerXMLStatRecebimentoLote(xmlRetorno);
				gravarSituacaoNotaComBaseNoStatus(notaFiscalSaidaVO, status, xmlRetorno, usuarioLogado);
				return CodigoRetorno.MensagemRetorno(UteisNfe.obterCodigoRetorno(xmlRetorno));
			}
			return "";
		} catch (Exception e) {
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.financeiro.teste#gravarSituacaoNotaComBaseNoStatus
	 * (negocio.comuns.financeiro.NotaFiscalSaidaVO, java.lang.String,
	 * java.lang.String, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void gravarSituacaoNotaComBaseNoStatus(NotaFiscalSaidaVO notaFiscalSaidaVO, String status, String xmlRetorno, UsuarioVO usuarioLogado) throws Exception {
		if (status.equals("102")) {
			notaFiscalSaidaVO.setSituacao(SituacaoNotaFiscalSaidaEnum.INUTILIZADA.getValor());
			notaFiscalSaidaVO.setProtocoloInutilizacao(UteisNfe.getProtocoloEnvioNFe(xmlRetorno));
			notaFiscalSaidaVO.setDataStuacao(UteisNfe.getDataProtocoloEnvioNFe(xmlRetorno));
			gravarSituacaoInutilizado(notaFiscalSaidaVO.getCodigo(), SituacaoNotaFiscalSaidaEnum.INUTILIZADA.getValor(), notaFiscalSaidaVO.getProtocoloInutilizacao(), notaFiscalSaidaVO.getMotivoInutilizacao(), usuarioLogado);
		} else if (status.equals("101")) {
			notaFiscalSaidaVO.setSituacao(SituacaoNotaFiscalSaidaEnum.CANCELADA.getValor());
			notaFiscalSaidaVO.setProtocoloCancelamento(UteisNfe.getProtocoloEnvioNFe(xmlRetorno));
			notaFiscalSaidaVO.setDataStuacao(UteisNfe.getDataProtocoloEnvioNFe(xmlRetorno));
			gravarSituacaoCancelamento(notaFiscalSaidaVO.getCodigo(), SituacaoNotaFiscalSaidaEnum.CANCELADA.getValor(), notaFiscalSaidaVO.getProtocoloCancelamento(), notaFiscalSaidaVO.getXmlCancelamento(), notaFiscalSaidaVO.getMotivoCancelamento(), usuarioLogado);
		} else if (status.equals("100")) {
			notaFiscalSaidaVO.setSituacao(SituacaoNotaFiscalSaidaEnum.AUTORIZADA.getValor());
			notaFiscalSaidaVO.setProtocolo(UteisNfe.getProtocoloEnvioNFe(xmlRetorno));
			notaFiscalSaidaVO.setDataStuacao(UteisNfe.getDataProtocoloEnvioNFe(xmlRetorno));
			gravarSituacaoEnvio(notaFiscalSaidaVO, SituacaoNotaFiscalSaidaEnum.AUTORIZADA.getValor(), notaFiscalSaidaVO.getProtocolo(), notaFiscalSaidaVO.getDataStuacao(), notaFiscalSaidaVO.getIdentificadorReceita(), notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO(), usuarioLogado);
		} else if (Integer.parseInt(status) >= 201) {
			notaFiscalSaidaVO.setSituacao(SituacaoNotaFiscalSaidaEnum.REJEITADA.getValor());
			gravarSituacaoEnvio(notaFiscalSaidaVO, SituacaoNotaFiscalSaidaEnum.REJEITADA.getValor(), notaFiscalSaidaVO.getProtocolo(), notaFiscalSaidaVO.getDataStuacao(), notaFiscalSaidaVO.getIdentificadorReceita(), notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO(), usuarioLogado);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.financeiro.teste#consultarLoteEnviar(negocio.comuns
	 * .financeiro.NotaFiscalSaidaVO, java.lang.String,
	 * negocio.nfe.utilitarias.AmbienteNfe, negocio.nfe.utilitarias.TipoCertificado,
	 * negocio.comuns.basico.ConfiguracaoGeralSistemaVO, java.lang.String,
	 * negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public String consultarLoteEnviar(NotaFiscalSaidaVO notaFiscalSaidaVO, ConfiguracaoGeralSistemaVO conGeralSistemaVO, UsuarioVO usuarioLogado) throws Exception {
		try {
			String retorno = "";
			if (notaFiscalSaidaVO.getRecibo().equals("")) {
				notaFiscalSaidaVO.setSituacao(SituacaoNotaFiscalSaidaEnum.REJEITADA.getValor());
				return notaFiscalSaidaVO.getMensagemRetorno();
			} else {
				retorno = ConsultarLote.ConsularLote(notaFiscalSaidaVO.getUnidadeEnsinoVO().getCidade().getEstado().getCodigoIBGE(), notaFiscalSaidaVO.getRecibo(), conGeralSistemaVO, notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO(), usuarioLogado);
				notaFiscalSaidaVO.setRetornoCompleto(retorno);
				String cStat = UteisNfe.lerXMLStatRecebimentoLote(retorno);
				gravarSituacaoNotaComBaseNoStatus(notaFiscalSaidaVO, cStat, retorno, usuarioLogado);
				return UteisNfe.getMotivoXMLretornoLoteNFe(retorno);
			}
		} catch (Exception e) {
			notaFiscalSaidaVO.setSituacao(SituacaoNotaFiscalSaidaEnum.REJEITADA.getValor());
			try {
				consultarNota(notaFiscalSaidaVO, conGeralSistemaVO, usuarioLogado);
			} catch (Exception i) {
				throw e;
			}
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.financeiro.teste#inutilizarNota(negocio.comuns.financeiro
	 * .NotaFiscalSaidaVO, java.lang.String, negocio.nfe.utilitarias.AmbienteNfe,
	 * negocio.nfe.utilitarias.TipoCertificado,
	 * negocio.comuns.basico.ConfiguracaoGeralSistemaVO, java.lang.String,
	 * negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public String inutilizarNota(NotaFiscalSaidaVO notaFiscalSaidaVO, String caminhoNFe, AmbienteNfeEnum ambienteNfeEnum, TipoCertificadoEnum tipoCertificadoEnum, String caminhoCertificado, UsuarioVO usuarioLogado) throws Exception {
		try {
			executarValidacaoPossibilidadeInutilizarNota(notaFiscalSaidaVO);
			executarValidacaoTipoCertificado(tipoCertificadoEnum);
			UteisNfe.validarCaminhoCertificado(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO(), getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesLocalUploadArquivoFixo());
			String xmlRetorno = "";
			String protocolo = "";
			String modelo = "";
			if (notaFiscalSaidaVO.getModelo().equals("1")) {
				modelo = "55";
			}
			if (notaFiscalSaidaVO.getNumero() != 0) {
				xmlRetorno = Inutilizar.A1(notaFiscalSaidaVO.getUnidadeEnsinoVO().getCidade().getEstado().getCodigoIBGE(), UteisJSF.getCaminhoWebNFe() + File.separator + Uteis.getMontarCodigoBarra(notaFiscalSaidaVO.getNumeroNota(), 9) + "Inutilizacao.xml", notaFiscalSaidaVO.getMotivoInutilizacao(), Uteis.removerMascara(notaFiscalSaidaVO.getUnidadeEnsinoVO().getCNPJ()), modelo, notaFiscalSaidaVO.getSerie(), notaFiscalSaidaVO.getNumero().toString(), notaFiscalSaidaVO.getNumero().toString(), getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesLocalUploadArquivoFixo(),
				        notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO(), usuarioLogado);
			}
			String codigoRetorno = UteisNfe.obterCodigoRetorno(xmlRetorno);
			if ((codigoRetorno.equals("102") && notaFiscalSaidaVO.getNumero() != 0) || notaFiscalSaidaVO.getNumero() == 0) {
				if (!xmlRetorno.equals("")) {
					protocolo = UteisNfe.obterProtocoloInutilizacao(xmlRetorno);
				}
				notaFiscalSaidaVO.setSituacao("IN");
				gravarSituacaoInutilizado(notaFiscalSaidaVO.getCodigo(), "IN", protocolo, notaFiscalSaidaVO.getMotivoInutilizacao(), usuarioLogado);
			}
			return CodigoRetorno.MensagemRetorno(codigoRetorno);
		} catch (Exception e) {
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.financeiro.teste#consularStatusServicoNFe(negocio
	 * .comuns.financeiro.NotaFiscalSaidaVO)
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public String consultarStatusServicoNFe(UnidadeEnsinoVO unidadeEnsinoVO, UsuarioVO usuarioLogado) throws Exception {
		try {
			ConfiguracaoGeralSistemaVO conGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesLocalUploadArquivoFixo();
			UteisNfe.validarCaminhoCertificado(unidadeEnsinoVO.getConfiguracaoNotaFiscalVO(), conGeralSistemaVO);
			String xmlRetorno = StatusServico.A1(unidadeEnsinoVO.getCidade().getEstado().getCodigoIBGE(), conGeralSistemaVO, unidadeEnsinoVO.getConfiguracaoNotaFiscalVO(), usuarioLogado);
			System.out.println("STATUS-NFE-> XMLRetorno: " + xmlRetorno);
			String resultado = CodigoRetorno.MensagemRetorno(UteisNfe.obterCodigoRetorno(xmlRetorno));
			System.out.println("STATUS-NFE-> Resultado: " + resultado);
			return resultado;
		} catch (Exception e) {
			throw e;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.financeiro.teste#executarValidacaoTipoCertificado
	 * (negocio.nfe.utilitarias.TipoCertificado)
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void executarValidacaoTipoCertificado(TipoCertificadoEnum tipoCertificadoEnum) throws Exception {
		if (tipoCertificadoEnum.equals(TipoCertificadoEnum.A3)) {
			throw new Exception("Tipo de certificado Não Suportado");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.financeiro.teste#
	 * executarValidacaoPossibilidadeInutilizarNota
	 * (negocio.comuns.financeiro.NotaFiscalSaidaVO)
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void executarValidacaoPossibilidadeInutilizarNota(NotaFiscalSaidaVO notaFiscalSaidaVO) throws Exception {
		if (notaFiscalSaidaVO.getSituacao().equals(SituacaoNotaFiscalSaidaEnum.AUTORIZADA.getValor()) || notaFiscalSaidaVO.getSituacao().equals(SituacaoNotaFiscalSaidaEnum.CANCELADA.getValor()) || notaFiscalSaidaVO.getSituacao().equals(SituacaoNotaFiscalSaidaEnum.INUTILIZADA.getValor())) {
			throw new Exception("Esta nota não pode ser Inutilizada.");
		}
		if (notaFiscalSaidaVO.getSituacao().equals(SituacaoNotaFiscalSaidaEnum.ENVIADA.getValor())) {
			throw new Exception("Consulte o resultado do envio antes de Inutilizar");
		}
		if (notaFiscalSaidaVO.getMotivoInutilizacao().length() < 15) {
			throw new Exception("Informe o motivo da Inutilização com no mínimo 15 caracteres.");
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void executarValidacaoPossibilidadeCorrigirNota(NotaFiscalSaidaVO notaFiscalSaidaVO) throws Exception {
		if (!notaFiscalSaidaVO.getSituacao().equals(SituacaoNotaFiscalSaidaEnum.AUTORIZADA.getValor())) {
			throw new Exception("A Carta de Correção só pode ser utilizada em notas fiscais Autorizadas.");
		}
		if (notaFiscalSaidaVO.getSeqEventoCartaCorrecao().intValue() > 20) {
			throw new Exception("O Limite de 20 correções já foi atingido.");
		}
		if (notaFiscalSaidaVO.getDescricaoCartaCorrecao().length() < 15) {
			throw new Exception("Informe o motivo da Correção com no mínimo 15 caracteres.");
		}
		/*
		 * Foi informado pela Sefaz que ainda não estão validando se a nota foi emitida
		 * a mais de 30 dias
		 */
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.financeiro.teste#excluir(negocio.comuns.financeiro
	 * .NotaFiscalSaidaVO, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void excluir(NotaFiscalSaidaVO obj, UsuarioVO usuarioLogado) throws Exception {
		try {

			if (obj.getSituacao().equals(SituacaoNotaFiscalSaidaEnum.REJEITADA.getValor()) || obj.getSituacao().equals(SituacaoNotaFiscalSaidaEnum.AGUARDANDO_ENVIO.getValor())) {
				excluir(getIdEntidade(), usuarioLogado);
				excluirSemCommit(obj, usuarioLogado);
			}

		} catch (Exception e) {

			throw e;
		} finally {
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.financeiro.teste#excluirSemCommit(negocio.comuns.
	 * financeiro.NotaFiscalSaidaVO, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void excluirSemCommit(NotaFiscalSaidaVO obj, UsuarioVO usuarioLogado) throws Exception {
		String sql = "DELETE FROM NotaFiscalSaida WHERE ((codigo = ?))";
		getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.financeiro.teste#consultarNumeroNotaFiscal(java.lang
	 * .Integer)
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public Long consultarNumeroNotaFiscal(Integer codigo) throws Exception {
		String sqlStr = "SELECT numero FROM NotaFiscalSaida WHERE codigo = " + codigo.intValue();

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (tabelaResultado.next()) {
			return tabelaResultado.getLong("numero");
		}
		return 0l;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.financeiro.teste#consultarXMLEnvioPorData(java.util
	 * .Date)
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<String> consultarXMLEnvioPorData(Date data) throws Exception {
		List<String> xmlx = new ArrayList<String>();
		String sqlStr = "select xmlEnvio from notaFiscalSaida where situacao='AU' and ((dataSituacao>='" + UteisData.getDataJDBC(data) + " 00:00:00') and (dataSituacao<='" + UteisData.getDataJDBC(data) + " 23:59:59')) and xmlEnvio is not null and xmlEnvio <> '' ";

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		while (tabelaResultado.next()) {
			xmlx.add(tabelaResultado.getString("xmlEnvio"));
		}
		return xmlx;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.financeiro.teste#consultarXMLCancelamentoPorData(
	 * java.util.Date)
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<String> consultarXMLCancelamentoPorData(Date data) throws Exception {
		List<String> xmlx = new ArrayList<String>();
		String sqlStr = "select xmlCancelamento from notaFiscalSaida where situacao='CA' and ((dataSituacao>='" + UteisData.getDataJDBC(data) + " 00:00:00') and (dataSituacao<='" + UteisData.getDataJDBC(data) + " 23:59:59')) and xmlCancelamento is not null and xmlCancelamento <> ''";

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		while (tabelaResultado.next()) {
			xmlx.add(tabelaResultado.getString("xmlCancelamento"));
		}
		return xmlx;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.financeiro.teste#consultarNotaFiscalSaidaEnviadasPorData
	 * (java.util.Date, int, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<NotaFiscalSaidaVO> consultarNotaFiscalSaidaEnviadasPorData(Date data, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		String sqlStr = "select * from notaFiscalSaida where situacao='AU' and ((dataSituacao>='" + UteisData.getDataJDBC(data) + " 00:00:00') and (dataSituacao<='" + UteisData.getDataJDBC(data) + " 23:59:59')) and xmlEnvio is not null and xmlEnvio <> '' ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuarioLogado);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.financeiro.teste#
	 * consultarNotaFiscalSaidaCanceladasPorData(java.util.Date, int,
	 * negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<NotaFiscalSaidaVO> consultarNotaFiscalSaidaCanceladasPorData(Date data, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		String sqlStr = "select * from notaFiscalSaida where situacao='CA' and ((dataSituacao>='" + UteisData.getDataJDBC(data) + " 00:00:00') and (dataSituacao<='" + UteisData.getDataJDBC(data) + " 23:59:59')) and xmlCancelamento is not null and xmlEnvio <> '' ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuarioLogado);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.financeiro.teste#consultarPorNotaOrigem(java.lang
	 * .Integer, int, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List consultarPorNotaOrigem(Integer valorConsulta, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		consultar(getIdEntidade(), true, usuarioLogado);
		String sqlStr = "SELECT * FROM NotaFiscalSaida WHERE notaOrigem = " + valorConsulta.intValue() + " ORDER BY codigo";

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuarioLogado);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.financeiro.teste#consultarPorSituacao(java.lang.String ,
	 * java.util.Date, java.util.Date, java.lang.Integer, int, int, boolean, int,
	 * negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List consultarPorSituacao(String valorConsulta, Date prmIni, Date prmFim, String situacao, int limite, int pagina, boolean controleAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		consultar(getIdEntidade(), controleAcesso, usuarioLogado);
		StringBuilder sqlStr = new StringBuilder("SELECT * FROM NotaFiscalSaida WHERE 1=1");
		if (!valorConsulta.isEmpty()) {
			sqlStr.append(" and upper(situacao) = '").append(valorConsulta.toUpperCase()).append("' ");
		}

		if (prmIni != null && prmFim != null) {
			sqlStr.append(" and ((dataemissao>= '");
			sqlStr.append(UteisData.getDataJDBC(prmIni));
			sqlStr.append("') and (dataemissao <= '");
			sqlStr.append(UteisData.getDataJDBC(prmFim));
			sqlStr.append("')) ");
		} else if (prmIni != null) {
			sqlStr.append(" and ((dataemissao >= '");
			sqlStr.append(UteisData.getDataJDBC(prmIni));
			sqlStr.append("')) ");
		} else if (prmFim != null) {
			sqlStr.append(" and ((dataemissao <= '");
			sqlStr.append(UteisData.getDataJDBC(prmFim));
			sqlStr.append("')) ");
		}

		if (limite != 0) {
			sqlStr.append(" ORDER BY numero  limit ");
			sqlStr.append(limite);
			sqlStr.append(" offset ");
			sqlStr.append(pagina);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuarioLogado);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.financeiro.teste#consultarPorLote(java.lang.String,
	 * int, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List consultarPorLote(String valorConsulta, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		consultar(getIdEntidade(), true, usuarioLogado);
		String sqlStr = "SELECT * FROM NotaFiscalSaida WHERE lote = '" + valorConsulta + "' ORDER BY codigo";

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuarioLogado);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.financeiro.teste#consultarPorNumero(java.lang.Long,
	 * java.util.Date, java.util.Date, java.lang.String, java.lang.Integer, int,
	 * int, boolean, int, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List consultarPorNumero(Long valorConsulta, Date prmIni, Date prmFim, String situacao, UnidadeEnsinoVO unidadeEnsinoVO, CursoVO cursoVO, TurmaVO turmaVO, int limite, int pagina, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuarioLogado);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT notafiscalsaida.codigo, notafiscalsaida.serie, notafiscalsaida.linkacesso, notafiscalsaida.numero, notafiscalsaida.numerorps, notafiscalsaida.recibo, notafiscalsaida.matricula, notafiscalsaida.aluno, aluno.nome as \"aluno.nome\", aluno.codigo as \"aluno.codigo\", valortotal, notafiscalsaida.tipopessoa, dataemissao, ");
		sqlStr.append(" situacao, unidadeensino, mensagemretorno, lote, identificadorreceita, protocolo, responsavelfinanceiro, responsavel.nome as \"responsavel.nome\", responsavel.codigo as \"responsavel.codigo\", observacaocontribuinte, ");
		sqlStr.append(" funcionario.codigo as \"funcionario.codigo\", parceiro.codigo as \"parceiro.codigo\", parceiro.nome as \"parceiro.nome\", ");
		sqlStr.append(" notafiscalsaida.valortotalpis as valortotalpis, notafiscalsaida.valortotalcofins as valortotalcofins, ");
		sqlStr.append(" notafiscalsaida.aliquotaissqn as aliquotaissqn, notafiscalsaida.aliquotapis as aliquotapis, notafiscalsaida.aliquotainss as aliquotainss, notafiscalsaida.aliquotacsll as aliquotacsll, notafiscalsaida.aliquotair as aliquotair, ");
		sqlStr.append(" notafiscalsaida.valortotalinss as valortotalinss, notafiscalsaida.valortotalirrf as valortotalirrf, notafiscalsaida.valortotalcsll as valortotalcsll, notafiscalsaida.valortotalissqn as valortotalissqn, notafiscalsaida.valorliquido as valorliquido, ");
		sqlStr.append(" notafiscalsaida.endereco as endereco, notafiscalsaida.setor as setor, notafiscalsaida.nomeRazaoSocial as nomeRazaoSocial, notafiscalsaida.cnpjCpf as cnpjCpf, notafiscalsaida.cep as cep, notafiscalsaida.inscricaoEstadual as inscricaoEstadual, notafiscalsaida.uf as uf, notafiscalsaida.telefone as telefone, notafiscalsaida.municipio as municipio, ");
		sqlStr.append(" notafiscalsaida.informacaoFiliacaoApresentarDanfe as informacaoFiliacaoApresentarDanfe, notafiscalsaida.issretido, notafiscalsaida.naturezaOperacaoEnum, notafiscalsaida.isIncentivadorCultural, notafiscalsaida.regimeEspecialTributacaoEnum, notafiscalsaida.codigoCNAE, notafiscalsaida.percentualCargaTributaria, notafiscalsaida.valorCargaTributaria, notafiscalsaida.fonteCargaTributaria, ");
		sqlStr.append(" ( select distinct sum(totalissqn) from notafiscalsaidaservico where notafiscalsaidaservico.notafiscalsaida = notafiscalsaida.codigo ) AS totalissqn ");
		sqlStr.append(" FROM notafiscalsaida ");
		sqlStr.append(" LEFT JOIN pessoa as aluno on aluno.codigo = notafiscalsaida.aluno ");
		sqlStr.append(" LEFT JOIN pessoa as responsavel on responsavel.codigo = notafiscalsaida.responsavelfinanceiro ");
		sqlStr.append(" LEFT JOIN funcionario as funcionario on funcionario.codigo = notafiscalsaida.funcionario ");
		sqlStr.append(" LEFT JOIN pessoa as pessoaFuncionario on pessoaFuncionario.codigo = funcionario.pessoa ");
		sqlStr.append(" LEFT JOIN parceiro as parceiro on parceiro.codigo = notafiscalsaida.parceiro ");
		if (valorConsulta != 0) {
			sqlStr.append("WHERE notafiscalsaida.numero = ").append(valorConsulta).append(" ");
		} else {
			sqlStr.append("WHERE notafiscalsaida.numero >= ").append(valorConsulta).append(" ");
		}

		if (!situacao.trim().equals("-1")) {
			sqlStr.append(" and situacao = '").append(situacao).append("' ");
		}

		if (unidadeEnsinoVO != null && unidadeEnsinoVO.getCodigo().intValue() != 0) {
			sqlStr.append(" and unidadeensino = ").append(unidadeEnsinoVO.getCodigo()).append(" ");
		}

		/*
		 * if (prmIni != null && prmFim != null) {
		 * sqlStr.append(" and ((dataemissao>= '");
		 * sqlStr.append(UteisData.getDataJDBC(prmIni));
		 * sqlStr.append("') and (dataemissao <= '");
		 * sqlStr.append(UteisData.getDataJDBC(prmFim)); sqlStr.append("')) "); } else
		 * if (prmIni != null) { sqlStr.append(" and ((dataemissao >= '");
		 * sqlStr.append(UteisData.getDataJDBC(prmIni)); sqlStr.append("')) "); } else
		 * if (prmFim != null) { sqlStr.append(" and ((dataemissao <= '");
		 * sqlStr.append(UteisData.getDataJDBC(prmFim)); sqlStr.append("')) "); }
		 */
		if (limite != 0) {
			sqlStr.append(" ORDER BY notafiscalsaida.numero DESC  limit ");
			sqlStr.append(limite);
			sqlStr.append(" offset ");
			sqlStr.append(pagina);
		} else {
			sqlStr.append(" ORDER BY notafiscalsaida.numero DESC ");
		}

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuarioLogado));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.financeiro.teste#consultarPorNumeroUnico(java.lang
	 * .Long, java.lang.String, boolean, int, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public NotaFiscalSaidaVO consultarPorNumeroUnico(Long valorConsulta, String transferencia, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuarioLogado);
		String sqlStr = "SELECT * FROM NotaFiscalSaida WHERE  numero = " + valorConsulta + " ORDER BY numero";
		if (transferencia.equals("S")) {
			sqlStr = "SELECT * FROM NotaFiscalSaida WHERE  numero  = " + valorConsulta + " and transferencia = true ORDER BY numero";
		}
		if (transferencia.equals("N")) {
			sqlStr = "SELECT * FROM NotaFiscalSaida WHERE numero  = " + valorConsulta + " and transferencia = false ORDER BY numero";
		}

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( NotaFiscalSaida ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuarioLogado));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.financeiro.teste#consultarClienteNotaFiscal(java.
	 * lang.Long, java.lang.String, int, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public NotaFiscalSaidaVO consultarClienteNotaFiscal(Long valorConsulta, String identificador, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {

		String sqlStr = "SELECT * FROM NotaFiscalSaida WHERE  numero = " + valorConsulta + " ORDER BY numero";

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( NotaFiscalSaida ).");
		}
		return montarDados(tabelaResultado, nivelMontarDados, usuarioLogado);

	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List consultarPorMatricula(String valorConsulta, Date prmIni, Date prmFim, String situacao, UnidadeEnsinoVO unidadeEnsinoVO, CursoVO cursoVO, TurmaVO turmaVO, int limite, int pagina, Boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		consultar(getIdEntidade(), true, usuarioLogado);
		List listaResultado = new ArrayList(0);

		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT distinct notafiscalsaida.codigo, notafiscalsaida.serie, notafiscalsaida.linkacesso, notafiscalsaida.numero, notafiscalsaida.numerorps, notafiscalsaida.recibo, notafiscalsaida.matricula, notafiscalsaida.aluno, aluno.nome as \"aluno.nome\", aluno.codigo as \"aluno.codigo\", valortotal, notafiscalsaida.tipopessoa, dataemissao, ");
		sqlStr.append(" notafiscalsaida.situacao, notafiscalsaida.unidadeensino, mensagemretorno, lote, identificadorreceita, protocolo, notafiscalsaida.responsavelfinanceiro, responsavel.nome as \"responsavel.nome\", responsavel.codigo as \"responsavel.codigo\", observacaocontribuinte, ");
		sqlStr.append(" funcionario.codigo as \"funcionario.codigo\", parceiro.codigo as \"parceiro.codigo\", parceiro.nome as \"parceiro.nome\", ");
		sqlStr.append(" notafiscalsaida.valortotalpis as valortotalpis, notafiscalsaida.valortotalcofins as valortotalcofins, ");
		sqlStr.append(" notafiscalsaida.aliquotaissqn as aliquotaissqn, notafiscalsaida.aliquotapis as aliquotapis, notafiscalsaida.aliquotainss as aliquotainss, notafiscalsaida.aliquotacsll as aliquotacsll, notafiscalsaida.aliquotair as aliquotair, ");
		sqlStr.append(" notafiscalsaida.valortotalinss as valortotalinss, notafiscalsaida.valortotalirrf as valortotalirrf, notafiscalsaida.valortotalcsll as valortotalcsll, notafiscalsaida.valortotalissqn as valortotalissqn, notafiscalsaida.valorliquido as valorliquido, ");
		sqlStr.append(" notafiscalsaida.endereco as endereco, notafiscalsaida.setor as setor, notafiscalsaida.nomeRazaoSocial as nomeRazaoSocial, notafiscalsaida.cnpjCpf as cnpjCpf, notafiscalsaida.cep as cep, notafiscalsaida.inscricaoEstadual as inscricaoEstadual, notafiscalsaida.uf as uf, notafiscalsaida.telefone as telefone, notafiscalsaida.municipio as municipio, ");
		sqlStr.append(" notafiscalsaida.informacaoFiliacaoApresentarDanfe as informacaoFiliacaoApresentarDanfe, notafiscalsaida.issretido, notafiscalsaida.naturezaOperacaoEnum, notafiscalsaida.isIncentivadorCultural, notafiscalsaida.regimeEspecialTributacaoEnum, notafiscalsaida.codigoCNAE, notafiscalsaida.percentualCargaTributaria, notafiscalsaida.valorCargaTributaria, notafiscalsaida.fonteCargaTributaria, ");
		sqlStr.append(" ( select distinct sum(totalissqn) from notafiscalsaidaservico where notafiscalsaidaservico.notafiscalsaida = notafiscalsaida.codigo ) AS totalissqn ");
		sqlStr.append(" FROM notafiscalsaida ");
		sqlStr.append(" LEFT JOIN pessoa as responsavel on responsavel.codigo = notafiscalsaida.responsavelfinanceiro ");
		sqlStr.append(" LEFT JOIN funcionario as funcionario on funcionario.codigo = notafiscalsaida.funcionario ");
		sqlStr.append(" LEFT JOIN pessoa as pessoaFuncionario on pessoaFuncionario.codigo = funcionario.pessoa ");
		sqlStr.append(" LEFT JOIN parceiro as parceiro on parceiro.codigo = notafiscalsaida.parceiro ");
		sqlStr.append(" LEFT JOIN pessoa as aluno on aluno.codigo = notafiscalsaida.aluno ");
		sqlStr.append(" WHERE notafiscalsaida.matricula = '").append(valorConsulta).append("' ");

		if (!situacao.trim().equals("-1")) {
			sqlStr.append(" and notafiscalsaida.situacao = '").append(situacao).append("' ");
		}

		if (unidadeEnsinoVO != null && unidadeEnsinoVO.getCodigo().intValue() != 0) {
			sqlStr.append(" and notafiscalsaida.unidadeensino = ").append(unidadeEnsinoVO.getCodigo()).append(" ");
		}

		if (prmIni != null && prmFim != null) {
			sqlStr.append(" and ((dataemissao>= '");
			sqlStr.append(UteisData.getDataJDBC(prmIni));
			sqlStr.append("') and (dataemissao <= '");
			sqlStr.append(UteisData.getDataJDBC(prmFim));
			sqlStr.append("')) ");
		} else if (prmIni != null) {
			sqlStr.append(" and ((dataemissao >= '");
			sqlStr.append(UteisData.getDataJDBC(prmIni));
			sqlStr.append("')) ");
		} else if (prmFim != null) {
			sqlStr.append(" and ((dataemissao <= '");
			sqlStr.append(UteisData.getDataJDBC(prmFim));
			sqlStr.append("')) ");
		}

		if (limite != 0) {
			sqlStr.append(" ORDER BY notafiscalsaida.numero DESC  limit ");
			sqlStr.append(limite);
			sqlStr.append(" offset ");
			sqlStr.append(pagina);
		} else {
			sqlStr.append(" ORDER BY notafiscalsaida.numero DESC ");
		}

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		while (tabelaResultado.next()) {
			listaResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuarioLogado));
		}

		return listaResultado;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.financeiro.teste#consultarPorNomeCliente(java.lang
	 * .String, java.util.Date, java.util.Date, java.lang.String, java.lang.Integer,
	 * java.lang.String, int, int, java.lang.Boolean, int,
	 * negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List consultarPorNomeCliente(String valorConsulta, Date prmIni, Date prmFim, String situacao, UnidadeEnsinoVO unidadeEnsinoVO, CursoVO cursoVO, TurmaVO turmaVO, int limite, int pagina, Boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		consultar(getIdEntidade(), true, usuarioLogado);

		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT notafiscalsaida.codigo, notafiscalsaida.serie, notafiscalsaida.linkacesso, notafiscalsaida.numero, notafiscalsaida.numerorps, notafiscalsaida.recibo, notafiscalsaida.matricula, notafiscalsaida.aluno, aluno.nome as \"aluno.nome\", aluno.codigo as \"aluno.codigo\", valortotal, notafiscalsaida.tipopessoa, dataemissao, ");
		sqlStr.append(" situacao, unidadeensino, mensagemretorno, lote, identificadorreceita, protocolo, responsavelfinanceiro, responsavel.nome as \"responsavel.nome\", responsavel.codigo as \"responsavel.codigo\", observacaocontribuinte, ");
		sqlStr.append(" funcionario.codigo as \"funcionario.codigo\", parceiro.codigo as \"parceiro.codigo\", parceiro.nome as \"parceiro.nome\", ");
		sqlStr.append(" notafiscalsaida.valortotalpis as valortotalpis, notafiscalsaida.valortotalcofins as valortotalcofins, ");
		sqlStr.append(" notafiscalsaida.aliquotaissqn as aliquotaissqn, notafiscalsaida.aliquotapis as aliquotapis, notafiscalsaida.aliquotainss as aliquotainss, notafiscalsaida.aliquotacsll as aliquotacsll, notafiscalsaida.aliquotair as aliquotair, ");
		sqlStr.append(" notafiscalsaida.valortotalinss as valortotalinss, notafiscalsaida.valortotalirrf as valortotalirrf, notafiscalsaida.valortotalcsll as valortotalcsll, notafiscalsaida.valortotalissqn as valortotalissqn, notafiscalsaida.valorliquido as valorliquido, ");
		sqlStr.append(" notafiscalsaida.endereco as endereco, notafiscalsaida.setor as setor, notafiscalsaida.nomeRazaoSocial as nomeRazaoSocial, notafiscalsaida.cnpjCpf as cnpjCpf, notafiscalsaida.cep as cep, notafiscalsaida.inscricaoEstadual as inscricaoEstadual, notafiscalsaida.uf as uf, notafiscalsaida.telefone as telefone, notafiscalsaida.municipio as municipio, ");
		sqlStr.append(" notafiscalsaida.informacaoFiliacaoApresentarDanfe as informacaoFiliacaoApresentarDanfe, notafiscalsaida.issretido, notafiscalsaida.naturezaOperacaoEnum, notafiscalsaida.isIncentivadorCultural, notafiscalsaida.regimeEspecialTributacaoEnum, notafiscalsaida.codigoCNAE, notafiscalsaida.percentualCargaTributaria, notafiscalsaida.valorCargaTributaria, notafiscalsaida.fonteCargaTributaria, ");
		sqlStr.append(" ( select distinct sum(totalissqn) from notafiscalsaidaservico where notafiscalsaidaservico.notafiscalsaida = notafiscalsaida.codigo ) AS totalissqn ");
		sqlStr.append(" FROM notafiscalsaida ");
		sqlStr.append(" LEFT JOIN pessoa as aluno on aluno.codigo = notafiscalsaida.aluno ");
		sqlStr.append(" LEFT JOIN pessoa as responsavel on responsavel.codigo = notafiscalsaida.responsavelfinanceiro ");
		sqlStr.append(" LEFT JOIN funcionario as funcionario on funcionario.codigo = notafiscalsaida.funcionario ");
		sqlStr.append(" LEFT JOIN pessoa as pessoaFuncionario on pessoaFuncionario.codigo = funcionario.pessoa ");
		sqlStr.append(" LEFT JOIN parceiro as parceiro on parceiro.codigo = notafiscalsaida.parceiro ");

		sqlStr.append(" WHERE 1=1 ");
		if (!valorConsulta.trim().isEmpty()) {
			sqlStr.append(" AND CASE WHEN tipopessoa = 'AL' then sem_acentos(aluno.nome) ILIKE sem_acentos('%").append(valorConsulta).append("%') ELSE ");
			sqlStr.append(" CASE WHEN tipopessoa = 'RF' then sem_acentos(responsavel.nome) ILIKE sem_acentos('%").append(valorConsulta).append("%') ELSE ");
			sqlStr.append(" CASE WHEN tipopessoa = 'FU' then sem_acentos(pessoaFuncionario.nome) ILIKE sem_acentos('%").append(valorConsulta).append("%') ELSE ");
			sqlStr.append(" CASE WHEN tipopessoa = 'PA' then sem_acentos(parceiro.nome) ILIKE sem_acentos('%").append(valorConsulta).append("%') END END END END ");
		}
		if (!situacao.trim().equals("-1")) {
			sqlStr.append(" and notafiscalsaida.situacao = '").append(situacao).append("' ");
		}

		if (unidadeEnsinoVO != null && unidadeEnsinoVO.getCodigo().intValue() != 0) {
			sqlStr.append(" and notafiscalsaida.unidadeensino = ").append(unidadeEnsinoVO.getCodigo()).append(" ");
		}

		if (prmIni != null && prmFim != null) {
			sqlStr.append(" and ((dataemissao>= '");
			sqlStr.append(UteisData.getDataJDBC(prmIni));
			sqlStr.append("') and (dataemissao <= '");
			sqlStr.append(UteisData.getDataJDBC(prmFim));
			sqlStr.append("')) ");
		} else if (prmIni != null) {
			sqlStr.append(" and ((dataemissao >= '");
			sqlStr.append(UteisData.getDataJDBC(prmIni));
			sqlStr.append("')) ");
		} else if (prmFim != null) {
			sqlStr.append(" and ((dataemissao <= '");
			sqlStr.append(UteisData.getDataJDBC(prmFim));
			sqlStr.append("')) ");
		}

		if (limite != 0) {
			sqlStr.append(" ORDER BY notafiscalsaida.numero DESC  limit ");
			sqlStr.append(limite);
			sqlStr.append(" offset ");
			sqlStr.append(pagina);
		} else {
			sqlStr.append(" ORDER BY notafiscalsaida.numero DESC ");
		}

		List listaResultado = new ArrayList(0);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		while (tabelaResultado.next()) {
			listaResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuarioLogado));
		}
		return listaResultado;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.financeiro.teste#consultarPorDataEmissao(java.util
	 * .Date, java.util.Date, java.lang.String, java.lang.Integer, int, int,
	 * boolean, int, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List consultarPorDataEmissao(Date prmIni, Date prmFim, String situacao, int limite, int pagina, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuarioLogado);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT notafiscalsaida.codigo, notafiscalsaida.serie, notafiscalsaida.numero, notafiscalsaida.numerorps, notafiscalsaida.recibo, notafiscalsaida.matricula, notafiscalsaida.aluno, aluno.nome as \"aluno.nome\", aluno.codigo as \"aluno.codigo\", valortotal, notafiscalsaida.tipopessoa, dataemissao, ");
		sqlStr.append(" situacao, unidadeensino, mensagemretorno, lote, identificadorreceita, protocolo, responsavelfinanceiro, responsavel.nome as \"responsavel.nome\", responsavel.codigo as \"responsavel.codigo\", observacaocontribuinte, ");
		sqlStr.append(" funcionario.codigo as \"funcionario.codigo\", parceiro.codigo as \"parceiro.codigo\", parceiro.nome as \"parceiro.nome\", ");
		sqlStr.append(" notafiscalsaida.valortotalpis as valortotalpis, notafiscalsaida.valortotalcofins as valortotalcofins, ");
		sqlStr.append(" notafiscalsaida.aliquotaissqn as aliquotaissqn, notafiscalsaida.aliquotapis as aliquotapis, notafiscalsaida.aliquotainss as aliquotainss, notafiscalsaida.aliquotacsll as aliquotacsll, notafiscalsaida.aliquotair as aliquotair, ");
		sqlStr.append(" notafiscalsaida.valortotalinss as valortotalinss, notafiscalsaida.valortotalirrf as valortotalirrf, notafiscalsaida.valortotalcsll as valortotalcsll, notafiscalsaida.valortotalissqn as valortotalissqn, notafiscalsaida.valorliquido as valorliquido, ");
		sqlStr.append(" notafiscalsaida.endereco as endereco, notafiscalsaida.setor as setor, notafiscalsaida.nomeRazaoSocial as nomeRazaoSocial, notafiscalsaida.cnpjCpf as cnpjCpf, notafiscalsaida.cep as cep, notafiscalsaida.inscricaoEstadual as inscricaoEstadual, notafiscalsaida.uf as uf, notafiscalsaida.telefone as telefone, notafiscalsaida.municipio as municipio, notafiscalsaida.issretido, notafiscalsaida.naturezaOperacaoEnum, notafiscalsaida.isIncentivadorCultural, notafiscalsaida.regimeEspecialTributacaoEnum, notafiscalsaida.codigoCNAE, notafiscalsaida.percentualCargaTributaria, notafiscalsaida.valorCargaTributaria, notafiscalsaida.fonteCargaTributaria, ");
		sqlStr.append(" ( select distinct sum(totalissqn) from notafiscalsaidaservico where notafiscalsaidaservico.notafiscalsaida = notafiscalsaida.codigo ) AS totalissqn ");
		sqlStr.append(" FROM notafiscalsaida ");
		sqlStr.append(" LEFT JOIN pessoa as aluno on aluno.codigo = notafiscalsaida.aluno ");
		sqlStr.append(" LEFT JOIN pessoa as responsavel on responsavel.codigo = notafiscalsaida.responsavelfinanceiro ");
		sqlStr.append(" LEFT JOIN funcionario as funcionario on funcionario.codigo = notafiscalsaida.funcionario ");
		sqlStr.append(" LEFT JOIN pessoa as pessoaFuncionario on pessoaFuncionario.codigo = funcionario.pessoa ");
		sqlStr.append(" LEFT JOIN parceiro as parceiro on parceiro.codigo = notafiscalsaida.parceiro ");
		sqlStr.append(" where 1=1 ");

		if (!situacao.trim().equals("-1")) {
			sqlStr.append(" and situacao = '").append(situacao).append("' ");
		}

		if (prmIni != null && prmFim != null) {
			sqlStr.append(" and ((dataEmissao>= '");
			sqlStr.append(UteisData.getDataJDBC(prmIni));
			sqlStr.append("') and (dataEmissao <= '");
			sqlStr.append(UteisData.getDataJDBC(prmFim));
			sqlStr.append("')) ");
		} else if (prmIni != null) {
			sqlStr.append(" and ((dataEmissao >= '");
			sqlStr.append(UteisData.getDataJDBC(prmIni));
			sqlStr.append("')) ");
		} else if (prmFim != null) {
			sqlStr.append(" and ((dataEmissao <= '");
			sqlStr.append(UteisData.getDataJDBC(prmFim));
			sqlStr.append("')) ");
		}

		if (limite != 0) {
			sqlStr.append(" ORDER BY numero DESC  limit ");
			sqlStr.append(limite);
			sqlStr.append(" offset ");
			sqlStr.append(pagina);
		} else {
			sqlStr.append(" ORDER BY numero DESC ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuarioLogado));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.financeiro.teste#consultarPorCodigo(java.lang.Integer ,
	 * java.util.Date, java.util.Date, java.lang.Integer, int, int, boolean, int,
	 * negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List consultarPorCodigo(Integer valorConsulta, Date prmIni, Date prmFim, String situacao, int limite, int pagina, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuarioLogado);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT notafiscalsaida.codigo, notafiscalsaida.serie, notafiscalsaida.linkacesso, notafiscalsaida.numero, notafiscalsaida.numerorps, notafiscalsaida.recibo, notafiscalsaida.matricula, notafiscalsaida.aluno, aluno.nome as \"aluno.nome\", aluno.codigo as \"aluno.codigo\", valortotal, notafiscalsaida.tipopessoa, dataemissao, ");
		sqlStr.append(" situacao, unidadeensino, mensagemretorno, lote, identificadorreceita, protocolo, responsavelfinanceiro, responsavel.nome as \"responsavel.nome\", responsavel.codigo as \"responsavel.codigo\", observacaocontribuinte, ");
		sqlStr.append(" funcionario.codigo as \"funcionario.codigo\", parceiro.codigo as \"parceiro.codigo\", parceiro.nome as \"parceiro.nome\", ");
		sqlStr.append(" notafiscalsaida.valortotalpis as valortotalpis, notafiscalsaida.valortotalcofins as valortotalcofins, ");
		sqlStr.append(" notafiscalsaida.aliquotaissqn as aliquotaissqn, notafiscalsaida.aliquotapis as aliquotapis, notafiscalsaida.aliquotainss as aliquotainss, notafiscalsaida.aliquotacsll as aliquotacsll, notafiscalsaida.aliquotair as aliquotair, ");
		sqlStr.append(" notafiscalsaida.valortotalinss as valortotalinss, notafiscalsaida.valortotalirrf as valortotalirrf, notafiscalsaida.valortotalcsll as valortotalcsll, notafiscalsaida.valortotalissqn as valortotalissqn, notafiscalsaida.valorliquido as valorliquido, ");
		sqlStr.append(" notafiscalsaida.endereco as endereco, notafiscalsaida.setor as setor, notafiscalsaida.nomeRazaoSocial as nomeRazaoSocial, notafiscalsaida.cnpjCpf as cnpjCpf, notafiscalsaida.cep as cep, notafiscalsaida.inscricaoEstadual as inscricaoEstadual, notafiscalsaida.uf as uf, notafiscalsaida.telefone as telefone, notafiscalsaida.municipio as municipio, ");
		sqlStr.append(" notafiscalsaida.informacaoFiliacaoApresentarDanfe as informacaoFiliacaoApresentarDanfe, notafiscalsaida.issretido, notafiscalsaida.naturezaOperacaoEnum, notafiscalsaida.isIncentivadorCultural, notafiscalsaida.regimeEspecialTributacaoEnum, notafiscalsaida.codigoCNAE, notafiscalsaida.percentualCargaTributaria, notafiscalsaida.valorCargaTributaria, notafiscalsaida.fonteCargaTributaria, ");
		sqlStr.append(" ( select distinct sum(totalissqn) from notafiscalsaidaservico where notafiscalsaidaservico.notafiscalsaida = notafiscalsaida.codigo ) AS totalissqn ");
		sqlStr.append(" FROM notafiscalsaida ");
		sqlStr.append(" LEFT JOIN pessoa as aluno on aluno.codigo = notafiscalsaida.aluno ");
		sqlStr.append(" LEFT JOIN pessoa as responsavel on responsavel.codigo = notafiscalsaida.responsavelfinanceiro ");
		sqlStr.append(" LEFT JOIN funcionario as funcionario on funcionario.codigo = notafiscalsaida.funcionario ");
		sqlStr.append(" LEFT JOIN pessoa as pessoaFuncionario on pessoaFuncionario.codigo = funcionario.pessoa ");
		sqlStr.append(" LEFT JOIN parceiro as parceiro on parceiro.codigo = notafiscalsaida.parceiro ");
		sqlStr.append(" WHERE notafiscalsaida.codigo = " + valorConsulta.intValue());

		if (!situacao.trim().equals("-1")) {
			sqlStr.append(" and notafiscalsaida.situacao = '").append(situacao).append("' ");
		}

		/*
		 * if (valorConsulta.intValue() == 0) { if (prmIni != null && prmFim != null) {
		 * sqlStr.append(" and ((dataemissao>= '");
		 * sqlStr.append(UteisData.getDataJDBC(prmIni));
		 * sqlStr.append("') and (dataemissao <= '");
		 * sqlStr.append(UteisData.getDataJDBC(prmFim)); sqlStr.append("')) "); } else
		 * if (prmIni != null) { sqlStr.append(" and ((dataemissao >= '");
		 * sqlStr.append(UteisData.getDataJDBC(prmIni)); sqlStr.append("')) "); } else
		 * if (prmFim != null) { sqlStr.append(" and ((dataemissao <= '");
		 * sqlStr.append(UteisData.getDataJDBC(prmFim)); sqlStr.append("')) "); } }
		 */

		if (limite != 0) {
			sqlStr.append(" ORDER BY notafiscalsaida.numero DESC  limit ");
			sqlStr.append(limite);
			sqlStr.append(" offset ");
			sqlStr.append(pagina);
		} else {
			sqlStr.append(" ORDER BY notafiscalsaida.numero DESC ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuarioLogado));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.financeiro.teste#montarDadosConsulta(org.springframework
	 * .jdbc.support.rowset.SqlRowSet, int, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class })
	public List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		List vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuarioLogado));
		}
		tabelaResultado = null;
		return vetResultado;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.financeiro.teste#consultarPorDataEmissaoCliente(java
	 * .util.Date, java.util.Date, java.lang.Integer, int, int, boolean, int,
	 * negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List consultarPorDataEmissaoCliente(Date prmIni, Date prmFim, Integer cliente, int limite, int pagina, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuarioLogado);
		StringBuilder sqlStr = new StringBuilder("SELECT * FROM NotaFiscalSaida where cliente = '" + cliente + "' and situacao = 'AU' ");

		if (prmIni != null && prmFim != null) {
			sqlStr.append(" and ((dataEmissao>= '");
			sqlStr.append(UteisData.getDataJDBC(prmIni));
			sqlStr.append("') and (dataEmissao <= '");
			sqlStr.append(UteisData.getDataJDBC(prmFim));
			sqlStr.append("')) ");
		} else if (prmIni != null) {
			sqlStr.append(" and ((dataEmissao >= '");
			sqlStr.append(UteisData.getDataJDBC(prmIni));
			sqlStr.append("')) ");
		} else if (prmFim != null) {
			sqlStr.append(" and ((dataEmissao <= '");
			sqlStr.append(UteisData.getDataJDBC(prmFim));
			sqlStr.append("')) ");
		}

		if (limite != 0) {
			sqlStr.append(" ORDER BY numero desc limit ");
			sqlStr.append(limite);
			sqlStr.append(" offset ");
			sqlStr.append(pagina);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuarioLogado));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.financeiro.teste#
	 * obterTotalRegistroConsultarDataEmissaoCliente(java.lang.Integer,
	 * java.util.Date, java.util.Date)
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public Integer obterTotalRegistroConsultarDataEmissaoCliente(Integer cliente, Date prmIni, Date prmFim) throws Exception {
		StringBuilder sqlStr = new StringBuilder("SELECT COUNT(NotaFiscalSaida.codigo) FROM NotaFiscalSaida WHERE 1=1 and cliente = '" + cliente + "' and situacao = 'AU' ");

		if (prmIni != null && prmFim != null) {
			sqlStr.append(" and ((dataEmissao>= '");
			sqlStr.append(UteisData.getDataJDBC(prmIni));
			sqlStr.append("') and (dataEmissao <= '");
			sqlStr.append(UteisData.getDataJDBC(prmFim));
			sqlStr.append("')) ");
		} else if (prmIni != null) {
			sqlStr.append(" and ((dataEmissao >= '");
			sqlStr.append(UteisData.getDataJDBC(prmIni));
			sqlStr.append("')) ");
		} else if (prmFim != null) {
			sqlStr.append(" and ((dataEmissao <= '");
			sqlStr.append(UteisData.getDataJDBC(prmFim));
			sqlStr.append("')) ");
		}

		return (getTotalRegistro(getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString())));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.financeiro.teste#consultarPorNumeroUnicoCliente(java
	 * .lang.Long, java.lang.Integer, boolean, int,
	 * negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public NotaFiscalSaidaVO consultarPorNumeroUnicoCliente(Long valorConsulta, Integer cliente, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuarioLogado);
		String sqlStr = "SELECT * FROM NotaFiscalSaida WHERE  numero = " + valorConsulta + " and cliente = '" + cliente + "' ORDER BY numero";

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( NotaFiscalSaida ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuarioLogado));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.financeiro.teste#
	 * obterTotalRegistroConsultarPorNumeroUnicoCliente(java.lang.Long,
	 * java.lang.Integer)
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public Integer obterTotalRegistroConsultarPorNumeroUnicoCliente(Long valorConsulta, Integer cliente) throws Exception {
		StringBuilder sqlStr = new StringBuilder("SELECT COUNT(notaFiscalSaida.codigo) FROM NotaFiscalSaida WHERE numero = " + valorConsulta + " and cliente = '" + cliente + "' ");
		return (getTotalRegistro(getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString())));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.financeiro.teste#montarDados(org.springframework.
	 * jdbc.support.rowset.SqlRowSet, int, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class })
	public NotaFiscalSaidaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		NotaFiscalSaidaVO obj = new NotaFiscalSaidaVO();
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setSerie(dadosSQL.getString("serie"));
		obj.setDataEmissao(dadosSQL.getDate("dataEmissao"));
		obj.getUnidadeEnsinoVO().setCodigo(dadosSQL.getInt("unidadeensino"));
		obj.setNumero(dadosSQL.getLong("numero"));
		obj.setNumeroRPS(dadosSQL.getInt("numerorps"));
		obj.setLote(dadosSQL.getString("lote"));
		obj.setValorTotal((dadosSQL.getDouble("valortotal")));
		obj.setSituacao(dadosSQL.getString("situacao"));
		obj.setMatricula(dadosSQL.getString("matricula"));
		obj.setTipoPessoa(dadosSQL.getString("tipoPessoa"));
		obj.getPessoaVO().setCodigo(dadosSQL.getInt("aluno.codigo"));
		obj.getResponsavelFinanceiro().setCodigo(dadosSQL.getInt("responsavel.codigo"));
		obj.getFuncionarioVO().setCodigo(dadosSQL.getInt("funcionario.codigo"));
		obj.getParceiroVO().setCodigo(dadosSQL.getInt("parceiro.codigo"));
		obj.setIdentificadorReceita(dadosSQL.getString("identificadorreceita"));
		obj.setProtocolo(dadosSQL.getString("protocolo"));
		obj.setMensagemRetorno(dadosSQL.getString("mensagemretorno"));
		obj.setObservacaoContribuinte(dadosSQL.getString("observacaocontribuinte"));
		obj.setTotalIssqn(dadosSQL.getDouble("totalissqn"));
		// dados de impostos
		obj.setAliquotaIssqn(dadosSQL.getDouble("aliquotaissqn"));
		obj.setAliquotaPis(dadosSQL.getDouble("aliquotapis"));
		obj.setAliquotaInss(dadosSQL.getDouble("aliquotainss"));
		obj.setAliquotaCsll(dadosSQL.getDouble("aliquotacsll"));
		obj.setAliquotaIr(dadosSQL.getDouble("aliquotair"));
		obj.setValorTotalINSS(dadosSQL.getDouble("valortotalinss"));
		obj.setValorTotalIRRF(dadosSQL.getDouble("valortotalirrf"));
		obj.setValorTotalCSLL(dadosSQL.getDouble("valortotalcsll"));
		obj.setValorLiquido(dadosSQL.getDouble("valorliquido"));
		obj.setValorTotalPIS(dadosSQL.getDouble("valortotalpis"));
		obj.setValorTotalCOFINS(dadosSQL.getDouble("valortotalcofins"));
		// dados de endereço e afins
		obj.setEndereco(dadosSQL.getString("endereco"));
		obj.setSetor(dadosSQL.getString("setor"));
		obj.setNomeRazaoSocial(dadosSQL.getString("nomeRazaoSocial"));
		obj.setCnpjCpf(dadosSQL.getString("cnpjCpf"));
		obj.setCep(dadosSQL.getString("cep"));
		obj.setInscricaoEstadual(dadosSQL.getString("inscricaoEstadual"));
		obj.setUf(dadosSQL.getString("uf"));
		obj.setTelefone(dadosSQL.getString("telefone"));
		obj.setMunicipio(dadosSQL.getString("municipio"));
		obj.setInformacaoFiliacaoApresentarDanfe(dadosSQL.getString("informacaoFiliacaoApresentarDanfe"));
		obj.setIssRetido(dadosSQL.getBoolean("issRetido"));
		try {
			obj.setNaturezaOperacaoEnum(NaturezaOperacaoEnum.valueOf(NaturezaOperacaoEnum.class, dadosSQL.getString("naturezaOperacaoEnum")));
		} catch (Exception e) {
			obj.setNaturezaOperacaoEnum(null);
		}
		obj.setIsIncentivadorCultural(dadosSQL.getBoolean("isIncentivadorCultural"));
		if (Uteis.isAtributoPreenchido(dadosSQL.getString("regimeEspecialTributacaoEnum"))) {
			obj.setRegimeEspecialTributacaoEnum(RegimeEspecialTributacaoEnum.valueOf(dadosSQL.getString("regimeEspecialTributacaoEnum")));
		} else {
			obj.setRegimeEspecialTributacaoEnum(null);
		}
		obj.setCodigoCNAE(dadosSQL.getString("codigoCNAE"));
		obj.setPercentualCargaTributaria(dadosSQL.getDouble("percentualCargaTributaria"));
		obj.setValorCargaTributaria(dadosSQL.getDouble("valorCargaTributaria"));
		obj.setFonteCargaTributaria(dadosSQL.getString("fonteCargaTributaria"));
		obj.setRecibo(dadosSQL.getString("recibo"));
		if (obj.getTipoPessoa().equals(TipoPessoa.PARCEIRO.getValor())) {
			if (!obj.getMatricula().isEmpty()) {
				try {
					obj.setPessoaVO(getFacadeFactory().getPessoaFacade().consultarAlunoPorMatricula(obj.getMatricula(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioLogado));
				} catch (Exception e) {
					obj.setPessoaVO(getFacadeFactory().getPessoaFacade().consultarDadosGerarNotaFiscalSaida(obj.getPessoaVO().getCodigo(), false, usuarioLogado));
				}
			}
			obj.setParceiroVO(getFacadeFactory().getParceiroFacade().consultarPorChavePrimaria(obj.getParceiroVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado));
		} else if (obj.getTipoPessoa().equals(TipoPessoa.FUNCIONARIO.getValor())) {
			obj.setFuncionarioVO(getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimaria(obj.getFuncionarioVO().getCodigo(), obj.getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado));
		} else if (obj.getTipoPessoa().equals(TipoPessoa.RESPONSAVEL_FINANCEIRO.getValor())) {
			obj.setPessoaVO(getFacadeFactory().getPessoaFacade().consultarDadosGerarNotaFiscalSaida(obj.getPessoaVO().getCodigo(), false, usuarioLogado));
			obj.setResponsavelFinanceiro(getFacadeFactory().getPessoaFacade().consultarDadosGerarNotaFiscalSaida(obj.getResponsavelFinanceiro().getCodigo(), false, usuarioLogado));
		} else {
			obj.setPessoaVO(getFacadeFactory().getPessoaFacade().consultarDadosGerarNotaFiscalSaida(obj.getPessoaVO().getCodigo(), false, usuarioLogado));
		}
		obj.setIdentificadorReceita(dadosSQL.getString("identificadorreceita"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_TODOS) {
			obj.setModelo(dadosSQL.getString("modelo"));
			obj.setDataStuacao(dadosSQL.getDate("datasituacao"));
			obj.setNomeNaturezaOperacao(dadosSQL.getString("nomenaturezaoperacao"));
			obj.setCodigoNaturezaOperacao(dadosSQL.getString("codigonaturezaoperacao"));
			obj.setNotaFiscalSaidaServicoVOs(getFacadeFactory().getNotaFiscalSaidaServicoFacade().consultarNotaFiscalSaidaServicos(dadosSQL.getInt("codigo"), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado));
		}
		obj.setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(dadosSQL.getInt("unidadeensino"), false, Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado));
		obj.setTotalBaseIssqn(obj.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getIssqn());
		obj.setLinkAcesso(dadosSQL.getString("linkacesso"));
		obj.setWebServicesNFSEEnum(WebServicesNFSEEnum.getEnumPorIdCidadeTipoAcaoServico(obj.getUnidadeEnsinoVO().getCidade().getCodigo(), TipoAcaoServicoNFSEEnum.GerarNfse));
		CidadeVO cidade = obj.getUnidadeEnsinoVO().getCidadeNFSe();
		if (cidade.isWebserviceNFSeImplementado()) {
			obj.setWebServicesNFSEEnum(WebServicesNFSEEnum.NFSE_WEBSERVICE);
		}
		obj.getCartaCorrecaoVOs().addAll(getFacadeFactory().getCartaCorrecaoNotaFiscalFacade().consultarPorNotaFiscal(obj.getCodigo(), nivelMontarDados, usuarioLogado));
		obj.setNovoObj(false);
		return obj;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.financeiro.teste#consultarPorChavePrimaria(java.lang
	 * .Integer, int, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public NotaFiscalSaidaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		// consultar(getIdEntidade(), false);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT notafiscalsaida.codigo, notafiscalsaida.serie, notafiscalsaida.numero, notafiscalsaida.numerorps, notafiscalsaida.linkacesso, notafiscalsaida.matricula, notafiscalsaida.aluno, aluno.nome as \"aluno.nome\", aluno.codigo as \"aluno.codigo\", valortotal, notafiscalsaida.tipopessoa, dataemissao, ");
		sqlStr.append(" situacao, unidadeensino, mensagemretorno, lote, responsavelfinanceiro, responsavel.nome as \"responsavel.nome\", responsavel.codigo as \"responsavel.codigo\", observacaocontribuinte, ");
		sqlStr.append(" funcionario.codigo as \"funcionario.codigo\", parceiro.codigo as \"parceiro.codigo\", parceiro.nome as \"parceiro.nome\", ");
		sqlStr.append(" notafiscalsaida.valortotalpis as valortotalpis, notafiscalsaida.valortotalcofins as valortotalcofins, ");
		sqlStr.append(" notafiscalsaida.aliquotaissqn as aliquotaissqn, notafiscalsaida.aliquotapis as aliquotapis, notafiscalsaida.aliquotainss as aliquotainss, notafiscalsaida.aliquotacsll as aliquotacsll, notafiscalsaida.aliquotair as aliquotair, ");
		sqlStr.append(" notafiscalsaida.valortotalinss as valortotalinss, notafiscalsaida.valortotalirrf as valortotalirrf, notafiscalsaida.valortotalcsll as valortotalcsll, notafiscalsaida.valortotalissqn as valortotalissqn, notafiscalsaida.valorliquido as valorliquido, ");
		sqlStr.append(" notafiscalsaida.endereco as endereco, notafiscalsaida.setor as setor, notafiscalsaida.nomeRazaoSocial as nomeRazaoSocial, notafiscalsaida.cnpjCpf as cnpjCpf, notafiscalsaida.cep as cep, notafiscalsaida.inscricaoEstadual as inscricaoEstadual, notafiscalsaida.uf as uf, notafiscalsaida.telefone as telefone, notafiscalsaida.municipio as municipio, ");
		sqlStr.append(" notafiscalsaida.informacaoFiliacaoApresentarDanfe as informacaoFiliacaoApresentarDanfe, notafiscalsaida.issretido, notafiscalsaida.naturezaOperacaoEnum, notafiscalsaida.isIncentivadorCultural, notafiscalsaida.regimeEspecialTributacaoEnum, notafiscalsaida.codigoCNAE, notafiscalsaida.percentualCargaTributaria, notafiscalsaida.valorCargaTributaria, notafiscalsaida.fonteCargaTributaria, ");
		sqlStr.append(" ( select distinct sum(totalissqn) from notafiscalsaidaservico where notafiscalsaidaservico.notafiscalsaida = notafiscalsaida.codigo ) AS totalissqn, identificadorreceita, modelo, serie, recibo, datasituacao, ");
		sqlStr.append(" nomenaturezaoperacao, protocolo, codigonaturezaoperacao  ");
		sqlStr.append(" FROM notafiscalsaida ");
		sqlStr.append(" LEFT JOIN pessoa as aluno on aluno.codigo = notafiscalsaida.aluno ");
		sqlStr.append(" LEFT JOIN pessoa as responsavel on responsavel.codigo = notafiscalsaida.responsavelfinanceiro ");
		sqlStr.append(" LEFT JOIN funcionario as funcionario on funcionario.codigo = notafiscalsaida.funcionario ");
		sqlStr.append(" LEFT JOIN parceiro as parceiro on parceiro.codigo = notafiscalsaida.parceiro ");
		sqlStr.append(" WHERE notafiscalsaida.codigo = ").append(codigoPrm);

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( NotaFiscalSaida ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuarioLogado));
	}

	/**
	 * Operação responsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as operações
	 * desta classe.
	 */
	public static String getIdEntidade() {
		return NotaFiscalSaida.idEntidade;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.financeiro.teste#setIdEntidade(java.lang.String)
	 */
	@Override
	public void setIdEntidade(String idEntidade) {
		NotaFiscalSaida.idEntidade = idEntidade;
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public String consultaXmlNotaFiscalParaGerarArquivoCliente(Integer codigoNotaFiscal, Boolean controleAcesso, UsuarioVO usuarioLogado) throws Exception {
		consultar(getIdEntidade(), controleAcesso, usuarioLogado);
		StringBuilder sqlStr = new StringBuilder("SELECT xmlEnvio FROM NotaFiscalSaida WHERE codigo =  " + codigoNotaFiscal);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getString("xmlEnvio");
		}
		return "";
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public String corrigirNotaFiscal(NotaFiscalSaidaVO notaFiscalSaidaVO, String caminhoNFe, AmbienteNfeEnum ambienteNfeEnum, TipoCertificadoEnum tipoCertificadoEnum, String caminhoCertificado, UsuarioVO usuarioLogado) throws Exception {
		int sequenciaAtual = notaFiscalSaidaVO.getSeqEventoCartaCorrecao();
		try {
			executarValidacaoPossibilidadeCorrigirNota(notaFiscalSaidaVO);
			// executarValidacaoTipoCertificado(tipoCertificado);
			UteisNfe.validarCaminhoCertificado(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO(), getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesLocalUploadArquivoFixo());
			String xmlRetorno = "";
			String protocolo = "";
			notaFiscalSaidaVO.setSeqEventoCartaCorrecao(consultarProximaSequenciaCartaCorrecao(notaFiscalSaidaVO.getCodigo()) + 1);
			if (notaFiscalSaidaVO.getNumero() != 0) {
				xmlRetorno = CartaCorrecao.A1(notaFiscalSaidaVO.getUnidadeEnsinoVO().getCidade().getEstado().getCodigoIBGE(), UteisJSF.getCaminhoWebNFe() + File.separator + Uteis.getMontarCodigoBarra(notaFiscalSaidaVO.getNumeroNota(), 9) + "CartaCorrecao.xml", notaFiscalSaidaVO.getChaveAcesso(), Uteis.removerMascara(notaFiscalSaidaVO.getUnidadeEnsinoVO().getCNPJ()), notaFiscalSaidaVO.getDescricaoCartaCorrecao(), notaFiscalSaidaVO.getSeqEventoCartaCorrecao(), getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesLocalUploadArquivoFixo(), notaFiscalSaidaVO
				        .getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO(), usuarioLogado);
				// notaFiscalSaidaVO.setXmlEnvioCartaCorrecao(UteisNfe.obterXMLNFeSemAssinaturaPorCaminho(UteisJSF.getCaminhoWebNFe()
				// + File.separator +
				// Uteis.getMontarCodigoBarra(notaFiscalSaidaVO.getNumeroNota(),
				// 9) + "CartaCorrecao.xmlCartaCorrecao"));
				notaFiscalSaidaVO.setXmlCartaCorrecao(xmlRetorno);
				// //System.out.println(xmlRetorno);
			}
			String codigoRetorno = UteisNfe.obterCodigoRetorno(xmlRetorno);
			// //System.out.println("CARTACORRECAO: " + codigoRetorno);
			if (codigoRetorno.equals("135")) {
				notaFiscalSaidaVO.setProtocoloCartaCorrecao(UteisNfe.getProtocoloEnvioNFe(xmlRetorno));
				gravarSituacaoCartaCorrecao(notaFiscalSaidaVO, usuarioLogado);

				String xml = notaFiscalSaidaVO.getXmlCartaCorrecao();
				String nomeXml = Uteis.getMontarCodigoBarra(notaFiscalSaidaVO.getNumero().toString(), 9);
				gerarArquivosEnvio(Arrays.asList(gerarArquivoXml(xml, nomeXml)), "CC", new ConfiguracaoGeralSistemaVO());

			} else {
				notaFiscalSaidaVO.setSeqEventoCartaCorrecao(sequenciaAtual);
			}
			return CodigoRetorno.MensagemRetorno(codigoRetorno);
		} catch (Exception e) {
			notaFiscalSaidaVO.setSeqEventoCartaCorrecao(sequenciaAtual);
			throw e;
		}
	}

	public Integer consultarProximaSequenciaCartaCorrecao(Integer codigoNotaFiscal) throws Exception {
		StringBuilder sqlStr = new StringBuilder("SELECT (seqEventoCartaCorrecao) as seq FROM NotaFiscalSaida WHERE codigo =  " + codigoNotaFiscal);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("seq");
		}
		return 1;
	}

	private void validarSeNotaFiscalAindaConstaNaBaseDeDados(Integer codigoNotaFiscal) throws Exception {
		StringBuilder sqlStr = new StringBuilder("SELECT codigo FROM NotaFiscalSaida WHERE codigo =  " + codigoNotaFiscal);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return;
		} else {
			throw new Exception("Nota Fiscal não encontrada! Verifique se a mesma não foi excluida.");
		}
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public Double consultarValorTotalPorCodigo(Integer codigoPrm, UsuarioVO usuarioLogado) throws Exception {
		String sql = "SELECT valorTotal FROM NotaFiscalSaida WHERE codigo = ?";

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			return 0.0;
		} else {
			return tabelaResultado.getDouble("valortotal");
		}
	}

	public void adicionarNotaFiscalServico(NotaFiscalSaidaVO notaFiscalSaidaVO, ContaReceberRecebimentoVO contaReceberRecebimentoVO, ConfiguracaoNotaFiscalVO configuracaoNotaFiscalVO) throws Exception {
		String nomePessoa = getFacadeFactory().getPessoaFacade().consultarDadosGerarNotaFiscalSaida(contaReceberRecebimentoVO.getContaReceberVO().getPessoa().getCodigo(), false, new UsuarioVO()).getNome();
		if (Uteis.isAtributoPreenchido(notaFiscalSaidaVO.getInformacaoFiliacaoApresentarDanfe()) && !notaFiscalSaidaVO.getInformacaoFiliacaoApresentarDanfe().contains(nomePessoa)) {
			notaFiscalSaidaVO.setInformacaoFiliacaoApresentarDanfe(notaFiscalSaidaVO.getInformacaoFiliacaoApresentarDanfe() + " , " + nomePessoa);
		} else {
			notaFiscalSaidaVO.setInformacaoFiliacaoApresentarDanfe("Aluno(s): " + nomePessoa);
		}
		for (NotaFiscalSaidaServicoVO nfss : notaFiscalSaidaVO.getNotaFiscalSaidaServicoVOs()) {
			if (nfss.getContaReceberRecebimentoVO().getContaReceber().equals(contaReceberRecebimentoVO.getContaReceber())) {
				nfss.setPrecoUnitario(nfss.getPrecoUnitario() + contaReceberRecebimentoVO.getValorRecebimento());
				nfss.setAliquotaPIS(configuracaoNotaFiscalVO.getPis());
				nfss.setAliquotaCOFINS(configuracaoNotaFiscalVO.getCofins());
				nfss.setTotalIssqn(new Double(Uteis.getValorTruncadoDeDoubleParaString((nfss.getBaseIssqn() * nfss.getPrecoUnitario()), 2)));
				nfss.setTotalPIS(new Double(Uteis.getValorTruncadoDeDoubleParaString((nfss.getPrecoUnitario() * (nfss.getAliquotaPIS() / 100)), 2)));
				nfss.setTotalCOFINS(new Double(Uteis.getValorTruncadoDeDoubleParaString((nfss.getPrecoUnitario() * (nfss.getAliquotaCOFINS() / 100)), 2)));
				nfss.setTotalINSS(new Double(Uteis.getValorTruncadoDeDoubleParaString((nfss.getPrecoUnitario() * configuracaoNotaFiscalVO.getInss()), 2)));
				nfss.setTotalCSLL(new Double(Uteis.getValorTruncadoDeDoubleParaString((nfss.getPrecoUnitario() * configuracaoNotaFiscalVO.getCsll()), 2)));
				nfss.setTotalIRRF(new Double(Uteis.getValorTruncadoDeDoubleParaString((nfss.getPrecoUnitario() * configuracaoNotaFiscalVO.getAliquotaIR()), 2)));
				nfss.getListaCodigoContaReceberRecebimento().add(contaReceberRecebimentoVO.getCodigo());
				return;
			}
		}
		notaFiscalSaidaVO.getNotaFiscalSaidaServicoVOs().add(montarDadosNotaFiscalSaidaServico(contaReceberRecebimentoVO, configuracaoNotaFiscalVO));
	}

	public void adicionarNotaFiscalServico(NotaFiscalSaidaVO notaFiscalSaidaVO, ContaReceberVO contaReceberVO, ConfiguracaoNotaFiscalVO configuracaoNotaFiscalVO) throws Exception {
		String nomePessoa = getFacadeFactory().getPessoaFacade().consultarDadosGerarNotaFiscalSaida(contaReceberVO.getPessoa().getCodigo(), false, new UsuarioVO()).getNome();
		if (Uteis.isAtributoPreenchido(notaFiscalSaidaVO.getInformacaoFiliacaoApresentarDanfe()) && !notaFiscalSaidaVO.getInformacaoFiliacaoApresentarDanfe().contains(nomePessoa)) {
			notaFiscalSaidaVO.setInformacaoFiliacaoApresentarDanfe(notaFiscalSaidaVO.getInformacaoFiliacaoApresentarDanfe() + " , " + nomePessoa);
		} else {
			notaFiscalSaidaVO.setInformacaoFiliacaoApresentarDanfe("Aluno(s): " + nomePessoa);
		}
		for (NotaFiscalSaidaServicoVO nfss : notaFiscalSaidaVO.getNotaFiscalSaidaServicoVOs()) {
			if (nfss.getContaReceberVO().getCodigo().equals(contaReceberVO.getCodigo())) {
				nfss.setPrecoUnitario(nfss.getPrecoUnitario() + contaReceberVO.getValor());
				nfss.setAliquotaPIS(configuracaoNotaFiscalVO.getPis());
				nfss.setAliquotaCOFINS(configuracaoNotaFiscalVO.getCofins());
				nfss.setTotalIssqn(new Double(Uteis.getValorTruncadoDeDoubleParaString((nfss.getBaseIssqn() * nfss.getPrecoUnitario()), 2)));
				nfss.setTotalPIS(new Double(Uteis.getValorTruncadoDeDoubleParaString((nfss.getPrecoUnitario() * (nfss.getAliquotaPIS() / 100)), 2)));
				nfss.setTotalCOFINS(new Double(Uteis.getValorTruncadoDeDoubleParaString((nfss.getPrecoUnitario() * (nfss.getAliquotaCOFINS() / 100)), 2)));
				nfss.setTotalINSS(new Double(Uteis.getValorTruncadoDeDoubleParaString((nfss.getPrecoUnitario() * configuracaoNotaFiscalVO.getInss()), 2)));
				nfss.setTotalCSLL(new Double(Uteis.getValorTruncadoDeDoubleParaString((nfss.getPrecoUnitario() * configuracaoNotaFiscalVO.getCsll()), 2)));
				nfss.setTotalIRRF(new Double(Uteis.getValorTruncadoDeDoubleParaString((nfss.getPrecoUnitario() * configuracaoNotaFiscalVO.getAliquotaIR()), 2)));
				nfss.getListaCodigoContaReceber().add(contaReceberVO);
				return;
			}
		}
		notaFiscalSaidaVO.getNotaFiscalSaidaServicoVOs().add(montarDadosNotaFiscalSaidaServico(contaReceberVO, configuracaoNotaFiscalVO));
	}

	@Override
	public void montarDadosGeracaoNotaFiscalSaida(List<NotaFiscalSaidaVO> notaFiscalSaidaVOs, List<ContaReceberRecebimentoVO> contaReceberRecebimentoVOs, List<ContaReceberVO> contasReceberVOs, Integer unidadeEnsino, List<NotaFiscalSaidaVO> notaFiscalSaidaEnviadasVOs, UsuarioVO usuarioVO) throws Exception {
		notaFiscalSaidaVOs.clear();
		HashMap<String, NotaFiscalSaidaVO> hashMapNFS = new HashMap<String, NotaFiscalSaidaVO>(0);
		if (contaReceberRecebimentoVOs.size() > 0) {
			montarDadosGeracaoNotaFiscalSaidaContasRecebidas(hashMapNFS, notaFiscalSaidaVOs, contaReceberRecebimentoVOs, unidadeEnsino, notaFiscalSaidaEnviadasVOs, usuarioVO);
		}
		if (contasReceberVOs.size() > 0) {
			montarDadosGeracaoNotaFiscalSaidaContasAReceber(hashMapNFS, notaFiscalSaidaVOs, contasReceberVOs, unidadeEnsino, notaFiscalSaidaEnviadasVOs, usuarioVO);
		}
		notaFiscalSaidaVOs.sort((a, b) ->  Double.compare(a.getValorTotal(), b.getValorTotal()));
	}

	public void montarDadosGeracaoNotaFiscalSaidaContasRecebidas(HashMap<String, NotaFiscalSaidaVO> hashMapNFS, List<NotaFiscalSaidaVO> notaFiscalSaidaVOs, List<ContaReceberRecebimentoVO> contaReceberRecebimentoVOs, Integer unidadeEnsino, List<NotaFiscalSaidaVO> notaFiscalSaidaEnviadasVOs, UsuarioVO usuarioVO) throws Exception {
		NotaFiscalSaidaVO notaFiscalSaidaVO = new NotaFiscalSaidaVO();
		Double valorTotal;
		Double valorImpostoCalc;
		Double valorTotalPIS;
		Double valorTotalCOFINS;
		Double valorTotalINSS;
		Double valorTotalIRRF;
		Double valorTotalCSLL;
		int codigoSacado = 0;
		String chaveNotaFiscal = "";
		ConfiguracaoNotaFiscalVO configuracaoNotaFiscalVO = getFacadeFactory().getConfiguracaoNotaFiscalFacade().consultarPorUnidadeEnsino(unidadeEnsino, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
		Iterator<ContaReceberRecebimentoVO> iteratorNota = contaReceberRecebimentoVOs.iterator();
		while (iteratorNota.hasNext()) {
			ContaReceberRecebimentoVO obj = (ContaReceberRecebimentoVO) iteratorNota.next();
			if (obj.getContaReceberVO().getTipoPessoa().equals(TipoPessoa.PARCEIRO.getValor())) {
				if (Uteis.isAtributoPreenchido(obj.getContaReceberVO().getResponsavelFinanceiro().getCodigo())) {
					codigoSacado = obj.getContaReceberVO().getResponsavelFinanceiro().getCodigo();
				} else if (Uteis.isAtributoPreenchido(obj.getContaReceberVO().getPessoa().getCodigo())) {
					codigoSacado = obj.getContaReceberVO().getPessoa().getCodigo();
				} else {
					codigoSacado = obj.getContaReceberVO().getParceiroVO().getCodigo();
				}
			} else if (obj.getContaReceberVO().getTipoPessoa().equals(TipoPessoa.FORNECEDOR.getValor())) {
				codigoSacado = obj.getContaReceberVO().getFornecedor().getCodigo();
			} else if (obj.getContaReceberVO().getTipoPessoa().equals(TipoPessoa.FUNCIONARIO.getValor())) {
				codigoSacado = obj.getContaReceberVO().getFuncionario().getCodigo();
			} else if (obj.getContaReceberVO().getTipoPessoa().equals(TipoPessoa.RESPONSAVEL_FINANCEIRO.getValor()) && configuracaoNotaFiscalVO.isAgruparNotaFicalPorResponsavelFinanceiro()) {
				codigoSacado = obj.getContaReceberVO().getResponsavelFinanceiro().getCodigo();
			} else {
				codigoSacado = obj.getContaReceberVO().getPessoa().getCodigo();
			}
			chaveNotaFiscal = codigoSacado + "," + obj.getNotaFiscalSaidaServicoVO().getNotaFiscalSaida().getCodigo();
			if (Uteis.isAtributoPreenchido(obj.getNotaFiscalSaidaServicoVO().getNotaFiscalSaida().getCodigo()) && !hashMapNFS.containsKey(chaveNotaFiscal)) {
				NotaFiscalSaidaVO notaFiscalSaidaVOExistente = new NotaFiscalSaidaVO();
				notaFiscalSaidaVOExistente = getFacadeFactory().getNotaFiscalSaidaFacade().consultarPorChavePrimaria(obj.getNotaFiscalSaidaServicoVO().getNotaFiscalSaida().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
				for (NotaFiscalSaidaServicoVO servico : notaFiscalSaidaVOExistente.getNotaFiscalSaidaServicoVOs()) {
					if (servico.getCodigo().intValue() == obj.getNotaFiscalSaidaServicoVO().getCodigo().intValue()) {
						servico.setContaReceberVO(obj.getContaReceberVO());
						servico.getContaReceberRecebimentoVO().setContaReceberVO(obj.getContaReceberVO());
					}
				}
				hashMapNFS.put(chaveNotaFiscal, notaFiscalSaidaVOExistente);
				notaFiscalSaidaVOExistente.setNovoObj(false);
				if (notaFiscalSaidaVOExistente.getSituacao().equals(SituacaoNotaFiscalSaidaEnum.ENVIADA.getValor())) {
					notaFiscalSaidaEnviadasVOs.add(notaFiscalSaidaVOExistente);
				} else {
					notaFiscalSaidaVOs.add(notaFiscalSaidaVOExistente);
				}
			} else {
				
				if (hashMapNFS.containsKey(chaveNotaFiscal)) {
					for (NotaFiscalSaidaServicoVO servico : hashMapNFS.get(chaveNotaFiscal).getNotaFiscalSaidaServicoVOs()) {
						if (servico.getCodigo().intValue() == obj.getNotaFiscalSaidaServicoVO().getCodigo().intValue()) {
							servico.setContaReceberVO(obj.getContaReceberVO());
							servico.getContaReceberRecebimentoVO().setContaReceberVO(obj.getContaReceberVO());
						}
					}
				}
			
				if (hashMapNFS.containsKey(chaveNotaFiscal) && !Uteis.isAtributoPreenchido(obj.getNotaFiscalSaidaServicoVO().getNotaFiscalSaida().getCodigo()) ){
					adicionarNotaFiscalServico(hashMapNFS.get(chaveNotaFiscal), obj, configuracaoNotaFiscalVO);
					// hashMapNFS.get(notaFiscalSaidaVO.getCodigoSacado()).getNotaFiscalSaidaServicoVOs().add(montarDadosNotaFiscalSaidaServico(obj,
					// configuracaoNotaFiscalVO));
					valorTotal = 0.0;
					valorImpostoCalc = 0.0;
					valorTotalPIS = 0.0;
					valorTotalCOFINS = 0.0;
					valorTotalINSS = 0.0;
					valorTotalIRRF = 0.0;
					valorTotalCSLL = 0.0;
					Iterator<NotaFiscalSaidaServicoVO> iteratorServico = hashMapNFS.get(chaveNotaFiscal).getNotaFiscalSaidaServicoVOs().iterator();
					while (iteratorServico.hasNext()) {
						NotaFiscalSaidaServicoVO nfss = (NotaFiscalSaidaServicoVO) iteratorServico.next();
						valorTotal += new Double(Uteis.arrendondarForcando2CadasDecimais(nfss.getPrecoUnitario()));
						valorImpostoCalc += new Double(Uteis.arrendondarForcando2CadasDecimais(nfss.getTotalIssqn()));
						valorTotalPIS += new Double(Uteis.arrendondarForcando2CadasDecimais(nfss.getTotalPIS()));
						valorTotalCOFINS += new Double(Uteis.arrendondarForcando2CadasDecimais(nfss.getTotalCOFINS()));
						valorTotalINSS += new Double(Uteis.arrendondarForcando2CadasDecimais(nfss.getTotalINSS()));
						valorTotalIRRF += new Double(Uteis.arrendondarForcando2CadasDecimais(nfss.getTotalIRRF()));
						valorTotalCSLL += new Double(Uteis.arrendondarForcando2CadasDecimais(nfss.getTotalCSLL()));
					}
	
					hashMapNFS.get(chaveNotaFiscal).setValorTotal(valorTotal);
					hashMapNFS.get(chaveNotaFiscal).setTotalIssqn(valorImpostoCalc);
					hashMapNFS.get(chaveNotaFiscal).setValorTotalPIS(valorTotalPIS);
					hashMapNFS.get(chaveNotaFiscal).setValorTotalCOFINS(valorTotalCOFINS);
					hashMapNFS.get(chaveNotaFiscal).setTotalBaseIssqn(configuracaoNotaFiscalVO.getIssqn());
					hashMapNFS.get(chaveNotaFiscal).setTotalAliquotaIssqn(configuracaoNotaFiscalVO.getIssqn());
					hashMapNFS.get(chaveNotaFiscal).setValorTotalINSS(valorTotalINSS);
					hashMapNFS.get(chaveNotaFiscal).setAliquotaInss(configuracaoNotaFiscalVO.getInss());
					hashMapNFS.get(chaveNotaFiscal).setValorTotalIRRF(valorTotalIRRF);
					hashMapNFS.get(chaveNotaFiscal).setAliquotaIr(configuracaoNotaFiscalVO.getAliquotaIR());
					hashMapNFS.get(chaveNotaFiscal).setValorTotalCSLL(valorTotalCSLL);
					hashMapNFS.get(chaveNotaFiscal).setAliquotaCsll(configuracaoNotaFiscalVO.getCsll());
					if (configuracaoNotaFiscalVO.getCodigoRegimeTributarioEnum().equals(CodigoRegimeTributarioEnum.SIMPLES_NACIONAL)) {
						hashMapNFS.get(chaveNotaFiscal).setValorLiquido(new Double(Uteis.getValorTruncadoDeDoubleParaString((valorTotal - valorTotalPIS - valorTotalCOFINS - valorTotalINSS - valorTotalIRRF - valorTotalCSLL - valorImpostoCalc), 2)));
					} else {
						hashMapNFS.get(chaveNotaFiscal).setValorLiquido(new Double(Uteis.getValorTruncadoDeDoubleParaString((valorTotal - valorTotalPIS - valorTotalCOFINS - valorTotalINSS - valorTotalIRRF - valorTotalCSLL), 2)));
					}
					calcularImpostos(hashMapNFS.get(chaveNotaFiscal), configuracaoNotaFiscalVO);
				} else if (!Uteis.isAtributoPreenchido(obj.getNotaFiscalSaidaServicoVO().getNotaFiscalSaida().getCodigo())) {
					notaFiscalSaidaVO = new NotaFiscalSaidaVO();
					notaFiscalSaidaVO.setCodigoSacado(codigoSacado);
					notaFiscalSaidaVO.setMatricula(obj.getContaReceberVO().getMatriculaAluno().getMatricula());
					if (obj.getContaReceberVO().getTipoPessoa().equals(TipoPessoa.PARCEIRO.getValor())
					        && obj.getContaReceberVO().getParceiroVO().getEmitirNotaFiscalParaBeneficiario()) {
						if (Uteis.isAtributoPreenchido(obj.getContaReceberVO().getResponsavelFinanceiro().getCodigo())) {
							notaFiscalSaidaVO.setTipoPessoa(TipoPessoa.RESPONSAVEL_FINANCEIRO.getValor());
						} else if (Uteis.isAtributoPreenchido(obj.getContaReceberVO().getPessoa().getCodigo())) {
							notaFiscalSaidaVO.setTipoPessoa(TipoPessoa.ALUNO.getValor());
						} else {
							notaFiscalSaidaVO.setTipoPessoa(TipoPessoa.PARCEIRO.getValor());
						}
					} else {
						notaFiscalSaidaVO.setTipoPessoa(obj.getContaReceberVO().getTipoPessoa());
					}
					if (notaFiscalSaidaVO.getTipoPessoa().equals(TipoPessoa.PARCEIRO.getValor())) {
						if (!notaFiscalSaidaVO.getMatricula().isEmpty()) {
							notaFiscalSaidaVO.setPessoaVO(getFacadeFactory().getPessoaFacade().consultarAlunoPorMatricula(notaFiscalSaidaVO.getMatricula(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO));
						}
						notaFiscalSaidaVO.setParceiroVO(getFacadeFactory().getParceiroFacade().consultarPorChavePrimaria(obj.getContaReceberVO().getParceiroVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
						notaFiscalSaidaVO.setSacado(notaFiscalSaidaVO.getParceiroVO().getNome());
						notaFiscalSaidaVO.setEndereco(notaFiscalSaidaVO.getParceiroVO().getEndereco() + " " + notaFiscalSaidaVO.getParceiroVO().getNumero() + " " + notaFiscalSaidaVO.getParceiroVO().getComplemento());
						notaFiscalSaidaVO.setNomeRazaoSocial(notaFiscalSaidaVO.getParceiroVO().getNome());
						if (notaFiscalSaidaVO.getParceiroVO().getTipoEmpresa().equals("JU")) {
							notaFiscalSaidaVO.setCnpjCpf(notaFiscalSaidaVO.getParceiroVO().getCNPJ());
						} else {
							notaFiscalSaidaVO.setCnpjCpf(notaFiscalSaidaVO.getParceiroVO().getCPF());
						}
						notaFiscalSaidaVO.setCep(notaFiscalSaidaVO.getParceiroVO().getCEP());
						if (notaFiscalSaidaVO.getParceiroVO().getTipoEmpresa().equals("JU")) {
							notaFiscalSaidaVO.setInscricaoEstadual(notaFiscalSaidaVO.getInscricaoEstadual());
						} else {
							notaFiscalSaidaVO.setInscricaoEstadual("");
						}
						notaFiscalSaidaVO.setUf(notaFiscalSaidaVO.getParceiroVO().getCidade().getEstado().getSigla());
						notaFiscalSaidaVO.setTelefone(notaFiscalSaidaVO.getParceiroVO().getTelComercial1());
						notaFiscalSaidaVO.setMunicipio(notaFiscalSaidaVO.getParceiroVO().getCidade().getNome());
	
					} else if (notaFiscalSaidaVO.getTipoPessoa().equals(TipoPessoa.FORNECEDOR.getValor())) {
						notaFiscalSaidaVO.setFornecedorVO(getFacadeFactory().getFornecedorFacade().consultarPorChavePrimaria(obj.getContaReceberVO().getFornecedor().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));
						notaFiscalSaidaVO.setSacado(notaFiscalSaidaVO.getFornecedorVO().getNome());
						notaFiscalSaidaVO.setEndereco(notaFiscalSaidaVO.getFornecedorVO().getEndereco() + " " + notaFiscalSaidaVO.getFornecedorVO().getNumero() + " " + notaFiscalSaidaVO.getFornecedorVO().getComplemento());
						notaFiscalSaidaVO.setNomeRazaoSocial(notaFiscalSaidaVO.getFornecedorVO().getNome());
						if (notaFiscalSaidaVO.getFornecedorVO().getTipoEmpresa().equals("JU")) {
							notaFiscalSaidaVO.setCnpjCpf(notaFiscalSaidaVO.getFornecedorVO().getCNPJ());
						} else {
							notaFiscalSaidaVO.setCnpjCpf(notaFiscalSaidaVO.getFornecedorVO().getCPF());
						}
						notaFiscalSaidaVO.setCep(notaFiscalSaidaVO.getFornecedorVO().getCEP());
						if (notaFiscalSaidaVO.getFornecedorVO().getTipoEmpresa().equals("JU")) {
							notaFiscalSaidaVO.setInscricaoEstadual(notaFiscalSaidaVO.getFornecedorVO().getInscEstadual());
						} else {
							notaFiscalSaidaVO.setInscricaoEstadual("");
						}
						notaFiscalSaidaVO.setUf(notaFiscalSaidaVO.getFornecedorVO().getCidade().getEstado().getSigla());
						notaFiscalSaidaVO.setTelefone(notaFiscalSaidaVO.getFornecedorVO().getTelComercial1());
						notaFiscalSaidaVO.setMunicipio(notaFiscalSaidaVO.getFornecedorVO().getCidade().getNome());
	
					} else if (notaFiscalSaidaVO.getTipoPessoa().equals(TipoPessoa.FUNCIONARIO.getValor())) {
						notaFiscalSaidaVO.setFuncionarioVO(getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimaria(obj.getContaReceberVO().getFuncionario().getCodigo(), 0, false, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));
						notaFiscalSaidaVO.setSacado(notaFiscalSaidaVO.getFuncionarioVO().getPessoa().getNome());
						notaFiscalSaidaVO.setEndereco(notaFiscalSaidaVO.getPessoaVO().getEndereco() + " " + notaFiscalSaidaVO.getPessoaVO().getNumero() + " " + notaFiscalSaidaVO.getPessoaVO().getComplemento());
						notaFiscalSaidaVO.setNomeRazaoSocial(notaFiscalSaidaVO.getFuncionarioVO().getPessoa().getNome());
						notaFiscalSaidaVO.setCnpjCpf(notaFiscalSaidaVO.getFuncionarioVO().getPessoa().getCPF());
						notaFiscalSaidaVO.setCep(notaFiscalSaidaVO.getFuncionarioVO().getPessoa().getCEP());
						notaFiscalSaidaVO.setInscricaoEstadual("");
						notaFiscalSaidaVO.setUf(notaFiscalSaidaVO.getFuncionarioVO().getPessoa().getCidade().getEstado().getSigla());
						notaFiscalSaidaVO.setTelefone(notaFiscalSaidaVO.getFuncionarioVO().getPessoa().getTelefoneRes());
						notaFiscalSaidaVO.setMunicipio(notaFiscalSaidaVO.getFuncionarioVO().getPessoa().getCidade().getNome());
	
					} else if (notaFiscalSaidaVO.getTipoPessoa().equals(TipoPessoa.RESPONSAVEL_FINANCEIRO.getValor())) {
						notaFiscalSaidaVO.setPessoaVO(getFacadeFactory().getPessoaFacade().consultarDadosGerarNotaFiscalSaida(obj.getContaReceberVO().getPessoa().getCodigo(), false, usuarioVO));
						notaFiscalSaidaVO.setResponsavelFinanceiro(getFacadeFactory().getPessoaFacade().consultarDadosGerarNotaFiscalSaida(obj.getContaReceberVO().getResponsavelFinanceiro().getCodigo(), false, usuarioVO));
						notaFiscalSaidaVO.setSacado(notaFiscalSaidaVO.getResponsavelFinanceiro().getNome());
						notaFiscalSaidaVO.setEndereco(notaFiscalSaidaVO.getResponsavelFinanceiro().getEndereco() + " " + notaFiscalSaidaVO.getResponsavelFinanceiro().getNumero() + " " + notaFiscalSaidaVO.getResponsavelFinanceiro().getComplemento());
						notaFiscalSaidaVO.setNomeRazaoSocial(notaFiscalSaidaVO.getResponsavelFinanceiro().getNome());
						notaFiscalSaidaVO.setCnpjCpf(notaFiscalSaidaVO.getResponsavelFinanceiro().getCPF());
						notaFiscalSaidaVO.setCep(notaFiscalSaidaVO.getResponsavelFinanceiro().getCEP());
						notaFiscalSaidaVO.setUf(notaFiscalSaidaVO.getResponsavelFinanceiro().getCidade().getEstado().getSigla());
						notaFiscalSaidaVO.setTelefone(notaFiscalSaidaVO.getResponsavelFinanceiro().getTelefoneComer());
						notaFiscalSaidaVO.setMunicipio(notaFiscalSaidaVO.getResponsavelFinanceiro().getCidade().getNome());
	
					} else {
						notaFiscalSaidaVO.setPessoaVO(getFacadeFactory().getPessoaFacade().consultarDadosGerarNotaFiscalSaida(obj.getContaReceberVO().getPessoa().getCodigo(), false, usuarioVO));
						notaFiscalSaidaVO.setSacado(notaFiscalSaidaVO.getPessoaVO().getNome());
						notaFiscalSaidaVO.setEndereco(notaFiscalSaidaVO.getPessoaVO().getEndereco() + " " + notaFiscalSaidaVO.getPessoaVO().getNumero() + " " + notaFiscalSaidaVO.getPessoaVO().getComplemento());
						notaFiscalSaidaVO.setNomeRazaoSocial(notaFiscalSaidaVO.getPessoaVO().getNome());
						notaFiscalSaidaVO.setCnpjCpf(notaFiscalSaidaVO.getPessoaVO().getCPF());
						notaFiscalSaidaVO.setCep(notaFiscalSaidaVO.getPessoaVO().getCEP());
						notaFiscalSaidaVO.setUf(notaFiscalSaidaVO.getPessoaVO().getCidade().getEstado().getSigla());
						notaFiscalSaidaVO.setTelefone(notaFiscalSaidaVO.getPessoaVO().getTelefoneComer());
						notaFiscalSaidaVO.setMunicipio(notaFiscalSaidaVO.getPessoaVO().getCidade().getNome());
	
					}
					adicionarNotaFiscalServico(notaFiscalSaidaVO, obj, configuracaoNotaFiscalVO);
					// notaFiscalSaidaVO.getNotaFiscalSaidaServicoVOs().add(montarDadosNotaFiscalSaidaServico(obj,
					// configuracaoNotaFiscalVO));
					notaFiscalSaidaVO.setValorTotal(new Double(Uteis.getValorTruncadoDeDoubleParaString(notaFiscalSaidaVO.getNotaFiscalSaidaServicoVOs().get(0).getPrecoUnitario(), 2)));
					notaFiscalSaidaVO.setTotalIssqn(notaFiscalSaidaVO.getNotaFiscalSaidaServicoVOs().get(0).getPrecoUnitario() * (configuracaoNotaFiscalVO.getIssqn() / 100));
					notaFiscalSaidaVO.setValorTotalPIS(notaFiscalSaidaVO.getNotaFiscalSaidaServicoVOs().get(0).getPrecoUnitario() * (configuracaoNotaFiscalVO.getPis() / 100));
					notaFiscalSaidaVO.setValorTotalCOFINS(notaFiscalSaidaVO.getNotaFiscalSaidaServicoVOs().get(0).getPrecoUnitario() * (configuracaoNotaFiscalVO.getCofins() / 100));
					if (!notaFiscalSaidaVO.getSituacao().equals(SituacaoNotaFiscalSaidaEnum.ENVIADA.getValor())) {
						calcularImpostos(notaFiscalSaidaVO, configuracaoNotaFiscalVO);
					}
					notaFiscalSaidaVO.setCodigo(obj.getNotaFiscalSaidaServicoVO().getNotaFiscalSaida().getCodigo());
					notaFiscalSaidaVO.setNumero(obj.getNotaFiscalSaidaServicoVO().getNotaFiscalSaida().getNumero());
					notaFiscalSaidaVO.setNumeroRPS(obj.getNotaFiscalSaidaServicoVO().getNotaFiscalSaida().getNumeroRPS());
					notaFiscalSaidaVO.setLote(obj.getNotaFiscalSaidaServicoVO().getNotaFiscalSaida().getLote());
					notaFiscalSaidaVO.setSituacao(obj.getNotaFiscalSaidaServicoVO().getNotaFiscalSaida().getSituacao());
					notaFiscalSaidaVO.setCodigoNaturezaOperacao(configuracaoNotaFiscalVO.getCodigoNaturezaOperacao());
					notaFiscalSaidaVO.setNomeNaturezaOperacao(configuracaoNotaFiscalVO.getNomeNaturezaOperacao());
					notaFiscalSaidaVO.setNaturezaOperacaoEnum(configuracaoNotaFiscalVO.getNaturezaOperacaoEnum());
					notaFiscalSaidaVO.setIsIncentivadorCultural(configuracaoNotaFiscalVO.getIsIncentivadorCultural());
					notaFiscalSaidaVO.setRegimeEspecialTributacaoEnum(configuracaoNotaFiscalVO.getRegimeEspecialTributacaoEnum());
					notaFiscalSaidaVO.setCodigoCNAE(configuracaoNotaFiscalVO.getCodigoCNAE());
					notaFiscalSaidaVO.setPercentualCargaTributaria(configuracaoNotaFiscalVO.getPercentualCargaTributaria());
					notaFiscalSaidaVO.setFonteCargaTributaria(configuracaoNotaFiscalVO.getFonteCargaTributaria());
					notaFiscalSaidaVO.setMensagemRetorno(obj.getNotaFiscalSaidaServicoVO().getNotaFiscalSaida().getMensagemRetorno());
					notaFiscalSaidaVO.setModelo("55");
					if (AmbienteNfeEnum.PRODUCAO.equals(configuracaoNotaFiscalVO.getAmbienteNfeEnum())) {
						notaFiscalSaidaVO.setSerie(configuracaoNotaFiscalVO.getSerie());
					} else {
						notaFiscalSaidaVO.setSerie(configuracaoNotaFiscalVO.getSerieHomologacao());
					}
					notaFiscalSaidaVO.getUnidadeEnsinoVO().setConfiguracaoNotaFiscalVO(getFacadeFactory().getConfiguracaoNotaFiscalFacade().consultarPorUnidadeEnsino(unidadeEnsino, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
					hashMapNFS.put(chaveNotaFiscal, notaFiscalSaidaVO);
					notaFiscalSaidaVO.setNovoObj(false);
					if (notaFiscalSaidaVO.getSituacao().equals(SituacaoNotaFiscalSaidaEnum.ENVIADA.getValor())) {
						notaFiscalSaidaEnviadasVOs.add(notaFiscalSaidaVO);
					} else {
						notaFiscalSaidaVOs.add(notaFiscalSaidaVO);
					}
				}
			}
		}
		Ordenacao.ordenarLista(notaFiscalSaidaVOs, "sacado");
		Ordenacao.ordenarLista(notaFiscalSaidaEnviadasVOs, "sacado");
	}

	public void montarDadosGeracaoNotaFiscalSaidaContasAReceber(HashMap<String, NotaFiscalSaidaVO> hashMapNFS, List<NotaFiscalSaidaVO> notaFiscalSaidaVOs, List<ContaReceberVO> contasReceberVOs, Integer unidadeEnsino, List<NotaFiscalSaidaVO> notaFiscalSaidaEnviadasVOs, UsuarioVO usuarioVO) throws Exception {
		NotaFiscalSaidaVO notaFiscalSaidaVO = new NotaFiscalSaidaVO();
		Double valorTotal;
		Double valorImpostoCalc;
		Double valorTotalPIS;
		Double valorTotalCOFINS;
		Double valorTotalINSS;
		Double valorTotalIRRF;
		Double valorTotalCSLL;
		int codigoSacado = 0;
		String chaveNotaFiscal = "";
		ConfiguracaoNotaFiscalVO configuracaoNotaFiscalVO = getFacadeFactory().getConfiguracaoNotaFiscalFacade().consultarPorUnidadeEnsino(unidadeEnsino, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
		
		
		Iterator<ContaReceberVO> iteratorNota = contasReceberVOs.iterator();
		while (iteratorNota.hasNext()) {
			ContaReceberVO obj = (ContaReceberVO) iteratorNota.next();

			if (obj.getTipoPessoa().equals(TipoPessoa.PARCEIRO.getValor())) {
				if (Uteis.isAtributoPreenchido(obj.getResponsavelFinanceiro().getCodigo())) {
					codigoSacado = obj.getResponsavelFinanceiro().getCodigo();
				} else if (Uteis.isAtributoPreenchido(obj.getPessoa().getCodigo())) {
					codigoSacado = obj.getPessoa().getCodigo();
				} else {
					codigoSacado = obj.getParceiroVO().getCodigo();
				}
			} else if (obj.getTipoPessoa().equals(TipoPessoa.FORNECEDOR.getValor())) {
				codigoSacado = obj.getFornecedor().getCodigo();
			} else if (obj.getTipoPessoa().equals(TipoPessoa.FUNCIONARIO.getValor())) {
				codigoSacado = obj.getFuncionario().getCodigo();
			} else if (obj.getTipoPessoa().equals(TipoPessoa.RESPONSAVEL_FINANCEIRO.getValor()) && configuracaoNotaFiscalVO.isAgruparNotaFicalPorResponsavelFinanceiro()) {
				codigoSacado = obj.getResponsavelFinanceiro().getCodigo();
			} else {
				codigoSacado = obj.getPessoa().getCodigo();
			}
			
			chaveNotaFiscal = codigoSacado + "," + obj.getNotaFiscalSaidaServicoVO().getNotaFiscalSaida().getCodigo();
			if (Uteis.isAtributoPreenchido(obj.getNotaFiscalSaidaServicoVO().getNotaFiscalSaida().getCodigo()) && !hashMapNFS.containsKey(chaveNotaFiscal)) {
				NotaFiscalSaidaVO notaFiscalSaidaVOExistente = new NotaFiscalSaidaVO();
				notaFiscalSaidaVOExistente = getFacadeFactory().getNotaFiscalSaidaFacade().consultarPorChavePrimaria(obj.getNotaFiscalSaidaServicoVO().getNotaFiscalSaida().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
				for (NotaFiscalSaidaServicoVO servico : notaFiscalSaidaVOExistente.getNotaFiscalSaidaServicoVOs()) {
					if (servico.getCodigo().intValue() == obj.getNotaFiscalSaidaServicoVO().getCodigo().intValue()) {
						servico.setContaReceberVO(obj);
						servico.getContaReceberRecebimentoVO().setContaReceberVO(obj);
					}
				}
				hashMapNFS.put(chaveNotaFiscal, notaFiscalSaidaVOExistente);
				notaFiscalSaidaVOExistente.setNovoObj(false);
				if (notaFiscalSaidaVOExistente.getSituacao().equals(SituacaoNotaFiscalSaidaEnum.ENVIADA.getValor())) {
					notaFiscalSaidaEnviadasVOs.add(notaFiscalSaidaVOExistente);
				} else {
					notaFiscalSaidaVOs.add(notaFiscalSaidaVOExistente);
				}
			} else {
			if (hashMapNFS.containsKey(chaveNotaFiscal) && !Uteis.isAtributoPreenchido(obj.getNotaFiscalSaidaServicoVO().getNotaFiscalSaida().getCodigo())) {
				adicionarNotaFiscalServico(hashMapNFS.get(chaveNotaFiscal), obj, configuracaoNotaFiscalVO);
				valorTotal = 0.0;
				valorImpostoCalc = 0.0;
				valorTotalPIS = 0.0;
				valorTotalCOFINS = 0.0;
				valorTotalINSS = 0.0;
				valorTotalIRRF = 0.0;
				valorTotalCSLL = 0.0;
				Iterator<NotaFiscalSaidaServicoVO> iteratorServico = hashMapNFS.get(chaveNotaFiscal).getNotaFiscalSaidaServicoVOs().iterator();
				while (iteratorServico.hasNext()) {
					NotaFiscalSaidaServicoVO nfss = (NotaFiscalSaidaServicoVO) iteratorServico.next();
					valorTotal += new Double(Uteis.getValorTruncadoDeDoubleParaString(nfss.getPrecoUnitario(), 2));
					valorImpostoCalc += new Double(Uteis.getValorTruncadoDeDoubleParaString(nfss.getTotalIssqn(), 2));
					valorTotalPIS += new Double(Uteis.getValorTruncadoDeDoubleParaString(nfss.getTotalPIS(), 2));
					valorTotalCOFINS += new Double(Uteis.getValorTruncadoDeDoubleParaString(nfss.getTotalCOFINS(), 2));
					valorTotalINSS += new Double(Uteis.getValorTruncadoDeDoubleParaString(nfss.getTotalINSS(), 2));
					valorTotalIRRF += new Double(Uteis.getValorTruncadoDeDoubleParaString(nfss.getTotalIRRF(), 2));
					valorTotalCSLL += new Double(Uteis.getValorTruncadoDeDoubleParaString(nfss.getTotalCSLL(), 2));
				}
				hashMapNFS.get(chaveNotaFiscal).setValorTotal(new Double(Uteis.getValorTruncadoDeDoubleParaString((valorTotal), 2)));
				hashMapNFS.get(chaveNotaFiscal).setTotalIssqn(new Double(Uteis.getValorTruncadoDeDoubleParaString((valorImpostoCalc), 2)));
				hashMapNFS.get(chaveNotaFiscal).setValorTotalPIS(new Double(Uteis.getValorTruncadoDeDoubleParaString((valorTotalPIS), 2)));
				hashMapNFS.get(chaveNotaFiscal).setValorTotalCOFINS(new Double(Uteis.getValorTruncadoDeDoubleParaString((valorTotalCOFINS), 2)));
				hashMapNFS.get(chaveNotaFiscal).setTotalBaseIssqn(configuracaoNotaFiscalVO.getIssqn());
				hashMapNFS.get(chaveNotaFiscal).setTotalAliquotaIssqn(configuracaoNotaFiscalVO.getIssqn());
				hashMapNFS.get(chaveNotaFiscal).setValorTotalINSS(new Double(Uteis.getValorTruncadoDeDoubleParaString((valorTotalINSS), 2)));
				hashMapNFS.get(chaveNotaFiscal).setAliquotaInss(configuracaoNotaFiscalVO.getInss());
				hashMapNFS.get(chaveNotaFiscal).setValorTotalIRRF(new Double(Uteis.getValorTruncadoDeDoubleParaString((valorTotalIRRF), 2)));
				hashMapNFS.get(chaveNotaFiscal).setAliquotaIr(configuracaoNotaFiscalVO.getAliquotaIR());
				hashMapNFS.get(chaveNotaFiscal).setValorTotalCSLL(new Double(Uteis.getValorTruncadoDeDoubleParaString((valorTotalCSLL), 2)));
				hashMapNFS.get(chaveNotaFiscal).setAliquotaCsll(configuracaoNotaFiscalVO.getCsll());
				if (configuracaoNotaFiscalVO.getCodigoRegimeTributarioEnum().equals(CodigoRegimeTributarioEnum.SIMPLES_NACIONAL)) {
					hashMapNFS.get(chaveNotaFiscal).setValorLiquido(new Double(Uteis.getValorTruncadoDeDoubleParaString((valorTotal - valorTotalPIS - valorTotalCOFINS - valorTotalINSS - valorTotalIRRF - valorTotalCSLL - valorImpostoCalc), 2)));
				} else {
					hashMapNFS.get(chaveNotaFiscal).setValorLiquido(new Double(Uteis.getValorTruncadoDeDoubleParaString((valorTotal - valorTotalPIS - valorTotalCOFINS - valorTotalINSS - valorTotalIRRF - valorTotalCSLL), 2)));
				}
				calcularImpostos(hashMapNFS.get(chaveNotaFiscal), configuracaoNotaFiscalVO);
			} else if (!Uteis.isAtributoPreenchido(obj.getNotaFiscalSaidaServicoVO().getNotaFiscalSaida().getCodigo())) {
				notaFiscalSaidaVO = new NotaFiscalSaidaVO();
				notaFiscalSaidaVO.setCodigoSacado(codigoSacado);
				notaFiscalSaidaVO.setMatricula(obj.getMatriculaAluno().getMatricula());
				if (obj.getTipoPessoa().equals(TipoPessoa.PARCEIRO.getValor()) && obj.getParceiroVO().getEmitirNotaFiscalParaBeneficiario()) {
					if (Uteis.isAtributoPreenchido(obj.getResponsavelFinanceiro().getCodigo())) {
						notaFiscalSaidaVO.setTipoPessoa(TipoPessoa.RESPONSAVEL_FINANCEIRO.getValor());
					} else if (Uteis.isAtributoPreenchido(obj.getPessoa().getCodigo())) {
						notaFiscalSaidaVO.setTipoPessoa(TipoPessoa.ALUNO.getValor());
					} else {
						notaFiscalSaidaVO.setTipoPessoa(TipoPessoa.PARCEIRO.getValor());
					}
				} else {
					notaFiscalSaidaVO.setTipoPessoa(obj.getTipoPessoa());
				}
				if (notaFiscalSaidaVO.getTipoPessoa().equals(TipoPessoa.PARCEIRO.getValor())) {
					if (!notaFiscalSaidaVO.getMatricula().isEmpty()) {
						notaFiscalSaidaVO.setPessoaVO(getFacadeFactory().getPessoaFacade().consultarAlunoPorMatricula(notaFiscalSaidaVO.getMatricula(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO));
					}
					notaFiscalSaidaVO.setParceiroVO(getFacadeFactory().getParceiroFacade().consultarPorChavePrimaria(obj.getParceiroVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
					notaFiscalSaidaVO.setSacado(notaFiscalSaidaVO.getParceiroVO().getNome());
					notaFiscalSaidaVO.setEndereco(notaFiscalSaidaVO.getParceiroVO().getEndereco() + " " + notaFiscalSaidaVO.getParceiroVO().getNumero() + " " + notaFiscalSaidaVO.getParceiroVO().getComplemento());
					notaFiscalSaidaVO.setNomeRazaoSocial(notaFiscalSaidaVO.getParceiroVO().getNome());
					if (notaFiscalSaidaVO.getParceiroVO().getTipoEmpresa().equals("JU")) {
						notaFiscalSaidaVO.setCnpjCpf(notaFiscalSaidaVO.getParceiroVO().getCNPJ());
					} else {
						notaFiscalSaidaVO.setCnpjCpf(notaFiscalSaidaVO.getParceiroVO().getCPF());
					}
					notaFiscalSaidaVO.setCep(notaFiscalSaidaVO.getParceiroVO().getCEP());
					if (notaFiscalSaidaVO.getParceiroVO().getTipoEmpresa().equals("JU")) {
						notaFiscalSaidaVO.setInscricaoEstadual(notaFiscalSaidaVO.getInscricaoEstadual());
					} else {
						notaFiscalSaidaVO.setInscricaoEstadual("");
					}
					notaFiscalSaidaVO.setUf(notaFiscalSaidaVO.getParceiroVO().getCidade().getEstado().getSigla());
					notaFiscalSaidaVO.setTelefone(notaFiscalSaidaVO.getParceiroVO().getTelComercial1());
					notaFiscalSaidaVO.setMunicipio(notaFiscalSaidaVO.getParceiroVO().getCidade().getNome());

				} else if (notaFiscalSaidaVO.getTipoPessoa().equals(TipoPessoa.FORNECEDOR.getValor())) {
					notaFiscalSaidaVO.setFornecedorVO(getFacadeFactory().getFornecedorFacade().consultarPorChavePrimaria(obj.getFornecedor().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));
					notaFiscalSaidaVO.setSacado(notaFiscalSaidaVO.getFornecedorVO().getNome());
					notaFiscalSaidaVO.setEndereco(notaFiscalSaidaVO.getFornecedorVO().getEndereco() + " " + notaFiscalSaidaVO.getFornecedorVO().getNumero() + " " + notaFiscalSaidaVO.getFornecedorVO().getComplemento());
					notaFiscalSaidaVO.setNomeRazaoSocial(notaFiscalSaidaVO.getFornecedorVO().getNome());
					if (notaFiscalSaidaVO.getFornecedorVO().getTipoEmpresa().equals("JU")) {
						notaFiscalSaidaVO.setCnpjCpf(notaFiscalSaidaVO.getFornecedorVO().getCNPJ());
					} else {
						notaFiscalSaidaVO.setCnpjCpf(notaFiscalSaidaVO.getFornecedorVO().getCPF());
					}
					notaFiscalSaidaVO.setCep(notaFiscalSaidaVO.getFornecedorVO().getCEP());
					if (notaFiscalSaidaVO.getFornecedorVO().getTipoEmpresa().equals("JU")) {
						notaFiscalSaidaVO.setInscricaoEstadual(notaFiscalSaidaVO.getFornecedorVO().getInscEstadual());
					} else {
						notaFiscalSaidaVO.setInscricaoEstadual("");
					}
					notaFiscalSaidaVO.setUf(notaFiscalSaidaVO.getFornecedorVO().getCidade().getEstado().getSigla());
					notaFiscalSaidaVO.setTelefone(notaFiscalSaidaVO.getFornecedorVO().getTelComercial1());
					notaFiscalSaidaVO.setMunicipio(notaFiscalSaidaVO.getFornecedorVO().getCidade().getNome());
					notaFiscalSaidaVO.setSetor(notaFiscalSaidaVO.getFornecedorVO().getSetor());

				} else if (notaFiscalSaidaVO.getTipoPessoa().equals(TipoPessoa.FUNCIONARIO.getValor())) {
					notaFiscalSaidaVO.setFuncionarioVO(getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimaria(obj.getFuncionario().getCodigo(), 0, false, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));
					notaFiscalSaidaVO.setSacado(notaFiscalSaidaVO.getFuncionarioVO().getPessoa().getNome());
					notaFiscalSaidaVO.setEndereco(notaFiscalSaidaVO.getPessoaVO().getEndereco() + " " + notaFiscalSaidaVO.getPessoaVO().getNumero() + " " + notaFiscalSaidaVO.getPessoaVO().getComplemento());
					notaFiscalSaidaVO.setNomeRazaoSocial(notaFiscalSaidaVO.getFuncionarioVO().getPessoa().getNome());
					notaFiscalSaidaVO.setCnpjCpf(notaFiscalSaidaVO.getFuncionarioVO().getPessoa().getCPF());
					notaFiscalSaidaVO.setCep(notaFiscalSaidaVO.getFuncionarioVO().getPessoa().getCEP());
					notaFiscalSaidaVO.setInscricaoEstadual("");
					notaFiscalSaidaVO.setUf(notaFiscalSaidaVO.getFuncionarioVO().getPessoa().getCidade().getEstado().getSigla());
					notaFiscalSaidaVO.setTelefone(notaFiscalSaidaVO.getFuncionarioVO().getPessoa().getTelefoneRes());
					notaFiscalSaidaVO.setMunicipio(notaFiscalSaidaVO.getFuncionarioVO().getPessoa().getCidade().getNome());
					notaFiscalSaidaVO.setSetor(notaFiscalSaidaVO.getFuncionarioVO().getPessoa().getSetor());

				} else if (notaFiscalSaidaVO.getTipoPessoa().equals(TipoPessoa.RESPONSAVEL_FINANCEIRO.getValor())) {
					notaFiscalSaidaVO.setPessoaVO(getFacadeFactory().getPessoaFacade().consultarDadosGerarNotaFiscalSaida(obj.getPessoa().getCodigo(), false, usuarioVO));
					notaFiscalSaidaVO.setResponsavelFinanceiro(getFacadeFactory().getPessoaFacade().consultarDadosGerarNotaFiscalSaida(obj.getResponsavelFinanceiro().getCodigo(), false, usuarioVO));
					notaFiscalSaidaVO.setSacado(notaFiscalSaidaVO.getResponsavelFinanceiro().getNome());
					notaFiscalSaidaVO.setEndereco(notaFiscalSaidaVO.getResponsavelFinanceiro().getEndereco() + " " + notaFiscalSaidaVO.getResponsavelFinanceiro().getNumero() + " " + notaFiscalSaidaVO.getResponsavelFinanceiro().getComplemento());
					notaFiscalSaidaVO.setNomeRazaoSocial(notaFiscalSaidaVO.getResponsavelFinanceiro().getNome());
					notaFiscalSaidaVO.setCnpjCpf(notaFiscalSaidaVO.getResponsavelFinanceiro().getCPF());
					notaFiscalSaidaVO.setCep(notaFiscalSaidaVO.getResponsavelFinanceiro().getCEP());
					notaFiscalSaidaVO.setUf(notaFiscalSaidaVO.getResponsavelFinanceiro().getCidade().getEstado().getSigla());
					notaFiscalSaidaVO.setTelefone(notaFiscalSaidaVO.getResponsavelFinanceiro().getTelefoneComer());
					notaFiscalSaidaVO.setMunicipio(notaFiscalSaidaVO.getResponsavelFinanceiro().getCidade().getNome());
					notaFiscalSaidaVO.setSetor(notaFiscalSaidaVO.getResponsavelFinanceiro().getSetor());

				} else {
					notaFiscalSaidaVO.setPessoaVO(getFacadeFactory().getPessoaFacade().consultarDadosGerarNotaFiscalSaida(obj.getPessoa().getCodigo(), false, usuarioVO));
					notaFiscalSaidaVO.setSacado(notaFiscalSaidaVO.getPessoaVO().getNome());
					notaFiscalSaidaVO.setEndereco(notaFiscalSaidaVO.getPessoaVO().getEndereco() + " " + notaFiscalSaidaVO.getPessoaVO().getNumero() + " " + notaFiscalSaidaVO.getPessoaVO().getComplemento());
					notaFiscalSaidaVO.setNomeRazaoSocial(notaFiscalSaidaVO.getPessoaVO().getNome());
					notaFiscalSaidaVO.setCnpjCpf(notaFiscalSaidaVO.getPessoaVO().getCPF());
					notaFiscalSaidaVO.setCep(notaFiscalSaidaVO.getPessoaVO().getCEP());
					notaFiscalSaidaVO.setUf(notaFiscalSaidaVO.getPessoaVO().getCidade().getEstado().getSigla());
					notaFiscalSaidaVO.setTelefone(notaFiscalSaidaVO.getPessoaVO().getTelefoneComer());
					notaFiscalSaidaVO.setMunicipio(notaFiscalSaidaVO.getPessoaVO().getCidade().getNome());
					notaFiscalSaidaVO.setSetor(notaFiscalSaidaVO.getPessoaVO().getSetor());
				}
				adicionarNotaFiscalServico(notaFiscalSaidaVO, obj, configuracaoNotaFiscalVO);
				// notaFiscalSaidaVO.getNotaFiscalSaidaServicoVOs().add(montarDadosNotaFiscalSaidaServico(obj,
				// configuracaoNotaFiscalVO));
				notaFiscalSaidaVO.setValorTotal(new Double(Uteis.getValorTruncadoDeDoubleParaString(notaFiscalSaidaVO.getNotaFiscalSaidaServicoVOs().get(0).getPrecoUnitario(), 2)));
				notaFiscalSaidaVO.setTotalIssqn(notaFiscalSaidaVO.getNotaFiscalSaidaServicoVOs().get(0).getPrecoUnitario() * (configuracaoNotaFiscalVO.getIssqn() / 100));
				notaFiscalSaidaVO.setValorTotalPIS(notaFiscalSaidaVO.getNotaFiscalSaidaServicoVOs().get(0).getPrecoUnitario() * (configuracaoNotaFiscalVO.getPis() / 100));
				notaFiscalSaidaVO.setValorTotalCOFINS(notaFiscalSaidaVO.getNotaFiscalSaidaServicoVOs().get(0).getPrecoUnitario() * (configuracaoNotaFiscalVO.getCofins() / 100));
				if (!notaFiscalSaidaVO.getSituacao().equals(SituacaoNotaFiscalSaidaEnum.AUTORIZADA.getValor())) {
					calcularImpostos(notaFiscalSaidaVO, configuracaoNotaFiscalVO);
				}
				notaFiscalSaidaVO.setCodigo(obj.getNotaFiscalSaidaServicoVO().getNotaFiscalSaida().getCodigo());
				notaFiscalSaidaVO.setNumero(obj.getNotaFiscalSaidaServicoVO().getNotaFiscalSaida().getNumero());
				notaFiscalSaidaVO.setNumeroRPS(obj.getNotaFiscalSaidaServicoVO().getNotaFiscalSaida().getNumeroRPS());
				notaFiscalSaidaVO.setLote(obj.getNotaFiscalSaidaServicoVO().getNotaFiscalSaida().getLote());
				notaFiscalSaidaVO.setSituacao(obj.getNotaFiscalSaidaServicoVO().getNotaFiscalSaida().getSituacao());
				notaFiscalSaidaVO.setCodigoNaturezaOperacao(configuracaoNotaFiscalVO.getCodigoNaturezaOperacao());
				notaFiscalSaidaVO.setNomeNaturezaOperacao(configuracaoNotaFiscalVO.getNomeNaturezaOperacao());
				notaFiscalSaidaVO.setNaturezaOperacaoEnum(configuracaoNotaFiscalVO.getNaturezaOperacaoEnum());
				notaFiscalSaidaVO.setIsIncentivadorCultural(configuracaoNotaFiscalVO.getIsIncentivadorCultural());
				notaFiscalSaidaVO.setRegimeEspecialTributacaoEnum(configuracaoNotaFiscalVO.getRegimeEspecialTributacaoEnum());
				notaFiscalSaidaVO.setCodigoCNAE(configuracaoNotaFiscalVO.getCodigoCNAE());
				notaFiscalSaidaVO.setPercentualCargaTributaria(configuracaoNotaFiscalVO.getPercentualCargaTributaria());
				notaFiscalSaidaVO.setFonteCargaTributaria(configuracaoNotaFiscalVO.getFonteCargaTributaria());
				notaFiscalSaidaVO.setMensagemRetorno(obj.getNotaFiscalSaidaServicoVO().getNotaFiscalSaida().getMensagemRetorno());
				notaFiscalSaidaVO.setModelo("55");
				if (AmbienteNfeEnum.PRODUCAO.equals(configuracaoNotaFiscalVO.getAmbienteNfeEnum())) {
					notaFiscalSaidaVO.setSerie(configuracaoNotaFiscalVO.getSerie());
				} else {
					notaFiscalSaidaVO.setSerie(configuracaoNotaFiscalVO.getSerieHomologacao());
				}
				notaFiscalSaidaVO.getUnidadeEnsinoVO().setConfiguracaoNotaFiscalVO(getFacadeFactory().getConfiguracaoNotaFiscalFacade().consultarPorUnidadeEnsino(unidadeEnsino, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
				hashMapNFS.put(chaveNotaFiscal, notaFiscalSaidaVO);
				notaFiscalSaidaVO.setNovoObj(false);
				if (notaFiscalSaidaVO.getSituacao().equals(SituacaoNotaFiscalSaidaEnum.ENVIADA.getValor())) {
					notaFiscalSaidaEnviadasVOs.add(notaFiscalSaidaVO);
				} else {
					notaFiscalSaidaVOs.add(notaFiscalSaidaVO);
				}
			}
		}
	}

	}

	public NotaFiscalSaidaServicoVO montarDadosNotaFiscalSaidaServico(ContaReceberRecebimentoVO crrVO, ConfiguracaoNotaFiscalVO cnfVO) throws Exception {
		NotaFiscalSaidaServicoVO obj = new NotaFiscalSaidaServicoVO();
		if (Uteis.isAtributoPreenchido(crrVO.getNotaFiscalSaidaServicoVO())) {
			obj = getFacadeFactory().getNotaFiscalSaidaServicoFacade().consultarPorChavePrimaria(crrVO.getNotaFiscalSaidaServicoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, null);
			obj.getContaReceberVO().setCodigo(crrVO.getContaReceber());
	        obj.getContaReceberRecebimentoVO().getContaReceberVO().setCodigo(crrVO.getContaReceber());
		} else {
			if (crrVO.getContaReceberVO().getDescricaoPagamento().trim().isEmpty()) {
				obj.setDescricao(crrVO.getContaReceberVO().getTipoOrigem_apresentar() + " - Parcela: " + crrVO.getContaReceberVO().getParcela());
			} else {
				obj.setDescricao(Uteis.removeCaractersEspeciais(crrVO.getContaReceberVO().getDescricaoPagamento()));
			}
		}
		obj.setPrecoUnitario(crrVO.getValorRecebimento());

		obj.setAliquotaIssqn(cnfVO.getIssqn());
		obj.setBaseIssqn(cnfVO.getIssqn() / 100);
		obj.setTotalIssqn(new Double(Uteis.getValorTruncadoDeDoubleParaString((obj.getBaseIssqn() * obj.getPrecoUnitario()), 2)));
		// obj.setTotalIssqn(obj.getBaseIssqn() * obj.getPrecoUnitario());

		obj.getContaReceberRecebimentoVO().setCodigo(crrVO.getCodigo());
		obj.getListaCodigoContaReceberRecebimento().add(crrVO.getCodigo());
		obj.getContaReceberVO().setCodigo(crrVO.getContaReceber());
        obj.getContaReceberRecebimentoVO().setContaReceberVO(crrVO.getContaReceberVO());
        obj.getContaReceberRecebimentoVO().setContaReceber(crrVO.getContaReceber());
		obj.setCodigoNaturezaOperacao(cnfVO.getCodigoNaturezaOperacao());
		obj.setNomeNaturezaOperacao(cnfVO.getNomeNaturezaOperacao());
		obj.setCodigoNCM(cnfVO.getCodigoNCM());
		// obj.setNotaFiscalSaida(crrVO.getNotaFiscalSaidaServicoVO().getNotaFiscalSaida());
		obj.getNotaFiscalSaida().setCodigo(crrVO.getNotaFiscalSaidaServicoVO().getNotaFiscalSaida().getCodigo());

		return obj;
	}

	public NotaFiscalSaidaServicoVO montarDadosNotaFiscalSaidaServico(ContaReceberVO contaReceberVO, ConfiguracaoNotaFiscalVO cnfVO) throws Exception {
		NotaFiscalSaidaServicoVO obj = new NotaFiscalSaidaServicoVO();
		if (Uteis.isAtributoPreenchido(contaReceberVO.getNotaFiscalSaidaServicoVO())) {
			obj = getFacadeFactory().getNotaFiscalSaidaServicoFacade().consultarPorChavePrimaria(contaReceberVO.getNotaFiscalSaidaServicoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, null);
			obj.getContaReceberVO().setCodigo(contaReceberVO.getCodigo());
	        obj.getContaReceberRecebimentoVO().getContaReceberVO().setCodigo(contaReceberVO.getCodigo());
		} else {
			if (contaReceberVO.getDescricaoPagamento().trim().isEmpty()) {
				obj.setDescricao(contaReceberVO.getTipoOrigem_apresentar() + " - Parcela: " + contaReceberVO.getParcela());
			} else {
				obj.setDescricao(Uteis.removeCaractersEspeciais(contaReceberVO.getDescricaoPagamento()));
			}
			obj.setPrecoUnitario(contaReceberVO.getValor());
			obj.setAliquotaIssqn(cnfVO.getIssqn());
			obj.setBaseIssqn(cnfVO.getIssqn() / 100);
			obj.setTotalIssqn(new Double(Uteis.getValorTruncadoDeDoubleParaString((obj.getBaseIssqn() * obj.getPrecoUnitario()), 2)));
			obj.getContaReceberVO().setCodigo(contaReceberVO.getCodigo());
	        obj.getContaReceberRecebimentoVO().getContaReceberVO().setCodigo(contaReceberVO.getCodigo());
	        obj.getContaReceberRecebimentoVO().setContaReceber(contaReceberVO.getCodigo());
			obj.getListaCodigoContaReceber().add(contaReceberVO);
			obj.setCodigoNaturezaOperacao(cnfVO.getCodigoNaturezaOperacao());
			obj.setNomeNaturezaOperacao(cnfVO.getNomeNaturezaOperacao());
			obj.setCodigoNCM(cnfVO.getCodigoNCM());
		}
		return obj;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public synchronized void registrarNumeroNotaFiscal(NotaFiscalSaidaVO notaFiscal, ConfiguracaoNotaFiscalVO configuracao ,UsuarioVO usuarioLogado) throws Exception {
		try {
			Long numeroExistente = consultarNumeroNotaFiscal(notaFiscal.getCodigo());
			if (numeroExistente == null || numeroExistente.longValue() == 0) {
				Long numeroNota = 0L;
				String sqlStr = "SELECT case when ambientenfe = '1' then numeronota else numeronotahomologacao end as numeronota FROM configuracaonotafiscal where codigo = " + configuracao.getCodigo();
				SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
				if (tabelaResultado.next()) {
					numeroNota = tabelaResultado.getLong("numeronota");
				}
				numeroNota = numeroNota + 1;
				if (numeroNota.longValue() > 999999999) {
					numeroNota = 1l;
				}
				while (consultarExistenciaNotaFiscalComMesmoNumeroESerie(notaFiscal, numeroNota, configuracao.getCodigo())) {
					numeroNota++;
				}
				notaFiscal.setNumero(numeroNota);
				if (notaFiscal.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getTipoIntegracaoNfeEnum().equals(TipoIntegracaoNfeEnum.NFE)) {
					gravarNumeroNotaFiscal(notaFiscal, configuracao, numeroNota,usuarioLogado);
				}
				sqlStr = null;
				tabelaResultado = null;
			}
		} catch (Exception e) {
			notaFiscal.setNumero(0l);
			notaFiscal.setIdentificadorReceita("");
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public synchronized void registrarNumeroRPS(NotaFiscalSaidaVO notaFiscal, ConfiguracaoNotaFiscalVO configuracao) throws Exception {
		try {
			Long numeroExistente = consultarNumeroNotaFiscal(notaFiscal.getCodigo());
			if (numeroExistente == null || numeroExistente.longValue() == 0) {
				Integer numeroRPS = 0;
				String sqlStr = "SELECT case when ambientenfe = '1' then numeronota else numeronotahomologacao end as numerorps FROM configuracaonotafiscal where codigo = " + configuracao.getCodigo();
				SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
				if (!tabelaResultado.next()) {
				} else {
					numeroRPS = tabelaResultado.getInt("numerorps");
				}
				numeroRPS = numeroRPS + 1;
				if (numeroRPS.longValue() > 999999999) {
					numeroRPS = 1;
				}
				while (consultarExistenciaNotaFiscalComMesmoNumeroRPSESerie(notaFiscal, numeroRPS, configuracao.getCodigo())) {
					numeroRPS++;
				}
				notaFiscal.setNumeroRPS(numeroRPS);
				if (AmbienteNfeEnum.PRODUCAO.equals(configuracao.getAmbienteNfeEnum())) {
					sqlStr = "UPDATE configuracaonotafiscal set numeronota=?  WHERE codigo = " + notaFiscal.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCodigo() + " and numeronota < ?";
				} else {
					sqlStr = "UPDATE configuracaonotafiscal set numeronotahomologacao=?  WHERE codigo = " + notaFiscal.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCodigo() + " and numeronotahomologacao < ?";
				}
				getConexao().getJdbcTemplate().update(sqlStr, new Object[] { notaFiscal.getNumeroRPS(), notaFiscal.getNumeroRPS() });
				sqlStr = null;
				tabelaResultado = null;
			}
		} catch (Exception e) {
			notaFiscal.setNumeroRPS(0);
			notaFiscal.setIdentificadorReceita("");
			throw e;
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public synchronized void registrarLoteNotaFiscal(NotaFiscalSaidaVO notaSaidaVO) throws Exception {
		try {
			String sqlStr = "SELECT case when ambientenfe = '1' then lote else lotehomologacao end as lote FROM configuracaonotafiscal where codigo = " + notaSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCodigo();
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
			Long numeroLote = 0L;
			if (tabelaResultado.next()) {
				numeroLote = tabelaResultado.getLong("lote");
			}
			numeroLote = numeroLote + 1;
			if (numeroLote.longValue() > 999999999) {
				numeroLote = 1l;
			}
			if (AmbienteNfeEnum.PRODUCAO.equals(notaSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getAmbienteNfeEnum())) {
				sqlStr = "UPDATE configuracaonotafiscal set lote=?  WHERE codigo = " + notaSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCodigo();
			} else {
				sqlStr = "UPDATE configuracaonotafiscal set lotehomologacao=?  WHERE codigo = " + notaSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCodigo();
			}
			getConexao().getJdbcTemplate().update(sqlStr, new Object[] { numeroLote });
			sqlStr = null;
			tabelaResultado = null;
			if (AmbienteNfeEnum.PRODUCAO.equals(notaSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getAmbienteNfeEnum())) {
				notaSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().setLote(numeroLote);
				notaSaidaVO.setLote(String.valueOf(notaSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getLote()));
			} else {
				notaSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().setLoteHomologacao(Integer.valueOf(numeroLote.toString()));
				notaSaidaVO.setLote(String.valueOf(notaSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getLoteHomologacao()));

			}
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void preencherTodosListaContasRecebidas(List<NotaFiscalSaidaVO> notaFiscalSaidaVOs) {
		for (NotaFiscalSaidaVO obj : notaFiscalSaidaVOs) {
			if (!obj.getSituacao().equals("AU")) {
				obj.setNotaFiscalSaidaSelecionado(true);
			}
		}
	}

	@Override
	public void desmarcarTodosListaContasRecebidas(List<NotaFiscalSaidaVO> notaFiscalSaidaVOs) {
		for (NotaFiscalSaidaVO obj : notaFiscalSaidaVOs) {
			obj.setNotaFiscalSaidaSelecionado(false);
		}
	}

	public void montarDadosEmitenteNotaFiscal(NfeVO nfeVO, NotaFiscalSaidaVO nota, ConfiguracaoNotaFiscalVO conNotaFiscalVO) throws Exception {
		if (conNotaFiscalVO.getUtilizarEnderecoDiferenteUnidade()) {
			nfeVO.setFoneEmit(conNotaFiscalVO.getTelComercial1().trim());
			nfeVO.setCepEmit(conNotaFiscalVO.getCEP().trim());
			nfeVO.setMunicipioEmit(conNotaFiscalVO.getCidade().getNome());
			nfeVO.setBairoEmit(conNotaFiscalVO.getSetor().toUpperCase());
			nfeVO.setNrEmit(conNotaFiscalVO.getNumero().trim());
			nfeVO.setLogradouroEmit(conNotaFiscalVO.getEndereco().trim());
			nfeVO.setUfEmit(conNotaFiscalVO.getCidade().getEstado().getSigla());
		} else {
			nfeVO.setFoneEmit(nota.getUnidadeEnsinoVO().getTelComercial1().trim());
			nfeVO.setCepEmit(nota.getUnidadeEnsinoVO().getCEP().trim());
			nfeVO.setMunicipioEmit(nota.getUnidadeEnsinoVO().getCidade().getNome());
			nfeVO.setBairoEmit(nota.getUnidadeEnsinoVO().getSetor().toUpperCase());
			nfeVO.setNrEmit(nota.getUnidadeEnsinoVO().getNumero().trim());
			nfeVO.setLogradouroEmit(nota.getUnidadeEnsinoVO().getEndereco().trim());
			nfeVO.setUfEmit(nota.getUnidadeEnsinoVO().getCidade().getEstado().getSigla());
		}
		nfeVO.setCnpjEmit(nota.getUnidadeEnsinoVO().getCNPJ());
		nfeVO.setNomeEmit(nota.getUnidadeEnsinoVO().getRazaoSocial().trim());
		nfeVO.setNomeFantasiaEmit(nota.getUnidadeEnsinoVO().getNome().trim());
		nfeVO.setPaisEmit("1058");
		nfeVO.setNomePaisEmit("BRASIL");
		nfeVO.setInscricaoEstEmit(nota.getUnidadeEnsinoVO().getInscEstadual().trim());
		nfeVO.setInscricaoMunicipalEmit(nota.getUnidadeEnsinoVO().getInscMunicipal().trim());
		if (nfeVO.getNrEmit().equals("")) {
			nfeVO.setNrEmit("SN");
		}
	}

	public void montarDadosDestinatarioNotaFiscal(NfeVO nfeVO, NotaFiscalSaidaVO nota) throws Exception {
		String cpfDest = "";
		String nomeDest = "";
		String cnpjDest = "";
		String logradouroDest = "";
		String nrDest = "";
		String bairroDest = "";
		String codigoIBGEMunicipioDest = "";
		String tipoPessoaDest = "";
		String ufDest = "";
		String municipioDest = "";
		String cepDest = "";
		if (nota.getTipoPessoa().equals(TipoPessoa.PARCEIRO.getValor())) {
			cpfDest = nota.getParceiroVO().getCPF();
			nomeDest = nota.getParceiroVO().getNome();
			cnpjDest = nota.getParceiroVO().getCNPJ();
			logradouroDest = nota.getParceiroVO().getEndereco();
			nrDest = nota.getParceiroVO().getNumero();
			bairroDest = Uteis.removerCaracteresEspeciais3(nota.getParceiroVO().getSetor());
			codigoIBGEMunicipioDest = nota.getParceiroVO().getCidade().getCodigoIBGE();
			tipoPessoaDest = nota.getParceiroVO().getTipoEmpresa();
			ufDest = nota.getParceiroVO().getCidade().getEstado().getSigla();
			municipioDest = nota.getParceiroVO().getCidade().getNome();
			cepDest = nota.getParceiroVO().getCEP();
			
		} else if (nota.getTipoPessoa().equals(TipoPessoa.FORNECEDOR.getValor())) {
			cpfDest = nota.getFornecedorVO().getCPF();
			nomeDest = nota.getFornecedorVO().getNome();
			cnpjDest = nota.getFornecedorVO().getCNPJ();
			logradouroDest = nota.getFornecedorVO().getEndereco();
			nrDest = nota.getFornecedorVO().getNumero();
			bairroDest = Uteis.removerCaracteresEspeciais3(nota.getFornecedorVO().getSetor());
			codigoIBGEMunicipioDest = nota.getFornecedorVO().getCidade().getCodigoIBGE();
			tipoPessoaDest = nota.getFornecedorVO().getTipoEmpresa();
			cepDest = nota.getFornecedorVO().getCEP();
		} else if (nota.getTipoPessoa().equals(TipoPessoa.FUNCIONARIO.getValor())) {
			cpfDest = nota.getFuncionarioVO().getPessoa().getCPF();
			nomeDest = nota.getFuncionarioVO().getPessoa().getNome();
			logradouroDest = nota.getFuncionarioVO().getPessoa().getEndereco();
			nrDest = nota.getFuncionarioVO().getPessoa().getNumero();
			bairroDest = Uteis.removerCaracteresEspeciais3(nota.getFuncionarioVO().getPessoa().getSetor());
			codigoIBGEMunicipioDest = nota.getFuncionarioVO().getPessoa().getCidade().getCodigoIBGE();
			ufDest = nota.getFuncionarioVO().getPessoa().getCidade().getEstado().getSigla();
			municipioDest = nota.getFuncionarioVO().getPessoa().getCidade().getNome();
			tipoPessoaDest = "FI";
			cepDest = nota.getFuncionarioVO().getPessoa().getCEP();
		} else if (nota.getTipoPessoa().equals(TipoPessoa.RESPONSAVEL_FINANCEIRO.getValor())) {
			cpfDest = nota.getResponsavelFinanceiro().getCPF();
			nomeDest = nota.getResponsavelFinanceiro().getNome();
			logradouroDest = nota.getResponsavelFinanceiro().getEndereco();
			nrDest = nota.getResponsavelFinanceiro().getNumero();
			bairroDest = Uteis.removerCaracteresEspeciais3(nota.getResponsavelFinanceiro().getSetor());
			codigoIBGEMunicipioDest = nota.getResponsavelFinanceiro().getCidade().getCodigoIBGE();
			tipoPessoaDest = "FI";
			ufDest = nota.getResponsavelFinanceiro().getCidade().getEstado().getSigla();
			municipioDest = nota.getResponsavelFinanceiro().getCidade().getNome();
			cepDest = nota.getResponsavelFinanceiro().getCEP();
		} else {
			cpfDest = nota.getPessoaVO().getCPF();
			nomeDest = nota.getPessoaVO().getNome();
			logradouroDest = nota.getPessoaVO().getEndereco();
			nrDest = nota.getPessoaVO().getNumero();
			bairroDest = Uteis.removerCaracteresEspeciais3(nota.getPessoaVO().getSetor());
			codigoIBGEMunicipioDest = nota.getPessoaVO().getCidade().getCodigoIBGE();
			tipoPessoaDest = "FI";
			ufDest = nota.getPessoaVO().getCidade().getEstado().getSigla();
			municipioDest = nota.getPessoaVO().getCidade().getNome();
			cepDest = nota.getPessoaVO().getCEP();
		}
		nfeVO.setTipoPessoaDest(tipoPessoaDest);
		nfeVO.setNomeDest(nomeDest.trim());
		if (nfeVO.getTipoPessoaDest().equals("JU")) {
			nfeVO.setCnpjDest(cnpjDest);
			nfeVO.setInscricaoEstDest(Uteis.removerMascara(nota.getParceiroVO().getInscEstadual()).trim());
		} else {
			nfeVO.setCpfDest(cpfDest);
		}

		if (nota.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getAmbienteNfeEnum().getKey().toString().equals("2")) {
			if (nfeVO.getTipoPessoaDest().equals("JU")) {
				nfeVO.setCnpjDest(cnpjDest);
				nfeVO.setInscricaoEstDest(Uteis.removerMascara(nota.getParceiroVO().getInscEstadual()).trim());
			} else {
				nfeVO.setCpfDest(cpfDest);
			}
			nfeVO.setNomeDest("NF-E EMITIDA EM AMBIENTE DE HOMOLOGACAO - SEM VALOR FISCAL");
		}

		nfeVO.setPaisDest("1058");
		nfeVO.setNomePaisDest("BRASIL");
		nfeVO.setLogradouroDest(logradouroDest);
		if (!nrDest.equals("")) {
			nfeVO.setNrDest(nrDest);
		} else {
			nfeVO.setNrDest("SN");
		}
		nfeVO.setBairroDest(bairroDest);
		nfeVO.setCodigoIBGEMunicipioDest(codigoIBGEMunicipioDest);
		nfeVO.setUfDest(ufDest);
		nfeVO.setMunicipioDest(municipioDest);
		nfeVO.setCepDest(cepDest);

		String nomeNaturezaOperacao = "";
		if (ufDest.equals(nota.getUnidadeEnsinoVO().getCidade().getEstado().getSigla())) {
			nfeVO.setIdLocalDestinoOperacao(DadosNfe.OPERACAO_INTERNA);
			nomeNaturezaOperacao = nota.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getNomeNaturezaOperacao();
			nfeVO.setCodigoNaturezaOperacao(nota.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCodigoNaturezaOperacao());
		} else {
			nfeVO.setIdLocalDestinoOperacao(DadosNfe.OPERACAO_INTERESTADUAL);
			nomeNaturezaOperacao = nota.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getNomeNaturezaOperacaoInterestadual();
			nfeVO.setCodigoNaturezaOperacao(nota.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCodigoNaturezaOperacaoInterestadual());
		}
		nfeVO.setNaturezaOperacao(nomeNaturezaOperacao);
	}

	public String getDesignIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + "NFRel.jrxml");
	}

	public String getCaminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator);
	}

	public static void validarDados(NotaFiscalSaidaVO notaFiscalSaidaVO,  Integer unidadeEnsino, boolean buscarContasAReceber, boolean buscarContasRecebidas, String dataConsiderar) throws Exception {
		if (!Uteis.isAtributoPreenchido(unidadeEnsino)) {
			throw new Exception("O campo Unidade Ensino deve ser informado.");
		}
		if (buscarContasAReceber == false && buscarContasRecebidas == false) {
			throw new Exception("Deve ser selecionado pelo menos uma das opções ( Trazer Contas A Receber , Trazer Contas Recebidas ).");
		}
		if (buscarContasRecebidas == true) {
			if (dataConsiderar.equals("")) {
				throw new Exception("Para busca de Contas Recebidas, a data Considerar deve ser selecionada.");
			}
		}
		
		verificarCompetenciaBloqueadaParaRegistrosEntidade(notaFiscalSaidaVO, "INCLUIR", notaFiscalSaidaVO.getDataEmissao(), unidadeEnsino, TipoOrigemHistoricoBloqueioEnum.NFSAIDA, notaFiscalSaidaVO.getResponsavel());
	}

	/**
	 * Método responsável por verificar se Nota Fiscal Saída já foi emitida para
	 * aquela Conta Receber Recebimento, caso seja verdadeiro é retornado o objeto
	 * preenchido com as informações de Emissão.
	 * 
	 * @param notaFiscalSaidaVO
	 * @param usuarioLogado
	 * @return
	 * @throws Exception
	 */
	public boolean verificarNotaFiscalSaidaJaEmitida(NotaFiscalSaidaVO notaFiscalSaidaVO, UsuarioVO usuarioLogado) throws Exception {
		for (NotaFiscalSaidaServicoVO nfssVO : notaFiscalSaidaVO.getNotaFiscalSaidaServicoVOs()) {
			Integer notaFiscalSaidaEmitida = getFacadeFactory().getContaReceberRecebimentoFacade().verificarNotaFiscalSaidaServicoEmitidaContaReceberRecebimento(nfssVO.getContaReceberRecebimentoVO().getCodigo());
			if (Uteis.isAtributoPreenchido(notaFiscalSaidaEmitida)) {
				alterar(notaFiscalSaidaVO, usuarioLogado);
				notaFiscalSaidaVO.setCodigo(notaFiscalSaidaEmitida);
				this.carregarDados(notaFiscalSaidaVO, usuarioLogado);
				return true;
			}
		}
		return false;
	}

	public void carregarDados(NotaFiscalSaidaVO obj, UsuarioVO usuario) throws Exception {
		SqlRowSet dadosSQL = consultarPorChavePrimariaDadosSeremCarregados(obj.getCodigo());
		if (dadosSQL.next()) {
			montarDadosCompleto(obj, dadosSQL, usuario);
		}
	}

	public void montarDadosCompleto(NotaFiscalSaidaVO obj, SqlRowSet dadosSQL, UsuarioVO usuarioLogado) throws Exception {
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setDataEmissao(dadosSQL.getDate("dataEmissao"));
		obj.getUnidadeEnsinoVO().setCodigo(dadosSQL.getInt("unidadeensino"));
		obj.setNumero(dadosSQL.getLong("numero"));
		obj.setLote(dadosSQL.getString("lote"));
		obj.setValorTotal((dadosSQL.getDouble("valortotal")));
		obj.setSituacao(dadosSQL.getString("situacao"));
		obj.setMatricula(dadosSQL.getString("matricula"));
		obj.setTipoPessoa(dadosSQL.getString("tipoPessoa"));
		obj.getPessoaVO().setCodigo(dadosSQL.getInt("aluno.codigo"));
		obj.getResponsavelFinanceiro().setCodigo(dadosSQL.getInt("responsavel.codigo"));
		obj.getFuncionarioVO().setCodigo(dadosSQL.getInt("funcionario.codigo"));
		obj.getParceiroVO().setCodigo(dadosSQL.getInt("parceiro.codigo"));

		if (obj.getTipoPessoa().equals(TipoPessoa.PARCEIRO.getValor())) {
			if (!obj.getMatricula().isEmpty()) {
				obj.setPessoaVO(getFacadeFactory().getPessoaFacade().consultarAlunoPorMatricula(obj.getMatricula(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioLogado));
			}
			obj.setParceiroVO(getFacadeFactory().getParceiroFacade().consultarPorChavePrimaria(obj.getParceiroVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado));
		} else if (obj.getTipoPessoa().equals(TipoPessoa.FUNCIONARIO.getValor())) {
			obj.setFuncionarioVO(getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimaria(obj.getFuncionarioVO().getCodigo(), obj.getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado));
		}
		if (obj.getPessoaVO() != null && obj.getPessoaVO().getCodigo().intValue() != 0) {
			obj.setPessoaVO(getFacadeFactory().getPessoaFacade().consultarDadosGerarNotaFiscalSaida(dadosSQL.getInt("aluno.codigo"), false, usuarioLogado));
		}
		if (obj.getResponsavelFinanceiro() != null && obj.getResponsavelFinanceiro().getCodigo().intValue() != 0) {
			obj.setResponsavelFinanceiro(getFacadeFactory().getPessoaFacade().consultarDadosGerarNotaFiscalSaida(dadosSQL.getInt("responsavel.codigo"), false, usuarioLogado));
		}
		obj.setMensagemRetorno(dadosSQL.getString("mensagemretorno"));
		obj.setObservacaoContribuinte(dadosSQL.getString("observacaocontribuinte"));
		obj.setTotalIssqn(dadosSQL.getDouble("totalissqn"));

		obj.setIdentificadorReceita(dadosSQL.getString("identificadorreceita"));
		obj.setModelo(dadosSQL.getString("modelo"));
		obj.setSerie(dadosSQL.getString("serie"));
		obj.setRecibo(dadosSQL.getString("recibo"));
		obj.setDataStuacao(dadosSQL.getDate("datasituacao"));
		obj.setNomeNaturezaOperacao(dadosSQL.getString("nomenaturezaoperacao"));
		obj.setProtocolo(dadosSQL.getString("protocolo"));
		obj.setCodigoNaturezaOperacao(dadosSQL.getString("codigonaturezaoperacao"));

		obj.setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(dadosSQL.getInt("unidadeensino"), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado));
		obj.setTotalBaseIssqn(obj.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getIssqn());
		obj.setNotaFiscalSaidaServicoVOs(getFacadeFactory().getNotaFiscalSaidaServicoFacade().consultarNotaFiscalSaidaServicos(dadosSQL.getInt("codigo"), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado));
		obj.setNovoObj(false);
	}

	public SqlRowSet consultarPorChavePrimariaDadosSeremCarregados(Integer codigoPrm) throws Exception {
		// consultar(getIdEntidade(), false);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT notafiscalsaida.codigo, notafiscalsaida.numero, notafiscalsaida.matricula, notafiscalsaida.aluno, aluno.nome as \"aluno.nome\", aluno.codigo as \"aluno.codigo\", valortotal, notafiscalsaida.tipopessoa, dataemissao, ");
		sqlStr.append(" situacao, unidadeensino, mensagemretorno, lote, responsavelfinanceiro, responsavel.nome as \"responsavel.nome\", responsavel.codigo as \"responsavel.codigo\", observacaocontribuinte, ");
		sqlStr.append(" funcionario.codigo as \"funcionario.codigo\", parceiro.codigo as \"parceiro.codigo\", parceiro.nome as \"parceiro.nome\", ");
		sqlStr.append(" ( select distinct sum(totalissqn) from notafiscalsaidaservico where notafiscalsaidaservico.notafiscalsaida = notafiscalsaida.codigo ) AS totalissqn, identificadorreceita, modelo, serie, recibo, datasituacao, ");
		sqlStr.append(" nomenaturezaoperacao, protocolo, codigonaturezaoperacao  ");
		sqlStr.append(" FROM notafiscalsaida ");
		sqlStr.append(" LEFT JOIN pessoa as aluno on aluno.codigo = notafiscalsaida.aluno ");
		sqlStr.append(" LEFT JOIN pessoa as responsavel on responsavel.codigo = notafiscalsaida.responsavelfinanceiro ");
		sqlStr.append(" LEFT JOIN funcionario as funcionario on funcionario.codigo = notafiscalsaida.funcionario ");
		sqlStr.append(" LEFT JOIN parceiro as parceiro on parceiro.codigo = notafiscalsaida.parceiro ");
		sqlStr.append(" WHERE notafiscalsaida.codigo = ").append(codigoPrm);
		return getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
	}

	/**
	 * @author Victor Hugo de Paula Costa
	 * @param notaFiscalSaidaVO
	 * @param configuracaoRespositoriArquivo
	 * @param usuarioVO
	 * @return
	 * @throws Exception
	 */
	private void enviarNFSE(NotaFiscalSaidaVO notaFiscalSaidaVO, ConfiguracaoGeralSistemaVO configuracaoRespositoriArquivo, ProgressBarVO progressBar, String autorizado, UsuarioVO usuarioVO) throws Exception {
		notaFiscalSaidaVO.setWebServicesNFSEEnum(WebServicesNFSEEnum.getEnumPorIdCidadeTipoAcaoServico(notaFiscalSaidaVO.getUnidadeEnsinoVO().getCidade().getCodigo(), TipoAcaoServicoNFSEEnum.GerarNfse));
		CidadeVO cidade = notaFiscalSaidaVO.getUnidadeEnsinoVO().getCidadeNFSe();
		if (cidade.isWebserviceNFSeImplementado()) {
			notaFiscalSaidaVO.setWebServicesNFSEEnum(WebServicesNFSEEnum.NFSE_WEBSERVICE);
		}
		if (notaFiscalSaidaVO.getWebServicesNFSEEnum().equals(WebServicesNFSEEnum.GOIANIA_GO)) {
			getFacadeFactory().getNotaFiscalServicoEletronicaGoianiaFacade().enviarXmlEnvio(notaFiscalSaidaVO, configuracaoRespositoriArquivo, progressBar, autorizado, usuarioVO);
		} else if (notaFiscalSaidaVO.getWebServicesNFSEEnum().equals(WebServicesNFSEEnum.UBERLANDIA_MG)) {
			getFacadeFactory().getNotaFiscalServicoEletronicaUberlandiaFacade().enviarXmlEnvio(notaFiscalSaidaVO, configuracaoRespositoriArquivo, progressBar, autorizado, usuarioVO);
		}else if (notaFiscalSaidaVO.getWebServicesNFSEEnum().equals(WebServicesNFSEEnum.ARAGUAINA_TO)) {
			getFacadeFactory().getNotaFiscalServicoEletronicaAraguainaFacade().enviarXmlEnvio(notaFiscalSaidaVO, configuracaoRespositoriArquivo, progressBar, autorizado, usuarioVO);
		} else if (notaFiscalSaidaVO.getWebServicesNFSEEnum().equals(WebServicesNFSEEnum.JOAO_PESSOA_PA_GERAR)) {
			getFacadeFactory().getNotaFiscalServicoEletronicaJoaoPessoaFacade().enviarXmlEnvio(notaFiscalSaidaVO, configuracaoRespositoriArquivo, progressBar, autorizado, usuarioVO);
		} else if (notaFiscalSaidaVO.getWebServicesNFSEEnum().equals(WebServicesNFSEEnum.RIO_DE_JANEIRO_RJ_GERAR)) {
			getFacadeFactory().getNotaFiscalServicoEletronicaRioDeJaneiroFacade().enviarXmlEnvio(notaFiscalSaidaVO, configuracaoRespositoriArquivo, progressBar, autorizado, usuarioVO);
		} else if (notaFiscalSaidaVO.getWebServicesNFSEEnum().equals(WebServicesNFSEEnum.BELO_HORIZONTE_MG_GERAR)) {
			getFacadeFactory().getNotaFiscalServicoEletronicaBeloHorizonteFacade().enviarXmlEnvio(notaFiscalSaidaVO, configuracaoRespositoriArquivo, progressBar, autorizado, usuarioVO);
		} else if (notaFiscalSaidaVO.getWebServicesNFSEEnum().equals(WebServicesNFSEEnum.BELEM_PA_GERAR)) {
			getFacadeFactory().getNotaFiscalServicoEletronicaBelemFacade().enviarXmlEnvio(notaFiscalSaidaVO, configuracaoRespositoriArquivo, progressBar, autorizado, usuarioVO);
		} else if (notaFiscalSaidaVO.getWebServicesNFSEEnum().equals(WebServicesNFSEEnum.CURITIBA_PR_GERAR)) {
			getFacadeFactory().getNotaFiscalServicoEletronicaCuritibaFacade().enviarXmlEnvio(notaFiscalSaidaVO, configuracaoRespositoriArquivo, progressBar, autorizado, usuarioVO);
		} else if (notaFiscalSaidaVO.getWebServicesNFSEEnum().equals(WebServicesNFSEEnum.LONDRINA_PR_GERAR)) {
			getFacadeFactory().getNotaFiscalServicoEletronicaLondrinaFacade().enviarXmlEnvio(notaFiscalSaidaVO, configuracaoRespositoriArquivo, progressBar, autorizado, usuarioVO);
		} else if (notaFiscalSaidaVO.getWebServicesNFSEEnum().equals(WebServicesNFSEEnum.TERESINA_PI_GERAR)) {
			getFacadeFactory().getNotaFiscalServicoEletronicaTeresinaFacade().enviarXmlEnvio(notaFiscalSaidaVO, configuracaoRespositoriArquivo, progressBar, autorizado, usuarioVO);
		} else if (notaFiscalSaidaVO.getWebServicesNFSEEnum().equals(WebServicesNFSEEnum.PALMAS_TO_GERAR)) {
			getFacadeFactory().getNotaFiscalServicoEletronicaPalmasFacade().enviarXmlEnvio(notaFiscalSaidaVO, configuracaoRespositoriArquivo, progressBar, autorizado, usuarioVO);
		}  else if (notaFiscalSaidaVO.getWebServicesNFSEEnum().equals(WebServicesNFSEEnum.SAOLUIS_MA)) {
			getFacadeFactory().getNotaFiscalServicoEletronicaSaoLuisFacade().enviarXmlEnvio(notaFiscalSaidaVO, configuracaoRespositoriArquivo, progressBar, autorizado, usuarioVO);
		} else if (notaFiscalSaidaVO.getWebServicesNFSEEnum().equals(WebServicesNFSEEnum.MACEIO_AL_GERAR)) {
			getFacadeFactory().getNotaFiscalServicoEletronicaMaceioFacade().enviarXmlEnvio(notaFiscalSaidaVO, configuracaoRespositoriArquivo, progressBar, autorizado, usuarioVO);
		} else if (notaFiscalSaidaVO.getWebServicesNFSEEnum().equals(WebServicesNFSEEnum.PORTO_ALEGRE_RS_GERAR)) {
			getFacadeFactory().getNotaFiscalServicoEletronicaPortoAlegreFacade().enviarXmlEnvio(notaFiscalSaidaVO, configuracaoRespositoriArquivo, progressBar, autorizado, usuarioVO);
		} else if (notaFiscalSaidaVO.getWebServicesNFSEEnum().equals(WebServicesNFSEEnum.NATAL_RN_GERAR)) {
			getFacadeFactory().getNotaFiscalServicoEletronicaNatalFacade().enviarXmlEnvio(notaFiscalSaidaVO, configuracaoRespositoriArquivo, progressBar, autorizado, usuarioVO);
		} else if (notaFiscalSaidaVO.getWebServicesNFSEEnum().equals(WebServicesNFSEEnum.VITORIA_ES_GERAR)) {
			getFacadeFactory().getNotaFiscalServicoEletronicaVitoriaFacade().enviarXmlEnvio(notaFiscalSaidaVO, configuracaoRespositoriArquivo, progressBar, autorizado, usuarioVO);
		} else if (notaFiscalSaidaVO.getWebServicesNFSEEnum().equals(WebServicesNFSEEnum.SERRA_ES_GERAR)) {
			getFacadeFactory().getNotaFiscalServicoEletronicaSerraFacade().enviarXmlEnvio(notaFiscalSaidaVO, configuracaoRespositoriArquivo, progressBar, autorizado, usuarioVO);
		} else if (notaFiscalSaidaVO.getWebServicesNFSEEnum().equals(WebServicesNFSEEnum.CAMPO_GRANDE_MS_GERAR)) {
			getFacadeFactory().getNotaFiscalServicoEletronicaCampoGrandeFacade().enviarXmlEnvio(notaFiscalSaidaVO, configuracaoRespositoriArquivo, progressBar, autorizado, usuarioVO);
		} else if (notaFiscalSaidaVO.getWebServicesNFSEEnum().equals(WebServicesNFSEEnum.ANAPOLIS_GO_GERAR)) {
			getFacadeFactory().getNotaFiscalServicoEletronicaAnapolisFacade().enviarXmlEnvio(notaFiscalSaidaVO, configuracaoRespositoriArquivo, progressBar, autorizado, usuarioVO);
		} else if (notaFiscalSaidaVO.getWebServicesNFSEEnum().equals(WebServicesNFSEEnum.NFSE_WEBSERVICE)) {
			getFacadeFactory().getNotaFiscalServicoEletronicaFacade().enviarXmlEnvio(notaFiscalSaidaVO, configuracaoRespositoriArquivo, progressBar, autorizado, usuarioVO);
		}
	}

	public ComunicacaoInternaVO montarDadosEnvioEmailNFSE(NotaFiscalSaidaVO notaFiscalSaidaVO, ConfiguracaoGeralSistemaVO configuracaoEmail, PersonalizacaoMensagemAutomaticaVO mensagemTemplate, UsuarioVO usuarioLogado) throws Exception {
		ComunicacaoInternaVO comunicacaoInternaVO = new ComunicacaoInternaVO();
		ComunicadoInternoDestinatarioVO destinatario = new ComunicadoInternoDestinatarioVO();
		if (notaFiscalSaidaVO.getTipoPessoa().equals(TipoPessoa.PARCEIRO.getValor())) {
			comunicacaoInternaVO.setTipoDestinatario(notaFiscalSaidaVO.getTipoPessoa());
			destinatario.setDestinatarioParceiro(notaFiscalSaidaVO.getParceiroVO());
			if (!notaFiscalSaidaVO.getParceiroVO().getEmail().isEmpty()) {
				destinatario.setEmail(notaFiscalSaidaVO.getParceiroVO().getEmail());
				destinatario.setNome(notaFiscalSaidaVO.getParceiroVO().getNome());
			}
		} else {
			comunicacaoInternaVO.setAluno(notaFiscalSaidaVO.getPessoaVO());
			comunicacaoInternaVO.setTipoDestinatario(notaFiscalSaidaVO.getTipoPessoa());
			destinatario.setDestinatario(notaFiscalSaidaVO.getPessoaVO());
			if (!notaFiscalSaidaVO.getPessoaVO().getEmail().isEmpty()) {
				destinatario.setEmail(notaFiscalSaidaVO.getPessoaVO().getEmail());
				destinatario.setNome(notaFiscalSaidaVO.getPessoaVO().getNome());
			}
		}
		comunicacaoInternaVO.setTipoRemetente("FU");
		comunicacaoInternaVO.setEnviarEmail(Boolean.TRUE);
		comunicacaoInternaVO.setAssunto(mensagemTemplate.getAssunto());
		comunicacaoInternaVO.setMensagem(realizarSubstituicaoTagsMensagemNotaFiscal(notaFiscalSaidaVO, destinatario, mensagemTemplate.getMensagem()));

		comunicacaoInternaVO.setTipoComunicadoInterno(TipoComunicadoInterno.SOMENTE_LEITURA.getValor());
		comunicacaoInternaVO.setPrioridade(PrioridadeComunicadoInterno.NORMAL.getValor());
		comunicacaoInternaVO.setTipoMarketing(Boolean.FALSE);
		comunicacaoInternaVO.setTipoLeituraObrigatoria(Boolean.FALSE);
		comunicacaoInternaVO.setDigitarMensagem(Boolean.TRUE);
		comunicacaoInternaVO.setResponsavel(configuracaoEmail.getResponsavelPadraoComunicadoInterno());
		comunicacaoInternaVO.setUnidadeEnsino(notaFiscalSaidaVO.getUnidadeEnsinoVO());
		destinatario.setCiJaLida(Boolean.FALSE);
		destinatario.setTipoComunicadoInterno(TipoComunicadoInterno.SOMENTE_LEITURA.getValor());
		comunicacaoInternaVO.getComunicadoInternoDestinatarioVOs().add(destinatario);
		if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
			comunicacaoInternaVO.setEnviarSMS(true);
			comunicacaoInternaVO.setMensagemSMS(realizarSubstituicaoTagsMensagemNotaFiscal(notaFiscalSaidaVO, destinatario, mensagemTemplate.getMensagemSMS()));
		}
		return comunicacaoInternaVO;
	}
	
	public String realizarSubstituicaoTagsMensagemNotaFiscal(NotaFiscalSaidaVO notaFiscalSaidaVO, ComunicadoInternoDestinatarioVO destinatario, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_DESTINATARIO.name(), destinatario.getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NUMERO_NOTA.name(), notaFiscalSaidaVO.getNumeroNota());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_EMISSAO.name(), notaFiscalSaidaVO.getDataEmissao_Apresentar());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.LINK_DOWNLOAD.name(), notaFiscalSaidaVO.getLinkAcesso());
		return mensagemTexto;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public Integer consultarTotalRegistroPorNomeCliente(String valorConsulta, Date prmIni, Date prmFim, String situacao, UnidadeEnsinoVO unidadeEnsinoVO, CursoVO cursoVO, TurmaVO turmaVO, Boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		consultar(getIdEntidade(), true, usuarioLogado);

		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT count(notafiscalsaida.codigo) as total ");
		sqlStr.append(" FROM notafiscalsaida ");
		sqlStr.append(" LEFT JOIN pessoa as aluno on aluno.codigo = notafiscalsaida.aluno ");
		sqlStr.append(" LEFT JOIN pessoa as responsavel on responsavel.codigo = notafiscalsaida.responsavelfinanceiro ");
		sqlStr.append(" LEFT JOIN funcionario as funcionario on funcionario.codigo = notafiscalsaida.funcionario ");
		sqlStr.append(" LEFT JOIN pessoa as pessoaFuncionario on pessoaFuncionario.codigo = funcionario.pessoa ");
		sqlStr.append(" LEFT JOIN parceiro as parceiro on parceiro.codigo = notafiscalsaida.parceiro ");

		sqlStr.append(" WHERE 1=1 ");
		sqlStr.append(" AND CASE WHEN tipopessoa = 'AL' then aluno.nome ILIKE '%").append(valorConsulta).append("%' ELSE ");
		sqlStr.append(" CASE WHEN tipopessoa = 'RF' then responsavel.nome ILIKE '%").append(valorConsulta).append("%' ELSE ");
		sqlStr.append(" CASE WHEN tipopessoa = 'FU' then pessoaFuncionario.nome ILIKE '%").append(valorConsulta).append("%' ELSE ");
		sqlStr.append(" CASE WHEN tipopessoa = 'PA' then parceiro.nome ILIKE '%").append(valorConsulta).append("%' END END END END ");

		if (!situacao.trim().equals("-1")) {
			sqlStr.append(" and notafiscalsaida.situacao = '").append(situacao).append("' ");
		}

		if (unidadeEnsinoVO != null && unidadeEnsinoVO.getCodigo().intValue() != 0) {
			sqlStr.append(" and notafiscalsaida.unidadeensino = ").append(unidadeEnsinoVO.getCodigo()).append(" ");
		}

		if (prmIni != null && prmFim != null) {
			sqlStr.append(" and ((dataemissao>= '");
			sqlStr.append(UteisData.getDataJDBC(prmIni));
			sqlStr.append("') and (dataemissao <= '");
			sqlStr.append(UteisData.getDataJDBC(prmFim));
			sqlStr.append("')) ");
		} else if (prmIni != null) {
			sqlStr.append(" and ((dataemissao >= '");
			sqlStr.append(UteisData.getDataJDBC(prmIni));
			sqlStr.append("')) ");
		} else if (prmFim != null) {
			sqlStr.append(" and ((dataemissao <= '");
			sqlStr.append(UteisData.getDataJDBC(prmFim));
			sqlStr.append("')) ");
		}

		Integer totalRegistro = 0;
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (rs.next()) {
			totalRegistro = rs.getInt("total");
		}

		return totalRegistro;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public Integer consultarTotalRegistroPorMatricula(String valorConsulta, Date prmIni, Date prmFim, String situacao, UnidadeEnsinoVO unidadeEnsinoVO, CursoVO cursoVO, TurmaVO turmaVO, Boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		consultar(getIdEntidade(), true, usuarioLogado);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT count(distinct notafiscalsaida.codigo) as total ");
		sqlStr.append(" FROM notafiscalsaida ");
		sqlStr.append(" LEFT JOIN pessoa as responsavel on responsavel.codigo = notafiscalsaida.responsavelfinanceiro ");
		sqlStr.append(" LEFT JOIN funcionario as funcionario on funcionario.codigo = notafiscalsaida.funcionario ");
		sqlStr.append(" LEFT JOIN pessoa as pessoaFuncionario on pessoaFuncionario.codigo = funcionario.pessoa ");
		sqlStr.append(" LEFT JOIN parceiro as parceiro on parceiro.codigo = notafiscalsaida.parceiro ");
		sqlStr.append(" WHERE notafiscalsaida.matricula = '").append(valorConsulta).append("' ");

		if (!situacao.trim().equals("-1")) {
			sqlStr.append(" and notafiscalsaida.situacao = '").append(situacao).append("' ");
		}

		if (unidadeEnsinoVO != null && unidadeEnsinoVO.getCodigo().intValue() != 0) {
			sqlStr.append(" and notafiscalsaida.unidadeensino = ").append(unidadeEnsinoVO.getCodigo()).append(" ");
		}

		if (prmIni != null && prmFim != null) {
			sqlStr.append(" and ((dataemissao>= '");
			sqlStr.append(UteisData.getDataJDBC(prmIni));
			sqlStr.append("') and (dataemissao <= '");
			sqlStr.append(UteisData.getDataJDBC(prmFim));
			sqlStr.append("')) ");
		} else if (prmIni != null) {
			sqlStr.append(" and ((dataemissao >= '");
			sqlStr.append(UteisData.getDataJDBC(prmIni));
			sqlStr.append("')) ");
		} else if (prmFim != null) {
			sqlStr.append(" and ((dataemissao <= '");
			sqlStr.append(UteisData.getDataJDBC(prmFim));
			sqlStr.append("')) ");
		}

		Integer totalRegistro = 0;
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (rs.next()) {
			totalRegistro = rs.getInt("total");
		}

		return totalRegistro;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public HashMap<String, Double> consultarTotalNotaTotalISSQNPorNomeCliente(String valorConsulta, Date prmIni, Date prmFim, String situacao, UnidadeEnsinoVO unidadeEnsinoVO, CursoVO cursoVO, TurmaVO turmaVO, Boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		consultar(getIdEntidade(), true, usuarioLogado);

		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select sum(valortotalissqn) AS valortotalissqn, sum(valortotal) AS valortotal, sum(valortotalpis) as valortotalpis, sum(valortotalcofins) as valortotalcofins, sum(valortotalinss) as valortotalinss, sum(valortotalcsll) as valortotalcsll, sum(valortotalirrf) as valortotalirrf from (");
		sqlStr.append(" SELECT ");
		/*
		 * sqlStr.append(" ( "); sqlStr.
		 * append("		select distinct sum(notafiscalsaidaservico.totalissqn) from notafiscalsaidaservico "
		 * ); sqlStr.
		 * append("		where notafiscalsaidaservico.notafiscalsaida = notafiscalsaida.codigo "
		 * ); sqlStr.append("		group by notafiscalsaidaservico.notafiscalsaida");
		 * sqlStr.append(") AS totalissqn, ");
		 */
		sqlStr.append(" sum(notafiscalsaida.valortotal) as valortotal, notafiscalsaida.codigo, ");
		sqlStr.append(" sum(notafiscalsaida.valortotalissqn) as valortotalissqn, notafiscalsaida.codigo, ");
		sqlStr.append(" sum(notafiscalsaida.valortotalpis) as valortotalpis, ");
		sqlStr.append(" sum(notafiscalsaida.valortotalcofins) as valortotalcofins, ");

		sqlStr.append(" sum(notafiscalsaida.valortotalinss) as valortotalinss, ");
		sqlStr.append(" sum(notafiscalsaida.valortotalcsll) as valortotalcsll, ");
		sqlStr.append(" sum(notafiscalsaida.valortotalirrf) as valortotalirrf ");
		sqlStr.append(" FROM notafiscalsaida ");
		sqlStr.append(" LEFT JOIN pessoa as aluno on aluno.codigo = notafiscalsaida.aluno ");
		sqlStr.append(" LEFT JOIN pessoa as responsavel on responsavel.codigo = notafiscalsaida.responsavelfinanceiro ");
		sqlStr.append(" LEFT JOIN funcionario as funcionario on funcionario.codigo = notafiscalsaida.funcionario ");
		sqlStr.append(" LEFT JOIN pessoa as pessoaFuncionario on pessoaFuncionario.codigo = funcionario.pessoa ");
		sqlStr.append(" LEFT JOIN parceiro as parceiro on parceiro.codigo = notafiscalsaida.parceiro ");

		sqlStr.append(" WHERE 1=1 ");
		sqlStr.append(" AND CASE WHEN tipopessoa = 'AL' then aluno.nome ILIKE '%").append(valorConsulta).append("%' ELSE ");
		sqlStr.append(" CASE WHEN tipopessoa = 'RF' then responsavel.nome ILIKE '%").append(valorConsulta).append("%' ELSE ");
		sqlStr.append(" CASE WHEN tipopessoa = 'FU' then pessoaFuncionario.nome ILIKE '%").append(valorConsulta).append("%' ELSE ");
		sqlStr.append(" CASE WHEN tipopessoa = 'PA' then parceiro.nome ILIKE '%").append(valorConsulta).append("%' END END END END ");

		if (!situacao.trim().equals("-1")) {
			sqlStr.append(" and notafiscalsaida.situacao = '").append(situacao).append("' ");
		}

		if (unidadeEnsinoVO != null && unidadeEnsinoVO.getCodigo().intValue() != 0) {
			sqlStr.append(" and notafiscalsaida.unidadeensino = ").append(unidadeEnsinoVO.getCodigo()).append(" ");
		}

		if (prmIni != null && prmFim != null) {
			sqlStr.append(" and ((dataemissao>= '");
			sqlStr.append(UteisData.getDataJDBC(prmIni));
			sqlStr.append("') and (dataemissao <= '");
			sqlStr.append(UteisData.getDataJDBC(prmFim));
			sqlStr.append("')) ");
		} else if (prmIni != null) {
			sqlStr.append(" and ((dataemissao >= '");
			sqlStr.append(UteisData.getDataJDBC(prmIni));
			sqlStr.append("')) ");
		} else if (prmFim != null) {
			sqlStr.append(" and ((dataemissao <= '");
			sqlStr.append(UteisData.getDataJDBC(prmFim));
			sqlStr.append("')) ");
		}
		sqlStr.append(" group by notafiscalsaida.codigo");
		sqlStr.append(" ) AS t");

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		HashMap<String, Double> total = new HashMap<String, Double>(0);
		if (rs.next()) {
			total.put("valorTotal", rs.getDouble("valortotal"));
			total.put("valorTotalISSQN", rs.getDouble("valortotalissqn"));
			total.put("valorTotalPIS", rs.getDouble("valortotalpis"));
			total.put("valorTotalCOFINS", rs.getDouble("valortotalcofins"));
			total.put("valorTotalINSS", rs.getDouble("valortotalinss"));
			total.put("valorTotalCSLL", rs.getDouble("valortotalcsll"));
			total.put("valorTotalIRRF", rs.getDouble("valortotalirrf"));

			return total;
		} else {
			total.put("valorTotal", 0.0);
			total.put("valorTotalISSQN", 0.0);
			total.put("valorTotalPIS", 0.0);
			total.put("valorTotalCOFINS", 0.0);
			total.put("valorTotalINSS", 0.0);
			total.put("valorTotalCSLL", 0.0);
			total.put("valorTotalIRRF", 0.0);
			return total;
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public HashMap<String, Double> consultarTotalNotaTotalISSQNPorMatricula(String valorConsulta, Date prmIni, Date prmFim, String situacao, UnidadeEnsinoVO unidadeEnsinoVO, CursoVO cursoVO, TurmaVO turmaVO, Boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		consultar(getIdEntidade(), true, usuarioLogado);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select sum(valortotalissqn) AS valortotalissqn, sum(valortotal) AS valortotal, sum(valortotalpis) as valortotalpis, sum(valortotalcofins) as valortotalcofins, sum(valortotalinss) as valortotalinss, sum(valortotalcsll) as valortotalcsll, sum(valortotalirrf) as valortotalirrf from (");
		sqlStr.append(" SELECT ");
		/*sqlStr.append(" ( ");
		sqlStr.append("		select distinct sum(notafiscalsaidaservico.totalissqn) from notafiscalsaidaservico ");
		sqlStr.append("		where notafiscalsaidaservico.notafiscalsaida = notafiscalsaida.codigo ");
		sqlStr.append("		group by notafiscalsaidaservico.notafiscalsaida");
		sqlStr.append(") AS totalissqn, ");*/
		sqlStr.append(" sum(notafiscalsaida.valortotal) as valortotal, notafiscalsaida.codigo, ");
		sqlStr.append(" sum(notafiscalsaida.valortotalissqn) as valortotalissqn, notafiscalsaida.codigo, ");
		sqlStr.append(" sum(notafiscalsaida.valortotalpis) as valortotalpis, ");
		sqlStr.append(" sum(notafiscalsaida.valortotalcofins) as valortotalcofins, ");
		
		sqlStr.append(" sum(notafiscalsaida.valortotalinss) as valortotalinss, ");
		sqlStr.append(" sum(notafiscalsaida.valortotalcsll) as valortotalcsll, ");
		sqlStr.append(" sum(notafiscalsaida.valortotalirrf) as valortotalirrf ");
		sqlStr.append(" FROM notafiscalsaida ");
		sqlStr.append(" LEFT JOIN pessoa as responsavel on responsavel.codigo = notafiscalsaida.responsavelfinanceiro ");
		sqlStr.append(" LEFT JOIN funcionario as funcionario on funcionario.codigo = notafiscalsaida.funcionario ");
		sqlStr.append(" LEFT JOIN pessoa as pessoaFuncionario on pessoaFuncionario.codigo = funcionario.pessoa ");
		sqlStr.append(" LEFT JOIN parceiro as parceiro on parceiro.codigo = notafiscalsaida.parceiro ");
		sqlStr.append(" WHERE notafiscalsaida.matricula = '").append(valorConsulta).append("' ");

		if (!situacao.trim().equals("-1")) {
			sqlStr.append(" and notafiscalsaida.situacao = '").append(situacao).append("' ");
		}

		if (unidadeEnsinoVO != null && unidadeEnsinoVO.getCodigo().intValue() != 0) {
			sqlStr.append(" and notafiscalsaida.unidadeensino = ").append(unidadeEnsinoVO.getCodigo()).append(" ");
		}

		if (prmIni != null && prmFim != null) {
			sqlStr.append(" and ((dataemissao>= '");
			sqlStr.append(UteisData.getDataJDBC(prmIni));
			sqlStr.append("') and (dataemissao <= '");
			sqlStr.append(UteisData.getDataJDBC(prmFim));
			sqlStr.append("')) ");
		} else if (prmIni != null) {
			sqlStr.append(" and ((dataemissao >= '");
			sqlStr.append(UteisData.getDataJDBC(prmIni));
			sqlStr.append("')) ");
		} else if (prmFim != null) {
			sqlStr.append(" and ((dataemissao <= '");
			sqlStr.append(UteisData.getDataJDBC(prmFim));
			sqlStr.append("')) ");
		}
		sqlStr.append(" group by notafiscalsaida.codigo");
		sqlStr.append(" ) AS t");

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		HashMap<String, Double> total = new HashMap<String, Double>(0);
		if (rs.next()) {
			total.put("valorTotal", rs.getDouble("valortotal"));
			total.put("valorTotalISSQN", rs.getDouble("valortotalissqn"));
			total.put("valorTotalPIS", rs.getDouble("valortotalpis"));
			total.put("valorTotalCOFINS", rs.getDouble("valortotalcofins"));
			total.put("valorTotalINSS", rs.getDouble("valortotalinss"));
			total.put("valorTotalCSLL", rs.getDouble("valortotalcsll"));
			total.put("valorTotalIRRF", rs.getDouble("valortotalirrf"));
			return total;
		} else {
			total.put("valorTotal", 0.0);
			total.put("valorTotalISSQN", 0.0);
			total.put("valorTotalPIS", 0.0);
			total.put("valorTotalCOFINS", 0.0);
			total.put("valorTotalINSS", 0.0);
			total.put("valorTotalCSLL", 0.0);
			total.put("valorTotalIRRF", 0.0);
			return total;
		}
	}

	/**
	 * Resposável por executar a consulta das notas fiscais de saídas para geração
	 * do arquivo zip.
	 * 
	 * @author Wellington - 10 de dez de 2015
	 * @param campoConsulta
	 * @param valorConsulta
	 * @param dataInicio
	 * @param dataTermino
	 * @param situacao
	 * @param unidadeEnsinoVO
	 * @param cursoVO
	 * @param turmaVO
	 * @param limite
	 * @param pagina
	 * @param controlarAcesso
	 * @param nivelMontarDados
	 * @param usuarioLogado
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<NotaFiscalSaidaVO> consultarDadosParaCompactacaoNotasEnviadas(String campoConsulta, String valorConsulta, Date dataInicio, Date dataTermino, String situacao, UnidadeEnsinoVO unidadeEnsinoVO, CursoVO cursoVO, TurmaVO turmaVO, int limite, int pagina, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		if (campoConsulta.equals("codigo")) {
			int valorInt = 0;
			if (valorConsulta != null && !valorConsulta.equals("")) {
				valorInt = Integer.parseInt(valorConsulta);
			}
			return consultarPorCodigoParaCompactacaoNotasEnviadas(valorInt, dataInicio, dataTermino, situacao, limite, pagina, controlarAcesso, nivelMontarDados, usuarioLogado, unidadeEnsinoVO);
		}
		if (campoConsulta.equals("sacado")) {
			return consultarPorNomeClienteParaCompactacaoNotasEnviadas(valorConsulta, dataInicio, dataTermino, situacao, unidadeEnsinoVO, cursoVO, turmaVO, limite, pagina, controlarAcesso, nivelMontarDados, usuarioLogado);
		}
		if (campoConsulta.equals("matricula")) {
			return consultarPorMatriculaParaCompactacaoNotasEnviadas(valorConsulta, dataInicio, dataTermino, situacao, unidadeEnsinoVO, cursoVO, turmaVO, limite, pagina, controlarAcesso, nivelMontarDados, usuarioLogado);
		}
		if (campoConsulta.equals("numero")) {
			Long numero = 0L;
			if (valorConsulta != null && !valorConsulta.equals("")) {
				numero = Long.valueOf(valorConsulta);
			}
			return consultarPorNumeroParaCompactacaoNotasEnviadas(numero, dataInicio, dataTermino, situacao, unidadeEnsinoVO, cursoVO, turmaVO, limite, pagina, true, nivelMontarDados, usuarioLogado);
		}
		if (campoConsulta.equals("tipoPessoa")) {
			return consultarPorTipoPessoaParaCompactacaoNotasEnviadas(valorConsulta, dataInicio, dataTermino, situacao, unidadeEnsinoVO, cursoVO, turmaVO, limite, pagina, controlarAcesso, nivelMontarDados, usuarioLogado);
		}
		return new ArrayList<NotaFiscalSaidaVO>(0);
	}

	private List<NotaFiscalSaidaVO> consultarPorCodigoParaCompactacaoNotasEnviadas(Integer valorConsulta, Date prmIni, Date prmFim, String situacao, int limite, int pagina, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado, UnidadeEnsinoVO unidadeEnsinoVO) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuarioLogado);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT notafiscalsaida.codigo, notafiscalsaida.numero, notafiscalsaida.situacao, notafiscalsaida.xmlenvio, notafiscalsaida.unidadeensino, xmlnfeproc ");
		sqlStr.append("FROM notafiscalsaida ");
		sqlStr.append("WHERE notafiscalsaida.codigo = " + valorConsulta.intValue());
		if (!situacao.trim().equals("-1")) {
			sqlStr.append(" and notafiscalsaida.situacao = '").append(situacao).append("' ");
		}
		if (valorConsulta.intValue() == 0) {
			if (prmIni != null && prmFim != null) {
				sqlStr.append(" and ((dataemissao>= '");
				sqlStr.append(UteisData.getDataJDBC(prmIni));
				sqlStr.append("') and (dataemissao <= '");
				sqlStr.append(UteisData.getDataJDBC(prmFim));
				sqlStr.append("')) ");
			} else if (prmIni != null) {
				sqlStr.append(" and ((dataemissao >= '");
				sqlStr.append(UteisData.getDataJDBC(prmIni));
				sqlStr.append("')) ");
			} else if (prmFim != null) {
				sqlStr.append(" and ((dataemissao <= '");
				sqlStr.append(UteisData.getDataJDBC(prmFim));
				sqlStr.append("')) ");
			}
		}
		if (limite != 0) {
			sqlStr.append(" ORDER BY notafiscalsaida.numero DESC  limit ");
			sqlStr.append(limite);
			sqlStr.append(" offset ");
			sqlStr.append(pagina);
		} else {
			sqlStr.append(" ORDER BY notafiscalsaida.numero DESC ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaParaCompactacaoNotasEnviadas(tabelaResultado, nivelMontarDados, usuarioLogado, unidadeEnsinoVO);
	}

	private List<NotaFiscalSaidaVO> consultarPorNomeClienteParaCompactacaoNotasEnviadas(String valorConsulta, Date prmIni, Date prmFim, String situacao, UnidadeEnsinoVO unidadeEnsinoVO, CursoVO cursoVO, TurmaVO turmaVO, int limite, int pagina, Boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		consultar(getIdEntidade(), true, usuarioLogado);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT notafiscalsaida.codigo, notafiscalsaida.numero, notafiscalsaida.situacao, notafiscalsaida.xmlenvio, notafiscalsaida.unidadeensino, notafiscalsaida.xmlnfeproc ");
		sqlStr.append("FROM notafiscalsaida ");
		sqlStr.append("LEFT JOIN pessoa as aluno on aluno.codigo = notafiscalsaida.aluno ");
		sqlStr.append("LEFT JOIN pessoa as responsavel on responsavel.codigo = notafiscalsaida.responsavelfinanceiro ");
		sqlStr.append("LEFT JOIN funcionario as funcionario on funcionario.codigo = notafiscalsaida.funcionario ");
		sqlStr.append("LEFT JOIN pessoa as pessoaFuncionario on pessoaFuncionario.codigo = funcionario.pessoa ");
		sqlStr.append("LEFT JOIN parceiro as parceiro on parceiro.codigo = notafiscalsaida.parceiro ");
		sqlStr.append("WHERE 1=1 ");
		if (!valorConsulta.trim().isEmpty()) {
			sqlStr.append(" AND CASE WHEN tipopessoa = 'AL' then sem_acentos(aluno.nome) ILIKE sem_acentos('%").append(valorConsulta).append("%') ELSE ");
			sqlStr.append(" CASE WHEN tipopessoa = 'RF' then sem_acentos(responsavel.nome) ILIKE sem_acentos('%").append(valorConsulta).append("%') ELSE ");
			sqlStr.append(" CASE WHEN tipopessoa = 'FU' then sem_acentos(pessoaFuncionario.nome) ILIKE sem_acentos('%").append(valorConsulta).append("%') ELSE ");
			sqlStr.append(" CASE WHEN tipopessoa = 'PA' then sem_acentos(parceiro.nome) ILIKE sem_acentos('%").append(valorConsulta).append("%') END END END END ");
		}
		if (!situacao.trim().equals("-1")) {
			sqlStr.append(" and notafiscalsaida.situacao = '").append(situacao).append("' ");
		}

		if (unidadeEnsinoVO != null && unidadeEnsinoVO.getCodigo().intValue() != 0) {
			sqlStr.append(" and notafiscalsaida.unidadeensino = ").append(unidadeEnsinoVO.getCodigo()).append(" ");
		}

		if (prmIni != null && prmFim != null) {
			sqlStr.append(" and ((dataemissao>= '");
			sqlStr.append(UteisData.getDataJDBC(prmIni));
			sqlStr.append("') and (dataemissao <= '");
			sqlStr.append(UteisData.getDataJDBC(prmFim));
			sqlStr.append("')) ");
		} else if (prmIni != null) {
			sqlStr.append(" and ((dataemissao >= '");
			sqlStr.append(UteisData.getDataJDBC(prmIni));
			sqlStr.append("')) ");
		} else if (prmFim != null) {
			sqlStr.append(" and ((dataemissao <= '");
			sqlStr.append(UteisData.getDataJDBC(prmFim));
			sqlStr.append("')) ");
		}

		if (limite != 0) {
			sqlStr.append(" ORDER BY notafiscalsaida.numero DESC  limit ");
			sqlStr.append(limite);
			sqlStr.append(" offset ");
			sqlStr.append(pagina);
		} else {
			sqlStr.append(" ORDER BY notafiscalsaida.numero DESC ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaParaCompactacaoNotasEnviadas(tabelaResultado, nivelMontarDados, usuarioLogado, unidadeEnsinoVO);
	}

	private List<NotaFiscalSaidaVO> consultarPorMatriculaParaCompactacaoNotasEnviadas(String valorConsulta, Date prmIni, Date prmFim, String situacao, UnidadeEnsinoVO unidadeEnsinoVO, CursoVO cursoVO, TurmaVO turmaVO, int limite, int pagina, Boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		consultar(getIdEntidade(), true, usuarioLogado);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT notafiscalsaida.codigo, notafiscalsaida.numero, notafiscalsaida.situacao, notafiscalsaida.xmlenvio, notafiscalsaida.unidadeensino, notafiscalsaida.xmlnfeproc ");
		sqlStr.append("FROM notafiscalsaida ");
		sqlStr.append("WHERE notafiscalsaida.matricula = '").append(valorConsulta).append("' ");
		if (!situacao.trim().equals("-1")) {
			sqlStr.append(" and situacao = '").append(situacao).append("' ");
		}
		if (unidadeEnsinoVO != null && unidadeEnsinoVO.getCodigo().intValue() != 0) {
			sqlStr.append(" and unidadeensino = ").append(unidadeEnsinoVO.getCodigo()).append(" ");
		}
		if (prmIni != null && prmFim != null) {
			sqlStr.append(" and ((dataemissao>= '");
			sqlStr.append(UteisData.getDataJDBC(prmIni));
			sqlStr.append("') and (dataemissao <= '");
			sqlStr.append(UteisData.getDataJDBC(prmFim));
			sqlStr.append("')) ");
		} else if (prmIni != null) {
			sqlStr.append(" and ((dataemissao >= '");
			sqlStr.append(UteisData.getDataJDBC(prmIni));
			sqlStr.append("')) ");
		} else if (prmFim != null) {
			sqlStr.append(" and ((dataemissao <= '");
			sqlStr.append(UteisData.getDataJDBC(prmFim));
			sqlStr.append("')) ");
		}
		if (limite != 0) {
			sqlStr.append(" ORDER BY numero DESC  limit ");
			sqlStr.append(limite);
			sqlStr.append(" offset ");
			sqlStr.append(pagina);
		} else {
			sqlStr.append(" ORDER BY numero DESC ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaParaCompactacaoNotasEnviadas(tabelaResultado, nivelMontarDados, usuarioLogado, unidadeEnsinoVO);
	}

	private List<NotaFiscalSaidaVO> consultarPorNumeroParaCompactacaoNotasEnviadas(Long valorConsulta, Date prmIni, Date prmFim, String situacao, UnidadeEnsinoVO unidadeEnsinoVO, CursoVO cursoVO, TurmaVO turmaVO, int limite, int pagina, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuarioLogado);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT notafiscalsaida.codigo, notafiscalsaida.numero, notafiscalsaida.situacao, notafiscalsaida.xmlenvio, notafiscalsaida.unidadeensino, notafiscalsaida.xmlnfeproc ");
		sqlStr.append("FROM notafiscalsaida ");
		if (valorConsulta != 0) {
			sqlStr.append("WHERE notafiscalsaida.numero = ").append(valorConsulta).append(" ");
		} else {
			sqlStr.append("WHERE notafiscalsaida.numero >= ").append(valorConsulta).append(" ");
		}
		if (!situacao.trim().equals("-1")) {
			sqlStr.append(" and situacao = '").append(situacao).append("' ");
		}
		if (unidadeEnsinoVO != null && unidadeEnsinoVO.getCodigo().intValue() != 0) {
			sqlStr.append(" and unidadeensino = ").append(unidadeEnsinoVO.getCodigo()).append(" ");
		}
		if (prmIni != null && prmFim != null) {
			sqlStr.append(" and ((dataemissao>= '");
			sqlStr.append(UteisData.getDataJDBC(prmIni));
			sqlStr.append("') and (dataemissao <= '");
			sqlStr.append(UteisData.getDataJDBC(prmFim));
			sqlStr.append("')) ");
		} else if (prmIni != null) {
			sqlStr.append(" and ((dataemissao >= '");
			sqlStr.append(UteisData.getDataJDBC(prmIni));
			sqlStr.append("')) ");
		} else if (prmFim != null) {
			sqlStr.append(" and ((dataemissao <= '");
			sqlStr.append(UteisData.getDataJDBC(prmFim));
			sqlStr.append("')) ");
		}
		if (limite != 0) {
			sqlStr.append(" ORDER BY notafiscalsaida.numero DESC  limit ");
			sqlStr.append(limite);
			sqlStr.append(" offset ");
			sqlStr.append(pagina);
		} else {
			sqlStr.append(" ORDER BY notafiscalsaida.numero DESC ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaParaCompactacaoNotasEnviadas(tabelaResultado, nivelMontarDados, usuarioLogado, unidadeEnsinoVO);
	}

	private List<NotaFiscalSaidaVO> montarDadosConsultaParaCompactacaoNotasEnviadas(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado, UnidadeEnsinoVO unidadeEnsinoVO) throws Exception {
		List<NotaFiscalSaidaVO> vetResultado = new ArrayList<NotaFiscalSaidaVO>(0);
		unidadeEnsinoVO = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(unidadeEnsinoVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDadosParaCompactacaoNotasEnviadas(tabelaResultado, nivelMontarDados, usuarioLogado, unidadeEnsinoVO));
		}
		return vetResultado;
	}

	private NotaFiscalSaidaVO montarDadosParaCompactacaoNotasEnviadas(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuarioLogado, UnidadeEnsinoVO unidadeEnsinoVO) throws Exception {
		NotaFiscalSaidaVO obj = new NotaFiscalSaidaVO();
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setNumero(dadosSQL.getLong("numero"));
		obj.setSituacao(dadosSQL.getString("situacao"));
		obj.setXmlEnvio(dadosSQL.getString("xmlenvio"));
		obj.setXmlNfeProc(dadosSQL.getString("xmlnfeproc"));
		obj.setUnidadeEnsinoVO(unidadeEnsinoVO);
		obj.setNovoObj(false);
		return obj;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public synchronized void gravarNumeroNotaFiscal(NotaFiscalSaidaVO notaFiscal, ConfiguracaoNotaFiscalVO configuracao, long numeroNota, UsuarioVO usuarioLogado) throws Exception {
		JobExecutarRegistroNumeroNotaFiscal jobExecutarRegistroNumeroNotaFiscal = new JobExecutarRegistroNumeroNotaFiscal(notaFiscal, configuracao, numeroNota, usuarioLogado);
		Thread job = new Thread(jobExecutarRegistroNumeroNotaFiscal);
		job.start();
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public synchronized void gravarNumeroNotaFiscalViaJobComTransacaoIndependente(NotaFiscalSaidaVO notaFiscal, ConfiguracaoNotaFiscalVO configuracao, long numeroNota ,UsuarioVO usuario ) throws Exception {
		try {
			String sqlStr = "";
			if (AmbienteNfeEnum.PRODUCAO.equals(configuracao.getAmbienteNfeEnum())) {
				sqlStr = "UPDATE configuracaonotafiscal set numeronota=? WHERE codigo = " + configuracao.getCodigo() + " and numeronota < ?" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			} else {
				sqlStr = "UPDATE configuracaonotafiscal set numeronotahomologacao=? WHERE codigo = " + configuracao.getCodigo() + " and numeronotahomologacao < ?" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			}
			getConexao().getJdbcTemplate().update(sqlStr, new Object[] { numeroNota, numeroNota });
			notaFiscal.setNumero(numeroNota);
			sqlStr = "UPDATE notaFiscalSaida set numero=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sqlStr, new Object[] { numeroNota, notaFiscal.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 
	 * @param notaFiscalSaidaVO
	 * @param configuracaoRespositoriArquivo
	 * @param progressBar
	 * @param autorizado
	 * @param usuarioVO
	 * @throws Exception
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public synchronized void consultarNFSE(AplicacaoControle aplicacaoControle, NotaFiscalSaidaVO notaFiscalSaidaVO, boolean persistirObservacaoContribuinte, String observacaoContribuinte, ConfiguracaoGeralSistemaVO configuracaoRespositoriArquivo, ProgressBarVO progressBar, String autorizado, UsuarioVO usuarioVO) throws Exception {
		if (notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getTipoIntegracaoNfeEnum().equals(TipoIntegracaoNfeEnum.NFE)) {
			consultarNFE(notaFiscalSaidaVO, configuracaoRespositoriArquivo, progressBar, usuarioVO);
		} else {
			notaFiscalSaidaVO.setWebServicesNFSEEnum(WebServicesNFSEEnum.getEnumPorIdCidadeTipoAcaoServico(notaFiscalSaidaVO.getUnidadeEnsinoVO().getCidade().getCodigo(), TipoAcaoServicoNFSEEnum.ConsultarSituacaoLoteRps));
			CidadeVO cidade = notaFiscalSaidaVO.getUnidadeEnsinoVO().getCidadeNFSe();
			if (cidade.isWebserviceNFSeImplementado()) {
				notaFiscalSaidaVO.setWebServicesNFSEEnum(WebServicesNFSEEnum.NFSE_WEBSERVICE);
			}
			if (notaFiscalSaidaVO.getWebServicesNFSEEnum().equals(WebServicesNFSEEnum.NATAL_RN_CONSULTAR_SITUACAO_LOTE)) {
				getFacadeFactory().getNotaFiscalServicoEletronicaNatalFacade().consultarLoteRps(notaFiscalSaidaVO, configuracaoRespositoriArquivo, progressBar, autorizado, usuarioVO);
			} else if (notaFiscalSaidaVO.getWebServicesNFSEEnum().equals(WebServicesNFSEEnum.PALMAS_TO_CONSULTAR_SITUACAO_LOTE)) {
				getFacadeFactory().getNotaFiscalServicoEletronicaPalmasFacade().consultarLoteRps(notaFiscalSaidaVO, configuracaoRespositoriArquivo, progressBar, autorizado, usuarioVO);
			}  else if (notaFiscalSaidaVO.getWebServicesNFSEEnum().equals(WebServicesNFSEEnum.BELEM_PA_CONSULTAR_SITUACAO_LOTE)) {
				getFacadeFactory().getNotaFiscalServicoEletronicaBelemFacade().consultarLoteRps(notaFiscalSaidaVO, configuracaoRespositoriArquivo, progressBar, autorizado, usuarioVO);
			} else if (notaFiscalSaidaVO.getWebServicesNFSEEnum().equals(WebServicesNFSEEnum.CAMPO_GRANDE_MS_CONSULTAR_SITUACAO_LOTE)) {
				getFacadeFactory().getNotaFiscalServicoEletronicaCampoGrandeFacade().consultarLoteRps(notaFiscalSaidaVO, configuracaoRespositoriArquivo, progressBar, autorizado, usuarioVO);
			} else if (notaFiscalSaidaVO.getWebServicesNFSEEnum().equals(WebServicesNFSEEnum.ANAPOLIS_GO_CONSULTAR_SITUACAO_LOTE)) {
				getFacadeFactory().getNotaFiscalServicoEletronicaAnapolisFacade().consultarLoteRps(notaFiscalSaidaVO, configuracaoRespositoriArquivo, progressBar, autorizado, usuarioVO);
			} else if (notaFiscalSaidaVO.getWebServicesNFSEEnum().equals(WebServicesNFSEEnum.NFSE_WEBSERVICE)) {
				getFacadeFactory().getNotaFiscalServicoEletronicaFacade().consultarLoteRps(notaFiscalSaidaVO, configuracaoRespositoriArquivo, progressBar, autorizado, usuarioVO);
			} /*
			   * else
			   * if(notaFiscalSaidaVO.getWebServicesNFSEEnum().equals(WebServicesNFSEEnum.
			   * GOIANIA_GO_CONSULTAR)) {
			   * NotaFiscalServicoEletronicaGoiania.consultarLoteRps(notaFiscalSaidaVO,
			   * configuracaoRespositoriArquivo, progressBar, autorizado, usuarioVO); }
			   */
			gravarLinkAcesso(notaFiscalSaidaVO, usuarioVO);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public synchronized void gravarNumeroNotaFiscal(NotaFiscalSaidaVO notaFiscal, long numeroNota) throws Exception {
		String sqlStr = "";
		sqlStr = "UPDATE notaFiscalSaida set numero=? WHERE ((codigo = ?))";
		getConexao().getJdbcTemplate().update(sqlStr, new Object[] { numeroNota, notaFiscal.getCodigo() });
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private void consultarNFE(NotaFiscalSaidaVO notaFiscalSaidaVO, ConfiguracaoGeralSistemaVO conGeralSistemaVO, ProgressBarVO progressBar, UsuarioVO usuarioLogado) throws Exception {
		try {
			progressBar.setStatus("Consultando NF-e n° " + notaFiscalSaidaVO.getNumeroNota() + " ( " + progressBar.getProgresso() + " de " + progressBar.getMaxValue() + " ) ");
			String autorizado = "";
			autorizado = consultarLoteEnviar(notaFiscalSaidaVO, conGeralSistemaVO, usuarioLogado);
			notaFiscalSaidaVO.setMensagemRetorno(autorizado);
			if (!notaFiscalSaidaVO.getSituacao().equals(SituacaoNotaFiscalSaidaEnum.AUTORIZADA.getValor())) {
				progressBar.setStatus("NF-e n° " + notaFiscalSaidaVO.getNumeroNota() + " REJEITADA ( " + progressBar.getProgresso() + " de " + progressBar.getMaxValue() + " ) ");
			} else if (notaFiscalSaidaVO.getSituacao().equals(SituacaoNotaFiscalSaidaEnum.AUTORIZADA.getValor())) {
				progressBar.setStatus("NF-e n° " + notaFiscalSaidaVO.getNumeroNota() + " AUTORIZADA ( " + progressBar.getProgresso() + " de " + progressBar.getMaxValue() + " ) ");
			}
			notaFiscalSaidaVO.setNotaFiscalSaidaSelecionado(false);
			alterarMensagemRetorno(notaFiscalSaidaVO, usuarioLogado);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void gravarLinkAcesso(NotaFiscalSaidaVO notaFiscal, UsuarioVO usuarioLogado) throws Exception {
		try {
			String sql = "UPDATE NotaFiscalSaida set linkacesso=? WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(sql, new Object[] { notaFiscal.getLinkAcesso(), notaFiscal.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void gravarPISCOFINS(Integer codigo, Double valorPIS, Double valorCOFINS, UsuarioVO usuarioLogado) throws Exception {
		try {
			String sql = "UPDATE NotaFiscalSaida set valorTotalPIS=?, valorTotalCOFINS=? WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(sql, new Object[] { valorPIS, valorCOFINS, codigo });
		} catch (Exception e) {
			throw e;
		}
	}

	public void calcularImpostos(NotaFiscalSaidaVO nota, ConfiguracaoNotaFiscalVO configuracaoNotaFiscalVO) {
		calcularISSQN(nota, configuracaoNotaFiscalVO);
		calcularCOFINS(nota, configuracaoNotaFiscalVO);
		calcularPIS(nota, configuracaoNotaFiscalVO);
		calcularINSS(nota, configuracaoNotaFiscalVO);
		calcularIRRF(nota, configuracaoNotaFiscalVO);
		calcularCSLL(nota, configuracaoNotaFiscalVO);
		calcularCargaTributaria(nota, configuracaoNotaFiscalVO);
		calcularValorLiquidoNotaFiscal(nota, configuracaoNotaFiscalVO);
	}

	private void calcularISSQN(NotaFiscalSaidaVO nota, ConfiguracaoNotaFiscalVO configuracaoNotaFiscalVO) {
		nota.setAliquotaIssqn(configuracaoNotaFiscalVO.getIssqn());
		BigDecimal valorCalculo = BigDecimal.ZERO;
		for (NotaFiscalSaidaServicoVO item : nota.getNotaFiscalSaidaServicoVOs()) {
			valorCalculo = (new BigDecimal(Uteis.getValorTruncadoDeDoubleParaString(item.getPrecoUnitario() * (configuracaoNotaFiscalVO.getIssqn() / 100), 2)).add(valorCalculo));
			new BigDecimal(Uteis.getValorTruncadoDeDoubleParaString(item.getPrecoUnitario() * (configuracaoNotaFiscalVO.getIssqn() / 100), 2));
		}
		nota.setTotalIssqn(new Double(Uteis.getValorTruncadoDeDoubleParaString(valorCalculo.doubleValue(), 2)));

	}

	private void calcularCOFINS(NotaFiscalSaidaVO nota, ConfiguracaoNotaFiscalVO configuracaoNotaFiscalVO) {
		nota.setAliquotaCofins(configuracaoNotaFiscalVO.getCofins());
		nota.setValorTotalCOFINS(new Double(Uteis.getValorTruncadoDeDoubleParaString(nota.getValorTotal() * (configuracaoNotaFiscalVO.getCofins() / 100), 2)));
	}

	private void calcularPIS(NotaFiscalSaidaVO nota, ConfiguracaoNotaFiscalVO configuracaoNotaFiscalVO) {
		nota.setAliquotaPis(configuracaoNotaFiscalVO.getPis());
		nota.setValorTotalPIS(new Double(Uteis.getValorTruncadoDeDoubleParaString(nota.getValorTotal() * (configuracaoNotaFiscalVO.getPis() / 100), 2)));
	}

	private void calcularINSS(NotaFiscalSaidaVO nota, ConfiguracaoNotaFiscalVO configuracaoNotaFiscalVO) {
		nota.setAliquotaInss(configuracaoNotaFiscalVO.getInss());
		nota.setValorTotalINSS(new Double(Uteis.getValorTruncadoDeDoubleParaString(nota.getValorTotal() * (configuracaoNotaFiscalVO.getInss() / 100), 2)));
	}

	private void calcularIRRF(NotaFiscalSaidaVO nota, ConfiguracaoNotaFiscalVO configuracaoNotaFiscalVO) {
		nota.setAliquotaIr(configuracaoNotaFiscalVO.getAliquotaIR());
		nota.setValorTotalIRRF(new Double(Uteis.getValorTruncadoDeDoubleParaString(nota.getValorTotal() * (configuracaoNotaFiscalVO.getAliquotaIR() / 100), 2)));
	}

	private void calcularCSLL(NotaFiscalSaidaVO nota, ConfiguracaoNotaFiscalVO configuracaoNotaFiscalVO) {
		nota.setAliquotaCsll(configuracaoNotaFiscalVO.getCsll());
		nota.setValorTotalCSLL(new Double(Uteis.getValorTruncadoDeDoubleParaString(nota.getValorTotal() * (configuracaoNotaFiscalVO.getCsll() / 100), 2)));
	}
	
	private void calcularCargaTributaria(NotaFiscalSaidaVO nota, ConfiguracaoNotaFiscalVO configuracaoNotaFiscalVO) {
		if (Uteis.isAtributoPreenchido(nota.getPercentualCargaTributaria())) {
			nota.setPercentualCargaTributaria(new Double(Uteis.getValorTruncadoDeDoubleParaString((configuracaoNotaFiscalVO.getPercentualCargaTributaria() / 100), 4)));
			nota.setValorCargaTributaria(new Double(Uteis.getValorTruncadoDeDoubleParaString(nota.getValorTotal() * (configuracaoNotaFiscalVO.getPercentualCargaTributaria() / 100), 2)));
			nota.setFonteCargaTributaria(configuracaoNotaFiscalVO.getFonteCargaTributaria());
		}
	}

	private void calcularValorLiquidoNotaFiscal(NotaFiscalSaidaVO nota, ConfiguracaoNotaFiscalVO configuracaoNotaFiscalVO) {
		if (configuracaoNotaFiscalVO.getCodigoRegimeTributarioEnum().equals(CodigoRegimeTributarioEnum.SIMPLES_NACIONAL)) {
			nota.setValorLiquido(new Double(Uteis.getValorTruncadoDeDoubleParaString(nota.getValorTotal() - nota.getValorTotalPIS() - nota.getValorTotalCOFINS() - nota.getValorTotalINSS() - nota.getValorTotalIRRF() - nota.getValorTotalCSLL() - nota.getTotalIssqn(), 2)));
		} else {
			nota.setValorLiquido(new Double(Uteis.getValorTruncadoDeDoubleParaString(nota.getValorTotal() - nota.getValorTotalPIS() - nota.getValorTotalCOFINS() - nota.getValorTotalINSS() - nota.getValorTotalIRRF() - nota.getValorTotalCSLL(), 2)));
		}
	}

	@Override
	public List<ContaReceberVO> consultarContasReceberNotaFiscalSaida(MatriculaVO matriculaVO, Date dataInicio, Date dataFim, CursoVO cursoVO, TurmaVO turmaVO, String tipoValorConsultaContasAReceber,
	        FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, String situacaoContaReceber, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, Integer unidadeEnsino, UsuarioVO usuarioVO) throws Exception {
		List<ContaReceberVO> contasReceber = null;
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append(" select ");
		if (tipoValorConsultaContasAReceber.equals("valorCheio")) {
			sqlStr.append(" contareceber.valor  as valor, ");
		} else if (tipoValorConsultaContasAReceber.equals("valorAtual")) {
			sqlStr.append(" contareceber.valorrecebercalculado  as valor, ");
		} else if (tipoValorConsultaContasAReceber.equals("valorBaseDesconto")) {
			sqlStr.append(" (contareceber.valor - contareceber.valordescontocalculado)  as valor, ");
		}
		sqlStr.append(" contareceber.codigo, contareceber.notaFiscalSaidaServico, nfss.notaFiscalSaida, notaFiscalSaida.numero as numeroNotaFiscalSaida, notaFiscalSaida.mensagemRetorno as mensagemretorno, ");
		sqlStr.append(" contareceber.matriculaAluno as matricula, contareceber.tipoPessoa as tipopessoa, contareceber.tipoOrigem, contareceber.parcela, ");
		sqlStr.append(" case when contareceber.tipopessoa = 'PA' then matricula.aluno else contareceber.pessoa end as pessoa, ");
		sqlStr.append(" case when contareceber.tipopessoa = 'PA' then (select filiacao.pais from filiacao where  filiacao.aluno = matricula.aluno and filiacao.responsavelFinanceiro order by filiacao.pais  limit 1 ) else contareceber.responsavelfinanceiro end as responsavelfinanceiro, ");
		sqlStr.append(" parceiro.emitirNotaFiscalParaBeneficiario, contareceber.candidato, contareceber.funcionario, contareceber.fornecedor, contareceber.parceiro,");
		sqlStr.append(" notaFiscalSaida.situacao as situacaoNotaFiscalSaida, notaFiscalSaida.codigo as codigoNotaFiscalSaida, notaFiscalSaida.numeroRPS, notaFiscalSaida.lote, contareceber.descricaopagamento ");
		sqlStr.append("from contareceber ");
		sqlStr.append("inner join unidadeensino on unidadeensino.codigo = contareceber.unidadeensino ");
		sqlStr.append("left join matriculaperiodo on matriculaperiodo.codigo = contareceber.matriculaperiodo ");
		sqlStr.append("left join matricula on matricula.matricula = contareceber.matriculaaluno ");
		sqlStr.append("left join turma on turma.codigo = matriculaperiodo.turma ");
		sqlStr.append("left join turma turmareq on turmareq.codigo = contareceber.turma ");
		sqlStr.append("left join curso on curso.codigo = turma.curso ");
		sqlStr.append("left join curso cursoreq on cursoreq.codigo = turmareq.curso ");
		sqlStr.append("left join notaFiscalSaidaServico nfss on nfss.codigo = contareceber.notaFiscalSaidaServico ");
		sqlStr.append("left join notaFiscalSaida on notaFiscalSaida.codigo = nfss.notaFiscalSaida ");
		sqlStr.append("left join parceiro on parceiro.codigo = contareceber.parceiro ");
		sqlStr.append("where contareceber.unidadeEnsino = ").append(unidadeEnsino);
		sqlStr.append(" and case when notaFiscalSaida.codigo is not null then notaFiscalSaida.situacao in ('', 'RE', 'EN', 'CA') else notaFiscalSaida.codigo is null end ");
		if (!matriculaVO.getMatricula().equals("")) {
			sqlStr.append(" and contareceber.matriculaAluno = '").append(matriculaVO.getMatricula()).append("' ");
		}
		if (Uteis.isAtributoPreenchido(cursoVO)) {
			sqlStr.append(" and (curso.codigo = ").append(cursoVO.getCodigo()).append(" or cursoreq.codigo = ").append(cursoVO.getCodigo()).append(") ");
		}
		if (Uteis.isAtributoPreenchido(turmaVO)) {
			sqlStr.append(" and (turma.codigo = ").append(turmaVO.getCodigo()).append(" or turmareq.codigo = ").append(turmaVO.getCodigo()).append(" ) ");
		}
		sqlStr.append(" and ").append(adicionarFiltroTipoOrigemContaReceber(filtroRelatorioFinanceiroVO, "contareceber"));

		sqlStr.append(" and  contareceber.situacao = 'AR' ");
		if (Uteis.isAtributoPreenchido(dataInicio)) {
			sqlStr.append(" and dataVencimento >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
		}
		if (Uteis.isAtributoPreenchido(dataFim)) {
			sqlStr.append(" and dataVencimento <= '").append(Uteis.getDataJDBC(dataFim)).append("' ");
		}
		sqlStr.append(" order by contareceber.datavencimento ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());

		return montarDadosContasAReceber(rs, configuracaoFinanceiroVO, usuarioVO);
	}

	@Override
	public List<ContaReceberVO> consultarContasRecebidasValorCheioNotaFiscal(MatriculaVO matriculaVO, Date dataInicio, Date dataFim, CursoVO cursoVO, TurmaVO turmaVO, String tipoValorConsultaContasAReceber,
	        FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, String situacaoContaReceber, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, Integer unidadeEnsino, UsuarioVO usuarioVO) throws Exception {
		List<ContaReceberVO> contasReceber = null;
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append(" select contareceber.valor  as valor, ");
		sqlStr.append(" contareceber.codigo, contareceber.notaFiscalSaidaServico, nfss.notaFiscalSaida, notaFiscalSaida.numero as numeroNotaFiscalSaida, notaFiscalSaida.mensagemRetorno as mensagemretorno, ");
		sqlStr.append(" contareceber.matriculaAluno as matricula, contareceber.tipoPessoa as tipopessoa, contareceber.tipoOrigem, contareceber.parcela, ");
		sqlStr.append(" case when contareceber.tipopessoa = 'PA' then matricula.aluno else contareceber.pessoa end as pessoa, ");
		sqlStr.append(" case when contareceber.tipopessoa = 'PA' then (select filiacao.pais from filiacao where  filiacao.aluno = matricula.aluno and filiacao.responsavelFinanceiro order by filiacao.pais  limit 1 ) else contareceber.responsavelfinanceiro end as responsavelfinanceiro, ");
		sqlStr.append(" parceiro.emitirNotaFiscalParaBeneficiario, contareceber.candidato, contareceber.funcionario, contareceber.fornecedor, contareceber.parceiro,");
		sqlStr.append(" notaFiscalSaida.situacao as situacaoNotaFiscalSaida, notaFiscalSaida.codigo as codigoNotaFiscalSaida, notaFiscalSaida.numeroRPS, notaFiscalSaida.lote, contareceber.descricaopagamento ");
		sqlStr.append("from contareceber ");
		sqlStr.append("inner join unidadeensino on unidadeensino.codigo = contareceber.unidadeensino ");
		sqlStr.append("left join matriculaperiodo on matriculaperiodo.codigo = contareceber.matriculaperiodo ");
		sqlStr.append("left join matricula on matricula.matricula = contareceber.matriculaaluno ");
		sqlStr.append("left join turma on turma.codigo = matriculaperiodo.turma ");
		sqlStr.append("left join turma turmareq on turmareq.codigo = contareceber.turma ");
		sqlStr.append("left join curso on curso.codigo = turma.curso ");
		sqlStr.append("left join curso cursoreq on cursoreq.codigo = turmareq.curso ");
		sqlStr.append("left join notaFiscalSaidaServico nfss on nfss.codigo = contareceber.notaFiscalSaidaServico ");
		sqlStr.append("left join notaFiscalSaida on notaFiscalSaida.codigo = nfss.notaFiscalSaida ");
		sqlStr.append("left join parceiro on parceiro.codigo = contareceber.parceiro ");
		sqlStr.append("where contareceber.unidadeEnsino = ").append(unidadeEnsino);
		sqlStr.append(" and case when notaFiscalSaida.codigo is not null then notaFiscalSaida.situacao in ('', 'RE', 'EN', 'CA') else notaFiscalSaida.codigo is null end ");
		if (!matriculaVO.getMatricula().equals("")) {
			sqlStr.append(" and contareceber.matriculaAluno = '").append(matriculaVO.getMatricula()).append("' ");
		}
		if (Uteis.isAtributoPreenchido(cursoVO)) {
			sqlStr.append(" and (curso.codigo = ").append(cursoVO.getCodigo()).append(" or cursoreq.codigo = ").append(cursoVO.getCodigo()).append(") ");
		}
		if (Uteis.isAtributoPreenchido(turmaVO)) {
			sqlStr.append(" and (turma.codigo = ").append(turmaVO.getCodigo()).append(" or turmareq.codigo = ").append(turmaVO.getCodigo()).append(" ) ");
		}
		sqlStr.append(" and ").append(adicionarFiltroTipoOrigemContaReceber(filtroRelatorioFinanceiroVO, "contareceber"));

		sqlStr.append(" and  contareceber.situacao = 'RE' ");

		sqlStr.append(" and exists (select crr.codigo from contareceberrecebimento as crr ");
		sqlStr.append("inner join negociacaorecebimento on negociacaorecebimento.codigo = crr.negociacaorecebimento ");
		sqlStr.append("inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.negociacaorecebimento = negociacaorecebimento.codigo and contareceber.codigo = contarecebernegociacaorecebimento.contareceber ");
		sqlStr.append("inner join formapagamentonegociacaorecebimento on formapagamentonegociacaorecebimento.codigo = crr.formapagamentonegociacaorecebimento ");
		sqlStr.append(" where crr.contareceber = contareceber.codigo ");
		if (Uteis.isAtributoPreenchido(dataInicio)) {
			sqlStr.append(" and dataRecebimento >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
		}
		if (Uteis.isAtributoPreenchido(dataFim)) {
			sqlStr.append(" and dataRecebimento <= '").append(Uteis.getDataJDBC(dataFim)).append("' ");
		}
		sqlStr.append(" limit 1) ");

		sqlStr.append(" and not exists (select crr.codigo from contareceberrecebimento as crr ");
		sqlStr.append(" inner join negociacaorecebimento on negociacaorecebimento.codigo = crr.negociacaorecebimento ");
		sqlStr.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.negociacaorecebimento = negociacaorecebimento.codigo and contareceber.codigo = contarecebernegociacaorecebimento.contareceber ");
		sqlStr.append(" inner join formapagamentonegociacaorecebimento on formapagamentonegociacaorecebimento.codigo = crr.formapagamentonegociacaorecebimento ");
		sqlStr.append(" inner join notafiscalsaidaservico on notafiscalsaidaservico.codigo = crr.notafiscalsaidaservico ");
		sqlStr.append(" inner join notafiscalsaida on notafiscalsaida.codigo = notafiscalsaidaservico.notafiscalsaida ");
		sqlStr.append(" where crr.contareceber = contareceber.codigo and notafiscalsaida.situacao in ('AU', '', 'AE') ");
		sqlStr.append(" limit 1) ");

		sqlStr.append(" order by contareceber.datavencimento ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());

		return montarDadosContasAReceber(rs, configuracaoFinanceiroVO, usuarioVO);
	}

	public List<ContaReceberVO> montarDadosContasAReceber(SqlRowSet rs, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
		List<ContaReceberVO> contasReceber = new ArrayList<ContaReceberVO>(0);
		while (rs.next()) {
			ContaReceberVO contaReceberVO = new ContaReceberVO();
			contaReceberVO.setValor(rs.getDouble("valor"));
			contaReceberVO.setCodigo(rs.getInt("codigo"));
			contaReceberVO.getNotaFiscalSaidaServicoVO().setCodigo(rs.getInt("notaFiscalSaidaServico"));
			contaReceberVO.getNotaFiscalSaidaServicoVO().getNotaFiscalSaida().setNumero(rs.getLong("numeroNotaFiscalSaida"));
			contaReceberVO.getNotaFiscalSaidaServicoVO().getNotaFiscalSaida().setSituacao(rs.getString("situacaoNotaFiscalSaida"));
			contaReceberVO.getNotaFiscalSaidaServicoVO().getNotaFiscalSaida().setMensagemRetorno(rs.getString("mensagemretorno"));
			contaReceberVO.getNotaFiscalSaidaServicoVO().getNotaFiscalSaida().setCodigo(rs.getInt("codigoNotaFiscalSaida"));
			contaReceberVO.getNotaFiscalSaidaServicoVO().getNotaFiscalSaida().setNumeroRPS(rs.getInt("numeroRPS"));
			contaReceberVO.getNotaFiscalSaidaServicoVO().getNotaFiscalSaida().setLote(rs.getString("lote"));
			contaReceberVO.getMatriculaAluno().setMatricula(rs.getString("matricula"));
			contaReceberVO.setTipoPessoa(rs.getString("tipopessoa"));
			contaReceberVO.setTipoOrigem(rs.getString("tipoOrigem"));
			contaReceberVO.setParcela(rs.getString("parcela"));
			contaReceberVO.getPessoa().setCodigo(rs.getInt("pessoa"));
			contaReceberVO.getResponsavelFinanceiro().setCodigo(rs.getInt("responsavelfinanceiro"));
			contaReceberVO.getCandidato().setCodigo(rs.getInt("candidato"));
			contaReceberVO.getFuncionario().setCodigo(rs.getInt("funcionario"));
			contaReceberVO.getFornecedor().setCodigo(rs.getInt("fornecedor"));
			contaReceberVO.getParceiroVO().setCodigo(rs.getInt("parceiro"));
			contaReceberVO.getParceiroVO().setEmitirNotaFiscalParaBeneficiario(rs.getBoolean("emitirNotaFiscalParaBeneficiario"));
			contaReceberVO.setDescricaoPagamento(rs.getString("descricaopagamento"));
			contasReceber.add(contaReceberVO);
		}

		return contasReceber;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void cancelarNFSE(NotaFiscalSaidaVO notaFiscalSaidaVO, ConfiguracaoGeralSistemaVO conSistemaVO, UsuarioVO usuarioLogado) throws Exception {
		try {
			if (notaFiscalSaidaVO.getNotaFiscalSaidaServicoVOs().isEmpty()) {
				notaFiscalSaidaVO.getNotaFiscalSaidaServicoVOs().addAll(getFacadeFactory().getNotaFiscalSaidaServicoFacade().consultarNotaFiscalSaidaServicos(notaFiscalSaidaVO.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado));
			}
			getFacadeFactory().getNotaFiscalServicoEletronicaFacade().cancelar(notaFiscalSaidaVO, conSistemaVO, usuarioLogado);
		} catch(Exception e) {
			throw new Exception(notaFiscalSaidaVO.getMensagemRetorno());
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void gravarLogContasVinculadas(NotaFiscalSaidaVO notaFiscalSaidaVO, UsuarioVO usuarioLogado) throws Exception {
		try {
			StringBuilder logRecebidas = new StringBuilder();
			StringBuilder logReceber = new StringBuilder();
			for (NotaFiscalSaidaServicoVO servico : notaFiscalSaidaVO.getNotaFiscalSaidaServicoVOs()) {
				consultarContasReberRecebimentoPorNotaFiscalSaidaServico(servico);
				if (Uteis.isAtributoPreenchido(servico.getContaReceberRecebimentoVO().getCodigo())) {
					if (logRecebidas.toString().equals("")) {
						logRecebidas.append(" Conta Recebidas: ").append(servico.getContaReceberRecebimentoVO().getCodigo() + " ,");
					} else {
						logRecebidas.append(servico.getContaReceberRecebimentoVO().getCodigo() + " ,");
					}
				}
			}
			for (NotaFiscalSaidaServicoVO servico : notaFiscalSaidaVO.getNotaFiscalSaidaServicoVOs()) {
				consultarContasReberPorNotaFiscalSaidaServico(servico);
				if (Uteis.isAtributoPreenchido(servico.getContaReceberVO().getCodigo())) {
					if (logReceber.toString().equals("")) {
						logReceber.append(" Conta Receber: ").append(servico.getCodigo() + " ,");
					} else {
						logReceber.append(servico.getCodigo() + " ,");
					}
				}
			}
			String sql = "UPDATE notafiscalsaida set contasVinculadas=?  WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(sql, new Object[] { logReceber.append(logRecebidas), notaFiscalSaidaVO.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	public void consultarContasReberRecebimentoPorNotaFiscalSaidaServico(NotaFiscalSaidaServicoVO nfss) throws Exception {
		String sql = "SELECT codigo FROM contareceberrecebimento WHERE notafiscalsaidaservico = " + nfss.getCodigo();
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql);
		while (rs.next()) {
			nfss.getContaReceberRecebimentoVO().setCodigo(rs.getInt("codigo"));
		}
	}

	public void consultarContasReberPorNotaFiscalSaidaServico(NotaFiscalSaidaServicoVO nfss) throws Exception {
		String sql = "SELECT codigo FROM contareceber WHERE notafiscalsaidaservico = " + nfss.getCodigo();
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql);
		while (rs.next()) {
			nfss.getContaReceberVO().setCodigo(rs.getInt("codigo"));
		}
	}

	public boolean consultarExistenciaNotaFiscalComMesmoNumeroESerie(NotaFiscalSaidaVO nf, Long numero, int configuracaoNotafiscalSaida) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT notafiscalsaida.codigo FROM notafiscalsaida ");
		sqlStr.append(" inner join unidadeensino on unidadeensino.codigo = notafiscalsaida.unidadeensino ");
		sqlStr.append(" inner join configuracaonotafiscal on configuracaonotafiscal.codigo = unidadeensino.configuracaonotafiscal ");
		sqlStr.append(" WHERE notafiscalsaida.numero = ").append(numero);
		sqlStr.append(" and notafiscalsaida.serie = '").append(nf.getSerie()).append("' ");
		sqlStr.append(" and notafiscalsaida.codigo <> ").append(nf.getCodigo());
		sqlStr.append(" and configuracaonotafiscal.codigo = ").append(configuracaoNotafiscalSaida);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		while (rs.next()) {
			return true;
		}
		return false;
	}
	
	public boolean consultarExistenciaNotaFiscalComMesmoNumeroRPSESerie(NotaFiscalSaidaVO nf, Integer numeroRPS, int configuracaoNotafiscalSaida) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT notafiscalsaida.codigo FROM notafiscalsaida ");
		sqlStr.append(" inner join unidadeensino on unidadeensino.codigo = notafiscalsaida.unidadeensino ");
		sqlStr.append(" inner join configuracaonotafiscal on configuracaonotafiscal.codigo = unidadeensino.configuracaonotafiscal ");
		sqlStr.append(" WHERE notafiscalsaida.numerorps = ").append(numeroRPS);
		sqlStr.append(" and notafiscalsaida.serie = '").append(nf.getSerie()).append("' ");
		sqlStr.append(" and notafiscalsaida.codigo <> ").append(nf.getCodigo());
		sqlStr.append(" and configuracaonotafiscal.codigo = ").append(configuracaoNotafiscalSaida);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		while (rs.next()) {
			return true;
		}
		return false;
	}

	public String montarDescrimicaoNotaFiscalServico(String mensagem, Object[] parametros) throws Exception {
		mensagem = mensagem.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), parametros[0].toString());
		mensagem = mensagem.replaceAll(TagsMensagemAutomaticaEnum.NOME_CURSO.name(), parametros[1].toString().toUpperCase());
		mensagem = mensagem.replaceAll(TagsMensagemAutomaticaEnum.TIPO_PESSOA.name(), parametros[2].toString());
		mensagem = mensagem.replaceAll(TagsMensagemAutomaticaEnum.COMPETENCIA.name(), parametros[3].toString());
		mensagem = mensagem.replaceAll(TagsMensagemAutomaticaEnum.DESCRICAO_CONVENIO.name(), parametros[4].toString());
		return mensagem;
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List consultarPorTipoPessoa(String valorConsulta, Date prmIni, Date prmFim, String situacao, UnidadeEnsinoVO unidadeEnsinoVO, CursoVO cursoVO, TurmaVO turmaVO, int limite, int pagina, Boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado, String tipoPessoa) throws Exception {
		consultar(getIdEntidade(), true, usuarioLogado);

		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT notafiscalsaida.codigo, notafiscalsaida.serie, notafiscalsaida.linkacesso, notafiscalsaida.numero, notafiscalsaida.numerorps, notafiscalsaida.recibo, notafiscalsaida.matricula, notafiscalsaida.aluno, aluno.nome as \"aluno.nome\", aluno.codigo as \"aluno.codigo\", valortotal, notafiscalsaida.tipopessoa, dataemissao, ");
		sqlStr.append(" situacao, unidadeensino, mensagemretorno, lote, identificadorreceita, protocolo, responsavelfinanceiro, responsavel.nome as \"responsavel.nome\", responsavel.codigo as \"responsavel.codigo\", observacaocontribuinte, ");
		sqlStr.append(" funcionario.codigo as \"funcionario.codigo\", parceiro.codigo as \"parceiro.codigo\", parceiro.nome as \"parceiro.nome\", ");
		sqlStr.append(" notafiscalsaida.valortotalpis as valortotalpis, notafiscalsaida.valortotalcofins as valortotalcofins, ");
		sqlStr.append(" notafiscalsaida.aliquotaissqn as aliquotaissqn, notafiscalsaida.aliquotapis as aliquotapis, notafiscalsaida.aliquotainss as aliquotainss, notafiscalsaida.aliquotacsll as aliquotacsll, notafiscalsaida.aliquotair as aliquotair, ");
		sqlStr.append(" notafiscalsaida.valortotalinss as valortotalinss, notafiscalsaida.valortotalirrf as valortotalirrf, notafiscalsaida.valortotalcsll as valortotalcsll, notafiscalsaida.valortotalissqn as valortotalissqn, notafiscalsaida.valorliquido as valorliquido, ");
		sqlStr.append(" notafiscalsaida.endereco as endereco, notafiscalsaida.setor as setor, notafiscalsaida.nomeRazaoSocial as nomeRazaoSocial, notafiscalsaida.cnpjCpf as cnpjCpf, notafiscalsaida.cep as cep, notafiscalsaida.inscricaoEstadual as inscricaoEstadual, notafiscalsaida.uf as uf, notafiscalsaida.telefone as telefone, notafiscalsaida.municipio as municipio, ");
		sqlStr.append(" notafiscalsaida.informacaoFiliacaoApresentarDanfe as informacaoFiliacaoApresentarDanfe, notafiscalsaida.issretido, notafiscalsaida.naturezaOperacaoEnum, notafiscalsaida.isIncentivadorCultural, notafiscalsaida.regimeEspecialTributacaoEnum, notafiscalsaida.codigoCNAE, notafiscalsaida.percentualCargaTributaria, notafiscalsaida.valorCargaTributaria, notafiscalsaida.fonteCargaTributaria, ");
		sqlStr.append(" ( select distinct sum(totalissqn) from notafiscalsaidaservico where notafiscalsaidaservico.notafiscalsaida = notafiscalsaida.codigo ) AS totalissqn ");
		sqlStr.append(" FROM notafiscalsaida ");
		sqlStr.append(" LEFT JOIN pessoa as aluno on aluno.codigo = notafiscalsaida.aluno ");
		sqlStr.append(" LEFT JOIN pessoa as responsavel on responsavel.codigo = notafiscalsaida.responsavelfinanceiro ");
		sqlStr.append(" LEFT JOIN funcionario as funcionario on funcionario.codigo = notafiscalsaida.funcionario ");
		sqlStr.append(" LEFT JOIN pessoa as pessoaFuncionario on pessoaFuncionario.codigo = funcionario.pessoa ");
		sqlStr.append(" LEFT JOIN parceiro as parceiro on parceiro.codigo = notafiscalsaida.parceiro ");

		sqlStr.append(" WHERE 1=1 ");
		if (!tipoPessoa.trim().isEmpty()) {
			sqlStr.append(" AND tipopessoa = '").append(tipoPessoa).append("'");
		}
		if (!situacao.trim().equals("-1")) {
			sqlStr.append(" and notafiscalsaida.situacao = '").append(situacao).append("' ");
		}

		if (unidadeEnsinoVO != null && unidadeEnsinoVO.getCodigo().intValue() != 0) {
			sqlStr.append(" and notafiscalsaida.unidadeensino = ").append(unidadeEnsinoVO.getCodigo()).append(" ");
		}

		if (prmIni != null && prmFim != null) {
			sqlStr.append(" and ((dataemissao>= '");
			sqlStr.append(UteisData.getDataJDBC(prmIni));
			sqlStr.append("') and (dataemissao <= '");
			sqlStr.append(UteisData.getDataJDBC(prmFim));
			sqlStr.append("')) ");
		} else if (prmIni != null) {
			sqlStr.append(" and ((dataemissao >= '");
			sqlStr.append(UteisData.getDataJDBC(prmIni));
			sqlStr.append("')) ");
		} else if (prmFim != null) {
			sqlStr.append(" and ((dataemissao <= '");
			sqlStr.append(UteisData.getDataJDBC(prmFim));
			sqlStr.append("')) ");
		}

		if (limite != 0) {
			sqlStr.append(" ORDER BY notafiscalsaida.numero DESC  limit ");
			sqlStr.append(limite);
			sqlStr.append(" offset ");
			sqlStr.append(pagina);
		} else {
			sqlStr.append(" ORDER BY notafiscalsaida.numero DESC ");
		}

		List listaResultado = new ArrayList(0);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		while (tabelaResultado.next()) {
			listaResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuarioLogado));
		}
		return listaResultado;
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public Integer consultarTotalRegistroPorTipoPessoa(String valorConsulta, Date prmIni, Date prmFim, String situacao, UnidadeEnsinoVO unidadeEnsinoVO, CursoVO cursoVO, TurmaVO turmaVO, Boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado, String tipoPessoa) throws Exception {
		consultar(getIdEntidade(), true, usuarioLogado);

		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT count(notafiscalsaida.codigo) as total ");
		sqlStr.append(" FROM notafiscalsaida ");
		sqlStr.append(" LEFT JOIN pessoa as aluno on aluno.codigo = notafiscalsaida.aluno ");
		sqlStr.append(" LEFT JOIN pessoa as responsavel on responsavel.codigo = notafiscalsaida.responsavelfinanceiro ");
		sqlStr.append(" LEFT JOIN funcionario as funcionario on funcionario.codigo = notafiscalsaida.funcionario ");
		sqlStr.append(" LEFT JOIN pessoa as pessoaFuncionario on pessoaFuncionario.codigo = funcionario.pessoa ");
		sqlStr.append(" LEFT JOIN parceiro as parceiro on parceiro.codigo = notafiscalsaida.parceiro ");

		sqlStr.append(" WHERE 1=1 ");
		
		if (!tipoPessoa.trim().isEmpty()) {
			sqlStr.append(" AND tipopessoa = '").append(tipoPessoa).append("'");
		}

		if (!situacao.trim().equals("-1")) {
			sqlStr.append(" and notafiscalsaida.situacao = '").append(situacao).append("' ");
		}

		if (unidadeEnsinoVO != null && unidadeEnsinoVO.getCodigo().intValue() != 0) {
			sqlStr.append(" and notafiscalsaida.unidadeensino = ").append(unidadeEnsinoVO.getCodigo()).append(" ");
		}

		if (prmIni != null && prmFim != null) {
			sqlStr.append(" and ((dataemissao>= '");
			sqlStr.append(UteisData.getDataJDBC(prmIni));
			sqlStr.append("') and (dataemissao <= '");
			sqlStr.append(UteisData.getDataJDBC(prmFim));
			sqlStr.append("')) ");
		} else if (prmIni != null) {
			sqlStr.append(" and ((dataemissao >= '");
			sqlStr.append(UteisData.getDataJDBC(prmIni));
			sqlStr.append("')) ");
		} else if (prmFim != null) {
			sqlStr.append(" and ((dataemissao <= '");
			sqlStr.append(UteisData.getDataJDBC(prmFim));
			sqlStr.append("')) ");
		}

		Integer totalRegistro = 0;
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (rs.next()) {
			totalRegistro = rs.getInt("total");
		}

		return totalRegistro;
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public HashMap<String, Double> consultarTotalNotaTotalISSQNPorTipoPessoa(String valorConsulta, Date prmIni, Date prmFim, String situacao, UnidadeEnsinoVO unidadeEnsinoVO, CursoVO cursoVO, TurmaVO turmaVO, Boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado, String tipoPessoa) throws Exception {
		consultar(getIdEntidade(), true, usuarioLogado);

		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select sum(valortotalissqn) AS valortotalissqn, sum(valortotal) AS valortotal, sum(valortotalpis) as valortotalpis, sum(valortotalcofins) as valortotalcofins, sum(valortotalinss) as valortotalinss, sum(valortotalcsll) as valortotalcsll, sum(valortotalirrf) as valortotalirrf from (");
		sqlStr.append(" SELECT ");
		/*sqlStr.append(" ( ");
		sqlStr.append("		select distinct sum(notafiscalsaidaservico.totalissqn) from notafiscalsaidaservico ");
		sqlStr.append("		where notafiscalsaidaservico.notafiscalsaida = notafiscalsaida.codigo ");
		sqlStr.append("		group by notafiscalsaidaservico.notafiscalsaida");
		sqlStr.append(") AS totalissqn, ");*/
		sqlStr.append(" sum(notafiscalsaida.valortotal) as valortotal, notafiscalsaida.codigo, ");
		sqlStr.append(" sum(notafiscalsaida.valortotalissqn) as valortotalissqn, notafiscalsaida.codigo, ");
		sqlStr.append(" sum(notafiscalsaida.valortotalpis) as valortotalpis, ");
		sqlStr.append(" sum(notafiscalsaida.valortotalcofins) as valortotalcofins, ");
		
		sqlStr.append(" sum(notafiscalsaida.valortotalinss) as valortotalinss, ");
		sqlStr.append(" sum(notafiscalsaida.valortotalcsll) as valortotalcsll, ");
		sqlStr.append(" sum(notafiscalsaida.valortotalirrf) as valortotalirrf ");
		sqlStr.append(" FROM notafiscalsaida ");
		sqlStr.append(" LEFT JOIN pessoa as aluno on aluno.codigo = notafiscalsaida.aluno ");
		sqlStr.append(" LEFT JOIN pessoa as responsavel on responsavel.codigo = notafiscalsaida.responsavelfinanceiro ");
		sqlStr.append(" LEFT JOIN funcionario as funcionario on funcionario.codigo = notafiscalsaida.funcionario ");
		sqlStr.append(" LEFT JOIN pessoa as pessoaFuncionario on pessoaFuncionario.codigo = funcionario.pessoa ");
		sqlStr.append(" LEFT JOIN parceiro as parceiro on parceiro.codigo = notafiscalsaida.parceiro ");

		sqlStr.append(" WHERE 1=1 ");
		
		if (!tipoPessoa.trim().isEmpty()) {
			sqlStr.append(" AND tipopessoa = '").append(tipoPessoa).append("'");
		}

		if (!situacao.trim().equals("-1")) {
			sqlStr.append(" and notafiscalsaida.situacao = '").append(situacao).append("' ");
		}

		if (unidadeEnsinoVO != null && unidadeEnsinoVO.getCodigo().intValue() != 0) {
			sqlStr.append(" and notafiscalsaida.unidadeensino = ").append(unidadeEnsinoVO.getCodigo()).append(" ");
		}

		if (prmIni != null && prmFim != null) {
			sqlStr.append(" and ((dataemissao>= '");
			sqlStr.append(UteisData.getDataJDBC(prmIni));
			sqlStr.append("') and (dataemissao <= '");
			sqlStr.append(UteisData.getDataJDBC(prmFim));
			sqlStr.append("')) ");
		} else if (prmIni != null) {
			sqlStr.append(" and ((dataemissao >= '");
			sqlStr.append(UteisData.getDataJDBC(prmIni));
			sqlStr.append("')) ");
		} else if (prmFim != null) {
			sqlStr.append(" and ((dataemissao <= '");
			sqlStr.append(UteisData.getDataJDBC(prmFim));
			sqlStr.append("')) ");
		}
		sqlStr.append(" group by notafiscalsaida.codigo");
		sqlStr.append(" ) AS t");

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		HashMap<String, Double> total = new HashMap<String, Double>(0);
		if (rs.next()) {
			total.put("valorTotal", rs.getDouble("valortotal"));
			total.put("valorTotalISSQN", rs.getDouble("valortotalissqn"));
			total.put("valorTotalPIS", rs.getDouble("valortotalpis"));
			total.put("valorTotalCOFINS", rs.getDouble("valortotalcofins"));
			total.put("valorTotalINSS", rs.getDouble("valortotalinss"));
			total.put("valorTotalCSLL", rs.getDouble("valortotalcsll"));
			total.put("valorTotalIRRF", rs.getDouble("valortotalirrf"));			
			
			return total;
		} else {
			total.put("valorTotal", 0.0);
			total.put("valorTotalISSQN", 0.0);
			total.put("valorTotalPIS", 0.0);
			total.put("valorTotalCOFINS", 0.0);
			total.put("valorTotalINSS", 0.0);
			total.put("valorTotalCSLL", 0.0);
			total.put("valorTotalIRRF", 0.0);
			return total;
		}
	}
	
	public String consultarStatusServicoNFeWebservice(UnidadeEnsinoVO unidadeEnsinoVO, UsuarioVO usuarioLogado) throws Exception {
		ConfiguracaoGeralSistemaVO conGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesWebserviceNFe();
		String url = conGeralSistemaVO.getUrlWebserviceNFe() + "/servico/statusServico/" + unidadeEnsinoVO.getConfiguracaoNotaFiscalVO().getTokenWebserviceNFe();
		System.out.println("STATUS-NFE-> Chamando Webservice: " + url);
		RestTemplate restTemplate = new RestTemplate();
    	RequestEntity<Void> request = RequestEntity.get(new URI(url)).accept(MediaType.TEXT_PLAIN).build();
    	ResponseEntity<String> response = restTemplate.exchange(request, String.class);
    	System.out.println("STATUS-NFE-> Retorno: " + response.getBody());
    	return response.getBody();
    }
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public String consultarLoteEnviarWebservice(NotaFiscalSaidaVO notaFiscalSaidaVO, ConfiguracaoGeralSistemaVO conGeralSistemaVO, UsuarioVO usuarioLogado) throws Exception {
		try {
			String url = conGeralSistemaVO.getUrlWebserviceNFe() + "/servico/consultaNFe/" +
					notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getTokenWebserviceNFe();
			RestTemplate restTemplate = new RestTemplate();
			NfeVO nfeVO = new NfeVO();
			nfeVO.setCnpjEmit(notaFiscalSaidaVO.getUnidadeEnsinoVO().getCNPJ());
			nfeVO.setSerie(notaFiscalSaidaVO.getSerie());
			nfeVO.setNNF(notaFiscalSaidaVO.getNumero().toString());
			RequestEntity<NfeVO> request = RequestEntity.post(new URI(url)).contentType(MediaType.APPLICATION_JSON).body(nfeVO);
	    	ResponseEntity<NfeVO> response = restTemplate.exchange(request, NfeVO.class);
	    	NfeVO nfeAux = response.getBody();
	    	nfeAux.getChaveAcesso(); // persistir ???
	    	nfeAux.getIdNfe();
//	    	notaFiscalSaidaVO.setIdentificadorReceita(nfeAux.getIdNfe());
//			notaFiscalSaidaVO.setRecibo(UteisNfe.getRecido(nfeAux.getRecibo()));
//			gravarRecibo(notaFiscalSaidaVO.getCodigo(), notaFiscalSaidaVO.getRecibo(), usuarioLogado);
	    	notaFiscalSaidaVO.setMensagemRetorno(nfeAux.getMotivo());
			alterarMensagemRetorno(notaFiscalSaidaVO, usuarioLogado);
			gravarSituacaoNotaComBaseNoStatusWebservice(notaFiscalSaidaVO, nfeAux, usuarioLogado);
			return notaFiscalSaidaVO.getMensagemRetorno();
		} catch (Exception e) {
			notaFiscalSaidaVO.setSituacao(SituacaoNotaFiscalSaidaEnum.REJEITADA.getValor());
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public String enviarNFeWebservice(AplicacaoControle aplicacaoControle, NotaFiscalSaidaVO notaSaidaVO, boolean persistirObservacaoContribuinte, String observacaoContribuinte, ConfiguracaoGeralSistemaVO configuracaoRespositoriArquivo, ConfiguracaoGeralSistemaVO configuracaoEmail, ProgressBarVO progressBar, Integer unidadeEnsino, UsuarioVO usuarioLogado) throws Exception {
		boolean novoObj = notaSaidaVO.isNovoObj();
		Integer codigoConfNFE = notaSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCodigo();

		boolean debug = false;
		if (notaSaidaVO.getNotaFiscalSaidaSelecionado()) {
			try {
				/**
				 * Responsável por validar se notas já estão sendo emitidas para a configuração
				 * de nota fiscal para esta unidade de ensino selecionada
				 */
				aplicacaoControle.executarEnvioNotasFiscais(codigoConfNFE, true, false, true);
			} catch (AcessoException e) {
				if (debug) {
					System.out.println("NFE_LOG: Nota Fiscal para esta configuração já está sendo emitida!");
				}
				throw e;
			}
			try {

				/**
				 * Responsável por verificar se Nota Fiscal Saída já foi emitida para aquela
				 * Conta Receber Recebimento, caso seja verdadeiro é retornado o objeto
				 * preenchido com as informações de Emissão.
				 */
				if (!(notaSaidaVO.getSituacao().equals(SituacaoNotaFiscalSaidaEnum.REJEITADA.getValor())) && verificarNotaFiscalSaidaJaEmitida(notaSaidaVO, usuarioLogado)) {
					notaSaidaVO.setNotaFiscalSaidaSelecionado(false);
					if (debug) {
						System.out.println("NFE_LOG: Retornando pois nota já está Rejeitada!");
					}
					return "";
				}

				if (notaSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO() == null || notaSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCodigo().intValue() == 0) {
					if (debug) {
						System.out.println("NFE_LOG: Lançando exception devido não existir configuração nota fiscal na unidade de ensino!");
					}
					throw new AcessoException("É obrigatório possuir na Unidade de Ensino \"" + notaSaidaVO.getUnidadeEnsinoVO().getNome() + "\" uma Configuração de Nota Fiscal.");
				}
				notaSaidaVO.setResponsavel(usuarioLogado);
				notaSaidaVO.setObservacaoContribuinte(observacaoContribuinte);
				if (notaSaidaVO.getCodigo().intValue() == 0) {
					registrarLoteNotaFiscal(notaSaidaVO);
					if (AmbienteNfeEnum.PRODUCAO.equals(notaSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getAmbienteNfeEnum())) {
						notaSaidaVO.setLote(String.valueOf(notaSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getLote()));
					} else {
						notaSaidaVO.setLote(String.valueOf(notaSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getLoteHomologacao()));
					}
					CidadeVO cidade = notaSaidaVO.getUnidadeEnsinoVO().getCidadeNFSe();
					if (cidade.isWebserviceNFSeImplementado()) {
						try {
							registrarNumeroRPS(notaSaidaVO, notaSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO());
						} catch (Exception e) {
							progressBar.setStatus("NF-e n° " + notaSaidaVO.getNumeroNota() + " REJEITADA ( " + progressBar.getProgresso() + " de " + progressBar.getMaxValue() + " ) ");
							notaSaidaVO.setMensagemRetorno("Erro ao registrar Numero RPS ( " + e.getMessage() + " )");
							if (debug) {
								System.out.println("NFE_LOG: Lançando exception devido falha ao registrar número RPS!");
							}
							throw e;
						}
					}
					try {
						incluir(notaSaidaVO, usuarioLogado);
					} catch (Exception e) {
						progressBar.setStatus("NF-e n° " + notaSaidaVO.getNumeroNota() + " REJEITADA ( " + progressBar.getProgresso() + " de " + progressBar.getMaxValue() + " ) ");
						notaSaidaVO.setMensagemRetorno("Erro ao incluir Nota ( " + e.getMessage() + " )");
						if (debug) {
							System.out.println("NFE_LOG: Lançando exception devido falha ao incluir nota!");
						}
						throw e;
					}
					if (notaSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getTipoIntegracaoNfeEnum().equals(TipoIntegracaoNfeEnum.NFE)) {
						try {
							registrarNumeroNotaFiscal(notaSaidaVO, notaSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO(),usuarioLogado);
						} catch (Exception e) {
							progressBar.setStatus("NF-e n° " + notaSaidaVO.getNumeroNota() + " REJEITADA ( " + progressBar.getProgresso() + " de " + progressBar.getMaxValue() + " ) ");
							notaSaidaVO.setMensagemRetorno("Erro ao registrar Numero da Nota ( " + e.getMessage() + " )");
							if (debug) {
								System.out.println("NFE_LOG: Lançando exception devido falha ao registrar número da nota!");
							}
							throw e;
						}
					}
				} else {
					CidadeVO cidade = notaSaidaVO.getUnidadeEnsinoVO().getCidadeNFSe();
					if (cidade.isWebserviceNFSeImplementado() && notaSaidaVO.getNumeroRPS().equals(0)) {
						try {
							registrarNumeroRPS(notaSaidaVO, notaSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO());
						} catch (Exception e) {
							progressBar.setStatus("NF-e n° " + notaSaidaVO.getNumeroNota() + " REJEITADA ( " + progressBar.getProgresso() + " de " + progressBar.getMaxValue() + " ) ");
							notaSaidaVO.setMensagemRetorno("Erro ao registrar Numero RPS ( " + e.getMessage() + " )");
							if (debug) {
								System.out.println("NFE_LOG: Lançando exception devido falha ao registrar número RPS!");
							}
							throw e;
						}
					}
					alterar(notaSaidaVO, usuarioLogado);
				}

				progressBar.incrementar();
				progressBar.setStatus("Enviando NF-e n° " + notaSaidaVO.getNumeroNota() + " ( " + progressBar.getProgresso() + " de " + progressBar.getMaxValue() + " ) ");
				if (notaSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getTipoIntegracaoNfeEnum().equals(TipoIntegracaoNfeEnum.NFE)) {
					NfeVO nfeVO = new NfeVO();
					try {
						if (debug) {
							System.out.println("NFE_LOG: Iniciando envio de nota fiscal número " + notaSaidaVO.getNumeroRPS());
						}
						inicializarDadosObjetoEnvio(nfeVO, notaSaidaVO, notaSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO(),configuracaoRespositoriArquivo, usuarioLogado);
						if (persistirObservacaoContribuinte) {
							alterarObservacaoContribuinte(notaSaidaVO, usuarioLogado);
						}
						/*nfeVO.setPastaArquivoXML(configuracaoRespositoriArquivo.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.NFE.getValue() + File.separator + PastaBaseArquivoEnum.NOTAS_ENVIADAS.getValue() + File.separator + notaSaidaVO.getUnidadeEnsinoVO().getCodigo());
						nfeVO.setNomeArquivoXML(Uteis.getMontarCodigoBarra(notaSaidaVO.getNumero().toString(), 9) + ".xml");
						*/
//						new GerarXMLEnvio().gerarXMLEnvio(null, nfeVO);
						UnidadeEnsinoVO unidadeEnsinoVO = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(unidadeEnsino, false, Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado);
						ConfiguracaoGeralSistemaVO conGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesWebserviceNFe();
						String url = conGeralSistemaVO.getUrlWebserviceNFe() + "/servico/enviaNFe/" + unidadeEnsinoVO.getConfiguracaoNotaFiscalVO().getTokenWebserviceNFe();
						if (debug) {
							System.out.println("NFE_LOG: Chamando Webservice: " + url);
						}
						RestTemplate restTemplate = new RestTemplate();
						
				    	RequestEntity<NfeVO> request = RequestEntity.post(new URI(url)).contentType(MediaType.APPLICATION_JSON).body(nfeVO);
				    	ResponseEntity<NfeVO> response = restTemplate.exchange(request, NfeVO.class);
				    	NfeVO nfeAux = response.getBody();

				    	if ("100".equals(nfeAux.getStatus())) {
							notaSaidaVO.setSituacao(SituacaoNotaFiscalSaidaEnum.AUTORIZADA.getValor());
						} else {
							notaSaidaVO.setSituacao(SituacaoNotaFiscalSaidaEnum.REJEITADA.getValor());
						}
				    	if (debug) {
							System.out.println("NFE_LOG: Resposta aponta nota como " + notaSaidaVO.getSituacao());
				    	}
						notaSaidaVO.setIdentificadorReceita(nfeAux.getIdNfe());
						notaSaidaVO.setXmlEnvio(nfeAux.getXml());
						notaSaidaVO.setMensagemRetorno(nfeAux.getMotivo());
						alterarMensagemRetorno(notaSaidaVO, usuarioLogado);
						gravarXmlEnvio(notaSaidaVO.getCodigo(), notaSaidaVO.getXmlEnvio(), usuarioLogado);
						notaSaidaVO.setRecibo(UteisNfe.getRecido(nfeAux.getRecibo()));
						gravarRecibo(notaSaidaVO.getCodigo(), notaSaidaVO.getRecibo(), usuarioLogado);
						notaSaidaVO.setProtocolo(nfeAux.getProtocolo());
						notaSaidaVO.setDataEmissao(new Date());
						gravarSituacaoEnvio(notaSaidaVO, notaSaidaVO.getSituacao(), notaSaidaVO.getProtocolo(), notaSaidaVO.getDataStuacao(), notaSaidaVO.getIdentificadorReceita(), notaSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO(), usuarioLogado);
					} catch (Exception e) {
						if (debug) {
							System.out.println("NFE_LOG: Lançando exception devido " + e.getMessage());
						}
						if (SituacaoNotaFiscalSaidaEnum.AUTORIZADA.getValor().equals(notaSaidaVO.getSituacao())) {
							progressBar.setStatus("NF-e n° " + notaSaidaVO.getNumeroNota() + " AUTORIZADA ( " + progressBar.getProgresso() + " de " + progressBar.getMaxValue() + " ) ");
							notaSaidaVO.setSituacao(SituacaoNotaFiscalSaidaEnum.AUTORIZADA.getValor());
						} else {
							progressBar.setStatus("NF-e n° " + notaSaidaVO.getNumeroNota() + " REJEITADA ( " + progressBar.getProgresso() + " de " + progressBar.getMaxValue() + " ) ");
							notaSaidaVO.setSituacao(SituacaoNotaFiscalSaidaEnum.REJEITADA.getValor());
						}
						notaSaidaVO.setDataEmissao(new Date());
						gravarSituacaoEnvio(notaSaidaVO, notaSaidaVO.getSituacao(), notaSaidaVO.getProtocolo(), notaSaidaVO.getDataStuacao(), notaSaidaVO.getIdentificadorReceita(), notaSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO(), usuarioLogado);
						notaSaidaVO.setMensagemRetorno(e.getMessage());
						alterarMensagemRetorno(notaSaidaVO, usuarioLogado);
						throw e;
					}
//					int count = 0;
//					while (notaSaidaVO.getSituacao().equals(SituacaoNotaFiscalSaidaEnum.ENVIADA.getValor()) && count < 5) {
//						count++;
//						Thread.sleep(1000);
//						autorizado = consultarLoteEnviar(notaSaidaVO, configuracaoRespositoriArquivo, usuarioLogado);
//						notaSaidaVO.setMensagemRetorno(autorizado);
//						if (!notaSaidaVO.getSituacao().equals(SituacaoNotaFiscalSaidaEnum.AUTORIZADA.getValor()) && !notaSaidaVO.getSituacao().equals(SituacaoNotaFiscalSaidaEnum.ENVIADA.getValor())) {
//							progressBar.setStatus("NF-e n° " + notaSaidaVO.getNumeroNota() + " REJEITADA ( " + progressBar.getProgresso() + " de " + progressBar.getMaxValue() + " ) ");
//							gravarSituacaoEnvio(notaSaidaVO, SituacaoNotaFiscalSaidaEnum.REJEITADA.getValor(), notaSaidaVO.getProtocolo(), notaSaidaVO.getDataStuacao(), notaSaidaVO.getIdentificadorReceita(), notaSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO(), usuarioLogado);
//						} else if (notaSaidaVO.getSituacao().equals(SituacaoNotaFiscalSaidaEnum.AUTORIZADA.getValor())) {
//							progressBar.setStatus("NF-e n° " + notaSaidaVO.getNumeroNota() + " AUTORIZADA ( " + progressBar.getProgresso() + " de " + progressBar.getMaxValue() + " ) ");
//							gravarSituacaoEnvio(notaSaidaVO, SituacaoNotaFiscalSaidaEnum.AUTORIZADA.getValor(), notaSaidaVO.getProtocolo(), notaSaidaVO.getDataStuacao(), notaSaidaVO.getIdentificadorReceita(), notaSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO(), usuarioLogado);
//							gerarProcNFe(notaSaidaVO, configuracaoRespositoriArquivo);
							alterarXmlNfeProc(notaSaidaVO, usuarioLogado);
//						}
//					}
					notaSaidaVO.setNotaFiscalSaidaSelecionado(false);
				} else if (notaSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getTipoIntegracaoNfeEnum().equals(TipoIntegracaoNfeEnum.NFSE)) {
					try {
						progressBar.setStatus("Enviando NF-e n° " + notaSaidaVO.getNumeroNota() + " ( " + progressBar.getProgresso() + " de " + progressBar.getMaxValue() + " ) ");
						CidadeVO cidade = notaSaidaVO.getUnidadeEnsinoVO().getCidadeNFSe();
						if (!cidade.isWebserviceNFSeImplementado()) {
							registrarNumeroNotaFiscal(notaSaidaVO, notaSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO(),usuarioLogado);
						}
					} catch (Exception e) {
						progressBar.setStatus("NF-e n° " + notaSaidaVO.getNumeroNota() + " REJEITADA ( " + progressBar.getProgresso() + " de " + progressBar.getMaxValue() + " ) ");
						notaSaidaVO.setMensagemRetorno("Erro ao registrar Numero da Nota ( " + e.getMessage() + " )");
						throw e;
					}
					progressBar.setStatus("Enviando NF-e n° " + notaSaidaVO.getNumeroNota() + " ( " + progressBar.getProgresso() + " de " + progressBar.getMaxValue() + " ) ");
					notaSaidaVO.setNomesConvenios(getFacadeFactory().getContaReceberFacade().consultarNomesParceiroPorContaReceber(notaSaidaVO.getNotaFiscalSaidaServicoVOs()));
					enviarNFSE(notaSaidaVO, configuracaoRespositoriArquivo, progressBar, "", usuarioLogado);
					notaSaidaVO.setNotaFiscalSaidaSelecionado(false);
					gravarLinkAcesso(notaSaidaVO, usuarioLogado);
				}
				if (notaSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getNotificarAlunoNotaFiscalGerada()) {
					if (debug) {
						System.out.println("NFE_LOG: Enviando email.");
					}
					getFacadeFactory().getNotaFiscalSaidaFacade().realizarEnvioEmailNotaFiscal(notaSaidaVO, configuracaoRespositoriArquivo, configuracaoEmail, notaSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getNotificarAlunoNotaFiscalGerada() , usuarioLogado);
				}
				return "";
			} catch (Exception e) {
				if (debug) {
					System.out.println("NFE_LOG: Exception " + e.getMessage());
				}
				if (novoObj) {
					notaSaidaVO.setNumero(null);
					notaSaidaVO.setLote(null);
					notaSaidaVO.setCodigo(0);
					notaSaidaVO.setNovoObj(true);
					notaSaidaVO.setMensagemRetorno(e.getMessage());
				}
				return e.getMessage();
			} finally {
				aplicacaoControle.executarEnvioNotasFiscais(codigoConfNFE, false, true, false);
				if (debug) {
					System.out.println("NFE_LOG: Liberando sessão para emissão de nota fiscal nesta configuração.");
				}
			}
		}
		return "";
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void cancelarNFeWebservice(List<NotaFiscalSaidaVO> notaSaidaVOs, List<NotaFiscalSaidaVO> notaFiscalSaidaErroVOs, ConfiguracaoGeralSistemaVO conSistemaVO, ConfiguracaoGeralSistemaVO configuracaoRespositoriArquivo, UsuarioVO usuarioLogado) throws Exception {
		try {
			UnidadeEnsinoVO unidadeEnsinoVO = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(notaSaidaVOs.get(0).getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado);
			for (Iterator<NotaFiscalSaidaVO> iterator = notaSaidaVOs.iterator(); iterator.hasNext();) {
				NotaFiscalSaidaVO notaSaidaVO = (NotaFiscalSaidaVO) iterator.next();
				if (notaSaidaVO.getNotaFiscalSaidaSelecionado()) {
					notaSaidaVO.validarCancelamento();
					if (notaSaidaVO.getNotaFiscalSaidaServicoVOs().isEmpty()) {
						notaSaidaVO.getNotaFiscalSaidaServicoVOs().addAll(getFacadeFactory().getNotaFiscalSaidaServicoFacade().consultarNotaFiscalSaidaServicos(notaSaidaVO.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado));
					}
					String chave = "";
					if (notaSaidaVO.getIdentificadorReceita().contains("NFe")) {
						chave = notaSaidaVO.getIdentificadorReceita().substring(3);
					} else {
						chave = notaSaidaVO.getIdentificadorReceita();
					}
					String diretorioNotasCanceladasStr = conSistemaVO.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.NFE.getValue() + File.separator + PastaBaseArquivoEnum.NOTAS_CANCELADAS.getValue() + File.separator + notaSaidaVO.getUnidadeEnsinoVO().getCodigo() + File.separator;
					File diretorioNotasCanceladas = new File(diretorioNotasCanceladasStr);
					if (!diretorioNotasCanceladas.exists()) {
						diretorioNotasCanceladas.mkdirs();
					}
					
					String url = conSistemaVO.getUrlWebserviceNFe() + "/servico/cancelamentoNFe/" + unidadeEnsinoVO.getConfiguracaoNotaFiscalVO().getTokenWebserviceNFe();
					RestTemplate restTemplate = new RestTemplate();
					
					NfeVO nfeVO = new NfeVO();
					nfeVO.setNNF(notaSaidaVO.getNumeroNota());
					nfeVO.setCnpjEmit(notaSaidaVO.getUnidadeEnsinoVO().getCNPJ());
					nfeVO.setChaveAcesso(chave);
					nfeVO.setProtocolo(notaSaidaVO.getProtocolo());
					nfeVO.setMotivo(notaSaidaVO.getMotivoCancelamento());
					nfeVO.setPastaArquivoXML(configuracaoRespositoriArquivo.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.NFE.getValue() + File.separator + PastaBaseArquivoEnum.NOTAS_CANCELADAS.getValue() + File.separator + notaSaidaVO.getUnidadeEnsinoVO().getCodigo());
					nfeVO.setNomeArquivoXML(Uteis.getMontarCodigoBarra(notaSaidaVO.getNumeroNota(), 9) + "Cancelamento.xml");
			    	RequestEntity<NfeVO> request = RequestEntity.post(new URI(url)).contentType(MediaType.APPLICATION_JSON).body(nfeVO);
			    	ResponseEntity<NfeVO> response = restTemplate.exchange(request, NfeVO.class);
			    	NfeVO nfeAux = response.getBody();
//					retorno = Cancelar.A1(notaSaidaVO.getUnidadeEnsinoVO().getCidade().getEstado().getCodigoIBGE(), diretorioNotasCanceladasStr + Uteis.getMontarCodigoBarra(notaSaidaVO.getNumero().toString(), 9) + "Cancelamento.xml", chave, notaSaidaVO.getProtocolo(), notaSaidaVO.getMotivoCancelamento(), Uteis.removerMascara(notaSaidaVO.getUnidadeEnsinoVO().getCNPJ()), conSistemaVO, conNotaFiscalVO, usuarioLogado);
					if (nfeAux.getStatus().equals("135")) {
						notaSaidaVO.setXmlCancelamento(nfeAux.getXml());
						notaSaidaVO.setSituacao("CA");
						notaSaidaVO.setProtocoloCancelamento(nfeAux.getProtocolo());
						notaSaidaVO.setDataStuacao(UteisNfe.getDate(nfeAux.getDataRecebimento()));
						gravarSituacaoCancelamento(notaSaidaVO.getCodigo(), "CA", notaSaidaVO.getProtocoloCancelamento(), notaSaidaVO.getXmlCancelamento(), notaSaidaVO.getMotivoCancelamento(), usuarioLogado);
						notaSaidaVO.setMensagemRetorno(nfeAux.getMotivo());
						alterarMensagemRetorno(notaSaidaVO, usuarioLogado);
						gravarLogContasVinculadas(notaSaidaVO, usuarioLogado);
						for (NotaFiscalSaidaServicoVO nfssVO : notaSaidaVO.getNotaFiscalSaidaServicoVOs()) {
							getFacadeFactory().getContaReceberRecebimentoFacade().removerVinculoNotaFiscalSaidaServicoContaReceberRecebimento(nfssVO.getCodigo(), usuarioLogado);
							getFacadeFactory().getContaReceberFacade().removerVinculoNotaFiscalSaidaServicoContaReceber(nfssVO.getCodigo(), usuarioLogado);
						}
						String xml = notaSaidaVO.getXmlCancelamento();
						String nomeXml = Uteis.getMontarCodigoBarra(notaSaidaVO.getNumero().toString(), 9);
						gerarArquivosEnvio(Arrays.asList(gerarArquivoXml(xml, nomeXml)), notaSaidaVO.getSituacao(), conSistemaVO);
					} else if ((nfeAux.getStatus().equals("573"))) {
						gravarSituacaoCancelamento(notaSaidaVO.getCodigo(), "CA", notaSaidaVO.getProtocoloCancelamento(), notaSaidaVO.getXmlCancelamento(), notaSaidaVO.getMotivoCancelamento(), usuarioLogado);
						gravarLogContasVinculadas(notaSaidaVO, usuarioLogado);
						for (NotaFiscalSaidaServicoVO nfssVO : notaSaidaVO.getNotaFiscalSaidaServicoVOs()) {
							getFacadeFactory().getContaReceberRecebimentoFacade().removerVinculoNotaFiscalSaidaServicoContaReceberRecebimento(nfssVO.getCodigo(), usuarioLogado);
							getFacadeFactory().getContaReceberFacade().removerVinculoNotaFiscalSaidaServicoContaReceber(nfssVO.getCodigo(), usuarioLogado);
						}
						notaSaidaVO.setMensagemRetorno("Nota fiscal cancelada no sistema SEI - Dados de cancelamento dísponivel apenas na Sefaz por meio da chave de acesso na NFe");
						alterarMensagemRetorno(notaSaidaVO, usuarioLogado);
					} else {
						notaSaidaVO.setMensagemRetorno(nfeAux.getMotivo());
						notaFiscalSaidaErroVOs.add(notaSaidaVO);
						iterator.remove();
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void gravarSituacaoNotaComBaseNoStatusWebservice(NotaFiscalSaidaVO notaFiscalSaidaVO, NfeVO nfeVO, UsuarioVO usuarioLogado) throws Exception {
		if (nfeVO.getStatus().equals("102")) {
			notaFiscalSaidaVO.setSituacao(SituacaoNotaFiscalSaidaEnum.INUTILIZADA.getValor());
			notaFiscalSaidaVO.setProtocoloInutilizacao(nfeVO.getProtocolo());
			notaFiscalSaidaVO.setDataStuacao(UteisNfe.getDate(nfeVO.getDataRecebimento()));
			gravarSituacaoInutilizado(notaFiscalSaidaVO.getCodigo(), SituacaoNotaFiscalSaidaEnum.INUTILIZADA.getValor(), notaFiscalSaidaVO.getProtocoloInutilizacao(), notaFiscalSaidaVO.getMotivoInutilizacao(), usuarioLogado);
		} else if (nfeVO.getStatus().equals("101")) {
			notaFiscalSaidaVO.setSituacao(SituacaoNotaFiscalSaidaEnum.CANCELADA.getValor());
			notaFiscalSaidaVO.setProtocoloCancelamento(nfeVO.getProtocolo());
			notaFiscalSaidaVO.setDataStuacao(UteisNfe.getDate(nfeVO.getDataRecebimento()));
			gravarSituacaoCancelamento(notaFiscalSaidaVO.getCodigo(), SituacaoNotaFiscalSaidaEnum.CANCELADA.getValor(), notaFiscalSaidaVO.getProtocoloCancelamento(), notaFiscalSaidaVO.getXmlCancelamento(), notaFiscalSaidaVO.getMotivoCancelamento(), usuarioLogado);
		} else if (nfeVO.getStatus().equals("100")) {
			notaFiscalSaidaVO.setSituacao(SituacaoNotaFiscalSaidaEnum.AUTORIZADA.getValor());
			notaFiscalSaidaVO.setProtocolo(nfeVO.getProtocolo());
			notaFiscalSaidaVO.setDataStuacao(UteisNfe.getDate(nfeVO.getDataRecebimento()));
			gravarSituacaoEnvio(notaFiscalSaidaVO, SituacaoNotaFiscalSaidaEnum.AUTORIZADA.getValor(), notaFiscalSaidaVO.getProtocolo(), notaFiscalSaidaVO.getDataStuacao(), notaFiscalSaidaVO.getIdentificadorReceita(), notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO(), usuarioLogado);
		} else if (Integer.parseInt(nfeVO.getStatus()) >= 201) {
			notaFiscalSaidaVO.setSituacao(SituacaoNotaFiscalSaidaEnum.REJEITADA.getValor());
			gravarSituacaoEnvio(notaFiscalSaidaVO, SituacaoNotaFiscalSaidaEnum.REJEITADA.getValor(), notaFiscalSaidaVO.getProtocolo(), notaFiscalSaidaVO.getDataStuacao(), notaFiscalSaidaVO.getIdentificadorReceita(), notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO(), usuarioLogado);
		}
	}
	
	private List<NotaFiscalSaidaVO> consultarPorTipoPessoaParaCompactacaoNotasEnviadas(String valorConsulta, Date prmIni, Date prmFim, String situacao, UnidadeEnsinoVO unidadeEnsinoVO, CursoVO cursoVO, TurmaVO turmaVO, int limite, int pagina, Boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		consultar(getIdEntidade(), true, usuarioLogado);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT notafiscalsaida.codigo, notafiscalsaida.numero, notafiscalsaida.situacao, notafiscalsaida.xmlenvio, notafiscalsaida.unidadeensino, notafiscalsaida.xmlnfeproc ");
		sqlStr.append("FROM notafiscalsaida ");
		sqlStr.append("WHERE notafiscalsaida.tipoPessoa = '").append(valorConsulta).append("' ");
		if (!situacao.trim().equals("-1")) {
			sqlStr.append(" and situacao = '").append(situacao).append("' ");
		}
		if (unidadeEnsinoVO != null && unidadeEnsinoVO.getCodigo().intValue() != 0) {
			sqlStr.append(" and unidadeensino = ").append(unidadeEnsinoVO.getCodigo()).append(" ");
		}
		if (prmIni != null && prmFim != null) {
			sqlStr.append(" and ((dataemissao>= '");
			sqlStr.append(UteisData.getDataJDBC(prmIni));
			sqlStr.append("') and (dataemissao <= '");
			sqlStr.append(UteisData.getDataJDBC(prmFim));
			sqlStr.append("')) ");
		} else if (prmIni != null) {
			sqlStr.append(" and ((dataemissao >= '");
			sqlStr.append(UteisData.getDataJDBC(prmIni));
			sqlStr.append("')) ");
		} else if (prmFim != null) {
			sqlStr.append(" and ((dataemissao <= '");
			sqlStr.append(UteisData.getDataJDBC(prmFim));
			sqlStr.append("')) ");
		}
		if (limite != 0) {
			sqlStr.append(" ORDER BY numero DESC  limit ");
			sqlStr.append(limite);
			sqlStr.append(" offset ");
			sqlStr.append(pagina);
		} else {
			sqlStr.append(" ORDER BY numero DESC ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaParaCompactacaoNotasEnviadas(tabelaResultado, nivelMontarDados, usuarioLogado, unidadeEnsinoVO);
	}
	
	public String concatenarDatasCompetenciasContaReceberNotaFiscal(List<NotaFiscalSaidaServicoVO> notaFiscalSaidaServicoVOs) throws Exception {
		StringBuilder str = new StringBuilder("");
		for (NotaFiscalSaidaServicoVO notaFiscalSaidaServicoVO : notaFiscalSaidaServicoVOs) {
			try {
				if (Uteis.isAtributoPreenchido(notaFiscalSaidaServicoVO.getContaReceberVO())) {
					notaFiscalSaidaServicoVO.getContaReceberVO().setNivelMontarDados(NivelMontarDados.NAO_INICIALIZADO);
					getFacadeFactory().getContaReceberFacade().carregarDados(notaFiscalSaidaServicoVO.getContaReceberVO(), NivelMontarDados.BASICO, new ConfiguracaoFinanceiroVO(), null);
					if (!str.toString().contains(Uteis.getMesReferenciaData(notaFiscalSaidaServicoVO.getContaReceberVO().getDataCompetencia()))) {
						if (str.length() > 0) {
							str.append(", ");
						}
						str.append(Uteis.getMesReferenciaData(notaFiscalSaidaServicoVO.getContaReceberVO().getDataCompetencia()));
					}
				} else if (Uteis.isAtributoPreenchido(notaFiscalSaidaServicoVO.getContaReceberRecebimentoVO())) {
					notaFiscalSaidaServicoVO.getContaReceberRecebimentoVO().getContaReceberVO().setNivelMontarDados(NivelMontarDados.NAO_INICIALIZADO);
					getFacadeFactory().getContaReceberFacade().carregarDados(notaFiscalSaidaServicoVO.getContaReceberRecebimentoVO().getContaReceberVO(), NivelMontarDados.BASICO, new ConfiguracaoFinanceiroVO(), null);
					if (!str.toString().contains(Uteis.getMesReferenciaData(notaFiscalSaidaServicoVO.getContaReceberRecebimentoVO().getContaReceberVO().getDataCompetencia()))) {
						if (str.length() > 0) {
							str.append(", ");
						}
						str.append(Uteis.getMesReferenciaData(notaFiscalSaidaServicoVO.getContaReceberRecebimentoVO().getContaReceberVO().getDataCompetencia()));
					}
				}
			} catch (Exception e) {
				throw e;
			}
		}
		String datasCompetenciaConcatenadas = str.toString().trim();
		return datasCompetenciaConcatenadas;
	}
	
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void realizarInclusaoNotaFiscalSaidaEnviandoNFeWebservice(NotaFiscalSaidaVO notaSaidaVO,  ConfiguracaoGeralSistemaVO configuracaoRespositoriArquivo,ConfiguracaoGeralSistemaVO conGeralSistemaVO ,ConfiguracaoGeralSistemaVO configuracaoEmail, ProgressBarVO progressBar, Integer unidadeEnsino, UsuarioVO usuarioLogado) throws Exception {
		boolean novoObj = notaSaidaVO.isNovoObj();
		try {
			realizarEscritaTextoDebugNFE_LOG("Acesso ao metodo realizarInclusaoNotaFiscalSaidaEnviandoNFeWebservice para envio da Nota Fiscal de Saida ");
			if (!(notaSaidaVO.getSituacao().equals(SituacaoNotaFiscalSaidaEnum.REJEITADA.getValor())) && verificarNotaFiscalSaidaJaEmitida(notaSaidaVO, usuarioLogado)) {
				 notaSaidaVO.setNotaFiscalSaidaSelecionado(false);
				 realizarEscritaTextoDebugNFE_LOG("Nota Fiscal de Saida de numero :"+notaSaidaVO.getNumeroNota()+"não será enviada .  Nota já foi Emitida e está Rejeitada!");	
				 return ;
			}
			if (notaSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO() == null || notaSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCodigo().intValue() == 0) {
				throw new AcessoException("É obrigatório possuir na Unidade de Ensino \"" + notaSaidaVO.getUnidadeEnsinoVO().getNome() + "\" uma Configuração de Nota Fiscal.");
			}
				
			getAplicacaoControle().executarEnvioNotasFiscais(notaSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCodigo(), true, false, true);			
			
			getFacadeFactory().getNotaFiscalSaidaFacade().persistirNotaFiscalRegistrandoNumeroNotaNumeroRPS(notaSaidaVO, progressBar, usuarioLogado);
							
			getFacadeFactory().getNotaFiscalSaidaFacade().realizarEnvioNotaFiscalEletronica(notaSaidaVO,configuracaoRespositoriArquivo, conGeralSistemaVO, configuracaoEmail, progressBar, unidadeEnsino, usuarioLogado);
			
			getFacadeFactory().getNotaFiscalSaidaFacade().realizarEnvioEmailNotaFiscal(notaSaidaVO, configuracaoRespositoriArquivo, configuracaoEmail, notaSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getNotificarAlunoNotaFiscalGerada(), usuarioLogado);
						
		} catch (Exception e) {
			realizarEscritaTextoDebugNFE_LOG(" Exception " + e.getMessage());
			notaSaidaVO.setPossuiErroWebservice(Boolean.TRUE);
			if (novoObj) {
				notaSaidaVO.setNumero(null);
				notaSaidaVO.setLote(null);
				notaSaidaVO.setCodigo(0);
				notaSaidaVO.setNovoObj(true);
				notaSaidaVO.setMensagemRetorno(e.getMessage());
			}
			
			
		} finally {
			realizarEscritaTextoDebugNFE_LOG("Acesso ao metodo realizarInclusaoNotaFiscalSaidaEnviandoNFeWebservice Liberando sessão para emissão de nota fiscal nesta configuração.");	
			getAplicacaoControle().executarEnvioNotasFiscais(notaSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCodigo(), false, true, false);
			
		}
			
	
	}
	
	

	

	

	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public synchronized void enviarNFE(NfeVO nfeVO , NotaFiscalSaidaVO notaSaidaVO, ConfiguracaoGeralSistemaVO conGeralSistemaVO,	ProgressBarVO progressBar, UsuarioVO usuarioLogado) throws Exception {
		
		try {	
			 realizarEnvioNFEWebService(nfeVO,notaSaidaVO,conGeralSistemaVO.getUrlWebserviceNFe(), progressBar, usuarioLogado);			
			 Uteis.checkState(notaSaidaVO.getPossuiErroWebservice(), nfeVO.getMotivo());	
			 notaSaidaVO.setSituacao(SituacaoNotaFiscalSaidaEnum.ENVIADA.getValor());
		     realizarConsultaReciboNotaFiscalNFEWebService(nfeVO,notaSaidaVO,conGeralSistemaVO.getUrlWebserviceNFe(),  usuarioLogado);
		     Uteis.checkState(notaSaidaVO.getPossuiErroWebservice(), nfeVO.getMotivo());
		     
			} catch (Exception e) {
				realizarEscritaTextoDebugNFE_LOG(" Lançando exception devido " + e.getMessage());
				if (!SituacaoNotaFiscalSaidaEnum.AUTORIZADA.getValor().equals(notaSaidaVO.getSituacao()) && !SituacaoNotaFiscalSaidaEnum.ENVIADA.getValor().equals(notaSaidaVO.getSituacao())) {
					notaSaidaVO.setSituacao(SituacaoNotaFiscalSaidaEnum.REJEITADA.getValor());
				} 
				progressBar.setStatus("NF-e n° " + notaSaidaVO.getNumeroNota() + " "+SituacaoNotaFiscalSaidaEnum.getEnumPorValor(notaSaidaVO.getSituacao())+ "  ( " + progressBar.getProgresso() + " de " + progressBar.getMaxValue() + " ) ");
				notaSaidaVO.setMensagemRetorno(e.getMessage());
				notaSaidaVO.setPossuiErroWebservice(Boolean.TRUE);					
		   }finally {			   
			    executarRegistroDadosRetornoNotaFiscalSaida(notaSaidaVO, usuarioLogado);	
			    notaSaidaVO.setDataEmissao(new Date());
			    gravarSituacaoEnvio(notaSaidaVO, notaSaidaVO.getSituacao(), notaSaidaVO.getProtocolo(), notaSaidaVO.getDataStuacao(), notaSaidaVO.getIdentificadorReceita(), notaSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO(), usuarioLogado);
				notaSaidaVO.setNotaFiscalSaidaSelecionado(false);
		}	
	}
	

	@Override
	public void realizarEnvioNFEWebService(NfeVO nfeVO, NotaFiscalSaidaVO notaSaidaVO,	String urlEnviar, ProgressBarVO progressBar, UsuarioVO usuarioLogado) throws Exception  {
		try {
			String url = urlEnviar+ "/servico/enviaNFe_V1/"  + notaSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getTokenWebserviceNFe();
			realizarEscritaTextoDebugNFE_LOG(" Acesso ao metodo realizarEnvioNFEWebService : Envio da nota Fiscal Numero :"+nfeVO.getnNF() +"  url: " + url);	    		
	    	RequestEntity<NfeVO>  request = RequestEntity.post(new URI(url)).contentType(MediaType.APPLICATION_JSON).body(nfeVO);
			ResponseEntity<NfeVO> response = new RestTemplate().exchange(request, NfeVO.class);
	    	NfeVO nfeAux = response.getBody();   	
	    	nfeVO.setRecibo(nfeAux.getRecibo());	    	
	    	notaSaidaVO.setRecibo(nfeAux.getRecibo());	    	
	    	notaSaidaVO.setJsonEnvio(nfeAux.getJsonEnvio());  
	    	notaSaidaVO.setJsonRetornoEnvio(nfeAux.getJsonRetornoEnvio());
	    	nfeVO.setMotivo(nfeAux.getMotivo());
	    	nfeVO.setStatus(nfeAux.getStatus());	    	
	    	realizarEscritaTextoDebugNFE_LOG(" Acesso ao metodo realizarEnvioNFEWebService :  retorno Envio da nota Fiscal Numero :"+nfeVO.getnNF() +"  recibo: " + nfeAux.getRecibo());	    	
		    Uteis.checkState(nfeAux.getStatus().equals("ERRO"), nfeAux.getMotivo());
		} catch (Exception e) {	
			nfeVO.setStatus("ERRO");				
			notaSaidaVO.setPossuiErroWebservice(Boolean.TRUE);
	    	realizarEscritaTextoDebugNFE_LOG(" Acesso ao metodo realizarEnvioNFEWebService :  Erro Envio da nota Fiscal Numero :"+nfeVO.getnNF()  +"  Motivo"+ e.getMessage());	    	
	        nfeVO.setMotivo(e.getMessage());	    	
		}
	}
	
	
	@Override
	public  void realizarConsultaReciboNotaFiscalNFEWebService(NfeVO nfeVO, NotaFiscalSaidaVO notaSaidaVO,	String urlConsulta , UsuarioVO usuarioLogado) {
		
		try {	
				String url = urlConsulta+ "/servico/consultaReciboEnvioNFe/" + notaSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getTokenWebserviceNFe();
		    	realizarEscritaTextoDebugNFE_LOG(" Chamando Webservice: consulta da nota Fiscal Numero :"+nfeVO.getnNF() +"  recibo: " + nfeVO.getRecibo());
	  		   	RequestEntity<NfeVO> requestConsulta = RequestEntity.post(new URI(url)).contentType(MediaType.APPLICATION_JSON).body(nfeVO);
		    	ResponseEntity<NfeVO> responseConsulta = new RestTemplate().exchange(requestConsulta, NfeVO.class);
		    	NfeVO nfeConsultaAux = responseConsulta.getBody(); 
		    	if ("100".equals(nfeConsultaAux.getStatus())) {
					notaSaidaVO.setSituacao(SituacaoNotaFiscalSaidaEnum.AUTORIZADA.getValor());
				} else {
					notaSaidaVO.setSituacao(SituacaoNotaFiscalSaidaEnum.REJEITADA.getValor());
				}		
		    	notaSaidaVO.setIdentificadorReceita(nfeConsultaAux.getIdNfe());
				notaSaidaVO.setXmlEnvio(nfeConsultaAux.getXml());
				notaSaidaVO.setMensagemRetorno(nfeConsultaAux.getMotivo());
				//notaSaidaVO.setRecibo(UteisNfe.getRecido(nfeVO.getRecibo()));
				notaSaidaVO.setProtocolo(nfeConsultaAux.getProtocolo());							
				realizarEscritaTextoDebugNFE_LOG(" Resposta aponta nota como " + notaSaidaVO.getSituacao());	
			} catch (Exception e) {
				realizarEscritaTextoDebugNFE_LOG(" Lançando exception devido " + e.getMessage());
				nfeVO.setStatus("ERRO");
				notaSaidaVO.setPossuiErroWebservice(Boolean.TRUE);				
				nfeVO.setMotivo(e.getMessage());				
				if(e.getCause().toString().contains("SocketException")) {
					nfeVO.setStatus("ERRO_SOCKET");
				}				
			}	
	}

	
	/**
	 * @param notaSaidaVO
	 * @param progressBar
	 * @param usuarioLogado
	 * @param debug
	 * @throws Exception
	 */
	
	@Override
	@Transactional(readOnly = false , isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public   void persistirNotaFiscalRegistrandoNumeroNotaNumeroRPS(NotaFiscalSaidaVO notaSaidaVO,ProgressBarVO progressBar, UsuarioVO usuarioLogado) throws Exception {
		realizarEscritaTextoDebugNFE_LOG("Acesso ao metodo persistirNotaFiscalRegistrandoNumeroNotaNumeroRPS para Nota Fiscal de Saida de numero :"+notaSaidaVO.getNumeroNota());

		if (notaSaidaVO.getUnidadeEnsinoVO().getCidadeNFSe().isWebserviceNFSeImplementado()  && (!Uteis.isAtributoPreenchido(notaSaidaVO.getCodigo())  || !Uteis.isAtributoPreenchido(notaSaidaVO.getNumeroRPS()))) {
			try {
				realizarEscritaTextoDebugNFE_LOG("Acesso ao metodo persistirNotaFiscalRegistrandoNumeroNotaNumeroRPS  iniciando metodo registrarNumeroRPS para Nota Fiscal de Saida de numero :"+notaSaidaVO.getNumeroNota());	
				registrarNumeroRPS(notaSaidaVO, notaSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO());
				realizarEscritaTextoDebugNFE_LOG("Acesso ao metodo persistirNotaFiscalRegistrandoNumeroNotaNumeroRPS  finalizando metodo registrarNumeroRPS para Nota Fiscal de Saida de numero :"+notaSaidaVO.getNumeroNota());	
			} catch (Exception e) {
				progressBar.setStatus("NF-e n° " + notaSaidaVO.getNumeroNota() + " REJEITADA ( " + progressBar.getProgresso() + " de " + progressBar.getMaxValue() + " ) ");
				notaSaidaVO.setMensagemRetorno("Erro ao registrar Numero RPS ( " + e.getMessage() + " )");				
				throw e;
			}
		}
		
		if (Uteis.isAtributoPreenchido(notaSaidaVO.getCodigo())) {	
			try {
				 realizarEscritaTextoDebugNFE_LOG("Acesso ao metodo persistirNotaFiscalRegistrandoNumeroNotaNumeroRPS  iniciando metodo alterar para Nota Fiscal de Saida de numero :"+notaSaidaVO.getNumeroNota());	
			     alterar(notaSaidaVO, usuarioLogado);	
				 realizarEscritaTextoDebugNFE_LOG("Acesso ao metodo persistirNotaFiscalRegistrandoNumeroNotaNumeroRPS  finalizando metodo alterar para Nota Fiscal de Saida de numero :"+notaSaidaVO.getNumeroNota());	
				} catch (Exception e) {
					progressBar.setStatus("NF-e n° " + notaSaidaVO.getNumeroNota() + " REJEITADA ( " + progressBar.getProgresso() + " de " + progressBar.getMaxValue() + " ) ");
					notaSaidaVO.setMensagemRetorno("Erro ao alterar Nota ( " + e.getMessage() + " )");					
					throw e;
				}
			
		} else {	
			
			try {
				 realizarEscritaTextoDebugNFE_LOG("Acesso ao metodo persistirNotaFiscalRegistrandoNumeroNotaNumeroRPS  iniciando metodo registrarLoteNotaFiscal para Nota Fiscal de Saida .");	
			     registrarLoteNotaFiscal(notaSaidaVO);		
				 realizarEscritaTextoDebugNFE_LOG("Acesso ao metodo persistirNotaFiscalRegistrandoNumeroNotaNumeroRPS  finalizando metodo registrarLoteNotaFiscal para Nota Fiscal de Saida .");	
			} catch (Exception e) {
				progressBar.setStatus("NF-e n° " + notaSaidaVO.getNumeroNota() + " REJEITADA ( " + progressBar.getProgresso() + " de " + progressBar.getMaxValue() + " ) ");
				notaSaidaVO.setMensagemRetorno("Erro ao registrar Lote Nota ( " + e.getMessage() + " )");				
				throw e;
			}
			
			try {
				 realizarEscritaTextoDebugNFE_LOG("Acesso ao metodo persistirNotaFiscalRegistrandoNumeroNotaNumeroRPS  iniciando metodo incluir para Nota Fiscal de Saida .");	
				 incluir(notaSaidaVO, usuarioLogado);
				 realizarEscritaTextoDebugNFE_LOG("Acesso ao metodo persistirNotaFiscalRegistrandoNumeroNotaNumeroRPS  finalizando metodo incluir para Nota Fiscal de Saida de codigo :"+notaSaidaVO.getCodigo());	
			} catch (Exception e) {
				progressBar.setStatus("NF-e n° " + notaSaidaVO.getNumeroNota() + " REJEITADA ( " + progressBar.getProgresso() + " de " + progressBar.getMaxValue() + " ) ");
				notaSaidaVO.setMensagemRetorno("Erro ao incluir Nota ( " + e.getMessage() + " )");
				throw e;
			}
			
			if (notaSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getTipoIntegracaoNfeEnum().equals(TipoIntegracaoNfeEnum.NFE) ||( notaSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getTipoIntegracaoNfeEnum().equals(TipoIntegracaoNfeEnum.NFSE) && !notaSaidaVO.getUnidadeEnsinoVO().getCidadeNFSe().isWebserviceNFSeImplementado())) {
				try {
					realizarEscritaTextoDebugNFE_LOG("Acesso ao metodo persistirNotaFiscalRegistrandoNumeroNotaNumeroRPS iniciando metodo registrarNumeroNotaFiscal para  Nota Fiscal Saida de codigo : "+notaSaidaVO.getCodigo());	
					registrarNumeroNotaFiscal(notaSaidaVO, notaSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO(), usuarioLogado);
					realizarEscritaTextoDebugNFE_LOG("Acesso ao metodo persistirNotaFiscalRegistrandoNumeroNotaNumeroRPS finalizando  metodo registrarNumeroNotaFiscal para  Nota Fiscal Saida de codigo : "+notaSaidaVO.getCodigo());	
				} catch (Exception e) {
					progressBar.setStatus("NF-e n° " + notaSaidaVO.getNumeroNota() + " REJEITADA ( " + progressBar.getProgresso() + " de " + progressBar.getMaxValue() + " ) ");
					notaSaidaVO.setMensagemRetorno("Erro ao registrar Numero da Nota ( " + e.getMessage() + " )");								
					throw e;
				}
			}
		}
	}
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public synchronized void gravarReciboJsonEnvioNotaFiscalSaida(String recibo ,Integer codigo, String jsonEnvio ,String jsonRetornoEnvio , UsuarioVO usuarioLogado) throws Exception {
		try {
			System.out.println("entrou no update . Recibo"+recibo +" codigo : "+ codigo +"  JSON : "+jsonEnvio +"  json RETORNO" + jsonRetornoEnvio  );
			String sql = "UPDATE NotaFiscalSaida set  recibo =? , jsonEnvio=?, jsonRetornoEnvio=?  WHERE codigo = ? "+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado);
			getConexao().getJdbcTemplate().update(sql, new Object[] {recibo ,jsonEnvio ,jsonRetornoEnvio  , codigo });
		} catch (Exception e) {
			realizarEscritaTextoDebugNFE_LOG(" Erro ao realizar gravação do json envio "+ e);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public synchronized void realizarEnvioNotaFiscalEletronica(NotaFiscalSaidaVO notaSaidaVO,	ConfiguracaoGeralSistemaVO configuracaoRespositorioArquivo, ConfiguracaoGeralSistemaVO conGeralSistemaVO,ConfiguracaoGeralSistemaVO configuracaoEmail, ProgressBarVO progressBar, Integer unidadeEnsino,	UsuarioVO usuarioLogado) throws Exception {

		try {
			realizarEscritaTextoDebugNFE_LOG("Acesso ao metodo realizarEnvioNotaFiscalEletronica para Nota Fiscal de Saida de numero :"+notaSaidaVO.getNumeroNota());
			progressBar.incrementar();
			progressBar.setStatus("Enviando NF-e n° " + notaSaidaVO.getNumeroNota() + " ( " + progressBar.getProgresso() + " de " + progressBar.getMaxValue() + " ) ");		

			if (notaSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getTipoIntegracaoNfeEnum().equals(TipoIntegracaoNfeEnum.NFE)) {							
				NfeVO nfeVO = new NfeVO();
				inicializarDadosObjetoEnvio(nfeVO, notaSaidaVO, notaSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO(),configuracaoRespositorioArquivo, usuarioLogado);
				alterarObservacaoContribuinte(notaSaidaVO, usuarioLogado);			
				enviarNFE(nfeVO,notaSaidaVO,conGeralSistemaVO ,progressBar, usuarioLogado);				
			
			} else if (notaSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getTipoIntegracaoNfeEnum().equals(TipoIntegracaoNfeEnum.NFSE)) {				
				notaSaidaVO.setNomesConvenios(getFacadeFactory().getContaReceberFacade().consultarNomesParceiroPorContaReceber(notaSaidaVO.getNotaFiscalSaidaServicoVOs()));
				enviarNFSE(notaSaidaVO, configuracaoRespositorioArquivo, progressBar, "", usuarioLogado);				
				gravarLinkAcesso(notaSaidaVO, usuarioLogado);				
			}
		
		} catch (Exception e) {
			realizarEscritaTextoDebugNFE_LOG(" Exception " + e.getMessage());	
			throw e;
		} finally {
			getAplicacaoControle().executarEnvioNotasFiscais(notaSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCodigo(), false, true, false);
			realizarEscritaTextoDebugNFE_LOG("Acesso ao metodo realizarEnvioNotaFiscalEletronica Liberando sessão para emissão de nota fiscal nesta configuração.");	
			
		}	
	}
    
	
	@Override
	public void realizarEscritaTextoDebugNFE_LOG(String mensagem) {
	    AplicacaoControle.realizarEscritaTextoDebug(AssuntoDebugEnum.NOTA_FISCAL_SAIDA,"NFE_LOG: "+mensagem);
	}
	
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public synchronized void executarRegistroReciboJsonEnvioNotaFiscalSaida(String recibo , Integer codigo, String jsonEnvio ,String jsonRetornoEnvio , UsuarioVO usuario) throws Exception {
		 if(Uteis.isAtributoPreenchido(codigo) && (Uteis.isAtributoPreenchido(recibo) || Uteis.isAtributoPreenchido(jsonEnvio) || Uteis.isAtributoPreenchido(jsonRetornoEnvio))) {
			/*ExecutarRegistroReciboJsonEnvioNotaFiscalSaida servico = new ExecutarRegistroReciboJsonEnvioNotaFiscalSaida(recibo, codigo, jsonEnvio, jsonRetornoEnvio,usuario);
			Thread job = new Thread(servico);
		    job.start();*/
			getFacadeFactory().getNotaFiscalSaidaFacade().gravarReciboJsonEnvioNotaFiscalSaida(recibo,codigo, jsonEnvio, jsonRetornoEnvio, usuario);
		}
		
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public synchronized void executarRegistroDadosRetornoNotaFiscalSaida(NotaFiscalSaidaVO notaFiscalSaida, UsuarioVO usuario) throws Exception {
		try {			
			String sql = "UPDATE NotaFiscalSaida set xmlEnvio=? ,mensagemretorno=? ,xmlnfeproc=? ,recibo=? ,jsonEnvio=? ,jsonRetornoEnvio=?  WHERE codigo = ? "+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] {notaFiscalSaida.getXmlEnvio() , notaFiscalSaida.getMensagemRetorno() , notaFiscalSaida.getXmlNfeProc() ,notaFiscalSaida.getRecibo() , notaFiscalSaida.getJsonEnvio() ,notaFiscalSaida.getJsonRetornoEnvio() , notaFiscalSaida.getCodigo() });
		} catch (Exception e) {
			realizarEscritaTextoDebugNFE_LOG(" Erro ao realizar update dos dados da nota fiscal de saida de codigo "+notaFiscalSaida.getCodigo() +" utilizando o metodo  executarRegistroDadodRetornoNotaFiscalSaida ERRO :  "+ e);
		}
		
	}

	class  ExecutarRegistroReciboJsonEnvioNotaFiscalSaida implements Runnable {
		private String recibo ;
		private Integer codigo;
		private String jsonEnvio;
		private String jsonRetornoEnvio ;
		private UsuarioVO usuario;		
		
		public ExecutarRegistroReciboJsonEnvioNotaFiscalSaida(String recibo , Integer codigo, String jsonEnvio ,String jsonRetornoEnvio , UsuarioVO usuario) {
			super();this.recibo = recibo;this.codigo = codigo;this.jsonEnvio = jsonEnvio;this.jsonRetornoEnvio = jsonRetornoEnvio;this.usuario = usuario;			
		}

		@Override
		public void run() {
			try {
				realizarEscritaTextoDebugNFE_LOG("Acesso ao metodo ExecutarRegistroReciboJsonEnvioNotaFiscalSaida  iniciando  ação update independente da nota fiscal :"+codigo + "  Recibo :"+recibo);	
				getFacadeFactory().getNotaFiscalSaidaFacade().gravarReciboJsonEnvioNotaFiscalSaida(recibo,codigo, jsonEnvio, jsonRetornoEnvio, usuario);
				realizarEscritaTextoDebugNFE_LOG("Acesso ao metodo ExecutarRegistroReciboJsonEnvioNotaFiscalSaida  finalizando  ação  update independente da nota fiscal :"+codigo + "  Recibo :"+recibo);	
			} catch (Exception e) {
				realizarEscritaTextoDebugNFE_LOG("Acesso ao metodo ExecutarRegistroReciboJsonEnvioNotaFiscalSaida  erro  ação  update independente da nota fiscal :"+codigo + "  Recibo :"+recibo);	
				e.printStackTrace();
			}
		}
	}
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public synchronized void consultarPorReciboEnvioNFE(NotaFiscalSaidaVO notaSaidaVO, ConfiguracaoGeralSistemaVO conGeralSistemaVO, ConfiguracaoGeralSistemaVO configuracaoRespositorioArquivo, UsuarioVO usuarioLogado) throws Exception {
		
		try {	
			 NfeVO nfeVO = new NfeVO();
			 inicializarDadosObjetoEnvio(nfeVO, notaSaidaVO, notaSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO(),configuracaoRespositorioArquivo, usuarioLogado);	 
			 nfeVO.setRecibo(notaSaidaVO.getRecibo());			
		     realizarConsultaReciboNotaFiscalNFEWebService(nfeVO,notaSaidaVO,conGeralSistemaVO.getUrlWebserviceNFe(),  usuarioLogado);
		     Uteis.checkState(notaSaidaVO.getPossuiErroWebservice(), nfeVO.getMotivo());
		     
			} catch (Exception e) {
				realizarEscritaTextoDebugNFE_LOG(" Lançando exception devido " + e.getMessage());
				if (!SituacaoNotaFiscalSaidaEnum.AUTORIZADA.getValor().equals(notaSaidaVO.getSituacao()) && !SituacaoNotaFiscalSaidaEnum.ENVIADA.getValor().equals(notaSaidaVO.getSituacao())) {
					notaSaidaVO.setSituacao(SituacaoNotaFiscalSaidaEnum.REJEITADA.getValor());
				} 
				notaSaidaVO.setMensagemRetorno(e.getMessage());
				notaSaidaVO.setPossuiErroWebservice(Boolean.TRUE);					
		   }finally {		    
			    alterarMensagemRetorno(notaSaidaVO, usuarioLogado);
				gravarXmlEnvio(notaSaidaVO.getCodigo(), notaSaidaVO.getXmlEnvio(), usuarioLogado);
				notaSaidaVO.setDataEmissao(new Date());		    
				gravarSituacaoEnvio(notaSaidaVO, notaSaidaVO.getSituacao(), notaSaidaVO.getProtocolo(), notaSaidaVO.getDataStuacao(), notaSaidaVO.getIdentificadorReceita(), notaSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO(), usuarioLogado);
			}	
	}
	
	
	class  JobExecutarRegistroNumeroNotaFiscal implements Runnable {
		
		private static final long serialVersionUID = 1L;
		private NotaFiscalSaidaVO notaFiscal;
		private ConfiguracaoNotaFiscalVO configuracao;
		private long numeroNota;
		private UsuarioVO usuarioVO;
		
		public JobExecutarRegistroNumeroNotaFiscal(NotaFiscalSaidaVO notaFiscal, ConfiguracaoNotaFiscalVO configuracao, long numeroNota, UsuarioVO usuarioVO) {
			this.notaFiscal = notaFiscal;
			this.configuracao = configuracao;
			this.numeroNota = numeroNota;
			this.usuarioVO = usuarioVO;
		}

		@Override
		public void run(){
			 try {
				 getFacadeFactory().getNotaFiscalSaidaFacade().gravarNumeroNotaFiscalViaJobComTransacaoIndependente(notaFiscal, configuracao, numeroNota, usuarioVO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	
	
	
}