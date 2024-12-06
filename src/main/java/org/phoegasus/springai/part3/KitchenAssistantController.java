package org.phoegasus.springai.part3;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/kitchenAssistant")
@RequiredArgsConstructor
class KitchenAssistantController {

  private final ChatClient chatClient;

  @Value("classpath:/prompts/kitchenAssistant/system_prompt.st")
  private Resource systemPrompt;
  @Value("classpath:/prompts/kitchenAssistant/user_prompt.st")
  private Resource userPrompt;

  @GetMapping("/dessert")
  public Recipe dessert(@RequestParam("ingredient") String ingredient) {
    Recipe dessert = this.chatClient.prompt()
            .system(systemPrompt)
            .user(ingredient)
            .call()
            .entity(Recipe.class);
    System.out.println(dessert.getName());
    System.out.println(dessert.getIngredients());
    return dessert;
  }

  @GetMapping("/entree")
  public Recipe entree(@RequestParam("ingredient") String ingredient) {
    PromptTemplate template = new PromptTemplate(userPrompt);
    Prompt prompt = template.create(Map.of("ingredient", ingredient));
    return this.chatClient.prompt(prompt)
            .call()
            .entity(Recipe.class);
  }
}
