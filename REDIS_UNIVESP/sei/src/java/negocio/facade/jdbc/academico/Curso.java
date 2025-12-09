package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import negocio.comuns.academico.*;
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

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.enumeradores.BimestreEnum;
import negocio.comuns.academico.enumeradores.ModalidadeDisciplinaEnum;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.academico.enumeradores.TipoAutorizacaoCursoEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.estagio.GrupoPessoaVO;
import negocio.comuns.financeiro.CondicaoRenegociacaoUnidadeEnsinoVO;
import negocio.comuns.pesquisa.AreaConhecimentoVO;
import negocio.comuns.processosel.ProcSeletivoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.academico.CursoInterfaceFacade;
import webservice.servicos.CursoObject;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>CursoVO</code>. Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>CursoVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see CursoVO
 * @see ControleAcesso
 */
@SuppressWarnings({ "unchecked", "serial", "rawtypes" })
@Repository
@Scope("singleton")
@Lazy
public class Curso extends ControleAcesso implements CursoInterfaceFacade {

	protected static String idEntidade;

	public Curso() throws Exception {
		super();
		setIdEntidade("Curso");
	}

	public CursoVO novo() throws Exception {
		Curso.incluir(getIdEntidade());
		CursoVO obj = new CursoVO();
		return obj;
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe <code>CursoVO</code>. Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>incluir</code> da superclasse.
	 *
	 * @param obj
	 *            Objeto da classe <code>CursoVO</code> que será gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final CursoVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
		try {
			CursoVO.validarDados(obj);
			Curso.incluir(getIdEntidade(), usuario);
			final StringBuilder sql = new StringBuilder("INSERT INTO Curso ( nome, objetivos, descricao, publicoAlvo, perfilEgresso, ")
					.append("nivelEducacional, nrPeriodoLetivo, dataPublicacaoDO, nrRegistroInterno, dataCriacao, ")
					.append("periodicidade, regimeAprovacao, regime, areaConhecimento, configuracaoAcademico, ")
					.append("idcursoinep, habilitacao, titulo, abreviatura, titulacaoDoFormando, ")
					.append("limitardiasdownload, qtdediaslimitedownload, funcionarioRespPreInscricao, quantidadeDisciplinasOptativasExpedicaoDiploma, liberarRegistroAulaEntrePeriodo, ")
					.append("apresentarHomePreInscricao, configuracaoTCC, titulacaoDoFormandoFeminino, percentualEvolucaoAcademicaIngressanteEnade, percentualEvolucaoAcademicaConcluinteEnade, ")
					.append("anoMudancaLegislacao, preposicaoNomeCurso, nomeDocumentacao, titulacaoMasculinoApresentarDiploma, titulacaoFemininoApresentarDiploma, ")
					.append("codigoContabil, nomeContabil, dataultimaalteracao, apresentarCursoBiblioteca, nivelContabil, modalidadeCurso, utilizarRecursoAvaTerceiros, questionario, ")
					.append(" textoDeclaracaoPPI , textoDeclaracaoBolsasAuxilios , textoDeclaracaoEscolaridadePublica , textoConfirmacaoNovaMatricula , urlDeclaracaoNormasMatricula , permitirAssinarContratoPendenciaDocumentacao ,   ativarPreMatriculaAposEntregaDocumentosObrigatorios , ativarMatriculaAposAssinaturaContrato ,")
					.append(" grupoPessoaAnaliseRelatorioFinalEstagio, grupoPessoaAnaliseAproveitamentoEstagioObrigatorio, grupoPessoaAnaliseEquivalenciaEstagioObrigatorio, funcionarioResponsavelAssinaturaTermoEstagio, idConteudoMasterBlackboardEstagio, configuracaoldap, eixocurso ,textoPadraoContratoMatriculaCalouro , ")
					.append(" permitirOutraMatriculaMesmoAluno, autorizacaoResolucaoEmTramitacao, numeroProcessoAutorizacaoResolucao, tipoProcessoAutorizacaoResolucao, dataCadastroAutorizacaoResolucao, dataProtocoloAutorizacaoResolucao, dataHabilitacao, possuiCodigoEMEC, codigoEMEC, numeroProcessoEMEC, tipoProcessoEMEC, dataCadastroEMEC, dataProtocoloEMEC, tipoAutorizacaoCursoEnum, numeroAutorizacao, dataCredenciamento, veiculoPublicacao, secaoPublicacao, paginaPublicacao, numeroDOU ")
					.append(") VALUES ( ?, ?, ?, ?, ?, ")
					.append("?, ?, ?, ?, ?, ")
					.append("?, ?, ?, ?, ?, ")
					.append("?, ?, ?, ?, ?, ")
					.append("?, ?, ?, ?, ?, ")
					.append("?, ?, ?, ?, ?, ")
					.append("?, ?, ?, ?, ?, ")
					.append("?, ?, now(), ?, ?,")
					.append("?, ?, ?, ?, ?, ")
					.append("?, ?, ?, ?, ?, ?, ")
					.append("?, ?, ? ,? ,?, ? ,")
					.append("?, ? ,?, ")
					.append("?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ")
			        .append(") returning codigo").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));

			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
					sqlInserir.setString(1, obj.getNome());
					sqlInserir.setString(2, obj.getObjetivos());
					sqlInserir.setString(3, obj.getDescricao());
					sqlInserir.setString(4, obj.getPublicoAlvo());
					sqlInserir.setString(5, obj.getPerfilEgresso());
					sqlInserir.setString(6, obj.getNivelEducacional());
					// sqlInserir.setString(7, obj.getBaseLegal());
					sqlInserir.setInt(7, obj.getNrPeriodoLetivo().intValue());
					sqlInserir.setDate(8, Uteis.getDataJDBC(obj.getDataPublicacaoDO()));
					sqlInserir.setString(9, obj.getNrRegistroInterno());
					sqlInserir.setDate(10, Uteis.getDataJDBC(obj.getDataCriacao()));
					sqlInserir.setString(11, obj.getPeriodicidade());
					sqlInserir.setString(12, obj.getRegimeAprovacao());
					sqlInserir.setString(13, obj.getRegime());
					sqlInserir.setInt(14, obj.getAreaConhecimento().getCodigo().intValue());
					sqlInserir.setInt(15, obj.getConfiguracaoAcademico().getCodigo().intValue());
					if (obj.getIdCursoInep() != null && obj.getIdCursoInep().intValue() != 0) {
						sqlInserir.setInt(16, obj.getIdCursoInep());
					} else {
						sqlInserir.setInt(16, 0);
					}
					sqlInserir.setString(17, obj.getHabilitacao());
					sqlInserir.setString(18, obj.getTitulo());
					sqlInserir.setString(19, obj.getAbreviatura().trim());
					sqlInserir.setString(20, obj.getTitulacaoDoFormando());
					sqlInserir.setBoolean(21, obj.getLimitarQtdeDiasMaxDownload());
					sqlInserir.setInt(22, obj.getQtdeMaxDiasDownload().intValue());
					sqlInserir.setInt(23, obj.getFuncionarioRespPreInscricao().getCodigo().intValue());
					sqlInserir.setInt(24, obj.getQuantidadeDisciplinasOptativasExpedicaoDiploma());
					sqlInserir.setBoolean(25, obj.getLiberarRegistroAulaEntrePeriodo());
					sqlInserir.setBoolean(26, obj.getApresentarHomePreInscricao());
					if (obj.getConfiguracaoTCC() != null && obj.getConfiguracaoTCC().getCodigo().intValue() != 0) {
						sqlInserir.setInt(27, obj.getConfiguracaoTCC().getCodigo());
					} else {
						sqlInserir.setNull(27, 0);
					}
					sqlInserir.setString(28, obj.getTitulacaoDoFormandoFeminino());
					sqlInserir.setInt(29, obj.getPercentualEvolucaoAcademicaIngressanteEnade());
					sqlInserir.setInt(30, obj.getPercentualEvolucaoAcademicaConcluinteEnade());
					sqlInserir.setString(31, obj.getAnoMudancaLegislacao());
					sqlInserir.setString(32, obj.getPreposicaoNomeCurso());
					sqlInserir.setString(33, obj.getNomeDocumentacao());
					sqlInserir.setString(34, obj.getTitulacaoMasculinoApresentarDiploma());
					sqlInserir.setString(35, obj.getTitulacaoFemininoApresentarDiploma());
					sqlInserir.setString(36, obj.getCodigoContabil());
					sqlInserir.setString(37, obj.getNomeContabil());
					sqlInserir.setBoolean(38, obj.getApresentarCursoBiblioteca());
					sqlInserir.setString(39, obj.getNivelContabil());
					sqlInserir.setString(40, obj.getModalidadeCurso().name());
					sqlInserir.setBoolean(41, obj.getUtilizarRecursoAvaTerceiros());
					if(Uteis.isAtributoPreenchido(obj.getQuestionarioVO().getCodigo())) {
						sqlInserir.setInt(42, obj.getQuestionarioVO().getCodigo());
					}else {
						sqlInserir.setNull(42, 0);
					}
					sqlInserir.setString(43, obj.getTextoDeclaracaoPPI());
					sqlInserir.setString(44, obj.getTextoDeclaracaoBolsasAuxilios());
					sqlInserir.setString(45, obj.getTextoDeclaracaoEscolaridadePublica());
					sqlInserir.setString(46, obj.getTextoConfirmacaoNovaMatricula());
					sqlInserir.setString(47, obj.getUrlDeclaracaoNormasMatricula());
					sqlInserir.setBoolean(48, obj.getPermitirAssinarContratoPendenciaDocumentacao());
				    sqlInserir.setBoolean(49, obj.getAtivarPreMatriculaAposEntregaDocumentosObrigatorios());
				    sqlInserir.setBoolean(50, obj.getAtivarMatriculaAposAssinaturaContrato());
					int i = 50;
					Uteis.setValuePreparedStatement(obj.getGrupoPessoaAnaliseRelatorioFinalEstagioVO(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getGrupoPessoaAnaliseAproveitamentoEstagioObrigatorioVO(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getGrupoPessoaAnaliseEquivalenciaEstagioObrigatorioVO(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getFuncionarioResponsavelAssinaturaTermoEstagioVO(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getIdConteudoMasterBlackboardEstagio(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getConfiguracaoLdapVO(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getEixoCursoVO(), ++i, sqlInserir);	
					Uteis.setValuePreparedStatement(obj.getTextoPadraoContratoMatriculaCalouro(), ++i, sqlInserir);		
					Uteis.setValuePreparedStatement(obj.getPermitirOutraMatriculaMesmoAluno(), ++i, sqlInserir);
					sqlInserir.setBoolean(++i, obj.getAutorizacaoResolucaoEmTramitacao());
				    if (obj.getAutorizacaoResolucaoEmTramitacao()) {
				    	sqlInserir.setString(++i, obj.getNumeroProcessoAutorizacaoResolucao());
				    	sqlInserir.setString(++i, obj.getTipoProcessoAutorizacaoResolucao());
				    	sqlInserir.setDate(++i, Uteis.getDataJDBC(obj.getDataCadastroAutorizacaoResolucao()));
				    	sqlInserir.setDate(++i, Uteis.getDataJDBC(obj.getDataProtocoloAutorizacaoResolucao()));
				    } else {
				    	sqlInserir.setNull(++i, 0);
				    	sqlInserir.setNull(++i, 0);
				    	sqlInserir.setNull(++i, 0);
				    	sqlInserir.setNull(++i, 0);
				    }
				    sqlInserir.setDate(++i, Uteis.getDataJDBC(obj.getDataHabilitacao()));
				    sqlInserir.setBoolean(++i, obj.getPossuiCodigoEMEC());
					sqlInserir.setInt(++i, obj.getCodigoEMEC());
					sqlInserir.setInt(++i, obj.getNumeroProcessoEMEC());
					sqlInserir.setString(++i, obj.getTipoProcessoEMEC());
					if (Uteis.isAtributoPreenchido(obj.getDataCadastroEMEC())) {
						sqlInserir.setDate(++i, Uteis.getDataJDBC(obj.getDataCadastroEMEC()));
					} else {
						sqlInserir.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getDataProtocoloEMEC())) {
						sqlInserir.setDate(++i, Uteis.getDataJDBC(obj.getDataProtocoloEMEC()));
					} else {
						sqlInserir.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getTipoAutorizacaoCursoEnum())) {
						sqlInserir.setString(++i, obj.getTipoAutorizacaoCursoEnum().name());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					sqlInserir.setString(++i, obj.getNumeroAutorizacao());
					if (obj.getDataCredenciamento() != null) {
						sqlInserir.setDate(++i, Uteis.getDataJDBC(obj.getDataCredenciamento()));
					} else {
						sqlInserir.setNull(++i, 0);
					}
					sqlInserir.setString(++i, obj.getVeiculoPublicacao());
					sqlInserir.setInt(++i, obj.getSecaoPublicacao());
					sqlInserir.setInt(++i, obj.getPaginaPublicacao());
					sqlInserir.setInt(++i, obj.getNumeroDOU());
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

			getFacadeFactory().getDocumentacaoCursoFacade().incluirDocumentacaoCursos(obj.getCodigo(), obj.getDocumentacaoCursoVOs(), usuario);
			getFacadeFactory().getMaterialCursoFacade().incluirMaterialCursos(obj.getCodigo(), obj.getMaterialCursoVOs(), usuario, configuracaoGeralSistema);
			getFacadeFactory().getCursoTurnoFacade().incluirCursoTurnos(obj.getCodigo(), obj.getCursoTurnoVOs(), usuario);
			getFacadeFactory().getAutorizacaoCursoFacade().validarDadosAutorizacaoCurso(obj.getAutorizacaoCursoVOs(), obj.getAutorizacaoCursoVOs());
			getFacadeFactory().getAutorizacaoCursoFacade().realizarAtualizacaoAutorizacaoCursoVOs(obj.getAutorizacaoCursoVOs(), obj.getCodigo(), usuario);
			if (!obj.getCursoCoordenadorVOs().isEmpty()) {
				getFacadeFactory().getCursoCoordenadorFacade().gravarCursoCoordenadorVOs(obj, usuario);
			}
			// incluir o template.
			if (obj.getHabilitarMensagemNotificacaoNovaMatricula()) {
				obj.getMensagemConfirmacaoNovaMatricula().getCursoVO().setCodigo(obj.getCodigo());
				getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().incluir(obj.getMensagemConfirmacaoNovaMatricula(), usuario);
			}
			if (obj.getHabilitarMensagemNotificacaoRenovacaoMatricula()) {
				obj.getMensagemRenovacaoMatricula().getCursoVO().setCodigo(obj.getCodigo());
				getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().incluir(obj.getMensagemRenovacaoMatricula(), usuario);
			}
			if (obj.getHabilitarMensagemNotificacaoAtivacaoPreMatriculaEntregaDocumento()) {
				obj.getMensagemAtivacaoPreMatriculaEntregaDocumento().getCursoVO().setCodigo(obj.getCodigo());
				getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().incluir(obj.getMensagemAtivacaoPreMatriculaEntregaDocumento(), usuario);
			}
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}


	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe <code>CursoVO</code>. Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado. Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>alterar</code> da superclasse.
	 *
	 * @param obj
	 *            Objeto da classe <code>CursoVO</code> que será alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final CursoVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
		try {
			CursoVO.validarDados(obj);
			Curso.alterar(getIdEntidade(), usuario);
			final String sql = "UPDATE Curso set nome=?, objetivos=?, descricao=?, publicoAlvo=?, perfilEgresso=?, nivelEducacional=?, nrPeriodoLetivo=?, " + "dataPublicacaoDO=?, nrRegistroInterno=?, dataCriacao=?, periodicidade=?, " + "regimeAprovacao=?, regime=?, areaConhecimento=?, configuracaoAcademico=?, eixocurso=?, " + "idcursoinep=?, habilitacao=?, titulo=?, abreviatura=?, titulacaoDoFormando=?, limitardiasdownload=?, qtdediaslimitedownload=?, funcionarioRespPreInscricao=?, quantidadeDisciplinasOptativasExpedicaoDiploma=?, " + "liberarRegistroAulaEntrePeriodo=?, apresentarHomePreInscricao=?, configuracaoTCC=?, titulacaoDoFormandoFeminino = ?, percentualEvolucaoAcademicaIngressanteEnade=?, percentualEvolucaoAcademicaConcluinteEnade=?, anoMudancaLegislacao = ?, " + "preposicaoNomeCurso=?, nomeDocumentacao=?, titulacaoMasculinoApresentarDiploma=?, titulacaoFemininoApresentarDiploma=?, " + "codigocontabil=?, nomecontabil=?, dataultimaalteracao = now(), apresentarCursoBiblioteca=?, nivelContabil=?, modalidadeCurso=?, utilizarRecursoAvaTerceiros=?, questionario=?, textoDeclaracaoPPI=? , textoDeclaracaoBolsasAuxilios=? , textoDeclaracaoEscolaridadePublica=? , textoConfirmacaoNovaMatricula=? , urlDeclaracaoNormasMatricula=? , 	permitirAssinarContratoPendenciaDocumentacao=? ,  ativarPreMatriculaAposEntregaDocumentosObrigatorios=? ,  ativarMatriculaAposAssinaturaContrato=? , grupoPessoaAnaliseRelatorioFinalEstagio=?, grupoPessoaAnaliseAproveitamentoEstagioObrigatorio=?, grupoPessoaAnaliseEquivalenciaEstagioObrigatorio=?, funcionarioResponsavelAssinaturaTermoEstagio=?, configuracaoldap=? , idConteudoMasterBlackboardEstagio=? , textoPadraoContratoMatriculaCalouro=? ,permitirOutraMatriculaMesmoAluno=?, autorizacaoResolucaoEmTramitacao=?, numeroProcessoAutorizacaoResolucao=?, tipoProcessoAutorizacaoResolucao=?, dataCadastroAutorizacaoResolucao=?, dataProtocoloAutorizacaoResolucao=?, dataHabilitacao=?, possuiCodigoEMEC=?, codigoEMEC=?, numeroProcessoEMEC=?, tipoProcessoEMEC=?, dataCadastroEMEC=?, dataProtocoloEMEC=?, tipoAutorizacaoCursoEnum=?, numeroAutorizacao=?, dataCredenciamento=?, veiculoPublicacao=?, secaoPublicacao=?, paginaPublicacao=?, numeroDOU=?   WHERE ((codigo = ?))"
					+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getNome());
					sqlAlterar.setString(2, obj.getObjetivos());
					sqlAlterar.setString(3, obj.getDescricao());
					sqlAlterar.setString(4, obj.getPublicoAlvo());
					sqlAlterar.setString(5, obj.getPerfilEgresso());
					sqlAlterar.setString(6, obj.getNivelEducacional());
					sqlAlterar.setInt(7, obj.getNrPeriodoLetivo().intValue());
					sqlAlterar.setDate(8, Uteis.getDataJDBC(obj.getDataPublicacaoDO()));
					sqlAlterar.setString(9, obj.getNrRegistroInterno());
					sqlAlterar.setDate(10, Uteis.getDataJDBC(obj.getDataCriacao()));
					sqlAlterar.setString(11, obj.getPeriodicidade());
					sqlAlterar.setString(12, obj.getRegimeAprovacao());
					sqlAlterar.setString(13, obj.getRegime());
					sqlAlterar.setInt(14, obj.getAreaConhecimento().getCodigo().intValue());
					sqlAlterar.setInt(15, obj.getConfiguracaoAcademico().getCodigo().intValue());
					if(Uteis.isAtributoPreenchido(obj.getEixoCursoVO())) {
						sqlAlterar.setInt(16, obj.getEixoCursoVO().getCodigo().intValue());
					}else {
						sqlAlterar.setNull(16, 0);
					}
					if (obj.getIdCursoInep() != null && obj.getIdCursoInep().intValue() != 0) {
						sqlAlterar.setInt(17, obj.getIdCursoInep());
					} else {
						sqlAlterar.setInt(17, 0);
					}
					sqlAlterar.setString(18, obj.getHabilitacao());
					sqlAlterar.setString(19, obj.getTitulo());
					sqlAlterar.setString(20, obj.getAbreviatura().trim());
					sqlAlterar.setString(21, obj.getTitulacaoDoFormando());
					sqlAlterar.setBoolean(22, obj.getLimitarQtdeDiasMaxDownload());
					sqlAlterar.setInt(23, obj.getQtdeMaxDiasDownload().intValue());
					sqlAlterar.setInt(24, obj.getFuncionarioRespPreInscricao().getCodigo().intValue());
					sqlAlterar.setInt(25, obj.getQuantidadeDisciplinasOptativasExpedicaoDiploma());
					sqlAlterar.setBoolean(26, obj.getLiberarRegistroAulaEntrePeriodo());
					sqlAlterar.setBoolean(27, obj.getApresentarHomePreInscricao());
					if (obj.getConfiguracaoTCC() != null && obj.getConfiguracaoTCC().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(28, obj.getConfiguracaoTCC().getCodigo());
					} else {
						sqlAlterar.setNull(28, 0);
					}
					sqlAlterar.setString(29, obj.getTitulacaoDoFormandoFeminino());                    
					sqlAlterar.setInt(30, obj.getPercentualEvolucaoAcademicaIngressanteEnade());
					sqlAlterar.setInt(31, obj.getPercentualEvolucaoAcademicaConcluinteEnade());
					sqlAlterar.setString(32, obj.getAnoMudancaLegislacao());
					sqlAlterar.setString(33, obj.getPreposicaoNomeCurso());
					sqlAlterar.setString(34, obj.getNomeDocumentacao());
					sqlAlterar.setString(35, obj.getTitulacaoMasculinoApresentarDiploma());
					sqlAlterar.setString(36, obj.getTitulacaoFemininoApresentarDiploma());
					sqlAlterar.setString(37, obj.getCodigoContabil());
					sqlAlterar.setString(38, obj.getNomeContabil());
					sqlAlterar.setBoolean(39, obj.getApresentarCursoBiblioteca());
					sqlAlterar.setString(40, obj.getNivelContabil());
					sqlAlterar.setString(41, obj.getModalidadeCurso().name());
					sqlAlterar.setBoolean(42, obj.getUtilizarRecursoAvaTerceiros());
					if(Uteis.isAtributoPreenchido(obj.getQuestionarioVO().getCodigo())) {
						sqlAlterar.setInt(43, obj.getQuestionarioVO().getCodigo());
					}else {
						sqlAlterar.setNull(43, 0);
					}
					sqlAlterar.setString(44, obj.getTextoDeclaracaoPPI());
					sqlAlterar.setString(45, obj.getTextoDeclaracaoBolsasAuxilios());
					sqlAlterar.setString(46, obj.getTextoDeclaracaoEscolaridadePublica());
					sqlAlterar.setString(47, obj.getTextoConfirmacaoNovaMatricula());
					sqlAlterar.setString(48, obj.getUrlDeclaracaoNormasMatricula());
					sqlAlterar.setBoolean(49, obj.getPermitirAssinarContratoPendenciaDocumentacao());
					sqlAlterar.setBoolean(50, obj.getAtivarPreMatriculaAposEntregaDocumentosObrigatorios());
					sqlAlterar.setBoolean(51, obj.getAtivarMatriculaAposAssinaturaContrato());
					int i = 51;
					Uteis.setValuePreparedStatement(obj.getGrupoPessoaAnaliseRelatorioFinalEstagioVO(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getGrupoPessoaAnaliseAproveitamentoEstagioObrigatorioVO(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getGrupoPessoaAnaliseEquivalenciaEstagioObrigatorioVO(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getFuncionarioResponsavelAssinaturaTermoEstagioVO(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getConfiguracaoLdapVO(), ++i, sqlAlterar);								
					Uteis.setValuePreparedStatement(obj.getIdConteudoMasterBlackboardEstagio(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getTextoPadraoContratoMatriculaCalouro(), ++i, sqlAlterar);	
					Uteis.setValuePreparedStatement(obj.getPermitirOutraMatriculaMesmoAluno(), ++i, sqlAlterar);
					sqlAlterar.setBoolean(++i, obj.getAutorizacaoResolucaoEmTramitacao());
				    if (obj.getAutorizacaoResolucaoEmTramitacao()) {
				    	sqlAlterar.setString(++i, obj.getNumeroProcessoAutorizacaoResolucao());
				    	sqlAlterar.setString(++i, obj.getTipoProcessoAutorizacaoResolucao());
				    	sqlAlterar.setDate(++i, Uteis.getDataJDBC(obj.getDataCadastroAutorizacaoResolucao()));
				    	sqlAlterar.setDate(++i, Uteis.getDataJDBC(obj.getDataProtocoloAutorizacaoResolucao()));
				    } else {
				    	sqlAlterar.setNull(++i, 0);
				    	sqlAlterar.setNull(++i, 0);
				    	sqlAlterar.setNull(++i, 0);
				    	sqlAlterar.setNull(++i, 0);
				    }
				    sqlAlterar.setDate(++i, Uteis.getDataJDBC(obj.getDataHabilitacao()));
				    sqlAlterar.setBoolean(++i, obj.getPossuiCodigoEMEC());
					sqlAlterar.setInt(++i, obj.getCodigoEMEC());
					sqlAlterar.setInt(++i, obj.getNumeroProcessoEMEC());
					sqlAlterar.setString(++i, obj.getTipoProcessoEMEC());
					if (Uteis.isAtributoPreenchido(obj.getDataCadastroEMEC())) {
						sqlAlterar.setDate(++i, Uteis.getDataJDBC(obj.getDataCadastroEMEC()));
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getDataProtocoloEMEC())) {
						sqlAlterar.setDate(++i, Uteis.getDataJDBC(obj.getDataProtocoloEMEC()));
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getTipoAutorizacaoCursoEnum())) {
						sqlAlterar.setString(++i, obj.getTipoAutorizacaoCursoEnum().name());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					sqlAlterar.setString(++i, obj.getNumeroAutorizacao());
					if (obj.getDataCredenciamento() != null) {
						sqlAlterar.setDate(++i, Uteis.getDataJDBC(obj.getDataCredenciamento()));
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					sqlAlterar.setString(++i, obj.getVeiculoPublicacao());
					if (obj.getSecaoPublicacao() != null) {
						sqlAlterar.setInt(++i, obj.getSecaoPublicacao());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (obj.getPaginaPublicacao() != null) {
						sqlAlterar.setInt(++i, obj.getPaginaPublicacao());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (obj.getNumeroDOU() != null) {
						sqlAlterar.setInt(++i, obj.getNumeroDOU());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);
					return sqlAlterar;
				}
			});

			getFacadeFactory().getDocumentacaoCursoFacade().alterarDocumentacaoCursos(obj.getCodigo(), obj.getDocumentacaoCursoVOs(), usuario);
			getFacadeFactory().getMaterialCursoFacade().alterarMaterialCursos(obj.getCodigo(), obj.getMaterialCursoVOs(), usuario, configuracaoGeralSistema);
			getFacadeFactory().getCursoTurnoFacade().alterarCursoTurnos(obj.getCodigo(), obj.getCursoTurnoVOs(), usuario);
			getFacadeFactory().getAutorizacaoCursoFacade().validarDadosAutorizacaoCurso(obj.getAutorizacaoCursoVOs(), obj.getAutorizacaoCursoVOs());
			getFacadeFactory().getAutorizacaoCursoFacade().realizarAtualizacaoAutorizacaoCursoVOs(obj.getAutorizacaoCursoVOs(), obj.getCodigo(), usuario);
			if (!obj.getCursoCoordenadorVOs().isEmpty()) {
				getFacadeFactory().getCursoCoordenadorFacade().gravarCursoCoordenadorVOs(obj, usuario);
			}
			/// mensagem personalizada
			if (obj.getHabilitarMensagemNotificacaoNovaMatricula()) {
				obj.getMensagemConfirmacaoNovaMatricula().getCursoVO().setCodigo(obj.getCodigo());
				if (Uteis.isAtributoPreenchido(obj.getMensagemConfirmacaoNovaMatricula())) {
					getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().alterar(obj.getMensagemConfirmacaoNovaMatricula(), usuario);
				} else {
					getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().incluir(obj.getMensagemConfirmacaoNovaMatricula(), usuario);
				}
//				aqui verifica se altera ou inclui
			} else if (Uteis.isAtributoPreenchido(obj.getMensagemConfirmacaoNovaMatricula()) && Uteis.isAtributoPreenchido(obj.getMensagemConfirmacaoNovaMatricula().getCursoVO())) {
				// exclusao da mensagem
				getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().excluirPorCursoTampleteMensagemAutomatica(obj.getMensagemConfirmacaoNovaMatricula(), usuario);
			}
			if (obj.getHabilitarMensagemNotificacaoRenovacaoMatricula()) {
				obj.getMensagemRenovacaoMatricula().getCursoVO().setCodigo(obj.getCodigo());
				if (Uteis.isAtributoPreenchido(obj.getMensagemRenovacaoMatricula())) {
					getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().alterar(obj.getMensagemRenovacaoMatricula(), usuario);
				} else {
					getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().incluir(obj.getMensagemRenovacaoMatricula(), usuario);
				}
//				aqui verifica se altera ou inclui
			} else if (Uteis.isAtributoPreenchido(obj.getMensagemRenovacaoMatricula()) && Uteis.isAtributoPreenchido(obj.getMensagemRenovacaoMatricula().getCursoVO())) {
				// exclusao da mensagem
				getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().excluirPorCursoTampleteMensagemAutomatica(obj.getMensagemRenovacaoMatricula(), usuario);
			}
			if (obj.getHabilitarMensagemNotificacaoAtivacaoPreMatriculaEntregaDocumento()) {
				obj.getMensagemAtivacaoPreMatriculaEntregaDocumento().getCursoVO().setCodigo(obj.getCodigo());
				if (!Uteis.isAtributoPreenchido(obj.getMensagemAtivacaoPreMatriculaEntregaDocumento())) {
					getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().incluir(obj.getMensagemAtivacaoPreMatriculaEntregaDocumento(), usuario);
				}else {
					getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().alterar(obj.getMensagemAtivacaoPreMatriculaEntregaDocumento(), usuario);
				}
			} else if (Uteis.isAtributoPreenchido(obj.getMensagemAtivacaoPreMatriculaEntregaDocumento()) && Uteis.isAtributoPreenchido(obj.getMensagemAtivacaoPreMatriculaEntregaDocumento().getCursoVO())) {
				// exclusao da mensagem
				getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().excluirPorCursoTampleteMensagemAutomatica(obj.getMensagemAtivacaoPreMatriculaEntregaDocumento(), usuario);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe <code>CursoVO</code>. Sempre localiza o registro a ser excluído através da chave primária da entidade. Primeiramente verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>excluir</code> da superclasse.
	 *
	 * @param obj
	 *            Objeto da classe <code>CursoVO</code> que será removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(CursoVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
		try {
			Curso.excluir(getIdEntidade(), usuario);
			Iterator i = obj.getGradeCurricularVOs().iterator();
			while (i.hasNext()) {
				GradeCurricularVO objs = (GradeCurricularVO) i.next();
				getFacadeFactory().getGradeCurricularFacade().excluir(objs, usuario);
			}
			String sql = "DELETE FROM Curso WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });

			getFacadeFactory().getDocumentacaoCursoFacade().excluirDocumentacaoCursos(obj.getCodigo(), usuario);
			getFacadeFactory().getMaterialCursoFacade().excluirMaterialCursos(obj.getCodigo(), usuario, configuracaoGeralSistema);
			getFacadeFactory().getCursoTurnoFacade().excluirCursoTurnos(obj.getCodigo(), usuario);
			getFacadeFactory().getCursoCoordenadorFacade().excluirCursoCoordenador(obj.getCodigo(), usuario);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Responsável por realizar uma consulta de <code>Curso</code> através do valor do atributo <code>nome</code> da classe <code>AreaConhecimento</code> Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 *
	 * @return List Contendo vários objetos da classe <code>CursoVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorNomeAreaConhecimento(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true, usuario);
		String sqlStr = "SELECT Curso.* FROM Curso, AreaConhecimento WHERE Curso.areaConhecimento = AreaConhecimento.codigo and lower (AreaConhecimento.nome) like('" + valorConsulta.toLowerCase() + "%') ORDER BY AreaConhecimento.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	@Override
	public List consultarPorNomeAreaConhecimentoNivelEducacional(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, String nivelEducacional, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true, usuario);
		String sqlStr = "SELECT Curso.* FROM Curso, AreaConhecimento WHERE Curso.areaConhecimento = AreaConhecimento.codigo and lower (AreaConhecimento.nome) like('" + valorConsulta.toLowerCase() + "%') AND Curso.niveleducacional = '" + nivelEducacional + "' ORDER BY AreaConhecimento.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/**
	 * Responsável por realizar uma consulta de <code>Curso</code> através do valor do atributo <code>String nrRegistroInterno</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 *
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>CursoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorNrRegistroInterno(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Curso WHERE lower (nrRegistroInterno) like('" + valorConsulta.toLowerCase() + "%') ORDER BY nrRegistroInterno";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<CursoVO> consultarPorCodigoProfessor(Integer professor, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "	select curso.* from curso " + "left join gradecurricular on curso.codigo = gradecurricular.curso " + "left join turma on turma.gradecurricular = gradecurricular.codigo " + "left join horarioturmaprofessordisciplina on horarioturmaprofessordisciplina.turma = turma.codigo " + "left join pessoa on pessoa.codigo = horarioturmaprofessordisciplina.professor " + "where pessoa.codigo = " + professor.intValue();
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List consultarPorNrNivelEducacional(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Curso WHERE lower (nivelEducacional) like('" + valorConsulta.toLowerCase() + "%') ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<CursoVO> consultaCursoDoProfessor(Integer professor, Integer unidadeEnsino, boolean consultarTurmasEAD, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = new StringBuilder("select distinct Curso.* from turma ");
		sql.append("INNER JOIN curso on turma.curso = curso.codigo ");
		sql.append("INNER JOIN horarioTurmaProfessorDisciplina htpd on turma.codigo = htpd.turma ");
		sql.append("INNER JOIN horarioturma ON horarioturma.codigo = htpd.horarioturma ");
		sql.append("WHERE htpd.professor = ").append(professor).append(" ");
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sql.append(" AND turma.unidadeEnsino = ").append(unidadeEnsino).append(" ");
		}
		if (consultarTurmasEAD) {
			sql.append(" union");
			sql.append(" select distinct curso.* from programacaotutoriaonline ");
			sql.append(" inner join turmadisciplina on turmadisciplina.disciplina = programacaotutoriaonline.disciplina and turmadisciplina.definicoestutoriaonline = 'DINAMICA' ");
			sql.append(" inner join turma on turma.codigo = turmadisciplina.turma ");
			sql.append(" inner join curso on ((turma.turmaagrupada = false and curso.codigo = turma.curso) or (turma.turmaagrupada and curso.codigo in (select t.curso from turmaagrupada inner join turma as t on t.codigo = turmaagrupada.turma where turmaagrupada.turmaorigem = turma.codigo  )))   ");
			sql.append(" inner join programacaotutoriaonlineprofessor on programacaotutoriaonlineprofessor.programacaotutoriaonline = programacaotutoriaonline.codigo ");
			sql.append(" where  programacaotutoriaonlineprofessor.professor =  ").append(professor);
			sql.append(" and (programacaotutoriaonline.unidadeensino is null or programacaotutoriaonline.unidadeensino = ").append(unidadeEnsino).append(" )");
			sql.append(" and programacaotutoriaonline.turma is null and programacaotutoriaonline.curso is null ");
			if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
				sql.append(" AND turma.unidadeEnsino = ").append(unidadeEnsino).append(" ");
			}

			sql.append(" union");
			sql.append(" select distinct curso.* from programacaotutoriaonline");
			sql.append(" inner join turma on turma.codigo = programacaotutoriaonline.turma");
			sql.append(" inner join curso on ((turma.turmaagrupada = false and curso.codigo = turma.curso) or (turma.turmaagrupada and curso.codigo in (select t.curso from turmaagrupada inner join turma as t on t.codigo = turmaagrupada.turma where turmaagrupada.turmaorigem = turma.codigo  )))   ");
			sql.append(" inner join programacaotutoriaonlineprofessor on programacaotutoriaonlineprofessor.programacaotutoriaonline = programacaotutoriaonline.codigo");
			sql.append(" where  programacaotutoriaonlineprofessor.professor =  ").append(professor);
			sql.append(" and (programacaotutoriaonline.unidadeensino is null or programacaotutoriaonline.unidadeensino = ").append(unidadeEnsino).append(" )");
			sql.append(" and programacaotutoriaonline.turma is not null  and programacaotutoriaonline.curso is null ");
			if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
				sql.append(" AND turma.unidadeEnsino = ").append(unidadeEnsino).append(" ");
			}

			sql.append(" union");
			sql.append(" select distinct curso.* from programacaotutoriaonline");
			sql.append(" inner join curso on curso.codigo = programacaotutoriaonline.curso");
			sql.append(" inner join programacaotutoriaonlineprofessor on programacaotutoriaonlineprofessor.programacaotutoriaonline = programacaotutoriaonline.codigo");
			sql.append(" where  programacaotutoriaonlineprofessor.professor =  ").append(professor);
			sql.append(" and (programacaotutoriaonline.unidadeensino is null or programacaotutoriaonline.unidadeensino = ").append(unidadeEnsino).append(" )");
			sql.append(" and programacaotutoriaonline.curso is not null ");

		}

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/**
	 * Responsável por realizar uma consulta de <code>Curso</code> através do valor do atributo <code>String nome</code> . Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 *
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>CursoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<CursoVO> consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT distinct (Curso.*) FROM Curso LEFT JOIN unidadeensinocurso ON unidadeensinocurso.curso = curso.codigo WHERE lower (nome) like('%" + valorConsulta.toLowerCase() + "%') ";
		if (!(usuario.getUnidadeEnsinoLogado() == null || usuario.getUnidadeEnsinoLogado().getCodigo() == null || usuario.getUnidadeEnsinoLogado().getCodigo().intValue() == 0)) {
			sqlStr += "AND unidadeensinocurso.unidadeEnsino = " + usuario.getUnidadeEnsinoLogado().getCodigo() + " ";
		}
		sqlStr += "ORDER BY curso.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}


    public List consultarPorCodigoUnidadeEnsino(Integer codigoUnidadeEnsino, String periodicidade, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT DISTINCT c.* FROM UnidadeEnsinoCurso AS uec "
                + "INNER JOIN Curso c ON c.codigo = uec.curso "
                + "WHERE uec.unidadeEnsino = " + codigoUnidadeEnsino.intValue();
        if (Uteis.isAtributoPreenchido(periodicidade)) {
        	sqlStr += " AND c.periodicidade = ? ORDER BY c.nome "; 
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, periodicidade);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

	public List<CursoVO> consultarPorNomeCursoUnidadeEnsino(String valorConsulta, Integer codigoUnidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT distinct Curso.* " 
		+ "FROM Curso, UnidadeEnsinoCurso, UnidadeEnsino " 
		+ "WHERE UnidadeEnsinoCurso.unidadeEnsino = UnidadeEnsino.codigo " 
		+ "and UnidadeEnsinoCurso.curso = Curso.codigo and lower (sem_acentos(Curso.nome)) like lower(sem_acentos(?)) ";
		if(Uteis.isAtributoPreenchido(codigoUnidadeEnsino)){
			sqlStr+=" and UnidadeEnsino.codigo = " + codigoUnidadeEnsino.intValue();
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, PERCENT + valorConsulta + PERCENT);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List consultarPorUnidadeEnsino(Integer codigoUnidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT distinct c.* FROM UnidadeEnsinoCurso AS uec " + "INNER JOIN Curso c ON c.codigo = uec.curso ";
		if (codigoUnidadeEnsino != null && codigoUnidadeEnsino != 0) {
			sqlStr += "WHERE uec.unidadeEnsino = " + codigoUnidadeEnsino.intValue();
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List consultarPorCodigoUnidadeEnsino(Integer codigoUnidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT DISTINCT c.* FROM UnidadeEnsinoCurso AS uec " + "INNER JOIN Curso c ON c.codigo = uec.curso " + "WHERE uec.unidadeEnsino = " + codigoUnidadeEnsino.intValue();
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List consultarPorCodigoUnidadeEnsinoProcSeletivo(Integer codigoUnidadeEnsino, Integer procSeletivo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT distinct c.* FROM UnidadeEnsinoCurso AS uec  " + "INNER JOIN Curso c ON c.codigo = uec.curso " + "INNER JOIN procSeletivoCurso on procSeletivoCurso.unidadeEnsinoCurso = uec.codigo " + "INNER JOIN procSeletivoUnidadeEnsino as psuec on procSeletivoCurso.procSeletivoUnidadeEnsino = psuec.codigo " + "INNER JOIN procSeletivo on procSeletivo.codigo = psuec.procSeletivo " + "WHERE uec.unidadeEnsino = " + codigoUnidadeEnsino.intValue() + " and procSeletivo.codigo = " + procSeletivo.intValue() + " ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public CursoVO consultarPorCodigoUnidadeEnsinoCurso(Integer codigoUnidadeEnsinoCurso, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM curso LEFT JOIN unidadeensinocurso ON curso.codigo = unidadeensinocurso.curso WHERE unidadeensinocurso.codigo = " + codigoUnidadeEnsinoCurso.intValue();
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		tabelaResultado.next();
		return (montarDados(tabelaResultado, nivelMontarDados, false, usuario));
	}

	public List consultarPorNomeCurso_UnidadeEnsino(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * from Curso where codigo in (SELECT Curso.codigo FROM Curso, UnidadeEnsinoCurso WHERE lower (Curso.nome) like('%" + valorConsulta.toLowerCase() + "%') GROUP BY Curso.codigo ) ORDER BY Curso.nome";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr = "SELECT * from Curso where codigo in (SELECT Curso.codigo FROM Curso, UnidadeEnsinoCurso WHERE lower (Curso.nome) like('%" + valorConsulta.toLowerCase() + "%') and UnidadeEnsinoCurso.unidadeEnsino = " + unidadeEnsino.intValue() + " GROUP BY Curso.codigo ) ORDER BY Curso.nome";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List consultarPorNomeUnidadeEnsino(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT DISTINCT curso.*" + " FROM curso" + " INNER JOIN unidadeensinocurso ON unidadeensinocurso.curso = curso.codigo" + " INNER JOIN unidadeensino ON unidadeensinocurso.unidadeEnsino = unidadeensino.codigo";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr += " where unidadeensino.codigo = " + unidadeEnsino;
		}

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List consultarPorNomeUnidadeEnsinoNivelEducacional(String valorConsulta, Integer unidadeEnsino, String nivelEducacional, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT distinct Curso.* FROM Curso ";
		if (unidadeEnsino != null && unidadeEnsino != 0) {
			sqlStr += " INNER JOIN unidadeEnsinoCurso ON unidadeEnsinoCurso.curso = curso.codigo";
		}
		sqlStr += " WHERE Upper(curso.nome) like('" + valorConsulta.toUpperCase() + "%') AND curso.niveleducacional = '" + nivelEducacional + "' ";
		if (unidadeEnsino != null && unidadeEnsino != 0) {
			sqlStr += " AND unidadeensinocurso.unidadeensino = " + unidadeEnsino;
		}
		sqlStr += " ORDER BY curso.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>Curso</code> através do valor do atributo <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 *
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>CursoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Curso WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<CursoVO> consultarPorCodigoEUnidadeEnsino(Integer valorConsulta, Integer unidadeDeEnsinoCodigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT DISTINCT c.* FROM UnidadeEnsinoCurso AS uec " + "INNER JOIN Curso c ON c.codigo = uec.curso " + "WHERE c.codigo = " + valorConsulta;
		if (unidadeDeEnsinoCodigo != null && !unidadeDeEnsinoCodigo.equals(0)) {
			sqlStr += " and uec.unidadeEnsino = " + unidadeDeEnsinoCodigo;
		}
		sqlStr += " ORDER BY c.codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List consultarPorCodigoNivelEducacionalUnidadeEnsino(Integer valorConsulta, String nivelEducacional, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT distinct Curso.* FROM Curso ";
		if (unidadeEnsino != null && unidadeEnsino != 0) {
			sqlStr += " INNER JOIN unidadeEnsinoCurso ON unidadeEnsinoCurso.curso = curso.codigo";
		}
		sqlStr += " WHERE curso.codigo >= " + valorConsulta.intValue() + " AND curso.niveleducacional = '" + nivelEducacional + "' ";
		if (unidadeEnsino != null && unidadeEnsino != 0) {
			sqlStr += " AND unidadeensinocurso.unidadeensino = " + unidadeEnsino;
		}
		sqlStr += " ORDER BY curso.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List consultarPeriodoLetivoPorGradeCurricular(String prm, Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT curso.*, gradecurricular.*, periodoletivo.* FROM PeriodoLetivo, GradeCurricular , curso where periodoletivo.gradecurricular = " + valorConsulta.intValue() + " and (gradecurricular.curso = curso.codigo)  and (gradecurricular.codigo= periodoletivo.gradecurricular) ORDER BY Curso.codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados ( <code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 *
	 * @return List Contendo vários objetos da classe <code>CursoVO</code> resultantes da consulta.
	 */
	public static List<CursoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<CursoVO> vetResultado = new ArrayList<CursoVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, false, usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um objeto da classe <code>CursoVO</code>.
	 *
	 * @return O objeto da classe <code>CursoVO</code> com os dados devidamente montados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public static CursoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, boolean montarListaGradeCurricularNivelDadosBasicos, UsuarioVO usuario) throws Exception {
		CursoVO obj = new CursoVO();
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
			obj.setCodigo(dadosSQL.getInt("codigo"));
			obj.setNome(dadosSQL.getString("nome"));
			obj.setAbreviatura(dadosSQL.getString("abreviatura"));
			obj.setNomeDocumentacao(dadosSQL.getString("nomeDocumentacao"));
			obj.setNivelEducacional(dadosSQL.getString("nivelEducacional"));
			obj.setPeriodicidade(dadosSQL.getString("periodicidade"));
			obj.setQuantidadeDisciplinasOptativasExpedicaoDiploma(dadosSQL.getInt("quantidadeDisciplinasOptativasExpedicaoDiploma"));
			obj.getQuestionarioVO().setCodigo(dadosSQL.getInt("questionario"));
			obj.getConfiguracaoLdapVO().setCodigo(dadosSQL.getInt("configuracaoldap"));
			return obj;
		}
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setNome(dadosSQL.getString("nome"));
		obj.setCodigoContabil(dadosSQL.getString("codigocontabil"));
		obj.setNomeContabil(dadosSQL.getString("nomecontabil"));
		obj.setNivelContabil(dadosSQL.getString("nivelContabil"));
		if (dadosSQL.getString("modalidadeCurso") != null) {
			obj.setModalidadeCurso(ModalidadeDisciplinaEnum.valueOf(dadosSQL.getString("modalidadeCurso")));	
		}		
		obj.setNomeDocumentacao(dadosSQL.getString("nomeDocumentacao"));
		obj.setAnoMudancaLegislacao(dadosSQL.getString("anoMudancaLegislacao"));
		obj.setNrRegistroInterno(dadosSQL.getString("nrRegistroInterno"));
		obj.getAreaConhecimento().setCodigo(dadosSQL.getInt("areaConhecimento"));
		obj.setIdCursoInep(dadosSQL.getInt("idcursoinep"));
		obj.setRegimeAprovacao(dadosSQL.getString("regimeAprovacao"));
		obj.setRegime(dadosSQL.getString("regime"));
		obj.setHabilitacao(dadosSQL.getString("habilitacao"));
		obj.setTitulo(dadosSQL.getString("titulo"));
		obj.getConfiguracaoAcademico().setCodigo(dadosSQL.getInt("configuracaoAcademico"));
		obj.getConfiguracaoTCC().setCodigo(dadosSQL.getInt("configuracaoTCC"));
		obj.setObjetivos(dadosSQL.getString("objetivos"));
		obj.setDescricao(dadosSQL.getString("descricao"));
		obj.setPublicoAlvo(dadosSQL.getString("publicoAlvo"));
		obj.setPerfilEgresso(dadosSQL.getString("perfilEgresso"));
		obj.setNivelEducacional(dadosSQL.getString("nivelEducacional"));
		obj.setNrPeriodoLetivo(dadosSQL.getInt("nrPeriodoLetivo"));
		obj.setDataPublicacaoDO(dadosSQL.getDate("dataPublicacaoDO"));
		obj.setDataCriacao(dadosSQL.getDate("dataCriacao"));
		obj.setPeriodicidade(dadosSQL.getString("periodicidade"));
		obj.setAbreviatura(dadosSQL.getString("abreviatura"));
		obj.setTitulacaoDoFormando(dadosSQL.getString("titulacaoDoFormando"));
		obj.setTitulacaoDoFormandoFeminino(dadosSQL.getString("titulacaoDoFormandoFeminino"));
		obj.setLimitarQtdeDiasMaxDownload(dadosSQL.getBoolean("limitarDiasDownload"));
		obj.setQtdeMaxDiasDownload(dadosSQL.getInt("qtdeDiasLimiteDownload"));
		obj.setQuantidadeDisciplinasOptativasExpedicaoDiploma(dadosSQL.getInt("quantidadeDisciplinasOptativasExpedicaoDiploma"));
		obj.getFuncionarioRespPreInscricao().setCodigo(dadosSQL.getInt("funcionarioRespPreInscricao"));
		obj.setLiberarRegistroAulaEntrePeriodo(dadosSQL.getBoolean("liberarRegistroAulaEntrePeriodo"));
		obj.setApresentarHomePreInscricao(dadosSQL.getBoolean("apresentarHomePreInscricao"));
		obj.setPercentualEvolucaoAcademicaIngressanteEnade(dadosSQL.getInt("percentualEvolucaoAcademicaIngressanteEnade"));
		obj.setPercentualEvolucaoAcademicaConcluinteEnade(dadosSQL.getInt("percentualEvolucaoAcademicaConcluinteEnade"));
		obj.setPreposicaoNomeCurso(dadosSQL.getString("preposicaoNomeCurso"));
		obj.setNomeDocumentacao(dadosSQL.getString("nomeDocumentacao"));
		obj.setTitulacaoMasculinoApresentarDiploma(dadosSQL.getString("titulacaoMasculinoApresentarDiploma"));
		obj.setTitulacaoFemininoApresentarDiploma(dadosSQL.getString("titulacaoFemininoApresentarDiploma"));
		obj.setApresentarCursoBiblioteca(dadosSQL.getBoolean("apresentarCursoBiblioteca"));
		obj.setUtilizarRecursoAvaTerceiros(dadosSQL.getBoolean("utilizarRecursoAvaTerceiros"));
		obj.getQuestionarioVO().setCodigo(dadosSQL.getInt("questionario"));
		obj.getConfiguracaoLdapVO().setCodigo(dadosSQL.getInt("configuracaoldap"));		
		obj.setTextoDeclaracaoPPI(dadosSQL.getString("textoDeclaracaoPPI"));
		obj.setTextoDeclaracaoEscolaridadePublica(dadosSQL.getString("textoDeclaracaoEscolaridadePublica"));
		obj.setTextoConfirmacaoNovaMatricula(dadosSQL.getString("textoConfirmacaoNovaMatricula"));
		obj.setUrlDeclaracaoNormasMatricula(dadosSQL.getString("urlDeclaracaoNormasMatricula")); 
		obj.setTextoDeclaracaoBolsasAuxilios(dadosSQL.getString("textoDeclaracaoBolsasAuxilios")); 
		obj.setPermitirAssinarContratoPendenciaDocumentacao(dadosSQL.getBoolean("permitirAssinarContratoPendenciaDocumentacao")); 
		obj.setAtivarPreMatriculaAposEntregaDocumentosObrigatorios(dadosSQL.getBoolean("ativarPreMatriculaAposEntregaDocumentosObrigatorios"));		
		obj.setAtivarMatriculaAposAssinaturaContrato(dadosSQL.getBoolean("ativarMatriculaAposAssinaturaContrato"));		
		obj.getEixoCursoVO().setCodigo(dadosSQL.getInt("eixocurso"));
		obj.getTextoPadraoContratoMatriculaCalouro().setCodigo(dadosSQL.getInt("textoPadraoContratoMatriculaCalouro"));
		obj.setPermitirOutraMatriculaMesmoAluno(dadosSQL.getBoolean("permitirOutraMatriculaMesmoAluno"));
		obj.setAutorizacaoResolucaoEmTramitacao(dadosSQL.getBoolean("autorizacaoResolucaoEmTramitacao"));
		obj.setNumeroProcessoAutorizacaoResolucao(dadosSQL.getString("numeroProcessoAutorizacaoResolucao"));
		obj.setTipoProcessoAutorizacaoResolucao(dadosSQL.getString("tipoProcessoAutorizacaoResolucao"));
		obj.setDataCadastroAutorizacaoResolucao(dadosSQL.getDate("dataCadastroAutorizacaoResolucao"));
		obj.setDataProtocoloAutorizacaoResolucao(dadosSQL.getDate("dataProtocoloAutorizacaoResolucao"));
		obj.setDataHabilitacao(dadosSQL.getDate("dataHabilitacao"));
		obj.setPossuiCodigoEMEC(dadosSQL.getBoolean("possuiCodigoEMEC"));
		obj.setCodigoEMEC(dadosSQL.getInt("codigoEMEC"));
		obj.setNumeroProcessoEMEC(dadosSQL.getInt("numeroProcessoEMEC"));
		obj.setTipoProcessoEMEC(dadosSQL.getString("tipoProcessoEMEC"));
		obj.setDataCadastroEMEC(dadosSQL.getDate("dataCadastroEMEC"));
		obj.setDataProtocoloEMEC(dadosSQL.getDate("dataProtocoloEMEC"));
		if (Uteis.isAtributoPreenchido(dadosSQL.getString("tipoAutorizacaoCursoEnum"))) {
			obj.setTipoAutorizacaoCursoEnum(TipoAutorizacaoCursoEnum.valueOf(dadosSQL.getString("tipoAutorizacaoCursoEnum")));
		}
		obj.setNumeroAutorizacao(dadosSQL.getString("numeroAutorizacao"));
		obj.setDataCredenciamento(dadosSQL.getDate("dataCredenciamento"));
		obj.setVeiculoPublicacao(dadosSQL.getString("veiculoPublicacao"));
		if (Uteis.isAtributoPreenchido(dadosSQL.getInt("secaoPublicacao"))) {
			obj.setSecaoPublicacao(dadosSQL.getInt("secaoPublicacao"));
		}
		if (Uteis.isAtributoPreenchido(dadosSQL.getInt("paginaPublicacao"))) {
			obj.setPaginaPublicacao(dadosSQL.getInt("paginaPublicacao"));
		}
		if (Uteis.isAtributoPreenchido(dadosSQL.getInt("numeroDOU"))) {
			obj.setNumeroDOU(dadosSQL.getInt("numeroDOU"));
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSCONSULTA) {
			return obj;
		}
		
		montarDadosFuncionarioRespPreInscricao(obj, usuario);
		montarDadosAreaConhecimento(obj, usuario);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			return obj;
		}
		obj.setNovoObj(false);
		obj.setAutorizacaoCursoVOs(getFacadeFactory().getAutorizacaoCursoFacade().consultarPorCurso(obj.getCodigo(), nivelMontarDados, usuario));
		obj.setIdConteudoMasterBlackboardEstagio(dadosSQL.getString("idConteudoMasterBlackboardEstagio"));
		obj.setFuncionarioResponsavelAssinaturaTermoEstagioVO(Uteis.montarDadosVO(dadosSQL.getInt("funcionarioResponsavelAssinaturaTermoEstagio"), FuncionarioVO.class, p -> getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimaria(p, 0, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario)));
		
		obj.getGrupoPessoaAnaliseRelatorioFinalEstagioVO().setCodigo(dadosSQL.getInt("grupoPessoaAnaliseRelatorioFinalEstagio"));
		obj.getGrupoPessoaAnaliseAproveitamentoEstagioObrigatorioVO().setCodigo(dadosSQL.getInt("grupoPessoaAnaliseAproveitamentoEstagioObrigatorio"));
		obj.getGrupoPessoaAnaliseEquivalenciaEstagioObrigatorioVO().setCodigo(dadosSQL.getInt("grupoPessoaAnaliseEquivalenciaEstagioObrigatorio"));
		
		montarDadosConfiguracaoAcademico(obj, usuario);
		montarDadosConfiguracaoTCC(obj, nivelMontarDados);
		montarDadosTextoPadraoContratoMatriculaCalouro(obj, nivelMontarDados,usuario);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		obj.setDocumentacaoCursoVOs(getFacadeFactory().getDocumentacaoCursoFacade().consultarPorCurso(obj.getCodigo(), false, usuario));
		if(nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS_CANDIDATO) {
			return obj;
		}
		obj.setCursoTurnoVOs(getFacadeFactory().getCursoTurnoFacade().consultarCursoTurnos(obj.getCodigo(), false, usuario));
		if (montarListaGradeCurricularNivelDadosBasicos) {
			obj.setGradeCurricularVOs(getFacadeFactory().getGradeCurricularFacade().consultarGradeCurriculars(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
		} else {
			obj.setGradeCurricularVOs(getFacadeFactory().getGradeCurricularFacade().consultarGradeCurriculars(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuario));
		}
		obj.setCursoCoordenadorVOs(getFacadeFactory().getCursoCoordenadorFacade().consultarPorCodigoCurso(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
		obj.setMaterialCursoVOs(getFacadeFactory().getMaterialCursoFacade().consultarPorCurso(obj.getCodigo(), nivelMontarDados, usuario));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
			return obj;
		}
		
		return obj;
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>ConfiguracaoAcademicoVO</code> relacionado ao objeto <code>CursoVO</code>. Faz uso da chave primária da classe <code>ConfiguracaoAcademicoVO</code> para realizar a consulta.
	 *
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosConfiguracaoAcademico(CursoVO obj, UsuarioVO usuario) throws Exception {
		if (obj.getConfiguracaoAcademico().getCodigo().intValue() == 0) {
			return;
		}
		obj.setConfiguracaoAcademico(getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(obj.getConfiguracaoAcademico().getCodigo(), usuario));
	}

	public static void montarDadosConfiguracaoTCC(CursoVO obj, int nivelMontarDados) throws Exception {
		if (obj.getConfiguracaoTCC().getCodigo().intValue() == 0) {
			return;
		}
		obj.setConfiguracaoTCC(getFacadeFactory().getConfiguracaoTCCFacade().consultarPorChavePrimaria(obj.getConfiguracaoTCC().getCodigo(), nivelMontarDados));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>AreaConhecimentoVO</code> relacionado ao objeto <code>CursoVO</code>. Faz uso da chave primária da classe <code>AreaConhecimentoVO</code> para realizar a consulta.
	 *
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosAreaConhecimento(CursoVO obj, UsuarioVO usuario) throws Exception {
		if (obj.getAreaConhecimento().getCodigo().intValue() == 0) {
			obj.setAreaConhecimento(new AreaConhecimentoVO());
			return;
		}
		obj.setAreaConhecimento(getFacadeFactory().getAreaConhecimentoFacade().consultarPorChavePrimaria(obj.getAreaConhecimento().getCodigo(), usuario));
	}

	public static void montarDadosFuncionarioRespPreInscricao(CursoVO obj, UsuarioVO usuario) throws Exception {
		if (obj.getFuncionarioRespPreInscricao().getCodigo().intValue() == 0) {
			obj.setFuncionarioRespPreInscricao(new FuncionarioVO());
			return;
		}
		obj.setFuncionarioRespPreInscricao(getFacadeFactory().getFuncionarioFacade().consultaRapidaPorChavePrimaria(obj.getFuncionarioRespPreInscricao().getCodigo(), false, usuario));
	}

	/**
	 * Operação responsável por localizar um objeto da classe <code>CursoVO</code> através de sua chave primária.
	 *
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto procurado.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public CursoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, boolean montarListaGradeCurricularNivelDadosBasicos, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		
		String sql = "SELECT * FROM Curso WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados (Curso).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, montarListaGradeCurricularNivelDadosBasicos, usuario));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
	 */
	public static String getIdEntidade() {
		return Curso.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		Curso.idEntidade = idEntidade;
	}

	public List consultarPorCodigo(Integer valorConsulta, Integer unidadeEnsinoCodigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<UnidadeEnsinoVO> unidadeEnsinoVOs = new ArrayList<>(0);
		if (Uteis.isAtributoPreenchido(unidadeEnsinoCodigo)) {
			unidadeEnsinoVOs.add(new UnidadeEnsinoVO(unidadeEnsinoCodigo));
		}
		return consultarPorCodigo(valorConsulta, unidadeEnsinoVOs, controlarAcesso, nivelMontarDados, usuario);
	}
	
	@Override
	public List<CursoVO> consultarPorCodigo(Integer valorConsulta, List<UnidadeEnsinoVO> unidadeEnsinoVOs, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = " SELECT DISTINCT Curso.* FROM  curso " + " LEFT JOIN unidadeensinocurso on unidadeensinocurso.curso = curso.codigo" + " LEFT JOIN unidadeensino on unidadeensinocurso.unidadeensino = unidadeensino.codigo" + " WHERE curso.codigo >= " + valorConsulta.intValue();
		if (Uteis.isAtributoPreenchido(unidadeEnsinoVOs)) {
			sqlStr += " AND unidadeensino.codigo IN (" + unidadeEnsinoVOs.stream().map(u -> u.getCodigo().toString()).collect(Collectors.joining(", ")) +") ";
		}
		sqlStr += " ORDER BY curso.codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	@Override
	public List<CursoVO> consultarPorCodigoCoordenador(Integer valorConsulta, Integer unidadeEnsinoCodigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, Integer codigoCoordenador) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		List<Object> filtros = new ArrayList<>();
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT DISTINCT Curso.* FROM  curso ");
		sqlStr.append(" LEFT JOIN unidadeensinocurso on unidadeensinocurso.curso = curso.codigo");
		sqlStr.append(" LEFT JOIN unidadeensino on unidadeensinocurso.unidadeensino = unidadeensino.codigo");
		sqlStr.append(" inner join cursocoordenador on cursocoordenador.curso = curso.codigo");
		sqlStr.append(" inner join funcionario on cursocoordenador.funcionario = funcionario.codigo");
		sqlStr.append(" inner join pessoa on funcionario.pessoa = pessoa.codigo");
		sqlStr.append(" WHERE curso.codigo = ? ");
		filtros.add(valorConsulta);
		
		if (Uteis.isAtributoPreenchido(unidadeEnsinoCodigo)) {
			sqlStr.append(" AND unidadeensino.codigo = ?");
			filtros.add(unidadeEnsinoCodigo);
		}

		if (Uteis.isAtributoPreenchido(codigoCoordenador)) {
			sqlStr.append(" and pessoa.codigo = ?");
			filtros.add(codigoCoordenador);
		}

		sqlStr.append(" ORDER BY curso.codigo");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), filtros.toArray());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List consultarPorNomeCoordenador(String valorConsulta, Integer unidadeEnsinoCodigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, Integer codigoCoordenador) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		List<Object> filtros = new ArrayList<>();
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT DISTINCT Curso.* FROM  curso ");
		sqlStr.append(" LEFT JOIN unidadeensinocurso on unidadeensinocurso.curso = curso.codigo");
		sqlStr.append(" LEFT JOIN unidadeensino on unidadeensinocurso.unidadeensino = unidadeensino.codigo");
		sqlStr.append(" inner join cursocoordenador on cursocoordenador.curso = curso.codigo");
		sqlStr.append(" inner join funcionario on cursocoordenador.funcionario = funcionario.codigo");
		sqlStr.append(" inner join pessoa on funcionario.pessoa = pessoa.codigo");
		sqlStr.append(" WHERE lower (sem_acentos(curso.nome)) ilike(sem_acentos(?)) ");
		filtros.add("%"+valorConsulta + "%");

		if (unidadeEnsinoCodigo.intValue() != 0 && unidadeEnsinoCodigo != null) {
			sqlStr.append(" AND unidadeensino.codigo = ?");
			filtros.add(unidadeEnsinoCodigo.intValue());
		}

		if (Uteis.isAtributoPreenchido(codigoCoordenador)) {
			sqlStr.append(" and pessoa.codigo = ?");
			filtros.add(codigoCoordenador);
		}

		sqlStr.append(" ORDER BY curso.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), filtros.toArray());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List consultarPorNome(String valorConsulta, Integer unidadeEnsinoCodigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<UnidadeEnsinoVO> unidadeEnsinoVOs = new ArrayList<>(0);
		if (Uteis.isAtributoPreenchido(unidadeEnsinoCodigo)) {
			unidadeEnsinoVOs.add(new UnidadeEnsinoVO(unidadeEnsinoCodigo));
		}
		return consultarPorNome(valorConsulta, unidadeEnsinoVOs, controlarAcesso, nivelMontarDados, usuario);
	}
	
	@Override
	public List<CursoVO> consultarPorNome(String valorConsulta, List<UnidadeEnsinoVO> unidadeEnsinoVOs, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = " SELECT DISTINCT Curso.* FROM   curso " + " LEFT JOIN unidadeensinocurso on unidadeensinocurso.curso = curso.codigo " 
				+ " LEFT JOIN unidadeensino on unidadeensinocurso.unidadeensino = unidadeensino.codigo " + " WHERE lower (sem_acentos(curso.nome)) like lower(sem_acentos(?)) ";
		if (Uteis.isAtributoPreenchido(unidadeEnsinoVOs)) {
			sqlStr += " AND unidadeensino.codigo IN (" + unidadeEnsinoVOs.stream().map(u -> u.getCodigo().toString()).collect(Collectors.joining(", ")) + ") ";
		}
		sqlStr += " ORDER BY curso.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta + PERCENT);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public CursoVO consultarCursoPorMatricula(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "select * from curso left join matricula on matricula.curso = curso.codigo where matricula.matricula = '" + valorConsulta + "' ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (!tabelaResultado.next()) {
			return new CursoVO();
		}
		return (montarDados(tabelaResultado, nivelMontarDados, false, usuario));
	}
	
	public CursoVO consultarCursoPorTurma(Integer turma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("SELECT * FROM curso ");
		sqlStr.append("INNER JOIN turma ON turma.curso = curso.codigo ");
		sqlStr.append("WHERE turma.codigo = ").append(turma);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			return new CursoVO();
		}
		return (montarDados(tabelaResultado, nivelMontarDados, false, usuario));
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirPeriodosLetivosGradeCurricular(GradeCurricularVO obj, CursoVO cursoVO, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getGradeCurricularFacade().excluirPeriodosLetivosVOs(obj, usuario);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirObjGradeCurricularVOs(GradeCurricularVO obj, CursoVO cursoVO, UsuarioVO usuario) throws Exception {
		int index = 0;
		Iterator i = cursoVO.getGradeCurricularVOs().iterator();
		while (i.hasNext()) {
			GradeCurricularVO objExistente = (GradeCurricularVO) i.next();
			if (objExistente.getNome().equals(obj.getNome())) {
				if (!obj.getCodigo().equals(0)) {
					getFacadeFactory().getGradeCurricularFacade().excluir(obj, usuario);
				}
				cursoVO.getGradeCurricularVOs().remove(index);
				return;
			}
			index++;
		}
	}

	public List<CursoVO> consultarPorCodigoPessoa(Integer codigoPessoa, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		String sqlStr = "SELECT * FROM curso LEFT JOIN matricula ON matricula.curso = curso.codigo WHERE matricula.aluno = " + codigoPessoa + " ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/**
	 * Responsável por realizar uma consulta de <code>UnidadeEnsino</code> através do valor do atributo <code>String nome</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
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
		str.append("select count(*) over() as qtde_total_registros, curso.codigo, curso.nome, curso.codigocontabil,  curso.nomecontabil, curso.nivelcontabil, curso.nomeDocumentacao, curso.idCursoInep,  curso.periodicidade, curso.nivelEducacional, curso.nrRegistroInterno, areaConhecimento.codigo as \"areaConhecimento.codigo\", areaConhecimento.nome as \"areaConhecimento.nome\", ");
		str.append("curso.configuracaoAcademico as \"curso.configuracaoAcademico\", curso.limitarDiasDownload as \"curso.limitarDiasDownload\", curso.qtdeDiasLimiteDownload as \"curso.qtdeDiasLimiteDownload\", curso.utilizarRecursoAvaTerceiros, curso.questionario as \"curso.questionario\" , curso.nrperiodoletivo as \"curso.nrperiodoletivo\", curso.configuracaoldap as \"curso.configuracaoldap\" , ");
		str.append(" curso.textoDeclaracaoPPI as \"curso.textoDeclaracaoPPI\" , curso.textoDeclaracaoEscolaridadePublica as \"curso.textoDeclaracaoEscolaridadePublica\" , curso.textoConfirmacaoNovaMatricula as \"curso.textoConfirmacaoNovaMatricula\" , curso.urlDeclaracaoNormasMatricula as \"curso.urlDeclaracaoNormasMatricula\" , curso.textoDeclaracaoBolsasAuxilios as \"curso.textoDeclaracaoBolsasAuxilios\" , ") ;
		str.append(" curso.permitirAssinarContratoPendenciaDocumentacao as \"curso.permitirAssinarContratoPendenciaDocumentacao\"  ,	curso.ativarPreMatriculaAposEntregaDocumentosObrigatorios as \"curso.ativarPreMatriculaAposEntregaDocumentosObrigatorios\"  , curso.ativarMatriculaAposAssinaturaContrato  as   \"curso.ativarMatriculaAposAssinaturaContrato\"  , curso.textoPadraoContratoMatriculaCalouro  as   \"curso.textoPadraoContratoMatriculaCalouro\" , curso.permitirOutraMatriculaMesmoAluno  as \"curso.permitirOutraMatriculaMesmoAluno\" ");
		str.append(" from curso ");
		str.append("left join areaConhecimento on areaConhecimento.codigo = curso.areaConhecimento ");
		return str;
	}

	public List<CursoVO> montarDadosConsultaRapida(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
		List<CursoVO> vetResultado = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			CursoVO obj = new CursoVO();
			montarDadosBasico(obj, tabelaResultado, usuario);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	public List<CursoVO> montarDadosConsultaCompleta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
		List<CursoVO> vetResultado = new ArrayList<CursoVO>(0);
		while (tabelaResultado.next()) {
			CursoVO obj = new CursoVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			carregarDados(obj, usuario);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	/**
	 * Consulta que espera um ResultSet com os campos mínimos para uma consulta rápida e intelegente. Desta maneira, a mesma será sempre capaz de montar os atributos básicos do objeto e alguns atributos relacionados de relevância para o contexto da aplicação.
	 *
	 * @param obj
	 * @throws Exception
	 */
	private void montarDadosBasico(CursoVO obj, SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
		// Dados do Curso
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setNome(dadosSQL.getString("nome"));
		obj.setCodigoContabil(dadosSQL.getString("codigocontabil"));
		obj.setNomeContabil(dadosSQL.getString("nomecontabil"));
		obj.setNivelContabil(dadosSQL.getString("nivelcontabil"));
		obj.setNomeDocumentacao(dadosSQL.getString("nomeDocumentacao"));
		obj.setPeriodicidade(dadosSQL.getString("periodicidade"));
		obj.setNrRegistroInterno(dadosSQL.getString("nrRegistroInterno"));
		obj.setNivelEducacional(dadosSQL.getString("nivelEducacional"));
		obj.getConfiguracaoAcademico().setCodigo(dadosSQL.getInt("curso.configuracaoAcademico"));
		obj.setLimitarQtdeDiasMaxDownload(dadosSQL.getBoolean("curso.limitarDiasDownload"));
		obj.setQtdeMaxDiasDownload(dadosSQL.getInt("curso.qtdeDiasLimiteDownload"));
		obj.setUtilizarRecursoAvaTerceiros(dadosSQL.getBoolean("utilizarRecursoAvaTerceiros"));
		obj.setIdCursoInep(dadosSQL.getInt("idCursoInep"));
		// Dados do Area Conhecimento
		obj.getAreaConhecimento().setCodigo(dadosSQL.getInt("areaConhecimento.codigo"));
		obj.getAreaConhecimento().setNome(dadosSQL.getString("areaConhecimento.nome"));
		
		obj.getQuestionarioVO().setCodigo(dadosSQL.getInt("curso.questionario"));
		obj.getConfiguracaoLdapVO().setCodigo(dadosSQL.getInt("curso.configuracaoldap"));
		obj.setNrPeriodoLetivo(dadosSQL.getInt("curso.nrPeriodoLetivo"));
		obj.setTextoDeclaracaoPPI(dadosSQL.getString("curso.textoDeclaracaoPPI"));
		obj.setTextoDeclaracaoEscolaridadePublica(dadosSQL.getString("curso.textoDeclaracaoEscolaridadePublica"));
		obj.setTextoConfirmacaoNovaMatricula(dadosSQL.getString("curso.textoConfirmacaoNovaMatricula"));
		obj.setUrlDeclaracaoNormasMatricula(dadosSQL.getString("curso.urlDeclaracaoNormasMatricula")); 
		obj.setTextoDeclaracaoBolsasAuxilios(dadosSQL.getString("curso.textoDeclaracaoBolsasAuxilios"));
		obj.setPermitirAssinarContratoPendenciaDocumentacao(dadosSQL.getBoolean("curso.permitirAssinarContratoPendenciaDocumentacao")); 
		obj.setAtivarPreMatriculaAposEntregaDocumentosObrigatorios(dadosSQL.getBoolean("curso.ativarPreMatriculaAposEntregaDocumentosObrigatorios"));	
		obj.setAtivarMatriculaAposAssinaturaContrato(dadosSQL.getBoolean("curso.ativarMatriculaAposAssinaturaContrato"));	
		obj.setDocumentacaoCursoVOs(getFacadeFactory().getDocumentacaoCursoFacade().consultarPorCurso(obj.getCodigo(), false, usuario));
		obj.getTextoPadraoContratoMatriculaCalouro().setCodigo(dadosSQL.getInt("curso.textoPadraoContratoMatriculaCalouro"));
		obj.setPermitirOutraMatriculaMesmoAluno(dadosSQL.getBoolean("curso.permitirOutraMatriculaMesmoAluno"));	
	}

	/**
	 * Método responsavel por invocar uma consulta rápida(Básica) e padrão que buscará apenas campos necessários para visualização do cliente na tela. Está consulta é considerada Padrão pelo motivo de todos os métodos chamar a mesma consulta(getSQLPadraoConsultaBasica) e apenas adicionar as cláusulas de condições e ordenação.
	 *
	 * @author Carlos
	 */
	public List<CursoVO> consultaRapidaPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, Boolean validarUnidadeLogada, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("LEFT JOIN unidadeensinocurso ON unidadeensinocurso.curso = curso.codigo ");
		sqlStr.append("WHERE sem_acentos(curso.nome) ilike(sem_acentos(?)) ");
		if (validarUnidadeLogada && Uteis.isAtributoPreenchido(usuario.getUnidadeEnsinoLogado())) {
			sqlStr.append("AND unidadeensinocurso.unidadeEnsino = ").append(usuario.getUnidadeEnsinoLogado().getCodigo()).append(" ");
		}
		appendGroupBy(sqlStr);
		sqlStr.append("ORDER BY curso.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta + PERCENT);
		return montarDadosConsultaRapida(tabelaResultado, usuario);
	}

	@Override
	public List<CursoVO> consultaRapidaPorNomePorUnidadeEnsinoPorNivelEducacional(String valorConsulta, Integer unidadeEnsino, TipoNivelEducacional tipoNivelEducacional, DataModelo dataModelo) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("LEFT JOIN unidadeensinocurso ON unidadeensinocurso.curso = curso.codigo ");
		sqlStr.append("WHERE lower(sem_acentos(curso.nome)) ilike(sem_acentos(?)) ");
		dataModelo.getListaFiltros().add(valorConsulta + "%");
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sqlStr.append("AND unidadeensinocurso.unidadeEnsino = ? ");
			dataModelo.getListaFiltros().add(unidadeEnsino);
		}
		if (Uteis.isAtributoPreenchido(tipoNivelEducacional)) {
			sqlStr.append("AND curso.nivelEducacional = ? ");
			dataModelo.getListaFiltros().add(tipoNivelEducacional.getValor());
		}
		appendGroupBy(sqlStr);
		sqlStr.append("ORDER BY curso.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), dataModelo.getListaFiltros().toArray());
		return montarDadosConsultaRapida(tabelaResultado, dataModelo.getUsuario());
	}

	@Override
	public List<CursoVO> consultaRapidaPorNomePorListaUnidadeEnsinoPorNivelEducacional(String valorConsulta,  List<UnidadeEnsinoVO> unidadeEnsinos, TipoNivelEducacional tipoNivelEducacional, DataModelo dataModelo) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("LEFT JOIN unidadeensinocurso ON unidadeensinocurso.curso = curso.codigo ");
		sqlStr.append("WHERE sem_acentos(curso.nome) ilike(sem_acentos(?)) ");
		dataModelo.getListaFiltros().add(valorConsulta + "%");
		if(!unidadeEnsinos.isEmpty()){
			sqlStr.append("AND unidadeensinocurso.unidadeEnsino IN (").append(unidadeEnsinos.stream().map(ue -> ue.getCodigo().toString()).collect(Collectors.joining(", "))).append(") ");
		}else{
			sqlStr.append("AND unidadeensinocurso.unidadeEnsino IN (0) ");
		}
		if (Uteis.isAtributoPreenchido(tipoNivelEducacional)) {
			sqlStr.append("AND curso.nivelEducacional = ? ");
			dataModelo.getListaFiltros().add(tipoNivelEducacional.getValor());
		}
		appendGroupBy(sqlStr);
		sqlStr.append("ORDER BY curso.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), dataModelo.getListaFiltros().toArray());
		return montarDadosConsultaRapida(tabelaResultado, dataModelo.getUsuario());
	}

	public List consultaRapidaPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("LEFT JOIN unidadeensinocurso ON unidadeensinocurso.curso = curso.codigo ");
		sqlStr.append(" WHERE curso.codigo >= " + valorConsulta.intValue() + " ");
		if (!(usuario.getUnidadeEnsinoLogado() == null || usuario.getUnidadeEnsinoLogado().getCodigo() == null || usuario.getUnidadeEnsinoLogado().getCodigo().intValue() == 0)) {
			sqlStr.append("AND unidadeensinocurso.unidadeEnsino = " + usuario.getUnidadeEnsinoLogado().getCodigo() + " ");
		}
		appendGroupBy(sqlStr);
		sqlStr.append("ORDER BY curso.codigo");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado, usuario);
	}

	@Override
	public List<CursoVO> consultaRapidaPorCodigoPorUnidadeEnsinoPorNivelEducacional(Integer valorConsulta, Integer unidadeEnsino, TipoNivelEducacional tipoNivelEducacional, DataModelo dataModelo) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("LEFT JOIN unidadeensinocurso ON unidadeensinocurso.curso = curso.codigo ");
		sqlStr.append(" WHERE curso.codigo >= ? ");
		dataModelo.getListaFiltros().add(valorConsulta);
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sqlStr.append("AND unidadeensinocurso.unidadeEnsino = ? ");
			dataModelo.getListaFiltros().add(unidadeEnsino);
		}
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sqlStr.append("AND curso.nivelEducacional = ? ");
			dataModelo.getListaFiltros().add(tipoNivelEducacional.getValor());
		}
		appendGroupBy(sqlStr);
		sqlStr.append("ORDER BY curso.codigo");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), dataModelo.getListaFiltros());
		return montarDadosConsultaRapida(tabelaResultado, dataModelo.getUsuario());
	}

	public List consultaRapidaPorCodigoCursoProcessoMatricula(Integer valorConsulta, Integer processoMatricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer str = new StringBuffer(getSQLPadraoConsultaBasica());
		str.append(" INNER JOIN unidadeensinocurso ON unidadeensinocurso.curso = curso.codigo ");
		str.append(" INNER JOIN ProcessoMatriculaCalendario ON ProcessoMatriculaCalendario.curso = unidadeEnsinocurso.curso and ProcessoMatriculaCalendario.turno = unidadeEnsinocurso.turno ");
		str.append(" WHERE ProcessoMatriculaCalendario.processomatricula = " + processoMatricula.intValue() + " ");
		str.append(" AND curso.codigo >= " + valorConsulta.intValue() + " ");
		if (!(usuario.getUnidadeEnsinoLogado() == null || usuario.getUnidadeEnsinoLogado().getCodigo() == null || usuario.getUnidadeEnsinoLogado().getCodigo().intValue() == 0)) {
			str.append("AND unidadeensinocurso.unidadeEnsino = " + usuario.getUnidadeEnsinoLogado().getCodigo() + " ");
		}
		appendGroupBy(str);
		str.append("ORDER BY curso.codigo");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(str.toString());
		return montarDadosConsultaRapida(tabelaResultado, usuario);
	}

	public List consultaRapidaPorNomeCursoProcessoMatricula(String valorConsulta, Integer processoMatricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer str = new StringBuffer(getSQLPadraoConsultaBasica());				
		str.append(" INNER JOIN unidadeensinocurso ON unidadeensinocurso.curso = curso.codigo ");
		str.append(" INNER JOIN ProcessoMatriculaCalendario ON ProcessoMatriculaCalendario.curso = unidadeEnsinocurso.curso and ProcessoMatriculaCalendario.turno = unidadeEnsinocurso.turno ");
		str.append(" WHERE ProcessoMatriculaCalendario.processomatricula = " + processoMatricula.intValue() + " ");
		str.append(" AND sem_acentos(curso.nome) ilike sem_acentos(?) ");
		if (!(usuario.getUnidadeEnsinoLogado() == null || usuario.getUnidadeEnsinoLogado().getCodigo() == null || usuario.getUnidadeEnsinoLogado().getCodigo().intValue() == 0)) {
			str.append("AND unidadeensinocurso.unidadeEnsino = " + usuario.getUnidadeEnsinoLogado().getCodigo() + " ");
		}
		appendGroupBy(str);
		str.append("ORDER BY curso.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(str.toString(), valorConsulta+"%");
		return montarDadosConsultaRapida(tabelaResultado, usuario);
	}

	public List<CursoVO> consultaRapidaPorNrRegistroInterno(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("LEFT JOIN unidadeensinocurso ON unidadeensinocurso.curso = curso.codigo ");
		sqlStr.append(" WHERE lower (nrRegistroInterno) like lower(?) ");
		if (!(usuario.getUnidadeEnsinoLogado() == null || usuario.getUnidadeEnsinoLogado().getCodigo() == null || usuario.getUnidadeEnsinoLogado().getCodigo().intValue() == 0)) {
			sqlStr.append("AND unidadeensinocurso.unidadeEnsino = " + usuario.getUnidadeEnsinoLogado().getCodigo() + " ");
		}
		appendGroupBy(sqlStr);
		sqlStr.append("ORDER BY nrRegistroInterno");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta + PERCENT);
		return montarDadosConsultaRapida(tabelaResultado, usuario);
	}

	public List consultaRapidaPorNomeAreaConhecimento(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("LEFT JOIN unidadeensinocurso ON unidadeensinocurso.curso = curso.codigo ");
		sqlStr.append(" WHERE Curso.areaConhecimento = AreaConhecimento.codigo and ");
		sqlStr.append(" lower (AreaConhecimento.nome) like lower(?) ");
		if (!(usuario.getUnidadeEnsinoLogado() == null || usuario.getUnidadeEnsinoLogado().getCodigo() == null || usuario.getUnidadeEnsinoLogado().getCodigo().intValue() == 0)) {
			sqlStr.append("AND unidadeensinocurso.unidadeEnsino = " + usuario.getUnidadeEnsinoLogado().getCodigo() + " ");
		}
		appendGroupBy(sqlStr);
		sqlStr.append("ORDER BY AreaConhecimento.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta + PERCENT);
		return montarDadosConsultaRapida(tabelaResultado, usuario);
	}

	public List consultaRapidaPorCodigoAreaConhecimento(Integer codigo, PeriodicidadeEnum periodicidadeEnum, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("LEFT JOIN unidadeensinocurso ON unidadeensinocurso.curso = curso.codigo ");
		sqlStr.append(" WHERE Curso.areaConhecimento = AreaConhecimento.codigo and ");
		sqlStr.append(" AreaConhecimento.codigo = " + codigo + " ");
		if (!(usuario.getUnidadeEnsinoLogado() == null || usuario.getUnidadeEnsinoLogado().getCodigo() == null || usuario.getUnidadeEnsinoLogado().getCodigo().intValue() == 0)) {
			sqlStr.append("AND unidadeensinocurso.unidadeEnsino = " + usuario.getUnidadeEnsinoLogado().getCodigo() + " ");
		}
		if(Uteis.isAtributoPreenchido(periodicidadeEnum)) {
			sqlStr.append(" AND curso.periodicidade = '").append(periodicidadeEnum.getValor()).append("'");
		}
		appendGroupBy(sqlStr);
		sqlStr.append("ORDER BY AreaConhecimento.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado, usuario);
	}

	public List<CursoVO> consultaRapidaPorNrNivelEducacional(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("LEFT JOIN unidadeensinocurso ON unidadeensinocurso.curso = curso.codigo ");
		sqlStr.append(" WHERE lower (nivelEducacional) like lower(?) ");
		if (!(usuario.getUnidadeEnsinoLogado() == null || usuario.getUnidadeEnsinoLogado().getCodigo() == null || usuario.getUnidadeEnsinoLogado().getCodigo().intValue() == 0)) {
			sqlStr.append("AND unidadeensinocurso.unidadeEnsino = " + usuario.getUnidadeEnsinoLogado().getCodigo() + " ");
		}
		appendGroupBy(sqlStr);
		sqlStr.append("ORDER BY codigo");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta + PERCENT);
		return montarDadosConsultaRapida(tabelaResultado, usuario);
	}

	public List<CursoVO> consultaRapidaPorNomeUnidadeEnsino(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" LEFT JOIN unidadeensinocurso ON unidadeensinocurso.curso = curso.codigo ");
		sqlStr.append(" LEFT JOIN unidadeensino ON unidadeensinocurso.unidadeEnsino = unidadeensino.codigo ");
		SqlRowSet tabelaResultado = null;
		if (!(usuario.getUnidadeEnsinoLogado() == null || usuario.getUnidadeEnsinoLogado().getCodigo() == null || usuario.getUnidadeEnsinoLogado().getCodigo().intValue() == 0)) {
			sqlStr.append("WHERE unidadeensino.codigo = " + usuario.getUnidadeEnsinoLogado().getCodigo() + " ");
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		} else {
			sqlStr.append("WHERE LOWER(unidadeensino.nome) LIKE lower(?)");
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta + PERCENT);
		}
		appendGroupBy(sqlStr);
		return montarDadosConsultaRapida(tabelaResultado, usuario);
	}

	public List<CursoVO> consultaRapidaPorCodigoUnidadeEnsino(Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" LEFT JOIN unidadeensinocurso ON unidadeensinocurso.curso = curso.codigo ");
		sqlStr.append(" LEFT JOIN unidadeensino ON unidadeensinocurso.unidadeEnsino = unidadeensino.codigo ");
		if (unidadeEnsino != null && unidadeEnsino != 0) {
			sqlStr.append("WHERE unidadeensino.codigo = " + unidadeEnsino + " ");
		}
		appendGroupBy(sqlStr);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado, usuario);
	}

	public List<CursoVO> consultaRapidaPorCodigoCursoUnidadeEnsino(Integer codigoCurso, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" LEFT JOIN unidadeensinocurso ON unidadeensinocurso.curso = curso.codigo ");
		sqlStr.append(" LEFT JOIN unidadeensino ON unidadeensinocurso.unidadeEnsino = unidadeensino.codigo ");
		sqlStr.append(" WHERE curso.codigo = ").append(codigoCurso);
		if (unidadeEnsino != null && unidadeEnsino != 0) {
			sqlStr.append(" AND unidadeensino.codigo = ").append(unidadeEnsino).append(" ");
		}
		appendGroupBy(sqlStr);
		sqlStr.append(" ORDER BY curso.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado, usuario);
	}

	public List<CursoVO> consultaRapidaPorNomeCursoUnidadeEnsino(String nomeCurso, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" LEFT JOIN unidadeensinocurso ON unidadeensinocurso.curso = curso.codigo ");
		sqlStr.append(" LEFT JOIN unidadeensino ON unidadeensinocurso.unidadeEnsino = unidadeensino.codigo ");
		sqlStr.append(" WHERE (sem_acentos(curso.nome)) ILIKE (sem_acentos(?))");
		if (unidadeEnsino != null && unidadeEnsino != 0) {
			sqlStr.append(" AND unidadeensino.codigo = ").append(unidadeEnsino).append(" ");
		}
		appendGroupBy(sqlStr);
		sqlStr.append(" ORDER BY curso.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), nomeCurso + PERCENT);
		return montarDadosConsultaRapida(tabelaResultado, usuario);
	}

	public List<CursoVO> consultaRapidaPorCodigoUnidadeEnsinoTurno(Integer unidadeEnsino, Integer turno, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		String and = "";
		sqlStr.append(" LEFT JOIN unidadeensinocurso ON unidadeensinocurso.curso = curso.codigo ");
		sqlStr.append(" LEFT JOIN unidadeensino ON unidadeensinocurso.unidadeEnsino = unidadeensino.codigo ");
		if (unidadeEnsino != null && unidadeEnsino != 0) {
			sqlStr.append("WHERE unidadeensino.codigo = " + unidadeEnsino + " ");
			and = " AND ";
		}
		if (turno != null && turno != 0) {
			if (and.equals("")) {
				and = " WHERE ";
			}
			sqlStr.append(and).append(" unidadeensinocurso.turno = " + turno + " ");
		}
		appendGroupBy(sqlStr);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado, usuario);
	}

	@Override
	public List<CursoVO> consultaRapidaPorNomePorCodigoUnidadeEnsinoTurno(String nome, Integer unidadeEnsino, Integer turno, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		String and = "";
		sqlStr.append(" LEFT JOIN unidadeensinocurso ON unidadeensinocurso.curso = curso.codigo ");
		sqlStr.append(" LEFT JOIN unidadeensino ON unidadeensinocurso.unidadeEnsino = unidadeensino.codigo ");
		if (unidadeEnsino != null && unidadeEnsino != 0) {
			sqlStr.append("WHERE unidadeensino.codigo = " + unidadeEnsino + " ");
			and = " AND ";
		}
		if (turno != null && turno != 0) {
			if (and.equals("")) {
				and = " WHERE ";
			}
			sqlStr.append(and).append(" unidadeensinocurso.turno = " + turno + " ");
		}
		if (nome != null && !nome.isEmpty()) {
			if (and.equals("")) {
				and = " WHERE ";
			}
			sqlStr.append(and).append(" curso.nome ILIKE '%").append(nome).append("%'");
		}
		appendGroupBy(sqlStr);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado, usuario);
	}

	@Override
	public List<CursoVO> consultaRapidaPorCodigoPorCodigoUnidadeEnsinoTurno(Integer codigo, Integer unidadeEnsino, Integer turno, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		String and = "";
		sqlStr.append(" LEFT JOIN unidadeensinocurso ON unidadeensinocurso.curso = curso.codigo ");
		sqlStr.append(" LEFT JOIN unidadeensino ON unidadeensinocurso.unidadeEnsino = unidadeensino.codigo ");
		if (unidadeEnsino != null && unidadeEnsino != 0) {
			sqlStr.append("WHERE unidadeensino.codigo = " + unidadeEnsino + " ");
			and = " AND ";
		}
		if (turno != null && turno != 0) {
			if (and.equals("")) {
				and = " WHERE ";
			}
			sqlStr.append(and).append(" unidadeensinocurso.turno = " + turno + " ");
		}
		if (codigo != null && codigo > 0) {
			if (and.equals("")) {
				and = " WHERE ";
			}
			sqlStr.append(and).append(" curso.codigo = ").append(codigo);
		}
		appendGroupBy(sqlStr);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado, usuario);
	}

	public void carregarDados(CursoVO obj, UsuarioVO usuario) throws Exception {
		carregarDados(obj, NivelMontarDados.TODOS, usuario);
	}

	public void carregarDados(CursoVO obj, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception {
		SqlRowSet resultado = null;
		if ((nivelMontarDados.equals(NivelMontarDados.BASICO)) && (obj.getIsNivelMontarDadosNaoInicializado())) {
			resultado = consultaRapidaPorChavePrimariaDadosBasicos(obj.getCodigo(), usuario);
			montarDadosBasico(obj, resultado, usuario);
		}
		if ((nivelMontarDados.equals(NivelMontarDados.TODOS)) && (!obj.getIsNivelMontarDadosTodos())) {
			resultado = consultaRapidaPorChavePrimariaDadosCompletos(obj.getCodigo(), usuario);
			montarDadosCompleto(obj, resultado, usuario);
		}
	}

	private SqlRowSet consultaRapidaPorChavePrimariaDadosBasicos(Integer codCurso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE (Curso.codigo= ").append(codCurso).append(")");
		appendGroupBy(sqlStr);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return tabelaResultado;
	}

	private SqlRowSet consultaRapidaPorChavePrimariaDadosCompletos(Integer codCurso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaCompleta();
		sqlStr.append(" WHERE (Curso.codigo= ").append(codCurso).append(")");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return tabelaResultado;
	}

	private StringBuffer getSQLPadraoConsultaCompleta() {
		StringBuffer str = new StringBuffer();
		str.append(" select curso.*, areaconhecimento.codigo AS \"areaconhecimento.codigo\",");
		str.append(" areaconhecimento.nome AS \"areaconhecimento.nome\", configuracaoacademico.codigo AS \"configuracaoacademico.codigo\",");
		str.append(" configuracaoacademico.nome AS \"configuracaoacademico.nome\" ");
		str.append(" from curso");
		str.append(" LEFT JOIN areaconhecimento ON areaconhecimento.codigo = curso.areaconhecimento");
		str.append(" LEFT JOIN configuracaoacademico ON configuracaoacademico.codigo = curso.configuracaoacademico");
		return str;
	}

	/**
	 * Consulta que espera um ResultSet com todos os campos e dados de objetos relacionados, Para reconstituir o objeto por completo, de uma determinada entidade.
	 * 
	 * @param obj
	 * @throws Exception
	 */
	private void montarDadosCompleto(CursoVO obj, SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
		// Dados Curso
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setNome(dadosSQL.getString("nome"));
		obj.setCodigoContabil(dadosSQL.getString("codigocontabil"));
		obj.setNomeContabil(dadosSQL.getString("nomecontabil"));
		obj.setNivelContabil(dadosSQL.getString("nivelcontabil"));
		String modalidade = dadosSQL.getString("modalidadeCurso");
		if (modalidade != null) {
			obj.setModalidadeCurso(ModalidadeDisciplinaEnum.valueOf(dadosSQL.getString("modalidadeCurso")));
		}
		obj.setNomeDocumentacao(dadosSQL.getString("nomeDocumentacao"));
		obj.setNrRegistroInterno(dadosSQL.getString("nrRegistroInterno"));
		obj.getAreaConhecimento().setCodigo(dadosSQL.getInt("areaConhecimento"));
		obj.setIdCursoInep(dadosSQL.getInt("idcursoinep"));
		obj.setRegimeAprovacao(dadosSQL.getString("regimeAprovacao"));
		obj.setRegime(dadosSQL.getString("regime"));
		obj.setHabilitacao(dadosSQL.getString("habilitacao"));
		obj.setTitulo(dadosSQL.getString("titulo"));
		obj.getConfiguracaoAcademico().setCodigo(dadosSQL.getInt("configuracaoAcademico"));
		obj.setObjetivos(dadosSQL.getString("objetivos"));
		obj.setDescricao(dadosSQL.getString("descricao"));
		obj.setPublicoAlvo(dadosSQL.getString("publicoAlvo"));
		obj.setPerfilEgresso(dadosSQL.getString("perfilEgresso"));
		obj.setNivelEducacional(dadosSQL.getString("nivelEducacional"));
		obj.setNrPeriodoLetivo(dadosSQL.getInt("nrPeriodoLetivo"));
		obj.setLiberarRegistroAulaEntrePeriodo(dadosSQL.getBoolean("liberarRegistroAulaEntrePeriodo"));
		obj.setApresentarHomePreInscricao(dadosSQL.getBoolean("apresentarHomePreInscricao"));
		obj.setDataPublicacaoDO(dadosSQL.getDate("dataPublicacaoDO"));
		obj.setDataCriacao(dadosSQL.getDate("dataCriacao"));
		obj.setPeriodicidade(dadosSQL.getString("periodicidade"));
		obj.setTitulacaoDoFormando(dadosSQL.getString("titulacaoDoFormando"));
		obj.setTitulacaoMasculinoApresentarDiploma(dadosSQL.getString("titulacaoMasculinoApresentarDiploma"));
		obj.setTitulacaoFemininoApresentarDiploma(dadosSQL.getString("titulacaoFemininoApresentarDiploma"));
		obj.setLimitarQtdeDiasMaxDownload(dadosSQL.getBoolean("limitarDiasDownload"));
		obj.setQtdeMaxDiasDownload(dadosSQL.getInt("qtdeDiasLimiteDownload"));
		obj.setPercentualEvolucaoAcademicaIngressanteEnade(dadosSQL.getInt("percentualEvolucaoAcademicaIngressanteEnade"));
		obj.setPercentualEvolucaoAcademicaConcluinteEnade(dadosSQL.getInt("percentualEvolucaoAcademicaConcluinteEnade"));
		obj.setUtilizarRecursoAvaTerceiros(dadosSQL.getBoolean("utilizarRecursoAvaTerceiros"));
		// Dados da Area Conhecimento
		obj.getAreaConhecimento().setCodigo(dadosSQL.getInt("areaconhecimento.codigo"));
		obj.getAreaConhecimento().setNome(dadosSQL.getString("areaconhecimento.nome"));
		// Dados da Configuracao Academico
		obj.getConfiguracaoAcademico().setCodigo(dadosSQL.getInt("configuracaoacademico.codigo"));
		obj.getConfiguracaoAcademico().setNome(dadosSQL.getString("configuracaoacademico.nome"));
		
		obj.setIdConteudoMasterBlackboardEstagio(dadosSQL.getString("idConteudoMasterBlackboardEstagio"));
		obj.getGrupoPessoaAnaliseAproveitamentoEstagioObrigatorioVO().setCodigo(dadosSQL.getInt("grupoPessoaAnaliseAproveitamentoEstagioObrigatorio"));
		obj.getGrupoPessoaAnaliseEquivalenciaEstagioObrigatorioVO().setCodigo(dadosSQL.getInt("grupoPessoaAnaliseEquivalenciaEstagioObrigatorio"));
		obj.getGrupoPessoaAnaliseRelatorioFinalEstagioVO().setCodigo(dadosSQL.getInt("grupoPessoaAnaliseRelatorioFinalEstagio"));		
		obj.setDocumentacaoCursoVOs(getFacadeFactory().getDocumentacaoCursoFacade().consultarPorCurso(obj.getCodigo(), false, usuario));
		obj.setCursoTurnoVOs(getFacadeFactory().getCursoTurnoFacade().consultarCursoTurnos(obj.getCodigo(), false, usuario));
		obj.setGradeCurricularVOs(getFacadeFactory().getGradeCurricularFacade().consultarGradeCurriculars(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuario));
		obj.setCursoCoordenadorVOs(getFacadeFactory().getCursoCoordenadorFacade().consultarCursoCoordenadors(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario));		
		
		obj.setTextoDeclaracaoPPI(dadosSQL.getString("textoDeclaracaoPPI"));
		obj.setTextoDeclaracaoEscolaridadePublica(dadosSQL.getString("textoDeclaracaoEscolaridadePublica"));
		obj.setTextoConfirmacaoNovaMatricula(dadosSQL.getString("textoConfirmacaoNovaMatricula"));
		obj.setUrlDeclaracaoNormasMatricula(dadosSQL.getString("urlDeclaracaoNormasMatricula")); 
		obj.setTextoDeclaracaoBolsasAuxilios(dadosSQL.getString("textoDeclaracaoBolsasAuxilios"));
		obj.setPermitirAssinarContratoPendenciaDocumentacao(dadosSQL.getBoolean("permitirAssinarContratoPendenciaDocumentacao")); 
		obj.setAtivarPreMatriculaAposEntregaDocumentosObrigatorios(dadosSQL.getBoolean("ativarPreMatriculaAposEntregaDocumentosObrigatorios"));		
		obj.setAtivarMatriculaAposAssinaturaContrato(dadosSQL.getBoolean("ativarMatriculaAposAssinaturaContrato"));	
		obj.setTextoPadraoContratoMatriculaCalouro(getFacadeFactory().getTextoPadraoFacade().consultarTextoPadraoContratoMatriculaPorCurso(obj.getCodigo(), false ,Uteis.NIVELMONTARDADOS_TODOS, usuario));		
		obj.setPermitirOutraMatriculaMesmoAluno(dadosSQL.getBoolean("permitirOutraMatriculaMesmoAluno"));	
	}

	public List<CursoVO> consultarCursoPorNomePeriodicidadeEUnidadeEnsinoVOs(String nome, String periodicidade, String nivelEducacional, List<UnidadeEnsinoVO> unidadeEnsinoVOs, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder(" SELECT curso.codigo, curso.nome, curso.codigocontabil,  curso.nomecontabil, curso.nivelcontabil, curso.nomeDocumentacao, curso.idCursoInep,  curso.periodicidade, curso.nivelEducacional, curso.nrRegistroInterno, areaConhecimento.codigo as \"areaConhecimento.codigo\", areaConhecimento.nome as \"areaConhecimento.nome\", ");
		sb.append(" curso.configuracaoAcademico as \"curso.configuracaoAcademico\", curso.limitarDiasDownload as \"curso.limitarDiasDownload\", curso.qtdeDiasLimiteDownload as \"curso.qtdeDiasLimiteDownload\", curso.utilizarRecursoAvaTerceiros, curso.questionario as \"curso.questionario\" , curso.nrperiodoletivo as \"curso.nrperiodoletivo\", curso.configuracaoldap as \"curso.configuracaoldap\" , ");
		sb.append(" curso.textoDeclaracaoPPI as \"curso.textoDeclaracaoPPI\" , curso.textoDeclaracaoEscolaridadePublica as \"curso.textoDeclaracaoEscolaridadePublica\" , curso.textoConfirmacaoNovaMatricula as \"curso.textoConfirmacaoNovaMatricula\" , curso.urlDeclaracaoNormasMatricula as \"curso.urlDeclaracaoNormasMatricula\" , curso.textoDeclaracaoBolsasAuxilios as \"curso.textoDeclaracaoBolsasAuxilios\" , ") ;
		sb.append(" curso.permitirAssinarContratoPendenciaDocumentacao as \"curso.permitirAssinarContratoPendenciaDocumentacao\"  ,	curso.ativarPreMatriculaAposEntregaDocumentosObrigatorios as \"curso.ativarPreMatriculaAposEntregaDocumentosObrigatorios\"  , curso.ativarMatriculaAposAssinaturaContrato  as   \"curso.ativarMatriculaAposAssinaturaContrato\" , curso.textoPadraoContratoMatriculaCalouro  as   \"curso.textoPadraoContratoMatriculaCalouro\"  , curso.permitirOutraMatriculaMesmoAluno as \"curso.permitirOutraMatriculaMesmoAluno\" ");
		sb.append(" FROM curso ");		
		sb.append(" LEFT JOIN areaConhecimento ON areaConhecimento.codigo = curso.areaConhecimento ");
		sb.append(" WHERE sem_acentos(curso.nome) ILIKE sem_acentos(?) ");
		if (Uteis.isAtributoPreenchido(periodicidade)) {
			sb.append(" AND curso.periodicidade = '").append(periodicidade).append("' ");
		}
		if (Uteis.isAtributoPreenchido(nivelEducacional)) {
			sb.append(" AND curso.nivelEducacional = '").append(nivelEducacional).append("' ");
		}
		sb.append(" AND EXISTS ( SELECT FROM matricula ");
		sb.append(" INNER JOIN unidadeensinocurso ON matricula.curso = unidadeensinocurso.curso ");
		sb.append(" AND matricula.unidadeensino = unidadeensinocurso.unidadeensino ");
		sb.append(" AND matricula.turno = unidadeensinocurso.turno ");
		sb.append(" WHERE matricula.curso = curso.codigo ");
		if (Uteis.isAtributoPreenchido(unidadeEnsinoVOs) && unidadeEnsinoVOs.stream().anyMatch(UnidadeEnsinoVO::getFiltrarUnidadeEnsino)) {
			sb.append(unidadeEnsinoVOs.stream()
					.filter(UnidadeEnsinoVO::getFiltrarUnidadeEnsino)
					.map(ue -> ue.getCodigo().toString())
					.collect(Collectors.joining(", ", " AND matricula.unidadeensino IN (", ")")));
		}
		sb.append(" ) ORDER BY curso.nome ;");
		return montarDadosConsultaRapida(getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), "%"+nome+"%"), usuario);
	}

