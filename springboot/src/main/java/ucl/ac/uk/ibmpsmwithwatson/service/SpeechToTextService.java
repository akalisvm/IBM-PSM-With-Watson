package ucl.ac.uk.ibmpsmwithwatson.service;

import cn.hutool.core.io.resource.ClassPathResource;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.speech_to_text.v1.SpeechToText;
import com.ibm.watson.speech_to_text.v1.model.RecognizeWithWebsocketsOptions;
import com.ibm.watson.speech_to_text.v1.model.SpeechRecognitionResults;
import com.ibm.watson.speech_to_text.v1.websocket.BaseRecognizeCallback;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class SpeechToTextService {

    public String getTextTranscriptFromSpeech(String filename) throws InterruptedException {
        final String APIkey = "G8CReY88aWVwoM5zqaZkn55JktaXkRYHAjq4IEpkZZz3";
        final String URL = "https://api.eu-gb.speech-to-text.watson.cloud.ibm.com/instances/37720f6d-8937-4abf-bcec-fead11212620";
        IamAuthenticator authenticator = new IamAuthenticator.Builder().apikey(APIkey).build();
        SpeechToText speechToText = new SpeechToText(authenticator);
        speechToText.setServiceUrl(URL);
        final Object object = new Object();
        final String[] transcript = {""};
        try {
            ClassPathResource resource = new ClassPathResource("audio" + File.separator + filename);
            RecognizeWithWebsocketsOptions recognizeOptions = new RecognizeWithWebsocketsOptions.Builder()
                    .audio(resource.getStream())
                    .contentType("application/octet-stream")
                    .model("en-US_BroadbandModel")
                    .build();

            BaseRecognizeCallback baseRecognizeCallback = new BaseRecognizeCallback() {
                        @Override
                        public void onTranscription
                                (SpeechRecognitionResults speechRecognitionResults) {
                            // System.out.println(speechRecognitionResults);
                            transcript[0] = speechRecognitionResults.getResults().get(0).getAlternatives().get(0).getTranscript();
                        }
                        @Override
                        public void onTranscriptionComplete() {
                            synchronized (object) {
                                object.notifyAll();
                            }
                        }
                        @Override
                        public void onDisconnected() {
                            System.exit(0);
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
}
