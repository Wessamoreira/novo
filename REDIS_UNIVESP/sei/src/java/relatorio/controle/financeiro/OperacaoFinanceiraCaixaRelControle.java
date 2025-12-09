/**
 * 
 */
package relatorio.controle.financeiro;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.financeiro.OperacaoFinanceiraRelVO;
import relatorio.negocio.jdbc.financeiro.OperacaoFinanceiraCaixaRel;

/**
 * @author Rodrigo Wind
 *
 */
@Controller("OperacaoFinanceiraCaixaRelControle")
@Lazy
@Scope("viewScope")
public class OperacaoFinanceiraCaixaRelControle extends SuperControleRelatorio {

	/**
	 * 
	 */
	private static final long serialVersionUID = 326179259646472084L;

	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private List<SelectItem> listaSelectItemContaCaixa;
	private Integer codigoUnidadeEnsino;
	private Integer contaCaixa;
	private Date dataInicio;
	private Date dataFim;
	private String tipoLayout;

	/**
	 * @author Rodrigo Wind - 04/11/2015
	 */

	public void realizarImpressaoPDF() {
		realizarImpressaoRelatorio(TipoRelatorioEnum.PDF);
	}

	public void realizarImpressaoExcel() {
		realizarImpressaoRelatorio(TipoRelatorioEnum.EXCEL);
	}

