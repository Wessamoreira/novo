package jobs;

import java.util.Date;
import java.util.List;

import org.springframework.jdbc.UncategorizedSQLException;

import controle.arquitetura.AplicacaoControle;
import negocio.comuns.academico.GradeDisciplinaCompostaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.RenovacaoMatriculaTurmaMatriculaPeriodoVO;
import negocio.comuns.academico.RenovacaoMatriculaTurmaVO;
import negocio.comuns.academico.enumeradores.SituacaoRenovacaoMatriculaPeriodoEnum;
import negocio.comuns.academico.enumeradores.SituacaoRenovacaoTurmaEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.arquitetura.SuperFacadeJDBC;

public class JobRenovacaoTurma extends SuperFacadeJDBC implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1273196558001359605L;
	private AplicacaoControle aplicacaoControle;
	private RenovacaoMatriculaTurmaVO renovacaoMatriculaTurma;
	private ConfiguracaoFinanceiroVO configuracaoFinanceiro = null;
	private ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = null;
	private List<GradeDisciplinaCompostaVO> gradeDisciplinaCompostaVOs;

	public JobRenovacaoTurma(AplicacaoControle aplicacaoControle, RenovacaoMatriculaTurmaVO renovacaoMatriculaTurma, List<GradeDisciplinaCompostaVO> gradeDisciplinaCompostaVOs) {
		super();
		this.aplicacaoControle = aplicacaoControle;
		this.renovacaoMatriculaTurma = renovacaoMatriculaTurma;
		this.gradeDisciplinaCompostaVOs = gradeDisciplinaCompostaVOs;
	}

	@Override
	public void run() {		
		boolean permitirRealizarMatriculaDisciplinaPreRequisito = false;
		try {
			getRenovacaoMatriculaTurma().setResponsavel(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(getUsuarioLogado().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			if(getRenovacaoMatriculaTurma().getSituacao().equals(SituacaoRenovacaoTurmaEnum.EM_PROCESSAMENTO)){
				try {
					ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("Matricula_PermitirRealizarMatriculaDisciplinaPreRequisito", getUsuarioLogado());
					permitirRealizarMatriculaDisciplinaPreRequisito = Boolean.TRUE;
				} catch(UncategorizedSQLException ex){
					throw ex;	
				} catch(InterruptedException ex){
					throw ex;	
				} catch (Exception e) {
					permitirRealizarMatriculaDisciplinaPreRequisito = false;
				}			
				setConfiguracaoFinanceiro(getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_TODOS, getRenovacaoMatriculaTurma().getTurmaVO().getUnidadeEnsino().getCodigo(), getRenovacaoMatriculaTurma().getResponsavel()));
				setConfiguracaoGeralSistemaVO(getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_TODOS, getRenovacaoMatriculaTurma().getResponsavel(), getRenovacaoMatriculaTurma().getTurmaVO().getUnidadeEnsino().getCodigo()));
				getFacadeFactory().getRenovacaoMatriculaTurmaMatriculaPeriodoFacade().realizarInicializacaoDadosPertinentesProcessamento(getRenovacaoMatriculaTurma(), getUsuarioLogado());
				if(Uteis.isAtributoPreenchido(getRenovacaoMatriculaTurma().getTurmaVO())) {
					executarRenovacaoMatriculaTurmaPorTurma(permitirRealizarMatriculaDisciplinaPreRequisito);
				}else {
					getFacadeFactory().getRenovacaoMatriculaTurmaMatriculaPeriodoFacade().realizarConsultarMatriculaPeriodoAptaRenovarPorTurma(getRenovacaoMatriculaTurma(), getConfiguracaoFinanceiro(), getConfiguracaoGeralSistemaVO(), permitirRealizarMatriculaDisciplinaPreRequisito, getRenovacaoMatriculaTurma().getResponsavel(), gradeDisciplinaCompostaVOs, null);
					getFacadeFactory().getRenovacaoMatriculaTurmaGradeDisciplinaCompostaFacade().executarGeracaoRenovacaoMatriculaTurmaGradeDisciplinaComposta(getRenovacaoMatriculaTurma(), gradeDisciplinaCompostaVOs);
					getFacadeFactory().getRenovacaoMatriculaTurmaGradeDisciplinaCompostaFacade().persistirPorRenovacaoMatriculaTurma(getRenovacaoMatriculaTurma(), false, getUsuarioLogado());
				}
				if (getRenovacaoMatriculaTurma().getSituacao().equals(SituacaoRenovacaoTurmaEnum.EM_PROCESSAMENTO)) {
					getRenovacaoMatriculaTurma().setMensagemErro("");
					getRenovacaoMatriculaTurma().setSituacao(SituacaoRenovacaoTurmaEnum.PROCESSAMENTO_CONCLUIDO);
					getRenovacaoMatriculaTurma().setDataTerminoProcessamento(new Date());
					try {
						getFacadeFactory().getRenovacaoMatriculaTurmaFacade().alterarDadosBasicos(getRenovacaoMatriculaTurma(), false, getRenovacaoMatriculaTurma().getResponsavel());
						getAplicacaoControle().executarBloqueioTurmaNaRenovacaoTurma(getRenovacaoMatriculaTurma().getCodigo(), null, false, true, false, false);
					} catch(InterruptedException ex){
						throw ex;		
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		} catch(UncategorizedSQLException ex){
			throw ex;	
		}catch(InterruptedException ex){
			return;
		} catch (Exception e) {
			if (!getRenovacaoMatriculaTurma().getSituacao().equals(SituacaoRenovacaoTurmaEnum.PROCESSAMENTO_INTERROMPIDO)) {
				getRenovacaoMatriculaTurma().setMensagemErro(e.getMessage());
				getRenovacaoMatriculaTurma().setSituacao(SituacaoRenovacaoTurmaEnum.ERRO_PROCESSAMENTO);
				getRenovacaoMatriculaTurma().setDataTerminoProcessamento(new Date());
				try {
					getFacadeFactory().getRenovacaoMatriculaTurmaFacade().alterarDadosBasicos(getRenovacaoMatriculaTurma(), false, getRenovacaoMatriculaTurma().getResponsavel());
					getAplicacaoControle().executarBloqueioTurmaNaRenovacaoTurma(getRenovacaoMatriculaTurma().getCodigo(), null, false, true, false, false);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}finally {
			getAplicacaoControle().executarBloqueioTurmaNaRenovacaoTurma(getRenovacaoMatriculaTurma().getCodigo(), null, false, true, false, false);
		}
	}

	private void executarRenovacaoMatriculaTurmaPorTurma(boolean permitirRealizarMatriculaDisciplinaPreRequisito) throws Exception, InterruptedException {
		List<RenovacaoMatriculaTurmaMatriculaPeriodoVO> renovacaoMatriculaTurmaMatriculaPeriodoVOs = getFacadeFactory().getRenovacaoMatriculaTurmaMatriculaPeriodoFacade().consultarPorRenovacaoMatriculaTurmaESituacao(getRenovacaoMatriculaTurma().getCodigo(), SituacaoRenovacaoMatriculaPeriodoEnum.AGUARDANDO_REALIZACAO, Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, getRenovacaoMatriculaTurma().getResponsavel(), 20, 0);
		Boolean processar = renovacaoMatriculaTurmaMatriculaPeriodoVOs.size() > 0;
		while (processar) {
			for (RenovacaoMatriculaTurmaMatriculaPeriodoVO renovacaoMatriculaTurmaMatriculaPeriodoVO : renovacaoMatriculaTurmaMatriculaPeriodoVOs) {
				try {
					if (!getFacadeFactory().getRenovacaoMatriculaTurmaFacade().realizarVerificacaoRenovacaoTurmaInterrompida(getRenovacaoMatriculaTurma())) {
						renovacaoMatriculaTurmaMatriculaPeriodoVO.setNovaMatriculaPeriodoVO(getFacadeFactory().getRenovacaoMatriculaTurmaMatriculaPeriodoFacade().realizarRenovacaoAutomaticaAtravesRenovacaPorTurma(getRenovacaoMatriculaTurma(), renovacaoMatriculaTurmaMatriculaPeriodoVO, getConfiguracaoFinanceiro(), getConfiguracaoGeralSistemaVO(), permitirRealizarMatriculaDisciplinaPreRequisito, getRenovacaoMatriculaTurma().getResponsavel(), false, gradeDisciplinaCompostaVOs));
						renovacaoMatriculaTurmaMatriculaPeriodoVO.setSituacao(SituacaoRenovacaoMatriculaPeriodoEnum.REALIZADO_SUCESSO);
						renovacaoMatriculaTurmaMatriculaPeriodoVO.setMensagemErro("");
					} else {
						getRenovacaoMatriculaTurma().setSituacao(SituacaoRenovacaoTurmaEnum.PROCESSAMENTO_INTERROMPIDO);
						break;
					}
				} catch(InterruptedException ex){
						throw ex;			
				} catch(UncategorizedSQLException ex){
					throw ex;			
				}catch (Exception ex) {		
					renovacaoMatriculaTurmaMatriculaPeriodoVO.setNovaMatriculaPeriodoVO(new MatriculaPeriodoVO());
					renovacaoMatriculaTurmaMatriculaPeriodoVO.setMensagemErro(ex.getMessage());
					renovacaoMatriculaTurmaMatriculaPeriodoVO.setSituacao(SituacaoRenovacaoMatriculaPeriodoEnum.REALIZADO_ERRO);
				}
				
				try {
					getFacadeFactory().getRenovacaoMatriculaTurmaMatriculaPeriodoFacade().alterar(renovacaoMatriculaTurmaMatriculaPeriodoVO, false, getRenovacaoMatriculaTurma().getResponsavel());
				} catch(InterruptedException ex){
					throw ex;
				} catch(UncategorizedSQLException ex){
					throw ex;	
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			if (getRenovacaoMatriculaTurma().getSituacao().equals(SituacaoRenovacaoTurmaEnum.EM_PROCESSAMENTO)) {
				renovacaoMatriculaTurmaMatriculaPeriodoVOs = getFacadeFactory().getRenovacaoMatriculaTurmaMatriculaPeriodoFacade().consultarPorRenovacaoMatriculaTurmaESituacao(getRenovacaoMatriculaTurma().getCodigo(), SituacaoRenovacaoMatriculaPeriodoEnum.AGUARDANDO_REALIZACAO, Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, getRenovacaoMatriculaTurma().getResponsavel(), 20, 0);
				processar = renovacaoMatriculaTurmaMatriculaPeriodoVOs.size() > 0;
			} else {
				processar = false;
				break;
			}
		}
	}

	public ConfiguracaoFinanceiroVO getConfiguracaoFinanceiro() {
		return configuracaoFinanceiro;
	}

	public void setConfiguracaoFinanceiro(ConfiguracaoFinanceiroVO configuracaoFinanceiro) {
		this.configuracaoFinanceiro = configuracaoFinanceiro;
	}

	public ConfiguracaoGeralSistemaVO getConfiguracaoGeralSistemaVO() {
		return configuracaoGeralSistemaVO;
	}

	public void setConfiguracaoGeralSistemaVO(ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) {
		this.configuracaoGeralSistemaVO = configuracaoGeralSistemaVO;
	}

	public UsuarioVO getUsuarioLogado() {
		return getRenovacaoMatriculaTurma().getResponsavel();
	}

	public AplicacaoControle getAplicacaoControle() {
		if (aplicacaoControle == null) {
			aplicacaoControle = new AplicacaoControle();
		}
		return aplicacaoControle;
	}

	public void setAplicacaoControle(AplicacaoControle aplicacaoControle) {
		this.aplicacaoControle = aplicacaoControle;
	}

	public RenovacaoMatriculaTurmaVO getRenovacaoMatriculaTurma() {
		if (renovacaoMatriculaTurma == null) {
			renovacaoMatriculaTurma = new RenovacaoMatriculaTurmaVO();
		}
		return renovacaoMatriculaTurma;
	}

	public void setRenovacaoMatriculaTurma(RenovacaoMatriculaTurmaVO renovacaoMatriculaTurma) {
		this.renovacaoMatriculaTurma = renovacaoMatriculaTurma;
	}

}
