package controle.financeiro;

/**
 * 
 * @author Rodrigo
 */
import java.io.Serializable;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoModuloEnum;
import negocio.comuns.arquitetura.enumeradores.TipoVisaoEnum;
import negocio.comuns.utilitarias.DashboardVO;
import negocio.comuns.utilitarias.dominios.TipoDashboardEnum;
import negocio.facade.jdbc.arquitetura.ControleAcesso;

@Controller("PainelGestorBancoCurriculumControle")
@Scope("session")
@Lazy
public class PainelGestorBancoCurriculumControle extends SuperControle implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8419796058479398105L;
	public String dataUltimoCadastroParceiro;
	public String dataUltimoCadastroVaga;
	public Integer qtdEmpresasVagasAberta;
	public Integer qtdVagasAberta;
	private Integer qtdVagasEncerradas;
	private Integer qtdVagasExpiradas;
	private Integer qtdVagasSobAnalise;
	public Integer qtdParceirosCadastrados;
	private Integer qtdParceirosInativados;
	public Integer qtdAlunosAtivos;
	private Integer qtdAlunosSelecionados;
	private Integer qtdAlunosContratados;
	
//	private DashboardVO dashboardBancoCurriculum;
//		
//	public DashboardVO getDashboardBancoCurriculum() {		
//		return dashboardBancoCurriculum;
//	}
//
//	public void setDashboardBancoCurriculum(DashboardVO dashboardBancoCurriculum) {
//		this.dashboardBancoCurriculum = dashboardBancoCurriculum;
//	}

	public PainelGestorBancoCurriculumControle() {

	}

	public void candidatoBancoCurriculo() {
		try {
			context().getExternalContext().getSessionMap().put("realizarConsulta", Boolean.TRUE);
		} catch (Exception e) {
			e.getMessage();
		}
	}

	public void parceiroCadastrado() {
		try {
			context().getExternalContext().getSessionMap().put("parceiroCadastrado", Boolean.TRUE);
		} catch (Exception e) {
			e.getMessage();
		}
	}

	public void parceiroInativos() {
		try {
			context().getExternalContext().getSessionMap().put("parceiroInativos", Boolean.TRUE);
		} catch (Exception e) {
			e.getMessage();
		}
	}

	public void vagaEncerradas() {
		try {
			context().getExternalContext().getSessionMap().put("vagaEncerradas", Boolean.TRUE);
		} catch (Exception e) {
			e.getMessage();
		}
	}

	public void vagaExpiradas() {
		try {
			context().getExternalContext().getSessionMap().put("vagaExpiradas", Boolean.TRUE);
		} catch (Exception e) {
			e.getMessage();
		}
	}

	public void vagaSobAnalise() {
		try {
			context().getExternalContext().getSessionMap().put("vagaSobAnalise", Boolean.TRUE);
		} catch (Exception e) {
			e.getMessage();
		}
	}

	public void parceiroComVaga() {
		try {
			context().getExternalContext().getSessionMap().put("parceiroComVaga", Boolean.TRUE);
		} catch (Exception e) {
			e.getMessage();
		}
	}

	public void candidatoSelecionado() {
		try {
			context().getExternalContext().getSessionMap().put("candidatoSelecionado", Boolean.TRUE);
		} catch (Exception e) {
			e.getMessage();
		}
	}

	public void candidatoContratado() {
		try {
			context().getExternalContext().getSessionMap().put("candidatoContratado", Boolean.TRUE);
		} catch (Exception e) {
			e.getMessage();
		}
	}

	private Boolean perfilAcessoPainelBancoCurriculum;

	public Boolean getPerfilAcessoPainelBancoCurriculum() {
		if (perfilAcessoPainelBancoCurriculum == null) {
			try {
				ControleAcesso.consultar("PainelBancoCurriculum", true, getUsuarioLogado());
				perfilAcessoPainelBancoCurriculum = true;
			} catch (Exception e) {
				perfilAcessoPainelBancoCurriculum = false;
			}
		}
		return perfilAcessoPainelBancoCurriculum;
	}

	@PostConstruct
	public void init() {
		inicializarDadosPainelBancoCurriculum();
	}
		public void inicializarDadosPainelBancoCurriculum() {
//		if (getPerfilAcessoPainelBancoCurriculum()) {
//			if (!getLoginControle().getMapDashboards().containsKey(TipoDashboardEnum.BANCO_CURRICULUM.name())) {
//				dashboardBancoCurriculum = 
//						new DashboardVO(TipoDashboardEnum.BANCO_CURRICULUM, false, 11, TipoVisaoEnum.ADMINISTRATIVA, PerfilAcessoModuloEnum.ADMINISTRATIVO,
//								getUsuarioLogado());
//				getLoginControle().getMapDashboards().put(TipoDashboardEnum.BANCO_CURRICULUM.name(),dashboardBancoCurriculum);
//			}else {
//				dashboardBancoCurriculum = getLoginControle().getMapDashboards().get(TipoDashboardEnum.BANCO_CURRICULUM.name());
//			}
//			dashboardBancoCurriculum.iniciar(1l, 2, "Consultando...", true, this, "consultarDadosBancoCurriculum");	
//			dashboardBancoCurriculum.iniciarAssincrono();
//		} else {
//			if (getLoginControle().getMapDashboards().containsKey(TipoDashboardEnum.BANCO_CURRICULUM.name())) {
//				getLoginControle().getMapDashboards().remove(TipoDashboardEnum.BANCO_CURRICULUM.name());
//			}
//		}
	}

	public void consultarDadosBancoCurriculum() {
		try {
			setQtdAlunosAtivos(getFacadeFactory().getMatriculaPeriodoFacade().consultarQuantidadeAlunosAtivos());
			setQtdVagasAberta(getFacadeFactory().getVagasFacade().consultarQuantidadeVagasAbertas());
			setQtdParceirosCadastrados(getFacadeFactory().getParceiroFacade().consultarQuantidadeParceiroCadastrados());
			setQtdParceirosInativados(getFacadeFactory().getParceiroFacade().consultarQuantidadeParceiroInativados());
			setQtdVagasEncerradas(getFacadeFactory().getVagasFacade().consultarQuantidadeVagasEncerradas());
			setQtdVagasExpiradas(getFacadeFactory().getVagasFacade().consultarQuantidadeVagasExpiradas());
			setQtdVagasSobAnalise(getFacadeFactory().getVagasFacade().consultarQuantidadeVagasSobAnalise());
			setQtdEmpresasVagasAberta(getFacadeFactory().getVagasFacade().consultarQuantidadeEmpresasVagasAbertas());
			setQtdAlunosSelecionados(getFacadeFactory().getVagasFacade().consultarQuantidadeAlunosSelecionados());
			setQtdAlunosContratados(getFacadeFactory().getVagasFacade().consultarQuantidadeAlunosContratado());
		} catch (Exception e) {
			e.getMessage();
		}finally {
//			getDashboardBancoCurriculum().incrementar();
//			getDashboardBancoCurriculum().encerrar();
		}
	}

	public String getDataUltimoCadastroParceiro() {
		if (dataUltimoCadastroParceiro == null) {
			dataUltimoCadastroParceiro = "";
		}
		return dataUltimoCadastroParceiro;
	}

	public void setDataUltimoCadastroParceiro(String dataUltimoCadastroParceiro) {
		this.dataUltimoCadastroParceiro = dataUltimoCadastroParceiro;
	}

	public String getDataUltimoCadastroVaga() {
		if (dataUltimoCadastroVaga == null) {
			dataUltimoCadastroVaga = "";
		}
		return dataUltimoCadastroVaga;
	}

	public void setDataUltimoCadastroVaga(String dataUltimoCadastroVaga) {
		this.dataUltimoCadastroVaga = dataUltimoCadastroVaga;
	}

	public Integer getQtdEmpresasVagasAberta() {
		if (qtdEmpresasVagasAberta == null) {
			qtdEmpresasVagasAberta = 0;
		}
		return qtdEmpresasVagasAberta;
	}

	public void setQtdEmpresasVagasAberta(Integer qtdEmpresasVagasAberta) {
		this.qtdEmpresasVagasAberta = qtdEmpresasVagasAberta;
	}

	public Integer getQtdVagasAberta() {
		if (qtdVagasAberta == null) {
			qtdVagasAberta = 0;
		}
		return qtdVagasAberta;
	}

	public void setQtdVagasAberta(Integer qtdVagasAberta) {
		this.qtdVagasAberta = qtdVagasAberta;
	}

	public Integer getQtdParceirosCadastrados() {
		if (qtdParceirosCadastrados == null) {
			qtdParceirosCadastrados = 0;
		}
		return qtdParceirosCadastrados;
	}

	public void setQtdParceirosCadastrados(Integer qtdParceirosCadastrados) {
		this.qtdParceirosCadastrados = qtdParceirosCadastrados;
	}

	public Integer getQtdAlunosAtivos() {
		if (qtdAlunosAtivos == null) {
			qtdAlunosAtivos = 0;
		}
		return qtdAlunosAtivos;
	}

	public void setQtdAlunosAtivos(Integer qtdAlunosAtivos) {
		this.qtdAlunosAtivos = qtdAlunosAtivos;
	}

	/**
	 * @return the qtdAlunosSelecionados
	 */
	public Integer getQtdAlunosSelecionados() {
		if (qtdAlunosSelecionados == null) {
			qtdAlunosSelecionados = 0;
		}
		return qtdAlunosSelecionados;
	}

	/**
	 * @param qtdAlunosSelecionados the qtdAlunosSelecionados to set
	 */
	public void setQtdAlunosSelecionados(Integer qtdAlunosSelecionados) {
		this.qtdAlunosSelecionados = qtdAlunosSelecionados;
	}

	/**
	 * @return the qtdParceirosInativados
	 */
	public Integer getQtdParceirosInativados() {
		if (qtdParceirosInativados == null) {
			qtdParceirosInativados = 0;
		}
		return qtdParceirosInativados;
	}

	/**
	 * @param qtdParceirosInativados the qtdParceirosInativados to set
	 */
	public void setQtdParceirosInativados(Integer qtdParceirosInativados) {
		this.qtdParceirosInativados = qtdParceirosInativados;
	}

	/**
	 * @return the qtdAlunosContratados
	 */
	public Integer getQtdAlunosContratados() {
		if (qtdAlunosContratados == null) {
			qtdAlunosContratados = 0;
		}
		return qtdAlunosContratados;
	}

	/**
	 * @param qtdAlunosContratados the qtdAlunosContratados to set
	 */
	public void setQtdAlunosContratados(Integer qtdAlunosContratados) {
		this.qtdAlunosContratados = qtdAlunosContratados;
	}

	/**
	 * @return the qtdVagasEncerradas
	 */
	public Integer getQtdVagasEncerradas() {
		if (qtdVagasEncerradas == null) {
			qtdVagasEncerradas = 0;
		}
		return qtdVagasEncerradas;
	}

	/**
	 * @param qtdVagasEncerradas the qtdVagasEncerradas to set
	 */
	public void setQtdVagasEncerradas(Integer qtdVagasEncerradas) {
		this.qtdVagasEncerradas = qtdVagasEncerradas;
	}

	/**
	 * @return the qtdVagasExpiradas
	 */
	public Integer getQtdVagasExpiradas() {
		if (qtdVagasExpiradas == null) {
			qtdVagasExpiradas = 0;
		}
		return qtdVagasExpiradas;
	}

	/**
	 * @param qtdVagasExpiradas the qtdVagasExpiradas to set
	 */
	public void setQtdVagasExpiradas(Integer qtdVagasExpiradas) {
		this.qtdVagasExpiradas = qtdVagasExpiradas;
	}

	/**
	 * @return the qtdVagasSobAnalise
	 */
	public Integer getQtdVagasSobAnalise() {
		if (qtdVagasSobAnalise == null) {
			qtdVagasSobAnalise = 0;
		}
		return qtdVagasSobAnalise;
	}

	/**
	 * @param qtdVagasSobAnalise the qtdVagasSobAnalise to set
	 */
	public void setQtdVagasSobAnalise(Integer qtdVagasSobAnalise) {
		this.qtdVagasSobAnalise = qtdVagasSobAnalise;
	}
}
