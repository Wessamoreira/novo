/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.controle.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.bancocurriculum.CandidatosVagasVO;
import negocio.comuns.basico.PessoaVO;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;

/**
 * 
 * @author Rogerio
 */
@SuppressWarnings("unchecked")
@Controller("CurriculumRelControle")
@Scope("viewScope")
@Lazy
public class CurriculumRelControle extends SuperControleRelatorio {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7551642219156416168L;
	private PessoaVO pessoaVO;
	private String tipoRelatorio;
	private static String IdEntidade;

	public CurriculumRelControle() throws Exception {
		setMensagemID("msg_entre_prmrelatorio");
	}

	public void imprimirPDF() {
		
		try {
			
			gerarRelatorio(TipoRelatorioEnum.PDF);

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			
		}
	}
	
	public void gerarRelatorio(TipoRelatorioEnum tipoRelatorioEnum) throws Exception{
		if (getTipoRelatorio().equals("")) {
			throw new Exception("O campo LAYOUT deve ser informado.");
		}
		List<CandidatosVagasVO> listaObjetos = new ArrayList<CandidatosVagasVO>(0);
		setIdEntidade(getTipoRelatorio());
		CandidatosVagasVO candidatosVagasVO = (CandidatosVagasVO) context().getExternalContext().getSessionMap().get("candidatosVagasVO");
		if(candidatosVagasVO == null){
			candidatosVagasVO = new CandidatosVagasVO();
			candidatosVagasVO.setPessoa((PessoaVO) context().getExternalContext().getSessionMap().get("pessoaVO"));
			//context().getExternalContext().getSessionMap().remove("pessoaVO");
		}else{
			//context().getExternalContext().getSessionMap().remove("candidatosVagasVO");
		}
		if (candidatosVagasVO != null && candidatosVagasVO.getPessoa().getCodigo()>0) {
			
			candidatosVagasVO.setCandidatoVagaQuestaoVOs(getFacadeFactory().getCandidatoVagaQuestaoFacade().consultarPorCandidatoVaga(candidatosVagasVO.getCodigo(), candidatosVagasVO.getVaga().getCodigo()));
			
			candidatosVagasVO.getPessoa().getAreaProfissionalInteresseContratacaoVOs().clear();
			candidatosVagasVO.getPessoa().getDadosComerciaisVOs().clear();
			candidatosVagasVO.getPessoa().getFormacaoExtraCurricularVOs().clear();
			candidatosVagasVO.getPessoa().getFormacaoAcademicaVOs().clear();
			getFacadeFactory().getPessoaFacade().carregarDados(candidatosVagasVO.getPessoa(), getUsuarioLogado());
			candidatosVagasVO.getPessoa().setFormacaoExtraCurricularVOs(getFacadeFactory().getFormacaoExtraCurricularFacade().consultarPorCodigoPessoaOrdemNovaAntiga(candidatosVagasVO.getPessoa().getCodigo(), false, getUsuarioLogado()));
			candidatosVagasVO.getPessoa().setFormacaoAcademicaVOs(getFacadeFactory().getFormacaoAcademicaFacade().consultarPorCodigoPessoaOrdemNovaAntiga(candidatosVagasVO.getPessoa().getCodigo(), false, getUsuarioLogado()));
			candidatosVagasVO.getPessoa().setDadosComerciaisVOs(getFacadeFactory().getDadosComerciaisFacade().consultarPorCodigoPessoaOrdemNovaAntiga(candidatosVagasVO.getPessoa().getCodigo(), false, getUsuarioLogado()));
			listaObjetos.add(candidatosVagasVO);
			if (!listaObjetos.isEmpty()) {
				getSuperParametroRelVO().setNomeDesignIreport(getDesignIReportRelatorio());
				getSuperParametroRelVO().setTipoRelatorioEnum(tipoRelatorioEnum);
				getSuperParametroRelVO().setSubReport_Dir(getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio("");
				getSuperParametroRelVO().setListaObjetos(listaObjetos);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeEmpresa("");
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setFiltros("");
				getSuperParametroRelVO().adicionarParametro("checkboxDesmarcado2", getCaminhoPastaWeb() + File.separator + "resources" + File.separator + "imagens" + File.separator + "checkboxDesmarcado2.png");
				getSuperParametroRelVO().adicionarParametro("checkboxMarcado2", getCaminhoPastaWeb() + File.separator + "resources" + File.separator + "imagens" + File.separator + "checkboxMarcado2.png");
				getSuperParametroRelVO().adicionarParametro("radioButtomDesmarcado2", getCaminhoPastaWeb() + File.separator + "resources" + File.separator + "imagens" + File.separator + "radioButtomDesmarcado2.png");
				getSuperParametroRelVO().adicionarParametro("radioButtomMarcado2", getCaminhoPastaWeb() + File.separator + "resources" + File.separator + "imagens" + File.separator + "radioButtomMarcado2.png");
				realizarImpressaoRelatorio();
				removerObjetoMemoria(this);
				setMensagemID("msg_relatorio_ok");
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
		}
	}

	public void imprimirWord() {		
		try {
			gerarRelatorio(TipoRelatorioEnum.DOC);

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			
		}
	}

	public List<SelectItem> getTipoConsultaRelatorio() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("CurriculumLayout1Rel", "Layout 1"));
		// itens.add(new SelectItem("CurriculumLayout2Rel", "Layout 2"));
		return itens;
	}

	public static String getDesignIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade() + ".jrxml");
	}

	public static String getCaminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
	}

	public PessoaVO getPessoaVO() {
		return pessoaVO;
	}

	public void setPessoaVO(PessoaVO pessoaVO) {
		this.pessoaVO = pessoaVO;
	}

	public String getTipoRelatorio() {
		if (tipoRelatorio == null) {
			tipoRelatorio = "CurriculumLayout1Rel";
		}
		return tipoRelatorio;
	}

	public void setTipoRelatorio(String tipoRelatorio) {
		this.tipoRelatorio = tipoRelatorio;
	}

	public static String getIdEntidade() {
		if (IdEntidade == null) {
			IdEntidade = "CurriculumLayout1Rel";
		}
		return IdEntidade;
	}

	public static void setIdEntidade(String IdEntidade) {
		CurriculumRelControle.IdEntidade = IdEntidade;
	}
}
