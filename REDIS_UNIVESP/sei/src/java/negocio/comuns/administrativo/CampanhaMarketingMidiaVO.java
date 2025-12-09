package negocio.comuns.administrativo;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;

/**
 * 
 * @author rodrigo
 */
public class CampanhaMarketingMidiaVO extends SuperVO {

    private Integer codigo;
    private Integer campanhaMarketing;
    private TipoMidiaCaptacaoVO midia;
    public static final long serialVersionUID = 1L;

    public CampanhaMarketingMidiaVO() {
        super();
    }

    public static void validarDados(CampanhaMarketingMidiaVO obj) throws ConsistirException {
        if ((obj.getMidia() == null) || obj.getMidia().getCodigo().intValue() == 0) {
            throw new ConsistirException("O campo MIDIA (Mídia de Captação) deve ser informado.");
        }
    }

    public Integer getCampanhaMarketing() {
        if (campanhaMarketing == null) {
            campanhaMarketing = 0;
        }
        return campanhaMarketing;
    }

    public void setCampanhaMarketing(Integer campanhaMarketing) {
        this.campanhaMarketing = campanhaMarketing;
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

    public TipoMidiaCaptacaoVO getMidia() {
        if (midia == null) {
            midia = new TipoMidiaCaptacaoVO();
        }
        return midia;
    }

    public void setMidia(TipoMidiaCaptacaoVO midia) {
        this.midia = midia;
    }
}
