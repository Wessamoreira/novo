package controle.basico;

import java.io.BufferedReader;
//import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas 
 * cidadeForm.jsp cidadeCons.jsp) com as funcionalidades da classe <code>Cidade</code>.
 * Implemtação da camada controle (Backing Bean).
 * @see SuperControle
 * @see Cidade
 * @see CidadeVO
 */
import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.basico.PessoaEADIPOGVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;

@Controller("IntegracaoEADIPOGControle")
@Scope("viewScope")
@Lazy
public class IntegracaoEADIPOGControle extends SuperControle implements Serializable {

	public PessoaEADIPOGVO pessoaEADIPOGVO;
	public MatriculaVO matriculaVO;
	public MatriculaPeriodoVO matriculaPeriodoVO;
	public TurmaVO turma;
	public DisciplinaVO disciplina;
	public String urlDestino;
	public List listaSelectItemDisciplinaOnline;
	public Boolean apresentarMenuEADIPOG;
	public Integer qtdeNovidades;
	public String popUpApresentar;

	public IntegracaoEADIPOGControle() throws Exception {
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
		verificarApresentarMenuEADIPOG();
		verificarQtdNovidades();
	}

	public void verificarQtdNovidades() {
		try {
			setQtdeNovidades(1);
		} catch (Exception e) {

		}
	}

	public void verificarApresentarMenuEADIPOG() {
		try {
			String matricula = "";
			if (getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade().getIdAutenticacaoServOtimize().equals("IPOG")) {
				setApresentarMenuEADIPOG(Boolean.TRUE);
			}
//			VisaoAlunoControle visaoAluno = (VisaoAlunoControle) context().getExternalContext().getSessionMap().get("VisaoAlunoControle");
//			if (visaoAluno != null) {
//				matricula = visaoAluno.getMatricula().getMatricula();
//			}
//			getMatriculaVO().setMatricula(matricula);
//			getFacadeFactory().getMatriculaFacade().carregarDados(getMatriculaVO(), NivelMontarDados.BASICO, getUsuarioLogado());
//			getFacadeFactory().getPessoaFacade().carregarDados(getMatriculaVO().getAluno(), NivelMontarDados.BASICO, getUsuarioLogado());
//			if (getMatriculaVO().getCurso().getNivelEducacionalPosGraduacao()) {				
//				TurmaEADIPOGVO turma = getFacadeFactory().getLiberacaoTurmaEADFacade().consultarPorTurma(matricula, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
//				if (turma.getCodigo().intValue() > 0) {
//					setApresentarMenuEADIPOG(Boolean.TRUE);
//				} else {
//					setApresentarMenuEADIPOG(Boolean.FALSE);
//				}
//			} else {
//				setApresentarMenuEADIPOG(Boolean.FALSE);
//			}
		} catch (Exception e) {
			setApresentarMenuEADIPOG(Boolean.FALSE);
		}
	}

