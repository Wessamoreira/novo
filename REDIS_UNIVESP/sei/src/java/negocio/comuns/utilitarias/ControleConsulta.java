package negocio.comuns.utilitarias;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 * Classe responsável por controlar a paginação de um conjunto de objetos armazenados em um lista <code>List</code>.
 * Fornecer atributos que persistem informações sobre: os parâmetros para realização da consulta, página atual da 
 * da consulta, posicição inicial e final da lista que está sendo apresentada e, por fim, o número de itens total
 * armazenados na lista que está sendo paginada. Adicionalmente, esta classe fornece métodos que permitem navegar
 * para primeira, próxima, anterior e última página. Calculando automaticamente a posição inicial e final da próxima 
 * página. Também fornece informações como número total de páginas.
 */
public class ControleConsulta implements Serializable {

    private String campoConsulta;
    private String valorConsulta;
    private Date dataIni;
    private Date dataFim;
    private Boolean buscarPeriodoCompleto;
    private int paginaAtual;
    private int paginaAtual2;
	private int paginaAtual3;
    private List listaConsulta;
    private List listaConsulta2;
	private List listaConsulta3;
    private String situacao;
    private int posInicialListar;
    private int posInicialListar2;
	private int posInicialListar3;
    private int posFinalListar;
    private int posFinalListar2;
	private int posFinalListar3;
    private int tamanhoConsulta;
    private int tamanhoConsulta2;
	private int tamanhoConsulta3;
    private int limitePorPagina;
    private int limitePorPagina2;
	private int limitePorPagina3;
    private int totalPaginas;
    private int totalPaginas2;
	private int totalPaginas3;
    private int offset;
    private int offset2;
	private int offset3;
    private List<Integer> listaPaginas;
    private List<Integer> listaPaginas2;
	private List<Integer> listaPaginas3;
    private long totalRegistrosEncontrados;
    private long totalRegistrosEncontrados2;
	private long totalRegistrosEncontrados3;
    private int numeroPaginaScroll;
    private int numeroPaginaScroll2;
	private int numeroPaginaScroll3;
    private String ordenarPor;
    private String campoConsultaEspecificoSimulacao;

    public ControleConsulta() {
        inicializarDados();
    }

    /**
     * Construtor que define por default o campo de consulta selecionado.
     * @param campoConsultaPadrao
     */
    public ControleConsulta(String campoConsultaPadrao) {
        setCampoConsulta(campoConsultaPadrao);
        inicializarDados();
    }

    private void inicializarDados() {
        campoConsulta = "";
        valorConsulta = "";
        situacao = "";
        paginaAtual = 1;
        paginaAtual2 = 1;
		paginaAtual3 = 1;
        posInicialListar = 0;
        posInicialListar2 = 0;
		posInicialListar3 = 0;
        posFinalListar = Uteis.TAMANHOLISTA;
        posFinalListar2 = Uteis.TAMANHOLISTA;
		posFinalListar3 = Uteis.TAMANHOLISTA;
        tamanhoConsulta = 0;
        tamanhoConsulta2 = 0;
		tamanhoConsulta3 = 0;
        dataIni = new Date();
        dataFim = Uteis.getNewDateComUmMesAMais();
        numeroPaginaScroll = 10;
        numeroPaginaScroll2 = 10;
		numeroPaginaScroll3 = 10;
        totalRegistrosEncontrados = 0;
        totalRegistrosEncontrados2 = 0;
		totalRegistrosEncontrados3 = 0;
        listaConsulta = null;
        listaConsulta2 = null;
		listaConsulta3 = null;
        listaPaginas = null;
        listaPaginas2 = null;
		listaPaginas3 = null;
        limitePorPagina = Uteis.TAMANHOLISTA;
        limitePorPagina2 = Uteis.TAMANHOLISTA;
		limitePorPagina3 = Uteis.TAMANHOLISTA;
    }

