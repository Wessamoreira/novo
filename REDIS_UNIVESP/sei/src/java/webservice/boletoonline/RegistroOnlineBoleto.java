package webservice.boletoonline;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.sun.jersey.api.client.ClientResponse.Status;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.ControleRemessaContaReceberVO;
import negocio.comuns.financeiro.ControleRemessaVO;
import negocio.comuns.financeiro.enumerador.SituacaoControleRemessaContaReceberEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.Bancos;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import relatorio.negocio.comuns.financeiro.BoletoBancarioRelVO;
import webservice.boletoonline.bancoBrasil.BancoBrasilBoletoOnline;
import webservice.boletoonline.bancoBrasil.comuns.RegistroRetornoBoletoBb;
import webservice.boletoonline.bradesco.BradescoBoletoOnline;
import webservice.boletoonline.bradesco.classes.RegistroRetornoBoleto;
import webservice.boletoonline.caixaEconomicaFederal.CaixaEconomicaFederalBoletoOnline;
import webservice.boletoonline.itau.ItauBoletoOnline;
import webservice.boletoonline.santander.SantanderBoletoOnline;
import webservice.boletoonline.sicredi.SicrediBoletoOnline;


public class RegistroOnlineBoleto extends ControleAcesso {

	public RegistroOnlineBoleto() {
	}

	/*
	 * Metodo responsavel por verificar qual banco será acionado no momento do
	 * registro online, hoje o SEI dispoe de registro online do bradesco. Mais
	 * futuramente outros bancos estaram disponiveis.
	 */
	public void enviarBoletoRemessaOnlineLista(List<BoletoBancarioRelVO> listaBoletoBancarioRelVO, ConfiguracaoFinanceiroVO config, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configGeral) throws Exception {
		Iterator i = listaBoletoBancarioRelVO.iterator();
		boolean erro = false;
		while (i.hasNext()) {
			BoletoBancarioRelVO boleto = (BoletoBancarioRelVO) i.next();
			enviarBoletoRemessaOnlineLista(boleto, config, usuario, configGeral);			
		}
	}
	
