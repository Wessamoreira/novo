package negocio.comuns.utilitarias;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.administrativo.LayoutRelatorioSEIDecidirVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoModuloEnum;
import negocio.comuns.arquitetura.enumeradores.TipoVisaoEnum;
import negocio.comuns.utilitarias.dominios.ModeloApresentacaoDashboardEnum;
import negocio.comuns.utilitarias.dominios.TipoDashboardEnum;

public class DashboardVO extends ProgressBarVO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3272077024100769748L;

	protected Integer codigo;
    
    private TipoDashboardEnum tipoDashboard;
    
    private TipoVisaoEnum ambiente;
    
    private PerfilAcessoModuloEnum modulo;
    
    private Boolean maximizado;
        
    private Boolean ocultar;
    
    private Integer ordem;
    
    private UsuarioVO usuarioVO;
    
    private LayoutRelatorioSEIDecidirVO layoutRelatorioSEIDecidirVO;
    
    private String grafico;
    
    private String urlPdfView;
    
    private String html;
    
    private String titulo;
    
    private String icone;
    
    private List<Object> objectVOs;
    
    private ModeloApresentacaoDashboardEnum modeloApresentacaoDashboard;
    
    

	public DashboardVO() {
		super();
		
	}

	public DashboardVO(TipoDashboardEnum tipoDashboard, Boolean ocultar, Integer ordem, TipoVisaoEnum ambiente,
			UsuarioVO usuarioVO) {
		super();		
		this.tipoDashboard = tipoDashboard;
		this.ambiente = ambiente;
		this.ocultar = ocultar;
		this.ordem = ordem;
		this.usuarioVO = usuarioVO;
	}
	
	public DashboardVO(TipoDashboardEnum tipoDashboard, Boolean ocultar, Integer ordem, TipoVisaoEnum ambiente, PerfilAcessoModuloEnum modulo,
			UsuarioVO usuarioVO) {
		super();		
		this.tipoDashboard = tipoDashboard;
		this.ambiente = ambiente;
		this.ocultar = ocultar;
		this.ordem = ordem;
		this.modulo = modulo;
		this.usuarioVO = usuarioVO;
	}
	
	public DashboardVO(TipoDashboardEnum tipoDashboard, Boolean ocultar, Integer ordem, TipoVisaoEnum ambiente, PerfilAcessoModuloEnum modulo, ModeloApresentacaoDashboardEnum modeloApresentacaoDashboard,
			UsuarioVO usuarioVO) {
		super();		
		this.tipoDashboard = tipoDashboard;
		this.ambiente = ambiente;
		this.ocultar = ocultar;
		this.ordem = ordem;
		this.modulo = modulo;
		this.usuarioVO = usuarioVO;
		this.modeloApresentacaoDashboard = modeloApresentacaoDashboard;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public TipoDashboardEnum getTipoDashboard() {
		return tipoDashboard;
	}

	public void setTipoDashboard(TipoDashboardEnum tipoDashboard) {
		this.tipoDashboard = tipoDashboard;
	}

	public Boolean getMaximizado() {
		if(maximizado == null) {
			maximizado =  true;
		}
		return maximizado;
	}

	public void setMaximizado(Boolean maximizado) {
		this.maximizado = maximizado;
	}

	public Boolean getOcultar() {
		if(ocultar == null) {
			ocultar =  false;
		}
		return ocultar;
	}

	public void setOcultar(Boolean ocultar) {
		this.ocultar = ocultar;
	}

	public Integer getOrdem() {
		if(ordem == null) {
			ordem = 1;
		}
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	public UsuarioVO getUsuarioVO() {
		if(usuarioVO == null) {
			usuarioVO =  new UsuarioVO();
		}
		return usuarioVO;
	}

	public void setUsuarioVO(UsuarioVO usuarioVO) {
		this.usuarioVO = usuarioVO;
	}

	public LayoutRelatorioSEIDecidirVO getLayoutRelatorioSEIDecidirVO() {
		if(layoutRelatorioSEIDecidirVO == null) {
			layoutRelatorioSEIDecidirVO = new LayoutRelatorioSEIDecidirVO();
		}
		return layoutRelatorioSEIDecidirVO;
	}

	public void setLayoutRelatorioSEIDecidirVO(LayoutRelatorioSEIDecidirVO layoutRelatorioSEIDecidirVO) {
		this.layoutRelatorioSEIDecidirVO = layoutRelatorioSEIDecidirVO;
	}

	public String getGrafico() {
		if(grafico == null) {
			grafico = "";
		}
		return grafico;
	}

	public void setGrafico(String grafico) {
		this.grafico = grafico;
	}

	public String getUrlPdfView() {
		if(urlPdfView == null) {
			urlPdfView = "";
		}
		return urlPdfView;
	}

	public void setUrlPdfView(String urlPdfView) {
		this.urlPdfView = urlPdfView;
	}

	public String getHtml() {
		if(html == null) {
			html = "";
		}
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public String getTitulo() {
		if(titulo == null) {
			titulo =  Uteis.isAtributoPreenchido(getLayoutRelatorioSEIDecidirVO()) ? getLayoutRelatorioSEIDecidirVO().getDescricao() : getTipoDashboard().getTitulo();
		}
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getIcone() {
		if(icone == null) {
			icone =  Uteis.isAtributoPreenchido(getLayoutRelatorioSEIDecidirVO()) ? getTipoDashboard().getIconePadrao() : getTipoDashboard().getIconePadrao();
		}
		return icone;
	}

	public void setIcone(String icone) {
		this.icone = icone;
	}

	public List<Object> getObjectVOs() {
		if(objectVOs == null) {
			objectVOs = new ArrayList<Object>(0);
		}
		return objectVOs;
	}

	public void setObjectVOs(List<Object> objectVOs) {
		this.objectVOs = objectVOs;
	}

	public TipoVisaoEnum getAmbiente() {
		if(ambiente == null) {
			ambiente =  TipoVisaoEnum.ADMINISTRATIVA;
		}
		return ambiente;
	}

	public void setAmbiente(TipoVisaoEnum ambiente) {
		this.ambiente = ambiente;
	}

	

	public PerfilAcessoModuloEnum getModulo() {
		if(modulo == null) {
			modulo =  PerfilAcessoModuloEnum.TODOS;
		}
		return modulo;
	}

	public void setModulo(PerfilAcessoModuloEnum modulo) {
		this.modulo = modulo;
	}

	public ModeloApresentacaoDashboardEnum getModeloApresentacaoDashboard() {
		if(modeloApresentacaoDashboard == null) {
			modeloApresentacaoDashboard = ModeloApresentacaoDashboardEnum.DEFAULT; 
		}
		return modeloApresentacaoDashboard;
	}

	public void setModeloApresentacaoDashboard(ModeloApresentacaoDashboardEnum modeloApresentacaoDashboard) {
		this.modeloApresentacaoDashboard = modeloApresentacaoDashboard;
	}
    
    
    
}