    /**
     * Inicializa todos os atributos necessários para o início de uma nova consulta.
     */
    public static void inicializar(ControleConsulta controleConsulta) {
        controleConsulta.paginaAtual = 1;
        controleConsulta.posInicialListar = 0;
        controleConsulta.posFinalListar = Uteis.TAMANHOLISTA;
        controleConsulta.tamanhoConsulta = 0;
        controleConsulta.paginaAtual2 = 1;
		controleConsulta.paginaAtual3 = 1;
        controleConsulta.posInicialListar2 = 0;
		controleConsulta.posInicialListar3 = 0;
        controleConsulta.posFinalListar2 = Uteis.TAMANHOLISTA;
		controleConsulta.posFinalListar3 = Uteis.TAMANHOLISTA;
        controleConsulta.tamanhoConsulta2 = 0;
		controleConsulta.tamanhoConsulta3 = 0;
    }

    /**
     * Configura todos os atributos necessários para apresentação da página fornecida pelo parâmetro.
     * Permitindo uma fácil navegação para a página desejada.
     */
    public void definirProximaPaginaApresentar(int pagina) {
        paginaAtual = pagina;
        if (paginaAtual > 1) {
            posInicialListar = (pagina - 1) * Uteis.TAMANHOLISTA;
            posFinalListar = (pagina) * Uteis.TAMANHOLISTA;
        } else {
            posInicialListar = 0;
            posFinalListar = Uteis.TAMANHOLISTA;
        }
    }

    public void definirProximaPaginaApresentar2(int pagina2) {
        paginaAtual2 = pagina2;
        if (paginaAtual2 > 1) {
            posInicialListar2 = (pagina2 - 1) * Uteis.TAMANHOLISTA;
            posFinalListar2 = (pagina2) * Uteis.TAMANHOLISTA;
        } else {
            posInicialListar2 = 0;
            posFinalListar2 = Uteis.TAMANHOLISTA;
        }
    }
    public void definirProximaPaginaApresentar3(int pagina3) {
        paginaAtual3 = pagina3;
        if (paginaAtual3 > 1) {
            posInicialListar3 = (pagina3 - 1) * Uteis.TAMANHOLISTA;
            posFinalListar3 = (pagina3) * Uteis.TAMANHOLISTA;
        } else {
            posInicialListar3 = 0;
            posFinalListar3 = Uteis.TAMANHOLISTA;
        }
    }

    public String getURLPrimeiraPagina() {
        String url = null;
        if ((paginaAtual > 1)
                && (getNrTotalPaginas() > 1)) {
            url = "?consultar=0"
                    + "&pagina=" + 1
                    + "&campoConsulta=" + campoConsulta
                    + "&valorConsulta=" + valorConsulta;
        }
        return url;
    }

    public String getURLPrimeiraPagina2() {
        String url = null;
        if ((paginaAtual2 > 1)
                && (getNrTotalPaginas2() > 1)) {
            url = "?consultar=0"
                    + "&pagina=" + 1
                    + "&campoConsulta=" + campoConsulta
                    + "&valorConsulta=" + valorConsulta;
        }
        return url;
    }
    public String getURLPrimeiraPagina3() {
        String url = null;
        if ((paginaAtual3 > 1)
                && (getNrTotalPaginas3() > 1)) {
            url = "?consultar=0"
                    + "&pagina=" + 1
                    + "&campoConsulta=" + campoConsulta
                    + "&valorConsulta=" + valorConsulta;
        }
        return url;
    }

    public String getURLPaginaAnterior() {
        String url = null;
        if (paginaAtual > 1) {
            url = "?consultar=0"
                    + "&pagina=" + (paginaAtual - 1)
                    + "&campoConsulta=" + campoConsulta
                    + "&valorConsulta=" + valorConsulta;
        }
        return url;
    }

