package at.jku.dke.etutor.modules.fd;

import net.minidev.json.JSONObject;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

public class TestDataImport {
    public static void main(String[] args) {
//        importFile("src/main/resources/fd-test-data/keys_1-40.txt");
        importFile("src/main/resources/fd-test-data/Normalform_bestimmen_1-20.txt");
    }

    public static void importFile(String pathname) {

        try
        {
            File file=new File(pathname);
            FileReader fr=new FileReader(file);
            BufferedReader br=new BufferedReader(fr);
            JSONObject completeJson = new JSONObject();


            String line;
            while((line=br.readLine())!=null)
            {
                String [] attribute = line.split("\\t")[1].replaceAll("[( )}]", "").split("");
                completeJson.put("relation", attribute);
                String line2 = br.readLine();
                String [] dependencies = line2.split("\\t")[1].replaceAll("[{}]","").split(", ");
                ArrayList<JSONObject> objectArrayList = new ArrayList<>();
                for (String s: dependencies) {
                    JSONObject dependenciesJson = new JSONObject();
                    String[] dependency = s.split("→ ");
                    String[] leftSide = dependency[0].split(" ");
                    dependenciesJson.put("leftSide", leftSide);
                    String[] rightSide = dependency[1].split(" ");
                    dependenciesJson.put("rightSide", rightSide);
                    objectArrayList.add(dependenciesJson);
                }
                completeJson.put("dependencies", objectArrayList);

                String serviceUrl = "http://localhost:8081/fd/new_exercise";
                HttpClient client = HttpClient.newHttpClient();

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(serviceUrl))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(completeJson.toJSONString()))
                        .build();
                try {
                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            fr.close();

        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
