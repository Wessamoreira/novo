package relatorio.controle.financeiro;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.ImageIcon;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import controle.arquitetura.AplicacaoControle;
import controle.arquitetura.LoginControle;
import controle.processosel.InscricaoControle;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.crm.InteracaoWorkflowVO;
import negocio.comuns.crm.enumerador.TipoOrigemInteracaoEnum;
import negocio.comuns.financeiro.ContaReceberAgrupadaVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.utilitarias.SpringUtil;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.crm.InteracaoWorkflow;
import relatorio.arquitetura.SuperRelatorioSV;
import relatorio.negocio.comuns.financeiro.BoletoBancarioRelVO;
import relatorio.negocio.interfaces.financeiro.BoletoBancarioRelInterfaceFacade;
import relatorio.negocio.jdbc.financeiro.BoletoBancarioRel;

/**
 *
 * @author Edgar
 */
@Service
@Lazy
public class BoletoBancarioSV extends SuperRelatorioSV {

    @SuppressWarnings("FieldNameHidesFieldInSuperclass")
    public static final long serialVersionUID = 1L;
    InscricaoControle inscricaoControle;
    private BoletoBancarioRelInterfaceFacade boletoBancarioRel;
    /*private Integer codigoContaReceber;
    private Integer codigoUsuario;*/
    private Integer contaReceberAgrupada;
    private String tipoBoleto = "boleto";
    private Boolean permiteImprimirBoletoRecebidoBoolean;
    //guarda os valores a serem exibidos no grÃ¡fico  
    private int[] values;  
    //guarda o buffer da imagem desenhada  
    private BufferedImage imageBuffer;  
    //guarda a cor de fundo  
    private Color background;  
    //guardam as dimensÃµes da imagem  
    private int width, height;  
    //cores para os pedaÃ§os do grÃ¡fico  
    private Color[] colors = { Color.BLUE, Color.GREEN, Color.RED,  
                                Color.YELLOW, Color.ORANGE, Color.PINK,  
                                Color.MAGENTA, Color.LIGHT_GRAY, Color.GRAY,  
                                Color.BLACK };      

