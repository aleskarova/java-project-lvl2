package hexlet.code;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

public class Differ {

    public static String generate(String filepath1, String filepath2) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonGenerator.Feature.QUOTE_FIELD_NAMES, false);

        Map<String, Object> document1 = mapper.readValue(new File(filepath1),
                new TypeReference<Map<String, Object>>() {});
        Map<String, Object> document2 = mapper.readValue(new File(filepath2),
                new TypeReference<Map<String, Object>>() {});

        List<String> keys = mergeKeys(document1, document2);

        Map<String, Object> result = new LinkedHashMap<>();
        for (String key : keys) {
            if (!document2.containsKey(key)) {
                result.put("- " + key, document1.get(key));
            } else if (!document1.containsKey(key)) {
                result.put("+ " + key, document2.get(key));
            } else {
                Object value1 = document1.get(key);
                Object value2 = document2.get(key);
                if (value1.equals(value2)) {
                    result.put("  " + key, value1);
                } else {
                    result.put("- " + key, value1);
                    result.put("+ " + key, value2);
                }
            }
        }
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(result);
    }

    private static <V> List<String> mergeKeys(Map<String, V> map1, Map<String, V> map2) {
        Set<String> keySet = new HashSet<>(map1.keySet());
        keySet.addAll(map2.keySet());
        return  keySet.stream().sorted().toList();
    }

}
