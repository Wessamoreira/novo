package controle.faturamento.nfe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.faturamento.nfe.ConfiguracaoNotaFiscalVO;
import negocio.comuns.faturamento.nfe.LoteInutilizacaoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.faturamento.nfe.AmbienteNfeEnum;

@Controller("InutilizarNotaFiscalControle")
@Scope("viewScope")
@Lazy
public class InutilizarNotaFiscalControle extends SuperControle implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

		private LoteInutilizacaoVO loteInutilizacao;
		protected List listaSelectItemUnidadeEnsino;
		private String campoConsulta;
		private Integer numeroNotaConsulta;
		private Date dataInicial;
		private Date dataFinal;
		private List<LoteInutilizacaoVO> lotesInutilizacaoVO;
		
		public InutilizarNotaFiscalControle() throws Exception{
			montarListaSelectItemUnidadeEnsino(0);
		}
		

	    public String novo() throws Exception {
	    	loteInutilizacao = new LoteInutilizacaoVO();
	    	loteInutilizacao.setDataInutilizacao(new Date());
	        setMensagemID("msg_entre_dados");
	        return Uteis.getCaminhoRedirecionamentoNavegacao("inutilizacaoNumeroNotaFiscalForm");
	    }
	    
	    public String inicializarConsultar() {
	    	setLotesInutilizacaoVO(null);
	        setMensagemID("msg_entre_prmconsulta");
	        return Uteis.getCaminhoRedirecionamentoNavegacao("inutilizacaoNumeroNotaFiscalCons");
	    }
	    
	    public String editar() {
	    	try {
		        setLoteInutilizacao((LoteInutilizacaoVO) context().getExternalContext().getRequestMap().get("itemLote"));
		        setMensagemID("msg_entre_dados");
		        return Uteis.getCaminhoRedirecionamentoNavegacao("inutilizacaoNumeroNotaFiscalForm");
	    	} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
				return Uteis.getCaminhoRedirecionamentoNavegacao("inutilizacaoNumeroNotaFiscalCons");
			}
	    }
	    
		public void executarInutilizacaoNota(){
			ConfiguracaoGeralSistemaVO conGeralSistemaVO;
			ConfiguracaoGeralSistemaVO confGeralSistemaWebserviceVO;
			try {
				validarDados();
				conGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesLocalUploadArquivoFixo();
				confGeralSistemaWebserviceVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesWebserviceNFe();
				getLoteInutilizacao().setUnidadeEnsinoVO(getAplicacaoControle().getUnidadeEnsinoVO(getLoteInutilizacao().getUnidadeEnsinoVO().getCodigo(), getUsuarioLogado()));				
				getLoteInutilizacao().setUsuarioVO(getUsuarioLogadoClone());
//				getFacadeFactory().getInutilizacaoNotaInterfaceFacade().inutilizarNota(getLoteInutilizacao(), conGeralSistemaVO, getUsuarioLogado());
				getFacadeFactory().getInutilizacaoNotaInterfaceFacade().inutilizarNotaWebservice(getLoteInutilizacao(), confGeralSistemaWebserviceVO, conGeralSistemaVO, getUsuarioLogado());
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
			
		}
		
		public void validarDados() throws Exception{
			if(getLoteInutilizacao().getUnidadeEnsinoVO().getCodigo() == null || getLoteInutilizacao().getUnidadeEnsinoVO().getCodigo() == 0){
				throw new Exception("A UNIDADE DE ENSINO é obrigatoria.");
			}
			if(getLoteInutilizacao().getNrInicial() <= 0){
				throw new Exception("O Número INICIAL é obrigatorio.");
			}
			if(getLoteInutilizacao().getNrfinal() <= 0){
				throw new Exception("O Número FINAL é obrigatorio.");
			}
			if(getLoteInutilizacao().getNrInicial() > getLoteInutilizacao().getNrfinal()){
				throw new Exception("O Número FINAL deve ser maior ou igual o INICIAL.");
			}
			if(getLoteInutilizacao().getMotivo().length() < 15 || getLoteInutilizacao().getMotivo().length() > 255){ // <=
				throw new Exception("O campo MOTIVO deve ter entre 15 a 255 caracteres.");
			}
			
			ConfiguracaoNotaFiscalVO configuracaoNotaFiscalVO = getFacadeFactory().getConfiguracaoNotaFiscalFacade().consultarPorUnidadeEnsino(getLoteInutilizacao().getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			
			if (AmbienteNfeEnum.PRODUCAO.equals(configuracaoNotaFiscalVO.getAmbienteNfeEnum())) {
				if(getLoteInutilizacao().getNrInicial() > configuracaoNotaFiscalVO.getNumeroNota() || getLoteInutilizacao().getNrfinal() > configuracaoNotaFiscalVO.getNumeroNota()){
					throw new Exception("A faixa de Número para inutilização não pode ser maior que o numero atual de Nota Fiscal.");
				}
			}
		}
		
		public void validarDadosFiltros() throws Exception{
			if(getCampoConsulta().equals("periodo")){
				if(getDataInicial() == null ){
					throw new Exception("A Data Inicial é obrigatoria.");
				}
				if(getDataFinal() == null){
					throw new Exception("A Data Final é obrigatoria.");
				}
			}
			if(getCampoConsulta().equals("numeroNota")){
				if(getNumeroNotaConsulta() == null){
					throw new Exception("O Número da Nota é obrigatoria.");
				}
			}
		}
		
		public void consultarLoteInutilizacao(){
			try {
				validarDadosFiltros();
				if(getCampoConsulta().equals("periodo")){
					setLotesInutilizacaoVO(getFacadeFactory().getInutilizacaoNotaInterfaceFacade().buscarPorPerido(getDataInicial(), getDataFinal(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
				}else if(getCampoConsulta().equals("numeroNota")){
					setLotesInutilizacaoVO(getFacadeFactory().getInutilizacaoNotaInterfaceFacade().buscarNumeroNota(getNumeroNotaConsulta(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
				}
				setMensagemID("msg_dados_consultados");
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
		}
		
		public void montarListaSelectItemUnidadeEnsino(Integer prm) throws Exception {
			List resultadoConsulta = null;
			Iterator i = null;
			try {
				if (getUnidadeEnsinoLogado().getCodigo().intValue() != 0) {
					setListaSelectItemUnidadeEnsino(new ArrayList<SelectItem>());
					getListaSelectItemUnidadeEnsino().add(new SelectItem(getUnidadeEnsinoLogado().getCodigo(), getUnidadeEnsinoLogado().getNome()));
					return;
				}
				resultadoConsulta = consultarUnidadeEnsinoPorCodigo(prm);
				i = resultadoConsulta.iterator();
				List objs = new ArrayList(0);
				objs.add(new SelectItem(0, ""));
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
		
		public List consultarUnidadeEnsinoPorCodigo(Integer numeroPrm) throws Exception {
			List lista = null;
			if ((numeroPrm != null && !numeroPrm.equals(0)) || (getUnidadeEnsinoLogado().getCodigo() != null && !getUnidadeEnsinoLogado().getCodigo().equals(0))) {
				lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorCodigo(numeroPrm, getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			} else {
				lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarTodasUnidades(false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			return lista;
		}

		public List getListaSelectItemUnidadeEnsino() {
			return listaSelectItemUnidadeEnsino;
		}

		public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
			this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
		}
		
		public List getTipoConsultaCombo() {
	        List itens = new ArrayList(0);
	        itens.add(new SelectItem("numeroNota", "Numero da Nota"));
	        itens.add(new SelectItem("periodo", "Período"));
	        return itens;
	    }
		
		
		public String getCampoConsulta() {
			if(campoConsulta == null){
				campoConsulta = "periodo";
			}
			return campoConsulta;
		}

		public void setCampoConsulta(String campoConsulta) {
			this.campoConsulta = campoConsulta;
		}

		public boolean getIsPerido(){
			if(getCampoConsulta().equals("periodo")){
				return true;
			}
			return false;
		}
		
		public boolean getIsNumeroNota(){
			if(getCampoConsulta().equals("numeroNota")){
				return true;
			}
			return false;
		}

		public Integer getNumeroNotaConsulta() {
			if(numeroNotaConsulta == null){
				numeroNotaConsulta =0;
			}
			return numeroNotaConsulta;
		}

		public void setNumeroNotaConsulta(Integer numeroNotaConsulta) {
			this.numeroNotaConsulta = numeroNotaConsulta;
		}


		public LoteInutilizacaoVO getLoteInutilizacao() {
			if(loteInutilizacao == null){
				loteInutilizacao = new LoteInutilizacaoVO();
			}
			return loteInutilizacao;
		}

		public void setLoteInutilizacao(LoteInutilizacaoVO loteInutilizacao) {
			this.loteInutilizacao = loteInutilizacao;
		}

		public Date getDataInicial() {
			return dataInicial;
		}

		public void setDataInicial(Date dataInicial) {
			this.dataInicial = dataInicial;
		}

		public Date getDataFinal() {
			return dataFinal;
		}

		public void setDataFinal(Date dataFinal) {
			this.dataFinal = dataFinal;
		}

		public List<LoteInutilizacaoVO> getLotesInutilizacaoVO() {
			if(lotesInutilizacaoVO == null){
				lotesInutilizacaoVO = new ArrayList<LoteInutilizacaoVO>(0);
			}
			return lotesInutilizacaoVO;
		}

		public void setLotesInutilizacaoVO(List<LoteInutilizacaoVO> lotesInutilizacaoVO) {
			this.lotesInutilizacaoVO = lotesInutilizacaoVO;
		}
	
		
		
	}
