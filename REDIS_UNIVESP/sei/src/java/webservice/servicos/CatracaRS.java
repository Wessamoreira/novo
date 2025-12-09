package webservice.servicos;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import negocio.comuns.basico.CatracaVO;
import negocio.comuns.ead.enumeradores.SituacaoEnum;
import negocio.comuns.utilitarias.Uteis;
import controle.arquitetura.SuperControle;

/**
 * @author Wendel Rodrigues
 * @sice 13 de maio de 2015
 * 
 *       Classe responsável pela liberação do aluno na instituição através da Catraca.
 */
@Path("/catraca")
public class CatracaRS extends SuperControle {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1475356585405904620L;

//	@GET
//	@Produces({ "application/xml", "application/json" })
//	@Path("/liberar/{codigo}")
//	public PessoaObject executarValidacaoLiberarPessoaCatraca(@PathParam("codigo") final Integer codigo) throws Exception {
//		return getFacadeFactory().getBiometriaFacade().consultarPessoaAcessarCatracaPeloCodigo(codigo);
//	}
	
	@GET
	@Produces("text/html")
	@Path("/totalSincronizar")
	public Integer consultarTotalPessoaSincronizar() throws Exception {
		return getFacadeFactory().getBiometriaFacade().consultarTotalPessoaSincronizarCatraca();
	}
	
	@GET
	@Produces("text/html")
	@Path("/sincronizado/{pessoas}")
	public String realizarRegistroPessoaSincronizada(@PathParam("pessoas") final String pessoas) throws Exception {
//			List<PessoaObject> pessoaObjects = new ArrayList<PessoaObject>(0);
//			JAXBContext context = JAXBContext.newInstance(PessoaObject.class);
//			Unmarshaller unMarshaller = context.createUnmarshaller();
////			String[] pessoaSplit = pessoas.split("</pessoa>");			
//			Object objs = unMarshaller.unmarshal(new InputSource(pessoas));
//			if(objs instanceof List<?>){
//				pessoaObjects = (List<PessoaObject>) objs;
//			}
			StringBuilder pessoasSincronizadas = new StringBuilder("0");
			for(String pessoa:pessoas.split("-")){
				pessoasSincronizadas.append(", ").append(pessoa);
			}
			try{
				getFacadeFactory().getBiometriaFacade().gravarSincronismoRegistradoPorPessoa(pessoasSincronizadas.toString());
				return "OK";
			}catch(Exception e){
				return "ERROR NO SINCRONISMO: "+e.getMessage();
			}
	}
	
	@GET
	@Produces({ "application/xml", "application/json" })
	@Path("/sincronizar/{quantidade}")
	public List<PessoaObject> consultarPessoaSincronizar(@PathParam("quantidade") final Integer quantidade ) throws Exception {
		return getFacadeFactory().getBiometriaFacade().consultarPessoaSincronizarCatraca(quantidade);
	}
	
//	@GET
//	@Produces({ "application/xml", "application/json" })
//	@Path("/liberarMatricula/{matricula}")
//	public PessoaObject executarValidacaoLiberarPessoaCatraca(@PathParam("matricula") final String matricula) throws Exception {
//		return getFacadeFactory().getBiometriaFacade().consultarPessoaAcessarCatracaPelaMatricula(matricula);
//	}

	@GET
	@Produces({ "application/xml", "application/json" })
	@Path("/listarCatraca")
	public List<CatracaObject> getCatracaObjects(@Context final HttpServletRequest request, @Context final SecurityContext security) throws Exception {
		List<CatracaObject> catracas = new ArrayList<CatracaObject>(0);
		List<CatracaVO> catracaVOs = getFacadeFactory().getCatracaFacade().consultarPorSituacao(SituacaoEnum.ATIVO.getName(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null);
		CatracaObject catraca = null;
		for (CatracaVO catracaVO : catracaVOs) {
			catraca = new CatracaObject();
			catraca.setIp(catracaVO.getIp());
			catraca.setResolucao(catracaVO.getResolucao());
			catraca.setSerie(catracaVO.getSerie());
			catraca.setMensagemLiberar(catracaVO.getMensagemLiberar());
			catraca.setMensagemBloquear(catracaVO.getMensagemBloquear());
			catraca.setDuracaoDesbloquear(catracaVO.getDuracaoDesbloquear());
			catraca.setDuracaoMensagem(catracaVO.getDuracaoMensagem());
			catraca.setDirecao(catracaVO.getDirecao());
			catracas.add(catraca);
			catraca = null;
		}
		return catracas;
	}

}
