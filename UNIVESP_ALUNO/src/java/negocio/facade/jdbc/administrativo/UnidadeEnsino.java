package negocio.facade.jdbc.administrativo;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import controle.academico.RenovarMatriculaControle;
import negocio.comuns.academico.ProgramacaoFormaturaUnidadeEnsinoVO;
import negocio.comuns.academico.enumeradores.TipoAutorizacaoCursoEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoCentroResultadoVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoNivelEducacionalCentroResultadoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.EmpresaVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.processosel.ProcSeletivoVO;
import negocio.comuns.utilitarias.ArquivoHelper;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisExcel;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.DiaSemana;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.arquitetura.FacadeFactory;
import negocio.facade.jdbc.utilitarias.Conexao;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.administrativo.UnidadeEnsinoInterfaceFacade;
import webservice.servicos.UnidadeEnsinoRSVO;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>UnidadeEnsinoVO</code>. Responsável por
 * implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>UnidadeEnsinoVO</code>. Encapsula toda a interação com
 * o banco de dados.
 * 
 * @see UnidadeEnsinoVO
 * @see ControleAcesso
 * @see EmpresaVO
 */
@Repository
@Scope("singleton")
@Lazy
public class UnidadeEnsino extends ControleAcesso implements UnidadeEnsinoInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	// private Hashtable unidadeEnsinoCursos;

	public UnidadeEnsino() throws Exception {
		super();
		setIdEntidade("UnidadeEnsino");
	}
	
	
	public List<UnidadeEnsinoVO> consultarPorProcSeletivoComboBox(List<ProcSeletivoVO> listaProcSeletivoVOs, Boolean possuiUnidadeEnsinoLogada, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct unidadeensino.codigo, unidadeensino.nome, unidadeensino.cnpj from unidadeensino ");
		sb.append(" inner join procseletivounidadeensino on procseletivounidadeensino.unidadeEnsino = unidadeensino.codigo ");
		if (possuiUnidadeEnsinoLogada) {
			sb.append(" inner join usuarioPerfilAcesso on usuarioPerfilAcesso.unidadeEnsino = unidadeEnsino.codigo ");
		}
		sb.append(" where 1=1 ");
		if (!listaProcSeletivoVOs.isEmpty()) {
			sb.append("and procseletivounidadeensino.procSeletivo  in (");
		}
		for (ProcSeletivoVO procSeletivoVO : listaProcSeletivoVOs) {
			if (procSeletivoVO.getFiltrarProcessoSeletivo()) {
				sb.append(procSeletivoVO.getCodigo()).append(", ");
			}
		}
		if (!listaProcSeletivoVOs.isEmpty()) {
			sb.append("0) ");
		}
		if (possuiUnidadeEnsinoLogada) {
			sb.append(" and usuarioPerfilAcesso.usuario = ").append(usuarioVO.getCodigo());
		}
		sb.append(" order by unidadeEnsino.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<UnidadeEnsinoVO> listaUnidadeEnsinoVOs = null;
		UnidadeEnsinoVO unidadeEnsinoVO = null;
		while (tabelaResultado.next()) {
			if (listaUnidadeEnsinoVOs == null) {
				listaUnidadeEnsinoVOs = new ArrayList<UnidadeEnsinoVO>(0);
			}
			unidadeEnsinoVO = new UnidadeEnsinoVO();
			unidadeEnsinoVO.setCodigo(tabelaResultado.getInt("codigo"));
			unidadeEnsinoVO.setNome(tabelaResultado.getString("nome"));
			unidadeEnsinoVO.setCNPJ(tabelaResultado.getString("cnpj"));
			unidadeEnsinoVO.setFiltrarUnidadeEnsino(true);
			listaUnidadeEnsinoVOs.add(unidadeEnsinoVO);
		}
		return listaUnidadeEnsinoVOs;
	}
	
//	public UnidadeEnsino(Conexao conexao, FacadeFactory facadeFactory) {
//		setFacadeFactory(facadeFactory);
//		setConexao(conexao);
//	}

	/**
	 * Operação responsável por retornar um novo objeto da classe <code>UnidadeEnsinoVO</code>.
	 */
	public UnidadeEnsinoVO novo() throws Exception {
		UnidadeEnsino.incluir(getIdEntidade());
		UnidadeEnsinoVO obj = new UnidadeEnsinoVO();
		return obj;
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe <code>UnidadeEnsinoVO</code>. Primeiramente valida os dados (
	 * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>UnidadeEnsinoVO</code> que será gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final UnidadeEnsinoVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception {
		try {
			UnidadeEnsinoVO.validarDados(obj);
			incluir(getIdEntidade(), true, usuario);
			salvarLogoUnidadeEnsino(obj, configuracaoGeralSistemaVO);
			final String sql = "INSERT INTO UnidadeEnsino( nome, razaoSocial, endereco, setor, numero, complemento, cidade, CEP, tipoEmpresa, CNPJ, inscEstadual, RG, "//12
					+ "CPF, telComercial1, telComercial2, telComercial3, email, site, fax, matriz, abreviatura, configuracoes, ano, numeroDocumento, codigoies, " //13
					+ "diretorGeral, credenciamentoportaria, datapublicacaodo, mantenedora, permitirVisualizacaoLogin, desativada, apresentarTelaProcessoSeletivo, "//7
					+ "responsavelCobrancaUnidade, "//1
					+ "contaCorrentePadrao, caminhoBaseLogo, nomeArquivoLogo, caminhoBaseLogoIndex, nomeArquivoLogoIndex, caminhoBaseLogoRelatorio, "//6
							+ "nomeArquivoLogoRelatorio, credenciamento, configuracaonotafiscal, inscmunicipal, nomeExpedicaoDiploma,dependenciaAdministrativa,"//6
							+ "localizacaoZonaEscola,categoriaEscolaPrivada,"//2
							+ "conveniadaPoderPublico,localFuncionamentoDaEscola,formaOcupacaoPredio,predioCompartilhado,codigoEscolaCompartilhada1,"//5
							+ "codigoEscolaCompartilhada2,codigoEscolaCompartilhada3,codigoEscolaCompartilhada4,codigoEscolaCompartilhada5,codigoEscolaCompartilhada6,"//5
							+ "aguaConsumida,abastecimentoAgua,abastecimentoEnergia,esgotoSanitario,destinoLixo,"//5
							+ "salaDiretoria,salaProfessores,salaSecretaria,laboratorioInformatica,laboratorioCiencias,recursosMultifuncionais,quadraEsportesCoberta,"//7
							+ "quadraEsportesDescoberta,"//1
							+ "cozinha,biblioteca,salaLeitura,parqueInfantil,bercario,banheiroForaPredio,banheiroDentroPredio,banheiroEducacaoInfantil,"//8
							+ "banheiroDeficiencia,"//1
							+ "viasDeficiencia,banheiroChuveiro,refeitorio,despensa,almoxarifado,auditorio,patioCoberto,patioDescoberto,alojamentoAluno,"//9
							+ "alojamentoProfessor,"//1
							+ "areaVerde,lavanderia,nenhumaDependencia,numeroSalasAulaExistente,numeroSalasDentroForaPredio,"//5
							+ "quantidadeTelevisao,quantidadeVideoCassete,quantidadeDVD,quantidadeAntenaParabolica,quantidadeCopiadora, " // 5
							+ "quantidadeRetroprojetor,quantidadeImpressora,quantidadeAparelhoSom,quantidadeProjetorMultimidia,quantidadeFax,"//5
							+ "quantidadeMaquinaFotograficaFilmadora,quantidadeComputadores,quantidadeComputadoresAdministrativos,quantidadeComputadoresAlunos,computadoresAcessoInternet,"//5
							+ "internetBandaLarga,caixaPostal, codigoTributacaoMunicipio,  codigoOrgaoRegionalEnsino, codigoDistritoCenso, apresentarHomePreInscricao, leiCriacao1, leiCriacao2, nomeArquivoLogoPaginaInicial, caminhoBaseLogoPaginaInicial,"//1
							+ "caminhoBaseLogoEmailCima, nomeArquivoLogoEmailCima, caminhoBaseLogoEmailBaixo, nomeArquivoLogoEmailBaixo, "
							+ "caminhobaselogomunicipio, nomearquivologomunicipio, nomearquivologoaplicativo, caminhobaselogoaplicativo, codigoIntegracaoContabil, usarConfiguracaoPadrao, configuracaoContabil, "
							+ "centroResultado, contaCorrentePadraoProcessoSeletivo, contaCorrentePadraoMatricula, contaCorrentePadraoMensalidade, contaCorrentePadraoMaterialDidatico, "
							+ "contaCorrentePadraoBiblioteca, contaCorrentePadraoRequerimento, contaCorrentePadraoNegociacao, contaCorrentePadraoDevolucaoCheque, informacoesAdicionaisEndereco, "
							+ "centroResultadoRequerimento, codigoIESMantenedora, cnpjmantenedora, unidadecertificadora, cnpjunidadecertificadora, codigoiesunidadecertificadora,configuracaoged, configuracaomobile, localizacaoDiferenciadaEscola, unidadeVinculadaEscolaEducacaoBasica, codigoEscolaSede, forneceAguaPotavelConsumoHumano, tratamentoLixo, banheiroExclusivoFuncionarios, piscina, "
							+ " salaRepousoAluno, salaArtes, salaMusica, salaDanca, salaMultiuso, terreirao, viveiroAnimais, corrimaoGuardaCorpos, elevador, pisosTateis, portasVaoLivreMinimoOitentaCentimetros, rampas, sinalizacaoSonora, sinalizacaoTatil, sinalizacaoVisual, nenhumRecursoAcessibilidade, numeroSalasAulaUtilizadasEscolaDentroPredioEscolar,  "
							+ "antenaParabolica, computadores, copiadora,  impressora, impressoraMultifuncional, scanner, acessoInternetUsoAdiministrativo, acessoInternetUsoProcessoEnsinoAprendizagem, acessoInternetUsoAlunos, acessoInternetComunidade, naoPossuiAcessoInternet, acessoInternetComputadoresEscola, acessoInternetDispositivosPessoais, "
							+ "redeLocalCabo, redeLocalWireless, NaoExisteRedeLocal, alimentacaoEscolarAlunos, educacaoEscolarIndigena, linguaIndigena, linguaPortuguesa, codigoLinguaIndigena1, codigoLinguaIndigena2, codigoLinguaIndigena3, chancela, tipochancela, porcentagemchancela, valorfixochancela  , responsavelNotificacaoAlteracaoCronogramaAula, "
							+ "orientadorPadraoEstagio, observacao  , operadorResponsavel, configuracaoDiplomaDigital,"
							+ "dataCredenciamento, veiculoPublicacaoCredenciamento, secaoPublicacaoCredenciamento, paginaPublicacaoCredenciamento, numeroDOUCredenciamento, tipoAutorizacaoEnum , "
							+ "informarDadosRegistradora, utilizarEnderecoUnidadeEnsinoRegistradora, cepRegistradora, cidadeRegistradora, complementoRegistradora, bairroRegistradora, enderecoRegistradora, numeroRegistradora,"
							+ "utilizarCredenciamentoUnidadeEnsino, numeroCredenciamentoRegistradora, dataCredenciamentoRegistradora, dataPublicacaoDORegistradora, veiculoPublicacaoCredenciamentoRegistradora, secaoPublicacaoCredenciamentoRegistradora, paginaPublicacaoCredenciamentoRegistradora, numeroPublicacaoCredenciamentoRegistradora, "
							+ "utilizarMantenedoraUnidadeEnsino, mantenedoraRegistradora, cnpjMantenedoraRegistradora, cepMantenedoraRegistradora, enderecoMantenedoraRegistradora, numeroMantenedoraRegistradora, cidadeMantenedoraRegistradora, complementoMantenedoraRegistradora, bairroMantenedoraRegistradora, "
							+ "utilizarEnderecoUnidadeEnsinoMantenedora, cepMantenedora, enderecoMantenedora, numeroMantenedora, cidadeMantenedora, complementoMantenedora, bairroMantenedora, numeroRecredenciamento, dataRecredenciamento, dataPublicacaoRecredenciamento, veiculoPublicacaoRecredenciamento, secaoPublicacaoRecredenciamento, paginaPublicacaoRecredenciamento, numeroDOURecredenciamento, tipoAutorizacaoRecredenciamento, tipoAutorizacaoCredenciamentoRegistradora, "
							+ "numeroRenovacaoRecredenciamento, dataRenovacaoRecredenciamento, dataPublicacaoRenovacaoRecredenciamento, veiculoPublicacaoRenovacaoRecredenciamento, secaoPublicacaoRenovacaoRecredenciamento, paginaPublicacaoRenovacaoRecredenciamento, numeroDOURenovacaoRecredenciamento, tipoAutorizacaoRenovacaoRecredenciamento, "
							+ "numeroCredenciamentoEAD, credenciamentoEAD, dataCredenciamentoEAD, dataPublicacaoDOEAD, credenciamentoPortariaEAD, veiculoPublicacaoCredenciamentoEAD, secaoPublicacaoCredenciamentoEAD, paginaPublicacaoCredenciamentoEAD, numeroDOUCredenciamentoEAD, tipoAutorizacaoEAD, numeroRecredenciamentoEAD, dataRecredenciamentoEAD, dataPublicacaoRecredenciamentoEAD, veiculoPublicacaoRecredenciamentoEAD, secaoPublicacaoRecredenciamentoEAD, paginaPublicacaoRecredenciamentoEAD, numeroDOURecredenciamentoEAD, tipoAutorizacaoRecredenciamentoEAD, numeroRenovacaoRecredenciamentoEAD, dataRenovacaoRecredenciamentoEAD, dataPublicacaoRenovacaoRecredenciamentoEAD, veiculoPublicacaoRenovacaoRecredenciamentoEAD, secaoPublicacaoRenovacaoRecredenciamentoEAD, paginaPublicacaoRenovacaoRecredenciamentoEAD, numeroDOURenovacaoRecredenciamentoEAD, tipoAutorizacaoRenovacaoRecredenciamentoEAD, "
							+ "credenciamentoEmTramitacao, numeroProcessoCredenciamento, tipoProcessoCredenciamento, dataCadastroCredenciamento, dataProtocoloCredenciamento, recredenciamentoEmTramitacao, numeroProcessoRecredenciamento, tipoProcessoRecredenciamento, dataCadastroRecredenciamento, dataProtocoloRecredenciamento, renovacaoRecredenciamentoEmTramitacao, numeroProcessoRenovacaoRecredenciamento, tipoProcessoRenovacaoRecredenciamento, dataCadastroRenovacaoRecredenciamento, dataProtocoloRenovacaoRecredenciamento, "
							+ "credenciamentoEadEmTramitacao, numeroProcessoCredenciamentoEad, tipoProcessoCredenciamentoEad, dataCadastroCredenciamentoEad, dataProtocoloCredenciamentoEad, recredenciamentoEmTramitacaoEad, numeroProcessoRecredenciamentoEad, tipoProcessoRecredenciamentoEad, dataCadastroRecredenciamentoEad, dataProtocoloRecredenciamentoEad, renovacaoRecredenciamentoEmTramitacaoEad, numeroProcessoRenovacaoRecredenciamentoEad, tipoProcessoRenovacaoRecredenciamentoEad, dataCadastroRenovacaoRecredenciamentoEad, dataProtocoloRenovacaoRecredenciamentoEad, credenciamentoRegistradoraEmTramitacao, numeroProcessoCredenciamentoRegistradora, tipoProcessoCredenciamentoRegistradora, dataCadastroCredenciamentoRegistradora, dataProtocoloCredenciamentoRegistradora, numeroCredenciamento, numeroVagaOfertada) "
							+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "//30
							+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "//30
							+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "//30
							+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "//30
							+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "//30
							+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "//30
							+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "//30
							+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "//30
							+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "//30
							+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "//30
							+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
					+ " returning codigo"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlInserir = con.prepareStatement(sql);
					sqlInserir.setString(1, obj.getNome());
					sqlInserir.setString(2, obj.getRazaoSocial());
					sqlInserir.setString(3, obj.getEndereco());
					sqlInserir.setString(4, obj.getSetor());
					sqlInserir.setString(5, obj.getNumero());
					sqlInserir.setString(6, obj.getComplemento());
					if (Uteis.isAtributoPreenchido(obj.getCidade())) {
						sqlInserir.setInt(7, obj.getCidade().getCodigo().intValue());
					} else {
						sqlInserir.setNull(7, 0);
					}
					sqlInserir.setString(8, obj.getCEP());
					sqlInserir.setString(9, obj.getTipoEmpresa());
					sqlInserir.setString(10, obj.getCNPJ());
					sqlInserir.setString(11, obj.getInscEstadual());
					sqlInserir.setString(12, obj.getRG());
					sqlInserir.setString(13, obj.getCPF());
					sqlInserir.setString(14, obj.getTelComercial1());
					sqlInserir.setString(15, obj.getTelComercial2());
					sqlInserir.setString(16, obj.getTelComercial3());
					sqlInserir.setString(17, obj.getEmail());
					sqlInserir.setString(18, obj.getSite());
					sqlInserir.setString(19, obj.getFax());
					sqlInserir.setBoolean(20, obj.isMatriz().booleanValue());
					sqlInserir.setString(21, obj.getAbreviatura());
					if (obj.getConfiguracoes().getCodigo().intValue() != 0) {
						sqlInserir.setInt(22, obj.getConfiguracoes().getCodigo());
					} else {
						sqlInserir.setNull(22, 0);
					}
					sqlInserir.setString(23, obj.getAno());
					sqlInserir.setInt(24, obj.getNumeroDocumento().intValue());
					sqlInserir.setInt(25, obj.getCodigoIES());
					if (obj.getDiretorGeral().getCodigo().intValue() != 0) {
						sqlInserir.setInt(26, obj.getDiretorGeral().getCodigo().intValue());
					} else {
						sqlInserir.setNull(26, 0);
					}
					sqlInserir.setString(27, obj.getCredenciamentoPortaria());
					sqlInserir.setDate(28, Uteis.getDataJDBC(obj.getDataPublicacaoDO()));
					sqlInserir.setString(29, obj.getMantenedora());
					sqlInserir.setBoolean(30, obj.getPermitirVisualizacaoLogin().booleanValue());
					sqlInserir.setBoolean(31, obj.getDesativada().booleanValue());
					sqlInserir.setBoolean(32, obj.getApresentarTelaProcessoSeletivo().booleanValue());
					if (obj.getResponsavelCobrancaUnidade().getCodigo().intValue() != 0) {
						sqlInserir.setInt(33, obj.getResponsavelCobrancaUnidade().getCodigo().intValue());
					} else {
						sqlInserir.setNull(33, 0);
					}
//					if (obj.getContaCorrentePadraoVO().getCodigo().intValue() != 0) {
//						sqlInserir.setInt(34, obj.getContaCorrentePadraoVO().getCodigo().intValue());
//					} else {
						sqlInserir.setNull(34, 0);
//					}
					sqlInserir.setString(35, obj.getCaminhoBaseLogo());
					sqlInserir.setString(36, obj.getNomeArquivoLogo());
					sqlInserir.setString(37, obj.getCaminhoBaseLogoIndex());
					sqlInserir.setString(38, obj.getNomeArquivoLogoIndex());
					sqlInserir.setString(39, obj.getCaminhoBaseLogoRelatorio());
					sqlInserir.setString(40, obj.getNomeArquivoLogoRelatorio());
					sqlInserir.setString(41, obj.getCredenciamento());
					
					sqlInserir.setString(43, obj.getInscMunicipal());
					sqlInserir.setString(44, obj.getNomeExpedicaoDiploma());
					sqlInserir.setString(45, obj.getDependenciaAdministrativa());
					sqlInserir.setString(46, obj.getLocalizacaoZonaEscola());
					sqlInserir.setString(47, obj.getCategoriaEscolaPrivada());
					sqlInserir.setString(48, obj.getConveniadaPoderPublico());
					sqlInserir.setString(49, obj.getLocalFuncionamentoDaEscola());
					sqlInserir.setString(50, obj.getFormaOcupacaoPredio());
					sqlInserir.setBoolean(51, obj.getPredioCompartilhado());
					sqlInserir.setString(52, obj.getCodigoEscolaCompartilhada1());
					sqlInserir.setString(53, obj.getCodigoEscolaCompartilhada2());
					sqlInserir.setString(54, obj.getCodigoEscolaCompartilhada3());
					sqlInserir.setString(55, obj.getCodigoEscolaCompartilhada4());
					sqlInserir.setString(56, obj.getCodigoEscolaCompartilhada5());
					sqlInserir.setString(57, obj.getCodigoEscolaCompartilhada6());
					sqlInserir.setString(58, obj.getAguaConsumida());
					sqlInserir.setString(59, obj.getAbastecimentoAgua());
					sqlInserir.setString(60, obj.getAbastecimentoEnergia());
					sqlInserir.setString(61, obj.getEsgotoSanitario());
					sqlInserir.setString(62, obj.getDestinoLixo());
					sqlInserir.setBoolean(63, obj.getSalaDiretoria());
					sqlInserir.setBoolean(64, obj.getSalaProfessores());
					sqlInserir.setBoolean(65, obj.getSalaSecretaria());
					sqlInserir.setBoolean(66, obj.getLaboratorioInformatica());
					sqlInserir.setBoolean(67, obj.getLaboratorioCiencias());
					sqlInserir.setBoolean(68, obj.getRecursosMultifuncionais());
					sqlInserir.setBoolean(69, obj.getQuadraEsportesCoberta());
					sqlInserir.setBoolean(70, obj.getQuadraEsportesDescoberta());
					sqlInserir.setBoolean(71, obj.getCozinha());
					sqlInserir.setBoolean(72, obj.getBiblioteca());
					sqlInserir.setBoolean(73, obj.getSalaLeitura());
					sqlInserir.setBoolean(74, obj.getParqueInfantil());
					sqlInserir.setBoolean(75, obj.getBercario());
					sqlInserir.setBoolean(76, obj.getBanheiroForaPredio());
					sqlInserir.setBoolean(77, obj.getBanheiroDentroPredio());
					sqlInserir.setBoolean(78, obj.getBanheiroEducacaoInfantil());
					sqlInserir.setBoolean(79, obj.getBanheiroDeficiencia());
					sqlInserir.setBoolean(80, obj.getViasDeficiencia());
					sqlInserir.setBoolean(81, obj.getBanheiroChuveiro());
					sqlInserir.setBoolean(82, obj.getRefeitorio());
					sqlInserir.setBoolean(83, obj.getDespensa());
					sqlInserir.setBoolean(84, obj.getAlmoxarifado());
					sqlInserir.setBoolean(85, obj.getAuditorio());
					sqlInserir.setBoolean(86, obj.getPatioCoberto());
					sqlInserir.setBoolean(87, obj.getPatioDescoberto());
					sqlInserir.setBoolean(88, obj.getAlojamentoAluno());
					sqlInserir.setBoolean(89, obj.getAlojamentoProfessor());
					sqlInserir.setBoolean(90, obj.getAreaVerde());
					sqlInserir.setBoolean(91, obj.getLavanderia());
					sqlInserir.setBoolean(92, obj.getNenhumaDependencia());
					sqlInserir.setString(93, obj.getNumeroSalasAulaExistente());
					sqlInserir.setString(94, obj.getNumeroSalasDentroForaPredio());

					if (obj.getQuantidadeTelevisao() != null) {
						sqlInserir.setInt(95, obj.getQuantidadeTelevisao());
					} else {
						sqlInserir.setNull(95, 0);
					}
					if (obj.getQuantidadeVideoCassete() != null) {
						sqlInserir.setInt(96, obj.getQuantidadeVideoCassete());
					} else {
						sqlInserir.setNull(96, 0);
					}
					if (obj.getQuantidadeDVD() != null) {
						sqlInserir.setInt(97, obj.getQuantidadeDVD());
					} else {
						sqlInserir.setNull(97, 0);
					}
					if (obj.getQuantidadeAntenaParabolica() != null) {
						sqlInserir.setInt(98, obj.getQuantidadeAntenaParabolica());
					} else {
						sqlInserir.setNull(98, 0);
					}
					if (obj.getQuantidadeCopiadora() != null) {
						sqlInserir.setInt(99, obj.getQuantidadeCopiadora());
					} else {
						sqlInserir.setNull(99, 0);
					}
					if (obj.getQuantidadeRetroprojetor() != null) {
						sqlInserir.setInt(100, obj.getQuantidadeRetroprojetor());
					} else {
						sqlInserir.setNull(100, 0);
					}
					if (obj.getQuantidadeImpressora() != null) {
						sqlInserir.setInt(101, obj.getQuantidadeImpressora());
					} else {
						sqlInserir.setNull(101, 0);
					}
					if (obj.getQuantidadeAparelhoSom() != null) {
						sqlInserir.setInt(102, obj.getQuantidadeAparelhoSom());
					} else {
						sqlInserir.setNull(102, 0);
					}
					if (obj.getQuantidadeProjetorMultimidia() != null) {
						sqlInserir.setInt(103, obj.getQuantidadeProjetorMultimidia());
					} else {
						sqlInserir.setNull(103, 0);
					}
					if (obj.getQuantidadeFax() != null) {
						sqlInserir.setInt(104, obj.getQuantidadeFax());
					} else {
						sqlInserir.setNull(104, 0);
					}
					if (obj.getQuantidadeMaquinaFotograficaFilmadora() != null) {
						sqlInserir.setInt(105, obj.getQuantidadeMaquinaFotograficaFilmadora());
					} else {
						sqlInserir.setNull(105, 0);
					}
					if (obj.getQuantidadeComputadores() != null) {
						sqlInserir.setInt(106, obj.getQuantidadeComputadores());
					} else {
						sqlInserir.setNull(106, 0);
					}
					if (obj.getQuantidadeComputadoresAdministrativos() != null) {
						sqlInserir.setInt(107, obj.getQuantidadeComputadoresAdministrativos());
					} else {
						sqlInserir.setNull(107, 0);
					}
					if (obj.getQuantidadeComputadoresAlunos() != null) {
						sqlInserir.setInt(108, obj.getQuantidadeComputadoresAlunos());
					} else {
						sqlInserir.setNull(108, 0);
					}
					sqlInserir.setBoolean(109, obj.getComputadoresAcessoInternet());
					sqlInserir.setBoolean(110, obj.getInternetBandaLarga());
					sqlInserir.setString(111, obj.getCaixaPostal());
					sqlInserir.setString(112, obj.getCodigoTributacaoMunicipio());
					sqlInserir.setString(113, obj.getCodigoOrgaoRegionalEnsino());
					sqlInserir.setString(114, obj.getCodigoDistritoCenso());
					sqlInserir.setBoolean(115, obj.getApresentarHomePreInscricao());
					sqlInserir.setString(116, obj.getLeiCriacao1());
					sqlInserir.setString(117, obj.getLeiCriacao2());
					sqlInserir.setString(118, obj.getNomeArquivoLogoPaginaInicial());
					sqlInserir.setString(119, obj.getCaminhoBaseLogoPaginaInicial());
					sqlInserir.setString(120, obj.getCaminhoBaseLogoEmailCima());
					sqlInserir.setString(121, obj.getNomeArquivoLogoEmailCima());
					sqlInserir.setString(122, obj.getCaminhoBaseLogoEmailBaixo());
					sqlInserir.setString(123, obj.getNomeArquivoLogoEmailBaixo());
					sqlInserir.setString(124, obj.getCaminhoBaseLogoMunicipio());
					sqlInserir.setString(125, obj.getNomeArquivoLogoMunicipio());
					sqlInserir.setString(126, obj.getNomeArquivoLogoAplicativo());
					sqlInserir.setString(127, obj.getCaminhoBaseLogoAplicativo());
					sqlInserir.setString(128, obj.getCodigoIntegracaoContabil());
					sqlInserir.setBoolean(129, obj.getUsarConfiguracaoPadrao());
					int i = 129;
//					Uteis.setValuePreparedStatement(obj.getConfiguracaoContabilVO(), ++i, sqlInserir);
//					Uteis.setValuePreparedStatement(obj.getCentroResultadoVO(), ++i, sqlInserir);
					
					if (obj.getContaCorrentePadraoProcessoSeletivo().intValue() > 0) {
						sqlInserir.setInt(++i, obj.getContaCorrentePadraoProcessoSeletivo());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					if (obj.getContaCorrentePadraoMatricula().intValue() > 0) {
						sqlInserir.setInt(++i, obj.getContaCorrentePadraoMatricula());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					if (obj.getContaCorrentePadraoMensalidade().intValue() > 0) {
						sqlInserir.setInt(++i, obj.getContaCorrentePadraoMensalidade());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					if (obj.getContaCorrentePadraoMaterialDidatico().intValue() > 0) {
						sqlInserir.setInt(++i, obj.getContaCorrentePadraoMaterialDidatico());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					if (obj.getContaCorrentePadraoBiblioteca().intValue() > 0) {
						sqlInserir.setInt(++i, obj.getContaCorrentePadraoBiblioteca());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					if (obj.getContaCorrentePadraoRequerimento().intValue() > 0) {
						sqlInserir.setInt(++i, obj.getContaCorrentePadraoRequerimento());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					if (obj.getContaCorrentePadraoNegociacao().intValue() > 0) {
						sqlInserir.setInt(++i, obj.getContaCorrentePadraoNegociacao());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					if (obj.getContaCorrentePadraoDevolucaoCheque().intValue() > 0) {
						sqlInserir.setInt(++i, obj.getContaCorrentePadraoDevolucaoCheque());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					sqlInserir.setString(++i, obj.getInformacoesAdicionaisEndereco());
//					Uteis.setValuePreparedStatement(obj.getCentroResultadoRequerimentoVO(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getCodigoIESMantenedora(), ++i, sqlInserir);
					
					Uteis.setValuePreparedStatement(obj.getCnpjMantenedora(), ++i, sqlInserir);
					if (obj.getInformarDadosRegistradora()) {
						Uteis.setValuePreparedStatement(obj.getUnidadeCertificadora(), ++i, sqlInserir);
						Uteis.setValuePreparedStatement(obj.getCnpjUnidadeCertificadora(), ++i, sqlInserir);
						Uteis.setValuePreparedStatement(obj.getCodigoIESUnidadeCertificadora(), ++i, sqlInserir);
					} else {
						sqlInserir.setString(++i, "");
						sqlInserir.setString(++i, "");
						sqlInserir.setInt(++i, 0);
					}
					if (obj.getConfiguracaoGEDVO() != null && obj.getConfiguracaoGEDVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(++i, obj.getConfiguracaoGEDVO().getCodigo());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getConfiguracaoMobileVO().getCodigo())) {
						sqlInserir.setInt(++i, obj.getConfiguracaoMobileVO().getCodigo());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					Uteis.setValuePreparedStatement(obj.getLocalizacaoDiferenciadaEscola(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getUnidadeVinculadaEscolaEducacaoBasica(), ++i, sqlInserir);
					if (Uteis.isAtributoPreenchido(obj.getCodigoEscolaSede())) {
						sqlInserir.setInt(++i, obj.getCodigoEscolaSede());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					Uteis.setValuePreparedStatement(obj.getForneceAguaPotavelConsumoHumano(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getTratamentoLixo(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getBanheiroExclusivoFuncionarios(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getPiscina(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getSalaRepousoAluno(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getSalaArtes(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getSalaMusica(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getSalaDanca(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getSalaMultiuso(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getTerreirao(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getViveiroAnimais(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getCorrimaoGuardaCorpos(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getElevador(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getPisosTateis(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getPortasVaoLivreMinimoOitentaCentimetros(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getRampas(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getSinalizacaoSonora(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getSinalizacaoTatil(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getSinalizacaoVisual(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getNenhumRecursoAcessibilidade(), ++i, sqlInserir);
					if(Uteis.isAtributoPreenchido(obj.getNumeroSalasAulaUtilizadasEscolaDentroPredioEscolar())) {
						Uteis.setValuePreparedStatement(obj.getNumeroSalasAulaUtilizadasEscolaDentroPredioEscolar(), ++i, sqlInserir);
					}else {
						sqlInserir.setNull(++i, 0);
					}
					Uteis.setValuePreparedStatement(obj.getAntenaParabolica(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getComputadores(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getCopiadora(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getImpressora(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getImpressoraMultifuncional(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getScanner(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getAcessoInternetUsoAdiministrativo(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getAcessoInternetUsoProcessoEnsinoAprendizagem(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getAcessoInternetUsoAlunos(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getAcessoInternetComunidade(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getNaoPossuiAcessoInternet(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getAcessoInternetComputadoresEscola(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getAcessoInternetDispositivosPessoais(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getRedeLocalCabo(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getRedeLocalWireless(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getNaoExisteRedeLocal(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getAlimentacaoEscolarAlunos(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getEducacaoEscolarIndigena(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getLinguaIndigena(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getLinguaPortuguesa(), ++i, sqlInserir);
					if (Uteis.isAtributoPreenchido(obj.getCodigoLinguaIndigena1())) {
						Uteis.setValuePreparedStatement(obj.getCodigoLinguaIndigena1(), ++i, sqlInserir);
					}else {
						sqlInserir.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getCodigoLinguaIndigena2())) {
						Uteis.setValuePreparedStatement(obj.getCodigoLinguaIndigena2(), ++i, sqlInserir);
					}else {
						sqlInserir.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getCodigoLinguaIndigena3())) {
						Uteis.setValuePreparedStatement(obj.getCodigoLinguaIndigena3(), ++i, sqlInserir);
					}else {
						sqlInserir.setNull(++i, 0);
					}
					
//					if (obj.getChancelaVO().getCodigo().intValue() != 0) {
//						sqlInserir.setInt(++i, obj.getChancelaVO().getCodigo().intValue());
//					} else {
//						sqlInserir.setNull(++i, 0);
//					}
					Uteis.setValuePreparedStatement(obj.getTipoChancela(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getPorcentagemChancela(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getValorFixoChancela(), ++i, sqlInserir);
					
					if (obj.getResponsavelNotificacaoAlteracaoCronogramaAula().getCodigo().intValue() != 0) {
						sqlInserir.setInt(++i, obj.getResponsavelNotificacaoAlteracaoCronogramaAula().getCodigo().intValue());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					Uteis.setValuePreparedStatement(obj.getOrientadorPadraoEstagio(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getObservacao(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getOperadorResponsavel(), ++i, sqlInserir);
//					if (Uteis.isAtributoPreenchido(obj.getConfiguracaoDiplomaDigital())) {
//						sqlInserir.setInt(++i, obj.getConfiguracaoDiplomaDigital().getCodigo());
//					} else {
//						sqlInserir.setNull(++i, 0);
//					}
					if (obj.getDataCredenciamento() != null) {
						sqlInserir.setDate(++i, Uteis.getDataJDBC(obj.getDataCredenciamento()));
					} else {
						sqlInserir.setNull(++i, 0);
					}
					sqlInserir.setString(++i, obj.getVeiculoPublicacaoCredenciamento());
					sqlInserir.setInt(++i, obj.getSecaoPublicacaoCredenciamento());
					sqlInserir.setInt(++i, obj.getPaginaPublicacaoCredenciamento());
					sqlInserir.setInt(++i, obj.getNumeroDOUCredenciamento());
					if (Uteis.isAtributoPreenchido(obj.getTipoAutorizacaoEnum())) {
						sqlInserir.setString(++i, obj.getTipoAutorizacaoEnum().name());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					sqlInserir.setBoolean(++i, obj.getInformarDadosRegistradora());
					sqlInserir.setBoolean(++i, obj.getUtilizarEnderecoUnidadeEnsinoRegistradora());
					sqlInserir.setString(++i, obj.getCepRegistradora());
					if (Uteis.isAtributoPreenchido(obj.getCidadeRegistradora())) {
						sqlInserir.setInt(++i, obj.getCidadeRegistradora().getCodigo());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					sqlInserir.setString(++i, obj.getComplementoRegistradora());
					sqlInserir.setString(++i, obj.getBairroRegistradora());
					sqlInserir.setString(++i, obj.getEnderecoRegistradora());
					sqlInserir.setString(++i, obj.getNumeroRegistradora());
					sqlInserir.setBoolean(++i, obj.getUtilizarCredenciamentoUnidadeEnsino());
					sqlInserir.setString(++i, obj.getNumeroCredenciamentoRegistradora());
					if (obj.getDataCredenciamentoRegistradora() != null) {
						sqlInserir.setDate(++i, Uteis.getDataJDBC(obj.getDataCredenciamentoRegistradora()));
					} else {
						sqlInserir.setNull(++i, 0);
					}
					if (obj.getDataPublicacaoDORegistradora() != null) {
						sqlInserir.setDate(++i, Uteis.getDataJDBC(obj.getDataPublicacaoDORegistradora()));
					} else {
						sqlInserir.setNull(++i, 0);
					}
					sqlInserir.setString(++i, obj.getVeiculoPublicacaoCredenciamentoRegistradora());
					sqlInserir.setInt(++i, obj.getSecaoPublicacaoCredenciamentoRegistradora());
					sqlInserir.setInt(++i, obj.getPaginaPublicacaoCredenciamentoRegistradora());
					sqlInserir.setInt(++i, obj.getNumeroPublicacaoCredenciamentoRegistradora());
					sqlInserir.setBoolean(++i, obj.getUtilizarMantenedoraUnidadeEnsino());
					sqlInserir.setString(++i, obj.getMantenedoraRegistradora());
					sqlInserir.setString(++i, obj.getCnpjMantenedoraRegistradora());
					sqlInserir.setString(++i, obj.getCepMantenedoraRegistradora());
					sqlInserir.setString(++i, obj.getEnderecoMantenedoraRegistradora());
					sqlInserir.setString(++i, obj.getNumeroMantenedoraRegistradora());
					if (Uteis.isAtributoPreenchido(obj.getCidadeMantenedoraRegistradora())) {
						sqlInserir.setInt(++i, obj.getCidadeMantenedoraRegistradora().getCodigo());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					sqlInserir.setString(++i, obj.getComplementoMantenedoraRegistradora());
					sqlInserir.setString(++i, obj.getBairroMantenedoraRegistradora());
					sqlInserir.setBoolean(++i, obj.getUtilizarEnderecoUnidadeEnsinoMantenedora());
					sqlInserir.setString(++i, obj.getCepMantenedora());
					sqlInserir.setString(++i, obj.getEnderecoMantenedora());
					sqlInserir.setString(++i, obj.getNumeroMantenedora());
					if (Uteis.isAtributoPreenchido(obj.getCidadeMantenedora())) {
						sqlInserir.setInt(++i, obj.getCidadeMantenedora().getCodigo());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					sqlInserir.setString(++i, obj.getComplementoMantenedora());
					sqlInserir.setString(++i, obj.getBairroMantenedora());
					sqlInserir.setString(++i, obj.getNumeroRecredenciamento());
					if (obj.getDataRecredenciamento() != null) {
						sqlInserir.setDate(++i, Uteis.getDataJDBC(obj.getDataRecredenciamento()));
					} else {
						sqlInserir.setNull(++i, 0);
					}
					if (obj.getDataPublicacaoRecredenciamento() != null) {
						sqlInserir.setDate(++i, Uteis.getDataJDBC(obj.getDataPublicacaoRecredenciamento()));
					} else {
						sqlInserir.setNull(++i, 0);
					}
					sqlInserir.setString(++i, obj.getVeiculoPublicacaoRecredenciamento());
					sqlInserir.setInt(++i, obj.getSecaoPublicacaoRecredenciamento());
					sqlInserir.setInt(++i, obj.getPaginaPublicacaoRecredenciamento());
					sqlInserir.setInt(++i, obj.getNumeroDOURecredenciamento());
					if (Uteis.isAtributoPreenchido(obj.getTipoAutorizacaoRecredenciamento())) {
						sqlInserir.setString(++i, obj.getTipoAutorizacaoRecredenciamento().name());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					if (obj.getTipoAutorizacaoCredenciamentoRegistradora() != null) {
						sqlInserir.setString(++i, obj.getTipoAutorizacaoCredenciamentoRegistradora().name());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					sqlInserir.setString(++i, obj.getNumeroRenovacaoRecredenciamento());
					if (obj.getDataRenovacaoRecredenciamento() != null) {
						sqlInserir.setDate(++i, Uteis.getDataJDBC(obj.getDataRenovacaoRecredenciamento()));
					} else {
						sqlInserir.setNull(++i, 0);
					}
					if (obj.getDataPublicacaoRenovacaoRecredenciamento() != null) {
						sqlInserir.setDate(++i, Uteis.getDataJDBC(obj.getDataPublicacaoRenovacaoRecredenciamento()));
					} else {
						sqlInserir.setNull(++i, 0);
					}
					sqlInserir.setString(++i, obj.getVeiculoPublicacaoRenovacaoRecredenciamento());
					sqlInserir.setInt(++i, obj.getSecaoPublicacaoRenovacaoRecredenciamento());
					sqlInserir.setInt(++i, obj.getPaginaPublicacaoRenovacaoRecredenciamento());
					sqlInserir.setInt(++i, obj.getNumeroDOURenovacaoRecredenciamento());
					if (Uteis.isAtributoPreenchido(obj.getTipoAutorizacaoRenovacaoRecredenciamento())) {
						sqlInserir.setString(++i, obj.getTipoAutorizacaoRenovacaoRecredenciamento().name());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					sqlInserir.setString(++i, obj.getNumeroCredenciamentoEAD());
					sqlInserir.setString(++i, obj.getCredenciamentoEAD());
					if (obj.getDataCredenciamentoEAD() != null) {
						sqlInserir.setDate(++i, Uteis.getDataJDBC(obj.getDataCredenciamentoEAD()));
					} else {
						sqlInserir.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getDataPublicacaoDOEAD())) {
						sqlInserir.setDate(++i, Uteis.getDataJDBC(obj.getDataPublicacaoDOEAD()));
					} else {
						sqlInserir.setNull(++i, 0);
					}
					sqlInserir.setString(++i, obj.getCredenciamentoPortariaEAD());
					sqlInserir.setString(++i, obj.getVeiculoPublicacaoCredenciamentoEAD());
					sqlInserir.setInt(++i, obj.getSecaoPublicacaoCredenciamentoEAD());
					sqlInserir.setInt(++i, obj.getPaginaPublicacaoCredenciamentoEAD());
					sqlInserir.setInt(++i, obj.getNumeroDOUCredenciamentoEAD());
					if (Uteis.isAtributoPreenchido(obj.getTipoAutorizacaoEAD())) {
						sqlInserir.setString(++i, obj.getTipoAutorizacaoEAD().name());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					sqlInserir.setString(++i, obj.getNumeroRecredenciamentoEAD());
					if (obj.getDataRecredenciamentoEAD() != null) {
						sqlInserir.setDate(++i, Uteis.getDataJDBC(obj.getDataRecredenciamentoEAD()));
					} else {
						sqlInserir.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getDataPublicacaoRecredenciamentoEAD())) {
						sqlInserir.setDate(++i, Uteis.getDataJDBC(obj.getDataPublicacaoRecredenciamentoEAD()));
					} else {
						sqlInserir.setNull(++i, 0);
					}
					sqlInserir.setString(++i, obj.getVeiculoPublicacaoRecredenciamentoEAD());
					sqlInserir.setInt(++i, obj.getSecaoPublicacaoRecredenciamentoEAD());
					sqlInserir.setInt(++i, obj.getPaginaPublicacaoRecredenciamentoEAD());
					sqlInserir.setInt(++i, obj.getNumeroDOURecredenciamentoEAD());
					if (Uteis.isAtributoPreenchido(obj.getTipoAutorizacaoRecredenciamentoEAD())) {
						sqlInserir.setString(++i, obj.getTipoAutorizacaoRecredenciamentoEAD().name());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					sqlInserir.setString(++i, obj.getNumeroRenovacaoRecredenciamentoEAD());
					if (obj.getDataRenovacaoRecredenciamentoEAD() != null) {
						sqlInserir.setDate(++i, Uteis.getDataJDBC(obj.getDataRenovacaoRecredenciamentoEAD()));
					} else {
						sqlInserir.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getDataPublicacaoRenovacaoRecredenciamentoEAD())) {
						sqlInserir.setDate(++i, Uteis.getDataJDBC(obj.getDataPublicacaoRenovacaoRecredenciamentoEAD()));
					} else {
						sqlInserir.setNull(++i, 0);
					}
					sqlInserir.setString(++i, obj.getVeiculoPublicacaoRenovacaoRecredenciamentoEAD());
					sqlInserir.setInt(++i, obj.getSecaoPublicacaoRenovacaoRecredenciamentoEAD());
					sqlInserir.setInt(++i, obj.getPaginaPublicacaoRenovacaoRecredenciamentoEAD());
					sqlInserir.setInt(++i, obj.getNumeroDOURenovacaoRecredenciamentoEAD());
					if (Uteis.isAtributoPreenchido(obj.getTipoAutorizacaoRenovacaoRecredenciamentoEAD())) {
						sqlInserir.setString(++i, obj.getTipoAutorizacaoRenovacaoRecredenciamentoEAD().name());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					sqlInserir.setBoolean(++i, obj.getCredenciamentoEmTramitacao());
					if (obj.getCredenciamentoEmTramitacao()) {
						sqlInserir.setString(++i, obj.getNumeroProcessoCredenciamento());
						sqlInserir.setString(++i, obj.getTipoProcessoCredenciamento());
						sqlInserir.setDate(++i, Uteis.getDataJDBC(obj.getDataCadastroCredenciamento()));
						sqlInserir.setDate(++i, Uteis.getDataJDBC(obj.getDataProtocoloCredenciamento()));
					} else {
						sqlInserir.setNull(++i, 0);
						sqlInserir.setNull(++i, 0);
						sqlInserir.setNull(++i, 0);
						sqlInserir.setNull(++i, 0);
					}
					sqlInserir.setBoolean(++i, obj.getRecredenciamentoEmTramitacao());
					if (obj.getRecredenciamentoEmTramitacao()) {
						sqlInserir.setString(++i, obj.getNumeroProcessoRecredenciamento());
						sqlInserir.setString(++i, obj.getTipoProcessoRecredenciamento());
						sqlInserir.setDate(++i, Uteis.getDataJDBC(obj.getDataCadastroRecredenciamento()));
						sqlInserir.setDate(++i, Uteis.getDataJDBC(obj.getDataProtocoloRecredenciamento()));
					} else {
						sqlInserir.setNull(++i, 0);
						sqlInserir.setNull(++i, 0);
						sqlInserir.setNull(++i, 0);
						sqlInserir.setNull(++i, 0);
					}
					sqlInserir.setBoolean(++i, obj.getRenovacaoRecredenciamentoEmTramitacao());
					if (obj.getRenovacaoRecredenciamentoEmTramitacao()) {
						sqlInserir.setString(++i, obj.getNumeroProcessoRenovacaoRecredenciamento());
						sqlInserir.setString(++i, obj.getTipoProcessoRenovacaoRecredenciamento());
						sqlInserir.setDate(++i, Uteis.getDataJDBC(obj.getDataCadastroRenovacaoRecredenciamento()));
						sqlInserir.setDate(++i, Uteis.getDataJDBC(obj.getDataProtocoloRenovacaoRecredenciamento()));
					} else {
						sqlInserir.setNull(++i, 0);
						sqlInserir.setNull(++i, 0);
						sqlInserir.setNull(++i, 0);
						sqlInserir.setNull(++i, 0);
					}
					sqlInserir.setBoolean(++i, obj.getCredenciamentoEadEmTramitacao());
					if (obj.getCredenciamentoEadEmTramitacao()) {
						sqlInserir.setString(++i, obj.getNumeroProcessoCredenciamentoEad());
						sqlInserir.setString(++i, obj.getTipoProcessoCredenciamentoEad());
						sqlInserir.setDate(++i, Uteis.getDataJDBC(obj.getDataCadastroCredenciamentoEad()));
						sqlInserir.setDate(++i, Uteis.getDataJDBC(obj.getDataProtocoloCredenciamentoEad()));
					} else {
						sqlInserir.setNull(++i, 0);
						sqlInserir.setNull(++i, 0);
						sqlInserir.setNull(++i, 0);
						sqlInserir.setNull(++i, 0);
					}
					sqlInserir.setBoolean(++i, obj.getRecredenciamentoEmTramitacaoEad());
					if (obj.getRecredenciamentoEmTramitacaoEad()) {
						sqlInserir.setString(++i, obj.getNumeroProcessoRecredenciamentoEad());
						sqlInserir.setString(++i, obj.getTipoProcessoRecredenciamentoEad());
						sqlInserir.setDate(++i, Uteis.getDataJDBC(obj.getDataCadastroRecredenciamentoEad()));
						sqlInserir.setDate(++i, Uteis.getDataJDBC(obj.getDataProtocoloRecredenciamentoEad()));
					} else {
						sqlInserir.setNull(++i, 0);
						sqlInserir.setNull(++i, 0);
						sqlInserir.setNull(++i, 0);
						sqlInserir.setNull(++i, 0);
					}
					sqlInserir.setBoolean(++i, obj.getRenovacaoRecredenciamentoEmTramitacaoEad());
					if (obj.getRenovacaoRecredenciamentoEmTramitacaoEad()) {
						sqlInserir.setString(++i, obj.getNumeroProcessoRenovacaoRecredenciamentoEad());
						sqlInserir.setString(++i, obj.getTipoProcessoRenovacaoRecredenciamentoEad());
						sqlInserir.setDate(++i, Uteis.getDataJDBC(obj.getDataCadastroRenovacaoRecredenciamentoEad()));
						sqlInserir.setDate(++i, Uteis.getDataJDBC(obj.getDataProtocoloRenovacaoRecredenciamentoEad()));
					} else {
						sqlInserir.setNull(++i, 0);
						sqlInserir.setNull(++i, 0);
						sqlInserir.setNull(++i, 0);
						sqlInserir.setNull(++i, 0);
					}
					sqlInserir.setBoolean(++i, obj.getCredenciamentoRegistradoraEmTramitacao());
					sqlInserir.setString(++i, obj.getNumeroProcessoCredenciamentoRegistradora());
					sqlInserir.setString(++i, obj.getTipoProcessoCredenciamentoRegistradora());
					sqlInserir.setDate(++i, Uteis.getDataJDBC(obj.getDataCadastroCredenciamentoRegistradora()));
					sqlInserir.setDate(++i, Uteis.getDataJDBC(obj.getDataProtocoloCredenciamentoRegistradora()));
					sqlInserir.setString(++i, obj.getNumeroCredenciamento());
					sqlInserir.setInt(++i, obj.getNumeroVagaOfertada());
					return sqlInserir;
				}
			}, new ResultSetExtractor<Integer>() {

				public Integer extractData(ResultSet rs) throws SQLException {
					if (rs.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
			getFacadeFactory().getUnidadeEnsinoCursoFacade().incluirUnidadeEnsinoCursos(obj.getCodigo(), obj.getUnidadeEnsinoCursoVOs(), usuario);
			getFacadeFactory().getMaterialUnidadeEnsinoFacade().incluirMaterialUnidadeEnsinos(obj.getCodigo(), obj.getMaterialUnidadeEnsinoVOs(), usuario, configuracaoGeralSistemaVO);
			obj.setNovoObj(Boolean.FALSE);
			getFacadeFactory().getEnderecoFacade().incluirNovoCep(obj, usuario);
			validarCentroResultadoExistentePorUnidadeEnsino(obj, usuario);
			atualizarUnidadeEnsinoCampoCentroResultado(obj, usuario);

			validarSeRegistroForamExcluidoDasListaSubordinadas(obj.getUnidadeEnsinoCursoCentroResultado(), "unidadeEnsinoCursoCentroResultado", idEntidade, obj.getCodigo(), usuario);
			validarSeRegistroForamExcluidoDasListaSubordinadas(obj.getUnidadeEnsinoNivelEducacionalCentroResultado(), "unidadeEnsinoNivelEducacionalCentroResultado", idEntidade, obj.getCodigo(), usuario);
			validarSeRegistroForamExcluidoDasListaSubordinadas(obj.getUnidadeEnsinoTipoRequerimentoCentroResultado(), "unidadeEnsinoTipoRequerimentoCentroResultado", idEntidade, obj.getCodigo(), usuario);
			
			getFacadeFactory().getUnidadeEnsinoCursoCentroResultadoFacade().persistir(obj.getUnidadeEnsinoCursoCentroResultado(), false, usuario);
			getFacadeFactory().getUnidadeEnsinoNivelEducacionalCentroResultadoFacade().persistir(obj.getUnidadeEnsinoNivelEducacionalCentroResultado(), false, usuario);
			getFacadeFactory().getUnidadeEnsinoTipoRequerimentoCentroResultadoFacade().persistir(obj.getUnidadeEnsinoTipoRequerimentoCentroResultado(), false, usuario);
			
			validarCentroResultadoExistentePorUnidadeEnsinoCurso(obj, usuario);
			obj.getUnidadeEnsinoCursoCentroResultado().forEach(p ->	p.setEdicaoManual(false));
			obj.getUnidadeEnsinoNivelEducacionalCentroResultado().forEach(p ->	p.setEdicaoManual(false));
		} catch (Exception e) {
			List<UnidadeEnsinoCursoCentroResultadoVO> unidadeEnsinoCursoCentroResultadoVOs = obj.getUnidadeEnsinoCursoCentroResultado().stream().filter(p -> !p.isEdicaoManual()).collect(Collectors.toList());
			obj.setUnidadeEnsinoCursoCentroResultado(unidadeEnsinoCursoCentroResultadoVOs);
			List<UnidadeEnsinoNivelEducacionalCentroResultadoVO> unidadeEnsinoNivelEducacionalCentroResultadoVOs = obj.getUnidadeEnsinoNivelEducacionalCentroResultado().stream().filter(p -> !p.isEdicaoManual()).collect(Collectors.toList());
			obj.setUnidadeEnsinoNivelEducacionalCentroResultado(unidadeEnsinoNivelEducacionalCentroResultadoVOs);
			throw e;
		}
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe <code>UnidadeEnsinoVO</code>. Sempre utiliza a chave primária da classe
	 * como atributo para localização do registro a ser alterado. Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a
	 * conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>alterar</code>
	 * da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>UnidadeEnsinoVO</code> que será alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final UnidadeEnsinoVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception {
		try {
			UnidadeEnsinoVO.validarDados(obj);
			salvarLogoUnidadeEnsino(obj, configuracaoGeralSistemaVO);
			alterar(getIdEntidade(), true, usuario);
			final String sql = "UPDATE UnidadeEnsino set nome=?, razaoSocial=?, endereco=?, setor=?, numero=?, complemento=?, cidade=?, "
					+ "CEP=?, tipoEmpresa=?, CNPJ=?, " + "inscEstadual=?, RG=?, CPF=?, telComercial1=?, telComercial2=?, telComercial3=?, "
							+ "email=?, site=?, fax=?, matriz=?, abreviatura=?, " + "configuracoes=?, ano=?, numeroDocumento=?, "
									+ "codigoies=?, diretorGeral=?, credenciamentoportaria=?, datapublicacaodo=?, mantenedora=?, " 
							+ "permitirVisualizacaoLogin=?, desativada=?, apresentarTelaProcessoSeletivo=?, "
							+ "responsavelCobrancaUnidade=?, contaCorrentePadrao=?, caminhoBaseLogo = ?, "
							+ "nomeArquivoLogo = ?, caminhoBaseLogoIndex = ?, nomeArquivoLogoIndex = ? , "
							+ "caminhoBaseLogoRelatorio = ?, nomeArquivoLogoRelatorio = ? , credenciamento=?, "
							+ "configuracaonotafiscal=?, inscmunicipal=?, nomeExpedicaoDiploma=?, dependenciaAdministrativa=?,localizacaoZonaEscola=?,"
							+ "categoriaEscolaPrivada=?,conveniadaPoderPublico=?,localFuncionamentoDaEscola=?,formaOcupacaoPredio=?,predioCompartilhado=?,  "
							+ "codigoEscolaCompartilhada1=?,codigoEscolaCompartilhada2=?,codigoEscolaCompartilhada3=?,"
							+ "codigoEscolaCompartilhada4=?,codigoEscolaCompartilhada5=?,codigoEscolaCompartilhada6=?,"
							+ "aguaConsumida=?,abastecimentoAgua=?,abastecimentoEnergia=?,esgotoSanitario=?,destinoLixo=?, "
							+ "salaDiretoria=?,salaProfessores=?,salaSecretaria=?,laboratorioInformatica=?,laboratorioCiencias=?,recursosMultifuncionais=?,quadraEsportesCoberta=?,quadraEsportesDescoberta=?,"
							+ "cozinha=?,biblioteca=?,salaLeitura=?,parqueInfantil=?,bercario=?,banheiroForaPredio=?,banheiroDentroPredio=?,banheiroEducacaoInfantil=?,banheiroDeficiencia=?,"
							+ "viasDeficiencia=?,banheiroChuveiro=?,refeitorio=?,despensa=?,almoxarifado=?,auditorio=?,patioCoberto=?,patioDescoberto=?,alojamentoAluno=?,alojamentoProfessor=?,"
							+ "areaVerde=?,lavanderia=?,nenhumaDependencia=?,numeroSalasAulaExistente=?,numeroSalasDentroForaPredio=?, "
							+ "quantidadeTelevisao=?,quantidadeVideoCassete=?,quantidadeDVD=?,quantidadeAntenaParabolica=?,quantidadeCopiadora=?, " // 5
							+ "quantidadeRetroprojetor=?,quantidadeImpressora=?,quantidadeAparelhoSom=?,quantidadeProjetorMultimidia=?,quantidadeFax=?,"//5
							+ "quantidadeMaquinaFotograficaFilmadora=?,quantidadeComputadores=?,quantidadeComputadoresAdministrativos=?,quantidadeComputadoresAlunos=?,computadoresAcessoInternet=?,"//5
							+ "internetBandaLarga=?,caixaPostal=?, codigoTributacaoMunicipio=?, codigoOrgaoRegionalEnsino=?, codigoDistritoCenso=?, apresentarHomePreInscricao=?, leiCriacao1=?, leiCriacao2=?, nomeArquivoLogoPaginaInicial=?, caminhoBaseLogoPaginaInicial=?,"//1
							+ "caminhoBaseLogoEmailCima=?, nomeArquivoLogoEmailCima=?, caminhoBaseLogoEmailBaixo=?, nomeArquivoLogoEmailBaixo=?, "
							+ "caminhobaselogomunicipio=?, nomearquivologomunicipio=?, nomearquivologoaplicativo=?, caminhobaselogoaplicativo=?, codigoIntegracaoContabil = ?, usarConfiguracaoPadrao = ?,  "
							+ "configuracaoContabil = ?, centroResultado=?, contaCorrentePadraoProcessoSeletivo=?, contaCorrentePadraoMatricula=?, contaCorrentePadraoMensalidade=?, contaCorrentePadraoMaterialDidatico=?, contaCorrentePadraoBiblioteca=?, contaCorrentePadraoRequerimento=?, contaCorrentePadraoNegociacao=?, contaCorrentePadraoDevolucaoCheque=?, informacoesAdicionaisEndereco=?, "
							+ "centroResultadoRequerimento=?, codigoIESMantenedora=? , cnpjmantenedora=?, unidadecertificadora=?, cnpjunidadecertificadora=?, codigoiesunidadecertificadora=?,configuracaoGED=?, configuracaomobile=?, localizacaoDiferenciadaEscola=?, unidadeVinculadaEscolaEducacaoBasica=?, codigoEscolaSede=?, forneceAguaPotavelConsumoHumano=?, tratamentoLixo=?, banheiroExclusivoFuncionarios=?, piscina=?, "
							+ " salaRepousoAluno=? , salaArtes=? , salaMusica=? , salaDanca=? , salaMultiuso=? , terreirao=? , viveiroAnimais=?, corrimaoGuardaCorpos=? , elevador=? , pisosTateis=? , portasVaoLivreMinimoOitentaCentimetros=? , rampas=? , sinalizacaoSonora=? , sinalizacaoTatil=? , sinalizacaoVisual=? , nenhumRecursoAcessibilidade=?, numeroSalasAulaUtilizadasEscolaDentroPredioEscolar=?,  "
							+ " antenaParabolica=?, computadores=?, copiadora=?,  impressora=?, impressoraMultifuncional=?, scanner=?, acessoInternetUsoAdiministrativo=?, acessoInternetUsoProcessoEnsinoAprendizagem=?, acessoInternetUsoAlunos=?, acessoInternetComunidade=?, naoPossuiAcessoInternet=?, acessoInternetComputadoresEscola=?, acessoInternetDispositivosPessoais=?, "
							+ " redeLocalCabo=? , redeLocalWireless=?, NaoExisteRedeLocal=?, alimentacaoEscolarAlunos=?, educacaoEscolarIndigena=?, linguaIndigena=?, linguaPortuguesa=?, codigoLinguaIndigena1=?, codigoLinguaIndigena2=?, codigoLinguaIndigena3=?, chancela=?, tipochancela=?, porcentagemchancela=?, valorfixochancela=?   , responsavelNotificacaoAlteracaoCronogramaAula=?, "
							+ " orientadorPadraoEstagio=?, observacao=? , operadorResponsavel=?, configuracaoDiplomaDigital=?, dataCredenciamento=?, veiculoPublicacaoCredenciamento=?, secaoPublicacaoCredenciamento=?, paginaPublicacaoCredenciamento=?, numeroDOUCredenciamento=?, tipoAutorizacaoEnum=?, informarDadosRegistradora=?, utilizarEnderecoUnidadeEnsinoRegistradora=?, cepRegistradora=?, cidadeRegistradora=?, complementoRegistradora=?, bairroRegistradora=?, enderecoRegistradora=?, numeroRegistradora=?, "
							+ "utilizarCredenciamentoUnidadeEnsino=?, numeroCredenciamentoRegistradora=?, dataCredenciamentoRegistradora=?, dataPublicacaoDORegistradora=?, veiculoPublicacaoCredenciamentoRegistradora=?, secaoPublicacaoCredenciamentoRegistradora=?, paginaPublicacaoCredenciamentoRegistradora=?, numeroPublicacaoCredenciamentoRegistradora=?, "
							+ "utilizarMantenedoraUnidadeEnsino=?, mantenedoraRegistradora=?, cnpjMantenedoraRegistradora=?, cepMantenedoraRegistradora=?, enderecoMantenedoraRegistradora=?, numeroMantenedoraRegistradora=?, cidadeMantenedoraRegistradora=?, complementoMantenedoraRegistradora=?, bairroMantenedoraRegistradora=?,"
							+ "utilizarEnderecoUnidadeEnsinoMantenedora=?, cepMantenedora=?, enderecoMantenedora=?, numeroMantenedora=?, cidadeMantenedora=?, complementoMantenedora=?, bairroMantenedora=?, numeroRecredenciamento=?, dataRecredenciamento=?, dataPublicacaoRecredenciamento=?, veiculoPublicacaoRecredenciamento=?, secaoPublicacaoRecredenciamento=?, paginaPublicacaoRecredenciamento=?, numeroDOURecredenciamento=?, tipoAutorizacaoRecredenciamento=?, tipoAutorizacaoCredenciamentoRegistradora=?, "
							+ "numeroRenovacaoRecredenciamento=?, dataRenovacaoRecredenciamento=?, dataPublicacaoRenovacaoRecredenciamento=?, veiculoPublicacaoRenovacaoRecredenciamento=?, secaoPublicacaoRenovacaoRecredenciamento=?, paginaPublicacaoRenovacaoRecredenciamento=?, numeroDOURenovacaoRecredenciamento=?, tipoAutorizacaoRenovacaoRecredenciamento=?, "
							+ "numeroCredenciamentoEAD=?, credenciamentoEAD=?, dataCredenciamentoEAD=?, dataPublicacaoDOEAD=?, credenciamentoPortariaEAD=?, veiculoPublicacaoCredenciamentoEAD=?, secaoPublicacaoCredenciamentoEAD=?, paginaPublicacaoCredenciamentoEAD=?, numeroDOUCredenciamentoEAD=?, tipoAutorizacaoEAD=?, numeroRecredenciamentoEAD=?, dataRecredenciamentoEAD=?, dataPublicacaoRecredenciamentoEAD=?, veiculoPublicacaoRecredenciamentoEAD=?, secaoPublicacaoRecredenciamentoEAD=?, paginaPublicacaoRecredenciamentoEAD=?, numeroDOURecredenciamentoEAD=?, tipoAutorizacaoRecredenciamentoEAD=?, numeroRenovacaoRecredenciamentoEAD=?, dataRenovacaoRecredenciamentoEAD=?, dataPublicacaoRenovacaoRecredenciamentoEAD=?, veiculoPublicacaoRenovacaoRecredenciamentoEAD=?, secaoPublicacaoRenovacaoRecredenciamentoEAD=?, paginaPublicacaoRenovacaoRecredenciamentoEAD=?, numeroDOURenovacaoRecredenciamentoEAD=?, tipoAutorizacaoRenovacaoRecredenciamentoEAD=?, "
							+ "credenciamentoEmTramitacao=?, numeroProcessoCredenciamento=?, tipoProcessoCredenciamento=?, dataCadastroCredenciamento=?, dataProtocoloCredenciamento=?, recredenciamentoEmTramitacao=?, numeroProcessoRecredenciamento=?, tipoProcessoRecredenciamento=?, dataCadastroRecredenciamento=?, dataProtocoloRecredenciamento=?, renovacaoRecredenciamentoEmTramitacao=?, numeroProcessoRenovacaoRecredenciamento=?, tipoProcessoRenovacaoRecredenciamento=?, dataCadastroRenovacaoRecredenciamento=?, dataProtocoloRenovacaoRecredenciamento=?,"
							+ "credenciamentoEadEmTramitacao=?, numeroProcessoCredenciamentoEad=?, tipoProcessoCredenciamentoEad=?, dataCadastroCredenciamentoEad=?, dataProtocoloCredenciamentoEad=?, recredenciamentoEmTramitacaoEad=?, numeroProcessoRecredenciamentoEad=?, tipoProcessoRecredenciamentoEad=?, dataCadastroRecredenciamentoEad=?, dataProtocoloRecredenciamentoEad=?, renovacaoRecredenciamentoEmTramitacaoEad=?, numeroProcessoRenovacaoRecredenciamentoEad=?, tipoProcessoRenovacaoRecredenciamentoEad=?, dataCadastroRenovacaoRecredenciamentoEad=?, dataProtocoloRenovacaoRecredenciamentoEad=?,"
							+ "credenciamentoRegistradoraEmTramitacao=?, numeroProcessoCredenciamentoRegistradora=?, tipoProcessoCredenciamentoRegistradora=?, dataCadastroCredenciamentoRegistradora=?, dataProtocoloCredenciamentoRegistradora=?, numeroCredenciamento=?, numeroVagaOfertada=?  WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getNome());
					sqlAlterar.setString(2, obj.getRazaoSocial());
					sqlAlterar.setString(3, obj.getEndereco());
					sqlAlterar.setString(4, obj.getSetor());
					sqlAlterar.setString(5, obj.getNumero());
					sqlAlterar.setString(6, obj.getComplemento());
					if (Uteis.isAtributoPreenchido(obj.getCidade())) {
						sqlAlterar.setInt(7, obj.getCidade().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(22, 0);
					}
					sqlAlterar.setString(8, obj.getCEP());
					sqlAlterar.setString(9, obj.getTipoEmpresa());
					sqlAlterar.setString(10, obj.getCNPJ());
					sqlAlterar.setString(11, obj.getInscEstadual());
					sqlAlterar.setString(12, obj.getRG());
					sqlAlterar.setString(13, obj.getCPF());
					sqlAlterar.setString(14, obj.getTelComercial1());
					sqlAlterar.setString(15, obj.getTelComercial2());
					sqlAlterar.setString(16, obj.getTelComercial3());
					sqlAlterar.setString(17, obj.getEmail());
					sqlAlterar.setString(18, obj.getSite());
					sqlAlterar.setString(19, obj.getFax());
					sqlAlterar.setBoolean(20, obj.isMatriz().booleanValue());
					sqlAlterar.setString(21, obj.getAbreviatura());
					if (obj.getConfiguracoes().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(22, obj.getConfiguracoes().getCodigo());
					} else {
						sqlAlterar.setNull(22, 0);
					}
					sqlAlterar.setString(23, obj.getAno());
					sqlAlterar.setInt(24, obj.getNumeroDocumento().intValue());
					sqlAlterar.setInt(25, obj.getCodigoIES());
					if (obj.getDiretorGeral().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(26, obj.getDiretorGeral().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(26, 0);
					}
					sqlAlterar.setString(27, obj.getCredenciamentoPortaria());
					sqlAlterar.setDate(28, Uteis.getDataJDBC(obj.getDataPublicacaoDO()));
					sqlAlterar.setString(29, obj.getMantenedora());
					sqlAlterar.setBoolean(30, obj.getPermitirVisualizacaoLogin().booleanValue());
					sqlAlterar.setBoolean(31, obj.getDesativada().booleanValue());
					sqlAlterar.setBoolean(32, obj.getApresentarTelaProcessoSeletivo().booleanValue());
					if (obj.getResponsavelCobrancaUnidade().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(33, obj.getResponsavelCobrancaUnidade().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(33, 0);
					}
//					if (obj.getContaCorrentePadraoVO().getCodigo().intValue() != 0) {
//						sqlAlterar.setInt(34, obj.getContaCorrentePadraoVO().getCodigo().intValue());
//					} else {
						sqlAlterar.setNull(34, 0);
//					}
					sqlAlterar.setString(35, obj.getCaminhoBaseLogo());
					sqlAlterar.setString(36, obj.getNomeArquivoLogo());
					sqlAlterar.setString(37, obj.getCaminhoBaseLogoIndex());
					sqlAlterar.setString(38, obj.getNomeArquivoLogoIndex());
					sqlAlterar.setString(39, obj.getCaminhoBaseLogoRelatorio());
					sqlAlterar.setString(40, obj.getNomeArquivoLogoRelatorio());
					sqlAlterar.setString(41, obj.getCredenciamento());
//					if (obj.getConfiguracaoNotaFiscalVO() != null && obj.getConfiguracaoNotaFiscalVO().getCodigo().intValue() != 0) {
//						sqlAlterar.setInt(42, obj.getConfiguracaoNotaFiscalVO().getCodigo());
//					} else {
//						sqlAlterar.setNull(42, 0);
//					}
					sqlAlterar.setString(43, obj.getInscMunicipal());
					sqlAlterar.setString(44, obj.getNomeExpedicaoDiploma());
					sqlAlterar.setString(45, obj.getDependenciaAdministrativa());
					sqlAlterar.setString(46, obj.getLocalizacaoZonaEscola());
					sqlAlterar.setString(47, obj.getCategoriaEscolaPrivada());
					sqlAlterar.setString(48, obj.getConveniadaPoderPublico());
					sqlAlterar.setString(49, obj.getLocalFuncionamentoDaEscola());
					sqlAlterar.setString(50, obj.getFormaOcupacaoPredio());
					sqlAlterar.setBoolean(51, obj.getPredioCompartilhado());
					sqlAlterar.setString(52, obj.getCodigoEscolaCompartilhada1());
					sqlAlterar.setString(53, obj.getCodigoEscolaCompartilhada2());
					sqlAlterar.setString(54, obj.getCodigoEscolaCompartilhada3());
					sqlAlterar.setString(55, obj.getCodigoEscolaCompartilhada4());
					sqlAlterar.setString(56, obj.getCodigoEscolaCompartilhada5());
					sqlAlterar.setString(57, obj.getCodigoEscolaCompartilhada6());
					sqlAlterar.setString(58, obj.getAguaConsumida());
					sqlAlterar.setString(59, obj.getAbastecimentoAgua());
					sqlAlterar.setString(60, obj.getAbastecimentoEnergia());
					sqlAlterar.setString(61, obj.getEsgotoSanitario());
					sqlAlterar.setString(62, obj.getDestinoLixo());
					sqlAlterar.setBoolean(63, obj.getSalaDiretoria());
					sqlAlterar.setBoolean(64, obj.getSalaProfessores());
					sqlAlterar.setBoolean(65, obj.getSalaSecretaria());
					sqlAlterar.setBoolean(66, obj.getLaboratorioInformatica());
					sqlAlterar.setBoolean(67, obj.getLaboratorioCiencias());
					sqlAlterar.setBoolean(68, obj.getRecursosMultifuncionais());
					sqlAlterar.setBoolean(69, obj.getQuadraEsportesCoberta());
					sqlAlterar.setBoolean(70, obj.getQuadraEsportesDescoberta());
					sqlAlterar.setBoolean(71, obj.getCozinha());
					sqlAlterar.setBoolean(72, obj.getBiblioteca());
					sqlAlterar.setBoolean(73, obj.getSalaLeitura());
					sqlAlterar.setBoolean(74, obj.getParqueInfantil());
					sqlAlterar.setBoolean(75, obj.getBercario());
					sqlAlterar.setBoolean(76, obj.getBanheiroForaPredio());
					sqlAlterar.setBoolean(77, obj.getBanheiroDentroPredio());
					sqlAlterar.setBoolean(78, obj.getBanheiroEducacaoInfantil());
					sqlAlterar.setBoolean(79, obj.getBanheiroDeficiencia());
					sqlAlterar.setBoolean(80, obj.getViasDeficiencia());
					sqlAlterar.setBoolean(81, obj.getBanheiroChuveiro());
					sqlAlterar.setBoolean(82, obj.getRefeitorio());
					sqlAlterar.setBoolean(83, obj.getDespensa());
					sqlAlterar.setBoolean(84, obj.getAlmoxarifado());
					sqlAlterar.setBoolean(85, obj.getAuditorio());
					sqlAlterar.setBoolean(86, obj.getPatioCoberto());
					sqlAlterar.setBoolean(87, obj.getPatioDescoberto());
					sqlAlterar.setBoolean(88, obj.getAlojamentoAluno());
					sqlAlterar.setBoolean(89, obj.getAlojamentoProfessor());
					sqlAlterar.setBoolean(90, obj.getAreaVerde());
					sqlAlterar.setBoolean(91, obj.getLavanderia());
					sqlAlterar.setBoolean(92, obj.getNenhumaDependencia());
					sqlAlterar.setString(93, obj.getNumeroSalasAulaExistente());
					sqlAlterar.setString(94, obj.getNumeroSalasDentroForaPredio());
					if (obj.getQuantidadeTelevisao() != null) {
						sqlAlterar.setInt(95, obj.getQuantidadeTelevisao());
					} else {
						sqlAlterar.setNull(95, 0);
					}
					if (obj.getQuantidadeVideoCassete() != null) {
						sqlAlterar.setInt(96, obj.getQuantidadeVideoCassete());
					} else {
						sqlAlterar.setNull(96, 0);
					}
					if (obj.getQuantidadeDVD() != null) {
						sqlAlterar.setInt(97, obj.getQuantidadeDVD());
					} else {
						sqlAlterar.setNull(97, 0);
					}
					if (obj.getQuantidadeAntenaParabolica() != null) {
						sqlAlterar.setInt(98, obj.getQuantidadeAntenaParabolica());
					} else {
						sqlAlterar.setNull(98, 0);
					}
					if (obj.getQuantidadeCopiadora() != null) {
						sqlAlterar.setInt(99, obj.getQuantidadeCopiadora());
					} else {
						sqlAlterar.setNull(99, 0);
					}
					if (obj.getQuantidadeRetroprojetor() != null) {
						sqlAlterar.setInt(100, obj.getQuantidadeRetroprojetor());
					} else {
						sqlAlterar.setNull(100, 0);
					}
					if (obj.getQuantidadeImpressora() != null) {
						sqlAlterar.setInt(101, obj.getQuantidadeImpressora());
					} else {
						sqlAlterar.setNull(101, 0);
					}
					if (obj.getQuantidadeAparelhoSom() != null) {
						sqlAlterar.setInt(102, obj.getQuantidadeAparelhoSom());
					} else {
						sqlAlterar.setNull(102, 0);
					}
					if (obj.getQuantidadeProjetorMultimidia() != null) {
						sqlAlterar.setInt(103, obj.getQuantidadeProjetorMultimidia());
					} else {
						sqlAlterar.setNull(103, 0);
					}
					if (obj.getQuantidadeFax() != null) {
						sqlAlterar.setInt(104, obj.getQuantidadeFax());
					} else {
						sqlAlterar.setNull(104, 0);
					}
					if (obj.getQuantidadeMaquinaFotograficaFilmadora() != null) {
						sqlAlterar.setInt(105, obj.getQuantidadeMaquinaFotograficaFilmadora());
					} else {
						sqlAlterar.setNull(105, 0);
					}
					if (obj.getQuantidadeComputadores() != null) {
						sqlAlterar.setInt(106, obj.getQuantidadeComputadores());
					} else {
						sqlAlterar.setNull(106, 0);
					}
					if (obj.getQuantidadeComputadoresAdministrativos() != null) {
						sqlAlterar.setInt(107, obj.getQuantidadeComputadoresAdministrativos());
					} else {
						sqlAlterar.setNull(107, 0);
					}
					if (obj.getQuantidadeComputadoresAlunos() != null) {
						sqlAlterar.setInt(108, obj.getQuantidadeComputadoresAlunos());
					} else {
						sqlAlterar.setNull(108, 0);
					}
					sqlAlterar.setBoolean(109, obj.getComputadoresAcessoInternet());
					sqlAlterar.setBoolean(110, obj.getInternetBandaLarga());
					sqlAlterar.setString(111, obj.getCaixaPostal());
					sqlAlterar.setString(112, obj.getCodigoTributacaoMunicipio());
					sqlAlterar.setString(113, obj.getCodigoOrgaoRegionalEnsino());
					sqlAlterar.setString(114, obj.getCodigoDistritoCenso());
					sqlAlterar.setBoolean(115, obj.getApresentarHomePreInscricao());
					sqlAlterar.setString(116, obj.getLeiCriacao1());
					sqlAlterar.setString(117, obj.getLeiCriacao2());
					sqlAlterar.setString(118, obj.getNomeArquivoLogoPaginaInicial());
					sqlAlterar.setString(119, obj.getCaminhoBaseLogoPaginaInicial());

					sqlAlterar.setString(120, obj.getCaminhoBaseLogoEmailCima());
					sqlAlterar.setString(121, obj.getNomeArquivoLogoEmailCima());
					sqlAlterar.setString(122, obj.getCaminhoBaseLogoEmailBaixo());
					sqlAlterar.setString(123, obj.getNomeArquivoLogoEmailBaixo());					
					sqlAlterar.setString(124, obj.getCaminhoBaseLogoMunicipio());
					sqlAlterar.setString(125, obj.getNomeArquivoLogoMunicipio());
					sqlAlterar.setString(126, obj.getNomeArquivoLogoAplicativo());
					sqlAlterar.setString(127, obj.getCaminhoBaseLogoAplicativo());
					sqlAlterar.setString(128, obj.getCodigoIntegracaoContabil());
					sqlAlterar.setBoolean(129, obj.getUsarConfiguracaoPadrao());
					int i = 129;
//					Uteis.setValuePreparedStatement(obj.getConfiguracaoContabilVO(), ++i, sqlAlterar);
//					Uteis.setValuePreparedStatement(obj.getCentroResultadoVO(), ++i, sqlAlterar);
					
					if (obj.getContaCorrentePadraoProcessoSeletivo().intValue() > 0) {
						sqlAlterar.setInt(++i, obj.getContaCorrentePadraoProcessoSeletivo());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (obj.getContaCorrentePadraoMatricula().intValue() > 0) {
						sqlAlterar.setInt(++i, obj.getContaCorrentePadraoMatricula());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (obj.getContaCorrentePadraoMensalidade().intValue() > 0) {
						sqlAlterar.setInt(++i, obj.getContaCorrentePadraoMensalidade());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (obj.getContaCorrentePadraoMaterialDidatico().intValue() > 0) {
						sqlAlterar.setInt(++i, obj.getContaCorrentePadraoMaterialDidatico());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (obj.getContaCorrentePadraoBiblioteca().intValue() > 0) {
						sqlAlterar.setInt(++i, obj.getContaCorrentePadraoBiblioteca());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (obj.getContaCorrentePadraoRequerimento().intValue() > 0) {
						sqlAlterar.setInt(++i, obj.getContaCorrentePadraoRequerimento());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (obj.getContaCorrentePadraoNegociacao().intValue() > 0) {
						sqlAlterar.setInt(++i, obj.getContaCorrentePadraoNegociacao());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (obj.getContaCorrentePadraoDevolucaoCheque().intValue() > 0) {
						sqlAlterar.setInt(++i, obj.getContaCorrentePadraoDevolucaoCheque());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					sqlAlterar.setString(++i, obj.getInformacoesAdicionaisEndereco());
//					Uteis.setValuePreparedStatement(obj.getCentroResultadoRequerimentoVO(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getCodigoIESMantenedora(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getCnpjMantenedora(), ++i, sqlAlterar);
					if (obj.getInformarDadosRegistradora()) {
						Uteis.setValuePreparedStatement(obj.getUnidadeCertificadora(), ++i, sqlAlterar);
						Uteis.setValuePreparedStatement(obj.getCnpjUnidadeCertificadora(), ++i, sqlAlterar);
						Uteis.setValuePreparedStatement(obj.getCodigoIESUnidadeCertificadora(), ++i, sqlAlterar);
					} else {
						sqlAlterar.setString(++i, "");
						sqlAlterar.setString(++i, "");
						sqlAlterar.setInt(++i, 0);
					}
					if (obj.getConfiguracaoGEDVO() != null && obj.getConfiguracaoGEDVO().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(++i, obj.getConfiguracaoGEDVO().getCodigo());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getConfiguracaoMobileVO().getCodigo())) {
						sqlAlterar.setInt(++i, obj.getConfiguracaoMobileVO().getCodigo());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					Uteis.setValuePreparedStatement(obj.getLocalizacaoDiferenciadaEscola(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getUnidadeVinculadaEscolaEducacaoBasica(), ++i, sqlAlterar);
					if (Uteis.isAtributoPreenchido(obj.getCodigoEscolaSede())) {
						sqlAlterar.setInt(++i, obj.getCodigoEscolaSede());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					Uteis.setValuePreparedStatement(obj.getForneceAguaPotavelConsumoHumano(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getTratamentoLixo(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getBanheiroExclusivoFuncionarios(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getPiscina(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getSalaRepousoAluno(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getSalaArtes(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getSalaMusica(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getSalaDanca(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getSalaMultiuso(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getTerreirao(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getViveiroAnimais(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getCorrimaoGuardaCorpos(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getElevador(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getPisosTateis(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getPortasVaoLivreMinimoOitentaCentimetros(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getRampas(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getSinalizacaoSonora(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getSinalizacaoTatil(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getSinalizacaoVisual(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getNenhumRecursoAcessibilidade(), ++i, sqlAlterar);
					if(Uteis.isAtributoPreenchido(obj.getNumeroSalasAulaUtilizadasEscolaDentroPredioEscolar())) {
						Uteis.setValuePreparedStatement(obj.getNumeroSalasAulaUtilizadasEscolaDentroPredioEscolar(), ++i, sqlAlterar);
					}else {
						sqlAlterar.setNull(++i, 0);
					}
					Uteis.setValuePreparedStatement(obj.getAntenaParabolica(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getComputadores(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getCopiadora(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getImpressora(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getImpressoraMultifuncional(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getScanner(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getAcessoInternetUsoAdiministrativo(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getAcessoInternetUsoProcessoEnsinoAprendizagem(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getAcessoInternetUsoAlunos(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getAcessoInternetComunidade(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getNaoPossuiAcessoInternet(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getAcessoInternetComputadoresEscola(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getAcessoInternetDispositivosPessoais(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getRedeLocalCabo(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getRedeLocalWireless(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getNaoExisteRedeLocal(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getAlimentacaoEscolarAlunos(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getEducacaoEscolarIndigena(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getLinguaIndigena(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getLinguaPortuguesa(), ++i, sqlAlterar);
					if (Uteis.isAtributoPreenchido(obj.getCodigoLinguaIndigena1())) {
						Uteis.setValuePreparedStatement(obj.getCodigoLinguaIndigena1(), ++i, sqlAlterar);
					}else {
						sqlAlterar.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getCodigoLinguaIndigena2())) {
						Uteis.setValuePreparedStatement(obj.getCodigoLinguaIndigena2(), ++i, sqlAlterar);
					}else {
						sqlAlterar.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getCodigoLinguaIndigena3())) {
						Uteis.setValuePreparedStatement(obj.getCodigoLinguaIndigena3(), ++i, sqlAlterar);
					}else {
						sqlAlterar.setNull(++i, 0);
					}
					
//					if (obj.getChancelaVO().getCodigo().intValue() != 0) {
//						sqlAlterar.setInt(++i, obj.getChancelaVO().getCodigo().intValue());
//					} else {
//						sqlAlterar.setNull(++i, 0);
//					}
					Uteis.setValuePreparedStatement(obj.getTipoChancela(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getPorcentagemChancela(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getValorFixoChancela(), ++i, sqlAlterar);
					
					if (obj.getResponsavelNotificacaoAlteracaoCronogramaAula().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(++i, obj.getResponsavelNotificacaoAlteracaoCronogramaAula().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					Uteis.setValuePreparedStatement(obj.getOrientadorPadraoEstagio(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getObservacao(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getOperadorResponsavel(), ++i, sqlAlterar);
//					if (Uteis.isAtributoPreenchido(obj.getConfiguracaoDiplomaDigital())) {
//						sqlAlterar.setInt(++i, obj.getConfiguracaoDiplomaDigital().getCodigo());
//					} else {
//						sqlAlterar.setNull(++i, 0);
//					}
					if (obj.getDataCredenciamento() != null) {
						sqlAlterar.setDate(++i, Uteis.getDataJDBC(obj.getDataCredenciamento()));
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					sqlAlterar.setString(++i, obj.getVeiculoPublicacaoCredenciamento());
					sqlAlterar.setInt(++i, obj.getSecaoPublicacaoCredenciamento());
					sqlAlterar.setInt(++i, obj.getPaginaPublicacaoCredenciamento());
					sqlAlterar.setInt(++i, obj.getNumeroDOUCredenciamento());
					if (Uteis.isAtributoPreenchido(obj.getTipoAutorizacaoEnum())) {
						sqlAlterar.setString(++i, obj.getTipoAutorizacaoEnum().name());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					sqlAlterar.setBoolean(++i, obj.getInformarDadosRegistradora());
					sqlAlterar.setBoolean(++i, obj.getUtilizarEnderecoUnidadeEnsinoRegistradora());
					sqlAlterar.setString(++i, obj.getCepRegistradora());
					if (Uteis.isAtributoPreenchido(obj.getCidadeRegistradora())) {
						sqlAlterar.setInt(++i, obj.getCidadeRegistradora().getCodigo());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					sqlAlterar.setString(++i, obj.getComplementoRegistradora());
					sqlAlterar.setString(++i, obj.getBairroRegistradora());
					sqlAlterar.setString(++i, obj.getEnderecoRegistradora());
					sqlAlterar.setString(++i, obj.getNumeroRegistradora());
					sqlAlterar.setBoolean(++i, obj.getUtilizarCredenciamentoUnidadeEnsino());
					sqlAlterar.setString(++i, obj.getNumeroCredenciamentoRegistradora());
					if (obj.getDataCredenciamentoRegistradora() != null) {
						sqlAlterar.setDate(++i, Uteis.getDataJDBC(obj.getDataCredenciamentoRegistradora()));
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (obj.getDataPublicacaoDORegistradora() != null) {
						sqlAlterar.setDate(++i, Uteis.getDataJDBC(obj.getDataPublicacaoDORegistradora()));
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					sqlAlterar.setString(++i, obj.getVeiculoPublicacaoCredenciamentoRegistradora());
					sqlAlterar.setInt(++i, obj.getSecaoPublicacaoCredenciamentoRegistradora());
					sqlAlterar.setInt(++i, obj.getPaginaPublicacaoCredenciamentoRegistradora());
					sqlAlterar.setInt(++i, obj.getNumeroPublicacaoCredenciamentoRegistradora());
					sqlAlterar.setBoolean(++i, obj.getUtilizarMantenedoraUnidadeEnsino());
					sqlAlterar.setString(++i, obj.getMantenedoraRegistradora());
					sqlAlterar.setString(++i, obj.getCnpjMantenedoraRegistradora());
					sqlAlterar.setString(++i, obj.getCepMantenedoraRegistradora());
					sqlAlterar.setString(++i, obj.getEnderecoMantenedoraRegistradora());
					sqlAlterar.setString(++i, obj.getNumeroMantenedoraRegistradora());
					if (Uteis.isAtributoPreenchido(obj.getCidadeMantenedoraRegistradora())) {
						sqlAlterar.setInt(++i, obj.getCidadeMantenedoraRegistradora().getCodigo());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					sqlAlterar.setString(++i, obj.getComplementoMantenedoraRegistradora());
					sqlAlterar.setString(++i, obj.getBairroMantenedoraRegistradora());
					sqlAlterar.setBoolean(++i, obj.getUtilizarEnderecoUnidadeEnsinoMantenedora());
					sqlAlterar.setString(++i, obj.getCepMantenedora());
					sqlAlterar.setString(++i, obj.getEnderecoMantenedora());
					sqlAlterar.setString(++i, obj.getNumeroMantenedora());
					if (Uteis.isAtributoPreenchido(obj.getCidadeMantenedora())) {
						sqlAlterar.setInt(++i, obj.getCidadeMantenedora().getCodigo());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					sqlAlterar.setString(++i, obj.getComplementoMantenedora());
					sqlAlterar.setString(++i, obj.getBairroMantenedora());
					sqlAlterar.setString(++i, obj.getNumeroRecredenciamento());
					if (obj.getDataRecredenciamento() != null) {
						sqlAlterar.setDate(++i, Uteis.getDataJDBC(obj.getDataRecredenciamento()));
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (obj.getDataPublicacaoRecredenciamento() != null) {
						sqlAlterar.setDate(++i, Uteis.getDataJDBC(obj.getDataPublicacaoRecredenciamento()));
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					sqlAlterar.setString(++i, obj.getVeiculoPublicacaoRecredenciamento());
					sqlAlterar.setInt(++i, obj.getSecaoPublicacaoRecredenciamento());
					sqlAlterar.setInt(++i, obj.getPaginaPublicacaoRecredenciamento());
					sqlAlterar.setInt(++i, obj.getNumeroDOURecredenciamento());
					if (Uteis.isAtributoPreenchido(obj.getTipoAutorizacaoRecredenciamento())) {
						sqlAlterar.setString(++i, obj.getTipoAutorizacaoRecredenciamento().name());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (obj.getTipoAutorizacaoCredenciamentoRegistradora() != null) {
						sqlAlterar.setString(++i, obj.getTipoAutorizacaoCredenciamentoRegistradora().name());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					sqlAlterar.setString(++i, obj.getNumeroRenovacaoRecredenciamento());
					if (obj.getDataRenovacaoRecredenciamento() != null) {
						sqlAlterar.setDate(++i, Uteis.getDataJDBC(obj.getDataRenovacaoRecredenciamento()));
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (obj.getDataPublicacaoRenovacaoRecredenciamento() != null) {
						sqlAlterar.setDate(++i, Uteis.getDataJDBC(obj.getDataPublicacaoRenovacaoRecredenciamento()));
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					sqlAlterar.setString(++i, obj.getVeiculoPublicacaoRenovacaoRecredenciamento());
					sqlAlterar.setInt(++i, obj.getSecaoPublicacaoRenovacaoRecredenciamento());
					sqlAlterar.setInt(++i, obj.getPaginaPublicacaoRenovacaoRecredenciamento());
					sqlAlterar.setInt(++i, obj.getNumeroDOURenovacaoRecredenciamento());
					if (Uteis.isAtributoPreenchido(obj.getTipoAutorizacaoRenovacaoRecredenciamento())) {
						sqlAlterar.setString(++i, obj.getTipoAutorizacaoRenovacaoRecredenciamento().name());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					sqlAlterar.setString(++i, obj.getNumeroCredenciamentoEAD());
					sqlAlterar.setString(++i, obj.getCredenciamentoEAD());
					if (obj.getDataCredenciamentoEAD() != null) {
						sqlAlterar.setDate(++i, Uteis.getDataJDBC(obj.getDataCredenciamentoEAD()));
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (obj.getDataPublicacaoDOEAD() != null) {
						sqlAlterar.setDate(++i, Uteis.getDataJDBC(obj.getDataPublicacaoDOEAD()));
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					sqlAlterar.setString(++i, obj.getCredenciamentoPortariaEAD());
					sqlAlterar.setString(++i, obj.getVeiculoPublicacaoCredenciamentoEAD());
					sqlAlterar.setInt(++i, obj.getSecaoPublicacaoCredenciamentoEAD());
					sqlAlterar.setInt(++i, obj.getPaginaPublicacaoCredenciamentoEAD());
					sqlAlterar.setInt(++i, obj.getNumeroDOUCredenciamentoEAD());
					if (Uteis.isAtributoPreenchido(obj.getTipoAutorizacaoEAD())) {
						sqlAlterar.setString(++i, obj.getTipoAutorizacaoEAD().name());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					sqlAlterar.setString(++i, obj.getNumeroRecredenciamentoEAD());
					if (obj.getDataRecredenciamentoEAD() != null) {
						sqlAlterar.setDate(++i, Uteis.getDataJDBC(obj.getDataRecredenciamentoEAD()));
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (obj.getDataPublicacaoRecredenciamentoEAD() != null) {
						sqlAlterar.setDate(++i, Uteis.getDataJDBC(obj.getDataPublicacaoRecredenciamentoEAD()));
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					sqlAlterar.setString(++i, obj.getVeiculoPublicacaoRecredenciamentoEAD());
					sqlAlterar.setInt(++i, obj.getSecaoPublicacaoRecredenciamentoEAD());
					sqlAlterar.setInt(++i, obj.getPaginaPublicacaoRecredenciamentoEAD());
					sqlAlterar.setInt(++i, obj.getNumeroDOURecredenciamentoEAD());
					if (Uteis.isAtributoPreenchido(obj.getTipoAutorizacaoRecredenciamentoEAD())) {
						sqlAlterar.setString(++i, obj.getTipoAutorizacaoRecredenciamentoEAD().name());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					sqlAlterar.setString(++i, obj.getNumeroRenovacaoRecredenciamentoEAD());
					if (obj.getDataRenovacaoRecredenciamentoEAD() != null) {
						sqlAlterar.setDate(++i, Uteis.getDataJDBC(obj.getDataRenovacaoRecredenciamentoEAD()));
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (obj.getDataPublicacaoRenovacaoRecredenciamentoEAD() != null) {
						sqlAlterar.setDate(++i, Uteis.getDataJDBC(obj.getDataPublicacaoRenovacaoRecredenciamentoEAD()));
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					sqlAlterar.setString(++i, obj.getVeiculoPublicacaoRenovacaoRecredenciamentoEAD());
					sqlAlterar.setInt(++i, obj.getSecaoPublicacaoRenovacaoRecredenciamentoEAD());
					sqlAlterar.setInt(++i, obj.getPaginaPublicacaoRenovacaoRecredenciamentoEAD());
					sqlAlterar.setInt(++i, obj.getNumeroDOURenovacaoRecredenciamentoEAD());
					if (Uteis.isAtributoPreenchido(obj.getTipoAutorizacaoRenovacaoRecredenciamentoEAD())) {
						sqlAlterar.setString(++i, obj.getTipoAutorizacaoRenovacaoRecredenciamentoEAD().name());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					sqlAlterar.setBoolean(++i, obj.getCredenciamentoEmTramitacao());
					if (obj.getCredenciamentoEmTramitacao()) {
						sqlAlterar.setString(++i, obj.getNumeroProcessoCredenciamento());
						sqlAlterar.setString(++i, obj.getTipoProcessoCredenciamento());
						sqlAlterar.setDate(++i, Uteis.getDataJDBC(obj.getDataCadastroCredenciamento()));
						sqlAlterar.setDate(++i, Uteis.getDataJDBC(obj.getDataProtocoloCredenciamento()));
					} else {
						sqlAlterar.setNull(++i, 0);
						sqlAlterar.setNull(++i, 0);
						sqlAlterar.setNull(++i, 0);
						sqlAlterar.setNull(++i, 0);
					}
					sqlAlterar.setBoolean(++i, obj.getRecredenciamentoEmTramitacao());
					if (obj.getRecredenciamentoEmTramitacao()) {
						sqlAlterar.setString(++i, obj.getNumeroProcessoRecredenciamento());
						sqlAlterar.setString(++i, obj.getTipoProcessoRecredenciamento());
						sqlAlterar.setDate(++i, Uteis.getDataJDBC(obj.getDataCadastroRecredenciamento()));
						sqlAlterar.setDate(++i, Uteis.getDataJDBC(obj.getDataProtocoloRecredenciamento()));
					} else {
						sqlAlterar.setNull(++i, 0);
						sqlAlterar.setNull(++i, 0);
						sqlAlterar.setNull(++i, 0);
						sqlAlterar.setNull(++i, 0);
					}
					sqlAlterar.setBoolean(++i, obj.getRenovacaoRecredenciamentoEmTramitacao());
					if (obj.getRenovacaoRecredenciamentoEmTramitacao()) {
						sqlAlterar.setString(++i, obj.getNumeroProcessoRenovacaoRecredenciamento());
						sqlAlterar.setString(++i, obj.getTipoProcessoRenovacaoRecredenciamento());
						sqlAlterar.setDate(++i, Uteis.getDataJDBC(obj.getDataCadastroRenovacaoRecredenciamento()));
						sqlAlterar.setDate(++i, Uteis.getDataJDBC(obj.getDataProtocoloRenovacaoRecredenciamento()));
					} else {
						sqlAlterar.setNull(++i, 0);
						sqlAlterar.setNull(++i, 0);
						sqlAlterar.setNull(++i, 0);
						sqlAlterar.setNull(++i, 0);
					}
					sqlAlterar.setBoolean(++i, obj.getCredenciamentoEadEmTramitacao());
					if (obj.getCredenciamentoEadEmTramitacao()) {
						sqlAlterar.setString(++i, obj.getNumeroProcessoCredenciamentoEad());
						sqlAlterar.setString(++i, obj.getTipoProcessoCredenciamentoEad());
						sqlAlterar.setDate(++i, Uteis.getDataJDBC(obj.getDataCadastroCredenciamentoEad()));
						sqlAlterar.setDate(++i, Uteis.getDataJDBC(obj.getDataProtocoloCredenciamentoEad()));
					} else {
						sqlAlterar.setNull(++i, 0);
						sqlAlterar.setNull(++i, 0);
						sqlAlterar.setNull(++i, 0);
						sqlAlterar.setNull(++i, 0);
					}
					sqlAlterar.setBoolean(++i, obj.getRecredenciamentoEmTramitacaoEad());
					if (obj.getRecredenciamentoEmTramitacaoEad()) {
						sqlAlterar.setString(++i, obj.getNumeroProcessoRecredenciamentoEad());
						sqlAlterar.setString(++i, obj.getTipoProcessoRecredenciamentoEad());
						sqlAlterar.setDate(++i, Uteis.getDataJDBC(obj.getDataCadastroRecredenciamentoEad()));
						sqlAlterar.setDate(++i, Uteis.getDataJDBC(obj.getDataProtocoloRecredenciamentoEad()));
					} else {
						sqlAlterar.setNull(++i, 0);
						sqlAlterar.setNull(++i, 0);
						sqlAlterar.setNull(++i, 0);
						sqlAlterar.setNull(++i, 0);
					}
					sqlAlterar.setBoolean(++i, obj.getRenovacaoRecredenciamentoEmTramitacaoEad());
					if (obj.getRenovacaoRecredenciamentoEmTramitacaoEad()) {
						sqlAlterar.setString(++i, obj.getNumeroProcessoRenovacaoRecredenciamentoEad());
						sqlAlterar.setString(++i, obj.getTipoProcessoRenovacaoRecredenciamentoEad());
						sqlAlterar.setDate(++i, Uteis.getDataJDBC(obj.getDataCadastroRenovacaoRecredenciamentoEad()));
						sqlAlterar.setDate(++i, Uteis.getDataJDBC(obj.getDataProtocoloRenovacaoRecredenciamentoEad()));
					} else {
						sqlAlterar.setNull(++i, 0);
						sqlAlterar.setNull(++i, 0);
						sqlAlterar.setNull(++i, 0);
						sqlAlterar.setNull(++i, 0);
					}
					sqlAlterar.setBoolean(++i, obj.getCredenciamentoRegistradoraEmTramitacao());
					sqlAlterar.setString(++i, obj.getNumeroProcessoCredenciamentoRegistradora());
					sqlAlterar.setString(++i, obj.getTipoProcessoCredenciamentoRegistradora());
					sqlAlterar.setDate(++i, Uteis.getDataJDBC(obj.getDataCadastroCredenciamentoRegistradora()));
					sqlAlterar.setDate(++i, Uteis.getDataJDBC(obj.getDataProtocoloCredenciamentoRegistradora()));
					sqlAlterar.setString(++i, obj.getNumeroCredenciamento());
					sqlAlterar.setInt(++i, obj.getNumeroVagaOfertada());
					Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);

					return sqlAlterar;
				}
			});
			
			getFacadeFactory().getUnidadeEnsinoCursoFacade().alterarUnidadeEnsinoCursos(obj.getCodigo(), obj.getUnidadeEnsinoCursoVOs(), usuario);
			getFacadeFactory().getMaterialUnidadeEnsinoFacade().alterarMaterialUnidadeEnsinos(obj.getCodigo(), obj.getMaterialUnidadeEnsinoVOs(), usuario, configuracaoGeralSistemaVO);
			getFacadeFactory().getEnderecoFacade().incluirNovoCep(obj, usuario);
			atualizarUnidadeEnsinoCampoCentroResultado(obj, usuario);
			
			
			validarSeRegistroForamExcluidoDasListaSubordinadas(obj.getUnidadeEnsinoCursoCentroResultado(), "unidadeEnsinoCursoCentroResultado", idEntidade, obj.getCodigo(), usuario);
			validarSeRegistroForamExcluidoDasListaSubordinadas(obj.getUnidadeEnsinoNivelEducacionalCentroResultado(), "unidadeEnsinoNivelEducacionalCentroResultado", idEntidade, obj.getCodigo(), usuario);
			validarSeRegistroForamExcluidoDasListaSubordinadas(obj.getUnidadeEnsinoTipoRequerimentoCentroResultado(), "unidadeEnsinoTipoRequerimentoCentroResultado", idEntidade, obj.getCodigo(), usuario);
			
			getFacadeFactory().getUnidadeEnsinoNivelEducacionalCentroResultadoFacade().persistir(obj.getUnidadeEnsinoNivelEducacionalCentroResultado(), false, usuario);
			getFacadeFactory().getUnidadeEnsinoCursoCentroResultadoFacade().persistir(obj.getUnidadeEnsinoCursoCentroResultado(), false, usuario);
			getFacadeFactory().getUnidadeEnsinoTipoRequerimentoCentroResultadoFacade().persistir(obj.getUnidadeEnsinoTipoRequerimentoCentroResultado(), false, usuario);
			
			validarCentroResultadoExistentePorUnidadeEnsinoCurso(obj, usuario);
//			getAplicacaoControle().removerConfiguracaoFinanceiraNoMapConfiguracaoUnidadeEnsino(obj.getCodigo());
			obj.getUnidadeEnsinoCursoCentroResultado().forEach(p ->	p.setEdicaoManual(false));
			obj.getUnidadeEnsinoNivelEducacionalCentroResultado().forEach(p ->	p.setEdicaoManual(false));
			getAplicacaoControle().removerConfiguracaoGEDVOPorUnidadeEnsino(obj.getCodigo());
		} catch (Exception e) {
			List<UnidadeEnsinoCursoCentroResultadoVO> unidadeEnsinoCursoCentroResultadoVOs = obj.getUnidadeEnsinoCursoCentroResultado().stream().filter(p -> !p.isEdicaoManual()).collect(Collectors.toList());
			obj.setUnidadeEnsinoCursoCentroResultado(unidadeEnsinoCursoCentroResultadoVOs);
			List<UnidadeEnsinoNivelEducacionalCentroResultadoVO> unidadeEnsinoNivelEducacionalCentroResultadoVOs = obj.getUnidadeEnsinoNivelEducacionalCentroResultado().stream().filter(p -> !p.isEdicaoManual()).collect(Collectors.toList());
			obj.setUnidadeEnsinoNivelEducacionalCentroResultado(unidadeEnsinoNivelEducacionalCentroResultadoVOs);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarAnoNumeroDocumento(final UnidadeEnsinoVO obj) throws Exception {
		try {
			final String sql = "UPDATE UnidadeEnsino set ano=?, numeroDocumento=? WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getAno());
					sqlAlterar.setInt(2, obj.getNumeroDocumento().intValue());
					sqlAlterar.setInt(3, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarCoordenadoresTCC(final UnidadeEnsinoVO obj, UsuarioVO usuario) throws Exception {
		try {
			getFacadeFactory().getControleAcessoFacade().alterar("CoordenadorTCC", true, usuario);
			final String sql = "UPDATE UnidadeEnsino set coordenadorTCC=? WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);
					if (obj.getCoordenadorTCC().getCodigo().intValue() == 0) {
						sqlAlterar.setNull(1, 0);
					} else {
						sqlAlterar.setInt(1, obj.getCoordenadorTCC().getCodigo().intValue());
					}
					sqlAlterar.setInt(2, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
			for (UnidadeEnsinoCursoVO unidade : obj.getUnidadeEnsinoCursoVOs()) {
				getFacadeFactory().getUnidadeEnsinoCursoFacade().alterarCoordenadorTCC(unidade, usuario);
			}
			getFacadeFactory().getTrabalhoConclusaoCursoFacade().alterarCoordenadorTCC(obj, usuario);
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	
	private void validarCentroResultadoExistentePorUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino, UsuarioVO usuarioVO) {
//		if(!Uteis.isAtributoPreenchido(unidadeEnsino.getCentroResultadoVO())){
//			CentroResultadoVO obj = getFacadeFactory().getCentroResultadoFacade().validarGeracaoDoCentroResultadoAutomatico(unidadeEnsino.getNome(), null, null, null, false, usuarioVO);
//			unidadeEnsino.setCentroResultadoVO(obj);	
//		}
//		if(!Uteis.isAtributoPreenchido(unidadeEnsino.getCentroResultadoRequerimentoVO())){
//			CentroResultadoVO obj = getFacadeFactory().getCentroResultadoFacade().validarGeracaoDoCentroResultadoAutomatico("Requerimento - "+unidadeEnsino.getNome(), unidadeEnsino.getCodigo(), null, null, true, usuarioVO);
//			unidadeEnsino.setCentroResultadoRequerimentoVO(obj);	
//		}
	}
	
	
	private void validarCentroResultadoExistentePorUnidadeEnsinoCurso(UnidadeEnsinoVO unidadeEnsino, UsuarioVO usuarioVO) {
		try {
		
			for (UnidadeEnsinoCursoVO unidadeEnsinoCursoVO : unidadeEnsino.getUnidadeEnsinoCursoVOs()) {
				boolean existeCentroResultado = unidadeEnsino.getUnidadeEnsinoCursoCentroResultado().stream().anyMatch(p->p.getCursoVO().getCodigo().equals(unidadeEnsinoCursoVO.getCurso().getCodigo()));
				if(!existeCentroResultado){
					
					if(!unidadeEnsino.getUnidadeEnsinoNivelEducacionalCentroResultado().stream().anyMatch(p -> p.getTipoNivelEducacional().equals(TipoNivelEducacional.getEnum(unidadeEnsinoCursoVO.getCurso().getNivelEducacional())))) {
//						CentroResultadoVO centroResultadoNivel = getFacadeFactory().getCentroResultadoFacade().validarGeracaoDoCentroResultadoAutomatico(unidadeEnsinoCursoVO.getCurso().getNivelEducacional_Apresentar() + " - " + unidadeEnsino.getNome(), unidadeEnsino.getCodigo(), null, null, true, usuarioVO);
						UnidadeEnsinoNivelEducacionalCentroResultadoVO nivelEducacionalCentroResultado = new UnidadeEnsinoNivelEducacionalCentroResultadoVO();
//						nivelEducacionalCentroResultado.setCentroResultadoVO(centroResultadoNivel);
						nivelEducacionalCentroResultado.setUnidadeEnsinoVO(unidadeEnsino);
						nivelEducacionalCentroResultado.setTipoNivelEducacional(TipoNivelEducacional.getEnum(unidadeEnsinoCursoVO.getCurso().getNivelEducacional()));
						getFacadeFactory().getUnidadeEnsinoNivelEducacionalCentroResultadoFacade().persistir(nivelEducacionalCentroResultado, false, usuarioVO);
						unidadeEnsino.getUnidadeEnsinoNivelEducacionalCentroResultado().add(nivelEducacionalCentroResultado);
						nivelEducacionalCentroResultado.setEdicaoManual(true);
					}
					
//					CentroResultadoVO obj = getFacadeFactory().getCentroResultadoFacade().validarGeracaoDoCentroResultadoAutomatico(unidadeEnsinoCursoVO.getCurso().getNome(), unidadeEnsino.getCodigo(), TipoNivelEducacional.getEnum(unidadeEnsinoCursoVO.getCurso().getNivelEducacional()), null, true, usuarioVO);
					UnidadeEnsinoCursoCentroResultadoVO uecr = new UnidadeEnsinoCursoCentroResultadoVO();
//					uecr.setCentroResultadoVO(obj);
					uecr.setCursoVO(unidadeEnsinoCursoVO.getCurso());
					uecr.setUnidadeEnsinoVO(unidadeEnsino);
					getFacadeFactory().getUnidadeEnsinoCursoCentroResultadoFacade().persistir(uecr, false, usuarioVO);
					
					addUnidadeEnsinoCursoCentroResultado(unidadeEnsino, uecr, usuarioVO);	
					uecr.setEdicaoManual(true);
					
				}
			}	
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	
	private void atualizarUnidadeEnsinoCampoCentroResultado(UnidadeEnsinoVO obj, UsuarioVO usuario)  {
		try {
			StringBuilder sqlStr = new StringBuilder();
			sqlStr.append(" UPDATE UnidadeEnsino SET ");
//			if(Uteis.isAtributoPreenchido(obj.getCentroResultadoVO())){
//				sqlStr.append(" centroResultado = ").append(obj.getCentroResultadoVO().getCodigo());
//			}else{
//				sqlStr.append(" centroResultado = null ");
//			}
//			if(Uteis.isAtributoPreenchido(obj.getCentroResultadoRequerimentoVO())){
//				sqlStr.append(" ,centroResultadoRequerimento = ").append(obj.getCentroResultadoRequerimentoVO().getCodigo());
//			}else{
//				sqlStr.append(" ,centroResultadoRequerimento = null ");
//			}
			sqlStr.append(" WHERE codigo = ").append(obj.getCodigo()).append(" ");
			sqlStr.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			getConexao().getJdbcTemplate().update(sqlStr.toString());
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void addUnidadeEnsinoCursoCentroResultado(UnidadeEnsinoVO obj, UnidadeEnsinoCursoCentroResultadoVO unidadeEnsinoCentroResultadoVO,  UsuarioVO usuario)  {
		int index = 0;
		unidadeEnsinoCentroResultadoVO.setUnidadeEnsinoVO(obj);
		getFacadeFactory().getUnidadeEnsinoCursoCentroResultadoFacade().validarDadosAntesAdicionar(unidadeEnsinoCentroResultadoVO);
		for (UnidadeEnsinoCursoCentroResultadoVO objExistente : obj.getUnidadeEnsinoCursoCentroResultado()) {
			if (objExistente.equalsCampoSelecaoLista(unidadeEnsinoCentroResultadoVO)) {
				obj.getUnidadeEnsinoCursoCentroResultado().set(index, unidadeEnsinoCentroResultadoVO);
				return;
			}
			index++;
		}
		obj.getUnidadeEnsinoCursoCentroResultado().add(unidadeEnsinoCentroResultadoVO);
		
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void removerUnidadeEnsinoCursoCentroResultado(UnidadeEnsinoVO obj, UnidadeEnsinoCursoCentroResultadoVO unidadeEnsinoCentroResultadoVO,  UsuarioVO usuario)  {
		Iterator<UnidadeEnsinoCursoCentroResultadoVO> i = obj.getUnidadeEnsinoCursoCentroResultado().iterator();
		while (i.hasNext()) {
			UnidadeEnsinoCursoCentroResultadoVO objExistente =  i.next();
			if (objExistente.equalsCampoSelecaoLista(unidadeEnsinoCentroResultadoVO)) {
				i.remove();
				return;
			}
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe <code>UnidadeEnsinoVO</code>. Sempre localiza o registro a ser excluído através da
	 * chave primária da entidade. Primeiramente verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na
	 * entidade. Isto, através da operação <code>excluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>UnidadeEnsinoVO</code> que será removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(UnidadeEnsinoVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			excluir(getIdEntidade(), true, usuarioVO);
			getFacadeFactory().getUnidadeEnsinoCursoFacade().excluirUnidadeEnsinoCursos(obj.getCodigo());
			String sql = "DELETE FROM UnidadeEnsino WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(sql, obj.getCodigo());
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Responsável por realizar uma consulta de <code>UnidadeEnsino</code> através do valor do atributo <code>String CPF</code>. Retorna os objetos,
	 * com início do valor do atributo idêntico ao parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>UnidadeEnsinoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<UnidadeEnsinoVO> consultarPorCPF(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM UnidadeEnsino WHERE CPF like('" + valorConsulta + "%') ORDER BY CPF";
		if (unidadeEnsino != null && unidadeEnsino.intValue() != 0) {
			sqlStr = "SELECT * FROM UnidadeEnsino WHERE CPF like('" + valorConsulta + "%') and codigo = " + unidadeEnsino.intValue() + " ORDER BY CPF";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public UnidadeEnsinoVO consultarPorCPFUnico(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM unidadeensino WHERE CPF like('" + valorConsulta + "%') ORDER BY codigo LIMIT 1";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDados(tabelaResultado, nivelMontarDados, usuario);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public UnidadeEnsinoVO consultaBasicaPorFuncionario(FuncionarioVO funcionario, UsuarioVO usuario) throws Exception {
		StringBuilder str = new StringBuilder(getSQLPadraoConsultaBasica());
		str.append(" WHERE unidadeensino.codigo in ( ");
		str.append(" select");
		str.append(" 	case when t.ue_funcionariocargo is not null then t.ue_funcionariocargo else t.ue_usuario end ");
		str.append(" from (");
		str.append(" 		select");
		str.append(" 			(select unidadeensino.codigo from unidadeensino ");
		str.append(" 			   	inner join funcionariocargo on funcionariocargo.unidadeensino = unidadeensino.codigo ");
		str.append(" 			   	where funcionariocargo.funcionario  = funcionario.codigo and funcionariocargo.ativo ");
		str.append(" 			   	limit 1  ");
		str.append(" 		    ) as ue_funcionariocargo, ");
		str.append(" 			(select unidadeensino.codigo from unidadeensino ");
		str.append(" 				inner join usuarioperfilacesso on usuarioperfilacesso.unidadeensino = unidadeensino.codigo ");
		str.append(" 				inner join usuario on usuario.codigo = usuarioperfilacesso.usuario ");
		str.append(" 				where usuario.pessoa = funcionario.pessoa ");
		str.append(" 				limit 1 ");
		str.append(" 			) as ue_usuario ");
		str.append(" 		from funcionario ");
		str.append(" 		where funcionario.codigo  = ? ");
		str.append(" ) as t ");
		str.append(" )  ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(str.toString(), funcionario.getCodigo());
		UnidadeEnsinoVO obj = new UnidadeEnsinoVO();
		if (tabelaResultado.next()) {
			montarDadosBasico(obj, tabelaResultado, Uteis.NIVELMONTARDADOS_COMBOBOX);
		}
		return obj;
	}

	/**
	 * Responsável por realizar uma consulta de <code>UnidadeEnsino</code> através do valor do atributo <code>String RG</code>. Retorna os objetos,
	 * com início do valor do atributo idêntico ao parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>UnidadeEnsinoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<UnidadeEnsinoVO> consultarPorRG(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM UnidadeEnsino WHERE RG like('" + valorConsulta + "%') ORDER BY RG";
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr = "SELECT * FROM UnidadeEnsino WHERE RG like('" + valorConsulta + "%') and codigo = " + unidadeEnsino.intValue() + " ORDER BY RG";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>UnidadeEnsino</code> através do valor do atributo <code>String inscEstadual</code>. Retorna os
	 * objetos, com início do valor do atributo idêntico ao parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o
	 * trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>UnidadeEnsinoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<UnidadeEnsinoVO> consultarPorInscEstadual(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM UnidadeEnsino WHERE lower (inscEstadual) like('" + valorConsulta.toLowerCase() + "%') ORDER BY inscEstadual";
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr = "SELECT * FROM UnidadeEnsino WHERE lower (inscEstadual) like('" + valorConsulta.toLowerCase() + "%') and codigo = " + unidadeEnsino.intValue() + " ORDER BY inscEstadual";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>UnidadeEnsino</code> através do valor do atributo <code>String CNPJ</code>. Retorna os objetos,
	 * com início do valor do atributo idêntico ao parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>UnidadeEnsinoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<UnidadeEnsinoVO> consultarPorCNPJ(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM UnidadeEnsino WHERE CNPJ like('" + valorConsulta + "%') ORDER BY CNPJ";
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr = "SELECT * FROM UnidadeEnsino WHERE CNPJ like('" + valorConsulta + "%') and codigo = " + unidadeEnsino.intValue() + " ORDER BY CNPJ";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public UnidadeEnsinoVO consultarPorCNPJUnico(String valorConsulta, String nomeConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM unidadeensino WHERE CNPJ like('" + valorConsulta + "%') AND nome = '" + nomeConsulta + "'ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (UnidadeEnsinoVO) montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario).get(0);
	}

	/**
	 * Responsável por realizar uma consulta de <code>UnidadeEnsino</code> através do valor do atributo <code>nome</code> da classe
	 * <code>Cidade</code> Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe <code>UnidadeEnsinoVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<UnidadeEnsinoVO> consultarPorNomeCidade(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT UnidadeEnsino.* FROM UnidadeEnsino, Cidade WHERE UnidadeEnsino.cidade = Cidade.codigo and lower (sem_acentos(Cidade.nome)) ilike(sem_acentos('" + valorConsulta.toLowerCase() + "%')) ORDER BY Cidade.nome";
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr = "SELECT UnidadeEnsino.* FROM UnidadeEnsino, Cidade WHERE UnidadeEnsino.cidade = Cidade.codigo and lower (Cidade.nome) like('" + valorConsulta.toLowerCase() + "%') and codigo = " + unidadeEnsino.intValue() + " ORDER BY Cidade.nome";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<UnidadeEnsinoVO> consultarPorCodigoCurso(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT DISTINCT UnidadeEnsino.* FROM UnidadeEnsino, UnidadeEnsinoCurso WHERE UnidadeEnsino.codigo = UnidadeEnsinoCurso.unidadeEnsino and curso = " + valorConsulta.intValue() + " ORDER BY UnidadeEnsino.nome";
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr = "SELECT DISTINCT UnidadeEnsino.* FROM UnidadeEnsino, UnidadeEnsinoCurso WHERE UnidadeEnsino.codigo = UnidadeEnsinoCurso.unidadeEnsino and curso = " + valorConsulta.intValue() + " and unidadeEnsino.codigo = " + unidadeEnsino + " ORDER BY UnidadeEnsino.nome";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/**
	 * Responsável por realizar uma consulta de <code>UnidadeEnsino</code> através do valor do atributo <code>String razaoSocial</code>. Retorna os
	 * objetos, com início do valor do atributo idêntico ao parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o
	 * trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>UnidadeEnsinoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<UnidadeEnsinoVO> consultarPorRazaoSocial(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM UnidadeEnsino WHERE lower (razaoSocial) like('" + valorConsulta.toLowerCase() + "%') ORDER BY razaoSocial";
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr = "SELECT * FROM UnidadeEnsino WHERE lower (razaoSocial) like('" + valorConsulta.toLowerCase() + "%') and codigo = " + unidadeEnsino.intValue() + " ORDER BY razaoSocial";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Método responsavel por fazer uma seleção completa da Entidade Unidade Ensino e mais algumas outras entidades que possuem relacionamento com a
	 * mesma. É uma consulta que busca completa e padrão.
	 * 
	 * @return List Contendo vários objetos da classe
	 * @author Carlos
	 */
	private StringBuffer getSQLPadraoConsultaCompleta() {
		StringBuffer str = new StringBuffer();
		str.append(" SELECT unidadeEnsino.*, ");
		str.append(" cidade.nome AS \"cidade.nome\", cidade.codigoibge as \"cidade.codigoIBGE\", cidade.codigodistrito as \"cidade.codigoDistrito\", cidade.codigoinep as \"cidade.codigoInep\", ");
		str.append(" pessoa.nome AS \"diretorGeral.nome\", p2.nome AS \"responsavelCobrancaUnidade.nome\" , p3.nome AS \"pessoaResponsavelNotificacaoAlteracaoCronogramaAula.nome\" ,  p3.codigo AS \"pessoaResponsavelNotificacaoAlteracaoCronogramaAula.codigo\" , curso.nome AS \"curso.nome\", curso.codigo as \"curso.codigo\", curso.niveleducacional as \"curso.niveleducacional\", planoFinanceiroCurso.codigo AS \"planoFinanceiroCurso.codigo\", ");
		str.append(" unidadeEnsinoCurso.situacaoCurso AS \"unidadeEnsinoCurso.situacaoCurso\", unidadeEnsinoCurso.mantenedora AS \"unidadeEnsinoCurso.mantenedora\", unidadeEnsinoCurso.mantida AS \"unidadeEnsinoCurso.mantida\", unidadeEnsinoCurso.codigo AS \"unidadeEnsinoCurso.codigo\", unidadeEnsinoCurso.nrVagasPeriodoLetivo AS \"unidadeEnsinoCurso.nrVagasPeriodoLetivo\", unidadeEnsinoCurso.valorMensalidade AS \"unidadeEnsinoCurso.valorMensalidade\", unidadeEnsinoCurso.codigoItemListaServico AS \"unidadeEnsinoCurso.codigoItemListaServico\", unidadeEnsinoCurso.codigoCursoUnidadeEnsinoGinfes AS \"unidadeEnsinoCurso.codigoCursoUnidadeEnsinoGinfes\",");
		str.append(" turno.nome as \"turno.nome\", turno.codigo as \"turno.codigo\", unidadeEnsinoCurso.coordenadorTCC as \"unidadeEnsinoCurso.coordenadorTCC\", ptcc.nome as \"unidadeEnsinoCurso.nomeCoordenadorTCC\", unidadeEnsinoCurso.codigoInep as \"unidadeEnsinoCurso.codigoInep\", unidadeEnsino.coordenadorTCC, p2tcc.nome as \"nomeCoordenadorTCC\", ptcc.codigo as \"codigoPessoaCoordenadorTCC\", ");
		str.append(" estado.nome AS \"estado.nome\", estado.codigo AS \"estado.codigo\" , estado.codigoibge as \"estado.codigoibge\", estado.sigla AS \"estado.sigla\", estado.codigoinep as \"estado.codigoInep\", ");

		str.append(" unidadeensino.contaCorrentePadraoMatricula AS \"contaCorrentePadraoMatricula.codigo\", ");
		str.append(" unidadeensino.contaCorrentePadraoMensalidade AS \"contaCorrentePadraoMensalidade.codigo\", ");
		str.append(" unidadeensino.contaCorrentePadraoBiblioteca AS \"contaCorrentePadraoBiblioteca.codigo\", ");
		str.append(" unidadeensino.contaCorrentePadraoDevolucaoCheque AS \"contaCorrentePadraoDevolucaoCheque.codigo\", ");
		str.append(" unidadeensino.contaCorrentePadraoMaterialDidatico AS \"contaCorrentePadraoMaterialDidatico.codigo\", ");
		str.append(" unidadeensino.contaCorrentePadraoNegociacao AS \"contaCorrentePadraoNegociacao.codigo\", ");
		str.append(" unidadeensino.contaCorrentePadraoProcessoSeletivo AS \"contaCorrentePadraoProcessoSeletivo.codigo\", ");
		str.append(" unidadeensino.dataCredenciamento AS \"unidadeensino.dataCredenciamento\", ");
		str.append(" unidadeensino.veiculoPublicacaoCredenciamento AS \"unidadeensino.veiculoPublicacaoCredenciamento\", ");
		str.append(" unidadeensino.secaoPublicacaoCredenciamento AS \"unidadeensino.secaoPublicacaoCredenciamento\", ");
		str.append(" unidadeensino.paginaPublicacaoCredenciamento AS \"unidadeensino.paginaPublicacaoCredenciamento\", ");
		str.append(" unidadeensino.numeroDOUCredenciamento AS \"unidadeensino.numeroDOUCredenciamento\", ");
		str.append(" unidadeensino.tipoAutorizacaoEnum AS \"unidadeensino.tipoAutorizacaoEnum\", ");
		str.append(" unidadeensino.informarDadosRegistradora AS \"unidadeensino.informarDadosRegistradora\", unidadeensino.utilizarEnderecoUnidadeEnsinoRegistradora AS \"unidadeensino.utilizarEnderecoUnidadeEnsinoRegistradora\", unidadeensino.cepRegistradora AS \"unidadeensino.cepRegistradora\", unidadeensino.cidadeRegistradora AS \"unidadeensino.cidadeRegistradora\", unidadeensino.complementoRegistradora AS \"unidadeensino.complementoRegistradora\", unidadeensino.bairroRegistradora AS \"unidadeensino.bairroRegistradora\", unidadeensino.enderecoRegistradora AS \"unidadeensino.enderecoRegistradora\", unidadeensino.numeroRegistradora AS \"unidadeensino.numeroRegistradora\", cidadeRegistradora.nome AS \"cidadeRegistradora.nome\", ");
		str.append(" unidadeensino.utilizarCredenciamentoUnidadeEnsino AS \"unidadeensino.utilizarCredenciamentoUnidadeEnsino\", unidadeensino.numeroCredenciamentoRegistradora AS \"unidadeensino.numeroCredenciamentoRegistradora\", unidadeensino.dataCredenciamentoRegistradora AS \"unidadeensino.dataCredenciamentoRegistradora\", unidadeensino.dataPublicacaoDORegistradora AS \"unidadeensino.dataPublicacaoDORegistradora\", unidadeensino.veiculoPublicacaoCredenciamentoRegistradora AS \"unidadeensino.veiculoPublicacaoCredenciamentoRegistradora\", unidadeensino.secaoPublicacaoCredenciamentoRegistradora AS \"unidadeensino.secaoPublicacaoCredenciamentoRegistradora\", unidadeensino.paginaPublicacaoCredenciamentoRegistradora AS \"unidadeensino.paginaPublicacaoCredenciamentoRegistradora\", unidadeensino.numeroPublicacaoCredenciamentoRegistradora AS \"unidadeensino.numeroPublicacaoCredenciamentoRegistradora\", ");
		str.append(" unidadeensino.utilizarMantenedoraUnidadeEnsino AS \"unidadeensino.utilizarMantenedoraUnidadeEnsino\", unidadeensino.mantenedoraRegistradora AS \"unidadeensino.mantenedoraRegistradora\", unidadeensino.cnpjMantenedoraRegistradora AS \"unidadeensino.cnpjMantenedoraRegistradora\", unidadeensino.cepMantenedoraRegistradora AS \"unidadeensino.cepMantenedoraRegistradora\", unidadeensino.enderecoMantenedoraRegistradora AS \"unidadeensino.enderecoMantenedoraRegistradora\", unidadeensino.numeroMantenedoraRegistradora AS \"unidadeensino.numeroMantenedoraRegistradora\", unidadeensino.cidadeMantenedoraRegistradora AS \"unidadeensino.cidadeMantenedoraRegistradora\", cidadeMantenedoraRegistradora.nome AS \"cidadeMantenedoraRegistradora.nome\", unidadeensino.complementoMantenedoraRegistradora AS \"unidadeensino.complementoMantenedoraRegistradora\", unidadeensino.bairroMantenedoraRegistradora AS \"unidadeensino.bairroMantenedoraRegistradora\", ");
		str.append(" unidadeensino.utilizarEnderecoUnidadeEnsinoMantenedora AS \"unidadeensino.utilizarEnderecoUnidadeEnsinoMantenedora\", unidadeensino.cepMantenedora AS \"unidadeensino.cepMantenedora\", unidadeensino.enderecoMantenedora AS \"unidadeensino.enderecoMantenedora\", unidadeensino.numeroMantenedora AS \"unidadeensino.numeroMantenedora\", unidadeensino.cidadeMantenedora AS \"unidadeensino.cidadeMantenedora\", cidadeMantenedora.nome AS \"cidadeMantenedora.nome\", unidadeensino.complementoMantenedora AS \"unidadeensino.complementoMantenedora\", unidadeensino.bairroMantenedora AS \"unidadeensino.bairroMantenedora\", ");
		str.append(" unidadeensino.numeroRecredenciamento AS \"unidadeensino.numeroRecredenciamento\", unidadeensino.dataRecredenciamento AS \"unidadeensino.dataRecredenciamento\", unidadeensino.dataPublicacaoRecredenciamento AS \"unidadeensino.dataPublicacaoRecredenciamento\", unidadeensino.veiculoPublicacaoRecredenciamento AS \"unidadeensino.veiculoPublicacaoRecredenciamento\", unidadeensino.secaoPublicacaoRecredenciamento AS \"unidadeensino.secaoPublicacaoRecredenciamento\", unidadeensino.paginaPublicacaoRecredenciamento AS \"unidadeensino.paginaPublicacaoRecredenciamento\", unidadeensino.numeroDOURecredenciamento AS \"unidadeensino.numeroDOURecredenciamento\", unidadeensino.tipoAutorizacaoRecredenciamento AS \"unidadeensino.tipoAutorizacaoRecredenciamento\", ");
		str.append(" unidadeensino.numeroRenovacaoRecredenciamento AS \"unidadeensino.numeroRenovacaoRecredenciamento\", unidadeensino.dataRenovacaoRecredenciamento AS \"unidadeensino.dataRenovacaoRecredenciamento\", unidadeensino.dataPublicacaoRenovacaoRecredenciamento AS \"unidadeensino.dataPublicacaoRenovacaoRecredenciamento\", unidadeensino.veiculoPublicacaoRenovacaoRecredenciamento AS \"unidadeensino.veiculoPublicacaoRenovacaoRecredenciamento\", unidadeensino.secaoPublicacaoRenovacaoRecredenciamento AS \"unidadeensino.secaoPublicacaoRenovacaoRecredenciamento\", unidadeensino.paginaPublicacaoRenovacaoRecredenciamento AS \"unidadeensino.paginaPublicacaoRenovacaoRecredenciamento\", unidadeensino.numeroDOURenovacaoRecredenciamento AS \"unidadeensino.numeroDOURenovacaoRecredenciamento\", unidadeensino.tipoAutorizacaoRenovacaoRecredenciamento AS \"unidadeensino.tipoAutorizacaoRenovacaoRecredenciamento\", cidadeMantenedora.codigoIBGE AS \"cidadeMantenedora.codigoIBGE\", estadoMantenedora.sigla AS \"estadoMantenedora.sigla\", ");
		str.append(" unidadeensino.numeroCredenciamentoEAD AS \"unidadeensino.numeroCredenciamentoEAD\", unidadeensino.credenciamentoEAD AS \"unidadeensino.credenciamentoEAD\", unidadeensino.dataCredenciamentoEAD AS \"unidadeensino.dataCredenciamentoEAD\", unidadeensino.dataPublicacaoDOEAD AS \"unidadeensino.dataPublicacaoDOEAD\", unidadeensino.credenciamentoPortariaEAD AS \"unidadeensino.credenciamentoPortariaEAD\", unidadeensino.veiculoPublicacaoCredenciamentoEAD AS \"unidadeensino.veiculoPublicacaoCredenciamentoEAD\", unidadeensino.secaoPublicacaoCredenciamentoEAD AS \"unidadeensino.secaoPublicacaoCredenciamentoEAD\", unidadeensino.paginaPublicacaoCredenciamentoEAD AS \"unidadeensino.paginaPublicacaoCredenciamentoEAD\", unidadeensino.numeroDOUCredenciamentoEAD AS \"unidadeensino.numeroDOUCredenciamentoEAD\", unidadeensino.tipoAutorizacaoEAD AS \"unidadeensino.tipoAutorizacaoEAD\", unidadeensino.numeroRecredenciamentoEAD AS \"unidadeensino.numeroRecredenciamentoEAD\", unidadeensino.dataRecredenciamentoEAD AS \"unidadeensino.dataRecredenciamentoEAD\", unidadeensino.dataPublicacaoRecredenciamentoEAD AS \"unidadeensino.dataPublicacaoRecredenciamentoEAD\", unidadeensino.veiculoPublicacaoRecredenciamentoEAD AS \"unidadeensino.veiculoPublicacaoRecredenciamentoEAD\", unidadeensino.secaoPublicacaoRecredenciamentoEAD AS \"unidadeensino.secaoPublicacaoRecredenciamentoEAD\", unidadeensino.paginaPublicacaoRecredenciamentoEAD AS \"unidadeensino.paginaPublicacaoRecredenciamentoEAD\", unidadeensino.numeroDOURecredenciamentoEAD AS \"unidadeensino.numeroDOURecredenciamentoEAD\", unidadeensino.tipoAutorizacaoRecredenciamentoEAD AS \"unidadeensino.tipoAutorizacaoRecredenciamentoEAD\", unidadeensino.numeroRenovacaoRecredenciamentoEAD AS \"unidadeensino.numeroRenovacaoRecredenciamentoEAD\", unidadeensino.dataRenovacaoRecredenciamentoEAD AS \"unidadeensino.dataRenovacaoRecredenciamentoEAD\", unidadeensino.dataPublicacaoRenovacaoRecredenciamentoEAD AS \"unidadeensino.dataPublicacaoRenovacaoRecredenciamentoEAD\", unidadeensino.veiculoPublicacaoRenovacaoRecredenciamentoEAD AS \"unidadeensino.veiculoPublicacaoRenovacaoRecredenciamentoEAD\", unidadeensino.secaoPublicacaoRenovacaoRecredenciamentoEAD AS \"unidadeensino.secaoPublicacaoRenovacaoRecredenciamentoEAD\", unidadeensino.paginaPublicacaoRenovacaoRecredenciamentoEAD AS \"unidadeensino.paginaPublicacaoRenovacaoRecredenciamentoEAD\", unidadeensino.numeroDOURenovacaoRecredenciamentoEAD AS \"unidadeensino.numeroDOURenovacaoRecredenciamentoEAD\", unidadeensino.tipoAutorizacaoRenovacaoRecredenciamentoEAD AS \"unidadeensino.tipoAutorizacaoRenovacaoRecredenciamentoEAD\", ");
		str.append(" unidadeensino.credenciamentoEmTramitacao AS \"unidadeensino.credenciamentoEmTramitacao\", unidadeensino.numeroProcessoCredenciamento AS \"unidadeensino.numeroProcessoCredenciamento\", unidadeensino.tipoProcessoCredenciamento AS \"unidadeensino.tipoProcessoCredenciamento\", unidadeensino.dataCadastroCredenciamento AS \"unidadeensino.dataCadastroCredenciamento\", unidadeensino.dataProtocoloCredenciamento AS \"unidadeensino.dataProtocoloCredenciamento\", unidadeensino.recredenciamentoEmTramitacao AS \"unidadeensino.recredenciamentoEmTramitacao\", unidadeensino.numeroProcessoRecredenciamento AS \"unidadeensino.numeroProcessoRecredenciamento\", unidadeensino.tipoProcessoRecredenciamento AS \"unidadeensino.tipoProcessoRecredenciamento\", unidadeensino.dataCadastroRecredenciamento AS \"unidadeensino.dataCadastroRecredenciamento\", unidadeensino.dataProtocoloRecredenciamento AS \"unidadeensino.dataProtocoloRecredenciamento\", unidadeensino.renovacaoRecredenciamentoEmTramitacao AS \"unidadeensino.renovacaoRecredenciamentoEmTramitacao\", unidadeensino.numeroProcessoRenovacaoRecredenciamento AS \"unidadeensino.numeroProcessoRenovacaoRecredenciamento\", unidadeensino.tipoProcessoRenovacaoRecredenciamento AS \"unidadeensino.tipoProcessoRenovacaoRecredenciamento\", unidadeensino.dataCadastroRenovacaoRecredenciamento AS \"unidadeensino.dataCadastroRenovacaoRecredenciamento\", unidadeensino.dataProtocoloRenovacaoRecredenciamento AS \"unidadeensino.dataProtocoloRenovacaoRecredenciamento\", ");
		str.append(" unidadeensino.credenciamentoEadEmTramitacao AS \"unidadeensino.credenciamentoEadEmTramitacao\", unidadeensino.numeroProcessoCredenciamentoEad AS \"unidadeensino.numeroProcessoCredenciamentoEad\", unidadeensino.tipoProcessoCredenciamentoEad AS \"unidadeensino.tipoProcessoCredenciamentoEad\", unidadeensino.dataCadastroCredenciamentoEad AS \"unidadeensino.dataCadastroCredenciamentoEad\", unidadeensino.dataProtocoloCredenciamentoEad AS \"unidadeensino.dataProtocoloCredenciamentoEad\", unidadeensino.recredenciamentoEmTramitacaoEad AS \"unidadeensino.recredenciamentoEmTramitacaoEad\", unidadeensino.numeroProcessoRecredenciamentoEad AS \"unidadeensino.numeroProcessoRecredenciamentoEad\", unidadeensino.tipoProcessoRecredenciamentoEad AS \"unidadeensino.tipoProcessoRecredenciamentoEad\", unidadeensino.dataCadastroRecredenciamentoEad AS \"unidadeensino.dataCadastroRecredenciamentoEad\", unidadeensino.dataProtocoloRecredenciamentoEad AS \"unidadeensino.dataProtocoloRecredenciamentoEad\", unidadeensino.renovacaoRecredenciamentoEmTramitacaoEad AS \"unidadeensino.renovacaoRecredenciamentoEmTramitacaoEad\", unidadeensino.numeroProcessoRenovacaoRecredenciamentoEad AS \"unidadeensino.numeroProcessoRenovacaoRecredenciamentoEad\", unidadeensino.tipoProcessoRenovacaoRecredenciamentoEad AS \"unidadeensino.tipoProcessoRenovacaoRecredenciamentoEad\", unidadeensino.dataCadastroRenovacaoRecredenciamentoEad AS \"unidadeensino.dataCadastroRenovacaoRecredenciamentoEad\", unidadeensino.dataProtocoloRenovacaoRecredenciamentoEad AS \"unidadeensino.dataProtocoloRenovacaoRecredenciamentoEad\", ");
		str.append(" unidadeensino.credenciamentoRegistradoraEmTramitacao AS \"unidadeensino.credenciamentoRegistradoraEmTramitacao\", unidadeensino.numeroProcessoCredenciamentoRegistradora AS \"unidadeensino.numeroProcessoCredenciamentoRegistradora\", unidadeensino.tipoProcessoCredenciamentoRegistradora AS \"unidadeensino.tipoProcessoCredenciamentoRegistradora\", unidadeensino.dataCadastroCredenciamentoRegistradora AS \"unidadeensino.dataCadastroCredenciamentoRegistradora\", unidadeensino.dataProtocoloCredenciamentoRegistradora AS \"unidadeensino.dataProtocoloCredenciamentoRegistradora\", unidadeensino.numeroCredenciamento AS \"unidadeensino.numeroCredenciamento\",unidadeensino.numeroVagaOfertada AS \"unidadeensino.numeroVagaOfertada\", ");
		str.append(" estadoMantenedoraRegistradora.sigla AS \"estadoMantenedoraRegistradora.sigla\", cidadeMantenedoraRegistradora.codigoIBGE AS \"cidadeMantenedoraRegistradora.codigoIBGE\", estadoRegistradora.sigla AS \"estadoRegistradora.sigla\", cidadeRegistradora.codigoIBGE AS \"cidadeRegistradora.codigoIBGE\", unidadeensino.tipoAutorizacaoCredenciamentoRegistradora AS \"unidadeensino.tipoAutorizacaoCredenciamentoRegistradora\", ");
		str.append(" pope.codigo AS \"orientadorPadraoEstagio.codigo\", ");
		str.append(" pope.nome AS \"orientadorPadraoEstagio.nome\", ");
		str.append(" pope.cpf AS \"orientadorPadraoEstagio.cpf\", ");
		str.append(" pope.telefoneRes as \"orientadorPadraoEstagio.telefoneRes\", pope.telefoneComer as \"orientadorPadraoEstagio.telefoneComer\", pope.celular as \"orientadorPadraoEstagio.celular\", ");
		str.append(" pesOperadorResponsavel.codigo AS \"operadorResponsavel.codigo\", ");
		str.append(" pesOperadorResponsavel.nome AS \"operadorResponsavel.nome\", ");
	
		
		str.append(" unidadeensino.apresentarHomePreInscricao, unidadeensino.leicriacao1, unidadeensino.leicriacao2, pdg.nome as diretorGeralNome, ");
		str.append(" configuracaocontabil.codigo AS \"configuracaocontabil.codigo\", ");
		str.append(" centroResultado.codigo AS \"centroResultado.codigo\", centroResultado.identificadorCentroResultado AS \"centroResultado.identificadorCentroResultado\", centroResultado.descricao AS \"centroResultado.descricao\", ");
		str.append(" centroResultadoRequerimento.codigo AS \"centroResultadoRequerimento.codigo\", centroResultadoRequerimento.identificadorCentroResultado AS \"centroResultadoRequerimento.identificadorCentroResultado\", centroResultadoRequerimento.descricao AS \"centroResultadoRequerimento.descricao\", ");
		str.append(" configuracaoGED.codigo AS \"configuracaoGED.codigo\", configuracaoGED.nome AS \"configuracaoGED.nome\", configuracaoDiplomaDigital.codigo AS \"configuracaoDiplomaDigital.codigo\", configuracaoDiplomaDigital.descricao AS \"configuracaoDiplomaDigital.descricao\", configuracaoDiplomaDigital.padrao AS \"configuracaoDiplomaDigital.padrao\" ");
		str.append(" FROM unidadeEnsino ");
		str.append(" LEFT JOIN cidade on cidade.codigo = unidadeEnsino.cidade ");
		str.append(" LEFT JOIN estado on estado.codigo = cidade.estado ");
		str.append(" LEFT JOIN funcionario on funcionario.codigo = unidadeEnsino.diretorGeral ");
		str.append(" LEFT JOIN pessoa on pessoa.codigo = funcionario.pessoa ");
		str.append(" LEFT JOIN pessoa p2 on p2.codigo = unidadeEnsino.responsavelCobrancaUnidade ");
		str.append(" LEFT JOIN funcionario frn on frn.codigo = unidadeEnsino.responsavelNotificacaoAlteracaoCronogramaAula ");
		str.append(" LEFT JOIN pessoa p3 on p3.codigo = frn.pessoa ");
		str.append(" LEFT JOIN unidadeEnsinoCurso on unidadeEnsinoCurso.unidadeEnsino = unidadeEnsino.codigo ");
		str.append(" LEFT JOIN funcionario ftcc on ftcc.codigo = unidadeEnsinoCurso.coordenadorTCC ");
		str.append(" LEFT JOIN pessoa ptcc on ptcc.codigo = ftcc.pessoa ");
		str.append(" LEFT JOIN funcionario f2tcc on f2tcc.codigo = unidadeEnsino.coordenadorTCC ");
		str.append(" LEFT JOIN pessoa p2tcc on p2tcc.codigo = f2tcc.pessoa ");
		str.append(" LEFT JOIN curso on curso.codigo = unidadeEnsinoCurso.curso ");
		str.append(" LEFT JOIN turno on turno.codigo = unidadeEnsinoCurso.turno ");
		str.append(" LEFT JOIN planoFinanceiroCurso on planoFinanceiroCurso.codigo = unidadeEnsinoCurso.planoFinanceiroCurso ");
		str.append(" LEFT JOIN configuracaocontabil on configuracaocontabil.codigo = unidadeEnsino.configuracaocontabil ");
		str.append(" LEFT JOIN centroResultado on centroResultado.codigo = unidadeEnsino.centroResultado ");
		str.append(" LEFT JOIN centroResultado as centroResultadoRequerimento on centroResultadoRequerimento.codigo = unidadeEnsino.centroResultadoRequerimento ");
		str.append(" LEFT JOIN funcionario fdg on fdg.codigo = unidadeEnsino.diretorGeral ");
		str.append(" LEFT JOIN pessoa pdg on pdg.codigo = fdg.pessoa ");
		str.append(" LEFT JOIN funcionario fope on fope.codigo = unidadeEnsino.orientadorPadraoEstagio ");
		str.append(" LEFT JOIN pessoa pope on pope.codigo = fope.pessoa ");
		str.append(" LEFT JOIN configuracaoGED on configuracaoGED.codigo = unidadeEnsino.configuracaoGED  ");
		str.append(" LEFT JOIN funcionario funOperadorResponsavel on funOperadorResponsavel.codigo = unidadeEnsino.operadorResponsavel ");
		str.append(" LEFT JOIN pessoa pesOperadorResponsavel on pesOperadorResponsavel.codigo = funOperadorResponsavel.pessoa ");
		str.append(" LEFT JOIN configuracaoDiplomaDigital on configuracaoDiplomaDigital.codigo = unidadeEnsino.configuracaoDiplomaDigital ");
		str.append(" LEFT JOIN cidade cidadeRegistradora on cidadeRegistradora.codigo = unidadeEnsino.cidadeRegistradora ");
		str.append(" LEFT JOIN cidade cidadeMantenedoraRegistradora on cidadeMantenedoraRegistradora.codigo = unidadeEnsino.cidadeMantenedoraRegistradora ");
		str.append(" LEFT JOIN cidade cidadeMantenedora on cidadeMantenedora.codigo = unidadeEnsino.cidadeMantenedora ");
		str.append(" LEFT JOIN estado estadoMantenedora on estadoMantenedora.codigo = cidadeMantenedora.estado ");
		str.append(" LEFT JOIN estado estadoMantenedoraRegistradora on estadoMantenedoraRegistradora.codigo = cidadeMantenedoraRegistradora.estado ");
		str.append(" LEFT JOIN estado estadoRegistradora on estadoRegistradora.codigo = cidadeRegistradora.estado ");
		return str;
	}

	/**
	 * Responsável por realizar uma consulta de <code>UnidadeEnsino</code> através do valor do atributo <code>String nome</code>. Retorna os objetos,
	 * com início do valor do atributo idêntico ao parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>UnidadeEnsinoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 * 
	 */
	private StringBuffer getSQLPadraoConsultaBasica() {
		StringBuffer str = new StringBuffer();
		str.append("select unidadeEnsino.nome, unidadeEnsino.abreviatura, unidadeEnsino.nomeExpedicaoDiploma, unidadeEnsino.mantenedora, unidadeEnsino.credenciamento, unidadeEnsino.credenciamentoportaria, unidadeEnsino.cep, unidadeEnsino.endereco, unidadeEnsino.caixaPostal, unidadeEnsino.numero, unidadeEnsino.complemento, unidadeEnsino.email, unidadeEnsino.site, unidadeEnsino.setor, agrupartelefone(unidadeensino.telcomercial1, unidadeensino.telcomercial2, unidadeensino.telcomercial3, '') as telefones, unidadeensino.telcomercial1, unidadeEnsino.razaoSocial, unidadeEnsino.codigo, unidadeEnsino.caminhoBaseLogo, unidadeEnsino.nomeArquivoLogo,  unidadeEnsino.caminhoBaseLogoIndex, unidadeEnsino.nomeArquivoLogoIndex, unidadeEnsino.caminhoBaseLogoRelatorio, unidadeEnsino.nomeArquivoLogoRelatorio, cidade.nome as \"cidade.nome\", cidade.codigo as \"cidade.codigo\", cidade.codigoibge as \"cidade.codigoibge\", unidadeEnsino.cnpj, unidadeEnsino.inscEstadual, unidadeEnsino.permitirvisualizacaologin, unidadeEnsino.matriz, unidadeEnsino.coordenadorTCC, ptcc.nome as \"nomeCoordenadorTCC\", ");
		str.append(" unidadeEnsino.caminhoBaseLogoEmailCima, unidadeEnsino.nomeArquivoLogoEmailCima, unidadeEnsino.caminhoBaseLogoEmailBaixo, unidadeEnsino.nomeArquivoLogoEmailBaixo, ");
		str.append(" unidadeEnsino.caminhobaselogoaplicativo, unidadeEnsino.nomeArquivoLogoAplicativo, ");
		str.append(" unidadeEnsino.caminhoBaseLogoMunicipio, unidadeEnsino.nomeArquivoLogoMunicipio, unidadeensino.codigoIntegracaoContabil, unidadeensino.usarConfiguracaoPadrao, ");
		str.append(" agruparCamposEndereco(unidadeensino.endereco, unidadeensino.numero, unidadeensino.setor, unidadeensino.cep) as endereco_completo, unidadeensino.configuracaonotafiscal, unidadeensino.codigoTributacaoMunicipio, unidadeensino.inscMunicipal, unidadeEnsino.datapublicacaodo, ");
		str.append(" configuracoes.codigo AS \"configuracoes.codigo\", configuracoes.nome AS \"configuracoes.nome\", pdg.nome as diretorGeralNome,  ");
		str.append(" pesOperadorResponsavel.codigo AS \"operadorResponsavel.codigo\", ");
		str.append(" pesOperadorResponsavel.nome AS \"operadorResponsavel.nome\", unidadeEnsino.operadorResponsavel ,   ");
		
		str.append("pess_orientadorPadraoEstagio.codigo as \"pess_orientadorPadraoEstagio.codigo\", pess_orientadorPadraoEstagio.nome as \"pess_orientadorPadraoEstagio.nome\", pess_orientadorPadraoEstagio.cpf as \"pess_orientadorPadraoEstagio.cpf\", ");
		str.append("pess_orientadorPadraoEstagio.telefoneRes as \"pess_orientadorPadraoEstagio.telefoneRes\", pess_orientadorPadraoEstagio.telefoneComer as \"pess_orientadorPadraoEstagio.telefoneComer\", pess_orientadorPadraoEstagio.celular as \"pess_orientadorPadraoEstagio.celular\", ");

		str.append(" unidadeensino.observacao AS \"unidadeensino.observacao\", ");
		str.append(" unidadeensino.contaCorrentePadraoMatricula AS \"contaCorrentePadraoMatricula.codigo\", ");
		str.append(" unidadeensino.contaCorrentePadraoMensalidade AS \"contaCorrentePadraoMensalidade.codigo\", ");
		str.append(" unidadeensino.contaCorrentePadraoBiblioteca AS \"contaCorrentePadraoBiblioteca.codigo\", ");
		str.append(" unidadeensino.contaCorrentePadraoDevolucaoCheque AS \"contaCorrentePadraoDevolucaoCheque.codigo\", ");
		str.append(" unidadeensino.contaCorrentePadraoMaterialDidatico AS \"contaCorrentePadraoMaterialDidatico.codigo\", ");
		str.append(" unidadeensino.contaCorrentePadraoNegociacao AS \"contaCorrentePadraoNegociacao.codigo\", ");
		str.append(" unidadeensino.contaCorrentePadraoProcessoSeletivo AS \"contaCorrentePadraoProcessoSeletivo.codigo\", ");
		str.append(" unidadeEnsino.codigoIES, unidadeEnsino.codigoIESMantenedora, ");
		str.append(" unidadeEnsino.cnpjmantenedora, unidadeEnsino.unidadecertificadora, ");
		str.append(" unidadeEnsino.cnpjunidadecertificadora, unidadeEnsino.codigoiesunidadecertificadora, unidadeEnsino.configuracaomobile, ");
		str.append(" estado.codigo as \"estado.codigo\", estado.sigla as \"estado.sigla\", estado.nome as \"estado.nome\", estado.codigoibge as \"estado.codigoibge\", paiz.codigo as \"paiz.codigo\", paiz.nome as \"paiz.nome\", apresentarHomePreInscricao, unidadeensino.leicriacao1, unidadeensino.leicriacao2, unidadeensino.nomearquivologopaginainicial, unidadeensino.caminhobaselogopaginainicial, unidadeensino.informacoesAdicionaisEndereco, unidadeEnsino.numeroCredenciamentoEad, ");
		str.append(" unidadeEnsino.credenciamentoEad, unidadeEnsino.credenciamentoPortariaEad ");
		str.append(" FROM unidadeEnsino ");
		str.append(" left join cidade on cidade.codigo = unidadeEnsino.cidade ");
		str.append(" left join estado on cidade.estado = estado.codigo ");
		str.append(" left join paiz on estado.paiz = paiz.codigo ");
		//str.append(" left join contaCorrente on contaCorrente.codigo = unidadeEnsino.contaCorrentePadrao ");
		str.append(" left join configuracoes on configuracoes.codigo = unidadeEnsino.configuracoes ");
		str.append(" left join funcionario ftcc on ftcc.codigo = unidadeEnsino.coordenadorTCC ");
		str.append(" left join pessoa ptcc on ptcc.codigo = ftcc.pessoa ");
		str.append(" left join funcionario fdg on fdg.codigo = unidadeEnsino.diretorGeral ");
		str.append(" left join pessoa pdg on pdg.codigo = fdg.pessoa ");		
		str.append(" left join funcionario funOperadorResponsavel on funOperadorResponsavel.codigo = unidadeEnsino.operadorResponsavel ");
		str.append(" left join pessoa pesOperadorResponsavel on pesOperadorResponsavel.codigo = funOperadorResponsavel.pessoa ");
		str.append(" left join funcionario func_orientadorPadraoEstagio ON (unidadeEnsino.orientadorPadraoEstagio = func_orientadorPadraoEstagio.codigo) ");
		str.append(" left join pessoa pess_orientadorPadraoEstagio ON (func_orientadorPadraoEstagio.pessoa = pess_orientadorPadraoEstagio.codigo) ");
		
		return str;
	}

	/**
	 * Método responsavel por invocar uma consulta rápida(Básica) e padrão que buscará apenas campos necessários para visualização do cliente na tela.
	 * Está consulta é considerada Padrão pelo motivo de todos os métodos chamar a mesma consulta(getSQLPadraoConsultaBasica) e apenas adicionar as
	 * cláusulas de condições e ordenação.
	 * 
	 * @author Carlos
	 */
	public List<UnidadeEnsinoVO> consultaRapidaPorNome(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE lower(sem_acentos(unidadeEnsino.nome)) ilike(sem_acentos('");
		sqlStr.append(valorConsulta.toLowerCase());
		sqlStr.append("%'))");
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sqlStr.append(" AND unidadeEnsino.codigo = ");
			sqlStr.append(unidadeEnsino.intValue());
		}
		sqlStr.append(" ORDER BY unidadeEnsino.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado, nivelMontarDados);
	}
	
	/**
	 * Método responsavel por invocar uma consulta rápida(Básica) e padrão que buscará apenas campos necessários para visualização do cliente na tela.
	 * Está consulta é considerada Padrão pelo motivo de todos os métodos chamar a mesma consulta(getSQLPadraoConsultaBasica) e apenas adicionar as
	 * cláusulas de condições e ordenação.
	 * 
	 * @author Pedro
	 */
	public List<UnidadeEnsinoVO> consultaRapidaPorNomePorApresentarHomePreInscricao(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE lower(sem_acentos(unidadeEnsino.nome)) ilike(sem_acentos('");
		sqlStr.append(valorConsulta.toLowerCase());
		sqlStr.append("%'))");
		sqlStr.append(" AND unidadeensino.apresentarHomePreInscricao = true ");
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND unidadeEnsino.codigo = ");
			sqlStr.append(unidadeEnsino.intValue());
		}
		sqlStr.append(" ORDER BY unidadeEnsino.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado, nivelMontarDados);
	}
	
	
	

	/**
	 * Método responsavel por invocar uma consulta rápida(Básica) e padrão que buscará apenas campos necessários para visualização do cliente na tela.
	 * Está consulta é considerada Padrão pelo motivo de todos os métodos chamar a mesma consulta(getSQLPadraoConsultaBasica) e apenas adicionar as
	 * cláusulas de condições e ordenação.
	 * 
	 * @author Carlos
	 */
	public List<UnidadeEnsinoVO> consultaRapidaPorRazaoSocial(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE lower(unidadeEnsino.razaoSocial) like('");
		sqlStr.append(valorConsulta.toLowerCase());
		sqlStr.append("%')");
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND unidadeEnsino.codigo = ");
			sqlStr.append(unidadeEnsino.intValue());
		}
		sqlStr.append(" ORDER BY unidadeEnsino.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado, nivelMontarDados);
	}

	public UnidadeEnsinoVO consultaRapidaPorCodigo(Integer codUnidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);		
		UnidadeEnsinoVO obj = getAplicacaoControle().getUnidadeEnsinoVO(codUnidadeEnsino, usuario);
		if (obj == null ) {
			obj = new UnidadeEnsinoVO();
		}
		return obj;
	}
	
    @Override
	public UnidadeEnsinoVO consultaRapidaResponsavelCobrancaUnidadePorCodigo(Integer codMatriculaPeriodoTurmaDisciplina) throws Exception {
		StringBuffer str = new StringBuffer();
		str.append(" SELECT responsavelCobrancaUnidade, ");
		str.append(" p2.email AS \"responsavelCobrancaUnidade.email\", ");
		str.append(" p2.nome AS \"responsavelCobrancaUnidade.nome\" ");
		str.append(" FROM matriculaperiodoturmadisciplina  ");
		str.append(" inner join turma on turma.codigo = matriculaperiodoturmadisciplina.turma inner join unidadeensino on unidadeensino.codigo = turma.unidadeensino inner join pessoa p2 on p2.codigo = unidadeEnsino.responsavelCobrancaUnidade   ");
		str.append(" WHERE matriculaperiodoturmadisciplina.codigo = ").append(codMatriculaPeriodoTurmaDisciplina.intValue());
		str.append(" ORDER BY unidadeEnsino.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(str.toString());
		UnidadeEnsinoVO obj = new UnidadeEnsinoVO();
		if (tabelaResultado.next()) {
			obj.getResponsavelCobrancaUnidade().setCodigo(tabelaResultado.getInt("responsavelCobrancaUnidade"));
			obj.getResponsavelCobrancaUnidade().setNome(tabelaResultado.getString("responsavelCobrancaUnidade.nome"));
			obj.getResponsavelCobrancaUnidade().setEmail(tabelaResultado.getString("responsavelCobrancaUnidade.email"));
		}
		return obj;
	}
	
	
	
	
	public UnidadeEnsinoVO consultaRapidaResponsavelNotificacaoAlteracaoCronogramaAulaPorCodigoUnidadeEnsino(Integer unidadeEnsino) throws Exception {
		StringBuffer str = new StringBuffer();
		str.append(" SELECT responsavelNotificacaoAlteracaoCronogramaAula, ");
		str.append(" p2.email AS \"responsavelNotificacaoAlteracaoCronogramaAula.email\", ");
		str.append(" p2.email2 AS \"responsavelNotificacaoAlteracaoCronogramaAula.email2\", ");
		str.append(" p2.codigo AS \"responsavelNotificacaoAlteracaoCronogramaAula.codigo\", ");
		str.append(" p2.nome AS \"responsavelNotificacaoAlteracaoCronogramaAula.nome\" ");
		str.append(" FROM unidadeensino  ");
		str.append(" inner join funcionario f2 on f2.codigo = unidadeEnsino.responsavelNotificacaoAlteracaoCronogramaAula   ");
		str.append(" inner join pessoa p2 on p2.codigo = f2.pessoa   ");
		str.append(" WHERE unidadeensino.codigo = ").append(unidadeEnsino.intValue());		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(str.toString());
		UnidadeEnsinoVO obj = new UnidadeEnsinoVO();
		if (tabelaResultado.next()) {
			obj.getResponsavelNotificacaoAlteracaoCronogramaAula().setCodigo(tabelaResultado.getInt("responsavelNotificacaoAlteracaoCronogramaAula"));
			obj.getResponsavelNotificacaoAlteracaoCronogramaAula().getPessoa().setCodigo(tabelaResultado.getInt("responsavelNotificacaoAlteracaoCronogramaAula.codigo"));
			obj.getResponsavelNotificacaoAlteracaoCronogramaAula().getPessoa().setNome(tabelaResultado.getString("responsavelNotificacaoAlteracaoCronogramaAula.nome"));
			obj.getResponsavelNotificacaoAlteracaoCronogramaAula().getPessoa().setEmail(tabelaResultado.getString("responsavelNotificacaoAlteracaoCronogramaAula.email"));
			obj.getResponsavelNotificacaoAlteracaoCronogramaAula().getPessoa().setEmail2(tabelaResultado.getString("responsavelNotificacaoAlteracaoCronogramaAula.email2"));
		}
		return obj;
	}

	/**
	 * Método responsavel por invocar uma consulta rápida(Básica) e padrão que buscará apenas campos necessários para visualização do cliente na tela.
	 * Está consulta é considerada Padrão pelo motivo de todos os métodos chamar a mesma consulta(getSQLPadraoConsultaBasica) e apenas adicionar as
	 * cláusulas de condições e ordenação.
	 * 
	 * @author Carlos
	 */
	public List<UnidadeEnsinoVO> consultaRapidaPorNomeCidade(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE lower(sem_acentos(cidade.nome)) ilike(sem_acentos('");
		sqlStr.append(valorConsulta.toLowerCase());
		sqlStr.append("%'))");
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND unidadeEnsino.codigo = ");
			sqlStr.append(unidadeEnsino.intValue());
		}
		sqlStr.append(" ORDER BY cidade.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado, nivelMontarDados);
	}

	/**
	 * Método responsavel por invocar uma consulta rápida(Básica) e padrão que buscará apenas campos necessários para visualização do cliente na tela.
	 * Está consulta é considerada Padrão pelo motivo de todos os métodos chamar a mesma consulta(getSQLPadraoConsultaBasica) e apenas adicionar as
	 * cláusulas de condições e ordenação.
	 * 
	 * @author Carlos
	 */
	public List<UnidadeEnsinoVO> consultaRapidaPorCnpj(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE lower(unidadeEnsino.cnpj) like('");
		sqlStr.append(valorConsulta.toLowerCase());
		sqlStr.append("%')");
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND unidadeEnsino.codigo = ");
			sqlStr.append(unidadeEnsino.intValue());
		}
		sqlStr.append(" ORDER BY unidadeEnsino.cnpj");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado, nivelMontarDados);
	}

	/**
	 * Método responsavel por invocar uma consulta rápida(Básica) e padrão que buscará apenas campos necessários para visualização do cliente na tela.
	 * Está consulta é considerada Padrão pelo motivo de todos os métodos chamar a mesma consulta(getSQLPadraoConsultaBasica) e apenas adicionar as
	 * cláusulas de condições e ordenação.
	 * 
	 * @author Carlos
	 */
	public List<UnidadeEnsinoVO> consultaRapidaPorInscEstatual(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE lower(unidadeEnsino.inscEstadual) like('");
		sqlStr.append(valorConsulta.toLowerCase());
		sqlStr.append("%')");
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND unidadeEnsino.codigo = ");
			sqlStr.append(unidadeEnsino.intValue());
		}
		sqlStr.append(" ORDER BY unidadeEnsino.inscEstadual");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado, nivelMontarDados);
	}

	/**
	 * Método responsavel por invocar uma consulta rápida(Básica) e padrão que buscará apenas campos necessários para visualização do cliente na tela.
	 * Está consulta é considerada Padrão pelo motivo de todos os métodos chamar a mesma consulta(getSQLPadraoConsultaBasica) e apenas adicionar as
	 * cláusulas de condições e ordenação.
	 * 
	 * @author Carlos
	 */
	public List<UnidadeEnsinoVO> consultaRapidaPorRg(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE lower(unidadeEnsino.rg) like('");
		sqlStr.append(valorConsulta.toLowerCase());
		sqlStr.append("%')");
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND unidadeEnsino.codigo = ");
			sqlStr.append(unidadeEnsino.intValue());
		}
		sqlStr.append(" ORDER BY unidadeEnsino.rg");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado, nivelMontarDados);
	}

	/**
	 * Método responsavel por invocar uma consulta rápida(Básica) e padrão que buscará apenas campos necessários para visualização do cliente na tela.
	 * Está consulta é considerada Padrão pelo motivo de todos os métodos chamar a mesma consulta(getSQLPadraoConsultaBasica) e apenas adicionar as
	 * cláusulas de condições e ordenação.
	 * 
	 * @author Carlos
	 */
	public List<UnidadeEnsinoVO> consultaRapidaPorCpf(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE lower(unidadeEnsino.cpf) like('");
		sqlStr.append(valorConsulta.toLowerCase());
		sqlStr.append("%')");
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND unidadeEnsino.codigo = ");
			sqlStr.append(unidadeEnsino.intValue());
		}
		sqlStr.append(" ORDER BY unidadeEnsino.cpf");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado, nivelMontarDados);
	}

	/**
	 * Responsável por realizar uma consulta de <code>UnidadeEnsino</code> através do valor do atributo <code>String nome</code>. Retorna osu objetos,
	 * com início do valor do atributo idêntico ao parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>UnidadeEnsinoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<UnidadeEnsinoVO> consultarPorNome(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM UnidadeEnsino WHERE lower(nome) like('" + "%" + valorConsulta.toLowerCase() + "%') ORDER BY nome";
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sqlStr = "SELECT * FROM UnidadeEnsino WHERE lower(nome) like('" + valorConsulta.toLowerCase() + "%') and codigo = " + unidadeEnsino + " ORDER BY codigo";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}
	
	@Override
	public List<UnidadeEnsinoVO> consultarPorConfiguracoes(Integer valorConsulta,  boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM UnidadeEnsino WHERE configuracoes = "+ valorConsulta +" ORDER BY nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}
	
	@Override
	public List<UnidadeEnsinoVO> consultarRapidaPorConfiguracaoContabil(Integer valorConsulta,  boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE configuracaoContabil = ").append(valorConsulta);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado, nivelMontarDados);
	}
	
	@Override
	public List<UnidadeEnsinoVO> consultarRapidaTodosCodigoContabilUnidadeEnsino(boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder str = new StringBuilder();
		str.append("select distinct codigoIntegracaoContabil, nome FROM unidadeEnsino order by codigoIntegracaoContabil ");		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(str.toString());
		List<UnidadeEnsinoVO> vetResultado = new ArrayList<UnidadeEnsinoVO>(0);
		while (tabelaResultado.next()) {
			UnidadeEnsinoVO obj = new UnidadeEnsinoVO();
			obj.setCodigoIntegracaoContabil(tabelaResultado.getString("codigoIntegracaoContabil"));
			obj.setNome(tabelaResultado.getString("nome"));
			vetResultado.add(obj);
		}
		return vetResultado;
	}
	
	public List<UnidadeEnsinoVO> consultarPorNomeVinculadoBiblioteca(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT distinct unidadeEnsino.* FROM UnidadeEnsino "
				+ " left join unidadeensinobiblioteca on unidadeensinobiblioteca.unidadeensino = unidadeensino.codigo"
				+ " WHERE lower(nome) like('" + "%" + valorConsulta.toLowerCase() + "%') ";
		if (unidadeEnsino > 0) {
			sqlStr += " and unidadeensinobiblioteca.unidadeensino = " + unidadeEnsino ;
		}
		sqlStr += "ORDER BY nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}	

	public List<UnidadeEnsinoVO> consultarUnidadeEnsinoComboBox(Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		return consultarUnidadeEnsinoComboBox(unidadeEnsino, controlarAcesso, false, usuario);
	}
	
	@Override
	public List<UnidadeEnsinoVO> consultarUnidadeEnsinoComboBox(Integer unidadeEnsino, boolean controlarAcesso, boolean apenasAtivas, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT DISTINCT codigo, nome FROM UnidadeEnsino where 1 = 1 ");
		if (unidadeEnsino != null && unidadeEnsino != 0) {
			sb.append(" and  codigo = ");
			sb.append(unidadeEnsino.intValue());
		}
		if(apenasAtivas) {
			sb.append(" and  UnidadeEnsino.desativada = false ");
		}
		sb.append(" ORDER BY nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<UnidadeEnsinoVO> vetResultado = new ArrayList<UnidadeEnsinoVO>(0);
		while (tabelaResultado.next()) {
			UnidadeEnsinoVO obj = new UnidadeEnsinoVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setNome(tabelaResultado.getInt("codigo") + " - " + tabelaResultado.getString("nome"));
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	public List<UnidadeEnsinoVO> consultarUnidadeEnsinoOndeCursoDiferenteDePosGraduacao(int nivelMontarDados, UsuarioVO usuario) throws Exception {
		// getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "";
		sqlStr = "select distinct unidadeensino.* from unidadeensino inner join unidadeensinocurso on unidadeensinocurso.unidadeensino = unidadeensino.codigo inner join curso on curso.codigo = unidadeensinocurso.curso where apresentarTelaProcessoSeletivo and unidadeensino.apresentarHomePreInscricao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<UnidadeEnsinoVO> consultarPorUsuarioUnidadeEnsinoVinculadaAoUsuario(Integer codUsuario, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder(" select distinct unidadeEnsino.* from unidadeEnsino "); 
		sqlStr.append(" inner join usuarioperfilacesso on usuarioperfilacesso.unidadeensino = unidadeensino.codigo "); 
		sqlStr.append(" where usuarioperfilacesso.usuario =  ").append(codUsuario);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<UnidadeEnsinoVO> consultarUnidadeEnsinoFaltandoLista(List<UnidadeEnsinoVO> unidadeEnsinoVOs, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder(getSQLPadraoConsultaBasica());
		sb.append(" where 1 = 1 ");
		if (!unidadeEnsinoVOs.isEmpty()) {
			sb.append("and unidadeEnsino.codigo  not in ( ");
		}
		int x = 0;
		for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOs) {
			if (x > 0) {
				sb.append(", ");
			}
			sb.append(unidadeEnsinoVO.getCodigo());
			x++;
		}
		if (!unidadeEnsinoVOs.isEmpty()) {
			sb.append(" ) ");
		}
		sb.append("ORDER BY unidadeEnsino.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		return (montarDadosConsultaRapida(tabelaResultado, nivelMontarDados));
	}

	/**
	 * Responsável por realizar uma consulta de <code>UnidadeEnsino</code> através do valor do atributo <code>Integer codigo</code>. Retorna os
	 * objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>UnidadeEnsinoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<UnidadeEnsinoVO> consultarPorCodigo(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM UnidadeEnsino WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
		if (unidadeEnsino != null && unidadeEnsino.intValue() != 0) {
			sqlStr = "SELECT * FROM UnidadeEnsino WHERE codigo = " + unidadeEnsino.intValue() + " ORDER BY codigo";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<UnidadeEnsinoVO> consultarTodasUnidades(boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM UnidadeEnsino ORDER BY nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public UnidadeEnsinoVO obterUnidadeMatriz(boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		UnidadeEnsinoVO obj = new UnidadeEnsinoVO();
		String sqlStr = "SELECT * FROM UnidadeEnsino WHERE unidadeensino.matriz = true;";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (tabelaResultado.next()) {
			obj = montarDados(tabelaResultado, nivelMontarDados, usuario);
		}
		return obj;
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe <code>UnidadeEnsinoVO</code> resultantes da consulta.
	 */
	private List<UnidadeEnsinoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<UnidadeEnsinoVO> vetResultado = new ArrayList<UnidadeEnsinoVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	public List<UnidadeEnsinoVO> montarDadosConsultaRapida(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
		List<UnidadeEnsinoVO> vetResultado = new ArrayList<UnidadeEnsinoVO>(0);
		while (tabelaResultado.next()) {
			UnidadeEnsinoVO obj = new UnidadeEnsinoVO();
			montarDadosBasico(obj, tabelaResultado, nivelMontarDados);
			obj.setFiltrarUnidadeEnsino(true);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	/**
	 * Consulta que espera um ResultSet com os campos mínimos para uma consulta rápida e intelegente. Desta maneira, a mesma será sempre capaz de
	 * montar os atributos básicos do objeto e alguns atributos relacionados de relevância para o contexto da aplicação.
	 * 
	 * @param obj
	 * @throws Exception
	 */
	private void montarDadosBasico(UnidadeEnsinoVO obj, SqlRowSet dadosSQL, int nivelMontarDados) throws Exception {
		// Dados da Unidade Ensino
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setNome(dadosSQL.getString("nome"));
		obj.setNomeExpedicaoDiploma(dadosSQL.getString("nomeExpedicaoDiploma"));
        obj.setMantenedora(dadosSQL.getString("mantenedora"));                
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
			return;
		}
		obj.setCNPJ(dadosSQL.getString("cnpj"));
		obj.setInscEstadual(dadosSQL.getString("inscestadual"));
		obj.setInscMunicipal(dadosSQL.getString("inscMunicipal"));
		obj.setRazaoSocial(dadosSQL.getString("razaoSocial"));
		obj.setCEP(dadosSQL.getString("cep"));
		obj.setEndereco(dadosSQL.getString("endereco"));
		obj.setEnderecoCompleto(dadosSQL.getString("endereco_completo"));
		obj.setNumero(dadosSQL.getString("numero"));
		obj.setSetor(dadosSQL.getString("setor"));
		obj.setComplemento(dadosSQL.getString("complemento"));
		obj.setEmail(dadosSQL.getString("email"));
		obj.setSite(dadosSQL.getString("site"));
		obj.setMantenedora(dadosSQL.getString("mantenedora"));
		obj.setCodigoIES(dadosSQL.getInt("codigoies"));
		obj.setCodigoIESMantenedora(dadosSQL.getInt("codigoIESMantenedora"));
		obj.setCredenciamento(dadosSQL.getString("credenciamento"));
		obj.setCredenciamentoPortaria(dadosSQL.getString("credenciamentoportaria"));
		obj.setCredenciamentoEAD(dadosSQL.getString("credenciamentoEad"));
		obj.setCredenciamentoPortariaEAD(dadosSQL.getString("credenciamentoPortariaEad"));
		obj.setDataPublicacaoDO(dadosSQL.getDate("datapublicacaodo"));
		obj.setTelComercial1(dadosSQL.getString("telcomercial1"));
		obj.setTelefones(dadosSQL.getString("telefones"));
		obj.setPermitirVisualizacaoLogin(dadosSQL.getBoolean("permitirvisualizacaologin"));
		obj.setCaminhoBaseLogo(dadosSQL.getString("caminhoBaseLogo"));
		obj.setNomeArquivoLogo(dadosSQL.getString("nomeArquivoLogo"));
		obj.setCaminhoBaseLogoIndex(dadosSQL.getString("caminhoBaseLogoIndex"));
		obj.setNomeArquivoLogoIndex(dadosSQL.getString("nomeArquivoLogoIndex"));
		obj.setCaminhoBaseLogoRelatorio(dadosSQL.getString("caminhoBaseLogoRelatorio"));
		obj.setNomeArquivoLogoRelatorio(dadosSQL.getString("nomeArquivoLogoRelatorio"));
		obj.setCaminhoBaseLogoEmailCima(dadosSQL.getString("caminhoBaseLogoEmailCima"));
		obj.setNomeArquivoLogoEmailCima(dadosSQL.getString("nomeArquivoLogoEmailCima"));
		obj.setCaminhoBaseLogoEmailBaixo(dadosSQL.getString("caminhoBaseLogoEmailBaixo"));
		obj.setNomeArquivoLogoEmailBaixo(dadosSQL.getString("nomeArquivoLogoEmailBaixo"));
		obj.setCaminhoBaseLogoMunicipio(dadosSQL.getString("caminhoBaseLogoMunicipio"));
		obj.setNomeArquivoLogoMunicipio(dadosSQL.getString("nomeArquivoLogoMunicipio"));
		obj.setCaminhoBaseLogoAplicativo(dadosSQL.getString("caminhobaselogoaplicativo"));
		obj.setCodigoIntegracaoContabil(dadosSQL.getString("codigoIntegracaoContabil"));		
		obj.setNomeArquivoLogoAplicativo(dadosSQL.getString("nomeArquivoLogoAplicativo"));
		obj.setAbreviatura(dadosSQL.getString("abreviatura"));
		obj.setMatriz(dadosSQL.getBoolean("matriz"));
		obj.setCaixaPostal(dadosSQL.getString("caixaPostal"));
		obj.setNivelMontarDados(NivelMontarDados.BASICO);
		obj.setNovoObj(Boolean.FALSE);
		// Dados da Cidade
		obj.getCidade().setCodigo(new Integer(dadosSQL.getInt("cidade.codigo")));
		obj.getCidade().setNome(dadosSQL.getString("cidade.nome"));
		obj.getCidade().setCodigoIBGE(dadosSQL.getString("cidade.codigoibge"));
		// Dados da Estado
		obj.getCidade().getEstado().setCodigo(new Integer(dadosSQL.getInt("estado.codigo")));
		obj.getCidade().getEstado().setNome(dadosSQL.getString("estado.nome"));
		obj.getCidade().getEstado().setSigla(dadosSQL.getString("estado.sigla"));
		obj.getCidade().getEstado().setCodigoIBGE(dadosSQL.getString("estado.codigoibge"));
		// Dados da Pais
		obj.getCidade().getEstado().getPaiz().setCodigo(new Integer(dadosSQL.getInt("paiz.codigo")));
		obj.getCidade().getEstado().getPaiz().setNome(dadosSQL.getString("paiz.nome"));
		// Dados da Conta Corrente
		obj.setContaCorrentePadraoMensalidade(dadosSQL.getInt("contaCorrentePadraoMensalidade.codigo"));
		obj.setContaCorrentePadraoMatricula(dadosSQL.getInt("contaCorrentePadraoMatricula.codigo"));
		obj.setContaCorrentePadraoBiblioteca(dadosSQL.getInt("contaCorrentePadraoBiblioteca.codigo"));
		obj.setContaCorrentePadraoDevolucaoCheque(dadosSQL.getInt("contaCorrentePadraoDevolucaoCheque.codigo"));
		obj.setContaCorrentePadraoMaterialDidatico(dadosSQL.getInt("contaCorrentePadraoMaterialDidatico.codigo"));
		obj.setContaCorrentePadraoNegociacao(dadosSQL.getInt("contaCorrentePadraoNegociacao.codigo"));
		obj.setContaCorrentePadraoProcessoSeletivo(dadosSQL.getInt("contaCorrentePadraoProcessoSeletivo.codigo"));
//		obj.getContaCorrentePadraoVO().setCodigo(dadosSQL.getInt("contaCorrentePadrao.codigo"));
//		obj.getContaCorrentePadraoVO().setNumero(dadosSQL.getString("contaCorrentePadrao.numero"));
		obj.setCnpjMantenedora(dadosSQL.getString("cnpjmantenedora"));
		obj.setUnidadeCertificadora(dadosSQL.getString("unidadecertificadora"));
		obj.setCnpjUnidadeCertificadora(dadosSQL.getString("cnpjunidadecertificadora"));
		obj.setCodigoIESUnidadeCertificadora(dadosSQL.getInt("codigoiesunidadecertificadora"));
		obj.getConfiguracaoMobileVO().setCodigo(dadosSQL.getInt("configuracaomobile"));

		// Dados da Configurações
		obj.getConfiguracoes().setCodigo(dadosSQL.getInt("configuracoes.codigo"));
		obj.getConfiguracoes().setNome(dadosSQL.getString("configuracoes.nome"));
		obj.getCidade().setNivelMontarDados(NivelMontarDados.BASICO);
		// Dados do Coordenador TCC
		obj.getCoordenadorTCC().setCodigo(dadosSQL.getInt("coordenadorTCC"));
		obj.getCoordenadorTCC().getPessoa().setNome(dadosSQL.getString("nomeCoordenadorTCC"));
		obj.setCodigoTributacaoMunicipio(dadosSQL.getString("codigoTributacaoMunicipio"));
		obj.setApresentarHomePreInscricao(dadosSQL.getBoolean("apresentarHomePreInscricao"));
		obj.setLeiCriacao1(dadosSQL.getString("leiCriacao1"));
		obj.setLeiCriacao2(dadosSQL.getString("leiCriacao2"));
		obj.setNomeArquivoLogoPaginaInicial(dadosSQL.getString("nomeArquivoLogoPaginaInicial"));
		obj.setCaminhoBaseLogoPaginaInicial(dadosSQL.getString("caminhoBaseLogoPaginaInicial"));
		obj.setUsarConfiguracaoPadrao(dadosSQL.getBoolean("usarConfiguracaoPadrao"));
		obj.getDiretorGeral().getPessoa().setNome(dadosSQL.getString("diretorGeralNome"));		
		obj.setInformacoesAdicionaisEndereco(dadosSQL.getString("informacoesAdicionaisEndereco"));
		
		if(dadosSQL.getInt("pess_orientadorPadraoEstagio.codigo") != 0) {
			obj.getOrientadorPadraoEstagio().getPessoa().setCodigo(dadosSQL.getInt("pess_orientadorPadraoEstagio.codigo"));
			obj.getOrientadorPadraoEstagio().getPessoa().setNome(dadosSQL.getString("pess_orientadorPadraoEstagio.nome"));
			obj.getOrientadorPadraoEstagio().getPessoa().setCPF(dadosSQL.getString("pess_orientadorPadraoEstagio.cpf"));
			obj.getOrientadorPadraoEstagio().getPessoa().setTelefoneRes(dadosSQL.getString("pess_orientadorPadraoEstagio.telefoneRes"));
			obj.getOrientadorPadraoEstagio().getPessoa().setTelefoneComer(dadosSQL.getString("pess_orientadorPadraoEstagio.telefoneComer"));
			obj.getOrientadorPadraoEstagio().getPessoa().setCelular(dadosSQL.getString("pess_orientadorPadraoEstagio.celular"));
			obj.getOrientadorPadraoEstagio().getPessoa().setListaPessoaEmailInstitucionalVO(getFacadeFactory().getPessoaEmailInstitucionalFacade().consultarListaPessoaEmailInstitucionalPorPessoa(dadosSQL.getInt("pess_orientadorPadraoEstagio.codigo"), Uteis.NIVELMONTARDADOS_DADOSBASICOS, new UsuarioVO()));	
		}
		
		obj.setObservacao(dadosSQL.getString("unidadeensino.observacao"));
		
		obj.getOperadorResponsavel().setCodigo(dadosSQL.getInt("operadorResponsavel"));
		obj.getOperadorResponsavel().getPessoa().setCodigo(dadosSQL.getInt("operadorResponsavel.codigo"));
		obj.getOperadorResponsavel().getPessoa().setNome(dadosSQL.getString("operadorResponsavel.nome"));
		obj.setNumeroCredenciamentoEAD(dadosSQL.getString("numeroCredenciamentoEad"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return;
		}		
//		obj.setConfiguracaoNotaFiscalVO(getFacadeFactory().getConfiguracaoNotaFiscalFacade().consultarPorChavePrimaria(dadosSQL.getInt("configuracaonotafiscal"), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, new UsuarioVO()));
	}

	/**
	 * Consulta que espera um ResultSet com todos os campos e dados de objetos relacionados, Para reconstituir o objeto por completo, de uma
	 * determinada entidade.
	 * 
	 * @param obj
	 * @throws Exception
	 */
	private void montarDadosCompleto(UnidadeEnsinoVO obj, SqlRowSet dadosSQL, boolean isAplicacao, UsuarioVO usuarioVO) throws Exception {
	    // Dados Unidade Ensino
	    obj.setCredenciamento(dadosSQL.getString("credenciamento"));
	    obj.setCodigo((dadosSQL.getInt("codigo")));
	    obj.setNome(dadosSQL.getString("nome"));
	    obj.setNomeExpedicaoDiploma(dadosSQL.getString("nomeExpedicaoDiploma"));
	    obj.setCNPJ(dadosSQL.getString("CNPJ"));
	    obj.setInscEstadual(dadosSQL.getString("inscEstadual"));
	    obj.setInscMunicipal(dadosSQL.getString("inscMunicipal"));
	    obj.getConfiguracoes().setCodigo(dadosSQL.getInt("configuracoes"));
	    obj.setCodigoIES(dadosSQL.getInt("codigoies"));
	    obj.setCodigoIESMantenedora(dadosSQL.getInt("codigoIESMantenedora"));
	    obj.setCredenciamentoPortaria(dadosSQL.getString("credenciamentoportaria"));
	    obj.setDataPublicacaoDO(dadosSQL.getDate("datapublicacaodo"));
	    obj.setMantenedora(dadosSQL.getString("mantenedora"));
	    obj.setRazaoSocial(dadosSQL.getString("razaoSocial"));
	    obj.setEndereco(dadosSQL.getString("endereco"));
	    obj.setCaixaPostal(dadosSQL.getString("caixaPostal"));
	    obj.setSetor(dadosSQL.getString("setor"));
	    obj.setNumero(dadosSQL.getString("numero"));
	    obj.setComplemento(dadosSQL.getString("complemento"));
	    obj.setCEP(dadosSQL.getString("CEP"));
	    obj.setTipoEmpresa(dadosSQL.getString("tipoEmpresa"));
	    obj.setRG(dadosSQL.getString("RG"));
	    obj.setCPF(dadosSQL.getString("CPF"));
	    obj.setTelComercial1(dadosSQL.getString("telComercial1"));
	    obj.setTelComercial2(dadosSQL.getString("telComercial2"));
	    obj.setTelComercial3(dadosSQL.getString("telComercial3"));
	    obj.setEmail(dadosSQL.getString("email"));
	    obj.setSite(dadosSQL.getString("site"));
	    obj.setFax(dadosSQL.getString("fax"));
	    obj.setMatriz(dadosSQL.getBoolean("matriz"));
	    obj.setDesativada(dadosSQL.getBoolean("desativada"));
	    obj.setApresentarTelaProcessoSeletivo(dadosSQL.getBoolean("apresentarTelaProcessoSeletivo"));
	    obj.setAbreviatura(dadosSQL.getString("abreviatura"));
	    obj.setCaminhoBaseLogo(dadosSQL.getString("caminhoBaseLogo"));
	    obj.setNomeArquivoLogo(dadosSQL.getString("nomeArquivoLogo"));
	    obj.setCaminhoBaseLogoIndex(dadosSQL.getString("caminhoBaseLogoIndex"));
	    obj.setNomeArquivoLogoIndex(dadosSQL.getString("nomeArquivoLogoIndex"));
	    obj.setCaminhoBaseLogoRelatorio(dadosSQL.getString("caminhoBaseLogoRelatorio"));
	    obj.setNomeArquivoLogoRelatorio(dadosSQL.getString("nomeArquivoLogoRelatorio"));
	    obj.setCaminhoBaseLogoEmailCima(dadosSQL.getString("caminhoBaseLogoEmailCima"));
	    obj.setNomeArquivoLogoEmailCima(dadosSQL.getString("nomeArquivoLogoEmailCima"));
	    obj.setCaminhoBaseLogoEmailBaixo(dadosSQL.getString("caminhoBaseLogoEmailBaixo"));
	    obj.setNomeArquivoLogoEmailBaixo(dadosSQL.getString("nomeArquivoLogoEmailBaixo"));
	    obj.setCaminhoBaseLogoMunicipio(dadosSQL.getString("caminhoBaseLogoMunicipio"));
	    obj.setNomeArquivoLogoMunicipio(dadosSQL.getString("nomeArquivoLogoMunicipio"));
	    obj.setCaminhoBaseLogoAplicativo(dadosSQL.getString("caminhobaselogoaplicativo"));
	    obj.setNomeArquivoLogoAplicativo(dadosSQL.getString("nomeArquivoLogoAplicativo"));
	    obj.setCodigoIntegracaoContabil(dadosSQL.getString("codigoIntegracaoContabil"));
	    // obj.getConfiguracaoContabilVO().setCodigo(dadosSQL.getInt("configuracaocontabil.codigo"));
	    // obj.setConfiguracaoNotaFiscalVO(getFacadeFactory().getConfiguracaoNotaFiscalFacade().consultarPorChavePrimaria(dadosSQL.getInt("configuracaonotafiscal"), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
	    obj.setCodigoTributacaoMunicipio(dadosSQL.getString("codigoTributacaoMunicipio"));
	    obj.setNovoObj(Boolean.FALSE);
	    obj.setAno(dadosSQL.getString("ano"));
	    obj.setPermitirVisualizacaoLogin(dadosSQL.getBoolean("permitirVisualizacaoLogin"));
	    obj.setDependenciaAdministrativa(dadosSQL.getString("dependenciaAdministrativa"));
	    obj.setLocalizacaoZonaEscola(dadosSQL.getString("localizacaoZonaEscola"));
	    obj.setLocalizacaoDiferenciadaEscola(dadosSQL.getString("localizacaoDiferenciadaEscola"));
	    obj.setUnidadeVinculadaEscolaEducacaoBasica(dadosSQL.getString("unidadeVinculadaEscolaEducacaoBasica"));
	    obj.setCodigoEscolaSede(dadosSQL.getInt("codigoEscolaSede"));
	    obj.setForneceAguaPotavelConsumoHumano(dadosSQL.getBoolean("forneceAguaPotavelConsumoHumano"));
	    obj.setCategoriaEscolaPrivada(dadosSQL.getString("categoriaEscolaPrivada"));
	    obj.setConveniadaPoderPublico(dadosSQL.getString("conveniadaPoderPublico"));
	    obj.setLocalFuncionamentoDaEscola(dadosSQL.getString("localFuncionamentoDaEscola"));
	    obj.setFormaOcupacaoPredio(dadosSQL.getString("formaOcupacaoPredio"));
	    obj.setPredioCompartilhado(dadosSQL.getBoolean("predioCompartilhado"));
	    obj.setCodigoEscolaCompartilhada1(dadosSQL.getString("codigoEscolaCompartilhada1"));
	    obj.setCodigoEscolaCompartilhada2(dadosSQL.getString("codigoEscolaCompartilhada2"));
	    obj.setCodigoEscolaCompartilhada3(dadosSQL.getString("codigoEscolaCompartilhada3"));
	    obj.setCodigoEscolaCompartilhada4(dadosSQL.getString("codigoEscolaCompartilhada4"));
	    obj.setCodigoEscolaCompartilhada5(dadosSQL.getString("codigoEscolaCompartilhada5"));
	    obj.setCodigoEscolaCompartilhada6(dadosSQL.getString("codigoEscolaCompartilhada6"));
	    obj.setAguaConsumida(dadosSQL.getString("aguaConsumida"));
	    obj.setAbastecimentoAgua(dadosSQL.getString("abastecimentoAgua"));
	    obj.setAbastecimentoEnergia(dadosSQL.getString("abastecimentoEnergia"));
	    obj.setEsgotoSanitario(dadosSQL.getString("esgotoSanitario"));
	    obj.setDestinoLixo(dadosSQL.getString("destinoLixo"));
	    obj.setTratamentoLixo(dadosSQL.getString("tratamentoLixo"));
	    obj.setBanheiroExclusivoFuncionarios(dadosSQL.getBoolean("banheiroExclusivoFuncionarios"));
	    obj.setPiscina(dadosSQL.getBoolean("piscina"));
	    obj.setSalaRepousoAluno(dadosSQL.getBoolean("salaRepousoAluno"));
	    obj.setSalaArtes(dadosSQL.getBoolean("salaArtes"));
	    obj.setSalaMusica(dadosSQL.getBoolean("salaMusica"));
	    obj.setSalaDanca(dadosSQL.getBoolean("salaDanca"));
	    obj.setSalaMultiuso(dadosSQL.getBoolean("salaMultiuso"));
	    obj.setTerreirao(dadosSQL.getBoolean("terreirao"));
	    obj.setViveiroAnimais(dadosSQL.getBoolean("viveiroAnimais"));
	    obj.setCorrimaoGuardaCorpos(dadosSQL.getBoolean("corrimaoGuardaCorpos"));
	    obj.setElevador(dadosSQL.getBoolean("elevador"));
	    obj.setPisosTateis(dadosSQL.getBoolean("pisosTateis"));
	    obj.setPortasVaoLivreMinimoOitentaCentimetros(dadosSQL.getBoolean("portasVaoLivreMinimoOitentaCentimetros"));
	    obj.setRampas(dadosSQL.getBoolean("rampas"));
	    obj.setSinalizacaoSonora(dadosSQL.getBoolean("sinalizacaoSonora"));
	    obj.setSinalizacaoTatil(dadosSQL.getBoolean("sinalizacaoTatil"));
	    obj.setSinalizacaoVisual(dadosSQL.getBoolean("sinalizacaoVisual"));
	    obj.setNenhumRecursoAcessibilidade(dadosSQL.getBoolean("nenhumRecursoAcessibilidade"));
	    obj.setNumeroSalasAulaUtilizadasEscolaDentroPredioEscolar(dadosSQL.getInt("numeroSalasAulaUtilizadasEscolaDentroPredioEscolar"));
	    obj.setAntenaParabolica(dadosSQL.getBoolean("antenaParabolica"));
	    obj.setComputadores(dadosSQL.getBoolean("computadores"));
	    obj.setCopiadora(dadosSQL.getBoolean("copiadora"));
	    obj.setImpressora(dadosSQL.getBoolean("impressora"));
	    obj.setImpressoraMultifuncional(dadosSQL.getBoolean("impressoraMultifuncional"));
	    obj.setScanner(dadosSQL.getBoolean("scanner"));
	    obj.setAcessoInternetUsoAdiministrativo(dadosSQL.getBoolean("acessoInternetUsoAdiministrativo"));
	    obj.setAcessoInternetUsoProcessoEnsinoAprendizagem(dadosSQL.getBoolean("acessoInternetUsoProcessoEnsinoAprendizagem"));
	    obj.setAcessoInternetUsoAlunos(dadosSQL.getBoolean("acessoInternetUsoAlunos"));
	    obj.setAcessoInternetComunidade(dadosSQL.getBoolean("acessoInternetComunidade"));
	    obj.setNaoPossuiAcessoInternet(dadosSQL.getBoolean("naoPossuiAcessoInternet"));
	    obj.setAcessoInternetComputadoresEscola(dadosSQL.getBoolean("acessoInternetComputadoresEscola"));
	    obj.setAcessoInternetDispositivosPessoais(dadosSQL.getBoolean("acessoInternetDispositivosPessoais"));
	    obj.setRedeLocalCabo(dadosSQL.getBoolean("redeLocalCabo"));
	    obj.setRedeLocalWireless(dadosSQL.getBoolean("redeLocalWireless"));
	    obj.setNaoExisteRedeLocal(dadosSQL.getBoolean("naoExisteRedeLocal"));
	    obj.setAlimentacaoEscolarAlunos(dadosSQL.getBoolean("alimentacaoEscolarAlunos"));
	    obj.setEducacaoEscolarIndigena(dadosSQL.getBoolean("educacaoEscolarIndigena"));
	    obj.setLinguaIndigena(dadosSQL.getBoolean("linguaIndigena"));
	    obj.setLinguaPortuguesa(dadosSQL.getBoolean("linguaPortuguesa"));
	    obj.setCodigoLinguaIndigena1(dadosSQL.getInt("codigoLinguaIndigena1"));
	    obj.setCodigoLinguaIndigena2(dadosSQL.getInt("codigoLinguaIndigena2"));
	    obj.setCodigoLinguaIndigena3(dadosSQL.getInt("codigoLinguaIndigena3"));
	    obj.setSalaDiretoria(dadosSQL.getBoolean("salaDiretoria"));
	    obj.setSalaProfessores(dadosSQL.getBoolean("salaProfessores"));
	    obj.setSalaSecretaria(dadosSQL.getBoolean("salaSecretaria"));
	    obj.setLaboratorioInformatica(dadosSQL.getBoolean("laboratorioInformatica"));
	    obj.setLaboratorioCiencias(dadosSQL.getBoolean("laboratorioCiencias"));
	    obj.setRecursosMultifuncionais(dadosSQL.getBoolean("recursosMultifuncionais"));
	    obj.setQuadraEsportesCoberta(dadosSQL.getBoolean("quadraEsportesCoberta"));
	    obj.setQuadraEsportesDescoberta(dadosSQL.getBoolean("quadraEsportesDescoberta"));
	    obj.setCozinha(dadosSQL.getBoolean("cozinha"));
	    obj.setBiblioteca(dadosSQL.getBoolean("biblioteca"));
	    obj.setSalaLeitura(dadosSQL.getBoolean("salaLeitura"));
	    obj.setParqueInfantil(dadosSQL.getBoolean("parqueInfantil"));
	    obj.setBercario(dadosSQL.getBoolean("bercario"));
	    obj.setBanheiroForaPredio(dadosSQL.getBoolean("banheiroForaPredio"));
	    obj.setBanheiroDentroPredio(dadosSQL.getBoolean("banheiroDentroPredio"));
	    obj.setBanheiroEducacaoInfantil(dadosSQL.getBoolean("banheiroEducacaoInfantil"));
	    obj.setBanheiroDeficiencia(dadosSQL.getBoolean("banheiroDeficiencia"));
	    obj.setViasDeficiencia(dadosSQL.getBoolean("viasDeficiencia"));
	    obj.setBanheiroChuveiro(dadosSQL.getBoolean("banheiroChuveiro"));
	    obj.setRefeitorio(dadosSQL.getBoolean("refeitorio"));
	    obj.setDespensa(dadosSQL.getBoolean("despensa"));
	    obj.setAlmoxarifado(dadosSQL.getBoolean("almoxarifado"));
	    obj.setAuditorio(dadosSQL.getBoolean("auditorio"));
	    obj.setPatioCoberto(dadosSQL.getBoolean("patioCoberto"));
	    obj.setPatioDescoberto(dadosSQL.getBoolean("patioDescoberto"));
	    obj.setAlojamentoAluno(dadosSQL.getBoolean("alojamentoAluno"));
	    obj.setAlojamentoProfessor(dadosSQL.getBoolean("alojamentoProfessor"));
	    obj.setAreaVerde(dadosSQL.getBoolean("areaVerde"));
	    obj.setLavanderia(dadosSQL.getBoolean("lavanderia"));
	    obj.setNenhumaDependencia(dadosSQL.getBoolean("nenhumaDependencia"));
	    obj.setNumeroSalasAulaExistente(dadosSQL.getString("numeroSalasAulaExistente"));
	    obj.setNumeroSalasDentroForaPredio(dadosSQL.getString("numeroSalasDentroForaPredio"));
	    obj.setQuantidadeTelevisao(dadosSQL.getInt("quantidadeTelevisao"));
	    obj.setQuantidadeVideoCassete(dadosSQL.getInt("quantidadeVideoCassete"));
	    obj.setQuantidadeDVD(dadosSQL.getInt("quantidadeDVD"));
	    obj.setQuantidadeAntenaParabolica(dadosSQL.getInt("quantidadeAntenaParabolica"));
	    obj.setQuantidadeCopiadora(dadosSQL.getInt("quantidadeCopiadora"));
	    obj.setQuantidadeRetroprojetor(dadosSQL.getInt("quantidadeRetroprojetor"));
	    obj.setQuantidadeImpressora(dadosSQL.getInt("quantidadeImpressora"));
	    obj.setQuantidadeAparelhoSom(dadosSQL.getInt("quantidadeAparelhoSom"));
	    obj.setQuantidadeProjetorMultimidia(dadosSQL.getInt("quantidadeProjetorMultimidia"));
	    obj.setQuantidadeFax(dadosSQL.getInt("quantidadeFax"));
	    obj.setQuantidadeMaquinaFotograficaFilmadora(dadosSQL.getInt("quantidadeMaquinaFotograficaFilmadora"));
	    obj.setQuantidadeComputadores(dadosSQL.getInt("quantidadeComputadores"));
	    obj.setQuantidadeComputadoresAdministrativos(dadosSQL.getInt("quantidadeComputadoresAdministrativos"));
	    obj.setQuantidadeComputadoresAlunos(dadosSQL.getInt("quantidadeComputadoresAlunos"));
	    obj.setComputadoresAcessoInternet(dadosSQL.getBoolean("computadoresAcessoInternet"));
	    obj.setInternetBandaLarga(dadosSQL.getBoolean("internetBandaLarga"));
	    obj.setCodigoOrgaoRegionalEnsino(dadosSQL.getString("codigoOrgaoRegionalEnsino"));
	    obj.setCodigoDistritoCenso(dadosSQL.getString("codigoDistritoCenso"));
	    
	    // obj.setChancelaVO(getFacadeFactory().getChancelaFacade().consultarPorChavePrimaria(dadosSQL.getInt("chancela"), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
	    obj.setTipoChancela(dadosSQL.getString("tipochancela"));
	    obj.setValorFixoChancela(dadosSQL.getDouble("valorFixoChancela"));
	    obj.setPorcentagemChancela(dadosSQL.getDouble("porcentagemChancela"));
	    
	    // Dados do Coordenador TCC
	    obj.getCoordenadorTCC().setCodigo(dadosSQL.getInt("coordenadorTCC"));
	    obj.getCoordenadorTCC().getPessoa().setCodigo(dadosSQL.getInt("codigoPessoaCoordenadorTCC"));
	    obj.getCoordenadorTCC().getPessoa().setNome(dadosSQL.getString("nomeCoordenadorTCC"));
	    // Dados da Cidade
	    obj.getCidade().setCodigo((dadosSQL.getInt("cidade")));
	    obj.getCidade().setNome(dadosSQL.getString("cidade.nome"));
	    obj.getCidade().setCodigoIBGE(dadosSQL.getString("cidade.codigoIBGE"));
	    obj.getCidade().setCodigoDistrito(dadosSQL.getInt("cidade.codigoDistrito"));
	    obj.getCidade().setCodigoInep(dadosSQL.getInt("cidade.codigoInep"));
	    // Dados da Estado
	    obj.getCidade().getEstado().setCodigo((dadosSQL.getInt("estado.codigo")));
	    obj.getCidade().getEstado().setNome(dadosSQL.getString("estado.nome"));
	    obj.getCidade().getEstado().setSigla(dadosSQL.getString("estado.sigla"));
	    obj.getCidade().getEstado().setCodigoInep(dadosSQL.getInt("estado.codigoInep"));
	    // Dados do Diretor Geral
	    obj.getDiretorGeral().setCodigo(dadosSQL.getInt("diretorGeral"));
	    obj.getDiretorGeral().getPessoa().setNome(dadosSQL.getString("diretorGeral.nome"));
	    obj.getResponsavelCobrancaUnidade().setCodigo(dadosSQL.getInt("responsavelCobrancaUnidade"));
	    obj.getResponsavelCobrancaUnidade().setNome(dadosSQL.getString("responsavelCobrancaUnidade.nome"));
	    obj.setApresentarHomePreInscricao(dadosSQL.getBoolean("apresentarHomePreInscricao"));
	    obj.setLeiCriacao1(dadosSQL.getString("leiCriacao1"));
	    obj.setLeiCriacao2(dadosSQL.getString("leiCriacao2"));
	    obj.setNomeArquivoLogoPaginaInicial(dadosSQL.getString("nomeArquivoLogoPaginaInicial"));
	    
	    obj.setContaCorrentePadraoProcessoSeletivo(dadosSQL.getInt("contaCorrentePadraoProcessoSeletivo"));
	    obj.setContaCorrentePadraoMatricula(dadosSQL.getInt("contaCorrentePadraoMatricula"));
	    obj.setContaCorrentePadraoMensalidade(dadosSQL.getInt("contaCorrentePadraoMensalidade"));
	    obj.setContaCorrentePadraoMaterialDidatico(dadosSQL.getInt("contaCorrentePadraoMaterialDidatico"));
	    obj.setContaCorrentePadraoBiblioteca(dadosSQL.getInt("contaCorrentePadraoBiblioteca"));
	    obj.setContaCorrentePadraoRequerimento(dadosSQL.getInt("contaCorrentePadraoRequerimento"));
	    obj.setContaCorrentePadraoNegociacao(dadosSQL.getInt("contaCorrentePadraoNegociacao"));
	    obj.setContaCorrentePadraoDevolucaoCheque(dadosSQL.getInt("contaCorrentePadraoDevolucaoCheque"));
	    obj.setInformacoesAdicionaisEndereco(dadosSQL.getString("informacoesAdicionaisEndereco"));

	    obj.setCnpjMantenedora(dadosSQL.getString("cnpjmantenedora"));
	    obj.setUnidadeCertificadora(dadosSQL.getString("unidadecertificadora"));
	    obj.setCnpjUnidadeCertificadora(dadosSQL.getString("cnpjunidadecertificadora"));
	    obj.setCodigoIESUnidadeCertificadora(dadosSQL.getInt("codigoiesunidadecertificadora"));
	    obj.getConfiguracaoMobileVO().setCodigo(dadosSQL.getInt("configuracaomobile"));

	    // obj.getCentroResultadoVO().setCodigo((dadosSQL.getInt("centroResultado.codigo")));
	    // obj.getCentroResultadoVO().setDescricao(dadosSQL.getString("centroResultado.descricao"));
	    // obj.getCentroResultadoVO().setIdentificadorCentroResultado(dadosSQL.getString("centroResultado.identificadorCentroResultado"));
	    // 
	    // obj.getCentroResultadoRequerimentoVO().setCodigo((dadosSQL.getInt("centroResultadoRequerimento.codigo")));
	    // obj.getCentroResultadoRequerimentoVO().setDescricao(dadosSQL.getString("centroResultadoRequerimento.descricao"));
	    // obj.getCentroResultadoRequerimentoVO().setIdentificadorCentroResultado(dadosSQL.getString("centroResultadoRequerimento.identificadorCentroResultado"));
	    
	    if (!isAplicacao) {
	        obj.setMaterialUnidadeEnsinoVOs(getFacadeFactory().getMaterialUnidadeEnsinoFacade().consultarPorUnidadeEnsino(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));
	        obj.setUnidadeEnsinoCursoCentroResultado(getFacadeFactory().getUnidadeEnsinoCursoCentroResultadoFacade().consultaRapidaPorUnidadeEnsino(obj, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
	        obj.setUnidadeEnsinoNivelEducacionalCentroResultado(getFacadeFactory().getUnidadeEnsinoNivelEducacionalCentroResultadoFacade().consultaRapidaPorUnidadeEnsino(obj, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
	        obj.setUnidadeEnsinoTipoRequerimentoCentroResultado(getFacadeFactory().getUnidadeEnsinoTipoRequerimentoCentroResultadoFacade().consultaRapidaPorUnidadeEnsino(obj, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
	    }
	    obj.getConfiguracaoGEDVO().setCodigo(dadosSQL.getInt("configuracaoGED.codigo"));
	    obj.getConfiguracaoGEDVO().setNome(dadosSQL.getString("configuracaoGED.nome"));
	    obj.getResponsavelNotificacaoAlteracaoCronogramaAula().setCodigo(dadosSQL.getInt("responsavelNotificacaoAlteracaoCronogramaAula"));
	    obj.getResponsavelNotificacaoAlteracaoCronogramaAula().getPessoa().setCodigo(dadosSQL.getInt("pessoaResponsavelNotificacaoAlteracaoCronogramaAula.codigo"));
	    obj.getResponsavelNotificacaoAlteracaoCronogramaAula().getPessoa().setNome(dadosSQL.getString("pessoaResponsavelNotificacaoAlteracaoCronogramaAula.nome"));
	    
	    if(dadosSQL.getInt("orientadorPadraoEstagio") != 0) {
	        obj.getOrientadorPadraoEstagio().setCodigo(dadosSQL.getInt("orientadorPadraoEstagio"));
	        obj.getOrientadorPadraoEstagio().getPessoa().setCodigo(dadosSQL.getInt("orientadorPadraoEstagio.codigo"));
	        obj.getOrientadorPadraoEstagio().getPessoa().setNome(dadosSQL.getString("orientadorPadraoEstagio.nome"));
	        obj.getOrientadorPadraoEstagio().getPessoa().setCPF(dadosSQL.getString("orientadorPadraoEstagio.cpf"));
	        obj.getOrientadorPadraoEstagio().getPessoa().setTelefoneRes(dadosSQL.getString("orientadorPadraoEstagio.telefoneRes"));
	        obj.getOrientadorPadraoEstagio().getPessoa().setTelefoneComer(dadosSQL.getString("orientadorPadraoEstagio.telefoneComer"));
	        obj.getOrientadorPadraoEstagio().getPessoa().setCelular(dadosSQL.getString("orientadorPadraoEstagio.celular"));
	        obj.getOrientadorPadraoEstagio().getPessoa().getListaPessoaEmailInstitucionalVO().add(getFacadeFactory().getPessoaEmailInstitucionalFacade().consultarPorPessoa(obj.getOrientadorPadraoEstagio().getPessoa().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
	    }
	    
	    
	    obj.setObservacao(dadosSQL.getString("observacao"));
	    obj.getOperadorResponsavel().getPessoa().setCodigo(dadosSQL.getInt("operadorResponsavel.codigo"));
	    obj.getOperadorResponsavel().getPessoa().setNome(dadosSQL.getString("operadorResponsavel.nome"));
	    if (Uteis.isAtributoPreenchido(dadosSQL.getInt("configuracaoDiplomaDigital.codigo"))) {
	        // obj.getConfiguracaoDiplomaDigital().setCodigo(dadosSQL.getInt("configuracaoDiplomaDigital.codigo"));
	        // obj.getConfiguracaoDiplomaDigital().setDescricao(dadosSQL.getString("configuracaoDiplomaDigital.descricao"));
	        // obj.getConfiguracaoDiplomaDigital().setPadrao(dadosSQL.getBoolean("configuracaoDiplomaDigital.padrao"));
	    }
	    obj.setDataCredenciamento(dadosSQL.getDate("unidadeensino.dataCredenciamento"));
	    obj.setVeiculoPublicacaoCredenciamento(dadosSQL.getString("unidadeensino.veiculoPublicacaoCredenciamento"));
	    obj.setSecaoPublicacaoCredenciamento(dadosSQL.getInt("unidadeensino.secaoPublicacaoCredenciamento"));
	    obj.setPaginaPublicacaoCredenciamento(dadosSQL.getInt("unidadeensino.paginaPublicacaoCredenciamento"));
	    obj.setNumeroDOUCredenciamento(dadosSQL.getInt("unidadeensino.numeroDOUCredenciamento"));
	    
	    // --- CORREÇÃO DE SEGURANÇA PARA ENUMS ---
	    obj.setTipoAutorizacaoEnum(safeEnumValueOf(dadosSQL.getString("unidadeensino.tipoAutorizacaoEnum")));
	    // ----------------------------------------

	    obj.setInformarDadosRegistradora(dadosSQL.getBoolean("unidadeensino.informarDadosRegistradora"));
	    if (obj.getInformarDadosRegistradora()) {
	        obj.setUtilizarEnderecoUnidadeEnsinoRegistradora(dadosSQL.getBoolean("unidadeensino.utilizarEnderecoUnidadeEnsinoRegistradora"));
	        obj.setUtilizarCredenciamentoUnidadeEnsino(dadosSQL.getBoolean("unidadeensino.utilizarCredenciamentoUnidadeEnsino"));
	        obj.setUtilizarMantenedoraUnidadeEnsino(dadosSQL.getBoolean("unidadeensino.utilizarMantenedoraUnidadeEnsino"));
	        if (!obj.getUtilizarEnderecoUnidadeEnsinoRegistradora()) {
	            obj.setCepRegistradora(dadosSQL.getString("unidadeensino.cepRegistradora"));
	            obj.getCidadeRegistradora().setCodigo(dadosSQL.getInt("unidadeensino.cidadeRegistradora"));
	            obj.getCidadeRegistradora().setNome(dadosSQL.getString("cidadeRegistradora.nome"));
	            obj.getCidadeRegistradora().setCodigoIBGE(dadosSQL.getString("cidadeRegistradora.codigoIBGE"));
	            obj.getCidadeRegistradora().getEstado().setSigla(dadosSQL.getString("estadoRegistradora.sigla"));
	            obj.setComplementoRegistradora(dadosSQL.getString("unidadeensino.complementoRegistradora"));
	            obj.setBairroRegistradora(dadosSQL.getString("unidadeensino.bairroRegistradora"));
	            obj.setEnderecoRegistradora(dadosSQL.getString("unidadeensino.enderecoRegistradora"));
	            obj.setNumeroRegistradora(dadosSQL.getString("unidadeensino.numeroRegistradora"));
	        }
	        if (!obj.getUtilizarCredenciamentoUnidadeEnsino()) {
	            obj.setNumeroCredenciamentoRegistradora(dadosSQL.getString("unidadeensino.numeroCredenciamentoRegistradora"));
	            obj.setDataCredenciamentoRegistradora(dadosSQL.getDate("unidadeensino.dataCredenciamentoRegistradora"));
	            obj.setDataPublicacaoDORegistradora(dadosSQL.getDate("unidadeensino.dataPublicacaoDORegistradora"));
	            obj.setVeiculoPublicacaoCredenciamentoRegistradora(dadosSQL.getString("unidadeensino.veiculoPublicacaoCredenciamentoRegistradora"));
	            obj.setSecaoPublicacaoCredenciamentoRegistradora(dadosSQL.getInt("unidadeensino.secaoPublicacaoCredenciamentoRegistradora"));
	            obj.setPaginaPublicacaoCredenciamentoRegistradora(dadosSQL.getInt("unidadeensino.paginaPublicacaoCredenciamentoRegistradora"));
	            obj.setNumeroPublicacaoCredenciamentoRegistradora(dadosSQL.getInt("unidadeensino.numeroPublicacaoCredenciamentoRegistradora"));
	        }
	        if (!obj.getUtilizarMantenedoraUnidadeEnsino()) {
	            obj.setMantenedoraRegistradora(dadosSQL.getString("unidadeensino.mantenedoraRegistradora"));
	            obj.setCnpjMantenedoraRegistradora(dadosSQL.getString("unidadeensino.cnpjMantenedoraRegistradora"));
	            obj.setCepMantenedoraRegistradora(dadosSQL.getString("unidadeensino.cepMantenedoraRegistradora"));
	            obj.setEnderecoMantenedoraRegistradora(dadosSQL.getString("unidadeensino.enderecoMantenedoraRegistradora"));
	            obj.setNumeroMantenedoraRegistradora(dadosSQL.getString("unidadeensino.numeroMantenedoraRegistradora"));
	            obj.getCidadeMantenedoraRegistradora().setCodigo(dadosSQL.getInt("unidadeensino.cidadeMantenedoraRegistradora"));
	            obj.getCidadeMantenedoraRegistradora().setNome(dadosSQL.getString("cidadeMantenedoraRegistradora.nome"));
	            obj.getCidadeMantenedoraRegistradora().setCodigoIBGE(dadosSQL.getString("cidadeMantenedoraRegistradora.codigoIBGE"));
	            obj.getCidadeMantenedoraRegistradora().getEstado().setSigla(dadosSQL.getString("estadoMantenedoraRegistradora.sigla"));
	            obj.setComplementoMantenedoraRegistradora(dadosSQL.getString("unidadeensino.complementoMantenedoraRegistradora"));
	            obj.setBairroMantenedoraRegistradora(dadosSQL.getString("unidadeensino.bairroMantenedoraRegistradora"));
	        }
	    }
	    obj.setUtilizarEnderecoUnidadeEnsinoMantenedora(dadosSQL.getBoolean("unidadeensino.utilizarEnderecoUnidadeEnsinoMantenedora"));
	    if (!obj.getUtilizarEnderecoUnidadeEnsinoMantenedora()) {
	        obj.setCepMantenedora(dadosSQL.getString("unidadeensino.cepMantenedora"));
	        obj.setEnderecoMantenedora(dadosSQL.getString("unidadeensino.enderecoMantenedora"));
	        obj.setNumeroMantenedora(dadosSQL.getString("unidadeensino.numeroMantenedora"));
	        obj.getCidadeMantenedora().setCodigo(dadosSQL.getInt("unidadeensino.cidadeMantenedora"));
	        obj.getCidadeMantenedora().setNome(dadosSQL.getString("cidadeMantenedora.nome"));
	        obj.getCidadeMantenedora().setCodigoIBGE(dadosSQL.getString("cidadeMantenedora.codigoIBGE"));
	        obj.getCidadeMantenedora().getEstado().setSigla(dadosSQL.getString("estadoMantenedora.sigla"));
	        obj.setComplementoMantenedora(dadosSQL.getString("unidadeensino.complementoMantenedora"));
	        obj.setBairroMantenedora(dadosSQL.getString("unidadeensino.bairroMantenedora"));
	    }
	    obj.setNumeroRecredenciamento(dadosSQL.getString("unidadeensino.numeroRecredenciamento"));
	    if (dadosSQL.getDate("unidadeensino.dataRecredenciamento") != null) {
	        obj.setDataRecredenciamento(dadosSQL.getDate("unidadeensino.dataRecredenciamento"));
	    }
	    if (dadosSQL.getDate("unidadeensino.dataPublicacaoRecredenciamento") != null) {
	        obj.setDataPublicacaoRecredenciamento(dadosSQL.getDate("unidadeensino.dataPublicacaoRecredenciamento"));
	    }
	    obj.setVeiculoPublicacaoRecredenciamento(dadosSQL.getString("unidadeensino.veiculoPublicacaoRecredenciamento"));
	    obj.setSecaoPublicacaoRecredenciamento(dadosSQL.getInt("unidadeensino.secaoPublicacaoRecredenciamento"));
	    obj.setPaginaPublicacaoRecredenciamento(dadosSQL.getInt("unidadeensino.paginaPublicacaoRecredenciamento"));
	    obj.setNumeroDOURecredenciamento(dadosSQL.getInt("unidadeensino.numeroDOURecredenciamento"));
	    
	    // --- CORREÇÃO DE SEGURANÇA PARA ENUMS ---
	    obj.setTipoAutorizacaoRecredenciamento(safeEnumValueOf(dadosSQL.getString("unidadeensino.tipoAutorizacaoRecredenciamento")));
	    // ----------------------------------------

	    obj.setNumeroRenovacaoRecredenciamento(dadosSQL.getString("unidadeensino.numeroRenovacaoRecredenciamento"));
	    if (dadosSQL.getDate("unidadeensino.dataRenovacaoRecredenciamento") != null) {
	        obj.setDataRenovacaoRecredenciamento(dadosSQL.getDate("unidadeensino.dataRenovacaoRecredenciamento"));
	    }
	    if (dadosSQL.getDate("unidadeensino.dataPublicacaoRenovacaoRecredenciamento") != null) {
	        obj.setDataPublicacaoRenovacaoRecredenciamento(dadosSQL.getDate("unidadeensino.dataPublicacaoRenovacaoRecredenciamento"));
	    }
	    obj.setVeiculoPublicacaoRenovacaoRecredenciamento(dadosSQL.getString("unidadeensino.veiculoPublicacaoRenovacaoRecredenciamento"));
	    obj.setSecaoPublicacaoRenovacaoRecredenciamento(dadosSQL.getInt("unidadeensino.secaoPublicacaoRenovacaoRecredenciamento"));
	    obj.setPaginaPublicacaoRenovacaoRecredenciamento(dadosSQL.getInt("unidadeensino.paginaPublicacaoRenovacaoRecredenciamento"));
	    obj.setNumeroDOURenovacaoRecredenciamento(dadosSQL.getInt("unidadeensino.numeroDOURenovacaoRecredenciamento"));
	    
	    // --- CORREÇÃO DE SEGURANÇA PARA ENUMS ---
	    obj.setTipoAutorizacaoRenovacaoRecredenciamento(safeEnumValueOf(dadosSQL.getString("unidadeensino.tipoAutorizacaoRenovacaoRecredenciamento")));
	    obj.setTipoAutorizacaoCredenciamentoRegistradora(safeEnumValueOf(dadosSQL.getString("unidadeensino.tipoAutorizacaoCredenciamentoRegistradora")));
	    // ----------------------------------------

	    obj.setNumeroCredenciamentoEAD(dadosSQL.getString("unidadeensino.numeroCredenciamentoEAD"));
	    obj.setCredenciamentoEAD(dadosSQL.getString("unidadeensino.credenciamentoEAD"));
	    if (dadosSQL.getDate("unidadeensino.dataCredenciamentoEAD") != null) {
	        obj.setDataCredenciamentoEAD(dadosSQL.getDate("unidadeensino.dataCredenciamentoEAD"));
	    }
	    if (dadosSQL.getDate("unidadeensino.dataPublicacaoDOEAD") != null) {
	        obj.setDataPublicacaoDOEAD(dadosSQL.getDate("unidadeensino.dataPublicacaoDOEAD"));
	    }
	    obj.setCredenciamentoPortariaEAD(dadosSQL.getString("unidadeensino.credenciamentoPortariaEAD"));
	    obj.setVeiculoPublicacaoCredenciamentoEAD(dadosSQL.getString("unidadeensino.veiculoPublicacaoCredenciamentoEAD"));
	    obj.setSecaoPublicacaoCredenciamentoEAD(dadosSQL.getInt("unidadeensino.secaoPublicacaoCredenciamentoEAD"));
	    obj.setPaginaPublicacaoCredenciamentoEAD(dadosSQL.getInt("unidadeensino.paginaPublicacaoCredenciamentoEAD"));
	    obj.setNumeroDOUCredenciamentoEAD(dadosSQL.getInt("unidadeensino.numeroDOUCredenciamentoEAD"));
	    
	    // --- CORREÇÃO DE SEGURANÇA PARA ENUMS ---
	    obj.setTipoAutorizacaoEAD(safeEnumValueOf(dadosSQL.getString("unidadeensino.tipoAutorizacaoEAD")));
	    // ----------------------------------------

	    obj.setNumeroRecredenciamentoEAD(dadosSQL.getString("unidadeensino.numeroRecredenciamentoEAD"));
	    if (dadosSQL.getDate("unidadeensino.dataRecredenciamentoEAD") != null) {
	        obj.setDataRecredenciamentoEAD(dadosSQL.getDate("unidadeensino.dataRecredenciamentoEAD"));
	    }
	    if (dadosSQL.getDate("unidadeensino.dataPublicacaoRecredenciamentoEAD") != null) {
	        obj.setDataPublicacaoRecredenciamentoEAD(dadosSQL.getDate("unidadeensino.dataPublicacaoRecredenciamentoEAD"));
	    }
	    obj.setVeiculoPublicacaoRecredenciamentoEAD(dadosSQL.getString("unidadeensino.veiculoPublicacaoRecredenciamentoEAD"));
	    obj.setSecaoPublicacaoRecredenciamentoEAD(dadosSQL.getInt("unidadeensino.secaoPublicacaoRecredenciamentoEAD"));
	    obj.setPaginaPublicacaoRecredenciamentoEAD(dadosSQL.getInt("unidadeensino.paginaPublicacaoRecredenciamentoEAD"));
	    obj.setNumeroDOURecredenciamentoEAD(dadosSQL.getInt("unidadeensino.numeroDOURecredenciamentoEAD"));
	    
	    // --- CORREÇÃO DE SEGURANÇA PARA ENUMS ---
	    obj.setTipoAutorizacaoRecredenciamentoEAD(safeEnumValueOf(dadosSQL.getString("unidadeensino.tipoAutorizacaoRecredenciamentoEAD")));
	    // ----------------------------------------

	    obj.setNumeroRenovacaoRecredenciamentoEAD(dadosSQL.getString("unidadeensino.numeroRenovacaoRecredenciamentoEAD"));
	    obj.setDataRenovacaoRecredenciamentoEAD(dadosSQL.getDate("unidadeensino.dataRenovacaoRecredenciamentoEAD"));
	    obj.setDataPublicacaoRenovacaoRecredenciamentoEAD(dadosSQL.getDate("unidadeensino.dataPublicacaoRenovacaoRecredenciamentoEAD"));
	    obj.setVeiculoPublicacaoRenovacaoRecredenciamentoEAD(dadosSQL.getString("unidadeensino.veiculoPublicacaoRenovacaoRecredenciamentoEAD"));
	    obj.setSecaoPublicacaoRenovacaoRecredenciamentoEAD(dadosSQL.getInt("unidadeensino.secaoPublicacaoRenovacaoRecredenciamentoEAD"));
	    obj.setPaginaPublicacaoRenovacaoRecredenciamentoEAD(dadosSQL.getInt("unidadeensino.paginaPublicacaoRenovacaoRecredenciamentoEAD"));
	    obj.setNumeroDOURenovacaoRecredenciamentoEAD(dadosSQL.getInt("unidadeensino.numeroDOURenovacaoRecredenciamentoEAD"));
	    
	    // --- CORREÇÃO DE SEGURANÇA PARA ENUMS ---
	    obj.setTipoAutorizacaoRenovacaoRecredenciamentoEAD(safeEnumValueOf(dadosSQL.getString("unidadeensino.tipoAutorizacaoRenovacaoRecredenciamentoEAD")));
	    // ----------------------------------------

	    obj.setCredenciamentoEmTramitacao(dadosSQL.getBoolean("unidadeensino.credenciamentoEmTramitacao"));
	    if (obj.getCredenciamentoEmTramitacao()) {
	        obj.setNumeroProcessoCredenciamento(dadosSQL.getString("unidadeensino.numeroProcessoCredenciamento"));
	        obj.setTipoProcessoCredenciamento(dadosSQL.getString("unidadeensino.tipoProcessoCredenciamento"));
	        obj.setDataCadastroCredenciamento(dadosSQL.getDate("unidadeensino.dataCadastroCredenciamento"));
	        obj.setDataProtocoloCredenciamento(dadosSQL.getDate("unidadeensino.dataProtocoloCredenciamento"));
	    }
	    obj.setRecredenciamentoEmTramitacao(dadosSQL.getBoolean("unidadeensino.recredenciamentoEmTramitacao"));
	    if (obj.getRecredenciamentoEmTramitacao()) {
	        obj.setNumeroProcessoRecredenciamento(dadosSQL.getString("unidadeensino.numeroProcessoRecredenciamento"));
	        obj.setTipoProcessoRecredenciamento(dadosSQL.getString("unidadeensino.tipoProcessoRecredenciamento"));
	        obj.setDataCadastroRecredenciamento(dadosSQL.getDate("unidadeensino.dataCadastroRecredenciamento"));
	        obj.setDataProtocoloRecredenciamento(dadosSQL.getDate("unidadeensino.dataProtocoloRecredenciamento"));
	    }
	    obj.setRenovacaoRecredenciamentoEmTramitacao(dadosSQL.getBoolean("unidadeensino.renovacaoRecredenciamentoEmTramitacao"));
	    if (obj.getRenovacaoRecredenciamentoEmTramitacao()) {
	        obj.setNumeroProcessoRenovacaoRecredenciamento(dadosSQL.getString("unidadeensino.numeroProcessoRenovacaoRecredenciamento"));
	        obj.setTipoProcessoRenovacaoRecredenciamento(dadosSQL.getString("unidadeensino.tipoProcessoRenovacaoRecredenciamento"));
	        obj.setDataCadastroRenovacaoRecredenciamento(dadosSQL.getDate("unidadeensino.dataCadastroRenovacaoRecredenciamento"));
	        obj.setDataProtocoloRenovacaoRecredenciamento(dadosSQL.getDate("unidadeensino.dataProtocoloRenovacaoRecredenciamento"));
	    }
	    obj.setCredenciamentoEadEmTramitacao(dadosSQL.getBoolean("unidadeensino.credenciamentoEadEmTramitacao"));
	    if (obj.getCredenciamentoEadEmTramitacao()) {
	        obj.setNumeroProcessoCredenciamentoEad(dadosSQL.getString("unidadeensino.numeroProcessoCredenciamentoEad"));
	        obj.setTipoProcessoCredenciamentoEad(dadosSQL.getString("unidadeensino.tipoProcessoCredenciamentoEad"));
	        obj.setDataCadastroCredenciamentoEad(dadosSQL.getDate("unidadeensino.dataCadastroCredenciamentoEad"));
	        obj.setDataProtocoloCredenciamentoEad(dadosSQL.getDate("unidadeensino.dataProtocoloCredenciamentoEad"));
	    }
	    obj.setRecredenciamentoEmTramitacaoEad(dadosSQL.getBoolean("unidadeensino.recredenciamentoEmTramitacaoEad"));
	    if (obj.getRecredenciamentoEmTramitacaoEad()) {
	        obj.setNumeroProcessoRecredenciamentoEad(dadosSQL.getString("unidadeensino.numeroProcessoRecredenciamentoEad"));
	        obj.setTipoProcessoRecredenciamentoEad(dadosSQL.getString("unidadeensino.tipoProcessoRecredenciamentoEad"));
	        obj.setDataCadastroRecredenciamentoEad(dadosSQL.getDate("unidadeensino.dataCadastroRecredenciamentoEad"));
	        obj.setDataProtocoloRecredenciamentoEad(dadosSQL.getDate("unidadeensino.dataProtocoloRecredenciamentoEad"));
	    }
	    obj.setRenovacaoRecredenciamentoEmTramitacaoEad(dadosSQL.getBoolean("unidadeensino.renovacaoRecredenciamentoEmTramitacaoEad"));
	    if (obj.getRenovacaoRecredenciamentoEmTramitacaoEad()) {
	        obj.setNumeroProcessoRenovacaoRecredenciamentoEad(dadosSQL.getString("unidadeensino.numeroProcessoRenovacaoRecredenciamentoEad"));
	        obj.setTipoProcessoRenovacaoRecredenciamentoEad(dadosSQL.getString("unidadeensino.tipoProcessoRenovacaoRecredenciamentoEad"));
	        obj.setDataCadastroRenovacaoRecredenciamentoEad(dadosSQL.getDate("unidadeensino.dataCadastroRenovacaoRecredenciamentoEad"));
	        obj.setDataProtocoloRenovacaoRecredenciamentoEad(dadosSQL.getDate("unidadeensino.dataProtocoloRenovacaoRecredenciamentoEad"));
	    }
	    if (!obj.getUtilizarCredenciamentoUnidadeEnsino()) {
	        obj.setCredenciamentoRegistradoraEmTramitacao(dadosSQL.getBoolean("unidadeensino.credenciamentoRegistradoraEmTramitacao"));
	        if (obj.getCredenciamentoRegistradoraEmTramitacao()) {
	            obj.setNumeroProcessoCredenciamentoRegistradora(dadosSQL.getString("unidadeensino.numeroProcessoCredenciamentoRegistradora"));
	            obj.setTipoProcessoCredenciamentoRegistradora(dadosSQL.getString("unidadeensino.tipoProcessoCredenciamentoRegistradora"));
	            obj.setDataCadastroCredenciamentoRegistradora(dadosSQL.getDate("unidadeensino.dataCadastroCredenciamentoRegistradora"));
	            obj.setDataProtocoloCredenciamentoRegistradora(dadosSQL.getDate("unidadeensino.dataProtocoloCredenciamentoRegistradora"));
	        }
	    }
	    obj.setNumeroCredenciamento(dadosSQL.getString("unidadeensino.numeroCredenciamento"));
	    obj.setNumeroVagaOfertada(dadosSQL.getInt("unidadeensino.numeroVagaOfertada"));
	    obj.setNivelMontarDados(NivelMontarDados.TODOS);
	    if (isAplicacao) {
	        return;
	    }
	    UnidadeEnsinoCursoVO unidadeEnsinoCursoVO = null;
	    do {
	        unidadeEnsinoCursoVO = new UnidadeEnsinoCursoVO();
	        unidadeEnsinoCursoVO.setCodigo((dadosSQL.getInt("unidadeEnsinoCurso.codigo")));
	        unidadeEnsinoCursoVO.getCurso().setNome(dadosSQL.getString("curso.nome"));
	        unidadeEnsinoCursoVO.getCurso().setNivelEducacional(dadosSQL.getString("curso.nivelEducacional"));
	        unidadeEnsinoCursoVO.setSituacaoCurso(dadosSQL.getString("unidadeEnsinoCurso.situacaoCurso"));
	        unidadeEnsinoCursoVO.setMantenedora(dadosSQL.getString("unidadeEnsinoCurso.mantenedora"));
	        unidadeEnsinoCursoVO.setMantida(dadosSQL.getString("unidadeEnsinoCurso.mantida"));
	        unidadeEnsinoCursoVO.getTurno().setNome(dadosSQL.getString("turno.nome"));
	        unidadeEnsinoCursoVO.getTurno().setCodigo((dadosSQL.getInt("turno.codigo")));
	        unidadeEnsinoCursoVO.getCurso().setCodigo((dadosSQL.getInt("curso.codigo")));
	        unidadeEnsinoCursoVO.setNrVagasPeriodoLetivo((dadosSQL.getInt("unidadeEnsinoCurso.nrVagasPeriodoLetivo")));
	        // unidadeEnsinoCursoVO.getPlanoFinanceiroCurso().setCodigo((dadosSQL.getInt("planoFinanceiroCurso.codigo")));
	        unidadeEnsinoCursoVO.setUnidadeEnsino((dadosSQL.getInt("codigo")));
	        unidadeEnsinoCursoVO.getCoordenadorTCC().setCodigo((dadosSQL.getInt("unidadeEnsinoCurso.coordenadorTCC")));
	        unidadeEnsinoCursoVO.getCoordenadorTCC().getPessoa().setNome(dadosSQL.getString("unidadeEnsinoCurso.nomeCoordenadorTCC"));
	        unidadeEnsinoCursoVO.setValorMensalidade(dadosSQL.getDouble("unidadeEnsinoCurso.valorMensalidade"));
	        unidadeEnsinoCursoVO.setCodigoItemListaServico(dadosSQL.getString("unidadeEnsinoCurso.codigoItemListaServico"));
	        unidadeEnsinoCursoVO.setCodigoInep(dadosSQL.getInt("unidadeEnsinoCurso.codigoInep"));
	        unidadeEnsinoCursoVO.setCodigoCursoUnidadeEnsinoGinfes(dadosSQL.getInt("unidadeEnsinoCurso.codigoCursoUnidadeEnsinoGinfes"));
	        if (unidadeEnsinoCursoVO.getCodigo() != 0) {
	            obj.getUnidadeEnsinoCursoVOs().add(unidadeEnsinoCursoVO);
	        }
	    } while (dadosSQL.next());
	}

	// --------------------------------------------------------------------------------------
	// HELPER METHOD TO SAFELY CONVERT ENUMS (ADD THIS TO YOUR CLASS UnidadeEnsino.java)
	// --------------------------------------------------------------------------------------
	private TipoAutorizacaoCursoEnum safeEnumValueOf(String value) {
	    if (value == null || value.trim().isEmpty()) {
	        return null;
	    }
	    try {
	        // Trims whitespace and handles basic matching to prevent crashes
	        return TipoAutorizacaoCursoEnum.valueOf(value.trim()); 
	    } catch (IllegalArgumentException e) {
	        // Optionally log this error so you know there's bad data, but don't crash
	        System.err.println("WARNING: Unknown enum value in database: " + value);
	        return null; 
	    }
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>UnidadeEnsinoVO</code>.
	 * 
	 * @return O objeto da classe <code>UnidadeEnsinoVO</code> com os dados devidamente montados.
	 */
	public UnidadeEnsinoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		// // System.out.println(">> Montar dados(UnidadeEnsino) - " + new Date());
		UnidadeEnsinoVO obj = new UnidadeEnsinoVO();
		obj.setCodigo((dadosSQL.getInt("codigo")));
		obj.setNome(dadosSQL.getString("nome"));
		obj.setNomeExpedicaoDiploma(dadosSQL.getString("nomeExpedicaoDiploma"));
		obj.setAbreviatura(dadosSQL.getString("abreviatura"));
		obj.setCNPJ(dadosSQL.getString("CNPJ"));
		obj.setMatriz(dadosSQL.getBoolean("matriz"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
			return obj;
		}
		obj.setCodigo((dadosSQL.getInt("codigo")));
		obj.setNome(dadosSQL.getString("nome"));
		obj.setNomeExpedicaoDiploma(dadosSQL.getString("nomeExpedicaoDiploma"));
		obj.getCidade().setCodigo((dadosSQL.getInt("cidade")));
		obj.setInscEstadual(dadosSQL.getString("inscEstadual"));
		obj.setInscMunicipal(dadosSQL.getString("inscMunicipal"));
		obj.getConfiguracoes().setCodigo(dadosSQL.getInt("configuracoes"));
		obj.setCodigoIES(dadosSQL.getInt("codigoies"));
		obj.setCodigoIESMantenedora(dadosSQL.getInt("codigoIESMantenedora"));
		obj.setCredenciamentoPortaria(dadosSQL.getString("credenciamentoportaria"));
		obj.setDataPublicacaoDO(dadosSQL.getDate("datapublicacaodo"));
		obj.setMantenedora(dadosSQL.getString("mantenedora"));
		obj.setRazaoSocial(dadosSQL.getString("razaoSocial"));
		obj.setCaminhoBaseLogo(dadosSQL.getString("caminhoBaseLogo"));
		obj.setNomeArquivoLogo(dadosSQL.getString("nomeArquivoLogo"));
		obj.setCaminhoBaseLogoIndex(dadosSQL.getString("caminhoBaseLogoIndex"));
		obj.setNomeArquivoLogoIndex(dadosSQL.getString("nomeArquivoLogoIndex"));
		obj.setCaminhoBaseLogoRelatorio(dadosSQL.getString("caminhoBaseLogoRelatorio"));
		obj.setNomeArquivoLogoRelatorio(dadosSQL.getString("nomeArquivoLogoRelatorio"));
		obj.setNomeArquivoLogoPaginaInicial(dadosSQL.getString("nomeArquivoLogoPaginaInicial"));
		obj.setNomeArquivoLogoPaginaInicial(dadosSQL.getString("caminhoBaseLogoPaginaInicial"));
		obj.setCaminhoBaseLogoEmailCima(dadosSQL.getString("caminhoBaseLogoEmailCima"));
		obj.setNomeArquivoLogoEmailCima(dadosSQL.getString("nomeArquivoLogoEmailCima"));
		obj.setCaminhoBaseLogoEmailBaixo(dadosSQL.getString("caminhoBaseLogoEmailBaixo"));
		obj.setNomeArquivoLogoEmailBaixo(dadosSQL.getString("nomeArquivoLogoEmailBaixo"));
		obj.setCaminhoBaseLogoMunicipio(dadosSQL.getString("caminhoBaseLogoMunicipio"));
		obj.setNomeArquivoLogoMunicipio(dadosSQL.getString("nomeArquivoLogoMunicipio"));
		obj.setCodigoIntegracaoContabil(dadosSQL.getString("codigoIntegracaoContabil"));
		obj.setPermitirVisualizacaoLogin(dadosSQL.getBoolean("permitirVisualizacaoLogin"));
		obj.setDesativada(dadosSQL.getBoolean("desativada"));
		obj.setDependenciaAdministrativa(dadosSQL.getString("dependenciaAdministrativa"));
		obj.setLocalizacaoZonaEscola(dadosSQL.getString("localizacaoZonaEscola"));
		obj.setLocalizacaoDiferenciadaEscola(dadosSQL.getString("localizacaoDiferenciadaEscola"));
		obj.setUnidadeVinculadaEscolaEducacaoBasica(dadosSQL.getString("unidadeVinculadaEscolaEducacaoBasica"));
		obj.setCodigoEscolaSede(dadosSQL.getInt("codigoEscolaSede"));
		obj.setForneceAguaPotavelConsumoHumano(dadosSQL.getBoolean("forneceAguaPotavelConsumoHumano"));
		obj.setCategoriaEscolaPrivada(dadosSQL.getString("categoriaEscolaPrivada"));
		obj.setConveniadaPoderPublico(dadosSQL.getString("conveniadaPoderPublico"));
		obj.setLocalFuncionamentoDaEscola(dadosSQL.getString("localFuncionamentoDaEscola"));
		obj.setFormaOcupacaoPredio(dadosSQL.getString("formaOcupacaoPredio"));
		obj.setPredioCompartilhado(dadosSQL.getBoolean("predioCompartilhado"));
		obj.setCodigoEscolaCompartilhada1(dadosSQL.getString("codigoEscolaCompartilhada1"));
		obj.setCodigoEscolaCompartilhada2(dadosSQL.getString("codigoEscolaCompartilhada2"));
		obj.setCodigoEscolaCompartilhada3(dadosSQL.getString("codigoEscolaCompartilhada3"));
		obj.setCodigoEscolaCompartilhada4(dadosSQL.getString("codigoEscolaCompartilhada4"));
		obj.setCodigoEscolaCompartilhada5(dadosSQL.getString("codigoEscolaCompartilhada5"));
		obj.setCodigoEscolaCompartilhada6(dadosSQL.getString("codigoEscolaCompartilhada6"));
		obj.setAguaConsumida(dadosSQL.getString("aguaConsumida"));
		obj.setAbastecimentoAgua(dadosSQL.getString("abastecimentoAgua"));
		obj.setAbastecimentoEnergia(dadosSQL.getString("abastecimentoEnergia"));
		obj.setEsgotoSanitario(dadosSQL.getString("esgotoSanitario"));
		obj.setDestinoLixo(dadosSQL.getString("destinoLixo"));
		obj.setTratamentoLixo(dadosSQL.getString("tratamentoLixo"));
		obj.setBanheiroExclusivoFuncionarios(dadosSQL.getBoolean("banheiroExclusivoFuncionarios"));
		obj.setPiscina(dadosSQL.getBoolean("piscina"));
		obj.setSalaRepousoAluno(dadosSQL.getBoolean("salaRepousoAluno"));
		obj.setSalaArtes(dadosSQL.getBoolean("salaArtes"));
		obj.setSalaMusica(dadosSQL.getBoolean("salaMusica"));
		obj.setSalaDanca(dadosSQL.getBoolean("salaDanca"));
		obj.setSalaMultiuso(dadosSQL.getBoolean("salaMultiuso"));
		obj.setTerreirao(dadosSQL.getBoolean("terreirao"));
		obj.setViveiroAnimais(dadosSQL.getBoolean("viveiroAnimais"));
		obj.setCorrimaoGuardaCorpos(dadosSQL.getBoolean("corrimaoGuardaCorpos"));
		obj.setElevador(dadosSQL.getBoolean("elevador"));
		obj.setPisosTateis(dadosSQL.getBoolean("pisosTateis"));
		obj.setPortasVaoLivreMinimoOitentaCentimetros(dadosSQL.getBoolean("portasVaoLivreMinimoOitentaCentimetros"));
		obj.setRampas(dadosSQL.getBoolean("rampas"));
		obj.setSinalizacaoSonora(dadosSQL.getBoolean("sinalizacaoSonora"));
		obj.setSinalizacaoTatil(dadosSQL.getBoolean("sinalizacaoTatil"));
		obj.setSinalizacaoVisual(dadosSQL.getBoolean("sinalizacaoVisual"));
		obj.setNenhumRecursoAcessibilidade(dadosSQL.getBoolean("nenhumRecursoAcessibilidade"));
		obj.setNumeroSalasAulaUtilizadasEscolaDentroPredioEscolar(dadosSQL.getInt("numeroSalasAulaUtilizadasEscolaDentroPredioEscolar"));
		obj.setAntenaParabolica(dadosSQL.getBoolean("antenaParabolica"));
		obj.setComputadores(dadosSQL.getBoolean("computadores"));
		obj.setCopiadora(dadosSQL.getBoolean("copiadora"));
		obj.setImpressora(dadosSQL.getBoolean("impressora"));
		obj.setImpressoraMultifuncional(dadosSQL.getBoolean("impressoraMultifuncional"));
		obj.setScanner(dadosSQL.getBoolean("scanner"));
		obj.setAcessoInternetUsoAdiministrativo(dadosSQL.getBoolean("acessoInternetUsoAdiministrativo"));
		obj.setAcessoInternetUsoProcessoEnsinoAprendizagem(dadosSQL.getBoolean("acessoInternetUsoProcessoEnsinoAprendizagem"));
		obj.setAcessoInternetUsoAlunos(dadosSQL.getBoolean("acessoInternetUsoAlunos"));
		obj.setAcessoInternetComunidade(dadosSQL.getBoolean("acessoInternetComunidade"));
		obj.setNaoPossuiAcessoInternet(dadosSQL.getBoolean("naoPossuiAcessoInternet"));
		obj.setAcessoInternetComputadoresEscola(dadosSQL.getBoolean("acessoInternetComputadoresEscola"));
		obj.setAcessoInternetDispositivosPessoais(dadosSQL.getBoolean("acessoInternetDispositivosPessoais"));
		obj.setRedeLocalCabo(dadosSQL.getBoolean("redeLocalCabo"));
		obj.setRedeLocalWireless(dadosSQL.getBoolean("redeLocalWireless"));
		obj.setNaoExisteRedeLocal(dadosSQL.getBoolean("naoExisteRedeLocal"));
		obj.setAlimentacaoEscolarAlunos(dadosSQL.getBoolean("alimentacaoEscolarAlunos"));
		obj.setEducacaoEscolarIndigena(dadosSQL.getBoolean("educacaoEscolarIndigena"));
		obj.setLinguaIndigena(dadosSQL.getBoolean("linguaIndigena"));
		obj.setLinguaPortuguesa(dadosSQL.getBoolean("linguaPortuguesa"));
		obj.setCodigoLinguaIndigena1(dadosSQL.getInt("codigoLinguaIndigena1"));
		obj.setCodigoLinguaIndigena2(dadosSQL.getInt("codigoLinguaIndigena2"));
		obj.setCodigoLinguaIndigena3(dadosSQL.getInt("codigoLinguaIndigena3"));
		obj.setSalaDiretoria(dadosSQL.getBoolean("salaDiretoria"));
		obj.setSalaProfessores(dadosSQL.getBoolean("salaProfessores"));
		obj.setSalaSecretaria(dadosSQL.getBoolean("salaSecretaria"));
		obj.setLaboratorioInformatica(dadosSQL.getBoolean("laboratorioInformatica"));
		obj.setLaboratorioCiencias(dadosSQL.getBoolean("laboratorioCiencias"));
		obj.setRecursosMultifuncionais(dadosSQL.getBoolean("recursosMultifuncionais"));
		obj.setQuadraEsportesCoberta(dadosSQL.getBoolean("quadraEsportesCoberta"));
		obj.setQuadraEsportesDescoberta(dadosSQL.getBoolean("quadraEsportesDescoberta"));
		obj.setCozinha(dadosSQL.getBoolean("cozinha"));
		obj.setBiblioteca(dadosSQL.getBoolean("biblioteca"));
		obj.setSalaLeitura(dadosSQL.getBoolean("salaLeitura"));
		obj.setParqueInfantil(dadosSQL.getBoolean("parqueInfantil"));
		obj.setBercario(dadosSQL.getBoolean("bercario"));
		obj.setBanheiroForaPredio(dadosSQL.getBoolean("banheiroForaPredio"));
		obj.setBanheiroDentroPredio(dadosSQL.getBoolean("banheiroDentroPredio"));
		obj.setBanheiroEducacaoInfantil(dadosSQL.getBoolean("banheiroEducacaoInfantil"));
		obj.setBanheiroDeficiencia(dadosSQL.getBoolean("banheiroDeficiencia"));
		obj.setViasDeficiencia(dadosSQL.getBoolean("viasDeficiencia"));
		obj.setBanheiroChuveiro(dadosSQL.getBoolean("banheiroChuveiro"));
		obj.setRefeitorio(dadosSQL.getBoolean("refeitorio"));
		obj.setDespensa(dadosSQL.getBoolean("despensa"));
		obj.setAlmoxarifado(dadosSQL.getBoolean("almoxarifado"));
		obj.setAuditorio(dadosSQL.getBoolean("auditorio"));
		obj.setPatioCoberto(dadosSQL.getBoolean("patioCoberto"));
		obj.setPatioDescoberto(dadosSQL.getBoolean("patioDescoberto"));
		obj.setAlojamentoAluno(dadosSQL.getBoolean("alojamentoAluno"));
		obj.setAlojamentoProfessor(dadosSQL.getBoolean("alojamentoProfessor"));
		obj.setAreaVerde(dadosSQL.getBoolean("areaVerde"));
		obj.setLavanderia(dadosSQL.getBoolean("lavanderia"));
		obj.setNenhumaDependencia(dadosSQL.getBoolean("nenhumaDependencia"));
		obj.setNumeroSalasAulaExistente(dadosSQL.getString("numeroSalasAulaExistente"));
		obj.setNumeroSalasDentroForaPredio(dadosSQL.getString("numeroSalasDentroForaPredio"));
		obj.setCodigoOrgaoRegionalEnsino(dadosSQL.getString("codigoOrgaoRegionalEnsino"));
		obj.setCodigoDistritoCenso(dadosSQL.getString("codigoDistritoCenso"));
		obj.setApresentarHomePreInscricao(dadosSQL.getBoolean("apresentarHomePreInscricao"));
		obj.setLeiCriacao1(dadosSQL.getString("leiCriacao1"));
		obj.setLeiCriacao2(dadosSQL.getString("leiCriacao2"));
		obj.setCnpjMantenedora(dadosSQL.getString("cnpjmantenedora"));
		obj.setUnidadeCertificadora(dadosSQL.getString("unidadecertificadora"));
		obj.setCnpjUnidadeCertificadora(dadosSQL.getString("cnpjunidadecertificadora"));
		obj.setCodigoIESUnidadeCertificadora(dadosSQL.getInt("codigoiesunidadecertificadora"));
		obj.getConfiguracaoMobileVO().setCodigo(dadosSQL.getInt("configuracaomobile"));

		montarDadosCidade(obj, nivelMontarDados, usuario);
		obj.setCodigoTributacaoMunicipio(dadosSQL.getString("codigoTributacaoMunicipio"));
//		obj.setConfiguracaoNotaFiscalVO(getFacadeFactory().getConfiguracaoNotaFiscalFacade().consultarPorUnidadeEnsino(obj.getCodigo(), false, nivelMontarDados, usuario));
		obj.setOperadorResponsavel(Uteis.montarDadosVO(dadosSQL.getInt("operadorResponsavel"), FuncionarioVO.class, p->getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimariaUnica(p, false, nivelMontarDados, usuario)));

		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			return obj;
		}
		obj.setEndereco(dadosSQL.getString("endereco"));
		obj.setCaixaPostal(dadosSQL.getString("caixaPostal"));
		obj.setSetor(dadosSQL.getString("setor"));
		obj.setNumero(dadosSQL.getString("numero"));
		obj.setComplemento(dadosSQL.getString("complemento"));
		obj.setCEP(dadosSQL.getString("CEP"));
		obj.setTipoEmpresa(dadosSQL.getString("tipoEmpresa"));
		obj.setRG(dadosSQL.getString("RG"));
		obj.setCPF(dadosSQL.getString("CPF"));
		obj.setTelComercial1(dadosSQL.getString("telComercial1"));
		obj.setTelComercial2(dadosSQL.getString("telComercial2"));
		obj.setTelComercial3(dadosSQL.getString("telComercial3"));
		obj.setEmail(dadosSQL.getString("email"));
		obj.setSite(dadosSQL.getString("site"));
		obj.setFax(dadosSQL.getString("fax"));
		obj.setAbreviatura(dadosSQL.getString("abreviatura"));
		obj.setNovoObj(Boolean.FALSE);
		obj.setAno(dadosSQL.getString("ano"));
		obj.getDiretorGeral().setCodigo(dadosSQL.getInt("diretorGeral"));
		obj.getResponsavelCobrancaUnidade().setCodigo(dadosSQL.getInt("responsavelCobrancaUnidade"));
		//obj.getContaCorrentePadraoVO().setCodigo(dadosSQL.getInt("contaCorrentePadrao"));
		obj.setContaCorrentePadraoMensalidade(dadosSQL.getInt("contaCorrentePadraoMensalidade"));
		obj.setContaCorrentePadraoMatricula(dadosSQL.getInt("contaCorrentePadraoMatricula"));
		obj.setContaCorrentePadraoBiblioteca(dadosSQL.getInt("contaCorrentePadraoBiblioteca"));
		obj.setContaCorrentePadraoDevolucaoCheque(dadosSQL.getInt("contaCorrentePadraoDevolucaoCheque"));
		obj.setContaCorrentePadraoMaterialDidatico(dadosSQL.getInt("contaCorrentePadraoMaterialDidatico"));
		obj.setContaCorrentePadraoNegociacao(dadosSQL.getInt("contaCorrentePadraoNegociacao"));
		obj.setContaCorrentePadraoProcessoSeletivo(dadosSQL.getInt("contaCorrentePadraoProcessoSeletivo"));	
		obj.getResponsavelNotificacaoAlteracaoCronogramaAula().setCodigo(dadosSQL.getInt("responsavelNotificacaoAlteracaoCronogramaAula"));
		obj.getOperadorResponsavel().setCodigo(dadosSQL.getInt("operadorResponsavel"));
		obj.setCredenciamento(dadosSQL.getString("credenciamento"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSLOGIN) {
			return obj;
		}
		montarDadosConfiguracoes(obj, nivelMontarDados, usuario);
		if (obj.getAno() == null) {
			obj.setAno(Uteis.getAnoDataAtual4Digitos());
		}
		obj.setNumeroDocumento(dadosSQL.getInt("numeroDocumento"));
		obj.getCoordenadorTCC().setCodigo(dadosSQL.getInt("coordenadorTCC"));
		obj.setInformacoesAdicionaisEndereco(dadosSQL.getString("informacoesAdicionaisEndereco"));
		
//		obj.setChancelaVO(getFacadeFactory().getChancelaFacade().consultarPorChavePrimaria(dadosSQL.getInt("chancela"), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
		obj.setTipoChancela(dadosSQL.getString("tipochancela"));
		obj.setValorFixoChancela(dadosSQL.getDouble("valorFixoChancela"));
		obj.setPorcentagemChancela(dadosSQL.getDouble("porcentagemChancela"));
		obj.setObservacao(dadosSQL.getString("observacao"));
		obj.setNumeroVagaOfertada(dadosSQL.getInt("numeroVagaOfertada"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
			return obj;
		}
		obj.setUnidadeEnsinoCursoVOs(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarUnidadeEnsinoCursos(obj.getCodigo(), nivelMontarDados, usuario));
		montarDadosCidade(obj, nivelMontarDados, usuario);
		montarDadosDiretorGeral(obj, nivelMontarDados, usuario);
		obj.setMaterialUnidadeEnsinoVOs(getFacadeFactory().getMaterialUnidadeEnsinoFacade().consultarPorUnidadeEnsino(obj.getCodigo(), nivelMontarDados, usuario));
		montarDadosResponsavelCobrancaUnidade(obj, nivelMontarDados, usuario);
		montarDadosResponsavelNotificacaoAlteracaoCronogramaAula(obj, nivelMontarDados, usuario);
//		montarDadosContaCorrente(obj, nivelMontarDados, usuario);
		montarDadosCoordenadorTCC(obj, nivelMontarDados, usuario);
//		obj.setConfiguracaoContabilVO(Uteis.montarDadosVO(dadosSQL.getInt("configuracaoContabil"), ConfiguracaoContabilVO.class, p->getFacadeFactory().getConfiguracaoContabilFacade().consultarPorChavePrimaria(p, false, nivelMontarDados, usuario)));
		obj.setOrientadorPadraoEstagio(Uteis.montarDadosVO(dadosSQL.getInt("orientadorPadraoEstagio"), FuncionarioVO.class, p->getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimariaUnica(p, false, nivelMontarDados, usuario)));

		return obj;
	}

	public  void montarDadosDiretorGeral(UnidadeEnsinoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getDiretorGeral().getCodigo().intValue() == 0) {
			obj.setDiretorGeral(new FuncionarioVO());
			return;
		}
		obj.setDiretorGeral(getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimaria(obj.getDiretorGeral().getCodigo(), obj.getDiretorGeral().getUnidadeEnsino().getCodigo(), false, nivelMontarDados, usuario));
	}

	public  void montarDadosResponsavelCobrancaUnidade(UnidadeEnsinoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getResponsavelCobrancaUnidade().getCodigo().intValue() == 0) {
			obj.setResponsavelCobrancaUnidade(new PessoaVO());
			return;
		}
		obj.setResponsavelCobrancaUnidade(getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(obj.getResponsavelCobrancaUnidade().getCodigo(), false, true, nivelMontarDados, usuario));
	}
	
	
	public  void montarDadosResponsavelNotificacaoAlteracaoCronogramaAula(UnidadeEnsinoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getResponsavelNotificacaoAlteracaoCronogramaAula().getCodigo().intValue() == 0) {
			obj.setResponsavelNotificacaoAlteracaoCronogramaAula(new FuncionarioVO());
			return;
		}
		obj.setResponsavelNotificacaoAlteracaoCronogramaAula(getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimaria(obj.getResponsavelNotificacaoAlteracaoCronogramaAula().getCodigo(), obj.getResponsavelNotificacaoAlteracaoCronogramaAula().getUnidadeEnsino().getCodigo(), false, nivelMontarDados, usuario));
	
	}

//	public static void montarDadosContaCorrente(UnidadeEnsinoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
//		if (obj.getContaCorrentePadraoVO().getCodigo().intValue() == 0) {
//			obj.setContaCorrentePadraoVO(new ContaCorrenteVO());
//			return;
//		}
//		obj.setContaCorrentePadraoVO(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(obj.getContaCorrentePadraoVO().getCodigo(), false, nivelMontarDados, usuario));
//	}

	public  void montarDadosCoordenadorTCC(UnidadeEnsinoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getCoordenadorTCC().getCodigo().intValue() == 0) {
			obj.setCoordenadorTCC(new FuncionarioVO());
			return;
		}
		obj.setCoordenadorTCC(getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimariaUnica(obj.getCoordenadorTCC().getCodigo(), false, nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>CidadeVO</code> relacionado ao objeto <code>UnidadeEnsinoVO</code>. Faz
	 * uso da chave primária da classe <code>CidadeVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public  void montarDadosCidade(UnidadeEnsinoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getCidade().getCodigo().intValue() == 0) {
			obj.setCidade(new CidadeVO());
			return;
		}
		obj.setCidade(getFacadeFactory().getCidadeFacade().consultarPorChavePrimaria(obj.getCidade().getCodigo(), false, usuario));
	}

	public  void montarDadosConfiguracoes(UnidadeEnsinoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getConfiguracoes().getCodigo().intValue() == 0) {
			return;
		}
		obj.setConfiguracoes(getFacadeFactory().getConfiguracoesFacade().consultarPorChavePrimaria(obj.getConfiguracoes().getCodigo(), nivelMontarDados, usuario));
	}

	public void carregarDados(UnidadeEnsinoVO obj, UsuarioVO usuario) throws Exception {
		carregarDados((UnidadeEnsinoVO) obj, NivelMontarDados.TODOS, usuario);
	}

	public void carregarDados(UnidadeEnsinoVO obj, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception {		
		SqlRowSet resultado = null;
		if ((nivelMontarDados.equals(NivelMontarDados.BASICO)) && (obj.getIsNivelMontarDadosNaoInicializado())) {
			resultado = consultaRapidaPorChavePrimariaDadosBasicos(obj.getCodigo(), usuario);
			montarDadosBasico((UnidadeEnsinoVO) obj, resultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
		}
		if ((nivelMontarDados.equals(NivelMontarDados.TODOS)) && (!obj.getIsNivelMontarDadosTodos())) {
			resultado = consultaRapidaPorChavePrimariaDadosCompletos(obj.getCodigo(), usuario);
			montarDadosCompleto((UnidadeEnsinoVO) obj, resultado, false, usuario);
		}
	}

	private SqlRowSet consultaRapidaPorChavePrimariaDadosBasicos(Integer codUnidadeEnsino, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE (UnidadeEnsino.codigo= ").append(codUnidadeEnsino).append(")");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return tabelaResultado;
	}

	private SqlRowSet consultaRapidaPorChavePrimariaDadosCompletos(Integer codUnidadeEnsino, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaCompleta();
		sqlStr.append(" WHERE (UnidadeEnsino.codigo= ").append(codUnidadeEnsino).append(")");
		sqlStr.append(" ORDER BY curso.nome, turno.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return tabelaResultado;
	}

	public List<UnidadeEnsinoVO> consultarPorProcessoSeletivo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "select unidadeEnsino.* from unidadeEnsino  inner join procSeletivoUnidadeEnsino as psue on psue.unidadeEnsino = unidadeEnsino.codigo where psue.procSeletivo = " + valorConsulta.intValue() + "";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<UnidadeEnsinoVO> consultarPorCoordenador(Integer coordenador, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("SELECT DISTINCT unidadeEnsino.codigo, unidadeEnsino.* FROM cursoCoordenador cc");
		sqlStr.append(" INNER JOIN unidadeensinocurso uc on uc.curso = cc.curso ");
		sqlStr.append(" INNER JOIN unidadeEnsino ON unidadeEnsino.codigo = uc.unidadeEnsino and (cc.unidadeensino is null or cc.unidadeensino = unidadeensino.codigo) ");
		sqlStr.append(" INNER JOIN funcionario ON funcionario.codigo = cc.funcionario");
		sqlStr.append(" INNER JOIN pessoa ON funcionario.pessoa = pessoa.codigo");
		sqlStr.append(" WHERE pessoa.codigo = ").append(coordenador);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/**
	 * Operação responsável por localizar um objeto da classe <code>UnidadeEnsinoVO</code> através de sua chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto procurado.
	 */	
	public UnidadeEnsinoVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		return getAplicacaoControle().getUnidadeEnsinoVO(codigoPrm, usuario);
	}

	public UnidadeEnsinoVO consultarPorMatricula(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = new StringBuilder();
		sql.append("select unidadeEnsino.codigo from unidadeEnsino ");
		sql.append(" inner join matricula on matricula.unidadeEnsino = unidadeEnsino.codigo ");
		sql.append(" where matricula.matricula = ? ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), valorConsulta);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return getAplicacaoControle().getUnidadeEnsinoVO(tabelaResultado.getInt("codigo"), usuario);		
	}

	/**
	 * Operação responsável por localizar um objeto da classe <code>EmpresaVO</code> através de sua chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto procurado.
	 */
	public UnidadeEnsinoVO consultarPorChavePrimaria(Integer codigoPrm, String tipoUsuario, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		return getAplicacaoControle().getUnidadeEnsinoVO(codigoPrm, usuario);		
	}

	@Override
	public List<UnidadeEnsinoVO> consultaRapidaPorNomeAutoComplete(String valorConsulta, int limit, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("select distinct codigo, nome ");
		sqlStr.append("from UnidadeEnsino ");
		sqlStr.append("WHERE nome ilike('%");
		sqlStr.append(valorConsulta.toLowerCase());
		sqlStr.append("%') ");
		sqlStr.append(" ORDER BY nome ");
		sqlStr.append(" limit ").append(limit);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		sqlStr = null;
		List<UnidadeEnsinoVO> listaCurso = new ArrayList<UnidadeEnsinoVO>();
		while (tabelaResultado.next()) {
			UnidadeEnsinoVO obj = new UnidadeEnsinoVO();
			obj.setCodigo((tabelaResultado.getInt("codigo")));
			obj.setNome(tabelaResultado.getString("nome"));
			listaCurso.add(obj);
		}
		return listaCurso;
	}

	@Override
	public UnidadeEnsinoVO consultarPorNomeCurso(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sql = "select distinct unidadeensino.* from unidadeensinocurso left join curso on unidadeensinocurso.curso = curso.codigo " + "left join unidadeensino on unidadeensinocurso.unidadeensino = unidadeensino.codigo where curso.nome = '" + valorConsulta + "' ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	public UnidadeEnsinoVO consultarSeExisteUnidade(Integer ValorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		return getAplicacaoControle().getUnidadeEnsinoVO(ValorConsulta, usuario);
//		String sql = "SELECT UnidadeEnsino.* FROM UnidadeEnsino where UnidadeEnsino.codigo =" + ValorConsulta.intValue();
//		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
//		UnidadeEnsinoVO obj = new UnidadeEnsinoVO();
//		if (tabelaResultado.next()) {
//			obj = montarDados(tabelaResultado, nivelMontarDados, usuario);
//		} else {
//			return null;
//		}
//		return (obj);
	}

	public UnidadeEnsinoVO consultarPorContaReceberDadosLogoRelatorio(Integer contaReceber, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(" select unidadeensino.codigo, unidadeensino.caminhobaselogorelatorio, unidadeensino.nomeArquivoLogoRelatorio,  ");
		sb.append(" unidadeensino.caminhobaselogo, unidadeensino.nomeArquivoLogo  ");
		sb.append(" from unidadeensino ");
		sb.append(" inner join contareceber on contareceber.unidadeensinofinanceira = unidadeensino.codigo ");
		sb.append(" where contaReceber.codigo = ").append(contaReceber);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		UnidadeEnsinoVO obj = new UnidadeEnsinoVO();
		if (tabelaResultado.next()) {
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setCaminhoBaseLogoRelatorio(tabelaResultado.getString("caminhobaselogorelatorio"));
			obj.setNomeArquivoLogoRelatorio(tabelaResultado.getString("nomeArquivoLogoRelatorio"));
			obj.setCaminhoBaseLogo(tabelaResultado.getString("caminhobaselogo"));
			obj.setNomeArquivoLogo(tabelaResultado.getString("nomeArquivoLogo"));
		} else {
			return null;
		}
		return (obj);
	}
	
	public UnidadeEnsinoVO consultarPorUnidadeEnsinoDadosLogoRelatorio(Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(" select unidadeensino.codigo, unidadeensino.caminhobaselogorelatorio, unidadeensino.nomeArquivoLogoRelatorio,  ");
		sb.append(" unidadeensino.caminhobaselogo, unidadeensino.nomeArquivoLogo  ");
		sb.append(" from unidadeensino ");
		sb.append(" where codigo = ").append(unidadeEnsino);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		UnidadeEnsinoVO obj = new UnidadeEnsinoVO();
		if (tabelaResultado.next()) {
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setCaminhoBaseLogoRelatorio(tabelaResultado.getString("caminhobaselogorelatorio"));
			obj.setNomeArquivoLogoRelatorio(tabelaResultado.getString("nomeArquivoLogoRelatorio"));
			obj.setCaminhoBaseLogo(tabelaResultado.getString("caminhobaselogo"));
			obj.setNomeArquivoLogo(tabelaResultado.getString("nomeArquivoLogo"));
		} else {
			return null;
		}
		return (obj);
	}

	public UnidadeEnsinoVO consultarSeExisteUnidadeEnsinoCurso(Integer codigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT distinct UnidadeEnsino.*  from  UnidadeEnsino , UnidadeEnsinoCurso WHERE UnidadeEnsinoCurso.curso = " + codigo.intValue() + " and UnidadeEnsinoCurso.unidadeensino= unidadeEnsino.codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		UnidadeEnsinoVO obj = new UnidadeEnsinoVO();
		if (tabelaResultado.next()) {
			obj = montarDados(tabelaResultado, nivelMontarDados, usuario);
		} else {
			return null;
		}
		return (obj);

	}

	public UnidadeEnsinoVO consultarSeExisteUnidadeMatriz(boolean somenteDadosBasicos, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sql = "SELECT codigo, nome, cnpj, razaosocial FROM UnidadeEnsino WHERE matriz = true";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
		UnidadeEnsinoVO obj = new UnidadeEnsinoVO();
		if (tabelaResultado.next()) {
			obj.setCodigo((tabelaResultado.getInt("codigo")));
			obj.setNome((tabelaResultado.getString("nome")));
			obj.setCNPJ((tabelaResultado.getString("cnpj")));
			obj.setRazaoSocial((tabelaResultado.getString("razaosocial")));
			obj.setMatriz(true);
		}
		return (obj);
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações
	 * desta classe.
	 */
	public static String getIdEntidade() {
		return UnidadeEnsino.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste
	 * identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		UnidadeEnsino.idEntidade = idEntidade;
	}

	public Integer consultarCodigoPessoaCoordenadorTCC(Integer unidadeEnsino, Integer curso) throws Exception {
		StringBuilder sqlStr = new StringBuilder("select case when (p.codigo is not null) then (p.codigo) else (p2.codigo) end as coordenador from unidadeensino ue ");
		sqlStr.append("left join unidadeensinocurso uec on uec.unidadeensino = ue.codigo and uec.curso = ").append(curso);
		sqlStr.append("left join funcionario f on uec.coordenadorTCC = f.codigo ");
		sqlStr.append("left join pessoa p on f.pessoa = p.codigo ");
		sqlStr.append("left join funcionario f2 on ue.coordenadorTCC = f2.codigo ");
		sqlStr.append("left join pessoa p2 on f2.pessoa = p2.codigo ");
		sqlStr.append("where ue.codigo = ").append(unidadeEnsino);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("coordenador");
		} else {
			return null;
		}
	}

	public List<UnidadeEnsinoVO> consultarPorProfessor(Integer professor, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("select distinct ue.* from unidadeensino ue ");
		sqlStr.append("inner join turma t on t.unidadeensino = ue.codigo ");
		sqlStr.append("inner join horarioturmaprofessordisciplina htpd on htpd.turma = t.codigo ");
		sqlStr.append("where htpd.professor = ").append(professor);
		sqlStr.append(" Order by ue.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	@Override
	public List<UnidadeEnsinoVO> consultarUnidadeEnsinoProfessorPorCodigoPessoa(Integer pessoa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("select distinct ue.* from unidadeensino ue ");
		sqlStr.append("inner join funcionarioCargo fc on fc.unidadeensino = ue.codigo ");
		sqlStr.append("inner join funcionario fu on fu.codigo = fc.funcionario ");
		sqlStr.append("where fu.pessoa = ").append(pessoa);
		sqlStr.append(" Order by ue.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public void validarDadosUnidadeEnsino(Integer unidadeEnsino) throws Exception {
		if (unidadeEnsino == null || unidadeEnsino == 0) {
			throw new Exception("O campo UNIDADE ENSINO deve ser informado.");
		}
	}

	public void salvarLogoUnidadeEnsino(UnidadeEnsinoVO unidadeEnsinoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		if (unidadeEnsinoVO.getLogoInformada() && !unidadeEnsinoVO.getNomeArquivoLogo().trim().isEmpty()) {
			String caminhoFinal = PastaBaseArquivoEnum.LOGO_UNIDADE_ENSINO.getValue() + File.separator + unidadeEnsinoVO.getCodigo();
			String nomeFinal = "topoLogoVisao" + unidadeEnsinoVO.getNomeArquivoLogo().substring(unidadeEnsinoVO.getNomeArquivoLogo().lastIndexOf("."), unidadeEnsinoVO.getNomeArquivoLogo().length());
			ArquivoHelper.delete(new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + caminhoFinal + File.separator + nomeFinal));
			ArquivoHelper.copiarArquivoDaPastaTempParaPastaFixaComOutroNome(unidadeEnsinoVO.getCaminhoBaseLogo(), caminhoFinal, unidadeEnsinoVO.getNomeArquivoLogo(), nomeFinal, configuracaoGeralSistemaVO,false);
			unidadeEnsinoVO.setCaminhoBaseLogo(caminhoFinal);
			unidadeEnsinoVO.setNomeArquivoLogo(nomeFinal);
			unidadeEnsinoVO.setLogoInformada(false);
		}

		if (unidadeEnsinoVO.getLogoInformadaIndex() && !unidadeEnsinoVO.getNomeArquivoLogoIndex().trim().isEmpty()) {
			String caminhoFinal = PastaBaseArquivoEnum.LOGO_UNIDADE_ENSINO.getValue() + File.separator + unidadeEnsinoVO.getCodigo();
			String nomeFinal = "logo" + unidadeEnsinoVO.getNomeArquivoLogoIndex().substring(unidadeEnsinoVO.getNomeArquivoLogoIndex().lastIndexOf("."), unidadeEnsinoVO.getNomeArquivoLogoIndex().length());
			ArquivoHelper.copiarArquivoDaPastaTempParaPastaFixaComOutroNome(unidadeEnsinoVO.getCaminhoBaseLogoIndex(), caminhoFinal, unidadeEnsinoVO.getNomeArquivoLogoIndex(), nomeFinal, configuracaoGeralSistemaVO,false);
			unidadeEnsinoVO.setCaminhoBaseLogoIndex(caminhoFinal);
			unidadeEnsinoVO.setNomeArquivoLogoIndex(nomeFinal);
			unidadeEnsinoVO.setLogoInformadaIndex(false);
		}
		if (unidadeEnsinoVO.getLogoInformadaRelatorio() && !unidadeEnsinoVO.getNomeArquivoLogoRelatorio().trim().isEmpty()) {
			String caminhoFinal = PastaBaseArquivoEnum.LOGO_UNIDADE_ENSINO.getValue() + File.separator + unidadeEnsinoVO.getCodigo();
			String nomeFinal = "logoRelatorio" + unidadeEnsinoVO.getNomeArquivoLogoRelatorio().substring(unidadeEnsinoVO.getNomeArquivoLogoRelatorio().lastIndexOf("."), unidadeEnsinoVO.getNomeArquivoLogoRelatorio().length());
			ArquivoHelper.copiarArquivoDaPastaTempParaPastaFixaComOutroNome(unidadeEnsinoVO.getCaminhoBaseLogoRelatorio(), caminhoFinal, unidadeEnsinoVO.getNomeArquivoLogoRelatorio(), nomeFinal, configuracaoGeralSistemaVO, false);
			unidadeEnsinoVO.setCaminhoBaseLogoRelatorio(caminhoFinal);
			unidadeEnsinoVO.setNomeArquivoLogoRelatorio(nomeFinal);
			unidadeEnsinoVO.setLogoInformadaRelatorio(false);
		}
		if (unidadeEnsinoVO.getLogoInformadaInicial() && !unidadeEnsinoVO.getNomeArquivoLogoPaginaInicial().trim().isEmpty()) {
			String caminhoFinal = PastaBaseArquivoEnum.LOGO_UNIDADE_ENSINO.getValue() + File.separator + unidadeEnsinoVO.getCodigo();
			String nomeFinal = "logoPaginaInicial" + unidadeEnsinoVO.getNomeArquivoLogoPaginaInicial().substring(unidadeEnsinoVO.getNomeArquivoLogoPaginaInicial().lastIndexOf("."), unidadeEnsinoVO.getNomeArquivoLogoPaginaInicial().length());
			ArquivoHelper.copiarArquivoDaPastaTempParaPastaFixaComOutroNome(unidadeEnsinoVO.getCaminhoBaseLogoPaginaInicial(), caminhoFinal, unidadeEnsinoVO.getNomeArquivoLogoPaginaInicial(), nomeFinal, configuracaoGeralSistemaVO, false);
			unidadeEnsinoVO.setCaminhoBaseLogoPaginaInicial(caminhoFinal);
			unidadeEnsinoVO.setNomeArquivoLogoPaginaInicial(nomeFinal);
			unidadeEnsinoVO.setLogoInformadaInicial(false);
		}
		if (unidadeEnsinoVO.getLogoInformadaMunicipio() && !unidadeEnsinoVO.getNomeArquivoLogoMunicipio().trim().isEmpty()) {
			String caminhoFinal = PastaBaseArquivoEnum.LOGO_UNIDADE_ENSINO.getValue() + File.separator + unidadeEnsinoVO.getCodigo();
			String nomeFinal = "logoMunicipio" + String.valueOf(new Date().getTime()) + unidadeEnsinoVO.getNomeArquivoLogoMunicipio().substring(unidadeEnsinoVO.getNomeArquivoLogoMunicipio().lastIndexOf("."), unidadeEnsinoVO.getNomeArquivoLogoMunicipio().length());
			ArquivoHelper.copiarArquivoDaPastaTempParaPastaFixaComOutroNome(unidadeEnsinoVO.getCaminhoBaseLogoMunicipio(), caminhoFinal, unidadeEnsinoVO.getNomeArquivoLogoMunicipio(), nomeFinal, configuracaoGeralSistemaVO, false);
			unidadeEnsinoVO.setCaminhoBaseLogoMunicipio(caminhoFinal);
			unidadeEnsinoVO.setNomeArquivoLogoMunicipio(nomeFinal);
			unidadeEnsinoVO.setLogoInformadaMunicipio(false);
		}
		if (unidadeEnsinoVO.getLogoInformadaEmailCima() && !unidadeEnsinoVO.getNomeArquivoLogoEmailCima().trim().isEmpty()) {
			String caminhoFinal = PastaBaseArquivoEnum.LOGO_UNIDADE_ENSINO.getValue() + File.separator + unidadeEnsinoVO.getCodigo();
			String nomeFinal = "logoEmailCima" + String.valueOf(new Date().getTime()) + unidadeEnsinoVO.getNomeArquivoLogoEmailCima().substring(unidadeEnsinoVO.getNomeArquivoLogoEmailCima().lastIndexOf("."), unidadeEnsinoVO.getNomeArquivoLogoEmailCima().length());
			ArquivoHelper.copiarArquivoDaPastaTempParaPastaFixaComOutroNome(unidadeEnsinoVO.getCaminhoBaseLogoEmailCima(), caminhoFinal, unidadeEnsinoVO.getNomeArquivoLogoEmailCima(), nomeFinal, configuracaoGeralSistemaVO, false);
			unidadeEnsinoVO.setCaminhoBaseLogoEmailCima(caminhoFinal);
			unidadeEnsinoVO.setNomeArquivoLogoEmailCima(nomeFinal);
			unidadeEnsinoVO.setLogoInformadaEmailCima(false);
		}
		if (unidadeEnsinoVO.getLogoInformadaEmailBaixo() && !unidadeEnsinoVO.getNomeArquivoLogoEmailBaixo().trim().isEmpty()) {
			String caminhoFinal = PastaBaseArquivoEnum.LOGO_UNIDADE_ENSINO.getValue() + File.separator + unidadeEnsinoVO.getCodigo();
			String nomeFinal = "logoEmailBaixo" + String.valueOf(new Date().getTime()) + unidadeEnsinoVO.getNomeArquivoLogoEmailBaixo().substring(unidadeEnsinoVO.getNomeArquivoLogoEmailBaixo().lastIndexOf("."), unidadeEnsinoVO.getNomeArquivoLogoEmailBaixo().length());
			ArquivoHelper.copiarArquivoDaPastaTempParaPastaFixaComOutroNome(unidadeEnsinoVO.getCaminhoBaseLogoEmailBaixo(), caminhoFinal, unidadeEnsinoVO.getNomeArquivoLogoEmailBaixo(), nomeFinal, configuracaoGeralSistemaVO, false);
			unidadeEnsinoVO.setCaminhoBaseLogoEmailBaixo(caminhoFinal);
			unidadeEnsinoVO.setNomeArquivoLogoEmailBaixo(nomeFinal);
			unidadeEnsinoVO.setLogoInformadaEmailBaixo(false);
		}
		
		if (unidadeEnsinoVO.getLogoInformadaAplicativo() && !unidadeEnsinoVO.getNomeArquivoLogoAplicativo().trim().isEmpty()) {
			String caminhoFinal = PastaBaseArquivoEnum.LOGO_UNIDADE_ENSINO.getValue() + File.separator + unidadeEnsinoVO.getCodigo();
			String nomeFinal = "logoAplicativo" + String.valueOf(new Date().getTime()) + unidadeEnsinoVO.getNomeArquivoLogoAplicativo().substring(unidadeEnsinoVO.getNomeArquivoLogoAplicativo().lastIndexOf("."), unidadeEnsinoVO.getNomeArquivoLogoAplicativo().length());
			ArquivoHelper.copiarArquivoDaPastaTempParaPastaFixaComOutroNome(unidadeEnsinoVO.getCaminhoBaseLogoAplicativo(), caminhoFinal, unidadeEnsinoVO.getNomeArquivoLogoAplicativo(), nomeFinal, configuracaoGeralSistemaVO, false);
			unidadeEnsinoVO.setCaminhoBaseLogoAplicativo(caminhoFinal);
			unidadeEnsinoVO.setNomeArquivoLogoAplicativo(nomeFinal);
			unidadeEnsinoVO.setLogoInformadaAplicativo(false);
		}
	}

	@Override
	public void upLoadLogoUnidadeEnsino(FileUploadEvent upload, UnidadeEnsinoVO unidadeEnsinoVO, String tipoLogo, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {
		String nomeIcone = String.valueOf(new Date().getTime()) + upload.getFile().getFileName().substring(upload.getFile().getFileName().lastIndexOf("."), upload.getFile().getFileName().length());
		File arquivoExistente = null;
		try {
			if (tipoLogo.equals("TOPO")) {
				if (unidadeEnsinoVO.getLogoInformada() && unidadeEnsinoVO.getCaminhoBaseLogo().contains(PastaBaseArquivoEnum.LOGO_UNIDADE_ENSINO_TMP.getValue())) {
					arquivoExistente = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator + unidadeEnsinoVO.getCaminhoBaseLogo() + File.separator + unidadeEnsinoVO.getNomeArquivoLogo());
					ArquivoHelper.delete(arquivoExistente);
				}

				ArquivoHelper.salvarArquivoNaPastaTemp(upload, nomeIcone, PastaBaseArquivoEnum.LOGO_UNIDADE_ENSINO_TMP.getValue(), configuracaoGeralSistemaVO, usuarioVO);
				unidadeEnsinoVO.setCaminhoBaseLogo(PastaBaseArquivoEnum.LOGO_UNIDADE_ENSINO_TMP.getValue());
				unidadeEnsinoVO.setNomeArquivoLogo(nomeIcone);
				unidadeEnsinoVO.setLogoInformada(true);
			} else if (tipoLogo.equals("INDEX")) {
				if (unidadeEnsinoVO.getLogoInformadaIndex() && unidadeEnsinoVO.getCaminhoBaseLogoIndex().contains(PastaBaseArquivoEnum.LOGO_UNIDADE_ENSINO_TMP.getValue())) {
					arquivoExistente = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator + unidadeEnsinoVO.getCaminhoBaseLogoIndex() + File.separator + unidadeEnsinoVO.getNomeArquivoLogoIndex());
					ArquivoHelper.delete(arquivoExistente);
				}

				ArquivoHelper.salvarArquivoNaPastaTemp(upload, nomeIcone, PastaBaseArquivoEnum.LOGO_UNIDADE_ENSINO_TMP.getValue(), configuracaoGeralSistemaVO, usuarioVO);
				unidadeEnsinoVO.setCaminhoBaseLogoIndex(PastaBaseArquivoEnum.LOGO_UNIDADE_ENSINO_TMP.getValue());
				unidadeEnsinoVO.setNomeArquivoLogoIndex(nomeIcone);
				unidadeEnsinoVO.setLogoInformadaIndex(true);
			} else if (tipoLogo.equals("RELATORIO")) {
				if (unidadeEnsinoVO.getLogoInformadaRelatorio() && unidadeEnsinoVO.getCaminhoBaseLogoRelatorio().contains(PastaBaseArquivoEnum.LOGO_UNIDADE_ENSINO_TMP.getValue())) {
					arquivoExistente = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator + unidadeEnsinoVO.getCaminhoBaseLogoRelatorio() + File.separator + unidadeEnsinoVO.getNomeArquivoLogoRelatorio());
					ArquivoHelper.delete(arquivoExistente);
				}

				ArquivoHelper.salvarArquivoNaPastaTemp(upload, nomeIcone, PastaBaseArquivoEnum.LOGO_UNIDADE_ENSINO_TMP.getValue(), configuracaoGeralSistemaVO, usuarioVO);
				unidadeEnsinoVO.setCaminhoBaseLogoRelatorio(PastaBaseArquivoEnum.LOGO_UNIDADE_ENSINO_TMP.getValue());
				unidadeEnsinoVO.setNomeArquivoLogoRelatorio(nomeIcone);
				unidadeEnsinoVO.setLogoInformadaRelatorio(true);
			}else if (tipoLogo.equals("INICIAL")) {
				if (unidadeEnsinoVO.getLogoInformadaInicial() && unidadeEnsinoVO.getCaminhoBaseLogoRelatorio().contains(PastaBaseArquivoEnum.LOGO_UNIDADE_ENSINO_TMP.getValue())) {
					arquivoExistente = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator + unidadeEnsinoVO.getCaminhoBaseLogoRelatorio() + File.separator + unidadeEnsinoVO.getNomeArquivoLogoRelatorio());
					ArquivoHelper.delete(arquivoExistente);
				}

				ArquivoHelper.salvarArquivoNaPastaTemp(upload, nomeIcone, PastaBaseArquivoEnum.LOGO_UNIDADE_ENSINO_TMP.getValue(), configuracaoGeralSistemaVO, usuarioVO);
				unidadeEnsinoVO.setCaminhoBaseLogoPaginaInicial(PastaBaseArquivoEnum.LOGO_UNIDADE_ENSINO_TMP.getValue());
				unidadeEnsinoVO.setNomeArquivoLogoPaginaInicial(nomeIcone);
				unidadeEnsinoVO.setLogoInformadaInicial(true);
			}  else if (tipoLogo.equals("EMAIL_CIMA")) {
				if (unidadeEnsinoVO.getLogoInformadaEmailCima() && unidadeEnsinoVO.getCaminhoBaseLogoEmailCima().contains(PastaBaseArquivoEnum.LOGO_UNIDADE_ENSINO_TMP.getValue())) {
					arquivoExistente = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator + unidadeEnsinoVO.getCaminhoBaseLogoEmailCima() + File.separator + unidadeEnsinoVO.getNomeArquivoLogoEmailCima());
					ArquivoHelper.delete(arquivoExistente);
				}

				ArquivoHelper.salvarArquivoNaPastaTemp(upload, nomeIcone, PastaBaseArquivoEnum.LOGO_UNIDADE_ENSINO_TMP.getValue(), configuracaoGeralSistemaVO, usuarioVO);
				unidadeEnsinoVO.setCaminhoBaseLogoEmailCima(PastaBaseArquivoEnum.LOGO_UNIDADE_ENSINO_TMP.getValue());
				unidadeEnsinoVO.setNomeArquivoLogoEmailCima(nomeIcone);
				unidadeEnsinoVO.setLogoInformadaEmailCima(true);
				
			} else if (tipoLogo.equals("EMAIL_BAIXO")) {
				if (unidadeEnsinoVO.getLogoInformadaEmailBaixo() && unidadeEnsinoVO.getCaminhoBaseLogoEmailBaixo().contains(PastaBaseArquivoEnum.LOGO_UNIDADE_ENSINO_TMP.getValue())) {
					arquivoExistente = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator + unidadeEnsinoVO.getCaminhoBaseLogoEmailBaixo() + File.separator + unidadeEnsinoVO.getNomeArquivoLogoEmailBaixo());
					ArquivoHelper.delete(arquivoExistente);
				}

				ArquivoHelper.salvarArquivoNaPastaTemp(upload, nomeIcone, PastaBaseArquivoEnum.LOGO_UNIDADE_ENSINO_TMP.getValue(), configuracaoGeralSistemaVO, usuarioVO);
				unidadeEnsinoVO.setCaminhoBaseLogoEmailBaixo(PastaBaseArquivoEnum.LOGO_UNIDADE_ENSINO_TMP.getValue());
				unidadeEnsinoVO.setNomeArquivoLogoEmailBaixo(nomeIcone);
				unidadeEnsinoVO.setLogoInformadaEmailBaixo(true);
				
			}else if (tipoLogo.equals("MUNICIPIO")) {
					if (unidadeEnsinoVO.getLogoInformadaMunicipio() && unidadeEnsinoVO.getCaminhoBaseLogoMunicipio().contains(PastaBaseArquivoEnum.LOGO_UNIDADE_ENSINO_TMP.getValue())) {
						arquivoExistente = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator + unidadeEnsinoVO.getCaminhoBaseLogoMunicipio() + File.separator + unidadeEnsinoVO.getNomeArquivoLogoMunicipio());
						ArquivoHelper.delete(arquivoExistente);
					}

					ArquivoHelper.salvarArquivoNaPastaTemp(upload, nomeIcone, PastaBaseArquivoEnum.LOGO_UNIDADE_ENSINO_TMP.getValue(), configuracaoGeralSistemaVO, usuarioVO);
					unidadeEnsinoVO.setCaminhoBaseLogoMunicipio(PastaBaseArquivoEnum.LOGO_UNIDADE_ENSINO_TMP.getValue());
					unidadeEnsinoVO.setNomeArquivoLogoMunicipio(nomeIcone);
					unidadeEnsinoVO.setLogoInformadaMunicipio(true);
					
			}else if (tipoLogo.equals("APLICATIVO")) {
				if (unidadeEnsinoVO.getLogoInformadaAplicativo() && unidadeEnsinoVO.getCaminhoBaseLogoAplicativo().contains(PastaBaseArquivoEnum.LOGO_UNIDADE_ENSINO_TMP.getValue())) {
					arquivoExistente = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator + unidadeEnsinoVO.getCaminhoBaseLogoAplicativo() + File.separator + unidadeEnsinoVO.getNomeArquivoLogoAplicativo());
					ArquivoHelper.delete(arquivoExistente);
				}

				ArquivoHelper.salvarArquivoNaPastaTemp(upload, nomeIcone, PastaBaseArquivoEnum.LOGO_UNIDADE_ENSINO_TMP.getValue(), configuracaoGeralSistemaVO, usuarioVO);
				unidadeEnsinoVO.setCaminhoBaseLogoAplicativo(PastaBaseArquivoEnum.LOGO_UNIDADE_ENSINO_TMP.getValue());
				unidadeEnsinoVO.setNomeArquivoLogoAplicativo(nomeIcone);
				unidadeEnsinoVO.setLogoInformadaAplicativo(true);
		}
		} catch (Exception e) {
			throw e;
		} finally {
			arquivoExistente = null;
		}
	}

	@Override
	public UnidadeEnsinoVO consultarPorTurma(Integer turma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append("SELECT UnidadeEnsino.* FROM UnidadeEnsino ");
		sqlStr.append("LEFT JOIN Turma on Turma.unidadeEnsino = UnidadeEnsino.codigo ");
		sqlStr.append("WHERE Turma.codigo = ").append(turma);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		while (tabelaResultado.next()) {
			return (montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return null;
	}

	@Override
	public List<UnidadeEnsinoVO> consultarPorUsuarioNomeEntidadePermissao(String nomeEntidade, String permissao, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuarioVO);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select distinct unidadeEnsino.* ");
		sqlStr.append("from usuarioPerfilAcesso ");
		sqlStr.append("inner join unidadeEnsino on unidadeEnsino.codigo = usuarioPerfilAcesso.unidadeEnsino ");
		sqlStr.append("inner join perfilAcesso on perfilAcesso.codigo = usuarioPerfilAcesso.perfilAcesso ");
		sqlStr.append("inner join permissao on permissao.codPerfilAcesso = perfilAcesso.codigo ");
		sqlStr.append("where 1=1 ");
		if (!nomeEntidade.equals("")) {
			sqlStr.append(" and permissao.nomeEntidade = '").append(nomeEntidade).append("'");
		}
		if (usuarioVO.getCodigo() > 0) {
			sqlStr.append(" and usuarioPerfilAcesso.usuario = ").append(usuarioVO.getCodigo());
		}
		// sqlStr.append(" and permissao.permissoes ");
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString()), nivelMontarDados, usuarioVO);
	}

	public List<UnidadeEnsinoVO> consultarPorUsuario(UsuarioVO usuarioVO) throws Exception {
		Boolean possuiPermissaoUnidade = Boolean.FALSE;
		Boolean possuiPermissaoConsultarRequerimentoOutroDepartamentoMesmoTramiteTodasUnidades = Boolean.FALSE;
		Boolean possuiPermissaoConsultarRequerimentoOutrosResponsaveisMesmoDepartamentoTodasUnidades = Boolean.FALSE;
		try {
			getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("Requerimento_consultarTodasUnidades", usuarioVO);
			possuiPermissaoUnidade = Boolean.TRUE;
		} catch (Exception e) {

		}
		try {
			getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("Requerimento_consultarRequerimentoOutroDepartamentoMesmoTramiteTodasUnidades", usuarioVO);
			possuiPermissaoConsultarRequerimentoOutroDepartamentoMesmoTramiteTodasUnidades = Boolean.TRUE;
		} catch (Exception e) {

		}
		try {
			getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("Requerimento_consultarRequerimentoOutrosResponsaveisMesmoDepartamentoTodasUnidades", usuarioVO);
			possuiPermissaoConsultarRequerimentoOutrosResponsaveisMesmoDepartamentoTodasUnidades = Boolean.TRUE;
		} catch (Exception e) {

		}
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select distinct unidadeEnsino.codigo, unidadeEnsino.nome, unidadeEnsino.nomeexpedicaodiploma, ");
		sqlStr.append("unidadeEnsino.abreviatura, unidadeEnsino.cnpj, unidadeEnsino.matriz ");
		sqlStr.append("from unidadeEnsino ");
		if(possuiPermissaoUnidade || possuiPermissaoConsultarRequerimentoOutroDepartamentoMesmoTramiteTodasUnidades || possuiPermissaoConsultarRequerimentoOutrosResponsaveisMesmoDepartamentoTodasUnidades){
			sqlStr.append("LEFT JOIN usuarioPerfilAcesso on unidadeEnsino.codigo = usuarioPerfilAcesso.unidadeEnsino ");
		}else{
			sqlStr.append("INNER JOIN usuarioPerfilAcesso on unidadeEnsino.codigo = usuarioPerfilAcesso.unidadeEnsino ");
		}
		sqlStr.append("where 1=1 ");
		if (!possuiPermissaoUnidade && !possuiPermissaoConsultarRequerimentoOutroDepartamentoMesmoTramiteTodasUnidades && !possuiPermissaoConsultarRequerimentoOutrosResponsaveisMesmoDepartamentoTodasUnidades) {
			sqlStr.append(" AND usuarioPerfilAcesso.usuario = ").append(usuarioVO.getCodigo());
		}
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString()), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO);
	}
	
	public List<UnidadeEnsinoVO> consultarUnidadeEnsinoConfiguracoesPorGestaoEnvioMensagemAutomatica() {
		List<UnidadeEnsinoVO> listaUnidadeEnsino = new ArrayList<UnidadeEnsinoVO>(0);
		StringBuilder sql = new StringBuilder("");
		sql.append(" select codigo ,configuracoes from unidadeensino");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		while (rs.next()) {
			UnidadeEnsinoVO obj = new UnidadeEnsinoVO();
			obj.setCodigo(rs.getInt("codigo"));
			obj.getConfiguracoes().setCodigo(rs.getInt("configuracoes"));
			listaUnidadeEnsino.add(obj);
		}

		return listaUnidadeEnsino;

	}

	public UnidadeEnsinoVO consultaRapidaPorChavePrimariaDadosBasicosBoleto(Integer codUnidadeEnsino, UsuarioVO usuario) throws Exception {
		return getAplicacaoControle().getUnidadeEnsinoVO(codUnidadeEnsino, usuario);
//		StringBuffer sqlStr = new StringBuffer(getSQLPadraoConsultaBasica());
//		sqlStr.append(" WHERE (UnidadeEnsino.codigo= ").append(codUnidadeEnsino).append(")");
//		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
//		if (!tabelaResultado.next()) {
//			throw new ConsistirException("Dados Não Encontrados.");
//		}
//		UnidadeEnsinoVO obj = new UnidadeEnsinoVO();
//		montarDadosBasico((UnidadeEnsinoVO) obj, tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS);
//		return obj;
	}

	@Override
    public List<UnidadeEnsinoVO> consultarUnidadeEnsinoDoProcessoMatriculaPorCodigoBanner(Integer codigoBanner, UsuarioVO usuarioVO) throws Exception {
    	StringBuilder sqlStr = new StringBuilder();
    	sqlStr.append(" select distinct unidadeensinocurso.unidadeensino as unidadeensino");
    	sqlStr.append(" from processomatricula  ");
    	sqlStr.append(" inner join processomatriculaunidadeensino on processomatriculaunidadeensino.processomatricula = processomatricula.codigo");
    	sqlStr.append(" inner join processomatriculacalendario on processomatriculacalendario.processomatricula = processomatricula.codigo");
    	sqlStr.append(" inner join unidadeensinocurso on unidadeensinocurso.curso = processomatriculacalendario.curso and unidadeensinocurso.turno = processomatriculacalendario.turno and unidadeensinocurso.unidadeEnsino = processomatriculaunidadeensino.unidadeEnsino  ");
    	sqlStr.append(" where processomatriculacalendario.politicadivulgacaomatriculaonline = ").append(codigoBanner);
    	sqlStr.append(" and processomatricula.situacao in ('AT', 'PR') ");
    	sqlStr.append(" and processomatricula.apresentarprocessovisaoaluno  ");
    	sqlStr.append(" and processomatricula.datainiciomatriculaonline <= now()");
    	sqlStr.append(" and processomatricula.datafimmatriculaonline >= now()");
    	sqlStr.append(" and (select turma.codigo from turma  where turma.curso = unidadeensinocurso.curso and turma.turno = unidadeensinocurso.turno ");
    	sqlStr.append(" and turma.unidadeensino = unidadeensinocurso.unidadeensino and turma.situacao = 'AB'  and turma.apresentarrenovacaoonline  ");
    	sqlStr.append(" and turma.periodoletivo in (");
    	sqlStr.append(" select periodoletivo.codigo from gradecurricular ");
    	sqlStr.append(" inner join periodoletivo on periodoletivo.gradecurricular = gradecurricular.codigo  where periodoletivo.periodoletivo = 1 ");
    	sqlStr.append(" and gradecurricular.curso = unidadeensinocurso.curso and gradecurricular.situacao = 'AT') ");
    	sqlStr.append(" and  (select planofinanceirocurso from condicaopagamentoplanofinanceirocurso ");
    	sqlStr.append("	where condicaopagamentoplanofinanceirocurso.tipousocondicaopagamento = 'MATRICULA_REGULAR' ");
    	sqlStr.append("	and condicaopagamentoplanofinanceirocurso.situacao = 'AT' ");
    	if (usuarioVO != null && (usuarioVO.getIsApresentarVisaoAluno() || usuarioVO.getIsApresentarVisaoPais())) {
    		sqlStr.append(" and condicaopagamentoplanofinanceirocurso.apresentarCondicaoVisaoAluno ");
		} else if (usuarioVO != null && (usuarioVO.getIsApresentarVisaoProfessor())) {
			sqlStr.append(" and condicaopagamentoplanofinanceirocurso.apresentarMatriculaOnlineProfessor ");
		} else if (usuarioVO != null && (usuarioVO.getIsApresentarVisaoCoordenador())) {
			sqlStr.append(" and condicaopagamentoplanofinanceirocurso.apresentarMatriculaOnlineCoordenador ");
		} else if (!Uteis.isAtributoPreenchido(usuarioVO) || !Uteis.isAtributoPreenchido(usuarioVO.getVisaoLogar())) {
			sqlStr.append(" and condicaopagamentoplanofinanceirocurso.apresentarMatriculaOnlineExterna ");
		}
    	sqlStr.append("	and condicaopagamentoplanofinanceirocurso.planofinanceirocurso = case when turma.planofinanceirocurso is not null then turma.planofinanceirocurso else unidadeensinocurso.planofinanceirocurso end ");
    	sqlStr.append("	limit 1");
    	sqlStr.append("	) is not null limit 1) is not null");
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        UnidadeEnsinoVO unidadeEnsinoVO = null;
        List<UnidadeEnsinoVO> unidadeEnsinoVOs = new ArrayList<UnidadeEnsinoVO>();
		while (rs.next()) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
			unidadeEnsinoVO.setCodigo(rs.getInt("unidadeensino"));
			unidadeEnsinoVO = consultarPorChavePrimaria(unidadeEnsinoVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
			unidadeEnsinoVOs.add(unidadeEnsinoVO);
		}
        return unidadeEnsinoVOs;
    }

	public List<UnidadeEnsinoVO> consultarPorProcSeletivoComboBox( Boolean possuiUnidadeEnsinoLogada, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct unidadeensino.codigo, unidadeensino.nome, unidadeensino.cnpj from unidadeensino ");
		sb.append(" inner join procseletivounidadeensino on procseletivounidadeensino.unidadeEnsino = unidadeensino.codigo ");
		if (possuiUnidadeEnsinoLogada) {
			sb.append(" inner join usuarioPerfilAcesso on usuarioPerfilAcesso.unidadeEnsino = unidadeEnsino.codigo ");
		}
		sb.append(" where 1=1 ");
//		if (!listaProcSeletivoVOs.isEmpty()) {
//			sb.append("and procseletivounidadeensino.procSeletivo  in (");
//		}
//		for (ProcSeletivoVO procSeletivoVO : listaProcSeletivoVOs) {
//			if (procSeletivoVO.getFiltrarProcessoSeletivo()) {
//				sb.append(procSeletivoVO.getCodigo()).append(", ");
//			}
//		}
//		if (!listaProcSeletivoVOs.isEmpty()) {
//			sb.append("0) ");
//		}
		if (possuiUnidadeEnsinoLogada) {
			sb.append(" and usuarioPerfilAcesso.usuario = ").append(usuarioVO.getCodigo());
		}
		sb.append(" order by unidadeEnsino.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<UnidadeEnsinoVO> listaUnidadeEnsinoVOs = null;
		UnidadeEnsinoVO unidadeEnsinoVO = null;
		while (tabelaResultado.next()) {
			if (listaUnidadeEnsinoVOs == null) {
				listaUnidadeEnsinoVOs = new ArrayList<UnidadeEnsinoVO>(0);
			}
			unidadeEnsinoVO = new UnidadeEnsinoVO();
			unidadeEnsinoVO.setCodigo(tabelaResultado.getInt("codigo"));
			unidadeEnsinoVO.setNome(tabelaResultado.getString("nome"));
			unidadeEnsinoVO.setCNPJ(tabelaResultado.getString("cnpj"));
			unidadeEnsinoVO.setFiltrarUnidadeEnsino(true);
			listaUnidadeEnsinoVOs.add(unidadeEnsinoVO);
		}
		return listaUnidadeEnsinoVOs;
	}

	
	public String renderizarLogoAplicativo(ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, String pastaBase, String nomeImagem) {
		String caminhoArquivo;
		String nomeArquivo = nomeImagem;
		File arquivoImagem = null;

		if (!nomeImagem.equals("") && !pastaBase.equals("")) {
			if (configuracaoGeralSistemaVO == null) {
				return "";
			}

			if (configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo().endsWith("/")) {
				caminhoArquivo = configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo() + pastaBase + "/";
			} else {
				caminhoArquivo = configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo() + "/" + pastaBase + "/";
			}

			arquivoImagem = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + pastaBase + File.separator + nomeArquivo);
			if (arquivoImagem.exists()) {
				return caminhoArquivo + "/" + nomeArquivo;
			} else {
				return "";
			}
		}

		return "";
	}
	
	@Override
	public List<UnidadeEnsinoVO> consultarPorUnidadeEnsinoPorBiblioteca(Integer biblioteca, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT distinct unidadeEnsino.* FROM UnidadeEnsino "
				+ " inner join unidadeensinobiblioteca on unidadeensinobiblioteca.unidadeensino = unidadeensino.codigo"
				+ " WHERE unidadeensinobiblioteca.biblioteca = "+ biblioteca;
		if (unidadeEnsino > 0) {
			sqlStr += " and unidadeensinobiblioteca.unidadeensino = " + unidadeEnsino ;
		}
		sqlStr += "ORDER BY nome ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}
	
	@Override
	public List<UnidadeEnsinoVO> consultarPorConfiguracaoAtendimento(boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT unidadeensino.* FROM unidadeensino inner join configuracaoatendimentounidadeensino on configuracaoatendimentounidadeensino.unidadeensino = unidadeensino.codigo where unidadeensino is not null";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}
	
	@Override
	public List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorProfessor(boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("select distinct turma.unidadeensino as codigo ,unidadeensino.nome as nome ,unidadeensino.nomeexpedicaodiploma , unidadeensino.abreviatura  from horarioturma ");
		sqlStr.append(" inner join turma on horarioturma.turma = turma.codigo");
		sqlStr.append(" inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo");
		sqlStr.append(" inner join horarioturmadiaitem on horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia");
		sqlStr.append(" inner join pessoa ON horarioturmadiaitem.professor = Pessoa.codigo");
		sqlStr.append(" inner join unidadeensino ON Turma.UnidadeEnsino = UnidadeEnsino.codigo");
		sqlStr.append(" left join turmaagrupada on turmaagrupada.turmaorigem = turma.codigo left join turma t2 on t2.codigo = turmaagrupada.turma");
		sqlStr.append(" left join curso on curso.codigo = case when turma.turmaagrupada then t2.curso else turma.curso end");
		sqlStr.append(" left join turno on turno.codigo = case when turma.turmaagrupada then t2.turno else");
		sqlStr.append(" turma.turno end where Pessoa.codigo = ").append(usuario.getPessoa().getCodigo());
		sqlStr.append(" order by codigo ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * 
	 * Consulta <code>UnidadeEnsino</code> através do valor do atributo <code>String nome</code>.
	 * Retorna o objeto com o valor do atributo idêntico ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code>
	 * 
	 * @param valorConsulta
	 * @param controlarAcesso
	 * @param nivelMontarDados
	 * @param usuario
	 * 
	 * @return UnidadeEnsinoVO
	 * 
	 * @throws Exception
	 * 
	 */
	@Override
	public UnidadeEnsinoVO consultarUnidadeDeEnsinoPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM UnidadeEnsino ue WHERE lower(ue.nome) = ('" + valorConsulta.toLowerCase() + "') ORDER BY ue.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		
		if(tabelaResultado.next()) {		
			return (montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		throw new Exception(UteisJSF.internacionalizar("msg_UnidadeDeEnsinoLeadNaoEncontrado"));
	}
	
	public List<UnidadeEnsinoVO> consultarPorArtefatoEntregaAluno(Integer artefato, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT distinct (unidadeensino.*) FROM unidadeensino INNER JOIN nivelcontroleartefato on unidadeensino.codigo=nivelcontroleartefato.unidadeensino WHERE nivelcontroleartefato.artefato = " + artefato;

		sqlStr += " ORDER BY unidadeensino.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	
	@Override
	public List<UnidadeEnsinoVO> consultarUnidadeEnsinoPessoaEBiblioteca(Integer pessoa, Integer biblioteca, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("select distinct ue.* from unidadeensino ue ");
		sqlStr.append("inner join funcionarioCargo fc on fc.unidadeensino = ue.codigo ");
		sqlStr.append("inner join funcionario fu on fu.codigo = fc.funcionario ");
//		sqlStr.append("inner join unidadeensinobiblioteca on unidadeensinobiblioteca.unidadeensino = ue.codigo and unidadeensinobiblioteca.biblioteca = ? ");
		sqlStr.append("where fu.pessoa = ? ");		
		sqlStr.append(" Order by ue.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), pessoa);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}
	
	@Override
	public List<UnidadeEnsinoVO> consultarUnidadeEnsinoAcademicaEFinanceiraConformeUnidadeEnsinoLogada(Integer unidadeEnsinoLogada, int nivelMontarDados, UsuarioVO usuario) throws Exception{
		StringBuilder sqlStr = new StringBuilder(" with cte_contareceber as (select distinct unidadeensinofinanceira,unidadeensino from contareceber where contareceber.unidadeensino = ? ) ");		
		sqlStr.append(" select * from unidadeensino where exists (select 1 from cte_contareceber where cte_contareceber.unidadeensino = ? ");	
		sqlStr.append(" and cte_contareceber.unidadeensinofinanceira = unidadeensino.codigo ) order by unidadeensino.nome");	
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), unidadeEnsinoLogada, unidadeEnsinoLogada);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}
	
	@Override
	public UnidadeEnsinoVO consultarPorNome(String nome, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder() ;
		sqlStr.append("SELECT ue.* FROM UnidadeEnsino ue ");
		sqlStr.append(" WHERE lower(ue.nome) = ('").append(nome.toLowerCase()).append("') ");
		sqlStr.append("ORDER BY ue.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		
		if(tabelaResultado.next()) {		
			return (montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return new UnidadeEnsinoVO();
	}
	
	@Override
	public UnidadeEnsinoVO consultarPorCodigo(Integer codigo, UsuarioVO usuarioVO) {
		StringBuilder sql = new StringBuilder();
		sql.append("select codigo, nome from unidadeEnsino where codigo = ").append(codigo);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (tabelaResultado.next()) {
			UnidadeEnsinoVO obj = new UnidadeEnsinoVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setNome(tabelaResultado.getString("nome"));
			return obj;
		}
		return new UnidadeEnsinoVO();
		
	}


	@Override
	public UnidadeEnsinoVO consultarPorChavePrimariaUnica(Integer codigoPrm,  UsuarioVO usuario) throws Exception {				
		UnidadeEnsinoVO unid = new UnidadeEnsinoVO();
		unid.setCodigo(codigoPrm);
		carregarDadosNivelAplicacao(unid, usuario);
		return unid;
	}

	@Override
	public UnidadeEnsinoVO consultarUnidadesVinculadaConfiguracaoGed(List<ProgramacaoFormaturaUnidadeEnsinoVO> listaProgramacao, int nivelMontardados, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ue.* FROM unidadeensino ue ");
		sql.append("INNER JOIN configuracaoged cg ON cg.codigo = ue.configuracaoged ");
		sql.append("WHERE (ue.codigo in (");
		if (Uteis.isAtributoPreenchido(listaProgramacao)) {
			for (ProgramacaoFormaturaUnidadeEnsinoVO unidade : listaProgramacao) {
				if (!listaProgramacao.get(listaProgramacao.size()-1).getUnidadeEnsinoVO().getCodigo().equals(unidade.getUnidadeEnsinoVO().getCodigo())) {
					sql.append(unidade.getUnidadeEnsinoVO().getCodigo() + ", ");
				} else {
					sql.append(unidade.getUnidadeEnsinoVO().getCodigo() + ") ");
				}
			}
		}
		sql.append("and ue.configuracaoged is not null) ORDER BY ue.codigo LIMIT 1 ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("As UNIDADES selecionadas não contem uma (Configuração Ged) vinculada.");
		} 
		return montarDados(tabelaResultado, nivelMontardados, usuarioLogado);
	}
	
	
	

	
	@Override
	public UnidadeEnsinoVO consultarSeExisteUnidadeMatrizParaExpedicaoDiploma(  Boolean controlarAcesso , UsuarioVO usuario) throws Exception {	
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		 sqlStr.append("SELECT UnidadeEnsino.codigo ,  UnidadeEnsino.nome ,  UnidadeEnsino.credenciamentoPortaria , UnidadeEnsino.dataPublicacaoDO , UnidadeEnsino.mantenedora , UnidadeEnsino.razaoSocial ,");
		 sqlStr.append(" cidade.nome as  \"cidade.nome\" ,  cidade.codigo as  \"cidade.codigo\"   FROM UnidadeEnsino ");
		 sqlStr.append(" left join cidade on cidade.codigo = UnidadeEnsino.cidade   WHERE   matriz = true  ORDER BY UnidadeEnsino.nome ");
		 SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		 UnidadeEnsinoVO obj = new UnidadeEnsinoVO();
		if (dadosSQL.next()) {	
			obj.setMatriz(true);
			obj.setCodigo(dadosSQL.getInt("codigo"));
			obj.setNome(dadosSQL.getString("nome"));
			obj.setCredenciamentoPortaria(dadosSQL.getString("credenciamentoPortaria"));
			obj.setDataPublicacaoDO(dadosSQL.getDate("dataPublicacaoDO"));
			obj.getCidade().setCodigo(dadosSQL.getInt("cidade.codigo"));
			obj.getCidade().setNome(dadosSQL.getString("cidade.nome"));
			obj.setMantenedora(dadosSQL.getString("mantenedora"));
			obj.setRazaoSocial(dadosSQL.getString("razaoSocial"));
			obj.setNivelMontarDados(NivelMontarDados.BASICO);			
		}	
		return obj;	
	}
	
	
	
	
	 @Override
		public UnidadeEnsinoVO consultarCodigoUnicaUnidadeEnsinoPorNome(String unidadeEnsino, int nivelMontarDados, UsuarioVO usuario) {
			String sqlStr = "select * from unidadeensino where sem_acentos(nome) ilike sem_acentos(?) ";
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, unidadeEnsino );
			if(tabelaResultado.next()) {
				try {
					return montarDados(tabelaResultado, nivelMontarDados, usuario);
				} catch (Exception e) {
					e.printStackTrace();
				}	
			}
			return null;
		}
	 
	 @Override
	 public List<UnidadeEnsinoVO> consultarPorCursoAtivo(Integer unidadeEnsino, Integer curso, Integer turno, Boolean validarVagasPorComputador, Boolean considerarVagaCurso,  String ano, String semestre, DiaSemana diaSemana, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
			getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
			StringBuilder sqlStr = new StringBuilder("SELECT UnidadeEnsino.* FROM UnidadeEnsino ");
			if (Uteis.isAtributoPreenchido(curso)) {
				sqlStr.append(" inner join unidadeensinocurso on unidadeensinocurso.unidadeensino = UnidadeEnsino.codigo and unidadeensinocurso.curso = ").append(curso).append(" and unidadeensinocurso.turno = ").append(turno).append(" and unidadeensinocurso.situacaocurso =  'AT' ");
			}
			sqlStr.append(" WHERE UnidadeEnsino.desativada = false ");
			if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
				sqlStr.append(" and UnidadeEnsino.codigo = ").append(unidadeEnsino).append(" ");
			}
			if(validarVagasPorComputador != null && validarVagasPorComputador && Uteis.isAtributoPreenchido(curso)) {
				sqlStr.append(" and UnidadeEnsino.quantidadeComputadoresAlunos > (select count(matriculaperiodo.codigo) from matriculaperiodo inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
				if(Uteis.isAtributoPreenchido(considerarVagaCurso) && considerarVagaCurso) {
					sqlStr.append(" and matricula.curso = unidadeensinocurso.curso ");
				}
				sqlStr.append(" and matricula.unidadeensino  = unidadeensinocurso.unidadeensino "); 
				sqlStr.append(" and matricula.situacao in ('AT', 'PR') ");
				sqlStr.append(" and matriculaperiodo.situacaomatriculaperiodo in ('AT', 'PR', 'FI') ");
				sqlStr.append(" and matricula.diasemanaaula = '").append(diaSemana.name()).append("' ");
				sqlStr.append(" and matriculaperiodo.ano = '").append(ano).append("' ");
				sqlStr.append(" and matriculaperiodo.semestre = '").append(semestre).append("' ");
				sqlStr.append(" ) ");				
			}
			sqlStr.append(" order by UnidadeEnsino.nome ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
		}
	 
	 
	 @Override
	 public UnidadeEnsinoRSVO consultarUnidadeEnsinoMatriculaOnlineProcessoSeletivo(Integer codUnidadeEnsino , UsuarioVO usuario, RenovarMatriculaControle renovarMatriculaControle) {
			UnidadeEnsinoRSVO unidadeEnsinoRSVO = new UnidadeEnsinoRSVO();
			try {
				UnidadeEnsinoVO unidadeEnsinoVO = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(codUnidadeEnsino, false, usuario);
				renovarMatriculaControle.getMatriculaVO().setUnidadeEnsino(unidadeEnsinoVO);
				if (Uteis.isAtributoPreenchido(unidadeEnsinoVO)) {
					unidadeEnsinoRSVO.setCodigo(unidadeEnsinoVO.getCodigo());
					unidadeEnsinoRSVO.setNome(unidadeEnsinoVO.getNome());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return unidadeEnsinoRSVO;
		}
	 
	 @Override
	 public List<UnidadeEnsinoVO> consultarTodasUnidadeEnsinoVagaOfertada() throws Exception {
		 StringBuilder sqlStr = new StringBuilder();
		 sqlStr.append("select codigo, nome, numerovagaofertada from unidadeEnsino");
		 SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		 List<UnidadeEnsinoVO> obj = new ArrayList<>();
		 
		 while (dadosSQL.next()) {
		     UnidadeEnsinoVO unidadeEnsino = new UnidadeEnsinoVO();
		     unidadeEnsino.setNome(dadosSQL.getString("nome")); 
		     unidadeEnsino.setNumeroVagaOfertada(dadosSQL.getInt("numerovagaofertada"));
		     unidadeEnsino.setCodigo(dadosSQL.getInt("codigo"));		     
		     obj.add(unidadeEnsino);
		 }
		 

		 return obj;
	 }
	 
	 private void montarCabecalhoExcelVagaOfertada(UteisExcel uteisExcel, XSSFSheet sheet){
			int cellnum = 0;
			Row row;
			
			if(sheet.getLastRowNum() > 0){ 
				row = sheet.createRow(sheet.getLastRowNum()+1);
			} else{
				row = sheet.createRow(0);
			}
			uteisExcel.preencherCelulaCabecalho(sheet,row, cellnum++, 5000, "Codigo");	
			uteisExcel.preencherCelulaCabecalho(sheet,row, cellnum++, 10000, "Nome");
			uteisExcel.preencherCelulaCabecalho(sheet,row,cellnum++, 5000, "Vagas Ofertadas");
			
		}

		private void montarItensExcelVagaOfertadaTransferencia(UteisExcel uteisExcel, XSSFSheet sheet, List<UnidadeEnsinoVO> listaUnidadeEnsino){
				for(UnidadeEnsinoVO unidade : listaUnidadeEnsino){
					int cellnum = 0;
					Row row = sheet.createRow(sheet.getLastRowNum() +1);
					uteisExcel.preencherCelula(row, cellnum++, unidade.getCodigo());
					uteisExcel.preencherCelula(row, cellnum++, unidade.getNome());
					uteisExcel.preencherCelula(row,	cellnum++, unidade.getNumeroVagaOfertada());
				}
		}

		@Override		
		public File carregarExcelVagaOfertadaUnidadeEnsino(List<UnidadeEnsinoVO> listaUnidadeEnsino,ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO ) throws Exception{
		    File arquivo = null;
		    XSSFWorkbook workbook = null;
		    FileOutputStream out = null;
			try {
				workbook = new XSSFWorkbook();
				XSSFSheet sheet = workbook.createSheet("Unidades de Ensino - Vagas Ofertadas");
				UteisExcel uteisExcel = new UteisExcel(workbook);
				listaUnidadeEnsino.sort(Comparator.comparingInt(UnidadeEnsinoVO::getCodigo));
				montarCabecalhoExcelVagaOfertada(uteisExcel, sheet);
				montarItensExcelVagaOfertadaTransferencia(uteisExcel, sheet, listaUnidadeEnsino);
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy"); 
				String data = dateFormat.format(new Date());
				arquivo = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoTemp()+ File.separator + PastaBaseArquivoEnum.UNIDADEENSINO_TMP.getValue() + File.separator + "UnidadeEnsinoVagasOfertadas-"+data+".xlsx");
				out = new FileOutputStream(arquivo);
				workbook.write(out);
				out.close();
				return arquivo;
				
			} catch (Exception e) {
				throw e;
			} finally {
				workbook.close();
			}
		}
		
		private Cell getValorCelula(int coluna, Row row, boolean isString) {
		    Cell cell = row.getCell(coluna);
		    if (cell == null) {
		        return null;
		    }

		    return cell;
		}
	    
	    @Override		
	    public List<UnidadeEnsinoVO> importarPlanilhaUnidadeEnsinoVagaOfertada(FileUploadEvent uploadEvent, UsuarioVO usuario) throws Exception {
	        String extensao = uploadEvent.getFile().getFileName().substring(uploadEvent.getFile().getFileName().lastIndexOf(".") + 1).toLowerCase();
	        List<UnidadeEnsinoVO> unidadesEnsino = new ArrayList<>();        
	        int rowMax = 0;
	        XSSFSheet mySheetXlsx = null;
	        HSSFSheet mySheetXls = null;
	        XSSFWorkbook xssfWorkbook = null;
	        HSSFWorkbook hssfWorkbook = null;
	        if (extensao.equals("xlsx")) {
	        	xssfWorkbook = new XSSFWorkbook(uploadEvent.getFile().getInputStream());
	            mySheetXlsx = xssfWorkbook.getSheetAt(0);
	            rowMax = mySheetXlsx.getLastRowNum();
	        } else if (extensao.equals("xls")) {
	        	hssfWorkbook = new HSSFWorkbook(uploadEvent.getFile().getInputStream());
	            mySheetXls = hssfWorkbook.getSheetAt(0);
	            rowMax = mySheetXls.getLastRowNum();
	        } else {
	            throw new Exception("Formato de arquivo não suportado.");
	        }	        
	        Row headerRow = (extensao.equals("xlsx")) ? mySheetXlsx.getRow(0) : mySheetXls.getRow(0);
	        if (headerRow.getPhysicalNumberOfCells() != 3) {
	            throw new Exception("A planilha deve conter exatamente 3 colunas: Código, Nome e Vagas Ofertadas.");
	        }	        
	        int linha = 1;
	        Row row;
	        
	        while (linha <= rowMax) {
	            if (extensao.equals("xlsx")) {
	                row = mySheetXlsx.getRow(linha);
	            } else {
	                row = mySheetXls.getRow(linha);
	            }
	            if (row == null || getValorCelula(0, row, true) == null || getValorCelula(0, row, true).toString().trim().isEmpty()) {
	                linha++;
	                continue;
	            }
	            try {
	                String codigoStr = getValorCelula(0, row, true).toString().replaceAll("\\.0*$", "").trim();
	                int codigo = Integer.parseInt(codigoStr);
	                String vagasStr = getValorCelula(2, row, true).toString().replaceAll("\\.0*$", "").trim();
	                int numeroVagaOfertada = Integer.parseInt(vagasStr);
	                String nome = getValorCelula(1, row, true).toString().trim();
	                UnidadeEnsinoVO unidade = new UnidadeEnsinoVO();
	                unidade.setCodigo(codigo);
	                unidade.setNome(nome);
	                unidade.setNumeroVagaOfertada(numeroVagaOfertada);
	                unidadesEnsino.add(unidade);	          
	            } catch (Exception e) {
	                throw new Exception("Erro ao processar a linha " + (linha + 1) + ": " + e.getMessage());
	            }  finally {
	                if (hssfWorkbook != null) {
//	                    hssfWorkbook.close();
	                }
	                if (xssfWorkbook != null) {
	                    xssfWorkbook.close();
	                }
	            }
				       
	            linha++;
	        }

	        return unidadesEnsino;
	    }
	    
		@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
		public void alterarNumeroVagaOfertadaUnidadeEnsino(final UnidadeEnsinoVO obj) throws Exception {
			try {
				final String sql = "UPDATE UnidadeEnsino set numeroVagaOfertada=? WHERE ((codigo = ?))";
				getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

					public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
						PreparedStatement sqlAlterar = con.prepareStatement(sql);
						sqlAlterar.setInt(1, obj.getNumeroVagaOfertada());
						sqlAlterar.setInt(2, obj.getCodigo().intValue());
						return sqlAlterar;
					}
				});
			} catch (Exception e) {
				throw e;
			}
		}
		
	private void carregarDadosNivelAplicacao(UnidadeEnsinoVO obj, UsuarioVO usuarioVO) throws Exception {
		if (Uteis.isAtributoPreenchido(obj)) {
			StringBuffer str = new StringBuffer();
			str.append(" SELECT unidadeEnsino.*, ");
			str.append(" cidade.nome AS \"cidade.nome\", cidade.codigoibge as \"cidade.codigoIBGE\", cidade.codigodistrito as \"cidade.codigoDistrito\", cidade.codigoinep as \"cidade.codigoInep\", ");
			str.append(" pessoa.nome AS \"diretorGeral.nome\", p2.nome AS \"responsavelCobrancaUnidade.nome\" , p3.nome AS \"pessoaResponsavelNotificacaoAlteracaoCronogramaAula.nome\" ,  p3.codigo AS \"pessoaResponsavelNotificacaoAlteracaoCronogramaAula.codigo\" , ");
			str.append(" unidadeEnsino.coordenadorTCC, p2tcc.nome as \"nomeCoordenadorTCC\", ");
			str.append(" estado.nome AS \"estado.nome\", estado.codigo AS \"estado.codigo\" , estado.codigoibge as \"estado.codigoibge\", estado.sigla AS \"estado.sigla\", estado.codigoinep as \"estado.codigoInep\", ");
			str.append(" unidadeensino.contaCorrentePadraoMatricula AS \"contaCorrentePadraoMatricula.codigo\", ");
			str.append(" unidadeensino.contaCorrentePadraoMensalidade AS \"contaCorrentePadraoMensalidade.codigo\", ");
			str.append(" unidadeensino.contaCorrentePadraoBiblioteca AS \"contaCorrentePadraoBiblioteca.codigo\", ");
			str.append(" unidadeensino.contaCorrentePadraoDevolucaoCheque AS \"contaCorrentePadraoDevolucaoCheque.codigo\", ");
			str.append(" unidadeensino.contaCorrentePadraoMaterialDidatico AS \"contaCorrentePadraoMaterialDidatico.codigo\", ");
			str.append(" unidadeensino.contaCorrentePadraoNegociacao AS \"contaCorrentePadraoNegociacao.codigo\", ");
			str.append(" unidadeensino.contaCorrentePadraoProcessoSeletivo AS \"contaCorrentePadraoProcessoSeletivo.codigo\", ");
			str.append(" unidadeensino.dataCredenciamento AS \"unidadeensino.dataCredenciamento\", ");
			str.append(" unidadeensino.veiculoPublicacaoCredenciamento AS \"unidadeensino.veiculoPublicacaoCredenciamento\", ");
			str.append(" unidadeensino.secaoPublicacaoCredenciamento AS \"unidadeensino.secaoPublicacaoCredenciamento\", ");
			str.append(" unidadeensino.paginaPublicacaoCredenciamento AS \"unidadeensino.paginaPublicacaoCredenciamento\", ");
			str.append(" unidadeensino.numeroDOUCredenciamento AS \"unidadeensino.numeroDOUCredenciamento\", ");
			str.append(" unidadeensino.tipoAutorizacaoEnum AS \"unidadeensino.tipoAutorizacaoEnum\", ");
			str.append(" unidadeensino.informarDadosRegistradora AS \"unidadeensino.informarDadosRegistradora\", unidadeensino.utilizarEnderecoUnidadeEnsinoRegistradora AS \"unidadeensino.utilizarEnderecoUnidadeEnsinoRegistradora\", unidadeensino.cepRegistradora AS \"unidadeensino.cepRegistradora\", unidadeensino.cidadeRegistradora AS \"unidadeensino.cidadeRegistradora\", unidadeensino.complementoRegistradora AS \"unidadeensino.complementoRegistradora\", unidadeensino.bairroRegistradora AS \"unidadeensino.bairroRegistradora\", unidadeensino.enderecoRegistradora AS \"unidadeensino.enderecoRegistradora\", unidadeensino.numeroRegistradora AS \"unidadeensino.numeroRegistradora\", cidadeRegistradora.nome AS \"cidadeRegistradora.nome\", ");
			str.append(" unidadeensino.utilizarCredenciamentoUnidadeEnsino AS \"unidadeensino.utilizarCredenciamentoUnidadeEnsino\", unidadeensino.numeroCredenciamentoRegistradora AS \"unidadeensino.numeroCredenciamentoRegistradora\", unidadeensino.dataCredenciamentoRegistradora AS \"unidadeensino.dataCredenciamentoRegistradora\", unidadeensino.dataPublicacaoDORegistradora AS \"unidadeensino.dataPublicacaoDORegistradora\", unidadeensino.veiculoPublicacaoCredenciamentoRegistradora AS \"unidadeensino.veiculoPublicacaoCredenciamentoRegistradora\", unidadeensino.secaoPublicacaoCredenciamentoRegistradora AS \"unidadeensino.secaoPublicacaoCredenciamentoRegistradora\", unidadeensino.paginaPublicacaoCredenciamentoRegistradora AS \"unidadeensino.paginaPublicacaoCredenciamentoRegistradora\", unidadeensino.numeroPublicacaoCredenciamentoRegistradora AS \"unidadeensino.numeroPublicacaoCredenciamentoRegistradora\", ");
			str.append(" unidadeensino.utilizarMantenedoraUnidadeEnsino AS \"unidadeensino.utilizarMantenedoraUnidadeEnsino\", unidadeensino.mantenedoraRegistradora AS \"unidadeensino.mantenedoraRegistradora\", unidadeensino.cnpjMantenedoraRegistradora AS \"unidadeensino.cnpjMantenedoraRegistradora\", unidadeensino.cepMantenedoraRegistradora AS \"unidadeensino.cepMantenedoraRegistradora\", unidadeensino.enderecoMantenedoraRegistradora AS \"unidadeensino.enderecoMantenedoraRegistradora\", unidadeensino.numeroMantenedoraRegistradora AS \"unidadeensino.numeroMantenedoraRegistradora\", unidadeensino.cidadeMantenedoraRegistradora AS \"unidadeensino.cidadeMantenedoraRegistradora\", cidadeMantenedoraRegistradora.nome AS \"cidadeMantenedoraRegistradora.nome\", unidadeensino.complementoMantenedoraRegistradora AS \"unidadeensino.complementoMantenedoraRegistradora\", unidadeensino.bairroMantenedoraRegistradora AS \"unidadeensino.bairroMantenedoraRegistradora\", ");
			str.append(" unidadeensino.utilizarEnderecoUnidadeEnsinoMantenedora AS \"unidadeensino.utilizarEnderecoUnidadeEnsinoMantenedora\", unidadeensino.cepMantenedora AS \"unidadeensino.cepMantenedora\", unidadeensino.enderecoMantenedora AS \"unidadeensino.enderecoMantenedora\", unidadeensino.numeroMantenedora AS \"unidadeensino.numeroMantenedora\", unidadeensino.cidadeMantenedora AS \"unidadeensino.cidadeMantenedora\", cidadeMantenedora.nome AS \"cidadeMantenedora.nome\", unidadeensino.complementoMantenedora AS \"unidadeensino.complementoMantenedora\", unidadeensino.bairroMantenedora AS \"unidadeensino.bairroMantenedora\", ");
			str.append(" unidadeensino.numeroRecredenciamento AS \"unidadeensino.numeroRecredenciamento\", unidadeensino.dataRecredenciamento AS \"unidadeensino.dataRecredenciamento\", unidadeensino.dataPublicacaoRecredenciamento AS \"unidadeensino.dataPublicacaoRecredenciamento\", unidadeensino.veiculoPublicacaoRecredenciamento AS \"unidadeensino.veiculoPublicacaoRecredenciamento\", unidadeensino.secaoPublicacaoRecredenciamento AS \"unidadeensino.secaoPublicacaoRecredenciamento\", unidadeensino.paginaPublicacaoRecredenciamento AS \"unidadeensino.paginaPublicacaoRecredenciamento\", unidadeensino.numeroDOURecredenciamento AS \"unidadeensino.numeroDOURecredenciamento\", unidadeensino.tipoAutorizacaoRecredenciamento AS \"unidadeensino.tipoAutorizacaoRecredenciamento\", ");
			str.append(" unidadeensino.numeroRenovacaoRecredenciamento AS \"unidadeensino.numeroRenovacaoRecredenciamento\", unidadeensino.dataRenovacaoRecredenciamento AS \"unidadeensino.dataRenovacaoRecredenciamento\", unidadeensino.dataPublicacaoRenovacaoRecredenciamento AS \"unidadeensino.dataPublicacaoRenovacaoRecredenciamento\", unidadeensino.veiculoPublicacaoRenovacaoRecredenciamento AS \"unidadeensino.veiculoPublicacaoRenovacaoRecredenciamento\", unidadeensino.secaoPublicacaoRenovacaoRecredenciamento AS \"unidadeensino.secaoPublicacaoRenovacaoRecredenciamento\", unidadeensino.paginaPublicacaoRenovacaoRecredenciamento AS \"unidadeensino.paginaPublicacaoRenovacaoRecredenciamento\", unidadeensino.numeroDOURenovacaoRecredenciamento AS \"unidadeensino.numeroDOURenovacaoRecredenciamento\", unidadeensino.tipoAutorizacaoRenovacaoRecredenciamento AS \"unidadeensino.tipoAutorizacaoRenovacaoRecredenciamento\", cidadeMantenedora.codigoIBGE AS \"cidadeMantenedora.codigoIBGE\", estadoMantenedora.sigla AS \"estadoMantenedora.sigla\", ");
			str.append(" unidadeensino.numeroCredenciamentoEAD AS \"unidadeensino.numeroCredenciamentoEAD\", unidadeensino.credenciamentoEAD AS \"unidadeensino.credenciamentoEAD\", unidadeensino.dataCredenciamentoEAD AS \"unidadeensino.dataCredenciamentoEAD\", unidadeensino.dataPublicacaoDOEAD AS \"unidadeensino.dataPublicacaoDOEAD\", unidadeensino.credenciamentoPortariaEAD AS \"unidadeensino.credenciamentoPortariaEAD\", unidadeensino.veiculoPublicacaoCredenciamentoEAD AS \"unidadeensino.veiculoPublicacaoCredenciamentoEAD\", unidadeensino.secaoPublicacaoCredenciamentoEAD AS \"unidadeensino.secaoPublicacaoCredenciamentoEAD\", unidadeensino.paginaPublicacaoCredenciamentoEAD AS \"unidadeensino.paginaPublicacaoCredenciamentoEAD\", unidadeensino.numeroDOUCredenciamentoEAD AS \"unidadeensino.numeroDOUCredenciamentoEAD\", unidadeensino.tipoAutorizacaoEAD AS \"unidadeensino.tipoAutorizacaoEAD\", unidadeensino.numeroRecredenciamentoEAD AS \"unidadeensino.numeroRecredenciamentoEAD\", unidadeensino.dataRecredenciamentoEAD AS \"unidadeensino.dataRecredenciamentoEAD\", unidadeensino.dataPublicacaoRecredenciamentoEAD AS \"unidadeensino.dataPublicacaoRecredenciamentoEAD\", unidadeensino.veiculoPublicacaoRecredenciamentoEAD AS \"unidadeensino.veiculoPublicacaoRecredenciamentoEAD\", unidadeensino.secaoPublicacaoRecredenciamentoEAD AS \"unidadeensino.secaoPublicacaoRecredenciamentoEAD\", unidadeensino.paginaPublicacaoRecredenciamentoEAD AS \"unidadeensino.paginaPublicacaoRecredenciamentoEAD\", unidadeensino.numeroDOURecredenciamentoEAD AS \"unidadeensino.numeroDOURecredenciamentoEAD\", unidadeensino.tipoAutorizacaoRecredenciamentoEAD AS \"unidadeensino.tipoAutorizacaoRecredenciamentoEAD\", unidadeensino.numeroRenovacaoRecredenciamentoEAD AS \"unidadeensino.numeroRenovacaoRecredenciamentoEAD\", unidadeensino.dataRenovacaoRecredenciamentoEAD AS \"unidadeensino.dataRenovacaoRecredenciamentoEAD\", unidadeensino.dataPublicacaoRenovacaoRecredenciamentoEAD AS \"unidadeensino.dataPublicacaoRenovacaoRecredenciamentoEAD\", unidadeensino.veiculoPublicacaoRenovacaoRecredenciamentoEAD AS \"unidadeensino.veiculoPublicacaoRenovacaoRecredenciamentoEAD\", unidadeensino.secaoPublicacaoRenovacaoRecredenciamentoEAD AS \"unidadeensino.secaoPublicacaoRenovacaoRecredenciamentoEAD\", unidadeensino.paginaPublicacaoRenovacaoRecredenciamentoEAD AS \"unidadeensino.paginaPublicacaoRenovacaoRecredenciamentoEAD\", unidadeensino.numeroDOURenovacaoRecredenciamentoEAD AS \"unidadeensino.numeroDOURenovacaoRecredenciamentoEAD\", unidadeensino.tipoAutorizacaoRenovacaoRecredenciamentoEAD AS \"unidadeensino.tipoAutorizacaoRenovacaoRecredenciamentoEAD\", ");
			str.append(" unidadeensino.credenciamentoEmTramitacao AS \"unidadeensino.credenciamentoEmTramitacao\", unidadeensino.numeroProcessoCredenciamento AS \"unidadeensino.numeroProcessoCredenciamento\", unidadeensino.tipoProcessoCredenciamento AS \"unidadeensino.tipoProcessoCredenciamento\", unidadeensino.dataCadastroCredenciamento AS \"unidadeensino.dataCadastroCredenciamento\", unidadeensino.dataProtocoloCredenciamento AS \"unidadeensino.dataProtocoloCredenciamento\", unidadeensino.recredenciamentoEmTramitacao AS \"unidadeensino.recredenciamentoEmTramitacao\", unidadeensino.numeroProcessoRecredenciamento AS \"unidadeensino.numeroProcessoRecredenciamento\", unidadeensino.tipoProcessoRecredenciamento AS \"unidadeensino.tipoProcessoRecredenciamento\", unidadeensino.dataCadastroRecredenciamento AS \"unidadeensino.dataCadastroRecredenciamento\", unidadeensino.dataProtocoloRecredenciamento AS \"unidadeensino.dataProtocoloRecredenciamento\", unidadeensino.renovacaoRecredenciamentoEmTramitacao AS \"unidadeensino.renovacaoRecredenciamentoEmTramitacao\", unidadeensino.numeroProcessoRenovacaoRecredenciamento AS \"unidadeensino.numeroProcessoRenovacaoRecredenciamento\", unidadeensino.tipoProcessoRenovacaoRecredenciamento AS \"unidadeensino.tipoProcessoRenovacaoRecredenciamento\", unidadeensino.dataCadastroRenovacaoRecredenciamento AS \"unidadeensino.dataCadastroRenovacaoRecredenciamento\", unidadeensino.dataProtocoloRenovacaoRecredenciamento AS \"unidadeensino.dataProtocoloRenovacaoRecredenciamento\", ");
			str.append(" unidadeensino.credenciamentoEadEmTramitacao AS \"unidadeensino.credenciamentoEadEmTramitacao\", unidadeensino.numeroProcessoCredenciamentoEad AS \"unidadeensino.numeroProcessoCredenciamentoEad\", unidadeensino.tipoProcessoCredenciamentoEad AS \"unidadeensino.tipoProcessoCredenciamentoEad\", unidadeensino.dataCadastroCredenciamentoEad AS \"unidadeensino.dataCadastroCredenciamentoEad\", unidadeensino.dataProtocoloCredenciamentoEad AS \"unidadeensino.dataProtocoloCredenciamentoEad\", unidadeensino.recredenciamentoEmTramitacaoEad AS \"unidadeensino.recredenciamentoEmTramitacaoEad\", unidadeensino.numeroProcessoRecredenciamentoEad AS \"unidadeensino.numeroProcessoRecredenciamentoEad\", unidadeensino.tipoProcessoRecredenciamentoEad AS \"unidadeensino.tipoProcessoRecredenciamentoEad\", unidadeensino.dataCadastroRecredenciamentoEad AS \"unidadeensino.dataCadastroRecredenciamentoEad\", unidadeensino.dataProtocoloRecredenciamentoEad AS \"unidadeensino.dataProtocoloRecredenciamentoEad\", unidadeensino.renovacaoRecredenciamentoEmTramitacaoEad AS \"unidadeensino.renovacaoRecredenciamentoEmTramitacaoEad\", unidadeensino.numeroProcessoRenovacaoRecredenciamentoEad AS \"unidadeensino.numeroProcessoRenovacaoRecredenciamentoEad\", unidadeensino.tipoProcessoRenovacaoRecredenciamentoEad AS \"unidadeensino.tipoProcessoRenovacaoRecredenciamentoEad\", unidadeensino.dataCadastroRenovacaoRecredenciamentoEad AS \"unidadeensino.dataCadastroRenovacaoRecredenciamentoEad\", unidadeensino.dataProtocoloRenovacaoRecredenciamentoEad AS \"unidadeensino.dataProtocoloRenovacaoRecredenciamentoEad\", ");
			str.append(" unidadeensino.credenciamentoRegistradoraEmTramitacao AS \"unidadeensino.credenciamentoRegistradoraEmTramitacao\", unidadeensino.numeroProcessoCredenciamentoRegistradora AS \"unidadeensino.numeroProcessoCredenciamentoRegistradora\", unidadeensino.tipoProcessoCredenciamentoRegistradora AS \"unidadeensino.tipoProcessoCredenciamentoRegistradora\", unidadeensino.dataCadastroCredenciamentoRegistradora AS \"unidadeensino.dataCadastroCredenciamentoRegistradora\", unidadeensino.dataProtocoloCredenciamentoRegistradora AS \"unidadeensino.dataProtocoloCredenciamentoRegistradora\", unidadeensino.numeroCredenciamento AS \"unidadeensino.numeroCredenciamento\",unidadeensino.numeroVagaOfertada AS \"unidadeensino.numeroVagaOfertada\", ");
			str.append(" estadoMantenedoraRegistradora.sigla AS \"estadoMantenedoraRegistradora.sigla\", cidadeMantenedoraRegistradora.codigoIBGE AS \"cidadeMantenedoraRegistradora.codigoIBGE\", estadoRegistradora.sigla AS \"estadoRegistradora.sigla\", cidadeRegistradora.codigoIBGE AS \"cidadeRegistradora.codigoIBGE\", unidadeensino.tipoAutorizacaoCredenciamentoRegistradora AS \"unidadeensino.tipoAutorizacaoCredenciamentoRegistradora\", ");
			str.append(" pope.codigo AS \"orientadorPadraoEstagio.codigo\", ");
			str.append(" pope.nome AS \"orientadorPadraoEstagio.nome\", ");
			str.append(" pope.cpf AS \"orientadorPadraoEstagio.cpf\", ");
			str.append(" pope.telefoneRes as \"orientadorPadraoEstagio.telefoneRes\", pope.telefoneComer as \"orientadorPadraoEstagio.telefoneComer\", pope.celular as \"orientadorPadraoEstagio.celular\", ");
			str.append(" pesOperadorResponsavel.codigo AS \"operadorResponsavel.codigo\", ");
			str.append(" pesOperadorResponsavel.nome AS \"operadorResponsavel.nome\", ");
			str.append(" unidadeensino.apresentarHomePreInscricao, unidadeensino.leicriacao1, unidadeensino.leicriacao2, pdg.nome as diretorGeralNome, ");
			str.append(" configuracaocontabil.codigo AS \"configuracaocontabil.codigo\", ");
			str.append(" centroResultado.codigo AS \"centroResultado.codigo\", centroResultado.identificadorCentroResultado AS \"centroResultado.identificadorCentroResultado\", centroResultado.descricao AS \"centroResultado.descricao\", ");
			str.append(" centroResultadoRequerimento.codigo AS \"centroResultadoRequerimento.codigo\", centroResultadoRequerimento.identificadorCentroResultado AS \"centroResultadoRequerimento.identificadorCentroResultado\", centroResultadoRequerimento.descricao AS \"centroResultadoRequerimento.descricao\", ");
			str.append(" configuracaoGED.codigo AS \"configuracaoGED.codigo\", configuracaoGED.nome AS \"configuracaoGED.nome\", configuracaoDiplomaDigital.codigo AS \"configuracaoDiplomaDigital.codigo\", configuracaoDiplomaDigital.descricao AS \"configuracaoDiplomaDigital.descricao\", configuracaoDiplomaDigital.padrao AS \"configuracaoDiplomaDigital.padrao\", ");
			str.append(" p2tcc.codigo AS \"codigoPessoaCoordenadorTCC\", p2tcc.nome AS \"nomeCoordenadorTCC\" ");
			str.append(" FROM unidadeEnsino ");
			str.append(" LEFT JOIN cidade on cidade.codigo = unidadeEnsino.cidade ");
			str.append(" LEFT JOIN estado on estado.codigo = cidade.estado ");
			str.append(" LEFT JOIN funcionario on funcionario.codigo = unidadeEnsino.diretorGeral ");
			str.append(" LEFT JOIN pessoa on pessoa.codigo = funcionario.pessoa ");
			str.append(" LEFT JOIN pessoa p2 on p2.codigo = unidadeEnsino.responsavelCobrancaUnidade ");
			str.append(" LEFT JOIN funcionario frn on frn.codigo = unidadeEnsino.responsavelNotificacaoAlteracaoCronogramaAula ");
			str.append(" LEFT JOIN pessoa p3 on p3.codigo = frn.pessoa ");
			str.append(" LEFT JOIN funcionario f2tcc on f2tcc.codigo = unidadeEnsino.coordenadorTCC ");
			str.append(" LEFT JOIN pessoa p2tcc on p2tcc.codigo = f2tcc.pessoa ");
			str.append(" LEFT JOIN configuracaocontabil on configuracaocontabil.codigo = unidadeEnsino.configuracaocontabil ");
			str.append(" LEFT JOIN centroResultado on centroResultado.codigo = unidadeEnsino.centroResultado ");
			str.append(" LEFT JOIN centroResultado as centroResultadoRequerimento on centroResultadoRequerimento.codigo = unidadeEnsino.centroResultadoRequerimento ");
			str.append(" LEFT JOIN funcionario fdg on fdg.codigo = unidadeEnsino.diretorGeral ");
			str.append(" LEFT JOIN pessoa pdg on pdg.codigo = fdg.pessoa ");
			str.append(" LEFT JOIN funcionario fope on fope.codigo = unidadeEnsino.orientadorPadraoEstagio ");
			str.append(" LEFT JOIN pessoa pope on pope.codigo = fope.pessoa ");
			str.append(" LEFT JOIN configuracaoGED on configuracaoGED.codigo = unidadeEnsino.configuracaoGED  ");
			str.append(" LEFT JOIN funcionario funOperadorResponsavel on funOperadorResponsavel.codigo = unidadeEnsino.operadorResponsavel ");
			str.append(" LEFT JOIN pessoa pesOperadorResponsavel on pesOperadorResponsavel.codigo = funOperadorResponsavel.pessoa ");
			str.append(" LEFT JOIN configuracaoDiplomaDigital on configuracaoDiplomaDigital.codigo = unidadeEnsino.configuracaoDiplomaDigital ");
			str.append(" LEFT JOIN cidade cidadeRegistradora on cidadeRegistradora.codigo = unidadeEnsino.cidadeRegistradora ");
			str.append(" LEFT JOIN cidade cidadeMantenedoraRegistradora on cidadeMantenedoraRegistradora.codigo = unidadeEnsino.cidadeMantenedoraRegistradora ");
			str.append(" LEFT JOIN cidade cidadeMantenedora on cidadeMantenedora.codigo = unidadeEnsino.cidadeMantenedora ");
			str.append(" LEFT JOIN estado estadoMantenedora on estadoMantenedora.codigo = cidadeMantenedora.estado ");
			str.append(" LEFT JOIN estado estadoMantenedoraRegistradora on estadoMantenedoraRegistradora.codigo = cidadeMantenedoraRegistradora.estado ");
			str.append(" LEFT JOIN estado estadoRegistradora on estadoRegistradora.codigo = cidadeRegistradora.estado ");
			str.append(" WHERE UnidadeEnsino.codigo = ").append(obj.getCodigo());
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(str.toString());
			if (!tabelaResultado.next()) {
				throw new ConsistirException("Dados Não Encontrados.");
			}
			montarDadosCompleto(obj, tabelaResultado, true, usuarioVO);
		}
	}

	
}