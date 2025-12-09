package relatorio.negocio.jdbc.avaliacaoInst;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.PerguntaQuestionarioVO;
import negocio.comuns.processosel.QuestionarioVO;
import negocio.comuns.processosel.RespostaPerguntaVO;
import negocio.comuns.utilitarias.Uteis;
import relatorio.negocio.comuns.avaliacaoInst.PerguntaRelVO;
import relatorio.negocio.comuns.avaliacaoInst.QuestionarioRelVO;
import relatorio.negocio.comuns.avaliacaoInst.RespostaRelVO;
import relatorio.negocio.interfaces.avaliacaoInst.QuestionarioRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class QuestionarioRel extends SuperRelatorio implements QuestionarioRelInterfaceFacade {

	private static final long serialVersionUID = 1L;

	public QuestionarioRel() {

	}
	
	public void validarDados(Integer questionario) throws Exception {
		if (questionario.equals(0)) {
			throw new Exception("O campo QUESTIONÁRIO deve ser informado.");
		}
	}
	
	public List<QuestionarioRelVO> criarObjeto(QuestionarioVO questionarioVO, UsuarioVO usuarioVO) throws Exception {
		validarDados(questionarioVO.getCodigo());
		List<QuestionarioRelVO> listaQuestionarioRelVOs = new ArrayList<QuestionarioRelVO>(0);
		questionarioVO = getFacadeFactory().getQuestionarioFacade().consultarPorChavePrimaria(questionarioVO.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
		listaQuestionarioRelVOs.add(montarDados(questionarioVO, usuarioVO));
		return listaQuestionarioRelVOs;
	}
	
	public QuestionarioRelVO montarDados(QuestionarioVO questionarioVO, UsuarioVO usuarioVO) {
		QuestionarioRelVO obj = new QuestionarioRelVO();
		obj.setNome(questionarioVO.getDescricao());
		obj.getPerguntaRelVOs().clear();
		montarDadosPerguntaRelVO(obj, questionarioVO, usuarioVO);
		return obj;
	}
	
	public void montarDadosPerguntaRelVO(QuestionarioRelVO obj, QuestionarioVO questionarioVO, UsuarioVO usuarioVO) {
		int nrPergunta = 1;
		for (PerguntaQuestionarioVO perguntaQuestionarioVO : questionarioVO.getPerguntaQuestionarioVOs()) {
			PerguntaRelVO perguntaRelVO = new PerguntaRelVO();
			perguntaRelVO.setNome(perguntaQuestionarioVO.getPergunta().getDescricao());
			perguntaRelVO.setTipoResposta(perguntaQuestionarioVO.getPergunta().getTipoResposta_Apresentar());
			perguntaRelVO.setRespostaPerguntaVOs(perguntaQuestionarioVO.getPergunta().getRespostaPerguntaVOs());
			perguntaRelVO.setNrPergunta(nrPergunta);
			nrPergunta++;
			for (RespostaPerguntaVO respostaPerguntaVO : perguntaQuestionarioVO.getPergunta().getRespostaPerguntaVOs()) {
				RespostaRelVO respostaRelVO = new RespostaRelVO();
				respostaRelVO.setNomeResposta(respostaPerguntaVO.getDescricao());
				respostaRelVO.setApresentarRespota(Boolean.FALSE);
				perguntaRelVO.getRespostaTexto().add(respostaRelVO);
			}
			obj.getPerguntaRelVOs().add(perguntaRelVO);
		}
	}
	
	public List<QuestionarioVO> consultar(String campoConsulta, String valorConsulta, UsuarioVO usuario) throws Exception {
		List<QuestionarioVO> objs = new ArrayList<>(0);
		if (campoConsulta.equals("codigo")) {
			if (valorConsulta.equals("")) {
				valorConsulta = ("0");
			}
			int valorInt = Integer.parseInt(valorConsulta);
			objs = getFacadeFactory().getQuestionarioFacade().consultarPorCodigo(new Integer(valorInt), "AI", true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		}
		if (campoConsulta.equals("descricao")) {
			objs = getFacadeFactory().getQuestionarioFacade().consultarPorDescricao(valorConsulta, "AI", true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		}

		return objs;
	}
	
	public static String getDesignIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "processosel" + File.separator + getIdEntidade() + ".jrxml");
	}
	
	public static String getDesignIReportRelatorio1() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "processosel" + File.separator + getIdEntidade1() + ".jrxml");
	}
	
	public static String getCaminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "processosel" + File.separator);
	}

	public static String getIdEntidade() {
		return ("QuestionarioRel");
	}
	public static String getIdEntidade1() {
		return ("QuestionarioRespostaManualRel");
	}
}
