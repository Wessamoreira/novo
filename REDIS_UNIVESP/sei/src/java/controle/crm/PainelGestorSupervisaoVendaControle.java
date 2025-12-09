/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controle.crm;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.arquitetura.enumeradores.Obrigatorio;
import negocio.comuns.crm.PainelGestorSupervisaoVendaVO;
import negocio.comuns.crm.enumerador.SituacaoProspectPipelineControleEnum;
import negocio.comuns.utilitarias.Uteis;

/**
 *
 * @author RODRIGO
 */
@Controller(value="PainelGestorSupervisaoVendaControle")
@Lazy
@Scope(value="viewScope")
public class PainelGestorSupervisaoVendaControle extends SuperControle{
        
    private List<SelectItem> listaSelectItemCampanha;
    private Date dataInicio;
    private Date dataTermino;
    private PainelGestorSupervisaoVendaVO painelGestorSupervisaoVendaVO;
    private Integer campanha;
    private Integer funcionario;
    private Boolean exibirRelatorio;
	private StringBuilder categoriasGraficoEstatisticasPorIdade;
	private StringBuilder categoriasGraficoEstatisticasPorRenda;
	private StringBuilder categoriasGraficoEstatisticasPorSexo;
	private StringBuilder categoriasGraficoEstatisticasPorFormacaoAcademica;
	private StringBuilder categoriasGraficoEstatisticasPorTipoEmpresa;
	private StringBuilder graficoEstatisticasPorIdade;
	private StringBuilder graficoEstatisticasPorSexo;
	private StringBuilder graficoEstatisticasPorRenda;
	private StringBuilder graficoEstatisticasPorFormacaoAcademica;
	private StringBuilder graficoEstatisticasPorTipoEmpresa;
        

    public PainelGestorSupervisaoVendaVO getPainelGestorSupervisaoVendaVO() {
        if(painelGestorSupervisaoVendaVO == null){
            painelGestorSupervisaoVendaVO = new PainelGestorSupervisaoVendaVO();
        }
        return painelGestorSupervisaoVendaVO;
    }

    public void setPainelGestorSupervisaoVendaVO(PainelGestorSupervisaoVendaVO painelGestorSupervisaoVendaVO) {
        this.painelGestorSupervisaoVendaVO = painelGestorSupervisaoVendaVO;
    }
    
