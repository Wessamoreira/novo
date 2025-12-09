package negocio.comuns.bancocurriculum;

import negocio.comuns.arquitetura.SuperVO;

public class VagaAreaVO extends SuperVO {

    /**
     *
     */
    private static final long serialVersionUID = 7540333131535827190L;
    private Integer codigo;
    private VagasVO vaga;
    private AreaProfissionalVO areaProfissional;
    // Campos usado apenas para controle de tela
    private Boolean selecionado;

    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public VagasVO getVaga() {
        if (vaga == null) {
            vaga = new VagasVO();
        }
        return vaga;
    }

    public void setVaga(VagasVO vaga) {
        this.vaga = vaga;
    }

    public AreaProfissionalVO getAreaProfissional() {
        if (areaProfissional == null) {
            areaProfissional = new AreaProfissionalVO();
        }
        return areaProfissional;
    }

    public void setAreaProfissional(AreaProfissionalVO areaProfissional) {
        this.areaProfissional = areaProfissional;
    }

    /**
     * @return the selecionado
     */
    public Boolean getSelecionado() {
        if (selecionado == null) {
            selecionado = Boolean.FALSE;
        }
        return selecionado;
    }

    /**
     * @param selecionado the selecionado to set
     */
    public void setSelecionado(Boolean selecionado) {
        this.selecionado = selecionado;
    }
}