	public String inicializarMenuIntegracaoEADIPOG() {
		try {
			if (getMatriculaVO().getCurso().getNivelEducacionalPosGraduacao()) {
				List<MatriculaPeriodoTurmaDisciplinaVO> lista = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarDisciplinaOnlineDoAlunoPorMatriculaIntegracaoEADIPOG(getMatriculaVO().getMatricula(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				if (!lista.isEmpty()) {
					SelectItemOrdemValor ordenador = null;
					Iterator i = null;
					i = lista.iterator();
					List objs = new ArrayList(0);
					objs.add(new SelectItem(0, ""));
					while (i.hasNext()) {
						MatriculaPeriodoTurmaDisciplinaVO obj = (MatriculaPeriodoTurmaDisciplinaVO) i.next();
						objs.add(new SelectItem(obj.getCodigo(), obj.getDisciplina().getNome()));
					}
					ordenador = new SelectItemOrdemValor();
					Collections.sort((List) objs, ordenador);
					setListaSelectItemDisciplinaOnline(objs);
				} else {
					setListaSelectItemDisciplinaOnline(null);
				}
			}
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("integracaoEADIPOG.xhtml");
		} catch (Exception e) {
			return "";
		}
	}

	public void verificaLogarJump() {
		try {
			if (!getFacadeFactory().getPessoaEADIPOGFacade().verificarExistenciaRegistroPessoaEADIPOG(getMatriculaVO().getAluno().getCodigo())) {
				setPopUpApresentar("RichFaces.$('panelAviso').show();");
			} else {
				setPopUpApresentar("");
				logarJump();
			}
		} catch (Exception e) {
			setPopUpApresentar("");
		}
	}

//	@ResponseBody
//	@RequestMapping(value = "/pie/processarComunicacaoInterna/{login}/{matricula}/{disciplina}/{turma}/{mensagem}/{email}/{subject}/{headers}", method = RequestMethod.POST)
//	public void processarComunicacaoInterna(@PathVariable String login, @PathVariable String matricula, @PathVariable Integer disciplina, @PathVariable String turma, @PathVariable String mensagem, @PathVariable String email, @PathVariable String subject, @PathVariable String headers) {
//		// valores para serem salvos.
//		System.out.print("ENTROU AQUI");
//	}

	public void logarJump() {
		try {
			if (getDisciplina().getCodigo().intValue() == 0) {
				throw new Exception("Selecione a disciplina para realizar o login no portal!");
			}
			MatriculaPeriodoTurmaDisciplinaVO mptd = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarPorChavePrimaria(getDisciplina().getCodigo().intValue(), Uteis.NIVELMONTARDADOS_COMBOBOX, null);
			getPessoaEADIPOGVO().setAluno(getMatriculaVO().getAluno().getCodigo());
			getPessoaEADIPOGVO().setMatricula(getMatriculaVO().getMatricula());
			getPessoaEADIPOGVO().setTurma(mptd.getTurma().getCodigo());
			getPessoaEADIPOGVO().setDisciplina(mptd.getDisciplina().getCodigo());
			getPessoaEADIPOGVO().setCpfCorrespondente(gerarCPFIntegracao(getMatriculaVO().getAluno().getCodigo()));
			getPessoaEADIPOGVO().setEmailCorrespondente(getMatriculaVO().getAluno().getNomeResumido().replaceAll(" ", "").replace(".", "").toLowerCase() + "@ipog.edu.br");
			getPessoaEADIPOGVO().setEmail2Correspondente(getMatriculaVO().getAluno().getNomeResumido().replaceAll(" ", "").replace(".", "").toLowerCase() + "2" + "@ipog.edu.br");
			getFacadeFactory().getPessoaEADIPOGFacade().persistir(getPessoaEADIPOGVO());
			getMatriculaPeriodoVO().getTurma().setCodigo(mptd.getTurma().getCodigo());
			getDisciplina().setCodigo(mptd.getDisciplina().getCodigo());
			setUrlDestino(getObjeto());
			
			if (!getUrlDestino().equals("")) {
				HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
				response.setContentType("text/html");
				response.setStatus(response.SC_MOVED_TEMPORARILY);
				response.setHeader("Location", getUrlDestino());		      
				response.sendRedirect(getUrlDestino());
			} else {
				throw new Exception("Não foi possível logar no ambiente de estudo, entre em contato com o departamento responsável!");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", "Não foi possível logar no ambiente de estudo, entre em contato com o departamento responsável!");
		}
	}

//	public HistoricoVO obterStatusAlunoJump(Integer aluno, String matricula, Integer disciplina, Integer turma) {
//		try {
//			PessoaEADIPOGVO pessoa = getFacadeFactory().getPessoaEADIPOGFacade().consultarPorDadosAluno(aluno, matricula, disciplina, turma, Uteis.NIVELMONTARDADOS_TODOS, null);									
//			return getObjetoStatusAluno(pessoa);
//		} catch (Exception e) {			
//			return new HistoricoVO();
//		}
//	}

	public ArrayList geraCPFParcial(String cpf) {
		int qtd = 0;
		if (cpf.length() < 9) {
			qtd = 9 - cpf.length();
		}
		if (qtd > 0) {
			for (int j = 0; j < qtd; j++) {
				cpf = cpf + "0";
			}
		}
		ArrayList listaAleatoria = new ArrayList();
		for (int i = 0; i < 9; i++) {
			listaAleatoria.add(Integer.parseInt(cpf.substring(i, i + 1)));
		}
		return listaAleatoria;
	}

	public String gerarCPFIntegracao(Integer codigo) throws Exception {
		ArrayList<Integer> listaAleatoria = new ArrayList<Integer>();
		listaAleatoria = geraCPFParcial(codigo.toString());
		geraPrimeiroDigito(listaAleatoria);
		geraSegundoDigito(listaAleatoria);
		String cpf = "";
		String texto = "";
		for (int item : listaAleatoria) {
			texto += item;
		}
		cpf = texto;
		return cpf;
	}

	public ArrayList geraSegundoDigito(ArrayList<Integer> listaAleatoria) {
		ArrayList<Integer> listaNumMultiplicados = new ArrayList<Integer>();
		int segundoDigito;
		int totalSomatoria = 0;
		int restoDivisao;
		int peso = 11;
		for (int item : listaAleatoria) {
			listaNumMultiplicados.add(item * peso);
			peso--;
		}
		for (int item : listaNumMultiplicados) {
			totalSomatoria += item;
		}
		restoDivisao = (totalSomatoria % 11);
		if (restoDivisao < 2) {
			segundoDigito = 0;
		} else {
			segundoDigito = 11 - restoDivisao;
		}
		listaAleatoria.add(segundoDigito);
		return listaAleatoria;
	}

	public ArrayList geraPrimeiroDigito(ArrayList<Integer> listaAleatoria) {
		ArrayList<Integer> listaNumMultiplicados = new ArrayList<Integer>();
		int primeiroDigito;
		int totalSomatoria = 0;
		int restoDivisao;
		int peso = 10;
		for (int item : listaAleatoria) {
			listaNumMultiplicados.add(item * peso);
			peso--;
		}
		for (int item : listaNumMultiplicados) {
			totalSomatoria += item;
		}
		restoDivisao = (totalSomatoria % 11);
		if (restoDivisao < 2) {
			primeiroDigito = 0;
		} else {
			primeiroDigito = 11 - restoDivisao;
		}
		listaAleatoria.add(primeiroDigito);
		return listaAleatoria;
	}

	public String getObjeto() throws Exception {
		
		// http://ww4jumpnext.com.br/ipog/IntegracaoView.json?metodo=getLogin&key=0de0bef380eb81a00b686d3e31c9bd7c&login=26540121822&nome=Jonathan&matricula=123123&curso=25
		// &turma=15&disciplina=15,12,50&email=jmoreira@jumpeducation.com.br&turma_dt_inicio=2014/09/01&turma_dt_fim=2014/12/20
		String dataPrimeiraAula = UteisData.getData(getFacadeFactory().getHorarioTurmaDiaFacade().consultarPrimeiraDataAulaPorTurma(getMatriculaPeriodoVO().getTurma().getCodigo(), "", ""), "bd");
		String dataUltimaAula = UteisData.getData(getFacadeFactory().getHorarioTurmaDiaFacade().consultarUltimaDataAulaPorTurma(getMatriculaPeriodoVO().getTurma().getCodigo(), "", ""), "bd");
		dataPrimeiraAula = dataPrimeiraAula.replace(".", "/");
		dataUltimaAula = dataUltimaAula.replace(".", "/");
		String urlWebService2 = "http://ww2.jumpnext.com.br/ipog/IntegracaoView.json?";
		urlWebService2 += "metodo=getLogin";
		urlWebService2 += "&turma=" + getMatriculaPeriodoVO().getTurma().getCodigo().intValue();
		urlWebService2 += "&disciplina=" + getDisciplina().getCodigo();
		urlWebService2 += "&curso=" + getMatriculaVO().getCurso().getCodigo().intValue();
		urlWebService2 += "&email=" + Uteis.removerAcentuacao(getPessoaEADIPOGVO().getEmailCorrespondente());
		urlWebService2 += "&turma_dt_inicio=" + dataPrimeiraAula;// +
		urlWebService2 += "&nome=" + Uteis.removerAcentuacao(getMatriculaVO().getAluno().getNome().replaceAll(" ", "_"));
		urlWebService2 += "&login=" + getPessoaEADIPOGVO().getCpfCorrespondente();
		urlWebService2 += "&turma_dt_fim=" + dataUltimaAula;
		//urlWebService2 += "&key=b0d98932119a2970683edd499f463797";
		urlWebService2 += "&key=0c02729c196894d1395d2482d9a67f5d";		
		urlWebService2 += "&matricula=" + getMatriculaVO().getMatricula();
		
		//urlWebService2 = "http://ww2.jumpnext.com.br/ipog/IntegracaoView.json?metodo=getLogin&turma=2040&disciplina=16&curso=16&email=x1@uol.com&turma_dt_inicio=2014/05/05&nome=x1@uol.com&login=x1@uol.com&turma_dt_fim=2014/12/12&key=b0d98932119a2970683edd499f463797&matricula=55555";		
		
		URL url = new URL(urlWebService2);
		Reader br = new InputStreamReader(url.openStream());

		JSONParser parser = new JSONParser();
		JSONObject jsonObjeto = (JSONObject) parser.parse(br);
		System.out.println(jsonObjeto);
		
		JSONObject jsonResponse = (JSONObject) jsonObjeto.get("response");
		String urlDestino = "";
		if (jsonResponse != null) {
			urlDestino = (String) jsonResponse.get("url");
		} else {
			urlDestino = "";
		}
		String parseException = (String) jsonObjeto.get("parseException");
		String status = (String) jsonObjeto.get("status");
		String sessionId = (String) jsonObjeto.get("sessionId");
		String cliente = (String) jsonObjeto.get("cliente");
		String systemException = (String) jsonObjeto.get("systemException");
		JSONObject jsonObjetoBus = (JSONObject) jsonObjeto.get("businessException");
		if (jsonObjetoBus != null) {
			String message = (String) jsonObjetoBus.get("message");
			throw new Exception(message);
		}
		//System.out.println(jsonObjeto);				
		return urlDestino;
	}

	public List<HistoricoVO> getObjetoStatusAluno() throws Exception {
//		String urlWebService2 = "http://ww2.jumpnext.com.br/ipog/IntegracaoView.json?";
//		urlWebService2 += "metodo=getUsuarioSatus";
//		urlWebService2 += "&key=0c02729c196894d1395d2482d9a67f5d";
//		urlWebService2 += "&login=" + pessoa.getCpfCorrespondente();
//		urlWebService2 += "&disciplina=" + pessoa.getDisciplina().intValue();
//		urlWebService2 += "&turma=" + pessoa.getTurma().intValue();
		
		String urlWebService2 = "http://ipog.estudar.online/moodle/servicos/notas.php?";
		urlWebService2 += "key=0de0bef380eb81a00b686d3e31c9bd7c";
		urlWebService2 += "&disciplina=16";
		
		URL url = new URL(urlWebService2);
		Reader br = new InputStreamReader(url.openStream());

		
		JSONParser parser = new JSONParser();
		JSONObject jsonObjeto = (JSONObject) parser.parse(br);
						

		
		JSONArray jsonResponse = (JSONArray) jsonObjeto.get("response");
		System.out.println(jsonResponse);	
		String response = jsonResponse.toString();

		// chamar metodo aqui
		List<HistoricoVO> listaHistorico = new ArrayList<HistoricoVO>();		
		while(response.contains("porcentagem")){
			response = preencherObjetoHistorico(response, listaHistorico);	
		}
		
		
					
		
		
		return listaHistorico;
	}

	public String preencherObjetoHistorico(String jsonResponse, List<HistoricoVO> listaHistorico) {		
		String response = jsonResponse;
		int posIni = response.indexOf("{");
		int posFim = response.indexOf("}");
		
		try {
			String jsonTrabalhar = response.substring(posIni, posFim + 1);			
			HistoricoVO hist = new HistoricoVO();
			hist.getDisciplina().setCodigo(16);
			
			int posIniTurma = jsonTrabalhar.indexOf("turma");
			int posFimTurma = jsonTrabalhar.indexOf(",");
			String strTurma = jsonTrabalhar.substring(posIniTurma, posFimTurma);
			String turma = strTurma.substring(strTurma.indexOf(":") + 2, strTurma.length() - 1);			
			try {
				List<TurmaVO> turmas = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(turma, false, null);
				if (!turmas.isEmpty()) {
					hist.getMatriculaPeriodoTurmaDisciplina().setTurma((TurmaVO)turmas.get(0));
				}				
			} catch (Exception e) {			
			}

			jsonTrabalhar = jsonTrabalhar.substring(posFimTurma + 1);		

			int posIniSinc = jsonTrabalhar.indexOf("sincronia");
			int posFimSinc = jsonTrabalhar.indexOf(",");
			String strSincronia = jsonTrabalhar.substring(posIniSinc, posFimSinc);		
			jsonTrabalhar = jsonTrabalhar.substring(posFimSinc + 1);		
			
			int posIniPorc = jsonTrabalhar.indexOf("porcentagem");
			int posFimPorc = jsonTrabalhar.indexOf(",");
			String strPorcentagem = jsonTrabalhar.substring(posIniPorc, posFimPorc);		
			String porcentagem = strPorcentagem.substring(strPorcentagem.indexOf(":") + 2, strPorcentagem.length() - 1);
			Double porcentagemDouble = new Double(0);
			try {
				porcentagemDouble = new Double(porcentagem);
				hist.setFreguencia(porcentagemDouble);
			} catch (Exception e) {			
			}
			jsonTrabalhar = jsonTrabalhar.substring(posFimPorc + 1);
			
			int posIniNota = jsonTrabalhar.indexOf("nota");
			int posFimNota = jsonTrabalhar.indexOf(",");
			String strNota = jsonTrabalhar.substring(posIniNota, posFimNota);		
			String nota = strNota.substring(strNota.indexOf(":") + 2, strNota.length() - 1);
			Double notaDouble = new Double(0);
			try {
				notaDouble = new Double(nota);
				hist.setMediaFinal(notaDouble);
			} catch (Exception e) {			
			}
			jsonTrabalhar = jsonTrabalhar.substring(posFimNota + 1);
			
			int posIniSta = jsonTrabalhar.indexOf("status");
			int posFimSta = jsonTrabalhar.indexOf(",");
			String strStatus = jsonTrabalhar.substring(posIniSta, posFimSta);
			String status = strStatus.substring(strStatus.indexOf(":") + 2, strStatus.length() - 1);
			hist.setSituacao(status);	
			jsonTrabalhar = jsonTrabalhar.substring(posFimSta + 1);
	
			int posIniMat = jsonTrabalhar.indexOf("matricula");			
			String strMat = jsonTrabalhar.substring(posIniMat + 1);
			String matricula = strMat.substring(strMat.indexOf(":") + 2, strMat.length() - 2);
			if (matricula.equals("null")) {
				throw new Exception("Matrícula null, verificar registro! => " + jsonTrabalhar);
			}
			hist.getMatricula().setMatricula(matricula);			
			
			response = response.substring(posFim + 2);
			listaHistorico.add(hist);
			return response;
		} catch (Exception e) {
			response = response.substring(posFim + 2);
			return response;
		}
	}

	// public static Object postObjeto(Object objetoEnvio, Class
	// tipoObjetoRetorno, String urlWebService) {
	// Object objetoRetorno = null;
	//
	// try {
	// String requestJson = toJson(objetoEnvio);
	//
	// URL url = new URL(urlWebService);
	// HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	// connection.setRequestMethod("POST");
	// connection.setDoOutput(true);
	// connection.setUseCaches(false);
	// connection.setConnectTimeout(15000);
	// connection.setRequestProperty("login", "teste");
	// connection.setRequestProperty("senha", "teste");
	// connection.setRequestProperty("Content-Type", "application/json");
	// connection.setRequestProperty("Accept", "application/json");
	// connection.setRequestProperty("Content-Length",
	// Integer.toString(requestJson.length()));
	//
	// DataOutputStream stream = new
	// DataOutputStream(connection.getOutputStream());
	// stream.write(requestJson.getBytes("UTF-8"));
	// stream.flush();
	// stream.close();
	// connection.connect();
	//
	// String responseJson = inputStreamToString(connection.getInputStream());
	// connection.disconnect();
	// objetoRetorno = fromJson(responseJson, tipoObjetoRetorno);
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return objetoRetorno;
	// }

	public static String inputStreamToString(InputStream is) throws IOException {
		if (is != null) {
			Writer writer = new StringWriter();

			char[] buffer = new char[1024];
			try {
				Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
			} finally {
				is.close();
			}
			return writer.toString();
		} else {
			return "";
		}
	}

	// public static String toJson(Object objeto) throws Exception {
	// ObjectMapper mapper = new ObjectMapper();
	// StringWriter jsonValue = new StringWriter();
	// mapper.writeValue(new PrintWriter(jsonValue), objeto);
	// return jsonValue.toString();
	// }
	//
	// public static Object fromJson(String json, Class objectClass) throws
	// Exception {
	// JsonFactory f = new MappingJsonFactory();
	// JsonParser jp = f.createJsonParser(json);
	// Object obj = jp.readValueAs(objectClass);
	// return obj;
	// }

	public PessoaEADIPOGVO getPessoaEADIPOGVO() {
		if (pessoaEADIPOGVO == null) {
			pessoaEADIPOGVO = new PessoaEADIPOGVO();
		}
		return pessoaEADIPOGVO;
	}

	public void setPessoaEADIPOGVO(PessoaEADIPOGVO pessoaEADIPOGVO) {
		this.pessoaEADIPOGVO = pessoaEADIPOGVO;
	}

	public MatriculaVO getMatriculaVO() {
		if (matriculaVO == null) {
			matriculaVO = new MatriculaVO();
		}
		return matriculaVO;
	}

	public void setMatriculaVO(MatriculaVO matriculaVO) {
		this.matriculaVO = matriculaVO;
	}

	public String getUrlDestino() {
		if (urlDestino == null) {
			urlDestino = "";
		}
		return urlDestino;
	}

	public void setUrlDestino(String urlDestino) {
		this.urlDestino = urlDestino;
	}

	public MatriculaPeriodoVO getMatriculaPeriodoVO() {
		if (matriculaPeriodoVO == null) {
			matriculaPeriodoVO = new MatriculaPeriodoVO();
		}
		return matriculaPeriodoVO;
	}

	public void setMatriculaPeriodoVO(MatriculaPeriodoVO matriculaPeriodoVO) {
		this.matriculaPeriodoVO = matriculaPeriodoVO;
	}

	public List getListaSelectItemDisciplinaOnline() {
		if (listaSelectItemDisciplinaOnline == null) {
			listaSelectItemDisciplinaOnline = new ArrayList(0);
		}
		return listaSelectItemDisciplinaOnline;
	}

	public void setListaSelectItemDisciplinaOnline(List listaSelectItemDisciplinaOnline) {
		this.listaSelectItemDisciplinaOnline = listaSelectItemDisciplinaOnline;
	}

	public DisciplinaVO getDisciplina() {
		if (disciplina == null) {
			disciplina = new DisciplinaVO();
		}
		return disciplina;
	}

	public void setDisciplina(DisciplinaVO disciplina) {
		this.disciplina = disciplina;
	}

	public Boolean getApresentarMenuEADIPOG() {
		if (apresentarMenuEADIPOG == null) {
			apresentarMenuEADIPOG = Boolean.FALSE;
		}
		return apresentarMenuEADIPOG;
	}

	public void setApresentarMenuEADIPOG(Boolean apresentarMenuEADIPOG) {
		this.apresentarMenuEADIPOG = apresentarMenuEADIPOG;
	}

	public Integer getQtdeNovidades() {
		if (qtdeNovidades == null) {
			qtdeNovidades = 0;
		}
		return qtdeNovidades;
	}

	public void setQtdeNovidades(Integer qtdeNovidades) {
		this.qtdeNovidades = qtdeNovidades;
	}

	public String getPopUpApresentar() {
		if (popUpApresentar == null) {
			popUpApresentar = "";
		}
		return popUpApresentar;
	}

	public void setPopUpApresentar(String popUpApresentar) {
		this.popUpApresentar = popUpApresentar;
	}

}
