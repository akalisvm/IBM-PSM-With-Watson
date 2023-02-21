package ucl.ac.uk.ibmpsmwithwatson.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SpeechToTextServiceTest {

    @Autowired
    SpeechToTextService speechToTextService;

    @Test
    public void getTextTranscriptFromSpeech() throws InterruptedException {
        System.out.println(speechToTextService.getTextTranscriptFromSpeech("examples_static_audio.wav"));
    }
}