	public void realizarImpressaoRelatorio(TipoRelatorioEnum tipoRelatorio) {
		List<OperacaoFinanceiraRelVO> listaObjetos= null;
		String retornoContaCaixa = "";
		ContaCorrenteVO obj = null;
		 try {
	            getFacadeFactory().getFluxoCaixaRelFacade().validarDados(getDataInicio(), getDataFim());
	            listaObjetos = getFacadeFactory().getOperacaoFinanceiraCaixaRelFacade().realizarGeracaoRelatorio(getCodigoUnidadeEnsino(), getContaCaixa(), getDataInicio(), getDataFim(), getTipoLayout(), getUsuarioLogado());
	            if (listaObjetos.isEmpty() || listaObjetos.get(0).getOperacaoFinanceiraRelVOs().isEmpty()) {
	            	setMensagemID("msg_relatorio_sem_dados");
	            	return;
	            }  
	            if(tipoRelatorio.equals(TipoRelatorioEnum.PDF)){
	            	if (getTipoLayout().equals("layout1")) {
	            		getSuperParametroRelVO().setNomeDesignIreport(OperacaoFinanceiraCaixaRel.designIReportRelatorio());	            		
	            	} else {
	            		getSuperParametroRelVO().setNomeDesignIreport(OperacaoFinanceiraCaixaRel.designIReportRelatorio2());
	            	}
	            } else {
	            	if (getTipoLayout().equals("layout1")) {
	            		getSuperParametroRelVO().setNomeDesignIreport("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + "OperacaoFinanceiraCaixaRelExcel.jrxml");
	            	} else {
	            		getSuperParametroRelVO().setNomeDesignIreport("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + "OperacaoFinanceiraCaixaRelExcel2.jrxml");
	            	}
	            }
	            getSuperParametroRelVO().setTipoRelatorioEnum(tipoRelatorio);
	            getSuperParametroRelVO().setSubReport_Dir(OperacaoFinanceiraCaixaRel.caminhoBaseIReportRelatorio());
	            getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
	            getSuperParametroRelVO().setTituloRelatorio("Relatório de Operações Financeiras Caixa");
	            getSuperParametroRelVO().setListaObjetos(listaObjetos);
	            getSuperParametroRelVO().setCaminhoBaseRelatorio(OperacaoFinanceiraCaixaRel.caminhoBaseIReportRelatorio());
	            getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());

	            getSuperParametroRelVO().setPeriodo(String.valueOf(Uteis.getData(getDataInicio())) + "  até  " + String.valueOf(Uteis.getData(getDataFim())));

	            if (getCodigoUnidadeEnsino() != null && getCodigoUnidadeEnsino() > 0) {
	                getSuperParametroRelVO().setUnidadeEnsino(
	                        (getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getCodigoUnidadeEnsino(), false,
	                        Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())).getNome());
	            } else {
	                getSuperParametroRelVO().setUnidadeEnsino("Todas");
	            }
	            retornoContaCaixa = "Todas";
	            if (getContaCaixa() != null && getContaCaixa() > 0) {
	                obj = getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(getContaCaixa(), false,Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				  if (Uteis.isAtributoPreenchido(obj.getNomeApresentacaoSistema())) {
					retornoContaCaixa = obj.getNomeApresentacaoSistema();
				  } else {
					retornoContaCaixa = obj.getNumero() + " - " + obj.getDigito();
				  }
	            }
	            getSuperParametroRelVO().setContaCorrente(retornoContaCaixa);	          
	            realizarImpressaoRelatorio();
//	            removerObjetoMemoria(this);
	            inicializarListasSelectItemTodosComboBox();
	            setMensagemID("msg_relatorio_ok");
	        } catch (Exception e) {
	            setMensagemDetalhada("msg_erro", e.getMessage());
	        } finally {
	            Uteis.liberarListaMemoria(listaObjetos);
	            
	        }
	}

	public void inicializarListasSelectItemTodosComboBox() throws Exception {
		montarListaSelectItemUnidadeEnsino();
		montarListaSelectItemContaCaixa();
	}

	public void montarListaSelectItemUnidadeEnsino() {
		try {
			montarListaSelectItemUnidadeEnsino("");
		} catch (Exception e) {
			
		}
	}

	public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
		List<UnidadeEnsinoVO> resultadoConsulta = null;
		Iterator<UnidadeEnsinoVO> i = null;
		try {
			resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
			i = resultadoConsulta.iterator();
			List<SelectItem> objs = new ArrayList<SelectItem>(0);
			if (super.getUnidadeEnsinoLogado().getCodigo().equals(0)) {
				objs.add(new SelectItem(0, ""));
			} else {
				setCodigoUnidadeEnsino(super.getUnidadeEnsinoLogado().getCodigo());
			}
			while (i.hasNext()) {
				UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
			}
			setListaSelectItemUnidadeEnsino(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public void montarListaSelectItemContaCaixa(){
		List<ContaCorrenteVO> resultadoConsulta = null;
		Iterator<ContaCorrenteVO> i = null;
		try {
			resultadoConsulta = consultarContaCaixaPorNumero();
			i = resultadoConsulta.iterator();
			List<SelectItem> objs = new ArrayList<SelectItem>(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				ContaCorrenteVO obj = (ContaCorrenteVO) i.next();
				if(Uteis.isAtributoPreenchido(obj.getNomeApresentacaoSistema())){
					objs.add(new SelectItem(obj.getCodigo(), obj.getNomeApresentacaoSistema()));
				}else{
					objs.add(new SelectItem(obj.getCodigo(), obj.getNumero() + "-" + obj.getDigito()));
				}
			}
			setListaSelectItemContaCaixa(objs);
		} catch (Exception e) {
			
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		List<UnidadeEnsinoVO> lista = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		return lista;
	}

	@SuppressWarnings("unchecked")
	public List<ContaCorrenteVO> consultarContaCaixaPorNumero() throws Exception {
		List<ContaCorrenteVO> lista = getFacadeFactory().getContaCorrenteFacade().consultarPorContaCaixa(true, getCodigoUnidadeEnsino(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		return lista;
	}

	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if(listaSelectItemUnidadeEnsino == null){
			montarListaSelectItemUnidadeEnsino();
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public List<SelectItem> getListaSelectItemContaCaixa() {
		if(listaSelectItemContaCaixa == null){
			montarListaSelectItemContaCaixa();
		}
		return listaSelectItemContaCaixa;
	}

	public void setListaSelectItemContaCaixa(List<SelectItem> listaSelectItemContaCaixa) {
		this.listaSelectItemContaCaixa = listaSelectItemContaCaixa;
	}

	public Date getDataFim() {
		if (dataFim == null) {
			dataFim = new Date();
		}
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public Date getDataInicio() {
		if (dataInicio == null) {
			dataInicio = new Date();
		}
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Integer getCodigoUnidadeEnsino() {
		if (codigoUnidadeEnsino == null) {
			codigoUnidadeEnsino = 0;
		}
		return codigoUnidadeEnsino;
	}

	public void setCodigoUnidadeEnsino(Integer codigoUnidadeEnsino) {
		this.codigoUnidadeEnsino = codigoUnidadeEnsino;
	}
	
	public Integer getContaCaixa() {
        if (contaCaixa == null) {
            contaCaixa = 0;
        }
        return contaCaixa;
    }

    public void setContaCaixa(Integer contaCaixa) {
        this.contaCaixa = contaCaixa;
    }
	
	public List getTipoLayoutCombo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("layout1", "Layout 1"));
		itens.add(new SelectItem("layout2", "Layout 2"));
		return itens;
	}

	public String getTipoLayout() {
		if (tipoLayout == null) {
			tipoLayout = "layout1";
		}
		return tipoLayout;
	}

	public void setTipoLayout(String tipoLayout) {
		this.tipoLayout = tipoLayout;
	}
	
}
