package controle.administrativo;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import controle.arquitetura.SuperControle;
import controle.sad.DespesaDWControle;

@Controller("PainelGestorGraficoControle")
@Scope("request")
@Lazy
public class PainelGestorGraficoControle extends SuperControle {

    
//    @ResponseBody    
//    @RequestMapping(value = "/pie/categoriaDespesa", method = RequestMethod.GET)
//    public Object getDadosGraficoCategriaDespesa() throws Exception {
//        PainelGestorControle pgc = (PainelGestorControle) context().getExternalContext().getSessionMap().get("PainelGestorControle");
//        if(pgc == null){
//            return null;
//        }
//        return pgc.getPainelGestorVO().getListaValoresCategoriaDespesa();
//    }
//    
//    @ResponseBody    
//    @RequestMapping(value = "/pie/categoriaDespesaInicial", method = RequestMethod.GET)
//    public Object getDadosGraficoCategriaDespesaInicial() throws Exception {
//        PainelGestorControle pgc = (PainelGestorControle) context().getExternalContext().getSessionMap().get("PainelGestorControle");
//        if(pgc == null){
//            return null;
//        }
//        return pgc.getPainelGestorVO().getListaValoresCategoriaDespesaInicial();
//    }
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 3921608688841428593L;


	@ResponseBody    
    @RequestMapping(value = "/pie/consultarPorNivelCategoriaDespesa", method = RequestMethod.POST)
    public void consultarDadosGraficoCategoriaDespesa(@RequestBody String nivel) {
        PainelGestorControle pgc = (PainelGestorControle) context().getExternalContext().getSessionMap().get("PainelGestorControle");
        if(pgc == null){
            return;
        }
        pgc.getPainelGestorVO().setNivelAtualCategoriaDespesa(nivel);
        pgc.consultarDadosPainelGestorCategoriaDespesa();        
    }
    
    
//    @ResponseBody    
//    @RequestMapping(value = "/pie/consumo", method = RequestMethod.GET)
//    public Object getDadosGraficoConsumo() throws Exception {
//        PainelGestorControle pgc = (PainelGestorControle) context().getExternalContext().getSessionMap().get("PainelGestorControle");
//        if(pgc == null){
//            return null;
//        }
//        return getFacadeFactory().getDespesaDWFacade().realizarMontagemGraficoPizzaJSON(pgc.getNivelGraficoDWVO().getLegendaGraficoVOs());
//    }
    
//    @ResponseBody    
//    @RequestMapping(value = "/pie/consumoNivelGrafico1", method = RequestMethod.GET)
//    public Object getDadosGraficoConsumoNivel1() throws Exception {
//        DespesaDWControle pgc = (DespesaDWControle) getControlador("DespesaDWControle");
//        if(pgc == null){
//            return null;
//        }
//        return getFacadeFactory().getDespesaDWFacade().realizarMontagemGraficoPizzaJSON(pgc.getNivelGraficoDWVO1().getLegendaGraficoVOs());
//    }
//    
//    @ResponseBody    
//    @RequestMapping(value = "/pie/consumoNivelGrafico2", method = RequestMethod.GET)
//    public Object getDadosGraficoConsumoNivel2() throws Exception {
//        DespesaDWControle pgc = (DespesaDWControle) getControlador("DespesaDWControle");
//        if(pgc == null){
//            return null;
//        }
//        return getFacadeFactory().getDespesaDWFacade().realizarMontagemGraficoPizzaJSON(pgc.getNivelGraficoDWVO2().getLegendaGraficoVOs());
//    }
    
    @ResponseBody    
    @RequestMapping(value = "/pie/selecionarLegendaGraficoConsumo/{codigo}/{nome}/{valor}/{nivel}", method = RequestMethod.POST)
    public void selecionarLegendaGraficoConsumo(@PathVariable Integer codigo, @PathVariable String nome, @PathVariable Double valor, @PathVariable String nivel) {
        DespesaDWControle pgc = (DespesaDWControle) getControlador("DespesaDWControle");
        if(pgc == null){
            return;
        }
        pgc.selecionarLegenda(codigo, nome, valor, nivel);        
    }
    
