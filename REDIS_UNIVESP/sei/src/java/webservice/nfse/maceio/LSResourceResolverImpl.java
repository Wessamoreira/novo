package webservice.nfse.maceio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

import negocio.comuns.utilitarias.UteisJSF;

public class LSResourceResolverImpl implements LSResourceResolver {
	@Override
    public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI) {
 
        LSInputImpl input = new LSInputImpl();
        InputStream stream;
		try {
			stream = new FileInputStream(UteisJSF.getCaminhoPastaWeb() + "/resources/nfse/xmldsig-core-schema20020212.xsd");
			input.setPublicId(publicId);
			input.setSystemId(systemId);
			input.setBaseURI(baseURI);
			input.setCharacterStream(new InputStreamReader(stream));
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
        return input;
    }
}