package org.phoegasus.springai.bonus;

import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.openai.OpenAiAudioSpeechOptions;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.ai.openai.audio.speech.SpeechModel;
import org.springframework.ai.openai.audio.speech.SpeechPrompt;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileOutputStream;
import java.io.IOException;

@RestController
@RequestMapping("/audio")
class AudioController {

  private final SpeechModel speechModel;
  private final OpenAiAudioTranscriptionModel transcriptionModel;

  public AudioController(SpeechModel speechModel, OpenAiAudioTranscriptionModel transcriptionModel) {
    this.speechModel = speechModel;
    this.transcriptionModel = transcriptionModel;
  }

  @GetMapping("/tts")
  public void tts(@RequestParam("text") String text) throws IOException {
    try (FileOutputStream fos = new FileOutputStream("C:\\Users\\Phoegasus\\Desktop\\output.mp3")) {
      SpeechPrompt speechPrompt = new SpeechPrompt(text,
              OpenAiAudioSpeechOptions.builder()
                      .withResponseFormat(OpenAiAudioApi.SpeechRequest.AudioResponseFormat.MP3)
                      .withSpeed(1.0f)
                      .withVoice(OpenAiAudioApi.SpeechRequest.Voice.ONYX)
                      .build());
      fos.write(speechModel.call(speechPrompt).getResult().getOutput());
    }
  }

  @GetMapping("/transcription")
  public String transcription() {
    FileSystemResource fsr = new FileSystemResource("C:\\Users\\Phoegasus\\Desktop\\output.mp3");

    return transcriptionModel.call(new AudioTranscriptionPrompt(fsr, OpenAiAudioTranscriptionOptions.builder()
            .withLanguage("fr")
            .withTemperature(0f)
            .withResponseFormat(OpenAiAudioApi.TranscriptResponseFormat.VTT)
            .build())).getResult().getOutput();

  }
}
