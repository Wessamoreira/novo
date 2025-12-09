/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package negocio.comuns.utilitarias.faturamento.nfe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

/**
 *
 * @author Euripedes Doutor
 */
public class SalvarXMLNFeSemAssinaturaDigital {

    public SalvarXMLNFeSemAssinaturaDigital(Document nfe, File facesConfigIniFile) throws FileNotFoundException, TransformerConfigurationException, TransformerException {
        OutputStream os = new FileOutputStream(facesConfigIniFile);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer trans = tf.newTransformer();
        trans.transform(new DOMSource(nfe), new StreamResult(os));
    }

  
}