	/*
	 * Metodo responsavel por verificar qual banco será acionado no momento do
	 * registro online, hoje o SEI dispoe de registro online do bradesco. Mais
	 * futuramente outros bancos estaram disponiveis.
	 */
	public void enviarBoletoRemessaOnlineLista(BoletoBancarioRelVO boleto, ConfiguracaoFinanceiroVO config, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configGeral) throws Exception {		
		if (boleto.getContareceber_situacao().equals("AR") && boleto.getContacorrente_habilitarRegistroRemessaOnline()) {
			if(!Uteis.isAtributoPreenchido(boleto.getContaCorrenteVO())) {
				boleto.setContaCorrenteVO(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(boleto.getContareceber_contacorrente(), false, Uteis.NIVELMONTARDADOS_TODOS, usuario));
			}
			if (boleto.getTipoOrigemContaReceberAptaGerarRemessaOnline()) {
				boolean erro = false;
				String motivo = "";
				ContaReceberVO obj = new ContaReceberVO();
				obj.setCodigo(boleto.getContareceber_codigo());
				getFacadeFactory().getContaReceberFacade().carregarDados(obj, config, usuario);
				List<ControleRemessaContaReceberVO> lista = new ArrayList<>();
				List<ContaCorrenteVO> listaContaCorrente = new ArrayList<>();
				listaContaCorrente.add(obj.getContaCorrenteVO());
				ControleRemessaContaReceberVO crcr = getFacadeFactory().getControleRemessaContaReceberFacade().consultaRapidaContaArquivoRemessaPorNossoNumeroContaReceber(listaContaCorrente, obj.getNossoNumero());				
				if (boleto.getContareceber_datavencimentodiautil().after(Uteis.obterDataAntiga(new Date(), 1)) 
						|| (Uteis.isAtributoPreenchido(boleto.getDataVencimentoRemessaOnline()) && boleto.getDataVencimentoRemessaOnline().after(Uteis.obterDataAntiga(new Date(), 1))) 
						|| Uteis.isAtributoPreenchido(obj.getProcessamentoIntegracaoFinanceiraDetalheVO())) {
					ControleRemessaVO cr = new ControleRemessaVO();
					cr.setContaCorrenteVO(obj.getContaCorrenteVO());					
					//obj.setDataVencimento(boleto.getContareceber_datavencimento());
					//obj.criarBoleto(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(obj.getContaCorrente(), false, Uteis.NIVELMONTARDADOS_TODOS, usuario));//Rotina adiciona pois as informações do codigo de barra e linha digitival estao desatualizado sendo necessario executar em tempo de execucao para boleto do ITAU.Pedro Andrade.					
				   	if (Uteis.isAtributoPreenchido(obj.getProcessamentoIntegracaoFinanceiraDetalheVO())) {
		                if(getFacadeFactory().getIntegracaoFinanceiroFacade().realizarVerificacaoProcessamentoIntegracaoFinanceira()){
		             	   throw new Exception("Prezado, a geração de remessa dos boletos está indisponível temporariamente, tente mais tarde.");
		         	    }
						lista = getFacadeFactory().getContaReceberFacade().consultaRapidaContasArquivoRemessaEntreDatasUtilizandoIntegracaoFinanceira(boleto.getContareceber_nossonumero(), obj.getDataVencimento(), obj.getDataVencimento(), obj.getUnidadeEnsino().getCodigo(), cr, null, config, usuario);
		        	} else {
		        		lista = getFacadeFactory().getContaReceberFacade().consultaRapidaContasArquivoRemessaEntreDatas(boleto.getContareceber_nossonumero(), boleto.getContareceber_codigo(),boleto.getContareceber_datavencimento(), boleto.getContareceber_datavencimento(),   
								obj.getUnidadeEnsino().getCodigo(), cr, null, false, config, usuario);
		        	}					
					if (!lista.isEmpty()) {
						if(boleto.getDataVencimentoRemessaOnline() != null) {
							obj.setDataVencimento(boleto.getDataVencimentoRemessaOnline());
							lista.get(0).setDataVencimento(boleto.getDataVencimentoRemessaOnline());
						}else if(boleto.getContareceber_datavencimentodiautil() != null && !Uteis.isAtributoPreenchido(obj.getProcessamentoIntegracaoFinanceiraDetalheVO()) && Uteis.compararDatasSemConsiderarHoraMinutoSegundo(boleto.getContareceber_datavencimentodiautil(), obj.getDataVencimento())) {
							obj.setDataVencimento(boleto.getContareceber_datavencimentodiautil());
							lista.get(0).setDataVencimento(boleto.getContareceber_datavencimentodiautil());
						}
						if(boleto.getValorAcrescimoRemessaOnline() != null) {
							obj.setAcrescimo(Uteis.arrendondarForcando2CadasDecimais(obj.getAcrescimo() + boleto.getValorAcrescimoRemessaOnline()));							
							lista.get(0).setAcrescimo(Uteis.arrendondarForcando2CadasDecimais(lista.get(0).getAcrescimo()+boleto.getValorAcrescimoRemessaOnline()));
							lista.get(0).setValorComAcrescimo(null);
							lista.get(0).setValorBaseComAcrescimo(null);
						}
						obj.setUnidadeEnsino(getAplicacaoControle().getUnidadeEnsinoVO(obj.getUnidadeEnsino().getCodigo(), usuario));
						if (!crcr.getSituacaoControleRemessaContaReceber().equals(SituacaoControleRemessaContaReceberEnum.REMETIDA)) {							
							 //Rotina adiciona pois as informações do codigo de barra e linha digitival estao desatualizado sendo necessario executar em tempo de execucao para boleto do ITAU.Pedro Andrade. atualizada pelo edson 					
							if(obj.getContaCorrenteVO().getCarteiraRegistrada() && obj.getContaCorrenteVO().getGerarRemessaSemDescontoAbatido()) {	
								obj.setDataVencimento(boleto.getContareceber_datavencimento());
								obj.criarBoleto(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(obj.getContaCorrente(), false, Uteis.NIVELMONTARDADOS_TODOS, usuario));

							}else {
								obj.setLinhaDigitavelCodigoBarras(boleto.getContareceber_linhadigitavelcodigobarras());
								obj.setCodigoBarra(boleto.getContareceber_codigobarra());
								
							}
							this.enviarBoletoRemessaOnline(obj, lista.get(0), configGeral, config, usuario);							
						} 
					} else {
						if(!crcr.getSituacaoControleRemessaContaReceber().equals(SituacaoControleRemessaContaReceberEnum.AGUARDANDO_PROCESSAMENTO)){
							erro = true;					
						}				
					}
				} else {
					if(!boleto.getContareceber_datavencimentodiautil().after(Uteis.obterDataAntiga(new Date(), 1)) && !crcr.getSituacaoControleRemessaContaReceber().equals(SituacaoControleRemessaContaReceberEnum.REMETIDA)) {												
						crcr.setMotivoEstorno("Não é possível registrar o boleto vencido no banco.");
						motivo += "Não é possível registrar o boleto vencido no banco.";
						erro = true;
					}
					
				//	erro = true;
				}
				if (erro == true) {
					if (!obj.getContaCorrenteVO().getPermitirEmissaoBoletoRemessaOnlineRejeita() &&
							obj.getContaCorrenteVO().getHabilitarRegistroRemessaOnline() &&
								obj.getTipoOrigemContaReceberAptaGerarRemessaOnline()) {
						// possui nao remessa
						if (lista.isEmpty() && crcr.getSituacaoControleRemessaContaReceber().equals(SituacaoControleRemessaContaReceberEnum.AGUARDANDO_PROCESSAMENTO) && motivo.isEmpty()) {
							//throw new Exception("Não foi possível registrar a remessa online do boleto! A impressão do boleto não pode ser realizada! Pois existe uma Remessa Manual para o nosso número! " + crcr.getNossoNumero());							
						} else if (lista.isEmpty() && crcr.getSituacaoControleRemessaContaReceber().equals(SituacaoControleRemessaContaReceberEnum.AGUARDANDO_PROCESSAMENTO) && !motivo.isEmpty()) {
							throw new Exception("Não foi possível registrar a remessa online do boleto! A impressão do boleto não pode ser realizada! " + motivo);							
						} else if (!crcr.getSituacaoControleRemessaContaReceber().equals(SituacaoControleRemessaContaReceberEnum.REMETIDA)) {
							throw new Exception("Não foi possível registrar a remessa online do boleto! A impressão do boleto não pode ser realizada! Procure a Instituição de Ensino para mais informações! " + crcr.getMotivoEstorno());							
						} 
					}				
				}
			}
		}
	}

