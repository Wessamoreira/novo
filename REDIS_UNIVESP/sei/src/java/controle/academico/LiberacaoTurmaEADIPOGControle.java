package controle.academico;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas censoForm.jsp censoCons.jsp) com
 * as funcionalidades da classe <code>Censo</code>. Implemtação da camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see Censo
 * @see CensoVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.LiberacaoTurmaEADIPOGVO;
import negocio.comuns.academico.TurmaEADIPOGVO;
import negocio.comuns.academico.enumeradores.ModalidadeDisciplinaEnum;
import negocio.comuns.utilitarias.ControleConsulta;

@Controller("LiberacaoTurmaEADIPOGControle")
@Scope("viewScope")
@Lazy
public class LiberacaoTurmaEADIPOGControle extends SuperControle implements Serializable {

	private LiberacaoTurmaEADIPOGVO liberacaoTurmaEADVO;
	private List<TurmaEADIPOGVO> listaLiberacao;
	
	public LiberacaoTurmaEADIPOGControle() throws Exception {
		setControleConsulta(new ControleConsulta());
		consultar();
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe
	 * <code>Censo</code> para alteração. O objeto desta classe é
	 * disponibilizado na session da página (request) para que o JSP
	 * correspondente possa disponibilizá-lo para edição.
	 */
	public void visualizar() {
		try {
			setListaLiberacao(null);
			LiberacaoTurmaEADIPOGVO obj = (LiberacaoTurmaEADIPOGVO) context().getExternalContext().getRequestMap().get("liberacaoTurmaEAD");
			setListaLiberacao(getFacadeFactory().getLiberacaoTurmaEADFacade().consultarTurmasPorModulo(obj.getModulo()));
			setLiberacaoTurmaEADVO(obj);
			obj.setNovoObj(Boolean.FALSE);
			setMensagemID("msg_dados_editar");
			//return "consultar";
		} catch (Exception e) {
			setListaLiberacao(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
			//return "consultar";
		}
	}

	public void liberarTurmasSelecionadas() {
		try {
			Iterator i = getListaLiberacao().iterator();
			while(i.hasNext()) {
				TurmaEADIPOGVO t = (TurmaEADIPOGVO)i.next();
				if (t.getSelecionado()) {
					t.setUsuario(getUsuarioLogado().getCodigo());
					getFacadeFactory().getLiberacaoTurmaEADFacade().incluir(t, getUsuarioLogado());
					getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().alteraModalidadeMatriculaPeriodoTurmaDisciplina(t.getCodTurma(), 16, ModalidadeDisciplinaEnum.ON_LINE.name());
				}
			}
			setListaLiberacao(getFacadeFactory().getLiberacaoTurmaEADFacade().consultarTurmasPorModulo(getLiberacaoTurmaEADVO().getModulo()));
			setListaConsulta(getFacadeFactory().getLiberacaoTurmaEADFacade().consultarTurmas());
			setMensagemID("msg_dados_liberacaoEADIPOG");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}


	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * CensoCons.jsp. Define o tipo de consulta a ser executada, por meio de
	 * ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
	 * resultado, disponibiliza um List com os objetos selecionados na sessao da
	 * pagina.
	 */
	public String consultar() {
		try {
			super.consultar();
			setListaConsulta(getFacadeFactory().getLiberacaoTurmaEADFacade().consultarTurmas());
			setMensagemID("msg_dados_consultados");
			return "consultar";
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "consultar";
		}
	}

	/**
	 * Operação que libera todos os recursos (atributos, listas, objetos) do
	 * backing bean. Garantindo uma melhor atuação do Garbage Coletor do Java. A
	 * mesma é automaticamente quando realiza o logout.
	 */
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
		liberacaoTurmaEADVO = null;
		listaLiberacao = null;
	}

	public LiberacaoTurmaEADIPOGVO getLiberacaoTurmaEADVO() {
		if (liberacaoTurmaEADVO == null) {
			liberacaoTurmaEADVO = new LiberacaoTurmaEADIPOGVO();
		}
		return liberacaoTurmaEADVO;
	}

	public void setLiberacaoTurmaEADVO(LiberacaoTurmaEADIPOGVO liberacaoTurmaEADVO) {
		this.liberacaoTurmaEADVO = liberacaoTurmaEADVO;
	}

	public List<TurmaEADIPOGVO> getListaLiberacao() {
		if (listaLiberacao == null) {
			listaLiberacao = new ArrayList<TurmaEADIPOGVO>();
		}
		return listaLiberacao;
	}

	public void setListaLiberacao(List<TurmaEADIPOGVO> listaLiberacao) {
		this.listaLiberacao = listaLiberacao;
	}
	
}
