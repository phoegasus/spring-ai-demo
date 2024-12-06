package org.phoegasus.springai.part8;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@RestController
@RequestMapping("/scheduleAssistant")
@RequiredArgsConstructor
public class ScheduleAssistantController {

  private final ChatClient chatClient;

  @Value("classpath:/prompts/scheduleAssistant/system_prompt.st")
  private Resource systemPrompt;

  @GetMapping
  public String chat(@RequestParam("request") String request) {
    PromptTemplate promptTemplate = new PromptTemplate(systemPrompt);
    String prompt = promptTemplate.render(Map.of("currentDateTime", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
    return chatClient.prompt()
            .user(request)
            .system(prompt)
            .functions("overlappingEventExists", "createEvent", "deleteEvent", "listEvents")
            .call().content();
  }
}