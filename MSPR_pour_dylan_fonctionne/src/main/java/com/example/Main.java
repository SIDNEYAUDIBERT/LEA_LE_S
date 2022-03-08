package com.example;
import org.apache.commons.io.FileUtils;
import java.io.*;
import java.net.URLConnection;
import java.sql.Array;
import java.util.*;
import java.io.IOException;
import java.net.URL;

import static org.apache.commons.io.FileUtils.writeStringToFile;

public class Main {


    public static List<String> users() {
        // Entrer ses données utilisateurs de Github
        java.net.URL url = null;
        String username = "sidney.audibert@gmail.com";
        String password = "Babounez01!";
        String file = "";
        try {
            // Se connecter au Github
            url = new java.net.URL("https://raw.githubusercontent.com/SIDNEYAUDIBERT/MSPR1/main/staff.txt");
            java.net.URLConnection uc;
            uc = url.openConnection();
            uc.setRequestProperty("X-Requested-With", "Curl");
            java.util.ArrayList<String> list = new java.util.ArrayList<String>();
            String userpass = username + ":" + password;
            String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userpass.getBytes()));
            uc.setRequestProperty("Authorization", basicAuth);
            BufferedReader reader = new BufferedReader(new InputStreamReader(uc.getInputStream()));

            // Créer une variable file qui contient toutes les lignes du .txt
            String line = null;
            while ((line = reader.readLine()) != null)
                //System.out.println(line);
                file = file + line + "\n";
        } catch (IOException e) {
            System.out.println("Wrong username and password");
        }


        // Créer un array contenant toutes les lignes de la variable file
        String[] words = file.split("\\s+");
        List<String> wordsList = Arrays.asList(words);

        for (String word: wordsList){
            word = word.replaceAll("[^\\w]", "");
        }
        //System.out.println(Arrays.toString(words));
        return wordsList;
    }

    public static void delete (List<String> listenoms) throws Exception {

        for (String nom : listenoms) {
            File file = new File("src/webpages/"+nom+".html");
            FileUtils.forceDelete(file);
            System.out.println("Fichier " + nom + " détruit");
        }
    }

    public static void generate (String...args) throws Exception {

        String[] names = users().toArray(new String[0]);
        int i = 0;

        //delete(Arrays.asList(names));
        StringBuilder stringBuilder = getStringBuilder(names);

        for (String prenom : names) {

            System.out.println("Création du fichier pour " + prenom);

            // Entrer ses données utilisateurs de Github
            URL url = null;
            String username = "sidney.audibert@gmail.com";
            String password = "Babounez01!";
            String file = "";
            try {
                // Se connecter au Github
                url = new URL("https://raw.githubusercontent.com/SIDNEYAUDIBERT/MSPR1/main/" + prenom + "/" + prenom + ".txt");
                URLConnection uc;
                uc = url.openConnection();
                uc.setRequestProperty("X-Requested-With", "Curl");
                ArrayList<String> list = new ArrayList<String>();
                String userpass = username + ":" + password;
                String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userpass.getBytes()));
                uc.setRequestProperty("Authorization", basicAuth);
                BufferedReader reader = new BufferedReader(new InputStreamReader(uc.getInputStream()));

                // Créer une variable file qui contient toutes les lignes du .txt
                String line = null;
                while ((line = reader.readLine()) != null)
                    //System.out.println(line);
                    file = file + line + "\n";
            } catch (IOException e) {
                System.out.println("Wrong username and password");
            }


            // Créer un array contenant toutes les lignes de la variable file
            String[] words = file.split("\\s+");
            List<String> wordsList = Arrays.asList(words);


            for (String word : wordsList) {
                word = word.replaceAll("[^\\w]", "");
            }
            //System.out.println(Arrays.toString(words));

            // Déterminer le fichier template, créer un nouveau fichier et écrire les données du template dedans
            File htmlTemplateFile = new File("src/webpages/template.html");
            String htmlString = FileUtils.readFileToString(htmlTemplateFile);
            File newHtmlFile = new File("src/webpages/"+prenom+".html");
            writeStringToFile(newHtmlFile, htmlString);
            File indexFile = new File("src/webpages/index.html");

            // ARRAY COMPORTANT LES NOMS DES POINTS DE REPERE DE LA PAGE TEMPLATE
            String[] infosTemplate = new String[]{"$lastname", "$name", "$tools"};

            // CREATION D'UN ARRAY USER
            List<String> user = new ArrayList<>();

            // CREATION D'UN ARRAY EQUIPEMENT CONTENANT TOUTES LES INFOS A PARTIR DE L'INDICE DEUX DE L'ARRAY infosTemplate
            List<String> equipments = new ArrayList(wordsList.subList(2, wordsList.size() - 1));

            // PUSH DES INFOS NOM ET PRENOM DANS L'ARRAY USER
            user.add(wordsList.get(0));
            user.add(wordsList.get(1));

            // Lire les données du fichiers html
            String data = FileUtils.readFileToString(newHtmlFile);
            String dataIndex = FileUtils.readFileToString(indexFile);

            //dataIndex = dataIndex.replace("$links", names3.toString().substring(1, names3.toString().length() - 1));
            dataIndex = dataIndex.replace("$links", stringBuilder.toString());
            dataIndex = dataIndex.replace("$people", Arrays.toString(names));
            writeStringToFile(indexFile, dataIndex);


            // Remplacer les infos du fichiers html par les nouvelles de l'utilisateur
            data = data.replace(infosTemplate[0], user.get(0));
            data = data.replace(infosTemplate[1], user.get(1));
            data = data.replace(infosTemplate[2], equipments.toString().substring(1, equipments.toString().length() - 1));
            data = data.replace("$imgsrc", "https://github.com/SIDNEYAUDIBERT/MSPR1/blob/main/"+prenom+"/"+prenom+".jpg?raw=true");
            writeStringToFile(newHtmlFile, data);
            i++;
        }
    }

    private static StringBuilder getStringBuilder(String[] names) {
        List<String> names3 = new ArrayList<>();
        for (String test : names) {
            test = "<li><a href=\""+test+".html\">"+test+"</a></li>";
            names3.add(test);
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String s: names3){
            stringBuilder.append(s).append(" ");
        }
        return stringBuilder;
    }


    public static void main(String... args) throws Exception {

        generate();



    }
}

