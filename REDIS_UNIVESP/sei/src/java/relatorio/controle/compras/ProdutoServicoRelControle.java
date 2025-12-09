package relatorio.controle.compras;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.compras.CategoriaProdutoVO;
import negocio.comuns.utilitarias.Uteis;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.compras.ProdutoServicoRelVO;
import relatorio.negocio.jdbc.compras.ProdutoServicoRel;

@SuppressWarnings({ "serial" , "deprecation"})
@Controller("ProdutoServicoRelControle")
@Scope("viewScope")
@Lazy
public class ProdutoServicoRelControle extends SuperControleRelatorio {	
	
	private Integer valorConsultaCategoriaProduto;
	private String valorConsultaTipoProduto;
	private String valorConsultaSituacaoProduto;
	private List listaSelectItemCategoriaProduto;
	
	public ProdutoServicoRelControle() {	
		montarListaSelectItemCategoriaProduto();
		setMensagemID("msg_entre_prmrelatorio");
	}
	
	public Integer getValorConsultaCategoriaProduto() {
		return valorConsultaCategoriaProduto;
	}

	public void setValorConsultaCategoriaProduto(Integer valorConsultaCategoriaProduto) {
		this.valorConsultaCategoriaProduto = valorConsultaCategoriaProduto;
	}

	public String getValorConsultaTipoProduto() {
		return valorConsultaTipoProduto;
	}

	public void setValorConsultaTipoProduto(String valorConsultaTipoProduto) {
		this.valorConsultaTipoProduto = valorConsultaTipoProduto;
	}

	public String getValorConsultaSituacaoProduto() {
		return valorConsultaSituacaoProduto;
	}

	public void setValorConsultaSituacaoProduto(String valorConsultaSituacaoProduto) {
		this.valorConsultaSituacaoProduto = valorConsultaSituacaoProduto;
	}

	public List getListaSelectItemCategoriaProduto() {
		return listaSelectItemCategoriaProduto;
	}

	public void setListaSelectItemCategoriaProduto(List listaSelectItemCategoriaProduto) {
		this.listaSelectItemCategoriaProduto = listaSelectItemCategoriaProduto;
	}
		
