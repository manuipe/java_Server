package application;

import java.io.*;
import java.net.Socket;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class EchoThread extends Thread {
    protected Socket socket;
    public EchoThread(Socket clientSocket) {
        this.socket = clientSocket;
    }
    public void run() {
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream(), "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder out = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                out.append(line + "\n");
                //System.out.println(line);
            }
            String clientString = out.toString();
            bufferedReader.close();

            String path = System.getProperty("user.home") + File.separator + "Invoices";

            File directory = new File(path);
            if (!directory.exists()) {
                directory.mkdir();
            }

            if (clientString.length() > 0) {
                //System.out.println("inside if line 35");

                ZonedDateTime zdt = ZonedDateTime.now();
                String time = zdt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                time = time.replaceAll(":", "_");

                String saveAs = null;

                if (clientString.startsWith("{")) {
                    saveAs = ".json";
                } else if (clientString.startsWith("<")) {
                    saveAs = ".xml";
                } else {
                    saveAs = ".txt";
                }

                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path + File.separator + time + saveAs, true));

                // create an array of char that contains all characters of the invoice
                char[] charArray = clientString.toCharArray();
                //for each char in the array, append it to the file
                for (char c : charArray) {
                    bufferedWriter.append(c);
                    bufferedWriter.flush();
                }

                bufferedWriter.close();

                System.out.println("File saved as: " + saveAs);

            }
        } catch (IOException e) {
            System.out.print(e);
            return;
        }
    }
}