	@Override
	public List<CursoVO> consultar(String campoConsultaCurso, String valorConsultaCurso, Integer unidadeEnsinoCodigo, boolean controlarAcesso, int nivelmontardadosDadosbasicos, UsuarioVO usuario) throws Exception {
		List<CursoVO> objs = new ArrayList<>(0);
		if (usuario.getIsApresentarVisaoCoordenador()) {
			if (campoConsultaCurso.equals("codigo")) {
				if (valorConsultaCurso.equals("")) {
					valorConsultaCurso = "0";
				}
				objs = getFacadeFactory().getCursoFacade().consultarListaCursoPorCodigoCursoCodigoPessoaCoordenador(Uteis.getValorInteiro(valorConsultaCurso),usuario.getPessoa().getCodigo(), unidadeEnsinoCodigo, null, nivelmontardadosDadosbasicos, usuario);
			}
			if (campoConsultaCurso.equals("nome")) {
				objs = getFacadeFactory().getCursoFacade().consultarListaCursoPorNomeCursoCodigoPessoaCoordenador(valorConsultaCurso, usuario.getPessoa().getCodigo(), unidadeEnsinoCodigo, null, nivelmontardadosDadosbasicos, usuario);
			}

		} else {
			if (campoConsultaCurso.equals("codigo")) {
				if (valorConsultaCurso.equals("")) {
					valorConsultaCurso = "0";
				}
				objs = getFacadeFactory().getCursoFacade().consultarPorCodigoEUnidadeEnsino(Uteis.getValorInteiro(valorConsultaCurso), unidadeEnsinoCodigo, controlarAcesso, nivelmontardadosDadosbasicos, usuario);
			}
			if (campoConsultaCurso.equals("nome")) {
				objs = getFacadeFactory().getCursoFacade().consultaRapidaPorNomeEUnidadeDeEnsino(valorConsultaCurso, unidadeEnsinoCodigo, controlarAcesso, nivelmontardadosDadosbasicos, usuario);
			}
		}
		return objs;
	}

