/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package controle.arquitetura;

import java.io.Serializable;

/**
 *
 * @author rodrigo
 */
public class ImagemData implements Serializable {

    public static final long serialVersionUID = 1L;
    Integer Width=110;
    Integer Height=50;
    
    public ImagemData() {
    
    }

    public Integer getHeight() {
        return Height;
    }
    public void setHeight(Integer height) {
        Height = height;
    }
    public Integer getWidth() {
        return Width;
    }
    public void setWidth(Integer width) {
        Width = width;
    }

}
