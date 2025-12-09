package jobs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.CalendarioAtividadeMatriculaVO;
import negocio.comuns.ead.enumeradores.SituacaoAvaliacaoOnlineMatriculaEnum;
import negocio.comuns.processosel.InscricaoVO;
import negocio.comuns.processosel.ResultadoProcessoSeletivoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.SuperFacadeJDBC;

public class JobProvaProcessoSeletivo extends SuperFacadeJDBC implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer codigoInscricao;
	private Long tempoLimite;

	public JobProvaProcessoSeletivo(Integer codigoInscricao, Long tempoLimite) {
		super();
		this.codigoInscricao = codigoInscricao;
		this.tempoLimite = tempoLimite;
	}

	@Override
	public void run() {
		try {
			while(this.tempoLimite - new Date().getTime() > 0){
				Thread.sleep(this.tempoLimite - new Date().getTime() + 60000);
			}
			if (this.tempoLimite - new Date().getTime() < 0) {
				Thread.sleep(60000);
			}
			try {							
				UsuarioVO usuarioVO = getFacadeFactory().getUsuarioFacade().obterUsuarioResponsavelOperacoesExternas();
				InscricaoVO inscricaoVO = getFacadeFactory().getInscricaoFacade().consultarPorChavePrimaria(codigoInscricao, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
				inscricaoVO = getFacadeFactory().getResultadoProcessoSeletivoFacade().executarMontagemDadosResultadoProcessoSeletivoPorInscricaoVO(inscricaoVO, usuarioVO);
				if (!Uteis.isAtributoPreenchido(inscricaoVO.getDataHoraTermino())) {
					List<ResultadoProcessoSeletivoVO> resultadoProcessoSeletivoVOs = new ArrayList<ResultadoProcessoSeletivoVO>();
					inscricaoVO.getResultadoProcessoSeletivoVO().setInscricao(inscricaoVO);
					inscricaoVO.getResultadoProcessoSeletivoVO().setNavegadorAcesso("DESKTOP_EXECUCAO_JOB");
					inscricaoVO.setNavegadorAcesso("DESKTOP_EXECUCAO_JOB");
					resultadoProcessoSeletivoVOs.add(inscricaoVO.getResultadoProcessoSeletivoVO());
					getFacadeFactory().getResultadoProcessoSeletivoFacade().gravarLancamentoNotaPorDisciplina(resultadoProcessoSeletivoVOs, usuarioVO);
					inscricaoVO.setDataHoraTermino(Uteis.getDataJDBCTimestamp(new Date())); 
					getFacadeFactory().getInscricaoFacade().alterarDataHoraTerminoProvaProcessoSeletivo(inscricaoVO,usuarioVO);
				}
				getAplicacaoControle().removerJobProvaProcessoSeletivoPorInscricao(inscricaoVO.getCodigo());				
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	public Long getTempoLimite() {
		return tempoLimite;
	}

	public void setTempoLimite(Long tempoLimite) {
		this.tempoLimite = tempoLimite;
	}
	
	public Integer getCodigoInscricao() {
		return codigoInscricao;
	}

	public void setCodigoInscricao(Integer codigoInscricao) {
		this.codigoInscricao = codigoInscricao;
	}
}