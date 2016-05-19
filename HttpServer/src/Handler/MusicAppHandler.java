package Handler;

import Database.Database;
import Log.Log;
import Send.SendClass;
import Send.SendClassList;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.Date;

public class MusicAppHandler implements HttpHandler {

    private Database db;

    public MusicAppHandler() {

        initiateDatabase();
    }

    @Override
    public void handle(HttpExchange t) throws IOException {

        InputStreamReader isr = new InputStreamReader(t.getRequestBody(),
                "utf-8");
        BufferedReader br = new BufferedReader(isr);

        int b;
        StringBuilder buf = new StringBuilder();
        while ((b = br.read()) != -1) {
            buf.append((char) b);
        }

        br.close();
        String request = buf.toString();
        Date date = new Date();

        Log.i("Handler", "Received:\n" + request);


        SendClassList scl = null;

        try {

            JAXBContext jaxbContext = JAXBContext
                    .newInstance(SendClassList.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            scl = (SendClassList) jaxbUnmarshaller.unmarshal(new StringReader(
                    request));

        } catch (JAXBException e) {

            Log.e("Handler", "Error when converting XML");

        }

        if (scl != null) {

            String groupName = scl.getGroupName();

            if (!groupName.equals("noName")) {

                if (db.isValidGroup(groupName)) {

                    setData(scl);
                    getData(t, scl);

                } else {

                    setData(scl);
                    sendError(t, 200);
                }

            } else {

                sendError(t, 206);

                Log.d("Handler", "No group initiated");

            }

        } else {

            sendError(t, 204);

            Log.d("Handler", "XML class is null");

        }
    }

    public void initiateDatabase() {

        Date date = new Date();

        try {

            Database.initiate();
            db = Database.getInstance();

            if (db.openConnection()) {

                Log.i("Handler","Database started");

            } else {

                Log.e("Handler","Unable to start database");

            }

        } catch (Exception e) {

            Log.e("Handler", "Unable to start database: " + e.getMessage());
            System.exit(1);
        }
    }

    private void sendError(HttpExchange t, int code) {

        Date date = new Date();
        Log.e("Handler","Sending error respons: " + code);

        try {

            String response = "";

            t.sendResponseHeaders(code, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();

        } catch (IOException e) {

            Log.e("Handler","Error when sending response");

        }

    }

    private void setData(SendClassList scl) {

        boolean isAbleToSave = false;
        Date date = new Date();

        String group = null;
        String info = null;
        String instrument = null;
        String volume = null;
        int BPM = 0;
        int bars = 0;
        Boolean hasData = null;

        for (int i = 0; i < scl.getSendClassList().size(); i++) {

            try {

                SendClass tempSC = scl.getSendClassList().get(i);
                info = tempSC.getData();
                instrument = tempSC.getInstrumentName();
                volume = tempSC.getVolume();
                bars = tempSC.getBars();
                hasData = tempSC.getHasData();
                group = scl.getGroupName();
                BPM = scl.getBPM();

            }catch(Exception e){

                Log.e("Handler","Unable to retrieve XML info");

                return;
            }

            if (hasData) {


                isAbleToSave = db.saveData(group, instrument, info, volume, bars,BPM);

            }
        }

        if (isAbleToSave) {

            Log.i("Handler", "Data set for group:" + group);

        } else {

            Log.i("Handler","Data not set for group:" + group);


        }
    }

    private void getData(HttpExchange t, SendClassList scl) throws IOException {


        SendClassList sendList = db.getData(scl.getGroupName());
        Date date = new Date();
        boolean hasValidData = false;

        String response = "";

        if (!sendList.getSendClassList().isEmpty()) {

            hasValidData = true;

            JAXBContext jaxbContext;

            try {

                jaxbContext = JAXBContext.newInstance(SendClassList.class);

                Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

                jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
                        true);

                jaxbMarshaller.marshal(sendList, System.out);
                StringWriter writer = new StringWriter();
                jaxbMarshaller.marshal(sendList, writer);

                response = writer.toString();

            } catch (JAXBException e) {

                Log.e("Handler", "Error when converting XML");

            }

        }

        try {

            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();

            Log.i("Handler", "Sending:\n" + response);

            os.write(response.getBytes());
            os.close();

        } catch (IOException e) {

            Log.e("Handler", "Error when sending response");

        }

        if (hasValidData) {

            Log.i("Handler", "Data found for group:" + sendList.getGroupName());

        } else {

            Log.i("Handler", "Data not found for group:" + sendList.getGroupName());

        }

    }
}