    public void imprimirPDF(HttpServletRequest request, HttpServletResponse response, Integer codigoContaReceber, Integer codigoUsuario, boolean isValidarTelaAluno) throws ServletException, IOException {
        String titulo = null;
        List<BoletoBancarioRelVO> lista = null;
        String design = null;
        BoletoBancarioRelVO boletoBancarioRelVO = null;
        String urlTempLogoUnidadeEnsinoRelatorio = "";
        String urlTempLogoUnidadeEnsino = "";
        
        LoginControle loginControle = (LoginControle) request.getSession().getAttribute("LoginControle");        
        try {
            titulo = "Recibo do Sacado";
            if (contaReceberAgrupada != null && contaReceberAgrupada > 0) {            	
    			ContaReceberAgrupadaVO contaAgrupada = getFacadeFactory().getContaReceberAgrupadaFacade().consultaPorChavePrimaria(contaReceberAgrupada, Uteis.NIVELMONTARDADOS_TODOS, null);
    			if(Uteis.isAtributoPreenchido(contaAgrupada)) {
                if (loginControle == null) {
                	lista = getFacadeFactory().getBoletoBancarioRelFacade().emitirRelatorioListaContaAgrupada(false, contaAgrupada, null, null, null, null, null, null, null, null, null, "contaAgrupada", null, null, "", null, null, null, getPermiteImprimirBoletoRecebidoBoolean());
                } else {
                	lista = getFacadeFactory().getBoletoBancarioRelFacade().emitirRelatorioListaContaAgrupada(false, contaAgrupada, null, null, null, null, null, null, null, null, null, "contaAgrupada", null, loginControle.getUsuario(), "", null, loginControle.getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(), null, getPermiteImprimirBoletoRecebidoBoolean());
                }       
    			}else {
    				throw new Exception("É necessário informar uma conta válida para emissão do boleto.");
    			}
            } else {
            	if(!Uteis.isAtributoPreenchido(codigoContaReceber)) {
            		throw new Exception("É necessário informar uma conta válida para emissão do boleto.");
            	}
            	// Na conta corrente ï¿½ permitido informar para bloquear emissï¿½o de boletos, esse metodo realiza a validaçãoo dessa emissï¿½o. 
            	if (getFacadeFactory().getContaReceberFacade().validaContaCorrenteBloqueadoEmissaoBoleto(codigoContaReceber)) {
                    throw new Exception("Conta a Receber vinculada a uma conta corrente bloqueada para emissão de boleto, realize a mudança de carteira ou reative a emissão de boleto dentro do cadastro da conta corrente");
            	}            	
            	ContaReceberVO contaReceberVO = getFacadeFactory().getContaReceberFacade().realizarRenegociacaoContaReceberAutomaticamente(codigoContaReceber, loginControle != null ? loginControle.getUsuario() : null);
            	if(Uteis.isAtributoPreenchido(contaReceberVO)) {
            		codigoContaReceber = contaReceberVO.getCodigo();
            	}
            	
	            if (loginControle == null) {
	                lista = boletoBancarioRel.emitirRelatorioLista(false, codigoContaReceber, null, null, null, null, null, null, null, null, null, "", 0, null, null, null, getPermiteImprimirBoletoRecebidoBoolean());
	            } else {
	                lista = boletoBancarioRel.emitirRelatorioLista(false,codigoContaReceber, null, null, null, null, null, null, null, null, null, "", 0, loginControle.getUsuario(), loginControle.getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(), null, getPermiteImprimirBoletoRecebidoBoolean());
	            }
            }
            if (!lista.isEmpty()) {
                boletoBancarioRelVO = (BoletoBancarioRelVO) lista.get(0);
                boletoBancarioRel.validarImpressaoBoletoAluno(codigoContaReceber, boletoBancarioRelVO, "BoletoBancarioSV", codigoUsuario);
            }else {
            	throw new Exception("Aconteceu um problema inesperado. Não foi possível gerar o boleto solicitado.");
            }
            
            design = boletoBancarioRel.getObterDesign(getTipoBoleto(), boletoBancarioRelVO);
            String logoBanco = "";
            if (loginControle != null && loginControle.getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino().getImprimirBoletoComLogoBanco() && boletoBancarioRelVO != null) {
            	logoBanco = boletoBancarioRelVO.getObterLogoBanco(getCaminhoPastaWeb());
            }
			if (loginControle != null) {
				UnidadeEnsinoVO unidadeEnsinoVO = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorContaReceberDadosLogoRelatorio(boletoBancarioRelVO.getContareceber_codigo(), null);
				if (unidadeEnsinoVO != null) {
					if (unidadeEnsinoVO.getExisteLogoRelatorio()) {
						loginControle.setUrlLogoUnidadeEnsinoRelatorio(unidadeEnsinoVO.getCaminhoBaseLogoRelatorio().replaceAll("\\\\", "/") + "/" + unidadeEnsinoVO.getNomeArquivoLogoRelatorio());
					} else {
						loginControle.setUrlLogoUnidadeEnsinoRelatorio("");
					}
					if(unidadeEnsinoVO.getExisteLogo()){
						loginControle.setUrlLogoUnidadeEnsino(unidadeEnsinoVO.getCaminhoBaseLogo().replaceAll("\\\\", "/") + "/" + unidadeEnsinoVO.getNomeArquivoLogo());	
					}else{
						loginControle.setUrlLogoUnidadeEnsino("");
					}
				}
			}
			if (loginControle != null && loginControle.getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino().getImprimirBoletoComImagemLinhaDigitavel()) {
				Iterator o = lista.iterator();
				while (o.hasNext()) {
					BoletoBancarioRelVO boleto = (BoletoBancarioRelVO)o.next();
					this.width = 700;
					this.height = 25;
					this.background = Color.WHITE;
					imageBuffer = new BufferedImage( width, height, BufferedImage.TYPE_INT_RGB );  
			        Graphics g = imageBuffer.createGraphics();  
			        g.setColor( background );  
			        g.fillRect( 0, 0, width, height );  
			        Font font = new Font("Courier", Font.BOLD, 20);  
			        g.setColor( Color.BLACK );  
			        g.setFont( font );  
			        g.drawString(boleto.getContareceber_linhadigitavelcodigobarras(),0, 20);  
					
			        File diretorio = null;
			        String nomeImagem = "linhadigital" + boleto.getContareceber_codigo() + ".png";
			        String diretorioWeb = "";
			        if (diretorioWeb == null || diretorioWeb.equals("")) {
			                ServletContext servletContext = (ServletContext) this.getServletContext();
			                diretorioWeb = servletContext.getRealPath("");
			        }
			        diretorio = new File(diretorioWeb + File.separator + "relatorio" + File.separator + "linhaDigitavel");
			        if(!diretorio.exists()) {
			        	diretorio.mkdirs();
			        }
			        try {  
			        	ImageIO.write(imageBuffer, "PNG", new File(diretorioWeb + File.separator + "relatorio" + File.separator + "linhaDigitavel" + File.separator + nomeImagem));  
			        } catch (IOException ex) {  
			            ex.getMessage();
			        }            			
			        boleto.setContaReceber_diretorioImgLinhaDigitavel(diretorioWeb + File.separator + "relatorio" + File.separator + "linhaDigitavel" + File.separator + nomeImagem);
				}
			}		
			String nomeBoleto = BoletoBancarioRel.getIdEntidade();
			String nomUsuarioPessoaBoleto = "";
			String nomeUnidadeEnsinoBoleto = "";
			if(!lista.isEmpty()) {
				nomeBoleto += ("_" + lista.get(0).getContareceber_codigo() + "_");
				nomeUnidadeEnsinoBoleto = lista.get(0).getContareceber_razaoSocialMantenedora();
				nomUsuarioPessoaBoleto = lista.get(0).getPessoa_nome();
			}	
			if (loginControle != null && codigoUsuario != null) {
				apresentarRelatorioObjetos(nomeBoleto, titulo, getNomeUnidadeEnsino(), loginControle.getUrlFisicoLogoUnidadeEnsinoRelatorio(), loginControle.getUrlFisicoLogoUnidadeEnsino(), "", "PDF",
                    "", design, getNomeUsuario(), "", lista, BoletoBancarioRel.getCaminhoBaseRelatorio(), logoBanco, "", "", "true", "BoletoBancarioSV1", codigoUsuario.toString(), request, response);
			} else if (loginControle != null && codigoUsuario == null) {
				apresentarRelatorioObjetos(nomeBoleto, titulo, nomeUnidadeEnsinoBoleto, loginControle.getUrlFisicoLogoUnidadeEnsinoRelatorio(), loginControle.getUrlFisicoLogoUnidadeEnsino(), "", "PDF",
	                    "", design, nomUsuarioPessoaBoleto, "", lista, BoletoBancarioRel.getCaminhoBaseRelatorio(), logoBanco, "", "", "true", "BoletoBancarioSV2", "", request, response);
			} else {
				apresentarRelatorioObjetos(nomeBoleto, titulo, "", "", "/resources/imagens/topoLogoVisao2.png", "", "PDF",
	                    "", design, "", "", lista, BoletoBancarioRel.getCaminhoBaseRelatorio(), logoBanco, "", "", "true", "BoletoBancarioSV3", "", request, response);				
			}
            Uteis.removerObjetoMemoria(this);
        } catch (Exception e) {
            e.getMessage();
            realizarCriacaoMensagemErro(request, response, e.getMessage());
            request.getSession().setAttribute("ERRO", e);            
        } finally {
            titulo = null;
            design = null;
            Uteis.liberarListaMemoria(lista);
            if (loginControle != null) {
            	loginControle.setUrlLogoUnidadeEnsino(urlTempLogoUnidadeEnsino);
            	loginControle.setUrlLogoUnidadeEnsinoRelatorio(urlTempLogoUnidadeEnsinoRelatorio);
            }
        }
    }
    