	/*
	 * Metodo responsavel por verificar qual banco será acionado no momento do
	 * registro online, hoje o SEI dispoe de registro online do bradesco. Mais
	 * futuramente outros bancos estaram disponiveis.
	 */
	public void enviarBoletoRemessaOnline(ContaReceberVO contareceberVO, ControleRemessaContaReceberVO crcrVO, ConfiguracaoGeralSistemaVO config, ConfiguracaoFinanceiroVO configFin, UsuarioVO usuario) throws Exception {
		try {
			if (contareceberVO.getSituacaoAReceber()
					&& contareceberVO.getContaCorrenteVO().getHabilitarRegistroRemessaOnline()
					&& contareceberVO.getTipoOrigemContaReceberAptaGerarRemessaOnline()) {
				if (contareceberVO.getContaCorrenteVO().getAgencia().getBanco().getNrBanco().equals(Bancos.BRADESCO.getNumeroBanco())) {
					enviarBoletoRemessaOnlinePorBradesco(contareceberVO, crcrVO, config, configFin, usuario);
				}
				if (contareceberVO.getContaCorrenteVO().getAgencia().getBanco().getNrBanco().equals(Bancos.SANTANDER.getNumeroBanco())) {
					enviarBoletoRemessaOnlinePorSantander(contareceberVO, crcrVO, config, configFin, usuario);
				}
				
				if (contareceberVO.getContaCorrenteVO().getAgencia().getBanco().getNrBanco().equals(Bancos.ITAU.getNumeroBanco()) ) {
					enviarBoletoRemessaOnlinePorItau(contareceberVO, crcrVO, config, configFin, usuario);
				}				
				if (contareceberVO.getContaCorrenteVO().getAgencia().getBanco().getNrBanco().equals(Bancos.CAIXA_ECONOMICA_FEDERAL.getNumeroBanco())) {
					enviarBoletoRemessaOnlinePorCaixaEconomicaFederal(contareceberVO, crcrVO, config, configFin, usuario);
				}
				if (contareceberVO.getContaCorrenteVO().getAgencia().getBanco().getNrBanco().equals(Bancos.SICRED.getNumeroBanco())) {
					enviarBoletoRemessaOnlinePorSicredi(contareceberVO, crcrVO, config, configFin, usuario);
				}
				if (contareceberVO.getContaCorrenteVO().getAgencia().getBanco().getNrBanco().equals(Bancos.BANCO_DO_BRASIL.getNumeroBanco())) {
					enviarBoletoRemessaOnlinePorBancoBrasil(contareceberVO, crcrVO, config, configFin, usuario);
				}
			}
		} catch (Exception e) {
			if (contareceberVO.getContaCorrenteVO().getPermitirEmissaoBoletoRemessaOnlineRejeita()) {
				return;
			} else {
				throw e;
			}
		}
	}