    public String getURLPaginaAnterior2() {
        String url = null;
        if (paginaAtual2 > 1) {
            url = "?consultar=0"
                    + "&pagina=" + (paginaAtual2 - 1)
                    + "&campoConsulta=" + campoConsulta
                    + "&valorConsulta=" + valorConsulta;
        }
        return url;
    }
    public String getURLPaginaAnterior3() {
        String url = null;
        if (paginaAtual3 > 1) {
            url = "?consultar=0"
                    + "&pagina=" + (paginaAtual3 - 1)
                    + "&campoConsulta=" + campoConsulta
                    + "&valorConsulta=" + valorConsulta;
        }
        return url;
    }


    public String getURLPaginaPosterior() {
        String url = null;
        if (paginaAtual < getNrTotalPaginas()) {
            url = "?consultar=0"
                    + "&pagina=" + (paginaAtual + 1)
                    + "&campoConsulta=" + campoConsulta
                    + "&valorConsulta=" + valorConsulta;
        }
        return url;
    }

    public String getURLPaginaPosterior2() {
        String url = null;
        if (paginaAtual2 < getNrTotalPaginas2()) {
            url = "?consultar=0"
                    + "&pagina=" + (paginaAtual2 + 1)
                    + "&campoConsulta=" + campoConsulta
                    + "&valorConsulta=" + valorConsulta;
        }
        return url;
    }
    public String getURLPaginaPosterior3() {
        String url = null;
        if (paginaAtual3 < getNrTotalPaginas3()) {
            url = "?consultar=0"
                    + "&pagina=" + (paginaAtual3 + 1)
                    + "&campoConsulta=" + campoConsulta
                    + "&valorConsulta=" + valorConsulta;
        }
        return url;
    }


    public String getURLUltimaPagina() {
        String url = null;
        if ((getNrTotalPaginas() > 1)
                && (paginaAtual < getNrTotalPaginas())) {
            url = "?consultar=0"
                    + "&pagina=" + getNrTotalPaginas()
                    + "&campoConsulta=" + campoConsulta
                    + "&valorConsulta=" + valorConsulta;
        }
        return url;
    }

    public String getURLUltimaPagina2() {
        String url = null;
        if ((getNrTotalPaginas2() > 1)
                && (paginaAtual2 < getNrTotalPaginas2())) {
            url = "?consultar=0"
                    + "&pagina=" + getNrTotalPaginas2()
                    + "&campoConsulta=" + campoConsulta
                    + "&valorConsulta=" + valorConsulta;
        }
        return url;
    }
    public String getURLUltimaPagina3() {
        String url = null;
        if ((getNrTotalPaginas3() > 1)
                && (paginaAtual3 < getNrTotalPaginas3())) {
            url = "?consultar=0"
                    + "&pagina=" + getNrTotalPaginas3()
                    + "&campoConsulta=" + campoConsulta
                    + "&valorConsulta=" + valorConsulta;
        }
        return url;
    }

    public static List paginarConsulta(HttpServletRequest request, List resultado, ControleConsulta controleConsulta) throws Exception {
        ControleConsulta.registrarTamanhoConsulta(resultado, controleConsulta);
        resultado = ControleConsulta.obterSubListPaginaApresentar(resultado, controleConsulta);
        ControleConsulta.registrarParametrosConsulta(request, controleConsulta);
        return resultado;
    }

    public static void registrarParametrosConsulta(HttpServletRequest request, ControleConsulta controleConsulta) throws Exception {
        request.setAttribute("campoConsulta", controleConsulta.getCampoConsulta());
        request.setAttribute("valorConsulta", controleConsulta.getValorConsulta());
        request.setAttribute("prmPrimeiraPg", controleConsulta.getURLPrimeiraPagina());
        request.setAttribute("prmPgAnterior", controleConsulta.getURLPaginaAnterior());
        request.setAttribute("prmPgPosterior", controleConsulta.getURLPaginaPosterior());
        request.setAttribute("prmUltimaPg", controleConsulta.getURLUltimaPagina());
        request.setAttribute("paginaAtual", controleConsulta.getPaginaAtualDeTodas());
        request.setAttribute("tamanhoTotalConsulta", String.valueOf(controleConsulta.getTamanhoConsulta()));
        request.setAttribute("controleConsulta", controleConsulta);
    }

