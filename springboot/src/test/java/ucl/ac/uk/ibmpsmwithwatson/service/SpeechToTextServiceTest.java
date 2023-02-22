package ucl.ac.uk.ibmpsmwithwatson.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SpeechToTextServiceTest {

    @Autowired
    SpeechToTextService speechToTextService;

    @Test
    public void getTextTranscriptFromSpeech() throws Exception {
        System.out.println(speechToTextService.getTranscriptByFilename("examples_static_audio.wav"));
    }
}
