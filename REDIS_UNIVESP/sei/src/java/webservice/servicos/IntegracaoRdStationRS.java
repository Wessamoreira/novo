package webservice.servicos;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.IOUtils;
import org.codehaus.jettison.json.JSONObject;
import org.codehaus.jettison.mapped.MappedNamespaceConvention;
import org.codehaus.jettison.mapped.MappedXMLStreamReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;

import controle.arquitetura.SuperControle;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.crm.ProspectsVO;
import negocio.comuns.processosel.PreInscricaoLogVO;
import negocio.comuns.processosel.PreInscricaoVO;
import negocio.comuns.processosel.enumeradores.SituacaoLogPreInscricaoEnum;
import negocio.comuns.utilitarias.UteisEmail;
import webservice.servicos.excepetion.ErrorInfoRSVO;
import webservice.servicos.excepetion.WebServiceException;
import webservice.servicos.objetos.CampoPersonalizadoLeadRSVO;
import webservice.servicos.objetos.LeadRSVO;

@Service
@Path("/integracaoRdStationRS")
public class IntegracaoRdStationRS extends SuperControle implements Serializable {

	private static final long serialVersionUID = 1L;
	private final String URL_INCLUIR_RD_STATION = "https://www.rdstation.com.br/api/1.3/conversions";
	
	private final String tagInformativaQueLeadVeioDoSEI = "SEI->RD";
	
	@Autowired
	private static JAXBContext context;
	
	public IntegracaoRdStationRS() throws Exception {
		super();
	}

	
	
	/**
	 * Responsavel por incluir pre-inscricao(PreInscricaoVO) de uma lead(LeadRSVO) recebida pelo WS do RD Station
	 *  
	 * @param request
	 * @return
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/webhookRDStation")
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Response incluirPreInscricaoComLeadRDStation(@Context final HttpServletRequest request) {
		
		PreInscricaoVO preInscricaoVO = new PreInscricaoVO(); 
		
		try {
			
			List<String> lines = IOUtils.readLines(request.getInputStream(), Charset.forName("UTF-8").toString());
			StringBuilder builder = new StringBuilder();
			for (String line : lines) {
				builder.append(line);
			}
			
			MappedNamespaceConvention con = new MappedNamespaceConvention();
			JSONObject jsonObject = new JSONObject(builder.toString());
			
			MappedXMLStreamReader xmlStreamReader = new MappedXMLStreamReader(jsonObject, con);

			context = JAXBContext.newInstance(LeadRSVO.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			LeadRSVO lead = (LeadRSVO)unmarshaller.unmarshal(xmlStreamReader);
			
			//Valida se lead veio do SEI
			if(lead.getTags().toUpperCase().contains(tagInformativaQueLeadVeioDoSEI.toUpperCase()) &&
					lead.getCamposPersonalizados().getCursoInteresse().length() <= 1)
				return Response.status(Response.Status.ACCEPTED).build();
			
			//seta os valores basicos para se caso de erro, as informacoes estarao contidas no log
			preInscricaoVO.setNome(lead.getNome());
			preInscricaoVO.setNomeBatismo(lead.getNomeBatismo());
			preInscricaoVO.setEmail(lead.getEmail());

			//Popula o objeto PreInscricaoVO com os dados da lead enviada pelo RD Station
			preInscricaoVO = getFacadeFactory().getPreInscricaoFacade().montarDadosAPartirDaLeadIntegracaoRDStation(lead);
			
			getFacadeFactory().getPreInscricaoFacade().incluirPreInscricaoAPartirSiteOuHomePreInscricao(preInscricaoVO, null);			
			
			registrarLog(preInscricaoVO, SituacaoLogPreInscricaoEnum.SUCESSO, null);
        	
			return Response.status(Response.Status.CREATED).build();
			
		} catch (Exception e) {
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.BAD_REQUEST.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			
			registrarLog(preInscricaoVO, SituacaoLogPreInscricaoEnum.FALHA, e.getMessage());
			new WebServiceException(errorInfoRSVO, Status.INTERNAL_SERVER_ERROR);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	
	/**
	 * Registrar LOG da pre inscricao
	 * 
	 * @param preInscricaoVO
	 * @param situacaoLogPreInscricao
	 * @param mensagemErro 
	 */
	private void registrarLog(PreInscricaoVO preInscricaoVO, SituacaoLogPreInscricaoEnum situacaoLogPreInscricao, String mensagemErro) {
		
		PreInscricaoLogVO preInscricaoLogVO = getFacadeFactory().getPreInscricaoLogFacade().montarDados(preInscricaoVO);
		preInscricaoLogVO.setSituacaoLogPreInscricao(situacaoLogPreInscricao);
		preInscricaoLogVO.setMensagemErro(mensagemErro);
		
		try {
			getFacadeFactory().getPreInscricaoLogFacade().incluir(preInscricaoLogVO);	
		} catch (Exception e) {
		}
				
	}


