package negocio.facade.jdbc.protocolo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.CidTipoRequerimentoVO;
import negocio.comuns.academico.enumeradores.TipoAlunoEnum;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.protocolo.TipoRequerimentoCursoVO;
import negocio.comuns.protocolo.TipoRequerimentoDepartamentoVO;
import negocio.comuns.protocolo.TipoRequerimentoVO;
import negocio.comuns.protocolo.enumeradores.TipoControleCobrancaViaRequerimentoEnum;
import negocio.comuns.secretaria.enumeradores.TipoUploadArquivoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TiposRequerimento;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.protocolo.TipoRequerimentoInterfaceFacade;


/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>TipoRequerimentoVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>TipoRequerimentoVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see TipoRequerimentoVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class TipoRequerimento extends ControleAcesso implements TipoRequerimentoInterfaceFacade {

    /**
	 * 
	 */
	private static final long serialVersionUID = 323924227159660151L;
	protected static String idEntidade = "TipoRequerimento";

    public  TipoRequerimento() {
        super();
    }    

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>TipoRequerimentoVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>TipoRequerimentoVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final TipoRequerimentoVO obj, UsuarioVO usuarioVO) throws Exception {
        try {
            TipoRequerimentoVO.validarDados(obj);
            TipoRequerimento.incluir(getIdEntidade(), true, usuarioVO);
            final StringBuilder sql = new StringBuilder("INSERT INTO TipoRequerimento( nome, prazoExecucao, departamentoResponsavel, orientacao, haDocumentoParaRetirada, tipo,requerimentoVisaoAluno, requerimentoVisaoProfessor, requerimentoVisaoCoordenador, permitirUploadArquivo,"); //10
                    sql.append(" permitirInformarEnderecoEntrega, qtdDiasVencimentoRequerimento, requerimentoVisaoPai, diasParaExclusaoRequerimentoDefazados, situacao, requerimentoSituacaoFinanceiroVisaoAluno, unidadeEnsinoEspecifico, mensagemAlerta, tramitaEntreDepartamentos, textoPadrao, "); //20
                    sql.append(" questionario, centroReceitaRequerimentoPadrao, requerimentominhasnotas, sigla, situacaoMatriculaAtiva, situacaoMatriculaPreMatriculada, situacaoMatriculaCancelada, situacaoMatriculaTrancada, situacaoMatriculaAbandonada, situacaoMatriculaTransferida,"); // 30
                    sql.append(" situacaoMatriculaFormada, verificarPendenciaBiblioteca, verificarPendenciaFinanceira, verificarPendenciaBibliotecaAtraso, verificarPendenciaFinanceiraAtraso, verificarPendenciaDocumentacao, verificarPendenciaEnade , verificarPendenciaEstagio, verificarPendenciaAtividadeComplementar, taxa ,"); //40
                    sql.append(" cobrarApartirVia,tipoControleCobrancaViaRequerimento, requerimentoVisaoAlunoApresImprimirDeclaracao , deferirAutomaticamente, qtdeDiasDisponivel, qtdeDiasAposPrimeiraImpressao, requerimentoVisaoFuncionario, considerarSegundaViaIndependenteSituacaoPrimeiraVia, requerimentoMembroComunidade, bloquearRecebimentoCartaoCreditoOnline, ");//50
                    sql.append(" assinarDigitalmenteDeclaracoesGeradasNoRequerimento, considerardiasuteis, verificarPendenciaApenasMatriculaRequerimento, tipoUploadArquivo, extensaoArquivo, orientacaoUploadArquivo, requerAutorizacaoPagamento, permiteDeferirAguardandoAutorizacaoPagamento, realizarIsencaoTaxaReposicaoMatriculaAposDataAula, quantidadeDiasAntesAulaPermitirExclusaoRequerimentoReposicao,"); //60
                    sql.append(" permitirSolicitacaoIsencaoTaxaRequerimento, solicitarAnexoComprovanteIsencaoTaxaRequerimento, orientacaoDocumentoComprovanteIsencaoTaxaRequerimento, permiteIncluirDisciplinaPorEquivalencia, permiteIncluirReposicaoTurmaOutraUnidade, permiteIncluirReposicaoTurmaOutroCurso, permitirReposicaoComChoqueHorario, usarCentroResultadoTurma, uploadArquivoObrigatorio, orientacaoAtendente, "); //70
                    sql.append(" bloquearQuantidadeRequerimentoAbertosSimultaneamente, quantidadeLimiteRequerimentoAbertoSimultaneamente, considerarBloqueioSimultaneoRequerimentoDeferido, considerarBloqueioSimultaneoRequerimentoIndeferido, permitirImpressaoHistoricoVisaoAluno, nivelEducacional, layoutHistoricoApresentar, aprovadoSituacaoHistorico, reprovadoSituacaoHistorico, trancadoSituacaoHistorico, ");//80
                    sql.append(" cursandoSituacaoHistorico, abandonoCursoSituacaoHistorico, transferidoSituacaoHistorico, canceladoSituacaoHistorico, assinarDigitalmenteHistorico, certificadoImpresso, apenasParaAlunosComTodasAulasRegistradas, verificarAlunoPossuiPeloMenosUmaDisciplinaAprovada, registrarFormaturaAoRealizarImpressaoCerticadoDigital, cobrarTaxaSomenteCertificadoImpresso,"); //90
                    sql.append(" mensagemEmissaoCertificadoImpresso, permitirAlterarDataPrevisaoConclusaoRequerimento , abrirOutroRequerimentoAoDeferirEsteTipoRequerimento, qtdDiasCobrarTaxa, tipoRequerimentoAbrirDeferimento, validarMatriculaIntegralizada, ocultarunidadeensinolistaturmareposicao,validarDebitoFinanceiroRequerimentoIsento, permitirReporDisciplinaComAulaProgramada, validarEntregaTccAluno, "); //100
                    sql.append(" deferirAutomaticamenteDocumentoImpresso, msgBloqueioNovaSolicitacaoAproveitamento, qtdeMaximaIndeferidoAproveitamento , percentualMinimoCargaHorariaAproveitamento, qtdeMinimaDeAnosAproveitamento, deferirAutomaticamenteTrancamento, bloqueioSimultaneoPelo , permitirAlunoAlterarUnidadeEnsino, permitirAlunoRejeitarDocumento, percentualIntegralizacaoCurricularInicial, "); //110
                    sql.append(" percentualIntegralizacaoCurricularFinal, registrarAproveitamentoDisciplinaTCC, registrarTrancamentoProximoSemestre, validarAnoSemestreIngresso, anoingresso, semestreingresso, considerarTodasMatriculasAlunoValidacaoAberturaSimultanea, permitirAlunoAlterarCurso, validarVagasPorNumeroComputadoresUnidadeEnsino, registrarTransferenciaProximoSemestre, "); //120
                    sql.append(" cidDeferirAutomaticamente, validarVagasPorNumeroComputadoresConsiderandoCurso, ano, semestre,  permitirAproveitarDisciplinasCursando, bimestre, tipoNota, enviarNotificacaoRequerente, campoafastamento, situacaoMatriculaJubilado, "); //130
                    sql.append(" utilizarmensagemdeferimentoexclusivo, utilizarmensagemindeferimentoexclusivo, tipoAluno, textopadraocontratoestagio) "); //134

                    sql.append(" VALUES ("
                    		+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," //10
                    		+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," //20
                    		+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," //30
                    		+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," //40
                    		+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," //50
                    		+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," //60
                    		+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," //70
                    		+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," //80
                    		+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," //90
                    		+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," //100
                    		+ " ?, ?, ?, ?, ?, ?, ? ,?, ?, ?," //110
                    		+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," //120
                    		+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," //130
                    		+ " ?, ?, ?, ?)"); //134

                    sql.append(" returning codigo"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement sqlInserir = con.prepareStatement(sql.toString());
                    int x = 1;
                    sqlInserir.setString(x++, obj.getNome());                    
                    sqlInserir.setInt(x++, obj.getPrazoExecucao().intValue());
                    if (!obj.getDepartamentoResponsavel().getCodigo().equals(0)) {
                        sqlInserir.setInt(x++, obj.getDepartamentoResponsavel().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(x++, 0);
                    }
                    sqlInserir.setString(x++, obj.getOrientacao());
                    sqlInserir.setBoolean(x++, obj.getHaDocumentoParaRetirada().booleanValue());                    
                    sqlInserir.setString(x++, obj.getTipo());
                    sqlInserir.setBoolean(x++, obj.getRequerimentoVisaoAluno());
                    sqlInserir.setBoolean(x++, obj.getRequerimentoVisaoProfessor());
                    sqlInserir.setBoolean(x++, obj.getRequerimentoVisaoCoordenador());
                    sqlInserir.setBoolean(x++, obj.getPermitirUploadArquivo());
                    sqlInserir.setBoolean(x++, obj.getPermitirInformarEnderecoEntrega());
                    sqlInserir.setInt(x++, obj.getQtdDiasVencimentoRequerimento().intValue());
                    sqlInserir.setBoolean(x++, obj.getRequerimentoVisaoPai());
                    sqlInserir.setInt(x++, obj.getDiasParaExclusaoRequerimentoDefazados());
                    sqlInserir.setString(x++, obj.getSituacao());
                    sqlInserir.setBoolean(x++, obj.getRequerimentoSituacaoFinanceiroVisaoAluno());
                    sqlInserir.setBoolean(x++, obj.getUnidadeEnsinoEspecifica());
                    sqlInserir.setString(x++, obj.getMensagemAlerta().trim());
                    sqlInserir.setBoolean(x++, obj.getTramitaEntreDepartamentos());
                    if ((!obj.getRequerimentoMembroComunidade()) && !obj.getTextoPadrao().getCodigo().equals(0)) {
                        sqlInserir.setInt(x++, obj.getTextoPadrao().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(x++, 0);
                    }
                    if (!obj.getQuestionario().getCodigo().equals(0)) {
                    	sqlInserir.setInt(x++, obj.getQuestionario().getCodigo().intValue());
                    } else {
                    	sqlInserir.setNull(x++, 0);
                    }
                    if (!obj.getCentroReceitaRequerimentoPadrao().getCodigo().equals(0)) {
                    	sqlInserir.setInt(x++, obj.getCentroReceitaRequerimentoPadrao().getCodigo().intValue());
                    } else {
                    	sqlInserir.setNull(x++, 0);
                    }
                    sqlInserir.setBoolean(x++, obj.getRequerimentoMinhasNotas());
                    sqlInserir.setString(x++, obj.getSigla());
                    sqlInserir.setBoolean(x++, obj.getSituacaoMatriculaAtiva());
                    sqlInserir.setBoolean(x++, obj.getSituacaoMatriculaPreMatriculada());
                    sqlInserir.setBoolean(x++, obj.getSituacaoMatriculaCancelada());
                    sqlInserir.setBoolean(x++, obj.getSituacaoMatriculaTrancada());
                    sqlInserir.setBoolean(x++, obj.getSituacaoMatriculaAbandonada());
                    sqlInserir.setBoolean(x++, obj.getSituacaoMatriculaTransferida());
                    sqlInserir.setBoolean(x++, obj.getSituacaoMatriculaFormada());
                    sqlInserir.setBoolean(x++, obj.getVerificarPendenciaBiblioteca());
                    sqlInserir.setBoolean(x++, obj.getVerificarPendenciaFinanceira());
                    sqlInserir.setBoolean(x++, obj.getVerificarPendenciaBibliotecaAtraso());
                    sqlInserir.setBoolean(x++, obj.getVerificarPendenciaFinanceiraAtraso());
                    sqlInserir.setBoolean(x++, obj.getVerificarPendenciaDocumentacao());
                    sqlInserir.setBoolean(x++, obj.getVerificarPendenciaEnade());
                    sqlInserir.setBoolean(x++, obj.getVerificarPendenciaEstagio());
                    sqlInserir.setBoolean(x++, obj.getVerificarPendenciaAtividadeComplementar());
                    if (!obj.getTaxa().getCodigo().equals(0)) {
                    	sqlInserir.setInt(x++, obj.getTaxa().getCodigo().intValue());
                    } else {
                    	sqlInserir.setNull(x++, 0);
                    }
                    sqlInserir.setInt(x++, obj.getCobrarApartirVia());                    
                    if (obj.getTipoControleCobrancaViaRequerimento() != null) {
                    	sqlInserir.setString(x++, obj.getTipoControleCobrancaViaRequerimento().name());
                    } else {
                    	sqlInserir.setNull(x++, 0);
                    }
                    sqlInserir.setBoolean(x++, obj.getRequerimentoVisaoAlunoApresImprimirDeclaracao());
                    sqlInserir.setBoolean(x++, obj.getDeferirAutomaticamente());
                    if(obj.getQtdeDiasDisponivel() != null){
                    	sqlInserir.setInt(x++,obj.getQtdeDiasDisponivel());
                    }else {
						sqlInserir.setNull(x++, 0);
					}
                    if(obj.getQtdeDiasAposPrimeiraImpressao() != null){
                    	sqlInserir.setInt(x++, obj.getQtdeDiasAposPrimeiraImpressao());
                    }else {
						sqlInserir.setNull(x++, 0);
					}
                    sqlInserir.setBoolean(x++, obj.getRequerimentoVisaoFuncionario());
                    sqlInserir.setBoolean(x++, obj.getConsiderarSegundaViaIndependenteSituacaoPrimeiraVia());
                    sqlInserir.setBoolean(x++, obj.getRequerimentoMembroComunidade());
                    sqlInserir.setBoolean(x++, obj.getBloquearRecebimentoCartaoCreditoOnline());
                    sqlInserir.setBoolean(x++, obj.getAssinarDigitalmenteDeclaracoesGeradasNoRequerimento());
                    sqlInserir.setBoolean(x++, obj.getConsiderarDiasUteis());
                    sqlInserir.setBoolean(x++, obj.getVerificarPendenciaApenasMatriculaRequerimento());                    
                    sqlInserir.setString(x++, obj.getTipoUploadArquivo().name());                    
                    sqlInserir.setString(x++, obj.getExtensaoArquivo());                    
                    sqlInserir.setString(x++, obj.getOrientacaoUploadArquivo());                    
                    sqlInserir.setBoolean(x++, obj.getRequerAutorizacaoPagamento());                    
                    sqlInserir.setBoolean(x++, obj.getPermiteDeferirAguardandoAutorizacaoPagamento());   
                    sqlInserir.setBoolean(x++, obj.getRealizarIsencaoTaxaReposicaoMatriculaAposDataAula());
                    sqlInserir.setInt(x++, obj.getQuantidadeDiasAntesAulaPermitirExclusaoRequerimentoReposicao());
                    sqlInserir.setBoolean(x++, obj.getPermitirSolicitacaoIsencaoTaxaRequerimento());
                    sqlInserir.setBoolean(x++, obj.getSolicitarAnexoComprovanteIsencaoTaxaRequerimento());
                    sqlInserir.setString(x++, obj.getOrientacaoDocumentoComprovanteIsencaoTaxaRequerimento());
                    sqlInserir.setBoolean(x++, obj.getPermiteIncluirDisciplinaPorEquivalencia());
                    sqlInserir.setBoolean(x++, obj.getPermiteIncluirReposicaoTurmaOutraUnidade());
                    sqlInserir.setBoolean(x++, obj.getPermiteIncluirReposicaoTurmaOutroCurso());
                    sqlInserir.setBoolean(x++, obj.getPermitirReposicaoComChoqueHorario());
                    sqlInserir.setBoolean(x++, obj.getUsarCentroResultadoTurma());
                    sqlInserir.setBoolean(x++, obj.getUploadArquivoObrigatorio());
					if (Uteis.isAtributoPreenchido(obj.getOrientacaoAtendente())) {
						sqlInserir.setString(x++, obj.getOrientacaoAtendente());
					} else {
						sqlInserir.setNull(x++, 0);
					}
                    
                    sqlInserir.setBoolean(x++, obj.getBloquearQuantidadeRequerimentoAbertosSimultaneamente());
                    sqlInserir.setInt(x++, obj.getQuantidadeLimiteRequerimentoAbertoSimultaneamente());
                    sqlInserir.setBoolean(x++, obj.getConsiderarBloqueioSimultaneoRequerimentoDeferido());
                    sqlInserir.setBoolean(x++, obj.getConsiderarBloqueioSimultaneoRequerimentoIndeferido());
                    if (obj.getTipo().equals(TiposRequerimento.HISTORICO.getValor())) {
                    	sqlInserir.setBoolean(x++, obj.getPermitirImpressaoHistoricoVisaoAluno());
                    } else {
                    	sqlInserir.setBoolean(x++, false);
                    }
                    sqlInserir.setString(x++, obj.getNivelEducacional());
                    if (obj.getTipo().equals(TiposRequerimento.HISTORICO.getValor())) {
                    	sqlInserir.setString(x++, obj.getLayoutHistoricoApresentar());
                    } else {
                    	sqlInserir.setString(x++, "");
                    }
                    sqlInserir.setBoolean(x++, obj.getAprovadoSituacaoHistorico());
                    sqlInserir.setBoolean(x++, obj.getReprovadoSituacaoHistorico());
                    sqlInserir.setBoolean(x++, obj.getTrancadoSituacaoHistorico());
                    sqlInserir.setBoolean(x++, obj.getCursandoSituacaoHistorico());
                    sqlInserir.setBoolean(x++, obj.getAbandonoCursoSituacaoHistorico());
                    sqlInserir.setBoolean(x++, obj.getTransferidoSituacaoHistorico());
                    sqlInserir.setBoolean(x++, obj.getCanceladoSituacaoHistorico());
                    sqlInserir.setBoolean(x++, obj.getAssinarDigitalmenteHistorico());
                    if (!obj.getCertificadoImpresso().getCodigo().equals(0)) {
                        sqlInserir.setInt(x++, obj.getCertificadoImpresso().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(x++, 0);
                    }
					sqlInserir.setBoolean(x++, obj.getApenasParaAlunosComTodasAulasRegistradas());
					sqlInserir.setBoolean(x++, obj.getVerificarAlunoPossuiPeloMenosUmaDisciplinaAprovada());
					sqlInserir.setBoolean(x++, obj.getRegistrarFormaturaAoRealizarImpressaoCerticadoDigital());
					sqlInserir.setBoolean(x++, obj.getCobrarTaxaSomenteCertificadoImpresso());
					sqlInserir.setString(x++, obj.getMensagemEmissaoCertificadoImpresso());
					sqlInserir.setBoolean(x++, obj.getPermitirAlterarDataPrevisaoConclusaoRequerimento());
					sqlInserir.setBoolean(x++, obj.getAbrirOutroRequerimentoAoDeferirEsteTipoRequerimento());
					Uteis.setValuePreparedStatement(obj.getQtdDiasCobrarTaxa(), x++, sqlInserir);

					if(!obj.getTipoRequerimentoAbrirDeferimento().getCodigo().equals(0)) {
						Uteis.setValuePreparedStatement(obj.getTipoRequerimentoAbrirDeferimento().getCodigo(), x++, sqlInserir);
					}else {
						sqlInserir.setNull(x++, 0);
					}
					Uteis.setValuePreparedStatement(obj.getValidarMatriculaIntegralizada(), x++, sqlInserir);
					sqlInserir.setBoolean(x++, obj.getOcultarUnidadeEnsinoListaTurmaReposicao());
					sqlInserir.setBoolean(x++, obj.getValidarDebitoFinanceiroRequerimentoIsento());
					sqlInserir.setBoolean(x++, obj.getPermitirReporDisciplinaComAulaProgramada());
					sqlInserir.setBoolean(x++, obj.getValidarEntregaTccAluno());
					sqlInserir.setBoolean(x++, obj.getDeferirAutomaticamenteDocumentoImpresso());
					sqlInserir.setString(x++, obj.getMsgBloqueioNovaSolicitacaoAproveitamento());
					sqlInserir.setInt(x++, obj.getQtdeMaximaIndeferidoAproveitamento());
					sqlInserir.setDouble(x++, obj.getPercentualMinimoCargaHorariaAproveitamento());
					sqlInserir.setInt(x++, obj.getQtdeMinimaDeAnosAproveitamento());
					sqlInserir.setBoolean(x++, obj.getDeferirAutomaticamenteTrancamento());
					sqlInserir.setString(x++, obj.getBloqueioSimultaneoPelo());
					sqlInserir.setBoolean(x++, obj.getPermitirAlunoAlterarUnidadeEnsino());
					sqlInserir.setBoolean(x++, obj.getPermitirAlunoRejeitarDocumento());
					sqlInserir.setInt(x++, obj.getPercentualIntegralizacaoCurricularInicial());
					sqlInserir.setInt(x++, obj.getPercentualIntegralizacaoCurricularFinal());
					sqlInserir.setBoolean(x++, obj.getRegistrarAproveitamentoDisciplinaTCC());
					sqlInserir.setBoolean(x++, obj.getRegistrarTrancamentoProximoSemestre());
					sqlInserir.setBoolean(x++, obj.getValidarAnoSemestreIngresso());
					sqlInserir.setString(x++, obj.getAnoIngresso());
					sqlInserir.setString(x++, obj.getSemestreIngresso());
					sqlInserir.setBoolean(x++, obj.getConsiderarTodasMatriculasAlunoValidacaoAberturaSimultanea());
					sqlInserir.setBoolean(x++, obj.getPermitirAlunoAlterarCurso());
					sqlInserir.setBoolean(x++, obj.getValidarVagasPorNumeroComputadoresUnidadeEnsino());
					sqlInserir.setBoolean(x++, obj.getRegistrarTransferenciaProximoSemestre());
					sqlInserir.setString(x++, obj.getCidDeferirAutomaticamente());
					sqlInserir.setBoolean(x++, obj.getValidarVagasPorNumeroComputadoresConsiderandoCurso());
					sqlInserir.setString(x++, obj.getAno());
					sqlInserir.setString(x++, obj.getSemestre());
					sqlInserir.setBoolean(x++, obj.getPermitirAproveitarDisciplinaCursando());
					sqlInserir.setString(x++, obj.getBimestre());
					sqlInserir.setString(x++, obj.getTipoNota());
					sqlInserir.setBoolean(x++, obj.getEnviarNotificacaoRequerente());
					sqlInserir.setBoolean(x++, obj.getCampoAfastamento());
					sqlInserir.setBoolean(x++, obj.getSituacaoMatriculaJubilado());
					sqlInserir.setBoolean(x++, obj.getUtilizarMensagemDeferimentoExclusivo());
					sqlInserir.setBoolean(x++, obj.getUtilizarMensagemIndeferimentoExclusivo());
					sqlInserir.setString(x++, obj.getTipoAluno().getValor());
					if ((!obj.getRequerimentoMembroComunidade()) && !obj.getTextoPadraoVO().getCodigo().equals(0)) {
                        sqlInserir.setInt(x++, obj.getTextoPadraoVO().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(x++, 0);
                    }
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
            obj.setNovoObj(Boolean.FALSE);
            getFacadeFactory().getTipoRequerimentoDepartamentoFacade().incluirTipoRequerimentoDepartamentoVOs(obj.getCodigo(), obj.getTipoRequerimentoDepartamentoVOs(), usuarioVO);
            getFacadeFactory().getTipoRequerimentoUnidadeEnsinoFacade().incluirItemTipoRequerimentoUnidadeEnsinoVOs(obj.getUnidadeEnsinoEspecificaVOs(), obj);
            getFacadeFactory().getTipoRequerimentoCursoFacade().incluirTipoRequerimentoCursoVOs(obj.getCodigo(), obj.getTipoRequerimentoCursoVOs(), usuarioVO);
            getFacadeFactory().getCidTipoRequerimentoInterfaceFacade().incluirCidTipoRequerimentoVOs(obj.getCodigo(), obj.getCidTipoRequerimentoVOs(), usuarioVO);
            if(obj.getVerificarPendenciaDocumentacao()) {
            	getFacadeFactory().getPendenciaTipoDocumentoTipoRequerimentoInterfaceFacade().incluirPendenciasTipoDocumentoTipoRequerimento(obj.getCodigo(), obj.getPendenciaTipoDocumentoTipoRequerimentoVOs(), usuarioVO);
            }
            verificarPersistirPersonalizacaoMensagemAutomaticaTipoRequerimento(obj, usuarioVO);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>TipoRequerimentoVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>TipoRequerimentoVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void inativar(final TipoRequerimentoVO obj, UsuarioVO usuarioVO) throws Exception {
        try {
            TipoRequerimentoVO.validarDados(obj);
            TipoRequerimento.alterar(getIdEntidade(), true, usuarioVO);
            final String sql = "UPDATE TipoRequerimento set situacao=? WHERE (codigo = ?)"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement sqlAlterar = con.prepareStatement(sql);
                    sqlAlterar.setString(1, obj.getSituacao());
                    sqlAlterar.setInt(2, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final TipoRequerimentoVO obj, UsuarioVO usuarioVO) throws Exception {
        try {
            TipoRequerimentoVO.validarDados(obj);
            TipoRequerimento.alterar(getIdEntidade(), true, usuarioVO);
            final StringBuilder sql = new StringBuilder("UPDATE TipoRequerimento set nome=?,  prazoExecucao=?, ");
            		sql.append(" departamentoResponsavel=?, orientacao=?, haDocumentoParaRetirada=?, ");
                    sql.append(" tipo=?, requerimentoVisaoAluno=?, ");
                    sql.append(" requerimentoVisaoProfessor=?, requerimentoVisaoCoordenador=?, permitirUploadArquivo=?, ");
                    sql.append(" permitirInformarEnderecoEntrega=?, qtdDiasVencimentoRequerimento=?, ");
                    sql.append(" requerimentoVisaoPai=?, diasParaExclusaoRequerimentoDefazados=?, ");
                    sql.append(" situacao=?, requerimentoSituacaoFinanceiroVisaoAluno=?, unidadeEnsinoEspecifico=?, ");
                    sql.append(" mensagemAlerta=?, tramitaEntreDepartamentos=?, textoPadrao = ?, questionario = ?, ");
                    sql.append(" centroReceitaRequerimentoPadrao=?, requerimentominhasnotas=?,sigla=?, ");
                    sql.append(" situacaoMatriculaAtiva = ?, situacaoMatriculaPreMatriculada = ?, situacaoMatriculaCancelada = ?, situacaoMatriculaTrancada = ?, ");
                    sql.append(" situacaoMatriculaAbandonada = ?, situacaoMatriculaTransferida = ?, situacaoMatriculaFormada = ?, verificarPendenciaBiblioteca = ?, verificarPendenciaFinanceira = ?, verificarPendenciaBibliotecaAtraso= ?, verificarPendenciaFinanceiraAtraso = ?, ");
                    sql.append(" verificarPendenciaDocumentacao = ?, verificarPendenciaEnade = ? , verificarPendenciaEstagio = ?, verificarPendenciaAtividadeComplementar = ?, taxa = ?, ");
                    sql.append(" cobrarApartirVia = ?, tipoControleCobrancaViaRequerimento = ?, requerimentoVisaoAlunoApresImprimirDeclaracao=?,deferirAutomaticamente=?,qtdeDiasDisponivel=?, qtdeDiasAposPrimeiraImpressao=?, requerimentoVisaoFuncionario=?, considerarSegundaViaIndependenteSituacaoPrimeiraVia = ?, requerimentoMembroComunidade = ?, bloquearRecebimentoCartaoCreditoOnline=? , ");                    
                    sql.append("  assinarDigitalmenteDeclaracoesGeradasNoRequerimento = ?, considerardiasuteis=?, verificarPendenciaApenasMatriculaRequerimento=?, tipoUploadArquivo = ?, extensaoArquivo = ?, orientacaoUploadArquivo = ?, requerAutorizacaoPagamento = ?, permiteDeferirAguardandoAutorizacaoPagamento = ?,  ");
                    sql.append(" realizarIsencaoTaxaReposicaoMatriculaAposDataAula = ?, quantidadeDiasAntesAulaPermitirExclusaoRequerimentoReposicao = ?, permitirSolicitacaoIsencaoTaxaRequerimento = ?, solicitarAnexoComprovanteIsencaoTaxaRequerimento = ?, orientacaoDocumentoComprovanteIsencaoTaxaRequerimento = ?, permiteIncluirDisciplinaPorEquivalencia = ?, ");
                    sql.append(" permiteIncluirReposicaoTurmaOutraUnidade  = ?, permiteIncluirReposicaoTurmaOutroCurso = ?, permitirReposicaoComChoqueHorario = ?, usarCentroResultadoTurma=?, uploadArquivoObrigatorio=?, orientacaoAtendente=?, ");
                    sql.append(" bloquearQuantidadeRequerimentoAbertosSimultaneamente = ?, quantidadeLimiteRequerimentoAbertoSimultaneamente = ?, considerarBloqueioSimultaneoRequerimentoDeferido = ?, considerarBloqueioSimultaneoRequerimentoIndeferido = ?, ");
                    sql.append(" permitirImpressaoHistoricoVisaoAluno=?, nivelEducacional=?, layoutHistoricoApresentar=?, aprovadoSituacaoHistorico=?, reprovadoSituacaoHistorico=?, trancadoSituacaoHistorico=?, ");
                    sql.append(" cursandoSituacaoHistorico=?, abandonoCursoSituacaoHistorico=?, transferidoSituacaoHistorico=?, canceladoSituacaoHistorico=?, assinarDigitalmenteHistorico=?, certificadoImpresso=?, apenasParaAlunosComTodasAulasRegistradas=?, verificarAlunoPossuiPeloMenosUmaDisciplinaAprovada=?, registrarFormaturaAoRealizarImpressaoCerticadoDigital=?, cobrarTaxaSomenteCertificadoImpresso=?, mensagemEmissaoCertificadoImpresso=?, ");
                    sql.append(" permitirAlterarDataPrevisaoConclusaoRequerimento = ?, abrirOutroRequerimentoAoDeferirEsteTipoRequerimento = ?, qtdDiasCobrarTaxa = ?, tipoRequerimentoAbrirDeferimento = ?, validarMatriculaIntegralizada = ?, ocultarunidadeensinolistaturmareposicao =? ,validarDebitoFinanceiroRequerimentoIsento=?, permitirReporDisciplinaComAulaProgramada=?, validarEntregaTccAluno=?, deferirAutomaticamenteDocumentoImpresso=?, ");
                    sql.append(" msgBloqueioNovaSolicitacaoAproveitamento=?, qtdeMaximaIndeferidoAproveitamento=? , percentualMinimoCargaHorariaAproveitamento=?, qtdeMinimaDeAnosAproveitamento=?, deferirAutomaticamenteTrancamento=?, bloqueioSimultaneoPelo=? , permitirAlunoAlterarUnidadeEnsino=?, permitirAlunoRejeitarDocumento=?, percentualIntegralizacaoCurricularInicial=?, percentualIntegralizacaoCurricularFinal=?, registrarAproveitamentoDisciplinaTCC =?, ");
                    sql.append(" registrarTrancamentoProximoSemestre = ?, validarAnoSemestreIngresso=?, anoingresso=?, semestreingresso=?, considerarTodasMatriculasAlunoValidacaoAberturaSimultanea=?, ");
                	sql.append(" permitirAlunoAlterarCurso = ?, validarVagasPorNumeroComputadoresUnidadeEnsino = ?, registrarTransferenciaProximoSemestre = ?, cidDeferirAutomaticamente = ?, validarVagasPorNumeroComputadoresConsiderandoCurso = ?, ano = ?, semestre = ?,  permitirAproveitarDisciplinasCursando = ?, bimestre=?, tipoNota = ?, enviarNotificacaoRequerente =?, campoafastamento=?, situacaoMatriculaJubilado=?, utilizarmensagemdeferimentoexclusivo=?, utilizarmensagemindeferimentoexclusivo=?, tipoAluno=?, ");
                	sql.append(" textopadraocontratoestagio = ? ");
                    sql.append("  WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement sqlAlterar = con.prepareStatement(sql.toString());
                    int x = 1;
                    sqlAlterar.setString(x++, obj.getNome());
                    
                    sqlAlterar.setInt(x++, obj.getPrazoExecucao().intValue());
                    if (!obj.getDepartamentoResponsavel().getCodigo().equals(0)) {
                        sqlAlterar.setInt(x++, obj.getDepartamentoResponsavel().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(x++, 0);
                    }
                    sqlAlterar.setString(x++, obj.getOrientacao());
                    sqlAlterar.setBoolean(x++, obj.getHaDocumentoParaRetirada().booleanValue());
                    
                    sqlAlterar.setString(x++, obj.getTipo());
                    sqlAlterar.setBoolean(x++, obj.getRequerimentoVisaoAluno());
                    sqlAlterar.setBoolean(x++, obj.getRequerimentoVisaoProfessor());
                    sqlAlterar.setBoolean(x++, obj.getRequerimentoVisaoCoordenador());
                    sqlAlterar.setBoolean(x++, obj.getPermitirUploadArquivo());
                    sqlAlterar.setBoolean(x++, obj.getPermitirInformarEnderecoEntrega());
                    sqlAlterar.setInt(x++, obj.getQtdDiasVencimentoRequerimento().intValue());
                    sqlAlterar.setBoolean(x++, obj.getRequerimentoVisaoPai());
                    sqlAlterar.setInt(x++, obj.getDiasParaExclusaoRequerimentoDefazados());
                    sqlAlterar.setString(x++, obj.getSituacao());
                    sqlAlterar.setBoolean(x++, obj.getRequerimentoSituacaoFinanceiroVisaoAluno());
                    sqlAlterar.setBoolean(x++, obj.getUnidadeEnsinoEspecifica());
                    sqlAlterar.setString(x++, obj.getMensagemAlerta());
                    sqlAlterar.setBoolean(x++, obj.getTramitaEntreDepartamentos());
                    if ((!obj.getRequerimentoMembroComunidade()) && !obj.getTextoPadrao().getCodigo().equals(0)) {
                    	sqlAlterar.setInt(x++, obj.getTextoPadrao().getCodigo().intValue());
                    } else {
                    	sqlAlterar.setNull(x++, 0);
                    }
                    if (!obj.getQuestionario().getCodigo().equals(0)) {
                    	sqlAlterar.setInt(x++, obj.getQuestionario().getCodigo().intValue());
                    } else {
                    	sqlAlterar.setNull(x++, 0);
                    }
                    if (!obj.getCentroReceitaRequerimentoPadrao().getCodigo().equals(0)) {
                    	sqlAlterar.setInt(x++, obj.getCentroReceitaRequerimentoPadrao().getCodigo().intValue());
                    } else {
                    	sqlAlterar.setNull(x++, 0);
                    }
                    
                    sqlAlterar.setBoolean(x++, obj.getRequerimentoMinhasNotas());
                    sqlAlterar.setString(x++, obj.getSigla());
                    sqlAlterar.setBoolean(x++, obj.getSituacaoMatriculaAtiva());
                    sqlAlterar.setBoolean(x++, obj.getSituacaoMatriculaPreMatriculada());
                    sqlAlterar.setBoolean(x++, obj.getSituacaoMatriculaCancelada());
                    sqlAlterar.setBoolean(x++, obj.getSituacaoMatriculaTrancada());
                    sqlAlterar.setBoolean(x++, obj.getSituacaoMatriculaAbandonada());
                    sqlAlterar.setBoolean(x++, obj.getSituacaoMatriculaTransferida());
                    sqlAlterar.setBoolean(x++, obj.getSituacaoMatriculaFormada());
                    sqlAlterar.setBoolean(x++, obj.getVerificarPendenciaBiblioteca());
                    sqlAlterar.setBoolean(x++, obj.getVerificarPendenciaFinanceira());
                    sqlAlterar.setBoolean(x++, obj.getVerificarPendenciaBibliotecaAtraso());
                    sqlAlterar.setBoolean(x++, obj.getVerificarPendenciaFinanceiraAtraso());
                    sqlAlterar.setBoolean(x++, obj.getVerificarPendenciaDocumentacao());
                    sqlAlterar.setBoolean(x++, obj.getVerificarPendenciaEnade());
                    sqlAlterar.setBoolean(x++, obj.getVerificarPendenciaEstagio());
                    sqlAlterar.setBoolean(x++, obj.getVerificarPendenciaAtividadeComplementar());
                    if (!obj.getTaxa().getCodigo().equals(0)) {
                    	sqlAlterar.setInt(x++, obj.getTaxa().getCodigo().intValue());
                    } else {
                    	sqlAlterar.setNull(x++, 0);
                    }
                    sqlAlterar.setInt(x++, obj.getCobrarApartirVia());
                    if (obj.getTipoControleCobrancaViaRequerimento() != null) {
                    	sqlAlterar.setString(x++, obj.getTipoControleCobrancaViaRequerimento().name());
                    } else {
                    	sqlAlterar.setNull(x++, 0);
                    }
                    sqlAlterar.setBoolean(x++, obj.getRequerimentoVisaoAlunoApresImprimirDeclaracao());
                    sqlAlterar.setBoolean(x++, obj.getDeferirAutomaticamente());
                    if(obj.getQtdeDiasDisponivel() != null){
                    	sqlAlterar.setInt(x++, obj.getQtdeDiasDisponivel());
                    }else{
                    	sqlAlterar.setNull(x++, 0);
                    }
                    if(obj.getQtdeDiasAposPrimeiraImpressao() != null){
                    	sqlAlterar.setInt(x++, obj.getQtdeDiasAposPrimeiraImpressao());
                    }else {
						sqlAlterar.setNull(x++, 0);
					}
                    sqlAlterar.setBoolean(x++, obj.getRequerimentoVisaoFuncionario());
                    sqlAlterar.setBoolean(x++, obj.getConsiderarSegundaViaIndependenteSituacaoPrimeiraVia());
                    sqlAlterar.setBoolean(x++, obj.getRequerimentoMembroComunidade());
                    sqlAlterar.setBoolean(x++, obj.getBloquearRecebimentoCartaoCreditoOnline());
                    sqlAlterar.setBoolean(x++, obj.getAssinarDigitalmenteDeclaracoesGeradasNoRequerimento());
                    sqlAlterar.setBoolean(x++, obj.getConsiderarDiasUteis());
                    sqlAlterar.setBoolean(x++, obj.getVerificarPendenciaApenasMatriculaRequerimento());
                    sqlAlterar.setString(x++, obj.getTipoUploadArquivo().name());
                    sqlAlterar.setString(x++, obj.getExtensaoArquivo());
                    sqlAlterar.setString(x++, obj.getOrientacaoUploadArquivo());
                    sqlAlterar.setBoolean(x++, obj.getRequerAutorizacaoPagamento());
                    sqlAlterar.setBoolean(x++, obj.getPermiteDeferirAguardandoAutorizacaoPagamento());
                    sqlAlterar.setBoolean(x++, obj.getRealizarIsencaoTaxaReposicaoMatriculaAposDataAula());
                    sqlAlterar.setInt(x++, obj.getQuantidadeDiasAntesAulaPermitirExclusaoRequerimentoReposicao());
                    sqlAlterar.setBoolean(x++, obj.getPermitirSolicitacaoIsencaoTaxaRequerimento());
                    sqlAlterar.setBoolean(x++, obj.getSolicitarAnexoComprovanteIsencaoTaxaRequerimento());
                    sqlAlterar.setString(x++, obj.getOrientacaoDocumentoComprovanteIsencaoTaxaRequerimento());
                    sqlAlterar.setBoolean(x++, obj.getPermiteIncluirDisciplinaPorEquivalencia());
                    sqlAlterar.setBoolean(x++, obj.getPermiteIncluirReposicaoTurmaOutraUnidade());
                    sqlAlterar.setBoolean(x++, obj.getPermiteIncluirReposicaoTurmaOutroCurso());
                    sqlAlterar.setBoolean(x++, obj.getPermitirReposicaoComChoqueHorario());
                    sqlAlterar.setBoolean(x++, obj.getUsarCentroResultadoTurma());
                    sqlAlterar.setBoolean(x++, obj.getUploadArquivoObrigatorio());
					if (Uteis.isAtributoPreenchido(obj.getOrientacaoAtendente())) {
						sqlAlterar.setString(x++, obj.getOrientacaoAtendente());
					} else {
						sqlAlterar.setNull(x++, 0);
					}
                    sqlAlterar.setBoolean(x++, obj.getBloquearQuantidadeRequerimentoAbertosSimultaneamente());
                    sqlAlterar.setInt(x++, obj.getQuantidadeLimiteRequerimentoAbertoSimultaneamente());
                    sqlAlterar.setBoolean(x++, obj.getConsiderarBloqueioSimultaneoRequerimentoDeferido());
                    sqlAlterar.setBoolean(x++, obj.getConsiderarBloqueioSimultaneoRequerimentoIndeferido());
                    if (obj.getTipo().equals(TiposRequerimento.HISTORICO.getValor())) {
                    	sqlAlterar.setBoolean(x++, obj.getPermitirImpressaoHistoricoVisaoAluno());
                    } else {
                    	sqlAlterar.setBoolean(x++, false);
                    }
                    sqlAlterar.setString(x++, obj.getNivelEducacional());
                    if (obj.getTipo().equals(TiposRequerimento.HISTORICO.getValor())) {
                    	sqlAlterar.setString(x++, obj.getLayoutHistoricoApresentar());
                    } else {
                    	sqlAlterar.setString(x++, "");
                    }
                    sqlAlterar.setBoolean(x++, obj.getAprovadoSituacaoHistorico());
                    sqlAlterar.setBoolean(x++, obj.getReprovadoSituacaoHistorico());
                    sqlAlterar.setBoolean(x++, obj.getTrancadoSituacaoHistorico());
                    sqlAlterar.setBoolean(x++, obj.getCursandoSituacaoHistorico());
                    sqlAlterar.setBoolean(x++, obj.getAbandonoCursoSituacaoHistorico());
                    sqlAlterar.setBoolean(x++, obj.getTransferidoSituacaoHistorico());
                    sqlAlterar.setBoolean(x++, obj.getCanceladoSituacaoHistorico());
                    sqlAlterar.setBoolean(x++, obj.getAssinarDigitalmenteHistorico());
                    
                    if (!obj.getCertificadoImpresso().getCodigo().equals(0)) {
                    	sqlAlterar.setInt(x++, obj.getCertificadoImpresso().getCodigo().intValue());
                    } else {
                    	sqlAlterar.setNull(x++, 0);
                    }
					sqlAlterar.setBoolean(x++, obj.getApenasParaAlunosComTodasAulasRegistradas());
					sqlAlterar.setBoolean(x++, obj.getVerificarAlunoPossuiPeloMenosUmaDisciplinaAprovada());
					sqlAlterar.setBoolean(x++, obj.getRegistrarFormaturaAoRealizarImpressaoCerticadoDigital());
					sqlAlterar.setBoolean(x++, obj.getCobrarTaxaSomenteCertificadoImpresso());
					sqlAlterar.setString(x++, obj.getMensagemEmissaoCertificadoImpresso());
					
					Uteis.setValuePreparedStatement(obj.getPermitirAlterarDataPrevisaoConclusaoRequerimento(), x++, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getAbrirOutroRequerimentoAoDeferirEsteTipoRequerimento(), x++, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getQtdDiasCobrarTaxa(), x++, sqlAlterar);
						
					if(Uteis.isAtributoPreenchido(obj.getTipoRequerimentoAbrirDeferimento().getCodigo())) {
						Uteis.setValuePreparedStatement(obj.getTipoRequerimentoAbrirDeferimento().getCodigo(), x++, sqlAlterar);
					}
					else {
						sqlAlterar.setNull(x++, 0);
					}
					
					Uteis.setValuePreparedStatement(obj.getValidarMatriculaIntegralizada(), x++, sqlAlterar);
 					sqlAlterar.setBoolean(x++, obj.getOcultarUnidadeEnsinoListaTurmaReposicao());
					
					sqlAlterar.setBoolean(x++, obj.getValidarDebitoFinanceiroRequerimentoIsento());
					sqlAlterar.setBoolean(x++, obj.getPermitirReporDisciplinaComAulaProgramada());
					sqlAlterar.setBoolean(x++, obj.getValidarEntregaTccAluno());
					sqlAlterar.setBoolean(x++, obj.getDeferirAutomaticamenteDocumentoImpresso());
					sqlAlterar.setString(x++, obj.getMsgBloqueioNovaSolicitacaoAproveitamento());
					sqlAlterar.setInt(x++, obj.getQtdeMaximaIndeferidoAproveitamento());
					sqlAlterar.setDouble(x++, obj.getPercentualMinimoCargaHorariaAproveitamento());
					sqlAlterar.setInt(x++, obj.getQtdeMinimaDeAnosAproveitamento());
					sqlAlterar.setBoolean(x++, obj.getDeferirAutomaticamenteTrancamento());
					sqlAlterar.setString(x++, obj.getBloqueioSimultaneoPelo());
					sqlAlterar.setBoolean(x++, obj.getPermitirAlunoAlterarUnidadeEnsino());
					sqlAlterar.setBoolean(x++, obj.getPermitirAlunoRejeitarDocumento());
					sqlAlterar.setInt(x++, obj.getPercentualIntegralizacaoCurricularInicial());
					sqlAlterar.setInt(x++, obj.getPercentualIntegralizacaoCurricularFinal());
					sqlAlterar.setBoolean(x++, obj.getRegistrarAproveitamentoDisciplinaTCC());
					sqlAlterar.setBoolean(x++, obj.getRegistrarTrancamentoProximoSemestre());
					sqlAlterar.setBoolean(x++, obj.getValidarAnoSemestreIngresso());
					sqlAlterar.setString(x++, obj.getAnoIngresso());
					sqlAlterar.setString(x++, obj.getSemestreIngresso());
					sqlAlterar.setBoolean(x++, obj.getConsiderarTodasMatriculasAlunoValidacaoAberturaSimultanea());
					sqlAlterar.setBoolean(x++, obj.getPermitirAlunoAlterarCurso());
					sqlAlterar.setBoolean(x++, obj.getValidarVagasPorNumeroComputadoresUnidadeEnsino());
					sqlAlterar.setBoolean(x++, obj.getRegistrarTransferenciaProximoSemestre());
					sqlAlterar.setString(x++, obj.getCidDeferirAutomaticamente());
					sqlAlterar.setBoolean(x++, obj.getValidarVagasPorNumeroComputadoresConsiderandoCurso());
					if((obj.getTipo().equals("TI") && !obj.getRegistrarTransferenciaProximoSemestre()) || (obj.getTipo().equals("TR") && !obj.getRegistrarTrancamentoProximoSemestre())) {
						sqlAlterar.setString(x++, "");
						sqlAlterar.setString(x++, "");
					}else {
						sqlAlterar.setString(x++, obj.getAno());
						sqlAlterar.setString(x++, obj.getSemestre());
					}
					sqlAlterar.setBoolean(x++, obj.getPermitirAproveitarDisciplinaCursando());
					sqlAlterar.setString(x++, obj.getBimestre());
					sqlAlterar.setString(x++, obj.getTipoNota());
					sqlAlterar.setBoolean(x++, obj.getEnviarNotificacaoRequerente());
					sqlAlterar.setBoolean(x++, obj.getCampoAfastamento());
					sqlAlterar.setBoolean(x++, obj.getSituacaoMatriculaJubilado());
					sqlAlterar.setBoolean(x++, obj.getUtilizarMensagemDeferimentoExclusivo());
					sqlAlterar.setBoolean(x++, obj.getUtilizarMensagemIndeferimentoExclusivo());
					sqlAlterar.setString(x++, obj.getTipoAluno().getValor());
					if ((!obj.getRequerimentoMembroComunidade()) && !obj.getTextoPadraoVO().getCodigo().equals(0)) {
                    	sqlAlterar.setInt(x++, obj.getTextoPadraoVO().getCodigo().intValue());
                    } else {
                    	sqlAlterar.setNull(x++, 0);
                    }
					sqlAlterar.setInt(x++, obj.getCodigo().intValue());

                    return sqlAlterar;
                }
            });
            getFacadeFactory().getTipoRequerimentoDepartamentoFacade().alterarTipoRequerimentoDepartamentoVOs(obj.getCodigo(), obj.getTipoRequerimentoDepartamentoVOs(), null);
            getFacadeFactory().getTipoRequerimentoUnidadeEnsinoFacade().alterarItemTipoRequerimentoUnidadeEnsinoVOs(obj.getUnidadeEnsinoEspecificaVOs(), obj);
            getFacadeFactory().getTipoRequerimentoCursoFacade().alterarTipoRequerimentoCursoVOs(obj.getCodigo(), obj.getTipoRequerimentoCursoVOs(), usuarioVO);
            getFacadeFactory().getCidTipoRequerimentoInterfaceFacade().alterarCidTipoRequerimentoVOs(obj.getCodigo(), obj.getCidTipoRequerimentoVOs(), usuarioVO);

            if(obj.getVerificarPendenciaDocumentacao()) {
            	getFacadeFactory().getPendenciaTipoDocumentoTipoRequerimentoInterfaceFacade().alterarPendenciasTipoDocumentoTipoRequerimento(obj.getCodigo(), obj.getPendenciaTipoDocumentoTipoRequerimentoVOs(), usuarioVO);
            }
            else {
            	getFacadeFactory().getPendenciaTipoDocumentoTipoRequerimentoInterfaceFacade().excluirPendenciaTipoDocumentoTipoRequerimentoVOs(obj.getCodigo(), null, usuarioVO);
            }
            verificarPersistirPersonalizacaoMensagemAutomaticaTipoRequerimento(obj, usuarioVO);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>TipoRequerimentoVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>TipoRequerimentoVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(TipoRequerimentoVO obj, UsuarioVO usuarioVO) throws Exception {
        try {
            TipoRequerimento.excluir(getIdEntidade(), true, usuarioVO);
            List<TipoRequerimentoDepartamentoVO> tipoRequerimentoDepartamentoVOs = new ArrayList<>(0);
            getFacadeFactory().getTipoRequerimentoDepartamentoFacade().excluirTipoRequerimentoDepartamentoVOs(obj.getCodigo(), tipoRequerimentoDepartamentoVOs , null);
            getFacadeFactory().getTipoRequerimentoUnidadeEnsinoFacade().excluirItemTipoRequerimentoUnidadeEnsinoVOs(obj.getUnidadeEnsinoEspecificaVOs(), obj);
            getFacadeFactory().getCidTipoRequerimentoInterfaceFacade().excluirCidTipoRequerimentoVOs(obj.getCodigo(), obj.getCidTipoRequerimentoVOs(), usuarioVO);
            getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().excluirPorTipoRequerimento(obj.getCodigo(), usuarioVO);
            String sql = "DELETE FROM TipoRequerimento WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
            getConexao().getJdbcTemplate().update(sql, obj.getCodigo());
        } catch (Exception e) {
        	if (Uteis.isAtributoPreenchido(e.getMessage()) && e.getMessage().contains("fk_requerimento_tiporequerimento")) {
        		throw new Exception("Existem requerimentos vinculados ao tipo de requerimento de código " + obj.getCodigo());
        	}
            throw e;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>TipoRequerimento</code> através do valor do atributo 
     * <code>nome</code> da classe <code>Departamento</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>TipoRequerimentoVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<TipoRequerimentoVO> consultarPorNomeDepartamento(String valorConsulta, String situacao, Integer unidadeEnsino,  int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("SELECT TipoRequerimento.* , (select taxavalor.valor from taxavalor where taxa = TipoRequerimento.taxa and taxavalor.data <= current_date order by taxavalor.data desc limit 1 ) as valor ");
        sqlStr.append(" FROM TipoRequerimento WHERE exists (");
        sqlStr.append(" select Departamento.codigo from TipoRequerimentoDepartamento inner join Departamento on Departamento.codigo =  TipoRequerimentoDepartamento.Departamento ");
        sqlStr.append(" where sem_acentos(Departamento.nome) ilike(sem_acentos(?)) ");
        sqlStr.append(" and TipoRequerimentoDepartamento.TipoRequerimento = TipoRequerimento.codigo ");
        sqlStr.append(" ");
        
        if(situacao != null && !situacao.equals("") && !situacao.equals("TO")) {
        	sqlStr.append(" and TipoRequerimento.situacao = '").append(situacao).append("' ");	
        }
        if(Uteis.isAtributoPreenchido(unidadeEnsino)) {
        	sqlStr.append(" and (TipoRequerimento.unidadeEnsinoEspecifico = false or ");
        	sqlStr.append(" ( unidadeEnsinoEspecifico = true and exists (select tipoRequerimentoUnidadeEnsino.codigo from tipoRequerimentoUnidadeEnsino ");
        	sqlStr.append(" where tipoRequerimentoUnidadeEnsino.tipoRequerimento = tipoRequerimento.codigo and tipoRequerimentoUnidadeEnsino.unidadeensino = ").append(unidadeEnsino).append(" )))");
        }
        sqlStr.append(" ) ORDER BY TipoRequerimento.nome");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta+"%");
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    public List<TipoRequerimentoVO> consultarPorSituacao(String valorConsulta, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT TipoRequerimento.* , (select taxavalor.valor from taxavalor where taxa = TipoRequerimento.taxa and taxavalor.data <= current_date order by taxavalor.data desc limit 1 ) as valor FROM TipoRequerimento WHERE TipoRequerimento.situacao ilike('" + valorConsulta + "') ORDER BY TipoRequerimento.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    @Override
    public List<TipoRequerimentoVO> consultarTipoRequerimentoPorSituacaoESemVinculoCalendarioAbertura(String valorConsulta,int valorCodigo, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("SELECT TipoRequerimento.* , ");
        sqlStr.append("(select taxavalor.valor from taxavalor where taxa = TipoRequerimento.taxa and taxavalor.data <= current_date order by taxavalor.data desc limit 1 ) ");
        sqlStr.append( "as valor FROM TipoRequerimento ");
        sqlStr.append( "WHERE TipoRequerimento.situacao = ? ");
        sqlStr.append( "AND ( TipoRequerimento.codigo not in (select tiporequerimento from calendarioaberturatiporequerimentoraprazo where calendarioaberturatiporequerimentoraprazo.calendarioaberturarequerimento = ?)) ");
        sqlStr.append( "ORDER BY TipoRequerimento.nome");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta, valorCodigo);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }
    
    /**
     * Responsável por realizar uma consulta de <code>TipoRequerimento</code> através do valor do atributo 
     * <code>Integer prazoExecucao</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>TipoRequerimentoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<TipoRequerimentoVO> consultarPorPrazoExecucao(Integer valorConsulta, String situacao, Integer unidadeEnsino, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("SELECT * , (select taxavalor.valor from taxavalor where taxa = TipoRequerimento.taxa and taxavalor.data <= current_date order by taxavalor.data desc limit 1 ) as valor ");
        sqlStr.append(" FROM TipoRequerimento WHERE prazoExecucao >= ").append(valorConsulta.intValue());
        if(situacao != null && !situacao.equals("") && !situacao.equals("TO")) {
        	sqlStr.append(" and TipoRequerimento.situacao = '").append(situacao).append("' ");	
        }
        if(Uteis.isAtributoPreenchido(unidadeEnsino)) {
        	sqlStr.append(" and (TipoRequerimento.unidadeEnsinoEspecifica = false or ");
        	sqlStr.append(" ( unidadeEnsinoEspecifica = true and exists (select tipoRequerimentoUnidadeEnsino.codigo from tipoRequerimentoUnidadeEnsino ");
        	sqlStr.append(" where tipoRequerimentoUnidadeEnsino.tipoRequerimento = tipoRequerimento.codigo and tipoRequerimentoUnidadeEnsino.unidadeensino = ").append(unidadeEnsino).append(" )))");
        }
        sqlStr.append(" ORDER BY prazoExecucao");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>TipoRequerimento</code> através do valor do atributo 
     * <code>Double valor</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>TipoRequerimentoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<TipoRequerimentoVO> consultarPorValor(Double valorConsulta, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * , (select taxavalor.valor from taxavalor where taxa = TipoRequerimento.taxa and taxavalor.data <= current_date order by taxavalor.data desc limit 1 ) as valor FROM TipoRequerimento WHERE (select taxavalor.valor from taxavalor where taxa = TipoRequerimento.taxa and taxavalor.data <= current_date order by taxavalor.data desc limit 1 ) >= " + valorConsulta.doubleValue() + " ORDER BY valor";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>TipoRequerimento</code> através do valor do atributo 
     * <code>String nome</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>TipoRequerimentoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<TipoRequerimentoVO> consultarPorNome(String valorConsulta, String situacao, Integer unidadeEnsino, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder(" SELECT *, (select taxavalor.valor from taxavalor where taxa = TipoRequerimento.taxa and taxavalor.data <= current_date order by taxavalor.data desc limit 1 ) as valor ");
        sqlStr.append(" FROM TipoRequerimento WHERE sem_acentos(nome) ilike(sem_acentos(?))");
        if(situacao != null && !situacao.equals("") && !situacao.equals("TO")) {
        	sqlStr.append(" and TipoRequerimento.situacao = '").append(situacao).append("' ");	
        }
        if(Uteis.isAtributoPreenchido(unidadeEnsino)) {
        	sqlStr.append(" and (TipoRequerimento.unidadeEnsinoEspecifico = false or ");
        	sqlStr.append(" ( unidadeEnsinoEspecifico = true and exists (select tipoRequerimentoUnidadeEnsino.codigo from tipoRequerimentoUnidadeEnsino ");
        	sqlStr.append(" where tipoRequerimentoUnidadeEnsino.tipoRequerimento = tipoRequerimento.codigo and tipoRequerimentoUnidadeEnsino.unidadeensino = ").append(unidadeEnsino).append(" )))");
        }
        sqlStr.append("  ORDER BY nome ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta+"%");
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List<TipoRequerimentoVO> consultarPorNome(String valorConsulta, String situacao, List<UnidadeEnsinoVO> unidadeEnsinos, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
    	ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
    	StringBuilder sqlStr = new StringBuilder(" SELECT *, (select taxavalor.valor from taxavalor where taxa = TipoRequerimento.taxa and taxavalor.data <= current_date order by taxavalor.data desc limit 1 ) as valor ");
    	sqlStr.append(" FROM TipoRequerimento WHERE sem_acentos(nome) ilike(sem_acentos(?))");
    	if(situacao != null && !situacao.equals("") && !situacao.equals("TO")) {
    		sqlStr.append(" and TipoRequerimento.situacao = '").append(situacao).append("' ");	
    	}
    	if(Uteis.isAtributoPreenchido(unidadeEnsinos)) {
    		sqlStr.append(" and (TipoRequerimento.unidadeEnsinoEspecifico = false or ");
    		sqlStr.append(" ( unidadeEnsinoEspecifico = true and exists (select tipoRequerimentoUnidadeEnsino.codigo from tipoRequerimentoUnidadeEnsino ");
    		sqlStr.append(" where tipoRequerimentoUnidadeEnsino.tipoRequerimento = tipoRequerimento.codigo and tipoRequerimentoUnidadeEnsino.unidadeensino in (0");
    		for (UnidadeEnsinoVO unidadeEnsino : unidadeEnsinos) {
    			sqlStr.append("," + unidadeEnsino.getCodigo());	
    		}    		
    		sqlStr.append(") )))");    		
    	}
    	sqlStr.append("  ORDER BY nome ");
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta+"%");
    	return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List<TipoRequerimentoVO> consultarTipoRequerimentoComboBox(boolean controlarAcesso, String situacao, Integer unidadeensino,Integer curso, boolean permiteAbriReqForaDoPrazo, UsuarioVO usuario, Boolean permitirUsuarioConsultarIncluirApenasRequerimentosProprios) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT distinct TipoRequerimento.codigo, nome FROM TipoRequerimento ";
        sqlStr += " left join tiporequerimentounidadeensino tu on tu.tiporequerimento = tiporequerimento.codigo ";
        sqlStr += " where situacao = '" + situacao + "' ";
        if (unidadeensino != 0) {
            sqlStr += " and (unidadeensinoespecifico = false or unidadeensinoespecifico is null or tu.unidadeensino = " + unidadeensino + ")";
        }
        if(usuario.getIsApresentarVisaoProfessor()){
        	sqlStr += " and (TipoRequerimento.requerimentoVisaoProfessor) ";
        }
        if(usuario.getIsApresentarVisaoCoordenador()){
        	sqlStr += " and (TipoRequerimento.requerimentoVisaoCoordenador) ";
        }
        if(usuario.getIsApresentarVisaoAluno()){
        	sqlStr += " and (TipoRequerimento.requerimentoVisaoAluno) ";
        }
        if(usuario.getIsApresentarVisaoPais()){
        	sqlStr += " and (TipoRequerimento.requerimentoVisaoPai) ";
        }        
		if (curso != null && !curso.equals(0)) {
			sqlStr += " and ((select tiporequerimentocurso.codigo from tiporequerimentocurso where tiporequerimentocurso.tiporequerimento = TipoRequerimento.codigo limit 1 ) is null ";
			sqlStr += " or ((select tiporequerimentocurso.codigo from tiporequerimentocurso where tiporequerimentocurso.tiporequerimento = TipoRequerimento.codigo and tiporequerimentocurso.curso = " + curso + " limit 1 ) is not null)) ";
			sqlStr += getSQLValidarNivelEducacionalTipoRequerimentoCursoAluno(curso);
		}
		if(!permiteAbriReqForaDoPrazo){
			sqlStr += " and (	";
			sqlStr += " 		exists ( ";
			sqlStr += " 			select ";
			sqlStr += " 				calendarioPrazo.tiporequerimento ";
			sqlStr += " 			from calendarioAberturaRequerimento caledarioReq ";
			sqlStr += " 			inner join calendarioAberturaTipoRequerimentoraPrazo calendarioPrazo on calendarioPrazo.calendarioAberturaRequerimento = caledarioReq.codigo ";
			sqlStr += "  			where TipoRequerimento.codigo = calendarioPrazo.tiporequerimento ";
			if (unidadeensino != 0) {
				sqlStr += " and (caledarioReq.unidadeensino =  " + unidadeensino + " or caledarioReq.unidadeensino is null) "  ;
			}
			sqlStr += "   			and '" +Uteis.getDataJDBC(new Date()) + "' between datainicio and datafim ";
			sqlStr += "  		) ";
			sqlStr += "  		or not exists ( ";
			sqlStr += " 			select calendarioAberturaTipoRequerimentoraPrazo.tiporequerimento ";
			sqlStr += " 			from calendarioAberturaTipoRequerimentoraPrazo where TipoRequerimento.codigo = calendarioAberturaTipoRequerimentoraPrazo.tiporequerimento and calendarioAberturaTipoRequerimentoraPrazo.datainicio is not null ";
			sqlStr += "  		) ";
			sqlStr += "    	) ";
		}
		
		if (permitirUsuarioConsultarIncluirApenasRequerimentosProprios) {
			sqlStr += " and (TipoRequerimento.requerimentovisaofuncionario) ";
		}
		
        sqlStr += " Order by nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        List<TipoRequerimentoVO> vetResultado = new ArrayList<>(0);
        while (tabelaResultado.next()) {
            TipoRequerimentoVO obj = new TipoRequerimentoVO();
            obj.setCodigo((tabelaResultado.getInt("codigo")));
            obj.setNome(tabelaResultado.getString("nome"));
            vetResultado.add(obj);
        }
        return vetResultado;
    }
    
    
    public List<TipoRequerimentoVO> consultarTipoRequerimentoTCCComboBox(boolean controlarAcesso, String situacao, Integer unidadeensino, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT distinct TipoRequerimento.codigo, nome FROM TipoRequerimento ";
        sqlStr += " left join tiporequerimentounidadeensino tu on tu.tiporequerimento = tiporequerimento.codigo ";
        sqlStr += " where situacao = '" + situacao + "' and tipo = 'TC' ";
        if (unidadeensino != 0) {
            sqlStr += " and (unidadeensinoespecifico = false or unidadeensinoespecifico is null or tu.unidadeensino = " + unidadeensino + ")";
        }
        
        sqlStr += " Order by nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        List<TipoRequerimentoVO> vetResultado = new ArrayList<>(0);
        while (tabelaResultado.next()) {
            TipoRequerimentoVO obj = new TipoRequerimentoVO();
            obj.setCodigo((tabelaResultado.getInt("codigo")));
            obj.setNome(tabelaResultado.getString("nome"));
            vetResultado.add(obj);
        }
        return vetResultado;
    }

    public List<TipoRequerimentoVO> consultarPorPermissaoVisaoAluno(Boolean visaoAluno, String situacao, Integer unidadeEnsino, Integer curso, String matricula, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT DISTINCT TipoRequerimento.*, (select taxavalor.valor from taxavalor where taxa = TipoRequerimento.taxa and taxavalor.data <= current_date order by taxavalor.data desc limit 1 ) as valor FROM TipoRequerimento ");
        sb.append(" left join tiporequerimentounidadeensino tu on tu.tiporequerimento = tiporequerimento.codigo ");
        sb.append(" WHERE requerimentoVisaoAluno = ").append(visaoAluno).append(" and situacao = '").append(situacao).append("' ");
        sb.append(" and (unidadeensinoespecifico = false or unidadeensinoespecifico is null or tu.unidadeensino = ").append(unidadeEnsino).append(") ");
        
        sb.append(" and ((");
        sb.append(" select tiporequerimentocurso.codigo from tiporequerimentocurso ");
        sb.append(" where tiporequerimentocurso.tiporequerimento = TipoRequerimento.codigo limit 1 ) is null or ");
        sb.append(" (( select tiporequerimentocurso.codigo from tiporequerimentocurso inner join curso on curso.codigo = tiporequerimentocurso.curso");
        sb.append(" where tiporequerimentocurso.tiporequerimento = TipoRequerimento.codigo ");
        sb.append(" and tiporequerimentocurso.curso = ").append(curso);
        sb.append(" and case when (");
        sb.append(" select tipoRequerimentoTurma.tipoRequerimentoCurso from tipoRequerimentoTurma ");
        sb.append(" where tipoRequerimentoTurma.tipoRequerimentoCurso = tipoRequerimentoCurso.codigo limit 1 ");
        sb.append(" ) is null then true else ");
        sb.append(" (select tipoRequerimentoTurma.tipoRequerimentoCurso from tipoRequerimentoTurma ");
        sb.append(" where tipoRequerimentoTurma.tipoRequerimentoCurso = tipoRequerimentoCurso.codigo ");
        sb.append(" and tipoRequerimentoTurma.turma = (");
        sb.append(" select matriculaperiodo.turma from matriculaperiodo ");
        sb.append(" where matriculaperiodo.matricula = '").append(matricula).append("' ");
        sb.append(" and case when curso.periodicidade = 'SE' then matriculaperiodo.ano = '").append(Uteis.getAnoDataAtual4Digitos()).append("' ").append(" and matriculaperiodo.semestre = '").append(Uteis.getSemestreAtual()).append("' ");
        sb.append(" else case when curso.periodicidade = 'AN' then matriculaperiodo.ano = '").append(Uteis.getAnoDataAtual4Digitos()).append("' else true ");
        sb.append(" end end order by (matriculaperiodo.ano || matriculaperiodo.semestre) desc, matriculaperiodo.codigo desc limit 1 ");
        sb.append(") ) > 0 end limit 1 ) is not null) )");
        sb.append(" and (	");
        sb.append(" 		exists ( ");
        sb.append(" 			select ");
        sb.append(" 				calendarioPrazo.tiporequerimento ");
        sb.append(" 			from calendarioAberturaRequerimento caledarioReq ");
        sb.append(" 			inner join calendarioAberturaTipoRequerimentoraPrazo calendarioPrazo on calendarioPrazo.calendarioAberturaRequerimento = caledarioReq.codigo ");
        sb.append("  			where TipoRequerimento.codigo = calendarioPrazo.tiporequerimento ");
		if (unidadeEnsino != 0) {
			sb.append(" and (caledarioReq.unidadeensino =  ").append(unidadeEnsino).append(" or caledarioReq.unidadeensino is null) ");
		}
		sb.append("   			and '" +Uteis.getDataJDBCTimestamp(new Date()) + "' between datainicio and datafim ");
		sb.append("  		) ");
		sb.append("  		or not exists ( ");
		sb.append(" 			select calendarioAberturaTipoRequerimentoraPrazo.tiporequerimento ");
		sb.append(" 			from calendarioAberturaTipoRequerimentoraPrazo ");
		sb.append(" 			INNER JOIN calendarioAberturaRequerimento ON calendarioAberturaRequerimento.codigo = calendarioAberturaTipoRequerimentoraPrazo.calendarioAberturaRequerimento");
		sb.append("				where TipoRequerimento.codigo = calendarioAberturaTipoRequerimentoraPrazo.tiporequerimento and calendarioAberturaTipoRequerimentoraPrazo.datainicio is not null ");
		if (unidadeEnsino != 0) {
			sb.append(" 		AND (calendarioAberturaRequerimento.unidadeensino =  ").append(unidadeEnsino).append(" or calendarioAberturaRequerimento.unidadeensino is null) ");
		}
		sb.append("  		) ");
		sb.append("    	) ");
		sb.append(getSQLValidarNivelEducacionalTipoRequerimentoCursoAluno(curso));
        sb.append(" Order by nome ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }
    
    
    @Override
    public List<TipoRequerimentoVO> consultarPorPermissaoVisaoAlunoMinhasNotas(Boolean minhasNotas, String situacao, Integer unidadeEnsino, Integer curso, String matricula, Integer disciplina, Integer turma, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT DISTINCT TipoRequerimento.* , (select taxavalor.valor from taxavalor where taxa = TipoRequerimento.taxa and taxavalor.data <= current_date order by taxavalor.data desc limit 1 ) as valor FROM TipoRequerimento ";
        sqlStr += " left join tiporequerimentounidadeensino tu on tu.tiporequerimento = tiporequerimento.codigo ";
        sqlStr += " WHERE requerimentominhasnotas = " + minhasNotas + " and situacao = '" + situacao + "' ";
        sqlStr += " and TipoRequerimento.tipo = '"+TiposRequerimento.REPOSICAO.getValor()+"' ";
        sqlStr += " and (unidadeensinoespecifico = false or unidadeensinoespecifico is null or tu.unidadeensino = " + unidadeEnsino + ")";
        sqlStr += " and (TipoRequerimento.requerimentoVisaoAluno or TipoRequerimento.requerimentoVisaoPai)  ";
        
        sqlStr += " and ((";
        sqlStr += " select tiporequerimentocurso.codigo from tiporequerimentocurso ";
        sqlStr += " where tiporequerimentocurso.tiporequerimento = TipoRequerimento.codigo limit 1 ) is null or ";
        sqlStr += " (( select tiporequerimentocurso.codigo from tiporequerimentocurso inner join curso on curso.codigo = tiporequerimentocurso.curso";
        sqlStr += " where tiporequerimentocurso.tiporequerimento = TipoRequerimento.codigo ";
        sqlStr += " and tiporequerimentocurso.curso = " + curso;
        sqlStr += " and case when (";
        sqlStr += " select tipoRequerimentoTurma.tipoRequerimentoCurso from tipoRequerimentoTurma ";
        sqlStr += " where tipoRequerimentoTurma.tipoRequerimentoCurso = tipoRequerimentoCurso.codigo limit 1 ";
        sqlStr += " ) is null then true else ";
        sqlStr += " (select tipoRequerimentoTurma.tipoRequerimentoCurso from tipoRequerimentoTurma ";
        sqlStr += " where tipoRequerimentoTurma.tipoRequerimentoCurso = tipoRequerimentoCurso.codigo ";
        sqlStr += " and tipoRequerimentoTurma.turma = (";
        sqlStr += " select matriculaperiodo.turma from matriculaperiodo ";
        sqlStr += " where matriculaperiodo.matricula = '" + matricula + "' ";
        sqlStr += " and case when curso.periodicidade = 'SE' then matriculaperiodo.ano = '"+Uteis.getAnoDataAtual4Digitos()+"' and matriculaperiodo.semestre = '" + Uteis.getSemestreAtual() + "' ";
        sqlStr += " else case when curso.periodicidade = 'AN' then matriculaperiodo.ano = '"+Uteis.getAnoDataAtual4Digitos()+"' else true ";
        sqlStr += " end end order by (matriculaperiodo.ano || matriculaperiodo.semestre) desc, matriculaperiodo.codigo desc limit 1 ";
        sqlStr += ") ) > 0 end limit 1 ) is not null) )";


        sqlStr += " and (	";
		sqlStr += " 		exists ( ";
		sqlStr += " 			select ";
		sqlStr += " 				calendarioPrazo.tiporequerimento ";
		sqlStr += " 			from calendarioAberturaRequerimento caledarioReq ";
		sqlStr += " 			inner join calendarioAberturaTipoRequerimentoraPrazo calendarioPrazo on calendarioPrazo.calendarioAberturaRequerimento = caledarioReq.codigo ";
		sqlStr += "  			where TipoRequerimento.codigo = calendarioPrazo.tiporequerimento ";
		if (unidadeEnsino != 0) {
			sqlStr += " and (caledarioReq.unidadeensino =  " + unidadeEnsino + " or caledarioReq.unidadeensino is null)" ;
		}
		sqlStr += "   			and '" +Uteis.getDataJDBC(new Date()) + "' between datainicio and datafim ";
		sqlStr += "  		) ";
		sqlStr += "  		or not exists ( ";
		sqlStr += " 			select calendarioAberturaTipoRequerimentoraPrazo.tiporequerimento ";
		sqlStr += " 			from calendarioAberturaTipoRequerimentoraPrazo where TipoRequerimento.codigo = calendarioAberturaTipoRequerimentoraPrazo.tiporequerimento and calendarioAberturaTipoRequerimentoraPrazo.datainicio is not null ";
		sqlStr += "  		) ";
		sqlStr += "    	) ";
		sqlStr += getSQLValidarNivelEducacionalTipoRequerimentoCursoAluno(curso);
		// Esta regra foi removida pois existem outras regras para controle de vaga de reposição
//        if (turma != null && disciplina != null) {
//        	sqlStr += " and (select case when ( select count(o) from (select matricula, turma, disciplina from requerimento  ";	
//        	sqlStr += " inner join turma on turma.codigo = requerimento.turma where tiporequerimento = tiporequerimento.codigo ";
//        	sqlStr += " and requerimento.turma = " + turma;
//        	sqlStr += " and requerimento.disciplina = " + disciplina;
//        	sqlStr += " group by matricula, turma, disciplina) as o ";
//        	sqlStr +=  ") < case when nrvagasinclusaoreposicao is null or nrvagasinclusaoreposicao = 0 then 1000 else nrvagasinclusaoreposicao end then true else false end ";
//        	sqlStr += " from turma where codigo = " + turma;
//        	sqlStr += " ) = true ";
//        }
        sqlStr += " Order by nome";            
        
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado,  nivelMontarDados, usuario));
    }

    public List<TipoRequerimentoVO> consultarPorPermissaoVisaoPais(Boolean visaoPai, Integer unidadeEnsino, String matricula, Integer curso, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT DISTINCT TipoRequerimento.* , (select taxavalor.valor from taxavalor where taxa = TipoRequerimento.taxa and taxavalor.data <= current_date order by taxavalor.data desc limit 1 ) as valor FROM TipoRequerimento ";
        sqlStr += " left join tiporequerimentounidadeensino tu on tu.tiporequerimento = tiporequerimento.codigo ";
        sqlStr += " WHERE requerimentoVisaoPai = " + visaoPai + " ";
        sqlStr += " and (unidadeensinoespecifico = false or unidadeensinoespecifico is null or tu.unidadeensino = " + unidadeEnsino + ")";
        
        sqlStr += " and ((";
        sqlStr += " select tiporequerimentocurso.codigo from tiporequerimentocurso ";
        sqlStr += " where tiporequerimentocurso.tiporequerimento = TipoRequerimento.codigo limit 1 ) is null or ";
        sqlStr += " (( select tiporequerimentocurso.codigo from tiporequerimentocurso inner join curso on curso.codigo = tiporequerimentocurso.curso";
        sqlStr += " where tiporequerimentocurso.tiporequerimento = TipoRequerimento.codigo ";
        sqlStr += " and tiporequerimentocurso.curso = " + curso;
        sqlStr += " and case when (";
        sqlStr += " select tipoRequerimentoTurma.tipoRequerimentoCurso from tipoRequerimentoTurma ";
        sqlStr += " where tipoRequerimentoTurma.tipoRequerimentoCurso = tipoRequerimentoCurso.codigo limit 1 ";
        sqlStr += " ) is null then true else ";
        sqlStr += " (select tipoRequerimentoTurma.tipoRequerimentoCurso from tipoRequerimentoTurma ";
        sqlStr += " where tipoRequerimentoTurma.tipoRequerimentoCurso = tipoRequerimentoCurso.codigo ";
        sqlStr += " and tipoRequerimentoTurma.turma = (";
        sqlStr += " select matriculaperiodo.turma from matriculaperiodo ";
        sqlStr += " where matriculaperiodo.matricula = '" + matricula + "' ";
        sqlStr += " and case when curso.periodicidade = 'SE' then matriculaperiodo.ano = '"+Uteis.getAnoDataAtual4Digitos()+"' and matriculaperiodo.semestre = '" + Uteis.getSemestreAtual() + "' ";
        sqlStr += " else case when curso.periodicidade = 'AN' then matriculaperiodo.ano = '"+Uteis.getAnoDataAtual4Digitos()+"' else true ";
        sqlStr += " end end order by (matriculaperiodo.ano || matriculaperiodo.semestre) desc, matriculaperiodo.codigo desc limit 1 ";
        sqlStr += ") ) > 0 end limit 1 ) is not null) )";


        sqlStr += " and (	";
		sqlStr += " 		exists ( ";
		sqlStr += " 			select ";
		sqlStr += " 				calendarioPrazo.tiporequerimento ";
		sqlStr += " 			from calendarioAberturaRequerimento caledarioReq ";
		sqlStr += " 			inner join calendarioAberturaTipoRequerimentoraPrazo calendarioPrazo on calendarioPrazo.calendarioAberturaRequerimento = caledarioReq.codigo ";
		sqlStr += "  			where TipoRequerimento.codigo = calendarioPrazo.tiporequerimento ";
		if (unidadeEnsino != 0) {
			sqlStr += " and (caledarioReq.unidadeensino =  " + unidadeEnsino+" or caledarioReq.unidadeensino is null) " ;
		}
		sqlStr += "   			and '" +Uteis.getDataJDBC(new Date()) + "' between datainicio and datafim ";
		sqlStr += "  		) ";
		sqlStr += "  		or not exists ( ";
		sqlStr += " 			select calendarioAberturaTipoRequerimentoraPrazo.tiporequerimento ";
		sqlStr += " 			from calendarioAberturaTipoRequerimentoraPrazo where TipoRequerimento.codigo = calendarioAberturaTipoRequerimentoraPrazo.tiporequerimento and calendarioAberturaTipoRequerimentoraPrazo.datainicio is not null ";
		sqlStr += "  		) ";
		sqlStr += "    	) ";
		sqlStr += getSQLValidarNivelEducacionalTipoRequerimentoCursoAluno(curso);        
        sqlStr += " Order by nome";        
        
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List<TipoRequerimentoVO> consultarPorPermissaoVisaoProfessor(Boolean visaoProfessor,Integer unidadeEnsino, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT DISTINCT TipoRequerimento.* , (select taxavalor.valor from taxavalor where taxa = TipoRequerimento.taxa and taxavalor.data <= current_date order by taxavalor.data desc limit 1 ) as valor FROM TipoRequerimento ";
        sqlStr += " left join tiporequerimentounidadeensino tu on tu.tiporequerimento = tiporequerimento.codigo ";
        sqlStr += " WHERE requerimentoVisaoProfessor = " + visaoProfessor + "";
        sqlStr += " and (unidadeensinoespecifico = false or unidadeensinoespecifico is null or tu.unidadeensino = " + unidadeEnsino + ")";
        sqlStr += " Order by nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List<TipoRequerimentoVO> consultarPorPermissaoVisaoCoordenador(Boolean visaoCoordenador, Integer unidadeEnsino, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT DISTINCT TipoRequerimento.* , (select taxavalor.valor from taxavalor where taxa = TipoRequerimento.taxa and taxavalor.data <= current_date order by taxavalor.data desc limit 1 ) as valor FROM TipoRequerimento ";
        sqlStr += " left join tiporequerimentounidadeensino tu on tu.tiporequerimento = tiporequerimento.codigo ";
        sqlStr += " WHERE requerimentoVisaoCoordenador = " + visaoCoordenador + "";
        sqlStr += " and (unidadeensinoespecifico = false or unidadeensinoespecifico is null or tu.unidadeensino = " + unidadeEnsino + ")";
        sqlStr += " Order by nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List<TipoRequerimentoVO> consultarPorTipo(String valorConsulta, int nivelMontarDados, boolean controlarAcesso,  UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * , (select taxavalor.valor from taxavalor where taxa = TipoRequerimento.taxa and taxavalor.data <= current_date order by taxavalor.data desc limit 1 ) as valor FROM TipoRequerimento WHERE tipo ilike('" + valorConsulta + "%') ORDER BY nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>TipoRequerimento</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>TipoRequerimentoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<TipoRequerimentoVO> consultarPorCodigo(Integer valorConsulta, String situacao, Integer unidadeEnsino, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder(" SELECT * , (select taxavalor.valor from taxavalor where taxa = TipoRequerimento.taxa and taxavalor.data <= current_date order by taxavalor.data desc limit 1 ) as valor ");
        sqlStr.append(" FROM TipoRequerimento WHERE codigo >= ").append(valorConsulta.intValue()).append(" ");
        if(situacao != null && !situacao.equals("") && !situacao.equals("TO")) {
        	sqlStr.append(" and TipoRequerimento.situacao = '").append(situacao).append("' ");	
        }
        if(Uteis.isAtributoPreenchido(unidadeEnsino)) {
        	sqlStr.append(" and (TipoRequerimento.unidadeEnsinoEspecifico = false or ");
        	sqlStr.append(" ( unidadeEnsinoEspecifico = true and exists (select tipoRequerimentoUnidadeEnsino.codigo from tipoRequerimentoUnidadeEnsino ");
        	sqlStr.append(" where tipoRequerimentoUnidadeEnsino.tipoRequerimento = tipoRequerimento.codigo and tipoRequerimentoUnidadeEnsino.unidadeensino = ").append(unidadeEnsino).append(" )))");
        }
        sqlStr.append(" ORDER BY codigo");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>TipoRequerimentoVO</code> resultantes da consulta.
     */
    public static List<TipoRequerimentoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List<TipoRequerimentoVO> vetResultado = new ArrayList<>(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>TipoRequerimentoVO</code>.
     * @return  O objeto da classe <code>TipoRequerimentoVO</code> com os dados devidamente montados.
     */
    public static TipoRequerimentoVO montarDados(SqlRowSet dadosSQL,  int nivelMontarDados, UsuarioVO usuario) throws Exception {
        TipoRequerimentoVO obj = new TipoRequerimentoVO();
        obj.setNovoObj(Boolean.FALSE);
        obj.setCodigo((dadosSQL.getInt("codigo")));
        obj.setNome(dadosSQL.getString("nome"));
		if(dadosSQL.getString("nivelEducacional") != null) {
			obj.setNivelEducacional(dadosSQL.getString("nivelEducacional"));
		}
		obj.setCobrarTaxaSomenteCertificadoImpresso(dadosSQL.getBoolean("cobrarTaxaSomenteCertificadoImpresso"));
		obj.setMensagemEmissaoCertificadoImpresso(dadosSQL.getString("mensagemEmissaoCertificadoImpresso"));
		obj.setSituacao(dadosSQL.getString("situacao"));
		obj.setEnviarNotificacaoRequerente(dadosSQL.getBoolean("enviarNotificacaoRequerente"));
		obj.setCampoAfastamento(dadosSQL.getBoolean("campoafastamento"));
        if(nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
        	return obj;
        }
        obj.setMensagemAlerta(dadosSQL.getString("mensagemAlerta"));
        obj.setValor((dadosSQL.getDouble("valor")));
        obj.setPrazoExecucao((dadosSQL.getInt("prazoExecucao")));
        obj.getDepartamentoResponsavel().setCodigo((dadosSQL.getInt("departamentoResponsavel")));
        obj.getQuestionario().setCodigo((dadosSQL.getInt("questionario")));
        obj.getCentroReceitaRequerimentoPadrao().setCodigo((dadosSQL.getInt("centroReceitaRequerimentoPadrao")));
        obj.getTextoPadrao().setCodigo((dadosSQL.getInt("textoPadrao")));
        obj.getTextoPadraoVO().setCodigo(dadosSQL.getInt("textopadraocontratoestagio"));
        obj.setOrientacao(dadosSQL.getString("orientacao"));
        obj.setTipo(dadosSQL.getString("tipo"));
        obj.setRequerimentoVisaoAluno(dadosSQL.getBoolean("requerimentoVisaoAluno"));
        obj.setRequerimentoVisaoFuncionario(dadosSQL.getBoolean("requerimentoVisaoFuncionario"));
        obj.setRequerimentoSituacaoFinanceiroVisaoAluno(dadosSQL.getBoolean("requerimentoSituacaoFinanceiroVisaoAluno"));
        obj.setRequerimentoVisaoPai(dadosSQL.getBoolean("requerimentoVisaoPai"));
        obj.setRequerimentoVisaoProfessor(dadosSQL.getBoolean("requerimentoVisaoProfessor"));
        obj.setRequerimentoVisaoCoordenador(dadosSQL.getBoolean("requerimentoVisaoCoordenador"));
        obj.setRequerimentoMembroComunidade(dadosSQL.getBoolean("requerimentoMembroComunidade"));
        obj.setConsiderarSegundaViaIndependenteSituacaoPrimeiraVia(dadosSQL.getBoolean("considerarSegundaViaIndependenteSituacaoPrimeiraVia"));
        obj.setHaDocumentoParaRetirada(dadosSQL.getBoolean("haDocumentoParaRetirada"));        
        obj.setPermitirUploadArquivo(dadosSQL.getBoolean("permitirUploadArquivo"));
        obj.setPermitirInformarEnderecoEntrega(dadosSQL.getBoolean("permitirInformarEnderecoEntrega"));
        obj.setQtdDiasVencimentoRequerimento(dadosSQL.getInt("qtdDiasVencimentoRequerimento"));
        obj.setDiasParaExclusaoRequerimentoDefazados(dadosSQL.getInt("diasParaExclusaoRequerimentoDefazados"));
        obj.setUnidadeEnsinoEspecifica(dadosSQL.getBoolean("unidadeEnsinoEspecifico"));
        obj.setTramitaEntreDepartamentos(dadosSQL.getBoolean("tramitaEntreDepartamentos"));
        obj.setSituacaoMatriculaAtiva(dadosSQL.getBoolean("situacaoMatriculaAtiva"));
        obj.setRequerimentoVisaoAlunoApresImprimirDeclaracao(dadosSQL.getBoolean("requerimentoVisaoAlunoApresImprimirDeclaracao"));
		obj.setSituacaoMatriculaPreMatriculada(dadosSQL.getBoolean("situacaoMatriculaPreMatriculada"));
		obj.setSituacaoMatriculaCancelada(dadosSQL.getBoolean("situacaoMatriculaCancelada"));
		obj.setSituacaoMatriculaTrancada(dadosSQL.getBoolean("situacaoMatriculaTrancada"));
		obj.setSituacaoMatriculaAbandonada(dadosSQL.getBoolean("situacaoMatriculaAbandonada"));
		obj.setSituacaoMatriculaTransferida(dadosSQL.getBoolean("situacaoMatriculaTransferida"));
		obj.setSituacaoMatriculaFormada(dadosSQL.getBoolean("situacaoMatriculaFormada"));
		obj.setSituacaoMatriculaJubilado(dadosSQL.getBoolean("situacaoMatriculaJubilado"));
		obj.setSituacao(dadosSQL.getString("situacao"));
		obj.setVerificarPendenciaBiblioteca(dadosSQL.getBoolean("verificarPendenciaBiblioteca"));
		obj.setVerificarPendenciaFinanceira(dadosSQL.getBoolean("verificarPendenciaFinanceira"));
		obj.setVerificarPendenciaBibliotecaAtraso(dadosSQL.getBoolean("verificarPendenciaBibliotecaAtraso"));
		obj.setVerificarPendenciaFinanceiraAtraso(dadosSQL.getBoolean("verificarPendenciaFinanceiraAtraso"));
		obj.setVerificarPendenciaDocumentacao(dadosSQL.getBoolean("verificarPendenciaDocumentacao"));
		obj.setVerificarPendenciaEnade(dadosSQL.getBoolean("verificarPendenciaEnade"));
		obj.setVerificarPendenciaEstagio(dadosSQL.getBoolean("verificarPendenciaEstagio"));
		obj.setVerificarPendenciaAtividadeComplementar(dadosSQL.getBoolean("verificarPendenciaAtividadeComplementar"));
        obj.getTaxa().setCodigo(dadosSQL.getInt("taxa"));
        obj.setDesabilitarTipoRequerimento(dadosSQL.getBoolean("desabilitarTipoRequerimento"));
        obj.setMensagemDesabilitarTipoRequerimento(dadosSQL.getString("mensagemDesabilitarTipoRequerimento"));
        obj.setRequerimentoMinhasNotas(dadosSQL.getBoolean("requerimentoMinhasNotas"));
        obj.setSigla(dadosSQL.getString("sigla"));
        obj.setCobrarApartirVia(dadosSQL.getInt("cobrarApartirVia"));
        obj.setDeferirAutomaticamente(dadosSQL.getBoolean("deferirAutomaticamente"));
        obj.setQtdeDiasDisponivel(dadosSQL.getInt("qtdeDiasDisponivel"));
        obj.setQtdeDiasAposPrimeiraImpressao(dadosSQL.getInt("qtdeDiasAposPrimeiraImpressao"));
        obj.setBloquearRecebimentoCartaoCreditoOnline(dadosSQL.getBoolean("bloquearRecebimentoCartaoCreditoOnline"));
        obj.setAssinarDigitalmenteDeclaracoesGeradasNoRequerimento(dadosSQL.getBoolean("assinarDigitalmenteDeclaracoesGeradasNoRequerimento"));
        obj.setConsiderarDiasUteis(dadosSQL.getBoolean("considerardiasuteis"));
        obj.setVerificarPendenciaApenasMatriculaRequerimento(dadosSQL.getBoolean("verificarPendenciaApenasMatriculaRequerimento"));
        obj.setTipoUploadArquivo(TipoUploadArquivoEnum.valueOf(dadosSQL.getString("tipoUploadArquivo")));
        obj.setExtensaoArquivo(dadosSQL.getString("extensaoArquivo"));
        obj.setOrientacaoUploadArquivo(dadosSQL.getString("orientacaoUploadArquivo"));
        obj.setRequerAutorizacaoPagamento(dadosSQL.getBoolean("requerAutorizacaoPagamento"));
        obj.setPermiteDeferirAguardandoAutorizacaoPagamento(dadosSQL.getBoolean("permiteDeferirAguardandoAutorizacaoPagamento"));
        obj.setRealizarIsencaoTaxaReposicaoMatriculaAposDataAula(dadosSQL.getBoolean("realizarIsencaoTaxaReposicaoMatriculaAposDataAula"));
        obj.setQuantidadeDiasAntesAulaPermitirExclusaoRequerimentoReposicao(dadosSQL.getInt("quantidadeDiasAntesAulaPermitirExclusaoRequerimentoReposicao"));
        obj.setPermitirSolicitacaoIsencaoTaxaRequerimento(dadosSQL.getBoolean("permitirSolicitacaoIsencaoTaxaRequerimento"));
        obj.setSolicitarAnexoComprovanteIsencaoTaxaRequerimento(dadosSQL.getBoolean("solicitarAnexoComprovanteIsencaoTaxaRequerimento"));
        obj.setOrientacaoDocumentoComprovanteIsencaoTaxaRequerimento(dadosSQL.getString("orientacaoDocumentoComprovanteIsencaoTaxaRequerimento"));
        obj.setPermiteIncluirDisciplinaPorEquivalencia(dadosSQL.getBoolean("permiteIncluirDisciplinaPorEquivalencia"));
        obj.setPermiteIncluirReposicaoTurmaOutraUnidade(dadosSQL.getBoolean("permiteIncluirReposicaoTurmaOutraUnidade"));
        obj.setPermiteIncluirReposicaoTurmaOutroCurso(dadosSQL.getBoolean("permiteIncluirReposicaoTurmaOutroCurso"));
        obj.setPermitirReposicaoComChoqueHorario(dadosSQL.getBoolean("permitirReposicaoComChoqueHorario"));
        obj.setUsarCentroResultadoTurma(dadosSQL.getBoolean("usarCentroResultadoTurma"));
        obj.setUploadArquivoObrigatorio(dadosSQL.getBoolean("uploadArquivoObrigatorio"));
        obj.setBloquearQuantidadeRequerimentoAbertosSimultaneamente(dadosSQL.getBoolean("bloquearQuantidadeRequerimentoAbertosSimultaneamente"));
        obj.setQuantidadeLimiteRequerimentoAbertoSimultaneamente(dadosSQL.getInt("quantidadeLimiteRequerimentoAbertoSimultaneamente"));
        obj.setConsiderarBloqueioSimultaneoRequerimentoDeferido(dadosSQL.getBoolean("considerarBloqueioSimultaneoRequerimentoDeferido"));
        obj.setConsiderarBloqueioSimultaneoRequerimentoIndeferido(dadosSQL.getBoolean("considerarBloqueioSimultaneoRequerimentoIndeferido"));
        obj.setBloqueioSimultaneoPelo(dadosSQL.getString("bloqueioSimultaneoPelo"));
        obj.setPermitirImpressaoHistoricoVisaoAluno(dadosSQL.getBoolean("permitirImpressaoHistoricoVisaoAluno"));
        obj.setNivelEducacional(dadosSQL.getString("nivelEducacional"));
        obj.setLayoutHistoricoApresentar(dadosSQL.getString("layoutHistoricoApresentar"));
        obj.setAprovadoSituacaoHistorico(dadosSQL.getBoolean("aprovadoSituacaoHistorico"));
        obj.setReprovadoSituacaoHistorico(dadosSQL.getBoolean("reprovadoSituacaoHistorico"));
        obj.setTrancadoSituacaoHistorico(dadosSQL.getBoolean("trancadoSituacaoHistorico"));
        obj.setCursandoSituacaoHistorico(dadosSQL.getBoolean("cursandoSituacaoHistorico"));
        obj.setAbandonoCursoSituacaoHistorico(dadosSQL.getBoolean("abandonoCursoSituacaoHistorico"));
        obj.setTransferidoSituacaoHistorico(dadosSQL.getBoolean("transferidoSituacaoHistorico"));
        obj.setCanceladoSituacaoHistorico(dadosSQL.getBoolean("canceladoSituacaoHistorico"));
        obj.setAssinarDigitalmenteHistorico(dadosSQL.getBoolean("assinarDigitalmenteHistorico"));
        obj.setPermitirAlunoRejeitarDocumento(dadosSQL.getBoolean("permitirAlunoRejeitarDocumento"));
        obj.setOcultarUnidadeEnsinoListaTurmaReposicao(dadosSQL.getBoolean("ocultarunidadeensinolistaturmareposicao"));
        obj.setPercentualMinimoCargaHorariaAproveitamento(dadosSQL.getDouble("percentualMinimoCargaHorariaAproveitamento"));
		obj.setQtdeMinimaDeAnosAproveitamento(dadosSQL.getInt("qtdeMinimaDeAnosAproveitamento"));
		obj.setMsgBloqueioNovaSolicitacaoAproveitamento(dadosSQL.getString("msgBloqueioNovaSolicitacaoAproveitamento"));
		obj.setQtdeMaximaIndeferidoAproveitamento(dadosSQL.getInt("qtdeMaximaIndeferidoAproveitamento"));
		obj.setPercentualIntegralizacaoCurricularInicial(dadosSQL.getInt("percentualIntegralizacaoCurricularInicial"));
		obj.setPercentualIntegralizacaoCurricularFinal(dadosSQL.getInt("percentualIntegralizacaoCurricularFinal"));
		obj.setRegistrarAproveitamentoDisciplinaTCC(dadosSQL.getBoolean("registrarAproveitamentoDisciplinaTCC"));
		obj.setRegistrarTrancamentoProximoSemestre(dadosSQL.getBoolean("registrarTrancamentoProximoSemestre"));
		obj.setRegistrarTransferenciaProximoSemestre(dadosSQL.getBoolean("registrarTransferenciaProximoSemestre"));
		obj.setPermitirAlunoAlterarCurso(dadosSQL.getBoolean("permitirAlunoAlterarCurso"));
		obj.setValidarVagasPorNumeroComputadoresUnidadeEnsino(dadosSQL.getBoolean("validarVagasPorNumeroComputadoresUnidadeEnsino"));
		obj.setValidarVagasPorNumeroComputadoresConsiderandoCurso(dadosSQL.getBoolean("validarVagasPorNumeroComputadoresConsiderandoCurso"));
		obj.setCidDeferirAutomaticamente(dadosSQL.getString("cidDeferirAutomaticamente"));
		obj.setAno(dadosSQL.getString("ano"));
		obj.setSemestre(dadosSQL.getString("semestre"));
		obj.setPermitirAproveitarDisciplinaCursando(dadosSQL.getBoolean("permitiraproveitardisciplinascursando"));
        
        if(dadosSQL.getString("tipoControleCobrancaViaRequerimento") != null){
        	obj.setTipoControleCobrancaViaRequerimento(TipoControleCobrancaViaRequerimentoEnum.valueOf(dadosSQL.getString("tipoControleCobrancaViaRequerimento")));
        }
		if(dadosSQL.getString("orientacaoAtendente") != null) {
			obj.setOrientacaoAtendente(dadosSQL.getString("orientacaoAtendente"));
		}
		
		if(dadosSQL.getString("certificadoImpresso") != null) {
			obj.getCertificadoImpresso().setCodigo(dadosSQL.getInt("certificadoImpresso"));
		}

		obj.setApenasParaAlunosComTodasAulasRegistradas(dadosSQL.getBoolean("apenasParaAlunosComTodasAulasRegistradas"));
		obj.setVerificarAlunoPossuiPeloMenosUmaDisciplinaAprovada(dadosSQL.getBoolean("verificarAlunoPossuiPeloMenosUmaDisciplinaAprovada"));
		obj.setRegistrarFormaturaAoRealizarImpressaoCerticadoDigital(dadosSQL.getBoolean("registrarFormaturaAoRealizarImpressaoCerticadoDigital"));       
		obj.setPermitirAlterarDataPrevisaoConclusaoRequerimento(dadosSQL.getBoolean("permitirAlterarDataPrevisaoConclusaoRequerimento"));
	    obj.setAbrirOutroRequerimentoAoDeferirEsteTipoRequerimento(dadosSQL.getBoolean("abrirOutroRequerimentoAoDeferirEsteTipoRequerimento"));
	    obj.setQtdDiasCobrarTaxa(dadosSQL.getInt("qtdDiasCobrarTaxa"));
	    
	    obj.setValidarMatriculaIntegralizada(dadosSQL.getBoolean("validarMatriculaIntegralizada"));
	    
	    if(dadosSQL.getInt("tipoRequerimentoAbrirDeferimento") != 0) {
	    	obj.setTipoRequerimentoAbrirDeferimento(getFacadeFactory().getTipoRequerimentoFacade().consultarPorChavePrimaria(dadosSQL.getInt("tipoRequerimentoAbrirDeferimento"), Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
	    }
	    
	    obj.setValidarDebitoFinanceiroRequerimentoIsento(dadosSQL.getBoolean("validarDebitoFinanceiroRequerimentoIsento"));
	    obj.setPermitirReporDisciplinaComAulaProgramada(dadosSQL.getBoolean("permitirReporDisciplinaComAulaProgramada"));
	    obj.setValidarEntregaTccAluno(dadosSQL.getBoolean("validarEntregaTccAluno"));
	    obj.setDeferirAutomaticamenteDocumentoImpresso(dadosSQL.getBoolean("deferirAutomaticamenteDocumentoImpresso"));
	    obj.setDeferirAutomaticamenteTrancamento(dadosSQL.getBoolean("deferirAutomaticamenteTrancamento"));
	    obj.setPermitirAlunoAlterarUnidadeEnsino(dadosSQL.getBoolean("permitirAlunoAlterarUnidadeEnsino"));
	    obj.setValidarAnoSemestreIngresso(dadosSQL.getBoolean("validaranosemestreingresso"));
	    obj.setAnoIngresso(dadosSQL.getString("anoingresso"));
	    obj.setSemestreIngresso(dadosSQL.getString("semestreingresso"));
	    obj.setConsiderarTodasMatriculasAlunoValidacaoAberturaSimultanea(dadosSQL.getBoolean("considerarTodasMatriculasAlunoValidacaoAberturaSimultanea"));
	    if(Uteis.isColunaExistente(dadosSQL, "bimestre")) {
	    	obj.setBimestre(dadosSQL.getString("bimestre"));
	    }
	    if(Uteis.isColunaExistente(dadosSQL, "tipoNota")) {
	    	obj.setTipoNota(dadosSQL.getString("tipoNota"));
	    }
	    obj.setUtilizarMensagemDeferimentoExclusivo(dadosSQL.getBoolean("utilizarMensagemDeferimentoExclusivo"));
	    obj.setUtilizarMensagemIndeferimentoExclusivo(dadosSQL.getBoolean("utilizarMensagemIndeferimentoExclusivo"));
	    obj.setTipoAluno(Uteis.isAtributoPreenchido(dadosSQL.getString("tipoAluno")) ? TipoAlunoEnum.valueOf(dadosSQL.getString("tipoAluno")) : null);
        if(nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
        	return obj;
        }
        montarDadosTaxa(obj, usuario);
        montarDadosDepartamentoResponsavel(obj, usuario);
        obj.setUnidadeEnsinoEspecificaVOs(getFacadeFactory().getTipoRequerimentoUnidadeEnsinoFacade().consultarItemTipoRequerimentoUnidadeEnsinoPorTipoRequerimento(obj.getCodigo(), NivelMontarDados.TODOS, usuario));
        obj.setTipoRequerimentoDepartamentoVOs(getFacadeFactory().getTipoRequerimentoDepartamentoFacade().consultarPorCodigoTipoRequerimento(obj.getCodigo(), false, usuario));
		obj.setTipoRequerimentoCursoVOs(getFacadeFactory().getTipoRequerimentoCursoFacade().consultarTipoRequerimentoCursoPorTipoRequerimento(obj.getCodigo(), usuario));
		obj.setPendenciaTipoDocumentoTipoRequerimentoVOs(getFacadeFactory().getPendenciaTipoDocumentoTipoRequerimentoInterfaceFacade().consultarPorTipoDocumentoTipoRequerimento(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario));
		obj.setPersonalizacaoMensagemAutomaticaDeferimento(getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarMensagemAutomaticaDeferimentoRequerimento(obj.getCodigo(), usuario));
		obj.setPersonalizacaoMensagemAutomaticaIndeferimento(getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarMensagemAutomaticaIndeferimentoRequerimento(obj.getCodigo(), usuario));
		return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>DepartamentoVO</code> relacionado ao objeto <code>TipoRequerimentoVO</code>.
     * Faz uso da chave primária da classe <code>DepartamentoVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosDepartamentoResponsavel(TipoRequerimentoVO obj, UsuarioVO usuario) throws Exception {
        if (obj.getDepartamentoResponsavel().getCodigo().intValue() == 0) {
            obj.setDepartamentoResponsavel(new DepartamentoVO());
            return;
        }
        obj.setDepartamentoResponsavel(getFacadeFactory().getDepartamentoFacade().consultarPorChavePrimaria(obj.getDepartamentoResponsavel().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
    }
    
    public static void montarDadosTaxa(TipoRequerimentoVO obj, UsuarioVO usuario) throws Exception {
    	if (obj.getTaxa().getCodigo().intValue() > 0) {
    		obj.setTaxa(getFacadeFactory().getTaxaFacade().consultarPorChavePrimaria(obj.getTaxa().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
    	}
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>TipoRequerimentoVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public TipoRequerimentoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT *, (select taxavalor.valor from taxavalor where taxa = TipoRequerimento.taxa and taxavalor.data <= current_date order by taxavalor.data desc limit 1 ) as valor FROM TipoRequerimento WHERE codigo = ? ";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, codigoPrm);
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados (TIPO REQUERIMENTO).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return TipoRequerimento.idEntidade;
    }    
    
    @Override
    public List<TipoRequerimentoVO> consultarPorTipoRequerimento(String tipo, String situacao, Integer unidadeEnsino, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append(" SELECT TipoRequerimento.*, (select taxavalor.valor from taxavalor where taxa = TipoRequerimento.taxa and taxavalor.data <= current_date order by taxavalor.data desc limit 1 ) as valor FROM TipoRequerimento ");
        sqlStr.append(" LEFT JOIN tiporequerimentounidadeensino tu ON tu.tiporequerimento = tiporequerimento.codigo ");
        sqlStr.append(" WHERE tipo = '").append(tipo).append("' ");
        sqlStr.append(" AND situacao = '").append(situacao).append("' ");
        sqlStr.append(" AND (unidadeensinoespecifico = FALSE OR unidadeensinoespecifico IS NULL OR tu.unidadeensino =  ").append(unidadeEnsino).append(") ");
        sqlStr.append(" ORDER BY nome");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }
    
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void adicionarTipoRequerimentoCursoVOs(List<TipoRequerimentoCursoVO> tipoRequerimentoCursoVOs, TipoRequerimentoCursoVO obj) {
		if (obj.getCursoVO().getCodigo() == 0) {
			throw new StreamSeiException("O curso (Tipo Requerimento Curso) deve ser informado");
		}
		for (TipoRequerimentoCursoVO tipoRequerimentoCursoVO : tipoRequerimentoCursoVOs) {
			if (tipoRequerimentoCursoVO.getCursoVO().getCodigo().equals(obj.getCursoVO().getCodigo())) {
				throw new StreamSeiException("Este curso já esta na lista !");
			}

		}
		tipoRequerimentoCursoVOs.add(obj);
    }
   
    @Override
   public void removerTipoRequerimentoCursoVOs(List<TipoRequerimentoCursoVO> tipoRequerimentoCursoVOs, TipoRequerimentoCursoVO obj) {
   		for (TipoRequerimentoCursoVO tipoRequerimentoCursoVO : tipoRequerimentoCursoVOs) {
   			if (tipoRequerimentoCursoVO.getCursoVO().getCodigo().equals(obj.getCursoVO().getCodigo())) {
   				tipoRequerimentoCursoVOs.remove(tipoRequerimentoCursoVO);
   				return;
   			}
   		}
    }
    
    public static void validarDadosTipoRequerimentoCursoVO(TipoRequerimentoCursoVO obj) throws ConsistirException {
        if ((obj.getTipoRequerimento() == null) || (obj.getTipoRequerimento()== 0)) {
            throw new ConsistirException("O campo DEPARTAMENTO (TipoRequerimentoDepartamento) deve ser informado.");
        }
    }
    
    @Override
    public List<TipoRequerimentoVO> consultarTipoDoTipoRequerimentoComTextoPadraoComSituacaoAtiva() {
    	StringBuilder sb = new StringBuilder();
    	sb.append("select distinct tipo from tipoRequerimento where situacao = 'AT' and textopadrao is not null order by tipo ");
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
    	List<TipoRequerimentoVO> listaTipoRequerimentoVOs = new ArrayList<>(0);
    	while (tabelaResultado.next()) {
    		TipoRequerimentoVO obj = new TipoRequerimentoVO();
    		obj.setTipo(tabelaResultado.getString("tipo"));
    		listaTipoRequerimentoVOs.add(obj);
    	}
    	return listaTipoRequerimentoVOs;
    }
    
    @Override
    public List<TipoRequerimentoVO> consultarTipoRequerimentoPorAlunoFichaAluno(Integer aluno, UsuarioVO usuarioVO) {
    	StringBuilder sb = new StringBuilder();
    	sb.append("select distinct tipoRequerimento.codigo, tipoRequerimento.nome from tipoRequerimento ");
    	sb.append(" inner join requerimento on requerimento.tipoRequerimento = tipoRequerimento.codigo ");
    	sb.append(" inner join matricula on matricula.matricula = requerimento.matricula ");
    	sb.append(" where matricula.aluno = ").append(aluno);
    	sb.append(" order by tipoRequerimento.nome");
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
    	List<TipoRequerimentoVO> listaTipoRequerimentoVOs = new ArrayList<>(0);
    	while (tabelaResultado.next()) {
    		TipoRequerimentoVO obj = new TipoRequerimentoVO();
    		obj.setCodigo(tabelaResultado.getInt("codigo"));
    		obj.setNome(tabelaResultado.getString("nome"));
    		listaTipoRequerimentoVOs.add(obj);
    	}
    	return listaTipoRequerimentoVOs;
    }
    
    @Override
    public Boolean validarSeTipoRequerimentoUsaCentroResultadoTurma(Integer requeriemnto, UsuarioVO usuarioVO) {
    	StringBuilder sb = new StringBuilder();
    	sb.append("select tipoRequerimento.usarCentroResultadoTurma from tipoRequerimento ");
    	sb.append(" inner join requerimento on requerimento.tipoRequerimento = tipoRequerimento.codigo ");
    	sb.append(" where requerimento.codigo = ").append(requeriemnto);
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
    	if(tabelaResultado.next()){
    		return tabelaResultado.getBoolean("usarCentroResultadoTurma");
    	}
    	return false ;
    	
    }
    
    @Override
    public String consultarMsgBloqueioNovaSolicitacaoAproveitamento(Integer tipoRequeriemnto, UsuarioVO usuarioVO) {
    	StringBuilder sb = new StringBuilder();
    	sb.append("select tipoRequerimento.msgBloqueioNovaSolicitacaoAproveitamento from tipoRequerimento  where codigo = ? ");
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), tipoRequeriemnto);
    	if(tabelaResultado.next()){
    		return tabelaResultado.getString("msgBloqueioNovaSolicitacaoAproveitamento");
    	}
    	return "" ;
    	
    }
    
    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>TipoRequerimentoVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>TipoRequerimentoVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void realizarAtivacaoTipoRequerimento(final TipoRequerimentoVO obj, UsuarioVO usuarioVO) throws Exception {
        try {
            TipoRequerimentoVO.validarDados(obj);
            TipoRequerimento.alterar(getIdEntidade(), true, usuarioVO);
            final String sql = "UPDATE TipoRequerimento set situacao=? WHERE (codigo = ?)"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement sqlAlterar = con.prepareStatement(sql);
                    sqlAlterar.setString(1, obj.getSituacao());
                    sqlAlterar.setInt(2, obj.getCodigo().intValue());
                    return sqlAlterar;
}
            });
        } catch (Exception e) {
            throw e;
        }
    }
    
	private String getSQLValidarNivelEducacionalTipoRequerimentoCursoAluno(Integer curso) {
		return " AND ((COALESCE(TipoRequerimento.niveleducacional, '') <> '' AND TipoRequerimento.niveleducacional = (SELECT c.niveleducacional FROM curso c WHERE c.codigo = "
				+ curso + " LIMIT 1)) OR  (COALESCE(TipoRequerimento.niveleducacional, '') = '')) ";
	}
	
	@Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void adicionarCidTipoRequerimentoVOs(List<CidTipoRequerimentoVO> cidTipoRequerimentoVOs, CidTipoRequerimentoVO obj) {
		if (!Uteis.isAtributoPreenchido(obj.getCodCid()) || !Uteis.isAtributoPreenchido(obj.getDescricaoCid()) && (obj.getImportacaoExcel() && Uteis.isAtributoPreenchido(cidTipoRequerimentoVOs))) {
			throw new StreamSeiException("O Campo descrição CID e Código CID devem estar preenchidos.");
		}
		for (CidTipoRequerimentoVO CidTipoRequerimentoVO : cidTipoRequerimentoVOs) {
			if (CidTipoRequerimentoVO.getCodCid().equals(obj.getCodCid())) {
				throw new StreamSeiException("Este Código CID já esta na lista!");
			}
		}
		cidTipoRequerimentoVOs.add(obj);
    }
   
	@Override
	   public void removerCidTipoRequerimentoVOs(List<CidTipoRequerimentoVO> cidTipoRequerimentoVOs, CidTipoRequerimentoVO obj) {
	   		for (CidTipoRequerimentoVO cidTipoRequerimentoVO : cidTipoRequerimentoVOs) {
	   			if (cidTipoRequerimentoVO.getCodCid().equals(obj.getCodCid())) {
	   				cidTipoRequerimentoVOs.remove(cidTipoRequerimentoVO);
	   				return;
	   			}
	   		}
	    }
	
	 @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	    public void adicionarListaImportacaoPlanilhaCidTipoRequerimentoVOs(List<CidTipoRequerimentoVO> cidTipoRequerimentoVOs, CidTipoRequerimentoVO obj) {
			if (obj.getImportacaoExcel() && !Uteis.isAtributoPreenchido(cidTipoRequerimentoVOs)) {
				throw new StreamSeiException("A lista deve estar preenchidos.");
			}
//			for (CidTipoRequerimentoVO CidTipoRequerimentoVO : cidTipoRequerimentoVOs) {
//				if (Uteis.isAtributoPreenchido(CidTipoRequerimentoVO)  && CidTipoRequerimentoVO.getCodCid().equals(obj.getCodCid())) {
//					throw new StreamSeiException("Este Código CID já esta na lista!");
//				}
//			}
			cidTipoRequerimentoVOs.add(obj);
	    }
	 
	 private void verificarPersistirPersonalizacaoMensagemAutomaticaTipoRequerimento(TipoRequerimentoVO obj, UsuarioVO usuarioVO) throws Exception {
		 if (Uteis.isAtributoPreenchido(obj)) {
			 if (obj.getUtilizarMensagemDeferimentoExclusivo()) {
				 obj.getPersonalizacaoMensagemAutomaticaDeferimento().setTipoRequerimento(obj.getCodigo());
				 getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().persistir(obj.getPersonalizacaoMensagemAutomaticaDeferimento(), usuarioVO);
			 }
			 if (obj.getUtilizarMensagemIndeferimentoExclusivo()) {
				 obj.getPersonalizacaoMensagemAutomaticaIndeferimento().setTipoRequerimento(obj.getCodigo());
				 getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().persistir(obj.getPersonalizacaoMensagemAutomaticaIndeferimento(), usuarioVO);
			 }
		 }
	 }
}