	public void imprimirPDF() {
		List<ProdutoServicoRelVO> listaObjetos = null;	
		try {

			registrarAtividadeUsuario(getUsuarioLogado(), "ProdutoServicoRelControle", "Inicializando Geração de Relatório Produto/Serviço" + getUsuarioLogado().getCodigo() + " - " + getUsuarioLogado().getPerfilAcesso().getCodigo(), "Emitindo Relatório");
			
			listaObjetos = getFacadeFactory().getProdutoServicoRelFacade().criarObjeto(getValorConsultaCategoriaProduto(), getValorConsultaTipoProduto(), getValorConsultaSituacaoProduto(), getUsuarioLogado());
			if (!listaObjetos.isEmpty()) {
				getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getProdutoServicoRelFacade().designIReportRelatorio());
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setSubReport_Dir(ProdutoServicoRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio("Relatório Produto/Serviço");
				getSuperParametroRelVO().setListaObjetos(listaObjetos);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getProdutoServicoRelFacade().caminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeEmpresa("");
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setFiltros("");	
				if(getValorConsultaCategoriaProduto() > 0) {
					getSuperParametroRelVO().adicionarParametro("categoriaProduto", getFacadeFactory().getCategoriaProdutoFacade().consultarPorChavePrimaria(getValorConsultaCategoriaProduto(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()).getNome());
				}
                else {
                	getSuperParametroRelVO().adicionarParametro("categoriaProduto", "Todas");
                }
				getSuperParametroRelVO().adicionarParametro("tipoProduto", getValorConsultaTipoProduto());
				getSuperParametroRelVO().adicionarParametro("situacaoProduto", getValorConsultaSituacaoProduto());
				
				realizarImpressaoRelatorio();
				setMensagemID("msg_relatorio_ok");
	            setValorConsultaCategoriaProduto(0);
	            setValorConsultaTipoProduto("");
	            setValorConsultaSituacaoProduto("");
	            registrarAtividadeUsuario(getUsuarioLogado(), "ProdutoServicoRelControle", "Finalizando Geração de Relatório Produto/Serviço", "Emitindo Relatório");	            
	            //removerObjetoMemoria(this);               
	            
			} else {
				setMensagemID("msg_relatorio_sem_dados");
				setFazerDownload(false);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(listaObjetos);	
		}
	}

	public void imprimirRelatorioExcel() {
		List<ProdutoServicoRelVO> listaObjetos = null;		
		try {

			registrarAtividadeUsuario(getUsuarioLogado(), "ProdutoServicoRelControle", "Inicializando Geração de Relatório Produto/Serviço" + getUsuarioLogado().getCodigo() + " - " + getUsuarioLogado().getPerfilAcesso().getCodigo(), "Emitindo Relatório");
			
			listaObjetos = getFacadeFactory().getProdutoServicoRelFacade().criarObjeto(getValorConsultaCategoriaProduto(), getValorConsultaTipoProduto(), getValorConsultaSituacaoProduto(), getUsuarioLogado());
			if (!listaObjetos.isEmpty()) {
				getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getProdutoServicoRelFacade().designIReportRelatorioExcel());
		        getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.EXCEL);
		        getSuperParametroRelVO().setSubReport_Dir(ProdutoServicoRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio("Relatório Produto/Serviço");
				getSuperParametroRelVO().setListaObjetos(listaObjetos);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getProdutoServicoRelFacade().caminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeEmpresa("");
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setFiltros("");
				if(getValorConsultaCategoriaProduto() > 0) {
					getSuperParametroRelVO().adicionarParametro("categoriaProduto", getFacadeFactory().getCategoriaProdutoFacade().consultarPorChavePrimaria(getValorConsultaCategoriaProduto(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()).getNome());
				}
                else {
                	getSuperParametroRelVO().adicionarParametro("categoriaProduto", "Todas");
                }
				getSuperParametroRelVO().adicionarParametro("tipoProduto", getValorConsultaTipoProduto());
				getSuperParametroRelVO().adicionarParametro("situacaoProduto", getValorConsultaSituacaoProduto());

				realizarImpressaoRelatorio();
				//removerObjetoMemoria(this);
	            setValorConsultaCategoriaProduto(0);
	            setValorConsultaTipoProduto("");
	            setValorConsultaSituacaoProduto("");
				setMensagemID("msg_relatorio_ok");

	            registrarAtividadeUsuario(getUsuarioLogado(), "ProdutoServicoRelControle", "Finalizando Geração de Relatório Produto/Serviço", "Emitindo Relatório");	
			} else {
				setMensagemID("msg_relatorio_sem_dados");
				setFazerDownload(false);
			} 			
		
		}catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(listaObjetos);
		}
	}
	
	public void montarListaSelectItemCategoriaProduto(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarCategoriaProdutoPorNome(prm, Uteis.NIVELMONTARDADOS_TODOS);
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				CategoriaProdutoVO obj = (CategoriaProdutoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
			}
			setListaSelectItemCategoriaProduto(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}
	
	public void montarListaSelectItemCategoriaProduto() {
		try {
			montarListaSelectItemCategoriaProduto("");
		} catch (Exception e) {
			// System.out.println(e.getMessage());
		}
	}
	
	public List consultarCategoriaProdutoPorNome(String descricaoPrm, int nivelMontarDados) throws Exception {
		List lista = getFacadeFactory().getCategoriaProdutoFacade().consultarPorNome(descricaoPrm, false, nivelMontarDados, getUsuarioLogado());
		return lista;
	}
	
	public List<SelectItem> getListaSelectItemSituacao() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("AT", "Ativo"));
		itens.add(new SelectItem("IN", "Inativo"));
		return itens;
	}
	
}