	private void enviarBoletoRemessaOnlinePorItau(ContaReceberVO contareceberVO, ControleRemessaContaReceberVO crcrVO, ConfiguracaoGeralSistemaVO config, ConfiguracaoFinanceiroVO configFin, UsuarioVO usuario) throws Exception {
		String motivo = "";
		Boolean ocorreuErroNoRegistro = false;
		try {
			ItauBoletoOnline itau = new ItauBoletoOnline(contareceberVO, crcrVO, config, configFin);
			itau.enviarBoletoRemessaOnlineItau();
			crcrVO.setSituacaoControleRemessaContaReceber(SituacaoControleRemessaContaReceberEnum.REMETIDA);
		} catch (Exception e) {
			motivo = e.getMessage();
			ocorreuErroNoRegistro = true;
			crcrVO.setSituacaoControleRemessaContaReceber(SituacaoControleRemessaContaReceberEnum.ESTORNADO);
			crcrVO.setMotivoEstorno(e.getMessage());
			crcrVO.setUsuarioEstorno(usuario);
			crcrVO.setDataEstorno(new Date());
		}
		getFacadeFactory().getControleRemessaFacade().incluirControleOnline(contareceberVO, crcrVO, usuario);
		if (ocorreuErroNoRegistro) {
			throw new Exception("Não foi possível registrar a remessa online do boleto! A impressão do boleto não pode ser realizada! Procure a Instituição de Ensino para mais informações! " + motivo);
		}
	}

	private void enviarBoletoRemessaOnlinePorSantander(ContaReceberVO contareceberVO, ControleRemessaContaReceberVO crcrVO, ConfiguracaoGeralSistemaVO config, ConfiguracaoFinanceiroVO configFin, UsuarioVO usuario) throws Exception {
		String motivo = "";
		Boolean ocorreuErroNoRegistro = false;
		try {
			SantanderBoletoOnline santander = new SantanderBoletoOnline(contareceberVO, crcrVO, config, configFin);
			santander.enviarBoletoRemessaOnlineSantander();
			crcrVO.setSituacaoControleRemessaContaReceber(SituacaoControleRemessaContaReceberEnum.REMETIDA);
		} catch (Exception e) {
			motivo = e.getMessage();
			ocorreuErroNoRegistro = true;
			crcrVO.setSituacaoControleRemessaContaReceber(SituacaoControleRemessaContaReceberEnum.ESTORNADO);
			crcrVO.setMotivoEstorno(e.getMessage());
			crcrVO.setUsuarioEstorno(usuario);
			crcrVO.setDataEstorno(new Date());
		}
		getFacadeFactory().getControleRemessaFacade().incluirControleOnline(contareceberVO, crcrVO, usuario);
		if (ocorreuErroNoRegistro) {
			throw new Exception("Não foi possível registrar a remessa online do boleto! A impressão do boleto não pode ser realizada! Procure a Instituição de Ensino para mais informações! " + motivo);
		}
	}

