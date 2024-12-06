package org.phoegasus.springai.part6;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/expert")
class ExpertController {

  private final ChatClient chatClient;
  private final VectorStore specStore;

  @Value("classpath:/prompts/expert/prompt.st")
  private Resource promptResource;

  public ExpertController(ChatClient chatClient, @Qualifier("specStore") VectorStore specStore) {
    this.chatClient = chatClient;
    this.specStore = specStore;
  }

  @GetMapping
  public String ask(@RequestParam("prompt") String userPrompt) {
    List<Document> similarDocs = specStore.similaritySearch(SearchRequest.query(userPrompt).withTopK(4));
    List<String> contentList = similarDocs.stream().map(Document::getContent).toList();

    PromptTemplate promptTemplate = new PromptTemplate(promptResource);

    Map<String, Object> params = new HashMap<>();
    params.put("input", userPrompt);
    params.put("documents", String.join("\n", contentList));

    Prompt prompt = promptTemplate.create(params);

    return chatClient.prompt(prompt).call().content();
  }
}
