package relatorio.negocio.jdbc.administrativo;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.SerializationUtils;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import com.sun.jna.platform.win32.Sspi.TimeStamp;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.GradeCurricularEstagioVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.CampanhaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FiltroPersonalizadoOpcaoVO;
import negocio.comuns.administrativo.FiltroPersonalizadoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.LayoutRelatorioSEIDecidirArquivoVO;
import negocio.comuns.administrativo.LayoutRelatorioSEIDecidirVO;
import negocio.comuns.administrativo.LayoutRelatorioSeiDecidirCampoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.RelatorioSEIDecidirModuloEnum;
import negocio.comuns.administrativo.enumeradores.RelatorioSEIDecidirNivelDetalhamentoEnum;
import negocio.comuns.administrativo.enumeradores.RelatorioSEIDecidirTipoTotalizadorEnum;
import negocio.comuns.administrativo.enumeradores.TagSEIDecidirContaReceberEnum;
import negocio.comuns.administrativo.enumeradores.TagSEIDecidirEntidadeEnum;
import negocio.comuns.administrativo.enumeradores.TagSEIDecidirMatriculaEnum;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.administrativo.enumeradores.TipoCampoFiltroEnum;
import negocio.comuns.arquitetura.RelatorioDinamicoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.financeiro.CentroReceitaVO;
import negocio.comuns.financeiro.CentroResultadoVO;
import negocio.comuns.financeiro.RelatorioSEIDecidirVO;
import negocio.comuns.financeiro.enumerador.OrigemFechamentoFinanceiroContaEnum;
import negocio.comuns.financeiro.enumerador.TipoCentroResultadoOrigemEnum;
import negocio.comuns.financeiro.enumerador.TipoFiltroPeriodoSeiDecidirEnum;
import negocio.comuns.processosel.enumeradores.SituacaoInscricaoEnum;
import negocio.comuns.processosel.enumeradores.SituacaoResultadoProcessoSeletivoEnum;
import negocio.comuns.protocolo.RequerimentoVO;
import negocio.comuns.protocolo.enumeradores.SituacaoSolicitacaoIsencaoTaxaRequerimentoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.ToHtml;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisExcel;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.SituacaoContaReceber;
import negocio.comuns.utilitarias.dominios.SituacaoFinanceira;
import negocio.comuns.utilitarias.dominios.SituacaoHistorico;
import negocio.comuns.utilitarias.dominios.SituacaoRequerimento;
import negocio.comuns.utilitarias.dominios.TipoHistorico;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.comuns.utilitarias.dominios.TipoOrigemContaReceber;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.comuns.utilitarias.dominios.TipoSacado;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.academico.MatriculaPeriodoTurmaDisciplina;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.arquitetura.FacadeFactory;
import negocio.facade.jdbc.utilitarias.Conexao;
import negocio.interfaces.administrativo.PerfilTagSEIDecidirEnum;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import relatorio.arquitetura.GeradorRelatorio;
import relatorio.negocio.comuns.arquitetura.CrosstabVO;
import relatorio.negocio.comuns.arquitetura.SuperParametroRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.financeiro.FiltroRelatorioFinanceiroVO;
import relatorio.negocio.comuns.processosel.FiltroRelatorioProcessoSeletivoVO;
import relatorio.negocio.interfaces.administrativo.RelatorioSeiDecidirInterfaceFacade;

@Service
@Lazy
public class RelatorioSeiDecidir extends ControleAcesso implements RelatorioSeiDecidirInterfaceFacade {

	private static final long serialVersionUID = 4107321165649122718L;

	private static final int WIDTH_COLUNA = 200;
	private static final String CAMPO = "campo";
	private static final String SUB_RELATORIO = "subRelatorio";
	private static final String SUBREPORT = "SUBREPORT";
	private static final String CROOSTAB = "crossTab";
	private static final String CROOSTAB_SUMARIO = "crossTabSumario";
	private static final String TITULO_CAMPO = "tituloCampo";

	public RelatorioSeiDecidir() {
	}

	public RelatorioSeiDecidir(Conexao conexao, FacadeFactory facade) {
		super();
		setConexao(conexao);
		setFacadeFactory(facade);
	}

	@Override
	public String realizarGeracaoRelatorioSeiDecidir(RelatorioSEIDecidirVO relatorioSEIDecidirVO, TipoRelatorioEnum tipoRelatorio, String urlLogoPadraoRelatorio, boolean gerarFormatoExportacaoDados, String campoSeparador, String extensaoArquivo, boolean verificarAcesso, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, ProgressBarVO progressBarVO) throws Exception {
		SqlRowSet sqlRowSet = null;
		try {
		validarDados(relatorioSEIDecidirVO);
		if(progressBarVO == null) {
			progressBarVO = new ProgressBarVO();
		} 
		progressBarVO.setStatus("Carregando Layout");
		LayoutRelatorioSEIDecidirVO layoutRelatorioSEIDecidirCloneVO = (LayoutRelatorioSEIDecidirVO) relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().clone();
		relatorioSEIDecidirVO.setLayoutRelatorioSEIDecidirVO(getFacadeFactory().getLayoutRelatorioSEIDecidirInterfaceFacade().consultarPorChavePrimaria(relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, false, usuarioVO));
		relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().setListaFiltroPersonalizadoVOs(layoutRelatorioSEIDecidirCloneVO.getListaFiltroPersonalizadoVOs());
//		tipoRelatorio = relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getTipoGeracaoRelatorio();
		if(gerarFormatoExportacaoDados){
			tipoRelatorio = TipoRelatorioEnum.EXCEL;
			relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().setAgruparRelatorioPor("");
			for(LayoutRelatorioSeiDecidirCampoVO campo: relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getLayoutRelatorioSeiDecidirCampoVOs()){
				campo.setTipoTotalizador(RelatorioSEIDecidirTipoTotalizadorEnum.NENHUM);
			}
		}
		realizarObtencaoTagGeracaoRelatorio(relatorioSEIDecidirVO);
		progressBarVO.setStatus("Consultando Dados de Geração do Relatório.");
		if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getLayoutPersonalizado()) {
			sqlRowSet = realizarConsultarGeracaoRelatorioLayoutPersonalizado(relatorioSEIDecidirVO, usuarioVO);
		} else {
			sqlRowSet = realizarConsultarGeracaoRelatorio(relatorioSEIDecidirVO, null, usuarioVO);
		}
		if (!sqlRowSet.next()) {
			throw new Exception(UteisJSF.internacionalizar("msg_relatorio_sem_dados"));
		}
		sqlRowSet.previous();
		sqlRowSet.last();		
		progressBarVO.setMaxValue(sqlRowSet.getRow()+3);
		sqlRowSet.beforeFirst();		
		progressBarVO.setStatus("Gerando do Relatório Com "+(progressBarVO.getMaxValue() - 3)+" registros.");
		if(relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getTipoGeracaoRelatorio().equals(TipoRelatorioEnum.PDF)) {
			return realizarGeracaoArquivoUsandoLayoutIReport(relatorioSEIDecidirVO, tipoRelatorio, urlLogoPadraoRelatorio, gerarFormatoExportacaoDados, sqlRowSet, usuarioVO, configuracaoGeralSistemaVO);			
		}else if(relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getTipoGeracaoRelatorio().equals(TipoRelatorioEnum.XML)) {			
			return realizarGeracaoArquivoXML(relatorioSEIDecidirVO, sqlRowSet, usuarioVO);
		}else if(gerarFormatoExportacaoDados && (extensaoArquivo.endsWith("csv") || extensaoArquivo.endsWith("txt"))) {
			return realizarCriacaoArquivoCsv(relatorioSEIDecidirVO, tipoRelatorio, urlLogoPadraoRelatorio, gerarFormatoExportacaoDados, campoSeparador, extensaoArquivo, sqlRowSet, usuarioVO, progressBarVO);
		}else {			
			return realizarCriacaoArquivo(relatorioSEIDecidirVO, tipoRelatorio, urlLogoPadraoRelatorio, gerarFormatoExportacaoDados, campoSeparador, extensaoArquivo, sqlRowSet, usuarioVO, progressBarVO);
		}
		}catch (Exception e) {
			throw e;
		}finally {
			sqlRowSet = null;
		}
	}

	private SuperParametroRelVO montarDadosSuperParametro(RelatorioSEIDecidirVO relatorioSEIDecidirVO, UsuarioVO usuarioVO) {
		SuperParametroRelVO superRelVO = new SuperParametroRelVO();
		superRelVO.setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
		superRelVO.setNomeUsuario(usuarioVO.getNome());
		superRelVO.setTituloRelatorio(relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getDescricao());
		return superRelVO;
	}

	/**
	 * Realiza a Geração do Relatorio no formato PDF a partir do {@link RelatorioSEIDecidirVO}.
	 * 
	 * @param relatorioSEIDecidirVO
	 * @param tipoRelatorio
	 * @param urlLogoPadraoRelatorio
	 * @param gerarFormatoExportacaoDados
	 * @param sqlRowSet
	 * @param usuarioVO
	 * @return
	 * @throws Exception
	 */
	private String realizarGeracaoArquivoUsandoLayoutIReport(RelatorioSEIDecidirVO relatorioSEIDecidirVO, TipoRelatorioEnum tipoRelatorio, 
			String urlLogoPadraoRelatorio, boolean gerarFormatoExportacaoDados, SqlRowSet sqlRowSet, UsuarioVO usuarioVO,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {

		relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().setListaRelatorioSEIDecidirArquivo(
				getFacadeFactory().getLayoutRelatorioSEIDecidirArquivoInterfaceFacade()
					.consultarPorLayoutRelatorioSeiDecidir(relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getCodigo()));
		Map<String, Object> parameters = new HashMap<String, Object>();

		if (Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getListaRelatorioSEIDecidirArquivo())) {
			JasperDesign jasperDesign = inicializarJasperDesign(relatorioSEIDecidirVO, configuracaoGeralSistemaVO);

			List<RelatorioDinamicoVO> listaRelatorio = montarDadosRelatorioPDF(relatorioSEIDecidirVO, usuarioVO, parameters,sqlRowSet);

			return realizarImpressaoRelatorio(relatorioSEIDecidirVO, usuarioVO, parameters, jasperDesign, listaRelatorio, urlLogoPadraoRelatorio, tipoRelatorio);
		} else {
			throw new Exception("Nenhum arquivo(jrxml) selecionado para este Relatório." + relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getDescricao());
		}
	}

	public JasperDesign inicializarJasperDesign(RelatorioSEIDecidirVO relatorioSEIDecidirVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws JRException {
		String caminhoFixo = configuracaoGeralSistemaVO.getLocalUploadArquivoFixo();
		String diretorio = (caminhoFixo.endsWith("/") ? caminhoFixo : caminhoFixo + File.separator) +
			relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getListaRelatorioSEIDecidirArquivo().get(0).getArquivoVO().getPastaBaseArquivo() + File.separator;
		String arquivo = diretorio + relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getListaRelatorioSEIDecidirArquivo().get(0).getArquivoVO().getNome();
		JasperDesign jasperDesign = JRXmlLoader.load(arquivo.replace("\\", File.separator).replace("/", File.separator));
		return jasperDesign;
	}

	/**
	 * Monta os dados do Relatorio 
	 * 
	 * @param relatorioSEIDecidirVO
	 * @param usuarioVO
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public List<RelatorioDinamicoVO> montarDadosRelatorioPDF(RelatorioSEIDecidirVO relatorioSEIDecidirVO, UsuarioVO usuarioVO, Map<String, Object> parameters, SqlRowSet resultSetRelatorioPrincipal) throws Exception {
		List<RelatorioDinamicoVO> listaRelatorio = new ArrayList<>();
		Map<String, Object> mapaParametrosRelatorio = new HashMap<>();

		int totalColunasRelatorio = relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getLayoutRelatorioSeiDecidirCampoVOs().size();
		int totalSubRelatorios = relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getListaRelatorioSEIDecidirArquivo().size();

		RelatorioSEIDecidirVO relatorioSEIDecidir = (RelatorioSEIDecidirVO) SerializationUtils.clone(relatorioSEIDecidirVO);

		while (resultSetRelatorioPrincipal.next()) {
			RelatorioDinamicoVO relatorioPrincipal = new RelatorioDinamicoVO();
			RelatorioDinamicoVO relatorioSubRelatorio = new RelatorioDinamicoVO();
			List<RelatorioDinamicoVO> listaSubRelatorio = new ArrayList<RelatorioDinamicoVO>();
			relatorioPrincipal.setUtilizarComoSumario(false);
			List<CrosstabVO> listaCrosstabVO = new ArrayList<CrosstabVO>();

			//monta campos relatorio principal
			for (int contadorRelPrincipal = 1; contadorRelPrincipal <= totalColunasRelatorio; contadorRelPrincipal++) {
				montarCampoRelatorio(relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO(), resultSetRelatorioPrincipal, relatorioPrincipal, contadorRelPrincipal, mapaParametrosRelatorio, parameters);
			}

			montarDadosSubRelatorio(relatorioSEIDecidirVO, usuarioVO, parameters, mapaParametrosRelatorio,
					totalSubRelatorios, relatorioSEIDecidir, relatorioPrincipal, relatorioSubRelatorio,
					listaSubRelatorio, listaCrosstabVO);

			if (Uteis.isAtributoPreenchido(listaCrosstabVO)) {
				relatorioPrincipal.getObjetos().put(CROOSTAB, relatorioPrincipal.getDataSourceListaCrossTab(listaCrosstabVO));
			}

			listaRelatorio.add(relatorioPrincipal);		
		}
		for(int contadorSubRelatorio = 1; contadorSubRelatorio < totalSubRelatorios; contadorSubRelatorio++) {
			if(relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getListaRelatorioSEIDecidirArquivo().get(contadorSubRelatorio).getUtilizarComoSumario()) {
				RelatorioDinamicoVO relatorioPrincipal = new RelatorioDinamicoVO();
				relatorioPrincipal.setUtilizarComoSumario(true);
				RelatorioDinamicoVO relatorioSubRelatorio = new RelatorioDinamicoVO();
				List<RelatorioDinamicoVO> listaSubRelatorio = new ArrayList<RelatorioDinamicoVO>();
				List<CrosstabVO> listaCrosstabVO = new ArrayList<CrosstabVO>();	
				LayoutRelatorioSEIDecidirVO layoutRelatorioSEIDecidirVO = inicializarLayoutRelatorioSeiDecidirSubRelatorio(relatorioSEIDecidirVO, usuarioVO, relatorioSEIDecidir, contadorSubRelatorio);
				realizarObtencaoTagGeracaoRelatorio(relatorioSEIDecidir);
				SqlRowSet sql = realizarConsultarGeracaoRelatorio(relatorioSEIDecidir, mapaParametrosRelatorio, null);
				while (sql.next()) {
					montarCamposSubRelatorio(relatorioPrincipal, relatorioSubRelatorio, listaSubRelatorio, listaCrosstabVO, contadorSubRelatorio, layoutRelatorioSEIDecidirVO, sql);
				}
				parameters.put(SUBREPORT + contadorSubRelatorio, relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getListaRelatorioSEIDecidirArquivo().get(contadorSubRelatorio).getArquivoVO().getPastaBaseArquivo() + File.separator +
					relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getListaRelatorioSEIDecidirArquivo().get(contadorSubRelatorio).getArquivoVO().getNome());				
				if (Uteis.isAtributoPreenchido(listaCrosstabVO)) {
					relatorioPrincipal.getObjetos().put(CROOSTAB, relatorioPrincipal.getDataSourceListaCrossTab(listaCrosstabVO));
				}
				listaRelatorio.add(relatorioPrincipal);		
			}				
		}
		return listaRelatorio;
	}

	/**
	 * Metodo responsavel por validar os dados e montar os dados do parametro.
	 *  
	 * @param relatorioSEIDecidirVO
	 * @param usuarioVO
	 * @param parameters
	 * @param mapaParametrosRelatorio
	 * @param totalSubRelatorios
	 * @param relatorioSEIDecidir
	 * @param relatorioPrincipal
	 * @param relatorioSubRelatorio
	 * @param listaSubRelatorio
	 * @param listaCrosstabVO
	 * @throws Exception
	 */
	public void montarDadosSubRelatorio(RelatorioSEIDecidirVO relatorioSEIDecidirVO, UsuarioVO usuarioVO, Map<String, Object> parameters, Map<String, Object> mapaParametrosRelatorio, int totalSubRelatorios,
			RelatorioSEIDecidirVO relatorioSEIDecidir, RelatorioDinamicoVO relatorioPrincipal, RelatorioDinamicoVO relatorioSubRelatorio, List<RelatorioDinamicoVO> listaSubRelatorio, List<CrosstabVO> listaCrosstabVO) throws Exception {
		
		//Se for menor que 1 so existe o relatório principal(Template)
		if (totalSubRelatorios > 1) {

			for(int contadorSubRelatorio = 1; contadorSubRelatorio < totalSubRelatorios; contadorSubRelatorio++) {
				if(!relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getListaRelatorioSEIDecidirArquivo().get(contadorSubRelatorio).getUtilizarComoSumario()) {
				LayoutRelatorioSEIDecidirVO layoutRelatorioSEIDecidirVO = inicializarLayoutRelatorioSeiDecidirSubRelatorio(relatorioSEIDecidirVO, usuarioVO, relatorioSEIDecidir, contadorSubRelatorio);
				realizarObtencaoTagGeracaoRelatorio(relatorioSEIDecidir);
				SqlRowSet sql = realizarConsultarGeracaoRelatorio(relatorioSEIDecidir, mapaParametrosRelatorio, null);

				while (sql.next()) {
					montarCamposSubRelatorio(relatorioPrincipal, relatorioSubRelatorio, listaSubRelatorio, listaCrosstabVO, contadorSubRelatorio, layoutRelatorioSEIDecidirVO, sql);
				}
				parameters.put(SUBREPORT + contadorSubRelatorio, relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getListaRelatorioSEIDecidirArquivo().get(contadorSubRelatorio).getArquivoVO().getPastaBaseArquivo() + File.separator +
						relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getListaRelatorioSEIDecidirArquivo().get(contadorSubRelatorio).getArquivoVO().getNome());
			}
		}
	}
	}

	/**
	 * Metodo que inicializa os dados do {@link LayoutRelatorioSEIDecidirVO} como 
	 * {@link LayoutRelatorioSeiDecidirCampoVO} é {@link LayoutRelatorioSEIDecidirArquivoVO}.
	 * 
	 * @param relatorioSEIDecidirVO
	 * @param usuarioVO
	 * @param relatorioSEIDecidir
	 * @param contadorSubRelatorio
	 * @return
	 * @throws Exception
	 */
	public LayoutRelatorioSEIDecidirVO inicializarLayoutRelatorioSeiDecidirSubRelatorio(RelatorioSEIDecidirVO relatorioSEIDecidirVO, UsuarioVO usuarioVO, RelatorioSEIDecidirVO relatorioSEIDecidir, int contadorSubRelatorio) throws Exception {
		LayoutRelatorioSEIDecidirVO layoutRelatorioSEIDecidirVO = relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getListaRelatorioSEIDecidirArquivo().get(contadorSubRelatorio).getLayoutRelatorioSEIDecidirSuperiorVO();
		layoutRelatorioSEIDecidirVO.setLayoutRelatorioSeiDecidirCampoVOs(getFacadeFactory().getLayoutRelatorioSEIDecidirCampoInterfaceFacade().consultarPorLayoutRelatorio(layoutRelatorioSEIDecidirVO, Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, usuarioVO));

		relatorioSEIDecidir.setLayoutRelatorioSEIDecidirArquivoVO(relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getListaRelatorioSEIDecidirArquivo().get(contadorSubRelatorio));
		relatorioSEIDecidir.setLayoutRelatorioSEIDecidirVO(layoutRelatorioSEIDecidirVO);
		return layoutRelatorioSEIDecidirVO;
	}

	public void montarCamposSubRelatorio(RelatorioDinamicoVO relatorioPrincipal, RelatorioDinamicoVO relatorioSubRelatorio, List<RelatorioDinamicoVO> listaSubRelatorio,
			List<CrosstabVO> listaCrosstabVO, int contadorSubRelatorio, LayoutRelatorioSEIDecidirVO layoutRelatorioSEIDecidirVO, SqlRowSet sql) throws Exception {
		CrosstabVO crosstabVO = new CrosstabVO();
		for (int contadorCampoSubReport = 1; contadorCampoSubReport <= layoutRelatorioSEIDecidirVO.getLayoutRelatorioSeiDecidirCampoVOs().size(); contadorCampoSubReport++) {
			montarCampoRelatorio(layoutRelatorioSEIDecidirVO, sql, relatorioSubRelatorio, contadorCampoSubReport, null, null);
			montarDadosCrosstab(listaCrosstabVO, layoutRelatorioSEIDecidirVO, sql, crosstabVO, contadorCampoSubReport);
		}
		listaCrosstabVO.add(crosstabVO);
		listaSubRelatorio.add(relatorioSubRelatorio);
		relatorioPrincipal.getObjetos().put(SUB_RELATORIO + contadorSubRelatorio, listaSubRelatorio);
	}

	/**
	 * Monta os dados la lista de {@link CrosstabVO}
	 * 
	 * @param listaCrosstabVO - List<CrosstabVO>
	 * @param layoutRelatorioSEIDecidirVO
	 * @param sql
	 * @param crosstabVO
	 * @param contadorCampoSubReport
	 * @throws Exception
	 */
	public void montarDadosCrosstab(List<CrosstabVO> listaCrosstabVO,
			LayoutRelatorioSEIDecidirVO layoutRelatorioSEIDecidirVO, SqlRowSet sql, CrosstabVO crosstabVO, int contadorCampoSubReport) throws Exception {

		if (layoutRelatorioSEIDecidirVO.getSubRelatorioCrossTab() && !Uteis.isAtributoPreenchido(layoutRelatorioSEIDecidirVO.getLayoutRelatorioSeiDecidirCampoVOs().get(contadorCampoSubReport - 1).getValorCrosstab())) {
			throw new Exception("Campo do Relatorio Layout SEI Decidir (" + layoutRelatorioSEIDecidirVO.getLayoutRelatorioSeiDecidirCampoVOs().get(contadorCampoSubReport - 1).getTitulo() + ") não foi informado o campo do 'CrossTab'.");
		}

		if (layoutRelatorioSEIDecidirVO.getSubRelatorioCrossTab()) {
			switch (layoutRelatorioSEIDecidirVO.getLayoutRelatorioSeiDecidirCampoVOs().get(contadorCampoSubReport - 1).getValorCrosstab()) {
			case LABEL_COLUNA:
				crosstabVO.setLabelColuna(sql.getString(CAMPO + contadorCampoSubReport));
				break;
			case LABEL_COLUNA2:
				crosstabVO.setLabelColuna2(sql.getString(CAMPO + contadorCampoSubReport));
				break;
			case LABEL_LINHA:
				crosstabVO.setLabelLinha(sql.getString(CAMPO + contadorCampoSubReport));
				break;
			case LABEL_LINHA2:
				crosstabVO.setLabelLinha2(sql.getString(CAMPO + contadorCampoSubReport));
				break;
			case LABEL_LINHA3:
				crosstabVO.setLabelLinha3(sql.getString(CAMPO + contadorCampoSubReport));
				break;
			case LABEL_LINHA4:
				crosstabVO.setLabelLinha4(sql.getString(CAMPO + contadorCampoSubReport));
				break;
			case VALOR:
				montarValorCrosstab(layoutRelatorioSEIDecidirVO, sql, crosstabVO, contadorCampoSubReport);
				break;
			case VALOR_2:
				montarValor2Crosstab(layoutRelatorioSEIDecidirVO, sql, crosstabVO, contadorCampoSubReport);
				break;
			default:
				break;
			}

			crosstabVO.setOrdemColuna(contadorCampoSubReport);
		}
	}

	/**
	 * Monta os valores do {@link CrosstabVO}.
	 * 
	 * @param layoutRelatorioSEIDecidirVO
	 * @param sql
	 * @param crosstabVO
	 * @param contadorCampoSubReport
	 */
	public void montarValorCrosstab(LayoutRelatorioSEIDecidirVO layoutRelatorioSEIDecidirVO, SqlRowSet sql,
			CrosstabVO crosstabVO, int contadorCampoSubReport) {
		switch (layoutRelatorioSEIDecidirVO.getLayoutRelatorioSeiDecidirCampoVOs().get(contadorCampoSubReport - 1).getTipoCampo()) {
		case BIG_DECIMAL:
			crosstabVO.setValorBigDecimal(sql.getBigDecimal(CAMPO + contadorCampoSubReport));
			break;
		case BOOLEAN:
			crosstabVO.setValorBoolean(sql.getBoolean(CAMPO + contadorCampoSubReport));
			break;
		case DOUBLE:
			crosstabVO.setValorDouble(sql.getDouble(CAMPO + contadorCampoSubReport));
			break;
		case INTEIRO:
			crosstabVO.setValorInteger(sql.getInt(CAMPO + contadorCampoSubReport));
			break;
		case TEXTO:
			crosstabVO.setValorString(sql.getString(CAMPO + contadorCampoSubReport));
			break;
		default:
			break;
		}
	}

	public void montarValor2Crosstab(LayoutRelatorioSEIDecidirVO layoutRelatorioSEIDecidirVO, SqlRowSet sql,
			CrosstabVO crosstabVO, int contadorCampoSubReport) {
		switch (layoutRelatorioSEIDecidirVO.getLayoutRelatorioSeiDecidirCampoVOs().get(contadorCampoSubReport - 1).getTipoCampo()) {
		case BIG_DECIMAL:			
			crosstabVO.setValorBigDecimal2(sql.getBigDecimal(CAMPO + contadorCampoSubReport));
			break;
		case BOOLEAN:
			crosstabVO.setValorBoolean2(sql.getBoolean(CAMPO + contadorCampoSubReport));
			break;
		case DOUBLE:
			crosstabVO.setValorDouble2(sql.getDouble(CAMPO + contadorCampoSubReport));
			break;
		case INTEIRO:
			crosstabVO.setValorInteger2(sql.getInt(CAMPO + contadorCampoSubReport));
			break;
		case TEXTO:
			crosstabVO.setValorString2(sql.getString(CAMPO + contadorCampoSubReport));
			break;
		default:
			break;
		}
	}

	public String realizarImpressaoRelatorio(RelatorioSEIDecidirVO relatorioSEIDecidirVO, UsuarioVO usuarioVO, Map<String, Object> parameters, JasperDesign jasperDesign, List<RelatorioDinamicoVO> listaRelatorio, String urlLogoPadraoRelatorio, TipoRelatorioEnum tipoRelatorio) throws JRException, Exception {
		jasperDesign.setName("templateRelatorio");

		JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
		JRBeanCollectionDataSource jbds = new JRBeanCollectionDataSource(listaRelatorio);

		String tituloRelatorio = relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getDescricao();

		switch (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getNivelDetalhamento()) {
		case PERIODO_AQUISITIVO:
			if (Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getSituacaoPeriodoAquisitivo())) {
				tituloRelatorio += " " + relatorioSEIDecidirVO.getSituacaoPeriodoAquisitivo();
			}
			break;
		case EVENTO_FOLHA_PAGAMENTO:
			parameters.put("nomeEvento", relatorioSEIDecidirVO.getEventoFolhaPagamentoVO().getDescricao());
			break;
		default:
			break;
		}

		parameters.put("tituloRelatorio", tituloRelatorio);			
		parameters.put("logoPadraoRelatorio", urlLogoPadraoRelatorio);
		parameters.put("nomeUsuario", usuarioVO.getNome());
		parameters.put("versaoSoftware", "SEI - " + Uteis.VERSAO_SISTEMA);
		parameters.put("filtro", relatorioSEIDecidirVO.getFiltro().toString());
		parameters.put("SUBREPORT_DIR", getAplicacaoControle().getConfiguracaoGeralSistemaVO(null, usuarioVO).getLocalUploadArquivoFixo().endsWith(File.separator) ? getAplicacaoControle().getConfiguracaoGeralSistemaVO(null, usuarioVO).getLocalUploadArquivoFixo() : getAplicacaoControle().getConfiguracaoGeralSistemaVO(null, usuarioVO).getLocalUploadArquivoFixo()+File.separator);



		if (Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getDataFimPeriodo())) {
			parameters.put("ANO_COMPETENCIA", String.valueOf(UteisData.getAnoData(relatorioSEIDecidirVO.getDataFimPeriodo())));
		}

		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, jbds);

		SuperParametroRelVO superRelVO = montarDadosSuperParametro(relatorioSEIDecidirVO, usuarioVO);

		GeradorRelatorio geradorRelatorio = new GeradorRelatorio();
		if(tipoRelatorio.equals(TipoRelatorioEnum.PDF)) {
			return geradorRelatorio.realizarExportacaoPDF(superRelVO, jasperPrint);
		}else {
			return geradorRelatorio.realizarExportacaoEXCEL(superRelVO, jasperPrint);
		}
	}

	/**
	 * Monta o campo do relatorio de acordo com o valor do tipo do campo informado no cadastro de {@link LayoutRelatorioSeiDecidirCampoVO}
	 *  
	 * @param relatorioSEIDecidirVO
	 * @param sqlRowSet
	 * @param relatorioPrincipal
	 * @param contador
	 */
	private void montarCampoRelatorio(LayoutRelatorioSEIDecidirVO layoutRelatorioSEIDecidirVO, SqlRowSet sqlRowSet, RelatorioDinamicoVO relatorioPrincipal, int contador, Map<String, Object> listaParametrosRelatorio, Map<String, Object> parameters) {
		if(parameters != null) {
		parameters.put(TITULO_CAMPO + contador, layoutRelatorioSEIDecidirVO.getLayoutRelatorioSeiDecidirCampoVOs().get(contador - 1).getTitulo());
		}
		switch (layoutRelatorioSEIDecidirVO.getLayoutRelatorioSeiDecidirCampoVOs().get(contador - 1).getTipoCampo()) {
		case BIG_DECIMAL:
			relatorioPrincipal.getObjetos().put(CAMPO + contador, sqlRowSet.getBigDecimal(CAMPO + contador));
			break;
		case BOOLEAN:
			relatorioPrincipal.getObjetos().put(CAMPO + contador, sqlRowSet.getBoolean(CAMPO + contador));
			break;
		case DATA:
			if (sqlRowSet.getDate(CAMPO + contador) != null) {
				relatorioPrincipal.getObjetos().put(CAMPO + contador, UteisData.getDataFormatada(sqlRowSet.getDate(CAMPO + contador)));
			}
			relatorioPrincipal.getObjetos().put("data" + contador, sqlRowSet.getDate(CAMPO + contador));
			break;
		case DOUBLE:
			relatorioPrincipal.getObjetos().put(CAMPO + contador, sqlRowSet.getDouble(CAMPO + contador));
			break;
		case INTEIRO:
			relatorioPrincipal.getObjetos().put(CAMPO + contador, sqlRowSet.getInt(CAMPO + contador));
			break;
		case TEXTO:
			relatorioPrincipal.getObjetos().put(CAMPO + contador, sqlRowSet.getString(CAMPO + contador));
			break;
		case MES_ANO:
			relatorioPrincipal.getObjetos().put(CAMPO + contador, sqlRowSet.getString(CAMPO + contador));
			break;
		default:
			break;
		}

		if (listaParametrosRelatorio != null) {
			LayoutRelatorioSeiDecidirCampoVO layoutRelatorioSeiDecidirCampoVO = layoutRelatorioSEIDecidirVO.getLayoutRelatorioSeiDecidirCampoVOs().get(contador-1);
			if (Uteis.isAtributoPreenchido(layoutRelatorioSeiDecidirCampoVO) && layoutRelatorioSeiDecidirCampoVO.getUtilizarParametroRelatorio()) {
				listaParametrosRelatorio.put(layoutRelatorioSeiDecidirCampoVO.getCampo() ,relatorioPrincipal.getObjetos().get(CAMPO + contador));
				listaParametrosRelatorio.put(CAMPO + contador ,relatorioPrincipal.getObjetos().get(CAMPO + contador));
			}
		}
	}

	private String realizarCriacaoArquivo(RelatorioSEIDecidirVO relatorioSEIDecidirVO, TipoRelatorioEnum tipoRelatorio, String urlLogoPadraoRelatorio, boolean gerarFormatoExportacaoDados, String campoSeparador, String extensaoArquivo, SqlRowSet sqlRowSet, UsuarioVO usuarioVO, ProgressBarVO progressBarVO) throws Exception {
		String nomePadrao = (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getNomePadraoArquivo().trim().isEmpty() ? "SEIDECIDIR_" : relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getNomePadraoArquivo().trim());
		String nomeArquivo =  nomePadrao + Uteis.getData(new Date(), "dd_MM_yy_hh_mm_ss") + "_" + usuarioVO.getCodigo() + ".xlsx";
		String nomeArquivoHtml = nomePadrao + Uteis.getData(new Date(), "dd_MM_yy_hh_mm_ss") + "_" + usuarioVO.getCodigo() + ".html";
		String nomeArquivoCsv = nomePadrao + Uteis.getData(new Date(), "dd_MM_yy_hh_mm_ss") + "_" + usuarioVO.getCodigo() + "."+extensaoArquivo;
		FileOutputStream fileOut = null;	
		
		
		XSSFWorkbook workbook = null;
		XSSFSheet worksheetfiltros = null;
		XSSFSheet worksheetresultado = null;
		UteisExcel uteisExcel = new UteisExcel();
		try {
			String fileName  = (Uteis.isAtributoPreenchido(progressBarVO.getCaminhoWebRelatorio()) ? progressBarVO.getCaminhoWebRelatorio() : UteisJSF.getCaminhoWeb()) + File.separator + "relatorio" + File.separator + nomeArquivo;
			fileOut = new FileOutputStream(fileName);	
					
			workbook = new XSSFWorkbook();
			
			if(!gerarFormatoExportacaoDados){
				worksheetfiltros = workbook.createSheet("Filtros do Relatório");
				worksheetfiltros.setAutobreaks(true);
				worksheetfiltros.setVerticallyCenter(true);
			}
			worksheetresultado = workbook.createSheet("Resultado Relatório");			
			if(!gerarFormatoExportacaoDados){
				worksheetresultado.setAutobreaks(true);
				worksheetresultado.setVerticallyCenter(true);
				worksheetresultado.createFreezePane(0, 7 );
			}			
			if(!gerarFormatoExportacaoDados){
				uteisExcel.realizarGeracaoTopoPadraoRelatorio(workbook, worksheetfiltros, urlLogoPadraoRelatorio, relatorioSEIDecidirVO , null, relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getDescricao());
				//realizarGeracaoTopoPadraoRelatorio(relatorioSEIDecidirVO, workbook, worksheetfiltros, urlLogoPadraoRelatorio);
				realizarGeracaoFiltrosRelatoriosRelatorio(relatorioSEIDecidirVO, workbook, worksheetfiltros);
				//realizarGeracaoTopoPadraoRelatorio(relatorioSEIDecidirVO, workbook, worksheetresultado, urlLogoPadraoRelatorio);
				uteisExcel.realizarGeracaoTopoPadraoRelatorio(workbook, worksheetresultado, urlLogoPadraoRelatorio, relatorioSEIDecidirVO , null, relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getDescricao());
				realizarGeracaoCabecalhoRelatorio(worksheetresultado, relatorioSEIDecidirVO);
				workbook.setActiveSheet(1);
			}
			if(gerarFormatoExportacaoDados){
				int coluna = 0;				
				for (LayoutRelatorioSeiDecidirCampoVO campo : relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getLayoutRelatorioSeiDecidirCampoVOs()) {
					worksheetresultado.setColumnWidth(coluna, campo.getTamanhoColuna() * RelatorioSeiDecidir.WIDTH_COLUNA);
					coluna++;
				}
			}			
			realizarGeracaoCorpoRelatorio(fileOut, workbook, fileName, worksheetresultado, sqlRowSet, gerarFormatoExportacaoDados, relatorioSEIDecidirVO, progressBarVO);			
			workbook.write(fileOut);
			if (tipoRelatorio.equals(TipoRelatorioEnum.HTML)) {
				ToHtml.create(workbook, new PrintWriter(new FileWriter(nomeArquivoHtml)));
			}else if(tipoRelatorio.equals(TipoRelatorioEnum.EXCEL) && gerarFormatoExportacaoDados) {
				DataFormatter formatter = new DataFormatter();
				OutputStream os = (OutputStream)new FileOutputStream(UteisJSF.getCaminhoWeb() + File.separator + "relatorio" + File.separator+nomeArquivoCsv);
				String encoding = "UTF8";
				OutputStreamWriter osw = new OutputStreamWriter(os, encoding);
				BufferedWriter bw = new BufferedWriter(osw);
				for (int x = 0 ; x <= workbook.getSheetAt(0).getLastRowNum(); x++) {
					if(x > 0) {
						bw.write("\n");
			}
					XSSFRow row = workbook.getSheetAt(0).getRow(x);
					boolean firstCell = true;
					for (int y = 0 ; y <= row.getLastCellNum(); y++) {
						XSSFCell cell = row.getCell(y);
						if(!firstCell) {
							bw.write(campoSeparador);
						}
						String text = formatter.formatCellValue(cell);
						bw.write(text);					
						firstCell = false;
					}					       
				}
				bw.write("\n");
				bw.flush();
			    bw.close();
			    os.flush();
			    os.close();
			}
		} catch (Exception e) {
			throw e;
		} finally {
			
			if (fileOut != null) {
				fileOut.flush();
				fileOut.close();
			}
			fileOut = null;
			workbook = null;
			worksheetfiltros = null;
			worksheetresultado = null;
		}
		if (tipoRelatorio.equals(TipoRelatorioEnum.HTML)) {
			return nomeArquivoHtml;
		}else if(tipoRelatorio.equals(TipoRelatorioEnum.EXCEL) && gerarFormatoExportacaoDados) {
			return nomeArquivoCsv;
		} else {
			return nomeArquivo;
		}
	}

	public void realizarGeracaoFiltrosRelatoriosRelatorio(RelatorioSEIDecidirVO relatorioSEIDecidirVO, XSSFWorkbook workbook, XSSFSheet worksheet) throws Exception {

		XSSFRow cabecalho = null;
		XSSFCell cellCabecalho = null;

		cabecalho = worksheet.createRow(worksheet.getLastRowNum() + 1);
		worksheet.addMergedRegion(new CellRangeAddress(worksheet.getLastRowNum(), worksheet.getLastRowNum(), 0, 1));

		cellCabecalho = cabecalho.createCell(0);
		cellCabecalho.setCellType(XSSFCell.CELL_TYPE_STRING);		
		cellCabecalho.setCellValue("Filtros");		
		cellCabecalho.setCellStyle(getCss(workbook, HSSFColor.GREY_25_PERCENT.index, XSSFCellStyle.ALIGN_LEFT, "", true, true, true, true, HSSFColor.BLACK.index, false));
		
		cellCabecalho = cabecalho.createCell(1);
		cellCabecalho.setCellType(XSSFCell.CELL_TYPE_STRING);						
		cellCabecalho.setCellStyle(getCss(workbook, HSSFColor.GREY_25_PERCENT.index, XSSFCellStyle.ALIGN_LEFT, "", true, true, true, true, HSSFColor.BLACK.index, false));

		if (!relatorioSEIDecidirVO.getUnidadeEnsinoApresentar().trim().isEmpty() && !relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.PROCESSO_SELETIVO)) {
			adicionarFiltroRelatorioExcel(worksheet, UteisJSF.internacionalizar("prt_ContaPagar_unidadeEnsino") + ": ", relatorioSEIDecidirVO.getUnidadeEnsinoApresentar());
		}
		
		if (!relatorioSEIDecidirVO.getCentroResultadoApresentar().trim().isEmpty()) {
			adicionarFiltroRelatorioExcel(worksheet, "Centro Resultado: ", relatorioSEIDecidirVO.getCentroResultadoApresentar());
		}
		if (Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getQtdNivelExistenteCentroResultado())) {
			adicionarFiltroRelatorioExcel(worksheet, "Nível Centro Resultado: ", relatorioSEIDecidirVO.getQtdNivelExistenteCentroResultado().toString());
		}
		
		if (!relatorioSEIDecidirVO.getCampanhaApresentar().trim().isEmpty()) {
			adicionarFiltroRelatorioExcel(worksheet, "Campanhas" + ": ", relatorioSEIDecidirVO.getCampanhaApresentar());
		}
		
		if (!relatorioSEIDecidirVO.getFuncionarioApresentar().trim().isEmpty() && relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.CRM)) {
			adicionarFiltroRelatorioExcel(worksheet, "Consultores" + ": ", relatorioSEIDecidirVO.getFuncionarioApresentar());
		}

		if (!relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltrosFinanceirosDespesa() && !relatorioSEIDecidirVO.getNivelEducacional().trim().isEmpty() && !relatorioSEIDecidirVO.getNivelEducacional().equals("0")) {
			adicionarFiltroRelatorioExcel(worksheet, UteisJSF.internacionalizar("prt_ReceitaDW_nivelEducacional") + ":", TipoNivelEducacional.getDescricao(relatorioSEIDecidirVO.getNivelEducacional()));
		}
		if (!relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltrosFinanceirosDespesa() && !relatorioSEIDecidirVO.getCursoApresentar().trim().isEmpty()) {
			adicionarFiltroRelatorioExcel(worksheet, UteisJSF.internacionalizar("prt_Matricula_curso") + ":", relatorioSEIDecidirVO.getCursoApresentar());
		}
		if (!relatorioSEIDecidirVO.getGradeCurricularEstagioApresentar().trim().isEmpty()) {
			adicionarFiltroRelatorioExcel(worksheet, "Componente Estágio" + ":", relatorioSEIDecidirVO.getGradeCurricularEstagioApresentar());
		}
		if (!relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltrosFinanceirosDespesa() && !relatorioSEIDecidirVO.getTurnoApresentar().trim().isEmpty()) {
			adicionarFiltroRelatorioExcel(worksheet, UteisJSF.internacionalizar("prt_Matricula_turno") + ":", relatorioSEIDecidirVO.getTurnoApresentar());

		}
		if (!relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltrosFinanceirosDespesa() && relatorioSEIDecidirVO.getTurmaVO().getCodigo() > 0) {
			adicionarFiltroRelatorioExcel(worksheet, UteisJSF.internacionalizar("prt_LancamentoNota_turma") + ":", relatorioSEIDecidirVO.getTurmaVO().getIdentificadorTurma());
		}
		if (!relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltrosFinanceirosDespesa() && !relatorioSEIDecidirVO.getCentroReceitaApresentar().trim().isEmpty()) {
			adicionarFiltroRelatorioExcel(worksheet, UteisJSF.internacionalizar("prt_ContaReceber_centroReceita") + ":", relatorioSEIDecidirVO.getCentroReceitaApresentar());

		}
		if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltrosFinanceirosDespesa() && !relatorioSEIDecidirVO.getCategoriaDespesaApresentar().trim().isEmpty()) {
			adicionarFiltroRelatorioExcel(worksheet, UteisJSF.internacionalizar("prt_ContaPagar_centroDespesa") + ":", relatorioSEIDecidirVO.getCategoriaDespesaApresentar());
			
		}
		if ((relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltrosFinanceirosDespesa() || relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltrosFinanceirosReceita()) && !relatorioSEIDecidirVO.getFormaPagamentoVO().getNome().trim().isEmpty()) {
			adicionarFiltroRelatorioExcel(worksheet,  "Forma Pagamento:", relatorioSEIDecidirVO.getFormaPagamentoVO().getNome());
			
		}
		if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltrosFinanceirosReceita() && relatorioSEIDecidirVO.getContaCorrenteVO().getCodigo() > 0) {
			relatorioSEIDecidirVO.setContaCorrenteVO(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(relatorioSEIDecidirVO.getContaCorrenteVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, null));
			adicionarFiltroRelatorioExcel(worksheet, UteisJSF.internacionalizar("prt_Recebimento_contaCorrente") + ":", relatorioSEIDecidirVO.getContaCorrenteVO().getBancoAgenciaContaCorrente());

		}
		if (!relatorioSEIDecidirVO.getTipoPessoaEnum().equals(TipoPessoa.NENHUM) && relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltrosFinanceirosReceita()) {
			String pessoa = relatorioSEIDecidirVO.getTipoPessoaEnum().getDescricao();
			if (relatorioSEIDecidirVO.getTipoPessoaEnum().equals(TipoPessoa.ALUNO) && !relatorioSEIDecidirVO.getMatriculaVO().getMatricula().trim().isEmpty()) {
				pessoa += ": " + relatorioSEIDecidirVO.getMatriculaVO().getMatricula() + " - " + relatorioSEIDecidirVO.getMatriculaVO().getAluno().getNome();
			} else if (relatorioSEIDecidirVO.getTipoPessoaEnum().equals(TipoPessoa.PARCEIRO) && relatorioSEIDecidirVO.getParceiroVO().getCodigo() > 0) {
				pessoa += " - " + relatorioSEIDecidirVO.getParceiroVO().getNome();
			} else if (relatorioSEIDecidirVO.getTipoPessoaEnum().equals(TipoPessoa.FUNCIONARIO) && relatorioSEIDecidirVO.getFuncionarioVO().getCodigo() > 0) {
				pessoa += " - " + relatorioSEIDecidirVO.getFuncionarioVO().getPessoa().getNome();
			} else if (relatorioSEIDecidirVO.getTipoPessoaEnum().equals(TipoPessoa.CANDIDATO) && relatorioSEIDecidirVO.getCandidatosVagasVO().getPessoa().getCodigo() > 0) {
				pessoa += " - " + relatorioSEIDecidirVO.getCandidatosVagasVO().getPessoa().getNome();
			} else if (relatorioSEIDecidirVO.getTipoPessoaEnum().equals(TipoPessoa.FORNECEDOR) && relatorioSEIDecidirVO.getFornecedorVO().getCodigo() > 0) {
				pessoa += " - " + relatorioSEIDecidirVO.getFornecedorVO().getNome();
			} else if (!relatorioSEIDecidirVO.getTipoPessoaEnum().equals(TipoPessoa.NENHUM) && relatorioSEIDecidirVO.getPessoaVO().getCodigo() > 0) {
				pessoa += " - " + relatorioSEIDecidirVO.getPessoaVO().getNome();
			}
			adicionarFiltroRelatorioExcel(worksheet, UteisJSF.internacionalizar("prt_ContaReceber_tipoPessoa") + ":", pessoa);
		}else if (!relatorioSEIDecidirVO.getTipoPessoaEnum().equals(TipoPessoa.NENHUM) && relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltrosFinanceirosDespesa()) {
			String pessoa = relatorioSEIDecidirVO.getTipoPessoaEnum().getDescricao();
			if (relatorioSEIDecidirVO.getTipoPessoaEnum().equals(TipoPessoa.ALUNO) && !relatorioSEIDecidirVO.getMatriculaVO().getMatricula().trim().isEmpty()) {
				pessoa += ": " + relatorioSEIDecidirVO.getMatriculaVO().getMatricula() + " - " + relatorioSEIDecidirVO.getMatriculaVO().getAluno().getNome();
			} else if (relatorioSEIDecidirVO.getTipoPessoaEnum().equals(TipoPessoa.PARCEIRO) && relatorioSEIDecidirVO.getParceiroVO().getCodigo() > 0) {
				pessoa += " - " + relatorioSEIDecidirVO.getParceiroVO().getNome();
			} else if (relatorioSEIDecidirVO.getTipoPessoaEnum().equals(TipoPessoa.FUNCIONARIO) && relatorioSEIDecidirVO.getFuncionarioVO().getCodigo() > 0) {
				pessoa += " - " + relatorioSEIDecidirVO.getFuncionarioVO().getPessoa().getNome();
			} else if (relatorioSEIDecidirVO.getTipoPessoaEnum().equals(TipoPessoa.CANDIDATO) && relatorioSEIDecidirVO.getCandidatosVagasVO().getPessoa().getCodigo() > 0) {
				pessoa += " - " + relatorioSEIDecidirVO.getCandidatosVagasVO().getPessoa().getNome();
			} else if (relatorioSEIDecidirVO.getTipoPessoaEnum().equals(TipoPessoa.FORNECEDOR) && relatorioSEIDecidirVO.getFornecedorVO().getCodigo() > 0) {
				pessoa += " - " + relatorioSEIDecidirVO.getFornecedorVO().getNome();
			} else if (!relatorioSEIDecidirVO.getTipoPessoaEnum().equals(TipoPessoa.NENHUM) && relatorioSEIDecidirVO.getPessoaVO().getCodigo() > 0) {
				pessoa += " - " + relatorioSEIDecidirVO.getPessoaVO().getNome();
			}
			adicionarFiltroRelatorioExcel(worksheet, UteisJSF.internacionalizar("prt_ContaReceber_tipoPessoa") + ":", pessoa);
		} else if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltrosMatricula() && !relatorioSEIDecidirVO.getMatriculaVO().getMatricula().trim().isEmpty()) {
			adicionarFiltroRelatorioExcel(worksheet, UteisJSF.internacionalizar("prt_Matricula_matricula") + ":", relatorioSEIDecidirVO.getMatriculaVO().getMatricula() + " - " + relatorioSEIDecidirVO.getMatriculaVO().getAluno().getNome());

		}

		switch (relatorioSEIDecidirVO.getTipoFiltroPeriodo()) {
		case ANO_SEMESTRE:
			adicionarFiltroRelatorioExcel(worksheet, TipoFiltroPeriodoSeiDecidirEnum.ANO_SEMESTRE.getValorApresentar() + ":", relatorioSEIDecidirVO.getAno() + "/" + relatorioSEIDecidirVO.getSemestre());
			break;
		case DATA_COMPETENCIA:
			adicionarFiltroRelatorioExcel(worksheet, TipoFiltroPeriodoSeiDecidirEnum.DATA_COMPETENCIA.getValorApresentar() + ":", Uteis.getData(relatorioSEIDecidirVO.getDataInicio()) + " à " + Uteis.getData(relatorioSEIDecidirVO.getDataFim()));
			break;
		case DATA_MATRICULA:
			adicionarFiltroRelatorioExcel(worksheet, TipoFiltroPeriodoSeiDecidirEnum.DATA_MATRICULA.getValorApresentar() + ":", Uteis.getData(relatorioSEIDecidirVO.getDataInicio()) + " à " + Uteis.getData(relatorioSEIDecidirVO.getDataFim()));
			break;
		case DATA_MATRICULA_E_ANO_SEMESTRE:
			adicionarFiltroRelatorioExcel(worksheet, TipoFiltroPeriodoSeiDecidirEnum.DATA_MATRICULA.getValorApresentar() + ":", Uteis.getData(relatorioSEIDecidirVO.getDataInicio()) + " à " + Uteis.getData(relatorioSEIDecidirVO.getDataFim()));
			adicionarFiltroRelatorioExcel(worksheet, TipoFiltroPeriodoSeiDecidirEnum.ANO_SEMESTRE.getValorApresentar() + ":", relatorioSEIDecidirVO.getAno() + "/" + relatorioSEIDecidirVO.getSemestre());
			break;
		case DATA_RECEBIMENTO:
			adicionarFiltroRelatorioExcel(worksheet, TipoFiltroPeriodoSeiDecidirEnum.DATA_RECEBIMENTO.getValorApresentar() + ":", Uteis.getData(relatorioSEIDecidirVO.getDataInicio()) + " à " + Uteis.getData(relatorioSEIDecidirVO.getDataFim()));
			break;
		case DATA_VENCIMENTO:
			adicionarFiltroRelatorioExcel(worksheet, TipoFiltroPeriodoSeiDecidirEnum.DATA_VENCIMENTO.getValorApresentar() + ":", Uteis.getData(relatorioSEIDecidirVO.getDataInicio()) + " à " + Uteis.getData(relatorioSEIDecidirVO.getDataFim()));
			break;
		case DATA_RECEBIMENTO_E_DATA_COMPETENCIA:
			adicionarFiltroRelatorioExcel(worksheet, TipoFiltroPeriodoSeiDecidirEnum.DATA_RECEBIMENTO.getValorApresentar() + ":", Uteis.getData(relatorioSEIDecidirVO.getDataInicio()) + " à " + Uteis.getData(relatorioSEIDecidirVO.getDataFim()));
			adicionarFiltroRelatorioExcel(worksheet, TipoFiltroPeriodoSeiDecidirEnum.DATA_COMPETENCIA.getValorApresentar() + ":", Uteis.getData(relatorioSEIDecidirVO.getDataInicio2()) + " à " + Uteis.getData(relatorioSEIDecidirVO.getDataFim2()));
			break;
		case DATA_VENCIMENTO_E_DATA_COMPETENCIA:
			adicionarFiltroRelatorioExcel(worksheet, TipoFiltroPeriodoSeiDecidirEnum.DATA_VENCIMENTO.getValorApresentar() + ":", Uteis.getData(relatorioSEIDecidirVO.getDataInicio()) + " à " + Uteis.getData(relatorioSEIDecidirVO.getDataFim()));
			adicionarFiltroRelatorioExcel(worksheet, TipoFiltroPeriodoSeiDecidirEnum.DATA_COMPETENCIA.getValorApresentar() + ":", Uteis.getData(relatorioSEIDecidirVO.getDataInicio2()) + " à " + Uteis.getData(relatorioSEIDecidirVO.getDataFim2()));
			
			break;
		case DATA_VENCIMENTO_E_DATA_RECEBIMENTO:
			adicionarFiltroRelatorioExcel(worksheet, TipoFiltroPeriodoSeiDecidirEnum.DATA_VENCIMENTO.getValorApresentar() + ":", Uteis.getData(relatorioSEIDecidirVO.getDataInicio()) + " à " + Uteis.getData(relatorioSEIDecidirVO.getDataFim()));
			adicionarFiltroRelatorioExcel(worksheet, TipoFiltroPeriodoSeiDecidirEnum.DATA_RECEBIMENTO.getValorApresentar() + ":", Uteis.getData(relatorioSEIDecidirVO.getDataInicio2()) + " à " + Uteis.getData(relatorioSEIDecidirVO.getDataFim2()));			
		case DATA_VENCIMENTO_E_DATA_COMPENSACAO:
			adicionarFiltroRelatorioExcel(worksheet, TipoFiltroPeriodoSeiDecidirEnum.DATA_VENCIMENTO.getValorApresentar() + ":", Uteis.getData(relatorioSEIDecidirVO.getDataInicio()) + " à " + Uteis.getData(relatorioSEIDecidirVO.getDataFim()));
			adicionarFiltroRelatorioExcel(worksheet, TipoFiltroPeriodoSeiDecidirEnum.DATA_COMPENSACAO.getValorApresentar() + ":", Uteis.getData(relatorioSEIDecidirVO.getDataInicio2()) + " à " + Uteis.getData(relatorioSEIDecidirVO.getDataFim2()));			
			break;
		case DATA_RECEBIMENTO_E_DATA_COMPENSACAO:
			adicionarFiltroRelatorioExcel(worksheet, TipoFiltroPeriodoSeiDecidirEnum.DATA_RECEBIMENTO.getValorApresentar() + ":", Uteis.getData(relatorioSEIDecidirVO.getDataInicio()) + " à " + Uteis.getData(relatorioSEIDecidirVO.getDataFim()));
			adicionarFiltroRelatorioExcel(worksheet, TipoFiltroPeriodoSeiDecidirEnum.DATA_COMPENSACAO.getValorApresentar() + ":", Uteis.getData(relatorioSEIDecidirVO.getDataInicio2()) + " à " + Uteis.getData(relatorioSEIDecidirVO.getDataFim2()));			
			break;		
		case DATA_COMPETENCIA_DATA_COMPENSACAO:
			adicionarFiltroRelatorioExcel(worksheet, TipoFiltroPeriodoSeiDecidirEnum.DATA_COMPETENCIA.getValorApresentar() + ":", Uteis.getData(relatorioSEIDecidirVO.getDataInicio()) + " à " + Uteis.getData(relatorioSEIDecidirVO.getDataFim()));
			adicionarFiltroRelatorioExcel(worksheet, TipoFiltroPeriodoSeiDecidirEnum.DATA_COMPENSACAO.getValorApresentar() + ":", Uteis.getData(relatorioSEIDecidirVO.getDataInicio2()) + " à " + Uteis.getData(relatorioSEIDecidirVO.getDataFim2()));			
			break;
		case DATA_PAGAMENTO:			
			adicionarFiltroRelatorioExcel(worksheet, TipoFiltroPeriodoSeiDecidirEnum.DATA_PAGAMENTO.getValorApresentar() + ":", Uteis.getData(relatorioSEIDecidirVO.getDataInicio()) + " à " + Uteis.getData(relatorioSEIDecidirVO.getDataFim()));
			break;
		case DATA_PAGAMENTO_E_DATA_COMPENSACAO:
			adicionarFiltroRelatorioExcel(worksheet, TipoFiltroPeriodoSeiDecidirEnum.DATA_PAGAMENTO.getValorApresentar() + ":", Uteis.getData(relatorioSEIDecidirVO.getDataInicio()) + " à " + Uteis.getData(relatorioSEIDecidirVO.getDataFim()));
			adicionarFiltroRelatorioExcel(worksheet, TipoFiltroPeriodoSeiDecidirEnum.DATA_COMPENSACAO.getValorApresentar()+":", Uteis.getData(relatorioSEIDecidirVO.getDataInicio2()) + " à " + Uteis.getData(relatorioSEIDecidirVO.getDataFim2()));
			break;
		case DATA_PAGAMENTO_E_DATA_COMPETENCIA:
			adicionarFiltroRelatorioExcel(worksheet, TipoFiltroPeriodoSeiDecidirEnum.DATA_PAGAMENTO.getValorApresentar() + ":", Uteis.getData(relatorioSEIDecidirVO.getDataInicio()) + " à " + Uteis.getData(relatorioSEIDecidirVO.getDataFim()));
			adicionarFiltroRelatorioExcel(worksheet, TipoFiltroPeriodoSeiDecidirEnum.DATA_COMPETENCIA.getValorApresentar() + ":", Uteis.getData(relatorioSEIDecidirVO.getDataInicio2()) + " à " + Uteis.getData(relatorioSEIDecidirVO.getDataFim2()));			
			break;
		case DATA_COMPENSACAO:
			adicionarFiltroRelatorioExcel(worksheet, TipoFiltroPeriodoSeiDecidirEnum.DATA_COMPENSACAO.getValorApresentar() + ":", Uteis.getData(relatorioSEIDecidirVO.getDataInicio()) + " à " + Uteis.getData(relatorioSEIDecidirVO.getDataFim()));
			break;
		case DATA_VENCIMENTO_E_DATA_PAGAMENTO:
			adicionarFiltroRelatorioExcel(worksheet, TipoFiltroPeriodoSeiDecidirEnum.DATA_VENCIMENTO.getValorApresentar() + ":", Uteis.getData(relatorioSEIDecidirVO.getDataInicio()) + " à " + Uteis.getData(relatorioSEIDecidirVO.getDataFim()));
			adicionarFiltroRelatorioExcel(worksheet, TipoFiltroPeriodoSeiDecidirEnum.DATA_PAGAMENTO.getValorApresentar() + ":", Uteis.getData(relatorioSEIDecidirVO.getDataInicio2()) + " à " + Uteis.getData(relatorioSEIDecidirVO.getDataFim2()));			
			break;
		case PERIODO_FOLLOW_UP:
			adicionarFiltroRelatorioExcel(worksheet, TipoFiltroPeriodoSeiDecidirEnum.PERIODO_FOLLOW_UP.getValorApresentar() + ":", Uteis.getData(relatorioSEIDecidirVO.getDataInicio()) + " à " + Uteis.getData(relatorioSEIDecidirVO.getDataFim()));
			break;
		case PERIODO_COMPROMISSO:
			adicionarFiltroRelatorioExcel(worksheet, TipoFiltroPeriodoSeiDecidirEnum.PERIODO_COMPROMISSO.getValorApresentar() + ":", Uteis.getData(relatorioSEIDecidirVO.getDataInicio()) + " à " + Uteis.getData(relatorioSEIDecidirVO.getDataFim()));
			break;
		case PERIODO_INTERACAO_WORKFLOW:
			adicionarFiltroRelatorioExcel(worksheet, TipoFiltroPeriodoSeiDecidirEnum.PERIODO_INTERACAO_WORKFLOW.getValorApresentar() + ":", Uteis.getData(relatorioSEIDecidirVO.getDataInicio()) + " à " + Uteis.getData(relatorioSEIDecidirVO.getDataFim()));
			break;
		case PERIODO_PRE_INSCRICAO:
			adicionarFiltroRelatorioExcel(worksheet, TipoFiltroPeriodoSeiDecidirEnum.PERIODO_PRE_INSCRICAO.getValorApresentar() + ":", Uteis.getData(relatorioSEIDecidirVO.getDataInicio()) + " à " + Uteis.getData(relatorioSEIDecidirVO.getDataFim()));
			break;
		case PERIODO_DATA_MOVIMENTACAO_CENTRO_ORIGEM:
			adicionarFiltroRelatorioExcel(worksheet, TipoFiltroPeriodoSeiDecidirEnum.PERIODO_DATA_MOVIMENTACAO_CENTRO_ORIGEM.getValorApresentar() + ":", Uteis.getData(relatorioSEIDecidirVO.getDataInicio()) + " à " + Uteis.getData(relatorioSEIDecidirVO.getDataFim()));
			break;
		default:
			break;
		}

		if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltrosFinanceirosReceita() && !relatorioSEIDecidirVO.getRelatorioFinanceiroVO().getItensFiltroSituacao().isEmpty()) {
			adicionarFiltroRelatorioExcel(worksheet, UteisJSF.internacionalizar("prt_RelatorioSeiDecidir_situacaoContaReceber"), relatorioSEIDecidirVO.getRelatorioFinanceiroVO().getItensFiltroSituacao());
		}
		if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltrosFinanceirosDespesa()) {
			StringBuilder situacoes = new StringBuilder("");
			if(!relatorioSEIDecidirVO.getRelatorioFinanceiroVO().getSituacaoPagar()){
				situacoes.append("A Pagar");
			}
			if(!relatorioSEIDecidirVO.getRelatorioFinanceiroVO().getSituacaoPago()){
				situacoes.append(situacoes.length()>0?"; ":"").append("Pago");
			}
			if(!relatorioSEIDecidirVO.getRelatorioFinanceiroVO().getSituacaoRenegociado()){
				situacoes.append(situacoes.length()>0?"; ":"").append("Negociado");
			}
			if(!relatorioSEIDecidirVO.getRelatorioFinanceiroVO().getSituacaoCancelado()){
				situacoes.append(situacoes.length()>0?"; ":"").append("Cancelado");
			}
			situacoes.append(situacoes.length()>0? "" : "A Pagar;Pago;Negociado;Cancelado");
			adicionarFiltroRelatorioExcel(worksheet, UteisJSF.internacionalizar("prt_RelatorioSeiDecidir_situacaoContaPagar"), situacoes.toString());
		}
		if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltrosFinanceirosReceita() && !relatorioSEIDecidirVO.getRelatorioFinanceiroVO().getItensFiltroTipoOrigem().isEmpty()) {
			adicionarFiltroRelatorioExcel(worksheet, UteisJSF.internacionalizar("prt_DeclaracaoImpostoRendaRel_tipoOrigemContaReceber"), relatorioSEIDecidirVO.getRelatorioFinanceiroVO().getItensFiltroTipoOrigem());
		}
		if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltrosSituacaoAcademica() && !relatorioSEIDecidirVO.getRelatorioAcademicoVO().getFiltroAcademicoUtilizado().isEmpty()) {
			adicionarFiltroRelatorioExcel(worksheet, UteisJSF.internacionalizar("prt_FiltroRelatorioAcademico_titulo"), relatorioSEIDecidirVO.getRelatorioAcademicoVO().getFiltroAcademicoUtilizado());

		}
		if (!relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltrosFinanceirosDespesa() && !relatorioSEIDecidirVO.getRelatorioAcademicoVO().getFiltroSituacaoMatriculaUtilizado().isEmpty()) {
			adicionarFiltroRelatorioExcel(worksheet, UteisJSF.internacionalizar("prt_MatriculaPeriodo_situacao"), relatorioSEIDecidirVO.getRelatorioAcademicoVO().getFiltroSituacaoMatriculaUtilizado());

		}
		if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.PROCESSO_SELETIVO)){
			if (!relatorioSEIDecidirVO.getProcSeletivoVO().getCodigo().equals(0)) {
				adicionarFiltroRelatorioExcel(worksheet, "Processo Seletivo", relatorioSEIDecidirVO.getProcSeletivoVO().getDescricao());				
			}
			if (!relatorioSEIDecidirVO.getUnidadeEnsinoCursoVO().getUnidadeEnsino().equals(0)) {
				UnidadeEnsinoVO unidadeEnsinoVO = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(relatorioSEIDecidirVO.getUnidadeEnsinoCursoVO().getUnidadeEnsino(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, null);
				adicionarFiltroRelatorioExcel(worksheet, "Unidade Ensino", unidadeEnsinoVO.getNome());				
			}
			if (!relatorioSEIDecidirVO.getUnidadeEnsinoCursoVO().getCurso().getCodigo().equals(0)) {
				adicionarFiltroRelatorioExcel(worksheet, "Curso", relatorioSEIDecidirVO.getUnidadeEnsinoCursoVO().getCurso().getNome());				
			}
			if (!relatorioSEIDecidirVO.getItemProcSeletivoDataProvaVO().getCodigo().equals(0)) {
				relatorioSEIDecidirVO.setItemProcSeletivoDataProvaVO(getFacadeFactory().getItemProcSeletivoDataProvaFacade().consultarPorChavePrimaria(relatorioSEIDecidirVO.getItemProcSeletivoDataProvaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
				adicionarFiltroRelatorioExcel(worksheet, "Data Prova", relatorioSEIDecidirVO.getItemProcSeletivoDataProvaVO().getDataProva_Apresentar());				
			}
			if (relatorioSEIDecidirVO.getFiltroRelatorioProcessoSeletivoVO() != null) {
				if (relatorioSEIDecidirVO.getFiltroRelatorioProcessoSeletivoVO().getAtivo() || relatorioSEIDecidirVO.getFiltroRelatorioProcessoSeletivoVO().getCancelado() || relatorioSEIDecidirVO.getFiltroRelatorioProcessoSeletivoVO().getCanceladoOutraInscricao() || relatorioSEIDecidirVO.getFiltroRelatorioProcessoSeletivoVO().getNaoCompareceu()) {
					StringBuilder sqlStr = new StringBuilder();
					boolean virgula = false;
					if (relatorioSEIDecidirVO.getFiltroRelatorioProcessoSeletivoVO().getAtivo()) {
						sqlStr.append(virgula ? "," : "").append(SituacaoInscricaoEnum.ATIVO.getValorApresentar());
						virgula = true;
					}
					if (relatorioSEIDecidirVO.getFiltroRelatorioProcessoSeletivoVO().getCancelado()) {
						sqlStr.append(virgula ? "," : "").append(SituacaoInscricaoEnum.CANCELADO.getValorApresentar());
						virgula = true;
					}
					if (relatorioSEIDecidirVO.getFiltroRelatorioProcessoSeletivoVO().getCanceladoOutraInscricao()) {
						sqlStr.append(virgula ? "," : "").append(SituacaoInscricaoEnum.CANCELADO_OUTRA_INSCRICAO.getValorApresentar());
						virgula = true;
					}
					if (relatorioSEIDecidirVO.getFiltroRelatorioProcessoSeletivoVO().getNaoCompareceu()) {
						sqlStr.append(virgula ? "," : "").append(SituacaoInscricaoEnum.NAO_COMPARECEU.getValorApresentar());
						virgula = true;
					}					
					adicionarFiltroRelatorioExcel(worksheet, "Situação Inscrição", sqlStr.toString());
				}
			}
			if (!relatorioSEIDecidirVO.getInscricaoVO().getSituacao().equals("AM")) {
				if (relatorioSEIDecidirVO.getInscricaoVO().getSituacao().equals("CO")) {
					adicionarFiltroRelatorioExcel(worksheet, "Situação Financeira", "Confirmado");					
				} else {
					adicionarFiltroRelatorioExcel(worksheet, "Situação Financeira", "Pendente Financeiramente");					
				}
			}
			if (Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getInscricaoVO().getChamada())) {
				adicionarFiltroRelatorioExcel(worksheet, "Chamada", relatorioSEIDecidirVO.getInscricaoVO().getChamada().toString());				
			}			
			adicionarFiltroRelatorioExcel(worksheet, "Situação Resultado Proc. Seletivo", relatorioSEIDecidirVO.getSituacaoResultadoProcessoSeletivo().getValorApresentar());							
		}
	}

	private void adicionarFiltroRelatorioExcel(XSSFSheet worksheet, String tituloFiltro, String valorFiltro) {
		XSSFCellStyle styleOfString = getCss(worksheet.getWorkbook(), HSSFColor.WHITE.index, XSSFCellStyle.ALIGN_LEFT, "", true, true, true, false, HSSFColor.BLACK.index, false);
		XSSFCellStyle styleOfString2 = getCss(worksheet.getWorkbook(), HSSFColor.WHITE.index, XSSFCellStyle.ALIGN_LEFT, "", true, true, false, true, HSSFColor.BLACK.index, false);
		XSSFRow cabecalho = null;
		XSSFCell cellCabecalho = null;
		cabecalho = worksheet.createRow(worksheet.getLastRowNum() + 1);
		
		cellCabecalho = cabecalho.createCell(0);
		cellCabecalho.setCellValue(tituloFiltro.trim());
		cellCabecalho.setCellStyle(styleOfString);
		
		cellCabecalho = cabecalho.createCell(1);
		cellCabecalho.setCellValue(valorFiltro.replaceAll(";", "\n").trim());
		cellCabecalho.setCellStyle(styleOfString2);
	}

	/*public void realizarGeracaoTopoPadraoRelatorio(RelatorioSEIDecidirVO relatorioSEIDecidirVO, XSSFWorkbook workbook, XSSFSheet worksheet, String caminhoLogo) throws Exception {

		int qtdeColuna = 1;
		worksheet.createRow(worksheet.getLastRowNum() + 1);
		worksheet.createRow(worksheet.getLastRowNum() + 1);
		worksheet.createRow(worksheet.getLastRowNum() + 1);
		worksheet.createRow(worksheet.getLastRowNum() + 1);
		worksheet.createRow(worksheet.getLastRowNum() + 1);

		if (!worksheet.getSheetName().contains("Filtros")) {
			int coluna = 0;
			qtdeColuna = relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getLayoutRelatorioSeiDecidirCampoVOs().size() - 1;
			for (LayoutRelatorioSeiDecidirCampoVO campo : relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getLayoutRelatorioSeiDecidirCampoVOs()) {
				worksheet.setColumnWidth(coluna, campo.getTamanhoColuna() * RelatorioSeiDecidir.WIDTH_COLUNA);
				coluna++;
			}
		} else {
			worksheet.setColumnWidth(0, 7000);
			worksheet.setColumnWidth(1, 24000);
		}

		worksheet.addMergedRegion(new CellRangeAddress(0, 4, 0, qtdeColuna));
		int pictureIndex = realizarImportacaoImagemExcel(workbook, caminhoLogo);

		HSSFPatriarch patriarch = worksheet.createDrawingPatriarch();
		// HSSFShapeGroup group = patriarch.createGroup(new HSSFClientAnchor(0,
		// 0, tamanhoExcel, 146, (short) 0, 0, colunas, 5));
		// Criar caixa de texto
		HSSFTextbox textbox1 = patriarch.createTextbox(new HSSFClientAnchor(0, 0, 1023, 0, (short) 0, 0, (short) qtdeColuna, 5));
		HSSFRichTextString texto = new HSSFRichTextString(relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getDescricao());
		textbox1.setString(texto);
		textbox1.setVerticalAlignment(XSSFCellStyle.ALIGN_CENTER);
		textbox1.setHorizontalAlignment(XSSFCellStyle.ALIGN_CENTER);

		int coluna = -1;
		double widthColumn = 0;
		while (widthColumn < 220) {
			coluna++;
			widthColumn += (worksheet.getColumnWidth(coluna) / 256) * 7;
		}

		HSSFClientAnchor anchor = null;
		anchor = new HSSFClientAnchor(0, 0, (short) 1023, 180, (short) 0, 0, (short) coluna, 5);
//		anchor.setAnchorType(HSSFSimpleShape.OBJECT_TYPE_PICTURE);
		HSSFPicture imagem = patriarch.createPicture(anchor, pictureIndex);
//		imagem.setShapeType(HSSFSimpleShape.OBJECT_TYPE_PICTURE);
		if (imagem.getImageDimension().getWidth() > widthColumn) {
			imagem.resize(220 / imagem.getImageDimension().getWidth());
		} else if (imagem.getImageDimension().getHeight() > (5 * 17)) {
			imagem.resize((5 * 17) / imagem.getImageDimension().getHeight());
		}
	}*/

	private void realizarGeracaoCabecalhoRelatorio(XSSFSheet worksheet, RelatorioSEIDecidirVO relatorioSEIDecidirVO) throws Exception {

		XSSFCellStyle styleOfHeader = getCss(worksheet.getWorkbook(), HSSFColor.GREY_25_PERCENT.index, XSSFCellStyle.ALIGN_CENTER, "", true, true, true, true, HSSFColor.BLACK.index, false);
		XSSFRow cabecalho = null;
		XSSFCell cellCabecalho = null;
		cabecalho = worksheet.createRow(worksheet.getLastRowNum() + 1);
		Integer coluna = 0;
		for (LayoutRelatorioSeiDecidirCampoVO layoutTag : relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getLayoutRelatorioSeiDecidirCampoVOs()) {
			cellCabecalho = cabecalho.createCell(coluna);
			cellCabecalho.setCellValue(layoutTag.getTitulo());
			cellCabecalho.setCellStyle(styleOfHeader);
			cellCabecalho.setCellType(XSSFCell.CELL_TYPE_STRING);

			coluna++;
		}
		worksheet.setAutobreaks(true);
	}

	private String getLetraCorrespondente(Integer coluna) {
		return CellReference.convertNumToColString(coluna - 1);
	}

	private void realizarGeracaoCorpoRelatorio(FileOutputStream fileOut, XSSFWorkbook workbook, String file,  XSSFSheet worksheet, SqlRowSet sqlRowSet, boolean gerarFormatoExportacaoDados,  RelatorioSEIDecidirVO relatorioSEIDecidirVO, ProgressBarVO progressBarVO) throws Exception {

		Integer coluna = 0;
		Integer linhaInicial = gerarFormatoExportacaoDados ? 0 : worksheet.getLastRowNum() + 1;

		XSSFRow cellLinha = null;
		XSSFCell cellColuna = null;

		boolean criarSubTotalAgrupamento = false;
		Map<String, Object> agrupamentoAtual = new HashMap<String, Object>(0);
		List<Integer> linhasSomatorio = new ArrayList<Integer>(0);
		List<Integer> linhasSomatorioGeral = new ArrayList<Integer>(0);

		boolean criarTopoAgrupamento = false;
		String textoSubstituir = "";
		String textoAgrupamentoBase = "";
		TipoCampoEnum tipoCampo = null;
		
		while (sqlRowSet.next()) {

//			if(progressBarVO.getProgresso() > 0 && progressBarVO.getProgresso() % 50000 == 0) {
//				progressBarVO.setStatus("Gravando arquivo parcial.");
//				XSSFFormulaEvaluator.evaluateAllFormulaCells(workbook);
//				workbook.write(fileOut);
//				workbook.close();
//				fileOut.close();
//				workbook =  null;
//				fileOut =  null;
//				worksheet =  null;
//				workbook = new XSSFWorkbook(new FileInputStream(new File(file)));				
//				if(gerarFormatoExportacaoDados) {
//					worksheet = workbook.getSheetAt(0);
//				}else {
//					worksheet = workbook.getSheetAt(1);
//				}
//				fileOut =  new FileOutputStream(file);
//				progressBarVO.setStatus("Arquivo parcial gravado.");
//				
//			}
			progressBarVO.setStatus("Gerando registro "+progressBarVO.getProgresso()+" de "+(progressBarVO.getMaxValue() - 3)+".");
//			System.out.println("Gerando Linha "+progressBarVO.getProgresso());
			coluna = 0;
			if(!gerarFormatoExportacaoDados){
			criarTopoAgrupamento = false;
			criarSubTotalAgrupamento = false;
			textoAgrupamentoBase = relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getAgruparRelatorioPor();
			for (String agrupamento : relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getTagAgrupamentoUtilizada().keySet()) {
				try{
				textoSubstituir = "";
				tipoCampo = relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getTagAgrupamentoUtilizada().get(agrupamento).getTipoCampo();
				switch (tipoCampo) {
				case DATA:
					textoSubstituir = Uteis.getData(sqlRowSet.getDate(agrupamento));
					break;
				case MES_ANO:
					if(sqlRowSet.getObject(agrupamento) instanceof java.sql.Date){
						textoSubstituir = Uteis.getData(sqlRowSet.getDate(agrupamento), "MM/yyyy");
					}else{
						textoSubstituir = sqlRowSet.getString(agrupamento);
					}
					break;
				case DOUBLE:
					textoSubstituir = Uteis.getDoubleFormatado(sqlRowSet.getDouble(agrupamento));
					break;
				case MOEDA:
					textoSubstituir = Uteis.getDoubleFormatado(sqlRowSet.getDouble(agrupamento));
					break;
				case INTEIRO:
					textoSubstituir = ((Integer) sqlRowSet.getInt(agrupamento)).toString();
					break;
				case TEXTO:
					textoSubstituir = sqlRowSet.getString(agrupamento);
					break;
				default:
					textoSubstituir = sqlRowSet.getObject(agrupamento).toString();
					break;
				}
				if (!agrupamentoAtual.containsKey(agrupamento)) {
					if (agrupamentoAtual.isEmpty()) {
						criarTopoAgrupamento = true;
					}

					agrupamentoAtual.put(agrupamento, sqlRowSet.getObject(agrupamento));
				} else if (!agrupamentoAtual.get(agrupamento).equals(sqlRowSet.getObject(agrupamento))) {
					agrupamentoAtual.put(agrupamento, sqlRowSet.getObject(agrupamento) == null ? "" : sqlRowSet.getObject(agrupamento));
					criarSubTotalAgrupamento = true;
					criarTopoAgrupamento = true;
				}
				if(textoSubstituir == null){
					textoSubstituir = "";
//					throw new Exception("Falha ao gerar o agrupador do relatório.");
				}
				textoAgrupamentoBase = textoAgrupamentoBase.replaceAll(relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getTagAgrupamentoUtilizada().get(agrupamento).getTag(), textoSubstituir);
				}catch(Exception e){
					throw e;
				}

			}
			if(relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getAgruparRelatorioPor().contains("@{")) {
				String[] tags = relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getAgruparRelatorioPor().split("@");
				Integer nrTag = relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getTagAgrupamentoUtilizada().size()+1;
				for(String tag: tags) {
					if(tag.contains("{")) { 
					Object valorTag = sqlRowSet.getObject("agrupamento"+nrTag);
					if(valorTag instanceof Date) {
						textoSubstituir = Uteis.getData((Date)valorTag, "MM/yyyy");					
					}else if(valorTag instanceof Double) {
						textoSubstituir = Uteis.getDoubleFormatado((Double)valorTag);
					}else {
						textoSubstituir = (String) Uteis.coalesce(valorTag, "");
					}
					tag = "@"+tag.substring(tag.indexOf("{"), tag.indexOf("}")+1);
					if (!agrupamentoAtual.containsKey(tag)) {
						if (agrupamentoAtual.isEmpty()) {
							criarTopoAgrupamento = true;
						}
						agrupamentoAtual.put(tag, Uteis.coalesce(valorTag, ""));
					} else if (!agrupamentoAtual.get(tag).equals(Uteis.coalesce(valorTag, ""))) {
						agrupamentoAtual.put(tag, Uteis.coalesce(valorTag, ""));
						criarSubTotalAgrupamento = true;
						criarTopoAgrupamento = true;
					}
					if(textoSubstituir == null){
						textoSubstituir = "";
					}
					textoAgrupamentoBase = textoAgrupamentoBase.replace(tag, textoSubstituir);
					nrTag++;
					}
				}
			}
			if (criarSubTotalAgrupamento) {
				linhasSomatorio.clear();
				linhasSomatorio.add(linhaInicial + 1);
				linhasSomatorio.add(worksheet.getLastRowNum() + 1);
				realizarGeracaoRodapeRelatorio(worksheet, relatorioSEIDecidirVO, linhasSomatorio, true);
				criarSubTotalAgrupamento = false;
				linhasSomatorioGeral.add(worksheet.getLastRowNum() + 1);
				linhaInicial = worksheet.getLastRowNum();
			}
			if (criarTopoAgrupamento) {
				cellLinha = worksheet.createRow(worksheet.getLastRowNum() + 1);
				worksheet.addMergedRegion(new CellRangeAddress(worksheet.getLastRowNum(), worksheet.getLastRowNum(), 0, relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getLayoutRelatorioSeiDecidirCampoVOs().size() - 1));
				cellColuna = cellLinha.createCell(0);
				cellColuna.setCellType(XSSFCell.CELL_TYPE_STRING);
				cellColuna.setCellValue(textoAgrupamentoBase);
				HSSFPalette palette = new HSSFWorkbook().getCustomPalette();
				byte rgb = (byte) 220;
				HSSFColor color = palette.findColor(rgb, rgb, rgb);
				if (color == null) {
					palette.setColorAtIndex(HSSFColor.GREY_50_PERCENT.index, rgb, rgb, rgb);
					color = palette.findColor(rgb, rgb, rgb);
				}
				cellColuna.setCellStyle(getCss(worksheet.getWorkbook(), color.getIndex(), XSSFCellStyle.ALIGN_CENTER, "", true, true, true, true, HSSFColor.BLACK.index, gerarFormatoExportacaoDados));

				linhaInicial = worksheet.getLastRowNum() + 1;
			}
			}
			if(!gerarFormatoExportacaoDados){
				cellLinha = worksheet.createRow(worksheet.getLastRowNum() + 1);
			}else{
				cellLinha = worksheet.createRow(linhaInicial);
				linhaInicial++;
			 if (linhaInicial == 1 && Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getTextoCabecalho())) {
				 cellColuna = cellLinha.createCell(coluna++);
				 cellColuna.setCellValue(relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getTextoCabecalho());
				 cellLinha = worksheet.createRow(linhaInicial);
			}	
			}
			for (LayoutRelatorioSeiDecidirCampoVO layoutTag : relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getLayoutRelatorioSeiDecidirCampoVOs()) {
				switch (layoutTag.getTipoCampo()) {
				case MES_ANO:
					cellColuna = cellLinha.createCell(coluna++);
					cellColuna.setCellType(XSSFCell.CELL_TYPE_BLANK);
					if(sqlRowSet.getObject(layoutTag.getAlias()) instanceof java.sql.Date && sqlRowSet.getDate(layoutTag.getAlias()) != null){						
						cellColuna.setCellValue(sqlRowSet.getDate(layoutTag.getAlias()));
					}else if(sqlRowSet.getObject(layoutTag.getAlias()) instanceof String){
						cellColuna.setCellValue(sqlRowSet.getString(layoutTag.getAlias()));
					}else{
						cellColuna.setCellValue("");
					}					
					cellColuna.setCellStyle(getCss(worksheet.getWorkbook(), HSSFColor.WHITE.index, XSSFCellStyle.ALIGN_CENTER, "MM/yyyy", true, true, true, true, HSSFColor.BLACK.index, gerarFormatoExportacaoDados));
					break;
				case DATA:
					cellColuna = cellLinha.createCell(coluna++);
					cellColuna.setCellType(XSSFCell.CELL_TYPE_BLANK);
					if(sqlRowSet.getDate(layoutTag.getAlias()) != null){
						cellColuna.setCellValue(sqlRowSet.getDate(layoutTag.getAlias()));
					}else{
						cellColuna.setCellValue("");
					}
					cellColuna.setCellStyle(getCss(worksheet.getWorkbook(), HSSFColor.WHITE.index, XSSFCellStyle.ALIGN_CENTER, "dd/MM/yyyy", true, true, true, true, HSSFColor.BLACK.index, gerarFormatoExportacaoDados));
					break;
				case DOUBLE:
					cellColuna = cellLinha.createCell(coluna++);
					cellColuna.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
					cellColuna.setCellValue(sqlRowSet.getDouble(layoutTag.getAlias()));
					String mascara = "#,##0";
					if (layoutTag.getQtdCasasDecimais() > 0) {
						mascara += ".";
						int x = 1;
						while (x <= layoutTag.getQtdCasasDecimais()) {
							mascara += "0";
							x++;
						}
					}
					cellColuna.setCellStyle(getCss(worksheet.getWorkbook(), HSSFColor.WHITE.index, XSSFCellStyle.ALIGN_RIGHT, mascara, true, true, true, true, HSSFColor.BLACK.index, gerarFormatoExportacaoDados));
					break;
				case MOEDA:
					cellColuna = cellLinha.createCell(coluna++);
					cellColuna.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
					cellColuna.setCellValue(sqlRowSet.getDouble(layoutTag.getAlias()));
					mascara = "R$ #,##0";
					if (layoutTag.getQtdCasasDecimais() > 0) {
						mascara += ".";
						int x = 1;
						while (x <= layoutTag.getQtdCasasDecimais()) {
							mascara += "0";
							x++;
						}
					}
					cellColuna.setCellStyle(getCss(worksheet.getWorkbook(), HSSFColor.WHITE.index, XSSFCellStyle.ALIGN_RIGHT, mascara, true, true, true, true, HSSFColor.BLACK.index, gerarFormatoExportacaoDados));
					break;
				case INTEIRO:
					cellColuna = cellLinha.createCell(coluna++);
					cellColuna.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
					cellColuna.setCellValue(sqlRowSet.getInt(layoutTag.getAlias()));
					cellColuna.setCellStyle(getCss(worksheet.getWorkbook(), HSSFColor.WHITE.index, XSSFCellStyle.ALIGN_CENTER, "", true, true, true, true, HSSFColor.BLACK.index, gerarFormatoExportacaoDados));
					break;
				default:
					cellColuna = cellLinha.createCell(coluna++);
					cellColuna.setCellType(XSSFCell.CELL_TYPE_STRING);
					if(sqlRowSet.getString(layoutTag.getAlias()) == null){
						cellColuna.setCellValue("");
					}else{
						if (layoutTag.getCampo().equals("SITUACAO_HISTORICO")) {
							cellColuna.setCellValue(SituacaoHistorico.getDescricao(sqlRowSet.getString(layoutTag.getAlias())));
						} else if (layoutTag.getCampo().equals("TIPO_HISTORICO")) {
							cellColuna.setCellValue(TipoHistorico.getDescricao(sqlRowSet.getString(layoutTag.getAlias())));
						} else if (layoutTag.getCampo().equals("SITUACAO_REQUERIMENTO")) {
							cellColuna.setCellValue(SituacaoRequerimento.getDescricao(sqlRowSet.getString(layoutTag.getAlias())));
						} else if (layoutTag.getCampo().equals("SITUACAO_FINANCEIRA_REQUERIMENTO")) {
							RequerimentoVO req = new RequerimentoVO();
							req.setSituacaoFinanceira(sqlRowSet.getString(layoutTag.getAlias()));
							cellColuna.setCellValue(req.getSituacaoFinanceira_Apresentar());
						} else {
							cellColuna.setCellValue(sqlRowSet.getString(layoutTag.getAlias()));
						}
						
					}
					cellColuna.setCellStyle(getCss(worksheet.getWorkbook(), HSSFColor.WHITE.index, XSSFCellStyle.ALIGN_LEFT, "", true, true, true, true, HSSFColor.BLACK.index, gerarFormatoExportacaoDados));
					break;
				}

			}
			progressBarVO.incrementar();
		}
		if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getGerarLinhaEmBranco()) {
			cellLinha = worksheet.createRow(linhaInicial);
			linhaInicial++;
		}
		if(!gerarFormatoExportacaoDados){
		linhasSomatorio.clear();
		linhasSomatorio.add(linhaInicial + 1);
		linhasSomatorio.add(worksheet.getLastRowNum() + 1);		
		realizarGeracaoRodapeRelatorio(worksheet, relatorioSEIDecidirVO, linhasSomatorio, true);
		if (!linhasSomatorioGeral.isEmpty()) {
			linhasSomatorioGeral.add(worksheet.getLastRowNum() + 1);
			cellLinha = worksheet.createRow(worksheet.getLastRowNum() + 1);
			cellLinha = worksheet.createRow(worksheet.getLastRowNum() + 1);
			for(LayoutRelatorioSeiDecidirCampoVO tag: relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getLayoutRelatorioSeiDecidirCampoVOs()){
				if(tag.getTipoTotalizador().equals(RelatorioSEIDecidirTipoTotalizadorEnum.CONTAR)){
					tag.setTipoTotalizador(RelatorioSEIDecidirTipoTotalizadorEnum.SOMAR);
					tag.setQtdCasasDecimais(0);
				}
			}
			realizarGeracaoRodapeRelatorio(worksheet, relatorioSEIDecidirVO, linhasSomatorioGeral, false);
		}
		}

	}

	private void realizarGeracaoRodapeRelatorio(XSSFSheet worksheet, RelatorioSEIDecidirVO relatorioSEIDecidirVO, List<Integer> linhasSomantorio, boolean linhaSequencial) throws Exception {
		XSSFRow cellLinha = null;
		XSSFCell cellColuna = null;
		cellLinha = worksheet.createRow(worksheet.getLastRowNum() + 1);
		int coluna = 0;
		String letra = null;
		StringBuilder linhaUtilizar = null;
		short cor = HSSFColor.GREY_25_PERCENT.index;
		if (linhaSequencial) {
			HSSFPalette palette = new HSSFWorkbook().getCustomPalette();
			byte rgb = (byte) 220;
			HSSFColor color = palette.findColor(rgb, rgb, rgb);
			if (color == null) {
				palette.setColorAtIndex(HSSFColor.GREY_50_PERCENT.index, rgb, rgb, rgb);
				color = palette.findColor(rgb, rgb, rgb);
			}
			cor = color.getIndex();
		}
		for (LayoutRelatorioSeiDecidirCampoVO layoutTag : relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getLayoutRelatorioSeiDecidirCampoVOs()) {
			if (!layoutTag.getTipoTotalizador().equals(RelatorioSEIDecidirTipoTotalizadorEnum.TEXTO) && !layoutTag.getTipoTotalizador().equals(RelatorioSEIDecidirTipoTotalizadorEnum.NENHUM)) {
				letra = getLetraCorrespondente(coluna + 1);
				linhaUtilizar = new StringBuilder("");
				if (linhaSequencial) {
					linhaUtilizar.append(letra + linhasSomantorio.get(0) + ":" + letra + linhasSomantorio.get(1));
				} else {
					for (Integer linhaSomar : linhasSomantorio) {
						linhaUtilizar.append((linhaUtilizar.length() == 0 ? "" : "+") + letra + linhaSomar);
					}
				}
			}

			switch (layoutTag.getTipoTotalizador()) {
			case CONTAR:
				cellColuna = cellLinha.createCell(coluna);
				if(layoutTag.getTipoCampo().equals(TipoCampoEnum.TEXTO)){
					cellColuna.setCellType(XSSFCell.CELL_TYPE_FORMULA);
					cellColuna.setCellFormula("COUNTA(" + linhaUtilizar.toString() + ")");
				}else{
					cellColuna.setCellType(XSSFCell.CELL_TYPE_FORMULA);
					cellColuna.setCellFormula("COUNT(" + linhaUtilizar.toString() + ")");
				}				
				cellColuna.setCellStyle(getCss(worksheet.getWorkbook(), cor, XSSFCellStyle.ALIGN_CENTER, "", true, true, true, true, HSSFColor.BLACK.index, false));
				break;
			case MEDIA:
				cellColuna = cellLinha.createCell(coluna);
				cellColuna.setCellType(XSSFCell.CELL_TYPE_FORMULA);
				cellColuna.setCellFormula("MEDIA(" + linhaUtilizar.toString() + ")");
				String mascara = "#,##0";
				if (layoutTag.getQtdCasasDecimais() > 0) {
					mascara += ".";
					int x = 1;
					while (x <= layoutTag.getQtdCasasDecimais()) {
						mascara += "0";
						x++;
					}
				}
				cellColuna.setCellStyle(getCss(worksheet.getWorkbook(), cor, XSSFCellStyle.ALIGN_RIGHT, mascara, true, true, true, true, HSSFColor.BLACK.index, false));
				break;
			case SOMAR:
				cellColuna = cellLinha.createCell(coluna);
				cellColuna.setCellType(XSSFCell.CELL_TYPE_FORMULA);
				cellColuna.setCellFormula("SUM(" + linhaUtilizar.toString() + ")");
				
				if(layoutTag.getTipoCampo().equals(TipoCampoEnum.INTEIRO)){
					cellColuna.setCellStyle(getCss(worksheet.getWorkbook(), cor, XSSFCellStyle.ALIGN_CENTER, "", true, true, true, true, HSSFColor.BLACK.index, false));
				}else{
					mascara = "#,##0";
					if (layoutTag.getQtdCasasDecimais() > 0) {
						mascara += ".";
						int x = 1;
						while (x <= layoutTag.getQtdCasasDecimais()) {
							mascara += "0";
							x++;
						}
					}
					cellColuna.setCellStyle(getCss(worksheet.getWorkbook(), cor, XSSFCellStyle.ALIGN_RIGHT, mascara, true, true, true, true, HSSFColor.BLACK.index, false));
				}
				break;
			case TEXTO:
				cellColuna = cellLinha.createCell(coluna);
				cellColuna.setCellType(XSSFCell.CELL_TYPE_STRING);
				cellColuna.setCellValue(layoutTag.getTextoTotalizador());
				cellColuna.setCellStyle(getCss(worksheet.getWorkbook(), cor, XSSFCellStyle.ALIGN_LEFT, "", true, true, true, true, HSSFColor.BLACK.index, false));
				break;

			default:
				cellColuna = cellLinha.createCell(coluna);
				cellColuna.setCellType(XSSFCell.CELL_TYPE_BLANK);
				cellColuna.setCellValue("");
				cellColuna.setCellStyle(getCss(worksheet.getWorkbook(), cor, XSSFCellStyle.ALIGN_LEFT, "", true, true, true, true, HSSFColor.BLACK.index, false));
				break;
			}
			coluna++;

		}
	}

	/**
	 * Este método é responsável em buscar os Enum das Tags utilizadas no
	 * LayoutRelatorioSeiDecidirCampoVO e adiciona-las na lista transiente
	 * (TagUtilizadaEnum) de forma que acessando o
	 * LayoutRelatorioSeiDecidirCampoVO tenha em mãos as tags utilizadas
	 * 
	 * @param relatorioSEIDecidirVO
	 */
	@SuppressWarnings("unlikely-arg-type")
	private void realizarObtencaoTagGeracaoRelatorio(RelatorioSEIDecidirVO relatorioSEIDecidirVO) {
		int nrArgupador = 0;
		for (TagSEIDecidirEntidadeEnum tagSEIDecidirEntidadeEnum : TagSEIDecidirEntidadeEnum.values()) {
			
			for (Enum<? extends PerfilTagSEIDecidirEnum> tag : tagSEIDecidirEntidadeEnum.getTagSeiDecidirEnum()) {
				
				PerfilTagSEIDecidirEnum tagUtilizar = ((PerfilTagSEIDecidirEnum) tag);								
				for (LayoutRelatorioSeiDecidirCampoVO layoutTag : relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getLayoutRelatorioSeiDecidirCampoVOs()) {
					if(relatorioSEIDecidirVO.getDataBase() == null){
						layoutTag.setCampoTagSubstituida(layoutTag.getCampoTagSubstituida().replaceAll("CONTA_RECEBER_DATA_BASE", "null"));
					}else{
						layoutTag.setCampoTagSubstituida(layoutTag.getCampoTagSubstituida().replaceAll("CONTA_RECEBER_DATA_BASE", "'"+Uteis.getDataJDBC(relatorioSEIDecidirVO.getDataBase()).toString()+"'"));
					}
					if (layoutTag.getCampo().contains(tag.name()) && !layoutTag.getTagUtilizadaEnum().contains(layoutTag)) {

						layoutTag.setCampoTagSubstituida(layoutTag.getCampoTagSubstituida().replaceAll(tag.name(), tagUtilizar.getCampo()));
						layoutTag.getTagUtilizadaEnum().add(tagUtilizar);
						relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getTagUtilizadaSelectEnums().add(tagUtilizar);
						if(!relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getTagUtilizadas().contains(tagUtilizar)){
							relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getTagUtilizadas().add(tagUtilizar);
							}
					}
				}
				if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getAgruparRelatorioPor().contains(tag.name())) {
					relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getTagAgrupamentoUtilizada().put("agrupador" + nrArgupador, tagUtilizar);
					if(!relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getTagUtilizadas().contains(tagUtilizar)){
						relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getTagUtilizadas().add(tagUtilizar);
						}
					nrArgupador++;
				}
				if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getCondicaoWhereAdicional().contains(tag.name())) {
					relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getTagCondicaoWhereUtilizada().put(tag.name(), tagUtilizar);
					if(!relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getTagUtilizadas().contains(tagUtilizar)){
					relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getTagUtilizadas().add(tagUtilizar);
					}
				}
				if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getGroupByAdicional().contains(tag.name())) {
					relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getTagCondicaoWhereUtilizada().put(tag.name(), tagUtilizar);
					if(!relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getTagUtilizadas().contains(tagUtilizar)){
						relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getTagUtilizadas().add(tagUtilizar);
					}
				}
				if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getOrderByAdicional().contains(tag.name())) {
					relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getTagCondicaoWhereUtilizada().put(tag.name(), tagUtilizar);
					if(!relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getTagUtilizadas().contains(tagUtilizar)){
						relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getTagUtilizadas().add(tagUtilizar);
			}
				}
			}

		}
	}

	/**
	 * Este método gerar o sql de consulta para a geração do relatório
	 * 
	 * @param relatorioSEIDecidirVO
	 * @return
	 * @throws Exception
	 */
	public SqlRowSet realizarConsultarGeracaoRelatorio(RelatorioSEIDecidirVO relatorioSEIDecidirVO, Map<String, Object> mapaParametrosRelatorio,
			UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(realizarGeracaoSelect(relatorioSEIDecidirVO));
		sql.append(realizarGeracaoFrom(relatorioSEIDecidirVO));
		sql.append(realizarGeracaoWhere(relatorioSEIDecidirVO, mapaParametrosRelatorio, usuarioVO));
		sql.append(realizarGeracaoGroupBy(relatorioSEIDecidirVO));
		sql.append(realizarGeracaoOrderBy(relatorioSEIDecidirVO));
		System.out.println(sql.toString());
		return getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
	}
	

	/**
	 * Este método gerar o select do sql de consulta para a geração do relatório
	 * de acordo com o layout selecionado, trazendo apenas os campos relacionado
	 * no mesmo com base nas tags informadas no layout
	 * 
	 * @param relatorioSEIDecidirVO
	 * @return
	 * @throws Exception
	 */
	private String realizarGeracaoSelect(RelatorioSEIDecidirVO relatorioSEIDecidirVO) throws Exception {
		StringBuilder sql = new StringBuilder(" ");
		if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.FINANCEIRO_CENTRO_RESULTADO)) {
			sql.append(getFacadeFactory().getCentroResultadoFacade().getSQLPadraoConsultaArvoreCentroResultadoPorNivel());
			sql.append(" SELECT centroresultadosuperior2.nivel, centroresultadosuperior2.nivelsuperior, "); 
		}else {
			sql.append(" SELECT ");
		}
		for (LayoutRelatorioSeiDecidirCampoVO layoutTag : relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getLayoutRelatorioSeiDecidirCampoVOs()) {
			sql.append(layoutTag.getOrdemApresentacao() > 1 ? "," : "").append(layoutTag.getCampoTagSubstituida()).append(" as ").append(layoutTag.getAlias());
		}
		for (String agrupador : relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getTagAgrupamentoUtilizada().keySet()) {
			if(relatorioSEIDecidirVO.getDataBase() == null){
				agrupador = agrupador.replaceAll("CONTA_RECEBER_DATA_BASE", "null");
			}else{
				agrupador = agrupador.replaceAll("CONTA_RECEBER_DATA_BASE", "'"+Uteis.getDataJDBC(relatorioSEIDecidirVO.getDataBase()).toString()+"'");				
			}
			sql.append(", ").append(relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getTagAgrupamentoUtilizada().get(agrupador).getCampo()).append(" as ").append(agrupador);
		}
		if(relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getAgruparRelatorioPor().contains("@{")) {
			String[] tags = relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getAgruparRelatorioPor().split("@");
			Integer nrTag = relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getTagAgrupamentoUtilizada().size()+1;
			for(String tag: tags) {
				if(tag.contains("{")) {
					tag = tag.substring(tag.indexOf("{")+1, tag.indexOf("}"));
					sql.append(", ").append(tag).append(" as agrupamento"+nrTag);
					nrTag++;
				}
			}
		}

		return sql.toString();
	}
	
	

	/**
	 * Este método gerar o from do sql de consulta para a geração do relatório
	 * de acordo com o layout selecionado, trazendo apenas os campos relacionado
	 * no mesmo com base nas tags informadas no layout
	 * 
	 * @param relatorioSEIDecidirVO
	 * @return
	 * @throws Exception
	 */
	private String realizarGeracaoFrom(RelatorioSEIDecidirVO relatorioSEIDecidirVO) throws Exception {
		StringBuilder sql = new StringBuilder("");
		if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.FINANCEIRO_RECEITA)) {
			sql.append(realizarGeracaoFromModuloFinanceiroReceita(relatorioSEIDecidirVO));
		}
		if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.FINANCEIRO_DESPESA)) {
			sql.append(realizarGeracaoFromModuloFinanceiroDespesa(relatorioSEIDecidirVO));
		}
		if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.ACADEMICO)) {
			sql.append(realizarGeracaoFromModuloAcademico(relatorioSEIDecidirVO));
		}
		if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.FINANCEIRO_FECHAMENTO_MES_RECEITA)) {
			sql.append(realizarGeracaoFromModuloFinanceiroFechamentoMes(relatorioSEIDecidirVO));
		}
		if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.RECURSOS_HUMANOS)) {
			sql.append(realizarGeracaoFromModuloRecursosHumanos(relatorioSEIDecidirVO));
		}
		if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.CRM)) {
			sql.append(realizarGeracaoFromModuloCrm(relatorioSEIDecidirVO));
		}
		if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.ESTAGIO)) {
			sql.append(realizarGeracaoFromModuloEstagio(relatorioSEIDecidirVO));
		}
		if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.FINANCEIRO_CENTRO_RESULTADO)) {
			sql.append(realizarGeracaoFromModuloCentroResultado(relatorioSEIDecidirVO));
		}
		if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.PLANO_ORCAMENTARIO)) {
			sql.append(realizarGeracaoFromModuloPlanoOrcamentario(relatorioSEIDecidirVO));
		}
		if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.REQUERIMENTO)) {
			sql.append(realizarGeracaoFromModuloRequerimento(relatorioSEIDecidirVO));
		}
		if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.PROCESSO_SELETIVO)) {
			sql.append(realizarGeracaoFromModuloProcessoSeletivo(relatorioSEIDecidirVO));
		}
		sql.append(relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getCondicaoJoinAdicional());
		return sql.toString();
	}

	public boolean realizaVerificacaoTabelaUtilizada(RelatorioSEIDecidirVO relatorioSEIDecidirVO, String tabela){
		for(PerfilTagSEIDecidirEnum  tag: relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getTagUtilizadas()){
			if(tag.getEntidade().equalsIgnoreCase(tabela) || tag.getCampo().toUpperCase().contains(tabela.toUpperCase())){
				return true;
			}
		}		
		if(relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getLayoutRelatorioSeiDecidirCampoVOs().stream().anyMatch(t -> t.getCampo().toLowerCase().contains(tabela.toLowerCase()))
				|| relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getCondicaoWhereAdicional().toLowerCase().contains(tabela.toLowerCase())
				|| relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getCondicaoJoinAdicional().toLowerCase().contains(tabela.toLowerCase())
				|| relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getGroupByAdicional().toLowerCase().contains(tabela.toLowerCase())
				|| relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getAgruparRelatorioPor().toLowerCase().contains(tabela.toLowerCase())) {
			return true;
		}
		return false;
	} 
	
	private String realizarGeracaoFromModuloFinanceiroReceita(RelatorioSEIDecidirVO relatorioSEIDecidirVO) throws Exception {
		StringBuilder sql = new StringBuilder(" ");
		sql.append(" FROM contareceber ");
		if(relatorioSEIDecidirVO.isConsiderarUnidadeEnsinoFinanceira()){
			sql.append(" inner join unidadeensino on unidadeensino.codigo = contareceber.unidadeensinofinanceira ");
		}else{
			sql.append(" inner join unidadeensino on unidadeensino.codigo = contareceber.unidadeensino ");
		}
		sql.append(" LEFT JOIN contarecebernegociacaorecebimento 				ON contarecebernegociacaorecebimento.contareceber 			   = contareceber.codigo ");
		sql.append(" LEFT JOIN negociacaorecebimento 							ON contarecebernegociacaorecebimento.negociacaorecebimento     = negociacaorecebimento.codigo ");
		if(realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "contareceberrecebimento") || realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "formapagamentonegociacaorecebimento")
				|| realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "formapagamento") || realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "cheque")
				|| realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "formapagamentonegociacaorecebimentocartaocredito") 
				|| Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getFormaPagamentoVO())
				|| relatorioSEIDecidirVO.getFormaPagamentoVOs().stream().anyMatch(t -> t.getFiltrarFormaPagamento())
				|| relatorioSEIDecidirVO.getTipoFiltroPeriodo().equals(TipoFiltroPeriodoSeiDecidirEnum.DATA_COMPENSACAO)
				|| relatorioSEIDecidirVO.getTipoFiltroPeriodo().equals(TipoFiltroPeriodoSeiDecidirEnum.DATA_RECEBIMENTO_E_DATA_COMPENSACAO)
				|| relatorioSEIDecidirVO.getTipoFiltroPeriodo().equals(TipoFiltroPeriodoSeiDecidirEnum.DATA_COMPETENCIA_DATA_COMPENSACAO)
				|| relatorioSEIDecidirVO.getTipoFiltroPeriodo().equals(TipoFiltroPeriodoSeiDecidirEnum.DATA_VENCIMENTO_E_DATA_COMPENSACAO)				
				) {
		sql.append(" LEFT JOIN contareceberrecebimento 							ON contareceberrecebimento.contareceber 					   = contareceber.codigo AND contareceberrecebimento.negociacaorecebimento = negociacaorecebimento.codigo ");
		sql.append(" LEFT JOIN formapagamentonegociacaorecebimento 				ON contareceberrecebimento.formapagamentonegociacaorecebimento = formapagamentonegociacaorecebimento.codigo ");		
		sql.append(" LEFT JOIN formapagamento 									ON formapagamentonegociacaorecebimento.formapagamento 		   = formapagamento.codigo ");
		sql.append(" LEFT JOIN cheque 											ON formapagamentonegociacaorecebimento.cheque 				   = cheque.codigo AND formapagamento.tipo = 'CH' ");
		sql.append(" LEFT JOIN formapagamentonegociacaorecebimentocartaocredito ON formapagamento.tipo = 'CA' AND ((formapagamentonegociacaorecebimento.formapagamentonegociacaorecebimentocartaocredito is not null and formapagamentonegociacaorecebimentocartaocredito.codigo  = formapagamentonegociacaorecebimento.formapagamentonegociacaorecebimentocartaocredito) or (formapagamentonegociacaorecebimento.formapagamentonegociacaorecebimentocartaocredito is null and formapagamentonegociacaorecebimento.codigo = formapagamentonegociacaorecebimentocartaocredito.formapagamentonegociacaorecebimento )) ");	
		}
		
		if (realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "unidadeensinocidade") || realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "unidadeensinoestado")) {
			sql.append(" left join cidade as unidadeensinocidade on unidadeensino.cidade = unidadeensinocidade.codigo ");
			if (realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "unidadeensinoestado")) {
				sql.append(" left join estado as unidadeensinoestado on unidadeensinoestado.codigo = unidadeensinocidade.estado ");
			}
		}
		if (realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "chequeDevolvido")) {
			sql.append(" left join devolucaocheque on contareceber.tipoorigem = '").append(TipoOrigemContaReceber.DEVOLUCAO_CHEQUE.getValor()).append("' and devolucaocheque.codigo::varchar = contareceber.codorigem ");
			sql.append(" left join cheque as chequeDevolvido on chequeDevolvido.codigo = devolucaocheque.cheque ");
		}
		sql.append(" left join contacorrente on contacorrente.codigo = contareceber.contacorrente ");
		sql.append(" left join matricula on matricula.matricula = contareceber.matriculaaluno ");
		sql.append(" LEFT JOIN matriculaperiodo ON matriculaperiodo.matricula = matricula.matricula ");	
		sql.append(" AND (CASE WHEN contareceber.matriculaperiodo IS NULL THEN matriculaperiodo.codigo = ( ");
	    sql.append(" SELECT mp.codigo  FROM matriculaperiodo mp   WHERE mp.matricula = matricula.matricula   AND mp.situacaoMatriculaPeriodo != 'PC' "); 
		sql.append(" ORDER BY (mp.ano || '/' || mp.semestre) DESC, CASE WHEN mp.situacaoMatriculaPeriodo in ('AT', 'PR', 'FI', 'FO') THEN 1 ELSE 2 END, mp.codigo DESC LIMIT 1 )");
		sql.append(" ELSE matriculaperiodo.codigo = contareceber.matriculaperiodo END) ");
		sql.append(" left join periodoletivo as periodoletivomatriculaperiodo on matriculaperiodo.periodoletivomatricula = periodoletivomatriculaperiodo.codigo ");
		sql.append(" left join turma on ((contareceber.turma is null and matriculaperiodo.turma = turma.codigo) or (contareceber.turma is not null and contareceber.turma = turma.codigo)) ");
		sql.append(" left join pessoa as aluno on aluno.codigo = matricula.aluno ");
		if (realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "gradecurricularatual")) {
			sql.append(" left join gradecurricular as gradecurricularatual on gradecurricularatual.codigo = matricula.gradecurricularatual ");
		}
		if (realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "alunocidade") || realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "alunoestado")) {
			sql.append(" left join cidade as alunocidade on aluno.cidade = alunocidade.codigo ");
			if (realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "alunoestado")) {
				sql.append(" left join estado as alunoestado on alunocidade.estado = alunoestado.codigo ");
			}
		}
		sql.append(" left join curso on curso.codigo = matricula.curso ");
		sql.append(" left join eixocurso on curso.eixocurso = eixocurso.codigo ");
		sql.append(" left join turno on turno.codigo = matricula.turno ");	
		sql.append(" left join pessoa as contareceberpessoa on contareceberpessoa.codigo = contareceber.pessoa ");
		sql.append(" left join parceiro as contareceberparceiro on contareceberparceiro.codigo = contareceber.parceiro ");
		sql.append(" left join fornecedor as contareceberfornecedor on contareceberfornecedor.codigo = contareceber.fornecedor ");
		sql.append(" left join pessoa as contareceberresponsavelfinanceiro on contareceberresponsavelfinanceiro.codigo = contareceber.responsavelfinanceiro ");

		if (realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "negociacaorecebimentoresponsavel")) {
			sql.append(" left join usuario as negociacaorecebimentoresponsavel on negociacaorecebimentoresponsavel.codigo = negociacaorecebimento.responsavel ");
		}
		if (realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "contarecebercidadepessoa")) {
			sql.append(" left join cidade as contarecebercidadepessoa on contareceberpessoa.cidade = contarecebercidadepessoa.codigo ");
		}
		if (realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "contarecebercidaderesponsavelfinanceiro")) {
			sql.append(" left join cidade as contarecebercidaderesponsavelfinanceiro on contareceberresponsavelfinanceiro.cidade = contarecebercidaderesponsavelfinanceiro.codigo ");
		}
		if (realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "contarecebercidadeparceiro")) {
			sql.append(" left join cidade as contarecebercidadeparceiro on contareceberparceiro.cidade = contarecebercidadeparceiro.codigo ");
		}
		if (realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "contarecebercidadefornecedor")) {
			sql.append(" left join cidade as contarecebercidadefornecedor on contareceberfornecedor.cidade = contarecebercidadefornecedor.codigo ");
		}
		if (realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "contareceberestadopessoa")) {
			sql.append(" left join estado as contareceberestadopessoa on contareceberestadopessoa.codigo = contarecebercidadepessoa.estado ");
		}
		if (realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "contareceberestadoresponsavelfinanceiro")) {
			sql.append(" left join estado as contareceberestadoresponsavelfinanceiro on contareceberestadoresponsavelfinanceiro.codigo = contarecebercidaderesponsavelfinanceiro.estado ");
		}
		if (realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "contareceberestadoparceiro")) {
			sql.append(" left join estado as contareceberestadoparceiro on contareceberestadoparceiro.codigo = contarecebercidadeparceiro.estado ");
		}
		if (realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "contareceberestadofornecedor")) {
			sql.append(" left join estado as contareceberestadofornecedor on contareceberestadofornecedor.codigo = contarecebercidadefornecedor.estado ");
		}
		if (realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "negociacaorecebimentopessoa")) {		
			sql.append(" left join pessoa as negociacaorecebimentopessoa on negociacaorecebimentopessoa.codigo = negociacaorecebimento.pessoa ");
		}
		if (realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "negociacaoRecebimentoParceiro")) {
			sql.append(" left join parceiro as negociacaoRecebimentoParceiro on negociacaoRecebimentoParceiro.codigo = negociacaorecebimento.parceiro ");
		}
		if (realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "negociacaoRecebimentofornecedor")) {
			sql.append(" left join fornecedor as negociacaoRecebimentofornecedor on negociacaoRecebimentofornecedor.codigo = negociacaorecebimento.fornecedor ");
		}		
	  return sql.toString();
	}
	private String realizarGeracaoFromModuloFinanceiroDespesa(RelatorioSEIDecidirVO relatorioSEIDecidirVO) throws Exception {
		StringBuilder sql = new StringBuilder(" ");
		sql.append(" FROM contapagar ");
		sql.append(" inner join unidadeensino on unidadeensino.codigo = contapagar.unidadeensino ");
		if (realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "unidadeensinocidade") || realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "unidadeensinoestado")) {
			sql.append(" left join cidade as unidadeensinocidade on unidadeensino.cidade = unidadeensinocidade.codigo ");
			if (realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "unidadeensinoestado")) {
				sql.append(" left join estado as unidadeensinoestado on unidadeensinoestado.codigo = unidadeensinocidade.estado ");
			}
		}
		sql.append(" left join curso on curso.codigo = contapagar.curso ");
		sql.append(" left join eixocurso on curso.eixocurso = eixocurso.codigo ");
		sql.append(" left join turma on turma.codigo = contapagar.turma ");
		sql.append(" left join turno on turno.codigo = contapagar.turno ");
		sql.append(" left join contacorrente on contacorrente.codigo = contapagar.contacorrente ");		
		sql.append(" left join funcionario as contapagarfuncionario on contapagarfuncionario.codigo = contapagar.funcionario ");
		sql.append(" left join pessoa as contapagarfuncionariopessoa on contapagarfuncionario.pessoa = contapagarfuncionariopessoa.codigo ");
		sql.append(" left join pessoa as contapagarpessoa on contapagarpessoa.codigo = contapagar.pessoa");
		sql.append(" left join parceiro as contapagarparceiro on contapagarparceiro.codigo = contapagar.parceiro ");
		sql.append(" left join fornecedor as contapagarfornecedor on contapagarfornecedor.codigo = contapagar.fornecedor ");
		sql.append(" left join pessoa as contapagarresponsavelfinanceiro on contapagarresponsavelfinanceiro.codigo = contapagar.responsavelfinanceiro ");
		sql.append(" left join banco as contapagarbanco on contapagarbanco.codigo = contapagar.banco ");
		sql.append(" left join operadoracartao as contapagaroperadoracartao on contapagaroperadoracartao.codigo = contapagar.operadoracartao ");		
		sql.append(" left join departamento on departamento.codigo = contapagar.departamento ");					
		sql.append(" left join contapagarnegociacaopagamento on contapagarnegociacaopagamento.contapagar = contapagar.codigo ");				
		sql.append(" left join negociacaopagamento on negociacaopagamento.codigo = contapagarnegociacaopagamento.negociacaocontapagar ");
		if (realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "negociacaopagamentoresponsavel")) {
			sql.append(" left join usuario as negociacaopagamentoresponsavel on negociacaopagamentoresponsavel.codigo = negociacaopagamento.responsavel ");
		}
		sql.append(" left join contapagarpagamento on contapagarpagamento.contapagar = contapagar.codigo and contapagarpagamento.negociacaopagamento = negociacaopagamento.codigo ");
		sql.append(" left join formapagamentonegociacaopagamento on contapagarpagamento.formapagamentonegociacaopagamento = formapagamentonegociacaopagamento.codigo ");		
		sql.append(" left join formapagamento on contapagarpagamento.formapagamento = formapagamento.codigo ");
		sql.append(" left join cheque on formapagamento.tipo = 'CH' and formapagamentonegociacaopagamento.cheque = cheque.codigo ");		

		return sql.toString();
	}

	private String realizarGeracaoFromModuloAcademico(RelatorioSEIDecidirVO relatorioSEIDecidirVO) throws Exception {
		StringBuilder sql = new StringBuilder(" ");
		sql.append(" FROM matricula ");
		sql.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
		sql.append(" inner join periodoletivo as periodoletivomatriculaperiodo on matriculaperiodo.periodoletivomatricula = periodoletivomatriculaperiodo.codigo ");
		sql.append(" inner join turma on matriculaperiodo.turma = turma.codigo ");
		sql.append(" inner join unidadeensino on unidadeensino.codigo = matricula.unidadeensino ");
		if (realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "gradecurricularatual")) {
			sql.append(" inner join gradecurricular as gradecurricularatual on gradecurricularatual.codigo = matricula.gradecurricularatual ");
		}
		
		if (realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "unidadeensinocidade") || realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "unidadeensinoestado")) {			
			sql.append(" left join cidade as unidadeensinocidade on unidadeensino.cidade = unidadeensinocidade.codigo ");
			if (realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "unidadeensinoestado")) {
				sql.append(" left join estado as unidadeensinoestado on unidadeensinoestado.codigo = unidadeensinocidade.estado ");
			}
		}
		sql.append(" inner join curso on curso.codigo = matricula.curso ");
		sql.append(" left join eixocurso on curso.eixocurso = eixocurso.codigo ");
		sql.append(" inner join turno on turno.codigo = matricula.turno ");
		sql.append(" inner join pessoa as aluno on aluno.codigo = matricula.aluno ");
		if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getNivelDetalhamento().equals(RelatorioSEIDecidirNivelDetalhamentoEnum.HISTORICO)) {
			sql.append(" inner join historico on historico.matricula = matricula.matricula and historico.matriculaperiodo = matriculaperiodo.codigo ");
			sql.append(" inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.codigo = historico.matriculaperiodoturmadisciplina ");
			sql.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));
			sql.append(" inner join turma turmaDisciplinaEstudada on turmaDisciplinaEstudada.codigo = matriculaperiodoturmadisciplina.turma ");
			sql.append(" inner join disciplina on disciplina.codigo = historico.disciplina ");
		}
		if (realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "alunocidade") || realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "alunoestado")) {
			sql.append(" left join cidade as alunocidade on aluno.cidade = alunocidade.codigo ");
			if (realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "alunoestado")) {
				sql.append(" left join estado as alunoestado on alunocidade.estado = alunoestado.codigo ");
			}
		}
		if (!relatorioSEIDecidirVO.getTipoFiltroDocumentoAssinado().equals("NAO_CONTROLA")) {
		 sql.append(" left join documentoassinado on documentoassinado.matriculaperiodo = matriculaperiodo.codigo ");
		 sql.append(" and documentoassinado.tipoorigemdocumentoassinado = 'CONTRATO' and documentoassinado.codigo = (");
		 sql.append("	select doc.codigo from documentoassinado doc where doc.matriculaperiodo = matriculaperiodo.codigo");
		 sql.append("	and doc.tipoorigemdocumentoassinado = 'CONTRATO' ");
		 sql.append("	order by doc.dataregistro desc limit 1 ");
		 sql.append(" ) ");
		 sql.append(" left join documentoassinadopessoa on documentoassinadopessoa.documentoassinado = documentoassinado.codigo and documentoassinadopessoa.pessoa = matricula.aluno and documentoassinadopessoa.tipopessoa = 'ALUNO' ");
		 sql.append(" left join documentoassinadopessoa documentoassinadopessoafuncionario1 on documentoassinadopessoafuncionario1.documentoassinado = documentoassinado.codigo and documentoassinadopessoafuncionario1.ordemassinatura = 1 ");
		 sql.append(" left join pessoa as assinaturafuncionario1 on documentoassinadopessoafuncionario1.pessoa = assinaturafuncionario1.codigo ");
		 sql.append(" left join documentoassinadopessoa documentoassinadopessoafuncionario2 on documentoassinadopessoafuncionario2.documentoassinado = documentoassinado.codigo and documentoassinadopessoafuncionario2.ordemassinatura = 2 ");
		 sql.append(" left join pessoa as assinaturafuncionario2 on documentoassinadopessoafuncionario2.pessoa = assinaturafuncionario2.codigo ");
		}
		return sql.toString();
	}
	
	private String realizarGeracaoFromModuloRequerimento(RelatorioSEIDecidirVO relatorioSEIDecidirVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder(" ");
		sqlStr.append(" from requerimento ");
		sqlStr.append(" inner join tipoRequerimento on tipoRequerimento.codigo = requerimento.tipoRequerimento ");
		sqlStr.append(" inner join pessoa on pessoa.codigo = requerimento.pessoa ");
		sqlStr.append(" left join unidadeEnsino on unidadeEnsino.codigo = requerimento.unidadeEnsino ");
		sqlStr.append(" left join matricula on matricula.matricula = requerimento.matricula ");
		sqlStr.append(" left join pessoa aluno on aluno.codigo = matricula.aluno ");
		sqlStr.append(" left join matriculaperiodo on matricula.matricula = matriculaperiodo.matricula and case when requerimento.matriculaperiodo is not null then requerimento.matriculaperiodo = matriculaperiodo.codigo  else  ");
		sqlStr.append(" matriculaperiodo.codigo = (select codigo from matriculaperiodo mp where  matriculaperiodo.matricula = mp.matricula and mp.data<= requerimento.data order by mp.codigo desc limit 1) end ");
		sqlStr.append(" left join periodoletivo as periodoletivomatriculaperiodo on matriculaperiodo.periodoletivomatricula = periodoletivomatriculaperiodo.codigo ");
		sqlStr.append(" left join turma on turma.codigo = matriculaperiodo.turma ");
		sqlStr.append(" left join disciplina on disciplina.codigo = requerimento.disciplina ");
		sqlStr.append(" left join funcionario on funcionario.codigo = requerimento.funcionario ");
		sqlStr.append(" left join pessoa as pessoaFunc on pessoaFunc.codigo = funcionario.pessoa ");
		sqlStr.append(" left join curso on matricula.curso = curso.codigo ");
		sqlStr.append(" left join eixocurso on curso.eixocurso = eixocurso.codigo ");
		sqlStr.append(" left join situacaorequerimentodepartamento on requerimento.situacaorequerimentodepartamento = situacaorequerimentodepartamento.codigo ");		
//		sqlStr.append(" left join cursocoordenador on cursocoordenador.curso = matricula.curso ");
//		sqlStr.append(" and cursocoordenador.unidadeensino = matricula.unidadeensino ");
//		sqlStr.append(" and cursocoordenador.tipoCoordenadorCurso = 'GERAL' ");
//		sqlStr.append(" and cursocoordenador.codigo = ("); 
//			sqlStr.append("	select cc.codigo from cursocoordenador as cc"); 
//			sqlStr.append("	where  cc.curso = matricula.curso and cc.unidadeensino = matricula.unidadeensino"); 
//			sqlStr.append("	and cc.tipoCoordenadorCurso = 'GERAL'" ); 
//			sqlStr.append("	and ((cc.turma is not null and cc.turma = matriculaperiodo.turma)");
//			sqlStr.append("	or (cc.turma is null and  cc.curso = matricula.curso)"); 
//			sqlStr.append(	")" ); 
//			sqlStr.append("	order by case when cc.turma is not null then 0 else 1 end limit 1" ); 
//		sqlStr.append(	")");
		sqlStr.append(" left join usuario responsavel on requerimento.responsavel = responsavel.codigo ");
//		sqlStr.append(" left join funcionario as funcionariocoordenador on funcionariocoordenador.codigo = cursocoordenador.funcionario ");
//		sqlStr.append(" left join pessoa as coordenador on funcionariocoordenador.pessoa = coordenador.codigo ");
		sqlStr.append(" left join departamento departamentoresponsavel on departamentoresponsavel.codigo = requerimento.departamentoresponsavel ");
		return sqlStr.toString();
	}

	@SuppressWarnings("unused")
	private String realizarGeracaoFromModuloTodos(RelatorioSEIDecidirVO relatorioSEIDecidirVO) throws Exception {
		StringBuilder sql = new StringBuilder(" ");
		sql.append(" FROM unidadeensino ");
		if (realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "unidadeensinocidade") || realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "unidadeensinoestado")) {
			sql.append(" left join cidade as unidadeensinocidade on unidadeensino.cidade = unidadeensinocidade.codigo ");
			if (realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "unidadeensinoestado")) {
				sql.append(" left join estado as unidadeensinoestado on unidadeensinoestado.codigo = unidadeensinocidade.estado ");
			}
		}
		sql.append(" left join matricula on unidadeensino.codigo = matricula.unidadeensino ");
		sql.append(" left join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
		sql.append(" left join turma on matriculaperiodo.turma = turma.codigo ");
		sql.append(" left join curso on curso.codigo = matricula.curso ");
		sql.append(" left join turno on turno.codigo = matricula.turno ");
		sql.append(" left join pessoa as aluno on aluno.codigo = matricula.aluno ");
		if (realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "alunocidade") || realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "alunoestado")) {
			sql.append(" left join cidade as alunocidade on aluno.cidade = alunocidade.codigo ");
			if (realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "alunoestado")) {
				sql.append(" left join estado as alunoestado on alunocidade.estado = alunoestado.codigo ");
			}
		}
		return sql.toString();
	}

	/**
	 * Este método gerar a clausula where do sql de consulta para a geração do
	 * relatório de acordo com o layout selecionado, observando o nivel de
	 * detalhamento bem como o modulo em que o mesmo se refere
	 * 
	 * @param relatorioSEIDecidirVO
	 * @return
	 * @throws Exception
	 */
	private String realizarGeracaoWhere(RelatorioSEIDecidirVO relatorioSEIDecidirVO, Map<String, Object> mapaParametrosRelatorio, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder("");
		if (!relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getLayoutPersonalizado()) {
			sql = new StringBuilder(" WHERE 1 = 1");
		} 		
		if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.RECURSOS_HUMANOS)) {
			realizarGeracaoWhereRecursosHumano(relatorioSEIDecidirVO, mapaParametrosRelatorio, sql, usuarioVO);	
		} else if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.CRM)) {
			realizarGeracaoWhereCrm(relatorioSEIDecidirVO, mapaParametrosRelatorio, sql, false);
		}else if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.FINANCEIRO_CENTRO_RESULTADO)) {
			realizarGeracaoWhereFinanceiroCentroResultado(relatorioSEIDecidirVO, mapaParametrosRelatorio, sql);
		} else if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.PLANO_ORCAMENTARIO)) {
			sql.append(adicionarFiltroPlanoOrcamentario(relatorioSEIDecidirVO));
		} else if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.PROCESSO_SELETIVO)) {
			sql.append(adicionarFiltroProcessoSeletivo(relatorioSEIDecidirVO));
		} else if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.BIBLIOTECA)) {
			sql.append(adicionarFiltroBiblioteca(relatorioSEIDecidirVO));
		} else if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.ESTAGIO)) {
			sql.append(adicionarFiltroEstagio(relatorioSEIDecidirVO));
		} else {
			if (Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirArquivoVO().getSqlWhere())) {

				montarDadosSQLSubRelatorioPDF(relatorioSEIDecidirVO, mapaParametrosRelatorio, sql);
			} 
			if (!Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirArquivoVO().getSqlWhere()) || (Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirArquivoVO().getSqlWhere()) && relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirArquivoVO().getUtilizarFiltrosPrincipais())) {		
				sql.append(adicionarFiltroUnidadeEnsino(relatorioSEIDecidirVO));
				if (!relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.FINANCEIRO_DESPESA)) {
					sql.append(adicionarFiltroCurso(relatorioSEIDecidirVO));
				}
				if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.FINANCEIRO_DESPESA)) {
					sql.append(adicionarFiltroFinanceiroDespesa(relatorioSEIDecidirVO));
				}
				if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.FINANCEIRO_RECEITA)) {
					sql.append(adicionarFiltroFinanceiroReceita(relatorioSEIDecidirVO));
					sql.append(adicionarFiltroPeriodoLetivo(relatorioSEIDecidirVO, relatorioSEIDecidirVO.getPeriodoLetivo()));
				}
				if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.FINANCEIRO_FECHAMENTO_MES_RECEITA)) {
					sql.append(adicionarFiltroFinanceiroFechamentoMesReceita(relatorioSEIDecidirVO));
					sql.append(adicionarFiltroPeriodoLetivo(relatorioSEIDecidirVO, relatorioSEIDecidirVO.getPeriodoLetivo()));
				}
				if (!relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.FINANCEIRO_DESPESA) 
				 && !relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.FINANCEIRO_FECHAMENTO_MES_RECEITA)
				 && !relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.REQUERIMENTO)
				 && !relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.ADMINISTRATIVO)
				 && !relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.COMPRA)
				 && !relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.NOTA_FISCAL)
				 && !relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.PATRIMONIO)
				 && !relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.PLANO_ORCAMENTARIO)
				 ) {
					sql.append(adicionarFiltroAcademico(relatorioSEIDecidirVO));
				}
				if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.ACADEMICO)) {
					sql.append(adicionarFiltroDocumentoAssinado(relatorioSEIDecidirVO, relatorioSEIDecidirVO.getTipoFiltroDocumentoAssinado()));
					sql.append(adicionarFiltroPeriodoAceiteContrato(relatorioSEIDecidirVO));
					sql.append(adicionarFiltroPeriodoLetivo(relatorioSEIDecidirVO, relatorioSEIDecidirVO.getPeriodoLetivo()));
					sql.append(adicionarFiltroTurno(relatorioSEIDecidirVO));
				}
				if (!relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.REQUERIMENTO)) {
					sql.append(adicionarFiltroPeriodo(relatorioSEIDecidirVO));					
				} else {
					sql.append(adicionarFiltroRequerimento(relatorioSEIDecidirVO));
					sql.append(adicionarFiltroPeriodoLetivo(relatorioSEIDecidirVO, relatorioSEIDecidirVO.getPeriodoLetivo()));
				}
								
			}
		}
		realizarGeracaoWhereAdicional(relatorioSEIDecidirVO, mapaParametrosRelatorio, sql);
		realizarGeracaoFiltroPersonalizado(sql, relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO(), usuarioVO);
		return sql.toString();
	}

	private void realizarGeracaoWhereRecursosHumano(RelatorioSEIDecidirVO relatorioSEIDecidirVO, Map<String, Object> mapaParametrosRelatorio, StringBuilder sql, UsuarioVO usuarioVO) throws Exception {
		if (Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirArquivoVO().getSqlWhere())) {
			montarDadosSQLSubRelatorioPDF(relatorioSEIDecidirVO, mapaParametrosRelatorio, sql);

			if (!relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getNivelDetalhamento().getName().equals(RelatorioSEIDecidirNivelDetalhamentoEnum.FUNCIONARIO_CARGO.name())) {
				sql.append(" AND ").append(realizarGeracaoWherePeriodo(relatorioSEIDecidirVO.getDataInicioPeriodo(), relatorioSEIDecidirVO.getDataFimPeriodo(), "cfp.dataCompetencia", true));
			}
		} else {
			sql.append(adicionarFiltroRecursosHumanos(relatorioSEIDecidirVO, usuarioVO));
		}
	}

	public void montarDadosSQLSubRelatorioPDF(RelatorioSEIDecidirVO relatorioSEIDecidirVO, Map<String, Object> mapaParametrosRelatorio, StringBuilder sql) {
		String where = montarDadosWhereSubRelatorio(relatorioSEIDecidirVO, mapaParametrosRelatorio);
		for(String tag: relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getTagCondicaoWhereUtilizada().keySet()){
			if(where.contains(tag)){
				PerfilTagSEIDecidirEnum tagEnum = relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getTagCondicaoWhereUtilizada().get(tag);
				where = where.replace(tag+tagEnum.getCampo(), tagEnum.getCampo());
			}
		}

		sql.append(where);
	}

	public String montarDadosWhereSubRelatorio(RelatorioSEIDecidirVO relatorioSEIDecidirVO, Map<String, Object> mapaParametrosRelatorio) {
		String where = relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirArquivoVO().getSqlWhere();
		TagSEIDecidirEntidadeEnum[] tags = TagSEIDecidirEntidadeEnum.getValues(relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getModulo(), false);
		for (TagSEIDecidirEntidadeEnum tagSEIDecidirEntidadeEnum : tags) {
			if (Uteis.isAtributoPreenchido(tagSEIDecidirEntidadeEnum)) {
				for (Enum<? extends PerfilTagSEIDecidirEnum> tagSEIDecidirEnum : tagSEIDecidirEntidadeEnum.getTagSeiDecidirEnum()) {
					if (where.contains(tagSEIDecidirEnum.toString())) {
						if (mapaParametrosRelatorio != null && !mapaParametrosRelatorio.isEmpty()) {
							where = where.replace(tagSEIDecidirEnum.toString(), mapaParametrosRelatorio.get(tagSEIDecidirEnum.toString()).toString());
						}
					}

				}
			}
		}
		for(String param : mapaParametrosRelatorio.keySet()) {			
			if(where.contains(param)) {								
				if(mapaParametrosRelatorio.get(param) instanceof String) {
					where = where.replace(param, "'"+(String) mapaParametrosRelatorio.get(param)+"'");								
				}else if(mapaParametrosRelatorio.get(param) instanceof TimeStamp) {
					where = where.replace(param, "'"+Uteis.getDataJDBCTimestamp((Date)mapaParametrosRelatorio.get(param))+"'");
				}else if(mapaParametrosRelatorio.get(param) instanceof Date) {
					where = where.replace(param, "'"+Uteis.getDataJDBC((Date)mapaParametrosRelatorio.get(param))+"'");
				}else {
					where = where.replace(param, mapaParametrosRelatorio.get(param).toString());
				}				
			}
		}
		return where;
	}
	

	
	
	private void realizarGeracaoWhereCrm(RelatorioSEIDecidirVO relatorioSEIDecidirVO, Map<String, Object> mapaParametrosRelatorio, StringBuilder sql, boolean utilizarPeriodo) {
		sql.append(adicionarFiltroUnidadeEnsino(relatorioSEIDecidirVO));
//		sql.append(adicionarFiltroCurso(relatorioSEIDecidirVO));
		sql.append(adicionarFiltroCampanha(relatorioSEIDecidirVO));
		sql.append(adicionarFiltroFuncionario(relatorioSEIDecidirVO));
//		sql.append(adicionarFiltroPeriodo(relatorioSEIDecidirVO, utilizarPeriodo));
//		sql.append(adicionarFiltroRelatorioCompromissoAgendaVO(relatorioSEIDecidirVO.getRelatorioCompromissoAgendaVO(), "compromissoagendapessoahorario.tiposituacaocompromissoenum"));
//		if (Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getMatriculaVO().getMatricula())) {
//			sql.append(" and matricula.matricula = '").append(relatorioSEIDecidirVO.getMatriculaVO().getMatricula()).append("' ");
//		}
		
	}
	
	
	private String adicionarFiltroCampanha(RelatorioSEIDecidirVO relatorioSEIDecidirVO) {
		if (!relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoCampanha()) {
			return "";
		}
		StringBuilder sql = new StringBuilder("");
		for (CampanhaVO campanhaVO : relatorioSEIDecidirVO.getListaCampanhaVOs()) {
			if (campanhaVO.getFiltrarCampanhaVO()) {
				sql.append(sql.length() == 0 ? " and campanha.codigo in(" : ", ").append(campanhaVO.getCodigo());
			}
		}
		if (sql.length() > 0) {
			sql.append(") ");
		}
		return sql.toString();
	}
	
	private String adicionarFiltroFuncionario(RelatorioSEIDecidirVO relatorioSEIDecidirVO) {
		if (!relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoConsultor()) {
			return "";
		}
		StringBuilder sql = new StringBuilder("");
		for (FuncionarioVO funcionarioVO : relatorioSEIDecidirVO.getListaFuncionarioVOs()) {
			if (funcionarioVO.getFiltrarFuncionarioVO()) {
				sql.append(sql.length() == 0 ? " and funcionario.codigo in(" : ", ").append(funcionarioVO.getCodigo());
			}
		}
		if (sql.length() > 0) {
			sql.append(") ");
		}
		return sql.toString();
	}
	
	
	private void realizarGeracaoWhereAdicional(RelatorioSEIDecidirVO relatorioSEIDecidirVO, Map<String, Object> mapaParametrosRelatorio, StringBuilder sql) {
		if(!relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getCondicaoWhereAdicional().trim().isEmpty()){
			String where = relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getCondicaoWhereAdicional();
			for(String tag: relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getTagCondicaoWhereUtilizada().keySet()){
				if(where.contains(tag)){
					PerfilTagSEIDecidirEnum tagEnum = relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getTagCondicaoWhereUtilizada().get(tag);
					where = where.replace(tag, tagEnum.getCampo());
				}
			}
			if(relatorioSEIDecidirVO.getDataBase() == null){
				where = where.replaceAll("CONTA_RECEBER_DATA_BASE", "null");
			}else{
				where = where.replaceAll("CONTA_RECEBER_DATA_BASE", "'"+Uteis.getDataJDBC(relatorioSEIDecidirVO.getDataBase()).toString()+"'");				
			}
			sql.append(" and (").append(where).append(") ");
		}
	}

	private String adicionarFiltroRecursosHumanos(RelatorioSEIDecidirVO obj, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append(getFacadeFactory().getTemplateLancamentoFolhaPagamentoInterfaceFacade().getFiltrosDoTemplate(obj.getTemplateLancamentoFolhaPagamento()));

		if (!obj.getLayoutRelatorioSEIDecidirVO().getNivelDetalhamento().getName().equals(RelatorioSEIDecidirNivelDetalhamentoEnum.FUNCIONARIO_CARGO.name())
				&& !obj.getLayoutRelatorioSEIDecidirVO().getNivelDetalhamento().getName().equals(RelatorioSEIDecidirNivelDetalhamentoEnum.PERIODO_AQUISITIVO.name()) 
				&& !obj.getLayoutRelatorioSEIDecidirVO().getNivelDetalhamento().getName().equals(RelatorioSEIDecidirNivelDetalhamentoEnum.FALTAS_FUNCIONARIO.name()) 
				&& !obj.getLayoutRelatorioSEIDecidirVO().getNivelDetalhamento().getName().equals(RelatorioSEIDecidirNivelDetalhamentoEnum.PROGRESSAO_SALARIAL.name())
				&& !obj.getLayoutRelatorioSEIDecidirVO().getNivelDetalhamento().getName().equals(RelatorioSEIDecidirNivelDetalhamentoEnum.CONTA_PAGAR_FUNCIONARIO.name())
				&& !obj.getLayoutRelatorioSEIDecidirVO().getNivelDetalhamento().getName().equals(RelatorioSEIDecidirNivelDetalhamentoEnum.ATIVIDADE_EXTRA_CLASSE_PROFESSOR.name())) {
			sql.append(" AND ").append(realizarGeracaoWherePeriodo(obj.getDataInicioPeriodo(), obj.getDataFimPeriodo(), "cfp.dataCompetencia", true));
		}

		if (obj.getLayoutRelatorioSEIDecidirVO().getNivelDetalhamento().getName().equals(RelatorioSEIDecidirNivelDetalhamentoEnum.CONTA_PAGAR_FUNCIONARIO.name())) {
			sql.append(" AND ").append(realizarGeracaoWherePeriodo(obj.getDataInicioPeriodo(), obj.getDataFimPeriodo(), "contapagar.dataVencimento", true));
		}

		if (obj.getLayoutRelatorioSEIDecidirVO().getNivelDetalhamento().getName().equals(RelatorioSEIDecidirNivelDetalhamentoEnum.FALTAS_FUNCIONARIO.name())) {
			sql.append(" AND ").append(realizarGeracaoWherePeriodo(obj.getDataInicioPeriodo(), obj.getDataFimPeriodo(), "faltasfuncionario.dataInicio", true));
		}

		if (obj.getLayoutRelatorioSEIDecidirVO().getNivelDetalhamento().getName().equals(RelatorioSEIDecidirNivelDetalhamentoEnum.PROGRESSAO_SALARIAL.name())) {
			sql.append(" AND ").append(realizarGeracaoWherePeriodo(obj.getDataInicioPeriodo(), obj.getDataFimPeriodo(), "historicosalarial.dataMudanca", true));
		}

		if (obj.getLayoutRelatorioSEIDecidirVO().getNivelDetalhamento().getName().equals(RelatorioSEIDecidirNivelDetalhamentoEnum.EVENTO_FOLHA_PAGAMENTO.name())) {
			sql.append(" AND eventofolhapagamento.codigo = ").append(obj.getEventoFolhaPagamentoVO().getCodigo());
		}
		
		if (obj.getLayoutRelatorioSEIDecidirVO().getNivelDetalhamento().getName().equals(RelatorioSEIDecidirNivelDetalhamentoEnum.ATIVIDADE_EXTRA_CLASSE_PROFESSOR.name())) {
			
			if (Uteis.isAtributoPreenchido(obj.getFuncionarioCargoVO())) {
				sql.append(" and atividadeextraclasseprofessorpostado.funcionariocargo = ").append(obj.getFuncionarioCargoVO().getCodigo());
				obj.getFiltro().append("Funcionário Cargo: ").append(obj.getFuncionarioCargoVO().getFuncionarioVO().getPessoa().getNome()).append("'\n'");
			}
			
			if (Uteis.isAtributoPreenchido(usuarioVO) && usuarioVO.getIsApresentarVisaoCoordenador()) {
				FuncionarioVO funcionarioVO = getFacadeFactory().getFuncionarioFacade().consultarPorCodigoPessoa(usuarioVO.getPessoa().getCodigo(), null, false, 
						Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO);
				sql.append(" and fc.curso in (select curso from cursocoordenador where cursocoordenador.funcionario = ").append(funcionarioVO.getCodigo()).append(")");
			}

			if (obj.getSituacoes() != null && obj.getSituacoes().length > 0) {
				StringBuilder filtro = new StringBuilder("");

				sql.append(realizarGeracaoInValor(obj.getSituacoes().length, obj.getSituacoes(), " and atividadeextraclasseprofessorpostado.situacao"));
				obj.getFiltro().append("Situações: ").append(filtro.toString()).append("\n");
			}
			
			sql.append(" and " + realizarGeracaoWherePeriodo(obj.getDataInicio(), obj.getDataFim(), "atividadeextraclasseprofessorpostado.dataatividade", false));
			
			if (Uteis.isAtributoPreenchido(obj.getCursoVO())) {
				sql.append(" and curso.codigo = ").append(obj.getCursoVO().getCodigo());
			}

			if (Uteis.isAtributoPreenchido(obj.getDataInicioPeriodo()) || Uteis.isAtributoPreenchido(obj.getDataFimPeriodo())) {
				obj.getFiltro().append("Data Início Período : ").append(UteisData.getDataFormatada(obj.getDataInicioPeriodo())).append("\n");
				obj.getFiltro().append("Data Final Período : ").append(UteisData.getDataFormatada(obj.getDataFimPeriodo())).append("\n");
			}
			sql.append(" and ").append(realizarGeracaoWherePeriodo(obj.getDataInicioPeriodo(), obj.getDataFimPeriodo(), "atividadeextraclasseprofessorpostado.dataatividade", false));
		} else {
			if (Uteis.isAtributoPreenchido(obj.getCargoVO())) {
				sql.append(" AND c.codigo = ").append(obj.getCargoVO().getCodigo());
			}

			if (Uteis.isAtributoPreenchido(obj.getMotivoProgressaoSalarial())) {
				sql.append(" AND historicosalarial.motivomudanca =").append("'" + obj.getMotivoProgressaoSalarial() + "'");
			}

			if (Uteis.isAtributoPreenchido(obj.getSituacaoPeriodoAquisitivo())) {
				sql.append(" AND periodoaquisitivoferias.situacao =").append("'" + obj.getSituacaoPeriodoAquisitivo() + "'");
			}
		}

		sql.append(obj.getLayoutRelatorioSEIDecidirVO().getCondicaoWhereAdicional());

		return sql.toString();
	}

	private void sqlFiltroCentroResultadoArvoreInferior(RelatorioSEIDecidirVO obj, StringBuilder sql) {
		if (obj.getConsiderarCentroResultadoArvoreInferior()) {
			sql.append(" and centroresultado.codigo in (");
			sql.append(" select \"centroresultado.codigo\" from ( ");
			sql.append(" WITH RECURSIVE  cdSuperior (  \"centroresultado.codigo\", \"centroresultado.descricao\", \"centroresultado.identificadorcentroresultado\", \"centroresultadoprincipal.codigo\"    )as ( ");
			sql.append(" SELECT centroresultado.codigo as \"centroresultado.codigo\", centroresultado.descricao as \"centroresultado.descricao\" , centroresultado.identificadorcentroresultado as \"centroresultado.identificadorcentroresultado\", centroresultado.centroresultadoprincipal as \"centroresultadoprincipal.codigo\"");
			sql.append(" FROM centroresultado  where centroresultado.codigo ").append(realizarGeracaoInValor(obj.getCentrosResultadoVOs().size(), 
					gerarArray(obj.getCentrosResultadoVOs())));
			sql.append(" union");
			sql.append(" SELECT centroresultado.codigo as \"centroresultado.codigo\", centroresultado.descricao as \"centroresultado.descricao\",  centroresultado.identificadorcentroresultado as \"centroresultado.identificadorcentroresultado\", centroresultado.centroresultadoprincipal as \"centroresultadoprincipal.codigo\"");
			sql.append(" FROM centroresultado ");
			sql.append(" inner join cdSuperior on centroresultado.codigo = cdSuperior.\"centroresultadoprincipal.codigo\"  )");
			sql.append(" select * from cdSuperior order by   case when cdSuperior.\"centroresultadoprincipal.codigo\" is null then 0   when cdSuperior.\"centroresultadoprincipal.codigo\" > cdSuperior.\"centroresultado.codigo\" then cdSuperior.\"centroresultado.codigo\"   else cdSuperior.\"centroresultadoprincipal.codigo\" end,   cdSuperior.\"centroresultado.codigo\"");
			sql.append("  ) as t)");
		}
	}

	private void sqlFiltroCategoriaProdutoArvoreInferior(RelatorioSEIDecidirVO obj, StringBuilder sql) {
		if (obj.getConsiderarCategoriaProdutoArvoreInferior()) {
			sql.append(" and categoriaproduto.codigo in (");
			sql.append(" select \"categoriaproduto.codigo\" from ( ");
			sql.append(" WITH RECURSIVE  cdSuperior (  \"categoriaproduto.codigo\", \"categoriaproduto.nome\", \"categoriaprodutopai.codigo\"    )as (");
			sql.append(" SELECT categoriaproduto.codigo as \"categoriadespesa.codigo\", categoriaproduto.nome as \"categoriaproduto.nome\" , categoriaproduto.categoriaprodutopai as \"categoriaprodutopai.codigo\"");
			sql.append(" FROM categoriaproduto  where categoriaproduto.codigo ").append(realizarGeracaoInValor(obj.getCategoriasProdutoVOs().size(), 
					gerarArray(obj.getCategoriasProdutoVOs())));
			sql.append(" union");
			sql.append(" SELECT categoriaproduto.codigo as \"categoriaproduto.codigo\", categoriaproduto.nome as \"categoriaproduto.nome\", categoriaproduto.categoriaprodutopai as \"categoriaprodutopai.codigo\"");
			sql.append(" FROM categoriaproduto ");
			sql.append(" inner join cdSuperior on categoriaproduto.codigo = cdSuperior.\"categoriaprodutopai.codigo\"  )");
			sql.append(" select * from cdSuperior order by   case when cdSuperior.\"categoriaprodutopai.codigo\" is null then 0   when cdSuperior.\"categoriaprodutopai.codigo\" > cdSuperior.\"categoriaproduto.codigo\" then cdSuperior.\"categoriaproduto.codigo\"   else cdSuperior.\"categoriaprodutopai.codigo\" end,   cdSuperior.\"categoriaproduto.codigo\"");
			sql.append(" ) as t)");
		}
	}

	private void sqlFiltroCategoriaDespesaArvoreInferior(RelatorioSEIDecidirVO obj, StringBuilder sql) {
		if (obj.getConsiderarCategoriaDespesaArvoreInferior()) {
			sql.append(" and categoriadespesa.codigo in (");
			sql.append("  select \"categoriadespesa.codigo\" from ( ");
			sql.append(" WITH RECURSIVE  cdSuperior (  \"categoriadespesa.codigo\", \"categoriadespesa.descricao\", \"categoriadespesa.identificadorcategoriadespesa\", \"categoriadespesaprincipal.codigo\"    )as ( ");
			sql.append(" SELECT categoriadespesa.codigo as \"categoriadespesa.codigo\", categoriadespesa.descricao as \"categoriadespesa.descricao\" , categoriadespesa.identificadorcategoriadespesa as \"categoriadespesa.identificadorcategoriadespesa\", categoriadespesa.categoriadespesaprincipal as \"categoriadespesaprincipal.codigo\"  ");
			sql.append(" FROM categoriadespesa  where categoriadespesa.codigo ").append(realizarGeracaoInValor(obj.getCategoriaDespesaVOs().size(), 
					gerarArray(obj.getCategoriaDespesaVOs())));
			sql.append("  union ");
			sql.append(" SELECT categoriadespesa.codigo as \"categoriadespesa.codigo\", categoriadespesa.descricao as \"categoriadespesa.descricao\",  categoriadespesa.identificadorcategoriadespesa as \"categoriadespesa.identificadorcategoriadespesa\", categoriadespesa.categoriadespesaprincipal as \"categoriadespesaprincipal.codigo\"  ");
			sql.append(" FROM categoriadespesa  ");
			sql.append(" inner join cdSuperior on categoriadespesa.codigo = cdSuperior.\"categoriadespesaprincipal.codigo\"  ) ");
			sql.append(" select * from cdSuperior order by   case when cdSuperior.\"categoriadespesaprincipal.codigo\" is null then 0   when cdSuperior.\"categoriadespesaprincipal.codigo\" > cdSuperior.\"categoriadespesa.codigo\" then cdSuperior.\"categoriadespesa.codigo\"   else cdSuperior.\"categoriadespesaprincipal.codigo\" end,   cdSuperior.\"categoriadespesa.codigo\" ");
			sql.append(" ) as t)");
		}
	}

	private String adicionarFiltroAcademico(RelatorioSEIDecidirVO relatorioSEIDecidirVO) {
		StringBuilder sql = new StringBuilder("");
		if (Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getTurmaVO().getCodigo()) && relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoTurma()) {
			sql.append(" and turma.codigo = ").append(relatorioSEIDecidirVO.getTurmaVO().getCodigo());
		}
		if (Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getTurmaEstudouDisciplinaVO().getCodigo()) && relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getNivelDetalhamento().equals(RelatorioSEIDecidirNivelDetalhamentoEnum.HISTORICO)  && relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoTurmaEstudouDisciplina()) {
			sql.append(" and matriculaPeriodoTurmaDisciplina.turma = ").append(relatorioSEIDecidirVO.getTurmaEstudouDisciplinaVO().getCodigo());
		}
		if (Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getDisciplinaVO().getCodigo()) && relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getNivelDetalhamento().equals(RelatorioSEIDecidirNivelDetalhamentoEnum.HISTORICO) && relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoDisciplina()) {
			sql.append(" and historico.disciplina = ").append(relatorioSEIDecidirVO.getDisciplinaVO().getCodigo());
		}
		if (Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getMatriculaVO().getMatricula()) && relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoMatricula()) {
			sql.append(" and matricula.matricula = '").append(relatorioSEIDecidirVO.getMatriculaVO().getMatricula()).append("' ");
		}

		if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.FINANCEIRO_RECEITA) ) {
			if((relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoSituacaoFiltroSituacaoAcademica() || relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoSituacaoFinanceiraMatricula())) {
			if (relatorioSEIDecidirVO.getRelatorioAcademicoVO().getConsiderarSituacaoAtualMatricula()) {
				
				if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoSituacaoFiltroSituacaoAcademica()) {
					sql.append(" and ((matriculaPeriodo.codigo is not null and ").append(adicionarFiltroSituacaoAcademicaMatricula(relatorioSEIDecidirVO.getRelatorioAcademicoVO(), "matricula")).append(" ");
				} else {
					sql.append(" and ((1=1 ");
				}
				if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoSituacaoFinanceiraMatricula()) {
					sql.append(" and ").append(adicionarFiltroSituacaoFinanceiraMatriculaPeriodo(relatorioSEIDecidirVO.getRelatorioAcademicoVO(), "matriculaPeriodo")).append(") ");
				} else {
					sql.append(" and 1=1) ");
				}
				if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoSituacaoFiltroSituacaoAcademica()) {
					sql.append(" or (matriculaPeriodo.codigo is null  and ").append(adicionarFiltroSituacaoAcademicaMatricula(relatorioSEIDecidirVO.getRelatorioAcademicoVO(), "matricula")).append(") ");
				} else {
					sql.append(" or (1=1) ");
				}
				if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoSituacaoFiltroSituacaoAcademica() || relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoSituacaoFinanceiraMatricula()) {
					sql.append(" or (matriculaPeriodo.codigo is null  and matricula.matricula is null )) ");
				}
			} else {
				if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoSituacaoFiltroSituacaoAcademica()) {
					sql.append(" and ((matriculaPeriodo.codigo is not null and ").append(adicionarFiltroSituacaoAcademicaMatriculaPeriodo(relatorioSEIDecidirVO.getRelatorioAcademicoVO(), "matriculaPeriodo")).append(" ");
				} else {
					sql.append(" and ((1=1 ");
				}
				if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoSituacaoFinanceiraMatricula()) {
					sql.append(" and ").append(adicionarFiltroSituacaoFinanceiraMatriculaPeriodo(relatorioSEIDecidirVO.getRelatorioAcademicoVO(), "matriculaPeriodo")).append(") ");
				} else {
					sql.append(" and 1=1) ");
				}
				if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoSituacaoFiltroSituacaoAcademica()) {
					sql.append(" or (matriculaPeriodo.codigo is null  and ").append(adicionarFiltroSituacaoAcademicaMatricula(relatorioSEIDecidirVO.getRelatorioAcademicoVO(), "matricula")).append(") ");
				} else {
					sql.append(" or (1=1) ");
				}
				if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoSituacaoFiltroSituacaoAcademica() || relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoSituacaoFinanceiraMatricula()) {
					sql.append(" or (matriculaPeriodo.codigo is null  and matricula.matricula is null )) ");
				}
			}
			}
		} else {
			if (relatorioSEIDecidirVO.getRelatorioAcademicoVO().getConsiderarSituacaoAtualMatricula()) {
				if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoSituacaoFiltroSituacaoAcademica()) {
					sql.append(" and (").append(adicionarFiltroSituacaoAcademicaMatricula(relatorioSEIDecidirVO.getRelatorioAcademicoVO(), "matricula")).append(") ");
				}
				if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoSituacaoFinanceiraMatricula()) {
					sql.append(" and ").append(adicionarFiltroSituacaoFinanceiraMatriculaPeriodo(relatorioSEIDecidirVO.getRelatorioAcademicoVO(), "matriculaPeriodo"));
				}
			}else{
				if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoSituacaoFiltroSituacaoAcademica()) {
					sql.append(" and (").append(adicionarFiltroSituacaoAcademicaMatriculaPeriodo(relatorioSEIDecidirVO.getRelatorioAcademicoVO(), "matriculaPeriodo")).append(") ");
				}
				if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoSituacaoFinanceiraMatricula()) {
					sql.append(" and ").append(adicionarFiltroSituacaoFinanceiraMatriculaPeriodo(relatorioSEIDecidirVO.getRelatorioAcademicoVO(), "matriculaPeriodo"));
				}
			}
		}
		
//		if (relatorioSEIDecidirVO.getRelatorioAcademicoVO().getConsiderarSituacaoAtualMatricula()) {
//			sql.append(" and (").append(adicionarFiltroSituacaoAcademicaMatricula(relatorioSEIDecidirVO.getRelatorioAcademicoVO(), "matricula")).append(") ");
//		}else{
//			sql.append(" and (").append(adicionarFiltroSituacaoAcademicaMatriculaPeriodo(relatorioSEIDecidirVO.getRelatorioAcademicoVO(), "matriculaPeriodo")).append(") ");
//			sql.append(" and ").append(adicionarFiltroSituacaoFinanceiraMatriculaPeriodo(relatorioSEIDecidirVO.getRelatorioAcademicoVO(), "matriculaPeriodo"));
//		}
		return sql.toString();
	}

	private String adicionarFiltroFinanceiroReceita(RelatorioSEIDecidirVO relatorioSEIDecidirVO) {
		StringBuilder sql = new StringBuilder("");
		if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.FINANCEIRO_RECEITA)) {
			if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoCentroReceita()) {
				sql.append(adicionarFiltroCentroReceita(relatorioSEIDecidirVO));
			}
			
			if (Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getContaCorrenteVO().getCodigo()) && relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoContaCorrente()) {
				sql.append(" and contacorrente.codigo = ").append(relatorioSEIDecidirVO.getContaCorrenteVO().getCodigo());
			}
			if (Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getTipoPessoaEnum()) && !relatorioSEIDecidirVO.getTipoPessoaEnum().equals(TipoPessoa.NENHUM) && relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoTipoPessoa()) {
				if(relatorioSEIDecidirVO.getTipoPessoaEnum().equals(TipoPessoa.RESPONSAVEL_FINANCEIRO) || relatorioSEIDecidirVO.getTipoPessoaEnum().equals(TipoPessoa.ALUNO)){
					sql.append(" and contareceber.tipoPessoa in ('").append(TipoPessoa.RESPONSAVEL_FINANCEIRO.getValor()).append("', '").append(TipoPessoa.ALUNO.getValor()).append("', '").append(TipoPessoa.PARCEIRO.getValor()).append("') ");
				}else{
					sql.append(" and contareceber.tipoPessoa = '").append(relatorioSEIDecidirVO.getTipoPessoaEnum().getValor()).append("' ");
				}
				sql.append(adicionarFiltroSacadoContaReceber(relatorioSEIDecidirVO));
			}
			if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoTipoOrigem()) {
				sql.append(adicionarFiltroTipoOrigem(relatorioSEIDecidirVO.getRelatorioFinanceiroVO()));
			}
			if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoSituacaoContaReceber()) {
				sql.append(adicionarFiltroSituacaoContaReceber(relatorioSEIDecidirVO));
			}
			if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoFormaPagamento()) {
				sql.append(adicionarFiltroFormaPagamentoFechamentoMesReceita(relatorioSEIDecidirVO, "formapagamento.codigo"));
			}

		}
		return sql.toString();
	}
	
	private String adicionarFiltroFinanceiroFechamentoMesReceita(RelatorioSEIDecidirVO relatorioSEIDecidirVO) {
		StringBuilder sql = new StringBuilder("");
		if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.FINANCEIRO_FECHAMENTO_MES_RECEITA)) {
			if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoFormaPagamento()) {
				sql.append(adicionarFiltroFormaPagamentoFechamentoMesReceita(relatorioSEIDecidirVO, "fechamentoFinanceiroFormaPagamento.formaPagamento"));
			}			
			if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoCentroReceita()) {
				sql.append(adicionarFiltroCentroReceitaFechamentoMesReceita(relatorioSEIDecidirVO));
			}
			
			if (Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getContaCorrenteVO().getCodigo()) && relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoContaCorrente()) {
				sql.append(adicionarFiltroContaCorrenteFechamentoMesReceita(relatorioSEIDecidirVO));
			}
			if (Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getTipoPessoaEnum()) && !relatorioSEIDecidirVO.getTipoPessoaEnum().equals(TipoPessoa.NENHUM) && relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoTipoPessoa()) {
				sql.append(adicionarFiltroSacadoFechamentoMesReceita(relatorioSEIDecidirVO));
			}
			if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoTipoOrigem()) {
				sql.append(adicionarFiltroTipoOrigemFechamentoMesReceita(relatorioSEIDecidirVO.getRelatorioFinanceiroVO()));
			}
			if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoSituacaoContaReceber()) {
				sql.append(adicionarFiltroSituacaoContaReceberFechamentoMesReceita(relatorioSEIDecidirVO));
			}
			if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoPeriodoFechamentoMesAno()) {
				sql.append(adicionarFiltroAnoSemestreFechamento(relatorioSEIDecidirVO));
			}
			
		}
		return sql.toString();
	}
	
	public String adicionarFiltroAnoSemestreFechamento(RelatorioSEIDecidirVO relatorioSEIDecidirVO) {
		StringBuilder sqlStr = new StringBuilder("");
		if (Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getPeriodoFechamentoMesAno())) {
			sqlStr.append(" and fechamentoMes.mes = '").append(Uteis.getMesData(relatorioSEIDecidirVO.getPeriodoFechamentoMesAno())).append("' ");
			sqlStr.append(" and fechamentoMes.ano = '").append(Uteis.getAno(relatorioSEIDecidirVO.getPeriodoFechamentoMesAno())).append("' ");
		}
		return sqlStr.toString();
	}

	private String adicionarFiltroFinanceiroDespesa(RelatorioSEIDecidirVO relatorioSEIDecidirVO) {
		StringBuilder sql = new StringBuilder("");
		if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.FINANCEIRO_DESPESA)) {
			sql.append(adicionarFiltroCategoriaDespesa(relatorioSEIDecidirVO));
			if (Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getContaCorrenteVO().getCodigo()) && relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoContaCorrente()) {
				sql.append(" and contacorrente.codigo = ").append(relatorioSEIDecidirVO.getContaCorrenteVO().getCodigo());
			}
			if (Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getTipoSacadoEnum()) && relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoFavorecido()) {
				sql.append(" and contapagar.tipoSacado = '").append(relatorioSEIDecidirVO.getTipoSacadoEnum().getValor()).append("' ");
				sql.append(adicionarFiltroSacadoContaPagar(relatorioSEIDecidirVO));
			}
			if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoSituacaoContaPagar()) {
				sql.append(adicionarFiltroSituacaoContaPagar(relatorioSEIDecidirVO));
			}
			if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoFormaPagamento()) {
				sql.append(adicionarFiltroFormaPagamentoFechamentoMesReceita(relatorioSEIDecidirVO, "formapagamento.codigo"));
			}
		}
		return sql.toString();
	}

	private String adicionarFiltroSituacaoContaReceber(RelatorioSEIDecidirVO relatorioSEIDecidirVO) {
		StringBuilder sqlStr = new StringBuilder("");
		if (relatorioSEIDecidirVO.getRelatorioFinanceiroVO().getSituacaoReceber()) {
			sqlStr.append(", 'AR'");
		}
		if (relatorioSEIDecidirVO.getRelatorioFinanceiroVO().getSituacaoRecebido()) {
			sqlStr.append(", 'RE'");
		}
		if (relatorioSEIDecidirVO.getRelatorioFinanceiroVO().getSituacaoRenegociado()) {
			sqlStr.append(", 'NE'");
		}
		if (relatorioSEIDecidirVO.getRelatorioFinanceiroVO().getSituacaoCancelado()) {
			sqlStr.append(", 'CF'");
		}		

		if (sqlStr.length() > 0) {
			return " and contareceber.situacao in (''" + sqlStr.toString() + ") ";
		}
		return " and contareceber.situacao in ('AR', 'RE', 'NE', 'CF') ";
	}
	
	private String adicionarFiltroSituacaoContaReceberFechamentoMesReceita(RelatorioSEIDecidirVO relatorioSEIDecidirVO) {
		StringBuilder sqlStr = new StringBuilder("");
		if (relatorioSEIDecidirVO.getRelatorioFinanceiroVO().getSituacaoReceber()) {
			sqlStr.append(", '").append(SituacaoContaReceber.A_RECEBER).append("' ");
		}
		if (relatorioSEIDecidirVO.getRelatorioFinanceiroVO().getSituacaoRecebido()) {
			sqlStr.append(", '").append(SituacaoContaReceber.RECEBIDO).append("' ");
		}
		if (relatorioSEIDecidirVO.getRelatorioFinanceiroVO().getSituacaoRenegociado()) {
			sqlStr.append(", '").append(SituacaoContaReceber.NEGOCIADO).append("' ");
		}
		if (relatorioSEIDecidirVO.getRelatorioFinanceiroVO().getSituacaoCancelado()) {
			sqlStr.append(", '").append(SituacaoContaReceber.CANCELADO_FINANCEIRO).append("' ");
		}		

		if (sqlStr.length() > 0) {
			return " and fechamentofinanceiroconta.situacaoContaReceber in (''" + sqlStr.toString() + ") ";
		}
		return "";
	}
	
	private String adicionarFiltroSituacaoContaPagar(RelatorioSEIDecidirVO relatorioSEIDecidirVO) {
		StringBuilder sqlStr = new StringBuilder("");
		if (relatorioSEIDecidirVO.getRelatorioFinanceiroVO().getSituacaoPagar()) {
			sqlStr.append(", 'AP', 'PP'");
		}
		if (relatorioSEIDecidirVO.getRelatorioFinanceiroVO().getSituacaoPago()) {
			sqlStr.append(", 'PA'");
		}
		if (relatorioSEIDecidirVO.getRelatorioFinanceiroVO().getSituacaoRenegociado()) {
			sqlStr.append(", 'NE'");
		}
		if (relatorioSEIDecidirVO.getRelatorioFinanceiroVO().getSituacaoCancelado()) {
			sqlStr.append(", 'CF'");
		}
		
		if (sqlStr.length() > 0) {
			return " and contapagar.situacao in (''" + sqlStr.toString() + ") ";
		}
		return " and contapagar.situacao in ('AP', 'PA', 'PP', 'NE', 'CF') ";
	}
	private String adicionarFiltroSacadoContaReceber(RelatorioSEIDecidirVO relatorioSEIDecidirVO) {
		StringBuilder sql = new StringBuilder("");
		if (relatorioSEIDecidirVO.getTipoPessoaEnum().equals(TipoPessoa.FUNCIONARIO) && Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getFuncionarioVO().getPessoa().getCodigo())) {
			sql.append(" and contareceberpessoa.codigo = ").append(relatorioSEIDecidirVO.getFuncionarioVO().getPessoa().getCodigo()).append(" ");
		}
		if (relatorioSEIDecidirVO.getTipoPessoaEnum().equals(TipoPessoa.PROFESSOR) && Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getPessoaVO().getCodigo())) {
			sql.append(" and contareceberpessoa.codigo = ").append(relatorioSEIDecidirVO.getPessoaVO().getCodigo()).append(" ");
		}
		if (relatorioSEIDecidirVO.getTipoPessoaEnum().equals(TipoPessoa.CANDIDATO) && Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getCandidatosVagasVO().getPessoa().getCodigo())) {
			sql.append(" and contareceberpessoa.codigo = ").append(relatorioSEIDecidirVO.getCandidatosVagasVO().getPessoa().getCodigo()).append(" ");
		}
		if (relatorioSEIDecidirVO.getTipoPessoaEnum().equals(TipoPessoa.RESPONSAVEL_FINANCEIRO) && Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getPessoaVO().getCodigo())) {
			sql.append(" and contareceberresponsavelfinanceiro.codigo = ").append(relatorioSEIDecidirVO.getPessoaVO().getCodigo()).append(" ");
		}
		if (relatorioSEIDecidirVO.getTipoPessoaEnum().equals(TipoPessoa.ALUNO) && Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getMatriculaVO().getAluno().getCodigo())) {
			sql.append(" and ( contareceberpessoa.codigo = ").append(relatorioSEIDecidirVO.getMatriculaVO().getAluno().getCodigo()).append(" ");
			if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.FINANCEIRO_RECEITA)) {				
				sql.append(" or matricula.aluno = ").append(relatorioSEIDecidirVO.getMatriculaVO().getAluno().getCodigo()).append(" ");
		}
			sql.append(" ) ");			
		}
		if (relatorioSEIDecidirVO.getTipoPessoaEnum().equals(TipoPessoa.PARCEIRO) && Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getParceiroVO().getCodigo())) {
			sql.append(" and contareceberparceiro.codigo = ").append(relatorioSEIDecidirVO.getParceiroVO().getCodigo()).append(" ");
		}
		if (relatorioSEIDecidirVO.getTipoPessoaEnum().equals(TipoPessoa.FORNECEDOR) && Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getFornecedorVO().getCodigo())) {
			sql.append(" and contareceberfornecedor.codigo = ").append(relatorioSEIDecidirVO.getFornecedorVO().getCodigo()).append(" ");
		}
		if (relatorioSEIDecidirVO.getTipoPessoaEnum().equals(TipoPessoa.REQUERENTE) && Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getPessoaVO().getCodigo())) {
			sql.append(" and contareceberpessoa.codigo = ").append(relatorioSEIDecidirVO.getPessoaVO().getCodigo()).append(" ");
		}
		if (relatorioSEIDecidirVO.getIsPermiteInformarAlunoJuntoOutroSacado() && relatorioSEIDecidirVO.getMatriculaVO() != null
				&& Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getMatriculaVO().getMatricula())
				&& Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getMatriculaVO().getAluno())) {
			if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.FINANCEIRO_RECEITA)) {
				if(!relatorioSEIDecidirVO.getConsiderarTodasMatriculasAluno()) {
			sql.append(" and matricula.matricula = '").append(relatorioSEIDecidirVO.getMatriculaVO().getMatricula()).append("' ");
		}
			}else {
				sql.append(" and matricula.matricula = '").append(relatorioSEIDecidirVO.getMatriculaVO().getMatricula()).append("' ");
			}
		}
		return sql.toString();
	}
	
	private String adicionarFiltroSacadoFechamentoMesReceita(RelatorioSEIDecidirVO relatorioSEIDecidirVO) {
		StringBuilder sql = new StringBuilder("");
		if (relatorioSEIDecidirVO.getTipoPessoaEnum().equals(TipoPessoa.FUNCIONARIO) && Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getFuncionarioVO().getPessoa().getCodigo())) {
			sql.append(" and fechamentofinanceiroconta.pessoa = ").append(relatorioSEIDecidirVO.getFuncionarioVO().getPessoa().getCodigo()).append(" ");
			sql.append(" and fechamentofinanceiroconta.tipoPessoa = '").append(TipoPessoa.FUNCIONARIO).append("' ");
		}
		if (relatorioSEIDecidirVO.getTipoPessoaEnum().equals(TipoPessoa.PROFESSOR) && Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getPessoaVO().getCodigo())) {
			sql.append(" and fechamentofinanceiroconta.pessoa = ").append(relatorioSEIDecidirVO.getPessoaVO().getCodigo()).append(" ");
			sql.append(" and fechamentofinanceiroconta.tipoPessoa = '").append(TipoPessoa.PROFESSOR).append("' ");
		}
		if (relatorioSEIDecidirVO.getTipoPessoaEnum().equals(TipoPessoa.CANDIDATO) && Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getCandidatosVagasVO().getPessoa().getCodigo())) {
			sql.append(" and fechamentofinanceiroconta.pessoa = ").append(relatorioSEIDecidirVO.getCandidatosVagasVO().getPessoa().getCodigo()).append(" ");
			sql.append(" and fechamentofinanceiroconta.tipoPessoa = '").append(TipoPessoa.CANDIDATO).append("' ");
		}
		if (relatorioSEIDecidirVO.getTipoPessoaEnum().equals(TipoPessoa.RESPONSAVEL_FINANCEIRO) && Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getPessoaVO().getCodigo())) {
			sql.append(" and fechamentofinanceiroconta.pessoa = ").append(relatorioSEIDecidirVO.getPessoaVO().getCodigo()).append(" ");
			sql.append(" and fechamentofinanceiroconta.tipoPessoa = '").append(TipoPessoa.RESPONSAVEL_FINANCEIRO).append("' ");
		}
		if (relatorioSEIDecidirVO.getTipoPessoaEnum().equals(TipoPessoa.ALUNO) && Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getMatriculaVO().getAluno().getCodigo())) {
			sql.append(" and fechamentofinanceiroconta.pessoa = ").append(relatorioSEIDecidirVO.getMatriculaVO().getAluno().getCodigo()).append(" ");
			sql.append(" and fechamentofinanceiroconta.tipoPessoa = '").append(TipoPessoa.ALUNO).append("' ");
		}
		if (relatorioSEIDecidirVO.getTipoPessoaEnum().equals(TipoPessoa.PARCEIRO) && Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getParceiroVO().getCodigo())) {
			sql.append(" and fechamentofinanceiroconta.parceiro = ").append(relatorioSEIDecidirVO.getParceiroVO().getCodigo()).append(" ");
			sql.append(" and fechamentofinanceiroconta.tipoPessoa = '").append(TipoPessoa.PARCEIRO).append("' ");
		}
		if (relatorioSEIDecidirVO.getTipoPessoaEnum().equals(TipoPessoa.FORNECEDOR) && Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getFornecedorVO().getCodigo())) {
			sql.append(" and fechamentofinanceiroconta.fornecedor = ").append(relatorioSEIDecidirVO.getFornecedorVO().getCodigo()).append(" ");
			sql.append(" and fechamentofinanceiroconta.tipoPessoa = '").append(TipoPessoa.FORNECEDOR).append("' ");
		}
		if (relatorioSEIDecidirVO.getTipoPessoaEnum().equals(TipoPessoa.REQUERENTE) && Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getPessoaVO().getCodigo())) {
			sql.append(" and fechamentofinanceiroconta.pessoa = ").append(relatorioSEIDecidirVO.getPessoaVO().getCodigo()).append(" ");
			sql.append(" and fechamentofinanceiroconta.tipoPessoa = '").append(TipoPessoa.REQUERENTE).append("' ");
		}
		if (relatorioSEIDecidirVO.getIsPermiteInformarAlunoJuntoOutroSacado() && relatorioSEIDecidirVO.getMatriculaVO() != null
				&& Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getMatriculaVO().getMatricula())
				&& Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getMatriculaVO().getAluno())) {
			sql.append(" and fechamentofinanceiroconta.matricula = '").append(relatorioSEIDecidirVO.getMatriculaVO().getMatricula()).append("' ");
		}
		return sql.toString();
	}
	
	private String adicionarFiltroSacadoContaPagar(RelatorioSEIDecidirVO relatorioSEIDecidirVO) {
		StringBuilder sql = new StringBuilder("");
		if (relatorioSEIDecidirVO.getTipoSacadoEnum().equals(TipoSacado.FUNCIONARIO_PROFESSOR) && Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getFuncionarioVO().getPessoa().getCodigo())) {
			sql.append(" and contapagarfuncionariopessoa.codigo = ").append(relatorioSEIDecidirVO.getFuncionarioVO().getPessoa().getCodigo()).append(" ");
		}
		if (relatorioSEIDecidirVO.getTipoSacadoEnum().equals(TipoSacado.BANCO) && Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getBancoVO().getCodigo())) {
			sql.append(" and contapagarbanco.codigo = ").append(relatorioSEIDecidirVO.getBancoVO().getCodigo()).append(" ");
		}
		if (relatorioSEIDecidirVO.getTipoSacadoEnum().equals(TipoSacado.ALUNO) && Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getMatriculaVO().getAluno().getCodigo())) {
			sql.append(" and contapagarpessoa.codigo = ").append(relatorioSEIDecidirVO.getMatriculaVO().getAluno().getCodigo()).append(" ");
		}
		if (relatorioSEIDecidirVO.getTipoSacadoEnum().equals(TipoSacado.PARCEIRO) && Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getParceiroVO().getCodigo())) {
			sql.append(" and contapagarparceiro.codigo = ").append(relatorioSEIDecidirVO.getParceiroVO().getCodigo()).append(" ");
		}
		if (relatorioSEIDecidirVO.getTipoSacadoEnum().equals(TipoSacado.FORNECEDOR) && Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getFornecedorVO().getCodigo())) {
			sql.append(" and contapagarfornecedor.codigo = ").append(relatorioSEIDecidirVO.getFornecedorVO().getCodigo()).append(" ");
		}
		if (relatorioSEIDecidirVO.getTipoSacadoEnum().equals(TipoSacado.OPERADORA_CARTAO) && Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getOperadoraCartaoVO().getCodigo())) {
			sql.append(" and contapagaroperadoracartao.codigo = ").append(relatorioSEIDecidirVO.getOperadoraCartaoVO().getCodigo()).append(" ");
		}
		if (relatorioSEIDecidirVO.getTipoSacadoEnum().equals(TipoSacado.RESPONSAVEL_FINANCEIRO) && Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getPessoaVO().getCodigo())) {
			sql.append(" and contapagarresponsavelfinanceiro.codigo = ").append(relatorioSEIDecidirVO.getPessoaVO().getCodigo()).append(" ");
		}
		return sql.toString();
	}

	private String adicionarFiltroCurso(RelatorioSEIDecidirVO relatorioSEIDecidirVO) {
		if (!relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoCurso()) {
			return "";
		}
		StringBuilder sql = new StringBuilder("");
		for (CursoVO cursoVO : relatorioSEIDecidirVO.getCursoVOs()) {
			if (cursoVO.getFiltrarCursoVO()) {
				sql.append(sql.length() == 0 ? " and curso.codigo in(" : ", ").append(cursoVO.getCodigo());
			}
		}
		if (sql.length() > 0) {
			sql.append(") ");
		}
		if (Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getNivelEducacional()) && !relatorioSEIDecidirVO.getNivelEducacional().equals("0")) {
			sql.append(" and curso.niveleducacional = '").append(relatorioSEIDecidirVO.getNivelEducacional()).append("' ");
		}
		return sql.toString();
	}
	
	private String adicionarFiltroTurno(RelatorioSEIDecidirVO relatorioSEIDecidirVO) {
		if (!relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoTurno()) {
			return "";
		}
		StringBuilder sql = new StringBuilder("");
		for (TurnoVO turno : relatorioSEIDecidirVO.getTurnoVOs()) {
			if (turno.getFiltrarTurnoVO()) {
				sql.append(sql.length() == 0 ? " and turno.codigo in(" : ", ").append(turno.getCodigo());
			}
		}
		if (sql.length() > 0) {
			sql.append(") ");
		}
		return sql.toString();
	}

	private String adicionarFiltroCentroReceita(RelatorioSEIDecidirVO relatorioSEIDecidirVO) {
		StringBuilder sql = new StringBuilder("");
		for (CentroReceitaVO centroReceitaVO : relatorioSEIDecidirVO.getCentroReceitaVOs()) {
			if (centroReceitaVO.getFiltrarCentroReceitaVO()) {
				if(sql.length() == 0) {
					sql.append(" and exists ( select centroresultadoorigem.codigo from centroresultadoorigem ");					
					sql.append(" where tipoCentroResultadoOrigem = '").append(TipoCentroResultadoOrigemEnum.CONTA_RECEBER.name()).append("' ");
					sql.append(" and centroresultadoorigem.codOrigem = contareceber.codigo::varchar  and centroresultadoorigem.centroReceita in ( ");
					sql.append(centroReceitaVO.getCodigo());
				}else {
					sql.append(", ").append(centroReceitaVO.getCodigo());
				}	
			}
		}
		if (sql.length() > 0) {
			sql.append(") limit 1) ");
		}
		return sql.toString();
	}
	
	private String adicionarFiltroCentroReceitaFechamentoMesReceita(RelatorioSEIDecidirVO relatorioSEIDecidirVO) {
		StringBuilder sql = new StringBuilder("");
		for (CentroReceitaVO centroReceitaVO : relatorioSEIDecidirVO.getCentroReceitaVOs()) {
			if (centroReceitaVO.getFiltrarCentroReceitaVO()) {
				if(sql.length() == 0) {
					sql.append(" and exists ( select centroreceita.codigo from centroreceita ");					
					sql.append(" where centroreceita.codigo = fechamentofinanceirocentroresultado.centroreceita ");
					sql.append(" and centroreceita.codigo in (");
					sql.append(centroReceitaVO.getCodigo());
				}else {
					sql.append(", ").append(centroReceitaVO.getCodigo());
				}	
			}
		}
		if (sql.length() > 0) {
			sql.append(") limit 1) ");
		}
		return sql.toString();
	}
	
	private String adicionarFiltroContaCorrenteFechamentoMesReceita(RelatorioSEIDecidirVO relatorioSEIDecidirVO) {
		StringBuilder sql = new StringBuilder("");
		for (CentroReceitaVO centroReceitaVO : relatorioSEIDecidirVO.getCentroReceitaVOs()) {
			if (centroReceitaVO.getFiltrarCentroReceitaVO()) {
				if(sql.length() == 0) {
					sql.append(" and exists ( select contacorrente.codigo from contacorrente ");					
					sql.append(" where contacorrente.codigo = fechamentofinanceiroformapagamento.contacorrente ");
					sql.append(" and contacorrente.codigo in (");
					sql.append(centroReceitaVO.getCodigo());
				}else {
					sql.append(", ").append(centroReceitaVO.getCodigo());
				}	
			}
		}
		if (sql.length() > 0) {
			sql.append(") limit 1) ");
		}
		return sql.toString();
	}

	private String adicionarFiltroCategoriaDespesa(RelatorioSEIDecidirVO relatorioSEIDecidirVO) {
		StringBuilder sql = new StringBuilder("");		
		for (CategoriaDespesaVO categoriaDespesaVO : relatorioSEIDecidirVO.getCategoriaDespesaVOs()) {
			if (categoriaDespesaVO.getSelecionar()) {
				if(sql.length() == 0) {
					sql.append(" and exists ( select centroresultadoorigem.codigo from centroresultadoorigem ");					
					sql.append(" where tipoCentroResultadoOrigem = '").append(TipoCentroResultadoOrigemEnum.CONTA_PAGAR.name()).append("' ");
					sql.append(" and centroresultadoorigem.codOrigem = contapagar.codigo::varchar  and centroresultadoorigem.categoriadespesa in ( ");
					sql.append(categoriaDespesaVO.getCodigo());
				}else {
					sql.append(", ").append(categoriaDespesaVO.getCodigo());
				}				
			}
		}
		if (sql.length() > 0) {
			sql.append(") limit 1) ");
		}
		return sql.toString();
	}

	private String adicionarFiltroUnidadeEnsino(RelatorioSEIDecidirVO relatorioSEIDecidirVO) {
		if (!relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoUnidadeEnsino()) {
			return "";
		}
		StringBuilder sql = new StringBuilder("");
		sql.append(" and unidadeEnsino.codigo in (0");
		for (UnidadeEnsinoVO unidadeEnsinoVO : relatorioSEIDecidirVO.getUnidadeEnsinoVOs()) {
			if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
				sql.append(", ").append(unidadeEnsinoVO.getCodigo());
			}
		}
		sql.append(" ) ");
		return sql.toString();
	}
	

	
	private String adicionarFiltroCentroResultado(RelatorioSEIDecidirVO relatorioSEIDecidirVO) {
		StringBuilder sql = new StringBuilder("");
		if(Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getListaCentroResultadoVO())) {
			sql.append(" and centroResultado.codigo in (0");
			for (CentroResultadoVO obj : relatorioSEIDecidirVO.getListaCentroResultadoVO()) {
				if (obj.getFiltrarCentroResultado()) {
					sql.append(", ").append(obj.getCodigo());
				}
			}
			sql.append(" ) ");
		}
		return sql.toString();
	}
	
	private String adicionarFiltroFormaPagamentoFechamentoMesReceita(RelatorioSEIDecidirVO relatorioSEIDecidirVO, String campoFiltrar) {
		StringBuilder sql = new StringBuilder("");
		boolean possuiFiltro = false;
		sql.append(" and ").append(campoFiltrar).append(" in (0");
		for (FormaPagamentoVO formaPagamentoVO : relatorioSEIDecidirVO.getFormaPagamentoVOs()) {
			if (formaPagamentoVO.getFiltrarFormaPagamento()) {
				sql.append(", ").append(formaPagamentoVO.getCodigo());
				possuiFiltro = true;
			}
		}
		sql.append(" ) ");
		if (!possuiFiltro) {
			return "";
		}
		return sql.toString();
	}

	private String adicionarFiltroPeriodo(RelatorioSEIDecidirVO relatorioSEIDecidirVO) {
		if (!relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoFiltroPeriodo()) {
			return "";
		}
		StringBuilder sql = new StringBuilder("");
		boolean entrouCondicao = false;
		StringBuilder sqlStr = new StringBuilder();
		String andOr = "";
		switch (relatorioSEIDecidirVO.getTipoFiltroPeriodo()) {
		case ANO_SEMESTRE:
			if (Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getAno())) {
				sql.append(" and matriculaperiodo.ano = '").append(relatorioSEIDecidirVO.getAno()).append("' ");
			}
			if (Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getSemestre())) {
				sql.append(" and matriculaperiodo.semestre = '").append(relatorioSEIDecidirVO.getSemestre()).append("' ");
			}
			break;
		case DATA_COMPETENCIA:
			if(relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.FINANCEIRO_RECEITA)){
				sql.append(" and ").append(realizarGeracaoWherePeriodoConsiderandoMesAno(relatorioSEIDecidirVO.getDataInicio(), relatorioSEIDecidirVO.getDataFim(), "contareceber.datacompetencia"));
			}else if(relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.FINANCEIRO_FECHAMENTO_MES_RECEITA)){
					sql.append(" and ").append(realizarGeracaoWherePeriodoConsiderandoMesAno(relatorioSEIDecidirVO.getDataInicio(), relatorioSEIDecidirVO.getDataFim(), "fechamentofinanceiroconta.datacompetencia"));
			}else{
				sql.append(" and ").append(realizarGeracaoWherePeriodoConsiderandoMesAno(relatorioSEIDecidirVO.getDataInicio(), relatorioSEIDecidirVO.getDataFim(), "contapagar.datafatogerador"));
			}
			break;
		case DATA_MATRICULA:
			sql.append(" and ").append(realizarGeracaoWherePeriodo(relatorioSEIDecidirVO.getDataInicio(), relatorioSEIDecidirVO.getDataFim(), "matriculaperiodo.data", false));
			break;
		case DATA_MATRICULA_E_ANO_SEMESTRE:
			sql.append(" and ").append(realizarGeracaoWherePeriodo(relatorioSEIDecidirVO.getDataInicio(), relatorioSEIDecidirVO.getDataFim(), "matriculaperiodo.data", false));
			if (Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getAno())) {
				sql.append(" and matriculaperiodo.ano = '").append(relatorioSEIDecidirVO.getAno()).append("' ");				
			}
			if (Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getSemestre())) {
				sql.append(" and matriculaperiodo.semestre = '").append(relatorioSEIDecidirVO.getSemestre()).append("' ");
			}
			break;
		case DATA_RECEBIMENTO:
			if(relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.FINANCEIRO_FECHAMENTO_MES_RECEITA)){
				 sql.append(" and ").append(realizarGeracaoWherePeriodo(relatorioSEIDecidirVO.getDataInicio(), relatorioSEIDecidirVO.getDataFim(), "fechamentofinanceiroconta.datarecebimento", false));
			 }else {
				 sql.append(" and ").append(realizarGeracaoWherePeriodo(relatorioSEIDecidirVO.getDataInicio(), relatorioSEIDecidirVO.getDataFim(), "negociacaorecebimento.data", false));
			 }
			break;
		case DATA_RECEBIMENTO_E_DATA_COMPETENCIA:
			entrouCondicao = false;
			sqlStr = new StringBuilder();
			sqlStr.append(" and (");		
			if(relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.FINANCEIRO_FECHAMENTO_MES_RECEITA)){
				if(relatorioSEIDecidirVO.getRelatorioFinanceiroVO().getSituacaoReceber() || relatorioSEIDecidirVO.getRelatorioFinanceiroVO().getSituacaoRenegociado() || relatorioSEIDecidirVO.getRelatorioFinanceiroVO().getSituacaoCancelado()){				
					sqlStr.append(" (fechamentofinanceiroconta.situacaoContaReceber  != '").append(SituacaoContaReceber.RECEBIDO.name()).append("' and ").append(realizarGeracaoWherePeriodoConsiderandoMesAno(relatorioSEIDecidirVO.getDataInicio2(), relatorioSEIDecidirVO.getDataFim2(), "fechamentofinanceiroconta.datacompetencia")).append(") ") ;
					andOr = " or ";
					entrouCondicao = true;
				}
				if(relatorioSEIDecidirVO.getRelatorioFinanceiroVO().getSituacaoRecebido()){
					sqlStr.append(andOr).append(" (fechamentofinanceiroconta.situacaoContaReceber = '").append(SituacaoContaReceber.RECEBIDO.name()).append("' and ").append(realizarGeracaoWherePeriodo(relatorioSEIDecidirVO.getDataInicio(), relatorioSEIDecidirVO.getDataFim(), "fechamentofinanceiroconta.datarecebimento", false));
					sqlStr.append(" and ").append(realizarGeracaoWherePeriodoConsiderandoMesAno(relatorioSEIDecidirVO.getDataInicio2(), relatorioSEIDecidirVO.getDataFim2(), "fechamentofinanceiroconta.datacompetencia")).append(") ");
					entrouCondicao = true;
				}			
			}else { 
				if(relatorioSEIDecidirVO.getRelatorioFinanceiroVO().getSituacaoReceber() || relatorioSEIDecidirVO.getRelatorioFinanceiroVO().getSituacaoRenegociado() || relatorioSEIDecidirVO.getRelatorioFinanceiroVO().getSituacaoCancelado()){				
					sqlStr.append(" (contareceber.situacao  != 'RE' and ").append(realizarGeracaoWherePeriodoConsiderandoMesAno(relatorioSEIDecidirVO.getDataInicio2(), relatorioSEIDecidirVO.getDataFim2(), "contareceber.datacompetencia")).append(") ") ;
					andOr = " or ";
					entrouCondicao = true;
				}
				if(relatorioSEIDecidirVO.getRelatorioFinanceiroVO().getSituacaoRecebido()){
					sqlStr.append(andOr).append(" (contareceber.situacao = 'RE' and ").append(realizarGeracaoWherePeriodo(relatorioSEIDecidirVO.getDataInicio(), relatorioSEIDecidirVO.getDataFim(), "negociacaorecebimento.data", false));
					sqlStr.append(" and ").append(realizarGeracaoWherePeriodoConsiderandoMesAno(relatorioSEIDecidirVO.getDataInicio2(), relatorioSEIDecidirVO.getDataFim2(), "contareceber.datacompetencia")).append(") ");
					entrouCondicao = true;
				}		
			}
			sqlStr.append(" ) ");
			if (entrouCondicao) {
				sql.append(sqlStr);
			}
			break;
		case DATA_VENCIMENTO:			
			if(relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.FINANCEIRO_RECEITA)){
				sql.append(" and ").append(realizarGeracaoWherePeriodo(relatorioSEIDecidirVO.getDataInicio(), relatorioSEIDecidirVO.getDataFim(), "contareceber.datavencimento", false));				
			} else if(relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.FINANCEIRO_FECHAMENTO_MES_RECEITA)){
					sql.append(" and ").append(realizarGeracaoWherePeriodo(relatorioSEIDecidirVO.getDataInicio(), relatorioSEIDecidirVO.getDataFim(), "fechamentofinanceiroconta.datavencimento", false));				
			}else if (!relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.CRM)) {
				sql.append(" and ").append(realizarGeracaoWherePeriodo(relatorioSEIDecidirVO.getDataInicio(), relatorioSEIDecidirVO.getDataFim(), "contapagar.datavencimento", false));
			}
			break;
		case DATA_VENCIMENTO_E_DATA_COMPETENCIA:
			if(relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.FINANCEIRO_RECEITA)){
				sql.append(" and ").append(realizarGeracaoWherePeriodo(relatorioSEIDecidirVO.getDataInicio(), relatorioSEIDecidirVO.getDataFim(), "contareceber.datavencimento", false));
				sql.append(" and ").append(realizarGeracaoWherePeriodoConsiderandoMesAno(relatorioSEIDecidirVO.getDataInicio2(), relatorioSEIDecidirVO.getDataFim2(), "contareceber.datacompetencia"));
			}else if(relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.FINANCEIRO_FECHAMENTO_MES_RECEITA)){
					sql.append(" and ").append(realizarGeracaoWherePeriodo(relatorioSEIDecidirVO.getDataInicio(), relatorioSEIDecidirVO.getDataFim(), "fechamentofinanceiroconta.datavencimento", false));
					sql.append(" and ").append(realizarGeracaoWherePeriodoConsiderandoMesAno(relatorioSEIDecidirVO.getDataInicio2(), relatorioSEIDecidirVO.getDataFim2(), "fechamentofinanceiroconta.datacompetencia"));
			}else{
				sql.append(" and ").append(realizarGeracaoWherePeriodo(relatorioSEIDecidirVO.getDataInicio(), relatorioSEIDecidirVO.getDataFim(), "contapagar.datavencimento", false));
				sql.append(" and ").append(realizarGeracaoWherePeriodoConsiderandoMesAno(relatorioSEIDecidirVO.getDataInicio2(), relatorioSEIDecidirVO.getDataFim2(), "contapagar.datafatogerador"));
			}
			break;
		case DATA_VENCIMENTO_E_DATA_RECEBIMENTO:
			entrouCondicao = false;
			sqlStr = new StringBuilder();
			sqlStr.append(" and (");
			if(relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.FINANCEIRO_FECHAMENTO_MES_RECEITA)){
				if(relatorioSEIDecidirVO.getRelatorioFinanceiroVO().getSituacaoReceber() || relatorioSEIDecidirVO.getRelatorioFinanceiroVO().getSituacaoRenegociado() || relatorioSEIDecidirVO.getRelatorioFinanceiroVO().getSituacaoCancelado()){
					sqlStr.append(" (fechamentofinanceiroconta.situacaoContaReceber  != '").append(SituacaoContaReceber.RECEBIDO.name()).append("' and ").append(realizarGeracaoWherePeriodo(relatorioSEIDecidirVO.getDataInicio(), relatorioSEIDecidirVO.getDataFim(), "fechamentofinanceiroconta.datavencimento", false)).append(")");
					andOr = " or ";
					entrouCondicao = true;
				}
				if(relatorioSEIDecidirVO.getRelatorioFinanceiroVO().getSituacaoRecebido()){
					sqlStr.append(andOr).append(" (fechamentofinanceiroconta.situacaoContaReceber = '").append(SituacaoContaReceber.RECEBIDO.name()).append("' and ").append(realizarGeracaoWherePeriodo(relatorioSEIDecidirVO.getDataInicio(), relatorioSEIDecidirVO.getDataFim(), "fechamentofinanceiroconta.datavencimento", false));
					sqlStr.append(" and ").append(realizarGeracaoWherePeriodo(relatorioSEIDecidirVO.getDataInicio2(), relatorioSEIDecidirVO.getDataFim2(), "fechamentofinanceiroconta.datarecebimento", false)).append(") ");
					entrouCondicao = true;
				}
				
			}else {
			if(relatorioSEIDecidirVO.getRelatorioFinanceiroVO().getSituacaoReceber() || relatorioSEIDecidirVO.getRelatorioFinanceiroVO().getSituacaoRenegociado() || relatorioSEIDecidirVO.getRelatorioFinanceiroVO().getSituacaoCancelado()){
				sqlStr.append(" (contareceber.situacao != 'RE' and ").append(realizarGeracaoWherePeriodo(relatorioSEIDecidirVO.getDataInicio(), relatorioSEIDecidirVO.getDataFim(), "contareceber.datavencimento", false)).append(")");
				andOr = " or ";
				entrouCondicao = true;
			}
			if(relatorioSEIDecidirVO.getRelatorioFinanceiroVO().getSituacaoRecebido()){
				sqlStr.append(andOr).append(" (contareceber.situacao = 'RE' and ").append(realizarGeracaoWherePeriodo(relatorioSEIDecidirVO.getDataInicio(), relatorioSEIDecidirVO.getDataFim(), "contareceber.datavencimento", false));
				sqlStr.append(" and ").append(realizarGeracaoWherePeriodo(relatorioSEIDecidirVO.getDataInicio2(), relatorioSEIDecidirVO.getDataFim2(), "negociacaorecebimento.data", false)).append(") ");
				entrouCondicao = true;
			}
			}
			sqlStr.append(" ) ");
			if (entrouCondicao) {
				sql.append(sqlStr);
			}
			break;
		case DATA_VENCIMENTO_E_DATA_PAGAMENTO:
			entrouCondicao = false;
			sqlStr = new StringBuilder();
			sqlStr.append(" and (");
			if(relatorioSEIDecidirVO.getRelatorioFinanceiroVO().getSituacaoPagar()){
				sqlStr.append(" (contapagar.situacao != 'PA' and ").append(realizarGeracaoWherePeriodo(relatorioSEIDecidirVO.getDataInicio(), relatorioSEIDecidirVO.getDataFim(), "contapagar.datavencimento", false)).append(") ");
				andOr = " or ";
				entrouCondicao = true;
			}
			if(relatorioSEIDecidirVO.getRelatorioFinanceiroVO().getSituacaoPago()){				
				sqlStr.append(andOr).append(" (contapagar.situacao = 'PA' and ").append(realizarGeracaoWherePeriodo(relatorioSEIDecidirVO.getDataInicio(), relatorioSEIDecidirVO.getDataFim(), "contapagar.datavencimento", false));
				sqlStr.append(" and ").append(realizarGeracaoWherePeriodo(relatorioSEIDecidirVO.getDataInicio2(), relatorioSEIDecidirVO.getDataFim2(), "negociacaopagamento.data", false)).append(") ");
				entrouCondicao = true;
			}
			sqlStr.append(" ) ");
			if (entrouCondicao) {
				sql.append(sqlStr);
			}
			
			break;
		case DATA_PAGAMENTO_E_DATA_COMPETENCIA:
			entrouCondicao = false;
			sqlStr = new StringBuilder();
			sqlStr.append(" and (");
			if(relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltrosFechamentoMesReceita()){
				if(relatorioSEIDecidirVO.getRelatorioFinanceiroVO().getSituacaoPagar()){
					sqlStr.append(" (fechamentofinanceiroconta.situacaocontapagar != '").append(SituacaoFinanceira.PAGO.name()).append("' and ").append(realizarGeracaoWherePeriodo(relatorioSEIDecidirVO.getDataInicio(), relatorioSEIDecidirVO.getDataFim(), "fechamentofinanceiroconta.datacompetencia", false)).append(") ");
					andOr = " or ";
					entrouCondicao = true;
				}
				if(relatorioSEIDecidirVO.getRelatorioFinanceiroVO().getSituacaoPago()){
					sqlStr.append(andOr).append(" (fechamentofinanceiroconta.situacaocontapagar = '").append(SituacaoFinanceira.PAGO.name()).append("' and ").append(realizarGeracaoWherePeriodoConsiderandoMesAno(relatorioSEIDecidirVO.getDataInicio(), relatorioSEIDecidirVO.getDataFim(), "fechamentofinanceiroconta.datacompetencia"));
					sqlStr.append(" and ").append(realizarGeracaoWherePeriodo(relatorioSEIDecidirVO.getDataInicio2(), relatorioSEIDecidirVO.getDataFim2(), "fechamentofinanceiroconta.datapagamento", false)).append(") ");
					entrouCondicao = true;
				}	
			}else {
				if(relatorioSEIDecidirVO.getRelatorioFinanceiroVO().getSituacaoPagar()){
					sqlStr.append(" (contapagar.situacao != 'PA' and ").append(realizarGeracaoWherePeriodo(relatorioSEIDecidirVO.getDataInicio(), relatorioSEIDecidirVO.getDataFim(), "contapagar.datafatogerador", false)).append(") ");
					andOr = " or ";
					entrouCondicao = true;
				}
				if(relatorioSEIDecidirVO.getRelatorioFinanceiroVO().getSituacaoPago()){
					sqlStr.append(andOr).append(" (contapagar.situacao = 'PA' and ").append(realizarGeracaoWherePeriodoConsiderandoMesAno(relatorioSEIDecidirVO.getDataInicio(), relatorioSEIDecidirVO.getDataFim(), "contapagar.datafatogerador"));
					sqlStr.append(" and ").append(realizarGeracaoWherePeriodo(relatorioSEIDecidirVO.getDataInicio2(), relatorioSEIDecidirVO.getDataFim2(), "negociacaopagamento.data", false)).append(") ");
					entrouCondicao = true;
				}
			}
			sqlStr.append(" ) ");
			if (entrouCondicao) {
				sql.append(sqlStr);
			}
			break;
		case DATA_PAGAMENTO:		
			if(relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarBotaoLiberarBloqueioFechamentoMes()){
				sql.append(" and ").append(realizarGeracaoWherePeriodo(relatorioSEIDecidirVO.getDataInicio(), relatorioSEIDecidirVO.getDataFim(), "fechamentofinanceiroconta.datapagamento", false));
			}else {
				sql.append(" and ").append(realizarGeracaoWherePeriodo(relatorioSEIDecidirVO.getDataInicio(), relatorioSEIDecidirVO.getDataFim(), "negociacaopagamento.data", false));
			}
			break;
		case DATA_COMPENSACAO:					
			sql.append(" and ").append(realizarGeracaoWherePeriodoDataCompensacao(relatorioSEIDecidirVO, relatorioSEIDecidirVO.getDataInicio(), relatorioSEIDecidirVO.getDataFim()));			
			break;
		case DATA_VENCIMENTO_E_DATA_COMPENSACAO:
			entrouCondicao = false;
			sqlStr = new StringBuilder();
			sqlStr.append(" and (");
			if(relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.FINANCEIRO_RECEITA)){
				if(relatorioSEIDecidirVO.getRelatorioFinanceiroVO().getSituacaoReceber() || relatorioSEIDecidirVO.getRelatorioFinanceiroVO().getSituacaoRenegociado() || relatorioSEIDecidirVO.getRelatorioFinanceiroVO().getSituacaoCancelado()){
					sqlStr.append(" (contareceber.situacao != 'RE' and ").append(realizarGeracaoWherePeriodo(relatorioSEIDecidirVO.getDataInicio(), relatorioSEIDecidirVO.getDataFim(), "contareceber.datavencimento", false)).append(") ");
					andOr = " or ";
					entrouCondicao = true;
				}
				if(relatorioSEIDecidirVO.getRelatorioFinanceiroVO().getSituacaoRecebido()){
					sqlStr.append(andOr).append(" (contareceber.situacao = 'RE' and ").append(realizarGeracaoWherePeriodo(relatorioSEIDecidirVO.getDataInicio(), relatorioSEIDecidirVO.getDataFim(), "contareceber.datavencimento", false));
					sqlStr.append(" and ").append(realizarGeracaoWherePeriodoDataCompensacao(relatorioSEIDecidirVO, relatorioSEIDecidirVO.getDataInicio2(), relatorioSEIDecidirVO.getDataFim2())).append(") ");
					entrouCondicao = true;
				}
			}else if(relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.FINANCEIRO_FECHAMENTO_MES_RECEITA)){
					if(relatorioSEIDecidirVO.getRelatorioFinanceiroVO().getSituacaoReceber() || relatorioSEIDecidirVO.getRelatorioFinanceiroVO().getSituacaoRenegociado() || relatorioSEIDecidirVO.getRelatorioFinanceiroVO().getSituacaoCancelado()){
						sqlStr.append(" (fechamentofinanceiroconta.situacaocontareceber != '").append(SituacaoContaReceber.RECEBIDO.name()).append("' and ").append(realizarGeracaoWherePeriodo(relatorioSEIDecidirVO.getDataInicio(), relatorioSEIDecidirVO.getDataFim(), "fechamentofinanceiroconta.datavencimento", false)).append(") ");
						andOr = " or ";
						entrouCondicao = true;
					}
					if(relatorioSEIDecidirVO.getRelatorioFinanceiroVO().getSituacaoRecebido()){
						sqlStr.append(andOr).append(" (fechamentofinanceiroconta.situacaocontareceber = '").append(SituacaoContaReceber.RECEBIDO.name()).append("' and ").append(realizarGeracaoWherePeriodo(relatorioSEIDecidirVO.getDataInicio(), relatorioSEIDecidirVO.getDataFim(), "fechamentofinanceiroconta.datavencimento", false));
						sqlStr.append(" and ").append(realizarGeracaoWherePeriodoDataCompensacao(relatorioSEIDecidirVO, relatorioSEIDecidirVO.getDataInicio2(), relatorioSEIDecidirVO.getDataFim2())).append(") ");
						entrouCondicao = true;
					}
			}else{
				if(relatorioSEIDecidirVO.getRelatorioFinanceiroVO().getSituacaoPagar()){
					sqlStr.append(" (contapagar.situacao != 'PA' and ").append(realizarGeracaoWherePeriodo(relatorioSEIDecidirVO.getDataInicio(), relatorioSEIDecidirVO.getDataFim(), "contapagar.datavencimento", false)).append(") ");
					andOr = " or ";
					entrouCondicao = true;
				}
				if(relatorioSEIDecidirVO.getRelatorioFinanceiroVO().getSituacaoPago()){
					sqlStr.append(andOr).append(" (contapagar.situacao = 'PA' and ").append(realizarGeracaoWherePeriodo(relatorioSEIDecidirVO.getDataInicio(), relatorioSEIDecidirVO.getDataFim(), "contapagar.datavencimento", false));
					sqlStr.append(" and ").append(realizarGeracaoWherePeriodoDataCompensacao(relatorioSEIDecidirVO, relatorioSEIDecidirVO.getDataInicio2(), relatorioSEIDecidirVO.getDataFim2())).append(") ");
					entrouCondicao = true;
				}
			}
			sqlStr.append(" ) ");
			if (entrouCondicao) {
				sql.append(sqlStr);
			}
			break;
		case DATA_COMPETENCIA_DATA_COMPENSACAO:
			entrouCondicao = false;
			sqlStr = new StringBuilder();
			sqlStr.append(" and (");
			if(relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.FINANCEIRO_RECEITA)){
				if(relatorioSEIDecidirVO.getRelatorioFinanceiroVO().getSituacaoReceber() || relatorioSEIDecidirVO.getRelatorioFinanceiroVO().getSituacaoRenegociado() || relatorioSEIDecidirVO.getRelatorioFinanceiroVO().getSituacaoCancelado()){
					sqlStr.append(" (contareceber.situacao != 'RE' and ").append(realizarGeracaoWherePeriodo(relatorioSEIDecidirVO.getDataInicio(), relatorioSEIDecidirVO.getDataFim(), "contareceber.datacompetencia", false)).append(") ");
					andOr = " or ";
				}
				if(relatorioSEIDecidirVO.getRelatorioFinanceiroVO().getSituacaoRecebido()){
					sqlStr.append(andOr).append(" (contareceber.situacao = 'RE' and ").append(realizarGeracaoWherePeriodo(relatorioSEIDecidirVO.getDataInicio(), relatorioSEIDecidirVO.getDataFim(), "contareceber.datacompetencia", false));
					sqlStr.append(" and ").append(realizarGeracaoWherePeriodoDataCompensacao(relatorioSEIDecidirVO, relatorioSEIDecidirVO.getDataInicio2(), relatorioSEIDecidirVO.getDataFim2())).append(") ");
				}
			}else if(relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.FINANCEIRO_FECHAMENTO_MES_RECEITA)){
					if(relatorioSEIDecidirVO.getRelatorioFinanceiroVO().getSituacaoReceber() || relatorioSEIDecidirVO.getRelatorioFinanceiroVO().getSituacaoRenegociado() || relatorioSEIDecidirVO.getRelatorioFinanceiroVO().getSituacaoCancelado()){
						sqlStr.append(" (fechamentofinanceiroconta.situacaocontareceber != '").append(SituacaoContaReceber.RECEBIDO.name()).append("' and ").append(realizarGeracaoWherePeriodo(relatorioSEIDecidirVO.getDataInicio(), relatorioSEIDecidirVO.getDataFim(), "fechamentofinanceiroconta.datacompetencia", false)).append(") ");
						andOr = " or ";
					}
					if(relatorioSEIDecidirVO.getRelatorioFinanceiroVO().getSituacaoRecebido()){
						sqlStr.append(andOr).append(" (fechamentofinanceiroconta.situacaocontareceber = '").append(SituacaoContaReceber.RECEBIDO.name()).append("' and ").append(realizarGeracaoWherePeriodo(relatorioSEIDecidirVO.getDataInicio(), relatorioSEIDecidirVO.getDataFim(), "fechamentofinanceiroconta.datacompetencia", false));
						sqlStr.append(" and ").append(realizarGeracaoWherePeriodoDataCompensacao(relatorioSEIDecidirVO, relatorioSEIDecidirVO.getDataInicio2(), relatorioSEIDecidirVO.getDataFim2())).append(") ");
					}
			}else{
				if(relatorioSEIDecidirVO.getRelatorioFinanceiroVO().getSituacaoPagar()){
					sqlStr.append(" (contapagar.situacao != 'PA' and ").append(realizarGeracaoWherePeriodo(relatorioSEIDecidirVO.getDataInicio(), relatorioSEIDecidirVO.getDataFim(), "contapagar.datafatogerador", false)).append(") ");
					andOr = " or ";
				}
				if(relatorioSEIDecidirVO.getRelatorioFinanceiroVO().getSituacaoPago()){
					sqlStr.append(andOr).append(" (contapagar.situacao = 'PA' and ").append(realizarGeracaoWherePeriodo(relatorioSEIDecidirVO.getDataInicio(), relatorioSEIDecidirVO.getDataFim(), "contapagar.datafatogerador", false));
					sqlStr.append(" and ").append(realizarGeracaoWherePeriodoDataCompensacao(relatorioSEIDecidirVO, relatorioSEIDecidirVO.getDataInicio2(), relatorioSEIDecidirVO.getDataFim2())).append(") ");
				}
			}
			sqlStr.append(" ) ");
			if (entrouCondicao) {
				sql.append(sqlStr);
			}
			break;
		case DATA_PAGAMENTO_E_DATA_COMPENSACAO:
			sql.append(" and ").append(realizarGeracaoWherePeriodo(relatorioSEIDecidirVO.getDataInicio(), relatorioSEIDecidirVO.getDataFim(), "negociacaopagamento.data", false));
			sql.append(" and ").append(realizarGeracaoWherePeriodoDataCompensacao(relatorioSEIDecidirVO, relatorioSEIDecidirVO.getDataInicio2(), relatorioSEIDecidirVO.getDataFim2()));			
			break;
		case DATA_RECEBIMENTO_E_DATA_COMPENSACAO:
			if(relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.FINANCEIRO_RECEITA)){
				sql.append(" and ").append(realizarGeracaoWherePeriodo(relatorioSEIDecidirVO.getDataInicio(), relatorioSEIDecidirVO.getDataFim(), "negociacaorecebimento.data", false));
				sql.append(" and ").append(realizarGeracaoWherePeriodoDataCompensacao(relatorioSEIDecidirVO, relatorioSEIDecidirVO.getDataInicio2(), relatorioSEIDecidirVO.getDataFim2()));
			}else {
				sql.append(" and ").append(realizarGeracaoWherePeriodo(relatorioSEIDecidirVO.getDataInicio(), relatorioSEIDecidirVO.getDataFim(), "fechamentofinanceiroconta.datarecebimento", false));
				sql.append(" and ").append(realizarGeracaoWherePeriodoDataCompensacao(relatorioSEIDecidirVO, relatorioSEIDecidirVO.getDataInicio2(), relatorioSEIDecidirVO.getDataFim2()));
			}
			break;		
		case PERIODO_COMPROMISSO:
			sql.append(" and ").append(realizarGeracaoWherePeriodo(relatorioSEIDecidirVO.getDataInicio(), relatorioSEIDecidirVO.getDataFim(), "compromissoagendapessoahorario.datacompromisso", false));
			break;
		case PERIODO_INTERACAO_WORKFLOW:
			sql.append(" and ").append(realizarGeracaoWhereDataInicioDataTermino(relatorioSEIDecidirVO.getDataInicio(), relatorioSEIDecidirVO.getDataFim(), "interacaoworkflow.datainicio",  "interacaoworkflow.datatermino", false));
			break;
		case PERIODO_FOLLOW_UP:
			
			break;
		case PERIODO_PRE_INSCRICAO:
			sql.append(" and ").append(realizarGeracaoWherePeriodo(relatorioSEIDecidirVO.getDataInicio(), relatorioSEIDecidirVO.getDataFim(), "preinscricao.data", false));
			break;
		case PERIODO_DATA_MOVIMENTACAO_CENTRO_ORIGEM:
			sql.append(" and ").append(realizarGeracaoWherePeriodo(relatorioSEIDecidirVO.getDataInicio(), relatorioSEIDecidirVO.getDataFim(), "centroresultadoorigem.datamovimentacao", false));
			break;
		default:
			break;
		}
		return sql.toString();
	}
	
	public String realizarGeracaoWherePeriodoDataCompensacao(RelatorioSEIDecidirVO relatorioSEIDecidirVO, Date dataInicio, Date dataTermino){
		if(relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltrosFinanceirosDespesa()){
			return realizarGeracaoWherePeriodoDataCompensacaoPagamento(relatorioSEIDecidirVO, dataInicio, dataTermino);
		}else if(relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltrosFinanceirosReceita()){
			if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.FINANCEIRO_RECEITA)) {
				return realizarGeracaoWherePeriodoDataCompensacaoRecebimento(relatorioSEIDecidirVO, dataInicio, dataTermino);
			} else if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.FINANCEIRO_FECHAMENTO_MES_RECEITA)) {
				return realizarGeracaoWherePeriodoDataCompensacaoFechamentoMesReceita(relatorioSEIDecidirVO, dataInicio, dataTermino);
			}
			
		}else if(relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltrosFechamentoMesReceita()){
			return realizarGeracaoWherePeriodoDataCompensacaoFechamentoMes(relatorioSEIDecidirVO, dataInicio, dataTermino);
		}
		return "";
	}
	
	public String realizarGeracaoWherePeriodoDataCompensacaoPagamento(RelatorioSEIDecidirVO relatorioSEIDecidirVO, Date dataInicio, Date dataTermino){
		StringBuilder sql = new StringBuilder();
		sql.append(" ( contapagar.situacao = 'PA' and ");
		sql.append(" ((formapagamento.tipo = 'CH' and  ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "cheque.dataprevisao", false)).append(") ");
		sql.append(" or (formapagamento.tipo != 'CH' and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "negociacaopagamento.data", false)).append(") ");
		sql.append(" ))");
		return sql.toString();
	}
	
	public String realizarGeracaoWherePeriodoDataCompensacaoRecebimento(RelatorioSEIDecidirVO relatorioSEIDecidirVO, Date dataInicio, Date dataTermino){
		StringBuilder sql = new StringBuilder();
		sql.append(" ( contareceber.situacao = '"+SituacaoContaReceber.RECEBIDO.getValor()+"' and ");
		sql.append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, TagSEIDecidirContaReceberEnum.CONTA_RECEBER_DATA_COMPENSACAO_FORMA_RECEBIMENTO.getCampo(), false));
		sql.append(" ) ");
		return sql.toString();
	}
	
	public String realizarGeracaoWherePeriodoDataCompensacaoFechamentoMesReceita(RelatorioSEIDecidirVO relatorioSEIDecidirVO, Date dataInicio, Date dataTermino){
		StringBuilder sql = new StringBuilder();
		sql.append(" (fechamentofinanceiroconta.origemfechamentofinanceiroconta = '").append(OrigemFechamentoFinanceiroContaEnum.CONTA_RECEBER.name()).append("' and fechamentofinanceiroconta.situacaocontareceber = '").append(SituacaoContaReceber.RECEBIDO.name()).append("')  ");
		sql.append(" and  ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "fechamentofinanceiroformapagamento.datacompensacao", false)).append("");
		return sql.toString();
	}
	
	public String realizarGeracaoWherePeriodoDataCompensacaoFechamentoMes(RelatorioSEIDecidirVO relatorioSEIDecidirVO, Date dataInicio, Date dataTermino){
		StringBuilder sql = new StringBuilder();
		sql.append(" ((fechamentofinanceiroconta.origemfechamentofinanceiroconta = ").append(OrigemFechamentoFinanceiroContaEnum.CONTA_RECEBER.name()).append(" and fechamentofinanceiroconta.situacaocontareceber = '").append(SituacaoContaReceber.RECEBIDO.name()).append("')  ");
		sql.append(" or (fechamentofinanceiroconta.origemfechamentofinanceiroconta = ").append(OrigemFechamentoFinanceiroContaEnum.CONTA_PAGAR.name()).append(" and fechamentofinanceiroconta.situacaocontapagar = '").append(SituacaoFinanceira.PAGO.name()).append("'))  ");
		sql.append(" and  ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "fechamentofinanceiroformapagamento.datacompensacao", false)).append("");
		return sql.toString();
	}
	

	private String adicionarFiltroTipoOrigem(FiltroRelatorioFinanceiroVO obj) {
		StringBuilder sqlStr = new StringBuilder("");
		if (obj.getTipoOrigemBiblioteca()) {
			sqlStr.append(", 'BIB'");
		}
		if (obj.getTipoOrigemBolsaCusteadaConvenio()) {
			sqlStr.append(", 'BCC'");
		}
		if (obj.getTipoOrigemContratoReceita()) {
			sqlStr.append(", 'CTR'");
		}
		if (obj.getTipoOrigemDevolucaoCheque()) {
			sqlStr.append(", 'DCH'");
		}
		if (obj.getTipoOrigemInclusaoReposicao()) {
			sqlStr.append(", 'IRE'");
		}
		if (obj.getTipoOrigemInscricaoProcessoSeletivo()) {
			sqlStr.append(", 'IPS'");
		}
		if (obj.getTipoOrigemMatricula()) {
			sqlStr.append(", 'MAT'");
		}
		if (obj.getTipoOrigemMensalidade()) {
			sqlStr.append(", 'MEN'");
		}
		if (obj.getTipoOrigemNegociacao()) {
			sqlStr.append(", 'NCR'");
		}
		if (obj.getTipoOrigemOutros()) {
			sqlStr.append(", 'OUT'");
		}
		if (obj.getTipoOrigemRequerimento()) {
			sqlStr.append(", 'REQ'");
		}
		if (obj.getTipoOrigemMaterialDidatico()) {
			sqlStr.append(", 'MDI'");
		}

		if (sqlStr.length() > 0) {
			return " and contareceber.tipoorigem in (''" + sqlStr.toString() + ") ";
		}
		return "";
	}
	
	private String adicionarFiltroTipoOrigemFechamentoMesReceita(FiltroRelatorioFinanceiroVO obj) {
		StringBuilder sqlStr = new StringBuilder("");
		if (obj.getTipoOrigemBiblioteca()) {
			sqlStr.append(", '").append(TipoOrigemContaReceber.BIBLIOTECA).append("' ");
		}
		if (obj.getTipoOrigemBolsaCusteadaConvenio()) {
			sqlStr.append(", '").append(TipoOrigemContaReceber.BOLSA_CUSTEADA_CONVENIO).append("' ");
		}
		if (obj.getTipoOrigemContratoReceita()) {
			sqlStr.append(", '").append(TipoOrigemContaReceber.CONTRATO_RECEITA).append("' ");
		}
		if (obj.getTipoOrigemDevolucaoCheque()) {
			sqlStr.append(", '").append(TipoOrigemContaReceber.DEVOLUCAO_CHEQUE).append("' ");
		}
		if (obj.getTipoOrigemInclusaoReposicao()) {
			sqlStr.append(", '").append(TipoOrigemContaReceber.INCLUSAOREPOSICAO).append("' ");
		}
		if (obj.getTipoOrigemInscricaoProcessoSeletivo()) {
			sqlStr.append(", '").append(TipoOrigemContaReceber.INSCRICAO_PROCESSO_SELETIVO).append("' ");
		}
		if (obj.getTipoOrigemMatricula()) {
			sqlStr.append(", '").append(TipoOrigemContaReceber.MATRICULA).append("' ");
		}
		if (obj.getTipoOrigemMensalidade()) {
			sqlStr.append(", '").append(TipoOrigemContaReceber.MENSALIDADE).append("' ");
		}
		if (obj.getTipoOrigemNegociacao()) {
			sqlStr.append(", '").append(TipoOrigemContaReceber.NEGOCIACAO).append("' ");
		}
		if (obj.getTipoOrigemOutros()) {
			sqlStr.append(", '").append(TipoOrigemContaReceber.OUTROS).append("' ");
		}
		if (obj.getTipoOrigemRequerimento()) {
			sqlStr.append(", '").append(TipoOrigemContaReceber.REQUERIMENTO).append("' ");
		}
		if (obj.getTipoOrigemMaterialDidatico()) {
			sqlStr.append(", '").append(TipoOrigemContaReceber.MATERIAL_DIDATICO).append("' ");
		}

		if (sqlStr.length() > 0) {
			return " and fechamentofinanceiroconta.tipoorigemcontareceber in (''" + sqlStr.toString() + ") ";
		}
		return "";
	}
	
	private void realizarGeracaoWhereFinanceiroCentroResultado(RelatorioSEIDecidirVO relatorioSEIDecidirVO, Map<String, Object> mapaParametrosRelatorio, StringBuilder sql) {
		sql.append(" and centroresultadoorigem.tipomovimentacaocentroresultadoorigemenum != 'NAO_CONTABILIZAR' ");
		if(Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getQtdNivelExistenteCentroResultado())) {
			sql.append(" and fn_countcaracter(centroresultadosuperior2.nivel, '.')+1 = ").append(relatorioSEIDecidirVO.getQtdNivelExistenteCentroResultado());	
		}
		sql.append(adicionarFiltroCentroResultado(relatorioSEIDecidirVO));
		if (realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "unidadeensino")
				|| relatorioSEIDecidirVO.getUnidadeEnsinoVOs().stream().anyMatch(p->p.getFiltrarUnidadeEnsino())) {
			sql.append(adicionarFiltroUnidadeEnsino(relatorioSEIDecidirVO));
		}
		if (realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "curso")
				|| relatorioSEIDecidirVO.getCursoVOs().stream().anyMatch(p->p.getFiltrarCursoVO())) {
			sql.append(adicionarFiltroCurso(relatorioSEIDecidirVO));	
		}
		if (realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "turno")
				|| relatorioSEIDecidirVO.getTurnoVOs().stream().anyMatch(p->p.getFiltrarTurnoVO())) {
			sql.append(adicionarFiltroTurno(relatorioSEIDecidirVO));
		}
		if (realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "centroreceita.descricao")
				|| realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "categoriadespesa.descricao")) {
			sql.append(adicionarFiltroCentroReceita(relatorioSEIDecidirVO));
			sql.append(adicionarFiltroCategoriaDespesa(relatorioSEIDecidirVO));		
		}
		
		if(relatorioSEIDecidirVO.getTipoFiltroPeriodo().equals(TipoFiltroPeriodoSeiDecidirEnum.PERIODO_DATA_MOVIMENTACAO_CENTRO_ORIGEM)) {
			sql.append(adicionarFiltroPeriodo(relatorioSEIDecidirVO));
		}
		
		if ((realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "turma")
				|| Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getTurmaVO())) && Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getTurmaVO())) {
			sql.append(" and turma.codigo = ").append(relatorioSEIDecidirVO.getTurmaVO().getCodigo());
		}
	}

	/**
	 * Este método gerar a clausula group by do sql de consulta para a geração
	 * do relatório de acordo com o layout selecionado, observando que para os
	 * campos que realizam operações de soma, count e media, maximo ou min não
	 * serão contalizadas
	 * 
	 * @param relatorioSEIDecidirVO
	 * @return
	 * @throws Exception
	 */
	private String realizarGeracaoGroupBy(RelatorioSEIDecidirVO relatorioSEIDecidirVO) throws Exception {
		boolean utilizarGroupBy = relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getLayoutRelatorioSeiDecidirCampoVOs().stream().anyMatch(p -> p.getUtilizarGroupBy()) || relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.FINANCEIRO_CENTRO_RESULTADO);
		
		if (utilizarGroupBy) {
			StringBuilder sql = new StringBuilder(" GROUP BY ");
			boolean virgula = false;
			if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.FINANCEIRO_CENTRO_RESULTADO)) {
				sql.append("	centroresultadosuperior2.nivel,");
				sql.append("	centroresultadosuperior2.nivelsuperior ");
				virgula = true;
			}
			for (String agrupador : relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getTagAgrupamentoUtilizada().keySet()) {
				if(relatorioSEIDecidirVO.getDataBase() == null){
					agrupador = agrupador.replaceAll("CONTA_RECEBER_DATA_BASE", "null");
				}else{
					agrupador = agrupador.replaceAll("CONTA_RECEBER_DATA_BASE", "'"+Uteis.getDataJDBC(relatorioSEIDecidirVO.getDataBase()).toString()+"'");				
				}
				sql.append(virgula ? ", " : "").append(relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getTagAgrupamentoUtilizada().get(agrupador).getCampo());
				virgula = true;
			}
			for (LayoutRelatorioSeiDecidirCampoVO layoutTag : relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getLayoutRelatorioSeiDecidirCampoVOs()) {
				if (layoutTag.getUtilizarGroupBy() && layoutTag.getCampoTagSubstituida() != null && !layoutTag.getCampoTagSubstituida().equals("null")) {
//					sql.append(virgula ? ", " : "").append(layoutTag.getCampoTagSubstituida().replace(" + ", ", "));
					sql.append(virgula ? ", " : "").append(layoutTag.getCampoTagSubstituida());
					virgula = true;
				}
			}
			String groupByAdicional = relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getGroupByAdicional();
			if(!groupByAdicional.trim().isEmpty()){
				for (String agrupador : relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getTagCondicaoWhereUtilizada().keySet()) {
					if(groupByAdicional.contains(agrupador) && relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getTagAgrupamentoUtilizada().containsKey(agrupador)){
						groupByAdicional = groupByAdicional.replaceAll(groupByAdicional, relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getTagAgrupamentoUtilizada().get(agrupador).getCampo());
					}
				}
				sql.append(virgula ? ", " : "").append(groupByAdicional);
			}
			if(relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getAgruparRelatorioPor().contains("@{")) {
				String[] tags = relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getAgruparRelatorioPor().split("@");				
				for(String tag: tags) {
					if(tag.contains("{")) {
						tag = tag.substring(tag.indexOf("{")+1, tag.indexOf("}"));
						sql.append(virgula ? ", " : "").append(tag);
					}
				}
			}
			
			
			return sql.toString();
		} else {
			if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getNivelDetalhamento().equals(RelatorioSEIDecidirNivelDetalhamentoEnum.ATIVIDADE_EXTRA_CLASSE_PROFESSOR)) {
				return relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getGroupByAdicional();
			}
			return "";
		}
	}

	/**
	 * Este método gerar a clausula order by do sql de consulta para a geração
	 * do relatório de acordo com o layout selecionado, observando que para os
	 * campos que realizam operações de soma, count e media, maximo ou min não
	 * serão utilizadas na ordenação, vale lembrar que o mesmo será ordenado de
	 * acordo com a ordem dos campos a serem apresentados no relatório Ex:
	 * unidadeensino, curso, turno, turma...
	 * 
	 * @param relatorioSEIDecidirVO
	 * @return
	 * @throws Exception
	 */
	private String realizarGeracaoOrderBy(RelatorioSEIDecidirVO relatorioSEIDecidirVO) throws Exception {
		boolean utilizarOrderBy = relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getLayoutRelatorioSeiDecidirCampoVOs().stream().anyMatch(p -> p.getUtilizarOrderBy()) || relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.FINANCEIRO_CENTRO_RESULTADO);

		if (utilizarOrderBy) {
			StringBuilder sql = new StringBuilder(" ORDER BY ");
			boolean virgula = false;
			if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.FINANCEIRO_CENTRO_RESULTADO)) {
				sql.append("	centroresultadosuperior2.nivel,");
				sql.append("	centroresultadosuperior2.nivelsuperior ");
				virgula = true;
			}
			if(Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getOrderByAdicional())) {
				String orderByAdd = relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getOrderByAdicional();			
				for (String orderBY : relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getTagCondicaoWhereUtilizada().keySet()) {
					if(orderByAdd.contains(orderBY)) {
						orderByAdd = orderByAdd.replace(orderBY, relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getTagCondicaoWhereUtilizada().get(orderBY).getCampo());
					}
				}							
				sql.append(virgula ? ", " : "").append(orderByAdd);
				virgula = true;
			}
			for (String agrupador : relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getTagAgrupamentoUtilizada().keySet()) {
				if(relatorioSEIDecidirVO.getDataBase() == null){
					agrupador = agrupador.replaceAll("CONTA_RECEBER_DATA_BASE", "null");
				}else{
					agrupador = agrupador.replaceAll("CONTA_RECEBER_DATA_BASE", "'"+Uteis.getDataJDBC(relatorioSEIDecidirVO.getDataBase()).toString()+"'");				
				}
				sql.append(virgula ? ", " : "").append(agrupador);
				virgula = true;
			}
			
			for (LayoutRelatorioSeiDecidirCampoVO layoutTag : relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getLayoutRelatorioSeiDecidirCampoVOs()) {
				if (layoutTag.getUtilizarOrderBy()  && layoutTag.getCampoTagSubstituida() != null && !layoutTag.getCampoTagSubstituida().equals("null")) {
					sql.append(virgula ? ", " : "").append(layoutTag.getAlias()).append(layoutTag.getOrdenarDescrecente() ? " desc": " asc");
					virgula = true;
				}
			}
			if(relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getAgruparRelatorioPor().contains("@{")) {
				String[] tags = relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getAgruparRelatorioPor().split("@");
				//Integer nrTag = relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getTagAgrupamentoUtilizada().size()+1;
				for(String tag: tags) {
					if(tag.contains("{")) {
						tag = tag.substring(tag.indexOf("{")+1, tag.indexOf("}"));
						sql.append(virgula ? ", " : "").append(tag);
						virgula = true;
					}
				}
			}
			return sql.toString();
		} else {
			return "";
		}
	}

	private void validarDados(RelatorioSEIDecidirVO relatorioSEIDecidirVO) throws Exception {
		boolean existeUnidade = false;
		if (!relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.PROCESSO_SELETIVO)) {
		for (UnidadeEnsinoVO unidade : relatorioSEIDecidirVO.getUnidadeEnsinoVOs()) {
			if (unidade.getFiltrarUnidadeEnsino()) {
				existeUnidade = true;
				break;
			}
		}
		if (!existeUnidade) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_RelatorioSEIDecidir_unidadeEnsino"));
		}
		}
		if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getCodigo() == 0) {
			throw new ConsistirException(UteisJSF.internacionalizar("ms_RelatorioSEIDecidir_layout"));
		}
		
		if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.FINANCEIRO_FECHAMENTO_MES_RECEITA)) {
			if (!Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getPeriodoFechamentoMesAno())) {
				throw new ConsistirException(UteisJSF.internacionalizar("ms_RelatorioSEIDecidir_PeriodoFechamento"));
			}
		}

		validarDadosRH(relatorioSEIDecidirVO);
		validarDadosPlanoOrcamentario(relatorioSEIDecidirVO);
	}

	private void validarDadosRH(RelatorioSEIDecidirVO relatorioSEIDecidirVO) throws ConsistirException {
		if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getNivelDetalhamento().getName().equals(RelatorioSEIDecidirNivelDetalhamentoEnum.EVENTO_FOLHA_PAGAMENTO.name())) {
			if (!Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getEventoFolhaPagamentoVO())) {
				throw new ConsistirException(UteisJSF.internacionalizar("ms_RelatorioSEIDecidir_EventoFolhaPagamento"));
			}
		}
	}
	
	public XSSFCellStyle getCss(XSSFWorkbook workbook, short corFundo, short alinhamentoHorizontal, String pattner, boolean bordaTopo, boolean bordaRodape, boolean bordaEsquerda, boolean bordaDireita, short corBorda, boolean gerarFormatoExportacaoDados) {
		XSSFCellStyle style = null;
		workbook.getNumCellStyles();
		if (gerarFormatoExportacaoDados) {
			for (int x = 0; x < workbook.getNumCellStyles(); x++) {
				style = workbook.getCellStyleAt((short) x);
				if (!pattner.trim().isEmpty() && style.getDataFormat() == workbook.createDataFormat().getFormat(pattner) 
						|| (pattner.trim().isEmpty() && style.getDataFormat() == 0)) {
					return style;
				}
			}
			style = workbook.createCellStyle();
			if (!pattner.trim().isEmpty()) {
				style.setDataFormat(workbook.createDataFormat().getFormat(pattner));
			}
			return style;
		}
		for (int x = 0; x < workbook.getNumCellStyles(); x++) {
			style = workbook.getCellStyleAt((short) x);
			if (style.getFillForegroundColor() == corFundo && style.getAlignment() == alinhamentoHorizontal && ((!pattner.trim().isEmpty() && style.getDataFormat() == workbook.createDataFormat().getFormat(pattner)) || (pattner.trim().isEmpty() && style.getDataFormat() == 0)) && ((bordaRodape && style.getBorderBottom() == XSSFCellStyle.BORDER_THIN && style.getBottomBorderColor() == corBorda) || (!bordaRodape && style.getBorderBottom() == XSSFCellStyle.BORDER_NONE)) && ((bordaTopo && style.getBorderTop() == XSSFCellStyle.BORDER_THIN && style.getTopBorderColor() == corBorda) || (!bordaTopo && style.getBorderTop() == XSSFCellStyle.BORDER_NONE)) && ((bordaEsquerda && style.getBorderLeft() == XSSFCellStyle.BORDER_THIN && style.getLeftBorderColor() == corBorda) || (!bordaTopo && style.getBorderLeft() == XSSFCellStyle.BORDER_NONE))
					&& ((bordaDireita && style.getBorderRight() == XSSFCellStyle.BORDER_THIN && style.getRightBorderColor() == corBorda) || (!bordaTopo && style.getBorderRight() == XSSFCellStyle.BORDER_NONE))) {
				return style;
			}
		}
		style = workbook.createCellStyle();
		if (!gerarFormatoExportacaoDados) {
			style.setFillForegroundColor(corFundo);
			style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		}
		if (!pattner.trim().isEmpty()) {
			style.setDataFormat(workbook.createDataFormat().getFormat(pattner));
		}
		if (bordaRodape && !gerarFormatoExportacaoDados) {
			style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
			style.setBottomBorderColor(corBorda);
		} else {
			style.setBorderBottom(XSSFCellStyle.BORDER_NONE);
		}
		if (bordaTopo && !gerarFormatoExportacaoDados) {
			style.setBorderTop(XSSFCellStyle.BORDER_THIN);
			style.setTopBorderColor(corBorda);
		} else {
			style.setBorderTop(XSSFCellStyle.BORDER_NONE);
		}
		if (bordaEsquerda && !gerarFormatoExportacaoDados) {
			style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
			style.setLeftBorderColor(corBorda);
		} else {
			style.setBorderLeft(XSSFCellStyle.BORDER_NONE);
		}
		if (bordaDireita && !gerarFormatoExportacaoDados) {
			style.setBorderRight(XSSFCellStyle.BORDER_THIN);
			style.setRightBorderColor(corBorda);
		} else {
			style.setBorderRight(XSSFCellStyle.BORDER_NONE);
		}
		style.setAlignment(alinhamentoHorizontal);
		style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		style.setWrapText(true);

		return style;
	}

	private int realizarImportacaoImagemExcel(XSSFWorkbook workbook, String caminhoLogo) throws Exception {
		FileInputStream fis = null;
		ByteArrayOutputStream bos = null;
		int pictureIndex = 0;
		try {
			fis = new FileInputStream(caminhoLogo);
			bos = new ByteArrayOutputStream();
			int c;
			while ((c = fis.read()) != -1)
				bos.write(c);
			if (caminhoLogo.endsWith("png") || caminhoLogo.endsWith("PNG")) {
				pictureIndex = workbook.addPicture(bos.toByteArray(), XSSFWorkbook.PICTURE_TYPE_PNG);
			} else {
				pictureIndex = workbook.addPicture(bos.toByteArray(), XSSFWorkbook.PICTURE_TYPE_JPEG);
			}
		} finally {
			if (fis != null)
				fis.close();
			if (bos != null)
				bos.close();
		}
		return pictureIndex;
	}

	
	public String realizarGeracaoArquivoXML(RelatorioSEIDecidirVO relatorioSEIDecidirVO, SqlRowSet sqlRowSet, UsuarioVO usuarioVO) throws Exception{
		String nomePadrao = (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getNomePadraoArquivo().trim().isEmpty() ? "SEIDECIDIR_" : relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getNomePadraoArquivo().trim()+"_");
		String nomeArquivo = nomePadrao + Uteis.getData(new Date(), "dd_MM_yy_hh_mm_ss") + "_" + usuarioVO.getCodigo() + ".xml";		
		File file = new File(UteisJSF.getCaminhoWeb() + File.separator + "relatorio" + File.separator + nomeArquivo);
		StringBuilder xml = new StringBuilder("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>").append("\n");
		xml.append(relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getTagAberturaGeralXml()).append("\n");
		while(sqlRowSet.next()){
			xml.append(relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getTagAberturaLinhaXml()).append("\n");
			int x = 1;
			for(LayoutRelatorioSeiDecidirCampoVO layoutTag: relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getLayoutRelatorioSeiDecidirCampoVOs()){
				xml.append("<").append(layoutTag.getTitulo()).append(">");
				String textoSubstituir = "";
									
				String agrupamento = "campo"+x;
					switch (layoutTag.getTipoCampo()) {
					case DATA:
						textoSubstituir = Uteis.getData(sqlRowSet.getDate(agrupamento));
						break;
					case MES_ANO:
						if(sqlRowSet.getObject(agrupamento) instanceof java.sql.Date){
							textoSubstituir = Uteis.getData(sqlRowSet.getDate(agrupamento), "MM/yyyy");
						}else{
							textoSubstituir = sqlRowSet.getString(agrupamento);
						}
						break;
					case DOUBLE:
						textoSubstituir = Uteis.getDoubleFormatado(sqlRowSet.getDouble(agrupamento));
						break;
					case MOEDA:
						textoSubstituir = Uteis.getDoubleFormatado(sqlRowSet.getDouble(agrupamento));
						break;
					case INTEIRO:
						textoSubstituir = ((Integer) sqlRowSet.getInt(agrupamento)).toString();
						break;
					case TEXTO:
						textoSubstituir = sqlRowSet.getString(agrupamento);
						break;
					default:
						textoSubstituir = sqlRowSet.getObject(agrupamento).toString();
						break;
					}
				
				xml.append(textoSubstituir).append("</").append(layoutTag.getTitulo()).append(">").append("\n");
				x++;
			}
			xml.append(relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getTagFechamentoLinhaXml()).append("\n");
		}
		xml.append(relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getTagFechamentoGeralXml());
		PrintWriter printWriter = new PrintWriter(file, "ISO-8859-1");
		printWriter.append(xml.toString());
		printWriter.flush();
		printWriter.close();
		return nomeArquivo;
	}

	private String realizarGeracaoFromModuloFinanceiroFechamentoMes(RelatorioSEIDecidirVO relatorioSEIDecidirVO) throws Exception {
		StringBuilder sql = new StringBuilder(" ");
		sql.append(" FROM fechamentoMes ");
		sql.append(" inner join fechamentofinanceiro on fechamentoMes.codigo = fechamentofinanceiro.fechamentoMes ");
		sql.append(" and fechamentofinanceiro.codigo = (select ff.codigo from fechamentofinanceiro ff where ff.fechamentoMes = fechamentoMes.codigo order by ff.dataFechamento desc, ff.codigo desc limit 1 ) ");
		sql.append(" inner join fechamentofinanceiroconta  on fechamentofinanceiro.codigo = fechamentofinanceiroconta.fechamentofinanceiro ");
//		sql.append(" left join fechamentofinanceirodetalhamentovalor on fechamentofinanceirodetalhamentovalor.fechamentofinanceiroconta = fechamentofinanceiroconta.codigo ");
//		sql.append(" left join fechamentofinanceirocentroresultado on fechamentofinanceirocentroresultado.codigoorigem = fechamentofinanceiroconta.codigo ");
//		sql.append(" and fechamentofinanceirocentroresultado.origemfechamentofinanceirocentroresultadoorigem = 'FECHAMENTO_FINANCEIRO_CONTA' ");
//		sql.append(" left join convenio on convenio.codigo = fechamentofinanceirodetalhamentovalor.codOrigemDoTipoDetalhe and fechamentofinanceirodetalhamentovalor.tipoCentroResultadoOrigemDetalhe in ('DESCONTO_CUSTEADO_CONTA_RECEBER', 'DESCONTO_CONVENIO') ");
//		sql.append(" left join parceiro on convenio.parceiro = parceiro.codigo ");
//		sql.append(" left join planodesconto on planodesconto.codigo = fechamentofinanceirodetalhamentovalor.codOrigemDoTipoDetalhe "); 
//		sql.append(" and fechamentofinanceirodetalhamentovalor.tipoCentroResultadoOrigemDetalhe in ('DESCONTO_INSTITUICAO') ");
		sql.append(" left join matricula on matricula.matricula = fechamentofinanceiroconta.matricula ");
		sql.append(" left join matriculaPeriodo on matriculaPeriodo.codigo = fechamentofinanceiroconta.matriculaPeriodo ");
		sql.append(" left join periodoletivo as periodoletivomatriculaperiodo on matriculaPeriodo.periodoletivomatricula = periodoletivomatriculaperiodo.codigo ");
//		sql.append(" left join fechamentoFinanceiroFormaPagamento on fechamentoFinanceiroFormaPagamento.fechamentofinanceiroconta = fechamentofinanceiroconta.codigo ");
		if(relatorioSEIDecidirVO.isConsiderarUnidadeEnsinoFinanceira()){
			sql.append(" inner join unidadeensino on unidadeensino.codigo = fechamentofinanceiroconta.unidadeensinofinanceira ");
		}else{
			sql.append(" inner join unidadeensino on unidadeensino.codigo = fechamentofinanceiroconta.unidadeensinoacademica ");
		}
		if (realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "unidadeensinocidade") || realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "unidadeensinoestado")) {			
			sql.append(" left join cidade as unidadeensinocidade on unidadeensino.cidade = unidadeensinocidade.codigo ");
			if (realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "unidadeensinoestado")) {
				sql.append(" left join estado as unidadeensinoestado on unidadeensinoestado.codigo = unidadeensinocidade.estado ");
			}
		}
		sql.append(" left join curso on curso.codigo = matricula.curso ");
		sql.append(" left join eixocurso on curso.eixocurso = eixocurso.codigo ");
		sql.append(" left join turno on turno.codigo = matricula.turno ");
		sql.append(" left join pessoa as aluno on aluno.codigo = matricula.aluno ");
		if (realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "alunocidade") || realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "alunoestado")) {
			sql.append(" left join cidade as alunocidade on aluno.cidade = alunocidade.codigo ");
			if (realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "alunoestado")) {
				sql.append(" left join estado as alunoestado on alunocidade.estado = alunoestado.codigo ");
			}
		}
		return sql.toString();
	}

	private String realizarGeracaoFromModuloRecursosHumanos(RelatorioSEIDecidirVO relatorioSEIDecidirVO) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" FROM funcionariocargo fc");
		sql.append(" inner join funcionario f on fc.funcionario = f.codigo");
		sql.append(" inner join pessoa p on f.pessoa = p.codigo ");
		sql.append(" left join cargo c on c.codigo = fc.cargo ");

		if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getNivelDetalhamento().getName().equals(RelatorioSEIDecidirNivelDetalhamentoEnum.ATIVIDADE_EXTRA_CLASSE_PROFESSOR.name())) {
			sql.append(" inner join atividadeextraclasseprofessorpostado as atividadeextraclasseprofessorpostado on atividadeextraclasseprofessorpostado.funcionariocargo = fc.codigo");
			sql.append(" left join atividadeextraclasseprofessor as atividadeextraclasseprofessor on atividadeextraclasseprofessor.codigo = atividadeextraclasseprofessorpostado.atividadeextraclasseprofessor");
			sql.append(" left join curso on atividadeextraclasseprofessorpostado.curso = curso.codigo");
		}

		if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getNivelDetalhamento().getName().equals(RelatorioSEIDecidirNivelDetalhamentoEnum.CONTRA_CHEQUE.name())
				|| relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getNivelDetalhamento().getName().equals(RelatorioSEIDecidirNivelDetalhamentoEnum.EVENTO_FOLHA_PAGAMENTO.name())
				|| relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getNivelDetalhamento().getName().equals(RelatorioSEIDecidirNivelDetalhamentoEnum.CROSSTAB.name())) {			
			sql.append(" left join contracheque on fc.codigo = contracheque.funcionariocargo");

			if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getNivelDetalhamento().getName().equals(RelatorioSEIDecidirNivelDetalhamentoEnum.EVENTO_FOLHA_PAGAMENTO.name())
					|| relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getNivelDetalhamento().getName().equals(RelatorioSEIDecidirNivelDetalhamentoEnum.CROSSTAB.name())) {
				sql.append(" INNER JOIN contrachequeevento ON contrachequeevento.contracheque = contracheque.codigo");				
				sql.append(" INNER JOIN eventofolhapagamento ON eventofolhapagamento.codigo = contrachequeevento.eventofolhapagamento");				
			}
		}

		if (!relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getNivelDetalhamento().getName().equals(RelatorioSEIDecidirNivelDetalhamentoEnum.PERIODO_AQUISITIVO.name())
				&& !relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getNivelDetalhamento().getName().equals(RelatorioSEIDecidirNivelDetalhamentoEnum.FUNCIONARIO_CARGO.name())
				&& !relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getNivelDetalhamento().getName().equals(RelatorioSEIDecidirNivelDetalhamentoEnum.FALTAS_FUNCIONARIO.name()) 
				&& !relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getNivelDetalhamento().getName().equals(RelatorioSEIDecidirNivelDetalhamentoEnum.PROGRESSAO_SALARIAL.name())
				&& !relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getNivelDetalhamento().getName().equals(RelatorioSEIDecidirNivelDetalhamentoEnum.CONTA_PAGAR_FUNCIONARIO.name())
				&& !relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getNivelDetalhamento().getName().equals(RelatorioSEIDecidirNivelDetalhamentoEnum.ATIVIDADE_EXTRA_CLASSE_PROFESSOR.name())) {
			sql.append(" INNER join competenciafolhapagamento cfp on cfp.codigo = contracheque.competenciafolhapagamento");
			//sql.append(" INNER join competenciaperiodofolhapagamento cp on cp.competenciafolhapagamento = contracheque.competenciafolhapagamento");
		}

		if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getNivelDetalhamento().getName().equals(RelatorioSEIDecidirNivelDetalhamentoEnum.FALTAS_FUNCIONARIO.name())) {
			sql.append("INNER JOIN faltasfuncionario ON faltasfuncionario.funcionariocargo = fc.codigo");
		}

		if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getNivelDetalhamento().getName().equals(RelatorioSEIDecidirNivelDetalhamentoEnum.PERIODO_AQUISITIVO.name())) {
			sql.append(" INNER JOIN periodoaquisitivoferias ON periodoaquisitivoferias.funcionariocargo = fc.codigo");
		}

		if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getNivelDetalhamento().getName().equals(RelatorioSEIDecidirNivelDetalhamentoEnum.CONTA_PAGAR_FUNCIONARIO.name())) {
			sql.append(" INNER JOIN contapagar ON contapagar.funcionario = f.codigo");
			sql.append(" left JOIN banco ON contapagar.bancorecebimento = banco.codigo");
			sql.append(" left join contapagarnegociacaopagamento on contapagarnegociacaopagamento.contapagar = contapagar.codigo ");
			sql.append(" left join negociacaopagamento on negociacaopagamento.codigo = contapagarnegociacaopagamento.negociacaocontapagar ");
			sql.append(" left join usuario as negociacaopagamentoresponsavel on negociacaopagamentoresponsavel.codigo = negociacaopagamento.responsavel ");
			
			sql.append(" left join contapagarpagamento on contapagarpagamento.contapagar = contapagar.codigo and contapagarpagamento.negociacaopagamento = negociacaopagamento.codigo ");
			sql.append(" left join formapagamentonegociacaopagamento on contapagarpagamento.formapagamentonegociacaopagamento = formapagamentonegociacaopagamento.codigo ");		
			sql.append(" left join formapagamento on contapagarpagamento.formapagamento = formapagamento.codigo ");
			sql.append(" left join cheque on formapagamentonegociacaopagamento.cheque = cheque.codigo ");
		}

		if (Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getTemplateLancamentoFolhaPagamento())) {
			sql.append(" left join templatelancamentofolhapagamento templatelancamentofolhapagamento on templatelancamentofolhapagamento.codigo = contracheque.templatelancamentofolhapagamento ");
		}

		if (Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getTemplateLancamentoFolhaPagamento().getSecaoFolhaPagamento())) {			
			sql.append(" left join secaofolhapagamento s on s.codigo = fc.secaofolhapagamento");
		}

		if (Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getUnidadeEnsinoVO())) {
			sql.append(" inner join unidadeensino u on u.codigo = fc.unidadeensino");
		}

		if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getNivelDetalhamento().getName().equals(RelatorioSEIDecidirNivelDetalhamentoEnum.PROGRESSAO_SALARIAL.name())) {
			sql.append(" INNER JOIN historicosalarial ON historicosalarial.funcionariocargo = fc.codigo");
		}

		return sql.toString();
	}
	
	
	
	private String realizarGeracaoFromModuloCrm(RelatorioSEIDecidirVO relatorioSEIDecidirVO) throws Exception {
		StringBuilder sql = new StringBuilder(" ");
		if(relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getNivelDetalhamento().equals(RelatorioSEIDecidirNivelDetalhamentoEnum.COMPROMISSO)) {
			sql.append(" from compromissoagendapessoahorario ");
			sql.append(" left join prospects on compromissoagendapessoahorario.prospect = prospects.codigo ");
			sql.append(" left join campanha on campanha.codigo = compromissoagendapessoahorario.campanha ");
			sql.append(" left join agendapessoahorario on agendapessoahorario.codigo = compromissoagendapessoahorario.agendapessoahorario ");
			sql.append(" left join agendapessoa on agendapessoa.codigo = agendapessoahorario.agendapessoa ");
			sql.append(" left join pessoa as funcionariopessoa on funcionariopessoa.codigo = agendapessoa.pessoa");
			sql.append(" left join funcionario on funcionario.pessoa = funcionariopessoa.codigo");
		}else if(relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getNivelDetalhamento().equals(RelatorioSEIDecidirNivelDetalhamentoEnum.PRE_INSCRICAO)) {
			sql.append(" from preinscricao ");
			sql.append(" left join prospects on preinscricao.prospect = prospects.codigo ");
		}else if(relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getNivelDetalhamento().equals(RelatorioSEIDecidirNivelDetalhamentoEnum.INTERACOES_WORKFLOW)) {
			sql.append(" from interacaoworkflow ");
			sql.append(" left join prospects on interacaoworkflow.prospect = prospects.codigo ");
		}else if(relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getNivelDetalhamento().equals(RelatorioSEIDecidirNivelDetalhamentoEnum.FOLLOW_UP)) {
			sql.append(" from followup ");
			sql.append(" left join prospects on followup.prospect = prospects.codigo ");
		}else if(relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getNivelDetalhamento().equals(RelatorioSEIDecidirNivelDetalhamentoEnum.PROSPECTS)) {
			sql.append(" from prospects ");
		}else if(relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getNivelDetalhamento().equals(RelatorioSEIDecidirNivelDetalhamentoEnum.UNIDADE_ENSINO)) {
			sql.append(" from unidadeensino ");
			sql.append(" left join prospects on unidadeensino.codigo = prospects.unidadeensino ");
		}else if(relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getNivelDetalhamento().equals(RelatorioSEIDecidirNivelDetalhamentoEnum.MATRICULA)) {
			sql.append(" from matricula ");
			sql.append(" left join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
			sql.append(" left join prospects on matricula.aluno = prospects.pessoa ");
		}else if(relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getNivelDetalhamento().equals(RelatorioSEIDecidirNivelDetalhamentoEnum.CURSO)) {
			sql.append(" from curso ");
			sql.append(" left join cursointeresse on cursointeresse.curso = curso.codigo ");
			sql.append(" left join eixocurso on curso.eixocurso = eixocurso.codigo ");
			sql.append(" left join prospects on cursointeresse.prospects = prospects.codigo ");
		}		
		
		if (realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "prospectscidade")) {
			sql.append(" left join cidade as prospectscidade on prospectscidade.codigo = prospects.cidade ");
			sql.append(" left join estado as prospectsestado on prospectsestado.codigo = prospectscidade.estado ");			
		}
		if ((realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "unidadeensino")
				|| relatorioSEIDecidirVO.getUnidadeEnsinoVOs().stream().anyMatch(p->p.getFiltrarUnidadeEnsino())) 
				&& !relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getNivelDetalhamento().equals(RelatorioSEIDecidirNivelDetalhamentoEnum.UNIDADE_ENSINO)) {
			sql.append(" left join unidadeensino on unidadeensino.codigo = prospects.unidadeensino ");
			if (realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "unidaeensinocidade")) {
				sql.append(" left join cidade as unidaeensinocidade on unidaeensinocidade.codigo = unidadeensino.cidade ");
				sql.append(" left join estado as unidaeensinoestado on unidaeensinoestado.codigo = unidaeensinocidade.estado ");
			}
		}
		if ((realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "cursointeresse")
				|| realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "curso")
				|| relatorioSEIDecidirVO.getCursoVOs().stream().anyMatch(p->p.getFiltrarCursoVO()))
				&& !relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getNivelDetalhamento().equals(RelatorioSEIDecidirNivelDetalhamentoEnum.CURSO)) {
			sql.append(" left join cursointeresse on cursointeresse.prospects = prospects.codigo ");
			sql.append(" left join curso on cursointeresse.curso = curso.codigo ");
			sql.append(" left join eixocurso on curso.eixocurso = eixocurso.codigo ");
		}
		if ((realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "compromissoagendapessoahorario")
				|| realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "campanha")
				|| realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "agendapessoahorario")
				|| realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "agendapessoa")
				|| realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "funcionario")
				|| relatorioSEIDecidirVO.getListaCampanhaVOs().stream().anyMatch(p->p.getFiltrarCampanhaVO())
				|| relatorioSEIDecidirVO.getListaFuncionarioVOs().stream().anyMatch(p->p.getFiltrarFuncionarioVO())
				|| relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltrosSituacaoCompromisso()
				|| relatorioSEIDecidirVO.getTipoFiltroPeriodo().equals(TipoFiltroPeriodoSeiDecidirEnum.PERIODO_COMPROMISSO))
				&& !relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getNivelDetalhamento().equals(RelatorioSEIDecidirNivelDetalhamentoEnum.COMPROMISSO)) {
			sql.append(" left join compromissoagendapessoahorario on compromissoagendapessoahorario.prospect = prospects.codigo ");
			sql.append(" left join campanha on campanha.codigo = compromissoagendapessoahorario.campanha ");
			sql.append(" left join agendapessoahorario on agendapessoahorario.codigo = compromissoagendapessoahorario.agendapessoahorario ");
			sql.append(" left join agendapessoa on agendapessoa.codigo = agendapessoahorario.agendapessoa ");
			sql.append(" left join pessoa as funcionariopessoa on funcionariopessoa.codigo = agendapessoa.pessoa");
			sql.append(" left join funcionario on funcionario.pessoa = funcionariopessoa.codigo");			
		}
		if ((realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "preinscricao")
				|| relatorioSEIDecidirVO.getTipoFiltroPeriodo().equals(TipoFiltroPeriodoSeiDecidirEnum.PERIODO_PRE_INSCRICAO)) 
				&& !relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getNivelDetalhamento().equals(RelatorioSEIDecidirNivelDetalhamentoEnum.PRE_INSCRICAO)) {
			sql.append(" left join preinscricao on preinscricao.prospect = prospects.codigo ");	
		}
		if ((realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "followup")
				|| relatorioSEIDecidirVO.getTipoFiltroPeriodo().equals(TipoFiltroPeriodoSeiDecidirEnum.PERIODO_FOLLOW_UP)) 
				&& !relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getNivelDetalhamento().equals(RelatorioSEIDecidirNivelDetalhamentoEnum.FOLLOW_UP)) {
			sql.append(" left join followup on followup.prospect = prospects.codigo ");	
		}
		if ((realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "interacaoworkflow")
				|| relatorioSEIDecidirVO.getTipoFiltroPeriodo().equals(TipoFiltroPeriodoSeiDecidirEnum.PERIODO_INTERACAO_WORKFLOW)) 
				&& !relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getNivelDetalhamento().equals(RelatorioSEIDecidirNivelDetalhamentoEnum.INTERACOES_WORKFLOW)) {
			sql.append(" left join interacaoworkflow on interacaoworkflow.prospect = prospects.codigo ");	
		}
		if (realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "matricula") && !relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getNivelDetalhamento().equals(RelatorioSEIDecidirNivelDetalhamentoEnum.MATRICULA)) {
			sql.append(" left join matricula on matricula.aluno = prospects.pessoa ");	
			sql.append(" left join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
		}
		return sql.toString();
	}
	
	private String realizarGeracaoFromModuloCentroResultado(RelatorioSEIDecidirVO relatorioSEIDecidirVO) throws Exception {
		StringBuilder sql = new StringBuilder(" ");
		sql.append(" from  centroresultado ");
		sql.append(" inner join centroresultadosuperior as centroresultadosuperior on centroresultado.codigo = any(string_to_array(centroresultadosuperior.codigossuperiores, ',')::int[]) ");
		sql.append(" inner join centroresultadosuperior as centroresultadosuperior2 on centroresultado.codigo = centroresultadosuperior2.codigo ");
		sql.append(" inner join centroresultadoorigem on centroresultadoorigem.centroresultadoadministrativo = centroresultadosuperior.codigo ");
		if (realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "unidadeensino")
				|| relatorioSEIDecidirVO.getUnidadeEnsinoVOs().stream().anyMatch(p->p.getFiltrarUnidadeEnsino())) {
			sql.append(" inner join unidadeensino  on unidadeensino.codigo = centroresultadoorigem.unidadeensino  ");
			if (realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "unidaeensinocidade")) {
				sql.append(" left join cidade as unidaeensinocidade on unidaeensinocidade.codigo = unidadeensino.cidade ");
				sql.append(" left join estado as unidaeensinoestado on unidaeensinoestado.codigo = unidaeensinocidade.estado ");
			}
		}
		if (realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "curso")
				|| relatorioSEIDecidirVO.getCursoVOs().stream().anyMatch(p->p.getFiltrarCursoVO())) {
			sql.append(" inner join curso on curso.codigo = centroresultadoorigem.curso ");
			sql.append(" left join eixocurso on curso.eixocurso = eixocurso.codigo ");
		}
		if (realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "turno")
				|| relatorioSEIDecidirVO.getTurnoVOs().stream().anyMatch(p->p.getFiltrarTurnoVO())) {
			sql.append(" inner join turno on turno.codigo = centroresultadoorigem.turno ");
		}		
		if (realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "turma")
				|| Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getTurmaVO())) {
			sql.append(" inner join turma on turma.codigo = centroresultadoorigem.turma ");
		}
		if (realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "centroreceita.descricao")
				|| realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "categoriadespesa.descricao")
				|| relatorioSEIDecidirVO.getCentroReceitaVOs().stream().anyMatch(p->p.getFiltrarCentroReceitaVO())
				|| relatorioSEIDecidirVO.getCategoriaDespesaVOs().stream().anyMatch(p->p.getSelecionar())) {
			sql.append(" left join centroreceita on centroreceita.codigo = centroresultadoorigem.centroreceita ");
			sql.append(" left join categoriadespesa on categoriadespesa.codigo = centroresultadoorigem.categoriadespesa ");
		}
		return sql.toString();
	}
	
	
	private String adicionarFiltroPlanoOrcamentario(RelatorioSEIDecidirVO obj) {
		StringBuilder sql = new StringBuilder();

		if (Uteis.isAtributoPreenchido(obj.getPlanosOrcamentarioVOs())) {
			sql.append(" and planoorcamentario.codigo ").append(realizarGeracaoInValor(obj.getPlanosOrcamentarioVOs().size(), 
					gerarArray(obj.getPlanosOrcamentarioVOs())));
		}

		if (Uteis.isAtributoPreenchido(obj.getDepartamentosVOs())) {
			sql.append(" and departamento.codigo ").append(realizarGeracaoInValor(obj.getDepartamentosVOs().size(), 
					gerarArray(obj.getDepartamentosVOs())));
		}

		if (Uteis.isAtributoPreenchido(obj.getCategoriaDespesaVOs())) {
			sql.append(" and categoriadespesa.codigo ").append(realizarGeracaoInValor(obj.getCategoriaDespesaVOs().size(), 
					gerarArray(obj.getCategoriaDespesaVOs())));
		}

		if (Uteis.isAtributoPreenchido(obj.getCategoriasProdutoVOs())) {
			sql.append(" and categoriaproduto.codigo ").append(realizarGeracaoInValor(obj.getCategoriasProdutoVOs().size(), 
					gerarArray(obj.getCategoriasProdutoVOs())));
		}

		if (Uteis.isAtributoPreenchido(obj.getCentrosResultadoVOs())) {
			sql.append(" and centroresultado.codigo ").append(realizarGeracaoInValor(obj.getCentrosResultadoVOs().size(), 
					gerarArray(obj.getCentrosResultadoVOs())));
		}

		if (obj.getSituacoes() != null && obj.getSituacoes().length > 0) {
			boolean existePE = false;
			for (String string : obj.getSituacoes()) {
				if (string.equals("PE")) {
					existePE = true;
				}
			}
			sql.append(" and (requisicao.situacaoAutorizacao ");
			sql.append(realizarGeracaoInValor(obj.getSituacoes().length, obj.getSituacoes()));
			sql.append(existePE ? " or requisicao.situacaoAutorizacao is null)" : ")"); 
		}

		if (obj.getTipoAutorizacoes() != null && obj.getTipoAutorizacoes().length > 0) {
			sql.append(" and requisicao.tipoautorizacaorequisicao ");
			sql.append(realizarGeracaoInValor(obj.getTipoAutorizacoes().length, obj.getTipoAutorizacoes()));
		}

		sql.append(" and " + realizarGeracaoWherePeriodo(obj.getDataInicio2(), obj.getDataFim2(), "requisicao.dataRequisicao", false));
		sql.append(" and " + realizarGeracaoWherePeriodo(obj.getDataInicioPeriodoAutorizacaoRequisicao(), obj.getDataFimPeriodoAutorizacaoRequisicao(), "requisicao.dataAutorizacao", false));
		sql.append(" and " + realizarGeracaoWherePeriodo(obj.getDataInicioPeriodoEntregaRequisicao(), obj.getDataFimPeriodoEntregaRequisicao(), "requisicao.dataRequisicao", false));

		sqlFiltroCategoriaDespesaArvoreInferior(obj, sql);
		sqlFiltroCategoriaProdutoArvoreInferior(obj, sql);
		sqlFiltroCentroResultadoArvoreInferior(obj, sql);

		return sql.toString();
	}

	private void validarDadosPlanoOrcamentario(RelatorioSEIDecidirVO relatorioSEIDecidirVO) throws ConsistirException {
		if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.PLANO_ORCAMENTARIO)) {
			if (!Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getPlanosOrcamentarioVOs())) {
				throw new ConsistirException(UteisJSF.internacionalizar("ms_RelatorioSEIDecidir_planoOrcamentario"));
			}
			
			if (relatorioSEIDecidirVO.getConsiderarCategoriaDespesaArvoreInferior() && !Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getCategoriaDespesaVOs())) {
				throw new ConsistirException(UteisJSF.internacionalizar("ms_RelatorioSEIDecidir_CategoriaDespesa"));
			}
			if (relatorioSEIDecidirVO.getConsiderarCategoriaProdutoArvoreInferior() & !Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getCategoriasProdutoVOs())) {
				throw new ConsistirException(UteisJSF.internacionalizar("ms_RelatorioSEIDecidir_CategoriaProduto"));
			}
			if (relatorioSEIDecidirVO.getConsiderarCentroResultadoArvoreInferior() && !Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getCategoriaDespesaVOs())) {
				throw new ConsistirException(UteisJSF.internacionalizar("ms_RelatorioSEIDecidir_CentroResultado"));
			}
		}
	}

	private String realizarGeracaoFromModuloPlanoOrcamentario(RelatorioSEIDecidirVO relatorioSEIDecidirVO) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" from solicitacaoorcamentoplanoorcamentario ");
		sql.append(" inner join planoorcamentario on solicitacaoorcamentoplanoorcamentario.planoorcamentario = planoorcamentario.codigo");	
		sql.append(" inner join itemsolicitacaoorcamentoplanoorcamentario on solicitacaoorcamentoplanoorcamentario.codigo = itemsolicitacaoorcamentoplanoorcamentario.solicitacaoorcamentoplanoorcamentario");

		sql.append(" left join requisicao on requisicao.categoriadespesa = itemsolicitacaoorcamentoplanoorcamentario.categoriadespesa");
		sql.append(" 	and requisicao.unidadeensino = solicitacaoorcamentoplanoorcamentario.unidadeensino");
		sql.append(" 	and planoorcamentario.datainicio <= requisicao.datarequisicao");
		sql.append(" 	and  planoorcamentario.datafinal >= requisicao.datarequisicao");
		sql.append(" left join funcionariocargo on funcionariocargo.codigo = requisicao.funcionariocargo");
		sql.append(" left join cargo on funcionariocargo.cargo = cargo.codigo");
		sql.append(" left join departamento on ((solicitacaoorcamentoplanoorcamentario.departamento is not null and departamento.codigo = solicitacaoorcamentoplanoorcamentario.departamento) ");
		sql.append(" 	or  (requisicao.departamento is null and funcionariocargo.departamento is not null and departamento.codigo = funcionariocargo.departamento)");
		sql.append(" 	or (requisicao.departamento is null and funcionariocargo.departamento is null and departamento.codigo = cargo.departamento))");
		sql.append(" 	and departamento.codigo = solicitacaoorcamentoplanoorcamentario.departamento");

		sql.append(" left join requisicaoitem on requisicaoitem.requisicao = requisicao.codigo ");
		sql.append(" and requisicaoitem.itemsolicitacaoorcamentoplanoorcamentario = itemsolicitacaoorcamentoplanoorcamentario.codigo");
		sql.append(" inner join categoriadespesa on categoriadespesa.codigo = itemsolicitacaoorcamentoplanoorcamentario.categoriadespesa");
		sql.append(" left join categoriaproduto on categoriaproduto.codigo = requisicao.categoriaproduto");
		sql.append(" left join centroresultado on centroresultado.codigo = requisicao.centroresultadoadministrativo");
		return sql.toString();
	}
	
	private String adicionarFiltroRequerimento(RelatorioSEIDecidirVO relatorioSEIDecidirVO) {
		StringBuilder sqlStr = new StringBuilder();
		if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoFiltroPeriodo()) {
			if(relatorioSEIDecidirVO.getFiltrarPeriodoPor().equals("dtAbertura")) {
				sqlStr.append("and CAST(requerimento.data AS DATE) between '").append(Uteis.getDataJDBC(relatorioSEIDecidirVO.getDataInicio())).append("' and '").append(Uteis.getDataJDBC(relatorioSEIDecidirVO.getDataFim())).append("' ");
			}else if(relatorioSEIDecidirVO.getFiltrarPeriodoPor().equals("dtConclusao")) {
				sqlStr.append("and CAST(requerimento.dataFinalizacao AS DATE) between '").append(Uteis.getDataJDBC(relatorioSEIDecidirVO.getDataInicio())).append("' and '").append(Uteis.getDataJDBC(relatorioSEIDecidirVO.getDataFim())).append("' ");
			}else if(relatorioSEIDecidirVO.getFiltrarPeriodoPor().equals("dtAtendimento")) {
				sqlStr.append("and exists (select codigo from requerimentoHistorico where requerimentoHistorico.requerimento = requerimento.codigo and CAST(requerimentoHistorico.dataEntradaDepartamento AS DATE)  between '").append(Uteis.getDataJDBC(relatorioSEIDecidirVO.getDataInicio())).append("' and '").append(Uteis.getDataJDBC(relatorioSEIDecidirVO.getDataFim())).append("')");
			}else if(relatorioSEIDecidirVO.getFiltrarPeriodoPor().equals("dtPrevisaoConclusao")) {
				sqlStr.append("and CAST(requerimento.dataPrevistaFinalizacao AS DATE) between '").append(Uteis.getDataJDBC(relatorioSEIDecidirVO.getDataInicio())).append("' and '").append(Uteis.getDataJDBC(relatorioSEIDecidirVO.getDataFim())).append("' ");
			}
		}
		if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoSituacaoRequerimento()) {
			montarSqlSituacao(relatorioSEIDecidirVO.getFinalizadoDeferido(), relatorioSEIDecidirVO.getFinalizadoIndeferido(), relatorioSEIDecidirVO.getEmExecucao(), relatorioSEIDecidirVO.getPendente(), relatorioSEIDecidirVO.getProntoRetirada(), relatorioSEIDecidirVO.getAtrasado(), sqlStr);
		}
		if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoSituacaoFinanceiraRequerimento()) {
			montarSqlSituacaoFinanceira(relatorioSEIDecidirVO.getAguardandoPagamento(), relatorioSEIDecidirVO.getAguardandoAutorizacaoPagamento(), relatorioSEIDecidirVO.getIsento(), relatorioSEIDecidirVO.getPago(), relatorioSEIDecidirVO.getCanceladoFinanceiro(),  relatorioSEIDecidirVO.getSolicitacaoIsencao(),  relatorioSEIDecidirVO.getSolicitacaoIsencaoDeferido(),  relatorioSEIDecidirVO.getSolicitacaoIsencaoIndeferido(), sqlStr);
		}

//		sqlStr.append(adicionarFiltroCurso(cursoVOs));

		if (!relatorioSEIDecidirVO.getTipoRequerimentoVO().getCodigo().equals(0) && relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoTipoRequerimento()) {
			sqlStr.append("and requerimento.tipoRequerimento = ").append(relatorioSEIDecidirVO.getTipoRequerimentoVO().getCodigo()).append(" ");
		}
		if (Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getSituacaoRequerimentoDepartamento()) && relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoSituacaoRequerimento()) {
			sqlStr.append("and requerimento.situacaoRequerimentoDepartamento = ").append(relatorioSEIDecidirVO.getSituacaoRequerimentoDepartamento()).append(" ");
		}
		if (Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getRequerenteVO().getCodigo()) && relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoRequerente()) {
			sqlStr.append("and pessoa.codigo = ").append(relatorioSEIDecidirVO.getRequerenteVO().getCodigo());
		}
		if (!relatorioSEIDecidirVO.getUnidadeEnsinoVOs().isEmpty() && relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoUnidadeEnsino()) {
			sqlStr.append(" and requerimento.unidadeensino in (");
			int x = 0;
			for (UnidadeEnsinoVO unidadeEnsinoVO : relatorioSEIDecidirVO.getUnidadeEnsinoVOs()) {
				if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
					if (x > 0) {
						sqlStr.append(", ");
					}
					sqlStr.append(unidadeEnsinoVO.getCodigo());
					x++;
				}

			}
			sqlStr.append(" ) ");
		}
		if (relatorioSEIDecidirVO.getFuncionarioVO().getCodigo() != 0 && relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoResponsavel()) {
			sqlStr.append("and requerimento.funcionario = ").append(relatorioSEIDecidirVO.getFuncionarioVO().getCodigo()).append(" ");
		}
//		if (Uteis.isAtributoPreenchido(curso)) {
//			sqlStr.append("and curso.codigo = ").append(curso.getCodigo());
//		}
		if (Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getTurmaVO().getCodigo()) && relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoTurma()) {
			sqlStr.append("and turma.codigo = ").append(relatorioSEIDecidirVO.getTurmaVO().getCodigo());
		}
		if (Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getTurmaReposicao().getCodigo()) && relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoTurmaReposicao()) {
			if(relatorioSEIDecidirVO.getTurmaReposicao().getTurmaAgrupada()) {
				sqlStr.append("and exists (select ta.codigo form turmaagrupada where requerimento.turmareposicao = turmaagrupada.turma and turmaagrupada.turmaorigem = ").append(relatorioSEIDecidirVO.getTurmaReposicao().getCodigo()).append(") ");
			}else {
				sqlStr.append("and requerimento.turmareposicao = ").append(relatorioSEIDecidirVO.getTurmaReposicao().getCodigo());
			}
		}
		if (relatorioSEIDecidirVO.getDepartamentoVO().getCodigo() != 0 && relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoDepartamentoResponsavel()) {
			sqlStr.append("and requerimento.departamentoresponsavel = ").append(relatorioSEIDecidirVO.getDepartamentoVO().getCodigo()).append(" ");
		}
		if (Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getDisciplinaVO().getCodigo()) && relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoDisciplina()) {
			sqlStr.append("and (requerimento.disciplina = ").append(relatorioSEIDecidirVO.getDisciplinaVO().getCodigo()).append(" ");
			sqlStr.append("or (exists (select  requerimentodisciplina.disciplina from requerimentodisciplina where requerimentodisciplina.requerimento = requerimento.codigo and requerimentodisciplina.disciplina =  ").append(relatorioSEIDecidirVO.getDisciplinaVO().getCodigo()).append(")) ");
			sqlStr.append("or (exists (select  requerimentoDisciplinasAproveitadas.disciplina from requerimentoDisciplinasAproveitadas where requerimentoDisciplinasAproveitadas.requerimento = requerimento.codigo and requerimentoDisciplinasAproveitadas.disciplina =  ").append(relatorioSEIDecidirVO.getDisciplinaVO().getCodigo()).append(")) ");
			sqlStr.append(") ");
		}
		if(Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getCoordenadorVO().getCodigo()) && relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoCoordenador()) {
			sqlStr.append("and coordenador.codigo = ").append(relatorioSEIDecidirVO.getCoordenadorVO().getCodigo());
		}
		return sqlStr.toString();
	}
	
	private void montarSqlSituacao(Boolean finalizadoDeferido, Boolean finalizadoIndeferido, Boolean emExecucao, Boolean pendente, Boolean prontoRetirada, Boolean atrasado, StringBuilder sqlStr) {
		if (finalizadoDeferido || finalizadoIndeferido || emExecucao || pendente || prontoRetirada || atrasado) {
			sqlStr.append("and (");
		}
		boolean possuiFiltroPorSituacao = false;
		if (finalizadoDeferido || finalizadoIndeferido || emExecucao || pendente || prontoRetirada) {
			possuiFiltroPorSituacao = true;
			sqlStr.append("requerimento.situacao in (");
			int filtros = 0;
			if (finalizadoDeferido) {
				sqlStr.append("'FD'");
				filtros++;
			}
			if (finalizadoIndeferido) {
				if (filtros > 0) {
					sqlStr.append(",");
				}
				sqlStr.append("'FI'");
				filtros++;
			}
			if (emExecucao) {
				if (filtros > 0) {
					sqlStr.append(",");
				}
				sqlStr.append("'EX'");
				filtros++;
			}
			if (pendente) {
				if (filtros > 0) {
					sqlStr.append(",");
				}
				sqlStr.append("'PE'");
				filtros++;
			}
			if (prontoRetirada) {
				if (filtros > 0) {
					sqlStr.append(",");
				}
				sqlStr.append("'PR'");
				filtros++;
			}
			sqlStr.append(")");
		}
		if (atrasado) {
			if (possuiFiltroPorSituacao) {
				sqlStr.append(" or ");
			}
			sqlStr.append(" ((requerimento.dataprevistafinalizacao < CURRENT_TIMESTAMP ");
			sqlStr.append(" and (requerimento.situacao = 'PE' or requerimento.situacao = 'EX' or requerimento.situacao = 'AP' or requerimento.situacao = 'PR')))");
		}
		if (finalizadoDeferido || finalizadoIndeferido || emExecucao || pendente || prontoRetirada || atrasado) {
			sqlStr.append(")");
		}
	}
	
	private void montarSqlSituacaoFinanceira(Boolean aguardandoPagamento, Boolean aguardandoAutorizacaoPagamento, Boolean isento, Boolean pago, Boolean canceladoFinanceiro, Boolean solicitacaoIsencao, Boolean solicitacaoIsencaoDeferido, Boolean solicitacaoIsencaoIndeferido, StringBuilder sqlStr) {
		String andOr = " AND ( ";
		if (aguardandoPagamento || isento || pago || aguardandoAutorizacaoPagamento || canceladoFinanceiro) {
			if((solicitacaoIsencao || solicitacaoIsencaoDeferido || solicitacaoIsencaoIndeferido)) {				
				sqlStr.append("and ( requerimento.situacaofinanceira in (");
			}else {
				sqlStr.append("and requerimento.situacaofinanceira in (");
			}
			int filtros = 0;
			if (aguardandoPagamento) {
				if (filtros > 0) {
					sqlStr.append(",");
				}
				sqlStr.append("'PE'");
				filtros++;
			}
			if (aguardandoAutorizacaoPagamento) {
				if (filtros > 0) {
					sqlStr.append(",");
				}
				sqlStr.append("'AP'");
				filtros++;
			}
			if (isento) {
				if (filtros > 0) {
					sqlStr.append(",");
				}
				sqlStr.append("'IS'");
				filtros++;
			}
			if (pago) {
				if (filtros > 0) {
					sqlStr.append(",");
				}
				sqlStr.append("'PG'");
				filtros++;
			}
			if (canceladoFinanceiro) {
				if (filtros > 0) {
					sqlStr.append(",");
				}
				sqlStr.append("'CA'");
				filtros++;
			}
			sqlStr.append(")");
			andOr = " or ";
		}
		
		if(solicitacaoIsencao || solicitacaoIsencaoDeferido || solicitacaoIsencaoIndeferido) {				
			if(solicitacaoIsencao) {
				sqlStr.append(andOr).append(" (requerimento.situacaoFinanceira not in ('PG', 'IS') ");
				sqlStr.append(" AND requerimento.situacaoIsencaoTaxa = '").append(SituacaoSolicitacaoIsencaoTaxaRequerimentoEnum.AGUARDANDO_RESPOSTA.name()).append("') ");
				andOr = " or ";
			}
			if(solicitacaoIsencaoDeferido) {				
				sqlStr.append(andOr).append(" (requerimento.situacaoIsencaoTaxa = '").append(SituacaoSolicitacaoIsencaoTaxaRequerimentoEnum.DEFERIDO.name()).append("') ");
				andOr = " or ";
			}
			if(solicitacaoIsencaoIndeferido) {				
				sqlStr.append(andOr).append(" (requerimento.situacaoIsencaoTaxa = '").append(SituacaoSolicitacaoIsencaoTaxaRequerimentoEnum.INDEFERIDO.name()).append("') ");
				andOr = " or ";
			}
			sqlStr.append(" ) ");
		}
	}
	
	private String realizarGeracaoFromModuloProcessoSeletivo(RelatorioSEIDecidirVO relatorioSEIDecidirVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder(" ");
		sqlStr.append(" FROM procseletivo ");
		if(relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getNivelDetalhamento().equals(RelatorioSEIDecidirNivelDetalhamentoEnum.INSCRICAO)) {
			sqlStr.append(" left join inscricao on inscricao.procseletivo = procseletivo.codigo ");
			sqlStr.append(" left join pessoa candidato on candidato.codigo = inscricao.candidato ");
			sqlStr.append(" left join unidadeensino on unidadeensino.codigo = inscricao.unidadeEnsino ");
			sqlStr.append(" left join unidadeEnsinoCurso uec1 on uec1.codigo = inscricao.cursoopcao1 ");
			sqlStr.append(" left join unidadeEnsinoCurso uec2 on uec2.codigo = inscricao.cursoopcao2 ");
			sqlStr.append(" left join unidadeEnsinoCurso uec3 on uec3.codigo = inscricao.cursoopcao3 ");
			sqlStr.append(" left join curso cursoOpcao1 on cursoOpcao1.codigo = uec1.curso ");
			sqlStr.append(" left join eixocurso as eixoCursoOpcao1 on cursoOpcao1.eixocurso = eixoCursoOpcao1.codigo ");
			sqlStr.append(" left join curso cursoOpcao2 on cursoOpcao2.codigo = uec2.curso ");
			sqlStr.append(" left join eixocurso as eixoCursoOpcao2 on cursoOpcao2.eixocurso = eixoCursoOpcao2.codigo ");
			sqlStr.append(" left join curso cursoOpcao3 on cursoOpcao3.codigo = uec3.curso ");
			sqlStr.append(" left join eixocurso as eixoCursoOpcao3 on cursoOpcao3.eixocurso = eixoCursoOpcao3.codigo ");
			sqlStr.append(" left join itemprocseletivodataprova on itemprocseletivodataprova.codigo = inscricao.itemProcessoSeletivoDataProva ");
			sqlStr.append(" left join resultadoprocessoseletivo on resultadoprocessoseletivo.inscricao = inscricao.codigo ");
			
		}else if(relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getNivelDetalhamento().equals(RelatorioSEIDecidirNivelDetalhamentoEnum.EIXO_CURSO)) { 
			sqlStr.append(" left join procseletivounidadeensino on procseletivounidadeensino.procseletivo = procseletivo.codigo ");
			sqlStr.append(" left join unidadeensino on procseletivounidadeensino.unidadeensino = unidadeensino.codigo ");			
			sqlStr.append(" left join procseletivounidadeensinoeixocurso on procseletivounidadeensinoeixocurso.procseletivounidadeensino = procseletivounidadeensino.codigo ");
			sqlStr.append(" left join eixocurso on procseletivounidadeensinoeixocurso.eixocurso = eixocurso.codigo ");
		}else if(relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getNivelDetalhamento().equals(RelatorioSEIDecidirNivelDetalhamentoEnum.PROCESSO_SELETIVO_CURSO)) { 
			sqlStr.append(" left join procseletivounidadeensino on procseletivounidadeensino.procseletivo = procseletivo.codigo ");
			sqlStr.append(" left join unidadeensino on procseletivounidadeensino.unidadeensino = unidadeensino.codigo ");
			sqlStr.append(" left join procseletivocurso on procseletivocurso.procseletivounidadeensino = procseletivounidadeensino.codigo ");
			sqlStr.append(" left join unidadeensinocurso on procseletivocurso.unidadeensinocurso = unidadeensinocurso.codigo ");
			sqlStr.append(" left join curso on unidadeensinocurso.curso = curso.codigo ");			
			sqlStr.append(" left join eixocurso on curso.eixocurso = eixocurso.codigo ");
		}else if(relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getNivelDetalhamento().equals(RelatorioSEIDecidirNivelDetalhamentoEnum.UNIDADE_ENSINO)) {
			sqlStr.append(" left join procseletivounidadeensino on procseletivounidadeensino.procseletivo = procseletivo.codigo ");
			sqlStr.append(" left join unidadeensino on procseletivounidadeensino.unidadeensino = unidadeensino.codigo ");
		}
		return sqlStr.toString();
	}
	
	private String adicionarFiltroProcessoSeletivo(RelatorioSEIDecidirVO relatorioSEIDecidirVO) {
		StringBuilder sqlStr = new StringBuilder(" ");
		if (!relatorioSEIDecidirVO.getProcSeletivoVO().getCodigo().equals(0) && relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoProcessoSeletivo()) {
			sqlStr.append(" and procseletivo.codigo = ").append(relatorioSEIDecidirVO.getProcSeletivoVO().getCodigo());
		}
		if (!relatorioSEIDecidirVO.getUnidadeEnsinoCursoVO().getUnidadeEnsino().equals(0) && relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoUnidadeEnsino()) {
			sqlStr.append(" and uec1.unidadeEnsino = ").append(relatorioSEIDecidirVO.getUnidadeEnsinoCursoVO().getUnidadeEnsino());
		}
		if (!relatorioSEIDecidirVO.getUnidadeEnsinoCursoVO().getCurso().getCodigo().equals(0) && relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoCurso()) {
			sqlStr.append(" and uec1.curso = ").append(relatorioSEIDecidirVO.getUnidadeEnsinoCursoVO().getCurso().getCodigo());
		}
		if (relatorioSEIDecidirVO.getFiltroRelatorioProcessoSeletivoVO() != null && relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoSituacaoInscricao()) {
			sqlStr.append("  ").append(adicionarFiltroRelatorioProcessoSeletivo(relatorioSEIDecidirVO.getFiltroRelatorioProcessoSeletivoVO(), "inscricao"));
		}
		if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoSituacaoFinanceiraProcessoSeletivo()) {
			if (!relatorioSEIDecidirVO.getInscricaoVO().getSituacao().equals("AM")) {
				if (relatorioSEIDecidirVO.getInscricaoVO().getSituacao().equals("CO")) {
					sqlStr.append(" and inscricao.situacao = 'CO' ");
				} else {
					sqlStr.append(" and inscricao.situacao = 'PF' ");
				}
			}
		}
		if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoSituacaoResultadoProcessoSeletivo()) {
			if (relatorioSEIDecidirVO.getSituacaoResultadoProcessoSeletivo().equals(SituacaoResultadoProcessoSeletivoEnum.APROVADO)) {
				sqlStr.append(" AND ((resultadoprocessoseletivo.resultadoprimeiraopcao = 'AP')");
				sqlStr.append(" OR (resultadoprocessoseletivo.resultadosegundaopcao = 'AP' AND resultadoprocessoseletivo.resultadoprimeiraopcao <> 'AP' )");
				sqlStr.append(" OR (resultadoprocessoseletivo.resultadoterceiraopcao = 'AP' AND resultadoprocessoseletivo.resultadosegundaopcao <> 'AP' AND resultadoprocessoseletivo.resultadoprimeiraopcao <> 'AP'))");
			}
			
			if (relatorioSEIDecidirVO.getSituacaoResultadoProcessoSeletivo().equals(SituacaoResultadoProcessoSeletivoEnum.APROVADO_1_OPCAO)) {
				sqlStr.append(" AND resultadoprocessoseletivo.resultadoprimeiraopcao = 'AP' ");
				sqlStr.append(" AND case when resultadoprocessoseletivo.resultadosegundaopcao is not null then resultadoprocessoseletivo.resultadosegundaopcao <> 'AP' else true end ");
				sqlStr.append(" AND case when resultadoprocessoseletivo.resultadoterceiraopcao is not null then resultadoprocessoseletivo.resultadoterceiraopcao <> 'AP' else true end ");
			}
			
			if (relatorioSEIDecidirVO.getSituacaoResultadoProcessoSeletivo().equals(SituacaoResultadoProcessoSeletivoEnum.APROVADO_2_OPCAO)) {
				sqlStr.append(" AND resultadoprocessoseletivo.resultadosegundaopcao = 'AP' ");
				sqlStr.append(" AND case when resultadoprocessoseletivo.resultadoprimeiraopcao is not null then resultadoprocessoseletivo.resultadoprimeiraopcao <> 'AP' else true end ");
				sqlStr.append(" AND case when resultadoprocessoseletivo.resultadoterceiraopcao is not null then resultadoprocessoseletivo.resultadoterceiraopcao <> 'AP' else true end ");
			}
			
			if (relatorioSEIDecidirVO.getSituacaoResultadoProcessoSeletivo().equals(SituacaoResultadoProcessoSeletivoEnum.APROVADO_3_OPCAO)) {
				sqlStr.append(" AND resultadoprocessoseletivo.resultadoterceiraopcao = 'AP' ");
				sqlStr.append(" AND case when resultadoprocessoseletivo.resultadoprimeiraopcao is not null then resultadoprocessoseletivo.resultadoprimeiraopcao <> 'AP' else true end ");
				sqlStr.append(" AND case when resultadoprocessoseletivo.resultadosegundaopcao is not null then resultadoprocessoseletivo.resultadosegundaopcao <> 'AP' else true end ");
			}
			
			if (relatorioSEIDecidirVO.getSituacaoResultadoProcessoSeletivo().equals(SituacaoResultadoProcessoSeletivoEnum.REPROVADO)) {
				sqlStr.append(" AND ((inscricao.cursoopcao1 is not null AND resultadoprocessoseletivo.resultadoprimeiraopcao = 'RE' )");
				sqlStr.append(" OR (inscricao.cursoopcao2 is not null and resultadoprocessoseletivo.resultadosegundaopcao = 'RE')");
				sqlStr.append(" OR (inscricao.cursoopcao3 is not null AND resultadoprocessoseletivo.resultadoterceiraopcao = 'RE'))");
			}
		}
		
		if (Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getDataProvaInicio()) && Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getDataProvaFim()) && relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoDataProvaInicio() && relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoDataProvaFim()) {
			sqlStr.append(" AND CAST(itemprocseletivodataprova.dataprova AS DATE)  BETWEEN '").append(relatorioSEIDecidirVO.getDataProvaInicio()).append("' AND '").append(relatorioSEIDecidirVO.getDataProvaFim()).append("' ");
		} else if(Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getDataProvaInicio()) && !Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getDataProvaFim())) {
			if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoDataProvaInicio()) {
				sqlStr.append(" AND CAST(itemprocseletivodataprova.dataprova AS DATE)  >= '").append(relatorioSEIDecidirVO.getDataProvaInicio()).append("' ");
			}			
		}else if(!Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getDataProvaInicio()) && Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getDataProvaFim())){
			if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoDataProvaFim()) {
				sqlStr.append(" AND CAST(itemprocseletivodataprova.dataprova AS DATE)  <= '").append(relatorioSEIDecidirVO.getDataProvaFim()).append("' ");
			}
		}
		return sqlStr.toString();
	}
	
	 public StringBuilder adicionarFiltroRelatorioProcessoSeletivo(FiltroRelatorioProcessoSeletivoVO filtroRelatorioProcessoSeletivoVO, String keyEntidade) {
			StringBuilder sqlStr = new StringBuilder("");
			keyEntidade = keyEntidade.trim();
			if (filtroRelatorioProcessoSeletivoVO.getAtivo() || filtroRelatorioProcessoSeletivoVO.getCancelado() || filtroRelatorioProcessoSeletivoVO.getCanceladoOutraInscricao() || filtroRelatorioProcessoSeletivoVO.getNaoCompareceu()) {
				sqlStr.append(" and ").append(keyEntidade).append(".situacaoInscricao in (");
				boolean virgula = false;
				if (filtroRelatorioProcessoSeletivoVO.getAtivo()) {
					sqlStr.append(virgula ? "," : "").append("'").append(SituacaoInscricaoEnum.ATIVO).append("'");
					virgula = true;
				}
				if (filtroRelatorioProcessoSeletivoVO.getCancelado()) {
					sqlStr.append(virgula ? "," : "").append("'").append(SituacaoInscricaoEnum.CANCELADO).append("'");
					virgula = true;
				}
				if (filtroRelatorioProcessoSeletivoVO.getCanceladoOutraInscricao()) {
					sqlStr.append(virgula ? "," : "").append("'").append(SituacaoInscricaoEnum.CANCELADO_OUTRA_INSCRICAO).append("'");
					virgula = true;
				}
				if (filtroRelatorioProcessoSeletivoVO.getNaoCompareceu()) {
					sqlStr.append(virgula ? "," : "").append("'").append(SituacaoInscricaoEnum.NAO_COMPARECEU).append("'");
					virgula = true;
				}
				sqlStr.append(" ) ");
			}
			if(!sqlStr.toString().isEmpty()){
				return sqlStr; 
			}
			return new StringBuilder(" and 1=1 ");
		}
	 
	 public StringBuilder adicionarFiltroDocumentoAssinado(RelatorioSEIDecidirVO relatorioSEIDecidirVO, String filtroAssinado) {
			StringBuilder sqlStr = new StringBuilder("");
			if (filtroAssinado.equals("NAO_CONTROLA") || !relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoSituacaoAceiteEletronicoContrato()) {
				return sqlStr;
			}else if(filtroAssinado.equals("TODOS")) {
				sqlStr.append(" and documentoassinadopessoa.situacaodocumentoassinadopessoa in ('PENDENTE','ASSINADO','REJEITADO')");
			}
			else {
				sqlStr.append(" and documentoassinadopessoa.situacaodocumentoassinadopessoa = '").append(filtroAssinado).append("'");
			}
			return sqlStr;
	 }
	 
	 public StringBuilder adicionarFiltroPeriodoAceiteContrato(RelatorioSEIDecidirVO relatorioSEIDecidirVO) {
		 	boolean and = false;
		 	StringBuilder sqlStr = new StringBuilder("");
		 	if (!relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoSituacaoAceiteEletronicoContrato()) {
		 		return sqlStr;
		 	}
		 	if (!relatorioSEIDecidirVO.getTipoFiltroDocumentoAssinado().equals("NAO_CONTROLA") && (Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getDataDocumentoInicio()) || Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getDataDocumentoFim()))) {
		 		sqlStr.append(" and case when documentoassinadopessoa.situacaodocumentoassinadopessoa = 'ASSINADO'");
		 		sqlStr.append(" then ");
		 		if (Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getDataDocumentoInicio())) {
		 			sqlStr.append(" documentoassinadopessoa.dataassinatura::date >= '").append(Uteis.getDataJDBC(relatorioSEIDecidirVO.getDataDocumentoInicio())).append("'");
		 			and = true;
				}
		 		if (Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getDataDocumentoFim())) {
		 			sqlStr.append(and ? " and " : "");
		 			sqlStr.append(" documentoassinadopessoa.dataassinatura::date <= '").append(Uteis.getDataJDBC(relatorioSEIDecidirVO.getDataDocumentoFim())).append("'");
				}
		 		sqlStr.append(" when documentoassinadopessoa.situacaodocumentoassinadopessoa = 'REJEITADO'");
		 		sqlStr.append(" then ");
		 		if (Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getDataDocumentoInicio())) {
		 			sqlStr.append(" documentoassinadopessoa.datarejeicao::date >= '").append(Uteis.getDataJDBC(relatorioSEIDecidirVO.getDataDocumentoInicio())).append("'");
				}
		 		if (Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getDataDocumentoFim())) {
		 			sqlStr.append(and ? " and " : "");
		 			sqlStr.append(" documentoassinadopessoa.datarejeicao::date <= '").append(Uteis.getDataJDBC(relatorioSEIDecidirVO.getDataDocumentoFim())).append("'");
				}
		 		sqlStr.append(" else ");
		 		if (Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getDataDocumentoInicio())) {
		 			sqlStr.append(" documentoassinadopessoa.datasolicitacao::date >= '").append(Uteis.getDataJDBC(relatorioSEIDecidirVO.getDataDocumentoInicio())).append("'");
				}
		 		if (Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getDataDocumentoFim())) {
		 			sqlStr.append(and ? " and " : "");
		 			sqlStr.append(" documentoassinadopessoa.datasolicitacao::date <= '").append(Uteis.getDataJDBC(relatorioSEIDecidirVO.getDataDocumentoFim())).append("'");
				}
		 		sqlStr.append(" end");
			}
			return sqlStr;
			
		}
	 

	 private String adicionarFiltroPeriodoLetivo(RelatorioSEIDecidirVO relatorioSEIDecidirVO, Integer filtroPeriodoLetivo) {
		 if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoPeriodoLetivo()) {
			 return filtroPeriodoLetivo != null && filtroPeriodoLetivo != 0 ? "AND periodoletivomatriculaperiodo.periodoletivo = '" + filtroPeriodoLetivo + "' " : "";
		 }
		 return "";
	 }

		private String realizarGeracaoFromModuloEstagio(RelatorioSEIDecidirVO relatorioSEIDecidirVO) throws Exception {
			StringBuilder sql = new StringBuilder(" ");
			sql.append(" from matricula ");
			sql.append(" inner join curso on curso.codigo = matricula.curso ");
			sql.append(" left join eixocurso on curso.eixocurso = eixocurso.codigo ");
			sql.append(" inner join unidadeensino on unidadeensino.codigo = matricula.unidadeensino ");
			sql.append(" left join gradecurricular as gradecurricularatual on gradecurricularatual.codigo = matricula.gradecurricularatual ");
			sql.append(" left join estagio on estagio.matricula = matricula.matricula ");
			sql.append(" left join tipoconcedente on tipoconcedente.codigo = estagio.tipoconcedente");
			sql.append(" left join gradecurricularestagio on gradecurricularestagio.codigo = estagio.gradecurricularestagio ");
			sql.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
			sql.append(" and matriculaperiodo.codigo in (select codigo from matriculaperiodo mp  ");
			sql.append(" where mp.matricula = matricula.matricula and mp.situacaomatriculaperiodo != 'PC' order by (ano|| '/'|| semestre) desc, data desc limit 1) ");
			if(Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getValorBuscaInicio()) || Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getValorBuscaFinal())) {
				sql.append(" INNER JOIN LATERAL matriculaIntegralizacaoCurricular(matricula.matricula) AS integralizacao ON true ") ;

			}
			if (realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "aluno") 
					|| realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "alunocidade") 
					|| realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "alunoestado")) {				
				sql.append(" inner join pessoa aluno on aluno.codigo = matricula.aluno ");
				
				if (realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "alunocidade") && !realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "alunoestado")) {
					sql.append(" left join cidade as alunocidade on aluno.cidade = alunocidade.codigo ");
				}
				if (realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "alunoestado")) {
					sql.append(" left join cidade as alunocidade on aluno.cidade = alunocidade.codigo ");
					sql.append(" left join estado as alunoestado on alunocidade.estado = alunoestado.codigo ");
				}
			}
			
			if (realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "grupopessoaitem")) {
				sql.append(" left join grupopessoaitem on grupopessoaitem.codigo = estagio.grupopessoaitem ");
				sql.append(" left join pessoa pessoa_gpi on pessoa_gpi.codigo = grupopessoaitem.pessoa ");
			}
			
			if (realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "usuariodeferimento")) {
				sql.append(" left join usuario usuario_rd on usuario_rd.codigo = estagio.responsaveldeferimento ");
				sql.append(" left join pessoa pessoa_rd on pessoa_rd.codigo = usuario_rd.pessoa ");
			}
			if (realizaVerificacaoTabelaUtilizada(relatorioSEIDecidirVO, "usuarioinddeferimento")) {
				sql.append(" left join usuario usuario_ri on usuario_ri.codigo = estagio.responsavelindeferimento ");
				sql.append(" left join pessoa pessoa_ri on pessoa_ri.codigo = usuario_ri.pessoa ");
			}
			return sql.toString();
		}

	 private String realizarGeracaoFromModuloBibliotecaNivelDetalhamentoEmprestimo(RelatorioSEIDecidirVO relatorioSEIDecidirVO) throws Exception {
			StringBuilder sqlStr = new StringBuilder(" ");
			sqlStr.append(" from emprestimo ");
			sqlStr.append(" inner join itememprestimo on itememprestimo.emprestimo = emprestimo.codigo ");
			sqlStr.append(" left join pessoa on pessoa.codigo = emprestimo.pessoa ");
			sqlStr.append(" left join biblioteca on biblioteca.codigo = emprestimo.biblioteca ");
			sqlStr.append(" left join usuario atendente on atendente.codigo = emprestimo.atendente ");
			sqlStr.append(" left join unidadeensino on unidadeensino.codigo = emprestimo.unidadeensino ");
			sqlStr.append(" left join usuario responsavelDevolucao on responsavelDevolucao.codigo = itememprestimo.responsaveldevolucao ");
			sqlStr.append(" inner join exemplar on exemplar.codigo = itememprestimo.exemplar ");
			sqlStr.append(" inner join catalogo on catalogo.codigo = exemplar.catalogo ");
			sqlStr.append(" left join catalogoareaconhecimento on catalogoareaconhecimento.catalogo = catalogo.codigo ");
			sqlStr.append(" left join areaconhecimento on areaconhecimento.codigo = catalogoareaconhecimento.areaconhecimento ");
			sqlStr.append(" left join secao on secao.codigo = exemplar.secao ");
			return sqlStr.toString();
	 }
	 
	 
	 private String adicionarFiltroBiblioteca(RelatorioSEIDecidirVO relatorioSEIDecidirVO) {
			StringBuilder sqlStr = new StringBuilder(" ");		
			if (relatorioSEIDecidirVO.getUnidadeEnsinoVOs().stream().anyMatch(p->p.getFiltrarUnidadeEnsino()) && relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoUnidadeEnsino()) {
				sqlStr.append(adicionarFiltroUnidadeEnsino(relatorioSEIDecidirVO));
			}
			if (!relatorioSEIDecidirVO.getBibliotecaVO().getCodigo().equals(0) && relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoBiblioteca()) {
				sqlStr.append(" AND biblioteca.codigo = ").append(relatorioSEIDecidirVO.getBibliotecaVO().getCodigo());
			}
			if (!relatorioSEIDecidirVO.getCatalogoVO().getCodigo().equals(0) && relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoCatalogoPeriodico()) {
				sqlStr.append(" AND catalogo.codigo = ").append(relatorioSEIDecidirVO.getCatalogoVO().getCodigo());
			}
			if (!relatorioSEIDecidirVO.getTituloCatalogo().equals("") && relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoTitulo()) {
				sqlStr.append(" AND trim(sem_acentos(catalogo.titulo)) ilike sem_acentos(trim('").append(relatorioSEIDecidirVO.getTituloCatalogo()).append("'))");
			}
			if (!relatorioSEIDecidirVO.getSecao().getCodigo().equals(0) && relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoSecao()) {
				sqlStr.append(" AND secao.codigo = ").append(relatorioSEIDecidirVO.getSecao().getCodigo());
			}
			if (!relatorioSEIDecidirVO.getTipoEntrada().equals("") && relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoFormaEntrada()) {
				sqlStr.append(" AND trim(sem_acentos(tipoEntrada)) ilike sem_acentos(trim('").append(relatorioSEIDecidirVO.getTipoEntrada()).append("'))");
			}
			if (!relatorioSEIDecidirVO.getAreaConhecimentoVO().getCodigo().equals(0) && relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoAreaConhecimento()) {
				sqlStr.append(" AND areaConhecimento.codigo = ").append(relatorioSEIDecidirVO.getAreaConhecimentoVO().getCodigo());
			}
			
			//Situação Emprestimo
			if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getNivelDetalhamento().equals(RelatorioSEIDecidirNivelDetalhamentoEnum.EMPRESTIMO) && relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoSituacaoEmprestimo()) {
				if (relatorioSEIDecidirVO.getSituacaoEmprestimo().equals("EM")) {
					sqlStr.append(" AND itemEmprestimo.situacao = '").append("EX' ");
				}
				if (relatorioSEIDecidirVO.getSituacaoEmprestimo().equals("DE")) {
					sqlStr.append(" AND itemEmprestimo.situacao = '").append("DE' ");
				}
				if (relatorioSEIDecidirVO.getSituacaoEmprestimo().equals("ECA")) {
					sqlStr.append(" AND itemEmprestimo.situacao = '").append("EX'").append(" AND itemEmprestimo.dataprevisaodevolucao < CURRENT_DATE ");
				}
				if (relatorioSEIDecidirVO.getSituacaoEmprestimo().equals("ESA")) {
					sqlStr.append(" AND itemEmprestimo.situacao = '").append("EX'").append(" AND CURRENT_DATE < itemEmprestimo.dataprevisaodevolucao ");
				}
				if (relatorioSEIDecidirVO.getSituacaoEmprestimo().equals("DCA")) {
					sqlStr.append(" AND itemEmprestimo.situacao = '").append("DE'").append(" AND itemEmprestimo.dataprevisaodevolucao < itemEmprestimo.datadevolucao ");
				}
				if (relatorioSEIDecidirVO.getSituacaoEmprestimo().equals("DSA")) {
					sqlStr.append(" AND itemEmprestimo.situacao = '").append("DE'").append(" AND itemEmprestimo.datadevolucao < itemEmprestimo.dataprevisaodevolucao ");
				}
			}
			//Data Emprestimo
			if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getNivelDetalhamento().equals(RelatorioSEIDecidirNivelDetalhamentoEnum.EMPRESTIMO)) {
				if (Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getDataInicioEmprestimo()) || Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getDataFimEmprestimo())) {
					if (Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getDataInicioEmprestimo()) && !Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getDataFimEmprestimo()) && relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoDataInicioEmprestimo()) {
						sqlStr.append(" AND emprestimo.data >=  '").append(Uteis.getDataJDBC(relatorioSEIDecidirVO.getDataInicioEmprestimo())).append(" 00:00:00' ");
					} else if (!Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getDataInicioEmprestimo()) && Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getDataFimEmprestimo()) && relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoDataFimEmprestimo()) {
						sqlStr.append(" AND emprestimo.data <=  '").append(Uteis.getDataJDBC(relatorioSEIDecidirVO.getDataFimEmprestimo())).append(" 23:59:59' ");
					} else {
						if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoDataInicioEmprestimo()) {
							sqlStr.append(" AND emprestimo.data >=  '").append(Uteis.getDataJDBC(relatorioSEIDecidirVO.getDataInicioEmprestimo())).append(" 00:00:00' ");
						}
						if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoDataFimEmprestimo()) {
							sqlStr.append(" AND emprestimo.data <=  '").append(Uteis.getDataJDBC(relatorioSEIDecidirVO.getDataFimEmprestimo())).append(" 23:59:59' ");
						}
					}
				}
			}
//			Data Aquisição exemplar
			if (Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getDataInicioAquisicaoExemplar()) || Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getDataFimAquisicaoExemplar())) {
				if (Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getDataInicioAquisicaoExemplar()) && !Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getDataFimAquisicaoExemplar()) && relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoDataInicioAquisicao()) {
					sqlStr.append(" AND exemplar.dataAquisicao >=  '").append(Uteis.getDataJDBC(relatorioSEIDecidirVO.getDataInicioAquisicaoExemplar())).append(" 00:00:00' ");
				} else if (!Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getDataInicioAquisicaoExemplar()) && Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getDataFimAquisicaoExemplar()) && relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoDataFimAquisicao()) {
					sqlStr.append(" AND exemplar.dataAquisicao <=  '").append(Uteis.getDataJDBC(relatorioSEIDecidirVO.getDataFimAquisicaoExemplar())).append(" 23:59:59' ");
				} else {
					if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoDataInicioAquisicao()) {
						sqlStr.append(" AND exemplar.dataAquisicao >=  '").append(Uteis.getDataJDBC(relatorioSEIDecidirVO.getDataInicioAquisicaoExemplar())).append(" 00:00:00' ");
					}
					if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoDataFimAquisicao()) {
						sqlStr.append(" AND exemplar.dataAquisicao <=  '").append(Uteis.getDataJDBC(relatorioSEIDecidirVO.getDataFimAquisicaoExemplar())).append(" 23:59:59' ");
					}
				}
			}
			if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getNivelDetalhamento().equals(RelatorioSEIDecidirNivelDetalhamentoEnum.EMPRESTIMO) && relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoTipoPessoa()) {
				if (relatorioSEIDecidirVO.getTipoPessoaEnum().equals(TipoPessoa.ALUNO)) {
					sqlStr.append(" AND emprestimo.tipoPessoa = 'AL' ");
				} 
				if (!relatorioSEIDecidirVO.getMatriculaVO().getMatricula().equals("")) {
					sqlStr.append(" AND emprestimo.matricula = '").append(relatorioSEIDecidirVO.getMatriculaVO().getMatricula()).append("' ");
				}
				if (relatorioSEIDecidirVO.getTipoPessoaEnum().equals(TipoPessoa.PROFESSOR)) {
					sqlStr.append(" AND emprestimo.tipoPessoa = 'PR' ");
				}
				if (!relatorioSEIDecidirVO.getProfessorVO().getPessoa().getCodigo().equals(0)) { 
					sqlStr.append(" AND emprestimo.pessoa = ").append(relatorioSEIDecidirVO.getProfessorVO().getPessoa().getCodigo());
				}
				if (relatorioSEIDecidirVO.getTipoPessoaEnum().equals(TipoPessoa.FUNCIONARIO)) {
					sqlStr.append(" AND emprestimo.tipoPessoa = 'FU' ");
				}
				if (!relatorioSEIDecidirVO.getFuncionarioVO().getPessoa().getCodigo().equals(0)) { 
					sqlStr.append(" AND emprestimo.pessoa = ").append(relatorioSEIDecidirVO.getFuncionarioVO().getPessoa().getCodigo());
				}
				if (relatorioSEIDecidirVO.getTipoPessoaEnum().equals(TipoPessoa.MEMBRO_COMUNIDADE)) {
					sqlStr.append(" AND emprestimo.tipoPessoa = 'MC' ");
				}
				if (!relatorioSEIDecidirVO.getPessoaVO().getCodigo().equals(0)) {
					sqlStr.append(" AND emprestimo.pessoa = ").append(relatorioSEIDecidirVO.getPessoaVO().getCodigo());
				}
			}
			return sqlStr.toString();
		 }

		private void realizarGeracaoWhereEstagio(RelatorioSEIDecidirVO relatorioSEIDecidirVO, Map<String, Object> mapaParametrosRelatorio, StringBuilder sql) {
			if (Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getMatriculaVO().getMatricula())) {
				sql.append(" and matricula.matricula = '").append(relatorioSEIDecidirVO.getMatriculaVO().getMatricula()).append("' ");
			}
			if (Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getConcedente())) {
				sql.append(" and sem_acentos((estagio.nomeconcedente)) iLIKE(trim(sem_acentos(").append(relatorioSEIDecidirVO.getConcedente()).append(")))");
			}
			sql.append(" and (").append(adicionarFiltroSituacaoAcademicaMatricula(relatorioSEIDecidirVO.getRelatorioAcademicoVO(), "matricula")).append(") ");
			sql.append(adicionarFiltroGradeCurricularEstagio(relatorioSEIDecidirVO));
			sql.append(adicionarFiltroCurso(relatorioSEIDecidirVO));
			if (relatorioSEIDecidirVO.getUnidadeEnsinoVOs().stream().anyMatch(p->p.getFiltrarUnidadeEnsino())) {
				sql.append(adicionarFiltroUnidadeEnsino(relatorioSEIDecidirVO));
			}
			
			if(Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getValorBuscaInicio(), true) && Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getValorBuscaFinal(), true)) {
				sql.append(" and (((");
				sql.append(TagSEIDecidirMatriculaEnum.MATRICULA_MATRIZ_CURRICULAR_CH_CUMPRIDA_OBRIGATORIA.getCampo());
				sql.append("+");
				sql.append(TagSEIDecidirMatriculaEnum.MATRICULA_MATRIZ_CURRICULAR_CH_CUMPRIDA_OPTATIVA.getCampo());
				sql.append("+");
				sql.append(TagSEIDecidirMatriculaEnum.MATRICULA_MATRIZ_CURRICULAR_CH_CUMPRIDO_ESTAGIO.getCampo());
				sql.append("+");
				sql.append(TagSEIDecidirMatriculaEnum.MATRICULA_MATRIZ_CURRICULAR_CH_CUMPRIDO_ATIVIDADE_COMPLEMENTAR.getCampo());
				sql.append(") * 100) / gradecurricularatual.cargahoraria) ");
				sql.append(" between ").append(relatorioSEIDecidirVO.getValorBuscaInicio()).append(" and ").append(relatorioSEIDecidirVO.getValorBuscaFinal()).append(" ");
			
			}
			
			if(Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getValorBuscaInicio1(), true) && Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getValorBuscaFinal1(), true)) {
				sql.append(" and (select ");
				sql.append(" ((sum(case when (est.situacaoEstagioEnum = 'DEFERIDO' and est.tipoestagio != 'NAO_OBRIGATORIO') then coalesce(est.cargaHorariaDeferida,0) else 0 end) * 100) / ");
				sql.append(" (select sum(coalesce(gradecurricularestagio.cargahorarioobrigatorio,0)) from gradecurricularestagio where gradecurricularestagio.gradecurricular = mat_percentualestagio.gradecurricularatual)) ");
				sql.append(" from estagio est ");
				sql.append(" inner join matricula mat_percentualestagio on mat_percentualestagio.matricula = est.matricula ");
				sql.append(" where est.matricula = matricula.matricula group by mat_percentualestagio.gradecurricularatual) ");
				sql.append(" between ").append(relatorioSEIDecidirVO.getValorBuscaInicio1()).append(" and ").append(relatorioSEIDecidirVO.getValorBuscaFinal1()).append(" ");
			}
		}
		
		private String adicionarFiltroGradeCurricularEstagio(RelatorioSEIDecidirVO relatorioSEIDecidirVO) {
			StringBuilder sql = new StringBuilder("");
			for (GradeCurricularEstagioVO gradeCurricularEstagio : relatorioSEIDecidirVO.getGradeCurricularEstagioVOs()) {
				if (gradeCurricularEstagio.getFiltrarGradeCurricularEstagioVO()) {
					sql.append(sql.length() == 0 ? " and gradecurricularestagio.codigo in(" : ", ").append(gradeCurricularEstagio.getCodigo());
				}
			}
			if (sql.length() > 0) {
				sql.append(") ");
			}
			return sql.toString();
		}
		private String realizarCriacaoArquivoCsv(RelatorioSEIDecidirVO relatorioSEIDecidirVO, TipoRelatorioEnum tipoRelatorio, String urlLogoPadraoRelatorio, boolean gerarFormatoExportacaoDados, String campoSeparador, String extensaoArquivo, SqlRowSet sqlRowSet, UsuarioVO usuarioVO, ProgressBarVO progressBarVO) throws Exception {
			String nomePadrao = (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getNomePadraoArquivo().trim().isEmpty() ? "SEIDECIDIR_" : relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getNomePadraoArquivo().trim());
			String nomeArquivo =  nomePadrao + Uteis.getData(new Date(), "dd_MM_yy_hh_mm_ss") + "_" + usuarioVO.getCodigo() + "."+extensaoArquivo;
			String file  = (Uteis.isAtributoPreenchido(progressBarVO.getCaminhoWebRelatorio()) ? progressBarVO.getCaminhoWebRelatorio() : UteisJSF.getCaminhoWeb()) + File.separator + "relatorio" + File.separator + nomeArquivo;			
			FileOutputStream csvOutputFile = null;		
			PrintWriter pw = null;		
			try {
				csvOutputFile = new FileOutputStream(file);
				pw = new PrintWriter(new OutputStreamWriter(csvOutputFile, "UTF-8"), true);
			progressBarVO.incrementar();
			while (sqlRowSet.next()) {
				progressBarVO.setStatus("Gerando registro "+progressBarVO.getProgresso()+" de "+(progressBarVO.getMaxValue() - 3)+" ("+Uteis.getDoubleFormatado(progressBarVO.getPorcentagem())+"%).");
				String[] data = new String[relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getLayoutRelatorioSeiDecidirCampoVOs().size()];
				for (LayoutRelatorioSeiDecidirCampoVO layoutTag : relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getLayoutRelatorioSeiDecidirCampoVOs()) {
					switch (layoutTag.getTipoCampo()) {
					case MES_ANO:
						if(sqlRowSet.getObject(layoutTag.getAlias()) instanceof java.sql.Date && sqlRowSet.getDate(layoutTag.getAlias()) != null){						
							data[layoutTag.getOrdemApresentacao()-1] = Uteis.getData(sqlRowSet.getDate(layoutTag.getAlias()), "MM/YYYY");
						}else if(sqlRowSet.getObject(layoutTag.getAlias()) instanceof String){
							data[layoutTag.getOrdemApresentacao()-1] = (sqlRowSet.getString(layoutTag.getAlias()));
						}else{
							data[layoutTag.getOrdemApresentacao()-1] = "";
						}					
						
						break;
					case DATA:
						if(sqlRowSet.getDate(layoutTag.getAlias()) != null){
							data[layoutTag.getOrdemApresentacao()-1] = Uteis.getData(sqlRowSet.getDate(layoutTag.getAlias()));							
						}else{
							data[layoutTag.getOrdemApresentacao()-1] = "";
						}						
						break;
					case DOUBLE:
						data[layoutTag.getOrdemApresentacao()-1] = Uteis.getValorTruncadoDeDoubleParaString(sqlRowSet.getDouble(layoutTag.getAlias()), layoutTag.getQtdCasasDecimais());
						break;
					case MOEDA:
						data[layoutTag.getOrdemApresentacao()-1] = Uteis.getValorTruncadoDeDoubleParaString(sqlRowSet.getDouble(layoutTag.getAlias()), 2);
						break;
					case INTEIRO:
						data[layoutTag.getOrdemApresentacao()-1] = ""+sqlRowSet.getInt(layoutTag.getAlias());
						break;
					default:
						if(sqlRowSet.getString(layoutTag.getAlias()) == null){
							data[layoutTag.getOrdemApresentacao()-1] = "";
						}else{
							
							if (layoutTag.getCampo().equals("SITUACAO_HISTORICO")) {
								data[layoutTag.getOrdemApresentacao()-1] = (SituacaoHistorico.getDescricao(sqlRowSet.getString(layoutTag.getAlias())));
							} else if (layoutTag.getCampo().equals("TIPO_HISTORICO")) {
								data[layoutTag.getOrdemApresentacao()-1] = (TipoHistorico.getDescricao(sqlRowSet.getString(layoutTag.getAlias())));
							} else if (layoutTag.getCampo().equals("SITUACAO_REQUERIMENTO")) {
								data[layoutTag.getOrdemApresentacao()-1] = (SituacaoRequerimento.getDescricao(sqlRowSet.getString(layoutTag.getAlias())));
							} else if (layoutTag.getCampo().equals("SITUACAO_FINANCEIRA_REQUERIMENTO")) {
								RequerimentoVO req = new RequerimentoVO();
								req.setSituacaoFinanceira(sqlRowSet.getString(layoutTag.getAlias()));
								data[layoutTag.getOrdemApresentacao()-1] = (req.getSituacaoFinanceira_Apresentar());
							} else {
								data[layoutTag.getOrdemApresentacao()-1] = (sqlRowSet.getString(layoutTag.getAlias()));
							}
							
						}						
						break;
					}

				}
				//datas.add(data);
				pw.println(convertToCSV(data, campoSeparador));				
				progressBarVO.incrementar();
			}
			sqlRowSet = null;
			progressBarVO.setStatus("Salvando arquivo para download.");
//		    try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
//		    	datas.stream().map(t -> convertToCSV(t, campoSeparador))
//		          .forEach(pw::println);
//		    }
				
//		    assertTrue(csvOutputFile.exists());		    
			return nomeArquivo;
			}catch (Exception e) {
				throw e;
			}finally {
				if(csvOutputFile!= null) {
					csvOutputFile.flush();
					csvOutputFile.close();
					
				}
				if(pw!= null) {
					pw.flush();
					pw.close();
					
				}
				
				csvOutputFile =  null;
			}
			
		}
		
		public String convertToCSV(String[] data, String campoSeparador) {
		    return Stream.of(data)
		      .map(this::escapeSpecialCharacters)
		      .collect(Collectors.joining(campoSeparador));
		}
		
		public String escapeSpecialCharacters(String data) {
		    String escapedData = data.replaceAll("\\R", " ");
		    if (data.contains(",") || data.contains("\"") || data.contains("'")) {
		        data = data.replace("\"", "\"\"");
		        escapedData = "\"" + data + "\"";
		    }
		    return escapedData;
		}
	 

	private String adicionarFiltroEstagio(RelatorioSEIDecidirVO relatorioSEIDecidirVO) {
		StringBuilder sql = new StringBuilder(" ");
		if (relatorioSEIDecidirVO.getUnidadeEnsinoVOs().stream().anyMatch(UnidadeEnsinoVO::getFiltrarUnidadeEnsino)) {
			sql.append(adicionarFiltroUnidadeEnsino(relatorioSEIDecidirVO));
		}
		if (relatorioSEIDecidirVO.getCursoVOs().stream().anyMatch(CursoVO::getFiltrarCursoVO)) {
			sql.append(adicionarFiltroCurso(relatorioSEIDecidirVO));
		}
		if (relatorioSEIDecidirVO.getTurnoVOs().stream().anyMatch(TurnoVO::getFiltrarTurnoVO) && Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getTurnoVOs()) ) {
			if (!relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoTurno()) {
				sql.append("");
			}
			sql.append(" and matricula.turno in (0 ");
			for (TurnoVO turno : relatorioSEIDecidirVO.getTurnoVOs()) {
				if (turno.getFiltrarTurnoVO()) {
					sql.append(", ").append(turno.getCodigo());
				}
			}
				sql.append(") ");
		}
		if (Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getTurmaVO().getCodigo()) && relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoTurma()) {
			sql.append(" and MatriculaPeriodo.turma = ").append(relatorioSEIDecidirVO.getTurmaVO().getCodigo());
		}
		if (relatorioSEIDecidirVO.isFiltrarAguardandoAssinatura() || relatorioSEIDecidirVO.isFiltrarDeferido() || relatorioSEIDecidirVO.isFiltrarEmAnalise() || relatorioSEIDecidirVO.isFiltrarEmCorrecao()  || relatorioSEIDecidirVO.isFiltrarIndeferido()|| relatorioSEIDecidirVO.isFiltrarEmRealizacao()) {
		    StringJoiner situacoes = new StringJoiner(" OR ", " AND (", ")");
		    boolean adicionou = false;
		    if (relatorioSEIDecidirVO.isFiltrarAguardandoAssinatura()) {
		        Date ini = relatorioSEIDecidirVO.getDataInicio();
		        Date fim = relatorioSEIDecidirVO.getDataFim();
		        StringBuilder c = new StringBuilder("(estagio.situacaoestagioenum = 'AGUARDANDO_ASSINATURA'");
		        if (ini != null) {
		          c.append(" and estagio.created >= '").append(Uteis.getData(ini, "yyyy-MM-dd")).append("'");
		        }
	            if (fim != null) {   
	             c.append(" and estagio.created <= '").append(Uteis.getData(fim, "yyyy-MM-dd")).append("'");
		        }
		        c.append(")");
		        situacoes.add(c.toString());
		        adicionou = true;
		    }
		    if (relatorioSEIDecidirVO.isFiltrarDeferido()) {
		        Date ini = relatorioSEIDecidirVO.getDataInicio5();
		        Date fim = relatorioSEIDecidirVO.getDataFim5();
		        StringBuilder c = new StringBuilder("(estagio.situacaoestagioenum = 'DEFERIDO'");
		        if (ini != null) {
		            c.append(" and estagio.created >= '").append(Uteis.getData(ini, "yyyy-MM-dd")).append("'");
		        }
	            if (fim != null) {
	            	c.append(" and estagio.created <= '").append(Uteis.getData(fim, "yyyy-MM-dd")).append("'");
		        }
		        c.append(")");
		        situacoes.add(c.toString());
		        adicionou = true;
		    }
		    if (relatorioSEIDecidirVO.isFiltrarEmAnalise()) {
		        Date ini = relatorioSEIDecidirVO.getDataInicio3();
		        Date fim = relatorioSEIDecidirVO.getDataFim3();
		        StringBuilder c = new StringBuilder("(estagio.situacaoestagioenum = 'EM_ANALISE'");
		        if (ini != null) {
		            c.append(" and estagio.created >= '").append(Uteis.getData(ini, "yyyy-MM-dd")).append("'");
		        }
		        if (fim != null) {
		            c.append(" and estagio.created <= '").append(Uteis.getData(fim, "yyyy-MM-dd")).append("'");
		        }
		        c.append(")");
		        situacoes.add(c.toString());
		        adicionou = true;
		    }
		    if (relatorioSEIDecidirVO.isFiltrarEmCorrecao()) {
		        Date ini = relatorioSEIDecidirVO.getDataInicio4();
		        Date fim = relatorioSEIDecidirVO.getDataFim4();
		        StringBuilder c = new StringBuilder("(estagio.situacaoestagioenum = 'EM_CORRECAO'");
		        if (ini != null) {
		            c.append(" and estagio.created >= '").append(Uteis.getData(ini, "yyyy-MM-dd")).append("'");
		        }
	            if (fim != null) {
	            	c.append(" and estagio.created <= '").append(Uteis.getData(fim, "yyyy-MM-dd")).append("'");
		        }
		        c.append(")");
		        situacoes.add(c.toString());
		        adicionou = true;
		    }
		    if (relatorioSEIDecidirVO.isFiltrarIndeferido()) {
		        Date ini = relatorioSEIDecidirVO.getDataInicioPeriodo();
		        Date fim = relatorioSEIDecidirVO.getDataFimPeriodo();
		        StringBuilder c = new StringBuilder("(estagio.situacaoestagioenum = 'INDEFERIDO'");
		        if (ini != null) {
		            c.append(" and estagio.created >= '").append(Uteis.getData(ini, "yyyy-MM-dd")).append("'");
		        }
		        if (fim != null) {
		            c.append(" and estagio.created <= '").append(Uteis.getData(fim, "yyyy-MM-dd")).append("'");
		        }
		        c.append(")");
		        situacoes.add(c.toString());
		        adicionou = true;
		    }
		    if (relatorioSEIDecidirVO.isFiltrarEmRealizacao()) {
		        Date ini = relatorioSEIDecidirVO.getDataInicio2();
		        Date fim = relatorioSEIDecidirVO.getDataFim2();
		        StringBuilder c = new StringBuilder("(estagio.situacaoestagioenum = 'REALIZANDO'");
		        if (ini != null && fim != null) {
		            c.append(" and estagio.created >= '").append(Uteis.getData(ini, "yyyy-MM-dd")).append("'");
		            c.append(" and estagio.created <= '").append(Uteis.getData(fim, "yyyy-MM-dd")).append("'");
		        }
		        c.append(")");
		        situacoes.add(c.toString());
		        adicionou = true;
		    }
		    if (adicionou) {
		        sql.append(situacoes.toString());
		    }
		}

		if (Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getConcedente())) {
			sql.append(" and estagio.concedente = '").append(relatorioSEIDecidirVO.getConcedente()).append("' ");
		}
		if(Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getValorBuscaInicio())) {
			sql.append(" and integralizacao.percentualintegralizado >= '").append(relatorioSEIDecidirVO.getValorBuscaInicio()).append("' ");
		}
		if(Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getValorBuscaFinal())) {
			sql.append(" and integralizacao.percentualintegralizado <= '").append(relatorioSEIDecidirVO.getValorBuscaFinal()).append("' ");
		}
	
		boolean temIni = Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getValorBuscaInicio1());
		boolean temFim = Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getValorBuscaFinal1());
		if (temIni && temFim) {
		    sql.append(" AND (CASE ")
		       .append("WHEN integralizacao.cargahorariaestagioexigido > 0 ")
		       .append(" then (integralizacao.cargahorariaestagiocumprido::numeric * 100.0 ")
		       .append("  / integralizacao.cargahorariaestagioexigido::numeric) ")
		       .append("ELSE 0 END) ")
		       .append(" BETWEEN ")
		       .append(relatorioSEIDecidirVO.getValorBuscaInicio1())
		       .append(" AND ")
		       .append(relatorioSEIDecidirVO.getValorBuscaFinal1());
		} else if (temIni && !temFim) {
		    sql.append(" AND (CASE ")
		       .append("WHEN integralizacao.cargahorariaestagioexigido > 0 THEN ")
		       .append(" (integralizacao.cargahorariaestagiocumprido::numeric * 100.0 ")
		       .append("  / integralizacao.cargahorariaestagioexigido::numeric) ")
		       .append("ELSE 0 END) >= ")
		       .append(relatorioSEIDecidirVO.getValorBuscaInicio1());
		} else if (temFim && !temIni) {
		    sql.append(" AND (CASE ")
		       .append("WHEN integralizacao.cargahorariaestagioexigido > 0 THEN ")
		       .append(" (integralizacao.cargahorariaestagiocumprido::numeric * 100.0 ")
		       .append("  / integralizacao.cargahorariaestagioexigido::numeric) ")
		       .append("ELSE 0 END) <= ")
		       .append(relatorioSEIDecidirVO.getValorBuscaFinal1());
		}

		if (Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getCodigosGradeCurricularEstagio())) {
			sql.append(" and estagio.gradecurricularestagio in (").append(relatorioSEIDecidirVO.getCodigosGradeCurricularEstagio()).append(") ");
		}
		if (Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getNivelEducacional())) {
			sql.append(" and curso.niveleducacional = '").append(relatorioSEIDecidirVO.getNivelEducacional()).append("' ");
		}
		if (Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getMatriculaVO().getMatricula()) && relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoMatricula()) {
			sql.append(" and matricula.matricula = '").append(relatorioSEIDecidirVO.getMatriculaVO().getMatricula()).append("' ");
		}
		if (relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getApresentarFiltroFixoSituacaoFiltroSituacaoAcademica()) {
			StringBuilder sql2 = adicionarFiltroSituacaoAcademicaMatricula(relatorioSEIDecidirVO.getRelatorioAcademicoVO(), "matricula");
			if(sql2.length() > 1) {
				sql.append(" AND ");
				sql.append(sql2);
			}
		}
		return sql.toString();
	}
	
	public void validarDadosFiltroPersonalizado(FiltroPersonalizadoVO obj, UsuarioVO usuarioVO) throws Exception {
		if (obj.getTipoCampoFiltro().equals(TipoCampoFiltroEnum.TEXTUAL)) {
			if (obj.getObrigatorio() && obj.getValorCampoTexto().equals("")) {
				throw new Exception("O campo " +obj.getTituloCampo().toUpperCase()+ " (Filtro Personalizado) deve ser informado.");
			}
		}
		if (obj.getTipoCampoFiltro().equals(TipoCampoFiltroEnum.INTEIRO)) {
			if (obj.getObrigatorio() && obj.getValorCampoInteiro().equals(0)) {
				throw new Exception("O campo " +obj.getTituloCampo().toUpperCase()+ " (Filtro Personalizado) deve ser informado.");
			}
		}
		if (obj.getTipoCampoFiltro().equals(TipoCampoFiltroEnum.DATA) && obj.getObrigatorio()) {
			if (obj.getValorCampoDataInicio() == null || obj.getValorCampoDataFim() == null) {
				throw new Exception("O campo " +obj.getTituloCampo().toUpperCase()+ " (Filtro Personalizado) deve ser informado.");
			}
		}
		if ((obj.getTipoCampoFiltro().equals(TipoCampoFiltroEnum.MULTIPLA_ESCOLHA) || obj.getTipoCampoFiltro().equals(TipoCampoFiltroEnum.SIMPLES_ESCOLHA)) && obj.getObrigatorio()) {
			boolean possuiSelecionado = false;
			for (FiltroPersonalizadoOpcaoVO filtroPersonalizadoOpcaoVO : obj.getListaFiltroPersonalizadoOpcaoVOs()) {
				if (filtroPersonalizadoOpcaoVO.getSelecionado()) {
					possuiSelecionado = true;
				}
			}
			if (!possuiSelecionado) {
				throw new Exception("O campo " +obj.getTituloCampo().toUpperCase()+ " (Filtro Personalizado) deve ser informado.");
			}
		}
		if (obj.getTipoCampoFiltro().equals(TipoCampoFiltroEnum.COMBOBOX_CUSTOMIZAVEL) && obj.getObrigatorio()) {
			if (obj.getValorCampoFiltroCustomizavel().equals(0)) {
				throw new Exception("O campo " +obj.getTituloCampo().toUpperCase()+ " (Filtro Personalizado) deve ser informado.");
			}
		}
		if (obj.getTipoCampoFiltro().equals(TipoCampoFiltroEnum.COMBOBOX) && obj.getObrigatorio()) {
			if (obj.getValorCampoCombobox().equals("")) {
				throw new Exception("O campo " +obj.getTituloCampo().toUpperCase()+ " (Filtro Personalizado) deve ser informado.");
			}
		}
	}
	 

	 public void realizarGeracaoFiltroPersonalizado(StringBuilder sql, LayoutRelatorioSEIDecidirVO layoutRelatorioSEIDecidirVO, UsuarioVO usuarioVO) throws Exception {
		 for (FiltroPersonalizadoVO filtroPersonalizadoVO : layoutRelatorioSEIDecidirVO.getListaFiltroPersonalizadoVOs()) {
			 validarDadosFiltroPersonalizado(filtroPersonalizadoVO, usuarioVO);
			    //TEXTUAL
			if (filtroPersonalizadoVO.getTipoCampoFiltro().equals(TipoCampoFiltroEnum.TEXTUAL) && !filtroPersonalizadoVO.getValorCampoTexto().equals("")) {
				String valor = filtroPersonalizadoVO.getCampoQuery().replace(filtroPersonalizadoVO.getTagSubstituirCampo(), filtroPersonalizadoVO.getValorCampoTexto());
				sql.append(" ");
				sql.append(valor).append(" ");
				
				// INTEIRO 
			} else if (filtroPersonalizadoVO.getTipoCampoFiltro().equals(TipoCampoFiltroEnum.INTEIRO) && !filtroPersonalizadoVO.getValorCampoInteiro().toString().equals("")) {
				if (!filtroPersonalizadoVO.getValorCampoInteiro().equals(0)) {
					String valor = filtroPersonalizadoVO.getCampoQuery().replace(filtroPersonalizadoVO.getTagSubstituirCampo(), filtroPersonalizadoVO.getValorCampoInteiro().toString());
					sql.append(" ");
					sql.append(valor).append(" ");
				}
				
				// DATA
			} else if (filtroPersonalizadoVO.getTipoCampoFiltro().equals(TipoCampoFiltroEnum.DATA)) {
				if (filtroPersonalizadoVO.getValorCampoDataInicio() != null && filtroPersonalizadoVO.getValorCampoDataFim() != null)  {
					String valorData = filtroPersonalizadoVO.getCampoQuery().replace(filtroPersonalizadoVO.getTagSubstituirCampoDataInicio(), UteisData.getDataAplicandoFormatacao(filtroPersonalizadoVO.getValorCampoDataInicio(), "yyyy-MM-dd")).replace(filtroPersonalizadoVO.getTagSubstituirCampoDataFim(), UteisData.getDataAplicandoFormatacao(filtroPersonalizadoVO.getValorCampoDataFim(), "yyyy-MM-dd"));
					sql.append(" ");
					sql.append(valorData);
				} else if (filtroPersonalizadoVO.getValorCampoDataInicio() != null && filtroPersonalizadoVO.getValorCampoDataFim() == null)  {
					throw new Exception("O campo DATA FIM (Filtro Personalizado) deve ser informado. ");
				} else if (filtroPersonalizadoVO.getValorCampoDataInicio() == null && filtroPersonalizadoVO.getValorCampoDataFim() != null)  {
					throw new Exception("O campo DATA INÍCIO (Filtro Personalizado) deve ser informado. ");
				}
				
				// BOOLEAN
			} else if (filtroPersonalizadoVO.getTipoCampoFiltro().equals(TipoCampoFiltroEnum.BOOLEAN)) {
				sql.append(" ");
				if (filtroPersonalizadoVO.getValorCampoBoolean()) {
					sql.append(filtroPersonalizadoVO.getCampoQuery());
				} else {
					sql.append(filtroPersonalizadoVO.getCampoQuery()).append(" = false");
				}
				
				// MULTIPLA_ESCOLHA - SIMPLES ESCOLHA
			} else if (filtroPersonalizadoVO.getTipoCampoFiltro().equals(TipoCampoFiltroEnum.MULTIPLA_ESCOLHA) || filtroPersonalizadoVO.getTipoCampoFiltro().equals(TipoCampoFiltroEnum.SIMPLES_ESCOLHA)) {
				StringBuilder sbAux = new StringBuilder();
				Boolean selecionado = false;
				for (FiltroPersonalizadoOpcaoVO filtroPersonalizadoOpcaoVO : filtroPersonalizadoVO.getListaFiltroPersonalizadoOpcaoVOs()) {
					if (filtroPersonalizadoOpcaoVO.getSelecionado()) {
						sbAux.append("'").append(filtroPersonalizadoOpcaoVO.getKeyOpcao()).append("', ");
						selecionado = true;
					}
				}
				if (selecionado) {
					sbAux.append("'0'");
					String valor = filtroPersonalizadoVO.getCampoQuery().replace(filtroPersonalizadoVO.getTagSubstituirCampo(), sbAux.toString());
					sql.append(" ");
					sql.append(valor);
				}
				
				// FILTRO CUSTOMIZÁVEL
			} else if (filtroPersonalizadoVO.getTipoCampoFiltro().equals(TipoCampoFiltroEnum.COMBOBOX_CUSTOMIZAVEL)) {
				if (!filtroPersonalizadoVO.getValorCampoFiltroCustomizavel().equals(0)) {
					String query = getFacadeFactory().getFiltroPersonalizadoOpcaoInterfaceFacade().consultarCampoQueryPorCodigo(filtroPersonalizadoVO.getValorCampoFiltroCustomizavel(), usuarioVO);
					sql.append(" ");
					sql.append(query);
				}
				
				// FILTRO COMBOBOX
			} else if (filtroPersonalizadoVO.getTipoCampoFiltro().equals(TipoCampoFiltroEnum.COMBOBOX)) {
				if (!filtroPersonalizadoVO.getValorCampoCombobox().trim().equals("")) {
					String valor = filtroPersonalizadoVO.getCampoQuery().replace(filtroPersonalizadoVO.getTagSubstituirCampo(), filtroPersonalizadoVO.getValorCampoCombobox().toString());
					sql.append(" ");
					sql.append(valor);
				}
			}
		}
	 }
	 
	 public SqlRowSet realizarConsultarGeracaoRelatorioLayoutPersonalizado(RelatorioSEIDecidirVO relatorioSEIDecidirVO, UsuarioVO usuarioVO) throws Exception {
	 	StringBuilder sql = new StringBuilder();
	 	sql.append(realizarGeracaoSelect(relatorioSEIDecidirVO));
	 	sql.append(" ").append(relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getQueryLayoutPersonalizado());
	 	sql.append(realizarGeracaoWhere(relatorioSEIDecidirVO, null, usuarioVO));
	 	sql.append(realizarGeracaoGroupBy(relatorioSEIDecidirVO));
		sql.append(realizarGeracaoOrderBy(relatorioSEIDecidirVO));
		return getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
	}
	 
	 
	 public SqlRowSet consultaIntegracaoSymplicty() {
		    StringBuffer sqlStr = new StringBuffer();
		    sqlStr.append(" select	replace(replace(replace(aluno.cpf, '.',	''),'-',''),' ','') as ID_ALUNO, replace(replace(replace(aluno.cpf,	'.',''),'-',''),' ','') as USUARIO, ");
		    sqlStr.append(" aluno.nome as NOME_COMPLETO, split_part(aluno.nome,	' ',1) as PRIMEIRO_NOME, split_part(aluno.nome, ' ', length(aluno.nome) - length(replace(aluno.nome,' ', ");
		    sqlStr.append(" ''))+ 1) as SOBRENOME,	replace(replace(replace(aluno.cpf,	'.', ''),	'-', ''),	' ',	'') as CPF,		"  );
		    sqlStr.append("	(case when matricula.situacao in ('FO','TR', 'JU', 'AC', 'CA') then aluno.email  else (select email from pessoaemailinstitucional where pessoaemailinstitucional.pessoa = aluno.codigo order by pessoaemailinstitucional.statusativoinativoenum, codigo desc limit 1) end) as EMAIL, ");
		    sqlStr.append("	(select	email	from pessoaemailinstitucional ");
		    sqlStr.append(" where	pessoaemailinstitucional.pessoa = aluno.codigo ");
		    sqlStr.append(" order by pessoaemailinstitucional.statusativoinativoenum,	codigo desc	limit 1) as EMAIL_INSTITUCIONAL, ");
		    sqlStr.append(" aluno.celular as TELEFONE,	replace(aluno.telefoneRes,	'|',	'') as TELEFONE_SECUNDARIO,	to_char(aluno.dataNasc,	'dd/MM/yyyy') as DATA_NASCIMENTO, ");
		    sqlStr.append(" case	aluno.sexo when 'M' then 'Masculino' when 'F' then 'Feminino'	else 'Não Informado'end as GENERO,	aluno.endereco || ', nr ' || coalesce(aluno.numero, ");
		    sqlStr.append(" 'sn')|| ', ' || aluno.setor as ENDERECO_ATUAL,	alunocidade.nome as CIDADE_ATUAL,	alunoestado.sigla as ESTADO_ATUAL,	replace(aluno.cep,	'.',	'') as CEP_ATUAL, ");
		    sqlStr.append(" paizaluno.nome as PAIS_ATUAL,	case		when matricula.situacao = 'AT' then 'No'		else 'Yes'	end as EGRESSO, ");
		    sqlStr.append(" case		when matricula.situacao = 'AT' then 'Aluno'		else 'Egresso'	end as TIPO_CANDIDATO,	 ");
		    sqlStr.append(" case		when not exists(		select			m.matricula		from	matricula as m	where	m.aluno = aluno.codigo	and m.situacao in ('AT', 'FI', 'CO', 'FO', 'TR', 'AC', 'AB') ");
		    sqlStr.append(" limit 1) then 'Yes'	else 'No'	end as CONTA_DESATIVADA,	'Univesp' as INSTITUICAO,	case	when (	select	m.matricula		from	matricula as m ");
		    sqlStr.append(" where	m.aluno = aluno.codigo	and m.situacao in ('AT', 'FI', 'CO', 'FO') ");
		    sqlStr.append(" order by case	m.situacao when 'FO' then 1	when 'FI' then 2 when 'CO' then 2	else 3 ");
		    sqlStr.append("	end,	anoingresso,	semestreingresso,	matricula limit 1) = matricula.matricula then 'Yes'	else 'No'	end as FORMACAO_PRINCIPAL,	matricula.matricula as ID_UNICO_FORMACAO, ");
		    sqlStr.append(" matricula.matricula as NUMERO_MATRICULA,	(case	matricula.situacao when 'AC' then 'Abandono de Curso'	when 'PR' then 'Pré-matrícula'	when 'DE' then 'Desligado' ");
		    sqlStr.append(" when 'IN' then 'Inativa'	when 'AT' then 'Ativa'	when 'CA' then 'Cancelada'	when 'CF' then 'Cancelada Financeiro'	when 'JU' then 'Jubilado'	when 'TS' then 'Transferida' ");
		    sqlStr.append(" when 'TR' then 'Trancada'	when 'FI' then 'Finalizada'	when 'PF' then 'Provável Formando'	when 'FO' then 'Formado'	when 'TI' then 'Transferida Internamente'	else 'Problema ao Tentar Definir Situação - Problema Importação' ");
		    sqlStr.append(" end) as SITUACAO_ACADEMICA,	(	select	pl.periodoletivo || 'º Semestre' from	matriculaperiodo mp ");
		    sqlStr.append(" inner join periodoletivo pl on		pl.codigo = mp.periodoletivomatricula	where	mp.matricula = matricula.matricula	order by mp.ano desc,	mp.semestre desc,	mp.codigo desc limit 1) as SEMESTRE_ALUNO, ");
		    sqlStr.append(" unidadeensino.nome as CAMPUS,	case	curso.nivelEducacional when 'IN' then 'Educação Infantil'	when 'BA' then 'Ensino Fundamental'	when 'ME' then 'Ensino Médio'	when 'EX' then 'Extensão' ");
		    sqlStr.append(" when 'SE' then 'Sequencial'	when 'GT' then 'Graduação Tecnológica'	when 'SU' then 'Graduação'	when 'PO' then 'Pós-graduação'	when 'PR' then 'Técnico/Profissionalizante'	when 'MT' then 'Pós-graduação(Stricto Sensu) - Mestrado' ");
		    sqlStr.append(" else '' end as NIVEL_FORMACAO, case	curso.nivelEducacional when 'IN' then 'Educação Infantil'	when 'BA' then 'Ensino Fundamental'	when 'ME' then 'Ensino Médio'	when 'EX' then 'Extensão' ");
		    sqlStr.append(" when 'SE' then 'Sequencial'	when 'GT' then 'Graduação Tecnológica'	when 'SU' then 'Graduação'	when 'PO' then 'Pós-graduação'	when 'PR' then 'Técnico/Profissionalizante'	when 'MT' then 'Pós-graduação(Stricto Sensu) - Mestrado' ");
		    sqlStr.append(" else ''	end || '/' || curso.nome as CURSO,	areaconhecimento.nome as AREA_CONHECIMENTO,	case when matricula.situacao = 'FO' then to_char(matricula.dataColacaoGrau, ");
		    sqlStr.append(" 'dd/MM/yyyy')	else ''	end as DATA_FORMATURA from	matricula ");
		    sqlStr.append(" inner join matriculaperiodo on	matriculaperiodo.matricula = matricula.matricula ");
		    sqlStr.append(" inner join periodoletivo as periodoletivomatriculaperiodo on	matriculaperiodo.periodoletivomatricula = periodoletivomatriculaperiodo.codigo ");
		    sqlStr.append(" inner join turma on	matriculaperiodo.turma = turma.codigo ");
		    sqlStr.append(" inner join unidadeensino on	unidadeensino.codigo = matricula.unidadeensino ");
		    sqlStr.append(" inner join curso on	curso.codigo = matricula.curso ");
		    sqlStr.append(" left join eixocurso on	curso.eixocurso = eixocurso.codigo ");
		    sqlStr.append(" inner join turno on	turno.codigo = matricula.turno ");
		    sqlStr.append(" inner join pessoa as aluno on	aluno.codigo = matricula.aluno ");
		    sqlStr.append(" left join cidade as alunocidade on	aluno.cidade = alunocidade.codigo ");
		    sqlStr.append(" left join estado as alunoestado on	alunocidade.estado = alunoestado.codigo ");
		    sqlStr.append(" left join paiz as paizaluno on	paizaluno.codigo = alunoestado.paiz ");
		    sqlStr.append(" left join areaconhecimento on	areaconhecimento.codigo = curso.areaconhecimento ");
		    sqlStr.append(" where curso.niveleducacional <> 'EX' and matricula.situacao not in ('TI', 'DE', 'IN') ");
		    sqlStr.append(" group by	replace(replace(replace(aluno.cpf,	'.',	''),'-',''),' ',''),replace(replace(replace(aluno.cpf,	'.',	''),	'-',	''),	' ',	''),	 ");
		    sqlStr.append(" aluno.nome,	split_part(aluno.nome,	' ', 1), split_part(aluno.nome,	' ',	length(aluno.nome) - length(replace(aluno.nome,	' ',	''))+ 1),	replace(replace(replace(aluno.cpf, ");
		    sqlStr.append(" '.',	''),	'-',	''),	' ',	''),	aluno.email,	aluno.celular,	replace(aluno.telefoneRes,	'|',	''),	to_char(aluno.dataNasc,	'dd/MM/yyyy'),	case ");
		    sqlStr.append(" aluno.sexo when 'M' then 'Masculino'	when 'F' then 'Feminio'	else 'Não Informado' end,	aluno.endereco || ', nr ' || coalesce(aluno.numero,	'sn')|| ', ' || aluno.setor, ");
		    sqlStr.append(" alunocidade.nome,	alunoestado.sigla,	replace(aluno.cep,	'.',	''),	paizaluno.nome,	case when matricula.situacao = 'AT' then 'No'	else 'Yes'	end, ");
		    sqlStr.append(" case when matricula.situacao = 'AT' then 'Aluno'	else 'Egresso'	end, case	when not exists(select	m.matricula	from matricula as m	where	m.aluno = aluno.codigo	and m.situacao in ('AT', 'FI', 'CO', 'FO', 'TR', 'AC', 'AB') ");
		    sqlStr.append(" limit 1) then 'Yes'	else 'No'	end,	case	when (	select	m.matricula	from	matricula as m	where	m.aluno = aluno.codigo	and m.situacao in ('AT', 'FI', 'CO', 'FO') ");
		    sqlStr.append(" order by case m.situacao when 'FO' then 1	when 'FI' then 2	when 'CO' then 2	else 3	end, anoingresso, semestreingresso,	matricula	limit 1) = matricula.matricula then 'Yes' ");
		    sqlStr.append(" else 'No' end,	matricula.matricula,	matricula.matricula,	(case		matricula.situacao when 'AC' then 'Abandono de Curso'		when 'PR' then 'Pré-matrícula'		when 'DE' then 'Desligado' ");
		    sqlStr.append(" when 'IN' then 'Inativa'	when 'AT' then 'Ativa'	when 'CA' then 'Cancelada'	when 'CF' then 'Cancelada Financeiro'	when 'JU' then 'Jubilado'	when 'TS' then 'Transferida' ");
		    sqlStr.append(" when 'TR' then 'Trancada' when 'FI' then 'Finalizada'	when 'PF' then 'Provável Formando'	when 'FO' then 'Formado'	when 'TI' then 'Transferida Internamente' ");
		    sqlStr.append(" else 'Problema ao Tentar Definir Situação - Problema Importação'	end) ,	unidadeensino.nome,	case	curso.nivelEducacional when 'IN' then 'Educação Infantil' ");
		    sqlStr.append(" when 'BA' then 'Ensino Fundamental'		when 'ME' then 'Ensino Médio'		when 'EX' then 'Extensão'		when 'SE' then 'Sequencial'		when 'GT' then 'Graduação Tecnológica' ");
		    sqlStr.append(" when 'SU' then 'Graduação'		when 'PO' then 'Pós-graduação'		when 'PR' then 'Técnico/Profissionalizante'		when 'MT' then 'Pós-graduação(Stricto Sensu) - Mestrado' ");
		    sqlStr.append(" else ''	end ,	case		curso.nivelEducacional when 'IN' then 'Educação Infantil'		when 'BA' then 'Ensino Fundamental'		when 'ME' then 'Ensino Médio'		when 'EX' then 'Extensão' ");
		    sqlStr.append(" when 'SE' then 'Sequencial'		when 'GT' then 'Graduação Tecnológica'		when 'SU' then 'Graduação'		when 'PO' then 'Pós-graduação'		when 'PR' then 'Técnico/Profissionalizante' ");
		    sqlStr.append(" when 'MT' then 'Pós-graduação(Stricto Sensu) - Mestrado'		else ''	end || '/' || curso.nome,	areaconhecimento.nome,	case		when matricula.situacao = 'FO' then to_char(matricula.dataColacaoGrau, ");
		    sqlStr.append(" 'dd/MM/yyyy')		else ''	end,	aluno.codigo ");
		    sqlStr.append(" order by aluno.codigo, 	CASE  WHEN matricula.situacao NOT IN ('AT') THEN 1 ELSE 2 END, ");
		    sqlStr.append(" NOME_COMPLETO asc ");
		    SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		    System.out.println(sqlStr.toString());
			return dadosSQL;
		}		
}
