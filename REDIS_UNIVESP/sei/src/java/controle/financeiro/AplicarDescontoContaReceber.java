/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controle.financeiro;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.DescontoProgressivoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PlanoDescontoVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis;

/**
 *
 * @author Rodrigo
 */
@Controller("AplicarDescontoContaReceber")
@Scope("request")
@Lazy
public class AplicarDescontoContaReceber extends SuperControle {

    MatriculaVO matriculaVO;
    Integer matriculaPeriodo;
    PlanoDescontoVO planoDescontoVO;
    DescontoProgressivoVO descontoProgressivoVO;
    List<ContaReceberVO> contaReceberVOs;
    List<SelectItem> tipoConsultaCombo;
    List<SelectItem> listaSelectItemSituacaoMatricula;
    List<SelectItem> listaSelectItemSituacaoFinanceiraMatricula;

    public AplicarDescontoContaReceber() {
    }

     public void gravar() {
        try {
        } catch (Exception e) {
        }
    }

     public void editar(){
     
     }

    public String novo() {
        removerObjetoMemoria(this);
        return "consultar";
    }

    public String consultar() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			if (getControleConsulta().getCampoConsulta().equals("matricula")) {
				MatriculaVO obj = getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(getControleConsulta().getValorConsulta(), this.getUnidadeEnsinoLogado().getCodigo(), true,
						Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
				if (!obj.getMatricula().equals("")) {
					objs.add(obj);
				}
			}

			if (getControleConsulta().getCampoConsulta().equals("nomePessoa")) {
				objs = getFacadeFactory().getMatriculaFacade().consultarPorNomePessoa(getControleConsulta().getValorConsulta(), this.getUnidadeEnsinoLogado().getCodigo(), true,
						Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("cpf")) {
				objs = getFacadeFactory().getMatriculaFacade().consultarPorCPF(getControleConsulta().getValorConsulta(), this.getUnidadeEnsinoLogado().getCodigo(), true,
						Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeCurso")) {
				objs = getFacadeFactory().getMatriculaFacade().consultarPorNomeCurso(getControleConsulta().getValorConsulta(), this.getUnidadeEnsinoLogado().getCodigo(), true,
						Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("data")) {
				Date valorData = Uteis.getDate(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getMatriculaFacade().consultarPorData(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59),
						this.getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("situacao")) {
				objs = getFacadeFactory().getMatriculaFacade().consultarPorSituacao(getControleConsulta().getValorConsulta(), this.getUnidadeEnsinoLogado().getCodigo(), true,
						Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("situacaoFinanceira")) {
				objs = getFacadeFactory().getMatriculaFacade().consultarPorSituacaoFinanceira(getControleConsulta().getValorConsulta(), this.getUnidadeEnsinoLogado().getCodigo(), true,
						Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeResponsavel")) {
				objs = getFacadeFactory().getMatriculaFacade().consultarPorNomePessoa(getControleConsulta().getValorConsulta(), this.getUnidadeEnsinoLogado().getCodigo(), true,
						Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			}
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			return "";
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}

    /**
     * Rotina responsável por preencher a combo de consulta da telas.
     */
    public List getTipoConsultaCombo() {
        if (tipoConsultaCombo == null) {
            tipoConsultaCombo = new ArrayList(0);            
            tipoConsultaCombo.add(new SelectItem("nomePessoa", "Aluno"));
            tipoConsultaCombo.add(new SelectItem("cpf", "CPF"));
            tipoConsultaCombo.add(new SelectItem("matricula", "Matrícula"));
            tipoConsultaCombo.add(new SelectItem("nomeCurso", "Curso"));
            tipoConsultaCombo.add(new SelectItem("data", "Data"));
            tipoConsultaCombo.add(new SelectItem("situacao", "Situação"));
            tipoConsultaCombo.add(new SelectItem("situacaoFinanceira", "Situação Financeira"));
        }
        return tipoConsultaCombo;
    }

    public List getListaSelectItemSituacaoMatricula() throws Exception {
        if (listaSelectItemSituacaoMatricula == null) {
            listaSelectItemSituacaoMatricula = new ArrayList(0);
            Hashtable situacaoMatriculas = (Hashtable) Dominios.getSituacaoMatricula();
            Enumeration keys = situacaoMatriculas.keys();
            while (keys.hasMoreElements()) {
                String value = (String) keys.nextElement();
                String label = (String) situacaoMatriculas.get(value);
                listaSelectItemSituacaoMatricula.add(new SelectItem(value, label));
            }
            SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
            Collections.sort((List) listaSelectItemSituacaoMatricula, ordenador);
        }
        return listaSelectItemSituacaoMatricula;
    }

    public Boolean getSituacaoFinanceira() {

        if (getControleConsulta().getCampoConsulta().equals("situacaoFinanceira")) {
            return true;
        }
        return false;
    }

    public Boolean getApresentarComboBoxSituacao() {
		if (getControleConsulta().getCampoConsulta().equals("situacao")) {
			return true;
		}
		return false;
	}

    

    public List getListaSelectItemSituacaoFinanceiraMatricula() throws Exception {
        if (listaSelectItemSituacaoFinanceiraMatricula == null) {
            listaSelectItemSituacaoFinanceiraMatricula = new ArrayList(0);
            Hashtable situacaoMatriculas = (Hashtable) Dominios.getSituacaoFinanceiraMatricula();
            Enumeration keys = situacaoMatriculas.keys();
            while (keys.hasMoreElements()) {
                String value = (String) keys.nextElement();
                String label = (String) situacaoMatriculas.get(value);
                listaSelectItemSituacaoFinanceiraMatricula.add(new SelectItem(value, label));
            }
            SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
            Collections.sort((List) listaSelectItemSituacaoFinanceiraMatricula, ordenador);
        }
        return listaSelectItemSituacaoFinanceiraMatricula;
    }

   

    public Integer getMatriculaPeriodo() {
        if (matriculaPeriodo == null) {
            matriculaPeriodo = 0;
        }
        return matriculaPeriodo;
    }

    public void setMatriculaPeriodo(Integer matriculaPeriodo) {
        this.matriculaPeriodo = matriculaPeriodo;
    }

    public List<ContaReceberVO> getContaReceberVOs() {
        if (contaReceberVOs == null) {
            contaReceberVOs = new ArrayList<ContaReceberVO>();
        }
        return contaReceberVOs;
    }

    public void setContaReceberVOs(List<ContaReceberVO> contaReceberVOs) {
        this.contaReceberVOs = contaReceberVOs;
    }

    public DescontoProgressivoVO getDescontoProgressivoVO() {
        if (descontoProgressivoVO == null) {
            descontoProgressivoVO = new DescontoProgressivoVO();
        }
        return descontoProgressivoVO;
    }

    public void setDescontoProgressivoVO(DescontoProgressivoVO descontoProgressivoVO) {
        this.descontoProgressivoVO = descontoProgressivoVO;
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

    public PlanoDescontoVO getPlanoDescontoVO() {
        if (planoDescontoVO == null) {
            planoDescontoVO = new PlanoDescontoVO();
        }
        return planoDescontoVO;
    }

    public void setPlanoDescontoVO(PlanoDescontoVO planoDescontoVO) {
        this.planoDescontoVO = planoDescontoVO;
    }
}
