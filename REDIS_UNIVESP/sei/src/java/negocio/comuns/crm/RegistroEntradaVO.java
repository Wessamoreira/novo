package negocio.comuns.crm;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.crm.enumerador.TipoUploadEnum;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade RegistroEntrada. Classe do tipo VO - Value Object 
 * composta pelos atributos da entidade com visibilidade protegida e os métodos de acesso a estes atributos.
 * Classe utilizada para apresentar e manter em memória os dados desta entidade.
 * @see SuperVO
*/

public class RegistroEntradaVO extends SuperVO {
	
    protected Integer codigo;
    protected String descricao;
    protected Date dataEntrada;
    /** Atributo responsável por manter os objetos da classe <code>RegistroEntradaProspects</code>. */
    private List<RegistroEntradaProspectsVO> registroEntradaProspectsVOs;
    /** Atributo responsável por manter o objeto relacionado da classe <code>UnidadeEnsino </code>.*/
    protected UnidadeEnsinoVO unidadeEnsino;
    /** Atributo responsável por manter o objeto relacionado da classe <code>Curso </code>.*/
    protected CursoVO cursoEntrada;
    private TipoUploadEnum tipoUpload;
    private String delimitador;
    /**
     * Transient
     */
    private List<SelectItem> listaSelectItemFuncionarioCargo;
    private FuncionarioCargoVO funcionarioCargoSugerido;
	
    /**
     * Construtor padrão da classe <code>RegistroEntrada</code>.
     * Cria uma nova instância desta entidade, inicializando automaticamente seus atributos (Classe VO).
    */
    public RegistroEntradaVO() {
        super();
    }
     
	
	

    /**
     * Retorna o objeto da classe <code>Curso</code> relacionado com (<code>RegistroEntrada</code>).
    */
    public CursoVO getCursoEntrada() {
        if (cursoEntrada == null) {
            cursoEntrada = new CursoVO();
        }
        return (cursoEntrada);
    }
     
    /**
     * Define o objeto da classe <code>Curso</code> relacionado com (<code>RegistroEntrada</code>).
    */
    public void setCursoEntrada( CursoVO obj) {
        this.cursoEntrada = obj;
    }

    /**
     * Retorna o objeto da classe <code>UnidadeEnsino</code> relacionado com (<code>RegistroEntrada</code>).
    */
    public UnidadeEnsinoVO getUnidadeEnsino() {
        if (unidadeEnsino == null) {
            unidadeEnsino = new UnidadeEnsinoVO();
        }
        return (unidadeEnsino);
    }
     
    /**
     * Define o objeto da classe <code>UnidadeEnsino</code> relacionado com (<code>RegistroEntrada</code>).
    */
    public void setUnidadeEnsino( UnidadeEnsinoVO obj) {
        this.unidadeEnsino = obj;
    }

    /** Retorna Atributo responsável por manter os objetos da classe <code>RegistroEntradaProspects</code>. */
    public List<RegistroEntradaProspectsVO> getRegistroEntradaProspectsVOs() {
        if (registroEntradaProspectsVOs == null) {
            registroEntradaProspectsVOs = new ArrayList(0);
        }
        return (registroEntradaProspectsVOs);
    }
     
    /** Define Atributo responsável por manter os objetos da classe <code>RegistroEntradaProspects</code>. */
    public void setRegistroEntradaProspectsVOs( List<RegistroEntradaProspectsVO> registroEntradaProspectsVOs ) {
        this.registroEntradaProspectsVOs = registroEntradaProspectsVOs;
    }

    public Date getDataEntrada() {
        if (dataEntrada == null) {
            dataEntrada = new Date();
        }
        return (dataEntrada);
    }
     
    /**
     * Operação responsável por retornar um atributo do tipo data no formato padrão dd/mm/aaaa. 
    */
    public String getDataEntrada_Apresentar() {
        return (Uteis.getData(dataEntrada));
    }
     
    public void setDataEntrada( Date dataEntrada ) {
        this.dataEntrada = dataEntrada;
    }

    public String getDescricao() {
        if (descricao == null) {
            return "";
        }
        return (descricao);
    }
     
    public void setDescricao( String descricao ) {
        this.descricao = descricao;
    }

    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return (codigo);
    }
     
    public void setCodigo( Integer codigo ) {
        this.codigo = codigo;
    }

    public TipoUploadEnum getTipoUpload() {
        if(tipoUpload == null){
            tipoUpload = TipoUploadEnum.NENHUM;
        }
        return tipoUpload;
    }

    public void setTipoUpload(TipoUploadEnum tipoUpload) {
        this.tipoUpload = tipoUpload;
    }

    public String getDelimitador() {
        if(delimitador == null){
            delimitador = "";
        }
        return delimitador;
    }

    public void setDelimitador(String delimitador) {
        this.delimitador = delimitador;
    }

	public FuncionarioCargoVO getFuncionarioCargoSugerido() {
		 if(funcionarioCargoSugerido == null){
			 funcionarioCargoSugerido = new FuncionarioCargoVO();
	     }
		return funcionarioCargoSugerido;
	}

	public void setFuncionarioCargoSugerido(FuncionarioCargoVO funcionarioCargoSugerido) {
		this.funcionarioCargoSugerido = funcionarioCargoSugerido;
	}

	public List<SelectItem> getListaSelectItemFuncionarioCargo() {
		if(listaSelectItemFuncionarioCargo == null) {
			listaSelectItemFuncionarioCargo = new ArrayList<>();
		}
		return listaSelectItemFuncionarioCargo;
	}

	public void setListaSelectItemFuncionarioCargo(List<SelectItem> listaSelectItemFuncionarioCargo) {
		this.listaSelectItemFuncionarioCargo = listaSelectItemFuncionarioCargo;
	}
    
    


    
}