/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.compras;

import negocio.comuns.arquitetura.SuperVO;

public class DevolucaoCompraItemImagemVO extends SuperVO {

    private Integer codigo;
    private Integer devolucaoCompraItem;
    private String nomeImagem;
    private String imagem;
    private String imagemTemp;
    public static final long serialVersionUID = 1L;

    public DevolucaoCompraItemImagemVO() {
        super();
        inicializarDados();
    }

    public void inicializarDados() {
        setCodigo(0);
        setDevolucaoCompraItem(0);
        setImagem("");
        setNomeImagem("");
        setImagemTemp("");
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Integer getDevolucaoCompraItem() {
        return devolucaoCompraItem;
    }

    public void setDevolucaoCompraItem(Integer devolucaoCompraItem) {
        this.devolucaoCompraItem = devolucaoCompraItem;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public String getImagemTemp() {
        return imagemTemp;
    }

    public void setImagemTemp(String imagemTemp) {
        this.imagemTemp = imagemTemp;
    }

    public String getNomeImagem() {
        return nomeImagem;
    }

    public void setNomeImagem(String nomeImagem) {
        this.nomeImagem = nomeImagem;
    }
}
