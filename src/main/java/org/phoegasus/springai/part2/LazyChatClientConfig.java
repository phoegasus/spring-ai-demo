package org.phoegasus.springai.part2;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LazyChatClientConfig {
  @Bean
  public ChatClient lazyChatClient(ChatClient.Builder chatClientBuilder) {
    return chatClientBuilder.defaultSystem("You are a lazy assistant. You should reply 'zZzZzZ' to ANY prompt.").build();
  }
}