	@Override
	public List<CursoVO> consultaRapidaPorNomeEUnidadeDeEnsino(String valorConsultaCurso, Integer unidadeEnsinoCodigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT DISTINCT c.* FROM UnidadeEnsinoCurso AS uec " + "INNER JOIN Curso c ON c.codigo = uec.curso " + "WHERE lower (sem_acentos(c.nome)) ilike lower(sem_acentos(?)) ";
		if (unidadeEnsinoCodigo != null && unidadeEnsinoCodigo > 0) {
			sqlStr += " and uec.unidadeEnsino = " + unidadeEnsinoCodigo;
		}
		sqlStr += " ORDER BY c.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsultaCurso + PERCENT);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}
	
	@Override
	public List<CursoVO> consultaRapidaPorCodigoEUnidadeDeEnsino(Integer valorConsultaCurso, Integer unidadeEnsinoCodigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = " SELECT DISTINCT c.* FROM UnidadeEnsinoCurso AS uec " + " INNER JOIN Curso c ON c.codigo = uec.curso " + " WHERE c.codigo = ? ";
		if (unidadeEnsinoCodigo != null && unidadeEnsinoCodigo > 0) {
			sqlStr += " and uec.unidadeEnsino = " + unidadeEnsinoCodigo;
		}
		sqlStr += " ORDER BY c.nome ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsultaCurso);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	@Override
	public List<CursoVO> consultaRapidaPorNomeEUnidadeDeEnsinoENivelEducacional(String valorConsultaCurso, List<UnidadeEnsinoVO> lista, Boolean infantil, Boolean basico, Boolean medio, Boolean extensao, Boolean sequencial, Boolean graduacaoTecnologica, Boolean superior, Boolean posGraduacao, Boolean profissionalizante, Boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT DISTINCT curso.* FROM UnidadeEnsinoCurso ");
		sqlStr.append("INNER JOIN Curso  ON curso.codigo = unidadeensinocurso.curso ");
		sqlStr.append("WHERE lower (sem_acentos(curso.nome)) like (sem_acentos('");
		sqlStr.append(valorConsultaCurso.toLowerCase());
		sqlStr.append("%')) ");
		if (!lista.isEmpty()) {
			sqlStr.append(" and unidadeensinocurso.unidadeensino in (");
			int x = 0;
			for (UnidadeEnsinoVO unidadeEnsinoVO : lista) {
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
		montarSqlNivelAcademico(infantil, basico, medio, extensao, sequencial, graduacaoTecnologica, superior, posGraduacao, profissionalizante, sqlStr);
		sqlStr.append(" ORDER BY curso.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	private void montarSqlNivelAcademico(Boolean infantil, Boolean basico, Boolean medio, Boolean extensao, Boolean sequencial, Boolean graduacaoTecnologica, Boolean superior, Boolean posGraduacao, Boolean profissionalizante, StringBuilder sqlStr) {
		if (infantil || basico || medio || extensao || sequencial || graduacaoTecnologica || superior || posGraduacao || profissionalizante) {
			sqlStr.append(" and curso.niveleducacional in ( ");
			int filtros = 0;
			if (infantil) {
				sqlStr.append("'IN'");
				filtros++;
			}
			if (basico) {
				if (filtros > 0) {
					sqlStr.append(" , ");
				}
				sqlStr.append("'BA'");
				filtros++;
			}
			if (medio) {
				if (filtros > 0) {
					sqlStr.append(" , ");
				}
				sqlStr.append("'ME'");
				filtros++;
			}
			if (extensao) {
				if (filtros > 0) {
					sqlStr.append(" , ");
				}
				sqlStr.append("'EX'");
				filtros++;
			}
			if (sequencial) {
				if (filtros > 0) {
					sqlStr.append(" , ");
				}
				sqlStr.append("'SE'");
				filtros++;
			}
			if (graduacaoTecnologica) {
				if (filtros > 0) {
					sqlStr.append(" , ");
				}
				sqlStr.append("'GT'");
				filtros++;
			}
			if (superior) {
				if (filtros > 0) {
					sqlStr.append(" , ");
				}
				sqlStr.append("'SU'");
				filtros++;
			}
			if (posGraduacao) {
				if (filtros > 0) {
					sqlStr.append(" , ");
				}
				sqlStr.append("'PO'");
				filtros++;
			}
			if (profissionalizante) {
				if (filtros > 0) {
					sqlStr.append(" , ");
				}
				sqlStr.append("'PR'");
				filtros++;
			}
			sqlStr.append(" )");

		}
	}

	@Override
	public void validarReconhecimentoCurso(AutorizacaoCursoVO autorizacaoCursoVO, CursoVO cursoVO) throws Exception {
		try {
			if (!autorizacaoCursoVO.getEmTramitacao()) {
				if (!Uteis.isAtributoPreenchido(autorizacaoCursoVO.getNome())) {
					throw new Exception("O campo Reconhecimento (Reconhecimento Curso) deve ser informado.");
				}
				if (!Uteis.isAtributoPreenchido(autorizacaoCursoVO.getTipoAutorizacaoCursoEnum())) {
					throw new Exception("O campo Tipo (Reconhecimento Curso) deve ser informado.");
				}
			} else {
				if (!Uteis.isAtributoPreenchido(autorizacaoCursoVO.getNumero())) {
					throw new Exception("O Número Processo (Reconhecimento Curso) deve ser informado.");
				}
				if (!Uteis.isAtributoPreenchido(autorizacaoCursoVO.getTipoProcesso())) {
					throw new Exception("O Tipo do Processo (Reconhecimento Curso) deve ser informado.");
				}
				if (!Uteis.isAtributoPreenchido(autorizacaoCursoVO.getDataCadastro())) {
					throw new Exception("A Data do Cadastro (Reconhecimento Curso) deve ser informado.");
				}
				if (!Uteis.isAtributoPreenchido(autorizacaoCursoVO.getDataProtocolo())) {
					throw new Exception("A Data do Protocolo (Reconhecimento Curso) deve ser informado.");
				}
			}
			if (cursoVO.getAutorizacaoCursoVOs().isEmpty() && cursoVO.getAutorizacaoCursoVOs().stream().anyMatch(obj -> (!obj.getCodigo().equals(autorizacaoCursoVO.getCodigo())) && (obj.getNome().equals(autorizacaoCursoVO.getNome()) && obj.getData().equals(autorizacaoCursoVO.getData())))) {
				throw new Exception("Não é possível adicionar o mesmo Reconhecimento duas vezes.");
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public CursoVO consultaRapidaPorMatricula(String matricula, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("LEFT JOIN unidadeensinocurso ON unidadeensinocurso.curso = curso.codigo ");
		sqlStr.append("LEFT JOIN matricula ON matricula.curso = curso.codigo ");
		sqlStr.append(" WHERE matricula.matricula = '" + matricula + "' ");
		if (!(usuario.getUnidadeEnsinoLogado() == null || usuario.getUnidadeEnsinoLogado().getCodigo() == null || usuario.getUnidadeEnsinoLogado().getCodigo().intValue() == 0)) {
			sqlStr.append("AND unidadeensinocurso.unidadeEnsino = " + usuario.getUnidadeEnsinoLogado().getCodigo() + " ");
		}
		appendGroupBy(sqlStr);
		sqlStr.append("ORDER BY curso.codigo");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		CursoVO cursoVO = new CursoVO();
		if (tabelaResultado.next()) {
			montarDadosBasico(cursoVO, tabelaResultado, usuario);
		}
		return cursoVO;
	}

	public String consultarNivelEducacionalPorTurma(Integer turma) {
		StringBuilder sqlStr = new StringBuilder();
        
        sqlStr.append(" select codigo,turmaprincipal,turmaagrupada,curso, ");
        sqlStr.append(" case when (curso is null and turma.turmaprincipal is not null  and turma.turmaagrupada = false ) ");
        sqlStr.append(" then (select distinct niveleducacional from curso inner join turma t2 on t2.codigo = turma.turmaprincipal and t2.curso = curso.codigo) ");
        sqlStr.append(" when (curso is null and turma.turmaprincipal is null and turma.turmaagrupada) ");
        sqlStr.append(" then (select distinct niveleducacional from curso inner join turma t2 on t2.codigo = (select turmaagrupada.turma from turmaagrupada where turmaagrupada.turmaorigem = turma.codigo LIMIT 1) and t2.curso = curso.codigo) ");
        sqlStr.append(" when (curso is null and turma.turmaprincipal is not null and turma.turmaagrupada) ");
        sqlStr.append(" then (select distinct niveleducacional from curso inner join turma t2 on t2.codigo = (select turmaagrupada.turma from turmaagrupada where turmaagrupada.turmaorigem = turma.codigo LIMIT 1) and t2.curso = curso.codigo) ");
        sqlStr.append(" else (select niveleducacional from curso where turma.curso = curso.codigo) end as niveleducacional from turma where codigo = ");

		sqlStr.append(turma);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getString("nivelEducacional");
		}
		return "";
	}


	public String consultarNivelEducacionalPorTurmaAgrupadaUnico(Integer turmaAgrupada, UsuarioVO usuarioVO) {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select distinct niveleducacional from turma ");
		sqlStr.append(" inner join curso on curso.codigo = turma.curso ");
		sqlStr.append(" inner join turmaagrupada on turmaagrupada.turma = turma.codigo ");
		sqlStr.append(" where turmaagrupada.turmaorigem = ");
		sqlStr.append(turmaAgrupada);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getString("nivelEducacional");
		}
		return "";
	}

	public List<String> consultarNivelEducacionalPorTurmaAgrupada(Integer turmaAgrupada, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct nivelEducacional from curso ");
		sb.append(" inner join turma on turma.curso = curso.codigo ");
		sb.append(" inner join turmaagrupada on turmaagrupada.turma = turma.codigo ");
		sb.append(" where turmaagrupada.turmaorigem = ").append(turmaAgrupada);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<String> listaNivelEducacional = new ArrayList<String>(0);
		while (tabelaResultado.next()) {
			listaNivelEducacional.add(tabelaResultado.getString("nivelEducacional"));
		}
		return listaNivelEducacional;
	}

	 public List<TipoNivelEducacional> consultarNivelEducacionalProfessor(UsuarioVO usuarioVO) throws Exception {
	    	StringBuilder sqlStr = new StringBuilder("");
	    	sqlStr.append(" select distinct curso.nivelEducacional ");
			sqlStr.append(" from horarioturma ");
			sqlStr.append(" inner join turma on horarioturma.turma = turma.codigo ");
			sqlStr.append(" inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo  ");
			sqlStr.append(" inner join horarioturmadiaitem on horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia ");
			sqlStr.append(" inner join pessoa ON horarioturmadiaitem.professor = Pessoa.codigo  ");
			sqlStr.append(" inner join unidadeensino ON Turma.UnidadeEnsino = UnidadeEnsino.codigo ");
			sqlStr.append(" left join turmaagrupada on turmaagrupada.turmaorigem = turma.codigo ");
			sqlStr.append(" left join turma t2 on t2.codigo = turmaagrupada.turma ");
			sqlStr.append(" left join curso on curso.codigo = case when turma.turmaagrupada then t2.curso else turma.curso end ");
			sqlStr.append(" left join turno on turno.codigo = case when turma.turmaagrupada then t2.turno else turma.turno end ");
			sqlStr.append(" where Pessoa.codigo = ").append(usuarioVO.getPessoa().getCodigo());
			sqlStr.append(" AND turma.situacao = 'AB' ");
	    	SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
	    	List<TipoNivelEducacional> tipoNivelEducacionals = new ArrayList<TipoNivelEducacional>(0);
	    	while(rs.next()) {
	    		tipoNivelEducacionals.add(TipoNivelEducacional.getEnum(rs.getString("nivelEducacional")));
	    	}
	    	if(tipoNivelEducacionals.isEmpty()) {
	    		tipoNivelEducacionals.add(TipoNivelEducacional.SUPERIOR);
	    	}
	        return tipoNivelEducacionals;
	}

	public String consultarNivelEducacionalPorCodigoUsuario(Integer usuario) {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select curso.niveleducacional from curso ");
		sqlStr.append(" inner join matricula on matricula.curso = curso.codigo ");
		sqlStr.append(" inner join pessoa on pessoa.codigo = matricula.aluno ");
		sqlStr.append(" inner join usuario on usuario.pessoa = pessoa.codigo ");
		sqlStr.append("where usuario.codigo = ");
		sqlStr.append(usuario);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getString("niveleducacional");
		}
		return "";
	}

	public List<String> consultarListaNiveisEducacionaisPorCodigoUsuario(Integer usuario) {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select curso.niveleducacional from curso ");
		sqlStr.append(" inner join matricula on matricula.curso = curso.codigo ");
		sqlStr.append(" inner join pessoa on pessoa.codigo = matricula.aluno ");
		sqlStr.append(" inner join usuario on usuario.pessoa = pessoa.codigo ");
		sqlStr.append("where usuario.codigo = ");
		sqlStr.append(usuario);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<String> listaNiveis = new ArrayList<String>(0);
		while (tabelaResultado.next()) {
			listaNiveis.add(tabelaResultado.getString("niveleducacional"));
		}
		return listaNiveis;
	}

	public List<String> consultarListaNivelEducacionalCursoPorCodigoPessoaCoordenador(Integer pessoa) {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select curso.niveleducacional from curso ");
		sqlStr.append(" inner join cursocoordenador on curso.codigo = cursocoordenador.curso ");
		sqlStr.append(" inner join funcionario on funcionario.codigo = cursocoordenador.funcionario ");
		sqlStr.append(" inner join pessoa on pessoa.codigo = funcionario.pessoa ");
		sqlStr.append("where pessoa.codigo = ");
		sqlStr.append(pessoa);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<String> listaNiveis = new ArrayList<String>(0);
		while (tabelaResultado.next()) {
			listaNiveis.add(tabelaResultado.getString("niveleducacional"));
		}
		return listaNiveis;
	}

	public List<CursoVO> consultarPorProfessor(String nome, Integer professor, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("select distinct c.* from curso c ");
		sqlStr.append("left join unidadeensinocurso uec on uec.curso = c.codigo ");
		sqlStr.append("inner join turma t on t.curso = c.codigo ");
		sqlStr.append("inner join horarioturmaprofessordisciplina htpd on htpd.turma = t.codigo ");
		sqlStr.append("where lower(c.nome) like '").append(nome).append("%' and htpd.professor = ").append(professor).append(" ");
		if (unidadeEnsino != null && unidadeEnsino != 0) {
			sqlStr.append("and uec.unidadeensino = ").append(unidadeEnsino);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public CursoVO consultarCursoPorMatriculaParaInicializarNotaRapida(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "select curso.codigo, curso.nome, curso.nivelEducacional, curso.configuracaoAcademico, curso.periodicidade  from curso left join matricula on matricula.curso = curso.codigo where matricula.matricula = '" + valorConsulta + "' ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		CursoVO curso = new CursoVO();
		if (!tabelaResultado.next()) {
			return curso;
		}
		curso.setCodigo(tabelaResultado.getInt("codigo"));
		curso.setNome(tabelaResultado.getString("nome"));
		curso.setNivelEducacional(tabelaResultado.getString("nivelEducacional"));
		curso.getConfiguracaoAcademico().setCodigo(tabelaResultado.getInt("configuracaoAcademico"));
		curso.setPeriodicidade(tabelaResultado.getString("periodicidade"));
		return curso;
	}

	public List<CursoVO> consultarPorCodigoCursoProfessor(Integer codigoCurso, Integer professor, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("select distinct c.* from curso c ");
		sqlStr.append("left join unidadeensinocurso uec on uec.curso = c.codigo ");
		sqlStr.append("inner join turma t on t.curso = c.codigo ");
		sqlStr.append("inner join horarioturmaprofessordisciplina htpd on htpd.turma = t.codigo ");
		sqlStr.append("where c.codigo = ").append(codigoCurso).append(" and htpd.professor = ").append(professor).append(" ");
		if (unidadeEnsino != null && unidadeEnsino != 0) {
			sqlStr.append("and uec.unidadeensino = ").append(unidadeEnsino);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	@Override
	public List<CursoVO> consultarPorNomeCursoProfessor(String nomeCurso, Integer professor, Integer unidadeEnsino, Integer disciplina, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("select distinct curso.* from curso ");
		sqlStr.append("left join unidadeensinocurso on unidadeensinocurso.curso = curso.codigo ");
		sqlStr.append("inner join turma on (curso.codigo = turma.curso or curso.codigo in (select t.curso from turma t where t.codigo in (select turma from turmaagrupada where turmaorigem = turma.codigo))) ");
		sqlStr.append("inner join horarioturmaprofessordisciplina on horarioturmaprofessordisciplina.turma = turma.codigo ");
		sqlStr.append("WHERE LOWER (sem_acentos(curso.nome)) ILIKE (sem_acentos('").append(nomeCurso.toLowerCase()).append("%'))").append(" and horarioturmaprofessordisciplina.professor = ").append(professor).append(" ");
		if (unidadeEnsino != null && unidadeEnsino != 0) {
			sqlStr.append("and unidadeensinocurso.unidadeensino = ").append(unidadeEnsino);
		}
		if (Uteis.isAtributoPreenchido(disciplina)) {
			sqlStr.append(" and exists (SELECT distinct GradeCurricular.nome as GradeCurricular, 'GRADE_DISCIPLINA' AS origem, periodoLetivo.codigo as periodoLetivo, periodoLetivo.descricao as periodoLetivo_descricao, periodoLetivo.nomecertificacao as periodoLetivo_nomecertificacao, periodoLetivo.periodoLetivo as periodoLetivo_periodoLetivo, ");
			sqlStr.append(" (select disciplinaprerequisito.codigo from disciplinaprerequisito where disciplinaprerequisito.gradedisciplina = gradedisciplina.codigo limit 1 ) is not null as prerequisito, ");
			sqlStr.append(" disciplina.nome as disciplina, gradedisciplina.tipodisciplina, ");
			sqlStr.append(" gradedisciplina.codigo as codigoOrigem, gradedisciplina.cargahoraria, gradedisciplina.horaaula, gradedisciplina.cargahorariapratica, gradedisciplina.cargahoraria - gradedisciplina.cargahorariapratica as  cargahorariateorica ");
			sqlStr.append(" FROM GradeCurricular ");
			sqlStr.append(" inner join periodoLetivo on periodoLetivo.GradeCurricular = GradeCurricular.codigo ");
			sqlStr.append(" inner join gradedisciplina on gradedisciplina.periodoLetivo = periodoLetivo.codigo ");
			sqlStr.append(" inner join disciplina on gradedisciplina.disciplina = disciplina.codigo ");
			sqlStr.append(" WHERE GradeCurricular.curso = curso.codigo ");
			sqlStr.append(" and  disciplina.codigo = ").append(disciplina);

			sqlStr.append(" union SELECT distinct GradeCurricular.nome as GradeCurricular, 'GRADE_DISCIPLINA_COMPOSTA' AS origem, periodoLetivo.codigo as periodoLetivo,  periodoLetivo.descricao as periodoLetivo_descricao, periodoLetivo.nomecertificacao as periodoLetivo_nomecertificacao, periodoLetivo.periodoLetivo as periodoLetivo_periodoLetivo, ");
			sqlStr.append(" (select disciplinaprerequisito.codigo from disciplinaprerequisito where disciplinaprerequisito.gradedisciplinacomposta = gradedisciplinacomposta.codigo limit 1 ) is not null as prerequisito, ");
			sqlStr.append(" disciplina.nome as disciplina, gradedisciplina.tipodisciplina, ");
			sqlStr.append(" gradedisciplinacomposta.codigo as codigoOrigem, gradedisciplinacomposta.cargahoraria, gradedisciplinacomposta.horaaula, gradedisciplinacomposta.cargahorariapratica, gradedisciplinacomposta.cargahorariateorica ");
			sqlStr.append(" FROM GradeCurricular ");
			sqlStr.append(" inner join periodoLetivo on periodoLetivo.GradeCurricular = GradeCurricular.codigo ");
			sqlStr.append(" inner join gradedisciplina on gradedisciplina.periodoLetivo = periodoLetivo.codigo ");
			sqlStr.append(" inner join gradedisciplinacomposta on gradedisciplina.codigo = gradedisciplinacomposta.gradedisciplina ");
			sqlStr.append(" inner join disciplina on gradedisciplinacomposta.disciplina = disciplina.codigo ");
			sqlStr.append(" WHERE GradeCurricular.curso = curso.codigo ");
			sqlStr.append(" and  disciplina.codigo = ").append(disciplina);

			sqlStr.append(" union (SELECT distinct GradeCurricular.nome as GradeCurricular, 'GRUPO_OPTATIVA' AS origem, periodoLetivo.codigo as periodoLetivo,  periodoLetivo.descricao as periodoLetivo_descricao, periodoLetivo.nomecertificacao as periodoLetivo_nomecertificacao, periodoLetivo.periodoLetivo as periodoLetivo_periodoLetivo, ");
			sqlStr.append(" (select disciplinaprerequisito.codigo from disciplinaprerequisito where disciplinaprerequisito.gradecurriculargrupooptativadisciplina = gradecurriculargrupooptativadisciplina.codigo limit 1 ) is not null as prerequisito, ");
			sqlStr.append(" disciplina.nome as disciplina, 'OP' as tipodisciplina, ");
			sqlStr.append(" gradecurriculargrupooptativadisciplina.codigo as codigoOrigem, gradecurriculargrupooptativadisciplina.cargahoraria, gradecurriculargrupooptativadisciplina.horaaula, gradecurriculargrupooptativadisciplina.cargahorariapratica, gradecurriculargrupooptativadisciplina.cargahoraria - gradecurriculargrupooptativadisciplina.cargahorariapratica as  cargahorariateorica ");
			sqlStr.append(" FROM GradeCurricular ");
			sqlStr.append(" inner join periodoLetivo on periodoLetivo.GradeCurricular = GradeCurricular.codigo ");
			sqlStr.append(" inner join gradecurriculargrupooptativa on periodoLetivo.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo ");
			sqlStr.append(" inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativa.codigo = gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa ");
			sqlStr.append(" inner join disciplina on gradecurriculargrupooptativadisciplina.disciplina = disciplina.codigo ");
			sqlStr.append(" WHERE GradeCurricular.curso = curso.codigo ");
			sqlStr.append(" and  disciplina.codigo = ").append(disciplina);
			sqlStr.append(" order by periodoLetivo.periodoLetivo limit 1) ");

			sqlStr.append(" union (SELECT distinct GradeCurricular.nome as GradeCurricular, 'GRADE_DISCIPLINA_COMPOSTA' AS origem, periodoLetivo.codigo as periodoLetivo,  periodoLetivo.descricao as periodoLetivo_descricao, periodoLetivo.nomecertificacao as periodoLetivo_nomecertificacao, periodoLetivo.periodoLetivo as periodoLetivo_periodoLetivo, ");
			sqlStr.append(" (select disciplinaprerequisito.codigo from disciplinaprerequisito where disciplinaprerequisito.gradedisciplinacomposta = gradedisciplinacomposta.codigo limit 1 ) is not null as prerequisito, ");
			sqlStr.append(" disciplina.nome as disciplina, 'OP' as tipodisciplina, ");
			sqlStr.append(" gradedisciplinacomposta.codigo as codigoOrigem, gradedisciplinacomposta.cargahoraria, gradedisciplinacomposta.horaaula, gradedisciplinacomposta.cargahorariapratica, gradedisciplinacomposta.cargahorariateorica ");
			sqlStr.append(" FROM GradeCurricular ");
			sqlStr.append(" inner join periodoLetivo on periodoLetivo.GradeCurricular = GradeCurricular.codigo ");
			sqlStr.append(" inner join gradecurriculargrupooptativa on periodoLetivo.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo ");
			sqlStr.append(" inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativa.codigo = gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa ");
			sqlStr.append(" inner join gradedisciplinacomposta on gradecurriculargrupooptativadisciplina.codigo = gradedisciplinacomposta.gradecurriculargrupooptativadisciplina ");
			sqlStr.append(" inner join disciplina on gradedisciplinacomposta.disciplina = disciplina.codigo ");
			sqlStr.append(" WHERE GradeCurricular.curso = curso.codigo ");
			sqlStr.append(" and  disciplina.codigo = ").append(disciplina);
			sqlStr.append(" order by periodoLetivo.periodoLetivo limit 1) ");
			sqlStr.append(" limit 1) ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<CursoVO> consultaRapidaPorNomeAutoComplete(String valorConsulta, Integer codigoUnidadeEnsino, int limit, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<UnidadeEnsinoVO> unidadeEnsinoVOs = new ArrayList<>(0);
		if (Uteis.isAtributoPreenchido(codigoUnidadeEnsino)) {
			unidadeEnsinoVOs.add(new UnidadeEnsinoVO(codigoUnidadeEnsino));
		}
		return consultaRapidaPorNomeAutoComplete(valorConsulta, unidadeEnsinoVOs, limit, controlarAcesso, nivelMontarDados, usuario);
	}
	
	@Override
	public List<CursoVO> consultaRapidaPorNomeAutoComplete(String valorConsulta, List<UnidadeEnsinoVO> unidadeEnsinoVOs, int limit, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("select distinct curso.codigo, curso.nome ");
		sqlStr.append("from curso ");
		sqlStr.append("LEFT JOIN unidadeensinocurso ON unidadeensinocurso.curso = curso.codigo ");
		sqlStr.append(" WHERE upper(sem_acentos(curso.nome)) like (upper(sem_acentos('%" + valorConsulta.toLowerCase() + "%'))) ");
		if (Uteis.isAtributoPreenchido(unidadeEnsinoVOs)) {
			sqlStr.append("AND unidadeensinocurso.unidadeEnsino IN (").append(unidadeEnsinoVOs.stream().map(u -> u.getCodigo().toString()).collect(Collectors.joining(", "))).append(") ");
		}
		sqlStr.append(" ORDER BY curso.nome ");
		sqlStr.append(" limit ").append(limit);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<CursoVO> listaCurso = new ArrayList<CursoVO>();
		while (tabelaResultado.next()) {
			CursoVO obj = new CursoVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setNome(tabelaResultado.getString("nome"));
			listaCurso.add(obj);
		}
		return listaCurso;
	}

	@Override
	public List consultarPorCodigoEspecifico(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, Boolean apresentarCursoBiblioteca) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr;
		if (Uteis.isAtributoPreenchido(valorConsulta)) {
			sqlStr = "SELECT * FROM Curso WHERE codigo >= " + valorConsulta;
		} else {
			sqlStr = "SELECT * FROM Curso WHERE codigo = " + valorConsulta;
		}
		if (Uteis.isAtributoPreenchido(apresentarCursoBiblioteca) && apresentarCursoBiblioteca) {
			sqlStr += " AND apresentarCursoBiblioteca = 'true' ";
		}
		sqlStr += " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	@Override
	public List consultarPorNomeEpecifico(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, Boolean apresentarCursoBiblioteca) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr;
		sqlStr = "SELECT * FROM curso WHERE lower (sem_acentos(nome)) ilike('";
		if (valorConsulta != null) {
			sqlStr += Uteis.removerAcentuacao(valorConsulta.toLowerCase());
		}
		sqlStr += "%') ";
		if (Uteis.isAtributoPreenchido(apresentarCursoBiblioteca)) {
			if (apresentarCursoBiblioteca) {
				sqlStr += " AND apresentarCursoBiblioteca = 'true' ";
			}
		}
		sqlStr += " ORDER BY nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	@Override
	public List<CursoVO> consultarCursoPorCodigoUnidadeEnsino(Integer codigoCurso, Integer unidadeEnsinoCodigo, boolean controleAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controleAcesso, usuario);
		String sqlStr = "SELECT distinct curso.* FROM unidadeensinocurso, curso, unidadeensino " + "where unidadeensinocurso.unidadeensino = unidadeensino.codigo and unidadeensinocurso.curso = curso.codigo " + "and curso.codigo=" + codigoCurso;
		if (unidadeEnsinoCodigo != 0) {
			sqlStr += "and unidadeensinocurso.unidadeensino = " + unidadeEnsinoCodigo.intValue();
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	@Override
	public List<CursoVO> consultarPorNomeCursoUnidadeEnsinoBasica(String valorConsulta, Integer unidadeEnsinoCodigo, boolean b, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), b, usuario);
		String sqlStr = "SELECT distinct curso.* FROM unidadeensinocurso, curso, unidadeensino " + "where unidadeensinocurso.unidadeensino = unidadeensino.codigo and unidadeensinocurso.curso = curso.codigo " + "and curso.nome ilike('" + valorConsulta + "%') ";
		if (unidadeEnsinoCodigo != 0) {
			sqlStr += "and unidadeensinocurso.unidadeensino = " + unidadeEnsinoCodigo.intValue();
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<CursoVO> consultarTodosCursos() {
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct codigo, nome from curso order by nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<CursoVO> listaCursoVOs = new ArrayList<CursoVO>(0);
		while (tabelaResultado.next()) {
			CursoVO obj = new CursoVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setNome(tabelaResultado.getString("nome"));
			listaCursoVOs.add(obj);
		}
		return listaCursoVOs;
	}

	public List consultarNotasPorCurso(Integer codigoCurso) throws Exception {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append(" select utilizarnota1, tituloNota1, utilizarnota2, tituloNota2, utilizarnota3, tituloNota3, utilizarnota4, tituloNota4, ");
		sqlStr.append(" utilizarnota5, tituloNota5, utilizarnota6, tituloNota6, utilizarnota7, tituloNota7, utilizarnota8, tituloNota8, ");
		sqlStr.append(" utilizarnota9, tituloNota9, utilizarnota10, tituloNota10, utilizarnota11, tituloNota11, utilizarnota12, tituloNota12, utilizarnota13, tituloNota13, ");
		sqlStr.append(" utilizarnota14, tituloNota14, utilizarnota15, tituloNota15, utilizarnota16, tituloNota16, utilizarnota17, tituloNota17, utilizarnota18, tituloNota18, ");
		sqlStr.append(" utilizarnota15, tituloNota15, utilizarnota16, tituloNota16, utilizarnota17, tituloNota17, utilizarnota18, tituloNota18, utilizarnota19, tituloNota19, ");
		sqlStr.append(" utilizarnota20, tituloNota20, utilizarnota21, tituloNota21, utilizarnota22, tituloNota22, utilizarnota23, tituloNota23, utilizarnota24, tituloNota24, ");
		sqlStr.append(" utilizarnota25, tituloNota25, utilizarnota26, tituloNota26, utilizarnota27, tituloNota27, utilizarnota28, tituloNota28, utilizarnota29, tituloNota29, ");
		sqlStr.append(" utilizarnota30, tituloNota30, bimestreNota1,bimestreNota2, bimestreNota3, bimestreNota4, bimestreNota5, bimestreNota6, bimestreNota7, bimestreNota8,");
		sqlStr.append(" bimestreNota9, bimestreNota10, bimestreNota11, bimestreNota12, bimestreNota13, bimestreNota14, bimestreNota15, bimestreNota16, bimestreNota17, bimestreNota18, ");
		sqlStr.append(" bimestreNota19, bimestreNota20, bimestreNota21, bimestreNota22, bimestreNota23, bimestreNota24, bimestreNota25, bimestreNota26, bimestreNota27, bimestreNota28,");
		sqlStr.append(" bimestreNota29, bimestreNota30 ");
		sqlStr.append(" from curso ");
		sqlStr.append(" inner join configuracaoacademico on configuracaoacademico.codigo = curso.configuracaoacademico ");
		sqlStr.append(" where curso.codigo = ").append(codigoCurso);

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List lista = new ArrayList(0);

		if (tabelaResultado.next()) {
			for (int i = 1; i <= 30; i++) {
				if (tabelaResultado.getBoolean("utilizarnota" + i)) {
					lista.add(tabelaResultado.getString("tituloNota" + i) + 
							(tabelaResultado.getString("bimestreNota" + i) != null || tabelaResultado.getString("bimestreNota" + i) != "" ? "-" + BimestreEnum.getEnum(tabelaResultado.getString("bimestreNota" + i)).getValorApresentar() :""));
				}
			}
		}

		return lista;
	}

	public List<CursoVO> consultarListaCursoPorCodigoPessoaCoordenador(Integer pessoa, Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
		StringBuffer str = new StringBuffer();
		str.append("select distinct curso.* from cursocoordenador ");
		str.append(" inner join funcionario on funcionario.codigo = cursocoordenador.funcionario  ");
		str.append(" inner join curso on curso.codigo = cursocoordenador.curso  ");
		str.append(" where funcionario.pessoa = ").append(pessoa);
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			str.append(" and (cursocoordenador.unidadeensino is null or cursocoordenador.unidadeensino = ").append(unidadeEnsino).append(")");		}
		str.append(" order by curso.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(str.toString());
		return montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, usuario);
	}

	@Override
	public List<CursoVO> consultarListaCursoPorCodigoCursoCodigoPessoaCoordenador(Integer codigoCurso, Integer pessoa, Integer unidadeEnsino, Integer disciplina, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("select distinct curso.* from cursocoordenador ");
		sqlStr.append(" inner join funcionario on funcionario.codigo = cursocoordenador.funcionario  ");
		sqlStr.append(" inner join curso on curso.codigo = cursocoordenador.curso  ");
		sqlStr.append(" where funcionario.pessoa = ").append(pessoa);
		sqlStr.append(" and curso.codigo = ").append(codigoCurso).append("  ");
		filtroConsultarVisaoCoordenador(unidadeEnsino, disciplina, sqlStr);
		sqlStr.append(" order by curso.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}	
	
	@Override
	public List<CursoVO> consultarListaCursoPorNomeCursoCodigoPessoaCoordenador(String nomeCurso, Integer pessoa, Integer unidadeEnsino, Integer disciplina, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("select distinct curso.* from cursocoordenador ");
		sqlStr.append(" inner join funcionario on funcionario.codigo = cursocoordenador.funcionario  ");
		sqlStr.append(" inner join curso on curso.codigo = cursocoordenador.curso  ");
		sqlStr.append(" where funcionario.pessoa = ").append(pessoa);
		sqlStr.append(" and sem_acentos(curso.nome) ilike (sem_acentos(?)) ");
		filtroConsultarVisaoCoordenador(unidadeEnsino, disciplina, sqlStr);
		sqlStr.append(" order by curso.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), nomeCurso + PERCENT);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}
	
	private void filtroConsultarVisaoCoordenador(Integer unidadeEnsino, Integer disciplina, StringBuffer sqlStr) {
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sqlStr.append(" and (cursocoordenador.unidadeensino is null or cursocoordenador.unidadeensino = ").append(unidadeEnsino).append(") ");
		}
		if (Uteis.isAtributoPreenchido(disciplina)) {
			sqlStr.append(" and exists (SELECT distinct GradeCurricular.nome as GradeCurricular, 'GRADE_DISCIPLINA' AS origem, periodoLetivo.codigo as periodoLetivo, periodoLetivo.descricao as periodoLetivo_descricao, periodoLetivo.nomecertificacao as periodoLetivo_nomecertificacao, periodoLetivo.periodoLetivo as periodoLetivo_periodoLetivo, ");
			sqlStr.append(" (select disciplinaprerequisito.codigo from disciplinaprerequisito where disciplinaprerequisito.gradedisciplina = gradedisciplina.codigo limit 1 ) is not null as prerequisito, ");
			sqlStr.append(" disciplina.nome as disciplina, gradedisciplina.tipodisciplina, ");
			sqlStr.append(" gradedisciplina.codigo as codigoOrigem, gradedisciplina.cargahoraria, gradedisciplina.horaaula, gradedisciplina.cargahorariapratica, gradedisciplina.cargahoraria - gradedisciplina.cargahorariapratica as  cargahorariateorica ");
			sqlStr.append(" FROM GradeCurricular ");
			sqlStr.append(" inner join periodoLetivo on periodoLetivo.GradeCurricular = GradeCurricular.codigo ");
			sqlStr.append(" inner join gradedisciplina on gradedisciplina.periodoLetivo = periodoLetivo.codigo ");
			sqlStr.append(" inner join disciplina on gradedisciplina.disciplina = disciplina.codigo ");
			sqlStr.append(" WHERE GradeCurricular.curso = curso.codigo ");
			sqlStr.append(" and  disciplina.codigo = ").append(disciplina);

			sqlStr.append(" union SELECT distinct GradeCurricular.nome as GradeCurricular, 'GRADE_DISCIPLINA_COMPOSTA' AS origem, periodoLetivo.codigo as periodoLetivo,  periodoLetivo.descricao as periodoLetivo_descricao, periodoLetivo.nomecertificacao as periodoLetivo_nomecertificacao, periodoLetivo.periodoLetivo as periodoLetivo_periodoLetivo, ");
			sqlStr.append(" (select disciplinaprerequisito.codigo from disciplinaprerequisito where disciplinaprerequisito.gradedisciplinacomposta = gradedisciplinacomposta.codigo limit 1 ) is not null as prerequisito, ");
			sqlStr.append(" disciplina.nome as disciplina, gradedisciplina.tipodisciplina, ");
			sqlStr.append(" gradedisciplinacomposta.codigo as codigoOrigem, gradedisciplinacomposta.cargahoraria, gradedisciplinacomposta.horaaula, gradedisciplinacomposta.cargahorariapratica, gradedisciplinacomposta.cargahorariateorica ");
			sqlStr.append(" FROM GradeCurricular ");
			sqlStr.append(" inner join periodoLetivo on periodoLetivo.GradeCurricular = GradeCurricular.codigo ");
			sqlStr.append(" inner join gradedisciplina on gradedisciplina.periodoLetivo = periodoLetivo.codigo ");
			sqlStr.append(" inner join gradedisciplinacomposta on gradedisciplina.codigo = gradedisciplinacomposta.gradedisciplina ");
			sqlStr.append(" inner join disciplina on gradedisciplinacomposta.disciplina = disciplina.codigo ");
			sqlStr.append(" WHERE GradeCurricular.curso = curso.codigo ");
			sqlStr.append(" and  disciplina.codigo = ").append(disciplina);

			sqlStr.append(" union (SELECT distinct GradeCurricular.nome as GradeCurricular, 'GRUPO_OPTATIVA' AS origem, periodoLetivo.codigo as periodoLetivo,  periodoLetivo.descricao as periodoLetivo_descricao, periodoLetivo.nomecertificacao as periodoLetivo_nomecertificacao, periodoLetivo.periodoLetivo as periodoLetivo_periodoLetivo, ");
			sqlStr.append(" (select disciplinaprerequisito.codigo from disciplinaprerequisito where disciplinaprerequisito.gradecurriculargrupooptativadisciplina = gradecurriculargrupooptativadisciplina.codigo limit 1 ) is not null as prerequisito, ");
			sqlStr.append(" disciplina.nome as disciplina, 'OP' as tipodisciplina, ");
			sqlStr.append(" gradecurriculargrupooptativadisciplina.codigo as codigoOrigem, gradecurriculargrupooptativadisciplina.cargahoraria, gradecurriculargrupooptativadisciplina.horaaula, gradecurriculargrupooptativadisciplina.cargahorariapratica, gradecurriculargrupooptativadisciplina.cargahoraria - gradecurriculargrupooptativadisciplina.cargahorariapratica as  cargahorariateorica ");
			sqlStr.append(" FROM GradeCurricular ");
			sqlStr.append(" inner join periodoLetivo on periodoLetivo.GradeCurricular = GradeCurricular.codigo ");
			sqlStr.append(" inner join gradecurriculargrupooptativa on periodoLetivo.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo ");
			sqlStr.append(" inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativa.codigo = gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa ");
			sqlStr.append(" inner join disciplina on gradecurriculargrupooptativadisciplina.disciplina = disciplina.codigo ");
			sqlStr.append(" WHERE GradeCurricular.curso = curso.codigo ");
			sqlStr.append(" and  disciplina.codigo = ").append(disciplina);
			sqlStr.append(" order by periodoLetivo.periodoLetivo limit 1) ");

			sqlStr.append(" union (SELECT distinct GradeCurricular.nome as GradeCurricular, 'GRADE_DISCIPLINA_COMPOSTA' AS origem, periodoLetivo.codigo as periodoLetivo,  periodoLetivo.descricao as periodoLetivo_descricao, periodoLetivo.nomecertificacao as periodoLetivo_nomecertificacao, periodoLetivo.periodoLetivo as periodoLetivo_periodoLetivo, ");
			sqlStr.append(" (select disciplinaprerequisito.codigo from disciplinaprerequisito where disciplinaprerequisito.gradedisciplinacomposta = gradedisciplinacomposta.codigo limit 1 ) is not null as prerequisito, ");
			sqlStr.append(" disciplina.nome as disciplina, 'OP' as tipodisciplina, ");
			sqlStr.append(" gradedisciplinacomposta.codigo as codigoOrigem, gradedisciplinacomposta.cargahoraria, gradedisciplinacomposta.horaaula, gradedisciplinacomposta.cargahorariapratica, gradedisciplinacomposta.cargahorariateorica ");
			sqlStr.append(" FROM GradeCurricular ");
			sqlStr.append(" inner join periodoLetivo on periodoLetivo.GradeCurricular = GradeCurricular.codigo ");
			sqlStr.append(" inner join gradecurriculargrupooptativa on periodoLetivo.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo ");
			sqlStr.append(" inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativa.codigo = gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa ");
			sqlStr.append(" inner join gradedisciplinacomposta on gradecurriculargrupooptativadisciplina.codigo = gradedisciplinacomposta.gradecurriculargrupooptativadisciplina ");
			sqlStr.append(" inner join disciplina on gradedisciplinacomposta.disciplina = disciplina.codigo ");
			sqlStr.append(" WHERE GradeCurricular.curso = curso.codigo ");
			sqlStr.append(" and  disciplina.codigo = ").append(disciplina);
			sqlStr.append(" order by periodoLetivo.periodoLetivo limit 1) ");
			sqlStr.append(" limit 1) ");
		}
	}

	public List<CursoVO> consultarCursoPorProfessorNivelDadosCombobox(Integer codigoPessoa, String semestre, String ano, String situacao, Integer areaConhecimento, Integer unidadeEnsino, Boolean visaoProfessor, Boolean nivelEducacionalPosGraduacao, Boolean nivelEducacionalDiferentePosGraduacao, PeriodicidadeEnum periodicidadeEnum, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder("(Select distinct curso.* from Turma");
		sqlStr.append(" INNER JOIN HorarioTurma on (Turma.codigo = horarioTurma.Turma) ");
		sqlStr.append(" INNER JOIN HorarioTurmadia on (HorarioTurmadia.HorarioTurma = horarioTurma.codigo) ");
		sqlStr.append(" INNER JOIN HorarioTurmadiaitem on (HorarioTurmadiaitem.HorarioTurmadia = HorarioTurmadia.codigo) ");
		sqlStr.append(" Inner Join pessoa ON HorarioTurmadiaitem.professor = Pessoa.codigo ");
		sqlStr.append(" Inner join unidadeensino ON Turma.UnidadeEnsino = UnidadeEnsino.codigo");
		sqlStr.append(" Inner join curso on curso.codigo = turma.curso");
		sqlStr.append(" left  join areaConhecimento on curso.areaConhecimento = areaConhecimento.codigo");
		sqlStr.append(" left  join turno on turno.codigo = turma.turno");
		if (visaoProfessor) {
			sqlStr.append(" Inner join unidadeensinocurso ON ( turma.unidadeensino = unidadeensinocurso.unidadeensino) and unidadeensinocurso.curso = turma.curso and unidadeensinocurso.turno = turma.turno ");
			sqlStr.append(" Inner join processomatriculaunidadeensino ON unidadeensinocurso.unidadeensino = processomatriculaunidadeensino.unidadeensino");
			sqlStr.append(" Inner join processomatriculacalendario ON processomatriculacalendario.processomatricula = processomatriculaunidadeensino.processomatricula and processomatriculacalendario.curso = unidadeensinocurso.curso and processomatriculacalendario.turno = unidadeensinocurso.turno ");
			sqlStr.append(" Inner Join periodoletivoativounidadeensinocurso ON processomatriculacalendario.periodoletivoativounidadeensinocurso = periodoletivoativounidadeensinocurso.codigo");
			if ((semestre != null && !semestre.trim().isEmpty()) || (ano != null && !ano.trim().isEmpty())) {
				sqlStr.append(" where  ((Turma.semestral and HorarioTurma.semestrevigente = '").append(semestre).append("' and HorarioTurma.anovigente = '").append(ano).append("') ");
				sqlStr.append(" or (Turma.anual and HorarioTurma.anovigente = '").append(ano).append("') ");
				sqlStr.append(" or (Turma.anual = false and turma.semestral = false)) ");

				sqlStr.append(" and  ((Turma.semestral and periodoletivoativounidadeensinocurso.semestrereferenciaperiodoletivo = '").append(semestre).append("' and periodoletivoativounidadeensinocurso.anoreferenciaperiodoletivo = '").append(ano).append("') ");
				sqlStr.append(" or (Turma.anual and periodoletivoativounidadeensinocurso.anoreferenciaperiodoletivo = '").append(ano).append("') ");
				sqlStr.append(" or (Turma.anual = false and turma.semestral = false) and periodoletivoativounidadeensinocurso.anoreferenciaperiodoletivo='' and periodoletivoativounidadeensinocurso.semestrereferenciaperiodoletivo='')) ");
			} else {
				sqlStr.append(" where ((turma.semestral and periodoletivoativounidadeensinocurso.anoreferenciaperiodoletivo = HorarioTurma.anovigente ");
				sqlStr.append(" and periodoletivoativounidadeensinocurso.semestrereferenciaperiodoletivo = HorarioTurma.semestrevigente) ");
				sqlStr.append(" or (turma.anual and periodoletivoativounidadeensinocurso.anoreferenciaperiodoletivo = HorarioTurma.anovigente) ");
				sqlStr.append(" or (turma.anual = false and turma.semestral = false)) ");
			}

			if (Uteis.isAtributoPreenchido(situacao)) {
				sqlStr.append(" AND periodoletivoativounidadeensinocurso.situacao = '").append(situacao).append("'");
			}
			if (Uteis.isAtributoPreenchido(areaConhecimento)) {
				sqlStr.append(" AND areaConhecimento.codigo = ").append(areaConhecimento).append("");
			}
			sqlStr.append(" and Pessoa.codigo = ").append(codigoPessoa.intValue());
		} else {
			sqlStr.append(" where Pessoa.codigo = ").append(codigoPessoa.intValue());
		}
		if (unidadeEnsino.intValue() != 0) {
			sqlStr.append(" and UnidadeEnsino.codigo = ").append(unidadeEnsino.intValue());
		}
		if (visaoProfessor) {
			if (nivelEducacionalPosGraduacao != null && nivelEducacionalPosGraduacao) {
				sqlStr.append(" AND ((curso.nivelEducacional in ('PO','EX')) OR turmaagrupada = true)");
			}
			if (nivelEducacionalDiferentePosGraduacao != null && nivelEducacionalDiferentePosGraduacao) {
				sqlStr.append(" AND (curso.nivelEducacional != 'PO' OR turmaagrupada = true)");
			}
		}
		if(Uteis.isAtributoPreenchido(periodicidadeEnum)) {
			sqlStr.append(" AND curso.periodicidade = '").append(periodicidadeEnum.getValor()).append("'");
		}
		sqlStr.append(" ) ");

		sqlStr.append(" union");
		sqlStr.append(" (select distinct curso.* ");
		sqlStr.append(" from Turma");
		sqlStr.append(" inner join programacaotutoriaonline on  programacaotutoriaonline.turma = turma.codigo");
		sqlStr.append(" inner join programacaotutoriaonlineprofessor ON programacaotutoriaonlineprofessor.programacaotutoriaonline = programacaotutoriaonline.codigo");
		sqlStr.append(" inner join curso on curso.codigo = turma.curso");
		sqlStr.append(" left join turno on turno.codigo = turma.turno");
		sqlStr.append(" left join unidadeensino ON Turma.UnidadeEnsino = UnidadeEnsino.codigo");
		sqlStr.append(" where programacaotutoriaonlineprofessor.professor = ").append(codigoPessoa.intValue());
		if(Uteis.isAtributoPreenchido(periodicidadeEnum)) {
			sqlStr.append(" AND curso.periodicidade = '").append(periodicidadeEnum.getValor()).append("'");
		}
		sqlStr.append(" )");

		sqlStr.append(" order by nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, usuario);
	}

	public Boolean consultarLiberarRegistroAulaEntrePeriodoPorTurma(Integer turma, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select liberarregistroaulaentreperiodo from curso ");
		sb.append(" inner join turma on turma.curso = curso.codigo ");
		sb.append(" where turma.codigo = ").append(turma);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getBoolean("liberarregistroaulaentreperiodo");
		}
		return Boolean.FALSE;
	}

	@Override
	public CursoVO consultarCursoPorTurmaPrincipalSubturma(Integer turma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("SELECT * FROM curso ");
		sqlStr.append("INNER JOIN turma ON turma.curso = curso.codigo ");
		sqlStr.append("WHERE turma.codigo = ").append(turma);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			return new CursoVO();
		}
		return (montarDados(tabelaResultado, nivelMontarDados, false, usuario));
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public CursoObject consultarPorChavePrimariaMatriculaExterna(Integer codigoCurso) throws Exception {
		String sql = "SELECT * FROM Curso WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoCurso });
		CursoObject cursoObject = null;
		if (tabelaResultado.next()) {
			cursoObject = new CursoObject();
			cursoObject.setCodigo(tabelaResultado.getInt("codigo"));
			cursoObject.setNome(tabelaResultado.getString("nome"));
			cursoObject.setGradeDisciplinaObject(getFacadeFactory().getGradeCurricularFacade().consultarGradeDisciplinaCursoMatriculaOnlineExterna(codigoCurso));
		}
		return cursoObject;
	}

	/**
	 * Responsável por realizar a consulta de cursos das turmas que fazem parte do agrupamento com base na turma origem.
	 * 
	 * @author Wellington - 9 de dez de 2015
	 * @param turmaOrigem
	 * @param controlarAcesso
	 * @param nivelMontarDados
	 * @param usuario
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<CursoVO> consultarCursoTurmasAgrupadasPorTurmaOrigem(Integer turmaOrigem, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("SELECT distinct curso.* FROM curso ");
		sqlStr.append("INNER JOIN turma ON turma.curso = curso.codigo ");
		sqlStr.append("INNER JOIN turmaagrupada on turmaagrupada.turma = turma.codigo ");
		sqlStr.append("WHERE turmaagrupada.turmaorigem = ").append(turmaOrigem);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<CursoVO> consultarCursoPorProcessoSeletivoUnidadeEnsinos(List<ProcSeletivoVO> procSeletivoVOs, List<UnidadeEnsinoVO> unidadeEnsinoVOs) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct curso.codigo, curso.nome from curso ");
		sb.append(" inner join unidadeensinocurso ON unidadeensinocurso.curso = curso.codigo ");
		sb.append(" inner join procseletivocurso on procseletivocurso.unidadeensinocurso = unidadeEnsinoCurso.codigo ");
		sb.append(" inner join procseletivounidadeensino on procseletivounidadeensino.codigo = procseletivocurso.procseletivounidadeensino ");
		sb.append(" and procseletivounidadeensino.unidadeensino = unidadeEnsinoCurso.unidadeensino ");
		sb.append(" where 1=1 ");
		if (!procSeletivoVOs.isEmpty()) {
			sb.append(" and procseletivounidadeensino.procSeletivo in (");
			for (ProcSeletivoVO procSeletivoVO : procSeletivoVOs) {
				if (procSeletivoVO.getFiltrarProcessoSeletivo()) {
					sb.append(procSeletivoVO.getCodigo()).append(", ");
				}
			}
			sb.append("0) ");
		}
		if (!unidadeEnsinoVOs.isEmpty()) {
			sb.append(" and unidadeensinocurso.unidadeensino in (");
			for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOs) {
				if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
					sb.append(unidadeEnsinoVO.getCodigo()).append(", ");
				}
			}
			sb.append("0) ");
		}
		sb.append(" order by curso.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		CursoVO obj = null;
		List<CursoVO> listaCursoVOs = null;
		while (tabelaResultado.next()) {
			if (listaCursoVOs == null) {
				listaCursoVOs = new ArrayList<CursoVO>(0);
			}
			obj = new CursoVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setNome(tabelaResultado.getString("nome"));
			listaCursoVOs.add(obj);
		}
		return listaCursoVOs;
	}

	@Override
	public List<CursoVO> consultarTodosCursosPorUnidadeEnsino(List<UnidadeEnsinoVO> unidadeEnsinoVOs) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT DISTINCT curso.codigo , curso.nome FROM curso ");
		if (!unidadeEnsinoVOs.isEmpty()) {
			sb.append(" INNER JOIN unidadeensinocurso ON unidadeensinocurso.curso    = curso.codigo ");
			sb.append(" INNER JOIN unidadeensino ON unidadeensinocurso.unidadeensino = unidadeensino.codigo ");
			sb.append(" WHERE unidadeensino.codigo  IN (");
			int x = 0;
			for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOs) {
				if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
					if (x > 0) {
						sb.append(", ");
					}
					sb.append(unidadeEnsinoVO.getCodigo());
					x++;
				}
			}
			sb.append(" ) ");
		}
		sb.append(" ORDER BY curso.nome;");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<CursoVO> listaCursoVOs = new ArrayList<CursoVO>(0);
		while (tabelaResultado.next()) {
			CursoVO obj = new CursoVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setNome(tabelaResultado.getString("nome"));
			listaCursoVOs.add(obj);
		}
		return listaCursoVOs;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public String consultarPeriodicidadePorMatricula(String matricula, UsuarioVO usuario) throws Exception {
		String sql = "SELECT c.periodicidade FROM Curso as c inner join matricula as m on m.curso = c.codigo WHERE m.matricula = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, matricula);
		if (tabelaResultado.next()) {
			return tabelaResultado.getString("periodicidade");
		} else {
			return "";
		}
	}

	@Override
	public List<CursoVO> consultarCursoApresentarProcessoSeletivoPorNome(String valorConsulta, Integer unidadeEnsinoCodigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = " SELECT DISTINCT Curso.* FROM   curso " + " INNER JOIN unidadeensinocurso on unidadeensinocurso.curso = curso.codigo " + " INNER JOIN unidadeensino on unidadeensinocurso.unidadeensino = unidadeensino.codigo " + " WHERE unidadeensino.apresentartelaprocessoseletivo = true and unidadeensinocurso.situacaocurso = 'AT' and lower (sem_acentos(curso.nome)) ilike(sem_acentos('" + valorConsulta.toLowerCase() + "%')) ";
		if (unidadeEnsinoCodigo.intValue() != 0 && unidadeEnsinoCodigo != null) {
			sqlStr += " AND unidadeensino.codigo = " + unidadeEnsinoCodigo.intValue();
		}
		sqlStr += " ORDER BY curso.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	@Override
	public CursoVO consultarPorNomeDoCursoEUnidadeDeEnsino(String nomeDoCurso, Integer idUnidadeEnsino, int nivelMontarDados, boolean montarListaGradeCurricularNivelDadosBasicos, UsuarioVO usuario) throws Exception {

		StringBuilder sql = new StringBuilder(" SELECT distinct on (c.codigo) * FROM  curso c ").append(" INNER JOIN unidadeensinocurso uec on uec.curso = c.codigo ").append(" INNER JOIN unidadeensino ue on uec.unidadeensino = ue.codigo  ").append(" WHERE ue.codigo = ").append(idUnidadeEnsino).append(" and lower(c.nome) = lower('").append(nomeDoCurso.toLowerCase()).append("')");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());

		if (tabelaResultado.next()) {
			return (montarDados(tabelaResultado, nivelMontarDados, false, usuario));
		}
		throw new Exception(UteisJSF.internacionalizar("msg_CursoInteresseLeadNaoEncontrado"));

	}

	public List<CursoVO> consultaRapidaPorUnidadeEnsinoNivelEducacional(String nomeCurso, Integer unidadeEnsino, String nivelEducacional, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" LEFT JOIN unidadeensinocurso ON unidadeensinocurso.curso = curso.codigo ");
		sqlStr.append(" LEFT JOIN unidadeensino ON unidadeensinocurso.unidadeEnsino = unidadeensino.codigo ");
		sqlStr.append(" WHERE LOWER (sem_acentos(curso.nome)) ILIKE LOWER(sem_acentos(?))");
		if (unidadeEnsino != null && unidadeEnsino != 0) {
			sqlStr.append(" AND unidadeensino.codigo = ").append(unidadeEnsino).append(" ");
		}
		sqlStr.append(" AND curso.niveleducacional = '").append(nivelEducacional).append("' ");
		appendGroupBy(sqlStr);
		sqlStr.append(" ORDER BY curso.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), nomeCurso + PERCENT);
		return montarDadosConsultaRapida(tabelaResultado, usuario);
	}

	/**
	 * Responsável por realizar uma consulta de <code>Curso</code> retornando apenas aqueles que foram definidos para serem apresentados na Biblioteca, por meio do valor Boolean apresentarCursoBiblioteca no cadastro de Curso
	 *
	 * @return List Contendo vários objetos da classe <code>CursoVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<CursoVO> consultarCursoApresentarBiblioteca(String campoConsultaCurso, String valorConsulta, Integer unidadeEnsinoCodigo, boolean controlarAcesso, int nivelmontardadosDadosbasicos, UsuarioVO usuario) throws Exception {
		List<CursoVO> objs = new ArrayList<>(0);
		if (campoConsultaCurso.equals("codigo")) {
			objs = consultarPorNomeOuCodigoEUnidadeEnsinoCursoApresentarBiblioteca(null, valorConsulta, controlarAcesso, nivelmontardadosDadosbasicos, usuario);
		}
		if (campoConsultaCurso.equals("nome")) {
			objs = consultarPorNomeOuCodigoEUnidadeEnsinoCursoApresentarBiblioteca(valorConsulta, null, controlarAcesso, nivelmontardadosDadosbasicos, usuario);
		}
		return objs;
	}

	private List<CursoVO> consultarPorNomeOuCodigoEUnidadeEnsinoCursoApresentarBiblioteca(String nomeCurso, String codigoCurso, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT DISTINCT c.* FROM UnidadeEnsinoCurso AS uec INNER JOIN Curso c ON c.codigo = uec.curso ");
		if (Uteis.isAtributoPreenchido(codigoCurso)) {
			if (Uteis.getIsValorNumerico(codigoCurso)) {
				sqlStr.append(" WHERE c.codigo = ").append(codigoCurso);
			} else {
				throw new ConsistirException("O valor para Código deve ser numérico!");
			}
		} else if (nomeCurso != null) {
			sqlStr.append(" WHERE lower (sem_acentos(c.nome)) ilike (sem_acentos('").append(nomeCurso).append("%')) ");
		}
		sqlStr.append(" AND c.apresentarCursoBiblioteca = 'true' ");
		sqlStr.append(" ORDER BY c.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}
   
    @Override
    public List<CursoVO> consultarCursosPossuemGradeCurricularComTipoAtividadeComplementar() {
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct codigo, nome from curso ");
		sb.append(" where exists (select gradecurricular.codigo from gradecurricular where gradecurricular.curso = curso.codigo ");
		sb.append(" and exists (select codigo from gradecurriculartipoatividadecomplementar where gradecurricular.codigo = gradecurriculartipoatividadecomplementar.gradecurricular  ) )");
		sb.append(" order by nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<CursoVO> listaCursoVOs = new ArrayList<CursoVO>(0);
		while (tabelaResultado.next()) {
			CursoVO obj = new CursoVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setNome(tabelaResultado.getString("nome"));
			listaCursoVOs.add(obj);
		}
		return listaCursoVOs;
	}
	
	public List<CursoVO> consultarPorArtefatoEntregaAluno(Integer artefato, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT distinct (curso.*) FROM curso INNER JOIN nivelcontroleartefato on curso.codigo=nivelcontroleartefato.curso WHERE nivelcontroleartefato.artefato = " + artefato;

		sqlStr += " ORDER BY curso.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}
	
	/**
	 * Método responsavel por invocar uma consulta rápida(Básica) e padrão que buscará apenas campos necessários para visualização do cliente na tela. Está consulta é considerada Padrão pelo motivo de todos os métodos chamar a mesma consulta(getSQLPadraoConsultaBasica) e apenas adicionar as cláusulas de condições e ordenação.
	 *
	 * @author Ana Claudia
	 */
	public List<CursoVO> consultaRapidaPorNomeEPeriodicidade(String periodicidadeArtefato, String valorConsulta, boolean controlarAcesso, int nivelMontarDados, Boolean validarUnidadeLogada, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("LEFT JOIN unidadeensinocurso ON unidadeensinocurso.curso = curso.codigo ");
		sqlStr.append("WHERE curso.periodicidade = '").append(periodicidadeArtefato).append("'");
		sqlStr.append(" AND lower(sem_acentos(curso.nome)) ilike(sem_acentos('");
		sqlStr.append(valorConsulta.toLowerCase());
		sqlStr.append("%')) ");
		if (validarUnidadeLogada && Uteis.isAtributoPreenchido(usuario.getUnidadeEnsinoLogado())) {
			sqlStr.append("AND unidadeensinocurso.unidadeEnsino = ").append(usuario.getUnidadeEnsinoLogado().getCodigo()).append(" ");
		}
		appendGroupBy(sqlStr);
		sqlStr.append("ORDER BY curso.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado, usuario);
	}
	
	public List<CursoVO> consultaRapidaPorNomePorCodigoUnidadeEnsinoCondicaoRenegociacao(String nome, List<CondicaoRenegociacaoUnidadeEnsinoVO> listaCondicaoUnidadeEnsinoVOs, Integer turno, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		String and = "";
		sqlStr.append(" LEFT JOIN unidadeensinocurso ON unidadeensinocurso.curso = curso.codigo ");
		sqlStr.append(" LEFT JOIN unidadeensino ON unidadeensinocurso.unidadeEnsino = unidadeensino.codigo ");
		if (!listaCondicaoUnidadeEnsinoVOs.isEmpty()) {
        	sqlStr.append(" WHERE ").append("unidadeensino.codigo in (");
			for (CondicaoRenegociacaoUnidadeEnsinoVO condicaoUnidadeEnsinoVO : listaCondicaoUnidadeEnsinoVOs) {
				if (condicaoUnidadeEnsinoVO.getUnidadeEnsinoVO().getFiltrarUnidadeEnsino()) {
					sqlStr.append(condicaoUnidadeEnsinoVO.getUnidadeEnsinoVO().getCodigo()).append(", ");
				}
			}
			sqlStr.append("0) ");
			and = " AND ";
		}
		if (turno != null && turno != 0) {
			if (and.equals("")) {
				and = " WHERE ";
			}
			sqlStr.append(and).append(" unidadeensinocurso.turno = " + turno + " ");
		}
		if (nome != null && !nome.isEmpty()) {
			if (and.equals("")) {
				and = " WHERE ";
			}
			sqlStr.append(and).append(" curso.nome ILIKE '%").append(nome).append("%'");
		}
		appendGroupBy(sqlStr);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado, usuario);
	}

	public List<CursoVO> consultaRapidaPorCodigoPorCodigoUnidadeEnsinoCondicaoRenegociacao(Integer codigo, List<CondicaoRenegociacaoUnidadeEnsinoVO> listaCondicaoUnidadeEnsinoVOs, Integer turno, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		String and = "";
		sqlStr.append(" LEFT JOIN unidadeensinocurso ON unidadeensinocurso.curso = curso.codigo ");
		sqlStr.append(" LEFT JOIN unidadeensino ON unidadeensinocurso.unidadeEnsino = unidadeensino.codigo ");
		if (!listaCondicaoUnidadeEnsinoVOs.isEmpty()) {
        	sqlStr.append(" WHERE ").append("unidadeensino.codigo in (");
			for (CondicaoRenegociacaoUnidadeEnsinoVO condicaoUnidadeEnsinoVO : listaCondicaoUnidadeEnsinoVOs) {
				if (condicaoUnidadeEnsinoVO.getUnidadeEnsinoVO().getFiltrarUnidadeEnsino()) {
					sqlStr.append(condicaoUnidadeEnsinoVO.getUnidadeEnsinoVO().getCodigo()).append(", ");
				}
			}
			sqlStr.append("0) ");
			and = " AND ";
		}
		if (turno != null && turno != 0) {
			if (and.equals("")) {
				and = " WHERE ";
			}
			sqlStr.append(and).append(" unidadeensinocurso.turno = " + turno + " ");
		}
		if (codigo != null && codigo > 0) {
			if (and.equals("")) {
				and = " WHERE ";
			}
			sqlStr.append(and).append(" curso.codigo = ").append(codigo);
		}
		appendGroupBy(sqlStr);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado, usuario);
	}
	
	public List<CursoVO> consultarCursoPorNivelEducacionalEUnidadeEnsinoVOs(String nivelEducacional, List<UnidadeEnsinoVO> unidadeEnsinoVOs, UsuarioVO usuario) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT * FROM ( ");
		sb.append(getSQLPadraoConsultaBasica());
		sb.append(" INNER JOIN unidadeensinocurso ON unidadeensinocurso.curso = curso.codigo ");
		sb.append(" INNER JOIN matricula		  ON matricula.curso = curso.codigo AND matricula.unidadeensino = unidadeensinocurso.unidadeensino ");
		sb.append(" WHERE 1 = 1 ");
		if (!unidadeEnsinoVOs.isEmpty()) {
			sb.append(" AND unidadeensinocurso.unidadeensino IN (");
			for (UnidadeEnsinoVO ue : unidadeEnsinoVOs) {
				if (ue.getFiltrarUnidadeEnsino()) {
					sb.append(ue.getCodigo()).append(", ");
				}
			}
			sb.append("0) ");
		}
		if (Uteis.isAtributoPreenchido(nivelEducacional)) {
			sb.append(" AND curso.niveleducacional   in ('").append(nivelEducacional).append("') ");
		}
		appendGroupBy(sb);
        sb.append(" ) AS t ");		
		sb.append(" ORDER BY t.nome ;");
		return montarDadosConsultaRapida(getConexao().getJdbcTemplate().queryForRowSet(sb.toString()), usuario);

	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarPeriodicidadeCurso(final CursoVO obj, UsuarioVO usuario) throws Exception {
		String sqlStr = "UPDATE Curso set nrPeriodoLetivo=?, periodicidade=? WHERE codigo = ?" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sqlStr, new Object[] { obj.getNrPeriodoLetivo().intValue(), obj.getPeriodicidade(), obj.getCodigo().intValue() });
	}
	
	
	public List<CursoVO> consultaRapidaPorNomeCursoUnidadeEnsinoNivelEducacional(String nomeCurso, Integer unidadeEnsino, String nivelEducacional,boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" LEFT JOIN unidadeensinocurso ON unidadeensinocurso.curso = curso.codigo ");
		sqlStr.append(" LEFT JOIN unidadeensino ON unidadeensinocurso.unidadeEnsino = unidadeensino.codigo ");
		sqlStr.append(" WHERE LOWER (sem_acentos(curso.nome)) ILIKE (sem_acentos('").append(nomeCurso.toLowerCase()).append("%'))");
		if (unidadeEnsino != null && unidadeEnsino != 0) {
			sqlStr.append(" AND unidadeensino.codigo = ").append(unidadeEnsino).append(" ");
		}
		if (Uteis.isAtributoPreenchido(nivelEducacional)) {
			sqlStr.append(" AND curso.niveleducacional   = '").append(nivelEducacional).append("' ");
		}
		appendGroupBy(sqlStr);
		sqlStr.append(" ORDER BY curso.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado, usuario);
	}

	@Override
	public List<CursoVO> consultarCursoAtividadeExtraClasseProfessor(int codigoFuncionarioCargo) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select distinct curso.codigo, curso.nome from curso");
		sql.append(" inner join atividadeextraclasseprofessorcurso on atividadeextraclasseprofessorcurso.curso = curso.codigo");
		sql.append(" inner join atividadeextraclasseprofessor on atividadeextraclasseprofessor.codigo = atividadeextraclasseprofessorcurso.atividadeextraclasseprofessor");
		sql.append(" inner join funcionariocargo on funcionariocargo.codigo = atividadeextraclasseprofessor.funcionariocargo");
		if (Uteis.isAtributoPreenchido(codigoFuncionarioCargo)) {
			sql.append(" where funcionariocargo.codigo = ?");
		}

		SqlRowSet rs = null;
		if (Uteis.isAtributoPreenchido(codigoFuncionarioCargo)) {
			rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigoFuncionarioCargo);
		} else {
			rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		}
		List<CursoVO> cursoVOs = new ArrayList<>(0);
		CursoVO cursoVO;
		while(rs.next()) {
			cursoVO = new CursoVO();
			cursoVO.setCodigo(rs.getInt("codigo"));
			cursoVO.setNome(rs.getString("nome"));
			cursoVOs.add(cursoVO);
		}
		return cursoVOs;
	}
	
	@Override
	public void consultarCursoDataModelo(DataModelo dataModelo, UsuarioVO usuarioVO, PeriodicidadeEnum periodicidadeEnum) {
		dataModelo.setListaConsulta(consultarCurso(dataModelo, periodicidadeEnum));
		dataModelo.setTotalRegistrosEncontrados(consultarTotalCurso(dataModelo, periodicidadeEnum));
	}

	private Integer consultarTotalCurso(DataModelo dataModelo, PeriodicidadeEnum periodicidadeEnum) {
		StringBuilder sql = new StringBuilder();
		dataModelo.getListaFiltros().clear();
		sql.append("SELECT count(codigo) as qtde FROM curso WHERE 1=1");
		switch (dataModelo.getCampoConsulta().toLowerCase()) {
		case "codigo":
			sql.append(" AND codigo = ?");
			break;
		case "nome":
			sql.append(" AND sem_acentos(nome) ILIKE(sem_acentos(?))");
			break;
		default:
			break;
		}
		
		dataModelo.getListaFiltros().add(UteisTexto.PORCENTAGEM + dataModelo.getValorConsulta() + UteisTexto.PORCENTAGEM);
		if (Uteis.isAtributoPreenchido(periodicidadeEnum)) {
			sql.append(" and periodicidade = ?");
			dataModelo.getListaFiltros().add(periodicidadeEnum.getValor());
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), dataModelo.getListaFiltros().toArray());

		return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, "qtde", TipoCampoEnum.INTEIRO);
	}

	private List<CursoVO> consultarCurso(DataModelo dataModelo, PeriodicidadeEnum periodicidade) {
		StringBuilder sql = new StringBuilder();
		dataModelo.getListaFiltros().clear();
		sql.append("SELECT codigo, nome FROM curso WHERE 1=1");
		switch (dataModelo.getCampoConsulta().toLowerCase()) {
		case "codigo":
			sql.append(" AND codigo = ?");
			break;
		case "nome":
			sql.append(" AND sem_acentos(nome) ILIKE(sem_acentos(?))");
			break;
		default:
			break;
		}
		
		dataModelo.getListaFiltros().add(UteisTexto.PORCENTAGEM + dataModelo.getValorConsulta() + UteisTexto.PORCENTAGEM);
		if (Uteis.isAtributoPreenchido(periodicidade)) {
			sql.append(" and periodicidade = ?");
			dataModelo.getListaFiltros().add(periodicidade.getValor());
		}

		sql.append(" ORDER BY NOME ");
		UteisTexto.addLimitAndOffset(sql, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), dataModelo.getListaFiltros().toArray());
		List<CursoVO> cursoVOs = new ArrayList<>(0);
		CursoVO cursoVO;
		while(rs.next()) {
			cursoVO = new CursoVO();
			cursoVO.setCodigo(rs.getInt("codigo"));
			cursoVO.setNome(rs.getString("nome"));
			cursoVOs.add(cursoVO);
		}
		return cursoVOs;
	}

	@Override
	public List<CursoVO> consultarCursoPorCoordenador(int codigoPessoaCoordenador) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select curso.codigo, curso.nome, * from cursocoordenador");
		sql.append(" inner join curso on cursocoordenador.curso = curso.codigo");
		sql.append(" inner join funcionario on cursocoordenador.funcionario = funcionario.codigo");
		sql.append(" inner join pessoa on funcionario.pessoa = pessoa.codigo");
		sql.append(" where pessoa.codigo = ?");

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigoPessoaCoordenador);
		List<CursoVO> cursoVOs = new ArrayList<>(0);
		CursoVO cursoVO;
		while(rs.next()) {
			cursoVO = new CursoVO();
			cursoVO.setCodigo(rs.getInt("codigo"));
			cursoVO.setNome(rs.getString("nome"));
			cursoVOs.add(cursoVO);
		}
		return cursoVOs;
	}

	@Override
	public CursoVO consultarPorNome(String nomeDoCurso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder(" SELECT distinct c.*, eixocurso.nome as \"eixocurso_nome\" FROM  curso c left join eixocurso on eixocurso.codigo = c.eixocurso ").append(" where sem_acentos(c.nome) ilike sem_acentos(?)");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), nomeDoCurso);
		if (tabelaResultado.next()) {
			CursoVO obj = montarDados(tabelaResultado, nivelMontarDados, false, usuario);
			obj.getEixoCursoVO().setCodigo(tabelaResultado.getInt("eixocurso"));
			obj.getEixoCursoVO().setNome(tabelaResultado.getString("eixocurso_nome"));
			return obj;
		}
		return new CursoVO();

	}


	@Override
	public List<CursoVO> consultarPorUnidadeEnsino(String campoConsulta, String valorConsulta, List<UnidadeEnsinoVO> unidadeEnsinoVOs, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuarioVO);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT DISTINCT curso.* FROM UnidadeEnsinoCurso ");
		sqlStr.append("INNER JOIN Curso  ON curso.codigo = unidadeensinocurso.curso ");
		sqlStr.append("WHERE 1=1 ");
		if (campoConsulta.equals("codigo")) {
			if (valorConsulta.equals("")) {
				valorConsulta = "0";
			}
			sqlStr.append("AND curso.codigo = ").append(valorConsulta);
		} else if (campoConsulta.equals("nome")) {
			sqlStr.append("AND lower (sem_acentos(curso.nome)) like (sem_acentos('");
			sqlStr.append(valorConsulta.toLowerCase());
			sqlStr.append("%')) ");
		}
		if (!unidadeEnsinoVOs.isEmpty() && unidadeEnsinoVOs.stream().anyMatch(p-> p.getFiltrarUnidadeEnsino())) {
			sqlStr.append(" and unidadeensinocurso.unidadeensino in (");
			int x = 0;
			for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOs) {
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
		sqlStr.append(" ORDER BY curso.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuarioVO);
	}
	
	@Override
	public List<CursoVO> consultaRapidaPorNomeEUnidadeDeEnsino(String valorConsultaCurso, List<ControleLivroRegistroDiplomaUnidadeEnsinoVO> controleLivroRegistroDiplomaUnidadeEnsinoVOs, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT DISTINCT c.* FROM UnidadeEnsinoCurso AS uec " + "INNER JOIN Curso c ON c.codigo = uec.curso " + "WHERE lower (sem_acentos(c.nome)) ilike lower(sem_acentos(?)) ";
		if (Uteis.isAtributoPreenchido(controleLivroRegistroDiplomaUnidadeEnsinoVOs)) {
			StringBuilder sql = new StringBuilder();
			sql.append(" and uec.unidadeEnsino in (");
			for (ControleLivroRegistroDiplomaUnidadeEnsinoVO listaUnidade : controleLivroRegistroDiplomaUnidadeEnsinoVOs) {
				if (listaUnidade.equals(controleLivroRegistroDiplomaUnidadeEnsinoVOs.get(controleLivroRegistroDiplomaUnidadeEnsinoVOs.size() -1))) {
					sql.append(listaUnidade.getUnidadeEnsino().getCodigo() + ") ");
				} else {
					sql.append(listaUnidade.getUnidadeEnsino().getCodigo() + ", ");
				}
			}
			sqlStr += sql.toString();
		}
		sqlStr += " ORDER BY c.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsultaCurso + PERCENT);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}
	
	@Override
	public List consultarPorUnidadesEnsinos(List<ControleLivroRegistroDiplomaUnidadeEnsinoVO> controleLivroRegistroDiplomaUnidadeEnsinoVOs, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT distinct c.* FROM UnidadeEnsinoCurso AS uec " + "INNER JOIN Curso c ON c.codigo = uec.curso ";
		if (Uteis.isAtributoPreenchido(controleLivroRegistroDiplomaUnidadeEnsinoVOs)) {
			sqlStr += "WHERE uec.unidadeEnsino in (";
			for (ControleLivroRegistroDiplomaUnidadeEnsinoVO listaUnidade : controleLivroRegistroDiplomaUnidadeEnsinoVOs) {
				if (listaUnidade.equals(controleLivroRegistroDiplomaUnidadeEnsinoVOs.get(controleLivroRegistroDiplomaUnidadeEnsinoVOs.size() -1))) {
					sqlStr+= listaUnidade.getUnidadeEnsino().getCodigo() + ", ";
				} else {
					sqlStr+= listaUnidade.getUnidadeEnsino().getCodigo() + ") ";
				}
			}
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}
	
	
	
	public List<CursoVO> consultarCursoPorNomePeriodicidadeNivelEducacionalEUnidadeEnsinoVOs(String nome, String periodicidade, String nivelEducacional ,List<UnidadeEnsinoVO> unidadeEnsinoVOs, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder(" SELECT curso.codigo, curso.nome ");
		sb.append(" FROM curso ");		
		sb.append(" WHERE sem_acentos(curso.nome) ILIKE sem_acentos(?) ");
		if (Uteis.isAtributoPreenchido(periodicidade)) {
			sb.append(" AND curso.periodicidade = '").append(periodicidade).append("' ");
		}
		if(Uteis.isAtributoPreenchido(nivelEducacional)) {
			sb.append(" AND curso.niveleducacional = '").append(nivelEducacional).append("' ");
		}
		sb.append(" AND EXISTS ( SELECT FROM matricula ");
		sb.append(" INNER JOIN unidadeensinocurso ON matricula.curso = unidadeensinocurso.curso ");
		sb.append(" AND matricula.unidadeensino = unidadeensinocurso.unidadeensino ");
		sb.append(" AND matricula.turno = unidadeensinocurso.turno ");
		sb.append(" WHERE matricula.curso = curso.codigo ");
		if (Uteis.isAtributoPreenchido(unidadeEnsinoVOs) && unidadeEnsinoVOs.stream().anyMatch(UnidadeEnsinoVO::getFiltrarUnidadeEnsino)) {
			sb.append(unidadeEnsinoVOs.stream()
					.filter(UnidadeEnsinoVO::getFiltrarUnidadeEnsino)
					.map(ue -> ue.getCodigo().toString())
					.collect(Collectors.joining(", ", " AND matricula.unidadeensino IN (", ")")));
		}
		sb.append(" ) ORDER BY curso.nome ;");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), "%"+nome+"%");
		List<CursoVO> vetResultado = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			CursoVO obj = new CursoVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setNome(tabelaResultado.getString("nome"));
			vetResultado.add(obj);
		}
		return vetResultado;
	}
	
	public List<CursoVO> consultarCursoPorCodigoPeriodicidadeNivelEducacionalEUnidadeEnsinoVO(Integer codigo, String periodicidade, String nivelEducacional ,Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * FROM ( ");
		sb.append("select distinct curso.codigo, curso.nome, curso.codigocontabil,  curso.nomecontabil, curso.nivelcontabil, curso.nomeDocumentacao, curso.idCursoInep,  curso.periodicidade, curso.nivelEducacional, curso.nrRegistroInterno, areaConhecimento.codigo as \"areaConhecimento.codigo\", areaConhecimento.nome as \"areaConhecimento.nome\", ");
		sb.append("curso.configuracaoAcademico as \"curso.configuracaoAcademico\", curso.limitarDiasDownload as \"curso.limitarDiasDownload\", curso.qtdeDiasLimiteDownload as \"curso.qtdeDiasLimiteDownload\", curso.utilizarRecursoAvaTerceiros, curso.questionario as \"curso.questionario\" , curso.nrperiodoletivo as \"curso.nrperiodoletivo\", curso.configuracaoldap as \"curso.configuracaoldap\" , ");
		sb.append(" curso.textoDeclaracaoPPI as \"curso.textoDeclaracaoPPI\" , curso.textoDeclaracaoEscolaridadePublica as \"curso.textoDeclaracaoEscolaridadePublica\" , curso.textoConfirmacaoNovaMatricula as \"curso.textoConfirmacaoNovaMatricula\" , curso.urlDeclaracaoNormasMatricula as \"curso.urlDeclaracaoNormasMatricula\" , curso.textoDeclaracaoBolsasAuxilios as \"curso.textoDeclaracaoBolsasAuxilios\" , ") ;
		sb.append(" curso.permitirAssinarContratoPendenciaDocumentacao as \"curso.permitirAssinarContratoPendenciaDocumentacao\"  ,	curso.ativarPreMatriculaAposEntregaDocumentosObrigatorios as \"curso.ativarPreMatriculaAposEntregaDocumentosObrigatorios\"  , curso.ativarMatriculaAposAssinaturaContrato  as   \"curso.ativarMatriculaAposAssinaturaContrato\" , curso.textoPadraoContratoMatriculaCalouro  as   \"curso.textoPadraoContratoMatriculaCalouro\" , curso.permitirOutraMatriculaMesmoAluno as \"curso.permitirOutraMatriculaMesmoAluno\"  ");
		sb.append(" from matricula ");		
		sb.append(" INNER JOIN unidadeensinocurso ON unidadeensinocurso.curso = matricula.curso AND matricula.unidadeensino = unidadeensinocurso.unidadeensino ");
		sb.append(" INNER JOIN curso ON matricula.curso = curso.codigo ");
		sb.append(" and unidadeensinocurso.turno = matricula.turno ");
		sb.append(" left join areaConhecimento on areaConhecimento.codigo = curso.areaConhecimento ");
		sb.append(" WHERE curso.codigo = '").append(codigo).append("' ");
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sb.append(" AND matricula.unidadeensino = '").append(unidadeEnsino).append("' ");
		}
		if (Uteis.isAtributoPreenchido(periodicidade)) {
			sb.append(" AND curso.periodicidade = '").append(periodicidade).append("' ");
		}
		if(Uteis.isAtributoPreenchido(nivelEducacional)) {
			sb.append(" AND curso.niveleducacional = '").append(nivelEducacional).append("' ");
		}
		sb.append(" ) AS t ");
		sb.append(" ORDER BY t.codigo ;");
		return montarDadosConsultaRapida(getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), codigo), usuario);

	}
	
	public List<CursoVO> consultarCursoPorNomePeriodicidadeNivelEducacionalEUnidadeEnsinoVO(String nome, String periodicidade, String nivelEducacional ,Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * FROM ( ");
		sb.append("select distinct curso.codigo, curso.nome, curso.codigocontabil,  curso.nomecontabil, curso.nivelcontabil, curso.nomeDocumentacao, curso.idCursoInep,  curso.periodicidade, curso.nivelEducacional, curso.nrRegistroInterno, areaConhecimento.codigo as \"areaConhecimento.codigo\", areaConhecimento.nome as \"areaConhecimento.nome\", ");
		sb.append("curso.configuracaoAcademico as \"curso.configuracaoAcademico\", curso.limitarDiasDownload as \"curso.limitarDiasDownload\", curso.qtdeDiasLimiteDownload as \"curso.qtdeDiasLimiteDownload\", curso.utilizarRecursoAvaTerceiros, curso.questionario as \"curso.questionario\" , curso.nrperiodoletivo as \"curso.nrperiodoletivo\", curso.configuracaoldap as \"curso.configuracaoldap\" , ");
		sb.append(" curso.textoDeclaracaoPPI as \"curso.textoDeclaracaoPPI\" , curso.textoDeclaracaoEscolaridadePublica as \"curso.textoDeclaracaoEscolaridadePublica\" , curso.textoConfirmacaoNovaMatricula as \"curso.textoConfirmacaoNovaMatricula\" , curso.urlDeclaracaoNormasMatricula as \"curso.urlDeclaracaoNormasMatricula\" , curso.textoDeclaracaoBolsasAuxilios as \"curso.textoDeclaracaoBolsasAuxilios\" , ") ;
		sb.append(" curso.permitirAssinarContratoPendenciaDocumentacao as \"curso.permitirAssinarContratoPendenciaDocumentacao\"  ,	curso.ativarPreMatriculaAposEntregaDocumentosObrigatorios as \"curso.ativarPreMatriculaAposEntregaDocumentosObrigatorios\"  , curso.ativarMatriculaAposAssinaturaContrato  as   \"curso.ativarMatriculaAposAssinaturaContrato\" , curso.textoPadraoContratoMatriculaCalouro  as   \"curso.textoPadraoContratoMatriculaCalouro\" , curso.permitirOutraMatriculaMesmoAluno as \"curso.permitirOutraMatriculaMesmoAluno\"  ");
		sb.append(" from matricula ");		
		sb.append(" INNER JOIN unidadeensinocurso ON unidadeensinocurso.curso = matricula.curso AND matricula.unidadeensino = unidadeensinocurso.unidadeensino ");
		sb.append(" INNER JOIN curso ON matricula.curso = curso.codigo ");
		sb.append(" and unidadeensinocurso.turno = matricula.turno ");
		sb.append("left join areaConhecimento on areaConhecimento.codigo = curso.areaConhecimento ");
		sb.append(" WHERE (sem_acentos(curso.nome)) ILIKE (sem_acentos(?)) ");
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sb.append(" AND matricula.unidadeensino = '").append(unidadeEnsino).append("' ");
		}
		if (Uteis.isAtributoPreenchido(periodicidade)) {
			sb.append(" AND curso.periodicidade = '").append(periodicidade).append("' ");
		}
		if(Uteis.isAtributoPreenchido(nivelEducacional)) {
			sb.append(" AND curso.niveleducacional = '").append(nivelEducacional).append("' ");
		}
		sb.append(" ) AS t ");
		sb.append(" ORDER BY t.nome ;");
		return montarDadosConsultaRapida(getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), "%"+nome+"%"), usuario);

	}
	
