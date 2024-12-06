package org.phoegasus.springai.part5;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/cookbook")
@RequiredArgsConstructor
class CookbookController {

  private final ChatClient chatClient;
  private final ImageModel imageModel;

  private final ValidRecipeAdvisor validRecipeAdvisor;
  private final SimpleLoggerAdvisor simpleLoggerAdvisor = new SimpleLoggerAdvisor();

  @Value("classpath:/prompts/cookbook/system_prompt.st")
  private Resource systemPrompt;

  @GetMapping
  public String recipe(@RequestParam("ingredient") String ingredient, Model model) {
    Recipe recipe = this.chatClient.prompt()
            .system(systemPrompt)
            .user(ingredient)
            .advisors(validRecipeAdvisor, simpleLoggerAdvisor)
            .call()
            .entity(Recipe.class);

    model.addAttribute("recipe", recipe);

    if (!recipe.isValid()) {
      return "invalid";
    }

    ImagePrompt prompt = new ImagePrompt(recipe.getImagePrompt(),
            OpenAiImageOptions.builder()
                    .withQuality("standard")
                    .withN(1)
                    .withHeight(1024)
                    .withWidth(1024).build());
    String imageUrl = imageModel.call(prompt).getResult().getOutput().getUrl();
    recipe.setImageLink(imageUrl);
    return "recipe";
  }
}
