

package negocio.comuns.bancocurriculum;

import negocio.comuns.arquitetura.SuperVO;


public class AreaProfissionalVO extends SuperVO{
    private String descricaoAreaProfissional;
    private String situacao;
    private Integer codigo;
    
    private Boolean selecionado;

    public AreaProfissionalVO(){
        super();
    }


    public String getDescricaoAreaProfissional() {
        return descricaoAreaProfissional;
    }

    public void setDescricaoAreaProfissional(String descricaoAreaProfissional) {
        if (descricaoAreaProfissional == null) {
            descricaoAreaProfissional = "";
        }
        this.descricaoAreaProfissional = descricaoAreaProfissional;
    }


    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    /**
     * @return the situacao
     */
    public String getSituacao_Apresentar() {
        if (situacao.equals("AT")) {
            return "Ativo";
        } else {
            return "Inativo";
        }
    }

    public String getSituacao() {
        if (situacao == null) {
            situacao = "AT";
        }
        return situacao;
    }

    /**
     * @param situacao the situacao to set
     */
    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }
    

	public Boolean getSelecionado() {
		if(selecionado == null){
			selecionado = false;
		}
		return selecionado;
	}


	public void setSelecionado(Boolean selecionado) {
		this.selecionado = selecionado;
	}
    
    


}