	public void realizarCriacaoMensagemErro(HttpServletRequest req, HttpServletResponse res, String erro) throws ServletException, IOException {
		PrintWriter out = res.getWriter();
		out.println("<html>");
		out.println("<head>");
		out.println("<link href='resources/css/otimize2.css' rel='stylesheet' type='text/css'>");
		out.println("</head>");
		out.println("<body>");
		out.println("<span class='mensagemDetalhada'>" + erro + "</span>");
		out.println("</body>");
		out.println("</html>");
		if(req.getParameter("telaOrigem") != null) {
			res.sendRedirect(req.getParameter("telaOrigem"));
		}		
	}

	/** 
     * Cria um objeto da classe que gera um grÃ¡fico em pizza. 
     * @param values Array de valores inteiros a serem representados. 
     * @param width Largura da imagem. 
     * @param height Altura da imagem. 
     * @param background Cor de fundo da imagem. 
     */  
    public void criarImagem( int[] values, int width, int height, Color background ) {  
        if( values==null || values.length<1 ||  
            width<0 || height<0 ||  
            background==null ) throw new IllegalArgumentException();  
        this.values = values;  
        this.width = width;  
        this.height = height;  
        this.background = background;  
        createChart();  
    }  
      
    /** 
     * Cria a imagem internamente. 
     */  
    private void createChart() {  
        imageBuffer = new BufferedImage( width, height, BufferedImage.TYPE_INT_RGB );  
        Graphics g = imageBuffer.createGraphics();  
        g.setColor( background );  
        g.fillRect( 0, 0, width, height );  
//        int arc = 0;  
//        int[] sizes = calculateAngles( values );  
//        for(int i=0, j=0; i<sizes.length; i++, j++) {  
//            if( j==10 ) j = 0;  
//            g.setColor( colors[j] );  
//            g.fillArc( 0, 0, width, height, arc, sizes[i] );  
//            arc += sizes[i];  
//        }
        Font font = new Font("Courier", Font.BOLD, 25);  
        g.setColor( Color.BLACK );  
        g.setFont( font );  
        g.drawString("10490.02890 05668.702433 89102.015008 3 66080000091300",0,10);        
    }  
      
