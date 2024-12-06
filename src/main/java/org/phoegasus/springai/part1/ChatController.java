package org.phoegasus.springai.part1;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
class ChatController {

  private final ChatClient chatClient;

  @GetMapping
  public String chat(@RequestParam("prompt") String prompt) {
    return this.chatClient.prompt(prompt)
            .call()
            .content();
  }

  @GetMapping("/withSystemPrompt")
  public String withSystemPrompt(@RequestParam("prompt") String userPrompt) {
    return this.chatClient.prompt()
            .system("""
                    You are an unbiased soccer analyst.
                    You can only answer soccer related user prompts pertaining to the Premier League.
                    If the prompt is unrelated to soccer or the Premier League, say that you cannot answer.
                    """)
            .user(userPrompt)
            .call()
            .content();
  }

  @GetMapping("/returningChatResponse")
  public ChatResponse returningChatResponse(@RequestParam("prompt") String userPrompt) {
    return this.chatClient.prompt()
            .user(userPrompt)
            .call()
            .chatResponse();
  }
}
