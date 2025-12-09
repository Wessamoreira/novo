package relatorio.negocio.comuns.biblioteca;

import negocio.comuns.biblioteca.CatalogoVO;
import negocio.comuns.biblioteca.ExemplarVO;

/**
 *
 * @author Carlos
 */
public class EtiquetaCodigoBarraRelVO {

    private CatalogoVO catalogoVO;
    private ExemplarVO exemplarVO;

    public EtiquetaCodigoBarraRelVO() {

    }

    public CatalogoVO getCatalogoVO() {
        if (catalogoVO == null) {
            catalogoVO = new CatalogoVO();
        }
        return catalogoVO;
    }

    public void setCatalogoVO(CatalogoVO catalogoVO) {
        this.catalogoVO = catalogoVO;
    }

    public ExemplarVO getExemplarVO() {
        if (exemplarVO == null) {
            exemplarVO = new ExemplarVO();
        }
        return exemplarVO;
    }

    public void setExemplarVO(ExemplarVO exemplarVO) {
        this.exemplarVO = exemplarVO;
    }

}