	/**
	 * Responsavel por incluir uma lead(LeadRSVO) no RD Station com os dados prospect(ProspectsVO) no SEI
	 * 
	 * @param codigoProspectVO
	 * @return
	 * 	HTTP Status CODE ( https://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html )
	 * @throws Exception
	 */
	public String incluirLeadRDStation(ProspectsVO prospectsVO, ConfiguracaoGeralSistemaVO config) throws Exception {

		String validaDados = validarDadosLeadQueSeraEnviado(prospectsVO, config);
		
		// Verifica que a validacao de dados esta OK antes de enviar para o WS do RD Station
		if(!validaDados.equals("200")) {
			return validaDados;
		}
	
		Gson gson = new Gson();
		LeadRSVO lead = new LeadRSVO();
		
		//Popula a lead com o objeto ProspectVO
		lead = montarDadosAPartirDoProspect(prospectsVO);
		lead.setTokenRdStation(config.getTokenRdStation());
		lead.setIdentificador(config.getIdentificadorRdStation());
		
		//Informa que esse lead veio da integração SEI -> RD
		lead.setTags(tagInformativaQueLeadVeioDoSEI);
		
		String leadJson= gson.toJson(lead);
		
		//HTTP Status CODE retornado do webservice do RD Station 
		int responseCode = enviarLeadRDStation(leadJson);
		
		return String.valueOf(responseCode);
	}

	
	/**
	 * Valida se os campos obrigatorios da integracao com o RD Station estao preenchidos 
	 * 
	 * @param prospectsVO
	 * @param config
	 * @return
	 * 		HTTP Status CODE
	 * 		Ex.: 200 - OK
	 * 			 400 - Dados obrigatorios nao preenchidos
	 * 			 401 - Nao autorizado
	 */
	public String validarDadosLeadQueSeraEnviado(ProspectsVO prospectsVO, ConfiguracaoGeralSistemaVO config) {
		
		if(config == null || !config.getAtivarIntegracaoRdStation()) {
			return "401";
		}
		
		if(config.getTokenRdStation() == null || config.getTokenRdStation().trim().length() <= 1) {
			return "400";
		}
		
		if(config.getIdentificadorRdStation() == null || config.getIdentificadorRdStation().trim().length() <= 1) {
			return "400";
		}
		
		if(prospectsVO == null || prospectsVO.getEmailPrincipal() == null || !UteisEmail.getValidEmail(prospectsVO.getEmailPrincipal())) {
			return "400";
		}
		
		return "200";
	}



	/**
	 * WS responsavel por incluir uma lead(LeadRSVO) no RD Station com os dados prospect(ProspectsVO) no SEI 
	 *  
	 * @param request
	 * @return
	 */
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/incluirLeadNoRdStation")
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public Response incluirLeadRDStationWS(@HeaderParam("codigoProspectVO") Integer codigoProspectVO,
									   @HeaderParam("tokenRdStation") String tokenRdStation,
									   @HeaderParam("identificador") String identificador) throws Exception {

		Gson gson = new Gson();
		LeadRSVO lead = new LeadRSVO();
		ProspectsVO prospect = getFacadeFactory().getProspectsFacade().consultarDadosCompletosPorIdProspect(codigoProspectVO, null);
		
		if(prospect == null || prospect.getEmailPrincipal() == null || prospect.getEmailPrincipal().length() <=1
				|| tokenRdStation == null || tokenRdStation.trim().length()<=0
				|| identificador == null || identificador.trim().length() <= 0) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		
		ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoPadraoSistema();
		
		if(!config.getAtivarIntegracaoRdStation()) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
		
		//Popula a lead com o objeto ProspectVO
		lead = montarDadosAPartirDoProspect(prospect);
		lead.setTokenRdStation(tokenRdStation);
		lead.setIdentificador(identificador);
		
		//Informa que esse lead veio da integração SEI -> RD
		lead.setTags(tagInformativaQueLeadVeioDoSEI);
		
		String leadJson= gson.toJson(lead);
		
		int responseCode = enviarLeadRDStation(leadJson);
		
		return Response.status(responseCode).build();
	}
	
	
	/**
	 * Envia lead para o RD Station
	 * 
	 * @param leadJson
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws ProtocolException
	 */
	public int enviarLeadRDStation(String leadJson) throws MalformedURLException, IOException, ProtocolException {
		
		if(leadJson == null || leadJson.isEmpty()) {
			return 406;
		}
		
		URL obj = new URL(URL_INCLUIR_RD_STATION);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		 
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type","application/json");
		
		con.setDoOutput(true);
		
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(leadJson);
		wr.flush();
		wr.close();
		
		int responseCode = con.getResponseCode();
		return responseCode;
	}
	
	
	/**
	 * Monta os dados da Lead de acordo com a necessidade da integracao
	 * 
	 * @param prospectsVO
	 * @return LeadRSVO
	 */
	public LeadRSVO montarDadosAPartirDoProspect(ProspectsVO prospectsVO) {
		
		LeadRSVO leadProspect = new LeadRSVO();
		
		leadProspect.setNome(prospectsVO.getNome());
		leadProspect.setNomeBatismo(prospectsVO.getNomeBatismo());
		leadProspect.setEmail(prospectsVO.getEmailPrincipal());
		leadProspect.setCelular(prospectsVO.getCelular());
		leadProspect.setTelefone(prospectsVO.getTelefoneEmpresa());
		leadProspect.setCidade(prospectsVO.getCidade().getNome());
		
		CampoPersonalizadoLeadRSVO camposPersonalizados = new CampoPersonalizadoLeadRSVO();
		camposPersonalizados.setUnidadeEnsino(prospectsVO.getUnidadeEnsino().getNome());
		
		leadProspect.setCamposPersonalizados(camposPersonalizados);
		
		return leadProspect;
	}
}