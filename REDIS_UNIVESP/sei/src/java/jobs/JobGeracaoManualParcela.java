package jobs;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import controle.arquitetura.AplicacaoControle;
import negocio.comuns.academico.CondicaoPagamentoPlanoFinanceiroCursoVO;
import negocio.comuns.academico.PlanoFinanceiroCursoVO;
import negocio.comuns.academico.ProcessoMatriculaCalendarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.GeracaoManualParcelaVO;
import negocio.comuns.financeiro.MatriculaPeriodoVencimentoVO;
import negocio.comuns.financeiro.enumerador.SituacaoProcessamentoGeracaoManualParcelaEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.SituacaoVencimentoMatriculaPeriodo;
import negocio.facade.jdbc.academico.MatriculaPeriodo;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

public class JobGeracaoManualParcela extends ControleAcesso implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5346990820071205093L;
	private GeracaoManualParcelaVO geracaoManualParcelaVO;
	private AplicacaoControle aplicacaoControle;

	public JobGeracaoManualParcela(GeracaoManualParcelaVO geracaoManualParcelaVO, AplicacaoControle aplicacaoControle) {
		super();
		this.geracaoManualParcelaVO = geracaoManualParcelaVO;
		this.aplicacaoControle = aplicacaoControle;
	}

	private void realizarGeracaoContaReceber(GeracaoManualParcelaVO geracaoManualParcelaVO, 
			ConfiguracaoFinanceiroVO configuracaoFinanceiro, 
			List<MatriculaPeriodoVencimentoVO> matriculaPeriodoVencimentoVOs,
			Map<ProcessoMatriculaCalendarioVO, ProcessoMatriculaCalendarioVO> mapProcessoMatriculaCalendarioVO,
			Map<Integer, PlanoFinanceiroCursoVO> mapPlanoFinanceiroCursoVO,
			Map<Integer, CondicaoPagamentoPlanoFinanceiroCursoVO> mapCondicaoPagamentoPlanoFinanceiroCursoVO) {
//		ProcessoMatriculaCalendarioVO processoMatriculaCalendarioVOCarregado = null;
//		PlanoFinanceiroCursoVO planoFinanceiroCursoVOCarregado = null;
//		CondicaoPagamentoPlanoFinanceiroCursoVO condicaoPagamentoPlanoFinanceiroCursoVOCarregado = null;
//		Integer unidadeEnsinoCursoCodigoAtual = null;
//		Map<Integer, ProcessoMatriculaCalendarioVO> mapProcessoMatriculaCalendarioVO = new HashMap<Integer, ProcessoMatriculaCalendarioVO>(0);
//		Map<Integer, PlanoFinanceiroCursoVO> mapPlanoFinanceiroCursoVO = new HashMap<Integer, PlanoFinanceiroCursoVO>(0);
//		Map<Integer, CondicaoPagamentoPlanoFinanceiroCursoVO> mapCondicaoPagamentoPlanoFinanceiroCursoVO = new HashMap<Integer, CondicaoPagamentoPlanoFinanceiroCursoVO>(0);
		for (MatriculaPeriodoVencimentoVO vcto : matriculaPeriodoVencimentoVOs) {
			try {
				// INICIALIZANDO A CONFIGURACAO FINANCEIRO, PARA EVITAR QUE ESTA
				// CONSULTA SEJA REALIZADA
				// NOVAMENTE, DE FORMA REPETIDA PARA CADA ALUNO.
				vcto.getMatriculaPeriodoVO().setConfiguracaoFinanceiro(configuracaoFinanceiro);
				vcto.getMatriculaPeriodoVO().getConfiguracaoFinanceiro().setNivelMontarDados(NivelMontarDados.TODOS);
				// INICIALIZANDO O PROCESSO MATRICULA, PARA GERACAO DA CONTA A
				// RECEBER
				// VALE RESSALTAR QUE A ROTINA TRABALHA COM O CONCEITO DE
				// ORDENAR OS VCTOS POR UNIDADEENSINOCURSO /
				// PROCESSOMATRICULA, DE FORMA QUE TODOS OS VCTO QUE
				// COMPARTILHAM ESTAS INFORMACOES SEJAM PROCESSADOS
				// CONJUNTAMENTE. EVITANDO ASSIM QUE ESTE DADO TENHA QUE SER
				// REPROCESSADO DIVERSAS VEZES				
				vcto.getMatriculaPeriodoVO().getProcessoMatriculaCalendarioVO().setProcessoMatricula(vcto.getMatriculaPeriodoVO().getProcessoMatricula());
				vcto.getMatriculaPeriodoVO().getProcessoMatriculaCalendarioVO().getCursoVO().setCodigo(vcto.getMatriculaPeriodoVO().getMatriculaVO().getCurso().getCodigo());
				vcto.getMatriculaPeriodoVO().getProcessoMatriculaCalendarioVO().getTurnoVO().setCodigo(vcto.getMatriculaPeriodoVO().getMatriculaVO().getTurno().getCodigo());
				if (!mapProcessoMatriculaCalendarioVO.containsKey(vcto.getMatriculaPeriodoVO().getProcessoMatriculaCalendarioVO())) {
					getFacadeFactory().getMatriculaPeriodoFacade().montarDadosProcessoMatriculaCalendarioVO(vcto.getMatriculaPeriodoVO().getMatriculaVO(), vcto.getMatriculaPeriodoVO(), NivelMontarDados.TODOS, geracaoManualParcelaVO.getUsuario());
					mapProcessoMatriculaCalendarioVO.put(vcto.getMatriculaPeriodoVO().getProcessoMatriculaCalendarioVO(), vcto.getMatriculaPeriodoVO().getProcessoMatriculaCalendarioVO());
					//processoMatriculaCalendarioVOCarregado = vcto.getMatriculaPeriodoVO().getProcessoMatriculaCalendarioVO();
					//unidadeEnsinoCursoCodigoAtual = vcto.getMatriculaPeriodoVO().getUnidadeEnsinoCursoVO().getCodigo();
				} else {
					vcto.getMatriculaPeriodoVO().setProcessoMatriculaCalendarioVO(mapProcessoMatriculaCalendarioVO.get(vcto.getMatriculaPeriodoVO().getProcessoMatriculaCalendarioVO()));
					vcto.getMatriculaPeriodoVO().getProcessoMatriculaCalendarioVO().setNivelMontarDados(NivelMontarDados.TODOS);
				}
				// INICIALIZANDO O PLANO FINANCEIRO DO CURSO;
				if (!mapPlanoFinanceiroCursoVO.containsKey(vcto.getMatriculaPeriodoVO().getPlanoFinanceiroCurso().getCodigo())) {				
					MatriculaPeriodo.montarDadosPlanoFinanceiroCurso(vcto.getMatriculaPeriodoVO(), NivelMontarDados.TODOS, geracaoManualParcelaVO.getUsuario());
					mapPlanoFinanceiroCursoVO.put(vcto.getMatriculaPeriodoVO().getPlanoFinanceiroCurso().getCodigo(), vcto.getMatriculaPeriodoVO().getPlanoFinanceiroCurso());					
				} else {
					vcto.getMatriculaPeriodoVO().setPlanoFinanceiroCurso(mapPlanoFinanceiroCursoVO.get(vcto.getMatriculaPeriodoVO().getPlanoFinanceiroCurso().getCodigo()));
					vcto.getMatriculaPeriodoVO().getPlanoFinanceiroCurso().setNivelMontarDados(NivelMontarDados.TODOS);
				}
				vcto.getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno().setPlanoFinanceiroCursoVO(mapPlanoFinanceiroCursoVO.get(vcto.getMatriculaPeriodoVO().getPlanoFinanceiroCurso().getCodigo()));
				// INICIALIZANDO CONDICAO PAGAMENTO PLANO FINANCEIRO CURSO
				if (!mapCondicaoPagamentoPlanoFinanceiroCursoVO.containsKey(vcto.getMatriculaPeriodoVO().getCondicaoPagamentoPlanoFinanceiroCurso().getCodigo())) {
					MatriculaPeriodo.montarDadosCondicaoPagamentoPlanoFinanceiroCurso(vcto.getMatriculaPeriodoVO(), NivelMontarDados.TODOS, geracaoManualParcelaVO.getUsuario());
					mapCondicaoPagamentoPlanoFinanceiroCursoVO.put(vcto.getMatriculaPeriodoVO().getCondicaoPagamentoPlanoFinanceiroCurso().getCodigo(), vcto.getMatriculaPeriodoVO().getCondicaoPagamentoPlanoFinanceiroCurso());
				} else {
					vcto.getMatriculaPeriodoVO().setCondicaoPagamentoPlanoFinanceiroCurso(mapCondicaoPagamentoPlanoFinanceiroCursoVO.get(vcto.getMatriculaPeriodoVO().getCondicaoPagamentoPlanoFinanceiroCurso().getCodigo()));
					vcto.getMatriculaPeriodoVO().getCondicaoPagamentoPlanoFinanceiroCurso().setNivelMontarDados(NivelMontarDados.TODOS);
				}
				vcto.getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno().setCondicaoPagamentoPlanoFinanceiroCursoVO(mapCondicaoPagamentoPlanoFinanceiroCursoVO.get(vcto.getMatriculaPeriodoVO().getCondicaoPagamentoPlanoFinanceiroCurso().getCodigo()));
				vcto.setGeracaoManualParcela(geracaoManualParcelaVO.getCodigo());
				vcto.getMatriculaPeriodoVO().getMatriculaPeriodoVencimentoVOs().add(vcto);
				//getFacadeFactory().getMatriculaPeriodoFacade().processarGeracaoContasReceberMesReferenciaEspecifica(vcto.getMatriculaPeriodoVO().getMatriculaVO(), vcto.getMatriculaPeriodoVO(), geracaoManualParcelaVO.getMesReferencia() + "/" + geracaoManualParcelaVO.getAnoReferencia(), configuracaoFinanceiro, geracaoManualParcelaVO.getUsuario());
				MatriculaPeriodoVencimentoVO mpvRegerar = new MatriculaPeriodoVencimentoVO();
	            mpvRegerar.setParcela(vcto.getParcela());
	            mpvRegerar.setTipoOrigemMatriculaPeriodoVencimento(vcto.getTipoOrigemMatriculaPeriodoVencimento());
	            mpvRegerar.setUsaValorPrimeiraParcela(vcto.isUsaValorPrimeiraParcela());
	            if (geracaoManualParcelaVO.getMesReferencia().equals("") && geracaoManualParcelaVO.getAnoReferencia().equals("")) {
	            	//Inicializo a parcela somente com Barra pois nos metodos a seguir sera usado para chegar no metodo gerarContasReceberComBaseParcelasMatriculaPeriodo
	            	mpvRegerar.setParcela("/");
            	} else if (mpvRegerar.getParcela().contains("/")) {
            		mpvRegerar.setParcela(mpvRegerar.getParcela().substring(0,mpvRegerar.getParcela().indexOf("/")));
            	}
				getFacadeFactory().getMatriculaPeriodoFacade().processarGeracaoContasReceberMesReferenciaEspecifica(vcto.getMatriculaPeriodoVO().getMatriculaVO(), vcto.getMatriculaPeriodoVO(), mpvRegerar, configuracaoFinanceiro, geracaoManualParcelaVO.getUsuario(), false);
				if(!vcto.getSituacao().equals(SituacaoVencimentoMatriculaPeriodo.CONTARECEBER_NAO_GERADA)){
					geracaoManualParcelaVO.setQtdeParcelaGerada(geracaoManualParcelaVO.getQtdeParcelaGerada() + 1);
					geracaoManualParcelaVO.setValorTotalParcelasGerada(geracaoManualParcelaVO.getValorTotalParcelasGerada() + vcto.getValor());
				}else{
					throw new Exception(UteisJSF.internacionalizar("msg_GaracaoManualParcela_naoGerada"));
				}				
			} catch (Exception e) {
				geracaoManualParcelaVO.setQtdeParcelaComErro(geracaoManualParcelaVO.getQtdeParcelaComErro() + 1);
				geracaoManualParcelaVO.setValorTotalParcelasNaoGerada(geracaoManualParcelaVO.getValorTotalParcelasNaoGerada() + vcto.getValor());
				try {
					getFacadeFactory().getMatriculaPeriodoVencimentoFacade().realizarRegistroErroGeracaoContaReceber(vcto.getCodigo(), geracaoManualParcelaVO.getCodigo(), e.getMessage());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			} finally {
				try {
					if (geracaoManualParcelaVO.getParcelaAtual() > geracaoManualParcelaVO.getQtdeParcelaGerar()) {
						geracaoManualParcelaVO.setQtdeParcelaGerar(geracaoManualParcelaVO.getParcelaAtual());
						geracaoManualParcelaVO.setValorTotalParcelasGerar(geracaoManualParcelaVO.getValorTotalParcelasGerada() + geracaoManualParcelaVO.getValorTotalParcelasNaoGerada());
					}
					getFacadeFactory().getGeracaoManualParcelaFacade().persistir(geracaoManualParcelaVO, geracaoManualParcelaVO.getUsuario());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void iniciarProcessamentoGeracaoManualParcela(GeracaoManualParcelaVO geracaoManualParcelaVO) {
		if (geracaoManualParcelaVO.getSituacaoProcessamentoGeracaoManualParcela().equals(SituacaoProcessamentoGeracaoManualParcelaEnum.AGUARDANDO_PROCESSAMENTO) || geracaoManualParcelaVO.getSituacaoProcessamentoGeracaoManualParcela().equals(SituacaoProcessamentoGeracaoManualParcelaEnum.ERRO_PROCESSAMENTO)) {
			geracaoManualParcelaVO.setDataInicioProcessamento(new Date());
			geracaoManualParcelaVO.setSituacaoProcessamentoGeracaoManualParcela(SituacaoProcessamentoGeracaoManualParcelaEnum.EM_PROCESSAMENTO);
		}
		try {			
			if (geracaoManualParcelaVO.getSituacaoProcessamentoGeracaoManualParcela().equals(SituacaoProcessamentoGeracaoManualParcelaEnum.AGUARDANDO_PROCESSAMENTO)){
				getFacadeFactory().getGeracaoManualParcelaFacade().persistir(geracaoManualParcelaVO, geracaoManualParcelaVO.getUsuario());
			}
			aplicacaoControle.realizarGeracaoManualParcela(geracaoManualParcelaVO.getUnidadeEnsino().getCodigo(), false, false);
		} catch (Exception e) {
			
		}
	}

	@Override
	public void run() {
		if (geracaoManualParcelaVO == null) {
			return;
		}
		iniciarProcessamentoGeracaoManualParcela(geracaoManualParcelaVO);
		if (geracaoManualParcelaVO.getCodigo() > 0) {
			ConfiguracaoFinanceiroVO configuracaoFinanceiro = null;
			Map<ProcessoMatriculaCalendarioVO, ProcessoMatriculaCalendarioVO> mapProcessoMatriculaCalendarioVO = new HashMap<ProcessoMatriculaCalendarioVO, ProcessoMatriculaCalendarioVO>(0);
			Map<Integer, PlanoFinanceiroCursoVO> mapPlanoFinanceiroCursoVO = new HashMap<Integer, PlanoFinanceiroCursoVO>(0);
			Map<Integer, CondicaoPagamentoPlanoFinanceiroCursoVO> mapCondicaoPagamentoPlanoFinanceiroCursoVO = new HashMap<Integer, CondicaoPagamentoPlanoFinanceiroCursoVO>(0);
			
			try {
				configuracaoFinanceiro = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_TODOS, geracaoManualParcelaVO.getUnidadeEnsino().getCodigo(), geracaoManualParcelaVO.getUsuario());
				List<MatriculaPeriodoVencimentoVO> matriculaPeriodoVencimentoVOs = getFacadeFactory().getMatriculaPeriodoVencimentoFacade().consultarPorMesReferenciaSituacaoUnidade(geracaoManualParcelaVO.getMesReferencia(), geracaoManualParcelaVO.getAnoReferencia(), "NG", geracaoManualParcelaVO.getUnidadeEnsino().getCodigo(), geracaoManualParcelaVO.getCurso().getCodigo(), geracaoManualParcelaVO.getTurma(), geracaoManualParcelaVO.getPermitirGerarParcelaAlunoPreMatricula(), geracaoManualParcelaVO.isTrazerMatriculasComCanceladoFinanceiro(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, configuracaoFinanceiro, geracaoManualParcelaVO.getUsuario(), 50, geracaoManualParcelaVO.getCodigo(), geracaoManualParcelaVO.getUtilizarDataCompetencia());
				Boolean processar = matriculaPeriodoVencimentoVOs.size() > 0;
				while (processar) {					
					realizarGeracaoContaReceber(geracaoManualParcelaVO, configuracaoFinanceiro, matriculaPeriodoVencimentoVOs, mapProcessoMatriculaCalendarioVO, mapPlanoFinanceiroCursoVO, mapCondicaoPagamentoPlanoFinanceiroCursoVO);
					matriculaPeriodoVencimentoVOs = getFacadeFactory().getMatriculaPeriodoVencimentoFacade().consultarPorMesReferenciaSituacaoUnidade(geracaoManualParcelaVO.getMesReferencia(), geracaoManualParcelaVO.getAnoReferencia(), "NG", geracaoManualParcelaVO.getUnidadeEnsino().getCodigo(), geracaoManualParcelaVO.getCurso().getCodigo(), geracaoManualParcelaVO.getTurma(), geracaoManualParcelaVO.getPermitirGerarParcelaAlunoPreMatricula(), geracaoManualParcelaVO.isTrazerMatriculasComCanceladoFinanceiro(),  Uteis.NIVELMONTARDADOS_DADOSBASICOS, configuracaoFinanceiro, geracaoManualParcelaVO.getUsuario(), 50, geracaoManualParcelaVO.getCodigo(), geracaoManualParcelaVO.getUtilizarDataCompetencia());
					processar = matriculaPeriodoVencimentoVOs.size() > 0;
					if(processar){
						Thread.sleep(60 * 1000);
					}
				}
				geracaoManualParcelaVO.setDataTerminoProcessamento(new Date());
				geracaoManualParcelaVO.setQtdeParcelaGerar(geracaoManualParcelaVO.getParcelaAtual());
				geracaoManualParcelaVO.setValorTotalParcelasGerar(geracaoManualParcelaVO.getValorTotalParcelasGerada()+geracaoManualParcelaVO.getValorTotalParcelasNaoGerada());
				geracaoManualParcelaVO.setSituacaoProcessamentoGeracaoManualParcela(SituacaoProcessamentoGeracaoManualParcelaEnum.PROCESSAMENTO_CONCLUIDO);
				getFacadeFactory().getGeracaoManualParcelaFacade().persistir(geracaoManualParcelaVO, geracaoManualParcelaVO.getUsuario());
			} catch (Exception e) {
				geracaoManualParcelaVO.setDataTerminoProcessamento(new Date());
				geracaoManualParcelaVO.setMensagemErro(e.getMessage());
				geracaoManualParcelaVO.setQtdeParcelaGerar(geracaoManualParcelaVO.getParcelaAtual());
				geracaoManualParcelaVO.setValorTotalParcelasGerar(geracaoManualParcelaVO.getValorTotalParcelasGerada()+geracaoManualParcelaVO.getValorTotalParcelasNaoGerada());
				geracaoManualParcelaVO.setSituacaoProcessamentoGeracaoManualParcela(SituacaoProcessamentoGeracaoManualParcelaEnum.ERRO_PROCESSAMENTO);
				if (geracaoManualParcelaVO.getCodigo() > 0) {
					try {
						getFacadeFactory().getGeracaoManualParcelaFacade().persistir(geracaoManualParcelaVO, geracaoManualParcelaVO.getUsuario());
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
				return;
			}finally{
				mapProcessoMatriculaCalendarioVO = null;
				mapPlanoFinanceiroCursoVO = null;
				mapCondicaoPagamentoPlanoFinanceiroCursoVO = null;
				configuracaoFinanceiro = null;
				try {
					aplicacaoControle.realizarGeracaoManualParcela(geracaoManualParcelaVO.getUnidadeEnsino().getCodigo(), false, true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

}