	public static void montarDadosTextoPadraoContratoMatriculaCalouro(CursoVO obj, int nivelMontarDados ,UsuarioVO usuario) throws Exception {
		if (obj.getTextoPadraoContratoMatriculaCalouro().getCodigo().intValue() == 0) {
			return;
		}
		obj.setTextoPadraoContratoMatriculaCalouro(getFacadeFactory().getTextoPadraoFacade().consultarPorChavePrimaria(obj.getTextoPadraoContratoMatriculaCalouro().getCodigo(),nivelMontarDados, usuario));
	}
	
	@Override
	public List<CursoVO> consultarCursoPorUnidadeEnsinoNivelEducacional(List<UnidadeEnsinoVO> unidadeEnsinoVOs, List<TipoNivelEducacional> tipoNivelEducacional, UsuarioVO usuario) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(getSQLPadraoConsultaBasica());
		sql.append(" WHERE 1 = 1 ");
		if (Uteis.isAtributoPreenchido(unidadeEnsinoVOs)) {
			sql.append(" AND EXISTS (SELECT FROM unidadeensinocurso u WHERE u.curso = curso.codigo AND u.unidadeensino IN (" + unidadeEnsinoVOs.stream().map(u -> u.getCodigo().toString()).collect(Collectors.joining(", ")) + ") ) ");
		}
		if (Uteis.isAtributoPreenchido(tipoNivelEducacional)) {
			sql.append(tipoNivelEducacional.stream().map(TipoNivelEducacional::getValor).collect(Collectors.joining("', '", " AND curso.niveleducacional IN ('", "') ")));
		}
		appendGroupBy(sql);
		sql.append(" ORDER BY curso.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaRapida(tabelaResultado, usuario);
	}


