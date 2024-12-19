//JsonManager
package auto.managers;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This class helps in managing different methods related json like
 * conversion,reading ,writing
 *
 * @author GTIDKE
 *
 */
public class JsonManager {

    /**
     * <p>
     * Description: this method helps in converting the json string into
     * java.util.Map<String,String>
     * </p>
     *
     * @param jsonStr
     * @return
     */
    public static Map<String, String> convertJsonStrToMap(String jsonStr) {
        Map<String, String> map = null;
        if(jsonStr==null)
            return null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            map = mapper.readValue(jsonStr, new TypeReference<Map<String, String>>() {
            });
        } catch (IOException e) {
//            throw new FrameworkException("Invalid Json String "+e.toString());
        }
        return map;
    }



}