	private void enviarBoletoRemessaOnlinePorBradesco(ContaReceberVO contareceberVO, ControleRemessaContaReceberVO crcrVO, ConfiguracaoGeralSistemaVO config, ConfiguracaoFinanceiroVO configFin, UsuarioVO usuario) throws Exception {
		try {
			BradescoBoletoOnline bradesco = new BradescoBoletoOnline(contareceberVO, crcrVO, config, configFin);
			RegistroRetornoBoleto reg = bradesco.enviarBoletoRemessaOnlineBradesco();
			if (!reg.getCdErro().equals("") && !reg.getCdErro().equals("0") && !reg.getMsgErro().contains("69")) {
				crcrVO.setSituacaoControleRemessaContaReceber(SituacaoControleRemessaContaReceberEnum.ESTORNADO);
				crcrVO.setMotivoEstorno(reg.getCdErro() + " - " + reg.getMsgErro());
				crcrVO.setUsuarioEstorno(usuario);
				crcrVO.setDataEstorno(new Date());
				getFacadeFactory().getControleRemessaContaReceberFacade().realizarEstorno(crcrVO, usuario);
			}
			if (!contareceberVO.getContaCorrenteVO().getPermitirEmissaoBoletoRemessaOnlineRejeita() && !reg.getCdErro().equals("") && !reg.getCdErro().equals("0") && !reg.getMsgErro().contains("69")) {
				throw new Exception("Não foi possível registrar a remessa online do boleto! A impressão do boleto não pode ser realizada! Procure a Instituição de Ensino para mais informações! " + reg.getMsgErro());
			}
			crcrVO.setSituacaoControleRemessaContaReceber(SituacaoControleRemessaContaReceberEnum.REMETIDA);
			getFacadeFactory().getControleRemessaFacade().incluirControleOnline(contareceberVO, crcrVO, usuario);
		} catch (Exception e) {
			crcrVO.setSituacaoControleRemessaContaReceber(SituacaoControleRemessaContaReceberEnum.ESTORNADO);
			crcrVO.setMotivoEstorno(e.getMessage());
			crcrVO.setUsuarioEstorno(usuario);
			crcrVO.setDataEstorno(new Date());
			getFacadeFactory().getControleRemessaFacade().incluirControleOnline(contareceberVO, crcrVO, usuario);
			throw new Exception("Não foi possível registrar a remessa online do boleto! A impressão do boleto não pode ser realizada! Procure a Instituição de Ensino para mais informações! " + e.getMessage());
		}
	}
	private void enviarBoletoRemessaOnlinePorCaixaEconomicaFederal(ContaReceberVO contareceberVO, ControleRemessaContaReceberVO crcrVO, ConfiguracaoGeralSistemaVO config, ConfiguracaoFinanceiroVO configFin, UsuarioVO usuario) throws Exception {
		String motivo = "";
		Boolean ocorreuErroNoRegistro = false;
		try {
			CaixaEconomicaFederalBoletoOnline caixaEconomica = new  CaixaEconomicaFederalBoletoOnline(contareceberVO, crcrVO, config, configFin);
			caixaEconomica.enviarBoletoRemessaOnlineCaixaEconomica();
			crcrVO.setSituacaoControleRemessaContaReceber(SituacaoControleRemessaContaReceberEnum.REMETIDA);
		} catch (Exception e) {
			e.printStackTrace();
			motivo = e.getMessage();
			ocorreuErroNoRegistro = true;
			crcrVO.setSituacaoControleRemessaContaReceber(SituacaoControleRemessaContaReceberEnum.ESTORNADO);
			crcrVO.setMotivoEstorno(e.getMessage());
			crcrVO.setUsuarioEstorno(usuario);
			crcrVO.setDataEstorno(new Date());
		}
		getFacadeFactory().getControleRemessaFacade().incluirControleOnline(contareceberVO, crcrVO, usuario);
		if (ocorreuErroNoRegistro) {
			throw new Exception("Não foi possível registrar a remessa online do boleto! A impressão do boleto não pode ser realizada! Procure a Instituição de Ensino para mais informações! " + motivo);
		}
	}
	