	@Override
	public void consultaControleConsultaOtimizadoCurso(DataModelo dataModelo, Integer unidadeEnsino, TipoNivelEducacional tipoNivelEducacional) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		dataModelo.getListaFiltros().clear();
		dataModelo.getListaConsulta().clear();
		if (dataModelo.getCampoConsulta().equals("nomeCurso")) {
			sqlStr.append(" WHERE sem_acentos(curso.nome) ilike(sem_acentos(?)) ");
			dataModelo.getListaFiltros().add(PERCENT + dataModelo.getValorConsulta() + PERCENT);
		} else {
			sqlStr.append(" WHERE sem_acentos(areaConhecimento.nome) ilike(sem_acentos(?)) ");
			dataModelo.getListaFiltros().add(PERCENT + dataModelo.getValorConsulta() + PERCENT);
		}
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sqlStr.append("AND EXISTS(SELECT FROM unidadeensinocurso where unidadeensinocurso.curso = curso.codigo AND unidadeensinocurso.unidadeEnsino = ?) ");
			dataModelo.getListaFiltros().add(unidadeEnsino);
		}
		if (Uteis.isAtributoPreenchido(tipoNivelEducacional)) {
			sqlStr.append("AND curso.nivelEducacional = ? ");
			dataModelo.getListaFiltros().add(tipoNivelEducacional.getValor());
		}
		appendGroupBy(sqlStr);
		sqlStr.append("ORDER BY curso.nome");
		if (dataModelo != null && Uteis.isAtributoPreenchido(dataModelo.getLimitePorPagina())) {
			sqlStr.append(" limit ").append(dataModelo.getLimitePorPagina()).append(" offset ").append(dataModelo.getOffset());
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), dataModelo.getListaFiltros().toArray());
		if (dataModelo != null && tabelaResultado.next()) {
			dataModelo.setTotalRegistrosEncontrados(0);
			dataModelo.setTotalRegistrosEncontrados(tabelaResultado.getInt("qtde_total_registros"));
		}
		tabelaResultado.beforeFirst();
		dataModelo.setListaConsulta(montarDadosConsultaRapida(tabelaResultado, dataModelo.getUsuario()));
	}

	@Override
	public List<CursoVO> consultarCursoPorDisciplina(Integer disciplina, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("select curso.* from disciplina ");
		sqlStr.append("left join disciplinaEquivalente de on de.equivalente = disciplina.codigo ");
		sqlStr.append("inner join gradeDisciplina on (gradeDisciplina.disciplina = disciplina.codigo or (case when (de.equivalente is not null) then gradedisciplina.disciplina = de.disciplina end))");
		sqlStr.append("inner join periodoletivo on gradeDisciplina.periodoLetivo = periodoLetivo.codigo ");
		sqlStr.append("inner join gradecurricular on periodoLetivo.gradecurricular = gradeCurricular.codigo ");
		sqlStr.append("inner join curso on gradeCurricular.curso = curso.codigo ");
		sqlStr.append("where disciplina.codigo = ? ");

		sqlStr.append(" union select curso.* from disciplina ");
		sqlStr.append("inner join gradeDisciplinacomposta on gradeDisciplinacomposta.disciplina = disciplina.codigo ");
		sqlStr.append("inner join gradeDisciplina on gradeDisciplina.codigo = gradeDisciplinacomposta.gradeDisciplina ");
		sqlStr.append("inner join periodoletivo on gradeDisciplina.periodoLetivo = periodoLetivo.codigo ");
		sqlStr.append("inner join gradecurricular on periodoLetivo.gradecurricular = gradeCurricular.codigo ");
		sqlStr.append("inner join curso on gradeCurricular.curso = curso.codigo ");
		sqlStr.append("where disciplina.codigo = ? ");

		sqlStr.append(" union select  curso.* from disciplina ");
		sqlStr.append("inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.disciplina = disciplina.codigo ");
		sqlStr.append("inner join gradecurriculargrupooptativa on gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo ");
		sqlStr.append("inner join gradecurricular on gradecurriculargrupooptativa.gradecurricular = gradeCurricular.codigo ");
		sqlStr.append("inner join curso on gradeCurricular.curso = curso.codigo ");
		sqlStr.append("where disciplina.codigo = ? ");

		sqlStr.append(" union select curso.* from disciplina ");
		sqlStr.append("inner join gradeDisciplinacomposta on gradeDisciplinacomposta.disciplina = disciplina.codigo ");
		sqlStr.append("inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.codigo = gradeDisciplinacomposta.gradecurriculargrupooptativadisciplina ");
		sqlStr.append("inner join gradecurriculargrupooptativa on gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo ");
		sqlStr.append("inner join gradecurricular on gradecurriculargrupooptativa.gradecurricular = gradeCurricular.codigo ");
		sqlStr.append("inner join curso on gradeCurricular.curso = curso.codigo ");
		sqlStr.append("where disciplina.codigo = ? ");
		sqlStr.append(" order by nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), disciplina,disciplina,disciplina,disciplina);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public void appendGroupBy(StringBuffer sqlStr){
		sqlStr.append(" GROUP BY 		curso.codigo,		curso.nome,		curso.codigocontabil,		curso.nomecontabil,		curso.nivelcontabil,		curso.nomeDocumentacao, ");
		sqlStr.append(" curso.idCursoInep,		curso.periodicidade,		curso.nivelEducacional,		curso.nrRegistroInterno,		areaConhecimento.codigo ,		areaConhecimento.nome , ");
		sqlStr.append(" curso.configuracaoAcademico ,		curso.limitarDiasDownload ,		curso.qtdeDiasLimiteDownload ,		curso.utilizarRecursoAvaTerceiros,		curso.questionario , ");
		sqlStr.append(" curso.nrperiodoletivo ,		curso.configuracaoldap ,		curso.textoDeclaracaoPPI,		curso.textoDeclaracaoEscolaridadePublica ,		curso.textoConfirmacaoNovaMatricula , ");
		sqlStr.append(" curso.urlDeclaracaoNormasMatricula ,		curso.textoDeclaracaoBolsasAuxilios ,		curso.permitirAssinarContratoPendenciaDocumentacao ,		curso.ativarPreMatriculaAposEntregaDocumentosObrigatorios , ");
		sqlStr.append(" curso.ativarMatriculaAposAssinaturaContrato ,		curso.textoPadraoContratoMatriculaCalouro ,		curso.permitirOutraMatriculaMesmoAluno  ");
	}

}