    public void executarGeracaoDadosEstatisticaCampanha(){
        try{
            setPainelGestorSupervisaoVendaVO(getFacadeFactory().getPainelGestorSupervisaoVendaFacade().executarGeracaoDadosEstatisticaCampanha(getCampanha(), getFuncionario(), getDataInicio(), getDataTermino()));
            context().getExternalContext().getRequestMap().put("urlXmlSucesso", "http://192.168.0.15/repositorio/XML/etapas.xml");
            context().getExternalContext().getRequestMap().put("time", new Date().getTime());
            setExibirRelatorio(true);
            setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
        }catch(Exception e){
            setExibirRelatorio(false);
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }
    
    private Boolean abrirModalPerfilProspect;

    public Boolean getAbrirModalPerfilProspect() {
        if(abrirModalPerfilProspect == null){
            abrirModalPerfilProspect = false;
        }
        return abrirModalPerfilProspect;
    }
    
    

    public void setAbrirModalPerfilProspect(Boolean abrirModalPerfilProspect) {
        this.abrirModalPerfilProspect = abrirModalPerfilProspect;
    }
    
    public String getModalPerfilProspect(){
        return getAbrirModalPerfilProspect()?"RichFaces.$('modalPerfilProspect').show();":"RichFaces.$('modalPerfilProspect').hide();";
    }
    
    public void realizarApresentacaoPerfilProspectSucesso(){
        try{
            getFacadeFactory().getPainelGestorSupervisaoVendaFacade().consultarEstatisticaoPerfilProspect(getPainelGestorSupervisaoVendaVO(), getPainelGestorSupervisaoVendaVO().getCampanha(), getPainelGestorSupervisaoVendaVO().getVendedor().getCodigo(),
                    getDataInicio(), getDataTermino(), SituacaoProspectPipelineControleEnum.FINALIZADO_SUCESSO);
            montarCategoriasGraficoEstatisticaPorIdade(painelGestorSupervisaoVendaVO.getEstatisticaPorIdadeData());
            montarCategoriasGraficoEstatisticaPorRenda(painelGestorSupervisaoVendaVO.getEstatisticaPorRendaData());
            montarCategoriasGraficoEstatisticaPorFormacaoAcademica(painelGestorSupervisaoVendaVO.getEstatisticaPorFormacaoAcademicaData());
            getPainelGestorSupervisaoVendaVO().setEstatisticaPorIdade(montarGraficoEstatisticaPorIdade());
            getPainelGestorSupervisaoVendaVO().setEstatisticaPorRenda( montarGraficoEstatisticaPorRenda());
            getPainelGestorSupervisaoVendaVO().setEstatisticaPorSexo(montarGraficoEstatisticaPorSexo());
            getPainelGestorSupervisaoVendaVO().setEstatisticaPorTipoEmpresa(montarGraficoEstatisticaPorTipoEmpresa());
            getPainelGestorSupervisaoVendaVO().setEstatisticaPorFormacaoAcademica(montarGraficoEstatisticaPorFormacaoAcademica());
            setAbrirModalPerfilProspect(true);
            setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
        }catch(Exception e){
            setAbrirModalPerfilProspect(false);
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }
            
    public void realizarApresentacaoPerfilProspectInsucesso(){
        try{
            getFacadeFactory().getPainelGestorSupervisaoVendaFacade().consultarEstatisticaoPerfilProspect(getPainelGestorSupervisaoVendaVO(), getPainelGestorSupervisaoVendaVO().getCampanha(), getPainelGestorSupervisaoVendaVO().getVendedor().getCodigo(),
                    getDataInicio(), getDataTermino(), SituacaoProspectPipelineControleEnum.FINALIZADO_INSUCESSO);
            montarCategoriasGraficoEstatisticaPorIdade(painelGestorSupervisaoVendaVO.getEstatisticaPorIdadeData());
            montarCategoriasGraficoEstatisticaPorRenda(painelGestorSupervisaoVendaVO.getEstatisticaPorRendaData());
            montarCategoriasGraficoEstatisticaPorFormacaoAcademica(painelGestorSupervisaoVendaVO.getEstatisticaPorFormacaoAcademicaData());
            getPainelGestorSupervisaoVendaVO().setEstatisticaPorIdade(montarGraficoEstatisticaPorIdade());
            getPainelGestorSupervisaoVendaVO().setEstatisticaPorRenda( montarGraficoEstatisticaPorRenda());
            getPainelGestorSupervisaoVendaVO().setEstatisticaPorSexo(montarGraficoEstatisticaPorSexo());
            getPainelGestorSupervisaoVendaVO().setEstatisticaPorTipoEmpresa(montarGraficoEstatisticaPorTipoEmpresa());
            getPainelGestorSupervisaoVendaVO().setEstatisticaPorFormacaoAcademica(montarGraficoEstatisticaPorFormacaoAcademica());
            setAbrirModalPerfilProspect(true);
            setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
        }catch(Exception e){
            setAbrirModalPerfilProspect(false);
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }
    
    public void realizarApresentacaoPerfilProspectNaoFinalizado(){
        try{
            getFacadeFactory().getPainelGestorSupervisaoVendaFacade().consultarEstatisticaoPerfilProspect(getPainelGestorSupervisaoVendaVO(), getPainelGestorSupervisaoVendaVO().getCampanha(), getPainelGestorSupervisaoVendaVO().getVendedor().getCodigo(),
                    getDataInicio(), getDataTermino(), SituacaoProspectPipelineControleEnum.NENHUM);
            montarCategoriasGraficoEstatisticaPorIdade(painelGestorSupervisaoVendaVO.getEstatisticaPorIdadeData());
            montarCategoriasGraficoEstatisticaPorRenda(painelGestorSupervisaoVendaVO.getEstatisticaPorRendaData());
            montarCategoriasGraficoEstatisticaPorFormacaoAcademica(painelGestorSupervisaoVendaVO.getEstatisticaPorFormacaoAcademicaData());
            getPainelGestorSupervisaoVendaVO().setEstatisticaPorIdade(montarGraficoEstatisticaPorIdade());
            getPainelGestorSupervisaoVendaVO().setEstatisticaPorRenda( montarGraficoEstatisticaPorRenda());
            getPainelGestorSupervisaoVendaVO().setEstatisticaPorSexo(montarGraficoEstatisticaPorSexo());
            getPainelGestorSupervisaoVendaVO().setEstatisticaPorTipoEmpresa(montarGraficoEstatisticaPorTipoEmpresa());
            getPainelGestorSupervisaoVendaVO().setEstatisticaPorFormacaoAcademica(montarGraficoEstatisticaPorFormacaoAcademica());
            setAbrirModalPerfilProspect(true);
            setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
        }catch(Exception e){
            setAbrirModalPerfilProspect(false);
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }
    

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataTermino() {
        return dataTermino;
    }

    public void setDataTermino(Date dataTermino) {
        this.dataTermino = dataTermino;
    }

    public List<SelectItem> getListaSelectItemCampanha() {
        if(listaSelectItemCampanha == null){
            try {
                listaSelectItemCampanha = getFacadeFactory().getCampanhaFacade().consultarListaSelectItemCampanha(getUnidadeEnsinoLogado().getCodigo(), "AT", Obrigatorio.SIM, null);
                if(listaSelectItemCampanha != null && !listaSelectItemCampanha.isEmpty()){
                    setCampanha((Integer)listaSelectItemCampanha.get(0).getValue());
                }
            } catch (Exception ex) {
                setMensagemDetalhada("msg_erro", ex.getMessage(), Uteis.ERRO);
            }
        }   
        return listaSelectItemCampanha;
    }

    public void setListaSelectItemCampanha(List<SelectItem> listaSelectItemCampanha) {
        this.listaSelectItemCampanha = listaSelectItemCampanha;
    }

    public Integer getCampanha() {
        return campanha;
    }

    public void setCampanha(Integer campanha) {
        this.campanha = campanha;
    }

    public Integer getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Integer funcionario) {
        this.funcionario = funcionario;
    }

    public Boolean getExibirRelatorio() {
        if(exibirRelatorio == null){
            exibirRelatorio = false;
        }
        return exibirRelatorio;
    }

    public void setExibirRelatorio(Boolean exibirRelatorio) {
        this.exibirRelatorio = exibirRelatorio;
    }
    

	public String montarCategoriasGraficoEstatisticaPorIdade(Map<String, Integer> map) {
		categoriasGraficoEstatisticasPorIdade = new StringBuilder();
		Boolean virgula = false;
		for (Map.Entry<String, Integer> categoria : map.entrySet()) {
			if (virgula) {
				categoriasGraficoEstatisticasPorIdade.append(", ");
			}
			categoriasGraficoEstatisticasPorIdade.append("'").append(categoria.getKey()).append("'");
			virgula = true;
		}
		return categoriasGraficoEstatisticasPorIdade.toString();
	}
	
	public String montarGraficoEstatisticaPorIdade() {
		graficoEstatisticasPorIdade = new StringBuilder();
		Boolean virgula = false;
		for (Map.Entry<String, Integer> estatisticaPorIdade : painelGestorSupervisaoVendaVO.getEstatisticaPorIdadeData().entrySet()) {
			if (virgula) {
				graficoEstatisticasPorIdade.append(", ");
			}
			graficoEstatisticasPorIdade.append("{name:").append("'").append(estatisticaPorIdade.getKey()).append("'").append(", ").append("y : ").append(estatisticaPorIdade.getValue());
			virgula = true;
			graficoEstatisticasPorIdade.append("}");
		}
		return graficoEstatisticasPorIdade.toString();
	}
	
	public String montarCategoriasGraficoEstatisticaPorRenda(Map<String, Integer> map) {
		categoriasGraficoEstatisticasPorRenda = new StringBuilder();
		Boolean virgula = false;
		categoriasGraficoEstatisticasPorRenda.append("[");
		for (Map.Entry<String, Integer> categoria : map.entrySet()) {
			if (virgula) {
				categoriasGraficoEstatisticasPorRenda.append(", ");
			}
			categoriasGraficoEstatisticasPorRenda.append("'").append(categoria.getKey()).append("'");
			virgula = true;
		}
		categoriasGraficoEstatisticasPorRenda.append("]");
		return categoriasGraficoEstatisticasPorRenda.toString();
	}
	
	public String montarGraficoEstatisticaPorRenda() {
		graficoEstatisticasPorRenda = new StringBuilder();
		Boolean virgula = false;
		for (Map.Entry<String, Integer> estatisticaPorRenda : painelGestorSupervisaoVendaVO.getEstatisticaPorRendaData().entrySet()) {
			if (virgula) {
				graficoEstatisticasPorRenda.append(", ");
			}
			graficoEstatisticasPorRenda.append("{name:").append("'").append(estatisticaPorRenda.getKey()).append("'").append(", ").append("y: ").append(estatisticaPorRenda.getValue());
			virgula = true;
			graficoEstatisticasPorRenda.append("}");
		}
		return graficoEstatisticasPorRenda.toString();
	}
	
	public String montarGraficoEstatisticaPorSexo() {
		graficoEstatisticasPorSexo = new StringBuilder();
		Boolean virgula = false;
		for (Map.Entry<String, Integer> estatisticaPorSexo : painelGestorSupervisaoVendaVO.getEstatisticaPorSexoData().entrySet()) {
			if (virgula) {
				graficoEstatisticasPorSexo.append(", ");
			}
			graficoEstatisticasPorSexo.append("{name:").append("'").append(estatisticaPorSexo.getKey()).append("'").append(", ").append("y: ").append(estatisticaPorSexo.getValue());
			virgula = true;
			graficoEstatisticasPorSexo.append("}");
		}
		return graficoEstatisticasPorSexo.toString();
	}
	
	public String montarGraficoEstatisticaPorTipoEmpresa() {
		graficoEstatisticasPorTipoEmpresa = new StringBuilder();
		Boolean virgula = false;
		if (painelGestorSupervisaoVendaVO.getEstatisticaPorTipoEmpresaData() != null) {
			for (Map.Entry<String, Integer> estatisticaPorTipoEmpresa : painelGestorSupervisaoVendaVO.getEstatisticaPorTipoEmpresaData().entrySet()) {
				if (virgula) {
					graficoEstatisticasPorTipoEmpresa.append(", ");
				}
				graficoEstatisticasPorTipoEmpresa.append("{name:").append("'").append(estatisticaPorTipoEmpresa.getKey()).append("'").append(", ").append("y: ").append(estatisticaPorTipoEmpresa.getValue());
				virgula = true;
				graficoEstatisticasPorTipoEmpresa.append("}");
			}
		}
		return graficoEstatisticasPorTipoEmpresa.toString();
	}
	
	public String montarGraficoEstatisticaPorFormacaoAcademica() {
		graficoEstatisticasPorFormacaoAcademica = new StringBuilder();
		Boolean virgula = false;
		if (painelGestorSupervisaoVendaVO.getEstatisticaPorFormacaoAcademicaData() != null) {
			for (Map.Entry<String, Integer> estatisticaPorFormacaoAcademica : painelGestorSupervisaoVendaVO.getEstatisticaPorFormacaoAcademicaData().entrySet()) {
				if (virgula) {
					graficoEstatisticasPorFormacaoAcademica.append(", ");
				}
				graficoEstatisticasPorFormacaoAcademica.append("{name:").append("'").append(estatisticaPorFormacaoAcademica.getKey()).append("'").append(", ").append("y: ").append(estatisticaPorFormacaoAcademica.getValue());
				virgula = true;
				graficoEstatisticasPorFormacaoAcademica.append("}");
			}
		}
		return graficoEstatisticasPorFormacaoAcademica.toString();
	}
	
	public StringBuilder getCategoriasGraficoEstatisticasPorIdade() {
		if (categoriasGraficoEstatisticasPorIdade == null) {
			categoriasGraficoEstatisticasPorIdade = new StringBuilder();
		}
		return categoriasGraficoEstatisticasPorIdade;
	}

	public void setCategoriasGraficoEstatisticasPorIdade(StringBuilder categoriasGraficoEstatisticasPorIdade) {
		this.categoriasGraficoEstatisticasPorIdade = categoriasGraficoEstatisticasPorIdade;
	}

	public StringBuilder getGraficoEstatisticasPorIdade() {
		if (graficoEstatisticasPorIdade == null) {
			graficoEstatisticasPorIdade = new StringBuilder();
		}
		return graficoEstatisticasPorIdade;
	}

	public void setGraficoEstatisticasPorIdade(StringBuilder graficoEstatisticasPorIdade) {
		this.graficoEstatisticasPorIdade = graficoEstatisticasPorIdade;
	}

	public StringBuilder getGraficoEstatisticasPorSexo() {
		if (graficoEstatisticasPorSexo == null) {
			graficoEstatisticasPorSexo = new StringBuilder();
		}
		return graficoEstatisticasPorSexo;
	}

	public void setGraficoEstatisticasPorSexo(StringBuilder graficoEstatisticasPorSexo) {
		this.graficoEstatisticasPorSexo = graficoEstatisticasPorSexo;
	}

	public StringBuilder getGraficoEstatisticasPorRenda() {
		if (graficoEstatisticasPorRenda == null) {
			graficoEstatisticasPorRenda = new StringBuilder();
		}
		return graficoEstatisticasPorRenda;
	}

	public void setGraficoEstatisticasPorRenda(StringBuilder graficoEstatisticasPorRenda) {
		this.graficoEstatisticasPorRenda = graficoEstatisticasPorRenda;
	}

	public StringBuilder getGraficoEstatisticasPorFormacaoAcademica() {
		if (graficoEstatisticasPorFormacaoAcademica == null) {
			graficoEstatisticasPorFormacaoAcademica = new StringBuilder();
		}
		return graficoEstatisticasPorFormacaoAcademica;
	}

	public void setGraficoEstatisticasPorFormacaoAcademica(StringBuilder graficoEstatisticasPorFormacaoAcademica) {
		this.graficoEstatisticasPorFormacaoAcademica = graficoEstatisticasPorFormacaoAcademica;
	}

	public StringBuilder getGraficoEstatisticasPorTipoEmpresa() {
		if (graficoEstatisticasPorTipoEmpresa == null) {
			graficoEstatisticasPorTipoEmpresa = new StringBuilder();
		}
		return graficoEstatisticasPorTipoEmpresa;
	}

	public void setGraficoEstatisticasPorTipoEmpresa(StringBuilder graficoEstatisticasPorTipoEmpresa) {
		this.graficoEstatisticasPorTipoEmpresa = graficoEstatisticasPorTipoEmpresa;
	}

	public StringBuilder getCategoriasGraficoEstatisticasPorRenda() {
		if (categoriasGraficoEstatisticasPorRenda == null) {
			categoriasGraficoEstatisticasPorRenda = new StringBuilder();
		}
		return categoriasGraficoEstatisticasPorRenda;
	}

	public void setCategoriasGraficoEstatisticasPorRenda(StringBuilder categoriasGraficoEstatisticasPorRenda) {
		this.categoriasGraficoEstatisticasPorRenda = categoriasGraficoEstatisticasPorRenda;
	}

	public StringBuilder getCategoriasGraficoEstatisticasPorSexo() {
		if (categoriasGraficoEstatisticasPorSexo == null) {
			categoriasGraficoEstatisticasPorSexo = new StringBuilder();
		}
		return categoriasGraficoEstatisticasPorSexo;
	}

	public void setCategoriasGraficoEstatisticasPorSexo(StringBuilder categoriasGraficoEstatisticasPorSexo) {
		this.categoriasGraficoEstatisticasPorSexo = categoriasGraficoEstatisticasPorSexo;
	}

	public StringBuilder getCategoriasGraficoEstatisticasPorFormacaoAcademica() {
		if (categoriasGraficoEstatisticasPorFormacaoAcademica == null) {
			categoriasGraficoEstatisticasPorFormacaoAcademica = new StringBuilder();
		}
		return categoriasGraficoEstatisticasPorFormacaoAcademica;
	}

	public void setCategoriasGraficoEstatisticasPorFormacaoAcademica(StringBuilder categoriasGraficoEstatisticasPorFormacaoAcademica) {
		this.categoriasGraficoEstatisticasPorFormacaoAcademica = categoriasGraficoEstatisticasPorFormacaoAcademica;
	}

	public StringBuilder getCategoriasGraficoEstatisticasPorTipoEmpresa() {
		if (categoriasGraficoEstatisticasPorTipoEmpresa == null) {
			categoriasGraficoEstatisticasPorTipoEmpresa = new StringBuilder();
		}
		return categoriasGraficoEstatisticasPorTipoEmpresa;
	}

	public void setCategoriasGraficoEstatisticasPorTipoEmpresa(StringBuilder categoriasGraficoEstatisticasPorTipoEmpresa) {
		this.categoriasGraficoEstatisticasPorTipoEmpresa = categoriasGraficoEstatisticasPorTipoEmpresa;
	}
	
	public String montarCategoriasGraficoEstatisticaPorFormacaoAcademica(Map<String, Integer> map) {
		categoriasGraficoEstatisticasPorFormacaoAcademica = new StringBuilder();
		Boolean virgula = false;
		categoriasGraficoEstatisticasPorFormacaoAcademica.append("[");
		for (Map.Entry<String, Integer> categoria : map.entrySet()) {
			if (virgula) {
				categoriasGraficoEstatisticasPorFormacaoAcademica.append(", ");
			}
			categoriasGraficoEstatisticasPorFormacaoAcademica.append("'").append(categoria.getKey()).append("'");
			virgula = true;
		}
		categoriasGraficoEstatisticasPorFormacaoAcademica.append("]");
		return categoriasGraficoEstatisticasPorFormacaoAcademica.toString();
	}
	
}