    public static void registrarTamanhoConsulta(List resultado, ControleConsulta controleConsulta) {
        if (resultado == null) {
            return;
        }
        controleConsulta.setTamanhoConsulta(resultado.size());
    }

    public static List obterSubListPaginaApresentar(List resultado, ControleConsulta controleConsulta) {
        if (resultado == null) {
            inicializar(controleConsulta);
            return null;
        }
        if (resultado.isEmpty()) {
            inicializar(controleConsulta);
            return resultado;
        }
        List subList = new ArrayList();
        if (controleConsulta.getPosFinalListar() > resultado.size()) {
            controleConsulta.setPosFinalListar(resultado.size());
        }
        if (controleConsulta.getPosInicialListar() < resultado.size()) {
            subList = resultado.subList(controleConsulta.getPosInicialListar(), controleConsulta.getPosFinalListar());
        }
        controleConsulta.setTamanhoConsulta(resultado.size());
        return subList;
    }

    public int getNrTotalPaginas() {
        double tamanhoPagina = Uteis.TAMANHOLISTA;
        double nrPaginasDouble = Math.ceil(tamanhoConsulta / tamanhoPagina);
        String nrTotalPaginas = String.valueOf(nrPaginasDouble);
        nrTotalPaginas = nrTotalPaginas.substring(0, nrTotalPaginas.indexOf("."));
        return (Integer.parseInt(nrTotalPaginas));
    }

    public int getNrTotalPaginas2() {
        double tamanhoPagina2 = Uteis.TAMANHOLISTA;
        double nrPaginasDouble2 = Math.ceil(tamanhoConsulta2 / tamanhoPagina2);
        String nrTotalPaginas2 = String.valueOf(nrPaginasDouble2);
        nrTotalPaginas2 = nrTotalPaginas2.substring(0, nrTotalPaginas2.indexOf("."));
        return (Integer.parseInt(nrTotalPaginas2));
    }
    public int getNrTotalPaginas3() {
        double tamanhoPagina3 = Uteis.TAMANHOLISTA;
        double nrPaginasDouble3 = Math.ceil(tamanhoConsulta3 / tamanhoPagina3);
        String nrTotalPaginas3 = String.valueOf(nrPaginasDouble3);
        nrTotalPaginas3 = nrTotalPaginas3.substring(0, nrTotalPaginas3.indexOf("."));
        return (Integer.parseInt(nrTotalPaginas3));
    }

    public String getPaginaAtualDeTodas() {
        return paginaAtual + "/" + getNrTotalPaginas();
    }
    public String getPaginaAtualDeTodas2() {
        return paginaAtual2 + "/" + getNrTotalPaginas2();
    }
    public String getPaginaAtualDeTodas3() {
        return paginaAtual3 + "/" + getNrTotalPaginas3();
    }

    public int getPosInicialListar() {
        return posInicialListar;
    }

    public void setPosInicialListar(int posInicialListar) {
        this.posInicialListar = posInicialListar;
    }

    public int getPosInicialListar2() {
        return posInicialListar2;
    }
    public int getPosInicialListar3() {
        return posInicialListar3;
    }

    public void setPosInicialListar2(int posInicialListar2) {
        this.posInicialListar2 = posInicialListar2;
    }
    public void setPosInicialListar3(int posInicialListar3) {
        this.posInicialListar3 = posInicialListar3;
    }

    public int getPosFinalListar() {
        return posFinalListar;
    }

    public void setPosFinalListar(int posFinalListar) {
        this.posFinalListar = posFinalListar;
    }
    public int getPosFinalListar2() {
        return posFinalListar2;
    }
    public int getPosFinalListar3() {
        return posFinalListar3;
    }

