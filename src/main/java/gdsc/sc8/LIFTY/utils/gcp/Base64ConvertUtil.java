package gdsc.sc8.LIFTY.utils.gcp;

import gdsc.sc8.LIFTY.exception.ErrorStatus;
import gdsc.sc8.LIFTY.exception.model.NotFoundException;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;

@Component
public class Base64ConvertUtil {
    public String uriToBase64(String uri) {
        try{
            URL imageUrl = new URL(uri);
            URLConnection conn = imageUrl.openConnection();
            InputStream is = conn.getInputStream();
            ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int read;
            while ((read = is.read(buffer, 0, buffer.length)) != -1) {
                byteOutput.write(buffer, 0, read);
            }
            byteOutput.flush();
            return Base64.getEncoder().encodeToString(byteOutput.toByteArray());
        }catch (IOException e){
            throw new NotFoundException(ErrorStatus.NO_IMAGE, ErrorStatus.NO_IMAGE.getMessage());
        }
    }
}
