package org.phoegasus.springai.part4;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
class ImageController {

  private final ImageModel imageModel;

  @GetMapping
  public String image() {
    String promptText = """
            A plate of colorful homemade Pop-Tarts with various fruit fillings,
            decorated with icing and sprinkles, set on a rustic wooden table.
            """;
    ImagePrompt prompt = new ImagePrompt(promptText,
            OpenAiImageOptions.builder()
                    .withQuality("hd")
                    .withN(1)
                    .withHeight(1024)
                    .withWidth(1024).build());
    return imageModel.call(prompt).getResult().getOutput().getUrl();
  }
}