    public void setPosFinalListar2(int posFinalListar2) {
        this.posFinalListar2 = posFinalListar2;
    }
    public void setPosFinalListar3(int posFinalListar3) {
        this.posFinalListar3 = posFinalListar3;
    }

    public int getTamanhoConsulta() {
        return tamanhoConsulta;
    }

    public void setTamanhoConsulta(int tamanhoConsulta) {
        this.tamanhoConsulta = tamanhoConsulta;
    }

    public int getTamanhoConsulta2() {
        return tamanhoConsulta2;
    }
    public int getTamanhoConsulta3() {
        return tamanhoConsulta3;
    }

    public void setTamanhoConsulta2(int tamanhoConsulta2) {
        this.tamanhoConsulta2 = tamanhoConsulta2;
    }
    public void setTamanhoConsulta3(int tamanhoConsulta3) {
        this.tamanhoConsulta3 = tamanhoConsulta3;
    }

    public String getCampoConsulta() {
    	if (campoConsulta == null) {
            campoConsulta = "";
        }
        return campoConsulta;
    }

    public void setCampoConsulta(String campoConsulta) {
        if (campoConsulta == null) {
            campoConsulta = "";
        }
        this.campoConsulta = campoConsulta;
    }

    public String getValorConsulta() {
    	if (valorConsulta == null) {
            valorConsulta = "";
        }
        return valorConsulta;
    }

    public void setValorConsulta(String valorConsulta) {
        if (valorConsulta == null) {
            valorConsulta = "";
        }
        this.valorConsulta = valorConsulta;
    }

    public int getPaginaAtual() {
        return paginaAtual;
    }

    public void setPaginaAtual(int paginaAtual) {
        this.paginaAtual = paginaAtual;
    }

    public int getPaginaAtual2() {
        return paginaAtual2;
    }
    public int getPaginaAtual3() {
        return paginaAtual3;
    }

