package ucl.ac.uk.ibmpsmwithwatson.util;

import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import cn.hutool.json.JSONUtil;
import org.springframework.boot.web.client.RestTemplateBuilder;
import ucl.ac.uk.ibmpsmwithwatson.config.BangDBConfig;
import ucl.ac.uk.ibmpsmwithwatson.dao.GraphMapper;
import ucl.ac.uk.ibmpsmwithwatson.dao.TableMapper;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class insertDataFromTxt {

    private final static String[] USER_ATTR = new String[]{"id",
            "given_name",
            "family_name",
            "email",
            "phone_number",
            "password",
            "role",
            "gender",
            "birthdate",
            "nhs_number",
            "doctor",
            "questionnaire"
    };

    public static void insertDataToTable(String table, String filename, String[] attr) {
        try {
            TableMapper tableMapper = new TableMapper(new BangDBConfig(), new RestTemplateBuilder());
            ClassPathResource classPathResource = new ClassPathResource("static" + File.separator + filename);
            InputStream inputStream = classPathResource.getStream();
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader br = new BufferedReader(reader);
            String line = br.readLine();
            Digester md5 = new Digester(DigestAlgorithm.MD5);
            while(line != null) {
                String[] split = line.split(";");
                Map<String, String> map = new LinkedHashMap<>();
                for(int i = 0; i < attr.length; i++) {
                    if(attr[i].equals("password")) {
                        map.put(attr[i], md5.digestHex(split[i].trim()));
                    } else {
                        map.put(attr[i], split[i].trim());
                    }
                }
                tableMapper.insertData(table,
                        String.valueOf(tableMapper.getRetvalCount(table) + 1),
                        JSONUtil.toJsonStr(map));
                line = br.readLine();
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void insertUserDataToTable() {
        insertDataToTable("mygraph", "user.txt", USER_ATTR);
    }

    public static void insertDataToGraph(String filename, String[] attr, String label) {
        try {
            TableMapper tableMapper = new TableMapper(new BangDBConfig(), new RestTemplateBuilder());
            GraphMapper graphMapper = new GraphMapper(new BangDBConfig(), new RestTemplateBuilder());
            ClassPathResource classPathResource = new ClassPathResource("static" + File.separator + filename);
            InputStream inputStream = classPathResource.getStream();
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader br = new BufferedReader(reader);
            String line = br.readLine();
            Digester md5 = new Digester(DigestAlgorithm.MD5);
            while(line != null) {
                String[] split = line.split(";");
                Map<String, String> map = new LinkedHashMap<>();
                for(int i = 0; i < split.length; i++) {
                    if(attr[i].equals("password")) {
                        map.put(attr[i], md5.digestHex(split[i].trim()));
                    } else {
                        map.put(attr[i], split[i].trim());
                    }
                }
                for(int i = split.length; i < attr.length; i++) {
                    map.put(attr[i], "");
                }
                graphMapper.addNode(label,
                        label.toLowerCase() + "_" + (tableMapper.getRetvalCount(label) + 1),
                        JSONUtil.toJsonStr(map));
                line = br.readLine();
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void insertUserDataToGraph() {
        insertDataToGraph("user.txt", USER_ATTR, "User");
    }

    public static void main(String[] args) {
        insertUserDataToGraph();
    }
}
