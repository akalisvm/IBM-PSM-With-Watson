package ucl.ac.uk.ibmpsmwithwatson.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.IdUtil;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.speech_to_text.v1.SpeechToText;
import com.ibm.watson.speech_to_text.v1.model.RecognizeWithWebsocketsOptions;
import com.ibm.watson.speech_to_text.v1.model.SpeechRecognitionResults;
import com.ibm.watson.speech_to_text.v1.websocket.BaseRecognizeCallback;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Service
public class SpeechToTextService {

    static final String APIkey = null;
    static final String URL = null;

    public String getTranscriptByWavFile(MultipartFile file) throws InterruptedException, IOException {
        return getTranscriptByInputStream(file.getInputStream());
    }

    public String getTranscriptByFilename(String filename) throws InterruptedException {
        return getTranscriptByInputStream(new ClassPathResource("audio" + File.separator + filename).getStream());
    }

    public String getTranscriptByInputStream(InputStream inputStream) throws InterruptedException {
        IamAuthenticator authenticator = new IamAuthenticator.Builder().apikey(APIkey).build();
        SpeechToText speechToText = new SpeechToText(authenticator);
        speechToText.setServiceUrl(URL);
        final Object object = new Object();
        final String[] transcript = {""};
        try {
            RecognizeWithWebsocketsOptions recognizeOptions = new RecognizeWithWebsocketsOptions.Builder()
                    .audio(inputStream)
                    .contentType("application/octet-stream")
                    .model("en-US_BroadbandModel")
                    .build();
            BaseRecognizeCallback baseRecognizeCallback = new BaseRecognizeCallback() {
                @Override
                public void onTranscription
                        (SpeechRecognitionResults speechRecognitionResults) {
                    transcript[0] = speechRecognitionResults.getResults().get(0).getAlternatives().get(0).getTranscript();
                }
                @Override
                public void onTranscriptionComplete() {
                    synchronized (object) {
                        object.notifyAll();
                    }
                }
            };
            speechToText.recognizeUsingWebSocket(recognizeOptions, baseRecognizeCallback);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        synchronized (object) {
            object.wait();
        }
        return transcript[0];
    }

    public String uploadWavFile(MultipartFile file) throws IOException {
        String filename = IdUtil.fastSimpleUUID() + "_" + file.getOriginalFilename();
        String rootFilePath = System.getProperty("user.dir") + "/springboot/src/main/resources/audio/" + filename;
        FileUtil.writeBytes(file.getBytes(), rootFilePath);
        return filename;
    }
}
