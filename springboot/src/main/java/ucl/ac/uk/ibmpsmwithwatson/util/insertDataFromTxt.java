package ucl.ac.uk.ibmpsmwithwatson.util;

import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.json.JSONUtil;
import org.springframework.boot.web.client.RestTemplateBuilder;
import ucl.ac.uk.ibmpsmwithwatson.config.BangDBConfig;
import ucl.ac.uk.ibmpsmwithwatson.dao.TableMapper;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class insertDataFromTxt {

    private final static String[] USER_ATTR = new String[]{"given_name", "family_name", "email", "phone_number", "password", "role", "gender", "birthdate", "address", "nhs_number", "doctor"};

    public static void insertData(String table, String filename, String[] attr) {
        try {
            ClassPathResource classPathResource = new ClassPathResource("static" + File.separator + filename);
            InputStream inputStream = classPathResource.getStream();
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader br = new BufferedReader(reader);
            String line = br.readLine();
            while(line != null) {
                String[] split = line.split(";");
                Map<String, String> map = new HashMap<>();
                for(int i = 0; i < 11; i++) {
                    map.put(attr[i], split[i].trim());
                }
                TableMapper mapper = new TableMapper(new BangDBConfig(), new RestTemplateBuilder());
                mapper.insertData(table, String.valueOf(mapper.getCount(table) + 1), JSONUtil.toJsonStr(map));
                line = br.readLine();
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void insertUserData() {
        insertData("user", "user.txt", USER_ATTR);
    }

    public static void main(String[] args) {
        insertUserData();
    }
}