    public void setPaginaAtual2(int paginaAtual2) {
        this.paginaAtual2 = paginaAtual2;
    }
    public void setPaginaAtual3(int paginaAtual3) {
        this.paginaAtual3 = paginaAtual3;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public Date getDataIni() {
        if (dataIni == null) {
            dataIni = new Date();
        }
        return dataIni;
    }

    public String getDataIni_Apresentar() {
		if (dataIni == null) {
			return "";
		}
		return (Uteis.getData(dataIni));
    }
    
    public void setDataIni(Date dataIni) {
        this.dataIni = dataIni;
    }

    public String getSituacao() {
        if (situacao == null) {
            situacao = "";
        }
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public int getNumeroPaginaScroll() {
        return numeroPaginaScroll;
    }

    public void setNumeroPaginaScroll(int numeroPaginaScroll) {
        this.numeroPaginaScroll = numeroPaginaScroll;
    }
    public int getNumeroPaginaScroll2() {
        return numeroPaginaScroll2;
    }
    public int getNumeroPaginaScroll3() {
        return numeroPaginaScroll3;
    }

    public void setNumeroPaginaScroll2(int numeroPaginaScroll2) {
        this.numeroPaginaScroll2 = numeroPaginaScroll2;
    }
    public void setNumeroPaginaScroll3(int numeroPaginaScroll3) {
        this.numeroPaginaScroll3 = numeroPaginaScroll3;
    }

    public List getListaConsulta() {
        if (listaConsulta == null) {
            listaConsulta = new ArrayList(0);
        }
        return listaConsulta;
    }

    public void setListaConsulta(List listaConsulta) {
        this.listaConsulta = listaConsulta;
    }
    public List getListaConsulta2() {
        if (listaConsulta2 == null) {
            listaConsulta2 = new ArrayList(0);
        }
        return listaConsulta2;
    }
    public List getListaConsulta3() {
        if (listaConsulta3 == null) {
            listaConsulta3 = new ArrayList(0);
        }
        return listaConsulta3;
    }

    public void setListaConsulta2(List listaConsulta2) {
        this.listaConsulta2 = listaConsulta2;
    }
    public void setListaConsulta3(List listaConsulta3) {
        this.listaConsulta3 = listaConsulta3;
    }

    public void setLimitePorPagina(int limitePorPagina) {
        this.limitePorPagina = limitePorPagina;
    }

    public int getLimitePorPagina() {
        return limitePorPagina;
    }

    public void setLimitePorPagina2(int limitePorPagina2) {
        this.limitePorPagina2 = limitePorPagina2;
    }
    public void setLimitePorPagina3(int limitePorPagina3) {
        this.limitePorPagina3 = limitePorPagina3;
    }

    public int getLimitePorPagina2() {
        return limitePorPagina2;
    }
    public int getLimitePorPagina3() {
        return limitePorPagina3;
    }

    public List<Integer> getListaPaginas() {
        montarListaPaginas();
        return listaPaginas;
    }

    public List<Integer> getListaPaginas2() {
        montarListaPaginas2();
        return listaPaginas2;
    }
    public List<Integer> getListaPaginas3() {
        montarListaPaginas3();
        return listaPaginas3;
    }

    public int getOffset() {
        return getLimitePorPagina() * (getPaginaAtual() - 1);
    }

    public void setOffset(int offset) {
        if (offset < 0) {
            this.offset = 0;
        }
        this.offset = offset;
    }
    public int getOffset2() {
        return getLimitePorPagina2() * (getPaginaAtual2() - 1);
    }
    public int getOffset3() {
        return getLimitePorPagina3() * (getPaginaAtual3() - 1);
    }

    public void setOffset2(int offset2) {
        if (offset2 < 0) {
            this.offset2 = 0;
        }
        this.offset2 = offset2;
    }
    public void setOffset3(int offset3) {
        if (offset3 < 0) {
            this.offset3 = 0;
        }
        this.offset3 = offset3;
    }

    public long getTotalRegistrosEncontrados() {
        return totalRegistrosEncontrados;
    }

    public void setTotalRegistrosEncontrados(long totalRegistrosEncontrados) {
        this.totalRegistrosEncontrados = totalRegistrosEncontrados;
    }

    public long getTotalRegistrosEncontrados2() {
        return totalRegistrosEncontrados2;
    }
    public long getTotalRegistrosEncontrados3() {
        return totalRegistrosEncontrados3;
    }

    public void setTotalRegistrosEncontrados2(long totalRegistrosEncontrados2) {
        this.totalRegistrosEncontrados2 = totalRegistrosEncontrados2;
    }
    public void setTotalRegistrosEncontrados3(long totalRegistrosEncontrados3) {
        this.totalRegistrosEncontrados3 = totalRegistrosEncontrados3;
    }

    public int getTotalPaginas() {
        // Para fazer o arredondamento para cima, utilizando o método Math.ceil,
        // é necessário fazer cast para double devido a precisão.
        // Exemplo: 5.6 sem cast para double, resultado = 5
        //          5.6 com cast para double, resultado = 6 -> valor esperado.
        totalPaginas = (int) Math.ceil(((double) getTotalRegistrosEncontrados()) / ((double) getLimitePorPagina()));
        return totalPaginas;
    }

    public int getTotalPaginas2() {
        // Para fazer o arredondamento para cima, utilizando o método Math.ceil,
        // é necessário fazer cast para double devido a precisão.
        // Exemplo: 5.6 sem cast para double, resultado = 5
        //          5.6 com cast para double, resultado = 6 -> valor esperado.
        totalPaginas2 = (int) Math.ceil(((double) getTotalRegistrosEncontrados2()) / ((double) getLimitePorPagina2()));
        return totalPaginas2;
    }
    public int getTotalPaginas3() {
        // Para fazer o arredondamento para cima, utilizando o método Math.ceil,
        // é necessário fazer cast para double devido a precisão.
        // Exemplo: 5.6 sem cast para double, resultado = 5
        //          5.6 com cast para double, resultado = 6 -> valor esperado.
        totalPaginas3 = (int) Math.ceil(((double) getTotalRegistrosEncontrados3()) / ((double) getLimitePorPagina3()));
        return totalPaginas3;
    }

    public void resetarPaginacao() {
        setOffset(0);
        setPaginaAtual(1);
    }

    public void resetarPaginacao2() {
        setOffset2(0);
        setPaginaAtual2(1);
    }
    public void resetarPaginacao3() {
        setOffset3(0);
        setPaginaAtual3(1);
    }

    private void montarListaPaginas() {
        listaPaginas = null;
        listaPaginas = new ArrayList<Integer>(0);
        
        int paginasEsquerda = getPaginaAtual() - (getNumeroPaginaScroll()/2);
        int paginasDireita = getTotalPaginas() >  getNumeroPaginaScroll() ? getNumeroPaginaScroll() : getTotalPaginas();
        if(paginasEsquerda <= 0 ) {
        	paginasEsquerda = 1;        	
        }else {
        	paginasDireita = paginasEsquerda + getNumeroPaginaScroll()-1;
        	if(paginasDireita > getTotalPaginas() ) {
        		paginasDireita = getTotalPaginas();
        	}
        	if(paginasDireita - paginasDireita < getNumeroPaginaScroll() && getNumeroPaginaScroll() <= getTotalPaginas()) {
        		paginasEsquerda = paginasDireita - getNumeroPaginaScroll()+1;
        	}
        }        
        for (int i = paginasEsquerda; i <= paginasDireita; i++) {
        	listaPaginas.add(i);        	        	
        }
    }
    private void montarListaPaginas2() {
        listaPaginas2 = null;
        listaPaginas2 = new ArrayList<Integer>();
        
        int paginasEsquerda = getPaginaAtual2() - (getNumeroPaginaScroll2()/2);
        int paginasDireita = getTotalPaginas2() >  getNumeroPaginaScroll2() ? getNumeroPaginaScroll2() : getTotalPaginas2();
        if(paginasEsquerda <= 0 ) {
        	paginasEsquerda = 1;        	
        }else {
        	paginasDireita = paginasEsquerda + getNumeroPaginaScroll2()-1;
        	if(paginasDireita > getTotalPaginas2() ) {
        		paginasDireita = getTotalPaginas2();
        	}
        	if(paginasDireita - paginasDireita < getNumeroPaginaScroll2() && getNumeroPaginaScroll2() <= getTotalPaginas2()) {
        		paginasEsquerda = paginasDireita - getNumeroPaginaScroll2()+1;
        	}
        }        
        for (int i = paginasEsquerda; i <= paginasDireita; i++) {
        	listaPaginas2.add(i);        	        	
        }
       

    }
    private void montarListaPaginas3() {
        listaPaginas3 = null;
        listaPaginas3 = new ArrayList<Integer>();

        int paginasEsquerda = getPaginaAtual3() - (getNumeroPaginaScroll3()/2);
        int paginasDireita = getTotalPaginas3() >  getNumeroPaginaScroll3() ? getNumeroPaginaScroll3() : getTotalPaginas3();
        if(paginasEsquerda <= 0 ) {
        	paginasEsquerda = 1;        	
        }else {
        	paginasDireita = paginasEsquerda + getNumeroPaginaScroll3()-1;
        	if(paginasDireita > getTotalPaginas3() ) {
        		paginasDireita = getTotalPaginas3();
        	}
        	if(paginasDireita - paginasDireita < getNumeroPaginaScroll3() && getNumeroPaginaScroll3() <= getTotalPaginas3()) {
        		paginasEsquerda = paginasDireita - getNumeroPaginaScroll3()+1;
        	}
        }        
        for (int i = paginasEsquerda; i <= paginasDireita; i++) {
        	listaPaginas3.add(i);        	        	
        }

    }

    public String getNumeroMaximoPagina() {
        if (listaPaginas != null) {
            return "" + listaPaginas.size();
        }
        return "0";
    }

    public String getNumeroMaximoPagina2() {
        if (listaPaginas2 != null) {
            return "" + listaPaginas2.size();
        }
        return "0";
    }
    public String getNumeroMaximoPagina3() {
        if (listaPaginas3 != null) {
            return "" + listaPaginas3.size();
        }
        return "0";
    }

    public Boolean getIsTemProximo() {
        if (getPaginaAtual() >= getTotalPaginas()) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public Boolean getIsTemProximo2() {
        if (getPaginaAtual2() >= getTotalPaginas2()) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
    public Boolean getIsTemProximo3() {
        if (getPaginaAtual3() >= getTotalPaginas3()) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public Boolean getIsTemAnterior() {
        if (getPaginaAtual() > 1) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public Boolean getIsTemAnterior2() {
        if (getPaginaAtual2() > 1) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    public Boolean getIsTemAnterior3() {
        if (getPaginaAtual3() > 1) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public int getProximaPagina() {
        if (getIsTemProximo()) {
            return (getPaginaAtual() + 1);
        }
        return getTotalPaginas();
    }

    public int getProximaPagina2() {
        if (getIsTemProximo2()) {
            return (getPaginaAtual2() + 1);
        }
        return getTotalPaginas2();
    }
    public int getProximaPagina3() {
        if (getIsTemProximo3()) {
            return (getPaginaAtual3() + 1);
        }
        return getTotalPaginas3();
    }

    public int getPaginaAnterior() {
        if (getIsTemAnterior()) {
            return (getPaginaAtual() - 1);
        }
        return 1;
    }

    public int getPaginaAnterior2() {
        if (getIsTemAnterior2()) {
            return (getPaginaAtual2() - 1);
        }
        return 1;
    }
    public int getPaginaAnterior3() {
        if (getIsTemAnterior3()) {
            return (getPaginaAtual3() - 1);
        }
        return 1;
    }

    public String getCssPaginaAtual() {
        Integer pagina = (Integer) FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("pagina");
        if (getPaginaAtual() == pagina) {
            return "paginaAtual";
        }
        return "tituloCamposReduzidos";
    }

    public String getCssPaginaAtual2() {
        Integer pagina2 = (Integer) FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("pagina2");
        if (getPaginaAtual2() == pagina2) {
            return "paginaAtual";
        }
        return "tituloCamposReduzidos";
    }
    public String getCssPaginaAtual3() {
        Integer pagina3 = (Integer) FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("pagina3");
        if (getPaginaAtual3() == pagina3) {
            return "paginaAtual";
        }
        return "tituloCamposReduzidos";
    }

    /**
     * @return the buscarPeriodoCompleto
     */
    public Boolean getBuscarPeriodoCompleto() {
        if (buscarPeriodoCompleto == null) {
            buscarPeriodoCompleto = Boolean.FALSE;
        }
        return buscarPeriodoCompleto;
    }

    /**
     * @param buscarPeriodoCompleto the buscarPeriodoCompleto to set
     */
    public void setBuscarPeriodoCompleto(Boolean buscarPeriodoCompleto) {
        this.buscarPeriodoCompleto = buscarPeriodoCompleto;
    }

	public String getOrdenarPor() {
		if (ordenarPor == null) {
			ordenarPor = "";
		}
		return ordenarPor;
	}

	public void setOrdenarPor(String ordenarPor) {
		this.ordenarPor = ordenarPor;
	}

	public String getCampoConsultaEspecificoSimulacao() {
		return campoConsultaEspecificoSimulacao;
	}

	public void setCampoConsultaEspecificoSimulacao(String campoConsultaEspecificoSimulacao) {
		this.campoConsultaEspecificoSimulacao = campoConsultaEspecificoSimulacao;
	}
	
	public void limparCampoValor(){
    	this.setValorConsulta("");
    }
}
