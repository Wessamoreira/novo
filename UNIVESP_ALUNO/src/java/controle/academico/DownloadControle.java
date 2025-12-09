package controle.academico;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas downloadForm.jsp
 * downloadCons.jsp) com as funcionalidades da classe <code>Download</code>. Implemtação da camada controle (Backing
 * Bean).
 * 
 * @see SuperControle
 * @see Download
 * @see DownloadVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.faces. model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.DownloadVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis; @Controller("DownloadControle")
@Scope("request")
@Lazy
public class DownloadControle extends SuperControle implements Serializable {

	private DownloadVO downloadVO;
	private String campoConsultarArquivo;
	private String valorConsultarArquivo;
	private List listaConsultarArquivo;
	private String campoConsultarPesssoa;
	private String valorConsultarPesssoa;
	private List listaConsultarPesssoa;
	private List listaSelectItemTurma;

	public DownloadControle() throws Exception {
		//obterUsuarioLogado();
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe <code>Download</code> para edição pelo usuário da
	 * aplicação.
	 */
	public String novo() {         removerObjetoMemoria(this);
		setDownloadVO(new DownloadVO());
		setMensagemID("msg_entre_dados");
		return "editar";
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe <code>Download</code> para alteração. O
	 * objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa
	 * disponibilizá-lo para edição.
	 */
	public String editar() {
		DownloadVO obj = (DownloadVO) context().getExternalContext().getRequestMap().get("download");
		obj.setNovoObj(Boolean.FALSE);
		setDownloadVO(obj);
		setMensagemID("msg_dados_editar");
		return "editar";
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>Download</code>. Caso o
	 * objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é
	 * acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado
	 * para o usuário juntamente com uma mensagem de erro.
	 */
	public String gravar() {
		try {
			if (downloadVO.isNovoObj().booleanValue()) {
				getFacadeFactory().getDownloadFacade().incluir(downloadVO,getUsuarioLogado());
			} else {
				getFacadeFactory().getDownloadFacade().alterar(downloadVO,getUsuarioLogado());
			}
			setMensagemID("msg_dados_gravados");
			return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "editar";
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP DownloadCons.jsp. Define o tipo de consulta a ser
	 * executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como resultado,
	 * disponibiliza um List com os objetos selecionados na sessao da pagina.
	 */
	public String consultar() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			if (getControleConsulta().getCampoConsulta().equals("codigo")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getDownloadFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeArquivo")) {
				objs = getFacadeFactory().getDownloadFacade().consultarPorNomeArquivo(getControleConsulta().getValorConsulta(), Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomePessoa")) {
				objs = getFacadeFactory().getDownloadFacade().consultarPorNomePessoa(getControleConsulta().getValorConsulta(), Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("dataDownload")) {
				Date valorData = Uteis.getDate(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getDownloadFacade().consultarPorDataDownload(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), true,
						Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			return "consultar";
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "consultar";
		}
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe <code>DownloadVO</code> Após a exclusão ela
	 * automaticamente aciona a rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getDownloadFacade().excluir(downloadVO,getUsuarioLogado());
			novo();
			setMensagemID("msg_dados_excluidos");
			return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "editar";
		}
	}

	/**
	 * Método responsável por processar a consulta na entidade <code>Pessoa</code> por meio dos parametros informados no
	 * richmodal. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pelos parâmentros
	 * informados no richModal montando automaticamente o resultado da consulta para apresentação.
	 */
	public void consultarPesssoa() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultarPesssoa().equals("codigo")) {
				if (getValorConsultarPesssoa().equals("")) {
					setValorConsultarPesssoa("0");
				}
				Integer valorInt = Integer.parseInt(getValorConsultarPesssoa());
				objs = getFacadeFactory().getPessoaFacade().consultarPorCodigo(valorInt, false, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getCampoConsultarPesssoa().equals("nome")) {
				objs = getFacadeFactory().getPessoaFacade().consultarPorNome(getValorConsultarPesssoa(), false, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getCampoConsultarPesssoa().equals("nomeCidade")) {
				//objs = getFacadeFactory().getPessoaFacade().consultarPorNomeCidade(getValorConsultarPesssoa(), false, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getCampoConsultarPesssoa().equals("CPF")) {
				objs = getFacadeFactory().getPessoaFacade().consultarPorCPF(getValorConsultarPesssoa(), false, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getCampoConsultarPesssoa().equals("RG")) {
				objs = getFacadeFactory().getPessoaFacade().consultarPorRG(getValorConsultarPesssoa(), false, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getCampoConsultarPesssoa().equals("necessidadesEspeciais")) {
				//objs = getFacadeFactory().getPessoaFacade().consultarPorNecessidadesEspeciais(getValorConsultarPesssoa(), false, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			setListaConsultarPesssoa(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultarPesssoa(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarPesssoa() throws Exception {
		PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("pessoa");
		if (getMensagemDetalhada().equals("")) {
			this.getDownloadVO().setPessoa(obj);
		}
		Uteis.liberarListaMemoria(this.getListaConsultarPesssoa());
		this.setValorConsultarPesssoa(null);
		this.setCampoConsultarPesssoa(null);
	}

	public void limparCampoPesssoa() {
		this.getDownloadVO().setPessoa(new PessoaVO());
	}

	/**
	 * Rotina responsável por preencher a combo de consulta dos RichModal da telas.
	 */
	public List getTipoConsultarComboPesssoa() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("nome", "Nome"));
		//itens.add(new SelectItem("nomeCidade", "Cidade"));
		itens.add(new SelectItem("CPF", "CPF"));
		itens.add(new SelectItem("RG", "RG"));
		//itens.add(new SelectItem("necessidadesEspeciais", "Necessidades Especiais"));
		return itens;
	}

	/**
	 * Método responsável por processar a consulta na entidade <code>Arquivo</code> por meio dos parametros informados
	 * no richmodal. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pelos parâmentros
	 * informados no richModal montando automaticamente o resultado da consulta para apresentação.
	 */
	public void consultarArquivo() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultarArquivo().equals("codigo")) {
				if (getValorConsultarArquivo().equals("")) {
					setValorConsultarArquivo("0");
				}
				Integer valorInt = Integer.parseInt(getValorConsultarArquivo());
				objs = getFacadeFactory().getArquivoFacade().consultarPorCodigo(valorInt, false, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getCampoConsultarArquivo().equals("nome")) {
				objs = getFacadeFactory().getArquivoFacade().consultarPorNome(getValorConsultarArquivo(), false, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getCampoConsultarArquivo().equals("dataUpload")) {
				Date valorData = Uteis.getDate(getValorConsultarArquivo());
				objs = getFacadeFactory().getArquivoFacade().consultarPorDataUpload(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), false,
						Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getCampoConsultarArquivo().equals("codigoUsuario")) {
				if (getValorConsultarArquivo().equals("")) {
					setValorConsultarArquivo("0");
				}
				int valorInt = Integer.parseInt(getValorConsultarArquivo());
				objs = getFacadeFactory().getArquivoFacade().consultarPorCodigoUsuario(new Integer(valorInt), Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getCampoConsultarArquivo().equals("dataDisponibilizacao")) {
				Date valorData = Uteis.getDate(getValorConsultarArquivo());
				objs = getFacadeFactory().getArquivoFacade().consultarPorDataDisponibilizacao(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), false,
						Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getCampoConsultarArquivo().equals("nomeDisciplina")) {
				objs = getFacadeFactory().getArquivoFacade().consultarPorNomeDisciplina(getValorConsultarArquivo(), Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getCampoConsultarArquivo().equals("identificadorTurmaTurma")) {
				objs = getFacadeFactory().getArquivoFacade().consultarPorIdentificadorTurmaTurma(getValorConsultarArquivo(), Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getCampoConsultarArquivo().equals("situacao")) {
				objs = getFacadeFactory().getArquivoFacade().consultarPorSituacao(getValorConsultarArquivo(), false, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			setListaConsultarArquivo(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultarArquivo(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarArquivo() throws Exception {
		ArquivoVO obj = (ArquivoVO) context().getExternalContext().getRequestMap().get("arquivo");
		if (getMensagemDetalhada().equals("")) {
			this.getDownloadVO().setArquivo(obj);
		}
		Uteis.liberarListaMemoria(this.getListaConsultarArquivo());
		this.setValorConsultarArquivo(null);
		this.setCampoConsultarArquivo(null);
	}

	public void limparCampoArquivo() {
		this.getDownloadVO().setArquivo(new ArquivoVO());
	}

	/**
	 * Rotina responsável por preencher a combo de consulta dos RichModal da telas.
	 */
	public List getTipoConsultarComboArquivo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("dataUpload", "Data Upload"));
		itens.add(new SelectItem("codigoUsuario", "Responsável Upload"));
		itens.add(new SelectItem("dataDisponibilizacao", "Data Disponibilização"));
		itens.add(new SelectItem("nomeDisciplina", "Disciplina"));
		itens.add(new SelectItem("identificadorTurmaTurma", "Turma"));
		itens.add(new SelectItem("situacao", "Situação"));
		return itens;
	}

	/**
	 * Rotina responsável por atribui um javascript com o método de mascara para campos do tipo Data, CPF, CNPJ, etc.
	 */
	public String getMascaraConsulta() {
		return "";
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List getTipoConsultaCombo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("nomeArquivo", "Arquivo"));
		itens.add(new SelectItem("nomePessoa", "Pessoa"));
		itens.add(new SelectItem("dataDownload", "Data Download"));
		return itens;
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
	 */
	public String inicializarConsultar() {         removerObjetoMemoria(this);
		setListaConsulta(new ArrayList(0));
		setMensagemID("msg_entre_prmconsulta");
		return "consultar";
	}

	/**
	 * Operação que libera todos os recursos (atributos, listas, objetos) do backing bean. Garantindo uma melhor atuação
	 * do Garbage Coletor do Java. A mesma é automaticamente quando realiza o logout.
	 */
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
		downloadVO = null;
	}

	public String getCampoConsultarPesssoa() {
		return campoConsultarPesssoa;
	}

	public void setCampoConsultarPesssoa(String campoConsultarPesssoa) {
		this.campoConsultarPesssoa = campoConsultarPesssoa;
	}

	public String getValorConsultarPesssoa() {
		return valorConsultarPesssoa;
	}

	public void setValorConsultarPesssoa(String valorConsultarPesssoa) {
		this.valorConsultarPesssoa = valorConsultarPesssoa;
	}

	public List getListaConsultarPesssoa() {
		return listaConsultarPesssoa;
	}

	public void setListaConsultarPesssoa(List listaConsultarPesssoa) {
		this.listaConsultarPesssoa = listaConsultarPesssoa;
	}

	public String getCampoConsultarArquivo() {
		return campoConsultarArquivo;
	}

	public void setCampoConsultarArquivo(String campoConsultarArquivo) {
		this.campoConsultarArquivo = campoConsultarArquivo;
	}

	public String getValorConsultarArquivo() {
		return valorConsultarArquivo;
	}

	public void setValorConsultarArquivo(String valorConsultarArquivo) {
		this.valorConsultarArquivo = valorConsultarArquivo;
	}

	public List getListaConsultarArquivo() {
		return listaConsultarArquivo;
	}

	public void setListaConsultarArquivo(List listaConsultarArquivo) {
		this.listaConsultarArquivo = listaConsultarArquivo;
	}

	public DownloadVO getDownloadVO() {
		return downloadVO;
	}

	public void setDownloadVO(DownloadVO downloadVO) {
		this.downloadVO = downloadVO;
	}

	public List getListaSelectItemTurma() {
		return listaSelectItemTurma;
	}

	public void setListaSelectItemTurma(List listaSelectItemTurma) {
		this.listaSelectItemTurma = listaSelectItemTurma;
	}

}