    /** 
     * Calcula os Ã¢ngulos para cada valor informado. 
     * @param values Valores a terem seus Ã¢ngulos calculados. 
     * @return Array de int com os Ã¢ngulos para cada valor. 
     */  
    private int[] calculateAngles( int[] values ) {  
        int[] angles = new int[ values.length ];  
        int total = 0;  
        //calcula a somatÃ³ria total dos valores  
        for( int i=0; i<values.length; i++ ) {  
            total += values[i];  
        }  
        //calcula os Ã¢ngulos para cada pedaÃ§o  
        for( int i=0; i<values.length; i++ ) {  
            angles[i] = (360 * values[i]) / total;  
        }  
        
        return angles;  
    }  
      
    /** 
     * Retorna a imagem do grÃ¡fico em pizza. 
     * @return Retorna um objeto do tipo ImageIcon. 
     */  
    public ImageIcon getImageIcon() {  
        return new ImageIcon( imageBuffer );  
    }  
      
    /** 
     * Retorna o buffer da imagem do grÃ¡fico em pizza. 
     * @return Retorna um objeto do tipo BufferedImage. 
     */  
    public BufferedImage getBufferedImage() {  
        return imageBuffer;  
    }  
	
    /** in
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	Integer codigoContaReceber = null;
    	Integer codigoUsuario = null;
    	String telaOrigem = null;
        codigoContaReceber = Integer.parseInt(request.getParameter("codigoContaReceber"));
        try {
        contaReceberAgrupada = Integer.parseInt(request.getParameter("contaAgrupada"));
        } catch (Exception e) {
        	contaReceberAgrupada = null;        	
        }
        tipoBoleto = request.getParameter("tipoBoleto");
        try {
	        String permiteImprimirBoletoRecebido = request.getParameter("permiteImprimirBoletoRecebido");
	        if (permiteImprimirBoletoRecebido != null && permiteImprimirBoletoRecebido.equalsIgnoreCase("true")) {
	        	setPermiteImprimirBoletoRecebidoBoolean(Boolean.TRUE);
	        }
        } catch (Exception e) {
        	setPermiteImprimirBoletoRecebidoBoolean(null);
        }
        try {
        	telaOrigem = request.getParameter("telaOrigem");        	
        } catch (Exception e) {        	
        }
        LoginControle loginControle = (LoginControle) request.getSession().getAttribute("LoginControle");
        boolean usuarioAluno = false;
        if((telaOrigem != null && telaOrigem.equals("visaoAluno/minhasContasPagarAluno.xhtml")) || (loginControle != null && loginControle.getUsuario() != null && loginControle.getUsuario().getIsApresentarVisaoAlunoOuPais())){
        	usuarioAluno = true;
        }
        try {
        	codigoUsuario = Integer.parseInt(request.getParameter("codigoUsuario"));
        } catch (Exception e) {        	
        }
        if(!Uteis.isAtributoPreenchido(codigoUsuario) &&  loginControle != null && loginControle.getUsuario() != null &&  Uteis.isAtributoPreenchido(loginControle.getUsuario().getCodigo())) {
        	codigoUsuario = loginControle.getUsuario().getCodigo();        	
        }
        boletoBancarioRel = new BoletoBancarioRel();
        if(usuarioAluno){
        	imprimirPDF(request, response, codigoContaReceber, codigoUsuario, true);
    	}else{
    		imprimirPDF(request, response, codigoContaReceber, codigoUsuario, false);	
    }
    }
  
    
    public String getCaminhoPastaWeb() {
        String diretorioPastaWeb = null;
        ServletContext servletContext = (ServletContext) this.getServletContext();
        diretorioPastaWeb = servletContext.getRealPath("");
        return diretorioPastaWeb;
    }
    // <editor-fold defaultstate="collapsed" desc="Métodos HttpServlet. Clique no sinal de + ï¿½ esquerda para editar o cï¿½digo.">

    /**
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

	public String getTipoBoleto() {
		if(tipoBoleto == null){
			tipoBoleto = "boleto";
		}
		return tipoBoleto;
	}

	public void setTipoBoleto(String tipoBoleto) {
		this.tipoBoleto = tipoBoleto;
	}

	public Boolean getPermiteImprimirBoletoRecebidoBoolean() {
		if (permiteImprimirBoletoRecebidoBoolean == null) {
			permiteImprimirBoletoRecebidoBoolean = Boolean.FALSE;
		}
		return permiteImprimirBoletoRecebidoBoolean;
	}

	public void setPermiteImprimirBoletoRecebidoBoolean(Boolean permiteImprimirBoletoRecebidoBoolean) {
		this.permiteImprimirBoletoRecebidoBoolean = permiteImprimirBoletoRecebidoBoolean;
	}
    
    
}
