package controle.academico;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.enumeradores.SituacaoTipoAdvertenciaEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.Uteis;

@Controller("UnificacaoCadastroPessoaControle")
@Scope("viewScope")
@Lazy
public class UnificacaoCadastroPessoaControle extends SuperControle {

	private static final long serialVersionUID = 1L;
	private PessoaVO pessoaVO;
	private String valorConsultaPessoa;
	private String campoConsultaPessoa;
	private List listaConsultaPessoaVOs;
	
	private String campoConsultaAlunoUnificar;
	private String valorConsultaAlunoUnificar;
	private List<PessoaVO> listaConsultaAlunoUnificarVOs;
	private Boolean unificarUsuarioPessoa;
	private UsuarioVO usuarioManter;
	private List<UsuarioVO> usuarioVOs;

	public UnificacaoCadastroPessoaControle() throws Exception {
		removerObjetoMemoria(this);
		
	}

	public String novo() throws Exception {
		try {
			removerObjetoMemoria(this);
			setMensagemID("msg_entre_dados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("unificacaoCadastroPessoa.xhtml");
		} catch (Exception e) {
			return Uteis.getCaminhoRedirecionamentoNavegacao("unificacaoCadastroPessoa.xhtml");
		}
	}

	public List<SelectItem> getListaSelectItemSituacaoTipoAdvertencia() {
		List<SelectItem> objs = new ArrayList<SelectItem>();
		for (SituacaoTipoAdvertenciaEnum situacaoTipoAdvertenciaEnum : SituacaoTipoAdvertenciaEnum.values()) {
			objs.add(new SelectItem(situacaoTipoAdvertenciaEnum.name(), situacaoTipoAdvertenciaEnum.getValorApresentar()));
		}
		return objs;
	}

	public List<SelectItem> getTipoConsultaComboPessoaUnificar() {
		List<SelectItem> objs = new ArrayList<SelectItem>();
		objs.add(new SelectItem("NOME", "Nome Pessoa"));
		objs.add(new SelectItem("CPF", "CPF"));
		return objs;
	}
	
	public void consultarPessoaManter() {
		try {
			List objs = new ArrayList(0);
			if (getValorConsultaPessoa().equals("")) {
				throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
			}
			if (getCampoConsultaPessoa().equals("NOME")) {
				objs = getFacadeFactory().getPessoaFacade().consultaRapidaPorNome(getValorConsultaPessoa(), "", false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			if (getCampoConsultaPessoa().equals("CPF")) {
				objs = getFacadeFactory().getPessoaFacade().consultaRapidaPorCPF(getValorConsultaPessoa(), "", false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			setListaConsultaPessoaVOs(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaPessoaVOs(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void selecionarPessoaManter() throws Exception {
		try {
			PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("pessoaItens");
			getFacadeFactory().getPessoaFacade().consultarInformacoesBasicasPessoaUnificar(obj, getUsuarioLogado());
			setPessoaVO(obj);
			setValorConsultaPessoa("");
			setCampoConsultaPessoa("");
			getListaConsultaPessoaVOs().clear();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void consultarPessoaUnificacao() {
        try {
        	setListaConsultaAlunoUnificarVOs(getFacadeFactory().getPessoaFacade().consultarPessoaUnificacao(getPessoaVO(), getCampoConsultaAlunoUnificar(), getValorConsultaAlunoUnificar(), false, getUsuarioLogado()));
            setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
        } catch (Exception e) {
            getListaConsultaAlunoUnificarVOs().clear();
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
	}
	
	public void limparDadosPessoa() {
		setPessoaVO(new PessoaVO());
		getListaConsultaAlunoUnificarVOs().clear();
	}
	
	public void realizarUnificacaoPessoa() {
		try {
			getFacadeFactory().getPessoaFacade().realizarUnificacaoPessoa(getListaConsultaAlunoUnificarVOs(), getPessoaVO(), getUsuarioLogado());
			realizarVerificacaoPessoaManterPossuiUsuarioDuplicado();
			getListaConsultaAlunoUnificarVOs().clear();
		} catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
	}
	
	public void limparDadosModalPessoaUnificar() {
		
	}

	public PessoaVO getPessoaVO() {
		if (pessoaVO == null) {
			pessoaVO = new PessoaVO();
		}
		return pessoaVO;
	}

	public void setPessoaVO(PessoaVO pessoaVO) {
		this.pessoaVO = pessoaVO;
	}

	public String getValorConsultaPessoa() {
		if (valorConsultaPessoa == null) {
			valorConsultaPessoa = "";
		}
		return valorConsultaPessoa;
	}

	public void setValorConsultaPessoa(String valorConsultaPessoa) {
		this.valorConsultaPessoa = valorConsultaPessoa;
	}

	public String getCampoConsultaPessoa() {
		if (campoConsultaPessoa == null) {
			campoConsultaPessoa = "";
		}
		return campoConsultaPessoa;
	}

	public void setCampoConsultaPessoa(String campoConsultaPessoa) {
		this.campoConsultaPessoa = campoConsultaPessoa;
	}

	public List getListaConsultaPessoaVOs() {
		if (listaConsultaPessoaVOs == null) {
			listaConsultaPessoaVOs = new ArrayList<>(0);
		}
		return listaConsultaPessoaVOs;
	}

	public void setListaConsultaPessoaVOs(List listaConsultaPessoaVOs) {
		this.listaConsultaPessoaVOs = listaConsultaPessoaVOs;
	}

	public String getCampoConsultaAlunoUnificar() {
		if (campoConsultaAlunoUnificar == null) {
			campoConsultaAlunoUnificar = "";
		}
		return campoConsultaAlunoUnificar;
	}

	public void setCampoConsultaAlunoUnificar(String campoConsultaAlunoUnificar) {
		this.campoConsultaAlunoUnificar = campoConsultaAlunoUnificar;
	}

	public String getValorConsultaAlunoUnificar() {
		if (valorConsultaAlunoUnificar == null) {
			valorConsultaAlunoUnificar = "";
		}
		return valorConsultaAlunoUnificar;
	}

	public void setValorConsultaAlunoUnificar(String valorConsultaAlunoUnificar) {
		this.valorConsultaAlunoUnificar = valorConsultaAlunoUnificar;
	}

	public List<PessoaVO> getListaConsultaAlunoUnificarVOs() {
		if (listaConsultaAlunoUnificarVOs == null) {
			listaConsultaAlunoUnificarVOs = new ArrayList<PessoaVO>(0);
		}
		return listaConsultaAlunoUnificarVOs;
	}

	public void setListaConsultaAlunoUnificarVOs(List<PessoaVO> listaConsultaAlunoUnificarVOs) {
		this.listaConsultaAlunoUnificarVOs = listaConsultaAlunoUnificarVOs;
	}
	
	public Boolean getUnificarUsuarioPessoa() {
		if (unificarUsuarioPessoa == null) {
			unificarUsuarioPessoa = Boolean.FALSE;
		}
		return unificarUsuarioPessoa;
	}
	
	public void setUnificarUsuarioPessoa(Boolean unificarUsuarioPessoa) {
		this.unificarUsuarioPessoa = unificarUsuarioPessoa;
	}
	
	public UsuarioVO getUsuarioManter() {
		if (usuarioManter == null) {
			usuarioManter = new UsuarioVO();
		}
		return usuarioManter;
	}
	
	public void setUsuarioManter(UsuarioVO usuarioManter) {
		this.usuarioManter = usuarioManter;
	}
	
	public List<UsuarioVO> getUsuarioVOs() {
		if (usuarioVOs == null) {
			usuarioVOs = new ArrayList<>(0);
		}
		return usuarioVOs;
	}
	
	public void setUsuarioVOs(List<UsuarioVO> usuarioVOs) {
		this.usuarioVOs = usuarioVOs;
	}
	
	public void validarListaUsuarioPossuiDuplicidade(List<UsuarioVO> usuarioVOs) {
		setUsuarioManter(new UsuarioVO());
		setUsuarioVOs(new ArrayList<>(0));
		if (Uteis.isAtributoPreenchido(usuarioVOs)) {
			Map<String, List<UsuarioVO>> map = usuarioVOs.stream().filter(u -> Uteis.isAtributoPreenchido(u.getTipoUsuario())).collect(Collectors.groupingBy(UsuarioVO::getTipoUsuario));
			if (Uteis.isAtributoPreenchido(map)) {
				for (Entry<String, List<UsuarioVO>> entry : map.entrySet()) {
					if (entry.getValue().size() > 1) {
						getUsuarioVOs().addAll(entry.getValue());
					}
				}
			}
			setUnificarUsuarioPessoa(Uteis.isAtributoPreenchido(getUsuarioVOs()));
		}
	}
	
	public void realizarVerificacaoPessoaManterPossuiUsuarioDuplicado() {
		try {
			if (Uteis.isAtributoPreenchido(getPessoaVO())) {
				List<UsuarioVO> usuarioVOs = getFacadeFactory().getUsuarioFacade().consultarUsuariosPorPessoa(getPessoaVO().getCodigo(), Boolean.FALSE, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				validarListaUsuarioPossuiDuplicidade(usuarioVOs);
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void selecionarUsuario() {
		UsuarioVO obj = (UsuarioVO) context().getExternalContext().getRequestMap().get("usuarioItens");
		try {
			if (Uteis.isAtributoPreenchido(obj)) {
				if (Uteis.isAtributoPreenchido(getUsuarioManter())) {
					if (obj.getSelecionado()) {
						obj.setSelecionado(Boolean.FALSE);
					} else {
						obj.setSelecionado(Boolean.TRUE);
					}
				} else {
					obj.setSelecionado(Boolean.FALSE);
					setUsuarioManter((UsuarioVO) obj.clone());
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void removerUsuarioPermanecer() {
		setUsuarioManter(new UsuarioVO());
		getUsuarioVOs().stream().forEach(u -> u.setSelecionado(Boolean.FALSE));
	}
	
	public void removerUsuarioUnificadoLista() {
		if (Uteis.isAtributoPreenchido(getUsuarioVOs()) && Uteis.isAtributoPreenchido(getUsuarioManter())) {
			getUsuarioVOs().removeIf(u -> u.getSelecionado() || u.getCodigo().equals(getUsuarioManter().getCodigo()));
			realizarVerificacaoPessoaManterPossuiUsuarioDuplicado();
		}
	}
	
	public void realizarUnificacaoUsuarios() {
		try {
			setOncompleteModal(Constantes.EMPTY);
			if (Uteis.isAtributoPreenchido(getUsuarioManter()) && Uteis.isAtributoPreenchido(getUsuarioVOs()) && getUsuarioVOs().stream().noneMatch(UsuarioVO::getSelecionado)) {
				throw new Exception("Ao menos 1 USUÁRIO que será unificado deve ser selecionado");
			}
			getFacadeFactory().getUsuarioFacade().realizarUnificacaoUsuario(getUsuarioVOs(), getUsuarioManter(), getUsuarioLogado());
			removerUsuarioUnificadoLista();
			setOncompleteModal(!getUnificarUsuarioPessoa() ? "RichFaces.$('panelUnificacaoUsuario').hide();" : Constantes.EMPTY);
			setMensagemID("Usuário(s) unificado(s) com sucesso", Uteis.SUCESSO, Boolean.TRUE);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
}