	private void enviarBoletoRemessaOnlinePorSicredi(ContaReceberVO contareceberVO, ControleRemessaContaReceberVO crcrVO, ConfiguracaoGeralSistemaVO config, ConfiguracaoFinanceiroVO configFin, UsuarioVO usuario) throws Exception {
		String motivo = "";
		Boolean ocorreuErroNoRegistro = false;
		try {
			SicrediBoletoOnline sicrediBoletoOnline = new  SicrediBoletoOnline(contareceberVO, crcrVO, config, configFin);
			sicrediBoletoOnline.enviarBoletoRemessaOnlineSicredi(usuario);
			crcrVO.setSituacaoControleRemessaContaReceber(SituacaoControleRemessaContaReceberEnum.REMETIDA);
		} catch (Exception e) {
 			e.printStackTrace();
			motivo = e.getMessage();
			ocorreuErroNoRegistro = true;
			crcrVO.setSituacaoControleRemessaContaReceber(SituacaoControleRemessaContaReceberEnum.ESTORNADO);
			crcrVO.setMotivoEstorno(e.getMessage());
			crcrVO.setUsuarioEstorno(usuario);
			crcrVO.setDataEstorno(new Date());
		}
		getFacadeFactory().getControleRemessaFacade().incluirControleOnline(contareceberVO, crcrVO, usuario);
		if (ocorreuErroNoRegistro) {
			throw new Exception("Não foi possível registrar a remessa online do boleto! A impressão do boleto não pode ser realizada! Procure a Instituição de Ensino para mais informações! " + motivo);
		}
	}
	
	
	private void enviarBoletoRemessaOnlinePorBancoBrasil(ContaReceberVO contareceberVO, ControleRemessaContaReceberVO crcrVO, ConfiguracaoGeralSistemaVO config, ConfiguracaoFinanceiroVO configFin, UsuarioVO usuario) throws Exception {
	
			String motivo = "";
			Boolean ocorreuErroNoRegistro = false;
			try {
				BancoBrasilBoletoOnline bb = new BancoBrasilBoletoOnline(contareceberVO, crcrVO, config, configFin);
				RegistroRetornoBoletoBb reg = bb.enviarBoletoRemessaOnlineBancoBrasil();
				if(!reg.getStatusCode().equals(Status.CREATED.getStatusCode())) {
					throw new Exception("Não foi possível registrar a remessa online do boleto! A impressão do boleto não pode ser realizada! Procure a Instituição de Ensino para mais informações! ");
				}
				crcrVO.setSituacaoControleRemessaContaReceber(SituacaoControleRemessaContaReceberEnum.REMETIDA);
				
			} catch (Exception e) {
				motivo = e.getMessage();
				ocorreuErroNoRegistro = true;
				crcrVO.setSituacaoControleRemessaContaReceber(SituacaoControleRemessaContaReceberEnum.ESTORNADO);
				crcrVO.setMotivoEstorno(e.getMessage());
				crcrVO.setUsuarioEstorno(usuario);
				crcrVO.setDataEstorno(new Date());
			}
			getFacadeFactory().getControleRemessaFacade().incluirControleOnline(contareceberVO, crcrVO, usuario);
			if (ocorreuErroNoRegistro) {
				throw new Exception("Não foi possível registrar a remessa online do boleto! A impressão do boleto não pode ser realizada! Procure a Instituição de Ensino para mais informações! " + motivo);
			}		
	}
	

}
