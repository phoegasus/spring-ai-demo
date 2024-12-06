package org.phoegasus.springai.part2;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/chat")
class AdvancedChatController {

  private final ChatClient chatClient;
  private final ChatClient lazyChatClient;

  private final SafeGuardAdvisor safeGuardAdvisor = new SafeGuardAdvisor(List.of("perfume", "medicine"));
  private final PromptChatMemoryAdvisor promptChatMemoryAdvisor = new PromptChatMemoryAdvisor(new InMemoryChatMemory());

  AdvancedChatController(@Qualifier("chatClient") ChatClient chatClient, @Qualifier("lazyChatClient") ChatClient lazyChatClient) {
    this.chatClient = chatClient;
    this.lazyChatClient = lazyChatClient;
  }

  @GetMapping("/lazy")
  public String lazy(@RequestParam("prompt") String prompt) {
    return this.lazyChatClient.prompt(prompt)
            .call()
            .content();
  }

  @GetMapping("/sfw")
  public String safeForWork(@RequestParam("subject") String subject) {
    return this.chatClient.prompt()
            .user("Write a short poem about {subject}.".replace("{subject}", subject))
            .advisors(safeGuardAdvisor)
            .call()
            .content();
  }

  @GetMapping("/withMemory")
  public String withMemory(@RequestParam("prompt") String prompt) {
    return this.chatClient.prompt(prompt)
            .advisors(promptChatMemoryAdvisor)
            .call()
            .content();
  }
}