    @ResponseBody    
    @RequestMapping(value = "/pie/selecionarLegendaGraficoDepartamentoConsumo/{codigo}/{nome}/{valor}/{nivel}", method = RequestMethod.POST)
    public void selecionarLegendaGraficoDepartamentoConsumo(@PathVariable Integer codigo, @PathVariable String nome, @PathVariable Double valor, @PathVariable String nivel) {
        DespesaDWControle pgc = (DespesaDWControle) getControlador("DespesaDWControle");
        if(pgc == null){
            return;
        }
        pgc.selecionarLegenda1(codigo, nome, valor, nivel);        
    }
    
    @ResponseBody    
    @RequestMapping(value = "/pie/selecionarLegendaGraficoComoConsumo/{codigo}/{nome}/{valor}/{nivel}", method = RequestMethod.POST)
    public void selecionarLegenda2GraficoConsumo(@PathVariable Integer codigo, @PathVariable String nome, @PathVariable Double valor, @PathVariable String nivel) {
        DespesaDWControle pgc = (DespesaDWControle) getControlador("DespesaDWControle");
        if(pgc == null){
            return;
        }
        pgc.selecionarLegenda2(codigo, nome, valor, nivel);        
    }
    
    
    @ResponseBody    
    @RequestMapping(value = "/pie/selecionarLegendaGraficoConsumoInicial/{codigo}/{nome}/{valor}/{nivel}", method = RequestMethod.POST)
    public void selecionarLegendaGraficoConsumoInicial(@PathVariable Integer codigo, @PathVariable String nome, @PathVariable Double valor, @PathVariable String nivel) {
        DespesaDWControle despesaDWControle = (DespesaDWControle) getControlador("DespesaDWControle");
        PainelGestorControle pgc = (PainelGestorControle) context().getExternalContext().getSessionMap().get("PainelGestorControle");
        if(despesaDWControle == null){
            return;
        }
        despesaDWControle.selecionarLegendaPainel(pgc.getUnidadeEnsinoVOs(), pgc.getDataInicio(), pgc.getDataFinal(), codigo, nome, valor, nivel);        
    }
    
    @ResponseBody    
    @RequestMapping(value = "/monitoramentoCRM/selecionarLegendaGraficoProspect/{codigo}/{nome}/{nivel}", method = RequestMethod.POST)
    public void selecionarLegendaGraficoProspect(@PathVariable Integer codigo, @PathVariable String nome, @PathVariable String nivel) {    	
    	PainelGestorControle pgc = (PainelGestorControle) context().getExternalContext().getSessionMap().get("PainelGestorControle");    	
    	pgc.selecionarLegendaGraficoProspect(codigo, nome, nivel);        
    }
   
    
    @ResponseBody    
    @RequestMapping(value = "/monitoramentoAcademico/selecionarLegendaGraficoMonitoramentoAcademico/{campo}/{quantidade}/{codigo}", method = RequestMethod.POST)
    public void selecionarLegendaGraficoMonitoramentoAcademico(@PathVariable String campo, @PathVariable Integer quantidade, @PathVariable Integer codigo) {
        
        PainelGestorControle pgc = (PainelGestorControle) getControlador("PainelGestorControle");
        if(pgc == null){
            return;
        }
        pgc.selecionarLegendaGraficoMonitoramentoAcademico(campo, quantidade, codigo);        
    }
    
    @ResponseBody    
    @RequestMapping(value = "/monitoramentoAcademico/selecionarLegendaGraficoMonitoramentoAcademicoNivelEducacional/{nivelEducacional}/{periodicidade}/{quantidade}/{codigo}", method = RequestMethod.POST)
    public void selecionarLegendaGraficoMonitoramentoAcademicoNivelEducacional(@PathVariable String nivelEducacional, @PathVariable String periodicidade,  @PathVariable Integer quantidade, @PathVariable Integer codigo) {
        PainelGestorControle pgc = (PainelGestorControle) getControlador("PainelGestorControle");
        if(pgc == null){
            return;
        }
        pgc.selecionarLegendaGraficoMonitoramentoAcademicoNivelEducacional(nivelEducacional, periodicidade, quantidade, codigo);
    }
   
    @ResponseBody    
    @RequestMapping(value = "/pie/selecionarLegendaPorSegmentacao/{codigo}", method = RequestMethod.POST)
    public void selecionarLegendaPorSegmentacao(@PathVariable Integer codigo) {
    	PainelGestorControle pgc = (PainelGestorControle) getControlador("PainelGestorControle");
        if(pgc == null){
            return;
        }
        pgc.detalharSegmentacao(codigo);        
    }
    
}
